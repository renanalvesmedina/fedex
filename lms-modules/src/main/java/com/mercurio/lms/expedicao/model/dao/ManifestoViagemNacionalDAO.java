package com.mercurio.lms.expedicao.model.dao;

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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoViagemNacionalDAO extends BaseCrudDao<ManifestoViagemNacional, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ManifestoViagemNacional.class;
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("manifesto.filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}

	public ResultSetPage findPaginatedManifestoViagem(TypedFlatMap criteria,FindDefinition findDef) {
		SqlTemplate sql = getSqlTemplate(criteria);
		StringBuffer projection = new StringBuffer();

		projection.append("new Map( MVN.idManifestoViagemNacional as idManifestoViagemNacional, ")
				.append(" filialOrigem.idFilial as idFilialOrigem, ")
				.append(" filialOrigem.sgFilial as sgFilialOrigem, ")
				.append(" pessoaFilOrigem.nmFantasia as nmFantasiaFilialOrigem, ")	
				.append(" MVN.nrManifestoOrigem as nrManifestoOrigem, ")		  
				.append(" filialDestino.sgFilial as sgFilialDestino, ")
				.append(" pessoa.nmFantasia as nmFantasia, ")
				.append(" manifesto.tpStatusManifesto as tpStatusManifesto, ")
				.append(" manifesto.dhEmissaoManifesto as dhEmissaoManifesto )");
		sql.addProjection(projection.toString());

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

	public Integer getRowCountManifestoViagem(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap criteria){ 
		SqlTemplate sql = new SqlTemplate();
		StringBuffer from = new StringBuffer();

		from.append(getPersistentClass().getName() +" AS MVN ")
			.append("JOIN MVN.manifesto AS manifesto ")
			.append("JOIN manifesto.controleCarga AS controleC ")
			.append("LEFT OUTER JOIN controleC.proprietario AS proprietario ")
			.append("LEFT OUTER JOIN proprietario.pessoa AS pessoaP ")
			.append("LEFT OUTER JOIN controleC.motorista AS motorista ")
			.append("LEFT OUTER JOIN motorista.pessoa AS pessoaM ")
			.append("LEFT OUTER JOIN controleC.meioTransporteByIdTransportado AS meioTransporteTransportado ")
			.append("LEFT OUTER JOIN controleC.meioTransporteByIdSemiRebocado AS meioTransporteRebocado ")
			.append("JOIN manifesto.filialByIdFilialOrigem AS filialOrigem ")
			.append("JOIN filialOrigem.pessoa AS pessoaFilOrigem ")
			.append("JOIN manifesto.filialByIdFilialDestino AS filialDestino ")
			.append("JOIN filialDestino.pessoa AS pessoa ");
		sql.addFrom(from.toString());

		sql.addCriteria("manifesto.tpManifesto","=","V");
		sql.addCriteria("manifesto.tpAbrangencia","=","N");

		sql.addCriteria("filialOrigem.idFilial", "=", criteria.getLong("filialOrigem.idFilial"));

		if(criteria.get("manifesto") != null && 
			((Map)criteria.get("manifesto")).get("filialByIdFilialOrigem") != null && 
			((Map)((Map)criteria.get("manifesto")).get("filialByIdFilialOrigem")).get("idFilial") != null ){
			sql.addCriteria("filialOrigem.idFilial", "=", Long.parseLong( ((Map)((Map)criteria.get("manifesto")).get("filialByIdFilialOrigem")).get("idFilial").toString()));
		}

		sql.addCriteria("filialDestino.idFilial", "=", criteria.getLong("filialDestino.idFilial"));

		sql.addCriteria("MVN.nrManifestoOrigem", "=", criteria.getInteger("nrManifesto"));

		sql.addCriteria("MVN.nrManifestoOrigem", "=", criteria.getInteger("nrManifestoOrigem"));

		YearMonthDay dtInicial = criteria.getYearMonthDay("dtInicial");
		if(dtInicial != null && StringUtils.isNotBlank(dtInicial.toString())) {
			sql.addCriteria("trunc(manifesto.dhEmissaoManifesto.value)", ">=", dtInicial, YearMonthDay.class);
		}

		Long idDoctoServico = criteria.getLong("idDoctoServico");
		if (idDoctoServico != null) {
			sql.addCustomCriteria("EXISTS" + this.getSqlDocumentos(),idDoctoServico);
		}

		YearMonthDay dtFinal = criteria.getYearMonthDay("dtFinal");
		if(dtFinal != null && StringUtils.isNotBlank(dtFinal.toString())) {
			sql.addCriteria("trunc(manifesto.dhEmissaoManifesto.value)", "<=", dtFinal, YearMonthDay.class);
		}

		sql.addCriteria("manifesto.tpManifestoViagem","=",criteria.getString("manifesto.tpManifestoViagem"));

		sql.addOrderBy("filialOrigem.sgFilial");
		sql.addOrderBy("MVN.nrManifestoOrigem");
		sql.addOrderBy("filialDestino.sgFilial");
		sql.addOrderBy("manifesto.dhEmissaoManifesto.value");
		return sql;
	}

	private String getSqlDocumentos() {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(ManifestoNacionalCto.class.getName(), "MNC_E JOIN MNC_E.manifestoViagemNacional AS MVN_E " +
				"JOIN MNC_E.conhecimento AS CO_E ");

		sql.addCustomCriteria("manifesto = MVN_E.manifesto");
		sql.addCustomCriteria("CO_E.idDoctoServico = ?");

		return "(" + sql.getSql() + ")";
	}

	public ManifestoViagemNacional findByIdManifestoViagem(Long id) {
		StringBuffer hql = new StringBuffer();

		hql.append(" select new Map(");
		hql.append("  mvn.idManifestoViagemNacional as idManifestoViagemNacional,");
		hql.append("  filialOrigem.idFilial as manifesto_filialByIdFilialOrigem_idFilial, ");
		hql.append("  filialOrigem.sgFilial as manifesto_filialByIdFilialOrigem_sgFilial, ");
		hql.append("  mvn.nrManifestoOrigem as nrManifestoOrigem, ");
		hql.append("  filialDestino.idFilial as manifesto_filialByIdFilialDestino_idFilial, ");
		hql.append("  filialDestino.sgFilial as manifesto_filialByIdFilialDestino_sgFilial, ");
		hql.append("  pesFilDes.nmFantasia as manifesto_filialByIdFilialDestino_pessoa_nmFantasia, ");
		hql.append("  m.dhEmissaoManifesto as manifesto_dhEmissaoManifesto, ");
		hql.append("  m.tpStatusManifesto as manifesto_tpStatusManifesto, ");
		hql.append("  prop.idProprietario as manifesto_controleCarga_proprietario_idProprietario, ");
		hql.append("  pesProp.nmPessoa as manifesto_controleCarga_proprietario_pessoa_nmPessoa, ");
		hql.append("  pesProp.nrIdentificacao as manifesto_controleCarga_proprietario_pessoa_nrIdentificacao, ");
		hql.append("  mot.idMotorista as manifesto_controleCarga_motorista_idMotorista, ");
		hql.append("  pesMot.nmPessoa as manifesto_controleCarga_motorista_pessoa_nmPessoa, ");
		hql.append("  pesMot.nrIdentificacao as manifesto_controleCarga_motorista_pessoa_nrIdentificacao, ");
		hql.append("  transTransportado.nrIdentificador as manifesto_controleCarga_meioTransporteByIdTransportado_nrIdentificador, ");
		hql.append("  transTransportado.nrFrota as manifesto_controleCarga_meioTransporteByIdTransportado_nrFrota, ");
		hql.append("  transRebocado.nrIdentificador as manifesto_controleCarga_meioTransporteByIdSemiRebocado_nrIdentificador, ");
		hql.append("  transRebocado.nrFrota as manifesto_controleCarga_meioTransporteByIdSemiRebocado_nrFrota, ");
		hql.append("  cc.idControleCarga as manifesto_controleCarga_idControleCarga, ");
		hql.append("  cc.nrControleCarga as manifesto_controleCarga_nrControleCarga, ");
		hql.append("  filialControleCarga.sgFilial as manifesto_controleCarga_filialByIdFilialOrigem_sgFilial, ");
		hql.append("  filialControleCarga.idFilial as manifesto_controleCarga_filialByIdFilialOrigem_idFilial, ");
		hql.append("  pesFilControleCarga.nmFantasia as manifesto_controleCarga_filialByIdFilialOrigem_pessoa_nmFantasia ");
		hql.append(" ) ");

		hql.append(" from ").append(getPersistentClass().getName()).append(" mvn");
		hql.append(" JOIN mvn.manifesto m");
		hql.append(" JOIN m.controleCarga cc");
		hql.append(" LEFT OUTER JOIN cc.proprietario AS prop ");
		hql.append(" LEFT OUTER JOIN prop.pessoa AS pesProp ");
		hql.append(" LEFT OUTER JOIN cc.motorista AS mot ");
		hql.append(" LEFT OUTER JOIN mot.pessoa AS pesMot ");
		hql.append(" LEFT OUTER JOIN cc.meioTransporteByIdTransportado AS transTransportado ");
		hql.append(" LEFT OUTER JOIN cc.meioTransporteByIdSemiRebocado AS transRebocado ");
		hql.append(" JOIN m.filialByIdFilialOrigem AS filialOrigem ");
		hql.append(" JOIN m.filialByIdFilialDestino AS filialDestino ");
		hql.append(" JOIN cc.filialByIdFilialOrigem AS filialControleCarga ");
		hql.append(" JOIN filialDestino.pessoa AS pesFilDes");
		hql.append(" JOIN filialControleCarga.pessoa AS pesFilControleCarga");

		hql.append(" where mvn.id = ?");

		List<ManifestoViagemNacional> result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{id});
		if(result.size() == 1) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(ManifestoViagemNacional.class);
			result = transformer.transformListResult(result);
			return result.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> findPaginatedCadManifestoViagem(TypedFlatMap criteria) {
		StringBuffer hql = new StringBuffer();
		String criteriaEmbarcado;
		String criteriaReembarcado;

		Long idManifestoViagemNacional = criteria.getLong("idManifestoViagemNacional");

		hql.append(" select new Map(");
		hql.append("  sum(c.qtVolumes) as qtVolumes, ");
		hql.append("  sum(c.psReal) as psReal, ");
		hql.append("  sum(c.psAforado) as psAforado, ");
		hql.append("  sum(c.vlLiquido) as vlLiquido, ");
		hql.append("  sum(c.vlMercadoria) as vlMercadoria, ");
		hql.append("  count(*) as qtConhecimentos ");
		hql.append(" ) ");
		hql.append("  from ").append(ManifestoNacionalCto.class.getName()).append(" MN ");
		hql.append("  JOIN MN.conhecimento c ");
		hql.append("  JOIN c.filialByIdFilialOrigem foc ");
		hql.append("  JOIN MN.manifestoViagemNacional mvn ");
		hql.append("  JOIN mvn.manifesto m ");
		hql.append("  JOIN m.filialByIdFilialOrigem fom ");
		hql.append(" WHERE 	mvn.idManifestoViagemNacional = ? ");

		criteriaEmbarcado = hql.toString()+" AND fom.idFilial = foc.idFilial ";
		criteriaReembarcado = hql.toString()+" AND 	fom.idFilial <> foc.idFilial ";

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = new HashMap<String, Object>();

		data = (Map<String, Object>)getAdsmHibernateTemplate().findUniqueResult(criteriaEmbarcado, new Object[]{idManifestoViagemNacional});
		data.put("totais", "Embarcado");
		result.add(data);

		data = (Map<String, Object>)getAdsmHibernateTemplate().findUniqueResult(criteriaReembarcado,new Object[]{idManifestoViagemNacional});
		data.put("totais", "Reembarcado");
		result.add(data);

		data = (Map<String, Object>)getAdsmHibernateTemplate().findUniqueResult(hql.toString(),new Object[]{idManifestoViagemNacional});
		data.put("totais", "Total");
		result.add(data);

		return result;
	}

	public ResultSetPage findPaginatedConhecimentoManifestoViagem(TypedFlatMap criteria,FindDefinition findDef) {
		DetachedCriteria dc = getSqlTemplateConhecimento(criteria);

		dc.setProjection(
				Projections.projectionList()
					.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
					.add(Projections.property("co.nrConhecimento"), "nrConhecimento")
					.add(Projections.property("co.dvConhecimento"), "dvConhecimento")
					.add(Projections.property("co.vlMercadoria"), "vlMercadoria")
					.add(Projections.property("fd.sgFilial"), "sgFilialDestino")
					.add(Projections.property("co.vlTotalDocServico"), "vlTotalDocServico")
					.add(Projections.property("p.nmPessoa"), "nmPessoa")
					.add(Projections.property("pd.nmPessoa"), "nmPessoaD")
					.add(Projections.property("co.qtVolumes"), "qtVolumes")
					.add(Projections.property("co.psReal"), "psReal")
		);
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc,findDef.getCurrentPage(),findDef.getPageSize());
	}

	public Integer getRowCountConhecimentoManifestoViagem(TypedFlatMap criteria) {
		DetachedCriteria dc = getSqlTemplateConhecimento(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private DetachedCriteria getSqlTemplateConhecimento(TypedFlatMap criteria){ 
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoNacionalCto.class, "MN");
		dc.createAlias("MN.conhecimento","co");
		dc.createAlias("co.filialByIdFilialOrigem","fo");
		dc.createAlias("co.filialByIdFilialDestino","fd");
		dc.createAlias("co.clienteByIdClienteRemetente","cl");
		dc.createAlias("co.clienteByIdClienteDestinatario","cld");
		dc.createAlias("cl.pessoa","p");
		dc.createAlias("cld.pessoa","pd");
		dc.createAlias("MN.manifestoViagemNacional","mvn");

		dc.addOrder(Order.asc("fo.sgFilial"));
		dc.addOrder(Order.asc("co.nrConhecimento"));
		dc.addOrder(Order.asc("fd.sgFilial"));
		dc.addOrder(Order.asc("p.nmPessoa"));

		dc.add(Restrictions.eq("mvn.id", criteria.getLong("idManifestoViagemNacional")));
		return dc;
	}

	public List<ManifestoViagemNacional> findByNrManifestoOrigemByFilial(Integer nrManifestoOrigem, Long idFilial){
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoViagemNacional.class, "mvn");
		dc.createAlias("mvn.manifestoNacionalCtos","co");
		dc.add(Restrictions.eq("mvn.nrManifestoOrigem", nrManifestoOrigem));
		dc.add(Restrictions.eq("mvn.filial.idFilial", idFilial));
		
		return findByDetachedCriteria(dc);
	}

	/**
	 * Método responsável por carregar a data de emissão e a situação do manifesto passado por parâmetro.
	 * @param criteria
	 * @return List contendo o resultado do hql.
	 */
	public List<Map<String, Object>> findLookupManifestoVN(String nrManifestoOrigem, Long idFilial){
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoViagemNacional.class, "mvn");
 
		dc.createAlias("mvn.manifesto","mani");
		dc.createAlias("mvn.manifesto.filialByIdFilialOrigem","filialByIdFilialOrigem");
		
		dc.setProjection(
				Projections.projectionList()
					.add(Projections.property("mani.dhEmissaoManifesto"), "dataEmissao")
					.add(Projections.property("mani.tpStatusManifesto"), "tpStatusManifesto")
					.add(Projections.property("mani.filialByIdFilialOrigem"), "manifesto_filialByIdFilialOrigem")					
					.add(Projections.property("mvn.nrManifestoOrigem"), "nrManifestoOrigem")
					.add(Projections.property("mvn.idManifestoViagemNacional"), "idManifestoViagemNacional"));

		dc.add(Restrictions.eq("mvn.nrManifestoOrigem", Integer.valueOf(nrManifestoOrigem)));
		dc.add(Restrictions.eq("mvn.filial.idFilial", idFilial));

		dc.addOrder(Order.asc("mvn.nrManifestoOrigem"));

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return super.findByDetachedCriteria(dc);
	}

	/**
	 * Método de busca dos dados de manifesto para a Lookup de Manifesto
	 * 
	 * @author José Rodrigo Moraes
	 * @since 07/11/2006
	 * 
	 * @param idFilial Identificador da filial de origem do manifesto
	 * @param nrManifestoOrigem Número do manifesto
	 * @return List contendo o resultado do hql.
	 */
	public List<Map<String, Object>> findLookupManifestoVNSpecific(String nrManifestoOrigem, Long idFilial){
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(ManifestoViagemNacional.class.getName(), "mvn");
		sql.addInnerJoin("mvn.manifesto","mani");
		sql.addInnerJoin("mani.filialByIdFilialOrigem","filOrigem");
		sql.addInnerJoin("filOrigem.pessoa","pesFilOrigem");

		sql.addProjection("new Map(mvn.id as idManifestoViagemNacional, " +
						"        mvn.nrManifestoOrigem as nrManifestoOrigem, " +
						"        mani.id as manifesto_idManifesto, " +
						"        pesFilOrigem.nmFantasia as manifesto_filialByIdFilialOrigem_pessoa_nmFantasia, " +
						"        filOrigem.id as manifesto_filialByIdFilialOrigem_idFilial, " +
						"        filOrigem.sgFilial as manifesto_filialByIdFilialOrigem_sgFilial, " +
						"        mani.dhEmissaoManifesto as dataEmissao, " +
						"        mani.tpStatusManifesto as tpStatusManifesto)");

		Integer manifestoInteger = Integer.valueOf(nrManifestoOrigem);
		
		sql.addCriteria("mvn.nrManifestoOrigem","=",manifestoInteger);
		sql.addCriteria("mvn.filial.idFilial","=",idFilial);

		sql.addOrderBy("mvn.nrManifestoOrigem");

		AliasToTypedFlatMapResultTransformer alias = new AliasToTypedFlatMapResultTransformer();

		List<Map<String, Object>> data = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(data.size());
		for(Map<String, Object> element : data) {
			result.add(alias.transformeTupleMap(element));
		}
		return result;
		
	}

   /**
	* Método (sobrescrito) responsável por carregar dados páginados de acordo com os filtros passados
	* @param criteria
	* @return List contendo o resultado do hql.
	*/
	public ResultSetPage findPaginatedByManifestoViagemNacional(TypedFlatMap criteria){
		SqlTemplate sql = createSqlManifestoViagemNacional(criteria);

		sql.addProjection(
				new StringBuilder()
					.append("new Map( MNC.idManifestoNacionalCto as idManifestoNacionalCto, ")
					.append(" P.nmPessoa as nmPessoa, ")
					.append(" P.nrIdentificacao as nrIdentificacao, ")
					.append(" P.tpIdentificacao as tpIdentificacao, ")
					.append(" FO.sgFilial as sgFilial, ")
					.append(" CO.nrConhecimento as nrConhecimento, ")
					.append(" CO.dhEmissao as data_emissao, ")
					.append(" CO.vlTotalDocServico as valor, ")
					.append(" MNC.blGeraFronteiraRapida as situacao )")
					.toString()
			);

		sql.addOrderBy("FO.sgFilial, CO.nrConhecimento");

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

		/** Itera os registros retornados da consulta para concatenar a sigla da filial com o número do conhecimento(com máscara) */
		List<Map<String, Object>> list = rsp.getList();
		for(Map<String, Object> element : list) {
			/** Formata e concatena a sigla da filial com o número do conhecimento */
			Long nrConhecimento = (Long) element.remove("nrConhecimento");
			element.put("nrConhecimento",FormatUtils.formataNrDocumento(nrConhecimento.toString(),"CTR"));

			/** Formata e concatena o número de identificacao da pessoa com o nome da pessoa */
			DomainValue tpIdentificacao = (DomainValue) element.remove("tpIdentificacao");
			String nrIdentificacao = (String) element.remove("nrIdentificacao");
			String nmPessoa = (String) element.remove("nmPessoa");
			element.put("responsavel", FormatUtils.formatIdentificacao(tpIdentificacao.getValue(), nrIdentificacao) + " - " + nmPessoa);
		}
		return rsp;
	}

	/**
	 * Método responsável por fazer a contagem dos registros que retornam do hql.
	 * @param criteria
	 * @return Integer contendo o número de registros retornados.
	 */
	public Integer getRowCountByManifestoViagemNacional(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlManifestoViagemNacional(criteria);
		sql.addProjection("count(MNC.idManifestoNacionalCto)");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	private SqlTemplate createSqlManifestoViagemNacional(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(ManifestoNacionalCto.class.getName(), "MNC JOIN MNC.manifestoViagemNacional AS MVN " +
				"JOIN MVN.manifesto AS M " +
				"JOIN MNC.conhecimento AS CO " +
				"JOIN CO.clienteByIdClienteRemetente AS C " +
				"JOIN C.pessoa AS P " +
				"JOIN CO.filialByIdFilialOrigem AS FO ");

		Long idManifestoViagemNacional = criteria.getLong("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		Long idDoctoServico = criteria.getLong("doctoServico.idDoctoServico");
		String situacao = criteria.getString("incluidoFronteiraRapida");
		if(idManifestoViagemNacional != null) {
			sql.addCriteria("MVN.idManifestoViagemNacional", "=", idManifestoViagemNacional);
		}
		if(idDoctoServico != null) {
			sql.addCriteria("CO.idDoctoServico", "=", idDoctoServico);
		}
		if(StringUtils.isNotBlank(situacao)) {
			sql.addCriteria("MNC.blGeraFronteiraRapida", "=", situacao);
		}

		return sql;
	}

	/**
	 * Verifica se Existe Conhecimentos para Reembolsar 
	 * que ainda nao possuam Doctos de Reembolso.
	 * 
	 * @param idManifestoViagemNacional
	 * @param blReembolsado
	 * @return
	 */
	public boolean validateManifestoReembolso(Long idManifestoViagemNacional, Boolean blReembolsado) {
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoNacionalCto.class, "mnc");
		dc.setProjection(Projections.rowCount());
		dc.createAlias("mnc.conhecimento", "c");
		dc.add(Restrictions.eq("mnc.manifestoViagemNacional.id", idManifestoViagemNacional));
		dc.add(Restrictions.eq("c.blReembolso", blReembolsado));

		if(Boolean.FALSE.equals(blReembolsado)) {
			DetachedCriteria dcNE = DetachedCriteria.forClass(ReciboReembolso.class, "rr");
			dcNE.setProjection(Projections.property("rr.id"));
			dcNE.add(Restrictions.eqProperty("rr.doctoServicoByIdDoctoServReembolsado.id", "c.id"));

			dc.add(Subqueries.notExists(dcNE));
		}

		Integer result = (Integer)getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * Verifica se Existe Boletos.
	 * 
	 * @param idManifestoViagemNacional
	 * @return
	 */
	public boolean validateManifestoBoleto(Long idManifestoViagemNacional) {
		/** FROM */
		StringBuffer hql = new StringBuffer();
		hql.append(" FROM ").append(Fatura.class.getName()).append(" as f");
		/** WHERE */
		hql.append(" WHERE ").append("f.boleto IS NOT NULL");
		hql.append(" AND f.manifesto.id = ?");

		return CompareUtils.gt(getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(),new Object[]{idManifestoViagemNacional}), IntegerUtils.ZERO);
	}

	public List<ManifestoViagemNacional> findByAdiantamentoTrecho(Long idControleCarga, Long idFilialOrigem) {
		StringBuffer sql = new StringBuffer()
		.append("from ").append(ManifestoViagemNacional.class.getName()).append(" as mvn ")
		.append("inner join fetch mvn.manifesto manifesto ")
		.append("inner join fetch manifesto.filialByIdFilialOrigem filialOrigem ")
		.append("inner join fetch manifesto.filialByIdFilialDestino filialDestino ")
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("and manifesto.tpManifesto = 'V' ")
		.append("and manifesto.tpStatusManifesto <> 'CA' ")
		.append("and manifesto.filialByIdFilialOrigem.id = ? ");

		return super.getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga, idFilialOrigem});
	}
	
	public List<ManifestoViagemNacional> findByFilialDestino(Long idControleCarga, Long idFilialDestino) {
		StringBuffer sql = new StringBuffer().append("select mvn ")
		.append("from ").append(ManifestoViagemNacional.class.getName()).append(" as mvn ")
		.append("inner join  mvn.manifesto manifesto ")
		.append("inner join  manifesto.filialByIdFilialDestino filialDestino ")
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("order by manifesto.idManifesto asc");

		return super.getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
	}


	public List findLookupFilterDoctoServico(Map criteria) {
		
		SqlTemplate sql = getSqlTemplate(new TypedFlatMap(criteria));
		sql.addProjection("MVN");
		return getAdsmHibernateTemplate().find(sql.getSql(false),sql.getCriteria());
	}
	
	/** Nao realizado mapeamento desta tabela pois a manipulacao da mesma é feita pela integração. */
	public void removeIntManifestoViagemDestino(Long IdManifestoViagemNacional) {
		getSession()
		.createSQLQuery("DELETE INT_MANIFESTO_VIAGEM_DESTINO WHERE ID_MANIFESTO_VIAGEM_NACIONAL = :IdManifestoViagemNacional ")
			.setParameter("IdManifestoViagemNacional", IdManifestoViagemNacional).executeUpdate();
				
	}

	public ManifestoNacionalVolume findByIdManifestoViagemNacionalVinculadoDocServico(Long idManifestoViagemNacDescarregado, Long idVolumeNotaFiscalLido) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT mnv");
		hql.append("  FROM " + ManifestoNacionalVolume.class.getName() + " mnv");
		hql.append("  JOIN mnv.manifestoNacionalCto mnc");
		hql.append("  JOIN mnc.manifestoViagemNacional mvn");
		hql.append("  JOIN mnv.volumeNotaFiscal vnf");
		hql.append("  JOIN mnc.manifestoViagemNacional4 mvnc");
		hql.append(" WHERE mvn.idManifestoViagemNacional = ? ");//MANIFESTO_NACIONAL_VOLUME.ID_MANIFESTO_VIAGEM_NACIONAL = ID_MANIFESTO_VIAGEM_NAC que está sendo descarregado
		hql.append("   AND vnf.idVolumeNotaFiscal = ? ");//MANIFESTO_NACIONAL_VOLUME.ID_VOLUME_NOTA_FISCAL = ID_VOLUME_NOTA_FISCAL do volume que foi lido 
		hql.append("   AND mvnc.idManifestoViagemNacional = ? ");//MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = ID_MANIFESTO_VIAGEM_NAC  que está sendo descarregado. 
		
		ManifestoNacionalVolume manifestoNacionalVolume = 
				(ManifestoNacionalVolume) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), 
						new Object [] {idManifestoViagemNacDescarregado, idVolumeNotaFiscalLido, idManifestoViagemNacDescarregado});
		
		return manifestoNacionalVolume;
	}

	public List<Map<String, Object>> findManifestoViagemNacionalSuggest(final String sgFilial, final Long nrManifestoOrigem, final Long idEmpresa) {
final StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT mvn.id_manifesto_viagem_nacional, ");
		sql.append("       fo.sg_filial, "); 
		sql.append("       mvn.nr_manifesto_origem ");
		
		sql.append("  FROM manifesto_viagem_nacional mvn ");
		sql.append("       inner join filial fo on fo.id_filial = mvn.id_filial ");
		
		sql.append(" WHERE fo.sg_filial = :sgFilial ");
		sql.append("   and mvn.nr_manifesto_origem = :nrManifestoOrigem ");
		if (idEmpresa != null) {
			sql.append("   and fo.id_empresa = :idEmpresa ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_manifesto_viagem_nacional", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_manifesto_origem", Hibernate.LONG);
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setString("sgFilial", sgFilial);
				query.setLong("nrManifestoOrigem", nrManifestoOrigem);
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
			map.put("idManifestoViagem", o[0]);
			map.put("sgFilial", o[1]);
			map.put("nrManifestoOrigem", o[2]);
			toReturn.add(map);
			
		}
		
		return toReturn;
	}
	
	public ManifestoViagemNacional findByConhecimento(Conhecimento conhecimento, Filial filialFedex){
		String hql = "select mvn from ManifestoNacionalCto mnc "+
					"join mnc.manifestoViagemNacional mvn "+
					"join fetch mvn.manifesto m "+
					"join m.controleCarga cc "+
					"where mnc.conhecimento = :conhecimento " +	
					"and m.filialByIdFilialDestino = :filialFedex " +
					"and m.tpStatusManifesto = 'ED' " +
					"and cc.tpStatusControleCarga not in ('FE','CA')";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filialFedex", filialFedex);
		params.put("conhecimento", conhecimento);
		
		List<ManifestoViagemNacional> manifestos = getAdsmHibernateTemplate().findByNamedParam(hql, params);
		if (manifestos != null && manifestos.size() > 0){
			return manifestos.get(0);
		}
		return null;
	}
	
	public Long findCountConhecimentosEmDescargaControleCarga(ControleCarga controleCarga, Filial filial){
		String hql = "select c from ManifestoNacionalCto mnc "+
				"join mnc.manifestoViagemNacional mvn "+
				"join mvn.manifesto m "+
				"join m.controleCarga cc "+
				"join mnc.conhecimento c "+
				"where m.controleCarga = :controleCarga " +	
				"and c.localizacaoMercadoria.cdLocalizacaoMercadoria = 34 "+
				"and c.filialLocalizacao = :filial ";
				
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("controleCarga", controleCarga);
		params.put("filial", filial);
		

		List documentos = getAdsmHibernateTemplate().findByNamedParam(hql, params);
		if (documentos != null ){
			return Long.valueOf(documentos.size());
		}	
		return 0L;
	}
	
	
}