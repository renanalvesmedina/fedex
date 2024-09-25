package com.mercurio.lms.rest.contasareceber.lotecobranca.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO; 
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.FaturaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
 
public class LoteCobrancaFilterDTO extends BaseFilterDTO { 

	private static final long serialVersionUID = 1L; 
	private String nrLote;
	private YearMonthDay dtEnvioLoteInicial;
	private YearMonthDay dtEnvioLoteFinal;
	private YearMonthDay dtAlteracaoLoteInicial;
	private YearMonthDay dtAlteracaoLoteFinal;
	private DomainValue tpLote;
	private DomainValue tpLoteEnviado;
	private String descricao;
	private FaturaDTO fatura;
	private FilialSuggestDTO filial;
	
	public String getNrLote() {
		return nrLote;
	}
	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}
	public YearMonthDay getDtEnvioLoteInicial() {
		return dtEnvioLoteInicial;
	}
	public void setDtEnvioLoteInicial(YearMonthDay dtEnvioLoteInicial) {
		this.dtEnvioLoteInicial = dtEnvioLoteInicial;
	}
	public YearMonthDay getDtEnvioLoteFinal() {
		return dtEnvioLoteFinal;
	}
	public void setDtEnvioLoteFinal(YearMonthDay dtEnvioLoteFinal) {
		this.dtEnvioLoteFinal = dtEnvioLoteFinal;
	}
	public YearMonthDay getDtAlteracaoLoteInicial() {
		return dtAlteracaoLoteInicial;
	}
	public void setDtAlteracaoLoteInicial(YearMonthDay dtAlteracaoLoteInicial) {
		this.dtAlteracaoLoteInicial = dtAlteracaoLoteInicial;
	}
	public YearMonthDay getDtAlteracaoLoteFinal() {
		return dtAlteracaoLoteFinal;
	}
	public void setDtAlteracaoLoteFinal(YearMonthDay dtAlteracaoLoteFinal) {
		this.dtAlteracaoLoteFinal = dtAlteracaoLoteFinal;
	}
	public DomainValue getTpLote() {
		return tpLote;
	}
	public void setTpLote(DomainValue tpLote) {
		this.tpLote = tpLote;
	}
	public DomainValue getTpLoteEnviado() {
		return tpLoteEnviado;
	}
	public void setTpLoteEnviado(DomainValue tpLoteEnviado) {
		this.tpLoteEnviado = tpLoteEnviado;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public FaturaDTO getFatura() {
		return fatura;
	}
	public void setFatura(FaturaDTO fatura) {
		this.fatura = fatura;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
} 
