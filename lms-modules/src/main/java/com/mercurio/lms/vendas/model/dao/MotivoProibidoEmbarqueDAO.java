package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoProibidoEmbarqueDAO extends BaseCrudDao<MotivoProibidoEmbarque, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoProibidoEmbarque.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsMotivoProibidoEmbarque
     * @return <MotivoProibidoEmbarque>
     */
	public MotivoProibidoEmbarque findMotivoProibidoEmbarque(String dsMotivoProibidoEmbarque) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(BaseCompareVarcharI18n.eq("dsMotivoProibidoEmbarque", dsMotivoProibidoEmbarque, LocaleContextHolder.getLocale()));

		return (MotivoProibidoEmbarque) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

    /**
     * Método que garante a ordenação da combo de motivos de bloqueio pela descrição do motivo
     * @param criterions Critérios
     * @return List Lista contendo os motivos de bloqueio encontrados e ordenados pela descrição do motivo
     */
    public List findListByCriteria(Map criterions) {
        ArrayList order = new ArrayList();
        order.add("dsMotivoProibidoEmbarque");
        return super.findListByCriteria(criterions,order);
    }
}