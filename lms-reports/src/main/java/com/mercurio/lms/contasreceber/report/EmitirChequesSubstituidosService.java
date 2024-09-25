package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * @author JoseMR
 *
 * @spring.bean id="lms.contasreceber.emitirChequesSubstituidosService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirChequesSubstituidos.jasper"
 */
public class EmitirChequesSubstituidosService extends ReportServiceSupport {

	ConversaoMoedaService conversaoMoedaService;
	
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		final Long idMoedaDestino = Long.valueOf(parameters.get("moeda.idMoeda").toString());
		final Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		
		List rel = getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper() {
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map map = new HashMap();
				
				Long idMoeda = Long.valueOf(rs.getLong("MOEDA"));
				Long idPais = Long.valueOf(rs.getLong("PAIS"));
				
				map.put("FILIAL", new Double(rs.getDouble("FILIAL")));
				map.put("SG_FILIAL", rs.getString("SG_FILIAL"));
				map.put("NM_FANTASIA", rs.getString("NM_FANTASIA"));
				map.put("BANCO_ANT", Short.valueOf(rs.getShort("BANCO_ANT")));
				map.put("AGENCIA_ANT", Short.valueOf(rs.getShort("AGENCIA_ANT")));
				map.put("CONTA_ANT", rs.getString("CONTA_ANT"));
				map.put("CHEQUE_ANT", Long.valueOf(rs.getLong("CHEQUE_ANT")));
				
				BigDecimal valorAnt = rs.getBigDecimal("VALOR_ANT");
				BigDecimal novoValorAnt = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtAtual, valorAnt);
				map.put("VALOR_ANT", novoValorAnt);

				
				map.put("BANCO_SUB", Short.valueOf(rs.getShort("BANCO_SUB")));
				map.put("AGENCIA_SUB", Short.valueOf(rs.getShort("AGENCIA_SUB")));
				map.put("CONTA_SUB", rs.getString("CONTA_SUB"));
				map.put("CHEQUE_SUB", Long.valueOf(rs.getLong("CHEQUE_SUB")));
				
				BigDecimal valorSub = rs.getBigDecimal("VALOR_SUB");
				BigDecimal novoValorSub = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtAtual, valorSub);
				map.put("VALOR_SUB", novoValorSub);

				return map;
				
			}
		});
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rel);

		JRReportDataObject jr = createReportDataObject(jrMap, parameters);
		
		Map parametersReport = new HashMap();
		
		String filial = null;
		
		if( !tfm.getString("siglaFilial").equals("") && !tfm.getString("filial.pessoa.nmFantasia").equals("") ){
			filial = tfm.getString("siglaFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"); 
			sql.addFilterSummary("filialCobranca",filial);
		}	
		
		sql.addFilterSummary("periodoSubstituicaoInicial",JTFormatUtils.format(tfm.getYearMonthDay("periodoInicial")));
		sql.addFilterSummary("periodoSubstituicaoFinal",JTFormatUtils.format(tfm.getYearMonthDay("periodoFinal")));
		
		//moeda
		sql.addFilterSummary("moedaExibicao", tfm.getString("moeda.dsSimbolo"));

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));		
		
		

		jr.setParameters(parametersReport);

		return jr;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("fche.id_filial as FILIAL, " +
				          "fche.sg_filial as SG_FILIAL, " +
				          "p.nm_Fantasia as NM_FANTASIA, " +
				          "che.nr_banco as BANCO_ANT, " +
				          "che.nr_agencia as AGENCIA_ANT, " +
				          "che.nr_conta_corrente as CONTA_ANT, " +
				          "che.nr_cheque as CHEQUE_ANT, " +
				          "che.vl_cheque as VALOR_ANT, " +
				          "subche.nr_banco as BANCO_SUB, " +
				          "subche.nr_agencia as AGENCIA_SUB, " +
				          "subche.nr_conta_corrente as CONTA_SUB, " +
				          "subche.nr_cheque as CHEQUE_SUB, " +
				          "subche.vl_cheque as VALOR_SUB, " +
				          "mp.id_moeda as MOEDA, " +
				          "mp.id_pais as PAIS " );
		
		sql.addFrom("cheque che " +
				    "	inner join filial fche on che.id_filial = fche.id_filial " +
				    "   inner join pessoa p on fche.id_filial = p.id_pessoa " +
				    "   inner join moeda_pais mp on mp.id_moeda_pais = che.id_moeda_pais " +
				    "   left outer join cheque subche on che.id_cheque = subche.id_cheque_substituido " +
				    "   inner join historico_cheque histche on histche.id_cheque = che.id_cheque " );
		
		sql.addCustomCriteria("histche.tp_historico_cheque = 'SU'");
		sql.addCriteria("fche.id_filial","=", tfm.getLong("filial.idFilial"));
		sql.addCustomCriteria("histche.dh_historico_cheque between ? and ?");
		sql.addCriteriaValue( JTDateTimeUtils.getFirstHourOfDay(JTDateTimeUtils.yearMonthDayToDateTime(tfm.getYearMonthDay("periodoInicial"))) );
		sql.addCriteriaValue( JTDateTimeUtils.getLastHourOfDay( JTDateTimeUtils.yearMonthDayToDateTime(tfm.getYearMonthDay("periodoFinal"))) );
		
		sql.addGroupBy("fche.id_filial, " +
				       "fche.sg_filial, " +
				       "p.nm_Fantasia, " +
				       "che.nr_banco, " +
				       "che.nr_agencia, " +
				       "che.nr_conta_corrente, " +
				       "che.nr_cheque, " +
				       "che.vl_cheque, " +
				       "subche.nr_banco, " +
				       "subche.nr_agencia, " +
				       "subche.nr_conta_corrente, " +
				       "subche.nr_cheque, " +
				       "subche.vl_cheque, mp.id_moeda, mp.id_pais " );
		
		sql.addOrderBy("fche.sg_filial, " +
				       "che.nr_banco, " +
				       "che.nr_agencia, " +
				       "che.nr_conta_corrente, " +
				       "che.nr_cheque");
		
		return sql;

	}

}
