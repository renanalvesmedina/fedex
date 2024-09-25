package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.NotaDebitoInternacionalDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.notaDebitoInternacionalService"
 */
public class NotaDebitoInternacionalService {

	private NotaDebitoInternacionalDAO notaDebitoInternacionalDAO;

	/**
	 * Recupera uma inst�ncia de <code>NotaDebitoNacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Fatura findById(java.lang.Long id) {
        return (Fatura)this.notaDebitoInternacionalDAO.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	this.notaDebitoInternacionalDAO.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
		this.notaDebitoInternacionalDAO.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void store(Fatura bean) {
        this.notaDebitoInternacionalDAO.store(bean);
    }
    
    /**
     * Retorna a nota de debito internacional com o n�mero da nota e a filial informado.
     * 
     * @author Micka�l Jalbert
     * @since 07/07/2006
     * 
     * @param nrNotaDebito
     * @param idFilial
     * 
     * @return List
     */    
    public List findByNrNotaDebitoByFilial(Long nrNotaDebito, Long idFilial){
    	return getNotaDebitoInternacionalDAO().findByNrFaturaByFilial(nrNotaDebito, idFilial);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setNotaDebitoInternacionalDAO(NotaDebitoInternacionalDAO dao) {
        this.notaDebitoInternacionalDAO = dao;
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private NotaDebitoInternacionalDAO getNotaDebitoInternacionalDAO() {
        return (NotaDebitoInternacionalDAO) notaDebitoInternacionalDAO;
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getNotaDebitoInternacionalDAO().findPaginated(criteria, null);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getNotaDebitoInternacionalDAO().getRowCount(criteria);
	}
}