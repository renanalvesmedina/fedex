package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.RHCargo;
;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RHCargoDAO extends BaseCrudDao<RHCargo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RHCargo.class;
    }

	public List findCargoMotoristaInstrutor(TypedFlatMap criteria) {
		final StringBuilder sql = new StringBuilder();
		
		sql.append("FROM ").append(RHCargo.class.getName()).append(" vp ")
			.append("WHERE ")
			.append("	vp.codigo in ('025', '026') ")
			.append("ORDER BY vp.nome asc");
	
		return getHibernateTemplate().find(sql.toString());
	}

}
