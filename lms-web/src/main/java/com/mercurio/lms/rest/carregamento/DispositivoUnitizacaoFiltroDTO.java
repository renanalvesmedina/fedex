package com.mercurio.lms.rest.carregamento;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.portaria.dto.MacroZonaDTO;

public class DispositivoUnitizacaoFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private EmpresaDTO empresa;
	
	private TipoDispositivoUnitizacaoDTO tipoDispositivoUnitizacao;
	
	private String nrIdentificacao;
	
	private String acao;
	
	private DomainValue tpSituacao;
	
	private Boolean dispositivoVazio;
	
	private DispositivoUnitizacaoDTO dispositivoUnitizacaoPai;
	
	private MacroZonaDTO macroZona;
	
	private FilialSuggestDTO filial;

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public TipoDispositivoUnitizacaoDTO getTipoDispositivoUnitizacao() {
		return tipoDispositivoUnitizacao;
	}

	public void setTipoDispositivoUnitizacao(
			TipoDispositivoUnitizacaoDTO tipoDispositivoUnitizacao) {
		this.tipoDispositivoUnitizacao = tipoDispositivoUnitizacao;
	}
	
	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Boolean getDispositivoVazio() {
		return dispositivoVazio;
	}

	public void setDispositivoVazio(Boolean dispositivoVazio) {
		this.dispositivoVazio = dispositivoVazio;
	}

	public DispositivoUnitizacaoDTO getDispositivoUnitizacaoPai() {
		return dispositivoUnitizacaoPai;
	}

	public void setDispositivoUnitizacaoPai(
			DispositivoUnitizacaoDTO dispositivoUnitizacaoPai) {
		this.dispositivoUnitizacaoPai = dispositivoUnitizacaoPai;
	}

	public MacroZonaDTO getMacroZona() {
		return macroZona;
	}

	public void setMacroZona(MacroZonaDTO macroZona) {
		this.macroZona = macroZona;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

    public FilialSuggestDTO getFilial() {
        return filial;
    }

    public void setFilial(FilialSuggestDTO filial) {
        this.filial = filial;
    }
	
}
