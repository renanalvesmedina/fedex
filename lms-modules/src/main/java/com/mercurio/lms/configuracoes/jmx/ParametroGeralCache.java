/**
 * 
 */
package com.mercurio.lms.configuracoes.jmx;

import java.util.List;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.mercurio.lms.configuracoes.model.ParametroGeral;

/**
 * Implementação padrão da cache distribuida de parametros gerais do LMS.
 * 
 * @author Luis Carlos Poletto
 * 
 */
@ManagedResource(objectName = "bean:name=LMS-ParametroGeralCache", log = false)
public class ParametroGeralCache implements ParametroGeralCacheMBean {

	private static final long serialVersionUID = 6609504519237664388L;
	
	private List<ParametroGeral> parametrosGerais;
	private boolean blRefresh;

	public ParametroGeralCache() {
		blRefresh = true;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.lms.configuracoes.jmx.ParametroGeralCacheMBean#getParametrosGerais()
	 */
	@ManagedOperation(description = "Retrieves cache data.")
	public List<ParametroGeral> getParametrosGerais() {
		return parametrosGerais;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.lms.configuracoes.jmx.ParametroGeralCacheMBean#setParametrosGerais(java.util.List)
	 */
	@ManagedOperation(description = "Updates the cache data with the received information.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "parametrosGerais", description = "New cache data") })
	public void setParametrosGerais(List<ParametroGeral> parametrosGerais) {
		this.parametrosGerais = parametrosGerais;
		blRefresh = false;
	}

	@ManagedOperation(description = "Show the value for the Parametro Geral in the cache.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "name", description = "Parametro Geral name") })
	public String getValue(String name) {
		if (parametrosGerais == null) {
			return "Valores da cache não inicializados.";
		}
		for (ParametroGeral parametroGeral : parametrosGerais) {
			if (parametroGeral.getNmParametroGeral().equals(name)) {
				return parametroGeral.getDsConteudo();
			}
		}
		return "Parametro geral não encontrado.";
	}

	@ManagedOperation(description = "Show all values in the cache.")
	public String listValues() {
		if (parametrosGerais == null) {
			return "Valores da cache não inicializados.";
		}
		StringBuilder result = new StringBuilder();
		for (ParametroGeral parametroGeral : parametrosGerais) {
			result.append(parametroGeral.getNmParametroGeral());
			result.append(": ");
			result.append(parametroGeral.getDsConteudo());
			result.append("\n");
		}
		return result.toString();
	}

	@ManagedOperation(description = "Checks if this cache needs refresh.")
	public boolean needRefresh() {
		return blRefresh;
	}

	@ManagedOperation(description = "Clears the current cache data and change the update flag to true")
	public void refresh() {
		blRefresh = true;
		parametrosGerais = null;
	}
}
