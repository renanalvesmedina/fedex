package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.DescargaManifesto;
import com.mercurio.lms.carregamento.model.dao.DescargaManifestoDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.descargaManifestoService"
 */
public class DescargaManifestoService extends CrudService<DescargaManifesto, Long> {


	/**
	 * Recupera uma instância de <code>DescargaManifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DescargaManifesto findById(java.lang.Long id) {
        return (DescargaManifesto)super.findById(id);
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
    public java.io.Serializable store(DescargaManifesto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDescargaManifestoDAO(DescargaManifestoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DescargaManifestoDAO getDescargaManifestoDAO() {
        return (DescargaManifestoDAO) getDao();
    }
    
    /**
     * Retorna um POJO de Descarga Manifesto para o ID do Manifesto
     * 
     * @param idManifesto
     * @return
     */
    public DescargaManifesto findDescargaManifestoByIdManifesto(Long idManifesto) {
    	return this.getDescargaManifestoDAO().findDescargaManifestoByIdManifesto(idManifesto);
    }  	
    
    /**
     * Retorna um POJO de Descarga Manifesto para o ID do Manifesto Coleta
     * 
     * @param idManifestoColeta
     * @return
     */
    public DescargaManifesto findDescargaManifestoByIdManifestoColeta(Long idManifestoColeta) {
    	return this.getDescargaManifestoDAO().findDescargaManifestoByIdManifestoColeta(idManifestoColeta);
    }  	    
    
    
    public List<DescargaManifesto> findByCarregamentoDescarga(CarregamentoDescarga carregamentoDescarga){
    	return this.getDescargaManifestoDAO().findByCarregamentoDescarga(carregamentoDescarga);
    }
    
    
}