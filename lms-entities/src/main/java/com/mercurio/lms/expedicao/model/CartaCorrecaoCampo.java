package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Campos de informações minadas do XML de envio do CT-e
 * @see <a href="http://lx-swjir01/jira/browse/LMS-5904">LMS-5904</a>
 * 
 * @author fabiano.pinto@cwi.com.br
 */
public enum CartaCorrecaoCampo implements Serializable {

	//                 CHAVE                GRUPO         TAG        XPATH
	IDE_CFOP          ("ideCFOP",           "ide",        "CFOP",    "/CTe/infCte/ide/CFOP"),
	IDE_NATOP         ("ideNatOp",          "ide",        "natOp",   "/CTe/infCte/ide/natOp"),
	TOMA4_XNOME       ("toma4XNome",        "toma4",      "xNome",   "/CTe/infCte/ide/toma4/xNome"),
	TOMA4_XFANT       ("toma4XFant",        "toma4",      "xFant",   "/CTe/infCte/ide/toma4/xFant"),
	TOMA4_FONE        ("toma4Fone",         "toma4",      "fone",    "/CTe/infCte/ide/toma4/fone"),
	ENDERTOMA_XLGR    ("enderTomaXLgr",     "enderToma",  "xLgr",    "/CTe/infCte/ide/toma4/enderToma/xLgr"),
	ENDERTOMA_NRO     ("enderTomaNro",      "enderToma",  "nro",     "/CTe/infCte/ide/toma4/enderToma/nro"),
	ENDERTOMA_XCPL    ("enderTomaXCpl",     "enderToma",  "xCpl",    "/CTe/infCte/ide/toma4/enderToma/xCpl"),
	ENDERTOMA_XBAIRRO ("enderTomaXBairro",  "enderToma",  "xBairro", "/CTe/infCte/ide/toma4/enderToma/xBairro"),
	ENDERTOMA_CMUN    ("enderTomaCMun",     "enderToma",  "cMun",    "/CTe/infCte/ide/toma4/enderToma/cMun"),
	ENDERTOMA_XMUN    ("enderTomaXMun",     "enderToma",  "xMun",    "/CTe/infCte/ide/toma4/enderToma/xMun"),
	ENDERTOMA_CEP     ("enderTomaCEP",      "enderToma",  "CEP",     "/CTe/infCte/ide/toma4/enderToma/CEP"),
	ENDERTOMA_EMAIL   ("toma4Email",        "toma4",      "email",   "/CTe/infCte/ide/toma4/email"),
	COMPL_XOBS        ("complXObs",         "compl",      "xObs",    "/CTe/infCte/compl/xObs"),
	EMIT_XNOME        ("emitXNome",         "emit",       "xNome",   "/CTe/infCte/emit/xNome"),
	EMIT_XFANT        ("emitXFant",         "emit",       "xFant",   "/CTe/infCte/emit/xFant"),
	ENDEREMIT_XLGR    ("enderEmitXLgr",     "enderEmit",  "xLgr",    "/CTe/infCte/emit/enderEmit/xLgr"),
	ENDEREMIT_NRO     ("enderEmitNro",      "enderEmit",  "nro",     "/CTe/infCte/emit/enderEmit/nro"),
	ENDEREMIT_XCPL    ("enderEmitXCpl",     "enderEmit",  "xCpl",    "/CTe/infCte/emit/enderEmit/xCpl"),
	ENDEREMIT_XBAIRRO ("enderEmitXBairro",  "enderEmit",  "xBairro", "/CTe/infCte/emit/enderEmit/xBairro"),
	ENDEREMIT_CMUN    ("enderEmitCMun",     "enderEmit",  "cMun",    "/CTe/infCte/emit/enderEmit/cMun"),
	ENDEREMIT_XMUN    ("enderEmitXMun",     "enderEmit",  "xMun",    "/CTe/infCte/emit/enderEmit/xMun"),
	ENDEREMIT_CEP     ("enderEmitCEP",      "enderEmit",  "CEP",     "/CTe/infCte/emit/enderEmit/CEP"),
	ENDEREMIT_FONE    ("enderEmitFone",     "enderEmit",  "fone",    "/CTe/infCte/emit/enderEmit/fone"),
	REM_XNOME         ("remXNome",          "rem",        "xNome",   "/CTe/infCte/rem/xNome"),
	REM_XFANT         ("remXFant",          "rem",        "xFant",   "/CTe/infCte/rem/xFant"),
	REM_FONE          ("remFone",           "rem",        "fone",    "/CTe/infCte/rem/fone"),
	ENDERREME_XLGR    ("enderRemeXLgr",     "enderReme",  "xLgr",    "/CTe/infCte/rem/enderReme/xLgr"),
	ENDERREME_NRO     ("enderRemeNro",      "enderReme",  "nro",     "/CTe/infCte/rem/enderReme/nro"),
	ENDERREME_XCPL    ("enderRemeXCpl",     "enderReme",  "xCpl",    "/CTe/infCte/rem/enderReme/xCpl"),
	ENDERREME_XBAIRRO ("enderRemeXBairro",  "enderReme",  "xBairro", "/CTe/infCte/rem/enderReme/xBairro"),
	ENDERREME_CMUN    ("enderRemeCMun",     "enderReme",  "cMun",    "/CTe/infCte/rem/enderReme/cMun"),
	ENDERREME_XMUN    ("enderRemeXMun",     "enderReme",  "xMun",    "/CTe/infCte/rem/enderReme/xMun"),
	ENDERREME_CEP     ("enderRemeCEP",      "enderReme",  "CEP",     "/CTe/infCte/rem/enderReme/CEP"),
	ENDERREME_EMAIL   ("remEmail",          "rem",        "email",   "/CTe/infCte/rem/email"),
	EXPED_XNOME       ("expedXNome",        "exped",      "xNome",   "/CTe/infCte/exped/xNome"),
	EXPED_FONE        ("expedFone",         "exped",      "fone",    "/CTe/infCte/exped/fone"),
	ENDEREXPED_XLGR   ("enderExpedXLgr",    "enderExped", "xLgr",    "/CTe/infCte/exped/enderExped/xLgr"),
	ENDEREXPED_NRO    ("enderExpedNro",     "enderExped", "nro",     "/CTe/infCte/exped/enderExped/nro"),
	ENDEREXPED_XCPL   ("enderExpedXCpl",    "enderExped", "xCpl",    "/CTe/infCte/exped/enderExped/xCpl"),
	ENDEREXPED_XBAIRRO("enderExpedXBairro", "enderExped", "xBairro", "/CTe/infCte/exped/enderExped/xBairro"),
	ENDEREXPED_CMUN   ("enderExpedCMun",    "enderExped", "cMun",    "/CTe/infCte/exped/enderExped/cMun"),
	ENDEREXPED_XMUN   ("enderExpedXMun",    "enderExped", "xMun",    "/CTe/infCte/exped/enderExped/xMun"),
	ENDEREXPED_CEP    ("enderExpedCEP",     "enderExped", "CEP",     "/CTe/infCte/exped/enderExped/CEP"),
	ENDEREXPED_EMAIL  ("expedEmail",        "exped",      "email",   "/CTe/infCte/exped/email"),
	RECEB_XNOME       ("recebXNome",        "receb",      "xNome",   "/CTe/infCte/receb/xNome"),
	RECEB_FONE        ("recebFone",         "receb",      "fone",    "/CTe/infCte/receb/fone"),
	ENDERRECEB_XLGR   ("enderRecebXLgr",    "enderReceb", "xLgr",    "/CTe/infCte/receb/enderReceb/xLgr"),
	ENDERRECEB_NRO    ("enderRecebNro",     "enderReceb", "nro",     "/CTe/infCte/receb/enderReceb/nro"),
	ENDERRECEB_XCPL   ("enderRecebXCpl",    "enderReceb", "xCpl",    "/CTe/infCte/receb/enderReceb/xCpl"),
	ENDERRECEB_XBAIRRO("enderRecebXBairro", "enderReceb", "xBairro", "/CTe/infCte/receb/enderReceb/xBairro"),
	ENDERRECEB_CMUN   ("enderRecebCMun",    "enderReceb", "cMun",    "/CTe/infCte/receb/enderReceb/cMun"),
	ENDERRECEB_XMUN   ("enderRecebXMun",    "enderReceb", "xMun",    "/CTe/infCte/receb/enderReceb/xMun"),
	ENDERRECEB_CEP    ("enderRecebCEP",     "enderReceb", "CEP",     "/CTe/infCte/receb/enderReceb/CEP"),
	ENDERRECEB_EMAIL  ("recebEmail",        "receb",      "email",   "/CTe/infCte/receb/email"),
	DEST_XNOME        ("destXNome",         "dest",       "xNome",   "/CTe/infCte/dest/xNome"),
	DEST_FONE         ("destFone",          "dest",       "fone",    "/CTe/infCte/dest/fone"),
	DEST_ISUF         ("destISUF",          "dest",       "ISUF",    "/CTe/infCte/dest/ISUF"),
	ENDERDEST_XLGR    ("enderDestXLgr",     "enderDest",  "xLgr",    "/CTe/infCte/dest/enderDest/xLgr"),
	ENDERDEST_NRO     ("enderDestNro",      "enderDest",  "nro",     "/CTe/infCte/dest/enderDest/nro"),
	ENDERDEST_XCPL    ("enderDestXCpl",     "enderDest",  "xCpl",    "/CTe/infCte/dest/enderDest/xCpl"),
	ENDERDEST_XBAIRRO ("enderDestXBairro",  "enderDest",  "xBairro", "/CTe/infCte/dest/enderDest/xBairro"),
	ENDERDEST_CMUN    ("enderDestCMun",     "enderDest",  "cMun",    "/CTe/infCte/dest/enderDest/cMun"),
	ENDERDEST_XMUN    ("enderDestXMun",     "enderDest",  "xMun",    "/CTe/infCte/dest/enderDest/xMun"),
	ENDERDEST_CEP     ("enderDestCEP",      "enderDest",  "CEP",     "/CTe/infCte/dest/enderDest/CEP"),
	ENDERDEST_EMAIL   ("destEmail",         "dest",       "email",   "/CTe/infCte/dest/email"),
	LOCENT_CNPJ       ("locEntCNPJ",        "locEnt",     "CNPJ",    "/CTe/infCte/dest/locEnt/CNPJ"),
	LOCENT_CPF        ("locEntCPF",         "locEnt",     "CPF",     "/CTe/infCte/dest/locEnt/CPF"),
	LOCENT_XNOME      ("locEntXNome",       "locEnt",     "xNome",   "/CTe/infCte/dest/locEnt/xNome"),
	LOCENT_XLGR       ("locEntXLgr",        "locEnt",     "xLgr",    "/CTe/infCte/dest/locEnt/xLgr"),
	LOCENT_NRO        ("locEntNro",         "locEnt",     "nro",     "/CTe/infCte/dest/locEnt/nro"),
	LOCENT_XBAIRRO    ("locEntXBairro",     "locEnt",     "xBairro", "/CTe/infCte/dest/locEnt/xBairro"),
	LOCENT_CMUN       ("locEntCMun",        "locEnt",     "cMun",    "/CTe/infCte/dest/locEnt/cMun"),
	LOCENT_XMUN       ("locEntXMun",        "locEnt",     "xMun",    "/CTe/infCte/dest/locEnt/xMun");

