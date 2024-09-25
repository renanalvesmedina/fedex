package com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
 
public class ExcecaoNegativacaoFilterDTO extends BaseFilterDTO { 

	private static final long serialVersionUID = 1L; 
	private FaturaDTO fatura;
	private FilialSuggestDTO filial;
	private YearMonthDay dtVigente;
	
	public FaturaDTO getFatura() {
		return fatura;
	}
	public void setFatura(FaturaDTO fatura) {
		this.fatura = fatura;
	}
	public YearMonthDay getDtVigente() {
		return dtVigente;
	}
	public void setDtVigente(YearMonthDay dtVigente) {
		this.dtVigente = dtVigente;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	
} 
