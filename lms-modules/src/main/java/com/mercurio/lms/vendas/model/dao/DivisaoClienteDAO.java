package com.mercurio.lms.vendas.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DivisaoClienteDAO extends BaseCrudDao<DivisaoCliente, Long> {

	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("naturezaProduto", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return DivisaoCliente.class;
	}

	public List findByIdCliente(Long idCliente, String tpSituacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("dc.idDivisaoCliente"), "idDivisaoCliente")
			.add(Projections.property("dc.dsDivisaoCliente"), "dsDivisaoCliente");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc")
			.setProjection(pl)
			.add(Restrictions.eq("dc.cliente.id", idCliente))
			.add(Restrictions.eq("dc.tpSituacao", tpSituacao))
			.addOrder(Order.asc("dc.dsDivisaoCliente"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	 public PrazoVencimento findPrazoVencimentoByIdDivisao(Long idDivisaoCliente) {
	    	DetachedCriteria dc = DetachedCriteria.forClass(PrazoVencimento.class, "pv");
	    	dc.add(Restrictions.eq("pv.divisaoCliente.id", idDivisaoCliente));
	    	ResultSetPage rsp = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE);
	    	if(rsp.getList().isEmpty()) {
	    		return null;
	    	}
	    	return (PrazoVencimento) rsp.getList().get(0);
	    }

	/**
	 * Retorna os registros com situação ativa.
	 * @param tpSituacao
	 * @return
	 */
	public Integer getRowCountNotIn(Long idDivisaoCliente, Long idCliente, String tpSituacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.rowCount());
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc")
			.setProjection(pl)
			.add(Restrictions.eq("dc.tpSituacao", tpSituacao))
			.add(Restrictions.eq("dc.cliente.id", idCliente))
			/** NOT IN */
			.add(Restrictions.ne("dc.id", idDivisaoCliente));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Retorna registros, diferentes dos especificados na lista, com situação ativa.
	 * @param ids
	 * @param tpSituacao
	 * @return
	 */
	public List findByTpSituacao(DomainValue tpSituacao, List ids) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("dc.idDivisaoCliente"), "idDivisaoCliente");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc")
			.setProjection(pl)
			.add(Restrictions.eq("dc.tpSituacao", tpSituacao.getValue()))
			.add(Restrictions.not(Restrictions.in("dc.idDivisaoCliente", ids)))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public DivisaoCliente findPrimeiraDivisaoCadastradaByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc")
			.add(Restrictions.eq("dc.cliente.id", idCliente))
			.addOrder(Order.asc("dc.id"));
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()){
			return (DivisaoCliente)l.get(0);
		}
		return null;
	}
	
	/**
	 * Retorna o número de divisões ativas com dia de faturamento, dia de 
	 * vencimento e tabela de divisao de cliente relacionadas ao cliente 
	 * informado.
	 * 
	 * @author Mickaël Jalbert
	 * @author Luis Carlos Poletto
	 * @since 07/04/2006
	 * 
	 * @param Long idCliente
	 * @return Integer
	 * */
	public Long findCountByIdCliente(Long idCliente) {
		SqlTemplate hql = mountHql(idCliente);
		hql.addProjection("count(dc)");

		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (result.size() == 1){
			return (Long) result.get(0);			
		}
		return LongUtils.ZERO;
	}
	
	private SqlTemplate mountHql(Long idCliente){
		SqlTemplate hql = new SqlTemplate();
		hql.addInnerJoin(DivisaoCliente.class.getName(), "dc");
		hql.addInnerJoin("dc.diaFaturamentos", "df");
		hql.addInnerJoin("dc.prazoVencimentos", "pv");
		hql.addInnerJoin("dc.tabelaDivisaoClientes", "td");

		hql.addCustomCriteria("pv.nrPrazoPagamento IS NOT NULL");
		hql.addCriteria("dc.cliente.id", "=", idCliente);
		hql.addCriteria("dc.tpSituacao", "=", "A");
		return hql;
	}

	/**
	 * Realiza a ordenação da lista de endereços de Pessoas
	 * @param criterions Mapa de criterios vindo da tela
	 * @return List contendo os endereços encontrados
	 */
	@Override
	public List findListByCriteria(Map criterions) {
		List order = new ArrayList();
		order.add("dsDivisaoCliente");
		return super.findListByCriteria(criterions, order);
	}

	public List findByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc");
		dc.createAlias("dc.cliente","c");
		dc.createAlias("dc.tabelaDivisaoClientes","tdc");
		dc.createAlias("tdc.parametroClientes","pc");

		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("pc.tpSituacaoParametro", tpSituacaoParametro.getValue()));
		dc.add(Restrictions.ge("pc.dtVigenciaFinal", dtVigenciaFinal));

		if(idDivisaoCliente != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		}
		if(idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("tdc.idTabelaDivisaoCliente", idTabelaDivisaoCliente));
		} 

		dc.addOrder(Order.asc("dc.idDivisaoCliente"));
		dc.addOrder(Order.asc("tdc.idTabelaDivisaoCliente"));
		dc.addOrder(Order.asc("pc.idParametroCliente"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Retorna o idDivisaoCliente usando como critérios para a busca o idCliente e o cdDivisaoCliente.
	 * @param idCliente
	 * @param cdDivisaoCliente
	 * @return DivisaoCliente
	 */
	public DivisaoCliente findByIdClienteCdDivisao(Long idCliente, Long cdDivisaoCliente){
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("cliente","c")
			.add(Restrictions.eq("cdDivisaoCliente", cdDivisaoCliente))
			.add(Restrictions.eq("c.idCliente", idCliente));
		return (DivisaoCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param dsDivisaoCliente
	 * @return <DivisaoCliente> 
	 */
	public DivisaoCliente findDivisaoCliente(Long idCliente, String dsDivisaoCliente) {
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("cliente", "c")
			.add(Restrictions.eq("c.idCliente", idCliente))
			.add(Restrictions.eq("dsDivisaoCliente", dsDivisaoCliente).ignoreCase());
		return (DivisaoCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public DivisaoCliente findDivisaoClienteByClienteAndSituacao(Long idCliente, String divisaoCliente, String tpSituacao) {
		StringBuilder hql = new StringBuilder()
			.append(" FROM ")
			.append(DivisaoCliente.class.getName())
			.append(" WHERE  ")
			.append("  cliente.idCliente = ? ")
			.append("  AND lower(dsDivisaoCliente) = lower(?) ")
			.append("  AND tpSituacao = ? ");

		return (DivisaoCliente) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idCliente, divisaoCliente, tpSituacao});
	}
	
	public List findDivisaoClienteByClienteAndSituacao(Long idCliente, String tpSituacao) {
		StringBuilder hql = new StringBuilder()
			.append(" FROM ")
			.append(DivisaoCliente.class.getName())
			.append(" WHERE  ")
			.append("  cliente.idCliente = ? ")
			.append("  AND tpSituacao = ? ");

		return  getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, tpSituacao});
	}

	public Long findNewCdDivisaoCliente(Long idCliente){
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.max("dc.cdDivisaoCliente"));

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc");
		dc.createAlias("dc.cliente","c");
		dc.setProjection(pl);

		dc.add(Restrictions.eq("c.idCliente", idCliente));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (!result.isEmpty() && result.get(0) != null) {
			return Long.valueOf(((Long)result.get(0)).longValue() + 1);
		}
		return LongUtils.ONE;
	}

	/**
	 * TODO: alterar o nome do método
	 * Busca todas as divisões relacionadas ao cliente informado.
	 * 
	 * @param idCliente 
	 * @return
	 */
	public List findByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc")
			.add(Restrictions.eq("dc.cliente.id", idCliente));
		return findByDetachedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<DivisaoCliente> findDivisaoClienteByIdServico(Long idCliente, Long idServico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM ").append(DivisaoCliente.class.getName()).append(" dc")
			.append("       JOIN FETCH dc.tabelaDivisaoClientes tdc")
			.append(" WHERE dc.cliente.idCliente = ")
			.append("         (SELECT CASE WHEN c.tpCliente = 'F'")
			.append("                   THEN c.clienteMatriz.idCliente")
			.append("                   ELSE c.idCliente")
			.append("                 END as idCliente")
			.append("          FROM ").append(Cliente.class.getName()).append(" c")
			.append("          WHERE c.idCliente = ?)")
			.append("   AND tdc.servico.idServico = ?")
			.append("   AND dc.idDivisaoCliente = tdc.divisaoCliente.idDivisaoCliente")
			.append("   AND dc.tpSituacao = 'A'")
			.append(" ORDER BY dc.dsDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, idServico});
	}

	/**
	 * Busca as divisoes do cliente que estejam na mesma situacao fornecida, 
	 * normalmente o tpSituacao devera receber o valor "<code>A</code>".
	 * 
	 * @param idCliente
	 * @param tpSituacao
	 * @return
	 */
	public List findLookupDivisoesCliente(Long idCliente, String tpSituacao) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT new Map(dc.id as idDivisaoCliente, dc.dsDivisaoCliente as dsDivisaoCliente)")
			.append(" FROM ").append(DivisaoCliente.class.getName()).append(" dc")
			.append(" WHERE dc.cliente.idCliente = ")
			.append("         (SELECT CASE WHEN c.tpCliente = 'F'")
			.append("                   THEN c.clienteMatriz.idCliente")
			.append("                   ELSE c.idCliente")
			.append("                 END as idCliente")
			.append("          FROM ").append(Cliente.class.getName()).append(" c")
			.append("          WHERE c.idCliente = ?)")
			.append("   AND dc.tpSituacao = ?")
			.append(" ORDER BY dc.dsDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, tpSituacao});
	}

	/**
	 * Busca as divisoes do cliente que sejam do servico fornecido e que
	 * estejam na mesma situacao fornecida, normalmente o tpSituacao devera
	 * receber o valor "<code>A</code>".
	 * 
	 * @param idCliente
	 * @param idServico
	 * @param tpSituacao
	 * @return
	 */
	public List findDivisaoClienteByIdServico(Long idCliente, Long idServico, String tpSituacao) {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM ").append(DivisaoCliente.class.getName()).append(" dc")
			.append("       JOIN FETCH dc.tabelaDivisaoClientes tdc")
			.append(" WHERE dc.cliente.idCliente = ")
			.append("         (SELECT CASE WHEN c.tpCliente = 'F'")
			.append("                   THEN c.clienteMatriz.idCliente")
			.append("                   ELSE c.idCliente")
			.append("                 END as idCliente")
			.append("          FROM ").append(Cliente.class.getName()).append(" c")
			.append("          WHERE c.idCliente = ?)")
			.append("   AND tdc.servico.idServico = ?")
			.append("   AND dc.idDivisaoCliente = tdc.divisaoCliente.idDivisaoCliente")
			.append("   AND dc.tpSituacao = ?")
			.append(" ORDER BY dc.cdDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, idServico, tpSituacao});
	}

	@SuppressWarnings("unchecked")
	public List<DivisaoCliente> findDivisaoCliente(Long idCliente, DomainValue tpModal, DomainValue tpAbrangencia) {
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM ").append(DivisaoCliente.class.getName()).append(" dc")
			.append("       JOIN FETCH dc.tabelaDivisaoClientes tdc")
			.append(" WHERE dc.cliente.idCliente = ")
			.append("         (SELECT CASE WHEN c.tpCliente = 'F'")
			.append("                   THEN c.clienteMatriz.idCliente")
			.append("                   ELSE c.idCliente")
			.append("                 END as idCliente")
			.append("          FROM ").append(Cliente.class.getName()).append(" c")
			.append("          WHERE c.idCliente = ?)")
			.append("   AND tdc.servico.tpModal = ?")
			.append("   AND tdc.servico.tpAbrangencia = ?")
			.append("   AND dc.idDivisaoCliente = tdc.divisaoCliente.idDivisaoCliente")
			.append("   AND dc.tpSituacao = 'A'")
			.append(" ORDER BY dc.dsDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, tpModal.getValue(), tpAbrangencia.getValue()});
	}

	public List<DivisaoCliente> findByIdClienteAndTpModal(Long idCliente, String tpModal) {
		StringBuilder hql = new StringBuilder();
		
		hql
		.append("SELECT dc ")
		.append("FROM ").append(DivisaoCliente.class.getSimpleName()).append(" dc ")
		.append("JOIN dc.cliente cl ")
		.append("JOIN dc.tabelaDivisaoClientes tdc ")
		.append("JOIN tdc.servico s ")
		.append("WHERE ")
		.append("	 cl.idCliente =:idCliente ")
		.append("AND dc.tpSituacao =:tpSituacao ")
		.append("AND s.tpModal =:tpModal ")
		;
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("idCliente", idCliente);
		parameter.put("tpSituacao", ConstantesVendas.SITUACAO_ATIVO);
		parameter.put("tpModal", tpModal);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameter);
	}	
	
	/**
	 * Retorna a lista de divisões ativas ou que está dentro do devedor informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idCliente
	 * @param Long idDevedorDocServFat
	 * @return List
	 */
	public List findByIdClienteIdDevedorDocServFat(Long idCliente, Long idDevedorDocServFat) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(div.id as idDivisaoCliente, div.dsDivisaoCliente as dsDivisaoCliente)");

		hql.addFrom(Cliente.class.getName(), "cli");
		hql.addFrom(DivisaoCliente.class.getName(), "div left outer join div.devedorDocServFats dev");

		hql.addJoin("div.cliente.id", "NVL(cli.clienteMatriz.id, cli.id)");

		if (idDevedorDocServFat == null){
			hql.addCriteria("cli.id", "=", idCliente);
		} else {
			hql.addCustomCriteria("( ( div.tpSituacao = ? AND cli.id = ? ) OR dev.id = ? )");
			hql.addCriteriaValue("A");
			hql.addCriteriaValue(idCliente);
			hql.addCriteriaValue(idDevedorDocServFat);
		}
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()); 
	}

	/**
	 * Retorna a lista de divisões ativas ou que está dentro da fatura informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/12/2006
	 * 
	 * @param Long idCliente
	 * @param Long idFatura
	 * @return List
	 */
	public List findByIdClienteIdFatura(Long idCliente, Long idFatura) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(div.id as idDivisaoCliente, div.dsDivisaoCliente as dsDivisaoCliente)");

		hql.addFrom(Cliente.class.getName(), "cli");
		hql.addFrom(DivisaoCliente.class.getName(), "div left outer join div.faturas fat");

		hql.addJoin("div.cliente.id", "NVL(cli.clienteMatriz.id, cli.id)");		

		if (idFatura == null){
			hql.addCriteria("cli.id", "=", idCliente);
		} else {
			hql.addCustomCriteria("( ( div.tpSituacao = ? AND cli.id = ? ) OR fat.id = ? )");
			hql.addCriteriaValue("A");
			hql.addCriteriaValue(idCliente);
			hql.addCriteriaValue(idFatura);
		}

		hql.addGroupBy("div.id");
		hql.addGroupBy("div.dsDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()); 
	}
	
	/**
	 * Retorna a lista de divisões do cliente informado na situação informada. Se o cliente tem 
	 * um cliente matriz, retorna a lista de divisões do cliente matriz.
	 * 
	 * @author Mickaël Jalbert
	 * @since 12/12/2006
	 * 
	 * @param DivisaoClienteParam param
	 * @return List
	 */
	public List findByIdClienteMatriz(DivisaoClienteParam param) {
		SqlTemplate hql = mountHqlByIdClienteMatriz(param);
		hql.addProjection("distinct(div)");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());  

	}

	/**
	 * Retorna a lista de divisões do cliente informado na situação informada. Se o cliente tem 
	 * um cliente matriz, retorna a lista de divisões do cliente matriz.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/12/2006
	 * 
	 * @param Long idCliente
	 * @param String tpSituacao
	 * @param Long idDivisao
	 * @return List
	 */
	public List findMapByIdClienteMatriz(DivisaoClienteParam param) {
		SqlTemplate hql = mountHqlByIdClienteMatriz(param);

		hql.addProjection("new Map(div.id as idDivisaoCliente, div.dsDivisaoCliente as dsDivisaoCliente)");

		hql.addGroupBy("div.id");
		hql.addGroupBy("div.dsDivisaoCliente");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()); 
	}

	private SqlTemplate mountHqlByIdClienteMatriz(DivisaoClienteParam param) {
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(DivisaoCliente.class.getName(), "div");		
		hql.addInnerJoin("div.tabelaDivisaoClientes tdc");
		hql.addInnerJoin("tdc.servico ser, ");
		hql.addFrom(Cliente.class.getName(), "cli");

		StringBuffer criteria = new StringBuffer();

		criteria.append("div.cliente.id in ( cli.clienteMatriz.id, cli.id ) ");
		criteria.append(" and div.cliente.id = (case when cli.tpCliente = ? then  cli.clienteMatriz.id else cli.id end) ");
		hql.addCriteriaValue("F");
		
		criteria.append("AND ((cli.id = ? ");
		hql.addCriteriaValue(param.getIdCliente());

		if (param.getTpSituacao() != null){
			criteria.append("AND div.tpSituacao = ? ");
			hql.addCriteriaValue(param.getTpSituacao());
		}

		if (param.getTpAbrangencia() != null) {
			criteria.append(") AND ser.tpAbrangencia = ?");
			hql.addCriteriaValue(param.getTpAbrangencia());
		} else {
			criteria.append(")");
		}

		if (param.getTpModal() != null) {
			criteria.append(" AND ser.tpModal = ?");
			hql.addCriteriaValue(param.getTpModal());
		}

		if (param.getIdServico() != null) {
			criteria.append(" AND ser.id = ?");
			hql.addCriteriaValue(param.getIdServico());
		}

		if (param.getIdDivisaoCliente() != null){
			criteria.append(" OR div.id = ?");
			hql.addCriteriaValue(param.getIdDivisaoCliente());
		} 

		criteria.append(")");

		hql.addCustomCriteria(criteria.toString());
		hql.addOrderBy("div.dsDivisaoCliente");
		return hql;
	}	
	
	
	/**
	 * Retorna a divisao cliente com o menor cdDivisaoCliente
	 * 
	 * @param idCliente
	 * @param tpSituacao
	 * @return
	 */
	public DivisaoCliente findMenorCdDivisaoCliente(Long idCliente, String tpSituacao) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new map (dc.id as idDivisaoCliente) from ").append(DivisaoCliente.class.getName()).append(" dc ")
			.append("where dc.cliente.id = ? ")
			.append("and dc.tpSituacao = ? ")
			.append("and rownum <= 1 ")
			.append("order by dc.cdDivisaoCliente ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idCliente, tpSituacao});
    	if (result.isEmpty()) {
    		return null;
    	}
    	else {
    		AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(DivisaoCliente.class);
    		List<DivisaoCliente> lista = transformer.transformListResult(result);
    		return lista.get(0);
    	}
	}
	
	
	public List findDivisoesClienteByNrIdentificacaoAndTpSituacao(String nrIdentificacao, String tpSituacao) {
		String query = new StringBuilder()
							.append(" SELECT dc.id_divisao_cliente as idDivisaoCliente, dc.ds_divisao_cliente as dsDivisaoCliente, tdc.id_tabela_divisao_cliente as idTabDivisaoCliente ")
							.append(" FROM divisao_cliente dc, pessoa p, tabela_divisao_cliente tdc ")
							.append(" WHERE p.id_pessoa = dc.id_cliente ")
							.append("   and p.nr_identificacao = ?  ")
							.append("   and tdc.id_divisao_cliente = dc.id_divisao_cliente ") 
							.append("   and dc.tp_situacao = ? ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("id", resultSet.getLong(1));
				map.put("desc", resultSet.getString(2));
				map.put("idTabDivisaoCliente", resultSet.getLong(3));
				return map;
			}
		};

		return jdbcTemplate.query(query, new Object[]{nrIdentificacao, tpSituacao}, rowMapper);
	}
	
	public List findTabelaBaseByNrIdentificacaoOrDivisaoCliente(Long idDivisaoCliente, Long nrIdentificacao) {
		List<Long> params = new ArrayList<Long>();
		StringBuilder query = new StringBuilder()
		.append(" select ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco as tabelaCliente ")
		.append(" from pessoa p, tipo_tabela_preco ttp, subtipo_tabela_preco stp, tabela_preco tp, divisao_cliente dc, tabela_divisao_cliente tdc ")
		.append(" where dc.id_cliente = p.id_pessoa  ")
		.append("   and tdc.id_divisao_cliente = dc.id_divisao_cliente ")
		.append("   and tp.id_tabela_preco = tdc.id_tabela_preco  ")
		.append("   and ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco ")
		.append("   and stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ");
		
		if(nrIdentificacao != null){
			params.add(nrIdentificacao);
			query.append(" and p.nr_identificacao = :nrIdentificacao ");
		}
		
		if(idDivisaoCliente != null){
			params.add(idDivisaoCliente);
			query.append(" and dc.id_divisao_cliente = :idDivisaoCliente ");
		}
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return resultSet.getString(1);
			}
		};
		
		return jdbcTemplate.query(query.toString(), params.toArray(), rowMapper);
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List findIdTabelaDivisaoClienteByIdDivisaoCliente(Long idDivisaoCliente) {
		String query = new StringBuilder()
							.append(" SELECT ID_TABELA_DIVISAO_CLIENTE as idTabelaDivisao ")
							.append(" FROM TABELA_DIVISAO_CLIENTE")
							.append(" WHERE ID_DIVISAO_CLIENTE = ? ")
							.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Long> map = new HashMap();
				map.put("idTabelaDivisao", resultSet.getLong(1));				
				return map;
			}
		};

		return jdbcTemplate.query(query, new Object[]{idDivisaoCliente}, rowMapper);
	}

}