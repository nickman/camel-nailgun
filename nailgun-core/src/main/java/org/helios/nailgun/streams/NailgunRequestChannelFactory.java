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

import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalServerChannel;

/**
 * <p>Title: NailgunRequestChannelFactory</p>
 * <p>Description: A factory for {@link NailgunRequestChannel}s</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.streams.NailgunRequestChannelFactory</code></p>
 */

public class NailgunRequestChannelFactory extends DefaultLocalServerChannelFactory {
	/** The factory that will create channels for this server channel factory */
	protected final ChannelPipelineFactory pipelineFactory;
	/**
	 * Creates a new NailgunRequestChannelFactory
	 * @param pipelineFactory The factory that will create channels for this server channel factory
	 */
	public NailgunRequestChannelFactory(ChannelPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	/**
	 * Creates a new channel using the configured pipeline factory
	 * @return a new LocalServerChannel
	 * @throws Exception
	 */
	public LocalServerChannel newChannel() throws Exception {
		return new NailgunRequestChannel(super.newChannel(pipelineFactory.getPipeline()));
	}
}
