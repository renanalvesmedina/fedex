package com.mercurio.lms.sim.reports;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.tools.ant.filters.StringInputStream;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;

public class GerarDanfeService extends JRRemoteReportsRunner {

	public GerarDanfeService(Locale locale, String reportHostUrl) {
		super(locale, reportHostUrl);
	}

	public File execute(String xmlString, Locale locale) throws Exception {

		InputStream xmlInputStream = new StringInputStream(xmlString, "UTF-8");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactoryImpl.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlInputStream);
		
		JRXmlDataSource jRXmlDataSource = new JRXmlDataSource(doc, "/nfeProc/NFe/infNFe/det");
		
		ClassPathResource jasperInputStream = new ClassPathResource("com/mercurio/lms/sim/reports/gerarDanfe.jasper");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(JRParameter.REPORT_LOCALE, locale);

		return generateReportFile(JasperFillManager.fillReport(
				jasperInputStream.getInputStream(), parameters, jRXmlDataSource));

	}
}
