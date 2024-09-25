package com.mercurio.lms.contasreceber.report;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.service.BoletoService;

/**
 * Classe de serviço para CRUD:   
 *
 * @author HectorJ
 * @since 14/06/2006
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirBoletoBanrisul.jasper" 
 * @spring.bean id="lms.contasreceber.emitirBoletoService"
 */
public class EmitirBoletoService extends ReportServiceSupport {

	private BoletoService boletoService;
	private ParametroGeralService parametroGeralService;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {

		Map parametersReport = boletoService.getParametersReport(parameters);
		List reportList = boletoService.executeReport(parametersReport);
		
		if (reportList != null && reportList.size() > 0){
			Map boletoMap = (Map)reportList.get(0);
			Short nrBanco = (Short) boletoMap.get("NR_BANCO"); 
			parametersReport.put("NR_BANCO", nrBanco);
		}
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(reportList);
		this.setReportName(dispatcherRelatorio((Short)parametersReport.get("NR_BANCO")));
		return createReportDataObject(jrMap, parametersReport);
		}

	public List<Long> findFaturasBoletoByCriteria(TypedFlatMap parameters){

		return boletoService.findFaturasBoletoByCriteria(parameters);

		}

	/**
	 * Método responsável por retornar o nome do relatório de acordo com o número do banco
	 * 
	 * @author hectorj
	 * @since 26/06/2006
	 * 
	 * @param nrBanco
	 * @return String
	 */
	private String dispatcherRelatorio(Short nrBanco){
		String reportName = "";

		if (nrBanco.equals(ConstantesConfiguracoes.COD_BANRISUL))
			reportName = "com/mercurio/lms/contasreceber/report/emitirBoletoBanrisul.jasper";
		else if(nrBanco.equals(ConstantesConfiguracoes.COD_BRADESCO))
			reportName = "com/mercurio/lms/contasreceber/report/emitirBoletoBradesco.jasper";
		else if(nrBanco.equals(ConstantesConfiguracoes.COD_HSBC))
			reportName = "com/mercurio/lms/contasreceber/report/emitirBoletoHSBC.jasper";
		else if(nrBanco.equals(ConstantesConfiguracoes.COD_ITAU))
			reportName = "com/mercurio/lms/contasreceber/report/emitirBoletoItau.jasper";
		else
			throw new BusinessException("LMS-36166");

		return reportName;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

}
