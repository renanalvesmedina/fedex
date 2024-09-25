package com.mercurio.lms.configuracoes.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.RamoAtividade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RamoAtividadeDAO extends BaseCrudDao<RamoAtividade, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return RamoAtividade.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
        fetchModes.put("codigoFiscalOperacao", FetchMode.SELECT);        
    }
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("codigoFiscalOperacao", FetchMode.SELECT);
    }    

}