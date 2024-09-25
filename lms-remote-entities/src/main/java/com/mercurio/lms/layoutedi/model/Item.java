package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class Item  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3061588016601620318L;
	private String codItem;
	private BigDecimal pesoRealItem;
	private BigDecimal pesoCubadoItem;
	private BigDecimal alturaItem;
	private BigDecimal larguraItem;
	private BigDecimal comprimentoItem;
	private BigDecimal valorItem;
	public final String getCodItem() {
		return codItem;
	}
	public final void setCodItem(String codItem) {
		this.codItem = codItem;
	}
	public final BigDecimal getPesoRealItem() {
		return pesoRealItem;
	}
	public final void setPesoRealItem(BigDecimal pesoRealItem) {
		this.pesoRealItem = pesoRealItem;
	}
	public final BigDecimal getPesoCubadoItem() {
		return pesoCubadoItem;
	}
	public final void setPesoCubadoItem(BigDecimal pesoCubadoItem) {
		this.pesoCubadoItem = pesoCubadoItem;
	}
	public final BigDecimal getAlturaItem() {
		return alturaItem;
	}
	public final void setAlturaItem(BigDecimal alturaItem) {
		this.alturaItem = alturaItem;
	}
	public final BigDecimal getLarguraItem() {
		return larguraItem;
	}
	public final void setLarguraItem(BigDecimal larguraItem) {
		this.larguraItem = larguraItem;
	}
	public final BigDecimal getComprimentoItem() {
		return comprimentoItem;
	}
	public final void setComprimentoItem(BigDecimal comprimentoItem) {
		this.comprimentoItem = comprimentoItem;
	}
	public final BigDecimal getValorItem() {
		return valorItem;
	}
	public final void setValorItem(BigDecimal valorItem) {
		this.valorItem = valorItem;
	}
	
	
	public static Item parseMap(HashMap<String, Object> mapa){
		Item item = new Item();
		if (mapa.get("ALTURA_ITEM") != null)
			item.setAlturaItem(new BigDecimal(mapa.get("ALTURA_ITEM").toString()));

		if (mapa.get("COD_ITEM") != null)
			item.setCodItem((String)mapa.get("COD_ITEM"));

		if (mapa.get("COMPRIMENTO_ITEM") != null)
			item.setComprimentoItem(new BigDecimal(mapa.get("COMPRIMENTO_ITEM").toString()));

		if (mapa.get("LARGURA_ITEM") != null)
			item.setLarguraItem(new BigDecimal(mapa.get("LARGURA_ITEM").toString()));

		if (mapa.get("PESO_CUBADO_ITEM") != null)
			item.setPesoCubadoItem(new BigDecimal(mapa.get("PESO_CUBADO_ITEM").toString()));

		if (mapa.get("PESO_REAL_ITEM") != null)
			item.setPesoRealItem(new BigDecimal(mapa.get("PESO_REAL_ITEM").toString()));
		
		if (mapa.get("VALOR_ITEM") != null)
			item.setValorItem(new BigDecimal(mapa.get("VALOR_ITEM").toString()));

		return item;
	}
	
}
