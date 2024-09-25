package com.mercurio.lms.vendas.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.expedicao.model.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SegmentoMercado;
import com.mercurio.lms.vendas.util.ClienteUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClienteDAO extends BaseCrudDao<Cliente, Long> {	
	
    public static final String ID_CLIENTE = "id_cliente";
    public static final String COUNTRY = "country";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String CITY = "city";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE = "state";
    public static final String COUNTY = "county";
    
    public static final String SEGMENTO_MERCADO = "segmento_mercado";
    public static final String NR_IDENTIFICACAO = "nr_identificacao";
    public static final String COTACAO_ABERTA = "cotacao_aberta";
    public static final String FATURAMENTO_MENSAL = "faturamento_mensal";
    public static final String MEDIA_EMBARQUE_SEMANA = "media_embarque_semana";
    public static final String QTDS_RNC_ABERTAS = "qtds_rnc_abertas";
    public static final String QTDS_RNC_EM_ABERTO = "qtds_rnc_em_aberto";
    public static final String TIPO_CLIENTE = "tipo_cliente";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String NOME_PESSOA = "nome_pessoa";
    public static final String NOME_FANTASIA = "nome_fantasia";

	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Cliente.class;
	}

	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("pessoa",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialCobranca",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialCobranca.pessoa",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialCobranca.pessoa.enderecoPessoa",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialCobranca.pessoa.enderecoPessoa.municipio",FetchMode.JOIN);
	}

	/**
	 * Retorna o cliente com a pessoa 'fetchada';
	 * 
	 * @author Mickaël Jalbert
	 * @since 30/05/2006
	 * 
	 * @param Long idCliente
	 * @return Cliente
	 * */
	public Cliente findByIdComPessoa(Long idCliente){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("cl");
		hql.addInnerJoin(Cliente.class.getName(), "cl");
		hql.addInnerJoin("fetch cl.pessoa", "pes");

		hql.addCriteria("cl.id", "=", idCliente);

		return (Cliente) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param nrIdentificacao
	 * @return Cliente
	 */
	public Cliente findByNrIdentificacaoValidaDadosBasicos(String nrIdentificacao, Boolean validaDadosBasicos) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa", "p");
		dc.add(Restrictions.eq("p.nrIdentificacao", nrIdentificacao));
		dc.add(Restrictions.in("p.tpIdentificacao", new Object[]{"CNPJ", "CPF"}));
		
		if (validaDadosBasicos) {
			dc.createAlias("p.enderecoPessoa", "pep");
			this.validateDadosBasicosCliente(dc);			
		}
		
		return (Cliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public Cliente findByNumeroIdentificacao(String nrIdentificacao) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa", "p");
		dc.add(Restrictions.eq("p.nrIdentificacao", nrIdentificacao));
		return (Cliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	@SuppressWarnings("unchecked")
	public List<Long> findIdsClienteCCT(){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("idCliente");
		sql.addFrom(Cliente.class.getName(), "c");
		sql.addCriteria("c.blClienteCCT", "=", true);
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
	
	/**
	 * Busca de Cliente específica para uso no Job de importação de notas do CCT.
	 * @param nrIdentificacao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cliente findByNumeroIdentificacaoParaCCT(String nrIdentificacao) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("idCliente, blConfAgendamento, blRecolheICMS");
		sql.addFrom(Cliente.class.getName(), "c");
		sql.addCriteria("c.pessoa.nrIdentificacao", "=", nrIdentificacao);

		List<Object[]> listClientes = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		if( listClientes != null && !listClientes.isEmpty() ){
			Cliente cliente = new Cliente();
			Object[] clienteValues = listClientes.get(0);
			cliente.setIdCliente((Long) clienteValues[0]);
			cliente.setBlConfAgendamento((Boolean) clienteValues[1]);
			cliente.setBlRecolheICMS((Boolean) clienteValues[2]);
			return cliente;
		} else {
			return null;
		}
	}
	
	public void executeAprovacaoWKFilialComercial(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_atende_comercial = id_filial_comercial_solicitada WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public void executeReprovacaoWKFilialComercial(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_comercial_solicitada = id_filial_atende_comercial WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	public void executeAprovacaoWKFilialOperacional(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_atende_operacional = id_filial_oper_solicitada WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public void executeReprovacaoWKFilialOperacional(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_oper_solicitada = id_filial_atende_operacional WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	public void executeAprovacaoWKFilialFinanceiro(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_cobranca = id_filial_cobranca_solicitada WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public void executeReprovacaoWKFilialFinanceiro(Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cliente SET id_filial_cobranca_solicitada = id_filial_cobranca WHERE id_cliente = :idCliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCliente", idCliente);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	
	/**
	 * Busca um único cliente pelo CNPJ parcial(primeiro da lista)<br/> 
	 * Utilizado para extrair informações gerais sobre um CNPJ parcial
	 * 
	 * @author Vagner Huzalo
	 * @since 10/12/2007
	 * 
	 * @param cnpjParcial - CNPJ parcial com 8 dígitos
	 * @return <code>Cliente</code>
	 */
	public Cliente findByCNPJParcial(String cnpjParcial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa", "p");
		dc.add(Restrictions.like("p.nrIdentificacao", cnpjParcial+"%"));
		dc.add(Restrictions.eq("p.tpIdentificacao", "CNPJ"));
		List list = getAdsmHibernateTemplate().findByCriteria(dc); 
		if (list != null && list.size()!= 0){
			return (Cliente) list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca um Cliente a partir de um id de Pedido Coleta
	 * Usando em casos onde se precisa performance na busca, não precisando nesses casos
	 * buscar o Pedido Coleta para depois dar um "get" em cliente.
	 * @param idPedidoColeta
	 * @return
	 */
	public Cliente findByIdPedidoColeta(Long idPedidoColeta){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("pc.cliente");
		hql.addFrom(PedidoColeta.class.getName(), "pc");
		hql.addCriteria("pc.id","=",idPedidoColeta);
		return (Cliente)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	public Long findIdClienteMatriz(Long idCliente){
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cm.idCliente ");
		hql.append("FROM ");
		hql.append(getPersistentClass().getName());
		hql.append(" c ");
		hql.append("INNER JOIN c.clienteMatriz cm ");
		hql.append("WHERE ");
		hql.append("c.idCliente = ? ");
		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idCliente});
	}	
	
	/**
	 * Retorna o cliente com a pessoa 'fetchada';
	 * 
	 * @author Mickaël Jalbert
	 * @since 30/05/2006
	 * 
	 * @param Long idCliente
	 * @return Cliente
	 * */
	public Cliente findByIdComFilialCobranca(Long idCliente){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("cl");
		hql.addInnerJoin(Cliente.class.getName(), "cl");
		hql.addInnerJoin("fetch cl.filialByIdFilialCobranca", "fil");

		hql.addCriteria("cl.id", "=", idCliente);

		return (Cliente)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	

	/**
	 * Detalha um registro para a tela de cliente informando o id
	 * 
	 * @param Serializable id do cliente
	 * @return Map com os dados do cliente aninhado
	 * */
	public Object findDadosIdentificacao(Serializable idCliente) {
		String hql = ClienteUtils.getHQLDadosIdentificacao();
		return getAdsmHibernateTemplate().find(hql, new Object[]{idCliente});
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa", FetchMode.JOIN);
		lazyFindById.put("pessoa.enderecoPessoa",FetchMode.JOIN);
		lazyFindById.put("pessoa.enderecoPessoa.municipio",FetchMode.JOIN);
		lazyFindById.put("pessoa.enderecoPessoa.municipio.unidadeFederativa",FetchMode.JOIN);
		lazyFindById.put("grupoEconomico", FetchMode.JOIN);
		lazyFindById.put("ramoAtividade", FetchMode.JOIN);
		lazyFindById.put("segmentoMercado", FetchMode.JOIN);
		lazyFindById.put("cedente", FetchMode.JOIN);

		lazyFindById.put("usuarioByIdUsuarioInclusao", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuarioAlteracao", FetchMode.JOIN);

		lazyFindById.put("filialByIdFilialAtendeComercial", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialAtendeComercial.pessoa", FetchMode.JOIN);
		lazyFindById.put("moedaByIdMoedaFatPrev", FetchMode.JOIN);

		lazyFindById.put("regionalOperacional", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialAtendeOperacional", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialAtendeOperacional.pessoa", FetchMode.JOIN);

		lazyFindById.put("filialByIdFilialCobranca",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialCobranca.pessoa",FetchMode.JOIN);

		lazyFindById.put("regionalFinanceiro", FetchMode.JOIN);
		lazyFindById.put("moedaByIdMoedaLimDoctos", FetchMode.JOIN);
		lazyFindById.put("moedaByIdMoedaSaldoAtual", FetchMode.JOIN);
		lazyFindById.put("moedaByIdMoedaLimCred", FetchMode.JOIN);
		
		lazyFindById.put("clienteMatriz", FetchMode.JOIN);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlFindPaginated((TypedFlatMap) criteria);

		StringBuilder projection = new StringBuilder();
		projection.append("new Map(cl.idCliente as idCliente, \n");
		projection.append("pe.nmPessoa as pessoa_nmPessoa, \n");
		projection.append("pe.tpPessoa as pessoa_tpPessoa, \n");
		projection.append("pe.nrIdentificacao as pessoa_nrIdentificacao, \n");
		projection.append("pe.tpIdentificacao as pessoa_tpIdentificacao, \n");
		projection.append("pe.nmFantasia as pessoa_nmFantasia, \n");
		projection.append("ep.dsEndereco as enderecoPessoa_dsEndereco, \n");
		projection.append("ep.nrEndereco as enderecoPessoa_nrEndereco, \n");
		projection.append("ep.dsComplemento as enderecoPessoa_dsComplemento, \n");
		projection.append("tl.dsTipoLogradouro as enderecoPessoa_tipoLogradouro_dsTipoLogradouro, \n");
		projection.append("m.nmMunicipio as enderecoPessoa_municipio_nmMunicipio, \n");
		projection.append("uf.sgUnidadeFederativa as enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa, \n");
		projection.append("cl.nrConta as nrConta, \n");
		projection.append("cl.tpCliente as tpCliente, \n");
		projection.append("cl.tpSituacao as tpSituacao, \n");
		projection.append("cl.blNaoAtualizaDBI as blNaoAtualizaDBI, \n");
		projection.append("cl.blEnviaDacteXmlFat as blEnviaDacteXmlFat, \n");
		projection.append("filc.id as filialByIdFilialCobranca_idFilial, \n");
		projection.append("filc.sgFilial as filialByIdFilialCobranca_sgFilial, \n");
		projection.append("pesfilc.nmFantasia as filialByIdFilialCobranca_pessoa_nmFantasia)");


		sql.addProjection(projection.toString());

		StringBuffer joins = new StringBuffer();
		joins.append(" join cl.pessoa as pe ");
		joins.append(" join cl.filialByIdFilialCobranca as filc ");
		joins.append(" join filc.pessoa as pesfilc ");
		joins.append(" left outer join pe.enderecoPessoa as ep ");
		joins.append(" left outer join ep.tipoLogradouro as tl ");
		joins.append(" left outer join ep.municipio as m ");
		joins.append(" left outer join m.unidadeFederativa as uf ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")){
			Long idFilialOperacional = MapUtils.getLong(MapUtils.getMap(criteria, "filialByIdFilialAtendeOperacional"), "idFilial");
			if(idFilialOperacional != null) {
				sql.addCriteria("fOperacional.idFilial","=", idFilialOperacional);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}

		sql.addFrom(Cliente.class.getName()+" cl " + joins.toString());
		sql.addOrderBy("pe.nmPessoa");
		sql.addOrderBy("pe.tpIdentificacao");
		sql.addOrderBy("pe.nrIdentificacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria()); 
		List list = rsp.getList();
		rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(list));
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlFindPaginated(criteria);
		sql.addProjection("count(cl.idCliente)");

		StringBuilder joins = new StringBuilder();
		joins.append(Cliente.class.getName()).append(" cl");
		joins.append(" join cl.pessoa as pe ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")){
			Long idFilialOperacional = MapUtils.getLong(MapUtils.getMap(criteria, "filialByIdFilialAtendeOperacional"), "idFilial");
			if(idFilialOperacional != null) {
				sql.addCriteria("fOperacional.idFilial", "=", idFilialOperacional);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}

		sql.addFrom(joins.toString());

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}
	
	/**
	 * Método que retorna uma parte do sql do findPaginated
	 * 
	 * @param Map criteria
	 * @return SqlTemplate sql
	 * */
	private SqlTemplate getSqlFindPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");		
		sql.addJoin("nvl(pe.tpIdentificacao, 'CNPJ')","dv.value"); 
		sql.addCustomCriteria("do.name = 'DM_TIPO_IDENTIFICACAO'");

		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			sql.addCriteria("lower(pe.nrIdentificacao)", "like", nrIdentificacao.toLowerCase());
		}
		sql.addCriteria("pe.tpIdentificacao", "=", criteria.getString("pessoa.tpIdentificacao"));
		String nmPessoa = criteria.getString("pessoa.nmPessoa");
		if (StringUtils.isNotBlank(nmPessoa)) {
			sql.addCriteria("lower(pe.nmPessoa)", "like", nmPessoa.toLowerCase());
		}
		sql.addCriteria("pe.tpPessoa", "like", criteria.getString("pessoa.tpPessoa"));
		String nmFantasia = criteria.getString("nmFantasia");
		if (StringUtils.isNotBlank(nmFantasia)) {
			sql.addCriteria("lower(pe.nmFantasia)", "like", nmFantasia.toLowerCase());
		}
		sql.addCriteria("cl.nrConta", "=", criteria.getLong("nrConta"));
		sql.addCriteria("cl.tpCliente", "like", criteria.getString("tpCliente"));
		sql.addCriteria("cl.tpSituacao", "like", criteria.getString("tpSituacao"));

		Long idUsuario = criteria.getLong("usuariosCliente.usuarioLMS.idUsuario");
		if(idUsuario != null) {
			if(!Boolean.TRUE.equals(criteria.getBoolean("usuariosCliente.usuarioLMS.blIrrestritoCliente"))) 
			{
				sql.addCriteria("cl.usuariosCliente.usuarioLMS.id", "=", idUsuario);
			}
		}

		return sql;
	} 
		
	/**
	 * Retorna o proximo número de conta (max + 1)
	 * 
	 * @return Interer novo número de conta.
	 * */
	public Long findNewNrConta() {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("max(cl.nrConta)");
		sql.addFrom(Cliente.class.getName()+" cl ");		

		List<Long> numeroRegistro = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());

		if (numeroRegistro.size() > 0 && numeroRegistro.get(0) != null) {
			return Long.valueOf((numeroRegistro.get(0)).longValue() + 1);
		} else {
			return Long.valueOf(1);
		}
	}
	
	/**
	 * Retorna a um map de dados com a data de inclusão e o usuario de inclusão e modificação. 
	 * 
	 * @param Long Id do cliente
	 * @return Map Dados de inclusão
	 * */
	public Map<String, Object> findDataAfterSave(Serializable idCliente) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(cl.dtGeracao as dtGeracao, usi.idUsuario as idUsuarioi, usa.idUsuario as idUsuarioa, cl2.idCliente as idCliente, pe.nmPessoa as nmPessoa, pe.nrIdentificacao as nrIdentificacao, cl.tpSituacao as tpSituacao, cl.nrConta as nrConta)");
		sql.addFrom(Cliente.class.getName()+" cl left join cl.usuarioByIdUsuarioInclusao as usi left join cl.usuarioByIdUsuarioAlteracao as usa join cl.cliente as cl2 join cl2.pessoa as pe");
		sql.addCriteria("cl.idCliente", "=", idCliente);

		List<Map<String, Object>> list = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		return list.get(0);
	}
	
	public Cliente findClienteByFinanceiro(Long idClientePrincipal){
		SqlTemplate sql = new SqlTemplate();
		
		try {
			sql.addProjection("new Map(cl2.idCliente as idCliente, pe.nmPessoa as nmPessoa, pe.nrIdentificacao as nrIdentificacao)");
			sql.addFrom(Cliente.class.getName()+" cl join cl.cliente as cl2 join cl2.pessoa as pe");
			
			sql.addCriteria("cl.id", "=", idClientePrincipal);
			
		} catch (Exception e){
			log.error(e);
		}
		List<Map<String, Object>> list = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
		if (!list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			Cliente cliente = new Cliente();
			cliente.setIdCliente((Long)map.get("idCliente"));
			Pessoa pessoa = new Pessoa();
			pessoa.setNrIdentificacao((String)map.get("nrIdentificacao"));
			pessoa.setNmPessoa((String)map.get("nmPessoa"));
			cliente.setPessoa(pessoa);
			return cliente; 
		} else {
			return null;
		}
	}
	
	public List findClientesByEnderecoVigente(Long idMunicipio){
		DetachedCriteria dc = createDetachedCriteria();

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = Projections.projectionList()
		.add(Projections.distinct(Projections.property("idCliente")), "idCliente")
		.add(Projections.property("blCobrancaCentralizada"),"blCobrancaCentralizada")
		.add(Projections.property("filialByIdFilialCobranca.idFilial"),"filialByIdFilialCobranca.idFilial")
		.add(Projections.property("filialByIdFilialAtendeComercial.idFilial"),"filialByIdFilialAtendeComercial.idFilial")
		.add(Projections.property("filialByIdFilialAtendeOperacional.idFilial"),"filialByIdFilialAtendeOperacional.idFilial");
		dc.setProjection(projectionList);
		dc.createAlias("pessoa", "p");
		dc.createAlias("p.enderecoPessoas", "ep");

		dc.add(Restrictions.eq("ep.municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.le("ep.dtVigenciaInicial", today));
		dc.add(Restrictions.ge("ep.dtVigenciaFinal",today));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Cliente.class));

		return findByDetachedCriteria(dc);
	}

	public Integer getRowCountByIdPessoa(Long idPessoa) {
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(Projections.rowCount())
		.add(Restrictions.eq("pessoa.id", idPessoa));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Busca um cliente ativo pelo número da identificação.
	 * autor Julio Cesar Fernandes Corrêa
	 * 04/01/2006
	 * @param nrIndentificacao
	 * @return
	 */
	public List findLookupClienteEndereco(Long idCliente, String nrIndentificacao, Boolean validaDadosBasicos) {
		
		ProjectionList pl = Projections.projectionList()
		.add(Projections.distinct(Projections.property("c.idCliente")), "idCliente")
		.add(Projections.property("c.tpCliente"), "tpCliente")
		.add(Projections.property("c.tpEmissaoDoc"), "tpEmissaoDoc")
		.add(Projections.property("c.blDificuldadeEntrega"), "blDificuldadeEntrega")
		.add(Projections.property("c.blPermiteCte"), "blPermiteCte")
		.add(Projections.property("c.blObrigaSerie"), "blObrigaSerie")
		.add(Projections.property("c.blNumeroVolumeEDI"), "blNumeroVolumeEDI")
		.add(Projections.property("c.blProdutoPerigoso"), "blProdutoPerigoso")
		.add(Projections.property("c.blControladoPoliciaCivil"), "blControladoPoliciaCivil")
		.add(Projections.property("c.blControladoPoliciaFederal"), "blControladoPoliciaFederal")
		.add(Projections.property("c.blControladoExercito"), "blControladoExercito")
		.add(Projections.property("p.nmPessoa"), "pessoa_nmPessoa")
		.add(Projections.property("p.nrIdentificacao"), "pessoa_nrIdentificacao")
		.add(Projections.property("p.tpIdentificacao"), "pessoa_tpIdentificacao")
		.add(Projections.property("p.tpPessoa"), "pessoa_tpPessoa")
		.add(Projections.property("pep.dsEndereco"), "pessoa_endereco_dsEndereco")
		.add(Projections.property("pep.nrEndereco"), "pessoa_endereco_nrEndereco")
		.add(Projections.property("pep.dsComplemento"), "pessoa_endereco_dsComplemento")
		.add(Projections.property("pep.dsBairro"), "pessoa_endereco_dsBairro")
		.add(Projections.property("pep.nrCep"), "pessoa_endereco_nrCep")
		.add(Projections.property("pepm.idMunicipio"), "pessoa_endereco_idMunicipio")
		.add(Projections.property("pepm.nmMunicipio"), "pessoa_endereco_nmMunicipio")
		.add(Projections.property("pepmuf.idUnidadeFederativa"), "pessoa_endereco_idUnidadeFederativa")
		.add(Projections.property("pepmuf.sgUnidadeFederativa"), "pessoa_endereco_sgUnidadeFederativa")
		.add(Projections.property("pepmp.nmPais"), "pessoa_endereco_nmPais")
		.add(Projections.property("peptl.idTipoLogradouro"), "pessoa_endereco_idTipoLogradouro")
		.add(Projections.property("peptl.dsTipoLogradouro"), "pessoa_endereco_dsTipoLogradouro");


		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(pl)
		.createAlias("c.pessoa", "p")
		.createAlias("p.enderecoPessoa", "pep")
		.createAlias("pep.tipoLogradouro", "peptl")
		.createAlias("pep.municipio", "pepm")
		.createAlias("pepm.unidadeFederativa", "pepmuf")
		.createAlias("pepmuf.pais", "pepmp");

		if(idCliente != null) {
			dc.add(Restrictions.eq("c.id", idCliente));
		}
		if(StringUtils.isNotBlank(nrIndentificacao)) {
			dc.add(Restrictions.eq("p.nrIdentificacao", nrIndentificacao));
		}
		
		if (validaDadosBasicos) {
			this.validateDadosBasicosCliente(dc);			
		} else {
		dc.add(Restrictions.eq("c.tpSituacao", "A"));
		}

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	private void validateDadosBasicosCliente(DetachedCriteria dc) {
		dc.createAlias("p.inscricaoEstaduais", "ie");
		dc.createAlias("ie.tiposTributacaoIe", "tti");
		
		dc.add(Restrictions.ne("c.tpSituacao", "I"));
		dc.add(Restrictions.isNotNull("p.nmPessoa"));
		dc.add(Restrictions.isNotNull("pep.idEnderecoPessoa"));
	}

	public List findLookupCliente(String nrIndentificacao, String tpCliente, String tpSituacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idCliente"), "idCliente")
			.add(Projections.property("c.tpCliente"), "tpCliente")
			.add(Projections.property("p.nmPessoa"), "pessoa_nmPessoa")
			.add(Projections.property("p.nrIdentificacao"), "pessoa_nrIdentificacao")
			.add(Projections.property("p.tpIdentificacao"), "pessoa_tpIdentificacao");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.setProjection(pl)
			.createAlias("c.pessoa", "p")
			.add(Restrictions.eq("p.nrIdentificacao", nrIndentificacao));
		if(tpCliente != null) {
			dc.add(Restrictions.eq("c.tpCliente", tpCliente));
		}
		if(tpSituacao != null) {
			dc.add(Restrictions.eq("c.tpSituacao", tpSituacao));
		}

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List<Cliente> findLookupClienteCustom(String nrIndentificacao, String nrIndentificacaoCustom) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.createAlias("c.pessoa", "p");

		if(StringUtils.isNotBlank(nrIndentificacao)) {
			dc.add(Restrictions.eq("p.nrIdentificacao", nrIndentificacao));
		/** Somente para PJs */
		} else if(StringUtils.isNotBlank(nrIndentificacaoCustom)) {
			dc.add(Restrictions.eq("p.tpPessoa", "J"));
			dc.add(Restrictions.like("p.nrIdentificacao", nrIndentificacaoCustom));
		}
		return findByDetachedCriteria(dc);
	}

	public Cliente findClienteResponsavelByIdCliente(Long idCliente) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("resp.idCliente"), "idCliente")
			.add(Projections.property("resp.tpCliente"), "tpCliente")
			.add(Projections.property("p.nrIdentificacao"), "pessoa.nrIdentificacao");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.createAlias("c.cliente", "resp")
		.createAlias("resp.pessoa", "p")
		.setProjection(pl)
		.add(Restrictions.idEq(idCliente))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Cliente.class));
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (Cliente)l.get(0);
		return null;
	}

	public SegmentoMercado findSegmentoMercadoByIdCliente(Long idCliente) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.segmentoMercado.idSegmentoMercado"), "idSegmentoMercado");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.setProjection(pl)
			.add(Restrictions.idEq(idCliente))
			.setResultTransformer(new AliasToBeanResultTransformer(SegmentoMercado.class));
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (SegmentoMercado)l.get(0);
		return null;
	}

	public Integer getRowCountResponsavelDiferente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.setProjection(Projections.count("c.idCliente"))
			.add(Restrictions.idEq(idCliente))
			.add(Restrictions.ne("c.cliente.id", idCliente));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Método que retorna uma list de clientes.
	 * Possui telefone como criteria, sendo opcional. 
	 * @param TypedFlatMap criteria
	 * @param FindDefinition findDefinition
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedClientesByTelefone(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sql = this.getSqlFindPaginatedClientesByTelefone(criteria);
		StringBuilder projection = new StringBuilder();
		projection.append("distinct new Map( cl.idCliente as idCliente,");
		projection.append(" pe.nmPessoa as pessoa_nmPessoa,");
		projection.append(" pe.tpPessoa as pessoa_tpPessoa,");

		projection.append("ep.dsEndereco as enderecoPessoa_dsEndereco, \n");
		projection.append("ep.nrEndereco as enderecoPessoa_nrEndereco, \n");
		projection.append("ep.dsComplemento as enderecoPessoa_dsComplemento, \n");
		projection.append("tl.dsTipoLogradouro as enderecoPessoa_tipoLogradouro_dsTipoLogradouro, \n");
		projection.append("m.nmMunicipio as enderecoPessoa_municipio_nmMunicipio, \n");
		projection.append("uf.sgUnidadeFederativa as enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa, \n");
		
		projection.append(" pe.nrIdentificacao as pessoa_nrIdentificacao,");
		projection.append(" pe.tpIdentificacao as pessoa_tpIdentificacao,");
		projection.append(" pe.nmFantasia as pessoa_nmFantasia,");
		projection.append(" cl.nrConta as nrConta,");
		projection.append(" cl.tpCliente as tpCliente,");
		projection.append(" cl.tpSituacao as tpSituacao");
		projection.append(")");
		sql.addProjection(projection.toString());

		StringBuffer joins = new StringBuffer();
		joins.append(" join cl.pessoa as pe ");
		joins.append(" left join pe.telefoneEnderecos as te ");
		joins.append(" left outer join pe.enderecoPessoa as ep ");
		joins.append(" left outer join ep.tipoLogradouro as tl ");
		joins.append(" left outer join ep.municipio as m ");
		joins.append(" left outer join m.unidadeFederativa as uf ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")) {
			Long filialOperacional = criteria.getLong("filialByIdFilialAtendeOperacional.idFilial");
			if(filialOperacional != null) {
				sql.addCriteria("fOperacional.idFilial", "=", filialOperacional);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}

		sql.addFrom(Cliente.class.getName()+" cl " + joins.toString());
		sql.addOrderBy("pe.nmPessoa");
		sql.addOrderBy("pe.tpIdentificacao");
		sql.addOrderBy("pe.nrIdentificacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDefinition.getCurrentPage(), findDefinition.getPageSize(),sql.getCriteria()); 
		List list = rsp.getList();
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(list));
		return rsp;
	}

	/**
	 * Método que retorna a quantidade de clientes. 
	 * Possui telefone como criteria, sendo opcional. 
	 * @param TypedFlatMap criteria
	 * @param FindDefinition findDefinition
	 * @return Integer
	 */
	public Integer getRowCountClientesByTelefone(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sql = this.getSqlFindPaginatedClientesByTelefone(criteria);
		sql.addProjection("count(distinct cl.idCliente)");

		StringBuffer joins = new StringBuffer();
		joins.append(" join cl.pessoa as pe ");
		joins.append(" left join pe.telefoneEnderecos as te ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")) {
			Long filialOperacional = criteria.getLong("filialByIdFilialAtendeOperacional.idFilial");
			if(filialOperacional != null) {
				sql.addCriteria("fOperacional.idFilial", "=", filialOperacional);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}

		sql.addFrom(Cliente.class.getName()+" cl " + joins.toString());

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}	

	/**
	 * Método que retorna uma parte do sql do findPaginatedClientesByTelefone
	 * 
	 * @param TypedFlatMap criteria
	 * @return SqlTemplate sql
	 * */
	private SqlTemplate getSqlFindPaginatedClientesByTelefone(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");		
		sql.addJoin("nvl(pe.tpIdentificacao, 'CNPJ')", "dv.value"); 
		sql.addCustomCriteria("do.name = 'DM_TIPO_IDENTIFICACAO'");

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nrIdentificacao"))) { 
			sql.addCriteria("lower(pe.nrIdentificacao)", "like", (criteria.getString("pessoa.nrIdentificacao")).toLowerCase());
		}

		sql.addCriteria("pe.tpIdentificacao", "=", criteria.getString("pessoa.tpIdentificacao"));

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nmPessoa"))) { 
			sql.addCriteria("lower(pe.nmPessoa)", "like", (criteria.getString("pessoa.nmPessoa")).toLowerCase());
		}

		sql.addCriteria("pe.tpPessoa", "like", criteria.getString("pessoa.tpPessoa"));

		if (StringUtils.isNotBlank(criteria.getString("nmFantasia"))) { 
			sql.addCriteria("lower(pe.nmFantasia)", "like", (criteria.getString("nmFantasia")).toLowerCase());
		}
		if (StringUtils.isNotBlank(criteria.getString("nrDdd"))) { 
			sql.addCriteria("te.nrDdd", "=", criteria.getString("nrDdd"));
		}

		if (StringUtils.isNotBlank(criteria.getString("nrTelefone"))) { 
			sql.addCriteria("te.nrTelefone", "=", criteria.getString("nrTelefone"));
		}

		sql.addCriteria("cl.nrConta", "=", criteria.getLong("nrConta"));
		sql.addCriteria("cl.tpCliente", "like", criteria.getString("tpCliente"));
		sql.addCriteria("cl.tpSituacao", "like", criteria.getString("tpSituacao"));

		return sql;
	}

	public Cliente findById2(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cliente")
			.add(Restrictions.eq("cliente.id", idCliente));
		List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if(!l.isEmpty()){
			return (Cliente)l.get(0);
		}
		return null;
	}

	public List findClientesByUsuario(Usuario usuario) {

		StringBuffer sb = new StringBuffer();
		if (!usuario.getBlIrrestritoCliente().booleanValue()) {
			sb.append("select c")
			.append(" from " + Cliente.class.getName() + " as c")
			.append(" join c.usuariosCliente uc")
			.append(" where uc.usuarioLMS.id = ?");
		} else {
			sb.append("select c")
			.append(" from " + Usuario.class.getName() + " as u")
			.append(" join u.clienteUsuario cu")
			.append(" join cu.cliente c")
			.append(" where u.id = ?");
		}		
		return getAdsmHibernateTemplate().find(sb.toString(), usuario.getIdUsuario());
	}
	
	public List<Object[]> findCnpjAutorizadoByUser(Long idUsuario) {

		StringBuffer sql = new StringBuffer("select");
		sql.append(" p.id_pessoa as id,")
			.append(" cnpj.pess_id as cpfcnpj") 
			.append(" from usuario_cliente uc, ")
			.append(" cnpj_usuario cnpj,") 
			.append(" pessoa p where uc.id_usuario_cliente = :idUsuario")
			.append(" and cnpj.id_usuario_cliente = uc.id_usuario_cliente")
			.append(" and ltrim(p.nr_identificacao,'0') = to_char(cnpj.pess_id)");
		  ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
				@Override
				public void configQuery(SQLQuery sqlQuery) {	
					sqlQuery.addScalar("id",Hibernate.LONG);
					sqlQuery.addScalar("cpfcnpj", Hibernate.STRING);
				}
			};

			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idUsuario", idUsuario);
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);	  
	}
	
	public Cliente findClientePadraoByUsuario(Usuario usuario) {
		StringBuffer sb = new StringBuffer()
		.append(" select c")
		.append(" from " + Cliente.class.getName() + " as c")
		.append(" where c.usuariosPadraoCliente.idUsuario = ?");

		return (Cliente) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { usuario.getIdUsuario() });
	}

	/**
	 * Método de busca de um cliente pelo número de identificação
	 * @param nrIdentificacao Número de identificação
	 * @return Cliente
	 */
	public Cliente findClienteByNrIdentificacaoForDepositoContaCorrente(String nrIdentificacao){

		SqlTemplate sql = getQueryHqlBuscaCliente(nrIdentificacao);

		sql.addProjection("new Map(c.id as idCliente, " +
						" p.nmPessoa as nmPessoa, " +
						" p.nrIdentificacao as nrIdentificacao )");

		List<Map<String, Object>> listClientes = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		if( listClientes != null && !listClientes.isEmpty() ){
			Cliente cliente = new Cliente();
			Pessoa pessoa = new Pessoa();

			Map<String, Object> map = listClientes.get(0);

			cliente.setIdCliente((Long) map.get("idCliente"));			
			pessoa.setNmPessoa((String) map.get("nmPessoa"));
			pessoa.setNrIdentificacao((String) map.get("nrIdentificacao"));

			cliente.setPessoa(pessoa);

			return cliente;
		} else {
			return null;
		}
	}

	/**
	 * Monta a query padrão para a classe Cliente
	 * OBS : Query sem projection e sem orderBy, deve-se informar a projection específica e o orderBy específico 
	 * em cada método que utilizar esta query 
	 * @return SqlTemplate
	 */
	public SqlTemplate getQueryHqlBuscaCliente(String nrIdentificacao){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addInnerJoin(Cliente.class.getName(),"c");
		sql.addInnerJoin("c.pessoa","p");
		
		sql.addCriteria("p.nrIdentificacao","=",nrIdentificacao);
		
		return sql;
		
	}

	public List findLookupByUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();
		StringBuffer hql = new StringBuffer("select")
		.append(" c.idCliente")
		.append(", c.pessoa.nrIdentificacao")
		.append(", c.pessoa.nmFantasia")
		.append(" from ")
		.append(Cliente.class.getName()).append(" c")
		.append(" where")
		.append(" c.usuariosCliente.usuario.idUsuario = :idUsuario");
		if (StringUtils.isNotBlank(m.getString("nrIdentificacao")))
			hql.append(" and c.pessoa.nrIdentificacao = :nrIdentificacao");

		List<Object[]> l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[] { "nrIdentificacao", "idUsuario" }, new Object[] { m.getString("pessoa.nrIdentificacao"), u.getIdUsuario() } );

		List<TypedFlatMap> r = new ArrayList<TypedFlatMap>(l.size());
		for(Object[] o : l) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idCliente", o[0]);
			tfm.put("pessoa.nrIdentificacao", o[1]);
			tfm.put("pessoa.nmFantasia", o[2]);
			r.add(tfm);
		}
		return r;
	}

	public ResultSetPage findPaginatedByUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();
		m.put("idUsuario", u.getIdUsuario());

		FindDefinition fd = FindDefinition.createFindDefinition(m);

		StringBuffer hql = new StringBuffer("select")
		.append(" c.idCliente")
		.append(", c.pessoa.nrIdentificacao")
		.append(", c.pessoa.nmFantasia")
		.append(" from ")
		.append(Cliente.class.getName()).append(" c")
		.append(" where")
		.append(" c.usuariosCliente.usuario.idUsuario = :idUsuario");

		if (StringUtils.isNotBlank(m.getString("nrIdentificacao")))
			hql.append(" and c.pessoa.nrIdentificacao like :nrIdentificacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(), m);
		List<Object[]> l = rsp.getList();
		List<TypedFlatMap> r = new ArrayList<TypedFlatMap>();
		for(Object[] o : l) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idCliente", o[0]);
			tfm.put("pessoa.nrIdentificacao", o[1]);
			tfm.put("pessoa.nmFantasia", o[2]);

			r.add(tfm);
		}
		rsp.setList(r);
		return rsp;
	}

	public Integer getRowCountByUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();
		m.put("idUsuario", u.getIdUsuario());

		StringBuffer hql = new StringBuffer("from ")
		.append(Cliente.class.getName()).append(" c")
		.append(" where")
		.append(" c.usuariosCliente.usuario.idUsuario = :idUsuario");
		if(StringUtils.isNotBlank(m.getString("nrIdentificacao")))
			hql.append(" and c.pessoa.nrIdentificacao like :nrIdentificacao");

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), m);
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 14/06/2006
	 * 
	 * @param nrIdentificacao Número de identificacao do cliente
	 * @param tpIdentificacao Tipo de identificação do cliente
	 * @return Lista com clientes aninhados
	 */
	public List findLookupSimplificado(String nrIdentificacao, String tpIdentificacao, String tpSituacao, String tpCliente){
		SqlTemplate sql = montaSqlPadrao(nrIdentificacao, tpIdentificacao, tpSituacao, tpCliente);
		sql.addProjection("c","cliente");

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria(), "+ Use_Nl(pessoa1_,cliente0_)");
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 14/06/2006
	 * 
	 * Query padrão de clientes
	 * @param nrIdentificacao Número de identificacao do cliente
	 * @param tpIdentificacao Tipo de identificação do cliente
	 * @return SqlTemplate com a query padrão e os critérios
	 */
	private SqlTemplate montaSqlPadrao(String nrIdentificacao, String tpIdentificacao, String tpSituacao, String tpCliente) {
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(Cliente.class.getName(),"c");
		sql.addInnerJoin("fetch c.pessoa","p");		

		sql.addCriteria("lower(p.nrIdentificacao)","like",nrIdentificacao.replace(".","").replace("-","").replace("/",""));
		sql.addCriteria("p.tpIdentificacao","=",tpIdentificacao);
		sql.addCriteria("c.tpSituacao","=",tpSituacao);
		sql.addCriteria("c.tpCliente","=",tpCliente);

		return sql;
	}	

	public Cliente findByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c");
		dc.createAlias("c.divisaoClientes","dc");
		dc.createAlias("dc.tabelaDivisaoClientes","tdc");
		dc.createAlias("tdc.parametroClientes","pc");

		dc.setFetchMode("dc.tabelaDivisaoClientes",FetchMode.SELECT);
		dc.setFetchMode("tdc.parametroClientes",FetchMode.SELECT);
		dc.setFetchMode("pc",FetchMode.SELECT);

		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("pc.tpSituacaoParametro", tpSituacaoParametro.getValue()));
		dc.add(Restrictions.ge("pc.dtVigenciaFinal", dtVigenciaFinal));
		dc.add(Restrictions.eq("dc.tpSituacao", tpSituacaoParametro.getValue()));
		
		if(idDivisaoCliente != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		}
		if(idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("tdc.idTabelaDivisaoCliente", idTabelaDivisaoCliente));
		}

		dc.addOrder(Order.asc("dc.idDivisaoCliente"));
		dc.addOrder(Order.asc("tdc.idTabelaDivisaoCliente"));
		dc.addOrder(Order.asc("pc.idParametroCliente"));

		List<Cliente> lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if(lista != null && lista.size() > 0 ) {
			return lista.get(0);
		}
		return null;
	}

	/**
	 * Retorna o campo pcJuroDiario do cliente informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 25/07/2006
	 * 
	 * @param Long idCliente
	 * @return BigDecimal
	 */
	public BigDecimal findPcJuroDiario(Long idCliente){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("cli.pcJuroDiario");
		hql.addFrom(Cliente.class.getName(), "cli");
		hql.addCriteria("cli.id", "=", idCliente);

		return (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna 'true' se a pessoa informada é um cliente ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isCliente(Long idPessoa){
		SqlTemplate hql = mountHqlIsCliente(idPessoa);

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return (result.longValue() > 0);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um cliente especial ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isClienteEspecial(Long idPessoa){
		SqlTemplate hql = mountHqlIsCliente(idPessoa);
		hql.addCustomCriteria("cl.tpCliente IN ('P', 'S')");

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return (result.longValue() > 0);
	}

	/**
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * @param idPessoa
	 * @return
	 */
	private SqlTemplate mountHqlIsCliente(Long idPessoa) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("count(cl.id)");
		hql.addInnerJoin(Cliente.class.getName(), "cl");
		hql.addCriteria("cl.id", "=", idPessoa);
		hql.addCriteria("cl.tpSituacao", "=", "A");

		return hql;
	}

	/**
	 * Retorna a lista de id dos cliente que fazem parte do grupo economico informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * 
	 * @param Long idGrupoEconomico
	 * @return List
	 */
	public List<Long> findIdClienteByGrupoEconomico(Long idGrupoEconomico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("cl.id");
		hql.addInnerJoin(Cliente.class.getName(), "cl");
		hql.addCriteria("cl.grupoEconomico.id", "=", idGrupoEconomico);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Busca os ids das filiais do cliente recebido como parametro.
	 * 
	 * @param idCliente
	 * @return List
	 */
	public List findFiliaisClienteEspecial(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(Projections.property("c.id"))
		.add(Restrictions.eq("clienteMatriz.id", idCliente));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	
	/**
	 * Obtem todos os cliente que possuem filial atende comercial ,
	 * filial cobrança ou filial atende operacional
	 * 
	 * @param idFilial
	 * @return
	 */
	public List findListClientesByFilial(Long idFilial) {
		
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("c.id"), "idCliente")		
		.add(Projections.property("c.regionalComercial.id"), "idRegionalComercial")		
		.add(Projections.property("c.regionalFinanceiro.id"), "idRegionalFinanceiro")		
		.add(Projections.property("c.regionalOperacional.id"), "idRegionalOperacional");		
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(pl)		
		.add(Restrictions.or(Restrictions.eq("c.filialByIdFilialAtendeComercial.id", idFilial),
			Restrictions.or(Restrictions.eq("c.filialByIdFilialCobranca.id", idFilial), 
					Restrictions.eq("c.filialByIdFilialAtendeOperacional.id", idFilial))));
		
		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
				
		return getAdsmHibernateTemplate(). findByDetachedCriteria(dc);		
	}
	
	public List<Map> findListClientesByMunicipio(Long idMunicipio) {
		
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("c.id"), "idCliente")		
		.add(Projections.property("c.regionalComercial.id"), "idRegionalComercial")		
		.add(Projections.property("c.regionalFinanceiro.id"), "idRegionalFinanceiro")		
		.add(Projections.property("c.regionalOperacional.id"), "idRegionalOperacional");		
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(pl)		
		.createAlias("c.pessoa", "p")
		.createAlias("p.enderecoPessoa", "endereco")
		.createAlias("endereco.municipio", "mun")
		.setFetchMode("p", FetchMode.JOIN)
		.setFetchMode("endereco", FetchMode.JOIN)
		.setFetchMode("mun", FetchMode.JOIN)
		.add(Restrictions.eq("mun.id", idMunicipio));
		
		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
				
		return getAdsmHibernateTemplate(). findByDetachedCriteria(dc);
	}	

	/**
	 * Retorna o tipo de cliente e o id da filial comercial do cliente informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 15/01/2007
	 *
	 * @param idCliente
	 * @return map(tpCliente, idFilialComercial)
	 */
	public Map<String, Object> findTpClienteIdFilialComercial(Long idCliente) {
		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append("new Map(");
		hqlFrom.append("  c.tpCliente as tpCliente,");
		hqlFrom.append("  c.filialByIdFilialAtendeComercial.id as idFilialComercial,");
		hqlFrom.append("  c.filialByIdFilialAtendeComercial.dtImplantacaoLMS as dtImplantacaoFilialComercial,");
		hqlFrom.append("  c.filialByIdFilialAtendeOperacional.id as idFilialOperacional,");
		hqlFrom.append("  c.filialByIdFilialAtendeOperacional.dtImplantacaoLMS as dtImplantacaoFilialOperacional,");
		hqlFrom.append("  c.filialByIdFilialCobranca.id as idFilialCobranca,");
		hqlFrom.append("  c.filialByIdFilialCobranca.dtImplantacaoLMS as dtImplantacaoFilialCobranca,");
		hqlFrom.append("  c.regionalComercial.id as idRegionalComercial,");
		hqlFrom.append("  c.regionalOperacional.id as idRegionalOperacional,");
		hqlFrom.append("  c.regionalFinanceiro.id as idRegionalFinanceira");
		hqlFrom.append(")");

		SqlTemplate hql = new SqlTemplate();
		hql.addProjection(hqlFrom.toString());
		hql.addFrom(Cliente.class.getName(), "c");
		hql.addCriteria("c.id", "=", idCliente);

		List<Map<String, Object>> lstCliente = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!lstCliente.isEmpty()){
			return lstCliente.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT sub.id_cliente as idCliente, ");
		sql.append("       sub.nr_identificacao as nrIdentificacao, ");
		sql.append("       sub.tp_cliente, ");
		sql.append("       sub.nm_pessoa as nmPessoa, ");
		sql.append("       sub.nm_fantasia as nmFantasia, ");
		sql.append("       sub.nr_conta as nrConta, ");
		sql.append("       m.nm_municipio as nmMunicipio, ");
		sql.append("       uf.sg_unidade_federativa as sgUnidadeFederativa ");
		

		sql.append(" from (");
		
		sql.append("SELECT c.id_cliente, ");
		sql.append("       c.nr_conta, ");
		sql.append("       c.tp_cliente, ");
		sql.append("       p.nr_identificacao, ");
		sql.append("       p.nm_pessoa, ");
		sql.append("       p.nm_fantasia, ");
		sql.append("       p.id_endereco_pessoa ");
		
		sql.append("  FROM cliente c ");
		sql.append("       inner join pessoa p on p.id_pessoa = c.id_cliente ");
		
		sql.append(" where 1 = 1 ");
		
		Map<String, Object> params = new HashMap<String, Object>();

		String filtroNrIdentificacao = null;
		String nrIdentificacao = "";
		if (filter.get("nrIdentificacao") != null) {
			nrIdentificacao = filter.get("nrIdentificacao").toString();
			if (StringUtils.isNotBlank(nrIdentificacao)) {
				String operador = nrIdentificacao.length() == 8 ? "like" : "=";
				filtroNrIdentificacao = " p.nr_identificacao " + operador + " :nrIdentificacao ";
			}
		}
		
		Long nrConta = null;
		if (filter.get("nrConta") != null) {
			nrConta = (Long)filter.get("nrConta");
		}
		if (nrConta != null && nrConta > 0) {
			
			sql.append(" and (");
			if (filtroNrIdentificacao != null) {
				sql.append(filtroNrIdentificacao).append(" or ");
				params.put("nrIdentificacao", nrIdentificacao + (nrIdentificacao.length() == 8 ? "%" : ""));
			}
			sql.append(" c.nr_conta = :nrConta ");
			sql.append(")");
			
			params.put("nrConta", nrConta);
			
		} else if (filtroNrIdentificacao != null){
			sql.append(" and ").append(filtroNrIdentificacao);
			params.put("nrIdentificacao", nrIdentificacao + (nrIdentificacao.length() == 8 ? "%" : ""));
		}
		
		String nmPessoa = "";
		if (filter.get("nmPessoa") != null) {
			nmPessoa  = filter.get("nmPessoa").toString();
			if (StringUtils.isNotBlank(nmPessoa)) {
				sql.append(" and lower(p.nm_pessoa) like :nmPessoa ");
				params.put("nmPessoa",nmPessoa.toLowerCase()+"%");
			}
		}

		if(null != filter.get("tpCliente") && StringUtils.isNotBlank((String) filter.get("tpCliente"))) {
			sql.append(" and c.tp_cliente = :tpCliente ");
			params.put("tpCliente", filter.get("tpCliente"));
		}

		sql.append(" ) sub ");
		sql.append("       left join endereco_pessoa ep on ep.id_endereco_pessoa = sub.id_endereco_pessoa ");
		sql.append("       left join municipio m on m.id_municipio = ep.id_municipio ");
		sql.append("       left join unidade_federativa uf on uf.id_unidade_federativa = m.id_unidade_federativa ");	
		
		return new ResponseSuggest(sql.toString(), params);
	}
	
	public List<Contato> findContatosFromIdDestinatario(Long idCliente){
		StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT co ");
    	sql.append(" FROM ").append(Cliente.class.getName()).append(" c ");
    	sql.append(" , ").append(Contato.class.getName()).append(" co ");
    	sql.append(" WHERE c.id = co.pessoa.id ");
    	sql.append(" AND c.id = '").append(idCliente).append("'");
    	sql.append(" and co.tpContato in ('AC', 'AD') ");
    	return this.getAdsmHibernateTemplate().find(sql.toString());
	}
	
	public boolean findClientePossuiLiberacaoRodoNoAereo(Long idDoctoServico){
		boolean isPossuiLiberacaoRodoNoAereo = false; 
		
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM ");
		hql.append(DevedorDocServ.class.getName());
		hql.append(" as dev ");		
		hql.append(" JOIN dev.cliente cli ");
		hql.append(" JOIN dev.doctoServico doc ");
		hql.append(" WHERE NVL(cli.blPermiteEmbarqueRodoNoAereo, 'N') = 'N' ");
		hql.append(" AND doc.idDoctoServico = ? ");
		
		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{idDoctoServico});
		
		if (IntegerUtils.isZero(rows)){
			isPossuiLiberacaoRodoNoAereo = true;
		}
		
		return isPossuiLiberacaoRodoNoAereo;
	}

	public boolean findTomadorDoServicoComSeguroDiferenciado(Long idDoctoServico){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(Cliente.class.getName() + " cli ");
		hql.addFrom(DoctoServico.class.getName() + " docto ");
		hql.addFrom(DevedorDocServ.class.getName() + " devedor ");
		
		hql.addCustomCriteria("docto.idDoctoServico = ?");
		hql.addCriteriaValue(idDoctoServico);
		hql.addJoin("devedor.doctoServico.idDoctoServico", "docto.idDoctoServico");
		hql.addJoin("devedor.cliente.idCliente", "cli.idCliente");
		hql.addCustomCriteria("cli.blSeguroDiferenciadoAereo = 'S'");
		
		
    	List lista = super.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if(lista == null || lista.isEmpty()){
    		return false;
    	}else{
    		return true;
    	}
	}

	public List findClienteByNrIdentificacao(List<String> nrIdentificacao) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cli.idCliente from  "+ Cliente.class.getName() + " cli inner join cli.pessoa p ");
		
		if(nrIdentificacao != null && nrIdentificacao.size() > 0){
			sql.append( " where p.nrIdentificacao in ( ");
			StringBuilder sb = new StringBuilder();
			
			Iterator<String> it = nrIdentificacao.iterator();
			while(it.hasNext()){
				it.next();
				sb.append("?");
				
				if(it.hasNext()){
					sb.append(", ");
				}
			}
			
			sql.append(sb.toString()).append(") ");
		}

		return  getAdsmHibernateTemplate().find(sql.toString(), (nrIdentificacao != null ? nrIdentificacao.toArray() : null));
	}

	public List findClienteByNrIdentificacaoForAgrupamento(String nrIdentificacao) {
		String identificacao = nrIdentificacao.replace(".", "").replace("-","").replace("/", "");
		StringBuilder sql = new StringBuilder();
		sql.append("select cli from  "+ Cliente.class.getName() + " cli inner join cli.pessoa p ")
			.append( " where p.nrIdentificacao = ?");

		return  getAdsmHibernateTemplate().find(sql.toString(), identificacao);
	}
	
    public List<Object[]> findOrganization(Map<String, Object> params) {
        params.put("anoMesAnterior", FranqueadoUtils.buscarPrimeiroDiaMesAnterior().getYear());
        params.put("mesAnterior", FranqueadoUtils.buscarPrimeiroDiaMesAnterior().getMonthOfYear());
        params.put("anoAtual", FranqueadoUtils.buscarPrimeiroDiaMesAtual().getYear());
        params.put("mesAtual", FranqueadoUtils.buscarPrimeiroDiaMesAtual().getMonthOfYear());
        return getAdsmHibernateTemplate().findBySql(montaSqlOrganization(), params, configureSqlQueryOrganization());
    }

    private String montaSqlOrganization() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        
        sql.append(" PESSOA.ID_PESSOA as ").append(ID_CLIENTE).append(", ");
        sql.append(" PESSOA.NR_IDENTIFICACAO AS ").append(NR_IDENTIFICACAO).append(", ");
        sql.append(" UPPER(" + PropertyVarcharI18nProjection.createProjection("segmento_mercado.ds_segmento_mercado_i"))
                    .append(") as ").append(SEGMENTO_MERCADO).append(", ");
        sql.append("    (SELECT ");
        sql.append("        PAIS.SG_RESUMIDA ");
        sql.append("    FROM ");
        sql.append("        PAIS, ");
        sql.append("        UNIDADE_FEDERATIVA, ");
        sql.append("        MUNICIPIO ");
        sql.append("    WHERE ");
        sql.append("        MUNICIPIO.ID_MUNICIPIO = ENDERECO_PESSOA.ID_MUNICIPIO ");
        sql.append("        AND UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA = MUNICIPIO.ID_UNIDADE_FEDERATIVA ");
        sql.append("        AND PAIS.ID_PAIS = UNIDADE_FEDERATIVA.ID_PAIS ) as ").append(COUNTRY).append(", ");
        
        sql.append("    (SELECT ");
        sql.append(         PropertyVarcharI18nProjection.createProjection("TIPO_LOGRADOURO.DS_TIPO_LOGRADOURO_I"));
        sql.append("        || ' ' || ENDERECO_PESSOA.DS_ENDERECO ");
        sql.append("    FROM ");
        sql.append("        TIPO_LOGRADOURO ");
        sql.append("    WHERE ");
        sql.append("        TIPO_LOGRADOURO.ID_TIPO_LOGRADOURO = ENDERECO_PESSOA.ID_TIPO_LOGRADOURO) as ").append(ADDRESS1).append(", ");

        sql.append("    (SELECT ");
        sql.append("        ENDERECO_PESSOA.DS_COMPLEMENTO ");
        sql.append("    FROM ");
        sql.append("        TIPO_LOGRADOURO ");
        sql.append("    WHERE ");
        sql.append("        TIPO_LOGRADOURO. ID_TIPO_LOGRADOURO = ENDERECO_PESSOA. ID_TIPO_LOGRADOURO ");
        sql.append("        AND  ENDERECO_PESSOA.DS_COMPLEMENTO IS NOT NULL) as ").append(ADDRESS2).append(", ");
        
        sql.append("    (SELECT ");
        sql.append("        MUNICIPIO.NM_MUNICIPIO ");
        sql.append("    FROM ");
        sql.append("        MUNICIPIO ");
        sql.append("    WHERE ");
        sql.append("        MUNICIPIO.ID_MUNICIPIO = ENDERECO_PESSOA.ID_MUNICIPIO ) as ").append(CITY).append(", ");

        sql.append("    ENDERECO_PESSOA.NR_CEP as ").append(POSTAL_CODE).append(", ");
        
        sql.append("    (SElECT ");
        sql.append("        UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA ");
        sql.append("    FROM ");
        sql.append("        UNIDADE_FEDERATIVA, ");
        sql.append("        MUNICIPIO ");
        sql.append("    WHERE ");
        sql.append("        MUNICIPIO.ID_MUNICIPIO = ENDERECO_PESSOA.ID_MUNICIPIO ");
        sql.append("        AND UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA = MUNICIPIO.ID_UNIDADE_FEDERATIVA ) as ").append(STATE).append(", ");    

        sql.append("    (SELECT ");
        sql.append("        ENDERECO_PESSOA.DS_BAIRRO ");
        sql.append("    FROM ");
        sql.append("        TIPO_LOGRADOURO ");
        sql.append("    WHERE ");
        sql.append("        TIPO_LOGRADOURO. ID_TIPO_LOGRADOURO = ENDERECO_PESSOA. ID_TIPO_LOGRADOURO "); 
        sql.append("        AND ENDERECO_PESSOA.DS_BAIRRO is not null ) as ").append(COUNTY).append(", "); 

        sql.append("    (SELECT ");
        sql.append("        decode(count(*), 0, 0, 1) ");
        sql.append("    FROM ");
        sql.append("        cotacao ");                                                                      
        sql.append("    WHERE ");
        sql.append("        cotacao.id_cliente = cliente.id_cliente ");
        sql.append("        AND cotacao.tp_situacao = 'A' ");
        sql.append("        AND cotacao.dt_validade >= trunc(sysdate)) as ").append(COTACAO_ABERTA).append(", ");
        
        sql.append("    (SELECT ");
        sql.append("        sum(vf.receita_liquida) ");
        sql.append("    FROM ");
        sql.append("        MV_CRM_CLIENTES vf ");
        sql.append("    WHERE ");
        sql.append("        to_number(pessoa.nr_identificacao) = vf.cnpj_cpf_organizacao ");
        sql.append("        AND vf.ano = :anoMesAnterior ");
        sql.append("        AND vf.mes = :mesAnterior ) as ").append(FATURAMENTO_MENSAL).append(", ");

        sql.append("    (SELECT ");
        sql.append("        sum(vf.quantidade_ctes)/4 ");
        sql.append("    FROM ");
        sql.append("        MV_CRM_CLIENTES vf ");
        sql.append("    WHERE ");
        sql.append("        to_number(pessoa.nr_identificacao) = vf.cnpj_cpf_organizacao ");
        sql.append("        AND vf.ano = :anoMesAnterior ");
        sql.append("        AND vf.mes = :mesAnterior ) as ").append(MEDIA_EMBARQUE_SEMANA).append(", ");

        sql.append("    (SELECT ");
        sql.append("        sum(vf.rncs_abertas) ");
        sql.append("    FROM ");
        sql.append("        MV_CRM_CLIENTES vf ");
        sql.append("    WHERE ");
        sql.append("        to_number(pessoa.nr_identificacao) = vf.cnpj_cpf_organizacao ");
        sql.append("        AND vf.ano = :anoAtual "); 
        sql.append("        AND vf.mes = :mesAtual ) as ").append(QTDS_RNC_ABERTAS).append(", ");
        
        sql.append("    (SELECT ");
        sql.append("        sum(vf.rncs_em_aberto) ");
        sql.append("    FROM ");
        sql.append("        MV_CRM_CLIENTES vf ");
        sql.append("    WHERE ");
        sql.append("        to_number(pessoa.nr_identificacao) = vf.cnpj_cpf_organizacao ");
        sql.append("        AND vf.ano = :anoAtual "); 
        sql.append("        AND vf.mes = :mesAtual ) as ").append(QTDS_RNC_EM_ABERTO).append(", ");

        sql.append("    DECODE(cliente.tp_cliente, 'S', 'ESPECIAL', 'E', 'EVENTUAL', 'P', 'POTENCIAL', 'F', 'FILIAL DE CLIENTE ESPECIAL') as ").append(TIPO_CLIENTE).append(", ");

        sql.append("    DECODE(pessoa.tp_pessoa, 'F', 'FISICA', 'J', 'JURIDICA') as ").append(TIPO_PESSOA).append(", ");
        
        sql.append("    pessoa.nm_pessoa as ").append(NOME_PESSOA).append(", ");
        sql.append("    pessoa.nm_fantasia as ").append(NOME_FANTASIA);
        
        sql.append(" FROM ");
        sql.append("    PESSOA PESSOA ");
        sql.append("    INNER JOIN ENDERECO_PESSOA ENDERECO_PESSOA ON ENDERECO_PESSOA.ID_ENDERECO_PESSOA  = PESSOA.ID_ENDERECO_PESSOA ");
        sql.append("    INNER JOIN CLIENTE CLIENTE ON CLIENTE.ID_CLIENTE = PESSOA.ID_PESSOA");
        sql.append("    INNER JOIN FILIAL_CRM ON CLIENTE.ID_FILIAL_ATENDE_COMERCIAL = FILIAL_CRM.ID_FILIAL_CRM ");
        sql.append("    LEFT OUTER JOIN SEGMENTO_MERCADO ON SEGMENTO_MERCADO.ID_SEGMENTO_MERCADO = CLIENTE.ID_SEGMENTO_MERCADO ");
        sql.append(" WHERE ");
        sql.append("    PESSOA.ID_PESSOA in ( :idCliente ) ");
        sql.append("    AND FILIAL_CRM.DT_IMPLANTACAO < SYSDATE ");

        return sql.toString();
    }

    private ConfigureSqlQuery configureSqlQueryOrganization() {
        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar(ID_CLIENTE, Hibernate.LONG);
                sqlQuery.addScalar(NR_IDENTIFICACAO, Hibernate.STRING);
                sqlQuery.addScalar(SEGMENTO_MERCADO, Hibernate.STRING);
                sqlQuery.addScalar(COUNTRY, Hibernate.STRING);
                sqlQuery.addScalar(ADDRESS1, Hibernate.STRING);
                sqlQuery.addScalar(ADDRESS2, Hibernate.STRING);
                sqlQuery.addScalar(CITY, Hibernate.STRING);
                sqlQuery.addScalar(POSTAL_CODE, Hibernate.STRING);
                sqlQuery.addScalar(STATE, Hibernate.STRING);
                sqlQuery.addScalar(COUNTY, Hibernate.STRING);
                sqlQuery.addScalar(COTACAO_ABERTA, Hibernate.BOOLEAN);
                sqlQuery.addScalar(FATURAMENTO_MENSAL, Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar(MEDIA_EMBARQUE_SEMANA, Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar(QTDS_RNC_ABERTAS, Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar(QTDS_RNC_EM_ABERTO, Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar(TIPO_CLIENTE, Hibernate.STRING);
                sqlQuery.addScalar(TIPO_PESSOA, Hibernate.STRING);
                sqlQuery.addScalar(NOME_PESSOA, Hibernate.STRING);
                sqlQuery.addScalar(NOME_FANTASIA, Hibernate.STRING);
            }
        };
        return csq;
    }

    
    public List<Object[]> findCustomersByNrIdentificacao(String nrsIdentificacao) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	 ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
				@Override
				public void configQuery(SQLQuery sqlQuery) {	
					sqlQuery.addScalar("id",Hibernate.LONG);
					sqlQuery.addScalar("cpfcnpj", Hibernate.STRING);
				}
			};
			
        return getAdsmHibernateTemplate().findBySql(montaSqlCustomer(nrsIdentificacao), params, configureSqlQuery);
    }
    
    private String montaSqlCustomer(String nrsIdentificacao) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT P.ID_PESSOA as ID, P.NR_IDENTIFICACAO as CPFCNPJ   ");
        sql.append(" FROM PESSOA P                                             ");
        sql.append(" INNER JOIN CLIENTE C ON P.ID_PESSOA = C.ID_CLIENTE        ");
        sql.append(" WHERE P.NR_IDENTIFICACAO IN(					           ");  
        sql.append(nrsIdentificacao); 
        sql.append(")");  
        return sql.toString();
    }
    
    
    public List<Map<String,Object>> findClienteDivisao(Map criteria){
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select F.SG_FILIAL as FILIAL, ");
		sql.append(" R.DS_REGIONAL as REGIONAL, ");
		sql.append(" U.NM_USUARIO as USUARIO, ");
		sql.append(" P.NR_IDENTIFICACAO as IDENTIFICACAO, ");
		sql.append(" P.NM_PESSOA as RAZAO_SOCIAL, ");
		sql.append(" DV.DS_DIVISAO_CLIENTE as DIVISAO_CLIENTE, ");
		sql.append(" CASE TTP.ID_SERVICO WHEN 1 THEN 'RN' WHEN 2 THEN 'AN' END as MODAL, ");
		sql.append(" CASE C.TP_CLIENTE WHEN 'S' THEN 'ESPECIAL' WHEN 'F' THEN 'FILIAL DE CLIENTE ESPECIAL' WHEN 'P' THEN 'POTENCIAL' WHEN 'E' THEN 'EVENTUAL' END as TP_CLIENTE, ");
		sql.append(" (SELECT PE.NR_IDENTIFICACAO  FROM PESSOA PE WHERE PE.ID_PESSOA = C.ID_CLIENTE_MATRIZ) as CNPJ_MATRIZ, ");
		sql.append(" TTP.TP_TIPO_TABELA_PRECO || TTP.NR_VERSAO || '-' || STP.TP_SUBTIPO_TABELA_PRECO as TABELA_PRECO, ");
		sql.append(" CASE C.BL_CLIENTE_ESTRATEGICO WHEN 'S' THEN 'SIM' ELSE 'NÃO' END as CLIENTE_ESTRATEGICO, ");
		sql.append(" CASE TDC.BL_ATUALIZACAO_AUTOMATICA WHEN 'S' THEN 'AUTOMÁTICO' ELSE 'MANUAL' END as REAJUSTE ");
		
		sql.append(" from PESSOA P, ");
		sql.append(" CLIENTE C, ");
		sql.append(" REGIONAL R, ");
		sql.append(" FILIAL F, ");
		sql.append(" DIVISAO_CLIENTE DV, ");
		sql.append(" TABELA_DIVISAO_CLIENTE TDC, ");
		sql.append(" TABELA_PRECO TP, ");
		sql.append(" TIPO_TABELA_PRECO TTP, ");
		sql.append(" SUBTIPO_TABELA_PRECO STP, ");
		sql.append(" PROMOTOR_CLIENTE PC, ");
		sql.append(" USUARIO U");
		
		sql.append(" where P.ID_PESSOA = C.ID_CLIENTE ");
		sql.append(" AND DV.ID_CLIENTE = P.ID_PESSOA ");
		sql.append(" AND DV.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE ");
		sql.append(" AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO ");
		sql.append(" AND TTP.ID_TIPO_TABELA_PRECO = TP.ID_TIPO_TABELA_PRECO ");
		sql.append(" AND STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO ");
		sql.append(" AND C.ID_FILIAL_ATENDE_COMERCIAL = F.ID_FILIAL ");
		sql.append(" AND C.ID_REGIONAL_COMERCIAL = R.ID_REGIONAL ");
		sql.append(" AND PC.ID_CLIENTE = C.ID_CLIENTE ");
		sql.append(" AND PC.ID_USUARIO = U.ID_USUARIO ");
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		
		if(criteria.get("identificacao") != null){
			sql.append(" AND P.NR_IDENTIFICACAO = :identificacao ");
			parametersValues.put("identificacao", criteria.get("identificacao"));		
		}

		if(criteria.get("filial") != null){
			sql.append(" AND F.ID_FILIAL = :filial ");
			parametersValues.put("filial", criteria.get("filial"));	
		}
		
		if(criteria.get("reajuste") != null){
			sql.append(" AND TDC.BL_ATUALIZACAO_AUTOMATICA = :reajuste ");
			parametersValues.put("reajuste", criteria.get("reajuste"));	
		}
		
		if(criteria.get("divisao") != null){
			sql.append(" AND DV.TP_SITUACAO = :divisao ");
			parametersValues.put("divisao", criteria.get("divisao"));	
		}
		
		if(criteria.get("modal") != null){
			sql.append(" AND TTP.ID_SERVICO = :modal ");
			parametersValues.put("modal", criteria.get("modal"));	
		}
		
		if(criteria.get("tpCliente") != null){
			sql.append(" AND C.TP_CLIENTE = :tpCliente ");
			parametersValues.put("tpCliente", criteria.get("tpCliente"));	
		}
		
		if(criteria.get("estrategico") != null){
			sql.append(" AND C.BL_CLIENTE_ESTRATEGICO = :estrategico ");
			parametersValues.put("estrategico", criteria.get("estrategico"));	
		}
    	
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("FILIAL", Hibernate.STRING);
      			sqlQuery.addScalar("REGIONAL", Hibernate.STRING);
      			sqlQuery.addScalar("USUARIO", Hibernate.STRING);
      			sqlQuery.addScalar("IDENTIFICACAO", Hibernate.STRING);
      			sqlQuery.addScalar("RAZAO_SOCIAL", Hibernate.STRING);
      			sqlQuery.addScalar("DIVISAO_CLIENTE", Hibernate.STRING);
      			sqlQuery.addScalar("MODAL", Hibernate.STRING);
      			sqlQuery.addScalar("TP_CLIENTE", Hibernate.STRING);
      			sqlQuery.addScalar("CNPJ_MATRIZ", Hibernate.STRING);
      			sqlQuery.addScalar("TABELA_PRECO", Hibernate.STRING);
      			sqlQuery.addScalar("CLIENTE_ESTRATEGICO", Hibernate.STRING);
      			sqlQuery.addScalar("REAJUSTE", Hibernate.STRING);
      		}
    	};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, csq);
    }
    
	@SuppressWarnings("unchecked")
	public List<Cliente> findClienteSuggest(final String nrIdentificacao, final String nmPessoa, final Long idCliente, final String tpPessoa, List<String> notINrIdentificacao) {

		StringBuffer sb = new StringBuffer();
		mountSelectClientSuggest(sb);
		sb.append(" where cliente.tpCliente IN ('S','F') ");
		
		mountParamClientSuggest(nrIdentificacao, nmPessoa, idCliente, tpPessoa, notINrIdentificacao, sb);
		
		String[] restricao = {};

		return getAdsmHibernateTemplate().find(sb.toString(), restricao );
	}
	public List<Object[]> findComboRangeData(Long idDivisaoCliente) {
		StringBuilder sql = new StringBuilder();
		
			sql.append("SELECT TO_CHAR(data_Inicial, 'DD/MM/YYYY') || ' - ' || TO_CHAR(data_Final, 'DD/MM/YYYY') as range_Datas ");
			sql.append(" FROM ( ");
			sql.append("SELECT DISTINCT DT_VIGENCIA_INICIAL as data_Inicial , DT_VIGENCIA_FINAL as data_Final ");
	        sql.append(" FROM PARAMETRO_CLIENTE CLI  ");
	        sql.append(" WHERE CLI.ID_TABELA_DIVISAO_CLIENTE =  ");  
	        sql.append( idDivisaoCliente ); 
	        sql.append(" AND CLI.TP_SITUACAO_PARAMETRO IN('A','R')");
	        sql.append("ORDER BY CLI.DT_VIGENCIA_INICIAL )");
	        
	    Map<String, Object> params = new HashMap<String, Object>();
	    ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {	
				sqlQuery.addScalar("range_Datas",Hibernate.STRING);
			}
		};
				
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
	}
	
	public boolean findClienteGerarParcelaFreteVlLiquidoXmlCte(Long idCliente){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(Cliente.class.getName() + " cli ");
		
		hql.addCustomCriteria("cli.idCliente = ?");
		hql.addCriteriaValue(idCliente);
		hql.addCustomCriteria("cli.blGerarParcelaFreteValorLiquido = 'S'");
		
    	List lista = super.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if(lista == null || lista.isEmpty()){
    		return false;
    	}else{
    		return true;
    	}
	}
	
	private void mountSelectClientSuggest(StringBuffer sql) {
		sql.append(" select new " + Cliente.class.getName()) 
		.append("(pessoa.idPessoa, pessoa.nmPessoa, pessoa.nrIdentificacao, filial.idFilial, filial.sgFilial, pessoaFilial.nmFantasia)")
		.append(" from " + Cliente.class.getName() + " as cliente ")
		.append(" inner join cliente.pessoa as pessoa")
		.append(" left join cliente.filialByIdFilialAtendeComercial as filial")
		.append(" left join filial.pessoa as pessoaFilial");
	}
	
	private void mountParamClientSuggest
		(final String nrIdentificacao, final String nmPessoa, final Long idCliente, final String tpPessoa, 
		 List<String> notINrIdentificacao, StringBuffer sb) {
		
		if (LongUtils.hasValue(idCliente)) {
			sb.append(" and pessoa.idPessoa = ").append(idCliente);
		}
		
		if(nrIdentificacao != null && nrIdentificacao.length() > 0){
			sb.append(" and lower(pessoa.nrIdentificacao) like '").append(nrIdentificacao.toLowerCase()).append("'");
		}
		
		if(nmPessoa != null && nmPessoa.length() > 0){
			sb.append(" and lower(pessoa.nmPessoa) like '%").append(nmPessoa.toLowerCase()).append("%'");
		}
		
		if(tpPessoa != null && tpPessoa.length() > 0){
			sb.append(" and pessoa.tpPessoa = '").append(tpPessoa).append("'");
		}
		
		if(notINrIdentificacao != null && notINrIdentificacao.size() > 0){
			sb.append(" and pessoa.nrIdentificacao not in (");
			StringBuilder strNotINrIdentificacao = new StringBuilder("");
			for (String nrIdentificacaoIn : notINrIdentificacao) {
				strNotINrIdentificacao.append(",'").append(nrIdentificacaoIn).append("'");
			}
			
			sb.append(strNotINrIdentificacao.toString().replaceFirst(",", ""));
			sb.append(" ) ");
		}
	}
	
	public List<Cliente> findAllClienteSuggest(final String nrIdentificacao, final String nmPessoa, final Long idCliente, final String tpPessoa, List<String> notINrIdentificacao) {

		StringBuffer sb = new StringBuffer();
		mountSelectClientSuggest(sb);
		sb.append(" where 1=1 ");
		
		mountParamClientSuggest(nrIdentificacao, nmPessoa, idCliente, tpPessoa, notINrIdentificacao, sb);
		
		String[] restricao = {};

		return getAdsmHibernateTemplate().find(sb.toString(), restricao );
	}

	public Boolean isNfeConjulgada(Long cnpjCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT C.blNfeConjulgada FROM ");
		sql.append(Cliente.class.getName()).append(" C, ");
		sql.append(Pessoa.class.getName()).append(" P ");
		sql.append( " WHERE P.nrIdentificacao = ").append(cnpjCliente);
		sql.append(" AND P.idPessoa = C.idCliente AND C.blNfeConjulgada = 'S'");

		List lista = this.getAdsmHibernateTemplate().find(sql.toString());

		return !lista.isEmpty();
	}

	public Boolean isNfeConjulgadaByIdColeta(String idcoleta){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cliente.blNfeConjulgada FROM ");
		sql.append(Conhecimento.class.getName()).append(" conh, ");
		sql.append(DoctoServico.class.getName()).append(" ds, ");
		sql.append(NotaFiscalConhecimento.class.getName()).append(" nfc, ");
		sql.append(VolumeNotaFiscal.class.getName()).append(" vnf, ");
		sql.append(Pessoa.class.getName()).append(" pessRem, ");
		sql.append(Cliente.class.getName()).append(" cliente ");
		sql.append(" WHERE ds.idDoctoServico = conh.id AND nfc.conhecimento.id = conh.id ");
		sql.append(" AND vnf.notaFiscalConhecimento.idNotaFiscalConhecimento = nfc.idNotaFiscalConhecimento AND pessRem.idPessoa = ds.clienteByIdClienteRemetente.idCliente ");
		sql.append(" AND pessRem.idPessoa = cliente.idCliente AND vnf.nrVolumeColeta = '").append(idcoleta).append("'");
		sql.append(" AND cliente.blNfeConjulgada = 'S' ");

		List lista = this.getAdsmHibernateTemplate().find(sql.toString());

		return !lista.isEmpty();
	}

	public List<Object[]> findDiretoriosClientes(){

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cl.email, cl.nmPasta FROM ");
		sql.append(ClienteLayoutEDI.class.getName()).append(" cl ");
		sql.append(" WHERE cl.email IS NOT NULL AND LENGTH(trim(cl.email)) > 0 ");
		sql.append(" AND cl.nmPasta IS NOT NULL AND LENGTH(trim(cl.nmPasta)) > 0 ");

		return this.getAdsmHibernateTemplate().find(sql.toString());
	}
}
