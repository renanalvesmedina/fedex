package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.SegmentoMercado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SegmentoMercadoDAO extends BaseCrudDao<SegmentoMercado, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return SegmentoMercado.class;
    }
    
    public List findListByCriteria(Map criterions) {
    	List listaOrder = new ArrayList();
		listaOrder.add("dsSegmentoMercado:asc");
		return super.findListByCriteria(criterions,listaOrder);
    }
    
}