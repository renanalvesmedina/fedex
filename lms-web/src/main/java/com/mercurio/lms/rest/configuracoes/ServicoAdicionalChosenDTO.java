package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseDTO;

public class ServicoAdicionalChosenDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idServicoAdicional;
	private String dsServicoAdicional;

	public ServicoAdicionalChosenDTO() {
	}
	
	public ServicoAdicionalChosenDTO(Long idServicoAdicional, String dsServicoAdicional) {
		this.idServicoAdicional = idServicoAdicional;
		this.dsServicoAdicional = dsServicoAdicional;
	}

	public ServicoAdicionalChosenDTO(Long idServicoAdicional, VarcharI18n dsServicoAdicional) {
		this.idServicoAdicional = idServicoAdicional;
		if (dsServicoAdicional != null) this.dsServicoAdicional = dsServicoAdicional.toString();
	}

	public Long getIdServicoAdicional() {
		return idServicoAdicional;
	}
	
	public String getDsServicoAdicional() {
		return dsServicoAdicional;
	}
	
	public void setIdServicoAdicional(Long idServicoAdicional) {
		this.idServicoAdicional = idServicoAdicional;
	}
	
	public void setDsServicoAdicional(String dsServicoAdicional) {
		this.dsServicoAdicional = dsServicoAdicional;
	}

}
