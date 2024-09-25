package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoItemService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoService;

public class GerarPlanilhaServicosAdicionaisGeradosService extends ReportServiceSupport {
	private PreFaturaServicoService preFaturaServicoService;

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List<Map<String, Object>> itens = preFaturaServicoService.findGeracaoPlanilhaServicosAdicionaisGerados(parameters);
		
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
		jr.setParameters(reportParameters);
		return jr;
	}

	public void setPreFaturaServicoService(PreFaturaServicoService preFaturaServicoService) {
		this.preFaturaServicoService = preFaturaServicoService;
	}
}