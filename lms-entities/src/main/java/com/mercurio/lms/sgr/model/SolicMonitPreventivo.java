package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.municipios.model.Filial;

public class SolicMonitPreventivo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idSolicMonitPreventivo;
	private Integer nrSmp;
	private Integer nrSmpGR;
	private Integer nrSmpAnoGR;
	private VarcharI18n dsRetornoGR;
	private DateTime dhGeracao;
	private DomainValue tpStatusSmp;
	private DomainValue tpStatusSmpGR;
	private BigDecimal vlSmp;
	private ControleTrecho controleTrecho;
	private MeioTransporte meioTransporteByIdMeioTransporte;
	private MeioTransporte meioTransporteByIdMeioSemiReboque;
	private GerenciadoraRisco gerenciadoraRisco;
	private Motorista motorista;
	private ControleCarga controleCarga;
	private Filial filial;
	private MoedaPais moedaPais;
	private List<ExigenciaSmp> exigenciaSmps;
	private List<SmpManifesto> smpManifestos;
	// LMS-7906
	private List<EventoSMP> eventos;

	public Long getIdSolicMonitPreventivo() {
		return idSolicMonitPreventivo;
	}

	public void setIdSolicMonitPreventivo(Long idSolicMonitPreventivo) {
		this.idSolicMonitPreventivo = idSolicMonitPreventivo;
	}

	public Integer getNrSmp() {
		return nrSmp;
	}

	public void setNrSmp(Integer nrSmp) {
		this.nrSmp = nrSmp;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DomainValue getTpStatusSmp() {
		return tpStatusSmp;
	}

	public void setTpStatusSmp(DomainValue tpStatusSmp) {
		this.tpStatusSmp = tpStatusSmp;
	}

	public BigDecimal getVlSmp() {
		return vlSmp;
	}

	public void setVlSmp(BigDecimal vlSmp) {
		this.vlSmp = vlSmp;
	}

	public ControleTrecho getControleTrecho() {
		return controleTrecho;
	}

	public void setControleTrecho(ControleTrecho controleTrecho) {
		this.controleTrecho = controleTrecho;
	}

	public MeioTransporte getMeioTransporteByIdMeioTransporte() {
		return meioTransporteByIdMeioTransporte;
	}

	public void setMeioTransporteByIdMeioTransporte(MeioTransporte meioTransporteByIdMeioTransporte) {
		this.meioTransporteByIdMeioTransporte = meioTransporteByIdMeioTransporte;
	}

	public MeioTransporte getMeioTransporteByIdMeioSemiReboque() {
		return meioTransporteByIdMeioSemiReboque;
	}

	public void setMeioTransporteByIdMeioSemiReboque(MeioTransporte meioTransporteByIdMeioSemiReboque) {
		this.meioTransporteByIdMeioSemiReboque = meioTransporteByIdMeioSemiReboque;
	}

	public GerenciadoraRisco getGerenciadoraRisco() {
		return gerenciadoraRisco;
	}

	public void setGerenciadoraRisco(GerenciadoraRisco gerenciadoraRisco) {
		this.gerenciadoraRisco = gerenciadoraRisco;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	@ParametrizedAttribute(type = ExigenciaSmp.class)
	public List<ExigenciaSmp> getExigenciaSmps() {
		return exigenciaSmps;
	}

	public void setExigenciaSmps(List<ExigenciaSmp> exigenciaSmps) {
		this.exigenciaSmps = exigenciaSmps;
	}

	@ParametrizedAttribute(type = SmpManifesto.class)
	public List<SmpManifesto> getSmpManifestos() {
		return smpManifestos;
	}

	public void setSmpManifestos(List<SmpManifesto> smpManifestos) {
		this.smpManifestos = smpManifestos;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicMonitPreventivo)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicMonitPreventivo)) {
			return false;
		}
		SolicMonitPreventivo cast = (SolicMonitPreventivo) other;
		return new EqualsBuilder()
				.append(idSolicMonitPreventivo, cast.idSolicMonitPreventivo)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicMonitPreventivo)
				.toHashCode();
	}

	public Integer getNrSmpGR() {
		return nrSmpGR;
	}

	public void setNrSmpGR(Integer nrSmpGR) {
		this.nrSmpGR = nrSmpGR;
	}

	public Integer getNrSmpAnoGR() {
		return nrSmpAnoGR;
	}

	public void setNrSmpAnoGR(Integer nrSmpAnoGR) {
		this.nrSmpAnoGR = nrSmpAnoGR;
	}

	public VarcharI18n getDsRetornoGR() {
		return dsRetornoGR;
	}

	public void setDsRetornoGR(VarcharI18n dsRetornoGR) {
		this.dsRetornoGR = dsRetornoGR;
	}

	public DomainValue getTpStatusSmpGR() {
		return tpStatusSmpGR;
	}

	public void setTpStatusSmpGR(DomainValue tpStatusSmpGR) {
		this.tpStatusSmpGR = tpStatusSmpGR;
	}

	@ParametrizedAttribute(type = EventoSMP.class)
	public List<EventoSMP> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoSMP> eventos) {
		this.eventos = eventos;
	}

}
