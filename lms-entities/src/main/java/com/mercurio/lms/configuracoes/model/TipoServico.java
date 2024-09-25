package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoServico;

    /** persistent field */
    private VarcharI18n dsTipoServico;

    /** persistent field */
    private Boolean blPriorizar;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List servicos;

    public Long getIdTipoServico() {
        return this.idTipoServico;
    }

    public void setIdTipoServico(Long idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public VarcharI18n getDsTipoServico() {
		return dsTipoServico;
    }

	public void setDsTipoServico(VarcharI18n dsTipoServico) {
        this.dsTipoServico = dsTipoServico;
    }

    public Boolean getBlPriorizar() {
        return this.blPriorizar;
    }

    public void setBlPriorizar(Boolean blPriorizar) {
        this.blPriorizar = blPriorizar;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Servico.class)     
    public List getServicos() {
        return this.servicos;
    }

    public void setServicos(List servicos) {
        this.servicos = servicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoServico",
				getIdTipoServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoServico))
			return false;
        TipoServico castOther = (TipoServico) other;
		return new EqualsBuilder().append(this.getIdTipoServico(),
				castOther.getIdTipoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoServico()).toHashCode();
    }

}
