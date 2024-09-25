package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoItemService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public class EmitirPreFaturaServicoPlanilhaAnaliticoService extends ReportServiceSupport {
	private PreFaturaServicoItemService preFaturaServicoItemService;

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		Long idPreFatura = (Long)parameters.get("idPreFaturaServico");
		String tpRelatorio = (String)parameters.get("tpRelatorio");
		final List<Map<String, Object>> itens = preFaturaServicoItemService.findReportEmissaoPreFaturaItem(idPreFatura, tpRelatorio);
		
		JRReportDataObject jr = new JRReportDataObject() {			
			Map parameters = new HashMap();
			@Override
			public void setParameters(Map arg0) {
				parameters = arg0;
			}
			@Override
			public Map getParameters() {
				return parameters;
			}
			@Override
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(itens);
			}
		};

		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_CSV);
		reportParameters.put("filialEmissora", (String)parameters.get("sgFilialCobranca"));
		reportParameters.put("numero", FormatUtils.formataNrDocumento(((Long)parameters.get("nrPreFatura")).toString(), "PFS"));
		reportParameters.put("emissao", JTDateTimeUtils.formatDateTimeToString((DateTime)parameters.get("dhGeracao")));
		String tomador = (String)parameters.get("nrIdentificacao") + " " + (String)parameters.get("nmPessoa");
		reportParameters.put("clienteTomador", tomador);
		
		jr.setParameters(reportParameters);
		
		return jr;
	}
	
	public void setPreFaturaServicoItemService(
			PreFaturaServicoItemService preFaturaServicoItemService) {
		this.preFaturaServicoItemService = preFaturaServicoItemService;
	}
}