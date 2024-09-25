package com.mercurio.lms.recepcaodescarga.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.util.session.SessionUtils;

public class RelatorioDocumentosDivergenciaDescargaService extends ReportServiceSupport{

	public static final String DOCS_DESCARREGADOS_NAO_CONFERIDOS = "docsDescarregadosNaoConferidos";
	public static final String DOCS_NAO_DESCARREGADOS_CONFERIDOS = "docsNaoDescarregadosConferidos";
	public static final String DOCS_DESCARREGADOS_INCOMPLETOS = "docsDescarregadosIncompletos";
	public static final String DOCS_DESCARREGADOS_SEM_DOC = "docsDescarregadosSemDoc";
	public static final String VOLUME_SOBRA = "volumeSobra";
	public static final String VOLUMES_COLETADOS_NAO_DESCARREGADOS = "volumesColetadosNaoDescarregados";
	
	@Override
    public JRReportDataObject execute(Map parameters) throws Exception {
		
		String sql = null;
		
		String tpRelatorio = (String) parameters.get("tpRelatorio");
		if (DOCS_DESCARREGADOS_NAO_CONFERIDOS.equals(tpRelatorio)){
			sql = getSqlDescarregadosNaoConferidos((String)parameters.get("tpControleCarga"), Long.valueOf((String)parameters.get("idControleCarga"))); 
		}else if (DOCS_NAO_DESCARREGADOS_CONFERIDOS.equals(tpRelatorio)){
			sql = getSqlNaoDescarregadosConferidos((String)parameters.get("tpControleCarga"), Long.valueOf((String)parameters.get("idControleCarga")));
		}else if (DOCS_DESCARREGADOS_SEM_DOC.equals(tpRelatorio)){
			sql = getSqlDescarregadosSemDoc((String)parameters.get("tpControleCarga"), Long.valueOf((String)parameters.get("idControleCarga")),false);
		}else if (DOCS_DESCARREGADOS_INCOMPLETOS.equals(tpRelatorio)){
			sql= getSqlDescarregadosIncompletos((String)parameters.get("tpControleCarga"), Long.valueOf((String)parameters.get("idControleCarga")),false);
		}else if (VOLUME_SOBRA.equals(tpRelatorio)){
			sql= getSqlVolumeSobra(Long.valueOf((String)parameters.get("idCarregamentoDescarga")));
		}else if (VOLUMES_COLETADOS_NAO_DESCARREGADOS.equals(tpRelatorio)){
			sql= getSqlVolumesColetadosNaoDescarregados();
		}
		JRReportDataObject jr = null;
		
		if(VOLUMES_COLETADOS_NAO_DESCARREGADOS.equals(tpRelatorio)){
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("idControleCarga", Long.valueOf((String)parameters.get("idControleCarga")));
			params.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
			jr = executeQuery(sql, params);
		}else{
			jr = executeQuery(sql,new Object[]{});
		}
		
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("tpDocumento", tpRelatorio);
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        
        return jr;
    }
	
	public Long getRowCountByTpRelatorio(Long idControleCarga, String tpRelatorio, String tpControleCarga, Long idCarregamentoDescarga){
		String sql = null;
		
		if (DOCS_DESCARREGADOS_NAO_CONFERIDOS.equals(tpRelatorio)){
			sql = getSqlDescarregadosNaoConferidos(tpControleCarga , idControleCarga); 
		}else if (DOCS_NAO_DESCARREGADOS_CONFERIDOS.equals(tpRelatorio)){
			sql = getSqlNaoDescarregadosConferidos(tpControleCarga, idControleCarga);
		}else if (DOCS_DESCARREGADOS_SEM_DOC.equals(tpRelatorio)){
			sql = getSqlDescarregadosSemDoc(tpControleCarga,idControleCarga,true);
		}else if (DOCS_DESCARREGADOS_INCOMPLETOS.equals(tpRelatorio)){
			sql= getSqlDescarregadosIncompletos(tpControleCarga,idControleCarga,true);
		}else if (VOLUME_SOBRA.equals(tpRelatorio)){
			sql= getSqlVolumeSobra(idCarregamentoDescarga);
		}
		
		List result = getJdbcTemplate().queryForList(sql);
		
		return result!=null?result.size():0L;
	}
	
	public Integer getTotalVolumesNaoDescarregados(Long idControleCarga){
		String sql = "SELECT SUM(QT_VOLUMES_COLETADOS) - sum(QT_VOLUMES_DESCARREGADOS) AS TOTAL FROM (" + getSqlVolumesColetadosNaoDescarregados() + ") ";
		return getJdbcTemplate().queryForInt(sql, new Object[]{idControleCarga, SessionUtils.getFilialSessao().getIdFilial()});
		
	}
	
	private String getSqlDescarregadosIncompletos(String tpControleCarga,Long idControleCarga, boolean rowCount) {
		if ("V".equals(tpControleCarga)){
			return getSqlDescarregadosIncompletosViagem(idControleCarga,rowCount);
		}else{
			return getSqlDescarregadosIncompletosColeta(idControleCarga,rowCount);
		}
	}
	
	private String getSqlDescarregadosSemDoc(String tpControleCarga,
            Long idControleCarga,boolean rowCount) {
		if ("V".equals(tpControleCarga)){
			return getSqlDescarregadosSemDocumentacaoViagem(idControleCarga,rowCount);
		}else{
			return getSqlDescarregadosSemDocumentacaoColeta(idControleCarga,rowCount);
		}
	}

	private String getSqlNaoDescarregadosConferidos(String tpControleCarga,Long idControleCarga) {
		if ("V".equals(tpControleCarga)){
			return getSqlNaoDescarregadosConferidosViagem(idControleCarga);
		}else{
			return getSqlNaoDescarregadosConferidosColeta(idControleCarga);
		}
    }

	private String getSqlDescarregadosNaoConferidos(String tpControleCarga, Long idControleCarga){
		if ("V".equals(tpControleCarga)){
			return getSqlDescarregadosNaoConferidosViagem(idControleCarga);
		}else{
			return getSqlDescarregadosNaoConferidosColeta(idControleCarga);
		}
	}
	
