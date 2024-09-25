package com.mercurio.lms.sgr.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.sgr.model.VirusCarga;

/**
 * LMS-6874 - DAO para {@link VirusCarga}.
 * 
 * @author caetano.matos@cwi.com.br (Caetano da Silva Matos)
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class VirusCargaDAO extends BaseCrudDao<VirusCarga, Long> {

	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return VirusCarga.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("cliente", FetchMode.JOIN);
		fetchModes.put("cliente.pessoa", FetchMode.JOIN);
	}

	/**
	 * Busca quantidade de {@link VirusCarga} relacionados a determinado
	 * {@link ControleCarga}. O parâmetro {@code tpCancelado} determina o filtro
	 * sobre manifestos e eventos de coleta, desconsiderando somente os
	 * cancelados (se igual a {@code true}) ou cancelados, finalizados, etc (se
	 * igual a {@code false}).
	 * 
	 * @param idControleCarga
	 * @param tpCancelado
	 * @return
	 * 
	 * @see #makeQueryByControleCarga()
	 * @see #makeParametersByControleCarga(Long)
	 */
	public Integer getRowCountByControleCarga(Long idControleCarga, Boolean tpCancelado) {
		return getAdsmHibernateTemplate().getRowCountForQuery(
				makeQueryByControleCarga(tpCancelado), makeParametersByControleCarga(idControleCarga));
	}

	/**
	 * Busca {@link VirusCarga}s relacionados a determinado
	 * {@link ControleCarga}. O parâmetro {@code tpCancelado} determina o filtro
	 * sobre manifestos e eventos de coleta, desconsiderando somente os
	 * cancelados (se igual a {@code true}) ou cancelados, finalizados, etc (se
	 * igual a {@code false}).
	 * 
	 * @param idControleCarga
	 * @param tpCancelado
	 * @return
	 * 
	 * @see #makeQueryByControleCarga()
	 * @see #makeParametersByControleCarga(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<VirusCarga> findByControleCarga(Long idControleCarga, Boolean tpCancelado) {
		return getAdsmHibernateTemplate().findByNamedParam(
				makeQueryByControleCarga(tpCancelado), makeParametersByControleCarga(idControleCarga));
	}

	/**
	 * Prepara query HQL para busca de {@link VirusCarga} relacionados a
	 * determinado {@link ControleCarga} pelo parâmetro {@code idControleCarga},
	 * utilizando sub-queries para coletas c/ nota fiscal conhecimento, coletas
	 * c/ nota fiscal coleta, entregas, viagens com manifesto e viagens com
	 * pré-manifesto.
	 * 
	 * @param tpCancelado
	 * @return Query HQL para busca de {@link VirusCarga}s incluindo parâmetro
	 *         {@code idControleCarga}.
	 */
	private String makeQueryByControleCarga(Boolean tpCancelado) {
		String tpStatusManifesto = BooleanUtils.isTrue(tpCancelado) ? "'CA'" : "'CA', 'DC', 'ED', 'FE', 'PM'";
		String tpEventoColeta = BooleanUtils.isTrue(tpCancelado) ? "'CA'" : "'CA', 'EX', 'FD', 'ID'";
		StringBuilder hql = new StringBuilder()
				.append("SELECT virusCarga ")
				.append("FROM VirusCarga virusCarga ")
				.append("JOIN FETCH virusCarga.cliente cliente ")
				.append("JOIN FETCH cliente.pessoa pessoa ")
				.append("WHERE virusCarga.idVirusCarga IN ( ")
				/*
					--coleta (nota fiscal conhecimento)
					SELECT DISTINCT vc.id_virus_carga
					FROM controle_carga cc,
					    manifesto_coleta mc,
					    pedido_coleta pc,
					    detalhe_coleta dc,
					    docto_servico ds,
					    nota_fiscal_conhecimento nfds,
					    virus_carga vc
					WHERE cc.id_controle_carga                   = &id_controle_carga
					AND cc.id_controle_carga                     = mc.id_controle_carga
					AND mc.id_manifesto_coleta                   = pc.id_manifesto_coleta
					AND pc.id_pedido_coleta                      = dc.id_pedido_coleta
					AND dc.id_docto_servico                      = ds.id_docto_servico
					AND ds.id_docto_servico                      = nfds.id_conhecimento
					AND dc.id_detalhe_coleta                     = nfc.id_detalhe_coleta
					AND nfds.id_cliente                          = vc.id_cliente
					AND nfds.nr_nota_fiscal                      = vc.nr_nota_fiscal
					AND nfds.ds_serie                            = vc.ds_serie
					AND mc.tp_status_manifesto_coleta       NOT IN ('CA', 'FE', 'ED')
					AND EXISTS
						(SELECT 1
						FROM evento_coleta ec
						WHERE ec.tp_evento_coleta           NOT IN ('CA', 'EX', 'FD', 'ID')
						AND ec.id_pedido_coleta                  = pc.id_pedido_coleta
						)
					)
				 */
				.append("    SELECT vc.idVirusCarga ")
				.append("    FROM ControleCarga cc, Conhecimento c, VirusCarga vc ")
				.append("    JOIN cc.manifestoColetas mc ")
				.append("    JOIN mc.pedidoColetas pc ")
				.append("    JOIN pc.detalheColetas dc ")
				.append("    JOIN pc.eventoColetas ec ")
				.append("    JOIN dc.doctoServico ds ")
				.append("    JOIN c.notaFiscalConhecimentos nfds ")
				.append("    WHERE cc.idControleCarga            = :idControleCarga ")
				.append("    AND ds.idDoctoServico               = c.idDoctoServico ")
				.append("    AND nfds.cliente                    = vc.cliente ")
				.append("    AND nfds.nrNotaFiscal               = vc.nrNotaFiscal ")
				.append("    AND NVL(nfds.dsSerie, 'null')       = NVL(vc.dsSerie, 'null') ")
				.append("    AND mc.tpStatusManifestoColeta NOT IN (" + tpStatusManifesto + ") ")
				.append("    AND ec.tpEventoColeta          NOT IN (" + tpEventoColeta + ") ")
				.append(") ")
				.append("OR virusCarga.idVirusCarga IN ( ")
				/*
					--coleta (nota fiscal coleta)
					SELECT DISTINCT vc.id_virus_carga
					FROM controle_carga cc,
					    manifesto_coleta mc,
					    pedido_coleta pc,
					    detalhe_coleta dc,
					    nota_fiscal_coleta nfc,
					    virus_carga vc
					WHERE cc.id_controle_carga                   = &id_controle_carga
					AND cc.id_controle_carga                     = mc.id_controle_carga
					AND mc.id_manifesto_coleta                   = pc.id_manifesto_coleta
					AND pc.id_pedido_coleta                      = dc.id_pedido_coleta
					AND dc.id_detalhe_coleta                     = nfc.id_detalhe_coleta
					AND pc.id_cliente                            = vc.id_cliente
					AND (nfc.nr_nota_fiscal                      = vc.nr_nota_fiscal
					 OR nfc.nr_chave                             = vc.nr_chave)
					AND mc.tp_status_manifesto_coleta       NOT IN ('CA', 'FE', 'ED')
					AND EXISTS
						(SELECT 1
						FROM evento_coleta ec
						WHERE ec.tp_evento_coleta           NOT IN ('CA', 'EX', 'FD', 'ID')
						AND ec.id_pedido_coleta                  = pc.id_pedido_coleta
						)
					)
				 */
				.append("    SELECT vc.idVirusCarga ")
				.append("    FROM ControleCarga cc, VirusCarga vc ")
				.append("    JOIN cc.manifestoColetas mc ")
				.append("    JOIN mc.pedidoColetas pc ")
				.append("    JOIN pc.detalheColetas dc ")
				.append("    JOIN pc.eventoColetas ec ")
				.append("    JOIN dc.notaFiscalColetas nfc ")
				.append("    WHERE cc.idControleCarga            = :idControleCarga ")
				.append("    AND pc.cliente                      = vc.cliente ")
				.append("    AND (nfc.nrNotaFiscal               = vc.nrNotaFiscal ")
				.append("     OR nfc.nrChave                     = vc.nrChave) ")
				.append("    AND mc.tpStatusManifestoColeta NOT IN (" + tpStatusManifesto + ") ")
				.append("    AND ec.tpEventoColeta          NOT IN (" + tpEventoColeta + ") ")
				.append(") ")
				.append("OR virusCarga.idVirusCarga IN ( ")
				/*
					--entrega
					SELECT vc.id_virus_carga
					FROM controle_carga cc,
					    manifesto m,
					    manifesto_entrega me,
					    manifesto_entrega_documento med,
					    docto_servico ds,
					    nota_fiscal_conhecimento nfc,
					    virus_carga vc
					WHERE cc.id_controle_carga                   = &id_controle_carga
					AND cc.id_controle_carga                     = m.id_controle_carga
					AND m.id_manifesto                           = me.id_manifesto_entrega
					AND me.id_manifesto_entrega                  = med.id_manifesto_entrega
					AND med.id_docto_servico                     = ds.id_docto_servico
					AND ds.id_docto_servico                      = nfc.id_conhecimento
					AND ((nfc.nr_chave                           = vc.nr_chave)
					OR (nfc.nr_nota_fiscal                       = vc.nr_nota_fiscal
					AND nfc.ds_serie                             = vc.ds_serie
					AND nfc.id_cliente                           = vc.id_cliente))
					AND m.tp_status_manifesto               NOT IN ('CA', 'DC', 'ED', 'FE')
					AND ds.tp_documento_servico                 IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE')
				 */
				.append("    SELECT vc.idVirusCarga ")
				.append("    FROM ControleCarga cc, Conhecimento c, VirusCarga vc ")
				.append("    JOIN cc.manifestos m ")
				.append("    JOIN m.manifestoEntrega.manifestoEntregaDocumentos med ")
				.append("    JOIN med.doctoServico ds ")
				.append("    JOIN c.notaFiscalConhecimentos nfc ")
				.append("    WHERE cc.idControleCarga            = :idControleCarga ")
				.append("    AND ds.idDoctoServico               = c.idDoctoServico ")
				.append("    AND ((nfc.nrChave                   = vc.nrChave) ")
				.append("    OR (nfc.nrNotaFiscal                = vc.nrNotaFiscal ")
				.append("    AND NVL(nfc.dsSerie, 'null')        = NVL(vc.dsSerie, 'null') ")
				.append("    AND nfc.cliente                     = vc.cliente)) ")
				.append("    AND m.tpStatusManifesto        NOT IN (" + tpStatusManifesto + ") ")
				.append("    AND ds.tpDocumentoServico          IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
				.append(") ")
				.append("OR virusCarga.idVirusCarga IN ( ")
				/*
					--viagem (emitida)
					SELECT vc.id_virus_carga
					FROM controle_carga cc,
					    manifesto m,
					    manifesto_viagem_nacional mvn,
					    manifesto_nacional_cto mnc,
					    conhecimento c,
					    docto_servico ds,
					    nota_fiscal_conhecimento nfc,
					    virus_carga vc
					WHERE cc.id_controle_carga                   = &id_controle_carga
					AND cc.id_controle_carga                     = m.id_controle_carga
					AND m.id_manifesto                           = mvn.id_manifesto_viagem_nacional
					AND mvn.id_manifesto_viagem_nacional         = mnc.id_manifesto_viagem_nacional
					AND mnc.id_conhecimento                      = c.id_conhecimento
					AND c.id_conhecimento                        = ds.id_docto_servico
					AND c.id_conhecimento                        = nfc.id_conhecimento
					AND ((nfc.nr_chave                           = vc.nr_chave)
					OR (nfc.nr_nota_fiscal                       = vc.nr_nota_fiscal
					AND nfc.ds_serie                             = vc.ds_serie
					AND nfc.id_cliente                           = vc.id_cliente))
					AND m.tp_status_manifesto               NOT IN ('CA', 'DC', 'ED', 'FE', 'PM')
					AND c.tp_documento_servico                  IN ('CTR', 'CTE', 'NFT', 'NTE')
				 */
				.append("    SELECT vc.idVirusCarga ")
				.append("    FROM ControleCarga cc, VirusCarga vc ")
				.append("    JOIN cc.manifestos m ")
				.append("    JOIN m.manifestoViagemNacional.manifestoNacionalCtos mnc ")
				.append("    JOIN mnc.conhecimento c ")
				.append("    JOIN c.notaFiscalConhecimentos nfc ")
				.append("    WHERE cc.idControleCarga            = :idControleCarga ")
				.append("    AND ((nfc.nrChave                   = vc.nrChave) ")
				.append("    OR (nfc.nrNotaFiscal                = vc.nrNotaFiscal ")
				.append("    AND NVL(nfc.dsSerie, 'null')        = NVL(vc.dsSerie, 'null') ")
				.append("    AND nfc.cliente                     = vc.cliente)) ")
				.append("    AND m.tpStatusManifesto        NOT IN (" + tpStatusManifesto + ") ")
				.append("    AND c.tpDocumentoServico           IN ('CTR', 'CTE', 'NFT', 'NTE') ")
				.append(") ")
				.append("OR virusCarga.idVirusCarga IN ( ")
				/*
					--viagem (não emitida)
					SELECT vc.id_virus_carga
					FROM controle_carga cc,
					    manifesto m,
					    pre_manifesto_documento pmd,
					    docto_servico ds,
					    conhecimento c,
					    nota_fiscal_conhecimento nfc,
					    virus_carga vc
					WHERE cc.id_controle_carga                   = &id_controle_carga
					AND cc.id_controle_carga                     = m.id_controle_carga
					AND m.id_manifesto                           = pmd.id_manifesto
					AND pmd.id_docto_servico                     = ds.id_docto_servico
					AND ds.id_docto_servico                      = c.id_conhecimento
					AND c.id_conhecimento                        = nfc.id_conhecimento
					AND ((nfc.nr_chave                           = vc.nr_chave)
					OR (nfc.nr_nota_fiscal                       = vc.nr_nota_fiscal
					AND nfc.ds_serie                             = vc.ds_serie
					AND nfc.id_cliente                           = vc.id_cliente))
					AND m.tp_status_manifesto               NOT IN ('CA', 'DC', 'ED', 'FE')
					AND m.dh_emissao_manifesto                  IS NULL
					AND c.tp_documento_servico                  IN ('CTR', 'CTE', 'NFT', 'NTE')
				 */
				.append("    SELECT vc.idVirusCarga ")
				.append("    FROM ControleCarga cc, Conhecimento c, VirusCarga vc ")
				.append("    JOIN cc.manifestos m ")
				.append("    JOIN m.preManifestoDocumentos pmd ")
				.append("    JOIN pmd.doctoServico ds ")
				.append("    JOIN c.notaFiscalConhecimentos nfc ")
				.append("    WHERE cc.idControleCarga            = :idControleCarga ")
				.append("    AND ds.idDoctoServico               = c.idDoctoServico ")
				.append("    AND ((nfc.nrChave                   = vc.nrChave) ")
				.append("    OR (nfc.nrNotaFiscal                = vc.nrNotaFiscal ")
				.append("    AND NVL(nfc.dsSerie, 'null')        = NVL(vc.dsSerie, 'null') ")
				.append("    AND nfc.cliente                     = vc.cliente)) ")
				.append("    AND m.tpStatusManifesto        NOT IN (" + tpStatusManifesto + ") ")
				.append("    AND m.dhEmissaoManifesto           IS NULL ")
				.append("    AND ds.tpDocumentoServico          IN ('CTR', 'CTE', 'NFT', 'NTE') ")
				.append(") ");
		return hql.toString();
	}

	/**
	 * Prepara {@link Map} para busca parametrizada por {@code idControleCarga}.
	 * 
	 * @param idControleCarga
	 *            Id de {@link ControleCarga}.
	 * @return {@link Map} contendo {@code idControleCarga}.
	 */
	private Map<String, Object> makeParametersByControleCarga(Long idControleCarga) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);
		return parameters;
	}

	/**
	 * Busca quantidade de {@link VirusCarga} conforme parâmetros de filtro da
	 * tela "Manter Vírus de Carga".
	 * 
	 * @param criteria
	 * @return
	 * 
	 * @see #makeQueryByFilter(TypedFlatMap)
	 */
	public Integer getRowCountByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeQueryByFilter(criteria), criteria);
	}
	
	/**
	 * Busca instâncias de {@link VirusCarga} conforme parâmetros de filtro da
	 * tela "Manter Vírus de Carga".
	 * 
	 * @param criteria
	 * @return
	 * 
	 * @see #makeQueryByFilter(TypedFlatMap)
	 */
	@SuppressWarnings("unchecked")
	public List<VirusCarga> findByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeQueryByFilter(criteria), criteria);
	}

	/**
	 * Prepara query HQL para busca de {@link VirusCarga} relacionados a
	 * determinado {@link ControleCarga}, utilizando sub-queries para coletas,
	 * entregas, viagens com manifesto e viagens com pré-manifesto. Os
	 * relacionamentos com {@link Cliente} e {@link Pessoa} são recuperados
	 * imediatamente.
	 * 
	 * @return Query HQL para busca de {@link VirusCarga}s.
	 * 
	 * @param criteria
	 * @return
	 */
	private String makeQueryByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM VirusCarga vc ")
				.append("JOIN FETCH vc.cliente c ")
				.append("JOIN FETCH c.pessoa p ")
				.append("WHERE 1 = 1 ");
		appendIfContains(criteria, "dhAtivacaoInicial", hql, "AND vc.dhAtivacao.value >= :dhAtivacaoInicial ");
		appendIfContains(criteria, "dhAtivacaoFinal", hql, "AND vc.dhAtivacao.value <= :dhAtivacaoFinal ");
		appendIfContains(criteria, "dhInclusaoInicial", hql, "AND vc.dhInclusao.value >= :dhInclusaoInicial ");
		appendIfContains(criteria, "dhInclusaoFinal", hql, "AND vc.dhInclusao.value <= :dhInclusaoFinal ");
		appendIfContains(criteria, "idCliente", hql, "AND c.idCliente = :idCliente ");
		appendIfContains(criteria, "nrChave", hql, "AND vc.nrChave = :nrChave ");
		appendIfContains(criteria, "nrIscaCarga", hql, "AND vc.nrIscaCarga = :nrIscaCarga ");
		appendIfContains(criteria, "nrNotaFiscal", hql, "AND vc.nrNotaFiscal = :nrNotaFiscal ");
		appendIfContains(criteria, "dsSerie", hql, "AND vc.dsSerie = :dsSerie ");
		appendIfContains(criteria, "nrVolume", hql, "AND vc.nrVolume = :nrVolume ");
		return hql.toString();
	}

	/**
	 * Método auxiliar para construção das cláusulas de filtro na busca de
	 * {@link VirusCarga}.
	 * 
	 * @param criteria
	 *            {@link TypedFlatMap} com parâmetros de filtro;
	 * @param key
	 *            Chave para verificação de cláusula;
	 * @param hql
	 *            {@link StringBuilder} para construção de query HQL;
	 * @param clause
	 *            Cláusula para filtro sobre {@link VirusCarga}.
	 */
	private void appendIfContains(TypedFlatMap criteria, String key, StringBuilder hql, String clause) {
		if (criteria.containsKey(key) && !"".equals(criteria.get(key))) {
			hql.append(clause);
		}
	}

}
