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
import java.lang.ref.SoftReference;

/**
 * <p>Title: TriggeredSoftReference</p>
 * <p>Description: A {@link SoftReference} extension that supports a specified identity for the reference and a {@link Runnable} that will be executed 
 * if and when the reference is queued.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.TriggeredSoftReference</code></p>
 * @param <V> The type of the soft reference
 */

public class TriggeredSoftReference<V> extends SoftReference<V> {
	/** The runnable executed when the reference is enqueued */
	private final Runnable enqueueTask;
	
	/**
	 * Creates a new TriggeredSoftReference
	 * @param referent The reference value
	 * @param q The reference queue
	 * @param enqueueTask The runnable to execute when the reference is queued
	 */
	public TriggeredSoftReference(V referent, ReferenceQueue<? super V> q, Runnable enqueueTask) {
		super(referent, q);
		this.enqueueTask = enqueueTask;
	}
	
	/**
	 * Executes the configured runnable.
	 */
	public void run() {
		if(enqueueTask!=null) {
			enqueueTask.run();
		}
	}

}
