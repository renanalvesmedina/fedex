package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.fretecarreteiroviagem.emitirRelacaoFretesPagosCarreteiroService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirFretesPagosCarreteiro.jasper"
 */
public class EmitirRelacaoFretesPagosCarreteiroService extends ReportServiceSupport {
	
	private ConversaoMoedaService conversaoMoedaService;
	 
	 public void configReportDomains(ReportDomainConfig config) {
	    	config.configDomainField("TP_SITUACAO_RECIBO","DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE");
	    	super.configReportDomains(config);
	 }

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap)parameters;
		
		SqlTemplate sql = createSqlTemplate(map);
		
		Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        
        if(StringUtils.isNotBlank(map.getString("idMoedaDestino"))){
        	parametersReport.put("idMoedaPaisDestino", map.getLong("idMoedaDestino"));
            parametersReport.put("idPaisDestino", map.getLong("idPaisDestino"));	
            parametersReport.put("moeda","("+map.getString("dsSimbolo")+")");
          
            
            parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        }
        
        JRReportDataObject jr = null;
        
        try{
			jr = executeQuery(sql.getSql(), sql.getCriteria());
			jr.setParameters(parametersReport);
        }catch(UncategorizedSQLException e){
        	throw new InfrastructureException(e.getCause());
        }
		
		return jr;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		
		if(org.apache.commons.lang.StringUtils.isBlank(criteria.getString("proprietario.idProprietario")) && org.apache.commons.lang.StringUtils.isBlank(criteria.getString("tpReciboFreteCarreteiro")))
			throw new BusinessException("LMS-24020");
		
		SqlTemplate sql = createSqlTemplate();
		
		//************** PARAMETROS OBRIGATORIOS DA TELA *************************
		
		YearMonthDay dataInicio = criteria.getYearMonthDay("periodoEmissaoInicial");
		YearMonthDay dataFim = criteria.getYearMonthDay("periodoEmissaoFinal");
		Long idFilial = criteria.getLong("filial.idFilial");
				
		
		//*************************** PROJEÇÃO **********************************
		
		sql.addProjection("FIL.SG_FILIAL || ' - ' || PES.NM_FANTASIA", "IDENTIFICACAO_FILIAL");
		
		sql.addProjection("FIL.ID_FILIAL");
		
		sql.addProjection("RFCCOMP.ID_RECIBO_FRETE_CARRETEIRO","ID_RECIBO_COMPL");
		
		sql.addProjection("PES_PROP.NR_IDENTIFICACAO","IDENTIFICACAO_PROP");
		
		sql.addProjection("PROP.ID_PROPRIETARIO");
		
		sql.addProjection("PES_PROP.NM_PESSOA","NM_PROP");
		
		sql.addProjection("PES_PROP.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP");
		
		sql.addProjection("NVL(PES_BEN.NR_IDENTIFICACAO, '')","IDENTIFICACAO_BEN");
		
		sql.addProjection("NVL(PES_BEN.NM_PESSOA, '')","NM_BEN");
		
		sql.addProjection("NVL(PES_BEN.TP_IDENTIFICACAO, '')","TP_IDENTIFICACAO_BEN");
		
		sql.addProjection("BANCO.NR_BANCO");
		
		sql.addProjection("NVL2(AB.NR_AGENCIA_BANCARIA, AB.NR_AGENCIA_BANCARIA || '-' || AB.NR_DIGITO,'' )","IDENTIFICACAO_AGENCIA");
		
		sql.addProjection("NVL2(CB.NR_CONTA_BANCARIA, CB.NR_CONTA_BANCARIA || '-' || CB.DV_CONTA_BANCARIA,'')", "IDENTIFICACAO_CONTA");
		
		sql.addProjection("RFC.NR_RECIBO_FRETE_CARRETEIRO","IDENTIFICACAO_RECIBO");
		
		sql.addProjection("FIL.SG_FILIAL","FILIAL_RECIBO");
		
		sql.addProjection("RFC.DH_EMISSAO");
		
		sql.addProjection("RFC.ID_CONTA_BANCARIA");
		
		
		
		sql.addProjection("RFC.TP_SITUACAO_RECIBO");
		
		sql.addProjection("RFC.DT_PAGTO_REAL");
		
		sql.addProjection("NVL(CC.NR_CONTROLE_CARGA , '')", "IDENTIFICACAO_CONTROLE");
		
		sql.addProjection("NVL(FILCONTROLE.SG_FILIAL, '')", "FILIAL_CONTROLE");
		
		sql.addProjection("FILCONTDESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
		
		sql.addProjection("RFC.VL_BRUTO");
		
		sql.addProjection("RFC.VL_LIQUIDO","VL_LIQUIDO_COMPL");
		
		sql.addProjection("RFC.VL_IRRF");
		
		sql.addProjection("RFC.VL_INSS");
		
		sql.addProjection("RFC.VL_ISSQN");
		
		sql.addProjection("RFC.VL_PREMIO");
		
		sql.addProjection("RFC.VL_DIARIA");
		
		sql.addProjection("RFC.VL_POSTO_PASSAGEM");
		
		sql.addProjection("RFC.ID_RELACAO_PAGAMENTO");
		
		sql.addProjection("RP.NR_RELACAO_PAGAMENTO");
		
		
		sql.addProjection("RFC.VL_DESCONTO");
		
		sql.addProjection("(SELECT FIL.SG_FILIAL FROM " +
				"FILIAL FIL, HISTORICO_FILIAL HIST " +
				"WHERE " +
				"FIL.ID_FILIAL = HIST.ID_FILIAL AND " +
				"HIST.TP_FILIAL= 'MA' AND " +
				"FIL.ID_EMPRESA = (SELECT FIL.ID_EMPRESA FROM FILIAL FIL WHERE FIL.ID_FILIAL = ?))","SG_FILIAL_RP");
		
		String sqlSaldo = "nvl((SELECT SUM(RFCSALDO.VL_LIQUIDO) FROM RECIBO_FRETE_CARRETEIRO RFCSALDO WHERE " +
				"RFCSALDO.BL_ADIANTAMENTO = 'S' AND " +
				"RFCSALDO.ID_PROPRIETARIO = RFC.ID_PROPRIETARIO AND " +
		        "RFCSALDO.TP_SITUACAO_RECIBO <> 'CA' AND " +
				"RFCSALDO.ID_CONTROLE_CARGA = RFC.ID_CONTROLE_CARGA " +
				"AND RFCSALDO.DT_PAGTO_REAL IS NULL),0) + NVL2(RFC.DT_PAGTO_REAL, 0,RFC.VL_LIQUIDO)";
		sql.addProjection(sqlSaldo, "SALDO");
		
		String sqlVlLiquido = "RFC.VL_LIQUIDO + nvl((SELECT SUM(RFCSALDO.VL_liquido) FROM RECIBO_FRETE_CARRETEIRO RFCSALDO WHERE " +
				"RFCSALDO.BL_ADIANTAMENTO = 'S' AND " +
				"RFCSALDO.ID_PROPRIETARIO = RFC.ID_PROPRIETARIO AND " +
		        "RFCSALDO.TP_SITUACAO_RECIBO <> 'CA' AND " +
				"RFCSALDO.ID_CONTROLE_CARGA = RFC.ID_CONTROLE_CARGA ),0)";
		sql.addProjection(sqlVlLiquido, "VL_LIQUIDO");
	
		
		sql.addProjection("PROP.ID_PROPRIETARIO");
		sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("M.ID_MOEDA", "ID_MOEDA_PAIS_ORIGEM");
		sql.addProjection("MP.ID_PAIS", "ID_PAIS_ORIGEM");
		
		addConvMoedaProjection(sql, criteria, "RFC.VL_BRUTO", "VL_BRUTO_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_POSTO_PASSAGEM", "VL_POSTO_PASSAGEM_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_DESCONTO", "VL_DESCONTO_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_DIARIA", "VL_DIARIA_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_PREMIO", "VL_PREMIO_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_ISSQN", "VL_ISSQN_CONVERTIDO");
	    addConvMoedaProjection(sql, criteria, "RFC.VL_INSS", "VL_INSS_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_IRRF", "VL_IRRF_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, sqlSaldo, "SALDO_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, sqlVlLiquido, "VL_LIQUIDO_CONVERTIDO");
		addConvMoedaProjection(sql, criteria, "RFC.VL_LIQUIDO", "VL_LIQUIDO_COMPL_CONVERTIDO");
		
		//****************** FROM ***********************************
		
		sql.addFrom("RECIBO_FRETE_CARRETEIRO", "RFC");
		
		sql.addFrom("RECIBO_FRETE_CARRETEIRO", "RFCCOMP");
		
		sql.addFrom("FILIAL", "FIL");
		sql.addFrom("PESSOA", "PES");
		
		sql.addFrom("PROPRIETARIO", "PROP");
		sql.addFrom("PESSOA", "PES_PROP");
		
		sql.addFrom("PESSOA", "PES_BEN");
		
		sql.addFrom("CONTA_BANCARIA", "CB");
		sql.addFrom("BANCO", "BANCO");
		sql.addFrom("AGENCIA_BANCARIA", "AB");
		
		sql.addFrom("CONTROLE_CARGA", "CC");
		sql.addFrom("FILIAL", "FILCONTROLE");
		sql.addFrom("FILIAL", "FILCONTDESTINO");
		
		sql.addFrom("RELACAO_PAGAMENTO", "RP");
		
		sql.addFrom("MOEDA_PAIS", "MP");
		
		sql.addFrom("MOEDA", "M");
		
		sql.addFrom("(SELECT MAX(ADIANTAMENTO_TRECHO.ID_POSTO_CONVENIADO) ID_POSTO_CONVENIADO, ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO ID_RECIBO_FRETE_CARRETEIRO FROM ADIANTAMENTO_TRECHO GROUP BY ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO)", "POSTO_C");
		
		
		
		
		//******************************** JOINS *********************************
		
		sql.addJoin("RFC.ID_FILIAL","FIL.ID_FILIAL");
		sql.addJoin("RFC.ID_RECIBO_COMPLEMENTADO","RFCCOMP.ID_RECIBO_FRETE_CARRETEIRO(+)");
		sql.addJoin("FIL.ID_FILIAL","PES.ID_PESSOA");
		
		sql.addJoin("RFC.ID_PROPRIETARIO","PROP.ID_PROPRIETARIO");
		sql.addJoin("RFC.ID_PROPRIETARIO","PES_PROP.ID_PESSOA");
		
		sql.addJoin("RFC.ID_RECIBO_FRETE_CARRETEIRO", "POSTO_C.ID_RECIBO_FRETE_CARRETEIRO (+)");
		
		sql.addJoin("nvl(POSTO_C.ID_POSTO_CONVENIADO, RFC.ID_PROPRIETARIO)","PES_BEN.ID_PESSOA");
		
		sql.addJoin("RFC.ID_CONTA_BANCARIA","CB.ID_CONTA_BANCARIA(+)");
		sql.addJoin("CB.ID_AGENCIA_BANCARIA","AB.ID_AGENCIA_BANCARIA(+)");
		sql.addJoin("AB.ID_BANCO","BANCO.ID_BANCO(+)");
		
		sql.addJoin("RFC.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA(+)");
		sql.addJoin("CC.ID_FILIAL_ORIGEM","FILCONTROLE.ID_FILIAL");
		sql.addJoin("CC.ID_FILIAL_DESTINO","FILCONTDESTINO.ID_FILIAL(+)");
		
		sql.addJoin("RFC.ID_RELACAO_PAGAMENTO", "RP.ID_RELACAO_PAGAMENTO(+)");
		
		sql.addJoin("RFC.ID_MOEDA_PAIS", "MP.ID_MOEDA_PAIS(+)");
		
		sql.addJoin("MP.ID_MOEDA", "M.ID_MOEDA(+)");
		
				
		//******************** CRITERIAS ******************************
		
		//parametros para sub-selects da projecao
		sql.addCriteriaValue(idFilial);
		
				
		sql.addCriteria("RFC.TP_RECIBO_FRETE_CARRETEIRO", "=", "V");
		sql.addCriteria("RFC.BL_ADIANTAMENTO", "=", "N");
		sql.addCustomCriteria("RFC.ID_FILIAL = ? ",idFilial);
		sql.addFilterSummary("filial",criteria.getString("filial.sgFilial")+ " - " + criteria.getString("filial.pessoa.nmFantasia"));
		
		sql.addCriteria("RFC.DH_EMISSAO", ">=" , dataInicio.toDateTimeAtMidnight());
		sql.addCriteria("RFC.DH_EMISSAO" , "<", dataFim.toDateTimeAtMidnight().plusDays(1));
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(criteria.getString("proprietario.idProprietario"))){
			sql.addCustomCriteria("RFC.ID_PROPRIETARIO = ? ",criteria.getLong("proprietario.idProprietario"));
			sql.addCustomCriteria("PROP.TP_PROPRIETARIO = ? ", criteria.getString("tpCarreteiro"));
			sql.addFilterSummary("tipoCarreteiro", criteria.getString("descricaoTpCarreteiro"));
			sql.addFilterSummary("proprietario", criteria.getString("proprietario.pessoa.nrIdentificacao")+ " - " + criteria.getString("proprietario.pessoa.nmPessoa"));
		}else{
			if(!criteria.getString("tpCarreteiro").equals(""))
				sql.addCustomCriteria("PROP.TP_PROPRIETARIO = ? ", criteria.getString("tpCarreteiro"));
			else
				sql.addCustomCriteria("PROP.TP_PROPRIETARIO <> 'P' ");
		}
	 	
		if(org.apache.commons.lang.StringUtils.isNotBlank(criteria.getString("tpReciboFreteCarreteiro"))){
			sql.addCustomCriteria("RFC.TP_SITUACAO_RECIBO = ? ",criteria.getString("tpReciboFreteCarreteiro"));
			sql.addFilterSummary("situacaoRecibo",criteria.getString("descricaoTpRecibo"));
		}
				
		if(org.apache.commons.lang.StringUtils.isNotBlank(criteria.getString("descricaoMoeda")))
			sql.addFilterSummary("converterParaMoeda", criteria.getString("descricaoMoeda"));
		
		sql.addFilterSummary("periodoEmissaoInicial", JTFormatUtils.format(dataInicio));
		sql.addFilterSummary("periodoEmissaoFinal", JTFormatUtils.format(dataFim));
		
		sql.addOrderBy("FIL.SG_FILIAL");
		sql.addOrderBy("PES_PROP.NM_PESSOA");
		sql.addOrderBy("RFC.ID_PROPRIETARIO");
		sql.addOrderBy("RFC.ID_CONTA_BANCARIA");
		sql.addOrderBy("nvl(RFCCOMP.NR_RECIBO_FRETE_CARRETEIRO,RFC.NR_RECIBO_FRETE_CARRETEIRO)");
		sql.addOrderBy("RFC.NR_RECIBO_FRETE_CARRETEIRO");  
		
		return sql;
	}
	
	public JRDataSource executeValorAdiantamentoReciboFreteCarreteiro(Long idProprietario,Long idControleCarga) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("RFC2.VL_LIQUIDO");
		
		sql.addProjection("RFC2.DT_PAGTO_REAL");
		
		sql.addProjection("RFC2.ID_MOEDA_PAIS", "ID_MOEDA_PAIS_ORIGEM");
		
		sql.addProjection("MP.ID_PAIS", "ID_PAIS_ORIGEM");
		
		
		sql.addFrom("RECIBO_FRETE_CARRETEIRO","RFC2");
		sql.addFrom("MOEDA_PAIS", "MP");
		
		sql.addJoin("RFC2.ID_MOEDA_PAIS", "MP.ID_MOEDA_PAIS(+)");
		
		
		sql.addCriteria("RFC2.BL_ADIANTAMENTO","=", "S");
		sql.addCriteria("RFC2.TP_SITUACAO_RECIBO","<>","CA");
		sql.addCustomCriteria("RFC2.ID_CONTROLE_CARGA = ? ");
		sql.addCustomCriteria("RFC2.ID_PROPRIETARIO = ? ");
		
				
		sql.addCriteriaValue(idControleCarga);
		sql.addCriteriaValue(idProprietario);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();

	}
	
	public BigDecimal findConversaoMoeda(Long idMoedaPaisOrigem, Long idPaisOrigem, Long idMoedaPaisDestino, Long idPaisDestino, BigDecimal valor ){
		if(idMoedaPaisDestino != null && idPaisDestino == null){
			 Pais p = SessionUtils.getPaisSessao();
			 idPaisDestino = p.getIdPais();
		 }
		
		return getConversaoMoedaService().findConversaoMoeda(idPaisOrigem,idMoedaPaisOrigem, idPaisDestino, idMoedaPaisDestino, JTDateTimeUtils.getDataAtual(), valor);
	}

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	
	public static String formataIdentificacao(String tpIdentificacao, String conteudo){
		return FormatUtils.formatIdentificacao(tpIdentificacao,conteudo);
		
	}
	
	/**
	 * Inclui uma projection no sqlTemplate com a função de conversão de moeda 
	 * montada.
	 * <br>
	 * Permite que a exceção da falta de cotação da moeda para a data 
	 * especificada seja tratada de forma correta.
	 * 
	 * @author Vagner Huzalo
	 * 
	 * @param SqlTemplate sql
	 * @param TypedFlatMap parameters Parâmetros do relatório
	 * @param String projectionField Campo ou expressão a ser convertido 
	 * @param String alias Alias do campo ou expressão
	 */
	private void addConvMoedaProjection(SqlTemplate sql, TypedFlatMap parameters
			,String projectionField,String alias){
		StringBuilder strBuild = new StringBuilder();
		String moeda = parameters.getString("idMoedaDestino");
		String pais = parameters.getString("idPaisDestino");
		strBuild.append("F_CONV_MOEDA(")
			.append("MP.ID_PAIS,")	//Pais e Moeda de origem(documento)
			.append("M.ID_MOEDA,")
			.append(pais+",")		//Pais e moeda destido(parâmetros)
			.append(moeda+",")
			.append("to_date('" + JTDateTimeUtils.getDataAtual().toString("ddMMyyyy") + "', 'ddMMyyyy'),")
			.append(projectionField) 
			.append(")");
		sql.addProjection(strBuild.toString(), alias);
	}

}
