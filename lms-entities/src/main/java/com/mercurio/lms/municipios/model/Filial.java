package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Filial implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Filial() {
	}

	public Filial(Long idFilial) {
		this.idFilial = idFilial;
	}
	
	/** identifier field */
	private Long idFilial;

	/** persistent field */
	private String sgFilial;

	/** persistent field */
	private Integer codFilial;	
	
	/** persistent field */
	private Boolean blEmiteBoletoEntrega;

	/** persistent field */
	private Boolean blEmiteBoletoFaturamento;

	/** persistent field */
	private Boolean blEmiteReciboFrete;

	/** persistent field */
	private Boolean blColetorDadoScan;

	/** persistent field */
	private Boolean blRecebeVeiculosSemColeta;
	
	private Boolean blConfereDoctoDescarga;
	
	private Boolean blValidaLocalVeiculo;
	
	private Regional lastRegional;

	/** persistent field */
	private Boolean blInformaKmPortaria;

	/** persistent field */
	private Boolean blOrdenaEntregaValor;
	
	private HistoricoFilial lastHistoricoFilial;

	/** persistent field */
	private Boolean blObrigaBaixaEntregaOrdem;

	/** persistent field */
	private Byte nrPrazoCobranca;

	/** nullable persistent field */
	private BigDecimal vlCustoReembarque;

	/** nullable persistent field */
	private Short nrCentroCusto;

	/** nullable persistent field */
	private BigDecimal pcJuroDiario;

	private BigDecimal pcFreteCarreteiro;

	/** nullable persistent field */
	private Integer nrFranquiaPeso;

	/** nullable persistent field */
	private Integer nrFranquiaKm;

	/** nullable persistent field */
	private BigDecimal nrAreaTotal;

	/** nullable persistent field */
	private BigDecimal nrAreaArmazenagem;

	/** nullable persistent field */
	private Long nrInscricaoMunicipal;

	/** nullable persistent field */
	private String dsHomepage;

	/** nullable persistent field */
	private String obFilial;
	
	/** nullable persistent field */
	private String obAprovacao;
	
	/** nullable persistent field */
	private String idFilialConcatenado;
	
	private Long meioTransporteProprioCount;
	
	private Long meioTransporteAgregadoCount;
	
	private YearMonthDay dtImplantacaoLMS;
	
	private String dsTimezone;
	
	private String nrDdr;
	
	private String nrDddAgenda;
	
	private String nrTelefoneAgenda;
	
	private String cdFilialFedex;
	
	private Integer nrHrColeta;
	
	/** nullable persistent field */
	private Pessoa pessoa;

	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private Empresa empresa;
	
	private Empresa franqueado;

	/** persistent field */
	private Pendencia pendencia;

	/** persistent field */
	private Filial filialByIdFilialResponsavalAwb;

	/** persistent field */
	private Filial filialByIdFilialResponsavel;

	/** persistent field */
	private Cedente cedenteByIdCedente;

	/** persistent field */
	private Cedente cedenteByIdCedenteBloqueto;
	
	/** persistent field */
	private Aeroporto aeroporto;

	private PostoConveniado postoConveniado;

	/** persistent field */
	private Boolean blLiberaFobAereo;

	/** persistent field */
	private List<FilialRota> filialRotas;

	/** persistent field */
	private List<MunicipioFilial> municipioFiliais;

	/** persistent field */
	private List<RegionalFilial> regionalFiliais;

	/** persistent field */
	private List<FotoFilial> fotoFiliais;

	/** persistent field */
	private List<Turno> turnos;

	/** persistent field */
	private List<HistoricoFilial> historicoFiliais;

	/** persistent field */
	private DomainValue tpSistema;

	private TimeOfDay hrCorte;

	/** persistent field */
	private Boolean blLimitaNotaFiscalForm;

	/** persistent field */
	private Boolean blPagaDiariaExcedente;

	private List<Cliente> clientesByIdFilialAtendeOperacional; 

	/**
	 * blSorter identifies if has this object has sorter  
	 */
	private Boolean blSorter;

	private Boolean blWorkflowKm;

	private Boolean blGeraContratacaoRetornoVazio;
	
	private Boolean blRestrEntrOutrasFiliais;
		
	private Boolean blRestringeCCVinculo;
	
	private Boolean blRncAutomaticaCarregamento;
	
	private Boolean blRncAutomaticaDescarga;
	
	private Boolean blRncAutomaticaDescargaMww;
		
	private DomainValue tpOrdemDoc;

	public Filial(Long idFilial, String sgFilial, Integer codFilial, Boolean blEmiteBoletoEntrega, Boolean blEmiteBoletoFaturamento, Boolean blEmiteReciboFrete, Boolean blColetorDadoScan, Boolean blRecebeVeiculosSemColeta, Boolean blConfereDoctoDescarga, Boolean blValidaLocalVeiculo, Regional lastRegional, Boolean blInformaKmPortaria, Boolean blOrdenaEntregaValor, HistoricoFilial lastHistoricoFilial, Boolean blObrigaBaixaEntregaOrdem, Byte nrPrazoCobranca, BigDecimal vlCustoReembarque, Short nrCentroCusto, BigDecimal pcJuroDiario, BigDecimal pcFreteCarreteiro, Integer nrFranquiaPeso, Integer nrFranquiaKm, BigDecimal nrAreaTotal, BigDecimal nrAreaArmazenagem, Long nrInscricaoMunicipal, String dsHomepage, String obFilial, String obAprovacao, String idFilialConcatenado, Long meioTransporteProprioCount, Long meioTransporteAgregadoCount, YearMonthDay dtImplantacaoLMS, String dsTimezone, String nrDdr, String nrDddAgenda, String nrTelefoneAgenda, String cdFilialFedex, Pessoa pessoa, Moeda moeda, Empresa empresa, Empresa franqueado, Pendencia pendencia, Filial filialByIdFilialResponsavalAwb, Filial filialByIdFilialResponsavel, Cedente cedenteByIdCedente, Cedente cedenteByIdCedenteBloqueto, Aeroporto aeroporto, PostoConveniado postoConveniado, Boolean blLiberaFobAereo, List<FilialRota> filialRotas, List<MunicipioFilial> municipioFiliais, List<RegionalFilial> regionalFiliais, List<FotoFilial> fotoFiliais, List<Turno> turnos, List<HistoricoFilial> historicoFiliais, DomainValue tpSistema, TimeOfDay hrCorte, Boolean blLimitaNotaFiscalForm, Boolean blPagaDiariaExcedente, List<Cliente> clientesByIdFilialAtendeOperacional, Boolean blSorter, Boolean blWorkflowKm, Boolean blGeraContratacaoRetornoVazio, Boolean blRestrEntrOutrasFiliais, Boolean blRestringeCCVinculo, Boolean blRncAutomaticaCarregamento, Boolean blRncAutomaticaDescarga, Boolean blRncAutomaticaDescargaMww, DomainValue tpOrdemDoc) {
		this.idFilial = idFilial;
		this.sgFilial = sgFilial;
		this.codFilial = codFilial;
		this.blEmiteBoletoEntrega = blEmiteBoletoEntrega;
		this.blEmiteBoletoFaturamento = blEmiteBoletoFaturamento;
		this.blEmiteReciboFrete = blEmiteReciboFrete;
		this.blColetorDadoScan = blColetorDadoScan;
		this.blRecebeVeiculosSemColeta = blRecebeVeiculosSemColeta;
		this.blConfereDoctoDescarga = blConfereDoctoDescarga;
		this.blValidaLocalVeiculo = blValidaLocalVeiculo;
		this.lastRegional = lastRegional;
		this.blInformaKmPortaria = blInformaKmPortaria;
		this.blOrdenaEntregaValor = blOrdenaEntregaValor;
		this.lastHistoricoFilial = lastHistoricoFilial;
		this.blObrigaBaixaEntregaOrdem = blObrigaBaixaEntregaOrdem;
		this.nrPrazoCobranca = nrPrazoCobranca;
		this.vlCustoReembarque = vlCustoReembarque;
		this.nrCentroCusto = nrCentroCusto;
		this.pcJuroDiario = pcJuroDiario;
		this.pcFreteCarreteiro = pcFreteCarreteiro;
		this.nrFranquiaPeso = nrFranquiaPeso;
		this.nrFranquiaKm = nrFranquiaKm;
		this.nrAreaTotal = nrAreaTotal;
		this.nrAreaArmazenagem = nrAreaArmazenagem;
		this.nrInscricaoMunicipal = nrInscricaoMunicipal;
		this.dsHomepage = dsHomepage;
		this.obFilial = obFilial;
		this.obAprovacao = obAprovacao;
		this.idFilialConcatenado = idFilialConcatenado;
		this.meioTransporteProprioCount = meioTransporteProprioCount;
		this.meioTransporteAgregadoCount = meioTransporteAgregadoCount;
		this.dtImplantacaoLMS = dtImplantacaoLMS;
		this.dsTimezone = dsTimezone;
		this.nrDdr = nrDdr;
		this.nrDddAgenda = nrDddAgenda;
		this.nrTelefoneAgenda = nrTelefoneAgenda;
		this.cdFilialFedex = cdFilialFedex;
		this.pessoa = pessoa;
		this.moeda = moeda;
		this.empresa = empresa;
		this.franqueado = franqueado;
		this.pendencia = pendencia;
		this.filialByIdFilialResponsavalAwb = filialByIdFilialResponsavalAwb;
		this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
		this.cedenteByIdCedente = cedenteByIdCedente;
		this.cedenteByIdCedenteBloqueto = cedenteByIdCedenteBloqueto;
		this.aeroporto = aeroporto;
		this.postoConveniado = postoConveniado;
		this.blLiberaFobAereo = blLiberaFobAereo;
		this.filialRotas = filialRotas;
		this.municipioFiliais = municipioFiliais;
		this.regionalFiliais = regionalFiliais;
		this.fotoFiliais = fotoFiliais;
		this.turnos = turnos;
		this.historicoFiliais = historicoFiliais;
		this.tpSistema = tpSistema;
		this.hrCorte = hrCorte;
		this.blLimitaNotaFiscalForm = blLimitaNotaFiscalForm;
		this.blPagaDiariaExcedente = blPagaDiariaExcedente;
		this.clientesByIdFilialAtendeOperacional = clientesByIdFilialAtendeOperacional;
		this.blSorter = blSorter;
		this.blWorkflowKm = blWorkflowKm;
		this.blGeraContratacaoRetornoVazio = blGeraContratacaoRetornoVazio;
		this.blRestrEntrOutrasFiliais = blRestrEntrOutrasFiliais;
		this.blRestringeCCVinculo = blRestringeCCVinculo;
		this.blRncAutomaticaCarregamento = blRncAutomaticaCarregamento;
		this.blRncAutomaticaDescarga = blRncAutomaticaDescarga;
		this.blRncAutomaticaDescargaMww = blRncAutomaticaDescargaMww;
		this.tpOrdemDoc = tpOrdemDoc;
	}

	public Boolean getBlSorter() {
		return blSorter;
	}

	public void setBlSorter(Boolean blSorter) {
		this.blSorter = blSorter;
	}
	
	public Boolean getBlRestrEntrOutrasFiliais() {
		return blRestrEntrOutrasFiliais;
	}

	public void setBlRestrEntrOutrasFiliais(Boolean blRestrEntrOutrasFiliais) {
		this.blRestrEntrOutrasFiliais = blRestrEntrOutrasFiliais;
	}

	public Long getIdFilial() {
		return this.idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public String getSgFilial() {
		return this.sgFilial;
	}

	public String getSiglaNomeFilial() {
		if (Hibernate.isInitialized(this.pessoa) && this.pessoa != null){
			return this.sgFilial + " - " + this.getPessoa().getNmFantasia();
		}
		return this.sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Integer getCodFilial() {
		return codFilial;
	}

	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
	}
	
	public Boolean getBlEmiteBoletoEntrega() {
		return this.blEmiteBoletoEntrega;
	}

	public void setBlEmiteBoletoEntrega(Boolean blEmiteBoletoEntrega) {
		this.blEmiteBoletoEntrega = blEmiteBoletoEntrega;
	}

	public Boolean getBlEmiteBoletoFaturamento() {
		return this.blEmiteBoletoFaturamento;
	}

	public void setBlEmiteBoletoFaturamento(Boolean blEmiteBoletoFaturamento) {
		this.blEmiteBoletoFaturamento = blEmiteBoletoFaturamento;
	}

	public Boolean getBlEmiteReciboFrete() {
		return this.blEmiteReciboFrete;
	}

	public void setBlEmiteReciboFrete(Boolean blEmiteReciboFrete) {
		this.blEmiteReciboFrete = blEmiteReciboFrete;
	}

	public Boolean getBlRecebeVeiculosSemColeta() {
		return this.blRecebeVeiculosSemColeta;
	}

	public void setBlRecebeVeiculosSemColeta(Boolean blRecebeVeiculosSemColeta) {
		this.blRecebeVeiculosSemColeta = blRecebeVeiculosSemColeta;
	}

	public Boolean getBlInformaKmPortaria() {
		return this.blInformaKmPortaria;
	}

	public void setBlInformaKmPortaria(Boolean blInformaKmPortaria) {
		this.blInformaKmPortaria = blInformaKmPortaria;
	}

	public Boolean getBlOrdenaEntregaValor() {
		return this.blOrdenaEntregaValor;
	}

	public void setBlOrdenaEntregaValor(Boolean blOrdenaEntregaValor) {
		this.blOrdenaEntregaValor = blOrdenaEntregaValor;
	}

	public Boolean getBlObrigaBaixaEntregaOrdem() {
		return this.blObrigaBaixaEntregaOrdem;
	}

	public void setBlObrigaBaixaEntregaOrdem(Boolean blObrigaBaixaEntregaOrdem) {
		this.blObrigaBaixaEntregaOrdem = blObrigaBaixaEntregaOrdem;
	}

	public Byte getNrPrazoCobranca() {
		return this.nrPrazoCobranca;
	}

	public void setNrPrazoCobranca(Byte nrPrazoCobranca) {
		this.nrPrazoCobranca = nrPrazoCobranca;
	}

	public BigDecimal getVlCustoReembarque() {
		return this.vlCustoReembarque;
	}

	public void setVlCustoReembarque(BigDecimal vlCustoReembarque) {
		this.vlCustoReembarque = vlCustoReembarque;
	}

	public Short getNrCentroCusto() {
		return this.nrCentroCusto;
	}

	public void setNrCentroCusto(Short nrCentroCusto) {
		this.nrCentroCusto = nrCentroCusto;
	}

	public BigDecimal getPcJuroDiario() {
		return this.pcJuroDiario;
	}

	public void setPcJuroDiario(BigDecimal pcJuroDiario) {
		this.pcJuroDiario = pcJuroDiario;
	}

	public Integer getNrFranquiaPeso() {
		return this.nrFranquiaPeso;
	}

	public void setNrFranquiaPeso(Integer nrFranquiaPeso) {
		this.nrFranquiaPeso = nrFranquiaPeso;
	}

	public Integer getNrFranquiaKm() {
		return this.nrFranquiaKm;
	}

	public void setNrFranquiaKm(Integer nrFranquiaKm) {
		this.nrFranquiaKm = nrFranquiaKm;
	}

	public BigDecimal getNrAreaTotal() {
		return this.nrAreaTotal;
	}

	public void setNrAreaTotal(BigDecimal nrAreaTotal) {
		this.nrAreaTotal = nrAreaTotal;
	}

	public BigDecimal getNrAreaArmazenagem() {
		return this.nrAreaArmazenagem;
	}

	public void setNrAreaArmazenagem(BigDecimal nrAreaArmazenagem) {
		this.nrAreaArmazenagem = nrAreaArmazenagem;
	}

	public Long getNrInscricaoMunicipal() {
		return this.nrInscricaoMunicipal;
	}

	public void setNrInscricaoMunicipal(Long nrInscricaoMunicipal) {
		this.nrInscricaoMunicipal = nrInscricaoMunicipal;
	}

	public String getDsHomepage() {
		return this.dsHomepage;
	}

	public void setDsHomepage(String dsHomepage) {
		this.dsHomepage = dsHomepage;
	}

	public String getObFilial() {
		return this.obFilial;
	}

	public void setObFilial(String obFilial) {
		this.obFilial = obFilial;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Filial getFilialByIdFilialResponsavalAwb() {
		return this.filialByIdFilialResponsavalAwb;
	}

	public void setFilialByIdFilialResponsavalAwb(
			Filial filialByIdFilialResponsavalAwb) {
		this.filialByIdFilialResponsavalAwb = filialByIdFilialResponsavalAwb;
	}

	public Filial getFilialByIdFilialResponsavel() {
		return this.filialByIdFilialResponsavel;
	}

	public void setFilialByIdFilialResponsavel(
			Filial filialByIdFilialResponsavel) {
		this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
	}

	public Cedente getCedenteByIdCedente() {
		return this.cedenteByIdCedente;
	}

	public void setCedenteByIdCedente(Cedente cedenteByIdCedente) {
		this.cedenteByIdCedente = cedenteByIdCedente;
	}

	public Cedente getCedenteByIdCedenteBloqueto() {
		return this.cedenteByIdCedenteBloqueto;
	}

	public void setCedenteByIdCedenteBloqueto(Cedente cedenteByIdCedenteBloqueto) {
		this.cedenteByIdCedenteBloqueto = cedenteByIdCedenteBloqueto;
	}

	public Long getMeioTransporteAgregadoCount() {
		return meioTransporteAgregadoCount;
	}

	public void setMeioTransporteAgregadoCount(Long meioTransporteAgregadoCount) {
		this.meioTransporteAgregadoCount = meioTransporteAgregadoCount;
	}

	public Long getMeioTransporteProprioCount() {
		return meioTransporteProprioCount;
	}

	public void setMeioTransporteProprioCount(Long meioTransporteProprioCount) {
		this.meioTransporteProprioCount = meioTransporteProprioCount;
	}

	public Regional getLastRegional() {
		return lastRegional;
	}

	public void setLastRegional(Regional lastRegional) {
		this.lastRegional = lastRegional;
	}

	public HistoricoFilial getLastHistoricoFilial() {
		return lastHistoricoFilial;
	}

	public void setLastHistoricoFilial(HistoricoFilial lastHistoricoFilial) {
		this.lastHistoricoFilial = lastHistoricoFilial;
	}

	public String getIdFilialConcatenado() {
		return idFilialConcatenado;
	}

	public void setIdFilialConcatenado(String idFilialConcatenado) {
		this.idFilialConcatenado = idFilialConcatenado;
	}

	public YearMonthDay getDtImplantacaoLMS() {
		return dtImplantacaoLMS;
	}

	public void setDtImplantacaoLMS(YearMonthDay dtImplantacaoLMS) {
		this.dtImplantacaoLMS = dtImplantacaoLMS;
	}

	public String getObAprovacao() {
		return obAprovacao;
	}

	public void setObAprovacao(String obAprovacao) {
		this.obAprovacao = obAprovacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Aeroporto getAeroporto() {
		return aeroporto;
	}

	public void setAeroporto(Aeroporto aeroporto) {
		this.aeroporto = aeroporto;
	}

	public BigDecimal getPcFreteCarreteiro() {
		return pcFreteCarreteiro;
	}

	public void setPcFreteCarreteiro(BigDecimal pcFreteCarreteiro) {
		this.pcFreteCarreteiro = pcFreteCarreteiro;
	}	
	
	public String getDsTimezone() {
		return dsTimezone;
	}

	public String getNrDdr() {
		return nrDdr;
	}

	public void setNrDdr(String nrDdr) {
		this.nrDdr = nrDdr;
	}

	public String getNrDddAgenda() {
		return nrDddAgenda;
	}

	public void setNrDddAgenda(String nrDddAgenda) {
		this.nrDddAgenda = nrDddAgenda;
	}

	public String getNrTelefoneAgenda() {
		return nrTelefoneAgenda;
	}

	public void setNrTelefoneAgenda(String nrTelefoneAgenda) {
		this.nrTelefoneAgenda = nrTelefoneAgenda;
	}

	public void setDsTimezone(String dsTimezone) {
		this.dsTimezone = dsTimezone;
	}

	public DateTimeZone getDateTimeZone() {
		if (this.dsTimezone != null)
			return DateTimeZone.forID(dsTimezone);
		return null;
	}
	
	public Empresa getFranqueado() {
		return franqueado;
	}

	public void setFranqueado(Empresa franqueado) {
		this.franqueado = franqueado;
	}

	public Boolean getBlLiberaFobAereo() {
		return blLiberaFobAereo;
	}

	public void setBlLiberaFobAereo(Boolean blLiberaFobAereo) {
		this.blLiberaFobAereo = blLiberaFobAereo;
	}

	@ParametrizedAttribute(type = FilialRota.class)
	public List<FilialRota> getFilialRotas() {
		return this.filialRotas;
	}

	public void setFilialRotas(List<FilialRota> filialRotas) {
		this.filialRotas = filialRotas;
	}

	@ParametrizedAttribute(type = MunicipioFilial.class) 
	public List<MunicipioFilial> getMunicipioFiliais() {
		return this.municipioFiliais;
	}

	public void setMunicipioFiliais(List<MunicipioFilial> municipioFiliais) {
		this.municipioFiliais = municipioFiliais;
	}

	@ParametrizedAttribute(type = RegionalFilial.class) 
	public List<RegionalFilial> getRegionalFiliais() {
		return this.regionalFiliais;
	}

	public void setRegionalFiliais(List<RegionalFilial> regionalFiliais) {
		this.regionalFiliais = regionalFiliais;
	}

	@ParametrizedAttribute(type = FotoFilial.class)
	public List<FotoFilial> getFotoFiliais() {
		return fotoFiliais;
	}

	public void setFotoFiliais(List<FotoFilial> fotoFiliais) {
		this.fotoFiliais = fotoFiliais;
	}

	@ParametrizedAttribute(type = Turno.class)
	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}

	@ParametrizedAttribute(type = HistoricoFilial.class)
	public List<HistoricoFilial> getHistoricoFiliais() {
		return historicoFiliais;
	}

	public void setHistoricoFiliais(List<HistoricoFilial> historicoFiliais) {
		this.historicoFiliais = historicoFiliais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFilial", getIdFilial())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Filial))
			return false;
		Filial castOther = (Filial) other;
		return new EqualsBuilder().append(this.getIdFilial(),
				castOther.getIdFilial()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFilial()).toHashCode();
	}

	public void setPostoConveniado(PostoConveniado postoConveniado) {
		this.postoConveniado = postoConveniado;
	}

	public PostoConveniado getPostoConveniado() {
		return postoConveniado;
	}

	/**
	 * @param blColetorDadoScan
	 *            the blColetorDadoScan to set
	 */
	public void setBlColetorDadoScan(Boolean blColetorDadoScan) {
		this.blColetorDadoScan = blColetorDadoScan;
	}

	public Boolean getBlWorkflowKm() {
		return blWorkflowKm;
	}

	public void setBlWorkflowKm(Boolean blWorkflowKm) {
		this.blWorkflowKm = blWorkflowKm;
	}

	public Boolean getBlGeraContratacaoRetornoVazio() {
		return blGeraContratacaoRetornoVazio;
	}

	public void setBlGeraContratacaoRetornoVazio(
			Boolean blGeraContratacaoRetornoVazio) {
		this.blGeraContratacaoRetornoVazio = blGeraContratacaoRetornoVazio;
	}

	/**
	 * @return the blColetorDadoScan
	 */
	public Boolean getBlColetorDadoScan() {
		return blColetorDadoScan;
	}

	public DomainValue getTpSistema() {
		return tpSistema;
	}

	public void setTpSistema(DomainValue tpSistema) {
		this.tpSistema = tpSistema;
	}

	public List<Cliente> getClientesByIdFilialAtendeOperacional() {
		return clientesByIdFilialAtendeOperacional;
	}

	public void setClientesByIdFilialAtendeOperacional(
			List<Cliente> clientesByIdFilialAtendeOperacional) {
		this.clientesByIdFilialAtendeOperacional = clientesByIdFilialAtendeOperacional;
	}

	public TimeOfDay getHrCorte() {
		return hrCorte;
	}

	public void setHrCorte(TimeOfDay hrCorte) {
		this.hrCorte = hrCorte;
	}

	public void setBlConfereDoctoDescarga(Boolean blConfereDoctoDescarga) {
		this.blConfereDoctoDescarga = blConfereDoctoDescarga;
	}

	public Boolean getBlConfereDoctoDescarga() {
		return blConfereDoctoDescarga;
	}

	/**
	 * @return the blLimitaNotaFiscalForm
	 */
	public Boolean getBlLimitaNotaFiscalForm() {
		return blLimitaNotaFiscalForm;
	}

	/**
	 * @param blLimitaNotaFiscalForm
	 *            the blLimitaNotaFiscalForm to set
	 */
	public void setBlLimitaNotaFiscalForm(Boolean blLimitaNotaFiscalForm) {
		this.blLimitaNotaFiscalForm = blLimitaNotaFiscalForm;
	}
	
	public Boolean getBlPagaDiariaExcedente() {
		return blPagaDiariaExcedente;
	}

	public void setBlPagaDiariaExcedente(Boolean blPagaDiariaExcedente) {
		this.blPagaDiariaExcedente = blPagaDiariaExcedente;
}
	
	public Boolean getBlValidaLocalVeiculo() {
		return blValidaLocalVeiculo;
	}

	public void setBlValidaLocalVeiculo(Boolean blValidaLocalVeiculo) {
		this.blValidaLocalVeiculo = blValidaLocalVeiculo;
}
	
	public DomainValue getTpOrdemDoc() {
		return tpOrdemDoc;
	}

	public void setTpOrdemDoc(DomainValue tpOrdemDoc) {
		this.tpOrdemDoc = tpOrdemDoc;
}
	
	public Boolean getBlRestringeCCVinculo() {
		return blRestringeCCVinculo;
	}

	public void setBlRestringeCCVinculo(Boolean blRestringeCCVinculo) {
		this.blRestringeCCVinculo = blRestringeCCVinculo;
	}
	
	public void setBlRncAutomaticaCarregamento(Boolean blRncAutomaticaCarregamento) {
		this.blRncAutomaticaCarregamento = blRncAutomaticaCarregamento;
	}
	
	public void setBlRncAutomaticaDescarga(Boolean blRncAutomaticaDescarga) {
		this.blRncAutomaticaDescarga = blRncAutomaticaDescarga;
	}
	
	public Boolean getBlRncAutomaticaCarregamento() {
		return blRncAutomaticaCarregamento;
	}
	
	public Boolean getBlRncAutomaticaDescarga() {
		return blRncAutomaticaDescarga;
	}
	
	public Boolean getBlRncAutomaticaDescargaMww() {
		return blRncAutomaticaDescargaMww;
	}
	
	public void setBlRncAutomaticaDescargaMww(Boolean blRncAutomaticaDescargaMww) {
		this.blRncAutomaticaDescargaMww = blRncAutomaticaDescargaMww;
	}

	public String getCdFilialFedex() {
		return cdFilialFedex;
	}

	public void setCdFilialFedex(String cdFilialFedex) {
		this.cdFilialFedex = cdFilialFedex;
	}

	public Integer getNrHrColeta() {
		return nrHrColeta;
	}

	public void setNrHrColeta(Integer nrHrColeta) {
		this.nrHrColeta = nrHrColeta;
	}
}
