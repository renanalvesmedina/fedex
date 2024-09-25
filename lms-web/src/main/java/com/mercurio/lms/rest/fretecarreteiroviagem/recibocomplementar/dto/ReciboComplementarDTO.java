package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.configuracoes.dto.MoedaPaisDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.workflow.PendenciaDTO;

public class ReciboComplementarDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idReciboFreteCarreteiro;
	private Long nrReciboFreteCarreteiro;

	private DomainValue tpReciboFreteCarreteiro;
	private DomainValue tpSituacaoRecibo;
	private DomainValue tpSituacaoWorkflow;

	private Boolean blAdiantamento;

	private BigDecimal vlBruto;
	private BigDecimal pcAliquotaIssqn;
	private BigDecimal vlIssqn;
	private BigDecimal pcAliquotaInss;
	private BigDecimal vlSalarioContribuicao;
	private BigDecimal vlInss;
	private BigDecimal vlOutrasFontes;
	private BigDecimal pcAliquotaIrrf;
	private BigDecimal vlIrrf;
	private BigDecimal vlPremio;
	private BigDecimal vlPostoPassagem;
	private BigDecimal vlDiaria;
	private BigDecimal pcAdiantamentoFrete;
	private BigDecimal vlLiquido;
	private BigDecimal vlDesconto;

	private DateTime dhGeracaoMovimento;
	private DateTime dhEmissao;

	private YearMonthDay dtSugeridaPagto;
	private YearMonthDay dtProgramadaPagto;
	private YearMonthDay dtPagtoReal;
	private YearMonthDay dtContabilizacao;
	private YearMonthDay dtCalculoInss;

	private String nrNfCarreteiro;
	private String obReciboFreteCarreteiro;

	private UsuarioDTO usuario;
	private ReciboComplementarDTO reciboComplementado;
	private ControleCargaSuggestDTO controleCarga;
	private MeioTransporteSuggestDTO meioTransporteRodoviario;
	private MotoristaSuggestDTO motorista;
	private ProprietarioDTO proprietario;
	private FilialSuggestDTO filial;
	private FilialSuggestDTO filialDestino;
	private PendenciaDTO pendencia;
	private MoedaPaisDTO moedaPais;
	
	/**
	 * Campos auxiliares de tela.
	 */
	private List<Map<String, Object>> files;
	private List<String> tpSituacaoMatriz;
	
	private Boolean confirmed;
	private Boolean workflow;
	
	private Boolean btEmitir;
	private Boolean btOcorrencias;
	private Boolean btControleCarga;
	private Boolean btCancelar;
	private Boolean btSalvar;
	private Boolean btLimpar;
	private Boolean txtNrNfCarreteiro;
	private Boolean txtObservacao;
	private Boolean txtMoeda;
	private Boolean btAnexar;
	private Boolean dtProgramadaPagtoC;
	private Boolean btSituacao;
	
	private Long idControleCarga;
	private String nrControleCarga;
	private Long idFilialControleCarga;
	private String sgFilialControleCarga;
	private String nmFilialControleCarga;
	
	/**
	 * Attributo necessário para manter compatibilidade com a suggest de recibo.
	 */
	private String sgFilial;

	private DateTime dhEnvioJde;
	
	public List<Map<String, Object>> getFiles() {
		return files;
	}

	public void setFiles(List<Map<String, Object>> files) {
		this.files = files;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Boolean getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Boolean workflow) {
		this.workflow = workflow;
	}

	public Boolean getBtEmitir() {
		return btEmitir;
	}

	public void setBtEmitir(Boolean btEmitir) {
		this.btEmitir = btEmitir;
	}

	public Boolean getBtOcorrencias() {
		return btOcorrencias;
	}

	public void setBtOcorrencias(Boolean btOcorrencias) {
		this.btOcorrencias = btOcorrencias;
	}

	public Boolean getBtControleCarga() {
		return btControleCarga;
	}

	public void setBtControleCarga(Boolean btControleCarga) {
		this.btControleCarga = btControleCarga;
	}

	public Boolean getBtCancelar() {
		return btCancelar;
	}

	public void setBtCancelar(Boolean btCancelar) {
		this.btCancelar = btCancelar;
	}

	public Boolean getBtSalvar() {
		return btSalvar;
	}

	public void setBtSalvar(Boolean btSalvar) {
		this.btSalvar = btSalvar;
	}

	public Boolean getBtLimpar() {
		return btLimpar;
	}

	public void setBtLimpar(Boolean btLimpar) {
		this.btLimpar = btLimpar;
	}

	public Boolean getTxtNrNfCarreteiro() {
		return txtNrNfCarreteiro;
	}

	public void setTxtNrNfCarreteiro(Boolean txtNrNfCarreteiro) {
		this.txtNrNfCarreteiro = txtNrNfCarreteiro;
	}

	public Boolean getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(Boolean txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public Boolean getTxtMoeda() {
		return txtMoeda;
	}

	public void setTxtMoeda(Boolean txtMoeda) {
		this.txtMoeda = txtMoeda;
	}

	public Boolean getBtAnexar() {
		return btAnexar;
	}

	public void setBtAnexar(Boolean btAnexar) {
		this.btAnexar = btAnexar;
	}

	public Boolean getDtProgramadaPagtoC() {
		return dtProgramadaPagtoC;
	}

	public void setDtProgramadaPagtoC(Boolean dtProgramadaPagtoC) {
		this.dtProgramadaPagtoC = dtProgramadaPagtoC;
	}

	public Boolean getBtSituacao() {
		return btSituacao;
	}

	public void setBtSituacao(Boolean btSituacao) {
		this.btSituacao = btSituacao;
	}

	public List<String> getTpSituacaoMatriz() {
		return tpSituacaoMatriz;
	}

	public void setTpSituacaoMatriz(List<String> tpSituacaoMatriz) {
		this.tpSituacaoMatriz = tpSituacaoMatriz;
	}
	
	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public String getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(String nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public Long getIdFilialControleCarga() {
		return idFilialControleCarga;
	}

	public void setIdFilialControleCarga(Long idFilialControleCarga) {
		this.idFilialControleCarga = idFilialControleCarga;
	}

	public String getSgFilialControleCarga() {
		return sgFilialControleCarga;
	}

	public void setSgFilialControleCarga(String sgFilialControleCarga) {
		this.sgFilialControleCarga = sgFilialControleCarga;
	}

	public String getNmFilialControleCarga() {
		return nmFilialControleCarga;
	}

	public void setNmFilialControleCarga(String nmFilialControleCarga) {
		this.nmFilialControleCarga = nmFilialControleCarga;
	}
	


	public Long getIdReciboFreteCarreteiro() {
		return idReciboFreteCarreteiro;
	}

	public void setIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		this.idReciboFreteCarreteiro = idReciboFreteCarreteiro;
	}

	public Long getNrReciboFreteCarreteiro() {
		return nrReciboFreteCarreteiro;
	}

	public void setNrReciboFreteCarreteiro(Long nrReciboFreteCarreteiro) {
		this.nrReciboFreteCarreteiro = nrReciboFreteCarreteiro;
	}

	public DomainValue getTpReciboFreteCarreteiro() {
		return tpReciboFreteCarreteiro;
	}

	public void setTpReciboFreteCarreteiro(DomainValue tpReciboFreteCarreteiro) {
		this.tpReciboFreteCarreteiro = tpReciboFreteCarreteiro;
	}

	public DomainValue getTpSituacaoRecibo() {
		return tpSituacaoRecibo;
	}

	public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}

	public DomainValue getTpSituacaoWorkflow() {
		return tpSituacaoWorkflow;
	}

	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}

	public Boolean getBlAdiantamento() {
		return blAdiantamento;
	}

	public void setBlAdiantamento(Boolean blAdiantamento) {
		this.blAdiantamento = blAdiantamento;
	}

	public BigDecimal getVlBruto() {
		return vlBruto;
	}

	public void setVlBruto(BigDecimal vlBruto) {
		this.vlBruto = vlBruto;
	}

	public BigDecimal getPcAliquotaIssqn() {
		return pcAliquotaIssqn;
	}

	public void setPcAliquotaIssqn(BigDecimal pcAliquotaIssqn) {
		this.pcAliquotaIssqn = pcAliquotaIssqn;
	}

	public BigDecimal getVlIssqn() {
		return vlIssqn;
	}

	public void setVlIssqn(BigDecimal vlIssqn) {
		this.vlIssqn = vlIssqn;
	}

	public BigDecimal getPcAliquotaInss() {
		return pcAliquotaInss;
	}

	public void setPcAliquotaInss(BigDecimal pcAliquotaInss) {
		this.pcAliquotaInss = pcAliquotaInss;
	}

	public BigDecimal getVlSalarioContribuicao() {
		return vlSalarioContribuicao;
	}

	public void setVlSalarioContribuicao(BigDecimal vlSalarioContribuicao) {
		this.vlSalarioContribuicao = vlSalarioContribuicao;
	}

	public BigDecimal getVlInss() {
		return vlInss;
	}

	public void setVlInss(BigDecimal vlInss) {
		this.vlInss = vlInss;
	}

	public BigDecimal getVlOutrasFontes() {
		return vlOutrasFontes;
	}

	public void setVlOutrasFontes(BigDecimal vlOutrasFontes) {
		this.vlOutrasFontes = vlOutrasFontes;
	}

	public BigDecimal getPcAliquotaIrrf() {
		return pcAliquotaIrrf;
	}

	public void setPcAliquotaIrrf(BigDecimal pcAliquotaIrrf) {
		this.pcAliquotaIrrf = pcAliquotaIrrf;
	}

	public BigDecimal getVlIrrf() {
		return vlIrrf;
	}

	public void setVlIrrf(BigDecimal vlIrrf) {
		this.vlIrrf = vlIrrf;
	}

	public BigDecimal getVlPremio() {
		return vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public BigDecimal getVlPostoPassagem() {
		return vlPostoPassagem;
	}

	public void setVlPostoPassagem(BigDecimal vlPostoPassagem) {
		this.vlPostoPassagem = vlPostoPassagem;
	}

	public BigDecimal getVlDiaria() {
		return vlDiaria;
	}

	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}

	public BigDecimal getPcAdiantamentoFrete() {
		return pcAdiantamentoFrete;
	}

	public void setPcAdiantamentoFrete(BigDecimal pcAdiantamentoFrete) {
		this.pcAdiantamentoFrete = pcAdiantamentoFrete;
	}

	public BigDecimal getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(BigDecimal vlLiquido) {
		this.vlLiquido = vlLiquido;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public DateTime getDhGeracaoMovimento() {
		return dhGeracaoMovimento;
	}

	public void setDhGeracaoMovimento(DateTime dhGeracaoMovimento) {
		this.dhGeracaoMovimento = dhGeracaoMovimento;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public YearMonthDay getDtSugeridaPagto() {
		return dtSugeridaPagto;
	}

	public void setDtSugeridaPagto(YearMonthDay dtSugeridaPagto) {
		this.dtSugeridaPagto = dtSugeridaPagto;
	}

	public YearMonthDay getDtProgramadaPagto() {
		return dtProgramadaPagto;
	}

	public void setDtProgramadaPagto(YearMonthDay dtProgramadaPagto) {
		this.dtProgramadaPagto = dtProgramadaPagto;
	}

	public YearMonthDay getDtPagtoReal() {
		return dtPagtoReal;
	}

	public void setDtPagtoReal(YearMonthDay dtPagtoReal) {
		this.dtPagtoReal = dtPagtoReal;
	}

	public YearMonthDay getDtContabilizacao() {
		return dtContabilizacao;
	}

	public void setDtContabilizacao(YearMonthDay dtContabilizacao) {
		this.dtContabilizacao = dtContabilizacao;
	}

	public YearMonthDay getDtCalculoInss() {
		return dtCalculoInss;
	}

	public void setDtCalculoInss(YearMonthDay dtCalculoInss) {
		this.dtCalculoInss = dtCalculoInss;
	}

	public String getNrNfCarreteiro() {
		return nrNfCarreteiro;
	}

	public void setNrNfCarreteiro(String nrNfCarreteiro) {
		this.nrNfCarreteiro = nrNfCarreteiro;
	}

	public String getObReciboFreteCarreteiro() {
		return obReciboFreteCarreteiro;
	}

	public void setObReciboFreteCarreteiro(String obReciboFreteCarreteiro) {
		this.obReciboFreteCarreteiro = obReciboFreteCarreteiro;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public ReciboComplementarDTO getReciboComplementado() {
		return reciboComplementado;
	}

	public void setReciboComplementado(
			ReciboComplementarDTO reciboComplementado) {
		this.reciboComplementado = reciboComplementado;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public MeioTransporteSuggestDTO getMeioTransporteRodoviario() {
		return meioTransporteRodoviario;
	}

	public void setMeioTransporteRodoviario(
			MeioTransporteSuggestDTO meioTransporteRodoviario) {
		this.meioTransporteRodoviario = meioTransporteRodoviario;
	}

	public MotoristaSuggestDTO getMotorista() {
		return motorista;
	}

	public void setMotorista(MotoristaSuggestDTO motorista) {
		this.motorista = motorista;
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

	public FilialSuggestDTO getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(FilialSuggestDTO filialDestino) {
		this.filialDestino = filialDestino;
	}

	public PendenciaDTO getPendencia() {
		return pendencia;
	}

	public void setPendencia(PendenciaDTO pendencia) {
		this.pendencia = pendencia;
	}

	public MoedaPaisDTO getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPaisDTO moedaPais) {
		this.moedaPais = moedaPais;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public void setDhEnvioJde(DateTime dhEnvioJde) {
		this.dhEnvioJde = dhEnvioJde;
	}

	public DateTime getDhEnvioJde() {
		return dhEnvioJde;
	}
}