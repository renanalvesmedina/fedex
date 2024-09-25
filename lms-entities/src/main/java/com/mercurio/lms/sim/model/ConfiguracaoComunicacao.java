package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ConfiguracaoComunicacao implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;
	
	/** identifier field */
    private Long idConfiguracaoComunicacao;

    /** persistent field */
    private DomainValue tpCliente;
    
    /** persistent field */
    private DomainValue tpDocumento;
    
    /** persistent field */
    private DomainValue tpAcessoEvento;

    /** persistent field */
    private Boolean blSomenteDiasUteis;

    /** persistent field */
    private Boolean blComunicacaoCadaEvento;

    /** persistent field */
    private DomainValue tpMeioComunicacao;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Integer nrIntervaloComunicacao;

    /** nullable persistent field */
    private TimeOfDay hrDeterminado;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private List eventoClienteRecebes;

    /** persistent field */
    private List contatoComunicacoes;

    public Long getIdConfiguracaoComunicacao() {
        return this.idConfiguracaoComunicacao;
    }

    public void setIdConfiguracaoComunicacao(Long idConfiguracaoComunicacao) {
        this.idConfiguracaoComunicacao = idConfiguracaoComunicacao;
    }

    public DomainValue getTpCliente() {
        return this.tpCliente;
    }

    public void setTpCliente(DomainValue tpCliente) {
        this.tpCliente = tpCliente;
    }

    public DomainValue getTpAcessoEvento() {
        return this.tpAcessoEvento;
    }

    public void setTpAcessoEvento(DomainValue tpAcessoEvento) {
        this.tpAcessoEvento = tpAcessoEvento;
    }

    public Boolean getBlSomenteDiasUteis() {
        return this.blSomenteDiasUteis;
    }

    public void setBlSomenteDiasUteis(Boolean blSomenteDiasUteis) {
        this.blSomenteDiasUteis = blSomenteDiasUteis;
    }

    public Boolean getBlComunicacaoCadaEvento() {
        return this.blComunicacaoCadaEvento;
    }

    public void setBlComunicacaoCadaEvento(Boolean blComunicacaoCadaEvento) {
        this.blComunicacaoCadaEvento = blComunicacaoCadaEvento;
    }

    public DomainValue getTpMeioComunicacao() {
        return this.tpMeioComunicacao;
    }

    public void setTpMeioComunicacao(DomainValue tpMeioComunicacao) {
        this.tpMeioComunicacao = tpMeioComunicacao;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Integer getNrIntervaloComunicacao() {
		return nrIntervaloComunicacao;
	}

	public void setNrIntervaloComunicacao(Integer nrIntervaloComunicacao) {
		this.nrIntervaloComunicacao = nrIntervaloComunicacao;
	}

	public TimeOfDay getHrDeterminado() {
        return this.hrDeterminado;
    }

    public void setHrDeterminado(TimeOfDay hrDeterminado) {
        this.hrDeterminado = hrDeterminado;
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

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.EventoClienteRecebe.class)     
    public List getEventoClienteRecebes() {
        return this.eventoClienteRecebes;
    }

    public void setEventoClienteRecebes(List eventoClienteRecebes) {
        this.eventoClienteRecebes = eventoClienteRecebes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.ContatoComunicacao.class)     
    public List getContatoComunicacoes() {
        return this.contatoComunicacoes;
    }

    public void setContatoComunicacoes(List contatoComunicacoes) {
        this.contatoComunicacoes = contatoComunicacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConfiguracaoComunicacao",
				getIdConfiguracaoComunicacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ConfiguracaoComunicacao))
			return false;
        ConfiguracaoComunicacao castOther = (ConfiguracaoComunicacao) other;
		return new EqualsBuilder().append(this.getIdConfiguracaoComunicacao(),
				castOther.getIdConfiguracaoComunicacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConfiguracaoComunicacao())
            .toHashCode();
    }

	public DomainValue getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

}
