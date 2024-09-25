package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public interface CriadorEntidadeGenerica {

	TabelaPrecoParcela configuraEntidade(TabelaPrecoParcela resultado, ComponenteImportacao componente);

}
