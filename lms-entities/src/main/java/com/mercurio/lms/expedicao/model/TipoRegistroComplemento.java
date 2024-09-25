package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoRegistroComplemento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoRegistroComplemento;

    /** persistent field */
    private String dsTipoRegistroComplemento;

    /** persistent field */
    private List informacaoDocServicos;

    /** persistent field */
    private List faturas;

    private Integer versao;
    
    public Long getIdTipoRegistroComplemento() {
        return this.idTipoRegistroComplemento;
    }

    public void setIdTipoRegistroComplemento(Long idTipoRegistroComplemento) {
        this.idTipoRegistroComplemento = idTipoRegistroComplemento;
    }

    public String getDsTipoRegistroComplemento() {
        return this.dsTipoRegistroComplemento;
    }

    public void setDsTipoRegistroComplemento(String dsTipoRegistroComplemento) {
        this.dsTipoRegistroComplemento = dsTipoRegistroComplemento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.InformacaoDocServico.class)     
    public List getInformacaoDocServicos() {
        return this.informacaoDocServicos;
    }

    public void setInformacaoDocServicos(List informacaoDocServicos) {
        this.informacaoDocServicos = informacaoDocServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoRegistroComplemento",
				getIdTipoRegistroComplemento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoRegistroComplemento))
			return false;
        TipoRegistroComplemento castOther = (TipoRegistroComplemento) other;
		return new EqualsBuilder().append(this.getIdTipoRegistroComplemento(),
				castOther.getIdTipoRegistroComplemento()).isEquals();
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoRegistroComplemento())
            .toHashCode();
    }

}
