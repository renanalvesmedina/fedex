package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.municipios.model.Empresa;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaPreco implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient Long nrOrdem;
	
	/** identifier field */
	private Long idParcelaPreco;

	/** persistent field */
	private String cdParcelaPreco;

	/** persistent field */
	private VarcharI18n nmParcelaPreco;

	/** persistent field */
	private VarcharI18n dsParcelaPreco;

	/** persistent field */
	private Boolean blIncideIcms;

	/** persistent field */
	private DomainValue tpIndicadorCalculo;

	/** persistent field */
	private DomainValue tpParcelaPreco;

	/** persistent field */
	private DomainValue tpPrecificacao;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private Boolean blEmbuteParcela;

	/** persistent field */
	private ServicoAdicional servicoAdicional;

	/** persistent field */
	private UnidadeMedida unidadeMedida;

	/** persistent field */
	private Empresa empresa;

	/** default constructor */
	public ParcelaPreco() {
	}
	
	public ParcelaPreco(Long id) {
		this.idParcelaPreco = id;
	}

	public ParcelaPreco(Long id,String cd) {
		this.idParcelaPreco = id;
		this.cdParcelaPreco = cd;
	}

	public Long getIdParcelaPreco() {
		return this.idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public String getCdParcelaPreco() {
		return cdParcelaPreco;
	}

	public void setCdParcelaPreco(String cdParcelaPreco) {
		this.cdParcelaPreco = cdParcelaPreco;
	}

	public VarcharI18n getNmParcelaPreco() {
		return nmParcelaPreco;
	}

	public void setNmParcelaPreco(VarcharI18n nmParcelaPreco) {
		this.nmParcelaPreco = nmParcelaPreco;
	}

	public VarcharI18n getDsParcelaPreco() {
		return dsParcelaPreco;
	}

	public void setDsParcelaPreco(VarcharI18n dsParcelaPreco) {
		this.dsParcelaPreco = dsParcelaPreco;
	}

	public Boolean getBlIncideIcms() {
		return this.blIncideIcms;
	}

	public void setBlIncideIcms(Boolean blIncideIcms) {
		this.blIncideIcms = blIncideIcms;
	}

	public DomainValue getTpIndicadorCalculo() {
		return this.tpIndicadorCalculo;
	}

	public void setTpIndicadorCalculo(DomainValue tpIndicadorCalculo) {
		this.tpIndicadorCalculo = tpIndicadorCalculo;
	}

	public DomainValue getTpParcelaPreco() {
		return this.tpParcelaPreco;
	}

	public void setTpParcelaPreco(DomainValue tpParcelaPreco) {
		this.tpParcelaPreco = tpParcelaPreco;
	}

	public DomainValue getTpPrecificacao() {
		return this.tpPrecificacao;
	}

	public void setTpPrecificacao(DomainValue tpPrecificacao) {
		this.tpPrecificacao = tpPrecificacao;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Boolean getBlEmbuteParcela() {
		return this.blEmbuteParcela;
	}

	public void setBlEmbuteParcela(Boolean blEmbuteParcela) {
		this.blEmbuteParcela = blEmbuteParcela;
	}

	public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
		return this.servicoAdicional;
	}

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
		this.servicoAdicional = servicoAdicional;
	}

	public com.mercurio.lms.tabelaprecos.model.UnidadeMedida getUnidadeMedida() {
		return this.unidadeMedida;
	}

	public void setUnidadeMedida(
			com.mercurio.lms.tabelaprecos.model.UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
		this.empresa = empresa;
	}

	public Long getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Long nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idParcelaPreco",
				getIdParcelaPreco()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaPreco))
			return false;
		ParcelaPreco castOther = (ParcelaPreco) other;
		return new EqualsBuilder().append(this.getIdParcelaPreco(),
				castOther.getIdParcelaPreco()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaPreco()).toHashCode();
	}

}
