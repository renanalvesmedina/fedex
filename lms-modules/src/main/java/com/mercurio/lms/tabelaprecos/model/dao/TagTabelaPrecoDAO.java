package com.mercurio.lms.tabelaprecos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class TagTabelaPrecoDAO extends BaseCrudDao<TagTabelaPreco, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return TagTabelaPreco.class;
	}

	public TagTabelaPreco findByTag(String tag) {
		StringBuilder query = new StringBuilder();
		query.append("FROM TagTabelaPreco t  ");
		query.append("JOIN FETCH t.parcelaPreco pp ");
		query.append("WHERE t.tag = ? ");
		
		TagTabelaPreco resultado = (TagTabelaPreco) super.getAdsmHibernateTemplate().findUniqueResult(query.toString(), new String[] {tag});
		if (resultado == null) {
			return TagTabelaPreco.VAZIO;
		}
		return resultado;
	}
}
