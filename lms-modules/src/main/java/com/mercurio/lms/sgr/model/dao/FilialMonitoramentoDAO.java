package com.mercurio.lms.sgr.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.FilialMonitoramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialMonitoramentoDAO extends BaseCrudDao<FilialMonitoramento, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
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