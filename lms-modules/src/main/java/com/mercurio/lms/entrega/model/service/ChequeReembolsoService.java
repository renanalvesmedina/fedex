package com.mercurio.lms.entrega.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.ChequeReembolso;
import com.mercurio.lms.entrega.model.dao.ChequeReembolsoDAO;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.chequeReembolsoService"
 */
public class ChequeReembolsoService extends CrudService<ChequeReembolso, Long> {

	private LMComplementoDAO lmComplementoDao;
	
	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ChequeReembolso</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ChequeReembolso findById(java.lang.Long id) {
        return (ChequeReembolso)super.findById(id);
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
    public java.io.Serializable store(ChequeReembolso bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna Cheques de Reembolso de um Recibo espec�fico.
     * @param idReciboReembolso
     * @return List com pojos ChequeReembolso.
     */
	public List findChequesReciboReembolso(Long idReciboReembolso) {
    	return getChequeReembolsoDAO().findChequesReciboReembolso(idReciboReembolso);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setChequeReembolsoDAO(ChequeReembolsoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ChequeReembolsoDAO getChequeReembolsoDAO() {
        return (ChequeReembolsoDAO) getDao();
    }
    
    public List findPaginatedChequesByIdReembolso(Long idDoctoServico){
    	return lmComplementoDao.findPaginatedChequesByIdReembolso(idDoctoServico);
    }
}