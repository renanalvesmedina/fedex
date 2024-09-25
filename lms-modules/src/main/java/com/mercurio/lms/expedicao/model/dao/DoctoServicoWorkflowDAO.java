package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DoctoServicoWorkflow;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;

public class DoctoServicoWorkflowDAO extends BaseCrudDao<DoctoServicoWorkflow, Long> {

	@Override
	public Class getPersistentClass() {
		return DoctoServicoWorkflow.class;
	}

	public DoctoServicoWorkflow findById(Long id) {
		return (DoctoServicoWorkflow) super.findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<DoctoServicoWorkflow> findByMonitoramentoAndSituacao(Long idMonitoramentoDescarga, String tpSituacaoAprovacao) {
		Criteria criteria = getSession(false).createCriteria(DoctoServicoWorkflow.class, "docswork");
		criteria.createAlias("docswork.doctoServico", "docservi");
		
		DetachedCriteria sub = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "notasconhe");
    	sub.setProjection(Projections.id());
    	sub.createAlias("notasconhe.volumeNotaFiscais", "volumes");
    	sub.add(Restrictions.eqProperty("notasconhe.conhecimento.idDoctoServico", "docservi.idDoctoServico"));
    	sub.add(Restrictions.eq("volumes.monitoramentoDescarga.idMonitoramentoDescarga", idMonitoramentoDescarga));
        
    	criteria.add(Subqueries.exists(sub));
    	criteria.add(Restrictions.eq("tpSituacaoAprovacao", tpSituacaoAprovacao));
		
		return criteria.list();
	}
}
