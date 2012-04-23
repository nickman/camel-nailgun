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

import static org.jboss.netty.channel.Channels.pipeline;

import org.helios.nailgun.codecs.NailgunRequestDecoder;
import org.helios.nailgun.codecs.NailgunRequestDispatcher;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

/**
 * <p>Title: NailgunServerPipelineFactory</p>
 * <p>Description: A server pipeline factory for the nailgun server</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.NailgunServerPipelineFactory</code></p>
 */

public class NailgunServerPipelineFactory implements ChannelPipelineFactory {
	/** The request dispatcher */
	protected final NailgunRequestDispatcher requestDispatcher = new NailgunRequestDispatcher();
	/** The request executor which hands off the request to be processed by another thread */
	protected final ExecutionHandler executionHandler = new ExecutionHandler(
            new OrderedMemoryAwareThreadPoolExecutor(16, 1048576, 1048576));
	/** The shareable nailgun request decoder */
	protected final NailgunRequestDecoder requestDecoder = new NailgunRequestDecoder();
	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		//pipeline.addLast("nailgun-logger", new LoggingHandler(InternalLogLevel.INFO, true));
		//pipeline.addLast("nailgun-logger", new LoggingHandler(InternalLogLevel.INFO, false));
		//pipeline.addLast("nailgun-logger", new LoggingHandler(InternalLogLevel.INFO, false));
		pipeline.addLast("nailgun-decoder", requestDecoder);
		pipeline.addLast("nailgun-executor", executionHandler);
		pipeline.addLast("nailgun-dispatcher", requestDispatcher);
		return pipeline;
	}

}
