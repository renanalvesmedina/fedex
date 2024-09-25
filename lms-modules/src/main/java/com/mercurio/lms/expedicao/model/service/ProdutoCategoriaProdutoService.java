package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.ServicoAdicionalColeta;
import com.mercurio.lms.coleta.model.dao.ServicoAdicionalColetaDAO;
import com.mercurio.lms.expedicao.model.ProdutoCategoriaProduto;
import com.mercurio.lms.expedicao.model.dao.ProdutoCategoriaProdutoDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.servicoAdicionalColetaService"
 */
public class ProdutoCategoriaProdutoService extends CrudService<ProdutoCategoriaProduto, Long> {


	/**
	 * Recupera uma instância de <code>ServicoAdicionalColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ProdutoCategoriaProduto findById(java.lang.Long id) {
        return (ProdutoCategoriaProduto)super.findById(id);
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
    public java.io.Serializable store(ProdutoCategoriaProduto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setProdutoCategoriaProdutoDAO(ProdutoCategoriaProdutoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ProdutoCategoriaProdutoDAO getProdutoCategoriaProdutoDAO() {
        return (ProdutoCategoriaProdutoDAO) getDao();
    }
    
    public List findProdutoCategoriaProdutoByIdProduto(Long idProduto) {
        return this.getProdutoCategoriaProdutoDAO().findProdutoCategoriaProdutoByIdProduto(idProduto);
    }
    
    public boolean findProdutoCategoriaProdutoByIdProdutoCdCategoria(Long idProduto, List listCdCategoriaProduto) {
        return this.getProdutoCategoriaProdutoDAO().findProdutoCategoriaProdutoByIdProdutoCdCategoria(idProduto, listCdCategoriaProduto);
    }
	/**
	 * Apaga uma entidade através do Id do Pedido de Coleta.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeByIdProduto(Long idProduto) {
        this.getProdutoCategoriaProdutoDAO().removeByIdProduto(idProduto);
    }
    
}