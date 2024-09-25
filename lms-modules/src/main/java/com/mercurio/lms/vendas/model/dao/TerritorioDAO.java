package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class TerritorioDAO extends BaseCrudDao<Territorio, Long> {

	@Override
	protected final Class getPersistentClass() {
		return Territorio.class;
	}

	@Override
	public Territorio findById(Long id) {
		return super.findById(id);
	};

	public Territorio findByIdInitLazyProperties(Long id, Boolean initializeLazyProperties) {
		return super.findByIdInitLazyProperties(id, initializeLazyProperties);
	};

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
	}	

	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		criteria.createAlias("regional", "regional", Criteria.LEFT_JOIN);
		criteria.createAlias("filial", "filial", Criteria.LEFT_JOIN);
		criteria.createAlias("filial.pessoa", "filial_pessoa", Criteria.LEFT_JOIN);
		return criteria;
	}
	

	public List<Territorio> find(Long idRegional, Long idFilial, String nmTerritorio, DmStatusEnum tpSituacao) {
		Criteria criteria = createCriteria();
		
		List<Criterion> criterionList = createCriterions(idRegional, idFilial, nmTerritorio, tpSituacao);
		if (criterionList != null) {
	    	for (Criterion criterion : criterionList) {
	    		criteria.add(criterion);
			}
		}
		criteria.addOrder(Order.asc("regional.dsRegional"));
		criteria.addOrder(Order.asc("filial_pessoa.nmFantasia"));
		criteria.addOrder(Order.asc("nmTerritorio"));
		
		return criteria.list();
	}

	public Integer findCount(Long idRegional, Long idFilial, String nmTerritorio, DmStatusEnum tpSituacao) {
		return rowCountByCriteria(createCriterions(idRegional, idFilial, nmTerritorio, tpSituacao));
	}
	
	public Territorio findByNomeESituacao(String nmTerritorio, DomainValue tpSituacao) {
		StringBuilder sbHQL = new StringBuilder();
		sbHQL.append(" FROM Territorio t ");
		sbHQL.append(" WHERE t.tpSituacao = :tpSituacao");
		sbHQL.append("    AND upper(t.nmTerritorio) = :nmTerritorio");
		
		Session session = getSession(false);
		session.setFlushMode(FlushMode.COMMIT);
		
		Territorio territorio = (Territorio) session.createQuery(sbHQL.toString())
				.setParameter("tpSituacao", tpSituacao.getValue())
				.setString("nmTerritorio", StringUtils.upperCase(nmTerritorio))
				.uniqueResult();
		
		return territorio;
	}

	private List<Criterion> createCriterions(Long idRegional, Long idFilial, String nmTerritorio, DmStatusEnum tpSituacao) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (idRegional != null) {
			criterions.add(Restrictions.eq("regional.idRegional", idRegional));
		}

		if (idFilial != null) {
			criterions.add(Restrictions.eq("filial.idFilial", idFilial));
		}

		if (nmTerritorio != null && !nmTerritorio.isEmpty()) {
			criterions.add(Restrictions.ilike("nmTerritorio", nmTerritorio, MatchMode.ANYWHERE));
		}

		if (tpSituacao  != null) {
			criterions.add(Restrictions.eq("tpSituacao", tpSituacao.getDomainValue()));
		} else {
			criterions.add(Restrictions.eq("tpSituacao", DmStatusEnum.ATIVO.getDomainValue()));
		}

		return criterions;
	}

	@Override
	public ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		String nmTerritorio = (String) filter.get("nmTerritorio");

		StringBuilder sql = new StringBuilder();
		sql.append(" select id_territorio, nm_territorio from " + Territorio.class.getSimpleName());
		sql.append(" where tp_situacao = '" + DmStatusEnum.ATIVO.getValue() + "' ");

		if (nmTerritorio != null && !nmTerritorio.isEmpty()) {
			sql.append(" and lower(nm_territorio) like '%" + nmTerritorio.toLowerCase() + "%' ");
		}

		return new ResponseSuggest(sql.toString(), filter);
	}

	
}
