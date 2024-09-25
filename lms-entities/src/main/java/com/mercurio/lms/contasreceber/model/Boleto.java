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
public class Boleto implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum SITUACAOBOLETO{
		DB("2"), DI("1"), EM("2"), BA("3"), LI("4"), BN("5"), BP("6"), RE("7"), CA(
				"*");
		String tpSituacaoBloquete;

		SITUACAOBOLETO(String tpSituacaoBloquete){
			this.tpSituacaoBloquete = tpSituacaoBloquete;
		}
		
		public DomainValue getTpSituacaoBloquete(){
			return new DomainValue(tpSituacaoBloquete);
		}
		
		public DomainValue getTpSituacaoBoleto(){
			return new DomainValue(this.name());
		}
		
		public static DomainValue getSituacaoBloquete(DomainValue situacaoBoleto){
			SITUACAOBOLETO sb = SITUACAOBOLETO.valueOf(situacaoBoleto
					.getValue().toUpperCase());
			return sb.getTpSituacaoBloquete();
		}
	}

    /** identifier field */
    private Long idBoleto;

    /** persistent field */
    private Long nrSequenciaFilial;

    /** persistent field */
    private BigDecimal vlTotal;

    /** persistent field */
    private BigDecimal vlDesconto;

    /** persistent field */
    private BigDecimal vlJurosDia;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private YearMonthDay dtVencimento;
    
    /** persistent field */
    private YearMonthDay dtVencimentoNovo;

    /** persistent field */
    private String nrBoleto;

    /** persistent field */
    private DomainValue tpSituacaoBoleto;
    
    /** persistent field */
    private DomainValue tpSituacaoAntBoleto;    

    /** persistent field */
    private Boolean blBoletoConhecimento;

    /** persistent field */
    private Boolean blBoletoReemitido;

    /** nullable persistent field */
    private YearMonthDay dtLimitePagto;

    /** nullable persistent field */
    private DateTime dhReemissao;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cedente cedente;

    /** persistent field */
    private List ocorrenciaRetornoBancos;

    /** persistent field */
    private List historicoBoletos;

    /** persistent field */
    private List tarifaBoletos;

    private Integer versao;
    
    private List anexos;

    /** transient field */
    private boolean brokerIntegration = false;

    public boolean isBrokerIntegration() {
        return brokerIntegration;
    }

    public void setBrokerIntegration(boolean brokerIntegration) {
        this.brokerIntegration = brokerIntegration;
    }

    public Long getIdBoleto() {
        return this.idBoleto;
    }

    public void setIdBoleto(Long idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Long getNrSequenciaFilial() {
        return this.nrSequenciaFilial;
    }

    public void setNrSequenciaFilial(Long nrSequenciaFilial) {
        this.nrSequenciaFilial = nrSequenciaFilial;
    }

    public BigDecimal getVlTotal() {
        return this.vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public BigDecimal getVlJurosDia() {
        return this.vlJurosDia;
    }

    public void setVlJurosDia(BigDecimal vlJurosDia) {
        this.vlJurosDia = vlJurosDia;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getNrBoleto() {
        return this.nrBoleto;
    }

    public void setNrBoleto(String nrBoleto) {
        this.nrBoleto = nrBoleto;
    }

    public DomainValue getTpSituacaoBoleto() {
        return this.tpSituacaoBoleto;
    }

    public void setTpSituacaoBoleto(DomainValue tpSituacaoBoleto) {
        this.tpSituacaoBoleto = tpSituacaoBoleto;
    }

    public Boolean getBlBoletoConhecimento() {
        return this.blBoletoConhecimento;
    }

    public void setBlBoletoConhecimento(Boolean blBoletoConhecimento) {
        this.blBoletoConhecimento = blBoletoConhecimento;
    }

    public Boolean getBlBoletoReemitido() {
        return this.blBoletoReemitido;
    }

    public void setBlBoletoReemitido(Boolean blBoletoReemitido) {
        this.blBoletoReemitido = blBoletoReemitido;
    }

    public YearMonthDay getDtLimitePagto() {
        return this.dtLimitePagto;
    }

    public void setDtLimitePagto(YearMonthDay dtLimitePagto) {
        this.dtLimitePagto = dtLimitePagto;
    }

    public DateTime getDhReemissao() {
        return this.dhReemissao;
    }

    public void setDhReemissao(DateTime dhReemissao) {
        this.dhReemissao = dhReemissao;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contasreceber.model.Cedente getCedente() {
        return this.cedente;
    }

    public void setCedente(com.mercurio.lms.contasreceber.model.Cedente cedente) {
        this.cedente = cedente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.OcorrenciaRetornoBanco.class)     
    public List getOcorrenciaRetornoBancos() {
        return this.ocorrenciaRetornoBancos;
    }

    public void setOcorrenciaRetornoBancos(List ocorrenciaRetornoBancos) {
        this.ocorrenciaRetornoBancos = ocorrenciaRetornoBancos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoBoleto.class)     
    public List getHistoricoBoletos() {
        return this.historicoBoletos;
    }

    public void setHistoricoBoletos(List historicoBoletos) {
        this.historicoBoletos = historicoBoletos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.TarifaBoleto.class)     
    public List getTarifaBoletos() {
        return this.tarifaBoletos;
    }

    public void setTarifaBoletos(List tarifaBoletos) {
        this.tarifaBoletos = tarifaBoletos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBoleto", getIdBoleto())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Boleto))
			return false;
        Boleto castOther = (Boleto) other;
		return new EqualsBuilder().append(this.getIdBoleto(),
				castOther.getIdBoleto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBoleto()).toHashCode();
    }

	public YearMonthDay getDtVencimentoNovo() {
		return dtVencimentoNovo;
	}

	public void setDtVencimentoNovo(YearMonthDay dtVencimentoNovo) {
		this.dtVencimentoNovo = dtVencimentoNovo;
	}

	public DomainValue getTpSituacaoAntBoleto() {
		return tpSituacaoAntBoleto;
	}

	public void setTpSituacaoAntBoleto(DomainValue tpSituacaoAntBoleto) {
		this.tpSituacaoAntBoleto = tpSituacaoAntBoleto;
	}

	public Integer getVersao() {
		return versao;
}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public void setAnexos(List anexos) {
		this.anexos = anexos;
	}

	public List getAnexos() {
		return anexos;
	}
}
