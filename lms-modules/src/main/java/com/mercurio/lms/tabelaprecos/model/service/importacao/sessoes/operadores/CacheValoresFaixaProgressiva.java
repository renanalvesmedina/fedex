package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ItemValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;

public class CacheValoresFaixaProgressiva implements Iterator<ItemValorFaixaProgressivaImportacao> {
	
	private Set<ItemCache> valores = new TreeSet<ItemCache>();
	private Iterator<ItemCache> iteradorValores;

	private class ItemCache implements Comparable<ItemCache>{
		
		private String identificacaoItem;
		private final ItemValorFaixaProgressivaImportacao valorItem;

		public ItemCache(String identificacao, ItemValorFaixaProgressivaImportacao item) {
			this.identificacaoItem = identificacao;
			this.valorItem = item;
		}
		
		public boolean combina(String valor) {
			return this.identificacaoItem.contains(valor);
		}
		
		public ItemValorFaixaProgressivaImportacao item() {
			return valorItem;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			if (!ItemCache.class.isInstance(o)) {
				return false;
			}
			ItemCache item = (ItemCache) o;
			return item.identificacaoItem.equals(this.identificacaoItem);
		}
		
		@Override
		public int hashCode() {
			return this.identificacaoItem.hashCode();
		}

		public void incluiValor(ValorImportacao valor) {
			this.valorItem.incluiValor(valor);
			
		}

		public boolean jaContemValor(ValorImportacao valor) {
			return this.valorItem.jaContemValor(valor);
		}

		public boolean combinaTipo(String tag) {
			ValorImportacao valor = this.valorItem.valor();
			return valor != null && valor.mesmaTag(tag);
		}

		public boolean ehTipoCompleto() {
			return Pattern.matches("([A-Z]+)(_[0-9.,]+)(_[VP])", this.identificacaoItem);
		}

		public void mudaIdentificacao(String identificacao) {
			this.identificacaoItem = identificacao;
		}

		@Override
		public int compareTo(ItemCache o) {
			
			return this.identificacaoItem.hashCode() - o.identificacaoItem.hashCode();
		}
	}
	
	public void inclui(ValorImportacao valor, String identificacaoFaixa) {
		String identificacao = this.montaIdentificacaoCompleta(identificacaoFaixa, valor);
		ItemCache item = this.buscaItemCache(identificacao, valor);
		if (item == null) {
			item = this.buscaItemCache(identificacaoFaixa, valor);
			if (item != null) {
				this.valores.remove(item);
				item.mudaIdentificacao(identificacao);
			} 
		}
		if (item == null) {
			item = new ItemCache(identificacao, new ItemValorFaixaProgressivaImportacao());
		}
		item.incluiValor(valor);
		
		this.valores.add(item);
	}
	
	public boolean estahVazio() {
		return CollectionUtils.isEmpty(this.valores);
	}

	private String montaIdentificacaoCompleta(String identificacaoFaixa, ValorImportacao valor) {
		String sufixo = "";
		if (TagsFaixaProgressiva.tagEhValorFaixa(valor.tag())) {
			sufixo = "V";
		}
		if (TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(valor.tag())) {
			sufixo = "P";
		}
		String identificacao = identificacaoFaixa;
		if (StringUtils.isNotBlank(sufixo)) {
			identificacao = String.format("%s_%s", identificacao, sufixo);
		}
		return identificacao;
	}

	private ItemCache buscaItemCache(final String identificacao, final ValorImportacao valor) {
		return (ItemCache) CollectionUtils.find(this.valores, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				ItemCache item = (ItemCache) object;
				if (valorEhTagCompleta(valor.tag()) && item.ehTipoCompleto() && !item.combinaTipo(valor.tag())) {
					return false;
				}
				if (!item.combina(identificacao)) {
					return false;
				}
				return !item.jaContemValor(valor);
			}
			
			private boolean valorEhTagCompleta(String tag) {
				return TagsFaixaProgressiva.tagEhValorFaixa(tag) || TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(tag);
			}
		});
	}

	@Override
	public boolean hasNext() {
		if (this.iteradorValores == null) {
			this.iteradorValores = this.valores.iterator();
		}
		return this.iteradorValores.hasNext();
	}

	@Override
	public ItemValorFaixaProgressivaImportacao next() {
		if (this.iteradorValores == null) {
			this.iteradorValores = this.valores.iterator();
		}
		return this.iteradorValores.next().item();
	}

	@Override
	public void remove() {
		this.iteradorValores.remove();		
	}

	
	
}