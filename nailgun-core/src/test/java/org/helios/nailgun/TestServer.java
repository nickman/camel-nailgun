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
package org.helios.nailgun;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.helios.nailgun.codecs.NailgunRequestDecoder;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
/**
 * <p>Title: TestServer</p>
 * <p>Description: A quickie nailgun test server</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.TestServer</code></p>
 */

public class TestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log("NailGun Test Server");
		// Set the logging level for the stream handler
		Logger log = Logger.getLogger(NailgunRequestDecoder.class.getName());
		log.setLevel(Level.FINE);
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINE);
		log.addHandler(handler);

		
		log.fine("Logging Configured");
		
		//LogManager.getLogManager().getLogger(NailgunRequestDecoder.class.getName()).setLevel(Level.FINEST);
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new NailgunServerPipelineFactory());
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.receiveBufferSize", 1048576);
		 
		// Bind and start to accept incoming connections.
		InetSocketAddress isock = new InetSocketAddress("0.0.0.0", NailgunConstants.DEFAULT_PORT);
		bootstrap.bind(isock);
		System.out.println("Nailgun Server Started on [" + isock + "]");		
	}
	
	public static void log(Object msg) {
		System.out.println(msg);
	}

}
