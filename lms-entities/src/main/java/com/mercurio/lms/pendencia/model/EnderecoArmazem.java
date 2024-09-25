package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EnderecoArmazem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEnderecoArmazem;

    /** persistent field */
    private Short nrRua;

    /** persistent field */
    private Short nrPredio;
    
    /** persistent field */
    private Short nrAndar;

    /** persistent field */
    private Short nrApartamento;

    /** persistent field */
    private DomainValue tpFinalidade;
    
    /** persistent field */
    private DomainValue tpSituacaoEndereco;

    /** persistent field */
    private DomainValue tpTipo;    
    
    /** persistent field */
    private DomainValue tpCapacidade;    

    /** persistent field */
    private DomainValue tpLadoRua;    
        
    /** persistent field */
    private Long nrAltitudeEnderecos;    
    
    /** persistent field */
    private Integer qtPaleteReservado;    

    /** persistent field */
    private Integer qtIndCapacidadeEndereco;  
 
    /** persistent field */
    private Integer qtPaleteOcupado;  

    /** persistent field */
    private YearMonthDay dtUltimoInventario;  
    
    /** persistent field */
    private com.mercurio.lms.pendencia.model.Modulo modulo;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.Posto posto;
    
    /** persistent field */
	private List entradaPendenciaMatrizs;

    public YearMonthDay getDtUltimoInventario() {
		return dtUltimoInventario;
	}

	public void setDtUltimoInventario(YearMonthDay dtUltimoInventario) {
		this.dtUltimoInventario = dtUltimoInventario;
	}

	public Long getIdEnderecoArmazem() {
		return idEnderecoArmazem;
	}

	public void setIdEnderecoArmazem(Long idEnderecoArmazem) {
		this.idEnderecoArmazem = idEnderecoArmazem;
	}

	public com.mercurio.lms.pendencia.model.Modulo getModulo() {
		return modulo;
	}

	public void setModulo(com.mercurio.lms.pendencia.model.Modulo modulo) {
		this.modulo = modulo;
	}

	public com.mercurio.lms.pendencia.model.Posto getPosto() {
		return posto;
	}

	public void setPosto(com.mercurio.lms.pendencia.model.Posto posto) {
		this.posto = posto;
	}

	public Long getNrAltitudeEnderecos() {
		return nrAltitudeEnderecos;
	}

	public void setNrAltitudeEnderecos(Long nrAltitudeEnderecos) {
		this.nrAltitudeEnderecos = nrAltitudeEnderecos;
	}

	public Short getNrAndar() {
		return nrAndar;
	}

	public void setNrAndar(Short nrAndar) {
		this.nrAndar = nrAndar;
	}

	public Short getNrApartamento() {
		return nrApartamento;
	}

	public void setNrApartamento(Short nrApartamento) {
		this.nrApartamento = nrApartamento;
	}

	public Short getNrPredio() {
		return nrPredio;
	}

	public void setNrPredio(Short nrPredio) {
		this.nrPredio = nrPredio;
	}

	public Short getNrRua() {
		return nrRua;
	}

	public void setNrRua(Short nrRua) {
		this.nrRua = nrRua;
	}

	public Integer getQtIndCapacidadeEndereco() {
		return qtIndCapacidadeEndereco;
	}

	public void setQtIndCapacidadeEndereco(Integer qtIndCapacidadeEndereco) {
		this.qtIndCapacidadeEndereco = qtIndCapacidadeEndereco;
	}

	public Integer getQtPaleteOcupado() {
		return qtPaleteOcupado;
	}

	public void setQtPaleteOcupado(Integer qtPaleteOcupado) {
		this.qtPaleteOcupado = qtPaleteOcupado;
	}

	public Integer getQtPaleteReservado() {
		return qtPaleteReservado;
	}

	public void setQtPaleteReservado(Integer qtPaleteReservado) {
		this.qtPaleteReservado = qtPaleteReservado;
	}

	public DomainValue getTpCapacidade() {
		return tpCapacidade;
	}

	public void setTpCapacidade(DomainValue tpCapacidade) {
		this.tpCapacidade = tpCapacidade;
	}

	public DomainValue getTpFinalidade() {
		return tpFinalidade;
	}

	public void setTpFinalidade(DomainValue tpFinalidade) {
		this.tpFinalidade = tpFinalidade;
	}

	public DomainValue getTpLadoRua() {
		return tpLadoRua;
	}

	public void setTpLadoRua(DomainValue tpLadoRua) {
		this.tpLadoRua = tpLadoRua;
	}

	public DomainValue getTpSituacaoEndereco() {
		return tpSituacaoEndereco;
	}

	public void setTpSituacaoEndereco(DomainValue tpSituacaoEndereco) {
		this.tpSituacaoEndereco = tpSituacaoEndereco;
	}

	public DomainValue getTpTipo() {
		return tpTipo;
	}

	public void setTpTipo(DomainValue tpTipo) {
		this.tpTipo = tpTipo;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz.class)     
    public List getEntradaPendenciaMatrizs() {
        return this.entradaPendenciaMatrizs;
    }

    public void setEntradaPendenciaMatrizs(List entradaPendenciaMatrizs) {
        this.entradaPendenciaMatrizs = entradaPendenciaMatrizs;
    }	
	
	public String toString() {
		return new ToStringBuilder(this).append("idEnderecoArmazem",
				getIdEnderecoArmazem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EnderecoArmazem))
			return false;
        EnderecoArmazem castOther = (EnderecoArmazem) other;
		return new EqualsBuilder().append(this.getIdEnderecoArmazem(),
				castOther.getIdEnderecoArmazem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEnderecoArmazem())
            .toHashCode();
    }

}
