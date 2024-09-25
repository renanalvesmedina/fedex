package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;
import com.mercurio.lms.carregamento.model.dao.DispCarregDescQtdeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.dispCarregDescQtdeService"
 */
public class DispCarregDescQtdeService extends CrudService<DispCarregDescQtde, Long> {


	/**
	 * Recupera uma instância de <code>DispCarregDescQtde</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DispCarregDescQtde findById(java.lang.Long id) {
        return (DispCarregDescQtde)super.findById(id);
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
    public java.io.Serializable store(DispCarregDescQtde bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDispCarregDescQtdeDAO(DispCarregDescQtdeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DispCarregDescQtdeDAO getDispCarregDescQtdeDAO() {
        return (DispCarregDescQtdeDAO) getDao();
    }
 
    /**
     * Retorna uma list de DispCarregDescQtde
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public List findDispCarregDescQtdeByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
    	return this.getDispCarregDescQtdeDAO().findDispCarregDescQtdeByIdCarregamentoDescarga(idCarregamentoDescarga);
    }
    
    /**
     * Retorna o número de registros da uma list de DispCarregDescQtde
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public Integer getRowCountDispCarregDescQtdeByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
    	return this.getDispCarregDescQtdeDAO().getRowCountDispCarregDescQtdeByIdCarregamentoDescarga(idCarregamentoDescarga);
    }       
    
    /**
     * Salva os DispCarregDescQtde de um CarregamentoDescarga
     * 
     * @param itemsDispCarregDescQtde
     */
    public void storeDispCarregDescQtde(List itemsDispCarregDescQtde) {
		this.getDispCarregDescQtdeDAO().storeDispCarregDescQtde(itemsDispCarregDescQtde);
	}	    
    
    /**
     * Localiza os registros DispCarregDescQtde a partir de um idManifesto.
     * @param idManifesto
     * @return
     */
    public List findDispCarregDescQtdeByIdManifesto(Long idManifesto){
    	return this.getDispCarregDescQtdeDAO().findDispCarregDescQtdeByIdManifesto(idManifesto);
    }
}