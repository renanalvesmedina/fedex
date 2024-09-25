package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class GenericoImportacao implements ComponenteImportacao {

	public enum TipoCampoGenericoImportacao {
		VALOR("(\\[#[A-Z]+\\])"),
		VALOR_MINIMO("(\\[#[A-Z]+)(_VLMIN)(\\])"),
		PESO_MINIMO("(\\[#[A-Z]+)(_PSMIN)(\\])"),
		PESO_TAXADO("(\\[#[A-Z]+)(_PSTAXADO)(\\])"),
		VALOR_EXCEDENTE("(\\[#[A-Z]+)(_VLEXCED)(\\])");

		private Pattern pattern;
		private TipoCampoGenericoImportacao(String padrao) {
			this.pattern = Pattern.compile(padrao);
		}

		public boolean confere(String valor) {
			return this.pattern.matcher(valor).matches();
		}

		public static TipoCampoGenericoImportacao retornaTipo(String valor) {
			for (TipoCampoGenericoImportacao tipo : TipoCampoGenericoImportacao.values()) {
				if (tipo.confere(valor)) {
					return tipo;
				}
			}
			return null;
		}

	}

	private Map<TipoCampoGenericoImportacao, ValorImportacao> valores;
	private TagImportacao tagEmAberto;
	private TipoCampoGenericoImportacao tipoCampo;
	private String identificacaoGenerico;

	public void incluiTag(int linha, int coluna, String tag) {
		TipoCampoGenericoImportacao tipo = TipoCampoGenericoImportacao.retornaTipo(tag);
		if (tipo == null) {
			throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não é uma tag de Generalidade/Taxa/Serviço Adicional válida.", tag));
		}
		if (TipoCampoGenericoImportacao.VALOR.equals(tipo)) {
			this.identificacaoGenerico = tag;
		}
		this.tipoCampo = tipo;
		this.tagEmAberto = new TagImportacao(linha, coluna, tag);
	}

	private void iniciaValores() {
		if (this.valores == null) {
			this.valores = new HashMap<TipoCampoGenericoImportacao, ValorImportacao>();
		}
	}

	public void incluiValor(int linha, int coluna, String valor) {
		if (this.tagEmAberto == null) {
			throw new ImportacaoException(coluna, linha, String.format("Não há tag correspondente para o valor '%s'", valor));
		}
		iniciaValores();
		validaTagDuplicada();
		this.valores.put(this.tipoCampo, new ValorImportacao(linha, coluna, valor, this.tagEmAberto));

		this.tipoCampo = null;
		this.tagEmAberto = null;
	}

	private void validaTagDuplicada() {
		if (this.valores.get(this.tipoCampo) != null){
			throw new ImportacaoExceptionBloqueante(this.tagEmAberto.coluna(), this.tagEmAberto.linha(), String.format("A tag '%s' está duplicada.", this.tagEmAberto.tag()));
		}
	}

	public String identificacao() {
		return identificacaoGenerico;
	}

	public GeneralidadeImportacao comoGeneralidade() {
		return new GeneralidadeImportacao(this.valores.get(TipoCampoGenericoImportacao.VALOR), this.valores.get(TipoCampoGenericoImportacao.PESO_MINIMO), this.valores.get(TipoCampoGenericoImportacao.VALOR_MINIMO));
	}

	public TaxaImportacao comoTaxa() {
		return new TaxaImportacao(this.valores.get(TipoCampoGenericoImportacao.VALOR), this.valores.get(TipoCampoGenericoImportacao.PESO_TAXADO), this.valores.get(TipoCampoGenericoImportacao.VALOR_EXCEDENTE));
	}

	public ServicoAdicionalImportacao comoServicoAdicional() {
		return new ServicoAdicionalImportacao(this.valores.get(TipoCampoGenericoImportacao.VALOR), this.valores.get(TipoCampoGenericoImportacao.VALOR_MINIMO));
	}

	@Override
	public String valorTagPrincipal() {
		ValorImportacao valorImportacao = this.valores.get(TipoCampoGenericoImportacao.VALOR);
		return valorImportacao.tag();
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.GENERICO;
	}

	@Override
	public TagImportacao tagPrincipal() {
		ValorImportacao valor = this.valores.get(TipoCampoGenericoImportacao.VALOR);
		if (valor == null) {
			TagImportacao tag = this.primeiraTagNaoNula();
			throw new ImportacaoException(tag.coluna(), tag.linha(), String.format("A tag principal da tag '%s' não foi encontrada no arquivo.", tag.tag()));
		}
		return valor.tagReferancia();
	}

	private TagImportacao primeiraTagNaoNula() {
		if (!MapUtils.isEmpty(this.valores)) {
			for (ValorImportacao valor : this.valores.values()) {
				if (valor.tagReferancia() == null) {
					continue;
				}
				return valor.tagReferancia();
			}
		}
		throw new IllegalStateException("Não há valores disponíveis para este componente.");
	}

}
