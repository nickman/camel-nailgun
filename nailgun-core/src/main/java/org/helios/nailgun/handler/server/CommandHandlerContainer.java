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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.helios.nailgun.NailgunRequest;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.jboss.netty.channel.local.LocalServerChannelFactory;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * <p>Title: CommandHandlerContainer</p>
 * <p>Description: A container for a command handling service.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.CommandHandlerContainer</code></p>
 */

public class CommandHandlerContainer {
	/** Instance logger */
	protected final InternalLogger log = InternalLoggerFactory.getInstance(getClass());
	/** The command handler */
	protected final CommandHandler commandHandler;
	/** The local server bootstrap */
	protected final ServerBootstrap serverBootstrap;
	/** The local server channel factory */
	protected final LocalServerChannelFactory serverChannelFactory;
	/** The local addresses allocated to serve the command handler */
	protected final Set<LocalAddress> localAddresses = new HashSet<LocalAddress>();
	/** The command channel handler */
	protected final NailgunRequestChannelHandler requestChannelHandler;
	/** Static set of registered command names */
	protected final Set<String> registeredCommands = new CopyOnWriteArraySet<String>();
	
	
	/**
	 * Creates a new CommandHandlerContainer
	 * @param commandHandler the command handler 
	 */
	public CommandHandlerContainer(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
		requestChannelHandler = new NailgunRequestChannelHandler(this.commandHandler);
		serverChannelFactory =  new DefaultLocalServerChannelFactory();
		serverBootstrap = new ServerBootstrap(serverChannelFactory);
		for(String commandName: this.commandHandler.getCommandNames()) {
			if(commandName==null || commandName.trim().isEmpty()) continue;
			commandName = commandName.trim();
			boolean registered = false;
			if(!registeredCommands.contains(commandName)) {
				synchronized(registeredCommands) {
					if(!registeredCommands.contains(commandName)) {
						registeredCommands.add(commandName);
						LocalAddress localAddress = new LocalAddress(commandName);
						localAddresses.add(localAddress);
						serverBootstrap.bind(localAddress);
						
						serverBootstrap.getPipeline().addFirst(commandName + "Handler", requestChannelHandler);
						registered = true;
						log.info("Registered CommandHandler for [" + commandName + "]");
					}
				}
			}
			if(!registered) {
				log.warn("Duplicate CommandHandler [" + getClass().getName() + "] Detected for [" + commandName + "]");
				continue;
			}
		}
	}
	
	public boolean supportsStreaming() {
		return false;
	}
	
	
	/**
	 * <p>Title: NailgunRequestChannelHandler</p>
	 * <p>Description: An upstream handler, placed first in the pipeline that hands off the decoded nailgun request to the command handler</p> 
	 * <p>Company: Helios Development Group LLC</p>
	 * @author Whitehead (nwhitehead AT heliosdev DOT org)
	 * <p><code>org.helios.nailgun.handler.server.CommandHandlerContainer.NailgunRequestChannelHandler</code></p>
	 */
	private static class NailgunRequestChannelHandler extends  SimpleChannelUpstreamHandler {
		/** A simle command handler */
		final CommandHandler handler;

		/**
		 * Creates a new NailgunRequestChannelHandler
		 * @param handler The simple command handler
		 */
		public NailgunRequestChannelHandler(CommandHandler handler) {
			super();
			this.handler = handler;
		}

		/**
		 * {@inheritDoc}
		 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			Object msg = ((UpstreamMessageEvent)e).getMessage();
			if(msg instanceof NailgunRequest) {
				handler.onNailgunRequest((NailgunRequest)msg);
			} else {
				super.messageReceived(ctx, e);
			}
		}
	}

}
