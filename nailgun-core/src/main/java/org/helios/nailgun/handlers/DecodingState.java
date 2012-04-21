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

import java.util.HashMap;
import java.util.Map;

import org.helios.nailgun.NailgunConstants;


/**
 * <p>Title: DecodingState</p>
 * <p>Description: Enumerates the decoding states for {@link NailgunStreamHandler}</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handlers.DecodingState</code></p>
 */

public enum DecodingState {
	BYTES((byte)-1),
	TYPE((byte)-2),
    COMMAND(NailgunConstants.CHUNKTYPE_COMMAND, false),
    WORKING_DIR(NailgunConstants.CHUNKTYPE_WORKINGDIRECTORY, false),
    ENVIRONMENT(NailgunConstants.CHUNKTYPE_ENVIRONMENT, false),
    ARGUMENTS(NailgunConstants.CHUNKTYPE_ARGUMENT, false),
    STDIN(NailgunConstants.CHUNKTYPE_STDIN),
    STDIN_EOF(NailgunConstants.CHUNKTYPE_STDIN_EOF),
    STDOUT(NailgunConstants.CHUNKTYPE_STDOUT),
    STDERR(NailgunConstants.CHUNKTYPE_STDERR),
    EXIT(NailgunConstants.CHUNKTYPE_EXIT),
    INPUT(NailgunConstants.CHUNKTYPE_STARTINPUT);
    
	/**
	 * Returns the DecodingState for the passed chunkType
	 * @param chunkType The chunktype to get the DecodingState for
	 * @return a DecodingState or null in which case the chunk type was not recognized or not a valid state
	 */
	public static DecodingState getState(byte chunkType) {
		return BYTE2STATE.get(chunkType);
	}
	
	private DecodingState(byte chunkType, boolean service) {
		this.chunkType = chunkType;
		this.service = service;
	}
	
	private DecodingState(byte chunkType) {
		this(chunkType, true);
	}
	
	private final byte chunkType;
	private final boolean service;
	
	private static final Map<Byte, DecodingState> BYTE2STATE = new HashMap<Byte, DecodingState>(DecodingState.values().length);
	
	static {
		for(DecodingState ds: DecodingState.values()) {
			if(ds.chunkType>=0) BYTE2STATE.put(ds.chunkType, ds);
		}
	}
	
	public boolean isService() {
		return service;
	}
	
}
