package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoViagemNacional implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idManifestoViagemNacional;

	/** persistent field */
	private Integer nrManifestoOrigem;

	/** nullable persistent field */
	private Manifesto manifesto;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private List<ManifestoNacionalCto> manifestoNacionalCtos;

	/** persistent field */
	private List<ManifestoNacionalVolume> manifestoNacionalVolumes;
	
	private Integer nrCto;

	private String obManifestoViagemNacional;

	public Long getIdManifestoViagemNacional() {
		return this.idManifestoViagemNacional;
	}

	public void setIdManifestoViagemNacional(Long idManifestoViagemNacional) {
		this.idManifestoViagemNacional = idManifestoViagemNacional;
	}

	public Integer getNrManifestoOrigem() {
		return this.nrManifestoOrigem;
	}

	public void setNrManifestoOrigem(Integer nrManifestoOrigem) {
		this.nrManifestoOrigem = nrManifestoOrigem;
	}

	public Manifesto getManifesto() {
		return this.manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	@ParametrizedAttribute(type = ManifestoNacionalCto.class)	 
	public List<ManifestoNacionalCto> getManifestoNacionalCtos() {
		return this.manifestoNacionalCtos;
	}

	public void setManifestoNacionalCtos(
			List<ManifestoNacionalCto> manifestoNacionalCtos) {
		this.manifestoNacionalCtos = manifestoNacionalCtos;
	}

	@ParametrizedAttribute(type = ManifestoNacionalVolume.class)
	public List<ManifestoNacionalVolume> getManifestoNacionalVolumes() {
		return manifestoNacionalVolumes;
	}

	public void setManifestoNacionalVolumes(
			List<ManifestoNacionalVolume> manifestoNacionalVolumes) {
		this.manifestoNacionalVolumes = manifestoNacionalVolumes;
	}

	public Integer getNrCto() {
		return nrCto;
	}

	public void setNrCto(Integer nrCto) {
		this.nrCto = nrCto;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idManifestoViagemNacional",
				getIdManifestoViagemNacional()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoViagemNacional))
			return false;
		ManifestoViagemNacional castOther = (ManifestoViagemNacional) other;
		return new EqualsBuilder().append(this.getIdManifestoViagemNacional(),
				castOther.getIdManifestoViagemNacional()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoViagemNacional())
			.toHashCode();
	}

	public String getObManifestoViagemNacional() {
		return obManifestoViagemNacional;
}

	public void setObManifestoViagemNacional(String obManifestoViagemNacional) {
		this.obManifestoViagemNacional = obManifestoViagemNacional;
	}

}
