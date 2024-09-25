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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
	 * Recupera uma inst�ncia de <code>Autoridade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
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


    public void removeByUsuario(Long id) {
		getRegionalUsuarioDAO().removeByUsuario(id);
	}
    
    public void removeByIdEmpresaUsuario(Long id) {
		getRegionalUsuarioDAO().removeByIdEmpresaUsuario(id);
	}    

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByUsuarios(List ids) {
		getRegionalUsuarioDAO().removeByUsuarios(ids);
	}

    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRegionalUsuarioDAO(RegionalUsuarioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RegionalUsuarioDAO getRegionalUsuarioDAO() {
        return (RegionalUsuarioDAO) getDao();
    }
    
    public void storeWorkFlowForAll(List collection) {
    	getRegionalUsuarioDAO().storeWorkFlowForAll(collection);
    }    

   }