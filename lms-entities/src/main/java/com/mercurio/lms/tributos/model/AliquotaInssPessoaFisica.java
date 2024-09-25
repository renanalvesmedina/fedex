package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaInssPessoaFisica implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAliquotaInssPessoaFisica;

    /** persistent field */
    private YearMonthDay dtInicioVigencia;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private BigDecimal vlSalarioBase;

    /** persistent field */
    private BigDecimal vlMaximoRecolhimento;
    
    /** persistent field */
    private BigDecimal pcBaseCalcReduzida;

    public Long getIdAliquotaInssPessoaFisica() {
        return this.idAliquotaInssPessoaFisica;
    }

    public void setIdAliquotaInssPessoaFisica(Long idAliquotaInssPessoaFisica) {
        this.idAliquotaInssPessoaFisica = idAliquotaInssPessoaFisica;
    }

    public YearMonthDay getDtInicioVigencia() {
        return this.dtInicioVigencia;
    }

    public void setDtInicioVigencia(YearMonthDay dtInicioVigencia) {
        this.dtInicioVigencia = dtInicioVigencia;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public BigDecimal getVlSalarioBase() {
        return this.vlSalarioBase;
    }

    public void setVlSalarioBase(BigDecimal vlSalarioBase) {
        this.vlSalarioBase = vlSalarioBase;
    }

    public BigDecimal getVlMaximoRecolhimento() {
        return this.vlMaximoRecolhimento;
    }

    public void setVlMaximoRecolhimento(BigDecimal vlMaximoRecolhimento) {
        this.vlMaximoRecolhimento = vlMaximoRecolhimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaInssPessoaFisica",
				getIdAliquotaInssPessoaFisica()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaInssPessoaFisica))
			return false;
        AliquotaInssPessoaFisica castOther = (AliquotaInssPessoaFisica) other;
		return new EqualsBuilder().append(this.getIdAliquotaInssPessoaFisica(),
				castOther.getIdAliquotaInssPessoaFisica()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaInssPessoaFisica())
            .toHashCode();
    }

	public BigDecimal getPcBaseCalcReduzida() {
		return pcBaseCalcReduzida;
	}

	public void setPcBaseCalcReduzida(BigDecimal pcBaseCalcReduzida) {
		this.pcBaseCalcReduzida = pcBaseCalcReduzida;
	}

}
