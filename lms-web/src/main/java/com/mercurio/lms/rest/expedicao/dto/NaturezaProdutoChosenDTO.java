package com.mercurio.lms.rest.expedicao.dto;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.expedicao.model.NaturezaProduto;

/**
 * LMS-6885 - DTO para diretiva "chosen" de {@link NaturezaProduto}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class NaturezaProdutoChosenDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String dsNaturezaProduto;

	public String getDsNaturezaProduto() {
		return dsNaturezaProduto;
	}

	public void setDsNaturezaProduto(String dsNaturezaProduto) {
		this.dsNaturezaProduto = dsNaturezaProduto;
	}

}
