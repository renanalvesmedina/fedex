package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class ChecklistMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idChecklistMeioTransporte;
    
    private Long nrChecklist;

    /** persistent field */
    private YearMonthDay dtRealizacao;

    /** persistent field */
    private Boolean blAprovado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdSegundoMotorista;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdPrimeiroMotorista;
    
    private Filial filial;

    /** persistent field */
    private List respostaChecklists;

    public Long getIdChecklistMeioTransporte() {
        return this.idChecklistMeioTransporte;
    }

    public void setIdChecklistMeioTransporte(Long idChecklistMeioTransporte) {
        this.idChecklistMeioTransporte = idChecklistMeioTransporte;
    }

    public YearMonthDay getDtRealizacao() {
        return this.dtRealizacao;
    }

    public void setDtRealizacao(YearMonthDay dtRealizacao) {
        this.dtRealizacao = dtRealizacao;
    }

    public Boolean getBlAprovado() {
        return this.blAprovado;
    }

    public void setBlAprovado(Boolean blAprovado) {
        this.blAprovado = blAprovado;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
        return this.solicitacaoContratacao;
    }

	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
        this.solicitacaoContratacao = solicitacaoContratacao;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoaByIdSegundoMotorista() {
        return this.pessoaByIdSegundoMotorista;
    }

	public void setPessoaByIdSegundoMotorista(
			com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdSegundoMotorista) {
        this.pessoaByIdSegundoMotorista = pessoaByIdSegundoMotorista;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoaByIdPrimeiroMotorista() {
        return this.pessoaByIdPrimeiroMotorista;
    }

	public void setPessoaByIdPrimeiroMotorista(
			com.mercurio.lms.configuracoes.model.Pessoa pessoaByIdPrimeiroMotorista) {
        this.pessoaByIdPrimeiroMotorista = pessoaByIdPrimeiroMotorista;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.RespostaChecklist.class)     
    public List getRespostaChecklists() {
        return this.respostaChecklists;
    }

    public void setRespostaChecklists(List respostaChecklists) {
        this.respostaChecklists = respostaChecklists;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idChecklistMeioTransporte",
				getIdChecklistMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ChecklistMeioTransporte))
			return false;
        ChecklistMeioTransporte castOther = (ChecklistMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdChecklistMeioTransporte(),
				castOther.getIdChecklistMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdChecklistMeioTransporte())
            .toHashCode();
    }

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Long getNrChecklist() {
		return nrChecklist;
	}

	public void setNrChecklist(Long nrChecklist) {
		this.nrChecklist = nrChecklist;
	}

}
