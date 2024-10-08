package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;
import com.mercurio.lms.carregamento.model.dao.CarregamentoPreManifestoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.carregamentoPreManifestoService"
 */
public class CarregamentoPreManifestoService extends CrudService<CarregamentoPreManifesto, Long> {


	/**
	 * Recupera uma inst�ncia de <code>CarregamentoPreManifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public CarregamentoPreManifesto findById(java.lang.Long id) {
        return (CarregamentoPreManifesto)super.findById(id);
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
    public java.io.Serializable store(CarregamentoPreManifesto bean) {
        return super.store(bean);
    }


    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCarregamentoPreManifestoDAO(CarregamentoPreManifestoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CarregamentoPreManifestoDAO getCarregamentoPreManifestoDAO() {
        return (CarregamentoPreManifestoDAO) getDao();
    }
    
    /**
     * Solicita��o da Integra��o - CQPRO00005520
     * Criar um m�todo na classe CarregamentoPreManifestoService que retorne uma instancia da classe CarregamentoPreManifesto conforme os parametros especificados.
     * Nome do m�todo: findCarregamentoPreManifesto(long idManifesto, long idCarregamentoDescarga ) : CarregamentoPreManifesto
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