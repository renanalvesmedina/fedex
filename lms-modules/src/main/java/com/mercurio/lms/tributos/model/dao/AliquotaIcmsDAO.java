package com.mercurio.lms.tributos.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicaï¿½ï¿½o
 * atravï¿½s do suporte ao Hibernate em conjunto com o Spring.
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AliquotaIcmsDAO extends BaseCrudDao<AliquotaIcms, Long>
{

	/**
	 * Nome da classe que o DAO ï¿½ responsï¿½vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaIcms.class;
    }
    
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativaDestino", FetchMode.JOIN);
		lazyFindById.put("unidadeFederativaOrigem", FetchMode.JOIN);
		lazyFindById.put("tpTipoFrete", FetchMode.JOIN);
		lazyFindById.put("tipoTributacaoIcms", FetchMode.JOIN);
		lazyFindById.put("embasamento", FetchMode.JOIN);
		lazyFindById.put("regiaoGeografica", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("unidadeFederativaDestino", FetchMode.JOIN);
		lazyFindPaginated.put("unidadeFederativaOrigem", FetchMode.JOIN);
		lazyFindPaginated.put("tpTipoFrete", FetchMode.JOIN);
		lazyFindPaginated.put("tipoTributacaoIcms", FetchMode.JOIN);
		lazyFindPaginated.put("embasamento", FetchMode.JOIN);
		lazyFindPaginated.put("regiaoGeografica", FetchMode.JOIN);		
	}
   
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		TypedFlatMap map = new TypedFlatMap();
		map.putAll(criteria);
		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("al");
		
		sql.addInnerJoin("AliquotaIcms","al");
		sql.addInnerJoin("fetch al.unidadeFederativaOrigem","ufOrigem");
		sql.addLeftOuterJoin("fetch al.unidadeFederativaDestino","ufDestino");
		sql.addInnerJoin("fetch al.tipoTributacaoIcms","tti");
		sql.addLeftOuterJoin("fetch al.regiaoGeografica","rgf,");
				
		sql.addFrom(Domain.class.getName(),"d1");
		sql.addFrom(Domain.class.getName(),"d2");
		sql.addFrom(Domain.class.getName(),"d3");

				
		sql.addFrom( DomainValue.class.getName() + " vd1 ");
		sql.addFrom( DomainValue.class.getName() + " vd2 ");
		sql.addFrom( DomainValue.class.getName() + " vd3 ");
		
		
		sql.addJoin("vd1.domain","d1");
		sql.addJoin("vd2.domain","d2");
		sql.addJoin("vd3.domain","d3");
		
		
		sql.addJoin("vd1.value","al.tpSituacaoTribRemetente");
		sql.addJoin("vd2.value","al.tpSituacaoTribDestinatario");
		sql.addJoin("vd3.value","al.tpTipoFrete");
		
		sql.addCriteria("d1.name","=","DM_SITUACAO_TRIBUTARIA");
		sql.addCriteria("d2.name","=","DM_SITUACAO_TRIBUTARIA");
		sql.addCriteria("d3.name","=","DM_TIPO_FRETE");
		
		sql.addCriteria("al.pcEmbute","=",map.getBigDecimal("pcEmbute"));
		sql.addCriteria("al.tpSituacaoTribDestinatario","=",map.getString("tpSituacaoTribDestinatario"));
		sql.addCriteria("ufDestino.idUnidadeFederativa","=",map.getLong("unidadeFederativaDestino.idUnidadeFederativa"));
		sql.addCriteria("al.tpSituacaoTribRemetente","=",map.getString("tpSituacaoTribRemetente"));
		sql.addCriteria("al.pcAliquota","=",map.getBigDecimal("pcAliquota"));
		sql.addCriteria("al.tpTipoFrete","=",map.getString("tpTipoFrete"));
		sql.addCriteria("ufOrigem.idUnidadeFederativa","=",map.getLong("unidadeFederativaOrigem.idUnidadeFederativa"));
		sql.addCriteria("tti.idTipoTributacaoIcms","=",map.getLong("tipoTributacaoIcms.idTipoTributacaoIcms"));
		
		sql.addCriteria("rgf.idRegiaoGeografica","=",map.getLong("regiaoGeografica.idRegiaoGeografica"));
		
		YearMonthDay dataBusca = map.getYearMonthDay("dtVigencia");
		if ( dataBusca != null ){
			sql.addCustomCriteria(" ( ? between al.dtVigenciaInicial and al.dtVigenciaFinal) ");
			sql.addCriteriaValue(dataBusca);
		}

		sql.addOrderBy("ufOrigem.sgUnidadeFederativa");
		sql.addOrderBy("ufDestino.sgUnidadeFederativa");
		
		if(map.getLong("regiaoGeografica.idRegiaoGeografica") != null){
			sql.addOrderBy(OrderVarcharI18n.hqlOrder("rgf.dsRegiaoGeografica", LocaleContextHolder.getLocale()));
		}
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", LocaleContextHolder.getLocale())); 
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd2.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd3.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy("al.dtVigenciaInicial");
		
		return getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
			
	}
	
	
	public Integer getRowCount(Map criteria) {
		
		return super.getRowCount(criteria);
	}
	
	/**
	 * @author Josï¿½ Rodrigo Moraes
	 * @since 31/07/2006
	 * 
	 * Mï¿½todo alterado/refeito para correta validaï¿½ï¿½o das vigï¿½ncias
	 * 
	 * @param aliquotaIcms
	 * @return <code>true</code> Caso existe conflito de vigï¿½ncias <br>
	 *         <code>false</code> Caso nï¿½o ocorra conflito
	 */
	public boolean findExisteVigencia(AliquotaIcms aliquotaIcms) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(AliquotaIcms.class.getName(),"ai");

		if(aliquotaIcms.getUnidadeFederativaDestino() != null){
		hql.addCriteria("ai.unidadeFederativaDestino.id","=",aliquotaIcms.getUnidadeFederativaDestino().getIdUnidadeFederativa());
		}else{
			hql.addCustomCriteria("ai.unidadeFederativaDestino.id is null");
		}
		
		if(aliquotaIcms.getRegiaoGeografica() != null ){
			hql.addCriteria("ai.regiaoGeografica.id","=", aliquotaIcms.getRegiaoGeografica().getIdRegiaoGeografica());
		}else{
			hql.addCustomCriteria("ai.regiaoGeografica.id is null");
		}
		
		hql.addCriteria("ai.unidadeFederativaOrigem.id","=",aliquotaIcms.getUnidadeFederativaOrigem().getIdUnidadeFederativa());		
		hql.addCriteria("ai.tpSituacaoTribRemetente","=",aliquotaIcms.getTpSituacaoTribRemetente().getValue());
		hql.addCriteria("ai.tpSituacaoTribDestinatario","=",aliquotaIcms.getTpSituacaoTribDestinatario().getValue());
		hql.addCriteria("ai.tpTipoFrete","=",aliquotaIcms.getTpTipoFrete().getValue());		
		hql.addCriteria("ai.id","!=",aliquotaIcms.getIdAliquotaIcms());		
		
		JTVigenciaUtils.getHqlValidaVigencia(hql,
				                             "ai.dtVigenciaInicial",
				                             "ai.dtVigenciaFinal",
				                             aliquotaIcms.getDtVigenciaInicial(),
				                             aliquotaIcms.getDtVigenciaFinal());		

		Integer resultado = this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
		
		return resultado.intValue() > 0;
		
	}
	
	/**
	 * Busca AlicotaIcms por UF Origem/UF Destino ou Regiao Geografica Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
	
    	hql.addProjection("al");
    	hql.addInnerJoin(AliquotaIcms.class.getName(), "al");
    	hql.addInnerJoin("fetch al.tipoTributacaoIcms", "tti");
    	hql.addLeftOuterJoin("fetch al.embasamento", "emb");

    	hql.addCriteria("al.unidadeFederativaOrigem.id", "=", idUfOrigem);
    	hql.addCustomCriteria("al.unidadeFederativaDestino.id IS NULL");
    	hql.addCustomCriteria("al.regiaoGeografica.id IS NULL");
    	if(StringUtils.isNotBlank(tpSituacaoTribRemetente)) {
    		hql.addCriteria("al.tpSituacaoTribRemetente", "=", tpSituacaoTribRemetente);
    	}
    	if(StringUtils.isNotBlank(tpSituacaoTribDestinatario)) {
        	hql.addCriteria("al.tpSituacaoTribDestinatario", "=", tpSituacaoTribDestinatario);
    	}
    	if(StringUtils.isNotBlank(tpFrete)) {
        	hql.addCriteria("al.tpTipoFrete", "=", tpFrete);
    	}
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "al.dtVigenciaInicial", "al.dtVigenciaFinal",dtVigencia);

    	List<AliquotaIcms> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	if(!result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }
	
	
	
	
	
	/**
	 * Busca AlicotaIcms por UF Origem/UF Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("al");
    	hql.addInnerJoin(AliquotaIcms.class.getName(), "al");
    	hql.addInnerJoin("fetch al.tipoTributacaoIcms", "tti");
    	hql.addLeftOuterJoin("fetch al.embasamento", "emb");
    	
    	hql.addCriteria("al.unidadeFederativaOrigem.id", "=", idUfOrigem);
    	hql.addCriteria("al.unidadeFederativaDestino.id", "=", idUfDestino);

    	if(StringUtils.isNotBlank(tpSituacaoTribRemetente)) {
    		hql.addCustomCriteria("al.regiaoGeografica.id IS NULL");
    	hql.addCriteria("al.tpSituacaoTribRemetente", "=", tpSituacaoTribRemetente);
    	}
    	if(StringUtils.isNotBlank(tpSituacaoTribDestinatario)) {
    	hql.addCriteria("al.tpSituacaoTribDestinatario", "=", tpSituacaoTribDestinatario);
    	}
    	if(StringUtils.isNotBlank(tpFrete)) {
    	hql.addCriteria("al.tpTipoFrete", "=", tpFrete);
    	}
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "al.dtVigenciaInicial", "al.dtVigenciaFinal",dtVigencia);
    	
    	List<AliquotaIcms> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	if(!result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }
    	
	/**
	 * Busca a aliquota ICMS vigente para a Regiao Geografica da UF de destino. 
	 * 
	 * @param idUfOrigem
	 * @param idUfDestino
	 * @param idPais
	 * @param tpSituacaoTribRemetente
	 * @param tpSituacaoTribDestinatario
	 * @param tpFrete
	 * @param dtVigencia
	 * @return
	 */
	public AliquotaIcms findByRegiaoGeograficaDestinoVigente(Long idUfOrigem,Long idUfDestino, Long idPais, String tpSituacaoTribRemetente, 
			String tpSituacaoTribDestinatario, String tpFrete,YearMonthDay dtVigencia){
		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("al");
    	hql.addFrom(UnidadeFederativa.class.getName(), "uf");
    	hql.addFrom(AliquotaIcms.class.getName(), "al");
   	
    	hql.addCriteria("al.unidadeFederativaOrigem.id", "=", idUfOrigem);
    	hql.addCriteria("uf.id", "=", idUfDestino);
    	hql.addCriteria("uf.pais.id", "=", idPais);
    	hql.addCustomCriteria("al.regiaoGeografica = uf.regiaoGeografica");
    	hql.addCriteria("al.tpSituacaoTribRemetente", "=", tpSituacaoTribRemetente);
    	hql.addCriteria("al.tpSituacaoTribDestinatario", "=", tpSituacaoTribDestinatario);
    	hql.addCriteria("al.tpTipoFrete", "=", tpFrete);

    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "al.dtVigenciaInicial", "al.dtVigenciaFinal",dtVigencia);
    	
    	List<AliquotaIcms> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	if(!result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
	}
    	
	/**
	 * Verifica se existe aliquota de ICMS vigente baseado apenas nas UFs de
	 * origem e destino.
	 * 
	 * @param idUfOrigem
	 *            identificador da UF de origem
	 * @param idUfDestino
	 *            identificador da UF de destino
	 * @param dtVigencia
	 *            data para verificar a vigência
	 * @return <code>true</code> caso exista aliquota vigente e
	 *         <code>false</code> caso contrário
	 * @author Luis Carlos Poletto
	 */
	public Boolean findExistsAliquotaIcms(Long idUfOrigem, Long idUfDestino, YearMonthDay dtVigencia) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select distinct 1 from ");
		hql.append(AliquotaIcms.class.getName());
		hql.append(" ai where ai.unidadeFederativaOrigem.id = ? ");
		hql.append(" and ai.unidadeFederativaDestino.id = ? ");
		hql.append(" and ai.dtVigenciaInicial <= ? ");
		hql.append(" and ai.dtVigenciaFinal >= ? ");
	
		List<Object> result = getAdsmHibernateTemplate().find(hql.toString(),
				new Object[] { idUfOrigem, idUfDestino, dtVigencia, dtVigencia });
	
		if (!result.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Busca AlicotaIcms por UF Origem/UF Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public List<AliquotaIcms> findAliquotaIcmsVigente(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("al");
    	hql.addInnerJoin(AliquotaIcms.class.getName(), "al");
    	hql.addInnerJoin("fetch al.tipoTributacaoIcms", "tti");
    	hql.addLeftOuterJoin("fetch al.embasamento", "emb");
    	
    	hql.addCriteria("al.unidadeFederativaOrigem.id", "=", idUfOrigem);
    	hql.addCriteria("al.unidadeFederativaDestino.id", "=", idUfDestino);

    	if(StringUtils.isNotBlank(tpSituacaoTribRemetente)) {
    		hql.addCustomCriteria("al.regiaoGeografica.id IS NULL");
    	hql.addCriteria("al.tpSituacaoTribRemetente", "=", tpSituacaoTribRemetente);
    	}
    	if(StringUtils.isNotBlank(tpSituacaoTribDestinatario)) {
    	hql.addCriteria("al.tpSituacaoTribDestinatario", "=", tpSituacaoTribDestinatario);
    	}
    	if(StringUtils.isNotBlank(tpFrete)) {
    	hql.addCriteria("al.tpTipoFrete", "=", tpFrete);
    	}
    	
    	List<AliquotaIcms> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	if(!result.isEmpty()) {
    		return result;
    	}else{
    		return null;
    	}
    }
	
	
    	
	/**
	 * Busca AlicotaIcms por UF Origem/REGIAO GEOGRAFICA Destino
	 * @author Andrï¿½ Valadas
	 * @since 05/05/2009
	 * 
	 * @return
	 */
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, Long idPais, YearMonthDay dtVigencia){
		StringBuilder query = new StringBuilder();
		query.append("SELECT A.PC_ALIQUOTA AS pcAliquota, ");
		query.append(" A.PC_EMBUTE AS pcEmbute");
		query.append(" FROM ALIQUOTA_ICMS A");
		query.append("		,UNIDADE_FEDERATIVA U");
		query.append(" WHERE A.ID_UNIDADE_FEDERATIVA_ORIGEM = :idUfOrigem");
		query.append("	AND U.ID_UNIDADE_FEDERATIVA = :idUfDestino");
		query.append("	AND U.ID_PAIS = :idPais");
		query.append("	AND	A.ID_REGIAO_GEOGRAFICA_DESTINO = U.ID_REGIAO_GEOGRAFICA");
		query.append("	AND A.ID_UNIDADE_FEDERATIVA_DESTINO IS NULL ");
		// Vigencias
		query.append("	AND (A.DT_VIGENCIA_INICIAL <= :dtVigencia AND A.DT_VIGENCIA_FINAL >= :dtVigencia)");

		/** Alias dos filtros */
		Map criteria = new HashMap();
		criteria.put("idUfOrigem", idUfOrigem);
		criteria.put("idUfDestino", idUfDestino);
		criteria.put("idPais", idPais);
		criteria.put("dtVigencia", dtVigencia);

		/** Configura o retorno da consulta */
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("pcAliquota", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("pcEmbute", Hibernate.BIG_DECIMAL);
			} 
		};
		List<Object[]> result = getAdsmHibernateTemplate().findPaginatedBySql(query.toString(), Integer.valueOf(1), Integer.valueOf(1), criteria, configSql).getList();

		List<AliquotaIcms> toReturn = new ArrayList<AliquotaIcms>(result.size());
		for(Object[] pcAliquota : result) {
			AliquotaIcms ai = new AliquotaIcms();
			ai.setPcAliquota((BigDecimal)pcAliquota[0]);
			ai.setPcEmbute((BigDecimal)pcAliquota[1]);
			toReturn.add(ai);
		}
		
    	if(!toReturn.isEmpty()) {
    		return toReturn.get(0);
    	}
    		return null;
    	}
	public AliquotaIcms findAliquotaIcms(Long idUfOrigem, Long idUfDestino, Long idPais, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
		StringBuilder query = new StringBuilder();
		query.append("SELECT A.PC_ALIQUOTA AS pcAliquota");
		query.append("		,A.PC_EMBUTE AS pcEmbute");
		query.append("		,A.ID_TIPO_TRIBUTACAO_ICMS AS idTipoTributacaoIcms");
		query.append("		,E.DS_EMB_LEGAL_RESUMIDO AS dsEmbLegalResumido");
		query.append("		,E.CD_EMB_LEGAL_MASTERSAF AS cdEmbLegalMasterSaf");
		query.append("		,A.ID_ALIQUOTA_ICMS AS idAliquotaIcms");
		query.append(" FROM ALIQUOTA_ICMS A");
		query.append("		,UNIDADE_FEDERATIVA U");
		query.append("		,EMBASAMENTO_LEGAL_ICMS E");
		query.append(" WHERE A.ID_UNIDADE_FEDERATIVA_ORIGEM = :idUfOrigem");
		query.append("	AND U.ID_UNIDADE_FEDERATIVA = :idUfDestino");
		query.append("	AND U.ID_PAIS = :idPais");
		query.append("	AND	A.ID_REGIAO_GEOGRAFICA_DESTINO = U.ID_REGIAO_GEOGRAFICA");
		query.append("	AND A.ID_UNIDADE_FEDERATIVA_DESTINO IS NULL ");
		query.append("	AND A.TP_SITUACAO_TRIB_REMETENTE = :tpSituacaoTributariaRemetente");
		query.append("	AND A.TP_SITUACAO_TRIB_DESTINATARIO = :tpSituacaoTributariaDestinatario");
		query.append("	AND	A.TP_TIPO_FRETE = :tpFrete");
		query.append("	AND A.ID_EMBASAMENTO_LEGAL_ICMS = E.ID_EMBASAMENTO_LEGAL_ICMS(+)");
		// Vigencias
		query.append("	AND (A.DT_VIGENCIA_INICIAL <= :dtVigencia AND A.DT_VIGENCIA_FINAL >= :dtVigencia)");

		/** Alias dos filtros */
		Map criteria = new HashMap();
		criteria.put("idUfOrigem", idUfOrigem);
		criteria.put("idUfDestino", idUfDestino);
		criteria.put("idPais", idPais);
		criteria.put("tpSituacaoTributariaRemetente", tpSituacaoTribRemetente);
		criteria.put("tpSituacaoTributariaDestinatario", tpSituacaoTribDestinatario);
		criteria.put("tpFrete", tpFrete);
		criteria.put("dtVigencia", dtVigencia);

		/** Configura o retorno da consulta */
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("pcAliquota", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("pcEmbute", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("idTipoTributacaoIcms", Hibernate.LONG);
				sqlQuery.addScalar("dsEmbLegalResumido", Hibernate.STRING);
				sqlQuery.addScalar("cdEmbLegalMasterSaf", Hibernate.STRING);
				sqlQuery.addScalar("idAliquotaIcms", Hibernate.LONG);
    }	
		};
		List<Object[]> result = getAdsmHibernateTemplate().findPaginatedBySql(query.toString(), Integer.valueOf(1), Integer.valueOf(1), criteria, configSql).getList();
		if(!result.isEmpty()) {
			Object[] data = result.get(0);
			AliquotaIcms aliquotaIcms = new AliquotaIcms();
			aliquotaIcms.setIdAliquotaIcms(LongUtils.getLong(data[5]));
			aliquotaIcms.setPcAliquota(BigDecimalUtils.getBigDecimal(data[0]));
			TipoTributacaoIcms tributacaoIcms = new TipoTributacaoIcms();
			tributacaoIcms.setIdTipoTributacaoIcms(LongUtils.getLong(data[2]));
			aliquotaIcms.setTipoTributacaoIcms(tributacaoIcms);
			if((data[3] != null && data[3] instanceof String) || (data[4] != null && data[4] instanceof String)) {
				EmbasamentoLegalIcms embasamentoLegalIcms = new EmbasamentoLegalIcms();
				embasamentoLegalIcms.setDsEmbLegalResumido((String) data[3]);
				embasamentoLegalIcms.setCdEmbLegalMasterSaf((String) data[4]);
				aliquotaIcms.setEmbasamento(embasamentoLegalIcms);
			}

			return aliquotaIcms;
		}
    	return null;
    }
	
	/**
	 * Busca AliquotaIcms por situacao tributaria
	 * @return
	 */
    public AliquotaIcms findByUfAndSituacao(Long idUfOrigem, Long idUfDestino, String tpSituacaoTribRemetente, String tpSituacaoTribDestinatario, String tpFrete, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("al");
    	hql.addInnerJoin(AliquotaIcms.class.getName(), "al");
    	hql.addInnerJoin("fetch al.tipoTributacaoIcms", "tti");
    	hql.addLeftOuterJoin("fetch al.embasamento", "emb");
    	
    	hql.addCriteria("al.unidadeFederativaOrigem.id", "=", idUfOrigem);
    	hql.addCriteria("al.unidadeFederativaDestino.id", "=", idUfDestino);
    	hql.addCriteria("al.tpSituacaoTribRemetente", "=", tpSituacaoTribRemetente);
    	hql.addCriteria("al.tpSituacaoTribDestinatario", "=", tpSituacaoTribDestinatario);
    	hql.addCriteria("al.tpTipoFrete", "=", tpFrete);
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "al.dtVigenciaInicial", "al.dtVigenciaFinal",dtVigencia);

    	List<AliquotaIcms> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	if(!result.isEmpty()) {
    		return result.get(0);
    	}
    	return null;
    }
    
	public List findAlquotasByEmbasamento(Long idEmbasamento){
		
    	DetachedCriteria dc = createDetachedCriteria()			
		.setFetchMode("embasamento", FetchMode.JOIN)
		.add(Restrictions.eq("embasamento.id", idEmbasamento));
    	
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);    	
    			 
	}
    

}