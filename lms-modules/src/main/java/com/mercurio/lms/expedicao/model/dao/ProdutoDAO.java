package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NomeProduto;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.ProdutoCategoriaProduto;
import com.mercurio.lms.vendas.model.ProdutoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProdutoDAO extends BaseCrudDao<Produto, Long>{

    private static final String FROM = " from ";
    private static final String CATEGORIA_PRODUTO = "categoriaProduto";
    private static final String CLASSE_RISCO = "classeRisco";
    private static final String NATUREZA_PRODUTO = "naturezaProduto";
    private static final String SUBCLASSE_RISCO = "subClasseRisco";
    private static final String TIPO_PRODUTO = "tipoProduto";
    private static final String DENSIDADE = "densidade";
    
    
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Produto.class;
    }

    protected void initFindByIdLazyProperties(Map arg0) {
        arg0.put(NATUREZA_PRODUTO, FetchMode.JOIN);
        arg0.put(DENSIDADE, FetchMode.JOIN);
        arg0.put(TIPO_PRODUTO, FetchMode.JOIN);
        arg0.put(CLASSE_RISCO, FetchMode.JOIN);
        arg0.put(SUBCLASSE_RISCO, FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map arg0) {
        arg0.put(NATUREZA_PRODUTO, FetchMode.JOIN);
        arg0.put(DENSIDADE, FetchMode.JOIN);
        arg0.put(TIPO_PRODUTO, FetchMode.JOIN);
        arg0.put(CLASSE_RISCO, FetchMode.JOIN);
        arg0.put(SUBCLASSE_RISCO, FetchMode.JOIN); 
    }
    
    protected void initFindLookupLazyProperties(Map lazyFindLookup) {
    	lazyFindLookup.put(NATUREZA_PRODUTO, FetchMode.JOIN);
    	lazyFindLookup.put(DENSIDADE, FetchMode.JOIN);
    	lazyFindLookup.put(TIPO_PRODUTO, FetchMode.JOIN);
    	lazyFindLookup.put(CLASSE_RISCO, FetchMode.JOIN);
    	lazyFindLookup.put(SUBCLASSE_RISCO, FetchMode.JOIN);
    }

    /**
     * Obtém uma lista de produtos por cliente remetente do documento de servico, ordernando pela descrição do produto.
     * @param idDoctoServico
     * @return
     */
    public List findProdutoClienteRemetenteByIdDoctoServico(Long idDoctoServico) {
    	StringBuffer sb = new StringBuffer()
    	.append("select produto ")
    	.append(FROM + DoctoServico.class.getName() + " ds ")
    	.append(" join ds.clienteByIdClienteRemetente cli ")
    	.append(" join cli.produtoClientes pc ")
    	.append(" join pc.produto produto ")
    	.append(" where ds.id = ?")
    	.append(" order by " + OrderVarcharI18n.hqlOrder("produto.dsProduto", LocaleContextHolder.getLocale()));
    	
    	return getAdsmHibernateTemplate()
		    	.getSessionFactory()
		    	.getCurrentSession()
		    	.createQuery(sb.toString())
		    	.setParameter(0, idDoctoServico).list();
    }

	/**
	 * Busca Uma Lista de Produtos a partir de uma lista de ids
	 * 
	 * @param ids
	 * @return
	 */
	public List<Produto> findByIdListProdutosAprovados(Long idCliente, List<Long> idsProdutos) {

    	StringBuilder sb = new StringBuilder()
    	.append("select p ")
    	.append(FROM + ProdutoCliente.class.getName() + " pc ")
    	.append(" inner join pc.produto p ")
    	.append(" where pc.cliente.idCliente = ? ")
    	.append(" and pc.situacaoAprovacao = ? ")
    	.append(" and pc.produto.idProduto in ("+StringUtils.arrayToCommaDelimitedString(idsProdutos.toArray())+") ");
    	
    	return (List<Produto>)getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString())
		    	.setParameter(0, idCliente)
		    	.setParameter(1, "A")
		    	.list();
	}
	
	
	/**
	 * Verifica se algum dos produtos é um produto Perigoso
	 * 
	 * @param ids
	 * @return
	 */
	public Boolean findByIdListProdutoPerigoso(List<Long> idsProdutos) {
		
    	StringBuilder sb = new StringBuilder()
    	.append("select p ")
    	.append(FROM + Produto.class.getName() + " p ")
    	.append(" where p.idProduto in ("+StringUtils.arrayToCommaDelimitedString(idsProdutos.toArray())+") ")
    	.append(" and p.categoria = 'PE'");
    	
    	return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString())
		    	.list().size() > 0;
	}
	
	/**
	 * Verifica se o produto é um produto Perigoso
	 * 
	 * @param ids
	 * @return
	 */
	public Boolean findByIdProdutoPerigoso(Long id) {
		List<Long>idList = new ArrayList<Long>();
		idList.add(id);
    	return this.findByIdListProdutoPerigoso(idList);
	}
	
	public void removeAll(ItemList items){
        removeListNomeProduto(items.getRemovedItems());
        getAdsmHibernateTemplate().flush();
    }
	
	private void removeListNomeProduto(List removeItems) {
        getAdsmHibernateTemplate().deleteAll(removeItems);
    }
	
	public ResultSetPage findPaginatedManterProdutos(Map criteria, FindDefinition findDefinition) {
	    List param = new ArrayList();
        String sql = addSqlByNomeProduto(criteria, param);
        return getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }
    
    public Integer getRowCountManterProdutos(Map criteria, FindDefinition findDefinition) {
        List param = new ArrayList();
        String sql = addSqlByNomeProduto(criteria, param);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }
    
    @SuppressWarnings("unchecked")
    public String addSqlByNomeProduto(Map criteria, List param) {
        StringBuilder sb = new StringBuilder();
        
        if (criteria.get(CATEGORIA_PRODUTO) != null && !"".equals(criteria.get(CATEGORIA_PRODUTO))) {
            String idCategoriaProduto = (String) criteria.get(CATEGORIA_PRODUTO);
            sb.append("select np ")
            .append(FROM + ProdutoCategoriaProduto.class.getName() + " pcp,  NomeProduto np ")
            .append(" join fetch np.produto pd ")
            .append(" left join fetch pd.classeRisco cr ")
            .append(" left join fetch pd.subClasseRisco scr ")
            .append(" left join fetch pd.tipoProduto tp ")
            .append(" left join fetch pd.naturezaProduto ntp ")
            .append(" where pcp.produto = np.produto ")
            .append(" and pcp.categoriaProduto.id = ?  ");
            
            param.add(Long.valueOf(idCategoriaProduto));
        }else{
            sb.append("select np ")
            .append(FROM + NomeProduto.class.getName() + " np ")
            .append(" join fetch np.produto pd ")
            .append(" left join fetch pd.classeRisco cr ")
            .append(" left join fetch pd.subClasseRisco scr ")
            .append(" left join fetch pd.tipoProduto tp ")
            .append(" left join fetch pd.naturezaProduto ntp ");
            sb.append(" where 1=1 ");
        }
        addCriteriaNomeProduto(criteria, param, sb);
        addCriteriaTpSituacao(criteria, param, sb);
        addCriteriaClasseRisco(criteria, param, sb);
        addCriteriaSubClasseRisco(criteria, param, sb);
        addCriteriaTipoProduto(criteria, param, sb);
        addCriteriaNaturezaProduto(criteria, param, sb);
        return sb.toString();
    }

    private void addCriteriaNaturezaProduto(Map criteria, List param, StringBuilder sb) {
        if (criteria.get(NATUREZA_PRODUTO) == null) {
            return;
        }
        String idNaturezaProduto = (String)((Map)criteria.get(NATUREZA_PRODUTO)).get("idNaturezaProduto");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(idNaturezaProduto)) {
            sb.append(" and ntp.id = ? ");
            param.add(Long.valueOf(idNaturezaProduto));
        }
    }

    private void addCriteriaTipoProduto(Map criteria, List param, StringBuilder sb) {
        if (criteria.get(TIPO_PRODUTO) == null) {
            return;
        }
        String idTipoProduto = (String)((Map)criteria.get(TIPO_PRODUTO)).get("idTipoProduto");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(idTipoProduto)) {
            sb.append(" and tp.id = ? ");
            param.add(Long.valueOf(idTipoProduto));
        }
    }

    private void addCriteriaSubClasseRisco(Map criteria, List param, StringBuilder sb) {
        if (criteria.get(SUBCLASSE_RISCO) == null) {
            return;
        }
        String idSubClasseRisco = (String)((Map)criteria.get(SUBCLASSE_RISCO)).get("idSubClasseRisco");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(idSubClasseRisco)) {
            sb.append(" and scr.id = ? ");
            param.add(Long.valueOf(idSubClasseRisco));
        }
    }

    private void addCriteriaTpSituacao(Map criteria, List param, StringBuilder sb) {
        if (criteria.get("tpSituacao") == null) {
            return;
        }
        String tpSituacao = (String)criteria.get("tpSituacao");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(tpSituacao)){
            sb.append(" and pd.tpSituacao = ? ");
            param.add(tpSituacao);
        }
    }

    private void addCriteriaNomeProduto(Map criteria, List param, StringBuilder sb) {
        if (criteria.get("dsNomeProduto") == null) {
            return;   
        }    
        String dsNomeProduto = (String)criteria.get("dsNomeProduto");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(dsNomeProduto)){
            sb.append(" and lower(np.dsNomeProduto) like ? ");
            param.add(dsNomeProduto.toLowerCase());
        }
    }

    private void addCriteriaClasseRisco(Map criteria, List param, StringBuilder sb) {
        if (criteria.get(CLASSE_RISCO) == null) {
            return;
        }
        String idClasseRisco = (String)((Map)criteria.get(CLASSE_RISCO)).get("idClasseRisco");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(idClasseRisco)) {
            sb.append(" and cr.id = ? ");
            param.add(Long.valueOf(idClasseRisco));
        }
    }
    
    public Produto storeAll(Produto produto) {
        super.store(produto);
        super.store(produto.getNomeProdutos());
        super.store(produto.getProdutoCategoriaProdutos());
        
        getAdsmHibernateTemplate().flush();
        return produto;
    }     
}