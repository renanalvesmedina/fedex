package com.mercurio.lms.tabelaprecos.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * 
 * @spring.bean id="lms.tabelaprecos.emitirRelatorioClientesDivisaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/tabelaprecos/report/emitirRelatorioClientesDivisao.jasper"
 */
public class EmitirRelatorioClientesDivisaoService extends ReportServiceSupport {

	private  ClienteService clienteService;

	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		List<Map<String,Object>> list = clienteService.findClienteDivisao(criteria);
		if(CollectionUtils.isEmpty(list)){
			throw new BusinessException("emptyReport");
		}
		
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put(JRParameter.IS_IGNORE_PAGINATION, true);
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		return createReportDataObject(new JRMapArrayDataSource(list.toArray()), parametersReport);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}