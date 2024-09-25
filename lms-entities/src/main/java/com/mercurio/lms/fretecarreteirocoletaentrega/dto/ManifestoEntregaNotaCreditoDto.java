package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.NotaCreditoDtoUtils;

public class ManifestoEntregaNotaCreditoDto {

	private String sgFilialOrigem;
	private Long nrDoctoServico;
	private String enderecoReal;
	private BigDecimal vlFrete = BigDecimal.ZERO;
	private BigDecimal vlMercadorias = BigDecimal.ZERO ;
	private Integer evento;
	private Long qtVolumes;
	private BigDecimal psReal= BigDecimal.ZERO;
	private BigDecimal psAforado = BigDecimal.ZERO;
	private BigDecimal psAferido = BigDecimal.ZERO;
	private String tpCalculoNotaCredito;
	private Boolean hasParcelaPv = Boolean.FALSE;
	private Boolean hasParcelaPf = Boolean.FALSE;
	private ManifestoEntregaControleCargaNotaCreditoDto manifestoEntrega;
	
	public ManifestoEntregaNotaCreditoDto(Map<String, Object> map, String tpCalculoNotaCredito, List<ParcelaTabelaCe> listParcelasCe, Set<String> enderecos) {
		sgFilialOrigem = (String) map.get("sgFilial");
		nrDoctoServico = (Long) map.get("nrDoctoServico");
		enderecoReal = (String) map.get("dsEnderecoEntregaReal");
		vlFrete = NotaCreditoDtoUtils.nvl((BigDecimal) map.get("vlTotalParcelas"));
		vlMercadorias = NotaCreditoDtoUtils.nvl((BigDecimal) map.get("vlMercadoria"));
		qtVolumes = (Long) map.get("countVolumes");
		psReal = NotaCreditoDtoUtils.nvl((BigDecimal) map.get("psReal"));
		psAforado = NotaCreditoDtoUtils.nvl((BigDecimal) map.get("psAforado"));
		psAferido = NotaCreditoDtoUtils.nvl((BigDecimal) map.get("psAferido"));
		this.tpCalculoNotaCredito = tpCalculoNotaCredito;
		evento = 1;
		
		if ("C1".equals(tpCalculoNotaCredito)) {
			if (enderecos.contains(enderecoReal)) {
				evento = 0;
			}
		}

		if (CollectionUtils.isNotEmpty(listParcelasCe)) {
			for (ParcelaTabelaCe parcelaTabelaCe : listParcelasCe) {
				if("PV".equals(parcelaTabelaCe.getTpParcela())){
					hasParcelaPv = Boolean.TRUE;
				} else if("PF".equals(parcelaTabelaCe.getTpParcela())) {
					hasParcelaPf = Boolean.TRUE;
				}
			}
		}
		
		enderecos.add(enderecoReal);
	}

	public ManifestoEntregaNotaCreditoDto(ManifestoEntregaControleCargaNotaCreditoDto dto) {
		this.manifestoEntrega = dto;
	}

	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}

	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public String getEnderecoReal() {
		return enderecoReal;
	}

	public void setEnderecoReal(String enderecoReal) {
		this.enderecoReal = enderecoReal;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public BigDecimal getVlMercadorias() {
		return vlMercadorias;
	}

	public void setVlMercadorias(BigDecimal vlMercadorias) {
		this.vlMercadorias = vlMercadorias;
	}

	public Integer getEvento() {
		return evento;
	}

	public void setEvento(Integer evento) {
		this.evento = evento;
	}

	public Long getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Long qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsReal() {
		return psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public BigDecimal getPsAforado() {
		return psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public BigDecimal getPsAferido() {
		return psAferido;
	}

	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}

	public String getTpCalculoNotaCredito() {
		return tpCalculoNotaCredito;
	}

	public void setTpCalculoNotaCredito(String tpCalculoNotaCredito) {
		this.tpCalculoNotaCredito = tpCalculoNotaCredito;
	}

	public Boolean getHasParcelaPv() {
		return hasParcelaPv;
	}

	public void setHasParcelaPv(Boolean hasParcelaPv) {
		this.hasParcelaPv = hasParcelaPv;
	}

	public Boolean getHasParcelaPf() {
		return hasParcelaPf;
	}

	public void setHasParcelaPf(Boolean hasParcelaPf) {
		this.hasParcelaPf = hasParcelaPf;
	}

	public ManifestoEntregaControleCargaNotaCreditoDto getManifestoEntrega() {
		return manifestoEntrega;
	}

	public void setManifestoEntrega(
			ManifestoEntregaControleCargaNotaCreditoDto manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}
	
	/*metodo de calculo*/
	public BigDecimal getVlPeso() {
		List<BigDecimal> values = Arrays.asList(nvl(psReal), nvl(psAforado), nvl(psAferido));
		Collections.sort(values);
		return values.get(values.size() - 1);
	}
	
	private BigDecimal nvl(BigDecimal value) {
		if (value == null) {
			return BigDecimal.valueOf(1);
		}
		return value;
	}

}
