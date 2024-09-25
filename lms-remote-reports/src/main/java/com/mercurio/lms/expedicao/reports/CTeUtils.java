package com.mercurio.lms.expedicao.reports;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.pdf.Barcode128;

/**
 * Utilities class for CT-e printing
 * 
 * 
 * @author Vagner Huzalo
 *
 */
public class CTeUtils{

	private static final Logger LOGGER = LogManager.getLogger(CTeUtils.class);
	private static final int ICMS00 = 0;
	private static final int ICMS20 = 1;
	private static final int ICMS45 = 2;
	private static final int ICMS60 = 3;
	private static final int ICMS90 = 4;
	private static final int ICMS_OUTRO = 5;
	private static final int CST00 = 6;
	private static final int CST20 = 7;
	private static final int CST45 = 8;
	private static final int CST80 = 9;
	private static final int CST81 = 10;
	private static final int CST90 = 11;

	private static final Pattern PHONE_PATTERN = Pattern.compile("(\\d{2})?(\\d{2,4})(\\d{4})");
	private static final Pattern CNPJ_PATTERN = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
	private static final Pattern CPF_PATTERN = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
	
	public static java.awt.Image generateQRCode(String codeText, int size) throws Exception {
			
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		
		hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
 
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, size, size, hintMap);
		int CrunchifyWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
 
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
		graphics.setColor(Color.BLACK);
 
