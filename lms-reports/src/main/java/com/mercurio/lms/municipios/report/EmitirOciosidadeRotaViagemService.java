package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author 
 *
 * @spring.bean id="lms.municipios.emitirOciosidadeRotaViagemService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirOciosidadeRota.jasper"
 */
public class EmitirOciosidadeRotaViagemService extends ReportServiceSupport {

	private DomainService domainService;
	
	 
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap parameters = (TypedFlatMap)criteria;
		
        SqlTemplate sql = getSqlTemplate(parameters);                        
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("dsSimboloMoeda", parameters.getString("moeda.dsSimbolo"));
        parametersReport.put("idMoedaDestino", parameters.getLong("idMoedaDestino"));
        parametersReport.put("idPaisDestino", parameters.getLong("idPaisDestino"));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        
        return jr; 
	}
	
    private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
         
        Long idRotaIdaVolta = parameters.getLong("rotaIdaVolta.idRotaIdaVolta");
        String dsRota = parameters.getString("rotaViagem.dsRota");
        String tpRota = parameters.getString("rotaViagem.tpRota");
        DateTime dtVigenciaInicial = parameters.getYearMonthDay("rotaViagem.dtVigenciaInicial").toDateTimeAtMidnight();
        DateTime dtVigenciaFinal = JTDateTimeUtils.copyWithMaxTime(parameters.getYearMonthDay("rotaViagem.dtVigenciaFinal").toDateTimeAtMidnight());
        
        Long idFilialOrigem = parameters.getLong("filialOrigem.idFilial");
        Long idFilialDestino = parameters.getLong("filialDestino.idFilial");
        Long idFilialIntermediaria = parameters.getLong("filialIntermediaria.idFilial");
    	
        SqlTemplate ociosidadeSql = createSqlTemplate();
        
        ociosidadeSql.addProjection("CC.ID_CONTROLE_CARGA","ID_CONTROLE_CARGA");
        ociosidadeSql.addProjection("CC.NR_CONTROLE_CARGA","NR_CONTROLE_CARGA");
  
        ociosidadeSql.addProjection("FCC.SG_FILIAL","FILIAL_CONTROLE_CARGA");
        
        ociosidadeSql.addProjection("FO.ID_FILIAL","ID_FILIAL_ORIGEM");
        ociosidadeSql.addProjection("FO.SG_FILIAL","TRECHO_ORIGEM");
        ociosidadeSql.addProjection("FD.SG_FILIAL","TRECHO_DESTINO");
        ociosidadeSql.addProjection("CC.ID_MOEDA","ID_MOEDA_ORIGEM");
        ociosidadeSql.addProjection("CT.DH_SAIDA","HR_SAIDA");
        ociosidadeSql.addProjection("CT.NR_DISTANCIA","NR_DISTANCIA");

        ociosidadeSql.addProjection("RIV.NR_ROTA","NR_ROTA");
        ociosidadeSql.addProjection("RIV.ID_ROTA_IDA_VOLTA","ID_ROTA_IDA_VOLTA");
         
        ociosidadeSql.addProjection("R.DS_ROTA","DS_ROTA");
        ociosidadeSql.addProjection("RV.TP_ROTA","TP_ROTA");
        ociosidadeSql.addProjection("TIPO_MT.NR_CAPACIDADE_PESO_INICIAL","NR_CAPACIDADE_PESO_INICIAL");
        ociosidadeSql.addProjection("TIPO_MT.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE");
        ociosidadeSql.addProjection("RV.TP_ROTA","DS_TP_ROTA");
        
        // subqueries
        ociosidadeSql.addProjection(totalPesoRealSubquery(), "TOTAL_PESO_REAL");        
        ociosidadeSql.addProjection(totalPesoAforadoSubquery(),"TOTAL_PESO_AFORADO");
        
        ociosidadeSql.addProjection(new StringBuilder()
        		.append("F_CONV_MOEDA((")
        		.append(this.getPaisOrigemSubQuery())
        		.append("),CC.ID_MOEDA,?,?,ECC.DH_EVENTO,")
        		.append(totalValorSubquery())
        		.append(")")
        		.toString(),"TOTAL_VALOR");
        ociosidadeSql.addCriteriaValue(parameters.getLong("idPaisDestino"));
        ociosidadeSql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
        
        ociosidadeSql.addProjection("ECC.DH_EVENTO","DT_COTACAO");
      
  
        ociosidadeSql.addProjection("ECC.PS_REAL","PR");
        ociosidadeSql.addProjection("ECC.PS_AFORADO","PA");
        ociosidadeSql.addProjection("ECC.PC_OCUPACAO_CALCULADO","OCUPACAO_REAL");
        ociosidadeSql.addProjection("ECC.PC_OCUPACAO_AFORADO_CALCULADO","OCUPACAO_AFORADA");
        ociosidadeSql.addProjection("ECC.PC_OCUPACAO_INFORMADO","OCUPACAO_VISUAL");
        
        ociosidadeSql.addProjection(new StringBuilder()
        		.append("F_CONV_MOEDA((")
        		.append(this.getPaisOrigemSubQuery())
        		.append("),CC.ID_MOEDA,?,?,ECC.DH_EVENTO,ECC.VL_TOTAL)")
        		.toString(),"VALOR");
        ociosidadeSql.addCriteriaValue(parameters.getLong("idPaisDestino"));
        ociosidadeSql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
        

        StringBuffer sqlFrom = new StringBuffer()
	        .append("CONTROLE_CARGA CC ")
	        .append("INNER JOIN CONTROLE_TRECHO CT ON CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
	        .append("INNER JOIN ROTA_IDA_VOLTA RIV ON CC.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA ")
	        .append("INNER JOIN ROTA R ON RIV.ID_ROTA = R.ID_ROTA ")
	        .append("INNER JOIN ROTA_VIAGEM RV ON RIV.ID_ROTA_VIAGEM = RV.ID_ROTA_VIAGEM ")
	        .append("INNER JOIN FILIAL FO ON CT.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
	        .append("INNER JOIN FILIAL FD ON CT.ID_FILIAL_DESTINO = FD.ID_FILIAL ")
	        .append("INNER JOIN FILIAL FCC ON CC.ID_FILIAL_ORIGEM = FCC.ID_FILIAL ")
        	.append("INNER JOIN EVENTO_CONTROLE_CARGA ECC ON ECC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND ECC.ID_FILIAL = CT.ID_FILIAL_ORIGEM ")
	        .append("LEFT JOIN TIPO_MEIO_TRANSPORTE TIPO_MT ON RV.ID_TIPO_MEIO_TRANSPORTE = TIPO_MT.ID_TIPO_MEIO_TRANSPORTE ");
	        
        ociosidadeSql.addFrom(sqlFrom.toString());
  
        // subquery
        ociosidadeSql.addCustomCriteria("(SELECT MAX(DH_EVENTO) " +
        		"FROM EVENTO_CONTROLE_CARGA ECC2 " +
				"WHERE ECC2.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA " +
				"AND ECC2.ID_FILIAL = CC.ID_FILIAL_DESTINO " +
				"AND ECC2.TP_EVENTO_CONTROLE_CARGA = 'FD') "+
				"BETWEEN ? AND ? "); 
            
        ociosidadeSql.addCriteriaValue(dtVigenciaInicial);
        ociosidadeSql.addCriteriaValue(dtVigenciaFinal);

        ociosidadeSql.addFilterSummary("periodoEmissaoCcInicial", JTFormatUtils.format(dtVigenciaInicial,JTFormatUtils.MEDIUM,JTFormatUtils.YEARMONTHDAY));
        ociosidadeSql.addFilterSummary("periodoEmissaoCcFinal", JTFormatUtils.format(dtVigenciaFinal,JTFormatUtils.MEDIUM,JTFormatUtils.YEARMONTHDAY));

        ociosidadeSql.addCustomCriteria("CC.TP_CONTROLE_CARGA = 'V'");
        ociosidadeSql.addCustomCriteria("CC.TP_STATUS_CONTROLE_CARGA = 'FE'");
        ociosidadeSql.addCustomCriteria("CT.BL_TRECHO_DIRETO = 'S'");
        ociosidadeSql.addCustomCriteria("ECC.TP_EVENTO_CONTROLE_CARGA = 'EM'");

		if (idFilialOrigem != null) {
			ociosidadeSql.addCustomCriteria("EXISTS (SELECT 1 " +
					"		 FROM FILIAL_ROTA FR" +
					"		 WHERE FR.ID_ROTA = R.ID_ROTA" +
					"		 AND FR.ID_FILIAL = ?" +
					"		 AND FR.BL_ORIGEM_ROTA = 'S')");
			ociosidadeSql.addCriteriaValue(idFilialOrigem);
			ociosidadeSql.addFilterSummary("filialOrigem", parameters.getString("filialOrigem.sgFilial"));
		}

		if (idFilialDestino != null) {
			ociosidadeSql.addCustomCriteria("EXISTS (SELECT 1 " +
					"		 FROM FILIAL_ROTA FR" +
					"		 WHERE FR.ID_ROTA = R.ID_ROTA" +
					"		 AND FR.ID_FILIAL = ?" +
					"		 AND FR.BL_DESTINO_ROTA = 'S')");
			ociosidadeSql.addCriteriaValue(idFilialDestino);
			ociosidadeSql.addFilterSummary("filialDestino", parameters.getString("filialDestino.sgFilial"));
		}

		if (idFilialIntermediaria != null) {
			ociosidadeSql.addCustomCriteria("EXISTS (SELECT 1 " +
					"		 FROM FILIAL_ROTA FR" +
					"		 WHERE FR.ID_ROTA = R.ID_ROTA" +
					"		 AND FR.ID_FILIAL = ?)");
			ociosidadeSql.addCriteriaValue(idFilialIntermediaria);
			ociosidadeSql.addFilterSummary("filialIntermediaria", parameters.getString("filialIntermediaria.sgFilial"));										    
		}

		if (idRotaIdaVolta != null) {
			ociosidadeSql.addCustomCriteria("RIV.ID_ROTA_IDA_VOLTA = ?");
			ociosidadeSql.addCriteriaValue(idRotaIdaVolta);
			ociosidadeSql.addFilterSummary("rota", dsRota);
		}
		
        if (StringUtils.isNotBlank(tpRota)) {
			ociosidadeSql.addCustomCriteria("TP_ROTA = ?");	
			ociosidadeSql.addCriteriaValue(tpRota);
		    
       		Domain dmTpRotaViagem = domainService.findByName("DM_TIPO_ROTA_VIAGEM");
       		String tipo = dmTpRotaViagem.findDomainValueByValue(tpRota).getDescription().getValue(); 
       		ociosidadeSql.addFilterSummary("tipoRota", tipo);
		}

		ociosidadeSql.addOrderBy("DS_ROTA");
		ociosidadeSql.addOrderBy("HR_SAIDA");
		ociosidadeSql.addOrderBy("NR_CONTROLE_CARGA"); 
		ociosidadeSql.addOrderBy("TRECHO_ORIGEM");
		ociosidadeSql.addOrderBy("TRECHO_DESTINO"); 
        
		
		ociosidadeSql.addFilterSummary("converterParaMoeda", parameters.getString("moeda.siglaSimbolo"));
        
        return ociosidadeSql;
    }
    
    private String getPaisOrigemSubQuery() {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("P2.ID_PAIS");
		sql.addFrom(new StringBuilder()
				.append("PESSOA P2")
				.append(" INNER JOIN ENDERECO_PESSOA EP2 ON EP2.ID_ENDERECO_PESSOA = P2.ID_ENDERECO_PESSOA")
				.append(" INNER JOIN MUNICIPIO M2 ON M2.ID_MUNICIPIO = EP2.ID_MUNICIPIO")
				.append(" INNER JOIN UNIDADE_FEDERATIVA UF2 ON UF2.ID_UNIDADE_FEDERATIVA = M2.ID_UNIDADE_FEDERATIVA")
				.append(" INNER JOIN PAIS P2 ON P2.ID_PAIS = UF2.ID_PAIS").toString());
		sql.addCustomCriteria("P2.ID_PESSOA = FO.ID_FILIAL");
				
		return sql.getSql();
	}

	private String totalPesoRealSubquery() {
    	
    	SqlTemplate sql = createSqlTemplate();
    	sql.addProjection("NVL(SUM(M.PS_TOTAL_MANIFESTO),0)");
    	sql.addFrom("MANIFESTO", "M");
    	sql.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
    	
    	return ( " (" + sql.getSql() + ") " );
    }
    
    private String totalPesoAforadoSubquery() {
    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("NVL(SUM(M.PS_TOTAL_AFORADO_MANIFESTO),0)");
    	sql.addFrom("MANIFESTO", "M");
    	sql.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
    	
    	return ( " (" + sql.getSql() + ") " );
    }
    
    private String totalValorSubquery() {
    	
    	SqlTemplate sql = createSqlTemplate();
    	sql.addProjection("NVL(SUM(M.VL_TOTAL_MANIFESTO),0)");
    	sql.addFrom("MANIFESTO", "M");
    	sql.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
    	
    	return ( " (" + sql.getSql() + ") " );
    }

    
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("DS_TP_ROTA", "DM_TIPO_ROTA_VIAGEM"); 
    } 

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
}
