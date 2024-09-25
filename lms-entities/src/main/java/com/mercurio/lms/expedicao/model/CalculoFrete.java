package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.lms.tabelaprecos.model.AjusteTarifa;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

public class CalculoFrete extends CalculoServico<Conhecimento> {

	private static final long serialVersionUID = 1L;
	
	private Boolean blCalculoFreteTabelaCheia = null; 
	
	//Atributos de entrada/saida
	private String nrCepColeta = null;
	private String nrCepEntrega = null;
	private Long idTarifa = null;
	private Long idDensidade = null;
	private Long idProdutoEspecifico = null;
	private Long idNaturezaProduto = null;
	private BigDecimal psRealInformado = null;
	private BigDecimal psCubadoInformado = null;

	private Integer qtVolumes = null;
	private Boolean blColetaEmergencia = null;
	private Boolean blEntregaEmergencia = null;
	
	private BigDecimal metragemCubicaCotacao = null;
	private BigDecimal totalDimensao = null;
	private BigDecimal nrCubagemCalculo = null;
	
	private DoctoServicoDadosCliente dadosCliente;
	//parametros de saida
	private Boolean blDificuldadeEntrega = null;
	private Boolean blUFDestinoCobraTas = null;
	private Boolean blUFDestinoCobraSuframa = null;
	private Boolean blParametroFretePercentual = null;
	private ImpostoServico tributo;
	private Long idTipoTributacaoICMS = null;
	private BigDecimal vlRetensaoSituacaoTributaria = null;
	private BigDecimal pcRetensaoSituacaoTributaria = null;
	private Boolean blIncideIcmsPedagio = null;
	private Boolean blSubcontratacao = null;
	private DateTime dhEmissaoDocRecalculo;
	private Boolean blGeraReceita;
	private BigDecimal vlComplementoIcms;

	private AjusteTarifa ajusteTarifa;
	private TarifaPreco tarifaPreco;
	
	private GrupoRegiao grupoRegiaoOrigem;
	private GrupoRegiao grupoRegiaoDestino;
	
	/**
	 * Método criado para melhorar a rastreabilidade, pois era feito instanceOf CalculoNFT
	 * para saber se o objeto representava um cálculo de nota de transporte ou não 
	 */
	public boolean isCalculoNotaTransporte() {
		return false;
	}
	
	@Override
	public Conhecimento getDoctoServico() {
		return this.doctoServico;
	}

	@Override
	public void setDoctoServico(Conhecimento doctoServico) {
		this.doctoServico = doctoServico;
	}

	@Override
	public String getTpDocumentoServico() {
		return this.doctoServico.getTpDocumentoServico().getValue();
	}

	public Boolean getBlColetaEmergencia() {
		return blColetaEmergencia;
	}

	public void setBlColetaEmergencia(Boolean blColetaEmergencia) {
		this.blColetaEmergencia = blColetaEmergencia;
	}

	public Boolean getBlCalculoFreteTabelaCheia() {
		if(blCalculoFreteTabelaCheia == null){
			blCalculoFreteTabelaCheia = Boolean.FALSE;
		}
		return blCalculoFreteTabelaCheia;
	}

	public void setBlCalculoFreteTabelaCheia(Boolean blCalculoFreteTabelaCheia) {
		this.blCalculoFreteTabelaCheia = blCalculoFreteTabelaCheia;
	}
	
	public Boolean getBlEntregaEmergencia() {
		return blEntregaEmergencia;
	}

	public void setBlEntregaEmergencia(Boolean blEntregaEmergencia) {
		this.blEntregaEmergencia = blEntregaEmergencia;
	}

	public String getNrCepColeta() {
		return nrCepColeta;
	}

	public void setNrCepColeta(String nrCepColeta) {
		this.nrCepColeta = nrCepColeta;
	}

	public String getNrCepEntrega() {
		return nrCepEntrega;
	}

	public void setNrCepEntrega(String nrCepEntrega) {
		this.nrCepEntrega = nrCepEntrega;
	}

