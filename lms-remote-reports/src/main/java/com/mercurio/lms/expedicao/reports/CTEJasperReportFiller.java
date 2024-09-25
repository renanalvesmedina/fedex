package com.mercurio.lms.expedicao.reports;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

/**
 * Classe encarregada de gerar a impressao do CTE.
 * @author vagnerh
 * 
 * @since LMSA-5774 em 16/11/2017
 * metodos da classe ajustadas para que impresso CTe considere mais de uma
 * versão de XML (SEFAZ) em uma unica solicitacao 
 * - refatorado os metodos publicos retirando o Input
 */
public class CTEJasperReportFiller {
	
	public static final String ADSM_REPORT_HOST = "adsm.report.host";
	private static final Logger LOGGER = LogManager.getLogger(CTEJasperReportFiller.class);

	/**
	 * Gera o JasperPrint contendo a impresssao do CTE.
	 * 
	 * @param cteXML Lista contendo os XMLs dos CTEs a serem impressas.
	 * @param nrViasCTe Numero de vias a serem impressas por CTE.
	 * @param reportHostUrl
	 * @return JasperPrint PDF CTe
	 */
	public static JasperPrint fillXmlJasperReport(List<Map<String, Object>> cteXML, Integer nrViasCTe, String reportHostUrl) {
		return generateJasperPrint(cteXML, nrViasCTe, null, reportHostUrl);
    }
	
	/**
	 * Gera o JasperPrint contendo a impresssao do CTE.
	 * 
	 * @param cteXML Lista contendo os XMLs dos CTEs a serem impressos.
	 * @param nrViasCTe Numero de vias a serem impressas por CTE.
	 * @param locale
	 * @param reportHostUrl
	 * @return JasperPrint PDF CTe
	 */
	public static JasperPrint fillXmlJasperReport(List<Map<String, Object>> cteXML, Integer nrViasCTe, Locale locale, String reportHostUrl) {
		return generateJasperPrint(cteXML, nrViasCTe, locale, reportHostUrl);
    }
	
	/**
	 * Gera o JasperPrint contendo a impresssao do CTE.
	 * 
	 * @param cteXML XML do CTE a ser impresso.
	 * @param nrProtocolo Numero de vias a serem impressas por CTE.
	 * @param reportHostUrl
	 * @return JasperPrint PDF CTe
	 */
	public static JasperPrint fillXmlJasperReport(String cteXML, Long nrProtocolo, String reportHostUrl) {
		return fillXmlJasperReport(cteXML, nrProtocolo, null, reportHostUrl);
    }
	
	/**
	 * Gera o JasperPrint contendo a impresssao do CTE.
	 * @param cteXML XML do CTE a ser impresso.
	 * @param nrProtocolo Numero de vias a serem impressas por CTE.
	 * @param locale
	 * @param reportHostUrl
	 * @return JasperPrint PDF CTe
	 */
	public static JasperPrint fillXmlJasperReport(String cteXML, Long nrProtocolo, Locale locale, String reportHostUrl) {
        StringBuilder xml = new StringBuilder(cteXML);
        if (cteXML != null && !cteXML.isEmpty() && nrProtocolo != null) {
            int index = xml.indexOf("</dadosAdic>");
            if (index > -1){
                xml.insert(index, "<nrProtocolo>" + nrProtocolo + "</nrProtocolo>");
            }
        }
        return generateJasperPrint(
                xml.toString(),
                null, 
                reportHostUrl);
    }
	
