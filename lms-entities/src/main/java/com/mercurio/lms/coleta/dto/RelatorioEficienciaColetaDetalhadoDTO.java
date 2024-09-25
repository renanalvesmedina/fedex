package com.mercurio.lms.coleta.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class RelatorioEficienciaColetaDetalhadoDTO {

	private String idCliente;
	private String tipoIdentificacao;
	private String nomeCliente;
	private String idFilial;
	private String nomeFilial;
	private BigDecimal numeroColeta;
	private BigDecimal numeroRota;
	private VarcharI18n modoPedidoColeta;
	private String nomeUsuario;
	private BigDecimal quantidadeDocumentos;
	private Date dataSolicitacao;
	private Date dataPrevista;
	private Date dataBaixa;
	private String usuarioBaixa;
	private String efc;
	private BigDecimal codigoUltimaOcorrencia;
	private VarcharI18n descricaoUltimaOcorrencia;
	private String usuarioUltimaOcorrencia;
	private Date dataUltimaOcorrencia;
	private BigDecimal codigoOcorrenciaTNT;
	private VarcharI18n descricaoOcorrenciaTNT;
	private String usuarioOcorrenciaTNT;
	private Date dataOcorrenciaTNT;
 
	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getTipoIdentificacao() {
		return tipoIdentificacao;
	}

	public void setTipoIdentificacao(String tipoIdentificacao) {
		this.tipoIdentificacao = tipoIdentificacao;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getNomeFilial() {
		return nomeFilial;
	}

	public void setNomeFilial(String nomeFilial) {
		this.nomeFilial = nomeFilial;
	}

	public BigDecimal getNumeroColeta() {
		return numeroColeta;
	}

	public void setNumeroColeta(BigDecimal numeroColeta) {
		this.numeroColeta = numeroColeta;
	}

	public BigDecimal getNumeroRota() {
		return numeroRota;
	}

	public void setNumeroRota(BigDecimal numeroRota) {
		this.numeroRota = numeroRota;
	}

	public VarcharI18n getModoPedidoColeta() {
		return modoPedidoColeta;
	}

	public void setModoPedidoColeta(VarcharI18n modoPedidoColeta) {
		this.modoPedidoColeta = modoPedidoColeta;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public BigDecimal getQuantidadeDocumentos() {
		return quantidadeDocumentos;
	}

	public void setQuantidadeDocumentos(BigDecimal quantidadeDocumentos) {
		this.quantidadeDocumentos = quantidadeDocumentos;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public Date getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public String getUsuarioBaixa() {
		return usuarioBaixa;
	}

	public void setUsuarioBaixa(String usuarioBaixa) {
		this.usuarioBaixa = usuarioBaixa;
	}

	public String getEfc() {
		return efc;
	}

	public void setEfc(String efc) {
		this.efc = efc;
	}

	public BigDecimal getCodigoUltimaOcorrencia() {
		return codigoUltimaOcorrencia;
	}

	public void setCodigoUltimaOcorrencia(BigDecimal codigoUltimaOcorrencia) {
		this.codigoUltimaOcorrencia = codigoUltimaOcorrencia;
	}

	public VarcharI18n getDescricaoUltimaOcorrencia() {
		return descricaoUltimaOcorrencia;
	}

	public void setDescricaoUltimaOcorrencia(VarcharI18n descricaoUltimaOcorrencia) {
		this.descricaoUltimaOcorrencia = descricaoUltimaOcorrencia;
	}

	public String getUsuarioUltimaOcorrencia() {
		return usuarioUltimaOcorrencia;
	}

	public void setUsuarioUltimaOcorrencia(String usuarioUltimaOcorrencia) {
		this.usuarioUltimaOcorrencia = usuarioUltimaOcorrencia;
	}

	public Date getDataUltimaOcorrencia() {
		return dataUltimaOcorrencia;
	}

	public void setDataUltimaOcorrencia(Date dataUltimaOcorrencia) {
		this.dataUltimaOcorrencia = dataUltimaOcorrencia;
	}

	public BigDecimal getCodigoOcorrenciaTNT() {
		return codigoOcorrenciaTNT;
	}

	public void setCodigoOcorrenciaTNT(BigDecimal codigoOcorrenciaTNT) {
		this.codigoOcorrenciaTNT = codigoOcorrenciaTNT;
	}

	public VarcharI18n getDescricaoOcorrenciaTNT() {
		return descricaoOcorrenciaTNT;
	}

	public void setDescricaoOcorrenciaTNT(VarcharI18n descricaoOcorrenciaTNT) {
		this.descricaoOcorrenciaTNT = descricaoOcorrenciaTNT;
	}

	public String getUsuarioOcorrenciaTNT() {
		return usuarioOcorrenciaTNT;
	}

	public void setUsuarioOcorrenciaTNT(String usuarioOcorrenciaTNT) {
		this.usuarioOcorrenciaTNT = usuarioOcorrenciaTNT;
	}

	public Date getDataOcorrenciaTNT() {
		return dataOcorrenciaTNT;
	}

	public void setDataOcorrenciaTNT(Date dataOcorrenciaTNT) {
		this.dataOcorrenciaTNT = dataOcorrenciaTNT;
	}

}
