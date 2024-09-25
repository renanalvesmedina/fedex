package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoDocumentoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoDocumentoEntrega;

    /** persistent field */
    private VarcharI18n  dsTipoDocumentoEntrega;

    /** persistent field */
    private DomainValue tpDocumentoCobranca;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List registroDocumentoEntregas;

    /** persistent field */
    private List documentoClientes;

    public Long getIdTipoDocumentoEntrega() {
        return this.idTipoDocumentoEntrega;
    }

    public void setIdTipoDocumentoEntrega(Long idTipoDocumentoEntrega) {
        this.idTipoDocumentoEntrega = idTipoDocumentoEntrega;
    }

    public VarcharI18n getDsTipoDocumentoEntrega() {
		return dsTipoDocumentoEntrega;
    }

	public void setDsTipoDocumentoEntrega(VarcharI18n dsTipoDocumentoEntrega) {
        this.dsTipoDocumentoEntrega = dsTipoDocumentoEntrega;
    }

    public DomainValue getTpDocumentoCobranca() {
        return this.tpDocumentoCobranca;
    }

    public void setTpDocumentoCobranca(DomainValue tpDocumentoCobranca) {
        this.tpDocumentoCobranca = tpDocumentoCobranca;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.RegistroDocumentoEntrega.class)     
    public List getRegistroDocumentoEntregas() {
        return this.registroDocumentoEntregas;
    }

    public void setRegistroDocumentoEntregas(List registroDocumentoEntregas) {
        this.registroDocumentoEntregas = registroDocumentoEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DocumentoCliente.class)     
    public List getDocumentoClientes() {
        return this.documentoClientes;
    }

    public void setDocumentoClientes(List documentoClientes) {
        this.documentoClientes = documentoClientes;
    }
    
    public String getDsTipoDocumentoEntregaTpDocumentoCobranca() {
    	if (getTpDocumentoCobranca() != null) {
			return getDsTipoDocumentoEntrega().getValue(
					LocaleContextHolder.getLocale())
					+ " - "
					+ getTpDocumentoCobranca().getDescription().getValue(
							LocaleContextHolder.getLocale());
    	} else {
            
            if( getDsTipoDocumentoEntrega() != null ){            
				return getDsTipoDocumentoEntrega().getValue(
						LocaleContextHolder.getLocale());
            } else {
                return null;
            }
    	}
    }    

    public String toString() {
		return new ToStringBuilder(this).append("idTipoDocumentoEntrega",
				getIdTipoDocumentoEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoDocumentoEntrega))
			return false;
        TipoDocumentoEntrega castOther = (TipoDocumentoEntrega) other;
		return new EqualsBuilder().append(this.getIdTipoDocumentoEntrega(),
				castOther.getIdTipoDocumentoEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoDocumentoEntrega())
            .toHashCode();
    }

}
