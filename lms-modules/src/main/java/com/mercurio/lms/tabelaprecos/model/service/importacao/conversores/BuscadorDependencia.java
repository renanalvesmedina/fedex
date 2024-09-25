package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.UnidadeMedida;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.tabelaprecos.model.service.TagTabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.UnidadeMedidaService;

public class BuscadorDependencia {

	private TagTabelaPrecoService tagTabelaPrecoService;
	private UnidadeMedidaService unidadeMedidaService;
	private ProdutoEspecificoService produtoEspecificoService;

	public BuscadorDependencia(TagTabelaPrecoService tagTabelaPrecoService, UnidadeMedidaService unidadeMedidaService, ProdutoEspecificoService produtoEspecificoService) {
		this.tagTabelaPrecoService = tagTabelaPrecoService;
		this.unidadeMedidaService = unidadeMedidaService;
		this.produtoEspecificoService = produtoEspecificoService;
	}

	public TagTabelaPreco buscarTagTabelaPreco(String tag) {
		return this.tagTabelaPrecoService.findByTag(tag);
	}

	public UnidadeMedida buscarUnidadeMedida(Long idUnidadeMedida) {
		return this.unidadeMedidaService.findById(idUnidadeMedida);
	}

	public ProdutoEspecifico buscarProdutoEspecifico(Short nrTarifa) {
		return this.produtoEspecificoService.findByNrTarifa(nrTarifa);
	}
	
}
