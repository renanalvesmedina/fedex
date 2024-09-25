package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class InformacaoDocServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idInformacaoDocServico;

    /** persistent field */
    private Short nrTamanho;

    /** persistent field */
    private String dsCampo;

    /** persistent field */
    private Boolean blImprimeConhecimento;

    /** persistent field */
    private DomainValue tpCampo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Short nrDecimais;

    /** nullable persistent field */
    private Boolean blOpcional;

    /** nullable persistent field */
    private String dsFormatacao;

    /** nullable persistent field */
    private Boolean blIndicadorNotaFiscal;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.TipoRegistroComplemento tipoRegistroComplemento;

    /** persistent field */
    private List dominioAgrupamentos;

    /** persistent field */
    private List dadosComplementos;
    
    private Integer versao;

    public Long getIdInformacaoDocServico() {
        return this.idInformacaoDocServico;
    }

    public void setIdInformacaoDocServico(Long idInformacaoDocServico) {
        this.idInformacaoDocServico = idInformacaoDocServico;
    }

    public Short getNrTamanho() {
        return this.nrTamanho;
    }

    public void setNrTamanho(Short nrTamanho) {
        this.nrTamanho = nrTamanho;
    }

    public String getDsCampo() {
        return this.dsCampo;
    }

    public void setDsCampo(String dsCampo) {
        this.dsCampo = dsCampo;
    }

    public Boolean getBlImprimeConhecimento() {
        return this.blImprimeConhecimento;
    }

    public void setBlImprimeConhecimento(Boolean blImprimeConhecimento) {
        this.blImprimeConhecimento = blImprimeConhecimento;
    }

    public DomainValue getTpCampo() {
        return this.tpCampo;
    }

    public void setTpCampo(DomainValue tpCampo) {
        this.tpCampo = tpCampo;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Short getNrDecimais() {
        return this.nrDecimais;
    }

    public void setNrDecimais(Short nrDecimais) {
        this.nrDecimais = nrDecimais;
    }

    public Boolean getBlOpcional() {
        return this.blOpcional;
    }

    public void setBlOpcional(Boolean blOpcional) {
        this.blOpcional = blOpcional;
    }

    public String getDsFormatacao() {
        return this.dsFormatacao;
    }

    public void setDsFormatacao(String dsFormatacao) {
        this.dsFormatacao = dsFormatacao;
    }

    public Boolean getBlIndicadorNotaFiscal() {
        return this.blIndicadorNotaFiscal;
    }

    public void setBlIndicadorNotaFiscal(Boolean blIndicadorNotaFiscal) {
        this.blIndicadorNotaFiscal = blIndicadorNotaFiscal;
    }

    public com.mercurio.lms.expedicao.model.TipoRegistroComplemento getTipoRegistroComplemento() {
        return this.tipoRegistroComplemento;
    }

	public void setTipoRegistroComplemento(
			com.mercurio.lms.expedicao.model.TipoRegistroComplemento tipoRegistroComplemento) {
        this.tipoRegistroComplemento = tipoRegistroComplemento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DominioAgrupamento.class)     
    public List getDominioAgrupamentos() {
        return this.dominioAgrupamentos;
    }

    public void setDominioAgrupamentos(List dominioAgrupamentos) {
        this.dominioAgrupamentos = dominioAgrupamentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DadosComplemento.class)     
    public List getDadosComplementos() {
        return this.dadosComplementos;
    }

    public void setDadosComplementos(List dadosComplementos) {
        this.dadosComplementos = dadosComplementos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idInformacaoDocServico",
				getIdInformacaoDocServico()).toString();
    }
    
    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof InformacaoDocServico))
			return false;
        InformacaoDocServico castOther = (InformacaoDocServico) other;
		return new EqualsBuilder().append(this.getIdInformacaoDocServico(),
				castOther.getIdInformacaoDocServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdInformacaoDocServico())
            .toHashCode();
    }

}
