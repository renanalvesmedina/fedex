package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.workflow.PendenciaDTO;

public class DescontoRfcDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idDescontoRfc;
	private Long nrDescontoRfc;

	private String nrIdentificacaoMeioTransp;
	private String nrIdentificacaoSemiReboque;
	private String obDesconto;

	private BigDecimal vlTotalDesconto;
	private BigDecimal vlSaldoDevedor;
	private Long nrControleCarga;
	private YearMonthDay dtInicioDesconto;
	private Integer qtParcelas;
	private BigDecimal pcDesconto;
	private Integer qtDias;
	private BigDecimal vlFixoParcela;
	private DomainValue blParcelado;
	private DomainValue tpSituacao;
	private DomainValue tpOperacao;
	private DomainValue tpSituacaoPendencia;
	private YearMonthDay dtAtualizacao;
	private Integer nrReciboIndenizacao;

	private ReciboFreteCarreteiroSuggestDTO recibo;
	private ControleCargaSuggestDTO controleCarga;
	private MeioTransporteSuggestDTO meioTransporte;
	private TipoDescontoRfcDTO tipoDescontoRfc;
	private ProprietarioDTO proprietario;
	private FilialSuggestDTO filial;
	private PendenciaDTO pendencia;

	private List<ParcelaDescontoRfcDTO> parcelas;

	private List<Map<String, Object>> files;

	private boolean disabled;
	private boolean cancelar;
	private boolean workflow;
	private boolean bloquearParcelas;
	private boolean prioritario;
	
	public Long getIdDescontoRfc() {
		return idDescontoRfc;
	}

	public void setIdDescontoRfc(Long idDescontoRfc) {
		this.idDescontoRfc = idDescontoRfc;
	}

	public Long getNrDescontoRfc() {
		return nrDescontoRfc;
	}

	public void setNrDescontoRfc(Long nrDescontoRfc) {
		this.nrDescontoRfc = nrDescontoRfc;
	}

	public String getNrIdentificacaoMeioTransp() {
		return nrIdentificacaoMeioTransp;
	}

	public void setNrIdentificacaoMeioTransp(String nrIdentificacaoMeioTransp) {
		this.nrIdentificacaoMeioTransp = nrIdentificacaoMeioTransp;
	}

	public String getNrIdentificacaoSemiReboque() {
		return nrIdentificacaoSemiReboque;
	}

	public void setNrIdentificacaoSemiReboque(String nrIdentificacaoSemiReboque) {
		this.nrIdentificacaoSemiReboque = nrIdentificacaoSemiReboque;
	}

	public String getObDesconto() {
		return obDesconto;
	}

	public void setObDesconto(String obDesconto) {
		this.obDesconto = obDesconto;
	}

	public BigDecimal getVlTotalDesconto() {
		return vlTotalDesconto;
	}

	public void setVlTotalDesconto(BigDecimal vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}

	public BigDecimal getVlSaldoDevedor() {
		return vlSaldoDevedor;
	}

	public void setVlSaldoDevedor(BigDecimal vlSaldoDevedor) {
		this.vlSaldoDevedor = vlSaldoDevedor;
	}

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public YearMonthDay getDtInicioDesconto() {
		return dtInicioDesconto;
	}

	public void setDtInicioDesconto(YearMonthDay dtInicioDesconto) {
		this.dtInicioDesconto = dtInicioDesconto;
	}

	public Integer getQtParcelas() {
		return qtParcelas;
	}

	public void setQtParcelas(Integer qtParcelas) {
		this.qtParcelas = qtParcelas;
	}

	public BigDecimal getPcDesconto() {
		return pcDesconto;
	}

	public void setPcDesconto(BigDecimal pcDesconto) {
		this.pcDesconto = pcDesconto;
	}

	public Integer getQtDias() {
		return qtDias;
	}

	public void setQtDias(Integer qtDias) {
		this.qtDias = qtDias;
	}

	public BigDecimal getVlFixoParcela() {
		return vlFixoParcela;
	}

	public void setVlFixoParcela(BigDecimal vlFixoParcela) {
		this.vlFixoParcela = vlFixoParcela;
	}

	public DomainValue getBlParcelado() {
		return blParcelado;
	}

	public void setBlParcelado(DomainValue blParcelado) {
		this.blParcelado = blParcelado;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public Integer getNrReciboIndenizacao() {
		return nrReciboIndenizacao;
	}

	public void setNrReciboIndenizacao(Integer nrReciboIndenizacao) {
		this.nrReciboIndenizacao = nrReciboIndenizacao;
	}

	public ReciboFreteCarreteiroSuggestDTO getRecibo() {
		return recibo;
	}

	public void setRecibo(ReciboFreteCarreteiroSuggestDTO recibo) {
		this.recibo = recibo;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public TipoDescontoRfcDTO getTipoDescontoRfc() {
		return tipoDescontoRfc;
	}

	public void setTipoDescontoRfc(TipoDescontoRfcDTO tipoDescontoRfc) {
		this.tipoDescontoRfc = tipoDescontoRfc;
	}

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public PendenciaDTO getPendencia() {
		return pendencia;
	}

	public void setPendencia(PendenciaDTO pendencia) {
		this.pendencia = pendencia;
	}

	public List<ParcelaDescontoRfcDTO> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaDescontoRfcDTO> parcelas) {
		this.parcelas = parcelas;
	}

	public List<Map<String, Object>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, Object>> files) {
		this.files = files;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isCancelar() {
		return cancelar;
	}

	public void setCancelar(boolean cancelar) {
		this.cancelar = cancelar;
	}

	public boolean isWorkflow() {
		return workflow;
	}

	public void setWorkflow(boolean workflow) {
		this.workflow = workflow;
	}

	public boolean isBloquearParcelas() {
		return bloquearParcelas;
	}

	public void setBloquearParcelas(boolean bloquearParcelas) {
		this.bloquearParcelas = bloquearParcelas;
	}

	public boolean isPrioritario() {
		return prioritario;
	}

	public void setPrioritario(boolean prioritario) {
		this.prioritario = prioritario;
	}

}
