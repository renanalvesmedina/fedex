package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoInternacCto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoInternacCto;

    /** persistent field */
    private String dsAduanaDestino;

    /** persistent field */
    private String dsCodigoAduanaDestino;

    /** persistent field */
    private String dsPaisOrigem;

    /** persistent field */
    private BigDecimal vlFot;

    /** persistent field */
    private BigDecimal vlFrete;

    /** persistent field */
    private BigDecimal vlSeguro;

    /** persistent field */
    private Byte cdVolumes;

    /** persistent field */
    private Long qtVolumes;

    /** persistent field */
    private BigDecimal psBruto;

    /** persistent field */
    private BigDecimal psLiquido;

    /** persistent field */
    private String dsDadosRemetente;

    /** persistent field */
    private String dsDadosDestinatario;

    /** persistent field */
    private String dsDadosConsignatario;

    /** nullable persistent field */
    private String dsDocumentosAnexos;

    /** persistent field */
    private String dsMercadorias;

    /** nullable persistent field */
    private String dsCodigoMercadoria;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.ManifestoInternacional manifestoViagemInternacional;

    public Long getIdManifestoInternacCto() {
        return this.idManifestoInternacCto;
    }

    public void setIdManifestoInternacCto(Long idManifestoInternacCto) {
        this.idManifestoInternacCto = idManifestoInternacCto;
    }

    public String getDsAduanaDestino() {
        return this.dsAduanaDestino;
    }

    public void setDsAduanaDestino(String dsAduanaDestino) {
        this.dsAduanaDestino = dsAduanaDestino;
    }

    public String getDsCodigoAduanaDestino() {
        return this.dsCodigoAduanaDestino;
    }

    public void setDsCodigoAduanaDestino(String dsCodigoAduanaDestino) {
        this.dsCodigoAduanaDestino = dsCodigoAduanaDestino;
    }

    public String getDsPaisOrigem() {
        return this.dsPaisOrigem;
    }

    public void setDsPaisOrigem(String dsPaisOrigem) {
        this.dsPaisOrigem = dsPaisOrigem;
    }

    public BigDecimal getVlFot() {
        return this.vlFot;
    }

    public void setVlFot(BigDecimal vlFot) {
        this.vlFot = vlFot;
    }

    public BigDecimal getVlFrete() {
        return this.vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
    }

    public BigDecimal getVlSeguro() {
        return this.vlSeguro;
    }

    public void setVlSeguro(BigDecimal vlSeguro) {
        this.vlSeguro = vlSeguro;
    }

    public Byte getCdVolumes() {
        return this.cdVolumes;
    }

    public void setCdVolumes(Byte cdVolumes) {
        this.cdVolumes = cdVolumes;
    }

    public Long getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Long qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getPsBruto() {
        return this.psBruto;
    }

    public void setPsBruto(BigDecimal psBruto) {
        this.psBruto = psBruto;
    }

    public BigDecimal getPsLiquido() {
        return this.psLiquido;
    }

    public void setPsLiquido(BigDecimal psLiquido) {
        this.psLiquido = psLiquido;
    }

    public String getDsDadosRemetente() {
        return this.dsDadosRemetente;
    }

    public void setDsDadosRemetente(String dsDadosRemetente) {
        this.dsDadosRemetente = dsDadosRemetente;
    }

    public String getDsDadosDestinatario() {
        return this.dsDadosDestinatario;
    }

    public void setDsDadosDestinatario(String dsDadosDestinatario) {
        this.dsDadosDestinatario = dsDadosDestinatario;
    }

    public String getDsDadosConsignatario() {
        return this.dsDadosConsignatario;
    }

    public void setDsDadosConsignatario(String dsDadosConsignatario) {
        this.dsDadosConsignatario = dsDadosConsignatario;
    }

    public String getDsDocumentosAnexos() {
        return this.dsDocumentosAnexos;
    }

    public void setDsDocumentosAnexos(String dsDocumentosAnexos) {
        this.dsDocumentosAnexos = dsDocumentosAnexos;
    }

    public String getDsMercadorias() {
        return this.dsMercadorias;
    }

    public void setDsMercadorias(String dsMercadorias) {
        this.dsMercadorias = dsMercadorias;
    }

    public String getDsCodigoMercadoria() {
        return this.dsCodigoMercadoria;
    }

    public void setDsCodigoMercadoria(String dsCodigoMercadoria) {
        this.dsCodigoMercadoria = dsCodigoMercadoria;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.ManifestoInternacional getManifestoViagemInternacional() {
        return this.manifestoViagemInternacional;
    }

	public void setManifestoViagemInternacional(
			com.mercurio.lms.expedicao.model.ManifestoInternacional manifestoViagemInternacional) {
        this.manifestoViagemInternacional = manifestoViagemInternacional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idManifestoInternacCto",
				getIdManifestoInternacCto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoInternacCto))
			return false;
        ManifestoInternacCto castOther = (ManifestoInternacCto) other;
		return new EqualsBuilder().append(this.getIdManifestoInternacCto(),
				castOther.getIdManifestoInternacCto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoInternacCto())
            .toHashCode();
    }

}
