package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseDTO;

public class LocalidadeEspecialSuggestDTO extends BaseDTO{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dsLocalidade;
	
	public LocalidadeEspecialSuggestDTO(){}
	
	public LocalidadeEspecialSuggestDTO(Long idLocalidadeEspecial, String dsLocalidade) {
		super();
		setId(idLocalidadeEspecial);
		this.dsLocalidade = dsLocalidade;
		
	}
	
	public String getDsLocalidade() {
		return dsLocalidade;
	}
	public void setDsLocalidade(String dsLocalidade) {
		this.dsLocalidade = dsLocalidade;
	}
}
