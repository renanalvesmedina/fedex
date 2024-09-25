package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class MilkRun implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMilkRun;

    /** persistent field */
    private Boolean blColetasInterdependentes;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private List milkRemetentes;

    /** persistent field */
    private List pedidoColetas;

    private Integer versao;
    
    public Long getIdMilkRun() {
        return this.idMilkRun;
    }

    public void setIdMilkRun(Long idMilkRun) {
        this.idMilkRun = idMilkRun;
    }

    public Boolean getBlColetasInterdependentes() {
        return this.blColetasInterdependentes;
    }

    public void setBlColetasInterdependentes(Boolean blColetasInterdependentes) {
        this.blColetasInterdependentes = blColetasInterdependentes;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRemetente.class)     
    public List getMilkRemetentes() {
        return this.milkRemetentes;
    }

    public void setMilkRemetentes(List milkRemetentes) {
        this.milkRemetentes = milkRemetentes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColeta.class)     
    public List getPedidoColetas() {
        return this.pedidoColetas;
    }

	public void setPedidoColetas(List pedidoColetas) {
        this.pedidoColetas = pedidoColetas;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idMilkRun", getIdMilkRun())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MilkRun))
			return false;
        MilkRun castOther = (MilkRun) other;
		return new EqualsBuilder().append(this.getIdMilkRun(),
				castOther.getIdMilkRun()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMilkRun()).toHashCode();
    }

}
