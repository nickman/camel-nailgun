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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalServerChannelFactory;

/**
 * <p>Title: CommandHandlerServer</p>
 * <p>Description: A local channel Netty server where command handlers are registered and pipelines are created to process incoming nailgun requests</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.CommandHandlerServer</code></p>
 */

public class CommandHandlerServer {
	/** The singleton instance */
	private static volatile CommandHandlerServer instance = null;
	/** The singleton instance ctor lock */
	private static final Object lock = new Object();
	/** The command handler map */
	protected final Map<String, CommandHandler> commandHandlers = new ConcurrentHashMap<String, CommandHandler>();
	/** The server bootstrap */
	protected final ServerBootstrap serverBootstrap;
	/**
	 * Acquires the {@link CommandHandlerServer} singleton instance
	 * @return the {@link CommandHandlerServer} singleton instance
	 */
	public static CommandHandlerServer getInstance() {
		if(instance==null) {
			synchronized(lock) {
				if(instance==null) {
					instance = new CommandHandlerServer();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Creates a new CommandHandlerServer
	 */
	private CommandHandlerServer() {
		serverBootstrap = new ServerBootstrap();
	}
}
