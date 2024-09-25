package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.criadores;

import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GeneralidadeImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GenericoImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.CriadorEntidadeGenerica;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class CriadorGeneralidade implements CriadorEntidadeGenerica {

	@Override
	public TabelaPrecoParcela configuraEntidade(TabelaPrecoParcela tabelaPrecoParcela, ComponenteImportacao componente) {
		GeneralidadeImportacao generalidade = ((GenericoImportacao) componente).comoGeneralidade();
		Generalidade entidade = new Generalidade();
		entidade.setVlGeneralidade(generalidade.valor());
		entidade.setVlMinimo(generalidade.valorMinimo());
		entidade.setPsMinimo(generalidade.pesoMinimo());
		entidade.setTabelaPrecoParcela(tabelaPrecoParcela);
		tabelaPrecoParcela.setGeneralidade(entidade);
		return tabelaPrecoParcela;
	}

}
