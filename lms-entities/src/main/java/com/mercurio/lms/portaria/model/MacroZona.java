package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

public class MacroZona implements Serializable {

	private static final long serialVersionUID = 1L;
	
    /** identifier field */
    private Long idMacroZona;

    /** persistent field */
    private String dsMacroZona;
    
    /** nullable persistent field */
    private BigDecimal nrCodigoBarras;
    
    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private Terminal terminal;

    public Long getIdMacroZona() {
		return idMacroZona;
	}

	public void setIdMacroZona(Long idMacroZona) {
		this.idMacroZona = idMacroZona;
	}

	public String getDsMacroZona() {
		return dsMacroZona;
	}

	public void setDsMacroZona(String dsMacroZona) {
		this.dsMacroZona = dsMacroZona;
	}

	public BigDecimal getNrCodigoBarras() {
		return nrCodigoBarras;
	}

	public void setNrCodigoBarras(BigDecimal nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public String toString() {
        return new ToStringBuilder(this)
				.append("idMacroZona", getIdMacroZona()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MacroZona))
			return false;
        MacroZona castOther = (MacroZona) other;
		return new EqualsBuilder().append(this.getIdMacroZona(),
				castOther.getIdMacroZona()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMacroZona()).toHashCode();
    }
    
    @Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idMacroZona", this.getIdMacroZona());
		bean.put("dsMacroZona", this.getDsMacroZona());
		return bean;
	}
}
