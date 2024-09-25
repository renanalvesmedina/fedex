package com.mercurio.lms.entrega.report;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import java.util.*;

public class EmitirEntregasEfetuadasOrigemService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		
		SqlTemplate sql = montaSql(map);
		
		Map parametersReport = new HashMap();
		montaFilterSummary(sql, map);
		
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
	
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr;
	}
	
	private void montaFilterSummary(SqlTemplate sql, TypedFlatMap parametros){
		if (!"".equals(parametros.getString("filial.sgFilial")) && !"".equals(parametros.getString("filial.pessoa.nmFantasia")))
			sql.addFilterSummary("filial", parametros.getString("filial.sgFilial") + " - " + parametros.getString("filial.pessoa.nmFantasia"));
				
		sql.addFilterSummary("filiaisDeOrigem", montaFiliaisFilterSummary(parametros.getList("filiais")));
		
		sql.addFilterSummary("servico", parametros.getString("servico.dsServico"));
		
		if (!"".equals(parametros.getString("dtPeriodoInicial")))
			sql.addFilterSummary("periodoDeEntregaInicial", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoInicial")));
		 
		if (!"".equals(parametros.getString("dtPeriodoFinal")))
			sql.addFilterSummary("periodoDeEntregaFinal", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoFinal")));
	}
	
	private String montaFiliaisFilterSummary(List filiais){
		
		if (filiais == null || filiais.isEmpty())
			return null;
		
		StringBuffer retorno = new StringBuffer();

		for (Iterator iter = filiais.iterator(); iter.hasNext();) {
			TypedFlatMap filial = (TypedFlatMap) iter.next();
			retorno.append(filial.getString("filial.sgFilial"))
				   .append(" - ")
				   .append(filial.getString("filial.pessoa.nmFantasia"))
				   .append(iter.hasNext()? " - " : "");			
		}
		
		return retorno.toString();
	}
	
	private SqlTemplate montaSql(TypedFlatMap parametros){
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("me.id_manifesto_entrega", "id_manifesto_entrega");
		sql.addProjection("ds.id_docto_servico", "id_docto_servico");

		sql.addProjection("(SELECT f_ds.id_filial FROM filial f_ds WHERE ds.id_filial_origem = f_ds.id_filial)", "id_filial");
		sql.addProjection("(SELECT f_ds.sg_filial FROM filial f_ds WHERE ds.id_filial_origem = f_ds.id_filial)", "sg_filial");
		sql.addProjection("(SELECT p_ds.nm_fantasia FROM pessoa p_ds WHERE p_ds.id_pessoa = ds.id_filial_origem)", "nm_fantasia");
		sql.addProjection("(SELECT SUBSTR(REGEXP_SUBSTR(s.ds_servico_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(s.ds_servico_i, 'pt_BR»[^¦]+'),  'pt_BR')+LENGTH('pt_BR')) FROM servico s WHERE ds.id_servico = s.id_servico)", "ds_servico");

		sql.addProjection("ds.id_servico", "id_servico");
		sql.addProjection("ds.tp_documento_servico", "tp_documento_servico");
		sql.addProjection("ds.nr_docto_servico", "nr_docto_servico");
		sql.addProjection("p.tp_identificacao", "tp_identificacao");
		sql.addProjection("p.nr_identificacao", "nr_identificacao");
		sql.addProjection("p.nm_pessoa", "nm_pessoa");
		sql.addProjection("ds.ds_endereco_entrega_real", "ds_endereco_entrega");
		sql.addProjection("ds.dh_emissao", "dh_emissao");
		sql.addProjection("ds.dt_prev_entrega", "dt_prev_entrega");
		sql.addProjection("ds.nr_dias_prev_entrega", "nr_dias_prev_entrega");
		sql.addProjection("ds.nr_dias_real_entrega", "nr_dias_real_entrega");
		sql.addProjection("ds.nr_dias_bloqueio", "nr_dias_bloqueio");
		sql.addProjection("(SELECT Min(nfc.nr_nota_fiscal) FROM nota_fiscal_conhecimento nfc WHERE nfc.id_conhecimento = ds.id_docto_servico)", "nr_nota_fiscal");

		StringBuffer dhChegada = new StringBuffer()
				.append("CASE WHEN ds.id_filial_origem <> me.id_filial THEN \n")
				.append("	 (SELECT max(em.dh_evento) \n")
				.append("	  FROM  pre_manifesto_documento pmd \n")
				.append("		   ,manifesto m \n")
				.append("		   ,evento_manifesto em \n")
				.append("	  WHERE pmd.id_docto_servico = ds.id_docto_servico \n")
				.append("			AND pmd.id_manifesto = m.id_manifesto \n")
				.append("			AND em.id_manifesto = m.id_manifesto \n")
				.append("			AND m.tp_manifesto = 'V' \n")
				.append("			AND em.tp_evento_manifesto = 'CP'\n")
				.append("			AND m.id_filial_destino = ds.id_filial_destino_operacional) \n")
				.append("	ELSE null \n")
				.append("END");
		sql.addProjection(dhChegada.toString(), "dh_chegada");

		StringBuffer dhDescarga = new StringBuffer()
				.append("CASE WHEN ds.id_filial_origem <> me.id_filial THEN \n")
				.append("	 (SELECT max(em.dh_evento) \n")
				.append("	  FROM  pre_manifesto_documento pmd \n")
				.append("		   ,manifesto m \n")
				.append("		   ,evento_manifesto em \n")
				.append("	  WHERE pmd.id_docto_servico = ds.id_docto_servico \n")
				.append("			AND pmd.id_manifesto = m.id_manifesto \n")
				.append("			AND em.id_manifesto = m.id_manifesto \n")
				.append("			AND m.tp_manifesto = 'V' \n")
				.append("			AND em.tp_evento_manifesto = 'FD'\n")
				.append("			AND m.id_filial_destino = ds.id_filial_destino_operacional) \n")
				.append("	ELSE null \n")
				.append("END");
		sql.addProjection(dhDescarga.toString(), "dh_descarga");

		StringBuffer dhBaixa = new StringBuffer()
				.append("(select max(eds.dh_evento) \n")
				.append("		 from evento_documento_servico eds, \n")
				.append("		 	  evento e \n")
				.append("		 where eds.id_docto_servico = ds.id_docto_servico \n")
				.append("			   and e.id_evento = eds.id_evento \n")
				.append("			   and e.cd_evento = '21' \n")
				.append("			   and eds.bl_evento_cancelado = 'N')");
		sql.addProjection(dhBaixa.toString(), "dh_baixa");

		sql.addFrom("manifesto_entrega", "me");
		sql.addFrom("manifesto_entrega_documento", "med");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("pessoa", "p");

		sql.addJoin("me.id_manifesto_entrega", "med.id_manifesto_entrega");
		sql.addJoin("med.id_docto_servico", "ds.id_docto_servico");
		sql.addJoin("ds.id_cliente_destinatario", "p.id_pessoa");

		StringBuffer existsEvento = new StringBuffer()
				.append("exists (select 1 \n")
				.append("		 from evento_documento_servico eds, \n")
				.append("		 	  evento e \n")
				.append("		 where eds.id_docto_servico = ds.id_docto_servico \n")
				.append("			   and e.id_evento = eds.id_evento \n")
				.append("			   and e.cd_evento = '21' \n")
				.append("			   and eds.bl_evento_cancelado = 'N')");

		sql.addCustomCriteria(existsEvento.toString());
		sql.addCustomCriteria("me.id_filial IN " + montaInBind(parametros.getList("filiais")), montaIn(parametros.getList("filiais")));
		sql.addCriteria("ds.id_servico", "=", parametros.getLong("servico.idServico"));
		sql.addCriteria("SYS_EXTRACT_UTC(med.dh_ocorrencia)",  ">=", retornarDataInicioDoDia(parametros.getYearMonthDay("dtPeriodoInicial")));
		sql.addCriteria("SYS_EXTRACT_UTC(med.dh_ocorrencia)", "<=", retornarDataFimDoDia(parametros.getYearMonthDay("dtPeriodoFinal")));

		sql.addOrderBy("4");
		sql.addOrderBy("6");
		sql.addOrderBy("8");
		sql.addOrderBy("9");

		return sql;
	}
	
	private String retornarDataInicioDoDia(YearMonthDay dataInicial) {
		try{
			return JTDateTimeUtils.formatDateTimeToString(dataInicial.toDateTime(new TimeOfDay(00, 00, 00)), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN);
		}catch (Exception e) {
			return JTDateTimeUtils.formatDateTimeToString(dataInicial.toDateTime(new TimeOfDay(01, 00, 00)), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN);
		}
	}

	private String retornarDataFimDoDia(YearMonthDay dataFinal) {
		try{
			return JTDateTimeUtils.formatDateTimeToString(dataFinal.toDateTime(new TimeOfDay(23, 59, 59)), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN);
		}catch (Exception e) {
			return JTDateTimeUtils.formatDateTimeToString(dataFinal.toDateTime(new TimeOfDay(22, 59, 59)), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN);
		}
	}
	
	private String montaInBind(List filiais){
		StringBuffer retorno = new StringBuffer("");
		
		if (filiais != null){
			retorno.append("(");
			int size = filiais.size();

			for (int i=0; i < size; i++){
				retorno.append("?").append(i == size-1? "" : ",");
			}
			
			retorno.append(")");
		}
		
		return retorno.toString();
	}
	
	private Object[] montaIn(List filiais){
		List retorno = new ArrayList();
		
		if (filiais != null) {
			for (Object filial : filiais) {
				TypedFlatMap element = (TypedFlatMap) filial;
				retorno.add(element.getLong("filial.idFilial"));
			}			
		}
		
		return retorno.isEmpty() ? null : retorno.toArray();
	}
}
