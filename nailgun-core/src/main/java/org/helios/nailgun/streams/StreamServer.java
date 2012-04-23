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
package org.helios.nailgun.streams;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;

/**
 * <p>Title: StreamServer</p>
 * <p>Description: Local netty server that bridges streams from the ng client to handlers interested in processing STDIN from the client and sending STDOUT, STDERR and exit codes to the ng client.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.streams.StreamServer</code></p>
 */
public class StreamServer {
	/** The local address of the StreamServer */
	public static final String LOCAL_STREAMS_ADDRESS = "NGStreamServer";
	/** The address impl for the StreamServer */
	protected final LocalAddress localAddress = new LocalAddress(LOCAL_STREAMS_ADDRESS);
	/** The server bootstrap */
	protected final ServerBootstrap serverBootstrap;
	/** The singleton instance */
	protected static volatile StreamServer instance = null;
	/** The singleton instance ctor lock */
	protected static final Object lock = new Object();
	
	/**
	 * Acquires the stream server singleton instance
	 * @return the stream server singleton instance
	 */
	public static StreamServer getInstance() {
		if(instance==null) {
			synchronized(lock) {
				if(instance==null) {
					instance = new StreamServer();
				}
			}
		} 
		return instance;
	}
	
	
	/**
	 * Creates a new StreamServer
	 */
	private StreamServer() {
		serverBootstrap = new ServerBootstrap(new DefaultLocalServerChannelFactory());		
	}
	
	
	public void startNgStream
	
}
