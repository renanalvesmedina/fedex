package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.PermissoEmpresaPais;
import com.mercurio.lms.municipios.model.PontoParada;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class CtoInternacional extends DoctoServico { 
	private static final long serialVersionUID = 1L;

	/** persistent field */
	private Long nrCrt;

	/** persistent field */
	private Integer nrPermisso;

	/** persistent field */
	private BigDecimal psLiquido;

	/** persistent field */
	private BigDecimal vlVolume;

	/** persistent field */
	private BigDecimal vlTotalMercadoria;

	/** persistent field */
	private BigDecimal vlFreteExterno;

	/** persistent field */
	private String sgPais;

	/** persistent field */
	private String dsDadosRemetente;

	/** persistent field */
	private String dsNomeRemetente;

	/** persistent field */
	private String dsDadosDestinatario;

	/** persistent field */
	private String dsDadosConsignatario;

	/** persistent field */
	private String dsNotificar;

	/** persistent field */
	private String dsLocalEmissao;

	/** persistent field */
	private String dsLocalCarregamento;

	/** persistent field */
	private String dsLocalEntrega;

	/** persistent field */
	private String dsTransportesSucessivos;

	/** persistent field */
	private String dsDadosMercadoria;

	/** persistent field */
	private String dsValorMercadoria;

	/** persistent field */
	private DomainValue tpSituacaoCrt;

	/** nullable persistent field */
	private YearMonthDay dtCarregamento;

	/** nullable persistent field */
	private DomainValue tpDevedorCrt;

	/** nullable persistent field */
	private BigDecimal pcAforo;

	/** nullable persistent field */
	private BigDecimal pcFreteExportador;

	/** nullable persistent field */
	private String dsMotivoCancelamento;

	/** nullable persistent field */
	private DomainValue tpEntregarEm;

	/** nullable persistent field */
	private String dsParceiroEntrega;

	/** nullable persistent field */
	private String dsAduanas;

	/** nullable persistent field */
	private String dsAnexos;

	/** nullable persistent field */
	private DomainValue tpSituacaoPendenciaReemissao;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Embalagem embalagem;

	/** persistent field */
	private PontoParada pontoParadaAduanaOrigem;

	/** persistent field */
	private PontoParada pontoParadaAduanaDestino;

	/** persistent field */
	private Produto produto;

	/** persistent field */
	private Empresa empresaEntregadora;

	/** persistent field */
	private PermissoEmpresaPais permissaoEmpresaPais;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Pendencia pendenciaReemissao;

	/** persistent field */
	private Moeda moedaValorMercadoria;

	/** persistent field */
	private Moeda moedaValorTotalMercadoria;

	/** persistent field */
	private MotivoCancelamento motivoCancelamento;

	/** persistent field */
	private List<AduanaCtoInt> aduanasCtoInternacional;

	/** persistent field */
	private List<DespachanteCtoInt> despachantesCtoInternacional;

	/** persistent field */
	private List<Dimensao> dimensoes;

	/** persistent field */
	private List<TrechoCtoInt> trechosCtoInternacional;

	/** persistent field */
	private List<DocumentoAnexo> documentosAnexos;
	
	public Long getNrCrt() {
		return this.nrCrt;
	}

	public void setNrCrt(Long nrCrt) {
		this.nrCrt = nrCrt;
	}

	public Integer getNrPermisso() {
		return nrPermisso;
	}

	public void setNrPermisso(Integer nrPermisso) {
		this.nrPermisso = nrPermisso;
	}

	public BigDecimal getPsLiquido() {
		return this.psLiquido;
	}

	public void setPsLiquido(BigDecimal psLiquido) {
		this.psLiquido = psLiquido;
	}

	public BigDecimal getVlVolume() {
		return this.vlVolume;
	}

	public void setVlVolume(BigDecimal vlVolume) {
		this.vlVolume = vlVolume;
	}

	public BigDecimal getVlTotalMercadoria() {
		return this.vlTotalMercadoria;
	}

	public void setVlTotalMercadoria(BigDecimal vlTotalMercadoria) {
		this.vlTotalMercadoria = vlTotalMercadoria;
	}

	public BigDecimal getVlFreteExterno() {
		return this.vlFreteExterno;
	}

	public void setVlFreteExterno(BigDecimal vlFreteExterno) {
		this.vlFreteExterno = vlFreteExterno;
	}

	public String getSgPais() {
		return this.sgPais;
	}

	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
	}

	public String getDsDadosRemetente() {
		return this.dsDadosRemetente;
	}

	public void setDsDadosRemetente(String dsDadosRemetente) {
		this.dsDadosRemetente = dsDadosRemetente;
	}

	public String getDsNomeRemetente() {
		return this.dsNomeRemetente;
	}

	public void setDsNomeRemetente(String dsNomeRemetente) {
		this.dsNomeRemetente = dsNomeRemetente;
	}

	public String getDsDadosDestinatario() {
		return this.dsDadosDestinatario;
	}

	public void setDsDadosDestinatario(String dsDadosDestinatario) {
		this.dsDadosDestinatario = dsDadosDestinatario;
	}

	public String getDsDadosConsignatario() {
		return this.dsDadosConsignatario;
	}

	public void setDsDadosConsignatario(String dsDadosConsignatario) {
		this.dsDadosConsignatario = dsDadosConsignatario;
	}

	public String getDsNotificar() {
		return this.dsNotificar;
	}

	public void setDsNotificar(String dsNotificar) {
		this.dsNotificar = dsNotificar;
	}

	public String getDsLocalEmissao() {
		return this.dsLocalEmissao;
	}

	public void setDsLocalEmissao(String dsLocalEmissao) {
		this.dsLocalEmissao = dsLocalEmissao;
	}

	public String getDsLocalCarregamento() {
		return this.dsLocalCarregamento;
	}

	public void setDsLocalCarregamento(String dsLocalCarregamento) {
		this.dsLocalCarregamento = dsLocalCarregamento;
	}

	public String getDsLocalEntrega() {
		return this.dsLocalEntrega;
	}

	public void setDsLocalEntrega(String dsLocalEntrega) {
		this.dsLocalEntrega = dsLocalEntrega;
	}

	public String getDsTransportesSucessivos() {
		return dsTransportesSucessivos;
	}

	public void setDsTransportesSucessivos(String dsTransportesSucessivos) {
		this.dsTransportesSucessivos = dsTransportesSucessivos;
	}

	public String getDsDadosMercadoria() {
		return this.dsDadosMercadoria;
	}

	public void setDsDadosMercadoria(String dsDadosMercadoria) {
		this.dsDadosMercadoria = dsDadosMercadoria;
	}

	public String getDsValorMercadoria() {
		return dsValorMercadoria;
	}

	public void setDsValorMercadoria(String dsValorMercadoria) {
		this.dsValorMercadoria = dsValorMercadoria;
	}

	public DomainValue getTpSituacaoCrt() {
		return this.tpSituacaoCrt;
	}

	public void setTpSituacaoCrt(DomainValue tpSituacaoCrt) {
		this.tpSituacaoCrt = tpSituacaoCrt;
	}

	public YearMonthDay getDtCarregamento() {
		return dtCarregamento;
	}

	public void setDtCarregamento(YearMonthDay dtCarregamento) {
		this.dtCarregamento = dtCarregamento;
	}

	public DomainValue getTpDevedorCrt() {
		return this.tpDevedorCrt;
	}

	public void setTpDevedorCrt(DomainValue tpDevedorCrt) {
		this.tpDevedorCrt = tpDevedorCrt;
	}

	public BigDecimal getPcAforo() {
		return this.pcAforo;
	}

	public void setPcAforo(BigDecimal pcAforo) {
		this.pcAforo = pcAforo;
	}

	public BigDecimal getPcFreteExportador() {
		return this.pcFreteExportador;
	}

	public void setPcFreteExportador(BigDecimal pcFreteExportador) {
		this.pcFreteExportador = pcFreteExportador;
	}

	public String getDsMotivoCancelamento() {
		return this.dsMotivoCancelamento;
	}

	public void setDsMotivoCancelamento(String dsMotivoCancelamento) {
		this.dsMotivoCancelamento = dsMotivoCancelamento;
	}

	public DomainValue getTpEntregarEm() {
		return this.tpEntregarEm;
	}

	public void setTpEntregarEm(DomainValue tpEntregarEm) {
		this.tpEntregarEm = tpEntregarEm;
	}

	public String getDsParceiroEntrega() {
		return this.dsParceiroEntrega;
	}

	public void setDsParceiroEntrega(String dsParceiroEntrega) {
		this.dsParceiroEntrega = dsParceiroEntrega;
	}

	public String getDsAduanas() {
		return this.dsAduanas;
	}

	public void setDsAduanas(String dsAduanas) {
		this.dsAduanas = dsAduanas;
	}

	public String getDsAnexos() {
		return this.dsAnexos;
	}

	public void setDsAnexos(String dsAnexos) {
		this.dsAnexos = dsAnexos;
	}

	public DomainValue getTpSituacaoPendenciaReemissao() {
		return tpSituacaoPendenciaReemissao;
	}

	public void setTpSituacaoPendenciaReemissao(
			DomainValue tpSituacaoPendenciaReemissao) {
		this.tpSituacaoPendenciaReemissao = tpSituacaoPendenciaReemissao;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Embalagem getEmbalagem() {
		return this.embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public PontoParada getPontoParadaAduanaDestino() {
		return pontoParadaAduanaDestino;
	}

	public void setPontoParadaAduanaDestino(PontoParada pontoParadaAduanaDestino) {
		this.pontoParadaAduanaDestino = pontoParadaAduanaDestino;
	}

	public PontoParada getPontoParadaAduanaOrigem() {
		return pontoParadaAduanaOrigem;
	}

	public void setPontoParadaAduanaOrigem(PontoParada pontoParadaAduanaOrigem) {
		this.pontoParadaAduanaOrigem = pontoParadaAduanaOrigem;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Empresa getEmpresaEntregadora() {
		return this.empresaEntregadora;
	}

	public void setEmpresaEntregadora(Empresa empresaEntregadora) {
		this.empresaEntregadora = empresaEntregadora;
	}

	public PermissoEmpresaPais getPermissaoEmpresaPais() {
		return permissaoEmpresaPais;
	}

	public void setPermissaoEmpresaPais(PermissoEmpresaPais permissaoEmpresaPais) {
		this.permissaoEmpresaPais = permissaoEmpresaPais;
	}

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Pendencia getPendenciaReemissao() {
		return this.pendenciaReemissao;
	}

	public void setPendenciaReemissao(Pendencia pendenciaReemissao) {
		this.pendenciaReemissao = pendenciaReemissao;
	}

	public Moeda getMoedaValorMercadoria() {
		return this.moedaValorMercadoria;
	}

	public void setMoedaValorMercadoria(Moeda moedaValorMercadoria) {
		this.moedaValorMercadoria = moedaValorMercadoria;
	}

	public Moeda getMoedaValorTotalMercadoria() {
		return this.moedaValorTotalMercadoria;
	}

	public void setMoedaValorTotalMercadoria(Moeda moedaValorTotalMercadoria) {
		this.moedaValorTotalMercadoria = moedaValorTotalMercadoria;
	}

	public MotivoCancelamento getMotivoCancelamento() {
		return this.motivoCancelamento;
	}

	public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.AduanaCtoInt.class) 
	public List<AduanaCtoInt> getAduanasCtoInternacional() {
		return aduanasCtoInternacional;
	}

	public void setAduanasCtoInternacional(
			List<AduanaCtoInt> aduanasCtoInternacional) {
		this.aduanasCtoInternacional = aduanasCtoInternacional;
	}

	public void addAduanaCtoInternacional(AduanaCtoInt aduanaCtoInternacional){
		if(aduanasCtoInternacional == null) {
			aduanasCtoInternacional = new ArrayList<AduanaCtoInt>();
		}
		aduanasCtoInternacional.add(aduanaCtoInternacional);
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DespachanteCtoInt.class) 
	public List<DespachanteCtoInt> getDespachantesCtoInternacional() {
		return despachantesCtoInternacional;
	}

	public void setDespachantesCtoInternacional(
			List<DespachanteCtoInt> despachantesCtoInternacional) {
		this.despachantesCtoInternacional = despachantesCtoInternacional;
	}

	public void addDespachantesCtoInternacional(
			DespachanteCtoInt despachanteCtoInternacional) {
		if(despachantesCtoInternacional == null) {
			despachantesCtoInternacional = new ArrayList<DespachanteCtoInt>();
		}
		despachantesCtoInternacional.add(despachanteCtoInternacional);
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Dimensao.class) 
	public List<Dimensao> getDimensoes() {
		return this.dimensoes;
	}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.TrechoCtoInt.class) 
	public List<TrechoCtoInt> getTrechosCtoInternacional() {
		return trechosCtoInternacional;
	}

	public void setTrechosCtoInternacional(
			List<TrechoCtoInt> trechosCtoInternacional) {
		this.trechosCtoInternacional = trechosCtoInternacional;
	}

	public void addTrechoCtoInternacional(TrechoCtoInt trechoCtoInternacional) {
		if(trechosCtoInternacional == null) {
			trechosCtoInternacional = new ArrayList<TrechoCtoInt>();
		}
		trechosCtoInternacional.add(trechoCtoInternacional);
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DocumentoAnexo.class) 
	public List<DocumentoAnexo> getDocumentosAnexos() {
		return documentosAnexos;
	}

	public void setDocumentosAnexos(List<DocumentoAnexo> documentosAnexos) {
		this.documentosAnexos = documentosAnexos;
	}

	public void addDocumentoAnexo(DocumentoAnexo documentoAnexo) {
		if(documentosAnexos == null) {
			documentosAnexos = new ArrayList<DocumentoAnexo>();
		}
		documentosAnexos.add(documentoAnexo);
	}

}