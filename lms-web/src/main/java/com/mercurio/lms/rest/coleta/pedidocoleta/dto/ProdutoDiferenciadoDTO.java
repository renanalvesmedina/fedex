package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class ProdutoDiferenciadoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idPedidoColetaProduto;
	private Long idProduto;
	private String dsProduto;

	public Long getIdPedidoColetaProduto() {
		return idPedidoColetaProduto;
	}

	public void setIdPedidoColetaProduto(Long idPedidoColetaProduto) {
		this.idPedidoColetaProduto = idPedidoColetaProduto;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getDsProduto() {
		return dsProduto;
	}

	public void setDsProduto(String dsProduto) {
		this.dsProduto = dsProduto;
	}

}
