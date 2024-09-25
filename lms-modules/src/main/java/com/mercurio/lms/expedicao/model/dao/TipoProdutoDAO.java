package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.TipoProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoProdutoDAO extends BaseCrudDao<TipoProduto, Long>
{

	public List findListByCriteria(Map criterions) {
		List listaOrder = new ArrayList();
		listaOrder.add("dsTipoProduto:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoProduto.class;
    }

   


}