package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class PedidoCompra implements Serializable {
 
	public PedidoCompra() {
	}
	
	public PedidoCompra(Long idPedidoCompra) {
		this.idPedidoCompra = idPedidoCompra;
	}
	
	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPedidoCompra;

    /** persistent field */
    private Long nrPedido;

    /** persistent field */
    private String nrPedidoInternacional;

    /** persistent field */
    private DomainValue tpOrigem;

    /** persistent field */
    private String nrFatura;

    /** persistent field */
    private String nrNotaFiscal;

    /** persistent field */
    private Boolean blCartao;

    /** persistent field */
    private DomainValue tpMotalBrasil;

    /** persistent field */
    private DomainValue tpModalExterior;

    /** persistent field */
    private DateTime dhEmissao;
    
    /** nullable persistent field */
    private DateTime dhInclusao;
    
    /** nullable persistent field */
    private BigDecimal nrPesoBruto;

    /** nullable persistent field */
    private BigDecimal nrPesoLiquido;

    /** nullable persistent field */
    private Integer nrQuantidadeVolumes;

    /** nullable persistent field */
    private BigDecimal vlExportacao;

    /** nullable persistent field */
    private YearMonthDay dtPrevisaoEntregaBrasil;

    /** nullable persistent field */
    private YearMonthDay dtPrevisaoEntregaExterior;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente remetente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente destinatario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
        
    /** persistent field */
    private List eventoDocumentoServicos;

    public Long getIdPedidoCompra() {
        return this.idPedidoCompra;
    }

    public void setIdPedidoCompra(Long idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public Long getNrPedido() {
        return this.nrPedido;
    }

    public void setNrPedido(Long nrPedido) {
        this.nrPedido = nrPedido;
    }

    public String getNrPedidoInternacional() {
        return this.nrPedidoInternacional;
    }

    public void setNrPedidoInternacional(String nrPedidoInternacional) {
        this.nrPedidoInternacional = nrPedidoInternacional;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public String getNrFatura() {
        return this.nrFatura;
    }

    public void setNrFatura(String nrFatura) {
        this.nrFatura = nrFatura;
    }

    public String getNrNotaFiscal() {
        return this.nrNotaFiscal;
    }

    public void setNrNotaFiscal(String nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public Boolean getBlCartao() {
        return this.blCartao;
    }

    public void setBlCartao(Boolean blCartao) {
        this.blCartao = blCartao;
    }

    public DomainValue getTpMotalBrasil() {
        return this.tpMotalBrasil;
    }

    public void setTpMotalBrasil(DomainValue tpMotalBrasil) {
        this.tpMotalBrasil = tpMotalBrasil;
    }

    public DomainValue getTpModalExterior() {
        return this.tpModalExterior;
    }

    public void setTpModalExterior(DomainValue tpModalExterior) {
        this.tpModalExterior = tpModalExterior;
    }

    public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public BigDecimal getNrPesoBruto() {
        return this.nrPesoBruto;
    }

    public void setNrPesoBruto(BigDecimal nrPesoBruto) {
        this.nrPesoBruto = nrPesoBruto;
    }

    public BigDecimal getNrPesoLiquido() {
        return this.nrPesoLiquido;
    }

    public void setNrPesoLiquido(BigDecimal nrPesoLiquido) {
        this.nrPesoLiquido = nrPesoLiquido;
    }

    public Integer getNrQuantidadeVolumes() {
        return this.nrQuantidadeVolumes;
    }

    public void setNrQuantidadeVolumes(Integer nrQuantidadeVolumes) {
        this.nrQuantidadeVolumes = nrQuantidadeVolumes;
    }

    public BigDecimal getVlExportacao() {
        return this.vlExportacao;
    }

    public void setVlExportacao(BigDecimal vlExportacao) {
        this.vlExportacao = vlExportacao;
    }

    public YearMonthDay getDtPrevisaoEntregaBrasil() {
        return this.dtPrevisaoEntregaBrasil;
    }

    public void setDtPrevisaoEntregaBrasil(YearMonthDay dtPrevisaoEntregaBrasil) {
        this.dtPrevisaoEntregaBrasil = dtPrevisaoEntregaBrasil;
    }

    public YearMonthDay getDtPrevisaoEntregaExterior() {
        return this.dtPrevisaoEntregaExterior;
    }

	public void setDtPrevisaoEntregaExterior(
			YearMonthDay dtPrevisaoEntregaExterior) {
        this.dtPrevisaoEntregaExterior = dtPrevisaoEntregaExterior;
    }

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}
	
    public com.mercurio.lms.vendas.model.Cliente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(
			com.mercurio.lms.vendas.model.Cliente destinatario) {
		this.destinatario = destinatario;
	}

	public com.mercurio.lms.vendas.model.Cliente getRemetente() {
		return remetente;
	}

	public void setRemetente(com.mercurio.lms.vendas.model.Cliente remetente) {
		this.remetente = remetente;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}
	
    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.EventoDocumentoServico.class)     
    public List getEventoDocumentoServicos() {
        return this.eventoDocumentoServicos;
    }

    public void setEventoDocumentoServicos(List eventoDocumentoServicos) {
        this.eventoDocumentoServicos = eventoDocumentoServicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPedidoCompra",
				getIdPedidoCompra()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PedidoCompra))
			return false;
        PedidoCompra castOther = (PedidoCompra) other;
		return new EqualsBuilder().append(this.getIdPedidoCompra(),
				castOther.getIdPedidoCompra()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPedidoCompra()).toHashCode();
    }
}
