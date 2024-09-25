package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.MotivoTransferencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoTransferenciaDAO extends BaseCrudDao<MotivoTransferencia, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoTransferencia.class;
    }
    
    
	/**
	 * Realiza a ordena��o da lista de endere�os de Pessoas
	 * @param criterions Mapa de criterios vindo da tela
	 * @return List contendo os endere�os encontrados
	 */
	public List findListByCriteria(Map criterions) {
		
		List order = new ArrayList();
		
		order.add("dsMotivoTransferencia");
		
		return super.findListByCriteria(criterions, order);
	}
	
   


}