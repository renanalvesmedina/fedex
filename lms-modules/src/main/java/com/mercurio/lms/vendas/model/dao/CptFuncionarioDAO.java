package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.CptFuncionario;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CptFuncionarioDAO extends BaseCrudDao<CptFuncionario, Long> {

	protected final Class getPersistentClass() {
		return CptFuncionario.class;
	}
	
	protected void initFindByIdLazyProperties(Map map) {		
        map.put("cliente", FetchMode.JOIN);
        map.put("cliente.pessoa", FetchMode.JOIN);
	}

	/**
	 * Obtem todas as vigencias que possuem BL_VIGENCIA_INICIAL = N ou BL_VIGENCIA_FINAL = N 
	 * @return
	 */
	public List findVigencias() {
	
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cpt")
		.add(Restrictions.or(Restrictions.eq("blVigenciaInicial", Boolean.FALSE), Restrictions.eq("blVigenciaFinal", Boolean.FALSE)));
				
		return super.findByDetachedCriteria(dc);
	}

	
	public List findFuncionarioCliente(Long idCliente, String nrMatricula) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
		.add(Restrictions.eq("cliente.id", idCliente))
		.add(Restrictions.eq("nrMatricula", nrMatricula));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
			
}