package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.util.session.SessionUtils;

public class ExecuteWorkflowControleCarga {
	private EventoControleCargaService eventoControleCargaService;
	private ManifestoService manifestoService;

	public String executeWorkflow(List<Long> idsSolicitacao, List<String> tpsStituacao) {
		if (idsSolicitacao.size() != 1)
			throw new IllegalArgumentException("Você deve informar uma solicitacao");
		if (tpsStituacao.size() != 1)
			throw new IllegalArgumentException("Você deve informar uma situação");

		String tpSituacao = (String) tpsStituacao.get(0);
		
		EventoControleCarga eventoFindedById = getEventoControleCargaService().findById((Long) idsSolicitacao.get(0));
		eventoFindedById.setTpSituacaoPendencia(new DomainValue(tpSituacao));
		if ("A".equals(tpSituacao)) {
			eventoFindedById.setDhEventoOriginal(eventoFindedById.getDhEvento());
			eventoFindedById.setDhEvento(eventoFindedById.getDhEventoSolicitacao());
			eventoFindedById.setDhEventoSolicitacao(null);
			eventoFindedById.setUsuarioAprovador(SessionUtils.getUsuarioLogado());
			List<Manifesto> manifestosByControleCarga = manifestoService.findManifestosByControleCarga(eventoFindedById.getControleCarga());
			for (int index = 0; index < manifestosByControleCarga.size(); index++) {
				Manifesto manifestoLoop = manifestosByControleCarga.get(index);
				manifestoLoop.getManifestoEntrega().setDhEmissao(eventoFindedById.getDhEvento());
				manifestoLoop.setDhEmissaoManifesto(eventoFindedById.getDhEvento());
			}
			
			manifestoService.storeAll(manifestosByControleCarga);
		} else {
			eventoFindedById.setDhEventoSolicitacao(null);
		}
		eventoControleCargaService.store(eventoFindedById);
		return null;
	}

	public EventoControleCargaService getEventoControleCargaService() {
		return eventoControleCargaService;
	}

	public void setEventoControleCargaService(
			EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	
}
