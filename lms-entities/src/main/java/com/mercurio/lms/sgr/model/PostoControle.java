package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;

public class PostoControle implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long idPostoControle;
    private String nmPostoControlePassaporte;
    private String nmLocal;
    private Integer nrKm;
    private DomainValue tpSituacao;
    private String nrTelefone;
    private String nmResponsavel;
    private String dsCorreioEletronico;
    private DomainValue tpBandeiraPosto;
    private Municipio municipio;
    private ReguladoraSeguro reguladoraSeguro;
    private Rodovia rodovia;
    private List<PostoPassaporte> postoPassaportes;
	private List<RotaPostoControle> rotaPostoControles;

    public Long getIdPostoControle() {
        return idPostoControle;
    }

    public void setIdPostoControle(Long idPostoControle) {
        this.idPostoControle = idPostoControle;
    }

    public String getNmPostoControlePassaporte() {
        return nmPostoControlePassaporte;
    }

    public void setNmPostoControlePassaporte(String nmPostoControlePassaporte) {
        this.nmPostoControlePassaporte = nmPostoControlePassaporte;
    }

    public String getNmLocal() {
        return nmLocal;
    }

    public void setNmLocal(String nmLocal) {
        this.nmLocal = nmLocal;
    }

    public Integer getNrKm() {
        return nrKm;
    }

    public void setNrKm(Integer nrKm) {
        this.nrKm = nrKm;
    }

    public DomainValue getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getNrTelefone() {
        return nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public String getNmResponsavel() {
        return nmResponsavel;
    }

    public void setNmResponsavel(String nmResponsavel) {
        this.nmResponsavel = nmResponsavel;
    }

    public String getDsCorreioEletronico() {
        return dsCorreioEletronico;
    }

    public void setDsCorreioEletronico(String dsCorreioEletronico) {
        this.dsCorreioEletronico = dsCorreioEletronico;
    }

    public DomainValue getTpBandeiraPosto() {
        return tpBandeiraPosto;
    }

    public void setTpBandeiraPosto(DomainValue tpBandeiraPosto) {
        this.tpBandeiraPosto = tpBandeiraPosto;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

	public void setMunicipio(
			Municipio municipio) {
        this.municipio = municipio;
    }

    public ReguladoraSeguro getReguladoraSeguro() {
        return reguladoraSeguro;
    }

	public void setReguladoraSeguro(
			ReguladoraSeguro reguladoraSeguro) {
        this.reguladoraSeguro = reguladoraSeguro;
    }

    public Rodovia getRodovia() {
        return rodovia;
    }

    public void setRodovia(Rodovia rodovia) {
        this.rodovia = rodovia;
    }

    @ParametrizedAttribute(type = PostoPassaporte.class)     
    public List<PostoPassaporte> getPostoPassaportes() {
        return postoPassaportes;
    }

    public void setPostoPassaportes(List<PostoPassaporte> postoPassaportes) {
        this.postoPassaportes = postoPassaportes;
    }

    @ParametrizedAttribute(type = RotaPostoControle.class)     
    public List<RotaPostoControle> getRotaPostoControles() {
        return rotaPostoControles;
    }

    public void setRotaPostoControles(List<RotaPostoControle> rotaPostoControles) {
        this.rotaPostoControles = rotaPostoControles;
    }

    public String toString() {
		return new ToStringBuilder(this)
				.append(idPostoControle)
				.toString();
    }

    public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PostoControle)) {
			return false;
		}
		PostoControle cast = (PostoControle) other;
		return new EqualsBuilder()
				.append(idPostoControle, cast.idPostoControle)
				.isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder()
				.append(idPostoControle)
				.toHashCode();
    }

}
