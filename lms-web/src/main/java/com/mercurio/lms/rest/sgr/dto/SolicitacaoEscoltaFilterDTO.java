package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class SolicitacaoEscoltaFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filialSolicitacao;
	private Long nrSolicitacaoEscolta;
	private DomainValue tpSituacao;
	private UsuarioDTO usuarioSolicitante;
	private DateTime dhInclusaoInicial;
	private DateTime dhInclusaoFinal;
	private DateTime dhInicioPrevistoInicial;
	private DateTime dhInicioPrevistoFinal;
	private Long idExigenciaGerRisco;
	private Long nrKmSolicitacaoEscoltaInicial;
	private Long nrKmSolicitacaoEscoltaFinal;
	private ClienteSuggestDTO clienteEscolta;
	private ControleCargaSuggestDTO controleCarga;
	private Long idNaturezaProduto;
	private MeioTransporteSuggestDTO meioTransporteByIdTransportado;
	private MeioTransporteSuggestDTO meioTransporteBySemiReboque;
	private DomainValue tpOrigem;
	private FilialSuggestDTO filialOrigem;
	private ClienteSuggestDTO clienteOrigem;
	private AeroportoSuggestDTO aeroportoOrigem;
	private DomainValue tpDestino;
	private FilialSuggestDTO filialDestino;
	private ClienteSuggestDTO clienteDestino;
	private AeroportoSuggestDTO aeroportoDestino;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;

	public FilialSuggestDTO getFilialSolicitacao() {
		return filialSolicitacao;
	}

	public void setFilialSolicitacao(FilialSuggestDTO filialSolicitacao) {
		this.filialSolicitacao = filialSolicitacao;
	}

	public Long getNrSolicitacaoEscolta() {
		return nrSolicitacaoEscolta;
	}

	public void setNrSolicitacaoEscolta(Long nrSolicitacaoEscolta) {
		this.nrSolicitacaoEscolta = nrSolicitacaoEscolta;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public UsuarioDTO getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(UsuarioDTO usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public DateTime getDhInclusaoInicial() {
		return dhInclusaoInicial;
	}

	public void setDhInclusaoInicial(DateTime dhInclusaoInicial) {
		this.dhInclusaoInicial = dhInclusaoInicial;
	}

	public DateTime getDhInclusaoFinal() {
		return dhInclusaoFinal;
	}

	public void setDhInclusaoFinal(DateTime dhInclusaoFinal) {
		this.dhInclusaoFinal = dhInclusaoFinal;
	}

	public DateTime getDhInicioPrevistoInicial() {
		return dhInicioPrevistoInicial;
	}

	public void setDhInicioPrevistoInicial(DateTime dhInicioPrevistoInicial) {
		this.dhInicioPrevistoInicial = dhInicioPrevistoInicial;
	}

	public DateTime getDhInicioPrevistoFinal() {
		return dhInicioPrevistoFinal;
	}

	public void setDhInicioPrevistoFinal(DateTime dhInicioPrevistoFinal) {
		this.dhInicioPrevistoFinal = dhInicioPrevistoFinal;
	}

	public Long getIdExigenciaGerRisco() {
		return idExigenciaGerRisco;
	}

	public void setIdExigenciaGerRisco(Long idExigenciaGerRisco) {
		this.idExigenciaGerRisco = idExigenciaGerRisco;
	}

	public Long getNrKmSolicitacaoEscoltaInicial() {
		return nrKmSolicitacaoEscoltaInicial;
	}

	public void setNrKmSolicitacaoEscoltaInicial(Long nrKmSolicitacaoEscoltaInicial) {
		this.nrKmSolicitacaoEscoltaInicial = nrKmSolicitacaoEscoltaInicial;
	}

	public Long getNrKmSolicitacaoEscoltaFinal() {
		return nrKmSolicitacaoEscoltaFinal;
	}

	public void setNrKmSolicitacaoEscoltaFinal(Long nrKmSolicitacaoEscoltaFinal) {
		this.nrKmSolicitacaoEscoltaFinal = nrKmSolicitacaoEscoltaFinal;
	}

	public ClienteSuggestDTO getClienteEscolta() {
		return clienteEscolta;
	}

	public void setClienteEscolta(ClienteSuggestDTO clienteEscolta) {
		this.clienteEscolta = clienteEscolta;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Long getIdNaturezaProduto() {
		return idNaturezaProduto;
	}

	public void setIdNaturezaProduto(Long idNaturezaProduto) {
		this.idNaturezaProduto = idNaturezaProduto;
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

	public ClienteSuggestDTO getClienteOrigem() {
		return clienteOrigem;
	}

	public void setClienteOrigem(ClienteSuggestDTO clienteOrigem) {
		this.clienteOrigem = clienteOrigem;
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

	public ClienteSuggestDTO getClienteDestino() {
		return clienteDestino;
	}

	public void setClienteDestino(ClienteSuggestDTO clienteDestino) {
		this.clienteDestino = clienteDestino;
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

}
