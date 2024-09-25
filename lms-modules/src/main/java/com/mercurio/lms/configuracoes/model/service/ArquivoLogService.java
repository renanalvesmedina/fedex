package com.mercurio.lms.configuracoes.model.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ArquivoLog;
import com.mercurio.lms.configuracoes.model.dao.ArquivoLogDAO;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.arquivoLogService"
 */
public class ArquivoLogService extends CrudService<ArquivoLog, Long> {
	
	/**
	 * Retorna uma lista com os erros encontrados nos logs selecionados.
	 * 
	 * @param criteria
	 * @return 
	 */
	public List findLogsErro(final DateTime dtInicial, final DateTime dtFinal, final String servidor){
		if( ("").equals(servidor) || servidor == null) {
			throw new BusinessException("LMS-05182");
		}
		
		if(dtInicial == null || dtFinal == null || dtInicial.compareTo(dtFinal) >= 0) {
			throw new BusinessException("LMS-04098");
		} 

		if(new Period(dtInicial, dtFinal).getMonths() > 1){
			throw new BusinessException("LMS-05183");
		}
		
		final List<String> paths = new ArrayList<String>();

		if(("00").equals(servidor) ) {
			paths.add("\\\\lx-swlms04\\lmsalogs\\"); 
			paths.add("\\\\lx-swlms05\\lmsalogs\\");
			paths.add("\\\\lx-swlms06\\lmsalogs\\");
		} else {
			paths.add("\\\\lx-swlms"+servidor+"\\lmsalogs\\"); 
		}
								 						 
		
		final List<String> filesName = getFilesNames(dtInicial, dtFinal, paths);
		
		final boolean is503 = false;
		
		final List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			for (String fileName : filesName) {
				try {
					// Create a pattern to match comments
					final Pattern p;
					
					if(is503){ 
						p = Pattern.compile("\\d+.\\d+.\\d.\\d+\\s-\\s-\\s\\[\\d{2}/[JFMASOND][aebugo][nvrilotz]/\\d{4}:\\d{2}:\\d{2}:\\d{2}\\s[+-]\\d{4}\\]\\s\\[\\w+\\]\\s\"\\w+\"\\s500\\s304", Pattern.MULTILINE);
						
					} else {
						p = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d{3}\\sERROR[^\\n]*\\n(\\D[^\\n]*\\n)*)|(^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d{3}\\sWARN[^\\n]*\\n;[^\\n]*)", Pattern.MULTILINE);
					}
					
					// Get a Channel for the source file
					fis = new FileInputStream(fileName);
					fc = fis.getChannel();
					
					// Get a CharBuffer from the source file
					final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int)fc.size());
					final Charset cs = Charset.forName("UTF-8");
					final CharsetDecoder cd = cs.newDecoder();
					final CharBuffer cb = cd.decode(bb);
					
					// Run some matches
					final Matcher m = p.matcher(cb);
					
