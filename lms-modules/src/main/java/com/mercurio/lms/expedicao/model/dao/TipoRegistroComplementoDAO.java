package com.mercurio.lms.expedicao.model.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.TipoRegistroComplemento;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoRegistroComplementoDAO extends BaseCrudDao<TipoRegistroComplemento, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TipoRegistroComplemento.class;
	}

	public boolean verificaDadosComplemento(Object id) {
		DetachedCriteria dc = createDetachedCriteria();
		if (id instanceof Collection)
			dc.add(Restrictions.in("id", (Collection)id));
		else
			dc.add(Restrictions.idEq(id));
		dc.createAlias("informacaoDocServicos", "ids")
		.createAlias("ids.dadosComplementos", "dc")
		.setProjection(Projections.count("dc.id"));
		Integer total = getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(dc);
		return (total.intValue() > 0 ? false : true);
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}

	public int removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			this.removeById(id);
		}
		return ids.size();
	}

	private void storeInformacaoDocServico(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeInformacaoDocServico(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

	public List findInformacaoDocServicoByTipoRegistroComplementoId(Long tipoRegistroId) {
		return super.findByDetachedCriteria(getDetachedCriteria(tipoRegistroId));
	}

	public Integer getRowCountInfoDocServicoByTipoRegistroId(Long tipoRegistroId) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(tipoRegistroId)
				.setProjection(Projections.rowCount()));
	}

	private DetachedCriteria getDetachedCriteria(Long tipoRegistroId) {
		return DetachedCriteria.forClass(InformacaoDocServico.class)
		.add(Restrictions.eq("tipoRegistroComplemento.id", tipoRegistroId));
	}

	public TipoRegistroComplemento store(TipoRegistroComplemento bean, ItemList items) {
		super.store(bean);
		removeInformacaoDocServico(items.getRemovedItems());
		storeInformacaoDocServico(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();
		return bean;
	}

}