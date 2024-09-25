package com.mercurio.lms.rest.contasareceber.diascortefaturamento.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO; 
 
public class DiasCorteFaturamentoFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
 
	private YearMonthDay dataCorteInicial;
	private YearMonthDay dataCorteFinal;
	private YearMonthDay dataAlteracaoInicial;
	private YearMonthDay dataAlteracaoFinal;
	
	public YearMonthDay getDataCorteInicial() {
		return dataCorteInicial;
	}
	public void setDataCorteInicial(YearMonthDay dataCorteInicial) {
		this.dataCorteInicial = dataCorteInicial;
	}
	public YearMonthDay getDataCorteFinal() {
		return dataCorteFinal;
	}
	public void setDataCorteFinal(YearMonthDay dataCorteFinal) {
		this.dataCorteFinal = dataCorteFinal;
	}
	public YearMonthDay getDataAlteracaoInicial() {
		return dataAlteracaoInicial;
	}
	public void setDataAlteracaoInicial(YearMonthDay dataAlteracaoInicial) {
		this.dataAlteracaoInicial = dataAlteracaoInicial;
	}
	public YearMonthDay getDataAlteracaoFinal() {
		return dataAlteracaoFinal;
	}
	public void setDataAlteracaoFinal(YearMonthDay dataAlteracaoFinal) {
		this.dataAlteracaoFinal = dataAlteracaoFinal;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
} 
