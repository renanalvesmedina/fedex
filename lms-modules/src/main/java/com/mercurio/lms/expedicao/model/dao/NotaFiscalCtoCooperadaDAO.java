package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.BaseCrudDao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalCtoCooperadaDAO extends BaseCrudDao<com.mercurio.lms.expedicao.model.NotaFiscalCtoCooperada, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return com.mercurio.lms.expedicao.model.NotaFiscalCtoCooperada.class;
    }

	public List findByIdCtoCtoCooperada(Long idCtoCtoCooperada) {
		Projection projection = Projections.projectionList()
			.add(Projections.property("nfcto.id"),"idNfCtoCooperada")
			.add(Projections.property("nfcto.psMercadoria"),"psMercadoria")
			.add(Projections.property("nfcto.vlTotal"),"vlTotal")
			.add(Projections.property("nfcto.qtVolumes"),"qtVolumes")
			.add(Projections.property("nfcto.dtEmissao"),"dtEmissao")
			.add(Projections.property("nfcto.dsSerie"),"dsSerie")
			.add(Projections.property("nfcto.nrNotaFiscal"),"nrNotaFiscal");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "nfcto")
			.setProjection(projection)
			.createAlias("nfcto.ctoCtoCooperada", "cto")
			.add(Restrictions.eq("cto.id", idCtoCtoCooperada))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public int removeByIds(List ids) {
		return super.removeByIds(ids);
	}
}