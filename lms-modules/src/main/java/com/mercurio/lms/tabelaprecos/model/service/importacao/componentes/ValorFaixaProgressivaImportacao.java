package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class ValorFaixaProgressivaImportacao implements Comparable<ValorFaixaProgressivaImportacao>{
	
	private static final int GRUPO_PREFIXO = 2;

	private String identificacaoFaixa;
	private ValorImportacao faixaValorFaixaProgressiva;
	private ValorImportacao faixaPesoMinimo;
	private ValorImportacao faixaFatorMultiplicacao;
	private Map<ChaveProgressao, ItemValorFaixaProgressivaImportacao> valoresValorFaixaProgressiva;
	private ChaveProgressao chaveCorrente;
	private static final Pattern PADRAO_VALOR_IDENTIFICACAO = Pattern.compile("([VP]:)?((\\d+)([,.]\\d+)?)");
	
	
	public ItemValorFaixaProgressivaImportacao novoItem(){
		return new ItemValorFaixaProgressivaImportacao();
	}

	public boolean incluifaixa(ValorImportacao valor) {
		if (this.jahPossuiFaixaPara(valor)) {
			ValorImportacao faixaExistente = this.buscaFaixa(valor.tag());
			throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("A faixa para '%s' já foi informada na coluna %d.", valor.tag(), faixaExistente.coluna()));
		}
		if (TagsFaixaProgressiva.tagEhValorFaixa(valor.tag()) || TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(valor.tag())) {
			this.faixaValorFaixaProgressiva = valor;
			this.identificacaoFaixa = montaIdentificacaoFaixa(valor);
		}
		if (TagsFaixaProgressiva.tagEhPesoMinimo(valor.tag())) {
			this.faixaPesoMinimo = valor;
		}
		if (TagsFaixaProgressiva.tagEhFatorMultiplicacao(valor.tag())) {
			this.faixaFatorMultiplicacao = valor;
		}
		if (StringUtils.isBlank(this.identificacaoFaixa)) {
			this.identificacaoFaixa = montaIdentificacaoFaixa(valor);
		}
		return true;
	}

	private ValorImportacao buscaFaixa(String tag) {
		if (TagsFaixaProgressiva.tagEhValorFaixa(tag) || TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(tag)) {
			return this.faixaValorFaixaProgressiva;
		}
		if (TagsFaixaProgressiva.tagEhPesoMinimo(tag)) {
			return this.faixaPesoMinimo;
		}
		if (TagsFaixaProgressiva.tagEhFatorMultiplicacao(tag)) {
			return this.faixaFatorMultiplicacao;
		}
		return null;
	}

	private void incluiChave(ChaveProgressao chave){
		iniciaValoresFaixaProgressiva();
		
		this.valoresValorFaixaProgressiva.put(chave, new ItemValorFaixaProgressivaImportacao());
		chaveCorrente = chave;
	}

	private void iniciaValoresFaixaProgressiva() {
		if (this.valoresValorFaixaProgressiva == null) {
			this.valoresValorFaixaProgressiva = new HashMap<ChaveProgressao, ItemValorFaixaProgressivaImportacao>();
		}
	}

	public boolean incluiValor(ChaveProgressao chave, ValorImportacao valor) {
		validaChave(chave, valor);
		
		if(this.chaveCorrente == null || chave.linha() != chaveCorrente.linha()){
			incluiChave(chave);
		}
		
		ItemValorFaixaProgressivaImportacao item = valoresValorFaixaProgressiva.get(chave);
		item.incluiValor(valor);
		
		return true;
	}

	private void validaChave(ChaveProgressao chave, ValorImportacao valor) {
		if(chave == null){
			throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("Não foi possível incluir valor para a tag '%s', pois a rota/tarifa não foi informada.", valor.tag()));
		}
	}
	
	public boolean incluiValores(ChaveProgressao chave, ItemValorFaixaProgressivaImportacao item) {
		validaChave(chave, item.valor());
		iniciaValoresFaixaProgressiva();
		if (!this.valoresValorFaixaProgressiva.containsKey(chave)) {
			this.valoresValorFaixaProgressiva.put(chave, item);
			return true;
		}
			
		ItemValorFaixaProgressivaImportacao itemExistente = this.valoresValorFaixaProgressiva.get(chave);
		if (!item.equals(itemExistente)) {
			ValorImportacao valor = item.valor();
			throw new ImportacaoException(valor.coluna(), valor.linha(), 
					String.format("A chave '%s' informada nessa linha tem valores diferentes para essa faixa na linha %d.", chave.tipo().name(), itemExistente.valor().linha() + 1));
		}
		return true;
	}

	public Map<ChaveProgressao, ItemValorFaixaProgressivaImportacao> valores(){
		return this.valoresValorFaixaProgressiva;
	}

	public Short idProdutoEspecifico() {
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_PRODUTO_ESPECIFICO,  faixaValorFaixaProgressiva.tag())) {
			return faixaValorFaixaProgressiva.valorShort();
		}
		return null;
	}

	public BigDecimal valorfaixaProgressiva() {
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA, faixaValorFaixaProgressiva.tag())) {
			return faixaValorFaixaProgressiva.valorBigDecimal();
		}
		return null;
	}

	public ValorImportacao faixa(){
		return this.faixaValorFaixaProgressiva;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!o.getClass().isAssignableFrom(ValorFaixaProgressivaImportacao.class)) {
			return false;
		}
		ValorFaixaProgressivaImportacao valor = (ValorFaixaProgressivaImportacao) o;
		if(valor == this){
			return true;
		}
		return new EqualsBuilder().append(valor.identificacaoFaixa, this.identificacaoFaixa).isEquals();
	}

	@Override
	public int hashCode() {
		if (this.identificacaoFaixa == null) {
			return 0;
		}
		return new HashCodeBuilder().append(this.identificacaoFaixa).toHashCode();
	}

	@Override
	public int compareTo(ValorFaixaProgressivaImportacao o) {
		if (o == null) {
			return -1;
		}
		return this.identificacaoFaixa.hashCode() - o.identificacaoFaixa.hashCode();
	}

	public String identificacao() {
		return this.identificacaoFaixa;
	}
	
	public static String montaIdentificacaoFaixa(ValorImportacao faixa) {
		if (TagsFaixaProgressiva.tagEhValorFaixa(faixa.tag())) {
			return String.format("V:%s", faixa.valorString());
		}
		if (TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(faixa.tag())) {
			return String.format("P:%s", faixa.valorString());
		}
		return faixa.valorString();
	}

	public boolean identificacaoCombinaCom(String identificacao) {
		String valorIdentificacaoLocal = this.buscaValorIdentificacao(this.identificacaoFaixa);
		String valorIdentificacaoExterna = this.buscaValorIdentificacao(identificacao);
		return valorIdentificacaoLocal.equals(valorIdentificacaoExterna);
	}

	private String buscaValorIdentificacao(String identificacao) {
		Matcher matcher = PADRAO_VALOR_IDENTIFICACAO.matcher(identificacao);
		if (matcher.matches()) {
			return matcher.group(GRUPO_PREFIXO);
		}
		throw new IllegalArgumentException("Essa identificação não é classificada como identificação válida: " + identificacao);
	}

	public String tagFaixa() {
		if (this.faixaValorFaixaProgressiva == null) {
			return StringUtils.EMPTY;
		}
		return this.faixaValorFaixaProgressiva.tag();
	}

	public boolean jahPossuiFaixaPara(ValorImportacao faixa) {
		return this.buscaFaixa(faixa.tag()) != null;
	}

	public ValorImportacao colunaFaixaPesoMinimo() {
		return this.faixaPesoMinimo;
	}

}
