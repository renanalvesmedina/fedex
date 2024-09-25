package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.dao.TipoPagamPostoPassagemDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.tipoPagamPostoPassagemService"
 */
public class TipoPagamPostoPassagemService extends CrudService<TipoPagamPostoPassagem, Long> {


	/**
	 * Recupera uma instância de <code>TipoPagamentoPostoPassagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TipoPagamPostoPassagem findById(java.lang.Long id) {
        return (TipoPagamPostoPassagem)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    public List find(Map criteria) {
    	List order = new ArrayList(1);
    	order.add("dsTipoPagamPostoPassagem");
    	return getTipoPagamPostoPassagemDAO().findListByCriteria(criteria,order);
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
    public java.io.Serializable store(TipoPagamPostoPassagem bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoPagamPostoPassagemDAO(TipoPagamPostoPassagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoPagamPostoPassagemDAO getTipoPagamPostoPassagemDAO() {
        return (TipoPagamPostoPassagemDAO) getDao();
    }
 
    /**
     * @param idPostoPassagem
     * @return
     */
    public List findFormasPagamentoPostoPassagemCc (Long idPostoPassagem, String tpControleCarga) {
    	List result = getTipoPagamPostoPassagemDAO().findFormasPagamentoPostoPassagemCc(idPostoPassagem, tpControleCarga);
    	result = new AliasToNestedBeanResultTransformer(TipoPagamPostoPassagem.class).transformListResult(result);
    	return result;
    }
}