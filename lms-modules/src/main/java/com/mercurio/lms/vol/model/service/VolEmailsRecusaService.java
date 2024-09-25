package com.mercurio.lms.vol.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vol.model.VolContatos;
import com.mercurio.lms.vol.model.VolEmailsRecusa;
import com.mercurio.lms.vol.model.dao.VolEmailsRecusaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.volEmailsRecusaService"
 */
public class VolEmailsRecusaService extends CrudService<VolEmailsRecusa, Long> {

	private VolEmailsRecusaDAO volEmailRecusaDAO;
	/**
	 * Recupera uma inst�ncia de <code>VolEmailsRecusa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolEmailsRecusa findById(java.lang.Long id) {
        return (VolEmailsRecusa)super.findById(id);
    }

    public List<VolContatos> findContatoByIdRecusa(java.lang.Long id) {
        return (List<VolContatos>)getVolEmailRecusaDAO().findContatoByIdRecusa(id);
    }
    
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(VolEmailsRecusa bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolEmailsRecusaDAO(VolEmailsRecusaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private VolEmailsRecusaDAO getVolEmailsRecusaDAO() {
        return (VolEmailsRecusaDAO) getDao();
    }

	public void setVolEmailRecusaDAO(VolEmailsRecusaDAO volEmailRecusaDAO) {
		this.volEmailRecusaDAO = volEmailRecusaDAO;
	}

	public VolEmailsRecusaDAO getVolEmailRecusaDAO() {
		return volEmailRecusaDAO;
	}
   }

