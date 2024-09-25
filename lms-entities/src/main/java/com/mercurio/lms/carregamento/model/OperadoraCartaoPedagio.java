package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OperadoraCartaoPedagio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOperadoraCartaoPedagio;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String dsEnderecoWeb;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List cartaoPedagios;
    
    /** persistent field */
    private List pagtoPedagioCcs;

    public Long getIdOperadoraCartaoPedagio() {
        return this.idOperadoraCartaoPedagio;
    }

    public void setIdOperadoraCartaoPedagio(Long idOperadoraCartaoPedagio) {
        this.idOperadoraCartaoPedagio = idOperadoraCartaoPedagio;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsEnderecoWeb() {
        return this.dsEnderecoWeb;
    }

    public void setDsEnderecoWeb(String dsEnderecoWeb) {
        this.dsEnderecoWeb = dsEnderecoWeb;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CartaoPedagio.class)     
    public List getCartaoPedagios() {
        return this.cartaoPedagios;
    }

    public void setCartaoPedagios(List cartaoPedagios) {
        this.cartaoPedagios = cartaoPedagios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoPedagioCc.class)     
    public List getPagtoPedagioCcs() {
		return pagtoPedagioCcs;
	}

	public void setPagtoPedagioCcs(List pagtoPedagioCcs) {
		this.pagtoPedagioCcs = pagtoPedagioCcs;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idOperadoraCartaoPedagio",
				getIdOperadoraCartaoPedagio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OperadoraCartaoPedagio))
			return false;
        OperadoraCartaoPedagio castOther = (OperadoraCartaoPedagio) other;
		return new EqualsBuilder().append(this.getIdOperadoraCartaoPedagio(),
				castOther.getIdOperadoraCartaoPedagio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOperadoraCartaoPedagio())
            .toHashCode();
    }

}
