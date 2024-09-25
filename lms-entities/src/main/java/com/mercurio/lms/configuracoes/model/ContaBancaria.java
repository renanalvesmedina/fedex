package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ContaBancaria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idContaBancaria;

    /** persistent field */
    private String nrContaBancaria;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpConta;

    /** persistent field */
    private String dvContaBancaria;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List chequeReembolsos;

    /** persistent field */
    private List reciboFreteCarreteiros;

    public Long getIdContaBancaria() {
        return this.idContaBancaria;
    }

    public void setIdContaBancaria(Long idContaBancaria) {
        this.idContaBancaria = idContaBancaria;
    }

    public String getNrContaBancaria() {
        return this.nrContaBancaria;
    }

    public void setNrContaBancaria(String nrContaBancaria) {
        this.nrContaBancaria = nrContaBancaria;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public DomainValue getTpConta() {
        return this.tpConta;
    }

    public void setTpConta(DomainValue tpConta) {
        this.tpConta = tpConta;
    }

    public String getDvContaBancaria() {
        return this.dvContaBancaria;
    }

    public void setDvContaBancaria(String dvContaBancaria) {
        this.dvContaBancaria = dvContaBancaria;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.configuracoes.model.AgenciaBancaria getAgenciaBancaria() {
        return this.agenciaBancaria;
    }

	public void setAgenciaBancaria(
			com.mercurio.lms.configuracoes.model.AgenciaBancaria agenciaBancaria) {
        this.agenciaBancaria = agenciaBancaria;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ChequeReembolso.class)     
    public List getChequeReembolsos() {
        return this.chequeReembolsos;
    }

    public void setChequeReembolsos(List chequeReembolsos) {
        this.chequeReembolsos = chequeReembolsos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
    public List getReciboFreteCarreteiros() {
        return this.reciboFreteCarreteiros;
    }

    public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
    }
    
    /**
	 * Retorna o número da conta com o dígito verificador no formato :
	 * xxxxxxx-xx
	 * 
	 * @return String Retorna o número da conta com o dígito verificador no
	 *         formato : xxxxxxx-xx
     */
    public String getNrContaDvConta(){
    	return this.getNrContaBancaria() + "-" + this.getDvContaBancaria(); 
    }

    public String toString() {
		return new ToStringBuilder(this).append("idContaBancaria",
				getIdContaBancaria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ContaBancaria))
			return false;
        ContaBancaria castOther = (ContaBancaria) other;
		return new EqualsBuilder().append(this.getIdContaBancaria(),
				castOther.getIdContaBancaria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdContaBancaria()).toHashCode();
    }

}
