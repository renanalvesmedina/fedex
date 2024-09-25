package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
@Entity
@Table(name="PRODUTO_CATEGORIA_PRODUTO")
@SequenceGenerator(name="PRODUTO_CATEGORIA_PRODUTO_SEQ", sequenceName="PRODUTO_CATEGORIA_PRODUTO_SQ", allocationSize=1)
public class ProdutoCategoriaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUTO_CATEGORIA_PRODUTO_SEQ")
    @Column(name = "ID_PRODUTO_CATEGORIA_PRODUTO", nullable = false)
    private Long idProdutoCategoriaProduto;

    /** persistent field */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_CATEGORIA_PRODUTO", nullable=true)
    private CategoriaProduto categoriaProduto;

    /** persistent field */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_PRODUTO", nullable=true)
    private Produto produto;

    public Long getIdProdutoCategoriaProduto() {
        return idProdutoCategoriaProduto;
    }

    public void setIdProdutoCategoriaProduto(Long idProdutoCategoriaProduto) {
        this.idProdutoCategoriaProduto = idProdutoCategoriaProduto;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
