package com.mercurio.lms.expedicao.reports;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.tools.ant.filters.StringInputStream;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Service responsável por criar um relatório de uma nota fiscal conjugada.
 * 
 */
public class NFEConjugadaReportService extends JRRemoteReportsRunner {

	private static final String LMS_URL = "LMS_URL";
	private static final String REPORT_HOST = "adsm.report.host";
	private static final String REPORT_TEMPLATE = "com/mercurio/lms/expedicao/reports/impressaoNFEConjugada.jasper";
	private static final String REPORT_DS_START = "/enviNFe/NFe/infNFe/det";
	
	public NFEConjugadaReportService(Locale locale, String reportHostUrl) {
		super(locale, reportHostUrl);
	}

	/**
	 * Executa a criação um jasper print do xml de uma nota fiscal conjugada.
	 * 
	 * @param xmls
	 * @param locale
	 * @return JasperPrint
	 * 
	 * @throws Exception
	 */
	public JasperPrint execute(List<Object[]> xmls, Locale locale) throws Exception {
		JasperPrint jasperPrint = null;
				
		ClassPathResource jasperInputStream = new ClassPathResource(REPORT_TEMPLATE);
		
		for (Object[] xml : xmls) {
			JRXmlDataSource jRXmlDataSource = getXMLDataSource((byte[]) xml[0]);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			parameters.put(LMS_URL, System.getProperty(REPORT_HOST));
			
			JasperPrint page = JasperFillManager.fillReport(jasperInputStream.getInputStream(), parameters, jRXmlDataSource);
			
			jasperPrint = addPage(jasperPrint, page);			
		}
		
		return jasperPrint;				
	}

	@SuppressWarnings("unchecked")
	private JasperPrint addPage(JasperPrint jasperPrint, JasperPrint page) {
		if(jasperPrint == null){
			jasperPrint = page;				
		} else {
			jasperPrint.getPages().add(page);
		}
		
		return jasperPrint;
	}

	private JRXmlDataSource getXMLDataSource(byte[] xml)
			throws UnsupportedEncodingException, ParserConfigurationException,
			SAXException, IOException, JRException {
		InputStream xmlInputStream = new StringInputStream(new String(xml, "UTF-8"), "UTF-8");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactoryImpl.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlInputStream);
		
		return new JRXmlDataSource(doc, REPORT_DS_START);
	}
}
