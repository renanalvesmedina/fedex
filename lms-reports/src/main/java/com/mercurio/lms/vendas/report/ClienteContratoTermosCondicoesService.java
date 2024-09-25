package com.mercurio.lms.vendas.report;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.util.EmptyDataSourceByGroups;


/**
 * 
 * @spring.bean id="lms.vendas.clienteContratoTermosCondicoesService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesReportTermoseCondicoes.jasper"
 * 
 */
@Deprecated
public class ClienteContratoTermosCondicoesService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		JRReportDataObject empty = new JRReportDataObject() {
			Map parameters;
			
			@Override
			public void setParameters(Map parameters) {
				this.parameters = parameters;
			}
			
			@Override
			public Map getParameters() {
				return this.parameters;
			}
			
			@Override
			public JRDataSource getDataSource() {
				return new EmptyDataSourceByGroups();
			}
		};
		
		empty.setParameters(parameters);
		return empty;
	}
	public void findDados(TypedFlatMap parameters) {
	}


	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
				return parameters;
			}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
			}

	public void setEmitirTabelasClienteDAO(
			EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
}