package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.CartaCorrecaoEletronica;
import com.mercurio.lms.util.LongUtils;

public class CartaCorrecaoEletronicaDAO extends BaseCrudDao<CartaCorrecaoEletronica, Long> {

	@Override
	protected Class<CartaCorrecaoEletronica> getPersistentClass() {
		return CartaCorrecaoEletronica.class;
	}
	
	/**
	 * Busca histórico de alterações para tela Carta de Correção.
	 * 
	 * @param idDoctoServico Identificador do documento de serviço
	 * @return Histórico de alterações para determinado documento de serviço
	 */
	@SuppressWarnings("unchecked")
	public List<CartaCorrecaoEletronica> findHistoricoCCEByIdDoctoServico(Long idDoctoServico) {
		List<String> listTpSituacaoCCe = new ArrayList<String>();
		listTpSituacaoCCe.add("E");
		listTpSituacaoCCe.add("A");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteriaIn("cce.tpSituacaoCartaCorrecao", listTpSituacaoCCe);
		sql.addOrderBy("cce.idCartaCorrecaoEletronica");
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * Busca Cartas de Correção aprovadas para determinado documento de serviço.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de serviço
	 * @return Cartas de Correção aprovadas para determinado documento de
	 *         serviço
	 */
	@SuppressWarnings("unchecked")
	public List<CartaCorrecaoEletronica> findAprovadasByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("cce.tpSituacaoCartaCorrecao", "=", "A");
		sql.addOrderBy("cce.idCartaCorrecaoEletronica");
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	public Long validateLimiteCCEByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("max(cce.nrCartaCorrecaoEletronica)");
		sql.addFrom(getPersistentClass().getName(), "cce");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		
		Long nrCartaCorrecaoEletronica = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	
		if (nrCartaCorrecaoEletronica == null) {
			return LongUtils.ZERO;
		}
		
		return nrCartaCorrecaoEletronica;
	}

	/**
	 * Busca coleção de Cartas de Correção autorizadas e enviadas de determinado
	 * Documento de Serviço.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de serviço
	 * @return Coleção de Cartas de Correção autorizadas e enviadas
	 */
	@SuppressWarnings("unchecked")
	public List<CartaCorrecaoEletronica> findAutorizadosEnviadosByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteriaIn("cce.tpSituacaoCartaCorrecao", new String[] { "A", "E" });
		sql.addOrderBy("cce.idCartaCorrecaoEletronica");
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca último número de Carta de Correção para determinado Documento de
	 * Serviço.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de serviço
	 * @return Último número de Carta de Correção
	 */
	public Long findMaxNrCCEByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("MAX(cce.nrCartaCorrecaoEletronica)", "maxNrCartaCorrecaoEletronica");
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteriaIn("cce.tpSituacaoCartaCorrecao", new String[] { "A", "E" });
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca coleção de Cartas de Correção enviadas de determinado Documento de
	 * Serviço.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de serviço
	 * @return Coleção de Cartas de Correção autorizadas e enviadas
	 */
	@SuppressWarnings("unchecked")
	public List<CartaCorrecaoEletronica> findEnviadosByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("cce.tpSituacaoCartaCorrecao", "=", "E");
		sql.addOrderBy("cce.idCartaCorrecaoEletronica");
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * Busca Carta de Correção de determinado Documento de Serviço.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de serviço
	 * @return Cartas de Correção de determinado documento de serviço
	 */
	@SuppressWarnings("unchecked")
	public List<CartaCorrecaoEletronica> findByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addOrderBy("cce.idCartaCorrecaoEletronica");
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public Long NrProtocoloByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("MAX(cce.nrProtoclo)", "maxNrCartaCorrecaoEletronica");
		sql.addFrom(getPersistentClass().getName() + " cce ");
		sql.addCriteria("cce.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteriaIn("cce.tpSituacaoCartaCorrecao", new String[] { "A", "E" });
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}
	
}
