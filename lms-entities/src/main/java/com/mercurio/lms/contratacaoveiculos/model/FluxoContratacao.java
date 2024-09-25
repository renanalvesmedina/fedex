package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class FluxoContratacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFluxoContratacao;

    /** persistent field */
    private String nrChaveLiberacao;
    
    /** persistent field */
    private BigDecimal pcValorFrete;
    
    /** persistent field */
    private Filial filialOrigem;

    /** persistent field */
    private Filial filialDestino;

    /** persistent field */
    private SolicitacaoContratacao solicitacaoContratacao;
    
    /** persistent field */
	private DomainValue tpAbrangencia;
    
    public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Long getIdFluxoContratacao() {
		return idFluxoContratacao;
	}

	public void setIdFluxoContratacao(Long idFluxoContratacao) {
		this.idFluxoContratacao = idFluxoContratacao;
	}

	public String getNrChaveLiberacao() {
		return nrChaveLiberacao;
	}

	public void setNrChaveLiberacao(String nrChaveLiberacao) {
		this.nrChaveLiberacao = nrChaveLiberacao;
	}

	public BigDecimal getPcValorFrete() {
		return pcValorFrete;
	}

	public void setPcValorFrete(BigDecimal pcValorFrete) {
		this.pcValorFrete = pcValorFrete;
	}

	public SolicitacaoContratacao getSolicitacaoContratacao() {
		return solicitacaoContratacao;
	}

	public void setSolicitacaoContratacao(
			SolicitacaoContratacao solicitacaoContratacao) {
		this.solicitacaoContratacao = solicitacaoContratacao;
	}
	
	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFluxoContratacao",
				getIdFluxoContratacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FluxoContratacao))
			return false;
        FluxoContratacao castOther = (FluxoContratacao) other;
		return new EqualsBuilder().append(this.getIdFluxoContratacao(),
				castOther.getIdFluxoContratacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFluxoContratacao())
            .toHashCode();
    }
}    