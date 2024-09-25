package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.dto.InscricaoEstadualColetivaFilterDto;
import com.mercurio.lms.tributos.model.InscricaoEstadualColetiva;
import com.mercurio.lms.util.JTVigenciaUtils;

public class InscricaoEstadualColetivaDAO extends BaseCrudDao<InscricaoEstadualColetiva, Long>{

	@Override
	protected Class<InscricaoEstadualColetiva> getPersistentClass() {
		return InscricaoEstadualColetiva.class;
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
		
		.append("select new Map(inec.idInscricaoEstadualColetiva as idInscricaoEstadualColetiva ")
		.append("      ,pess.nrIdentificacao as nrIdentificacao ")
		.append("      ,pess.tpIdentificacao as tpIdentificacao ")
		.append("      ,pess.nmPessoa as nmPessoa ")
		.append("      ,unfe.sgUnidadeFederativa as sgUnidadeFederativa ")
		.append("      ,inec.nrInscricaoEstadualColetiva as nrInscricaoEstadualColetiva ")
		.append("      ,inec.dtVigenciaInicial as dtVigenciaInicial ")
		.append("      ,inec.dtVigenciaFinal as dtVigenciaFinal ) ")
		.append("from " + getPersistentClass().getName() + " as inec ")
		.append("inner join inec.cliente as clie ")
		.append("inner join clie.pessoa as pess ")
		.append("inner join inec.unidadeFederativa as unfe ")
		
		.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and clie.idCliente = :idCliente ");
		}
		
		if(MapUtils.getObject(criteria, "idUnidadeFederativa") != null) {
			query.append("  and unfe.idUnidadeFederativa = :idUnidadeFederativa ");
		}
		
		if(MapUtils.getObject(criteria, "nrInscricaoEstadual") != null){
			query.append("  and inec.nrInscricaoEstadualColetiva like :nrInscricaoEstadual ");
		}
		
		if(MapUtils.getObject(criteria, "periodoInicial") != null 
				&& MapUtils.getObject(criteria, "periodoFinal") != null) {
			query.append("  and inec.dtVigenciaInicial >= :periodoInicial ");
			query.append("  and inec.dtVigenciaFinal <= :periodoFinal ");
		}
		
		query.append("order by pess.nmPessoa, pess.tpIdentificacao, unfe.sgUnidadeFederativa, inec.dtVigenciaInicial ");

		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public Boolean findExistenciaInscricaoEstadualColetivaVigente(InscricaoEstadualColetiva bean) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select count(inec) ");
		hql.append("from InscricaoEstadualColetiva as inec ");
		hql.append("where ");
		hql.append("inec.cliente = ? ");
		params.add(bean.getCliente());
		hql.append("and inec.unidadeFederativa = ? ");
		params.add(bean.getUnidadeFederativa());
		
		if(bean.getIdInscricaoEstadualColetiva()!= null){
			hql.append("and inec.idInscricaoEstadualColetiva <> ? ");
			params.add(bean.getIdInscricaoEstadualColetiva());
		}
		
		hql.append("and (")
			.append("inec.dtVigenciaInicial").append(" <= ? and ( ")
			.append("inec.dtVigenciaFinal").append(" is NULL or ").append("inec.dtVigenciaFinal").append(" >= ?")
			.append(") OR (")
			.append("inec.dtVigenciaInicial").append(" > ? ");
		
		if (bean.getDtVigenciaFinal() != null) {
			hql.append(" and ").append("inec.dtVigenciaInicial").append(" <= ? ");
		}
		
		hql.append("))");

		params.add(bean.getDtVigenciaInicial());
		params.add(bean.getDtVigenciaInicial());
		params.add(bean.getDtVigenciaInicial());
		if (bean.getDtVigenciaFinal() != null) {
			params.add(bean.getDtVigenciaFinal());
		}
		
		Long count = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
		return Boolean.valueOf(count != null && count > 0);
	}
	
	@SuppressWarnings("rawtypes")
	public List findDadosIEColetativaByFilter(InscricaoEstadualColetivaFilterDto filterDto) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append("select new Map(inec.nrInscricaoEstadualColetiva as nrInscricaoEstadualColetiva ");
		hql.append("      ,unfe.sgUnidadeFederativa as sgUnidadeFederativa) ");
		hql.append("from InscricaoEstadualColetiva as inec ");
		hql.append("join inec.unidadeFederativa as unfe ");
		hql.append("join unfe.pais as pais ");
		hql.append("join inec.cliente as clie ");
		hql.append("where clie.idCliente = :idRemetente ");
		hql.append("and inec.dtVigenciaInicial <= :dtEmissao ");
		hql.append("and inec.dtVigenciaFinal >= :dtEmissao ");
		hql.append("and unfe.idUnidadeFederativa = :idUfDestino ");
		hql.append("and pais.idPais = :idPais ");
		
		params.put("idRemetente", filterDto.getIdRemetente());
		params.put("dtEmissao", filterDto.getDtEmissao());
		params.put("idUfDestino", filterDto.getIdUfDestino());
		params.put("idPais", filterDto.getIdPais());
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}

}
