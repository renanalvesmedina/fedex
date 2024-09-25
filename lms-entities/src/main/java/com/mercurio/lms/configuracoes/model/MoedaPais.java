package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class MoedaPais implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_MOEDA_BY_PAIS_SITUACAO = "moedaPais.findMoedaByPaisSituacao";
	
    /** identifier field */
    private Long idMoedaPais;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Boolean blIndicadorPadrao;

    /** persistent field */
    private Boolean blIndicadorMaisUtilizada;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private List cheques;

    /** persistent field */
    private List cotacaoMoedas;

    /** persistent field */
    private List rotaIdaVoltas;

    /** persistent field */
    private List solicMonitPreventivos;

    public Long getIdMoedaPais() {
        return this.idMoedaPais;
    }

    public void setIdMoedaPais(Long idMoedaPais) {
        this.idMoedaPais = idMoedaPais;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Boolean getBlIndicadorPadrao() {
        return this.blIndicadorPadrao;
    }

    public void setBlIndicadorPadrao(Boolean blIndicadorPadrao) {
        this.blIndicadorPadrao = blIndicadorPadrao;
    }

    public Boolean getBlIndicadorMaisUtilizada() {
        return this.blIndicadorMaisUtilizada;
    }

    public void setBlIndicadorMaisUtilizada(Boolean blIndicadorMaisUtilizada) {
        this.blIndicadorMaisUtilizada = blIndicadorMaisUtilizada;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cheque.class)     
    public List getCheques() {
        return this.cheques;
    }

    public void setCheques(List cheques) {
        this.cheques = cheques;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.CotacaoMoeda.class)     
    public List getCotacaoMoedas() {
        return this.cotacaoMoedas;
    }
    
    public void setCotacaoMoedas(List cotacaoMoedas) {
        this.cotacaoMoedas = cotacaoMoedas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaIdaVolta.class)     
    public List getRotaIdaVoltas() {
        return this.rotaIdaVoltas;
    }

    public void setRotaIdaVoltas(List rotaIdaVoltas) {
        this.rotaIdaVoltas = rotaIdaVoltas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List getSolicMonitPreventivos() {
		return solicMonitPreventivos;
	}

	public void setSolicMonitPreventivos(List solicMonitPreventivos) {
		this.solicMonitPreventivos = solicMonitPreventivos;
	}

	public String toString() {
        return new ToStringBuilder(this)
				.append("idMoedaPais", getIdMoedaPais()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MoedaPais))
			return false;
        MoedaPais castOther = (MoedaPais) other;
		return new EqualsBuilder().append(this.getIdMoedaPais(),
				castOther.getIdMoedaPais()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMoedaPais()).toHashCode();
    }

}
