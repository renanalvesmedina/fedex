package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.contasreceber.util.SituacaoFaturaLookup;

public class FaturaLookupParam {

	private Long idFilialFaturamentoFatura;
	
	private String sgFilialFaturamentoFatura;
	
	private Long nrFatura;
	
	private Long idClienteFatura;
	
	private Long idFilialCobrancaFatura;
	
	private String sgFilialCobrancaFatura;
	
	private String nrPreFatura;
	
	private Long idBancoFatura;
	
	private String tpModalFatura;
	
	private String tpAbrangemciaFatura;
	
	private String tpSituacaoFatura;
	
	private YearMonthDay dtLiquidacaoInicialFatura;
	
	private YearMonthDay dtLiquidacaoFinalFatura;
	
	private YearMonthDay dtVencimentoInicialFatura;
	
	private YearMonthDay dtVencimentoFinalFatura;
	
	private YearMonthDay dtEmissaoInicialFatura;
	
	private YearMonthDay dtEmissaoFinalFatura;
	
	private Integer tpSituacaoFaturaValido;
	
	/** Get's and Set's */
	
	public Long getIdClienteFatura() {
		return idClienteFatura;
	}

	public void setIdClienteFatura(Long idClienteFatura) {
		this.idClienteFatura = idClienteFatura;
	}

	public Long getIdFilialCobrancaFatura() {
		return idFilialCobrancaFatura;
	}

	public void setIdFilialCobrancaFatura(Long idFilialCobrancaFatura) {
		this.idFilialCobrancaFatura = idFilialCobrancaFatura;
	}

	public Long getIdFilialFaturamentoFatura() {
		return idFilialFaturamentoFatura;
	}

	public void setIdFilialFaturamentoFatura(Long idFilialFaturamentoFatura) {
		this.idFilialFaturamentoFatura = idFilialFaturamentoFatura;
	}

	public Long getNrFatura() {
		return nrFatura;
	}
	
	public void setNrFatura(Long nrFatura) {
		this.nrFatura = nrFatura;
	}
	
	public String getNrPreFatura() {
		return nrPreFatura;
	}
	
	public void setNrPreFatura(String nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public Long getIdBancoFatura() {
		return idBancoFatura;
	}

	public void setIdBancoFatura(Long idBancoFatura) {
		this.idBancoFatura = idBancoFatura;
	}

	public String getTpAbrangemciaFatura() {
		return tpAbrangemciaFatura;
	}

	public void setTpAbrangemciaFatura(String tpAbrangemciaFatura) {
		this.tpAbrangemciaFatura = tpAbrangemciaFatura;
	}

	public String getTpModalFatura() {
		return tpModalFatura;
	}

	public void setTpModalFatura(String tpModalFatura) {
		this.tpModalFatura = tpModalFatura;
	}

	public String getTpSituacaoFatura() {
		return tpSituacaoFatura;
	}

	public void setTpSituacaoFatura(String tpSituacaoFatura) {
		this.tpSituacaoFatura = tpSituacaoFatura;
	}

	public YearMonthDay getDtLiquidacaoFinalFatura() {
		return dtLiquidacaoFinalFatura;
	}

	public void setDtLiquidacaoFinalFatura(YearMonthDay dtLiquidacaoFinalFatura) {
		this.dtLiquidacaoFinalFatura = dtLiquidacaoFinalFatura;
	}

	public YearMonthDay getDtLiquidacaoInicialFatura() {
		return dtLiquidacaoInicialFatura;
	}

	public void setDtLiquidacaoInicialFatura(YearMonthDay dtLiquidacaoInicialFatura) {
		this.dtLiquidacaoInicialFatura = dtLiquidacaoInicialFatura;
	}

	public YearMonthDay getDtEmissaoFinalFatura() {
		return dtEmissaoFinalFatura;
	}

	public void setDtEmissaoFinalFatura(YearMonthDay dtEmissaoFinalFatura) {
		this.dtEmissaoFinalFatura = dtEmissaoFinalFatura;
	}

	public YearMonthDay getDtEmissaoInicialFatura() {
		return dtEmissaoInicialFatura;
	}

	public void setDtEmissaoInicialFatura(YearMonthDay dtEmissaoInicialFatura) {
		this.dtEmissaoInicialFatura = dtEmissaoInicialFatura;
	}

	public YearMonthDay getDtVencimentoFinalFatura() {
		return dtVencimentoFinalFatura;
	}

	public void setDtVencimentoFinalFatura(YearMonthDay dtVencimentoFinalFatura) {
		this.dtVencimentoFinalFatura = dtVencimentoFinalFatura;
	}

	public YearMonthDay getDtVencimentoInicialFatura() {
		return dtVencimentoInicialFatura;
	}

	public void setDtVencimentoInicialFatura(YearMonthDay dtVencimentoInicialFatura) {
		this.dtVencimentoInicialFatura = dtVencimentoInicialFatura;
	}

	public List getTpSituacaoFaturaValidoList() {
		return (new SituacaoFaturaLookup(tpSituacaoFaturaValido)).getTpSituacoesFatura();
	}
	
	public Integer getTpSituacaoFaturaValido() {
		return tpSituacaoFaturaValido;
	}	

	public void setTpSituacaoFaturaValido(Integer tpSituacaoFaturaValido) {
		this.tpSituacaoFaturaValido = tpSituacaoFaturaValido;
	}

	public String getSgFilialCobrancaFatura() {
		return sgFilialCobrancaFatura;
	}

	public void setSgFilialCobrancaFatura(String sgFilialCobrancaFatura) {
		this.sgFilialCobrancaFatura = sgFilialCobrancaFatura;
	}

	public String getSgFilialFaturamentoFatura() {
		return sgFilialFaturamentoFatura;
	}

	public void setSgFilialFaturamentoFatura(String sgFilialFaturamentoFatura) {
		this.sgFilialFaturamentoFatura = sgFilialFaturamentoFatura;
	}

}
