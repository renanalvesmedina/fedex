package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Cotacao implements Serializable {
	private static final long serialVersionUID = 2L;
	private transient Boolean blFrete;
	private transient Boolean blServicosAdicionais;
	private transient DoctoServicoDadosCliente dadosCliente;

	/** identifier field */
	private Long idCotacao;

	/** persistent field */
	private Integer nrCotacao;

	/** persistent field */
	private DomainValue tpDocumentoCotacao;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private DomainValue tpFrete;

	/** persistent field */
	private BigDecimal psReal;

	/** persistent field */
	private Boolean blIndicadorSeguro;

	/** persistent field */
	private BigDecimal vlTotalCotacao;

	/** transient field */
	private BigDecimal vlLiquido;

	/** persistent field */
	private BigDecimal vlTotalParcelas;
	
	/** persistent field */
	private BigDecimal vlTotalServicos;

	/** persistent field */
	private BigDecimal vlDesconto;

	/** persistent field */
	private BigDecimal vlImposto;

	/** nullable persistent field */
	private BigDecimal vlIcmsSubstituicaoTributaria;

	/** persistent field */
	private Boolean blIncideIcmsPedagio;

	/** persistent field */
	private YearMonthDay dtValidade;

	/** persistent field */
	private YearMonthDay dtGeracaoCotacao;
	
	private YearMonthDay dtAprovacao;
	
	private YearMonthDay dtAprovacaoDimensoes;

	/** nullable persistent field */
	private Integer nrNotaFiscal;

	/** nullable persistent field */
	private BigDecimal psCubado;

	/** nullable persistent field */
	private BigDecimal vlMercadoria;
	
	/** nullable persistent field */
	private Long nrTelefone;

	/** nullable persistent field */
	private Long nrFax;

	/** nullable persistent field */
	private String nrIdentifClienteRem;

	/** nullable persistent field */
	private String nrIdentifClienteDest;

	/** nullable persistent field */
	private String nrIdentifResponsFrete;

	/** nullable persistent field */
	private Short nrPpe;

	/** nullable persistent field */
	private DomainValue tpDevedorFrete;

	/** nullable persistent field */
	private DomainValue tpCalculo;

	/** nullable persistent field */
	private String dsEmail;

	/** nullable persistent field */
	private String dsContato;

	/** nullable persistent field */
	private String dsMotivo;

	/** nullable persistent field */
	private String nmClienteRemetente;

	/** nullable persistent field */
	private String nmClienteDestino;

	/** nullable persistent field */
	private DomainValue tpSitTributariaRemetente;

	/** nullable persistent field */
	private DomainValue tpSitTributariaDestinatario;

	/** nullable persistent field */
	private DomainValue tpSitTributariaResponsavel;

	/** nullable persistent field */
	private String nmResponsavelFrete;

	/** nullable persistent field */
	private String nrDocumentoCotacao;

	/** nullable persistent field */
	private String obCotacao;
	
	/** nullable persistent field */
	private DomainValue tpSituacaoAprovacao;
	
	/** persistent field */
	private Boolean blColetaEmergencia;

	/** persistent field */
	private Boolean blEntregaEmergencia;
	
	/** persistent field */
	private Boolean blMercadoriaExportacao;

	/** persistent field */
	private Cliente clienteByIdCliente;

	/** persistent field */
	private Cliente clienteByIdClienteSolicitou;
	
	/** persistent field */
	private Cliente clienteByIdClienteDestino;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualRemetente;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualDestinatario;

	/** persistent field */
	private InscricaoEstadual inscricaoEstadualResponsavel;

	/** persistent field */
	private Municipio municipioByIdMunicipioEntrega;

	/** persistent field */
	private Municipio municipioByIdMunicipioOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioDestino;

	/** persistent field */
	private Municipio municipioByIdMunicipioResponsavel;

	/** persistent field */
	private Usuario usuarioByIdUsuarioAprovou;
	
	/** persistent field */
	private Usuario usuarioByIdUsuarioAprovouDimensoes;

	/** persistent field */
	private Usuario usuarioByIdUsuarioRealizou;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private Servico servico;

	/** persistent field */
	private TabelaPreco tabelaPreco;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialResponsavel;

	/** persistent field */
	private DivisaoCliente divisaoCliente;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoOrigem;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoDestino;

	/** persistent field */
	private ProdutoEspecifico produtoEspecifico;
	
	private Pendencia pendencia;

	/** persistent field */
	private List servicoAdicionalClientes;

	/** persistent field */
	private List pedidoColetas;

	/** persistent field */
	private List detalheColetas;
	
	/** persistent field */
	private List parametroClientes;

	/** persistent field */
	private List parcelaCotacoes;
	
	/** persistent field */
	private List<Dimensao> dimensoes;
	
	/** persistent field */
	private List<ServAdicionalDocServ> servAdicionalDocServs;

	/** persistent field */
	private List<LiberacaoDocServ> liberacoesDoctoServico;

	private ParametroCliente parametroClienteOriginal;
	
	private Long qtVolumes;
	
	private String tpModoCotacao;
	
	private Pendencia pendenciaDimensoes;
	
	private DomainValue tpSituacaoAprovacaoDimensoes;
	
	
	public Long getIdCotacao() {
		return this.idCotacao;
	}

	public void setIdCotacao(Long idCotacao) {
		this.idCotacao = idCotacao;
	}

	public Boolean getBlFrete() {
		return blFrete;
	}

	public void setBlFrete(Boolean blFrete) {
		this.blFrete = blFrete;
	}

	public Boolean getBlServicosAdicionais() {
		return blServicosAdicionais;
	}

	public void setBlServicosAdicionais(Boolean blServicosAdicionais) {
		this.blServicosAdicionais = blServicosAdicionais;
	}

	public Integer getNrCotacao() {
		return this.nrCotacao;
	}

	public void setNrCotacao(Integer nrCotacao) {
		this.nrCotacao = nrCotacao;
	}

	public DomainValue getTpDocumentoCotacao() {
		return tpDocumentoCotacao;
	}

	public void setTpDocumentoCotacao(DomainValue tpDocumentoCotacao) {
		this.tpDocumentoCotacao = tpDocumentoCotacao;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpFrete() {
		return this.tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public BigDecimal getPsReal() {
		return this.psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public Boolean getBlIndicadorSeguro() {
		return this.blIndicadorSeguro;
	}

	public void setBlIndicadorSeguro(Boolean blIndicadorSeguro) {
		this.blIndicadorSeguro = blIndicadorSeguro;
	}

	public BigDecimal getVlTotalCotacao() {
		return this.vlTotalCotacao;
	}

	public void setVlTotalCotacao(BigDecimal vlTotalCotacao) {
		this.vlTotalCotacao = vlTotalCotacao;
	}

	public BigDecimal getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(BigDecimal vlLiquido) {
		this.vlLiquido = vlLiquido;
	}

	public BigDecimal getVlIcmsSubstituicaoTributaria() {
		return vlIcmsSubstituicaoTributaria;
	}

	public void setVlIcmsSubstituicaoTributaria(
			BigDecimal vlIcmsSubstituicaoTributaria) {
		this.vlIcmsSubstituicaoTributaria = vlIcmsSubstituicaoTributaria;
	}

	public Boolean getBlIncideIcmsPedagio() {
		return blIncideIcmsPedagio;
	}

	public void setBlIncideIcmsPedagio(Boolean blIncideIcmsPedagio) {
		this.blIncideIcmsPedagio = blIncideIcmsPedagio;
	}

	public YearMonthDay getDtGeracaoCotacao() {
		return dtGeracaoCotacao;
	}

	public void setDtGeracaoCotacao(YearMonthDay dtGeracaoCotacao) {
		this.dtGeracaoCotacao = dtGeracaoCotacao;
	}

	public YearMonthDay getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(YearMonthDay dtValidade) {
		this.dtValidade = dtValidade;
	}

	public Integer getNrNotaFiscal() {
		return this.nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public BigDecimal getPsCubado() {
		return this.psCubado;
	}

	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}

	public BigDecimal getVlMercadoria() {
		return this.vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public Long getNrTelefone() {
		return this.nrTelefone;
	}

	public void setNrTelefone(Long nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	public Long getNrFax() {
		return this.nrFax;
	}

	public void setNrFax(Long nrFax) {
		this.nrFax = nrFax;
	}

	public String getNrIdentifClienteRem() {
		return this.nrIdentifClienteRem;
	}

	public void setNrIdentifClienteRem(String nrIdentifClienteRem) {
		this.nrIdentifClienteRem = nrIdentifClienteRem;
	}

	public String getNrIdentifClienteDest() {
		return this.nrIdentifClienteDest;
	}

	public void setNrIdentifClienteDest(String nrIdentifClienteDest) {
		this.nrIdentifClienteDest = nrIdentifClienteDest;
	}

	public String getNrIdentifResponsFrete() {
		return this.nrIdentifResponsFrete;
	}

	public void setNrIdentifResponsFrete(String nrIdentifResponsFrete) {
		this.nrIdentifResponsFrete = nrIdentifResponsFrete;
	}

	public Short getNrPpe() {
		return this.nrPpe;
	}

	public void setNrPpe(Short nrPpe) {
		this.nrPpe = nrPpe;
	}

	public DomainValue getTpDevedorFrete() {
		return this.tpDevedorFrete;
	}

	public void setTpDevedorFrete(DomainValue tpDevedorFrete) {
		this.tpDevedorFrete = tpDevedorFrete;
	}

	public DomainValue getTpCalculo() {
		return this.tpCalculo;
	}

	public void setTpCalculo(DomainValue tpCalculo) {
		this.tpCalculo = tpCalculo;
	}

	public String getDsEmail() {
		return this.dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public String getDsContato() {
		return this.dsContato;
	}

	public void setDsContato(String dsContato) {
		this.dsContato = dsContato;
	}

	public String getDsMotivo() {
		return this.dsMotivo;
	}

	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}

	public String getNmClienteRemetente() {
		return this.nmClienteRemetente;
	}

	public void setNmClienteRemetente(String nmClienteRemetente) {
		this.nmClienteRemetente = nmClienteRemetente;
	}

	public String getNmClienteDestino() {
		return this.nmClienteDestino;
	}

	public void setNmClienteDestino(String nmClienteDestino) {
		this.nmClienteDestino = nmClienteDestino;
	}

	public DomainValue getTpSitTributariaRemetente() {
		return this.tpSitTributariaRemetente;
	}

	public void setTpSitTributariaRemetente(DomainValue tpSitTributariaRemetente) {
		this.tpSitTributariaRemetente = tpSitTributariaRemetente;
	}

	public DomainValue getTpSitTributariaDestinatario() {
		return this.tpSitTributariaDestinatario;
	}

	public void setTpSitTributariaDestinatario(
			DomainValue tpSitTributariaDestinatario) {
		this.tpSitTributariaDestinatario = tpSitTributariaDestinatario;
	}

	public DomainValue getTpSitTributariaResponsavel() {
		return this.tpSitTributariaResponsavel;
	}

	public void setTpSitTributariaResponsavel(
			DomainValue tpSitTributariaResponsavel) {
		this.tpSitTributariaResponsavel = tpSitTributariaResponsavel;
	}

	public String getNmResponsavelFrete() {
		return this.nmResponsavelFrete;
	}

	public void setNmResponsavelFrete(String nmResponsavelFrete) {
		this.nmResponsavelFrete = nmResponsavelFrete;
	}

	public String getNrDocumentoCotacao() {
		return this.nrDocumentoCotacao;
	}

	public void setNrDocumentoCotacao(String nrDocumentoCotacao) {
		this.nrDocumentoCotacao = nrDocumentoCotacao;
	}

	public String getObCotacao() {
		return this.obCotacao;
	}

	public void setObCotacao(String obCotacao) {
		this.obCotacao = obCotacao;
	}

	public Cliente getClienteByIdCliente() {
		return this.clienteByIdCliente;
	}

	public void setClienteByIdCliente(Cliente clienteByIdCliente) {
		this.clienteByIdCliente = clienteByIdCliente;
	}

	public Cliente getClienteByIdClienteSolicitou() {
		return this.clienteByIdClienteSolicitou;
	}

	public void setClienteByIdClienteSolicitou(
			Cliente clienteByIdClienteSolicitou) {
		this.clienteByIdClienteSolicitou = clienteByIdClienteSolicitou;
	}

	public InscricaoEstadual getInscricaoEstadualDestinatario() {
		return inscricaoEstadualDestinatario;
	}

	public void setInscricaoEstadualDestinatario(
			InscricaoEstadual inscricaoEstadualDestinatario) {
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
	}

	public InscricaoEstadual getInscricaoEstadualRemetente() {
		return inscricaoEstadualRemetente;
	}

	public void setInscricaoEstadualRemetente(
			InscricaoEstadual inscricaoEstadualRemetente) {
		this.inscricaoEstadualRemetente = inscricaoEstadualRemetente;
	}

	public InscricaoEstadual getInscricaoEstadualResponsavel() {
		return inscricaoEstadualResponsavel;
	}

	public void setInscricaoEstadualResponsavel(
			InscricaoEstadual inscricaoEstadualResponsavel) {
		this.inscricaoEstadualResponsavel = inscricaoEstadualResponsavel;
	}

	public Cliente getClienteByIdClienteDestino() {
		return clienteByIdClienteDestino;
	}

	public void setClienteByIdClienteDestino(Cliente clienteByIdClienteDestino) {
		this.clienteByIdClienteDestino = clienteByIdClienteDestino;
	}

	public Municipio getMunicipioByIdMunicipioEntrega() {
		return this.municipioByIdMunicipioEntrega;
	}

	public void setMunicipioByIdMunicipioEntrega(
			Municipio municipioByIdMunicipioEntrega) {
		this.municipioByIdMunicipioEntrega = municipioByIdMunicipioEntrega;
	}

	public Municipio getMunicipioByIdMunicipioOrigem() {
		return this.municipioByIdMunicipioOrigem;
	}

	public void setMunicipioByIdMunicipioOrigem(
			Municipio municipioByIdMunicipioOrigem) {
		this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
	}

	public Municipio getMunicipioByIdMunicipioDestino() {
		return this.municipioByIdMunicipioDestino;
	}

	public void setMunicipioByIdMunicipioDestino(
			Municipio municipioByIdMunicipioDestino) {
		this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
	}

	public Municipio getMunicipioByIdMunicipioResponsavel() {
		return municipioByIdMunicipioResponsavel;
	}

	public void setMunicipioByIdMunicipioResponsavel(
			Municipio municipioByIdMunicipioResponsavel) {
		this.municipioByIdMunicipioResponsavel = municipioByIdMunicipioResponsavel;
	}

	public Usuario getUsuarioByIdUsuarioAprovou() {
		return this.usuarioByIdUsuarioAprovou;
	}

	public void setUsuarioByIdUsuarioAprovou(Usuario usuarioByIdUsuarioAprovou) {
		this.usuarioByIdUsuarioAprovou = usuarioByIdUsuarioAprovou;
	}

	public Usuario getUsuarioByIdUsuarioRealizou() {
		return this.usuarioByIdUsuarioRealizou;
	}

	public void setUsuarioByIdUsuarioRealizou(Usuario usuarioByIdUsuarioRealizou) {
		this.usuarioByIdUsuarioRealizou = usuarioByIdUsuarioRealizou;
	}

	public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
		return this.naturezaProduto;
	}

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Servico getServico() {
		return this.servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialResponsavel() {
		return filialByIdFilialResponsavel;
	}

	public void setFilialByIdFilialResponsavel(
			Filial filialByIdFilialResponsavel) {
		this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
	}
	
	public Aeroporto getAeroportoByIdAeroportoDestino() {
		return aeroportoByIdAeroportoDestino;
	}

	public void setAeroportoByIdAeroportoDestino(
			Aeroporto aeroportoByIdAeroportoDestino) {
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
	}

	public Aeroporto getAeroportoByIdAeroportoOrigem() {
		return aeroportoByIdAeroportoOrigem;
	}

	public void setAeroportoByIdAeroportoOrigem(
			Aeroporto aeroportoByIdAeroportoOrigem) {
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
	}

	public Boolean getBlColetaEmergencia() {
		return blColetaEmergencia;
	}

	public void setBlColetaEmergencia(Boolean blColetaEmergencia) {
		this.blColetaEmergencia = blColetaEmergencia;
	}

	public Boolean getBlEntregaEmergencia() {
		return blEntregaEmergencia;
	}

	public void setBlEntregaEmergencia(Boolean blEntregaEmergencia) {
		this.blEntregaEmergencia = blEntregaEmergencia;
	}
	
	public Boolean getBlMercadoriaExportacao() {
		return blMercadoriaExportacao;
	}

	public void setBlMercadoriaExportacao(Boolean blMercadoriaExportacao) {
		this.blMercadoriaExportacao = blMercadoriaExportacao;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public ProdutoEspecifico getProdutoEspecifico() {
		return produtoEspecifico;
	}

	public void setProdutoEspecifico(ProdutoEspecifico produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ServicoAdicionalCliente.class) 
	public List getServicoAdicionalClientes() {
		return this.servicoAdicionalClientes;
	}

	public void setServicoAdicionalClientes(List servicoAdicionalClientes) {
		this.servicoAdicionalClientes = servicoAdicionalClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColeta.class) 
	public List getPedidoColetas() {
		return this.pedidoColetas;
	}

	public void setPedidoColetas(List pedidoColetas) {
		this.pedidoColetas = pedidoColetas;
	}
	
	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class) 
	public List getDetalheColetas() {
		return this.detalheColetas;
	}

	public void setDetalheColetas(List detalheColetas) {
		this.detalheColetas = detalheColetas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class) 
	public List getParametroClientes() {
		return this.parametroClientes;
	}

	public void setParametroClientes(List parametroClientes) {
		this.parametroClientes = parametroClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParcelaCotacao.class) 
	public List getParcelaCotacoes() {
		return this.parcelaCotacoes;
	}

	public void setParcelaCotacoes(List parcelaCotacoes) {
		this.parcelaCotacoes = parcelaCotacoes;
	}
	
	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ServAdicionalDocServ.class) 
	public List<ServAdicionalDocServ> getServAdicionalDocServs() {
		return this.servAdicionalDocServs;
	}

	public void setServAdicionalDocServs(
			List<ServAdicionalDocServ> servAdicionalDocServs) {
		this.servAdicionalDocServs = servAdicionalDocServs;
	}

	public void addServAdicionalDocServ(ServAdicionalDocServ novo){
		if (this.servAdicionalDocServs == null) {
			this.servAdicionalDocServs = new ArrayList<ServAdicionalDocServ>();
		}
		this.servAdicionalDocServs.add(novo);
	}

	public boolean removeServAdicionalDocServ(Long id){
		for (Iterator iter = servAdicionalDocServs.iterator(); iter.hasNext();) {
			ServAdicionalDocServ d = (ServAdicionalDocServ) iter.next();
			if(d.getIdServAdicionalDocServ().equals(id)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	public ServAdicionalDocServ getServAdicionalDocServ(Long id){
		if (this.servAdicionalDocServs != null){
			for (Iterator iter = servAdicionalDocServs.iterator(); iter
					.hasNext();) {
				ServAdicionalDocServ d = (ServAdicionalDocServ) iter.next();
				if (d.getIdServAdicionalDocServ().equals(id))
					return d;
			}
		}
		return null;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Dimensao.class) 
	public List<Dimensao> getDimensoes() {
		return this.dimensoes;
	}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

	public void addDimensao(Dimensao novo){
		if (this.dimensoes == null)
			this.dimensoes = new ArrayList<Dimensao>();
		this.dimensoes.add(novo);
	}

	public boolean removeDimensao(Long idDimensao) {
		for (Iterator iter = dimensoes.iterator(); iter.hasNext();) {
			Dimensao d = (Dimensao) iter.next();
			if(d.getIdDimensao().equals(idDimensao)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	public Dimensao getDimensao(Long idDimensao){
		if (this.dimensoes != null){
			for (Iterator iter = dimensoes.iterator(); iter.hasNext();) {
				Dimensao d = (Dimensao) iter.next();
				if (d.getIdDimensao().equals(idDimensao))
					return d;
			}
		}
		return null;
	}

	public BigDecimal getVlImposto() {
		return vlImposto;
	}

	public void setVlImposto(BigDecimal vlImposto) {
		this.vlImposto = vlImposto;
	}

	public DoctoServicoDadosCliente getDadosCliente() {
		return dadosCliente;
	}

	public void setDadosCliente(DoctoServicoDadosCliente dadosCliente) {
		this.dadosCliente = dadosCliente;
	}

	public BigDecimal getVlTotalParcelas() {
		return vlTotalParcelas;
	}

	public void setVlTotalParcelas(BigDecimal vlTotalParcelas) {
		this.vlTotalParcelas = vlTotalParcelas;
	}

	public BigDecimal getVlTotalServicos() {
		return vlTotalServicos;
	}

	public void setVlTotalServicos(BigDecimal vlTotalServicos) {
		this.vlTotalServicos = vlTotalServicos;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.LiberacaoDocServ.class) 
	public List<LiberacaoDocServ> getLiberacoesDoctoServico() {
		return liberacoesDoctoServico;
	}

	public void setLiberacoesDoctoServico(
			List<LiberacaoDocServ> liberacoesDoctoServico) {
		this.liberacoesDoctoServico = liberacoesDoctoServico;
	}

	public void addLiberacaoEmbarque(LiberacaoDocServ liberacaoDoctoServico){
		if(this.liberacoesDoctoServico == null) {
			this.liberacoesDoctoServico = new ArrayList<LiberacaoDocServ>();
		}
		this.liberacoesDoctoServico.add(liberacaoDoctoServico);
	}

	public String toString() {
		return new ToStringBuilder(this).append("idCotacao", getIdCotacao())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Cotacao))
			return false;
		Cotacao castOther = (Cotacao) other;
		return new EqualsBuilder().append(this.getIdCotacao(),
				castOther.getIdCotacao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdCotacao()).toHashCode();
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public YearMonthDay getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public ParametroCliente getParametroClienteOriginal() {
		return parametroClienteOriginal;
	}

	public void setParametroClienteOriginal(
			ParametroCliente parametroClienteOriginal) {
		this.parametroClienteOriginal = parametroClienteOriginal;
	}

	public Long getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Long qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public String getTpModoCotacao() {
		return tpModoCotacao;
	}

	public void setTpModoCotacao(String tpModoCotacao) {
		this.tpModoCotacao = tpModoCotacao;
	}

	public Pendencia getPendenciaDimensoes() {
		return pendenciaDimensoes;
	}

	public void setPendenciaDimensoes(Pendencia pendenciaDimensoes) {
		this.pendenciaDimensoes = pendenciaDimensoes;
	}

	public Usuario getUsuarioByIdUsuarioAprovouDimensoes() {
		return usuarioByIdUsuarioAprovouDimensoes;
	}

	public void setUsuarioByIdUsuarioAprovouDimensoes(
			Usuario usuarioByIdUsuarioAprovouDimensoes) {
		this.usuarioByIdUsuarioAprovouDimensoes = usuarioByIdUsuarioAprovouDimensoes;
	}

	public DomainValue getTpSituacaoAprovacaoDimensoes() {
		return tpSituacaoAprovacaoDimensoes;
	}

	public void setTpSituacaoAprovacaoDimensoes(
			DomainValue tpSituacaoAprovacaoDimensoes) {
		this.tpSituacaoAprovacaoDimensoes = tpSituacaoAprovacaoDimensoes;
	}

	public YearMonthDay getDtAprovacaoDimensoes() {
		return dtAprovacaoDimensoes;
	}

	public void setDtAprovacaoDimensoes(YearMonthDay dtAprovacaoDimensoes) {
		this.dtAprovacaoDimensoes = dtAprovacaoDimensoes;
	}
	
	

}