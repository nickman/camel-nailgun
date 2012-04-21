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

import java.net.InetSocketAddress;

import org.helios.nailgun.DefaultNailgunRequestImpl;
import org.helios.nailgun.NailgunConstants;
import org.helios.nailgun.NailgunRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * <p>Title: NailgunStreamHandler</p>
 * <p>Description: The Netty channel handler that decodes a nailgun request</p> 
 * <p>Based in great part on:<ol>
 * 	<li><b><code>com.martiansoftware.nailgun.NGSession</code></b> by <a href="http://www.martiansoftware.com/contact.html">Marty Lamb</a></li>
 *  <li><b><code>com.biasedbit.nettytutorials.customcodecs.common.Decoder</code></b> by <a href="http://biasedbit.com/about/">Bruno Decarvalho</a></li>
 * </ol>
 * <p>The nailgun stream processor does not rely on a particular in the way that requests are sent, but as of this writing, the order is this:<ol>
 * 	<li><b>ARGUMENTS</b></li>
 * 	<li><b>ENVIRONMENT</b></li>
 * 	<li><b>WORKING_DIR</b></li>
 * 	<li><b>COMMAND</b></li>
 *  </ol></p>
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handlers.NailgunStreamHandler</code></p>
 */

public class NailgunStreamHandler extends ReplayingDecoder<DecodingState>  {
	/** The internal logger */
	protected final InternalLogger log = InternalLoggerFactory.getInstance(getClass());
	/** The request created */
	private DefaultNailgunRequestImpl message;
	/** byte array buffer */
	private byte[] bytes = null;
	/** The type decoding state */
	private DecodingState state = null;	
	/** The current chunk type */
	private byte chunkType;
	/** The number of bytes to read in the next event */
	private int bytesToRead;
	
	
	
	/** The signal to send the client when we're ready to handle the stream input */
	private static final ChannelBuffer STREAM_IN_READY = ChannelBuffers.buffer(5);

	
	
	
	static {
		// Prep the stream in readiness buffer
		STREAM_IN_READY.writeInt(0);
		STREAM_IN_READY.writeByte(NailgunConstants.CHUNKTYPE_STARTINPUT);
	}
	
	/**
	 * Creates a new NailgunStreamHandler
	 */
	public NailgunStreamHandler() {
		if(log.isDebugEnabled()) log.debug("Created NailgunStreamHandler Instance");
		//System.out.println("Created NailgunStreamHandler Instance");
		reset();
	}

