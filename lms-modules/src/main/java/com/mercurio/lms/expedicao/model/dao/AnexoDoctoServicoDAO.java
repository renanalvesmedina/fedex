package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.AnexoDoctoServico;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AnexoDoctoServicoDAO extends BaseCrudDao<AnexoDoctoServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<AnexoDoctoServico> getPersistentClass() {
		return AnexoDoctoServico.class;
	}

	public List<AnexoDoctoServico> findAnexosDoctoServico(TypedFlatMap criteria) {
		DetachedCriteria dc = createDetachedCriteria();
		DomainValue tpSituacao = criteria.getDomainValue("tpSituacao");
		if (tpSituacao != null) {
			dc.add(Restrictions.eq("tpSituacao", tpSituacao.getValue()));
		}
		dc.addOrder(OrderVarcharI18n.asc("dsAnexoDoctoServico", LocaleContextHolder.getLocale()));
		return findByDetachedCriteria(dc);
	}
}