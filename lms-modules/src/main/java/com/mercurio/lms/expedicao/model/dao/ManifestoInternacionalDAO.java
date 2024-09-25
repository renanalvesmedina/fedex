package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ManifestoInternacional;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoInternacionalDAO extends BaseCrudDao<ManifestoInternacional, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ManifestoInternacional> getPersistentClass() {
		return ManifestoInternacional.class;
	}

	public List<ManifestoInternacional> findByNrManifestoByFilialByTpMic(Long nrManifesto, Long idFilial, String tpMic){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrManifestoInt", nrManifesto));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));
		dc.add(Restrictions.eq("tpMic", tpMic));
		return findByDetachedCriteria(dc);
	}

	public List<Long> findDocumentosServico(Long idManifestoInternacional, String tpMic){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("manifestoInternacCtos", "mic");
		dc.createAlias("mic.ctoInternacional", "ci");
		dc.add(Restrictions.eq("idManifestoInternacional", idManifestoInternacional));
		dc.add(Restrictions.eq("tpMic", tpMic));
		dc.setProjection(Projections.property("ci.idDoctoServico"));

		return findByDetachedCriteria(dc);	
	}

}