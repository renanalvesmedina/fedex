package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.historicoEmbarqueProibidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/historicoEmbarqueProibido.jasper"
 */
public class HistoricoEmbarqueProibidoService extends ReportServiceSupport {
	
	private List filiaisUsuario;

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		filiaisUsuario = buscaIdsFiliais(SessionUtils.getFiliaisUsuarioLogado());
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);
		
		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception {
        
		SqlTemplate sql = this.createSqlTemplate();
        
		sql.addProjection("c.ID_CLIENTE","ID_CLIENTE");
		sql.addProjection("c.ID_FILIAL_ATENDE_COMERCIAL","ID_FILIAL_ATENDE_COMERCIAL");
		sql.addProjection("c.ID_REGIONAL_COMERCIAL","ID_REGIONAL");
		sql.addProjection("r.SG_REGIONAL || ' - ' || r.DS_REGIONAL", "REGIONAL");
		sql.addProjection("f.SG_FILIAL || ' - ' || pf.NM_FANTASIA", "FILIAL");
		sql.addProjection("p.NM_PESSOA", "CLIENTE");
		sql.addProjection("p.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");		
		sql.addProjection("p.NR_IDENTIFICACAO", "CNPJ");
		sql.addProjection("pe.DT_BLOQUEIO", "DATABLOQUEIO");
        sql.addProjection("pe.DT_DESBLOQUEIO", "DATA_DESBLOQUEIO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("mpe.DS_MOTIVO_PROIBIDO_EMBARQUE_I"), "MOTIVOBLOQUEIO");
        sql.addProjection("mpe.BL_FINANCEIRO", "BL_FINANCEIRO");
		sql.addProjection("usuarioBloqueio.NM_USUARIO", "RESPONSAVEL_BLOQUEIO");
        sql.addProjection("usuarioDesbloqueio.NM_USUARIO", "RESPONSAVEL_DESBLOQUEIO");
        
        sql.addFrom("PROIBIDO_EMBARQUE pe " +
                    "  inner join MOTIVO_PROIBIDO_EMBARQUE mpe on pe.ID_MOTIVO_PROIBIDO_EMBARQUE = mpe.ID_MOTIVO_PROIBIDO_EMBARQUE " +                    
                    "  inner join USUARIO usuarioBloqueio on pe.ID_USUARIO_BLOQUEIO = usuarioBloqueio.ID_USUARIO " +
                    "  left outer join USUARIO usuarioDesbloqueio on pe.ID_USUARIO_DESBLOQUEIO = usuarioDesbloqueio.ID_USUARIO" );
        sql.addFrom("CLIENTE c " +                      
                    "  inner join PESSOA p on c.ID_CLIENTE = p.ID_PESSOA " +
                    "  inner join FILIAL f on c.ID_FILIAL_ATENDE_COMERCIAL = f.ID_FILIAL " +
                    "  inner join PESSOA pf on f.ID_FILIAL = pf.ID_PESSOA " +
                    "  left outer join REGIONAL r on c.ID_REGIONAL_COMERCIAL = r.ID_REGIONAL ");
        
        sql.addJoin("pe.ID_CLIENTE","c.ID_CLIENTE");
		
		String idRegional = tfm.getString("regional.idRegional");
		
		SQLUtils.joinExpressionsWithComma(filiaisUsuario,sql,"f.id_filial");
        
		if(StringUtils.isNotBlank(idRegional)) {
			sql.addCriteria("c.ID_REGIONAL_COMERCIAL", "=", idRegional, Long.class);
			String dsRegional = tfm.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		String idFilial = tfm.getString("filial.idFilial");
		if(StringUtils.isNotBlank(idFilial)) {
			sql.addCriteria("c.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial, Long.class);
			String dsFilial = tfm.getString("siglaNomeFilial");
			sql.addFilterSummary("filial", dsFilial);
		}

		String idMotivoProibidoEmbarque = tfm.getString("motivoProibido.idMotivoProibidoEmbarque");
		if(StringUtils.isNotBlank(idMotivoProibidoEmbarque)) {
			sql.addCriteria("mpe.ID_MOTIVO_PROIBIDO_EMBARQUE", "=", idMotivoProibidoEmbarque, Long.class);
			String dsMotivoProibidoEmbarque = tfm.getString("motivoProibido.dsMotivoProibidoEmbarque");
			sql.addFilterSummary("motivo", dsMotivoProibidoEmbarque);
		}

		YearMonthDay dataInicial = tfm.getYearMonthDay("dtEmissaoInicial");
		YearMonthDay dataFinal   = tfm.getYearMonthDay("dtEmissaoFinal");
		
		if(dataInicial != null){
			
			if( dataFinal != null ){
				sql.addCustomCriteria(" ( pe.DT_BLOQUEIO >= ? AND pe.DT_DESBLOQUEIO <= ? ) ");
				sql.addCriteriaValue(dataInicial);
				sql.addCriteriaValue(dataFinal);
				sql.addFilterSummary("periodoInicial", JTFormatUtils.format(tfm.getYearMonthDay("dtEmissaoInicial")));
				sql.addFilterSummary("periodoFinal", JTFormatUtils.format(tfm.getYearMonthDay("dtEmissaoFinal")));
			} else {
				sql.addCustomCriteria(" ( pe.DT_BLOQUEIO >= ? ) ");
				sql.addCriteriaValue(dataInicial);
				sql.addFilterSummary("periodoInicial", JTFormatUtils.format(tfm.getYearMonthDay("dtEmissaoInicial")));
			}
			
			
		} else if(dataFinal != null){
			sql.addCustomCriteria(" ( pe.DT_DESBLOQUEIO <= ? ) ");
			sql.addCriteriaValue(dataFinal);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(tfm.getYearMonthDay("dtEmissaoFinal")));
		}
		
		String idCliente = tfm.getString("cliente.idCliente");
		if(StringUtils.isNotBlank(idCliente)) {
			sql.addCriteria("c.ID_CLIENTE", "=", idCliente, Long.class);
			String dsCliente = tfm.getString("cliente.pessoa.nmPessoa");
			sql.addFilterSummary("cliente", dsCliente);
		}		

		sql.addOrderBy("r.SG_REGIONAL, " +
				       "f.SG_FILIAL, " +
				       "p.NM_PESSOA, " +
				       "p.NR_IDENTIFICACAO, " +
				       "pe.DT_BLOQUEIO");

		return sql;
	}
	
	private List buscaIdsFiliais(List filiaisUsuarioLogado) {
		List retorno = new ArrayList();
		
		for (Iterator iter = filiaisUsuarioLogado.iterator(); iter.hasNext();) {
			Filial f = (Filial) iter.next();
			retorno.add(f.getIdFilial());
		}
		
		return retorno;
	}
}
