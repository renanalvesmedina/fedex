package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.expedicao.model.DoctoServicoWorkflow;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoWorkflowDAO;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.service.AcaoService;

public class DoctoServicoWorkflowService extends CrudService<DoctoServicoWorkflow, Long> {

	private AcaoService acaoService;
	private UsuarioLMSService usuarioLMSService;

	public void setDAO(DoctoServicoWorkflowDAO dao) {
		setDao(dao);
	}

	private DoctoServicoWorkflowDAO getDAO() {
		return (DoctoServicoWorkflowDAO) getDao();
	}

	public Serializable store(DoctoServicoWorkflow bean) {
		return super.store(bean);
	}

	public DoctoServicoWorkflow findById(Long id) {
		return getDAO().findById(id);
	}

	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	@SuppressWarnings("unchecked")
	public String executeWorkflow(List<Long> idsDoctoServicoWorkflow, List<String> situacoes) {
		for (int i = 0; i < idsDoctoServicoWorkflow.size(); i++) {
			String tpSituacao = situacoes.get(i);

			DoctoServicoWorkflow doctoServicoWorkflow = this.findById(idsDoctoServicoWorkflow.get(i));
			doctoServicoWorkflow.setTpSituacaoAprovacao(new DomainValue(tpSituacao));

			if ("A".equals(tpSituacao)) {
				List<Acao> acoes = acaoService.findByIdPendenciaTpSituacaoAcao(doctoServicoWorkflow.getPendencia().getIdPendencia(), tpSituacao);

				if (acoes != null && !acoes.isEmpty()) {
					Acao acao = acoes.get(0);

					if (acao.getUsuario() != null) {
						UsuarioLMS usuario = usuarioLMSService.findById(acao.getUsuario().getIdUsuario());
						doctoServicoWorkflow.setUsuario(usuario);
					}

					doctoServicoWorkflow.setDtAprovacao(new YearMonthDay(acao.getDhAcao()));
				}
			}

			this.store(doctoServicoWorkflow);
		}

		return null;
	}

	public List<DoctoServicoWorkflow> findByMonitoramentoAndSituacao(Long idMonitoramentoDescarga, String tpSituacaoAprovacao) {
		return getDAO().findByMonitoramentoAndSituacao(idMonitoramentoDescarga, tpSituacaoAprovacao);
	}

}
