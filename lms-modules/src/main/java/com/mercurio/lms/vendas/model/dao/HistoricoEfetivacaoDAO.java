package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.HistoricoEfetivacao;

public class HistoricoEfetivacaoDAO extends BaseCrudDao<HistoricoEfetivacao, Long> {

	@Override
	protected Class<HistoricoEfetivacao> getPersistentClass() {
		return HistoricoEfetivacao.class;
	}
	

	@SuppressWarnings("rawtypes")
	public List findByIdSimulacao(Long idSimulacao) {
		StringBuilder hql = new StringBuilder();    
		hql.append("SELECT historico ")
		.append("FROM ")
		.append(this.getPersistentClass().getName()).append(" historico ")
		.append("JOIN historico.simulacao simulacao ")
		.append("JOIN historico.usuarioSolicitacao usuarioSol ")
		.append("LEFT OUTER JOIN historico.usuarioReprovador usuarioRep ")
		.append("WHERE historico.simulacao.id = :simulacao ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("simulacao", idSimulacao);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(),params);
	}
	
	/**
	 * LMS-6191 - Busca lista de atributos de <tt>HistoricoEfetivacao</tt> pelo
	 * id da <tt>Simulacao</tt> a que estão relacionadas, correspondente
	 * atributo <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return lista de <tt>HistoricoEfetivacao</tt>
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<TypedFlatMap> findHistoricoEfetivacaoList(TypedFlatMap criteria) {
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		Long idSimulacao = criteria.getLong("simulacao.idSimulacao");
		StringBuilder hql = new StringBuilder()
				.append("SELECT new Map( ")
				.append("  he.idHistoricoEfetivacao AS idHistoricoEfetivacao, ")
				.append("  he.dhSolicitacao AS dhSolicitacao, ")
				.append("  u_s.nmUsuario AS usuarioSolicitacao_nmUsuario, ")
				.append("  he.dhReprovacao AS dhReprovacao, ") 
				.append("  u_r.nmUsuario AS usuarioReprovador_nmUsuario, ")
				.append("  he.dhReprovacao AS dhReprovacao, ")
				.append("  he.dsMotivo AS dsMotivo, ")
				.append("  m.dsMotivoReprovacao AS motivoReprovacao_dsMotivo ")
				.append(") ")
				.append("FROM HistoricoEfetivacao he ")
				.append("LEFT OUTER JOIN he.usuarioSolicitacao.usuarioADSM u_s ")
				.append("LEFT OUTER JOIN he.usuarioReprovador.usuarioADSM u_r ")
				.append("LEFT OUTER JOIN he.motivoReprovacao m ")
				.append("WHERE he.simulacao.idSimulacao = ? ")
				.append("ORDER BY he.dhSolicitacao ");

		return (ResultSetPage<TypedFlatMap>) getAdsmHibernateTemplate().findPaginated(hql.toString(),findDefinition.getCurrentPage(),findDefinition.getPageSize(),new Object[] { idSimulacao });
	}

	/**
	 * LMS-6191 - Busca quantidade de <tt>HistoricoEfetivacao</tt> relacionadas
	 * a uma <tt>Simulacao</tt> de determinado id.
	 * 
	 * @param idSimulacao
	 *            id da <tt>Simulacao</tt>
	 * @return quantidade de <tt>HistoricoEfetivacao</tt>
	 */
	public Integer findHistoricoEfetivacaoRowCount(Long idSimulacao) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("simulacao.idSimulacao", idSimulacao);
		return getRowCount(criteria);
	}

}
