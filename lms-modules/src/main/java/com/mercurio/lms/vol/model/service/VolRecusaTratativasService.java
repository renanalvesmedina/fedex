package com.mercurio.lms.vol.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.dao.VolRecusasDAO;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolRecusasDAO(VolRecusasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private VolRecusasDAO getVolRecusasDAO() {
        return (VolRecusasDAO) getDao();
    }


}
