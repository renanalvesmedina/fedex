package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.NotaDebitoInternacionalDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.notaDebitoInternacionalService"
 */
public class NotaDebitoInternacionalService {

	private NotaDebitoInternacionalDAO notaDebitoInternacionalDAO;

	/**
	 * Recupera uma instância de <code>NotaDebitoNacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Fatura findById(java.lang.Long id) {
        return (Fatura)this.notaDebitoInternacionalDAO.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	this.notaDebitoInternacionalDAO.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
		this.notaDebitoInternacionalDAO.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void store(Fatura bean) {
        this.notaDebitoInternacionalDAO.store(bean);
    }
    
    /**
     * Retorna a nota de debito internacional com o número da nota e a filial informado.
     * 
     * @author Mickaël Jalbert
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
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setNotaDebitoInternacionalDAO(NotaDebitoInternacionalDAO dao) {
        this.notaDebitoInternacionalDAO = dao;
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
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