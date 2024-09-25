package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.lms.expedicao.model.TipoLiberacaoEmbarque;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoLiberacaoEmbarqueDAO extends BaseCrudDao<TipoLiberacaoEmbarque, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoLiberacaoEmbarque.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsTipoLiberacaoEmbarque
     * @param tpSituacao
     * @return <TipoLiberacaoEmbarque>  
     */
    public TipoLiberacaoEmbarque findTipoLiberacaoEmbarque(String dsTipoLiberacaoEmbarque, DomainValue tpSituacao) {
    	DetachedCriteria dc = createDetachedCriteria();
		dc.add(BaseCompareVarcharI18n.eq("dsTipoLiberacaoEmbarque", dsTipoLiberacaoEmbarque, LocaleContextHolder.getLocale()));
		dc.add(Restrictions.eq("tpSituacao", tpSituacao));
		return (TipoLiberacaoEmbarque) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public List findListByCriteria(Map criterions, List order) {
    	
   		order = new ArrayList(1);	
   		order.add("dsTipoLiberacaoEmbarque");
    	return super.findListByCriteria(criterions, order);
    }
}