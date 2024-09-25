package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.FreteCarreteiroFacade;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author 
 *
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaNotaCreditoService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirExtratoFrotaNotaCredito.jasper"
 */
public class EmitirExtratoFrotaNotaCreditoService extends ReportServiceSupport {

	private FreteCarreteiroFacade freteCarreteiroFacade;
	private EmitirExtratoFrotaReciboService emitirExtratoFrotaReciboService;

	public EmitirExtratoFrotaReciboService getEmitirExtratoFrotaReciboService() {
		return emitirExtratoFrotaReciboService;
	}

	public void setEmitirExtratoFrotaReciboService(
			EmitirExtratoFrotaReciboService emitirExtratoFrotaReciboService) {
		this.emitirExtratoFrotaReciboService = emitirExtratoFrotaReciboService;
	}

	public FreteCarreteiroFacade getFreteCarreteiroFacade() {
		return freteCarreteiroFacade;
	}

	public void setFreteCarreteiroFacade(FreteCarreteiroFacade freteCarreteiroFacade) {
		this.freteCarreteiroFacade = freteCarreteiroFacade;
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
        
        parametersReport.put("SERVICE_RECIBO", emitirExtratoFrotaReciboService);
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        JRReportDataObject jr = null;  
        try{
	        jr = executeQuery(sql.getSql(), sql.getCriteria());
	        jr.setParameters(parametersReport);
        }catch(UncategorizedSQLException e){
        	throw new InfrastructureException(e.getCause());
        }
        
        return jr; 
	}

	private SqlTemplate montaSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection(new StringBuilder()
        		.append("F_CONV_MOEDA(")
        		.append("MPNC.ID_PAIS,MPNC.ID_MOEDA,?,?,NC.DH_EMISSAO,")
        		.append("(")
        		.append(this.getValorMercadoriaNotaCredito())
        		.append(")")
        		.append(")")
        		.toString(),"PERC_VLMERC_NC");
		sql.addCriteriaValue(parameters.getLong("idPaisDestino"));
		sql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
		
		sql.addProjection(new StringBuilder()
        		.append("F_CONV_MOEDA(")
        		.append("MPNC.ID_PAIS,MPNC.ID_MOEDA,?,?,NC.DH_EMISSAO,")
        		.append("(")
        		.append(this.getValorFreteNotaCredito())
        		.append(")")
        		.append(")")
        		.toString(),"PERC_VLFRETE_NC");
		sql.addCriteriaValue(parameters.getLong("idPaisDestino"));
		sql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
		
		sql.addProjection(new StringBuilder()
        		.append("F_CONV_MOEDA(")
        		.append("MPNC.ID_PAIS,CC.ID_MOEDA,?,?,")
        		.append("(")
        		.append(this.getDataEmissaoControleCarga())
        		.append("),")
        		.append("CC.VL_PEDAGIO)")
        		.toString(),"VL_PEDAGIO_CONV");
		sql.addCriteriaValue(parameters.getLong("idPaisDestino"));
		sql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
				
		sql.addProjection("NC.ID_NOTA_CREDITO");
		sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("FILCC.SG_FILIAL || ' - '||PESCC.NM_FANTASIA","FILIAL_CONTROLE_CARGA");
		sql.addProjection("FILCC.ID_FILIAL","ID_FILIAL_CC");
		sql.addProjection("NVL2(MTCC.NR_FROTA,MTCC.NR_FROTA || ' - ' ||MTCC.NR_IDENTIFICADOR,'')","IDENTIFICACAO_MT");
		sql.addProjection("MTCC.ID_MEIO_TRANSPORTE");
		sql.addProjection("PESPROP.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PROP");
		sql.addProjection("PESPROP.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP");
		sql.addProjection("PESPROP.NM_PESSOA","NM_PESSOA_PROP");
		sql.addProjection("CC.ID_PROPRIETARIO");
				
