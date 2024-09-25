package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.Criterion;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class AtendimentoCliente implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAtendimentoCliente;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.OperacaoServicoLocaliza operacaoServicoLocaliza;
    
    /** transient **/
    private Criterion criterionDiasChecados;

    public Long getIdAtendimentoCliente() {
        return this.idAtendimentoCliente;
    }

    public void setIdAtendimentoCliente(Long idAtendimentoCliente) {
        this.idAtendimentoCliente = idAtendimentoCliente;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.OperacaoServicoLocaliza getOperacaoServicoLocaliza() {
        return this.operacaoServicoLocaliza;
    }

	public void setOperacaoServicoLocaliza(
			com.mercurio.lms.municipios.model.OperacaoServicoLocaliza operacaoServicoLocaliza) {
        this.operacaoServicoLocaliza = operacaoServicoLocaliza;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAtendimentoCliente",
				getIdAtendimentoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AtendimentoCliente))
			return false;
        AtendimentoCliente castOther = (AtendimentoCliente) other;
		return new EqualsBuilder().append(this.getIdAtendimentoCliente(),
				castOther.getIdAtendimentoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAtendimentoCliente())
            .toHashCode();
    }

	public Criterion getCriterionDiasChecados() {
		return criterionDiasChecados;
	}

	public void setCriterionDiasChecados(Criterion criterionDiasChecados) {
		this.criterionDiasChecados = criterionDiasChecados;
	}

}
