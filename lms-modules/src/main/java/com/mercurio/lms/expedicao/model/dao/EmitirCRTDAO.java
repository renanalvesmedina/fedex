package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.expedicao.model.CtoInternacional;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class EmitirCRTDAO extends AdsmDao {

	public List<CtoInternacional> validateCRTCancelado(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(CtoInternacional.class, "ci");

		dc.add(Restrictions.eq("ci.idDoctoServico",idDoctoServico));
		dc.add(Restrictions.eq("ci.tpSituacaoCrt","C"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<CtoInternacional> validateCRTEmitido(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(CtoInternacional.class, "ci");

		dc.add(Restrictions.eq("ci.idDoctoServico",idDoctoServico));
		//dc.add(Restrictions.eq("ci.idDoctoServico",Long.valueOf(3)));
		dc.add(Restrictions.eq("ci.tpSituacaoCrt","E"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<CtoInternacional> validatePendenciaAutorizacao(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(CtoInternacional.class, "ci");
		dc.createAlias("ci.pendenciaReemissao", "pr");
		dc.setFetchMode("pr",FetchMode.DEFAULT);

		dc.add(Restrictions.eq("ci.idDoctoServico",idDoctoServico));
		dc.add(Restrictions.isNotNull("pr.idPendencia"));
		dc.add(Restrictions.ne("pr.tpSituacaoPendencia","C"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<CtoInternacional> validatePendenciaAprovada(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(CtoInternacional.class, "ci");
		dc.createAlias("ci.pendenciaReemissao", "pr");
		dc.setFetchMode("pr",FetchMode.DEFAULT);

		dc.add(Restrictions.eq("ci.idDoctoServico",idDoctoServico));
		dc.add(Restrictions.ne("pr.tpSituacaoPendencia","A"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<CtoInternacional> executeValidacaoDescontoAprovadoWorkflow(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(CtoInternacional.class, "ci");
		dc.createAlias("ci.devedorDocServFats", "dd");
		dc.createAlias("dd.descontos", "de");

		dc.add(Restrictions.eq("ci.idDoctoServico",idDoctoServico));
		dc.add(Restrictions.eq("de.tpSituacaoAprovacao","A"));

		dc.setProjection(
				Projections.projectionList()
				.add(Projections.alias(Projections.property("ci.idDoctoServico"),"idDoctoServico"))
				.add(Projections.alias(Projections.property("ci.vlTotalDocServico"),"vlTotalDocServico"))
				.add(Projections.alias(Projections.property("ci.vlDesconto"),"vlDesconto")));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
}
