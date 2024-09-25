package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DiaSemana implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDiaSemana;

    private Boolean blUtilDom;
    
    private Boolean blUtilSeg;
    
    private Boolean blUtilTer;
    
    private Boolean blUtilQua;
    
    private Boolean blUtilQui;
    
    private Boolean blUtilSex;
    
    private Boolean blUtilSab;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;
    
    public Long getIdDiaSemana() {
        return this.idDiaSemana;
    }

    public void setIdDiaSemana(Long idDiaSemana) {
        this.idDiaSemana = idDiaSemana;
    }

    public Boolean getBlUtilDom() {
		return blUtilDom;
	}

	public void setBlUtilDom(Boolean blUtilDom) {
		this.blUtilDom = blUtilDom;
	}

	public Boolean getBlUtilQua() {
		return blUtilQua;
	}

	public void setBlUtilQua(Boolean blUtilQua) {
		this.blUtilQua = blUtilQua;
	}

	public Boolean getBlUtilQui() {
		return blUtilQui;
	}

	public void setBlUtilQui(Boolean blUtilQui) {
		this.blUtilQui = blUtilQui;
	}

	public Boolean getBlUtilSab() {
		return blUtilSab;
	}

	public void setBlUtilSab(Boolean blUtilSab) {
		this.blUtilSab = blUtilSab;
	}

	public Boolean getBlUtilSeg() {
		return blUtilSeg;
	}

	public void setBlUtilSeg(Boolean blUtilSeg) {
		this.blUtilSeg = blUtilSeg;
	}

	public Boolean getBlUtilSex() {
		return blUtilSex;
	}

	public void setBlUtilSex(Boolean blUtilSex) {
		this.blUtilSex = blUtilSex;
	}

	public Boolean getBlUtilTer() {
		return blUtilTer;
	}

	public void setBlUtilTer(Boolean blUtilTer) {
		this.blUtilTer = blUtilTer;
	}

	public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }
    
	public String toString() {
        return new ToStringBuilder(this)
				.append("idDiaSemana", getIdDiaSemana()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DiaSemana))
			return false;
        DiaSemana castOther = (DiaSemana) other;
		return new EqualsBuilder().append(this.getIdDiaSemana(),
				castOther.getIdDiaSemana()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDiaSemana()).toHashCode();
    }
}
