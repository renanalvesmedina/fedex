package com.mercurio.lms.tabelaprecos.model.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.tabelaprecos.model.AtualizarReajusteDTO;
import com.mercurio.lms.tabelaprecos.model.ParametroClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ParametroReajusteTabPreco;
import com.mercurio.lms.tabelaprecos.model.ReajusteGeneralidadeCliente;
import com.mercurio.lms.tabelaprecos.model.ReajusteServicoAdicionalCliente;
import com.mercurio.lms.tabelaprecos.model.ReajusteTaxaCliente;

public class ParametroReajusteTabelaPrecoDAO  extends BaseCrudDao<ParametroReajusteTabPreco, Long>{
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected Class getPersistentClass() {
		return ParametroReajusteTabPreco.class;
	}
	
	public List<Long> listIdsParametroReajusteTabelaPreco(List<Long> idsReajusteTabPrecoParcela) {
		return jdbcTemplate.query(queryIdsParametroReajusteTabelaPreco(whereIdsParametroReajusteTabelaPreco(idsReajusteTabPrecoParcela)), listIdsRowMapper());
	}

	private String queryIdsParametroReajusteTabelaPreco(String where){
		return new StringBuilder().append(" select id_parametro_reaj_tab_preco from parametro_reajuste_tab_preco ").append(where).toString();
	}

	private String whereIdsParametroReajusteTabelaPreco(List<Long> idsReajusteTabPrecoParcela){
		StringBuilder  where    = new StringBuilder().append(" where "); 
		Iterator<Long> iterator = idsReajusteTabPrecoParcela.iterator();
		while(iterator.hasNext()){
			where.append(" id_reajuste_tab_preco_parcela = ").append(iterator.next()).append(iterator.hasNext() ? " or " : "" );
		}
		
		return where.toString();
	}
	
