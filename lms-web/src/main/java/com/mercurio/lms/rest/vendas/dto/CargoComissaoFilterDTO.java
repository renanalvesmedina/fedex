package com.mercurio.lms.rest.vendas.dto;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestDTO;
 
public class CargoComissaoFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
	
	private UsuarioDTO usuarioDTO;
	private DomainValue tpCargo;
	
	private RegionalSuggestDTO regional;
	private FilialSuggestDTO filial;
	
	private DomainValue situacaoAtivo;
	
	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}
	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}
	public DomainValue getTpCargo() {
		return tpCargo;
	}
	public void setTpCargo(DomainValue tpCargo) {
		this.tpCargo = tpCargo;
	}
	public RegionalSuggestDTO getRegional() {
		return regional;
	}
	public void setRegional(RegionalSuggestDTO regional) {
		this.regional = regional;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public DomainValue getSituacaoAtivo() {
		return situacaoAtivo;
	}
	public void setSituacaoAtivo(DomainValue situacaoAtivo) {
		this.situacaoAtivo = situacaoAtivo;
	}
} 