	public Long getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Long idTarifa) {
		this.idTarifa = idTarifa;
	}

	public Long getIdDensidade() {
		return idDensidade;
	}

	public void setIdDensidade(Long idDensidade) {
		this.idDensidade = idDensidade;
	}

	public DoctoServicoDadosCliente getDadosCliente() {
		if (dadosCliente == null) {
			dadosCliente = new DoctoServicoDadosCliente();
		}
		return dadosCliente;
	}

	public void setDadosCliente(DoctoServicoDadosCliente dadosCliente) {
		this.dadosCliente = dadosCliente;
	}

	public Boolean getBlDificuldadeEntrega() {
		return blDificuldadeEntrega;
	}

	public void setBlDificuldadeEntrega(Boolean blDificuldadeEntrega) {
		this.blDificuldadeEntrega = blDificuldadeEntrega;
	}

	public Boolean getBlUFDestinoCobraTas() {
		return blUFDestinoCobraTas;
	}

	public void setBlUFDestinoCobraTas(Boolean blUFDestinoCobraTas) {
		this.blUFDestinoCobraTas = blUFDestinoCobraTas;
	}

	public BigDecimal getPsCubadoInformado() {
		return psCubadoInformado;
	}

	public void setPsCubadoInformado(BigDecimal psCubadoInformado) {
		this.psCubadoInformado = psCubadoInformado;
	}

	public BigDecimal getPsRealInformado() {
		return psRealInformado;
	}

	public void setPsRealInformado(BigDecimal psRealInformado) {
		this.psRealInformado = psRealInformado;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public Long getIdProdutoEspecifico() {
		return idProdutoEspecifico;
	}

	public void setIdProdutoEspecifico(Long idProdutoEspecifico) {
		this.idProdutoEspecifico = idProdutoEspecifico;
	}

	public Boolean getBlParametroFretePercentual() {
		return blParametroFretePercentual;
	}

	public void setBlParametroFretePercentual(Boolean blParametroFretePercentual) {
		this.blParametroFretePercentual = blParametroFretePercentual;
	}

	public ImpostoServico getTributo() {
		if(this.tributo == null) {
			this.tributo = new ImpostoServico();
		}
		return this.tributo;
	}

	public void setTributo(ImpostoServico tributo) {
		this.tributo = tributo;
	}

	public Long getIdTipoTributacaoICMS() {
		return idTipoTributacaoICMS;
	}

	public void setIdTipoTributacaoICMS(Long idTipoTributacaoICMS) {
		this.idTipoTributacaoICMS = idTipoTributacaoICMS;
	}

	public BigDecimal getPcRetensaoSituacaoTributaria() {
		return pcRetensaoSituacaoTributaria;
	}

	public void setPcRetensaoSituacaoTributaria(
			BigDecimal pcRetensaoSituacaoTributaria) {
		this.pcRetensaoSituacaoTributaria = pcRetensaoSituacaoTributaria;
	}

	public BigDecimal getVlRetensaoSituacaoTributaria() {
		return vlRetensaoSituacaoTributaria;
	}

	public void setVlRetensaoSituacaoTributaria(
			BigDecimal vlRetensaoSituacaoTributaria) {
		this.vlRetensaoSituacaoTributaria = vlRetensaoSituacaoTributaria;
	}

	public Boolean getBlIncideIcmsPedagio() {
		return blIncideIcmsPedagio;
	}
	
	public void setBlIncideIcmsPedagio(Boolean blIncideIcmsPedagio) {
		this.blIncideIcmsPedagio = blIncideIcmsPedagio;
	}
	
	public boolean isBlSubcontratacao() {
		return blSubcontratacao;
	}
	
	public void setBlSubcontratacao(boolean blSubcontratacao) {
		this.blSubcontratacao = blSubcontratacao;
	}

	public Long getIdNaturezaProduto() {
		if(getDoctoServico().getNaturezaProduto() != null) {
			this.idNaturezaProduto = getDoctoServico().getNaturezaProduto()
					.getIdNaturezaProduto();
}
		return this.idNaturezaProduto;
	}

	public AjusteTarifa getAjusteTarifa() {
		return ajusteTarifa;
	}

	public void setAjusteTarifa(AjusteTarifa ajusteTarifa) {
		this.ajusteTarifa = ajusteTarifa;
	}

	public TarifaPreco getTarifaPreco() {
		return tarifaPreco;
	}

	public void setTarifaPreco(TarifaPreco tarifaPreco) {
		this.tarifaPreco = tarifaPreco;
	}

	public DateTime getDhEmissaoDocRecalculo() {
		return dhEmissaoDocRecalculo;
	}

	public void setDhEmissaoDocRecalculo(DateTime dhEmissaoDocRecalculo) {
		this.dhEmissaoDocRecalculo = dhEmissaoDocRecalculo;
	}

	public Boolean gerarParcelaFretePesoCtrcCompleto() {
		Boolean result = Boolean.TRUE;
		if("CI".equals(getTpConhecimento()) 
				&& blGeraReceita != null && Boolean.FALSE.equals(blGeraReceita)) {
			result = Boolean.FALSE;
		}
		return result;
	}
	
	public Boolean isBlGeraReceita() {
		return blGeraReceita;
	}
	
	public void setBlGeraReceita(Boolean blGeraReceita) {
		this.blGeraReceita = blGeraReceita;
	}
	
	public GrupoRegiao getGrupoRegiaoDestino() {
		return grupoRegiaoDestino;
	}

	public void setGrupoRegiaoDestino(GrupoRegiao grupoRegiaoDestino) {
		this.grupoRegiaoDestino = grupoRegiaoDestino;
	}

	public GrupoRegiao getGrupoRegiaoOrigem() {
		return grupoRegiaoOrigem;
	}

	public void setGrupoRegiaoOrigem(GrupoRegiao grupoRegiaoOrigem) {
		this.grupoRegiaoOrigem = grupoRegiaoOrigem;
	}
	
	public BigDecimal getVlComplementoIcms() {
		return vlComplementoIcms;
	}
	
	public void setVlComplementoIcms(BigDecimal vlComlementoIcms) {
		this.vlComplementoIcms = vlComlementoIcms;
	}
	public BigDecimal getMetragemCubicaCotacao() {
		return metragemCubicaCotacao;
	}

	public void setMetragemCubicaCotacao(BigDecimal metragemCubicaCotacao) {
		this.metragemCubicaCotacao = metragemCubicaCotacao;
	}

	public BigDecimal getTotalDimensao() {
		return totalDimensao;
	}

	public void setTotalDimensao(BigDecimal totalDimensao) {
		this.totalDimensao = totalDimensao;
	}

	public BigDecimal getNrCubagemCalculo() {
		return nrCubagemCalculo;
	}

	public void setNrCubagemCalculo(BigDecimal nrCubagemCalculo) {
		this.nrCubagemCalculo = nrCubagemCalculo;
	}

	public Boolean getBlUFDestinoCobraSuframa() {
		return blUFDestinoCobraSuframa;
	}

	public void setBlUFDestinoCobraSuframa(Boolean blUFDestinoCobraSuframa) {
		this.blUFDestinoCobraSuframa = blUFDestinoCobraSuframa;
	}	
	
}