package com.mercurio.lms.sim.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.SimNaoType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SolicitacaoRetiradaDAO extends BaseCrudDao<SolicitacaoRetirada, Long> {

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("remetente", FetchMode.JOIN);
		lazyFindById.put("remetente.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("destinatario", FetchMode.JOIN);
		lazyFindById.put("destinatario.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("consignatario", FetchMode.JOIN);
		lazyFindById.put("consignatario.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("filialRetirada", FetchMode.JOIN);
		lazyFindById.put("filialRetirada.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("usuarioAutorizacao", FetchMode.JOIN);				
		lazyFindById.put("usuarioCriacao", FetchMode.JOIN);	
		
	}
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("remetente", FetchMode.JOIN);
		lazyFindLookup.put("remetente.pessoa", FetchMode.JOIN);
		
		lazyFindLookup.put("destinatario", FetchMode.JOIN);
		lazyFindLookup.put("destinatario.pessoa", FetchMode.JOIN);
		
		lazyFindLookup.put("consignatario", FetchMode.JOIN);
		lazyFindLookup.put("consignatario.pessoa", FetchMode.JOIN);
		
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
		
		lazyFindLookup.put("filialRetirada", FetchMode.JOIN);
		lazyFindLookup.put("filialRetirada.pessoa", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SolicitacaoRetirada.class;
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = montaSqlPaginated((TypedFlatMap) criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaSqlPaginated(criteria);
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}

	private SqlTemplate montaSqlPaginated(TypedFlatMap parametros){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(sr.idSolicitacaoRetirada", "idSolicitacaoRetirada");
		sql.addProjection("sr.nrSolicitacaoRetirada", "nrSolicitacaoRetirada");
		sql.addProjection("sr.tpRegistroLiberacao", "tpRegistroLiberacao");
		sql.addProjection("sr.tpSituacao", "tpSituacao");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("pes_r.nmPessoa", "nmRemetente");
		sql.addProjection("pes_r.tpIdentificacao", "tpIdentificacaoRemetente");
		sql.addProjection("pes_r.nrIdentificacao", "nrIdentificacaoRemetente");
		sql.addProjection("pes_d.nmPessoa", "nmDestinatario");
		sql.addProjection("pes_d.tpIdentificacao", "tpIdentificacaoDestinatario");
		sql.addProjection("pes_d.nrIdentificacao", "nrIdentificacaoDestinatario");
		sql.addProjection("pes_d.nrIdentificacao", "nrIdentificacaoDestinatario");
		sql.addProjection("fr.idFilial", "idFilialRetirada");
		sql.addProjection("fr.sgFilial", "sgFilialRetirada");
		sql.addProjection("pes_f.nmFantasia", "nmFantasiaRetirada");
		sql.addProjection("(case when sr.tpRegistroLiberacao = 'G' then vf.nmFuncionario else sr.nmResponsavelAutorizacao end)", "nmResponsavelAutorizacao");
		sql.addProjection("vf.nrMatricula", "nrMatricula");
		sql.addProjection("vf.nmFuncionario", "nmFuncionario");
		sql.addProjection("sr.dsFuncaoResponsavelAutoriza", "dsFuncaoResponsavelAutoriza");
		sql.addProjection("sr.nrTelefoneAutorizador", "nrTelefoneAutorizador");
		sql.addProjection("sr.nmRetirante", "nmRetirante");
		sql.addProjection("sr.tpIdentificacao", "tpIdentificacao");
		sql.addProjection("sr.nrCnpj", "nrCnpj");
		sql.addProjection("sr.nrRg", "nrRg");
		sql.addProjection("sr.dhPrevistaRetirada", "dhPrevistaRetirada");
		sql.addProjection("sr.dhSolicitacao", "dhSolicitacao");
		sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
		sql.addProjection("me.dhEmissao", "dhEmissao");
		sql.addProjection("me.dhFechamento", "dhFechamento");
		sql.addProjection("filMe.sgFilial", "sgFilialManifesto)");

		sql.addInnerJoin(SolicitacaoRetirada.class.getName(),"sr");
		sql.addInnerJoin("sr.filial", "f");
		sql.addLeftOuterJoin("sr.remetente", "r");
		sql.addLeftOuterJoin("r.pessoa", "pes_r");
		sql.addLeftOuterJoin("sr.destinatario", "d");
		sql.addLeftOuterJoin("d.pessoa", "pes_d");
		sql.addInnerJoin("sr.filialRetirada", "fr");
		sql.addInnerJoin("fr.pessoa", "pes_f");
		sql.addLeftOuterJoin("sr.usuarioAutorizacao", "u");
		sql.addLeftOuterJoin("u.vfuncionario", "vf");
		sql.addLeftOuterJoin("sr.manifestos", "m");
		sql.addLeftOuterJoin("m.manifestoEntrega", "me");
		sql.addLeftOuterJoin("m.filialByIdFilialOrigem", "filMe");

		sql.addCriteria("f.idFilial", "=", parametros.getLong("filial.idFilial"));
		sql.addCriteria("sr.nrSolicitacaoRetirada", "=", parametros.getLong("nrSolicitacaoRetirada"));
		sql.addCriteria("r.idCliente", "=", parametros.getLong("remetente.idCliente"));
		sql.addCriteria("d.idCliente", "=", parametros.getLong("destinatario.idCliente"));
		sql.addCriteria("sr.consignatario.idCliente", "=", parametros.getLong("consignatario.idCliente"));
		sql.addCriteria("fr.idFilial", "=", parametros.getLong("filialRetirada.idFilial"));
		sql.addCriteria("trunc(cast(sr.dhPrevistaRetirada.value as date))", ">=", parametros.getYearMonthDay("dhPrevistaRetiradaInicial"));
		sql.addCriteria("trunc(cast(sr.dhPrevistaRetirada.value as date))", "<=", parametros.getYearMonthDay("dhPrevistaRetiradaFinal"));
		sql.addCriteria("trunc(cast(sr.dhSolicitacao.value as date))", ">=", parametros.getYearMonthDay("dhSolicitacaoInicial"));
		sql.addCriteria("trunc(cast(sr.dhSolicitacao.value as date))", "<=", parametros.getYearMonthDay("dhSolicitacaoFinal"));
		sql.addCriteria("me.idManifestoEntrega", "=", parametros.getLong("manifestoEntrega.idManifestoEntrega"));

		Long idDoctoServico = parametros.getLong("idDoctoServico") != null ? parametros.getLong("idDoctoServico") : null;
		Long idFilialDestino = parametros.getLong("filialDestino.idFilial");
 
		if (idDoctoServico != null || idFilialDestino != null) { 

			StringBuffer exists = new StringBuffer()
			.append("exists (select 1 ")
			.append("       from DocumentoServicoRetirada dsr ")
			.append("       inner join dsr.doctoServico ds ")
			.append("       where dsr.solicitacaoRetirada.id = sr.id ");

			if (idDoctoServico != null) {
				exists.append("  and ds.id = ?");
				sql.addCriteriaValue(idDoctoServico);
			}

			if (idFilialDestino != null) {
				exists.append("  and ds.filialByIdFilialDestino.id = ?");
				sql.addCriteriaValue(idFilialDestino);
			}

			exists.append(")");
			sql.addCustomCriteria(exists.toString());
		}

		sql.addOrderBy("f.sgFilial");
		sql.addOrderBy("sr.nrSolicitacaoRetirada");

		return sql;
	}

	public List findGridDoctoServicoPaginated(Map criteria){
		SqlTemplate sql = montaSqlGridDoctoServico((TypedFlatMap) criteria);
	
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {

				sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico",Hibernate.STRING);

				Properties properties = new Properties();
				properties.put("domainName","DM_TIPO_DOCUMENTO_SERVICO");
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.custom(DomainCompositeUserType.class,properties));

				sqlQuery.addScalar("idDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("idFilialDestinoOperacional",Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("cdLocalizacaoMercadoria",Hibernate.SHORT);
				sqlQuery.addScalar("idFilialLocalizacao",Hibernate.LONG);
				sqlQuery.addScalar("blPrioridadeCarregamento",Hibernate.custom(SimNaoType.class));
				sqlQuery.addScalar("nrNotaFiscal",Hibernate.STRING); 
			} 
		};

		return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1), Integer.valueOf(1000), sql.getCriteria(),csq).getList();
	}

	public Integer getRowCountGridDoctoServico(TypedFlatMap criteria) {
		SqlTemplate sql = montaSqlGridDoctoServico(criteria);
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}

	private SqlTemplate montaSqlGridDoctoServico(TypedFlatMap parametros){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("f.sg_filial", "sgFilialOrigem");
		sql.addProjection("ds.tp_documento_servico", "tpDocumentoServico");
		sql.addProjection("ds.nr_docto_servico", "nrDoctoServico");
		sql.addProjection("ds.id_docto_servico", "idDoctoServico");
		sql.addProjection("ds.id_filial_destino_operacional", "idFilialDestinoOperacional");
		sql.addProjection("p.nm_pessoa", "nmPessoa");
		sql.addProjection("p.tp_identificacao", "tpIdentificacao");
		sql.addProjection("p.nr_identificacao", "nrIdentificacao");	
		sql.addProjection("lm.cd_localizacao_mercadoria", "cdLocalizacaoMercadoria");	
		sql.addProjection("ds.id_filial_localizacao", "idFilialLocalizacao");
		sql.addProjection("ds.bl_prioridade_carregamento", "blPrioridadeCarregamento");
		sql.addProjection("(select min(nfc.nr_nota_fiscal) from nota_fiscal_conhecimento nfc where nfc.id_conhecimento = c.id_conhecimento)", "nrNotaFiscal");

		sql.addFrom("docto_servico", "ds");
		sql.addFrom("filial", "f");
		sql.addFrom("conhecimento", "c");		
		sql.addFrom("cliente", "r");
		sql.addFrom("pessoa", "p");
		sql.addFrom("localizacao_mercadoria", "lm");

		sql.addJoin("ds.id_docto_servico", "c.id_conhecimento (+)");
		sql.addJoin("ds.id_filial_origem", "f.id_filial");
		sql.addJoin("ds.id_cliente_remetente", "r.id_cliente");
		sql.addJoin("r.id_cliente", "p.id_pessoa");
		sql.addJoin("ds.id_localizacao_mercadoria", "lm.id_localizacao_mercadoria (+)");

		sql.addCustomCriteria(new StringBuffer().append("not exists (select 1 \n")
												.append(" from manifesto_entrega_documento med, \n")
												.append("		manifesto_entrega me, \n")
												.append("		manifesto m, \n")
												.append("		ocorrencia_entrega oe \n")
												.append(" where med.id_manifesto_entrega = me.id_manifesto_entrega \n")
												.append("   and me.id_manifesto_entrega = m.id_manifesto \n")
												.append("	and med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega (+) \n")
												.append("   and med.id_docto_servico = ds.id_docto_servico \n")
												.append("   and ((oe.id_ocorrencia_entrega is not null and oe.tp_ocorrencia = 'E') \n")
												.append("        or m.tp_status_manifesto in ('AD', 'FE', 'TC', 'ED', 'DC')))").toString());

		sql.addCustomCriteria("(c.id_conhecimento is null or c.tp_situacao_conhecimento <> 'C')");
		sql.addCriteria("ds.id_cliente_destinatario", "=", parametros.getLong("destinatario.idCliente"));
		sql.addCriteria("lm.cd_Localizacao_Mercadoria", "<>", Short.valueOf("32"));
		sql.addCriteria("ds.id_cliente_consignatario", "=", parametros.getLong("consignatario.idCliente"));
		sql.addCriteria("ds.id_cliente_remetente", "=", parametros.getLong("remetente.idCliente"));

		return sql;
	}

	public boolean verificaLocalizacaoMercadoria(Long idDoctoServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");

		sql.addFrom("DoctoServico ds inner join ds.localizacaoMercadoria lm");

		sql.addCriteria("lm.cdLocalizacaoMercadoria", "=", ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
		sql.addCriteria("ds.id", "=", idDoctoServico);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() > 0);
	}

	public boolean verificaFilialLocalizacaoMercadoria(Long idDoctoServico, Long idFilial){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");

		sql.addFrom("DoctoServico ds");

		sql.addCriteria("ds.id", "=", idDoctoServico);
		sql.addCriteria("ds.filialLocalizacao.idFilial", "=", idFilial);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() > 0);
	}

	public List findDocsBySolicitacaoRetirada(Long idSolicitacaoRetirada){
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(DocumentoServicoRetirada.class.getName(), "dsr");
		sql.addInnerJoin("fetch dsr.doctoServico", "ds");
		sql.addInnerJoin("fetch ds.filialLocalizacao", "fl");
		sql.addInnerJoin("fetch ds.localizacaoMercadoria", "lm");
		sql.addInnerJoin("fetch ds.clienteByIdClienteRemetente", "cr");
		sql.addInnerJoin("fetch cr.pessoa", "p");
		sql.addInnerJoin("fetch ds.filialByIdFilialOrigem", "f");
		sql.addInnerJoin("fetch ds.filialByIdFilialDestino", "fDest");
		sql.addInnerJoin("fetch fDest.pessoa", "pFilial");
		sql.addLeftOuterJoin("fetch dsr.pendencia", "pn");

		sql.addCriteria("dsr.solicitacaoRetirada.id", "=", idSolicitacaoRetirada);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public Integer getRowCountDocsBySolicitacaoRetirada(Long idSolicitacaoRetirada){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");

		sql.addFrom(DocumentoServicoRetirada.class.getName() + " dsr");

		sql.addCriteria("dsr.solicitacaoRetirada.id", "=", idSolicitacaoRetirada);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	public Map findDadosDoctoServico(Long idDoctoServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(ds.idDoctoServico", "idDoctoServico");
		sql.addProjection("ds.nrDoctoServico", "nrDoctoServico");
		sql.addProjection("ds.blPrioridadeCarregamento", "blPrioridadeCarregamento");
		sql.addProjection("fil.idFilial", "idFilial");
		sql.addProjection("fil.sgFilial", "sgFilial");
		sql.addProjection("pes_fil.nmFantasia", "nmFantasia");
		sql.addProjection("cr.idCliente", "idCliente");
		sql.addProjection("p.nmPessoa", "nmPessoa");
		sql.addProjection("p.tpIdentificacao", "tpIdentificacao");
		sql.addProjection("p.nrIdentificacao", "nrIdentificacao)");

		sql.addInnerJoin(DoctoServico.class.getName(), "ds");
		sql.addLeftOuterJoin("ds.clienteByIdClienteRemetente", "cr");
		sql.addLeftOuterJoin("cr.pessoa", "p");
		sql.addInnerJoin("ds.filialByIdFilialDestino", "fil");
		sql.addInnerJoin("fil.pessoa", "pes_fil");

		sql.addCriteria("ds.id", "=", idDoctoServico);

		return (Map) getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria()).get(0);
	}

	public void storeItems(ItemList items) {
		getAdsmHibernateTemplate().deleteAll(items.getRemovedItems());
		getAdsmHibernateTemplate().flush();
		getAdsmHibernateTemplate().saveOrUpdateAll(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();
	}

	public void alteraSituacaoSolicitacaoRetirada(final Long idSolicitacaoRetirada, final String tpSituacao){
		HibernateCallback updateSituacao = new HibernateCallback() {

		public Object doInHibernate(Session session) throws HibernateException, SQLException {

			String update = "update SolicitacaoRetirada as sr set sr.tpSituacao = ? where sr.id = ?";

			session.createQuery(update)
					.setString(0, tpSituacao)
					.setLong(1, idSolicitacaoRetirada.longValue())
					.executeUpdate();			

			return null;
		}
		};

		getAdsmHibernateTemplate().execute(updateSituacao);
	}

	/**
	 * Verifica se o documento de servico ja esta associado a alguma solicitacao de retirada nao cancelada 
	 * 
	 * @param idSolicitacaoRetirada
	 * @param idDoctoServico
	 * @return
	 */
	public boolean validateDoctoServicoInclusao(Long idSolicitacaoRetirada, Long idDoctoServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");
		sql.addInnerJoin(DocumentoServicoRetirada.class.getName(), "dsr");
		sql.addInnerJoin("dsr.solicitacaoRetirada", "sr");
		sql.addCriteria("sr.tpSituacao", "!=", "C");
		sql.addCriteria("dsr.doctoServico.id", "=", idDoctoServico);
		sql.addCriteria("dsr.solicitacaoRetirada.id", "!=", idSolicitacaoRetirada);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() > 0);
	}

	public String findFluxoDoctoServico(Long idDoctoServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ff.dsFluxoFilial");
		sql.addInnerJoin(DoctoServico.class.getName(), "ds");
		sql.addInnerJoin("ds.fluxoFilial", "ff");
		sql.addCriteria("ds.id", "=", idDoctoServico);

		List<String> result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		return ((!result.isEmpty()) ? result.get(0) : null);
	}

	public void removeByIdComplete(java.lang.Long id) {
		SolicitacaoRetirada sr = (SolicitacaoRetirada)findById(id);
		sr.getDocumentoServicoRetiradas().clear();

		getAdsmHibernateTemplate().delete(sr);
	}

	public void removeByIdsComplete(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ; )
			removeByIdComplete((Long)i.next());
	}

	/**
	 * Método que retorna um List de solicitação de retirada pelo ID do DoctoServico.
	 * @param idDoctoServico
	 * @return
	 */
	public List findSolicitacaoRetiradaByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(SolicitacaoRetirada.class.getName() + " sr left join fetch sr.documentoServicoRetiradas dsr " +
														" left join fetch dsr.doctoServico ds ");

		hql.addCustomCriteria(" sr.idSolicitacaoRetirada = ( select max(sr2.idSolicitacaoRetirada) " +
															"  from " + DocumentoServicoRetirada.class.getName() + " dsr2 " +
															"  join dsr2.solicitacaoRetirada sr2 " +
															"  join dsr2.doctoServico doc2 " +
															" where doc2.idDoctoServico = " + idDoctoServico + " ) ");

		hql.addCriteria("ds.id", "=", idDoctoServico);	

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public Object findExistenciaSolicitacaoRetirada(Long idDoctoServico, String tpSituacao) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select count(sore) as quantidade ");
		hql.append("from SolicitacaoRetirada as sore ");
		hql.append("join sore.documentoServicoRetiradas as dosr ");
		hql.append("join dosr.doctoServico as doct ");
		hql.append("where doct.idDoctoServico = ? ");
		hql.append("and sore.tpSituacao = ? ");
				
		params.add(idDoctoServico);
		params.add(tpSituacao);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), params.toArray());
	}

}