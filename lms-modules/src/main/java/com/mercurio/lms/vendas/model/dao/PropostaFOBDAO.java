package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.PropostaFOB;

public class PropostaFOBDAO extends BaseCrudDao<PropostaFOB, Long> {

	@Override
	protected Class getPersistentClass() {
		return PropostaFOB.class;
	}

	public List<PropostaFOB> findByCliente(Long idCliente){
		
		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("tabelaPreco", "tab")
		.createAlias("tab.tipoTabelaPreco", "ttp")
		.createAlias("tab.subtipoTabelaPreco", "stt")
		.createAlias("cliente", "cli")
		.createAlias("municipio", "mun")
		.add(Restrictions.eq("cliente.id", idCliente));
		
		return findByDetachedCriteria(dc);
	}
	
	
}
