package com.mercurio.lms.contasreceber.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TransferenciaDAO extends BaseCrudDao<Transferencia, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Transferencia.class;
	}

	/**
	 * Verifica se a dhTransmissao da Transferencia é NULL.<BR>
	 * @param idTransferencia
	 * @return true se não for NULL; false, se for.
	 */
	public boolean validateDhTransmissaoIsNull(Long idTransferencia){
		if (idTransferencia == null) return false;
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(getPersistentClass().getName(), " t ");
		sql.addProjection(" count(t.id) ");
		sql.addCriteria("t.id", "=", idTransferencia);
		sql.addCustomCriteria("t.dhTransmissao.value is null");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() != 0);
	}

	/**
	 * Count de Itens da Transferencia.<BR>
	 * @param idTransferencia
	 * @param count
	 * @return
	 */
	public Integer findCountItens(Long idTransferencia){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(getPersistentClass().getName(), "t inner join t.itemTransferencias as itens ");
		sql.addProjection(" count(itens.id) ");
		sql.addCriteria("t.id","=", idTransferencia);
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	private SqlTemplate mountItensByTransferencia(Map parameters){
		TypedFlatMap map = (TypedFlatMap) parameters;
		SqlTemplate sql = new SqlTemplate();
		sql.addInnerJoin(ItemTransferencia.class.getName(), "item");
		sql.addInnerJoin("item.transferencia", "tra");
		sql.addInnerJoin("item.motivoTransferencia","mot");
		sql.addInnerJoin("item.clienteByIdNovoResponsavel", "cli");
		sql.addInnerJoin("cli.pessoa", "pes");
		sql.addInnerJoin("item.devedorDocServFat", "dev");
		sql.addInnerJoin("dev.doctoServico", "doc");
		sql.addInnerJoin("doc.filialByIdFilialOrigem", "fil");
		sql.addInnerJoin("doc.devedorDocServFats", "docsf");

		sql.addCriteria("tra.id", "=", map.getLong("idTransferencia"));
		return sql;
	}
	
	public ResultSetPage findItensByTransferencia(Map parameters, FindDefinition findDef){
		SqlTemplate sql = mountItensByTransferencia(parameters);
		sql.addProjection(" new Map( item.id as idItemTransferencia, " +
				" mot.dsMotivoTransferencia as motivoTransferencia_dsMotivoTransferencia, " +
				" pes.nmPessoa as novoResponsavel_nmPessoa, " +
				" doc.tpDocumentoServico as tpDocumentoServico, " +
				" doc.id as idDoctoServico, " +
				" fil.sgFilial as sgFilialOrigem, " +
				" doc.nrDoctoServico as nrDoctoServico, " +
				" docsf.vlDevido as vlrFrete " +
				") ");
		
		
		sql.addOrderBy("doc.tpDocumentoServico");
		sql.addOrderBy("fil.sgFilial");
		sql.addOrderBy("doc.nrDoctoServico");
		
		ResultSetPage rs = getAdsmHibernateTemplate()
			.findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		rs.setList( AliasToNestedMapResultTransformer.getInstance().transformListResult(rs.getList()) );
		return rs;
	}

	public Integer getRowCountFromItensByTransferencia(Map parameters){
		SqlTemplate sql = mountItensByTransferencia(parameters);
		sql.addProjection(" count(item.id) ");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	public java.io.Serializable findByIdWithFiliais(Long id) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(t.id as idTransferencia, " +

				"filOri.id as filialByIdFilialOrigem_idFilial, " +
				"filOri.sgFilial as filialByIdFilialOrigem_sgFilial, " +
				"pesOri.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, " +
				
				"filDes.id as filialByIdFilialDestino_idFilial, " +				
				"filDes.sgFilial as filialByIdFilialDestino_sgFilial, " +
				"pesDes.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, " +
				
				" t.dtEmissao as dtEmissao, " +
				" t.tpOrigem as tpOrigem, " +
				" t.tpSituacaoTransferencia as tpSituacaoTransferencia, " +
				" t.dtRecebimento as dtRecebimento, " +
				" t.dhTransmissao as dhTransmissao, " +
				
				"t.nrTransferencia as nrTransferencia, " +
				" t.dtEmissao as dtEmissao, t.tpOrigem as tpOrigem )");

		sql.addInnerJoin(Transferencia.class.getName(), "t");
		sql.addInnerJoin("t.filialByIdFilialOrigem", "filOri");
		sql.addInnerJoin("filOri.pessoa", "pesOri");
		sql.addInnerJoin("t.filialByIdFilialDestino", "filDes");
		sql.addInnerJoin("filDes.pessoa", "pesDes");
		
		
		
		sql.addCriteria("t.id","=",id);

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
		if (list != null){
			return (Serializable) AliasToNestedMapResultTransformer.getInstance().transformListResult(list).get(0);
		}

		return null;
	}

	private SqlTemplate mountSqlTransferenciaDebito(TypedFlatMap map){
		SqlTemplate sql = new SqlTemplate();
		sql.addInnerJoin(Transferencia.class.getName(), "t");
		sql.addInnerJoin("t.filialByIdFilialOrigem", "filOri");
		sql.addInnerJoin("filOri.pessoa", "pesOri");
		sql.addInnerJoin("t.filialByIdFilialDestino", "filDes");
		sql.addInnerJoin("filDes.pessoa", "pesDes");
		
		sql.addCriteria("filOri.id","=",map.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("filDes.id","=",map.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("t.nrTransferencia","=",map.getLong("nrTransferencia"));
		sql.addCriteria("t.dtEmissao","=",map.getYearMonthDay("dtEmissao"));
		sql.addCriteria("t.tpOrigem","=",map.getString("tpOrigem"));
		sql.addCriteria("t.tpSituacaoTransferencia","=",map.getString("tpSituacaoTransferencia"));
		return sql;
	}

	public ResultSetPage findPaginatedTransferenciaDebito(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = mountSqlTransferenciaDebito((TypedFlatMap) criteria);
		sql.addProjection("new Map(t.id as idTransferencia, " +
				"pesOri.nmFantasia as filialOrigem, " +
				"filOri.sgFilial as sgFilialOrigem, " +
				"pesDes.nmFantasia as filialDestino, " +
				"filDes.sgFilial as sgFilialDestino, " +
				" t.nrTransferencia as nrTransferencia, " +
				" t.dtEmissao as dtEmissao, t.tpOrigem as tpOrigem, " +
				" t.tpSituacaoTransferencia as tpSituacaoTransferencia" +
				" )");
		sql.addOrderBy("t.filialByIdFilialOrigem.sgFilial, t.nrTransferencia");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

	public Integer getRowCountTransferenciaDebito(Map criteria){
		SqlTemplate sql = mountSqlTransferenciaDebito((TypedFlatMap) criteria);
		sql.addProjection("count (t.id)");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Retorna a lista de transferancia, com os itens, a partir do id do devedor.
	 * 
	 * @author Mickaël Jalbert
	 * 06/03/2006
	 * 
	 * @param Long idDevedor
	 * @return List
	 */
	public List findByIdDevedorDocServFat(Long idDevedor){
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(Transferencia.class.getName(),"tra");
		hql.addInnerJoin("tra.itemTransferencias", "itra");
		hql.addInnerJoin("itra.devedorDocServFat", "dev");
		hql.addCriteria("dev.idDevedorDocServFat","=",idDevedor);
		hql.addCriteria("tra.tpSituacaoTransferencia", "=", "PR");

		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna a lista de transferancia de acordo com o idDoctoServico e o tpSituacaoTransferencia
	 * 
	 * @author Hector Julian Esnaola Junior
	 * 29/06/2006
	 * 
	 * @param Long idDoctoServico
	 * @param String tpSituacaoTransferencia
	 *  
	 * @return List
	 * */
	public List findByIdDoctoServicoAndTpSituacaoTransferencia(Long idDoctoServico, String tpSituacaoTransferencia){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("t");

		hql.addFrom(Transferencia.class.getName()," t " +
					" JOIN t.itemTransferencias it " +
					" JOIN it.devedorDocServFat ddsf " +
					" JOIN ddsf.doctoServico ds " +
					" JOIN FETCH t.filialByIdFilialOrigem fo ");

		hql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);
		hql.addCriteria("t.tpSituacaoTransferencia", "=", tpSituacaoTransferencia);

		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * 
	 * @param idItemTransferencia
	 * @return Map
	 */
	public Map findDadosTransferencia(Long idItemTransferencia){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map( fo.sgFilial as filialOrigem, " +
									"pfo.nmFantasia as filialOrigemNome," +
									"t.nrTransferencia as nrTransferencia," +
									"fd.sgFilial as filialDestino," +
									"pfd.nmFantasia as filialDestinoNome," +
									"t.tpSituacaoTransferencia as tpSituacaoTransferencia," +
									"t.dtEmissao as dtEmissao," +
									"pn.nrIdentificacao as identificacaoNova," +
									"pn.nmPessoa as nmPessoaNova," +
									"pa.nrIdentificacao as identificacaoAntiga," +
									"pa.nmPessoa as nmPessoaAntiga," +
									"m.dsMotivoTransferencia as dsMotivoTransferencia," +
									"it.obItemTransferencia as obItemTransferencia )");

		hql.addFrom(ItemTransferencia.class.getName() + " it " +
				"inner join it.transferencia t " +
				"inner join t.filialByIdFilialOrigem fo " +
				"inner join fo.pessoa as pfo " +
				"inner join t.filialByIdFilialDestino fd " +
				"inner join fd.pessoa as pfd " +
				"inner join it.clienteByIdNovoResponsavel cn " +
				"inner join it.clienteByIdAntigoResponsavel ca " +
				"inner join it.motivoTransferencia m " +
				"inner join cn.pessoa pn " +
				"inner join ca.pessoa pa");

		hql.addCriteria("it.idItemTransferencia", "=", idItemTransferencia);

		return (Map)(getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria())).get(0);
	}

	/**
	 * Retorna a lista de transferancia de acordo com a tpSituacaoTransferencia
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 16/05/2007
	 * 
	 * @param Long nrTransferencia
	 * @param Long idFilialOrigem
	 *  
	 * @return List
	 * 
	 */
	public Transferencia findByNrTransfereciaAndIdFilialOrigem(Long nrTransferencia, Long idFilialOrigem){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("tra");
		hql.addInnerJoin(Transferencia.class.getName(),"tra");
		hql.addInnerJoin("tra.itemTransferencias", "itra");
		hql.addCriteria("tra.nrTransferencia", "=", nrTransferencia);
		hql.addCriteria("tra.filialByIdFilialOrigem.id", "=", idFilialOrigem);
		hql.addOrderBy("tra.dtEmissao");

		return (Transferencia)this.getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

}