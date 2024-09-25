package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Éderson Frozi
 *
 * @spring.bean id="lms.expedicao.controleContaFreteService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirControleContasFrete.jasper"
 */
public class ControleContaFreteService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap)parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());

		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		SqlTemplate subSql = this.createSqlTemplate();
		subSql.addProjection("(select nfc.nr_nota_fiscal from nota_fiscal_conhecimento nfc" +
				"where nfc.id_conhecimento = c.id_conhecimento" +
				"and id_nota_fiscal_conhecimento = " +
				"(select min(id_nota_fiscal_conhecimento) from nota_fiscal_conhecimento nfc1 " +
				"where nfc1.id_conhecimento = nfc.id_conhecimento)"
		);
		
		// PROJEÇÃO
		sql.addProjection("c.nr_formulario", "formulario");
		sql.addProjection("ds.dh_emissao", "dh_emissao");
		sql.addProjection("fo.sg_filial", "sg_filial_origem");
		sql.addProjection("c.nr_conhecimento", "nr_conhecimento");
		sql.addProjection("c.dv_conhecimento", "dv_conhecimento");
		sql.addProjection("fd.sg_filial", "sg_filial_destino");
		sql.addProjection("p.nm_pessoa", "nm_cliente");
		sql.addProjection("p.tp_identificacao", "tp_identificacao");
		sql.addProjection("p.nr_identificacao", "nr_identificacao");
		sql.addProjection("pfo.nm_fantasia", "nm_fantasiaF");
		sql.addProjection("i.ds_check_in", "nm_impressora");
		sql.addProjection("(select nfc.nr_nota_fiscal from nota_fiscal_conhecimento nfc" +
				" where nfc.id_conhecimento = c.id_conhecimento" +
				" and id_nota_fiscal_conhecimento = " +
				"(select min(id_nota_fiscal_conhecimento) from nota_fiscal_conhecimento nfc1 " +
				" where nfc1.id_conhecimento = nfc.id_conhecimento))", "nr_nota_fiscal");

		// FROM
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("devedor_doc_serv", "dds");
		sql.addFrom("conhecimento", "c");
		sql.addFrom("impressora", "i");
		sql.addFrom("filial", "fo");
		sql.addFrom("filial", "fd");
		sql.addFrom("pessoa", "p");
		sql.addFrom("pessoa", "pfo");

		// FILTROS
		//Filial
		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("fo.id_filial", "=", idFilial);
			String dsFilial = parameters.getString("filial.dsFilial");
			sql.addFilterSummary("filial", dsFilial);
		}
		//Impressora
		Long idImpressora = parameters.getLong("impressora.idImpressora");
		if(idImpressora != null) {
			sql.addCriteria("ds.id_impressora", "=", idImpressora);
			String dsImpressora = parameters.getString("impressora.nmImpressora");
			sql.addFilterSummary("impressora", dsImpressora);
		}
		// Filtro de período
		DateTime dataInicial = parameters.getDateTime("dataInicial");
		if(dataInicial != null) {
			sql.addCriteria("dh_emissao", ">=", dataInicial, DateTime.class);
			sql.addFilterSummary("periodoInicial", JTFormatUtils.format(dataInicial, JTFormatUtils.MEDIUM));
		}
		DateTime dataFinal = parameters.getDateTime("dataFinal").plusSeconds(59);
		if(dataFinal != null) {
			sql.addCriteria("dh_emissao", "<=", dataFinal, DateTime.class);
			sql.addFilterSummary("periodoFinal", JTFormatUtils.format(dataFinal, JTFormatUtils.MEDIUM));
		}
		//Funcionário
		Long idFuncionario = parameters.getLong("usuario.idUsuario");
		if(idFuncionario != null) {
			sql.addCriteria("ds.id_usuario_inclusao", "=", idFuncionario);
			String dsFuncionario = parameters.getString("funcionario.codPessoa.nome");
			sql.addFilterSummary("funcionario", dsFuncionario);
		}
		//Cliente
		Long idCliente = parameters.getLong("clienteByIdCliente.idCliente");
		if(idCliente != null) {
			sql.addCriteria("dds.id_cliente", "=", idCliente);
			String dsCliente = parameters.getString("clienteByIdCliente.pessoa.nmPessoa");
			sql.addFilterSummary("cliente", dsCliente);
		}

		// Tipo de Frete = CIF
		sql.addCriteria("c.tp_frete", "=", "C", String.class);
		
		// Tipo de documento
		sql.addCriteria("c.tp_documento_servico", "=", ConstantesExpedicao.CONHECIMENTO_NACIONAL, String.class);
		
		// E=Emitido B=Bloqueado C=Cancelado
		sql.addCustomCriteria("tp_situacao_conhecimento in (?, ?, ?)");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("B");
		sql.addCriteriaValue("C");

		// JOIN
		sql.addJoin("fo.id_filial", "pfo.id_pessoa");
		sql.addJoin("ds.id_docto_servico", "c.id_conhecimento");
		sql.addJoin("ds.id_impressora", " i.id_impressora");
		sql.addJoin("ds.id_filial_origem", "fo.id_filial");
		sql.addJoin("ds.id_filial_destino", "fd.id_filial");
		sql.addJoin("ds.id_docto_servico", "dds.id_docto_servico");
		sql.addJoin("dds.id_cliente", "p.id_pessoa");

		String filiais = this.getFiliaisUsuarioLogado();
		if(filiais != null) {
			sql.addCustomCriteria(filiais);
		}

		// ORDENAÇÃO
		sql.addOrderBy("fo.sg_filial");
		sql.addOrderBy("i.ds_check_in");
		sql.addOrderBy("ds.dh_emissao");

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
					sql.append("ds.id_filial_origem in (").append(filial.getIdFilial());
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