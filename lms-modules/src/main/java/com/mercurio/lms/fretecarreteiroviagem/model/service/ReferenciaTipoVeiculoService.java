package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaTipoVeiculo;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.ReferenciaTipoVeiculoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteiroviagem.referenciaTipoVeiculoService"
 */
public class ReferenciaTipoVeiculoService extends CrudService<ReferenciaTipoVeiculo, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ReferenciaTipoVeiculo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ReferenciaTipoVeiculo findById(java.lang.Long id) {
        return (ReferenciaTipoVeiculo)super.findById(id);
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
    public java.io.Serializable store(ReferenciaTipoVeiculo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setReferenciaTipoVeiculoDAO(ReferenciaTipoVeiculoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ReferenciaTipoVeiculoDAO getReferenciaTipoVeiculoDAO() {
        return (ReferenciaTipoVeiculoDAO) getDao();
    }
    
    public List findReferenciaTipoVeiculoByIdReferenciaFrete(Long idRefFreteCarreteiro){
    	return getReferenciaTipoVeiculoDAO().findReferenciaTipoVeiculoByIdReferenciaFrete(idRefFreteCarreteiro);
    }
    
    public BigDecimal findValorFreteCarreteiroByIdReferenciaFrete(Long idRefFreteCarreteiro, Long idTipoMeioTransporte, Integer qtKm ){
    	return getReferenciaTipoVeiculoDAO().findValorFreteCarreteiroByIdReferenciaFrete(idRefFreteCarreteiro,idTipoMeioTransporte,qtKm);
    }
}