package com.mercurio.lms.entrega.report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author José Rodrigo Moraes
 * @since  29/05/2006
 * 
 * @spring.bean id="lms.entrega.emitirTentativasAgendamentoService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirTentativasAgendamento.jasper"
 */
public class EmitirTentativasAgendamentoService extends ReportServiceSupport {
 
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);
        
        createFilterSummary(sql,tfm);
        
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	                       
	    Map parametersReport = new HashMap();
        
        /** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        
        return jr;
        
	} 
	
	/**
	 * Monta os dados de filtragem para o cabeçalho do relatório
	 * @param sql SqlTemplate que abrigará os filtros
	 * @param tfm Filtros da tela
	 */
	private void createFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		
		//Filial Origem
		if( (tfm.getString("sgFilialOrigem") != null && !tfm.getString("sgFilialOrigem").equals("")) &&
			(tfm.getString("filialByIdFilialOrigem.pessoa.nmFantasia") != null && !tfm.getString("filialByIdFilialOrigem.pessoa.nmFantasia").equals("")) ){
			sql.addFilterSummary("filialOrigem",tfm.getString("sgFilialOrigem") + " - " + tfm.getString("filialByIdFilialOrigem.pessoa.nmFantasia"));
		}
		
		//Filial Destino
		if( (tfm.getString("sgFilialDestino") != null && !tfm.getString("sgFilialDestino").equals("")) &&
			(tfm.getString("filialByIdFilialDestino.pessoa.nmFantasia") != null && !tfm.getString("filialByIdFilialDestino.pessoa.nmFantasia").equals("")) ){
			sql.addFilterSummary("filialDestino",tfm.getString("sgFilialDestino") + " - " + tfm.getString("filialByIdFilialDestino.pessoa.nmFantasia"));
		} 
		
		//Filial Agendamento
		if( (tfm.getString("sgFilialAgendamento") != null && !tfm.getString("sgFilialAgendamento").equals("")) &&
			(tfm.getString("filialAgendamento.pessoa.nmFantasia") != null && !tfm.getString("filialAgendamento.pessoa.nmFantasia").equals("")) ){
			sql.addFilterSummary("filialAgendamento",tfm.getString("sgFilialAgendamento") + " - " + tfm.getString("filialAgendamento.pessoa.nmFantasia"));
		}
		
		//Remetente
		if (tfm.getString("remetenteNrIdentificacao") != null && !tfm.getString("remetenteNrIdentificacao").equals("")){
			sql.addFilterSummary("remetente",tfm.getString("remetenteNrIdentificacao") + " - " +tfm.getString("remetente.pessoa.nmPessoa"));
		}
		
		
		//Destinatário
		if (tfm.getString("destinatarioNrIdentificacao") != null && !tfm.getString("destinatarioNrIdentificacao").equals("")){
			sql.addFilterSummary("destinatario",tfm.getString("destinatarioNrIdentificacao") + " - " + tfm.getString("destinatario.pessoa.nmPessoa"));
		}
		
		//Documento de Serviço
		if( tfm.getLong("idDoctoServico") != null ){
			sql.addFilterSummary("documentoServico",tfm.getString("dsTpDocumentoServico") + " " + 
					                                tfm.getString("doctoServico.filialByIdFilialOrigem.sgFilial") + " " +
					                                FormatUtils.formataNrDocumento(tfm.getString("nrDoctoServico"),tfm.getString("doctoServico.tpDocumentoServico")));
		}
		
		YearMonthDay dtInicial = tfm.getYearMonthDay("lancamentoInicial");
		YearMonthDay dtFinal   = tfm.getYearMonthDay("lancamentoFinal");
		
		if( dtInicial != null ){
			if( dtFinal != null ){
				sql.addFilterSummary("periodoTentativaAgendamentoInicial",JTFormatUtils.format(dtInicial,JTFormatUtils.MEDIUM));
				sql.addFilterSummary("periodoTentativaAgendamentoFinal",JTFormatUtils.format(dtFinal,JTFormatUtils.MEDIUM));
			} else {
				sql.addFilterSummary("periodoTentativaAgendamentoInicial",JTFormatUtils.format(dtInicial,JTFormatUtils.MEDIUM));
			}
		} else if( dtFinal != null ){
			sql.addFilterSummary("periodoTentativaAgendamentoFinal",JTFormatUtils.format(dtFinal,JTFormatUtils.MEDIUM));
		}
		
	}
	
	/**
	 * Monta a query padrão do relatório
	 * @param parameters Critérios de pesquisa da tela
	 * @return SqlTemplate contendo a query e os parâmetros de critério
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {
		
		SqlTemplate sql = createSqlTemplate();
		 
		sql.addProjection("DS.ID_DOCTO_SERVICO            AS ID_DOCTO_SERVICO, " +
				          "DS.TP_DOCUMENTO_SERVICO        AS TP_DOCUMENTO_SERVICO, " +
				          "DS.NR_DOCTO_SERVICO            AS NR_DOCTO_SERVICO, " +
				          "FIL_ORIGEM.SG_FILIAL       	  AS SG_FILIAL_ORIGEM, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_TP_DOCUMENTO_SERVICO") + ", " +
				          "PES_REMET.TP_IDENTIFICACAO     AS TP_IDENTIFICACAO_REMETENTE, " +
				          "PES_REMET.NR_IDENTIFICACAO     AS NR_IDENTIFICACAO_REMETENTE, " +
				          "PES_REMET.NM_PESSOA            AS NM_REMETENTE, " +
				          "PES_DEST.TP_IDENTIFICACAO      AS TP_IDENTIFICACAO_DESTINATARIO, " +
				          "PES_DEST.NR_IDENTIFICACAO      AS NR_IDENTIFICACAO_DESTINATARIO, " +
				          "PES_DEST.NM_PESSOA             AS NM_DESTINATARIO, " +
				          "FIL_DESTINO.SG_FILIAL          AS SG_FILIAL_DESTINO, " +
				          "PES_FIL_DESTINO.NM_FANTASIA    AS NM_FILIAL_DESTINO, " +
				          "FIL_AGENDA.ID_FILIAL           AS ID_FILIAL_AGENDAMENTO, " +
				          "FIL_AGENDA.SG_FILIAL           AS SG_FILIAL_AGENDAMENTO, " +
				          "PES_FIL_AGENDA.NM_FANTASIA     AS NM_FILIAL_AGENDAMENTO, " +
				          "AE.DH_CONTATO                  AS DH_CONTATO, " +
				          "AE.NR_DDD                      AS NR_DDD, " +
				          "AE.NR_TELEFONE                 AS NR_TELEFONE, " +
				          "AE.NR_RAMAL                    AS NR_RAMAL, " +
				          "AE.OB_TENTATIVA                AS OCORRENCIA, " +
				          "AE.OB_AGENDAMENTO_ENTREGA      AS OBSERVACAO, " +
	 			          "USU_CRIACAO.NM_USUARIO         AS NM_USUARIO");
		
		sql.addFrom("AGENDAMENTO_ENTREGA AE " +
				    "	INNER JOIN USUARIO USU_CRIACAO            ON AE.ID_USUARIO_CRIACAO      = USU_CRIACAO.ID_USUARIO " +
				    "   INNER JOIN FILIAL FIL_AGENDA              ON AE.ID_FILIAL               = FIL_AGENDA.ID_FILIAL " +
				    "   INNER JOIN PESSOA PES_FIL_AGENDA          ON FIL_AGENDA.ID_FILIAL       = PES_FIL_AGENDA.ID_PESSOA " +
				    "   INNER JOIN AGENDAMENTO_DOCTO_SERVICO ADS  ON AE.ID_AGENDAMENTO_ENTREGA  = ADS.ID_AGENDAMENTO_ENTREGA " +
				    "   INNER JOIN DOCTO_SERVICO DS               ON ADS.ID_DOCTO_SERVICO       = DS.ID_DOCTO_SERVICO " +
				    "   INNER JOIN FILIAL FIL_ORIGEM              ON DS.ID_FILIAL_ORIGEM        = FIL_ORIGEM.ID_FILIAL " +
				    "   LEFT OUTER JOIN FILIAL FIL_DESTINO        ON DS.ID_FILIAL_DESTINO       = FIL_DESTINO.ID_FILIAL " +
				    "   LEFT OUTER JOIN PESSOA PES_FIL_DESTINO    ON FIL_DESTINO.ID_FILIAL      = PES_FIL_DESTINO.ID_PESSOA " +
				    "   LEFT OUTER JOIN CLIENTE REMETENTE         ON DS.ID_CLIENTE_REMETENTE    = REMETENTE.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PES_REMET          ON REMETENTE.ID_CLIENTE       = PES_REMET.ID_PESSOA " +
				    "   LEFT OUTER JOIN CLIENTE DESTINATARIO      ON DS.ID_CLIENTE_DESTINATARIO = DESTINATARIO.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PES_DEST           ON DESTINATARIO.ID_CLIENTE    = PES_DEST.ID_PESSOA, " +
				    "VALOR_DOMINIO VD " +
				    "   INNER JOIN DOMINIO D                      ON VD.ID_DOMINIO              = D.ID_DOMINIO ");
		
		sql.addCriteria("AE.TP_AGENDAMENTO","=","TA");
		sql.addCriteria("D.NM_DOMINIO","=","DM_TIPO_DOCUMENTO_SERVICO");
		sql.addJoin("VD.VL_VALOR_DOMINIO","DS.TP_DOCUMENTO_SERVICO");
		
		sql.addCriteria("FIL_AGENDA.ID_FILIAL","=",parameters.getLong("filialAgendamento.idFilial"));
		sql.addCriteria("FIL_DESTINO.ID_FILIAL","=",parameters.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("FIL_ORIGEM.ID_FILIAL","=",parameters.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("DS.ID_DOCTO_SERVICO","=",parameters.getLong("idDoctoServico"));
		
		sql.addCriteria("REMETENTE.ID_CLIENTE","=",parameters.getLong("remetente.idCliente"));
		sql.addCriteria("DESTINATARIO.ID_CLIENTE","=",parameters.getLong("destinatario.idCliente"));
		 
		YearMonthDay dtInicial = parameters.getYearMonthDay("lancamentoInicial");
		YearMonthDay dtFinal   = parameters.getYearMonthDay("lancamentoFinal");
		
		if( dtInicial != null ){
			
			if( dtFinal != null ){
				sql.addCustomCriteria("TRUNC(CAST(DH_CONTATO AS DATE)) BETWEEN ? AND ?");
				sql.addCriteriaValue(dtInicial);
				sql.addCriteriaValue(dtFinal);
			} else {
				sql.addCriteria("TRUNC(CAST(DH_CONTATO AS DATE))",">=",dtInicial);
			}
			
		} else if( dtFinal != null ){
			sql.addCriteria("DH_CONTATO","<=",dtFinal);
		}

		sql.addOrderBy("FIL_AGENDA.SG_FILIAL," +
					   "DS.TP_DOCUMENTO_SERVICO, " +
				       "FIL_ORIGEM.SG_FILIAL, " +
				       "DS.NR_DOCTO_SERVICO, " +
				       "AE.DH_CONTATO");
	
		return sql;
	}

	/**
	 * Verifica se o usuário tem permissão de acesso a alguma das filiais passadas por parâmetro
	 * @param idFilialOrigem Identificador da filial de origem
	 * @param idFilialDestino Identificador da filial de destino
	 * @param idFilialAgendamento Identificador da filial de agendamento
	 * @return Boolean.TRUE se tem permissão e Boolean.FALSE caso contrário
	 */
	public Serializable findPermissaoUsuario(Long idFilialOrigem, Long idFilialDestino, Long idFilialAgendamento) {
		
		List filiais = SessionUtils.getFiliaisUsuarioLogado();
		
		Map chaves = new HashMap();
		
		for (Iterator iter = filiais.iterator(); iter.hasNext();) {			
			Filial filial = (Filial) iter.next();			
			chaves.put(filial.getIdFilial(),filial.getSgFilial());
		}
		
		if( (idFilialOrigem == null || !chaves.containsKey(idFilialOrigem)) && 
			(idFilialDestino == null || !chaves.containsKey(idFilialDestino)) &&
			(idFilialAgendamento == null || !chaves.containsKey(idFilialAgendamento))){
			throw new BusinessException("LMS-09073");
		}
		
		return Boolean.TRUE;
		
	}	

}
