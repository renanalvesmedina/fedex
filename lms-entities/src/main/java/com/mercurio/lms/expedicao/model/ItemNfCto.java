package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemNfCto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemNfCto;

    /** persistent field */
    private Long cdItemNfCto;

    /** persistent field */
    private BigDecimal vlUnitario;

    /** persistent field */
    private Integer qtVolumes;

    /** persistent field */
    private String dsItemNfCto;

    /** persistent field */
    private String dsEmbalagem;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdItemNfCto() {
        return this.idItemNfCto;
    }

    public void setIdItemNfCto(Long idItemNfCto) {
        this.idItemNfCto = idItemNfCto;
    }

    public Long getCdItemNfCto() {
        return this.cdItemNfCto;
    }

    public void setCdItemNfCto(Long cdItemNfCto) {
        this.cdItemNfCto = cdItemNfCto;
    }

    public BigDecimal getVlUnitario() {
        return this.vlUnitario;
    }

    public void setVlUnitario(BigDecimal vlUnitario) {
        this.vlUnitario = vlUnitario;
    }

    public Integer getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Integer qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public String getDsItemNfCto() {
        return this.dsItemNfCto;
    }

    public void setDsItemNfCto(String dsItemNfCto) {
        this.dsItemNfCto = dsItemNfCto;
    }

    public String getDsEmbalagem() {
        return this.dsEmbalagem;
    }

    public void setDsEmbalagem(String dsEmbalagem) {
        this.dsEmbalagem = dsEmbalagem;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idItemNfCto", getIdItemNfCto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemNfCto))
			return false;
        ItemNfCto castOther = (ItemNfCto) other;
		return new EqualsBuilder().append(this.getIdItemNfCto(),
				castOther.getIdItemNfCto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemNfCto()).toHashCode();
    }

}
