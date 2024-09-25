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
	 * Busca hist�rico de altera��es para tela Carta de Corre��o.
	 * 
	 * @param idDoctoServico Identificador do documento de servi�o
	 * @return Hist�rico de altera��es para determinado documento de servi�o
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
	 * Busca Cartas de Corre��o aprovadas para determinado documento de servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Cartas de Corre��o aprovadas para determinado documento de
	 *         servi�o
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
	 * Busca cole��o de Cartas de Corre��o autorizadas e enviadas de determinado
	 * Documento de Servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Cole��o de Cartas de Corre��o autorizadas e enviadas
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
	 * Busca �ltimo n�mero de Carta de Corre��o para determinado Documento de
	 * Servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return �ltimo n�mero de Carta de Corre��o
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
	 * Busca cole��o de Cartas de Corre��o enviadas de determinado Documento de
	 * Servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Cole��o de Cartas de Corre��o autorizadas e enviadas
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
	 * Busca Carta de Corre��o de determinado Documento de Servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Cartas de Corre��o de determinado documento de servi�o
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
