package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemMda implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemMda;

    /** persistent field */
    private Integer qtVolumes;

    /** persistent field */
    private BigDecimal vlMercadoria;

    /** persistent field */
    private BigDecimal psItem;

    /** persistent field */
    private String dsMercadoria;

    /** persistent field */
    private String obItemMda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.Mda mda;

    /** persistent field */
    private com.mercurio.lms.rnc.model.NaoConformidade naoConformidade;

    /** persistent field */
    private List nfItemMdas;

    public Long getIdItemMda() {
        return this.idItemMda;
    }

    public void setIdItemMda(Long idItemMda) {
        this.idItemMda = idItemMda;
    }

    public Integer getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Integer qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getVlMercadoria() {
        return this.vlMercadoria;
    }

    public void setVlMercadoria(BigDecimal vlMercadoria) {
        this.vlMercadoria = vlMercadoria;
    }

    public BigDecimal getPsItem() {
        return this.psItem;
    }

    public void setPsItem(BigDecimal psItem) {
        this.psItem = psItem;
    }

    public String getDsMercadoria() {
        return this.dsMercadoria;
    }

    public void setDsMercadoria(String dsMercadoria) {
        this.dsMercadoria = dsMercadoria;
    }

    public String getObItemMda() {
        return this.obItemMda;
    }

    public void setObItemMda(String obItemMda) {
        this.obItemMda = obItemMda;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.pendencia.model.Mda getMda() {
        return this.mda;
    }

    public void setMda(com.mercurio.lms.pendencia.model.Mda mda) {
        this.mda = mda;
    }

    public com.mercurio.lms.rnc.model.NaoConformidade getNaoConformidade() {
		return naoConformidade;
	}

	public void setNaoConformidade(
			com.mercurio.lms.rnc.model.NaoConformidade naoConformidade) {
		this.naoConformidade = naoConformidade;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.NfItemMda.class)     
    public List getNfItemMdas() {
        return this.nfItemMdas;
    }

    public void setNfItemMdas(List nfItemMdas) {
        this.nfItemMdas = nfItemMdas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemMda", getIdItemMda())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemMda))
			return false;
        ItemMda castOther = (ItemMda) other;
		return new EqualsBuilder().append(this.getIdItemMda(),
				castOther.getIdItemMda()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemMda()).toHashCode();
    }

}
