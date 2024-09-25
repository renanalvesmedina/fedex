package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Cedente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCedente;

    /** persistent field */
    private Long cdCedente;

    /** persistent field */
    private Long sqCobranca;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;
    
    /** persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private String nrContaCorrente;

    /** nullable persistent field */
    private Short nrCarteira;

    /** nullable persistent field */
    private YearMonthDay dtUltimaRemessaCobranca;

    /** nullable persistent field */
    private YearMonthDay dtUltimoRetornoCobranca;

    /** nullable persistent field */
    private String dsNomeArquivoCobranca;

    /** nullable persistent field */
    private String dsCedente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria;

    /** persistent field */
    private List filiaisByIdCedente;

    /** persistent field */
    private List filiaisByIdCedenteBloqueto;

    /** persistent field */
    private List complementoEmpresaCedentes;

    /** persistent field */
    private List clientes;

    /** persistent field */
    private List faturas;

    /** persistent field */
    private List boletos;

    /** persistent field */
    private List cedenteModalAbrangencias;

    public Long getIdCedente() {
        return this.idCedente;
    }

    public void setIdCedente(Long idCedente) {
        this.idCedente = idCedente;
    }

    public Long getCdCedente() {
        return this.cdCedente;
    }

    public void setCdCedente(Long cdCedente) {
        this.cdCedente = cdCedente;
    }

    public Long getSqCobranca() {
        return this.sqCobranca;
    }

    public void setSqCobranca(Long sqCobranca) {
        this.sqCobranca = sqCobranca;
    }
  
    public String getNrContaCorrente() {
        return this.nrContaCorrente;
    }

    public void setNrContaCorrente(String nrContaCorrente) {
        this.nrContaCorrente = nrContaCorrente;
    }

    public Short getNrCarteira() {
        return this.nrCarteira;
    }

    public void setNrCarteira(Short nrCarteira) {
        this.nrCarteira = nrCarteira;
    }

    public YearMonthDay getDtUltimaRemessaCobranca() {
        return this.dtUltimaRemessaCobranca;
    }

    public void setDtUltimaRemessaCobranca(YearMonthDay dtUltimaRemessaCobranca) {
        this.dtUltimaRemessaCobranca = dtUltimaRemessaCobranca;
    }

    public YearMonthDay getDtUltimoRetornoCobranca() {
        return this.dtUltimoRetornoCobranca;
    }

    public void setDtUltimoRetornoCobranca(YearMonthDay dtUltimoRetornoCobranca) {
        this.dtUltimoRetornoCobranca = dtUltimoRetornoCobranca;
    }

    public String getDsNomeArquivoCobranca() {
        return this.dsNomeArquivoCobranca;
    }

    public void setDsNomeArquivoCobranca(String dsNomeArquivoCobranca) {
        this.dsNomeArquivoCobranca = dsNomeArquivoCobranca;
    }

    public String getDsCedente() {
        return this.dsCedente;
    }

    public void setDsCedente(String dsCedente) {
        this.dsCedente = dsCedente;
    }

    public com.mercurio.lms.configuracoes.model.AgenciaBancaria getAgenciaBancaria() {
        return this.agenciaBancaria;
    }

	public void setAgenciaBancaria(
			com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria) {
        this.agenciaBancaria = agenciaBancaria;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Filial.class)     
    public List getFiliaisByIdCedente() {
        return this.filiaisByIdCedente;
    }

    public void setFiliaisByIdCedente(List filiaisByIdCedente) {
        this.filiaisByIdCedente = filiaisByIdCedente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Filial.class)     
    public List getFiliaisByIdCedenteBloqueto() {
        return this.filiaisByIdCedenteBloqueto;
    }

    public void setFiliaisByIdCedenteBloqueto(List filiaisByIdCedenteBloqueto) {
        this.filiaisByIdCedenteBloqueto = filiaisByIdCedenteBloqueto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ComplementoEmpresaCedente.class)     
    public List getComplementoEmpresaCedentes() {
        return this.complementoEmpresaCedentes;
    }

    public void setComplementoEmpresaCedentes(List complementoEmpresaCedentes) {
        this.complementoEmpresaCedentes = complementoEmpresaCedentes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cliente.class)     
    public List getClientes() {
        return this.clientes;
    }

    public void setClientes(List clientes) {
        this.clientes = clientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    @ParametrizedAttribute(type = Boleto.class)     
    public List getBoletos() {
        return this.boletos;
    }

    public void setBoletos(List boletos) {
        this.boletos = boletos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.CedenteModalAbrangencia.class)     
    public List getCedenteModalAbrangencias() {
        return this.cedenteModalAbrangencias;
    }

    public void setCedenteModalAbrangencias(List cedenteModalAbrangencias) {
        this.cedenteModalAbrangencias = cedenteModalAbrangencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCedente", getIdCedente())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Cedente))
			return false;
        Cedente castOther = (Cedente) other;
		return new EqualsBuilder().append(this.getIdCedente(),
				castOther.getIdCedente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCedente()).toHashCode();
    }

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
}
