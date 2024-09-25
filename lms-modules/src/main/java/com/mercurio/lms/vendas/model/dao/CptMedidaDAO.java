package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.CptMedida;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CptMedidaDAO extends BaseCrudDao<CptMedida, Long> {

	protected final Class getPersistentClass() {
		return CptMedida.class;
	}
	
	public void initFindByIdLazyProperties(Map map) {		
        map.put("cliente", FetchMode.JOIN);
        map.put("cliente.pessoa", FetchMode.JOIN);        
        map.put("cptComplexidade", FetchMode.JOIN);
        map.put("cptComplexidade.cptTipoValor", FetchMode.JOIN);
	}

	public List findComplexidadeCliente(Long idCliente, Long idCptTipoValor) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
		.setFetchMode("cptComplexidade", FetchMode.JOIN)
		.setFetchMode("cptComplexidade.cptTipoValor", FetchMode.JOIN)
		.createAlias("cptComplexidade.cptTipoValor", "tpValor")
		.add(Restrictions.eq("cliente.id", idCliente))
		.add(Restrictions.eq("tpValor.id", idCptTipoValor));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}	
	
}