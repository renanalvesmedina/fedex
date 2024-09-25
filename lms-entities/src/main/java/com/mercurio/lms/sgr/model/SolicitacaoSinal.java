package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.OperadoraMct;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.municipios.model.Filial;

public class SolicitacaoSinal implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long idSolicitacaoSinal;
    private DateTime dhGeracao;
    private Integer nrSolicitacaoSinal;
    private Long nrRastreador;
    private String nrTelefoneEmpresa;
    private String nmEmpresaAnterior;
    private String nmResponsavelEmpresa;
    private Boolean blPertProjCaminhoneiro;
    private String obSolicitacaoSinal;
    private DomainValue tpStatusSolicitacao;
    private ControleCarga controleCarga;
    private MeioTransporte meioTransporte;
    private MeioTransporte meioTransporteByIdSemiReboque;    
    private OperadoraMct operadoraMct;
    private Motorista motorista;
    private Proprietario proprietario;
    private Filial filial;

    public Long getIdSolicitacaoSinal() {
        return idSolicitacaoSinal;
    }

    public void setIdSolicitacaoSinal(Long idSolicitacaoSinal) {
        this.idSolicitacaoSinal = idSolicitacaoSinal;
    }

    public DateTime getDhGeracao() {
        return dhGeracao;
    }

    public void setDhGeracao(DateTime dhGeracao) {
        this.dhGeracao = dhGeracao;
    }

    public Integer getNrSolicitacaoSinal() {
        return nrSolicitacaoSinal;
    }

    public void setNrSolicitacaoSinal(Integer nrSolicitacaoSinal) {
        this.nrSolicitacaoSinal = nrSolicitacaoSinal;
    }

    public Long getNrRastreador() {
        return nrRastreador;
    }

    public void setNrRastreador(Long nrRastreador) {
        this.nrRastreador = nrRastreador;
    }

    public String getNrTelefoneEmpresa() {
        return nrTelefoneEmpresa;
    }

    public void setNrTelefoneEmpresa(String nrTelefoneEmpresa) {
        this.nrTelefoneEmpresa = nrTelefoneEmpresa;
    }

    public String getNmEmpresaAnterior() {
        return nmEmpresaAnterior;
    }

    public void setNmEmpresaAnterior(String nmEmpresaAnterior) {
        this.nmEmpresaAnterior = nmEmpresaAnterior;
    }

    public String getNmResponsavelEmpresa() {
        return nmResponsavelEmpresa;
    }

    public void setNmResponsavelEmpresa(String nmResponsavelEmpresa) {
        this.nmResponsavelEmpresa = nmResponsavelEmpresa;
    }

    public Boolean getBlPertProjCaminhoneiro() {
        return blPertProjCaminhoneiro;
    }

    public void setBlPertProjCaminhoneiro(Boolean blPertProjCaminhoneiro) {
        this.blPertProjCaminhoneiro = blPertProjCaminhoneiro;
    }

    public String getObSolicitacaoSinal() {
        return obSolicitacaoSinal;
    }

    public void setObSolicitacaoSinal(String obSolicitacaoSinal) {
        this.obSolicitacaoSinal = obSolicitacaoSinal;
    }

    public DomainValue getTpStatusSolicitacao() {
		return tpStatusSolicitacao;
	}

	public void setTpStatusSolicitacao(DomainValue tpStatusSolicitacao) {
		this.tpStatusSolicitacao = tpStatusSolicitacao;
	}

	public ControleCarga getControleCarga() {
        return controleCarga;
    }

	public void setControleCarga(
			ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public MeioTransporte getMeioTransporte() {
        return meioTransporte;
    }

	public void setMeioTransporte(
			MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }
    
	public MeioTransporte getMeioTransporteByIdSemiReboque() {
		return meioTransporteByIdSemiReboque;
	}

	public void setMeioTransporteByIdSemiReboque(
			MeioTransporte meioTransporteByIdSemiReboque) {
		this.meioTransporteByIdSemiReboque = meioTransporteByIdSemiReboque;
	}    

    public OperadoraMct getOperadoraMct() {
        return operadoraMct;
    }

	public void setOperadoraMct(
			OperadoraMct operadoraMct) {
        this.operadoraMct = operadoraMct;
    }

    public Motorista getMotorista() {
        return motorista;
    }

	public void setMotorista(
			Motorista motorista) {
        this.motorista = motorista;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

	public void setProprietario(
			Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicitacaoSinal)
				.toString();
    }

    public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicitacaoSinal)) {
			return false;
		}
		SolicitacaoSinal cast = (SolicitacaoSinal) other;
		return new EqualsBuilder()
				.append(idSolicitacaoSinal, cast.idSolicitacaoSinal)
				.isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicitacaoSinal)
				.toHashCode();
    }

}
