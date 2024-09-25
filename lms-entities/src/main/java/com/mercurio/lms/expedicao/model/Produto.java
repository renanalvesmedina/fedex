package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.municipios.model.Empresa;

/** @author LMS Custom Hibernate CodeGenerator */
public class Produto implements Serializable {
    
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idProduto;

	/** persistent field */
	private VarcharI18n dsProduto;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private NaturezaProduto naturezaProduto;

	/** persistent field */
	private TipoProduto tipoProduto;

	/** persistent field */
	private Empresa empresa;
	
	/** persistent field */
    private DomainValue categoria;
	
	/** identifier field */
	private Integer nrOnu;
	
	/** persistent field */
	private Integer nrOrdem;
	
	/** persistent field */
	private String nrNcm;
	
	/** persistent field */
	private ClasseRisco classeRisco;
	
	/** persistent field */
	private SubClasseRisco subClasseRisco;
    
    /** persistent field */
    private Boolean blProdutoProibido;
    
    /** persistent field */
    @SuppressWarnings("rawtypes")
    private List<NomeProduto> nomeProdutos;
    
    @SuppressWarnings("rawtypes")
    private List<ProdutoCategoriaProduto> produtoCategoriaProdutos;
	
	public Long getIdProduto() {
		return this.idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public VarcharI18n getDsProduto() {
		return dsProduto;
	}

	public void setDsProduto(VarcharI18n dsProduto) {
		this.dsProduto = dsProduto;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public NaturezaProduto getNaturezaProduto() {
		return this.naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public TipoProduto getTipoProduto() {
		return this.tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public DomainValue getCategoria() {
        return categoria;
    }

    public void setCategoria(DomainValue categoria) {
        this.categoria = categoria;
    }

    public Integer getNrOnu() {
		return nrOnu;
	}

	public void setNrOnu(Integer nrOnu) {
		this.nrOnu = nrOnu;
	}

	public ClasseRisco getClasseRisco() {
		return classeRisco;
	}

	public void setClasseRisco(ClasseRisco classeRisco) {
		this.classeRisco = classeRisco;
	}

    public Integer getNrOrdem() {
        return nrOrdem;
    }

    public void setNrOrdem(Integer nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public String getNrNcm() {
        return nrNcm;
    }

    public void setNrNcm(String nrNcm) {
        this.nrNcm = nrNcm;
    }

    public SubClasseRisco getSubClasseRisco() {
        return subClasseRisco;
    }

    public void setSubClasseRisco(SubClasseRisco subClasseRisco) {
        this.subClasseRisco = subClasseRisco;
    }

    public Boolean getBlProdutoProibido() {
        return blProdutoProibido;
    }

    public void setBlProdutoProibido(Boolean blProdutoProibido) {
        this.blProdutoProibido = blProdutoProibido;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.NomeProduto.class) 
    public List<NomeProduto> getNomeProdutos() {
        return nomeProdutos;
    }

    public void setNomeProdutos(List<NomeProduto> nomeProdutos) {
        this.nomeProdutos = nomeProdutos;
    }
    
    @SuppressWarnings("rawtypes")
    @ParametrizedAttribute(type = ProdutoCategoriaProduto.class)
    public List<ProdutoCategoriaProduto> getProdutoCategoriaProdutos() {
        return produtoCategoriaProdutos;
    }

    public void setProdutoCategoriaProdutos(List<ProdutoCategoriaProduto> produtoCategoriaProdutos) {
        this.produtoCategoriaProdutos = produtoCategoriaProdutos;
    }

    @Override
    public String toString() {
		return new ToStringBuilder(this).append("idProduto", getIdProduto())
			.toString();
	}

    @Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Produto))
			return false;
		Produto castOther = (Produto) other;
		return new EqualsBuilder().append(this.getIdProduto(),
				castOther.getIdProduto()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdProduto()).toHashCode();
	}
}
