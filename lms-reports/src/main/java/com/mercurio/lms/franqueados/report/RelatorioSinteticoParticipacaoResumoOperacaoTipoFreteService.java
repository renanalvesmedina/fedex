package com.mercurio.lms.franqueados.report;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

public class RelatorioSinteticoParticipacaoResumoOperacaoTipoFreteService extends RelatorioSinteticoParticipacaoService {

	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private PessoaService pessoaService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List listaComumOrdered = (List) (parameters.containsKey("defaultQuery")?parameters.get("defaultQuery"):Collections.EMPTY_LIST);
		
		List listaColetaEntregaOrdedered =doctoServicoFranqueadoService.findRelatorioSinteticoColetaEntrega(parameters);

		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(listaComumOrdered);
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
		
		
		parametersReport.put("dataSource1", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource2", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource3", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource4", new JRBeanCollectionDataSource(listaColetaEntregaOrdedered));
		parametersReport.put("dataSource5", new JRBeanCollectionDataSource(listaColetaEntregaOrdedered));

		jr.setParameters(parametersReport);

		return jr;
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
