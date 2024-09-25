package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.lms.carregamento.model.PadraoCodigoBarra;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PadraoCodigoBarraDAO extends BaseCrudDao<PadraoCodigoBarra, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PadraoCodigoBarra.class;
	}
	
	public List<PadraoCodigoBarra> findPadraoCodigoBarraByNrCaracter(String codigoBarra){
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("nrTamanho", Long.valueOf(codigoBarra.length())));
		
		return (List<PadraoCodigoBarra>) c.list();	
	}
}