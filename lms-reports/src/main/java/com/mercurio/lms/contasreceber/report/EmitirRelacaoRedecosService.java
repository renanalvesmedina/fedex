package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;


import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;


import com.mercurio.lms.util.session.SessionUtils;




/**
*
* @spring.bean id="lms.contasreceber.emitirRelacaoRedecos"
* @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRelacaoRedecos.jasper"
*/
public class EmitirRelacaoRedecosService extends ReportServiceSupport {

	
	ConversaoMoedaService conversaoMoedaService;
	
	private DomainService domainService;
	
	

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	
	
	
	
	public JRReportDataObject execute(Map criteria) throws Exception {

		
		//setando os dominios para que seja usados mais tarde
		final Domain dominioStatusRedeco = getDomainService().findByName("DM_STATUS_REDECO");
		final Domain dominioFinalidadeRedeco = getDomainService().findByName("DM_FINALIDADE_REDECO");
		final Domain dominioTipoRecebimento = getDomainService().findByName("DM_TIPO_RECEBIMENTO");

		
		TypedFlatMap map = (TypedFlatMap)criteria;
		
		SqlTemplate sql = mountSql(map, dominioStatusRedeco,  dominioFinalidadeRedeco);
		
		//pegando a data atual
		final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		
		//pegando a moeda que foi selecionada na tela
		final Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		final Long idMoedaDestino = Long.valueOf(map.get("moeda.idMoeda").toString());
		
		

		
		
		//lista com o retorno da consulta
		List rel = getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper() {
			
			//tratamento para cada tupla da consulta
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				//map que sera retornado
				Map mapRet = new HashMap();
				
				//pegando o valor tp_situacao
				String tpSituacao = rs.getString("situacao"); 
				
				//var para manipulacao
				BigDecimal valor 	= rs.getBigDecimal("total").subtract(rs.getBigDecimal("totalTarifas")).subtract(rs.getBigDecimal("totalDescontos")).add(rs.getBigDecimal("totalJuros"));
				Long idMoeda = Long.valueOf(rs.getLong("MOEDA"));
				Long idPais = Long.valueOf(rs.getLong("PAIS"));
				BigDecimal novoValorMoeda;
				
				if ("LI".equalsIgnoreCase( tpSituacao )  ){
					//converte conforme redeco.dt_liquidacao e  e moeda exibicao
					YearMonthDay dtLiquidacao 	= JTFormatUtils.buildYmd( rs.getDate("liquidacao") );
					novoValorMoeda = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtLiquidacao, valor);
				}else{
					//converte conforme data atual e moeda exibicao
					novoValorMoeda = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtAtual, valor);
				}
				
				//colocando o novoValorMoeda
				mapRet.put("VALOR", new Double(novoValorMoeda.doubleValue())); 
				
				
				
				// DP label do relatorio
				int dif;
				if ("LI".equalsIgnoreCase( tpSituacao ) || "CJ".equalsIgnoreCase( tpSituacao ) || "CA".equalsIgnoreCase( tpSituacao ) ){
					//colocar zero
					dif = 0 ;
				}else{
					//calcular a diferenca de dias conforme  redeco.dt_emissao ate a data atual
					YearMonthDay dtEmissao 	= JTFormatUtils.buildYmd( rs.getDate("emissao") );
					dif = JTDateTimeUtils.getIntervalInDays(dtEmissao,dtAtual);
					
					
				}
				//colocando a dif em dias
				mapRet.put("DP",new Double(dif));
				
				
				
				mapRet.put("NUMERO",		rs.getString("numero"));
				mapRet.put("EMISSAO",		rs.getDate("emissao"));
				mapRet.put("RECEBIMENTO",	rs.getDate("recebimento"));
				mapRet.put("LIQUIDACAO",	rs.getDate("liquidacao"));
				mapRet.put("SITUACAO",		dominioStatusRedeco.findDomainValueByValue(rs.getString("situacao")).getDescription().getValue());
				mapRet.put("TIPO_RECEBIMENTO",	dominioTipoRecebimento.findDomainValueByValue(rs.getString("tipo_recebimento")).getDescription().getValue());
				mapRet.put("FINALIDADE",	dominioFinalidadeRedeco.findDomainValueByValue(rs.getString("finalidade")).getDescription().getValue());
				mapRet.put("FILIAL",		rs.getString("filial"));
				mapRet.put("QTDE_DOCTOS",	new Double(rs.getDouble("qtde_doctos")));
						
