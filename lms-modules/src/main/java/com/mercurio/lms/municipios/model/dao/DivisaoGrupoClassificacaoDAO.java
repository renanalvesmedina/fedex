package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DivisaoGrupoClassificacaoDAO extends BaseCrudDao<DivisaoGrupoClassificacao, Long>
{

	
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("grupoClassificacao",FetchMode.JOIN);
	}
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("grupoClassificacao",FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DivisaoGrupoClassificacao.class;
    }

    public List find(Map criteria) {
    	List order = new ArrayList();
    	order.add("dsDivisaoGrupoClassificacao");
    	return findListByCriteria(criteria,order);
    }
    
    public List findByIdGrupoClassificacao(Long idGrupoClassificacao){
    	ProjectionList pl = Projections.projectionList()
    		.add(Projections.property("d.idDivisaoGrupoClassificacao"),"idDivisaoGrupoClassificacao")
    		.add(Projections.property("d.dsDivisaoGrupoClassificacao"),"dsDivisaoGrupoClassificacao")
    		.add(Projections.property("g.dsGrupoClassificacao"),"dsGrupoClassificacao");
    	DetachedCriteria dc = DetachedCriteria.forClass(DivisaoGrupoClassificacao.class,"d")
    		.createAlias("d.grupoClassificacao","g")
    		.setProjection(pl)
    		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP)
    		.add(Restrictions.eq("g.id",idGrupoClassificacao));
    	return findByDetachedCriteria(dc);
    }
    
   

}