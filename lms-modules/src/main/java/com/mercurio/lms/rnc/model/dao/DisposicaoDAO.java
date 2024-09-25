package com.mercurio.lms.rnc.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.rnc.model.Disposicao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DisposicaoDAO extends BaseCrudDao<Disposicao, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Disposicao.class;
    }

    /**
     * Solicita��o CQPRO00004843 da integra��o.
     * Busca uma Disposi��o a partir do id da Ocorr�ncia de N�o Conformidade.
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