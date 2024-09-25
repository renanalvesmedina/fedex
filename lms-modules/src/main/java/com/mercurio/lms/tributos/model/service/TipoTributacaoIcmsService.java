package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.tributos.model.dao.TipoTributacaoIcmsDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.tipoTributacaoIcmsService"
 */
public class TipoTributacaoIcmsService extends CrudService<TipoTributacaoIcms, Long> {


	/**
	 * Recupera uma instância de <code>TipoTributacaoIcms</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TipoTributacaoIcms findById(java.lang.Long id) {
        return (TipoTributacaoIcms)super.findById(id);
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
    public java.io.Serializable store(TipoTributacaoIcms bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoTributacaoIcmsDAO(TipoTributacaoIcmsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoTributacaoIcmsDAO getTipoTributacaoIcmsDAO() {
        return (TipoTributacaoIcmsDAO) getDao();
    }
    
    /**
     * Método responsável por popular a combo de TipoTributacaoICMS
     * 
     * @param notInIdsParametrosGerais
     * @return List de TipoTributacaoICMS
     */
    public List findComboTipoTributacaoIcms(List notInIdsParametrosGerais, String onlyActiveValues, Long idTipoTributacao){
		return getTipoTributacaoIcmsDAO().findComboTipoTributacaoIcms(notInIdsParametrosGerais, onlyActiveValues, idTipoTributacao);
	}
 
    /**
     * Busca uma entidade TipoTributacaoIcms de acordo com o campo dsTipoTributacaoIcms
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param dsTipoTributacaoIcms
     * @return
     *
     */
    public TipoTributacaoIcms findTipoTributacaoIcmsByDsTipoTributacaoIcms( String dsTipoTributacaoIcms ){
    	return getTipoTributacaoIcmsDAO().findTipoTributacaoIcmsByDsTipoTributacaoIcms(dsTipoTributacaoIcms);
    }

}