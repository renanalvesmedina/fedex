package com.mercurio.lms.vol.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.dao.VolRecusasDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volRecusaTratativasService"
 */
public class VolRecusaTratativasService extends CrudService{
	
	/**
	 * recupera os contatos relacionado com a recusa
	 *@param idRecusa
	 *@return contatos 
	 */
	public List findContato(TypedFlatMap criteria){	
		return this.getVolRecusasDAO().findContato(criteria); 

	}
    
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolRecusasDAO(VolRecusasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolRecusasDAO getVolRecusasDAO() {
        return (VolRecusasDAO) getDao();
    }


}
