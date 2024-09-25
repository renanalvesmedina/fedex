package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;
import com.mercurio.lms.carregamento.model.dao.CarregamentoPreManifestoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.carregamentoPreManifestoService"
 */
public class CarregamentoPreManifestoService extends CrudService<CarregamentoPreManifesto, Long> {


	/**
	 * Recupera uma instância de <code>CarregamentoPreManifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public CarregamentoPreManifesto findById(java.lang.Long id) {
        return (CarregamentoPreManifesto)super.findById(id);
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
    public java.io.Serializable store(CarregamentoPreManifesto bean) {
        return super.store(bean);
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCarregamentoPreManifestoDAO(CarregamentoPreManifestoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CarregamentoPreManifestoDAO getCarregamentoPreManifestoDAO() {
        return (CarregamentoPreManifestoDAO) getDao();
    }
    
    /**
     * Solicitação da Integração - CQPRO00005520
     * Criar um método na classe CarregamentoPreManifestoService que retorne uma instancia da classe CarregamentoPreManifesto conforme os parametros especificados.
     * Nome do método: findCarregamentoPreManifesto(long idManifesto, long idCarregamentoDescarga ) : CarregamentoPreManifesto
     * @param idManifesto
     * @param idCarregamentoDescarga
     * @return
     */
    public CarregamentoPreManifesto findCarregamentoPreManifesto(Long idManifesto, Long idCarregamentoDescarga){
   		return getCarregamentoPreManifestoDAO().findCarregamentoPreManifesto(idManifesto, idCarregamentoDescarga);
    }

    public CarregamentoPreManifesto findByCarregamentoAndDestino(Long idCarregamentoDescarga, Long idFilialDestino, String tpModal){
    	return getCarregamentoPreManifestoDAO().findByCarregamentoAndDestino(idCarregamentoDescarga, idFilialDestino, tpModal);
    }
    
	public List<CarregamentoPreManifesto> findByIdCarregamentoDescarga(long idCarregamentoDescarga) {
		return getCarregamentoPreManifestoDAO().findByIdCarregamentoDescarga(idCarregamentoDescarga);
	}
    
   }