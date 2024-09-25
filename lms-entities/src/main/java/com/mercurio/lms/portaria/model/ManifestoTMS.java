package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;

public class ManifestoTMS implements Serializable {

	private static final long serialVersionUID = 1L;
	
    /** identifier field */
    private Long idManifestoTMS;

    /** persistent field */
    private Long nrManifesto;
    
    /** nullable persistent field */
    private String dsVeiculo;
    
    /** persistent field */
    private Integer nrQtdCtos;
    
    /** persistent field */
    private DateTime dhChegada;

    /** persistent field */
    private Filial filial;
    
    /** persistent field */
    private Filial filialManifesto;

    /** persistent field */
    private DoctoServico doctoServico;

    public Long getIdManifestoTMS() {
		return idManifestoTMS;
	}

	public void setIdManifestoTMS(Long idManifestoTMS) {
		this.idManifestoTMS = idManifestoTMS;
	}

	public Long getNrManifesto() {
		return nrManifesto;
	}

	public void setNrManifesto(Long nrManifesto) {
		this.nrManifesto = nrManifesto;
	}

	public String getDsVeiculo() {
		return dsVeiculo;
	}

	public void setDsVeiculo(String dsVeiculo) {
		this.dsVeiculo = dsVeiculo;
	}

	public Integer getNrQtdCtos() {
		return nrQtdCtos;
	}

	public void setNrQtdCtos(Integer nrQtdCtos) {
		this.nrQtdCtos = nrQtdCtos;
	}

	public DateTime getDhChegada() {
		return dhChegada;
	}

	public void setDhChegada(DateTime dhChegada) {
		this.dhChegada = dhChegada;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Filial getFilialManifesto() {
		return filialManifesto;
	}

	public void setFilialManifesto(Filial filialManifesto) {
		this.filialManifesto = filialManifesto;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idManifestoTMS",
				getIdManifestoTMS()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoTMS))
			return false;
        ManifestoTMS castOther = (ManifestoTMS) other;
		return new EqualsBuilder().append(this.getIdManifestoTMS(),
				castOther.getIdManifestoTMS()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoTMS()).toHashCode();
    }
}
