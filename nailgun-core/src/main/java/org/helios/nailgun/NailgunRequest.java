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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.helios.nailgun.handlers.NailgunStreamHandler;

/**
 * <p>Title: NailgunRequest</p>
 * <p>Description: Instances of this class are created by the {@link NailgunStreamHandler} when a nailgun request is received. It represents the contents of the request.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.NailgunRequest</code></p>
 */

public class NailgunRequest implements Serializable {
	/**  */
	private static final long serialVersionUID = -3507580873475441885L;
	/** The command or specified invocation target */
	private String command;
	/** The caller's command line working directory  */
	private String workingDirectory;
	/** The caller's environment  */
	private final Properties environment = new Properties();
	/** The caller's command line arguments */
	private final List<String> arguments = new ArrayList<String>();
    /** Processing state: The number of bytes to read */
    private transient int bytesToRead = -1;
	
	
	
	/**
	 * Creates a new NailgunRequest
	 */
	public NailgunRequest() {
		
	}
	



	
	/**
	 * Returns the command or specified invocation target
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}


	/**
	 * Returns the caller's command line working directory
	 * @return the workingDirectory
	 */
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Returns the caller's environment
	 * @return the environment
	 */
	public Properties getEnvironment() {
		return environment;
	}


	/**
	 * Returns the caller's command line arguments
	 * @return the arguments
	 */
	public String[] getArguments() {
		return arguments.toArray(new String[arguments.size()]);
	}



	/**
	 * Sets the command
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Sets the working directory
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	/**
	 * Sets the environment
	 * @param environment the environment to set
	 */
	public void setEnvironment(Properties environment) {
		this.environment.putAll(environment);
	}
	
	/**
	 * Adds a property to the remote environment
	 * @param key The property key
	 * @param value The property value
	 */
	public void addToEnvironment(String key, String value) {
		this.environment.setProperty(key, value);
	}
	
	/**
	 * Adds a property to the remote environment
	 * @param line The property line (i.e. <code>KEY=VALUE</code>)
	 */
	public void addToEnvironment(String line) {
		int equalsIndex = line.indexOf('=');
		if (equalsIndex > 0) {
			environment.setProperty(line.substring(0, equalsIndex),line.substring(equalsIndex + 1));
		}		
	}
	

	/**
	 * Sets the arguments
	 * @param arguments the arguments to set
	 */
	public void setArguments(String[] arguments) {
		Collections.addAll(this.arguments, arguments);		
	}
	
	/**
	 * Adds an argument to the request
	 * @param arg the argument to add
	 */
	public void addArgument(String arg) {
		this.arguments.add(arg);
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
	    StringBuilder retValue = new StringBuilder("NailgunRequest [")
	        .append(TAB).append("command:").append(this.command)
	        .append(TAB).append("workingDirectory:").append(this.workingDirectory)
	        .append(TAB).append("environment:").append(this.environment.size()).append(" properties")
	        .append(TAB).append("arguments:").append(this.arguments)
	        .append("\n]");    
	    return retValue.toString();
	}

	/**
	 * Returns the number of bytes to read for the next field
	 * @return the bytesToRead
	 */
	public int getBytesToRead() {
		return bytesToRead;
	}

	/**
	 * Sets the number of bytes to read for the next field
	 * @param bytesToRead the bytesToRead to set
	 */
	public void setBytesToRead(int bytesToRead) {
		if (bytesToRead<= 0) {
            throw new RuntimeException("Invalid content size [" + bytesToRead + "]", new Throwable());
        }		
		this.bytesToRead = bytesToRead;
	}

	
}
