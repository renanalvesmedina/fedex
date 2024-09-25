package com.mercurio.lms.sgr.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.model.SolicEscoltaHistorico;
import com.mercurio.lms.sgr.model.SolicEscoltaNegociacao;
import com.mercurio.lms.sgr.model.SolicitacaoEscolta;

public class SolicitacaoEscoltaDAO extends BaseCrudDao<SolicitacaoEscolta, Long> {

	@Override
	protected Class<SolicitacaoEscolta> getPersistentClass() {
		return SolicitacaoEscolta.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("exigenciaGerRisco", FetchMode.SELECT);
		lazyFindById.put("controleCarga.filialByIdFilialOrigem", FetchMode.SELECT);
		lazyFindById.put("filialSolicitante.pessoa", FetchMode.SELECT);
		lazyFindById.put("usuarioSolicitante.usuarioADSM", FetchMode.SELECT);
		lazyFindById.put("filialOrigem.pessoa", FetchMode.SELECT);
		lazyFindById.put("aeroportoOrigem.pessoa", FetchMode.SELECT);
		lazyFindById.put("clienteOrigem.pessoa", FetchMode.SELECT);
		lazyFindById.put("filialDestino.pessoa", FetchMode.SELECT);
		lazyFindById.put("aeroportoDestino.pessoa", FetchMode.SELECT);
		lazyFindById.put("clienteDestino.pessoa", FetchMode.SELECT);
		lazyFindById.put("rotaColetaEntrega", FetchMode.SELECT);
		lazyFindById.put("motorista.pessoa", FetchMode.SELECT);
		lazyFindById.put("clienteEscolta.pessoa", FetchMode.SELECT);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	@Override
	public SolicitacaoEscolta findById(Long id) {
		return super.findById(id);
	}

	public Long findNrSolicitacaoEscolta(Filial filial) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT MAX(se.nrSolicitacaoEscolta) ")
				.append("FROM SolicitacaoEscolta se ")
				.append("WHERE se.filialSolicitante.idFilial = :idFilialSolicitante ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idFilialSolicitante", filial.getIdFilial());
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters);
	}

	public Integer getRowCountByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeQueryByFilter(criteria), criteria);
	}

	@SuppressWarnings("unchecked")
	public List<SolicitacaoEscolta> findByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeQueryByFilter(criteria), criteria);
	}

	private String makeQueryByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT se ")
				.append("FROM SolicitacaoEscolta se ")
				.append("JOIN FETCH se.exigenciaGerRisco egr ")
				.append("JOIN FETCH se.filialSolicitante fs ")
				.append("JOIN FETCH fs.pessoa ")
				.append("JOIN FETCH se.clienteEscolta ce ")
				.append("JOIN FETCH ce.pessoa ")
				.append("LEFT JOIN FETCH se.filialOrigem fo ")
				.append("LEFT JOIN FETCH fo.pessoa ")
				.append("LEFT JOIN FETCH se.aeroportoOrigem ao ")
				.append("LEFT JOIN FETCH ao.pessoa ")
				.append("LEFT JOIN FETCH se.clienteOrigem co ")
				.append("LEFT JOIN FETCH co.pessoa ")
				.append("LEFT JOIN FETCH se.filialDestino fd ")
				.append("LEFT JOIN FETCH fd.pessoa ")
				.append("LEFT JOIN FETCH se.aeroportoDestino ad ")
				.append("LEFT JOIN FETCH ad.pessoa ")
				.append("LEFT JOIN FETCH se.clienteDestino cd ")
				.append("LEFT JOIN FETCH cd.pessoa ")
				.append("LEFT JOIN FETCH se.clienteDestino cd ")
				.append("LEFT JOIN FETCH cd.pessoa ")
				.append("LEFT JOIN FETCH se.rotaColetaEntrega rce ")
				.append("WHERE 1 = 1 ");
		appendIfContains(criteria, "idFilialSolicitante", hql, "AND fs.idFilial = :idFilialSolicitante ");
		appendIfContains(criteria, "nrSolicitacaoEscolta", hql, "AND se.nrSolicitacaoEscolta = :nrSolicitacaoEscolta ");
		appendIfContains(criteria, "tpSituacao", hql, "AND se.tpSituacao = :tpSituacao ");
		appendIfContains(criteria, "idUsuarioSolicitante", hql, "AND se.usuarioSolicitante.idUsuario = :idUsuarioSolicitante ");
		appendIfContains(criteria, "dhInclusaoInicial", hql, "AND se.dhInclusao.value >= :dhInclusaoInicial ");
		appendIfContains(criteria, "dhInclusaoFinal", hql, "AND se.dhInclusao.value < :dhInclusaoFinal ");
		appendIfContains(criteria, "dhInicioPrevistoInicial", hql, "AND se.dhInicioPrevisto.value >= :dhInicioPrevistoInicial ");
		appendIfContains(criteria, "dhInicioPrevistoFinal", hql, "AND se.dhInicioPrevisto.value < :dhInicioPrevistoFinal ");
		appendIfContains(criteria, "idExigenciaGerRisco", hql, "AND egr.idExigenciaGerRisco = :idExigenciaGerRisco ");
		appendIfContains(criteria, "nrKmSolicitacaoEscoltaInicial", hql, "AND se.nrKmSolicitacaoEscolta >= :nrKmSolicitacaoEscoltaInicial ");
		appendIfContains(criteria, "nrKmSolicitacaoEscoltaFinal", hql, "AND se.nrKmSolicitacaoEscolta <= :nrKmSolicitacaoEscoltaFinal ");
		appendIfContains(criteria, "idClienteEscolta", hql, "AND ce.idCliente = :idClienteEscolta ");
		appendIfContains(criteria, "idControleCarga", hql, "AND se.controleCarga.idControleCarga = :idControleCarga ");
		appendIfContains(criteria, "idNaturezaProduto", hql, "AND se.naturezaProduto.idNaturezaProduto = :idNaturezaProduto ");
		appendIfContains(criteria, "idMeioTransporteTransportado", hql, "AND se.meioTransporteByIdTransportado.idMeioTransporte = :idMeioTransporteTransportado ");
		appendIfContains(criteria, "idMeioTransporteSemiReboque", hql, "AND se.meioTransporteBySemiReboque.idMeioTransporte = :idMeioTransporteSemiReboque ");
		appendIfContains(criteria, "tpOrigem", hql, "AND se.tpOrigem = :tpOrigem ");
		appendIfContains(criteria, "idFilialOrigem", hql, "AND fo.idFilial = :idFilialOrigem ");
		appendIfContains(criteria, "idClienteOrigem", hql, "AND co.idCliente = :idClienteOrigem ");
		appendIfContains(criteria, "idAeroportoOrigem", hql, "AND ao.idAeroporto = :idAeroportoOrigem ");
		appendIfContains(criteria, "tpDestino", hql, "AND se.tpDestino = :tpDestino ");
		appendIfContains(criteria, "idFilialDestino", hql, "AND fd.idFilial = :idFilialDestino ");
		appendIfContains(criteria, "idClienteDestino", hql, "AND cd.idCliente = :idClienteDestino ");
		appendIfContains(criteria, "idAeroportoDestino", hql, "AND ad.idAeroporto = :idAeroportoDestino ");
		appendIfContains(criteria, "idRotaColetaEntrega", hql, "AND rce.idRotaColetaEntrega = :idRotaColetaEntrega ");
		return hql.toString();
	}

	private void appendIfContains(TypedFlatMap criteria, String key, StringBuilder hql, String clause) {
		if (criteria.containsKey(key) && !"".equals(criteria.get(key))) {
			hql.append(clause);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SolicEscoltaHistorico> findSolicEscoltaHistoricoByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM SolicEscoltaHistorico seh ")
				.append("JOIN FETCH seh.usuario.usuarioADSM ")
				.append("WHERE seh.solicitacaoEscolta.idSolicitacaoEscolta = :idSolicitacaoEscolta ")
				.append("ORDER BY seh.dhInclusao ");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
	}

	public void removeSolicEscoltaHistoricoByIds(List<Long> ids) {
		StringBuilder hql = new StringBuilder()
				.append("DELETE SolicEscoltaHistorico seh ")
				.append("WHERE seh.solicitacaoEscolta.idSolicitacaoEscolta IN (:id) ")
				.append("AND seh.tpSituacao = 'I' ");
		getAdsmHibernateTemplate().removeByIds(hql.toString(), ids);
	}

	@SuppressWarnings("unchecked")
	public List<SolicEscoltaNegociacao> findSolicEscoltaNegociacaoByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM SolicEscoltaNegociacao sen ")
				.append("JOIN FETCH seh.usuario.usuarioADSM ")
				.append("WHERE sen.solicitacaoEscolta.idSolicitacaoEscolta = :idSolicitacaoEscolta ")
				.append("ORDER BY sen.dhInclusao ");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
	}

}
