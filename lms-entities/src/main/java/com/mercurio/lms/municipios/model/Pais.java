package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class Pais implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idPais;

	/** persistent field */
	private String sgPais;

	/** persistent field */
	private VarcharI18n nmPais;

	/** persistent field */
	private DomainValue tpSituacao;

	/** nullable persistent field */
	private String sgResumida;

	/** nullable persistent field */
	private Integer cdIso;

	/** persistent field */
	private Boolean blCepOpcional;

	/** persistent field */
	private Boolean blCepDuplicado;

	/** persistent field */
	private Boolean blCepAlfanumerico;

	/** persistent field */
	private com.mercurio.lms.municipios.model.Zona zona;

	/** persistent field */
	private DomainValue tpBuscaEndereco;

	/** persistent field */
	private Long nrBacen;

	/** persistent field */
	private List rotaPrecosByIdPaisOrigem;

	/** persistent field */
	private List rotaPrecosByIdPaisDestino;

	/** persistent field */
	private List diaSemanas;

	/** persistent field */
	private List paisEnquadramentos;

	/** persistent field */
	private List restricaoColetas;

	/** persistent field */
	private List indicadorFinanceiros;

	/** persistent field */
	private List permissoEmpresaPaisByIdPaisDestino;

	/** persistent field */
	private List permissoEmpresaPaisByIdPaisOrigem;

	/** persistent field */
	private List rodovias;

	/** persistent field */
	private List meioTranspRodoPermissos;

	/** persistent field */
	private List parametroClientesByIdPaisDestino;

	/** persistent field */
	private List parametroClientesByIdPaisOrigem;

	/** persistent field */
	private List horarioCorteClientes;

	/** persistent field */
	private List prazoEntregaClientesByIdPaisDestino;

	/** persistent field */
	private List prazoEntregaClientesByIdPaisOrigem;

	/** persistent field */
	private List feriados;

	/** persistent field */
	private List bancos;

	/** persistent field */
	private List unidadeFederativas;

	/** persistent field */
	private List aliquotaIvas;

	/** persistent field */
	private List moedaPais;

	/** default constructor */
	public Pais() {
	}

	public Pais(Long idPais, String sgPais, VarcharI18n nmPais,
			DomainValue tpSituacao) {
		this.idPais = idPais;
		this.sgPais = sgPais;
		this.nmPais = nmPais;
		this.tpSituacao = tpSituacao;
	}

	public Pais(VarcharI18n nome) {
		this.nmPais = nome;
	}

	public Long getIdPais() {
		return this.idPais;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public String getSgPais() {
		return this.sgPais;
	}

	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
	}

	public VarcharI18n getNmPais() {
		return nmPais;
	}

	public void setNmPais(VarcharI18n nmPais) {
		this.nmPais = nmPais;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String getSgResumida() {
		return this.sgResumida;
	}

	public void setSgResumida(String sgResumida) {
		this.sgResumida = sgResumida;
	}

	public Integer getCdIso() {
		return this.cdIso;
	}

	public void setCdIso(Integer cdIso) {
		this.cdIso = cdIso;
	}

	public Boolean getBlCepOpcional() {
		return blCepOpcional;
	}

	public void setBlCepOpcional(Boolean blCepOpcional) {
		this.blCepOpcional = blCepOpcional;
	}

	public Boolean getBlCepDuplicado() {
		return blCepDuplicado;
	}

	public void setBlCepDuplicado(Boolean blCepDuplicado) {
		this.blCepDuplicado = blCepDuplicado;
	}

	public Boolean getBlCepAlfanumerico() {
		return blCepAlfanumerico;
	}

	public void setBlCepAlfanumerico(Boolean blCepAlfanumerico) {
		this.blCepAlfanumerico = blCepAlfanumerico;
	}

	public com.mercurio.lms.municipios.model.Zona getZona() {
		return this.zona;
	}

	public void setZona(com.mercurio.lms.municipios.model.Zona zona) {
		this.zona = zona;
	}

	public DomainValue getTpBuscaEndereco() {
		return tpBuscaEndereco;
	}

	public void setTpBuscaEndereco(DomainValue tpBuscaEndereco) {
		this.tpBuscaEndereco = tpBuscaEndereco;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class) 
	public List getRotaPrecosByIdPaisOrigem() {
		return this.rotaPrecosByIdPaisOrigem;
	}

	public void setRotaPrecosByIdPaisOrigem(List rotaPrecosByIdPaisOrigem) {
		this.rotaPrecosByIdPaisOrigem = rotaPrecosByIdPaisOrigem;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class) 
	public List getRotaPrecosByIdPaisDestino() {
		return this.rotaPrecosByIdPaisDestino;
	}

	public void setRotaPrecosByIdPaisDestino(List rotaPrecosByIdPaisDestino) {
		this.rotaPrecosByIdPaisDestino = rotaPrecosByIdPaisDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.DiaSemana.class) 
	public List getDiaSemanas() {
		return this.diaSemanas;
	}

	public void setDiaSemanas(List diaSemanas) {
		this.diaSemanas = diaSemanas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sgr.model.PaisEnquadramento.class) 
	public List getPaisEnquadramentos() {
		return this.paisEnquadramentos;
	}

	public void setPaisEnquadramentos(List paisEnquadramentos) {
		this.paisEnquadramentos = paisEnquadramentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.RestricaoColeta.class) 
	public List getRestricaoColetas() {
		return this.restricaoColetas;
	}

	public void setRestricaoColetas(List restricaoColetas) {
		this.restricaoColetas = restricaoColetas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.IndicadorFinanceiro.class) 
	public List getIndicadorFinanceiros() {
		return this.indicadorFinanceiros;
	}

	public void setIndicadorFinanceiros(List indicadorFinanceiros) {
		this.indicadorFinanceiros = indicadorFinanceiros;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PermissoEmpresaPais.class) 
	public List getPermissoEmpresaPaisByIdPaisDestino() {
		return this.permissoEmpresaPaisByIdPaisDestino;
	}

	public void setPermissoEmpresaPaisByIdPaisDestino(
			List permissoEmpresaPaisByIdPaisDestino) {
		this.permissoEmpresaPaisByIdPaisDestino = permissoEmpresaPaisByIdPaisDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PermissoEmpresaPais.class) 
	public List getPermissoEmpresaPaisByIdPaisOrigem() {
		return this.permissoEmpresaPaisByIdPaisOrigem;
	}

	public void setPermissoEmpresaPaisByIdPaisOrigem(
			List permissoEmpresaPaisByIdPaisOrigem) {
		this.permissoEmpresaPaisByIdPaisOrigem = permissoEmpresaPaisByIdPaisOrigem;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Rodovia.class) 
	public List getRodovias() {
		return this.rodovias;
	}

	public void setRodovias(List rodovias) {
		this.rodovias = rodovias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoPermisso.class) 
	public List getMeioTranspRodoPermissos() {
		return this.meioTranspRodoPermissos;
	}

	public void setMeioTranspRodoPermissos(List meioTranspRodoPermissos) {
		this.meioTranspRodoPermissos = meioTranspRodoPermissos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class) 
	public List getParametroClientesByIdPaisDestino() {
		return this.parametroClientesByIdPaisDestino;
	}

	public void setParametroClientesByIdPaisDestino(
			List parametroClientesByIdPaisDestino) {
		this.parametroClientesByIdPaisDestino = parametroClientesByIdPaisDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class) 
	public List getParametroClientesByIdPaisOrigem() {
		return this.parametroClientesByIdPaisOrigem;
	}

	public void setParametroClientesByIdPaisOrigem(
			List parametroClientesByIdPaisOrigem) {
		this.parametroClientesByIdPaisOrigem = parametroClientesByIdPaisOrigem;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HorarioCorteCliente.class) 
	public List getHorarioCorteClientes() {
		return this.horarioCorteClientes;
	}

	public void setHorarioCorteClientes(List horarioCorteClientes) {
		this.horarioCorteClientes = horarioCorteClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class) 
	public List getPrazoEntregaClientesByIdPaisDestino() {
		return this.prazoEntregaClientesByIdPaisDestino;
	}

	public void setPrazoEntregaClientesByIdPaisDestino(
			List prazoEntregaClientesByIdPaisDestino) {
		this.prazoEntregaClientesByIdPaisDestino = prazoEntregaClientesByIdPaisDestino;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class) 
	public List getPrazoEntregaClientesByIdPaisOrigem() {
		return this.prazoEntregaClientesByIdPaisOrigem;
	}

	public void setPrazoEntregaClientesByIdPaisOrigem(
			List prazoEntregaClientesByIdPaisOrigem) {
		this.prazoEntregaClientesByIdPaisOrigem = prazoEntregaClientesByIdPaisOrigem;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Feriado.class) 
	public List getFeriados() {
		return this.feriados;
	}

	public void setFeriados(List feriados) {
		this.feriados = feriados;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Banco.class) 
	public List getBancos() {
		return this.bancos;
	}

	public void setBancos(List bancos) {
		this.bancos = bancos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.UnidadeFederativa.class) 
	public List getUnidadeFederativas() {
		return this.unidadeFederativas;
	}

	public void setUnidadeFederativas(List unidadeFederativas) {
		this.unidadeFederativas = unidadeFederativas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaIva.class) 
	public List getAliquotaIvas() {
		return this.aliquotaIvas;
	}

	public void setAliquotaIvas(List aliquotaIvas) {
		this.aliquotaIvas = aliquotaIvas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.MoedaPais.class) 
	public List getMoedaPais() {
		return this.moedaPais;
	}

	public void setMoedaPais(List moedaPais) {
		this.moedaPais = moedaPais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPais", getIdPais())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Pais))
			return false;
		Pais castOther = (Pais) other;
		return new EqualsBuilder().append(this.getIdPais(),
				castOther.getIdPais()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdPais()).toHashCode();
	}

	public Long getNrBacen() {
		return nrBacen;
}

	public void setNrBacen(Long nrBacen) {
		this.nrBacen = nrBacen;
	}

}
