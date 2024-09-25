package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.GrupoPrecoFreteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.PrecoFreteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class ConfiguradorPrecoFrete extends ConfiguradorTabelaPrecoParcela {

	private FornecedoresRotaTarifa fornecedoresChave;

	public ConfiguradorPrecoFrete(Map<String, Object> dependencias) {
		super(dependencias);
		this.fornecedoresChave = (FornecedoresRotaTarifa) dependencias.get(ConfiguradoresTabelaPrecoParcela.FORNECEDORES_ROTAVSTARIFA);
	}
	
	@Override
	public List<TabelaPrecoParcela> configura(List<ComponenteImportacao> componentes) {

		List<TabelaPrecoParcela> retorno = new ArrayList<TabelaPrecoParcela>();

		for(ComponenteImportacao componente : componentes){
			TabelaPrecoParcela tabelaPrecoParcela = new TabelaPrecoParcela();

			tabelaPrecoParcela.setTabelaPreco(this.tabelaPreco);
			tabelaPrecoParcela.setParcelaPreco(this.obtemParcelaPreco(componente));
			GrupoPrecoFreteImportacao grupoPrecoFreteImportacao = (GrupoPrecoFreteImportacao) componente;

			incluiGrupoPrecoFrete(tabelaPrecoParcela, grupoPrecoFreteImportacao);
			retorno.add(tabelaPrecoParcela);
		}

		return retorno;
	}

	private void incluiGrupoPrecoFrete(TabelaPrecoParcela tabelaPrecoParcela, GrupoPrecoFreteImportacao grupoPrecoFreteImportacao) {
		for (PrecoFreteImportacao precoFreteImportacao : grupoPrecoFreteImportacao.valores()) {
			try {
				PrecoFrete precoFrete = criarPrecoFrete(tabelaPrecoParcela, precoFreteImportacao);
				if (precoFrete == null) {
					continue;
				}
				tabelaPrecoParcela.incluiPrecoFrete(precoFrete);
			} catch (ImportacaoException ie) {
				this.capturaErro(ie);
			}
		}
	}

	private PrecoFrete criarPrecoFrete(TabelaPrecoParcela tabelaPrecoParcela, PrecoFreteImportacao precoFreteImportacao) {
		ChaveProgressao chave = precoFreteImportacao.chave();
		FornecedorChave fornecedor = this.fornecedoresChave.fornecedorPara(chave.tipo());
		if (fornecedor.chaveComProblema(chave)) {
			return null;
		}
		PrecoFrete precoFrete = new PrecoFrete();
		precoFrete.setVlPrecoFrete(precoFreteImportacao.valorFrete());
		precoFrete.setPesoMinimo(precoFreteImportacao.pesoMinimo());
		precoFrete.setTabelaPrecoParcela(tabelaPrecoParcela);
		

		TradutorChaveProgressao tradutor = fornecedor.forneceChave(chave);
		precoFrete.setTarifaPreco(tradutor.tarifaPreco());
		precoFrete.setRotaPreco(tradutor.rotaPreco());
		return precoFrete;
	}

	private ParcelaPreco obtemParcelaPreco(ComponenteImportacao componente) {
		if (componente == null) {
			return null;
		}
		String tagArquivo = componente.valorTagPrincipal();
		TagTabelaPreco tag = buscador.buscarTagTabelaPreco(tagArquivo);
		return tag.getParcelaPreco();
	}



}
