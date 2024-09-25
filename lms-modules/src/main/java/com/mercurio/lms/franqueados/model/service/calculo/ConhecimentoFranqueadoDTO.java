package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public abstract class ConhecimentoFranqueadoDTO extends DocumentoFranqueadoDTO {

	private static final long serialVersionUID = 1L;

	private Long idMunicipioColeta;
	private Long idMunicipioEntrega;
	private Long idClienteRemetente;
	private Long idClienteDestinatario;
	
	private Long idFilialOrigem;
	private Long idFilialDestino;
	
	private String tpFrete;
	
	private BigDecimal vlTotalDocServico = BigDecimal.ZERO;
	
	private Long idDevedorDocServFat;
	
	private Long idDesconto;
	private BigDecimal vlDesconto;
	private String tpSituacaoAprovacao;
	
	private Long idMotivoDesconto;
	private BigDecimal vlMercadoria = BigDecimal.ZERO;
	
	private BigDecimal vlImposto = BigDecimal.ZERO ;
	
	private Integer nrDistancia = 0;
	
	private BigDecimal vlCustoCarreteiro = BigDecimal.ZERO ;
	private BigDecimal vlCustoAereo = BigDecimal.ZERO ;
	
	private int qtFranquias = 0 ;
	
	private Long idDoctoServicoFranqueadoOriginal;
	private BigDecimal vlParticipacaoOriginal;
	
	private Long idServicoAdicional;
	
	public CalculoNacionalFranqueados ICalculoFranquiado(){
		return new CalculoNacionalFranqueados();
	}
	
	public Long getIdMunicipioColeta() {
		return idMunicipioColeta;
	}
	public void setIdMunicipioColeta(Long idMunicipioColeta) {
		this.idMunicipioColeta = idMunicipioColeta;
	}
	public Long getIdMunicipioEntrega() {
		return idMunicipioEntrega;
	}
	public void setIdMunicipioEntrega(Long idMunicipioEntrega) {
		this.idMunicipioEntrega = idMunicipioEntrega;
	}
	public Long getIdClienteRemetente() {
		return idClienteRemetente;
	}
	public void setIdClienteRemetente(Long idClienteRemetente) {
		this.idClienteRemetente = idClienteRemetente;
	}
	public Long getIdClienteDestinatario() {
		return idClienteDestinatario;
	}
	public void setIdClienteDestinatario(Long idClienteDestinatario) {
		this.idClienteDestinatario = idClienteDestinatario;
	}
	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}
	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}
	public Long getIdFilialDestino() {
		return idFilialDestino;
	}
	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}
	public String getTpFrete() {
		return tpFrete;
	}
	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}
	public BigDecimal getVlTotalDocServico() {
		return vlTotalDocServico;
	}
	public void setVlTotalDocServico(BigDecimal vlTotalDocServico) {
		this.vlTotalDocServico = vlTotalDocServico;
	}
	public Long getIdDevedorDocServFat() {
		return idDevedorDocServFat;
	}
	public void setIdDevedorDocServFat(Long idDevedorDocServFat) {
		this.idDevedorDocServFat = idDevedorDocServFat;
	}
	public Long getIdDesconto() {
		return idDesconto;
	}
	public void setIdDesconto(Long idDesconto) {
		this.idDesconto = idDesconto;
	}
	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}
	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}
	public String getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}
	public void setTpSituacaoAprovacao(String tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}
	public Long getIdMotivoDesconto() {
		return idMotivoDesconto;
	}
	public void setIdMotivoDesconto(Long idMotivoDesconto) {
		this.idMotivoDesconto = idMotivoDesconto;
	}
	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}
	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}
	public BigDecimal getVlImposto() {
		return vlImposto;
	}
	public void setVlImposto(BigDecimal vlImposto) {
		this.vlImposto = vlImposto;
	}
	public Integer getNrDistancia() {
		return nrDistancia;
	}
	public void setNrDistancia(Integer nrDistancia) {
		this.nrDistancia = nrDistancia;
	}
	public BigDecimal getVlCustoCarreteiro() {
		return vlCustoCarreteiro;
	}
	public void setVlCustoCarreteiro(BigDecimal vlCustoCarreteiro) {
		this.vlCustoCarreteiro = vlCustoCarreteiro;
	}
	public BigDecimal getVlCustoAereo() {
		return vlCustoAereo;
	}
	public void setVlCustoAereo(BigDecimal vlCustoAereo) {
		this.vlCustoAereo = vlCustoAereo;
	}
	public int getQtFranquias() {
		return qtFranquias;
	}
	public void setQtFranquias(Integer qtFranquias) {
		if( qtFranquias == null ){
			this.qtFranquias = 0;
		}else{
			this.qtFranquias = qtFranquias;
		}
	}
	public Long getIdDoctoServicoFranqueadoOriginal() {
		return idDoctoServicoFranqueadoOriginal;
	}
	public void setIdDoctoServicoFranqueadoOriginal(Long idDoctoServicoFranqueadoOriginal) {
		this.idDoctoServicoFranqueadoOriginal = idDoctoServicoFranqueadoOriginal;
	}
	public BigDecimal getVlParticipacaoOriginal() {
		return vlParticipacaoOriginal;
	}
	public void setVlParticipacaoOriginal(BigDecimal vlParticipacaoOriginal) {
		this.vlParticipacaoOriginal = vlParticipacaoOriginal;
	}
	public Long getIdServicoAdicional() {
		return idServicoAdicional;
	}
	public void setIdServicoAdicional(Long idServicoAdicional) {
		this.idServicoAdicional = idServicoAdicional;
	}

}
