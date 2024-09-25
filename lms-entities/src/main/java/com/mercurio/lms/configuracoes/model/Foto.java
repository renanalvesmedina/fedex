package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Foto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFoto;

    /** persistent field */
    private byte[] foto;

    /** persistent field */
    private List fotoOcorrencias;

    /** persistent field */
    private List fotoCarregmtoDescargas;

    /** persistent field */
    private List mercadoriaPendenciaMzs;

    /** persistent field */
    private List fotoProcessoSinistros;

    public Long getIdFoto() {
        return this.idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.FotoOcorrencia.class)     
    public List getFotoOcorrencias() {
        return this.fotoOcorrencias;
    }

    public void setFotoOcorrencias(List fotoOcorrencias) {
        this.fotoOcorrencias = fotoOcorrencias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga.class)     
    public List getFotoCarregmtoDescargas() {
        return this.fotoCarregmtoDescargas;
    }

    public void setFotoCarregmtoDescargas(List fotoCarregmtoDescargas) {
        this.fotoCarregmtoDescargas = fotoCarregmtoDescargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzs() {
        return this.mercadoriaPendenciaMzs;
    }

    public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
        this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.FotoProcessoSinistro.class)     
    public List getFotoProcessoSinistros() {
        return this.fotoProcessoSinistros;
    }

    public void setFotoProcessoSinistros(List fotoProcessoSinistros) {
        this.fotoProcessoSinistros = fotoProcessoSinistros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFoto", getIdFoto())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Foto))
			return false;
        Foto castOther = (Foto) other;
		return new EqualsBuilder().append(this.getIdFoto(),
				castOther.getIdFoto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFoto()).toHashCode();
    }

}
