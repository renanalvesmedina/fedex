package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaRateioFreteCarreteiroCE;

public class ParametrosCalculoRateio {
	
	private List<Map<String, Object>> docts;
	private Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio;	
	private NotaCreditoParcela notaCreditoParcela;
	private String tpParcela;
	private BigDecimal quilo ; 
	private Long idCliente;
	private List<Long> parcelasEspeciais;
	
	
	
	public ParametrosCalculoRateio(List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio,
			NotaCreditoParcela notaCreditoParcela, String tpParcela, BigDecimal quilo, Long idCliente, List<Long> parcelasEspeciais) {
		super();
		this.docts = docts;
		this.rateio = rateio;
		this.notaCreditoParcela = notaCreditoParcela;
		this.tpParcela = tpParcela;
		this.quilo = quilo;
		this.idCliente = idCliente;
		this.parcelasEspeciais = parcelasEspeciais;
	}
	
	public List<Map<String, Object>> getDocts() {
		return docts;
	}
	public void setDocts(List<Map<String, Object>> docts) {
		this.docts = docts;
	}
	public Map<Long, List<ParcelaRateioFreteCarreteiroCE>> getRateio() {
		return rateio;
	}
	public void setRateio(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		this.rateio = rateio;
	}
	public NotaCreditoParcela getNotaCreditoParcela() {
		return notaCreditoParcela;
	}
	public void setNotaCreditoParcela(NotaCreditoParcela notaCreditoParcela) {
		this.notaCreditoParcela = notaCreditoParcela;
	}
	public String getTpParcela() {
		return tpParcela;
	}
	public void setTpParcela(String tpParcela) {
		this.tpParcela = tpParcela;
	}
	public BigDecimal getQuilo() {
		return quilo;
	}
	public void setQuilo(BigDecimal quilo) {
		this.quilo = quilo;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public List<Long> getParcelasEspeciais() {
		return parcelasEspeciais;
	}
	public void setParcelasEspeciais(List<Long> parcelasEspeciais) {
		this.parcelasEspeciais = parcelasEspeciais;
	}

	@Override
	public String toString() {
		return "ParametrosCalculoRateio [docts=" + docts + ", rateio=" + rateio + ", notaCreditoParcela=" + notaCreditoParcela + ", tpParcela="
				+ tpParcela + ", quilo=" + quilo + ", idCliente=" + idCliente + ", parcelasEspeciais=" + parcelasEspeciais + "]";
	}
	
}
