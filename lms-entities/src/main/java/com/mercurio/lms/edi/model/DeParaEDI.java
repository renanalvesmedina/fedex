package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeParaEDI implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idDeParaEDI;
	
	private String nmDeParaEDI;

	public Long getIdDeParaEDI() {
		return idDeParaEDI;
	}

	public void setIdDeParaEDI(Long idDeParaEDI) {
		this.idDeParaEDI = idDeParaEDI;
	}

	public String getNmDeParaEDI() {
		return nmDeParaEDI;
	}

	public void setNmDeParaEDI(String nmDeParaEDI) {
		this.nmDeParaEDI = nmDeParaEDI;
	}
		
	public Map<String, Object> getMapped(){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		/*idDeParaEDI*/
		param.put("idDeParaEDI", this.idDeParaEDI);
		/*nmDeParaEDI*/
		param.put("nmDeParaEDI", this.nmDeParaEDI);
		
		return param;
	}
	
}
