package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class BaixaDevMerc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBaixaDevMerc;

    /** persistent field */
    private Long nrBdm;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private DateTime dhTransmissao;
    
    private DateTime dhCancelamento;

	/** persistent field */
    private Integer versao;

    /** persistent field */
    private List itemBaixaDevMercs;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialEmissora;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialDestino;
    
    public Long getIdBaixaDevMerc() {
        return this.idBaixaDevMerc;
    }

    public void setIdBaixaDevMerc(Long idBaixaDevMerc) {
        this.idBaixaDevMerc = idBaixaDevMerc;
    }

    public Long getNrBdm() {
        return this.nrBdm;
    }

    public void setNrBdm(Long nrBdm) {
        this.nrBdm = nrBdm;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DateTime getDhTransmissao() {
        return this.dhTransmissao;
    }

    public void setDhTransmissao(DateTime dhTransmissao) {
        this.dhTransmissao = dhTransmissao;
    }

    /**
	 * @return the dhCancelamento
	 */
	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	/**
	 * @param dhCancelamento the dhCancelamento to set
	 */
	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

    public Integer getVersao() {
        return this.versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemBaixaDevMerc.class) 
    public List getItemBaixaDevMercs() {
        return this.itemBaixaDevMercs;
    }

    public void setItemBaixaDevMercs(List itemBaixaDevMercs) {
        this.itemBaixaDevMercs = itemBaixaDevMercs;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return cliente;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialEmissora() {
		return filialEmissora;
	}

	public void setFilialEmissora(
			com.mercurio.lms.municipios.model.Filial filialEmissora) {
		this.filialEmissora = filialEmissora;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idBaixaDevMerc",
				getIdBaixaDevMerc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BaixaDevMerc))
			return false;
        BaixaDevMerc castOther = (BaixaDevMerc) other;
		return new EqualsBuilder().append(this.getIdBaixaDevMerc(),
				castOther.getIdBaixaDevMerc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBaixaDevMerc()).toHashCode();
    }

}
