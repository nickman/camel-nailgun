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
package org.helios.nailgun.handlers;

import org.helios.nailgun.NailgunRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

/**
 * <p>Title: NailgunStreamHandler</p>
 * <p>Description: The Netty channel handler that decodes a nailgun request</p> 
 * <p>Based in great part on:<ol>
 * 	<li><b><code>com.martiansoftware.nailgun.NGSession</code></b> by <a href="http://www.martiansoftware.com/contact.html">Marty Lamb</a></li>
 *  <li><b><code>com.biasedbit.nettytutorials.customcodecs.common.Decoder</code></b> by <a href="http://biasedbit.com/about/">Bruno Decarvalho</a></li>
 * </ol>
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handlers.NailgunStreamHandler</code></p>
 */

public class NailgunStreamHandler extends ReplayingDecoder<DecodingState>  {
	/** The request created */
	private NailgunRequest message;
	/** byte array buffer */
	private byte[] bytes = null;
	/** The type decoding state */
	private DecodingState ds = null;
	/** The current chunk type */
	private byte chunkType;
	
	/**
	 * Creates a new NailgunStreamHandler
	 */
	public NailgunStreamHandler() {
		reset();
	}

	/**
	 * Resets the decoder
	 */
	private void reset() {
		cleanup();
        checkpoint(DecodingState.BYTES);
        this.message = new NailgunRequest();
    }


	
	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.handler.codec.replay.ReplayingDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer, java.lang.Enum)
	 */
	@Override
	protected NailgunRequest decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, DecodingState state) throws Exception {
		
		switch(state) {
			case BYTES:
				message.setBytesToRead(buffer.readInt());
				checkpoint(DecodingState.TYPE);
			case TYPE:
				chunkType = buffer.readByte();
				ds = DecodingState.getState(chunkType);
				if(ds==null) {
					throw new Exception("Invalid chunk type [" + chunkType + "]", new Throwable());
				}
				checkpoint(ds);										
			case WORKING_DIR:
				bytes = new byte[message.getBytesToRead()];
				buffer.readBytes(bytes);
				message.setWorkingDirectory(new String(bytes));
				checkpoint(DecodingState.BYTES);
			case ENVIRONMENT:
				bytes = new byte[message.getBytesToRead()];
				buffer.readBytes(bytes);
				message.addToEnvironment(new String(bytes));
				checkpoint(DecodingState.BYTES);
			case ARGUMENTS:
				bytes = new byte[message.getBytesToRead()];
				buffer.readBytes(bytes);
				message.addArgument(new String(bytes));
				checkpoint(DecodingState.BYTES);				
			case COMMAND:
				bytes = new byte[message.getBytesToRead()];
				buffer.readBytes(bytes);
				message.setCommand(new String(bytes));
				break;
			default:
				throw new IllegalStateException("Unhandled Decoding State: [" + state + "]", new Throwable());
		}
		try {
            return this.message;
        } finally {
            this.reset();
        }		
	}
	
	 /**
	 * Cleans up any remaining state
	 */
	protected void cleanup() {
		bytes = null;
		ds = null;
		chunkType = -10;
	}

}
