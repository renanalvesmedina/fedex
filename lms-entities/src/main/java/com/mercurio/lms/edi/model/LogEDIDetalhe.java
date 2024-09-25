package com.mercurio.lms.edi.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

@Entity
@Table(name = "LOG_ARQUIVO_EDI_DETALHE")
@Proxy(lazy=false)  
public class LogEDIDetalhe implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long idLogEdiDetalhe;
	private LogEDI logEDI;
	private String nomeReme;
	private Long cnpjReme;
	private String ieReme;
	private String enderecoReme;
	private String bairroReme;
	private String municipioReme;
	private String ufReme;
	private Integer cepEnderReme;
	private Integer cepMuniReme;
	private String nomeDest;
	private Long cnpjDest;
	private String ieDest;
	private String enderecoDest;
	private String bairroDest;
	private String municipioDest;
	private String ufDest;
	private Integer cepEnderDest;
	private Integer cepMunicDest;
	private String nomeConsig;
	private Long cnpjConsig;
	private String ieConsig;
	private String enderecoConsig;
	private String bairroConsig;
	private String municipioConsig;
	private String ufConsig;
	private Integer cepEnderConsig;
	private Integer cepMunicConsig;
	private String nomeRedesp;
	private Long cnpjRedesp;
	private String ieRedesp;
	private String enderecoRedesp;
	private String bairroRedesp;
	private String municipioRedesp;
	private String ufRedesp;
	private String cepEnderRedesp;
	private String cepMunicRedesp;
	private String nomeTomador;
	private Long cnpjTomador;
	private String ieTomador;
	private String enderecoTomador;
	private String bairroTomador;
	private String municipioTomador;
	private String ufTomador;
	private Integer cepEnderTomador;
	private Integer cepMunicTomador;
	private String natureza;
	private String especie;
	private String tipoFrete;
	private String modalFrete;
	private String tipoTabela;
	private Short tarifa;
	private String serieNf;
	private Integer nrNotaFiscal;
	private YearMonthDay dataEmissaoNf;
	private Date dataLog;
	private BigDecimal qtdeVolumes;
	private BigDecimal vlrTotalMerc;
	private BigDecimal pesoReal;
	private BigDecimal pesoCubado;
	private String chaveNfe;
	private BigDecimal vlrIcmsNf;
	private BigDecimal vlrIcmsStNf;
	private BigDecimal aliqNf;
	private BigDecimal vlrBaseCalcNf;
	private BigDecimal vlrBaseCalcStNf;
	private BigDecimal vlrTotProdutosNf;
	private Short cfopNf;
	private Integer pinSuframa;
	private Long sequenciaAgrupamento;
	private BigDecimal vlrFretePeso;
	private BigDecimal vlrFreteValor;
	private BigDecimal vlrCat;
	private BigDecimal vlrDespacho;
	private BigDecimal vlrItr;
	private BigDecimal vlrAdeme;
	private BigDecimal vlrPedagio;
	private BigDecimal vlrTaxas;
	private BigDecimal outrosValores;
	private BigDecimal vlrIcms;
	private BigDecimal vlrBaseCalcIcms;
	private BigDecimal aliqIcms;
	private BigDecimal vlrFreteLiquido;
	private BigDecimal vlrFreteTotal;
	private BigDecimal pesoRealTotal;
	private BigDecimal pesoCubadoTotal;
	private BigDecimal vlrTotalMercTotal;
	private String dsDivisaoCliente;
	private String nrCtrcSubcontratante;
	private String status;
	private String observacao;
	private List<LogEDIItem> logItens;
	private List<LogEDIComplemento> logComplementos;
	private List<LogEDIVolume> logVolumes;
	
	@OneToMany(mappedBy="logEDIDetalhe")
	public List<LogEDIItem> getLogItens() {
		return logItens;
	}
	
	public void setLogItens(List<LogEDIItem> logItens) {
		this.logItens = logItens;
	}

	@OneToMany(mappedBy="logEDIDetalhe")
	public List<LogEDIComplemento> getLogComplementos() {
		return logComplementos;
	}

	public void setLogComplementos(List<LogEDIComplemento> logComplementos) {
		this.logComplementos = logComplementos;
	}

	@OneToMany(mappedBy="logEDIDetalhe")
	public List<LogEDIVolume> getLogVolumes() {
		return logVolumes;
	}

	public void setLogVolumes(List<LogEDIVolume> logVolumes) {
		this.logVolumes = logVolumes;
	}

	@Id
	@Column(name = "ID_LOG_ARQUIVO_EDI_DETALHE", nullable = false)
	public Long getIdLogEdiDetalhe() {
		return idLogEdiDetalhe;
	}

	public void setIdLogEdiDetalhe(Long idLogEdiDetalhe) {
		this.idLogEdiDetalhe = idLogEdiDetalhe;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LOAE_ID_LOG_ARQUIVO_EDI", nullable = false)
	public LogEDI getLogEDI() {
		return logEDI;
	}

	public void setLogEDI(LogEDI logEDI) {
		this.logEDI = logEDI;
	}

	@Column(name = "NOME_REME", length = 40)
	public String getNomeReme() {
		return this.nomeReme;
	}

	public void setNomeReme(String nomeReme) {
		this.nomeReme = nomeReme;
	}

	@Column(name = "CNPJ_REME", length = 14)
	public Long getCnpjReme() {
		return this.cnpjReme;
	}

	public void setCnpjReme(Long cnpjReme) {
		this.cnpjReme = cnpjReme;
	}

	@Column(name = "IE_REME", length = 20)
	public String getIeReme() {
		return this.ieReme;
	}

	public void setIeReme(String ieReme) {
		this.ieReme = ieReme;
	}

	@Column(name = "ENDERECO_REME", length = 165)
	public String getEnderecoReme() {
		return this.enderecoReme;
	}

	public void setEnderecoReme(String enderecoReme) {
		this.enderecoReme = enderecoReme;
	}

	@Column(name = "BAIRRO_REME", length = 60)
	public String getBairroReme() {
		return this.bairroReme;
	}

	public void setBairroReme(String bairroReme) {
		this.bairroReme = bairroReme;
	}

	@Column(name = "MUNICIPIO_REME", length = 60)
	public String getMunicipioReme() {
		return this.municipioReme;
	}

	public void setMunicipioReme(String municipioReme) {
		this.municipioReme = municipioReme;
	}

	@Column(name = "UF_REME", length = 2)
	public String getUfReme() {
		return this.ufReme;
	}

	public void setUfReme(String ufReme) {
		this.ufReme = ufReme;
	}

	@Column(name = "CEP_ENDER_REME", length = 8)
	public Integer getCepEnderReme() {
		return this.cepEnderReme;
	}

	public void setCepEnderReme(Integer cepEnderReme) {
		this.cepEnderReme = cepEnderReme;
	}

	@Column(name = "CEP_MUNI_REME", length = 5)
	public Integer getCepMuniReme() {
		return this.cepMuniReme;
	}

	public void setCepMuniReme(Integer cepMuniReme) {
		this.cepMuniReme = cepMuniReme;
	}

	@Column(name = "NOME_DEST", length = 40)
	public String getNomeDest() {
		return this.nomeDest;
	}

	public void setNomeDest(String nomeDest) {
		this.nomeDest = nomeDest;
	}

	@Column(name = "CNPJ_DEST", length = 14)
	public Long getCnpjDest() {
		return this.cnpjDest;
	}

	public void setCnpjDest(Long cnpjDest) {
		this.cnpjDest = cnpjDest;
	}

	@Column(name = "IE_DEST", length = 20)
	public String getIeDest() {
		return this.ieDest;
	}

	public void setIeDest(String ieDest) {
		this.ieDest = ieDest;
	}

	@Column(name = "ENDERECO_DEST", length = 165)
	public String getEnderecoDest() {
		return this.enderecoDest;
	}

	public void setEnderecoDest(String enderecoDest) {
		this.enderecoDest = enderecoDest;
	}

	@Column(name = "BAIRRO_DEST", length = 60)
	public String getBairroDest() {
		return this.bairroDest;
	}

	public void setBairroDest(String bairroDest) {
		this.bairroDest = bairroDest;
	}

	@Column(name = "MUNICIPIO_DEST", length = 60)
	public String getMunicipioDest() {
		return this.municipioDest;
	}

	public void setMunicipioDest(String municipioDest) {
		this.municipioDest = municipioDest;
	}

	@Column(name = "UF_DEST", length = 2)
	public String getUfDest() {
		return this.ufDest;
	}

	public void setUfDest(String ufDest) {
		this.ufDest = ufDest;
	}

	@Column(name = "CEP_ENDER_DEST", length = 8)
	public Integer getCepEnderDest() {
		return this.cepEnderDest;
	}

	public void setCepEnderDest(Integer cepEnderDest) {
		this.cepEnderDest = cepEnderDest;
	}

	@Column(name = "CEP_MUNIC_DEST", length = 5)
	public Integer getCepMunicDest() {
		return this.cepMunicDest;
	}

	public void setCepMunicDest(Integer cepMunicDest) {
		this.cepMunicDest = cepMunicDest;
	}

	@Column(name = "NOME_CONSIG", length = 40)
	public String getNomeConsig() {
		return this.nomeConsig;
	}

	public void setNomeConsig(String nomeConsig) {
		this.nomeConsig = nomeConsig;
	}

	@Column(name = "CNPJ_CONSIG", length = 14)
	public Long getCnpjConsig() {
		return this.cnpjConsig;
	}

	public void setCnpjConsig(Long cnpjConsig) {
		this.cnpjConsig = cnpjConsig;
	}

	@Column(name = "IE_CONSIG", length = 20)
	public String getIeConsig() {
		return this.ieConsig;
	}

	public void setIeConsig(String ieConsig) {
		this.ieConsig = ieConsig;
	}

	@Column(name = "ENDERECO_CONSIG", length = 165)
	public String getEnderecoConsig() {
		return this.enderecoConsig;
	}

	public void setEnderecoConsig(String enderecoConsig) {
		this.enderecoConsig = enderecoConsig;
	}

	@Column(name = "BAIRRO_CONSIG", length = 60)
	public String getBairroConsig() {
		return this.bairroConsig;
	}

	public void setBairroConsig(String bairroConsig) {
		this.bairroConsig = bairroConsig;
	}

	@Column(name = "MUNICIPIO_CONSIG", length = 60)
	public String getMunicipioConsig() {
		return this.municipioConsig;
	}

	public void setMunicipioConsig(String municipioConsig) {
		this.municipioConsig = municipioConsig;
	}

	@Column(name = "UF_CONSIG", length = 2)
	public String getUfConsig() {
		return this.ufConsig;
	}

	public void setUfConsig(String ufConsig) {
		this.ufConsig = ufConsig;
	}

	@Column(name = "CEP_ENDER_CONSIG", length = 8)
	public Integer getCepEnderConsig() {
		return this.cepEnderConsig;
	}

	public void setCepEnderConsig(Integer cepEnderConsig) {
		this.cepEnderConsig = cepEnderConsig;
	}

	@Column(name = "CEP_MUNIC_CONSIG", length = 5)
	public Integer getCepMunicConsig() {
		return this.cepMunicConsig;
	}

	public void setCepMunicConsig(Integer cepMunicConsig) {
		this.cepMunicConsig = cepMunicConsig;
	}

	@Column(name = "NOME_REDESP", length = 40)
	public String getNomeRedesp() {
		return this.nomeRedesp;
	}

	public void setNomeRedesp(String nomeRedesp) {
		this.nomeRedesp = nomeRedesp;
	}

	@Column(name = "CNPJ_REDESP", length = 14)
	public Long getCnpjRedesp() {
		return this.cnpjRedesp;
	}

	public void setCnpjRedesp(Long cnpjRedesp) {
		this.cnpjRedesp = cnpjRedesp;
	}

	@Column(name = "IE_REDESP", length = 20)
	public String getIeRedesp() {
		return this.ieRedesp;
	}

	public void setIeRedesp(String ieRedesp) {
		this.ieRedesp = ieRedesp;
	}

	@Column(name = "ENDERECO_REDESP", length = 165)
	public String getEnderecoRedesp() {
		return this.enderecoRedesp;
	}

	public void setEnderecoRedesp(String enderecoRedesp) {
		this.enderecoRedesp = enderecoRedesp;
	}

	@Column(name = "BAIRRO_REDESP", length = 60)
	public String getBairroRedesp() {
		return this.bairroRedesp;
	}

	public void setBairroRedesp(String bairroRedesp) {
		this.bairroRedesp = bairroRedesp;
	}

	@Column(name = "MUNICIPIO_REDESP", length = 60)
	public String getMunicipioRedesp() {
		return this.municipioRedesp;
	}

	public void setMunicipioRedesp(String municipioRedesp) {
		this.municipioRedesp = municipioRedesp;
	}

	@Column(name = "UF_REDESP", length = 2)
	public String getUfRedesp() {
		return this.ufRedesp;
	}

	public void setUfRedesp(String ufRedesp) {
		this.ufRedesp = ufRedesp;
	}

	@Column(name = "CEP_ENDER_REDESP", length = 8)
	public String getCepEnderRedesp() {
		return this.cepEnderRedesp;
	}

	public void setCepEnderRedesp(String cepEnderRedesp) {
		this.cepEnderRedesp = cepEnderRedesp;
	}

	@Column(name = "CEP_MUNIC_REDESP", length = 5)
	public String getCepMunicRedesp() {
		return this.cepMunicRedesp;
	}

	public void setCepMunicRedesp(String cepMunicRedesp) {
		this.cepMunicRedesp = cepMunicRedesp;
	}

	@Column(name = "NOME_TOMADOR", length = 40)
	public String getNomeTomador() {
		return this.nomeTomador;
	}

	public void setNomeTomador(String nomeTomador) {
		this.nomeTomador = nomeTomador;
	}

	@Column(name = "CNPJ_TOMADOR", length = 14)
	public Long getCnpjTomador() {
		return this.cnpjTomador;
	}

	public void setCnpjTomador(Long cnpjTomador) {
		this.cnpjTomador = cnpjTomador;
	}

	@Column(name = "IE_TOMADOR", length = 20)
	public String getIeTomador() {
		return this.ieTomador;
	}

	public void setIeTomador(String ieTomador) {
		this.ieTomador = ieTomador;
	}

	@Column(name = "ENDERECO_TOMADOR", length = 165)
	public String getEnderecoTomador() {
		return this.enderecoTomador;
	}

	public void setEnderecoTomador(String enderecoTomador) {
		this.enderecoTomador = enderecoTomador;
	}

	@Column(name = "BAIRRO_TOMADOR", length = 60)
	public String getBairroTomador() {
		return this.bairroTomador;
	}

	public void setBairroTomador(String bairroTomador) {
		this.bairroTomador = bairroTomador;
	}

	@Column(name = "MUNICIPIO_TOMADOR", length = 60)
	public String getMunicipioTomador() {
		return this.municipioTomador;
	}

	public void setMunicipioTomador(String municipioTomador) {
		this.municipioTomador = municipioTomador;
	}

	@Column(name = "UF_TOMADOR", length = 2)
	public String getUfTomador() {
		return this.ufTomador;
	}

	public void setUfTomador(String ufTomador) {
		this.ufTomador = ufTomador;
	}

	@Column(name = "CEP_ENDER_TOMADOR", length = 8)
	public Integer getCepEnderTomador() {
		return this.cepEnderTomador;
	}

	public void setCepEnderTomador(Integer cepEnderTomador) {
		this.cepEnderTomador = cepEnderTomador;
	}

	@Column(name = "CEP_MUNIC_TOMADOR", length = 5)
	public Integer getCepMunicTomador() {
		return this.cepMunicTomador;
	}

	public void setCepMunicTomador(Integer cepMunicTomador) {
		this.cepMunicTomador = cepMunicTomador;
	}

	@Column(name = "NATUREZA", length = 15)
	public String getNatureza() {
		return this.natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	@Column(name = "ESPECIE", length = 15)
	public String getEspecie() {
		return this.especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	@Column(name = "TIPO_FRETE", length = 3)
	public String getTipoFrete() {
		return this.tipoFrete;
	}

	public void setTipoFrete(String tipoFrete) {
		this.tipoFrete = tipoFrete;
	}

	@Column(name = "MODAL_FRETE", length = 1)
	public String getModalFrete() {
		return this.modalFrete;
	}

	public void setModalFrete(String modalFrete) {
		this.modalFrete = modalFrete;
	}

	@Column(name = "TIPO_TABELA", length = 1)
	public String getTipoTabela() {
		return this.tipoTabela;
	}

	public void setTipoTabela(String tipoTabela) {
		this.tipoTabela = tipoTabela;
	}

	@Column(name = "TARIFA", length = 4)
	public Short getTarifa() {
		return this.tarifa;
	}

	public void setTarifa(Short tarifa) {
		this.tarifa = tarifa;
	}

	@Column(name = "SERIE_NF", length = 3)
	public String getSerieNf() {
		return this.serieNf;
	}

	public void setSerieNf(String serieNf) {
		this.serieNf = serieNf;
	}

	@Column(name = "NR_NOTA_FISCAL", length = 9)
	public Integer getNrNotaFiscal() {
		return this.nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	@Column(name = "DT_EMISSAO_NF", length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDataEmissaoNf() {
		return this.dataEmissaoNf;
	}

	public void setDataEmissaoNf(YearMonthDay dataEmissaoNf) {
		this.dataEmissaoNf = dataEmissaoNf;
	}

	@Column(name = "QTDE_VOLUMES", length = 5)
	public BigDecimal getQtdeVolumes() {
		return this.qtdeVolumes;
	}

	public void setQtdeVolumes(BigDecimal qtdeVolumes) {
		this.qtdeVolumes = qtdeVolumes;
	}

	@Column(name = "VLR_TOTAL_MERC", length = 13)
	public BigDecimal getVlrTotalMerc() {
		return this.vlrTotalMerc;
	}

	public void setVlrTotalMerc(BigDecimal vlrTotalMerc) {
		this.vlrTotalMerc = vlrTotalMerc;
	}

	@Column(name = "PESO_REAL", length = 5)
	public BigDecimal getPesoReal() {
		return this.pesoReal;
	}

	public void setPesoReal(BigDecimal pesoReal) {
		this.pesoReal = pesoReal;
	}

	@Column(name = "PESO_CUBADO", length = 5)
	public BigDecimal getPesoCubado() {
		return this.pesoCubado;
	}

	public void setPesoCubado(BigDecimal pesoCubado) {
		this.pesoCubado = pesoCubado;
	}

	@Column(name = "CHAVE_NFE", length = 44)
	public String getChaveNfe() {
		return this.chaveNfe;
	}

	public void setChaveNfe(String chaveNfe) {
		this.chaveNfe = chaveNfe;
	}

	@Column(name = "VLR_ICMS_NF", length = 13)
	public BigDecimal getVlrIcmsNf() {
		return this.vlrIcmsNf;
	}

	public void setVlrIcmsNf(BigDecimal vlrIcmsNf) {
		this.vlrIcmsNf = vlrIcmsNf;
	}

	@Column(name = "VLR_ICMS_ST_NF", length = 13)
	public BigDecimal getVlrIcmsStNf() {
		return this.vlrIcmsStNf;
	}

	public void setVlrIcmsStNf(BigDecimal vlrIcmsStNf) {
		this.vlrIcmsStNf = vlrIcmsStNf;
	}

	@Column(name = "ALIQ_NF", length = 13)
	public BigDecimal getAliqNf() {
		return this.aliqNf;
	}

	public void setAliqNf(BigDecimal aliqNf) {
		this.aliqNf = aliqNf;
	}

	@Column(name = "VLR_BASE_CALC_NF", length = 13)
	public BigDecimal getVlrBaseCalcNf() {
		return this.vlrBaseCalcNf;
	}

	public void setVlrBaseCalcNf(BigDecimal vlrBaseCalcNf) {
		this.vlrBaseCalcNf = vlrBaseCalcNf;
	}

	@Column(name = "VLR_BASE_CALC_ST_NF", length = 13)
	public BigDecimal getVlrBaseCalcStNf() {
		return this.vlrBaseCalcStNf;
	}

	public void setVlrBaseCalcStNf(BigDecimal vlrBaseCalcStNf) {
		this.vlrBaseCalcStNf = vlrBaseCalcStNf;
	}

	@Column(name = "VLR_TOT_PRODUTOS_NF", length = 13)
	public BigDecimal getVlrTotProdutosNf() {
		return this.vlrTotProdutosNf;
	}

	public void setVlrTotProdutosNf(BigDecimal vlrTotProdutosNf) {
		this.vlrTotProdutosNf = vlrTotProdutosNf;
	}

	@Column(name = "CFOP_NF", length = 4)
	public Short getCfopNf() {
		return this.cfopNf;
	}

	public void setCfopNf(Short cfopNf) {
		this.cfopNf = cfopNf;
	}

	@Column(name = "PIN_SUFRAMA", length = 9)
	public Integer getPinSuframa() {
		return this.pinSuframa;
	}

	public void setPinSuframa(Integer pinSuframa) {
		this.pinSuframa = pinSuframa;
	}

	@Column(name = "VLR_FRETE_PESO", length = 13)
	public BigDecimal getVlrFretePeso() {
		return this.vlrFretePeso;
	}

	public void setVlrFretePeso(BigDecimal vlrFretePeso) {
		this.vlrFretePeso = vlrFretePeso;
	}

	@Column(name = "VLR_FRETE_VALOR", length = 13)
	public BigDecimal getVlrFreteValor() {
		return this.vlrFreteValor;
	}

	public void setVlrFreteValor(BigDecimal vlrFreteValor) {
		this.vlrFreteValor = vlrFreteValor;
	}

	@Column(name = "VLR_CAT", length = 13)
	public BigDecimal getVlrCat() {
		return this.vlrCat;
	}

	public void setVlrCat(BigDecimal vlrCat) {
		this.vlrCat = vlrCat;
	}

	@Column(name = "VLR_DESPACHO", length = 13)
	public BigDecimal getVlrDespacho() {
		return this.vlrDespacho;
	}

	public void setVlrDespacho(BigDecimal vlrDespacho) {
		this.vlrDespacho = vlrDespacho;
	}

	@Column(name = "VLR_ITR", length = 13)
	public BigDecimal getVlrItr() {
		return this.vlrItr;
	}

	public void setVlrItr(BigDecimal vlrItr) {
		this.vlrItr = vlrItr;
	}

	@Column(name = "VLR_ADEME", length = 13)
	public BigDecimal getVlrAdeme() {
		return this.vlrAdeme;
	}

	public void setVlrAdeme(BigDecimal vlrAdeme) {
		this.vlrAdeme = vlrAdeme;
	}

	@Column(name = "VLR_PEDAGIO", length = 13)
	public BigDecimal getVlrPedagio() {
		return this.vlrPedagio;
	}

	public void setVlrPedagio(BigDecimal vlrPedagio) {
		this.vlrPedagio = vlrPedagio;
	}

	@Column(name = "VLR_TAXAS", length = 13)
	public BigDecimal getVlrTaxas() {
		return this.vlrTaxas;
	}

	public void setVlrTaxas(BigDecimal vlrTaxas) {
		this.vlrTaxas = vlrTaxas;
	}

	@Column(name = "OUTROS_VALORES", length = 13)
	public BigDecimal getOutrosValores() {
		return this.outrosValores;
	}

	public void setOutrosValores(BigDecimal outrosValores) {
		this.outrosValores = outrosValores;
	}

	@Column(name = "VLR_ICMS", length = 13)
	public BigDecimal getVlrIcms() {
		return this.vlrIcms;
	}

	public void setVlrIcms(BigDecimal vlrIcms) {
		this.vlrIcms = vlrIcms;
	}

	@Column(name = "VLR_BASE_CALC", length = 13)
	public BigDecimal getVlrBaseCalcIcms() {
		return this.vlrBaseCalcIcms;
	}

	public void setVlrBaseCalcIcms(BigDecimal vlrBaseCalcIcms) {
		this.vlrBaseCalcIcms = vlrBaseCalcIcms;
	}

	@Column(name = "ALIQ_ICMS", length = 3)
	public BigDecimal getAliqIcms() {
		return this.aliqIcms;
	}

	public void setAliqIcms(BigDecimal aliqIcms) {
		this.aliqIcms = aliqIcms;
	}

	@Column(name = "VLR_FRETE_LIQUIDO", length = 13)
	public BigDecimal getVlrFreteLiquido() {
		return this.vlrFreteLiquido;
	}

	public void setVlrFreteLiquido(BigDecimal vlrFreteLiquido) {
		this.vlrFreteLiquido = vlrFreteLiquido;
	}

	@Column(name = "VLR_FRETE_TOTAL", length = 13)
	public BigDecimal getVlrFreteTotal() {
		return this.vlrFreteTotal;
	}

	public void setVlrFreteTotal(BigDecimal vlrFreteTotal) {
		this.vlrFreteTotal = vlrFreteTotal;
	}

	@Column(name = "PESO_REAL_TOTAL", length = 5)
	public BigDecimal getPesoRealTotal() {
		return this.pesoRealTotal;
	}

	public void setPesoRealTotal(BigDecimal pesoRealTotal) {
		this.pesoRealTotal = pesoRealTotal;
	}

	@Column(name = "PESO_CUBADO_TOTAL", length = 5)
	public BigDecimal getPesoCubadoTotal() {
		return this.pesoCubadoTotal;
	}

	public void setPesoCubadoTotal(BigDecimal pesoCubadoTotal) {
		this.pesoCubadoTotal = pesoCubadoTotal;
	}

	@Column(name = "VLR_TOTAL_MERC_TOTAL", length = 13)
	public BigDecimal getVlrTotalMercTotal() {
		return this.vlrTotalMercTotal;
	}

	public void setVlrTotalMercTotal(BigDecimal vlrTotalMercTotal) {
		this.vlrTotalMercTotal = vlrTotalMercTotal;
	}

	@Column(name = "STATUS", length = 30)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "OBSERVACAO", length = 100)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DH_LOG", length = 7)
	public Date getDataLog() {
		return this.dataLog;
	}

	public void setDataLog(Date dataLog) {
		this.dataLog = dataLog;
	}

	@Column(name = "SEQUENCIA_AGRUPAMENTO", length = 10)
	public Long getSequenciaAgrupamento() {
		return this.sequenciaAgrupamento;
	}

	public void setSequenciaAgrupamento(Long sequenciaAgrupamento) {
		this.sequenciaAgrupamento = sequenciaAgrupamento;
	}
	
	@Column(name = "DS_DIVISAO_CLIENTE", length = 60)
	public String getDsDivisaoCliente() {
		return dsDivisaoCliente;
	}
	
	public void setDsDivisaoCliente(String dsDivisaoCliente) {
		this.dsDivisaoCliente = dsDivisaoCliente;
	}

	@Column(name = "NR_CTRC_SUBCONTRATANTE", length = 44)
	public String getNrCtrcSubcontratante() {
		return nrCtrcSubcontratante;
	}
	
	public void setNrCtrcSubcontratante(String nrCtrcSubcontratante) {
		this.nrCtrcSubcontratante = nrCtrcSubcontratante;
	}
}
