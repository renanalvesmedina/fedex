package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Regional implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idRegional;

	/** persistent field */
	private String sgRegional;

	/** persistent field */
	private String dsRegional;

	/** persistent field */
	private YearMonthDay dtVigenciaInicial;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaFinal;

	private Usuario usuario;

	private Usuario usuarioFaturamento;

	private String nrDdd;
	private String nrTelefone;
	private String dsEmailFaturamento;

	/** persistent field */
	private List criterioAplicSimulacoes;

	/** persistent field */
	private List substAtendimentoFiliais;

	/** persistent field */
	private List regionalFiliais;

	private List usuariosRegional;

	private List regionalUsuariosPadrao;

	public Long getIdRegional() {
		return this.idRegional;
	}

	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}

	public String getSgRegional() {
		return this.sgRegional;
	}

	public void setSgRegional(String sgRegional) {
		this.sgRegional = sgRegional;
	}

	public String getDsRegional() {
		return this.dsRegional;
	}

	public void setDsRegional(String dsRegional) {
		this.dsRegional = dsRegional;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao.class)
	public List getCriterioAplicSimulacoes() {
		return this.criterioAplicSimulacoes;
	}

	public void setCriterioAplicSimulacoes(List criterioAplicSimulacoes) {
		this.criterioAplicSimulacoes = criterioAplicSimulacoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.SubstAtendimentoFilial.class)
	public List getSubstAtendimentoFiliais() {
		return this.substAtendimentoFiliais;
	}

	public void setSubstAtendimentoFiliais(List substAtendimentoFiliais) {
		this.substAtendimentoFiliais = substAtendimentoFiliais;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RegionalFilial.class)
	public List getRegionalFiliais() {
		return this.regionalFiliais;
	}

	public void setRegionalFiliais(List regionalFiliais) {
		this.regionalFiliais = regionalFiliais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idRegional", getIdRegional())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Regional))
			return false;
		Regional castOther = (Regional) other;
		return new EqualsBuilder().append(this.getIdRegional(),
				castOther.getIdRegional()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdRegional()).toHashCode();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSiglaDescricao() {
		if (this.getSgRegional() != null && this.getDsRegional() != null) {
			return this.getSgRegional() + " - " + this.getDsRegional();
		} else {
			return null;
		}
	}

	public List getUsuariosRegional() {
		return usuariosRegional;
	}

	public void setUsuariosRegional(List usuariosRegional) {
		this.usuariosRegional = usuariosRegional;
	}

	public List getRegionalUsuariosPadrao() {
		return regionalUsuariosPadrao;
	}

	public void setRegionalUsuariosPadrao(List regionalUsuariosPadrao) {
		this.regionalUsuariosPadrao = regionalUsuariosPadrao;
	}

	public void setUsuarioFaturamento(Usuario usuarioFaturamento) {
		this.usuarioFaturamento = usuarioFaturamento;
	}

	public Usuario getUsuarioFaturamento() {
		return usuarioFaturamento;
	}

	public void setNrTelefone(String telefoneRegional) {
		this.nrTelefone = telefoneRegional;
	}

	public void setNrDdd(String dddRegional) {
		this.nrDdd = dddRegional;
	}

	public String getNrDdd() {
		return nrDdd;
	}

	public String getNrTelefone() {
		return nrTelefone;
	}
	
	public void setDsEmailFaturamento(String dsEmailFaturamento) {
		this.dsEmailFaturamento = dsEmailFaturamento;
	}
	public String getDsEmailFaturamento() {
		return dsEmailFaturamento;
	}
}