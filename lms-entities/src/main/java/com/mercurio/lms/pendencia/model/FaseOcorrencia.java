package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.sim.model.FaseProcesso;

public class FaseOcorrencia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idFaseOcorrencia;
	private FaseProcesso faseProcesso;
	private OcorrenciaPendencia ocorrenciaPendencia;
	
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		bean.put("idFaseOcorrencia", this.getIdFaseOcorrencia());
		bean.put("faseProcesso", this.getFaseProcesso());
		bean.put("ocorrenciaPendencia", this.getOcorrenciaPendencia());
		
		return bean;
	}
	
	public Long getIdFaseOcorrencia() {
		return idFaseOcorrencia;
	}
	
	public void setIdFaseOcorrencia(Long idFaseOcorrencia) {
		this.idFaseOcorrencia = idFaseOcorrencia;
	}
	
	public FaseProcesso getFaseProcesso() {
		return faseProcesso;
	}
	
	public void setFaseProcesso(FaseProcesso faseProcesso) {
		this.faseProcesso = faseProcesso;
	}
	
	public OcorrenciaPendencia getOcorrenciaPendencia() {
		return ocorrenciaPendencia;
	}
	
	public void setOcorrenciaPendencia(OcorrenciaPendencia ocorrenciaPendencia) {
		this.ocorrenciaPendencia = ocorrenciaPendencia;
	}
	
	
}
