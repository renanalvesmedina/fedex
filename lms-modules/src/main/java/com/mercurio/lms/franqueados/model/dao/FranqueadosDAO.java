package com.mercurio.lms.franqueados.model.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.Franqueado;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe responsável por reunir as consultas referente ao franqueado
 * @author MarceloG
 *
 */
public class FranqueadosDAO extends BaseCrudDao<Franqueado, Long> {

	@Override
	protected Class<Franqueado> getPersistentClass() {
		return Franqueado.class;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginatedFranqueados(Map criteria){
		Map clone = new HashMap();
		clone.putAll(criteria);
		
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("f.idFranqueado"), "idFranqueado")
				.add(Projections.property("p.nrIdentificacao"), "nrIdentificacao")
				.add(Projections.property("p.nmPessoa"), "nmPessoa")
				.add(Projections.property("f.blOptanteSimples"), "blOptanteSimples");
	
		FindDefinition fd = FindDefinition.createFindDefinition(clone);
		
		DetachedCriteria dc = createDcFindPaginated(criteria, projection).setProjection(projection);
		
		if (!fd.getOrder().isEmpty()) {
			dc.addOrder(Order.asc((String) fd.getOrder().get(0)));
		}
		
		DetachedCriteria dcCount = createDcFindPaginated(criteria, projection).setProjection(Projections.projectionList().add(Projections.countDistinct("idFranqueado")));
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc,dcCount,fd.getCurrentPage(), fd.getPageSize());
	}

	@SuppressWarnings("rawtypes")
	private DetachedCriteria createDcFindPaginated(Map criteria,
			ProjectionList projection) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
		.createAlias("pessoa", "p");
		
		String nrIdentificacao = (String) criteria.get("nrIdentificacao");
		if (nrIdentificacao != null) {
			dc.add(Restrictions.eq("p.nrIdentificacao", PessoaUtils.clearIdentificacao(nrIdentificacao)));
		}

		String nmPessoa = (String) criteria.get("nmPessoa");
		if (nmPessoa != null) {
			dc.add(Restrictions.ilike("p.nmPessoa", nmPessoa));
		}

		String blOptanteSimples = (String) criteria.get("blOptanteSimples");
		if (blOptanteSimples != null) {
			dc.add(Restrictions.eq("f.blOptanteSimples", "S".equals(blOptanteSimples) ? true : false));
		}
		
		return dc;
	}
	
	@SuppressWarnings("rawtypes")
	public Serializable findByIdFranqueados(Long id){
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("f.idFranqueado"), "idFranqueado")
				.add(Projections.property("p.idPessoa"), "idPessoa")
				.add(Projections.property("p.nrIdentificacao"), "nrIdentificacao")
				.add(Projections.property("p.nmPessoa"), "nmPessoa")
				.add(Projections.property("f.blOptanteSimples"), "blOptanteSimples");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
		.createAlias("pessoa", "p")
		.setProjection(projection);
		

		if (id != null) {
			dc.add(Restrictions.eq("f.idFranqueado", id));
		}
		
		List franqueados = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		
		TypedFlatMap franqueado = null;
		if(!franqueados.isEmpty()){
			Object[] obj = (Object[]) franqueados.get(0);
			
			franqueado = new TypedFlatMap();
			franqueado.put("idFranqueado",obj[0]);
			franqueado.put("idPessoa", obj[1]);
			franqueado.put("nrIdentificacao", obj[2]);
			franqueado.put("nmPessoa",obj[3]);
			franqueado.put("blOptanteSimples",(Boolean)obj[4]?"S":"N");
		}
		
		return franqueado;
	}
	
	@Override
	public Franqueado findById(Long id) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as f ");
		query.append("inner join fetch f.pessoa p ");
		query.append("left join fetch f.franqueadoFranquias ff ");
		query.append("where ");
		query.append(" f.idFranqueado = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (Franqueado) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<TypedFlatMap> findDocumentosFranqueados(String tipoCalculo, Long idFranquia, YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia, boolean isLocal){  
		final StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		
		sql.append("SELECT CO.TP_FRETE,");
		sql.append("	   DS.ID_DOCTO_SERVICO,");
		sql.append("	   DS.ID_FILIAL_ORIGEM,");
		sql.append("	   DS.ID_FILIAL_DESTINO,");
		sql.append("	   CO.TP_SITUACAO_CONHECIMENTO,");
		sql.append("	   CO.TP_CONHECIMENTO,");
		sql.append("	   DS.VL_TOTAL_DOC_SERVICO,");
		sql.append("	   DS.VL_IMPOSTO,");
		sql.append("	   DS.VL_MERCADORIA");
		sql.append("  FROM DOCTO_SERVICO DS");
		sql.append(" INNER JOIN CONHECIMENTO CO ON (CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO)");
		sql.append(" WHERE DS.ID_FILIAL_ORIGEM = ?");
		sql.append("   AND DS.ID_FILIAL_DESTINO <> ?");
		sql.append("   AND TRUNC(CAST(DS.DH_EMISSAO AS DATE)) BETWEEN ? AND ?");
		sql.append("   AND CO.TP_SITUACAO_CONHECIMENTO = 'E'");
		
		param.add(addParametro(idFranquia, dtIniCompetencia, dtFimCompetencia));
		
		sql.append(" UNION "); 
		
		sql.append("SELECT CO.TP_FRETE,");
		sql.append("	   DS.ID_DOCTO_SERVICO,");
		sql.append("	   DS.ID_FILIAL_ORIGEM,");
		sql.append("	   DS.ID_FILIAL_DESTINO,");
		sql.append("	   CO.TP_SITUACAO_CONHECIMENTO,");
		sql.append("	   CO.TP_CONHECIMENTO,");
		sql.append("	   DS.VL_TOTAL_DOC_SERVICO,");
		sql.append("	   DS.VL_IMPOSTO,");
		sql.append("	   DS.VL_MERCADORIA");
		sql.append("  FROM DOCTO_SERVICO DS");
		sql.append(" INNER JOIN CONHECIMENTO CO ON (CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO)");
		sql.append(" WHERE DS.ID_FILIAL_ORIGEM <> ? ");
		sql.append("   AND DS.ID_FILIAL_DESTINO = ? ");
		sql.append("   AND TRUNC(CAST(DS.DH_EMISSAO AS DATE)) BETWEEN ? AND ? ");
		sql.append("   AND CO.TP_SITUACAO_CONHECIMENTO = 'E' ");
		
		param.add(addParametro(idFranquia, dtIniCompetencia, dtFimCompetencia));
		
		if (isLocal){
			sql.append(" UNION "); 
			
			sql.append("SELECT CO.TP_FRETE,");
			sql.append("	   DS.ID_DOCTO_SERVICO,");
			sql.append("	   DS.ID_FILIAL_ORIGEM,");
			sql.append("	   DS.ID_FILIAL_DESTINO,");
			sql.append("	   CO.TP_SITUACAO_CONHECIMENTO,");
			sql.append("	   CO.TP_CONHECIMENTO,");
			sql.append("	   DS.VL_TOTAL_DOC_SERVICO,");
			sql.append("	   DS.VL_IMPOSTO,");
			sql.append("	   DS.VL_MERCADORIA");
			sql.append("  FROM DOCTO_SERVICO DS");
			sql.append(" INNER JOIN CONHECIMENTO CO ON (CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO)");
			sql.append(" WHERE DS.ID_FILIAL_ORIGEM = ?");
		    sql.append("   AND DS.ID_FILIAL_DESTINO = ?");
			sql.append("   AND TRUNC(CAST(DS.DH_EMISSAO AS DATE)) BETWEEN ? AND ?");
			sql.append("   AND CO.TP_SITUACAO_CONHECIMENTO = 'E' ");
			
			param.add(addParametro(idFranquia, dtIniCompetencia, dtFimCompetencia));
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("TP_FRETE", Hibernate.STRING);
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("TP_SITUACAO_CONHECIMENTO", Hibernate.STRING);
				sqlQuery.addScalar("TP_CONHECIMENTO", Hibernate.STRING);
				sqlQuery.addScalar("VL_TOTAL_DOC_SERVICO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_IMPOSTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);				
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};
		
		return getHibernateTemplate().executeFind(hcb);
	}

	private Object addParametro(Long idFranquia, YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia) {
		List<Object> param = new ArrayList<Object>();

		param.add(idFranquia);
		param.add(idFranquia);
		param.add(dtIniCompetencia);
		param.add(dtFimCompetencia);

		return param;
	}	
	
}
