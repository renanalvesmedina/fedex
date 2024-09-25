package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.GerenciadorSessoesTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.util.LeitorCelulaImportacao;

public class InterpretadorImportacaoTabelaPreco {

	private List<String> errosInterpretadorImportacao;
	private ProcessadorTabelaPreco processadorAtivo;
	private GerenciadorSessoesTabelaPreco gerenciadorSessoes;
	private boolean processavel;
	private Map<Character, ProcessadorTabelaPreco> processadores = new HashMap<Character, ProcessadorTabelaPreco>();
	private String ultimaTag;

	public ComponentesTabelaPreco varrer(HSSFWorkbook arquivo) {
		if (arquivo == null) {
			throw new BusinessException("Arquivo inválido: null");
		}
		this.errosInterpretadorImportacao = null;
		HSSFSheet pagina = arquivo.getSheetAt(0);

		this.gerenciadorSessoes = new GerenciadorSessoesTabelaPreco();

		this.processar(pagina);

		try {
			Map<TipoComponente, List<ComponenteImportacao>> componentes = gerenciadorSessoes.componentes();
			return new ComponentesTabelaPreco(componentes);
		} catch (ImportacaoException e) {
			this.capturaErro(e);
		}
		return new ComponentesTabelaPreco(new HashMap<ComponenteImportacao.TipoComponente, List<ComponenteImportacao>>());

	}

	@SuppressWarnings("unchecked")
	private void processar(HSSFSheet pagina) {
		Iterator<HSSFRow> linhas = pagina.rowIterator();
		while (linhas.hasNext()) {
			boolean continua = processaLinhaTratandoExcecao(linhas.next());
			if (!continua) {
				break;
			}
		}
		for (ProcessadorTabelaPreco processador : processadores.values()) {
			this.incluiErrosProcessoTabela(processador);

		}
	}

	private void incluiErrosProcessoTabela(ProcessadorTabelaPreco processador) {
		if (processador == null) {
			return;
		}
		List<String> errosProcessadorTabela = processador.erros();
		if (CollectionUtils.isNotEmpty(errosProcessadorTabela)) {
			for (String erro : errosProcessadorTabela) {
				this.incluiErro(erro);
			}
		}
	}

	private void incluiErro(String ... erros) {
		if (this.errosInterpretadorImportacao == null) {
			this.errosInterpretadorImportacao = new ArrayList<String>();
		}
		this.errosInterpretadorImportacao.addAll(Arrays.asList(erros));
	}

	private boolean processaLinhaTratandoExcecao(HSSFRow linha) {
		boolean continua = false;
		try {
			continua = this.processarLinha(linha);
		} catch (ImportacaoException e) {
			this.capturaErro(e);
		}
		return continua;
	}

	private void capturaErro(ImportacaoException... erros) {
		for (ImportacaoException erro : erros) {
			this.incluiErro(erro.mensagens().toArray(new String[erro.mensagens().size()]));
		}
	}

	private boolean processarLinha(HSSFRow linha) {
		for(short x = linha.getFirstCellNum(); x < linha.getLastCellNum(); x++){
			HSSFCell celula = linha.getCell(x);
			int numeroLinha = linha.getRowNum();
			if(celula != null){
				short numeroColuna = celula.getCellNum();
				String valorCelula = this.valorCelula(celula);
				if (TagsImportacaoTabelaPreco.TAG_FIM_ARQUIVO.equals(valorCelula)) {
					if (this.processadorAtivo != null ){
						throw new ImportacaoException(numeroColuna, numeroLinha, "Há uma sessão que ainda não está fechada. Você deve fechar todas as sessões antes de fechar o arquivo.");
					}
					return false;
				}
				ProcessadorTabelaPreco processador = this.buscaProcessador(numeroLinha, numeroColuna, valorCelula);
				if(!this.processavel){
					break;
				}
				processador.processaLinha(linha);
				break;
			}
		}
		return true;

	}

	private ProcessadorTabelaPreco buscaProcessador(int linha, int coluna, String valorCelula) {
		if (this.processadorAtivo != null) {
			if (TagsImportacaoTabelaPreco.ehTagFechaSessao(valorCelula)) {
				if (isAbreFechaBlocoTabela(this.ultimaTag, valorCelula) || isAbreFechaBlocoFixo(this.ultimaTag, valorCelula)){
					this.processadorAtivo.fechaSessao(linha, coluna);
					this.processadorAtivo = null;
					this.processavel = false;
					return this.processadorAtivo;
				}else{
					throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não está encerrando o bloco corretamente.", valorCelula));
				}
			}
			if (TagsImportacaoTabelaPreco.ehTagAbreSessao(valorCelula)) {
				throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não é válido aqui", valorCelula));
			}
			this.processavel= true;
			return this.processadorAtivo;
		}else{
			isTagFechaSessao(linha, coluna, valorCelula);
		}

		defineAbreTabelaOuFixo(linha, coluna, valorCelula);

		this.processavel = false;
		return this.processadorAtivo;
	}

	private void isTagFechaSessao(int linha, int coluna, String valorCelula){
		if(TagsImportacaoTabelaPreco.ehTagFechaSessao(valorCelula)){
			throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não está encerrando nenhum bloco correspondente.", valorCelula));
		}
	}

	private boolean isAbreFechaBlocoTabela(String tagInicio, String tagFim){
		return TagsImportacaoTabelaPreco.TAG_ABRE_TABELA.equals(tagInicio) && TagsImportacaoTabelaPreco.TAG_FECHA_TABELA.equals(tagFim);
	}

	private boolean isAbreFechaBlocoFixo(String tagInicio, String tagFim){
		return TagsImportacaoTabelaPreco.TAG_ABRE_FIXO.equals(tagInicio) && TagsImportacaoTabelaPreco.TAG_FECHA_FIXO.equals(tagFim);
	}

	private void defineAbreTabelaOuFixo(int linha, int coluna, String valorCelula){
		if (TagsImportacaoTabelaPreco.TAG_ABRE_TABELA.equals(valorCelula)) {
			this.abreTabela(linha, coluna);
			this.ultimaTag = TagsImportacaoTabelaPreco.TAG_ABRE_TABELA;
		}
		if (TagsImportacaoTabelaPreco.TAG_ABRE_FIXO.equals(valorCelula)) {
			this.abreFixo(linha, coluna);
			this.ultimaTag = TagsImportacaoTabelaPreco.TAG_ABRE_FIXO;
		}
	}

	private void abreTabela(int linha, int coluna) {
		if(!this.processadores.containsKey('T')){
			ProcessadorTabela processadorTabela = new ProcessadorTabela(this.gerenciadorSessoes);
			this.processadores.put('T', processadorTabela);
		}
		this.processadorAtivo = this.processadores.get('T');
		this.processadorAtivo.abreSessao(linha, coluna);
	}

	private void abreFixo(int linha, int coluna) {
		if(!this.processadores.containsKey('F')){
			ProcessadorFixos processadorFixo = new ProcessadorFixos(this.gerenciadorSessoes);
			this.processadores.put('F', processadorFixo);
		}
		this.processadorAtivo = this.processadores.get('F');
		this.processadorAtivo.abreSessao(linha, coluna);
	}

	private String valorCelula(HSSFCell celula) {
		return LeitorCelulaImportacao.valorCelula(celula);
	}

	public List<String> erros () {
		if (this.errosInterpretadorImportacao == null || this.errosInterpretadorImportacao.isEmpty()) {
			return Collections.emptyList();
		}
		return this.errosInterpretadorImportacao;
	}

}
