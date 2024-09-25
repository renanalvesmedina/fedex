package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoOficialTributoDAO extends BaseCrudDao<ServicoOficialTributo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoOficialTributo.class;
    }

    /**
     * Busca uma lista de Serviços Oficiais de Tributos
     * de acordo com os criterios passados e ordenado pela descrição do serviço oficial tributo
     * @param criterions Critérios para a pesquisa
     * @return List Lista contendo serviços oficiais de tributos
     */
    public List findListByCriteria(Map criterions) {
    	ArrayList order = new ArrayList();
    	order.add("nrServicoOficialTributo");    	
    	
    	return super.findListByCriteria(criterions,order);
    }


}