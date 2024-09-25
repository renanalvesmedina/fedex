package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.OcorrenciaEndereco;
import com.mercurio.lms.configuracoes.model.Pessoa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaEnderecoDAO extends BaseCrudDao<OcorrenciaEndereco, Long> {

	protected final Class getPersistentClass() {
		return OcorrenciaEndereco.class;
	}

	/**
	 * Busca OcorrenciaEndereco pelo id da pessoa
	 * @return OcorrenciaEndereco da pessoa
	 */
	public OcorrenciaEndereco findByPessoa(Pessoa pessoa) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("oe");
		sql.addFrom(getPersistentClass().getName() + " oe "
				+ "join fetch oe.pessoa p ");

		sql.addCriteria("p.id", "=", pessoa.getIdPessoa());

		List ret = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria()); 
		
		if (ret == null || ret.isEmpty())
			return null;
		else
			return (OcorrenciaEndereco) ret.get(0);
	}

}