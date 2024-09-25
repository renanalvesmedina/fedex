package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.util.List;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;

public class ProgramacaoColetasVeiculosFilterDTO extends BaseFilterDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RotaColetaEntregaSuggestDTO rotaColetaEntregaFiltroColetas;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;
	private MeioTransporteSuggestDTO meioTransporte;
	private FilialSuggestDTO filial;

	private Long idPedidoColeta;
	private Long idMeioTransporte;

	private List<Long> idsPedido;

	public RotaColetaEntregaSuggestDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}
	public void setRotaColetaEntrega(RotaColetaEntregaSuggestDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}
	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}
	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}
	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}
	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}
	public Long getIdMeioTransporte() {
		return idMeioTransporte;
	}
	public void setIdMeioTransporte(Long idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}
	public List<Long> getIdsPedido() {
		return idsPedido;
	}
	public void setIdsPedido(List<Long> idsPedido) {
		this.idsPedido = idsPedido;
	}
	public RotaColetaEntregaSuggestDTO getRotaColetaEntregaFiltroColetas() {
		return rotaColetaEntregaFiltroColetas;
	}
	public void setRotaColetaEntregaFiltroColetas(RotaColetaEntregaSuggestDTO rotaColetaEntregaFiltroColetas) {
		this.rotaColetaEntregaFiltroColetas = rotaColetaEntregaFiltroColetas;
	}
	public Long getIdRotaColetaEntregaSuggest() {
		if (this.rotaColetaEntrega  != null) {
			return this.rotaColetaEntrega.getIdRotaColetaEntrega();
		}else{ return null; }
	}
	public Long getIdMeioTransporteSuggest() {
		if (this.meioTransporte  != null) {
			return this.meioTransporte.getIdMeioTransporte();
		} else{ return null; }
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}


}
