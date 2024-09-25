package com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class NotaCreditoPadraoRestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idNotaCredito;
	private Long nrNotaCredito;
	private Long idPendencia;

	private Long idControleCarga;

	private FilialSuggestDTO filial;
	private ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiro;
	private ProprietarioDTO proprietario;
	private MeioTransporteSuggestDTO meioTransporte;

	private DateTime dhEmissao;
	private DateTime dhGeracao;

	private DomainValue tpNotaCredito;
	private DomainValue tpSituacao;
	private DomainValue tpAcrescimoDesconto;

	private String obNotaCredito;
	private String dsSimboloMoeda;
	private String tpSituacaoPendenciaDescricao;

	private BigDecimal vlTotal;
	private BigDecimal vlTotalItens;
	private BigDecimal vlAcrescimoDesconto;
	private BigDecimal vlDescUsoEquipamento;

	private boolean disabledRegerar;
	private boolean disabledEmitir;
	private boolean disabledVisualizar;
	private boolean disabledSalvar;
	private boolean disabledAcrescimoDesconto;
	private boolean disabledTpAcrescimoDesconto;
	private boolean workflow;

	private List<Map<String, Object>> files;

	private String messageType;
	private String messageTypeIcon;
	private String messageText;

	public Long getIdNotaCredito() {
		return idNotaCredito;
	}

	public void setIdNotaCredito(Long idNotaCredito) {
		this.idNotaCredito = idNotaCredito;
	}

	public Long getNrNotaCredito() {
		return nrNotaCredito;
	}

	public void setNrNotaCredito(Long nrNotaCredito) {
		this.nrNotaCredito = nrNotaCredito;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public ReciboFreteCarreteiroSuggestDTO getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(
			ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public List<Map<String, Object>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, Object>> files) {
		this.files = files;
	}

	public String getObNotaCredito() {
		return obNotaCredito;
	}

	public void setObNotaCredito(String obNotaCredito) {
		this.obNotaCredito = obNotaCredito;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public String getDsSimboloMoeda() {
		return dsSimboloMoeda;
	}

	public void setDsSimboloMoeda(String dsSimboloMoeda) {
		this.dsSimboloMoeda = dsSimboloMoeda;
	}

	public DomainValue getTpAcrescimoDesconto() {
		return tpAcrescimoDesconto;
	}

	public void setTpAcrescimoDesconto(DomainValue tpAcrescimoDesconto) {
		this.tpAcrescimoDesconto = tpAcrescimoDesconto;
	}

	public BigDecimal getVlAcrescimoDesconto() {
		return vlAcrescimoDesconto;
	}

	public void setVlAcrescimoDesconto(BigDecimal vlAcrescimoDesconto) {
		this.vlAcrescimoDesconto = vlAcrescimoDesconto;
	}

	public String getTpSituacaoPendenciaDescricao() {
		return tpSituacaoPendenciaDescricao;
	}

	public void setTpSituacaoPendenciaDescricao(
			String tpSituacaoPendenciaDescricao) {
		this.tpSituacaoPendenciaDescricao = tpSituacaoPendenciaDescricao;
	}

	public boolean isWorkflow() {
		return workflow;
	}

	public void setWorkflow(boolean workflow) {
		this.workflow = workflow;
	}

	public boolean isDisabledSalvar() {
		return disabledSalvar;
	}

	public void setDisabledSalvar(boolean disabledSalvar) {
		this.disabledSalvar = disabledSalvar;
	}

	public boolean isDisabledAcrescimoDesconto() {
		return disabledAcrescimoDesconto;
	}

	public void setDisabledAcrescimoDesconto(boolean disabledAcrescimoDesconto) {
		this.disabledAcrescimoDesconto = disabledAcrescimoDesconto;
	}

	public boolean isDisabledTpAcrescimoDesconto() {
		return disabledTpAcrescimoDesconto;
	}

	public void setDisabledTpAcrescimoDesconto(
			boolean disabledTpAcrescimoDesconto) {
		this.disabledTpAcrescimoDesconto = disabledTpAcrescimoDesconto;
	}

	public BigDecimal getVlTotalItens() {
		return vlTotalItens;
	}

	public void setVlTotalItens(BigDecimal vlTotalItens) {
		this.vlTotalItens = vlTotalItens;
	}

	public Long getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(Long idPendencia) {
		this.idPendencia = idPendencia;
	}

	public DomainValue getTpNotaCredito() {
		return tpNotaCredito;
	}

	public void setTpNotaCredito(DomainValue tpNotaCredito) {
		this.tpNotaCredito = tpNotaCredito;
	}

	public BigDecimal getVlDescUsoEquipamento() {
		return vlDescUsoEquipamento;
	}

	public void setVlDescUsoEquipamento(BigDecimal vlDescUsoEquipamento) {
		this.vlDescUsoEquipamento = vlDescUsoEquipamento;
	}

	public boolean isDisabledEmitir() {
		return disabledEmitir;
	}

	public void setDisabledEmitir(boolean disabledEmitir) {
		this.disabledEmitir = disabledEmitir;
	}

	public boolean isDisabledVisualizar() {
		return disabledVisualizar;
	}

	public void setDisabledVisualizar(boolean disabledVisualizar) {
		this.disabledVisualizar = disabledVisualizar;
	}

	public boolean isDisabledRegerar() {
		return disabledRegerar;
	}

	public void setDisabledRegerar(boolean disabledRegerar) {
		this.disabledRegerar = disabledRegerar;
	}

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageTypeIcon() {
		return messageTypeIcon;
	}

	public void setMessageTypeIcon(String messageTypeIcon) {
		this.messageTypeIcon = messageTypeIcon;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}