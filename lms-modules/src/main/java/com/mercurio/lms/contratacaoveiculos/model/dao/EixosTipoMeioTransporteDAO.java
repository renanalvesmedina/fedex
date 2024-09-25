package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EixosTipoMeioTransporteDAO extends BaseCrudDao<EixosTipoMeioTransporte, Long> {

	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EixosTipoMeioTransporte.class;
    }

    public List findByTpMeioTranpAndNrEixos(Long idTipoMeioTransporte, Integer nrEixos) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"ETMT")
    								.createAlias("ETMT.tipoMeioTransporte","TMT");

    	if (idTipoMeioTransporte != null)
    		dc.add(Restrictions.eq("TMT.id",idTipoMeioTransporte));
    	if (nrEixos != null)
    		dc.add(Restrictions.eq("ETMT.qtEixos",nrEixos));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
     
    /**
     * Consulta todos os eixos de um tipo de meio de transporte.
     * 
     * @param idTipoMeioTransporte
     * @return Lista de pojos de quantidadesEixosTipoMeioTransporte
     */
    public List findEixosByTpMeioTransp(Long idTipoMeioTransporte) {
    	DetachedCriteria dc = DetachedCriteria.forClass(EixosTipoMeioTransporte.class,"ETMT")
    			.add(Restrictions.eq("ETMT.tipoMeioTransporte.id",idTipoMeioTransporte));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    /**
     * Consulta a soma dos eixos de um tipo de meio de transporte e seu composto.
     * 
     * @param idTipoMeioTransporte
     * @return Lista de Integer com a quantidade de Eixos do TipoMeioTransporte
     */
    public List findSumEixosByTpMeioTransp(Long idTipoMeioTransporte) {
    	SqlTemplate sql = new SqlTemplate(); 
    	
    	sql.addProjection("DISTINCT eim.qtEixos + NVL(eic.qtEixos,0) ");
    	
    	sql.addFrom("TipoMeioTransporte tmt inner join tmt.eixosTipoMeioTransporte eim " +
    									   "left join tmt.tipoMeioTransporte tmc " +
    									   "left join tmc.eixosTipoMeioTransporte eic");
    	
    	sql.addCriteria("tmt.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	
    	sql.addCriteria("tmt.tpSituacao","=","A");
    	
    	sql.addOrderBy("1"); 
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public List findSumEixosAllMeioTransportes() {
    	SqlTemplate sql = new SqlTemplate(); 
    	  
    	sql.addProjection("DISTINCT eim.qtEixos + NVL(eic.qtEixos,0) ");
    	sql.addProjection("tmt.dsTipoMeioTransporte");
    	sql.addProjection("tmc.dsTipoMeioTransporte");
    	sql.addProjection("tmt.id");

    	sql.addFrom("TipoMeioTransporte tmt inner join tmt.eixosTipoMeioTransporte eim " +
    									   "left join tmt.tipoMeioTransporte tmc " +
    									   "left join tmc.eixosTipoMeioTransporte eic");
    	
    	sql.addCustomCriteria(new StringBuffer("not exists (select TMT2.id from ")
    									.append(TipoMeioTransporte.class.getName()).append(" TMT2 ")
    									.append("where TMT2.tipoMeioTransporte.id = tmt.id AND TMT2.tpSituacao = ?)").toString());
    	sql.addCriteriaValue("A");

    	sql.addCriteria("tmt.tpSituacao","=","A");
    	
    	
    	sql.addOrderBy("eim.qtEixos + NVL(eic.qtEixos,0)");
    	sql.addOrderBy("tmt.dsTipoMeioTransporte");
    	sql.addOrderBy("tmc.dsTipoMeioTransporte");
    	  
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }

	public EixosTipoMeioTransporte findByByIdMeioTransporte(Long idMeioTransporte) {
		StringBuilder hql = new StringBuilder();
		hql.append("select eixos from ").append(getPersistentClass().getName()).append(" eixos ")

		.append(" where exists (select 1 from ").append(MeioTransporteRodoviario.class.getName()).append(" mtr where mtr.id = ? and mtr.eixosTipoMeioTransporte = eixos) ");
		return (EixosTipoMeioTransporte) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idMeioTransporte});
	}
    
}
