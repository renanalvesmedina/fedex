package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ServicoMunicipio;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoMunicipioDAO extends BaseCrudDao<ServicoMunicipio, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoMunicipio.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("municipio",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("municipio",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }
    
    /**
     * Método que garante a ordenação do combo de Serviços do Município pelo
     * número do serviço do município.
     * @param criterions Critérios de pesquisa
     * @return List Contendo os Serviços do Município retornados da pesquisa e ordenas pelo número do serviço
     */
    public List findListByCriteria(Map criterions) {
        ArrayList order = new ArrayList();
        order.add("nrServicoMunicipio");
        return super.findListByCriteria(criterions,order);
    }
}