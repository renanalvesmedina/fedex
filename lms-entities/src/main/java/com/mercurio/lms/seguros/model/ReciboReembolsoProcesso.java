package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboReembolsoProcesso implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReciboReembolsoProcesso;

    /** persistent field */
    private Long nrRecibo;

    /** persistent field */
    private YearMonthDay dtReebolso;

    /** persistent field */
    private BigDecimal vlReembolso;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;

    /** persistent field */
    private List reciboReembolsoDoctoServicos;
    
    /** persistent field */
    private Integer versao;

    /** persistent field */
    private BigDecimal vlReembolsoAvulso;
    
    /** nullable persistent field */
    private String obReciboReembolso;
    
    public Long getIdReciboReembolsoProcesso() {
        return this.idReciboReembolsoProcesso;
    }

    public void setIdReciboReembolsoProcesso(Long idReciboReembolsoProcesso) {
        this.idReciboReembolsoProcesso = idReciboReembolsoProcesso;
    }

    public Long getNrRecibo() {
        return this.nrRecibo;
    }

    public void setNrRecibo(Long nrRecibo) {
        this.nrRecibo = nrRecibo;
    }

    public YearMonthDay getDtReebolso() {
        return this.dtReebolso;
    }

    public void setDtReebolso(YearMonthDay dtReebolso) {
        this.dtReebolso = dtReebolso;
    }

    public BigDecimal getVlReembolso() {
        return this.vlReembolso;
    }

    public void setVlReembolso(BigDecimal vlReembolso) {
        this.vlReembolso = vlReembolso;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
        return this.processoSinistro;
    }

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
        this.processoSinistro = processoSinistro;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ReciboReembolsoDoctoServico.class)     
    public List getReciboReembolsoDoctoServicos() {
        return this.reciboReembolsoDoctoServicos;
    }

	public void setReciboReembolsoDoctoServicos(
			List reciboReembolsoDoctoServicos) {
        this.reciboReembolsoDoctoServicos = reciboReembolsoDoctoServicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReciboReembolsoProcesso",
				getIdReciboReembolsoProcesso()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboReembolsoProcesso))
			return false;
        ReciboReembolsoProcesso castOther = (ReciboReembolsoProcesso) other;
		return new EqualsBuilder().append(this.getIdReciboReembolsoProcesso(),
				castOther.getIdReciboReembolsoProcesso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboReembolsoProcesso())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlReembolsoAvulso() {
		return vlReembolsoAvulso;
	}

	public void setVlReembolsoAvulso(BigDecimal vlReembolsoAvulso) {
		this.vlReembolsoAvulso = vlReembolsoAvulso;
	}

	public String getObReciboReembolso() {
		return obReciboReembolso;
	}

	public void setObReciboReembolso(String obReciboReembolso) {
		this.obReciboReembolso = obReciboReembolso;
	}

}
