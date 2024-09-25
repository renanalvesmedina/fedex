package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseDTO;

public class ServicoChosenDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idServico;
	private String dsServico;

	public ServicoChosenDTO() {
	}
	
	public ServicoChosenDTO(Long idServico, String dsServico) {
		this.idServico = idServico;
		this.dsServico = dsServico;
	}

	public ServicoChosenDTO(Long idServico, VarcharI18n dsServico) {
		this.idServico = idServico;
		if (dsServico != null) this.dsServico = dsServico.toString();
	}

	public Long getIdServico() {
		return idServico;
	}

	public String getDsServico() {
		return dsServico;
	}
	
	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}
	
	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}

}
