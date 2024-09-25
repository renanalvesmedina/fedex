package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;
import com.mercurio.lms.configuracoes.model.dao.RegionalUsuarioDAO;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.regionalUsuarioService"
 */
public class RegionalUsuarioService extends CrudService<RegionalUsuario, Long> {

	private RegionalFilialService regionalFilialService;
	private RegionalService regionalService;
	
	
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	public RegionalService getRegionalService() {
		return this.regionalService;
	}
	
	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}
	
	/**
	 * Recupera uma instância de <code>Autoridade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public RegionalUsuario findById(java.lang.Long id) {
        return (RegionalUsuario)super.findById(id);
    }

    public RegionalUsuario findByIdRegionalEmpresaUsuario(Long idEmpresaUsuario, Long idRegional){
    	return getRegionalUsuarioDAO().findByIdRegionalEmpresaUsuario( idEmpresaUsuario, idRegional );
    }
    
    public List findByIdUsuario(java.lang.Long id) {
        return getRegionalUsuarioDAO().findByIdUsuario(id);
    }

    public List findByIdEmpresaUsuario(java.lang.Long id) {
        return getRegionalUsuarioDAO().findByIdEmpresaUsuario(id);
    }
    
    public List findByIdEmpresaUsuarioSemFetch(Long id){
    	return getRegionalUsuarioDAO().findByIdEmpresaUsuarioSemFetch(id);
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


    public void removeByUsuario(Long id) {
		getRegionalUsuarioDAO().removeByUsuario(id);
	}
    
    public void removeByIdEmpresaUsuario(Long id) {
		getRegionalUsuarioDAO().removeByIdEmpresaUsuario(id);
	}    

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByUsuarios(List ids) {
		getRegionalUsuarioDAO().removeByUsuarios(ids);
	}

    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(RegionalUsuario bean) {
    	Boolean isRegionalEmpresa = regionalFilialService.findByIdRegionalAndIdEmpresa(bean.getRegional().getIdRegional(),bean.getEmpresaUsuario().getEmpresa().getIdEmpresa());

    	Regional regional = (Regional)regionalService.findById(bean.getRegional().getIdRegional());
    	
    	if (isRegionalEmpresa==null || isRegionalEmpresa.booleanValue()==false) throw new BusinessException("LMS_SEG_REGIONAL_EMPRESA", new Object[]{regional.getDsRegional()});
    	return super.store(bean);        
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRegionalUsuarioDAO(RegionalUsuarioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RegionalUsuarioDAO getRegionalUsuarioDAO() {
        return (RegionalUsuarioDAO) getDao();
    }
    
    public void storeWorkFlowForAll(List collection) {
    	getRegionalUsuarioDAO().storeWorkFlowForAll(collection);
    }    

   }