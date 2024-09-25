package com.mercurio.lms.rest.vendas.dto;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;

public class PropostaClienteServicoAdicionalFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private DomainValue tpIndicador;
	private Long idParcelaPreco;
	private List servicos;

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public List getServicos() {
		return servicos;
	}

	public void setServicos(List servicos) {
		this.servicos = servicos;
	}
}