	/**
	 * Resets the decoder
	 */
	private void reset() {
		if(log.isDebugEnabled()) log.debug("NailgunStreamHandler Reset");
		cleanup();
        checkpoint(DecodingState.BYTES);
        this.message = new DefaultNailgunRequestImpl();
    }

	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.handler.codec.replay.ReplayingDecoder#checkpoint(java.lang.Enum)
	 */
	protected void checkpoint(DecodingState state) {
		this.state = state;
		super.checkpoint(state);
		if(log.isDebugEnabled()) log.debug("checkpoint:" + state);
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.handler.codec.replay.ReplayingDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer, java.lang.Enum)
	 */
	@Override
	protected NailgunRequest decode(final ChannelHandlerContext ctx, final Channel channel, ChannelBuffer buffer, DecodingState decodeState) throws Exception {
		state = decodeState;
		if(message.getRemoteAddress()==null) {
			InetSocketAddress remote = (InetSocketAddress)channel.getRemoteAddress();
			message.setRemoteAddress(remote.getAddress());
			message.setRemotePort(remote.getPort());
		}
		while(true) {
			switch(state) {
				case BYTES:
					bytesToRead = buffer.readInt();
					checkpoint(DecodingState.TYPE);	
					if(log.isDebugEnabled()) log.debug("NG Chunk [BYTES]:" + bytesToRead);
					break;
				case TYPE:
					chunkType = buffer.readByte();
					state = DecodingState.getState(chunkType);
					if(state==null) {
						throw new Exception("Invalid chunk type [" + chunkType + "]", new Throwable());
					}
					checkpoint(state);
					if(log.isDebugEnabled()) log.debug("NG Chunk [TYPE]:" + state);
					//log.info("NG Chunk [TYPE]:" + state);
					break;
				case WORKING_DIR:
					bytes = new byte[bytesToRead];
					buffer.readBytes(bytes);
					message.setWorkingDirectory(new String(bytes));
					checkpoint(DecodingState.BYTES);
					if(log.isDebugEnabled()) log.debug("NG Chunk [WORKING_DIR]:" + message.getWorkingDirectory());
					break;
				case ENVIRONMENT:
					bytes = new byte[bytesToRead];
					buffer.readBytes(bytes);
					message.addToEnvironment(new String(bytes));
					checkpoint(DecodingState.BYTES);
					//if(log.isDebugEnabled()) log.debug("NG Chunk [ENVIRONMENT]:" + new String(bytes));
					break;
				case ARGUMENTS:
					bytes = new byte[bytesToRead];
					buffer.readBytes(bytes);
					message.addArgument(new String(bytes));
					checkpoint(DecodingState.BYTES);
					if(log.isDebugEnabled()) log.debug("NG Chunk [ARGUMENTS]:" + new String(bytes));
					break;
				case STDIN:
					bytes = new byte[bytesToRead];
					buffer.readBytes(bytes);
					System.out.println("INSTREAM:" + new String(bytes));
					checkpoint(DecodingState.STDIN_EOF);
					System.out.println("ENV:\n" + message.printEnvironment());
				case COMMAND:
					bytes = new byte[bytesToRead];
					buffer.readBytes(bytes);
					message.setCommand(new String(bytes));					
					if(log.isDebugEnabled()) log.debug("NG Chunk [COMMAND]:" + message.getCommand());
					checkpoint(DecodingState.BYTES);
					sendStartStdInSignal(ctx, channel);
					// at this point, we can complete the Nailgun request
					// and send it for dispatch.
					if(log.isDebugEnabled()) log.debug("Nailgun Client Complete:\n" + message);
					break;
				case STDIN_EOF:
					// this means that the std in has completed
					if(log.isDebugEnabled()) log.debug("NG Chunk [" + state + "]");
//				case STARTINPUT:
//					channel.write(NailgunConstants.CHUNKTYPE_STARTINPUT).awaitUninterruptibly();
//					checkpoint(DecodingState.DEBUG);
//					break;
//				case STDIN_EOF:
//					// the request is complete
//					System.out.println(state);
//				case STDIN:
//					// starting to stream input
//					System.out.println(state);
//				case EXIT:
//					// exit code from client
//					System.out.println(state);
//				case STARTINPUT:
//					System.out.println(state);
//				case STDERR:
//					System.out.println(state);
//				case STDOUT:
//					System.out.println(state);
				default:
					
					System.out.println("DEBUG:" + (char)buffer.readByte());
//					while(true) {
//						StringBuilder b = new StringBuilder();
//						try {
//							b.append((char)buffer.readByte());
//							System.out.println(b);
//						} catch (Throwable t) {
//							System.out.println(b);
//							done = true;
//							break;
//						}
//						
//					}
//					done = true;
					break;
//					throw new IllegalStateException("Unhandled Decoding State: [" + state + "]", new Throwable());
			}
		}
	}
	
	/**
	 * Sends a signal back to the nailgun client indicating that we're ready to accept the input stream
	 * @param ctx The ChannelHandlerContext
	 * @param channel The channel
	 */
	protected void sendStartStdInSignal(ChannelHandlerContext ctx, Channel channel) {
		DownstreamMessageEvent dme = new DownstreamMessageEvent(channel, Channels.future(channel), STREAM_IN_READY, channel.getRemoteAddress());
		ctx.sendDownstream(dme);
	}
	
	 /**
	 * Cleans up any remaining state
	 */
	protected void cleanup() {
		bytes = null;
		state = null;
		chunkType = -10;
		bytesToRead = -1;
	}

}
