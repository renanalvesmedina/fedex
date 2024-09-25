package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.Densidade;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DensidadeDAO extends BaseCrudDao<Densidade, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Densidade.class;
    }

	public List findAllAtivo() {
		DetachedCriteria dc = DetachedCriteria.forClass(Densidade.class, "d")
			.add(Restrictions.eq("d.tpSituacao", "A"))
			.setProjection(
					Projections.projectionList()
					.add(Projections.property("d.idDensidade"), "idDensidade")
					.add(Projections.property("d.tpDensidade"), "tpDensidade")
					.add(Projections.property("d.vlFator"), "vlFator"))
			.addOrder(Order.asc("d.tpDensidade"))		
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public Densidade findByIdEmpresaTpDensidade(Long idEmpresa, String tpDensidade) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.createAlias("d.empresa", "e")
			.add(Restrictions.eq("e.id", idEmpresa))
			.add(Restrictions.eq("d.tpDensidade", tpDensidade))
			.add(Restrictions.eq("d.tpSituacao", "A"));
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()){
			return (Densidade)l.get(0);
		}
		return null;
	}
   
	public Densidade findByTpDensidade(String tpDensidade){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
		.add(Restrictions.eq("d.tpDensidade", tpDensidade))
		.add(Restrictions.eq("d.tpSituacao", "A"));
	
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()){
			return (Densidade)l.get(0);
		}

		return null;
	}


}