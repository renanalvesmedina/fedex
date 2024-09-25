package com.mercurio.lms.vendas.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.TrtCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TrtClienteDAO extends BaseCrudDao<TrtCliente, Long> {

	private static final String APROVADO = "A";
	private static final int ANO_FUTURO = 4000;
	private JdbcTemplate jdbcTemplate;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return TrtCliente.class;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {	
		DetachedCriteria dc = montaDetachedCriteria(criteria);
    	return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = montaDetachedCriteria(criteria);
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc); 
	}
	
	@SuppressWarnings("unchecked")
	public List<TrtCliente> findTrtVigenteByIdCliente(Long id, YearMonthDay dataVigenciaInicial) {
		//LMS-5434 - HF
		if (dataVigenciaInicial == null) {
			dataVigenciaInicial = JTDateTimeUtils.getDataAtual();
		}

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc")
				.add(Restrictions.eq("tc.cliente.id", id))
				.add(Restrictions.le("tc.dtVigenciaInicial", dataVigenciaInicial))
				.add(Restrictions.ge("tc.dtVigenciaFinal", dataVigenciaInicial));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public Boolean validateRegistroFilhoEmAprovacao(Long idTrtCliente) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" tc ");
		hql.append("INNER JOIN tc.trtClienteOriginal tco ");
		hql.append("WHERE tco.idTrtCliente = ? ");
		param.add(idTrtCliente);
		hql.append("AND tc.tpSituacaoAprovacao = ? ");
		param.add("E");
		
		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
		return !registros.isEmpty();
	}
	
	public Boolean validateTrtVigenteByIdCliente(Long idCliente, Long idTrtClienteAlteracao, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" tc ");
		hql.append("inner join tc.cliente c ");
		hql.append("WHERE ");
		hql.append("tc.tpSituacaoAprovacao = ? ");
		param.add(APROVADO);
		
		hql.append("AND c.idCliente = ? ");
		param.add(idCliente);
		
		if(idTrtClienteAlteracao != null){
			hql.append("AND tc.idTrtCliente != ? ");
			param.add(idTrtClienteAlteracao);	
		}
		
		hql.append("AND tc.dtVigenciaFinal >= ? ");
		param.add(dataVigenciaInicial);

		hql.append("AND tc.dtVigenciaInicial <= ? ");
		param.add(JTDateTimeUtils.maxYmd(dataVigenciaFinal));
		
		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());

		return !registros.isEmpty();
	}
	
	public Boolean validateTrtVigenteByIdTabela(Long idTabela, Long idTrtClienteAlteracao, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" tc ");
		hql.append("WHERE ");
		hql.append("tc.tpSituacaoAprovacao = ? ");
		param.add(APROVADO);
		
		hql.append("AND tc.idTabelaPreco = ? ");
		param.add(idTabela);
		
		if(idTrtClienteAlteracao != null){
			hql.append("AND tc.idTrtCliente <> ? ");
			param.add(idTrtClienteAlteracao);	
		}
		
		hql.append("AND tc.dtVigenciaFinal >= ? ");
		param.add(dataVigenciaInicial);

		hql.append("AND tc.dtVigenciaInicial <= ? ");
		param.add(JTDateTimeUtils.maxYmd(dataVigenciaFinal));
		
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray()).isEmpty();
	}
	
	public Boolean validateTrtSolicitadaByIdCliente(Long idCliente, Long idTrtClienteAlteracao, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal) {
		List<Object> param = new ArrayList<Object>();

		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" tc ");
		hql.append("inner join tc.cliente c ");
		hql.append("WHERE ");
		hql.append("tc.tpSituacaoAprovacao = ? ");
		param.add("E");
		
		hql.append("AND c.idCliente = ? ");
		param.add(idCliente);
		
		if(idTrtClienteAlteracao != null){
			hql.append("AND tc.idTrtCliente != ? ");
			param.add(idTrtClienteAlteracao);	
		}
		
		hql.append("AND tc.dtVigenciaFinalSolicitada >= ? ");
		param.add(dataVigenciaInicial);

		hql.append("AND tc.dtVigenciaInicialSolicitada <= ? ");
		param.add(JTDateTimeUtils.maxYmd(dataVigenciaFinal));
		
		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());

		return !registros.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public List<TrtCliente> findTrtClienteByClientes(List<Long> idClientes) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc")
			.add(Restrictions.in("tc.cliente.id", idClientes));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TrtCliente> findTrtClienteByIdTabela(Long idTabela) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tc").add(Restrictions.eq("tc.idTabelaPreco", idTabela));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	public List<TrtCliente> findCurrentTrtClienteByIdTabela(Long idTabela) {
		String hql = new StringBuilder()    
						.append(" FROM ")
						.append( TrtCliente.class.getName() ).append("  tc ")
						.append("  WHERE tc.idTabelaPreco = ? ")
						.append("  and tc.dtVigenciaInicial <= sysdate ")
						.append("  and tc.dtVigenciaFinal > sysdate ")
						.toString();
		
		List<Long> param = new ArrayList<Long>();
		param.add(idTabela);
		
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}
	
	private DetachedCriteria montaDetachedCriteria(TypedFlatMap criteria){
		ProjectionList pl =  Projections.projectionList();    	
    	pl.add(Projections.property("tc.idTrtCliente"), "idTrtCliente");
    	pl.add(Projections.property("tc.dtVigenciaInicial"), "dtVigenciaInicial");    	
    	pl.add(Projections.property("tc.dtVigenciaFinal"), "dtVigenciaFinal");
    	pl.add(Projections.property("tc.dtVigenciaInicialSolicitada"), "dtVigenciaInicialSolicitada");    	
    	pl.add(Projections.property("tc.dtVigenciaFinalSolicitada"), "dtVigenciaFinalSolicitada");
    	pl.add(Projections.property("tc.tpSituacaoAprovacao"), "tpSituacaoAprovacao");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(TrtCliente.class, "tc");
    	dc.setProjection(pl);
    	
    	if(criteria.getLong("idTabelaPreco") != null){
    		dc.add(Restrictions.eq("tc.idTabelaPreco", criteria.getLong("idTabelaPreco")));
    	}    		
    	
		if (criteria.getLong("cliente.idCliente") != null){
			dc.createAlias("tc.cliente", "c");
			dc.add(Restrictions.eq("c.idCliente", criteria.getLong("cliente.idCliente")));
		}
		
		if (criteria.getYearMonthDay("trtCliente.dtVigenciaInicial") != null){
			dc.add(Restrictions.ge("tc.dtVigenciaInicial", criteria.getYearMonthDay("trtCliente.dtVigenciaInicial")));
		}
		
		if (criteria.getYearMonthDay("trtCliente.dtVigenciaFinal") != null){
			dc.add(Restrictions.le("tc.dtVigenciaFinal", criteria.getYearMonthDay("trtCliente.dtVigenciaFinal")));
		} 		
		
    	dc.addOrder(Order.asc("tc.dtVigenciaInicial"));
    	dc.addOrder(Order.asc("tc.dtVigenciaFinal"));
    	dc.addOrder(Order.asc("tc.dtVigenciaInicialSolicitada"));
    	dc.addOrder(Order.asc("tc.dtVigenciaFinalSolicitada"));
    	dc.addOrder(Order.asc("tc.tpSituacaoAprovacao"));
    	
    	return dc;
	}

	public Long insertCliente(Cliente cliente, YearMonthDay vigenciaInicial) {

		TrtCliente trtCliente = new TrtCliente();
		trtCliente.setCliente(cliente);
		trtCliente.setDtVigenciaInicial(vigenciaInicial);
		YearMonthDay futuro = new YearMonthDay(ANO_FUTURO,1,1);
		trtCliente.setDtVigenciaFinal(futuro );
		trtCliente.setTpSituacaoAprovacao(new DomainValue(APROVADO));
		
		store(trtCliente, true);
		
		return trtCliente.getIdTrtCliente();
	}
	
	public void insertTrtClienteByTabela(Long idTabelaNova, Long idTabelaBase) {

		StringBuilder sql = new StringBuilder()
			.append(" insert into trt_cliente (" )
				.append("id_trt_cliente , ")
				.append("id_cliente , ")
				.append("dt_vigencia_inicial , ")
				.append("dt_vigencia_final , ")
				.append("dt_vigencia_inicial_solicitada , ")
				.append("dt_vigencia_final_solicitada , ")
				.append("id_trt_cliente_original , ")
				.append("id_pendencia , ")
				.append("tp_situacao_aprovacao , ")
				.append("id_tabela_preco) ")
					.append("select trt_cliente_sq.nextval , ")
					.append("id_cliente , ")
					.append("dt_vigencia_inicial , ")
					.append("dt_vigencia_final , ")
					.append("dt_vigencia_inicial_solicitada , ")
					.append("dt_vigencia_final_solicitada , ")
					.append("id_trt_cliente_original , ")
					.append("id_pendencia , ")
					.append(":tpSituacaoAprovacao , ")
					.append(":idTabelaNova ")
					.append("from trt_cliente ")
					.append("where id_tabela_preco = :idTabelaBase");

		Map<String, Object> params = new HashMap<>();
		params.put("tpSituacaoAprovacao", new DomainValue(APROVADO).getValue());
		params.put("idTabelaNova", idTabelaNova);
		params.put("idTabelaBase", idTabelaBase);
		getAdsmHibernateTemplate().executeUpdateBySql(String.valueOf(sql), params);
	}

	public void insertMunicipio(Long idMunicipio, Long idCliente, String cobraTRT) {

		String sql = new StringBuilder()
			.append(" insert into MUNICIPIO_TRT_CLIENTE  ")
			.append(" (ID_MUNICIPIO_TRT_CLIENTE, id_municipio ,id_trt_cliente, bl_cobra_trt) ")
			.append("  values (MUNICIPIO_TRT_CLIENTE_SQ.nextval, :idMunicipio, :idCliente, :cobraTRT) ")
			.toString();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idMunicipio", idMunicipio);
		params.put("idCliente", idCliente);
		params.put("cobraTRT", cobraTRT);
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);
	}

	public Long findIdCliente(String cnpj) {
		String query = new StringBuilder().
				append(" select id_pessoa from pessoa ").
				append(" where nr_identificacao = ? ").
				append(" and tp_identificacao = 'CNPJ'").
				toString();

		return  (Long) jdbcTemplate.query(query,  new Object[]{cnpj},  getLong());
	}

	private ResultSetExtractor getLong() {
		return new ResultSetExtractor() {			
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getLong(1) : null;
			}
		};	
	}

	public Long findIdMunicipio(String municipio, String uf) {
		String query = new StringBuilder().
				append(" select m.id_municipio ").
				append(" from municipio m, unidade_federativa uf  ").
				append(" where m.id_unidade_federativa = uf.id_unidade_federativa ").
				append(" and m.nm_municipio = ? ").
				append(" and uf.sg_unidade_federativa = ? ").
				append(" and uf.id_pais = 30 ").
				toString();

		return  (Long) jdbcTemplate.query(query,  new Object[]{municipio, uf},  getLong());
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void updateClienteTRTVigente(Long idCliente, YearMonthDay vigenciaInicial) {

		String sql = new StringBuilder()
		.append(" update TRT_CLIENTE   ")
		.append(" set dt_vigencia_final =  :vigencia  ")
		.append(" where dt_vigencia_inicial <= trunc(SYSDATE) and  ")
		.append("           dt_vigencia_final  >= trunc(SYSDATE) and  ")
		.append("           id_cliente = :idCliente          ")
		.toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vigencia", vigenciaInicial.minusDays(1));
		params.put("idCliente", idCliente);

		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);		
	}

	public void deleteMuniciopioClienteTRTFuturo(Long idCliente) {

		String sql = new StringBuilder()
		.append(" delete from MUNICIPIO_TRT_CLIENTE where id_trt_cliente in (   ")
		.append("         select id_trt_cliente from TRT_CLIENTE   ")
		.append("         where dt_vigencia_inicial > trunc(SYSDATE) and   ")
		.append("           id_cliente = :idCliente   ")
		.append("           )   ")
		.toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idCliente", idCliente);

		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);		

	}
	public void deleteClienteTRTFuturo(Long idCliente) {
		String sql = new StringBuilder()
		.append(" delete from TRT_CLIENTE where dt_vigencia_inicial > trunc(SYSDATE) and  ")
		.append("           id_cliente = :idCliente   ")
		.toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idCliente", idCliente);

		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);

	}

}
