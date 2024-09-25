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
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;

/**
 * 
 * @spring.bean id="lms.tabelaprecos.emitirRelatorioTabelaDePrecosService"
 * @spring.property name="reportName" value="com/mercurio/lms/tabelaprecos/report/emitirRelatorioTabelaDePrecos.jasper"
 */

public class EmitirRelatorioTabelaDePrecosService  extends ReportServiceSupport{

	private static final String FORMAT_EXCEL = "xls";
	
	private TabelaPrecoDAO tabelaPrecoDAO;
	

	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		List<Map<String, Object>> list = tabelaPrecoDAO.findRelatorioTabelaPreco(criteria);
		if(CollectionUtils.isEmpty(list)){
			throw new BusinessException("emptyReport");
		}
		
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put(JRParameter.IS_IGNORE_PAGINATION, true);
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		return createReportDataObject(new JRMapArrayDataSource(list.toArray()), parametersReport);
	}

	/**
	 * Configura Dominios
	 */
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		//config.configDomainField("VNF_TP_ORIGEM_PESO", "DM_TIPO_ORIGEM_PESO");
	}
	public void setTabelaPrecoDAO(TabelaPrecoDAO tabelaPrecoDAO) {
		this.tabelaPrecoDAO = tabelaPrecoDAO;
	}
}
