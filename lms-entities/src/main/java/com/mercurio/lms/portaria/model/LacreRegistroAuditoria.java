package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.LacreControleCarga;

public class LacreRegistroAuditoria  implements Serializable {

	private static final long serialVersionUID = 1L;

	 /** identifier field */
    private Long idLacreRegistroAuditoria;

    /** persistent field */
    private Boolean blOriginal;
    
    /** persistent field */
    private LacreControleCarga lacreControleCarga;
    
    /** persistent field */
    private RegistroAuditoria registroAuditoria;

	public Boolean getBlOriginal() {
		return blOriginal;
	}

	public void setBlOriginal(Boolean blOriginal) {
		this.blOriginal = blOriginal;
	}

	public Long getIdLacreRegistroAuditoria() {
		return idLacreRegistroAuditoria;
	}

	public void setIdLacreRegistroAuditoria(Long idLacreRegistroAuditoria) {
		this.idLacreRegistroAuditoria = idLacreRegistroAuditoria;
	}

	public LacreControleCarga getLacreControleCarga() {
		return lacreControleCarga;
	}

	public void setLacreControleCarga(LacreControleCarga lacreControleCarga) {
		this.lacreControleCarga = lacreControleCarga;
	}

	public RegistroAuditoria getRegistroAuditoria() {
		return registroAuditoria;
	}

	public void setRegistroAuditoria(RegistroAuditoria registroAuditoria) {
		this.registroAuditoria = registroAuditoria;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idLacreRegistroAuditoria",
				getIdLacreRegistroAuditoria()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LacreRegistroAuditoria))
			return false;
        LacreRegistroAuditoria castOther = (LacreRegistroAuditoria) other;
		return new EqualsBuilder().append(this.getIdLacreRegistroAuditoria(),
				castOther.getIdLacreRegistroAuditoria()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdLacreRegistroAuditoria())
            .toHashCode();
	}

}
