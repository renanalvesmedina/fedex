package com.mercurio.lms.expedicao.report;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.expedicao.model.service.CheckReceitaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do EXCEL da Receita
 * 
 * @author André Valadas
 * @since 15/04/2011
 * 
 * @spring.bean id="lms.expedicao.gerarExcelCheckReceitaService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/excelCheckReceita.jasper"
 */
public class GerarExcelCheckReceitaService extends ReportServiceSupport {

	private CheckReceitaService checkReceitaService;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Busca dados para contemplar a Receita */
		final List<Map> listReceitaData = checkReceitaService.findCheckReceitaMapped();
		parameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		parameters.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		return createReportDataObject(new JRMapArrayDataSource(listReceitaData.toArray()), parameters);
	}

	/**
	 * @param checkReceitaService
	 *            the checkReceitaService to set
	 */
	public void setCheckReceitaService(CheckReceitaService checkReceitaService) {
		this.checkReceitaService = checkReceitaService;
	}
}