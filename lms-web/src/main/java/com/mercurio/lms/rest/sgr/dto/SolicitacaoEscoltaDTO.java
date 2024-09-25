package com.mercurio.lms.rest.sgr.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class SolicitacaoEscoltaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private DateTime dhInclusao;
	private DateTime dhInicioPrevisto;
	private DateTime dhFimPrevisto;
	private Long idExigenciaGerRisco;
	private ControleCargaSuggestDTO controleCarga;
	private FilialSuggestDTO filialSolicitante;
	private String nmUsuarioSolicitacao;
	private Long nrSolicitacaoEscolta;
	private DomainValue tpOrigem;
	private FilialSuggestDTO filialOrigem;
	private AeroportoSuggestDTO aeroportoOrigem;
	private ClienteSuggestDTO clienteOrigem;
	private DomainValue tpDestino;
	private FilialSuggestDTO filialDestino;
	private AeroportoSuggestDTO aeroportoDestino;
	private ClienteSuggestDTO clienteDestino;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;
	private Long nrKmSolicitacaoEscolta;
	private String dsObservacao;
	private DomainValue tpSituacao;
	private MeioTransporteSuggestDTO meioTransporteByIdTransportado;
	private MeioTransporteSuggestDTO meioTransporteBySemiReboque;
	private MotoristaSuggestDTO motorista;
	private Long idNaturezaProduto;
	private ClienteSuggestDTO clienteEscolta;
	private BigDecimal vlCargaCliente;
	private BigDecimal vlCargaTotal;
	private Long nrNotaAtendimento;
	private Long nrKmReal;
	private DateTime dhRealChegada;
	private DateTime dhRealInicioMissao;
	private DateTime dhRealFimMissao;
	private Long nrTempoViagemRealMin;
	private String dsOrdemServico;
	private Long idFaixaDeValor;
	private Long qtViaturas;

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhInicioPrevisto() {
		return dhInicioPrevisto;
	}

	public void setDhInicioPrevisto(DateTime dhInicioPrevisto) {
		this.dhInicioPrevisto = dhInicioPrevisto;
	}

	public DateTime getDhFimPrevisto() {
		return dhFimPrevisto;
	}

	public void setDhFimPrevisto(DateTime dhFimPrevisto) {
		this.dhFimPrevisto = dhFimPrevisto;
	}

	public Long getIdExigenciaGerRisco() {
		return idExigenciaGerRisco;
	}

	public void setIdExigenciaGerRisco(Long idExigenciaGerRisco) {
		this.idExigenciaGerRisco = idExigenciaGerRisco;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public String getNmUsuarioSolicitacao() {
		return nmUsuarioSolicitacao;
	}

	public void setNmUsuarioSolicitacao(String nmUsuarioSolicitacao) {
		this.nmUsuarioSolicitacao = nmUsuarioSolicitacao;
	}

	public Long getNrSolicitacaoEscolta() {
		return nrSolicitacaoEscolta;
	}

	public void setNrSolicitacaoEscolta(Long nrSolicitacaoEscolta) {
		this.nrSolicitacaoEscolta = nrSolicitacaoEscolta;
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

	public FilialSuggestDTO getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(FilialSuggestDTO filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public AeroportoSuggestDTO getAeroportoOrigem() {
		return aeroportoOrigem;
	}

	public void setAeroportoOrigem(AeroportoSuggestDTO aeroportoOrigem) {
		this.aeroportoOrigem = aeroportoOrigem;
	}

	public DomainValue getTpDestino() {
		return tpDestino;
	}

	public void setTpDestino(DomainValue tpDestino) {
		this.tpDestino = tpDestino;
	}

	public FilialSuggestDTO getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(FilialSuggestDTO filialDestino) {
		this.filialDestino = filialDestino;
	}

	public AeroportoSuggestDTO getAeroportoDestino() {
		return aeroportoDestino;
	}

	public void setAeroportoDestino(AeroportoSuggestDTO aeroportoDestino) {
		this.aeroportoDestino = aeroportoDestino;
	}

	public RotaColetaEntregaSuggestDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(RotaColetaEntregaSuggestDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public Long getNrKmSolicitacaoEscolta() {
		return nrKmSolicitacaoEscolta;
	}

	public void setNrKmSolicitacaoEscolta(Long nrKmSolicitacaoEscolta) {
		this.nrKmSolicitacaoEscolta = nrKmSolicitacaoEscolta;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public MeioTransporteSuggestDTO getMeioTransporteByIdTransportado() {
		return meioTransporteByIdTransportado;
	}

	public void setMeioTransporteByIdTransportado(MeioTransporteSuggestDTO meioTransporteByIdTransportado) {
		this.meioTransporteByIdTransportado = meioTransporteByIdTransportado;
	}

	public MeioTransporteSuggestDTO getMeioTransporteBySemiReboque() {
		return meioTransporteBySemiReboque;
	}

	public void setMeioTransporteBySemiReboque(MeioTransporteSuggestDTO meioTransporteBySemiReboque) {
		this.meioTransporteBySemiReboque = meioTransporteBySemiReboque;
	}

	public MotoristaSuggestDTO getMotorista() {
		return motorista;
	}

	public void setMotorista(MotoristaSuggestDTO motorista) {
		this.motorista = motorista;
	}

	public Long getIdNaturezaProduto() {
		return idNaturezaProduto;
	}

	public void setIdNaturezaProduto(Long idNaturezaProduto) {
		this.idNaturezaProduto = idNaturezaProduto;
	}

	public BigDecimal getVlCargaCliente() {
		return vlCargaCliente;
	}

	public void setVlCargaCliente(BigDecimal vlCargaCliente) {
		this.vlCargaCliente = vlCargaCliente;
	}

	public BigDecimal getVlCargaTotal() {
		return vlCargaTotal;
	}

	public void setVlCargaTotal(BigDecimal vlCargaTotal) {
		this.vlCargaTotal = vlCargaTotal;
	}

	public Long getNrNotaAtendimento() {
		return nrNotaAtendimento;
	}

	public void setNrNotaAtendimento(Long nrNotaAtendimento) {
		this.nrNotaAtendimento = nrNotaAtendimento;
	}

	public Long getNrKmReal() {
		return nrKmReal;
	}

	public void setNrKmReal(Long nrKmReal) {
		this.nrKmReal = nrKmReal;
	}

	public DateTime getDhRealChegada() {
		return dhRealChegada;
	}

	public void setDhRealChegada(DateTime dhRealChegada) {
		this.dhRealChegada = dhRealChegada;
	}

	public DateTime getDhRealInicioMissao() {
		return dhRealInicioMissao;
	}

	public void setDhRealInicioMissao(DateTime dhRealInicioMissao) {
		this.dhRealInicioMissao = dhRealInicioMissao;
	}

	public DateTime getDhRealFimMissao() {
		return dhRealFimMissao;
	}

	public void setDhRealFimMissao(DateTime dhRealFimMissao) {
		this.dhRealFimMissao = dhRealFimMissao;
	}

	public Long getNrTempoViagemRealMin() {
		return nrTempoViagemRealMin;
	}

	public void setNrTempoViagemRealMin(Long nrTempoViagemRealMin) {
		this.nrTempoViagemRealMin = nrTempoViagemRealMin;
	}

	public String getDsOrdemServico() {
		return dsOrdemServico;
	}

	public void setDsOrdemServico(String dsOrdemServico) {
		this.dsOrdemServico = dsOrdemServico;
	}

	public Long getIdFaixaDeValor() {
		return idFaixaDeValor;
	}

	public void setIdFaixaDeValor(Long idFaixaDeValor) {
		this.idFaixaDeValor = idFaixaDeValor;
	}

	public Long getQtViaturas() {
		return qtViaturas;
	}

	public void setQtViaturas(Long qtViaturas) {
		this.qtViaturas = qtViaturas;
	}

	public ClienteSuggestDTO getClienteOrigem() {
		return clienteOrigem;
	}

	public void setClienteOrigem(ClienteSuggestDTO clienteOrigem) {
		this.clienteOrigem = clienteOrigem;
	}

	public ClienteSuggestDTO getClienteDestino() {
		return clienteDestino;
	}

	public void setClienteDestino(ClienteSuggestDTO clienteDestino) {
		this.clienteDestino = clienteDestino;
	}

	public ClienteSuggestDTO getClienteEscolta() {
		return clienteEscolta;
	}

	public void setClienteEscolta(ClienteSuggestDTO clienteEscolta) {
		this.clienteEscolta = clienteEscolta;
	}

	public FilialSuggestDTO getFilialSolicitante() {
		return filialSolicitante;
	}

	public void setFilialSolicitante(FilialSuggestDTO filialSolicitante) {
		this.filialSolicitante = filialSolicitante;
	}

}
