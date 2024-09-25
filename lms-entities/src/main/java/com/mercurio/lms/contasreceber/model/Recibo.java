package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Recibo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRecibo;

    /** persistent field */
    private Long nrRecibo;

    /** persistent field */
    private BigDecimal vlTotalRecibo;

    /** persistent field */
    private BigDecimal vlTotalDocumentos;

    /** persistent field */
    private BigDecimal vlTotalJuros;
    
    /** persistent field */
    private BigDecimal vlTotalDesconto;
    
    /** FACILITADOR, NÃO É UM CAMPO PERSISTENTE */
    private BigDecimal vlTotalJuroRecebido;    
    
    /** persistent field */
    private Integer versao;

    /** persistent field */
    private BigDecimal vlDiferencaCambial;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpSituacaoRecibo;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** nullable persistent field */
    private String obRecibo;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;     

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.CotacaoMoeda cotacaoMoeda;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialEmissora;

    /** persistent field */
    private List recebimentoRecibos;

    /** persistent field */
    private List itemRedecos;

    /** persistent field */
    private List faturaRecibos;

    public Long getIdRecibo() {
        return this.idRecibo;
    }

    public void setIdRecibo(Long idRecibo) {
        this.idRecibo = idRecibo;
    }

    public Long getNrRecibo() {
        return this.nrRecibo;
    }

    public void setNrRecibo(Long nrRecibo) {
        this.nrRecibo = nrRecibo;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlTotalDesconto() {
		return vlTotalDesconto;
	}

	public void setVlTotalDesconto(BigDecimal vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}

	public BigDecimal getVlTotalDocumentos() {
		return vlTotalDocumentos;
	}

	public void setVlTotalDocumentos(BigDecimal vlTotalDocumentos) {
		this.vlTotalDocumentos = vlTotalDocumentos;
	}

	public BigDecimal getVlTotalJuros() {
		return vlTotalJuros;
	}

	public void setVlTotalJuros(BigDecimal vlTotalJuros) {
		this.vlTotalJuros = vlTotalJuros;
	}

	public BigDecimal getVlTotalRecibo() {
		return vlTotalRecibo;
	}

	public void setVlTotalRecibo(BigDecimal vlTotalRecibo) {
		this.vlTotalRecibo = vlTotalRecibo;
	}

	public BigDecimal getVlDiferencaCambial() {
        return this.vlDiferencaCambial;
    }

    public void setVlDiferencaCambial(BigDecimal vlDiferencaCambial) {
        this.vlDiferencaCambial = vlDiferencaCambial;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpSituacaoRecibo() {
        return this.tpSituacaoRecibo;
    }

    public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
        this.tpSituacaoRecibo = tpSituacaoRecibo;
    }

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public String getObRecibo() {
        return this.obRecibo;
    }

    public void setObRecibo(String obRecibo) {
        this.obRecibo = obRecibo;
    }

    public com.mercurio.lms.configuracoes.model.CotacaoMoeda getCotacaoMoeda() {
        return this.cotacaoMoeda;
    }

	public void setCotacaoMoeda(
			com.mercurio.lms.configuracoes.model.CotacaoMoeda cotacaoMoeda) {
        this.cotacaoMoeda = cotacaoMoeda;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialEmissora() {
        return this.filialByIdFilialEmissora;
    }

	public void setFilialByIdFilialEmissora(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialEmissora) {
        this.filialByIdFilialEmissora = filialByIdFilialEmissora;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.RecebimentoRecibo.class)     
    public List getRecebimentoRecibos() {
        return this.recebimentoRecibos;
    }

    public void setRecebimentoRecibos(List recebimentoRecibos) {
        this.recebimentoRecibos = recebimentoRecibos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemRedeco.class)     
    public List getItemRedecos() {
        return this.itemRedecos;
    }

    public void setItemRedecos(List itemRedecos) {
        this.itemRedecos = itemRedecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.FaturaRecibo.class)     
    public List getFaturaRecibos() {
        return this.faturaRecibos;
    }

    public void setFaturaRecibos(List faturaRecibos) {
        this.faturaRecibos = faturaRecibos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRecibo", getIdRecibo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Recibo))
			return false;
        Recibo castOther = (Recibo) other;
		return new EqualsBuilder().append(this.getIdRecibo(),
				castOther.getIdRecibo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRecibo()).toHashCode();
    }

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public BigDecimal getVlTotalJuroRecebido() {
		return vlTotalJuroRecebido;
	}

	public void setVlTotalJuroRecebido(BigDecimal vlTotalJuroRecebido) {
		this.vlTotalJuroRecebido = vlTotalJuroRecebido;
	}

}
