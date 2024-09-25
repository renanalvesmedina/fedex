package com.mercurio.lms.portaria.model.dao;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil;
import com.mercurio.lms.util.JTVigenciaUtils;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConfiguracaoAuditoriaFilDAO extends BaseCrudDao<ConfiguracaoAuditoriaFil, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ConfiguracaoAuditoriaFil.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("rotaIdaVolta", FetchMode.JOIN);
    	lazyFindById.put("rotaColetaEntrega", FetchMode.JOIN);
    	lazyFindById.put("meioTransporteRodoviario", FetchMode.JOIN);
    	lazyFindById.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);    	
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("filial", FetchMode.JOIN);
    	lazyFindPaginated.put("rotaIdaVolta", FetchMode.JOIN);
    	lazyFindPaginated.put("rotaColetaEntrega", FetchMode.JOIN);
    	lazyFindPaginated.put("meioTransporteRodoviario", FetchMode.JOIN);
    	lazyFindPaginated.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
    }
    
    /**
     * Não pode haver mais de uma configurações para a mesma filial, para o mesmo tipo de operacao,
     * e no mesmo intervalo de vigencia e horas específico
     * @param caf ConfiguracaoAuditoriaFil 
     * @return false, se não for possível cadastrar um novo registro
     * @author luisfco
     */
    public boolean validaVigenciaCadastramento(ConfiguracaoAuditoriaFil caf) {
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.setProjection(Projections.rowCount());
    	
    	if (caf.getIdConfiguracaoAuditoriaFil() != null)
    		dc.add(Restrictions.ne("id", caf.getIdConfiguracaoAuditoriaFil()));
    	
    	dc = JTVigenciaUtils.getDetachedVigencia(dc, caf.getDtVigenciaInicial(), caf.getDtVigenciaFinal());
    	
    	if (caf.getMeioTransporteRodoviario() != null)
    		dc.add(Restrictions.eq("meioTransporteRodoviario.idMeioTransporte", caf.getMeioTransporteRodoviario().getIdMeioTransporte()));
    	
    	dc.add(Restrictions.eq("tpOperacao", caf.getTpOperacao().getValue()));
    	
    	if (caf.getTpOperacao().getValue().equals("V") && caf.getRotaIdaVolta() != null){    		
    		dc.add(Restrictions.eq("rotaIdaVolta.id", caf.getRotaIdaVolta().getIdRotaIdaVolta()));
    		
    	} else if(caf.getTpOperacao().getValue().equals("E") && caf.getRotaColetaEntrega() != null){
    		dc.add(Restrictions.eq("rotaColetaEntrega.id", caf.getRotaColetaEntrega().getIdRotaColetaEntrega()));
    	}
    	
    	dc.add(Restrictions.eq("filial.idFilial", caf.getFilial().getIdFilial()));
    	dc.add(Restrictions.ge("hrAuditoriaFinal", caf.getHrAuditoriaInicial()));
    	dc.add(Restrictions.le("hrAuditoriaInicial", caf.getHrAuditoriaFinal()));
    			
    	return ! (((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0);
    }
    
    private Object[] finderQuery(TypedFlatMap tfm, boolean allowProjection ) {
    	
    	SqlTemplate s = new SqlTemplate();
    	
  		s.addProjection("CAF.ID_CONFIGURACAO_AUDITORIA_FIL", "idConfiguracaoAuditoriaFil");
		s.addProjection("F.ID_FILIAL", "idFilial");
  		s.addProjection("F.SG_FILIAL", "sgFilial");
  		s.addProjection("FILIAL_PESSOA.NM_FANTASIA", "nmFilial");
  		s.addProjection("SG_FILIAL || ' - ' || FILIAL_PESSOA.NM_FANTASIA", "siglaNomeFilial");
  		s.addProjection("MT.NR_FROTA", "nrFrota");
  		s.addProjection("MT.NR_IDENTIFICADOR", "nrIdentificador");  		
  		s.addProjection("CAF.HR_AUDITORIA_INICIAL", "hrAuditoriaInicial");
  		s.addProjection("CAF.HR_AUDITORIA_FINAL", "hrAuditoriaFinal");
  		s.addProjection("CAF.DT_VIGENCIA_INICIAL", "dtVigenciaInicial");
  		s.addProjection("CAF.DT_VIGENCIA_FINAL", "dtVigenciaFinal");
  		s.addProjection("CASE WHEN (RCE.NR_ROTA IS NOT NULL AND RCE.DS_ROTA IS NOT NULL) THEN RCE.NR_ROTA || ' - ' || RCE.DS_ROTA ELSE NULL END", "dsRotaColetaEntrega");
  		s.addProjection("RCE.NR_ROTA", "nrRotaColetaEntrega");
  		s.addProjection("RCE.DS_ROTA", "dsSRotaColetaEntrega");
  		s.addProjection("CAF.TP_OPERACAO", "tpOperacao");
  	 	
        s.addProjection("RIV.NR_ROTA", "nrRota");
  
        s.addProjection("R.DS_ROTA", "dsRotaIdaVolta");
  		
  		s.addProjection("MT.ID_MEIO_TRANSPORTE", "idMeioTransporte");
  		s.addProjection("RCE.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
  		
  		s.addProjection("RIV.ID_ROTA_IDA_VOLTA", "idRotaIdaVolta");
        s.addProjection("RIV.TP_ROTA_IDA_VOLTA", "tpRotaIdaVolta");
        
        s.addProjection("RIV.NR_VERSAO", "nrVersao");
  		
  		s.addFrom("CONFIGURACAO_AUDITORIA_FIL", "CAF");
  		s.addFrom("ROTA_COLETA_ENTREGA", "RCE");
  		s.addFrom("MEIO_TRANSPORTE", "MT");
  		s.addFrom("ROTA_IDA_VOLTA", "RIV");
  		s.addFrom("ROTA", "R");
  		s.addFrom("FILIAL", "F");
  		s.addFrom("PESSOA", "FILIAL_PESSOA");
  		
  		s.addJoin("CAF.ID_MEIO_TRANSPORTE", "MT.ID_MEIO_TRANSPORTE (+)");
  		s.addJoin("CAF.ID_ROTA_COLETA_ENTREGA", "RCE.ID_ROTA_COLETA_ENTREGA (+)");
  		s.addJoin("CAF.ID_ROTA_IDA_VOLTA", "RIV.ID_ROTA_IDA_VOLTA (+)");
  		s.addJoin("RIV.ID_ROTA", "R.ID_ROTA (+)");
  		
  		s.addJoin("CAF.ID_FILIAL", "F.ID_FILIAL");
  		s.addJoin("FILIAL_PESSOA.ID_PESSOA", "F.ID_FILIAL");
  		
  		s.addOrderBy("F.SG_FILIAL");
  		s.addOrderBy("CAF.TP_OPERACAO");
  		s.addOrderBy("CAF.HR_AUDITORIA_INICIAL");
  		s.addOrderBy("CAF.HR_AUDITORIA_FINAL");
  		
  		Long idConfiguracaoAuditoriaFil = tfm.getLong("idConfiguracaoAuditoriaFil"); 
    	Long idFilial = tfm.getLong("filial.idFilial");
    	
    	String tpOperacao = (tfm.getDomainValue("tpOperacao") != null) ? tfm.getDomainValue("tpOperacao").getValue() : "";
    	Long idMeioTransporte = tfm.getLong("meioTransporteRodoviario.idMeioTransporte");
    	Long idRotaIdaVolta = tfm.getLong("rotaIdaVolta.idRotaIdaVolta");
    	Long idRotaColetaEntrega = tfm.getLong("rotaColetaEntrega.idRotaColetaEntrega");
    	TimeOfDay hrAuditoriaInicial = tfm.getTimeOfDay("hrAuditoriaInicial");
    	TimeOfDay hrAuditoriaFinal = tfm.getTimeOfDay("hrAuditoriaFinal");
    	YearMonthDay dtVigenciaInicial = tfm.getYearMonthDay("dtVigenciaInicial");
    	YearMonthDay dtVigenciaFinal = tfm.getYearMonthDay("dtVigenciaFinal");
    	
    	Map parametros = new HashMap();
    	
    	if (idFilial != null) {
    		s.addCustomCriteria("F.ID_FILIAL = :idFilial");
    		parametros.put("idFilial", idFilial);
    	}
    	if (StringUtils.isNotBlank(tpOperacao)) {
    		s.addCustomCriteria("CAF.TP_OPERACAO = :tpOperacao");
    		parametros.put("tpOperacao", tpOperacao);
    	}
    	if (idMeioTransporte != null) {
    		s.addCustomCriteria("MT.ID_MEIO_TRANSPORTE = :idMeioTransporte");
    		parametros.put("idMeioTransporte", idMeioTransporte);
    	}
    	if (idRotaIdaVolta != null) {
    		s.addCustomCriteria("RIV.ID_ROTA_IDA_VOLTA = :idRotaIdaVolta");
    		parametros.put("idRotaIdaVolta", idRotaIdaVolta);
    	}
    	if (idRotaColetaEntrega != null) {
    		s.addCustomCriteria("RCE.ID_ROTA_COLETA_ENTREGA = :idRotaColetaEntrega");
    		parametros.put("idRotaColetaEntrega", idRotaColetaEntrega);
    	}
    	if (hrAuditoriaInicial != null) {
    		s.addCustomCriteria("CAF.HR_AUDITORIA_INICIAL >= :hrAuditoriaInicial");
    		parametros.put("hrAuditoriaInicial", hrAuditoriaInicial);
    	}
    	if (hrAuditoriaFinal != null) {
    		s.addCustomCriteria("(CAF.HR_AUDITORIA_FINAL <= :hrAuditoriaFinal OR CAF.HR_AUDITORIA_FINAL IS NULL)");
    		parametros.put("hrAuditoriaFinal", hrAuditoriaFinal);
    	}
    	if (dtVigenciaInicial != null) {
    		s.addCustomCriteria("CAF.DT_VIGENCIA_INICIAL >= :dtVigenciaInicial");
    		parametros.put("dtVigenciaInicial", dtVigenciaInicial);
    	}
    	if (dtVigenciaFinal != null) {
    		s.addCustomCriteria("(CAF.DT_VIGENCIA_FINAL <= :dtVigenciaFinal OR CAF.DT_VIGENCIA_FINAL IS NULL)");
    		parametros.put("dtVigenciaFinal", dtVigenciaFinal);
    	}
    	
    	// ISSO É NOVO
    	if (idConfiguracaoAuditoriaFil != null) {
    		s.addCustomCriteria("CAF.ID_CONFIGURACAO_AUDITORIA_FIL = :idConfiguracaoAuditoriaFil");
    		parametros.put("idConfiguracaoAuditoriaFil", idConfiguracaoAuditoriaFil);
    	}
    	
    	
    	return new Object[]{s.getSql(allowProjection), parametros};
    }
    
    public Object[] findByIdCustom(TypedFlatMap tfm) {
    	
    	Object [] obj = finderQuery(tfm, true);
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {

    			sqlQuery.addScalar("idConfiguracaoAuditoriaFil",Hibernate.LONG);
    			sqlQuery.addScalar("idFilial",Hibernate.LONG);
    			sqlQuery.addScalar("sgFilial",Hibernate.STRING);
    			sqlQuery.addScalar("nmFilial",Hibernate.STRING);
    			sqlQuery.addScalar("siglaNomeFilial",Hibernate.STRING);    			
    			sqlQuery.addScalar("nrFrota",Hibernate.STRING);
    			sqlQuery.addScalar("nrIdentificador",Hibernate.STRING);
    			sqlQuery.addScalar("hrAuditoriaInicial",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    			sqlQuery.addScalar("hrAuditoriaFinal",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
    				sqlQuery.addScalar("dtVigenciaInicial",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    				sqlQuery.addScalar("dtVigenciaFinal",Hibernate.custom(JodaTimeYearMonthDayNotNullUserType.class));
    			sqlQuery.addScalar("dsRotaColetaEntrega",Hibernate.STRING);
    			sqlQuery.addScalar("tpOperacao",Hibernate.STRING);
    			sqlQuery.addScalar("nrRota",Hibernate.INTEGER);
    			sqlQuery.addScalar("dsRotaIdaVolta",Hibernate.STRING);
    			sqlQuery.addScalar("idMeioTransporte",Hibernate.LONG);
    			sqlQuery.addScalar("idRotaColetaEntrega",Hibernate.LONG);
    			sqlQuery.addScalar("idRotaIdaVolta", Hibernate.LONG);
    			sqlQuery.addScalar("nrVersao",Hibernate.INTEGER);
    			sqlQuery.addScalar("tpRotaIdaVolta", Hibernate.STRING);
    		}
    	};
    	return (Object[]) getAdsmHibernateTemplate().findByIdBySql((String)obj[0], (Map)obj[1], csq);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap tfm, FindDefinition findDef) {
    	
        	Object [] obj = finderQuery(tfm, true);
        	
        	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
        		public void configQuery(org.hibernate.SQLQuery sqlQuery) {

        			sqlQuery.addScalar("idConfiguracaoAuditoriaFil",Hibernate.LONG);
        			sqlQuery.addScalar("idFilial",Hibernate.LONG);
        			sqlQuery.addScalar("sgFilial",Hibernate.STRING);
        			sqlQuery.addScalar("nmFilial",Hibernate.STRING);
        			sqlQuery.addScalar("siglaNomeFilial",Hibernate.STRING);        			
        			sqlQuery.addScalar("nrFrota",Hibernate.STRING);
        			sqlQuery.addScalar("nrIdentificador",Hibernate.STRING);
	        			sqlQuery.addScalar("hrAuditoriaInicial",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
	        			sqlQuery.addScalar("hrAuditoriaFinal",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
	        			sqlQuery.addScalar("dtVigenciaInicial",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
	        			sqlQuery.addScalar("dtVigenciaFinal",Hibernate.custom(JodaTimeYearMonthDayNotNullUserType.class));
        			sqlQuery.addScalar("dsSRotaColetaEntrega",Hibernate.STRING);
        			sqlQuery.addScalar("nrRotaColetaEntrega",Hibernate.INTEGER);
        			sqlQuery.addScalar("tpOperacao",Hibernate.STRING);
        			sqlQuery.addScalar("dsRotaIdaVolta",Hibernate.STRING);
        			sqlQuery.addScalar("nrRota", Hibernate.INTEGER);
        			sqlQuery.addScalar("idMeioTransporte",Hibernate.LONG);
        			sqlQuery.addScalar("idRotaColetaEntrega",Hibernate.LONG);
        			sqlQuery.addScalar("idRotaIdaVolta", Hibernate.LONG);   
        			sqlQuery.addScalar("tpRotaIdaVolta", Hibernate.STRING);
        			
        			
        		}
        	};

       	return getAdsmHibernateTemplate().findPaginatedBySql((String)obj[0], findDef.getCurrentPage(), findDef.getPageSize(),(Map)obj[1],csq);
    }
    
 
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	Object [] obj = finderQuery(tfm, false);
    	return getAdsmHibernateTemplate().getRowCountBySql((String)obj[0], (Map)obj[1]);
	}
    
	public boolean isThereConfiguracaoVigente(Long idConfiguracaoAuditoriaFil, Long idFilial, DomainValue tpOperacao, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, TimeOfDay hrConfiguracaoInicial, TimeOfDay hrConfiguracaoFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		if (idConfiguracaoAuditoriaFil != null)
			dc.add(Restrictions.ne("id", idConfiguracaoAuditoriaFil));
		
		dc.setProjection(Projections.rowCount());  
		dc.add(Restrictions.eq("filial.id", idFilial));
		dc.add(Restrictions.eq("tpOperacao", tpOperacao));
		
		dc = JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
		
		dc.add(Restrictions.ge("hrAuditoriaFinal", hrConfiguracaoInicial));
    	dc.add(Restrictions.le("hrAuditoriaInicial", hrConfiguracaoFinal));
		return (((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0);
	}

    
}