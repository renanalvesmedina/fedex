package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CIOTControleCarga;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CIOTControleCargaDAO extends BaseCrudDao<CIOTControleCarga, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CIOTControleCarga.class;
	}
	
	public CIOTControleCarga findByIdForMonitoramento(Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append("INNER JOIN FETCH ccc.ciot c ");
		hql.append("INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append("INNER JOIN FETCH c.meioTransporte m ");
		hql.append("INNER JOIN FETCH cc.filialByIdFilialOrigem f ");
		hql.append("LEFT JOIN FETCH cc.proprietario p ");
		hql.append("LEFT JOIN FETCH p.pessoa pe ");
		hql.append("WHERE ");
		hql.append("ccc.idCIOTControleCarga = ?");
		
		return (CIOTControleCarga) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{id});
	}
	
	public Integer getRowCountMonitoramento(TypedFlatMap criteria) {
		String sql = getSqlPaginatedMonitoramento(criteria, Boolean.TRUE);
		return getAdsmHibernateTemplate().getRowCountBySql(sql, criteria);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginatedMonitoramento(TypedFlatMap criteria) {
		String sql = getSqlPaginatedMonitoramento(criteria, Boolean.FALSE);
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(sql, criteria, getConfigureSqlQueryPaginatedMonitoramento());
	}
	
	private String getSqlPaginatedMonitoramento(TypedFlatMap criteria, Boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("ccc.id_ciot_controle_carga as id ");
		
		if(!isRowCount){
			sql.append(",cc.nr_controle_carga as nrControleCarga ");
			sql.append(",f.sg_filial as sgFilialControleCarga ");
			sql.append(",mp.nr_frota as nrFrota ");
			sql.append(",mp.NR_IDENTIFICADOR as nrIdentificador ");
			sql.append(",c.nr_ciot as nrCIOT ");
			sql.append(",c.nr_cod_verificador as nrCodigoVerificador ");
			sql.append(",VI18N(vd.ds_valor_dominio_i) as tpSituacao ");
			sql.append(",c.ds_observacao as dsObservacao ");
		}
		
		sql.append("FROM ciot c, controle_carga cc, ciot_controle_carga ccc, filial f, meio_transporte mp, dominio d, valor_dominio vd ");
		sql.append("WHERE 1=1 ");
		sql.append("AND c.id_ciot = ccc.id_ciot ");
		sql.append("AND cc.id_controle_carga = ccc.id_controle_carga ");
		sql.append("AND cc.id_filial_origem = f.id_filial ");
		sql.append("AND c.id_meio_transporte = mp.id_meio_transporte ");
		sql.append("AND d.nm_dominio = 'DM_SITUACAO_CIOT' ");
		sql.append("AND d.id_dominio = vd.id_dominio ");
		sql.append("AND c.tp_situacao = vd.vl_valor_dominio ");
		sql.append("");
		
		if(criteria.getLong("idFilial") != null){
			sql.append("AND f.id_filial = :idFilial ");
		}
		
		if(criteria.getLong("idControleCarga") != null){
			sql.append("AND ccc.id_controle_carga = :idControleCarga ");
		}
		
		if(criteria.getDateTime("periodoInicial") != null){
			sql.append("AND TRUNC(c.dh_geracao) >= :periodoInicial ");
		}

		if(criteria.getDateTime("periodoFinal") != null){
			sql.append("AND TRUNC(c.dh_geracao) <= :periodoFinal ");
		}
		
		if(criteria.getString("tpSituacao") != null){
			sql.append("AND c.tp_situacao = :tpSituacao ");
		}
		
		if(!isRowCount){
			sql.append(" ORDER BY f.sg_filial, cc.nr_controle_carga, mp.nr_frota, mp.NR_IDENTIFICADOR");
		}
		return sql.toString();
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryPaginatedMonitoramento(){
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id", Hibernate.LONG);
				sqlQuery.addScalar("nrControleCarga", Hibernate.LONG);
				sqlQuery.addScalar("sgFilialControleCarga", Hibernate.STRING);
				sqlQuery.addScalar("nrFrota", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificador", Hibernate.STRING);
				sqlQuery.addScalar("nrCIOT", Hibernate.LONG);
				sqlQuery.addScalar("nrCodigoVerificador", Hibernate.STRING);
				sqlQuery.addScalar("tpSituacao", Hibernate.STRING);
				sqlQuery.addScalar("dsObservacao", Hibernate.STRING);
			}
		};
		return configSql;
	}
	
	public CIOTControleCarga findByIdControleCarga(Long idControleCarga) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append(" INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append(" INNER JOIN FETCH cc.veiculoControleCargas vcc ");
		hql.append(" INNER JOIN FETCH vcc.meioTransporte ccm ");
		hql.append(" INNER JOIN FETCH ccc.ciot c ");
		hql.append(" INNER JOIN FETCH c.meioTransporte cm ");
		hql.append(" WHERE cc.idControleCarga = ? ");
		hql.append(" AND ccm.idMeioTransporte = cm.idMeioTransporte ");
		
		List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idControleCarga});
		return (result == null || result.isEmpty()) ? null : (CIOTControleCarga) result.get(0);
	}
	
	public CIOTControleCarga findByIdControleCargaIdMeioTransporte(Long idControleCarga, Long idMeioTransporte) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append(" INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append(" INNER JOIN FETCH cc.meioTransporteByIdTransportado ccm ");
		hql.append(" INNER JOIN FETCH ccc.ciot c ");
		hql.append(" INNER JOIN FETCH c.meioTransporte cm ");
		hql.append(" WHERE cc.idControleCarga = ? ");
		hql.append(" AND ccm.idMeioTransporte = ? ");
		hql.append(" AND ccm.idMeioTransporte = cm.idMeioTransporte ");
		
		return (CIOTControleCarga) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idControleCarga, idMeioTransporte});
	}
	
	public CIOTControleCarga findGeradoAPartirDe(Long idMeioTransporte, YearMonthDay data) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append(" INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append(" INNER JOIN FETCH cc.meioTransporteByIdTransportado ccm ");
		hql.append(" INNER JOIN FETCH ccc.ciot c ");
		hql.append(" INNER JOIN FETCH c.meioTransporte cm ");
		hql.append(" WHERE cm.idMeioTransporte = ? ");
		hql.append(" AND TRUNC(c.dhGeracao.value) > ? ");
		hql.append(" AND c.tpSituacao = 'G' ");
		hql.append(" AND cc.tpControleCarga = 'C' ");
		hql.append(" AND ccm.idMeioTransporte = cm.idMeioTransporte ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idMeioTransporte, data});

		if(result != null && result.size() > 0) {
			return (CIOTControleCarga) result.get(0);
		}else{
			return null;
		}
	}
	
	public CIOTControleCarga findGeradoByIdControleCarga(Long idControleCarga) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append(" INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append(" INNER JOIN FETCH ccc.ciot c ");
		hql.append(" WHERE cc.idControleCarga = ? ");
		hql.append(" AND c.tpSituacao = 'G' ");
		
		return (CIOTControleCarga) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idControleCarga});
	}
	
	@SuppressWarnings("unchecked")
	public List<CIOTControleCarga> findByCiotDiferenteControleCarga(Long idCiot, Long idControleCarga) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ccc FROM ");
		hql.append(getPersistentClass().getName()).append(" ccc ");
		hql.append(" INNER JOIN FETCH ccc.controleCarga cc ");
		hql.append(" INNER JOIN FETCH ccc.ciot c ");
		hql.append(" WHERE cc.idControleCarga <> :idControleCarga ");
		hql.append(" AND cc.tpStatusControleCarga <> 'CA' ");
		hql.append(" AND c.idCIOT = :idCiot ");
		
		params.put("idCiot", idCiot);
		params.put("idControleCarga", idControleCarga);

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
}