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


/**
 * <p>Title: AbstractCommandHandler</p>
 * <p>Description: An abstract base class for simple non-stream handling command handlers</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.server.AbstractCommandHandler</code></p>
 */

public abstract class AbstractCommandHandler implements CommandHandler {
	/** The command names supported by this handler */
	protected final String[] commandNames;

	/**
	 * Creates a new AbstractCommandHandler
	 * @param commandNames The command names supported by this handler
	 */
	public AbstractCommandHandler(String[] commandNames) {
		this.commandNames = commandNames;
	}
	
	/**
	 * Creates a new AbstractCommandHandler
	 * Command names are determined from the @CommandNames annotation which must be present.
	 */
	public AbstractCommandHandler() {
		HandlerCommands hc = this.getClass().getAnnotation(HandlerCommands.class);
		if(hc==null) throw new RuntimeException("AbstractCommandHandler [" + getClass().getName() + "]created with no supplied command names and not annotated with @HandlerCommands", new Throwable());
		commandNames = hc.commands();
	}
	

	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.handler.server.CommandHandler#getCommandNames()
	 */
	@Override
	public String[] getCommandNames() {
		return commandNames;
	}


}
