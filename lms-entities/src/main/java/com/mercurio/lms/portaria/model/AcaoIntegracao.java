package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.vendas.model.GrupoEconomico;

public class AcaoIntegracao implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idAcaoIntegracao;
	private Boolean blUfDestinoDiferenteDe;
	private DomainValue tpModal;
	private DomainValue tpDocumento;
	private Boolean blRemetenteOrigem;
	private String dsAcaoIntegracao;
	private String dsProcessoIntegracao;
	private Boolean blUfOrigemDiferenteDe;
	private Filial filialOrigem;
	private Filial filialDestino;

	private UnidadeFederativa unidadeFederativaOrigem;
	private UnidadeFederativa unidadeFederativaDestino;

	private GrupoEconomico grupoEconomico;
	
	public Long getIdAcaoIntegracao() {
		return idAcaoIntegracao;
	}

	public void setIdAcaoIntegracao(Long idAcaoIntegracao) {
		this.idAcaoIntegracao = idAcaoIntegracao;
	}

	public Boolean getBlUfDestinoDiferenteDe() {
		return blUfDestinoDiferenteDe;
	}

	public void setBlUfDestinoDiferenteDe(Boolean blUfDestinoDiferenteDe) {
		this.blUfDestinoDiferenteDe = blUfDestinoDiferenteDe;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public Boolean getBlRemetenteOrigem() {
		return blRemetenteOrigem;
	}

	public void setBlRemetenteOrigem(Boolean blRemetenteOrigem) {
		this.blRemetenteOrigem = blRemetenteOrigem;
	}

	public String getDsAcaoIntegracao() {
		return dsAcaoIntegracao;
	}

	public void setDsAcaoIntegracao(String dsAcaoIntegracao) {
		this.dsAcaoIntegracao = dsAcaoIntegracao;
	}

	public String getDsProcessoIntegracao() {
		return dsProcessoIntegracao;
	}

	public void setDsProcessoIntegracao(String dsProcessoIntegracao) {
		this.dsProcessoIntegracao = dsProcessoIntegracao;
	}

	public Boolean getBlUfOrigemDiferenteDe() {
		return blUfOrigemDiferenteDe;
	}

	public void setBlUfOrigemDiferenteDe(Boolean blUfOrigemDiferenteDe) {
		this.blUfOrigemDiferenteDe = blUfOrigemDiferenteDe;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public GrupoEconomico getGrupoEconomico() {
		return grupoEconomico;
	}

	public void setGrupoEconomico(GrupoEconomico grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	public UnidadeFederativa getUnidadeFederativaOrigem() {
		return unidadeFederativaOrigem;
	}

	public void setUnidadeFederativaOrigem(
			UnidadeFederativa unidadeFederativaOrigem) {
		this.unidadeFederativaOrigem = unidadeFederativaOrigem;
	}

	public UnidadeFederativa getUnidadeFederativaDestino() {
		return unidadeFederativaDestino;
	}

	public void setUnidadeFederativaDestino(
			UnidadeFederativa unidadeFederativaDestino) {
		this.unidadeFederativaDestino = unidadeFederativaDestino;
	}

	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public DomainValue getTpDocumento() {
		return tpDocumento;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idAcaoIntegracao",
				getIdAcaoIntegracao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AcaoIntegracao))
			return false;
		AcaoIntegracao castOther = (AcaoIntegracao) other;
		return new EqualsBuilder().append(this.getIdAcaoIntegracao(),
				castOther.getIdAcaoIntegracao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAcaoIntegracao()).toHashCode();
	}
}
