package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeParaDetalheEDI implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idDeParaEDIDetalhe;
	
	private String de;
	
	private String para;

	private DeParaEDI deParaEDI;

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
		
	public Map<String, Object> getMapped(){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		/*idDeParaEDIDetalhe*/
		param.put("idDeParaEDIDetalhe", this.idDeParaEDIDetalhe);
		/*de*/
		param.put("de", this.de);
		/*para*/
		param.put("para", this.para);
		
		/*DePara*/
		if(this.deParaEDI != null){
			param.put("idDeParaEDI", this.deParaEDI.getIdDeParaEDI());
			param.put("nmDeParaEDI", this.deParaEDI.getNmDeParaEDI());
		}
		
		return param;
	}

	public Long getIdDeParaEDIDetalhe() {
		return idDeParaEDIDetalhe;
	}

	public void setIdDeParaEDIDetalhe(Long idDeParaEDIDetalhe) {
		this.idDeParaEDIDetalhe = idDeParaEDIDetalhe;
	}

	public DeParaEDI getDeParaEDI() {
		return deParaEDI;
	}

	public void setDeParaEDI(DeParaEDI deParaEDI) {
		this.deParaEDI = deParaEDI;
	}
	
}
