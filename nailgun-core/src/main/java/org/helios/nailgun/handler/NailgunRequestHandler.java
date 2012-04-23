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
package org.helios.nailgun.handler;

import org.helios.nailgun.NailgunRequest;

/**
 * <p>Title: NailgunRequestHandler</p>
 * <p>Description: Defines a class that will handle a nailgun request.</p>
 * <p>Note that this interface does not specify any functionality regarding processing the inputstream sent from the nailgun client.</p>
 * <p>Request attributes and functionality exposed to a NailgunRequestHandler instance will be as follows:<ul>
 * 	<li>The command name, command arguments, IP address, port and environment of the calling client.</li>
 * 	<li>The handler can write STDOUT to the client</li>
 * 	<li>The handler can write STDERR to the client</li>
 * 	<li>The handler can write an exit code to the client</li>
 * </ul>
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.NailgunRequestHandler</code></p>
 */
public interface NailgunRequestHandler {
	/**
	 * Invoked on a handler when a request has been fully decoded.
	 * Note that when this method is called, there may still be pending input IO 
	 * in the form of STDIN from the client.
	 * @param request The request from a remote nailgun client.
	 */
	public void onNailgunRequest(NailgunRequest request);
}
