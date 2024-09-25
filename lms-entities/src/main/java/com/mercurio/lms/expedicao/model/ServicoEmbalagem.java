package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoEmbalagem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoEmbalagem;

    /** nullable persistent field */
    private Integer nrQuantidade;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Embalagem embalagem;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    public Long getIdServicoEmbalagem() {
        return this.idServicoEmbalagem;
    }

    public void setIdServicoEmbalagem(Long idServicoEmbalagem) {
        this.idServicoEmbalagem = idServicoEmbalagem;
    }

    public Integer getNrQuantidade() {
        return this.nrQuantidade;
    }

    public void setNrQuantidade(Integer nrQuantidade) {
        this.nrQuantidade = nrQuantidade;
    }

    public com.mercurio.lms.expedicao.model.Embalagem getEmbalagem() {
        return this.embalagem;
    }

	public void setEmbalagem(
			com.mercurio.lms.expedicao.model.Embalagem embalagem) {
        this.embalagem = embalagem;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoEmbalagem",
				getIdServicoEmbalagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoEmbalagem))
			return false;
        ServicoEmbalagem castOther = (ServicoEmbalagem) other;
		return new EqualsBuilder().append(this.getIdServicoEmbalagem(),
				castOther.getIdServicoEmbalagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoEmbalagem())
            .toHashCode();
    }

}
