package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Cheque;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ChequeDAO extends BaseCrudDao<Cheque, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Cheque.class;
	}
	
	/**
     * Método que busca os cheques de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginated(TypedFlatMap criteria){

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
    	Long idLoteCheque = criteria.getLong("loteCheque.idLoteCheque");
    	Long idFilial = criteria.getLong("filial.idFilial");
    	Long idCliente = criteria.getLong("cliente.idCliente");
    	Long nrBanco = criteria.getLong("nrBanco");
    	Long nrChequeInicial = criteria.getLong("nrChequeInicial");
    	Long nrChequeFinal = criteria.getLong("nrChequeFinal");
    	Long nrAgencia = criteria.getLong("nrAgencia");
    	Long nrContaCorrente = criteria.getLong("nrContaCorrente");
    	Long tpSituacaoCheque = criteria.getLong("tpSituacaoCheque");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( c.loteCheque.nrLoteCheque as loteCheque_nrLoteCheque, ")
    				.append(" filial.sgFilial as filial_sgFilial, ")
    				.append(" cliente.pessoa.nmFantasia as cliente_pessoa_nmFantasia, ")
    				.append(" c.nrBanco as nrBanco, ")
    				.append(" c.nrAgencia as nrAgencia, ")
    				.append(" c.idCheque as idCheque, ")
    				.append(" c.nrCheque as nrCheque, ")
    				.append(" c.vlCheque as vlCheque, ")
    				.append(" c.nrContaCorrente as nrContaCorrente, ")
    				.append(" c.tpSituacaoCheque as tpSituacaoCheque) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName(), " c " +
    			" left join c.filial filial " +
    			" left join c.cliente cliente " +
    			" left join cliente.pessoa pessoa");
    	
    	sql.addCriteria("c.loteCheque.idLoteCheque", "=", idLoteCheque);
		sql.addCriteria("filial.idFilial", "=", idFilial);
		sql.addCriteria("cliente.idCliente", "=", idCliente);
    	sql.addCriteria("c.nrBanco", "=", nrBanco);
    	sql.addCriteria("c.nrAgencia", "=", nrAgencia);
    	sql.addCriteria("c.nrContaCorrente", "=", nrContaCorrente);
		sql.addCriteria("c.tpSituacaoCheque", "=", tpSituacaoCheque);
		
		sql.addCriteria("c.nrCheque", ">=", nrChequeInicial);
		sql.addCriteria("c.nrCheque", "<=", nrChequeFinal);
		
		sql.addOrderBy("c.loteCheque.idLoteCheque, c.nrBanco, c.nrAgencia, c.nrContaCorrente, c.nrCheque");
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
    							sql.getSql(), 
    							findDef.getCurrentPage(), findDef.getPageSize(), 
    							sql.getCriteria()
    						);

    	List list = rsp.getList();
    	list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
    	rsp.setList(list);
    	
    	return rsp;
	}
	
    /**
     * Método que retorna o número de registros de acordo com os filtros passados
     * @param criteria
     * @return Integer quantidade de registros encontrados
     */
	public Integer getRowCount(TypedFlatMap criteria){
    	Long idLoteCheque = criteria.getLong("loteCheque.idLoteCheque");
    	Long idFilial = criteria.getLong("filial.idFilial");
    	Long idCliente = criteria.getLong("cliente.idCliente");
    	Long nrChequeInicial = criteria.getLong("nrChequeInicial");
    	Long nrChequeFinal = criteria.getLong("nrChequeFinal");
    	Long nrBanco = criteria.getLong("nrBanco");
    	Long nrAgencia = criteria.getLong("nrAgencia");
    	Long nrContaCorrente = criteria.getLong("nrContaCorrente");
    	Long tpSituacaoCheque = criteria.getLong("tpSituacaoCheque");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("count(c.idCheque) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName(), " c " +
    			" left join c.filial filial " +
    			" left join c.cliente cliente " +
    			" left join cliente.pessoa pessoa");
    	
    	sql.addCriteria("c.loteCheque.idLoteCheque", "=", idLoteCheque);
		sql.addCriteria("filial.idFilial", "=", idFilial);
		sql.addCriteria("cliente.idCliente", "=", idCliente);
    	sql.addCriteria("c.nrBanco", "=", nrBanco);
    	sql.addCriteria("c.nrAgencia", "=", nrAgencia);
    	sql.addCriteria("c.nrContaCorrente", "=", nrContaCorrente);
		sql.addCriteria("c.tpSituacaoCheque", "=", tpSituacaoCheque);
		
		sql.addCriteria("c.nrCheque", ">=", nrChequeInicial);
		sql.addCriteria("c.nrCheque", "<=", nrChequeFinal);
		
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    	return result.intValue();
	}
	
	/**
	 * Pesquisa de cheques pelo id do cheque 
	 * @param Long idCheque
	 * @return Cheque selecionado 
	 */
	public Cheque findById(Long idCheque){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map( c.nrCheque as cheque_idCheque, c.idCheque as cheque_idCheque )");
    	
    	sql.addFrom(getPersistentClass().getName(), " c ");
    	
    	sql.addCriteria("c.idCheque", "=", idCheque);
		
    	List list = getAdsmHibernateTemplate().find(
    							sql.getSql(), 
    							sql.getCriteria()
    						);

    	list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
    	
    	return (Cheque) list.get(0);
	}	
	
	/**
	 * Pesquisa de cheques pelo id do cheque 
	 * @param Long idCheque
	 * @return Cheque selecionado 
	 */
	public Cheque findChequeById(Long idCheque){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(" c ");
    	
    	sql.addFrom(getPersistentClass().getName(), " c ");
    	
    	sql.addCriteria("c.idCheque", "=", idCheque);
		
    	List list = getAdsmHibernateTemplate().find(
    							sql.getSql(), 
    							sql.getCriteria()
    						);

    	return (Cheque) list.get(0);
	}	
	
	/**
	 * Pesquisa a situação do último histórico do cheque
	 * @param Long idCheque
	 * @return String situação do último histórico do cheque
	 */
	public String findHistoricoChequeByIdCheque(Long idCheque){
		SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("hist as tp_historico_cheque from (" +
			    			"select tp_historico_cheque as hist from historico_cheque "+
			    			"  where id_cheque = " + idCheque +
			    			"  order by id_historico_cheque desc "+
			    			") where rownum = 1");

    	Object obj = getAdsmHibernateTemplate().findByIdBySql(
				sql.getSql(), 
				sql.getCriteria(),
				new ConfigureSqlQuery() {
					public void configQuery(SQLQuery sqlQuery) {
						sqlQuery.addScalar("tp_historico_cheque", Hibernate.STRING);
					}
				}
			);

    	if (obj == null)
    		return null;
   		else 
   			return obj.toString();    	
	}	

	public Map findCheque(TypedFlatMap criteria){

    	Long idFilial = criteria.getLong("filial.idFilial");
    	Long nrCheque = criteria.getLong("nrCheque");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( c.idCheque as cheque_idCheque, ")
    				.append(" c.nrCheque as nrCheque) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName(), " c " +
    			" left join c.filial filial " +
    			" left join c.cliente cliente " +
    			" left join cliente.pessoa pessoa");
    	
		sql.addCriteria("filial.idFilial", "=", idFilial);
		sql.addCriteria("c.nrCheque", "=", nrCheque);
		
		sql.addOrderBy("c.nrCheque");
    	
    	List list = getAdsmHibernateTemplate().find(
						sql.getSql(), 
						sql.getCriteria()
    				); 

    	list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
    	
    	return (Map) list.get(0);
	}
	
	/**
	 * Retorna a lista de cheque por Lote e Situação.
	 * 
	 * @author Mickaël Jalbert
	 * 22/03/2006
	 * 
	 * @param Long idLoteCheque
	 * @param String tpSituacaoCheque
	 * */
	public List findChequeByLoteSituacao(Long idLoteCheque, String tpSituacaoCheque){
		return this.findByCriterios(idLoteCheque, null, tpSituacaoCheque, null, null);
	}
	
	/**
	 * Retorna a lista de cheque por filtros informados.
	 * Alterado por José Rodrigo Moraes em 11/07/2006
	 * @author Mickaël Jalbert
	 * @since  22/03/2006
	 * 
	 * @param Long idLoteCheque
	 * @param Long idCheque
	 * @param String tpSituacaoCheque
	 * */
	public List findByCriterios(Long idLoteCheque, Long idCheque, String tpSituacaoCheque, Long nrCheque, Long idFilial){
		SqlTemplate hql = mountSqlTemplate(idLoteCheque, idCheque, tpSituacaoCheque, nrCheque, idFilial);
		hql.addProjection("ch");
		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	private SqlTemplate mountSqlTemplate(Long idLoteCheque, Long idCheque, String tpSituacaoCheque, Long nrCheque, Long idFilial){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(Cheque.class.getName(),"ch");		
		hql.addInnerJoin("fetch ch.loteCheque", "lc");
		hql.addLeftOuterJoin("fetch ch.cheque", "chSubstituido");
		hql.addInnerJoin("fetch ch.filial", "f");
		hql.addInnerJoin("fetch f.pessoa","fp");
				
		hql.addCriteria("lc.id","=",idLoteCheque);
		hql.addCriteria("ch.id","=",idCheque);
		hql.addCriteria("ch.tpSituacaoCheque","=",tpSituacaoCheque);
		hql.addCriteria("f.id","=",idFilial);
		hql.addCriteria("ch.nrCheque","=",nrCheque);
		
		return hql;
	}
	
}