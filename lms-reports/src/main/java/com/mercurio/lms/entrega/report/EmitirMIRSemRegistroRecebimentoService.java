package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirMIRSemRegistroRecebimtoService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirMIRSemRegistroRecebimto.jasper"
 */
public class EmitirMIRSemRegistroRecebimentoService extends
		ReportServiceSupport {
	
	private DomainValueService domainValueService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);
	    configureFilterSummary(sql,tfm);
	    
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	                       
        Map parametersReport = new HashMap(); 
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("dsSimboloMoeda", SessionUtils.getMoedaSessao().getDsSimbolo());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        
        return jr; 
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("M.ID_MIR");
		sql.addProjection("M.NR_MIR");
		sql.addProjection("M.TP_MIR");
		sql.addProjection("M.TP_DOCUMENTO_MIR");
		sql.addProjection("M.DH_EMISSAO");
		sql.addProjection("M.DH_ENVIO");
		sql.addProjection("FO.ID_FILIAL","ID_FILIAL_ORIGEM");
		sql.addProjection("FO.SG_FILIAL","SG_FILIAL_ORIGEM");
		sql.addProjection("PO.NM_FANTASIA","NM_FILIAL_ORIGEM");
		sql.addProjection("FD.ID_FILIAL","ID_FILIAL_DESTINO");
		sql.addProjection("FD.SG_FILIAL","SG_FILIAL_DESTINO");
		sql.addProjection("PD.NM_FANTASIA","NM_FILIAL_DESTINO");
		sql.addProjection("UO.NM_USUARIO","NM_USUARIO_ORIGEM");
		sql.addProjection("UD.NM_USUARIO","NM_USUARIO_RECEBIMENTO");

		StringBuffer sqlFrom = new StringBuffer()
				.append("MIR M INNER JOIN FILIAL FO ON FO.ID_FILIAL = M.ID_FILIAL_ORIGEM " +
						"INNER JOIN PESSOA PO ON PO.ID_PESSOA = FO.ID_FILIAL " +
						"INNER JOIN FILIAL FD ON FD.ID_FILIAL = M.ID_FILIAL_DESTINO " +
						"INNER JOIN PESSOA PD ON PD.ID_PESSOA = FD.ID_FILIAL " +
						" LEFT JOIN USUARIO UO ON UO.ID_USUARIO = M.ID_USUARIO_CRIACAO " +
						" LEFT JOIN USUARIO UD ON UD.ID_USUARIO = M.ID_USUARIO_RECEBIMENTO ");
		sql.addFrom(sqlFrom.toString());
		
		sql.addCustomCriteria("DH_RECEBIMENTO IS NULL");

		Long idMir = parameters.getLong("mir.idMir");
		sql.addCriteria("M.ID_MIR","=",idMir);
		Long idRemetente = parameters.getLong("clienteOrigem.idCliente");
		Long idDestinatario = parameters.getLong("clienteDestino.idCliente");
		if (idRemetente != null || idDestinatario != null) {
			StringBuffer crit = new StringBuffer();

			crit.append("(");
			crit.append("  EXISTS(");
			crit.append("    SELECT * FROM DOCUMENTO_MIR DM1,");
			crit.append("      REGISTRO_DOCUMENTO_ENTREGA RDE,");
			crit.append("      DOCTO_SERVICO DS1");
			crit.append("    WHERE DM1.ID_MIR = M.ID_MIR");
			crit.append("      AND RDE.ID_REGISTRO_DOCUMENTO_ENTREGA = DM1.ID_REGISTRO_DOCUMENTO_ENTREGA");
			crit.append("      AND DS1.ID_DOCTO_SERVICO = RDE.ID_DOCTO_SERVICO");

			if (idRemetente != null) {
				crit.append("  AND DS1.ID_CLIENTE_REMETENTE = ?");
				sql.addCriteriaValue(idRemetente);
			}
			if (idDestinatario != null) {
				crit.append("  AND DS1.ID_CLIENTE_DESTINATARIO = ?");
				sql.addCriteriaValue(idDestinatario);
			}

			crit.append("  )");
			crit.append("  OR");
			crit.append("  EXISTS(");
			crit.append("    SELECT * FROM DOCUMENTO_MIR DM2,");
			crit.append("      RECIBO_REEMBOLSO RR,");
			crit.append("      DOCTO_SERVICO DS2");
			crit.append("    WHERE DM2.ID_MIR = M.ID_MIR");
			crit.append("      AND RR.ID_RECIBO_REEMBOLSO = DM2.ID_RECIBO_REEMBOLSO");
			crit.append("      AND DS2.ID_DOCTO_SERVICO = RR.ID_RECIBO_REEMBOLSO");

			if (idRemetente != null) {
				crit.append("  AND DS2.ID_CLIENTE_REMETENTE = ?");
				sql.addCriteriaValue(idRemetente);
			}
			if (idDestinatario != null) {
				crit.append("  AND DS2.ID_CLIENTE_DESTINATARIO = ?");
				sql.addCriteriaValue(idDestinatario);
			}
			crit.append("  )");
			crit.append(")");
			 
			sql.addCustomCriteria(crit.toString());
		}

		sql.addCriteria("FO.ID_FILIAL","=",parameters.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("FD.ID_FILIAL","=",parameters.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("M.TP_MIR","=",parameters.getString("tpMir"));
		sql.addCriteria("M.ID_USUARIO_CRIACAO","=",parameters.getLong("usuarioByIdUsuarioCriacao.idUsuario"));
		sql.addCriteria("M.ID_USUARIO_RECEBIMENTO","=",parameters.getLong("usuarioByIdUsuarioRecebimento.idUsuario"));
		
		sql.addOrderBy("FO.SG_FILIAL");
		sql.addOrderBy("UO.NM_USUARIO");
		sql.addOrderBy("M.TP_MIR");
		sql.addOrderBy("FD.SG_FILIAL");
		sql.addOrderBy("M.NR_MIR");
		
		return sql;
	}
	
	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap params) {
	
		if (params.getLong("filialByIdFilialOrigem.idFilial") != null) {
			String filialOrigem = params.getString("filialByIdFilialOrigem.sgFilial").concat(" - ")
					.concat(params.getString("filialByIdFilialOrigem.pessoa.nmFantasia"));
			sql.addFilterSummary("filialOrigem",filialOrigem);
		}
		
		if (params.getLong("filialByIdFilialDestino.idFilial") != null) {
			String filialDestino = params.getString("filialByIdFilialDestino.sgFilial").concat(" - ")
					.concat(params.getString("filialByIdFilialDestino.pessoa.nmFantasia"));
			sql.addFilterSummary("filialDestino",filialDestino);
		}
		
		String tpMir = params.getString("tpMir");
		if (StringUtils.isNotBlank(tpMir)) {
			DomainValue dvTpMir = domainValueService.findDomainValueByValue("DM_TIPO_MIR",tpMir);
			sql.addFilterSummary("tipoMir",dvTpMir.getDescription());
		}		
		
		if (params.getLong("mir.idMir") != null) {
			String mir = params.getString("filialByIdFilialOrigem.sgFilial").concat(" ")
					.concat(FormatUtils.formatIntegerWithZeros(params.getInteger("mir.nrMir"),"0000000000"));
			sql.addFilterSummary("mir",mir);
		}

		String clienteOrigem = params.getString("clienteOrigem.pessoa.nrItentificacaoV");
		if (StringUtils.isNotBlank(clienteOrigem)) {
			sql.addFilterSummary("remetente",clienteOrigem + " - " + params.getString("clienteOrigem.pessoa.nmPessoa"));
		}
		String clienteDestino = params.getString("clienteDestino.pessoa.nrItentificacaoV");
		if (StringUtils.isNotBlank(clienteDestino)) {
			sql.addFilterSummary("destinatario",clienteDestino + " - " + params.getString("clienteDestino.pessoa.nmPessoa"));
		}
		sql.addFilterSummary("funcionarioOrigem",params.getString("usuarioByIdUsuarioCriacao.nmUsuario"));
		sql.addFilterSummary("funcionarioDestino",params.getString("usuarioByIdUsuarioRecebimento.nmUsuario"));
	}

	public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_MIR", "DM_TIPO_MIR");
        config.configDomainField("TP_DOCUMENTO_MIR", "DM_TIPO_DOCUMENTO_MIR");
    }
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
