package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.NotaCreditoDtoUtils;

public class ManifestoColetaNotaCreditoDto {

	private String sgFilial;
	private Long nrPedidoColeta;
	private String enderecoReal;
	private Integer evento;
	private Integer qtVolumes;
	private BigDecimal psTotalVerificado = new BigDecimal(0);
	private BigDecimal psTotalAforadoVerificado = new BigDecimal(0);

	private ManifestoColetaControleCargaNotaCreditoDto manifestoColeta;

	public ManifestoColetaNotaCreditoDto(Map<String, Object> map, ManifestoColetaControleCargaNotaCreditoDto manifestocoleta, String tpCalculoNotaCredito, List<String> enderecos) {
		
		this.sgFilial = (String) map.get("sgFilial"); 
		this.nrPedidoColeta = (Long) map.get("nrPedidoColeta");
		this.evento = 1;

		if ( map.containsKey("enderecoReal") ) {
			this.enderecoReal = (String) map.get("enderecoReal");
		}
		
		if ("C1".equals(tpCalculoNotaCredito)) {
			if (enderecos.contains(enderecoReal)) {
				this.evento = 0;
			}
		}

		this.qtVolumes = (Integer) map.get("qtVolumes");
		this.manifestoColeta = manifestocoleta;
		this.psTotalVerificado = (BigDecimal) map.get("psTotalVerificado");
		this.psTotalAforadoVerificado = (BigDecimal) map.get("psTotalAforadoVerificado");
		
		if ( this.enderecoReal != null) {
			enderecos.add(this.enderecoReal);
		}
	}

	public ManifestoColetaNotaCreditoDto(ManifestoColetaControleCargaNotaCreditoDto dto) {
		this.manifestoColeta = dto;
	}

	/*metodo de calculo*/
	public BigDecimal getVlPeso() {
		BigDecimal result = new BigDecimal(0);
		result = NotaCreditoDtoUtils.nvl(psTotalVerificado).max(NotaCreditoDtoUtils.nvl(psTotalAforadoVerificado));
		return result;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Long getNrPedidoColeta() {
		return nrPedidoColeta;
	}

	public void setNrPedidoColeta(Long nrPedidoColeta) {
		this.nrPedidoColeta = nrPedidoColeta;
	}

	public String getEnderecoReal() {
		return enderecoReal;
	}

	public void setEnderecoReal(String enderecoReal) {
		this.enderecoReal = enderecoReal;
	}

	public Integer getEvento() {
		return evento;
	}

	public void setEvento(Integer evento) {
		this.evento = evento;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsTotalVerificado() {
		return psTotalVerificado;
	}

	public void setPsTotalVerificado(BigDecimal psTotalVerificado) {
		this.psTotalVerificado = psTotalVerificado;
	}

	public BigDecimal getPsTotalAforadoVerificado() {
		return psTotalAforadoVerificado;
	}

	public void setPsTotalAforadoVerificado(BigDecimal psTotalAforadoVerificado) {
		this.psTotalAforadoVerificado = psTotalAforadoVerificado;
	}

	public ManifestoColetaControleCargaNotaCreditoDto getManifestoColeta() {
		return manifestoColeta;
	}

	public void setManifestoColeta(ManifestoColetaControleCargaNotaCreditoDto manifestoColeta) {
		this.manifestoColeta = manifestoColeta;
	}

}
