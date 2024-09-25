package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialCiaAerea implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialCiaAerea;

    /** persistent field */
    private Long cdFornecedor;

    /** persistent field */
    private Boolean blImprimeMinuta;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Boolean blTaxaTerrestre;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroporto;

	/**
	 * persistent field private
	 * com.mercurio.lms.configuracoes.model.InscricaoEstadual inscricaoEstadual;
	 */

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List filialMercurioFilialCias;

    /** persistent field */
    private List atendimFilialCiaAereas;

    public Long getIdFilialCiaAerea() {
        return this.idFilialCiaAerea;
    }

    public void setIdFilialCiaAerea(Long pessIdPessoa) {
        this.idFilialCiaAerea = pessIdPessoa;
    }

    public Long getCdFornecedor() {
        return this.cdFornecedor;
    }

    public void setCdFornecedor(Long cdFornecedor) {
        this.cdFornecedor = cdFornecedor;
    }

    public Boolean getBlImprimeMinuta() {
        return this.blImprimeMinuta;
    }

    public void setBlImprimeMinuta(Boolean blImprimeMinuta) {
        this.blImprimeMinuta = blImprimeMinuta;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Boolean getBlTaxaTerrestre() {
        return this.blTaxaTerrestre;
    }

    public void setBlTaxaTerrestre(Boolean blTaxaTerrestre) {
        this.blTaxaTerrestre = blTaxaTerrestre;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroporto() {
        return this.aeroporto;
    }

	public void setAeroporto(
			com.mercurio.lms.municipios.model.Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FilialMercurioFilialCia.class)     
    public List getFilialMercurioFilialCias() {
        return this.filialMercurioFilialCias;
    }

    public void setFilialMercurioFilialCias(List filialMercurioFilialCias) {
        this.filialMercurioFilialCias = filialMercurioFilialCias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.AtendimFilialCiaAerea.class)     
    public List getAtendimFilialCiaAereas() {
        return this.atendimFilialCiaAereas;
    }

    public void setAtendimFilialCiaAereas(List atendimFilialCiaAereas) {
        this.atendimFilialCiaAereas = atendimFilialCiaAereas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("pessIdPessoa",
				getIdFilialCiaAerea()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialCiaAerea))
			return false;
        FilialCiaAerea castOther = (FilialCiaAerea) other;
		return new EqualsBuilder().append(this.getIdFilialCiaAerea(),
				castOther.getIdFilialCiaAerea()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialCiaAerea()).toHashCode();
    }

}
