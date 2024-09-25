package com.mercurio.lms.workflow.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.workflow.emitirAprovacaoAprovadorService"
 * @spring.property name="reportName" value="com/mercurio/lms/workflow/report/emitirAprovacaoAprovador.jasper"
 *
 */
public class EmitirAprovacaoAprovadorService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap map = new TypedFlatMap();
			
		map.putAll(parameters);
		
		//idUsuario
		Long idUsuario = null;
		String strIdUsuario = (String)parameters.get("usuario.idUsuario");
		
		if(StringUtils.isNotBlank(strIdUsuario)){
			idUsuario = Long.valueOf(strIdUsuario);
		}
		
		//tpSituacao
		String tpSituacao = (String) parameters.get("tpSituacao");
		
		
		//dtInclusaoInicial
		DateTime dhLiberacaoInicial = map.getDateTime("dhLiberacaoInicial");
		
		//dtInclusaoFinal
		DateTime dhLiberacaoFinal = map.getDateTime("dhLiberacaoFinal");
		
		Map mapFiltros = this.getMapFiltroSqlAcoes(idUsuario, map.getString("tpSituacao"), dhLiberacaoInicial, dhLiberacaoFinal);
		 		
		JRReportDataObject jr = executeQuery2(mountSql(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal),mapFiltros);
		
		SqlTemplate sql = this.createSqlTemplate();
		//Parametros do filtro
		String codPessoaNome = (String)parameters.get("usuario.nmUsuario"); 
		if (StringUtils.isNotBlank(codPessoaNome)){
			sql.addFilterSummary("aprovador", codPessoaNome);
		}
		
		if (StringUtils.isNotBlank(tpSituacao)){
			String dsSituacao = (String)parameters.get("dsSituacao");
			sql.addFilterSummary("situacao", dsSituacao);
		}
		
		if (dhLiberacaoInicial != null) {
			sql.addFilterSummary("dataLiberacaoInicial", JTFormatUtils.format(dhLiberacaoInicial, JTFormatUtils.DATETIME) );
		}
		
		if (dhLiberacaoFinal != null) {		
			sql.addFilterSummary("dataLiberacaoFinal", JTFormatUtils.format(dhLiberacaoFinal, JTFormatUtils.DATETIME) );		
		}

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		
		return jr; 
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("tpSituacao", "DM_STATUS_WORKFLOW");
	}
	
	/**
	 * Monta sql que contem todas as ações que o usuário logado deve visualizar
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @param findDef
	 * @return
	 */
	private String mountSql(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getSqlUsuarioIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlPerfilUsuarioIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoUsuarioIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoPerfilIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoUsuarioPerfilIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));				
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoFaltaUsuarioIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlSubstitutoFaltaPerfilUsuarioIntegrante(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" UNION ");
		sql.append(getSqlHistoricoAcao(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		
		
		sql.append(" ORDER BY FUNCIONARIO, DATASOLICITACAO, DESCTIPOEVENTO ");
		
		return sql.toString();
	}
	
	/**
	 * Monta o map de filtros
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return Map
	 * */
	private Map getMapFiltroSqlAcoes(Long idUsuario, String dsTipoEvento, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		Map map = new HashMap();
		map.put("idUsuario",idUsuario);
		map.put("dsTipoEvento",dsTipoEvento);
		map.put("dhLiberacaoInicial",dhLiberacaoInicial);
		map.put("dhLiberacaoFinal",dhLiberacaoFinal);
		map.put("dhAtual",JTDateTimeUtils.getDataHoraAtual());
		map.put("dtAtual",JTDateTimeUtils.getFirstHourOfDay(JTDateTimeUtils.getDataHoraAtual()));
		return map;
	}
	
	/**
	 * Monta o sql que retorna as ações não pendente
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlHistoricoAcao(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(getBasicJoins());
		
		sql.append(" and		A.ID_USUARIO = US.ID_USUARIO \n");		

		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append(" and		US.ID_USUARIO = :idUsuario \n");	

		return sql.toString();
		
	}	
	
	/**
	 * Monta o sql que retorna as ações onde o usuário logado
	 * está configurado como usuário integrante do comitê de aprovação
	 * @param dsTipoEvento
	 * @param dhLiberacaoInicial
	 * @param dhLiberacaoFinal
	 * @return
	 */
	private String getSqlUsuarioIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(getBasicJoins());
		
		sql.append(" and		INT.ID_USUARIO = US.ID_USUARIO \n");		

		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
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
	private String getSqlPerfilUsuarioIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());		
		sql.append(", 		PERFIL_USUARIO PU \n");

		sql.append(getBasicJoins());
		sql.append(" and		PU.ID_PERFIL = INT.ID_PERFIL \n");
		
		sql.append(" and		PU.ID_USUARIO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
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
	private String getSqlSubstitutoUsuarioIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");		
		
		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUIDO = INT.ID_USUARIO \n");
		
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		(( trunc( :dtAtual ) BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL) OR A.ID_USUARIO = :idUsuario ) \n");
		
		return sql.toString();
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
	private String getSqlSubstitutoPerfilIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");		

		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_PERFIL_SUBSTITUIDO = INT.ID_PERFIL \n");
		
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		(( trunc( :dtAtual ) BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL) OR A.ID_USUARIO = :idUsuario ) \n");
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
	private String getSqlSubstitutoUsuarioPerfilIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO SUB \n");
		sql.append(", 		PERFIL_USUARIO PERUSU \n");		
		
		sql.append(getBasicJoins());
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUIDO = PERUSU.ID_USUARIO \n");
		sql.append(" and		PERUSU.ID_PERFIL = INT.ID_PERFIL \n");	
		
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
		sql.append(" and		SUB.ID_USUARIO_SUBSTITUTO = :idUsuario ");
		sql.append("\n and		(( trunc( :dtAtual ) BETWEEN SUB.DT_SUBSTITUICAO_INICIAL and SUB.DT_SUBSTITUICAO_FINAL) OR A.ID_USUARIO = :idUsuario ) \n");
		
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
	private String getSqlSubstitutoFaltaUsuarioIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(getProjection());
		sql.append(getBasicFrom());
		sql.append(", 		SUBSTITUTO_FALTA SF \n");		
		sql.append(", 		SUBSTITUTO_FALTA_ACAO SFA \n");
		
		sql.append(getBasicJoins());
		sql.append(" and		SF.ID_INTEGRANTE = INT.ID_INTEGRANTE \n");	
		sql.append(" and		SF.ID_SUBSTITUTO_FALTA = SFA.ID_SUBSTITUTO_FALTA \n");
		sql.append(" and		A.ID_ACAO = SFA.ID_ACAO \n");
		
		sql.append(" and		SF.ID_USUARIO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
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
	private String getSqlSubstitutoFaltaPerfilUsuarioIntegrante(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){
		
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
		
		sql.append(" and		PU.ID_USUARIO = US.ID_USUARIO \n");		
		
		sql.append(getBasicWhere(idUsuario, tpSituacao, dhLiberacaoInicial, dhLiberacaoFinal));
		sql.append("and		A.TP_SITUACAO_ACAO = 'E' \n");
		sql.append(" and		PU.ID_USUARIO = :idUsuario \n");
		sql.append("\n and		SF.TP_SITUACAO = 'A' \n");
		
		return sql.toString();
	}

	/**
	 * Retorna a projeção dos SQL do UNION
	 * @return
	 */
	private String getProjection(){

		StringBuffer sProjection = new StringBuffer();
		sProjection.append("select uso.NM_USUARIO as FUNCIONARIO, a.DH_INCLUSAO as DATAINCLUSAO, \n" +
				PropertyVarcharI18nProjection.createProjection("te.DS_TIPO_EVENTO_I", "DESCTIPOEVENTO") + ", " + 
				PropertyVarcharI18nProjection.createProjection("vd.ds_valor_dominio_i", "STATUS") + ", " + 
				"a.DH_LIBERACAO as DATALIBERACAO, oco.DH_INCLUSAO as DATASOLICITACAO, \n" +
				"a.id_acao as idAcao, oco.dh_inclusao as dhInclusao, te.id_tipo_evento as idTipoEvento, " +
				"p.ds_pendencia as dspendencia \n");
		return sProjection.toString();
	
	}
	
	/**
	 * Retorna as tabelas que serão acessadas por todos os sqls
	 * @return
	 */
	private String getBasicFrom(){

		StringBuffer sFrom = new StringBuffer(); 
		
		sFrom.append("from		ACAO A, \n");
		sFrom.append("		PENDENCIA P, \n");
		sFrom.append("		OCORRENCIA OCO, \n");
		sFrom.append("		EVENTO_WORKFLOW EW, \n");
		sFrom.append("		V_TIPO_EVENTO_I TE, \n");
		sFrom.append("		V_COMITE_I COM, \n");
		sFrom.append("		INTEGRANTE INT, \n");
		sFrom.append(" 		USUARIO_ADSM US, \n");		
		sFrom.append(" 		USUARIO_LMS USL, \n");
		sFrom.append(" 		USUARIO_ADSM USO, \n");	
		
		sFrom.append("		DOMINIO DOM, \n");
		sFrom.append("		VALOR_DOMINIO VD \n");			

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
		sJoins.append("and		US.ID_USUARIO = USL.ID_USUARIO \n");
		
		sJoins.append("and		OCO.ID_USUARIO = USO.ID_USUARIO \n");
		
		sJoins.append("and		DOM.ID_DOMINIO = VD.ID_DOMINIO \n");		
		sJoins.append("and		VD.VL_VALOR_DOMINIO = A.TP_SITUACAO_ACAO \n");		
		
		return sJoins.toString();

	}
	
	/**
	 * Retorna os criterios que serão acessadas por todos os sqls
	 * @return
	 */
	private String getBasicWhere(Long idUsuario, String tpSituacao, DateTime dhLiberacaoInicial, DateTime dhLiberacaoFinal){

		StringBuffer sWhere = new StringBuffer();

		sWhere.append("and		A.BL_LIBERADA = 'S' \n"); 					
		sWhere.append("and		DOM.NM_DOMINIO = 'DM_STATUS_WORKFLOW' \n");
		sWhere.append("and		A.DH_LIBERACAO <= :dhAtual \n"); 		
		
		
		sWhere.append("and      EXISTS (SELECT * \n" +
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
		
		if (StringUtils.isNotBlank(tpSituacao)){
			if (tpSituacao.equals("E")) {
				sWhere.append("and		P.TP_SITUACAO_PENDENCIA = 'E' \n");
			}
			sWhere.append("and		A.TP_SITUACAO_ACAO = '"+tpSituacao+"'\n");
		}		
		
		if (dhLiberacaoInicial != null) {
			sWhere.append("and		A.DH_LIBERACAO  >= :dhLiberacaoInicial \n");
		}
		if (dhLiberacaoFinal != null) {
			sWhere.append("and		A.DH_LIBERACAO <= :dhLiberacaoFinal \n");
		}
		
		return sWhere.toString();

	}
	
	protected final JRReportDataObject executeQuery2(String sql, Map args) {
		List keys = new ArrayList(); int pos = 0;
		
		sql = StringUtils.replace(sql, "i:i18n", "i_i18n");   
		sql = StringUtils.replace(sql, "xmlns:i", "xmlns_i");
		sql = StringUtils.replace(sql, "http:", "http_");
		sql = StringUtils.replace(sql, "HH:MI", "HH_MI");
		
		while ((pos = sql.indexOf(":", pos + 1)) > 0) {
			keys.add(sql.substring(pos + 1, sql.indexOf(' ', pos)));
		}
		sql = sql.replaceAll(":[^ ]*", "? "); 
		
		sql = StringUtils.replace(sql, "i_i18n", "i:i18n"); 
		sql = StringUtils.replace(sql, "xmlns_i", "xmlns:i");
		sql = StringUtils.replace(sql, "http_", "http:");
		sql = StringUtils.replace(sql, "HH_MI", "HH:MI");

		Object[] values = new Object[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			values[i] = args.get(keys.get(i));
		}
		return executeQuery(sql, JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), values));
	}	
}