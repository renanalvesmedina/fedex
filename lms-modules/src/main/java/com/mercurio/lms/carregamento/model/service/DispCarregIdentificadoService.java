package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.dao.DispCarregIdentificadoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.dispCarregIdentificadoService"
 */
public class DispCarregIdentificadoService extends CrudService<DispCarregIdentificado, Long> {


	/**
	 * Recupera uma instância de <code>DispCarregIdentificado</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DispCarregIdentificado findById(java.lang.Long id) {
        return (DispCarregIdentificado)super.findById(id);
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
    public java.io.Serializable store(DispCarregIdentificado bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDispCarregIdentificadoDAO(DispCarregIdentificadoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DispCarregIdentificadoDAO getDispCarregIdentificadoDAO() {
        return (DispCarregIdentificadoDAO) getDao();
    }
    
    /**
     * Retorna uma list de DispCarregIdentificado
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public List findDispCarregIdentificadoByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
    	return this.getDispCarregIdentificadoDAO().findDispCarregIdentificadoByIdCarregamentoDescarga(idCarregamentoDescarga);
    }
    
    /**
     * Retorna o número de registros da uma list de DispCarregIdentificado
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public Integer getRowCountDispCarregIdentificadoByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
    	return this.getDispCarregIdentificadoDAO().getRowCountDispCarregIdentificadoByIdCarregamentoDescarga(idCarregamentoDescarga);
    }     
    
    /**
     * Salva os DispCarregIdentificado de um CarregamentoDescarga
     * 
     * @param itemsDispCarregIdentificado
     */
    public void storeDispCarregIdentificado(List itemsDispCarregIdentificado) {
		this.getDispCarregIdentificadoDAO().storeDispCarregIdentificado(itemsDispCarregIdentificado);
	}	     
    
    /**
     * Localiza os registros DispCarregIdentificado a partir de um idManifesto.
     * @param idManifesto
     * @return
     */
    public List findDispCarregIdentificadoByIdManifesto(Long idManifesto){
    	return this.getDispCarregIdentificadoDAO().findDispCarregIdentificadoByIdManifesto(idManifesto);
    }
    
    /**
     * Método que verifica se o dispositivo de unitização passado por parâmetro faz parte do controle de carga
     * @param idControleCarga
     * @param idDispositivoUnitizacao
     * @return
     */
    public DispCarregIdentificado findByDispositivoUnitizacaoAndControleCarga(Long idControleCarga, Long idDispositivoUnitizacao) {
    	return this.getDispCarregIdentificadoDAO().findByDispositivoUnitizacaoAndControleCarga(idControleCarga, idDispositivoUnitizacao);
    }
    
    
	public List<DispCarregIdentificado> findListDispCarregIdentificadoByControleCarga(Long idControleCarga) {
		return this.getDispCarregIdentificadoDAO().findListDispCarregIdentificadoByControleCarga(idControleCarga);
	}
	
}