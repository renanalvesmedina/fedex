package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.CedenteModalAbrangencia;
import com.mercurio.lms.contasreceber.model.dao.CedenteModalAbrangenciaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.cedenteModalAbrangenciaService"
 */
public class CedenteModalAbrangenciaService extends CrudService<CedenteModalAbrangencia, Long> {


	/**
     * M�todo que busca as CedenteModalAbrangencia de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginatedByCedenteModalAbrangencia(TypedFlatMap criteria){
		return getCedenteModalAbrangenciaDAO().findPaginatedByCedenteModalAbrangencia(criteria);
	}
	
	 /**
     * M�todo que retorna o n�mero de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
	public Integer getRowCountByCedenteModalAbrangencia(TypedFlatMap criteria){
		return getCedenteModalAbrangenciaDAO().getRowCountByCedenteModalAbrangencia(criteria);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>CedenteModalAbrangencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public CedenteModalAbrangencia findById(java.lang.Long id) {
        return (CedenteModalAbrangencia)super.findById(id);
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
    public java.io.Serializable store(CedenteModalAbrangencia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCedenteModalAbrangenciaDAO(CedenteModalAbrangenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CedenteModalAbrangenciaDAO getCedenteModalAbrangenciaDAO() {
        return (CedenteModalAbrangenciaDAO) getDao();
    }
   }