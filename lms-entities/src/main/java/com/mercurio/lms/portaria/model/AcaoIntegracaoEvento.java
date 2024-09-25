package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class AcaoIntegracaoEvento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idAcaoIntegracaoEvento;
	private AcaoIntegracao acaoIntegracao;
	private Filial filial;
	private DomainValue tpDocumento;
	private Long nrDocumento;
	private Long nrAgrupador;
	private String dsInformacao;
	private String dhGeracaoTzr;
	
	public Long getIdAcaoIntegracaoEvento() {
		return idAcaoIntegracaoEvento;
	}

	public void setIdAcaoIntegracaoEvento(Long idAcaoIntegracaoEvento) {
		this.idAcaoIntegracaoEvento = idAcaoIntegracaoEvento;
	}

	public Long getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(Long nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public AcaoIntegracao getAcaoIntegracao() {
		return acaoIntegracao;
	}

	public void setAcaoIntegracao(AcaoIntegracao acaoIntegracao) {
		this.acaoIntegracao = acaoIntegracao;
	}

	public DomainValue getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}	

	public String getDsInformacao() {
		return dsInformacao;
	}

	public void setDsInformacao(String dsInformacao) {
		this.dsInformacao = dsInformacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idAcaoIntegracaoEventos",
				getIdAcaoIntegracaoEvento()).toString();
	}

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_ACAO_INTEGRACAO", idAcaoIntegracaoEvento)
				.append("ID_ACAO_INTEGRACAO_EVENTO", acaoIntegracao != null ? acaoIntegracao.getIdAcaoIntegracao() : null)
				.append("NR_DOCUMENTO", nrDocumento)
				.append("TP_DOCUMENTO", tpDocumento != null ? tpDocumento.getValue() : null)
				.append("NR_AGRUPADOR", nrAgrupador)
				.append("DS_INFORMACAO", dsInformacao)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AcaoIntegracaoEvento))
			return false;
		AcaoIntegracaoEvento castOther = (AcaoIntegracaoEvento) other;
		return new EqualsBuilder().append(this.getIdAcaoIntegracaoEvento(),
				castOther.getIdAcaoIntegracaoEvento()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAcaoIntegracaoEvento())
			.toHashCode();
	}

	public void setNrAgrupador(Long nrAgrupador) {
		this.nrAgrupador = nrAgrupador;
	}

	public Long getNrAgrupador() {
		return nrAgrupador;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Filial getFilial() {
		return filial;
	}

	public String getDhGeracaoTzr() {
		return dhGeracaoTzr;
	}

	public void setDhGeracaoTzr(String dhGeracaoTzr) {
		this.dhGeracaoTzr = dhGeracaoTzr;
	}

}
