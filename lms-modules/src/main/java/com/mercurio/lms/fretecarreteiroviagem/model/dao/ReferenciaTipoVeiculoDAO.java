package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaTipoVeiculo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReferenciaTipoVeiculoDAO extends BaseCrudDao<ReferenciaTipoVeiculo, Long>
{

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoMeioTransporte", FetchMode.JOIN);
		
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReferenciaTipoVeiculo.class;
    }
    
    public List findReferenciaTipoVeiculoByIdReferenciaFrete(Long idRefFreteCarreteiro){
    		
    	DetachedCriteria dc = DetachedCriteria.forClass(ReferenciaTipoVeiculo.class,"rtv")
    	.createAlias("tipoMeioTransporte", "tmt")
    	.createAlias("referenciaFreteCarreteiro","rfc");
    	
    	
    	dc.add(Restrictions.eq("referenciaFreteCarreteiro.idReferenciaFreteCarreteiro",idRefFreteCarreteiro));
    	return findByDetachedCriteria(dc);
    }
    
    public BigDecimal findValorFreteCarreteiroByIdReferenciaFrete(Long idRefFreteCarreteiro, Long idTipoMeioTransporte, Integer qtKm ){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("sum(rtv.vlFreteReferencia)");
    	
    	hql.addFrom(ReferenciaTipoVeiculo.class.getName(), new StringBuffer("rtv ")
    			.append("join rtv.referenciaFreteCarreteiro as rfc ")
    			.append("join rtv.tipoMeioTransporte as tmt ")
    			.toString());
    	
    	hql.addCriteria("rfc.idReferenciaFreteCarreteiro","=", idRefFreteCarreteiro);
    	hql.addCriteria("tmt.idTipoMeioTransporte","=", idTipoMeioTransporte);
    	if (qtKm != null) {
	    	hql.addCriteria("rtv.qtKmInicial","<=", qtKm);
	    	hql.addCriteria("rtv.qtKmFinal",">=", qtKm);
    	}
	    return (BigDecimal)getAdsmHibernateTemplate().find(hql.getSql().toString(),hql.getCriteria()).get(0);
    	
    	
    }

   


}