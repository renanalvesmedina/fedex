package com.mercurio.lms.carregamento.reports;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mercurio.lms.expedicao.reports.CTeUtils;

/**
 * 
 * @author RafaelKF
 * 
 */
public class MDFeUtils extends CTeUtils {

	private static final Logger LOGGER = LogManager.getLogger(MDFeUtils.class);

	public static String formatCep(String cep) {
		String retorno = "";
		if (cep != null && cep != "") {
			if (cep.length() == 8) {
				retorno += cep.substring(0, 5) + "-" + cep.substring(5, 8);
			} else {
				retorno = cep;
			}
		}
		return retorno;
	}

	/**
	 * Recebe o número e retorna o número formatado com 8 zeros a esquerda.
	 */
	public static String formatLongWithZeros(Long number, String pattern) {
		String retorno = null;
		if (number != null) {
			DecimalFormat df = new DecimalFormat(pattern);
			retorno = df.format(number);
		}
		return retorno;
	}
	
	/**
	 * Adiciona os ctes do xml ao map
	 */
	public static Map<String,List<Map<String,String>>> putCtes(Map<String,List<Map<String,String>>> listChaveCte, byte[] xml) {
			
        String sxml;
        try {
            sxml = new String(xml, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            sxml = String.valueOf(xml);
        }
		StringReader sr = new StringReader(sxml);
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();          
			Document doc = db.parse(new InputSource(sr));
			doc.getDocumentElement().normalize();
			
			
			NodeList tpEmis = doc.getElementsByTagName("tpEmis");
			Node nodeTpEmis = tpEmis.item(0);
			String sTpEmis = nodeTpEmis.getTextContent();
			if ("2".equals(sTpEmis)) {
				
				NodeList infMDFe = doc.getElementsByTagName("infMDFe");
				Node nodeId = infMDFe.item(0);
				
				Element e = (Element)nodeId;
				String id = e.getAttribute("Id");
				
				List<Map<String,String>> ctes = new ArrayList<Map<String,String>>();
				NodeList chCTe = doc.getElementsByTagName("chCTe");
				int length = chCTe.getLength();
				int diferenca = 0;
				if (length < 120) {
					diferenca = 120 - length;
				} else {
					diferenca = 120 - length % 120;
				}
				for (int x = 0; x < length; x++) {
					Node nodeCte = chCTe.item(x);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("chaveCte", nodeCte.getTextContent());
					ctes.add(map);
					
				}
				for (int x = 0; x < diferenca; x++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("chaveCte", "");
					ctes.add(map);
				}
				
				listChaveCte.put(id, ctes);
				
			}
			
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			if(sr != null){
				sr.close();
			}
		}
		
	
		return listChaveCte;
		
	}

}
