/**
 * 
 */
package com.mercurio.lms.configuracoes.jmx;

import com.mercurio.lms.configuracoes.model.Imagem;
import java.io.Serializable;
import java.util.List;

/**
 * Interface que define os metodos para utilização de uma cache distribuida de
 * imagens do sistema.
 * 
 * @author  Gilmar Costa
 */
public interface ImagemCacheMBean extends Serializable {

	/**
	 * Busca a lista de imagens configurados do sistema.
	 * 
	 * @return lista de imagens cacheados
	 */
	List<Imagem> getImagens();

	/**
	 * Atualiza a lista de imagens cacheados do sistema para os dados
	 * recebidos.
	 * 
	 * @param imagens
	 *            nova lista de imagens
	 */
	void setImagens(List<Imagem> imagens);

	/**
	 * Busca o valor do imagem na cache.
	 * 
	 * @param name
	 *            nome da imagem a buscar o valor
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
