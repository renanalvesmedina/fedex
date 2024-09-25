/**
 * 
 */
package com.mercurio.lms.configuracoes.jmx;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.configuracoes.model.ParametroGeral;

/**
 * Interface que define os metodos para utilização de uma cache distribuida de
 * parametros gerais do sistema.
 * 
 * @author Luis Carlos Poletto
 */
public interface ParametroGeralCacheMBean extends Serializable {

	/**
	 * Busca a lista de parametros configurados do sistema.
	 * 
	 * @return lista de parametros cacheados
	 */
	List<ParametroGeral> getParametrosGerais();

	/**
	 * Atualiza a lista de parametros gerais cacheados do sistema para os dados
	 * recebidos.
	 * 
	 * @param parametrosGerais
	 *            nova lista de parametros gerais
	 */
	void setParametrosGerais(List<ParametroGeral> parametrosGerais);

	/**
	 * Busca o valor do parametro na cache.
	 * 
	 * @param name
	 *            nome do parametro a buscar o valor
	 * @return valor encontrado
	 */
	String getValue(String name);

	/**
	 * Lista todos os valores contidos na cache.
	 * 
	 * @return lista de valores
	 */
	String listValues();

	/**
	 * Verifica se a cache precisa de atualização.
	 * 
	 * @return <code>true</code> caso precise e <code>false</code> caso
	 *         contrário
	 */
	boolean needRefresh();

	/**
	 * Altera a variável de controle de atualização da cache para
	 * <code>true</code>.
	 */
	void refresh();
}
