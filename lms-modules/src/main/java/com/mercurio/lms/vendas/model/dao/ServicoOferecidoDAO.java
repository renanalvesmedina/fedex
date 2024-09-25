package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ServicoOferecido;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoOferecidoDAO extends BaseCrudDao<ServicoOferecido, Long>
{

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("servico", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoOferecido.class;
    }

    /**
     * Remove os registros pertencentes � etapaVisita onde o
     * idEtapaVisita � recebido como par�metro
     * @param idVisita
     */
    public void removeByIdEtapaVisita(Long idEtapaVisita){
        String hql = " delete from " + ServicoOferecido.class.getName() + " as so where so.etapaVisita.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idEtapaVisita);
        getAdsmHibernateTemplate().flush();
    }


}