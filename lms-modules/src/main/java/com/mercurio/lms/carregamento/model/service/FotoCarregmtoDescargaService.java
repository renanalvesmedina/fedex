package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga;
import com.mercurio.lms.carregamento.model.dao.FotoCarregmtoDescargaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.fotoCarregmtoDescargaService"
 */
public class FotoCarregmtoDescargaService extends CrudService<FotoCarregmtoDescarga, Long> {


	/**
	 * Recupera uma instância de <code>FotoCarregmtoDescarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FotoCarregmtoDescarga findById(java.lang.Long id) {
        return (FotoCarregmtoDescarga)super.findById(id);
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
    public java.io.Serializable store(FotoCarregmtoDescarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFotoCarregmtoDescargaDAO(FotoCarregmtoDescargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FotoCarregmtoDescargaDAO getFotoCarregmtoDescargaDAO() {
        return (FotoCarregmtoDescargaDAO) getDao();
    }
    
    /**
     * Retorna uma list de Fotos Carregamento Descarga
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public List findFotoCarregmtoDescarga(Long idCarregamentoDescarga){
    	return this.getFotoCarregmtoDescargaDAO().findFotoCarregmtoDescargaByIdCarregamentoDescarga(idCarregamentoDescarga);
    }
    
    /**
     * Retorna o número de registros da uma list de Fotos Carregamento Descarga
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public Integer getRowCountFotoCarregmtoDescarga(Long idCarregamentoDescarga){
    	return this.getFotoCarregmtoDescargaDAO().getRowCountFotoCarregmtoDescargaByIdCarregamentoDescarga(idCarregamentoDescarga);
    }
    
    /**
     * Salva as fotos de um carregamento descarga
     * 
     * @param listFotosCarregmtoDescarga
     */
    public void storeFotosCarregmtoDescarga(List listFotosCarregmtoDescarga) {
    	this.getFotoCarregmtoDescargaDAO().storeFotosCarregmtoDescarga(listFotosCarregmtoDescarga);
	}	 
    
}