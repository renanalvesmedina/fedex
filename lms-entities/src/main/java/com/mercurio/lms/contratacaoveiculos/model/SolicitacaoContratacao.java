package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.VeiculoControleCarga;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class SolicitacaoContratacao implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idSolicitacaoContratacao;

	/** persistent field */
	private Long nrSolicitacaoContratacao;

	/** persistent field */
	private DomainValue tpSolicitacaoContratacao;

	private DomainValue tpVinculoContratacao;

	private DomainValue tpRotaSolicitacao;
	
	// LMSA-6319
	private DomainValue tpCargaCompartilhada;

	private RotaIdaVolta rotaIdaVolta;

	private String nrDddSolicitante;

	private String nrTelefoneSolicitante;

	/** persistent field */
	private DomainValue tpSituacaoContratacao;

	/** persistent field */
	private Boolean blIndicadorRastreamento;

	/** persistent field */
	private Long nrAnoFabricacaoMeioTransporteSemiReboque;

	/** persistent field */
	private Long nrAnoFabricacaoMeioTransporte;

	/** persistent field */
	private YearMonthDay dtCriacao;

	/** nullable persistent field */
	private YearMonthDay dtViagem;

	/** nullable persistent field */
	private BigDecimal vlFreteSugerido;

	/** nullable persistent field */
	private BigDecimal vlPremio;

	/** nullable persistent field */
	private BigDecimal vlFreteMaximoAutorizado;

	/** nullable persistent field */
	private BigDecimal vlFreteNegociado;

	/** nullable persistent field */
	private YearMonthDay dtInicioContratacao;

	/** nullable persistent field */
	private YearMonthDay dtFimContratacao;

	/** nullable persistent field */
	private String nrIdentificacaoMeioTransp;

	/** nullable persistent field */
	private String nrIdentificacaoSemiReboque;

	/** nullable persistent field */
	private String obObservacao;

	/** nullable persistent field */
	private String obMotivoReprovacao;

	private DomainValue tpFluxoContratacao;

	/** persistent field */
	private Rota rota;

	/** persistent field */
	private TipoMeioTransporte tipoMeioTransporte;

	private Acao acao;

	/** persistent field */
	private Pendencia pendencia;

	/** persistent field */
	private Usuario usuarioSolicitador;

	/** persistent field */
	private MoedaPais moedaPais;

	/** persistent field */
	private Filial filial;
	
	/** persistent field */
	private Integer qtEixos;

	/** persistent field */
	private BigDecimal vlPostoPassagem;

	/** persistent field */
	private DomainValue tpAbrangencia;

	/** persistent field */
	private Boolean blQuebraMeioTransporte;

	/** nullable persistent field */
	private ControleCarga controleCarga;

	private DomainValue tpModal;

	/** persistent field */
	private List<TabelaColetaEntrega> tabelaColetaEntregas;

	/** persistent field */
	private List<ChecklistMeioTransporte> checklistMeioTransportes;

	/** persistent field */
	private List<MeioTransporteContratado> meioTransporteContratados;

	/** persistent field */
	private List<ControleCarga> controleCargas;

	/** nullable persistent field */
	private List<VeiculoControleCarga> veiculoControleCargas;

	/** persistent field */
	private List<FluxoContratacao> fluxosContratacao;
	
	public SolicitacaoContratacao() {
	}

	public SolicitacaoContratacao(Long idSolicitacaoContratacao, Long nrSolicitacaoContratacao, DomainValue tpSolicitacaoContratacao, DomainValue tpVinculoContratacao, DomainValue tpRotaSolicitacao, RotaIdaVolta rotaIdaVolta, String nrDddSolicitante, String nrTelefoneSolicitante, DomainValue tpSituacaoContratacao, Boolean blIndicadorRastreamento, Long nrAnoFabricacaoMeioTransporteSemiReboque, Long nrAnoFabricacaoMeioTransporte, YearMonthDay dtCriacao, YearMonthDay dtViagem, BigDecimal vlFreteSugerido, BigDecimal vlPremio, BigDecimal vlFreteMaximoAutorizado, BigDecimal vlFreteNegociado, YearMonthDay dtInicioContratacao, YearMonthDay dtFimContratacao, String nrIdentificacaoMeioTransp, String nrIdentificacaoSemiReboque, String obObservacao, String obMotivoReprovacao, DomainValue tpFluxoContratacao, Rota rota, TipoMeioTransporte tipoMeioTransporte, Acao acao, Pendencia pendencia, Usuario usuarioSolicitador, MoedaPais moedaPais, Filial filial, Integer qtEixos, BigDecimal vlPostoPassagem, DomainValue tpAbrangencia, Boolean blQuebraMeioTransporte, ControleCarga controleCarga, DomainValue tpModal, List<TabelaColetaEntrega> tabelaColetaEntregas, List<ChecklistMeioTransporte> checklistMeioTransportes, List<MeioTransporteContratado> meioTransporteContratados, List<ControleCarga> controleCargas, List<VeiculoControleCarga> veiculoControleCargas, List<FluxoContratacao> fluxosContratacao) {
		this.idSolicitacaoContratacao = idSolicitacaoContratacao;
		this.nrSolicitacaoContratacao = nrSolicitacaoContratacao;
		this.tpSolicitacaoContratacao = tpSolicitacaoContratacao;
		this.tpVinculoContratacao = tpVinculoContratacao;
		this.tpRotaSolicitacao = tpRotaSolicitacao;
		this.rotaIdaVolta = rotaIdaVolta;
		this.nrDddSolicitante = nrDddSolicitante;
		this.nrTelefoneSolicitante = nrTelefoneSolicitante;
		this.tpSituacaoContratacao = tpSituacaoContratacao;
		this.blIndicadorRastreamento = blIndicadorRastreamento;
		this.nrAnoFabricacaoMeioTransporteSemiReboque = nrAnoFabricacaoMeioTransporteSemiReboque;
		this.nrAnoFabricacaoMeioTransporte = nrAnoFabricacaoMeioTransporte;
		this.dtCriacao = dtCriacao;
		this.dtViagem = dtViagem;
		this.vlFreteSugerido = vlFreteSugerido;
		this.vlPremio = vlPremio;
		this.vlFreteMaximoAutorizado = vlFreteMaximoAutorizado;
		this.vlFreteNegociado = vlFreteNegociado;
		this.dtInicioContratacao = dtInicioContratacao;
		this.dtFimContratacao = dtFimContratacao;
		this.nrIdentificacaoMeioTransp = nrIdentificacaoMeioTransp;
		this.nrIdentificacaoSemiReboque = nrIdentificacaoSemiReboque;
		this.obObservacao = obObservacao;
		this.obMotivoReprovacao = obMotivoReprovacao;
		this.tpFluxoContratacao = tpFluxoContratacao;
		this.rota = rota;
		this.tipoMeioTransporte = tipoMeioTransporte;
		this.acao = acao;
		this.pendencia = pendencia;
		this.usuarioSolicitador = usuarioSolicitador;
		this.moedaPais = moedaPais;
		this.filial = filial;
		this.qtEixos = qtEixos;
		this.vlPostoPassagem = vlPostoPassagem;
		this.tpAbrangencia = tpAbrangencia;
		this.blQuebraMeioTransporte = blQuebraMeioTransporte;
		this.controleCarga = controleCarga;
		this.tpModal = tpModal;
		this.tabelaColetaEntregas = tabelaColetaEntregas;
		this.checklistMeioTransportes = checklistMeioTransportes;
		this.meioTransporteContratados = meioTransporteContratados;
		this.controleCargas = controleCargas;
		this.veiculoControleCargas = veiculoControleCargas;
		this.fluxosContratacao = fluxosContratacao;
	}

	public Long getIdSolicitacaoContratacao() {
		return this.idSolicitacaoContratacao;
	}

	public void setIdSolicitacaoContratacao(Long idSolicitacaoContratacao) {
		this.idSolicitacaoContratacao = idSolicitacaoContratacao;
	}

	public Long getNrSolicitacaoContratacao() {
		return this.nrSolicitacaoContratacao;
	}

	public void setNrSolicitacaoContratacao(Long nrSolicitacaoContratacao) {
		this.nrSolicitacaoContratacao = nrSolicitacaoContratacao;
	}

	public DomainValue getTpSolicitacaoContratacao() {
		return this.tpSolicitacaoContratacao;
	}

	public void setTpSolicitacaoContratacao(DomainValue tpSolicitacaoContratacao) {
		this.tpSolicitacaoContratacao = tpSolicitacaoContratacao;
	}

	public DomainValue getTpSituacaoContratacao() {
		return this.tpSituacaoContratacao;
	}

	public void setTpSituacaoContratacao(DomainValue tpSituacaoContratacao) {
		this.tpSituacaoContratacao = tpSituacaoContratacao;
	}

	public Boolean getBlIndicadorRastreamento() {
		return this.blIndicadorRastreamento;
	}

	public void setBlIndicadorRastreamento(Boolean blIndicadorRastreamento) {
		this.blIndicadorRastreamento = blIndicadorRastreamento;
	}

	public YearMonthDay getDtCriacao() {
		return this.dtCriacao;
	}

	public void setDtCriacao(YearMonthDay dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public YearMonthDay getDtViagem() {
		return this.dtViagem;
	}

	public void setDtViagem(YearMonthDay dtViagem) {
		this.dtViagem = dtViagem;
	}

	public BigDecimal getVlFreteSugerido() {
		return this.vlFreteSugerido;
	}

	public void setVlFreteSugerido(BigDecimal vlFreteSugerido) {
		this.vlFreteSugerido = vlFreteSugerido;
	}

	public BigDecimal getVlFreteMaximoAutorizado() {
		return this.vlFreteMaximoAutorizado;
	}

	public void setVlFreteMaximoAutorizado(BigDecimal vlFreteMaximoAutorizado) {
		this.vlFreteMaximoAutorizado = vlFreteMaximoAutorizado;
	}

	public BigDecimal getVlFreteNegociado() {
		return this.vlFreteNegociado;
	}

	public void setVlFreteNegociado(BigDecimal vlFreteNegociado) {
		this.vlFreteNegociado = vlFreteNegociado;
	}

	public YearMonthDay getDtInicioContratacao() {
		return this.dtInicioContratacao;
	}

	public void setDtInicioContratacao(YearMonthDay dtInicioContratacao) {
		this.dtInicioContratacao = dtInicioContratacao;
	}

	public YearMonthDay getDtFimContratacao() {
		return this.dtFimContratacao;
	}

	public void setDtFimContratacao(YearMonthDay dtFimContratacao) {
		this.dtFimContratacao = dtFimContratacao;
	}

	public String getNrIdentificacaoMeioTransp() {
		return this.nrIdentificacaoMeioTransp;
	}

	public void setNrIdentificacaoMeioTransp(String nrIdentificacaoMeioTransp) {
		this.nrIdentificacaoMeioTransp = nrIdentificacaoMeioTransp;
	}

	public String getNrIdentificacaoSemiReboque() {
		return this.nrIdentificacaoSemiReboque;
	}

	public void setNrIdentificacaoSemiReboque(String nrIdentificacaoSemiReboque) {
		this.nrIdentificacaoSemiReboque = nrIdentificacaoSemiReboque;
	}

	public String getObObservacao() {
		return this.obObservacao;
	}

	public void setObObservacao(String obObservacao) {
		this.obObservacao = obObservacao;
	}

	public DomainValue getTpFluxoContratacao() {
		return tpFluxoContratacao;
	}

	public void setTpFluxoContratacao(DomainValue tpFluxoContratacao) {
		this.tpFluxoContratacao = tpFluxoContratacao;
	}

	public Rota getRota() {
		return this.rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public TipoMeioTransporte getTipoMeioTransporte() {
		return this.tipoMeioTransporte;
	}

	public void setTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
		this.tipoMeioTransporte = tipoMeioTransporte;
	}

	public Usuario getUsuarioSolicitador() {
		return this.usuarioSolicitador;
	}

	public void setUsuarioSolicitador(Usuario funcionarioSolicitador) {
		this.usuarioSolicitador = funcionarioSolicitador;
	}

	public MoedaPais getMoedaPais() {
		return this.moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Boolean getBlQuebraMeioTransporte() {
		return blQuebraMeioTransporte;
	}

	public void setBlQuebraMeioTransporte(Boolean blQuebraMeioTransporte) {
		this.blQuebraMeioTransporte = blQuebraMeioTransporte;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	@ParametrizedAttribute(type = TabelaColetaEntrega.class)
	public List<TabelaColetaEntrega> getTabelaColetaEntregas() {
		return this.tabelaColetaEntregas;
	}

	public void setTabelaColetaEntregas(
			List<TabelaColetaEntrega> tabelaColetaEntregas) {
		this.tabelaColetaEntregas = tabelaColetaEntregas;
	}

	@ParametrizedAttribute(type = ChecklistMeioTransporte.class)
	public List<ChecklistMeioTransporte> getChecklistMeioTransportes() {
		return this.checklistMeioTransportes;
	}

	public void setChecklistMeioTransportes(
			List<ChecklistMeioTransporte> checklistMeioTransportes) {
		this.checklistMeioTransportes = checklistMeioTransportes;
	}

	@ParametrizedAttribute(type = MeioTransporteContratado.class)
	public List<MeioTransporteContratado> getMeioTransporteContratados() {
		return this.meioTransporteContratados;
	}

	public void setMeioTransporteContratados(
			List<MeioTransporteContratado> meioTransporteContratados) {
		this.meioTransporteContratados = meioTransporteContratados;
	}

	@ParametrizedAttribute(type = VeiculoControleCarga.class)
	public List<VeiculoControleCarga> getVeiculoControleCargas() {
		return veiculoControleCargas;
	}

	public void setVeiculoControleCargas(
			List<VeiculoControleCarga> veiculoControleCargas) {
		this.veiculoControleCargas = veiculoControleCargas;
	}

	@ParametrizedAttribute(type = ControleCarga.class)
	public List<ControleCarga> getControleCargas() {
		return this.controleCargas;
	}

	public void setControleCargas(List<ControleCarga> controleCargas) {
		this.controleCargas = controleCargas;
	}

	public DomainValue getTpVinculoContratacao() {
		return tpVinculoContratacao;
	}

	public void setTpVinculoContratacao(DomainValue tpVinculoContratacao) {
		this.tpVinculoContratacao = tpVinculoContratacao;
	}

	public String getObMotivoReprovacao() {
		return obMotivoReprovacao;
	}

	public void setObMotivoReprovacao(String obMotivoReprovacao) {
		this.obMotivoReprovacao = obMotivoReprovacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Long getNrAnoFabricacaoMeioTransporte() {
		return nrAnoFabricacaoMeioTransporte;
	}

	public void setNrAnoFabricacaoMeioTransporte(
			Long nrAnoFabricacaoMeioTransporte) {
		this.nrAnoFabricacaoMeioTransporte = nrAnoFabricacaoMeioTransporte;
	}

	public Long getNrAnoFabricacaoMeioTransporteSemiReboque() {
		return nrAnoFabricacaoMeioTransporteSemiReboque;
	}

	public void setNrAnoFabricacaoMeioTransporteSemiReboque(
			Long nrAnoFabricacaoMeioTransporteSemiReboque) {
		this.nrAnoFabricacaoMeioTransporteSemiReboque = nrAnoFabricacaoMeioTransporteSemiReboque;
	}

	public BigDecimal getVlPremio() {
		return vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public DomainValue getTpRotaSolicitacao() {
		return tpRotaSolicitacao;
	}

	public void setTpRotaSolicitacao(DomainValue tpRotaSolicitacao) {
		this.tpRotaSolicitacao = tpRotaSolicitacao;
	}

	public RotaIdaVolta getRotaIdaVolta() {
		return rotaIdaVolta;
	}

	public void setRotaIdaVolta(RotaIdaVolta rotaIdaVolta) {
		this.rotaIdaVolta = rotaIdaVolta;
	}

	public String getNrDddSolicitante() {
		return nrDddSolicitante;
	}

	public void setNrDddSolicitante(String nrDddSolicitante) {
		this.nrDddSolicitante = nrDddSolicitante;
	}

	public String getNrTelefoneSolicitante() {
		return nrTelefoneSolicitante;
	}

	public void setNrTelefoneSolicitante(String nrTelefoneSolicitante) {
		this.nrTelefoneSolicitante = nrTelefoneSolicitante;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	
	public Integer getQtEixos() {
		return qtEixos;
	}

	public void setQtEixos(Integer qtEixos) {
		this.qtEixos = qtEixos;
	}

	public BigDecimal getVlPostoPassagem() {
		return vlPostoPassagem;
	}

	public void setVlPostoPassagem(BigDecimal vlPostoPassagem) {
		this.vlPostoPassagem = vlPostoPassagem;
	}

	@ParametrizedAttribute(type = FluxoContratacao.class)
	public List<FluxoContratacao> getFluxosContratacao() {
		return fluxosContratacao;
	}

	public void setFluxosContratacao(List<FluxoContratacao> fluxosContratacao) {
		this.fluxosContratacao = fluxosContratacao;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}
	
	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idSolicitacaoContratacao",
				getIdSolicitacaoContratacao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SolicitacaoContratacao))
			return false;
		SolicitacaoContratacao castOther = (SolicitacaoContratacao) other;
		return new EqualsBuilder().append(this.getIdSolicitacaoContratacao(),
				castOther.getIdSolicitacaoContratacao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdSolicitacaoContratacao())
			.toHashCode();
	}

	/**
	 * @param tpModal
	 *            the tpModal to set
	 */
	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
}

	/**
	 * @return the tpModal
	 */
	public DomainValue getTpModal() {
		return tpModal;
	}

	// LMSA-6319
	public DomainValue getTpCargaCompartilhada() {
		return tpCargaCompartilhada;
	}

	public void setTpCargaCompartilhada(DomainValue tpCargaCompartilhada) {
		this.tpCargaCompartilhada = tpCargaCompartilhada;
	}
	
	
	
}
