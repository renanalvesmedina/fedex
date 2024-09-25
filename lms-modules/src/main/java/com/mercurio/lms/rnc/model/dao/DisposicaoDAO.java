package com.mercurio.lms.rnc.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.rnc.model.Disposicao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DisposicaoDAO extends BaseCrudDao<Disposicao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Disposicao.class;
    }

    /**
     * Solicitação CQPRO00004843 da integração.
     * Busca uma Disposição a partir do id da Ocorrência de Não Conformidade.
     * 
     * @param idOcorrenciaNaoConformidade
     * @return
     */    
    public Disposicao findByIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade){
    	DetachedCriteria dc = DetachedCriteria.forClass(Disposicao.class);
    	dc.add(Restrictions.eq("ocorrenciaNaoConformidade.id", idOcorrenciaNaoConformidade));
    	return (Disposicao)getAdsmHibernateTemplate().findUniqueResult(dc);
    }

}