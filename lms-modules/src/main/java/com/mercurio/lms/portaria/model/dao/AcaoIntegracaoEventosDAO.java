package com.mercurio.lms.portaria.model.dao;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AcaoIntegracaoEventosDAO  extends BaseCrudDao<AcaoIntegracaoEvento, Long>{

	@Override
	protected Class getPersistentClass() {
		return AcaoIntegracaoEvento.class;
	}
	
	public Long findLastAgrupador(){
		StringBuffer sql = new StringBuffer();
		sql.append("select max(nrAgrupador) from AcaoIntegracaoEvento ");
		
		List<Long> list = getAdsmHibernateTemplate().find(sql.toString());
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0);
		}
		return Long.valueOf(0);
	}

	public AcaoIntegracaoEvento findByProcesso(String dsProcesso) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(AcaoIntegracaoEvento.class, "aie")
		.createAlias("aie.acaoIntegracao", "ai")
		.add(Restrictions.eq("ai.dsProcessoIntegracao", dsProcesso));
		
		List list = findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()){
			return (AcaoIntegracaoEvento) list.get(0);
}
		
		return null;	
	}
}
