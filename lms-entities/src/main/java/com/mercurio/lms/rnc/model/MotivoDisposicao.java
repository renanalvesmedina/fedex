package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoDisposicao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoDisposicao;

    /** persistent field */
    private VarcharI18n dsMotivo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Boolean blReverteRespFilAbertura;

    /** persistent field */
    private Boolean blSomenteAutomatico;

    /** persistent field */
    private List motAberturaMotDisposicoes;

    /** persistent field */
    private List disposicoes;

    public Long getIdMotivoDisposicao() {
        return this.idMotivoDisposicao;
    }

    public void setIdMotivoDisposicao(Long idMotivoDisposicao) {
        this.idMotivoDisposicao = idMotivoDisposicao;
    }

    public VarcharI18n getDsMotivo() {
		return dsMotivo;
    }

	public void setDsMotivo(VarcharI18n dsMotivo) {
        this.dsMotivo = dsMotivo;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Boolean getBlReverteRespFilAbertura() {
        return this.blReverteRespFilAbertura;
    }

    public void setBlReverteRespFilAbertura(Boolean blReverteRespFilAbertura) {
        this.blReverteRespFilAbertura = blReverteRespFilAbertura;
    }

    public Boolean getBlSomenteAutomatico() {
        return this.blSomenteAutomatico;
    }

    public void setBlSomenteAutomatico(Boolean blSomenteAutomatico) {
        this.blSomenteAutomatico = blSomenteAutomatico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.MotAberturaMotDisposicao.class)     
    public List getMotAberturaMotDisposicoes() {
        return this.motAberturaMotDisposicoes;
    }

    public void setMotAberturaMotDisposicoes(List motAberturaMotDisposicoes) {
        this.motAberturaMotDisposicoes = motAberturaMotDisposicoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.Disposicao.class)     
    public List getDisposicoes() {
        return this.disposicoes;
    }

    public void setDisposicoes(List disposicoes) {
        this.disposicoes = disposicoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoDisposicao",
				getIdMotivoDisposicao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoDisposicao))
			return false;
        MotivoDisposicao castOther = (MotivoDisposicao) other;
		return new EqualsBuilder().append(this.getIdMotivoDisposicao(),
				castOther.getIdMotivoDisposicao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoDisposicao())
            .toHashCode();
    }

}
