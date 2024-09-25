package com.mercurio.lms.vendas.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.tntbrasil.integracao.domains.sms.CnpjUsuarioDMN;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.util.ConstantesCotacao;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CotacaoDAO extends BaseCrudDao<Cotacao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Cotacao.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("municipioByIdMunicipioEntrega", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	public List findCotacoes(Long idCliente, String tpDocumentoCotacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idCotacao"), "idCotacao")
			.add(Projections.property("c.nrCotacao"), "nrCotacao")
			.add(Projections.property("c.nmClienteDestino"), "nmClienteDestino")
			.add(Projections.property("f.sgFilial"), "sgFilial");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c");
		dc.setProjection(pl);

		dc.createAlias("c.filial", "f");

		if (StringUtils.isNotBlank(tpDocumentoCotacao)) {
			dc.add(Restrictions.eq("c.tpDocumentoCotacao", tpDocumentoCotacao));
		}
		dc.add(Restrictions.ge("c.dtValidade", JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.or(
				Restrictions.eq("c.tpSituacao", "O"), 
				Restrictions.eq("c.tpSituacao", "A"))
		);
		dc.add(
			Restrictions.or(
				Restrictions.eq("c.clienteByIdClienteSolicitou.id", idCliente),
				Restrictions.isNull("c.clienteByIdClienteSolicitou.id")
			)
		);

		dc.addOrder(Order.asc("f.sgFilial"));
		dc.addOrder(Order.asc("c.nrCotacao"));

		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);

		return findByDetachedCriteria(dc);
	}

	public List findCotacaoByIdClienteTpSituacaoNotas(Long idCliente, String tpSituacao, List nrNotas) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("c.idCotacao"), "idCotacao")
		.add(Projections.property("c.municipioByIdMunicipioOrigem.idMunicipio"), "idMunicipio");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(pl)
		.add(Restrictions.ge("c.dtValidade", JTDateTimeUtils.getDataAtual()))
		.add(Restrictions.eq("c.tpSituacao", tpSituacao))
		.add(Restrictions.eq("c.clienteByIdClienteSolicitou.id", idCliente))
		.add(Restrictions.in("c.nrNotaFiscal", nrNotas))
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public Map findByIdCtrc(Long idCotacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idCotacao"), "idCotacao")
			.add(Projections.property("c.psReal"), "psReal")
			.add(Projections.property("c.tpFrete"), "tpFrete")
			.add(Projections.property("c.psCubado"), "psCubado")
			.add(Projections.property("c.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("c.municipioByIdMunicipioOrigem.idMunicipio"), "idMunicipioOrigem")
			.add(Projections.property("c.municipioByIdMunicipioDestino.idMunicipio"), "idMunicipioDestino")
			.add(Projections.property("c.clienteByIdClienteSolicitou.idCliente"), "idClienteSolicitou")
			.add(Projections.property("c.clienteByIdClienteDestino.idCliente"), "idClienteDestino")
			.add(Projections.property("c.clienteByIdCliente.idCliente"), "idCliente");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.setProjection(pl)
			.add(Restrictions.idEq(idCotacao))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (Map)l.get(0);
		return null;
	}

	public TabelaPreco findTabelaPrecoById(Long idTabelaTreco){
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(TabelaPreco.class.getName()).append(" as tp ")
    	.append(" inner join fetch tp.subtipoTabelaPreco stp ")
		.append(" inner join fetch tp.moeda m ")
		.append(" inner join fetch tp.tipoTabelaPreco ttp ")
    	.append(" where ")
    	.append(" tp.id = ? ");
		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sql.toString(),new Long[]{idTabelaTreco});	
	}
	
	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select pojo.idCotacao " +
			"from "+ Cotacao.class.getName() + " as pojo " +
			"join pojo.doctoServico as ds " +
			"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}

	public ResultSetPage findCotacaoPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = getQuery(criteria);
		sql.addFrom(Cotacao.class.getName() + " as c " +
				"join c.filial as f " +
				"join c.servico as s ");
		sql.addProjection("new map(c.idCotacao as idCotacao, " +
				" (f.sgFilial || ' ' || c.nrCotacao) as nrCotacao, " +
				"c.dtGeracaoCotacao as dtGeracaoCotacao, " +
				"c.tpSituacao as tpSituacao, " +
				"c.tpDocumentoCotacao as tpDocumentoCotacao, " +
				"c.nmClienteRemetente as nmClienteRemetente, " +
				"c.nmClienteDestino as nmClienteDestinatario, " +
				"c.nmResponsavelFrete as nmCliente," +
				"s.dsServico as dsServico)");
		sql.addOrderBy("f.sgFilial");
		sql.addOrderBy("c.nrCotacao");

		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), def.getCurrentPage(), def.getPageSize(), sql.getCriteria());
	}

	private SqlTemplate getQuery(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addCriteria("c.filialByIdFilialOrigem.id","=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("c.clienteByIdClienteSolicitou.id", "=", criteria.getLong("clienteByIdClienteSolicitou.idCliente"));
		sql.addCriteria("c.filialByIdFilialDestino.id","=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("c.clienteByIdClienteDestino.id", "=", criteria.getLong("clienteByIdClienteDestino.idCliente"));
		sql.addCriteria("f.id","=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("c.clienteByIdCliente.id", "=", criteria.getLong("clienteByIdCliente.idCliente"));
		sql.addCriteria("c.nrCotacao", "=", criteria.getInteger("nrCotacao"));
		sql.addCriteria("c.nrNotaFiscal", "=", criteria.getInteger("nrNotaFiscal"));
		sql.addCriteria("c.dtGeracaoCotacao", ">=", criteria.getYearMonthDay("dtGeracaoCotacaoInicial"));
		sql.addCriteria("c.dtGeracaoCotacao", "<=", criteria.getYearMonthDay("dtGeracaoCotacaoFinal"));
		sql.addCriteria("c.tpDocumentoCotacao", "=", criteria.getString("tpDocumentoCotacao"));
		return sql;
	}

	public Integer getRowCountCotacao(TypedFlatMap criteria) {
		SqlTemplate sql = getQuery(criteria);
		sql.addFrom(Cotacao.class.getName() + " as c join c.filial as f ");
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * Método que retorna uma lista de Cotações com data validade >= data atual
	 * 
	 * @param criteria
	 */
	public List<Object[]> findCotacoesByUserAndTipoSituacaoPaginated(Map criteria){
		Long[] idCotacoes  = (Long[]) criteria.get("idCotacoes");
		Long[] idClientes = (Long[]) criteria.get("idClientes");
		String tpSituacao  = (String) criteria.get("tpSituacao");
		String order = getQueryOrderBy((String) criteria.get("order"));
		Integer firstResult  = (Integer) criteria.get("firstResult");
		Integer maxResult  = (Integer) criteria.get("maxResult");
		
		String paramIdsCotacoes = "";
		for (int i = 0; i < idCotacoes.length; i++) {
			paramIdsCotacoes += idCotacoes[i] + ",";
			if(i == idCotacoes.length - 1){
				paramIdsCotacoes = paramIdsCotacoes.substring(0, paramIdsCotacoes.length() - 1);
			}
		}
		
		StringBuilder sql = new StringBuilder()
		.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ FROM ( ")
		.append("SELECT COT.ID_COTACAO AS ID_COTACAO")
		.append(", FO.SG_FILIAL || ' - ' || COT.NR_COTACAO AS DS_COTACAO")
		.append(", COT.VL_TOTAL_COTACAO AS VL_TOT_COT")
		.append(", TO_CHAR(COT.DT_GERACAO_COTACAO , 'dd/mm/yyyy') AS DATA_GERACAO")
		.append(", TO_CHAR(COT.DT_VALIDADE , 'dd/mm/yyyy') AS DATA_VALIDADE")
		.append(", PR.NR_IDENTIFICACAO AS NR_IDENT_REM")
		.append(", PR.NM_PESSOA AS NM_PES_REM")
		.append(", COT.NR_IDENTIF_CLIENTE_REM AS NR_IDENT_REM_COT")
		.append(", COT.NM_CLIENTE_REMETENTE AS NM_PES_REM_COT")
		.append(", MO.NM_MUNICIPIO || ' - ' || UFO.SG_UNIDADE_FEDERATIVA AS ORIGEM")
		.append(", PD.NR_IDENTIFICACAO AS NR_IDENT_DEST")
		.append(", PD.NM_PESSOA AS NM_PES_DEST")
		.append(", COT.NR_IDENTIF_CLIENTE_DEST  AS NR_IDENT_DEST_COT")
		.append(", COT.NM_CLIENTE_DESTINO AS NM_PES_DEST_COT")
		.append(", MD.NM_MUNICIPIO || ' - ' || UFD.SG_UNIDADE_FEDERATIVA AS DESTINO")
		.append(", COT.NR_NOTA_FISCAL AS NR_NOTA_FISCAL")
		.append(", COT.PS_REAL AS PS_REAL")
		.append(", COT.PS_CUBADO AS PS_CUBADO")
		.append(", COT.VL_MERCADORIA AS VL_MERCADORIA")
		.append(" FROM ")
		.append("COTACAO COT")
		.append(", PESSOA PR")
		.append(", PESSOA PD")
		.append(", FILIAL FO")
		.append(", MUNICIPIO MO")
		.append(", MUNICIPIO MD")
		.append(", UNIDADE_FEDERATIVA UFO")
		.append(", UNIDADE_FEDERATIVA UFD")
		.append(" WHERE ")
		.append(" COT.DT_VALIDADE >= sysdate")
		.append(" AND COT.TP_SITUACAO = :tpSituacao")
		.append(" AND COT.TP_MODO_COTACAO = 'WE'")
		.append(" AND COT.ID_FILIAL_ORIGEM = FO.ID_FILIAL")
		.append(" AND COT.ID_CLIENTE_SOLICITOU = PR.ID_PESSOA(+)")
		.append(" AND MO.ID_MUNICIPIO = COT.ID_MUNICIPIO_ORIGEM")
		.append(" AND UFO.ID_UNIDADE_FEDERATIVA = MO.ID_UNIDADE_FEDERATIVA")
		.append(" AND COT.ID_CLIENTE_DESTINO  = PD.ID_PESSOA(+)")
		.append(" AND MD.ID_MUNICIPIO = COT.ID_MUNICIPIO_DESTINO")
		.append(" AND UFD.ID_UNIDADE_FEDERATIVA = MD.ID_UNIDADE_FEDERATIVA")
		.append(" AND PR.ID_PESSOA IN (:idClientes)")
		.append(" AND COT.ID_COTACAO IN ("+paramIdsCotacoes+")")		
		.append(" ORDER BY "+order+")")
		.append(" row_ ) WHERE  rownum_ > :firstResult and ROWNUM <= :maxResult");
		

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {	
				sqlQuery.addScalar("ID_COTACAO",Hibernate.LONG);
				sqlQuery.addScalar("DS_COTACAO");
				sqlQuery.addScalar("VL_TOT_COT");
				sqlQuery.addScalar("DATA_GERACAO");
				sqlQuery.addScalar("DATA_VALIDADE");
				sqlQuery.addScalar("NR_IDENT_REM");
				sqlQuery.addScalar("NM_PES_REM");
				sqlQuery.addScalar("NR_IDENT_REM_COT");
				sqlQuery.addScalar("NM_PES_REM_COT");
				sqlQuery.addScalar("ORIGEM");
				sqlQuery.addScalar("NR_IDENT_DEST");
				sqlQuery.addScalar("NM_PES_DEST");
				sqlQuery.addScalar("NR_IDENT_DEST_COT");
				sqlQuery.addScalar("NM_PES_DEST_COT");
				sqlQuery.addScalar("DESTINO");
				sqlQuery.addScalar("NR_NOTA_FISCAL");
				sqlQuery.addScalar("PS_REAL");
				sqlQuery.addScalar("PS_CUBADO");
				sqlQuery.addScalar("VL_MERCADORIA");
				
			}
		};

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpSituacao", tpSituacao);
		params.put("idClientes", idClientes);
		params.put("maxResult", maxResult);
		params.put("firstResult", firstResult);
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
	}
	

	private String getQueryOrderBy(String orderBy) {
		String ordenacao = ConstantesCotacao.DATA_HORA;
		if(orderBy.equalsIgnoreCase(ConstantesCotacao.DATA_HORA)){
			ordenacao = "DATA_GERACAO";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.COTACAO)){
			ordenacao = "DS_COTACAO";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.DESTINATARIO)){
			ordenacao = "NM_PES_DEST_COT";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.NOTA_FISCAL)){
			ordenacao = "NR_NOTA_FISCAL";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.ORIGEM)){
			ordenacao = "ORIGEM";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.DESTINO)){
			ordenacao = "DESTINO";
		}else if(orderBy.equalsIgnoreCase(ConstantesCotacao.VALIDADE)){
			ordenacao = "DATA_VALIDADE";
		}
		return ordenacao;
	}

	public void flush() {
		getAdsmHibernateTemplate().flush();
	}

	public void updateCotacaoByWorkflowDimensoes(Map<String, Object> parametersValues) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE cotacao SET ");
		sql.append("tp_situacao_aprova_dimensoes = :tpSituacaoAprovacaoDimensoes ");
		
		if(parametersValues.get("idUsuarioAprovouDimensoes") != null){
			sql.append(",id_usuario_aprovou_dimensoes = :idUsuarioAprovouDimensoes ");
		} else {
			sql.append(",id_usuario_aprovou_dimensoes = null ");
		}
		
		if(parametersValues.get("dtAprovacaoDimensoes") != null){
			sql.append(",dt_aprovacao_dimensoes = :dtAprovacaoDimensoes ");
		} else {
			sql.append(",dt_aprovacao_dimensoes = null ");
		}
		
		if(parametersValues.get("tpSituacao") != null){
			sql.append(",tp_situacao = :tpSituacao ");	
		}
		
		sql.append("WHERE id_cotacao = :idCotacao ");
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	public Map findCotacaoById(Long id) {
		
		List l = getAdsmHibernateTemplate()
			.findByNamedQueryAndNamedParam(ConstantesVendas.FIND_COTACAO_BY_ID, "idCotacao", id);		
		if(!l.isEmpty())
			return AliasToNestedMapResultTransformer.getInstance().transformeTupleMap((Map)l.get(0));
		return null;
	}

	public boolean validateExclusao(List ids) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
			.setProjection(Projections.count("idCotacao"))
			.add(Restrictions.in("id", ids))
			.add(Restrictions.ne("tpSituacao", "T"));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc).intValue() == 0;
	}

	/**
	 * Método que retorna uma Cotação a partir do seu ID.
	 * 
	 * @param idCotacao
	 * @return
	 */
	public Cotacao findCotacaoByIdCotacao(Long idCotacao) {		
		SqlTemplate sqlTemp = new SqlTemplate();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		sqlTemp.addFrom(Cotacao.class.getName() + " cotacao join fetch cotacao.servico se " +
												" join fetch cotacao.filialByIdFilialOrigem fo " +
												" left join fetch cotacao.naturezaProduto np " +
												" join fetch cotacao.municipioByIdMunicipioDestino md " +
												" left join fetch cotacao.clienteByIdClienteSolicitou cli " +
												" left join fetch cli.pessoa pes " +
												" join fetch cotacao.moeda moe ");

		sqlTemp.addCriteria("cotacao.idCotacao", "=", idCotacao);

		return (Cotacao) getAdsmHibernateTemplate().findUniqueResult(sqlTemp.getSql(), sqlTemp.getCriteria());
	}

	/**
	 * Método que retorna uma Cotação a partir do ID da Filial Origem e do Número da Cotação.
	 * 
	 * @param idFilialOrigem
	 * @param nrCotacao
	 * @return Cotacao
	 */
	//FIXME POSTERIORMENTE MUDAR NOME PARA findCotacaoByIdFilialOrigemByNrCotacao, POIS A FILIAL DA COTACAO É APENAS "FILIAL", E NAO "FILIALORIGEM"
	public Cotacao findCotacaoByIdFilialOrigemByNrCotacao(Long idFilialOrigem, Integer nrCotacao) {		
		StringBuffer hql = new StringBuffer();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.append(" from ").append(Cotacao.class.getName()).append(" c");
		hql.append(" join fetch c.servico se");
		hql.append(" left join fetch c.naturezaProduto np");
		hql.append(" join fetch c.municipioByIdMunicipioOrigem mo");
		hql.append(" join fetch c.municipioByIdMunicipioDestino md");
		hql.append(" left join fetch c.clienteByIdCliente cli");
		hql.append(" left join fetch cli.pessoa pes");
		hql.append(" left join fetch c.clienteByIdClienteDestino clidest");
		hql.append(" left join fetch clidest.pessoa clidestpes");
		hql.append(" left join fetch c.clienteByIdClienteSolicitou clisolic");
		hql.append(" left join fetch clisolic.pessoa clisolicpes");		
		hql.append(" join fetch c.moeda moe");

		hql.append(" where c.filial.id = ?");
		hql.append("   and c.nrCotacao = ?");

		return (Cotacao) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idFilialOrigem, nrCotacao});
	}

	public List<Cotacao> findPocByDepotAndLastID(Long idDepot, Long idLast) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from ").append(Cotacao.class.getName()).append(" c");
		hql.append(" where c.filial.id = ?");
		hql.append("   and c.idCotacao > ?");
		return getAdsmHibernateTemplate().find(hql.toString(), new Long[]{idDepot, idLast});
	}

	public List<Map<String, Object>> findCotacaoSuggest(final String sgFilial, final Long nrCotacao, final Long idEmpresa) {
		final StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT c.id_cotacao, ");
		sql.append("       fo.sg_filial as sg_filial, "); 
		sql.append("       c.nr_cotacao, ");
		sql.append("       c.dt_geracao_cotacao ");
		
		sql.append("  FROM cotacao c ");
		sql.append("       inner join filial fo on fo.id_filial = c.id_filial ");
		
		sql.append(" WHERE fo.sg_filial = :sgFilial ");
		sql.append("   and c.nr_cotacao = :nrCotacao ");
		if (idEmpresa != null) {
			sql.append("   and fo.id_empresa = :idEmpresa ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_cotacao", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_cotacao", Hibernate.LONG);
				sqlQuery.addScalar("dt_geracao_cotacao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setString("sgFilial", sgFilial);
				query.setLong("nrCotacao", nrCotacao);
				if (idEmpresa != null) {
					query.setLong("idEmpresa", idEmpresa);
				}
            	csq.configQuery(query);
				return query.list();
			}
		};
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("idCotacao", o[0]);
			map.put("sgFilial", o[1]);
			map.put("nrCotacao", o[2]);
			map.put("dtGeracaoCotacao", o[3]);
			toReturn.add(map);
			
		}
		
		return toReturn;
	}
}