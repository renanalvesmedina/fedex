package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.FilialMonitoramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialMonitoramentoDAO extends BaseCrudDao<FilialMonitoramento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialMonitoramento.class;
    }    
    
	protected void initFindByIdLazyProperties(Map map) {
		map.put("filialByIdFilialMonitorada.pessoa", FetchMode.SELECT);
		map.put("filialByIdFilialResponsavel.pessoa", FetchMode.SELECT);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("filialByIdFilialMonitorada.pessoa", FetchMode.SELECT);
		map.put("filialByIdFilialResponsavel.pessoa", FetchMode.SELECT);
	}
    
}