package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;


/** @author LMS Custom Hibernate CodeGenerator */
public class DoctoServicoIndenizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDoctoServicoIndenizacao;

    /** persistent field */
    private BigDecimal vlIndenizado;
    
    /** persistent field */
    private java.lang.Integer qtVolumes;    

    /** persistent field */
    private DomainValue tpPrejuizo;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Produto produto;
    
    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao;
    
    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialSinistro;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial rotaSinistro;
    
    private Integer versao;
    
    /** persistent field */
    private List reciboIndenizacaoNfs;

    /** persistent field */
	private List filialDebitadas;

    public DoctoServicoIndenizacao() {
    }

    public DoctoServicoIndenizacao(Long idDoctoServicoIndenizacao, BigDecimal vlIndenizado, Integer qtVolumes, DomainValue tpPrejuizo, Moeda moeda, DoctoServico doctoServico, Produto produto, ReciboIndenizacao reciboIndenizacao, OcorrenciaNaoConformidade ocorrenciaNaoConformidade, Filial filialSinistro, Filial rotaSinistro, Integer versao, List reciboIndenizacaoNfs, List filialDebitadas) {
        this.idDoctoServicoIndenizacao = idDoctoServicoIndenizacao;
        this.vlIndenizado = vlIndenizado;
        this.qtVolumes = qtVolumes;
        this.tpPrejuizo = tpPrejuizo;
        this.moeda = moeda;
        this.doctoServico = doctoServico;
        this.produto = produto;
        this.reciboIndenizacao = reciboIndenizacao;
        this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
        this.filialSinistro = filialSinistro;
        this.rotaSinistro = rotaSinistro;
        this.versao = versao;
        this.reciboIndenizacaoNfs = reciboIndenizacaoNfs;
        this.filialDebitadas = filialDebitadas;
    }

    public Long getIdDoctoServicoIndenizacao() {
        return this.idDoctoServicoIndenizacao;
    }

    public void setIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
        this.idDoctoServicoIndenizacao = idDoctoServicoIndenizacao;
    }

    public BigDecimal getVlIndenizado() {
        return this.vlIndenizado;
    }

    public void setVlIndenizado(BigDecimal vlIndenizado) {
        this.vlIndenizado = vlIndenizado;
    }

    public java.lang.Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(java.lang.Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public DomainValue getTpPrejuizo() {
		return tpPrejuizo;
	}

	public void setTpPrejuizo(DomainValue tpPrejuizo) {
		this.tpPrejuizo = tpPrejuizo;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.Produto getProduto() {
		return produto;
	}

	public void setProduto(com.mercurio.lms.expedicao.model.Produto produto) {
		this.produto = produto;
	}

	public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

    public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
		return ocorrenciaNaoConformidade;
	}

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
		this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
	}

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.indenizacoes.model.ReciboIndenizacao getReciboIndenizacao() {
        return this.reciboIndenizacao;
    }

	public void setReciboIndenizacao(
			com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao) {
        this.reciboIndenizacao = reciboIndenizacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf.class)     
    public List getReciboIndenizacaoNfs() {
        return this.reciboIndenizacaoNfs;
    }

    public void setReciboIndenizacaoNfs(List reciboIndenizacaoNfs) {
        this.reciboIndenizacaoNfs = reciboIndenizacaoNfs;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.FilialDebitada.class)     
    public List getFilialDebitadas() {
        return this.filialDebitadas;
    }

    public void setFilialDebitadas(List filialDebitadas) {
        this.filialDebitadas = filialDebitadas;
    }    

    public String toString() {
		return new ToStringBuilder(this).append("idDoctoServicoIndenizacao",
				getIdDoctoServicoIndenizacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DoctoServicoIndenizacao))
			return false;
        DoctoServicoIndenizacao castOther = (DoctoServicoIndenizacao) other;
		return new EqualsBuilder().append(this.getIdDoctoServicoIndenizacao(),
				castOther.getIdDoctoServicoIndenizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDoctoServicoIndenizacao())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialSinistro() {
		return filialSinistro;
	}

	public void setFilialSinistro(
			com.mercurio.lms.municipios.model.Filial filialSinistro) {
		this.filialSinistro = filialSinistro;
	}

	public com.mercurio.lms.municipios.model.Filial getRotaSinistro() {
		return rotaSinistro;
	}

	public void setRotaSinistro(
			com.mercurio.lms.municipios.model.Filial rotaSinistro) {
		this.rotaSinistro = rotaSinistro;
	}

}
