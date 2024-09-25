package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.carregamento.model.dao.CartaoPedagioDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.cartaoPedagioService"
 */
public class CartaoPedagioService extends CrudService<CartaoPedagio, Long> {


	/**
	 * Recupera uma inst�ncia de <code>CartaoPedagio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public CartaoPedagio findById(java.lang.Long id) {
    	CartaoPedagio cartaoPedagio = (CartaoPedagio)super.findById(id);
    	if (JTDateTimeUtils.comparaData(cartaoPedagio.getDtValidade(), JTDateTimeUtils.getDataAtual() ) < 0)
    		cartaoPedagio.setDtValidadeMenorDtAtual("true");
    	else
    		cartaoPedagio.setDtValidadeMenorDtAtual("false");
        return cartaoPedagio;
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
    public java.io.Serializable store(CartaoPedagio bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCartaoPedagioDAO(CartaoPedagioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CartaoPedagioDAO getCartaoPedagioDAO() {
        return (CartaoPedagioDAO) getDao();
    }
    
    protected CartaoPedagio beforeStore(CartaoPedagio bean) {
    	CartaoPedagio cartaoPedagio = (CartaoPedagio)bean;
    	if (cartaoPedagio.getDtValidade() != null &&  JTDateTimeUtils.comparaData(cartaoPedagio.getDtValidade(), JTDateTimeUtils.getDataAtual()) < 0) {
    		throw new BusinessException("LMS-00009");
    	}
    	return super.beforeStore(bean);
    }


    /**
     * 
     * @param idOperadoraCartaoPedagio
     * @param nrCartao
     * @param blVerificarDtValidade
     * @return
     */
    public List findCartaoPedagioByOperadora(Long idOperadoraCartaoPedagio, Long nrCartao, Boolean blVerificarDtValidade){
    	return getCartaoPedagioDAO().findCartaoPedagioByOperadora(null, idOperadoraCartaoPedagio, nrCartao, blVerificarDtValidade);
    }

    
    /**
     * 
     * @param idCartaoPedagio
     */
    public void validateDtValidadeByIdCartaoPedagio(Long idCartaoPedagio) {
    	List resultado = getCartaoPedagioDAO().findCartaoPedagioByOperadora(idCartaoPedagio, null, null, Boolean.TRUE);
    	if (resultado.isEmpty())
    		throw new BusinessException("LMS-05142");
    }
}