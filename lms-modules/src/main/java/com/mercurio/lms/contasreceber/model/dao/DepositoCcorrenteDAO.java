package com.mercurio.lms.contasreceber.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DepositoCcorrente;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;
import com.mercurio.lms.contasreceber.model.param.DepositoCcorrenteParam;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DepositoCcorrenteDAO extends BaseCrudDao<DepositoCcorrente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DepositoCcorrente.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("cedente", FetchMode.JOIN);
    	lazyFindById.put("cedente.agenciaBancaria", FetchMode.JOIN);
    	lazyFindById.put("cedente.agenciaBancaria.banco", FetchMode.JOIN);
    	lazyFindById.put("cliente", FetchMode.JOIN);
    	lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
    }
    
    
    public void removeById(Long id) {
    	super.removeById(id, true);
    }
    
    public int removeByIds(List ids) {
    	return super.removeByIds(ids, true);
    }
    
    public ResultSetPage findPaginated(DepositoCcorrenteParam depositoCcorrenteParam, FindDefinition definition) {
    	SqlTemplate hql = mountHql(depositoCcorrenteParam);
    	
    	hql.addProjection("dcc");
    	
    	hql.addOrderBy("ba.nmBanco");
    	hql.addOrderBy("ag.nmAgenciaBancaria");
    	hql.addOrderBy("pe.nmPessoa");
    	hql.addOrderBy("dcc.id");
    	
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(), definition.getCurrentPage(), definition.getPageSize(), hql.getCriteria());
    }

    public Integer getRowCount(DepositoCcorrenteParam depositoCcorrenteParam) {
    	SqlTemplate hql = mountHql(depositoCcorrenteParam);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Retorna um objeto SqlTemplate Padrão montado a partir dos parametros
     * do objeto DepositoCcorrenteParam
     * 
	 * author Mickaël Jalbert
	 * @since 28/03/2006
     * 
	 * @param DepositoCcorrenteParam depositoCcorrenteParam
	 * @return SqlTemplate
     * */
    private SqlTemplate mountHql(DepositoCcorrenteParam depositoCcorrenteParam){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(DepositoCcorrente.class.getName(), "dcc");
    	hql.addInnerJoin("fetch dcc.cedente","ce");
    	hql.addInnerJoin("fetch dcc.cliente","cl");
    	hql.addInnerJoin("fetch cl.pessoa","pe");
    	hql.addInnerJoin("fetch ce.agenciaBancaria","ag");
    	hql.addInnerJoin("fetch ag.banco","ba");
    	
    	hql.addCriteria("cl.id", "=", depositoCcorrenteParam.getIdCliente());
    	hql.addCriteria("dcc.id", "=", depositoCcorrenteParam.getIdDepositoCcorrente());
    	hql.addCriteria("ce.id", "=", depositoCcorrenteParam.getIdCedente());
    	hql.addCriteria("dcc.dtDeposito", ">=", depositoCcorrenteParam.getDtDepositoInicial());
    	hql.addCriteria("dcc.dtDeposito", "<=", depositoCcorrenteParam.getDtDepositoFinal());
    	hql.addCriteria("dcc.tpSituacaoRelacao", "=", depositoCcorrenteParam.getTpSituacaoDeposito());
    	
    	return hql;
    }
    
    
	/**
	 * Método que salva as alterações feitas no mestre e nos detalhes
	 * 
	 * author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param DepositoCcorrente bean
	 * @param ItemList items
	 * @return id gerado para o mestre
	 */  
    public DepositoCcorrente store(DepositoCcorrente bean, ItemList items) {
        super.store(bean);
        removeItemDepositoCcorrente(items.getRemovedItems());
		storeItemDepositoCcorrente(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();    
        return bean;
    }  
    
    /**
     * 
     * 
     * Hector Julian Esnaola Junior
     * 03/03/2008
     *
     * @param bean
     * @return
     *
     * Serializable
     *
     */
    public Serializable store (Serializable bean) {
    	super.store(bean);
    	return bean;
    }
    
	public List findItemDepositoCcorrente(Long idDepositoCcorrente) {
		SqlTemplate hql = mountHqlItemDepositoCcorrente(idDepositoCcorrente);
		
		hql.addProjection("idcc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Integer getRowCountItemDepositoCcorrente(Long idDepositoCcorrente) {
		SqlTemplate hql = mountHqlItemDepositoCcorrente(idDepositoCcorrente);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}
	
    /**
     * Retorna um objeto SqlTemplate para detalhar o filho ItemDepositoCcorrente
     * 
	 * author Mickaël Jalbert
	 * @since 28/03/2006
     * 
	 * @param Long idDepositoCcorrente
	 * @return SqlTemplate
     * */
    private SqlTemplate mountHqlItemDepositoCcorrente(Long idDepositoCcorrente){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(ItemDepositoCcorrente.class.getName(), "idcc");
    	hql.addLeftOuterJoin("fetch idcc.fatura","fat");
    	hql.addLeftOuterJoin("fetch fat.moeda","moe2");
    	hql.addLeftOuterJoin("fetch fat.filialByIdFilial","fil2");
    	hql.addInnerJoin("fetch idcc.depositoCcorrente","dcc");
    	hql.addLeftOuterJoin("fetch idcc.devedorDocServFat","dev");
    	hql.addLeftOuterJoin("fetch dev.doctoServico","doc");
    	hql.addLeftOuterJoin("fetch dev.descontos","des");
    	hql.addLeftOuterJoin("fetch doc.moeda","moe");
    	hql.addLeftOuterJoin("fetch doc.filialByIdFilialOrigem","fil");
    	
    	hql.addCriteria("dcc.id", "=", idDepositoCcorrente);
    	
    	return hql;
    }
    
	public void storeItemDepositoCcorrente(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	public void removeItemDepositoCcorrente(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}    

   


}