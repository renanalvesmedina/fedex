package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgenciaBancaria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgenciaBancaria;

    /** persistent field */
    private Short nrAgenciaBancaria;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String nrDigito;

    /** nullable persistent field */
    private String dsEndereco;

    /** nullable persistent field */
    private String nmAgenciaBancaria;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Banco banco;

    /** persistent field */
    private List reciboIndenizacoes;

    /** persistent field */
    private List contaBancarias;

    /** persistent field */
    private List cedentes;

    public Long getIdAgenciaBancaria() {
        return this.idAgenciaBancaria;
    }

    public void setIdAgenciaBancaria(Long idAgenciaBancaria) {
        this.idAgenciaBancaria = idAgenciaBancaria;
    }

    public Short getNrAgenciaBancaria() {
        return this.nrAgenciaBancaria;
    }

    public void setNrAgenciaBancaria(Short nrAgenciaBancaria) {
        this.nrAgenciaBancaria = nrAgenciaBancaria;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getNrDigito() {
        return this.nrDigito;
    }

    public void setNrDigito(String nrDigito) {
        this.nrDigito = nrDigito;
    }

    public String getDsEndereco() {
        return this.dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public String getNmAgenciaBancaria() {
        return this.nmAgenciaBancaria;
    }

    public void setNmAgenciaBancaria(String nmAgenciaBancaria) {
        this.nmAgenciaBancaria = nmAgenciaBancaria;
    }

    public com.mercurio.lms.configuracoes.model.Banco getBanco() {
        return this.banco;
    }

    public void setBanco(com.mercurio.lms.configuracoes.model.Banco banco) {
        this.banco = banco;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacao.class)     
    public List getReciboIndenizacoes() {
        return this.reciboIndenizacoes;
    }

    public void setReciboIndenizacoes(List reciboIndenizacoes) {
        this.reciboIndenizacoes = reciboIndenizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ContaBancaria.class)     
    public List getContaBancarias() {
        return this.contaBancarias;
    }

    public void setContaBancarias(List contaBancarias) {
        this.contaBancarias = contaBancarias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cedente.class)     
    public List getCedentes() {
        return this.cedentes;
    }

    public void setCedentes(List cedentes) {
        this.cedentes = cedentes;
    }

    /**
     * 
     * @return
     */
    public String getNrAgenciaBancariaDigito(){
    	if (getNrDigito() != null && !"".equals(getNrDigito().trim())) {
			return new StringBuffer().append(getNrAgenciaBancaria())
					.append("-").append(getNrDigito()).toString();
    	} else {
    		if (getNrAgenciaBancaria() != null){    		
    			return getNrAgenciaBancaria().toString();
    		} else {
    			return "";
    		}
    	}
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idAgenciaBancaria",
				getIdAgenciaBancaria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgenciaBancaria))
			return false;
        AgenciaBancaria castOther = (AgenciaBancaria) other;
		return new EqualsBuilder().append(this.getIdAgenciaBancaria(),
				castOther.getIdAgenciaBancaria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgenciaBancaria())
            .toHashCode();
    }

}
