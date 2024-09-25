package com.mercurio.lms.sgr.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * LMS-6850 - Classe utilitária para manipulação de {@link Collection}s,
 * {@link Map}s e operações sobre {@link BigDecimal}s.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 */
public final class PlanoGerenciamentoRiscoCollectionUtils {

	/**
	 * Método auxiliar para inserção de elemento em {@link Collection},
	 * inicializando a {@link Collection} caso igual a {@code null}.
	 * 
	 * @param collection
	 * @param element
	 * @return
	 */
	public static <E> Collection<E> initAndAdd(Collection<E> collection, E element) {
		Collection<E> result = collection != null ? collection : new ArrayList<E>();
		result.add(element);
		return result;
	}

	/**
	 * Método auxiliar para inserção de elemento em {@link Collection} dentro de
	 * {@link Map}, inicializando o {@link Map} caso igual a {@code null}.
	 * 
	 * @param map
	 * @param key
	 * @param element
	 * @return
	 */
	public static <K, E> Map<K, Collection<E>> initAndPut(Map<K, Collection<E>> map, K key, E element) {
		Map<K, Collection<E>> result = map != null ? map : new HashMap<K, Collection<E>>();
		Collection<E> collection = result.get(key);
		if (collection == null) {
			collection = new ArrayList<E>();
			result.put(key, collection);
		}
		collection.add(element);
		return result;
	}

	/**
	 * Método auxiliar para recuperação de {@link Collections} dentro de
	 * {@link Map}, retornando uma {@link Collection} vazia caso não exista o
	 * elemento.
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K, E> Collection<E> getCollection(Map<K, Collection<E>> map, K key) {
		if (map != null && map.containsKey(key)) {
			return map.get(key);
		}
		return Collections.emptyList();
	}

	/**
	 * Método auxiliar para recuperação de {@link BigDecimal} dentro de
	 * {@link Map}, retornando o valor zero caso não exista o elemento ou o
	 * {@link Map} seja igual a {@code null}.
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K> BigDecimal getBigDecimal(Map<K, BigDecimal> map, K key) {
		if (map != null && map.containsKey(key)) {
			return map.get(key);
		}
		return BigDecimal.ZERO;
	}

	/**
	 * Método auxiliar para adição de {@link BigDecimal} dentro de {@link Map},
	 * inicializando o {@link Map} caso igual a {@code null}.
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	public static <K> Map<K, BigDecimal> initAndSum(Map<K, BigDecimal> map, K key, BigDecimal value) {
		Map<K, BigDecimal> result = map != null ? map : new HashMap<K, BigDecimal>();
		result.put(key, getBigDecimal(result, key).add(value != null ? value : BigDecimal.ZERO));
		return result;
	}

	private PlanoGerenciamentoRiscoCollectionUtils() {
		throw new AssertionError();
	}

}
