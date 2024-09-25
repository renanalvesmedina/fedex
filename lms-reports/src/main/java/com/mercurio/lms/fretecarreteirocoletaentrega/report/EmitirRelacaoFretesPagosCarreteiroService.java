package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirRelacaoFretesPagosCarreteiroService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirFretesPagosCarreteiro.jasper"
 */
public class EmitirRelacaoFretesPagosCarreteiroService extends ReportServiceSupport {
	private ConversaoMoedaService conversaoMoedaService;
	
	public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TP_SITUACAO_RECIBO","DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE");
    	super.configReportDomains(config);
    }
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap parameters = (TypedFlatMap)criteria;
			
		SqlTemplate sql = montaSqlTemplate(parameters);  
                
        Map parametersReport = new HashMap();
        if(StringUtils.isNotBlank(parameters.getString("idMoedaDestino"))){
        	parametersReport.put("idMoedaPaisDestino", parameters.getLong("idMoedaDestino"));
            parametersReport.put("idPaisDestino", parameters.getLong("idPaisDestino"));
            parametersReport.put("moeda","("+parameters.getString("dsSimbolo")+")");
            
        }
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        
        
        JRReportDataObject jr = null;
        try{
        	jr= executeQuery(sql.getSql(), sql.getCriteria());
        	jr.setParameters(parametersReport);
        }catch(UncategorizedSQLException e){
        	throw new InfrastructureException(e.getCause());
        }
        return jr;
        
	}	
	
	public SqlTemplate montaSqlTemplate(TypedFlatMap criteria){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("FIL.ID_FILIAL");
		sql.addProjection("PROP.ID_PROPRIETARIO");
		sql.addProjection("RFC.DT_PAGTO_REAL");
		sql.addProjection("RFC.VL_LIQUIDO");
		sql.addProjection("RFC.VL_INSS");
		sql.addProjection("RFC.VL_IRRF");
		sql.addProjection("RFC.VL_ISSQN");
		sql.addProjection("RFC.VL_POSTO_PASSAGEM");
		sql.addProjection("RFC.VL_BRUTO");
		sql.addProjection("RFC.DH_EMISSAO");
		sql.addProjection("RFC.TP_SITUACAO_RECIBO");
		sql.addProjection("RFC.NR_RECIBO_FRETE_CARRETEIRO");
		sql.addProjection("FIL.SG_FILIAL || ' - ' ||PESFIL.NM_FANTASIA","DESC_FILIAL_RECIBO");
		sql.addProjection("FIL.SG_FILIAL ","SG_FILIAL_RECIBO");
		sql.addProjection("CB.NR_CONTA_BANCARIA");
		sql.addProjection("CB.DV_CONTA_BANCARIA");
		sql.addProjection("AB.NR_AGENCIA_BANCARIA");
		sql.addProjection("BA.NR_BANCO");
		sql.addProjection("PESBEN.NM_PESSOA","NM_PESSOA_BEN");
		sql.addProjection("PESBEN.NR_IDENTIFICACAO","NR_IDENTIFICACAO_BEN");
		sql.addProjection("PESBEN.TP_IDENTIFICACAO","TP_IDENTIFICACAO_BEN");
		sql.addProjection("PESPROP.NM_PESSOA","NM_PESSOA_PROP");
		sql.addProjection("PESPROP.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PROP");
		sql.addProjection("PESPROP.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP");
		sql.addProjection("MO.ID_MOEDA","ID_MOEDA_ORIGEM");
		sql.addProjection("MP.ID_PAIS","ID_PAIS_ORIGEM");
		
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_BRUTO", "VL_BRUTO_CONVERTIDO");
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_LIQUIDO", "VL_LIQUIDO_CONVERTIDO");
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_POSTO_PASSAGEM", "VL_POSTO_PASSAGEM_CONVERTIDO");
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_INSS", "VL_INSS_CONVERTIDO");
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_ISSQN", "VL_ISSQN_CONVERTIDO");
		addProjectionConversaoMoeda(sql, criteria, "RFC.VL_IRRF", "VL_IRRF_CONVERTIDO");
		
		sql.addFrom("RECIBO_FRETE_CARRETEIRO","RFC");
		sql.addFrom("PROPRIETARIO","PROP");
		sql.addFrom("FILIAL","FIL");
		sql.addFrom("PESSOA","PESFIL");
		sql.addFrom("CONTA_BANCARIA","CB");
		sql.addFrom("AGENCIA_BANCARIA","AB");
		sql.addFrom("BANCO","BA");
		sql.addFrom("PESSOA","PESPROP");
		sql.addFrom("PESSOA","PESBEN");
		sql.addFrom("MOEDA_PAIS", "MP");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("(SELECT MAX(ADIANTAMENTO_TRECHO.ID_POSTO_CONVENIADO) ID_POSTO_CONVENIADO, ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO ID_RECIBO_FRETE_CARRETEIRO FROM ADIANTAMENTO_TRECHO GROUP BY ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO)", "POSTO_C");
		
				
		sql.addJoin("RFC.ID_MOEDA_PAIS","MP.ID_MOEDA_PAIS");
		sql.addJoin("MP.ID_MOEDA","MO.ID_MOEDA");
		sql.addJoin("RFC.ID_PROPRIETARIO","PROP.ID_PROPRIETARIO");
		sql.addJoin("PROP.ID_PROPRIETARIO","PESPROP.ID_PESSOA");
		sql.addJoin("RFC.ID_FILIAL","FIL.ID_FILIAL");
		
		
		sql.addJoin("FIL.ID_FILIAL","PESFIL.ID_PESSOA");
		
		
		sql.addJoin("RFC.ID_RECIBO_FRETE_CARRETEIRO", "POSTO_C.ID_RECIBO_FRETE_CARRETEIRO (+)");
		
		sql.addJoin("nvl(POSTO_C.ID_POSTO_CONVENIADO, RFC.ID_PROPRIETARIO)","PESBEN.ID_PESSOA");
		
		sql.addJoin("RFC.ID_CONTA_BANCARIA","CB.ID_CONTA_BANCARIA(+)");
		sql.addJoin("CB.ID_AGENCIA_BANCARIA","AB.ID_AGENCIA_BANCARIA(+)");
		sql.addJoin("AB.ID_BANCO","BA.ID_BANCO(+)");
		
		sql.addCriteria("RFC.TP_RECIBO_FRETE_CARRETEIRO","=", "C");
		//Regra 2.2
		sql.addCriteria("RFC.TP_SITUACAO_RECIBO","<>", "CA");
		
		sql.addCriteria("RFC.ID_FILIAL","=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("RFC.ID_PROPRIETARIO","=", criteria.getLong("proprietario.idProprietario"));
		sql.addCriteria("RFC.ID_MEIO_TRANSPORTE","=", criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
		sql.addCriteria("RFC.TP_SITUACAO_RECIBO","=", criteria.getString("tpSituacaoRecibo"));
		
		if(criteria.getYearMonthDay("periodoEmissaoInicial")!= null)
			sql.addCriteria("RFC.DH_EMISSAO",">=",criteria.getYearMonthDay("periodoEmissaoInicial").toDateTimeAtMidnight());
		
		if(criteria.getYearMonthDay("periodoEmissaoFinal")!= null)
			sql.addCriteria("RFC.DH_EMISSAO","<",criteria.getYearMonthDay("periodoEmissaoFinal").toDateTimeAtMidnight().plusDays(1));
		
		sql.addCriteria("RFC.DT_PAGTO_REAL",">=",criteria.getYearMonthDay("periodoPagamentoInicial"));
		sql.addCriteria("RFC.DT_PAGTO_REAL","<=",criteria.getYearMonthDay("periodoPagamentoFinal"));
		
		
		if(criteria.getLong("filial.idFilial")!= null)
			sql.addFilterSummary("filial",criteria.getString("filialSigla")+" - "+criteria.getString("filial.pessoa.nmFantasia"));
		
		if(criteria.getLong("proprietario.idProprietario")!=null)
			 sql.addFilterSummary("proprietario",criteria.getString("proprietarioNrIdentificacao")+" - "+criteria.getString("proprietario.pessoa.nmPessoa"));
		 
		if(criteria.getLong("meioTransporteRodoviario.idMeioTransporte")!=null)
			 sql.addFilterSummary("meioTransporte",criteria.getString("meioTransporteNrFrota")+" - "+criteria.getString("identificacaoMeioTransporte"));
		
		if(criteria.getString("descricaoTpRecibo")!=null)
			 sql.addFilterSummary("situacaoRecibo",criteria.getString("descricaoTpRecibo"));
		
		if(StringUtils.isNotBlank(criteria.getString("periodoEmissaoInicial")))
				sql.addFilterSummary("periodoEmissaoInicial",JTFormatUtils.format(criteria.getYearMonthDay("periodoEmissaoInicial")));
		
		if(StringUtils.isNotBlank(criteria.getString("periodoEmissaoFinal")))
			    sql.addFilterSummary("periodoEmissaoFinal",JTFormatUtils.format(criteria.getYearMonthDay("periodoEmissaoFinal")));
		
		if(StringUtils.isNotBlank(criteria.getString("periodoPagamentoInicial")))
			sql.addFilterSummary("periodoPagamentoInicial",JTFormatUtils.format(criteria.getYearMonthDay("periodoPagamentoInicial")));
	
		if(StringUtils.isNotBlank(criteria.getString("periodoPagamentoFinal")))
		    sql.addFilterSummary("periodoPagamentoFinal",JTFormatUtils.format(criteria.getYearMonthDay("periodoPagamentoFinal")));
		
		if(criteria.getString("descricaoMoeda")!="")
			sql.addFilterSummary("converterParaMoeda",criteria.getString("descricaoMoeda"));
		
		sql.addOrderBy("FIL.SG_FILIAL");
		sql.addOrderBy("PESPROP.NM_PESSOA");
		sql.addOrderBy("RFC.NR_RECIBO_FRETE_CARRETEIRO");
				
		return sql;
		
	}
	
	 public static String formataIdentificacao(String tpIdentificacao, String conteudo){
			return FormatUtils.formatIdentificacao(tpIdentificacao,conteudo);
	 }
	 
	 public BigDecimal findConversaoMoeda(Long idMoedaPaisOrigem, Long idPaisOrigem, Long idMoedaPaisDestino, Long idPaisDestino, BigDecimal valor, String strDhCotacao ){
		 YearMonthDay dtCotacao = null;
		 if(idMoedaPaisDestino != null && idPaisDestino == null){
			 Pais p = SessionUtils.getPaisSessao();
			 idPaisDestino = p.getIdPais();
		 }
		 if(idMoedaPaisDestino == null && idPaisDestino == null){
			 idMoedaPaisDestino = idMoedaPaisOrigem;
			 idPaisDestino = idPaisOrigem;
		 }
		 if(StringUtils.isNotBlank(strDhCotacao)) {
			  dtCotacao = JTFormatUtils.buildDateTimeFromTimestampTzString(strDhCotacao).toYearMonthDay();
		 } else {
			 return BigDecimal.valueOf(0);
		 }
		 
		 return getConversaoMoedaService().findConversaoMoeda(idPaisOrigem,idMoedaPaisOrigem, idPaisDestino, idMoedaPaisDestino,dtCotacao, valor);
	}

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}


	
	public void addProjectionConversaoMoeda(SqlTemplate sql,TypedFlatMap parameters, String campo,String alias){
		String projection = "F_CONV_MOEDA("+
		"MP.ID_PAIS, " +
		"MO.ID_MOEDA, " +
		parameters.getString("idPaisDestino") + ", " +
		parameters.getString("idMoedaDestino") + ", " +
		"RFC.DH_EMISSAO, " +
		campo+ ")";
		
		sql.addProjection(projection, alias);
		
		
	}
}
