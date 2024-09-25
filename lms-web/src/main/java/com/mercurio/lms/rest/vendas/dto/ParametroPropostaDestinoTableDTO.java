package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ParametroPropostaDestinoTableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal pcDiferencaFretePeso;
	private BigDecimal vlFretePeso;
	private BigDecimal pcDiferencaAdvalorem;
	private String siglaDescricao;
	private BigDecimal vlAdvalorem;
	private Long idDestinoProposta;
	private DomainValue tpIndicadorFretePeso;
	private DomainValue tpDiferencaAdvalorem;
	private DomainValue tpIndicadorFreteMinimo;
	private Long idUnidadeFederativaDestino;
	private DomainValue tpIndicadorAdvalorem;
	private BigDecimal vlFreteMinimo;
	private Long idTipoLocalizacaoMunicipioDestino;
	private Long idGrupoRegiao;
	private Boolean tpIndicadorFreteMinimoDisabled;
	private Boolean vlFreteMinimoDisabled;
	private Boolean tpIndicadorFretePesoDisabled;
	private Boolean vlFretePesoDisabled;
	private Boolean pcDiferencaFretePesoDisabled;
	private Boolean tpIndicadorAdvaloremDisabled;
	private Boolean vlAdvaloremDisabled;
	private Boolean tpDiferencaAdvaloremDisabled;
	private Boolean pcDiferencaAdvaloremDisabled;
	
	public BigDecimal getPcDiferencaFretePeso() {
		return pcDiferencaFretePeso;
	}
	public void setPcDiferencaFretePeso(BigDecimal pcDiferencaFretePeso) {
		this.pcDiferencaFretePeso = pcDiferencaFretePeso;
	}
	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}
	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}
	public BigDecimal getPcDiferencaAdvalorem() {
		return pcDiferencaAdvalorem;
	}
	public void setPcDiferencaAdvalorem(BigDecimal pcDiferencaAdvalorem) {
		this.pcDiferencaAdvalorem = pcDiferencaAdvalorem;
	}
	public String getSiglaDescricao() {
		return siglaDescricao;
	}
	public void setSiglaDescricao(String siglaDescricao) {
		this.siglaDescricao = siglaDescricao;
	}
	public BigDecimal getVlAdvalorem() {
		return vlAdvalorem;
	}
	public void setVlAdvalorem(BigDecimal vlAdvalorem) {
		this.vlAdvalorem = vlAdvalorem;
	}
	public Long getIdDestinoProposta() {
		return idDestinoProposta;
	}
	public void setIdDestinoProposta(Long idDestinoProposta) {
		this.idDestinoProposta = idDestinoProposta;
	}
	public DomainValue getTpIndicadorFretePeso() {
		return tpIndicadorFretePeso;
	}
	public void setTpIndicadorFretePeso(DomainValue tpIndicadorFretePeso) {
		this.tpIndicadorFretePeso = tpIndicadorFretePeso;
	}
	public DomainValue getTpDiferencaAdvalorem() {
		return tpDiferencaAdvalorem;
	}
	public void setTpDiferencaAdvalorem(DomainValue tpDiferencaAdvalorem) {
		this.tpDiferencaAdvalorem = tpDiferencaAdvalorem;
	}
	public DomainValue getTpIndicadorFreteMinimo() {
		return tpIndicadorFreteMinimo;
	}
	public void setTpIndicadorFreteMinimo(DomainValue tpIndicadorFreteMinimo) {
		this.tpIndicadorFreteMinimo = tpIndicadorFreteMinimo;
	}
	public Long getIdUnidadeFederativaDestino() {
		return idUnidadeFederativaDestino;
	}
	public void setIdUnidadeFederativaDestino(Long idUnidadeFederativaDestino) {
		this.idUnidadeFederativaDestino = idUnidadeFederativaDestino;
	}
	public DomainValue getTpIndicadorAdvalorem() {
		return tpIndicadorAdvalorem;
	}
	public void setTpIndicadorAdvalorem(DomainValue tpIndicadorAdvalorem) {
		this.tpIndicadorAdvalorem = tpIndicadorAdvalorem;
	}
	public BigDecimal getVlFreteMinimo() {
		return vlFreteMinimo;
	}
	public void setVlFreteMinimo(BigDecimal vlFreteMinimo) {
		this.vlFreteMinimo = vlFreteMinimo;
	}
	public Long getIdTipoLocalizacaoMunicipioDestino() {
		return idTipoLocalizacaoMunicipioDestino;
	}
	public void setIdTipoLocalizacaoMunicipioDestino(Long idTipoLocalizacaoMunicipioDestino) {
		this.idTipoLocalizacaoMunicipioDestino = idTipoLocalizacaoMunicipioDestino;
	}
	public Long getIdGrupoRegiao() {
		return idGrupoRegiao;
	}
	public void setIdGrupoRegiao(Long idGrupoRegiao) {
		this.idGrupoRegiao = idGrupoRegiao;
	}
	public Boolean getTpIndicadorFreteMinimoDisabled() {
		return tpIndicadorFreteMinimoDisabled;
	}
	public void setTpIndicadorFreteMinimoDisabled(Boolean tpIndicadorFreteMinimoDisabled) {
		this.tpIndicadorFreteMinimoDisabled = tpIndicadorFreteMinimoDisabled;
	}
	public Boolean getVlFreteMinimoDisabled() {
		return vlFreteMinimoDisabled;
	}
	public void setVlFreteMinimoDisabled(Boolean vlFreteMinimoDisabled) {
		this.vlFreteMinimoDisabled = vlFreteMinimoDisabled;
	}
	public Boolean getTpIndicadorFretePesoDisabled() {
		return tpIndicadorFretePesoDisabled;
	}
	public void setTpIndicadorFretePesoDisabled(Boolean tpIndicadorFretePesoDisabled) {
		this.tpIndicadorFretePesoDisabled = tpIndicadorFretePesoDisabled;
	}
	public Boolean getVlFretePesoDisabled() {
		return vlFretePesoDisabled;
	}
	public void setVlFretePesoDisabled(Boolean vlFretePesoDisabled) {
		this.vlFretePesoDisabled = vlFretePesoDisabled;
	}
	public Boolean getPcDiferencaFretePesoDisabled() {
		return pcDiferencaFretePesoDisabled;
	}
	public void setPcDiferencaFretePesoDisabled(Boolean pcDiferencaFretePesoDisabled) {
		this.pcDiferencaFretePesoDisabled = pcDiferencaFretePesoDisabled;
	}
	public Boolean getTpIndicadorAdvaloremDisabled() {
		return tpIndicadorAdvaloremDisabled;
	}
	public void setTpIndicadorAdvaloremDisabled(Boolean tpIndicadorAdvaloremDisabled) {
		this.tpIndicadorAdvaloremDisabled = tpIndicadorAdvaloremDisabled;
	}
	public Boolean getVlAdvaloremDisabled() {
		return vlAdvaloremDisabled;
	}
	public void setVlAdvaloremDisabled(Boolean vlAdvaloremDisabled) {
		this.vlAdvaloremDisabled = vlAdvaloremDisabled;
	}
	public Boolean getTpDiferencaAdvaloremDisabled() {
		return tpDiferencaAdvaloremDisabled;
	}
	public void setTpDiferencaAdvaloremDisabled(Boolean tpDiferencaAdvaloremDisabled) {
		this.tpDiferencaAdvaloremDisabled = tpDiferencaAdvaloremDisabled;
	}
	public Boolean getPcDiferencaAdvaloremDisabled() {
		return pcDiferencaAdvaloremDisabled;
	}
	public void setPcDiferencaAdvaloremDisabled(Boolean pcDiferencaAdvaloremDisabled) {
		this.pcDiferencaAdvaloremDisabled = pcDiferencaAdvaloremDisabled;
	}

}
