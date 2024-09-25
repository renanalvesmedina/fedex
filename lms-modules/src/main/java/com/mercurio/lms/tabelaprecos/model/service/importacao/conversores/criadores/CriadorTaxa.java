package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.criadores;

import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GenericoImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TaxaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.CriadorEntidadeGenerica;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class CriadorTaxa implements CriadorEntidadeGenerica {

	@Override
	public TabelaPrecoParcela configuraEntidade(TabelaPrecoParcela tabelaPrecoParcela, ComponenteImportacao componente) {
		TaxaImportacao taxa = ((GenericoImportacao) componente).comoTaxa();
		ValorTaxa entidade = new ValorTaxa();
		entidade.setVlTaxa(taxa.valor());
		entidade.setVlExcedente(taxa.valorExcedente());
		entidade.setPsTaxado(taxa.pesoTaxado());
		entidade.setTabelaPrecoParcela(tabelaPrecoParcela);
		tabelaPrecoParcela.setValorTaxa(entidade);
		return tabelaPrecoParcela;
	}

}
