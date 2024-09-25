package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ComponentesTabelaPreco.ItemComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;

public class ComponentesTabelaPreco implements Iterator<ItemComponenteImportacao>{

	private Map<TipoComponente, List<ComponenteImportacao>> componentes = new HashMap<ComponenteImportacao.TipoComponente, List<ComponenteImportacao>>();
	Iterator<Entry<TipoComponente, List<ComponenteImportacao>>> entrySet;

	public ComponentesTabelaPreco(Map<TipoComponente, List<ComponenteImportacao>> componentes) {
		this.componentes.putAll(componentes);
	}

	public List<ComponenteImportacao> precosFrete() {
		return this.componentes.get(TipoComponente.PRECO_FRETE);
	}

	@Override
	public boolean hasNext() {
		if (entrySet == null) {
			entrySet = this.componentes.entrySet().iterator();
		}
		return entrySet.hasNext();
	}

	@Override
	public ItemComponenteImportacao next() {
		Entry<TipoComponente, List<ComponenteImportacao>> next = entrySet.next();
		return new ItemComponenteImportacao(next.getKey(), next.getValue());
	}

	@Override
	public void remove() {
		//não faz nada
	}

	public boolean estahVazio() {
		return MapUtils.isEmpty(this.componentes);
	}

	public boolean naoEstahVazio() {
		return !this.estahVazio();
	}


	public static class ItemComponenteImportacao {
		TipoComponente tipoItemComponente;
		List<ComponenteImportacao> componentes;
		private ItemComponenteImportacao(TipoComponente tipo, List<ComponenteImportacao> componentes) {
			this.tipoItemComponente = tipo;
			this.componentes = componentes;
		}

		public TipoComponente tipo() {
			return tipoItemComponente;
		}

		public List<ComponenteImportacao> itens() {
			return componentes;
		}
	}


}
