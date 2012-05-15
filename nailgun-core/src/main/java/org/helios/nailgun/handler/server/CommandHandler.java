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

import org.helios.nailgun.NailgunRequest;

/**
 * <p>Title: CommandHandler</p>
 * <p>Description: Defines a class that handles nailgun requests for a specific command name</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.CommandHandler</code></p>
 */

public interface CommandHandler {
	/**
	 * Returns the names of the commands that this command handler handles
	 * @return the names of the commands that this command handler handles
	 */
	public String[] getCommandNames();
	
	/**
	 * Callback when the mainline server completes the decode of a 
	 * nailgun request for the command handled by this handler
	 * @param request The nailgun request
	 */
	public void onNailgunRequest(NailgunRequest request);
	
}
