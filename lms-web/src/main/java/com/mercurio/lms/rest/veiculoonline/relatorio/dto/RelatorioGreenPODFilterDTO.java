package com.mercurio.lms.rest.veiculoonline.relatorio.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class RelatorioGreenPODFilterDTO extends BaseFilterDTO {

    private static final long serialVersionUID = 7488562180247630948L;
    
    private YearMonthDay dtPeriodoInicial;
    private YearMonthDay dtPeriodoFinal;
    private FilialSuggestDTO filialOrigem;
    private FilialSuggestDTO filialDestino;
    
    public YearMonthDay getDtPeriodoInicial() {
		return dtPeriodoInicial;
	}
	public void setDtPeriodoInicial(YearMonthDay dtPeriodoInicial) {
		this.dtPeriodoInicial = dtPeriodoInicial;
	}
	public YearMonthDay getDtPeriodoFinal() {
		return dtPeriodoFinal;
	}
	public void setDtPeriodoFinal(YearMonthDay dtPeriodoFinal) {
		this.dtPeriodoFinal = dtPeriodoFinal;
	}
	public FilialSuggestDTO getFilialOrigem() {
		return filialOrigem;
	}
	public void setFilialOrigem(FilialSuggestDTO filialOrigem) {
		this.filialOrigem = filialOrigem;
	}
	public FilialSuggestDTO getFilialDestino() {
		return filialDestino;
	}
	public void setFilialDestino(FilialSuggestDTO filialDestino) {
		this.filialDestino = filialDestino;
	}
}
