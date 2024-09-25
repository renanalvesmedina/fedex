package com.mercurio.lms.integracao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.integracao.model.PaisCorporativo;
import com.mercurio.lms.integracao.model.dao.PaisCorporativoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.paisCorporativoService"
 */
public class PaisCorporativoService extends CrudService{

	/**
	 * Recupera uma inst�ncia de <code>PaisCorporativo</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws //
	 *             
	 */
	public PaisCorporativo findById(java.lang.Long id) {
		return (PaisCorporativo) super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		//N�o deve remover dados do Corporativo
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		//N�o deve remover dados do Corporativo
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean
	 * 			 entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PaisCorporativo bean) {
		//N�o deve alterar dados do Corporativo
		return null;
	}
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setPaisCorporativoDAO(PaisCorporativoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos 
     * dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private PaisCorporativoDAO getPaisCorporativoDAO() {
        return (PaisCorporativoDAO) getDao();
    }
}
