package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public abstract class ConfiguradorTabelaPrecoParcela {

	protected BuscadorDependencia buscador;
	protected TabelaPreco tabelaPreco;
	private List<String> errosConfiguradorTabela;

	public ConfiguradorTabelaPrecoParcela(Map<String, Object> dependencias){
		this.buscador = (BuscadorDependencia) dependencias.get(ConfiguradoresTabelaPrecoParcela.BUSCADOR_DEPENDENCIAS);
		this.tabelaPreco = (TabelaPreco) dependencias.get(ConfiguradoresTabelaPrecoParcela.TABELA_PRECO);
	}
	
	public abstract List<TabelaPrecoParcela> configura(List<ComponenteImportacao> componentes);
	
	public boolean ocorreramErros() {
		return CollectionUtils.isNotEmpty(this.errosConfiguradorTabela);
	}
	
	public List<String> erros() {
		return Collections.unmodifiableList(this.errosConfiguradorTabela);
	}

	protected void capturaErro(ImportacaoException ie) {
		if (this.errosConfiguradorTabela == null) {
			this.errosConfiguradorTabela = new ArrayList<String>();
		}
		this.errosConfiguradorTabela.addAll(ie.mensagens());
		
	}


}
