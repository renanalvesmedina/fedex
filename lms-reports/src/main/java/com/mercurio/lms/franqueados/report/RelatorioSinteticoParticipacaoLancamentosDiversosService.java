package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class RelatorioSinteticoParticipacaoLancamentosDiversosService extends RelatorioSinteticoParticipacaoService {
	
	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private PessoaService pessoaService;
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List lancamentos =doctoServicoFranqueadoService.findRelatorioSinteticoLancamento(parameters);

		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(lancamentos);
			}

			public Map getParameters() {
				return parameters;
			}

			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null)
			parametersReport.put("idFranquia", parameters.get("idFilial"));
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			parametersReport.put("competencia", dtCompetencia);
		}
		
		parametersReport.put("Cte", fixoFranqueadoService.getValorCte((Long)parameters.get("idFilial"), (YearMonthDay)parameters.get("competencia")));
		parametersReport.put("NomeEmpresa", pessoaService.findNomePessoaByCompetenciaIdFranquia((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial")));
		
		
		jr.setParameters(parametersReport);
		return jr;
	}
	

	@SuppressWarnings("rawtypes")
	protected Object[] getParameters(Map parameters){
		Long idFranquia = null;
	  	if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			idFranquia = (Long) parameters.get("idFilial");
    	}
		String competencia = null;
    	if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
    		YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
    		
    		competencia = dtCompetencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
    	}

		
		return  new Object[]{competencia,idFranquia,competencia,idFranquia};
	}
	
	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
	
	public void setFixoFranqueadoService(
			FixoFranqueadoService fixoFranqueadoService) {
		this.fixoFranqueadoService = fixoFranqueadoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}
