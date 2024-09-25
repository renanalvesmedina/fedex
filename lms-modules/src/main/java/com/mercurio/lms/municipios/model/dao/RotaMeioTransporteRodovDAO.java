package com.mercurio.lms.municipios.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RotaMeioTransporteRodov;
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
public class RotaMeioTransporteRodovDAO extends BaseCrudDao<RotaMeioTransporteRodov, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotaMeioTransporteRodov.class;
    }

    public boolean verificaRotaMeioTransporteVigente(RotaMeioTransporteRodov rotaMeioTransporteRodov){
    	DetachedCriteria dc = createDetachedCriteria();

    	if (rotaMeioTransporteRodov.getIdRotaMeioTransporteRodov() != null){
    		dc.add(Restrictions.ne("id",rotaMeioTransporteRodov.getIdRotaMeioTransporteRodov()));
    	}
    	if (rotaMeioTransporteRodov.getMeioTransporteRodoviario() != null) { 
    		dc.add(Restrictions.eq("meioTransporteRodoviario.id",rotaMeioTransporteRodov.getMeioTransporteRodoviario().getIdMeioTransporte()));
    	}
    	if (rotaMeioTransporteRodov.getRotaTipoMeioTransporte() != null){
    		dc.add(Restrictions.eq("rotaTipoMeioTransporte.id",rotaMeioTransporteRodov.getRotaTipoMeioTransporte().getIdRotaTipoMeioTransporte()));
    	}
    	dc= JTVigenciaUtils.getDetachedVigencia(dc,rotaMeioTransporteRodov.getDtVigenciaInicial(), rotaMeioTransporteRodov.getDtVigenciaFinal());  	
    	return findByDetachedCriteria(dc).size()>0;
    }


    public Integer getRowCountPrincipal(TypedFlatMap criteria) {
    	StringBuffer hql = new StringBuffer();
    	Map parameters = new HashMap(); 

    	hql.append("from ").append(RotaMeioTransporteRodov.class.getName()).append(" as rotaMT " )
    	.append("inner join rotaMT.meioTransporteRodoviario as mTR " )
    	.append("inner join rotaMT.rotaTipoMeioTransporte as rTMT " )
    	.append("inner join rTMT.rotaColetaEntrega as RCE " )
    	.append("where 1=1 ");
    	    	
    	if (criteria.getYearMonthDay("dtVigenciaInicial") != null){
            hql.append("and rotaMT.dtVigenciaInicial >= :dtVigenciaInicial ");
            parameters.put("dtVigenciaInicial",criteria.getYearMonthDay("dtVigenciaInicial"));
    	}    
    	if (criteria.getYearMonthDay("dtVigenciaFinal") != null){
    		hql.append("and rotaMT.dtVigenciaFinal <= :dtVigenciaFinal ");
    		parameters.put("dtVigenciaFinal",criteria.getYearMonthDay("dtVigenciaFinal"));
    	}
	    if (criteria.getLong("meioTransporteRodoviario.idMeioTransporte") != null){
	    	hql.append("and mTR.idMeioTransporte = :idMeioTransporte ");
	    	Long idMeioTransporte = criteria.getLong("meioTransporteRodoviario.idMeioTransporte");
	    	parameters.put("idMeioTransporte", idMeioTransporte);
	    }
	    if (criteria.getLong("rotaTipoMeioTransporte.idRotaTipoMeioTransporte") != null){
	    	hql.append("and rTMT.idRotaTipoMeioTransporte = :idRotaTipoMeioTransporte ");
	    	Long idRota =criteria.getLong("rotaTipoMeioTransporte.idRotaTipoMeioTransporte");
	    	parameters.put("idRotaTipoMeioTransporte",idRota);
	    }
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(),parameters);
    }


    public ResultSetPage findPaginatedPrincipal(TypedFlatMap criteria,FindDefinition def) {
    	Object[] obj = createHqlPaginated(criteria,false);
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idRotaMeioTransporte",Hibernate.LONG);
    			sqlQuery.addScalar("sgFilial",Hibernate.STRING);
    			sqlQuery.addScalar("pessoaFilial",Hibernate.STRING);
    			sqlQuery.addScalar("dtVigenciaInicial",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("dtVigenciaFinal",Hibernate.custom(JodaTimeYearMonthDayNotNullUserType.class));
    			sqlQuery.addScalar("nrFrota",Hibernate.STRING);
    			sqlQuery.addScalar("proprietario",Hibernate.STRING);
    			sqlQuery.addScalar("pessoa",Hibernate.STRING);
    		}
    	};
    	return getAdsmHibernateTemplate().findPaginatedBySql((String)obj[0],def.getCurrentPage(),def.getPageSize(),(Map)obj[1],csq);
    }
    
    
    
    private Object[] createHqlPaginated(TypedFlatMap criteria, boolean isRowCount) {
    	StringBuffer sql = new StringBuffer()
			.append("SELECT ")
	
			.append("(SELECT pes.NM_PESSOA FROM MEIO_TRANSP_PROPRIETARIO mtp " )
			.append("inner join MEIO_TRANSPORTE mt ON mt.ID_MEIO_TRANSPORTE=mtp.ID_MEIO_TRANSPORTE ")
			.append("inner join PROPRIETARIO p on p.ID_PROPRIETARIO=mtp.ID_PROPRIETARIO ")
			.append("inner join PESSOA pes on p.ID_PROPRIETARIO=pes.ID_PESSOA ")
			.append("WHERE mt.ID_MEIO_TRANSPORTE=rmtr.ID_MEIO_TRANSPORTE ")
			.append("AND (mtp.DT_VIGENCIA_INICIAL<= :dataAtual or mtp.DT_VIGENCIA_INICIAL is null) ")
			.append("AND mtp.DT_VIGENCIA_FINAL>= :dataAtual) AS pessoa ,")

	 		.append("rmtr.ID_ROTA_MEIO_TRANSPORTE_RODOV as idRotaMeioTransporte, ")
	 		.append("pes.NM_FANTASIA as pessoaFilial, ")
	 		.append("fil.SG_FILIAL as sgFilial, ")
	 		
	 		.append("rmtr.DT_VIGENCIA_INICIAL as dtVigenciaInicial, ")
	 		.append("rmtr.DT_VIGENCIA_FINAL as dtVigenciaFinal, ")
	 		.append("mt.NR_FROTA as nrFrota, ")
	 		.append("mt.NR_IDENTIFICADOR as proprietario ")
 
    	 	.append("from ROTA_MEIO_TRANSPORTE_RODOV rmtr ")
	 		.append("inner join MEIO_TRANSPORTE mt on Rmtr.ID_MEIO_TRANSPORTE = mt.ID_MEIO_TRANSPORTE ")
	 		.append("inner join ROTA_TIPO_MEIO_TRANSPORTE rtmt on rmtr.ID_ROTA_TIPO_MEIO_TRANSPORTE=rtmt.ID_ROTA_TIPO_MEIO_TRANSPORTE ")
	 		.append("inner join ROTA_COLETA_ENTREGA rce on rtmt.ID_ROTA_COLETA_ENTREGA=rce.ID_ROTA_COLETA_ENTREGA ")
	 		.append("inner join FILIAL fil on rce.ID_FILIAL=fil.ID_FILIAL ")
			.append("inner join PESSOA pes on fil.ID_FILIAL=pes.ID_PESSOA ")
			.append("where 1=1 ");

    	Object objValue = new Object();

    	Map parameters = new HashMap();
    	parameters.put("dataAtual",JTDateTimeUtils.getDataAtual());

    	Long idRota = criteria.getLong("rotaTipoMeioTransporte.idRotaTipoMeioTransporte");
    	objValue = idRota;
    	if (objValue != null && !"".equals(objValue)) {
    		sql.append("and rmtr.ID_ROTA_TIPO_MEIO_TRANSPORTE = :idRotaTipoMeioTransporte ");
        	parameters.put("idRotaTipoMeioTransporte",objValue);
    	}
    	
    	objValue = criteria.getYearMonthDay("dtVigenciaInicial");
    	if (objValue != null && !"".equals(objValue)) {
    		sql.append("and rmtr.DT_VIGENCIA_INICIAL >= :dtVigenciaInicial ");
        	parameters.put("dtVigenciaInicial",objValue);
    	}
    	
    	objValue = criteria.getYearMonthDay("dtVigenciaFinal");
    	if (objValue != null && !"".equals(objValue)) {
    		sql.append("and rmtr.DT_VIGENCIA_FINAL <= :dtVigenciaFinal ");
        	parameters.put("dtVigenciaFinal",objValue);
    	}
    	
    	objValue = criteria.getLong("meioTransporteRodoviario.idMeioTransporte");
    	if (objValue != null && !"".equals(objValue)) {
    		sql.append("and rmtr.ID_MEIO_TRANSPORTE = :idMeioTransporte ");
        	parameters.put("idMeioTransporte",objValue);
    	}

    	sql.append("ORDER BY mt.NR_FROTA");

    	return new Object[]{sql.toString(),parameters};
	 }
    

    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("rotaTipoMeioTransporte", FetchMode.JOIN);
    	fetchModes.put("meioTransporteRodoviario", FetchMode.JOIN);
    	fetchModes.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
    }
}