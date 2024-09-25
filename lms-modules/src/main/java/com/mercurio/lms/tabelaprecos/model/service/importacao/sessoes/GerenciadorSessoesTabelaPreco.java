package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;

public class GerenciadorSessoesTabelaPreco {

	private List<SessaoArquivoImportacao> sessoesArquivo = null;

	private boolean existeSessaoAtiva = false;

	public SessaoTabela abreSessaoTabela() {
		validaSessaoAtiva();
		this.iniciaSessoesArquivos();

		SessaoTabela sessaoTabela = new SessaoTabela();
		this.sessoesArquivo.add(sessaoTabela);
		this.existeSessaoAtiva = true;
		return sessaoTabela;
	}

	private void validaSessaoAtiva() {
		if (this.existeSessaoAtiva) {
			throw new IllegalStateException("Uma outra sessão já está aberta.");
		}
	}

	private void iniciaSessoesArquivos() {
		if (this.sessoesArquivo == null) {
			this.sessoesArquivo = new ArrayList<SessaoArquivoImportacao>();
		}
	}

	public void fecharSessaoAtiva() {
		this.existeSessaoAtiva = false;
	}

	public boolean temSessaoAberta() {
		return existeSessaoAtiva;
	}

	public Map<TipoComponente, List<ComponenteImportacao>> componentes() {
		if (CollectionUtils.isEmpty(this.sessoesArquivo)) {
			return Collections.emptyMap();
		}
		Map<TipoComponente, List<ComponenteImportacao>> resultado = new HashMap<ComponenteImportacao.TipoComponente, List<ComponenteImportacao>>();
		for (SessaoArquivoImportacao sessao : sessoesArquivo) {
			resultado.putAll(sessao.resultadoImportacao());
		}
		return Collections.unmodifiableMap(resultado);
	}

	public SessaoValoresFixos abreSessaoFixos() {
		this.validaSessaoAtiva();
		validaSessaoAtiva();
		this.iniciaSessoesArquivos();

		SessaoValoresFixos sessaoFixos = new SessaoValoresFixos();
		this.sessoesArquivo.add(sessaoFixos);
		this.existeSessaoAtiva = true;
		return sessaoFixos;
	}



}
