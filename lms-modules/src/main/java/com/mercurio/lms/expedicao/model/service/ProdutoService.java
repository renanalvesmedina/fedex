package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CategoriaProduto;
import com.mercurio.lms.expedicao.model.NomeProduto;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.ProdutoCategoriaProduto;
import com.mercurio.lms.expedicao.model.dao.ProdutoDAO;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.produtoService"
 */
public class ProdutoService extends CrudService<Produto, Long> {
    
    private ProdutoCategoriaProdutoService produtoCategoriaProdutoService;
    private NomeProdutoService nomeProdutoService;

	/**
	 * Recupera uma instância de <code>Produto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Produto findById(java.lang.Long id) {
		return (Produto)super.findById(id);
	}

	public List find(Map criteria) {
		List<String> orderBy = new ArrayList<String>(1);
		orderBy.add("dsProduto");
		return this.getProdutoDAO().findListByCriteria(criteria, orderBy);   
	}


	/**
	 * Busca Uma Lista de Produtos a partir de uma lista de ids
	 * 
	 * @param ids
	 * @return
	 */	
	public List<Produto> findByIdListProdutosAprovados(Long idCliente, List<Long> idsProdutos) {
		return getProdutoDAO().findByIdListProdutosAprovados(idCliente, idsProdutos);
	}	


	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		
		// Se produto for perigoso perigoso, não pode ser removido
		if (!getProdutoDAO().findByIdProdutoPerigoso(id)) {		
		super.removeById(id);
		} else {
			// mensagem na tela
			throw new BusinessException("LMS-02092");			
	}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	public Produto beforeStore(Produto bean){
		Empresa e = (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);   
		bean.setEmpresa(e);
		return super.beforeStore(bean);	
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(Produto bean) {
		return super.store(bean);
	}

    public void storeAll(Produto bean, TypedFlatMap tfmBean, ItemList items, ItemListConfig config) {
        boolean rollbackMasterId = bean.getIdProduto() == null;
        try {
            if (items == null || !items.hasItems()) {
               throw new BusinessException("LMS-04573");
            }

            this.getProdutoDAO().removeAll(items);
            
            if(bean.getIdProduto()!= null){
                produtoCategoriaProdutoService.removeByIdProduto(bean.getIdProduto());
                bean.getProdutoCategoriaProdutos().clear();
            }
            
            bean.setNomeProdutos(new ArrayList<NomeProduto>());
            for (Iterator iter = items.iterator(bean.getIdProduto(), config); iter.hasNext(); ) {
                NomeProduto nomeProduto = (NomeProduto) iter.next();
                if(nomeProdutoService.findBydsNomeProduto(bean.getIdProduto(), nomeProduto.getDsNomeProduto())){
                    throw new BusinessException("LMS-04574");
                }
                
                if(nomeProduto.getIdNomeProduto()!= null && nomeProduto.getIdNomeProduto() < 0){
                    nomeProduto.setIdNomeProduto(null);
                }
                nomeProduto.setProduto(bean);
                bean.getNomeProdutos().add(nomeProduto);
            }

            List<TypedFlatMap> listMapsProdutoCategoriaProdutos = tfmBean.getList("produtoCategoriaProdutos");
            bean.setProdutoCategoriaProdutos(createListProdutoCategoriaProduto(bean, listMapsProdutoCategoriaProdutos));
            
            Empresa e = (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);   
            bean.setEmpresa(e);
            
            getProdutoDAO().storeAll(bean);
        }catch (BusinessException e) {
            if ("LMS-04574".equals(((BusinessException) e).getMessageKey())) {
                throw e;
            }
        } catch (RuntimeException e) {
            this.rollbackMasterState(bean, rollbackMasterId, e);
            if(items!= null){
                items.rollbackItemsState();
            }
        }
    }
    
    private List<ProdutoCategoriaProduto> createListProdutoCategoriaProduto(Produto bean, List<TypedFlatMap> listMapsProdutoCategoriaProdutos) {
        List<ProdutoCategoriaProduto> listProdutoCategoriaProdutos = new ArrayList<ProdutoCategoriaProduto>();
        for (TypedFlatMap typedFlatMap : listMapsProdutoCategoriaProdutos) {
            ProdutoCategoriaProduto produtoCategoriaProduto = new ProdutoCategoriaProduto();
            
            CategoriaProduto categoriaProduto = new CategoriaProduto();
            categoriaProduto.setIdCategoriaProduto(typedFlatMap.getLong("idCategoriaProduto"));
            
            if(typedFlatMap.getLong("idProdutoCategoriaProduto")!= null){
                produtoCategoriaProduto.setIdProdutoCategoriaProduto(typedFlatMap.getLong("idProdutoCategoriaProduto"));
            }
            produtoCategoriaProduto.setIdProdutoCategoriaProduto(null);
            produtoCategoriaProduto.setCategoriaProduto(categoriaProduto);
            produtoCategoriaProduto.setProduto(bean);
            
            listProdutoCategoriaProdutos.add(produtoCategoriaProduto);
        }
        return listProdutoCategoriaProdutos;
    }
    
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setProdutoDAO(ProdutoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ProdutoDAO getProdutoDAO() {
		return (ProdutoDAO) getDao();
	}
	
	/**
	 * Obtém uma lista de produtos por cliente remetente do documento de servico, ordernando pela descrição do produto.
	 * @param idDoctoServico
	 * @return
	 */
	public List<Produto> findProdutoClienteRemetenteByIdDoctoServico(Long idDoctoServico) {
		return getProdutoDAO().findProdutoClienteRemetenteByIdDoctoServico(idDoctoServico);
	}
	
	public ResultSetPage findPaginated(Map criteria) {
        return this.getProdutoDAO().findPaginatedManterProdutos(criteria, FindDefinition.createFindDefinition(criteria));
    }

    public Integer getRowCount(Map criteria) {
        return this.getProdutoDAO().getRowCountManterProdutos(criteria, FindDefinition.createFindDefinition(criteria));
    }

    public void setProdutoCategoriaProdutoService(
            ProdutoCategoriaProdutoService produtoCategoriaProdutoService) {
        this.produtoCategoriaProdutoService = produtoCategoriaProdutoService;
    }

    public void setNomeProdutoService(NomeProdutoService nomeProdutoService) {
        this.nomeProdutoService = nomeProdutoService;
    }
    
}
