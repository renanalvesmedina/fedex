package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

/**
 * Classe responsável por armazenar os dados informados na planilha, inclusive com coluna e linha, relativos a uma determinada parcela
 * de preço do tipo de precificação "MÍNIMO PROGRESSIVO".
 * 
 * Cada parcela desse tipo terá um (1) objeto desse tipo, controlando as faixas (5kg, 10kg, 20kg, etc) ou produtos específicos (1, 10, 240, etc)
 * de acordo com as colunas informadas na planilha. Essas faixas serão identificadas pelo prefixo da tag, ex: [#FRPE_FXP_VLFXPROG] terá como 
 * prefixo o valor FRPE. 
 * 
 * @author Marcelo Schmidt (marceloschmidt@cwi.com.br)
 *
 */
public class FaixaProgressivaImportacao implements ComponenteImportacao {

	private static final int TAMANHO_MAXIMO_MINIMO_PROGRESSIVO = 2;
	
	private String identificacao;
	private ValorImportacao minimoProgressivoFaixaProgressiva;
	private ValorImportacao unidadeMedidaFaixaProgressiva;
	private final TagImportacao tagPrincipalFaixaProgressiva;

	private Map<Integer, ValorFaixaProgressivaImportacao> faixas = new HashMap<Integer, ValorFaixaProgressivaImportacao>();
	private Set<ValorFaixaProgressivaImportacao> valoresRetornados = null;

	public FaixaProgressivaImportacao(String identificacao, TagImportacao tagPrincipal){
		this.identificacao = identificacao;
		this.tagPrincipalFaixaProgressiva = tagPrincipal;
	}

	public void incluiFaixa(ValorImportacao faixa) {
		this.valoresRetornados = null;
		ValorFaixaProgressivaImportacao valor = this.buscaFaixa(faixa, ValorFaixaProgressivaImportacao.montaIdentificacaoFaixa(faixa));
		if (valor == null) {
			valor = new ValorFaixaProgressivaImportacao();
		}
		valor.incluifaixa(faixa);
		this.faixas.put(faixa.coluna(), valor);
	}

