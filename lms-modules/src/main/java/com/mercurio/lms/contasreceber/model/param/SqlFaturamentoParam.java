package com.mercurio.lms.contasreceber.model.param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.JTDateTimeUtils;


public class SqlFaturamentoParam {
	
	private Long idFilialCobranca;
	
	private Long idCliente;
	
	private Long idDivisaoCliente;
	
	private String tpFrete;
	
	private String tpModal;
	
	private String tpAbrangencia;
	
	private Long idFormaAgrupamento;
	
	private Long idTipoAgrupamento;
	
	private Long idAgrupamentoCliente;
	
	private YearMonthDay dtInicioEmissao;
	
	private YearMonthDay dtFinalEmissao;
	
	private Boolean blGerarBoleto;
	
	private YearMonthDay dtEmissao;
	
	private YearMonthDay dtVencimento;
	
	private Long idCedente;
	
	private Long idCotacaoMoeda;
	
	private BigDecimal vlCotacaoMoeda;
	
	private Long tpDiaSemana;
	
	private boolean blFaturamentoManual = false;
	
	private List<Long> nrDiaDecendio = new ArrayList<Long>();
	
	private List<Long> nrDiaQuinzenal = new ArrayList<Long>();
	
	private List<Long> nrDiaMes = new ArrayList<Long>();
	
	private List lstDevedorDocServFat;
	
	private List lstAgrupador;
	

	public SqlFaturamentoParam() {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

		this.tpDiaSemana = Long.valueOf(JTDateTimeUtils.yearMonthDayToDateTime(dataAtual).getDayOfWeek());
		
		int nrDiaMesAtual = dataAtual.getDayOfMonth();
		
		int nrUltimoDiaMes = JTDateTimeUtils.getLastDayOfMonth(dataAtual.getYear(), dataAtual.getMonthOfYear()); 

		//Numero decendio
		if (nrDiaMesAtual < 11){
			this.nrDiaDecendio.add( Long.valueOf( nrDiaMesAtual ) );
		} else if (nrDiaMesAtual < 21) {
			this.nrDiaDecendio.add( nrDiaMesAtual - 10L );
		} else {
			if (nrDiaMesAtual == nrUltimoDiaMes){
				if (nrDiaMesAtual == 28){
					this.nrDiaDecendio.add( 8L );
					this.nrDiaDecendio.add( 9L );
					this.nrDiaDecendio.add( 10L );
				} else if (nrDiaMesAtual == 29){
					this.nrDiaDecendio.add( 9L );
					this.nrDiaDecendio.add( 10L );
				} else  {
					this.nrDiaDecendio.add( 10L );					
				}
			} else {
				this.nrDiaDecendio.add(Long.valueOf( nrDiaMesAtual - 20 ));	
			}
		}
		
		//Numero quinzena
		if (nrDiaMesAtual < 16){
			this.nrDiaQuinzenal.add( Long.valueOf( nrDiaMesAtual ));
		} else {
			if (nrDiaMesAtual == nrUltimoDiaMes){			
				if (nrDiaMesAtual == 28){
					this.nrDiaQuinzenal.add( 13L );
					this.nrDiaQuinzenal.add( 14L );
					this.nrDiaQuinzenal.add( 15L );
				} else if (nrDiaMesAtual == 29){
					this.nrDiaQuinzenal.add( 14L );
					this.nrDiaQuinzenal.add( 15L );
				} else  {
					this.nrDiaQuinzenal.add( 15L );					
				}
			} else {
				this.nrDiaQuinzenal.add( nrDiaMesAtual - 15L );
			}
		}
		
		//Numero mes
		if (nrDiaMesAtual == nrUltimoDiaMes) {			
			
			switch ( nrDiaMesAtual ) {
			
				case 28:
					this.nrDiaMes.add( 28L );
					this.nrDiaMes.add( 29L );
					this.nrDiaMes.add( 30L );
					this.nrDiaMes.add( 31L );
					break;

				case 29: 
					this.nrDiaMes.add( 29L );
					this.nrDiaMes.add( 30L);
					this.nrDiaMes.add( 31L );
					break;
					
				case 30: 
					this.nrDiaMes.add( 30L );
					this.nrDiaMes.add( 31L );
					break;

				default: 
					this.nrDiaMes.add( 31L );
					break;
					
			}
		} else {
			this.nrDiaMes.add( Long.valueOf( nrDiaMesAtual ) );
		}
	}

