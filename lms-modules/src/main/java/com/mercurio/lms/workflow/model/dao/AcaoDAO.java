package com.mercurio.lms.workflow.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.core.model.hibernate.VarcharI18nFunction;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.SubstitutoFaltaAcao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class AcaoDAO extends BaseCrudDao<Acao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Acao.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("usuario", FetchMode.JOIN);
    	lazyFindById.put("pendencia", FetchMode.JOIN);
    	lazyFindById.put("pendencia.ocorrencia", FetchMode.JOIN);
    	lazyFindById.put("pendencia.ocorrencia.filial", FetchMode.JOIN);
    	lazyFindById.put("pendencia.ocorrencia.usuario", FetchMode.JOIN);
    	lazyFindById.put("pendencia.ocorrencia.eventoWorkflow", FetchMode.JOIN);
    	lazyFindById.put("pendencia.ocorrencia.eventoWorkflow.tipoEvento", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }

    /**
     * Paginação para visualização do fluxo da pendência.
     * @param criteria
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedPendencia(TypedFlatMap criteria, FindDefinition findDef) {

    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection(" a ");
    	sql.addFrom(getPersistentClass().getName(), " a join a.pendencia pen join fetch a.integrante integrante " +
    			" left join fetch integrante.usuario usuario left join fetch integrante.perfil perfil ");

    	String idPendencia = criteria.getString("pendencia.idPendencia");
    	if (StringUtils.isNotBlank(idPendencia)){
    		sql.addCriteria("pen.id","=",Long.valueOf(idPendencia));
    	}

    	sql.addOrderBy("a.nrOrdemAprovacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		return rsp;
    }

    /**
     * Contagem de registros para listagem das Pendencias
     * @param criteria
     * @return
     */
    public Integer getRowCountPendencia(TypedFlatMap criteria) {

    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection(" count(*) ");
    	sql.addFrom(getPersistentClass().getName(), " a join a.pendencia pen join a.integrante integrante " +
    			" left join integrante.usuario usuario left join integrante.perfil perfil ");

    	String idPendencia = criteria.getString("pendencia.idPendencia");
    	if (StringUtils.isNotBlank(idPendencia)){
    		sql.addCriteria("pen.id","=",Long.valueOf(idPendencia));
    	}

    	sql.addOrderBy("a.nrOrdemAprovacao");

    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
   		return result.intValue();
    }

	public Integer getRowCountAcoesUsuarioLogado(
			String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia) {

		StringBuffer sql = new StringBuffer();
		Map map = this.getMapFiltroSqlAcoes(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia);

		sql.append("select count(*) total from (");
		sql.append(getSqlAcoes(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(")");

		ConfigureSqlQuery configSql = sqlQuery -> sqlQuery.addScalar("total", Hibernate.INTEGER);
		//TODO verificar
		return (Integer)getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(1), map, configSql).getList().get(0);

	}

    /**
	 * @param idEvento: Evento do workflow
	 * @param dhLiberacaoInicial e dhLiberacaoFinal
	 * 		  Data de inclusão da ação
	 * @return List com as seguintes ações:
	 * 		- pendentes para o usuário logado
	 * 		- pendentes para usuários onde o usuário logado seja subsitituto por falta
	 * 		- pendentes para usuários onde o usuário logado seja substituto
	 *
	 * pendentes = blLiberada = 'S' e dhLiberacao menor ou igual data atual
	 *
	 * order: dhLiberacao e dsTipoEvento
	 *
	 */
	public ResultSetPage findAcoesUsuarioLogado(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia,
			int currentPage,
			int pageSize) {

		StringBuffer sql = new StringBuffer();
		Map map = this.getMapFiltroSqlAcoes(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia);

		sql.append(getSqlAcoes(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" order by	dhLiberacao desc");

		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idAcao", Hibernate.LONG);
				sqlQuery.addScalar("dhLiberacao", Hibernate.TIMESTAMP);
				sqlQuery.addScalar("idTipoEvento", Hibernate.LONG);
				sqlQuery.addScalar("pendencia", Hibernate.STRING);
				sqlQuery.addScalar("idProcesso", Hibernate.LONG);
				sqlQuery.addScalar("dsTipoEvento", Hibernate.STRING);
				sqlQuery.addScalar("nmClasseVisualizacao", Hibernate.STRING);
				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("nrTipoEvento", Hibernate.STRING);
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), currentPage, pageSize, map, configSql);

	}

	/**
	 * Monta sql que contem todas as ações que o usuário logado deve visualizar
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @param findDef
	 * @return
	 */
	private String getSqlAcoes(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia) {

		StringBuffer sql = new StringBuffer();

		sql.append(getSqlUsuarioIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlPerfilUsuarioIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoUsuarioIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoPerfilIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoUsuarioPerfilIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoFaltaUsuarioIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoFaltaPerfilUsuarioIntegrante(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));


		return sql.toString();
	}

	/**
	 * Monta o map de filtros
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return Map
	 * */
	private Map getMapFiltroSqlAcoes(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		Map map = new HashMap();
		map.put("idUsuario", SessionUtils.getUsuarioLogado().getIdUsuario());
		map.put("dsTipoEvento",dsTipoEvento);
		map.put("dsPendencia",dsPendencia);

		DateTime dhLiberacaoIni = null;
		if (dhLiberacaoInicial != null){
			dhLiberacaoIni = JTDateTimeUtils.yearMonthDayToDateTime(dhLiberacaoInicial);
		}

		DateTime dhLiberacaoFim = null;
		if (dhLiberacaoFinal != null){
			dhLiberacaoFinal = dhLiberacaoFinal.plusDays(1);
			dhLiberacaoFim = JTDateTimeUtils.yearMonthDayToDateTime(dhLiberacaoFinal);
		}

		map.put("idProcesso", idProcesso);
		map.put("idSolicitante", idSolicitante);
		map.put("dhLiberacaoInicial", dhLiberacaoIni);
		map.put("dhLiberacaoFinal", dhLiberacaoFim);
		map.put("dhAtual",JTDateTimeUtils.getDataHoraAtual());
		map.put("dtAtual",JTDateTimeUtils.getDataAtual());
		return map;
	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * está configurado como usuário integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlUsuarioIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(getBasicJoins());
		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));

		//-----------------------------------------------------------------------
		sql.append(" and		INT.ID_USUARIO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(" and		INT.ID_USUARIO = :idUsuario \n");

		return sql.toString();

	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * faz parte do perfil que está configurado
	 * como perfil integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlPerfilUsuarioIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		PERFIL_USUARIO PU \n");

		sql.append(getBasicJoins());
		sql.append(" and		PU.ID_PERFIL = INT.ID_PERFIL \n");

		//-----------------------------------------------------------------------
		sql.append(" and		PU.ID_USUARIO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" and		PU.ID_USUARIO = :idUsuario \n");
		return sql.toString();

	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * é substituto do usuario que está configurado
	 * como integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlSubstitutoUsuarioIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");

		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUIDO = INT.ID_USUARIO \n");

		//-----------------------------------------------------------------------
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		addRestricaoDePermissaoParaSubstituido(sql);

		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		:dtAtual BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL \n");

		return sql.toString();
	}

	private void addRestricaoDePermissaoParaSubstituido(StringBuffer sql) {
		// regra para exibir somente as pendencias que o usuário substituido tem acesso e não todas as pendencias que o usuario
		// logado poderia ver devido ao seu perfil
		sql.append("and      EXISTS (SELECT * \n" +
                "FROM FILIAL_USUARIO FILUSU, EMPRESA_USUARIO EMPUSU, \n" +
                "	  REGIONAL_USUARIO REGUSU, REGIONAL_FILIAL  REGFIL \n" +
                "WHERE  FILUSU.ID_EMPRESA_USUARIO(+) = EMPUSU.ID_EMPRESA_USUARIO \n" +
				        "AND    REGUSU.ID_EMPRESA_USUARIO(+) = EMPUSU.ID_EMPRESA_USUARIO \n" +
				        "AND    REGFIL.ID_REGIONAL(+) = REGUSU.ID_REGIONAL \n" +
				        "AND    SUB.ID_USUARIO_SUBSTITUIDO = EMPUSU.ID_USUARIO \n" +
				        "AND    OCO.ID_FILIAL = FILUSU.ID_FILIAL(+) \n" +
				        "AND    OCO.ID_FILIAL = REGFIL.ID_FILIAL(+) \n" +
				        "AND    (FILUSU.BL_APROVA_WORKFLOW = 'S' \n" +
				        "OR     EMPUSU.BL_IRRESTRITO_FILIAL = 'S' \n" +
				        "OR     (REGUSU.BL_APROVA_WORKFLOW = 'S' \n" +
				        "		AND    REGFIL.DT_VIGENCIA_INICIAL <= SYSDATE AND REGFIL.DT_VIGENCIA_FINAL >= SYSDATE)) \n" +
				")");
	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * é substituto do perfil que está configurado
	 * como integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlSubstitutoPerfilIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){
		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");

		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_PERFIL_SUBSTITUIDO = INT.ID_PERFIL \n");

		//-----------------------------------------------------------------------
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		:dtAtual BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL \n");
		return sql.toString();
	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * é substituto de um usuario que pertence ao perfil que
	 * está configurado como integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlSubstitutoUsuarioPerfilIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");
		sql.append(", 		PERFIL_USUARIO PERUSU \n");

		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUIDO = PERUSU.ID_USUARIO \n");
		sql.append(" and		PERUSU.ID_PERFIL = INT.ID_PERFIL \n");

		//-----------------------------------------------------------------------
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		addRestricaoDePermissaoParaSubstituido(sql);
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		:dtAtual BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL \n");

		return sql.toString();

	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * é substituto em caso de falta do integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlSubstitutoFaltaUsuarioIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO_FALTA SF \n");
		sql.append(", 		SUBSTITUTO_FALTA_ACAO SFA \n");

		sql.append(getBasicJoins());
		sql.append(" and		SF.ID_INTEGRANTE = INT.ID_INTEGRANTE \n");
		sql.append(" and		SF.ID_SUBSTITUTO_FALTA = SFA.ID_SUBSTITUTO_FALTA \n");
		sql.append(" and		A.ID_ACAO = SFA.ID_ACAO \n");

		//-----------------------------------------------------------------------
		sql.append(" and		SF.ID_USUARIO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" and		SF.ID_USUARIO = :idUsuario ");
		sql.append("\n and		SF.TP_SITUACAO = 'A' \n");

		return sql.toString();

	}

	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * pertence ao perfil que é substituto em caso de falta do integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlSubstitutoFaltaPerfilUsuarioIntegrante(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sql = new StringBuffer();

		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO_FALTA SF \n");
		sql.append(", 		SUBSTITUTO_FALTA_ACAO SFA \n");
		sql.append(", 		PERFIL_USUARIO PU \n");

		sql.append(getBasicJoins());
		sql.append(" and		SF.ID_INTEGRANTE = INT.ID_INTEGRANTE \n");
		sql.append(" and		PU.ID_PERFIL = SF.ID_PERFIL \n");
		sql.append(" and		SF.ID_SUBSTITUTO_FALTA = SFA.ID_SUBSTITUTO_FALTA \n");
		sql.append(" and		A.ID_ACAO = SFA.ID_ACAO \n");

		//-----------------------------------------------------------------------
		sql.append(" and		PU.ID_USUARIO = US.ID_USUARIO \n");
		//-----------------------------------------------------------------------

		sql.append(getBasicWhere(dsTipoEvento, dhLiberacaoInicial, dhLiberacaoFinal, idSolicitante, idProcesso, dsPendencia));
		sql.append(" and		PU.ID_USUARIO = :idUsuario ");
		sql.append("\n and		SF.TP_SITUACAO = 'A' \n");

		return sql.toString();
	}

	/**
	 * Retorna a projeção dos SQL do UNION
	 * @return
	 */
	private String getProjection(){

		StringBuffer sProjection = new StringBuffer();

		sProjection.append("\n select		A.ID_ACAO idAcao, \n");
		sProjection.append("		A.DH_LIBERACAO dhLiberacao, \n");
		sProjection.append("		TE.ID_TIPO_EVENTO idTipoEvento, \n");
		sProjection.append("		P.DS_PENDENCIA pendencia, \n");
		sProjection.append("		P.ID_PENDENCIA idPendencia, \n");
		sProjection.append("		P.ID_PROCESSO idProcesso, \n");
		sProjection.append("		F.SG_FILIAL sgFilial, \n");
		sProjection.append("		TE.NR_TIPO_EVENTO nrTipoEvento, \n");
		sProjection.append("		"+ PropertyVarcharI18nProjection.createProjection("TE.DS_TIPO_EVENTO_I", "dsTipoEvento")+", \n");
		sProjection.append("		EW.NM_CLASSE_VISUALIZACAO nmClasseVisualizacao \n");

		return sProjection.toString();

	}

	/**
	 * Retorna as tabelas que serão acessadas por todos os sqls
	 * @return
	 */
	private String getBasicFrom(){
		StringBuffer sFrom = new StringBuffer();

		sFrom.append("from	ACAO A, \n");
		sFrom.append("		PENDENCIA P, \n");
		sFrom.append("		OCORRENCIA OCO, \n");
		sFrom.append("		EVENTO_WORKFLOW EW, \n");
		sFrom.append("		TIPO_EVENTO TE, \n");
		sFrom.append("		COMITE COM, \n");
		sFrom.append("		INTEGRANTE INT, \n");
		sFrom.append(" 		USUARIO US, \n");
		sFrom.append(" 		FILIAL F, \n");
		sFrom.append(" 		RECIBO_INDENIZACAO REC, \n");
		sFrom.append(" 		PESSOA PES \n");


		return sFrom.toString();

	}

	/**
	 * Retorna os joins que serão acessadas por todos os sqls
	 * @return
	 */
	private String getBasicJoins(){

		StringBuffer sJoins = new StringBuffer();

		sJoins.append("where		P.ID_PENDENCIA = A.ID_PENDENCIA \n");
		sJoins.append("and		OCO.ID_OCORRENCIA = P.ID_OCORRENCIA \n");
		sJoins.append("and		EW.ID_EVENTO_WORKFLOW = OCO.ID_EVENTO_WORKFLOW \n");
		sJoins.append("and		TE.ID_TIPO_EVENTO = EW.ID_EVENTO_WORKFLOW \n");
		sJoins.append("and		COM.ID_COMITE = EW.ID_COMITE \n");
		sJoins.append("and		INT.ID_COMITE = COM.ID_COMITE \n");
		sJoins.append("and		INT.ID_INTEGRANTE = A.ID_INTEGRANTE \n");
		sJoins.append("and		US.ID_USUARIO = US.ID_USUARIO \n");
		sJoins.append("and		OCO.ID_FILIAL = F.ID_FILIAL(+) \n");
		sJoins.append("and		P.ID_PENDENCIA = REC.ID_PENDENCIA(+) \n");
		sJoins.append("and		REC.ID_FAVORECIDO = PES.ID_PESSOA(+) \n");

		return sJoins.toString();

	}

	/**
	 * Retorna os criterios que serão acessadas por todos os sqls
	 * @return
	 */
	private String getBasicWhere(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){

		StringBuffer sWhere = new StringBuffer();

		sWhere.append("and		P.TP_SITUACAO_PENDENCIA = 'E' \n");
		sWhere.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
		sWhere.append("and		A.BL_LIBERADA = 'S' \n");
		sWhere.append("and		A.DH_LIBERACAO <= :dhAtual \n");

		sWhere.append("and      (EXISTS (SELECT * \n" +
                "FROM FILIAL_USUARIO FILUSU, EMPRESA_USUARIO EMPUSU, \n" +
                "	  REGIONAL_USUARIO REGUSU, REGIONAL_FILIAL  REGFIL \n" +
                "WHERE  FILUSU.ID_EMPRESA_USUARIO(+) = EMPUSU.ID_EMPRESA_USUARIO \n" +
				        "AND    REGUSU.ID_EMPRESA_USUARIO(+) = EMPUSU.ID_EMPRESA_USUARIO \n" +
				        "AND    REGFIL.ID_REGIONAL(+) = REGUSU.ID_REGIONAL \n" +
				        "AND    US.ID_USUARIO = EMPUSU.ID_USUARIO \n" +
				        "AND    OCO.ID_FILIAL = FILUSU.ID_FILIAL(+) \n" +
				        "AND    OCO.ID_FILIAL = REGFIL.ID_FILIAL(+) \n" +
				        "AND    (FILUSU.BL_APROVA_WORKFLOW = 'S' \n" +
				        "OR     EMPUSU.BL_IRRESTRITO_FILIAL = 'S' \n" +
				        "OR     (REGUSU.BL_APROVA_WORKFLOW = 'S' \n" +
				        "		AND    REGFIL.DT_VIGENCIA_INICIAL <= SYSDATE AND REGFIL.DT_VIGENCIA_FINAL >= SYSDATE)) \n" +
				")");
		sWhere.append("		OR ");
		sWhere.append("			EXISTS (SELECT 1 \n" +
									"FROM comite com, 			 					\n" +
									" 	integrante INT								\n" +
									"WHERE pes.nr_identificacao = substr(com.nm_comite_i, instr(com.nm_comite_i, 'pt_BR»') + length('pt_BR»'), 14) \n" +
									"	AND com.id_comite = INT.id_comite 			\n" +
									"	AND ( 										\n" +
									"	INT.id_usuario = us.id_usuario 				\n" +
									"	OR INT.id_perfil IN (						\n" +
									" 		SELECT per.id_perfil 					\n" +
									" 		FROM perfil_usuario per					\n" +
									" 		WHERE per.id_usuario = us.id_usuario	\n" +
									"	)\n" +
									"	)											\n" +
								")													\n" +
					")");

		if (StringUtils.isNotBlank(dsTipoEvento)){
			sWhere.append("and		lower("+VarcharI18nFunction.extractValueI18n("TE.DS_TIPO_EVENTO_I", LocaleContextHolder.getLocale())+") like '" + dsTipoEvento.toLowerCase() + "%' \n");
		}
		if (StringUtils.isNotBlank(dsPendencia)){
			sWhere.append("and		lower(P.DS_PENDENCIA) like '" + dsPendencia.toLowerCase() + "' \n");
		}
		if (dhLiberacaoInicial != null) {
			sWhere.append("and		cast(A.DH_LIBERACAO as date) >= :dhLiberacaoInicial \n");
		}
		if (dhLiberacaoFinal != null) {
			sWhere.append("and		cast(A.DH_LIBERACAO as date) <= :dhLiberacaoFinal \n");
		}
		if (idSolicitante != null) {
			sWhere.append("and		OCO.ID_USUARIO = :idSolicitante \n");
		}
		if (idProcesso != null) {
			sWhere.append("and		P.ID_PROCESSO = :idProcesso \n");
		}

		return sWhere.toString();

	}

	/**
	 * Retorna uma lista dados de ação que tem que ser liberada
	 *
	 * @return List
	 * */
	public List findAcoesNaoLiberadas() {
		SqlTemplate sql = this.mountSqlFindAcoes();
		sql.addProjection("a");
    	sql.addCriteria("a.dhLiberacao.value", "<=", JTDateTimeUtils.getDataHoraAtual());
    	sql.addCriteria("a.blLiberada", "=", Boolean.FALSE);
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna uma lista dados de ação que venceram e que tem que tomar uma attitude
	 *
	 * @return List
	 * */
	public List findAcoesPendente() {
		SqlTemplate sql = this.mountSqlFindAcoes();
    	sql.addProjection("new Map(a as acao, ew.tpAcaoAutomatica as tpAcaoAutomatica, i.idIntegrante as idIntegrante, te.nrTipoEvento as nrTipoEvento, o.filial.id as idFilial, us.dsEmail as dsEmail, pe.dsPendencia as dsPendencia, o.dhInclusao as dhInclusao)");
    	sql.addCustomCriteria("a.dhLiberacao.value <= ( ? - ((to_char(ew.hrAcaoAutomatica,'hh24')/24) + (to_char(ew.hrAcaoAutomatica,'mi')/1440)))");
    	sql.addCriteriaValue(JTDateTimeUtils.getDataHoraAtual());
    	sql.addCriteria("a.blLiberada", "=", Boolean.TRUE);
    	sql.addCriteria("a.tpSituacaoAcao","=","E");
    	sql.addCriteria("(to_char(ew.hrAcaoAutomatica,'hh24')/24) + (to_char(ew.hrAcaoAutomatica,'mi')/1440)","<>", Integer.valueOf(0));
    	sql.addCustomCriteria("not exists(select su from "+SubstitutoFaltaAcao.class.getName()+" su where su.acao.tpSituacaoAcao = 'A')");
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna o sql basico para buscar ações pendentes
	 *
	 * @return SqlTemplate
	 * */
	private SqlTemplate mountSqlFindAcoes(){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(getPersistentClass().getName(), " a " +
    			"join a.integrante as int " +
    			"join a.pendencia as pe " +
    			"join pe.ocorrencia as o " +
    			"join o.eventoWorkflow as ew " +
    			"join o.usuario as us " +
    			"join ew.tipoEvento as te " +
    			"join ew.comite as co " +
    			"join co.integrantes as i ");

    	sql.addJoin("int.idIntegrante","i.idIntegrante");
    	sql.addCustomCriteria("a.dhAcao.value is null");
    	sql.addCriteria("a.tpSituacaoAcao","<>","C");
    	return sql;
	}

	/**
	 * @param acao: Retorna a próxima ação de acordo
	 * 				com a ordem (acao.nr_ordem)
	 */
	public Acao findProximaAcao(Long idPendencia, Byte nrOrdemAtual) {
		Validate.notNull(idPendencia);
		Validate.notNull(nrOrdemAtual);
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("a");

		sql.addFrom(Acao.class.getName(), "a " +
				"join a.pendencia as pe " +
				"join fetch a.integrante as i " +
				"left outer join fetch i.usuario as u " +
				"left outer join fetch i.perfil as p ");
		sql.addCriteria("pe.idPendencia","=",idPendencia);
		sql.addCriteria("a.nrOrdemAprovacao",">",nrOrdemAtual);
		sql.addOrderBy("a.nrOrdemAprovacao asc");
        List list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
        if (!list.isEmpty()){
        	return (Acao)list.get(0);
        } else {
        	return null;
        }
	}
	public Long findLastNrNotaCredito(Long idPendencia) {
			return null;
		}

	public Long findLastNr(Long idProcesso) {

			return null;
		}

	public static HibernateCallback findBySql(final String sql,final Object[] parametersValues,final ConfigureSqlQuery configQuery) {

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);

				// chama a impl que configura a SQLQuery.
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

	/**
	 * Retorna as ações pendentes de uma pendencia
	 *
	 * @param Long idPendencia
	 * @return List
	 */
	public List findByPendencia(Long idPendencia) {
		Validate.notNull(idPendencia);
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("a");

		sql.addFrom(Acao.class.getName(), "a");
		sql.addCriteria("a.pendencia.id","=",idPendencia);
		sql.addCriteria("a.tpSituacaoAcao","=","E");
		sql.addOrderBy("a.nrOrdemAprovacao asc");
        return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Retorna a observação da última ação da pendencia informada.
	 *
	 * @param Long idPendencia
	 * @return String
	 */
	public List findUltimaAcaoByPendencia(Long idPendencia) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("a");

		sql.addFrom(Acao.class.getName(), "a");
		sql.addCriteria("a.pendencia.id","=",idPendencia);
		sql.addOrderBy("a.nrOrdemAprovacao desc");
        return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Pesquisa as acoes relacionadas a pendencia informada ordenando os
	 * resultados de forma descendente pelos campos dhAcao e idAcao.
	 *
	 * @param idPendencia pendencia a ser pesquisada
	 * @param def criterios da pagina
	 * @return pagina de resultados
	 */
	public ResultSetPage findPaginatedByIdPendencia(Long idPendencia, FindDefinition def) {
		StringBuilder hql = new StringBuilder()
		.append(" select new map( \n")
		.append("        a.idAcao as idAcao, \n")
		.append("        u.nrMatricula as usuario_nrMatricula, \n")
		.append("        u.nmUsuario as usuario_nmUsuario, \n")
		.append("        a.dhAcao as dhAcao, \n")
		.append("        a.dhLiberacao as dhLiberacao, \n")
		.append("        a.tpSituacaoAcao as tpSituacaoAcao, \n")
		.append("        a.obAcao as obAcao, \n")
		.append("        NVL(u.nmUsuario, NVL(ulms.nmUsuario, per.dsPerfil)) as integrante \n")
		.append(" ) \n")
		.append("   from ").append(getPersistentClass().getName()).append(" a \n")
		.append("   left outer join a.usuario u \n")
		.append("   inner join a.integrante int \n")
		.append("   left outer JOIN int.usuario ulms \n")
		.append("   left outer JOIN int.perfil per \n")

		.append("  where a.pendencia.id = ? \n")
		.append("  order by a.nrOrdemAprovacao asc, \n")
		.append("  a.idAcao desc \n");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				hql.toString(),
				def.getCurrentPage(),
				def.getPageSize(),
				new Object[] {idPendencia});

		List result = rsp.getList();
		if (result != null && result.size() > 0) {
			rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result));
		}

		return rsp;
	}

	/**
	 * Calcula o numero de acoes relacionadas a pendencia informada.
	 *
	 * @param idPendencia
	 *            id da pendencia a ser pesquisada
	 * @return numero de acoes
	 */
	public Integer getRowCountByIdPendencia(Long idPendencia) {
		StringBuilder hql = new StringBuilder()
		.append(" from ").append(getPersistentClass().getName()).append(" a \n")
		.append(" left outer join a.usuario u \n")
		.append(" inner join a.integrante int \n")
		.append(" left outer JOIN int.usuario ulms \n")
		.append(" left outer JOIN int.perfil per \n")
		.append(" where a.pendencia.id = ? \n");

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[] {idPendencia});
	}

	/**
	 * Retorna a ultima acao para a pendencia recebida como parametro.
	 *
	 * @param idPendencia
	 * 			id da pendencia a ser pesquisada
	 * @return ultima acao realizada
	 */
	public List findByIdPendenciaTpSituacaoAcao(Long idPendencia, String tpSituacaoAcao) {
		Validate.notNull(idPendencia);
		Validate.notEmpty(tpSituacaoAcao);
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "a")
			.createAlias("a.usuario", "u")
			.add(Restrictions.eq("a.pendencia.id", idPendencia))
			.add(Restrictions.eq("a.tpSituacaoAcao", tpSituacaoAcao))
			.addOrder(Order.desc("a.dhAcao.value"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}


	public Acao findLastAcaoByPendencia(Long idPendencia) {
		Validate.notNull(idPendencia);
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName(),"A");
		hql.addCriteria("A.pendencia.id","=",idPendencia);

		//SUB SELECT PARA PEGAR A ULTIMA ACAO DA LIBERACAO
		StringBuffer subSelect = new StringBuffer("A.dhLiberacao.value = (SELECT MAX(A2.dhLiberacao.value) FROM ")
			.append(getPersistentClass().getName()).append(" AS A2 ")
			.append("WHERE A2.pendencia.id = ?)");

		hql.addCustomCriteria(subSelect.toString());
		hql.addCriteriaValue(idPendencia);

		return (Acao)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public List<Map<String,Object>> findPendencias(Long idPendencia) {
		StringBuilder hql = new StringBuilder()
		.append(" select new map( \n")
		.append("        a.idAcao as idAcao, \n")
		.append("        u.nrMatricula as usuario_nrMatricula, \n")
		.append("        u.nmUsuario as aprovador, \n")
		.append("        a.dhAcao as dhAcao, \n")
		.append("        a.dhLiberacao as dhLiberacao, \n")
		.append("        a.tpSituacaoAcao as tpSituacaoAcao, \n")
		.append("        a.obAcao as obAcao, \n")
		.append("        NVL(u.nmUsuario, NVL(ulms.nmUsuario, per.dsPerfil)) as integrante \n")
		.append(" ) \n")
		.append("   from ").append(getPersistentClass().getName()).append(" a \n")
		.append("   left outer join a.usuario u \n")
		.append("   inner join a.integrante int \n")
		.append("   left outer JOIN int.usuario ulms \n")
		.append("   left outer JOIN int.perfil per \n")
		.append("  where a.pendencia.id = ? \n")
		.append("  order by a.nrOrdemAprovacao asc, \n")
		.append("  a.idAcao desc \n");

		List<Map<String, Object>> list = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idPendencia});

		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(list);
	}

	public List<Object[]> findPendenciaSituacaoWorkflow(Long idDoctoServico) {

		StringBuilder sql = new StringBuilder()
				.append(" SELECT 																  ")
				.append(" 	a.ID_ACAO AS idAcao,                                                  ")
				.append(" 	u.NR_MATRICULA AS usuario_nrMatricula,                                ")
				.append(" 	u.NM_USUARIO AS aprovador,                                            ")
				.append(" 	to_char(a.DH_ACAO, 'dd/mm/yyyy hh24:mi:ss') AS dhAcao,                ")
				.append(" 	to_char(a.DH_LIBERACAO, 'dd/mm/yyyy hh24:mi:ss') AS dhLiberacao,      ")
				.append(" 	a.TP_SITUACAO_ACAO AS tpSituacaoAcao,                                 ")
				.append(" 	a.OB_ACAO AS obAcao,                                                  ")
				.append(" 	nvl(u.NM_USUARIO, nvl(usuario.NM_USUARIO, p.DS_PERFIL)) AS integrante,")
				.append(" 	a.ID_PENDENCIA AS idPendencia                                         ")
				.append(" FROM ACAO a                                                             ")
				.append(" 	,USUARIO u                                                            ")
				.append(" 	,INTEGRANTE int                                                       ")
				.append(" 	,USUARIO usuario                                                      ")
				.append(" 	,PERFIL p                                                             ")
				.append(" 	,DOCTO_SERVICO DS                                                     ")
				.append(" WHERE a.ID_USUARIO = u.ID_USUARIO(+)                                    ")
				.append(" 	AND a.ID_INTEGRANTE = int.ID_INTEGRANTE                               ")
				.append(" 	AND int.ID_USUARIO = usuario.ID_USUARIO(+)                            ")
				.append(" 	AND int.ID_PERFIL = p.ID_PERFIL(+)                                    ")
				.append(" 	AND DS.ID_PENDENCIA = a.ID_PENDENCIA                                  ")
				.append(" 	AND DS.ID_DOCTO_SERVICO = :idDoctoServico                             ")
				.append(" ORDER BY a.NR_ORDEM_APROVACAO ASC ,a.ID_ACAO DESC                       ");

		Map<String, Object> param = new HashMap<>();
		param.put("idDoctoServico", idDoctoServico);

		List<Object[]> list = getAdsmHibernateTemplate()
				.findBySql(sql.toString(), param, configureSqlQueryPendenciaSituacaoWorkflow());

		return list;
	}

	private ConfigureSqlQuery configureSqlQueryPendenciaSituacaoWorkflow(){
		return sqlQuery -> {
			sqlQuery.addScalar("idAcao", Hibernate.LONG);
			sqlQuery.addScalar("usuario_nrMatricula", Hibernate.STRING);
			sqlQuery.addScalar("aprovador", Hibernate.STRING);
			sqlQuery.addScalar("dhAcao", Hibernate.STRING);
			sqlQuery.addScalar("dhLiberacao", Hibernate.STRING);
			sqlQuery.addScalar("tpSituacaoAcao", Hibernate.STRING);
			sqlQuery.addScalar("obAcao", Hibernate.STRING);
			sqlQuery.addScalar("idPendencia", Hibernate.LONG);
		};
	}
}
