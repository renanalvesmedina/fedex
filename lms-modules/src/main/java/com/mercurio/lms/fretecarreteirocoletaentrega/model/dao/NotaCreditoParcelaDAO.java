package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaCreditoParcelaDAO extends BaseCrudDao<NotaCreditoParcela, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaCreditoParcela.class;
    }

	public List<NotaCreditoParcela> findByIdNotaCredito(Long idNotaCredito) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("notaCredito.id", idNotaCredito));
		return findByDetachedCriteria(detachedCriteria);
	}
   



}