package com.mercurio.lms.rest.carregamento;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.portaria.dto.MacroZonaDTO;
import org.joda.time.DateTime;

public class DispositivoUnitizacaoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private EmpresaDTO empresa;
	
	private TipoDispositivoUnitizacaoDTO tipoDispositivoUnitizacao;
	
	private Long tpNrIdentificacao;
	
	private String nrIdentificacao;
	
	private DomainValue tpSituacao;
	
	private String sgFilialLocalizacaoMercadoria;
	
	private String dsLocalizacaoMercadoria;
	
	private MacroZonaDTO macroZona;

	private DispositivoUnitizacaoDTO dispositivoUnitizacaoPai;
	
	private Integer qtVolumes;
	
	private Integer qtDispositivos;

	private DateTime dhUltimaMovimentacao;
	
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

	public Long getTpNrIdentificacao() {
		return tpNrIdentificacao;
	}

	public void setTpNrIdentificacao(Long tpNrIdentificacao) {
		this.tpNrIdentificacao = tpNrIdentificacao;
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

	public String getSgFilialLocalizacaoMercadoria() {
		return sgFilialLocalizacaoMercadoria;
	}

	public void setSgFilialLocalizacaoMercadoria(
			String sgFilialLocalizacaoMercadoria) {
		this.sgFilialLocalizacaoMercadoria = sgFilialLocalizacaoMercadoria;
	}

	public String getDsLocalizacaoMercadoria() {
		return dsLocalizacaoMercadoria;
	}

	public void setDsLocalizacaoMercadoria(String dsLocalizacaoMercadoria) {
		this.dsLocalizacaoMercadoria = dsLocalizacaoMercadoria;
	}

	public MacroZonaDTO getMacroZona() {
		return macroZona;
	}

	public void setMacroZona(MacroZonaDTO macroZona) {
		this.macroZona = macroZona;
	}

	public DispositivoUnitizacaoDTO getDispositivoUnitizacaoPai() {
		return dispositivoUnitizacaoPai;
	}

	public void setDispositivoUnitizacaoPai(
			DispositivoUnitizacaoDTO dispositivoUnitizacaoPai) {
		this.dispositivoUnitizacaoPai = dispositivoUnitizacaoPai;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public Integer getQtDispositivos() {
		return qtDispositivos;
	}

	public void setQtDispositivos(Integer qtDispositivos) {
		this.qtDispositivos = qtDispositivos;
	}

	public DateTime getDhUltimaMovimentacao() {
		return dhUltimaMovimentacao;
	}

	public void setDhUltimaMovimentacao(DateTime dhUltimaMovimentacao) {
		this.dhUltimaMovimentacao = dhUltimaMovimentacao;
	}
}