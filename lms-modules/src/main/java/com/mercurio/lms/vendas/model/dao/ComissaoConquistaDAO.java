package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.ComissaoConquista;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class ComissaoConquistaDAO extends BaseCrudDao<ComissaoConquista, Long> {

	@Override
	protected Class getPersistentClass() {
		return ComissaoConquista.class;
	}

	@Override
	public ComissaoConquista findById(Long id) {
		return findByIdByCriteria(createCriteria(), "idComissaoConquista", id);
	}

	public List<ComissaoConquista> find(ComissaoConquista comissaoConquista) {
		return createCriteria(comissaoConquista, null).list();
	}
	
	public ComissaoConquista findByIdClienteTerritorio(Long idClienteTerritorio) {
		ClienteTerritorio clienteTerritorio = new ClienteTerritorio();
		clienteTerritorio.setIdClienteTerritorio(idClienteTerritorio);
		ComissaoConquista comissaoConquista = new ComissaoConquista();
		comissaoConquista.setClienteTerritorio(clienteTerritorio);
		return (ComissaoConquista)createCriteria(comissaoConquista, DmStatusEnum.ATIVO).uniqueResult();
	}

	public Integer findCount(ComissaoConquista comissaoConquista) {
		return rowCountByCriteria(createCriteria(comissaoConquista, null));
	}

	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		return criteria;
	}
	
	private Criteria createCriteria(ComissaoConquista comissaoConquista, DmStatusEnum tpSituacao) {
		Criteria criteria = createCriteria();
		
		if (comissaoConquista.getClienteTerritorio() != null) {
			criteria.add(Restrictions.eq("clienteTerritorio.idClienteTerritorio", comissaoConquista.getClienteTerritorio().getIdClienteTerritorio()));
		}

		YearMonthDay dtInicio = comissaoConquista.getDtInicio();
		if (dtInicio != null) {
			criteria.add(Restrictions.eq("dtInicio", dtInicio));
		}
		
		YearMonthDay dtFim = comissaoConquista.getDtFim();
		if (dtFim != null) {
			criteria.add(Restrictions.eq("dtFim", dtFim));
		}
		
		if (tpSituacao != null) {
			criteria.add(Restrictions.eq("tpSituacao", tpSituacao.getDomainValue()));
		} else {
			criteria.add(Restrictions.eq("tpSituacao", DmStatusEnum.ATIVO.getDomainValue()));
		}

		return criteria;
	}
	
}
