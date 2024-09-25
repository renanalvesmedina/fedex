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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.tpCombustTpMeioTranspService"
 */
public class TpCombustTpMeioTranspService extends CrudService<TpCombustTpMeioTransp, Long> {
	
	
    public ResultSetPage findPaginated(Map criteria) {
		// TODO Auto-generated method stub
		return super.findPaginated(criteria);
	}

	/* REGRA DE NEGOCIO
     * - Regra ao clicar em salvar, deve ser alterada a mensagem de exce��o para LMS-00002 - .
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
	 * Recupera uma inst�ncia de <code>TpCombustTpMeioTransp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public TpCombustTpMeioTransp findById(java.lang.Long id) {
        return (TpCombustTpMeioTransp)super.findById(id);
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
    public java.io.Serializable store(TpCombustTpMeioTransp bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTpCombustTpMeioTranspDAO(TpCombustTpMeioTranspDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TpCombustTpMeioTranspDAO getTpCombustTpMeioTranspDAO() {
        return (TpCombustTpMeioTranspDAO) getDao();
    }

}