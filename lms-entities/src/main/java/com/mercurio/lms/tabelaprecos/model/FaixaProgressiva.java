package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class FaixaProgressiva implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFaixaProgressiva;

    /** persistent field */
    private DomainValue tpIndicadorCalculo;

    /** persistent field */
    private DomainValue cdMinimoProgressivo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private BigDecimal vlFaixaProgressiva;
    
    private Integer versao;

    /** nullable persistent field */
    private com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico produtoEspecifico;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.UnidadeMedida unidadeMedida;
    
    private List valoresFaixasProgressivas;
    
    public FaixaProgressiva() {}
    
    public FaixaProgressiva(Long id) {
    	this.idFaixaProgressiva = id;
    }

    public Long getIdFaixaProgressiva() {
        return this.idFaixaProgressiva;
    }

    public void setIdFaixaProgressiva(Long idFaixaProgressiva) {
        this.idFaixaProgressiva = idFaixaProgressiva;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public DomainValue getCdMinimoProgressivo() {
        return this.cdMinimoProgressivo;
    }

    public void setCdMinimoProgressivo(DomainValue cdMinimoProgressivo) {
        this.cdMinimoProgressivo = cdMinimoProgressivo;
    }

    public DomainValue getTpIndicadorCalculo() {
		return tpIndicadorCalculo;
	}

	public void setTpIndicadorCalculo(DomainValue tpIndicadorCalculo) {
		this.tpIndicadorCalculo = tpIndicadorCalculo;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public BigDecimal getVlFaixaProgressiva() {
        return this.vlFaixaProgressiva;
    }

    public void setVlFaixaProgressiva(BigDecimal vlFaixaProgressiva) {
        this.vlFaixaProgressiva = vlFaixaProgressiva;
    }

    public com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela getTabelaPrecoParcela() {
        return this.tabelaPrecoParcela;
    }

	public void setTabelaPrecoParcela(
			com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela) {
        this.tabelaPrecoParcela = tabelaPrecoParcela;
    }

    public com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico getProdutoEspecifico() {
        return this.produtoEspecifico;
    }

	public void setProdutoEspecifico(
			com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico produtoEspecifico) {
        this.produtoEspecifico = produtoEspecifico;
    }

    public com.mercurio.lms.tabelaprecos.model.UnidadeMedida getUnidadeMedida() {
        return this.unidadeMedida;
    }

	public void setUnidadeMedida(
			com.mercurio.lms.tabelaprecos.model.UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva.class)     
    public List getValoresFaixasProgressivas() {
		return valoresFaixasProgressivas;
	}

	public void setValoresFaixasProgressivas(List valoresFaixasProgressivas) {
		this.valoresFaixasProgressivas = valoresFaixasProgressivas;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idFaixaProgressiva",
				getIdFaixaProgressiva()).toString();
    }

    @Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaixaProgressiva))
			return false;
        FaixaProgressiva castOther = (FaixaProgressiva) other;
		return new EqualsBuilder().append(this.getIdFaixaProgressiva(),
				castOther.getIdFaixaProgressiva()).isEquals();
    }

    @Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdFaixaProgressiva())
            .toHashCode();
    }

	public Long getIdParcelaPreco() {
		if (tabelaPrecoParcela == null) {
			return null;
		}
		return tabelaPrecoParcela.getIdParcelaPreco();
	}

	public String getNmParcelaPreco() {
		if (tabelaPrecoParcela != null && tabelaPrecoParcela.getParcelaPreco() != null && tabelaPrecoParcela.getParcelaPreco().getNmParcelaPreco() != null)  {
			return tabelaPrecoParcela.getParcelaPreco().getNmParcelaPreco().getValue();
		}
		return null;
	}
	
}
