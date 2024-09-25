package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotaFiscal  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8934475593984980952L;
	private String natureza;
	private String especie;
	private String tipoFrete;
	private String modalFrete;
	private String tipoTabela;
	private Short tarifa;
	private String serieNf;
	private Integer nrNotaFiscal;
	private Date dataEmissaoNf;
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
	private String erro;
	private String dsDivisaoCliente;
	private String nrCtrcSubcontratante;

	private List<Item> itens;
	private List<Volume> volumes;
	private List<Complemento> complementos;
	
	public final List<Item> getItens() {
		return itens;
	}
	public final void setItens(List<Item> itens) {
		this.itens = itens;
	}
	public final List<Volume> getVolumes() {
		return volumes;
	}
	public final void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}
	public final List<Complemento> getComplementos() {
		return complementos;
	}
	public final void setComplementos(List<Complemento> complementos) {
		this.complementos = complementos;
	}
	public final String getNatureza() {
		return natureza;
	}
	public final void setNatureza(String natureza) {
		this.natureza = natureza;
	}
	public final String getEspecie() {
		return especie;
	}
	public final void setEspecie(String especie) {
		this.especie = especie;
	}
	public final String getTipoFrete() {
		return tipoFrete;
	}
	public final void setTipoFrete(String tipoFrete) {
		this.tipoFrete = tipoFrete;
	}
	public final String getModalFrete() {
		return modalFrete;
	}
	public final void setModalFrete(String modalFrete) {
		this.modalFrete = modalFrete;
	}
	public final String getTipoTabela() {
		return tipoTabela;
	}
	public final void setTipoTabela(String tipoTabela) {
		this.tipoTabela = tipoTabela;
	}
	public final Short getTarifa() {
		return tarifa;
	}
	public final void setTarifa(Short tarifa) {
		this.tarifa = tarifa;
	}
	public final String getSerieNf() {
		return serieNf;
	}
	public final void setSerieNf(String serieNf) {
		this.serieNf = serieNf;
	}
	public final Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}
	public final void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}
	public final Date getDataEmissaoNf() {
		return dataEmissaoNf;
	}
	public final void setDataEmissaoNf(Date dataEmissaoNf) {
		this.dataEmissaoNf = dataEmissaoNf;
	}
	public final BigDecimal getQtdeVolumes() {
		return qtdeVolumes;
	}
	public final void setQtdeVolumes(BigDecimal qtdeVolumes) {
		this.qtdeVolumes = qtdeVolumes;
	}
	public final BigDecimal getVlrTotalMerc() {
		return vlrTotalMerc;
	}
	public final void setVlrTotalMerc(BigDecimal vlrTotalMerc) {
		this.vlrTotalMerc = vlrTotalMerc;
	}
	public final BigDecimal getPesoReal() {
		return pesoReal;
	}
	public final void setPesoReal(BigDecimal pesoReal) {
		this.pesoReal = pesoReal;
	}
	public final BigDecimal getPesoCubado() {
		return pesoCubado;
	}
	public final void setPesoCubado(BigDecimal pesoCubado) {
		this.pesoCubado = pesoCubado;
	}
	public final String getChaveNfe() {
		return chaveNfe;
	}
	public final void setChaveNfe(String chaveNfe) {
		this.chaveNfe = chaveNfe;
	}
	public final BigDecimal getVlrIcmsNf() {
		return vlrIcmsNf;
	}
	public final void setVlrIcmsNf(BigDecimal vlrIcmsNf) {
		this.vlrIcmsNf = vlrIcmsNf;
	}
	public final BigDecimal getVlrIcmsStNf() {
		return vlrIcmsStNf;
	}
	public final void setVlrIcmsStNf(BigDecimal vlrIcmsStNf) {
		this.vlrIcmsStNf = vlrIcmsStNf;
	}
	public final BigDecimal getAliqNf() {
		return aliqNf;
	}
	public final void setAliqNf(BigDecimal aliqNf) {
		this.aliqNf = aliqNf;
	}
	public final BigDecimal getVlrBaseCalcNf() {
		return vlrBaseCalcNf;
	}
	public final void setVlrBaseCalcNf(BigDecimal vlrBaseCalcNf) {
		this.vlrBaseCalcNf = vlrBaseCalcNf;
	}
	public final BigDecimal getVlrBaseCalcStNf() {
		return vlrBaseCalcStNf;
	}
	public final void setVlrBaseCalcStNf(BigDecimal vlrBaseCalcStNf) {
		this.vlrBaseCalcStNf = vlrBaseCalcStNf;
	}
	public final BigDecimal getVlrTotProdutosNf() {
		return vlrTotProdutosNf;
	}
	public final void setVlrTotProdutosNf(BigDecimal vlrTotProdutosNf) {
		this.vlrTotProdutosNf = vlrTotProdutosNf;
	}
	public final Short getCfopNf() {
		return cfopNf;
	}
	public final void setCfopNf(Short cfopNf) {
		this.cfopNf = cfopNf;
	}
	public final Integer getPinSuframa() {
		return pinSuframa;
	}
	public final void setPinSuframa(Integer pinSuframa) {
		this.pinSuframa = pinSuframa;
	}
	public final BigDecimal getVlrFretePeso() {
		return vlrFretePeso;
	}
	public final void setVlrFretePeso(BigDecimal vlrFretePeso) {
		this.vlrFretePeso = vlrFretePeso;
	}
	public final BigDecimal getVlrFreteValor() {
		return vlrFreteValor;
	}
	public final void setVlrFreteValor(BigDecimal vlrFreteValor) {
		this.vlrFreteValor = vlrFreteValor;
	}
	public final BigDecimal getVlrCat() {
		return vlrCat;
	}
	public final void setVlrCat(BigDecimal vlrCat) {
		this.vlrCat = vlrCat;
	}
	public final BigDecimal getVlrDespacho() {
		return vlrDespacho;
	}
	public final void setVlrDespacho(BigDecimal vlrDespacho) {
		this.vlrDespacho = vlrDespacho;
	}
	public final BigDecimal getVlrItr() {
		return vlrItr;
	}
	public final void setVlrItr(BigDecimal vlrItr) {
		this.vlrItr = vlrItr;
	}
	public final BigDecimal getVlrAdeme() {
		return vlrAdeme;
	}
	public final void setVlrAdeme(BigDecimal vlrAdeme) {
		this.vlrAdeme = vlrAdeme;
	}
	public final BigDecimal getVlrPedagio() {
		return vlrPedagio;
	}
	public final void setVlrPedagio(BigDecimal vlrPedagio) {
		this.vlrPedagio = vlrPedagio;
	}
	public final BigDecimal getVlrTaxas() {
		return vlrTaxas;
	}
	public final void setVlrTaxas(BigDecimal vlrTaxas) {
		this.vlrTaxas = vlrTaxas;
	}
	public final BigDecimal getOutrosValores() {
		return outrosValores;
	}
	public final void setOutrosValores(BigDecimal outrosValores) {
		this.outrosValores = outrosValores;
	}
	public final BigDecimal getVlrIcms() {
		return vlrIcms;
	}
	public final void setVlrIcms(BigDecimal vlrIcms) {
		this.vlrIcms = vlrIcms;
	}
	public final BigDecimal getVlrBaseCalcIcms() {
		return vlrBaseCalcIcms;
	}
	public final void setVlrBaseCalcIcms(BigDecimal vlrBaseCalcIcms) {
		this.vlrBaseCalcIcms = vlrBaseCalcIcms;
	}
	public final BigDecimal getAliqIcms() {
		return aliqIcms;
	}
	public final void setAliqIcms(BigDecimal aliqIcms) {
		this.aliqIcms = aliqIcms;
	}
	public final BigDecimal getVlrFreteLiquido() {
		return vlrFreteLiquido;
	}
	public final void setVlrFreteLiquido(BigDecimal vlrFreteLiquido) {
		this.vlrFreteLiquido = vlrFreteLiquido;
	}
	public final BigDecimal getVlrFreteTotal() {
		return vlrFreteTotal;
	}
	public final void setVlrFreteTotal(BigDecimal vlrFreteTotal) {
		this.vlrFreteTotal = vlrFreteTotal;
	}
	public final BigDecimal getPesoRealTotal() {
		return pesoRealTotal;
	}
	public final void setPesoRealTotal(BigDecimal pesoRealTotal) {
		this.pesoRealTotal = pesoRealTotal;
	}
	public final BigDecimal getPesoCubadoTotal() {
		return pesoCubadoTotal;
	}
	public final void setPesoCubadoTotal(BigDecimal pesoCubadoTotal) {
		this.pesoCubadoTotal = pesoCubadoTotal;
	}
	public final BigDecimal getVlrTotalMercTotal() {
		return vlrTotalMercTotal;
	}
	public final void setVlrTotalMercTotal(BigDecimal vlrTotalMercTotal) {
		this.vlrTotalMercTotal = vlrTotalMercTotal;
	}
	public String getDsDivisaoCliente() {
		return dsDivisaoCliente;
	}
	public void setDsDivisaoCliente(String dsDivisaoCliente) {
		this.dsDivisaoCliente = dsDivisaoCliente;
	}
	
	public String getNrCtrcSubcontratante() {
		return nrCtrcSubcontratante;
	}
	public void setNrCtrcSubcontratante(String nrCtrcSubcontratante) {
		this.nrCtrcSubcontratante = nrCtrcSubcontratante;
	}
	
	public final String getErro() {
		return erro;
	}
	public final void setErro(String erro) {
		this.erro = erro;
	}
	public static NotaFiscal parseMap(HashMap<String, Object> mapa){
		NotaFiscal nf = new NotaFiscal();
		
		if(mapa.get("ERRO") != null)
			nf.setErro((String)mapa.get("ERRO"));
		
		if (mapa.get("ALIQ_ICMS") != null)
			nf.setAliqIcms(new BigDecimal(mapa.get("ALIQ_ICMS").toString()));

		if (mapa.get("ALIQ_NF") != null)
			nf.setAliqNf(new BigDecimal(mapa.get("ALIQ_NF").toString()));

		if (mapa.get("CFOP_NF") != null)
			nf.setCfopNf((Short)mapa.get("CFOP_NF"));

		if (mapa.get("CHAVE_NFE") != null)
			nf.setChaveNfe(mapa.get("CHAVE_NFE").toString());

		if (mapa.get("DATA_EMISSAO_NF") != null)
			nf.setDataEmissaoNf((Date)mapa.get("DATA_EMISSAO_NF"));

		if (mapa.get("ESPECIE") != null)
			nf.setEspecie(mapa.get("ESPECIE").toString());

		if (mapa.get("MODAL_FRETE") != null)
			nf.setModalFrete(mapa.get("MODAL_FRETE").toString());

		if (mapa.get("NATUREZA") != null)
			nf.setNatureza(mapa.get("NATUREZA").toString());

		if (mapa.get("NR_NOTA_FISCAL") != null)
			//nf.setNrNotaFiscal(mapa.get("NR_NOTA_FISCAL"));
			nf.setNrNotaFiscal(Integer.valueOf(mapa.get("NR_NOTA_FISCAL").toString()));

		if (mapa.get("OUTROS_VALORES") != null)
			nf.setOutrosValores(new BigDecimal(mapa.get("OUTROS_VALORES").toString()));

		if (mapa.get("PESO_CUBADO") != null)
			nf.setPesoCubado(new BigDecimal(mapa.get("PESO_CUBADO").toString()));

		if (mapa.get("PESO_CUBADO_TOTAL") != null)
			nf.setPesoCubadoTotal(new BigDecimal(mapa.get("PESO_CUBADO_TOTAL").toString()));

		if (mapa.get("PESO_REAL") != null)
			nf.setPesoReal(new BigDecimal(mapa.get("PESO_REAL").toString()));

		if (mapa.get("PESO_REAL_TOTAL") != null)
			nf.setPesoRealTotal(new BigDecimal(mapa.get("PESO_REAL_TOTAL").toString()));

		if (mapa.get("PIN_SUFRAMA") != null)
			nf.setPinSuframa(Integer.valueOf(mapa.get("PIN_SUFRAMA").toString()));

		if (mapa.get("QTDE_VOLUMES") != null)
			nf.setQtdeVolumes(new BigDecimal(mapa.get("QTDE_VOLUMES").toString()));

		if (mapa.get("SERIE_NF") != null)
			nf.setSerieNf(mapa.get("SERIE_NF").toString());

		if (mapa.get("TARIFA") != null)
			nf.setTarifa((Short)mapa.get("TARIFA"));

		if (mapa.get("TIPO_FRETE") != null)
			nf.setTipoFrete(mapa.get("TIPO_FRETE").toString());

		if (mapa.get("TIPO_TABELA") != null)
			nf.setTipoTabela(mapa.get("TIPO_TABELA").toString());

		if (mapa.get("VLR_ADEME") != null)
			nf.setVlrAdeme(new BigDecimal(mapa.get("VLR_ADEME").toString()));

		if (mapa.get("VLR_BASE_CALC_ICMS") != null)
			nf.setVlrBaseCalcIcms(new BigDecimal(mapa.get("VLR_BASE_CALC_ICMS").toString()));

		if (mapa.get("VLR_BASE_CALC_NF") != null)
			nf.setVlrBaseCalcNf(new BigDecimal(mapa.get("VLR_BASE_CALC_NF").toString()));
		
		if (mapa.get("VLR_BASE_CALC_ST_NF") != null)
			nf.setVlrBaseCalcStNf(new BigDecimal(mapa.get("VLR_BASE_CALC_ST_NF").toString()));
		
		if (mapa.get("VLR_CAT") != null)
			nf.setVlrCat(new BigDecimal(mapa.get("VLR_CAT").toString()));

		if (mapa.get("VLR_DESPACHO") != null)
			nf.setVlrDespacho(new BigDecimal(mapa.get("VLR_DESPACHO").toString()));

		if (mapa.get("VLR_FRETE_LIQUIDO") != null)
			nf.setVlrFreteLiquido(new BigDecimal(mapa.get("VLR_FRETE_LIQUIDO").toString()));

		if (mapa.get("VLR_FRETE_PESO") != null)
			nf.setVlrFretePeso(new BigDecimal(mapa.get("VLR_FRETE_PESO").toString()));

		if (mapa.get("VLR_FRETE_VALOR") != null)
			nf.setVlrFreteValor(new BigDecimal(mapa.get("VLR_FRETE_VALOR").toString()));
		
		if (mapa.get("VLR_FRETE_TOTAL") != null)
			nf.setVlrFreteTotal(new BigDecimal(mapa.get("VLR_FRETE_TOTAL").toString()));

		if (mapa.get("VLR_ICMS") != null)
			nf.setVlrIcms(new BigDecimal(mapa.get("VLR_ICMS").toString()));

		if (mapa.get("VLR_ICMS_NF") != null)
			nf.setVlrIcmsNf(new BigDecimal(mapa.get("VLR_ICMS_NF").toString()));

		if (mapa.get("VLR_ICMS_ST_NF") != null)
			nf.setVlrIcmsStNf(new BigDecimal(mapa.get("VLR_ICMS_ST_NF").toString()));

		if (mapa.get("VLR_ITR") != null)
			nf.setVlrItr(new BigDecimal(mapa.get("VLR_ITR").toString()));

		if (mapa.get("VLR_PEDAGIO") != null)
			nf.setVlrPedagio(new BigDecimal(mapa.get("VLR_PEDAGIO").toString()));

		if (mapa.get("VLR_TAXAS") != null)
			nf.setVlrTaxas(new BigDecimal(mapa.get("VLR_TAXAS").toString()));

		if (mapa.get("VLR_TOTAL_MERC") != null)
			nf.setVlrTotalMerc(new BigDecimal(mapa.get("VLR_TOTAL_MERC").toString()));

		if (mapa.get("VLR_TOTAL_MERC_TOTAL") != null)
			nf.setVlrTotalMercTotal(new BigDecimal(mapa.get("VLR_TOTAL_MERC_TOTAL").toString()));

		if (mapa.get("VLR_TOT_PRODUTOS_NF") != null)
			nf.setVlrTotProdutosNf(new BigDecimal(mapa.get("VLR_TOT_PRODUTOS_NF").toString()));

		if (mapa.get("DS_DIVISAO_CLIENTE") != null)
			nf.setDsDivisaoCliente(mapa.get("DS_DIVISAO_CLIENTE").toString());
		
		if (mapa.get("NR_CTRC_SUBCONTRATANTE") != null)
			nf.setNrCtrcSubcontratante(mapa.get("NR_CTRC_SUBCONTRATANTE").toString());
		

		return nf;
	}
	
}
