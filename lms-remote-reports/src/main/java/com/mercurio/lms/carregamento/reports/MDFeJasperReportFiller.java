package com.mercurio.lms.carregamento.reports;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;

/**
 * Classe encarregada de gerar a impressao do MDFe.
 */
public class MDFeJasperReportFiller {

	public static final String ADSM_REPORT_HOST = "adsm.report.host";

	private static final Logger LOGGER = LogManager.getLogger(MDFeJasperReportFiller.class);

	public static JasperPrint fillXmlJasperReport(List<byte[]> dsXmls, List<Long> protocolos, List<String> observacoes, Locale locale, InputStream inputStream,
			String reportHostUrl, String obsContingMdfe1, String obsContingMdfe2, Map<String,List<Map<String,String>>> listChaveCte) {
		String xmlUnificado = getXmlUnificado(dsXmls, protocolos, observacoes);
		return generateJasperPrint(new StringInputStream(xmlUnificado, "UTF-8"), locale, inputStream, reportHostUrl, obsContingMdfe1, obsContingMdfe2, listChaveCte);
	}

	/**
	 * Unifica lista de xmls de MDFe em um único xml para geração do report,
	 * adicionando as tags para o protocolo e a observação quando necessário.
	 * 
	 * @param dsXmls
	 * @param protocolos
	 * @param observacoes
	 * @return
	 */
	private static String getXmlUnificado(List<byte[]> dsXmls, List<Long> protocolos, List<String> observacoes) {
		Integer count = 0;
		StringBuilder allMDFeXml = new StringBuilder("<MDFeList>");

		for (byte[] dsXml : dsXmls) {

			if (dsXml != null) {
				StringBuilder xml = new StringBuilder(new String(dsXml, Charset.forName("UTF-8")));

				if (protocolos.get(count) != null) {
					int index = xml.indexOf("</dadosAdic>");
					if (index > -1) {
						xml.insert(index, "<nrProtocolo>" + protocolos.get(count) + "</nrProtocolo>");
					}
				}

				if (observacoes.get(count) != null) {
					int index = xml.indexOf("</dadosAdic>");
					if (index > -1) {
						xml.insert(index, "<observacoesManifesto>" + StringEscapeUtils.escapeXml(observacoes.get(count).replace(";", "\n")) + "</observacoesManifesto>");
					}
				}

				allMDFeXml.append(xml);
			}

			if (allMDFeXml.indexOf("<?") != -1) {
				allMDFeXml.replace(allMDFeXml.indexOf("<?"), allMDFeXml.indexOf("?>") + 2, "");
			}

			count++;
		}
		
		allMDFeXml.append("</MDFeList>");

		return allMDFeXml.toString();
	}

	/**
	 * @param xmlInputStream
	 * @param obsContingMdfe1 
	 * @param obsContingMdfe2 
	 * @param listChaveCte 
	 * @return
	 */
	private static JasperPrint generateJasperPrint(InputStream xmlInputStream, Locale locale, InputStream jasperInputStream, String reportHostUrl, String obsContingMdfe1, String obsContingMdfe2, Map<String,List<Map<String,String>>> listChaveCte) {

		JasperPrint jasperPrint = null;
		
		try {
			DocumentBuilderFactoryImpl dbFactory = (DocumentBuilderFactoryImpl) DocumentBuilderFactoryImpl.newInstance();
			DocumentBuilderImpl dBuilder = (DocumentBuilderImpl) dbFactory.newDocumentBuilder();
			DocumentImpl doc = (DocumentImpl) dBuilder.parse(xmlInputStream);

			if (StringUtils.isBlank(reportHostUrl)) {
				reportHostUrl = System.getProperty(ADSM_REPORT_HOST);
			}

			JRXmlDataSource ds = new JRXmlDataSource(doc, "/MDFeList/MDFe/infMDFe");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, locale);
			parameters.put("LMS_URL", reportHostUrl);
			parameters.put("OBS_CONTING_MDFE1", obsContingMdfe1);
			parameters.put("OBS_CONTING_MDFE2", obsContingMdfe2);
			parameters.put("LIST_CHAVE_CTES", listChaveCte);
			parameters.put("QR_CODE", doc.getElementsByTagName("qrCodMDFe").item(0) != null ? doc.getElementsByTagName("qrCodMDFe").item(0).getTextContent() : null);

			if (jasperInputStream == null) {
				jasperInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/mercurio/lms/carregamento/reports/impressaoMDFe.jasper");
			}

			jasperPrint = JasperFillManager.fillReport(jasperInputStream, parameters, ds);

		} catch (Exception e) {
			LOGGER.error(e);
			jasperPrint = new JasperPrint();
		}
		return jasperPrint;
	}
}
