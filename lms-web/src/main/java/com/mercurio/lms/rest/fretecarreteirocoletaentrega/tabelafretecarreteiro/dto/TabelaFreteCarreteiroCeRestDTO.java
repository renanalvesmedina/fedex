package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class TabelaFreteCarreteiroCeRestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private boolean disabled;
	private boolean ended;

	private Long idTabelaFreteCarreteiroCe;
	private Long nrTabelaFreteCarreteiroCe;

	private Long idTabelaClonada;
	private Long nrTabelaClonada;
	
	private String sgFilialTabelaClonada;
	
	private DateTime dhVigenciaInicial;
	private DateTime dhVigenciaFinal;
	private YearMonthDay dtCriacao;
	private YearMonthDay dtAtualizacao;

	private Boolean blDomingo;
	private Boolean blSegunda;
	private Boolean blTerca;
	private Boolean blQuarta;
	private Boolean blQuinta;
	private Boolean blSexta;
	private Boolean blSabado;
	private Boolean blDedicado;
	private Boolean blDescontaFrete;

	private DomainValue tpVinculo;
	private DomainValue tpOperacao;
	private DomainValue tpModal;
	private DomainValue tpPeso;

	private BigDecimal pcPremioCte;
	private BigDecimal pcPremioEvento;
	private BigDecimal pcPremioDiaria;
	private BigDecimal pcPremioVolume;
	private BigDecimal pcPremioSaida;
	private BigDecimal pcPremioFreteBruto;
	private BigDecimal pcPremioFreteLiq;
	private BigDecimal pcPremioMercadoria;

	private FilialSuggestDTO filial;
	private UsuarioDTO usuarioCriacao;
	private UsuarioDTO usuarioAlteracao;

	private String obTabelaFrete;

	private List<TabelaFcValoresRestDTO> listTabelaFcValoresRest;

	private String messageType;
	private String messageTypeIcon;
	private String messageText;

	private List<Map<String, Object>> files;
	
	public Long getIdTabelaFreteCarreteiroCe() {
		return idTabelaFreteCarreteiroCe;
	}

	public void setIdTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		this.idTabelaFreteCarreteiroCe = idTabelaFreteCarreteiroCe;
	}

	public Long getNrTabelaFreteCarreteiroCe() {
		return nrTabelaFreteCarreteiroCe;
	}

	public void setNrTabelaFreteCarreteiroCe(Long nrTabelaFreteCarreteiroCe) {
		this.nrTabelaFreteCarreteiroCe = nrTabelaFreteCarreteiroCe;
	}

	public DomainValue getTpVinculo() {
		return tpVinculo;
	}

	public void setTpVinculo(DomainValue tpVinculo) {
		this.tpVinculo = tpVinculo;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public UsuarioDTO getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(UsuarioDTO usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public UsuarioDTO getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioDTO usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Boolean getBlDomingo() {
		return blDomingo;
	}

	public void setBlDomingo(Boolean blDomingo) {
		this.blDomingo = blDomingo;
	}

	public Boolean getBlSegunda() {
		return blSegunda;
	}

	public void setBlSegunda(Boolean blSegunda) {
		this.blSegunda = blSegunda;
	}

	public Boolean getBlTerca() {
		return blTerca;
	}

	public void setBlTerca(Boolean blTerca) {
		this.blTerca = blTerca;
	}

	public Boolean getBlQuarta() {
		return blQuarta;
	}

	public void setBlQuarta(Boolean blQuarta) {
		this.blQuarta = blQuarta;
	}

	public Boolean getBlQuinta() {
		return blQuinta;
	}

	public void setBlQuinta(Boolean blQuinta) {
		this.blQuinta = blQuinta;
	}

	public Boolean getBlSexta() {
		return blSexta;
	}

	public void setBlSexta(Boolean blSexta) {
		this.blSexta = blSexta;
	}

	public Boolean getBlSabado() {
		return blSabado;
	}

	public void setBlSabado(Boolean blSabado) {
		this.blSabado = blSabado;
	}

	public Boolean getBlDedicado() {
		return blDedicado;
	}

	public void setBlDedicado(Boolean blDedicado) {
		this.blDedicado = blDedicado;
	}

	public Boolean getBlDescontaFrete() {
		return blDescontaFrete;
	}

	public void setBlDescontaFrete(Boolean blDescontaFrete) {
		this.blDescontaFrete = blDescontaFrete;
	}

	public String getObTabelaFrete() {
		return obTabelaFrete;
	}

	public void setObTabelaFrete(String obTabelaFrete) {
		this.obTabelaFrete = obTabelaFrete;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}
	
	public DomainValue getTpPeso() {
		return tpPeso;
	}

	public void setTpPeso(DomainValue tpPeso) {
		this.tpPeso = tpPeso;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<TabelaFcValoresRestDTO> getListTabelaFcValoresRest() {
		return listTabelaFcValoresRest;
	}

	public void setListTabelaFcValoresRest(
			List<TabelaFcValoresRestDTO> listTabelaFcValoresRest) {
		this.listTabelaFcValoresRest = listTabelaFcValoresRest;
	}

	public DateTime getDhVigenciaInicial() {
		return dhVigenciaInicial;
	}

	public void setDhVigenciaInicial(DateTime dhVigenciaInicial) {
		this.dhVigenciaInicial = dhVigenciaInicial;
	}

	public DateTime getDhVigenciaFinal() {
		return dhVigenciaFinal;
	}

	public void setDhVigenciaFinal(DateTime dhVigenciaFinal) {
		this.dhVigenciaFinal = dhVigenciaFinal;
	}

	public YearMonthDay getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(YearMonthDay dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public BigDecimal getPcPremioCte() {
		return pcPremioCte;
	}

	public void setPcPremioCte(BigDecimal pcPremioCte) {
		this.pcPremioCte = pcPremioCte;
	}

	public BigDecimal getPcPremioEvento() {
		return pcPremioEvento;
	}

	public void setPcPremioEvento(BigDecimal pcPremioEvento) {
		this.pcPremioEvento = pcPremioEvento;
	}

	public BigDecimal getPcPremioDiaria() {
		return pcPremioDiaria;
	}

	public void setPcPremioDiaria(BigDecimal pcPremioDiaria) {
		this.pcPremioDiaria = pcPremioDiaria;
	}

	public BigDecimal getPcPremioVolume() {
		return pcPremioVolume;
	}

	public void setPcPremioVolume(BigDecimal pcPremioVolume) {
		this.pcPremioVolume = pcPremioVolume;
	}

	public BigDecimal getPcPremioSaida() {
		return pcPremioSaida;
	}

	public void setPcPremioSaida(BigDecimal pcPremioSaida) {
		this.pcPremioSaida = pcPremioSaida;
	}

	public BigDecimal getPcPremioFreteBruto() {
		return pcPremioFreteBruto;
	}

	public void setPcPremioFreteBruto(BigDecimal pcPremioFreteBruto) {
		this.pcPremioFreteBruto = pcPremioFreteBruto;
	}

	public BigDecimal getPcPremioFreteLiq() {
		return pcPremioFreteLiq;
	}

	public void setPcPremioFreteLiq(BigDecimal pcPremioFreteLiq) {
		this.pcPremioFreteLiq = pcPremioFreteLiq;
	}

	public BigDecimal getPcPremioMercadoria() {
		return pcPremioMercadoria;
	}

	public void setPcPremioMercadoria(BigDecimal pcPremioMercadoria) {
		this.pcPremioMercadoria = pcPremioMercadoria;
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

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public Long getIdTabelaClonada() {
		return idTabelaClonada;
	}

	public void setIdTabelaClonada(Long idTabelaClonada) {
		this.idTabelaClonada = idTabelaClonada;
	}

	public Long getNrTabelaClonada() {
		return nrTabelaClonada;
	}

	public void setNrTabelaClonada(Long nrTabelaClonada) {
		this.nrTabelaClonada = nrTabelaClonada;
	}

	public String getSgFilialTabelaClonada() {
		return sgFilialTabelaClonada;
	}

	public void setSgFilialTabelaClonada(String sgFilialTabelaClonada) {
		this.sgFilialTabelaClonada = sgFilialTabelaClonada;
	}

	public List<Map<String, Object>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, Object>> files) {
		this.files = files;
	}

}