package com.mercurio.lms.util.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.GregorianChronology;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.Vigencia;

/**
 * Métodos de tratamento do comportamento de vigências. 
 * 
 * @author FelipeF
 * @spring.bean 
 */
public class VigenciaDAO extends AdsmDao {

	public Long getIdentifierFromBean(Object bean) {
		return (Long)getHibernateTemplate().getSessionFactory().getClassMetadata(Hibernate.getClass(bean)).getIdentifier(bean, EntityMode.POJO);
	}
	
	private List findEntidadesVigentes(Class clazz, Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		
		if (id != null) {
			dc.add(Restrictions.eq("id",id));
		}			
		
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Inicial <= '"+dtVigenciaInicial.toString()+"'"));
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Final >= '"+JTDateTimeUtils.maxYmd(dtVigenciaFinal).toString()+"'"));
		
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List findEntidadeVigente(Vigencia vigencia, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		Long id = this.getIdentifierFromBean(vigencia);
		return this.findEntidadesVigentes(vigencia.getClass(),id,dtVigenciaInicial,dtVigenciaFinal);
	}
	
	public List findEntidadesVigentes(Class clazz, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return this.findEntidadesVigentes(clazz,null,dtVigenciaInicial,dtVigenciaFinal);
	}
	
	
	private List findEntidadesVigentes(Class clazz, Long id, YearMonthDay dtReferencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		
		if (id != null) {
			dc.add(Restrictions.eq("id",id));
		}
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Inicial <= '"+dtReferencia.toString()+"'"));
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Final >= '"+dtReferencia.toString()+"'"));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List findEntidadeVigente(Vigencia vigencia, YearMonthDay dtReferencia) {
		Long id = this.getIdentifierFromBean(vigencia);
		return this.findEntidadesVigentes(vigencia.getClass(),id,dtReferencia);
	}
	
	public List findEntidadesVigentes(Class clazz, YearMonthDay dtReferencia) {
		return this.findEntidadesVigentes(clazz,(Long)null,dtReferencia);
	}
	
	public boolean validateIntegridadeVigencia(Class clazz, String strMaster, Vigencia master) {
    	DetachedCriteria dc = DetachedCriteria.forClass(clazz);

    
    	Long idMaster = this.getIdentifierFromBean(master);

    	dc.setProjection(Projections.count("id"));

    	dc.add(Restrictions.eq(strMaster+".id",idMaster));

    	dc.add(Restrictions.or(
    			Restrictions.sqlRestriction("dt_Vigencia_Inicial < '"+master.getDtVigenciaInicial().toString()+"'"),
    			Restrictions.sqlRestriction("dt_Vigencia_Final > '"+JTDateTimeUtils.maxYmd(master.getDtVigenciaFinal()).toString()+"'")
    				));

    	
    	Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    	return (result.intValue() > 0);
    }

}