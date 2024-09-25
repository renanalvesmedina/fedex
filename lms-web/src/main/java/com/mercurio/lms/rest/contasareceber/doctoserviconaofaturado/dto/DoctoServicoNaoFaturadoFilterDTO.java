package com.mercurio.lms.rest.contasareceber.doctoserviconaofaturado.dto;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
 
public class DoctoServicoNaoFaturadoFilterDTO extends BaseFilterDTO { 
	
	private static final long serialVersionUID = 1L; 
		
	private FilialSuggestDTO filial;
	private DomainValue classificacaoCliente;
	private DomainValue blCobrancaCentralizada;
	private DomainValue blAgendaTransferencia;
	private DomainValue tpCliente;
	private DomainValue tpCobranca;
	private DomainValue blPreFatura;
	private DomainValue estadoCobranca;
	private String devedoresListar;
	private String devedoresExcluir;
	private Long idRegional;
	private DomainValue tpDocumento;
	private DomainValue blBloqueado;
	private FilialSuggestDTO idFilialCobranca;
	private DomainValue tpFrete;
	private String tpModeloMensagem;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpConhecimento;
	private DomainValue blTransferenciaPendentes;
	private Long idServico;
	private DomainValue tpCalculo;
	private DomainValue filialCobDifResp;
	private List<Map<String,Object>> regionais;
	private List<Map<String,Object>> servicos;
	private boolean blExportaDtNatura;
	
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public DomainValue getClassificacaoCliente() {
		return classificacaoCliente;
	}
	public void setClassificacaoCliente(DomainValue classificacaoCliente) {
		this.classificacaoCliente = classificacaoCliente;
	}
	public DomainValue getBlCobrancaCentralizada() {
		return blCobrancaCentralizada;
	}
	public void setBlCobrancaCentralizada(DomainValue blCobrancaCentralizada) {
		this.blCobrancaCentralizada = blCobrancaCentralizada;
	}
	public DomainValue getTpCliente() {
		return tpCliente;
	}
	public void setTpCliente(DomainValue tpCliente) {
		this.tpCliente = tpCliente;
	}
	public DomainValue getTpCobranca() {
		return tpCobranca;
	}
	public void setTpCobranca(DomainValue tpCobranca) {
		this.tpCobranca = tpCobranca;
	}
	public DomainValue getBlPreFatura() {
		return blPreFatura;
	}
	public void setBlPreFatura(DomainValue blPreFatura) {
		this.blPreFatura = blPreFatura;
	}
	public String getDevedoresListar() {
		return devedoresListar;
	}
	public void setDevedoresListar(String devedoresListar) {
		this.devedoresListar = devedoresListar;
	}
	public String getDevedoresExcluir() {
		return devedoresExcluir;
	}
	public void setDevedoresExcluir(String devedoresExcluir) {
		this.devedoresExcluir = devedoresExcluir;
	}
	public Long getIdRegional() {
		return idRegional;
	}
	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}
	public DomainValue getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	public DomainValue getBlBloqueado() {
		return blBloqueado;
	}
	public void setBlBloqueado(DomainValue blBloqueado) {
		this.blBloqueado = blBloqueado;
	}
	public FilialSuggestDTO getIdFilialCobranca() {
		return idFilialCobranca;
	}
	public void setIdFilialCobranca(FilialSuggestDTO idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}
	public DomainValue getTpFrete() {
		return tpFrete;
	}
	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}
	public String getTpModeloMensagem() {
		return tpModeloMensagem;
	}
	public void setTpModeloMensagem(String tpModeloMensagem) {
		this.tpModeloMensagem = tpModeloMensagem;
	}
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	public DomainValue getTpConhecimento() {
		return tpConhecimento;
	}
	public void setTpConhecimento(DomainValue tpConhecimento) {
		this.tpConhecimento = tpConhecimento;
	}
	public DomainValue getBlTransferenciaPendentes() {
		return blTransferenciaPendentes;
	}
	public void setBlTransferenciaPendentes(DomainValue blTransferenciaPendentes) {
		this.blTransferenciaPendentes = blTransferenciaPendentes;
	}
	public Long getIdServico() {
		return idServico;
	}
	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}
	public DomainValue getTpCalculo() {
		return tpCalculo;
	}
	public void setTpCalculo(DomainValue tpCalculo) {
		this.tpCalculo = tpCalculo;
	}
	public DomainValue getFilialCobDifResp() {
		return filialCobDifResp;
	}
	public void setFilialCobDifResp(DomainValue filialCobDifResp) {
		this.filialCobDifResp = filialCobDifResp;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setRegionais(List<Map<String, Object>> regionais) {
		this.regionais = regionais;
	}
	public List<Map<String, Object>> getRegionais() {
		return regionais;
	}
	public void setServicos(List<Map<String, Object>> servicos) {
		this.servicos = servicos;
	}
	public List<Map<String, Object>> getServicos() {
		return servicos;
	}
	public DomainValue getBlAgendaTransferencia() {
		return blAgendaTransferencia;
	}
	public void setBlAgendaTransferencia(DomainValue blAgendaTransferencia) {
		this.blAgendaTransferencia = blAgendaTransferencia;
	}
	public DomainValue getEstadoCobranca() {
		return estadoCobranca;
	}
	public void setEstadoCobranca(DomainValue estadoCobranca) {
		this.estadoCobranca = estadoCobranca;
	}
	public boolean getBlExportaDtNatura() {
		return blExportaDtNatura;
	}
	public void setBlExportaDtNatura(boolean blExportaDtNatura) {
		this.blExportaDtNatura = blExportaDtNatura;
	}

} 
