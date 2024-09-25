package com.mercurio.lms.integracao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.MunicipioVinculo;
import com.mercurio.lms.integracao.model.dao.MunicipioVinculoDAO;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.integracao.municipioVinculoService"
 */

public class MunicipioVinculoService extends CrudService<MunicipioVinculo, Long> {

	private MunicipioService municipioService;
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage rsp = getMunicipioVinculoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		return rsp;
    }
    
	public Integer getRowCount(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getMunicipioVinculoDAO().getRowCount(criteria);
		return rowCountCustom;
	}

	/**
	 * Recupera uma instância de <code>MunicipioVinculo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public MunicipioVinculo findById(java.lang.Long id) {
        return (MunicipioVinculo) super.findById(id);
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
    public java.io.Serializable store(MunicipioVinculo bean) {
        return super.store(bean);
    }
    
    /**
     * Busca o municipio do LMS vinculado ao Municipio da Integracao
     */
    public Municipio findMunicipioLmsByIdIntegracao(String cep) {
    	return getMunicipioVinculoDAO().findMunicipioLmsByIdIntegracao(cep);
    }
    
    /**
     * Busca a lista de municipios do LMS vinculado ao Municipio da Integracao  
     * através do cep
     */
    public List<Municipio>  findListMunicipioLmsByIdIntegracao(String cep) {
    	return getMunicipioVinculoDAO().findListMunicipioLmsByIdIntegracao(cep);
    }
    
    /**
     * Busca o municipio do LMS vinculado ao Municipio da Integracao via idMunicipio
     */
    public Municipio findMunicipioLmsByIdMunicipio(String idMunicipio) {
    	return getMunicipioVinculoDAO().findMunicipioLmsByIdMunicipio(idMunicipio);
    }
	
    public String findCepMunicipioSOMByIdMunicipio(Long idMunicipio) {
    	List result = getMunicipioVinculoDAO().findCepMunicipioSOMByIdMunicipio(idMunicipio);
    	if(idMunicipio != null && (result == null || result.size() != 1)) {
    		Municipio municipio = municipioService.findById(idMunicipio);
    		Object[] args = new String[1];
    		args[0] = municipio.getNmMunicipio() + "/" + municipio.getUnidadeFederativa().getSgUnidadeFederativa() + " (CEP " + municipio.getNrCep() + ")";
			throw new BusinessException("LMS-04255", args);
    	}
    	return (String) result.get(0);
    }
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMunicipioVinculoDAO(MunicipioVinculoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MunicipioVinculoDAO getMunicipioVinculoDAO() {
        return (MunicipioVinculoDAO) getDao();
    }
	
	public MunicipioService getMunicipioService() {
		return municipioService;
}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	
}
