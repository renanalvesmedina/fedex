package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.Observacao;

public class ObservacaoDAO  extends BaseCrudDao< Observacao, Long> {

	@Override
	protected Class getPersistentClass() {
		return Observacao.class;
	}
	
	public List<Observacao> findByIdTabelaPreco(Long idTabelaPreco){

		String hql = new StringBuilder().
			append("   FROM ").
			append(Observacao.class.getName()).
			append("  WHERE nomeTabela = 'TABELA_PRECO'  AND ").
			append(" idTabela = :idTabela ").
			toString();

		 
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("idTabela", idTabelaPreco);
		
		return (List<Observacao>) getAdsmHibernateTemplate().findByNamedParam(hql, params);
	}

}
