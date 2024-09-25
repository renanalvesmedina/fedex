package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.util.BigDecimalUtils;

public class DestinoPropostaParcelaDTO extends BaseDTO implements Comparable<DestinoPropostaParcelaDTO>{
    
    private static final long serialVersionUID = 5110505413707219800L;
    
    
    private String dsParcela;
    private BigDecimal vlOriginal;
    private DomainValue tpIndicador;
    private BigDecimal vlCalculado;
    private BigDecimal vlPercentual;
    
    private String cdParcelaPreco;
    private Long idFaixaProgressivaProposta;
    private Long idValorFaixaProgressiva;
    private BigDecimal vlExcedente;
    private BigDecimal vlExcedenteOriginal;
    private BigDecimal psMinimo;
    private BigDecimal psMinimoOriginal;
    private Long idProdutoEspecifico;
    private BigDecimal vlFaixaProgressiva;
    
    private Byte order;
    
    public DestinoPropostaParcelaDTO(){
        super();
    }
    
    public DestinoPropostaParcelaDTO(String dsParcela, BigDecimal vlOriginal, DomainValue tpIndicador, BigDecimal vlCalculado,
            BigDecimal vlPercentual) {
        super();
        this.dsParcela = dsParcela;
        this.vlOriginal = vlOriginal;
        this.tpIndicador = tpIndicador;
        this.vlCalculado = vlCalculado;
        this.vlPercentual = vlPercentual;
    }
    
    public String getDsParcela() {
        return dsParcela;
    }
    public void setDsParcela(String dsFaixa) {
        this.dsParcela = dsFaixa;
    }
    public BigDecimal getVlOriginal() {
        return vlOriginal;
    }
    public void setVlOriginal(BigDecimal vlOriginal) {
        this.vlOriginal = vlOriginal;
    }
    public DomainValue getTpIndicador() {
        return tpIndicador;
    }
    public void setTpIndicador(DomainValue tpIndicador) {
        this.tpIndicador = tpIndicador;
    }
    public BigDecimal getVlCalculado() {
        return vlCalculado;
    }
    public void setVlCalculado(BigDecimal vlCalculado) {
        this.vlCalculado = vlCalculado;
    }
    public BigDecimal getVlPercentual() {
        return vlPercentual;
    }
    public void setVlPercentual(BigDecimal vlPercentual) {
        this.vlPercentual = vlPercentual;
    }

    public String getCdParcelaPreco() {
        return cdParcelaPreco;
    }

    public void setCdParcelaPreco(String cdParcelaPreco) {
        this.cdParcelaPreco = cdParcelaPreco;
    }

    public Long getIdValorFaixaProgressiva() {
        return idValorFaixaProgressiva;
    }

    public void setIdValorFaixaProgressiva(Long idValorFaixaProgressiva) {
        this.idValorFaixaProgressiva = idValorFaixaProgressiva;
    }

    public BigDecimal getVlExcedente() {
        return vlExcedente;
    }

    public void setVlExcedente(BigDecimal vlExcedente) {
        this.vlExcedente = vlExcedente;
    }

    public BigDecimal getPsMinimo() {
        return psMinimo;
    }

    public void setPsMinimo(BigDecimal psMinimo) {
        this.psMinimo = psMinimo;
    }

    public Long getIdProdutoEspecifico() {
        return idProdutoEspecifico;
    }

    public void setIdProdutoEspecifico(Long idProdutoEspecifico) {
        this.idProdutoEspecifico = idProdutoEspecifico;
    }
    
    

    public Byte getOrder() {
        return order;
    }

    public void setOrder(Byte order) {
        this.order = order;
    }
    
    public BigDecimal calculaDescontoAcrescimo(){
        if (tpIndicador == null || tpIndicador.getValue() == null || BigDecimal.ZERO.equals(vlOriginal)){
            return BigDecimal.ZERO;
        }
        
        if (tpIndicador.getValue().equals("D")){
            return vlOriginal.subtract(vlCalculado).multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal,BigDecimal.ROUND_UP);
        }else if(tpIndicador.getValue().equals("A")){
            return vlCalculado.subtract(vlOriginal).multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal, BigDecimal.ROUND_UP);
        }else if(tpIndicador.getValue().equals("V")){
        	BigDecimal diferenca = vlCalculado.subtract(vlOriginal);
	        return diferenca.multiply(BigDecimalUtils.HUNDRED).divide(vlOriginal, BigDecimal.ROUND_UP);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int compareTo(DestinoPropostaParcelaDTO o) {
        return this.order.compareTo(o.order);
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

    public BigDecimal getVlFaixaProgressiva() {
        return vlFaixaProgressiva;
    }

    public void setVlFaixaProgressiva(BigDecimal vlFaixaProgressiva) {
        this.vlFaixaProgressiva = vlFaixaProgressiva;
    }

	public Long getIdFaixaProgressivaProposta() {
		return idFaixaProgressivaProposta;
	}

	public void setIdFaixaProgressivaProposta(Long idFaixaProgressivaProposta) {
		this.idFaixaProgressivaProposta = idFaixaProgressivaProposta;
	}
}
