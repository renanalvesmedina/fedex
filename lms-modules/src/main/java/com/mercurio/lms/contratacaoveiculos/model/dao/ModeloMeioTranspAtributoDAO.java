package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ModeloMeioTranspAtributoDAO extends BaseCrudDao<ModeloMeioTranspAtributo, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("atributoMeioTransporte",FetchMode.JOIN);
		fetchModes.put("conteudoAtributoModelos",FetchMode.SELECT);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("atributoMeioTransporte",FetchMode.JOIN);
		
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ModeloMeioTranspAtributo.class;
    }
    
    public boolean verificaAtributoModelo(Long idAtributoMeioTransporte,Long idModeloMeioTransporte, Long idModeloMeioTranspAtributo){
    	DetachedCriteria dc = createDetachedCriteria();
    	if(idModeloMeioTranspAtributo != null){
    		dc.add(Restrictions.ne("idModeloMeioTranspAtributo",idModeloMeioTranspAtributo));
    	}
    	DetachedCriteria dcAtributo = dc.createCriteria("atributoMeioTransporte");
    	dcAtributo.add(Restrictions.eq("idAtributoMeioTransporte",idAtributoMeioTransporte));
    	
    	DetachedCriteria dcModelo = dc.createCriteria("tipoMeioTransporte");
    	dcModelo.add(Restrictions.eq("idTipoMeioTransporte",idModeloMeioTransporte));
    	
    	return findByDetachedCriteria(dc).size()>0;
    	
    }
   
	public int removeByIds(List ids) {
		for(int i=0; i< ids.size();i++){
	    	  ModeloMeioTranspAtributo modeloMeioTranspAtributo = (ModeloMeioTranspAtributo)findById((Long)ids.get(i));
	    	  modeloMeioTranspAtributo.getConteudoAtributoModelos().clear();
	    	  getAdsmHibernateTemplate().delete(modeloMeioTranspAtributo);
	    }
		return super.removeByIds(ids);
	}

	public void removeById(Long id) {
		ModeloMeioTranspAtributo modeloMeioTranspAtributo = (ModeloMeioTranspAtributo)findById(id);
		modeloMeioTranspAtributo.getConteudoAtributoModelos().clear();
		getAdsmHibernateTemplate().delete(modeloMeioTranspAtributo);
	}
	
	public List findAtributosByModelo(Long idModelo) {
		DetachedCriteria dc = DetachedCriteria.forClass(ModeloMeioTranspAtributo.class,"modeloAtributos");
			
		dc.createAlias("tipoMeioTransporte","mod");
		dc.createAlias("atributoMeioTransporte","atrib");
		
		dc.setFetchMode("mod",FetchMode.JOIN);
		dc.setFetchMode("atrib",FetchMode.JOIN);
		
		dc.add(Restrictions.eq("mod.idTipoMeioTransporte",idModelo));
		dc.add(Restrictions.eq("tpSituacao",new DomainValue("A")));
		
		dc.addOrder(OrderVarcharI18n.asc("atrib.dsGrupo", LocaleContextHolder.getLocale()));
		dc.addOrder(Order.asc("atrib.nrOrdem"));
		
		List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		
		return l;
	}
	
	/**
	 * Solicitação CQPRO00006051 da integração.
	 * Método que retorna uma instancia da classe ModeloMeioTranspAtributo de acordo com os parâmetros passados.
	 * @param idAtributoMeioTransporte
	 * @param idTipoMeioTransporte 
	 * @return
	 */
	public ModeloMeioTranspAtributo findAtributoTipoMeioTransporte(Long idAtributoMeioTransporte , Long idTipoMeioTransporte){
		DetachedCriteria dc = DetachedCriteria.forClass(ModeloMeioTranspAtributo.class, "mmta");
		dc.add(Restrictions.eq("mmta.atributoMeioTransporte.id", idAtributoMeioTransporte));
		dc.add(Restrictions.eq("mmta.tipoMeioTransporte.id", idTipoMeioTransporte));
		return (ModeloMeioTranspAtributo)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}