					Map<String, Object> map;
					DateTime date;
					while (m.find()) {
					    final String line = m.group(); 
					    
					    map = new HashMap<String, Object>();
					    
					    if(is503){
					    	date = getDataLog(line, true);
					    	
					    	if(date.isAfter(dtFinal) ){
					    		break;
					    	} else if(date.isBefore(dtInicial)){
					    		continue;
					    	}
					    	
					    	map.put("time",     date );
					    	map.put("errorMsg", null ); 	
					    	map.put("local",    line.substring( ( (line.indexOf("\"POST") >= 0) ?  
					    										   line.indexOf("\"POST")   	: 
					    										   line.indexOf("\"GET")
					    										) + 1, line.indexOf("\" 503"))
					    			);
					    	
					    	map.put("ipUser",   line.substring(0,line.indexOf(" ")));
					    	map.put("type",		"503");
					    	map.put("file",     fileName);
					    	map.put("errorDs",  line);
					    	
					    } else {
					    	date = getDataLog(line, false);
	
					    	if(date.isAfter(dtFinal) ){
					    		break;
					    		
					    	} else if(date.isBefore(dtInicial)){
					    		continue;
					    	}
					    	
					    	final String subLine = line.substring(0,line.indexOf("\n"));
					    	final String[] dadosLine = subLine.split("]");
					    	
					    	map.put("time",     date );
					    	map.put("errorMsg", subLine.substring(subLine.indexOf(dadosLine[1]+"]") + dadosLine[1].length()+1).trim());
					    	map.put("local",    dadosLine[0].substring(dadosLine[0].indexOf("[")+1));
					    	map.put("ipUser",   dadosLine[1].substring(dadosLine[1].indexOf("[")+1)); 				
					    	map.put("type",		getType(subLine)); 					
					    	map.put("file",     fileName); 							
					    	map.put("errorDs",  line);								
					    }
						
						list.add(map);
					}
				} finally {
					if(fis != null) {
						fis.close();
					}
					
					if(fc != null){
						fc.close();
					}
				}
			}
			
		} catch (Exception e) {
			throw new BusinessException("", e);
		}
		
		return list;
	}

	/**
	 * Retorna os arquivos que serão processados.
	 */
	private List<String> getFilesNames(final DateTime dtInicial, final DateTime dtFinal, final List<String> paths) {
		final List<String> result = new ArrayList<String>();
		DateTime dtFile;

		String lastFile = null;
		boolean inseriu = false;
		for(String path : paths){
			for (File file : getFilesOrderedByModification(new File(path))) {
				if(file.isFile() && file.getName().indexOf("lms.log") >= 0){
					dtFile = new DateTime(file.lastModified());
	
					//	dtFile deve ser >= dtInicial && <= dtFinal
					if( ( dtFile.isAfter(dtInicial) || dtFile.isEqual(dtInicial) ) &&
							( dtFile.isBefore(dtFinal) || dtFile.isEqual(dtFinal) )
							){

						
						result.add(file.getAbsolutePath());
						inseriu = true;
						
					} else {
						if(inseriu){
							inseriu = false;
							lastFile = file.getAbsolutePath();
						}
					}
				}
			}
			
			// Deve adicionar o arquivo seguinte ao último inserido
			if(lastFile != null){
				result.add(lastFile);
				lastFile = null;
			}
		} 
		
		return result;
	}

	
	/**
	 * Método que efetua a ordenação da lista de arquivos (por Data de Modificação)
	 * do diretório, visando possibilitar recuperar o arquivo seguinte ao último 
	 * pertencente ao período informado, tal é necessário pois no mesmo pode conter 
	 * dados pertencentes ao período.
	 * 
	 * @param dir - Diretório contendo arquivos a serem ordenados
	 * 
	 * @return arquivos ordenados por Data de Modificação
	 */
	private File[] getFilesOrderedByModification(final File dir){
		final File[] files = dir.listFiles();

		Arrays.sort(files, new Comparator<File>() {
			public int compare(File file1, File file2) {
				return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
			}
		});
		
		return files;
	}
	
	
	private String getType(final String line) {
		 final Matcher matcherData = Pattern.compile("ERROR|WARN").matcher(line);

	    if(matcherData != null && matcherData.find()) {
	    	return matcherData.group();
	    } else {
	    	return null;
	    }
	}

	private static DateTime getDataLog(final String line, final boolean is503) {
		String time = null;
	    DateTime dateTime = null;
	    final Matcher matcherData;
	    
	    if(is503){
	    	matcherData = Pattern.compile("(\\d{2}/[JFMASOND][aebugo][nvrilotz]/\\d{4}:\\d{2}:\\d{2}:\\d{2})")
	    					     .matcher(line);
	    } else {
	    	matcherData = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})|(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2})")
	    					  	 .matcher(line);
	    }
	    
	    if(matcherData != null && matcherData.find()) {
	    	time = matcherData.group();
	    	if(is503){
	    		dateTime = DateTimeFormat.forPattern("dd/MMM/yyyy:HH:mm:ss").withZone(SessionUtils.getFilialSessao().getDateTimeZone()).parseDateTime(time);
	    		
	    	} else {
	    		dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(SessionUtils.getFilialSessao().getDateTimeZone()).parseDateTime(time);
	    	}
	    }
	    return dateTime;
	}
	
	
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ArquivoLogDAO getArquivoLogDAO() {
		return (ArquivoLogDAO) getDao();
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setArquivoLogDAO(ArquivoLogDAO dao) {
        setDao( dao );
    }
}