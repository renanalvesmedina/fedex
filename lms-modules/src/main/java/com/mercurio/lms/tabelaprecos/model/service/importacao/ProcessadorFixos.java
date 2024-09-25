package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.GerenciadorSessoesTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.SessaoValoresFixos;
import com.mercurio.lms.tabelaprecos.model.service.importacao.util.LeitorCelulaImportacao;

public class ProcessadorFixos implements ProcessadorTabelaPreco {

	private GerenciadorSessoesTabelaPreco gerenciador;
	private SessaoValoresFixos sessaoFixos;
	private List<String> errosProcessadorFixos;

	public ProcessadorFixos(GerenciadorSessoesTabelaPreco gerenciador) {
		if (gerenciador == null) {
			throw new IllegalArgumentException("Gerenciador de sessões de arquivo de importação inválido: null");
		}
		this.gerenciador = gerenciador;
	}

	@Override
	public void abreSessao(int linha, int coluna) {
		if (this.gerenciador.temSessaoAberta()) {
			throw new ImportacaoException(coluna, linha, "Já existe uma sessão aberta. Você deve fechar a sessão anterior antes de abrir uma nova.");
		}
		this.sessaoFixos = this.gerenciador.abreSessaoFixos();
	}

	@Override
	public void processaLinha(HSSFRow row) {
		if (row == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		Iterator<HSSFCell> celulas = row.cellIterator();
		Stream<HSSFCell> cellStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(celulas, Spliterator.ORDERED), false);
		celulas = cellStream.sorted(Comparator.comparing(c -> c.getCellNum())).iterator();
		if (celulas == null) {
			return;
		}
		while(celulas.hasNext()) {
			this.processaCelulaTratandoExcecao(row.getRowNum(), celulas.next());
		}
	}

	private void processaCelulaTratandoExcecao(int linha, HSSFCell celula) {
		int coluna = celula.getCellNum();
		String valor = LeitorCelulaImportacao.valorCelula(celula);
		try {
			this.sessaoFixos.informaConteudo(linha, coluna, valor);
		} catch (ImportacaoException e) {
			this.incluiErro(e);
		}
	}

	private void incluiErro(ImportacaoException exception) {
		if (this.errosProcessadorFixos == null) {
			this.errosProcessadorFixos = new ArrayList<String>();
		}
		this.errosProcessadorFixos.addAll(exception.mensagens());
	}

	@Override
	public void fechaSessao(int linha, int coluna) {
		this.gerenciador.fecharSessaoAtiva();
		this.sessaoFixos = null;
	}

	public boolean temSessaoAberta() {
		return this.sessaoFixos != null;
	}

	@Override
	public List<String> erros() {
		if(CollectionUtils.isEmpty(this.errosProcessadorFixos)){
			return Collections.emptyList();
		}
		return this.errosProcessadorFixos;
	}
}
