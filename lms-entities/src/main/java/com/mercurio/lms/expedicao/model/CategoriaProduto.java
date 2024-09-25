package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="CATEGORIA_PRODUTO")
@SequenceGenerator(name="CATEGORIA_PRODUTO_SEQ", sequenceName="CATEGORIA_PRODUTO_SQ", allocationSize=1)
public class CategoriaProduto implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIA_PRODUTO_SEQ")
    @Column(name = "ID_CATEGORIA_PRODUTO", nullable = false)
	private Long idCategoriaProduto;
	
    @Column(name="DS_CATEGORIA_PRODUTO", length=100, nullable=true)
	private String dsCategoriaProduto;
	
    @Column(name = "TP_SITUACAO", length = 1)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;
    
    @Column(name="CD_CATEGORIA_PRODUTO", length=2, nullable=true)
    private String cdCategoriaProduto;

    public Long getIdCategoriaProduto() {
        return idCategoriaProduto;
    }

    public void setIdCategoriaProduto(Long idCategoriaProduto) {
        this.idCategoriaProduto = idCategoriaProduto;
    }

    public String getDsCategoriaProduto() {
        return dsCategoriaProduto;
    }

    public void setDsCategoriaProduto(String dsCategoriaProduto) {
        this.dsCategoriaProduto = dsCategoriaProduto;
    }

    public DomainValue getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getCdCategoriaProduto() {
        return cdCategoriaProduto;
    }

    public void setCdCategoriaProduto(String cdCategoriaProduto) {
        this.cdCategoriaProduto = cdCategoriaProduto;
    }
    
}
