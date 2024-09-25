package com.mercurio.lms.municipios.model.dao;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.Feriado;
import com.mercurio.lms.municipios.model.Municipio;
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
public class FeriadoDAO extends BaseCrudDao<Feriado, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Feriado.class;
    }

	protected void initFindByIdLazyProperties(Map fetchModes) {		
		fetchModes.put("unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("pais", FetchMode.JOIN);	
		fetchModes.put("municipio", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map fetchModes) {		
		fetchModes.put("unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("pais", FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
	}

	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = montaQuery(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
		
	}
	
	/**
	 * Define o tipo de abrangecia de cada feriado retornado na consulta
	 * @param rspList 
	 * @return
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		SqlTemplate sql = montaQuery(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(false), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

	}
	
	/**
	 * Cria a HQL para consulta de feriados
	 * @param criteria
	 * @return
	 */
	private SqlTemplate montaQuery(Map criteria){
		SqlTemplate sql = new SqlTemplate();
		   		   
		sql.addFrom(Feriado.class.getName()+ " as f " +  
			"left join fetch f.unidadeFederativa " +    			   
			"left join fetch f.pais " +
			"left join fetch f.municipio");   	   
		       	   
		sql.addCriteria("f.unidadeFederativa.idUnidadeFederativa", "=", ((String)((Map)criteria.get("unidadeFederativa")).get("idUnidadeFederativa")), Long.class);
		sql.addCriteria("f.pais.idPais", "=", ((String)((Map)criteria.get("pais")).get("idPais")), Long.class);
		sql.addCriteria("f.municipio.idMunicipio", "=", ((String)((Map)criteria.get("municipio")).get("idMunicipio")), Long.class);
		sql.addCriteria("f.tpFeriado", "=", (String)criteria.get("tpFeriado"));
		sql.addCriteria("f.dtVigenciaInicial", ">=", (String)criteria.get("dtVigenciaInicial"), YearMonthDay.class);
		sql.addCriteria("f.dtVigenciaFinal", "<=", (String)criteria.get("dtVigenciaFinal"), YearMonthDay.class);
		   
		String dsFeriado = (String)criteria.get("dsFeriado");
		if (StringUtils.isNotBlank(dsFeriado)) {
			sql.addCriteria("lower(f.dsFeriado)", "like", (String)dsFeriado.toLowerCase());
		}
		
		if (StringUtils.isNotBlank((String)criteria.get("blFacultativo"))) {
			sql.addCriteria("f.blFacultativo", "=", criteria.get("blFacultativo").equals("S")? Boolean.TRUE : Boolean.FALSE);
		}
		
		if (StringUtils.isNotBlank((String)criteria.get("dtFeriado"))){
			YearMonthDay dtFeriado = (YearMonthDay) ReflectionUtils.getConverterInstance().convert((String)criteria.get("dtFeriado"), YearMonthDay.class);
			sql.addCustomCriteria("to_char(f.dtFeriado,'dd/MM') = to_char(?,'dd/MM')");
			sql.addCriteriaValue(dtFeriado);
		}
    	   
    	   if (StringUtils.isNotBlank((String)(criteria.get("abrangencia")))){
    		   
    		   String abrangencia = (String)(criteria.get("abrangencia"));
    		   
    		   // Se a abrangencia for Municipal
    		   if (abrangencia.equals("M"))
    			   sql.addCustomCriteria("f.municipio.idMunicipio is not null");
    		   
    		   // Se a abrangencia for Estadual
    		   else if (abrangencia.equals("E")) {
    			   sql.addCustomCriteria("f.unidadeFederativa.idUnidadeFederativa is not null");
    			   sql.addCustomCriteria("f.municipio.idMunicipio is null");
    	       
    		   // Se a abrangencia for Nacional
    		   } else if (abrangencia.equals("N")){
    			   sql.addCustomCriteria("f.pais.idPais is not null");
    			   sql.addCustomCriteria("f.unidadeFederativa.idUnidadeFederativa is null");
    			   sql.addCustomCriteria("f.municipio.idMunicipio is null");
    			   
      		   // Se a abrangencia for Mundial
    		   } else if (abrangencia.equals("W")){    			   
    			   sql.addCustomCriteria("f.pais.idPais is null");
    			   sql.addCustomCriteria("f.unidadeFederativa.idUnidadeFederativa is null");
    			   sql.addCustomCriteria("f.municipio.idMunicipio is null");
    		   }
    	   }    	    
    	   
    	   sql.addOrderBy("f.dtFeriado");
    	   sql.addOrderBy("f.dtVigenciaInicial");
    	   // O case tem que ser repetido no Order By pq o Hibernate nao projeta o alias 'abrangencia'
    	   sql.addOrderBy("case when f.municipio.idMunicipio is not null then 'Municipal' "
								     +"		 when (f.unidadeFederativa.idUnidadeFederativa is not null "
								     +"	  		  and f.municipio.idMunicipio is null) then 'Estadual' "
								     +"		 when (f.pais.idPais is not null "
								     +"			  and f.unidadeFederativa.idUnidadeFederativa is null "
								     +"			  and f.municipio.idMunicipio is null) then 'Nacional'"
								     +"		 else 'Mundial'"								     
								     +"		 end");
    	   sql.addOrderBy("f.tpFeriado");
    	   
		   return sql;
	}
	
	
	/**
	 * Verifica se o feriado ja esta cadastrado na mesma vigencia e abrangencia
	 * @param idFeriado
	 * @param idMunicipio
	 * @param idUF
	 * @param idPais
	 * @param dtFeriado
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return TRUE se o feriado ja esta vigente para a abrangencia informada, FALSE caso contrario
	 */
	public boolean verificaFeriadoAbrangencia(Long idFeriado, Long idMunicipio, Long idUF, Long idPais, YearMonthDay dtFeriado, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(Feriado.class.getName(),"F");
		
		if (idMunicipio != null){
			
			hql.addCriteria("F.municipio.idMunicipio","=",idMunicipio);
			
		} else if (idUF != null){
			
			hql.addCriteria("F.unidadeFederativa.idUnidadeFederativa","=",idUF);
			hql.addCustomCriteria("F.municipio.idMunicipio is null");
			
		} else if (idPais != null){
			
			hql.addCriteria("F.pais.idPais","=",idPais);
			hql.addCustomCriteria("F.municipio.idMunicipio is null");
			hql.addCustomCriteria("F.unidadeFederativa.idUnidadeFederativa is null");
			
		} else {
			
			hql.addCustomCriteria("F.pais.idPais is null");
			hql.addCustomCriteria("F.municipio.idMunicipio is null");
			hql.addCustomCriteria("F.unidadeFederativa.idUnidadeFederativa is null");
			
		}
			
		hql.addCustomCriteria("to_char(F.dtFeriado,'dd/MM') = to_char(?,'dd/MM')");
		hql.addCriteriaValue(dtFeriado);			
				
		JTVigenciaUtils.getHqlVigencia(hql,dtVigenciaInicial,dtVigenciaFinal,"F");
		
		hql.addCriteria("F.idFeriado","!=",idFeriado);
		
		Integer count = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
		
		return count.intValue() > 0;
	}
	
	/**
	 * Verifica se a data ja esta cadastrada como feriado para uma abrangencia diferente da informada
	 * @param dtFeriado
	 * @return TRUE se o feriado ja esta cadastrado para outras abrangencias, FALSE caso contrario
	 */
	public boolean verificaFeriadoExistente(Long idFeriado, Long idMunicipio, Long idUF, Long idPais, YearMonthDay dtFeriado, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(Feriado.class.getName(),"F");
		
		if (idMunicipio != null){
			
			hql.addCustomCriteria("F.municipio.idMunicipio is null");
			
		} else if (idUF != null){
			
			hql.addCustomCriteria("(F.unidadeFederativa.idUnidadeFederativa IS NULL OR F.municipio.idMunicipio IS NOT NULL)");
			
		} else if (idPais != null){
			
			hql.addCustomCriteria("(F.pais.idPais IS NULL " +
						  			"OR (F.municipio.idMunicipio IS NOT NULL OR " +
									"F.unidadeFederativa.idUnidadeFederativa IS NOT NULL))");

		} else { 

			hql.addCustomCriteria("(F.pais.idPais IS NOT NULL " +
		  							"OR (F.municipio.idMunicipio IS NOT NULL OR " +
									"F.unidadeFederativa.idUnidadeFederativa IS NOT NULL))");

		}		
			
		hql.addCustomCriteria("to_char(F.dtFeriado,'dd/MM') = to_char(to_date(?,'yyyy-MM-dd'),'dd/MM')");
		hql.addCriteriaValue(dtFeriado.toString());
				
		JTVigenciaUtils.getHqlVigencia(hql,dtVigenciaInicial,dtVigenciaFinal,"F");
		
		hql.addCriteria("F.idFeriado","=",idFeriado);
		
		Integer count = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
		
		return count.intValue() > 0;
	}
	
	public List findFeriadosVigentesByIdMunicipio(Long idMunicipio){
		StringBuffer hql = new StringBuffer()
		.append("select new Map(fer.tpFeriado as tpFeriado, ")
		.append("fer.blFacultativo as blFacultativo, ")
		.append("fer.dtFeriado as dtFeriado, ")
		.append("fer.dtFeriado as dtFeriado, ")
		.append("fer.dsFeriado as dsFeriado, ")
		.append("mun.nmMunicipio as municipio, ")
		.append("uf.nmUnidadeFederativa as unidadeFederativa, ")
		.append("uf.sgUnidadeFederativa as sgUnidadeFederativa, ")
		.append("pais.nmPais as pais) ")
		 
		.append("from "+Feriado.class.getName()+"  as fer ")
		.append("left outer join fer.unidadeFederativa as uf ")
		.append("left outer join fer.municipio as mun ")
		.append("left outer join fer.pais as pais ")
		.append("where mun.idMunicipio = ? ")
		.append("and fer.dtVigenciaInicial <= ? ")
		.append("and fer.dtVigenciaFinal >= ? ");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipio,dataAtual,dataAtual});
		
		return lista;
		
	}
	
	/**
	 * Retorna uma lista (string) com o atributo DT_FERIADO (DD-MM) vigentes de um Município
	 * @param idMunicipio
	 * @return
	 */
	public List findAllDtFeriadosVigentesByIdMunicipio(Long idMunicipio) {
		StringBuilder query = new StringBuilder();

		query.append(" SELECT ");
		query.append("   /*+ INDEX(feriado0_ FERI_DT_FERIADO_IDX_02)*/ ");
		query.append("   TO_CHAR(feriado0_.DT_FERIADO, 'dd-MM') AS col_0_0_ ");
		query.append(" FROM FERIADO feriado0_ ");
		query.append(" WHERE (feriado0_.ID_MUNICIPIO         = :idMunicipio ");
		query.append(" OR (feriado0_.ID_MUNICIPIO           IS NULL) ");
		query.append(" AND (feriado0_.ID_UNIDADE_FEDERATIVA IS NULL) ");
		query.append(" AND (feriado0_.ID_PAIS               IS NULL) ");
		query.append(" OR (feriado0_.ID_MUNICIPIO           IS NULL) ");
		query.append(" AND feriado0_.ID_UNIDADE_FEDERATIVA   = ");
		query.append("   (SELECT municipio1_.ID_UNIDADE_FEDERATIVA ");
		query.append("   FROM MUNICIPIO municipio1_ ");
		query.append("   WHERE municipio1_.ID_MUNICIPIO= :idMunicipio ");
		query.append("   ) ");
		query.append(" OR (feriado0_.ID_MUNICIPIO           IS NULL) ");
		query.append(" AND (feriado0_.ID_UNIDADE_FEDERATIVA IS NULL) ");
		query.append(" AND feriado0_.ID_PAIS                 = ");
		query.append("   (SELECT unidadefed3_.ID_PAIS ");
		query.append("   FROM MUNICIPIO municipio2_, ");
		query.append("     UNIDADE_FEDERATIVA unidadefed3_ ");
		query.append("   WHERE municipio2_.ID_UNIDADE_FEDERATIVA=unidadefed3_.ID_UNIDADE_FEDERATIVA ");
		query.append("   AND municipio2_.ID_MUNICIPIO           = :idMunicipio ");
		query.append("   ) ) ");
		query.append(" AND feriado0_.DT_VIGENCIA_INICIAL<=to_date( :dataAtual, 'yyyy-MM-dd') ");
		query.append(" AND feriado0_.DT_VIGENCIA_FINAL  >=to_date( :dataAtual, 'yyyy-MM-dd') ");


		String sql = query.toString();

		sql = sql.replaceAll(":idMunicipio", idMunicipio.toString());

		String dataAtual = JTDateTimeUtils.getDataAtual().toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
		sql = sql.replaceAll(":dataAtual", "'"+dataAtual+"'");

		List lista = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		return lista;
	}

	/**
	 * Retorna uma lista (string) com o feriados nacionais e mundial vigente por município
	 * @param idMunicipio
	 * @return
	 */
	public List findDtFeriadosNacionaisEMundialByIdMunicipio(Long idMunicipio) {
		StringBuilder query = new StringBuilder();

		query.append(" SELECT ");
		query.append("   TO_CHAR(feriado0_.DT_FERIADO, 'dd-MM') AS col_0_0_ ");
		query.append(" FROM FERIADO feriado0_ ");
		query.append(" WHERE feriado0_.ID_MUNICIPIO           IS NULL ");
		query.append(" AND feriado0_.ID_UNIDADE_FEDERATIVA IS NULL ");
		query.append(" AND ( feriado0_.ID_PAIS IS NULL ");
		query.append(" OR feriado0_.ID_PAIS                 = ");
		query.append("   (SELECT unidadefed3_.ID_PAIS ");
		query.append("   FROM MUNICIPIO municipio2_, ");
		query.append("     UNIDADE_FEDERATIVA unidadefed3_ ");
		query.append("   WHERE municipio2_.ID_UNIDADE_FEDERATIVA=unidadefed3_.ID_UNIDADE_FEDERATIVA ");
		query.append("   AND municipio2_.ID_MUNICIPIO           = :idMunicipio ");
		query.append("   ) )");
		query.append(" AND feriado0_.DT_VIGENCIA_INICIAL<=to_date( :dataAtual, 'yyyy-MM-dd') ");
		query.append(" AND feriado0_.DT_VIGENCIA_FINAL  >=to_date( :dataAtual, 'yyyy-MM-dd') ");

		String sql = query.toString();

		sql = sql.replaceAll(":idMunicipio", idMunicipio.toString());

		String dataAtual = JTDateTimeUtils.getDataAtual().toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
		sql = sql.replaceAll(":dataAtual", "'"+dataAtual+"'");

		List lista = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		return lista;
	}

	/**
	 * verifica se existe
	 * @param idMunicipio
	 * @param dtFeriado
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public List findFeriadoByMunicipio(List idMunicipios, List dtFeriados, List dtVigenciaInicials, List dtVigenciaFinals) {
		SqlTemplate sqlTemplate = new SqlTemplate();
		
		sqlTemplate.addProjection("distinct f.dtFeriado ");
		sqlTemplate.addFrom("Feriado f");
		sqlTemplate.addFrom("Municipio m inner join m.unidadeFederativa uf inner join uf.pais p");
		sqlTemplate.addCustomCriteria("(f.municipio.idMunicipio is null or f.municipio.idMunicipio = m.idMunicipio)");
		sqlTemplate.addCustomCriteria("(f.unidadeFederativa.idUnidadeFederativa is null or f.unidadeFederativa.idUnidadeFederativa = uf.idUnidadeFederativa)");		            
		sqlTemplate.addCustomCriteria("(f.pais.idPais is null or f.pais.idPais = p.idPais)");

		StringBuffer createCriteria = new StringBuffer("( ");
    	String token = null;
    	
    	for(Iterator i = idMunicipios.iterator(); i.hasNext();) {
    		Long idMunicipio = (Long)i.next();
    		
    		if (token == null)
    			token = " OR ";
    		else
    			createCriteria.append(token); 
    		
    		createCriteria.append(" (m.id = ?) ");
    		
    		sqlTemplate.addCriteriaValue(idMunicipio);
    	}
    	
    	sqlTemplate.addCustomCriteria(createCriteria.append(") ").toString());

		createCriteria = new StringBuffer("( ");
    	token = null;
    	
    	for(int x = 0; x < dtFeriados.size(); x++) {
    		YearMonthDay dtFeriado = (YearMonthDay)dtFeriados.get(x);
    		YearMonthDay dtVigenciaInicial = (YearMonthDay)dtVigenciaInicials.get(x);
    		YearMonthDay dtVigenciaFinal = (YearMonthDay)dtVigenciaFinals.get(x);
    		
    		if (token == null)
    			token = " OR ";
    		else
    			createCriteria.append(token);
    		
    		createCriteria.append(" ( f.dtVigenciaInicial <= to_date(?,'yyyy-mm-dd') AND f.dtVigenciaFinal >= to_date(?,'yyyy-mm-dd') AND to_char(f.dtFeriado,'dd/MM') = to_char(to_date(?,'yyyy-mm-dd'),'dd/MM')) ");
    		
    		sqlTemplate.addCriteriaValue(dtVigenciaInicial.toString());
    		sqlTemplate.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal).toString());
    		sqlTemplate.addCriteriaValue(dtFeriado.toString());
    	}
    	sqlTemplate.addCustomCriteria(createCriteria.append(") ").toString());

    	return getAdsmHibernateTemplate().find(sqlTemplate.getSql(),sqlTemplate.getCriteria());
	}
	
	/**
	 * FIXME: AJUSTAR ESSA FUNCAO, ESTA ASSIM PQ FOI ALTERADO A IMPLEMENTACAO DO METODO PARA ATENDER OUTRO CASO.
	 * PASSAR ESSA IMPLEMENTACAO PARA A SERVICE.
	 * @param idMunicipio
	 * @param dtFeriado
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean validateExisteFeriadoByMunicipio(Long idMunicipio, YearMonthDay dtFeriado, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		List idsMunicipio       = new ArrayList();
	 	List dtFeriados         = new ArrayList();
		List dtVigenciaIniciais = new ArrayList();
		List dtVigenciaFinais   = new ArrayList();
		idsMunicipio.add(idMunicipio);
		dtFeriados.add(dtFeriado);
		dtVigenciaIniciais.add(dtVigenciaInicial);
		dtVigenciaFinais.add(dtVigenciaFinal);
		
		
		return findFeriadoByMunicipio(idsMunicipio,dtFeriados,dtVigenciaIniciais,dtVigenciaFinais).size() > 0;
	}
	
	 public Boolean validateDiaUtil(YearMonthDay dtUtil, Long idMunicipio) {
	    	StringBuilder sql = new StringBuilder();
	    	
	    	sql.append("SELECT ");
	    	sql.append("    'X' ");
	    	sql.append("FROM ");
	    	sql.append("    DIA_SEMANA DIAS,");
	    	sql.append("    PAIS PAIS,");
	    	sql.append("    UNIDADE_FEDERATIVA UNFE,");
	    	sql.append("    MUNICIPIO MUNI ");
	    	sql.append("WHERE MUNI.ID_MUNICIPIO = ? ");
	    	sql.append("AND DIAS.ID_PAIS = PAIS.ID_PAIS ");
	    	sql.append("AND PAIS.ID_PAIS = UNFE.ID_PAIS ");
	    	sql.append("AND MUNI.ID_UNIDADE_FEDERATIVA = UNFE.ID_UNIDADE_FEDERATIVA ");
	    	sql.append("AND 'S' = DECODE(TO_CHAR(to_date(?,'YYYY-MM-DD'),'D'),");
	    	sql.append("    '1', DIAS.BL_UTIL_DOM,");
	    	sql.append("    '2', DIAS.BL_UTIL_SEG,");
	    	sql.append("    '3', DIAS.BL_UTIL_TER,");
	    	sql.append("    '4', DIAS.BL_UTIL_QUA,");
	    	sql.append("    '5', DIAS.BL_UTIL_QUI,");
	    	sql.append("    '6', DIAS.BL_UTIL_SEX,");
	    	sql.append("    '7', DIAS.BL_UTIL_SAB, NULL) ");
	    	sql.append("AND NOT EXISTS (");
	    	sql.append("    SELECT 'X' ");
	    	sql.append("    FROM FERIADO FERI");
	    	sql.append("    WHERE TO_CHAR(to_date(?,'YYYY-MM-DD'), 'DD/MM') = TO_CHAR(FERI.DT_FERIADO, 'DD/MM')");
	    	sql.append("    AND TO_DATE(?,'YYYY-MM-DD') BETWEEN FERI.DT_VIGENCIA_INICIAL AND FERI.DT_VIGENCIA_FINAL");
	    	sql.append("    AND (FERI.ID_MUNICIPIO = MUNI.ID_MUNICIPIO OR FERI.ID_MUNICIPIO IS NULL)");
	    	sql.append("    AND (FERI.ID_UNIDADE_FEDERATIVA = UNFE.ID_UNIDADE_FEDERATIVA OR FERI.ID_UNIDADE_FEDERATIVA IS NULL)");
	    	sql.append("    AND (FERI.ID_PAIS = PAIS.ID_PAIS OR FERI.ID_PAIS IS NULL)");
	    	sql.append(")");	    		    
	    	
	    	Object retorno = getAdsmHibernateTemplate().findByIdBySql(sql.toString(), new Object[]{idMunicipio, dtUtil.toString(), dtUtil.toString(), dtUtil.toString()}, null);
	    	
	    	if(retorno == null) {
	    		return Boolean.FALSE;
	    	} else {
	    		return Boolean.TRUE;
	    	}	    		    
	    }
	 
	/**
	 * Retorna uma lista (feriados) vigentes de
	 * um Município
	 * 
	 * @param idMunicipio
	 * @return
	 */
	public List findAllFeriadosByIdMunicipio(Long idMunicipio) {
		StringBuffer hql = new StringBuffer()
		.append("select f ")		 
		.append("from " + Feriado.class.getName() + " as f ")
		.append("where (f.municipio.id = ? ")
		
		.append("or (f.municipio.id is null and f.unidadeFederativa.id is null and f.pais.id is null) ")
		.append("or (f.municipio.id is null and f.unidadeFederativa.id = ")
			.append("(select m1.unidadeFederativa.id from " + Municipio.class.getName() + " as m1 where m1.id = ?)) ")
		.append("or (f.municipio.id is null and f.unidadeFederativa.id is null and f.pais.id = ")
			.append("(select uf2.pais.id from " + Municipio.class.getName() + " as m2 ")
			.append("inner join m2.unidadeFederativa as uf2 where m2.id = ?))) ");
		
		List lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idMunicipio, idMunicipio, idMunicipio });
		return lista;
	}
}
