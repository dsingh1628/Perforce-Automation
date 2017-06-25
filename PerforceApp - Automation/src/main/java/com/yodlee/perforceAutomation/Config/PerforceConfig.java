package com.yodlee.perforceAutomation.Config;

import java.net.URISyntaxException;
import java.util.Properties;

import com.perforce.p4java.PropertyDefs;
import com.perforce.p4java.exception.ConfigException;
import com.perforce.p4java.exception.ConnectionException;
import com.perforce.p4java.exception.NoSuchObjectException;
import com.perforce.p4java.exception.ResourceException;
import com.perforce.p4java.server.IServer;
import com.perforce.p4java.server.IServerAddress;
import com.perforce.p4java.server.ServerFactory;

public class PerforceConfig {

	
	private static final Properties properties = new Properties();
	private static IServer server=null;
	private static final String URL=IServerAddress.Protocol.P4JAVA.toString() + "://#perforce_directory_path";	
	static {
		properties.setProperty(PropertyDefs.PASSWORD_KEY, "#password");
		properties.setProperty(PropertyDefs.USER_NAME_KEY, "#username");
	}
	
	private PerforceConfig() {
		
	}
	
	public static IServer getConnection() throws ConnectionException, NoSuchObjectException, ConfigException, ResourceException, URISyntaxException{
		if(server==null) 
			server= ServerFactory.getServer(URL, properties);	
		
		return server;	
		
	}
	
	
}
