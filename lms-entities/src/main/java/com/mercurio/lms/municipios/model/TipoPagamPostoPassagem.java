package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class TipoPagamPostoPassagem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoPagamPostoPassagem;

    /** persistent field */
    private VarcharI18n  dsTipoPagamPostoPassagem;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private Boolean blCartaoPedagio;

    /** persistent field */
    private List postoPassagemCcs;

    /** persistent field */
    private List tipoPagamentoPostos;

    /** persistent field */
    private List pagtoPedagioCcs;

    public Long getIdTipoPagamPostoPassagem() {
        return this.idTipoPagamPostoPassagem;
    }

    public void setIdTipoPagamPostoPassagem(Long idTipoPagamPostoPassagem) {
        this.idTipoPagamPostoPassagem = idTipoPagamPostoPassagem;
    }

    public VarcharI18n getDsTipoPagamPostoPassagem() {
		return dsTipoPagamPostoPassagem;
    }

	public void setDsTipoPagamPostoPassagem(VarcharI18n dsTipoPagamPostoPassagem) {
        this.dsTipoPagamPostoPassagem = dsTipoPagamPostoPassagem;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Boolean getBlCartaoPedagio() {
		return blCartaoPedagio;
	}

	public void setBlCartaoPedagio(Boolean blCartaoPedagio) {
		this.blCartaoPedagio = blCartaoPedagio;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List getPostoPassagemCcs() {
        return this.postoPassagemCcs;
    }

    public void setPostoPassagemCcs(List postoPassagemCcs) {
        this.postoPassagemCcs = postoPassagemCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TipoPagamentoPosto.class)     
    public List getTipoPagamentoPostos() {
        return this.tipoPagamentoPostos;
    }

    public void setTipoPagamentoPostos(List tipoPagamentoPostos) {
        this.tipoPagamentoPostos = tipoPagamentoPostos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoPedagioCc.class)     
    public List getPagtoPedagioCcs() {
        return this.pagtoPedagioCcs;
    }

    public void setPagtoPedagioCcs(List pagtoPedagioCcs) {
        this.pagtoPedagioCcs = pagtoPedagioCcs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoPagamPostoPassagem",
				getIdTipoPagamPostoPassagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoPagamPostoPassagem))
			return false;
        TipoPagamPostoPassagem castOther = (TipoPagamPostoPassagem) other;
		return new EqualsBuilder().append(this.getIdTipoPagamPostoPassagem(),
				castOther.getIdTipoPagamPostoPassagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoPagamPostoPassagem())
            .toHashCode();
    }
}
