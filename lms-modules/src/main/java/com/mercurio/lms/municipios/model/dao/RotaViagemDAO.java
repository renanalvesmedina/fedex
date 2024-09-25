package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.RotaViagem;
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
public class RotaViagemDAO extends BaseCrudDao<RotaViagem, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotaViagem.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("tipoMeioTransporte",FetchMode.JOIN);
    	lazyFindById.put("cliente",FetchMode.JOIN);
    	lazyFindById.put("cliente.pessoa",FetchMode.JOIN);
    }

    /**
     * Consulta informações da Rota.
     * Método específico da tela 'Manter Rotas de Viagem'
     * 
     * @author Felipe Ferreira
     * @param id id da Rota de Ida e Votla desejado.
     * @param tipoRota String o tipo de Rota desejado
     * @return Map com informações necessárias no detalhamento da RF 'Manter Rotas de Viagem': 29.06.01.01 
     */
    public Map findByIdDetalhamentoRota(Long id, String tpRotaIdaVolta) {   	
    	StringBuffer hql = new StringBuffer()
			.append(" select new Map( ")
			.append("		IV.idRotaIdaVolta as idRotaIdaVolta, ")
			.append("		ROTA.idRota as idRota, ")
			.append("		ROTA.dsRota as dsRota, ")
			.append("		IV.tpRotaIdaVolta as tpRotaIdaVolta, ")
			.append("		IV.nrRota as nrRota, ")
			.append("		IV.nrDistancia as nrDistancia, ")
			.append("		IV.vlFreteKm as vlFreteKm, ")
			.append("		IV.vlFreteCarreteiro as vlFreteCarreteiro, ")
			.append("		IV.vlPremio as vlPremio, ")
			.append("		IV.obItinerario as obItinerario, ")
			.append("		IV.obRotaIdaVolta as obRotaIdaVolta, ")
			.append("		IV.versao as versao, ")
			.append("		MP.idMoedaPais as moedaPais_idMoedaPais, ")
			.append("		MOEDA.dsSimbolo as moedaPais_moeda_dsSimbolo ")
			.append(" )")
			.append(" from " + RotaIdaVolta.class.getName() + " as IV ")
			.append(" inner join IV.rota as ROTA ")
			.append(" inner join IV.moedaPais as MP ")
			.append(" inner join MP.moeda as MOEDA ")
			.append(" inner join IV.rotaViagem as RV ")
			.append(" where RV.idRotaViagem = :idR ")
			.append(" and IV.tpRotaIdaVolta = :tipoRota ");

    	List l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"idR","tipoRota"},
    			new Object[]{id,tpRotaIdaVolta});

    	return (l.size() > 0) ? (Map)l.get(0) : null;
    }

    public ResultSetPage findPaginatedRotaViagem(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate sql = getSqlPaginated(criteria);

    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idRotaViagem",Hibernate.LONG);
    			sqlQuery.addScalar("tpRota",Hibernate.STRING);
    			sqlQuery.addScalar("tpSistemaRota",Hibernate.STRING);
    			sqlQuery.addScalar("dtVigenciaInicial",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("dtVigenciaFinal",Hibernate.custom(JodaTimeYearMonthDayNotNullUserType.class));
    			sqlQuery.addScalar("dsTipoMeioTransporte",Hibernate.STRING);
    			sqlQuery.addScalar("dsRotaIda",Hibernate.STRING);
    			sqlQuery.addScalar("dsRotaVolta",Hibernate.STRING);
    			sqlQuery.addScalar("hrSaidaIda",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    			sqlQuery.addScalar("hrSaidaVolta",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    			sqlQuery.addScalar("nrRotaIda",Hibernate.INTEGER);
    			sqlQuery.addScalar("nrRotaVolta",Hibernate.INTEGER);
    		}
    	};

    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(),findDef.getCurrentPage(),
    			findDef.getPageSize(),sql.getCriteria(),csq);
    }

    public Integer getRowCountRotaViagem(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlPaginated(criteria);
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false),sql.getCriteria());
    }

    private SqlTemplate getSqlPaginated(TypedFlatMap criteria) {
    	SqlTemplate select = new SqlTemplate();
    	SqlTemplate subSelect = new SqlTemplate();

    	StringBuffer sqlProjection = new StringBuffer()
			.append("	RV.ID_ROTA_VIAGEM as idRotaViagem, \n")
			.append("	RV.TP_ROTA as tpRota, \n")
			.append("	RV.TP_SISTEMA_ROTA as tpSistemaRota, \n")
			.append("	RV.DT_VIGENCIA_INICIAL as dtVigenciaInicial, \n")
			.append("	RV.DT_VIGENCIA_FINAL as dtVigenciaFinal, \n")
			.append("	TMT.DS_TIPO_MEIO_TRANSPORTE AS dsTipoMeioTransporte, \n")
			.append(" "+sqlDSRotaIdaVolta("I")+" , ")
    		.append(" "+sqlDSRotaIdaVolta("V")+" , ")
    		.append(" "+sqlNRRotaIdaVolta("I")+" , ")
    		.append(" "+sqlNRRotaIdaVolta("V")+" , ")
    		.append(" "+sqlHrSaidaIdaVolta("I")+" , ")
    		.append(" "+sqlHrSaidaIdaVolta("V")+" ");

		StringBuffer sqlFrom = new StringBuffer()
			.append("	ROTA_VIAGEM RV ")
			.append("	left join TIPO_MEIO_TRANSPORTE TMT on TMT.ID_TIPO_MEIO_TRANSPORTE = RV.ID_TIPO_MEIO_TRANSPORTE ");

		if (criteria.getInteger("rotaIda.nrRota") != null)
			sqlFrom.append("	inner join ROTA_IDA_VOLTA RIDA ON RV.ID_ROTA_VIAGEM = RIDA.ID_ROTA_VIAGEM AND RIDA.TP_ROTA_IDA_VOLTA = 'I' ");
		if (criteria.getInteger("rotaVolta.nrRota") != null)
			sqlFrom.append("	inner join ROTA_IDA_VOLTA RVOL ON RV.ID_ROTA_VIAGEM = RVOL.ID_ROTA_VIAGEM AND RVOL.TP_ROTA_IDA_VOLTA = 'V' ");

    	subSelect.addProjection(sqlProjection.toString());
    	subSelect.addFrom(sqlFrom.toString());

    	String isVigentes = criteria.getString("vigentes");

    	Object objValue = new Object();

    	objValue = criteria.get("tpRota");
    	if (StringUtils.isNotBlank((String)objValue)) {
    		subSelect.addCustomCriteria(" RV.TP_ROTA = ? ");
        	select.addCriteriaValue((String)objValue);
    	}

    	objValue = criteria.get("tpSistemaRota");
    	if (StringUtils.isNotBlank((String)objValue)) {
    		subSelect.addCustomCriteria(" RV.TP_SISTEMA_ROTA = ? ");
    		select.addCriteriaValue((String)objValue);
    	}

    	objValue = criteria.getInteger("cliente.idCliente");
    	if (objValue != null) {
    		subSelect.addCustomCriteria(" RV.ID_CLIENTE = ? ");
    		select.addCriteriaValue((Integer)objValue);
    	}

    	objValue = criteria.get("tipoMeioTransporte.idTipoMeioTransporte");
    	if (objValue != null) {	
	    	if (StringUtils.isNotBlank((String)objValue)) {
	    		subSelect.addCustomCriteria(" TMT.ID_TIPO_MEIO_TRANSPORTE = ? ");
	    		select.addCriteriaValue((String)objValue);
	    	}
    	}

    	objValue = criteria.getYearMonthDay("dtVigenciaInicial");
    	if (objValue != null) {
    		subSelect.addCustomCriteria(" RV.DT_VIGENCIA_INICIAL >= ? ");
    		select.addCriteriaValue((YearMonthDay)objValue);
    	}

    	objValue = criteria.getYearMonthDay("dtVigenciaFinal");
    	if (objValue != null) {
    		subSelect.addCustomCriteria(" RV.DT_VIGENCIA_FINAL <= ? ");
    		select.addCriteriaValue((YearMonthDay)objValue);
    	}

    	objValue = criteria.getInteger("rotaIda.nrRota");
    	if (objValue != null) {
    		subSelect.addCustomCriteria(" RIDA.NR_ROTA = ? ");
    		select.addCriteriaValue((Integer)objValue);
    	}

    	objValue = criteria.getInteger("rotaVolta.nrRota");
    	if (objValue != null) {
    		subSelect.addCustomCriteria(" RVOL.NR_ROTA = ? ");
    		select.addCriteriaValue((Integer)objValue);
    	}

    	sqlProjection = new StringBuffer()
				.append("ROTAV.idRotaViagem as idRotaViagem, ")
				.append("ROTAV.tpRota as tpRota, ")
				.append("ROTAV.tpSistemaRota as tpSistemaRota, ")
				.append("ROTAV.dtVigenciaInicial as dtVigenciaInicial, ")
				.append("ROTAV.dtVigenciaFinal as dtVigenciaFinal, ")
				.append("ROTAV.dsTipoMeioTransporte as dsTipoMeioTransporte, ")
				.append("ROTAV.dsRotaIda as dsRotaIda, ")
				.append("ROTAV.dsRotaVolta as dsRotaVolta, ")
				.append("ROTAV.hrSaidaIda as hrSaidaIda, ")
				.append("ROTAV.hrSaidaVolta as hrSaidaVolta, ")
				.append("ROTAV.nrRotaIda as nrRotaIda, ")
				.append("ROTAV.nrRotaVolta as nrRotaVolta ");
    	sqlFrom = new StringBuffer()
				.append(" ( ")
				.append(subSelect.getSql())
				.append(" ) ROTAV ");

		select.addProjection(sqlProjection.toString());
		select.addFrom(sqlFrom.toString());

		objValue = criteria.get("dsRotaIda");
		if (StringUtils.isNotBlank((String)objValue))
			select.addCriteria("lower(ROTAV.dsRotaIda)","like","%".concat((String)objValue).toLowerCase().concat("%"));

		objValue = criteria.get("dsRotaVolta");
		if (StringUtils.isNotBlank((String)objValue))
			select.addCriteria("lower(dsRotaVolta)","like","%".concat((String)objValue).toLowerCase().concat("%"));

		select.addCriteria("hrSaidaIda","=",criteria.getTimeOfDay("hrSaidaIda"));

		select.addCriteria("hrSaidaVolta","=",criteria.getTimeOfDay("hrSaidaVolta"));

		if (isVigentes.equals("S")){
			select.addCriteria("dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
			select.addCriteria("dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
    	} else if (isVigentes.equals("N")){
			select.addCustomCriteria("(dtVigenciaInicial > ? OR dtVigenciaFinal < ?)",
					new Object[]{JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
    	}		

    	select.addOrderBy("dsRotaIda");
    	select.addOrderBy("tpRota");
    	select.addOrderBy("hrSaidaIda");
    	select.addOrderBy("hrSaidaVolta");

    	return select;
	}

    /**
     * Recupera <b>nrDistância</b> e <b>obItinerario</b> de Rota viagem <b>EXPRESSA</b> a partir dos Parâmetros informados. 
     * @param idRotaViagem parâmetro exclusivo
     * @param idRota
     * @return Map com <b>nrDistância</b> e <b>obItinerario</b>.
     */
    public Map findInfoByRotaViagem(Long idRotaViagem,Long idRota) {
    	StringBuffer hql = new StringBuffer()
    		.append(" select new Map( ")
    		.append("		RV.idRotaViagem as idRotaViagem, ")
    		.append("		RIV.nrDistancia as nrDistancia, ")
    		.append("		RIV.obItinerario as obItinerario ")
    		.append(" )")
    		.append(" from " + RotaViagem.class.getName() + " as RV ")
    		.append(" inner join RV.rotaIdaVoltas RIV ")
    		.append(" inner join RIV.rota R ")
    		.append(" where RV.idRotaViagem != :idRV ")
    		.append(" and R.idRota = :idR ")
    		.append(" and RV.tpRota = :tpRV ")
    		.append(" and RV.dtVigenciaInicial <= :dataAtual ")
    		.append(" and RV.dtVigenciaFinal >= :dataAtual ");

    	List l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
    			new String[]{"idRV",		"idR",	"tpRV",	"dataAtual"},
    			new Object[]{idRotaViagem,	idRota,	"EX",	JTDateTimeUtils.getDataAtual()});

    	return (l.size() > 0) ? (Map)l.get(0) : null;	
    }

    public boolean validateDuplicated(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		List rotaIdasVoltas) {
    	SqlTemplate hql = new SqlTemplate();

    	JTVigenciaUtils.getHqlVigencia(hql,dtVigenciaInicial,dtVigenciaFinal,"RV");
    		
    	StringBuffer hqlFrom = new StringBuffer()
    		.append(getPersistentClass().getName()).append(" RV ");
    	
    	boolean blValidate = false;
    	for (int i = 0 ; i < rotaIdasVoltas.size() ; i++) {
    		RotaIdaVolta riv = (RotaIdaVolta)rotaIdasVoltas.get(i);
    		
    		hqlFrom.append(" inner join RV.rotaIdaVoltas as riv" + i);
    		Long idRotaIdaVolta = riv.getIdRotaIdaVolta();
    		if (idRotaIdaVolta != null)
    			blValidate = true;
			hql.addCriteria("riv" + i + ".id","=",idRotaIdaVolta);
    	}
    	
    	hql.addCriteria("RV.idRotaViagem","!=",id);
    	
    	hql.addFrom(hqlFrom.toString());
    	
    	// Valida somente se há uma rota ida volta com id...
    	if (blValidate)
    		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria()).intValue() > 0;
    	else return false; 
    }

    public List findListNrRotaByFiliais(List l, Long nrRota) {
    	SqlTemplate hql = new SqlTemplate();

    	hql.addProjection("distinct(RIV.nrRota)");
    	hql.addFrom(RotaIdaVolta.class.getName(),"RIV");

    	SqlTemplate subHql = new SqlTemplate();
    	subHql.addProjection("FR.id");
    	subHql.addFrom(FilialRota.class.getName(),"FR");    	
    	subHql.addJoin("RIV.rota.id","FR.rota.id");
    	subHql.addCustomCriteria("FR.blDestinoRota = ?");
    	hql.addCriteriaValue("N");

    	if (!l.isEmpty()) {
    		StringBuffer s = new StringBuffer();

    		Iterator it = l.iterator();
    		while (it.hasNext()) {
    			if (s.length() > 0)
    				s.append(",");
    			s.append("?");
    			hql.addCriteriaValue((Long)it.next());
    		}

    		subHql.addCustomCriteria("FR.filial.id in (" + s.toString() + ")");
    	}

    	hql.addCustomCriteria("exists (" + subHql.getSql() + ")");
    	hql.addCriteria("RIV.nrRota",">=",nrRota);

    	hql.addOrderBy("RIV.nrRota");

    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }

//##############################################################################################################################################
//	Métodos de remoção de RotaViagem na tela 'Manter Rotas de Viagem.    
//##############################################################################################################################################    
    
    public void removeByIdComplete(java.lang.Long id) {
    	RotaViagem rotaViagem = (RotaViagem)findById(id);    	
    	
    	rotaViagem.getRotaIdaVoltas().clear();
    	for (Iterator i = rotaViagem.getRotaIdaVoltas().iterator() ; i.hasNext() ; ) {
    		RotaIdaVolta rotaIdaVolta = (RotaIdaVolta)i.next();
			rotaIdaVolta.getTrechoRotaIdaVoltas().clear();
    		rotaIdaVolta.getTipoMeioTranspRotaEvents().clear();
    	}
		getAdsmHibernateTemplate().delete(rotaViagem);
    }
    
    public void removeByIdsComplete(List ids) {
    	for (Iterator i = ids.iterator() ; i.hasNext() ; )
    		removeByIdComplete((Long)i.next());
    }
    
//##############################################################################################################################################
//	Métodos utilizados nas rotinas específicas do comportamento de DF2 da tela 'Manter rotas de Viagem'.    
//##############################################################################################################################################   
    
    public RotaViagem storeWithItems(RotaViagem rotaViagem, ItemList itemsIda, ItemList itemsEvent, ItemList itemsVolta) {
        super.store(rotaViagem);
        
    	removeTrechoRotaIdaVolta(itemsIda.getRemovedItems());
    	storeTrechoRotaIdaVolta(itemsIda.getNewOrModifiedItems());
    	
    	removeTrechoRotaIdaVolta(itemsVolta.getRemovedItems());
    	storeTrechoRotaIdaVolta(itemsVolta.getNewOrModifiedItems());
    	
    	super.store(rotaViagem);
    	
    	getAdsmHibernateTemplate().flush();
    	
        return rotaViagem;
    }

	private void storeTrechoRotaIdaVolta(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeTrechoRotaIdaVolta(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}
    
	/**
	 * Método específico do processo salvar da tela 'Manter Rotas de Viagem'
	 * 
	 * @author Felipe Ferreira
	 * @param idRota 
	 * @return List com  Object[] contendo id da FilialRota, id da Filial da FilialRota e nrOrdem
	 */
	public List findFiliaisRotaToTrechosOfRotaIdaVolta(Long idRota) {
		if (idRota == null)
			return new ArrayList();
		
		StringBuffer hql = new StringBuffer()
				.append(" select FR.idFilialRota,FR.filial.id,FR.nrOrdem from ")
				.append(FilialRota.class.getName() + " FR ")
				.append(" where FR.rota.id = ? ");
		
		return getAdsmHibernateTemplate().find(hql.toString(),idRota);
	}
		    
//#############################################################################################################################################	
//	Métodos referentes à tela CONSULTAR ROTAS 
//#############################################################################################################################################	  

    public ResultSetPage findPaginatedToConsultarRotas(TypedFlatMap criteria,FindDefinition findDef) {
    	SqlTemplate sql = getSqlPaginatedToConsultarRotas(criteria);
		
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idRotaViagem",Hibernate.LONG);
    			sqlQuery.addScalar("tpRota",Hibernate.STRING);
    			sqlQuery.addScalar("tpSistemaRota",Hibernate.STRING);
    			sqlQuery.addScalar("dtVigenciaInicial",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("dtVigenciaFinal",Hibernate.custom(JodaTimeYearMonthDayNotNullUserType.class));
    			sqlQuery.addScalar("dsTipoMeioTransporte",Hibernate.STRING);
    			sqlQuery.addScalar("versao",Hibernate.INTEGER);
    			sqlQuery.addScalar("nrRotaIda",Hibernate.LONG);
    			sqlQuery.addScalar("dsRotaIda",Hibernate.STRING);
    			sqlQuery.addScalar("hrSaidaIda",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    			sqlQuery.addScalar("nrRotaVolta",Hibernate.LONG);
    			sqlQuery.addScalar("dsRotaVolta",Hibernate.STRING);
    			sqlQuery.addScalar("hrSaidaVolta",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    			sqlQuery.addScalar("sgFilialIda",Hibernate.STRING);
    		}
    	};

    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),
    			sql.getCriteria(),csq);
    }
    
    public Integer getRowCountToConsultarRotas(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlPaginatedToConsultarRotas(criteria);
    	
    	SqlTemplate sqlCount = new SqlTemplate();
    	sqlCount.addFrom("( " + sql.getSql() + " )");
    	
    	return getAdsmHibernateTemplate().getRowCountBySql(sqlCount.getSql(false),sql.getCriteria());
	}
   
    public SqlTemplate getSqlPaginatedToConsultarRotas(TypedFlatMap criteria) {
    	SqlTemplate select = new SqlTemplate();
    	SqlTemplate subSelect = new SqlTemplate();
    	    	
    	StringBuffer sqlProjection = new StringBuffer()
				.append("	RV.ID_ROTA_VIAGEM as idRotaViagem, \n")
				.append("	RV.TP_ROTA as tpRota, \n")
				.append("	RV.TP_SISTEMA_ROTA as tpSistemaRota, \n")
				.append("	RV.DT_VIGENCIA_INICIAL as dtVigenciaInicial, \n")
				.append("	RV.DT_VIGENCIA_FINAL as dtVigenciaFinal, \n")
				.append("   RV.NR_VERSAO as versao, \n")
				.append("	TMT.DS_TIPO_MEIO_TRANSPORTE AS dsTipoMeioTransporte, \n")
		    	.append(" "+sqlDSRotaIdaVolta("I")+" , ")
				.append(" "+sqlDSRotaIdaVolta("V")+" , ")
				.append(" "+sqlNRRotaIdaVolta("I")+" , ")
				.append(" "+sqlNRRotaIdaVolta("V")+" , ")
				.append(" "+sqlHrSaidaIdaVolta("I")+" , ")
				.append(" "+sqlHrSaidaIdaVolta("V")+" , ")
				.append(" "+sqlSGFilialIda()+" ");
		
		subSelect.addProjection(sqlProjection.toString());
		
		subSelect.addFrom("ROTA_VIAGEM", "RV");
		subSelect.addFrom("TIPO_MEIO_TRANSPORTE", "TMT");
		
		subSelect.addJoin("RV.ID_TIPO_MEIO_TRANSPORTE", "TMT.ID_TIPO_MEIO_TRANSPORTE (+)");
		
		
		/*Critérios da consulta*/
	  	Long idFilialOrigem = criteria.getLong("filialOrigem.idFilial");
    	Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
    	Long idFilialIntermediaria = criteria.getLong("filialIntermediaria.idFilial");
    	Long nrRota = criteria.getLong("nrRota");
    	String isVigentes = criteria.getString("vigentes");

    	if (StringUtils.isNotBlank(criteria.getString("tpRota"))) {
    		subSelect.addCustomCriteria(" RV.TP_ROTA = ? ");
    		select.addCriteriaValue(criteria.getString("tpRota"));
    	}	
    	
    	if (StringUtils.isNotBlank(criteria.getString("tpSistemaRota"))) {
    		subSelect.addCustomCriteria("RV.TP_SISTEMA_ROTA = ? ");
    		select.addCriteriaValue(criteria.getString("tpSistemaRota"));
    	}	
    	 
    	if (criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte") != null) {
    		subSelect.addCustomCriteria("TMT.ID_TIPO_MEIO_TRANSPORTE = ? ");
    		select.addCriteriaValue(criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
    	}
    	
  	
    	if (idFilialOrigem != null) {
    		subSelect.addCustomCriteria(
    				"exists(select FRO.id_filial_rota " +
    				"from FILIAL_ROTA FRO, ROTA R, FILIAL F, ROTA_IDA_VOLTA RIV " +
    				"where FRO.ID_ROTA = R.ID_ROTA " +
    				"AND   FRO.bl_Origem_Rota = ? " +
    				"AND   FRO.ID_FILIAL = F.ID_FILIAL " +
    				"AND   RIV.ID_ROTA = R.ID_ROTA "+
    				"AND   RIV.ID_ROTA_VIAGEM = RV.ID_ROTA_VIAGEM "+
    				"AND   F.id_FILIAL = ?) ");   
    		select.addCriteriaValue(Boolean.TRUE);
    		select.addCriteriaValue(idFilialOrigem);
    	}
    	    	
    	if (idFilialDestino != null) {
    		subSelect.addCustomCriteria(
    				"exists(select FRD.id_FILIAL_ROTA " +
    				"from FILIAL_ROTA FRD, ROTA R, FILIAL F, ROTA_IDA_VOLTA RIV " +
    				"where FRD.ID_ROTA = R.ID_ROTA " +
    				"AND   FRD.bl_Destino_Rota = ? " +
    				"AND   FRD.ID_FILIAL = F.ID_FILIAL " +
    				"AND   RIV.ID_ROTA = R.ID_ROTA "+
    				"AND   RIV.ID_ROTA_VIAGEM = RV.ID_ROTA_VIAGEM "+
    				"AND   F.ID_FILIAL = ?) ");
    		select.addCriteriaValue(Boolean.TRUE);
    		select.addCriteriaValue(idFilialDestino);
    	}
    	
    	if (idFilialIntermediaria != null) {
    		subSelect.addCustomCriteria(
    				"exists(select FRI.id_FILIAL_ROTA " +
    				"from FILIAL_ROTA FRI, ROTA R, FILIAL F, ROTA_IDA_VOLTA RIV " +
    				"where FRI.ID_ROTA = R.ID_ROTA " +
    				"AND   RIV.ID_ROTA = R.ID_ROTA "+
    				"AND   RIV.ID_ROTA_VIAGEM = RV.ID_ROTA_VIAGEM "+
    				"AND   FRI.ID_FILIAL = F.ID_FILIAL " +
    				"AND   F.ID_FILIAL  = ?) ");
    		select.addCriteriaValue(idFilialIntermediaria);
    	}
		
    	if ( nrRota != null){
    		subSelect.addCustomCriteria(
    				"EXISTS (SELECT ID_ROTA_IDA_VOLTA " +
    				"FROM ROTA_IDA_VOLTA RIV "+
    				"WHERE RIV.ID_ROTA_VIAGEM = rv.id_rota_viagem " +
    				"and RIV.NR_ROTA = ?) ");
    		select.addCriteriaValue(nrRota);
    	}
    	
    	sqlProjection = new StringBuffer()
			.append("ROTAV.idRotaViagem as idRotaViagem, ")
			.append("ROTAV.tpRota as tpRota, ")
			.append("ROTAV.tpSistemaRota as tpSistemaRota, ")
			.append("ROTAV.dtVigenciaInicial as dtVigenciaInicial, ")
			.append("ROTAV.dtVigenciaFinal as dtVigenciaFinal, ")
			.append("ROTAV.versao as versao, ")
			.append("ROTAV.dsTipoMeioTransporte as dsTipoMeioTransporte, ")
			.append("ROTAV.dsRotaIda as dsRotaIda, ")
			.append("ROTAV.dsRotaVolta as dsRotaVolta, ")
			.append("ROTAV.hrSaidaIda as hrSaidaIda, ")
			.append("ROTAV.hrSaidaVolta as hrSaidaVolta, ")
			.append("ROTAV.nrRotaIda as nrRotaIda, ")
			.append("ROTAV.nrRotaVolta as nrRotaVolta, ")
			.append("ROTAV.sgFilialIda as sgFilialIda ");
    	
    	
    	StringBuffer sqlFrom = new StringBuffer();
    		sqlFrom.append(" ( ");
			sqlFrom.append(subSelect.getSql());
			sqlFrom.append(" ) ROTAV ");
    	
			
		select.addProjection(sqlProjection.toString());
		select.addFrom(sqlFrom.toString());	
		
		TimeOfDay hrSaida = criteria.getTimeOfDay("hrSaida");
		if (hrSaida != null) {
			select.addCustomCriteria("(hrSaidaIda = ? or hrSaidaVolta = ?)",new Object[]{hrSaida,hrSaida});
		}
		
		if (isVigentes.equals("S")){
			select.addCriteria("dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
			select.addCriteria("dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
    	} else if (isVigentes.equals("N")){
    		select.addCustomCriteria("(dtVigenciaInicial = ? or dtVigenciaFinal = ?)",new Object[]{JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
    	}		
		
    	/*ordenacao*/
    	select.addOrderBy("sgFilialIda");
    	select.addOrderBy("hrSaidaIda");
    	select.addOrderBy("nrRotaIda");
    	
		return select;
    }
    
    /** 
     * Consulta último trecho da Rota inserido onde:
     * 		filial origem é igual à primeira rota
     * @param idRotaIdaVolta
     * @return TrechoRotaIdaVolta
     */
    public Integer findMaiorTempoViagemOfRota(Long idRotaIdaVolta) {
    	SqlTemplate hql = new SqlTemplate();

    	hql.addProjection("max(TRIV.nrTempoViagem)");

    	hql.addFrom(new StringBuilder()
				.append(RotaIdaVolta.class.getName()).append(" RIV	")
				.append(" inner join RIV.trechoRotaIdaVoltas as TRIV ")
				.toString());

		hql.addCriteria("RIV.idRotaIdaVolta","=",idRotaIdaVolta);

		return (Integer)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    }

    /**
     * SQL da descrição da rota de ida
     * @param tipo
     * @return
     */
    private static String sqlDSRotaIdaVolta(String tipo){
    	StringBuffer sqlProjection = new StringBuffer()
    	.append("	(SELECT R.DS_ROTA ")
    	.append("	FROM   ROTA_IDA_VOLTA RIV, ")
    	.append("	ROTA R ")
    	.append("	WHERE  RV.ID_ROTA_VIAGEM = RIV.ID_ROTA_VIAGEM ")
    	.append("	AND    RIV.ID_ROTA = R.ID_ROTA ");
    	if (tipo.equals("I")){
    		sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'I') AS dsRotaIda ");
    	} else	sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'V') AS dsRotaVolta ");
    	
    	return sqlProjection.toString();

    }
    
    /**
     * SQL do núumero da rota de volta
     * @param tipo
     * @return
     */
    private static  String sqlNRRotaIdaVolta(String tipo){
    	StringBuffer sqlProjection = new StringBuffer()
    	.append("	(SELECT RIV.NR_ROTA ")
    	.append("	FROM   ROTA_IDA_VOLTA RIV ")
    	.append("	WHERE  RV.ID_ROTA_VIAGEM = RIV.ID_ROTA_VIAGEM ");
    	if (tipo.equals("I")){
    		sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'I') AS nrRotaIda ");
    	}else sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'V') AS nrRotaVolta ");
    	
    	return sqlProjection.toString();
    }
    
    /**
     * SQL de horários dos trechos da rota
     * @param tipo
     * @return
     */
    private static  String sqlHrSaidaIdaVolta(String tipo){
    	StringBuffer sqlProjection = new StringBuffer();
    	
    	if (tipo.equals("I")){
    		sqlProjection.append("	(SELECT MIN(TRIV.HR_SAIDA) ");
    	} else 	sqlProjection.append("	(SELECT MAX(TRIV.HR_SAIDA) ");
		sqlProjection.append("	FROM   ROTA_IDA_VOLTA RIV, ");
		sqlProjection.append("	ROTA R, ");
		sqlProjection.append("	FILIAL_ROTA FRO, ");
		sqlProjection.append("	FILIAL_ROTA FRD, ");
		sqlProjection.append("	TRECHO_ROTA_IDA_VOLTA TRIV ");
		sqlProjection.append("	WHERE  RV.ID_ROTA_VIAGEM = RIV.ID_ROTA_VIAGEM ");
		sqlProjection.append("	AND    FRO.ID_ROTA = R.ID_ROTA ");
		sqlProjection.append("	AND    FRD.ID_ROTA = R.ID_ROTA ");
		sqlProjection.append("	AND    FRO.BL_ORIGEM_ROTA = 'S' ");
		sqlProjection.append("	AND    FRD.BL_DESTINO_ROTA = 'S' ");
		sqlProjection.append("	AND    RIV.ID_ROTA = R.ID_ROTA ");
		sqlProjection.append("	AND    TRIV.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA ");
		sqlProjection.append("	AND    TRIV.ID_FILIAL_ROTA_ORIGEM = FRO.ID_FILIAL_ROTA ");
		sqlProjection.append("	AND    TRIV.ID_FILIAL_ROTA_DESTINO = FRD.ID_FILIAL_ROTA ");
		if (tipo.equals("I")){
			sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'I') AS hrSaidaIda ");
		} else sqlProjection.append("	AND    RIV.TP_ROTA_IDA_VOLTA = 'V') AS hrSaidaVolta ");	
    	
    	return sqlProjection.toString();
    }

    /**
     * SQL da sigla da primeira filial da rota de ida 
     * @return
     */
    private static String sqlSGFilialIda(){
    	StringBuffer sqlProjection = new StringBuffer()
    	.append("	(SELECT f.sg_filial ")
    	.append(" 	FROM rota_ida_volta riv, ")
    	.append(" 	rota r, ") 
    	.append(" 	filial_rota fr, ") 
    	.append(" 	filial f ")
    	.append(" 	WHERE  RV.ID_ROTA_VIAGEM = riv.id_rota_viagem ")
    	.append(" 	AND riv.id_rota = r.id_rota ")
    	.append(" 	and fr.id_rota = r.id_rota ")
    	.append(" 	and fr.id_filial = f.id_filial ")
    	.append(" 	AND riv.tp_rota_ida_volta = 'I' ")
    	.append(" 	and fr.nr_ordem = 1) as sgFilialIda ");

    	return sqlProjection.toString();
    }

    /**
     * Verifica se existe alguma rota vigente além da rota informada
     * 
     * @param idRotaViagem
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * @return
     */
	public Integer getRowCountRotasViagemVigentes(Long idRotaViagem, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		if(dtVigenciaFinal == null) {
			dtVigenciaFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;
		}

		StringBuilder hql = new StringBuilder();

		hql.append("  from RotaViagem rv");
		hql.append(" where rv.id <> ?");
		hql.append("   and rv.id in (");
		hql.append("     select distinct rv1.id");
		hql.append("       from RotaIdaVolta riv1");
		hql.append("       inner join riv1.rotaViagem rv1");
		hql.append("      where not exists (");
		hql.append("        select distinct riv2.nrRota");
		hql.append("          from RotaIdaVolta riv2");
		hql.append("         inner join riv2.rotaViagem rv2");
		hql.append("         where rv2.id = ?");
		hql.append("           and not exists (");
		hql.append("              select distinct riv3.nrRota");
		hql.append("                from RotaIdaVolta riv3");
		hql.append("               inner join riv3.rotaViagem rv3");
		hql.append("               where riv3.nrRota = riv2.nrRota");
		hql.append("                 and rv3.id = rv1.id");
		hql.append("           )");
		hql.append("       )");
		hql.append("   )");
		hql.append("   and rv.dtVigenciaInicial <= ?");
		hql.append("   and rv.dtVigenciaFinal >= ?");

		Object[] parametersValues = new Object[]{idRotaViagem, idRotaViagem, dtVigenciaInicial, dtVigenciaFinal};
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), parametersValues);
    }
	
	/**
     * Verifica se existe alguma rota vigente após a data de vigencia informada.
     * 
     * @param idRotaViagem
     * @param dtVigencia
     * @return
     */
	public Boolean findRotaViagemFutura(Long idRotaViagem, YearMonthDay dtVigencia) {
		StringBuilder hql = new StringBuilder()
		.append("  from RotaViagem rv")
		.append(" where rv.id <> ?")
		.append("   and rv.id in (")
		.append("     select distinct rv1.id")
		.append("       from RotaIdaVolta riv1")
		.append("       inner join riv1.rotaViagem rv1")
		.append("      where not exists (")
		.append("        select distinct riv2.nrRota")
		.append("          from RotaIdaVolta riv2")
		.append("         inner join riv2.rotaViagem rv2")
		.append("         where rv2.id = ?")
		.append("           and not exists (")
		.append("              select distinct riv3.nrRota")
		.append("                from RotaIdaVolta riv3")
		.append("               inner join riv3.rotaViagem rv3")
		.append("               where riv3.nrRota = riv2.nrRota")
		.append("                 and rv3.id = rv1.id")
		.append("           )")
		.append("       )")
		.append("   )")
		.append("   and rv.dtVigenciaInicial > ?");

		Object[] parametersValues = new Object[] {idRotaViagem, idRotaViagem, dtVigencia};
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), parametersValues) > 0;
    }
}  
