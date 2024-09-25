package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class Funcionario implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private String nrMatricula;
    
    private String nmFuncionario;
    
    private String cdFuncao;
    
    private String dsFuncao;
    
    private String cdCargo;
    
    private String dsCargo;
    
    private String cdDepartamento;
    
    private String cdSetor;
    
    private String cdSecao;
    
    private String nrCpf;
    
    private Date dtNascimento;

    private DomainValue tpSexo;    

    private String nrRg;

    private String dsOrgaoEmissor;

    private Date dtEmissaoRg; 

    private String nrCnh; 

    private String tpCategoriaCnh; 

    private Date dtVencimentoHabilitacao; 

    private String dsEmail;
    
    private String dsSituacao;
    
    private String tpSituacaoFuncionario;
    
    private String dsApelido;
    
    private Filial filial;
    
    private Usuario usuario;
    
    public String toString() {
        return new ToStringBuilder(this)
				.append("nrMatricula", getNrMatricula()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Funcionario))
			return false;
        Funcionario castOther = (Funcionario) other;
		return new EqualsBuilder().append(this.getNrMatricula(),
				castOther.getNrMatricula()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getNrMatricula()).toHashCode();
    }

	public String getCdCargo() {
		return cdCargo;
	}

	public void setCdCargo(String cdCargo) {
		this.cdCargo = cdCargo;
	}

	public String getCdDepartamento() {
		return cdDepartamento;
	}

	public void setCdDepartamento(String cdDepartamento) {
		this.cdDepartamento = cdDepartamento;
	}

	public String getCdFuncao() {
		return cdFuncao;
	}

	public void setCdFuncao(String cdFuncao) {
		this.cdFuncao = cdFuncao;
	}

	public String getCdSetor() {
		return cdSetor;
	}

	public void setCdSetor(String cdSetor) {
		this.cdSetor = cdSetor;
	}

	public String getDsCargo() {
		return dsCargo;
	}

	public void setDsCargo(String dsCargo) {
		this.dsCargo = dsCargo;
	}

	public String getDsFuncao() {
		return dsFuncao;
	}

	public void setDsFuncao(String dsFuncao) {
		this.dsFuncao = dsFuncao;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public String getNmFuncionario() {
		return nmFuncionario;
	}

	public void setNmFuncionario(String nmFuncionario) {
		this.nmFuncionario = nmFuncionario;
	}

	public String getNrMatricula() {
		return nrMatricula;
	}

	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNrCpf() {
		return nrCpf;
	}

	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}

	/**
	 * @return Returns the dsEmail.
	 */
	public String getDsEmail() {
		return dsEmail;
	}

	/**
	 * @param dsEmail
	 *            The dsEmail to set.
	 */
	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	/**
	 * @return Returns the dsOrgaoEmissor.
	 */
	public String getDsOrgaoEmissor() {
		return dsOrgaoEmissor;
	}

	/**
	 * @param dsOrgaoEmissor
	 *            The dsOrgaoEmissor to set.
	 */
	public void setDsOrgaoEmissor(String dsOrgaoEmissor) {
		this.dsOrgaoEmissor = dsOrgaoEmissor;
	}

	/**
	 * @return Returns the dtEmissaoRg.
	 */
	public Date getDtEmissaoRg() {
		return dtEmissaoRg;
	}

	/**
	 * @param dtEmissaoRg
	 *            The dtEmissaoRg to set.
	 */
	public void setDtEmissaoRg(Date dtEmissaoRg) {
		this.dtEmissaoRg = dtEmissaoRg;
	}

	/**
	 * @return Returns the dtNascimento.
	 */
	public Date getDtNascimento() {
		return dtNascimento;
	}

	/**
	 * @param dtNascimento
	 *            The dtNascimento to set.
	 */
	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	/**
	 * @return Returns the dtVencimentoHabilitacao.
	 */
	public Date getDtVencimentoHabilitacao() {
		return dtVencimentoHabilitacao;
	}

	/**
	 * @param dtVencimentoHabilitacao
	 *            The dtVencimentoHabilitacao to set.
	 */
	public void setDtVencimentoHabilitacao(Date dtVencimentoHabilitacao) {
		this.dtVencimentoHabilitacao = dtVencimentoHabilitacao;
	}

	/**
	 * @return Returns the nrCnh.
	 */
	public String getNrCnh() {
		return nrCnh;
	}

	/**
	 * @param nrCnh
	 *            The nrCnh to set.
	 */
	public void setNrCnh(String nrCnh) {
		this.nrCnh = nrCnh;
	}

	/**
	 * @return Returns the nrRg.
	 */
	public String getNrRg() {
		return nrRg;
	}

	/**
	 * @param nrRg
	 *            The nrRg to set.
	 */
	public void setNrRg(String nrRg) {
		this.nrRg = nrRg;
	}

	/**
	 * @return Returns the tpCategoriaCnh.
	 */
	public String getTpCategoriaCnh() {
		return tpCategoriaCnh;
	}

	/**
	 * @param tpCategoriaCnh
	 *            The tpCategoriaCnh to set.
	 */
	public void setTpCategoriaCnh(String tpCategoriaCnh) {
		this.tpCategoriaCnh = tpCategoriaCnh;
	}

	/**
	 * @return Returns the tpSexo.
	 */
	public DomainValue getTpSexo() {
		return tpSexo;
	}

	/**
	 * @param tpSexo
	 *            The tpSexo to set.
	 */
	public void setTpSexo(DomainValue tpSexo) {
		this.tpSexo = tpSexo;
	}

	public String getDsSituacao() {
		return dsSituacao;
	}

	public void setDsSituacao(String dsSituacao) {
		this.dsSituacao = dsSituacao;
	}

	public String getTpSituacaoFuncionario() {
		return tpSituacaoFuncionario;
	}

	public void setTpSituacaoFuncionario(String tpSituacaoFuncionario) {
		this.tpSituacaoFuncionario = tpSituacaoFuncionario;
	}

	public String getCdSecao() {
		return cdSecao;
	}

	public void setCdSecao(String cdSecao) {
		this.cdSecao = cdSecao;
	}

	public String getDsApelido() {
		return dsApelido;
	}

	public void setDsApelido(String dsApelido) {
		this.dsApelido = dsApelido;
	}
}