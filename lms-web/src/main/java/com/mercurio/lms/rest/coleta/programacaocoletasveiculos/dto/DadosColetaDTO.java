package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class DadosColetaDTO extends BaseDTO{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	

	private String sgFilialPedidoColeta;
	private Long nrColeta;
	private String tpStatusColeta_description;
	private String filialByIdFilialResponsavel_sgFilial;
	private String filialByIdFilialResponsavel_nmFantasia;
	private String filialByIdFilialSolicitante_sgFilial;
	private String filialByIdFilialSolicitante_nmFantasia;
	private DateTime dhPedidoColeta;
	private String tpPedidoColeta_description;
	private String nmSolicitante;
	private String tpModoPedidoColeta_description;
	private String cliente_nrIdentificacaoFormatado;
	private String cliente_nmPessoa;
	private String enderecoComComplemento;
	private String nmMunicipio;
	private String nrCep;
	private DateTime dhColetaDisponivel;
	private DateTime hrLimiteColeta;
	private YearMonthDay dtPrevisaoColeta;
	private String nrTelefoneCliente;
	private String nmContatoCliente;
	private String dsRota;
	private String dsRegiaoColetaEntregaFil;
	private Integer qtTotalVolumesVerificado;
	private BigDecimal psTotalInformado;
	private BigDecimal vlTotalVerificado;
	private BigDecimal psTotalVerificado;
	private String nrFrota;
	private String nrIdentificador;
	private String filialByIdFilialOrigem_sgFilial;
	private Long idControleCarga;
	private Long nrControleCarga;
	private String manifestoColeta_sgFilial;
	private Integer manifestoColeta_nrManifesto;
	private Boolean blProdutoDiferenciado;
	private String obPedidoColeta;
	private String obsCancelamento;

	
	
	public String getSgFilialPedidoColeta() {
		return sgFilialPedidoColeta;
	}
	public void setSgFilialPedidoColeta(String sgFilialPedidoColeta) {
		this.sgFilialPedidoColeta = sgFilialPedidoColeta;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}
	public String getTpStatusColeta_description() {
		return tpStatusColeta_description;
	}
	public void setTpStatusColeta_description(String tpStatusColeta_description) {
		this.tpStatusColeta_description = tpStatusColeta_description;
	}
	public String getFilialByIdFilialResponsavel_sgFilial() {
		return filialByIdFilialResponsavel_sgFilial;
	}
	public void setFilialByIdFilialResponsavel_sgFilial(String filialByIdFilialResponsavel_sgFilial) {
		this.filialByIdFilialResponsavel_sgFilial = filialByIdFilialResponsavel_sgFilial;
	}
	public String getFilialByIdFilialResponsavel_nmFantasia() {
		return filialByIdFilialResponsavel_nmFantasia;
	}
	public void setFilialByIdFilialResponsavel_nmFantasia(String filialByIdFilialResponsavel_nmFantasia) {
		this.filialByIdFilialResponsavel_nmFantasia = filialByIdFilialResponsavel_nmFantasia;
	}
	public String getFilialByIdFilialSolicitante_sgFilial() {
		return filialByIdFilialSolicitante_sgFilial;
	}
	public void setFilialByIdFilialSolicitante_sgFilial(String filialByIdFilialSolicitante_sgFilial) {
		this.filialByIdFilialSolicitante_sgFilial = filialByIdFilialSolicitante_sgFilial;
	}
	public String getFilialByIdFilialSolicitante_nmFantasia() {
		return filialByIdFilialSolicitante_nmFantasia;
	}
	public void setFilialByIdFilialSolicitante_nmFantasia(String filialByIdFilialSolicitante_nmFantasia) {
		this.filialByIdFilialSolicitante_nmFantasia = filialByIdFilialSolicitante_nmFantasia;
	}
	public DateTime getDhPedidoColeta() {
		return dhPedidoColeta;
	}
	public void setDhPedidoColeta(DateTime dhPedidoColeta) {
		this.dhPedidoColeta = dhPedidoColeta;
	}
	public String getTpPedidoColeta_description() {
		return tpPedidoColeta_description;
	}
	public void setTpPedidoColeta_description(String tpPedidoColeta_description) {
		this.tpPedidoColeta_description = tpPedidoColeta_description;
	}
	public String getNmSolicitante() {
		return nmSolicitante;
	}
	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}
	public String getTpModoPedidoColeta_description() {
		return tpModoPedidoColeta_description;
	}
	public void setTpModoPedidoColeta_description(String tpModoPedidoColeta_description) {
		this.tpModoPedidoColeta_description = tpModoPedidoColeta_description;
	}
	public String getCliente_nrIdentificacaoFormatado() {
		return cliente_nrIdentificacaoFormatado;
	}
	public void setCliente_nrIdentificacaoFormatado(String cliente_nrIdentificacaoFormatado) {
		this.cliente_nrIdentificacaoFormatado = cliente_nrIdentificacaoFormatado;
	}
	public String getCliente_nmPessoa() {
		return cliente_nmPessoa;
	}
	public void setCliente_nmPessoa(String cliente_nmPessoa) {
		this.cliente_nmPessoa = cliente_nmPessoa;
	}
	public String getEnderecoComComplemento() {
		return enderecoComComplemento;
	}
	public void setEnderecoComComplemento(String enderecoComComplemento) {
		this.enderecoComComplemento = enderecoComComplemento;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public DateTime getDhColetaDisponivel() {
		return dhColetaDisponivel;
	}
	public void setDhColetaDisponivel(DateTime dhColetaDisponivel) {
		this.dhColetaDisponivel = dhColetaDisponivel;
	}
	public DateTime getHrLimiteColeta() {
		return hrLimiteColeta;
	}
	public void setHrLimiteColeta(DateTime hrLimiteColeta) {
		this.hrLimiteColeta = hrLimiteColeta;
	}
	public YearMonthDay getDtPrevisaoColeta() {
		return dtPrevisaoColeta;
	}
	public void setDtPrevisaoColeta(YearMonthDay dtPrevisaoColeta) {
		this.dtPrevisaoColeta = dtPrevisaoColeta;
	}
	public String getNrTelefoneCliente() {
		return nrTelefoneCliente;
	}
	public void setNrTelefoneCliente(String nrTelefoneCliente) {
		this.nrTelefoneCliente = nrTelefoneCliente;
	}
	public String getNmContatoCliente() {
		return nmContatoCliente;
	}
	public void setNmContatoCliente(String nmContatoCliente) {
		this.nmContatoCliente = nmContatoCliente;
	}
	public String getDsRota() {
		return dsRota;
	}
	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}
	public String getDsRegiaoColetaEntregaFil() {
		return dsRegiaoColetaEntregaFil;
	}
	public void setDsRegiaoColetaEntregaFil(String dsRegiaoColetaEntregaFil) {
		this.dsRegiaoColetaEntregaFil = dsRegiaoColetaEntregaFil;
	}
	public Integer getQtTotalVolumesVerificado() {
		return qtTotalVolumesVerificado;
	}
	public void setQtTotalVolumesVerificado(Integer qtTotalVolumesVerificado) {
		this.qtTotalVolumesVerificado = qtTotalVolumesVerificado;
	}
	public BigDecimal getPsTotalInformado() {
		return psTotalInformado;
	}
	public void setPsTotalInformado(BigDecimal psTotalInformado) {
		this.psTotalInformado = psTotalInformado;
	}
	public BigDecimal getVlTotalVerificado() {
		return vlTotalVerificado;
	}
	public void setVlTotalVerificado(BigDecimal vlTotalVerificado) {
		this.vlTotalVerificado = vlTotalVerificado;
	}
	public BigDecimal getPsTotalVerificado() {
		return psTotalVerificado;
	}
	public void setPsTotalVerificado(BigDecimal psTotalVerificado) {
		this.psTotalVerificado = psTotalVerificado;
	}
	public String getNrFrota() {
		return nrFrota;
	}
	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}
	public String getNrIdentificador() {
		return nrIdentificador;
	}
	public void setNrIdentificador(String nrIdentificador) {
		this.nrIdentificador = nrIdentificador;
	}
	public String getFilialByIdFilialOrigem_sgFilial() {
		return filialByIdFilialOrigem_sgFilial;
	}
	public void setFilialByIdFilialOrigem_sgFilial(String filialByIdFilialOrigem_sgFilial) {
		this.filialByIdFilialOrigem_sgFilial = filialByIdFilialOrigem_sgFilial;
	}
	public Long getNrControleCarga() {
		return nrControleCarga;
	}
	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}
	public String getManifestoColeta_sgFilial() {
		return manifestoColeta_sgFilial;
	}
	public void setManifestoColeta_sgFilial(String manifestoColeta_sgFilial) {
		this.manifestoColeta_sgFilial = manifestoColeta_sgFilial;
	}
	public Integer getManifestoColeta_nrManifesto() {
		return manifestoColeta_nrManifesto;
	}
	public void setManifestoColeta_nrManifesto(Integer manifestoColeta_nrManifesto) {
		this.manifestoColeta_nrManifesto = manifestoColeta_nrManifesto;
	}
	public Boolean getBlProdutoDiferenciado() {
		return blProdutoDiferenciado;
	}
	public void setBlProdutoDiferenciado(Boolean blProdutoDiferenciado) {
		this.blProdutoDiferenciado = blProdutoDiferenciado;
	}
	public String getObPedidoColeta() {
		return obPedidoColeta;
	}
	public void setObPedidoColeta(String obPedidoColeta) {
		this.obPedidoColeta = obPedidoColeta;
	}
	public String getObsCancelamento() {
		return obsCancelamento;
	}
	public void setObsCancelamento(String obsCancelamento) {
		this.obsCancelamento = obsCancelamento;
	}
	public Long getIdControleCarga() {
		return idControleCarga;
	}
	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}}
