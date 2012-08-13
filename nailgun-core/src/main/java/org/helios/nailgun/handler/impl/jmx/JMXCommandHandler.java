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
package org.helios.nailgun.handler.impl.jmx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.helios.nailgun.NailgunRequest;
import org.helios.nailgun.handler.NailgunRequestHandler;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * <p>Title: JMXCommandHandler</p>
 * <p>Description: Sample request handler implementation that retrieves JMX attribute values from local in VM MBeanServers</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.nailgun.handler.impl.jmx.JMXCommandHandler</code></p>
 */

public class JMXCommandHandler implements NailgunRequestHandler {
	/** The internal logger */
	protected final InternalLogger log = InternalLoggerFactory.getInstance(getClass());	
	/** A map of MBeanServers keyed by the default domain name */
	protected final Map<String, MBeanServerConnection> mbeanConnections = new ConcurrentHashMap<String, MBeanServerConnection>();
	
	/** The default domain name */
	public static final String DEFAULT_DOMAIN = "DefaultDomain";
	/** The domain name argument prefix */
	public static final String DOMAIN_ARG_PREFIX = "-domain=";
	
	/**
	 * Creates a new JMXCommandHandler and acquires references to all located MBeanServers in this VM.
	 */
	public JMXCommandHandler() {
		log.debug("Initializing " + getClass().getName());
		for(MBeanServer server: MBeanServerFactory.findMBeanServer(null)) {
			String name = server.getDefaultDomain();
			if(name==null) name = DEFAULT_DOMAIN;
			mbeanConnections.put(name, server);
		}
		if(mbeanConnections.size()<1 || !mbeanConnections.containsKey(DEFAULT_DOMAIN)) {
			mbeanConnections.put(DEFAULT_DOMAIN, ManagementFactory.getPlatformMBeanServer());
		}
		log.debug("Created " + getClass().getName() + " with " + mbeanConnections.size() + " MBeanServers");
	}
	
	/**
	 * Looks up the named MBeanServer connection
	 * @param name The name of the mbeanserver's default domain
	 * @return The located MBeanServer connection or null if one was not found
	 */
	protected MBeanServerConnection getServer(String name) {
		MBeanServerConnection conn = mbeanConnections.get(name);
		if(conn==null) {
			for(MBeanServer server: MBeanServerFactory.findMBeanServer(null)) {
				if(server.getDefaultDomain().equals(name)) {
					conn = server;
					mbeanConnections.put(name, conn);
				}
			}
		}
		return conn;
	}
	
	// 48, 65, 6c, 6c, 6f
	
	protected void readInputStream(NailgunRequest request) {
		try {
			InputStream is = request.getInputStream(5000, TimeUnit.MILLISECONDS);
			byte[] buff = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytesRead = -1;
			int totalBytesRead = 0;
			while((bytesRead = is.read(buff))!=-1) {
				baos.write(buff, 0, bytesRead);
				totalBytesRead += bytesRead;
			}
			log.info("STDIN (" + totalBytesRead + ")  [" + baos.toString() + "]");
			request.out("Content:" + baos.toString() + "\n");
		} catch (Exception e) {
			log.error("Failed to read input stream", e);
		}
	}
	
	/**
	 * <p>Arguments: <ol>
	 * 	<li>(Mandatory) The object name to query</li>
	 * 	<li>(Mandatory) The attribute name to query</li>
	 * 	<li>(Optional) <b><code>-domain=&lt;Domain&gt;</code></b>: The default domain name of the MBeanServer to query from</li>
	 * </ol></p>
	 * {@inheritDoc}
	 * @see org.helios.nailgun.handler.NailgunRequestHandler#onNailgunRequest(org.helios.nailgun.NailgunRequest)
	 */
	@Override
	public void onNailgunRequest(NailgunRequest request) {		
		if(request==null) throw new IllegalArgumentException("The passed request was null", new Throwable());
		if(log.isDebugEnabled()) log.debug("Processing request " + Arrays.toString(request.getArguments()));
		readInputStream(request);
		//DOMAIN_ARG_PREFIX
		String[] args = request.getArguments();
		try {
			if(args==null || args.length<2) {
				request.err("[JMXCommandHandler] Invalid request. Insufficient arguments " + ((args==null) ? "[null]" : Arrays.toString(args)));				
				return;
			}
			String errMsg = null;
			Set<String> attrNames = new HashSet<String>();
			ObjectName on = null;
			MBeanServerConnection server = null;
			try {
				errMsg = "Invalid Object Name [" + args[0] + "]";
				on = new ObjectName(args[0]);
				errMsg = "Attribute Processing";
				for(int i = 1; i < args.length; i++) {
					if(args[i].toLowerCase().startsWith(DOMAIN_ARG_PREFIX)) {
						errMsg = "Looking up MBeanServer [" + args[i] + "]";
						String svrName = args[i].split("=")[1];
						server = getServer(svrName);
						if(server==null) {
							request.err("[JMXCommandHandler] Request failed. Message: " + errMsg);
							return;
						}
						errMsg = "Attribute Processing";
					} else {
						attrNames.add(args[i]);
					}
				}
				if(attrNames.isEmpty()) {
					request.err("[JMXCommandHandler] Request failed. No attributes specified");
					return;					
				}
				String[] attrs = attrNames.toArray(new String[0]);
				if(server==null) server = getServer(DEFAULT_DOMAIN);
				boolean isPattern = on.isPattern();
				if(!isPattern) {
					if(!server.isRegistered(on)) {
						request.err("[JMXCommandHandler] Request failed. Invalid ObjectName [" + on + "]");
						return;
					}
				}
				StringBuilder b = new StringBuilder();
				for(ObjectName objectName: server.queryNames(on, null)) {
					if(isPattern) b.append(objectName.toString()).append("\n=============\n");
					AttributeList attrList = server.getAttributes(objectName, attrs);
					for(Attribute a: attrList.asList()) {
						b.append(a.getName()).append(":").append(a.getValue()).append("\n");
					}
				}
				request.out(b);
			} catch (Exception e) {
				log.error("JMXAttr Request Failed", e);
				request.err("[JMXCommandHandler] Request failed. Message: " + errMsg + "  Exception:" + e.toString());				
				return;			
			}
		} finally {
			request.end();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see org.helios.nailgun.handler.NailgunRequestHandler#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return "jmxattr";
	}

}
