package com.mercurio.lms.carregamento.swt.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.gm.model.service.RotaEmbarqueService;
import com.mercurio.lms.gm.report.RelatorioGerencialEmbarquesInHouseService;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * 
 * @spring.bean id="lms.carregamento.swt.relatorioGerencialEmbarquesInHouseAction"
 */

public class RelatorioGerencialEmbarquesInHouseAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	private RotaEmbarqueService rotaEmbarqueService;
	private RelatorioGerencialEmbarquesInHouseService relatorioGerencialEmbarquesInHouseService;

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setRotaEmbarqueService(RotaEmbarqueService rotaEmbarqueService) {
		this.rotaEmbarqueService = rotaEmbarqueService;
	}

	/**
	 * M�todo responsavel por emitir os relat�rios da gerencia de embarques GM. 
	 * 
	 * Demanda LMS-2795
	 * 
	 * @param filters
	 * @return
	 * @throws Exception
	 */
	public String executeReport(Map filters) throws Exception {

		String reportName = "com/mercurio/lms/gm/report/relatorioGerencialEmbarquesInHouse.jasper";

		if (filters.get("tpFormatoRelatorio").equals("xls") && reportName.lastIndexOf("Excel.jasper") == -1) { // Se nao tiver c sufixo Excel, e For xsl, coloca o sufixo
			reportName = reportName.substring(0, reportName.lastIndexOf('.')) + "Excel.jasper";
		} else if (!filters.get("tpFormatoRelatorio").equals("xls") && reportName.lastIndexOf("Excel.jasper") != -1) { // Se tiver c sufixo Excel, e N�o for xsl, retira o sufixo
			reportName = reportName.substring(0, reportName.lastIndexOf("Excel.jasper")) + ".jasper";
		}
		relatorioGerencialEmbarquesInHouseService.setReportName(reportName);

		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);

		return reportExecutionManager.generateReportLocator(relatorioGerencialEmbarquesInHouseService, tfm);
	}

	/**
	 * Lista todas as rotas de embarque
	 * 
	 * @return
	 */
	public List findRotasEmbarque() {
		return rotaEmbarqueService.find(null);
	}

	public void setRelatorioGerencialEmbarquesInHouseService(RelatorioGerencialEmbarquesInHouseService relatorioGerencialEmbarquesInHouseService) {
		this.relatorioGerencialEmbarquesInHouseService = relatorioGerencialEmbarquesInHouseService;
	}
}
