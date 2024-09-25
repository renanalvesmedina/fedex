package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ServicoSuggestDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private Long idServico;
	private String nomeServico;
    private DomainValue tpModal;
    private DomainValue tpAbrangencia;
	
	public ServicoSuggestDTO() {
	}

	public ServicoSuggestDTO(Long idServico, String nomeServico, DomainValue tpModal, DomainValue tpAbrangencia) {
		this.idServico = idServico;
		this.nomeServico = nomeServico;
		this.tpModal = tpModal;
		this.tpAbrangencia = tpAbrangencia;
		
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public String getNomeServico() {
		return nomeServico;
	}

	public void setNomeServico(String nomeServico) {
		this.nomeServico = nomeServico;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}
	
}
