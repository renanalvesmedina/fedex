package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboFreteCarreteiro implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idReciboFreteCarreteiro;

	/** persistent field */
	private Long nrReciboFreteCarreteiro;

	/** persistent field */
	private DomainValue tpReciboFreteCarreteiro;

	/** persistent field */
	private DateTime dhEmissao;

	/** persistent field */
	private BigDecimal vlBruto;

	/** persistent field */
	private DomainValue tpSituacaoRecibo;

	/** persistent field */
	private Boolean blAdiantamento;

	/** nullable persistent field */
	private BigDecimal pcAliquotaIssqn;

	/** nullable persistent field */
	private BigDecimal vlIssqn;

	/** nullable persistent field */
	private BigDecimal pcAliquotaInss;

	/** nullable persistent field */
	private BigDecimal vlSalarioContribuicao;

	/** nullable persistent field */
	private BigDecimal vlInss;

	/** nullable persistent field */
	private BigDecimal vlOutrasFontes;

	/** nullable persistent field */
	private BigDecimal pcAliquotaIrrf;

	/** nullable persistent field */
	private BigDecimal vlIrrf;

	/** nullable persistent field */
	private YearMonthDay dtSugeridaPagto;
	
	/** nullable persistent field */
	private YearMonthDay dtProgramadaPagto;

	/** nullable persistent field */
	private YearMonthDay dtPagtoReal;

	/** nullable persistent field */
	private YearMonthDay dtContabilizacao;

	/** nullable persistent field */
	private String nrNfCarreteiro;

	/** nullable persistent field */
	private String obReciboFreteCarreteiro;

	/** nullable persistent field */
	private BigDecimal vlPremio;
	
	/** nullable persistent field */
	private BigDecimal vlPostoPassagem;
	
	/** nullable persistent field */
	private BigDecimal vlDiaria;
	
	/** nullable persistent field */
	private YearMonthDay dtCalculoInss;
	
	/** nullable persistent field */
	private BigDecimal vlLiquido;
	
	/** nullable persistent field */
	private BigDecimal vlDesconto;
	
	/** persistent field */
	private DateTime dhGeracaoMovimento;
	
	
	/** persistent field */
	private DateTime dhEnvioJde;
	
	/** persistent field */
	private BigDecimal pcAdiantamentoFrete;
	
	/** persistent field */
	private ControleCarga controleCarga;

	/** persistent field */
	private ReciboFreteCarreteiro reciboComplementado;

	/** persistent field */
	private RelacaoPagamento relacaoPagamento;

	/** persistent field */
	private MoedaPais moedaPais;

	/** persistent field */
	private Beneficiario beneficiario;

	/** persistent field */
	private Proprietario proprietario;
	
	/** persistent field */
	private Filial filial;

	/** persistent field */
	private ContaBancaria contaBancaria;
	
	/** persistent field */
	private Motorista motorista;

	/** persistent field */
	private MeioTransporteRodoviario meioTransporteRodoviario;
	
	/** nullable persistent field */
	private ManifestoViagemNacional manifestoViagemNacional;

	/** nullable persistent field */
	private Filial filialDestino;

	private PostoConveniado postoConveniado;

	/** persistent field */
	private List recibosComplementares;

	/** persistent field */
	private List ocorrenciaFreteCarreteiros;
	
	/** persistent field */
	private List notaCreditos;
	
	  /** persistent field */
    private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private Pendencia pendencia;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

	public Long getIdReciboFreteCarreteiro() {
		return this.idReciboFreteCarreteiro;
	}

	public void setIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		this.idReciboFreteCarreteiro = idReciboFreteCarreteiro;
	}

	public Long getNrReciboFreteCarreteiro() {
		return this.nrReciboFreteCarreteiro;
	}

	public void setNrReciboFreteCarreteiro(Long nrReciboFreteCarreteiro) {
		this.nrReciboFreteCarreteiro = nrReciboFreteCarreteiro;
	}

	public DomainValue getTpReciboFreteCarreteiro() {
		return this.tpReciboFreteCarreteiro;
	}

	public void setTpReciboFreteCarreteiro(DomainValue tpReciboFreteCarreteiro) {
		this.tpReciboFreteCarreteiro = tpReciboFreteCarreteiro;
	}

	public DateTime getDhEmissao() {
		return this.dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public BigDecimal getVlBruto() {
		return this.vlBruto;
	}

	public void setVlBruto(BigDecimal vlBruto) {
		this.vlBruto = vlBruto;
	}

	public DomainValue getTpSituacaoRecibo() {
		return this.tpSituacaoRecibo;
	}

	public void setTpSituacaoRecibo(DomainValue tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}

	public Boolean getBlAdiantamento() {
		return this.blAdiantamento;
	}

	public void setBlAdiantamento(Boolean blAdiantamento) {
		this.blAdiantamento = blAdiantamento;
	}

	public BigDecimal getPcAliquotaIssqn() {
		return this.pcAliquotaIssqn;
	}

	public void setPcAliquotaIssqn(BigDecimal pcAliquotaIssqn) {
		this.pcAliquotaIssqn = pcAliquotaIssqn;
	}

	public BigDecimal getVlIssqn() {
		return this.vlIssqn;
	}

	public void setVlIssqn(BigDecimal vlIssqn) {
		this.vlIssqn = vlIssqn;
	}

	public BigDecimal getPcAliquotaInss() {
		return this.pcAliquotaInss;
	}

	public void setPcAliquotaInss(BigDecimal pcAliquotaInss) {
		this.pcAliquotaInss = pcAliquotaInss;
	}

	public BigDecimal getVlSalarioContribuicao() {
		return this.vlSalarioContribuicao;
	}

	public void setVlSalarioContribuicao(BigDecimal vlSalarioContribuicao) {
		this.vlSalarioContribuicao = vlSalarioContribuicao;
	}

	public BigDecimal getVlInss() {
		return this.vlInss;
	}

	public void setVlInss(BigDecimal vlInss) {
		this.vlInss = vlInss;
	}

	public BigDecimal getVlOutrasFontes() {
		return this.vlOutrasFontes;
	}

	public void setVlOutrasFontes(BigDecimal vlOutrasFontes) {
		this.vlOutrasFontes = vlOutrasFontes;
	}

	public BigDecimal getPcAliquotaIrrf() {
		return this.pcAliquotaIrrf;
	}

	public void setPcAliquotaIrrf(BigDecimal pcAliquotaIrrf) {
		this.pcAliquotaIrrf = pcAliquotaIrrf;
	}

	public BigDecimal getVlIrrf() {
		return this.vlIrrf;
	}

	public void setVlIrrf(BigDecimal vlIrrf) {
		this.vlIrrf = vlIrrf;
	}

	public YearMonthDay getDtSugeridaPagto() {
		return this.dtSugeridaPagto;
	}

	public void setDtSugeridaPagto(YearMonthDay dtSugeridaPagto) {
		this.dtSugeridaPagto = dtSugeridaPagto;
	}

	public YearMonthDay getDtPagtoReal() {
		return this.dtPagtoReal;
	}

	public void setDtPagtoReal(YearMonthDay dtPagtoReal) {
		this.dtPagtoReal = dtPagtoReal;
	}

	public YearMonthDay getDtContabilizacao() {
		return this.dtContabilizacao;
	}

	public void setDtContabilizacao(YearMonthDay dtContabilizacao) {
		this.dtContabilizacao = dtContabilizacao;
	}

	public String getNrNfCarreteiro() {
		return this.nrNfCarreteiro;
	}

	public void setNrNfCarreteiro(String nrNfCarreteiro) {
		this.nrNfCarreteiro = nrNfCarreteiro;
	}

	public String getObReciboFreteCarreteiro() {
		return this.obReciboFreteCarreteiro;
	}

	public void setObReciboFreteCarreteiro(String obReciboFreteCarreteiro) {
		this.obReciboFreteCarreteiro = obReciboFreteCarreteiro;
	}

	public ControleCarga getControleCarga() {
		return this.controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public ReciboFreteCarreteiro getReciboComplementado() {
		return this.reciboComplementado;
	}

	public void setReciboComplementado(ReciboFreteCarreteiro reciboComplementado) {
		this.reciboComplementado = reciboComplementado;
	}

	public RelacaoPagamento getRelacaoPagamento() {
		return this.relacaoPagamento;
	}

	public void setRelacaoPagamento(RelacaoPagamento relacaoPagamento) {
		this.relacaoPagamento = relacaoPagamento;
	}

	public MoedaPais getMoedaPais() {
		return this.moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	public Beneficiario getBeneficiario() {
		return this.beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public BigDecimal getVlDiaria() {
		return vlDiaria;
	}

	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}

	public BigDecimal getVlPostoPassagem() {
		return vlPostoPassagem;
	}

	public void setVlPostoPassagem(BigDecimal vlPostoPassagem) {
		this.vlPostoPassagem = vlPostoPassagem;
	}

	public BigDecimal getVlPremio() {
		return vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public YearMonthDay getDtCalculoInss() {
		return dtCalculoInss;
	}

	public void setDtCalculoInss(YearMonthDay dtCalculoInss) {
		this.dtCalculoInss = dtCalculoInss;
	}

	public BigDecimal getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(BigDecimal vlLiquido) {
		this.vlLiquido = vlLiquido;
	}

	public DateTime getDhGeracaoMovimento() {
		return dhGeracaoMovimento;
	}

	public void setDhGeracaoMovimento(DateTime dhGeracaoMovimento) {
		this.dhGeracaoMovimento = dhGeracaoMovimento;
	}

	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public MeioTransporteRodoviario getMeioTransporteRodoviario() {
		return meioTransporteRodoviario;
	}

	public void setMeioTransporteRodoviario(
			MeioTransporteRodoviario meioTransporteRodoviario) {
		this.meioTransporteRodoviario = meioTransporteRodoviario;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public BigDecimal getPcAdiantamentoFrete() {
		return pcAdiantamentoFrete;
	}

	public void setPcAdiantamentoFrete(BigDecimal pcAdiantamentoFrete) {
		this.pcAdiantamentoFrete = pcAdiantamentoFrete;
	}

	public ManifestoViagemNacional getManifestoViagemNacional() {
		return manifestoViagemNacional;
	}

	public void setManifestoViagemNacional(
			ManifestoViagemNacional manifestoViagemNacional) {
		this.manifestoViagemNacional = manifestoViagemNacional;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	@ParametrizedAttribute(type = ReciboFreteCarreteiro.class)	 
	public List getRecibosComplementares() {
		return this.recibosComplementares;
	}

	public void setRecibosComplementares(List recibosComplementares) {
		this.recibosComplementares = recibosComplementares;
	}

	@ParametrizedAttribute(type = OcorrenciaFreteCarreteiro.class)	 
	public List getOcorrenciaFreteCarreteiros() {
		return this.ocorrenciaFreteCarreteiros;
	}

	public void setOcorrenciaFreteCarreteiros(List ocorrenciaFreteCarreteiros) {
		this.ocorrenciaFreteCarreteiros = ocorrenciaFreteCarreteiros;
	}

	@ParametrizedAttribute(type = NotaCredito.class)	 
	public List getNotaCreditos() {
		return this.notaCreditos;
	}

	public void setNotaCreditos(List notaCreditos) {
		this.notaCreditos = notaCreditos;
	}
	
	public DomainValue getTpSituacaoWorkflow() {
		return tpSituacaoWorkflow;
	}

	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtProgramadaPagto() {
		return dtProgramadaPagto;
	}

	public void setDtProgramadaPagto(YearMonthDay dtProgramadaPagto) {
		this.dtProgramadaPagto = dtProgramadaPagto;
	}


	public String toString() {
		return new ToStringBuilder(this).append("idReciboFreteCarreteiro",
				getIdReciboFreteCarreteiro()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboFreteCarreteiro))
			return false;
		ReciboFreteCarreteiro castOther = (ReciboFreteCarreteiro) other;
		return new EqualsBuilder().append(this.getIdReciboFreteCarreteiro(),
				castOther.getIdReciboFreteCarreteiro()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboFreteCarreteiro())
			.toHashCode();
	}

	public DateTime getDhEnvioJde() {
		return dhEnvioJde;
	}

	public void setDhEnvioJde(DateTime dhEnvioJde) {
		this.dhEnvioJde = dhEnvioJde;
	}

}