	/**
	 * @param xmlInputStream
	 * @return
	 */
	@SuppressWarnings("unchecked")
    private static JasperPrint generateJasperPrint(
			List<Map<String, Object>> cteXML, 
			int numeroViasCTe,
			Locale locale, 
			String reportHostUrl) {

		// se dados de XML para geracao do impresso estiver vazio retorna um relatorio vazio 
		if (cteXML.isEmpty()) {
			return new JasperPrint();
		}
		
		JasperPrint result = null;
        JasperPrint jasper = null;

		for (Map<String, Object> cte : cteXML) {
		    if (result == null) {
		        result = generateJasperPrint(cte.get("xml").toString(), locale, reportHostUrl);
	            jasper = result;
		    } else {
                jasper = generateJasperPrint(cte.get("xml").toString(), locale, reportHostUrl);
                result.getPages().addAll(jasper.getPages());
		    }
	        // se solicitado mais de uma via, volta a adicionar o relatorio gerado N vezes, quanto solicitado
	        for (int via = 2; via < numeroViasCTe; via++) {
                result.getPages().addAll(jasper.getPages());
	        }
		}
		
		return result;
	}
	
	
	/**
	 * Gerar o impresso CTe a partir de um unico XML CTe
	 * @param cteXml xml CTe
	 * @param locale
	 * @param reportHostUrl 
	 * @return JasperPrint
	 */
	private static JasperPrint generateJasperPrint(
			String cteXml, 
			Locale locale, 
			String reportHostUrl) {

		// se dados de XML para geracao do impresso estiver vazio retorna um relatorio vazio 
		if (cteXml == null || cteXml.isEmpty()) {
			return new JasperPrint();
		}

		JasperPrint jasperPrintResult = null;

        Map<String, Object> parameters = new HashMap<String,Object>();
        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put("LMS_URL", StringUtils.isBlank(reportHostUrl) ? System.getProperty(ADSM_REPORT_HOST) : reportHostUrl);
        parameters.put("LIST_CHAVE_CTE_NFE", getListChaveCteNfe(cteXml));
     
      cteXml =  cteXml.replace("&", "&amp;");   
        
    	try {
    		// forca a utilizacao do Factory da apache pois o DocumentBuilder do Oracle gera documents vazios
    		DocumentBuilderFactoryImpl dbFactory = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();
    		DocumentBuilderImpl dBuilder = (DocumentBuilderImpl) dbFactory.newDocumentBuilder();
    		
    		DocumentImpl document = (DocumentImpl) dBuilder.parse(new StringInputStream (cteXml, "UTF-8"));
	    	
            parameters.put("QR_CODE", document.getElementsByTagName("qrCodCTe").item(0) != null ? document.getElementsByTagName("qrCodCTe").item(0).getTextContent() : null);
    			
            JRXmlDataSource dataSourceXmlCTe = new JRXmlDataSource(document, "/CTe/infCte");
                
            // selecionar o template JASPER de acordo com a versao do xml
			jasperPrintResult = JasperFillManager.fillReport(
			        getTemplateCTe(),
			        parameters, 
			        dataSourceXmlCTe);
    	} catch(Exception e) {
    	    LOGGER.error(e);
    		jasperPrintResult = new JasperPrint();
    	}

		return jasperPrintResult;
	}
	
	private static List<Map<String,String>> getListChaveCteNfe(String cteXml){
	    List<Map<String,String>> ctes = new ArrayList<Map<String,String>>();
	    StringReader sr = new StringReader(cteXml);
	    
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();          
            Document doc = db.parse(new InputSource(sr));
            doc.getDocumentElement().normalize();
	        
            NodeList nodeListTpServ = doc.getElementsByTagName("tpServ");
            Node nodetpServ = nodeListTpServ.item(0);
            String tpServ = nodetpServ.getTextContent();
            
            if("1".equals(tpServ) || "2".equals(tpServ) || "3".equals(tpServ)){
                buildListChaveCTe(ctes, doc);
            }
            
            if(!"3".equals(tpServ)){
                buildListChaveNFe(ctes, doc);
            }
            
            if("0".equals(tpServ)){
                buildListChaveOutros(ctes, doc);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            if(sr != null){
                sr.close();
            }
        }
	    
	    return ctes;
	}

