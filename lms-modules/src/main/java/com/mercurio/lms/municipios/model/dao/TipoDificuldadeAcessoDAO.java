package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.TipoDificuldadeAcesso;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoDificuldadeAcessoDAO extends BaseCrudDao<TipoDificuldadeAcesso, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoDificuldadeAcesso.class;
    }

    public List findListByCriteria(Map criterions) {
		List listaOrder = new ArrayList();
		listaOrder.add("dsTipoDificuldadeAcesso:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}

}