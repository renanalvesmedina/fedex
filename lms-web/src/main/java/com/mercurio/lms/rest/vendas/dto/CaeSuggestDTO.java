package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class CaeSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private String nrCae;
	private String nmFilial;
	private Long idFilial;

	public CaeSuggestDTO() {
	}

	public CaeSuggestDTO(String nrCae, String nmFilial, Long idFilial) {
		super();
		this.setId(Long.valueOf(nrCae));
		this.nrCae = nrCae;
		this.nmFilial = nmFilial;
		this.idFilial = idFilial;
	}

	public String getNrCae() {
		return nrCae;
	}

	public void setNrCae(String nrCae) {
		this.nrCae = nrCae;
	}

	public String getNmFilial() {
		return nmFilial;
	}

	public void setNmFilial(String nmFilial) {
		this.nmFilial = nmFilial;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

}