    private static void buildListChaveOutros(List<Map<String, String>> ctes,
            Document doc) {
        HashMap<String, String> listChaveOutros = new HashMap<String, String>();
        StringBuilder chaveOutros = new StringBuilder();

        NodeList nodeListTpDoc = doc.getElementsByTagName("tpDoc");
		int length = nodeListTpDoc.getLength();
        if(length > 0){
			Node nodeTpDoc = nodeListTpDoc.item(0);
			String tpDoc = nodeTpDoc.getTextContent();
        
			if(tpDoc != null){
				if("00".equals(tpDoc)){
					chaveOutros.append(StringUtils.rightPad("Declaração", 90, " "));
				}else{
					NodeList nodeListInfOutrosDescOutros = doc.getElementsByTagName("descOutros");
					Node nodeDescOutros = nodeListInfOutrosDescOutros.item(0);
					if(nodeDescOutros.getTextContent()!= null){
						String descOutros = nodeDescOutros.getTextContent();
						chaveOutros.append(StringUtils.rightPad(descOutros, 95, " "));
					}                
				}
			}
		}	
        
        NodeList nodeListInfNF = doc.getElementsByTagName("infNF");
        length = nodeListInfNF.getLength();
        if(length > 0){
            NodeList nodeListInfNFSerie = doc.getElementsByTagName("infNF_serie");
            Node nodeInfNFSerie = nodeListInfNFSerie.item(0);
            String infNFSerie = nodeInfNFSerie.getTextContent();
            
            if(infNFSerie != null){
                NodeList nodeListInfNFcnpj = doc.getElementsByTagName("infNF_cnpj");
                Node nodeInfNFcnpj = nodeListInfNFcnpj.item(0);
                String infNFcnpj = nodeInfNFcnpj.getTextContent();
                
                chaveOutros.append(CTeUtils.formatCNPJ(infNFcnpj));
            }else{
                NodeList nodeListInfNFcpf = doc.getElementsByTagName("infNF_CPF");
                Node nodeInfNFcpf = nodeListInfNFcpf.item(0);
                String infNFcpf = nodeInfNFcpf.getTextContent();
                
                chaveOutros.append(CTeUtils.formatCPF(infNFcpf));
            }
            
            chaveOutros.append(infNFSerie);
        }
        
        NodeList nodeListInfNFnDoc = doc.getElementsByTagName("nDoc");
		length = nodeListInfNFnDoc.getLength();
        if(length > 0){
			Node nodeInfNFnDoc = nodeListInfNFnDoc.item(0);
			String infNFnDoc = nodeInfNFnDoc.getTextContent();
        
			chaveOutros.append(infNFnDoc);
		}	
        
        listChaveOutros.put("chave", chaveOutros.toString());
        ctes.add(listChaveOutros);
    }

    private static void buildListChaveNFe(List<Map<String, String>> ctes,
            Document doc) {
        NodeList nodeListChaveNFE = doc.getElementsByTagName("chave");
        int lengthChaveNFE = nodeListChaveNFE.getLength();
        
        for (int x = 0; x < lengthChaveNFE; x++) {
            Node nodeChaveNFE = nodeListChaveNFE.item(x);
            HashMap<String, String> listChaveCteNfe = new HashMap<String, String>();
            StringBuilder chaveNFe = new StringBuilder();
            chaveNFe.append(StringUtils.rightPad("NFe:", 15, ""));
            chaveNFe.append(nodeChaveNFE.getTextContent());
            chaveNFe.append(" NF: ");
            chaveNFe.append(nodeChaveNFE.getTextContent().substring(25,34));
            
            listChaveCteNfe.put("chave", chaveNFe.toString());
            ctes.add(listChaveCteNfe);
        }
    }

    private static void buildListChaveCTe(List<Map<String, String>> ctes,
            Document doc) {
        NodeList nodeListChCTe = doc.getElementsByTagName("chCTe");
        int lengthChCTe = nodeListChCTe.getLength();

        for (int x = 0; x < lengthChCTe; x++) {
            Node nodeChCte = nodeListChCTe.item(x);
            HashMap<String, String> listChaveCteNfe = new HashMap<String, String>();
            StringBuilder chaveCTe = new StringBuilder();
            chaveCTe.append(StringUtils.rightPad("CT-e:", 15, " "));
            chaveCTe.append(nodeChCte.getTextContent());
            
            listChaveCteNfe.put("chave", chaveCTe.toString());
            ctes.add(listChaveCteNfe);
        }
    }
	

	private static InputStream getTemplateCTe() {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream("com/mercurio/lms/expedicao/reports/impressaoCTE.jasper");
	}
}
