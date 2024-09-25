package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class ClienteTerritorioDAO extends BaseCrudDao<ClienteTerritorio, Long> {

	@Override
	protected final Class getPersistentClass() {
		return ClienteTerritorio.class;
	}

	@Override
	public ClienteTerritorio findById(Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ct");

		sql.addInnerJoin(getPersistentClass().getName(),"ct");

		sql.addLeftOuterJoin("fetch ct.cliente","cliente");
		sql.addLeftOuterJoin("fetch ct.territorio","territorio");
		sql.addLeftOuterJoin("fetch cliente.pessoa","clientePessoa");
		sql.addLeftOuterJoin("fetch cliente.filialByIdFilialAtendeComercial","filialComercial");
		sql.addLeftOuterJoin("fetch filialComercial.pessoa","filialComercialPessoa");
		sql.addLeftOuterJoin("fetch territorio.filial","territorioFilial");
		sql.addLeftOuterJoin("fetch territorioFilial.pessoa","territorioFilialPessoa");
		sql.addLeftOuterJoin("fetch territorio.regional","territorioRegional");
		sql.addLeftOuterJoin("fetch ct.pendenciaAprovacao","pendenciaAprovacao");

		sql.addCriteria("ct.idClienteTerritorio", "=", id);

		List <ClienteTerritorio> retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		if(retorno.isEmpty()) {
			return null;
		}

		return retorno.iterator().next();
	}

	public List <ClienteTerritorio> find(Long idCliente, Long idTerritorio,
			DomainValue tpModal, YearMonthDay dtInicio, YearMonthDay dtFim,
			DmStatusEnum tpSituacao, Long idFilial, Long idRegional, Long idClienteTerritorio) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("ct");

		sql.addInnerJoin(getPersistentClass().getName(),"ct");

		sql.addLeftOuterJoin("fetch ct.cliente","cliente");
		sql.addLeftOuterJoin("fetch ct.territorio","territorio");
		sql.addLeftOuterJoin("fetch cliente.pessoa","clientePessoa");
		sql.addLeftOuterJoin("fetch cliente.filialByIdFilialAtendeComercial","filialComercial");
		sql.addLeftOuterJoin("fetch filialComercial.pessoa","filialComercialPessoa");
		sql.addLeftOuterJoin("fetch territorio.filial","territorioFilial");
		sql.addLeftOuterJoin("fetch territorioFilial.pessoa","territorioFilialPessoa");
		sql.addLeftOuterJoin("fetch territorio.regional","territorioRegional");
		sql.addLeftOuterJoin("fetch ct.pendenciaAprovacao","pendenciaAprovacao");

		if (idClienteTerritorio != null) {
			sql.addCriteria("ct.idClienteTerritorio", "=", idClienteTerritorio);
		}
		
		if (idCliente != null) {
			sql.addCriteria("cliente.idCliente", "=", idCliente);
		}

		if (idTerritorio != null) {
			sql.addCriteria("territorio.idTerritorio", "=",idTerritorio);
		}

		if (tpModal != null) {
			sql.addCriteria("ct.tpModal", "=", tpModal.getValue());
		}
		
		if (dtInicio != null) {
			sql.addCriteria("ct.dtInicio", ">=", dtInicio);
		}

		if (dtFim != null) {
			sql.addCriteria("ct.dtFim", "<=", dtFim);
		}

		if (tpSituacao != null) {
			sql.addCriteria("ct.tpSituacao", "=", tpSituacao.getDomainValue());
		} else {
			sql.addCustomCriteria("(ct.tpSituacao = 'A' OR (ct.tpSituacao = 'I' AND ct.tpSituacaoAprovacao = 'E')) ");
		}

		if (idFilial != null) {
			sql.addCriteria("territorioFilial.idFilial", "=", idFilial);
		}
		
		if (idRegional != null) {
			sql.addCriteria("territorioRegional.idRegional", "=", idRegional);
		}
		
		List <ClienteTerritorio> retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		return retorno;
	}
	
	public Integer findCount(
			Long idCliente, 
			Long idTerritorio, 
			DomainValue tpModal, 
			YearMonthDay dtInicio, 
			YearMonthDay dtFim,
			DmStatusEnum tpSituacao) {
		return rowCountByCriteria(createCriterions(idCliente, idTerritorio, tpModal, dtInicio, dtFim, tpSituacao));
	}

	public List<ClienteTerritorio> find(Long idCliente, Long idTerritorio,
			DomainValue tpModal, YearMonthDay dtInicio, YearMonthDay dtFim,
			DmStatusEnum tpSituacao, Long idFilial, Long idRegional) {

		return find(idCliente, idTerritorio, tpModal, dtInicio, dtFim,
				tpSituacao, idFilial, idRegional, null);
	}

	
	private List<Criterion> createCriterions(
			Long idCliente, 
			Long idTerritorio, 
			DomainValue tpModal, 
			YearMonthDay dtInicio, 
			YearMonthDay dtFim,
			DmStatusEnum tpSituacao) {

		List<Criterion> criterionList = new ArrayList<Criterion>();

		if (idCliente != null) {
			criterionList.add(Restrictions.eq("cliente.idCliente", idCliente));
		}

		if (idTerritorio != null) {
			criterionList.add(Restrictions.eq("territorio.idTerritorio", idTerritorio));
		}

		if (tpModal != null) {
			criterionList.add(Restrictions.eq("tpModal", tpModal.getValue()).ignoreCase());
		}
		
		if (dtInicio != null) {
			criterionList.add(Restrictions.ge("dtInicio", dtInicio));
		}

		if (dtFim != null) {
			criterionList.add(Restrictions.lt("dtFim", dtFim));
		}

		if (tpSituacao  != null) {
			criterionList.add(Restrictions.eq("tpSituacao", tpSituacao.getDomainValue()));
		} else {
			criterionList.add(Restrictions.eq("tpSituacao", DmStatusEnum.ATIVO.getDomainValue()));
		}

		return criterionList;
	}

	public List<ClienteTerritorio> findByTerritorio(Long idTerritorio) {
		return find(null, idTerritorio, null, null, null, null, null, null, null);
	}

}
