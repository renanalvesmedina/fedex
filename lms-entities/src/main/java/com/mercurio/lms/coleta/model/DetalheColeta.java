package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.municipios.model.Municipio;

/** @author LMS Custom Hibernate CodeGenerator */
public class DetalheColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDetalheColeta;

    /** persistent field */
    private DomainValue tpFrete;

    /** persistent field */
    private Integer qtVolumes;

    /** persistent field */
    private BigDecimal vlMercadoria;

    /** persistent field */
    private BigDecimal psMercadoria;

    /** nullable persistent field */
    private BigDecimal psAforado;

    /** nullable persistent field */
    private String obDetalheColeta;

    /** nullable persistent field */
    private String nmDestinatario;    
    
    /** nullable persistent field */
    private Boolean blEntregaDireta;
    
    /** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
    private String origem;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta;

    /** persistent field */
    private com.mercurio.lms.coleta.model.LocalidadeEspecial localidadeEspecial;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cotacao cotacao;
    
    /** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
    private com.mercurio.lms.coleta.model.EventoColeta eventoColeta;
    
    /** persistent field */
    private List awbColetas;

    /** persistent field */
    private List notaFiscalColetas;
    
    /** persistent field */
    private List eventoColetas;
    
    private Integer versao;
    
    private Awb awb;

    public DetalheColeta() {
    }

    public DetalheColeta(Long idDetalheColeta, DomainValue tpFrete, Integer qtVolumes, BigDecimal vlMercadoria, BigDecimal psMercadoria, BigDecimal psAforado, String obDetalheColeta, String nmDestinatario, Boolean blEntregaDireta, String origem, Cliente cliente, Municipio municipio, NaturezaProduto naturezaProduto, Moeda moeda, Servico servico, PedidoColeta pedidoColeta, LocalidadeEspecial localidadeEspecial, Filial filial, CtoInternacional ctoInternacional, DoctoServico doctoServico, Cotacao cotacao, EventoColeta eventoColeta, List awbColetas, List notaFiscalColetas, List eventoColetas, Integer versao, Awb awb) {
        this.idDetalheColeta = idDetalheColeta;
        this.tpFrete = tpFrete;
        this.qtVolumes = qtVolumes;
        this.vlMercadoria = vlMercadoria;
        this.psMercadoria = psMercadoria;
        this.psAforado = psAforado;
        this.obDetalheColeta = obDetalheColeta;
        this.nmDestinatario = nmDestinatario;
        this.blEntregaDireta = blEntregaDireta;
        this.origem = origem;
        this.cliente = cliente;
        this.municipio = municipio;
        this.naturezaProduto = naturezaProduto;
        this.moeda = moeda;
        this.servico = servico;
        this.pedidoColeta = pedidoColeta;
        this.localidadeEspecial = localidadeEspecial;
        this.filial = filial;
        this.ctoInternacional = ctoInternacional;
        this.doctoServico = doctoServico;
        this.cotacao = cotacao;
        this.eventoColeta = eventoColeta;
        this.awbColetas = awbColetas;
        this.notaFiscalColetas = notaFiscalColetas;
        this.eventoColetas = eventoColetas;
        this.versao = versao;
        this.awb = awb;
    }

    public Long getIdDetalheColeta() {
        return this.idDetalheColeta;
    }

    public void setIdDetalheColeta(Long idDetalheColeta) {
        this.idDetalheColeta = idDetalheColeta;
    }

    public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
    }

    public Integer getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Integer qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getVlMercadoria() {
        return this.vlMercadoria;
    }

    public void setVlMercadoria(BigDecimal vlMercadoria) {
        this.vlMercadoria = vlMercadoria;
    }

    public BigDecimal getPsMercadoria() {
        return this.psMercadoria;
    }

    public void setPsMercadoria(BigDecimal psMercadoria) {
        this.psMercadoria = psMercadoria;
    }

    public BigDecimal getPsAforado() {
        return this.psAforado;
    }

    public void setPsAforado(BigDecimal psAforado) {
        this.psAforado = psAforado;
    }

    public String getObDetalheColeta() {
        return this.obDetalheColeta;
    }

    public void setObDetalheColeta(String obDetalheColeta) {
        this.obDetalheColeta = obDetalheColeta;
    }
    
    public String getNmDestinatario() {
		return nmDestinatario;
	}

    public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	/** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
	public String getOrigem() {
		return origem;
	}

    /** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.coleta.model.PedidoColeta getPedidoColeta() {
        return this.pedidoColeta;
    }

	public void setPedidoColeta(
			com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta) {
        this.pedidoColeta = pedidoColeta;
    }

    public com.mercurio.lms.coleta.model.LocalidadeEspecial getLocalidadeEspecial() {
        return this.localidadeEspecial;
    }

	public void setLocalidadeEspecial(
			com.mercurio.lms.coleta.model.LocalidadeEspecial localidadeEspecial) {
        this.localidadeEspecial = localidadeEspecial;
    }

    public com.mercurio.lms.vendas.model.Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(com.mercurio.lms.vendas.model.Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
		return ctoInternacional;
	}

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
		this.ctoInternacional = ctoInternacional;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}
	
    /** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
	public com.mercurio.lms.coleta.model.EventoColeta getEventoColeta() {
		return eventoColeta;
	}

    /** 
	 * NOT persistent field Campo não persistido no banco, apenas utilizado no
	 * desenvolvimento
     */
	public void setEventoColeta(
			com.mercurio.lms.coleta.model.EventoColeta eventoColeta) {
		this.eventoColeta = eventoColeta;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.AwbColeta.class)     
    public List getAwbColetas() {
        return this.awbColetas;
    }

    public void setAwbColetas(List awbColetas) {
        this.awbColetas = awbColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.NotaFiscalColeta.class)     
    public List getNotaFiscalColetas() {
        return this.notaFiscalColetas;
    }

    public void setNotaFiscalColetas(List notaFiscalColetas) {
        this.notaFiscalColetas = notaFiscalColetas;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.EventoColeta.class)     
    public List getEventoColetas() {
        return this.eventoColetas;
    }

    public void setEventoColetas(List eventoColetas) {
        this.eventoColetas = eventoColetas;
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}    
	
	public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}
    
    public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}
	
	public Boolean getBlEntregaDireta() {
		return blEntregaDireta;
	}

	public void setBlEntregaDireta(Boolean blEntregaDireta) {
		this.blEntregaDireta = blEntregaDireta;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDetalheColeta",
				getIdDetalheColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DetalheColeta))
			return false;
        DetalheColeta castOther = (DetalheColeta) other;
		return new EqualsBuilder().append(this.getIdDetalheColeta(),
				castOther.getIdDetalheColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDetalheColeta()).toHashCode();
    }

}
