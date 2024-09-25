package com.mercurio.lms.franqueados.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class FranqueadoReportUtils {
	
	final static String percent = "%";
	final static BigDecimal ONE_HUNDRED = new BigDecimal(100L);
	final static MathContext context = new MathContext(6, FranqueadoUtils.ROUND_MODE);
	
	public final static String ZIP_FILE_EXTENSION = ".zip";
	public final static String CSV_FILE_EXTENSION = ".csv";
	public final static String PDF_FILE_EXTENSION = ".pdf";
	
	public final static String EXPORT_TYPE_CSV = "CSV";
	public final static String EXPORT_TYPE_PDF = "PDF";
	
	public final static int EMPTY_PDF_FILE = 762;
	
	
	public static BigDecimal divide(BigDecimal dividendo, BigDecimal divisor){
		if(divisor == null || divisor.equals(BigDecimal.ZERO) || dividendo == null || dividendo.equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		BigDecimal result = dividendo.divide(divisor,4,FranqueadoUtils.ROUND_TYPE);
		
		return result;
	}

	public static BigDecimal sum(BigDecimal... valores){
		if(valores == null){
			return BigDecimal.ZERO;
		}

		BigDecimal result = new BigDecimal(0L);
		for (BigDecimal valor : valores) {
			if(valor == null){
				continue;
			}
			result = result.add(valor);
		}
		
		return result;
	}
	
	public static BigDecimal toPercentage(BigDecimal valor){
		if(valor == null || valor.equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		BigDecimal result = valor.multiply(ONE_HUNDRED,context);
		
		return result;
	}

	
	public static Date timestampToDate(Timestamp timestamp){
		Date date = null;

		if(timestamp instanceof Timestamp){
			Timestamp time = (Timestamp)timestamp;
			date = new Date(time.getTime());
		}

		return date;
	}
	
	
	public static void zipFile(List<File> files,String zipFileName) throws IOException{
		 
	     byte[] buffer = new byte[1024];
	     
	    boolean hasAtLeastOneNotEmptyFile = false;
	    
		for(File file : files){
			if(isNotEmptyFile(file)){
				hasAtLeastOneNotEmptyFile = true;
				break;
			}
		}
		
	    FileOutputStream fos = new FileOutputStream(zipFileName);
	    if(hasAtLeastOneNotEmptyFile){

		    ZipOutputStream zos = new ZipOutputStream(fos);
	
		    for(File file : files){
				if(isNotEmptyFile(file)){
			    	ZipEntry ze= new ZipEntry(file.getName());
			        zos.putNextEntry(ze);
			 
			        FileInputStream in = new FileInputStream(file);
			 
			        int len;
			        while ((len = in.read(buffer)) > 0) {
			        		zos.write(buffer, 0, len);
			        }
			 
			        in.close();
				}
		    }
		    zos.closeEntry();
		    zos.close();
	    }

	}
	
	public static String toSinglePDF(List<File> reports, String outputName) throws FileNotFoundException, IOException, COSVisitorException {
		PDFMergerUtility merger = new PDFMergerUtility();
		
	    boolean hasAtLeastOneNotEmptyFile = false;
	    
		for(File file : reports){
			if(file.length() > EMPTY_PDF_FILE){
				hasAtLeastOneNotEmptyFile = true;
				break;
			}
		}
		
		if(hasAtLeastOneNotEmptyFile){
			for (File file : reports) {
				FileInputStream fileInputStream = new FileInputStream(file);
	
				if(fileInputStream.available() > EMPTY_PDF_FILE){
					merger.addSource(fileInputStream);
				}
			}
		} else {
			merger.addSource(reports.get(0));
		}
		
		merger.setDestinationFileName(outputName);
		merger.mergeDocuments();
		
		return merger.getDestinationFileName();
	}

	private static boolean isNotEmptyFile(File file) throws IOException{
		if(file == null){
			return false;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(file));     
		if (br.readLine() == null) {
			br.close();
			return false;
		}
		br.close();
		return true;
	}
}
