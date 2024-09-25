package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.util.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.GerenciadorSessoesTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.SessaoTabela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.util.LeitorCelulaImportacao;

public class ProcessadorTabela implements ProcessadorTabelaPreco {

	private enum TipoLinha {
		LINHA_TAGS, LINHA_FAIXAS, LINHA_VALORES;

		public TipoLinha proxima() {
			if (this.equals(LINHA_TAGS)) {
				return LINHA_FAIXAS;
			}
			return LINHA_VALORES;
		}
	}

	private GerenciadorSessoesTabelaPreco gerenciador;
	private SessaoTabela sessao;
	private List<String> mensagens;
	private TipoLinha proximaLinha;
	private Set<Integer> colunasIgnoradas;

	public ProcessadorTabela(GerenciadorSessoesTabelaPreco gerenciador) {
		this.gerenciador = gerenciador;
	}

	private void capturaErro(ImportacaoException e) {
		if (this.mensagens == null) {
			this.mensagens = new ArrayList<String>();
		}
		this.mensagens.addAll(e.mensagens());

	}

	@Override
	public List<String> erros() {
		if (CollectionUtils.isEmpty(this.mensagens)) {
			return Collections.emptyList();
		}
		return this.mensagens;
	}

	@Override
	public void abreSessao(int linha, int coluna) {
		if (this.gerenciador.temSessaoAberta()) {
			throw new ImportacaoException(coluna, linha, "Já existe uma tabela aberta. Você deve fechar a tabela que está aberta para abrir uma nova.");
		}
		this.sessao = this.gerenciador.abreSessaoTabela();
		this.proximaLinha = TipoLinha.LINHA_TAGS;
	}

	@Override
	public void fechaSessao(int linha, int coluna) {
		if (!this.gerenciador.temSessaoAberta()) {
			throw new ImportacaoException(coluna, linha, "Não há tabela aberta para fechar.");
		}
		this.gerenciador.fecharSessaoAtiva();
		this.sessao = null;
	}

	@Override
	public void processaLinha(HSSFRow linha) {
		try {
			if (TipoLinha.LINHA_TAGS.equals(this.proximaLinha)) {
				this.processaLinhaTags(linha);
				return;
			}
			if (TipoLinha.LINHA_FAIXAS.equals(this.proximaLinha)) {
				this.processaLinhaFaixas(linha);
				return;
			}
			if (TipoLinha.LINHA_VALORES.equals(this.proximaLinha)) {
				this.processaLinhaValores(linha);
			}
			
		} catch (ImportacaoException e) {
			this.capturaErro(e);
		} finally {
			this.proximaLinha = this.proximaLinha.proxima();
			sessao.novaLinha();
		}

	}

	private void processaLinhaTags(HSSFRow linha) {
		int numeroLinha = linha.getRowNum();
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> celulas = linha.cellIterator();
		while(celulas.hasNext()) {
			HSSFCell celula = celulas.next();
			try {
				incluiTag(numeroLinha, celula);
			} catch (ImportacaoException e) {
				incluiColunaIgnorada(celula.getCellNum());
				this.capturaErro(e);
			}
		}

		sessao.validaLinhaTags();
	}


	private void incluiTag(int numeroLinha, HSSFCell celula) {
		String valor = LeitorCelulaImportacao.valorCelula(celula);
		int numeroColuna = celula.getCellNum();
		if (!TagsImportacaoTabelaPreco.ehTag(valor)) {
			throw new ImportacaoExceptionBloqueante(numeroColuna, numeroLinha, String.format("O valor '%s' não é uma tag válida. Somente tags são esperadas nessa linha.", valor));
		}
		if(!TagsImportacaoTabelaPreco.ehTagTabela(valor)){
			throw new ImportacaoExceptionBloqueante(numeroColuna, numeroLinha, String.format("O valor '%s' não é uma tag de valores tabela válida.", valor));
		}
		this.sessao.incluiTag(numeroLinha, numeroColuna, valor);
	}

	private void incluiColunaIgnorada(short cellNum) {
		if (this.colunasIgnoradas == null) {
			this.colunasIgnoradas = new HashSet<Integer>();
		}
		this.colunasIgnoradas.add(Integer.valueOf(cellNum));
	}

	private void processaLinhaFaixas(HSSFRow linha) {
		int numeroLinha = linha.getRowNum();
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> celulas = linha.cellIterator();
		while(celulas.hasNext()) {
			HSSFCell celula = celulas.next();
			if (colunaDeveSerIgnorada(celula.getCellNum())) {
				continue;
			}
			try {
				String valor = LeitorCelulaImportacao.valorCelula(celula);
				this.sessao.incluiFaixa(numeroLinha, celula.getCellNum(), valor);
			} catch (ImportacaoException e) {
				this.incluiColunaIgnorada(celula.getCellNum());
				this.capturaErro(e);
			}
		}

	}
	
	private boolean colunaDeveSerIgnorada(short cellNum) {
		if (CollectionUtils.isEmpty(this.colunasIgnoradas)) {
			return false;
		}
		Integer coluna = Integer.valueOf(cellNum);
		return this.colunasIgnoradas.contains(coluna);
	}

	private void processaLinhaValores(HSSFRow linha) {
		int numeroLinha = linha.getRowNum();
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> celulas = linha.cellIterator();
		while(celulas.hasNext()) {
			HSSFCell celula = celulas.next();
			if (colunaDeveSerIgnorada(celula.getCellNum())) {
				continue;
			}
			try {
				String valor = LeitorCelulaImportacao.valorCelula(celula);
				this.sessao.incluiValor(numeroLinha, celula.getCellNum(), valor);
			} catch (ImportacaoException e) {
				this.capturaErro(e);
			}
		}
	}

	public boolean temSessaoAberta() {
		return this.sessao != null;
	}
}
