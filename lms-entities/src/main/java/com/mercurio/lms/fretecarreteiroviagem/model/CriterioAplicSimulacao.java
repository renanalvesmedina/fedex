package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CriterioAplicSimulacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCriterioAplicSimulacao;

    /** persistent field */
    private Boolean blAplicacao;

    /** persistent field */
    private DomainValue tpRota;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Regional regional;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.ParametroSimulacaoRota parametroSimulacaoRota;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda proprietario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda filial;

    public Long getIdCriterioAplicSimulacao() {
        return this.idCriterioAplicSimulacao;
    }

    public void setIdCriterioAplicSimulacao(Long idCriterioAplicSimulacao) {
        this.idCriterioAplicSimulacao = idCriterioAplicSimulacao;
    }

    public Boolean getBlAplicacao() {
        return this.blAplicacao;
    }

    public void setBlAplicacao(Boolean blAplicacao) {
        this.blAplicacao = blAplicacao;
    }

    public DomainValue getTpRota() {
        return this.tpRota;
    }

    public void setTpRota(DomainValue tpRota) {
        this.tpRota = tpRota;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.municipios.model.Regional getRegional() {
        return this.regional;
    }

    public void setRegional(com.mercurio.lms.municipios.model.Regional regional) {
        this.regional = regional;
    }

    public com.mercurio.lms.municipios.model.RotaIdaVolta getRotaIdaVolta() {
        return this.rotaIdaVolta;
    }

	public void setRotaIdaVolta(
			com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta) {
        this.rotaIdaVolta = rotaIdaVolta;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.ParametroSimulacaoRota getParametroSimulacaoRota() {
        return this.parametroSimulacaoRota;
    }

	public void setParametroSimulacaoRota(
			com.mercurio.lms.fretecarreteiroviagem.model.ParametroSimulacaoRota parametroSimulacaoRota) {
        this.parametroSimulacaoRota = parametroSimulacaoRota;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.configuracoes.model.Moeda proprietario) {
        this.proprietario = proprietario;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.configuracoes.model.Moeda filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCriterioAplicSimulacao",
				getIdCriterioAplicSimulacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CriterioAplicSimulacao))
			return false;
        CriterioAplicSimulacao castOther = (CriterioAplicSimulacao) other;
		return new EqualsBuilder().append(this.getIdCriterioAplicSimulacao(),
				castOther.getIdCriterioAplicSimulacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCriterioAplicSimulacao())
            .toHashCode();
    }

}
