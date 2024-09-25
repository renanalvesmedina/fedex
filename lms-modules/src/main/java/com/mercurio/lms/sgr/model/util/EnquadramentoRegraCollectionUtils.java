package com.mercurio.lms.sgr.model.util;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_ORIGEM;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * LMS-6850 - Classe utilitária para filtro de {@link EnquadramentoRegra} nos
 * procedimentos do Plano de Gerenciamento de Risco. Utiliza basicamente
 * especializações de {@link Transformer} e {@link Predicate} além dos métodos
 * estáticos {@link CollectionUtils#exists(Collection, Predicate)} e
 * {@link CollectionUtils#filter(Collection, Predicate)} para a identificação de
 * {@link EnquadramentoRegra} com conteúdo específico ou não determinado.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 * @see TransformerUtils
 * @see PredicateUtils
 */
public final class EnquadramentoRegraCollectionUtils {

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando no relacionamento
	 * one-to-many {@code clienteEnquadramentos} se o {@link Cliente} está de
	 * acordo com o parâmetro {@code idCliente} e se o atributo
	 * {@code tpIntegranteFrete} é compatível com o parâmetro
	 * {@code tpIntegranteFrete}. Se houver {@link EnquadramentoRegra}
	 * compatível a {@link Collection} é filtrada e o método retorna
	 * {@code true}, caso contrário a {@link Collection} não é alterada e o
	 * método retorna {@code false}.
	 * 
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * @param idCliente
	 *            Id do {@link Cliente}.
	 * @param tpIntegranteFrete
	 *            Tipo de integrante do frete.
	 * @return {@code true} se houver {@link EnquadramentoRegra} compatível,
	 *         {@code false} caso contrário.
	 * 
	 * @see EnquadramentoRegraCollectionUtilsTest
	 */
	public static boolean filterClienteEnquadramento(
			Collection<EnquadramentoRegra> regras, Long idCliente, String tpIntegranteFrete) {
		if (idCliente == null) {
			return false;
		}
		Predicate equalPredicate = PredicateUtils.transformedPredicate(
				TransformerUtils.invokerTransformer("getClienteEnquadramentos"),
				collectionPredicate(
						PredicateUtils.andPredicate(
								PredicateUtils.transformedPredicate(
										TransformerUtils.chainedTransformer(
												TransformerUtils.invokerTransformer("getCliente"),
												TransformerUtils.invokerTransformer("getIdCliente")
										),
										PredicateUtils.equalPredicate(idCliente)
								),
								PredicateUtils.transformedPredicate(
										TransformerUtils.chainedTransformer(
												TransformerUtils.invokerTransformer("getTpIntegranteFrete"),
												TransformerUtils.invokerTransformer("getValue")
										),
										PredicateUtils.orPredicate(
												PredicateUtils.equalPredicate(tpIntegranteFrete),
												PredicateUtils.equalPredicate(TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO)
										)
								)
						)
				)
		);
		if (CollectionUtils.exists(regras, equalPredicate)) {
			CollectionUtils.filter(regras, equalPredicate);
			return true;
		}
		return false;
	}

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando se o relacionamento
	 * one-to-many {@code clienteEnquadramentos} não contém informações, ou
	 * seja, {@link EnquadramentoRegra}s que não são para qualquer
	 * {@link Cliente} específico.
	 * 
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * 
	 * @see EnquadramentoRegraCollectionUtilsTest
	 */
	public static void filterClienteEnquadramento(Collection<EnquadramentoRegra> regras) {
		Predicate emptyPredicate = PredicateUtils.transformedPredicate(
				TransformerUtils.invokerTransformer("getClienteEnquadramentos"),
				PredicateUtils.nullIsTruePredicate(
						PredicateUtils.invokerPredicate("isEmpty")
				)
		);
		CollectionUtils.filter(regras, emptyPredicate);
	}

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando o atributo tipo
	 * {@link DomainValue} especificado pelo argumento {@code dominio} se existe
	 * valor de acordo com o parâmetro {@code valorDominio}. Se houver
	 * {@link EnquadramentoRegra} com o atributo e valor especificado a
	 * {@link Collection} é filtrada e o método retorna {@code true}, caso
	 * contrário a {@link Collection} é filtrada para os itens em que o valor é
	 * igual a {@code null} e o método retorna {@code false}.
	 * 
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * @param dominio
	 *            Nome do atributo tipo {@link DomainValue}.
	 * @param valorDominio
	 *            Valor do atributo a filtrar.
	 * @return {@code true} se houver {@link EnquadramentoRegra} compatível,
	 *         {@code false} caso contrário.
	 * 
	 * @see EnquadramentoRegraCollectionUtilsTest
	 */
	public static boolean filterValorDominio(Collection<EnquadramentoRegra> regras, String dominio, String valorDominio) {
		Transformer transformer = TransformerUtils.invokerTransformer("get" + dominio);
		Predicate equalPredicate = PredicateUtils.transformedPredicate(
				TransformerUtils.chainedTransformer(
						transformer,
						TransformerUtils.invokerTransformer("getValue")
				),
				PredicateUtils.equalPredicate(valorDominio)
		);
		Predicate nullPredicate = PredicateUtils.transformedPredicate(
				transformer,
				PredicateUtils.nullPredicate()
		);
		if (valorDominio != null && CollectionUtils.exists(regras, equalPredicate)) {
			CollectionUtils.filter(regras, equalPredicate);
			return true;
		}
		CollectionUtils.filter(regras, nullPredicate);
		return false;
	}

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando se o atributo especificado
	 * pelo argumento {@code dominio} está de acordo com o parâmetro
	 * {@code idAtributo}. Se houver {@link EnquadramentoRegra} com o atributo
	 * compatível a {@link Collection} é filtrada e o método retorna
	 * {@code true}, caso contrário a {@link Collection} é filtrada para os
	 * itens em que o atributo é {@code null} e o método retorna {@code false}.
	 * 
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * @param atributo
	 *            Nome do atributo a filtrar.
	 * @param idAtributo
	 *            Id para filtro pelo atributo.
	 * @return {@code true} se houver {@link EnquadramentoRegra} compatível,
	 *         {@code false} caso contrário.
	 * 
	 * @see EnquadramentoRegraCollectionUtilsTest
	 */
	public static boolean filterIdAtributo(
			Collection<EnquadramentoRegra> regras, String atributo, Long idAtributo) {
		Transformer transformer = TransformerUtils.chainedTransformer(
				TransformerUtils.invokerTransformer("get" + atributo),
				TransformerUtils.invokerTransformer("getId" + atributo)
		);
		Predicate equalPredicate = PredicateUtils.transformedPredicate(
				transformer,
				PredicateUtils.equalPredicate(idAtributo)
		);
		Predicate nullPredicate = PredicateUtils.transformedPredicate(
				transformer,
				PredicateUtils.nullPredicate()
		);
		if (idAtributo != null && CollectionUtils.exists(regras, equalPredicate)) {
			CollectionUtils.filter(regras, equalPredicate);
			return true;
		}
		CollectionUtils.filter(regras, nullPredicate);
		return false;
	}

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando se o atributo especificado
	 * pelo argumento {@code dominio} está de acordo com o parâmetro
	 * {@code idAtributo}. Se houver {@link EnquadramentoRegra} com o atributo
	 * compatível a {@link Collection} é filtrada e o método retorna
	 * {@code true}, caso contrário a {@link Collection} é filtrada para os
	 * itens que o atributo é {@code null} e o método retorna {@code false}.
	 * 
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * @param tpCriterio
	 *            Tipo de critério de enquadramento.
	 * @param criterio
	 *            Nome do critério de enquadramento.
	 * @param idCriterio
	 *            Id para filtro pelo critério de enquadramento.
	 * @return {@code true} se houver {@link EnquadramentoRegra} compatível,
	 *         {@code false} caso contrário.
	 * 
	 * @see EnquadramentoRegraCollectionUtilsTest
	 */
	public static boolean filterCriterioEnquadramento(
			Collection<EnquadramentoRegra> regras, String tpCriterio, String criterio, Long idCriterio) {
		String methodName = "get" + criterio + "Enquadramentos" + (TP_CRITERIO_ORIGEM.equals(tpCriterio) ? "Origem" : "Destino");
		Transformer transformer = TransformerUtils.invokerTransformer(methodName);
		Predicate equalPredicate = PredicateUtils.transformedPredicate(
				transformer,
				collectionPredicate(
						PredicateUtils.transformedPredicate(
								TransformerUtils.invokerTransformer("getId" + criterio),
								PredicateUtils.equalPredicate(idCriterio)
						)
				)
		);
		Predicate emptyPredicate = PredicateUtils.transformedPredicate(
				transformer,
				PredicateUtils.nullIsTruePredicate(
						PredicateUtils.invokerPredicate("isEmpty")
				)
		);
		if (CollectionUtils.exists(regras, equalPredicate)) {
			CollectionUtils.filter(regras, equalPredicate);
			return true;
		}
		CollectionUtils.filter(regras, emptyPredicate);
		return false;
	}

	/**
	 * Método auxiliar para criação de um {@link Predicate} a ser aplicado em
	 * {@link Collection}s dentro de outro {@link Predicate}.
	 * 
	 * @param predicate
	 *            {@link Predicate} para composição.
	 * @return {@link Predicate} para aplicação em {@link Collection}s.
	 */
	private static Predicate collectionPredicate(final Predicate predicate) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Collection<?> collection = (Collection<?>) object;
				return CollectionUtils.exists(collection, predicate);
			}
		};
	}

	private EnquadramentoRegraCollectionUtils() {
		throw new AssertionError();
	}

}
