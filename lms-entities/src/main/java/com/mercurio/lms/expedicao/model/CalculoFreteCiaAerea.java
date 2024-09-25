package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.TarifaSpot;

public class CalculoFreteCiaAerea extends CalculoFrete {
	private static final long serialVersionUID = 1L;
	
	private static long idCalculo=0;
	private Long idCalculoFrete;
	private Long idCiaAerea;
	private Long idExpedidor;
	private Long idAeroportoOrigem;
	private Long idAeroportoDestino;
	private Boolean blTarifaSpot;
	private BigDecimal psQuiloExcedente;
	private TarifaSpot tarifaSpot;
	private YearMonthDay dtEmissaoAwb;
	private String tpServicoAwb;

	public CalculoFreteCiaAerea() {
		this.idCalculoFrete = Long.valueOf(idCalculo--);
	}

	public Long getIdCalculoFrete() {
		return idCalculoFrete;
	}

	public Long getIdAeroportoDestino() {
		return idAeroportoDestino;
	}

	public void setIdAeroportoDestino(Long idAeroportoDestino) {
		this.idAeroportoDestino = idAeroportoDestino;
	}

	public Long getIdAeroportoOrigem() {
		return idAeroportoOrigem;
	}

	public void setIdAeroportoOrigem(Long idAeroportoOrigem) {
		this.idAeroportoOrigem = idAeroportoOrigem;
	}

	public Long getIdCiaAerea() {
		return idCiaAerea;
	}

	public void setIdCiaAerea(Long idCiaAerea) {
		this.idCiaAerea = idCiaAerea;
	}

	public Long getIdExpedidor() {
		return idExpedidor;
	}

	public void setIdExpedidor(Long idExpedidor) {
		this.idExpedidor = idExpedidor;
	}

	public Boolean getBlTarifaSpot() {
		return blTarifaSpot;
	}

	public void setBlTarifaSpot(Boolean blTarifaSpot) {
		this.blTarifaSpot = blTarifaSpot;
	}

	public TarifaSpot getTarifaSpot() {
		return tarifaSpot;
	}

	public void setTarifaSpot(TarifaSpot tarifaSpot) {
		this.tarifaSpot = tarifaSpot;
	}

	public YearMonthDay getDtEmissaoAwb() {
		return dtEmissaoAwb;
	}

	public void setDtEmissaoAwb(YearMonthDay dtEmissaoAwb) {
		this.dtEmissaoAwb = dtEmissaoAwb;
	}

	public String getTpServicoAwb() {
		return tpServicoAwb;
	}

	public void setTpServicoAwb(String tpServicoAwb) {
		this.tpServicoAwb = tpServicoAwb;
	}

	public BigDecimal getPsQuiloExcedente() {
		return psQuiloExcedente;
	}

	public void setPsQuiloExcedente(BigDecimal psQuiloExcedente) {
		this.psQuiloExcedente = psQuiloExcedente;
	}

}
