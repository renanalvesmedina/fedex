package com.mercurio.lms.municipios.model.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.expedicao.model.DoctoServico;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.dto.FilialAtendimentoDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.FotoFilial;
import com.mercurio.lms.municipios.model.GrupoClassificacaoFilial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.tracking.City;
import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.tracking.State;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.Acao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class FilialDAO extends BaseCrudDao<Filial, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */

	private JdbcTemplate jdbcTemplate;

	protected Class getPersistentClass() {
		return Filial.class;
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa", FetchMode.JOIN);
		fetchModes.put("empresa", FetchMode.JOIN);
		fetchModes.put("empresa.pessoa", FetchMode.JOIN);
		fetchModes.put("franqueado", FetchMode.JOIN);
		fetchModes.put("franqueado.pessoa", FetchMode.JOIN);
		fetchModes.put("cedenteByIdCedenteBloqueto", FetchMode.JOIN);
		fetchModes.put("cedenteByIdCedente", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavalAwb", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavalAwb.pessoa", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavel", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavel.pessoa", FetchMode.JOIN);
		fetchModes.put("moeda", FetchMode.JOIN);
		fetchModes.put("inscricaoEstadual", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(fetchModes);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa", FetchMode.JOIN);
		fetchModes.put("franqueado", FetchMode.JOIN);
		fetchModes.put("franqueado.pessoa", FetchMode.JOIN);
		fetchModes.put("pessoa.enderecoPessoa", FetchMode.JOIN);
		fetchModes.put("pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		fetchModes.put("empresa", FetchMode.JOIN);
		fetchModes.put("pendencia", FetchMode.JOIN);
		fetchModes.put("empresa.pessoa", FetchMode.JOIN);
		fetchModes.put("cedenteByIdCedenteBloqueto", FetchMode.JOIN);
		fetchModes.put("cedenteByIdCedente", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavalAwb", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavalAwb.pessoa", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavel", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialResponsavel.pessoa", FetchMode.JOIN);
		fetchModes.put("moeda", FetchMode.JOIN);
		fetchModes.put("aeroporto", FetchMode.JOIN);
		fetchModes.put("aeroporto.pessoa", FetchMode.JOIN);
		fetchModes.put("postoConveniado", FetchMode.JOIN);
		fetchModes.put("postoConveniado.pessoa", FetchMode.JOIN);
		fetchModes.put("inscricaoEstadual", FetchMode.JOIN);
		fetchModes.put("inscricaoEstadual.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("fotoFiliais", FetchMode.SELECT);
	}

	/**
	 * Busca uma entidade Filial a partir de seu id, sem fazer fetch em tabelas relacionadas.
	 * @param idFilial
	 * @return
	 */
	public Filial findByIdBasic(Long idFilial){
		return (Filial)getAdsmHibernateTemplate().get(Filial.class, idFilial);
	}

	public Filial findByIdJoinPessoa(Long idFilial){
		String hql = "select f from " + Filial.class.getName() + " f join fetch f.pessoa where f.id = ?";
		return (Filial)getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{idFilial});
	}

	public boolean isFilial(Long idPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f");
		dc.setProjection(Projections.count("id"));
		dc.add(Restrictions.eq("f.id", idPessoa));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	public List<Contato> findContato(Long idPessoa,String tpContato,boolean tpMercurio) {
		DetachedCriteria dc = DetachedCriteria.forClass(Contato.class)
			.add(Restrictions.eq("tpContato",tpContato))
			.add(Restrictions.eq("pessoa.idPessoa",idPessoa))
			.setFetchMode("usuario",FetchMode.JOIN);
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Retorna uma List contendo cada item da List, um Object[] contendo
	 * na seguinte ordem: (Long)idFilial, (String)sgFilial, (String)nmPessoa,
	 * e (Date)dtPrevisaoOperacaoInicial que é a data de previsão de inicio de
	 * operações do histórico da filial. Retorna estes dados caso a data de
	 * previsão de início de operações do histórico da filial seja maior ou
	 * igual à data atual.
	 * @param idFilial
	 * @return um List
	 * @author luisfco
	 */
	//FIXME Método deveria estar em AcaoDAO.
	public List<Long> getAcaoByFilial(Long idFilial,Long idPendenciaMaior,Long idPendenciaEquals) {
		DetachedCriteria dc = DetachedCriteria.forClass(Acao.class,"A");
		dc.setProjection(Projections.property("A.idAcao"));
		dc.createAlias("A.pendencia","P");
		dc.add(Restrictions.eq("P.idProcesso",idFilial));

		if (idPendenciaMaior != null) {
			dc.add(Restrictions.eq("A.tpSituacaoAcao","R"));
			dc.add(Restrictions.lt("P.id",idPendenciaMaior));
			dc.add(Restrictions.sqlRestriction("EXISTS (SELECT A.ID_ACAO FROM ACAO A INNER JOIN PENDENCIA P ON P.ID_PENDENCIA = A.ID_PENDENCIA WHERE P.ID_PROCESSO = ? AND A.ID_PENDENCIA = ? AND A.TP_SITUACAO_ACAO <> ?)",new Object[]{idFilial,idPendenciaMaior,"R"},new Type[]{new LongType(),new LongType(), new StringType()}));
		}else
			dc.add(Restrictions.eq("P.id",idPendenciaEquals));

		dc.addOrder(Order.desc("A.idAcao"));
		dc.addOrder(Order.desc("A.dhAcao.value"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findFilialComHistoricosFuturos(Long idFilial) {
		StringBuffer sb = new StringBuffer()
		.append(" select nvl(hist.dtRealOperacaoInicial, hist.dtPrevisaoOperacaoInicial) as dtSugestao_, fil.idFilial as idFilial_, fil.sgFilial as sgFilial_")
		.append(" from " + HistoricoFilial.class.getName() + " as hist")
		.append(" inner join hist.filial as fil")
		.append(" where fil.id = ? ")
		.append(" and nvl(hist.dtRealOperacaoInicial,hist.dtPrevisaoOperacaoInicial) >= trunc(current_date())")
		.append(" and 1 = (select count(*) ")
		.append(" from " + HistoricoFilial.class.getName() + " hist2 ")
		.append(" where hist2.filial = hist.filial ) ");

		return getAdsmHibernateTemplate().find(sb.toString(), idFilial);
	}

	private StringBuffer addHql(StringBuffer sb, String hql) {
		if (sb.length() == 0)
			sb.append("where ");
		else
			sb.append(" and ");
		return sb.append(hql);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public List<Map<String, Object>> findLookupFilialByUsuarioEmpresa( Usuario usuario, Empresa empresa, Map<String, Object> criteria) {
		StringBuffer sb = new StringBuffer(500);

		sb.append("select new map(filial.idFilial as idFilial, ")
		.append("pessoa.tpIdentificacao as pessoa_tpIdentificacao, ")
		.append("pessoa.nrIdentificacao as pessoa_nrIdentificacao, ")
		.append("pessoa.nmFantasia as pessoa_nmFantasia, ")
		.append("filial.dtImplantacaoLMS as dtImplantacaoLMS")
		.append("filial.sgFilial as sgFilial, ")
		.append("filial.sgFilial || ' - ' || pessoa.nmFantasia as siglaNomeFilial, ")
		.append("empresa.idEmpresa as empresa_idEmpresa, ")
		.append("empresa.tpEmpresa as empresa_tpEmpresa, ")
		.append("empresaPessoa.nmPessoa as empresa_pessoa_nmPessoa, ")
		.append("empresaPessoa.nrIdentificacao as empresa_pessoa_nrIdentificacao, ")
		.append("empresaPessoa.tpIdentificacao as empresa_pessoa_tpIdentificacao ) ")
		.append("from ").append(Filial.class.getName()).append(" as filial, EmpresaUsuario as empresaUsuario ")
		.append("inner join filial.pessoa as pessoa ")
		.append("inner join filial.empresa as empresa ")
		.append("inner join empresa.pessoa as empresaPessoa ")
		.append("where empresa.idEmpresa = ? AND empresaUsuario.usuario.idUsuario=? ")
		.append("AND lower(filial.sgFilial) like lower('")
		.append( criteria.get("sgFilial") )
		.append("' )");

		String filiaisSelecionadas = (String)criteria.get( "filiaisSelecionadas" );
		if( filiaisSelecionadas!= null && filiaisSelecionadas != "" ){
			sb.append(" AND filial.idFilial in( ");
			sb.append( filiaisSelecionadas );
			sb.append(" )" );
		}

		String[] restricao = { empresa.getIdEmpresa().toString(), usuario.getIdUsuario().toString() };

		return getHibernateTemplate().find( sb.toString(), restricao );
	}

	public Integer getRowCountFilial(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	public Integer getRowCountDocasByFilial(Long idFilial) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(Doca.class,"D")
				.setProjection(Projections.rowCount())
				.createAlias("D.terminal","T")
				.add(Restrictions.le("D.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()))
				.add(Restrictions.ge("D.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()))
				.add(Restrictions.eq("T.filial.id",idFilial)));
	}

	public Integer getRowCountBoxByFilial(Long idFilial) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(Box.class,"B")
			.setProjection(Projections.rowCount())
			.createAlias("B.doca","D")
			.createAlias("D.terminal","T")
			.add(Restrictions.le("B.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("B.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.eq("T.filial.id",idFilial)));
	}

	public String[] getTypes(String typeRecebido, String flag) {
		if (typeRecebido.equals("FI") || (typeRecebido.equals("FR") && flag.equals("C")))
			return new String[] { "FI","FR" };
		else if (typeRecebido.equals("LO"))
			return new String[] { "FI", "FR" };
		else
			return new String[] { "FI", "FR", "LO", "MA", "PA", "SU", "LG", "OP" };
	}

	/**
	 * Sobrecarga do mesmo métdo, usando mais o parâmetro "String tpEmpresa"
	 * @param sgFilial
	 * @param vigenteEm
	 * @param tpAcesso
	 * @return
	 */
	public List<Filial> findLookupBySgFilialVigenteEm(String sgFilial, YearMonthDay vigenteEm, String tpAcesso) {
		return findLookupBySgFilialVigenteEm(sgFilial, vigenteEm, tpAcesso, null);
	}

	/**
	 * Retorna as Filiais vigentes.<BR>
	 *@author Robson Edemar Gehl
	 * @param sgFilial
	 * @param vigente
	 * @return
	 */
	public List<Filial> findLookupBySgFilialVigenteEm(String sgFilial, YearMonthDay vigenteEm, String tpAcesso, String tpEmpresa) {

		SqlTemplate sqlSub = new SqlTemplate();
		SqlTemplate sql = new SqlTemplate();

		//Subquery
		sqlSub.addFrom(HistoricoFilial.class.getName(),"hf2");

		sqlSub.addProjection("max(hf2.id)");
		sqlSub.addJoin("hf2.filial.idFilial","f.idFilial");

		//Query principal
		sql.addProjection("f.idFilial as idFilial," +
						"f.sgFilial as sgFilial," +
						"p.nmFantasia as nmFantasia," +
						"ep.nmPessoa as nmEmpresa" );

		sql.addFrom(Filial.class.getName(), "f inner join f.pessoa as p" +
										" inner join f.empresa as e" +
										" inner join e.pessoa as ep" +
										" left outer join  f.historicoFiliais as hf");

		sql.addCriteria("upper(f.sgFilial)","like",sgFilial.toUpperCase());
		sql.addCustomCriteria("hf.id = ( " + sqlSub.getSql() + " ) ");

		sql.addCustomCriteria("hf.dtRealOperacaoInicial <= ? and ( hf.dtRealOperacaoFinal is null or hf.dtRealOperacaoFinal >= ? )");
		sql.addCriteriaValue(vigenteEm);
		sql.addCriteriaValue(vigenteEm);


		/*Programação dos critérios de acesso aos dados das filiais*/
		if (StringUtils.isNotBlank(tpAcesso)){
			/*TIPO DE ACESSO A- ABERTO*/
			if ("A".equals(tpAcesso)){
				//não usa critérios
			} else {
			/*TIPO DE ACESSO F- FILIAIS QUE O USUARIO LOGADO TEM ACESSO*/
				List<Long> idsFiliaisUsuarioLogado = findIdsFiliaisUsuarioLogado();
				SQLUtils.joinExpressionsWithComma(idsFiliaisUsuarioLogado, sql, "f.idFilial");
			}
		} else {
			/*Sempre busca as filiais da empresa do usuário logado*/
			sql.addCriteria("e.idEmpresa","=",SessionUtils.getEmpresaSessao().getIdEmpresa());
		}

		if (tpEmpresa != null) {
			sql.addCriteria("e.tpEmpresa","=",tpEmpresa);
		}

		List<Filial> retorno = new ArrayList<Filial>();
		List<Object[]> listaFiliais = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		Filial filial = null;
		Pessoa pessoa = null;
		Empresa empresa = null;
		Pessoa pessoaEmpresa = null;

		for(Object[] element : listaFiliais) {
			filial = new Filial();
			pessoa = new Pessoa();
			empresa = new Empresa();
			pessoaEmpresa = new Pessoa();
			filial.setIdFilial((Long)element[0]);
			filial.setSgFilial((String)element[1]);
			pessoa.setNmFantasia((String)element[2]);
			filial.setPessoa(pessoa);
			pessoaEmpresa.setNmPessoa((String)element[3]);
			empresa.setPessoa(pessoaEmpresa);
			filial.setEmpresa(empresa);

			retorno.add(filial);
		}

		return retorno;
	}

	/**
	 * Retorna a filial de cobrança do cliente informado.
	 *
	 * @param Long idCliente
	 * @return List
	 * */
	public List<Filial> findFilialCobrancaByCliente(Long idCliente){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("fi");
		sql.addFrom(Cliente.class.getName(),"cl " +
					"join cl.filialByIdFilialCobranca as fi " +
					"join fetch fi.pessoa as pe");
		sql.addCriteria("cl.id","=",idCliente);
		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna a lista de filial centralazadora da filial informada por tpModal e tpAbrangencia.
	 *
	 * @param Long idFilial
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @return List
	 */
	public List<Filial> findFilialCentralizadoraByFilial(Long idFilialCentralizada, Long idFilialCentralizadora, String tpModal, String tpAbrangencia){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("fi");
		sql.addFrom(CentralizadoraFaturamento.class.getName(),"cf " +
					"join cf.filialByIdFilialCentralizada as ficen " +
					"join cf.filialByIdFilialCentralizadora as fi " +
					"join fetch fi.pessoa as pe");
		sql.addCriteria("ficen.id","=",idFilialCentralizada);
		sql.addCriteria("fi.id","=",idFilialCentralizadora);
		sql.addCriteria("cf.tpModal","=",tpModal);
		sql.addCriteria("cf.tpAbrangencia","=",tpAbrangencia);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public Filial findFilialPessoaBySgFilial(String sgFilial, Boolean isMercurio) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
									.createAlias("pessoa", "p")
									.createAlias("empresa", "e")
									.add(Restrictions.ilike("f.sgFilial", sgFilial, MatchMode.EXACT));

		if (isMercurio != null && isMercurio.booleanValue())
			dc.add(Restrictions.eq("e.tpEmpresa","M"));

		return (Filial)dc.getExecutableCriteria(getSession()).uniqueResult();
	}

	public List findNmSgFilialById(Long idFilial) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("f.idFilial"), "idFilial")
		.add(Projections.property("f.sgFilial"), "sgFilial")
		.add(Projections.property("p.nmFantasia"), "pessoa_nmFantasia");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
			.createAlias("pessoa", "p")
			.setProjection(pl)
			.add(Restrictions.idEq(idFilial))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	/**
	 * Consulta as filiais de acordo com a sigla recebida por parâmetro.
	 *
	 * @author Felipe Ferreira
	 * @param sgFilial String com a sigla da filial a consultar.
	 * @return Long id da Filial encontrada.
	 */
	public Long findIdFilialBySigla(String sgFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.property("idFilial"));
		dc.add(Restrictions.ilike("sgFilial", sgFilial, MatchMode.EXACT));

		List<Long> result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		return (result.size() > 0) ? result.get(0) : null;
	}

	public List findBySgFilialEmpresa(String sgFilial, Long idEmpresa) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("f.idFilial"), "idFilial")
		.add(Projections.property("f.sgFilial"), "sgFilial")
		.add(Projections.property("p.nmFantasia"), "nmFantasia");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
		.setProjection(pl)
		.createAlias("f.pessoa", "p")
		.add(Restrictions.ilike("f.sgFilial", sgFilial.toLowerCase(), MatchMode.ANYWHERE))
		.add(Restrictions.eq("f.empresa.id", idEmpresa))
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findPaginatedByDetachedCriteria(dc, Integer.valueOf(1), Integer.valueOf(2)).getList();
	}

	/**
	 * Busca o mapeamento de/para de código para sigla de filiais.
	 * Definido pela issue LMS-882.
	 * @return o mapeamento entre código e sigla de filiais.
	 * @author Luis Carlos Poletto
	 */
	public Map<String, String> findFilialMapping() {
		Map<String, String> result = new HashMap<String, String>();
		ProjectionList projections = Projections.projectionList()
			.add(Projections.property("f.codFilial"), "codFilial")
			.add(Projections.property("f.sgFilial"), "sgFilial");
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "f")
			.setProjection(projections)
			.add(Expression.isNotNull("f.codFilial"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> filiais = findByDetachedCriteria(criteria);
		if (filiais != null) {
			for (Map<String, Object> filial : filiais) {
				result.put(String.valueOf(filial.get("codFilial")), (String) filial.get("sgFilial"));
			}
		}
		return result;
	}

	public Map<String, Object> findSgFilialTpEmpresaByIdFilial(Long idFilial) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("e.tpEmpresa"), "tpEmpresa")
			.add(Projections.property("f.sgFilial"), "sgFilial");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
			.createAlias("f.empresa", "e")
			.setProjection(pl)
			.add(Restrictions.idEq(idFilial))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return l.get(0);
		return Collections.EMPTY_MAP;
	}

	/**
	 * Retorna blInformaKmPortaria da filial passada por parâmetro.
	 *
	 * @author Felipe Ferreira
	 * @param idFilial id da Filial onde deseja-se o valor booleano.
	 * @return valor do campo de blInformaKmPortaria.
	 */
	public Boolean findBlInformaKmPortaria(Long idFilial) {
		StringBuffer hql = new StringBuffer()
			.append(" select F.blInformaKmPortaria from ")
			.append(Filial.class.getName())
			.append(" F where F.idFilial = ? ");

		List<Boolean> result = getAdsmHibernateTemplate().find(hql.toString(),idFilial);

		return result.isEmpty() ? null : result.get(0);
	}

	public Map<String, Object> findAeroportoFilial(Long idFilial) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("a.idAeroporto"), "idAeroporto")
			.add(Projections.property("a.sgAeroporto"), "sgAeroporto")
			.add(Projections.property("p.nmPessoa"), "nmAeroporto");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
			.createAlias("f.aeroporto", "a")
			.createAlias("a.pessoa", "p")
			.setProjection(pl)
			.add(Restrictions.idEq(idFilial))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);

		List<Map<String, Object>> l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return l.get(0);
		return Collections.EMPTY_MAP;
	}

	/**
	 * Retorna a lista de filiais por um array do numero de identificacao.
	 * @param Long[] nrIdentificacao
	 * @return List
	 * */
	public List<Filial> findFilialByArrayNrIdentificacaoPessoa(List<String> nrsIdentificacao) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("f");

		sql.addFrom(getPersistentClass().getName(), " f join fetch f.pessoa as p");

		if (!nrsIdentificacao.isEmpty()) {
			String customCriteria = "p.nrIdentificacao in (";
			for(Iterator<String> iter = nrsIdentificacao.iterator(); iter.hasNext();) {
				String nr = (String) iter.next();
				if (!nr.equals("")) {
					customCriteria += "'" + nr + "'";

					if (iter.hasNext()) {
						customCriteria += ", ";
					}
				}
			}
			customCriteria += ")";
			sql.addCustomCriteria(customCriteria);
		}
		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List <Filial>findFiliaisByUsuarioEmpresa(Usuario usuario,Empresa empresa, Map<String, Object> criteria) {
		StringBuffer restricao = new StringBuffer();
		if(criteria != null) {
			String filiaisSelecionadas = (String)criteria.get("filiaisSelecionadas");
			if( filiaisSelecionadas!= null && filiaisSelecionadas != "" ){
				restricao.append( " AND f.idFilial in( ");
				restricao.append( filiaisSelecionadas );
				restricao.append( " ) ");
			}
			restricao.append( " AND lower( f.sgFilial ) like lower('");
			restricao.append( criteria.get("sgFilial").toString() );
			restricao.append( " ' )");
		}

		YearMonthDay now = JTDateTimeUtils.getDataAtual();
		StringBuffer sb = new StringBuffer()
		.append(" select f")
		.append(" from " + FilialUsuario.class.getName() + " as fu")
		.append(" inner join fu.filial f")
		.append(" inner join fetch f.pessoa as p")
		.append(" inner join fetch f.historicoFiliais as hf")
		.append(" inner join fu.empresaUsuario empu")
		.append(" inner join empu.usuario u")
		.append(" inner join empu.empresa e")
		.append(" where u.idUsuario = ? and e.idEmpresa = ? ");
		 if(!SessionUtils.isIntegrationRunning()){
			 sb.append(" and hf.idHistoricoFilial = (")
			   .append("select max(hf1.idHistoricoFilial) from " + HistoricoFilial.class.getName() + " as hf1 where hf1.filial.idFilial = f.idFilial and hf1.dtRealOperacaoInicial <= ? and hf1.dtRealOperacaoFinal >= ?)");
	}

		sb.append( restricao.toString() );
		if(SessionUtils.isIntegrationRunning())
			return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{usuario.getIdUsuario(), empresa.getIdEmpresa()});
		return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{usuario.getIdUsuario(), empresa.getIdEmpresa(),now, now});
	}

	public List<Filial> findFiliaisByRegionais(com.mercurio.lms.configuracoes.model.Usuario usuario, Empresa empresa) {

		YearMonthDay now = JTDateTimeUtils.getDataAtual();
		StringBuffer sb = new StringBuffer()
		.append(" select rf.filial")
		.append(" from " + EmpresaUsuario.class.getName() + " as eu")
		.append(" inner join eu.usuario u")
		.append(" inner join eu.empresa e")
		.append(" inner join eu.regionalUsuario ru")
		.append(" inner join ru.regional reg")
		.append(" inner join reg.regionalFiliais rf")
		.append(" inner join rf.filial f")
		.append(" inner join fetch f.pessoa as p")
		.append(" inner join fetch f.historicoFiliais as hf")
		.append(" where hf.idHistoricoFilial = (")
		.append("select max(hf1.idHistoricoFilial) from " + HistoricoFilial.class.getName() + " as hf1 where hf1.filial.idFilial = f.idFilial and hf1.dtRealOperacaoInicial <= ? and hf1.dtRealOperacaoFinal >= ?")
		.append(") and u.idUsuario = ? and e.idEmpresa = ?");

		return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{now, now, usuario.getIdUsuario(), empresa.getIdEmpresa()});
	}

	public List<Filial> findFiliaisByEmpresa(Empresa empresa) {

		YearMonthDay now = JTDateTimeUtils.getDataAtual();
		StringBuffer sb = new StringBuffer()
		.append(" select f")
		.append(" from " + Empresa.class.getName() + " as e")
		.append(" inner join e.filiais as f")
		.append(" inner join f.historicoFiliais as hf")
		.append(" where hf.idHistoricoFilial = (")
		.append("select max(hf1.idHistoricoFilial) from " + HistoricoFilial.class.getName() + " as hf1 where hf1.filial.idFilial = f.idFilial and hf1.dtRealOperacaoInicial <= ? and hf1.dtRealOperacaoFinal >= ?")
		.append(") and e.idEmpresa = ?");

		return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{now, now, empresa.getIdEmpresa()});
	}

	public Filial findFilialPadraoByUsuarioEmpresa(Usuario usuario, Empresa empresa) {
		StringBuffer sb = new StringBuffer()
		.append(" select eu.filialPadrao")
		.append(" from " + EmpresaUsuario.class.getName() + " as eu")
		.append(" join fetch eu.filialPadrao.pessoa ")
		.append(" where eu.usuario.idUsuario = ? and eu.empresa.idEmpresa = ?");

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { usuario.getIdUsuario(), empresa.getIdEmpresa() });
	}

	public Filial findFilialLogadoById(Long id) {
		StringBuffer sb = new StringBuffer()
		.append(" select f")
		.append(" from ").append(getPersistentClass().getName()).append(" as f")
		.append(" join fetch f.pessoa p ")
		.append(" join fetch p.enderecoPessoa ep")
		.append(" join fetch ep.municipio m")
		.append(" join fetch m.unidadeFederativa uf")
		.append(" join fetch uf.pais")
		.append(" where f.idFilial = ?");

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { id });
	}

	public List<Filial> findFiliaisByRegionais(List regionais) {
		StringBuffer hql = new StringBuffer()
		.append("from ")
		.append(Filial.class.getName())
		.append(" f where f.regionalFiliais.regional in ( :rs )");

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "rs", regionais);
	}

	/**
	 * Aplica os criterios recebidos nas filiais que o usuário logado tem
	 * acesso.
	 *
	 * @param m
	 * @return
	 */
	public List<TypedFlatMap> findLookupByUsuarioLogado(TypedFlatMap m) {
		List filiais = SessionUtils.getFiliaisUsuarioLogado();

		StringBuffer hql = new StringBuffer();
		hql.append(" select f.idFilial, ");
		hql.append("        f.sgFilial, ");
		hql.append("        f.pessoa.nmFantasia");
		hql.append("   from ");
		hql.append(Filial.class.getName()).append(" f ");
		hql.append(" where ");

		if (filiais != null) {
			hql.append(" f.idFilial in ( :filiais ) ");
		}
		if (StringUtils.isNotBlank(m.getString("sgFilial"))) {
			if (filiais != null) {
				hql.append(" and ");
			}
			hql.append(" lower(f.sgFilial) = lower(:sgFilial) ");
		}

		Iterator it = filiais.iterator();
		List<Long> filiaisIdList = new ArrayList<Long>();

		while(it.hasNext()) {
			Filial filial = (Filial)it.next();
			filiaisIdList.add(filial.getIdFilial());
		}


		List<Object[]> l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
				new String[] { "sgFilial", "filiais" },
				new Object[] { m.getString("sgFilial"), filiaisIdList });

		List<TypedFlatMap> r = new ArrayList<TypedFlatMap>(l.size());
		for(Object[] o : l) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", o[0]);
			tfm.put("sgFilial", o[1]);
			tfm.put("pessoa.nmFantasia", o[2]);

			r.add(tfm);
		}

		return r;
	}

	public List findLookupByEmpresaUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();
		if(u == null){
			u = (Usuario) m.get(ConstantesAmbiente.USUARIO_LOGADO);
		}
		if (StringUtils.isNotBlank(m.getString("empresa.idEmpresa")))
			m.put("idEmpresa", m.getString("empresa.idEmpresa"));

		StringBuffer hql = new StringBuffer();
		hql.append("select distinct");
		hql.append(" f.idFilial, ");
		hql.append(" f.sgFilial,");
		hql.append(" f.pessoa.nmFantasia");
		hql.append(" from ");
		hql.append(EmpresaUsuario.class.getName()).append(" eu");
		hql.append(" inner join eu.empresa e");
		hql.append(" inner join e.filiais f");
		hql.append(" inner join f.historicoFiliais hf");
		hql.append(" where");
		hql.append(" eu.usuario.idUsuario = :idUsuario");
		hql.append(" and e.idEmpresa = :idEmpresa");
		hql.append(" and eu.blIrrestritoFilial = 'S'");
		hql.append(" and hf.dtRealOperacaoInicial <= :dtRealOperacaoInicial");
		hql.append(" and (hf.dtRealOperacaoFinal is null or hf.dtRealOperacaoFinal >= :dtRealOperacaoFinal)");
		String sgFilial = m.getString("sgFilial");
		if (StringUtils.isNotBlank(sgFilial))
			hql.append(" and lower(f.sgFilial) like lower(:sgFilial)");

		List l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
				new String[] { "idUsuario", "idEmpresa", "dtRealOperacaoInicial", "dtRealOperacaoFinal", "sgFilial" },
				new Object[] { u.getIdUsuario(), m.getLong("idEmpresa"), JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual(), sgFilial });

		List r = new ArrayList();
		for (Iterator it = l.iterator(); it.hasNext();) {
			Object[] o = (Object[]) it.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", o[0]);
			tfm.put("sgFilial", o[1]);
			tfm.put("pessoa.nmFantasia", o[2]);

			r.add(tfm);
		}

		if (r.size() > 0)
			return r;

		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" f.id_filial idFilial");
		sql.append(", f.sg_filial sgFilial");
		sql.append(", fp.nm_fantasia nmFantasia");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", filial_usuario fu");
		sql.append(", filial f");
		sql.append(", pessoa fp");
		sql.append(", historico_filial hf");
		sql.append(" where");
		sql.append(" eu.id_empresa_usuario = fu.id_empresa_usuario");
		sql.append(" and fu.id_filial = f.id_filial");
		sql.append(" and f.id_filial = fp.id_pessoa");
		sql.append(" and f.id_filial = hf.id_filial");
		sql.append(" and eu.id_usuario = ?");
		sql.append(" and eu.id_empresa = ?");
		sql.append(" and hf.dt_real_operacao_inicial <= to_date(?, 'yyyy-mm-dd')");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= to_date(?, 'yyyy-mm-dd'))");
		if (StringUtils.isNotBlank(sgFilial))
			sql.append(" and lower(f.sg_filial) like lower(?)");
		sql.append(" union");
		sql.append(" select");
		sql.append(" f.id_filial idFilial");
		sql.append(", f.sg_filial sgFilial");
		sql.append(", fp.nm_fantasia nmFantasia");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", regional_usuario ru");
		sql.append(", regional_filial rf");
		sql.append(", filial f");
		sql.append(", pessoa fp");
		sql.append(", historico_filial hf");
		sql.append(" where");
		sql.append(" eu.id_empresa_usuario = ru.id_empresa_usuario");
		sql.append(" and ru.id_regional = rf.id_regional");
		sql.append(" and rf.id_filial = f.id_filial");
		sql.append(" and f.id_filial = fp.id_pessoa");
		sql.append(" and f.id_filial = hf.id_filial");
		sql.append(" and eu.id_usuario = ?");
		sql.append(" and eu.id_empresa = ?");
		sql.append(" and rf.dt_vigencia_inicial <= to_date(?, 'yyyy-mm-dd')");
		sql.append(" and (rf.dt_vigencia_final is null or rf.dt_vigencia_final >= to_date(?, 'yyyy-mm-dd'))");
		sql.append(" and hf.dt_real_operacao_inicial <= to_date(?, 'yyyy-mm-dd')");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= to_date(?, 'yyyy-mm-dd'))");
		if (StringUtils.isNotBlank(sgFilial))
			sql.append(" and lower(f.sg_filial) like lower(?)");

		List args = new ArrayList();
		List types = new ArrayList();
		args.add(u.getIdUsuario());
		types.add(Types.INTEGER);
		args.add(m.getLong("empresa.idEmpresa"));
		types.add(Types.INTEGER);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		if (StringUtils.isNotBlank(sgFilial)) {
			args.add(sgFilial);
			types.add(Types.VARCHAR);
		}
		args.add(u.getIdUsuario());
		types.add(Types.INTEGER);
		args.add(m.getLong("empresa.idEmpresa"));
		types.add(Types.INTEGER);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		args.add(JTDateTimeUtils.getDataAtual().toString());
		types.add(Types.VARCHAR);
		if (StringUtils.isNotBlank(sgFilial)) {
			args.add(sgFilial);
			types.add(Types.VARCHAR);
		}

		int[] typesArray = new int[types.size()];
		int i = 0;
		for (Iterator it = types.iterator(); it.hasNext();) {
			int type = (Integer) it.next();
			typesArray[i++] = type;
		}

		final List r1 = new ArrayList();
		this.jdbcTemplate.query(sql.toString(), args.toArray(), new RowCallbackHandler() {

			public void processRow(ResultSet rs) throws SQLException {

				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("idFilial", rs.getLong("idFilial"));
				tfm.put("sgFilial", rs.getString("sgFilial"));
				tfm.put("pessoa.nmFantasia", rs.getString("nmFantasia"));
				r1.add(tfm);
			}
		});

		return r1;
		}

	public ResultSetPage findPaginatedByEmpresaUsuarioLogado(TypedFlatMap map) {

		FindDefinition fd = FindDefinition.createFindDefinition(map);

		Usuario usuario = SessionUtils.getUsuarioLogado();
		map.put("idUsuario", usuario.getIdUsuario());
		if (StringUtils.isNotBlank(map.getString("empresa.idEmpresa"))) {
			map.put("idEmpresa", map.getLong("empresa.idEmpresa"));
		}

		map.put("dtRealOperacaoInicial", JTDateTimeUtils.getDataAtual());
		map.put("dtRealOperacaoFinal", JTDateTimeUtils.getDataAtual());

		StringBuffer hql = new StringBuffer();
		hql.append("select distinct");
		hql.append(" f.idFilial, ");
		hql.append(" f.sgFilial,");
		hql.append(" fp.nmFantasia,");
		hql.append(" ep.nrIdentificacao,");
		hql.append(" ep.tpIdentificacao,");
		hql.append(" ep.nmPessoa,");
		hql.append(" f.sgFilial || ' - ' || fp.nmFantasia as filialSgNm");
		hql.append(" from ");
		hql.append(EmpresaUsuario.class.getName()).append(" eu");
		hql.append(" inner join eu.empresa e");
		hql.append(" inner join e.pessoa ep");
		hql.append(" inner join e.filiais f");
		hql.append(" inner join f.pessoa fp");
		hql.append(" inner join f.historicoFiliais hf");
		hql.append(" where");
		hql.append(" eu.usuario.idUsuario = :idUsuario");
		hql.append(" and e.idEmpresa = :idEmpresa");
		hql.append(" and eu.blIrrestritoFilial = 'S'");
		hql.append(" and hf.dtRealOperacaoInicial <= :dtRealOperacaoInicial");
		hql.append(" and (hf.dtRealOperacaoFinal is null or hf.dtRealOperacaoFinal >= :dtRealOperacaoFinal)");
		if (StringUtils.isNotBlank(map.getString("sgFilial"))) {
			hql.append(" and lower(f.sgFilial) like lower(:sgFilial)");
		}

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(), map);
		List r = new ArrayList();

		for (Iterator it = rsp.getList().iterator(); it.hasNext();) {
			Object[] o = (Object[]) it.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", o[0]);
			tfm.put("sgFilial", o[1]);
			tfm.put("pessoa.nmFantasia", o[2]);
			tfm.put("empresa.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(((DomainValue) o[4]).getValue(), (String) o[3]) + " - " + o[5]);
			tfm.put("filialSgNm", o[6]);
			r.add(tfm);
		}
		if (r.size() > 0) {
			rsp.setList(r);
			return rsp;
		}

		StringBuffer sql = new StringBuffer("select");
		sql.append(" f.id_filial idFilial");
		sql.append(", f.sg_filial sgFilial");
		sql.append(", fp.nm_fantasia nmFantasia");
		sql.append(", ep.nr_identificacao nrIdentificacao");
		sql.append(", ep.tp_identificacao tpIdentificacao");
		sql.append(", ep.nm_pessoa nmPessoa");
		sql.append(", f.sg_filial || ' - ' || fp.nm_fantasia filialSgNm");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", empresa e");
		sql.append(", pessoa ep");
		sql.append(", filial_usuario fu");
		sql.append(", filial f");
		sql.append(", pessoa fp");
		sql.append(", historico_filial hf");
		sql.append(" where");
		sql.append(" eu.id_empresa = e.id_empresa");
		sql.append(" and e.id_empresa = ep.id_pessoa");
		sql.append(" and eu.id_empresa_usuario = fu.id_empresa_usuario");
		sql.append(" and fu.id_filial = f.id_filial");
		sql.append(" and f.id_filial = fp.id_pessoa");
		sql.append(" and f.id_filial = hf.id_filial");
		sql.append(" and eu.id_usuario = :idUsuario1");
		sql.append(" and eu.id_empresa = :idEmpresa1");
		sql.append(" and hf.dt_real_operacao_inicial <= :dtRealOperacaoInicial1");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= :dtRealOperacaoFinal1)");
		if (StringUtils.isNotBlank(map.getString("sgFilial"))) {
			sql.append(" and lower(f.sg_filial) like lower(:sgFilial1)");
		}
		sql.append(" union");
		sql.append(" select");
		sql.append(" f.id_filial idFilial");
		sql.append(", f.sg_filial sgFilial");
		sql.append(", fp.nm_fantasia nmFantasia");
		sql.append(", ep.nr_identificacao nrIdentificacao");
		sql.append(", ep.tp_identificacao tpIdentificacao");
		sql.append(", ep.nm_pessoa nmPessoa");
		sql.append(", f.sg_filial || ' - ' || fp.nm_fantasia filialSgNm");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", empresa e");
		sql.append(", pessoa ep");
		sql.append(", regional_usuario ru");
		sql.append(", regional_filial rf");
		sql.append(", filial f");
		sql.append(", pessoa fp");
		sql.append(", historico_filial hf");
		sql.append(" where");
		sql.append(" eu.id_empresa = e.id_empresa");
		sql.append(" and e.id_empresa = ep.id_pessoa");
		sql.append(" and eu.id_empresa_usuario = ru.id_empresa_usuario");
		sql.append(" and ru.id_regional = rf.id_regional");
		sql.append(" and rf.id_filial = f.id_filial");
		sql.append(" and f.id_filial = fp.id_pessoa");
		sql.append(" and f.id_filial = hf.id_filial");
		sql.append(" and eu.id_usuario = :idUsuario2");
		sql.append(" and eu.id_empresa = :idEmpresa2");
		sql.append(" and rf.dt_vigencia_inicial <= :dtVigenciaInicial2");
		sql.append(" and (rf.dt_vigencia_final is null or rf.dt_vigencia_final >= :dtVigenciaFinal2)");
		sql.append(" and hf.dt_real_operacao_inicial <= :dtRealOperacaoInicial2");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= :dtRealOperacaoFinal2)");
		if (StringUtils.isNotBlank(map.getString("sgFilial")))
			sql.append(" and lower(f.sg_filial) like lower(:sgFilial2)");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idUsuario1", usuario.getIdUsuario());
		params.put("idEmpresa1", map.getLong("empresa.idEmpresa"));
		params.put("dtRealOperacaoInicial1", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoFinal1", JTDateTimeUtils.getDataAtual());
		params.put("sgFilial1", map.getString("sgFilial"));

		params.put("idUsuario2", usuario.getIdUsuario());
		params.put("idEmpresa2", map.getLong("empresa.idEmpresa"));
		params.put("dtVigenciaInicial2", JTDateTimeUtils.getDataAtual());
		params.put("dtVigenciaFinal2", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoInicial2", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoFinal2", JTDateTimeUtils.getDataAtual());
		params.put("sgFilial2", map.getString("sgFilial"));

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idFilial",Hibernate.LONG);
				sqlQuery.addScalar("sgFilial",Hibernate.STRING);
				sqlQuery.addScalar("nmFantasia",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("nmPessoa",Hibernate.STRING);
				sqlQuery.addScalar("filialSgNm",Hibernate.STRING);
			}
		};
		rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), fd.getCurrentPage(), fd.getPageSize(), params, csq);

		r = new ArrayList();
		for (Iterator it = rsp.getList().iterator(); it.hasNext();) {
			TypedFlatMap tfm = new TypedFlatMap();
			Object[] os = (Object[]) it.next();
			tfm.put("idFilial", os[0]);
			tfm.put("sgFilial", os[1]);
			tfm.put("pessoa.nmFantasia", os[2]);
			tfm.put("empresa.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao((String) os[4], (String) os[3]) + " - " + os[5]);
			tfm.put("filialSgNm", os[6]);
			r.add(tfm);
		}
		rsp.setList(r);

		return rsp;
	}

	public Integer getRowCountByEmpresaUsuarioLogado(TypedFlatMap m) {

		Usuario u = SessionUtils.getUsuarioLogado();
		m.put("idUsuario", u.getIdUsuario());
		if (StringUtils.isNotBlank(m.getString("empresa.idEmpresa"))) {
			m.put("idEmpresa", m.getLong("empresa.idEmpresa"));
		}
		m.put("dtRealOperacaoInicial", JTDateTimeUtils.getDataAtual());
		m.put("dtRealOperacaoFinal", JTDateTimeUtils.getDataAtual());

		StringBuffer hql = new StringBuffer();
		hql.append(" from ");
		hql.append(EmpresaUsuario.class.getName()).append(" eu");
		hql.append(" inner join eu.empresa e");
		hql.append(" inner join e.pessoa ep");
		hql.append(" inner join e.filiais f");
		hql.append(" inner join f.pessoa fp");
		hql.append(" inner join f.historicoFiliais hf");
		hql.append(" where");
		hql.append(" eu.usuario.idUsuario = :idUsuario");
		hql.append(" and e.idEmpresa = :idEmpresa");
		hql.append(" and eu.blIrrestritoFilial = 'S'");
		hql.append(" and hf.dtRealOperacaoInicial <= :dtRealOperacaoInicial");
		hql.append(" and (hf.dtRealOperacaoFinal is null or hf.dtRealOperacaoFinal >= :dtRealOperacaoFinal)");
		if (StringUtils.isNotBlank(m.getString("sgFilial")))
			hql.append(" and lower(f.sgFilial) like lower(:sgFilial)");

		Integer i = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), m);
		if (i > 0)
			return i;

		StringBuffer sql = new StringBuffer(" from ( select");
		sql.append(" fu.id_filial");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", filial_usuario fu");
		sql.append(", filial f");
		sql.append(", historico_filial hf");
		sql.append(" where");
		sql.append(" eu.id_empresa_usuario = fu.id_empresa_usuario");
		sql.append(" and fu.id_filial = hf.id_filial");
		sql.append(" and fu.id_filial = f.id_filial");
		sql.append(" and eu.id_usuario = :idUsuario1");
		sql.append(" and eu.id_empresa = :idEmpresa1");
		sql.append(" and hf.dt_real_operacao_inicial <= :dtRealOperacaoInicial1");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= :dtRealOperacaoFinal1)");
		if (StringUtils.isNotBlank(m.getString("sgFilial")))
			sql.append(" and lower(f.sg_filial) like lower(:sgFilial1)");
		sql.append(" union");
		sql.append(" select");
		sql.append(" rf.id_filial");
		sql.append(" from");
		sql.append(" empresa_usuario eu");
		sql.append(", regional_usuario ru");
		sql.append(", regional_filial rf");
		sql.append(", historico_filial hf");
		sql.append(", filial f");
		sql.append(" where");
		sql.append(" eu.id_empresa_usuario = ru.id_empresa_usuario");
		sql.append(" and ru.id_regional = rf.id_regional");
		sql.append(" and rf.id_filial = hf.id_filial");
		sql.append(" and rf.id_filial = f.id_filial");
		sql.append(" and eu.id_usuario = :idUsuario1");
		sql.append(" and eu.id_empresa = :idEmpresa1");
		sql.append(" and rf.dt_vigencia_inicial <= :dtVigenciaInicial2");
		sql.append(" and (rf.dt_vigencia_final is null or rf.dt_vigencia_final >= :dtVigenciaFinal2)");
		sql.append(" and hf.dt_real_operacao_inicial <= :dtRealOperacaoInicial2");
		sql.append(" and (hf.dt_real_operacao_final is null or hf.dt_real_operacao_final >= :dtRealOperacaoFinal2)");
		if (StringUtils.isNotBlank(m.getString("sgFilial"))) {
			sql.append(" and lower(f.sg_filial) like lower(:sgFilial2)");
		}
		sql.append(")");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idUsuario1", u.getIdUsuario());
		params.put("idEmpresa1", m.getLong("empresa.idEmpresa"));
		params.put("dtRealOperacaoInicial1", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoFinal1", JTDateTimeUtils.getDataAtual());
		params.put("sgFilial1", m.getString("sgFilial"));

		params.put("idUsuario2", u.getIdUsuario());
		params.put("idEmpresa2", m.getLong("empresa.idEmpresa"));
		params.put("dtVigenciaInicial2", JTDateTimeUtils.getDataAtual());
		params.put("dtVigenciaFinal2", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoInicial2", JTDateTimeUtils.getDataAtual());
		params.put("dtRealOperacaoFinal2", JTDateTimeUtils.getDataAtual());
		params.put("sgFilial2", m.getString("sgFilial"));

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), params);
	}

	public Filial findFilialByIdEmpresaTpFilial(Long idEmpresa, String tpFilial) {
		StringBuffer sb = new StringBuffer()
		.append("select hf.filial")
		.append(" from "+HistoricoFilial.class.getName()+" hf")
		.append(" where hf.id = (select max(hf2.id)")
						.append(" from "+HistoricoFilial.class.getName()+" hf2 ")
						.append(" where hf2.tpFilial = ?")
						.append("   and hf2.filial.empresa.id =?)");

		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());
		query.setParameter(0, tpFilial);
		query.setParameter(1, idEmpresa);
		return (Filial)query.uniqueResult();
	}

	/**
	 *
	 * @param idRotaIdaVolta
	 * @param idSolicitacaoContratacao
	 * @param blOrigemRota
	 * @param blDestinoRota
	 * @return
	 */
	public Filial findFilialOrigemDestinoToControleCarga(
			Long idRotaIdaVolta,
			Long idRota,
			Long idSolicitacaoContratacao,
			Boolean blOrigemRota,
			Boolean blDestinoRota
	) {
		List parametros = new ArrayList();

		StringBuffer hql = new StringBuffer()
			.append("select f ")
			.append("from ").append(Filial.class.getName()).append(" as f ")
			.append("inner join f.filialRotas as fr ")
			.append("inner join fr.rota as r ");

		if (idRota != null) {
			hql.append("where r.id = ? ");
			parametros.add(idRota);
		}
		else
		if (idRotaIdaVolta != null) {
			hql.append("inner join r.rotaIdaVoltas as riv ");
			hql.append("where riv.id = ? ");
			parametros.add(idRotaIdaVolta);
		}
		else
		if (idSolicitacaoContratacao != null) {
			hql.append("inner join r.solicitacaoContratacoes as sc ");
			hql.append("where sc.id = ? ");
			parametros.add(idSolicitacaoContratacao);
		}

		if (blOrigemRota != null) {
			hql.append("and fr.blOrigemRota = ? ");
			parametros.add(blOrigemRota);
		}
		else
		if (blDestinoRota != null) {
			hql.append("and fr.blDestinoRota = ? ");
			parametros.add(blDestinoRota);
		}

		List<Filial> result = getAdsmHibernateTemplate().find(hql.toString(), parametros.toArray());
		if(!result.isEmpty())
			return result.get(0);
		return null;
	}

	/**
	 * Retorna as filiais do usuario logado, ordenadas por sigla da filial
	 * @author Andresa Vargas
	 * @return
	 */
	public List findComboByUsuarioLogado() {
		List filiais = SessionUtils.getFiliaisUsuarioLogado();

		StringBuffer hql = new StringBuffer();
		hql.append(" select f.idFilial, ");
		hql.append("        f.sgFilial, ");
		hql.append("        f.pessoa.nmFantasia");
		hql.append("   from ");
		hql.append(Filial.class.getName()).append(" f ");
		hql.append("  where ");
		if (filiais != null) {
			hql.append(" f.idFilial in ( :filiais ) ");
		}
		hql.append("order by f.sgFilial");
		List l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "filiais", filiais);

		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
		for (Iterator it = l.iterator(); it.hasNext();) {
			Object[] o = (Object[]) it.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", o[0]);
			tfm.put("siglaNomeFilial", o[1]+" - "+o[2]);

			result.add(tfm);
		}
		return result;
	}

	public Integer findEntidadeByIdFilial(Class clazz,Long idFilial, YearMonthDay dtVigenciaFinalMax) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(clazz,"D")
			.add(Restrictions.eq("D.filial.id",idFilial))
			.add(Restrictions.gt("D.dtVigenciaFinal",dtVigenciaFinalMax))
			.setProjection(Projections.rowCount()));
	}

	public Integer findCepRotaColetaEntrega(Long idFilial, YearMonthDay dtVigenciaFinalMax) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(RotaColetaEntrega.class,"D")
				.add(Restrictions.eq("D.filial.id",idFilial))
				.createAlias("D.rotaIntervaloCeps","RIC")
				.add(Restrictions.ge("RIC.dtVigenciaFinal",dtVigenciaFinalMax))
				.setProjection(Projections.rowCount()));
	}

	/**
	 * Retorna o campo pcJuroDiario da filial informada
	 *
	 * @author Mickaël Jalbert
	 * @since 25/07/2006
	 *
	 * @param Long idFilial
	 * @return BigDecimal
	 */
	public BigDecimal findPcJuroDiario(Long idFilial){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("fil.pcJuroDiario");
		hql.addFrom(Filial.class.getName(), "fil");
		hql.addCriteria("fil.id", "=", idFilial);

		List<BigDecimal> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if(!result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public void deteleFotosFilial(Long idFilial) {
		String hql = new StringBuffer("SELECT FF FROM ").append(FotoFilial.class.getName()).append(" AS FF WHERE FF.filial.id = ?").toString();
		List<FotoFilial> fotos = getAdsmHibernateTemplate().find(hql,new Object[]{idFilial});
		for(FotoFilial fotoFilial : fotos) {
			getAdsmHibernateTemplate().delete(fotoFilial);
		}
	}

	public List<Map<String, Object>> findFilialByMatriculaUsuario(String nrMatricula) {
		SqlTemplate sql = this.createHQLFilial(nrMatricula);
		StringBuffer projecao = new StringBuffer()
			.append("new Map ( " )
			.append("filial.sgFilial as sgFilial, ")
			.append("filial.idFilial as idFilial, ")
			.append("pessoa.nmFantasia as nmFilial) ");

		sql.addProjection(projecao.toString());

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	private SqlTemplate createHQLFilial(String nrMatricula) {
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(Funcionario.class.getName(), "as fun " +
				"INNER JOIN fun.filial as filial " +
				"INNER JOIN filial.pessoa as pessoa");
		sql.addCriteria("fun.nrMatricula", "=", nrMatricula);
		return sql;
	}

	//###############################QUERY DA FILIAL################################################//

	/*Find padrão da lookup*/
	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa", FetchMode.JOIN);
	}

	/*Find lookup filial customizado*/
	public List<Map<String, Object>> findLookupFilial(Map<String, Object> map) {
		Object[] obj = createHqlFindLookup(map);
		return getHibernateTemplate().findByNamedParam((String)obj[0],(String[])obj[2],(Object[])obj[1]);
	}

	/*Método que cria o sql padrão para o lookup*/
	private Object[] createHqlFindLookup(Map<String, Object> criteria) {
		StringBuffer query = new StringBuffer(500)
			.append("select new map(filial.idFilial as idFilial, ")
			.append("pessoa.tpIdentificacao as pessoa_tpIdentificacao, ")
			.append("pessoa.nrIdentificacao as pessoa_nrIdentificacao, ")
			.append("pessoa.nmFantasia as pessoa_nmFantasia, ")
			.append("filial.dtImplantacaoLMS as dtImplantacaoLMS,")
			.append("filial.sgFilial as sgFilial, ")
			.append("filial.sgFilial || ' - ' || pessoa.nmFantasia as siglaNomeFilial, ")
			.append("moeda.sgMoeda || ' ' || moeda.dsSimbolo as moeda_siglaSimbolo, ")
			.append("empresa.idEmpresa as empresa_idEmpresa, ")
			.append("empresa.tpEmpresa as empresa_tpEmpresa, ")
			.append("empresaPessoa.nmPessoa as empresa_pessoa_nmPessoa, ")
			.append("empresaPessoa.nrIdentificacao as empresa_pessoa_nrIdentificacao, ")
			.append("empresaPessoa.tpIdentificacao as empresa_pessoa_tpIdentificacao, ")
			//blSorter
			.append("filial.blSorter as blSorter, ")

			.append("lastHistoricoFilial.tpFilial as lastHistoricoFilial_tpFilial , ")
			.append("lastHistoricoFilial.dtRealOperacaoInicial as lastHistoricoFilial_dtRealOperacaoInicial, ")
			.append("lastHistoricoFilial.dtRealOperacaoFinal as lastHistoricoFilial_dtRealOperacaoFinal) ")
			.append("from ").append(Filial.class.getName()).append(" as filial ")
			.append("inner join filial.pessoa as pessoa ")
			.append("left join filial.moeda as moeda ")
			.append("inner join filial.empresa as empresa ")
			.append("inner join empresa.pessoa as empresaPessoa ");

		Map<String, Object> mapRegional = null;
		if (criteria.get("regionalFiliais") != null){
			mapRegional = MapUtils.getMap(MapUtils.getMap(criteria, "regionalFiliais"), "regional");
			if (!StringUtils.isBlank((String)mapRegional.get("idRegional"))) {
				query.append("inner join filial.regionalFiliais as regionalFiliais ")
				.append("inner join regionalFiliais.regional as regional ");
			}
		}

		query.append("inner join filial.historicoFiliais as lastHistoricoFilial ")
			.append("where lastHistoricoFilial.id = ")
			.append("(select max(hf2.idHistoricoFilial) ")
			.append("from ").append(HistoricoFilial.class.getName()).append(" as hf2 ")
			.append(" where hf2.filial = lastHistoricoFilial.filial)");

		List<Object> objs = new ArrayList<Object>();
		List<String> objsName = new ArrayList<String>();
		if (mapRegional != null && !StringUtils.isBlank((String)mapRegional.get("idRegional"))) {
			query.append(" and regionalFiliais.id = (SELECT max(RE.id) FROM ")
				.append(RegionalFilial.class.getName()).append(" as RE ")
				.append("WHERE RE.id = regionalFiliais.id  ")
				.append("AND RE.dtVigenciaInicial <= SYSDATE  ")
				.append("AND (RE.dtVigenciaFinal IS NULL OR RE.dtVigenciaFinal >= SYSDATE)) ")
				.append(" and regional.dtVigenciaInicial <= SYSDATE ")
				.append(" and (regional.dtVigenciaFinal IS NULL OR regional.dtVigenciaFinal >= SYSDATE)");
			addHql(query,"regional.id = :idRegional");
			objs.add(LongUtils.getLong(mapRegional.get("idRegional")));

			objsName.add("idRegional");
		}

		/*Filtro sigla da filial*/
		addHql(query, " lower(filial.sgFilial) like lower(:sgFilial)");
		objs.add((String)criteria.get("sgFilial") );
		objsName.add("sgFilial");

		String flagType = ((String)criteria.get("flagType") == null) ? "" : (String)criteria.get("flagType");
		String flag = ((String)criteria.get("flag") == null) ? "" : (String)criteria.get("flag");
		String[] types = getTypes(flagType,flag);
		addHql(query, "lastHistoricoFilial.tpFilial in(:inFilial)");
		objs.add(types);
		objsName.add("inFilial");

		//Filtro pelo tipo de filial informado fora dos flags
		String tpFilial = (String)criteria.get("historicoFiliais.tpFilial");
		if (StringUtils.isNotBlank(tpFilial)){
			addHql(query, "lastHistoricoFilial.tpFilial = :paramTpFilial");
			objs.add(tpFilial);
			objsName.add("paramTpFilial");
		}

		Boolean blVigente = MapUtils.getBoolean(criteria, "blVigente");
		if((blVigente == null) || blVigente) {
			addHql(query, "lastHistoricoFilial.dtRealOperacaoInicial <= :dtVigencia AND (lastHistoricoFilial.dtRealOperacaoFinal is null OR lastHistoricoFilial.dtRealOperacaoFinal >= :dtVigencia)");
			objs.add(JTDateTimeUtils.getDataAtual());
			objsName.add("dtVigencia");
		}
		//Pesquisa filial que tenha BL_SORTER =TRUE

		if (criteria.get("flagBlSorter") != null) {
			Boolean blSorter = MapUtils.getBoolean(criteria,"flagBlSorter");

			addHql(query, "filial.blSorter = :blSorter");
			objs.add(blSorter);
			objsName.add("blSorter");

		}

		String tpAcesso = MapUtils.getString(criteria, "tpAcesso");
		if (StringUtils.isNotBlank(tpAcesso)) {
			/*TIPO DE ACESSO A- ABERTO*/

			/*Filtro para empresa*/
			if (StringUtils.isNotBlank((String)criteria.get("empresa.idEmpresa"))) {
				addHql(query,"empresa.idEmpresa = :idEmpresa");
				objs.add(LongUtils.getLong(criteria.get("empresa.idEmpresa")));
				objsName.add("idEmpresa");
			}

			/*Filtro para tipo de empresa*/
			if (criteria.get("empresa") != null) {
				String tpEmpresa = MapUtils.getString(MapUtils.getMap(criteria, "empresa"), "tpEmpresa");
				if(StringUtils.isNotBlank(tpEmpresa)) {
					addHql(query, "empresa.tpEmpresa = :tpEmpresa");
					objs.add(tpEmpresa);
					objsName.add("tpEmpresa");
				}
			}

			if("F".equals(tpAcesso)) {
			/*TIPO DE ACESSO F- FILIAIS QUE O USUARIO LOGADO TEM ACESSO*/
				List<Long> idsFiliaisUsuarioLogado = findIdsFiliaisUsuarioLogado();
				addHql(query,"filial.idFilial in (:idsFiliais)");
				objs.add(idsFiliaisUsuarioLogado);
				objsName.add("idsFiliais");
			}
		} else {
			/*Sempre busca as filiais da empresa do usuário logado*/
			addHql(query,"empresa.idEmpresa = :idEmpresa");
			objs.add(SessionUtils.getEmpresaSessao().getIdEmpresa());
			objsName.add("idEmpresa");
		}

		return new Object[]{query.toString(),objs.toArray(), objsName.toArray(new String[] {})};
	}

	/**
	 * Método sobrecarregado com mais um parametro "String tpEmpresa"
	 * @param sgFilial
	 * @param tpAcesso
	 * @param idEmpresa
	 * @return
	 */
	public List findLookupBySgFilial(String sgFilial, String tpAcesso, Long idEmpresa) {
		return findLookupBySgFilial(sgFilial, tpAcesso, idEmpresa, null);
	}

	/*Find lookup da filial que pesquisa somente pela sigla*/
	public List findLookupBySgFilial(String sgFilial, String tpAcesso, Long idEmpresa, String tpEmpresa) {

		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("f.idFilial"), "idFilial")
		.add(Projections.property("f.sgFilial"), "sgFilial")
		.add(Projections.property("pf.idPessoa"), "pessoa_idPessoa")
		.add(Projections.property("pf.nmFantasia"), "pessoa_nmFantasia")
		.add(Projections.property("e.tpEmpresa"), "empresa_tpEmpresa")
		.add(Projections.property("e.idEmpresa"), "empresa_idEmpresa")
		.add(Projections.property("pe.nmPessoa"), "empresa_pessoa_nmPessoa");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
		.createAlias("f.pessoa", "pf")
		.createAlias("f.empresa", "e")
		.createAlias("e.pessoa", "pe")
		.setProjection(pl)
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance())
		.add(Restrictions.ilike("f.sgFilial", sgFilial, MatchMode.EXACT));

		/*Programação dos critérios de acesso aos dados das filiais*/
		if(StringUtils.isNotBlank(tpAcesso)) {
			if("A".equals(tpAcesso)) {
				/*TIPO DE ACESSO A - ABERTO*/
				//fica aberto com o critério da sigla da filial tratado acima
			} else {
				/*TIPO DE ACESSO F - FILIAIS QUE O USUARIO LOGADO TEM ACESSO*/
				List<Long> idsFiliaisUsuarioLogado = findIdsFiliaisUsuarioLogado();
				dc.add(Restrictions.in("f.id", idsFiliaisUsuarioLogado));
			}
		} else {
			/*Sempre busca as filiais da empresa do usuário logado*/
			dc.add(Restrictions.eq("e.id", idEmpresa));
		}

		if (tpEmpresa != null){
			dc.add(Restrictions.eq("e.tpEmpresa",tpEmpresa));
		}

		return findPaginatedByDetachedCriteria(dc, Integer.valueOf(1), Integer.valueOf(2)).getList();
	}

	/*Find lookup da filial que utiliza os mesmo sql do paginated*/
	public List findLookupAsPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),Integer.valueOf(1),Integer.valueOf(2),sql.getCriteria()).getList();
	}

	/*Método que cria o sql padrão para o paginated e lookup*/
	private SqlTemplate createHqlPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		// Projections
		sql.addProjection("new map( filial.idFilial","idFilial");
		sql.addProjection("filial.codFilial","cdFilial");
		sql.addProjection("pessoa.tpIdentificacao","pessoa_tpIdentificacao");
		sql.addProjection("pessoa.nrIdentificacao","pessoa_nrIdentificacao");
		sql.addProjection("pessoa.idPessoa", "pessoa_idPessoa");
		sql.addProjection("moeda.sgMoeda || ' ' || moeda.dsSimbolo","moeda_siglaSimbolo");
		sql.addProjection("pessoa.nmFantasia","pessoa_nmFantasia");
		sql.addProjection("pessoa.nmFantasia","nmFantasia");
		sql.addProjection("filial.sgFilial","sgFilial");
		sql.addProjection("filialByIdFilialResponsavel.sgFilial","filialByIdFilialResponsavel_sgFilial");
		sql.addProjection("responsavelPessoa.nmFantasia","filialByIdFilialResponsavel_pessoa_nmFantasia");
		sql.addProjection("filialByIdFilialResponsavalAwb.sgFilial","filialByIdFilialResponsavalAwb_sgFilial");
		sql.addProjection("responsavalAwbPessoa.nmFantasia","filialByIdFilialResponsavalAwb_pessoa_nmFantasia");
		sql.addProjection("lastHistoricoFilial.tpFilial","lastHistoricoFilial_tpFilial");
		sql.addProjection("lastHistoricoFilial.dtRealOperacaoInicial","lastHistoricoFilial_dtRealOperacaoInicial");
		sql.addProjection("lastHistoricoFilial.dtRealOperacaoFinal","lastHistoricoFilial_dtRealOperacaoFinal");
		sql.addProjection("filial.dtImplantacaoLMS","dtImplantacaoLMS");
		sql.addProjection("empresa.idEmpresa","empresa_idEmpresa");
		sql.addProjection("empresa.tpEmpresa","empresa_tpEmpresa");
		sql.addProjection("empresaPessoa.nmPessoa","empresa_pessoa_nmPessoa");
		sql.addProjection("empresaPessoa.nrIdentificacao","empresa_pessoa_nrIdentificacao");
		sql.addProjection("empresaPessoa.tpIdentificacao","empresa_pessoa_tpIdentificacao)");

		//From,
		StringBuffer froms = new StringBuffer();
		froms.append( Filial.class.getName() ).append(" as filial ")
		.append("inner join filial.pessoa as pessoa ")
		.append("inner join filial.empresa as empresa ")
		.append("inner join empresa.pessoa as empresaPessoa ")
		.append("left join filial.filialByIdFilialResponsavel as filialByIdFilialResponsavel ")
		.append("left join filial.filialByIdFilialResponsavalAwb as filialByIdFilialResponsavalAwb ")
		.append("left join filialByIdFilialResponsavel.pessoa as responsavelPessoa ")
		.append("left join filialByIdFilialResponsavalAwb.pessoa as responsavalAwbPessoa ")
		.append("left join filial.moeda as moeda ");

		if (criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa") != null)
			froms.append("inner join pessoa.enderecoPessoas as enderecoPessoas ")
				.append("inner join enderecoPessoas.municipio as municipio ")
				.append("inner join municipio.unidadeFederativa as unidadeFederativa ");

		if (criteria.getLong("regionalFiliais.regional.idRegional") != null )
			froms.append("left join filial.regionalFiliais as regionalFiliais ")
				.append("left join regionalFiliais.regional as regional ");

		froms.append("left join filialByIdFilialResponsavalAwb.pessoa as responsavalAwbPessoa ")
			.append("left join filial.historicoFiliais as lastHistoricoFilial ");

		sql.addFrom(froms.toString());

		//Critérios
		sql.addCustomCriteria("lastHistoricoFilial.id = (select max(hf2.idHistoricoFilial) from "+HistoricoFilial.class.getName()+" as hf2 where hf2.filial = lastHistoricoFilial.filial) ");

		if (criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa") != null) {
			sql.addCustomCriteria(new StringBuffer("(enderecoPessoas.id = (SELECT max(EP.id) FROM ").append(EnderecoPessoa.class.getName()).append(" as EP WHERE EP.pessoa.idPessoa = enderecoPessoas.pessoa.idPessoa AND EP.dtVigenciaInicial <= ? AND (EP.dtVigenciaFinal IS NULL OR EP.dtVigenciaFinal >= ?)))").toString());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

			sql.addCriteria("unidadeFederativa.idUnidadeFederativa","=",criteria.getLong("pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa"));
		}

		if (criteria.getLong("regionalFiliais.regional.idRegional") != null ) {
			sql.addCustomCriteria(new StringBuffer("regionalFiliais.id = (SELECT max(RE.id) FROM ").append(RegionalFilial.class.getName()).append(" as RE WHERE RE.id = regionalFiliais.id AND RE.dtVigenciaInicial <= ? AND (RE.dtVigenciaFinal IS NULL OR RE.dtVigenciaFinal >= ?)) ").toString());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

			sql.addCriteria("regional.idRegional","=",criteria.getLong("regionalFiliais.regional.idRegional"));
		}

		/*Criterio usado para o findById, não segue os critérios abaixo*/
		if (criteria.getLong("idFilial") != null) {
			sql.addCriteria("filial.idFilial","=",criteria.getLong("idFilial"));
			return sql;
		}

		/*Criterio do workflow*/
		sql.addCriteria("filial.idFilial","=",criteria.getLong("idProcessoWorkflow"));

		if (StringUtils.isNotBlank(criteria.getString("sgFilial"))) {
			sql.addCustomCriteria("lower(filial.sgFilial) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("sgFilial"));
		}

		if (criteria.getYearMonthDay("dtImplantacaoLMS") != null)
			sql.addCriteria("filial.dtImplantacaoLMS","=",criteria.getYearMonthDay("dtImplantacaoLMS"));

		if (criteria.get("flagBlSorter") != null) {
			Boolean blSorter = MapUtils.getBoolean(criteria,"flagBlSorter");

			sql.addCriteria("filial.blSorter", "=",blSorter);
		}

		if (StringUtils.isNotBlank(criteria.getString("pessoa.tpIdentificacao")))
			sql.addCriteria("pessoa.tpIdentificacao","=",criteria.getString("pessoa.tpIdentificacao"));

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nrIdentificacao"))) {
			sql.addCustomCriteria("lower(pessoa.nrIdentificacao) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nrIdentificacao"));
		}

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nmFantasia"))) {
			sql.addCustomCriteria("lower(pessoa.nmFantasia) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nmFantasia"));
		}

		if (StringUtils.isNotBlank(criteria.getString("pessoa.nmPessoa"))) {
			sql.addCustomCriteria("lower(pessoa.nmPessoa) LIKE lower(?)");
			sql.addCriteriaValue(criteria.getString("pessoa.nmPessoa"));
		}

		if (criteria.getYearMonthDay("historicoFiliais.vigenteEm") != null) {
			sql.addCustomCriteria("lastHistoricoFilial.dtRealOperacaoInicial <= ? AND (lastHistoricoFilial.dtRealOperacaoFinal is null OR lastHistoricoFilial.dtRealOperacaoFinal >= ?)");
			sql.addCriteriaValue(criteria.getYearMonthDay("historicoFiliais.vigenteEm"));
			sql.addCriteriaValue(criteria.getYearMonthDay("historicoFiliais.vigenteEm"));
		}

		if (StringUtils.isNotBlank(criteria.getString("historicoFiliais.tpFilial")))
			sql.addCriteria("lastHistoricoFilial.tpFilial","=",criteria.getString("historicoFiliais.tpFilial"));

		if (criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoInicial") != null)
			sql.addCriteria("lastHistoricoFilial.dtRealOperacaoInicial",">=",criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoInicial"));

		if (criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoFinal") != null)
			sql.addCriteria("lastHistoricoFilial.dtRealOperacaoFinal","<=",criteria.getYearMonthDay("historicoFiliais.dtRealOperacaoFinal"));

		if (criteria.getLong("filialByIdFilialResponsavel.idFilial") != null)
			sql.addCriteria("filialByIdFilialResponsavel.id","=",criteria.getLong("filialByIdFilialResponsavel.idFilial"));

		if (criteria.getLong("filialByIdFilialResponsavalAwb.idFilial") != null)
			sql.addCriteria("filialByIdFilialResponsavalAwb.id","=",criteria.getLong("filialByIdFilialResponsavalAwb.idFilial"));

		if (criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.idDivisaoGrupoClassificacao") != null) {
			sql.addCustomCriteria(new StringBuffer("filial.id in (Select distinct(GC.filial.idFilial) from ")
						.append(GrupoClassificacaoFilial.class.getName()).append(" AS GC where GC.divisaoGrupoClassificacao.id = ?)").toString());
			sql.addCriteriaValue(criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.idDivisaoGrupoClassificacao"));
		}

		if (criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao") != null) {
			sql.addCustomCriteria(new StringBuffer("filial.id in (Select distinct(GC2.filial.idFilial) from ")
						.append(GrupoClassificacaoFilial.class.getName()).append(" GC2 where GC2.divisaoGrupoClassificacao.grupoClassificacao.id = ?)").toString());
			sql.addCriteriaValue(criteria.getLong("grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"));
		}

		if (StringUtils.isNotBlank(criteria.getString("obFilial"))) {
			String[] tp = ((String) criteria.get("obFilial")).split(":");
			String[] tpFilial = getTypes(tp[0], tp[1]);
			StringBuffer criterios = new StringBuffer();
			char token = ' ';
			for(int x = 0; x < tpFilial.length; x++) {
				criterios.append(token).append("?");
				token = ',';
				sql.addCriteriaValue(tpFilial[x]);
			}
			sql.addCustomCriteria(new StringBuffer("lastHistoricoFilial.tpFilial in (").append(criterios).append(") ").toString());
		}

		/*Programação dos critérios de acesso aos dados das filiais*/
		String tpAcesso = criteria.getString("tpAcesso");
		if (StringUtils.isNotBlank(tpAcesso)){
			/*TIPO DE ACESSO A- ABERTO*/
			if (criteria.getLong("empresa.idEmpresa") != null)
				sql.addCriteria("empresa.idEmpresa","=",criteria.getLong("empresa.idEmpresa"));

			if (StringUtils.isNotBlank(criteria.getString("empresa.tpEmpresa")))
				sql.addCriteria("empresa.tpEmpresa","=",criteria.getString("empresa.tpEmpresa"));

			if("F".equals(tpAcesso)) {
			/*TIPO DE ACESSO F- FILIAIS QUE O USUARIO LOGADO TEM ACESSO*/
				//se não for uma filial via processo de workflow
				if (criteria.getLong("idProcessoWorkflow")==null){
					List<Long> idsFiliaisUsuarioLogado = findIdsFiliaisUsuarioLogado();
					SQLUtils.joinExpressionsWithComma(idsFiliaisUsuarioLogado, sql, "filial.idFilial");
				}
			}
		} else {
			/*Sempre busca as filiais da empresa do usuário logado*/
			sql.addCriteria("empresa.idEmpresa","=",SessionUtils.getEmpresaSessao().getIdEmpresa());
		}

		sql.addOrderBy("filial.sgFilial");
		sql.addOrderBy("pessoa.nmFantasia");
		return sql;
	}

	/*Método que busca as filiais do usuário logado e retorna os ids das filiais*/
	private List<Long> findIdsFiliaisUsuarioLogado(){
		List<Filial> filiaisUsuario = SessionUtils.getFiliaisUsuarioLogado();
		List<Long> result = new ArrayList<Long>();
		for(Filial filial : filiaisUsuario) {
			result.add(filial.getIdFilial());
		}
		return result;
	}

	/**
	 * Busca a filial de acordo com a empresa e a sigla
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 08/12/2006
	 *
	 * @param sgFilial
	 * @param idEmpresa
	 * @return Filial
	 */
	public Filial findFilial(Long idEmpresa, String sgFilial) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection( "f" );

		hql.addInnerJoin(Filial.class.getName() + " f ");
		hql.addInnerJoin("f.empresa", "e");

		hql.addCriteria("e.id", "=", idEmpresa);
		hql.addCriteria("lower(f.sgFilial)", "=", sgFilial.toLowerCase());

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String findSgFilialLegadoByIdFilial(Long idFilial) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT F_INT_DE_PARA('FILIAL_SIGLA', ?, 3) AS SIGLA FROM dual");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("SIGLA", Hibernate.STRING);
			}
		};

		List<Long> param = new ArrayList<Long>();
		param.add(idFilial);

		List<String> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty())
			return null;

		return result.get(0).trim().toUpperCase();
	}

	public String findSgFilialLegadoByIdFilialCooperada(Long idFilial) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT F_INT_DE_PARA('UF_FILIAL_COOPERADA', ?, 3) AS SIGLA FROM dual");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("SIGLA", Hibernate.STRING);
			}
		};

		List<Long> param = new ArrayList<Long>();
		param.add(idFilial);

		List<String> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty())
			return null;

		return result.get(0).trim().toUpperCase();
	}


	public Long findIdFilialBySgFilialLegado(String sgFilialLegado) {
		StringBuffer sql = new StringBuffer()

		/*
		 * LMS-1149: Corrigir find pela tabela filial_sigla
		 */
		.append("SELECT F_INT_DE_PARA('FILIAL_SIGLA', ?, 4) AS ID FROM dual");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID", Hibernate.LONG);
			}
		};

		List<String> param = new ArrayList<String>();
		param.add(sgFilialLegado);

		List<Long> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty())
			return null;

		return result.get(0);
	}

	/**
	 * Solicitação CQPRO00005943 / CQPRO00007109 da Integração.
	 * @param tpEmpresa
	 * @param vigenteEm
	 * @return
	 */
	public List<Filial> findByTpEmpresa(String tpEmpresa, YearMonthDay vigenteEm){
		StringBuilder hql = new StringBuilder();
		hql.append("select distinct fi from Filial fi, Empresa em ")
			.append("where fi.empresa = em ")
			.append("and em.tpEmpresa = :tpEmpresa ")
			.append("and exists ( select 1 from HistoricoFilial hf ")
			.append("where hf.dtRealOperacaoInicial <= :dtRealOperacaoInicial ")
			.append("and hf.dtRealOperacaoFinal is null or hf.dtRealOperacaoFinal >= :dtRealOperacaoFinal ")
			.append("and hf.filial = fi ")
			.append("and (hf.tpFilial = :tpFilial1 or hf.tpFilial = :tpFilial2))");

		String[] paramNames = {"tpEmpresa", "dtRealOperacaoInicial", "dtRealOperacaoFinal", "tpFilial1", "tpFilial2"};
		Object[] paramValues = {tpEmpresa, vigenteEm, vigenteEm, "FI", "FR"};
		return getHibernateTemplate().findByNamedParam(hql.toString(), paramNames, paramValues);
	}


	/**
	 * Obtém um objeto Filial a partir do código da filial recebido por parâmetro.
	 *
	 * @author Aleksander Kostylew
	 * @param codFilial Integer com o código da filial a consultar.
	 * @return Retorna o objeto Filial selecionado pelo código da filial.
	 */
	public Filial findFilialByCodigoFilial(Integer codFilial) {
		StringBuilder hql = new StringBuilder();
		hql.append("select fi from Filial fi ")
			.append("where fi.codFilial = :codFilial ");

		if (getHibernateTemplate().findByNamedParam(hql.toString(), "codFilial", codFilial).size() > 0)
			return (Filial) getHibernateTemplate().findByNamedParam(hql.toString(), "codFilial", codFilial).get(0);
		else
			return null;
	}

	public List findFiliaisEmpresa() {

		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("empresa", "e")
		.createAlias("e.pessoa", "p")
		.createAlias("p.enderecoPessoa", "ep")
		.createAlias("ep.municipio", "mun")
		.add(Restrictions.eq("e.tpEmpresa", "M"));

		return findByDetachedCriteria(dc);
	}


	public List<Depot> findDepot() {
		StringBuilder sb = sqlDepotDefault();
		sb.append("GROUP BY F.ID_FILIAL, UF.NM_UNIDADE_FEDERATIVA, M.NM_MUNICIPIO ");
		sb.append("ORDER BY UF.NM_UNIDADE_FEDERATIVA ");

		List lista =  getAdsmHibernateTemplate().findPaginatedBySql(sb.toString(), Integer.valueOf(1), Integer.valueOf(100000), new String[]{}, getConfigSqlDepot()).getList();

		return convertDepots(lista);
	}

	public Depot findDepotByIdFilial(Long idFilial) {
		StringBuilder sb = sqlDepotDefault();
		sb.append(" AND F.ID_FILIAL = :idFilial ");
		sb.append("GROUP BY F.ID_FILIAL, UF.NM_UNIDADE_FEDERATIVA, M.NM_MUNICIPIO ");
		sb.append("ORDER BY UF.NM_UNIDADE_FEDERATIVA ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idFilial", idFilial);
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sb.toString(), params, getConfigSqlDepot());
		Depot depot = null;
		if(result  != null && !result.isEmpty()){
			depot = convertDepots(result).get(0);
		}
		return depot;
	}

	private StringBuilder sqlDepotDefault() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("F.ID_FILIAL code, ");
		sb.append("Max(P.NM_FANTASIA) description, ");
		sb.append("Max(F.SG_FILIAL) acronym, ");
		sb.append("Max(TL.DS_TIPO_LOGRADOURO_I) street,");
		sb.append("Max(EP.DS_ENDERECO) addressDescription, ");
		sb.append("Max(EP.NR_ENDERECO) addressNumber, ");
		sb.append("Max(EP.DS_COMPLEMENTO) addressComplement, ");
		sb.append("Max(EP.DS_BAIRRO) district,");
		sb.append("Max(EP.NR_CEP) zipCode, ");
		sb.append("Max(P.DS_EMAIL) email, ");
		sb.append("Max(TE.NR_DDD) phoneAreaCode, ");
		sb.append("Max(TE.NR_TELEFONE) phoneNumber, ");
		sb.append("Max(TE1.NR_DDD) faxAreaCode, ");
		sb.append("Max(TE1.NR_TELEFONE) faxNumber, ");
		sb.append("Max(EP.NR_LATITUDE_TMP) nrLatitude,");
		sb.append("Max(EP.NR_LONGITUDE_TMP) nrLongitude, ");
		sb.append("Max(EP.NR_QUALIDADE) nrQuality, ");
		sb.append("Max(UF.ID_UNIDADE_FEDERATIVA) idState, ");
		sb.append("UF.NM_UNIDADE_FEDERATIVA nameState, ");
		sb.append("Max(UF.SG_UNIDADE_FEDERATIVA) acronymState, ");
		sb.append("Max(M.ID_MUNICIPIO) idCity, ");
		sb.append("Max(M.NM_MUNICIPIO) nameCity ");
		sb.append("FROM ");
		sb.append("FILIAL F, ");
		sb.append("HISTORICO_FILIAL H, ");
		sb.append("PESSOA P, ");
		sb.append("ENDERECO_PESSOA EP, ");
		sb.append("TELEFONE_ENDERECO TE, ");
		sb.append("TELEFONE_ENDERECO TE1, ");
		sb.append("TIPO_LOGRADOURO TL, ");
		sb.append("MUNICIPIO M, ");
		sb.append("UNIDADE_FEDERATIVA UF ");
		sb.append("WHERE ");
		sb.append("H.ID_FILIAL = F.ID_FILIAL ");
		sb.append("AND F.TP_SISTEMA = 'LMS' ");
		sb.append("AND F.ID_EMPRESA = 361 ");
		sb.append("AND H.DT_REAL_OPERACAO_FINAL= To_Date('01/01/4000','DD/MM/YYYY') ");
		sb.append("AND P.ID_PESSOA = F.ID_FILIAL ");
		sb.append("AND EP.ID_ENDERECO_PESSOA = P.ID_ENDERECO_PESSOA ");
		sb.append("AND EP.DT_VIGENCIA_FINAL = To_Date('01/01/4000','DD/MM/YYYY') ");
		sb.append("AND EP.NR_LATITUDE_TMP IS NOT NULL ");
		sb.append("AND EP.NR_LONGITUDE_TMP IS NOT NULL ");
		sb.append("AND TE.ID_PESSOA (+) = P.ID_PESSOA ");
		sb.append("AND M.ID_MUNICIPIO = EP.ID_MUNICIPIO ");
		sb.append("AND UF.ID_UNIDADE_FEDERATIVA = M.ID_UNIDADE_FEDERATIVA ");
		sb.append("AND UF.ID_PAIS = 30 ");
		sb.append("AND EP.ID_TIPO_LOGRADOURO = TL.ID_TIPO_LOGRADOURO ");
		sb.append("AND TE.ID_PESSOA (+) = P.ID_PESSOA  ");
		sb.append("AND TE.ID_ENDERECO_PESSOA (+) = P.ID_ENDERECO_PESSOA ");
		sb.append("AND TE.TP_USO (+) <> 'FA'  ");
		sb.append("AND TE1.ID_PESSOA (+) = P.ID_PESSOA ");
		sb.append("AND TE1.ID_ENDERECO_PESSOA (+) = P.ID_ENDERECO_PESSOA ");
		sb.append("AND TE1.TP_USO (+) = 'FA' ");
		return sb;
	}

	private List<Depot> convertDepots(List lista){
		List<Depot> retorno = new ArrayList<Depot>();

		for (Iterator iter = lista.iterator(); iter.hasNext();){
			Depot depot = new Depot();
			int i=0;
			Object[] obj = (Object[])iter.next();
			depot.setCode((Long) obj[i++]);
			depot.setDescription((String) obj[i++]);
			depot.setAcronym((String) obj[i++]);
			depot.setStreet((String) obj[i++]);
			depot.setAddressDescription((String) obj[i++]);
			depot.setAddressNumber((String) obj[i++]);
			depot.setAddressComplement((String) obj[i++]);
			depot.setDistrict((String) obj[i++]);
			depot.setZipCode((String) obj[i++]);
			depot.setEmail((String) obj[i++]);
			depot.setPhoneAreaCode((String) obj[i++]);
			depot.setPhoneNumber((String) obj[i++]);
			depot.setFaxAreaCode((String) obj[i++]);
			depot.setFaxNumber((String) obj[i++]);
			depot.setNrLatitude((BigDecimal) obj[i++]);
			depot.setNrLongitude((BigDecimal) obj[i++]);
			depot.setNrQuality((Integer) obj[i++]);
			depot.setState(new State((Long) obj[i++], (String) obj[i++], (String) obj[i++]));
			depot.setCity(new City((Long) obj[i++], (String) obj[i++]));
			retorno.add(depot);
		}

		return retorno;
	}

	private ConfigureSqlQuery getConfigSqlDepot() {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("code", Hibernate.LONG);
				sqlQuery.addScalar("description", Hibernate.STRING);
				sqlQuery.addScalar("acronym", Hibernate.STRING);
				sqlQuery.addScalar("street", Hibernate.STRING);
				sqlQuery.addScalar("addressDescription", Hibernate.STRING);
				sqlQuery.addScalar("addressNumber", Hibernate.STRING);
				sqlQuery.addScalar("addressComplement", Hibernate.STRING);
				sqlQuery.addScalar("district", Hibernate.STRING);
				sqlQuery.addScalar("zipCode", Hibernate.STRING);
				sqlQuery.addScalar("email", Hibernate.STRING);
				sqlQuery.addScalar("phoneAreaCode", Hibernate.STRING);
				sqlQuery.addScalar("phoneNumber", Hibernate.STRING);
				sqlQuery.addScalar("faxAreaCode", Hibernate.STRING);
				sqlQuery.addScalar("faxNumber", Hibernate.STRING);
				sqlQuery.addScalar("nrLatitude", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrLongitude", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrQuality", Hibernate.INTEGER);
				sqlQuery.addScalar("idState", Hibernate.LONG);
				sqlQuery.addScalar("nameState", Hibernate.STRING);
				sqlQuery.addScalar("acronymState", Hibernate.STRING);
				sqlQuery.addScalar("idCity", Hibernate.LONG);
				sqlQuery.addScalar("nameCity", Hibernate.STRING);
			}
		};
		return configSql;
	}

	public List<Depot> findDepotByStateAndCity(TypedFlatMap tfm) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT Max(P.NM_FANTASIA) description, ");
		sb.append("Max(F.SG_FILIAL) acronym, ");
		sb.append("Max(TL.DS_TIPO_LOGRADOURO_I) street, ");
		sb.append("Max(EP.DS_ENDERECO) addressDescription, ");
		sb.append("Max(EP.NR_ENDERECO) addressNumber, ");
		sb.append("Max(EP.DS_COMPLEMENTO) addressComplement, ");
		sb.append("Max(EP.DS_BAIRRO) district, ");
		sb.append("Max(M.NM_MUNICIPIO) cityName, ");
		sb.append("Max(UF.SG_UNIDADE_FEDERATIVA) acronymState, ");
		sb.append("Max(EP.NR_CEP) zipCode, ");
		sb.append("Max(P.DS_EMAIL) email, ");
		sb.append("Max(TE.NR_DDD) phoneAreaCode, ");
		sb.append("Max(TE.NR_TELEFONE) phoneNumber, ");
		sb.append("Max(TE1.NR_DDD) faxAreaCode, ");
		sb.append("Max(TE1.NR_TELEFONE) faxNumber, ");
		sb.append("Max(EP.NR_LATITUDE_TMP) nrLatitude,");
		sb.append("Max(EP.NR_LONGITUDE_TMP) nrLongitude, ");
		sb.append("Max(EP.NR_QUALIDADE) nrQuality ");


		sb.append("FROM  ");
		sb.append("UNIDADE_FEDERATIVA UF1,  ");
		sb.append("MUNICIPIO M1,  ");
		sb.append("MUNICIPIO_FILIAL MF,  ");
		sb.append("FILIAL F,  ");
		sb.append("HISTORICO_FILIAL HF,  ");
		sb.append("PESSOA P,  ");
		sb.append("ENDERECO_PESSOA EP,  ");
		sb.append("TIPO_LOGRADOURO TL,  ");
		sb.append("MUNICIPIO M,  ");
		sb.append("UNIDADE_FEDERATIVA UF,  ");
		sb.append("TELEFONE_ENDERECO TE,  ");
		sb.append("TELEFONE_ENDERECO TE1  ");
		sb.append("WHERE  ");
		sb.append("UF1.ID_PAIS = 30  ");
		sb.append("AND UF1.TP_SITUACAO = 'A'  ");
		sb.append("AND M1.ID_UNIDADE_FEDERATIVA = UF1.ID_UNIDADE_FEDERATIVA  ");
		sb.append("AND M1.TP_SITUACAO = 'A'  ");
		sb.append("AND M1.BL_DISTRITO = 'N'  ");

		//Busca por sigla do estado e nome do município ignorando acentuação.
		sb.append("AND M1.ID_MUNICIPIO  in ( SELECT m.id_municipio FROM MUNICIPIO M, UNIDADE_FEDERATIVA UF  ");
		sb.append("							WHERE UF.ID_UNIDADE_FEDERATIVA = M.ID_UNIDADE_FEDERATIVA  ");
		sb.append("							AND  UF.SG_UNIDADE_FEDERATIVA = ?   ");
		sb.append("							AND UPPER(nlssort(M.NM_MUNICIPIO, 'NLS_SORT=generic_m_ai')) = UPPER(nlssort( ?, 'NLS_SORT=generic_m_ai')) )");

		sb.append("AND MF.ID_MUNICIPIO = M1.ID_MUNICIPIO  ");
		sb.append("AND MF.DT_VIGENCIA_FINAL = To_Date('01/01/4000','DD/MM/YYYY')  ");
		sb.append("AND F.ID_FILIAL = MF.ID_FILIAL  ");
		sb.append("AND HF.ID_FILIAL = F.ID_FILIAL  ");
		sb.append("AND HF.DT_REAL_OPERACAO_FINAL = To_Date('01/01/4000','DD/MM/YYYY')  ");
		sb.append("AND P.ID_PESSOA = F.ID_FILIAL  ");
		sb.append("AND EP.ID_ENDERECO_PESSOA (+) = P.ID_ENDERECO_PESSOA  ");
		sb.append("AND EP.DT_VIGENCIA_FINAL (+) = To_Date('01/01/4000','DD/MM/YYYY')  ");
		sb.append("AND TL.ID_TIPO_LOGRADOURO (+) = EP.ID_TIPO_LOGRADOURO  ");
		sb.append("AND M.ID_MUNICIPIO = EP.ID_MUNICIPIO  ");
		sb.append("AND UF.ID_UNIDADE_FEDERATIVA = M.ID_UNIDADE_FEDERATIVA  ");
		sb.append("AND TE.ID_PESSOA (+) = P.ID_PESSOA  ");
		sb.append("AND TE.ID_ENDERECO_PESSOA (+) = P.ID_ENDERECO_PESSOA  ");
		sb.append("AND TE.TP_USO (+) <> 'FA'  ");
		sb.append("AND TE1.ID_PESSOA (+) = P.ID_PESSOA  ");
		sb.append("AND TE1.ID_ENDERECO_PESSOA (+) = P.ID_ENDERECO_PESSOA  ");
		sb.append("AND TE1.TP_USO (+) = 'FA'  ");
		sb.append("GROUP BY MF.ID_FILIAL, F.SG_FILIAL ");

		List lista =  getAdsmHibernateTemplate().findPaginatedBySql(sb.toString(), Integer.valueOf(1), Integer.valueOf(100000), new String[]{tfm.getString("stateAcronym"), tfm.getString("cityName")}, getConfigSqlDepotByStateAndCity()).getList();

		return convertDepostByStateAndCity(lista);

	}

	private ConfigureSqlQuery getConfigSqlDepotByStateAndCity() {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("description", Hibernate.STRING);
				sqlQuery.addScalar("acronym", Hibernate.STRING);
				sqlQuery.addScalar("street", Hibernate.STRING);
				sqlQuery.addScalar("addressDescription", Hibernate.STRING);
				sqlQuery.addScalar("addressNumber", Hibernate.STRING);
				sqlQuery.addScalar("addressComplement", Hibernate.STRING);
				sqlQuery.addScalar("district", Hibernate.STRING);
				sqlQuery.addScalar("cityName", Hibernate.STRING);
				sqlQuery.addScalar("acronymState", Hibernate.STRING);
				sqlQuery.addScalar("zipCode", Hibernate.STRING);
				sqlQuery.addScalar("email", Hibernate.STRING);
				sqlQuery.addScalar("phoneAreaCode", Hibernate.STRING);
				sqlQuery.addScalar("phoneNumber", Hibernate.STRING);
				sqlQuery.addScalar("faxAreaCode", Hibernate.STRING);
				sqlQuery.addScalar("faxNumber", Hibernate.STRING);
				sqlQuery.addScalar("nrLatitude", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrLongitude", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrQuality", Hibernate.INTEGER);
			}
		};
		return configSql;
	}

	private List<Depot> convertDepostByStateAndCity(List lista){
		List<Depot> retorno = new ArrayList<Depot>();

		for (Iterator iter = lista.iterator(); iter.hasNext();){
			Depot depot = new Depot();
			int i=0;
			Object[] obj = (Object[])iter.next();
			depot.setDescription((String) obj[i++]);
			depot.setAcronym((String) obj[i++]);
			depot.setStreet((String) obj[i++]);
			depot.setAddressDescription((String) obj[i++]);
			depot.setAddressNumber((String) obj[i++]);
			depot.setAddressComplement((String) obj[i++]);
			depot.setDistrict((String) obj[i++]);
			depot.setCity(new City(null, (String) obj[i++]));
			depot.setState(new State(null, null, (String) obj[i++]));
			depot.setZipCode((String) obj[i++]);
			depot.setEmail((String) obj[i++]);
			depot.setPhoneAreaCode((String) obj[i++]);
			depot.setPhoneNumber((String) obj[i++]);
			depot.setFaxAreaCode((String) obj[i++]);
			depot.setFaxNumber((String) obj[i++]);
			depot.setNrLatitude((BigDecimal) obj[i++]);
			depot.setNrLongitude((BigDecimal) obj[i++]);
			depot.setNrQuality((Integer) obj[i++]);
			retorno.add(depot);
		}

		return retorno;
	}

	/**
	 * Serviço para o app (iPhone e Android).
	 * @return lista de filiais.
	 */
	public List<Filial> findListFilial() {
		StringBuffer sb = new StringBuffer()
		.append(" select f")
		.append(" from ").append(getPersistentClass().getName()).append(" as f")
		.append(" where ( f.empresa.id = 361")
		.append(" or f.empresa.id = 637) ")
		.append(" and pessoa.idPessoa = f.idFilial")
		.append(" and pessoa.nmFantasia is not null")
		.append(" order by pessoa.nmFantasia asc ");

		return getAdsmHibernateTemplate().find(sb.toString(), null);
	}


	public Filial findFilialByUsuarioAndEmpresaAndFilial(Usuario usuario, Empresa empresa, Long idFilial) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select fili ");
		hql.append("From Filial fili ");
		hql.append("where exists ( ");
		hql.append("	select fill ");
		hql.append("	from FilialUsuario fius ");
		hql.append("	join fius.empresaUsuario emus ");
		hql.append("	join emus.empresa empr ");
		hql.append("	join fius.filial fill ");
		hql.append("	join emus.usuario usua ");
		hql.append("	where fill = fili  ");
		hql.append("	and usua = ? ");
		hql.append("	and empr = ? ");
		hql.append("	and fill.idFilial = ? ");
		hql.append(")  ");
		params.add(usuario);
		params.add(empresa);
		params.add(idFilial);
		return (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}

	public List<Filial> findFiliaisDiferentesEntreFluxosFiliais(FluxoFilial fluxoFilialOrigem, FluxoFilial fluxoFilialClone) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		hql.append(" select fili ");
		hql.append(" from OrdemFilialFluxo orff ");
		hql.append(" join orff.filial fili ");
		hql.append(" join orff.fluxoFilial flfi ");
		hql.append(" where flfi.id = ?  ");
		hql.append(" and not exists ( ");
		hql.append(" 				select 1 from OrdemFilialFluxo orff1 ");
		hql.append(" 				join orff1.fluxoFilial flfi1 ");
		hql.append(" 				where flfi1.id = ? ");
		hql.append(" 				and fili = orff1.filial ");
		hql.append(" 				) ");
		params.add(fluxoFilialOrigem.getIdFluxoFilial());
		params.add(fluxoFilialClone.getIdFluxoFilial());

		return getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
	}

	public List<Filial> findContatosFiliaisMercurio() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fili ");
		sb.append("FROM " + Filial.class.getName() + " fili ");
		sb.append("INNER JOIN FETCH fili.pessoa pess ");
		sb.append("LEFT JOIN FETCH pess.enderecoPessoa ende ");
		sb.append("LEFT JOIN FETCH ende.telefoneEnderecos tele ");
		sb.append("LEFT JOIN FETCH ende.tipoLogradouro tipo ");
		sb.append("LEFT JOIN FETCH ende.municipio muni ");
		sb.append("LEFT JOIN FETCH muni.unidadeFederativa unfe ");

		sb.append("WHERE fili.empresa.id = 361 ");
		sb.append("AND fili.id in ( ");
		sb.append("		SELECT hist.filial.id ");
		sb.append("		FROM " + HistoricoFilial.class.getName() + " hist ");
		sb.append("		WHERE hist.dtRealOperacaoInicial <= ? ");
		sb.append("		AND hist.dtRealOperacaoFinal >= ? ");
		sb.append(") ");
		sb.append("ORDER BY pess.nmFantasia ");

		List localizacao = getAdsmHibernateTemplate().find(sb.toString(),
				new Object[]{JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
		return localizacao;
	}

	public Map<String,Object> findGerentesFiliaisMercurio() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT new map( ");
		sb.append("fili.sgFilial as sgFilial, ");
		sb.append("(	select sbqr.nmContato ");
		sb.append("		from " + Contato.class.getName() + " as sbqr ");
		sb.append(" 	where sbqr.tpContato = 'GE' and  ");
		sb.append("		sbqr.pessoa.idPessoa = pess.idPessoa and ");
		sb.append("		rownum = 1 ");
		sb.append(") as nmGerente) ");
		sb.append("FROM " + Filial.class.getName() + " fili ");
		sb.append("JOIN fili.pessoa pess ");

		sb.append("WHERE fili.empresa.id = 361 ");
		sb.append("ORDER BY fili.sgFilial ");
		List<Map<String,Object>> localizacao = getAdsmHibernateTemplate().find(sb.toString());

		Map<String, Object> retorno = new HashMap<String, Object>();
		if(localizacao != null &&  localizacao.size() > 0) {
			for(Map<String, Object> item : localizacao) {
				retorno.put((String)item.get("sgFilial"), item.get("nmGerente"));
			}
		}

		return retorno;
	}

	public Filial findBySgFilialAndTpEmpresa(String sgFilial, String tpEmpresa) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fi ");
		hql.append("   FROM ").append(Filial.class.getName()).append(" fi");
		hql.append("   JOIN fi.empresa AS emp");
		hql.append("  WHERE fi.sgFilial = ?");
		hql.append("    AND emp.tpEmpresa = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(sgFilial);
		params.add(tpEmpresa);

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
}

	public Filial findBySgFilialAndIdEmpresa(String sgFilial, Long idEmpresa) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fi ");
		hql.append("   FROM ").append(Filial.class.getName()).append(" fi");
		hql.append("   JOIN fi.empresa AS emp");
		hql.append("  WHERE fi.sgFilial = ?");
		hql.append("    AND emp.idEmpresa = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(sgFilial);
		params.add(idEmpresa);

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}

	public static HibernateCallback findBySql(final String sql,final Object[] parametersValues,final ConfigureSqlQuery configQuery) {

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);

				configQuery.configQuery(query);

				if (parametersValues != null) {
					for (int i = 0; i < parametersValues.length; i++) {
						if( parametersValues[i] instanceof YearMonthDay){
							query.setParameter(i, parametersValues[i], Hibernate.custom(JodaTimeYearMonthDayUserType.class));
						}else{
							query.setParameter(i, parametersValues[i]);
						}
					}
				}
				return query.list();
			}
		};
		return hcb;
	}

	public Filial findFilialByCodFilial(Long idEmpresa, Integer codFilial) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fi ");
		hql.append("   FROM Filial fi");
		hql.append("  WHERE fi.codFilial = ?");
		hql.append("    AND fi.empresa.idEmpresa = ?");

		List<Filial> list = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{codFilial, idEmpresa});

		if (list == null || list.isEmpty()) {
			return null;
}
		return list.get(0);
	}

	public Filial findFilialByCliente(Long idCliente) {
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT f ");
		hql.append(" FROM Cliente c "
				+ " join c.filialByIdFilialAtendeComercial f "
				+ " join fetch f.pessoa p ");
		hql.append(" WHERE ");
		hql.append(" c.idCliente = ?");

		Filial filial = (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idCliente});

		return filial;
	}

	public Filial findFilialByUsuario(Long idUsuario) {
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT f ");
		hql.append(" FROM UsuarioLMS u "
				+ " join u.filial f ");
		hql.append(" WHERE ");
		hql.append(" u.idUsuario = ?");

		Filial filial = (Filial) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idUsuario});

		return filial;
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		sql.append(" f.id_filial as idfilial, ");
		sql.append(" f.sg_filial as sgfilial, ");
		sql.append(" p.nm_fantasia as nmFilial, ");
		sql.append(" e.id_empresa as idEmpresa, ");
		sql.append(" pe.nm_pessoa as nmEmpresa ");
		sql.append(" from filial f ");
		sql.append("      inner join pessoa p on p.id_pessoa = f.id_filial ");
		sql.append("      inner join empresa e on e.id_empresa = f.id_empresa ");
		sql.append("      INNER JOIN pessoa pe ON pe.id_pessoa = e.id_empresa ");
		sql.append(" where upper(f.sg_filial) = upper(:sgFilial) ");

		if (filter.get("idEmpresa") != null) {
			sql.append(" and f.id_empresa = :idEmpresa ");
		}
		return new ResponseSuggest(sql.toString(), filter);
			}

	public Filial findByNrIdentificacaoAndIdEmpresa(String nrIdentificacaoExp, Long idEmpresa) {
		StringBuilder hql = new StringBuilder();

		hql.append("select f ")
		   .append("from " + Filial.class.getSimpleName() + " f ")
		   .append(" join fetch f.pessoa p ")
		   .append(" where p.nrIdentificacao = ? ")
		   .append(" and f.empresa.id = ? ");

		return (Filial)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nrIdentificacaoExp, idEmpresa});
	}

	public Filial findFilialRespByNrIdentificacaoAndIdEmpresa(String nrIdentificacao, Long idEmpresa) {
		StringBuilder hql = new StringBuilder();

		hql.append("select f ")
		   .append("from " + Cliente.class.getSimpleName() + " c ")
		   .append(" join c.filialByIdFilialAtendeOperacional f ")
		   .append(" join c.pessoa p ")
		   .append(" where p.nrIdentificacao = ? ")
		   .append(" and f.empresa.id = ? ");

		return (Filial)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nrIdentificacao, idEmpresa});
	}

	public Object[] findLocalizacaoFilialOrigemAndDestino(Map<String, Object> criteria) {
		Long idDoctoServico = (Long)criteria.get("idDoctoServico");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		StringBuilder sql = new StringBuilder()
		.append("SELECT DOC.ID_DOCTO_SERVICO,")
			.append(" PO.ID_PESSOA AS ID_FILIAL_ORIGEM,")
			.append(" FIO.SG_FILIAL AS SG_FILIAL_ORIGEM, ")
			.append(" PO.NM_FANTASIA AS NM_FILIAL_ORIGEM,")
			.append(" EPO.NR_LATITUDE_TMP AS LATITUDE_ORIGEM,")
			.append(" EPO.NR_LONGITUDE_TMP AS LONGITUDE_ORIGEM,")
			.append(" PD.ID_PESSOA AS ID_FILIAL_DESTINO,")
			.append(" FID.SG_FILIAL AS SG_FILIAL_DESTINO, ")
			.append(" PD.NM_FANTASIA AS NM_FILIAL_DESTINO,")
			.append(" EPD.NR_LATITUDE_TMP AS LATITUDE_DESTINO,")
			.append(" EPD.NR_LONGITUDE_TMP AS LONGITUDE_DESTINO,")
			.append(" SVC.TP_MODAL AS TP_MODAL")
		.append(" FROM DOCTO_SERVICO DOC,")
			.append(" PESSOA PO,")
			.append(" FILIAL FIO,")
			.append(" PESSOA PD,")
			.append(" FILIAL FID,")
			.append(" ENDERECO_PESSOA EPO,")
			.append(" ENDERECO_PESSOA EPD,")
			.append(" SERVICO SVC")
		.append(" WHERE ")
			.append(" PO.ID_PESSOA = DOC.ID_FILIAL_ORIGEM")
			.append(" AND EPO.ID_ENDERECO_PESSOA = PO.ID_ENDERECO_PESSOA")
			.append(" AND PD.ID_PESSOA = DOC.ID_FILIAL_DESTINO")
			.append(" AND PO.ID_PESSOA = FIO.ID_FILIAL")
			.append(" AND PD.ID_PESSOA = FID.ID_FILIAL")
			.append(" AND SVC.ID_SERVICO = DOC.ID_SERVICO")
			.append(" AND EPD.ID_ENDERECO_PESSOA = PD.ID_ENDERECO_PESSOA")
			.append(" AND DOC.ID_DOCTO_SERVICO = :idDoctoServico");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("NM_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("LONGITUDE_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("NM_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("LONGITUDE_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("TP_MODAL", Hibernate.STRING);
			}
		};

		Object[] localizacoesFiliais = null;
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
		if (result != null && !result.isEmpty()){
			localizacoesFiliais = result.get(0);
		}

		return localizacoesFiliais;
	}

	public Object[] findLocalizacaoFilialAtual(Map<String, Object> criteria) {
		Long idDoctoServico = (Long)criteria.get("idDoctoServico");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		StringBuilder sql = new StringBuilder()
			.append("SELECT * FROM")
			.append(" (SELECT DC.ID_DOCTO_SERVICO,")
			.append(" PA.ID_PESSOA AS ID_FILIAL,")
			.append(" FA.SG_FILIAL,")
			.append(" PA.NM_FANTASIA,")
			.append(" CASE")
			.append(" WHEN LM.DS_LOCALIZACAO_MERCADORIA_I IS NOT NULL THEN (LM.DS_LOCALIZACAO_MERCADORIA_I)")
			.append(" ELSE DE.DS_DESCRICAO_EVENTO_I")
			.append(" END AS LOCALIZACAO_MERCADORIA,")
			.append(" EPA.NR_LATITUDE_TMP AS LATITUDE_ATUAL,")
			.append(" EPA.NR_LONGITUDE_TMP AS LONGITUDE_ATUAL,")
			.append(" EPD.NR_LATITUDE_TMP AS LATITUDE_DESTINO,")
			.append(" EPD.NR_LONGITUDE_TMP AS LONGITUDE_DESTINO,")
			.append(" SVC.TP_MODAL AS TP_MODAL,")
			.append(" ONC.ID_MOTIVO_ABERTURA_NC")
			.append(" FROM EVENTO_DOCUMENTO_SERVICO EDS,")
			.append(" EVENTO EV,")
			.append(" DESCRICAO_EVENTO DE,")
			.append(" LOCALIZACAO_MERCADORIA LM,")
			.append(" DOCTO_SERVICO DC,")
			.append(" FILIAL FA,")
			.append(" FILIAL FD,")
			.append(" PESSOA PA,")
			.append(" PESSOA PD,")
			.append(" SERVICO SVC,")
			.append(" ENDERECO_PESSOA EPA,")
			.append(" ENDERECO_PESSOA EPD,")
			.append(" NAO_CONFORMIDADE NC,")
			.append(" OCORRENCIA_NAO_CONFORMIDADE ONC")
			.append(" WHERE EDS.ID_DOCTO_SERVICO = :idDoctoServico ")
			.append(" AND EDS.ID_DOCTO_SERVICO = DC.ID_DOCTO_SERVICO")
			.append(" AND DC.ID_FILIAL_lOCALIZACAO = FA.ID_FILIAL")
			.append(" AND DC.ID_FILIAL_DESTINO = FD.ID_FILIAL")
			.append(" AND FA.ID_FILIAL = PA.ID_PESSOA")
			.append(" AND FD.ID_FILIAL = PD.ID_PESSOA")
			.append(" AND EDS.ID_EVENTO = EV.ID_EVENTO")
			.append(" AND SVC.ID_SERVICO = DC.ID_SERVICO")
			.append(" AND PA.ID_ENDERECO_PESSOA = EPA.ID_ENDERECO_PESSOA")
			.append(" AND PD.ID_ENDERECO_PESSOA = EPD.ID_ENDERECO_PESSOA")
			.append(" AND NC.ID_DOCTO_SERVICO (+)= DC.ID_DOCTO_SERVICO")
			.append(" AND ONC.ID_NAO_CONFORMIDADE (+)= NC.ID_NAO_CONFORMIDADE")
			.append(" AND EV.ID_DESCRICAO_EVENTO = DE.ID_DESCRICAO_EVENTO")
			.append(" AND LM.ID_LOCALIZACAO_MERCADORIA (+) = EV.ID_LOCALIZACAO_MERCADORIA ")
			.append(" AND EDS.BL_EVENTO_CANCELADO = 'N' ")
			.append(" AND EV.BL_EXIBE_CLIENTE = 'S'");

		sql.append(" ORDER BY EDS.DH_EVENTO DESC, EDS.ID_EVENTO_DOCUMENTO_SERVICO DESC)");
		sql.append(" WHERE ROWNUM = 1");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NM_FANTASIA", Hibernate.STRING);
				sqlQuery.addScalar("LOCALIZACAO_MERCADORIA", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_ATUAL", Hibernate.STRING);
				sqlQuery.addScalar("LONGITUDE_ATUAL", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("LONGITUDE_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("TP_MODAL", Hibernate.STRING);
				sqlQuery.addScalar("ID_MOTIVO_ABERTURA_NC", Hibernate.LONG);
			}
		};
		Object[] localizacaoFilial = null;
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
		if (result != null && !result.isEmpty()){
			localizacaoFilial = result.get(0);
		}

		return localizacaoFilial;
	}


	/**
	 * Responsavel por retornar dados da filial de atendimento
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FilialAtendimentoDto findFilialAtendimento(Long idFilial) {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT F.SG_FILIAL AS \"sgFilial\" ,                       		");
		sql.append(" P.NM_FANTASIA AS \"nmFantasia\" ,                                  ");
		sql.append(" EP.DS_ENDERECO \"dsEndereco\",                                     ");
		sql.append(" EP.NR_ENDERECO AS \"nrEndereco\" ,                                 ");
		sql.append(" EP.DS_BAIRRO  AS \"dsBairro\" ,                                    ");
		sql.append(" M.NM_MUNICIPIO AS \"nmMunicipio\" ,                                ");
		sql.append(" UF.SG_UNIDADE_FEDERATIVA AS \"nmUnidadeFederativa\" ,              ");
		sql.append(" EP.NR_CEP AS \"nrCep\" ,                                           ");
		sql.append(" Max(TE.NR_DDD) AS \"nrDdd\" ,                                      ");
		sql.append(" Max(TE.NR_TELEFONE) AS \"nrTelefone\" ,                            ");
		sql.append(" P.DS_EMAIL AS \"dsEmail\"                                          ");
		sql.append(" FROM FILIAL F,                                                     ");
		sql.append(" PESSOA P,                                                          ");
		sql.append(" ENDERECO_PESSOA EP,                                                ");
		sql.append(" MUNICIPIO M,                                                       ");
		sql.append(" UNIDADE_FEDERATIVA UF,                                             ");
		sql.append(" TELEFONE_ENDERECO TE                                               ");
		sql.append(" WHERE F.ID_FILIAL = :idFilial                                      ");
		sql.append(" AND P.ID_PESSOA = F.ID_FILIAL                                      ");
		sql.append(" AND EP.ID_ENDERECO_PESSOA = P.ID_ENDERECO_PESSOA                   ");
		sql.append(" AND M.ID_MUNICIPIO = EP.ID_MUNICIPIO                               ");
		sql.append(" AND UF.ID_UNIDADE_FEDERATIVA = M.ID_UNIDADE_FEDERATIVA             ");
		sql.append(" AND TE.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA                  ");
		sql.append(" GROUP BY F.SG_FILIAL, P.NM_FANTASIA, EP.DS_ENDERECO,               ");
		sql.append(" EP.NR_ENDERECO, EP.DS_BAIRRO, M.NM_MUNICIPIO,                      ");
		sql.append(" UF.SG_UNIDADE_FEDERATIVA, EP.NR_CEP, P.DS_EMAIL                    ");


		Query query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(FilialAtendimentoDto.class));

		query.setParameter("idFilial", idFilial);

		List<FilialAtendimentoDto> lst = query.list();

		return (lst != null && lst.size() > 0? lst.get(0):null);
	}


	public List<Map<String,Object>> findFiliaisByUsuario(Long idUsuario) {
		StringBuilder sb = new StringBuilder();

		sb.append(" select f.sg_filial as sg_filial, f.id_filial as id_filial, fp.SG_FILIAL as filial_padrao, fp.id_filial as id_filial_padrao, eu.BL_IRRESTRITO_FILIAL as irrestrito");
		sb.append(" from empresa_usuario eu, filial_usuario fu, filial f, filial fp ");
		sb.append(" where eu.id_usuario = :idUsuario ");
		sb.append(" and fu.ID_EMPRESA_USUARIO = eu.ID_EMPRESA_USUARIO ");
		sb.append(" and f.id_filial = fu.id_filial ");
		sb.append(" and fp.id_filial = eu.ID_FILIAL_PADRAO ");

		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idUsuario", idUsuario);

		return getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(),
				namedParams, getSqlForFiliaisUsuario());
}

	private ConfigureSqlQuery getSqlForFiliaisUsuario() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("id_filial", Hibernate.LONG);
				sqlQuery.addScalar("filial_padrao", Hibernate.STRING);
				sqlQuery.addScalar("id_filial_padrao", Hibernate.LONG);
				sqlQuery.addScalar("irrestrito", Hibernate.STRING);
			}
		};
		return csq;
	}

	public Map<Long, String> findSiglaComDoisDigitos() {

		StringBuilder sb = new StringBuilder();
		sb.append("select DS_VALOR_LMS as KEY, ");
		sb.append("		  DS_VALOR_CORPORATIVO as VALUE ");
		sb.append("from dominio_vinculo_integracao ");
		sb.append("where ID_DOMINIO_NOME_INTEGRACAO in ");
		sb.append("		(select ID_DOMINIO_NOME_INTEGRACAO ");
		sb.append("		 from DOMINIO_NOME_INTEGRACAO ");
		sb.append("		 where NM_DOMINIO = 'FILIAL_SIGLA') ");

		List<Map<String,Object>> list = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(),
				new HashMap<String, Object>(),
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(org.hibernate.SQLQuery sqlQuery) {
						sqlQuery.addScalar("KEY", Hibernate.LONG);
						sqlQuery.addScalar("VALUE", Hibernate.STRING);
}
				});


		Map<Long,String> result = new TreeMap<Long, String>();
		for (Map<String, Object> map : list) {
			result.put((Long)map.get("KEY"), (String)map.get("VALUE"));
		}
		return result;
	}

	public List<Filial> findFiliaisByCodsFilial(List<Integer> codsFilial) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fi ");
		hql.append("   FROM Filial fi");
		hql.append("  WHERE fi.codFilial in (:codsFilial)");

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "codsFilial", codsFilial);
	}

    public Filial findFilialByCdFilialFedex(String cdFilialFedex) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "f")
                .createAlias("pessoa", "p")
                .createAlias("empresa", "e")
                .add(Restrictions.eq("f.cdFilialFedex", cdFilialFedex));

        return (Filial)dc.getExecutableCriteria(getSession()).uniqueResult();
    }


    public List<Filial> findFiliaisByCdFilialFedex(String cdFilialFedex) {

        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT f_parceira ");
        hql.append("   FROM ConteudoParametroFilial cpf, Filial f_parceira");
        hql.append("  WHERE ");
        hql.append("    cpf.parametroFilial.nmParametroFilial = :nmParametroFilial ");
        hql.append("    and cpf.vlConteudoParametroFilial = :vlConteudoParametroFilial ");
        hql.append("    and cpf.filial.idFilial = f_parceira.idFilial ");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[]{"nmParametroFilial", "vlConteudoParametroFilial"},
                new String[]{"CD_FILIAL_FEDEX", cdFilialFedex});

    }

	public Integer getRowCountFilialByIdPessoa(Long idPessoa) {
		StringBuilder hql = new StringBuilder();

		hql
		.append("FROM ").append(Filial.class.getSimpleName()).append(" f ")
		.append("WHERE ")
		.append("f.idFilial =:idPessoa ")
		;

		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idPessoa", idPessoa);

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), parametersValues);
	}




    public List<Integer> findCodFiliaisIntegraNotfis() {

        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT f.codFilial ");
        hql.append("   FROM ConteudoParametroFilial cpf, Filial f");
        hql.append("  WHERE ");
        hql.append("    cpf.parametroFilial.nmParametroFilial = :nmParametroFilial ");
        hql.append("    and cpf.vlConteudoParametroFilial = :vlConteudoParametroFilial ");
        hql.append("    and cpf.filial.idFilial = f.idFilial ");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[]{"nmParametroFilial", "vlConteudoParametroFilial"},
                new String[]{"INTEGRA_NOTFIS_FAAV", "S"});

    }

	public String findSgFilialByIdDoctoServico(Long idDoctoServico) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT f.sgFilial ");
		sql.append(" FROM " + Filial.class.getSimpleName() + " f, " + DoctoServico.class.getSimpleName() + " ds ");
		sql.append(" where f.id = ds.filialByIdFilialOrigem.id ");
		sql.append(" and ds.idDoctoServico = ? ");

		return (String) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idDoctoServico});

	}

}
