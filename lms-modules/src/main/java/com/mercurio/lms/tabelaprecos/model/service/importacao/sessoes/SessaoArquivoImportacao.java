package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.List;
import java.util.Map;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;

public interface SessaoArquivoImportacao {

	Map<TipoComponente, List<ComponenteImportacao>> resultadoImportacao();
}
