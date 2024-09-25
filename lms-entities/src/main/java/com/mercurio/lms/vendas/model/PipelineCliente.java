package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;

public class PipelineCliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private Long idPipelineCliente;
	
	/** persistent field */
	private Filial filial;	
	
	/** persistent field */
	private Moeda moeda;
	
	/** persistent field */
	private Cliente cliente;
	
	/** persistent field */
	private Usuario usuarioByIdUsuario;
	
	/** persistent field */
	private BigDecimal percProbabilidade = BigDecimal.ZERO;
	
	/** persistent field */
	private DomainValue tpSituacao;
	
	/** persistent field */
	private Boolean blClienteNovo;
	
	/** persistent field */	
	private BigDecimal vlReceitaAtual;
	
	/** persistent field */	
	private BigDecimal vlReceitaPrevista;
	
	/** persistent field */
	private DomainValue tpMotivoPerda;
	
	/** persistent field */
	private YearMonthDay dtPerda;
	
	/** persistent field */
	private SegmentoMercado segmentoMercado;
	
	/** persistent field */
	private String nmCliente;
	
	/** persistent field */
	private String nrMesFechamento;
	
	/** persistent field */
	private String nrMesFechamentoAtualizado;

	/** persistent field */
	private String nrAnoFechamento;

	/** persistent field */
	private String nrAnoFechamentoAtualizado;

	/** persistent field */
	private DomainValue tpProbabilidade;

	/** persistent field */
	private DomainValue tpNegociacao;

	/** persistent field */
	private Cliente clienteByIdClienteRespnsavel; 
	
	/** persistent field */
	private List<PipelineReceita> listPipelineReceita;
	
	/** persistent field */
	private List<PipelineEtapa> listPipelineEtapas;

	public Long getIdPipelineCliente() {
		return idPipelineCliente;
	}

	public void setIdPipelineCliente(Long idPipelineCliente) {
		this.idPipelineCliente = idPipelineCliente;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuarioByIdUsuario() {
		return usuarioByIdUsuario;
	}

	public void setUsuarioByIdUsuario(Usuario usuarioByIdUsuario) {
		this.usuarioByIdUsuario = usuarioByIdUsuario;
	}

	public BigDecimal getPercProbabilidade() {
		if (percProbabilidade == null) {
			percProbabilidade = BigDecimal.ZERO;
		}
		return percProbabilidade;
	}

	public void setPercProbabilidade(BigDecimal percProbabilidade) {
		this.percProbabilidade = percProbabilidade;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Boolean getBlClienteNovo() {
		return blClienteNovo;
	}

	public void setBlClienteNovo(Boolean blClienteNovo) {
		this.blClienteNovo = blClienteNovo;
	}

	public BigDecimal getVlReceitaAtual() {
		return vlReceitaAtual;
	}

	public void setVlReceitaAtual(BigDecimal vlReceitaAtual) {
		this.vlReceitaAtual = vlReceitaAtual;
	}

	public BigDecimal getVlReceitaPrevista() {
		return vlReceitaPrevista;
	}

	public void setVlReceitaPrevista(BigDecimal vlReceitaPrevista) {
		this.vlReceitaPrevista = vlReceitaPrevista;
	}

	public DomainValue getTpMotivoPerda() {
		return tpMotivoPerda;
	}

	public void setTpMotivoPerda(DomainValue tpMotivoPerda) {
		this.tpMotivoPerda = tpMotivoPerda;
	}

	public YearMonthDay getDtPerda() {
		return dtPerda;
	}

	public void setDtPerda(YearMonthDay dtPerda) {
		this.dtPerda = dtPerda;
	}

	public SegmentoMercado getSegmentoMercado() {
		return segmentoMercado;
	}

	public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
		this.segmentoMercado = segmentoMercado;
	}

	public List<PipelineReceita> getListPipelineReceita() {
		return listPipelineReceita;
	}

	public void setListPipelineReceita(List<PipelineReceita> listPipelineReceita) {
		this.listPipelineReceita = listPipelineReceita;
	}

	public List<PipelineEtapa> getListPipelineEtapas() {
		return listPipelineEtapas;
	}

	public void setListPipelineEtapas(List<PipelineEtapa> listPipelineEtapas) {
		this.listPipelineEtapas = listPipelineEtapas;
	}

	public String getNmCliente() {
		return nmCliente;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public String getNrMesFechamento() {
		return nrMesFechamento;
	}

	public void setNrMesFechamento(String nrMesFechamento) {
		this.nrMesFechamento = nrMesFechamento;
	}

	public String getNrMesFechamentoAtualizado() {
		return nrMesFechamentoAtualizado;
	}

	public void setNrMesFechamentoAtualizado(String nrMesFechamentoAtualizado) {
		this.nrMesFechamentoAtualizado = nrMesFechamentoAtualizado;
	}

	public String getNrAnoFechamento() {
		return nrAnoFechamento;
	}

	public void setNrAnoFechamento(String nrAnoFechamento) {
		this.nrAnoFechamento = nrAnoFechamento;
	}

	public String getNrAnoFechamentoAtualizado() {
		return nrAnoFechamentoAtualizado;
	}

	public void setNrAnoFechamentoAtualizado(String nrAnoFechamentoAtualizado) {
		this.nrAnoFechamentoAtualizado = nrAnoFechamentoAtualizado;
	}

	public DomainValue getTpProbabilidade() {
		return tpProbabilidade;
	}

	public void setTpProbabilidade(DomainValue tpProbabilidade) {
		this.tpProbabilidade = tpProbabilidade;
	}

	public DomainValue getTpNegociacao() {
		return tpNegociacao;
	}

	public void setTpNegociacao(DomainValue tpNegociacao) {
		this.tpNegociacao = tpNegociacao;
	}

	public Cliente getClienteByIdClienteRespnsavel() {
		return clienteByIdClienteRespnsavel;
	}

	public void setClienteByIdClienteRespnsavel(Cliente clienteByIdClienteRespnsavel) {
		this.clienteByIdClienteRespnsavel = clienteByIdClienteRespnsavel;
	}

	public boolean hasNrMesAnoFechamentoFilled() {
		return StringUtils.isNotBlank(nrMesFechamento) && StringUtils.isNotBlank(nrAnoFechamento);
	}

	public boolean hasNrMesAnoFechamentoAtualizadoFilled() {
		return StringUtils.isNotBlank(nrMesFechamentoAtualizado) && StringUtils.isNotBlank(nrAnoFechamentoAtualizado);
	}
	
}
