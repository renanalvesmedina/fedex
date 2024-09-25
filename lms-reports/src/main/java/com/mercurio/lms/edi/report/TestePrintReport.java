package com.mercurio.lms.edi.report;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestePrintReport {

	private static final Logger LOGGER = LogManager.getLogger(TestePrintReport.class);
	
	public static void main(String[] args) {
			JasperPrint   jasperPrint=null;
	        try{
               InputStream inputStream = new FileInputStream("c:\\TesteJonas.jasper");
               ArrayList lista = new ArrayList();
               lista.add("teste");
               JRBeanCollectionDataSource beanColDataSource = new  JRBeanCollectionDataSource(lista);
               /* this portion of code is only required if you want to compile on the fly  */
               jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap(), beanColDataSource);

	            long start = System.currentTimeMillis();
	            
	            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

	            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
	            JRPrintServiceExporter exporter = new JRPrintServiceExporter();

	            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
	            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
	            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
	            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
	            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);

	            exporter.exportReport();

	        }catch(Exception e){
                LOGGER.error(e);
            }
	}

}
