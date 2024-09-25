package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.CiaAereaCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CiaAereaClienteDAO extends BaseCrudDao<CiaAereaCliente, Long>
{
	public List findLookupByCriteria(Map criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cia");
		dc.setProjection(
				Projections.projectionList()
					.add(Projections.property("cia.idCiaAereaCliente"), "idCiaAereaCliente")
					.add(Projections.property("emp.idEmpresa"), "empresa_idEmpresa")
					.add(Projections.property("pes.nmPessoa"), "empresa_pessoa_nmPessoa")
			);
		dc.createAlias("cia.empresa", "emp");
		dc.createAlias("emp.pessoa", "pes");
		
		dc.addOrder(Order.asc("pes.nmPessoa"));
		
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CiaAereaCliente.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("empresa",FetchMode.JOIN);
    	lazyFindById.put("empresa.pessoa",FetchMode.JOIN);    	
    }
    
    /**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE cliente.id = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
	
	public String findCiaAereaPorAWB(Long idCiaFilialMercurio){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e.sgEmpresa ");
		sql.append(" FROM " + CiaFilialMercurio.class.getSimpleName() + " cia ");
		sql.append(" JOIN cia.empresa e ");
		sql.append("Where cia.idCiaFilialMercurio=? ");
		
		List lista = getAdsmHibernateTemplate().find(sql.toString(), idCiaFilialMercurio);
		
		if(lista != null){
			return lista.get(0).toString();
		}
		return null;
		
	}
}