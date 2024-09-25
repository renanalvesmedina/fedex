package com.mercurio.lms.vendas.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.HistoricoReajusteCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TabelaDivisaoClienteDAO extends BaseCrudDao<TabelaDivisaoCliente, Long> {

	private static final int FIRST = 0;

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.moeda", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoFob", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoFob.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoFob.subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente.cliente", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente.cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("servico", FetchMode.JOIN);
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("tabelaPreco", FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindPaginated.put("servico", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return TabelaDivisaoCliente.class;
	}

	public void evictTabelaDivisaoCliente(TabelaDivisaoCliente tabelaDivisaoCliente){
		getAdsmHibernateTemplate().evict(tabelaDivisaoCliente);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		StringBuilder from = new StringBuilder()
			.append(TabelaDivisaoCliente.class.getName()).append(" as tdc ")
			.append("left join fetch tdc.divisaoCliente").append(" as divisao ")
			.append("left join fetch tdc.tabelaPreco").append(" as tabela ")
			.append("left join fetch tabela.tipoTabelaPreco").append(" as ttp ")
			.append("left join fetch tabela.subtipoTabelaPreco").append(" as stp ")
			.append("left join fetch tdc.servico").append(" as servico ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		sql.addOrderBy("ttp.tpTipoTabelaPreco");
		sql.addOrderBy("ttp.nrVersao");
		sql.addOrderBy("stp.tpSubtipoTabelaPreco");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("servico.dsServico", LocaleContextHolder.getLocale()));

		// criterias, campos da tela de listagem
		sql.addCriteria("divisao.id", "=", criteria.getLong("divisaoCliente.idDivisaoCliente"), Long.class);
		sql.addCriteria("tabela.id", "=", criteria.getLong("tabelaPreco.idTabelaPreco"), Long.class);
		sql.addCriteria("tdc.blAtualizacaoAutomatica", "=", criteria.getBoolean("blAtualizacaoAutomatica"));

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());
	}

	public List<TabelaDivisaoCliente> findTabelaDivisaoClienteComboByDivisaoWithServico(Long idDivisao) {
		StringBuilder from = new StringBuilder()
			.append(TabelaDivisaoCliente.class.getName()).append(" as tdc ")
			.append("left join fetch tdc.divisaoCliente").append(" as divisao ")
			.append("left join fetch tdc.tabelaPreco").append(" as tabela ")
			.append("left join fetch tabela.tipoTabelaPreco").append(" as ttp ")
			.append("left join fetch tabela.subtipoTabelaPreco").append(" as stp ")
			.append("left join fetch tdc.servico").append(" as servico ")
			.append("left join fetch tabela.moeda").append(" as moeda ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		sql.addOrderBy("ttp.tpTipoTabelaPreco");
		sql.addOrderBy("ttp.nrVersao");
		sql.addOrderBy("stp.tpSubtipoTabelaPreco");

		if (idDivisao != null) {
			sql.addCriteria("divisao.id","=",idDivisao);
		}
		
		/**
		 * Find utilizado apenas em Parametros Cliente e Copy Parametros Cliente
		 * Tabelas do subtipo 'F' nunca terão parametrizações
		 */
		sql.addCriteria("stp.tpSubtipoTabelaPreco","<>","F");
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	public List<TabelaDivisaoCliente> findByDivisaoCliente(Long idDivisaoCliente){
		StringBuilder from = new StringBuilder()
		.append(TabelaDivisaoCliente.class.getName()).append(" as tdc ")
		.append("left join fetch tdc.divisaoCliente").append(" as divisao ")
		.append("left join fetch tdc.tabelaPreco").append(" as tabela ")
		.append("left join fetch tabela.tipoTabelaPreco").append(" as ttp ")
		.append("left join fetch tabela.subtipoTabelaPreco").append(" as stp ")
		.append("left join fetch tdc.servico").append(" as servico ");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		sql.addCriteria("divisao.idDivisaoCliente", "=", idDivisaoCliente);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	public int getRowCountByTabelaPreco(Long idTabelaPreco, List<String> cdParcelasPrecoList){
		StringBuilder sql = new StringBuilder()
		.append("SELECT TDC.ID_TABELA_DIVISAO_CLIENTE ")
		.append(" FROM TABELA_DIVISAO_CLIENTE TDC, ")
		.append(" SERVICO_ADICIONAL_CLIENTE SAC, ")
		.append(" PARCELA_PRECO PP ")
		.append(" WHERE TDC.ID_TABELA_PRECO = :idTabelaPreco ")
		.append(" AND SAC.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE ")
		.append(" AND SAC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
		.append(" AND PP.CD_PARCELA_PRECO IN (:parcelaPrecoList) ");
		
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idTabelaPreco", idTabelaPreco);
		parameters.put("parcelaPrecoList", cdParcelasPrecoList);
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters);
	}
	
	public int getRowCountByDivisaoCliente(Long idDivisaoCliente, List<String> cdParcelasPrecoList) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT TDC.ID_TABELA_DIVISAO_CLIENTE ")
		.append(" FROM TABELA_DIVISAO_CLIENTE TDC, ")
		.append(" SERVICO_ADICIONAL_CLIENTE SAC, ")
		.append(" PARCELA_PRECO PP ")
		.append(" WHERE TDC.ID_DIVISAO_CLIENTE = :idDivisaoCliente ")
		.append(" AND SAC.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE ")
		.append(" AND SAC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
		.append(" AND PP.CD_PARCELA_PRECO IN (:parcelaPrecoList) ");
		
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idDivisaoCliente", idDivisaoCliente);
		parameters.put("parcelaPrecoList", cdParcelasPrecoList);
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters);
	}
	
	public TabelaDivisaoCliente findTabelaDivisaoClienteById(Long idTabelaDivisaoCliente) {
		StringBuilder from = new StringBuilder()
			.append(TabelaDivisaoCliente.class.getName()).append(" as tdc ")
			.append("left join fetch tdc.divisaoCliente").append(" as divisao ")
			.append("left join fetch tdc.tabelaPreco").append(" as tabela ")
			.append("left join fetch tabela.tipoTabelaPreco").append(" as ttp ")
			.append("left join fetch tabela.subtipoTabelaPreco").append(" as stp ")
			.append("left join fetch tdc.servico").append(" as servico ")
			.append("left join fetch tabela.moeda").append(" as moeda ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());

		if (idTabelaDivisaoCliente != null) {
			sql.addCriteria("tdc.id","=",idTabelaDivisaoCliente);
		}
		return (TabelaDivisaoCliente) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
	}

	public Integer getRowCountByDivisaoClienteServicoBlObrigaDimensoes(Long idDivisaoCliente, Long idServico, Boolean blObrigaDimensoes) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "t")
			.setProjection(Projections.count("t.idTabelaDivisaoCliente"))
			.add(Restrictions.eq("t.blObrigaDimensoes", blObrigaDimensoes))
			.add(Restrictions.eq("t.divisaoCliente.id", idDivisaoCliente))
			.add(Restrictions.eq("t.servico.id", idServico));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idDivisaoCliente
     * @param tpTipoTabelaPreco
     * @return <List TabelaDivisaoCliente>
     */
	public List<TabelaDivisaoCliente> findTabelaDivisaoCliente(Long idDivisaoCliente, String tpTipoTabelaPreco) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("divisaoCliente", "dc");
		dc.createAlias("tabelaPreco", "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.add(Restrictions.eq("dc.id", idDivisaoCliente));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	

	/**
	 * Solicitação CQPRO00005948 da Integração.
	 * @param tpSubtipoTabelaPreco
	 * @param nrVersao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List<TabelaDivisaoCliente> findTabelaDivisaoCliente(String tpSubtipoTabelaPreco, Integer nrVersao, String tpTipoTabelaPreco){
		DetachedCriteria dc = DetachedCriteria.forClass(TabelaDivisaoCliente.class, "tdc");
		dc.createAlias("tdc.tabelaPreco", "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "sttp");
		dc.add(Restrictions.eq("sttp.tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		dc.add(Restrictions.eq("ttp.nrVersao", nrVersao));
		return findByDetachedCriteria(dc);
	} 

	public TabelaDivisaoCliente findTabelaDivisaoCliente(Long idDivisaoCliente, Long idServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tdc");
		dc.createAlias("tdc.divisaoCliente","dc");
		dc.createAlias("tdc.tabelaPreco","tp");
		dc.createAlias("tdc.servico","s");

		dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("s.idServico", idServico));

		return (TabelaDivisaoCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List<TabelaDivisaoCliente> findByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tdc");
		dc.createAlias("tdc.parametroClientes","pc");
		dc.createAlias("tdc.divisaoCliente","dc");
		dc.createAlias("dc.cliente","c");

		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("pc.tpSituacaoParametro", tpSituacaoParametro.getValue()));
		dc.add(Restrictions.ge("pc.dtVigenciaFinal", dtVigenciaFinal));

		if(idDivisaoCliente != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		}
		if(idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("tdc.idTabelaDivisaoCliente", idTabelaDivisaoCliente));
		}

		dc.addOrder(Order.asc("tdc.idTabelaDivisaoCliente"));
		dc.addOrder(Order.asc("pc.idParametroCliente"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Busca Tabelas da Divisao de Cliente.
	 * @param idTabelaPreco
	 * @param blAtualizacaoAutomatica
	 * @return
	 */
	public List<TabelaDivisaoCliente> findByIdTabelaPreco(Long idTabelaPreco, Boolean blAtualizacaoAutomatica) {
		StringBuilder from = new StringBuilder()
			.append(getPersistentClass().getName()).append(" as tdc")
			.append(" join fetch tdc.divisaoCliente as dc")
			.append(" join fetch tdc.tabelaPreco as tp");

		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(from.toString());

		/** Filters */
		if (idTabelaPreco != null) {
			hql.addCriteria("tp.id","=",idTabelaPreco);
		}
		if (blAtualizacaoAutomatica != null) {
			hql.addCriteria("tdc.blAtualizacaoAutomatica","=",blAtualizacaoAutomatica);
		}
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List<TabelaDivisaoCliente> findByServicoCliente(Long idCliente,String tpModal) {

		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("divisaoCliente", "dc")
		.createAlias("servico", "se")		
		.add(Restrictions.isNotNull("tabelaPreco"))
		.add(Restrictions.eq("dc.cliente.id", idCliente))
		.add(Restrictions.eq("se.tpModal", tpModal));
		
		return findByDetachedCriteria(dc);
	}

	public List<TabelaDivisaoCliente> findTabelaPrecoFob(Long idDivisaoCliente,String tpModal) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("divisaoCliente", "dc")
		.createAlias("servico", "se")
		.add(Restrictions.isNotNull("tabelaPrecoFob"))
		.add(Restrictions.eq("dc.id", idDivisaoCliente))
		.add(Restrictions.eq("se.tpModal", tpModal));		
		
		return null;
	}

	public Boolean findObrigatoriedadeDadosDimensaoECubagemByNrVolume(Long idVolumeNotaFiscal) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append(" SELECT tdc ");
		hql.append(" FROM VolumeNotaFiscal vnf, DoctoServico ds,TabelaDivisaoCliente tdc ");
		hql.append(" where 	ds.id = vnf.notaFiscalConhecimento.conhecimento.id ");
		hql.append(" 	and ds.divisaoCliente = tdc.divisaoCliente");
		// LMSA-1503
		hql.append(" 	and (ds.clienteByIdClienteBaseCalculo = tdc.divisaoCliente.cliente OR ds.clienteByIdClienteBaseCalculo.clienteMatriz = tdc.divisaoCliente.cliente)");
		hql.append(" 	and tdc.blObrigaDimensoes = 'S'");
		hql.append(" 	and vnf.id = :idVolumeNotaFiscal");
		
		params.put("idVolumeNotaFiscal", idVolumeNotaFiscal);
		
		Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), params);
		
		return  rowCount != null && rowCount > 0; 
		
	}
	
	
	public Boolean validateDimensaoOpcionalByFatorDensidade(Long idVolumeNotaFiscal) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append(" select tdc"); 
		hql.append(" from	VolumeNotaFiscal vnf, DevedorDocServ dds, TabelaDivisaoCliente tdc ");
		hql.append(" where 	dds.doctoServico.id = vnf.notaFiscalConhecimento.conhecimento.id ");
		hql.append(" 	and dds.doctoServico.divisaoCliente = tdc.divisaoCliente");
		hql.append(" 	and dds.cliente = tdc.divisaoCliente.cliente");
		hql.append(" 	and tdc.blObrigaDimensoes = 'N'");
		hql.append(" 	and tdc.nrFatorDensidade is not null");
		hql.append(" 	and vnf.id = :idVolumeNotaFiscal");
		params.put("idVolumeNotaFiscal", idVolumeNotaFiscal);
		
		Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), params);
		
		return  rowCount != null && rowCount > 0; 
	}
	
	@SuppressWarnings("unchecked")
	public List<TabelaDivisaoCliente> findByIdTabelaPrecoAndCliente(Long idTabelaPreco, Long idCliente, Long idDivisao) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("divisaoCliente", "dc");
		dc.createAlias("tabelaPreco", "tp");
		dc.createAlias("dc.cliente", "c");

		/** Filters */
		if (idTabelaPreco != null) {
			dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		}
		if (idCliente != null) {
			dc.add(Restrictions.eq("c.idCliente", idCliente));
		}
		if (idDivisao != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisao));
		}
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	
	/*	
		SELECT DISTINCT       TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO||TIPO_TABELA_PRECO.NR_VERSAO||'-      '||SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO
           , TABELA_PRECO.DS_DESCRICAO
        FROM TABELA_PRECO 
           , TIPO_TABELA_PRECO
           , SUBTIPO_TABELA_PRECO
           , TABELA_DIVISAO_CLIENTE
           , DIVISAO_CLIENTE
           , HISTORICO_REAJUSTE_CLIENTE
       WHERE 
   
         AND HISTORICO_REAJUSTE_CLIENTE.DT_REAJUSTE = (SELECT       MAX(HISTORICO_REAJUSTE_CLIENTE_DT.DT_REAJUSTE) DT_REAJUSTE
                                                         FROM HISTORICO_REAJUSTE_CLIENTE       HISTORICO_REAJUSTE_CLIENTE_DT
                                                        WHERE       HISTORICO_REAJUSTE_CLIENTE_DT.ID_TABELA_DIVISAO_CLIENTE =       TABELA_DIVISAO_CLIENTE.ID_TABELA_DIVISAO_CLIENTE)
         AND TABELA_DIVISAO_CLIENTE.ID_TABELA_DIVISAO_CLIENTE = <<id da       divisao cliente que está sendo consultada>>

	*/	

		public List<TabelaDivisaoCliente> findHistoricoReajusteClienteByIdDivisaoCliente(Long idDivisaoCliente) {
			StringBuilder from = new StringBuilder();
			
			from.append(HistoricoReajusteCliente.class.getSimpleName()).append(" hrc ");
			from.append(" left join fetch hrc.tabelaDivisaoCliente ").append(" tdc ");
			from.append(" left join fetch tdc.divisaoCliente ").append(" dc ");
			from.append(" left join fetch tdc.tabelaPreco ").append(" tp ");
			from.append(" left join fetch tp.subtipoTabelaPreco").append(" stp ");
			from.append(" left join fetch tp.tipoTabelaPreco").append(" ttp ");
			
			SqlTemplate hql = new SqlTemplate();
			hql.addFrom(from.toString());
			hql.addProjection(" tdc ");
			hql.addCriteria("dc.idDivisaoCliente", "=", idDivisaoCliente);
		
			List<TabelaDivisaoCliente> toReturn = getAdsmHibernateTemplate().find(hql.toString(), hql.getCriteria());

			return toReturn;		
		}
		
