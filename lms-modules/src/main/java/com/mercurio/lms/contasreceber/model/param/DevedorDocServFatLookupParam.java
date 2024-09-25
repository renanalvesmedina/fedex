package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.contasreceber.util.SituacaoDevedorDocServFatLookup;
import com.mercurio.lms.vendas.model.Cliente;

public class DevedorDocServFatLookupParam {
	
	private Long idDevedorDocServFat;
	
	private Long idDocumentoServico;
	
	private Long nrDocumentoServico;
	
	private String tpDocumentoServico;
	
	private Long idFilial;
	
	private Long idCliente;
	
	private Cliente cliente;
	
	private String tpSituacaoAprovacao;
	
	private String tpSituacaoCobranca;
	
	private String tpModal;
	
	private String tpAbrangencia;
	
	private Long idServico;
	
	private String tpSituacaoDesconto;
	
	private Long idMoeda;
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private String tpFrete;
	
	private Long idDivisaoCliente;
	
	private Integer tpSituacaoDevedorDocServFatValido;


	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdDevedorDocServFat() {
		return idDevedorDocServFat;
	}

	public void setIdDevedorDocServFat(Long idDevedorDocServFat) {
		this.idDevedorDocServFat = idDevedorDocServFat;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public String getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(String tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}
	
	public String getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public Long getNrDocumentoServico() {
		return nrDocumentoServico;
	}

	public void setNrDocumentoServico(Long nrDocumentoServico) {
		this.nrDocumentoServico = nrDocumentoServico;
	}

	public String getTpSituacaoDesconto() {
		return tpSituacaoDesconto;
	}

	public void setTpSituacaoDesconto(String tpSituacaoDesconto) {
		this.tpSituacaoDesconto = tpSituacaoDesconto;
	}

	public Long getIdDocumentoServico() {
		return idDocumentoServico;
	}

	public void setIdDocumentoServico(Long idDocumentoServico) {
		this.idDocumentoServico = idDocumentoServico;
	}

	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public YearMonthDay getDtEmissaoFinal() {
		return dtEmissaoFinal;
	}

	public void setDtEmissaoFinal(YearMonthDay dtEmissaoFinal) {
		this.dtEmissaoFinal = dtEmissaoFinal;
	}

	public YearMonthDay getDtEmissaoInicial() {
		return dtEmissaoInicial;
	}

	public void setDtEmissaoInicial(YearMonthDay dtEmissaoInicial) {
		this.dtEmissaoInicial = dtEmissaoInicial;
	}

	public List getTpSituacaoDevedorDocServFatValidoList() {
		return (new SituacaoDevedorDocServFatLookup(tpSituacaoDevedorDocServFatValido)).getTpSituacaoDevedor();
	}
	
	public Integer getTpSituacaoDevedorDocServFatValido() {
		return tpSituacaoDevedorDocServFatValido;
	}	

	public void setTpSituacaoDevedorDocServFatValido(Integer tpSituacaoDevedorDocServFatValido) {
		this.tpSituacaoDevedorDocServFatValido = tpSituacaoDevedorDocServFatValido;
	}

	public String getTpSituacaoCobranca() {
		return tpSituacaoCobranca;
	}

	public void setTpSituacaoCobranca(String tpSituacaoCobranca) {
		this.tpSituacaoCobranca = tpSituacaoCobranca;
	}

	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}
	
}
