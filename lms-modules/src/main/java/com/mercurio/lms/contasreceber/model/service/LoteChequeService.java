package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.LoteCheque;
import com.mercurio.lms.contasreceber.model.dao.LoteChequeDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.loteChequeService"
 */
public class LoteChequeService extends CrudService<LoteCheque, Long> {

	/**
	 * Facade para a valida��o se o Lote est� fechado
	 * @param idLoteCheque Identificador do lote 
	 * @exception BusinessException("LMS-36162") : Se o lote est� fechado n�o pode mais ser alterado
	 */
	public void validateLoteCheque(Long idLoteCheque){
		LoteCheque loteCheque = (LoteCheque)getLoteChequeDAO().getAdsmHibernateTemplate().get(LoteCheque.class, idLoteCheque);		
		validateLoteCheque(loteCheque);
	}	
	
	/**
	 * Valida��o se o Lote est� fechado
	 * @param idLoteCheque Identificador do lote 
	 * @exception BusinessException("LMS-36162") : Se o lote est� fechado n�o pode mais ser alterado
	 */
	public void validateLoteCheque(LoteCheque loteCheque){
		if (loteCheque.getBlFechado().equals(Boolean.TRUE)){
			throw new BusinessException("LMS-36162");
		}
	}
	
	protected LoteCheque beforeStore(LoteCheque bean) {
		validateLoteCheque((LoteCheque)bean);
		return super.beforeStore(bean);
	}
	
	protected void beforeRemoveById(Long id) {
		validateLoteCheque((Long)id);		
		super.beforeRemoveById(id);
	}
	
	protected void beforeRemoveByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long idLoteCheque = (Long) iter.next();			
			validateLoteCheque(idLoteCheque);
		}
		
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}
	
    public List findLookupLoteCheque(TypedFlatMap criteria) {
    	return getLoteChequeDAO().findLookupLoteCheque(criteria);
    }
	
	/**
	 * Monta combo de moeda da tela
	 * @return List combo de moedas conforme pais do usuario logado
	 */
	public List findComboMoeda(){
		return getLoteChequeDAO().findComboMoeda();
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setLoteChequeDAO(LoteChequeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	public LoteChequeDAO getLoteChequeDAO() {
		return (LoteChequeDAO) getDao();
	}	
	
}