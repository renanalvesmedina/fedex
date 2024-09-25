package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ParametroPais;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroPaisDAO extends BaseCrudDao<ParametroPais, Long> {
	private String sqlFindById;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ParametroPais.class;
	}

	public ParametroPais findByIdPais(Long idPais, boolean useLock) {
		ParametroPais parametroPais = null;
		Object[] paramValues = new Object[]{idPais};

		if(useLock) {
			parametroPais = (ParametroPais) getAdsmHibernateTemplate().findUniqueResultForUpdate(getSqlFindById(), paramValues, "pp");
		} else {
			parametroPais = (ParametroPais) getAdsmHibernateTemplate().findUniqueResult(getSqlFindById(), paramValues);
		}

		return parametroPais;
	}

	private String getSqlFindById() {
		if(sqlFindById == null) {
			StringBuilder builder = new StringBuilder()
			.append(" from ").append(getPersistentClass().getName()).append(" as pp")
			.append(" where pp.pais.id = ?");

			sqlFindById = builder.toString();
		}
		return sqlFindById;
	}

	
}