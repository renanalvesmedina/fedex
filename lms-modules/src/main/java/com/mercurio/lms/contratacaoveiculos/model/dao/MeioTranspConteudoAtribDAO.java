package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTranspConteudoAtribDAO extends BaseCrudDao<MeioTranspConteudoAtrib, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTranspConteudoAtrib.class;
    }

    private DetachedCriteria getDCFindConteudo(Long idModeloMeioTranspAtributo,Long idMeioTransporte, boolean includeModelo) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"mtca");
    	
    	ProjectionList pl = Projections.projectionList();
    	pl.add(Projections.property("mtca.idMeioTranspConteudoAtrib"),"idMeioTranspConteudoAtrib");
    	pl.add(Projections.property("conteudoAtributoModelo.idConteudoAtributoModelo"),"idConteudoAtributoModelo");
    	pl.add(Projections.property("mtca.dsConteudo"),"dsConteudo");
    	
    	dc.setProjection(pl);
    	dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
    	
    	dc.setFetchMode("conteudoAtributoModelo",FetchMode.JOIN);
    	
    	dc.createAlias("meioTransporte","mt");
    	dc.add(Restrictions.eq("mt.idMeioTransporte",idMeioTransporte));
    	
    	if (includeModelo) {
    		dc.createAlias("modeloMeioTranspAtributo","mmta");
    		dc.add(Restrictions.eq("mmta.idModeloMeioTranspAtributo",idModeloMeioTranspAtributo));
    	}
    	
    	return dc;
    }
    
    public List findConteudoByMeioAndModelo(Long idModeloMeioTranspAtributo,Long idMeioTransporte) {
    	return getAdsmHibernateTemplate().findByDetachedCriteria(getDCFindConteudo(idModeloMeioTranspAtributo,idMeioTransporte,true));
    }
    
    public List findConteudoByMeioTransporte(Long idMeioTransporte) {
    	return getAdsmHibernateTemplate().findByDetachedCriteria(getDCFindConteudo(null,idMeioTransporte,false));
    }
    
    /**
     * Remove todos atributos do meio de transporte.
     * 
     * @param idMeioTransporte
     * @return valor maior que zero caso os registros tenham sido excluidos
     */
    public Integer removeByMeioTransporte(Long idMeioTransporte) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(getPersistentClass().getName()).append(" as mtca ");
		sql.append(" where mtca.meioTransporte.id = :id");
		return getAdsmHibernateTemplate().removeById(sql.toString(), idMeioTransporte);
	}
    
}