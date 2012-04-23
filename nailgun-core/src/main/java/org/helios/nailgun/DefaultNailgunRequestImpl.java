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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.helios.nailgun.codecs.NailgunRequestDecoder;
import org.jboss.netty.channel.Channel;

/**
 * <p>Title: DefaultNailgunRequestImpl</p>
 * <p>Description: Instances of this class are created by the {@link NailgunRequestDecoder} when a nailgun request is received. It represents the contents of the request.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.DefaultNailgunRequestImpl</code></p>
 */

public class DefaultNailgunRequestImpl implements Serializable, NailgunRequest {
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
    /** The netty channel through which the client is communicating */
    private transient Channel channel = null;
	
	
	/**
	 * Creates a new DefaultNailgunRequestImpl
	 * @param command The command or specified invocation target
	 * @param workingDirectory The caller's command line working directory
	 * @param environment The caller's environment
	 * @param arguments The caller's command line arguments
	 * @return a new DefaultNailgunRequestImpl
	 */
	public static NailgunRequest newInstance(String command, String workingDirectory, Properties environment, String...arguments) {
		return new DefaultNailgunRequestImpl(command, workingDirectory, environment, arguments);
	}
	
	
	/**
	 * Creates a new DefaultNailgunRequestImpl
	 * @param command The command or specified invocation target
	 * @param workingDirectory The caller's command line working directory
	 * @param environment The caller's environment
	 * @param arguments The caller's command line arguments
	 */
	public DefaultNailgunRequestImpl(String command, String workingDirectory, Properties environment, String...arguments) {
		this.command = command;
		this.workingDirectory = workingDirectory;
		this.environment.putAll(environment);
		for(String arg: arguments) {
			if(arg!=null && !arg.isEmpty()) {
				this.arguments.add(arg);
			}
		}
	}
    
	
	/**
	 * Creates a new DefaultNailgunRequestImpl
	 */
	public DefaultNailgunRequestImpl() {
		
	}
	



	
	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getCommand()
	 */
	@Override
	public String getCommand() {
		return command;
	}


	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getWorkingDirectory()
	 */
	@Override
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getEnvironment()
	 */
	@Override
	public Properties getEnvironment() {
		return environment;
	}


	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getArguments()
	 */
	@Override
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
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getRemoteAddress()
	 */
	@Override
	public InetAddress getRemoteAddress() {
		return channel==null ? null : ((InetSocketAddress)channel.getRemoteAddress()).getAddress();
	}
	
	public String printEnvironment() {
		StringBuilder b = new StringBuilder(environment.size()*30);
		TreeMap<?, ?> env = new TreeMap<Object, Object>(environment); 
		for(Map.Entry<?, ?> entry: env.entrySet()) {
			b.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
		}
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.NailgunRequest#getRemotePort()
	 */
	@Override
	public int getRemotePort() {
		return channel==null ? -1: ((InetSocketAddress)channel.getRemoteAddress()).getPort();
	}
	
	/**
	 * Returns the channel
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}


	/**
	 * Sets the channel
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	/*
	 * OnInputStreamStart
	 * OnInoutStreamEOF
	 * getAvailableBytes()
	 * 
	 * 
	 * 
	 * InputStream  (reads from ng client), returns -1 on end of stream
	 * onChannelClosed
	 * -- How do we timeout when the ng client is not sending any input ?
	 */
	
	



	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
	    final String TAB = "\n\t";
	    StringBuilder retValue = new StringBuilder("DefaultNailgunRequestImpl [")
	    	.append(TAB).append("channel:").append(this.channel)
	        .append(TAB).append("command:").append(this.command)
	        .append(TAB).append("workingDirectory:").append(this.workingDirectory)
	        .append(TAB).append("environment:").append(this.environment.size()).append(" properties")
	        .append(TAB).append("arguments:").append(this.arguments)
	        .append(TAB).append("remoteAddress:").append(getRemoteAddress())
	        .append(TAB).append("remotePort:").append(getRemotePort())
	        .append("\n]");    
	    return retValue.toString();
	}


	
}
