package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.util.HashMap;
import java.util.Map;

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

import net.sf.jasperreports.engine.JRDataSource;
/**
 * @author 
 *
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaControleCargaService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirExtratoFrotaControleCarga.jasper"
 */
public class EmitirExtratoFrotaControleCargaService extends ReportServiceSupport {
 
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
        		.append("PAISCC.ID_PAIS,CC.ID_MOEDA,?,?,ECC.DH_EVENTO,")
        		.append("CC.VL_PEDAGIO)")
        		.toString(),"VL_PEDAGIO_CONV");
		sql.addCriteriaValue(parameters.getLong("idPaisDestino"));
		sql.addCriteriaValue(parameters.getLong("idMoedaDestino"));
		
		sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("FILCC.SG_FILIAL || ' - '||PESCC.NM_FANTASIA","FILIAL_CONTROLE_CARGA");
		sql.addProjection("FILCC.ID_FILIAL","ID_FILIAL_CC");
		sql.addProjection("NVL2(MTCC.NR_FROTA,MTCC.NR_FROTA || ' - ' ||MTCC.NR_IDENTIFICADOR,'')" ,"IDENTIFICACAO_MT");
		sql.addProjection("MTCC.ID_MEIO_TRANSPORTE");
		sql.addProjection("PESPROP.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PROP");
		sql.addProjection("PESPROP.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP");
		sql.addProjection("PESPROP.NM_PESSOA","NM_PESSOA_PROP");
		sql.addProjection("CC.ID_PROPRIETARIO");
		
		sql.addProjection("ECC.DH_EVENTO","DH_EMISSAO_CC");
		sql.addProjection("CC.ID_MOEDA", "ID_MOEDA_ORIGEM_CC");
		sql.addProjection("PAISCC.ID_PAIS", "ID_PAIS_ORIGEM_CC");
		
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
				
		sql.addProjection("(SELECT MAX(CQ.NR_QUILOMETRAGEM) FROM CONTROLE_QUILOMETRAGEM CQ WHERE CQ.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CQ.BL_SAIDA='S')AS KM_INICIAL_CC");
		sql.addProjection("(SELECT MAX(CQ.NR_QUILOMETRAGEM) FROM CONTROLE_QUILOMETRAGEM CQ WHERE CQ.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CQ.BL_SAIDA='N')AS KM_FINAL_CC");
		
		
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("EVENTO_CONTROLE_CARGA","ECC");
		
		// Somente controle de cargas que não possuam um evento de emissão cancelada
		// mais recente. (TP_EVENTO_CONTROLE_CARGA = 'EC')
		sql.addFrom(new StringBuilder()
		.append(" ( SELECT ECC.ID_EVENTO_CONTROLE_CARGA , ")
		.append("          ECC.TP_EVENTO_CONTROLE_CARGA , ")
		.append("          ROW_NUMBER() OVER(PARTITION BY ECC.ID_CONTROLE_CARGA ORDER BY ECC.DH_EVENTO DESC) RN ")
		.append("     FROM EVENTO_CONTROLE_CARGA ECC ")
		.append("    WHERE ECC.TP_EVENTO_CONTROLE_CARGA IN ('EM', 'EC') ")
		.append("      AND TRUNC(cast(ECC.DH_EVENTO as DATE)) BETWEEN ? AND ? ")
		.append("  )  ")
		.toString()," EV ");
		
		sql.addCriteriaValue(parameters.getYearMonthDay("periodoGeracaoInicial"));
		sql.addCriteriaValue(parameters.getYearMonthDay("periodoGeracaoFinal"));
		/////////////////////////////////////////////////////////////////////////
		
		sql.addFrom("FILIAL","FILCC");
		sql.addFrom("PESSOA","PESCC");
		
		sql.addFrom("ENDERECO_PESSOA","ENDPESCC");
		sql.addFrom("MUNICIPIO","MUNCC");
		sql.addFrom("UNIDADE_FEDERATIVA","UFCC");
		sql.addFrom("PAIS","PAISCC");
		
		sql.addFrom("MEIO_TRANSPORTE","MTCC");
		sql.addFrom("PROPRIETARIO","PROPCC");
		sql.addFrom("PESSOA","PESPROP");
		
		sql.addJoin("ECC.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA");
		sql.addJoin("CC.ID_FILIAL_ORIGEM","FILCC.ID_FILIAL");
		sql.addJoin("FILCC.ID_FILIAL","PESCC.ID_PESSOA");
		sql.addJoin("PESCC.ID_ENDERECO_PESSOA","ENDPESCC.ID_ENDERECO_PESSOA(+)");
		sql.addJoin("ENDPESCC.ID_MUNICIPIO","MUNCC.ID_MUNICIPIO(+)");
		sql.addJoin("MUNCC.ID_UNIDADE_FEDERATIVA","UFCC.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("UFCC.ID_PAIS","PAISCC.ID_PAIS(+)");
		
		sql.addJoin("CC.ID_TRANSPORTADO","MTCC.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("CC.ID_PROPRIETARIO","PROPCC.ID_PROPRIETARIO(+)");
		sql.addJoin("PROPCC.ID_PROPRIETARIO","PESPROP.ID_PESSOA(+)");
		
		// Somente controle de cargas que não possuam um evento de emissão cancelada
		// mais recente. (TP_EVENTO_CONTROLE_CARGA = 'EC')
		sql.addJoin("ECC.ID_EVENTO_CONTROLE_CARGA","EV.ID_EVENTO_CONTROLE_CARGA");
		//////////////////////////////////////////////////////////////
		
		sql.addCriteria("CC.TP_CONTROLE_CARGA", "=", "C");
		sql.addCriteria("ECC.TP_EVENTO_CONTROLE_CARGA","=","EM");
		
		sql.addCriteria("TRUNC(cast(ECC.DH_EVENTO as DATE))", ">=", parameters.getYearMonthDay("periodoGeracaoInicial"));
		sql.addCriteria("TRUNC(cast(ECC.DH_EVENTO as DATE))", "<=", parameters.getYearMonthDay("periodoGeracaoFinal"));
		
		// Somente controle de cargas que não possuam um evento de emissão cancelada
		// mais recente. (TP_EVENTO_CONTROLE_CARGA = 'EC')
		sql.addCriteria("EV.RN","=",1);
		sql.addCriteria("EV.TP_EVENTO_CONTROLE_CARGA","=","EM");
		//////////////////////////////////////////////////////////////
		
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
		
		
		
		sql.addOrderBy("ECC.DH_EVENTO");
		sql.addOrderBy("FILCC.SG_FILIAL");
		sql.addOrderBy("MTCC.NR_FROTA");
		sql.addOrderBy("CC.NR_CONTROLE_CARGA");
		
			
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
	 * Consulta a quantidade de entregas efetuadas
	 * 
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer findQuantidadeEntregasEfetuadasByIdManifestoEntrega(Long idManifestoEntrega) {
		return freteCarreteiroFacade.findQuantidadeEntregasEfetuadasByIdManifestoEntrega(idManifestoEntrega);
	 }

	/**
	 * Consulta a quantidade de coletas efetuadas
	 * 
	 * @param idManifestoColeta
	 * @return
	 */
	public Integer findQuantidadeColetasEfetuadasByIdManifestoEntrega(Long idManifestoColeta) {
		return emitirExtratoFrotaReciboService.findQuantidadeColetasEfetuadasByIdManifestoColeta(idManifestoColeta);
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
