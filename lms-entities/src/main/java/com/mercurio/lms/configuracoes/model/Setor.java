package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Setor implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSetor;

    /** persistent field */
    private VarcharI18n dsSetor;
    
    /** persistent field */
    private Short cdSetorRh;
    
    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private List protocoloTransferencias;

    /** persistent field */
    private List manifestoEntregas;

    /** persistent field */
    private List setorMotivoAberturaNcs;

    /** persistent field */
    private List mdas;

    /** persistent field */
    private List equipes;

    /** persistent field */
    private List mirsByIdSetorDestino;

    /** persistent field */
    private List mirsByIdSetorOrigem;

    public Long getIdSetor() {
        return this.idSetor;
    }

    public void setIdSetor(Long idSetor) {
        this.idSetor = idSetor;
    }

    /** default constructor */
    public Setor() {
    }
    
    public Setor(Long idSetor, VarcharI18n dsSetor) {
    	this.idSetor = idSetor;
    	this.dsSetor = dsSetor;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ProtocoloTransferencia.class)     
    public List getProtocoloTransferencias() {
        return this.protocoloTransferencias;
    }

    public void setProtocoloTransferencias(List protocoloTransferencias) {
        this.protocoloTransferencias = protocoloTransferencias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntrega.class)     
    public List getManifestoEntregas() {
        return this.manifestoEntregas;
    }

    public void setManifestoEntregas(List manifestoEntregas) {
        this.manifestoEntregas = manifestoEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.SetorMotivoAberturaNc.class)     
    public List getSetorMotivoAberturaNcs() {
        return this.setorMotivoAberturaNcs;
    }

    public void setSetorMotivoAberturaNcs(List setorMotivoAberturaNcs) {
        this.setorMotivoAberturaNcs = setorMotivoAberturaNcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Mda.class)     
    public List getMdas() {
        return this.mdas;
    }

    public void setMdas(List mdas) {
        this.mdas = mdas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.Equipe.class)     
    public List getEquipes() {
        return this.equipes;
    }

    public void setEquipes(List equipes) {
        this.equipes = equipes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.Mir.class)     
    public List getMirsByIdSetorDestino() {
        return this.mirsByIdSetorDestino;
    }

    public void setMirsByIdSetorDestino(List mirsByIdSetorDestino) {
        this.mirsByIdSetorDestino = mirsByIdSetorDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.Mir.class)     
    public List getMirsByIdSetorOrigem() {
        return this.mirsByIdSetorOrigem;
    }

    public void setMirsByIdSetorOrigem(List mirsByIdSetorOrigem) {
        this.mirsByIdSetorOrigem = mirsByIdSetorOrigem;
    }

    public Short getCdSetorRh() {
        return cdSetorRh;
    }

    public void setCdSetorRh(Short cdSetorRh) {
        this.cdSetorRh = cdSetorRh;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idSetor", getIdSetor())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Setor))
			return false;
        Setor castOther = (Setor) other;
		return new EqualsBuilder().append(this.getIdSetor(),
				castOther.getIdSetor()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSetor()).toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public VarcharI18n getDsSetor() {
		return dsSetor;
}

	public void setDsSetor(VarcharI18n dsSetor) {
		this.dsSetor = dsSetor;
	}
}
