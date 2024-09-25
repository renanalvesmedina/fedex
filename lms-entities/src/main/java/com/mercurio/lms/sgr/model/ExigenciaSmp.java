package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.municipios.model.Filial;

public class ExigenciaSmp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idExigenciaSmp;
	private SolicMonitPreventivo solicMonitPreventivo;
	private ExigenciaGerRisco exigenciaGerRisco;
	  /**   LMS - 6853  */

	private Integer qtExigida;

	private Integer qtExigidaOriginal;

	private Filial filialInicio;
	
	private Filial filialInicioOriginal;

	private Integer vlKmFranquia;
	
	private Integer vlKmFranquiaOriginal;

	private DomainValue tpManutRegistro;

	private VarcharI18n dsHistoricoAlteracao;

	public Long getIdExigenciaSmp() {
		return this.idExigenciaSmp;
	}

	public void setIdExigenciaSmp(Long idExigenciaSmp) {
		this.idExigenciaSmp = idExigenciaSmp;
	}

	public com.mercurio.lms.sgr.model.SolicMonitPreventivo getSolicMonitPreventivo() {
		return this.solicMonitPreventivo;
	}

	public void setSolicMonitPreventivo(com.mercurio.lms.sgr.model.SolicMonitPreventivo solicMonitPreventivo) {
		this.solicMonitPreventivo = solicMonitPreventivo;
	}

	public com.mercurio.lms.sgr.model.ExigenciaGerRisco getExigenciaGerRisco() {
		return this.exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(com.mercurio.lms.sgr.model.ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public Integer getQtExigida() {
		return qtExigida;
	}

	public void setQtExigida(Integer qtExigida) {
		this.qtExigida = qtExigida;
	}

	public Integer getQtExigidaOriginal() {
		return qtExigidaOriginal;
	}

	public void setQtExigidaOriginal(Integer qtExigidaOriginal) {
		this.qtExigidaOriginal = qtExigidaOriginal;
	}

	public Filial getFilialInicio() {
		return filialInicio;
	}

	public void setFilialInicio(Filial filialInicio) {
		this.filialInicio = filialInicio;
	}

	public Integer getVlKmFranquia() {
		return vlKmFranquia;
	}

	public void setVlKmFranquia(Integer vlKmFranquia) {
		this.vlKmFranquia = vlKmFranquia;
	}

	public DomainValue getTpManutRegistro() {
		return tpManutRegistro;
	}

	public void setTpManutRegistro(DomainValue tpManutRegistro) {
		this.tpManutRegistro = tpManutRegistro;
	}

	public VarcharI18n getDsHistoricoAlteracao() {
		return dsHistoricoAlteracao;
	}

	public void setDsHistoricoAlteracao(VarcharI18n dsHistoricoAlteracao) {
		this.dsHistoricoAlteracao = dsHistoricoAlteracao;
	}
	
	public Filial getFilialInicioOriginal() {
		return filialInicioOriginal;
	}

	public void setFilialInicioOriginal(Filial filialInicioOriginal) {
		this.filialInicioOriginal = filialInicioOriginal;
	}

	public Integer getVlKmFranquiaOriginal() {
		return vlKmFranquiaOriginal;
	}

	public void setVlKmFranquiaOriginal(Integer vlKmFranquiaOriginal) {
		this.vlKmFranquiaOriginal = vlKmFranquiaOriginal;
	}


	public String toString() {
		return new ToStringBuilder(this).append("idExigenciaSmp", getIdExigenciaSmp()).toString();
	}

	public boolean equals(Object other) {
		if (this == other){
			return true;
		}	
		if (!(other instanceof ExigenciaSmp)){
			return false;
		}	
		ExigenciaSmp castOther = (ExigenciaSmp) other;
		return new EqualsBuilder().append(this.getIdExigenciaSmp(), castOther.getIdExigenciaSmp()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdExigenciaSmp()).toHashCode();
	}

}
