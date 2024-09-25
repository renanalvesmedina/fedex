package com.mercurio.lms.contasreceber.model;

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
public class Redeco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRedeco;
    
    private Integer versao;    

    /** persistent field */
    private Long nrRedeco;

    /** persistent field */
    private BigDecimal vlDiferencaCambialCotacao;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpSituacaoRedeco;

    /** persistent field */
    private DomainValue tpFinalidade;
    
    /** persistent field */
    private DomainValue tpAbrangencia;    

    /** persistent field */
    private String nmResponsavelCobranca;

    /** persistent field */
    private DomainValue tpRecebimento;

    /** persistent field */
    private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private DomainValue blDigitacaoConcluida;

    /** nullable persistent field */
    private YearMonthDay dtLiquidacao;

    /** nullable persistent field */
    private YearMonthDay dtRecebimento;

    /** nullable persistent field */
    private DateTime dhTransmissao;

    /** nullable persistent field */
    private String obRedeco;

    /** non-persistent field */
    private BigDecimal vlTotalRecebido;
    
    /** non-persistent field */
    private BigDecimal vlTotalDescontos;    

    /** non-persistent field */
    private BigDecimal vlTotalJuroRecebido;
    
    private BigDecimal vlRecebido;    
    
    /** non-persistent field */
    private String tpFinalidadeAnterior;    
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EmpresaCobranca empresaCobranca;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendenciaDesconto;      

    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendenciaLucrosPerdas;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendenciaRecebimento;
    
    private com.mercurio.lms.configuracoes.model.Moeda moeda;
    
    /** persistent field */
    private List repositorioItemRedecos;

    /** persistent field */
    private List relacaoCobrancas;

    /** persistent field */
    private List itemRedecos;

    /** persistent field */
    private List reciboDescontos;

    /** persistent field */
    private List loteCheques;

    public Long getIdRedeco() {
        return this.idRedeco;
    }

    public void setIdRedeco(Long idRedeco) {
        this.idRedeco = idRedeco;
    }

    public Long getNrRedeco() {
        return this.nrRedeco;
    }

    public void setNrRedeco(Long nrRedeco) {
        this.nrRedeco = nrRedeco;
    }

    public BigDecimal getVlDiferencaCambialCotacao() {
        return this.vlDiferencaCambialCotacao;
    }

	public void setVlDiferencaCambialCotacao(
			BigDecimal vlDiferencaCambialCotacao) {
        this.vlDiferencaCambialCotacao = vlDiferencaCambialCotacao;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpSituacaoRedeco() {
        return this.tpSituacaoRedeco;
    }

    public void setTpSituacaoRedeco(DomainValue tpSituacaoRedeco) {
        this.tpSituacaoRedeco = tpSituacaoRedeco;
    }

    public DomainValue getTpFinalidade() {
        return this.tpFinalidade;
    }

    public void setTpFinalidade(DomainValue tpFinalidade) {
        this.tpFinalidade = tpFinalidade;
    }

    public String getNmResponsavelCobranca() {
        return this.nmResponsavelCobranca;
    }

    public void setNmResponsavelCobranca(String nmResponsavelCobranca) {
        this.nmResponsavelCobranca = nmResponsavelCobranca;
    }

    public DomainValue getTpRecebimento() {
        return this.tpRecebimento;
    }

    public void setTpRecebimento(DomainValue tpRecebimento) {
        this.tpRecebimento = tpRecebimento;
    }

    public DomainValue getTpSituacaoWorkflow() {
        return this.tpSituacaoWorkflow;
    }

    public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
        this.tpSituacaoWorkflow = tpSituacaoWorkflow;
    }

    public YearMonthDay getDtLiquidacao() {
        return this.dtLiquidacao;
    }

    public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
        this.dtLiquidacao = dtLiquidacao;
    }

    public YearMonthDay getDtRecebimento() {
        return this.dtRecebimento;
    }

    public void setDtRecebimento(YearMonthDay dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public DateTime getDhTransmissao() {
        return this.dhTransmissao;
    }

    public void setDhTransmissao(DateTime dhTransmissao) {
        this.dhTransmissao = dhTransmissao;
    }

    public String getObRedeco() {
        return this.obRedeco;
    }

    public void setObRedeco(String obRedeco) {
        this.obRedeco = obRedeco;
    }

    public com.mercurio.lms.configuracoes.model.EmpresaCobranca getEmpresaCobranca() {
        return this.empresaCobranca;
    }

	public void setEmpresaCobranca(
			com.mercurio.lms.configuracoes.model.EmpresaCobranca empresaCobranca) {
        this.empresaCobranca = empresaCobranca;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.RepositorioItemRedeco.class)     
    public List getRepositorioItemRedecos() {
        return this.repositorioItemRedecos;
    }

    public void setRepositorioItemRedecos(List repositorioItemRedecos) {
        this.repositorioItemRedecos = repositorioItemRedecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.RelacaoCobranca.class)     
    public List getRelacaoCobrancas() {
        return this.relacaoCobrancas;
    }

    public void setRelacaoCobrancas(List relacaoCobrancas) {
        this.relacaoCobrancas = relacaoCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemRedeco.class)     
    public List getItemRedecos() {
        return this.itemRedecos;
    }

    public void setItemRedecos(List itemRedecos) {
        this.itemRedecos = itemRedecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ReciboDesconto.class)     
    public List getReciboDescontos() {
        return this.reciboDescontos;
    }

    public void setReciboDescontos(List reciboDescontos) {
        this.reciboDescontos = reciboDescontos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.LoteCheque.class)     
    public List getLoteCheques() {
        return this.loteCheques;
    }

    public void setLoteCheques(List loteCheques) {
        this.loteCheques = loteCheques;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRedeco", getIdRedeco())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Redeco))
			return false;
        Redeco castOther = (Redeco) other;
		return new EqualsBuilder().append(this.getIdRedeco(),
				castOther.getIdRedeco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRedeco()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendenciaDesconto() {
		return pendenciaDesconto;
	}

	public void setPendenciaDesconto(
			com.mercurio.lms.workflow.model.Pendencia pendenciaDesconto) {
		this.pendenciaDesconto = pendenciaDesconto;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendenciaLucrosPerdas() {
		return pendenciaLucrosPerdas;
	}

	public void setPendenciaLucrosPerdas(
			com.mercurio.lms.workflow.model.Pendencia pendenciaLucrosPerdas) {
		this.pendenciaLucrosPerdas = pendenciaLucrosPerdas;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendenciaRecebimento() {
		return pendenciaRecebimento;
	}

	public void setPendenciaRecebimento(
			com.mercurio.lms.workflow.model.Pendencia pendenciaRecebimento) {
		this.pendenciaRecebimento = pendenciaRecebimento;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public BigDecimal getVlTotalDescontos() {
		return vlTotalDescontos;
	}

	public void setVlTotalDescontos(BigDecimal vlTotalDescontos) {
		this.vlTotalDescontos = vlTotalDescontos;
	}

	public BigDecimal getVlTotalJuroRecebido() {
		return vlTotalJuroRecebido;
	}

	public void setVlTotalJuroRecebido(BigDecimal vlTotalJuroRecebido) {
		this.vlTotalJuroRecebido = vlTotalJuroRecebido;
	}

	public BigDecimal getVlTotalRecebido() {
		return vlTotalRecebido;
	}

	public void setVlTotalRecebido(BigDecimal vlTotalRecebido) {
		this.vlTotalRecebido = vlTotalRecebido;
	}

	public String getTpFinalidadeAnterior() {
		return tpFinalidadeAnterior;
	}

	public void setTpFinalidadeAnterior(String tpFinalidadeAnterior) {
		this.tpFinalidadeAnterior = tpFinalidadeAnterior;
	}

	public DomainValue getBlDigitacaoConcluida() {
		return blDigitacaoConcluida;
}

	public void setBlDigitacaoConcluida(DomainValue blDigitacaoConcluida) {
		this.blDigitacaoConcluida = blDigitacaoConcluida;
	}

	public BigDecimal getVlRecebido() {
		return vlRecebido;
	}

	public void setVlRecebido(BigDecimal vlRecebido) {
		this.vlRecebido = vlRecebido;
	}

}
