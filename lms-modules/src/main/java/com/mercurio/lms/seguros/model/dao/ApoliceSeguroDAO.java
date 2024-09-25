package com.mercurio.lms.seguros.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ApoliceSeguroParcela;
import com.mercurio.lms.seguros.model.Seguradora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ApoliceSeguroDAO extends BaseCrudDao<ApoliceSeguro, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ApoliceSeguro.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("reguladoraSeguro",FetchMode.JOIN);
		map.put("reguladoraSeguro.pessoa",FetchMode.JOIN);
		map.put("seguradora",FetchMode.JOIN);
		map.put("seguradora.pessoa",FetchMode.JOIN);
		map.put("tipoSeguro",FetchMode.JOIN);
		map.put("moeda",FetchMode.JOIN);
		map.put("pessoa",FetchMode.JOIN);
		map.put("segurado",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("reguladoraSeguro",FetchMode.JOIN);
		map.put("reguladoraSeguro.pessoa",FetchMode.JOIN);
		map.put("seguradora",FetchMode.JOIN);
		map.put("seguradora.pessoa",FetchMode.JOIN);
		map.put("tipoSeguro",FetchMode.JOIN);
		map.put("moeda",FetchMode.JOIN);
		map.put("pessoa",FetchMode.JOIN);
	}
	
	/**
	 * LMS-7285 - Incluir relacionamento com {@link Seguradora} e {@link Pessoa}
	 * nas buscas para lookup.
	 * 
	 * @param lazyFindLookup
	 *            mapa de {@link FetchMode}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("seguradora", FetchMode.JOIN);
		lazyFindLookup.put("seguradora.pessoa", FetchMode.JOIN);
	}

	/**
	 * Retorna o numero de registros encontrados para serem populados na Grid
	 * 
	 * @param dtVigencia
	 * @param nrApolice
	 * @param idReguladora
	 * @param idSeguradora
	 * @param idTipoSeguro
	 * @param idSegurado
	 * @param idMoeda
	 * @param vlLimiteApolice
	 * @return Integer
	 */
	public Integer getRowCountByDtVigencia(Long idSegurado, YearMonthDay dtVigencia, String nrApolice, Long idReguladora, Long idSeguradora, Long idTipoSeguro, Long idMoeda, BigDecimal vlLimiteApolice) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ApoliceSeguro.class)
	    	.setProjection(Projections.rowCount()) 
	    	.setFetchMode("reguladoraSeguro",FetchMode.JOIN)
	    	.setFetchMode("reguladoraSeguro.pessoa",FetchMode.JOIN)
	    	.setFetchMode("seguradora",FetchMode.JOIN)
	    	.setFetchMode("seguradora.pessoa", FetchMode.JOIN)
	    	.setFetchMode("pessoa", FetchMode.JOIN) 
	    	.setFetchMode("tipoSeguro", FetchMode.JOIN)
			.setFetchMode("moeda", FetchMode.JOIN);
	    	
		detachedCriteria = this.getCriterionsQuery(detachedCriteria, idSegurado, dtVigencia, nrApolice, idReguladora, idSeguradora, idTipoSeguro, idMoeda, vlLimiteApolice);
		List result = super.findByDetachedCriteria(detachedCriteria);
		return (Integer) result.get(0);
	}
	
	/**
	 * Retorna os dados a serem populados na Grid.
	 * 
	 * @param dtVigencia
	 * @param nrApolice
	 * @param idReguladora
	 * @param idSeguradora
	 * @param idTipoSeguro
	 * @param idMoeda
	 * @param vlLimiteApolice
	 * @return ResultSetPage
	 */
    public ResultSetPage findPaginatedByRotaColetaEntrega(Long idSegurado, YearMonthDay dtVigencia, String nrApolice, Long idReguladora, Long idSeguradora, Long idTipoSeguro, Long idMoeda, BigDecimal vlLimiteApolice, FindDefinition findDefinition){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ApoliceSeguro.class)
	    	.setFetchMode("reguladoraSeguro",FetchMode.JOIN)
	    	.setFetchMode("reguladoraSeguro.pessoa",FetchMode.JOIN)
	    	.setFetchMode("seguradora",FetchMode.JOIN)
	    	.setFetchMode("seguradora.pessoa", FetchMode.JOIN)
	    	.setFetchMode("pessoa", FetchMode.JOIN)
	    	.setFetchMode("tipoSeguro", FetchMode.JOIN)
			.setFetchMode("moeda", FetchMode.JOIN);
    	
    	detachedCriteria = this.getCriterionsQuery(detachedCriteria, idSegurado, dtVigencia, nrApolice, idReguladora, idSeguradora, idTipoSeguro, idMoeda, vlLimiteApolice);
        return super.findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
    
    public ResultSetPage findPaginatedParcelaByIdApoliceSeguro(Long idApoliceSeguro, FindDefinition findDefinition){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ApoliceSeguroParcela.class)
    			.setFetchMode("apoliceSeguro",FetchMode.JOIN);
    	
    	if(idApoliceSeguro != null){
    		detachedCriteria.add(Restrictions.eq("apoliceSeguro.id",idApoliceSeguro));
    	}
    	
    	return super.findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
    
    
    /**
     * Monta os criterios para a consulta
     * 
     * @param detachedCriteria
     * @param dtVigencia
     * @param nrApolice
     * @param idReguladora
     * @param idSeguradora
     * @param idTipoSeguro
     * @param idMoeda
     * @param vlLimiteApolice
     * @return
     */
    private DetachedCriteria getCriterionsQuery (DetachedCriteria detachedCriteria, Long idSegurado, YearMonthDay dtVigencia, String nrApolice, Long idReguladora, Long idSeguradora, Long idTipoSeguro, Long idMoeda, BigDecimal vlLimiteApolice) {
    	if (dtVigencia!=null) {
	    	detachedCriteria.add(Restrictions.and(
	    		Restrictions.le("dtVigenciaInicial", dtVigencia),
	    		Restrictions.ge("dtVigenciaFinal", dtVigencia)));
    	}
    	
    	detachedCriteria.createAlias("segurado", "pess");
    	
    	if(idSegurado!=null) detachedCriteria.add(Restrictions.eq("segurado.id",idSegurado));
    	if (nrApolice!=null) detachedCriteria.add(Restrictions.like("nrApolice",nrApolice));
    	if (idReguladora!=null) detachedCriteria.add(Restrictions.eq("reguladoraSeguro.id",idReguladora));
    	if (idSeguradora!=null) detachedCriteria.add(Restrictions.eq("seguradora.id",idSeguradora));
    	if (idTipoSeguro!=null) detachedCriteria.add(Restrictions.eq("tipoSeguro.id",idTipoSeguro));
    	if (idMoeda!=null) detachedCriteria.add(Restrictions.eq("moeda.id",idMoeda));
    	if (vlLimiteApolice!=null) detachedCriteria.add(Restrictions.ge("vlLimiteApolice",vlLimiteApolice));
    	
    	detachedCriteria.addOrder(Order.asc("pess.nmPessoa"));
    	detachedCriteria.addOrder(Order.asc("nrApolice"));
    	
    	return detachedCriteria;
    }

    
    /**
     * Verifica se já existe uma apólice vigente p/ o mesmo tipo de seguro com a mesma reguladora.
     * 
     * @param idApoliceSeguro
     * @param idTipoSeguro
     * @param idReguladoraSeguro
     * @param dtAtual
     * @return true se já existe uma apólice vigente, caso contrário retorna false.
     */
    public boolean verificaApoliceVigente(Long idApoliceSeguro, Long idTipoSeguro, Long idReguladoraSeguro, YearMonthDay dtInicial, YearMonthDay dtFinal) {
    	StringBuffer query = new StringBuffer()
   			.append("Select count(apSeg.idApoliceSeguro) ")
    		.append("from ")
    		.append(ApoliceSeguro.class.getName()).append(" apSeg ")
    		.append("where apSeg.tipoSeguro.id = ? ")
    		.append("and apSeg.reguladoraSeguro.id = ? ")
    		.append("and (apSeg.dtVigenciaInicial between ? and ? ")
    		.append("or apSeg.dtVigenciaFinal between ? and ?) ");

    	Object criterios[] = {idTipoSeguro, idReguladoraSeguro, dtInicial, dtFinal, dtInicial, dtFinal};

    	if (idApoliceSeguro != null) {
    		query.append("and apSeg.id <> ?");
			Object[] tmp = new Object[criterios.length + 1];
			System.arraycopy(criterios, 0, tmp, 0, criterios.length);
			criterios = tmp;
			criterios[criterios.length - 1] = idApoliceSeguro;
    	}
    	
    	List resultado = getAdsmHibernateTemplate().find(query.toString(), criterios);
    	if ( ((Long)resultado.get(0)).intValue() > 0 ) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Retorna as ApoliceSeguro cfe parâmetros 
     * @param tpModal (Rodoviario/Aéreo)
     * @param dtVigencia
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<ApoliceSeguro> retornaApolices(String tpModal, Date dtVigencia){
    	
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ApoliceSeguro.class);
    	
    	detachedCriteria.createAlias("tipoSeguro", "ts");
    	detachedCriteria.createAlias("seguradora", "seg");
    	detachedCriteria.createAlias("seguradora.pessoa", "pessoa");
    	
    	detachedCriteria.setFetchMode("ts",FetchMode.JOIN);
    	detachedCriteria.setFetchMode("seg",FetchMode.JOIN);
    	detachedCriteria.setFetchMode("seg.pessoa", FetchMode.JOIN);
    	
    	detachedCriteria.add(Restrictions.le("dtVigenciaInicial", new YearMonthDay(dtVigencia)));
    	detachedCriteria.add(Restrictions.ge("dtVigenciaFinal", new YearMonthDay(dtVigencia)));
    	detachedCriteria.add(Restrictions.eq("ts.tpSituacao", "A"));
    	detachedCriteria.add(Restrictions.eq("ts.tpAbrangencia", "N"));
    	detachedCriteria.add(Restrictions.eq("ts.tpModal", tpModal));
    	
     	List findByCriteria = getAdsmHibernateTemplate().findByCriteria(detachedCriteria);
     	
    	return (List<ApoliceSeguro>) findByCriteria;
    	
    }
    
    /**
	 * Retorna valores para geração obrigatória das informações 
	 * de Seguros no XML do CT-e.
	 * 
	 * Jira LMS-3996
	 *
	 * @param sgTipo
	 * @return List
	 */
    public List findSegValues(String sgTipo) {
    	final StringBuilder sql = new StringBuilder();
    	sql.append("SELECT ps.nm_pessoa, ")
	    	.append("		aps.nr_apolice, ")
	    	.append("		aps.vl_limite_apolice,  ")
	    	.append("		ps.nr_identificacao,  ")
	    	.append("		ps.tp_pessoa  ")
	    	.append("from Apolice_Seguro aps ")
	    	.append("	JOIN tipo_seguro ts ON ts.id_tipo_seguro = aps.id_tipo_seguro ")
	    	.append("	JOIN servico svc ON svc.tp_modal = ts.tp_modal ")
	    	.append("	JOIN pessoa ps ON ps.id_pessoa = aps.id_seguradora ")
	    	.append("where ")
	    	.append("	(aps.dt_vigencia_inicial  <= sysdate AND ")
	    	.append("		aps.dt_vigencia_final >= sysdate) ")
	    	.append("	AND ts.tp_situacao 		= 'A' ")
	    	.append("	AND ts.tp_abrangencia 	= 'N' ")
	    	.append("	AND upper(ts.sg_tipo)	= '"+sgTipo+"' ")
	    	.append(" order by aps.vl_limite_apolice desc");
	
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nm_pessoa",Hibernate.STRING);
				sqlQuery.addScalar("nr_apolice",Hibernate.STRING);
				sqlQuery.addScalar("vl_limite_apolice",Hibernate.STRING);
				sqlQuery.addScalar("nr_identificacao",Hibernate.STRING);
				sqlQuery.addScalar("tp_pessoa",Hibernate.STRING);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}

    
    public List<ApoliceSeguro> findSeguroPorTipo(){
    	StringBuffer hql = new StringBuffer();
    	Long tipoSeguro = Long.valueOf(4);
    	
    	hql.append("FROM "); 
    	hql.append(ApoliceSeguro.class.getName());
    	hql.append(" as apolice "); 
    	hql.append(" WHERE apolice.tipoSeguro.idTipoSeguro = ?");
    	hql.append("   AND apolice.dtVigenciaInicial <= ?");
    	hql.append("   AND apolice.dtVigenciaFinal >= ?");
    	
    	YearMonthDay dtInicial = new YearMonthDay(new Date());
    	YearMonthDay dtFinal = new YearMonthDay(new Date());
    	
    	return super.getAdsmHibernateTemplate().find(hql.toString(), new Object[]{tipoSeguro, dtInicial, dtFinal});
    	
    }
    
    public List<ApoliceSeguro> findApoliceSeguroByNrApolice(String nrApoliceSeguro){
    	StringBuffer hql = new StringBuffer();
    	
    	hql.append(" FROM "); 
    	hql.append(ApoliceSeguro.class.getName());
    	hql.append(" as apolice "); 
    	hql.append(" WHERE apolice.nrApolice = ?");
    
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{nrApoliceSeguro});     	   			    	
    }
    
    //LMS-6178
    // retorna o nome da corretora para popular na tela de detalhamento
    public String findCorretoraByIdTipoSeguro(Long idTipoSeguro, String dhSinistro) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTipoSeguro", idTipoSeguro);
		params.put("dhSinistro", dhSinistro);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NM_PESSOA AS PESSOA ")
		.append("FROM PESSOA P ")
		.append("INNER JOIN APOLICE_SEGURO APOL ")
		.append("ON APOL.ID_REGULADORA = P.ID_PESSOA ")
		.append("WHERE APOL.ID_TIPO_SEGURO = :idTipoSeguro ")
		.append("AND '"+ dhSinistro + "' BETWEEN APOL.DT_VIGENCIA_INICIAL AND APOL.DT_VIGENCIA_FINAL");
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("PESSOA", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		if(!list.isEmpty()){
			return String.valueOf(list.get(0));
		}
		
		return "";
    }
    
    //LMS-6178
 // retorna o nome da seguradora para popular na tela de detalhamento
    public String findSeguradoraByIdTipoSeguro(Long idTipoSeguro, String dhSinistro) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTipoSeguro", idTipoSeguro);
		params.put("dhSinistro", dhSinistro);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NM_PESSOA AS PESSOA ")
		.append("FROM PESSOA P ")
		.append("INNER JOIN APOLICE_SEGURO APOL ")
		.append("ON APOL.ID_SEGURADORA = P.ID_PESSOA ")
		.append("WHERE APOL.ID_TIPO_SEGURO = :idTipoSeguro ")
		.append("AND '"+ dhSinistro + "' BETWEEN APOL.DT_VIGENCIA_INICIAL AND APOL.DT_VIGENCIA_FINAL");
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("PESSOA", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		if(!list.isEmpty()){
			return String.valueOf(list.get(0));
		}
		
		return "";
    }

	/**
	 * LMS-7285 - Atualiza valor limite para controle de carga
	 * (<tt>APOLICE_SEGURO.VL_LIMITE_CONTROLE_CARGA</tt>) de determinada
	 * {@link ApoliceSeguro}.
	 * 
	 * @param idApoliceSeguro
	 *            id da {@link ApoliceSeguro}
	 * @param vlLimiteControleCarga
	 *            valor limite para controle
	 */
	public void storeVlLimiteControleCarga(Long idApoliceSeguro, BigDecimal vlLimiteControleCarga) {
		StringBuilder sql = new StringBuilder()
				.append("UPDATE apolice_seguro ")
				.append("SET vl_limite_controle_carga = " + (vlLimiteControleCarga == null ? "NULL" : ":vl_limite_controle_carga") + " ")
				.append("WHERE id_apolice_seguro = :id_apolice_seguro");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id_apolice_seguro", idApoliceSeguro);
		parametersValues.put("vl_limite_controle_carga", vlLimiteControleCarga);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

}
