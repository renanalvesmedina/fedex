package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaCredito implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNotaCredito;

    /** persistent field */
    private Long nrNotaCredito;

    /** persistent field */
    private ControleCarga controleCarga;

    /** persistent field */
    private DateTime dhGeracao;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** nullable persistent field */
    private BigDecimal vlAcrescimoSugerido;

    /** nullable persistent field */
    private BigDecimal vlAcrescimo;

    /** nullable persistent field */
    private BigDecimal vlDescontoSugerido;

    /** nullable persistent field */
    private BigDecimal vlDesconto;
    
    /** nullable persistent field */
    private BigDecimal vlDescUsoEquipamento;
    
    /** nullable persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** nullable persistent field */
    private String obNotaCredito;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro;
    
    /** persistent field */
    private MoedaPais moedaPais;
    
    /** persistent field */
    private Pendencia pendencia;

    /** persistent field */
    private List<NotaCreditoParcela> notaCreditoParcelas;
    
    /** persistent field */
    private List controleCargas;

    private List<NotaCreditoDocto> notaCreditoDoctos;

    private List<NotaCreditoColeta> notaCreditoColetas;

    private List<NotaCreditoCalcPadrao> notaCreditoCalculoPadraoItens;
    
    private List<NotaCreditoCalcPadraoDocto> notaCreditoDoctoItens;
    
	/** nullable persistent field */
    private DomainValue tpNotaCredito;

	private BigDecimal vlTotal;

	public Long getIdNotaCredito() {
        return this.idNotaCredito;
    }

    public void setIdNotaCredito(Long idNotaCredito) {
        this.idNotaCredito = idNotaCredito;
    }

    public Long getNrNotaCredito() {
        return this.nrNotaCredito;
    }

    public void setNrNotaCredito(Long nrNotaCredito) {
        this.nrNotaCredito = nrNotaCredito;
    }

    public DateTime getDhGeracao() {
        return this.dhGeracao;
    }

    public void setDhGeracao(DateTime dhGeracao) {
        this.dhGeracao = dhGeracao;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public BigDecimal getVlAcrescimoSugerido() {
        return this.vlAcrescimoSugerido;
    }

    public void setVlAcrescimoSugerido(BigDecimal vlAcrescimoSugerido) {
        this.vlAcrescimoSugerido = vlAcrescimoSugerido;
    }

    public BigDecimal getVlAcrescimo() {
        return this.vlAcrescimo;
    }

    public void setVlAcrescimo(BigDecimal vlAcrescimo) {
        this.vlAcrescimo = vlAcrescimo;
    }

    public BigDecimal getVlDescontoSugerido() {
        return this.vlDescontoSugerido;
    }

    public void setVlDescontoSugerido(BigDecimal vlDescontoSugerido) {
        this.vlDescontoSugerido = vlDescontoSugerido;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public String getObNotaCredito() {
        return this.obNotaCredito;
    }

    public void setObNotaCredito(String obNotaCredito) {
        this.obNotaCredito = obNotaCredito;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }
    
    public com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(
			com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela.class)     
    public List<NotaCreditoParcela> getNotaCreditoParcelas() {
        return this.notaCreditoParcelas;
    }

    public void setNotaCreditoParcelas(List<NotaCreditoParcela> notaCreditoParcelas) {
        this.notaCreditoParcelas = notaCreditoParcelas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaCredito",
				getIdNotaCredito()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaCredito))
			return false;
        NotaCredito castOther = (NotaCredito) other;
		return new EqualsBuilder().append(this.getIdNotaCredito(),
				castOther.getIdNotaCredito()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaCredito()).toHashCode();
    }

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	/**
	 * @return Returns the vlDescUsoEquipamento.
	 */
	public BigDecimal getVlDescUsoEquipamento() {
		return vlDescUsoEquipamento;
	}

	/**
	 * @param vlDescUsoEquipamento
	 *            The vlDescUsoEquipamento to set.
	 */
	public void setVlDescUsoEquipamento(BigDecimal vlDescUsoEquipamento) {
		this.vlDescUsoEquipamento = vlDescUsoEquipamento;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

    public List<NotaCreditoColeta> getNotaCreditoColetas() {
        return notaCreditoColetas;
    }

    public void setNotaCreditoColetas(List<NotaCreditoColeta> notaCreditoColetas) {
        this.notaCreditoColetas = notaCreditoColetas;
    }

    public List<NotaCreditoDocto> getNotaCreditoDoctos() {
        return notaCreditoDoctos;
    }

    public void setNotaCreditoDoctos(List<NotaCreditoDocto> notaCreditoDoctos) {
        this.notaCreditoDoctos = notaCreditoDoctos;
    }

	public List<NotaCreditoCalcPadrao> getNotaCreditoCalculoPadraoItens() {
		return notaCreditoCalculoPadraoItens;
	}

	public void setNotaCreditoCalculoPadraoItens(
			List<NotaCreditoCalcPadrao> notaCreditoCalculoPadraoItens) {
		this.notaCreditoCalculoPadraoItens = notaCreditoCalculoPadraoItens;
	}

	public DomainValue getTpNotaCredito() {
		return tpNotaCredito;
	}

	public void setTpNotaCredito(DomainValue tpNotaCredito) {
		this.tpNotaCredito = tpNotaCredito;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}
	
	public List<NotaCreditoCalcPadraoDocto> getNotaCreditoDoctoItens() {
			return notaCreditoDoctoItens;
	}

	public void setNotaCreditoDoctoItens(
			List<NotaCreditoCalcPadraoDocto> notaCreditoDoctoItens) {
		this.notaCreditoDoctoItens = notaCreditoDoctoItens;
	}
}