//		5219		
		public List<TabelaDivisaoCliente> findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(Long idDivisaoCliente) {	
			StringBuilder from = new StringBuilder();
			from.append(TabelaDivisaoCliente.class.getSimpleName() + " as tdc ");
			from.append("left join fetch tdc.divisaoCliente").append(" as divisao ");
			from.append("left join fetch tdc.tabelaPreco").append(" as tabelaPreco ");
			from.append("left join fetch tabelaPreco.tipoTabelaPreco").append(" as tipoTabelaPreco ");
			from.append("left join fetch tabelaPreco.subtipoTabelaPreco").append(" as subtipoTabelaPreco ");
			
			SqlTemplate sql = new SqlTemplate();
			sql.addFrom(from.toString());
			
			sql.addCriteria("divisao.idDivisaoCliente","=",idDivisaoCliente);			
			
			List<TabelaDivisaoCliente> l = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
			
			return l;
			
		}
		
		
		public void updateAtualizacaoAutomaticaByNrIdentificacaoAndDivisaoCliente(List<Long> ids){
			String query = new StringBuilder()
								.append(" UPDATE  tabela_divisao_cliente SET BL_ATUALIZACAO_AUTOMATICA = 'S' ") 
								.append(" WHERE id_tabela_divisao_cliente in ( :ids ) ") 
								.toString(); 
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ids", ids);
			
			getAdsmHibernateTemplate().executeUpdateBySql(query, param);
			getAdsmHibernateTemplate().flush();			
		}
				
		public Long findIdTabelaDivisaoclienteByNrIdentificacaoAndDivisaoCliente(String nrIdentificacao, String descDivisaoCliente){
			String query = new StringBuilder()
								.append(" select tdc.id_tabela_divisao_cliente idTabelaDivisaoCliente from tabela_divisao_cliente tdc, divisao_cliente dc, pessoa p")
								.append(" where dc.id_divisao_cliente = tdc.id_divisao_cliente and p.id_pessoa = dc.id_cliente ")
								.append(" and p.nr_identificacao = :nrIdentificacao ")
								.append(" and trim(upper(dc.DS_DIVISAO_CLIENTE)) = upper(:descDivisaoCliente) ")
								.toString();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("nrIdentificacao", nrIdentificacao);
			params.put("descDivisaoCliente", descDivisaoCliente);
			
			ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
				@Override
				public void configQuery(SQLQuery sqlQuery) {				
					sqlQuery.addScalar("idTabelaDivisaoCliente", Hibernate.LONG);
				}
			};
			
			List list = getAdsmHibernateTemplate().findBySql(query, params, configQuery);
			return list.isEmpty() ? null : (Long)list.get(FIRST);
		}

		public void executeAtualizacaoManual(List<Long> registrosAtualizacaoManual) {
			
			String sql = new StringBuilder()
			.append(" update TABELA_DIVISAO_CLIENTE set BL_ATUALIZACAO_AUTOMATICA = 'N' ")
			.append(" where ID_TABELA_DIVISAO_CLIENTE in (:listIds) ")
			.toString();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("listIds",registrosAtualizacaoManual);
			getAdsmHibernateTemplate().executeUpdateBySql(
					sql, params );
		}

		public List<Long> findAtualizacaoManual(final Long tipoServico) {

			final String sql = new StringBuilder()
			.append(" select tdc.id_tabela_divisao_cliente idTabelaDivisaoCliente ")
			.append(" 	from pessoa p, cliente c, divisao_cliente dc, tabela_divisao_cliente tdc ")
			.append(" 	where c.tp_cliente = 'S' ")
			.append(" 	and   c.TP_SITUACAO = 'A' ")
			.append(" 	and   p.id_pessoa = c.id_cliente ")
			.append(" 	and   dc.ID_CLIENTE = c.ID_CLIENTE ")
			.append(" 	and   dc.TP_SITUACAO = 'A' ")
			.append(" 	and   tdc.ID_DIVISAO_CLIENTE = dc.ID_DIVISAO_CLIENTE ")
			.append(" 	and BL_ATUALIZACAO_AUTOMATICA  = 'S' ")
			.append(" 	and ID_SERVICO = :tipoServico ")
			.toString();

			final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
				@Override
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("idTabelaDivisaoCliente", Hibernate.LONG);
				}
			};

			final HibernateCallback hcb = new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(sql);
	            	csq.configQuery(query);
					return query.setParameter("tipoServico", tipoServico).list();
				}
			};

			return getAdsmHibernateTemplate().executeFind(hcb);
		}
		
		
		
}