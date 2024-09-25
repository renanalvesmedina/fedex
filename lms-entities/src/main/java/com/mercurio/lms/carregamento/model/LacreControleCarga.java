package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class LacreControleCarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLacreControleCarga;

    /** persistent field */
    private Integer nrLacre;

    private String nrLacres;
    
    /** persistent field */
    private DomainValue tpStatusLacre;

    /** nullable persistent field */
    private String dsLocalInclusao;

    /** nullable persistent field */
    private String dsLocalConferencia;

    /** nullable persistent field */
    private String obInclusaoLacre;

    /** nullable persistent field */
    private String obConferenciaLacre;
    
    /** persistent field */
    private DateTime dhInclusao;

    /** nullable persistent field */
    private DateTime dhAlteracao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncInclusao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialInclusao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialAlteraStatus;
    
    private Integer versao;

    public Long getIdLacreControleCarga() {
        return this.idLacreControleCarga;
    }

    public void setIdLacreControleCarga(Long idLacreControleCarga) {
        this.idLacreControleCarga = idLacreControleCarga;
    }

    public Integer getNrLacre() {
        return this.nrLacre;
    }

    public void setNrLacre(Integer nrLacre) {
        this.nrLacre = nrLacre;
    }

    public DomainValue getTpStatusLacre() {
        return this.tpStatusLacre;
    }

    public void setTpStatusLacre(DomainValue tpStatusLacre) {
        this.tpStatusLacre = tpStatusLacre;
    }

    public String getDsLocalInclusao() {
        return this.dsLocalInclusao;
    }

    public void setDsLocalInclusao(String dsLocalInclusao) {
        this.dsLocalInclusao = dsLocalInclusao;
    }

    public String getDsLocalConferencia() {
        return this.dsLocalConferencia;
    }

    public void setDsLocalConferencia(String dsLocalConferencia) {
        this.dsLocalConferencia = dsLocalConferencia;
    }

    public String getObInclusaoLacre() {
        return this.obInclusaoLacre;
    }

    public void setObInclusaoLacre(String obInclusaoLacre) {
        this.obInclusaoLacre = obInclusaoLacre;
    }

    public String getObConferenciaLacre() {
        return this.obConferenciaLacre;
    }

    public void setObConferenciaLacre(String obConferenciaLacre) {
        this.obConferenciaLacre = obConferenciaLacre;
    }

    public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncInclusao() {
        return this.usuarioByIdFuncInclusao;
    }

	public void setUsuarioByIdFuncInclusao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncInclusao) {
        this.usuarioByIdFuncInclusao = usuarioByIdFuncInclusao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncAlteraStatus() {
        return this.usuarioByIdFuncAlteraStatus;
    }

	public void setUsuarioByIdFuncAlteraStatus(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus) {
        this.usuarioByIdFuncAlteraStatus = usuarioByIdFuncAlteraStatus;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialInclusao() {
        return this.filialByIdFilialInclusao;
    }

	public void setFilialByIdFilialInclusao(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialInclusao) {
        this.filialByIdFilialInclusao = filialByIdFilialInclusao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialAlteraStatus() {
        return this.filialByIdFilialAlteraStatus;
    }

	public void setFilialByIdFilialAlteraStatus(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialAlteraStatus) {
        this.filialByIdFilialAlteraStatus = filialByIdFilialAlteraStatus;
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}    
    
    public String toString() {
		return new ToStringBuilder(this).append("idLacreControleCarga",
				getIdLacreControleCarga()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LacreControleCarga))
			return false;
        LacreControleCarga castOther = (LacreControleCarga) other;
		return new EqualsBuilder().append(this.getIdLacreControleCarga(),
				castOther.getIdLacreControleCarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLacreControleCarga())
            .toHashCode();
    }

	public String getNrLacres() {
		return nrLacres;
}

	public void setNrLacres(String nrLacres) {
		this.nrLacres = nrLacres;
	}

}
