package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.MeioTransporteRotaViagem;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaIdaVoltaDAO extends BaseCrudDao<RotaIdaVolta, Long>
{
	
	protected void initFindLookupLazyProperties(Map map) {
		map.put("rota", FetchMode.JOIN);
		map.put("moedaPais", FetchMode.JOIN);
		map.put("moedaPais.moeda", FetchMode.JOIN);
	}
	protected void initFindByIdLazyProperties(Map map) {
		map.put("rota", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("rota",FetchMode.JOIN);
	}
	

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotaIdaVolta.class;
    } 

    public Map findByIdToConsultarRotas(Long idRotaViagem, boolean isTipoIda) {
    	StringBuffer hql = new StringBuffer()
    		.append(" select new Map( ")
    		.append("	RIV.idRotaIdaVolta as idRotaIdaVolta, ")
    		.append("	RIV.vlFreteKm as vlFreteKm, ")
    		.append("	RIV.tpRotaIdaVolta as tpRotaIdaVoltaDominio, ")
    		.append("	RIV.vlPremio as vlPremio, ")
    		.append("	RIV.nrDistancia as nrDistancia, ")
    		.append("	RIV.obRotaIdaVolta as obRotaIdaVolta, ")
    		.append("	RIV.obItinerario as obItinerario, ")
    		.append("	(RIV.vlFreteKm*RIV.nrDistancia) as vlFreteCarreteiro, ")
    		.append("	M.dsMoeda as dsMoeda, ")
    		.append("	M.sgMoeda as sgMoeda, ")
    		.append("   M.dsSimbolo as dsSimbolo, ")
    		.append("	R.dsRota as dsRota, ")
    		.append("	RV.idRotaViagem as idRotaViagem, ")
    		.append("	RV.tpRota as tpRotaDominio, ")
    		
    		.append("	RV.tpSistemaRota as tpSistemaRotaDominio, ")
    		.append("	RIV.nrRota as nrRota, ")
    		.append("	TMP.dsTipoMeioTransporte as dsTipoMeioTransporte, ")
    		.append("	TMP.idTipoMeioTransporte as idTipoMeioTransporte, ")
    		.append("	MP.idMoedaPais as idMoedaPais ")
    		
    		.append(") from " + RotaIdaVolta.class.getName() + " RIV ")
    		.append(" inner join RIV.moedaPais as MP ")
    		.append(" inner join MP.moeda as M ")
    		.append(" inner join RIV.rota as R ")
    		.append(" inner join RIV.rotaViagem as RV ")
    		.append(" left  join RV.tipoMeioTransporte as TMP ")
    		.append(" where RV.idRotaViagem = ? ");
    	if (isTipoIda)
    		hql.append(" and (RIV.tpRotaIdaVolta = 'I' or RIV.tpRotaIdaVolta = 'E') ");
    	else
    		hql.append(" and RIV.tpRotaIdaVolta = 'V' ");
    	List l = getAdsmHibernateTemplate().find(hql.toString(),idRotaViagem);
    	return (l.size() > 0) ? (Map)l.get(0) : null;
    }
    
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria,FindDefinition fDef) {
    	SqlTemplate sql = getSqlTemplateCustom(criteria);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),sql.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlTemplateCustom(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }
    
    public List findLookupRotaIdaVolta(TypedFlatMap criteria){
    	SqlTemplate sql = getSqlTemplateCustom(criteria);
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    private SqlTemplate getSqlTemplateCustom(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("RIV");
    	
    	Long idFilialOrigem = criteria.getLong("filialOrigem.idFilial");
    	Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
    	Long idFilialIntermediaria = criteria.getLong("filialIntermediaria.idFilial");
    	String isVigentes = StringUtils.isNotBlank(criteria.getString("vigentes")) ? criteria.getString("vigentes") : "";
    	
    	Long idRegionalOrigem = criteria.getLong("regionalOrigem.idRegional");
    	Long idRegionalDestino = criteria.getLong("regionalDestino.idRegional");
    	String tipoRota = criteria.getString("tpRota");
    	Long idProprietario = criteria.getLong("proprietario.idProprietario");
    	Long idTipoMeioTransporte = criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte");
    	Long idMeioTransporte = criteria.getLong("meioTransporte.idMeioTransporte");
    	
    	    	
    	StringBuffer sqlFrom = new StringBuffer()
    			.append(RotaIdaVolta.class.getName() + " as RIV ")
    			.append(" inner join fetch RIV.rota as R ")
    			.append(" inner join fetch RIV.rotaViagem as RV ")
    			.append(" left join fetch RV.tipoMeioTransporte as TMT ")
    			.append(" inner join fetch RIV.moedaPais as MP ")
    			.append(" inner join fetch MP.moeda as M ");
    			
    	sql.addFrom(sqlFrom.toString());
    	
    	if(criteria.getInteger("rota.idRota") != null)
    	sql.addCriteria("R.id","=",criteria.getLong("rota.idRota"));
    	
    	if(criteria.getInteger("nrRota") != null)
    	sql.addCriteria("RIV.nrRota","=",criteria.getInteger("nrRota"));

    	if(criteria.getLong("idRotaIdaVolta") != null)
    		sql.addCriteria("RIV.idRotaIdaVolta","=",criteria.getLong("idRotaIdaVolta"));

    	if (tipoRota != null) {
        	sql.addCriteria("RV.tpRota","=",tipoRota);
    	}

    	if (idProprietario != null) {
    		sql.addCustomCriteria(
    				"exists(select MTRV.id from " + MeioTransporteRotaViagem.class.getName() + " as MTRV " +
    				"		where RV.idRotaViagem = MTRV.rotaViagem.idRotaViagem " +
    				"		  and exists ( select 1 from " + MeioTranspProprietario.class.getName() + " as MTP" +
    				" 					   where MTP.meioTransporte.idMeioTransporte = MTRV.meioTransporteRodoviario.idMeioTransporte " +
    				"					   and MTP.proprietario.idProprietario = ? ) )", new Object[]{idProprietario});    		
    	}
    	
    	if (idMeioTransporte != null) {
    		sql.addCustomCriteria(
    				"exists(select MTRV.id from " + MeioTransporteRotaViagem.class.getName() + " as MTRV " +
    				"		where RV.idRotaViagem = MTRV.rotaViagem.idRotaViagem " +
    				"		  and MTRV.meioTransporteRodoviario.idMeioTransporte = ? )", new Object[]{idMeioTransporte});    		
    	}

    	if (idTipoMeioTransporte != null) {
        	sql.addCriteria("RV.tipoMeioTransporte.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	}
    	
    	if (idRegionalOrigem != null) {
    		StringBuffer subSelect = new StringBuffer("exists(select FRD.id from ").append(FilialRota.class.getName()).append(" as FRD, RegionalFilial RF ")
    										.append("		where FRD.rota.id = R.id and FRD.blOrigemRota = ? ")
    										.append("		  and RF.filial.idFilial = FRD.filial.id          ")
    										.append("		  and RF.regional.idRegional = ?                  ");
    		
    		List filters = new ArrayList();
    		filters.add(Boolean.TRUE);
    		filters.add(idRegionalOrigem);
    		
    		if (idFilialOrigem != null) {
    			subSelect.append("		  and FRD.filial.id = ?                           ");
    			filters.add(idFilialOrigem);
    		}
    		
    		subSelect.append(" ) ");
    		
    		sql.addCustomCriteria(subSelect.toString(), filters.toArray());    		
    	}

    	if (idRegionalDestino != null) {
    		StringBuffer subSelect = new StringBuffer("exists(select FRD.id from ").append(FilialRota.class.getName()).append(" as FRD, RegionalFilial RF ")
    										.append("		where FRD.rota.id = R.id and FRD.blDestinoRota = ? ")
    										.append("		  and RF.filial.idFilial = FRD.filial.id          ")
    										.append("		  and RF.regional.idRegional = ?                  ");
    		
    		List filters = new ArrayList();
    		filters.add(Boolean.TRUE);
    		filters.add(idRegionalDestino);
    		
    		if (idFilialDestino != null) {
    			subSelect.append("		  and FRD.filial.id = ?                           ");
    			filters.add(idFilialDestino);
    		}
    		
    		subSelect.append(" ) ");
    		
    		sql.addCustomCriteria(subSelect.toString(), filters.toArray());    		
    	}
    	
    	if (idRegionalOrigem == null && idFilialOrigem != null) {
    		sql.addCustomCriteria(
    				"exists(select FRO.id from " + FilialRota.class.getName() + " as FRO " +
    				"		where FRO.rota.id = R.id and FRO.blOrigemRota = ? " +
    				"		  and FRO.filial.id = ?)", new Object[]{Boolean.TRUE,idFilialOrigem});    		
    	}
    	    	
    	if (idRegionalDestino == null && idFilialDestino != null) {
    		sql.addCustomCriteria(
    				"exists(select FRD.id from " + FilialRota.class.getName() + " as FRD " +
    				"		where FRD.rota.id = R.id and FRD.blDestinoRota = ? " +
    				"		  and FRD.filial.id = ?) ", new Object[]{Boolean.TRUE,idFilialDestino});
    	}
    	
    	if (idFilialIntermediaria != null) {
    		sql.addCustomCriteria(
    				"exists(select FRI.id from " + FilialRota.class.getName() + " as FRI " +
    				"		where FRI.rota.id = R.id " +
    				"		  and FRI.filial.id = ?) ", idFilialIntermediaria);
    	}
    	
    	if (isVigentes.equals("S")){
			sql.addCriteria("RV.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
			sql.addCriteria("RV.dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
    	} else if (isVigentes.equals("N")){
			sql.addCustomCriteria("(RV.dtVigenciaInicial > ? OR RV.dtVigenciaFinal < ?)",
					new Object[]{JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
    	}		
    	
    	sql.addOrderBy("RIV.nrRota");
    	return sql;
    }
     

    /**
     * Busca as rotas ida e volta que estao dentro do periodo de vigencia. 
     * 
     * @param nrRota
     * @param vigencia
     * @return
     */
    public List findRotaIdaVoltaInVigencia(Integer nrRota, YearMonthDay vigencia) {
    	
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RotaIdaVolta.class) 
    		.createAlias("rotaViagem", "rotaViagem")
    		.createAlias("rota", "rota")
    		.add(Restrictions.and(
    				Restrictions.le("rotaViagem.dtVigenciaInicial", vigencia),
    				Restrictions.ge("rotaViagem.dtVigenciaFinal", vigencia)))
    		.add(Restrictions.eq("nrRota", nrRota));
    		
    	return this.findByDetachedCriteria(detachedCriteria);
    }
    
    /**
     * Busca as filiais da rota
     * @param idRotaIdaVolta
     * @return List<FilialRota>
     */
    public List findFiliaisRotaByIdRotaIdaVolta(Long idRotaIdaVolta) {
    	StringBuffer hql = new StringBuffer();
    	hql.append("select filialRota from FilialRota as filialRota ")
    		.append("inner join fetch filialRota.filial as filial ")
    		.append("inner join filialRota.rota as rota ")
    		.append("inner join rota.rotaIdaVoltas as rotaIdaVolta ")
    		.append("where rotaIdaVolta.id = ? ")
    		.append("order by filialRota.nrOrdem ");
    	 	
    	return getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idRotaIdaVolta});
    }
    
    /**
     * Retorna uma lista com as rotas vigentes para a filial de origem informada 
     * @param idFilialOrigem
     * @return
     */
    public List<RotaIdaVolta> findRotasVigentesByIdFilialOrigem(Long idFilialOrigem) {
    	DetachedCriteria dc = DetachedCriteria.forClass(RotaIdaVolta.class);		
    	
    	dc.createAlias("rota", "rota");
    	dc.createAlias("rota.filialRotas", "filialRotas");
    	dc.createAlias("rota.filialRotas.filial", "filial");
    	dc.createAlias("rotaViagem", "rotaViagem");
    	
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaViagem", FetchMode.JOIN);		
		
		dc.add(Restrictions.and(
				Restrictions.le("rotaViagem.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()),
				Restrictions.ge("rotaViagem.dtVigenciaFinal", JTDateTimeUtils.getDataAtual())));		
		dc.add(Restrictions.eq("filialRotas.blOrigemRota", Boolean.TRUE)); 
		dc.add(Restrictions.eq("filial.id", idFilialOrigem));		
				
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    /**
     * Retorna uma lista com as rotas vigentes para a filial de origem informada 
     * @param idFilialOrigem
     * @return
     */
    public List<TrechoRotaIdaVolta> findRotasViagemByIdFilialOrigem(Long idFilialOrigem) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TrechoRotaIdaVolta.class);		
    	
    	dc.createAlias("rotaIdaVolta.rota", "rota");
    	dc.createAlias("filialRotaOrigem.filial", "filial");
    	dc.createAlias("rotaIdaVolta", "rotaIdaVolta");
    	dc.createAlias("rotaIdaVolta.rotaViagem", "rotaViagem");
    	dc.createAlias("filialRotaByIdFilialRotaOrigem", "filialRotaOrigem");
    	    	
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.setFetchMode("rotaViagem", FetchMode.JOIN);
		dc.setFetchMode("rotaIdaVolta", FetchMode.JOIN);
		dc.setFetchMode("filialRotaOrigem", FetchMode.JOIN);
		
		dc.add(Restrictions.and(
				Restrictions.le("rotaViagem.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()),
				Restrictions.ge("rotaViagem.dtVigenciaFinal", JTDateTimeUtils.getDataAtual())));		
		dc.add(Restrictions.eq("filialRotaOrigem.blOrigemRota", Boolean.TRUE)); 
		dc.add(Restrictions.eq("filial.id", idFilialOrigem));
		dc.addOrder(Order.asc("hrSaida"));
				
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
}