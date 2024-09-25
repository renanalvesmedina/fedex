package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.ItemBaixaDevMerc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemBaixaDevMercDAO extends BaseCrudDao<ItemBaixaDevMerc, Long>{

	@Override
	protected Class getPersistentClass() {
		return ItemBaixaDevMerc.class;
	}

	
}
