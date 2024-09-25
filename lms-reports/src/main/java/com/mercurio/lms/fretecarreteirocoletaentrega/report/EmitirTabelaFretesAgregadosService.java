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
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirTabelaFretesAgregadosService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirTabelaFretesAgregados.jasper"
 */
public class EmitirTabelaFretesAgregadosService extends ReportServiceSupport { 
	private EnderecoPessoaService enderecoPessoaService;
	private MoedaPaisService moedaPaisService;
	
	/* Ádamo
	 * É executado para chamar a pesquisa montaSql logo abaixo e em seguida chamar o relatorio do jasper passando o 
	 * resultado da pesquisa.
	 * */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		SqlTemplate sql = montaSql(tfm);
		
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
         
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        jr.setParameters(parametersReport);
		return jr;
	}
	 
	/* Ádamo
	 * Este método é responsavel por buscar os dados, filtrar, para emissao de tabela fretes agregados
	 * evento 25.04.02.02 Emiti tabela de fretes
	 * */
	private SqlTemplate montaSql(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		// Projection
		sql.addProjection("ttce.ID_TIPO_TABELA_COLETA_ENTREGA","ID_TIPO_TABELA_COLETA_ENTREGA");
		sql.addProjection("ttce.ds_tipo_tabela_coleta_entrega", "ds_tipo_tabela_coleta_entrega");
		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("f.sg_filial");
		sql.addProjection("p.NM_FANTASIA");
		sql.addProjection("tmt.ds_tipo_meio_transporte", "ds_tipo_meio_transporte");
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
		sql.addProjection("m.ds_simbolo","DS_SIMBOLO");
		
		// from 
		sql.addFrom("tabela_coleta_entrega","tce");
		sql.addFrom("tipo_tabela_coleta_entrega","ttce");
		sql.addFrom("filial","f");
		sql.addFrom("tipo_meio_transporte","tmt");
		sql.addFrom("pessoa","p");
		sql.addFrom("MOEDA_PAIS","mp");
		sql.addFrom("MOEDA","m");
		// where Join
	    sql.addJoin("TTCE.ID_TIPO_TABELA_COLETA_ENTREGA"," tce.id_tipo_tabela_coleta_entrega(+)");
	    sql.addJoin("F.ID_FILIAL","tce.id_filial");
	    sql.addJoin("P.ID_PESSOA","f.id_filial");
	    sql.addJoin("TMT.ID_TIPO_MEIO_TRANSPORTE","tce.id_tipo_meio_transporte");
	    sql.addJoin("M.ID_MOEDA","MP.ID_MOEDA");
	    sql.addJoin("TCE.ID_MOEDA_PAIS","MP.ID_MOEDA_PAIS");

	    if (parameters.getLong("regional.id") != null) {
			sql.addFrom("REGIONAL_FILIAL","RF");
			sql.addFrom("REGIONAL","R");
	   	    sql.addJoin("F.ID_FILIAL","RF.id_filial");
		    sql.addJoin("R.ID_REGIONAL","RF.ID_REGIONAL");
		    
		    sql.addCustomCriteria("RF.ID_REGIONAL_FILIAL = (SELECT MAX(RF2.ID_REGIONAL_FILIAL) FROM REGIONAL_FILIAL RF2 WHERE RF2.ID_FILIAL = F.ID_FILIAL AND RF2.ID_REGIONAL = ? AND ((RF2.DT_VIGENCIA_INICIAL <= ? AND RF2.DT_VIGENCIA_FINAL >= ?) OR (RF2.DT_VIGENCIA_INICIAL >= ? AND RF2.DT_VIGENCIA_INICIAL <= ?)))");
		    sql.addCriteriaValue(parameters.getLong("regional.id"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));
		    sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaInicial"));

		    if (parameters.getYearMonthDay("dtVigenciaFinal") != null)
		    	sql.addCriteriaValue(parameters.getYearMonthDay("dtVigenciaFinal"));
		    else
		    	sql.addCriteriaValue(JTDateTimeUtils.MAX_YEARMONTHDAY);

		    sql.addCriteria("R.ID_REGIONAL", "=", parameters.getLong("regional.id"));
		    sql.addFilterSummary("regional",parameters.getString("regional.desc"));
	    }
	    
	    // Filial
	    if (parameters.getLong("filial.idFilial") != null) {
		    sql.addCriteria("F.ID_FILIAL", "=", parameters.getLong("filial.idFilial"));
		    sql.addFilterSummary("filial",new StringBuffer(parameters.getString("filial.sgFilial")).append(" - ").append(parameters.getString("filial.pessoa.nmFantasia")).toString());
	    }
	    // Relatorio de agregados
	    sql.addCriteria("TCE.TP_REGISTRO","=","A");
	    
	    // Somente tabelas cuja situação esta como aprovada
	    sql.addCriteria("TCE.TP_SITUACAO_APROVACAO", "=", "A");
	    
	    // Tipo meio Transporte
	    sql.addCriteria("TMT.ID_TIPO_MEIO_TRANSPORTE", "=", parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
	    sql.addFilterSummary("tipoMeioTransporte",parameters.getString("tipoMeioTransporte.dsTipoMeioTransporte"));
	    
	    // Tipo de tabela
	    sql.addCriteria("TTCE.ID_TIPO_TABELA_COLETA_ENTREGA", "=", parameters.getLong("tipoTabela.idTipoTabela"));
	    sql.addFilterSummary("tipoTabela",parameters.getString("tipoTabela.dsTipoTabelaColetaEntrega"));
	    
		// Vigencia
	    sql.addCriteria("TCE.DT_VIGENCIA_INICIAL", ">=", parameters.getYearMonthDay("dtVigenciaInicial"));
	    sql.addFilterSummary("vigenciaInicial",parameters.getYearMonthDay("dtVigenciaInicial"));

	    sql.addCriteria("TCE.DT_VIGENCIA_FINAL", "<=", parameters.getYearMonthDay("dtVigenciaFinal"));
	    sql.addFilterSummary("vigenciaFinal",parameters.getYearMonthDay("dtVigenciaFinal"));
	    
   	  	// order by
	    sql.addOrderBy("f.sg_filial");
	    sql.addOrderBy("ttce.ds_tipo_tabela_coleta_entrega");
	    sql.addOrderBy("tmt.ds_tipo_meio_transporte");

	    logger.warn(sql.getSql());
		return sql; 
	}	



	/* Ádamo
	 * Métodos Get Set da classe.
	 * */
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
