package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
 
public class CargoComissaoDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
	
	private Long id;
	private UsuarioDTO usuarioDTO;
	private String nrCpf;
	private DomainValue tpCargo;

	private YearMonthDay dtDesligamento;
	private DomainValue tpSituacao;
	
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getNrCpf() {
		return nrCpf;
	}
	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}
	public DomainValue getTpCargo() {
		return tpCargo;
	}
	public void setTpCargo(DomainValue tpCargo) {
		this.tpCargo = tpCargo;
	}
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}
	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}
	public YearMonthDay getDtDesligamento() {
		return dtDesligamento;
	}
	public void setDtDesligamento(YearMonthDay dtDesligamento) {
		this.dtDesligamento = dtDesligamento;
	}
	
} 
