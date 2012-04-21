package org.helios.nailgun;

import java.net.InetAddress;
import java.util.Properties;

/**
 * <p>Title: NailgunRequest</p>
 * <p>Description: Defines a read-only nailgun request</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.NailgunRequest</code></p>
 */
public interface NailgunRequest {

	/**
	 * Returns the command or specified invocation target
	 * @return the command
	 */
	public abstract String getCommand();

	/**
	 * Returns the caller's command line working directory
	 * @return the workingDirectory
	 */
	public abstract String getWorkingDirectory();

	/**
	 * Returns the caller's environment
	 * @return the environment
	 */
	public abstract Properties getEnvironment();

	/**
	 * Returns the caller's command line arguments
	 * @return the arguments
	 */
	public abstract String[] getArguments();


	/**
	 * Returns the caller's IP address
	 * @return the remoteAddress
	 */
	public abstract InetAddress getRemoteAddress();

	/**
	 * Returns the caller's port
	 * @return the remotePort
	 */
	public abstract int getRemotePort();

}