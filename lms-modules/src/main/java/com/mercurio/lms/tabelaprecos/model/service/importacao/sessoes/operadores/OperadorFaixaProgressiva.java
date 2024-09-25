package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.FaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ItemValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.OperadorTagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class OperadorFaixaProgressiva implements OperadorTagImportacao, Observador {

	private static final int GRUPO_PREFIXO = 2;

	private TagsFaixaProgressiva tags = new TagsFaixaProgressiva();
	private Map<String, FaixaProgressivaImportacao> faixas = new HashMap<String, FaixaProgressivaImportacao>();
	private Map<Integer, FaixaProgressivaImportacao> faixasPorColunas = new HashMap<Integer, FaixaProgressivaImportacao>();
	private CacheValoresFaixaProgressiva cacheValores;
	private ChaveProgressao chave;
	private boolean processandoLinhaFaixas = false;

	@Override
	public boolean incluiTag(int linha, int coluna, String valor) {
		this.tags.informa(valor, linha, coluna);
		String prefixo = this.extrairPrefixo(valor);
		if (!this.faixas.containsKey(prefixo)) {
			this.faixas.put(prefixo, new FaixaProgressivaImportacao(prefixo, this.tags.tag(coluna)));
		}
		FaixaProgressivaImportacao faixaProgressiva = this.faixas.get(prefixo);
		this.faixasPorColunas.put(coluna, faixaProgressiva);
		return true;
	}

	private String extrairPrefixo(String valor) {
		Pattern padrao = Pattern.compile(TagsImportacaoTabelaPreco.FORMATO_TAG_FAIXA_PROGRESSIVA);
		Matcher matcher = padrao.matcher(valor);
		if (matcher.matches()) {
			return matcher.group(GRUPO_PREFIXO);
		}
		return "";
	}

	@Override
	public boolean incluiFaixa(int linha, int coluna, String valor) {
		TagImportacao tag = this.tags.tag(coluna);
		this.processandoLinhaFaixas = true;
		if (!TagsFaixaProgressiva.tagEhCodigoValorFaixa(tag.tag())){
			return true;
		}
		if(StringUtils.isBlank(valor)) {
			throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não é uma faixa válida para a tag %s.", valor, tag.tag()));
		}
		if (!Pattern.matches("([\\d]+)(([.,][\\d]+)?)", valor)) {
			throw new ImportacaoException(coluna, linha, String.format("O valor '%s' não é uma faixa válida para a tag %s.", valor, tag.tag()));
		}

		FaixaProgressivaImportacao faixaProgressiva = this.faixasPorColunas.get(coluna);
		faixaProgressiva.incluiFaixa(new ValorImportacao(linha, coluna, valor, tag));
		return true;
	}

	@Override
	public boolean incluiValor(int linha, int coluna, String valor) {
		FaixaProgressivaImportacao faixaProgressiva = this.faixasPorColunas.get(coluna);
		TagImportacao tag = this.tags.tag(coluna);

		ValorImportacao valorImportacao = new ValorImportacao(linha, coluna, valor, tag);
		validaValor(valorImportacao);
		if (chave == null && tagDeveSerCacheada(tag.tag())) {
			incluiValorEmCache(valorImportacao, faixaProgressiva);
			return true;
		}
		faixaProgressiva.incluiValor(chave, valorImportacao);
		return true;
	}

	private void validaValor(ValorImportacao valorImportacao){
		if(TagsFaixaProgressiva.tagEhTipoIndicadorCalculo(valorImportacao.tag()) && valorImportacao.valorString().isEmpty()){
			throw new ImportacaoException(valorImportacao.coluna(), valorImportacao.linha(), "O indicador de tipo de cálculo é obrigatório.");
		}
	}

	private boolean tagDeveSerCacheada(String tag) {
		return !(TagsFaixaProgressiva.tagEhMinimoProgressivo(tag) || TagsFaixaProgressiva.tagEhUnidadeMedida(tag) );
	}

	private void incluiValorEmCache(ValorImportacao valorImportacao, FaixaProgressivaImportacao faixa) {
		if (this.cacheValores == null) {
			this.cacheValores = new CacheValoresFaixaProgressiva();
		}
		String identificacao = String.format("%s_%s", faixa.identificacaoFaixa(), faixa.valorFaixaParaColuna(valorImportacao.coluna()));
		cacheValores.inclui(valorImportacao, identificacao);
	}

	@Override
	public void atualiza(Observavel observavel) {
		try {
			this.chave = ((OperadorObservavel) observavel).retornaChave();
		} catch (ImportacaoException e) {
			this.limpaCache();
			throw e;
		}
		this.chave.valida();
		this.atualizaValores(chave);
		return;
	}

	private void atualizaValores(ChaveProgressao chave) {
		if (this.cacheValores == null || this.cacheValores.estahVazio()) {
			return;
		}
		while (this.cacheValores.hasNext()) {
			ItemValorFaixaProgressivaImportacao item = this.cacheValores.next();

			ValorImportacao valorImportacao = item.valor();
			if (valorImportacao == null) {
				continue;
			}
			FaixaProgressivaImportacao faixaProgressiva = this.faixasPorColunas.get(valorImportacao.coluna());

			faixaProgressiva.incluiValores(chave, item);
		}
	}

	@Override
	public List<ComponenteImportacao> resultadoImportacao() {
		Set<FaixaProgressivaImportacao> resultadosExclusivos = new HashSet<FaixaProgressivaImportacao>(this.faixas.values());
		validaFaixas(resultadosExclusivos);
		return new ArrayList<ComponenteImportacao>(resultadosExclusivos);
	}

	private void validaFaixas(Set<FaixaProgressivaImportacao> resultadosExclusivos) {
		for (FaixaProgressivaImportacao faixa : resultadosExclusivos) {
			faixa.valida();
		}
	}

	@Override
	public TipoComponente tipoOperador() {
		return TipoComponente.FAIXA_PROGRESSIVA;
	}

	@Override
	public void validaTags() {
		if (!tags.atendeRequisitosMinimos()) {
			TagImportacao tag = this.tags.primeira();
			String prefixo = this.extrairPrefixo(tag.tag());
			throw new ImportacaoExceptionBloqueante(tag.coluna(), tag.linha(),
					String.format("Há faixas progressivas inválidas. Você deve informar [#%s_FXP_TPINDCL], [#%s_FXP_MINPROG] além de [#%s_FXP_VLFXPROG] e/ou [%s_FXP_PRDESP].", prefixo, prefixo, prefixo, prefixo));
		}
	}

	@Override
	public void reinicia() {
		this.limpaCache();
	}
	
	private void limpaCache() {
		this.chave = null;
		this.cacheValores = null;
	}

	@Override
	public void encerraLinha() {
		if (this.processandoLinhaFaixas) {
			try {
				this.verificaFaixas();
			} finally {
				this.processandoLinhaFaixas = false;
			}
		}
	}
	
	private void verificaFaixas() {
		Set<FaixaProgressivaImportacao> distinctFaixas = new HashSet<FaixaProgressivaImportacao>(this.faixas.values());
		for (FaixaProgressivaImportacao faixa : distinctFaixas) {
			verificaValoresFaixa(faixa);
		}
	}
	
	private void verificaValoresFaixa(FaixaProgressivaImportacao faixa) {
		for (ValorFaixaProgressivaImportacao valorFaixa : faixa.valores()) {
			this.validaTagFaixa(valorFaixa);
		}
	}
	
	private void validaTagFaixa(ValorFaixaProgressivaImportacao valorFaixa) {
		ValorImportacao faixa = valorFaixa.faixa();
		if (faixa == null) {
			ValorImportacao tagPesoMinimo = valorFaixa.colunaFaixaPesoMinimo();
			throw new ImportacaoException(tagPesoMinimo.coluna(), tagPesoMinimo.linha(), "A faixa não está associada a um valor de faixa progressiva ou código de produto específico válido.");
		}
		String tagFaixa = valorFaixa.tagFaixa();
		if (!TagsFaixaProgressiva.tagEhValorFaixa(tagFaixa) && !TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(tagFaixa)) {
			throw new ImportacaoException(faixa.coluna(), faixa.linha(), "A faixa não está associada a um valor de faixa progressiva ou código de produto específico válido."); 
		}
	}

}
