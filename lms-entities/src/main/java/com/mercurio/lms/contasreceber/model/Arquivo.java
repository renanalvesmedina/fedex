package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idArquivo;

    /** persistent field */
    private YearMonthDay dtExpiracao;

    /** persistent field */
    private byte[] arquivo;
    
    /** persistent field */
    private String nmArquivo;
    
    public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Long getIdArquivo() {
		return idArquivo;
	}

	public void setIdArquivo(Long idArquivo) {
		this.idArquivo = idArquivo;
	}

	public YearMonthDay getDtExpiracao() {
		return dtExpiracao;
	}

	public void setDtExpiracao(YearMonthDay dtExpiracao) {
		this.dtExpiracao = dtExpiracao;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idArquivo", getIdArquivo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Arquivo))
			return false;
        Arquivo castOther = (Arquivo) other;
		return new EqualsBuilder().append(this.getIdArquivo(),
				castOther.getIdArquivo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdArquivo()).toHashCode();
    }

}
