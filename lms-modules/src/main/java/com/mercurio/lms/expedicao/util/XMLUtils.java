package com.mercurio.lms.expedicao.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLUtils {

	private static final String INF_CTE = "</infCte>";

	private XMLUtils() {
	}

	public static String getStringElementValue(Document doc, XPath xpath, String xpathElement) throws XPathExpressionException {
		if (xpathElement != null) {
			XPathExpression expr = xpath.compile(xpathElement);
			if (expr != null) {
				return (String) expr.evaluate(doc, XPathConstants.STRING);
			}
		}
		return null;
	}

	public static Long getLongElementValue(Document doc, XPath xpath, String xpathElement) throws XPathExpressionException {
		String valueString = getStringElementValue(doc, xpath, xpathElement);
		if (valueString != null && !"".equals(valueString)) {
			return Long.parseLong(valueString);
		}
		return null;
	}

	public static Integer getIntegerElementValue(Document doc, XPath xpath, String xpathElement) throws XPathExpressionException {
		String valueString = getStringElementValue(doc, xpath, xpathElement);
		if (valueString != null && !"".equals(valueString)) {
			return Integer.parseInt(valueString);
		}
		return null;
	}

	public static BigDecimal getBigDecimalElementValue(Document doc, XPath xpath, String xpathElement) throws XPathExpressionException {
		String valueString = getStringElementValue(doc, xpath, xpathElement);
		if (valueString != null && !"".equals(valueString)) {
			return new BigDecimal(valueString);
		}
		return null;
	}

	public static Date getDatelElementValue(Document doc, XPath xpath, String xpathElement) throws XPathExpressionException {
		String valueString = getStringElementValue(doc, xpath, xpathElement);
		if (valueString != null && !"".equals(valueString)) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(valueString);
			} catch (ParseException e) {
				throw new XPathExpressionException(e);
			}
		}
		return null;
	}

	public static String getValue(Node n, String expr, XPath xpath) throws XPathExpressionException {
		XPathExpression pathExpr = xpath.compile(expr);
		return (String) pathExpr.evaluate(n, XPathConstants.STRING);
	}

	public static NodeList getNodeList(Document doc, XPath xpath, String expr) throws XPathExpressionException {
		XPathExpression pathExpr = xpath.compile(expr);
		return (NodeList) pathExpr.evaluate(doc, XPathConstants.NODESET);
	}

	public static StringBuilder buildXmlCTEAutenticadoSefaz(byte[] dsDadosDocumento) {
		try {
			StringBuilder xmlOriginal = new StringBuilder(new String(dsDadosDocumento, "UTF-8"));
			Integer beginIndexInfCte = xmlOriginal.indexOf("<infCte ");
			Integer endIndexInfCte = xmlOriginal.indexOf(INF_CTE) + INF_CTE.length();

			StringBuilder xmlCte = new StringBuilder();
			xmlCte.append("<CTe xmlns=\"http://www.portalfiscal.inf.br/cte\">");
			xmlCte.append(xmlOriginal.substring(beginIndexInfCte, endIndexInfCte));
			xmlCte.append("</CTe>");

			endIndexInfCte = xmlCte.indexOf(INF_CTE);
			xmlCte.insert(endIndexInfCte, "<dadosAdic></dadosAdic>");
			return xmlCte;
		} catch (StringIndexOutOfBoundsException e){
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> listNrChaveCtes(byte[] dsDadosDocumento) throws Exception {

		List<String> listNrCtes = new ArrayList<String>();

		String xml = new String(dsDadosDocumento);

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("infCTe");

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList name = element.getElementsByTagName("chCTe");
				Element line = (Element) name.item(0);
				listNrCtes.add(getValueElement(line));
			}
		}
		catch (Exception e) {
			throw new Exception("Erro ao recuperar Chaves Cte.", e);
		}

		return listNrCtes;
	}

	public static String getValueElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

}
