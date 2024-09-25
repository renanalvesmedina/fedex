package com.mercurio.lms.portaria.model.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TerminalDAO extends BaseCrudDao<Terminal, Long>
{	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Terminal.class;
    }
   public List findByIdView(Long idTerminal) {
	   StringBuffer sb = new StringBuffer();
   					sb.append("SELECT new map(T.idTerminal AS idTerminal, T.dtVigenciaInicial AS dtVigenciaInicial, T.dtVigenciaFinal AS dtVigenciaFinal, ")
   					  .append("T.nrAreaTotal AS nrAreaTotal, T.nrAreaArmazenagem AS nrAreaArmazenagem, T.obTerminal AS obTerminal, F.sgFilial AS filial_sgFilial, ")
   					  .append("FP.nmFantasia AS filial_pessoa_nmFantasia, F.idFilial AS filial_idFilial, F.sgFilial AS filial_sgFilial, ")
   					  .append("TP.idPessoa AS pessoa_idPessoa, TP.nmPessoa AS pessoa_nmPessoa) ")
   					  .append("from ").append(Terminal.class.getName()).append(" AS T ")
   					  .append("INNER JOIN T.filial AS F ")
   					  .append("INNER JOIN F.pessoa AS FP ")
   					  .append("INNER JOIN T.pessoa AS TP ")
   					  .append("WHERE T.idTerminal = ?");
	   return getAdsmHibernateTemplate().find(sb.toString(),idTerminal);
   }

   public boolean findTerminalByFilialAndNameVigente(Long idTerminal, Long idFilial,String name,YearMonthDay dtVigenciaInicial,YearMonthDay dtVigenciaFinal) {
	   DetachedCriteria dc = createDetachedCriteria()
	   					  .setProjection(Projections.property("idTerminal"))
	   					  .createAlias("pessoa","P")
	   					  .createAlias("filial","F")
	   					  .add(Restrictions.ilike("P.nmPessoa",name))
	   					  .add(Restrictions.eq("F.idFilial",idFilial));
	   JTVigenciaUtils.getDetachedVigencia(dc,dtVigenciaInicial,dtVigenciaFinal);
	   if (idTerminal != null)
		   dc.add(Restrictions.ne("idTerminal",idTerminal));
	   return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() != 0;
   }
   public ResultSetPage findPaginated(TypedFlatMap map, FindDefinition findDef) {
	   Object[] result = createSql(map);
	   StringBuffer sb = new StringBuffer();
			    	sb.append("SELECT new map(T.idTerminal AS idTerminal, T.dtVigenciaInicial AS dtVigenciaInicial, T.dtVigenciaFinal AS dtVigenciaFinal, ")
			    	  .append("T.nrAreaTotal AS nrAreaTotal, T.nrAreaArmazenagem AS nrAreaArmazenagem, T.obTerminal AS obTerminal, F.sgFilial AS filial_sgFilial, ")
			    	  .append("FP.nmFantasia AS filial_pessoa_nmFantasia, TP.nmPessoa AS pessoa_nmPessoa) ")
			    	  .append((String)result[0]);
    	return getAdsmHibernateTemplate().findPaginated(sb.toString(),findDef.getCurrentPage(),findDef.getPageSize(),(Map)result[1]);
   }
   public Integer getRowCount(TypedFlatMap criteria) {
	   	Object[] result = createSql(criteria);
   		return getAdsmHibernateTemplate().getRowCountForQuery((String)result[0],(Map)result[1]);
   }
   
   private Object[] createSql(TypedFlatMap map) {
	   	StringBuffer sb = new StringBuffer();
		StringBuffer sbWhere = new StringBuffer();
	   	Map c2 = new HashMap();
	   	//JOINS
	   	sb.append("from ").append(Terminal.class.getName()).append(" AS T ")
	   		.append("INNER JOIN T.filial AS F ")
	   		.append("INNER JOIN F.pessoa AS FP ")
	   		.append("INNER JOIN T.pessoa AS TP ");
	   	
	   	if (!StringUtils.isBlank(map.getString("filial.idFilial"))) {
	   		c2.put("filial",map.getLong("filial.idFilial"));
	   		sbWhere.append((sbWhere.length() == 0) ? "WHERE " : "AND ").append("F.idFilial = :filial ");
	   	}
	   	if (!StringUtils.isBlank(map.getString("pessoa.nmPessoa"))) {
	   		c2.put("nmPessoa",map.getString("pessoa.nmPessoa"));
	   		sbWhere.append((sbWhere.length() == 0) ? "WHERE " : "AND ").append("LOWER(TP.nmPessoa) LIKE LOWER(:nmPessoa) ");
	   	}
	   	if (!StringUtils.isBlank(map.getString("dtVigenciaInicial"))) {
	   		c2.put("dtVigenciaInicial",map.getYearMonthDay("dtVigenciaInicial"));
	   		sbWhere.append((sbWhere.length() == 0) ? "WHERE " : "AND ").append("T.dtVigenciaInicial >= :dtVigenciaInicial ");
	   	}
	   	if (!StringUtils.isBlank(map.getString("dtVigenciaFinal"))) {
	   		c2.put("dtVigenciaFinal",map.getYearMonthDay("dtVigenciaFinal"));
	   		sbWhere.append((sbWhere.length() == 0) ? "WHERE " : "AND ").append("T.dtVigenciaFinal <= :dtVigenciaFinal ");
	   	}
	   	sb.append(sbWhere.toString()).append("ORDER BY F.sgFilial, TP.nmPessoa, T.dtVigenciaInicial");
	   	return new Object[]{sb.toString(),c2};
   }
   
   
   public List findTerminalVigenteByFilial(Long idFilial){
	   return findTerminalVigenteByFilial(idFilial, null);
   }   
   
   /**
    * Consulta os terminais vigentes e com vigencia futura a partir da filial. Se o parametro idTerminal for informado,
    * ira trazer tambem o terminal correspondente.
    * @param idFilial
    * @param idTerminal
    * @return
    */
   public List findTerminalVigenteByFilial(Long idFilial, Long idTerminal){
	   DetachedCriteria dc = createDetachedCriteria();
	   
	   YearMonthDay today = JTDateTimeUtils.getDataAtual();
	   
	   ProjectionList pl = Projections.projectionList()
	   								.add(Projections.property("idTerminal"), "idTerminal")
	   								.add(Projections.property("dtVigenciaInicial"), "dtVigenciaInicial")
	   								.add(Projections.property("dtVigenciaFinal"), "dtVigenciaFinal")
	   								.add(Projections.property("pes.nmPessoa"), "pessoa.nmPessoa");
	   
	   dc.setProjection(pl);
	   dc.createAlias("pessoa", "pes");
	   
	   if (idFilial != null)
		   dc.add(Restrictions.eq("filial.idFilial", idFilial));
	   
	   Criterion vigencia =  Restrictions.ge("this.dtVigenciaFinal",today);
	   
		if (idTerminal != null) {
			vigencia = Restrictions.or(
							vigencia,
							Restrictions.eq("id", idTerminal));	   
	    }
		
	   dc.add(vigencia);	   	   
	   dc.addOrder(Order.asc("pes.nmPessoa"));	   
	   dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Terminal.class));
	   
	   return findByDetachedCriteria(dc);
   }

   /**
    * Consulta os terminais vigentes ou com vigencia futura a partir da filial.
    * @param idFilial
    * @return
    */
   public List findTerminalVigenteOrVigenciaFuturaByFilial(Long idFilial){
	   DetachedCriteria dc = createDetachedCriteria();
	   
	   YearMonthDay today = JTDateTimeUtils.getDataAtual();
	   
	   ProjectionList pl = Projections.projectionList()
	   								.add(Projections.property("idTerminal"), "idTerminal")
	   								.add(Projections.property("pes.nmPessoa"), "pessoa.nmPessoa");
	   
	   dc.setProjection(pl);
	   dc.createAlias("pessoa", "pes");
	   
	   if (idFilial != null)
		   dc.add(Restrictions.eq("filial.idFilial", idFilial));
	   
	   dc.add(Restrictions.or(Restrictions.ge("dtVigenciaInicial",today), Restrictions.and(
			   	Restrictions.le("dtVigenciaInicial",today),Restrictions.or(
			   							Restrictions.isNull("dtVigenciaFinal"),
										Restrictions.ge("dtVigenciaFinal",today)))));
		   
	   dc.addOrder(Order.asc("pes.nmPessoa"));	   
	   dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Terminal.class));
	   
	   return findByDetachedCriteria(dc);
   }
   
   /**
    * Verifica se a vigencia do terminal esta dentro do periodo informado
    * @param idTerminal
    * @param dtVigenciaInicial
    * @param dtVigenciaFinal
    * @return
    */
   public boolean findTerminalValidaVigencia(Long idTerminal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idTerminal",idTerminal));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
   }

   public List findCombo(TypedFlatMap criteria) {
	   ProjectionList pl = Projections.projectionList()
	   					.add(Projections.property("P.nmPessoa"), "pessoa.nmPessoa")
	   					.add(Projections.property("idTerminal"), "idTerminal")
	   					.add(Projections.property("dtVigenciaInicial"), "dtVigenciaInicial")
	   					.add(Projections.property("dtVigenciaFinal"), "dtVigenciaFinal");
	   
	   DetachedCriteria dc = createDetachedCriteria()
	   					  .setProjection(pl)
	   					  .createAlias("pessoa","P");
	   
	   if (criteria.getLong("filial.idFilial") != null)
		   dc.add(Restrictions.eq("filial.idFilial", criteria.getLong("filial.idFilial")));
	   
	   dc.addOrder(Order.asc("P.nmPessoa"));
	   
	   dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Terminal.class));
	   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
   }
   
}