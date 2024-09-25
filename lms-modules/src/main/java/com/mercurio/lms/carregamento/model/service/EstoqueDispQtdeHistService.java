package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.EstoqueDispQtdeHist;
import com.mercurio.lms.carregamento.model.dao.EstoqueDispQtdeHistDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.estoqueDispQtdeHistService"
 */
public class EstoqueDispQtdeHistService extends CrudService<EstoqueDispQtdeHist, Long> {

    /**
	 * Recupera uma instância de <code>EstoqueDispositivoQtde</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EstoqueDispQtdeHist findById(java.lang.Long id) {
        return (EstoqueDispQtdeHist)super.findById(id);
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
    public EstoqueDispQtdeHist store(EstoqueDispQtdeHist bean) {
        Long serializable = (Long) super.store(bean);
		return findById(serializable);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEstoqueDispQtdeHistDAO(EstoqueDispQtdeHistDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EstoqueDispQtdeHistDAO getEstoqueDispQtdeHistDAO() {
        return (EstoqueDispQtdeHistDAO) getDao();
    }

    

}