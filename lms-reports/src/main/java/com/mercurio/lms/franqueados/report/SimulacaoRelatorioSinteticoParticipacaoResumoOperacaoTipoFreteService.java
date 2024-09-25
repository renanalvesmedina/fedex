package com.mercurio.lms.franqueados.report;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.lms.franqueados.model.service.SimulacaoDoctoServicoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

public class SimulacaoRelatorioSinteticoParticipacaoResumoOperacaoTipoFreteService extends RelatorioSinteticoParticipacaoService {

	private SimulacaoDoctoServicoFranqueadoService simulacaoDoctoServicoFranqueadoService;
	
	public void setSimulacaoDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.simulacaoDoctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List listaComumOrdered = (List) (parameters.containsKey("defaultQuery")?parameters.get("defaultQuery"):Collections.EMPTY_LIST);
		
		List listaColetaEntregaOrdedered =simulacaoDoctoServicoFranqueadoService.findRelatorioSinteticoColetaEntrega(parameters);

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
		
		parametersReport.put("dataSource1", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource2", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource3", new JRBeanCollectionDataSource(listaComumOrdered));
		parametersReport.put("dataSource4", new JRBeanCollectionDataSource(listaColetaEntregaOrdedered));
		parametersReport.put("dataSource5", new JRBeanCollectionDataSource(listaColetaEntregaOrdedered));

		jr.setParameters(parametersReport);

		return jr;
	}

}