	private RowMapper listIdsRowMapper() {
		return new RowMapper(){
			@Override
			public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return resultSet.getLong(1);
			}
		};
	}
	
	public List<Map<String,Object>> listParametrosReajuste(Long idReajuste, Long idTabelaBase, List<Long> idsParcelas){
		StringBuilder sql = new StringBuilder()
									.append(queryGeneralidade(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)))
									.append(" union ")
									.append(queryServicoAdicional(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)))
									.append(" union ")
									.append(queryTaxa(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)))
									.append(" union ")
									.append(queryParcelasFaixaPeso(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)))
									.append(" union ")
									.append(queryParcelasTarifa(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)))
									.append(" union ")
									.append(queryParcelasTarifaMinima(whereParametrosReajuste(idReajuste, idTabelaBase, idsParcelas)));
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_REAJUSTE_TAB_PRECO_PARCELA", Hibernate.LONG);
				sqlQuery.addScalar("ID_PRECO_FRETE", Hibernate.LONG);
				sqlQuery.addScalar("ID_VALOR_FAIXA_PROGRESSIVA", Hibernate.LONG);
				sqlQuery.addScalar("ID_TABELA_PRECO_PARCELA", Hibernate.LONG);
				sqlQuery.addScalar("VALOR_PARCELA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_MINIMO_PARCELA", Hibernate.BIG_DECIMAL);
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), null, confSql); 
	}
	
	public void removeAllIds(List<Long> ids){
		SessionFactory sessionFactory = getAdsmHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		for (int i = 0; i < ids.size(); i++) {
			session.delete(new ParametroReajusteTabPreco(ids.get(i)));	
			if ( i % 1000 == 0 ) { 
				session.flush();
			    session.clear();
			}
		}
		
		tx.commit();
		session.close();
	}
	
	public List<Map<String,Object>> listParcelaByCodParcela(Long idReajuste, String codParcela){
		StringBuilder sql = new StringBuilder()
									.append(" select tp.CD_TARIFA_PRECO, prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.id_parametro_reaj_tab_preco ")
									.append(" from 	reajuste_tabela_preco rtp ")
									.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
									.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
									.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and pp.CD_PARCELA_PRECO = ? ")
									.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
									.append(" inner join preco_frete pf on pf.id_preco_frete = prtp.id_preco_frete ")
									.append(" inner join tarifa_preco tp on tp.id_tarifa_preco = pf.id_tarifa_preco ")
									.append(" where rtp.id_reajuste_tabela_preco = ? ")
									.append(" order by tp.CD_TARIFA_PRECO ");
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("CD_TARIFA_PRECO", resultSet.getString(1));
				map.put("PERCENTUAL", getPercentValue(resultSet.getBigDecimal(3), resultSet.getBigDecimal(2)));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(3));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(3), resultSet.getBigDecimal(2)));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(4));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql.toString(), new Object[]{codParcela, idReajuste}, rowMapper);
	}

	public List<Map<String,Object>> listParcelaTaxaTerrestre(Long idReajuste){
		StringBuilder sql = new StringBuilder()
		.append(" select SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), ")
		.append(" INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as parcela_preco, ")
		.append(" prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.PC_REAJUSTE_VAL_MIN_PARCELA, prtp.VALOR_MINIMO_PARCELA, prtp.id_parametro_reaj_tab_preco, ")
		.append(" fp.VL_FAIXA_PROGRESSIVA, vfp.ID_ROTA_PRECO ")
		.append(" from  reajuste_tabela_preco rtp, reajuste_tabela_preco_parcela rtpp, tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp, ")
		.append(" valor_faixa_progressiva vfp, faixa_progressiva fp ")
		.append(" where rtp.id_reajuste_tabela_preco = ? ")
		.append(" and   rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
		.append(" and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
		.append(" and   pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO ")
		.append(" and   pp.CD_PARCELA_PRECO = 'IDTaxaTerrestre' ")
		.append(" and   tp_parcela_preco = 'T' ")
		.append(" and   tp_precificacao = 'M' ")
		.append(" and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
		.append(" and   vfp.id_valor_faixa_progressiva = prtp.id_valor_faixa_progressiva ")
		.append(" and   fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
		.append(" and   fp.VL_FAIXA_PROGRESSIVA is not null ")
		.append(" order by fp.VL_FAIXA_PROGRESSIVA");

		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PERCENTUAL", resultSet.getBigDecimal(2));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(3));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(3), resultSet.getBigDecimal(2)));
				map.put("FAIXA_PROGRESSIVA", resultSet.getBigDecimal(7));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(6));
				return map;
			}
		};

		return jdbcTemplate.query(sql.toString(), new Object[]{idReajuste}, rowMapper);
	}
	
	public List<Map<String,Object>> listFretePesoByTabelaReferencialRodoviario(Long idReajuste){
		String sql = new StringBuilder()
							.append(" select fp.VL_FAIXA_PROGRESSIVA, vfp.NR_FATOR_MULTIPLICACAO ")
							.append(" from  reajuste_tabela_preco rtp  ")
							.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
							.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
							.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and (tp_parcela_preco = 'P' and tp_precificacao = 'M' )  ")
							.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
							.append(" inner join valor_faixa_progressiva vfp on vfp.id_valor_faixa_progressiva = prtp.id_valor_faixa_progressiva ")
							.append(" inner join faixa_progressiva fp on fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
							.append(" where rtp.id_reajuste_tabela_preco = ? ")
							.append(" order by fp.VL_FAIXA_PROGRESSIVA ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("FAIXA_PROGRESSIVA", resultSet.getLong(1));
				map.put("VALOR", resultSet.getBigDecimal(2));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, new Object[]{idReajuste}, rowMapper);
	}
	
	public List<Map<String,Object>> listParcelaByGeneralidade(Long idReajuste){
		String sql = new StringBuilder()
		.append(" select SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as parcela_preco, ")
		.append(" prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.PC_REAJUSTE_VAL_MIN_PARCELA, prtp.VALOR_MINIMO_PARCELA, prtp.id_parametro_reaj_tab_preco  ")
		.append(" from  reajuste_tabela_preco rtp  ")
		.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
		.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
		.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and (pp.tp_parcela_preco = 'G' or pp.tp_precificacao = 'G')  ")
		.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
		.append(" where rtp.id_reajuste_tabela_preco = ? ")
		.append(" order by parcela_preco ").toString();
		
		return listParcelaWithPercValorAndPercValorMin(idReajuste, sql);
	}
	
	public List<Map<String,Object>> listParcelaByServicoAdicional(Long idReajuste){
		String sql = new StringBuilder()
		.append(" select SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as parcela_preco, ")
		.append(" prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.PC_REAJUSTE_VAL_MIN_PARCELA, prtp.VALOR_MINIMO_PARCELA, prtp.id_parametro_reaj_tab_preco   ")
		.append(" from  reajuste_tabela_preco rtp   ")
		.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
		.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
		.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and (tp_parcela_preco = 'S' and tp_precificacao = 'S' )   ")
		.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
		.append(" where rtp.id_reajuste_tabela_preco = ? ")
		.append(" order by parcela_preco ").toString();
		
		return listParcelaWithPercValorAndPercValorMin(idReajuste, sql);
	}
	
	public List<Map<String,Object>> listParcelaByTaxas(Long idReajuste){
		String sql = new StringBuilder()
		.append(" select SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as parcela_preco, ")
		.append(" prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.PC_REAJUSTE_VAL_MIN_PARCELA, prtp.VALOR_MINIMO_PARCELA, prtp.id_parametro_reaj_tab_preco  ")
		.append(" from  reajuste_tabela_preco rtp  ")
		.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
		.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
		.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and (tp_parcela_preco = 'T' and tp_precificacao = 'T' )  ")
		.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
		.append(" where rtp.id_reajuste_tabela_preco = ? ")
		.append(" order by parcela_preco ").toString();
		
		return listParcelaWithPercValorAndPercValorMin(idReajuste, sql);
	}
	
	private List<Map<String,Object>> listParcelaWithPercValorAndPercValorMin(Long idReajuste, String sql){
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("DESCRICAO", resultSet.getString(1));
				map.put("PERCENTUAL_VALOR", getPercentValue(resultSet.getBigDecimal(3), resultSet.getBigDecimal(2)));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(3));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(3), resultSet.getBigDecimal(2)));
				map.put("PERCENTUAL_VALOR_MIN", getPercentValue(resultSet.getBigDecimal(5), resultSet.getBigDecimal(4)));
				map.put("VALOR_ORIGINAL_MIN", resultSet.getBigDecimal(5));
				map.put("VALOR_CALCULADO_MIN", valorCalculado(resultSet.getBigDecimal(5), resultSet.getBigDecimal(4)));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(6));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, new Object[]{idReajuste}, rowMapper);
	}
	
	private BigDecimal valorCalculado(BigDecimal orginalValue, BigDecimal percentValue){
		if(orginalValue == null){
			return null;
		}
		
		return orginalValue.add((orginalValue.multiply(percentValue)).divide(BigDecimal.valueOf(100L)));
	}
	
	private BigDecimal getPercentValue(BigDecimal value, BigDecimal percent){
		return value == null ? null : percent;
	}
	
	private String queryGeneralidade(String where){
		return new StringBuilder()
						.append(" select ")
						.append(" 		tpp.id_tabela_preco_parcela, null ID_PRECO_FRETE, null ID_VALOR_FAIXA_PROGRESSIVA, g.VL_GENERALIDADE VALOR_PARCELA, ")
						.append("		g.VL_MINIMO VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela ")
						.append(" from  tabela_preco_parcela tpp ")
						.append(" 	    inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco ")
						.append(" 		inner join generalidade g on g.id_generalidade = tpp.id_tabela_preco_parcela ")
						.append("       inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append( where )
						.toString();
	}
	
	private String queryServicoAdicional(String where){
		return new StringBuilder()
						.append(" select ")
						.append("		tpp.id_tabela_preco_parcela, null ID_PRECO_FRETE, null ID_VALOR_FAIXA_PROGRESSIVA, vsa.vl_servico VALOR_PARCELA, ")
						.append("		vsa.VL_MINIMO VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela ") 
						.append(" from  tabela_preco_parcela tpp ")
						.append(" 	    inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco ")
						.append("       inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append("		inner join valor_servico_adicional vsa on vsa.id_valor_servico_adicional = tpp.id_tabela_preco_parcela ") 
						.append( where )
						.toString();
	}
	
	private String queryTaxa(String where){
		return new StringBuilder()
						.append(" select ")
						.append(" 		tpp.id_tabela_preco_parcela, null ID_PRECO_FRETE, null ID_VALOR_FAIXA_PROGRESSIVA, vt.vl_taxa VALOR_PARCELA,  ")
						.append("  		vt.vl_excedente VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela ")
						.append(" from  tabela_preco_parcela tpp ")
						.append(" 	    inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco ")
						.append("       inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append("		inner join valor_taxa vt on  vt.id_valor_taxa = tpp.id_tabela_preco_parcela ") 
						.append( where )
						.toString();
	}
	
	private String queryParcelasFaixaPeso(String where){
		return new StringBuilder()
						.append(" select ")
						.append("  		tpp.id_tabela_preco_parcela, null ID_PRECO_FRETE, vfp.ID_VALOR_FAIXA_PROGRESSIVA, vfp.vl_fixo VALOR_PARCELA, ")
						.append("  		null VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela ")
						.append(" from  tabela_preco_parcela tpp ")
						.append(" 	    inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco ")
						.append("       inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append("  		inner join faixa_progressiva fp on fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append(" 		inner join valor_faixa_progressiva vfp on vfp.id_faixa_progressiva = fp.id_faixa_progressiva ")						
						.append( where )
						.toString();
	}
	
	private String queryParcelasTarifa(String where){
		return new StringBuilder()
						.append(" select ")
						.append("  		tpp.id_tabela_preco_parcela, pf.id_preco_frete ID_PRECO_FRETE, null ID_VALOR_FAIXA_PROGRESSIVA, pf.VL_PRECO_FRETE VALOR_PARCELA,  ")
						.append("		null VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela ") 
						.append(" from  tabela_preco_parcela tpp ")
						.append(" 	    inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco ")
						.append("       inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ")
						.append("		inner join preco_frete pf on pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela ") 
						.append("		inner join tarifa_preco tp on tp.id_tarifa_preco = pf.id_tarifa_preco ")
						.append( where )
						.toString();
	}
	
	private String queryParcelasTarifaMinima(String where){
		return new StringBuilder()
						.append(" select tpp.id_tabela_preco_parcela, pf.id_preco_frete ID_PRECO_FRETE, null ID_VALOR_FAIXA_PROGRESSIVA, pf.VL_PRECO_FRETE VALOR_PARCELA,  ")
						.append("        null VL_MINIMO_PARCELA, rpp.id_reajuste_tab_preco_parcela  ")
						.append(" from  tabela_preco_parcela tpp  ")
						.append("       inner join parcela_preco pp on pp.id_parcela_preco = tpp.id_parcela_preco   ")
						.append("		inner join reajuste_tabela_preco_parcela rpp on rpp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  ")
						.append(" 		inner join preco_frete pf on pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  ")
						.append("       inner join rota_preco rp on rp.id_rota_preco = pf.ID_ROTA_PRECO   ")
						.append( where )
						.toString();
	}
	
	private String whereParametrosReajuste(Long idReajuste, Long idTabelaBase, List<Long> idsParcelas){
		StringBuilder where = new StringBuilder();
		
		where.append(" where tpp.id_tabela_preco = ").append(idTabelaBase);
		where.append(" 	 and rpp.id_reajuste_tabela_preco = ").append(idReajuste);
		
		where.append("   and ( "); 
		
		Iterator iterator = idsParcelas.iterator();
		while(iterator.hasNext()){
			where.append(" tpp.id_tabela_preco_parcela = ").append(iterator.next()).append(iterator.hasNext() ? " or " : "" );
		}
		
		where.append(" ) "); 
		
		return where.toString();
	}
	
	public void updatePercentualValor(Long id, BigDecimal percentualValor){
		StringBuilder sql = new StringBuilder()
									.append(" UPDATE parametro_reajuste_tab_preco SET PC_REAJUSTE_VALOR_PARCELA = ").append(percentualValor)
									.append(" WHERE ID_PARAMETRO_REAJ_TAB_PRECO = ").append(id);
		
		jdbcTemplate.update(sql.toString());
	}
	
	public void updatePercValorAndPercValorMin(Long id, BigDecimal percentualValor, BigDecimal percentualValorMin){
		StringBuilder sql = new StringBuilder()
									.append(" UPDATE parametro_reajuste_tab_preco SET PC_REAJUSTE_VALOR_PARCELA = ").append(percentualValor)
									.append(" , PC_REAJUSTE_VAL_MIN_PARCELA = ").append(percentualValorMin) 
									.append(" WHERE ID_PARAMETRO_REAJ_TAB_PRECO = ").append(id);
		
		jdbcTemplate.update(sql.toString());
	}
	
	public List<Map<String, Object>> listFretePesoByTabelaAereo(Integer idReajuste, Integer idOrigem, Integer idDestino){
		String sql = new StringBuilder()
							.append(" select ")
							.append(" 	UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as rota_origem, ")
							.append(" 	UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as rota_destino, ")
							.append(" 	prtp.VALOR_PARCELA, fp.VL_FAIXA_PROGRESSIVA , prtp.id_parametro_reaj_tab_preco, prtp.pc_reajuste_valor_parcela ")
							.append(" from  reajuste_tabela_preco rtp, reajuste_tabela_preco_parcela rtpp, tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp, valor_faixa_progressiva vfp,  ")
							.append(" 		faixa_progressiva fp, rota_preco rp, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,  ")
							.append(" 		aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO ")
							.append(" where rtp.id_reajuste_tabela_preco = ? ")
							.append(" 		and   rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
							.append(" 		and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
							.append(" 		and   pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO  ")
							.append(" 		and  (tp_parcela_preco = 'P' and tp_precificacao = 'M' )  ")
							.append(" 		and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
							.append(" 		and   vfp.id_valor_faixa_progressiva = prtp.id_valor_faixa_progressiva ")
							.append(" 		and   fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
							.append(" 		and   rp.id_rota_preco = vfp.id_rota_preco ")
							.append(" 		and   rp.id_uf_origem = UF_ORIGEM.id_unidade_federativa (+)  ")
							.append(" 		and   rp.id_municipio_origem = M_ORIGEM.id_municipio (+) ")
							.append(" 		and   rp.id_filial_origem = F_ORIGEM.id_filial (+) ")
							.append(" 		and   rp.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+)  ")
							.append(" 		and   rp.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_origem = A_ORIGEM.id_aeroporto (+) ")
							.append(" 		and   rp.id_uf_destino = UF_DESTINO.id_unidade_federativa (+)  ")
							.append(" 		and   rp.id_municipio_destino = M_DESTINO.id_municipio (+)  ")
							.append(" 		and   rp.id_filial_destino = F_DESTINO.id_filial (+)  ")
							.append(" 		and   rp.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ")
							.append(" 		and   rp.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_destino = A_DESTINO.id_aeroporto (+) ")
							.append(" 		and   fp.VL_FAIXA_PROGRESSIVA is not null ")
							.append(		idOrigem  == null ? "" : " and   UF_ORIGEM.id_unidade_federativa = ? ")
							.append(		idDestino == null ? "" : " and   UF_DESTINO.id_unidade_federativa = ? ")
							.append(" 		order by rota_origem, rota_destino, fp.VL_FAIXA_PROGRESSIVA ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ORIGEM", resultSet.getString(1));
				map.put("DESTINO", resultSet.getString(2));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(3));
				map.put("FAIXA_PROGRESSIVA", resultSet.getLong(4));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(5));
				map.put("PERCENTUAL", resultSet.getBigDecimal(6));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(3), resultSet.getBigDecimal(6)));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, createWhereFretePesoAereo(idReajuste, idOrigem, idDestino), rowMapper);
	}
	
	public List<Map<String, Object>> listParcelaTarifaMinima(Integer idReajuste, Integer idOrigem, Integer idDestino){
		String sql = new StringBuilder()
							.append(" select  ")
							.append(" 		prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.id_parametro_reaj_tab_preco,  ")
							.append(" 		UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as rota_origem, ") 
							.append(" 		UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as rota_destino ") 
							.append(" from  reajuste_tabela_preco rtp, reajuste_tabela_preco_parcela rtpp, tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp, preco_frete pf,  ")
							.append("		rota_preco rp, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,  ")
							.append("		aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO  ")
							.append(" where rtp.id_reajuste_tabela_preco =  ? ")
							.append(		idOrigem  == null ? "" : " and   UF_ORIGEM.id_unidade_federativa = ? ")
							.append(		idDestino == null ? "" : " and   UF_DESTINO.id_unidade_federativa = ? ")
							.append(" 		and   rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
							.append(" 		and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
							.append(" 		and   pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO  ")
							.append(" 		and   tp_parcela_preco = 'P'  ")
							.append(" 		and   tp_precificacao = 'P'   ")
							.append(" 		and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
							.append(" 		and   pf.id_preco_frete = prtp.id_preco_frete ")
							.append(" 		and   rp.id_rota_preco = pf.id_rota_preco ")
							.append(" 		and   rp.id_uf_origem = UF_ORIGEM.id_unidade_federativa (+)  ")
							.append(" 		and   rp.id_municipio_origem = M_ORIGEM.id_municipio (+) ")
							.append("		and   rp.id_filial_origem = F_ORIGEM.id_filial (+)  ")
							.append(" 		and   rp.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+)   ")
							.append(" 		and   rp.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_origem = A_ORIGEM.id_aeroporto (+) ")
							.append(" 		and   rp.id_uf_destino = UF_DESTINO.id_unidade_federativa (+)        ")
							.append(" 		and   rp.id_municipio_destino = M_DESTINO.id_municipio (+)    ")
							.append(" 		and   rp.id_filial_destino = F_DESTINO.id_filial (+)           ")
							.append(" 		and   rp.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ")
							.append(" 		and   rp.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_destino = A_DESTINO.id_aeroporto (+) ")
							.append(" order by rota_origem, rota_destino ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PERCENTUAL", resultSet.getBigDecimal(1));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(2));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(2), resultSet.getBigDecimal(1)));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(3));
				map.put("ORIGEM", resultSet.getString(4));
				map.put("DESTINO", resultSet.getString(5));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, createWhereFretePesoAereo(idReajuste, idOrigem, idDestino), rowMapper);
	}
							
	
	public List<Map<String, Object>> listParcelaTaxaCombustivel(Integer idReajuste, Integer idOrigem, Integer idDestino){
		String sql = new StringBuilder()
							.append(" select  ")
							.append("   prtp.pc_reajuste_valor_parcela, prtp.VALOR_PARCELA, prtp.id_parametro_reaj_tab_preco, ")
							.append(" 	fp.VL_FAIXA_PROGRESSIVA,  ")
							.append("   UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as rota_origem, ") 
							.append("   UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as rota_destino ") 
							.append(" from  reajuste_tabela_preco rtp, reajuste_tabela_preco_parcela rtpp, tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp, valor_faixa_progressiva vfp,  ")
							.append("   faixa_progressiva fp, rota_preco rp, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,  ")
							.append("   aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO ")
							.append(" where rtp.id_reajuste_tabela_preco =  ? ")
							.append(		idOrigem  == null ? "" : " and   UF_ORIGEM.id_unidade_federativa = ? ")
							.append(		idDestino == null ? "" : " and   UF_DESTINO.id_unidade_federativa = ? ")
							.append(" 		and   rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
							.append(" 		and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
							.append(" 		and   pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO  ")
							.append(" 		and   tp_parcela_preco = 'T'  ")
							.append(" 		and   tp_precificacao = 'M'  ")
							.append(" 		and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
							.append(" 		and   vfp.id_valor_faixa_progressiva = prtp.id_valor_faixa_progressiva ")
							.append(" 		and   fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
							.append(" 		and   rp.id_rota_preco = vfp.id_rota_preco ")
							.append(" 		and   rp.id_uf_origem = UF_ORIGEM.id_unidade_federativa (+)     ")
							.append(" 		and   rp.id_municipio_origem = M_ORIGEM.id_municipio (+) ")
							.append(" 		and   rp.id_filial_origem = F_ORIGEM.id_filial (+) ")
							.append(" 		and   rp.id_tipo_localizacao_origem = TLM_ORIGEM.id_tipo_localizacao_municipio (+)   ")
							.append(" 		and   rp.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_origem = A_ORIGEM.id_aeroporto (+) ")
							.append(" 		and   rp.id_uf_destino = UF_DESTINO.id_unidade_federativa (+)   ")
							.append(" 		and   rp.id_municipio_destino = M_DESTINO.id_municipio (+)      ")
							.append(" 		and   rp.id_filial_destino = F_DESTINO.id_filial (+)   ")
							.append("  		and   rp.id_tipo_localizacao_destino = TLM_DESTINO.id_tipo_localizacao_municipio (+) ")
							.append(" 		and   rp.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao (+) ")
							.append(" 		and   rp.id_aeroporto_destino = A_DESTINO.id_aeroporto (+) ")
							.append(" 		and   fp.VL_FAIXA_PROGRESSIVA is not null ")
							.append(" order by rota_origem, rota_destino, fp.VL_FAIXA_PROGRESSIVA ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PERCENTUAL", resultSet.getBigDecimal(1));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(2));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(2), resultSet.getBigDecimal(1)));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(3));
				map.put("FAIXA_PROGRESSIVA", resultSet.getBigDecimal(4));
				map.put("ORIGEM", resultSet.getString(5));
				map.put("DESTINO", resultSet.getString(6));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, createWhereFretePesoAereo(idReajuste, idOrigem, idDestino), rowMapper);
	}
	
	public List<Map<String,Object>> listFretePesoByTabelaDiferenciada(Long idReajuste){
		String sql = new StringBuilder()
							.append(" select tp.CD_TARIFA_PRECO, fp.VL_FAIXA_PROGRESSIVA, prtp.VALOR_PARCELA, prtp.pc_reajuste_valor_parcela, prtp.id_parametro_reaj_tab_preco ")
							.append(" from  reajuste_tabela_preco rtp  ")
							.append(" inner join reajuste_tabela_preco_parcela rtpp on rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco  ")
							.append(" inner join tabela_preco_parcela tpp on tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela  ")
							.append(" inner join parcela_preco pp on pp.ID_PARCELA_PRECO = tpp.ID_PARCELA_PRECO and (tp_parcela_preco = 'P' and tp_precificacao = 'M' )  ")
							.append(" inner join parametro_reajuste_tab_preco prtp on prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela  ")
							.append(" inner join valor_faixa_progressiva vfp on vfp.id_valor_faixa_progressiva = prtp.id_valor_faixa_progressiva ")
							.append(" inner join faixa_progressiva fp on fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
							.append(" inner join tarifa_preco tp on tp.id_tarifa_preco = vfp.id_tarifa_preco  ")
							.append(" where rtp.id_reajuste_tabela_preco = ? ")
							.append(" order by tp.CD_TARIFA_PRECO, fp.VL_FAIXA_PROGRESSIVA ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("TARIFA", resultSet.getString(1));
				map.put("FAIXA_PROGRESSIVA", resultSet.getLong(2));
				map.put("VALOR_ORIGINAL", resultSet.getBigDecimal(3));
				map.put("PERCENTUAL", resultSet.getBigDecimal(4));
				map.put("ID_PARAMETRO_REAJUSTE", resultSet.getLong(5));
				map.put("VALOR_CALCULADO", valorCalculado(resultSet.getBigDecimal(3), resultSet.getBigDecimal(4)));
				return map;
			}
		};
		
		return jdbcTemplate.query(sql, new Object[]{idReajuste}, rowMapper);
	}
	
	private Object[] createWhereFretePesoAereo(Integer idReajuste, Integer idOrigem, Integer idDestino){
		List<Integer> where = new ArrayList<Integer>();
		where.add(idReajuste);
		
		if(idOrigem != null){
			where.add(idOrigem);
		}
		
		if(idDestino != null){
			where.add(idDestino);
		}
		
		return where.toArray();
	}
	
	public List<ParametroClienteAutomaticoDTO> percentuaisToReajusteClienteAutomaticos(Long idDivisaoTabelaCliente, boolean novoReajuste){
    	StringBuilder sql = new StringBuilder();
    			sql.append(" select ")
    	 		.append("  	( ").append( percentualSubQuery("IDGris") ).append(" ) as percentualGris, ")
    	 		.append("  	( ").append( percentualMinimoSubQuery("IDGris") ).append(" ) as percentualMinGris, ")
    	 		.append("  	( ").append( percentualSubQuery("IDTde") ).append(" ) as percentualTDE, ")
    	 		.append("  	( ").append( percentualMinimoSubQuery("IDTde") ).append(" ) as percentualMinTDE, ")
    	 		.append("  	( ").append( percentualSubQuery("IdTrt") ).append(" ) as percentualTRT, ")
    	 		.append("  	( ").append( percentualMinimoSubQuery("IdTrt") ).append(" ) as percentualMinTRT, ")
    	 		.append("  	( ").append( percentualSubQuery("IdPedagioFracao") ).append(" ) as percentualPedagioFracao, ")
    	 		.append("  	( ").append( percentualSubQuery("IdPedagioPostoFracao") ).append(" ) as percentualPedagioPostoFracao, ")
    	 		.append("  	( ").append( percentualSubQuery("IdPedagioFaixaPeso") ).append(" ) as percentualPedagioFaixaPeso, ")
    	 		.append("  	( ").append( percentualSubQuery("IdPedagioDocumento") ).append(" ) as percentualPedagioDocumento, ")
    	 		.append("  	( ").append( percentualSubQuery("IDAdvalorem2") ).append(" ) as percentualAdvalorem2, ")
    	 		.append("  	( ").append( valorTabelaSubQuery("IDGris") ).append(" ) as valorTabelaGris, ")
    	 		.append("  	( ").append( valorTabelaSubQuery("IDTde") ).append(" ) as valorTabelaTDE, ")
    	 		.append("  	( ").append( valorTabelaSubQuery("IdTrt") ).append(" ) as valorTabelaTRT, ")
    	 		.append("  	( ").append( valorMinTabelaSubQuery("IDGris") ).append(" ) as valorMinTabelaGris, ")
    	 		.append("  	( ").append( valorMinTabelaSubQuery("IDTde") ).append(" ) as valorMinTabelaTDE, ")
    	 		.append("  	( ").append( valorMinTabelaSubQuery("IdTrt") ).append(" ) as valorMinTabelaTRT, ")
    	 		.append("  	( ").append( valorPedagioSubQuery() ).append(" ) as valorTabelaPedagio, ")
    	 		.append("   rtp.pc_reajuste_geral as percentualMinProgr, rtp.pc_reajuste_geral as percentualFretePeso, rtp.pc_reajuste_geral as percentualAdvalorem, tp.TP_CALCULO_PEDAGIO as tpPedagioPadrao, ")
    	 		.append("   rtp.pc_reajuste_geral as percentualMinFreteQuilo,  rtp.pc_reajuste_geral as percentualTarifaMinima, rtp.pc_reajuste_geral as percentualMinFretePeso, rtp.id_reajuste_tabela_preco as idReajusteTabPreco,  ")
    	 		.append("   rtp.pc_reajuste_geral as percentualMinFrete, rtp.pc_reajuste_geral as percentualTonelada, rtp.pc_reajuste_geral as percentualFrete, ")
    	 		.append("   rtp.id_tabela_preco_base as idTabPrecoBase, rtp.id_tabela_nova as idTabNova, rtp.pc_reajuste_geral as percentualGeral, rtp.pc_reajuste_geral as percentualEspecifica ") 
    	 		.append(" from  tabela_divisao_cliente tdc, reajuste_tabela_preco rtp, tabela_preco tp  ");
    	 		
    	 		if(novoReajuste){
    	 			sql.append(" , reajuste_divisao_cliente rdc ")
    	 			   .append(" where tdc.id_divisao_cliente = rdc.id_divisao_cliente ")
 					   .append(" and tp.id_tabela_preco = rdc.id_tabela_preco_nova ")
 					   .append(" and rdc.dt_agendamento_reajuste > trunc(sysdate) ");
    	 		} else {
    	 			sql.append(" where tdc.bl_atualizacao_automatica = 'S' ")
    	 			   .append(" and rtp.dt_agendamento > trunc(sysdate) ");
    	 		}
    	 		
    	 		sql.append(" and tdc.id_tabela_divisao_cliente = :idDivisaoTabCliente ")
    			   .append(" and tp.id_tabela_preco = rtp.id_tabela_nova")
    			   .append(" and rtp.id_tabela_preco_base = tdc.id_tabela_preco ")
    			   .append(" and rtp.bl_efetivado = 'S'   ")
    			   .append(" and tdc.id_tabela_divisao_cliente not in  ")
    			   .append(" (select hrc.id_tabela_divisao_cliente from historico_reajuste_cliente hrc where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente and hrc.dt_reajuste >= rtp.dt_efetivacao) ");
 		
 		Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idDivisaoTabCliente", idDivisaoTabelaCliente);
 		return (List<ParametroClienteAutomaticoDTO>) getAdsmHibernateTemplate().findBySql(sql.toString(), namedParams, createConfigureSqlQuery(), new AliasToBeanResultTransformer(ParametroClienteAutomaticoDTO.class));
    }
    
    public List<ParametroClienteAutomaticoDTO> percentuaisToReajusteClienteManual(Long idDivisaoTabelaCliente, YearMonthDay dataVigenciaInicial){
    	String sql = new StringBuilder()
				.append(" select  ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualGris,  ")
				.append("   rc.PC_REAJUSTE_ACORDADO as percentualMinGris, 	  ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualTDE,  ")
				.append(" 	rc.PC_REAJUSTE_ACORDADO as percentualMinTDE,   ")
				.append(" 	rc.PC_REAJUSTE_ACORDADO as percentualTRT,   ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualMinTRT,  ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualPedagioFracao,  ")
				.append(" 	rc.PC_REAJUSTE_ACORDADO as percentualPedagioPostoFracao,  ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualPedagioFaixaPeso,  ")
				.append("  	rc.PC_REAJUSTE_ACORDADO as percentualPedagioDocumento,  ")
				.append(" 	rc.PC_REAJUSTE_ACORDADO as percentualAdvalorem2,   ")
				.append("  	( ").append( valorTabelaSubQueryReajusteManual("IDGris") ).append(" ) as valorTabelaGris, ")
				.append("  	( ").append( valorTabelaSubQueryReajusteManual("IDTde") ).append(" ) as valorTabelaTDE, ")
				.append("  	( ").append( valorTabelaSubQueryReajusteManual("IdTrt") ).append(" ) as valorTabelaTRT, ")
				.append("  	( ").append( valorMinTabelaSubQueryReajusteManual("IDGris") ).append(" ) as valorMinTabelaGris, ")
    	 		.append("  	( ").append( valorMinTabelaSubQueryReajusteManual("IDTde") ).append(" ) as valorMinTabelaTDE, ")
    	 		.append("  	( ").append( valorMinTabelaSubQueryReajusteManual("IdTrt") ).append(" ) as valorMinTabelaTRT, ")
    	 		.append("  	( ").append( valorPedagioSubQueryReajusteClienteManual() ).append(" ) as valorTabelaPedagio, ")
				.append("   rc.PC_REAJUSTE_ACORDADO as percentualMinProgr, rc.PC_REAJUSTE_ACORDADO as percentualFretePeso, rc.PC_REAJUSTE_ACORDADO as percentualAdvalorem, tp.TP_CALCULO_PEDAGIO as tpPedagioPadrao,   ")
				.append("   rc.PC_REAJUSTE_ACORDADO as percentualMinFreteQuilo,  rc.PC_REAJUSTE_ACORDADO as percentualTarifaMinima, rc.PC_REAJUSTE_ACORDADO as percentualMinFretePeso, rc.id_reajuste_cliente as idReajusteTabPreco,    ")
				.append("   rc.PC_REAJUSTE_ACORDADO as percentualMinFrete, rc.PC_REAJUSTE_ACORDADO as percentualTonelada, rc.PC_REAJUSTE_ACORDADO as percentualFrete,  ")
				.append("   tdc.id_tabela_preco as idTabPrecoBase, rc.id_tabela_preco as idTabNova, rc.PC_REAJUSTE_ACORDADO as percentualGeral, rc.PC_REAJUSTE_ACORDADO as percentualEspecifica   ")
				.append(" from  tabela_divisao_cliente tdc, reajuste_cliente rc, tabela_preco tp  ")
				.append(" where tdc.id_tabela_divisao_cliente = :idDivisaoTabCliente  ")
				.append("   and tp.id_tabela_preco = rc.id_tabela_preco ")
				.append("   and rc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente   ")
				.append("   and rc.dt_inicio_vigencia = :dataVigenciaInicial   ")				
				.append("   and tdc.id_tabela_divisao_cliente not in   ")
				.append("   (select hrc.id_tabela_divisao_cliente from historico_reajuste_cliente hrc where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente and hrc.dt_reajuste >= rc.dt_efetivacao)  ")
				.toString();
    	
    	Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idDivisaoTabCliente", idDivisaoTabelaCliente);
 		namedParams.put("dataVigenciaInicial", dataVigenciaInicial);
 		return (List<ParametroClienteAutomaticoDTO>) getAdsmHibernateTemplate().findBySql(sql, namedParams, createConfigureSqlQuery(), new AliasToBeanResultTransformer(ParametroClienteAutomaticoDTO.class));
    }
    
    private ConfigureSqlQuery createConfigureSqlQuery(){
    	return new ConfigureSqlQuery() {
 			@Override
 			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
 				sqlQuery.addScalar("percentualGris", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinGris", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualTDE", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinTDE", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualTRT", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinTRT", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualPedagioFracao", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualPedagioPostoFracao", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualPedagioFaixaPeso", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualPedagioDocumento", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualAdvalorem2", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabelaGris", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinTabelaGris", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinTabelaTDE", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabelaTDE", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabelaTRT", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinTabelaTRT", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabelaPedagio", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinProgr", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualFretePeso", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualAdvalorem", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("tpPedagioPadrao", Hibernate.STRING);
 				sqlQuery.addScalar("percentualMinFreteQuilo", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualTarifaMinima", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinFretePeso", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("idReajusteTabPreco", Hibernate.LONG);
 				sqlQuery.addScalar("percentualMinFrete", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualTonelada", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualFrete", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("idTabPrecoBase", Hibernate.LONG);
 				sqlQuery.addScalar("idTabNova", Hibernate.LONG);
 				sqlQuery.addScalar("percentualGeral", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualEspecifica", Hibernate.BIG_DECIMAL);
 			}
 		};
    }
	
    public List<AtualizarReajusteDTO> findDadosAtualizarHistoricoReajuste(Long idDivisaoTabelaCliente){
    	String sql = new StringBuilder()
    	 		.append(" select ")
    	 		.append("   rtp.id_tabela_preco_base as idTabelaBase, rtp.id_tabela_nova as idTabelaNova, rtp.pc_reajuste_geral as pcReajusteGeral ") 
    	 		.append(" from  tabela_divisao_cliente tdc, reajuste_tabela_preco rtp, tabela_preco tp  ")
    	 		.append(" where tdc.id_tabela_divisao_cliente = :idDivisaoTabCliente ")
    	 		.append(" and tdc.BL_ATUALIZACAO_AUTOMATICA = 'S' ")
    	 		.append(" and tp.id_tabela_preco = rtp.id_tabela_nova")
    	 		.append(" and rtp.id_tabela_preco_base = tdc.id_tabela_preco ")
    	 		.append(" and rtp.dt_agendamento > trunc(sysdate) ")
    	 		.append(" and rtp.bl_efetivado = 'S'   ")
    	 		.append(" and tdc.id_tabela_divisao_cliente not in  ")
    	 		.append(" (select hrc.id_tabela_divisao_cliente from historico_reajuste_cliente hrc where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente and hrc.dt_reajuste >= rtp.dt_efetivacao) ")
    	 		.toString();
    	 
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
 			@Override
 			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
 				sqlQuery.addScalar("idTabelaBase", Hibernate.LONG);
 				sqlQuery.addScalar("idTabelaNova", Hibernate.LONG);
 				sqlQuery.addScalar("pcReajusteGeral", Hibernate.BIG_DECIMAL);
 				
 			}
 		};
 		
 		Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idDivisaoTabCliente", idDivisaoTabelaCliente);
 		return (List<AtualizarReajusteDTO>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(AtualizarReajusteDTO.class));
    }
    
    public List<ReajusteGeneralidadeCliente> percentuaisToReajusteGeneralidadeCliente(Long idReajusteTabPreco, Long idParamCliente){
    	String sql = new StringBuilder()
 							.append(" select ")
 							.append(" 		gc.id_generalidade_cliente as idGeneralidadeCliente, ")
 							.append("		pp.id_parcela_preco as idParcelaPreco,  ")
 							.append("       prtp.pc_reajuste_valor_parcela as percentualReajuste,  ")
 							.append("       pc_reajuste_val_min_parcela as percentualMinReajuste, ")
 							.append("       g.vl_generalidade as valorTabela, ")
 							.append("       g.vl_minimo as valorMinTabela ")
 							.append("  from generalidade_cliente gc, reajuste_tabela_preco rtp, parametro_reajuste_tab_preco prtp, ")
 							.append("       reajuste_tabela_preco_parcela rtpp, tabela_preco_parcela tpp, parcela_preco pp, generalidade g ")
 							.append("  where rtp.id_reajuste_tabela_preco = :idReajusteTabPreco ")
 							.append("    and gc.id_parametro_cliente = :idParamCliente ")
 							.append("    and rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
 							.append("    and tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
 							.append("    and gc.id_parcela_preco = tpp.id_parcela_preco  ")
 							.append("    and pp.id_parcela_preco = gc.id_parcela_preco ")
 							.append("    and g.id_generalidade = tpp.id_tabela_preco_parcela ")
 							.append("    and prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
 							.toString();
   	 
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
 			@Override
 			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
 				sqlQuery.addScalar("idGeneralidadeCliente", Hibernate.LONG);
 				sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
 				sqlQuery.addScalar("percentualReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabela", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinTabela", Hibernate.BIG_DECIMAL);
 			}
 		};
 		
 		Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idReajusteTabPreco", idReajusteTabPreco);
 		namedParams.put("idParamCliente", idParamCliente);
 		return (List<ReajusteGeneralidadeCliente>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(ReajusteGeneralidadeCliente.class));
    }
    
    public List<ReajusteGeneralidadeCliente> percentuaisToReajusteManualGeneralidadeCliente(Long idParamCliente){
    	String sql = new StringBuilder()
 							.append(" select ")
 							.append(" 		gc.id_generalidade_cliente as idGeneralidadeCliente,  ")
 							.append("		pp.id_parcela_preco as idParcelaPreco,    ")
 							.append("       rc.PC_REAJUSTE_ACORDADO as percentualReajuste,   ")
 							.append("       rc.PC_REAJUSTE_ACORDADO as percentualMinReajuste, ")
 							.append("       g.vl_generalidade as valorTabela, ")
 							.append("       g.vl_minimo as valorMinTabela  ")
 							.append("  from generalidade_cliente gc, parcela_preco pp, generalidade g, tabela_preco_parcela tpp,   ")
 							.append("       parametro_cliente pc, tabela_divisao_cliente tdc, reajuste_cliente rc ")
 							.append("  where pc.id_parametro_cliente = :idParamCliente ")
 							.append("    and gc.id_parametro_cliente = pc.id_parametro_cliente ")
 							.append("    and tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente ")
 							.append("    and tpp.id_tabela_preco = tdc.id_tabela_preco ")
 							.append("    and tpp.id_parcela_preco = gc.id_parcela_preco ")
 							.append("    and pp.id_parcela_preco = gc.id_parcela_preco ")
 							.append("    and g.id_generalidade = tpp.id_tabela_preco_parcela ")
 							.append("    and rc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente   ")
 							.append("    and trunc(rc.dt_inicio_vigencia) = trunc(pc.dt_vigencia_inicial) ")
 							.toString();
   	 
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
 			@Override
 			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
 				sqlQuery.addScalar("idGeneralidadeCliente", Hibernate.LONG);
 				sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
 				sqlQuery.addScalar("percentualReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorTabela", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinTabela", Hibernate.BIG_DECIMAL);
 			}
 		};
 		
 		Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idParamCliente", idParamCliente);
 		return (List<ReajusteGeneralidadeCliente>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(ReajusteGeneralidadeCliente.class));
    }
    
    public List<ReajusteServicoAdicionalCliente> percentuaisToReajusteServicoAdiconalCliente(Long idDivisaoTabelaCliente, Long idReajuste){
    	String sql = new StringBuilder()
 							.append(" select ")
 							.append(" 		sac.id_servico_adicional_cliente as idServAdicionalCliente , ")
 							.append(" 		pp.id_parcela_preco as  idParcelaPreco , ")
 							.append(" 	    prtp.pc_reajuste_valor_parcela as percentualReajuste, ")
 							.append(" 	    pc_reajuste_val_min_parcela as percentualMinReajuste, ")
 							.append("       sac.vl_minimo as valorMinReajuste, ")
 							.append(" 	    sac.vl_valor as valorReajuste, ")
 							.append(" 	    sac.tp_indicador as tpIndicador ")
 							.append(" from  servico_adicional_cliente sac, reajuste_tabela_preco rtp, REAJUSTE_TABELA_PRECO_PARCELA rtpp, 	")
 							.append(" 	    tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp ")
 							.append(" where rtp.id_reajuste_tabela_preco = :idReajuste	")
 							.append("   and rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
 							.append(" 	and tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
 							.append("   and sac.id_parcela_preco = tpp.id_parcela_preco  ")
 							.append("   and sac.ID_TABELA_DIVISAO_CLIENTE = :idDivisaoTabelaCliente ")
 							.append("   and pp.id_parcela_preco = sac.id_parcela_preco  ")
 							.append(" 	and prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
 							.toString();
   	 
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
 			@Override
 			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
 				sqlQuery.addScalar("idServAdicionalCliente", Hibernate.LONG);
 				sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
 				sqlQuery.addScalar("percentualReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("percentualMinReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorMinReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("valorReajuste", Hibernate.BIG_DECIMAL);
 				sqlQuery.addScalar("tpIndicador", Hibernate.STRING);
 			}
 		};
 		
 		Map<String, Object> namedParams = new HashMap<String, Object>();
 		namedParams.put("idDivisaoTabelaCliente", idDivisaoTabelaCliente);
 		namedParams.put("idReajuste", idReajuste);
 		return (List<ReajusteServicoAdicionalCliente>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(ReajusteServicoAdicionalCliente.class));
    }

    public List<ReajusteTaxaCliente> percentuaisToReajusteTaxaCliente(Long idReajusteTabPreco, Long idParamCliente){
    	String sql = new StringBuilder()
    	.append(" select tc.id_taxa_cliente as idTaxaCliente, pp.id_parcela_preco as idParcelaPreco, ")
    	.append(" prtp.pc_reajuste_valor_parcela as percentualReajuste, pc_reajuste_val_min_parcela as percentualMinReajuste ")
    	.append(" from taxa_cliente tc, reajuste_tabela_preco rtp, REAJUSTE_TABELA_PRECO_PARCELA rtpp, tabela_preco_parcela tpp, parcela_preco pp, parametro_reajuste_tab_preco prtp ")
    	.append(" where rtp.id_reajuste_tabela_preco = :idTabela ")
    	.append(" and   rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
    	.append(" and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
    	.append(" and   tc.id_parcela_preco = tpp.id_parcela_preco  ")
    	.append(" and   tc.id_parametro_cliente = :idParametro ")
    	.append(" and   pp.id_parcela_preco = tc.id_parcela_preco  ")
    	.append(" and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
    	.toString();

    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
    		@Override
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idTaxaCliente", Hibernate.LONG);
    			sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
    			sqlQuery.addScalar("percentualReajuste", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("percentualMinReajuste", Hibernate.BIG_DECIMAL);
    		}
    	};

    	Map<String, Object> namedParams = new HashMap<String, Object>();
    	namedParams.put("idTabela", idReajusteTabPreco);
    	namedParams.put("idParametro", idParamCliente);
    	return (List<ReajusteTaxaCliente>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(ReajusteTaxaCliente.class));
    }
    
	private String percentualMinimoSubQuery(String codParcela){
		return new StringBuilder()
				.append(" select prtp.pc_reajuste_val_min_parcela ")
				.append(" from parametro_reajuste_tab_preco prtp, REAJUSTE_TABELA_PRECO_PARCELA rtpp, tabela_preco_parcela tpp, parcela_preco pp ")
				.append(" where rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
				.append("   and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
				.append("   and   pp.id_parcela_preco = tpp.id_parcela_preco ")
				.append("   and   pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append("   and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ") 
				.toString();
	}
	
	private String percentualSubQuery(String codParcela){
		return new StringBuilder()
				.append(" select prtp.pc_reajuste_valor_parcela ")
				.append(" from parametro_reajuste_tab_preco prtp, REAJUSTE_TABELA_PRECO_PARCELA rtpp, tabela_preco_parcela tpp, parcela_preco pp ")
				.append(" where rtpp.id_reajuste_tabela_preco = rtp.id_reajuste_tabela_preco ")
				.append("   and   tpp.id_tabela_preco_parcela = rtpp.id_tabela_preco_parcela ")
				.append("   and   pp.id_parcela_preco = tpp.id_parcela_preco ")
				.append("   and   pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append("   and   prtp.id_reajuste_tab_preco_parcela = rtpp.id_reajuste_tab_preco_parcela ")
				.toString();
	}
	
	private String valorPedagioSubQuery(){
		return new StringBuilder()
				.append(" case when tp.TP_CALCULO_PEDAGIO = 'P'  ")
				.append(" 	then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp	 ")
				.append("         where pp.cd_parcela_preco = 'IdPedagioPostoFracao' and   tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)  ")
				.append("   else case when tp.TP_CALCULO_PEDAGIO = 'F' ")
				.append("         then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("               where pp.cd_parcela_preco = 'IdPedagioFracao' and tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)  ")
				.append("         else case when tp.TP_CALCULO_PEDAGIO = 'D'  ")
				.append("              then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("                    where pp.cd_parcela_preco = 'IdPedagioDocumento' and tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)   ")
				.append("              else case when tp.TP_CALCULO_PEDAGIO = 'X' ")
				.append("                   then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("                         where pp.cd_parcela_preco = 'IdPedagioFaixaPeso' and tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA) ")
				.append("                   end  ")
				.append("              end ")
				.append("         end ")
				.append("   end ")
				.toString();
	}
	
	private String valorPedagioSubQueryReajusteClienteManual(){
		return new StringBuilder()
				.append(" case when tp.TP_CALCULO_PEDAGIO = 'P'  ")
				.append(" 	then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp	 ")
				.append("         where pp.cd_parcela_preco = 'IdPedagioPostoFracao' and   tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)  ")
				.append("   else case when tp.TP_CALCULO_PEDAGIO = 'F' ")
				.append("         then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("               where pp.cd_parcela_preco = 'IdPedagioFracao' and tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)  ")
				.append("         else case when tp.TP_CALCULO_PEDAGIO = 'D'  ")
				.append("              then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("                    where pp.cd_parcela_preco = 'IdPedagioDocumento' and tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA)   ")
				.append("              else case when tp.TP_CALCULO_PEDAGIO = 'X' ")
				.append("                   then (select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp ")
				.append("                         where pp.cd_parcela_preco = 'IdPedagioFaixaPeso' and tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA) ")
				.append("                   end  ")
				.append("              end ")
				.append("         end ")
				.append("   end ")
				.toString();
	}
	
	private String valorTabelaSubQuery(String codParcela){
		return new StringBuilder()
				.append(" select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp  ")
				.append(" where pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append(" and tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA  ")
				.toString();
	}
	
	private String valorTabelaSubQueryReajusteManual(String codParcela){
		return new StringBuilder()
				.append(" select g.vl_generalidade from tabela_preco_parcela tpp, generalidade g, parcela_preco pp  ")
				.append(" where pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append(" and tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA  ")
				.toString();
	}

	private String valorMinTabelaSubQuery(String codParcela){
		return new StringBuilder()
				.append(" select g.vl_minimo from tabela_preco_parcela tpp, generalidade g, parcela_preco pp  ")
				.append(" where pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append(" and tpp.id_tabela_preco = rtp.id_tabela_preco_base and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA  ")
				.toString();
	}
	
	private String valorMinTabelaSubQueryReajusteManual(String codParcela){
		return new StringBuilder()
				.append(" select g.vl_minimo from tabela_preco_parcela tpp, generalidade g, parcela_preco pp  ")
				.append(" where pp.cd_parcela_preco = '").append(codParcela).append("' ")
				.append(" and tpp.id_tabela_preco = tdc.id_tabela_preco and tpp.id_parcela_preco = pp.ID_PARCELA_PRECO and g.ID_GENERALIDADE = tpp.ID_TABELA_PRECO_PARCELA  ")
				.toString();
	}

	public void updateTabelaDivisao(AtualizarReajusteDTO tabelaDivisao) {
		
		String sql = new StringBuilder()
			.append(" UPDATE TABELA_DIVISAO_CLIENTE SET ")
			.append(" ID_TABELA_PRECO = :idTabelaNova , ")
			.append(" PC_AUMENTO = :pcReajusteGeral ")
			.append(" where id_tabela_divisao_cliente = :idTabelaDivisao ")
			.toString();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaDivisao", tabelaDivisao.getIdTabelaDivisao());
		params.put("idTabelaNova", tabelaDivisao.getIdTabelaNova());
		params.put("pcReajusteGeral", tabelaDivisao.getPcReajusteGeral());
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);
	}

	public void updateTabelaDivisaoFOB(AtualizarReajusteDTO tabelaDivisao) {

		String sql = new StringBuilder()
			.append(" UPDATE TABELA_DIVISAO_CLIENTE SET ")
			.append(" ID_TABELA_PRECO_FOB = :idTabelaNova ")
			.append(" WHERE  id_tabela_divisao_cliente = :idTabelaDivisao ")
			.toString();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaDivisao", tabelaDivisao.getIdTabelaDivisao());
		params.put("idTabelaNova", tabelaDivisao.getIdTabelaNova());
		params.put("pcReajusteGeral", tabelaDivisao.getPcReajusteGeral());
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);
	}

	public void insertHistoricoReajuste(AtualizarReajusteDTO tabelaDivisao, String formaReajuste) {

		String sql = new StringBuilder()
		.append(" INSERT INTO historico_reajuste_cliente (ID_HISTORICO_REAJUSTE_CLIENTE, DT_REAJUSTE,   ")
		.append(" ID_TABELA_DIVISAO_CLIENTE, ID_TABELA_PRECO_NOVA,  ")
		.append(" ID_TABELA_PRECO_ANTERIOR, PC_REAJUSTE, TP_FORMA_REAJUSTE) ")
		.append("  VALUES  ")
		.append(" (historico_reajuste_cliente_sq.nextval, trunc(sysdate), ?,?,?,?,?) ").toString();
    	jdbcTemplate.update(sql, new Object[]{
    			tabelaDivisao.getIdTabelaDivisao(), 
    			tabelaDivisao.getIdTabelaNova(), 
    			tabelaDivisao.getIdTabelaBase(), 
    			tabelaDivisao.getPcReajusteGeral(),
    			formaReajuste
    	});
	}	

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<AtualizarReajusteDTO> findTabelasDivisaoComReajusteParaADataAtual(boolean isNovoReajuste) {

		StringBuilder sql = new StringBuilder();
		
		if(isNovoReajuste){
			sql.append(" select tdc.id_tabela_divisao_cliente as idTabelaDivisao, ")
			.append(" 			rtp.ID_TABELA_PRECO_BASE as idTabelaBase,  ")
			.append(" 			rtp.id_tabela_nova as idTabelaNova,  ")
			.append(" 			rtp.PC_REAJUSTE_GERAL as pcReajusteGeral,  ")
			.append(" 		(select tdc1.id_tabela_preco_fob  ")
			.append(" 		from    tabela_divisao_cliente tdc1 ")
			.append(" 		where   tdc1.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ")
			.append(" 		and     tdc1.id_tabela_preco_fob = rtp.ID_TABELA_PRECO_BASE) as idTabelaFob ")
			.append(" 		from reajuste_divisao_cliente rdc, reajuste_tabela_preco rtp, tabela_divisao_cliente tdc ")
			.append(" 		where rtp.bl_efetivado = 'S' ")
			.append("     and  rdc.bl_reajustada = 'S' ")
			.append(" 		and   rdc.dt_agendamento_reajuste = trunc(sysdate) ")
			.append("     and rdc.id_divisao_cliente = tdc.ID_DIVISAO_CLIENTE ")
			.append("     and tdc.ID_TABELA_PRECO = rtp.ID_TABELA_PRECO_BASE ")
			.append("     and rdc.id_tabela_preco_nova = rtp.ID_TABELA_NOVA ")
			.append(" 		and  (tdc.ID_TABELA_PRECO = rtp.ID_TABELA_PRECO_BASE or tdc.id_tabela_preco_fob = rtp.ID_TABELA_PRECO_BASE) ")
			.append(" 		and   tdc.id_tabela_divisao_cliente not in (select hrc.id_tabela_divisao_cliente ")
			.append(" 		                  from historico_reajuste_cliente hrc ")
			.append(" 		                  where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ")
			.append(" 		                  and   hrc.dt_reajuste >= rtp.dt_efetivacao) ")
			.append(" 		order by tdc.id_tabela_divisao_cliente ") ;
			
		}else{
			sql.append(" select tdc.id_tabela_divisao_cliente as idTabelaDivisao, ")
			.append(" 	rtp.ID_TABELA_PRECO_BASE as idTabelaBase,  ")
			.append(" 	rtp.id_tabela_nova as idTabelaNova,  ")
			.append(" 	rtp.PC_REAJUSTE_GERAL as pcReajusteGeral,  ")
			.append(" (select tdc1.id_tabela_preco_fob  ")
			.append(" from    tabela_divisao_cliente tdc1 ")
			.append(" where   tdc1.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ")
			.append(" and     tdc1.id_tabela_preco_fob = rtp.ID_TABELA_PRECO_BASE) as idTabelaFob ")
			.append(" from reajuste_tabela_preco rtp, tabela_divisao_cliente tdc ")
			.append(" where rtp.bl_efetivado = 'S' ")
			.append(" and   rtp.dt_agendamento = trunc(sysdate) ")
			.append(" and  (tdc.ID_TABELA_PRECO = rtp.ID_TABELA_PRECO_BASE or tdc.id_tabela_preco_fob = rtp.ID_TABELA_PRECO_BASE) ")
			.append(" and   tdc.BL_ATUALIZACAO_AUTOMATICA = 'S' ")
			.append(" and   tdc.id_tabela_divisao_cliente not in (select hrc.id_tabela_divisao_cliente ")
			.append("                   from historico_reajuste_cliente hrc ")
			.append("                   where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ")
			.append("                   and   hrc.dt_reajuste >= rtp.dt_efetivacao) ")
			.append(" order by tdc.id_tabela_divisao_cliente  ");
			
		}
		
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
    		@Override
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idTabelaDivisao", Hibernate.LONG);
    			sqlQuery.addScalar("idTabelaBase", Hibernate.LONG);
    			sqlQuery.addScalar("idTabelaNova", Hibernate.LONG);
    			sqlQuery.addScalar("pcReajusteGeral", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("idTabelaFob", Hibernate.LONG);
    		}
    	};

    	Map<String, Object> namedParams = new HashMap<String, Object>();
    	return (List<AtualizarReajusteDTO>) getAdsmHibernateTemplate().findBySql(sql.toString(), namedParams, configSql, new AliasToBeanResultTransformer(AtualizarReajusteDTO.class));
		
	}

	public List<ReajusteTaxaCliente> percentuaisToReajusteTaxaClienteManual(
			Long idParamCliente) {

		String sql = new StringBuilder().
    	append(" select "). 
    	append(" 			 		tc.id_taxa_cliente as idTaxaCliente, "). 
    	append(" 									pp.id_parcela_preco as idParcelaPreco, "). 
    	append("  							       rc.PC_REAJUSTE_ACORDADO as percentualReajuste, "). 
    	append(" 							       rc.PC_REAJUSTE_ACORDADO as percentualMinReajuste, "). 
    	append("  							       t.vl_taxa as valorTabela, "). 
    	append("  							       t.vl_excedente as valorMinTabela "). 
    	append("  							  from taxa_cliente tc, parcela_preco pp, valor_taxa t, tabela_preco_parcela tpp, "). 
    	append("  							       parametro_cliente pc, tabela_divisao_cliente tdc, reajuste_cliente rc "). 
    	append("  							  where pc.id_parametro_cliente = :idParametro "). 
    	append("  							    and tc.id_parametro_cliente = pc.id_parametro_cliente "). 
    	append("  							    and tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente "). 
    	append("  							    and tpp.id_tabela_preco = tdc.id_tabela_preco  "). 
    	append("  							    and tpp.id_parcela_preco = tc.id_parcela_preco "). 
    	append(" 							    and pp.id_parcela_preco = tc.id_parcela_preco "). 
    	append("  							    and t.id_valor_taxa = tpp.id_tabela_preco_parcela "). 
    	append("  							    and rc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente "). 
    	append("  							    and trunc(rc.dt_inicio_vigencia) = trunc(pc.dt_vigencia_inicial) "). 
    	       	toString();

    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
    		@Override
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idTaxaCliente", Hibernate.LONG);
    			sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
    			sqlQuery.addScalar("percentualReajuste", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("percentualMinReajuste", Hibernate.BIG_DECIMAL);
    		}
    	};

    	Map<String, Object> namedParams = new HashMap<String, Object>();
    	namedParams.put("idParametro", idParamCliente);
    	return (List<ReajusteTaxaCliente>) getAdsmHibernateTemplate().findBySql(sql, namedParams, configSql, new AliasToBeanResultTransformer(ReajusteTaxaCliente.class));
	}

}