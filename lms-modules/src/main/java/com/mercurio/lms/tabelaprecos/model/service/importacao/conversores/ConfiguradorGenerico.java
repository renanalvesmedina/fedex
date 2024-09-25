package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class ConfiguradorGenerico extends ConfiguradorTabelaPrecoParcela {

	private final SubConfiguradorGenerico subConfigurador;

	public ConfiguradorGenerico(Map<String, Object> dependencias) {
		super(dependencias);
		this.subConfigurador = new SubConfiguradorGenerico(buscador);
	}

	@Override
	public List<TabelaPrecoParcela> configura(List<ComponenteImportacao> componentes) {
		List<TabelaPrecoParcela> resultado = new ArrayList<TabelaPrecoParcela>();
		if (CollectionUtils.isEmpty(componentes)) {
			return Collections.emptyList();
		}
		for (ComponenteImportacao componente : componentes) {
			TabelaPrecoParcela item = configura(componente);
			if (item == null) {
				continue;
			}
			resultado.add(item);
		}
		return resultado;
	}

	private TabelaPrecoParcela configura(ComponenteImportacao componente) {
		try {
			return subConfigurador.configura(componente);
		} catch (ImportacaoException e) {
			this.capturaErro(e);
		}
		return null;
	}

}
