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
 * @spring.bean id="lms.vendas.clienteDescontoTotalFreteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelClientesDescontoTotalFrete.jasper"
 */
public class ClienteDescontoTotalFreteService extends ReportServiceSupport {

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
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VVDSN.DS_VALOR_DOMINIO_I"), "BL_ATUALIZACAO_AUTOMATICA");
		sql.addProjection("paramCliente.ID_PARAMETRO_CLIENTE", "ID_PARAMETRO_CLIENTE");
		sql.addProjection("paramCliente.PC_DESCONTO_FRETE_TOTAL", "PC_DESCONTO_FRETE_TOTAL");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("zonaO.DS_ZONA_I"), "DS_ZONA_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("zonaD.DS_ZONA_I"), "DS_ZONA_DESTINO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("paisO.NM_PAIS_I"), "NM_PAIS_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("paisD.NM_PAIS_I"), "NM_PAIS_DESTINO");
		sql.addProjection("ufO.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_ORIGEM");
		sql.addProjection("ufD.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_DESTINO");
		sql.addProjection("municipioO.NM_MUNICIPIO", "NM_MUNICIPIO_ORIGEM");
		sql.addProjection("municipioD.NM_MUNICIPIO", "NM_MUNICIPIO_DESTINO");
		sql.addProjection("filialO.SG_FILIAL", "SG_FILIAL_ORIGEM");
		sql.addProjection("filialD.SG_FILIAL", "SG_FILIAL_DESTINO");
		sql.addProjection("aeroportoO.SG_AEROPORTO", "SG_AEROPORTO_ORIGEM");
		sql.addProjection("aeroportoD.SG_AEROPORTO", "SG_AEROPORTO_DESTINO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tpLocalMunicipioO.DS_TIPO_LOCAL_MUNICIPIO_I"), "DS_TP_LOCAL_MUNICIPIO_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tpLocalMunicipioD.DS_TIPO_LOCAL_MUNICIPIO_I"), "DS_TP_LOCAL_MUNICIPIO_DESTINO");
		
		
		sql.addFrom("REGIONAL", "regional");
		sql.addFrom("FILIAL", "filial");
		sql.addFrom("PESSOA", "pessoa");
		sql.addFrom("SERVICO", "servico");
		sql.addFrom("DIVISAO_CLIENTE","divisaoC");
		sql.addFrom("TABELA_PRECO", "tabelaP");
		sql.addFrom("TIPO_TABELA_PRECO", "tpTabelaP");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "subTabelaP");
		sql.addFrom("TABELA_DIVISAO_CLIENTE","tabelaDivisaoC");
		sql.addFrom("CLIENTE","cliente");
		sql.addFrom("REGIONAL_FILIAL","regionalFilial");
		sql.addFrom("PARAMETRO_CLIENTE","paramCliente");
		
		sql.addFrom("ZONA","zonaO");
		sql.addFrom("ZONA","zonaD");
		sql.addFrom("PAIS","paisO");
		sql.addFrom("PAIS","paisD");
		sql.addFrom("UNIDADE_FEDERATIVA","ufO");
		sql.addFrom("UNIDADE_FEDERATIVA","ufD");
		sql.addFrom("MUNICIPIO","municipioO");
		sql.addFrom("MUNICIPIO","municipioD");
		sql.addFrom("FILIAL", "filialO");
		sql.addFrom("FILIAL", "filialD");
		sql.addFrom("AEROPORTO","aeroportoO");
		sql.addFrom("AEROPORTO","aeroportoD");
		sql.addFrom("TIPO_LOCALIZACAO_MUNICIPIO","tpLocalMunicipioO");
		sql.addFrom("TIPO_LOCALIZACAO_MUNICIPIO","tpLocalMunicipioD");
		
		sql.addFrom("DOMINIO", "VDSN");
		sql.addFrom("VALOR_DOMINIO", "VVDSN");



		sql.addJoin("paramCliente.ID_TABELA_DIVISAO_CLIENTE","tabelaDivisaoC.ID_TABELA_DIVISAO_CLIENTE");
		sql.addJoin("tabelaDivisaoC.ID_DIVISAO_CLIENTE","divisaoC.ID_DIVISAO_CLIENTE");
		sql.addJoin("divisaoC.ID_CLIENTE","cliente.ID_CLIENTE");
		sql.addJoin("cliente.ID_CLIENTE","PESSOA.ID_PESSOA");
		sql.addJoin("tabelaDivisaoC.ID_SERVICO","servico.ID_SERVICO");
		sql.addJoin("tabelaDivisaoC.ID_TABELA_PRECO","tabelaP.ID_TABELA_PRECO");
		sql.addJoin("tabelaP.ID_TIPO_TABELA_PRECO","tpTabelaP.ID_TIPO_TABELA_PRECO");
		sql.addJoin("tabelaP.ID_SUBTIPO_TABELA_PRECO","subTabelaP.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("cliente.ID_FILIAL_ATENDE_COMERCIAL","filial.ID_FILIAL");
		sql.addJoin("regionalFilial.ID_FILIAL","filial.ID_FILIAL");
		sql.addJoin("regionalFilial.ID_REGIONAL","regional.ID_REGIONAL");
		sql.addJoin("paramCliente.ID_ZONA_ORIGEM","zonaO.ID_ZONA");
		sql.addJoin("paramCliente.ID_ZONA_DESTINO","zonaD.ID_ZONA");
		sql.addJoin("paramCliente.ID_PAIS_ORIGEM","paisO.ID_PAIS(+)");
		sql.addJoin("paramCliente.ID_PAIS_DESTINO","paisD.ID_PAIS(+)");
		sql.addJoin("paramCliente.ID_UF_ORIGEM","ufO.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("paramCliente.ID_UF_DESTINO","ufD.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("paramCliente.ID_MUNICIPIO_ORIGEM","municipioO.ID_MUNICIPIO(+)");
		sql.addJoin("paramCliente.ID_MUNICIPIO_DESTINO","municipioD.ID_MUNICIPIO(+)");
		sql.addJoin("paramCliente.ID_FILIAL_ORIGEM","filialO.ID_FILIAL(+)");
		sql.addJoin("paramCliente.ID_FILIAL_DESTINO","filialD.ID_FILIAL(+)");
		sql.addJoin("paramCliente.ID_AEROPORTO_ORIGEM","aeroportoO.ID_AEROPORTO(+)");
		sql.addJoin("paramCliente.ID_AEROPORTO_DESTINO","aeroportoD.ID_AEROPORTO(+)");
		sql.addJoin("paramCliente.ID_TIPO_LOC_MUNICIPIO_ORIGEM","tpLocalMunicipioO.ID_TIPO_LOCALIZACAO_MUNICIPIO(+)");
		sql.addJoin("paramCliente.ID_TIPO_LOC_MUNICIPIO_DESTINO","tpLocalMunicipioD.ID_TIPO_LOCALIZACAO_MUNICIPIO(+)");
		
		sql.addJoin("VDSN.ID_DOMINIO", "VVDSN.ID_DOMINIO");
		sql.addJoin("VVDSN.VL_VALOR_DOMINIO", "tabelaDivisaoC.BL_ATUALIZACAO_AUTOMATICA");

		
		sql.addCriteria("paramCliente.PC_DESCONTO_FRETE_TOTAL",">","0");
		sql.addCriteria("regionalFilial.DT_VIGENCIA_INICIAL","<=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("regionalFilial.DT_VIGENCIA_FINAL",">=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("paramCliente.TP_SITUACAO_PARAMETRO","=","A");
		sql.addCriteria("tabelaP.BL_EFETIVADA","=","S");
		sql.addCriteria("VDSN.NM_DOMINIO", "=", "DM_SIM_NAO");

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
			sql.addCriteria("paramCliente.DT_VIGENCIA_INICIAL","<=",dtReferencia,YearMonthDay.class);
			sql.addCriteria("paramCliente.DT_VIGENCIA_FINAL",">=",dtReferencia,YearMonthDay.class);
			sql.addFilterSummary("dataReferencia", JTFormatUtils.format(dtReferencia));
		}

		sql.addOrderBy("regional.SG_REGIONAL");
		sql.addOrderBy("filial.SG_FILIAL");
		sql.addOrderBy("pessoa.NM_PESSOA");
		sql.addOrderBy("pessoa.NR_IDENTIFICACAO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("servico.DS_SERVICO_I"));
		sql.addOrderBy("divisaoC.DS_DIVISAO_CLIENTE");
		sql.addOrderBy("tpTabelaP.TP_TIPO_TABELA_PRECO");
		sql.addOrderBy("tpTabelaP.NR_VERSAO");
		sql.addOrderBy("subTabelaP.TP_SUBTIPO_TABELA_PRECO");

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("zonaO.DS_ZONA_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("paisO.NM_PAIS_I"));
		sql.addOrderBy("filialO.SG_FILIAL");
		sql.addOrderBy("municipioO.NM_MUNICIPIO");
		sql.addOrderBy("aeroportoO.SG_AEROPORTO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("tpLocalMunicipioO.DS_TIPO_LOCAL_MUNICIPIO_I"));

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("zonaD.DS_ZONA_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("paisD.NM_PAIS_I"));
		sql.addOrderBy("filialD.SG_FILIAL");
		sql.addOrderBy("municipioD.NM_MUNICIPIO");
		sql.addOrderBy("aeroportoD.SG_AEROPORTO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("tpLocalMunicipioD.DS_TIPO_LOCAL_MUNICIPIO_I"));

		
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
