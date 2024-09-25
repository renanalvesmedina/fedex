package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="NOME_PRODUTO")
@SequenceGenerator(name="NOME_PRODUTO_SEQ", sequenceName="NOME_PRODUTO_SQ", allocationSize=1)
public class NomeProduto implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOME_PRODUTO_SEQ")
    @Column(name = "ID_NOME_PRODUTO", nullable = false)
	private Long idNomeProduto;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_PRODUTO", nullable=true)
	private Produto produto;
	
    @Column(name="DS_NOME_PRODUTO", length=360, nullable=true)
	private String dsNomeProduto;
	
    @Column(name="TP_NOME_PRODUTO", length=2, nullable =true)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_NOME_PRODUTO") })
	private DomainValue tpNomeProduto;

    public Long getIdNomeProduto() {
        return idNomeProduto;
    }

    public void setIdNomeProduto(Long idNomeProduto) {
        this.idNomeProduto = idNomeProduto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getDsNomeProduto() {
        return dsNomeProduto;
    }

    public void setDsNomeProduto(String dsNomeProduto) {
        this.dsNomeProduto = dsNomeProduto;
    }

    public DomainValue getTpNomeProduto() {
        return tpNomeProduto;
    }

    public void setTpNomeProduto(DomainValue tpNomeProduto) {
        this.tpNomeProduto = tpNomeProduto;
    }
    
}
