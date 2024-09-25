package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas 
 *
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirTabelaFretesEventuaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirTabelaFretesEventuais.jasper"
 */											
public class EmitirTabelaFretesEventuaisService extends
		ReportServiceSupport {
	/* Injetado pelo Spring */
	private EnderecoPessoaService enderecoPessoaService;
	private MoedaPaisService moedaPaisService;
	
	/* Ádamo
	 * É executado para chamar a pesquisa montaSql logo abaixo e em seguida chamar o relatorio do jasper passando o 
	 * resultado da pesquisa.
	 * */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		SqlTemplate sql = montaSql(tfm); 

        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
    	        
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        jr.setParameters(parametersReport);
		
		return jr;

	}
	/* Ádamo
	 * Este método é responsavel por buscar os dados, filtrar, para emissao de tabela fretes eventuais
	 * evento 25.04.02.02 Emiti tabela de fretes
	 * */
	private SqlTemplate montaSql(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		// Projection
		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("f.sg_filial");
		sql.addProjection("p.NM_PESSOA","NM_PROPRIETARIO");
		sql.addProjection("pf.NM_FANTASIA");
		sql.addProjection("p.TP_IDENTIFICACAO","TP_IDENTIFICACAO");
		sql.addProjection("p.NR_IDENTIFICACAO","NR_IDENTIFICACAO");

		sql.addProjection("tce.dt_vigencia_inicial","DT_VIGENCIA_INICIAL");
		sql.addProjection("tce.dt_vigencia_final","DT_VIGENCIA_FINAL");
 
		sql.addProjection("NVL((select vl_definido "+
		            "from parcela_tabela_ce TCE2 "+
		            "where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA "+
		            	"and tp_parcela = 'DH'),0)","DIARIA");
		sql.addProjection("NVL((select vl_definido "+
		            "from parcela_tabela_ce TCE2 "+
		            "where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA "+
		            	"and tp_parcela = 'EV'),0)","EVENTO");
		sql.addProjection("NVL((select vl_definido "+
		            "from parcela_tabela_ce TCE2 "+
		            "where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA "+
		            	"and tp_parcela = 'QU'),0)","KMEXCEDENTE");
		sql.addProjection("NVL((select vl_definido "+
	            "from parcela_tabela_ce TCE2 "+
	            "where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA "+
	            	"and tp_parcela = 'FP'),0)","FRACAOPESO");
		sql.addProjection("NVL((select vl_definido from parcela_tabela_ce TCE2 where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA and tp_parcela = 'PF'),0)","VALORFRETE");
		sql.addProjection("NVL((select vl_definido from parcela_tabela_ce TCE2 where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA and tp_parcela = 'PV'),0)","VALORMERCADORIA");

         
		sql.addProjection("(select pc_sobre_valor from parcela_tabela_ce TCE2 where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA and tp_parcela = 'PF')","VALORFRETEPER");
		sql.addProjection("(select pc_sobre_valor from parcela_tabela_ce TCE2 where TCE2.id_tabela_coleta_entrega = tce.id_TABELA_COLETA_ENTREGA and tp_parcela = 'PV')","VALORMERCADPER");
		sql.addProjection("mt.NR_IDENTIFICADOR","NR_IDENTIFICADOR");
		sql.addProjection("m.ds_simbolo","DS_SIMBOLO");
		sql.addProjection("MT.NR_FROTA","NR_FROTA");
		 
 
		 
 
		// from
		sql.addFrom("tabela_coleta_entrega","tce");
		sql.addFrom("filial","f");
		sql.addFrom("pessoa","p");
		sql.addFrom("pessoa","pf");
		sql.addFrom("MEIO_TRANSP_PROPRIETARIO","MTP");
		sql.addFrom("PROPRIETARIO","PO");
		sql.addFrom("MEIO_TRANSPORTE","MT");
		sql.addFrom("MOEDA_PAIS","mp");
		sql.addFrom("MOEDA","m");
		 
		// where Join
	    sql.addJoin("F.ID_FILIAL","tce.id_filial");
	    sql.addJoin("MT.ID_MEIO_TRANSPORTE","TCE.ID_MEIO_TRANSPORTE");
	    sql.addJoin("pf.ID_PESSOA","F.ID_FILIAL");
	    sql.addJoin("P.ID_PESSOA","PO.id_PROPRIETARIO");
	    sql.addJoin("MTP.ID_PROPRIETARIO","PO.ID_PROPRIETARIO");
	    sql.addJoin("MTP.ID_MEIO_TRANSPORTE","MT.ID_MEIO_TRANSPORTE");
	    sql.addJoin("M.ID_MOEDA","MP.ID_MOEDA");
	    sql.addJoin("TCE.ID_MOEDA_PAIS","MP.ID_MOEDA_PAIS");
	    
	    

	    //* Criterios da tela de pesquisa *//
	   sql.addCustomCriteria("MTP.ID_MEIO_TRANSP_PROPRIETARIO = (SELECT MAX(MTP2.ID_MEIO_TRANSP_PROPRIETARIO) FROM MEIO_TRANSP_PROPRIETARIO MTP2 WHERE MTP2.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE AND ((MTP2.DT_VIGENCIA_INICIAL <= TCE.DT_VIGENCIA_INICIAL AND MTP2.DT_VIGENCIA_FINAL >= TCE.DT_VIGENCIA_INICIAL) OR (MTP2.DT_VIGENCIA_INICIAL >= TCE.DT_VIGENCIA_INICIAL AND MTP2.DT_VIGENCIA_INICIAL <= TCE.DT_VIGENCIA_FINAL)))");
	    
	    // Regional 
	    if (parameters.getLong("regional.id") != null) {
	    	sql.addFrom("REGIONAL_FILIAL","RF");
	    	sql.addCriteria("RF.ID_REGIONAL", "=", parameters.getLong("regional.id"));
		    sql.addFilterSummary("regional",parameters.getString("regional.desc"));
		    
		    sql.addCustomCriteria("RF.ID_REGIONAL_FILIAL = (SELECT MAX(RF2.ID_REGIONAL_FILIAL) FROM REGIONAL_FILIAL RF2 WHERE RF2.ID_FILIAL = F.ID_FILIAL AND RF2.ID_REGIONAL = ? AND ((RF2.DT_VIGENCIA_INICIAL <= ? AND RF2.DT_VIGENCIA_FINAL >= ?) OR (RF2.DT_VIGENCIA_INICIAL >= ? AND RF2.DT_VIGENCIA_INICIAL <= ?)))");
		    sql.addCriteriaValue(parameters.getLong("regional.id"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));

		    if (parameters.getYearMonthDay("dtVigenciaFinal") != null)
		    	sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaFinal"));
		    else
		    	sql.addCriteriaValue(JTDateTimeUtils.MAX_YEARMONTHDAY);
		    
		    sql.addJoin("F.ID_FILIAL","RF.id_filial");
		    
	    }
		
	    
	    // Filial
	    if (parameters.getLong("filial.idFilial") != null) {
		    sql.addCriteria("F.ID_FILIAL", "=", parameters.getLong("filial.idFilial"));
		    sql.addFilterSummary("filial",new StringBuffer(parameters.getString("filial.sgFilial")).append(" - ").append(parameters.getString("filial.pessoa.nmFantasia")).toString());
	    }

	    // Relatorio para eventuais
	    sql.addCriteria("TCE.TP_REGISTRO","=","E");
	    
	    // Somente tabelas cuja situação esta como aprovada
	    sql.addCriteria("TCE.TP_SITUACAO_APROVACAO", "=", "A");

		// Proprietario
	    sql.addCriteria("P.id_PESSOA", "=", parameters.getLong("proprietario.idProprietario")); 
	    if (parameters.getLong("proprietario.idProprietario") != null)
	    	sql.addFilterSummary("proprietario",parameters.getString("proprietario.nrIdentificacao")+" - "+parameters.getString("pessoa.nmPessoa")); 
		
		// Meio de transporte
		if (parameters.getLong("meioTransporte.idMeioTransporte") != null) {
			sql.addCriteria("MT.ID_MEIO_TRANSPORTE", "=", parameters.getLong("meioTransporte.idMeioTransporte"));
			sql.addFilterSummary("meioTransporte",new StringBuilder(parameters.getString("meioTransporte2.nrFrota")).append(" - ").append(parameters.getString("meioTransporte.nrIdentificador")).toString());
		}
 
		// Vigencia
	    sql.addCriteria("TCE.DT_VIGENCIA_INICIAL", ">=", parameters.getYearMonthDay("dtVigenciaInicial"));
	    sql.addFilterSummary("vigenciaInicial",parameters.getYearMonthDay("dtVigenciaInicial"));

	    sql.addCriteria("TCE.DT_VIGENCIA_FINAL", "<=", parameters.getYearMonthDay("dtVigenciaFinal"));
	    sql.addFilterSummary("vigenciaFinal",parameters.getYearMonthDay("dtVigenciaFinal"));
	    
	    sql.addOrderBy("f.sg_filial");

	    logger.warn(sql.getSql());
		return sql; 
	}	
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public MoedaPaisService getMoedaPaisService() {
		return moedaPaisService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
    
}
