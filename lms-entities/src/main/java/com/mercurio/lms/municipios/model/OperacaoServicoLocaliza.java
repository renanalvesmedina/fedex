package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class OperacaoServicoLocaliza implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idOperacaoServicoLocaliza;

	/** persistent field */
	private DomainValue tpOperacao;

	/** persistent field */
	private Boolean blAtendimentoGeral;

	/** persistent field */
	private Boolean blCobraTaxaFluvial;

	/** persistent field */
	private Boolean blAceitaFreteFob;

	/** persistent field */
	private Boolean blDomingo;

	/** persistent field */
	private Boolean blSegunda;

	/** persistent field */
	private Boolean blTerca;

	/** persistent field */
	private Boolean blQuarta;

	/** persistent field */
	private Boolean blQuinta;

	/** persistent field */
	private Boolean blSexta;

	/** persistent field */
	private Boolean blSabado;

	/** persistent field */
	private YearMonthDay dtVigenciaInicial;

	/** nullable persistent field */
	private Long nrTempoColeta;

	/** nullable persistent field */
	private Long nrTempoEntrega;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaFinal;

	/** persistent field */
	private com.mercurio.lms.configuracoes.model.Servico servico;

	/** persistent field */
	private MunicipioFilial municipioFilial;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioFob;

	/** persistent field */
	private List<AtendimentoCliente> atendimentoClientes;

	public Long getIdOperacaoServicoLocaliza() {
		return this.idOperacaoServicoLocaliza;
	}

	public void setIdOperacaoServicoLocaliza(Long idOperacaoServicoLocaliza) {
		this.idOperacaoServicoLocaliza = idOperacaoServicoLocaliza;
	}

	public DomainValue getTpOperacao() {
		return this.tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public Boolean getBlAtendimentoGeral() {
		return this.blAtendimentoGeral;
	}

	public void setBlAtendimentoGeral(Boolean blAtendimentoGeral) {
		this.blAtendimentoGeral = blAtendimentoGeral;
	}

	public Boolean getBlCobraTaxaFluvial() {
		return this.blCobraTaxaFluvial;
	}

	public void setBlCobraTaxaFluvial(Boolean blCobraTaxaFluvial) {
		this.blCobraTaxaFluvial = blCobraTaxaFluvial;
	}

	public Boolean getBlAceitaFreteFob() {
		return this.blAceitaFreteFob;
	}

	public void setBlAceitaFreteFob(Boolean blAceitaFreteFob) {
		this.blAceitaFreteFob = blAceitaFreteFob;
	}

	public Boolean getBlDomingo() {
		return this.blDomingo;
	}

	public void setBlDomingo(Boolean blDomingo) {
		this.blDomingo = blDomingo;
	}

	public Boolean getBlSegunda() {
		return this.blSegunda;
	}

	public void setBlSegunda(Boolean blSegunda) {
		this.blSegunda = blSegunda;
	}

	public Boolean getBlTerca() {
		return this.blTerca;
	}

	public void setBlTerca(Boolean blTerca) {
		this.blTerca = blTerca;
	}

	public Boolean getBlQuarta() {
		return this.blQuarta;
	}

	public void setBlQuarta(Boolean blQuarta) {
		this.blQuarta = blQuarta;
	}

	public Boolean getBlQuinta() {
		return this.blQuinta;
	}

	public void setBlQuinta(Boolean blQuinta) {
		this.blQuinta = blQuinta;
	}

	public Boolean getBlSexta() {
		return this.blSexta;
	}

	public void setBlSexta(Boolean blSexta) {
		this.blSexta = blSexta;
	}

	public Boolean getBlSabado() {
		return this.blSabado;
	}

	public void setBlSabado(Boolean blSabado) {
		this.blSabado = blSabado;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public Long getNrTempoColeta() {
		return this.nrTempoColeta;
	}

	public void setNrTempoColeta(Long nrTempoColeta) {
		this.nrTempoColeta = nrTempoColeta;
	}

	public Long getNrTempoEntrega() {
		return this.nrTempoEntrega;
	}

	public void setNrTempoEntrega(Long nrTempoEntrega) {
		this.nrTempoEntrega = nrTempoEntrega;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public com.mercurio.lms.configuracoes.model.Servico getServico() {
		return this.servico;
	}

	public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
		this.servico = servico;
	}

	public MunicipioFilial getMunicipioFilial() {
		return this.municipioFilial;
	}

	public void setMunicipioFilial(MunicipioFilial municipioFilial) {
		this.municipioFilial = municipioFilial;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipio() {
		return this.tipoLocalizacaoMunicipio;
	}

	public void setTipoLocalizacaoMunicipio(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio) {
		this.tipoLocalizacaoMunicipio = tipoLocalizacaoMunicipio;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioFob() {
		return tipoLocalizacaoMunicipioFob;
	}

	public void setTipoLocalizacaoMunicipioFob(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioFob) {
		this.tipoLocalizacaoMunicipioFob = tipoLocalizacaoMunicipioFob;
	}

	@ParametrizedAttribute(type = AtendimentoCliente.class)	 
	public List<AtendimentoCliente> getAtendimentoClientes() {
		return this.atendimentoClientes;
	}

	public void setAtendimentoClientes(
			List<AtendimentoCliente> atendimentoClientes) {
		this.atendimentoClientes = atendimentoClientes;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idOperacaoServicoLocaliza",
				getIdOperacaoServicoLocaliza()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OperacaoServicoLocaliza))
			return false;
		OperacaoServicoLocaliza castOther = (OperacaoServicoLocaliza) other;
		return new EqualsBuilder().append(this.getIdOperacaoServicoLocaliza(),
				castOther.getIdOperacaoServicoLocaliza()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdOperacaoServicoLocaliza())
			.toHashCode();
	}
}
