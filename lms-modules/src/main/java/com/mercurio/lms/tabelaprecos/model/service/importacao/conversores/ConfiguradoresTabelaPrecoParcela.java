package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;

public class ConfiguradoresTabelaPrecoParcela {

	private Map<TipoComponente, Class<? extends ConfiguradorTabelaPrecoParcela>> classesConfiguradores = new HashMap<TipoComponente, Class<? extends ConfiguradorTabelaPrecoParcela>>();
	private Map<TipoComponente, ConfiguradorTabelaPrecoParcela> configuradores = new HashMap<TipoComponente, ConfiguradorTabelaPrecoParcela>();
	private Map<String, Object> dependenciasParaConfigurador = new HashMap<String, Object>();
	static final String BUSCADOR_DEPENDENCIAS = "BuscadorDependencias";
	static final String TABELA_PRECO = "TabelaPreco";
	static final String FORNECEDORES_ROTAVSTARIFA = "FornecedoresTarifaVsRota";

	public ConfiguradoresTabelaPrecoParcela(BuscadorDependencia buscador, TabelaPreco tabela, FornecedoresRotaTarifa fornecedoresChave) {
		dependenciasParaConfigurador.put(BUSCADOR_DEPENDENCIAS, buscador);
		dependenciasParaConfigurador.put(TABELA_PRECO, tabela);
		dependenciasParaConfigurador.put(FORNECEDORES_ROTAVSTARIFA, fornecedoresChave);
		classesConfiguradores.put(TipoComponente.PRECO_FRETE, ConfiguradorPrecoFrete.class);
		classesConfiguradores.put(TipoComponente.FAIXA_PROGRESSIVA, ConfiguradorFaixaProgressiva.class);
		classesConfiguradores.put(TipoComponente.GENERICO, ConfiguradorGenerico.class);
	}


	public ConfiguradorTabelaPrecoParcela para(TipoComponente tipoComponente) {
		if (this.configuradores.containsKey(tipoComponente)) {
			return this.configuradores.get(tipoComponente);
		}
		if (this.classesConfiguradores.containsKey(tipoComponente)) {
			Class<? extends ConfiguradorTabelaPrecoParcela> classe = this.classesConfiguradores.get(tipoComponente);
			ConfiguradorTabelaPrecoParcela configurador = criaConfigurador(classe);
			this.configuradores.put(tipoComponente, configurador);
			return configurador;
		}
		throw new IllegalArgumentException(String.format("O tipo de parcela ' %s' não tem configurador correspondente.", tipoComponente));
	}

	private ConfiguradorTabelaPrecoParcela criaConfigurador(Class<? extends ConfiguradorTabelaPrecoParcela> classe) {
		ConfiguradorTabelaPrecoParcela configurador = null;
		try {
			Constructor<? extends ConfiguradorTabelaPrecoParcela> construtor = classe.getConstructor(Map.class);
			construtor.setAccessible(true);
			configurador = construtor.newInstance(dependenciasParaConfigurador); 
		} catch (InstantiationException e) {
			throw new ADSMException(e);
		} catch (IllegalAccessException e) {
			throw new ADSMException(e);
		} catch (SecurityException e) {
			throw new ADSMException(e);
		} catch (NoSuchMethodException e) {
			throw new ADSMException(e);
		} catch (IllegalArgumentException e) {
			throw new ADSMException(e);
		} catch (InvocationTargetException e) {
			throw new ADSMException(e);
		}

		return configurador;
	}

}
