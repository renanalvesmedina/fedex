package com.mercurio.lms.expedicao.model.service;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

public enum XmlAwb {
	
	// TAM		
	//  					CHAVE        			GRUPO      		TAG     	XPATH					Type
	INF_CTE_ID  			("infCteId",			"infCte",   	"@Id", 		"//CTe/infCte/@Id" , 													XPathConstants.STRING),
	EXPED_CNPJ      		("expedCNPJ",			"exped",   		"CNPJ", 	"//CTe/infCte/exped/CNPJ", 												XPathConstants.STRING),
	IDE_SERIE	    		("expedSerie",			"ide",    		"serie",	"//CTe/infCte/ide/serie", 												XPathConstants.STRING),
	IDE_N_CT	    		("expedNCT",			"ide",    		"nCT", 		"//CTe/infCte/ide/nCT", 												XPathConstants.STRING),
	IDE_DH_EMI	    		("ideDHEMI",			"ide",    		"dhEmi",	"//CTe/infCte/ide/dhEmi", 												XPathConstants.STRING),
	AEREO_N_OCA     		("aereoNOCA", 			"aereo",		"nOCA", 	"//CTe/infCte/infCTeNorm/infModal/aereo/nOCA", 							XPathConstants.STRING),
	EMIT_CNPJ				("emitCNPJ", 			"emit",			"CNPJ", 	"//CTe/infCte/emit/CNPJ", 												XPathConstants.STRING),
	INF_NFE_CHAVE			("infNFeChave",			"infNFe",		"chave", 	"//CTe/infCte/emit/infNFe",		 										XPathConstants.NODESET),
	RECEB_CNPJ				("recebCNPJ",			"receb",		"CNPJ", 	"//CTe/infCte/receb/CNPJ", 												XPathConstants.STRING),
	TOMA4_CNPJ				("toma4CNPJ", 			"toma4",		"CNPJ", 	"//CTe/infCte/ide/toma4/CNPJ", 											XPathConstants.STRING),
	COMPL_X_OBS				("complXObx",			"compl",		"xObs", 	"//CTe/infCte/compl/xObs", 												XPathConstants.STRING),
	FLUXO_X_ORIG			("fluxoXOrig",			"fluxo",		"xOrig", 	"//CTe/infCte/compl/fluxo/xOrig", 										XPathConstants.STRING),
	FLUXO_X_DEST			("fluxoXDest",			"fluxo",		"xDest", 	"//CTe/infCte/compl/fluxo/xDest", 										XPathConstants.STRING),
	PASS_X_PASS				("passXPass",			"pass",			"xPass", 	"//CTe/infCte/compl/fluxo/pass/xPass", 									XPathConstants.STRING),
	V_PREST_V_REC			("vPrestVRec",			"vPrest",		"vRec", 	"//CTe/infCte/vPrest/vRec",	 											XPathConstants.STRING),
	ID_DOC_ANT_ELE_CHAVE	("idDocAntEleChave",	"idDocAntEle",	"chave",	"//infCte/infCTeNorm/docAnt/emiDocAnt/idDocAnt/idDocAntEle", 			XPathConstants.NODESET), 
	ICMS00_V_ICMS			("ICMS00vICMS",			"ICMS00",		"vICMS", 	"//CTe/infCte/imp/ICMS/ICMS00/vICMS", 									XPathConstants.STRING), 
	ICMS00_P_ICMS			("ICMS00pICMS",			"ICMS00",		"pICMS", 	"//CTe/infCte/imp/ICMS/ICMS00/pICMS", 									XPathConstants.STRING), 
	ICMS00_V_BC				("ICMS00vBC",			"ICMS00",		"vBC", 		"//CTe/infCte/infCte/imp/ICMS/ICMS00/vBC", 								XPathConstants.STRING),
	INFQ_QCARGA_03			("infQQCarga03",		"infQ",			"qCarga",	"//infCTeNorm/infCarga/infQ[cUnid[text()=\"03\"]]/qCarga", 				XPathConstants.STRING),
	INFQ_QCARGA_PS_BRUTO	("infQQCargaPsBruto",	"infQ",			"qCarga",	"//infCTeNorm/infCarga/infQ[tpMed[text()=\"PESO BRUTO(KG)\"]]/qCarga",	XPathConstants.STRING),
	INFQ_QCARGA_PS_CUBADO	("infQQCargaPsCubado",	"infQ",			"qCarga",	"//infCTeNorm/infCarga/infQ[tpMed[text()=\"PESO CUBADO(KG)\"]]/qCarga", XPathConstants.STRING), 
	TARIFA_V_TAR			("tarifaVTar",			"tarifa",		"vTar",		"//infCte/infCTeNorm/infModal/aereo/tarifa[CL[text()=\"E\"]]/vTar", 	XPathConstants.STRING),
	IDE_FOR_PAG  			("ideForPag",			"ide",			"forPag",	"//infCte/ide/forPag", 													XPathConstants.STRING),
	;
	
	private String chave;
	private String grupo;
	private String tag;
	private String xPath;
	private QName qName;

	private XmlAwb(String chave, String grupo, String tag, String xPath, QName qName) {
		this.chave = chave;
		this.grupo = grupo;
		this.tag = tag;
		this.xPath = xPath;
		this.qName = qName;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public QName getqName() {
		return qName;
	}

	public void setqName(QName qName) {
		this.qName = qName;
	}
}
