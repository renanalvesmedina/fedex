package com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO; 
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
 
public class ModelosMensagensFilterDTO extends BaseFilterDTO { 
	
	private static final long serialVersionUID = 1L; 
	private DomainValue tpModeloMensagem;
	private YearMonthDay dtVigencia;
		
	public void setTpModeloMensagem(DomainValue tpModeloMensagem) {
		this.tpModeloMensagem = tpModeloMensagem;
	}
	
	public void setDtVigencia(YearMonthDay dtVigencia) {
		this.dtVigencia = dtVigencia;
	}
	
	public DomainValue getTpModeloMensagem() {
		return tpModeloMensagem;
	}
	
	public YearMonthDay getDtVigencia() {
		return dtVigencia;
	}
	
	public static ModeloMensagem build(){
		ModeloMensagem modeloMensagem = new ModeloMensagem();
		return modeloMensagem;
	}
} 
