package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoControleCarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoControleCarga;

    /** persistent field */
    private DateTime dhEvento;

    /** persistent field */
    private DomainValue tpEventoControleCarga;

    /** nullable persistent field */
    private String dsEvento;
    
    /** nullable persistent field */
    private BigDecimal vlLiberacao;

    /** persistent field */
    private EquipeOperacao equipeOperacao; 
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoCalculado;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoAforadoCalculado;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoInformado;
    
    /** nullable persistent field */
    private BigDecimal psReal;
    
    /** nullable persistent field */
    private BigDecimal psAforado;
    
    /** nullable persistent field */
    private BigDecimal vlTotal;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;
    
    /** nullable persistent field */
    private DomainValue tpSituacaoPendencia;
    
    /** nullable persistent field */
    private Pendencia pendencia;
    
    /** persistent field */
    private Usuario usuarioAprovador;
    
    /** persistent field */
    private Usuario usuarioSolicitacao;
    
    /** persistent field */
    private DateTime dhEventoSolicitacao;
    
    /** persistent field */
    private DateTime dhEventoOriginal;
    
    /** nullable persistent field */
    private String dsObservacao;

    public Long getIdEventoControleCarga() {
        return this.idEventoControleCarga;
    }

    public void setIdEventoControleCarga(Long idEventoControleCarga) {
        this.idEventoControleCarga = idEventoControleCarga;
    }

    public DateTime getDhEvento() {
        return this.dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public DomainValue getTpEventoControleCarga() {
        return this.tpEventoControleCarga;
    }

    public void setTpEventoControleCarga(DomainValue tpEventoControleCarga) {
        this.tpEventoControleCarga = tpEventoControleCarga;
    }

    public String getDsEvento() {
        return this.dsEvento;
    }

    public void setDsEvento(String dsEvento) {
        this.dsEvento = dsEvento;
    }

    public BigDecimal getPcOcupacaoCalculado() {
        return this.pcOcupacaoCalculado;
    }

    public void setPcOcupacaoCalculado(BigDecimal pcOcupacaoCalculado) {
        this.pcOcupacaoCalculado = pcOcupacaoCalculado;
    }

    public BigDecimal getPcOcupacaoAforadoCalculado() {
        return this.pcOcupacaoAforadoCalculado;
    }

	public void setPcOcupacaoAforadoCalculado(
			BigDecimal pcOcupacaoAforadoCalculado) {
        this.pcOcupacaoAforadoCalculado = pcOcupacaoAforadoCalculado;
    }

    public BigDecimal getPcOcupacaoInformado() {
        return this.pcOcupacaoInformado;
    }

    public void setPcOcupacaoInformado(BigDecimal pcOcupacaoInformado) {
        this.pcOcupacaoInformado = pcOcupacaoInformado;
    }
    
    public BigDecimal getPsAforado() {
        return psAforado;
    }

    public void setPsAforado(BigDecimal psAforado) {
        this.psAforado = psAforado;
    }

    public BigDecimal getPsReal() {
        return psReal;
    }

    public void setPsReal(BigDecimal psReal) {
        this.psReal = psReal;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }
    
    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEventoControleCarga",
				getIdEventoControleCarga()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_CONTROLE_CARGA", idEventoControleCarga)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("DH_EVENTO", dhEvento)
				.append("TP_EVENTO_CONTROLE_CARGA", tpEventoControleCarga != null ? tpEventoControleCarga.getValue() : null)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("ID_EQUIPE_OPERACAO", equipeOperacao != null ? equipeOperacao.getIdEquipeOperacao() : null)
				.append("ID_MEIO_TRANSPORTE", meioTransporte != null ? meioTransporte.getIdMeioTransporte() : null)
				.append("DS_EVENTO", dsEvento)
				.append("VL_LIBERACAO", vlLiberacao)
				.append("PS_REAL", psReal)
				.append("PS_AFORADO", psAforado)
				.append("VL_TOTAL", vlTotal)
				.append("PC_OCUPACAO_CALCULADO", pcOcupacaoCalculado)
				.append("PC_OCUPACAO_AFORADO_CALCULADO", pcOcupacaoAforadoCalculado)
				.append("PC_OCUPACAO_INFORMADO", pcOcupacaoInformado)
				.append("ID_MOEDA", moeda != null ? moeda.getIdMoeda() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoControleCarga))
			return false;
        EventoControleCarga castOther = (EventoControleCarga) other;
		return new EqualsBuilder().append(this.getIdEventoControleCarga(),
				castOther.getIdEventoControleCarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoControleCarga())
            .toHashCode();
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return meioTransporte;
    }

    public void setMeioTransporte(
            com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public EquipeOperacao getEquipeOperacao() {
        return equipeOperacao;
    }

    public void setEquipeOperacao(EquipeOperacao equipeOperacao) {
        this.equipeOperacao = equipeOperacao;
    }

    public BigDecimal getVlLiberacao() {
        return vlLiberacao;
    }

    public void setVlLiberacao(BigDecimal vlLiberacao) {
        this.vlLiberacao = vlLiberacao;
    }

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

	public Usuario getUsuarioAprovador() {
		return usuarioAprovador;
	}

	public void setUsuarioAprovador(Usuario usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}

	public Usuario getUsuarioSolicitacao() {
		return usuarioSolicitacao;
	}

	public void setUsuarioSolicitacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioSolicitacao) {
		this.usuarioSolicitacao = usuarioSolicitacao;
	}

	public DateTime getDhEventoSolicitacao() {
		return dhEventoSolicitacao;
	}

	public void setDhEventoSolicitacao(DateTime dhEventoSolicitacao) {
		this.dhEventoSolicitacao = dhEventoSolicitacao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	public DateTime getDhEventoOriginal() {
		return dhEventoOriginal;
	}

	public void setDhEventoOriginal(DateTime dhEventoOriginal) {
		this.dhEventoOriginal = dhEventoOriginal;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

}
