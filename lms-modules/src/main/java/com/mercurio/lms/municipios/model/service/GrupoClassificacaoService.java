package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.GrupoClassificacao;
import com.mercurio.lms.municipios.model.dao.GrupoClassificacaoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.grupoClassificacaoService"
 */
public class GrupoClassificacaoService extends CrudService<GrupoClassificacao, Long> {


	/**
	 * Recupera uma instância de <code>GrupoClassificacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public GrupoClassificacao findById(java.lang.Long id) {
        return (GrupoClassificacao)super.findById(id);
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
    
    public List find(Map criteria) {
    	return getGrupoClassificacaoDAO().find(criteria);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public GrupoClassificacao store(GrupoClassificacao bean) {
         super.store(bean);
         return bean;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setGrupoClassificacaoDAO(GrupoClassificacaoDAO dao) {
        setDao( dao );
    }
    
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private GrupoClassificacaoDAO getGrupoClassificacaoDAO() {
        return (GrupoClassificacaoDAO) getDao();
    }
   }