				return mapRet;
				
			}
		});
		

		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rel);

		JRReportDataObject jr = createReportDataObject(jrMap, criteria);
		
		Map parametersReport = new HashMap();
		
		/* Tipo do relatório */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("tpFormatoRelatorio"));
		
    
        /* Parametros de pesquisa */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/* Usuario emissor */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		
		
		
		jr.setParameters(parametersReport);

		return jr;
	}

	
	private SqlTemplate mountSql(TypedFlatMap parametros, Domain dominioStatusRedeco, Domain dominioFinalidadeRedeco ){
		
		SqlTemplate sql = createSqlTemplate();
		//montando a projection
		sql.addProjection("red.nr_redeco","numero");
		sql.addProjection("red.dt_emissao","emissao");
		sql.addProjection("red.dt_recebimento","recebimento");
		sql.addProjection("red.dt_liquidacao","liquidacao");
		sql.addProjection("red.tp_situacao_redeco","situacao");
		sql.addProjection("red.tp_finalidade","finalidade");
		sql.addProjection("red.tp_recebimento","tipo_recebimento");
		sql.addProjection("sum(fat.vl_total)","total");
		sql.addProjection("sum(itRed.vl_tarifa)", "totalTarifas");
		sql.addProjection("sum(fat.vl_desconto)", "totalDescontos");
		sql.addProjection("sum(itRed.vl_juros)", "totalJuros");
		sql.addProjection("uFed.id_pais","pais");
		sql.addProjection("red.id_moeda","moeda");
		sql.addProjection("fil.sg_filial	|| ' - ' || pes.nm_fantasia","filial");
		sql.addProjection("count(itred.id_item_redeco)","qtde_doctos ");
							

		sql.addGroupBy("red.nr_redeco,red.dt_emissao,red.dt_recebimento,red.dt_liquidacao,red.tp_situacao_redeco,"+
				" red.tp_finalidade,red.tp_recebimento,uFed.id_pais,red.id_moeda, fil.sg_filial	|| ' - ' || pes.nm_fantasia");
		
		
		
		//from 
		sql.addFrom(" redeco red" +
				" INNER JOIN filial fil					ON red.id_filial = fil.id_filial" +
				" INNER JOIN pessoa pes 				ON fil.id_filial = pes.id_pessoa" +
				" INNER JOIN item_redeco itRed  		ON red.id_redeco = itRed.id_redeco" +
				" INNER JOIN fatura    fat      		ON fat.id_fatura = itRed.id_fatura" +
				" LEFT  JOIN empresa_cobranca empCob	ON empCob.id_empresa_cobranca = red.id_empresa_cobranca  "+
				" INNER JOIN endereco_pessoa endPes     ON pes.id_endereco_pessoa = endPes.id_endereco_pessoa" +
				" INNER JOIN municipio mun      		ON endPes.id_municipio = mun.id_municipio " +
				" INNER JOIN unidade_federativa  uFed   ON mun.ID_UNIDADE_FEDERATIVA = uFed.ID_UNIDADE_FEDERATIVA ");
		
		//os criteria

		
		//filial de cobranca
		sql.addCriteria("red.id_filial","=",parametros.getLong("filial.idFilial"));
//		finalidade
		sql.addCriteria("red.tp_finalidade","=",parametros.getString("tpFinalidade"));
//		situacao
		sql.addCriteria("red.tp_situacao_redeco","=",parametros.getString("tpSituacao"));
//		periodo de emissao
		
		sql.addCriteria("red.dt_emissao ",">=",parametros.getYearMonthDay("periodoEmissaoInicial"));
		sql.addCriteria("red.dt_emissao ","<=",parametros.getYearMonthDay("periodoEmissaoFim"));
		
		//		empresa de cobranca
		sql.addCriteria("red.id_empresa_cobranca","=",parametros.getLong("empresaCobranca.idEmpresaCobranca"));

		
		//FilterSummary
		if (  StringUtils.isNotBlank( parametros.getString("siglaFilial") ) ){
			sql.addFilterSummary("filialCobranca", parametros.getString("siglaFilial") + " - " + parametros.getString("filial.pessoa.nmFantasia"));
		}
		
		if (  StringUtils.isNotBlank( parametros.getString("tpFinalidade") ) ){
			sql.addFilterSummary("finalidade", dominioFinalidadeRedeco.findDomainValueByValue(parametros.getString("tpFinalidade")).getDescription().getValue());
		}
		if (  StringUtils.isNotBlank( parametros.getString("tpSituacao") )){
			sql.addFilterSummary("situacao", dominioStatusRedeco.findDomainValueByValue(parametros.getString("tpSituacao")).getDescription().getValue());
		}
		
		
		if (  StringUtils.isNotBlank( parametros.getString("periodoEmissaoInicial") ) ){
			sql.addFilterSummary("periodoEmissaoInicial", JTFormatUtils.format(  parametros.getYearMonthDay("periodoEmissaoInicial")) );
		}
	
		if (  StringUtils.isNotBlank( parametros.getString("periodoEmissaoFim") ) ){
			sql.addFilterSummary("periodoEmissaoFinal", JTFormatUtils.format(  parametros.getYearMonthDay("periodoEmissaoFim")) );
		}
		
		sql.addFilterSummary("empresaCobranca", parametros.getString("nmPessoa"));
		sql.addFilterSummary("moedaExibicao", parametros.getString("siglaMoeda"));
		
		//order by
		sql.addOrderBy("fil.sg_filial	|| ' - ' || pes.nm_fantasia, red.nr_redeco");
	
	    return sql;
	}





	/**
	 * @return Returns the domainService.
	 */
	public DomainService getDomainService() {
		return domainService;
	}





	/**
	 * @param domainService The domainService to set.
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	
	

	
	
	


}
