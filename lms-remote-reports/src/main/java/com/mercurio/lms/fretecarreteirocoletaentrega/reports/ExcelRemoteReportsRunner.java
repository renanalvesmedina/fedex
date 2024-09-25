package com.mercurio.lms.fretecarreteirocoletaentrega.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class ExcelRemoteReportsRunner {	
		private String reportHostUrl;
		protected abstract String getReportName();
		protected abstract HSSFWorkbook generateWorkbook(InputStream xlsModel) throws IOException;
	private Logger log = LogManager.getLogger(this.getClass());
		
		public ExcelRemoteReportsRunner(String reportHostUrl){
			this.reportHostUrl = reportHostUrl;
		}
		
		private File reportOutputDir;
		
		private static final Log logger = LogFactory.getLog(ExcelRemoteReportsRunner.class);		
		
		public File executeReport() throws Exception{
			return generateReportFile(getReportName());
		}
		
		private File generateReportFile(String reportName) throws Exception{	
			//Cria arquivo temp
			File reportOutputDir = generateOutputDir();			
			File reportFile = File.createTempFile("report-" + reportName + "-", ".xls", reportOutputDir);			
			FileOutputStream fout = new FileOutputStream(reportFile);			
			InputStream xlsModel = Thread.currentThread().getContextClassLoader().getResourceAsStream(reportName + "Model.xls");
			//Chama a função principal para geração do xls, passando o modelo lido via inputstream
			//Arquivo xls de modelo deve estar no classpath
			HSSFWorkbook wb = generateWorkbook(xlsModel);			
			try {
				//Escreve workbook no arquivo temporario
			    wb.write(fout);
			    fout.close();
			} catch (FileNotFoundException e) {
			    log.error(e);
			} catch (IOException e) {
			    log.error(e);
			}			
			return reportFile;
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
	}
