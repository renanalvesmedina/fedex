package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.ReembarqueDoctoServicoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSinteticoParticipacaoLancamentosDiversosService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoLancamentosDiversos.jasper"
 */
public class RelatorioAnaliticoParticipacaoService extends ReportServiceSupport {

	
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private ReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService;
	private LancamentoFranqueadoService lancamentoFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private PessoaService pessoaService;
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		boolean filtraFranquia = (parameters.containsKey("idFilial") && parameters.get("idFilial") != null);

		final List listaDocumentos = doctoServicoFranqueadoService.findRelatorioAnaliticoDocumentos(filtraFranquia, false, parameters);
		final List listaReembarque = reembarqueFranqueadoService.findRelatorioAnaliticoReembarques(filtraFranquia, false, parameters);
		final List listaFreteLocal = doctoServicoFranqueadoService.findRelatorioAnaliticoFretesLocal(filtraFranquia, false, parameters);
		final List listaBDM = doctoServicoFranqueadoService.findRelatorioAnaliticoBDM(filtraFranquia, false, parameters);
		final List listaServicoAdicional = doctoServicoFranqueadoService.findRelatorioAnaliticoServicosAdicionais(filtraFranquia, false, parameters);	
		final List listaCompetenciaAnterior = doctoServicoFranqueadoService.findRelatorioAnaliticoDocumentosCompetenciaAnterior(filtraFranquia, false, parameters);
		final List listaIRE = lancamentoFranqueadoService.findRelatorioAnaliticoIRE(filtraFranquia, false, parameters);
		
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(listaDocumentos);
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
		
		parametersReport.put("dataSourceDocumentos", new JRBeanCollectionDataSource(listaDocumentos));
		parametersReport.put("dataSourceFreteLocal", new JRBeanCollectionDataSource(listaFreteLocal));
		parametersReport.put("dataSourceBDM", new JRBeanCollectionDataSource(listaBDM));
		parametersReport.put("dataSourceServicoAdicional", new JRBeanCollectionDataSource(listaServicoAdicional));
		parametersReport.put("dataSourceReembarque", new JRBeanCollectionDataSource(listaReembarque));
		parametersReport.put("dataSourceCompetenciaAnterior", new JRBeanCollectionDataSource(listaCompetenciaAnterior));
		parametersReport.put("dataSourceIRE", new JRBeanCollectionDataSource(listaIRE));
		
		parametersReport.put("Cte", fixoFranqueadoService.getValorCte((Long)parameters.get("idFilial"), (YearMonthDay)parameters.get("competencia")));
		parametersReport.put("NomeEmpresa", pessoaService.findNomePessoaByCompetenciaIdFranquia((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial")));
		
		jr.setParameters(parametersReport);

		return jr;
	}
	
	@SuppressWarnings("rawtypes")
	protected String getFilterSummary(Map parameters) {
		SqlTemplate sql = createSqlTemplate();

		if (parameters.containsKey("nmFranquia") && parameters.get("nmFranquia") != null) {
			String nmFranquia = (String) parameters.get("nmFranquia");
			sql.addFilterSummary("franquia", nmFranquia);
		}

		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");

			String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("MM/yyyy"));

			sql.addFilterSummary("competencia", competencia);
		}

		return sql.getFilterSummary();
	}


	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
	

	public void setReembarqueFranqueadoService(
			ReembarqueDoctoServicoFranqueadoService reembarqueFranqueadoService) {
		this.reembarqueFranqueadoService = reembarqueFranqueadoService;
	}
	
	public void setFixoFranqueadoService(
			FixoFranqueadoService fixoFranqueadoService) {
		this.fixoFranqueadoService = fixoFranqueadoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}
}
