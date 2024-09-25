package com.mercurio.lms.sim.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.sim.model.EventoMonitoramentoCCT;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.dao.EventoMonitoramentoCCTDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EventoMonitoramentoCCTService extends CrudService<EventoMonitoramentoCCT, Long> {
	
	private UsuarioLMSService usuarioLMSService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;

	@Override
	public Serializable store(EventoMonitoramentoCCT bean) {
		return super.store(bean);
	}
	
	public void setEventoMonitoramentoCCTDAO(EventoMonitoramentoCCTDAO dao) {
		setDao(dao);
	}

	private EventoMonitoramentoCCTDAO getEventoMonitoramentoCCTDAO() {
		return (EventoMonitoramentoCCTDAO) getDao();
	}
	
	public void storePopUpEventos(Map parameters) {
		Map[] selectedRows = (Map[])parameters.get("listMonitoramentoCCT");
		MonitoramentoCCT monitoramento;
		for (Map row : selectedRows){
			
			monitoramento = getMonitoramentoNotasFiscaisCCTService().findById(Long.valueOf(row.get("idMonitoramentoCCT").toString()));
			
			// POPUP - SOLICITAÇÃO DE PAGAMENTO [OK]
			if (parameters.get("dtSolicitacaoICMS") != null){
				monitoramento.setDtSolicitacaoICMS((YearMonthDay) parameters.get("dtSolicitacaoICMS"));
				
				EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
				evento.setMonitoramentoCCT(monitoramento);
				evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				evento.setDtEvento((YearMonthDay) parameters.get("dtSolicitacaoICMS"));
				evento.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				evento.setTpSituacaoNotaFiscalCCT(monitoramento.getTpSituacaoNotaFiscalCCT());
				if (parameters.get("dsComentario") != null) {
					evento.setDsComentario(parameters.get("dsComentario").toString());					
				}
				
				this.store(evento);
				
			// POPUP - CONFIRMAÇÃO DE PAGAMENTO	[OK]
			}else if (parameters.get("dtPagamentoICMS") != null){
				monitoramento.setDtPagamentoICMS((YearMonthDay) parameters.get("dtPagamentoICMS"));
				monitoramento.setTpSituacaoNotaFiscalCCT(new DomainValue("PA"));
				
				EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
				evento.setMonitoramentoCCT(monitoramento);
				evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				evento.setDtEvento((YearMonthDay) parameters.get("dtPagamentoICMS"));
				evento.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				evento.setTpSituacaoNotaFiscalCCT(new DomainValue("PA"));
				if (parameters.get("dsComentario") != null) {
					evento.setDsComentario(parameters.get("dsComentario").toString());					
				}
				
				this.store(evento);
				
			// POPOUP - INCLUIR COMENTÁRIO	[OK]
			}else if(parameters.get("dsComentario") != null){
				EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
				
				evento.setMonitoramentoCCT(monitoramento);
				evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				evento.setDtEvento(JTDateTimeUtils.getDataAtual());
				evento.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				evento.setTpSituacaoNotaFiscalCCT(monitoramento.getTpSituacaoNotaFiscalCCT());
				if (parameters.get("dsComentario") != null) {
					evento.setDsComentario(parameters.get("dsComentario").toString());					
				}
				
				this.store(evento);

			// POPOUP - AGENDAMENTOS [OK]
			}else if(parameters.get("dtEntrega") != null){
				EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
				
				evento.setMonitoramentoCCT(monitoramento);
				evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				evento.setDtEvento((YearMonthDay) parameters.get("dtEntrega"));
				evento.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				if (monitoramento.getBlConfiguraAgendamento() == true) {
					evento.setTpSituacaoNotaFiscalCCT(new DomainValue("AA"));
				} else {
					evento.setTpSituacaoNotaFiscalCCT(new DomainValue("AG"));
				}
				if (parameters.get("dsComentario") != null) {
					evento.setDsComentario(parameters.get("dsComentario").toString());					
				}
				
				this.store(evento);
				
			// POPUP - INCLUIR EVENTOS
			}else if ("IE".equals(parameters.get("tpPopUpOrigem"))) {
				monitoramento.setTpSituacaoNotaFiscalCCT(new DomainValue(parameters.get("vlValorDominioSituacaoCCT").toString()));
				if(row.get("blRecolheICMS") != null){
					monitoramento.setBlRecolheICMS("S".equalsIgnoreCase((String) row.get("blRecolheICMS")) ? true : false);					
				}
				if (row.get("dtICMSPago") != null) {
					YearMonthDay dtICMSPago = JTDateTimeUtils.convertDataStringToYearMonthDay(row.get("dtICMSPago").toString(), "dd/MM/yyyy");
					monitoramento.setDtPagamentoICMS(dtICMSPago);					
				} else {
					monitoramento.setDtPagamentoICMS(null);					
				}
				
				if("AI".equals(parameters.get("vlValorDominioSituacaoCCT").toString())){
					monitoramento.setDtSolicitacaoICMS(JTDateTimeUtils.getDataAtual());
				}
				
				EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
				
				evento.setMonitoramentoCCT(monitoramento);
				evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				evento.setDtEvento(JTDateTimeUtils.getDataAtual());
				evento.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				evento.setTpSituacaoNotaFiscalCCT(new DomainValue(parameters.get("vlValorDominioSituacaoCCT").toString()));
				if (parameters.get("comentario") != null) {
					evento.setDsComentario(parameters.get("comentario").toString());					
				}
				
				this.store(evento);
				
			}
		}
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
	
	@SuppressWarnings("rawtypes")
	public List findByIdMonitoramentoCCT(Long idMonitoramentoCCT, boolean hasOrderByDhInclusao, String[] fetches, String... tpSituacao) {
	    return this.getEventoMonitoramentoCCTDAO().findByIdMonitoramentoCCTAndTpSituacao(idMonitoramentoCCT, hasOrderByDhInclusao, fetches, tpSituacao);
	}
	
	@SuppressWarnings("rawtypes")
	public List findByIdMonitoramentoCCT(Long idMonitoramentoCCT, boolean hasOrderByDhInclusao, String[] fetches) {
	    return this.getEventoMonitoramentoCCTDAO().findByIdMonitoramentoCCTAndTpSituacao(idMonitoramentoCCT, hasOrderByDhInclusao, fetches);
	}

	@SuppressWarnings("rawtypes")	
	public List findEventoMonitoramentoCCT(TypedFlatMap criteria) {
	    return this.getEventoMonitoramentoCCTDAO().findEventoMonitoramentoCCT(criteria);
	}
	
	@SuppressWarnings("rawtypes")	
	public List findNmUsuarioEventoByIdMonitoramentoCCT(Long idMonitoramentoCCT) {
	    return this.getEventoMonitoramentoCCTDAO().findNmUsuarioEventoByIdMonitoramentoCCT(idMonitoramentoCCT);
	}

	@SuppressWarnings("rawtypes")	
	public List findMonitoramentoCCTByConfirmacaoAgendamento(Long id) {
		return this.getEventoMonitoramentoCCTDAO().findMonitoramentoCCTByConfirmacaoAgendamento(id);
	}
}
