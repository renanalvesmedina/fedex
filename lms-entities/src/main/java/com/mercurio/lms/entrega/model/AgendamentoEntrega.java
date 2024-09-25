package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendamentoEntrega implements Serializable {

	private static final long serialVersionUID = 3L;

    /** identifier field */
    private Long idAgendamentoEntrega;

    /** persistent field */
    private DomainValue tpAgendamento;

    /** persistent field */
    private DateTime dhContato;

    /** persistent field */
    private DomainValue tpSituacaoAgendamento;

    /** persistent field */
    private String nmContato;

    /** persistent field */
    private String nrTelefone;

    /** persistent field */
    private Boolean blCartao;

    /** nullable persistent field */
    private YearMonthDay dtAgendamento;

    /** nullable persistent field */
    private TimeOfDay hrPreferenciaInicial;

    /** nullable persistent field */
    private TimeOfDay hrPreferenciaFinal;

    /** nullable persistent field */
    private String nrDdd;

    /** nullable persistent field */
    private String nrRamal;
 
    /** nullable persistent field */
    private DateTime dhCancelamento;

    /** nullable persistent field */
    private String obAgendamentoEntrega;

    /** nullable persistent field */
    private String obCancelamento;

    /** nullable persistent field */
    private String obTentativa;
    
    /** persistent field */
    private com.mercurio.lms.entrega.model.MotivoAgendamento motivoAgendamentoByIdMotivoCancelamento;

    /** persistent field */
    private com.mercurio.lms.entrega.model.MotivoAgendamento motivoAgendamentoByIdMotivoReagendamento;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCriacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCancelamento;

    /** persistent field */
    private com.mercurio.lms.entrega.model.Turno turno;

    /** persistent field */
    private com.mercurio.lms.entrega.model.AgendamentoEntrega reagendamento;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    private AgendamentoEntrega agendamentoEntregaOriginal;

    /** persistent field */
    private List agendamentoDoctoServicos;

    private DateTime dhFechamento;
    
    private DateTime dhEnvio;
    
    private String dsEmailTomador;
    
    private String dsEmailDestinatario;
    
    public Long getIdAgendamentoEntrega() {
        return this.idAgendamentoEntrega;
    }

    public void setIdAgendamentoEntrega(Long idAgendamentoEntrega) {
        this.idAgendamentoEntrega = idAgendamentoEntrega;
    }

    public DomainValue getTpAgendamento() {
        return this.tpAgendamento;
    }

    public void setTpAgendamento(DomainValue tpAgendamento) {
        this.tpAgendamento = tpAgendamento;
    }

    public DateTime getDhContato() {
        return this.dhContato;
    }

    public void setDhContato(DateTime dhContato) {
        this.dhContato = dhContato;
    }

    public DomainValue getTpSituacaoAgendamento() {
        return this.tpSituacaoAgendamento;
    }

    public void setTpSituacaoAgendamento(DomainValue tpSituacaoAgendamento) {
        this.tpSituacaoAgendamento = tpSituacaoAgendamento;
    }

    public String getNmContato() {
        return this.nmContato;
    }

    public void setNmContato(String nmContato) {
        this.nmContato = nmContato;
    }

    public String getNrTelefone() {
        return this.nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public Boolean getBlCartao() {
        return this.blCartao;
    }

    public void setBlCartao(Boolean blCartao) {
        this.blCartao = blCartao;
    }

    public YearMonthDay getDtAgendamento() {
        return this.dtAgendamento;
    }

    public void setDtAgendamento(YearMonthDay dtAgendamento) {
        this.dtAgendamento = dtAgendamento;
    }

    public TimeOfDay getHrPreferenciaInicial() {
        return this.hrPreferenciaInicial;
    }

    public void setHrPreferenciaInicial(TimeOfDay hrPreferenciaInicial) {
        this.hrPreferenciaInicial = hrPreferenciaInicial;
    }

    public TimeOfDay getHrPreferenciaFinal() {
        return this.hrPreferenciaFinal;
    }

    public void setHrPreferenciaFinal(TimeOfDay hrPreferenciaFinal) {
        this.hrPreferenciaFinal = hrPreferenciaFinal;
    }

    public String getNrDdd() {
        return this.nrDdd;
    }

    public void setNrDdd(String nrDdd) {
        this.nrDdd = nrDdd;
    }

    public String getNrRamal() {
        return this.nrRamal;
    }

    public void setNrRamal(String nrRamal) {
        this.nrRamal = nrRamal;
    }

    public DateTime getDhCancelamento() {
        return this.dhCancelamento;
    }

    public void setDhCancelamento(DateTime dhCancelamento) {
        this.dhCancelamento = dhCancelamento;
    }

    public String getObAgendamentoEntrega() {
        return this.obAgendamentoEntrega;
    }

    public void setObAgendamentoEntrega(String obAgendamentoEntrega) {
        this.obAgendamentoEntrega = obAgendamentoEntrega;
    }

    public String getObCancelamento() {
        return this.obCancelamento;
    }

    public void setObCancelamento(String obCancelamento) {
        this.obCancelamento = obCancelamento;
    }

    public String getObTentativa() {
        return this.obTentativa;
    }

    public void setObTentativa(String obTentativa) {
        this.obTentativa = obTentativa;
    }
    
    public com.mercurio.lms.entrega.model.MotivoAgendamento getMotivoAgendamentoByIdMotivoCancelamento() {
        return this.motivoAgendamentoByIdMotivoCancelamento;
    }

	public void setMotivoAgendamentoByIdMotivoCancelamento(
			com.mercurio.lms.entrega.model.MotivoAgendamento motivoAgendamentoByIdMotivoCancelamento) {
        this.motivoAgendamentoByIdMotivoCancelamento = motivoAgendamentoByIdMotivoCancelamento;
    }

    public com.mercurio.lms.entrega.model.MotivoAgendamento getMotivoAgendamentoByIdMotivoReagendamento() {
        return this.motivoAgendamentoByIdMotivoReagendamento;
    }

	public void setMotivoAgendamentoByIdMotivoReagendamento(
			com.mercurio.lms.entrega.model.MotivoAgendamento motivoAgendamentoByIdMotivoReagendamento) {
        this.motivoAgendamentoByIdMotivoReagendamento = motivoAgendamentoByIdMotivoReagendamento;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioCriacao() {
        return this.usuarioByIdUsuarioCriacao;
    }

	public void setUsuarioByIdUsuarioCriacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCriacao) {
        this.usuarioByIdUsuarioCriacao = usuarioByIdUsuarioCriacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioCancelamento() {
        return this.usuarioByIdUsuarioCancelamento;
    }

	public void setUsuarioByIdUsuarioCancelamento(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCancelamento) {
        this.usuarioByIdUsuarioCancelamento = usuarioByIdUsuarioCancelamento;
    }

    public com.mercurio.lms.entrega.model.Turno getTurno() {
        return this.turno;
    }

    public void setTurno(com.mercurio.lms.entrega.model.Turno turno) {
        this.turno = turno;
    }

    public com.mercurio.lms.entrega.model.AgendamentoEntrega getReagendamento() {
        return this.reagendamento;
    }

	public void setReagendamento(
			com.mercurio.lms.entrega.model.AgendamentoEntrega reagendamento) {
        this.reagendamento = reagendamento;
    }
    
    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.AgendamentoDoctoServico.class)     
    public List getAgendamentoDoctoServicos() {
        return this.agendamentoDoctoServicos;
    }

    public void setAgendamentoDoctoServicos(List agendamentoDoctoServicos) {
        this.agendamentoDoctoServicos = agendamentoDoctoServicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAgendamentoEntrega",
				getIdAgendamentoEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgendamentoEntrega))
			return false;
        AgendamentoEntrega castOther = (AgendamentoEntrega) other;
		return new EqualsBuilder().append(this.getIdAgendamentoEntrega(),
				castOther.getIdAgendamentoEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgendamentoEntrega())
            .toHashCode();
    }

	public AgendamentoEntrega getAgendamentoEntregaOriginal() {
    	return agendamentoEntregaOriginal;
}

	public void setAgendamentoEntregaOriginal(
            AgendamentoEntrega agendamentoEntregaOriginal) {
    	this.agendamentoEntregaOriginal = agendamentoEntregaOriginal;
    }

	public DateTime getDhFechamento() {
		return dhFechamento;
}

	public void setDhFechamento(DateTime dhFechamento) {
		this.dhFechamento = dhFechamento;
	}

	public DateTime getDhEnvio() {
		return dhEnvio;
}

	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}

	public String getDsEmailTomador() {
		return dsEmailTomador;
	}

	public void setDsEmailTomador(String dsEmailTomador) {
		this.dsEmailTomador = dsEmailTomador;
	}

	public String getDsEmailDestinatario() {
		return dsEmailDestinatario;
	}

	public void setDsEmailDestinatario(String dsEmailDestinatario) {
		this.dsEmailDestinatario = dsEmailDestinatario;
	}
	
}