	private String chave;
	private String grupo;
	private String tag;
	private String xPath;

	private CartaCorrecaoCampo(String chave, String grupo, String tag, String xPath) {
		this.chave = chave;
		this.grupo = grupo;
		this.tag = tag;
		this.xPath = xPath;
	}

	/**
	 * @return Chave para recurso de mensagens
	 */
	public String getChave() {
		return chave;
	}

	/**
	 * @return Grupo (parent element) do campo na estrutura do XML
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @return Tag (element) do campo na estrutura do XML
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return Caminho (XPATH) para campo na estrutura XML
	 */
	public String getxPath() {
		return xPath;
	}

	private static Map<String, CartaCorrecaoCampo> map;

	private static Map<String, CartaCorrecaoCampo> getMap() {
		if (map == null) {
			map = new HashMap<String, CartaCorrecaoCampo>();
			for (CartaCorrecaoCampo value : CartaCorrecaoCampo.values()) {
				map.put(CartaCorrecaoCampo.makeKey(value.getGrupo(), value.getTag()), value);
			}
		}
		return map;
	}

	private static String makeKey(String grupo, String tag) {
		return grupo.toUpperCase() + "/" + tag.toUpperCase();
	}

	/**
	 * Retorna um valor no enum para determinado grupo e tag
	 * 
	 * @param grupo Grupo do campo
	 * @param tag Tag do campo
	 * @return Valor correspondente ao grupo e tag
	 */
	public static CartaCorrecaoCampo findByGrupoTag(String grupo, String tag) {
		CartaCorrecaoCampo value = getMap().get(CartaCorrecaoCampo.makeKey(grupo, tag));
		if (value == null) {
			throw new IllegalArgumentException(String.format(
					"Valor inexistente para grupo e tag especificados (grupo=%s, tag=%s)", grupo, tag));
		}
		return value;
	}

}
