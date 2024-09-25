package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.ParametroIssMunicipio;
import com.mercurio.lms.tributos.model.dao.ParametroIssMunicipioDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.parametroIssMunicipioService"
 */
public class ParametroIssMunicipioService extends CrudService<ParametroIssMunicipio, Long> {

	private ServicoTributoService servicoTributoService;
    private ServicoAdicionalService servicoAdicionalService;
    private ServicoOficialTributoService servicoOficialTributoService;
    private UnidadeFederativaService unidadeFederativaService;


	/**
	 * Recupera uma instância de <code>ParametroIssMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ParametroIssMunicipio findById(java.lang.Long id) {
        return (ParametroIssMunicipio)super.findById(id);
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
     * Busca o parametro pelo id do municipio
     * @param municipio
     * @return
     */
    public ParametroIssMunicipio findByMunicipio(Long idMunicipio) {
    	return getParametroIssMunicipioDAO().findByMunicipio(idMunicipio);
    	}
    	
        
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ParametroIssMunicipio bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setParametroIssMunicipioDAO(ParametroIssMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ParametroIssMunicipioDAO getParametroIssMunicipioDAO() {
        return (ParametroIssMunicipioDAO) getDao();
    }

	public ServicoTributoService getServicoTributoService() {
		return servicoTributoService;
	}

	public void setServicoTributoService(ServicoTributoService servicoTributoService) {
		this.servicoTributoService = servicoTributoService;
	}

	public ServicoAdicionalService getServicoAdicionalService() {
		return servicoAdicionalService;
	}

	public void setServicoAdicionalService(
			ServicoAdicionalService servicoAdicionalService) {
		this.servicoAdicionalService = servicoAdicionalService;
	}

	public ServicoOficialTributoService getServicoOficialTributoService() {
		return servicoOficialTributoService;
	}

	public void setServicoOficialTributoService(
			ServicoOficialTributoService servicoOficialTributoService) {
		this.servicoOficialTributoService = servicoOficialTributoService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
   }