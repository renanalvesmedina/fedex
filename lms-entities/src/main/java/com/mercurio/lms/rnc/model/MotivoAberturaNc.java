package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoAberturaNc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoAberturaNc;

    /** persistent field */
    private VarcharI18n dsMotivoAbertura;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private DomainValue tpMotivo;

    /** persistent field */
    private Boolean blExigeDocServico;

    /** persistent field */
    private Boolean blPermiteIndenizacao;

    /** persistent field */
    private Boolean blExigeValor;

    /** persistent field */
    private Boolean blExigeQtdVolumes;
    
    /** persistent field */
    private List motAberturaMotDisposicoes;

    /** persistent field */
    private List setorMotivoAberturaNcs;

    /** persistent field */
    private List ocorrenciaNaoConformidades;

    /** persistent field */
    private List descricaoPadraoNcs;

    public Long getIdMotivoAberturaNc() {
        return this.idMotivoAberturaNc;
    }

    public void setIdMotivoAberturaNc(Long idMotivoAberturaNc) {
        this.idMotivoAberturaNc = idMotivoAberturaNc;
    }

    public VarcharI18n getDsMotivoAbertura() {
		return dsMotivoAbertura;
    }

	public void setDsMotivoAbertura(VarcharI18n dsMotivoAbertura) {
        this.dsMotivoAbertura = dsMotivoAbertura;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpMotivo() {
		return tpMotivo;
	}

	public void setTpMotivo(DomainValue tpMotivo) {
		this.tpMotivo = tpMotivo;
	}

    public Boolean getBlExigeDocServico() {
        return this.blExigeDocServico;
    }

    public void setBlExigeDocServico(Boolean blExigeDocServico) {
        this.blExigeDocServico = blExigeDocServico;
    }

    public Boolean getBlPermiteIndenizacao() {
        return this.blPermiteIndenizacao;
    }

    public void setBlPermiteIndenizacao(Boolean blPermiteIndenizacao) {
        this.blPermiteIndenizacao = blPermiteIndenizacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.MotAberturaMotDisposicao.class)     
    public List getMotAberturaMotDisposicoes() {
        return this.motAberturaMotDisposicoes;
    }

    public void setMotAberturaMotDisposicoes(List motAberturaMotDisposicoes) {
        this.motAberturaMotDisposicoes = motAberturaMotDisposicoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.SetorMotivoAberturaNc.class)     
    public List getSetorMotivoAberturaNcs() {
        return this.setorMotivoAberturaNcs;
    }

    public void setSetorMotivoAberturaNcs(List setorMotivoAberturaNcs) {
        this.setorMotivoAberturaNcs = setorMotivoAberturaNcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade.class)     
    public List getOcorrenciaNaoConformidades() {
        return this.ocorrenciaNaoConformidades;
    }

    public void setOcorrenciaNaoConformidades(List ocorrenciaNaoConformidades) {
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.DescricaoPadraoNc.class)     
    public List getDescricaoPadraoNcs() {
        return this.descricaoPadraoNcs;
    }

    public void setDescricaoPadraoNcs(List descricaoPadraoNcs) {
        this.descricaoPadraoNcs = descricaoPadraoNcs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoAberturaNc",
				getIdMotivoAberturaNc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoAberturaNc))
			return false;
        MotivoAberturaNc castOther = (MotivoAberturaNc) other;
		return new EqualsBuilder().append(this.getIdMotivoAberturaNc(),
				castOther.getIdMotivoAberturaNc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoAberturaNc())
            .toHashCode();
    }

	public Boolean getBlExigeQtdVolumes() {
		return blExigeQtdVolumes;
	}

	public void setBlExigeQtdVolumes(Boolean blExigeQtdVolumes) {
		this.blExigeQtdVolumes = blExigeQtdVolumes;
	}

	public Boolean getBlExigeValor() {
		return blExigeValor;
	}

	public void setBlExigeValor(Boolean blExigeValor) {
		this.blExigeValor = blExigeValor;
	}
}