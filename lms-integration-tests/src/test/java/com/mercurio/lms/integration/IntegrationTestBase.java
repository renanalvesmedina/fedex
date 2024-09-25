package com.mercurio.lms.integration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;

/**
 * Unit test for simple App.
 * 
 */
public class IntegrationTestBase extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return buildConfigLocations();
	}
	
	/**
	 * Define o arquivo de beans base a ser utilizado nos testes de integração.
	 * Valor padrão é integration-test-beans.xml
	 * @return
	 */
	protected String testBeans() {
		return "classpath:integration-test-beans.xml";
	}
	
	/**
	 * Define o arquivo de configuração do datasource. A configuração de datasource 
	 * está  externalizada no jdbc.properties
	 * @return
	 */
	protected String datasourceProperties() {
		return "classpath:default-data-source-properties.xml";
	}
	
	private String[] buildConfigLocations() {
		return new String[] { testBeans(), datasourceProperties() };
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		doHttpSessionMock();
	}
	
	@Override
	protected void onTearDownAfterTransaction() throws Exception {
		undoHttpSessionMock();
	}

	private void undoHttpSessionMock() {
		HttpServletRequestHolder.setHttpServletRequest(null);
	}

	private void doHttpSessionMock() {
		HttpServletRequest req = new MockHttpServletRequest();
		req.getSession();
		HttpServletRequestHolder.setHttpServletRequest(req);
	}
}
