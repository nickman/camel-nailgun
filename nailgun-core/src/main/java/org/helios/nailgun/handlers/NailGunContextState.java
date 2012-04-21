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

import org.helios.nailgun.DefaultNailgunRequestImpl;

/**
 * <p>Title: NailGunContextState</p>
 * <p>Description: A class whose instances retain the stateful context of a Nailgun request that is in flight.
 * These instances are attached to the ChannelHandlerContext for the duration of the conversation.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handlers.NailGunContextState</code></p>
 */

public class NailGunContextState {
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
	
	NailGunContextState() {
		
	}

	/**
	 * Returns 
	 * @return the message
	 */
	DefaultNailgunRequestImpl getMessage() {
		return message;
	}

	/**
	 * Sets 
	 * @param message the message to set
	 */
	void setMessage(DefaultNailgunRequestImpl message) {
		this.message = message;
	}

	/**
	 * Returns 
	 * @return the bytes
	 */
	byte[] getBytes() {
		return bytes;
	}

	/**
	 * Sets 
	 * @param bytes the bytes to set
	 */
	void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Returns 
	 * @return the state
	 */
	DecodingState getState() {
		return state;
	}

	/**
	 * Sets 
	 * @param state the state to set
	 */
	void setState(DecodingState state) {
		this.state = state;
	}

	/**
	 * Returns 
	 * @return the chunkType
	 */
	byte getChunkType() {
		return chunkType;
	}

	/**
	 * Sets 
	 * @param chunkType the chunkType to set
	 */
	void setChunkType(byte chunkType) {
		this.chunkType = chunkType;
	}

	/**
	 * Returns 
	 * @return the bytesToRead
	 */
	int getBytesToRead() {
		return bytesToRead;
	}

	/**
	 * Sets 
	 * @param bytesToRead the bytesToRead to set
	 */
	void setBytesToRead(int bytesToRead) {
		this.bytesToRead = bytesToRead;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
	    final String TAB = "\n\t";
	    StringBuilder retValue = new StringBuilder("NailGunContextState [")
	        .append(TAB).append("message:").append(this.message)
	        .append(TAB).append("bytes:").append(this.bytes)
	        .append(TAB).append("state:").append(this.state)
	        .append(TAB).append("chunkType:").append(this.chunkType)
	        .append(TAB).append("bytesToRead:").append(this.bytesToRead)
	        .append("\n]");    
	    return retValue.toString();
	}

}
