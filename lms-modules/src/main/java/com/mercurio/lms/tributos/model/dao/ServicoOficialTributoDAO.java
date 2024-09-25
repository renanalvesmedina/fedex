package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoOficialTributoDAO extends BaseCrudDao<ServicoOficialTributo, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoOficialTributo.class;
    }

    /**
     * Busca uma lista de Servi�os Oficiais de Tributos
     * de acordo com os criterios passados e ordenado pela descri��o do servi�o oficial tributo
     * @param criterions Crit�rios para a pesquisa
     * @return List Lista contendo servi�os oficiais de tributos
     */
    public List findListByCriteria(Map criterions) {
    	ArrayList order = new ArrayList();
    	order.add("nrServicoOficialTributo");    	
    	
    	return super.findListByCriteria(criterions,order);
    }


}