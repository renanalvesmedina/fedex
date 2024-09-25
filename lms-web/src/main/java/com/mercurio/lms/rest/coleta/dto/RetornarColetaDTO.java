package com.mercurio.lms.rest.coleta.dto;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class RetornarColetaDTO extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DomainValue blIneficienciaFrota;
	private Long idOcorrenciaColeta;
	private List<String> idsPedido;
	private List<Map<String, Long>> idsPedidoAndAwb;
	private Long idMeioTransporte;
	private DateTime dtHrOcorrencia;
	
	public DomainValue getBlIneficienciaFrota() {
		return blIneficienciaFrota;
	}
	public void setBlIneficienciaFrota(DomainValue blIneficienciaFrota) {
		this.blIneficienciaFrota = blIneficienciaFrota;
	}
	public Long getIdOcorrenciaColeta() {
		return idOcorrenciaColeta;
	}
	public void setIdOcorrenciaColeta(Long idOcorrenciaColeta) {
		this.idOcorrenciaColeta = idOcorrenciaColeta;
	}
	public List<String> getIdsPedido() {
		return idsPedido;
	}
	public void setIdsPedido(List<String> idsPedido) {
		this.idsPedido = idsPedido;
	}
	public Long getIdMeioTransporte() {
		return idMeioTransporte;
	}
	public void setIdMeioTransporte(Long idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}
	public DateTime getDtHrOcorrencia() {
		return dtHrOcorrencia;
	}
	public void setDtHrOcorrencia(DateTime dtHrOcorrencia) {
		this.dtHrOcorrencia = dtHrOcorrencia;
	}
	public List<Map<String, Long>> getIdsPedidoAndAwb() {
		return idsPedidoAndAwb;
	}
	public void setIdsPedidoAndAwb(List<Map<String, Long>> idsPedidoAndAwb) {
		this.idsPedidoAndAwb = idsPedidoAndAwb;
	}
	
	
}