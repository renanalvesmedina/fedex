package com.mercurio.lms.rest.contasareceber.consultarfaturasvencidas.dto;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class FaturasVencidasVencerFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filialResponsavel;
	private DomainValue classificacaoCliente;
	private DomainValue cobrancaCentralizada;
	private DomainValue tipoCliente;
	private DomainValue tipoCobranca;
	private DomainValue clienteComPreFatura;
	private DomainValue clienteEmailFaturamento;
	private DomainValue clienteEmailCobranca;
	private String devedoresListar;
	private String devedoresExcluir;
	private Long idServico;
	private DomainValue tipoDocumento;
	private DomainValue tipoFrete;

	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;

	private DomainValue tipoConhecimento;
	private DomainValue tipoCalculo;
	private Long idRegional;

	private YearMonthDay dtEmissaoFatInicial;
	private YearMonthDay dtEmissaoFatFinal;

	private FilialSuggestDTO idFilialCobranca;
	private YearMonthDay dtEmissaoBolInicial;
	private YearMonthDay dtEmissaoBolFinal;

	private Long nrFaturaInicial;
	private Long nrFaturaFinal;
	private YearMonthDay dtVencimentoInicial;
	private YearMonthDay dtVencimentoFinal;

	private String preFatura;
	private YearMonthDay dtNegSerasaInicial;
	private YearMonthDay dtNegSerasaFinal;

	private YearMonthDay dtEnvioCobTerceiraInicial;
	private YearMonthDay dtEnvioCobTerceiraFinal;

	private YearMonthDay dtPagtoCobTerceiraInicial;
	private YearMonthDay dtPagtoCobTerceiraFinal;

	private YearMonthDay dtDevolCobTerceiraInicial;
	private YearMonthDay dtDevolCobTerceiraFinal;

	private DomainValue situacaoFatura;
	private YearMonthDay dtExecSerasaInicial;
	private YearMonthDay dtExecSerasaFinal;

	private DomainValue situacaoBoleto;
	private YearMonthDay dtEnvioInicial;
	private YearMonthDay dtEnvioFinal;

	private DomainValue modal;
	private YearMonthDay dtRecebimentoInicial;
	private YearMonthDay dtRecebimentoFinal;

	private DomainValue abrangencia;
	private YearMonthDay dtDevolucaoInicial;
	private YearMonthDay dtDevolucaoFinal;

	private DomainValue situacaoAprovacao;
	private YearMonthDay dtErroInicial;
	private YearMonthDay dtErroFinal;

	private List<Map<String, Object>> regionais;
	private List<Map<String, Object>> servicos;

	private DomainValue comTratativa;
	
	private YearMonthDay dtLiquidacaoInicial;
	private YearMonthDay dtLiquidacaoFinal;

	public FilialSuggestDTO getFilialResponsavel() {
		return filialResponsavel;
	}

	public void setFilialResponsavel(FilialSuggestDTO filialResponsavel) {
		this.filialResponsavel = filialResponsavel;
	}

	public DomainValue getClassificacaoCliente() {
		return classificacaoCliente;
	}

	public void setClassificacaoCliente(DomainValue classificacaoCliente) {
		this.classificacaoCliente = classificacaoCliente;
	}

	public DomainValue getCobrancaCentralizada() {
		return cobrancaCentralizada;
	}

	public void setCobrancaCentralizada(DomainValue cobrancaCentralizada) {
		this.cobrancaCentralizada = cobrancaCentralizada;
	}

	public DomainValue getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(DomainValue tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public DomainValue getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(DomainValue tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public DomainValue getClienteComPreFatura() {
		return clienteComPreFatura;
	}

	public void setClienteComPreFatura(DomainValue clienteComPreFatura) {
		this.clienteComPreFatura = clienteComPreFatura;
	}

	public DomainValue getClienteEmailFaturamento() {
		return clienteEmailFaturamento;
	}

	public void setClienteEmailFaturamento(DomainValue clienteEmailFaturamento) {
		this.clienteEmailFaturamento = clienteEmailFaturamento;
	}

	public DomainValue getClienteEmailCobranca() {
		return clienteEmailCobranca;
	}

	public void setClienteEmailCobranca(DomainValue clienteEmailCobranca) {
		this.clienteEmailCobranca = clienteEmailCobranca;
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

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public DomainValue getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(DomainValue tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public DomainValue getTipoFrete() {
		return tipoFrete;
	}

	public void setTipoFrete(DomainValue tipoFrete) {
		this.tipoFrete = tipoFrete;
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

	public DomainValue getTipoConhecimento() {
		return tipoConhecimento;
	}

	public void setTipoConhecimento(DomainValue tipoConhecimento) {
		this.tipoConhecimento = tipoConhecimento;
	}

	public DomainValue getTipoCalculo() {
		return tipoCalculo;
	}

	public void setTipoCalculo(DomainValue tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	public Long getIdRegional() {
		return idRegional;
	}

	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}

	public YearMonthDay getDtEmissaoFatInicial() {
		return dtEmissaoFatInicial;
	}

	public void setDtEmissaoFatInicial(YearMonthDay dtEmissaoFatInicial) {
		this.dtEmissaoFatInicial = dtEmissaoFatInicial;
	}

	public YearMonthDay getDtEmissaoFatFinal() {
		return dtEmissaoFatFinal;
	}

	public void setDtEmissaoFatFinal(YearMonthDay dtEmissaoFatFinal) {
		this.dtEmissaoFatFinal = dtEmissaoFatFinal;
	}

	public FilialSuggestDTO getIdFilialCobranca() {
		return idFilialCobranca;
	}

	public void setIdFilialCobranca(FilialSuggestDTO idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}

	public YearMonthDay getDtEmissaoBolInicial() {
		return dtEmissaoBolInicial;
	}

	public void setDtEmissaoBolInicial(YearMonthDay dtEmissaoBolInicial) {
		this.dtEmissaoBolInicial = dtEmissaoBolInicial;
	}

	public YearMonthDay getDtEmissaoBolFinal() {
		return dtEmissaoBolFinal;
	}

	public void setDtEmissaoBolFinal(YearMonthDay dtEmissaoBolFinal) {
		this.dtEmissaoBolFinal = dtEmissaoBolFinal;
	}

	public Long getNrFaturaInicial() {
		return nrFaturaInicial;
	}

	public void setNrFaturaInicial(Long nrFaturaInicial) {
		this.nrFaturaInicial = nrFaturaInicial;
	}

	public Long getNrFaturaFinal() {
		return nrFaturaFinal;
	}

	public void setNrFaturaFinal(Long nrFaturaFinal) {
		this.nrFaturaFinal = nrFaturaFinal;
	}

	public YearMonthDay getDtVencimentoInicial() {
		return dtVencimentoInicial;
	}

	public void setDtVencimentoInicial(YearMonthDay dtVencimentoInicial) {
		this.dtVencimentoInicial = dtVencimentoInicial;
	}

	public YearMonthDay getDtVencimentoFinal() {
		return dtVencimentoFinal;
	}

	public void setDtVencimentoFinal(YearMonthDay dtVencimentoFinal) {
		this.dtVencimentoFinal = dtVencimentoFinal;
	}

	public String getPreFatura() {
		return preFatura;
	}

	public void setPreFatura(String preFatura) {
		this.preFatura = preFatura;
	}

	public YearMonthDay getDtNegSerasaInicial() {
		return dtNegSerasaInicial;
	}

	public void setDtNegSerasaInicial(YearMonthDay dtNegSerasaInicial) {
		this.dtNegSerasaInicial = dtNegSerasaInicial;
	}

	public YearMonthDay getDtNegSerasaFinal() {
		return dtNegSerasaFinal;
	}

	public void setDtNegSerasaFinal(YearMonthDay dtNegSerasaFinal) {
		this.dtNegSerasaFinal = dtNegSerasaFinal;
	}

	public DomainValue getSituacaoFatura() {
		return situacaoFatura;
	}

	public void setSituacaoFatura(DomainValue situacaoFatura) {
		this.situacaoFatura = situacaoFatura;
	}

	public YearMonthDay getDtExecSerasaInicial() {
		return dtExecSerasaInicial;
	}

	public void setDtExecSerasaInicial(YearMonthDay dtExecSerasaInicial) {
		this.dtExecSerasaInicial = dtExecSerasaInicial;
	}

	public YearMonthDay getDtExecSerasaFinal() {
		return dtExecSerasaFinal;
	}

	public void setDtExecSerasaFinal(YearMonthDay dtExecSerasaFinal) {
		this.dtExecSerasaFinal = dtExecSerasaFinal;
	}

	public DomainValue getSituacaoBoleto() {
		return situacaoBoleto;
	}

	public void setSituacaoBoleto(DomainValue situacaoBoleto) {
		this.situacaoBoleto = situacaoBoleto;
	}

	public YearMonthDay getDtEnvioInicial() {
		return dtEnvioInicial;
	}

	public void setDtEnvioInicial(YearMonthDay dtEnvioInicial) {
		this.dtEnvioInicial = dtEnvioInicial;
	}

	public YearMonthDay getDtEnvioFinal() {
		return dtEnvioFinal;
	}

	public void setDtEnvioFinal(YearMonthDay dtEnvioFinal) {
		this.dtEnvioFinal = dtEnvioFinal;
	}

	public DomainValue getModal() {
		return modal;
	}

	public void setModal(DomainValue modal) {
		this.modal = modal;
	}

	public YearMonthDay getDtRecebimentoInicial() {
		return dtRecebimentoInicial;
	}

	public void setDtRecebimentoInicial(YearMonthDay dtRecebimentoInicial) {
		this.dtRecebimentoInicial = dtRecebimentoInicial;
	}

	public YearMonthDay getDtRecebimentoFinal() {
		return dtRecebimentoFinal;
	}

	public void setDtRecebimentoFinal(YearMonthDay dtRecebimentoFinal) {
		this.dtRecebimentoFinal = dtRecebimentoFinal;
	}

	public DomainValue getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(DomainValue abrangencia) {
		this.abrangencia = abrangencia;
	}

	public YearMonthDay getDtDevolucaoInicial() {
		return dtDevolucaoInicial;
	}

	public void setDtDevolucaoInicial(YearMonthDay dtDevolucaoInicial) {
		this.dtDevolucaoInicial = dtDevolucaoInicial;
	}

	public YearMonthDay getDtDevolucaoFinal() {
		return dtDevolucaoFinal;
	}

	public void setDtDevolucaoFinal(YearMonthDay dtDevolucaoFinal) {
		this.dtDevolucaoFinal = dtDevolucaoFinal;
	}

	public DomainValue getSituacaoAprovacao() {
		return situacaoAprovacao;
	}

	public void setSituacaoAprovacao(DomainValue situacaoAprovacao) {
		this.situacaoAprovacao = situacaoAprovacao;
	}

	public YearMonthDay getDtErroInicial() {
		return dtErroInicial;
	}

	public void setDtErroInicial(YearMonthDay dtErroInicial) {
		this.dtErroInicial = dtErroInicial;
	}

	public YearMonthDay getDtErroFinal() {
		return dtErroFinal;
	}

	public void setDtErroFinal(YearMonthDay dtErroFinal) {
		this.dtErroFinal = dtErroFinal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Map<String, Object>> getRegionais() {
		return regionais;
	}

	public void setRegionais(List<Map<String, Object>> regionais) {
		this.regionais = regionais;
	}

	public List<Map<String, Object>> getServicos() {
		return servicos;
	}

	public void setServicos(List<Map<String, Object>> servicos) {
		this.servicos = servicos;
	}

	public YearMonthDay getDtEnvioCobTerceiraInicial() {
		return dtEnvioCobTerceiraInicial;
	}

	public void setDtEnvioCobTerceiraInicial(
			YearMonthDay dtEnvioCobTerceiraInicial) {
		this.dtEnvioCobTerceiraInicial = dtEnvioCobTerceiraInicial;
	}

	public YearMonthDay getDtEnvioCobTerceiraFinal() {
		return dtEnvioCobTerceiraFinal;
	}

	public void setDtEnvioCobTerceiraFinal(YearMonthDay dtEnvioCobTerceiraFinal) {
		this.dtEnvioCobTerceiraFinal = dtEnvioCobTerceiraFinal;
	}

	public YearMonthDay getDtPagtoCobTerceiraInicial() {
		return dtPagtoCobTerceiraInicial;
	}

	public void setDtPagtoCobTerceiraInicial(
			YearMonthDay dtPagtoCobTerceiraInicial) {
		this.dtPagtoCobTerceiraInicial = dtPagtoCobTerceiraInicial;
	}

	public YearMonthDay getDtPagtoCobTerceiraFinal() {
		return dtPagtoCobTerceiraFinal;
	}

	public void setDtPagtoCobTerceiraFinal(YearMonthDay dtPagtoCobTerceiraFinal) {
		this.dtPagtoCobTerceiraFinal = dtPagtoCobTerceiraFinal;
	}

	public YearMonthDay getDtDevolCobTerceiraInicial() {
		return dtDevolCobTerceiraInicial;
	}

	public void setDtDevolCobTerceiraInicial(
			YearMonthDay dtDevolCobTerceiraInicial) {
		this.dtDevolCobTerceiraInicial = dtDevolCobTerceiraInicial;
	}

	public YearMonthDay getDtDevolCobTerceiraFinal() {
		return dtDevolCobTerceiraFinal;
	}

	public void setDtDevolCobTerceiraFinal(YearMonthDay dtDevolCobTerceiraFinal) {
		this.dtDevolCobTerceiraFinal = dtDevolCobTerceiraFinal;
	}

	public void setComTratativa(DomainValue comTratativa) {
		this.comTratativa = comTratativa;
	}

	public DomainValue getComTratativa() {
		return comTratativa;
	}

	public YearMonthDay getDtLiquidacaoInicial() {
		return dtLiquidacaoInicial;
	}

	public void setDtLiquidacaoInicial(YearMonthDay dtLiquidacaoInicial) {
		this.dtLiquidacaoInicial = dtLiquidacaoInicial;
	}

	public YearMonthDay getDtLiquidacaoFinal() {
		return dtLiquidacaoFinal;
	}

	public void setDtLiquidacaoFinal(YearMonthDay dtLiquidacaoFinal) {
		this.dtLiquidacaoFinal = dtLiquidacaoFinal;
	}
}
