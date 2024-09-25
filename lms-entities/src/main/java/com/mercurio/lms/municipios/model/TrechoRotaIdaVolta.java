package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class TrechoRotaIdaVolta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTrechoRotaIdaVolta;

    /** field */
    private Integer versao;
    
    /** persistent field */
    private TimeOfDay hrSaida;

    /** persistent field */
    private Integer nrTempoViagem;

    /** persistent field */
    private Integer nrTempoOperacao;

    /** persistent field */
    private Integer nrDistancia;

    /** persistent field */
    private Boolean blDomingo;

    /** persistent field */
    private Boolean blSegunda;

    /** persistent field */
    private Boolean blTerca;

    /** persistent field */
    private Boolean blQuarta;

    /** persistent field */
    private Boolean blQuinta;

    /** persistent field */
    private Boolean blSexta;

    /** persistent field */
    private Boolean blSabado;
    
    /** persistent field */
    private BigDecimal vlRateio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.FilialRota filialRotaByIdFilialRotaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.FilialRota filialRotaByIdFilialRotaDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta;

    /** persistent field */
    private List pontoParadaTrechos;

    /** persistent field */
    private List controleTrechos;

    public Long getIdTrechoRotaIdaVolta() {
        return this.idTrechoRotaIdaVolta;
    }

    public void setIdTrechoRotaIdaVolta(Long idTrechoRotaIdaVolta) {
        this.idTrechoRotaIdaVolta = idTrechoRotaIdaVolta;
    }

    public TimeOfDay getHrSaida() {
        return this.hrSaida;
    }

    public void setHrSaida(TimeOfDay hrSaida) {
        this.hrSaida = hrSaida;
    }

    public Integer getNrTempoViagem() {
        return this.nrTempoViagem;
    }

    public void setNrTempoViagem(Integer nrTempoViagem) {
        this.nrTempoViagem = nrTempoViagem;
    }

    public Integer getNrTempoOperacao() {
        return this.nrTempoOperacao;
    }

    public void setNrTempoOperacao(Integer nrTempoOperacao) {
        this.nrTempoOperacao = nrTempoOperacao;
    }

    public Integer getNrDistancia() {
        return this.nrDistancia;
    }

    public void setNrDistancia(Integer nrDistancia) {
        this.nrDistancia = nrDistancia;
    }

    public Boolean getBlDomingo() {
        return this.blDomingo;
    }

    public void setBlDomingo(Boolean blDomingo) {
        this.blDomingo = blDomingo;
    }

    public Boolean getBlSegunda() {
        return this.blSegunda;
    }

    public void setBlSegunda(Boolean blSegunda) {
        this.blSegunda = blSegunda;
    }

    public Boolean getBlTerca() {
        return this.blTerca;
    }

    public void setBlTerca(Boolean blTerca) {
        this.blTerca = blTerca;
    }

    public Boolean getBlQuarta() {
        return this.blQuarta;
    }

    public void setBlQuarta(Boolean blQuarta) {
        this.blQuarta = blQuarta;
    }

    public Boolean getBlQuinta() {
        return this.blQuinta;
    }

    public void setBlQuinta(Boolean blQuinta) {
        this.blQuinta = blQuinta;
    }

    public Boolean getBlSexta() {
        return this.blSexta;
    }

    public void setBlSexta(Boolean blSexta) {
        this.blSexta = blSexta;
    }

    public Boolean getBlSabado() {
        return this.blSabado;
    }

    public void setBlSabado(Boolean blSabado) {
        this.blSabado = blSabado;
    }

    public com.mercurio.lms.municipios.model.FilialRota getFilialRotaByIdFilialRotaOrigem() {
        return this.filialRotaByIdFilialRotaOrigem;
    }

	public void setFilialRotaByIdFilialRotaOrigem(
			com.mercurio.lms.municipios.model.FilialRota filialRotaByIdFilialRotaOrigem) {
        this.filialRotaByIdFilialRotaOrigem = filialRotaByIdFilialRotaOrigem;
    }

    public com.mercurio.lms.municipios.model.FilialRota getFilialRotaByIdFilialRotaDestino() {
        return this.filialRotaByIdFilialRotaDestino;
    }

	public void setFilialRotaByIdFilialRotaDestino(
			com.mercurio.lms.municipios.model.FilialRota filialRotaByIdFilialRotaDestino) {
        this.filialRotaByIdFilialRotaDestino = filialRotaByIdFilialRotaDestino;
    }

    public com.mercurio.lms.municipios.model.RotaIdaVolta getRotaIdaVolta() {
        return this.rotaIdaVolta;
    }

	public void setRotaIdaVolta(
			com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta) {
        this.rotaIdaVolta = rotaIdaVolta;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PontoParadaTrecho.class)     
    public List getPontoParadaTrechos() {
        return this.pontoParadaTrechos;
    }

    public void setPontoParadaTrechos(List pontoParadaTrechos) {
        this.pontoParadaTrechos = pontoParadaTrechos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleTrecho.class)     
    public List getControleTrechos() {
        return this.controleTrechos;
    }

    public void setControleTrechos(List controleTrechos) {
        this.controleTrechos = controleTrechos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTrechoRotaIdaVolta",
				getIdTrechoRotaIdaVolta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TrechoRotaIdaVolta))
			return false;
        TrechoRotaIdaVolta castOther = (TrechoRotaIdaVolta) other;
		return new EqualsBuilder().append(this.getIdTrechoRotaIdaVolta(),
				castOther.getIdTrechoRotaIdaVolta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTrechoRotaIdaVolta())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlRateio() {
		return vlRateio;
	}

	public void setVlRateio(BigDecimal vlRateio) {
		this.vlRateio = vlRateio;
	}
	
}
