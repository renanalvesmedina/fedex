package com.mercurio.lms.rest.vendas.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.util.BigDecimalUtils;

public class ParametroPropostaDestinoAereoColunaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal vlOriginal;
	private BigDecimal valor;
	private DomainValue tpIndicador;
	private BigDecimal percentual;
	private BigDecimal psMinimoOriginal;
	private BigDecimal psMinimo;
	private BigDecimal vlExcedenteOriginal;
	private BigDecimal vlExcedente;

	public ParametroPropostaDestinoAereoColunaDTO(){
		
	}
	
	public ParametroPropostaDestinoAereoColunaDTO(BigDecimal vlOriginal, BigDecimal valor, String tpIndicadorValue, String tpIndicadorDescription, BigDecimal percentual) {
		super();
		this.vlOriginal = vlOriginal;
		this.valor = valor;
		this.tpIndicador = new DomainValue(tpIndicadorValue, new VarcharI18n(tpIndicadorDescription), true);
		this.percentual = percentual;
	}


	public BigDecimal getVlOriginal() {
		return vlOriginal;
	}

	public void setVlOriginal(BigDecimal vlOriginal) {
		this.vlOriginal = vlOriginal;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}
	
	public BigDecimal calculaDescontoAcrescimo(){
	    if (tpIndicador == null || tpIndicador.getValue() == null || BigDecimal.ZERO.equals(vlOriginal) || BigDecimal.ZERO.equals(valor)){
	        return BigDecimal.ZERO;
	    }
	    
	    if (tpIndicador.getValue().equals("D")){
	        return vlOriginal.subtract(valor).multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal,BigDecimal.ROUND_UP);
	    } else if(tpIndicador.getValue().equals("A")){
	        return valor.subtract(vlOriginal).multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal, BigDecimal.ROUND_UP);
	    } else if(tpIndicador.getValue().equals("V")){
	    	BigDecimal diferenca = valor.subtract(vlOriginal);
	        return diferenca.multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal, BigDecimal.ROUND_UP);
	    } 
	    return BigDecimal.ZERO;
	}

	public BigDecimal getPsMinimo() {
		return psMinimo;
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		this.psMinimo = psMinimo;
	}

	public BigDecimal getVlExcedente() {
		return vlExcedente;
	}

	public void setVlExcedente(BigDecimal vlExcedente) {
		this.vlExcedente = vlExcedente;
	}

	public BigDecimal getVlExcedenteOriginal() {
		return vlExcedenteOriginal;
	}

	public void setVlExcedenteOriginal(BigDecimal vlExcedenteOriginal) {
		this.vlExcedenteOriginal = vlExcedenteOriginal;
	}

	public BigDecimal getPsMinimoOriginal() {
		return psMinimoOriginal;
	}

	public void setPsMinimoOriginal(BigDecimal psMinimoOriginal) {
		this.psMinimoOriginal = psMinimoOriginal;
	}
}
