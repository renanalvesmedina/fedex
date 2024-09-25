package com.mercurio.lms.tracking.service;

import br.com.tntbrasil.integracao.domains.sim.StatusLinhaTempoRastreabilidade;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.facade.radar.TrackingFacade;


public class LinhaTempoService {
	
	private TrackingFacade trackingFacade;
	
	
	
	public StatusLinhaTempoRastreabilidade findLinhaTempoEvento(Long idDoctoServico) {
		StatusLinhaTempoRastreabilidade status = null;
		
		// Verifica se o evento está na Filial de Destino ou de não conformidade.
		status = setEventoFinalizadoOuNaoConformidade(idDoctoServico);
		if(status == StatusLinhaTempoRastreabilidade.EM_AVARIA){
			return status;
		}
		if(status == StatusLinhaTempoRastreabilidade.EM_FALTA){
			return status;
		}
		if(status == StatusLinhaTempoRastreabilidade.FINALIZADO){
			return status;
		}
		
		// Verifica se o evento está em Rota
		status = setEventoEmRotaEntrega(idDoctoServico);
		if(status == StatusLinhaTempoRastreabilidade.EM_ROTA_ENTREGA){
			return status;
		}
		
		// Verifica se o evento está na Filial de Destino.
		status = setEventoNaFilialDestino(idDoctoServico);
		if(status == StatusLinhaTempoRastreabilidade.EM_FILIAL_DESTINO){
			return status;
		}
		
		// Verifica se o evento está em Transito
		status = setEventoEmTransito(idDoctoServico);
		if(status == StatusLinhaTempoRastreabilidade.EM_TRANSITO){
			return status;
		}
		
		// Verifica se está em evento de Coleta.
		status = setEventoColeta(idDoctoServico);
		if(status == StatusLinhaTempoRastreabilidade.EM_COLETA){
			return status;
		}
		return status;
	}
	
	private StatusLinhaTempoRastreabilidade setEventoColeta(Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("isNotEventoCancelado", Boolean.TRUE);
		criteria.put("idDoctoServico", idDoctoServico);
		criteria.put("isNotNullIdEvento", Boolean.TRUE);
		criteria.put("isNotNullIdConhecimento", Boolean.TRUE);
		TypedFlatMap e = trackingFacade.findEventoByIdDoctoServico(criteria);
		if(e != null && e.get("cdEvento") != null){
				return StatusLinhaTempoRastreabilidade.EM_COLETA;
		}
		return StatusLinhaTempoRastreabilidade.NENHUMA;
	}
	
	private StatusLinhaTempoRastreabilidade setEventoEmTransito(Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("isFilialOrigem", Boolean.TRUE);
		criteria.put("idDoctoServico", idDoctoServico);
		criteria.put("cdEvento", StatusLinhaTempoRastreabilidade.EM_TRANSITO.getCdEvento());
		//criteria.put("tpDocumento", "MAV");
		criteria.put("isNotEventoCancelado", Boolean.TRUE);
		TypedFlatMap e = trackingFacade.findEventoByIdDoctoServico(criteria);
		if(e != null && e.get("cdEvento") != null){
			return StatusLinhaTempoRastreabilidade.EM_TRANSITO;
		}
		return StatusLinhaTempoRastreabilidade.NENHUMA;
	}
	
	private StatusLinhaTempoRastreabilidade setEventoNaFilialDestino(Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("isFilialDestino", Boolean.TRUE);
		criteria.put("idDoctoServico", idDoctoServico);
		criteria.put("cdEvento", StatusLinhaTempoRastreabilidade.EM_FILIAL_DESTINO.getCdEvento());
		criteria.put("isNotEventoCancelado", Boolean.TRUE);
		TypedFlatMap e = trackingFacade.findEventoByIdDoctoServico(criteria);
		if(e != null && e.get("cdEvento") != null){
			return StatusLinhaTempoRastreabilidade.EM_FILIAL_DESTINO;
		}
		return StatusLinhaTempoRastreabilidade.NENHUMA;
	}
	
	private StatusLinhaTempoRastreabilidade setEventoEmRotaEntrega(Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("isNotEventoCancelado", Boolean.TRUE);
		criteria.put("idDoctoServico", idDoctoServico);
		criteria.put("isManifestoEntrada", Boolean.TRUE);
		criteria.put("cdEvento", StatusLinhaTempoRastreabilidade.EM_ROTA_ENTREGA.getCdEvento());
		TypedFlatMap e = trackingFacade.findEventoByIdDoctoServico(criteria);
		if(e != null && e.get("cdEvento") != null ){
			return StatusLinhaTempoRastreabilidade.EM_ROTA_ENTREGA;
		}
		return StatusLinhaTempoRastreabilidade.NENHUMA;
	}
	
	private StatusLinhaTempoRastreabilidade setEventoFinalizadoOuNaoConformidade(Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idDoctoServico", idDoctoServico);
		criteria.put("isNotEventoCancelado", Boolean.TRUE);
		criteria.put("cdEvento", StatusLinhaTempoRastreabilidade.FINALIZADO.getCdEvento());
		criteria.put("idOcorrenciaEntrega", 5L);
		TypedFlatMap e = trackingFacade.findEventoByIdDoctoServico(criteria);
		if(e != null && e.get("cdEvento") != null){
			return StatusLinhaTempoRastreabilidade.FINALIZADO;
		} else {
			criteria = new TypedFlatMap();
			criteria.put("idDoctoServico", idDoctoServico);
			e = trackingFacade.findEventoNaoConformidade(criteria);
			if(e.get("idEvento") != null){
				Long idEvento = (Long) e.get("idEvento");
				return setStatusMotivoAberturaNc(idEvento.intValue());
			}
		}
		
		return StatusLinhaTempoRastreabilidade.NENHUMA;
	}
	
	private StatusLinhaTempoRastreabilidade setStatusMotivoAberturaNc(int ideventoMotivoAberturaNc) {
		switch(ideventoMotivoAberturaNc){
			case 15:
				return StatusLinhaTempoRastreabilidade.EM_AVARIA;
			case 16:
				return  StatusLinhaTempoRastreabilidade.EM_AVARIA;
			case 22:
				return StatusLinhaTempoRastreabilidade.EM_AVARIA;
			case 13:
				return StatusLinhaTempoRastreabilidade.EM_FALTA;
			case 17:
				return StatusLinhaTempoRastreabilidade.EM_FALTA;
			default:
				break;
		}
		return null;
	}

	public TrackingFacade getTrackingFacade() {
		return trackingFacade;
	}

	public void setTrackingFacade(TrackingFacade trackingFacade) {
		this.trackingFacade = trackingFacade;
	}

}