	public Boolean getBlGerarBoleto() {
		return blGerarBoleto;
	}

	public void setBlGerarBoleto(Boolean blGerarBoleto) {
		this.blGerarBoleto = blGerarBoleto;
	}

	public YearMonthDay getDtFinalEmissao() {
		return dtFinalEmissao;
	}

	public void setDtFinalEmissao(YearMonthDay dtFinalEmissao) {
		this.dtFinalEmissao = dtFinalEmissao;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public YearMonthDay getDtInicioEmissao() {
		return dtInicioEmissao;
	}

	public void setDtInicioEmissao(YearMonthDay dtInicioEmissao) {
		this.dtInicioEmissao = dtInicioEmissao;
	}

	public Long getIdCedente() {
		return idCedente;
	}

	public void setIdCedente(Long idCedente) {
		this.idCedente = idCedente;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdCotacaoMoeda() {
		return idCotacaoMoeda;
	}

	public void setIdCotacaoMoeda(Long idCotacaoMoeda) {
		this.idCotacaoMoeda = idCotacaoMoeda;
	}

	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public Long getIdFilialCobranca() {
		return idFilialCobranca;
	}

	public void setIdFilialCobranca(Long idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}

	public Long getIdFormaAgrupamento() {
		return idFormaAgrupamento;
	}

	public void setIdFormaAgrupamento(Long idFormaAgrupamento) {
		this.idFormaAgrupamento = idFormaAgrupamento;
	}

	public Long getIdTipoAgrupamento() {
		return idTipoAgrupamento;
	}

	public void setIdTipoAgrupamento(Long idTipoAgrupamento) {
		this.idTipoAgrupamento = idTipoAgrupamento;
	}

	public String getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public Long getTpDiaSemana() {
		return tpDiaSemana;
	}

	public void setTpDiaSemana(Long tpDiaSemana) {
		this.tpDiaSemana = tpDiaSemana;
	}

	public Long getIdAgrupamentoCliente() {
		return idAgrupamentoCliente;
	}

	public void setIdAgrupamentoCliente(Long idAgrupamentoCliente) {
		this.idAgrupamentoCliente = idAgrupamentoCliente;
	}

	public List getLstAgrupador() {
		return lstAgrupador;
	}

	public void setLstAgrupador(List lstAgrupador) {
		this.lstAgrupador = lstAgrupador;
	}

	public List getLstDevedorDocServFat() {
		return lstDevedorDocServFat;
	}

	public void setLstDevedorDocServFat(List lstDevedorDocServFat) {
		this.lstDevedorDocServFat = lstDevedorDocServFat;
	}

	public List getNrDiaDecendio() {
		return nrDiaDecendio;
	}

	public void setNrDiaDecendio(List nrDiaDecendio) {
		this.nrDiaDecendio = nrDiaDecendio;
	}

	public List getNrDiaMes() {
		return nrDiaMes;
	}

	public void setNrDiaMes(List nrDiaMes) {
		this.nrDiaMes = nrDiaMes;
	}

	public List getNrDiaQuinzenal() {
		return nrDiaQuinzenal;
	}

	public void setNrDiaQuinzenal(List nrDiaQuinzenal) {
		this.nrDiaQuinzenal = nrDiaQuinzenal;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public BigDecimal getVlCotacaoMoeda() {
		return vlCotacaoMoeda;
	}

	public void setVlCotacaoMoeda(BigDecimal vlCotacaoMoeda) {
		this.vlCotacaoMoeda = vlCotacaoMoeda;
	}

	public boolean getBlFaturamentoManual() {
		return blFaturamentoManual;
	}

	public void setBlFaturamentoManual(boolean blFaturamentoManual) {
		this.blFaturamentoManual = blFaturamentoManual;
	}

}