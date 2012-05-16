/**
 * Helios, OpenSource Monitoring
 * Brought to you by the Helios Development Group
 *
 * Copyright 2007, Helios Development Group and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 *
 */
package org.helios.nailgun.handler.server;

import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.helios.nailgun.NailgunRequest;
import org.helios.nailgun.codecs.NailgunRequestDecoder;

/**
 * <p>Title: LocalRequestRelay</p>
 * <p>Description: A singleton service to locally queue instances of {@link NailgunRequest}s so that they can be passed
 * from the {@link NailgunRequestDecoder} to a {@link CommandHandler} instance without serializing it.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.LocalRequestRelay</code></p>
 */

public class LocalRequestRelay {
	/** The singleton instance */
	private volatile static  LocalRequestRelay instance = null;
	/** The singleton instance ctor lock */
	private static final Object lock = new Object();
	/** A reference queue so we can count the number of gc'ed requests */
	private final ReferenceQueue<NailgunRequest> refQueue = new ReferenceQueue<NailgunRequest>();
	/** A map of nailgun requests keyed by a sequence ID */
	private final Map<Long, TriggeredSoftReference<NailgunRequest>> queueMap = new ConcurrentHashMap<Long, TriggeredSoftReference<NailgunRequest>>();
	/** The sequence ID generator */
	private final AtomicLong sequence = new AtomicLong(0L);
	/** The counter of enqueued requests*/
	private final AtomicLong enqueueCounter = new AtomicLong(0L);
	
	
	/**
	 * Acquires the LocalRequestRelay singleton instance
	 * @return the LocalRequestRelay singleton instance
	 */
	public static LocalRequestRelay getInstance() {
		if(instance==null) {
			synchronized(lock) {
				if(instance==null) {
					instance = new LocalRequestRelay();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Places a new request into the relay
	 * @param request The request to relay
	 * @return the ID key of the relayed request
	 */
	public long put(NailgunRequest request) {
		if(request==null) throw new IllegalArgumentException("The passed request was null", new Throwable());
		final long key = sequence.incrementAndGet();
		queueMap.put(key, new TriggeredSoftReference<NailgunRequest>(request, refQueue, new Runnable(){
			public void run() {
				if(queueMap.remove(key)!=null) {
					enqueueCounter.incrementAndGet();
				}
			}
		}));
		return key;
	}
	
	/**
	 * Retrieves the request with the designated key
	 * @param key The request key
	 * @return The keyed request or null
	 */
	public NailgunRequest get(long key) {
		TriggeredSoftReference<NailgunRequest> ref = (TriggeredSoftReference<NailgunRequest>)queueMap.remove(key);
		NailgunRequest request = null;
		if(ref!=null) {
			request = ref.get();
			ref.clear();
		}		
		return request;
	}
	
	/**
	 * Creates a new LocalRequestRelay
	 */
	private LocalRequestRelay() {
		Thread t = new Thread(new Runnable(){
			public void run() {
				while(true) {
					try {
						TriggeredSoftReference<NailgunRequest> tsr = (TriggeredSoftReference<NailgunRequest>)refQueue.remove();
						tsr.run();
					} catch (Throwable t) {}
				}
			}
		}, "LocalRequestRelayReaperThread");
		t.setDaemon(true);
		t.start();
	}
	
	
}
