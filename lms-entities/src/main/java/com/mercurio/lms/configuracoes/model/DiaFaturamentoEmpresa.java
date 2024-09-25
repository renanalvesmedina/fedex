package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class DiaFaturamentoEmpresa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDiaFaturamentoEmpresa;

    /** persistent field */
    private DomainValue tpPeriodicidade;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Byte ddCorte;

    private String ddCorteExt;
    
    public Long getIdDiaFaturamentoEmpresa() {
        return this.idDiaFaturamentoEmpresa;
    }

    public void setIdDiaFaturamentoEmpresa(Long idDiaFaturamentoEmpresa) {
        this.idDiaFaturamentoEmpresa = idDiaFaturamentoEmpresa;
    }

    public DomainValue getTpPeriodicidade() {
        return this.tpPeriodicidade;
    }

    public void setTpPeriodicidade(DomainValue tpPeriodicidade) {
        this.tpPeriodicidade = tpPeriodicidade;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Byte getDdCorte() {
        return this.ddCorte;
    }

    public void setDdCorte(Byte ddCorte) {
        this.ddCorte = ddCorte;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDiaFaturamentoEmpresa",
				getIdDiaFaturamentoEmpresa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DiaFaturamentoEmpresa))
			return false;
        DiaFaturamentoEmpresa castOther = (DiaFaturamentoEmpresa) other;
		return new EqualsBuilder().append(this.getIdDiaFaturamentoEmpresa(),
				castOther.getIdDiaFaturamentoEmpresa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDiaFaturamentoEmpresa())
            .toHashCode();
    }

	public String getDdCorteExt() {
		return ddCorteExt;
	}

	public void setDdCorteExt(String ddCorteExt) {
		this.ddCorteExt = ddCorteExt;
	}

	public Byte getNmDiaSemana() {
		return ddCorte;
	}

}
