package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ComissaoGarantida;

public class ComissaoGarantidaDAO extends BaseCrudDao<ComissaoGarantida, Long> {

	@Override
	protected Class getPersistentClass() {
		return ComissaoGarantida.class;
	}

	@Override
	public ComissaoGarantida findById(Long id) {
		return findByIdByCriteria(createCriteria(), "idComissaoGarantida", id);
	}

	public List<ComissaoGarantida> find(ComissaoGarantida comissaoGarantida) {
		return createCriteria(comissaoGarantida).list();
	}

	public Integer findCount(ComissaoGarantida comissaoGarantida) {
		return rowCountByCriteria(createCriteria(comissaoGarantida));
	}

	private Criteria createCriteria(ComissaoGarantida comissaoGarantida) {
		Criteria criteria = createCriteria();
		
		YearMonthDay dtInicio = comissaoGarantida.getDtInicio();
		if (dtInicio != null) {
			criteria.add(Restrictions.eq("dtInicio", dtInicio));
		}

		YearMonthDay dtFim = comissaoGarantida.getDtFim();
		if (dtFim != null) {
			criteria.add(Restrictions.eq("dtFim", dtFim));
		}

		if (comissaoGarantida.getExecutivoTerritorio() != null) {
			criteria.add(Restrictions.eq("executivoTerritorio.idExecutivoTerritorio", comissaoGarantida.getExecutivoTerritorio().getIdExecutivoTerritorio()));
		}

		return criteria;
	}
	
	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		return criteria;
	}

}
