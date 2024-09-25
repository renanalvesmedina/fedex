package com.mercurio.lms.coleta.model.service;

import br.com.tntbrasil.integracao.domains.dell.FaturaDellDMN;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;

public class EventoFaturaService {
	private IntegracaoJmsService integracaoJmsService;

	public void storeMessage(FaturaDMN faturaDMN) {
		if (faturaDMN instanceof FaturaDellDMN) {
			IntegracaoJmsService.JmsMessageSender message = integracaoJmsService.createMessage(VirtualTopics.EVENTO_FATURA_DELL, faturaDMN);
			integracaoJmsService.storeMessage(message);
		}
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
