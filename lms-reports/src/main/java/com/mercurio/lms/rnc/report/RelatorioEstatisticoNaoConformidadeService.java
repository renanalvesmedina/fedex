package com.mercurio.lms.rnc.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**  
 *
 * @spring.bean id="lms.rnc.relatorioEstatisticoNaoConformidadeService"
 * @spring.property name="reportName" value="com/mercurio/lms/rnc/report/emitirRelatorioEstatisticoNaoConformidade.jasper"
 */
public class RelatorioEstatisticoNaoConformidadeService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap criteria = (TypedFlatMap) parameters; 
		SqlTemplate sql = getSqlTemplate(criteria);
		JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("documentoServico", this.getDocumentoServico(criteria));
		parametersReport.put("documento", this.getDocumento(criteria));
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,criteria.get("tpFormatoRelatorio"));
		jrReportDataObject.setParameters(parametersReport);

		return jrReportDataObject;
	}
	
	/**
	 * Monta a SQL do relatorio se baseando nos dados para filtro enviados pela tela
	 * 
	 * @param parameters parametros enviados pela view para montar os filtros na sql
	 * @return retorna um objeto SQLTemplate contendo a sql desejada
	 * @throws Exception
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		
        SqlTemplate sqlInterno = createSqlTemplate();
        SqlTemplate sqlExterno = createSqlTemplate();
      	
        sqlInterno.addProjection("ocorrenciaNaoConformidade.DH_INCLUSAO");
        sqlInterno.addProjection("filialResponsavel.SG_FILIAL sgFilialResp");
        sqlInterno.addProjection("filialResponsavel.ID_FILIAL idFilialResp");
        sqlInterno.addProjection("filialAbertura.SG_FILIAL sgFilialAbertura");
        
        sqlInterno.addProjection(PropertyVarcharI18nProjection.createProjection("motivoAberturaNaoConformidade.DS_MOTIVO_ABERTURA_I","dsMotivoAbertura"));
        
        sqlInterno.addProjection("controleCarga.TP_CONTROLE_CARGA tpControleCarga");
        sqlInterno.addProjection("controleCarga.ID_ROTA_COLETA_ENTREGA idRotaColetaEntrega");
        sqlInterno.addProjection("rotaColetaEntrega.DS_ROTA rotaColetaEntrega");
        sqlInterno.addProjection("filialOrigemTrecho.SG_FILIAL || '-' || filialDestinoTrecho.SG_FILIAL AS trechoViagem");
        sqlInterno.addProjection("filialOrigemTrecho.ID_FILIAL || '-' || filialDestinoTrecho.ID_FILIAL AS idsTrechoViagem");
   		sqlInterno.addProjection("CASE WHEN naoConformidade.TP_STATUS_NAO_CONFORMIDADE = 'RNC' THEN 1 ELSE 0 END AS quantidadeRNC");
 		sqlInterno.addProjection("CASE WHEN naoConformidade.TP_STATUS_NAO_CONFORMIDADE = 'ROI' THEN 1 ELSE 0 END AS quantidadeROI");
 		sqlInterno.addProjection("CASE WHEN naoConformidade.TP_STATUS_NAO_CONFORMIDADE = 'RRC' THEN 1 ELSE 0 END AS quantidadeRRC");
 		
 		sqlInterno.addFrom("OCORRENCIA_NAO_CONFORMIDADE ocorrenciaNaoConformidade");
 		sqlInterno.addFrom("FILIAL filialAbertura");
 		sqlInterno.addFrom("FILIAL filialResponsavel");
 		sqlInterno.addFrom("V_MOTIVO_ABERTURA_NC_I motivoAberturaNaoConformidade");
 		sqlInterno.addFrom("NAO_CONFORMIDADE naoConformidade");
 		sqlInterno.addFrom("CONTROLE_CARGA controleCarga");
 		
 		sqlInterno.addFrom("ROTA_IDA_VOLTA rotaIdaVolta");
 		sqlInterno.addFrom("ROTA rota");
 		
        sqlInterno.addFrom("ROTA_COLETA_ENTREGA rotaColetaEntrega");
        sqlInterno.addFrom("MANIFESTO manifesto");
        sqlInterno.addFrom("FILIAL filialOrigemTrecho");
        sqlInterno.addFrom("FILIAL filialDestinoTrecho");
        
        sqlInterno.addJoin("ocorrenciaNaoConformidade.ID_MOTIVO_ABERTURA_NC", "motivoAberturaNaoConformidade.ID_MOTIVO_ABERTURA_NC");
        sqlInterno.addJoin("ocorrenciaNaoConformidade.ID_FILIAL_ABERTURA", "filialAbertura.ID_FILIAL");
        sqlInterno.addJoin("ocorrenciaNaoConformidade.ID_FILIAL_RESPONSAVEL", "filialResponsavel.ID_FILIAL");
        sqlInterno.addJoin("naoConformidade.ID_NAO_CONFORMIDADE", "ocorrenciaNaoConformidade.ID_NAO_CONFORMIDADE");
        sqlInterno.addJoin("ocorrenciaNaoConformidade.ID_CONTROLE_CARGA", "controleCarga.ID_CONTROLE_CARGA (+)");
       	sqlInterno.addJoin("controleCarga.ID_ROTA_COLETA_ENTREGA", "rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA(+)");
       	
       	sqlInterno.addJoin("controleCarga.ID_ROTA_IDA_VOLTA", "rotaIdaVolta.ID_ROTA_IDA_VOLTA(+)");
       	sqlInterno.addJoin("rotaIdaVolta.ID_ROTA", "rota.ID_ROTA(+)");
       	
       	sqlInterno.addJoin("ocorrenciaNaoConformidade.ID_MANIFESTO", "manifesto.ID_MANIFESTO(+)");
       	sqlInterno.addJoin("manifesto.ID_FILIAL_ORIGEM", "filialOrigemTrecho.ID_FILIAL(+)");
       	sqlInterno.addJoin("manifesto.ID_FILIAL_DESTINO", "filialDestinoTrecho.ID_FILIAL(+)");

        YearMonthDay dataInicial = parameters.getYearMonthDay("dataInicial");
        if(dataInicial!=null) {
        	sqlInterno.addCriteria("TRUNC (CAST(ocorrenciaNaoConformidade.DH_INCLUSAO AS DATE))", ">=", dataInicial);
            sqlExterno.addFilterSummary("periodoInicial", dataInicial);
        }
        
        YearMonthDay dataFinal = parameters.getYearMonthDay("dataFinal");
        if(dataFinal!=null) {
        	sqlInterno.addCriteria("TRUNC (CAST(ocorrenciaNaoConformidade.DH_INCLUSAO AS DATE))", "<=", dataFinal);
            sqlExterno.addFilterSummary("periodoFinal", dataFinal);
        }
      
       	String idRota = parameters.getString("rota.idRota");
        if(StringUtils.isNotEmpty(idRota)) {
        	sqlInterno.addCriteria("rota.ID_ROTA","=", idRota ,Long.class);            
            String dsRota= parameters.getString("rota.dsRota");
            sqlExterno.addFilterSummary("rotaViagem", dsRota);
        }

        idRota = parameters.getString("rotaColetaEntrega.idRotaColetaEntrega");
        if(StringUtils.isNotEmpty(idRota)) {
        	sqlInterno.addCriteria("rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA","=", idRota, Long.class);            
            String dsRotaColetaEntrega = parameters.getString("rotaColetaEntrega.dsRota");
            sqlExterno.addFilterSummary("rotaColetaEntrega", dsRotaColetaEntrega);
        }
        
        String idFilial = parameters.getString("filialAbertura.idFilial");
        if(StringUtils.isNotEmpty(idFilial)) {
        	sqlInterno.addCriteria("ocorrenciaNaoConformidade.ID_FILIAL_ABERTURA","=", idFilial ,Long.class);            
            String sgFilial= parameters.getString("filialAbertura.pessoa.nmFantasia");
            sqlExterno.addFilterSummary("filialAbertura", sgFilial);
        }

        idFilial = parameters.getString("filialResponsavel.idFilial");
        if(StringUtils.isNotEmpty(idFilial)) {
        	sqlInterno.addCriteria("ocorrenciaNaoConformidade.ID_FILIAL_RESPONSAVEL","=", idFilial, Long.class);            
            String sgFilialResp= parameters.getString("filialResponsavel.pessoa.nmFantasia");
            sqlExterno.addFilterSummary("filialResponsavel", sgFilialResp);
        }
        
       	
        String idMotivoAbertura = parameters.getString("ocorrenciaNaoConformidade.motivoAberturaNc");
        if(StringUtils.isNotEmpty(idMotivoAbertura)) {
        	sqlInterno.addCriteria("ocorrenciaNaoConformidade.ID_MOTIVO_ABERTURA_NC","=", idMotivoAbertura, Long.class);            
            String dsMotivoAbertura= parameters.getString("motivoAberturaNc.dsMotivoAbertura");
            sqlExterno.addFilterSummary("motivoAbertura", dsMotivoAbertura);
        }
       			
       	sqlExterno.addProjection("sgFilialResp");
       	sqlExterno.addProjection("idFilialResp");
       	sqlExterno.addProjection("sgFilialAbertura");
       	sqlExterno.addProjection("dsMotivoAbertura");
       	sqlExterno.addProjection("tpControleCarga");
       	sqlExterno.addProjection("idRotaColetaEntrega");
       	sqlExterno.addProjection("rotaColetaEntrega");
       	sqlExterno.addProjection("trechoViagem");
       	sqlExterno.addProjection("idsTrechoViagem");
       	
	 	sqlExterno.addProjection("CASE \n" + 
								 "WHEN tpControleCarga = 'V' THEN 1 " +
								 "WHEN tpControleCarga = 'C' THEN 2 " +
								 "WHEN tpControleCarga IS NULL THEN 3 " +
								 "ELSE 4 " +
								 "END AS ordemTipoRota");
	 
        sqlExterno.addProjection("CASE \n" + 
        						 "WHEN tpControleCarga = 'V' THEN trechoViagem " +
        						 "WHEN tpControleCarga = 'C' THEN rotaColetaEntrega " +
        						 "WHEN tpControleCarga IS NULL THEN '<Sem Manifesto>' " +
        						 "ELSE 'Inválido' " +
        						 "END AS rota");

        sqlExterno.addProjection("SUM(quantidadeRNC) quantidadeRNC");
        sqlExterno.addProjection("SUM(quantidadeROI) quantidadeROI");
        sqlExterno.addProjection("SUM(quantidadeRRC) quantidadeRRC");
       	
        sqlExterno.addFrom("(" + sqlInterno.getSql() + ")");    
        
        sqlExterno.addGroupBy("sgFilialResp");
        sqlExterno.addGroupBy("idFilialResp");
        sqlExterno.addGroupBy("sgFilialAbertura");
        sqlInterno.addGroupBy(PropertyVarcharI18nProjection.createProjection("motivoAberturaNaoConformidade.DS_MOTIVO_ABERTURA_I"));
        sqlExterno.addGroupBy("dsMotivoAbertura");
        sqlExterno.addGroupBy("tpControleCarga");
        sqlExterno.addGroupBy("idRotaColetaEntrega");
        sqlExterno.addGroupBy("rotaColetaEntrega");
        sqlExterno.addGroupBy("trechoViagem");
        sqlExterno.addGroupBy("idsTrechoViagem");
        
        sqlExterno.addOrderBy("sgFilialResp");
        sqlExterno.addOrderBy("sgFilialAbertura");
        sqlExterno.addOrderBy("OrdemTipoRota");
        sqlExterno.addOrderBy("rota");
        
        //Adiciona os valores dos criterios de pesquisa do sqlInterno
        for (int i = 0; i < sqlInterno.getCriteria().length; i++) {
            Object object = sqlInterno.getCriteria()[i];
            sqlExterno.addCriteriaValue(object);
        }
        
		return sqlExterno;
	}
	
	/**
	 * Monta o map para ser enviado como parametro para o relatorio, contendo
	 * a soma dos documentos de servico encontrados para determinado manifesto.
	 * 
	 * @param parameters parametros enviados pela view para montar os filtros na sql
	 * @return Map
	 */
	private Map getDocumentoServico(TypedFlatMap parameters) {

		SqlTemplate sqlInternoDocumentoServico = createSqlTemplate();
		SqlTemplate sqlExternoDocumentoServico = createSqlTemplate();
		
		YearMonthDay dataInicial = parameters.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal = parameters.getYearMonthDay("dataFinal");
		
		//Montagem da sql interna
		sqlInternoDocumentoServico.addProjection("ID_MANIFESTO"); 
		sqlInternoDocumentoServico.addProjection("MAN1.ID_FILIAL_ORIGEM");
		sqlInternoDocumentoServico.addProjection("(" + this.countDocumentos(dataInicial, dataFinal) + ") as qtdDocumentos");
		sqlInternoDocumentoServico.addFrom("MANIFESTO MAN1"); 
		sqlInternoDocumentoServico.addCustomCriteria("MAN1.DH_EMISSAO_MANIFESTO IS NOT NULL");

		//Filtro externo. Se datas forem nulas, o sqlTemplate ignora o criterio.
		//Portanto não é necessário testar se o objeto é diferente de null.
       	sqlInternoDocumentoServico.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlInternoDocumentoServico.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
        
		//Montagem da sql externa
		sqlExternoDocumentoServico.addProjection("ID_FILIAL_ORIGEM");
		sqlExternoDocumentoServico.addProjection("SUM(qtdDocumentos) qtdDocumentos");
		sqlExternoDocumentoServico.addFrom("(" + sqlInternoDocumentoServico.getSql() + ")");
		
		//Adiciona os valores dos criterios de pesquisa do sqlInterno
        for (int i = 0; i < sqlInternoDocumentoServico.getCriteria().length; i++) {
            Object object = sqlInternoDocumentoServico.getCriteria()[i];
            sqlExternoDocumentoServico.addCriteriaValue(object);
        }
        
		//Filtro
        String idFilialResponsavel = parameters.getString("filialResponsavel.idFilial");
        if(StringUtils.isNotEmpty(idFilialResponsavel)) {
        	sqlExternoDocumentoServico.addCriteria("ID_FILIAL_ORIGEM","=", idFilialResponsavel, Long.class);
        }
        sqlExternoDocumentoServico.addGroupBy("ID_FILIAL_ORIGEM");
        		
		Map mapDocumentoServico = (Map) this.getJdbcTemplate().query(
				sqlExternoDocumentoServico.getSql(),  
				JodaTimeUtils.jdbcPureParamConverter(
						this.getJdbcTemplate(), 
						sqlExternoDocumentoServico.getCriteria()), 
				new ResultSetExtractor() {
					public Object extractData(ResultSet rs) throws SQLException {
						Map mapa = new HashMap();
						while (rs.next()) {
							mapa.put(rs.getString("ID_FILIAL_ORIGEM"), rs.getString("qtdDocumentos"));
						}
						return mapa;
					}
				}
		); 
		
		sqlInternoDocumentoServico = createSqlTemplate();
		sqlExternoDocumentoServico = createSqlTemplate();
		
		//Montagem da sql interna
		sqlInternoDocumentoServico.addProjection("ID_MANIFESTO");
		sqlInternoDocumentoServico.addProjection("MAN1.ID_FILIAL_DESTINO");
		sqlInternoDocumentoServico.addProjection("(" + this.countDocumentos(dataInicial, dataFinal) + ") as qtdDocumentos ");
		sqlInternoDocumentoServico.addFrom("MANIFESTO MAN1"); 
		sqlInternoDocumentoServico.addFrom("CONTROLE_CARGA CC");
		sqlInternoDocumentoServico.addJoin("MAN1.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA");
		sqlInternoDocumentoServico.addCustomCriteria("MAN1.DH_EMISSAO_MANIFESTO IS NOT NULL");

		//Filtros. Se datas forem nulas, o sqlTemplate ignora o criterio.
		//Portanto não é necessário testar se o objeto é diferente de null.
       	sqlInternoDocumentoServico.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlInternoDocumentoServico.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO  AS DATE))", "<=", dataFinal);
        
        //Montagem da sql externa
		sqlExternoDocumentoServico.addProjection("ID_FILIAL_DESTINO");
		sqlExternoDocumentoServico.addProjection("SUM(qtdDocumentos) qtdDocumentos");
		sqlExternoDocumentoServico.addFrom("(" + sqlInternoDocumentoServico.getSql() + ") ");
		
		//Adiciona os valores dos criterios de pesquisa do sqlInterno
        for (int i = 0; i < sqlInternoDocumentoServico.getCriteria().length; i++) {
            Object object = sqlInternoDocumentoServico.getCriteria()[i];
            sqlExternoDocumentoServico.addCriteriaValue(object);
        }
		
        if(StringUtils.isNotEmpty(idFilialResponsavel)) {
        	sqlExternoDocumentoServico.addCriteria("ID_FILIAL_DESTINO","=", idFilialResponsavel, Long.class);
        }
		sqlExternoDocumentoServico.addGroupBy("ID_FILIAL_DESTINO");
		
		Map mapFilialDestino = (Map) this.getJdbcTemplate().query(sqlExternoDocumentoServico.getSql(), JodaTimeUtils.jdbcPureParamConverter(this.getJdbcTemplate(), sqlExternoDocumentoServico.getCriteria()), new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException {
				Map mapa = new HashMap();
				while (rs.next()) {
					mapa.put(rs.getString("ID_FILIAL_DESTINO"), rs.getString("qtdDocumentos"));
				}
				return mapa;
			}
		}); 
		
		String key = null;
		for (Iterator iterator = mapFilialDestino.keySet().iterator(); iterator.hasNext();) {
			key = (String) iterator.next();
			if (mapDocumentoServico.containsKey(key)) {
				mapDocumentoServico.put(key, String.valueOf(
						Integer.parseInt(mapFilialDestino.get(key).toString()) + 
						Integer.parseInt(mapDocumentoServico.get(key).toString())));
			} else {
				if (key!=null) mapDocumentoServico.put(key, mapFilialDestino.get(key));
			}
			
		}
		
		return mapDocumentoServico;
	}
	
	private Map getDocumento(TypedFlatMap parameters) {

		SqlTemplate sqlInternoDocumento = createSqlTemplate();
		SqlTemplate sqlExternoDocumento = createSqlTemplate();
		
		YearMonthDay dataInicial = parameters.getYearMonthDay("dataInicial");
        YearMonthDay dataFinal = parameters.getYearMonthDay("dataFinal");	

        //Montagem da sql interna
		sqlInternoDocumento.addProjection("ID_MANIFESTO");
		sqlInternoDocumento.addProjection("MAN1.ID_FILIAL_ORIGEM");
		sqlInternoDocumento.addProjection("MAN1.ID_FILIAL_DESTINO");
		sqlInternoDocumento.addProjection("(" + this.countDocumentos(dataInicial, dataFinal) + ") as qtdDocumentos ");
		sqlInternoDocumento.addFrom("MANIFESTO MAN1"); 
		sqlInternoDocumento.addCustomCriteria("MAN1.DH_EMISSAO_MANIFESTO IS NOT NULL");
		
		//Filtro externo. Se datas forem nulas, o sqlTemplate ignora o criterio.
		//Portanto não é necessário testar se o objeto é diferente de null.
       	sqlInternoDocumento.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlInternoDocumento.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
		
		//Montagem da sql externa
		sqlExternoDocumento.addProjection("ID_FILIAL_ORIGEM");
		sqlExternoDocumento.addProjection("ID_FILIAL_DESTINO");
		sqlExternoDocumento.addProjection("SUM(qtdDocumentos) qtdDocumentos");
		sqlExternoDocumento.addFrom("(" + sqlInternoDocumento.getSql() + ") ");
        sqlExternoDocumento.addGroupBy("ID_FILIAL_ORIGEM");
        sqlExternoDocumento.addGroupBy("ID_FILIAL_DESTINO");
		
		//Adiciona os valores dos criterios de pesquisa do sqlInterno
        for (int i = 0; i < sqlInternoDocumento.getCriteria().length; i++) {
            Object object = sqlInternoDocumento.getCriteria()[i];
            sqlExternoDocumento.addCriteriaValue(object);
        }
		
		Map mapDocumento = (Map) this.getJdbcTemplate().query(sqlExternoDocumento.getSql(), JodaTimeUtils.jdbcPureParamConverter(this.getJdbcTemplate(), sqlExternoDocumento.getCriteria()), new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException {
				Map mapa = new HashMap();
				while (rs.next()) {
					mapa.put(rs.getString("ID_FILIAL_ORIGEM") + "-" +  rs.getString("ID_FILIAL_DESTINO"), rs.getString("qtdDocumentos"));
				}
				return mapa;
			}
		}); 
					
		sqlInternoDocumento = createSqlTemplate();
		sqlExternoDocumento = createSqlTemplate();
		
		//Montagem da sql interna
		sqlInternoDocumento.addProjection("ID_MANIFESTO");
		sqlInternoDocumento.addProjection("CC.ID_ROTA_COLETA_ENTREGA");
		sqlInternoDocumento.addProjection("(" + this.countDocumentos(dataInicial, dataFinal) + ") as qtdDocumentos ");
		sqlInternoDocumento.addFrom("MANIFESTO MAN1"); 
		sqlInternoDocumento.addFrom("CONTROLE_CARGA CC");
		sqlInternoDocumento.addJoin("MAN1.ID_CONTROLE_CARGA","CC.ID_CONTROLE_CARGA");
		sqlInternoDocumento.addCustomCriteria("MAN1.DH_EMISSAO_MANIFESTO IS NOT NULL");

		//Filtro. Se datas forem nulas, o sqlTemplate ignora o criterio.
		//Portanto não é necessário testar se o objeto é diferente de null.
       	sqlInternoDocumento.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlInternoDocumento.addCriteria("TRUNC (CAST(MAN1.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
        
		//Montagem da sql externa
		sqlExternoDocumento.addProjection("ID_ROTA_COLETA_ENTREGA");
		sqlExternoDocumento.addProjection("SUM(qtdDocumentos) qtdDocumentos");
		sqlExternoDocumento.addFrom("(" + sqlInternoDocumento.getSql() + ") ");
        sqlExternoDocumento.addGroupBy("ID_ROTA_COLETA_ENTREGA");
		
        //Adiciona os valores dos criterios de pesquisa do sqlInterno
        for (int i = 0; i < sqlInternoDocumento.getCriteria().length; i++) {
            Object object = sqlInternoDocumento.getCriteria()[i];
            sqlExternoDocumento.addCriteriaValue(object);
        }

		Map mapTemporario = (Map) this.getJdbcTemplate().query(sqlExternoDocumento.getSql(), JodaTimeUtils.jdbcPureParamConverter(this.getJdbcTemplate(), sqlExternoDocumento.getCriteria()), new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException {
				Map mapa = new HashMap();
				while (rs.next()) {
					mapa.put(rs.getString("ID_ROTA_COLETA_ENTREGA"), rs.getString("qtdDocumentos"));
				}
				return mapa;
			}
		}); 
		
		String key = null;
		for (Iterator iterator = mapTemporario.keySet().iterator(); iterator.hasNext();) {
			key = (String) iterator.next();
			if (key!=null) mapDocumento.put(key, mapTemporario.get(key));
		}
		
		return mapDocumento;
	}
	
	private String countDocumentos(YearMonthDay dataInicial, YearMonthDay dataFinal) {
		
		SqlTemplate sqlSubInternoDocumentoServico = createSqlTemplate();
		
		SqlTemplate sqlSubSubInternoDocumentoServico1 = createSqlTemplate();
		SqlTemplate sqlSubSubInternoDocumentoServico2 = createSqlTemplate();
		SqlTemplate sqlSubSubInternoDocumentoServico3 = createSqlTemplate();
		SqlTemplate sqlSubSubInternoDocumentoServico4 = createSqlTemplate();
        
		//Montagem da sub sql interna1
		sqlSubSubInternoDocumentoServico1.addProjection("manifestoNacionalCTO.ID_CONHECIMENTO");
		sqlSubSubInternoDocumentoServico1.addFrom("MANIFESTO_VIAGEM_NACIONAL manifestoViagemNacional");
		sqlSubSubInternoDocumentoServico1.addFrom("MANIFESTO_NACIONAL_CTO manifestoNacionalCTO");
		sqlSubSubInternoDocumentoServico1.addFrom("MANIFESTO man");
		sqlSubSubInternoDocumentoServico1.addJoin("manifestoViagemNacional.ID_MANIFESTO_VIAGEM_NACIONAL","MAN.ID_MANIFESTO");
		sqlSubSubInternoDocumentoServico1.addJoin("manifestoViagemNacional.ID_MANIFESTO_VIAGEM_NACIONAL","manifestoNacionalCTO.ID_MANIFESTO_VIAGEM_NACIONAL");
		sqlSubSubInternoDocumentoServico1.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlSubSubInternoDocumentoServico1.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
		
		//Montagem da sub sql interna2
		sqlSubSubInternoDocumentoServico2.addProjection("manifestoInternacionalCTO.ID_CTO_INTERNACIONAL");
		sqlSubSubInternoDocumentoServico2.addFrom("MANIFESTO_INTERNACIONAL manifestoInternacional");
		sqlSubSubInternoDocumentoServico2.addFrom("MANIFESTO_INTERNAC_CTO manifestoInternacionalCTO");
		sqlSubSubInternoDocumentoServico2.addFrom("MANIFESTO man");
		sqlSubSubInternoDocumentoServico2.addJoin("manifestoInternacional.ID_MANIFESTO_INTERNACIONAL","MAN.ID_MANIFESTO");
		sqlSubSubInternoDocumentoServico2.addJoin("manifestoInternacional.ID_MANIFESTO_INTERNACIONAL","manifestoInternacionalCTO.ID_MANIFESTO_INTERNACIONAL");
		sqlSubSubInternoDocumentoServico2.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlSubSubInternoDocumentoServico2.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
		
		//Montagem da sub sql interna3
		sqlSubSubInternoDocumentoServico3.addProjection("manifestoEntregaDocumento.ID_DOCTO_SERVICO");
		sqlSubSubInternoDocumentoServico3.addFrom("MANIFESTO_ENTREGA manifestoEntrega");
		sqlSubSubInternoDocumentoServico3.addFrom("MANIFESTO_ENTREGA_DOCUMENTO manifestoEntregaDocumento");
		sqlSubSubInternoDocumentoServico3.addFrom("MANIFESTO man");		
		sqlSubSubInternoDocumentoServico3.addJoin("manifestoEntrega.ID_MANIFESTO_ENTREGA","MAN.ID_MANIFESTO");
		sqlSubSubInternoDocumentoServico3.addJoin("manifestoEntrega.ID_MANIFESTO_ENTREGA","manifestoEntregaDocumento.ID_MANIFESTO_ENTREGA_DOCUMENTO");
		sqlSubSubInternoDocumentoServico3.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlSubSubInternoDocumentoServico3.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
		
		//Montagem da sub sql interna4
		sqlSubSubInternoDocumentoServico4.addProjection("preManifestoDocumento.ID_DOCTO_SERVICO");
		sqlSubSubInternoDocumentoServico4.addFrom("PRE_MANIFESTO_DOCUMENTO preManifestoDocumento");
		sqlSubSubInternoDocumentoServico4.addFrom("MANIFESTO man");		
		sqlSubSubInternoDocumentoServico4.addJoin("preManifestoDocumento.ID_PRE_MANIFESTO_DOCUMENTO","MAN.ID_MANIFESTO");
		sqlSubSubInternoDocumentoServico4.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", ">=", dataInicial);
       	sqlSubSubInternoDocumentoServico4.addCriteria("TRUNC (CAST(MAN.DH_EMISSAO_MANIFESTO AS DATE))", "<=", dataFinal);
		
		//Montagem da sub sql interna
		sqlSubInternoDocumentoServico.addProjection("count(*)");
		sqlSubInternoDocumentoServico.addFrom("DOCTO_SERVICO documentoServico");
		sqlSubInternoDocumentoServico.addCustomCriteria("documentoServico.TP_DOCUMENTO_SERVICO IN ('CRT','CTR','MDA')");
		sqlSubInternoDocumentoServico.addCustomCriteria("ID_DOCTO_SERVICO IN ("+
				"(" + sqlSubSubInternoDocumentoServico1.getSql() + ") UNION " +
				"(" + sqlSubSubInternoDocumentoServico2.getSql() + ") UNION " +
				"(" + sqlSubSubInternoDocumentoServico3.getSql() + ") UNION " +
				"(" + sqlSubSubInternoDocumentoServico4.getSql() + "))");
		
		if (dataInicial!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataInicial);
		if (dataFinal!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataFinal);
		if (dataInicial!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataInicial);
		if (dataFinal!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataFinal);
		if (dataInicial!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataInicial);
		if (dataFinal!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataFinal);
		if (dataInicial!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataInicial);
		if (dataFinal!=null) sqlSubInternoDocumentoServico.addCriteriaValue(dataFinal);
		
		return (String)getJdbcTemplate().queryForObject(sqlSubInternoDocumentoServico.getSql(true), JodaTimeUtils.jdbcPureParamConverter(this.getJdbcTemplate(), sqlSubInternoDocumentoServico.getCriteria()), String.class);
	}
}
