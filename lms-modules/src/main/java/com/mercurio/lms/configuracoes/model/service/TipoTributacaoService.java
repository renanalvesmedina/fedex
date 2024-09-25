package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.TipoTributacao;
import com.mercurio.lms.configuracoes.model.dao.TipoTributacaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.tipoTributacaoService"
 */
public class TipoTributacaoService extends CrudService<TipoTributacao, Long> {


	/**
	 * Recupera uma instância de <code>TipoTributacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TipoTributacao findById(java.lang.Long id) {
        return (TipoTributacao)super.findById(id);
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
    public java.io.Serializable store(TipoTributacao bean) {
        return super.store(bean);
    }

    public BigDecimal findDeParaTipoTributacao(String dsTipoTributacaoIcms) {
    	return getTipoTributacaoDAO().findDeParaTipoTributacao(dsTipoTributacaoIcms);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoTributacaoDAO(TipoTributacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoTributacaoDAO getTipoTributacaoDAO() {
        return (TipoTributacaoDAO) getDao();
    }
   }