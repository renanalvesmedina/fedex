package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.vendas.model.Meta;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class MetaDAO extends BaseCrudDao<Meta, Long> {

	@Override
	protected final Class getPersistentClass() {
		return Meta.class;
	}

	@Override
	public Meta findById(Long id) {
		return findByIdByCriteria("idMeta", id);
	}

	public List<Meta> find(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		return findListByCriteria(createCriterions(idTerritorio, tpModal, nrAno, nmMes));
	}

	public Integer findCount(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		return rowCountByCriteria(createCriterions(idTerritorio, tpModal, nrAno, nmMes));
	}
	
	public Meta findByNaturalKey(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		StringBuilder sbHQL = new StringBuilder();
		sbHQL.append(" FROM Meta m ");
		sbHQL.append(" WHERE m.territorio.idTerritorio = :idTerritorio");
		sbHQL.append("    AND m.tpModal = :tpModal");
		sbHQL.append("    AND m.nrAno = :nrAno");
		sbHQL.append("    AND m.nmMes = :nmMes");
		
		Meta meta = (Meta) getSession().createQuery(sbHQL.toString())
				.setLong("idTerritorio", idTerritorio)
				.setParameter("tpModal", tpModal)
				.setInteger("nrAno", nrAno)
				.setString("nmMes", nmMes)
				.uniqueResult();
		
		return meta;
	}

	private List<Criterion> createCriterions(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		List<Criterion> criterions = new ArrayList<Criterion>();

		if (idTerritorio != null) {
			criterions.add(Restrictions.eq("meta.idTerritorio", idTerritorio));
		}

		if (tpModal != null) {
			criterions.add(Restrictions.eq("meta.tpModal", tpModal.getValue()));
		}
		
		if (nrAno != null) {
			criterions.add(Restrictions.eq("meta.nrAno", nrAno));
		}

		if (nmMes != null) {
			criterions.add(Restrictions.eq("meta.nmMes", nmMes));
		}

		return criterions;
	}
}
