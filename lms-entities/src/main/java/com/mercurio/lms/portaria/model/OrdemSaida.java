package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class OrdemSaida implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOrdemSaida;

    /** persistent field */
    private DateTime dhRegistro;

    /** persistent field */
    private String obMotivo;

    /** nullable persistent field */
    private DateTime dhChegada;

    /** nullable persistent field */
    private DateTime dhSaida;

    /** persistent field */
    private Boolean blSemRetorno;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviarioByIdSemiReboque;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviarioByIdMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private List controleQuilometragems;
    
    public Long getIdOrdemSaida() {
        return this.idOrdemSaida;
    }

    public void setIdOrdemSaida(Long idOrdemSaida) {
        this.idOrdemSaida = idOrdemSaida;
    }

    public DateTime getDhRegistro() {
        return this.dhRegistro;
    }

    public void setDhRegistro(DateTime dhRegistro) {
        this.dhRegistro = dhRegistro;
    }

    public String getObMotivo() {
        return this.obMotivo;
    }

    public void setObMotivo(String obMotivo) {
        this.obMotivo = obMotivo;
    }

    public DateTime getDhChegada() {
        return this.dhChegada;
    }

    public void setDhChegada(DateTime dhChegada) {
        this.dhChegada = dhChegada;
    }

    public DateTime getDhSaida() {
        return this.dhSaida;
    }

    public void setDhSaida(DateTime dhSaida) {
        this.dhSaida = dhSaida;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviarioByIdSemiReboque() {
        return this.meioTransporteRodoviarioByIdSemiReboque;
    }

	public void setMeioTransporteRodoviarioByIdSemiReboque(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviarioByIdSemiReboque) {
        this.meioTransporteRodoviarioByIdSemiReboque = meioTransporteRodoviarioByIdSemiReboque;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviarioByIdMeioTransporte() {
        return this.meioTransporteRodoviarioByIdMeioTransporte;
    }

	public void setMeioTransporteRodoviarioByIdMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviarioByIdMeioTransporte) {
        this.meioTransporteRodoviarioByIdMeioTransporte = meioTransporteRodoviarioByIdMeioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ControleQuilometragem.class)     
    public List getControleQuilometragems() {
        return this.controleQuilometragems;
    }

    public void setControleQuilometragems(List controleQuilometragems) {
        this.controleQuilometragems = controleQuilometragems;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOrdemSaida",
				getIdOrdemSaida()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_ORDEM_SAIDA", idOrdemSaida)
				.append("DH_REGISTRO", dhRegistro)
				.append("ID_FILIAL_ORIGEM", filialByIdFilialOrigem != null ? filialByIdFilialOrigem.getIdFilial() : null)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("ID_MOTORISTA", motorista != null ? motorista.getIdMotorista() : null)
				.append("OB_MOTIVO", obMotivo)
				.append("BL_SEM_RETORNO", blSemRetorno)
				.append("ID_MEIO_TRANSPORTE", meioTransporteRodoviarioByIdMeioTransporte != null ? meioTransporteRodoviarioByIdMeioTransporte.getIdMeioTransporte() : null)
				.append("ID_SEMI_REBOQUE", meioTransporteRodoviarioByIdSemiReboque != null ? meioTransporteRodoviarioByIdSemiReboque.getIdMeioTransporte() : null)
				.append("DH_SAIDA", dhSaida)
				.append("DH_CHEGADA", dhChegada)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OrdemSaida))
			return false;
        OrdemSaida castOther = (OrdemSaida) other;
		return new EqualsBuilder().append(this.getIdOrdemSaida(),
				castOther.getIdOrdemSaida()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOrdemSaida()).toHashCode();
    }

	/**
	 * @return Returns the blSemRetorno.
	 */
	public Boolean getBlSemRetorno() {
		return blSemRetorno;
	}

	/**
	 * @param blSemRetorno
	 *            The blSemRetorno to set.
	 */
	public void setBlSemRetorno(Boolean blSemRetorno) {
		this.blSemRetorno = blSemRetorno;
	}

}
