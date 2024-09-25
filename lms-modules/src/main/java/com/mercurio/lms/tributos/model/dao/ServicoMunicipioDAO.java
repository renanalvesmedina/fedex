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
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoMunicipioDAO extends BaseCrudDao<ServicoMunicipio, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
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
     * M�todo que garante a ordena��o do combo de Servi�os do Munic�pio pelo
     * n�mero do servi�o do munic�pio.
     * @param criterions Crit�rios de pesquisa
     * @return List Contendo os Servi�os do Munic�pio retornados da pesquisa e ordenas pelo n�mero do servi�o
     */
    public List findListByCriteria(Map criterions) {
        ArrayList order = new ArrayList();
        order.add("nrServicoMunicipio");
        return super.findListByCriteria(criterions,order);
    }
}