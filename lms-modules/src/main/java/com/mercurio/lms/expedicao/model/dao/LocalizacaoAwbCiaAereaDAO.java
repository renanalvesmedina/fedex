package com.mercurio.lms.expedicao.model.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.util.IntegerUtils;

public class LocalizacaoAwbCiaAereaDAO extends BaseCrudDao<LocalizacaoAwbCiaAerea, Long>{

	@Override
	protected Class getPersistentClass() {
		return LocalizacaoAwbCiaAerea.class;
	}
	
	public List findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();				
		
		//ORDENAÇÃO: Cia Aérea, Localização do AWB, Localização atual do AWB
		String sql = this.getHqlPaginatedCabecalho() + this.getHqlPaginated(criteria) + " ORDER BY 3, 5, 6 ";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("empresa.idEmpresa", MapUtils.getLong(criteria, "empresa.idEmpresa"));
		parameters.put("dsTracking", MapUtils.getString(criteria, "dsTracking"));
		parameters.put("tpLocalizacaoCiaAerea.value", MapUtils.getString(criteria, "tpLocalizacaoCiaAerea.value"));
		parameters.put("tpLocalizacaoAtual.value", MapUtils.getString(criteria, "tpLocalizacaoAtual.value"));
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_LOCALIZACAO_CIA_AEREA", Hibernate.LONG);
				sqlQuery.addScalar("ID_CIA_AEREA", Hibernate.LONG);
				sqlQuery.addScalar("NM_CIA_AEREA", Hibernate.STRING);
				sqlQuery.addScalar("DS_TRACKING", Hibernate.STRING);
				sqlQuery.addScalar("TP_LOCALIZACAO", Hibernate.STRING);
				sqlQuery.addScalar("TP_LOCALIZACAO_ATUAL", Hibernate.STRING);
			}
		};
			
		ResultSetPage rps = getAdsmHibernateTemplate().findPaginatedBySql(sql, paginatedQuery.getCurrentPage(), paginatedQuery.getPageSize(), parameters, configureSqlQuery);
		
		return rps.getList();
	}

	@Override
	public Integer getRowCount(Map criteria) {				
		return getAdsmHibernateTemplate().getRowCountBySql(this.getHqlPaginated(criteria), criteria);
	}
	
	public String getHqlPaginatedCabecalho() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select LCA.ID_LOCALIZACAO_CIA_AEREA ");
		sql.append(" 	, LCA.ID_CIA_AEREA AS ID_CIA_AEREA ");
		sql.append(" 	, PES.NM_PESSOA AS NM_CIA_AEREA ");
		sql.append(" 	, LCA.DS_TRACKING				");
		sql.append(" 	,(	select VI18N(VM.DS_VALOR_DOMINIO_I)														");
		sql.append(" 		from VALOR_DOMINIO VM																	");
		sql.append(" 		inner join DOMINIO DM on DM.ID_DOMINIO = VM.ID_DOMINIO									");
		sql.append(" 		where DM.NM_DOMINIO = 'DM_TIPO_LOCALIZACAO_AWB'														");
		sql.append(" 			and VM.VL_VALOR_DOMINIO = LCA.TP_LOCALIZACAO									");
		sql.append(" 	) as TP_LOCALIZACAO																			");
		sql.append(" 	,(	select VI18N(VM.DS_VALOR_DOMINIO_I)														");
		sql.append(" 		from VALOR_DOMINIO VM																	");
		sql.append(" 		inner join DOMINIO DM on DM.ID_DOMINIO = VM.ID_DOMINIO									");
		sql.append(" 		where DM.NM_DOMINIO = 'DM_TIPO_LOCALIZACAO_AWB'														");
		sql.append(" 			and VM.VL_VALOR_DOMINIO = LCA.TP_LOCALIZACAO_ATUAL									");
		sql.append(" 	) as TP_LOCALIZACAO_ATUAL																			");

		return sql.toString();
	}
	
	public String getHqlPaginated(Map<String,Object> criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM LOCALIZACAO_CIA_AEREA LCA");
		sql.append(" LEFT JOIN PESSOA PES ON LCA.ID_CIA_AEREA = PES.ID_PESSOA");
		sql.append(" WHERE 1 = 1");
		
		if(criteria.get("empresa.idEmpresa") != null) {
			sql.append(" AND LCA.ID_CIA_AEREA = :empresa.idEmpresa ");			
		}
		if(criteria.get("dsTracking") != null) {			
			sql.append(" AND upper(LCA.DS_TRACKING) LIKE upper('%' || :dsTracking || '%')");			
		}	
		if(criteria.get("tpLocalizacaoCiaAerea.value") != null) {
			sql.append(" AND LCA.TP_LOCALIZACAO = :tpLocalizacaoCiaAerea.value ");			
		}	
		
		if(criteria.get("tpLocalizacaoAtual.value") != null) {			
			sql.append("AND LCA.TP_LOCALIZACAO_ATUAL = :tpLocalizacaoAtual.value ");			
		}
		
		return sql.toString();
	}
	
	public Long store(LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea) {
		super.store(localizacaoAwbCiaAerea);
		return localizacaoAwbCiaAerea.getIdLocalizacaoAwbCiaAerea();
	}

    /**
	 * Recupera uma instância de <code>MacroZona</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public LocalizacaoAwbCiaAerea findById(Long id) {
    	DetachedCriteria dc = DetachedCriteria.forClass(LocalizacaoAwbCiaAerea.class)
    			.setFetchMode("ciaAerea", FetchMode.JOIN)
    			.setFetchMode("ciaAerea.pessoa", FetchMode.JOIN)
    			.add(Restrictions.eq("idLocalizacaoAwbCiaAerea", id));
    	return (LocalizacaoAwbCiaAerea) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

	public List<LocalizacaoAwbCiaAerea> findLocalizacaoesCiaAereaByIdCiaAereaAndTpLocalizacao(Long idCiaAerea) {
		StringBuilder sql = new StringBuilder()
    	.append("select loc ")
    	.append(" from ")
    	.append(LocalizacaoAwbCiaAerea.class.getName()).append(" as loc ")
    	.append(" where ")
    	.append(" loc.ciaAerea.idEmpresa = ? ")
    	.append(" and loc.tpLocalizacaoCiaAerea in ('DR', 'EV', 'FV' )");
    	
    	return (List<LocalizacaoAwbCiaAerea>) getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idCiaAerea});
	}

	public LocalizacaoAwbCiaAerea findByIdCiaAereaAndTpLocalizacao(Long idCiaAerea, DomainValue tpLocalizacao) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT ")
		.append(" laca ")
		.append(" FROM ")
		.append(getPersistentClass().getSimpleName()).append(" laca ")
		.append(" JOIN laca.ciaAerea ciaAerea ")
		.append(" WHERE ")
		.append("	 laca.ciaAerea.idEmpresa = :idCiaAerea ")
		.append("AND laca.tpLocalizacaoCiaAerea = :tpLocalizacao ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idCiaAerea", idCiaAerea);
		criteria.put("tpLocalizacao", tpLocalizacao.getValue());
		
		List<LocalizacaoAwbCiaAerea> l = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
		
		if (!l.isEmpty()) {
			return l.get(IntegerUtils.ZERO);
		}
		
		return null;
	}

}
