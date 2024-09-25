package com.mercurio.lms.tributos.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.CodigoFiscalOperacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CodigoFiscalOperacaoDAO extends BaseCrudDao<CodigoFiscalOperacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CodigoFiscalOperacao.class;
    }
    
    /**
     * Busca o CdCfop do Ramo de Atividade do Cliente informado.<BR>
     *@author Robson Edemar Gehl
     * @param idCliente
     * @return
     */
    public Long findCdCfopRamoAtividadeByCliente(Long idCliente){
		StringBuilder query = new StringBuilder()
		.append(" select cfo.cdCfop ")
		.append(" from Cliente cli ")
		.append(" join cli.ramoAtividade ra ")
		.append(" join ra.codigoFiscalOperacao cfo ")
		.append(" where cli.idCliente = ?");
    	
		List list = getAdsmHibernateTemplate().find(query.toString(), new Object [] {idCliente});
		
		if (list != null && !list.isEmpty()){
    		return (Long) list.get(0);
    	}
    	return null;
    }
   


}