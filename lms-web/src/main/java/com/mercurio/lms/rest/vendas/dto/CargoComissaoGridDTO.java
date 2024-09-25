package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;
 
public class CargoComissaoGridDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
	
	private Long id;
	private String nrMatricula;
	private String nmFuncionario;
	private String nrCpf;
	private String tpCargo;

	private String regional;
	private String filial;
	
	private String dtInclusaoComissao;
	private String dtAdmissao;
	private String dtDemissao;
	
	private String situacao;
	
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getNrMatricula() {
		return nrMatricula;
	}
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	public String getNmFuncionario() {
		return nmFuncionario;
	}
	public void setNmFuncionario(String nmFuncionario) {
		this.nmFuncionario = nmFuncionario;
	}
	public String getNrCpf() {
		return nrCpf;
	}
	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}
	public String getTpCargo() {
		return tpCargo;
	}
	public void setTpCargo(String tpCargo) {
		this.tpCargo = tpCargo;
	}
	public String getRegional() {
		return regional;
	}
	public void setRegional(String regional) {
		this.regional = regional;
	}
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getDtInclusaoComissao() {
		return dtInclusaoComissao;
	}
	public void setDtInclusaoComissao(String dtInclusaoComissao) {
		this.dtInclusaoComissao = dtInclusaoComissao;
	}
	public String getDtAdmissao() {
		return dtAdmissao;
	}
	public void setDtAdmissao(String dtAdmissao) {
		this.dtAdmissao = dtAdmissao;
	}
	public String getDtDemissao() {
		return dtDemissao;
	}
	public void setDtDemissao(String dtDemissao) {
		this.dtDemissao = dtDemissao;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	
} 
