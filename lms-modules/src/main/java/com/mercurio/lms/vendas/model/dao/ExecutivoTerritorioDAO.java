package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;

public class ExecutivoTerritorioDAO extends BaseCrudDao<ExecutivoTerritorio, Long> {

	@Override
	protected Class getPersistentClass() {
		return ExecutivoTerritorio.class;
	}

	@Override
	public ExecutivoTerritorio findById(Long id) {
		return findByIdByCriteria(createCriteria(), "idExecutivoTerritorio", id);
	}

	public List<ExecutivoTerritorio> find(ExecutivoTerritorio executivoTerritorio) {
		return createCriteria(executivoTerritorio).list();
	}

	public Integer findCount(ExecutivoTerritorio executivoTerritorio) {
		return rowCountByCriteria(createCriteria(executivoTerritorio));
	}
	
	public List<ExecutivoTerritorio> findComVigenciaAtiva(Long idUsuario, Long idTerritorio, DomainValue tpExecutivo) {
		Criteria criteria = createCriteria();
		
		if (idUsuario != null) {
			criteria.add(Restrictions.eq("usuario.idUsuario", idUsuario));
		}
		
		if (idTerritorio != null) {
			criteria.add(Restrictions.eq("territorio.idTerritorio", idTerritorio));
		}
		
		if (tpExecutivo != null) {
			criteria.add(Restrictions.eq("tpExecutivo", tpExecutivo));
		}
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		criteria.add(Restrictions.le("dtVigenciaInicial", dataAtual));
		
		criteria.add(Restrictions.disjunction()
				.add(Restrictions.isNull("dtVigenciaFinal"))
				.add(Restrictions.ge("dtVigenciaFinal", dataAtual))
		);
		
		return criteria.list();
	}

	public boolean findTerritoriosAlreadyContainsExecutivo(Long idTerritorio, Long idExecutivo) {
		Criteria criteria = createCriteria(ExecutivoTerritorio.class);
		
		criteria.add(Restrictions.eq("territorio.idTerritorio", idTerritorio));
		
		if (idExecutivo != null) {
			criteria.add(Restrictions.ne("idExecutivoTerritorio", idExecutivo));
		}

		Integer numberOfExecutivos = rowCountByCriteria(criteria);
		return !numberOfExecutivos.equals(0);
	}
	
	public boolean existsExecutivo(Long idTerritorio, DomainValue tpExecutivo) {
		Criteria criteria = createCriteria(ExecutivoTerritorio.class);
		
		criteria.add(Restrictions.eq("territorio.idTerritorio", idTerritorio));
		if(null != tpExecutivo) {
			criteria.add(Restrictions.eq("tpExecutivo", tpExecutivo));
		}
		
		Integer numberOfExecutivos = rowCountByCriteria(criteria);
		return !numberOfExecutivos.equals(0);
	}

	private Criteria createCriteria(ExecutivoTerritorio executivoTerritorio) {
		Criteria criteria = createCriteria();
		
		if (executivoTerritorio.getUsuario() != null) {
			criteria.add(Restrictions.eq("usuario.idUsuario", executivoTerritorio.getUsuario().getIdUsuario()));
		}
		
		if (executivoTerritorio.getTerritorio() != null) {
			criteria.add(Restrictions.eq("territorio.idTerritorio", executivoTerritorio.getTerritorio().getIdTerritorio()));
		}
		
		DomainValue tpExecutivo = executivoTerritorio.getTpExecutivo();
		if (tpExecutivo != null) {
			criteria.add(Restrictions.eq("tpExecutivo", tpExecutivo));
		}
		
		YearMonthDay dtVigenciaInicial = executivoTerritorio.getDtVigenciaInicial();
		if (dtVigenciaInicial != null) {
			criteria.add(Restrictions.eq("dtVigenciaInicial", dtVigenciaInicial));
		}
		
		YearMonthDay dtVigenciaFinal = executivoTerritorio.getDtVigenciaFinal();
		if (dtVigenciaFinal != null) {
			criteria.add(Restrictions.eq("dtVigenciaFinal", dtVigenciaFinal));
		}
		
		return criteria;
	}
	
	@Override
	protected Criteria createCriteria() {
		Criteria criteria = super.createCriteria();
		
		criteria.createAlias("usuario", "usuario");
		criteria.createAlias("usuario.usuarioADSM", "usuarioADSM");
		return criteria;
	}

	public List<ExecutivoTerritorio> findEquipeVendas(Long idTerritorio) {
		List<Long> executivosIds = findEquipeVendasIds(idTerritorio);
		
		if (executivosIds.isEmpty()) {
			return new ArrayList<ExecutivoTerritorio>();
		}
		
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.in("idExecutivoTerritorio", executivosIds));

		List<ExecutivoTerritorio> list = criteria.list();
		return list;
	}

	private List<Long> findEquipeVendasIds(Long idTerritorio) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("   select max(id_executivo_territorio) as id_executivo_territorio from executivo_territorio ");
		sb.append("     where id_territorio = :idTerritorio and tp_situacao = 'A' ");
		sb.append("     and (sysdate between dt_inicio and coalesce(dt_fim, to_date('25/12/2299', 'dd/mm/yyyy')) or dt_inicio >= sysdate) ");
		sb.append("     group by tp_executivo ");

		Map<String, Object> params = new TypedFlatMap();		
		params.put("idTerritorio", idTerritorio); 

    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_executivo_territorio", Hibernate.LONG);
			}
    	};

    	List<Map<String, Object>> mapResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), params, csq);
    	
    	List<Long> ids = new ArrayList<Long>();
    	for(Map<String, Object> map : mapResult) {
    		ids.add((Long)map.get("id_executivo_territorio"));
    	}

    	return ids;
	}

	public String findCargoCpfByIdExecutivo(Long idExecutivo) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  select nr_cpf  ");
		sb.append("   from usuario u, v_funcionario f ");
		sb.append("  where u.id_usuario = :idExecutivo ");
		sb.append("  and   f.NR_MATRICULA = u.NR_MATRICULA  ");
		
		Map<String, Object> params = new TypedFlatMap();		
		params.put("idExecutivo", idExecutivo); 

		
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nr_cpf", Hibernate.STRING);
			}
    	};


    	List<Map<String, Object>> mapResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), params, csq);

    	return mapResult.iterator().next().get("nr_cpf").toString();
	}

	public String getLocalizacaoFromExecutivo(String cpfExecutivo) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("  select sg_filial from v_funcionario f  ");
		sb.append("    where nr_cpf = :cpfExecutivo ");

		Map<String, Object> params = new TypedFlatMap();		
		params.put("cpfExecutivo", cpfExecutivo); 

    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
			}
    	};


    	List<Map<String, Object>> mapResult = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), params, csq);

    	if (mapResult.isEmpty()) {
    		return "";
    	}
    	
    	return mapResult.iterator().next().get("sg_filial").toString();
		
	}

	public ExecutivoTerritorio findByIdUsuario(Long idUsuario) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("usuario.idUsuario", idUsuario));
		
		List<ExecutivoTerritorio> list = criteria.list();
		
		if (list.isEmpty()) {
			return null;
		}
		
		return list.iterator().next();
		
	}
	
}
