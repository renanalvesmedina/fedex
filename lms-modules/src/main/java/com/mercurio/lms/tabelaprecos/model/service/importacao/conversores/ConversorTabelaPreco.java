package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ComponentesTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ComponentesTabelaPreco.ItemComponenteImportacao;

public class ConversorTabelaPreco {

	private List<TabelaPrecoParcela> resultadoConversorTabelaPreco = new ArrayList<TabelaPrecoParcela>();
	private List<String> errosConversorTabela = new ArrayList<String>();

	private BuscadorDependencia buscador;
	private TabelaPreco tabelaPreco;
	private FornecedoresRotaTarifa fornecedoresChave;


	public ConversorTabelaPreco(BuscadorDependencia buscador, FornecedoresRotaTarifa fornecedoresChave, TabelaPreco tabelaPreco) {
		this.buscador = buscador;
		this.fornecedoresChave = fornecedoresChave;
		this.tabelaPreco = tabelaPreco;
	
	}

	public void converter(ComponentesTabelaPreco componentes) {
		ConfiguradoresTabelaPrecoParcela configuradores = new ConfiguradoresTabelaPrecoParcela(this.buscador, this.tabelaPreco, this.fornecedoresChave);

		while (componentes.hasNext()) {
			ItemComponenteImportacao item = componentes.next();
			//buscar o tipo parcela
			ConfiguradorTabelaPrecoParcela configurador = configuradores.para(item.tipo());
			//configurar a entidade tabelaPrecoParcela
			List<TabelaPrecoParcela> itens = configurador.configura(item.itens());
			if (configurador.ocorreramErros()) {
				this.errosConversorTabela.addAll(configurador.erros());
				continue;
			}
			resultadoConversorTabelaPreco.addAll(itens);
		}
		this.fornecedoresChave.persisteTodosCaches();
	}

	public List<TabelaPrecoParcela> resultado(){
		return Collections.unmodifiableList(resultadoConversorTabelaPreco);
	}

	public List<String> erros(){
		return Collections.unmodifiableList(errosConversorTabela);
	}

	public boolean ocorreramErros(){
		return CollectionUtils.isNotEmpty(errosConversorTabela);
	}
}
