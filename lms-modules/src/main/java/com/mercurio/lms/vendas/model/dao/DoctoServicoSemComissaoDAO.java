package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DoctoServicoSemComissao;

public class DoctoServicoSemComissaoDAO extends BaseCrudDao<DoctoServicoSemComissao, Long> {

	@Override
	protected final Class getPersistentClass() {
		return DoctoServicoSemComissao.class;
	}
	
	public Integer findCount(Long idDoctoServico, Long idExecutivo, YearMonthDay dtCompetencia) {
		return rowCountByCriteria(createCriterions(idDoctoServico, idExecutivo, dtCompetencia));
	}

	public List<DoctoServicoSemComissao> find(Long idDoctoServico, Long idExecutivo, YearMonthDay dtCompetencia) {
		return findByCriterion(createCriterions(idDoctoServico, idExecutivo, dtCompetencia), null);
	}
	
	private List<Criterion> createCriterions(
			Long idDoctoServico, 
			Long idExecutivo, 
 			YearMonthDay dtCompetencia 
		) {

		List<Criterion> criterionList = new ArrayList<Criterion>();

		if (idDoctoServico != null) {
			criterionList.add(Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico));
		}

		if (idExecutivo != null) {
			criterionList.add(Restrictions.eq("executivo.idExecutivo", idExecutivo));
		}

		if (dtCompetencia != null) {
			criterionList.add(Restrictions.ge("dtCompetencia", dtCompetencia));
		}

		return criterionList;
	}

}
