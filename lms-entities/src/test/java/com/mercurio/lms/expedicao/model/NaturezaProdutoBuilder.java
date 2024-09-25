package com.mercurio.lms.expedicao.model;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.expedicao.model.NaturezaProduto;

public class NaturezaProdutoBuilder {

	public static NaturezaProdutoBuilder newNaturezaProduto() {
		return new NaturezaProdutoBuilder();
	}

	private NaturezaProduto naturezaProduto;

	private NaturezaProdutoBuilder() {
		naturezaProduto = new NaturezaProduto();
	}

	public NaturezaProdutoBuilder id(long id) {
		naturezaProduto.setIdNaturezaProduto(id);
		return this;
	}

	public NaturezaProdutoBuilder descricao(String descricao) {
		naturezaProduto.setDsNaturezaProduto(new VarcharI18n(descricao));
		return this;
	}
	
	public NaturezaProduto build() {
		return naturezaProduto;
	}

	public static NaturezaProduto CALCADOS() {
		return buildNaturezaProduto(1L, "Calçados");
	}

	public static NaturezaProduto AUTO_PECAS() {
		return buildNaturezaProduto(2L, "Auto-Peças");
	}

	public static NaturezaProduto COSMETICOS() {
		return buildNaturezaProduto(3L, "Cosméticos");
	}

	public static NaturezaProduto ELETRO_ELETRONICOS() {
		return buildNaturezaProduto(9L, "Eletro/Eletrônico");
	}

	public static NaturezaProduto CELULARES() {
		return buildNaturezaProduto(16L, "Celulares");
	}

	public static NaturezaProduto DIVERSOS() {
		return buildNaturezaProduto(65L, "Diversos");
	}

	private static NaturezaProduto buildNaturezaProduto(long id, String descricao) {
		return newNaturezaProduto()
				.id(id)
				.descricao(descricao)
				.build();
	}

}
