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

import java.util.LinkedHashMap;

import org.jboss.netty.channel.ChannelHandler;

/**
 * <p>Title: StreamUnmarshallingCommandHandler</p>
 * <p>Description: An extension of {@link CommandHandler} that can process the STDIN from a nailgun client
 * and provides a customized pipeline of decoding {@link ChannelHandler}s to unmarshall the stream into a different form.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.StreamUnmarshallingCommandHandler</code></p>
 * @param <T> The type that is expected to unmarshalled from the STDIN stream
 */

public interface StreamUnmarshallingCommandHandler<T> extends CommandHandler {
	/**
	 * Returns a map of channel handlers keyed by name 
	 * @return a map of channel handlers keyed by name 
	 */
	public LinkedHashMap<String, ChannelHandler> getPipeline();
	
	/**
	 * Called when this command handler's pipeline unmarshalls an event from the STDIN stream
	 * @param event The unmarshalled event
	 */
	public void onUnmarshalledEvent(T event);
	

}
