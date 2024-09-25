package com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class GerarNotaCreditoPadraoFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filial;
	private ProprietarioDTO proprietario;
	private MeioTransporteSuggestDTO meioTransporte;
	private ControleCargaSuggestDTO controleCarga;

	private DomainValue tpGerarNotaCredito;
	
	private DateTime dhSaidaColetaEntrega;

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public DomainValue getTpGerarNotaCredito() {
		return tpGerarNotaCredito;
	}

	public void setTpGerarNotaCredito(DomainValue tpGerarNotaCredito) {
		this.tpGerarNotaCredito = tpGerarNotaCredito;
	}

	public DateTime getDhSaidaColetaEntrega() {
		return dhSaidaColetaEntrega;
	}

	public void setDhSaidaColetaEntrega(DateTime dhSaidaColetaEntrega) {
		this.dhSaidaColetaEntrega = dhSaidaColetaEntrega;
	}

}