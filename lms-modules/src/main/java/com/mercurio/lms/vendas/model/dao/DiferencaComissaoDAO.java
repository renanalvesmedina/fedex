package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DiferencaComissao;

public class DiferencaComissaoDAO extends BaseCrudDao<DiferencaComissao, Long> {

	@Override
	protected Class getPersistentClass() {
		return DiferencaComissao.class;
	}

	@Override
	public DiferencaComissao findById(Long id) {
		return findByIdByCriteria(createCriteria(), "idDiferencaComissao", id);
	}

	public List<DiferencaComissao> find(DiferencaComissao diferencaComissao) {
		return createCriteria(diferencaComissao).list();
	}

	public Integer findCount(DiferencaComissao diferencaComissao) {
		return rowCountByCriteria(createCriteria(diferencaComissao));
	}

	private Criteria createCriteria(DiferencaComissao diferencaComissao) {
		Criteria criteria = createCriteria();

		YearMonthDay dtCompetencia = diferencaComissao.getDtCompetencia();
		if (dtCompetencia != null) {
			criteria.add(Restrictions.eq("dtCompetencia", dtCompetencia));
		}

		if (diferencaComissao.getExecutivoTerritorio() != null) {
			criteria.add(Restrictions.eq("executivoTerritorio.idExecutivoTerritorio", diferencaComissao.getExecutivoTerritorio().getIdExecutivoTerritorio()));
		}

		return criteria;
	}
	
	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		return criteria;
	}

}
