package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolEquipamentos implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEquipamento;

    /** persistent field */
    private Boolean blHabilitado;

    /** persistent field */
    private String dsNumero;

    /** persistent field */
    private DomainValue tpTarifa;

    /** nullable persistent field */
    private String dsImei;

    /** nullable persistent field */
    private Short nmPin2;

    /** nullable persistent field */
    private Short nmPin;

    /** nullable persistent field */
    private String dsIccid;

    /** nullable persistent field */
    private DateTime dhAtualizacao;

    /** nullable persistent field */
    private String nmVersao;

    /** nullable persistent field */
    private String obObservacao;

    /** persistent field */
    private Filial filial;

    /** persistent field */
    private MeioTransporte meioTransporte;

    /** persistent field */
    private VolTiposUso volTiposUso;

    /** persistent field */
    private VolModeloseqps volModeloseqp;

    /** persistent field */
    private VolOperadorasTelefonia volOperadorasTelefonia;

    /** persistent field */
    private List volLogEnviosSms;

    /** persistent field */
    private List volRetiradasEqptos;
    
    /** nullable persistent field */
    private String versaoSO;

    public Long getIdEquipamento() {
        return this.idEquipamento;
    }

    public void setIdEquipamento(Long idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public Boolean getBlHabilitado() {
        return this.blHabilitado;
    }

    public void setBlHabilitado(Boolean blHabilitado) {
        this.blHabilitado = blHabilitado;
    }

    public DomainValue getTpTarifa() {
        return this.tpTarifa;
    }

    public void setTpTarifa(DomainValue tpTarifa) {
        this.tpTarifa = tpTarifa;
    }

    public String getDsImei() {
        return this.dsImei;
    }

    public void setDsImei(String dsImei) {
        this.dsImei = dsImei;
    }

    public Short getNmPin2() {
        return this.nmPin2;
    }

    public void setNmPin2(Short nmPin2) {
        this.nmPin2 = nmPin2;
    }

    public Short getNmPin() {
        return this.nmPin;
    }

    public void setNmPin(Short nmPin) {
        this.nmPin = nmPin;
    }

    public String getDsIccid() {
        return this.dsIccid;
    }

    public void setDsIccid(String dsIccid) {
        this.dsIccid = dsIccid;
    }

    public DateTime getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(DateTime dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public String getNmVersao() {
        return this.nmVersao;
    }

    public void setNmVersao(String nmVersao) {
        this.nmVersao = nmVersao;
    }

    public String getObObservacao() {
        return this.obObservacao;
    }

    public void setObObservacao(String obObservacao) {
        this.obObservacao = obObservacao;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

    public void setMeioTransporte(MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.vol.model.VolTiposUso getVolTiposUso() {
        return this.volTiposUso;
    }

	public void setVolTiposUso(
			com.mercurio.lms.vol.model.VolTiposUso volTiposUso) {
        this.volTiposUso = volTiposUso;
    }

    public com.mercurio.lms.vol.model.VolModeloseqps getVolModeloseqp() {
        return this.volModeloseqp;
    }

	public void setVolModeloseqp(
			com.mercurio.lms.vol.model.VolModeloseqps volModeloseqp) {
        this.volModeloseqp = volModeloseqp;
    }

    public VolOperadorasTelefonia getVolOperadorasTelefonia() {
        return this.volOperadorasTelefonia;
    }

	public void setVolOperadorasTelefonia(
			VolOperadorasTelefonia volOperadorasTelefonia) {
        this.volOperadorasTelefonia = volOperadorasTelefonia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolLogEnviosSms.class)     
    public List getVolLogEnviosSms() {
        return this.volLogEnviosSms;
    }

    public void setVolLogEnviosSms(List volLogEnviosSms) {
        this.volLogEnviosSms = volLogEnviosSms;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolRetiradasEqptos.class)     
    public List getVolRetiradasEqptos() {
        return this.volRetiradasEqptos;
    }

    public void setVolRetiradasEqptos(List volRetiradasEqptos) {
        this.volRetiradasEqptos = volRetiradasEqptos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEquipamento",
				getIdEquipamento()).toString();
    }
    
    public String getVersaoSO() {
        return versaoSO;
    }

    public void setVersaoSO(String versaoSO) {
        this.versaoSO = versaoSO;
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolEquipamentos))
			return false;
        VolEquipamentos castOther = (VolEquipamentos) other;
		return new EqualsBuilder().append(this.getIdEquipamento(),
				castOther.getIdEquipamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEquipamento()).toHashCode();
    }

	public String getDsNumero() {
		return dsNumero;
	}

	public void setDsNumero(String dsNumero) {
		this.dsNumero = dsNumero;
	}

}
