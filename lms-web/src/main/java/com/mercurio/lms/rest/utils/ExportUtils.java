package com.mercurio.lms.rest.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Realiza a exportação dos dados para um arquivo CSV.
 * 
 */
public class ExportUtils {

	private static final Log log = LogFactory.getLog(ExportUtils.class);
	
	private static final String DEFAULT_CSV_SEPARATOR = ";";
	
	/**
	 * Executa a escrita dos dados para um arquivo CSV.
	 * 
	 * @param reportOutputDir
	 * @param name - Este parâmetro pode contero valor 'file.name:' com o 
	 * 		nome do arquivo a ser preenchido a ser construído, assim não irá gerar nome aleatoriamente
	 * @param list
	 * @param columns
	 * 
	 * @return String
	 * @throws IOException
	 */
	public static String exportCsv(File reportOutputDir, String name, List<Map<String, Object>> list, List<Map<String, String>> columns) throws IOException {
		
		File reportFile = doNameReport("report-"+name, reportOutputDir);
		
		OutputStreamWriter bos = new OutputStreamWriter(new FileOutputStream(reportFile),Charset.forName("ISO-8859-1"));
		long inicio = System.currentTimeMillis();
		log.debug("[Geracao CSV] Iniciando escrita do arquivo temporario");
		PrintWriter pw = new PrintWriter(bos);
		try {
			
			for (Map<String, String> column: columns) {
				pw.write(column.get("title")+";");
			}
			pw.write("\n");
			
			for (Map<String, Object> m: list) {
				for (Map<String, String> column: columns) {		
					Object object = m.get(column.get("column"));
					
					pw.write(getValue(object));
				}
				pw.write("\n");
			}
			pw.flush();
		} finally {
			pw.close();
		}
		log.debug("[Geracao CSV] Tempo escrita arquivo temporario: " + (System.currentTimeMillis() - inicio) + " ms");
		
		return reportFile.getName();
	}

	public static File exportFileCsv(File reportOutputDir, String name, List<Map<String, Object>> list, List<Map<String, String>> columns) throws IOException {
		String userName = SessionUtils.getUsuarioLogado().getLogin();
		String dateTime = LocalDateTime.now().toString().replace("-", "").replace(":", "").replace("T", "").replace(".", "");
		File reportFile = doNameReport(String.format("%s-relnaofaturados-%s-", userName,dateTime), reportOutputDir);

		OutputStreamWriter bos = new OutputStreamWriter(new FileOutputStream(reportFile),Charset.forName("ISO-8859-1"));
		long inicio = System.currentTimeMillis();
		log.debug("[Geracao CSV] Iniciando escrita do arquivo temporario");
		PrintWriter pw = new PrintWriter(bos);
		try {

			for (Map<String, String> column: columns) {
				pw.write(column.get("title")+";");
			}
			pw.write("\n");

			for (Map<String, Object> m: list) {
				for (Map<String, String> column: columns) {
					Object object = m.get(column.get("column"));

					pw.write(getValue(object));
				}
				pw.write("\n");
			}
			pw.flush();
		} finally {
			pw.close();
		}
		log.debug("[Geracao CSV] Tempo escrita arquivo temporario: " + (System.currentTimeMillis() - inicio) + " ms");

		return reportFile;
	}
	
	private static File doNameReport(String name, File reportOutputDir) throws IOException {
		
		if (name != null && name.contains("file.name:")) {
			return File.createTempFile(name.substring(name.indexOf("file.name:") + 10), ".csv", reportOutputDir);
		} else {
			return File.createTempFile(name, ".csv", reportOutputDir);
		}
	}
	
	/**
	 * Formata o valor de acordo com o seu tipo de dado.
	 * 
	 * @param o
	 * 
	 * @return String
	 */
	private static String getValue(Object o){		
		if (o == null) {
			return DEFAULT_CSV_SEPARATOR;
		}
		
		if(o.getClass().isAssignableFrom(DomainValue.class)){
			DomainValue domainValue = (DomainValue) o;
			return domainValue.getDescriptionAsString().concat(DEFAULT_CSV_SEPARATOR);
		} 
		
		if(o.getClass().isAssignableFrom(YearMonthDay.class)){
			YearMonthDay yearMonthDay = (YearMonthDay) o;
			return JTDateTimeUtils.formatDateYearMonthDayToString(yearMonthDay).concat(DEFAULT_CSV_SEPARATOR);
		}
		
		if(o.getClass().isAssignableFrom(DateTime.class)){			
			DateTime dateTime = (DateTime) o;
			return JTDateTimeUtils.formatDateTimeToString(dateTime).concat(DEFAULT_CSV_SEPARATOR);
		}
		
		if(o.getClass().isAssignableFrom(String.class)){			
			String string = (String) o;
			return StringUtils.chomp(string).concat(DEFAULT_CSV_SEPARATOR);
		}
		
		return o.toString().concat(DEFAULT_CSV_SEPARATOR);		
	}
}