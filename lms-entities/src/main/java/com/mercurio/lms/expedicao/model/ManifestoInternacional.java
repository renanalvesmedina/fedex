package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoInternacional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoInternacional;

    /** persistent field */
    private Long nrApoliceSeguroCarga;

    /** persistent field */
    private YearMonthDay dtVencimentoPermisso;

    /** persistent field */
    private YearMonthDay dtVencimentoSeguro;

    /** persistent field */
    private Long nrManifestoInt;

    /** persistent field */
    private Long nrPermisso;

    /** persistent field */
    private DomainValue tpMic;

    /** persistent field */
    private Boolean blTransitoAduaneiro;

    /** persistent field */
    private String dsAduanaCidadePaisPartida;

    /** persistent field */
    private String dsCidadePaisDestino;

    /** persistent field */
    private String dsRota;

    /** persistent field */
    private String dsPermisso;

    /** nullable persistent field */
    private String dsCodigoAduana;

    /** nullable persistent field */
    private String dsCodigoPaisDestino;

    /** nullable persistent field */
    private String dsNumeroDta;

    /** nullable persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private List manifestoInternacCtos;

    public Long getIdManifestoInternacional() {
        return this.idManifestoInternacional;
    }

    public void setIdManifestoInternacional(Long idManifestoInternacional) {
        this.idManifestoInternacional = idManifestoInternacional;
    }

    public Long getNrApoliceSeguroCarga() {
        return this.nrApoliceSeguroCarga;
    }

    public void setNrApoliceSeguroCarga(Long nrApoliceSeguroCarga) {
        this.nrApoliceSeguroCarga = nrApoliceSeguroCarga;
    }

    public YearMonthDay getDtVencimentoPermisso() {
        return this.dtVencimentoPermisso;
    }

    public void setDtVencimentoPermisso(YearMonthDay dtVencimentoPermisso) {
        this.dtVencimentoPermisso = dtVencimentoPermisso;
    }

    public YearMonthDay getDtVencimentoSeguro() {
        return this.dtVencimentoSeguro;
    }

    public void setDtVencimentoSeguro(YearMonthDay dtVencimentoSeguro) {
        this.dtVencimentoSeguro = dtVencimentoSeguro;
    }

    public Long getNrManifestoInt() {
        return this.nrManifestoInt;
    }

    public void setNrManifestoInt(Long nrManifestoInt) {
        this.nrManifestoInt = nrManifestoInt;
    }

    public Long getNrPermisso() {
        return this.nrPermisso;
    }

    public void setNrPermisso(Long nrPermisso) {
        this.nrPermisso = nrPermisso;
    }

    public DomainValue getTpMic() {
        return this.tpMic;
    }

    public void setTpMic(DomainValue tpMic) {
        this.tpMic = tpMic;
    }

    public Boolean getBlTransitoAduaneiro() {
        return this.blTransitoAduaneiro;
    }

    public void setBlTransitoAduaneiro(Boolean blTransitoAduaneiro) {
        this.blTransitoAduaneiro = blTransitoAduaneiro;
    }

    public String getDsAduanaCidadePaisPartida() {
        return this.dsAduanaCidadePaisPartida;
    }

    public void setDsAduanaCidadePaisPartida(String dsAduanaCidadePaisPartida) {
        this.dsAduanaCidadePaisPartida = dsAduanaCidadePaisPartida;
    }

    public String getDsCidadePaisDestino() {
        return this.dsCidadePaisDestino;
    }

    public void setDsCidadePaisDestino(String dsCidadePaisDestino) {
        this.dsCidadePaisDestino = dsCidadePaisDestino;
    }

    public String getDsRota() {
        return this.dsRota;
    }

    public void setDsRota(String dsRota) {
        this.dsRota = dsRota;
    }

    public String getDsPermisso() {
        return this.dsPermisso;
    }

    public void setDsPermisso(String dsPermisso) {
        this.dsPermisso = dsPermisso;
    }

    public String getDsCodigoAduana() {
        return this.dsCodigoAduana;
    }

    public void setDsCodigoAduana(String dsCodigoAduana) {
        this.dsCodigoAduana = dsCodigoAduana;
    }

    public String getDsCodigoPaisDestino() {
        return this.dsCodigoPaisDestino;
    }

    public void setDsCodigoPaisDestino(String dsCodigoPaisDestino) {
        this.dsCodigoPaisDestino = dsCodigoPaisDestino;
    }

    public String getDsNumeroDta() {
        return this.dsNumeroDta;
    }

    public void setDsNumeroDta(String dsNumeroDta) {
        this.dsNumeroDta = dsNumeroDta;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ManifestoInternacCto.class)     
    public List getManifestoInternacCtos() {
        return this.manifestoInternacCtos;
    }

    public void setManifestoInternacCtos(List manifestoInternacCtos) {
        this.manifestoInternacCtos = manifestoInternacCtos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idManifestoInternacional",
				getIdManifestoInternacional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoInternacional))
			return false;
        ManifestoInternacional castOther = (ManifestoInternacional) other;
		return new EqualsBuilder().append(this.getIdManifestoInternacional(),
				castOther.getIdManifestoInternacional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoInternacional())
            .toHashCode();
    }

}
