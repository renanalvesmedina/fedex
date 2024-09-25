package com.mercurio.lms.sim.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ContatoComunicacao implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idContatoComunicacao;

    /** persistent field */
    private DomainValue tpMeioTransmissao;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Contato contato;

    /** persistent field */
    private com.mercurio.lms.sim.model.ConfiguracaoComunicacao configuracaoComunicacao;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.TelefoneContato telefoneContato;
    
    public Long getIdContatoComunicacao() {
        return this.idContatoComunicacao;
    }

    public void setIdContatoComunicacao(Long idContatoComunicacao) {
        this.idContatoComunicacao = idContatoComunicacao;
    }

    public DomainValue getTpMeioTransmissao() {
        return this.tpMeioTransmissao;
    }

    public void setTpMeioTransmissao(DomainValue tpMeioTransmissao) {
        this.tpMeioTransmissao = tpMeioTransmissao;
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

    public com.mercurio.lms.configuracoes.model.Contato getContato() {
        return this.contato;
    }

    public void setContato(com.mercurio.lms.configuracoes.model.Contato contato) {
        this.contato = contato;
    }

    public com.mercurio.lms.sim.model.ConfiguracaoComunicacao getConfiguracaoComunicacao() {
        return this.configuracaoComunicacao;
    }

	public void setConfiguracaoComunicacao(
			com.mercurio.lms.sim.model.ConfiguracaoComunicacao configuracaoComunicacao) {
        this.configuracaoComunicacao = configuracaoComunicacao;
    }

    public com.mercurio.lms.configuracoes.model.TelefoneContato getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(
			com.mercurio.lms.configuracoes.model.TelefoneContato telefoneContato) {
		this.telefoneContato = telefoneContato;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idContatoComunicacao",
				getIdContatoComunicacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ContatoComunicacao))
			return false;
        ContatoComunicacao castOther = (ContatoComunicacao) other;
		return new EqualsBuilder().append(this.getIdContatoComunicacao(),
				castOther.getIdContatoComunicacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdContatoComunicacao())
            .toHashCode();
    }

}
