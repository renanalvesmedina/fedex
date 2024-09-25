package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.MotivoTransferencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoTransferenciaDAO extends BaseCrudDao<MotivoTransferencia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoTransferencia.class;
    }
    
    
	/**
	 * Realiza a ordenação da lista de endereços de Pessoas
	 * @param criterions Mapa de criterios vindo da tela
	 * @return List contendo os endereços encontrados
	 */
	public List findListByCriteria(Map criterions) {
		
		List order = new ArrayList();
		
		order.add("dsMotivoTransferencia");
		
		return super.findListByCriteria(criterions, order);
	}
	
   


}