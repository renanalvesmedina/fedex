package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Maurício Moraes
 *
 * @spring.bean id="lms.expedicao.crtEmitidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/relatorioCRTsEmitidos.jasper"
 */
public class CRTEmitidoService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		YearMonthDay dataInicial = ((TypedFlatMap)parameters).getYearMonthDay("dtInicial");
		YearMonthDay dataFinal = ((TypedFlatMap)parameters).getYearMonthDay("dtFinal");
		
		int intervaloDias = JTDateTimeUtils.getIntervalInDays(dataInicial,dataFinal);
		if(intervaloDias>30){
			throw new BusinessException("LMS-04134");
		}
		
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("fo.sg_filial", "sg_filial_origem");
		sql.addProjection("pfo.nm_fantasia","nm_filial_origem");
		sql.addProjection("ci.nr_crt","nr_crt");
		sql.addProjection("ci.vl_total_mercadoria","vl_total_mercadoria");
		sql.addProjection("ci.vl_volume","vl_volume");
		sql.addProjection("ds.ps_real","ps_real");
		sql.addProjection("ds.dh_emissao","dh_emissao");
		sql.addProjection("pr.nm_pessoa","nm_pessoa_remetente");
		sql.addProjection("pd.nm_pessoa","nm_pessoa_destinatario");
		sql.addProjection("fd.sg_filial", "sg_filial_destino");
		sql.addProjection("pfd.nm_fantasia","nm_filial_destino");
		sql.addProjection("vfo.vl_frete_remetente","vl_frete_remetente");
		sql.addProjection("vfd.vl_frete_destinatario","vl_frete_destinatario");
		sql.addProjection("m.sg_moeda || ' ' ||  m.ds_simbolo","moeda");
		
		sql.addFrom("docto_servico","ds");
		sql.addFrom("cto_internacional","ci");
		sql.addFrom("filial","fo");
		sql.addFrom("pessoa","pfo");
		sql.addFrom("filial","fd");
		sql.addFrom("pessoa","pfd");
		sql.addFrom("pessoa","pr");
		sql.addFrom("pessoa","pd");
		StringBuffer sb = new StringBuffer();
		sb.append("(select ci.id_cto_internacional as id_cto_internacional, sum(tci.vl_frete_remetente) as vl_frete_remetente ")
		.append("from trecho_cto_int tci, cto_internacional ci ")
		.append("where tci.id_cto_internacional=ci.id_cto_internacional ")
		.append("group by ci.id_cto_internacional)");
		sql.addFrom(sb.toString(),"vfo");
		sb = new StringBuffer();
		sb.append("(select ci.id_cto_internacional as id_cto_internacional, sum(tci.vl_frete_destinatario) as vl_frete_destinatario ")
		.append("from trecho_cto_int tci, cto_internacional ci ")
		.append("where tci.id_cto_internacional=ci.id_cto_internacional ")
		.append("group by ci.id_cto_internacional)");
		sql.addFrom(sb.toString(),"vfd");
		sql.addFrom("moeda","m");

		sql.addCustomCriteria("ds.id_docto_servico = ci.id_cto_internacional");
		sql.addCustomCriteria("ds.id_filial_origem = fo.id_filial(+)");
		sql.addCustomCriteria("ds.id_filial_destino = fd.id_filial(+)");
		sql.addCustomCriteria("ds.id_cliente_remetente = pr.id_pessoa(+)");
		sql.addCustomCriteria("ds.id_cliente_destinatario = pd.id_pessoa(+)");
		sql.addCustomCriteria("ds.tp_documento_servico = 'CRT'");
		sql.addCustomCriteria("ci.tp_situacao_crt = 'E'");
		sql.addCustomCriteria("ci.id_cto_internacional = vfo.id_cto_internacional(+)");
		sql.addCustomCriteria("ci.id_cto_internacional = vfd.id_cto_internacional(+)");
		sql.addCustomCriteria("ds.id_moeda = m.id_moeda");
		sql.addCustomCriteria("fo.id_filial = pfo.id_pessoa(+)");
		sql.addCustomCriteria("fd.id_filial = pfd.id_pessoa(+)");

		sql.addOrderBy("fo.sg_filial");
		sql.addOrderBy("ci.nr_crt");
		sql.addOrderBy("ds.dh_emissao");
		
		String filiaisCriteria = this.createFiliaisCriteria();
		if(filiaisCriteria != null) {
			sql.addCustomCriteria(filiaisCriteria);
		}

		Long idFilialOrigem = parameters.getLong("filialOrigem.idFilial");
		if(idFilialOrigem != null) {
			sql.addCriteria("ds.id_filial_origem", "=", idFilialOrigem);
			String dsFilialOrigem = parameters.getString("filialOrigem.sgFilial");
			sql.addFilterSummary("filialOrigem", dsFilialOrigem);
		}

		Long idFilialDestino = parameters.getLong("filialDestino.idFilial");
		if(idFilialDestino != null) {
			sql.addCriteria("ds.id_filial_destino", "=", idFilialDestino);
			String dsFilialDestino = parameters.getString("filialDestino.sgFilial");
			sql.addFilterSummary("filialDestino", dsFilialDestino);
		}

		YearMonthDay dataInicial = parameters.getYearMonthDay("dtInicial");
		if(dataInicial != null) {
			sql.addCriteria("ds.dh_emissao", ">=", dataInicial, YearMonthDay.class);
			sql.addFilterSummary("periodoEmissaoCRTInicial", JTFormatUtils.format(dataInicial, JTFormatUtils.MEDIUM));
		}

		YearMonthDay dataFinal = parameters.getYearMonthDay("dtFinal");
		if(dataFinal != null) {
			sql.addCriteria("ds.dh_emissao", "<", dataFinal.plusDays(1), YearMonthDay.class);
			sql.addFilterSummary("periodoEmissaoCRTFinal", JTFormatUtils.format(dataFinal, JTFormatUtils.MEDIUM));
		}

		return sql;
	}
	
	private String createFiliaisCriteria() {
		List filiais = SessionUtils.getFiliaisUsuarioLogado();
		if(filiais != null) {
			StringBuilder sql = new StringBuilder();
			boolean first = true;
			for(Iterator it = filiais.iterator(); it.hasNext();) {
				Filial filial = (Filial) it.next();
				if(first) {
					first = false;
					sql.append("ds.id_filial_origem in (").append(filial.getIdFilial());
				} else if(it.hasNext()) {
					sql.append(", ").append(filial.getIdFilial());
				} 
				if (!it.hasNext()) {
					sql.append(")");
				}
			}
			return sql.toString();
		}
		return null;
	}
}
