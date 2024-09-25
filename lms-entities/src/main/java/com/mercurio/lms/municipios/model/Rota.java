package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Rota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRota;
    
    /** persistent field */
    private String dsRota;

    /** persistent field */
    private Boolean blEnvolveParceira;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List rotaIdaVoltas;

    /** persistent field */
    private List solicitacaoContratacoes;

    /** persistent field */
    private List rotaPostoControles;

    /** persistent field */
    private List controleCargas;

    /** persistent field */
    private List filialRotas;
    
    private int numeroPostos;

    public int getNumeroPostos() {
    	return numeroPostos;
	}

	public void setNumeroPostos(int numeroPostos) {
		this.numeroPostos = numeroPostos;
	}

    public Long getIdRota() {
        return this.idRota;
    }

    public void setIdRota(Long idRota) {
        this.idRota = idRota;
    }

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}
	
    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaIdaVolta.class)     
    public List getRotaIdaVoltas() {
        return this.rotaIdaVoltas;
    }

    public void setRotaIdaVoltas(List rotaIdaVoltas) {
        this.rotaIdaVoltas = rotaIdaVoltas;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao.class)     
    public List getSolicitacaoContratacoes() {
        return this.solicitacaoContratacoes;
    }

    public void setSolicitacaoContratacoes(List solicitacaoContratacoes) {
        this.solicitacaoContratacoes = solicitacaoContratacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.RotaPostoControle.class)     
    public List getRotaPostoControles() {
        return this.rotaPostoControles;
    }

    public void setRotaPostoControles(List rotaPostoControles) {
        this.rotaPostoControles = rotaPostoControles;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FilialRota.class) 
    public List getFilialRotas() {
		return filialRotas;
	}

	public void setFilialRotas(List filialRotas) {
		this.filialRotas = filialRotas;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idRota", getIdRota())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Rota))
			return false;
        Rota castOther = (Rota) other;
		return new EqualsBuilder().append(this.getIdRota(),
				castOther.getIdRota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRota()).toHashCode();
    }

	public Boolean getBlEnvolveParceira() {
		return blEnvolveParceira;
	}

	public void setBlEnvolveParceira(Boolean blEnvolveParceira) {
		this.blEnvolveParceira = blEnvolveParceira;
	}

}
