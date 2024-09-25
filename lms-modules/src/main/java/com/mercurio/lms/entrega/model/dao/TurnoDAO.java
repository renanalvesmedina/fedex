package com.mercurio.lms.entrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.Turno;
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
public class TurnoDAO extends BaseCrudDao<Turno, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Turno.class;
    }
	  
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial", FetchMode.JOIN); 
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }

	public List validateVigenciaCustom(TypedFlatMap mapa) {
    	DetachedCriteria dc = DetachedCriteria.forClass(Turno.class,"t")
   			.add(Restrictions.and
   				(
   					Restrictions.ge("t.dtVigenciaInicial",mapa.getYearMonthDay("dtVigenciaInicial"))
   					,
   					Restrictions.le("t.dtVigenciaFinal",mapa.getYearMonthDay("dtVigenciaFinal"))
			     ))
   			.add(Restrictions.or
   				(
	                Restrictions.between("t.hrTurnoInicial",mapa.getTimeOfDay("hrTurnoInicial"),mapa.getTimeOfDay("hrTurnoFinal"))
   					,
   					Restrictions.between("t.hrTurnoFinal",mapa.getTimeOfDay("hrTurnoInicial"),mapa.getTimeOfDay("hrTurnoFinal"))
			     )			     
   				 )
			     ;
    	
		return super.findByDetachedCriteria(dc);
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#findListByCriteria(java.util.Map)
	 */
	public List findListByCriteria(Map criterions) {
		// TODO Auto-generated method stub
		return super.findListByCriteria(criterions);
	}

	public boolean findIntervalosTurnosVigentes(Turno turno){
		DetachedCriteria dc = createDetachedCriteria();
		if(turno.getIdTurno()!=null)
			dc.add(Restrictions.ne("idTurno", turno.getIdTurno()));
		
		dc.add(Restrictions.eq("filial.idFilial",turno.getFilial().getIdFilial()));
		 
		dc.add(Restrictions.ilike("dsTurno", turno.getDsTurno(), MatchMode.EXACT));
		
		dc = JTVigenciaUtils.getDetachedVigencia(dc,turno.getDtVigenciaInicial(),turno.getDtVigenciaFinal());
		
		if(findByDetachedCriteria(dc).size()>0){
			dc.add(Restrictions.le("hrTurnoInicial",turno.getHrTurnoFinal()));
			dc.add(Restrictions.ge("hrTurnoFinal",turno.getHrTurnoInicial()));
			return findByDetachedCriteria(dc).size()>0;
		}
		
		return false;
		
	}
	
	/**
	 * Retorna uma Lista de Turnos Vigentes, onde DataAtual esteja entre DT_VIGENCIA_INICIAL and DT_VIGENCIA_FINAL
	 * @param idFilial
	 * @return
	 */
	public List findTurnosVigentes(Long idFilial) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" turno " +
						  " from   Turno turno " +
						  " where  turno.filial.idFilial = " + idFilial + " " +
						  " and    ( " +
						  "         ( ? between turno.dtVigenciaInicial and turno.dtVigenciaFinal ) " +
	  					  "        ) ");
		
		List list = getAdsmHibernateTemplate().find(sql.getSql(), new Object [] {JTDateTimeUtils.getDataAtual()} );

		return list;		
	}
	 
	
	//###################### *** INTEGRAÇÃO *** ################################//
	/**
	 * find para buscar o turno conforme a filial, descrição e data
	 * Método solicitado pela equipe de integracao
	 * @param idFilial
	 * @param dsTurno
	 * @param data
	 * @return Turno
	 */
	public Turno findTurnoByIdFilialDsTurno(String idFilial, String dsTurno, YearMonthDay data){
		SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(Turno.class.getName() +" t inner join t.filial f");
    	sql.addCriteria("lower(t.dsTurno)","=", dsTurno.toLowerCase());
	    sql.addCriteria("f.idFilial","=", Long.valueOf(idFilial)); 
	    sql.addCriteria("t.dtVigenciaInicial","<=",data);
	    sql.addCriteria("t.dtVigenciaFinal",">=",data);
	    Object[] obj = (Object[]) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	    	
	    return (Turno)obj[0];
		}
	

}