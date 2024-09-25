package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.prestcontasciaaerea.model.IcmsPrestacao;
import com.mercurio.lms.prestcontasciaaerea.model.dao.IcmsPrestacaoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.prestcontasciaaerea.icmsPrestacaoService"
 */
public class IcmsPrestacaoService extends CrudService<IcmsPrestacao, Long> {


	/**
	 * Recupera uma inst�ncia de <code>IcmsPrestacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public IcmsPrestacao findById(java.lang.Long id) {
        return (IcmsPrestacao)super.findById(id);
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
    public java.io.Serializable store(IcmsPrestacao bean) {
    	return  super.store(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * Faz um flush logo em seguida
	 *
	 *
	 *@author Diego Umpierre
	 *	31/05/2006
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 *  
	 */
    public java.io.Serializable storeWithFlush(IcmsPrestacao bean) {
    	Serializable ret = super.store(bean);
    	getIcmsPrestacaoDAO().getAdsmHibernateTemplate().flush();
        return  ret;
    }    
    
    
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIcmsPrestacaoDAO(IcmsPrestacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IcmsPrestacaoDAO getIcmsPrestacaoDAO() {
        return (IcmsPrestacaoDAO) getDao();
    }
    
    /**
     * Remove os registros de ICMS da presta��o em quest�o
     * @param idPrestacaoConta
     */
    public void removeDesmarcarPrestacaoConta(Long idPrestacaoConta){
        
    	getIcmsPrestacaoDAO().removeDesmarcarPrestacaoConta(idPrestacaoConta);
    	
    }
    
   }