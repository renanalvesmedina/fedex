package com.mercurio.lms.rnc.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.rnc.model.DescricaoPadraoNc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescricaoPadraoNcDAO extends BaseCrudDao<DescricaoPadraoNc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescricaoPadraoNc.class;
    }

    /**
     * Solicitação CQPRO00004872 da integração.
     * Método que retorna uma instancia da classe DescricaoPadraoNc conforme o parametro especificado.
     * Esse método retorna uma lista. Caso necessite apenas o primeiro registro, entao deve-se utilizar um
     * get(0) ou outra forma similar no código que estiver chamando este método.
     * @param idMotivoAberturaNc 
     * @param tpSituacao -> Se null, busca todos os registros independente de estarem ativos ou inativos.
     * @return
     */
    public List findDescricaoPadraoNcByIdMotivoAberturaNc(Long idMotivoAberturaNc, String tpSituacao){
    	DetachedCriteria dc = DetachedCriteria.forClass(DescricaoPadraoNc.class);
    	dc.add(Restrictions.eq("motivoAberturaNc.id", idMotivoAberturaNc));
    	if (!StringUtils.isBlank(tpSituacao)){
    		dc.add(Restrictions.eq("tpSituacao", new DomainValue(tpSituacao)));	
    	}
    	return findByDetachedCriteria(dc);
    }

}