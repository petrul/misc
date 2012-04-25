package test.pertinence.dao;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * spring doesn't seem to allow to insert connectionProperties
 * @author dadi
 *
 */
public class CustomDbcpBasicDataSource extends BasicDataSource {
	public CustomDbcpBasicDataSource() {
		super();
		this.addConnectionProperty("autoReconnect", "true");
	}
}
