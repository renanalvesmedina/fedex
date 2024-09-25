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
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoProibidoEmbarqueDAO extends BaseCrudDao<MotivoProibidoEmbarque, Long> {
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoProibidoEmbarque.class;
    }

    /**
     * M�todo utilizado pela Integra��o
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
     * M�todo que garante a ordena��o da combo de motivos de bloqueio pela descri��o do motivo
     * @param criterions Crit�rios
     * @return List Lista contendo os motivos de bloqueio encontrados e ordenados pela descri��o do motivo
     */
    public List findListByCriteria(Map criterions) {
        ArrayList order = new ArrayList();
        order.add("dsMotivoProibidoEmbarque");
        return super.findListByCriteria(criterions,order);
    }
}