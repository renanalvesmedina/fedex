package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Pais;

/** @author LMS Custom Hibernate CodeGenerator */
public class Banco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBanco;

    /** persistent field */
    private Short nrBanco;

    /** persistent field */
    private String nmBanco;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private List recebimentoRecibos;

    /** persistent field */
    private List reciboIndenizacoes;

    /** persistent field */
    private List agenciaBancarias;

    /** persistent field */
    private List ocorrenciaBancos;

    /** persistent field */
    private List clientes;

    public Long getIdBanco() {
        return this.idBanco;
    }

    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    public Short getNrBanco() {
        return this.nrBanco;
    }

    public void setNrBanco(Short nrBanco) {
        this.nrBanco = nrBanco;
    }

    public String getNmBanco() {
        return this.nmBanco;
    }

    public void setNmBanco(String nmBanco) {
        this.nmBanco = nmBanco;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.RecebimentoRecibo.class)     
    public List getRecebimentoRecibos() {
        return this.recebimentoRecibos;
    }

    public void setRecebimentoRecibos(List recebimentoRecibos) {
        this.recebimentoRecibos = recebimentoRecibos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacao.class)     
    public List getReciboIndenizacoes() {
        return this.reciboIndenizacoes;
    }

    public void setReciboIndenizacoes(List reciboIndenizacoes) {
        this.reciboIndenizacoes = reciboIndenizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.AgenciaBancaria.class)     
    public List getAgenciaBancarias() {
        return this.agenciaBancarias;
    }

    public void setAgenciaBancarias(List agenciaBancarias) {
        this.agenciaBancarias = agenciaBancarias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.OcorrenciaBanco.class)     
    public List getOcorrenciaBancos() {
        return this.ocorrenciaBancos;
    }

    public void setOcorrenciaBancos(List ocorrenciaBancos) {
        this.ocorrenciaBancos = ocorrenciaBancos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cliente.class)     
    public List getClientes() {
        return this.clientes;
    }

    public void setClientes(List clientes) {
        this.clientes = clientes;
    }

    /**
     * Nome completo do Banco e seu pais
	 * 
     * @return "Nome do Banco - Nome do País" 
     */
    public String getNmBancoNmPais(){
    	Pais p = this.getPais();
    	
    	if (Hibernate.isInitialized(p) && p != null){
    		return this.getNmBanco() + " - " + p.getNmPais();
    	}else{
    		return this.getNmBanco();
    	}
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idBanco", getIdBanco())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Banco))
			return false;
        Banco castOther = (Banco) other;
		return new EqualsBuilder().append(this.getIdBanco(),
				castOther.getIdBanco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBanco()).toHashCode();
    }

}
