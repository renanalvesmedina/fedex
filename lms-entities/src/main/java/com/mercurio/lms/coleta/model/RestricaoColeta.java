package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RestricaoColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRestricaoColeta;

    /** nullable persistent field */
    private BigDecimal psMaximoVolume;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private com.mercurio.lms.coleta.model.ProdutoProibido produtoProibido;

    public Long getIdRestricaoColeta() {
        return this.idRestricaoColeta;
    }

    public void setIdRestricaoColeta(Long idRestricaoColeta) {
        this.idRestricaoColeta = idRestricaoColeta;
    }

    public BigDecimal getPsMaximoVolume() {
        return this.psMaximoVolume;
    }

    public void setPsMaximoVolume(BigDecimal psMaximoVolume) {
        this.psMaximoVolume = psMaximoVolume;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    public com.mercurio.lms.coleta.model.ProdutoProibido getProdutoProibido() {
        return this.produtoProibido;
    }

	public void setProdutoProibido(
			com.mercurio.lms.coleta.model.ProdutoProibido produtoProibido) {
        this.produtoProibido = produtoProibido;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRestricaoColeta",
				getIdRestricaoColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RestricaoColeta))
			return false;
        RestricaoColeta castOther = (RestricaoColeta) other;
		return new EqualsBuilder().append(this.getIdRestricaoColeta(),
				castOther.getIdRestricaoColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRestricaoColeta())
            .toHashCode();
    }

}
