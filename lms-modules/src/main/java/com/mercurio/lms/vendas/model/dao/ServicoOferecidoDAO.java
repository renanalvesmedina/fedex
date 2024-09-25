package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ServicoOferecido;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoOferecidoDAO extends BaseCrudDao<ServicoOferecido, Long>
{

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("servico", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoOferecido.class;
    }

    /**
     * Remove os registros pertencentes à etapaVisita onde o
     * idEtapaVisita é recebido como parâmetro
     * @param idVisita
     */
    public void removeByIdEtapaVisita(Long idEtapaVisita){
        String hql = " delete from " + ServicoOferecido.class.getName() + " as so where so.etapaVisita.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idEtapaVisita);
        getAdsmHibernateTemplate().flush();
    }


}