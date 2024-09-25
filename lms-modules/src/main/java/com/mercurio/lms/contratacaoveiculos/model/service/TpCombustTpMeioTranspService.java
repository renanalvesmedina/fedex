package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contratacaoveiculos.model.TpCombustTpMeioTransp;
import com.mercurio.lms.contratacaoveiculos.model.dao.TpCombustTpMeioTranspDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.tpCombustTpMeioTranspService"
 */
public class TpCombustTpMeioTranspService extends CrudService<TpCombustTpMeioTransp, Long> {
	
	
    public ResultSetPage findPaginated(Map criteria) {
		// TODO Auto-generated method stub
		return super.findPaginated(criteria);
	}

	/* REGRA DE NEGOCIO
     * - Regra ao clicar em salvar, deve ser alterada a mensagem de exceção para LMS-00002 - .
     *  (non-Javadoc)
     * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
     */
	protected TpCombustTpMeioTransp beforeStore(TpCombustTpMeioTransp bean) {
		TpCombustTpMeioTransp tpCombustTpMeioTransp = (TpCombustTpMeioTransp)bean;
		if(getTpCombustTpMeioTranspDAO().verificaTipoCombustivelMeioTransporte(tpCombustTpMeioTransp.getTipoMeioTransporte().getIdTipoMeioTransporte(),tpCombustTpMeioTransp.getTipoCombustivel().getIdTipoCombustivel(),tpCombustTpMeioTransp.getIdTpCombustTpMeioTransp()))
		     throw new BusinessException("LMS-00002");
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>TpCombustTpMeioTransp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TpCombustTpMeioTransp findById(java.lang.Long id) {
        return (TpCombustTpMeioTransp)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TpCombustTpMeioTransp bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTpCombustTpMeioTranspDAO(TpCombustTpMeioTranspDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TpCombustTpMeioTranspDAO getTpCombustTpMeioTranspDAO() {
        return (TpCombustTpMeioTranspDAO) getDao();
    }

}