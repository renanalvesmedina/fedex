package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * @author Claiton Grings
 * 
 * @spring.bean id="lms.vendas.clienteTabelaService"
 * @spring.property name="reportName"
 *                  value="com/mercurio/lms/vendas/report/emitirRelatorioClientesTabela.jasper"
 */
public class ClienteTabelaService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		this.verifyParameters(parameters);

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);

		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private void verifyParameters(Map parameters) throws Exception {
		String filial = (String) parameters.get("filial.idFilial");
		String uf = (String) parameters.get("uf.idUnidadeFederativa");

		if (StringUtils.isNotBlank(filial) && StringUtils.isNotBlank(uf)){
			throw new BusinessException("LMS-30031");
		}
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.setDistinct();
		
		sql.addProjection("tabela_preco.id_tabela_preco", "id_tabela_preco");
		sql.addProjection("tipo_tabela_preco.tp_tipo_tabela_preco", "tp_tipo_tabela_preco");
		sql.addProjection("tipo_tabela_preco.nr_versao", "nr_versao");
		sql.addProjection("subtipo_tabela_preco.tp_subtipo_tabela_preco", "tp_subtipo_tabela_preco");
		sql.addProjection("filial.id_filial", "id_filial");
		sql.addProjection("filial.sg_filial", "sg_filial");
		sql.addProjection("uf_filial.sg_unidade_federativa", "sg_uf_filial");
		sql.addProjection("cliente.id_cliente", "id_cliente");
		sql.addProjection("pessoa_cliente.nm_pessoa", "nm_pessoa");
		sql.addProjection("pessoa_cliente.tp_identificacao", "tp_identificacao");
		sql.addProjection("pessoa_cliente.nr_identificacao", "nr_identificacao");
		sql.addProjection("municipio_cliente.nm_municipio", "nm_municipio");
		sql.addProjection("uf_cliente.sg_unidade_federativa", "sg_uf_cliente");
		sql.addProjection("tipo_tabela_preco.tp_tipo_tabela_preco || tipo_tabela_preco.nr_versao || '-' || subtipo_tabela_preco.tp_subtipo_tabela_preco", "tabela");
		sql.addProjection("regional.sg_regional", "sg_regional");
		
		sql.addFrom("tabela_preco", "tabela_preco");
		sql.addFrom("tipo_tabela_preco", "tipo_tabela_preco");
		sql.addFrom("subtipo_tabela_preco", "subtipo_tabela_preco");
		sql.addFrom("filial", "filial");
		sql.addFrom("unidade_federativa", "uf_filial");
		sql.addFrom("cliente", "cliente");
		sql.addFrom("pessoa", "pessoa_cliente");
		sql.addFrom("pessoa", "pessoa_filial");
		sql.addFrom("municipio", "municipio_cliente");
		sql.addFrom("municipio", "municipio_filial");
		sql.addFrom("unidade_federativa", "uf_cliente");
		sql.addFrom("parametro_cliente", "parametro_cliente");
		sql.addFrom("tabela_divisao_cliente", "tabela_divisao_cliente");
		sql.addFrom("endereco_pessoa", "ep_cliente");
		sql.addFrom("endereco_pessoa", "ep_filial");
		sql.addFrom("regional", "regional");
		sql.addFrom("regional_filial", "regional_filial");
		sql.addFrom("divisao_cliente", "divisao_cliente");

		sql.addCustomCriteria("parametro_cliente.id_tabela_divisao_cliente (+) = tabela_divisao_cliente.id_tabela_divisao_cliente");
		sql.addCustomCriteria("tabela_divisao_cliente.id_tabela_preco = tabela_preco.id_tabela_preco");
		sql.addCustomCriteria("tipo_tabela_preco.id_tipo_tabela_preco = tabela_preco.id_tipo_tabela_preco");
		sql.addCustomCriteria("subtipo_tabela_preco.id_subtipo_tabela_preco = tabela_preco.id_subtipo_tabela_preco");
		sql.addCustomCriteria("tabela_divisao_cliente.id_divisao_cliente = divisao_cliente.id_divisao_cliente");
		sql.addCustomCriteria("divisao_cliente.id_cliente = cliente.id_cliente"); // fim da primeira leva de joins da ET
		sql.addCustomCriteria("cliente.id_cliente = pessoa_cliente.id_pessoa");
		sql.addCustomCriteria("ep_cliente.id_endereco_pessoa = pessoa_cliente.id_endereco_pessoa");
		sql.addCustomCriteria("ep_cliente.id_municipio = municipio_cliente.id_municipio");
		sql.addCustomCriteria("municipio_cliente.id_unidade_federativa = uf_cliente.id_unidade_federativa"); // fim da segunda leva de joins da ET
		sql.addCustomCriteria("cliente.id_filial_atende_comercial = filial.id_filial");
		sql.addCustomCriteria("filial.id_filial = pessoa_filial.id_pessoa");
		sql.addCustomCriteria("pessoa_filial.id_endereco_pessoa = ep_filial.id_endereco_pessoa");
		sql.addCustomCriteria("ep_filial.id_municipio = municipio_filial.id_municipio");
		sql.addCustomCriteria("municipio_filial.id_unidade_federativa = uf_filial.id_unidade_federativa"); // fim da terceira leva de joins da ET
		sql.addCustomCriteria("filial.id_filial = regional_filial.id_filial");
		sql.addCustomCriteria("regional_filial.id_regional = regional.id_regional"); // fim da quarta leva de joins da ET
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("parametro_cliente.dt_vigencia_inicial", "(+) <=", dataAtual, YearMonthDay.class);
		sql.addCriteria("parametro_cliente.dt_vigencia_final", "(+) >=", dataAtual, YearMonthDay.class);
		
		sql.addCriteria("parametro_cliente.tp_situacao_parametro", "(+) =", "A"); // ativo
		sql.addCriteria("tabela_preco.bl_efetivada", "=", "S"); // tabela efetivada
		
		sql.addCriteria("regional_filial.dt_vigencia_inicial", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("regional_filial.dt_vigencia_final", ">=", dataAtual, YearMonthDay.class);
		
		// cliente.tp_cliente <> F
		sql.addCriteria("cliente.TP_CLIENTE","<>",ConstantesVendas.CLIENTE_FILIAL);

		
		Long idTabelaPreco = parameters.getLong("tabelaPreco.idTabelaPreco");
		if(idTabelaPreco != null) {
			sql.addCriteria("tabela_preco.id_tabela_preco", "=", idTabelaPreco);
			String dsTabelaPreco = parameters.getString("tabelaPreco.tabelaPrecoStringHidden");
			sql.addFilterSummary("tabela", dsTabelaPreco);
		}
		
		Long idRegional = parameters.getLong("regional.idRegional");
		if(idRegional != null) {
			sql.addCriteria("regional.id_regional", "=", idRegional);
			String dsRegional = parameters.getString("regional.siglaDescricao");
			sql.addFilterSummary("regional", dsRegional);
		}
		
		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("cliente.id_filial_atende_comercial", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilialHidden");
			sql.addFilterSummary("filial", dsFilial);
		} else {
			sql.addCustomCriteria("cliente.id_filial_atende_comercial in "+SQLUtils.joinExpressionsWithComma(getIdsFiliais()));
		}
		
		Long idPais = parameters.getLong("pais.idPais");
		if(idPais != null) {
			sql.addCriteria("uf_cliente.id_pais", "=", idPais);
			String dsPais = parameters.getString("pais.nmPaisHidden");
			sql.addFilterSummary("pais", dsPais);
		}
		
		Long idUf = parameters.getLong("uf.idUnidadeFederativa");
		if(idUf != null) {
			sql.addCriteria("uf_cliente.id_unidade_federativa", "=", idUf);
			String dsUf = parameters.getString("uf.sgUnidadeFederativa");
			sql.addFilterSummary("uf", dsUf);
		}
		
		sql.addOrderBy("tipo_tabela_preco.tp_tipo_tabela_preco");
		sql.addOrderBy("tipo_tabela_preco.nr_versao");
		sql.addOrderBy("subtipo_tabela_preco.tp_subtipo_tabela_preco");
		sql.addOrderBy("filial.sg_filial");
		sql.addOrderBy("pessoa_cliente.nm_pessoa");

		return sql;
	}
	
	private List<Long> getIdsFiliais() {
		List<Long> result = new ArrayList<Long>();
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		for (Filial filial : filiais) {
			result.add(filial.getIdFilial());
		}
		return result;
	}

}
