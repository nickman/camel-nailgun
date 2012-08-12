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
	
	/**
	 * Returns a message back to the nail gun client's StdOut
	 * @param message The message to return
	 * @return this request
	 */
	public abstract NailgunRequest out(CharSequence message);
	
	/**
	 * Returns a message back to the nail gun client's StdErr
	 * @param message The message to return
	 * @return this request
	 */
	public abstract NailgunRequest err(CharSequence message);
	
	/**
	 * Terminates the response stream
	 * @param exitCode The exit code to send the nailgun client
	 */
	public abstract void end(int exitCode);

	/**
	 * Terminates the response stream with an exit code of 0
	 */
	public abstract void end();
	
	

}