package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.ParametroIssMunicipio;
import com.mercurio.lms.tributos.model.dao.ParametroIssMunicipioDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.parametroIssMunicipioService"
 */
public class ParametroIssMunicipioService extends CrudService<ParametroIssMunicipio, Long> {

	private ServicoTributoService servicoTributoService;
    private ServicoAdicionalService servicoAdicionalService;
    private ServicoOficialTributoService servicoOficialTributoService;
    private UnidadeFederativaService unidadeFederativaService;


	/**
	 * Recupera uma inst�ncia de <code>ParametroIssMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ParametroIssMunicipio findById(java.lang.Long id) {
        return (ParametroIssMunicipio)super.findById(id);
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
     * Busca o parametro pelo id do municipio
     * @param municipio
     * @return
     */
    public ParametroIssMunicipio findByMunicipio(Long idMunicipio) {
    	return getParametroIssMunicipioDAO().findByMunicipio(idMunicipio);
    	}
    	
        
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ParametroIssMunicipio bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setParametroIssMunicipioDAO(ParametroIssMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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