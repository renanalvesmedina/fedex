package com.mercurio.lms.util.session;

import java.sql.Connection;

import com.mercurio.adsm.core.model.hibernate.CustomConnectionSessionConfig;

/**
 * Ver classe com.mercurio.adsm.batch.core.BatchConnectionSessionConfig no projeto adsm-batch
 * @author felipec
 *
 */
@Deprecated
public class BatchConnectionSessionConfig implements
		CustomConnectionSessionConfig {

	public void sessionClose(Connection conn) {
		// do nothing
	}

	public void sessionInit(Connection conn) {
		setJobSessionConfigs(conn);
	}
	
	/**
	 * @param conn
	 */
	private void setJobSessionConfigs(Connection conn) {
	}
	
}
