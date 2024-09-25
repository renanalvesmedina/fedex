package com.mercurio.lms.tabelaprecos.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPrecoParcela;

public class ReajusteTabelaPrecoParcelaDAO extends BaseCrudDao<ReajusteTabelaPrecoParcela, Long> {

	private JdbcTemplate jdbcTemplate;

	
	public List<Long> listParcelasReajusteById(Long idReajuste) {
		return jdbcTemplate.query(queryListParcelasReajusteById(), new Object[]{idReajuste},  listIdsRowMapper());
	}
	
	public List<Long> listIdsReajusteTabelaPrecoParcela(Long idReajuste) {
		return jdbcTemplate.query(queryIdsReajusteTabelaPrecoParcela(whereIdsReajusteTabelaPrecoParcela(idReajuste)), listIdsRowMapper());
	}
	
	public List<Long> listIdsReajusteTabelaPrecoParcela(Long idReajuste, List<Long> listIdsParcelas) {
		return jdbcTemplate.query(queryIdsReajusteTabelaPrecoParcela(whereIdsReajusteTabelaPrecoParcela(listIdsParcelas, idReajuste)), listIdsRowMapper());
	}
	
	private String queryListParcelasReajusteById(){
		return new StringBuilder().append(" select id_tabela_preco_parcela from reajuste_tabela_preco_parcela where id_reajuste_tabela_preco = ? ").toString();
	}
	
	private String queryIdsReajusteTabelaPrecoParcela(String where){
		return new StringBuilder().append(" select id_reajuste_tab_preco_parcela from reajuste_tabela_preco_parcela ").append(where).toString();
	}
	
	private String whereIdsReajusteTabelaPrecoParcela(Long idReajuste) {
		return new StringBuilder().append(" where id_reajuste_tabela_preco = ").append(idReajuste).toString();
	}
	
	private String whereIdsReajusteTabelaPrecoParcela(List<Long> ids, Long idReajuste) {
		StringBuilder where 	  = new StringBuilder();
		StringBuilder idsParcelas = new StringBuilder();
		
		where.append(" where id_reajuste_tabela_preco = ").append(idReajuste);
		
		Iterator iterator = ids.iterator();
		while(iterator.hasNext()){
			idsParcelas.append(" id_tabela_preco_parcela = ").append(iterator.next()).append(iterator.hasNext() ? " or " : "" );
		}	

		if(StringUtils.isNotBlank(idsParcelas.toString())){
			where.append(" and ( ").append(idsParcelas).append(" ) ");
		}

		return where.toString();
	}
	
	private RowMapper listIdsRowMapper() {
		return new RowMapper(){
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return Long.valueOf(resultSet.getLong(1));
			}
		};
	}
	

	public List<ReajusteParametroParcelaDTO> findIdsParcelasReajuste(Long idReajuste){
		
		ParcelasReajusteTabela parcelasReajusteTabela = new ParcelasReajusteTabela();
		
		String query = parcelasReajusteTabela.mountSql();

		Map<String, Object> namedParams = parcelasReajusteTabela.params(idReajuste);

		return (List<ReajusteParametroParcelaDTO>) getAdsmHibernateTemplate().findBySql(query, namedParams, 
				parcelasReajusteTabela.configureSqlQuery(), new AliasToBeanResultTransformer(ReajusteParametroParcelaDTO.class));
	}
	
	static class ParcelasReajusteTabela {
	
		private Map<String, Object> params( Long idReajusteTabela) {
			Map<String, Object> namedParams = new HashMap<String, Object>();
	    	namedParams.put("idTabela", idReajusteTabela);
	    	return namedParams;
		}
		
		private String mountSql() {
			
			StringBuilder sqlFindParcelas = new StringBuilder();
			
			sqlFindParcelas.append(" select rtp.ID_REAJUSTE_TAB_PRECO_PARCELA as idReajusteTabelaPrecoParcela, rtp.ID_TABELA_PRECO_PARCELA  as idTabelaPrecoParcela, ");
			sqlFindParcelas.append(" prtp.ID_PARAMETRO_REAJ_TAB_PRECO as idParametro, ");
			sqlFindParcelas.append(" prtp.ID_PRECO_FRETE as idPrecoFrete, ");
			sqlFindParcelas.append(" prtp.ID_VALOR_FAIXA_PROGRESSIVA as idFaixaProgressiva, ");
			sqlFindParcelas.append(" prtp.PC_REAJUSTE_VALOR_PARCELA as pcParcela, ");
			sqlFindParcelas.append(" prtp.PC_REAJUSTE_VAL_MIN_PARCELA as pcMinParcela, ");
			sqlFindParcelas.append(" prtp.VALOR_PARCELA as valorParcela, ");
			sqlFindParcelas.append(" prtp.VALOR_MINIMO_PARCELA as valorMinParcela ");
			
			sqlFindParcelas.append(" from REAJUSTE_TABELA_PRECO_PARCELA rtp  ");
			sqlFindParcelas.append(" join PARAMETRO_REAJUSTE_TAB_PRECO prtp ");
			sqlFindParcelas.append(" on rtp.ID_REAJUSTE_TAB_PRECO_PARCELA = prtp.ID_REAJUSTE_TAB_PRECO_PARCELA ");
			sqlFindParcelas.append(" where rtp.ID_REAJUSTE_TABELA_PRECO = :idTabela ");
			
			return sqlFindParcelas.toString();
		}
	
		private ConfigureSqlQuery configureSqlQuery() {
			return new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("idReajusteTabelaPrecoParcela", Hibernate.LONG);
					sqlQuery.addScalar("idTabelaPrecoParcela", Hibernate.LONG);

					sqlQuery.addScalar("idParametro", Hibernate.LONG);
					sqlQuery.addScalar("idPrecoFrete", Hibernate.LONG);
					sqlQuery.addScalar("idFaixaProgressiva", Hibernate.LONG);
					sqlQuery.addScalar("pcParcela", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("pcMinParcela", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("valorParcela", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("valorMinParcela", Hibernate.BIG_DECIMAL);
				}
			};
		}
	}
	
	@Override
	protected Class getPersistentClass() {
		return ReajusteTabelaPrecoParcela.class;
	} 
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}



