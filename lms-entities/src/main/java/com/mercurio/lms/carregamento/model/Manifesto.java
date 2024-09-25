package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.expedicao.model.ManifestoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.sgr.model.SmpManifesto;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class Manifesto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idManifesto;

	/** persistent field */
	private Long qntManif;

	/** persistent field */
	private Long idControleCarga;

	/** persistent field */
	private Long nrPreManifesto;

	/** persistent field */
	private DateTime dhGeracaoPreManifesto;

	/** persistent field */
	private DomainValue tpManifesto;

	/** persistent field */
	private BigDecimal psTotalManifesto;

	/** persistent field */
	private BigDecimal vlTotalManifesto;

	/** nullable persistent field */
	private BigDecimal psTotalAforadoManifesto;

	/** nullable persistent field */
	private DateTime dhEmissaoManifesto;

	/** nullable persistent field */
	private DomainValue tpManifestoViagem;

	/** nullable persistent field */
	private DomainValue tpModal;

	/** nullable persistent field */
	private DomainValue tpManifestoEntrega;

	/** nullable persistent field */
	private DomainValue tpAbrangencia;

	/** persistent field */
	private DomainValue tpStatusManifesto;

	/** nullable persistent field */
	private BigDecimal vlRateioPedagio;

	/** nullable persistent field */
	private BigDecimal vlRateioFreteCarreteiro;

	/** nullable persistent field */
	private String obManifesto;

	/** persistent field */
	private Boolean blBloqueado;

	/** nullable persistent field */	
	private BigDecimal vlTotalManifestoEmissao;

	/** nullable persistent field */
	private BigDecimal psTotalManifestoEmissao;

	/** nullable persistent field */
	private Integer qtTotalVolumesEmissao;

	/** nullable persistent field */
	private BigDecimal vlTotalFreteCifEmissao;

	/** nullable persistent field */
	private BigDecimal vlTotalFreteFobEmissao;

	/** nullable persistent field */
	private BigDecimal vlTotalFreteEmissao;

	/** nullable persistent field */
	private Integer nrOrdem;

	/** nullable persistent field */
	private ManifestoEntrega manifestoEntrega;

	/** nullable persistent field */
	private ManifestoInternacional manifestoInternacional;

	/** nullable persistent field */
	private ManifestoViagemNacional manifestoViagemNacional;

	/** persistent field */
	private ControleCarga controleCarga;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private ControleTrecho controleTrecho;

	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private Filial filialByIdFilialControleCarga;

	/** nullable persistent field */
	private SolicitacaoRetirada solicitacaoRetirada;

	/** persistent field */
	private List<SmpManifesto> smpManifestos;

	/** persistent field */
	private List ocorrenciaNaoConformidades;

	/** persistent field */
	private List<CarregamentoPreManifesto> carregamentoPreManifestos;

	/** persistent field */
	private List descargaManifestos;

	/** persistent field */
	private List<PreManifestoDocumento> preManifestoDocumentos;

	private List<PreManifestoVolume> preManifestoVolumes;

	/** persistent field */
	private List eventoManifestos;

	/** persistent field */
	private List faturas;

	private Integer versao;
	
	public Manifesto() {
		
	}
	
	public Manifesto(Long idManifesto, Long qntManif, Long idControleCarga, Long nrPreManifesto, DateTime dhGeracaoPreManifesto, DomainValue tpManifesto, BigDecimal psTotalManifesto, BigDecimal vlTotalManifesto, BigDecimal psTotalAforadoManifesto, DateTime dhEmissaoManifesto, DomainValue tpManifestoViagem, DomainValue tpModal, DomainValue tpManifestoEntrega, DomainValue tpAbrangencia, DomainValue tpStatusManifesto, BigDecimal vlRateioPedagio, BigDecimal vlRateioFreteCarreteiro, String obManifesto, Boolean blBloqueado, BigDecimal vlTotalManifestoEmissao, BigDecimal psTotalManifestoEmissao, Integer qtTotalVolumesEmissao) {
		this.idManifesto = idManifesto;
		this.qntManif = qntManif;
		this.idControleCarga = idControleCarga;
		this.nrPreManifesto = nrPreManifesto;
		this.dhGeracaoPreManifesto = dhGeracaoPreManifesto;
		this.tpManifesto = tpManifesto;
		this.psTotalManifesto = psTotalManifesto;
		this.vlTotalManifesto = vlTotalManifesto;
		this.psTotalAforadoManifesto = psTotalAforadoManifesto;
		this.dhEmissaoManifesto = dhEmissaoManifesto;
		this.tpManifestoViagem = tpManifestoViagem;
		this.tpModal = tpModal;
		this.tpManifestoEntrega = tpManifestoEntrega;
		this.tpAbrangencia = tpAbrangencia;
		this.tpStatusManifesto = tpStatusManifesto;
		this.vlRateioPedagio = vlRateioPedagio;
		this.vlRateioFreteCarreteiro = vlRateioFreteCarreteiro;
		this.obManifesto = obManifesto;
		this.blBloqueado = blBloqueado;
		this.vlTotalManifestoEmissao = vlTotalManifestoEmissao;
		this.psTotalManifestoEmissao = psTotalManifestoEmissao;
		this.qtTotalVolumesEmissao = qtTotalVolumesEmissao;
	}

	public Manifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}

	public Manifesto(Long idManifesto, Long qntManif, Long idControleCarga, Long nrPreManifesto, DateTime dhGeracaoPreManifesto, DomainValue tpManifesto, BigDecimal psTotalManifesto, BigDecimal vlTotalManifesto, BigDecimal psTotalAforadoManifesto, DateTime dhEmissaoManifesto, DomainValue tpManifestoViagem, DomainValue tpModal, DomainValue tpManifestoEntrega, DomainValue tpAbrangencia, DomainValue tpStatusManifesto, BigDecimal vlRateioPedagio, BigDecimal vlRateioFreteCarreteiro, String obManifesto, Boolean blBloqueado, BigDecimal vlTotalManifestoEmissao, BigDecimal psTotalManifestoEmissao, Integer qtTotalVolumesEmissao, BigDecimal vlTotalFreteCifEmissao, BigDecimal vlTotalFreteFobEmissao, BigDecimal vlTotalFreteEmissao, Integer nrOrdem, ManifestoEntrega manifestoEntrega, ManifestoInternacional manifestoInternacional, ManifestoViagemNacional manifestoViagemNacional, ControleCarga controleCarga, Cliente cliente, ControleTrecho controleTrecho, Moeda moeda, Filial filialByIdFilialDestino, Filial filialByIdFilialOrigem, Filial filialByIdFilialControleCarga, SolicitacaoRetirada solicitacaoRetirada, List<SmpManifesto> smpManifestos, List ocorrenciaNaoConformidades, List<CarregamentoPreManifesto> carregamentoPreManifestos, List descargaManifestos, List<PreManifestoDocumento> preManifestoDocumentos, List<PreManifestoVolume> preManifestoVolumes, List eventoManifestos, List faturas, Integer versao) {
		this.idManifesto = idManifesto;
		this.qntManif = qntManif;
		this.idControleCarga = idControleCarga;
		this.nrPreManifesto = nrPreManifesto;
		this.dhGeracaoPreManifesto = dhGeracaoPreManifesto;
		this.tpManifesto = tpManifesto;
		this.psTotalManifesto = psTotalManifesto;
		this.vlTotalManifesto = vlTotalManifesto;
		this.psTotalAforadoManifesto = psTotalAforadoManifesto;
		this.dhEmissaoManifesto = dhEmissaoManifesto;
		this.tpManifestoViagem = tpManifestoViagem;
		this.tpModal = tpModal;
		this.tpManifestoEntrega = tpManifestoEntrega;
		this.tpAbrangencia = tpAbrangencia;
		this.tpStatusManifesto = tpStatusManifesto;
		this.vlRateioPedagio = vlRateioPedagio;
		this.vlRateioFreteCarreteiro = vlRateioFreteCarreteiro;
		this.obManifesto = obManifesto;
		this.blBloqueado = blBloqueado;
		this.vlTotalManifestoEmissao = vlTotalManifestoEmissao;
		this.psTotalManifestoEmissao = psTotalManifestoEmissao;
		this.qtTotalVolumesEmissao = qtTotalVolumesEmissao;
		this.vlTotalFreteCifEmissao = vlTotalFreteCifEmissao;
		this.vlTotalFreteFobEmissao = vlTotalFreteFobEmissao;
		this.vlTotalFreteEmissao = vlTotalFreteEmissao;
		this.nrOrdem = nrOrdem;
		this.manifestoEntrega = manifestoEntrega;
		this.manifestoInternacional = manifestoInternacional;
		this.manifestoViagemNacional = manifestoViagemNacional;
		this.controleCarga = controleCarga;
		this.cliente = cliente;
		this.controleTrecho = controleTrecho;
		this.moeda = moeda;
		this.filialByIdFilialDestino = filialByIdFilialDestino;
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
		this.filialByIdFilialControleCarga = filialByIdFilialControleCarga;
		this.solicitacaoRetirada = solicitacaoRetirada;
		this.smpManifestos = smpManifestos;
		this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
		this.carregamentoPreManifestos = carregamentoPreManifestos;
		this.descargaManifestos = descargaManifestos;
		this.preManifestoDocumentos = preManifestoDocumentos;
		this.preManifestoVolumes = preManifestoVolumes;
		this.eventoManifestos = eventoManifestos;
		this.faturas = faturas;
		this.versao = versao;
	}

	public Long getIdManifesto() {
		return this.idManifesto;
	}

	public void setIdManifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}

	public Long getNrPreManifesto() {
		return this.nrPreManifesto;
	}

	public void setNrPreManifesto(Long nrPreManifesto) {
		this.nrPreManifesto = nrPreManifesto;
	}

	public DateTime getDhGeracaoPreManifesto() {
		return this.dhGeracaoPreManifesto;
	}

	public void setDhGeracaoPreManifesto(DateTime dhGeracaoPreManifesto) {
		this.dhGeracaoPreManifesto = dhGeracaoPreManifesto;
	}

	public DomainValue getTpManifesto() {
		return this.tpManifesto;
	}

	public void setTpManifesto(DomainValue tpManifesto) {
		this.tpManifesto = tpManifesto;
	}

	public DateTime getDhEmissaoManifesto() {
		return this.dhEmissaoManifesto;
	}

	public void setDhEmissaoManifesto(DateTime dhEmissaoManifesto) {
		this.dhEmissaoManifesto = dhEmissaoManifesto;
	}

	public DomainValue getTpManifestoViagem() {
		return this.tpManifestoViagem;
	}

	public void setTpManifestoViagem(DomainValue tpManifestoViagem) {
		this.tpManifestoViagem = tpManifestoViagem;
	}

	public DomainValue getTpModal() {
		return this.tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpManifestoEntrega() {
		return this.tpManifestoEntrega;
	}

	public void setTpManifestoEntrega(DomainValue tpManifestoEntrega) {
		this.tpManifestoEntrega = tpManifestoEntrega;
	}

	public DomainValue getTpAbrangencia() {
		return this.tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpStatusManifesto() {
		return this.tpStatusManifesto;
	}

	public void setTpStatusManifesto(DomainValue tpStatusManifesto) {
		this.tpStatusManifesto = tpStatusManifesto;
	}

	public BigDecimal getVlRateioPedagio() {
		return this.vlRateioPedagio;
	}

	public void setVlRateioPedagio(BigDecimal vlRateioPedagio) {
		this.vlRateioPedagio = vlRateioPedagio;
	}

	public BigDecimal getVlRateioFreteCarreteiro() {
		return this.vlRateioFreteCarreteiro;
	}

	public void setVlRateioFreteCarreteiro(BigDecimal vlRateioFreteCarreteiro) {
		this.vlRateioFreteCarreteiro = vlRateioFreteCarreteiro;
	}

	public String getObManifesto() {
		return this.obManifesto;
	}

	public void setObManifesto(String obManifesto) {
		this.obManifesto = obManifesto;
	}

	public Boolean getBlBloqueado() {
		return blBloqueado;
	}

	public void setBlBloqueado(Boolean blBloqueado) {
		this.blBloqueado = blBloqueado;
	}

	public Integer getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Integer nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	public BigDecimal getPsTotalManifestoEmissao() {
		return psTotalManifestoEmissao;
	}

	public void setPsTotalManifestoEmissao(BigDecimal psTotalManifestoEmissao) {
		this.psTotalManifestoEmissao = psTotalManifestoEmissao;
	}

	public Integer getQtTotalVolumesEmissao() {
		return qtTotalVolumesEmissao;
	}

	public void setQtTotalVolumesEmissao(Integer qtTotalVolumesEmissao) {
		this.qtTotalVolumesEmissao = qtTotalVolumesEmissao;
	}

	public BigDecimal getVlTotalFreteCifEmissao() {
		return vlTotalFreteCifEmissao;
	}

	public void setVlTotalFreteCifEmissao(BigDecimal vlTotalFreteCifEmissao) {
		this.vlTotalFreteCifEmissao = vlTotalFreteCifEmissao;
	}

	public BigDecimal getVlTotalFreteEmissao() {
		return vlTotalFreteEmissao;
	}

	public void setVlTotalFreteEmissao(BigDecimal vlTotalFreteEmissao) {
		this.vlTotalFreteEmissao = vlTotalFreteEmissao;
	}

	public BigDecimal getVlTotalFreteFobEmissao() {
		return vlTotalFreteFobEmissao;
	}

	public void setVlTotalFreteFobEmissao(BigDecimal vlTotalFreteFobEmissao) {
		this.vlTotalFreteFobEmissao = vlTotalFreteFobEmissao;
	}

	public BigDecimal getVlTotalManifestoEmissao() {
		return vlTotalManifestoEmissao;
	}

	public void setVlTotalManifestoEmissao(BigDecimal vlTotalManifestoEmissao) {
		this.vlTotalManifestoEmissao = vlTotalManifestoEmissao;
	}

	public ManifestoInternacional getManifestoInternacional() {
		return this.manifestoInternacional;
	}

	public void setManifestoInternacional(
			ManifestoInternacional manifestoInternacional) {
		this.manifestoInternacional = manifestoInternacional;
	}

	public ManifestoViagemNacional getManifestoViagemNacional() {
		return this.manifestoViagemNacional;
	}

	public void setManifestoViagemNacional(
			ManifestoViagemNacional manifestoViagemNacional) {
		this.manifestoViagemNacional = manifestoViagemNacional;
	}

	public ControleCarga getControleCarga() {
		return this.controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ControleTrecho getControleTrecho() {
		return this.controleTrecho;
	}

	public void setControleTrecho(ControleTrecho controleTrecho) {
		this.controleTrecho = controleTrecho;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public BigDecimal getPsTotalAforadoManifesto() {
		return psTotalAforadoManifesto;
	}
	
	public void setPsTotalAforadoManifesto(BigDecimal psTotalAforadoManifesto) {
		this.psTotalAforadoManifesto = psTotalAforadoManifesto;
	}
	
	public BigDecimal getPsTotalManifesto() {
		return psTotalManifesto;
	}
	
	public void setPsTotalManifesto(BigDecimal psTotalManifesto) {
		this.psTotalManifesto = psTotalManifesto;
	}
	
	public BigDecimal getVlTotalManifesto() {
		return vlTotalManifesto;
	}
	
	public void setVlTotalManifesto(BigDecimal vlTotalManifesto) {
		this.vlTotalManifesto = vlTotalManifesto;
	}
	
	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Filial getFilialByIdFilialControleCarga() {
		return filialByIdFilialControleCarga;
	}

	public void setFilialByIdFilialControleCarga(
			Filial filialByIdFilialControleCarga) {
		this.filialByIdFilialControleCarga = filialByIdFilialControleCarga;
	}

	@ParametrizedAttribute(type = ManifestoEntrega.class)	 
	public ManifestoEntrega getManifestoEntrega() {
		return this.manifestoEntrega;
	}

	public void setManifestoEntrega(ManifestoEntrega manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}

	@ParametrizedAttribute(type = OcorrenciaNaoConformidade.class)	 
	public List getOcorrenciaNaoConformidades() {
		return this.ocorrenciaNaoConformidades;
	}

	public void setOcorrenciaNaoConformidades(List ocorrenciaNaoConformidades) {
		this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
	}

	@ParametrizedAttribute(type = CarregamentoPreManifesto.class)	 
	public List<CarregamentoPreManifesto> getCarregamentoPreManifestos() {
		return this.carregamentoPreManifestos;
	}

	public void setCarregamentoPreManifestos(
			List<CarregamentoPreManifesto> carregamentoPreManifestos) {
		this.carregamentoPreManifestos = carregamentoPreManifestos;
	}

	@ParametrizedAttribute(type = DescargaManifesto.class)	 
	public List getDescargaManifestos() {
		return this.descargaManifestos;
	}

	public void setDescargaManifestos(List descargaManifestos) {
		this.descargaManifestos = descargaManifestos;
	}

	@ParametrizedAttribute(type = PreManifestoDocumento.class)	 
	public List<PreManifestoDocumento> getPreManifestoDocumentos() {
		return this.preManifestoDocumentos;
	}

	public void setPreManifestoDocumentos(
			List<PreManifestoDocumento> preManifestoDocumentos) {
		this.preManifestoDocumentos = preManifestoDocumentos;
	}

	@ParametrizedAttribute(type = PreManifestoVolume.class)	 
	public List<PreManifestoVolume> getPreManifestoVolumes() {
		return preManifestoVolumes;
	}

	public void setPreManifestoVolumes(List<PreManifestoVolume> preManifestoVolumes) {
		this.preManifestoVolumes = preManifestoVolumes;
	}

	@ParametrizedAttribute(type = EventoManifesto.class)	 
	public List getEventoManifestos() {
		return this.eventoManifestos;
	}

	public void setEventoManifestos(List eventoManifestos) {
		this.eventoManifestos = eventoManifestos;
	}

	@ParametrizedAttribute(type = Fatura.class)	 
	public List getFaturas() {
		return this.faturas;
	}

	public void setFaturas(List faturas) {
		this.faturas = faturas;
	}
	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}	 

	public String toString() {
		return new ToStringBuilder(this)
				.append("idManifesto", getIdManifesto()).toString();
	}

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_MANIFESTO", idManifesto)
				.append("ID_FILIAL_ORIGEM", filialByIdFilialOrigem != null ? filialByIdFilialOrigem.getIdFilial() : null)
				.append("ID_FILIAL_DESTINO", filialByIdFilialDestino != null ? filialByIdFilialDestino.getIdFilial() : null)
				.append("ID_CONTROLE_CARGA", idControleCarga)
				.append("ID_CONTROLE_TRECHO", controleTrecho != null ? controleTrecho.getIdControleTrecho() : null)
				.append("NR_PRE_MANIFESTO", nrPreManifesto)
				.append("DH_GERACAO_PRE_MANIFESTO", dhGeracaoPreManifesto)
				.append("TP_MANIFESTO", tpManifesto != null ? tpManifesto.getValue() : null)
				.append("ID_CLIENTE_CONSIG", cliente != null ? cliente.getIdCliente() : null)
				.append("ID_MOEDA", moeda != null ? moeda.getIdMoeda() : null)
				.append("DH_EMISSAO_MANIFESTO", dhEmissaoManifesto)
				.append("TP_MANIFESTO_VIAGEM", tpManifestoViagem != null ? tpManifestoViagem.getValue() : null)
				.append("TP_MODAL", tpModal != null ? tpModal.getValue() : null)
				.append("TP_MANIFESTO_ENTREGA", tpManifestoEntrega != null ? tpManifestoEntrega.getValue() : null)
				.append("TP_ABRANGENCIA", tpAbrangencia != null ? tpAbrangencia.getValue() : null)
				.append("TP_STATUS_MANIFESTO", tpStatusManifesto != null ? tpStatusManifesto.getValue() : null)
				.append("VL_RATEIO_PEDAGIO", vlRateioPedagio)
				.append("VL_RATEIO_FRETE_CARRETEIRO", vlRateioFreteCarreteiro)
				.append("VL_TOTAL_MANIFESTO", vlTotalManifesto)
				.append("PS_TOTAL_MANIFESTO", psTotalManifesto)
				.append("PS_TOTAL_AFORADO_MANIFESTO", psTotalAforadoManifesto)
				.append("OB_MANIFESTO", obManifesto)
				.append("ID_SOLICITACAO_RETIRADA", solicitacaoRetirada != null ? solicitacaoRetirada.getIdSolicitacaoRetirada() : null)
				.append("NR_VERSAO", versao)
				.append("VL_TOTAL_MANIFESTO_EMISSAO", vlTotalFreteEmissao)
				.append("PS_TOTAL_MANIFESTO_EMISSAO", psTotalManifestoEmissao)
				.append("QT_TOTAL_VOLUMES_EMISSAO", qtTotalVolumesEmissao)
				.append("VL_TOTAL_FRETE_CIF_EMISSAO", vlTotalFreteCifEmissao)
				.append("VL_TOTAL_FRETE_FOB_EMISSAO", vlTotalFreteFobEmissao)
				.append("VL_TOTAL_FRETE_EMISSAO", vlTotalFreteEmissao)
				.append("NR_ORDEM", nrOrdem)
				.append("BL_BLOQUEADO", blBloqueado)
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Manifesto))
			return false;
		Manifesto castOther = (Manifesto) other;
		return new EqualsBuilder().append(this.getIdManifesto(),
				castOther.getIdManifesto()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdManifesto()).toHashCode();
	}

	/**
	 * @return Returns the solicitacaoRetirada.
	 */
	public SolicitacaoRetirada getSolicitacaoRetirada() {
		return solicitacaoRetirada;
	}

	/**
	 * @param solicitacaoRetirada
	 *            The solicitacaoRetirada to set.
	 */
	public void setSolicitacaoRetirada(SolicitacaoRetirada solicitacaoRetirada) {
		this.solicitacaoRetirada = solicitacaoRetirada;
	}

	public List<SmpManifesto> getSmpManifestos() {
		return smpManifestos;
	}

	public void setSmpManifestos(List<SmpManifesto> smpManifestos) {
		this.smpManifestos = smpManifestos;
	}

	public Long getQntManif() {
		return qntManif;
	}

	public void setQntManif(Long qntManif) {
		this.qntManif = qntManif;
	}

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}
	
	}
