package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.criadores;

import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GenericoImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ServicoAdicionalImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.CriadorEntidadeGenerica;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class CriadorServicoAdicional implements CriadorEntidadeGenerica {

	@Override
	public TabelaPrecoParcela configuraEntidade(TabelaPrecoParcela tabelaPrecoParcela, ComponenteImportacao componente) {
		ServicoAdicionalImportacao servicoAdicional = ((GenericoImportacao) componente).comoServicoAdicional();
		ValorServicoAdicional entidade = new ValorServicoAdicional();
		entidade.setVlServico(servicoAdicional.valor());
		entidade.setVlMinimo(servicoAdicional.valorMinimo());
		entidade.setTabelaPrecoParcela(tabelaPrecoParcela);
		tabelaPrecoParcela.setValorServicoAdicional(entidade);
		return tabelaPrecoParcela;
	}

}
