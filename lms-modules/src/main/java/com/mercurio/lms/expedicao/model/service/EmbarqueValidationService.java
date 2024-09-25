package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public class EmbarqueValidationService {

	private static final String PESSOA_FISICA = "F";

	public boolean hasToValidateEmbarqueProibido(Servico servico, Cliente clienteResponsavel, DomainValue tipoFrete) {
		return 		
			isRodoviario(servico) || 
			(
				isPessoaFisica(clienteResponsavel.getPessoa().getTpPessoa().getValue()) &&	
				isAereo(servico) && 
				isFOB(tipoFrete) && 
				isClienteEventualOrPotencial(clienteResponsavel.getTpCliente().getValue())
			);
	}

	private boolean isPessoaFisica(String value) {
		return PESSOA_FISICA.equalsIgnoreCase(value);
	}

	private boolean isClienteEventualOrPotencial(String tipoClienteResponsavel) {
		return 
			ConstantesVendas.CLIENTE_POTENCIAL.equals(tipoClienteResponsavel) || 
			ConstantesVendas.CLIENTE_EVENTUAL.equals(tipoClienteResponsavel);
	}

	private boolean isFOB(DomainValue tipoFrete) {
		return ConstantesExpedicao.TP_FRETE_FOB.equals(tipoFrete.getValue());
	}

	private boolean isAereo(Servico servico) {
		return hasModal(servico, ConstantesExpedicao.MODAL_AEREO);
	}

	private boolean isRodoviario(Servico servico) {
		return hasModal(servico, ConstantesExpedicao.MODAL_RODOVIARIO);
	}
	
	private boolean hasModal(Servico servico, String modal) {
		return modal.equals(servico.getTpModal().getValue());
	}
}