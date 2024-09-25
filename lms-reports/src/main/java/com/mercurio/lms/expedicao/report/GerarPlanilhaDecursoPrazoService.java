package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;

public class GerarPlanilhaDecursoPrazoService extends ReportServiceSupport {
	private ConhecimentoService conhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
	
		parameters.put("dhBloqueio", getVlParametroYearMonthDay("DT_IMP_SA"));
		BigDecimal decPrazoParam = (BigDecimal)configuracoesFacade.getValorParametro("DEC_PRAZO");
		parameters.put("decPrazoParam", decPrazoParam);
		final List<Map<String, Object>> itens = conhecimentoService.findByDecursoPrazo(parameters);
		
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
	
	private YearMonthDay getVlParametroYearMonthDay(String parametro) {
		String date = (String)configuracoesFacade.getValorParametro(parametro);
		DateTime fake = DateTimeFormat.forPattern(DATE_FORMAT).parseDateTime(date);
		return new YearMonthDay(fake.getYear(), fake.getMonthOfYear(), fake.getDayOfMonth());
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}