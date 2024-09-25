package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.BloqueioFaturamento;

public class BloqueioFaturamentoDAO extends BaseCrudDao<BloqueioFaturamento, Long> {

	public BloqueioFaturamento findById(Long id) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("bloqueio");
		hql.addInnerJoin(BloqueioFaturamento.class.getName(), "bloqueio");
		hql.addInnerJoin("FETCH bloqueio.devedorDocServFat", "devedor");
		hql.addInnerJoin("FETCH devedor.doctoServico", "ds");
		hql.addInnerJoin("FETCH ds.filialByIdFilialOrigem", "fo");
		hql.addInnerJoin("FETCH bloqueio.motivoOcorrencia", "mo");
		hql.addInnerJoin("FETCH bloqueio.usuarioBloqueio", "ub");
		hql.addInnerJoin("FETCH ub.usuarioADSM", "uba");
		hql.addLeftOuterJoin("FETCH bloqueio.usuarioDesbloqueio", "ud");
		hql.addLeftOuterJoin("FETCH ud.usuarioADSM", "uda");
		hql.addCriteria("bloqueio.idBloqueioFaturamento", "=", id);

		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (result.size() > 0)
			return (BloqueioFaturamento)result.get(0);
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return BloqueioFaturamento.class;
	}

	public Integer getRowCount(Map criteria) {
    	SqlTemplate sql = getHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }

	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		SqlTemplate sql = getHqlPaginated(criteria);

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), paginatedQuery.getCurrentPage(), paginatedQuery.getPageSize(), sql.getCriteria());
	}

	public SqlTemplate getHqlPaginated(Map<String,Object> criteria) {
		SqlTemplate hql = new SqlTemplate();

		final String projection = "new Map(" +
			"bloqueio.idBloqueioFaturamento as idBloqueioFaturamento, " +
			"ds.tpDocumentoServico as dsTpDocumentoServico, " +
			"ds.filialByIdFilialOrigem.sgFilial as sgFilialOrigem, " +
			"ds.nrDoctoServico as nrDoctoServico, " +
			"filial.sgFilial as sgFilialCobradora, " +
			"clientePessoa.nrIdentificacao as nrIdentificacao, " +
			"clientePessoa.nmPessoa as nmPessoaDevedor, " +
			"mo.dsMotivoOcorrencia as dsMotivoOcorrencia, " +
			"bloqueio.dhBloqueio as dhBloqueio, " +
			"bloqueio.dhDesbloqueio as dhDesbloqueio, " +
			"uba.nmUsuario as nmUsuarioBloqueio, " +
			"uda.nmUsuario as nmUsuarioDesbloqueio, " +

			"ds.tpDocumentoServico as tpDocumentoServico, " +
			"ds.idDoctoServico as idDoctoServico, " +
			"ds.nrDoctoServico as nrConhecimentoDoctoServico, " +
			"bloqueio.dtPrevisao as dtPrevisao, " +
			"bloqueio.dsBloqueio as dsBloqueio, " +
			"mo.idMotivoOcorrencia as idMotivoOcorrencia " +
		")";

		hql.addProjection(projection);
		hql.addInnerJoin(BloqueioFaturamento.class.getName(), "bloqueio");
		hql.addInnerJoin("bloqueio.devedorDocServFat", "devedor");
		hql.addInnerJoin("devedor.doctoServico", "ds");
		hql.addInnerJoin("devedor.cliente", "cliente");
		hql.addInnerJoin("devedor.filial", "filial");
		hql.addInnerJoin("filial.pessoa", "pessoa");
		hql.addInnerJoin("cliente.pessoa", "clientePessoa");
		hql.addInnerJoin("bloqueio.motivoOcorrencia", "mo");
		hql.addInnerJoin("bloqueio.usuarioBloqueio", "ub");
		hql.addInnerJoin("ub.usuarioADSM", "uba");
		hql.addLeftOuterJoin("bloqueio.usuarioDesbloqueio", "ud");
		hql.addLeftOuterJoin("ud.usuarioADSM", "uda");

		if (criteria.get("idFatura") != null) {
			hql.addInnerJoin("devedor.itemFaturas", "if");
			hql.addCriteria("if.fatura.idFatura", "=", criteria.get("idFatura"));
		}

		hql.addCriteria("devedor.doctoServico.idDoctoServico", "=", criteria.get("idDoctoServico"));
		hql.addCriteria("cliente.idCliente", "=", criteria.get("idCliente"));
		hql.addCriteria("filial.idFilial", "=", criteria.get("idFilialCobranca"));
		hql.addCriteria("ub.id", "=", criteria.get("idUsuarioBloqueio"));
		hql.addCriteria("ud.id", "=", criteria.get("idUsuarioDesbloqueio"));
		hql.addCriteria("bloqueio.motivoOcorrencia.idMotivoOcorrencia", "=", criteria.get("idMotivoOcorrencia"));
		hql.addCriteria("trunc(bloqueio.dtPrevisao)", ">=", criteria.get("dhInicioPrevisao"));
		hql.addCriteria("trunc(bloqueio.dtPrevisao)", "<=", criteria.get("dhFimPrevisao"));
		hql.addCriteria("trunc(bloqueio.dhBloqueio.value)", ">=", criteria.get("dhInicioBloqueio"));
		hql.addCriteria("trunc(bloqueio.dhBloqueio.value)", "<=", criteria.get("dhFimBloqueio"));
		
		if(criteria.get("dhInicioDesbloqueio") == null && criteria.get("dhFimDesbloqueio") == null){
			hql.addCustomCriteria("bloqueio.dhDesbloqueio is null");
		} else {
		hql.addCriteria("trunc(bloqueio.dhDesbloqueio.value)", ">=", criteria.get("dhInicioDesbloqueio"));
		hql.addCriteria("trunc(bloqueio.dhDesbloqueio.value)", "<=", criteria.get("dhFimDesbloqueio"));
		}

		hql.addOrderBy("filial.idFilial");
		hql.addOrderBy("devedor.idDevedorDocServFat");

		return hql;
	}


	public List<BloqueioFaturamento> findBloqueioFaturamentoAtivo(Long idDevedorDocFat, Long idBloqueioFaturamento) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("bloqueio");
		hql.addInnerJoin(BloqueioFaturamento.class.getName(), "bloqueio");
		hql.addInnerJoin("FETCH bloqueio.devedorDocServFat", "devedor");
		hql.addInnerJoin("FETCH devedor.doctoServico", "ds");
		hql.addInnerJoin("FETCH ds.filialByIdFilialOrigem", "dsfo");
		hql.addInnerJoin("FETCH devedor.filial", "filial");
		hql.addCustomCriteria("bloqueio.dhDesbloqueio is null");
		hql.addCriteria("devedor.idDevedorDocServFat", "=", idDevedorDocFat);

		if (idBloqueioFaturamento != null)
			hql.addCriteria("bloqueio.idBloqueioFaturamento", "<>", idBloqueioFaturamento);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}


	public BloqueioFaturamento findByIdDevedorDocServFat(Long id) {
		List<BloqueioFaturamento> registros = getAdsmHibernateTemplate().findByCriteria(
			DetachedCriteria.forClass(BloqueioFaturamento.class)
			.add(Restrictions.eq("devedorDocServFat.idDevedorDocServFat", id))
			.add(Restrictions.isNull("dhDesbloqueio"))
		);

		if (registros != null && registros.size() > 0)
			return registros.get(0);
		return null;
	}


	public List<Map<String, Object>> find(Map criteria) {
		SqlTemplate sql = getHqlPaginated(criteria);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
}
