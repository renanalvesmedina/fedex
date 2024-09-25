package com.mercurio.lms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.lms.facade.radar.impl.PosicaoFinanceiraFacadeImpl;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class FileUtils {

	private static final Logger LOGGER = LogManager.getLogger(PosicaoFinanceiraFacadeImpl.class);
	
	private static final int BEGIN_FILENAME = 0;
	private static final int BEGIN_EXTENSION_FILE = 1;
	private static final int END_FILENAME = 1024;
	private static final String SEPARATOR = ";";
	private static final String PREFIX = "report";
	private static final String SUFFIX = ".csv";
	
	public static byte[] readFile(File reportFile) {
		byte[] input = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(reportFile);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] temp = new byte[END_FILENAME];
			int read;
			while((read = fi.read(temp)) >= BEGIN_FILENAME){
				  buffer.write(temp, BEGIN_FILENAME, read);
			}
			if(buffer != null){
				input = buffer.toByteArray();
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("Problemas arquivo não encontrado", e1);
		}catch (IOException e2) {
			LOGGER.error("Problemas Entrada ou saida", e2);
		}finally{
			if(fi != null){
				try {
					fi.close();
				} catch (IOException e) {
					LOGGER.error("Problemas ao fechar a conexão do FileInputStream", e);
				}
			}
		}
		return input;
	}
	
	public static String getFileNameFromBlob(byte[] arquivo){
		byte[] fileName = Arrays.copyOfRange(arquivo, BEGIN_FILENAME, END_FILENAME);
		String str = new String(fileName);
		return str.trim();		
	}
		
	public static String getFileExtensionFromBlob(byte[] arquivo) {
		String fileName = getFileNameFromBlob(arquivo);
		return fileName.substring(fileName.indexOf(".") + BEGIN_EXTENSION_FILE );
	}
	
	
	public static File getFileByByteArray(byte[] tmpAnexo, String name, String extension) {
		byte[] anexo = Arrays.copyOfRange(tmpAnexo, 1024, tmpAnexo.length);
		InputStream is = new ByteArrayInputStream(anexo);
		
		File file = null;
		try {
			file = File.createTempFile(name, "." + extension);
			file.deleteOnExit();
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);  
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));
			
			InputStreamReader isReader = new InputStreamReader(is, Charset.forName("ISO-8859-1"));

			int c;  
			while ((c = isReader.read()) != -1) {  
				outputStreamWriter.write(c);  
			}  
			outputStreamWriter.close();
			fileOutputStream.close();
			return file;
		} catch (IOException e) {
			throw new InfrastructureException(e.getMessage());
		}	
	}
	
	public static File generateReportFile(List<Map<String, Object>> listaParaCsv,
			File reportOutputDir, String reportName) {
		
		try {
			File reportFile = File.createTempFile(PREFIX, SUFFIX, reportOutputDir);
			FileOutputStream out = new FileOutputStream(reportFile);

			if (listaParaCsv != null && !listaParaCsv.isEmpty()) {
				Map<String, Object> mapCsv = FranqueadoUtils.convertMappedListToCsv(reportName, listaParaCsv, SEPARATOR);
				out.write(mapCsv.get(reportName).toString().getBytes());
}

			out.flush();
			out.close();
			return reportFile;
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
	}
	
}