	private ValorFaixaProgressivaImportacao buscaFaixa(final ValorImportacao faixa, final String identificacaoFaixa) {
		ValorFaixaProgressivaImportacao valorFaixa = this.faixas.get(faixa.coluna());
		if (valorFaixa != null) {
			return valorFaixa;
		}
		return (ValorFaixaProgressivaImportacao) CollectionUtils.find(this.faixas.values(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				ValorFaixaProgressivaImportacao valor = (ValorFaixaProgressivaImportacao) object;
				if (valor.identificacao().equals(identificacaoFaixa)) {
					return true;
				}
				if (valor.identificacaoCombinaCom(identificacaoFaixa) && !valor.jahPossuiFaixaPara(faixa)) {
					return true;
				}
				return false;
			}
		});
	}

	public void incluiValor(ChaveProgressao chave, ValorImportacao valor) {
		if (valor == null) {
			return;
		}
		if (incluiValoresFixos(valor)) {
			return;
		}
		ValorFaixaProgressivaImportacao valorFaixa = buscaFaixa(valor, ValorFaixaProgressivaImportacao.montaIdentificacaoFaixa(valor));
		if (valorFaixa == null) {
			return;
		}
		valorFaixa.incluiValor(chave, valor);
	}
	
	public void incluiValores(ChaveProgressao chave, ItemValorFaixaProgressivaImportacao item) {
		if (item == null) {
			return;
		}
		ValorFaixaProgressivaImportacao valorFaixa = buscaFaixa(item.valor(), ValorFaixaProgressivaImportacao.montaIdentificacaoFaixa(item.valor()));
		if (valorFaixa == null) {
			return;
		}
		valorFaixa.incluiValores(chave, item);
	}

	private boolean incluiValoresFixos(ValorImportacao valor) {
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_MINIMO_PROGRESSIVO, valor.tag())) {
			informaMinimoProgressivo(valor);
			return true;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_UNIDADE_MEDIDA, valor.tag())) {
			this.informaUnidadeMedida(valor);
			return true;
		}
		return false;
	}

	private void informaMinimoProgressivo(ValorImportacao valor) {
		if (minimoProgressivoFaixaProgressiva != null) {
			return;
		}
		if(!ValorImportacao.estahInformado(valor)){
			throw new ImportacaoException(valor.coluna(), valor.linha(), "O valor de mínimo progressivo é de preenchimento obrigatório.");
		}
		if (valor.valorString().length() > TAMANHO_MAXIMO_MINIMO_PROGRESSIVO) {
			throw new ImportacaoException(valor.coluna(), valor.linha(), "O valor de mínimo progressivo tem um limite máximo de 2 caracteres.");
		}
		this.minimoProgressivoFaixaProgressiva = valor;
	}


	private void informaUnidadeMedida(ValorImportacao valor) {
		Pattern pattern = Pattern.compile("([\\d]+)([.,])?(0)?");
		Matcher matcher = pattern.matcher(valor.valorString());
		if(matcher.matches()){
			this.unidadeMedidaFaixaProgressiva = new ValorImportacao(valor.linha(), valor.coluna(), matcher.group(1), valor.tagReferancia());
			return;
		}
		throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("O valor '%s' não é um valor válido para a tag '%s'.", valor.valorString(), valor.tag()));
	}


	public String minimoProgressivo() {
		if (this.minimoProgressivoFaixaProgressiva == null) {
			return null;
		}
		return this.minimoProgressivoFaixaProgressiva.valorString();
	}

	public String unidadeMedida() {
		if(this.unidadeMedidaFaixaProgressiva == null){
			return null;
		}
		return this.unidadeMedidaFaixaProgressiva.valorString();
	}

	public String identificacaoFaixa() {
		return this.identificacao;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.identificacao).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof FaixaProgressivaImportacao)) {
			return false;
		}
		FaixaProgressivaImportacao outro = (FaixaProgressivaImportacao) o;
		return new EqualsBuilder().append(this.identificacao, outro.identificacao).isEquals();
	}

	@Override
	public String valorTagPrincipal() {
		return String.format("[#%s_FXP_VLPRECOFRETE]", this.identificacao);
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.FAIXA_PROGRESSIVA;
	}

	public boolean possuiValores() {
		return CollectionUtils.isNotEmpty(faixas.values());
	}

	
	public Collection<ValorFaixaProgressivaImportacao> valores() {
		if (valoresRetornados == null) {
			valoresRetornados = new TreeSet<ValorFaixaProgressivaImportacao>(this.faixas.values());
		}
		return valoresRetornados;
	}

	public ValorFaixaProgressivaImportacao faixaPorColuna(int coluna) {
		if (MapUtils.isEmpty(this.faixas)) {
			return null;
		}
		if (!this.faixas.containsKey(coluna)) {
			return null;
		}
		return this.faixas.get(coluna);
	}

	@Override
	public TagImportacao tagPrincipal() {
		return this.tagPrincipalFaixaProgressiva;
	}

	public void valida() {
		String mensagem = "";
		if (this.minimoProgressivoFaixaProgressiva == null) {
			mensagem += "O código de mínimo progressivo é obrigatório. ";
		}
		if (MapUtils.isEmpty(this.faixas)) {
			mensagem += "É preciso informar pelo menos um código de produto específico ou valor de faixa progressiva.";
		}
		if (StringUtils.isNotBlank(mensagem)) {
			throw new ImportacaoException(0, 0, mensagem);
		}
	}
	
	public String valorFaixaParaColuna(int coluna) {
		if (faixas.containsKey(coluna)) {
			return faixas.get(coluna).faixa().valorString();
		}
		return "";
	}

}
