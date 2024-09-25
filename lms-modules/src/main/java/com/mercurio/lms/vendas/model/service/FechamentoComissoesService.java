package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.FechamentoComissao;
import com.mercurio.lms.vendas.model.dao.FechamentoComissoesDAO;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

public class FechamentoComissoesService extends CrudService<FechamentoComissao, Long> {

	private FechamentoComissoesDAO fechamentoComissoesDAO;
	private UsuarioLMSService usuarioLMSService;
	private WorkflowPendenciaService workflowPendenciaService;
	
	public FechamentoComissao findHasAlreadyFechamentoMes(String tpFechamento) {
		Long id = fechamentoComissoesDAO.findIdFechamentoMes(tpFechamento);
		
		if (id == null) {
			return null;
		}
		
		return (FechamentoComissao) findById(id);
	}
	
	public void setFechamentoComissoesDAO(FechamentoComissoesDAO fechamentoComissoesDAO) {
		this.fechamentoComissoesDAO = fechamentoComissoesDAO;
	}

	public Serializable storeFechamento() {
		FechamentoComissao fechamentoComissao = new FechamentoComissao();

		Long idUsuario = SessionUtils.getUsuarioLogado().getIdUsuario();
		UsuarioLMS usuarioLms = usuarioLMSService.findById(idUsuario);
		
		fechamentoComissao.setTpFechamento(new DomainValue("F"));
		fechamentoComissao.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setUsuarioInclusao(usuarioLms);
		fechamentoComissao.setUsuarioAlteracao(usuarioLms);
		fechamentoComissao.setBlRetorno("N");
		
		fechamentoComissoesDAO.store(fechamentoComissao);
		return fechamentoComissao.getIdFechamentoComissao();
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	public Long storeAprovacao() {
		// TODO fazer um teste lancando excecao para saber o que vai dar
		FechamentoComissao fechamentoComissao = new FechamentoComissao();

		Long idUsuario = SessionUtils.getUsuarioLogado().getIdUsuario();
		UsuarioLMS usuarioLms = usuarioLMSService.findById(idUsuario);
		
		fechamentoComissao.setTpFechamento(new DomainValue("A"));
		fechamentoComissao.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setUsuarioInclusao(usuarioLms);
		fechamentoComissao.setUsuarioAlteracao(usuarioLms);
		fechamentoComissao.setBlRetorno("N");

		fechamentoComissoesDAO.store(fechamentoComissao);
		
		Short evento = 155;
		Long idProcesso = fechamentoComissao.getIdFechamentoComissao();
		String justificativa = " Cálculos de comissões finalizados. Para sequência no processo favor aprovar este workflow.";

		// TODO 
		// TODO trocar esse valor. NAO eh ATCF --- nao esta correto. nao esquecer
		// TODO
		CampoHistoricoWorkflow campoHistoricoWorkflow = CampoHistoricoWorkflow.ATCF;
		Pendencia pendencia = generatePendenciaWorkflow(evento, idProcesso, justificativa, campoHistoricoWorkflow);
		
		fechamentoComissao.setPendenciaAprovacao(pendencia);
		fechamentoComissao.setUsuarioAprovacao(usuarioLms);
		fechamentoComissao.setTpSituacaoAprovacao(new DomainValue("E"));
		fechamentoComissoesDAO.store(fechamentoComissao);
		
		return fechamentoComissao.getIdFechamentoComissao();
	}
	
	
	
	public void executeWorkflow(List<Long> idProcesso, List<String> tpSituacaoAprovacao) {
		FechamentoComissao fechamento = fechamentoComissoesDAO.findById(idProcesso.iterator().next());
		
		fechamento.setBlRetorno("S");
		fechamento.setTpSituacaoAprovacao(new DomainValue("A"));
		
		store(fechamento);
	}
	
	
	private Pendencia generatePendenciaWorkflow(Short evento, Long idProcesso,
			String justificativa, CampoHistoricoWorkflow campoHistoricoWorkflow) {
		
		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();

		pendenciaHistoricoDTO.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		pendenciaHistoricoDTO.setNrTipoEvento(evento);
		pendenciaHistoricoDTO.setIdProcesso(idProcesso);
		pendenciaHistoricoDTO.setDsProcesso(justificativa);
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(campoHistoricoWorkflow);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.FECHAMENTO_COMISSAO);
		pendenciaHistoricoDTO.setDsVlAntigo("");
		pendenciaHistoricoDTO.setDsVlNovo("");
		pendenciaHistoricoDTO.setDsObservacao(" - ");
 
		return workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
	}

	public void setFechamentoComissaoDao(FechamentoComissoesDAO dao) {
		setDao(dao);
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void storeEnvioRh() {
		FechamentoComissao fechamentoComissao = new FechamentoComissao();

		Long idUsuario = SessionUtils.getUsuarioLogado().getIdUsuario();
		UsuarioLMS usuarioLms = usuarioLMSService.findById(idUsuario);
		
		fechamentoComissao.setTpFechamento(new DomainValue("E"));
		fechamentoComissao.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		fechamentoComissao.setUsuarioInclusao(usuarioLms);
		fechamentoComissao.setUsuarioAlteracao(usuarioLms);
		fechamentoComissao.setBlRetorno("N");

		fechamentoComissoesDAO.store(fechamentoComissao);
	}
}
