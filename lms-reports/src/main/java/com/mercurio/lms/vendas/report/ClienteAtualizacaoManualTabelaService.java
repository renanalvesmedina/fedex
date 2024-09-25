package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.clienteAtualizacaoManualTabelaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesAtualizacaoManualTabela.jasper"
 */
public class ClienteAtualizacaoManualTabelaService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());

		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);

		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

	
		sql.setDistinct();
		sql.addProjection("regional.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("regional.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("filial.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("filial.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("pessoa.ID_PESSOA", "ID_PESSOA");
		sql.addProjection("pessoa.NM_PESSOA", "NM_PESSOA");
		sql.addProjection("pessoa.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("pessoa.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("servico.ID_SERVICO", "ID_SERVICO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("servico.DS_SERVICO_I"), "DS_SERVICO");
		sql.addProjection("divisaoC.ID_DIVISAO_CLIENTE", "ID_DIVISAO_CLIENTE");
		sql.addProjection("divisaoC.DS_DIVISAO_CLIENTE", "DS_DIVISAO_CLIENTE");
		sql.addProjection("tabelaP.ID_TABELA_PRECO", "ID_TABELA_PRECO");
		sql.addProjection("tabelaP.DT_VIGENCIA_INICIAL", "DT_VIGENCIA_INICIAL");
		sql.addProjection("tabelaP.DT_VIGENCIA_FINAL", "DT_VIGENCIA_FINAL");
		sql.addProjection("tpTabelaP.TP_TIPO_TABELA_PRECO", "TP_TIPO_TABELA_PRECO");
		sql.addProjection("tpTabelaP.NR_VERSAO", "NR_VERSAO");
		sql.addProjection("subTabelaP.TP_SUBTIPO_TABELA_PRECO", "TP_SUBTIPO_TABELA_PRECO");
		sql.addProjection("tabelaDivisaoC.ID_TABELA_DIVISAO_CLIENTE", "ID_TABELA_DIVISAO_CLIENTE");

		sql.addFrom("REGIONAL", "regional");
		sql.addFrom("FILIAL", "filial");
		sql.addFrom("PESSOA", "pessoa");
		sql.addFrom("V_SERVICO_I", "servico");
		sql.addFrom("DIVISAO_CLIENTE","divisaoC");
		sql.addFrom("TABELA_PRECO", "tabelaP");
		sql.addFrom("TIPO_TABELA_PRECO", "tpTabelaP");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "subTabelaP");
		sql.addFrom("TABELA_DIVISAO_CLIENTE","tabelaDivisaoC");
		sql.addFrom("CLIENTE","cliente");
		sql.addFrom("REGIONAL_FILIAL","regionalFilial");
		sql.addFrom("PARAMETRO_CLIENTE","pc");
		

		sql.addJoin("tabelaDivisaoC.ID_DIVISAO_CLIENTE","divisaoC.ID_DIVISAO_CLIENTE");
		sql.addJoin("tabelaDivisaoC.ID_TABELA_DIVISAO_CLIENTE","pc.ID_TABELA_DIVISAO_CLIENTE(+)");
		sql.addJoin("divisaoC.ID_CLIENTE","cliente.ID_CLIENTE");
		sql.addJoin("cliente.ID_CLIENTE","pessoa.ID_PESSOA");
		sql.addJoin("tabelaDivisaoC.ID_SERVICO","servico.ID_SERVICO");
		sql.addJoin("tabelaDivisaoC.ID_TABELA_PRECO","tabelaP.ID_TABELA_PRECO");
		sql.addJoin("tabelaP.ID_TIPO_TABELA_PRECO","tpTabelaP.ID_TIPO_TABELA_PRECO");
		sql.addJoin("tabelaP.ID_SUBTIPO_TABELA_PRECO","subTabelaP.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("cliente.ID_FILIAL_ATENDE_COMERCIAL","filial.ID_FILIAL");
		sql.addJoin("regionalFilial.ID_FILIAL","filial.ID_FILIAL");
		sql.addJoin("regionalFilial.ID_REGIONAL","regional.ID_REGIONAL");
		
		
		sql.addCriteria("tabelaDivisaoC.BL_ATUALIZACAO_AUTOMATICA","=","N");
		sql.addCriteria("regionalFilial.DT_VIGENCIA_INICIAL","<=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("regionalFilial.DT_VIGENCIA_FINAL",">=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("tabelaP.BL_EFETIVADA","=","S");
		sql.addCriteria("pc.TP_SITUACAO_PARAMETRO(+)","=","A");
		
		// cliente.tp_cliente <> F
		sql.addCriteria("cliente.TP_CLIENTE","<>",ConstantesVendas.CLIENTE_FILIAL);


		//filial.ID_FILIAL
		String filiais = this.getFiliaisUsuarioLogado();
		if(filiais != null) {
			sql.addCustomCriteria(filiais);
		}
		
		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("regional.ID_REGIONAL", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("cliente.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial");
			sql.addFilterSummary("filial", dsFilial);
		}

		String vlModal = parameters.getString("modal.valor");
		if(vlModal != null) {
			sql.addCriteria("servico.TP_MODAL","=",vlModal);
			String dsModal = parameters.getString("modal.descricao");
			sql.addFilterSummary("modal", dsModal);
		}

		String tpAbrangencia = parameters.getString("abrangencia.valor");
		if(tpAbrangencia != null) {
			sql.addCriteria("servico.TP_ABRANGENCIA","=",tpAbrangencia);
			String dsAbrangencia = parameters.getString("abrangencia.descricao");
			sql.addFilterSummary("abrangencia", dsAbrangencia);
		}

		YearMonthDay dtReferencia = parameters.getYearMonthDay("dataReferencia");
		if(dtReferencia != null) {
			sql.addCriteria("pc.DT_VIGENCIA_INICIAL(+)","<=",dtReferencia,YearMonthDay.class);
			sql.addCriteria("pc.DT_VIGENCIA_FINAL(+)",">=",dtReferencia,YearMonthDay.class);
			sql.addFilterSummary("dataReferencia", JTFormatUtils.format(dtReferencia));
		}

		sql.addOrderBy("regional.SG_REGIONAL");
		sql.addOrderBy("filial.SG_FILIAL");
		sql.addOrderBy("pessoa.NM_PESSOA");
		sql.addOrderBy("pessoa.NR_IDENTIFICACAO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("servico.DS_SERVICO_I"));
		sql.addOrderBy("divisaoC.DS_DIVISAO_CLIENTE");

		return sql;
	}
	
	private String getFiliaisUsuarioLogado() {
		List filiais = SessionUtils.getFiliaisUsuarioLogado();
		
		if(filiais != null && filiais.size() > 0) {
			StringBuilder sql = new StringBuilder();
			boolean first = true;
			for(Iterator it = filiais.iterator(); it.hasNext();) {
				Filial filial = (Filial) it.next();
				if(first) {
					first = false;
					sql.append("filial.ID_FILIAL in (").append(filial.getIdFilial());
				} else {
					sql.append(", ").append(filial.getIdFilial());
				}
			}
			sql.append(")");
			return sql.toString();
		}
		
		return null;
	}

}
