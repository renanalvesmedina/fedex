package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.TpCombustTpMeioTransp;
import com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoCombustivelDAO extends BaseCrudDao<TipoCombustivel, Long>
{
   
	public List findListByCriteria(Map criterions) {
		List lista = new ArrayList();
		lista.add("dsTipoCombustivel");
		return super.findListByCriteria(criterions,lista);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoCombustivel.class;
    }
   
    public List findTipoCombustivelByTpMeioTransporte(TypedFlatMap tfm){
    	SqlTemplate sql = new SqlTemplate();  	
    
    	sql.addProjection("tc");

    	sql.addInnerJoin(TpCombustTpMeioTransp.class.getName(), "tctmt");
    	sql.addInnerJoin("tctmt.tipoMeioTransporte", "tmt");
    	sql.addInnerJoin("tctmt.tipoCombustivel", "tc");

    	sql.addCriteria("tc.tpSituacao", "=", tfm.getDomainValue("tpSituacao").getValue());
    	sql.addCriteria("tmt.id", "=", tfm.getLong("id"));
	
    	sql.addOrderBy("tc.dsTipoCombustivel");
   
    	return (List)getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }




	
   


}