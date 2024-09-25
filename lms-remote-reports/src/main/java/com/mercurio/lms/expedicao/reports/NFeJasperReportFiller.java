package com.mercurio.lms.expedicao.reports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;

/**
 * Classe encarregada de gerar a impressao do NFe.
 * 
 * 
 */
public class NFeJasperReportFiller {

	private static final Logger LOGGER = LogManager.getLogger(NFeJasperReportFiller.class);
	public static final String ADSM_REPORT_HOST = "adsm.report.host";

	@SuppressWarnings("rawtypes")
	public static JasperPrint executeFillXmlJasperReportRpst(int nrVias, String xml, List listNrNotas, Map<String, String> infRpst, List dsObservacaoDoctoServico, Locale locale, InputStream inputStream, String reportHostUrl) {
		return executeGenerateJasperPrintRpst(nrVias, new StringInputStream(xml, "UTF-8"), listNrNotas, infRpst,dsObservacaoDoctoServico,
				locale, inputStream, reportHostUrl);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JasperPrint executeGenerateJasperPrintRpst(int nrVias, InputStream xmlInputStream, List listNrNotas, Map<String, String> infRpst, List dsObservacaoDoctoServico,  Locale locale, InputStream jasperInputStream, String reportHostUrl) {

		JasperPrint jasperPrintAll = new JasperPrint();
		try {
			
			DocumentBuilderFactoryImpl dbFactory = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();
    		DocumentBuilderImpl dBuilder = (DocumentBuilderImpl)dbFactory.newDocumentBuilder();
    		DocumentImpl doc = (DocumentImpl)dBuilder.parse(xmlInputStream);

			
			if (StringUtils.isBlank(reportHostUrl)) {
				reportHostUrl = System.getProperty(ADSM_REPORT_HOST);
			}

			JRXmlDataSource ds = new JRXmlDataSource(doc, "/Rps/InfRps");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			parameters.put("LMS_URL", reportHostUrl);
			
			parameters.put("INF_RPST", infRpst);
			parameters.put("LIST_NR_NOTAS", listNrNotas);
			List observacoes = new ArrayList();
			if(dsObservacaoDoctoServico != null && dsObservacaoDoctoServico.get(0) != null){
				observacoes.add(dsObservacaoDoctoServico.get(0));
			}
		
			parameters.put("DS_OBSERVACAO_DOCTOSERVICO", observacoes);
			
			if (jasperInputStream == null) {
				jasperInputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("com/mercurio/lms/expedicao/reports/impressaoRPST.jasper");
			}
			jasperPrintAll = JasperFillManager.fillReport(jasperInputStream, parameters, ds);
			if (nrVias > 1) {
				List pages = new ArrayList();
				for (Object o: jasperPrintAll.getPages()) {
					pages.add(o);
				}
				for (int i = 2; i <= nrVias; i++) {
					jasperPrintAll.getPages().addAll(pages);
				}
			}

		}catch(Exception e) {
			LOGGER.error(e);
			jasperPrintAll = new JasperPrint();
		}
		return jasperPrintAll;
	}
	
	@SuppressWarnings("rawtypes")
	public static JasperPrint executeFillXmlJasperReportRpss(int nrVias, String xml, Map<String, String> infRpst, List dsObservacaoDoctoServico, Locale locale, InputStream inputStream, String reportHostUrl) {
		return executeGenerateJasperPrintRpss(nrVias, new StringInputStream(xml, "UTF-8"), infRpst,dsObservacaoDoctoServico,
				locale, inputStream, reportHostUrl);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static JasperPrint executeGenerateJasperPrintRpss(int nrVias, InputStream xmlInputStream, Map<String, String> infRpst, List dsObservacaoDoctoServico, Locale locale, InputStream jasperInputStream, String reportHostUrl) {
		JasperPrint jasperPrintAll = new JasperPrint();
		try {
			
			DocumentBuilderFactoryImpl dbFactory = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();
    		DocumentBuilderImpl dBuilder = (DocumentBuilderImpl)dbFactory.newDocumentBuilder();
    		DocumentImpl doc = (DocumentImpl)dBuilder.parse(xmlInputStream);

			
			if (StringUtils.isBlank(reportHostUrl)) {
				reportHostUrl = System.getProperty(ADSM_REPORT_HOST);
			}

			JRXmlDataSource ds = new JRXmlDataSource(doc, "/Rps/InfRps");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			parameters.put("LMS_URL", reportHostUrl);
			
			parameters.put("INF_RPSS", infRpst);
			List observacoes = new ArrayList();
			if(dsObservacaoDoctoServico != null && dsObservacaoDoctoServico.get(0) != null){
				observacoes.add(dsObservacaoDoctoServico.get(0));
			}
		
			parameters.put("DS_OBSERVACAO_DOCTOSERVICO", observacoes);

			if (jasperInputStream == null) {
				jasperInputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("com/mercurio/lms/expedicao/reports/impressaoRPSS.jasper");
			}
			jasperPrintAll = JasperFillManager.fillReport(jasperInputStream, parameters, ds);
			if (nrVias > 1) {
				List pages = new ArrayList();
				for (Object o: jasperPrintAll.getPages()) {
					pages.add(o);
				}
				for (int i = 2; i <= nrVias; i++) {
					jasperPrintAll.getPages().addAll(pages);
				}
			}


		}catch(Exception e) {
			LOGGER.error(e);
			jasperPrintAll = new JasperPrint();
		}
		return jasperPrintAll;
	}
}
