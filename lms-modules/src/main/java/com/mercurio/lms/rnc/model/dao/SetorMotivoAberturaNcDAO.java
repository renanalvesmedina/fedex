package com.mercurio.lms.rnc.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.rnc.model.SetorMotivoAberturaNc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SetorMotivoAberturaNcDAO extends BaseCrudDao<SetorMotivoAberturaNc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SetorMotivoAberturaNc.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("motivoAberturaNc",FetchMode.SELECT);
		map.put("setor",FetchMode.SELECT);
		super.initFindByIdLazyProperties(map);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("motivoAberturaNc",FetchMode.SELECT);
		map.put("setor",FetchMode.SELECT);
		super.initFindPaginatedLazyProperties(map);
	}

   


}