		for (int i = 0; i < CrunchifyWidth; i++) {
			for (int j = 0; j < CrunchifyWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
			
		return image;
	}

	/**
	 *  
	 * 
	 * @param code
	 * @param height
	 * @return
	 */
	public static java.awt.Image generateBarcode(String code, int height) {
		Barcode128 barCode = new Barcode128();
		barCode.setCode(code);
		barCode.setX(1.0f);
		barCode.setSize(0);
		barCode.setBarHeight(height);
		return barCode.createAwtImage(new Color(0, 0, 0), new Color(255, 255,255));
	}
	
	/**
	 * @param dataSource
	 * @param filterFieldDesc
	 * @param fieldName
	 * @return
	 */
	public static String getFieldValueFromInfoQElement(Object dataSource, String filterFieldDesc, String fieldName){
		try {
	        JRXmlDataSource xmlDS = ((JRXmlDataSource) dataSource).subDataSource("//infQ");
	        
	        JRDesignField filterField = new JRDesignField();
	        filterField.setDescription("tpMed");
	        while(xmlDS.next()){
	        	String value = (String)xmlDS.getFieldValue(filterField);
	        	if (compareStringWithoutAccents(value, filterFieldDesc)){
	        		JRDesignField field = new JRDesignField();
	    	        field.setDescription(fieldName);
	    	        if (fieldName.equals("cUnid")){
	    	        	String valueString = (String)xmlDS.getFieldValue(field);
	    	        	if (valueString != null && valueString.equals("01")){
	    	        		return "kg";
	    	        	}else{
	    	        		return "un";
	    	        	}
	    	        }
	    	        return (String)xmlDS.getFieldValue(field);
	        	}
	        }
        } catch (JRException e) {
	        LOGGER.error(e);
        }
		return null;
	}
	
	private static boolean compareStringWithoutAccents(String str1, String str2) {  
		if (removeAccents(str1).equals(removeAccents(str2))){
			return true;
		}
		return false;
	}
	
	private static String removeAccents(String str) {  
	    str = Normalizer.normalize(str, Normalizer.Form.NFD);  
	    str = str.replaceAll("[^\\p{ASCII}]", "");  
	    return str;  
	} 
	
	/**
	 * Build a dataSource page from a specified dataSource  
	 * 
	 * @param dataSource
	 * @param element
	 * @param pageSize
	 * @param startElement
	 * @return
	 */
	public static JRXmlDataSource getDataSourcePage(JRXmlDataSource dataSource,String element, int pageSize, int startElement){
		JRXmlDataSource jrXmlDataSource = null;
		try {
			jrXmlDataSource = dataSource.subDataSource(element+"[position() >="+startElement+" and position() <"+(pageSize+startElement)+"]");
		}catch (JRException e) {
			LOGGER.error(e);
		}catch (Exception e) {
			LOGGER.error(e);
		}
		return jrXmlDataSource;
	}
	
	public static BigDecimal getBigDecimalICMSField(JRXmlDataSource dataSource,String fieldName,Locale locale){
		String returnValue = getICMSField(dataSource, fieldName, locale);
		
		if (StringUtils.isNotEmpty(returnValue) && isNumber(returnValue)){
			return new BigDecimal(returnValue);
		}
		return null;
	}
	
	private static boolean isNumber(String value){
		try{
			Double.parseDouble(value);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param fieldName
	 * @return
	 */
	public static String getICMSField(JRXmlDataSource dataSource,String fieldName,Locale locale){
		String icmsValue = null;
		
		try{
			int icmsType = dataSource.subDataSource("//imp/ICMS/ICMS00").next()? ICMS00
					: dataSource.subDataSource("//imp/ICMS/ICMS20").next()? ICMS20
					: dataSource.subDataSource("//imp/ICMS/ICMS45").next()? ICMS45
					: dataSource.subDataSource("//imp/ICMS/ICMS60").next()? ICMS60
					: dataSource.subDataSource("//imp/ICMS/ICMS90").next()? ICMS90
					: dataSource.subDataSource("//imp/ICMS/CST00").next()? CST00
					: dataSource.subDataSource("//imp/ICMS/CST20").next()? CST20
					: dataSource.subDataSource("//imp/ICMS/CST45").next()? CST45
					: dataSource.subDataSource("//imp/ICMS/CST80").next()? CST80
					: dataSource.subDataSource("//imp/ICMS/CST81").next()? CST81
					: dataSource.subDataSource("//imp/ICMS/CST90").next()? CST90

					:ICMS_OUTRO;
			
			JRXmlDataSource xmlDataSource = null;
			switch (icmsType) {
            case ICMS00:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMS00");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            case ICMS20:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMS20");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            case ICMS45:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMS45");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            case ICMS60:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMS60");
            	xmlDataSource.next();
            	if("vBC".equals(fieldName)){
            		fieldName = "vBCSTRet";
            	}else if("pICMS".equals(fieldName)){
            		fieldName = "pICMSSTRet";
            	}
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            case ICMS90:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMS90");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            case ICMS_OUTRO:
            	xmlDataSource = dataSource.subDataSource("//imp/ICMS/ICMSOutraUF");
            	xmlDataSource.next();
            	if("vBC".equals(fieldName)){
            		fieldName = "vBCOutraUF";
            	}else if("pICMS".equals(fieldName)){
            		fieldName = "pICMSOutraUF";
            	}else if("vlICMS".equals(fieldName)){
            		fieldName = "vICMSOutraUF";
            	}
            	
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
	            break;
            
			case CST00:
	         	xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST00");
	            xmlDataSource.next();
	            icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
		        break;

			case CST20:
				xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST20");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
            	break;
			case CST45:
				xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST45");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
            	break;
			case CST80:
			    xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST80");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
            	break;
			case CST81:
				xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST81");
            	xmlDataSource.next();
            	icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
            	break;
			case CST90:
				xmlDataSource = dataSource.subDataSource("//imp/ICMS/CST90");
         		xmlDataSource.next();
         		icmsValue = getFieldValueFromDataSource(xmlDataSource, fieldName);
            	break;			
			}
		}catch(JRException jre){
		    icmsValue = null;
		}
		
		if (icmsValue == null){
		    return "";
		}
		
		if (!"CST".equals(fieldName)){
		    return icmsValue.trim();
		}
		
		return generateCST(locale, icmsValue);
	}

    private static String generateCST(Locale locale, String icmsValue) {
        String returnValue = "";
		int value = Integer.parseInt(icmsValue);
		locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
		
		switch (value) {
		case 0:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.normal");
            break;
		case 20:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.bcReduzida");
            break;
		case 40:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.isencao");
            break;
		case 41:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.naoTributado");
            break;
		case 51:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.diferido");
            break;
		case 60:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.cobrado");
            break;
        default:
        	returnValue = getDefaultResourceBundle(locale).getString("tpTributacaoCte.outros");
            break;
        }
		
		return (icmsValue + "-" + returnValue).trim();
    }
	
	private static String getFieldValueFromDataSource(JRXmlDataSource dataSource, String fieldName) throws JRException{
		JRDesignField field = new JRDesignField();
        field.setDescription(fieldName);
        return (String)dataSource.getFieldValue(field);
	}
	
	
	/**
	 * Mount default resource bundle.
	 * 
	 * @return
	 */
	private static ResourceBundle getDefaultResourceBundle(Locale locale){
		return ResourceBundle.getBundle("com.mercurio.lms.expedicao.reports.impressaoCTE",locale);
	}
	
	public static String getPageCount(JRDataSource dataSource){
		int recordCount = 0;
		try {
	        while(dataSource.next()){
	        	recordCount++;
	        }
        } catch (JRException e) {
	        LOGGER.error(e);
        }
        
        if (recordCount == 0){
        	return "1";
        }
         
        if (recordCount < 116){
        	return "2";
        }
		return Integer.valueOf((recordCount % 116)+2).toString();
	}
	
	public static String getPageCountRedespachoIntermediario(JRDataSource dataSource){
		int recordCount = 0;
		try {
	        while(dataSource.next()){
	        	recordCount++;
	        }
        } catch (JRException e) {
	        LOGGER.error(e);
        }
        
        if (recordCount < 17){
        	return "1";
        }
         
        if (recordCount < 133){
        	return "2";
        }
		return Integer.valueOf((recordCount % 133)+2).toString();
	}
	
	public static Boolean getPrintDatailPageRedespachoIntermediario(JRDataSource dataSource){
		int recordCount = 0;
		try {
			while(dataSource.next()){
				recordCount++;
			}
		} catch (JRException e) {
			LOGGER.error(e);
		}
		
		if (recordCount > 16){
			return Boolean.TRUE;
		}
		
			return Boolean.FALSE;
	}
	
	public static String formatCPF(String cpf){
		if (cpf != null && cpf.length() >0){
			Matcher matcher = CPF_PATTERN.matcher(cpf);
			if (matcher.matches()){
				cpf = String.format("%s.%s.%s-%s", new Object[]{
						matcher.group(1),
						matcher.group(2),
						matcher.group(3),
						matcher.group(4)});
			}
		}
		return cpf;
	}
	
	public static String formatCNPJ(String cnpj){
		if (cnpj != null && cnpj.length() >0){
			Matcher matcher = CNPJ_PATTERN.matcher(cnpj);
			if (matcher.matches()){
				cnpj = String.format("%s.%s.%s/%s-%s", new Object[]{
						matcher.group(1),
						matcher.group(2),
						matcher.group(3),
						matcher.group(4),
						matcher.group(5)});
			}
		}
		return cnpj;
	}
	
	public static String formatChave(String chave){
		StringBuilder chaveBuilder = new StringBuilder();
		for(int index = 0; index < chave.length(); index++){
			chaveBuilder.append(chave.charAt(index));
			if ((index+1) % 4 == 0 && (index+1)!=chave.length()){
				chaveBuilder.append(" ");
			}
		}
		return chaveBuilder.toString();
	}
	
	public static String formatFone(String fone){
		if (fone != null && fone.length() >0){
			Matcher matcher = PHONE_PATTERN.matcher(fone);
			if (matcher.matches()){
				fone = "("+matcher.group(1) +")" + matcher.group(2) + "-" +matcher.group(3);
			}
		}
		return fone;
	}
	
	public static boolean isNFE(Object dataSource){
		return verifyTag(dataSource, "//infNFe");
	}

	public static boolean isNF(Object dataSource) {
		return verifyTag(dataSource, "//infNF");
	}
	
	private static boolean verifyTag(Object dataSource, String tag) {
		try {
			JRXmlDataSource xmlDS = ((JRXmlDataSource) dataSource).subDataSource(tag);
			return xmlDS.next();
		} catch (JRException e) {
			LOGGER.error(e);
		}
		return false;
	}
	
}
