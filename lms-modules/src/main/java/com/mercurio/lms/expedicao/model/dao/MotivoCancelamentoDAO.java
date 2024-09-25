package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.expedicao.model.MotivoCancelamento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoCancelamentoDAO extends BaseCrudDao<MotivoCancelamento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoCancelamento.class;
    }

	public List findAtivosLookup() {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "m")
			.add(Restrictions.eq("m.tpSituacao", "A"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.property("m.idMotivoCancelamento"), "idMotivoCancelamento")
				.add(Projections.property("m.dsMotivoCancelamento"), "dsMotivoCancelamento"))
			.addOrder(OrderVarcharI18n.asc("m.dsMotivoCancelamento", LocaleContextHolder.getLocale()))		
		    .setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);		
	}

   


}