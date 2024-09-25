package com.mercurio.lms.sgr.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.MunicipioEnquadramento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MunicipioEnquadramentoDAO extends BaseCrudDao<MunicipioEnquadramento, Long>
{
	/**
	 * Nome da query personalizada para buscar uma lista de municipios de origem 
	 */
	private static final String QUERY_MUNICIPIOS_ORIGEM = "com.mercurio.lms.sgr.model.MunicipioEnquadramento.municipiosOrigemByIdEnquadramento"; 

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MunicipioEnquadramento.class;
    }

    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("municipio", FetchMode.JOIN );
		super.initFindListLazyProperties(lazyFindList);
	}

	/**
     * Retorna uma lista de municipios de origem
     * @return List
     */
    public List findMunicipiosOrigem(Long parameterValue) {
    	final String[] parametersName = {"tpInfluencia", "idEnquadramentoRegra"};
    	final Object[] parametersValue = {"O", parameterValue};
    	
    	List result = getAdsmHibernateTemplate().findByNamedQueryAndNamedParam(QUERY_MUNICIPIOS_ORIGEM, parametersName, parametersValue);
    	
    	return result;
    }

}