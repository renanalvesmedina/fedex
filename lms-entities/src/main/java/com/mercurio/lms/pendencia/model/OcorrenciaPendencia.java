package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaPendencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaPendencia;

    /** persistent field */
    private Short cdOcorrencia;

    /** persistent field */
    private VarcharI18n dsOcorrencia;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private DomainValue tpOcorrencia;

    /** persistent field */
    private DomainValue tpPermissaoUnidade;

    /** persistent field */
    private Boolean blDescontaDpe;

    /** persistent field */
    private Boolean blExigeRnc;

    /** persistent field */
    private Boolean blPermiteOcorParaManif;

    /** persistent field */
    private Boolean blApreensao;

    /** persistent field */
    private com.mercurio.lms.sim.model.Evento evento;

    /** persistent field */
    private List liberacaoBloqueiosByIdOcorrenciaLiberacao;

    /** persistent field */
    private List liberacaoBloqueiosByIdOcorrenciaBloqueio;

    /** persistent field */
    private List ocorrenciaDoctoServicosByIdOcorBloqueio;

    /** persistent field */
    private List ocorrenciaDoctoServicosByIdOcorLiberacao;

    public OcorrenciaPendencia() {	
    }
    
    public OcorrenciaPendencia(Long idOcorrenciaPendencia) {
    	this.idOcorrenciaPendencia = idOcorrenciaPendencia;
    }
    
    public Long getIdOcorrenciaPendencia() {
        return this.idOcorrenciaPendencia;
    }

    public void setIdOcorrenciaPendencia(Long idOcorrenciaPendencia) {
        this.idOcorrenciaPendencia = idOcorrenciaPendencia;
    }

    public Short getCdOcorrencia() {
        return this.cdOcorrencia;
    }

    public void setCdOcorrencia(Short cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    public VarcharI18n getDsOcorrencia() {
		return dsOcorrencia;
    }

	public void setDsOcorrencia(VarcharI18n dsOcorrencia) {
        this.dsOcorrencia = dsOcorrencia;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpOcorrencia() {
        return this.tpOcorrencia;
    }

    public void setTpOcorrencia(DomainValue tpOcorrencia) {
        this.tpOcorrencia = tpOcorrencia;
    }

    public DomainValue getTpPermissaoUnidade() {
        return this.tpPermissaoUnidade;
    }

    public void setTpPermissaoUnidade(DomainValue tpPermissaoUnidade) {
        this.tpPermissaoUnidade = tpPermissaoUnidade;
    }

    public Boolean getBlDescontaDpe() {
        return this.blDescontaDpe;
    }

    public void setBlDescontaDpe(Boolean blDescontaDpe) {
        this.blDescontaDpe = blDescontaDpe;
    }

    public Boolean getBlExigeRnc() {
        return this.blExigeRnc;
    }

    public void setBlExigeRnc(Boolean blExigeRnc) {
        this.blExigeRnc = blExigeRnc;
    }

    public Boolean getBlPermiteOcorParaManif() {
        return this.blPermiteOcorParaManif;
    }

    public void setBlPermiteOcorParaManif(Boolean blPermiteOcorParaManif) {
        this.blPermiteOcorParaManif = blPermiteOcorParaManif;
    }

    public Boolean getBlApreensao() {
        return this.blApreensao;
    }

    public void setBlApreensao(Boolean blApreensao) {
        this.blApreensao = blApreensao;
    }

    public com.mercurio.lms.sim.model.Evento getEvento() {
        return this.evento;
    }

    public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
        this.evento = evento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.LiberacaoBloqueio.class)     
    public List getLiberacaoBloqueiosByIdOcorrenciaLiberacao() {
        return this.liberacaoBloqueiosByIdOcorrenciaLiberacao;
    }

	public void setLiberacaoBloqueiosByIdOcorrenciaLiberacao(
			List liberacaoBloqueiosByIdOcorrenciaLiberacao) {
        this.liberacaoBloqueiosByIdOcorrenciaLiberacao = liberacaoBloqueiosByIdOcorrenciaLiberacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.LiberacaoBloqueio.class)     
    public List getLiberacaoBloqueiosByIdOcorrenciaBloqueio() {
        return this.liberacaoBloqueiosByIdOcorrenciaBloqueio;
    }

	public void setLiberacaoBloqueiosByIdOcorrenciaBloqueio(
			List liberacaoBloqueiosByIdOcorrenciaBloqueio) {
        this.liberacaoBloqueiosByIdOcorrenciaBloqueio = liberacaoBloqueiosByIdOcorrenciaBloqueio;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico.class)     
    public List getOcorrenciaDoctoServicosByIdOcorBloqueio() {
        return this.ocorrenciaDoctoServicosByIdOcorBloqueio;
    }

	public void setOcorrenciaDoctoServicosByIdOcorBloqueio(
			List ocorrenciaDoctoServicosByIdOcorBloqueio) {
        this.ocorrenciaDoctoServicosByIdOcorBloqueio = ocorrenciaDoctoServicosByIdOcorBloqueio;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico.class)     
    public List getOcorrenciaDoctoServicosByIdOcorLiberacao() {
        return this.ocorrenciaDoctoServicosByIdOcorLiberacao;
    }

	public void setOcorrenciaDoctoServicosByIdOcorLiberacao(
			List ocorrenciaDoctoServicosByIdOcorLiberacao) {
        this.ocorrenciaDoctoServicosByIdOcorLiberacao = ocorrenciaDoctoServicosByIdOcorLiberacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaPendencia",
				getIdOcorrenciaPendencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaPendencia))
			return false;
        OcorrenciaPendencia castOther = (OcorrenciaPendencia) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaPendencia(),
				castOther.getIdOcorrenciaPendencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaPendencia())
            .toHashCode();
    }

}
