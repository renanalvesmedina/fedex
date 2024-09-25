package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemTransferenciaDAO extends BaseCrudDao<ItemTransferencia, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemTransferencia.class;
    }

	/**
	 * Busca as transfer�ncias que possuam itens de transfer�ncia e estejam pendentes de recebimento
	 * @param tfm Par�metros de pesquisa Pode ser passado o id do DevedorDocServFat pra filtrar por devedor
	 * @return Lista de transfer�ncias
	 */
    public List findTransferenciasPendentesWithItemTransferencia(TypedFlatMap tfm) {
    	
    	SqlTemplate sqlTemplate = new SqlTemplate();
    	
    	sqlTemplate.addProjection("distinct t");
    	
    	sqlTemplate.addInnerJoin(Transferencia.class.getName(), "t");
    	sqlTemplate.addInnerJoin("t.itemTransferencias", "it");
    	
    	sqlTemplate.addCriteria("t.tpSituacaoTransferencia","=", String.valueOf("PR"));
    	
    	if (tfm!= null) {
    		sqlTemplate.addCriteria("it.devedorDocServFat.id","=", tfm.getLong("idDevedorDocServFat"));
    	}
    	
		return this.getAdsmHibernateTemplate().find(sqlTemplate.getSql(),sqlTemplate.getCriteria());
		
	}

   /**
    * Busca todas as Transfer�ncias de um determinado Devedor
    * @param idDevedorDocServFat Identificador do Devedor
    * @return Lista de Transfer�ncias desse devedor
    */
    public List findTransferenciasByDevedorDocServFat(Long idDevedorDocServFat){
	   
	   SqlTemplate sqlTemplate = new SqlTemplate();
   	
	   sqlTemplate.addProjection("t");
	   
	   sqlTemplate.addFrom(ItemTransferencia.class.getName() + " as it " +
	   					   "	inner join it.transferencia as t ");
	   
	   sqlTemplate.addCriteria("it.devedorDocServFat","=", idDevedorDocServFat);
	   
	   return this.getAdsmHibernateTemplate().find(sqlTemplate.getSql(),sqlTemplate.getCriteria());
	   
	   
   }
    
    /**
     * Retorna o n�mero de transferencia que tem o devedor informado com a situacao de transferencia informada.
     * 
     * @author Micka�l Jalbert
     * @since 11/01/2007
     * 
     * @param idDevedorDocServFat
     * @param tpSituacaoTransferencia
     * @return
     */
	public Integer findRowCountByDevedorDocServFatTpSituacao(Long idDevedorDocServFat, String tpSituacaoTransferencia){

		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addProjection("count(distinct t.id)");
		sqlTemplate.addFrom(ItemTransferencia.class.getName() + " as it " +
			"	inner join it.transferencia as t ");

		sqlTemplate.addCriteria("t.tpSituacaoTransferencia", "=", tpSituacaoTransferencia);
		sqlTemplate.addCriteria("it.devedorDocServFat.id","=", idDevedorDocServFat);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sqlTemplate.getSql(),sqlTemplate.getCriteria());
		return result.intValue();
	}

    /**
     * 
     * @param idItemTransferencia
     * @return Map
     */
    public List findItemTransferenciaByDevedorDocServFat(Long idDevedorDocServFat){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map( (fo.sgFilial || ' - ' || pfo.nmPessoa) as filialOrigem, " +
    								"t.nrTransferencia as nrTransferencia, " +
    								"t.idTransferencia as idTransferencia, " +
    								"(fd.sgFilial || ' - ' || pfd.nmPessoa) as filialDestino, " +
    								"pn.nrIdentificacao as nrIdentificacaoNova, " +
    								"pn.tpIdentificacao as tpIdentificacaoNova, " +
    								"pn.nmPessoa as nmPessoaNova, " +
    								"it.idItemTransferencia as idItemTransferencia )");
    	
    	
    	hql.addFrom(ItemTransferencia.class.getName() + " it " +
    			"inner join it.transferencia t " +
    			"inner join it.devedorDocServFat ddsf " +
    			"inner join t.filialByIdFilialOrigem fo " +
    			"inner join t.filialByIdFilialDestino fd " +
    			"inner join it.clienteByIdNovoResponsavel cn " +
    			"inner join it.clienteByIdAntigoResponsavel ca " +
    			"inner join it.motivoTransferencia m " +
    			"inner join cn.pessoa pn " +
    			"inner join ca.pessoa pa " +
    			"inner join fo.pessoa pfo " +
    			"inner join fd.pessoa pfd ");
    	
    	hql.addCriteria("ddsf.idDevedorDocServFat", "=", idDevedorDocServFat);
    	
    	hql.addOrderBy("fo.sgFilial");
    	hql.addOrderBy("t.nrTransferencia");
    	hql.addOrderBy("fd.sgFilial");
    	hql.addOrderBy("pn.nrIdentificacao");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Exclui os elementos da List
     *
     * @author Hector Julian Esnaola Junior
     * @since 21/05/2007
     *
     */
    public void removeItensTransferencia(List<ItemTransferencia> itensTransferencia){
    	getAdsmHibernateTemplate().deleteAll(itensTransferencia);
    	getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
    }

}