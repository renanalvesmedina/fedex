package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.FuncionarioVisita;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FuncionarioVisitaDAO extends BaseCrudDao<FuncionarioVisita, Long>
{
	protected void initFindListLazyProperties(Map lazyFindList){
		lazyFindList.put("usuario", FetchMode.JOIN);
		lazyFindList.put("usuario.vfuncionario", FetchMode.JOIN);
		
		lazyFindList.put("visita", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FuncionarioVisita.class;
    }

    /**
     * Remove os registros pertencentes à visita onde o id visita é
     * recebido como parâmetro
     * @param idVisita
     */
    public void removeByIdVisita(Long idVisita){
        String hql = " delete from " + FuncionarioVisita.class.getName() + " as fv where fv.visita.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idVisita);
        getAdsmHibernateTemplate().flush();
    }
}