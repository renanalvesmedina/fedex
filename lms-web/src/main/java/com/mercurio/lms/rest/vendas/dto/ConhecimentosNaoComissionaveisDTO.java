package com.mercurio.lms.rest.vendas.dto;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ConhecimentosNaoComissionaveisDTO extends BaseDTO {

	private static final long serialVersionUID = 2215044079105669635L;
	
	private Long idExecutivo;
	private YearMonthDay dataCompetencia;
	private List<Long> naoComissionaveisList;
	private List<Long> comissionaveisList;
	
	public Long getIdExecutivo() {
		return idExecutivo;
	}

	public void setIdExecutivo(Long idExecutivo) {
		this.idExecutivo = idExecutivo;
	}

	public YearMonthDay getDataCompetencia() {
		return dataCompetencia;
	}
	
	public void setDataCompetencia(YearMonthDay dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}
	
	public List<Long> getNaoComissionaveisList() {
		return naoComissionaveisList;
	}
	
	public void setNaoComissionaveisList(List<Long> naoComissionaveisList) {
		this.naoComissionaveisList = naoComissionaveisList;
	}
	
	public List<Long> getComissionaveisList() {
		return comissionaveisList;
	}

	public void setComissionaveisList(List<Long> comissionaveisList) {
		this.comissionaveisList = comissionaveisList;
	}

}
