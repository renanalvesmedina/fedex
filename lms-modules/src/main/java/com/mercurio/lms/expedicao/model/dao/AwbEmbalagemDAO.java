package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.AwbEmbalagem;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AwbEmbalagemDAO extends BaseCrudDao<AwbEmbalagem, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class<AwbEmbalagem> getPersistentClass() {
        return AwbEmbalagem.class;
    }

    public void removeByIdAwb(Long idAwb) {
    	StringBuilder hql = new StringBuilder()
    	.append(" delete ").append(getPersistentClass().getName())
    	.append(" where awb.id = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idAwb);
    }

}