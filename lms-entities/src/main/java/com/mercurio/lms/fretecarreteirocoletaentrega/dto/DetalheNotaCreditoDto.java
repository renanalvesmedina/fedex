package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class DetalheNotaCreditoDto {

	private BigDecimal vlAcrescimo = BigDecimal.ZERO;
	private BigDecimal vlDesconto = BigDecimal.ZERO;
	private BigDecimal vlDescUsoEquipamento = BigDecimal.ZERO;
	private String obNotaCredito;
	private String dsObservacaoOutrasNotas;
	
	public BigDecimal getVlAcrescimo() {
		return vlAcrescimo;
	}
	
	public void setVlAcrescimo(BigDecimal vlAcrescimo) {
		this.vlAcrescimo = vlAcrescimo;
	}
	
	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}
	
	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}
	
	public BigDecimal getVlDescUsoEquipamento() {
		return vlDescUsoEquipamento;
	}
	
	public void setVlDescUsoEquipamento(BigDecimal vlDescUsoEquipamento) {
		this.vlDescUsoEquipamento = vlDescUsoEquipamento;
	}
	
	public String getObNotaCredito() {
		return obNotaCredito;
	}

	public void setObNotaCredito(String obNotaCredito) {
		this.obNotaCredito = obNotaCredito;
	}

	public String getDsObservacaoOutrasNotas() {
		return dsObservacaoOutrasNotas;
	}
	
	public void setDsObservacaoOutrasNotas(String dsObservacaoOutrasNotas) {
		this.dsObservacaoOutrasNotas = dsObservacaoOutrasNotas;
	}
	
	public boolean hasObservacao() {
		return StringUtils.isNotBlank(dsObservacaoOutrasNotas);
	}
	
	public String getObsFormatada(){
		String obsFormatada = new String();
		if(StringUtils.isNotBlank(obNotaCredito)){
			obsFormatada = obsFormatada + obNotaCredito;
		}
		if(dsObservacaoOutrasNotas != null){
			if(StringUtils.isNotBlank(obNotaCredito)) obsFormatada = obsFormatada + " "; 
			obsFormatada = obsFormatada +  "Existem outras notas de crédito emitidas para o mesmo período " + dsObservacaoOutrasNotas;
		}
		return obsFormatada;
	}
	
}
