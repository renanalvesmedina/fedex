package com.mercurio.lms.integracao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.MunicipioCorporativo;
import com.mercurio.lms.integracao.model.dao.MunicipioCorporativoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.municipioCorporativoService"
 */
public class MunicipioCorporativoService extends CrudService{

	/**
	 * Recupera uma inst�ncia de <code>MunicipioCorporativo</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws //
	 *             
	 */
	public MunicipioCorporativo findById(java.lang.Long id) {
		return (MunicipioCorporativo) super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		//nao faz nada
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
		//nao faz nada
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean
	 * 			 entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MunicipioCorporativo bean) {
		return null;
	}
	
	public List findLookupMunicipio(TypedFlatMap criteria){
		return getMunicipioCorporativoDAO().findLookupMunicipio(criteria);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getMunicipioCorporativoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getMunicipioCorporativoDAO().getRowCount(criteria);
	}
	
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMunicipioCorporativoDAO(MunicipioCorporativoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos 
     * dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MunicipioCorporativoDAO getMunicipioCorporativoDAO() {
        return (MunicipioCorporativoDAO) getDao();
    }
}