		sql.addProjection("FILNC.SG_FILIAL","SG_FILIAL_NC");
		sql.addProjection("NC.NR_NOTA_CREDITO");
		sql.addProjection("NC.DH_EMISSAO","DH_EMISSAO_NC");
		
		
		// Qtde diarias
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC  ")
		.append("  WHERE NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
		.append("    AND NC1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO  ")
		.append("    AND PTC.TP_PARCELA = 'DH' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_DIARIAS_NC ");
		
		// Qtde eventos
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC  ")
		.append("  WHERE NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
		.append("    AND NC1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO  ")
		.append("    AND PTC.TP_PARCELA = 'EV' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_EVENTOS_NC ");
		
		// Qtde coletas
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_COLETA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC  ")
		.append("  WHERE NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
		.append("    AND NC1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO  ")
		.append("    AND PTC.TP_PARCELA = 'EV' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_COLETAS_NC ");
		
		// Qtde entregas
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_ENTREGA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC  ")
		.append("  WHERE NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
		.append("    AND NC1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO  ")
		.append("    AND PTC.TP_PARCELA = 'EV' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_ENTREGAS_NC ");
		
		// Qtde km excedente
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC  ")
		.append("  WHERE NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
		.append("    AND NC1.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO  ")
		.append("    AND PTC.TP_PARCELA = 'QU' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_KMEXC_NC ");
		
		// Qtde franquia de peso
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("   FROM NOTA_CREDITO NC1,  ")
		.append("        NOTA_CREDITO_PARCELA NCP,  ")
		.append("        PARCELA_TABELA_CE PTC,  ")
		.append("        TABELA_COLETA_ENTREGA TCE ")
		.append("   WHERE NC.ID_NOTA_CREDITO = NC1.ID_NOTA_CREDITO ")
		.append("     AND NC1.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO ")
		.append("     AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("     AND PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA  ")
		.append("     AND PTC.TP_PARCELA = 'FP' ")
		.append("     AND TCE.TP_CALCULO = 'C1' ")
		.append("    AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("  GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_FRANQUIAPESO_NC ");
		
		// Qtde franquia de peso documento
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("    FROM NOTA_CREDITO NC1,  ")
		.append("         NOTA_CREDITO_PARCELA NCP,  ")
		.append("         PARCELA_TABELA_CE PTC,  ")
		.append("         TABELA_COLETA_ENTREGA TCE, ")
		.append("         FAIXA_PESO_PARC_TAB_CE FPPTC ")
		.append("   WHERE NC.ID_NOTA_CREDITO = NC1.ID_NOTA_CREDITO ")
		.append("     AND NC1.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO ")
		.append("     AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("     AND PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA  ")
		.append("     AND NCP.ID_FAIXA_PESO_PARC_TAB_CE = FPPTC.ID_FAIXA_PESO_PARC_TAB_CE ")
		.append("     AND PTC.TP_PARCELA='FP' ")
		.append("     AND TCE.TP_CALCULO = 'C2' ")
		.append("     AND (FPPTC.TP_FATOR = 'Documento' OR NCP.QT_NOTA_CREDITO_PARCELA = 1) ")
		.append("   GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(")  ")
		.append(" , 0) " )
		.toString()," QT_FRANQUIAPESO_DOCUMENTO_NC ");
		
		// Qtde franquia de peso kg
		sql.addProjection(new StringBuilder()
		.append(" NVL( " )
		.append("( SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) ")
		.append("    FROM NOTA_CREDITO NC1,  ")
		.append("         NOTA_CREDITO_PARCELA NCP,  ")
		.append("         PARCELA_TABELA_CE PTC,  ")
		.append("         TABELA_COLETA_ENTREGA TCE, ")
		.append("         FAIXA_PESO_PARC_TAB_CE FPPTC ")
		.append("   WHERE NC.ID_NOTA_CREDITO = NC1.ID_NOTA_CREDITO ")
		.append("     AND NC1.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO ")
		.append("     AND NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ")
		.append("     AND PTC.ID_TABELA_COLETA_ENTREGA = TCE.ID_TABELA_COLETA_ENTREGA  ")
		.append("     AND NCP.ID_FAIXA_PESO_PARC_TAB_CE = FPPTC.ID_FAIXA_PESO_PARC_TAB_CE ")
		.append("     AND PTC.TP_PARCELA='FP' ")
		.append("     AND TCE.TP_CALCULO = 'C2' ")
		.append("     AND (FPPTC.TP_FATOR = 'KG' OR NCP.QT_NOTA_CREDITO_PARCELA <> 1) ")
		.append("   GROUP BY NC1.ID_NOTA_CREDITO, NC1.DH_EMISSAO  ")
		.append(") ")
		.append(" , 0) " )
		.toString()," QT_FRANQUIAPESO_KG_NC ");
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(" ( ");
		sb.append(this.getDataEmissaoControleCarga());
		sb.append(") ");
		sb.append(" AS DH_EMISSAO_CC ");
		sql.addProjection(sb.toString());
		
		sql.addProjection("CC.ID_MOEDA", "ID_MOEDA_ORIGEM_CC");
		
		sql.addProjection("CC.NR_CONTROLE_CARGA");
		sql.addProjection("FILCC.SG_FILIAL","SG_FILIAL_CC");
		sql.addProjection("CC.DH_SAIDA_COLETA_ENTREGA");
		sql.addProjection("CC.DH_CHEGADA_COLETA_ENTREGA");
		sql.addProjection(new StringBuilder()
				.append("TO_SECONDS(")
				.append("CAST (Trunc(CC.DH_CHEGADA_COLETA_ENTREGA,'Mi') AS TIMESTAMP)")
				.append(" - ")
				.append("CAST (Trunc(CC.DH_SAIDA_COLETA_ENTREGA,'Mi') AS TIMESTAMP)")
				.append(") as TOTAL_SEGUNDOS")
				.toString());
		sql.addProjection("CC.VL_PEDAGIO");
		sql.addProjection("MPNC.ID_MOEDA", "ID_MOEDA_ORIGEM_NC");
		sql.addProjection("MPNC.ID_PAIS", "ID_PAIS_ORIGEM_NC");
		
		sql.addProjection("(SELECT MAX(CQ.NR_QUILOMETRAGEM) FROM CONTROLE_QUILOMETRAGEM CQ WHERE CQ.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CQ.BL_SAIDA='S')AS KM_INICIAL_CC");
		sql.addProjection("(SELECT MAX(CQ.NR_QUILOMETRAGEM) FROM CONTROLE_QUILOMETRAGEM CQ WHERE CQ.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CQ.BL_SAIDA='N')AS KM_FINAL_CC");
		
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("NOTA_CREDITO","NC");
		
		
		sql.addFrom("MOEDA_PAIS", "MPNC");
		
		sql.addFrom("FILIAL","FILCC");
		sql.addFrom("PESSOA","PESCC");
		sql.addFrom("MEIO_TRANSPORTE","MTCC");
		sql.addFrom("PROPRIETARIO","PROPCC");
		sql.addFrom("PESSOA","PESPROP");
		sql.addFrom("FILIAL","FILNC");
		
		sql.addJoin("CC.ID_NOTA_CREDITO","NC.ID_NOTA_CREDITO");
		sql.addJoin("NC.ID_MOEDA_PAIS","MPNC.ID_MOEDA_PAIS");
		sql.addJoin("CC.ID_FILIAL_ORIGEM","FILCC.ID_FILIAL");
		sql.addJoin("FILCC.ID_FILIAL","PESCC.ID_PESSOA");
		sql.addJoin("CC.ID_TRANSPORTADO","MTCC.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("CC.ID_PROPRIETARIO","PROPCC.ID_PROPRIETARIO(+)");
		sql.addJoin("PROPCC.ID_PROPRIETARIO","PESPROP.ID_PESSOA(+)");
		
		sql.addJoin("NC.ID_FILIAL","FILNC.ID_FILIAL(+)");
		
		sql.addCriteria("CC.TP_CONTROLE_CARGA", "=", "C");
		
		sql.addCriteria("TRUNC(cast(NC.DH_EMISSAO as DATE))", ">=", parameters.getYearMonthDay("periodoGeracaoInicial"));
		sql.addCriteria("TRUNC(cast(NC.DH_EMISSAO as DATE))", "<=", parameters.getYearMonthDay("periodoGeracaoFinal"));
		
		sql.addCriteria("CC.ID_FILIAL_ORIGEM", "=", parameters.getLong("filial.idFilial"));
		if(parameters.getLong("filial.idFilial")!= null)
			sql.addFilterSummary("filial",parameters.getString("filialSigla")+" - "+parameters.getString("filial.pessoa.nmFantasia"));
		
		sql.addFilterSummary("tipoSelecao",parameters.getString("dsSelecao"));
		
		 sql.addCriteria("CC.ID_PROPRIETARIO", "=",parameters.getLong("proprietario.idProprietario"));
		 if(parameters.getLong("proprietario.idProprietario")!=null)
			 sql.addFilterSummary("proprietario",parameters.getString("proprietarioNrIdentificacao")+" - "+parameters.getString("proprietario.pessoa.nmPessoa"));
		 
		sql.addCriteria("CC.ID_TRANSPORTADO", "=", parameters.getLong("meioTransporteRodoviario.idMeioTransporte"));
		if(parameters.getLong("meioTransporteRodoviario.idMeioTransporte")!=null)
			 sql.addFilterSummary("meioTransporte",parameters.getString("meioTransporteNrFrota")+" - "+parameters.getString("identificacaoMeioTransporte"));
		
		if(parameters.getString("descricaoMoeda")!="")
			sql.addFilterSummary("converterParaMoeda",parameters.getString("descricaoMoeda"));
		sql.addFilterSummary("periodoEmissaoInicial", JTFormatUtils.format(parameters.getYearMonthDay("periodoGeracaoInicial")));
		sql.addFilterSummary("periodoEmissaoFinal", JTFormatUtils.format(parameters.getYearMonthDay("periodoGeracaoFinal")));
		
		
		
		sql.addOrderBy("NC.DH_EMISSAO");
		sql.addOrderBy("FILCC.SG_FILIAL");
		sql.addOrderBy("MTCC.NR_FROTA");
		sql.addOrderBy("NC.NR_NOTA_CREDITO");
		
			
		return sql;
	}
	
	 public static String formataIdentificacao(String tpIdentificacao, String conteudo){
			return FormatUtils.formatIdentificacao(tpIdentificacao,conteudo);
			
	 }
	 
	public JRDataSource executeManifestosEntrega(Long idControleCarga)throws Exception{
		 SqlTemplate sql = new SqlTemplate();
		 sql.addProjection("ME.ID_MANIFESTO_ENTREGA");
		 sql.addProjection("ME.NR_MANIFESTO_ENTREGA");
		 sql.addProjection("MA.DH_EMISSAO_MANIFESTO","DH_EMISSAO");
		 sql.addProjection("ME.DH_FECHAMENTO");
		 sql.addProjection("MA.PS_TOTAL_MANIFESTO");
		 sql.addProjection("MA.PS_TOTAL_AFORADO_MANIFESTO");
		 sql.addProjection("MA.VL_TOTAL_MANIFESTO_EMISSAO");
		 sql.addProjection("MA.VL_TOTAL_FRETE_EMISSAO");
		 sql.addProjection("FILME.SG_FILIAL");
		 
		 sql.addFrom("CONTROLE_CARGA","CC");
		 sql.addFrom("MANIFESTO","MA");
		 sql.addFrom("MANIFESTO_ENTREGA","ME");
		 sql.addFrom("FILIAL","FILME");
		 
		 sql.addJoin("CC.ID_CONTROLE_CARGA","MA.ID_CONTROLE_CARGA");
		 sql.addJoin("MA.ID_MANIFESTO","ME.ID_MANIFESTO_ENTREGA");
		 sql.addJoin("ME.ID_FILIAL","FILME.ID_FILIAL");
		 
		 sql.addCriteria("CC.ID_CONTROLE_CARGA", "=", idControleCarga);
		 
		 		 
		 return executeQuery(sql.getSql(),sql.getCriteria()).getDataSource();
	 }
	
	
	public JRDataSource executeManifestosColeta(Long idControleCarga)throws Exception{
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("MC.ID_MANIFESTO_COLETA");
		sql.addProjection("MC.NR_MANIFESTO");
		sql.addProjection("MC.DH_EMISSAO");
		sql.addProjection("FILMC.SG_FILIAL");
		sql.addProjection("NVL(IV.PS_TOTAL_MANIFESTO,0)  PS_TOTAL_MANIFESTO ");
		sql.addProjection("NVL(IV.PS_TOTAL_AFORADO_MANIFESTO,0) PS_TOTAL_AFORADO_MANIFESTO ");
		sql.addProjection("NVL(IV.VL_TOTAL_MANIFESTO_EMISSAO,0) VL_TOTAL_MANIFESTO_EMISSAO ");
		
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("MANIFESTO_COLETA","MC");
		sql.addFrom("FILIAL","FILMC");
	
		StringBuilder sb = new StringBuilder();
		sb.append(" ( SELECT MC.ID_CONTROLE_CARGA ID_CONTROLE_CARGA , ")
		.append("          SUM(PC.PS_TOTAL_VERIFICADO) PS_TOTAL_MANIFESTO , ")
		.append("          SUM(PC.PS_TOTAL_AFORADO_VERIFICADO) PS_TOTAL_AFORADO_MANIFESTO, ")
		.append("          SUM(PC.VL_TOTAL_VERIFICADO) VL_TOTAL_MANIFESTO_EMISSAO ")
		.append("     FROM PEDIDO_COLETA PC, ")
		.append("          MANIFESTO_COLETA MC ")
		.append("    WHERE MC.ID_MANIFESTO_COLETA = PC.ID_MANIFESTO_COLETA")
		.append("      AND PC.TP_STATUS_COLETA IN ('FI','NT','EX')")
		.append("    GROUP BY MC.ID_CONTROLE_CARGA ) ");
		
		sql.addFrom(sb.toString(),"IV");
	
		sql.addJoin("CC.ID_CONTROLE_CARGA","MC.ID_CONTROLE_CARGA");
		sql.addJoin("MC.ID_FILIAL_ORIGEM","FILMC.ID_FILIAL");
		sql.addJoin("CC.ID_CONTROLE_CARGA","IV.ID_CONTROLE_CARGA(+)");
		
		sql.addCriteria("CC.ID_CONTROLE_CARGA","=", idControleCarga);
	
		

		return executeQuery(sql.getSql(),sql.getCriteria()).getDataSource();
	}
	
	 
	 private String getValorMercadoriaNotaCredito() {
			SqlTemplate sql = new SqlTemplate();
						
		sql.addProjection(" SUM(NCP.VL_NOTA_CREDITO_PARCELA) ");
		sql.addFrom(new StringBuilder().append("NOTA_CREDITO_PARCELA NCP, ")
				.append("PARCELA_TABELA_CE PTC ").toString());
			
			sql.addCustomCriteria("PTC.TP_PARCELA='PV' ");
			sql.addCustomCriteria(" NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ");
			sql.addCustomCriteria(" NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ");
			
			return sql.getSql();
	}
	 
	 private String getValorFreteNotaCredito() {
			SqlTemplate sql = new SqlTemplate();
						
		sql.addProjection(" SUM(NCP.VL_NOTA_CREDITO_PARCELA) ");
		sql.addFrom(new StringBuilder().append("NOTA_CREDITO_PARCELA NCP, ")
				.append("PARCELA_TABELA_CE PTC ").toString());
			
			sql.addCustomCriteria("PTC.TP_PARCELA='PF' ");
			sql.addCustomCriteria(" NCP.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ");
			sql.addCustomCriteria(" NCP.ID_PARCELA_TABELA_CE = PTC.ID_PARCELA_TABELA_CE ");
			
			return sql.getSql();
	} 
	 
	private String getDataEmissaoControleCarga() {
			SqlTemplate sql = new SqlTemplate();
						
		sql.addProjection("MAX(ECC.DH_EVENTO)");
		sql.addFrom(new StringBuilder().append("EVENTO_CONTROLE_CARGA ECC ")
					.toString());
			
			sql.addCustomCriteria("ECC.ID_CONTROLE_CARGA=CC.ID_CONTROLE_CARGA ");
			sql.addCustomCriteria(" ECC.TP_EVENTO_CONTROLE_CARGA='EM' ");
			
			return sql.getSql();
  }  
	 
	 public String retornaKgmPercorridaCC(Long idControleCarga){
		 String kgmPercorrida =null;
		 Long km = freteCarreteiroFacade.findQuilometragemPercorridaControleCarga(idControleCarga);
		 if(km != null)
			 kgmPercorrida = FormatUtils.formatDecimal(String.valueOf("#,##0"),km);
		 return kgmPercorrida;
	 }
	 
	 public Long retornaQtdeEntregaControleCarga(Long idControleCarga){
		 return freteCarreteiroFacade.findQuantidadeEntregasEfetuadasControleCarga(idControleCarga);
	 }
	 
	 public Long retornaQtdeColetasControleCarga(Long idControleCarga){
		 return freteCarreteiroFacade.findQuantidadeColetasEfetuadasControleCarga(idControleCarga);
	 }
	 
	/**
	 * Consulta Quilometragem inicial do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer findQuiloMetragemInicialByIdControleCarga(Long idControleCarga) {
		return freteCarreteiroFacade.findQuiloMetragemInicialByIdControleCarga(idControleCarga);
	 }

	/**
	 * Consulta Quilometragem Final do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer findQuiloMetragemFinalByIdControleCarga(Long idControleCarga) {
		return freteCarreteiroFacade.findQuiloMetragemFinalByIdControleCarga(idControleCarga);
	}

 }
