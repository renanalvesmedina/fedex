package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ProgramacaoColetasVeiculosRetornoExecutarDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> idsPedidoColeta;
	private Long idControleCarga;
	private Long idOcorrenciaColeta;
	private DomainValue blIneficienciaFrota;
	private String dsDescricao;
	private DateTime dtHoraOcorrencia;
	private Long idMeioTransporte;
	
	public DateTime getDtHoraOcorrencia() {
		return dtHoraOcorrencia;
	}
	public void setDtHoraOcorrencia(DateTime dtHoraOcorrencia) {
		this.dtHoraOcorrencia = dtHoraOcorrencia;
	}

	public Long getIdControleCarga() {
		return idControleCarga;
	}
	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}
	public Long getIdOcorrenciaColeta() {
		return idOcorrenciaColeta;
	}
	public void setIdOcorrenciaColeta(Long idOcorrenciaColeta) {
		this.idOcorrenciaColeta = idOcorrenciaColeta;
	}
	public DomainValue getBlIneficienciaFrota() {
		return blIneficienciaFrota;
	}
	public void setBlIneficienciaFrota(DomainValue blIneficienciaFrota) {
		this.blIneficienciaFrota = blIneficienciaFrota;
	}
	public String getDsDescricao() {
		return dsDescricao;
	}
	public void setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
	}
	public List<String> getIdsPedidoColeta() {
		return idsPedidoColeta;
	}
	public void setIdsPedidoColeta(List<String> idsPedidoColeta) {
		this.idsPedidoColeta = idsPedidoColeta;
	}
	public Long getIdMeioTransporte() {
		return idMeioTransporte;
	}
	public void setIdMeioTransporte(Long idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}
	

}
