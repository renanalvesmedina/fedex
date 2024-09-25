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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.produtoService"
 */
public class ProdutoService extends CrudService<Produto, Long> {
    
    private ProdutoCategoriaProdutoService produtoCategoriaProdutoService;
    private NomeProdutoService nomeProdutoService;

	/**
	 * Recupera uma inst�ncia de <code>Produto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
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
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		
		// Se produto for perigoso perigoso, n�o pode ser removido
		if (!getProdutoDAO().findByIdProdutoPerigoso(id)) {		
		super.removeById(id);
		} else {
			// mensagem na tela
			throw new BusinessException("LMS-02092");			
	}
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setProdutoDAO(ProdutoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ProdutoDAO getProdutoDAO() {
		return (ProdutoDAO) getDao();
	}
	
	/**
	 * Obt�m uma lista de produtos por cliente remetente do documento de servico, ordernando pela descri��o do produto.
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
