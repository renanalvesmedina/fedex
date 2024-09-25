package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

public class ParcelaServico implements Comparable, Serializable {
	private static final long serialVersionUID = 1L;
	private ParcelaPreco parcelaPreco;
	private BigDecimal vlUnitarioParcela;
	private BigDecimal vlBrutoParcela;
	private BigDecimal vlParcela;
	private BigDecimal vlEmbuteParcela;
	private BigDecimal vlParcelaOriginal;
	private int nrOrdem;
	private Object parametro;
	private Map parametrosAuxiliares;
	private Boolean blEmbuteParcela;
	private Boolean blCalculoBaseVlMinimo;

	public ParcelaServico(ParcelaPreco parcelaPreco) {
		this.setParcelaPreco(parcelaPreco);
	}

	public ParcelaServico(ParcelaPreco parcelaPreco,
			BigDecimal vlUnitarioParcela, BigDecimal vlBrutoParcela) {
		this.setParcelaPreco(parcelaPreco);
		this.setVlUnitarioParcela(vlUnitarioParcela);
		this.setVlBrutoParcela(vlBrutoParcela);
	}

	public ParcelaPreco getParcelaPreco() {
		return this.parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public BigDecimal getVlUnitarioParcela() {
		return vlUnitarioParcela;
	}

	public void setVlUnitarioParcela(BigDecimal vlUnitarioParcela) {
		if(vlUnitarioParcela != null){
			this.vlUnitarioParcela = vlUnitarioParcela.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlUnitarioParcela = vlUnitarioParcela;
	}
	}

	public BigDecimal getVlBrutoParcela() {
		return vlBrutoParcela;
	}

	public void setVlBrutoParcela(BigDecimal vlBrutoParcela) {
		if(vlBrutoParcela != null){
			this.vlBrutoParcela = vlBrutoParcela.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlBrutoParcela = vlBrutoParcela;
	}
	}

	public BigDecimal getVlParcela() {
		return vlParcela;
	}

	public void setVlParcela(BigDecimal vlParcela) {
		if(vlParcela != null){
			this.vlParcela = vlParcela.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlParcela = vlParcela;
	}
	}

	public BigDecimal getVlParcelaOriginal() {
		return vlParcelaOriginal;
	}

	public void setVlParcelaOriginal(BigDecimal vlParcelaOriginal) {
		if(vlParcelaOriginal != null){
			this.vlParcelaOriginal = vlParcelaOriginal.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlParcelaOriginal = vlParcelaOriginal;
	}
	}

	public void setNrOrdem(int nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	public int getNrOrdem() {
		return nrOrdem;
	}

	public Object getParametro() {
		return parametro;
	}

	public void setParametro(Object parametro) {
		this.parametro = parametro;
	}

	public BigDecimal getParametroAuxiliar(String key) {
		return (BigDecimal) parametrosAuxiliares.get(key);
	}

	public void addParametroAuxiliar(String key, Object value) {
		if(parametrosAuxiliares == null) {
			parametrosAuxiliares = new TypedFlatMap();
		}
		parametrosAuxiliares.put(key, value);
	}

	public Boolean getBlEmbuteParcela() {
		return blEmbuteParcela;
	}

	public void setBlEmbuteParcela(Boolean blEmbuteParcela) {
		this.blEmbuteParcela = blEmbuteParcela;
	}

	public int compareTo(Object o) {
		Collator collator = Collator.getInstance(LocaleContextHolder
				.getLocale());
		ParcelaServico t = (ParcelaServico) o;
		return collator
				.compare(this.getParcelaPreco().getNmParcelaPreco().getValue(),
				t.getParcelaPreco().getNmParcelaPreco().getValue());
	}

	public BigDecimal getVlEmbuteParcela() {
		return vlEmbuteParcela;
	}

	public void setVlEmbuteParcela(BigDecimal vlEmbuteParcela) {
		this.vlEmbuteParcela = vlEmbuteParcela;
	}

	public Boolean getBlCalculoBaseVlMinimo() {
		return blCalculoBaseVlMinimo;
	}

	public void setBlCalculoBaseVlMinimo(Boolean blCalculoBaseVlMinimo) {
		this.blCalculoBaseVlMinimo = blCalculoBaseVlMinimo;
	}

}