	private String getSqlVolumeSobra(Long idCarregamentoDescarga){
		StringBuilder builder = new StringBuilder();

		builder.append(" SELECT FO_DS.SG_FILIAL DS_FILIAL_ORIGEM, ") 
		.append("                DS.NR_DOCTO_SERVICO DS_NUMERO, ")
		.append(PropertyVarcharI18nProjection.createProjection("LM_DS.DS_LOCALIZACAO_MERCADORIA_I")+ " LOCALIZACAO,")
		.append("                VNF.NR_SEQUENCIA_PALETE NR_SEQUENCIA, ")
		.append("                DS.QT_VOLUMES QT_VOLUMES, ")
		.append("                FO_CC.SG_FILIAL CC_FILIAL_ORIGEM, ")
		.append("                CC.NR_CONTROLE_CARGA CC_NUMERO, ")
		.append("                '' MA_FILIAL_ORIGEM, ")
		.append("                '' MA_NUMERO, ")
		.append("                '' NR_NOTA_FISCAL, ")
		
		
		.append(" ''  NM_CIA_AEREA, ")
		.append(" ''  SG_EMPRESA, ")
		.append(" ''  DV_AWB, ")
		.append(" ''  DS_SERIE, ")
		.append(" ''  TP_STATUS_AWB, ")
		.append(" ''  ID_AWB, ")
		.append(" ''  NR_AWB, ")
		.append(" 0  QT_VOLUMES_COLETADOS, ")
		.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
		
		
		.append("                '' NR_FORMULARIO ")
		.append(" FROM   CONTROLE_CARGA CC, ")
		.append("        carregamento_descarga CD, ")
		.append("        DOCTO_SERVICO DS, ")
		.append("        FILIAL FO_DS, ")
		.append("        LOCALIZACAO_MERCADORIA LM_DS, ")
		.append("        VOLUME_NOTA_FISCAL VNF, ")
		.append("        VOLUME_SOBRA VS, ")
		.append("        NOTA_FISCAL_CONHECIMENTO NFC, ")
		.append("        FILIAL FO_CC ")
		.append("  WHERE VS.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL ")
		.append("        AND VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
		.append("        AND NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
		.append("        AND DS.ID_FILIAL_ORIGEM = FO_DS.ID_FILIAL ")
		.append("        and VS.ID_CARREGAMENTO_DESCARGA = CD.ID_CARREGAMENTO_DESCARGA ")
		.append("        AND CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
		.append("        AND CC.ID_FILIAL_ORIGEM = FO_CC.ID_FILIAL ")
		.append("        AND DS.ID_LOCALIZACAO_MERCADORIA = LM_DS.ID_LOCALIZACAO_MERCADORIA ")
		.append("        AND VS.ID_CARREGAMENTO_DESCARGA = "+idCarregamentoDescarga);
		
		return builder.toString();
	}
	
	private String getSqlDescarregadosNaoConferidosViagem(Long idControleCarga){
		StringBuilder builder = new StringBuilder();
		
		
		builder.append(" SELECT FO_CC.SG_FILIAL                 CC_FILIAL_ORIGEM,")
		.append("             CC.NR_CONTROLE_CARGA    CC_NUMERO,  ")
		.append("             FO_MA.SG_FILIAL                 MA_FILIAL_ORIGEM,")
		.append("             MA.NR_PRE_MANIFESTO        MA_NUMERO,")
		.append("             FO_DS.SG_FILIAL                  DS_FILIAL_ORIGEM,")
		.append("             DS.NR_DOCTO_SERVICO        DS_NUMERO,")
		.append("             CO.NR_FORMULARIO, ")
		
		.append("             CO.NR_FORMULARIO LOCALIZACAO,")
		.append("             CO.NR_FORMULARIO NR_NOTA_FISCAL,")
		.append("             CO.NR_FORMULARIO NR_SEQUENCIA,")
		
		.append(" ''  NM_CIA_AEREA, ")
		.append(" ''  SG_EMPRESA, ")
		.append(" ''  DV_AWB, ")
		.append(" ''  DS_SERIE, ")
		.append(" ''  TP_STATUS_AWB, ")
		.append(" ''  ID_AWB, ")
		.append(" ''  NR_AWB, ")
		.append(" 0  QT_VOLUMES_COLETADOS, ")
		.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
		
		
		.append("             CO.NR_FORMULARIO QT_VOLUMES")

		
		.append(" FROM CONTROLE_CARGA               CC,")
		.append("          MANIFESTO                          MA,")
		.append("          MANIFESTO_NACIONAL_CTO MNC,")
		.append("          DOCTO_SERVICO                  DS,")
		.append("          CONHECIMENTO                   CO,")
		.append("          FILIAL                                  FO_CC,")
		.append("          FILIAL                                  FO_MA,")
		.append("          FILIAL                                  FO_DS,")
		.append("          CARREGAMENTO_DESCARGA CD")
		.append(" WHERE MNC.ID_MANIFESTO_VIAGEM_NACIONAL        = MA.ID_MANIFESTO(+)")
		.append("     AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("     AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("     AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("     AND CD.TP_OPERACAO          = 'D'")
		.append("     AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("     AND CC.TP_CONTROLE_CARGA = 'V'")
		.append("     AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("     AND DS.ID_DOCTO_SERVICO     = CO.ID_CONHECIMENTO")
		.append("     AND DS.ID_DOCTO_SERVICO     = MNC.ID_CONHECIMENTO(+) ")
		.append("     AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("     AND EXISTS ")
		.append("      (SELECT NFC.ID_CONHECIMENTO")
		.append("       FROM EVENTO_VOLUME           EV,")
		.append("            EVENTO                   E,")
		.append("            NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("            VOLUME_NOTA_FISCAL       VNF,")
		.append("            MANIFESTO_NACIONAL_VOLUME MNV")
		.append("       WHERE EV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("         AND NFC.ID_NOTA_FISCAL_CONHECIMENTO   = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("         AND EV.ID_EVENTO                      = E.ID_EVENTO")
		.append("         AND VNF.TP_VOLUME                     IN ('U' , 'D')")
		.append("         AND EV.BL_EVENTO_CANCELADO            = 'N'")
		.append("         AND E.CD_EVENTO in (30,31,32,125)")
		.append("		  AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE")
		.append("         AND NFC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("         AND MNV.ID_VOLUME_NOTA_FISCAL         = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("         AND MNV.ID_CONHECIMENTO               = NFC.ID_CONHECIMENTO")
		.append("         AND MNV.ID_MANIFESTO_NACIONAL_CTO     = MNC.ID_MANIFESTO_NACIONAL_CTO")
		.append("         AND MNV.ID_MANIFESTO_VIAGEM_NACIONAL  = MNC.ID_MANIFESTO_VIAGEM_NACIONAL")
		.append("         AND MNV.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("      )")
		.append("   AND NOT EXISTS")
		.append("      (SELECT * FROM  CONTROLE_CARGA_CONH_SCAN CCCS")
		.append("       WHERE CCCS.ID_CONTROLE_CARGA        = CC.ID_CONTROLE_CARGA")
		.append("         AND CCCS.ID_CONHECIMENTO          = CO.ID_CONHECIMENTO")
		.append("         AND CCCS.ID_CARREGAMENTO_DESCARGA = CD.ID_CARREGAMENTO_DESCARGA")
		.append("      )  ")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO = "+SessionUtils.getFilialSessao().getIdFilial())	
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ")
		.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_FILIAL_ORIGEM,DS.ID_DOCTO_SERVICO");
		
		return builder.toString();
	}
	
	private String getSqlDescarregadosNaoConferidosColeta(Long idControleCarga){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT FO_CC.SG_FILIAL              CC_FILIAL_ORIGEM, ")
		.append("            CC.NR_CONTROLE_CARGA CC_NUMERO,  ")
		.append("            FO_MA.SG_FILIAL             MA_FILIAL_ORIGEM, ")
		.append("            MA.NR_PRE_MANIFESTO    MA_NUMERO, ")
		.append("            FO_DS.SG_FILIAL             DS_FILIAL_ORIGEM, ")
		.append("            DS.NR_DOCTO_SERVICO   DS_NUMERO, ")
		.append("            DS.NR_DOCTO_SERVICO   LOCALIZACAO, ")
		.append("            DS.NR_DOCTO_SERVICO   NR_NOTA_FISCAL, ")
		.append("            DS.NR_DOCTO_SERVICO   NR_SEQUENCIA, ")
		.append("            DS.NR_DOCTO_SERVICO   QT_VOLUMES, ")
		
		.append(" ''  NM_CIA_AEREA, ")
		.append(" ''  SG_EMPRESA, ")
		.append(" ''  DV_AWB, ")
		.append(" ''  DS_SERIE, ")
		.append(" ''  TP_STATUS_AWB, ")
		.append(" ''  ID_AWB, ")
		.append(" ''  NR_AWB, ")
		.append(" 0  QT_VOLUMES_COLETADOS, ")
		.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
		
		.append("            CO.NR_FORMULARIO         NR_FORMULARIO")
		
		.append(" FROM CONTROLE_CARGA          CC, ")
		.append("     MANIFESTO               MA, ")
		.append("     MANIFESTO_ENTREGA_DOCUMENTO  MED, ")
		.append("     DOCTO_SERVICO           DS,")
		.append("     CONHECIMENTO            CO,")
		.append("     FILIAL                  FO_CC,")
		.append("     FILIAL                  FO_MA,")
		.append("     FILIAL                  FO_DS,")
		.append("     CARREGAMENTO_DESCARGA    CD")
		.append(" WHERE MED.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO(+)")
		.append("  AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("  AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("  AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("  AND CD.TP_OPERACAO          = 'D'")
		.append("  AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("  AND CC.TP_CONTROLE_CARGA = 'C'")
		.append("  AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("  AND DS.ID_DOCTO_SERVICO     = CO.ID_CONHECIMENTO")
		.append("  AND DS.ID_DOCTO_SERVICO     = MED.ID_DOCTO_SERVICO(+) ")
		.append("  AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("  AND EXISTS ")
		.append("     (SELECT NFC.ID_CONHECIMENTO")
		.append("      FROM EVENTO_VOLUME           EV, ")
		.append("           EVENTO                   E,")
		.append("           NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("           VOLUME_NOTA_FISCAL       VNF,")
		.append("           MANIFESTO_ENTREGA_VOLUME MEV")
		.append("      WHERE EV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("        AND NFC.ID_NOTA_FISCAL_CONHECIMENTO   = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("        AND EV.ID_EVENTO                      = E.ID_EVENTO")
		.append("        AND VNF.TP_VOLUME                     IN ('U' , 'D')")
		.append("        AND EV.BL_EVENTO_CANCELADO            = 'N'")
		.append("        AND E.CD_EVENTO in (30,31,32,125)")
		.append("        AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE ")
		.append("        AND NFC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("        AND MEV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("        AND MEV.ID_DOCTO_SERVICO               = NFC.ID_CONHECIMENTO")
		.append("        AND MEV.ID_MANIFESTO_ENTREGA_DOCUMENTO = MED.ID_MANIFESTO_ENTREGA_DOCUMENTO")
		.append("        AND MEV.ID_MANIFESTO_ENTREGA           = MED.ID_MANIFESTO_ENTREGA")
		.append("        AND MEV.ID_DOCTO_SERVICO               = DS.ID_DOCTO_SERVICO")
		.append("     )")
		.append("  AND NOT EXISTS")
		.append("     (SELECT * FROM  CONTROLE_CARGA_CONH_SCAN CCCS")
		.append("      WHERE CCCS.ID_CONTROLE_CARGA        = CC.ID_CONTROLE_CARGA")
		.append("        AND CCCS.ID_CONHECIMENTO          = CO.ID_CONHECIMENTO")
		.append("        AND CCCS.ID_CARREGAMENTO_DESCARGA = CD.ID_CARREGAMENTO_DESCARGA")
		.append("     )  ")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())	
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ")
		.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_FILIAL_ORIGEM,DS.ID_DOCTO_SERVICO");

		return builder.toString();
	}
	
	private String getSqlNaoDescarregadosConferidosViagem(Long idControleCarga){
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT FO_CC.SG_FILIAL              CC_FILIAL_ORIGEM, ")
		.append("             CC.NR_CONTROLE_CARGA CC_NUMERO,  ")
		.append("             FO_MA.SG_FILIAL              MA_FILIAL_ORIGEM, ")
		.append("             MA.NR_PRE_MANIFESTO     MA_NUMERO, ")
		.append("             FO_DS.SG_FILIAL               DS_FILIAL_ORIGEM, ")
		.append("             DS.NR_DOCTO_SERVICO    DS_NUMERO, ")
		.append("            DS.NR_DOCTO_SERVICO   LOCALIZACAO, ")
		.append("            DS.NR_DOCTO_SERVICO   NR_NOTA_FISCAL, ")
		.append("            DS.NR_DOCTO_SERVICO   NR_SEQUENCIA, ")
		.append("            DS.NR_DOCTO_SERVICO   QT_VOLUMES, ")
		
		.append(" ''  NM_CIA_AEREA, ")
		.append(" ''  SG_EMPRESA, ")
		.append(" ''  DV_AWB, ")
		.append(" ''  DS_SERIE, ")
		.append(" ''  TP_STATUS_AWB, ")
		.append(" ''  ID_AWB, ")
		.append(" ''  NR_AWB, ")
		.append(" 0  QT_VOLUMES_COLETADOS, ")
		.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
		
		.append("             CO.NR_FORMULARIO          NR_FORMULARIO")
		
		.append(" FROM CONTROLE_CARGA          CC, ")
		.append("      MANIFESTO               MA, ")
		.append("      MANIFESTO_NACIONAL_CTO  MNC, ")
		.append("      DOCTO_SERVICO           DS,")
		.append("      CONHECIMENTO            CO,")
		.append("      FILIAL                  FO_CC,")
		.append("      FILIAL                  FO_MA,")
		.append("      FILIAL                  FO_DS,")
		.append("      CARREGAMENTO_DESCARGA    CD,")
		.append("      CONTROLE_CARGA_CONH_SCAN CCCS")
		.append(" WHERE CCCS.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
		.append("   AND CCCS.ID_CONHECIMENTO   = CO.ID_CONHECIMENTO")
		.append("   AND CCCS.ID_CARREGAMENTO_DESCARGA = CD.ID_CARREGAMENTO_DESCARGA")
		.append("   AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL        = MA.ID_MANIFESTO(+)")
		.append("   AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("   AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("   AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("   AND CD.TP_OPERACAO          = 'D'")
		.append("   AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("   AND CC.TP_CONTROLE_CARGA = 'V'")
		.append("   AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_DOCTO_SERVICO     = CO.ID_CONHECIMENTO")
		.append("   AND DS.ID_DOCTO_SERVICO     = MNC.ID_CONHECIMENTO(+) ")
		.append("   AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("   AND NOT EXISTS ")
		.append("      (SELECT NFC.ID_CONHECIMENTO")
		.append("       FROM EVENTO_VOLUME           EV, ")
		.append("            EVENTO                   E,")
		.append("            NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("            VOLUME_NOTA_FISCAL       VNF,")
		.append("            MANIFESTO_NACIONAL_VOLUME MNV")
		.append("       WHERE EV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("         AND NFC.ID_NOTA_FISCAL_CONHECIMENTO   = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("         AND EV.ID_EVENTO                      = E.ID_EVENTO")
		.append("         AND VNF.TP_VOLUME                     IN ('U' , 'D')")
		.append("         AND EV.BL_EVENTO_CANCELADO            = 'N'")
		.append("         AND E.CD_EVENTO in (30,31,32,125)")
		.append("         AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE ")
		.append("         AND NFC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
		.append("         AND MNV.ID_VOLUME_NOTA_FISCAL         = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("         AND MNV.ID_CONHECIMENTO               = NFC.ID_CONHECIMENTO")
		.append("         AND MNV.ID_MANIFESTO_NACIONAL_CTO     = MNC.ID_MANIFESTO_NACIONAL_CTO")
		.append("         AND MNV.ID_MANIFESTO_VIAGEM_NACIONAL  = MNC.ID_MANIFESTO_VIAGEM_NACIONAL")
		.append("         AND MNV.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("      )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())
		.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_FILIAL_ORIGEM,DS.ID_DOCTO_SERVICO");

		return builder.toString();
	}
	
	private String getSqlNaoDescarregadosConferidosColeta(Long idControleCarga){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT FO_CC.SG_FILIAL              CC_FILIAL_ORIGEM,")
		.append("            CC.NR_CONTROLE_CARGA CC_NUMERO,  ")
		.append("            FO_MA.SG_FILIAL             MA_FILIAL_ORIGEM,")
		.append("            MA.NR_PRE_MANIFESTO    MA_NUMERO,")
		.append("            FO_DS.SG_FILIAL              DS_FILIAL_ORIGEM,")
		.append("            DS.NR_DOCTO_SERVICO    DS_NUMERO,")
		
		.append(" ''  NM_CIA_AEREA, ")
		.append(" ''  SG_EMPRESA, ")
		.append(" ''  DV_AWB, ")
		.append(" ''  DS_SERIE, ")
		.append(" ''  TP_STATUS_AWB, ")
		.append(" ''  ID_AWB, ")
		.append(" ''  NR_AWB, ")
		.append(" 0  QT_VOLUMES_COLETADOS, ")
		.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
		
		.append("            CO.NR_FORMULARIO          NR_FORMULARIO")
		.append(" FROM CONTROLE_CARGA          CC,")
		.append("     MANIFESTO               MA,")
		.append("     MANIFESTO_ENTREGA_DOCUMENTO  MED,")
		.append("     DOCTO_SERVICO           DS,")
		.append("     CONHECIMENTO            CO,")
		.append("     FILIAL                  FO_CC,")
		.append("     FILIAL                  FO_MA,")
		.append("     FILIAL                  FO_DS,")
		.append("     CARREGAMENTO_DESCARGA    CD,")
		.append("     CONTROLE_CARGA_CONH_SCAN CCCS")
		.append(" WHERE CCCS.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
		.append("  AND CCCS.ID_CONHECIMENTO   = CO.ID_CONHECIMENTO")
		.append("  AND CCCS.ID_CARREGAMENTO_DESCARGA = CD.ID_CARREGAMENTO_DESCARGA")
		.append("  AND MED.ID_MANIFESTO_ENTREGA        = MA.ID_MANIFESTO(+)")
		.append("  AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("  AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("  AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("  AND CD.TP_OPERACAO          = 'D'")
		.append("  AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("  AND CC.TP_CONTROLE_CARGA = 'C'")
		.append("  AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("  AND DS.ID_DOCTO_SERVICO     = CO.ID_CONHECIMENTO")
		.append("  AND DS.ID_DOCTO_SERVICO     = MED.ID_DOCTO_SERVICO(+) ")
		.append("  AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("  AND NOT EXISTS ")
		.append("     (SELECT NFC.ID_CONHECIMENTO")
		.append("      FROM EVENTO_VOLUME           EV,")
		.append("           EVENTO                   E,")
		.append("           NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("           VOLUME_NOTA_FISCAL       VNF,")
		.append("           MANIFESTO_ENTREGA_VOLUME MEV")
		.append("      WHERE EV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("        AND NFC.ID_NOTA_FISCAL_CONHECIMENTO   = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("        AND EV.ID_EVENTO                      = E.ID_EVENTO")
		.append("        AND VNF.TP_VOLUME                     IN ('U' , 'D')")
		.append("        AND EV.BL_EVENTO_CANCELADO            = 'N'")
		.append("        AND E.CD_EVENTO in (30,31,32,125)")
		.append("        AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE")
		.append("        AND NFC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
		.append("        AND MEV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("        AND MEV.ID_DOCTO_SERVICO               = NFC.ID_CONHECIMENTO")
		.append("        AND MEV.ID_MANIFESTO_ENTREGA_DOCUMENTO = MED.ID_MANIFESTO_ENTREGA_DOCUMENTO")
		.append("        AND MEV.ID_MANIFESTO_ENTREGA           = MED.ID_MANIFESTO_ENTREGA")
		.append("        AND MEV.ID_DOCTO_SERVICO               = DS.ID_DOCTO_SERVICO")
		.append("     )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())
		.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_FILIAL_ORIGEM,DS.ID_DOCTO_SERVICO");

		return builder.toString();
	}
	
	private String getSqlDescarregadosIncompletosViagem(Long idControleCarga, boolean rowCount){
		StringBuilder builder = new StringBuilder();

		if (!rowCount){
			builder.append(" SELECT * FROM (");
			builder.append(" SELECT FO_CC.SG_FILIAL                                     CC_FILIAL_ORIGEM,")
			.append("             CC.NR_CONTROLE_CARGA                        CC_NUMERO,  ")
			.append("             FO_MA.SG_FILIAL                                    MA_FILIAL_ORIGEM,")
			.append("             MA.NR_PRE_MANIFESTO                           MA_NUMERO,")
			.append("             FO_DS.SG_FILIAL                                     DS_FILIAL_ORIGEM,")
			.append("             DS.NR_DOCTO_SERVICO                           DS_NUMERO,")
			.append("             DS.QT_VOLUMES                                      DS_TOTAL,")
			.append(PropertyVarcharI18nProjection.createProjection("LM_DS.DS_LOCALIZACAO_MERCADORIA_I")+ " LOCALIZACAO,")
			.append("             NFC.NR_NOTA_FISCAL                              NR_NOTA_FISCAL,")
			.append("             VNF.NR_SEQUENCIA_PALETE                         NR_SEQUENCIA,")
			.append("             VNF.NR_SEQUENCIA_PALETE                         NR_FORMULARIO,")
			.append("             DS.QT_VOLUMES                                 QT_VOLUMES,")
			
			.append(" ''  NM_CIA_AEREA, ")
			.append(" ''  SG_EMPRESA, ")
			.append(" ''  DV_AWB, ")
			.append(" ''  DS_SERIE, ")
			.append(" ''  TP_STATUS_AWB, ")
			.append(" ''  ID_AWB, ")
			.append(" ''  NR_AWB, ")
			.append(" 0  QT_VOLUMES_COLETADOS, ")
			.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
			
			
			.append("				COUNT(*) OVER(PARTITION BY DS.NR_DOCTO_SERVICO) QT_INCOMPLETOS");
		}else{
			builder.append(" SELECT distinct ID_DOCTO_SERVICO FROM (")
			.append("				SELECT DS.ID_DOCTO_SERVICO, ")
			.append("							COUNT(*) OVER(PARTITION BY DS.NR_DOCTO_SERVICO) QT_INCOMPLETOS,")
			.append("							DS.QT_VOLUMES  QT_VOLUMES ");
		}
		
		builder.append(" FROM CONTROLE_CARGA          CC,")
		.append("      MANIFESTO               MA,")
		.append("      MANIFESTO_NACIONAL_CTO  MNC,")
		.append("      MANIFESTO_NACIONAL_VOLUME MNV,")
		.append("      DOCTO_SERVICO           DS,")
		.append("      LOCALIZACAO_MERCADORIA  LM_DS,")
		.append("      FILIAL                  FO_CC,")
		.append("      FILIAL                  FO_MA,")
		.append("      FILIAL                  FO_DS,")
		.append("      VOLUME_NOTA_FISCAL      VNF,")
		.append("      NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("      CARREGAMENTO_DESCARGA    CD")
		.append(" WHERE MNC.ID_MANIFESTO_VIAGEM_NACIONAL        = MA.ID_MANIFESTO(+)")
		.append("   AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("   AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("   AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("   AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("   AND CC.TP_CONTROLE_CARGA = 'V'")
		.append("   AND CD.TP_OPERACAO          = 'D'")
		.append("   AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_DOCTO_SERVICO     = MNC.ID_CONHECIMENTO(+) ")
		.append("   AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_LOCALIZACAO_MERCADORIA = LM_DS.ID_LOCALIZACAO_MERCADORIA")
		.append("   AND DS.ID_DOCTO_SERVICO     = NFC.ID_CONHECIMENTO")
		.append("   AND NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("   AND VNF.TP_VOLUME IN ('U','D')")
		.append("   AND MNV.ID_VOLUME_NOTA_FISCAL         = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("   AND MNV.ID_CONHECIMENTO               = NFC.ID_CONHECIMENTO")
		.append("   AND MNV.ID_MANIFESTO_NACIONAL_CTO     = MNC.ID_MANIFESTO_NACIONAL_CTO")
		.append("   AND MNV.ID_MANIFESTO_VIAGEM_NACIONAL  = MNC.ID_MANIFESTO_VIAGEM_NACIONAL")
		.append("   AND MNV.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("   AND NOT EXISTS ")
		.append("       (SELECT EV.ID_VOLUME_NOTA_FISCAL")
		.append("        FROM EVENTO_VOLUME           EV,")
		.append("             EVENTO                   E   ")
		.append("        WHERE EV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("          AND EV.ID_EVENTO             = E.ID_EVENTO")
		.append("          AND EV.BL_EVENTO_CANCELADO   = 'N'")
		.append("          AND E.CD_EVENTO in (30,31,32,125)")
		.append("          AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE")
		.append("          ")
		.append("       )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ");
		if (!rowCount){
			builder.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_DOCTO_SERVICO,NFC.NR_NOTA_FISCAL,VNF.NR_SEQUENCIA_PALETE");
		}
		builder.append(") incompletos ")
		.append("WHERE QT_VOLUMES > QT_INCOMPLETOS");

		return builder.toString();
	}
	
	private String getSqlDescarregadosIncompletosColeta(Long idControleCarga, boolean rowCount){
		StringBuilder builder = new StringBuilder();
		if (!rowCount){
			builder.append(" SELECT * FROM (")
			.append(" SELECT FO_CC.SG_FILIAL                                     CC_FILIAL_ORIGEM,")
			.append("             CC.NR_CONTROLE_CARGA                        CC_NUMERO,  ")
			.append("             FO_MA.SG_FILIAL                                    MA_FILIAL_ORIGEM,")
			.append("             MA.NR_PRE_MANIFESTO                           MA_NUMERO,")
			.append("             FO_DS.SG_FILIAL                                     DS_FILIAL_ORIGEM,")
			.append("             DS.NR_DOCTO_SERVICO                           DS_NUMERO,")
			.append("             DS.QT_VOLUMES                                      DS_TOTAL,")
			.append(PropertyVarcharI18nProjection.createProjection("LM_DS.DS_LOCALIZACAO_MERCADORIA_I")+ " LOCALIZACAO,")
			.append("             NFC.NR_NOTA_FISCAL                              NR_NOTA_FISCAL,")
			.append("             VNF.NR_SEQUENCIA_PALETE                         NR_SEQUENCIA,")
			.append("             VNF.NR_SEQUENCIA_PALETE                         NR_FORMULARIO,")
			.append("             DS.QT_VOLUMES                                 QT_VOLUMES,")
			
			.append(" ''  NM_CIA_AEREA, ")
			.append(" ''  SG_EMPRESA, ")
			.append(" ''  DV_AWB, ")
			.append(" ''  DS_SERIE, ")
			.append(" ''  TP_STATUS_AWB, ")
			.append(" ''  ID_AWB, ")
			.append(" ''  NR_AWB, ")
			.append(" 0  QT_VOLUMES_COLETADOS, ")
			.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
			
			.append("				COUNT(*) OVER(PARTITION BY DS.NR_DOCTO_SERVICO) QT_INCOMPLETOS");
		}else{
			builder.append(" SELECT distinct ID_DOCTO_SERVICO FROM (")
			.append(" SELECT distinct DS.ID_DOCTO_SERVICO, ")
			.append("COUNT(*) OVER(PARTITION BY DS.NR_DOCTO_SERVICO) QT_INCOMPLETOS,")
			.append("DS.QT_VOLUMES");
		}
		
		builder.append(" FROM CONTROLE_CARGA          CC, ")
		.append("      MANIFESTO               MA, ")
		.append("      MANIFESTO_ENTREGA_DOCUMENTO MED, ")
		.append("      MANIFESTO_ENTREGA_VOLUME MEV,")
		.append("      DOCTO_SERVICO           DS,")
		.append("      LOCALIZACAO_MERCADORIA  LM_DS, ")
		.append("      FILIAL                  FO_CC,")
		.append("      FILIAL                  FO_MA,")
		.append("      FILIAL                  FO_DS,")
		.append("      VOLUME_NOTA_FISCAL      VNF,")
		.append("      NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("      CARREGAMENTO_DESCARGA    CD")
		.append(" WHERE MED.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO(+)")
		.append("   AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("   AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("   AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("   AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("   AND CC.TP_CONTROLE_CARGA = 'C'")
		.append("   AND CD.TP_OPERACAO          = 'D'")
		.append("   AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_DOCTO_SERVICO     = MED.ID_DOCTO_SERVICO(+) ")
		.append("   AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_LOCALIZACAO_MERCADORIA = LM_DS.ID_LOCALIZACAO_MERCADORIA")
		.append("   AND DS.ID_DOCTO_SERVICO     = NFC.ID_CONHECIMENTO")
		.append("   AND NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("   AND VNF.TP_VOLUME IN ('U','D')")
		.append("   AND MEV.ID_VOLUME_NOTA_FISCAL          = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("   AND MEV.ID_DOCTO_SERVICO               = NFC.ID_CONHECIMENTO")
		.append("   AND MEV.ID_MANIFESTO_ENTREGA_DOCUMENTO = MED.ID_MANIFESTO_ENTREGA_DOCUMENTO")
		.append("   AND MEV.ID_MANIFESTO_ENTREGA           = MED.ID_MANIFESTO_ENTREGA")
		.append("   AND MEV.ID_DOCTO_SERVICO               = DS.ID_DOCTO_SERVICO")
		.append("   AND NOT EXISTS ")
		.append("       (SELECT EV.ID_VOLUME_NOTA_FISCAL")
		.append("        FROM EVENTO_VOLUME           EV, ")
		.append("             EVENTO                   E   ")
		.append("        WHERE EV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("          AND EV.ID_EVENTO             = E.ID_EVENTO")
		.append("          AND EV.BL_EVENTO_CANCELADO   = 'N'")
		.append("          AND E.CD_EVENTO in (30,31,32,125)")
		.append("		  AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE")
		.append("       )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ");
		if (!rowCount){
			builder.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_DOCTO_SERVICO,NFC.NR_NOTA_FISCAL,VNF.NR_SEQUENCIA_PALETE");
		}
		builder.append(") incompletos ")
		.append("WHERE QT_VOLUMES > QT_INCOMPLETOS");

		return builder.toString();
	}
	
	private String getSqlDescarregadosSemDocumentacaoViagem(Long idControleCarga, boolean rowCount){
		StringBuilder builder = new StringBuilder();
		
		if(!rowCount){
			builder.append(" SELECT FO_CC.SG_FILIAL                                      CC_FILIAL_ORIGEM,")
			.append("             CC.NR_CONTROLE_CARGA                         CC_NUMERO,  ")
			.append("             FO_MA.SG_FILIAL                                     MA_FILIAL_ORIGEM,")
			.append("             MA.NR_PRE_MANIFESTO                           MA_NUMERO,")
			.append("             FO_DS.SG_FILIAL                                     DS_FILIAL_ORIGEM,")
			.append("             DS.NR_DOCTO_SERVICO                           DS_NUMERO,")
			.append("             DS.QT_VOLUMES                                      DS_TOTAL,")
			.append(PropertyVarcharI18nProjection.createProjection("LM_DS.DS_LOCALIZACAO_MERCADORIA_I")+ " LOCALIZACAO,")
			.append("             NFC.NR_NOTA_FISCAL                               NR_NOTA_FISCAL,")
			.append("             DS.QT_VOLUMES                               QT_VOLUMES,")
			.append("             NFC.NR_NOTA_FISCAL                               NR_FORMULARIO,")
			
			.append(" ''  NM_CIA_AEREA, ")
			.append(" ''  SG_EMPRESA, ")
			.append(" ''  DV_AWB, ")
			.append(" ''  DS_SERIE, ")
			.append(" ''  TP_STATUS_AWB, ")
			.append(" ''  ID_AWB, ")
			.append(" ''  NR_AWB, ")
			.append(" 0  QT_VOLUMES_COLETADOS, ")
			.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
			
			.append("             VNF.NR_SEQUENCIA_PALETE                          NR_SEQUENCIA");
		}else{
			builder.append(" SELECT distinct ds.id_docto_servico");
		}
		
		
		builder.append(" FROM CONTROLE_CARGA          CC,")
		.append("      MANIFESTO               MA,")
		.append("      MANIFESTO_NACIONAL_VOLUME MNV,")
		.append("      DOCTO_SERVICO           DS,")
		.append("      LOCALIZACAO_MERCADORIA  LM_DS,")
		.append("      FILIAL                  FO_CC,")
		.append("      FILIAL                  FO_MA,")
		.append("      FILIAL                  FO_DS,")
		.append("      VOLUME_NOTA_FISCAL      VNF,")
		.append("      NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("      CARREGAMENTO_DESCARGA    CD")
		.append(" WHERE MNV.ID_MANIFESTO_VIAGEM_NACIONAL = MA.ID_MANIFESTO(+)")
		.append("   AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("   AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("   AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("   AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("   AND CC.TP_CONTROLE_CARGA = 'V'")
		.append("   AND CD.TP_OPERACAO          = 'D'")
		.append("   AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_DOCTO_SERVICO     = MNV.ID_CONHECIMENTO(+) ")
		.append("   AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_LOCALIZACAO_MERCADORIA = LM_DS.ID_LOCALIZACAO_MERCADORIA")
		.append("   AND DS.ID_DOCTO_SERVICO     = NFC.ID_CONHECIMENTO")
		.append("   AND MNV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("   AND MNV.ID_CONHECIMENTO               = NFC.ID_CONHECIMENTO")
		.append("   AND MNV.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO")
		.append("   AND NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("   AND VNF.TP_VOLUME IN ('U','D')")
		.append("   AND EXISTS ")
		.append("       (SELECT EV.ID_VOLUME_NOTA_FISCAL")
		.append("        FROM EVENTO_VOLUME           EV,")
		.append("             EVENTO                   E   ")
		.append("        WHERE EV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("          AND EV.ID_EVENTO             = E.ID_EVENTO")
		.append("          AND EV.BL_EVENTO_CANCELADO   = 'N'")
		.append("          AND E.CD_EVENTO in (30,31,32,125)")
		.append("          AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE ")
		.append("       )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ")
		.append("  AND NOT EXISTS ")
		.append("       (SELECT MNC.ID_CONHECIMENTO")
		.append("                 FROM MANIFESTO_NACIONAL_CTO MNC")
		.append("                 WHERE MNC.ID_CONHECIMENTO              = MNV.ID_CONHECIMENTO")
		.append("                   AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MNV.ID_MANIFESTO_VIAGEM_NACIONAL")
		.append("                   AND MNC.ID_MANIFESTO_NACIONAL_CTO    = MNV.ID_MANIFESTO_NACIONAL_CTO")
		.append("       )");
		if (!rowCount){
			builder.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_DOCTO_SERVICO,NFC.NR_NOTA_FISCAL,VNF.NR_SEQUENCIA_PALETE");
		}
		


		return builder.toString();
	}
	
	private String getSqlDescarregadosSemDocumentacaoColeta(Long idControleCarga, boolean rowCount){
		StringBuilder builder = new StringBuilder();
		
		if(!rowCount){
			builder.append(" SELECT FO_CC.SG_FILIAL                                      CC_FILIAL_ORIGEM,")
			.append("             CC.NR_CONTROLE_CARGA                         CC_NUMERO,  ")
			.append("             FO_MA.SG_FILIAL                                     MA_FILIAL_ORIGEM,")
			.append("             MA.NR_PRE_MANIFESTO                           MA_NUMERO,")
			.append("             FO_DS.SG_FILIAL                                     DS_FILIAL_ORIGEM,")
			.append("             DS.NR_DOCTO_SERVICO                           DS_NUMERO,")
			.append("             DS.QT_VOLUMES                                      DS_TOTAL,")
			.append(PropertyVarcharI18nProjection.createProjection("LM_DS.DS_LOCALIZACAO_MERCADORIA_I")+ " LOCALIZACAO,")
			.append("             NFC.NR_NOTA_FISCAL                               NR_NOTA_FISCAL,")
			.append("             DS.QT_VOLUMES                               QT_VOLUMES,")
			.append("             NFC.NR_NOTA_FISCAL                               NR_FORMULARIO,")
			
			.append(" ''  NM_CIA_AEREA, ")
			.append(" ''  SG_EMPRESA, ")
			.append(" ''  DV_AWB, ")
			.append(" ''  DS_SERIE, ")
			.append(" ''  TP_STATUS_AWB, ")
			.append(" ''  ID_AWB, ")
			.append(" ''  NR_AWB, ")
			.append(" 0  QT_VOLUMES_COLETADOS, ")
			.append(" 0  QT_VOLUMES_DESCARREGADOS, ")
			
			.append("             VNF.NR_SEQUENCIA_PALETE                          NR_SEQUENCIA");
		}else{
			builder.append(" SELECT distinct ds.id_docto_servico");
		}
		builder.append(" FROM CONTROLE_CARGA          CC,")
		.append("      MANIFESTO               MA,")
		.append("      MANIFESTO_ENTREGA_VOLUME MEV,")
		.append("      DOCTO_SERVICO           DS,")
		.append("      LOCALIZACAO_MERCADORIA  LM_DS,")
		.append("      FILIAL                  FO_CC,")
		.append("      FILIAL                  FO_MA,")
		.append("      FILIAL                  FO_DS,")
		.append("      VOLUME_NOTA_FISCAL      VNF,")
		.append("      NOTA_FISCAL_CONHECIMENTO NFC,")
		.append("      CARREGAMENTO_DESCARGA    CD")
		.append(" WHERE MEV.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO(+)")
		.append("   AND FO_MA.ID_FILIAL         = MA.ID_FILIAL_ORIGEM    ")
		.append("   AND MA.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA(+)")
		.append("   AND CD.ID_CONTROLE_CARGA    = CC.ID_CONTROLE_CARGA")
		.append("   AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'")
		.append("   AND CC.TP_CONTROLE_CARGA = 'C'")
		.append("   AND CD.TP_OPERACAO          = 'D'")
		.append("   AND FO_CC.ID_FILIAL         = CC.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_DOCTO_SERVICO     = MEV.ID_DOCTO_SERVICO(+) ")
		.append("   AND FO_DS.ID_FILIAL         = DS.ID_FILIAL_ORIGEM")
		.append("   AND DS.ID_LOCALIZACAO_MERCADORIA = LM_DS.ID_LOCALIZACAO_MERCADORIA")
		.append("   AND DS.ID_DOCTO_SERVICO     = NFC.ID_CONHECIMENTO")
		.append("   AND MEV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("   AND MEV.ID_DOCTO_SERVICO               = NFC.ID_CONHECIMENTO")
		.append("   AND MEV.ID_DOCTO_SERVICO               = DS.ID_DOCTO_SERVICO")
		.append("   AND NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO")
		.append("   AND VNF.TP_VOLUME IN ('U','D')")
		.append("   AND EXISTS ")
		.append("       (SELECT EV.ID_VOLUME_NOTA_FISCAL")
		.append("        FROM EVENTO_VOLUME           EV,")
		.append("             EVENTO                   E   ")
		.append("        WHERE EV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL")
		.append("          AND EV.ID_EVENTO             = E.ID_EVENTO")
		.append("          AND EV.BL_EVENTO_CANCELADO   = 'N'")
		.append("          AND E.CD_EVENTO in (30,31,32,125)")
		.append("          AND EV.DH_EVENTO BETWEEN CD.DH_INICIO_OPERACAO AND SYSDATE ")
		.append("       )")
		.append("  AND CC.ID_CONTROLE_CARGA = "+idControleCarga)
		.append("  AND CD.ID_FILIAL = MA.ID_FILIAL_DESTINO ")
		.append("  AND MA.ID_FILIAL_DESTINO   = "+SessionUtils.getFilialSessao().getIdFilial())		.append("   AND NOT EXISTS ")
		.append("       (SELECT MED.ID_DOCTO_SERVICO")
		.append("                 FROM MANIFESTO_ENTREGA_DOCUMENTO MED")
		.append("                 WHERE MED.ID_DOCTO_SERVICO              = MEV.ID_DOCTO_SERVICO")
		.append("                   AND MED.ID_MANIFESTO_ENTREGA = MEV.ID_MANIFESTO_ENTREGA")
		.append("                   AND MEV.ID_MANIFESTO_ENTREGA_DOCUMENTO = MED.ID_MANIFESTO_ENTREGA_DOCUMENTO")
		.append("       )");

		if (!rowCount){
			builder.append(" ORDER BY MA.NR_PRE_MANIFESTO, DS.ID_DOCTO_SERVICO,NFC.NR_NOTA_FISCAL,VNF.NR_SEQUENCIA_PALETE");
		}
		return builder.toString();
	}
	
	private String getSqlVolumesColetadosNaoDescarregados() {
		StringBuilder builder = new StringBuilder();
		builder.append("  SELECT * FROM (SELECT FO_CC.SG_FILIAL                           CC_FILIAL_ORIGEM, ")
				.append(" CC.NR_CONTROLE_CARGA                             CC_NUMERO,  ")
				
				.append(" ''                                  MA_FILIAL_ORIGEM,  ")
				.append(" ''                                  MA_NUMERO,  ")
				.append(" ''                                  NR_FORMULARIO,  ")
				.append(" ''                                  NR_NOTA_FISCAL,  ")
				.append(" ''                                  NR_SEQUENCIA,  ")
				.append(" ''                                  QT_VOLUMES,  ")
				
				.append(" NVL(FO_DS.SG_FILIAL,FO_DSAWB.SG_FILIAL)          DS_FILIAL_ORIGEM, ") 
				.append(" NVL(DS.NR_DOCTO_SERVICO, DSAWB.NR_DOCTO_SERVICO) DS_NUMERO, ")
				.append(" (SELECT EMP.SG_EMPRESA ")
				.append(" FROM   CIA_FILIAL_MERCURIO CFM, EMPRESA EMP ")
				.append(" WHERE  CFM.ID_EMPRESA = EMP.ID_EMPRESA ")
				.append(" AND    CFM.ID_CIA_FILIAL_MERCURIO IN (AWB_DS.ID_CIA_FILIAL_MERCURIO)) AS SG_EMPRESA, ") 
				
				.append(" ''  NM_CIA_AEREA, ")
				.append(" AWB_DS.DV_AWB				AS DV_AWB, ")
				.append(" AWB_DS.DS_SERIE			AS DS_SERIE, ")
				.append(" AWB_DS.TP_STATUS_AWB  	AS TP_STATUS_AWB, ")
				.append(" AWB_DS.ID_AWB				AS ID_AWB, ")
				.append(" AWB_DS.NR_AWB 			AS NR_AWB, ")
				
				.append(" NVL(DS.qt_volumes, DSAWB.qt_volumes)   AS QT_VOLUMES_COLETADOS, ")
				
				.append(" (SELECT COUNT(*)  ")
				.append(" FROM   CARREGAMENTO_DESCARGA CD, ")
				.append(" CARREG_DESC_VOLUME CDV, ")
				.append(" VOLUME_NOTA_FISCAL VNF, ")
				.append(" NOTA_FISCAL_CONHECIMENTO NFC ")
				.append(" WHERE  CD.ID_CARREGAMENTO_DESCARGA = CDV.ID_CARREGAMENTO_DESCARGA ")
				.append(" AND    CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
				.append(" AND    CDV.ID_VOLUME_NOTA_FISCAL = VNF.ID_VOLUME_NOTA_FISCAL ")
				.append(" AND    VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
				.append(" AND    NFC.ID_CONHECIMENTO = NVL(DS.ID_DOCTO_SERVICO, DSAWB.ID_DOCTO_SERVICO) ")
				.append(" AND    CD.TP_OPERACAO = 'D') AS QT_VOLUMES_DESCARREGADOS, ")
				.append(" (SELECT MIN(VI18N(DS_LOCALIZACAO_MERCADORIA_I)) ")
				.append(" FROM LOCALIZACAO_MERCADORIA LM  ")
				.append(" WHERE LM.ID_LOCALIZACAO_MERCADORIA IN (DS.ID_LOCALIZACAO_MERCADORIA, DSAWB.ID_LOCALIZACAO_MERCADORIA)) AS LOCALIZACAO ")
				.append(" FROM CONTROLE_CARGA CC ")
				.append(" INNER JOIN FILIAL FO_CC ON CC.ID_FILIAL_ORIGEM = FO_CC.ID_FILIAL ")
				.append(" INNER JOIN MANIFESTO_COLETA MC ON CC.ID_CONTROLE_CARGA = MC.ID_CONTROLE_CARGA ")
				.append(" INNER JOIN FILIAL FO_MC ON MC.ID_FILIAL_ORIGEM = FO_MC.ID_FILIAL ")
				.append(" INNER JOIN PEDIDO_COLETA PC ON MC.ID_MANIFESTO_COLETA = PC.ID_MANIFESTO_COLETA ")
				.append(" INNER JOIN DETALHE_COLETA DC ON DC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
				.append(" LEFT OUTER JOIN AWB_COLETA AC ON DC.ID_DETALHE_COLETA = AC.ID_DETALHE_COLETA ")
				.append(" LEFT OUTER JOIN AWB AW ON AC.ID_AWB = AW.ID_AWB ")
				.append(" LEFT OUTER JOIN CTO_AWB CA ON AW.ID_AWB = CA.ID_AWB ")
				.append(" LEFT OUTER JOIN DOCTO_SERVICO DSAWB ON CA.ID_CONHECIMENTO = DSAWB.ID_DOCTO_SERVICO ")
				.append(" LEFT OUTER JOIN FILIAL FO_DSAWB ON DSAWB.ID_FILIAL_ORIGEM = FO_DSAWB.ID_FILIAL ")
				.append(" LEFT OUTER JOIN DOCTO_SERVICO DS ON DC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
				.append(" LEFT OUTER JOIN FILIAL FO_DS ON DS.ID_FILIAL_ORIGEM = FO_DS.ID_FILIAL ")
				.append(" LEFT OUTER JOIN CTO_AWB CA_DS ON DS.ID_DOCTO_SERVICO = CA_DS.ID_CONHECIMENTO ")
				.append(" LEFT OUTER JOIN AWB AWB_DS ON CA_DS.ID_AWB = AWB_DS.ID_AWB ")
				.append(" WHERE PC.TP_PEDIDO_COLETA = 'AE' ")
				.append(" AND   CC.ID_CONTROLE_CARGA = :idControleCarga ")
				.append(" AND   MC.ID_FILIAL_ORIGEM = :idFilial ")
				.append(" AND (AWB_DS.ID_AWB = (select max(a.id_awb) from   cto_awb a join awb on awb.id_awb = a.id_awb where  a.id_conhecimento = DC.id_docto_servico and awb.tp_status_awb <> 'C')  or DC.id_docto_servico is null) ")
				.append(" ORDER BY NM_CIA_AEREA, NR_AWB, DS_FILIAL_ORIGEM, DS_NUMERO) X WHERE X.QT_VOLUMES_COLETADOS <> X.QT_VOLUMES_DESCARREGADOS ");
				
				return builder.toString();
	}
	
	
	
}

