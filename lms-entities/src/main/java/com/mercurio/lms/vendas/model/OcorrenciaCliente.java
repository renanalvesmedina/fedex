package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaCliente;

    /** persistent field */
    private VarcharI18n dsOcorrenciaCliente;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;
    
    private com.mercurio.lms.entrega.model.OcorrenciaEntrega ocorrenciaEntrega;
    
    public Long getIdOcorrenciaCliente() {
        return this.idOcorrenciaCliente;
    }

    public void setIdOcorrenciaCliente(Long idOcorrenciaCliente) {
        this.idOcorrenciaCliente = idOcorrenciaCliente;
    }

    public VarcharI18n getDsOcorrenciaCliente() {
		return dsOcorrenciaCliente;
    }

	public void setDsOcorrenciaCliente(VarcharI18n dsOcorrenciaCliente) {
        this.dsOcorrenciaCliente = dsOcorrenciaCliente;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaCliente",
				getIdOcorrenciaCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaCliente))
			return false;
        OcorrenciaCliente castOther = (OcorrenciaCliente) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaCliente(),
				castOther.getIdOcorrenciaCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaCliente())
            .toHashCode();
    }

	public com.mercurio.lms.entrega.model.OcorrenciaEntrega getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public void setOcorrenciaEntrega(
			com.mercurio.lms.entrega.model.OcorrenciaEntrega ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public String getCodigoDescricaoEntrega() {
    	StringBuffer sb = new StringBuffer();
		if (this.getOcorrenciaEntrega() != null
				&& this.getOcorrenciaEntrega().getCdOcorrenciaEntrega() != null
				&& this.getOcorrenciaEntrega().getDsOcorrenciaEntrega() != null) {
    	   sb.append(this.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
    	   sb.append(" - ");
    	   sb.append(this.getOcorrenciaEntrega().getDsOcorrenciaEntrega());	
	    }   
 	   return sb.toString();
	}

}
