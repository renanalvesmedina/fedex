package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseDTO;


public class ProdutoEspecificoDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long idProdutoEspecifico;
	private String dsProdutoEspecifico;
    private Short nrTarifaEspecifica;
	
	public ProdutoEspecificoDTO() {
		
	}

	public ProdutoEspecificoDTO(Long idProdutoEspecifico, String dsProdutoEspecifico, Short nrTarifaEspecifica) {
		super();
		this.idProdutoEspecifico = idProdutoEspecifico;
		this.dsProdutoEspecifico = dsProdutoEspecifico;
		this.nrTarifaEspecifica = nrTarifaEspecifica;
	}

	public Long getIdProdutoEspecifico() {
		return idProdutoEspecifico;
	}

	public void setIdProdutoEspecifico(Long idProdutoEspecifico) {
		this.idProdutoEspecifico = idProdutoEspecifico;
	}

	public String getDsProdutoEspecifico() {
		return dsProdutoEspecifico;
	}

	public void setDsProdutoEspecifico(String dsProdutoEspecifico) {
		this.dsProdutoEspecifico = dsProdutoEspecifico;
	}

	public Short getNrTarifaEspecifica() {
		return nrTarifaEspecifica;
	}

	public void setNrTarifaEspecifica(Short nrTarifaEspecifica) {
		this.nrTarifaEspecifica = nrTarifaEspecifica;
	}
	
}
