package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.TipoServico;
import com.mercurio.lms.configuracoes.model.dao.TipoServicoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.tipoServicoService"
 */
public class TipoServicoService extends CrudService<TipoServico, Long> {
	/**
	 * Recupera uma instância de <code>TipoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TipoServico findById(java.lang.Long id) {
        return (TipoServico)super.findById(id);
    }

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
    @Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage  list = super.findPaginated(criteria);
		return list;
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	@Override
	protected TipoServico beforeStore(TipoServico bean) {
		// Valida o campo Priorizar
		validarCampoPriorizar(bean);
		return bean;
	}

	/**
	 * Valida se apenas um Tipo de Serviço possuí o campo Priorizar igual a sim.
	 * 
	 * @param tipoServico Tipo de Serviço
	 * @throws BusinessException Apenas um Tipo de Serviço pode conter o valor Priorizar igual a sim.
	 */
	private void validarCampoPriorizar(TipoServico tipoServico) {
		if(tipoServico.getBlPriorizar().booleanValue()) {
	    	if(!getTipoServicoDAO().verificarCampoPriorizar(tipoServico.getIdTipoServico())) {
	    		throw new BusinessException("LMS-27001");
	    	}
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TipoServico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoServicoDAO(TipoServicoDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoServicoDAO getTipoServicoDAO() {
        return (TipoServicoDAO) getDao();
    }

    /**
	 * Busca uma entidade entidade TipoServico de acordo com o campo dsTioServico
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/01/2007
	 *
	 * @param dsTipoServico
	 * @return
	 */
	public TipoServico findTipoServicoByDsTipoServico( String dsTipoServico ){
		return getTipoServicoDAO().findTipoServicoByDsTipoServico( dsTipoServico );
	}

}