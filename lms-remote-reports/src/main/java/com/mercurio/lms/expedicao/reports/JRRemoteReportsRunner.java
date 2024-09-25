package com.mercurio.lms.expedicao.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Responsável por gerar o arquivo para impressao dos relatorios remotos. 
 * 
 * @author vagnerh
 *
 */
public class JRRemoteReportsRunner {
	
	private Locale locale;
	private String reportHostUrl;
	public JRRemoteReportsRunner(String reportHostUrl){
		this.locale = new Locale("pt", "BR");
		this.reportHostUrl = reportHostUrl;
	}
	
	public JRRemoteReportsRunner(Locale locale,String reportHostUrl){
		this.locale = locale; 
		this.reportHostUrl = reportHostUrl;
	}
	
	private File reportOutputDir;
	
	private static final Log logger = LogFactory.getLog(JRRemoteReportsRunner.class);
	
	
	public File executeReport(List<Map<String, Object>> cteXML, Integer nrViasCTe) throws Exception{
		return generateReportFile(CTEJasperReportFiller.fillXmlJasperReport(cteXML, nrViasCTe, this.locale, reportHostUrl));
	}
	
	public File executeReport(String cteXML, Long nrProtocolo) throws Exception{
		return generateReportFile(CTEJasperReportFiller.fillXmlJasperReport(cteXML,nrProtocolo,this.locale,reportHostUrl));
	}
	
	public File executeReport(String cteXML, Long nrProtocolo, String reportName) throws Exception{
		return generateReportFile(CTEJasperReportFiller.fillXmlJasperReport(cteXML,nrProtocolo,this.locale,reportHostUrl),reportName);
	}
	
	protected File generateReportFile(JasperPrint jasperPrint) throws Exception{
		return generateReportFile(jasperPrint, null);
		
	}
	
	public File generateReportFile(JasperPrint jasperPrint, String reportName) throws Exception{
		jasperPrint = removeLastBlankPageCte(jasperPrint);
		return createPdf(jasperPrint, reportName);
	}

	public File createPdf(JasperPrint jasperPrint, String reportName)
			throws IOException, FileNotFoundException, JRException {
		File reportOutputDir = generateOutputDir();
		
		String fileName = reportName;
		if (reportName == null){
			fileName = "report-JRNAME-";
		}
		
		File reportFile = File.createTempFile(fileName, ".pdf", reportOutputDir);
		
		FileOutputStream fout = new FileOutputStream(reportFile);
		JasperExportManager.exportReportToPdfStream(jasperPrint, fout);
		return reportFile;
	}

	@SuppressWarnings("unchecked")
	public JasperPrint removeLastBlankPageCte(final JasperPrint jasperPrint) {
		List<JRPrintPage> pages =  (List<JRPrintPage>) jasperPrint.getPages();
        
        if (pages.size() > 0){
        	for (int index = 0; index <pages.size(); index ++){
            	if (pages.get(index).getElements().size()<=10){
            		pages.remove(index);
            	}
            }
        }
        
        return jasperPrint;
	}
	
	public File generateOutputDir() {
		if (SystemUtils.IS_OS_WINDOWS == false) {
			//No Linux o diretorio de relatorios é fixo /reports 
			this.reportOutputDir = new File("/reports");
		} else {
			// Nas maquinas Windows usar o diretorio temporário do usuario
			this.reportOutputDir = new File(System.getProperty("java.io.tmpdir"), "reports");
		}
		// valida a existência do diretório de saida dos reports.
		if (!this.reportOutputDir.exists()) {
			logger.info("Report output dir does not exist trying to create...");
			if (!this.reportOutputDir.mkdirs()) {
				logger.error("Cannot create report output dir: "+this.reportOutputDir);
				logger.error("ATENTION - Reports on this host will not be generated !!!");
			}
		}
		logger.info("Reports output temporary dir: "+this.reportOutputDir);
		
		return reportOutputDir;
	}

	public Locale getLocale() {
		return locale;
}

	public String getReportHostUrl() {
		return reportHostUrl;
	}
}
