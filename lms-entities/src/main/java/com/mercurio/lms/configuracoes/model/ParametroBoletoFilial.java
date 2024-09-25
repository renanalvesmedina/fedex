package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.Filial;

public class ParametroBoletoFilial implements Serializable{

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idParametroBoletoFilial;
	
	/** identifier field */
	private YearMonthDay dtVigenciaInicial;
	
	/** identifier field */
	private YearMonthDay dtVigenciaFinal;
	
	/** identifier field */
	private Boolean blValorLiquido;
	
	/** identifier field */
	private Filial filial;
    
    private Boolean blWorkflowCancelamento;

	public Boolean getBlWorkflowCancelamento() {
        return blWorkflowCancelamento;
    }

    public void setBlWorkflowCancelamento(Boolean blWorkflowCancelamento) {
        this.blWorkflowCancelamento = blWorkflowCancelamento;
    }

    public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
	public Boolean getBlValorLiquido() {
		return blValorLiquido;
	}

	public void setBlValorLiquido(Boolean blValorLiquido) {
		this.blValorLiquido = blValorLiquido;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Long getIdParametroBoletoFilial() {
		return idParametroBoletoFilial;
	}

	public void setIdParametroBoletoFilial(Long idParametroBoletoFilial) {
		this.idParametroBoletoFilial = idParametroBoletoFilial;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idParametroBoletoFilial",
				getIdParametroBoletoFilial()).toString();
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroBoletoFilial))
			return false;
        ParametroBoletoFilial castOther = (ParametroBoletoFilial) other;
		return new EqualsBuilder().append(this.getIdParametroBoletoFilial(),
				castOther.getIdParametroBoletoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroBoletoFilial())
            .toHashCode();
    }
	
}
