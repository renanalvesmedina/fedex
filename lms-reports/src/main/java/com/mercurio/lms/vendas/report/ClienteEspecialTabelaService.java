package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.clienteEspecialTabelaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelacaoClientesEspeciaisTabelas.jasper"
 */
public class ClienteEspecialTabelaService extends ReportServiceSupport {
	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio.valor");
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("F.ID_FILIAL", "ID_FILIAL");
		sql.addProjection("F.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("R.ID_REGIONAL", "ID_REGIONAL");
		sql.addProjection("R.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("M.SG_MOEDA", "SG_MOEDA");
		sql.addProjection("M.DS_SIMBOLO", "DS_SIMBOLO");
		sql.addProjection("M.SG_MOEDA || ' ' || M.DS_SIMBOLO", "MOEDA");
		sql.addProjection("P.ID_PESSOA", "ID_PESSOA");
		sql.addProjection("P.NM_PESSOA", "NM_PESSOA");
		sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TL.DS_TIPO_LOGRADOURO_I"), "DS_TIPO_LOGRADOURO");
		sql.addProjection("P.TP_PESSOA", "TP_PESSOA");
		sql.addProjection("EP.ID_ENDERECO_PESSOA", "ID_ENDERECO_PES");
		sql.addProjection("EP.DS_ENDERECO", "DS_ENDERECO");
		sql.addProjection("EP.NR_ENDERECO", "NR_ENDERECO");
		sql.addProjection("EP.DS_COMPLEMENTO", "DS_COMPLEMENTO");
		sql.addProjection("EP.DS_BAIRRO", "DS_BAIRRO");
		sql.addProjection("MU.NM_MUNICIPIO", "NM_MUNICIPIO");
		sql.addProjection("UF.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA");
		sql.addProjection("DC.DS_DIVISAO_CLIENTE", "DS_DIVISAO_CLIENTE");
		sql.addProjection("TP.ID_TABELA_PRECO", "ID_TABELA_PRECO");
		sql.addProjection("TTP.TP_TIPO_TABELA_PRECO", "TP_TIPO_TABELA_PRECO");
		sql.addProjection("TTP.NR_VERSAO", "NR_VERSAO");
		sql.addProjection("STP.TP_SUBTIPO_TABELA_PRECO", "TP_SUBTIPO_TABELA_PRECO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VSI.DS_SERVICO_I"), "DS_SERVICO");
		sql.addProjection("TDC.ID_TABELA_DIVISAO_CLIENTE", "ID_TABELA_DIVISAO_CLIENTE");
		sql.addProjection("TDC.BL_ATUALIZACAO_AUTOMATICA", "BL_ATUALIZACAO_AUTOMATICA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VZIO.DS_ZONA_I"), "DS_ZONA_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VZID.DS_ZONA_I"), "DS_ZONA_DESTINO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VPIO.NM_PAIS_I"), "NM_PAIS_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VPID.NM_PAIS_I"), "NM_PAIS_DESTINO");
		sql.addProjection("UFO.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_ORIGEM");
		sql.addProjection("UFD.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_DESTINO");
		sql.addProjection("MUO.NM_MUNICIPIO", "NM_MUNICIPIO_ORIGEM");
		sql.addProjection("MUD.NM_MUNICIPIO", "NM_MUNICIPIO_DESTINO");
		sql.addProjection("FO.SG_FILIAL", "SG_FILIAL_ORIGEM");
		sql.addProjection("FD.SG_FILIAL", "SG_FILIAL_DESTINO");
		sql.addProjection("AO.SG_AEROPORTO", "SG_AEROPORTO_ORIGEM");
		sql.addProjection("AOP.NM_PESSOA", "NM_PESSOA_ORIGEM");
		sql.addProjection("AD.SG_AEROPORTO", "SG_AEROPORTO_DESTINO");
		sql.addProjection("ADP.NM_PESSOA", "NM_PESSOA_DESTINO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VTLMO.DS_TIPO_LOCAL_MUNICIPIO_I"), "DS_TP_LOC_MUNICIPIO_ORIGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VTLMD.DS_TIPO_LOCAL_MUNICIPIO_I"), "DS_TP_LOC_MUNICIPIO_DESTINO");
		sql.addProjection("PC.DT_VIGENCIA_INICIAL", "DT_VIGENCIA_INICIAL");
		sql.addProjection("PC.DT_VIGENCIA_FINAL", "DT_VIGENCIA_FINAL");
		sql.addProjection("PC.TP_INDICADOR_PERC_MINIMO_PROGR", "TP_INDICADOR_PERC_MINIMO_PROGR");
		sql.addProjection("PC.VL_PERC_MINIMO_PROGR", "VL_PERC_MINIMO_PROGR");
		sql.addProjection("PC.TP_INDICADOR_FRETE_PESO", "TP_INDICADOR_FRETE_PESO");
		sql.addProjection("PC.VL_FRETE_PESO", "VL_FRETE_PESO");
		sql.addProjection("PC.VL_MINIMO_FRETE_QUILO", "VL_MINIMO_FRETE_QUILO");
		sql.addProjection("PC.TP_TARIFA_MINIMA", "TP_TARIFA_MINIMA");
		sql.addProjection("PC.VL_TARIFA_MINIMA", "VL_TARIFA_MINIMA");
		sql.addProjection("PC.BL_PAGA_CUBAGEM", "BL_PAGA_CUBAGEM");
		sql.addProjection("PC.TP_INDIC_VLR_TBL_ESPECIFICA", "TP_INDIC_VLR_TBL_ESPECIFICA");
		sql.addProjection("PC.VL_TBL_ESPECIFICA", "VL_TBL_ESPECIFICA");
		sql.addProjection("PC.TP_INDICADOR_MIN_FRETE_PESO", "TP_INDICADOR_MIN_FRETE_PESO");
		sql.addProjection("PC.VL_MIN_FRETE_PESO", "VL_MIN_FRETE_PESO");
		sql.addProjection("PC.VL_FRETE_VOLUME", "VL_FRETE_VOLUME");
		sql.addProjection("PC.BL_PAGA_PESO_EXCEDENTE", "BL_PAGA_PESO_EXCEDENTE");
		sql.addProjection("PC.PC_PAGA_CUBAGEM", "PC_PAGA_CUBAGEM");
		sql.addProjection("PC.TP_INDICADOR_ADVALOREM", "TP_INDICADOR_ADVALOREM");
		sql.addProjection("PC.VL_ADVALOREM", "VL_ADVALOREM");
		sql.addProjection("PC.TP_INDICADOR_ADVALOREM_2", "TP_INDICADOR_ADVALOREM_2");
		sql.addProjection("PC.VL_ADVALOREM_2", "VL_ADVALOREM_2");
		sql.addProjection("PC.TP_INDICADOR_VALOR_REFERENCIA", "TP_INDICADOR_VALOR_REFERENCIA");
		sql.addProjection("PC.VL_VALOR_REFERENCIA", "VL_VALOR_REFERENCIA");
		sql.addProjection("PC.PC_FRETE_PERCENTUAL", "PC_FRETE_PERCENTUAL");
		sql.addProjection("PC.VL_MINIMO_FRETE_PERCENTUAL", "VL_MINIMO_FRETE_PERCENTUAL");
		sql.addProjection("PC.VL_TONELADA_FRETE_PERCENTUAL", "VL_TONELADA_FRETE_PERCENTUAL");
		sql.addProjection("PC.PS_FRETE_PERCENTUAL", "PS_FRETE_PERCENTUAL");
		sql.addProjection("PC.TP_INDICADOR_PERCENTUAL_GRIS", "TP_INDICADOR_PERCENTUAL_GRIS");
		sql.addProjection("PC.VL_PERCENTUAL_GRIS", "VL_PERCENTUAL_GRIS");
		sql.addProjection("PC.TP_INDICADOR_MINIMO_GRIS", "TP_INDICADOR_MINIMO_GRIS");
		sql.addProjection("PC.VL_MINIMO_GRIS", "VL_MINIMO_GRIS");

		sql.addProjection("PC.TP_INDICADOR_PERCENTUAL_TDE", "TP_INDICADOR_PERCENTUAL_TDE");
		sql.addProjection("PC.VL_PERCENTUAL_TDE", "VL_PERCENTUAL_TDE");
		sql.addProjection("PC.TP_INDICADOR_MINIMO_TDE", "TP_INDICADOR_MINIMO_TDE");
		sql.addProjection("PC.VL_MINIMO_TDE", "VL_MINIMO_TDE");

		sql.addProjection("PC.TP_INDICADOR_PEDAGIO", "TP_INDICADOR_PEDAGIO");
		sql.addProjection("PC.VL_PEDAGIO", "VL_PEDAGIO");
		sql.addProjection("PC.PC_DESCONTO_FRETE_TOTAL", "PC_DESCONTO_FRETE_TOTAL");
		sql.addProjection("PC.PC_COBRANCA_REENTREGA", "PC_COBRANCA_REENTREGA");
		sql.addProjection("PC.PC_COBRANCA_DEVOLUCOES", "PC_COBRANCA_DEVOLUCOES");
		sql.addProjection("VSI.ID_SERVICO", "ID_SERVICO");
		sql.addProjection("DC.ID_DIVISAO_CLIENTE", "ID_DIVISAO_CLIENTE");
		sql.addProjection("PC.ID_PARAMETRO_CLIENTE", "ID_PARAMETRO_CLIENTE");

		sql.addFrom("FILIAL", "F");
		sql.addFrom("REGIONAL", "R");
		sql.addFrom("REGIONAL_FILIAL", "RF");
		sql.addFrom("MOEDA", "M");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("TIPO_LOGRADOURO", "TL");
		sql.addFrom("ENDERECO_PESSOA", "EP");
		sql.addFrom("MUNICIPIO", "MU");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF");
		sql.addFrom("DIVISAO_CLIENTE", "DC");
		sql.addFrom("TABELA_PRECO", "TP");
		sql.addFrom("TIPO_TABELA_PRECO", "TTP");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "STP");
		sql.addFrom("V_SERVICO_I", "VSI");
		sql.addFrom("TABELA_DIVISAO_CLIENTE", "TDC");
		sql.addFrom("V_ZONA_I", "VZIO");
		sql.addFrom("V_ZONA_I", "VZID");
		sql.addFrom("V_PAIS_I", "VPIO");
		sql.addFrom("V_PAIS_I", "VPID");
		sql.addFrom("UNIDADE_FEDERATIVA", "UFO");
		sql.addFrom("UNIDADE_FEDERATIVA", "UFD");
		sql.addFrom("MUNICIPIO", "MUO");
		sql.addFrom("MUNICIPIO", "MUD");
		sql.addFrom("FILIAL", "FO");
		sql.addFrom("FILIAL", "FD");
		sql.addFrom("AEROPORTO", "AO");
		sql.addFrom("PESSOA", "AOP");
		sql.addFrom("AEROPORTO", "AD");
		sql.addFrom("PESSOA", "ADP");
		sql.addFrom("V_TIPO_LOCALIZACAO_MUNICIPIO_I", "VTLMO");
		sql.addFrom("V_TIPO_LOCALIZACAO_MUNICIPIO_I", "VTLMD");
		sql.addFrom("PARAMETRO_CLIENTE", "PC");
		sql.addJoin("TDC.ID_TABELA_DIVISAO_CLIENTE", "PC.ID_TABELA_DIVISAO_CLIENTE (+)");
		sql.addJoin("TDC.ID_TABELA_PRECO", "TP.ID_TABELA_PRECO");
		sql.addJoin("TTP.ID_TIPO_TABELA_PRECO", "TP.ID_TIPO_TABELA_PRECO");
		sql.addJoin("STP.ID_SUBTIPO_TABELA_PRECO", "TP.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("TDC.ID_DIVISAO_CLIENTE", "DC.ID_DIVISAO_CLIENTE");
		sql.addJoin("TP.ID_MOEDA", "M.ID_MOEDA");
		sql.addJoin("DC.ID_CLIENTE", "C.ID_CLIENTE");
		sql.addJoin("TDC.ID_SERVICO", "VSI.ID_SERVICO");
		sql.addJoin("C.ID_CLIENTE", "P.ID_PESSOA");
		sql.addJoin("EP.ID_ENDERECO_PESSOA", "P.ID_ENDERECO_PESSOA");
		sql.addJoin("EP.ID_TIPO_LOGRADOURO", "TL.ID_TIPO_LOGRADOURO");
		sql.addJoin("EP.ID_MUNICIPIO", "MU.ID_MUNICIPIO");
		sql.addJoin("MU.ID_UNIDADE_FEDERATIVA", "UF.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("C.ID_FILIAL_ATENDE_COMERCIAL", "F.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "RF.ID_FILIAL");
		sql.addJoin("RF.ID_REGIONAL", "R.ID_REGIONAL");
		sql.addJoin("PC.ID_ZONA_ORIGEM", "VZIO.ID_ZONA (+)");
		sql.addJoin("PC.ID_ZONA_DESTINO", "VZID.ID_ZONA (+)");
		sql.addJoin("PC.ID_PAIS_ORIGEM", "VPIO.ID_PAIS (+)");
		sql.addJoin("PC.ID_PAIS_DESTINO", "VPID.ID_PAIS (+)");
		sql.addJoin("PC.ID_UF_ORIGEM", "UFO.ID_UNIDADE_FEDERATIVA (+)");
		sql.addJoin("PC.ID_UF_DESTINO", "UFD.ID_UNIDADE_FEDERATIVA (+)");
		sql.addJoin("PC.ID_MUNICIPIO_ORIGEM", "MUO.ID_MUNICIPIO (+)");
		sql.addJoin("PC.ID_MUNICIPIO_DESTINO", "MUD.ID_MUNICIPIO (+)");
		sql.addJoin("PC.ID_FILIAL_ORIGEM", "FO.ID_FILIAL (+)");
		sql.addJoin("PC.ID_FILIAL_DESTINO", "FD.ID_FILIAL (+)");
		sql.addJoin("PC.ID_AEROPORTO_ORIGEM", "AO.ID_AEROPORTO (+)");
		sql.addJoin("AO.ID_AEROPORTO", "AOP.ID_PESSOA (+)");
		sql.addJoin("PC.ID_AEROPORTO_DESTINO", "AD.ID_AEROPORTO (+)");
		sql.addJoin("AD.ID_AEROPORTO", "ADP.ID_PESSOA (+)");
		sql.addJoin("PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM", "VTLMO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)");
		sql.addJoin("PC.ID_TIPO_LOC_MUNICIPIO_DESTINO", "VTLMD.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)");
		
		sql.addCriteria("PC.TP_SITUACAO_PARAMETRO (+)", "=", "A");
		sql.addCriteria("TP.BL_EFETIVADA", "=", "S");
		sql.addCriteria("C.TP_CLIENTE", "=", "S");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sql.addCriteria("RF.DT_VIGENCIA_INICIAL", "<=", dataAtual, YearMonthDay.class);
		sql.addCriteria("RF.DT_VIGENCIA_FINAL", ">=", dataAtual, YearMonthDay.class);

		String filiaisCriteria = this.createFiliaisCriteria();
		if(filiaisCriteria != null) {
			sql.addCustomCriteria(filiaisCriteria);
		}

		Long idTabelaPreco = parameters.getLong("tabelaPreco.idTabelaPreco");
		if(idTabelaPreco != null) {
			sql.addCriteria("TP.ID_TABELA_PRECO", "=", idTabelaPreco);
			String dsTabelaPreco = parameters.getString("tabelaPreco.tabelaPrecoStringHidden");
			sql.addFilterSummary("tabela", dsTabelaPreco);
		}

		YearMonthDay dataReferencia = parameters.getYearMonthDay("dataReferencia");
		if(dataReferencia != null) {
			sql.addCriteria("PC.DT_VIGENCIA_INICIAL (+)", "<=", dataReferencia, YearMonthDay.class);
			sql.addCriteria("PC.DT_VIGENCIA_FINAL (+)", ">=", dataReferencia, YearMonthDay.class);
			String dsDataReferencia = JTFormatUtils.format(dataReferencia);
			sql.addFilterSummary("dataReferencia", dsDataReferencia);
		}

		Long idFilial = parameters.getLong("filial.idFilial");
		if(idFilial != null) {
			sql.addCriteria("C.ID_FILIAL_ATENDE_COMERCIAL", "=", idFilial);
			String dsFilial = parameters.getString("filial.sgFilial") + " - " + parameters.getString("filial.pessoa.nmFantasia");
			sql.addFilterSummary("filial", dsFilial);
		}

		Long idCliente = parameters.getLong("cliente.idCliente");
		if(idCliente != null) {
			sql.addCriteria("C.ID_CLIENTE", "=", idCliente);
			String dsCliente = parameters.getString("cliente.pessoa.nmPessoa");
			sql.addFilterSummary("cliente", dsCliente);
		}
		
		String tpModal = parameters.getString("tpModal.valor");
		if(StringUtils.isNotBlank(tpModal)) {
			sql.addCriteria("VSI.TP_MODAL", "=", tpModal);
			String dsModal = parameters.getString("tpModal.descricao");
			sql.addFilterSummary("modal", dsModal);
		}
		
		String tpAbrangencia = parameters.getString("tpAbrangencia.valor");
		if(StringUtils.isNotBlank(tpAbrangencia)) {
			sql.addCriteria("VSI.TP_ABRANGENCIA", "=", tpAbrangencia);
			String dsAbrangencia = parameters.getString("tpAbrangencia.descricao");
			sql.addFilterSummary("abrangencia", dsAbrangencia);
		}
		
		sql.addOrderBy("F.SG_FILIAL");
		sql.addOrderBy("P.NM_PESSOA");
		sql.addOrderBy("P.NR_IDENTIFICACAO");
		sql.addOrderBy("DC.DS_DIVISAO_CLIENTE");
		sql.addOrderBy("TTP.TP_TIPO_TABELA_PRECO");
		sql.addOrderBy("TTP.NR_VERSAO");
		sql.addOrderBy("STP.TP_SUBTIPO_TABELA_PRECO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VSI.DS_SERVICO_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VZIO.DS_ZONA_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VPIO.NM_PAIS_I"));
		sql.addOrderBy("UFO.SG_UNIDADE_FEDERATIVA");
		sql.addOrderBy("FO.SG_FILIAL");
		sql.addOrderBy("MUO.NM_MUNICIPIO");
		sql.addOrderBy("AO.SG_AEROPORTO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VTLMO.DS_TIPO_LOCAL_MUNICIPIO_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VZID.DS_ZONA_I"));
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VPID.NM_PAIS_I"));
		sql.addOrderBy("UFD.SG_UNIDADE_FEDERATIVA");
		sql.addOrderBy("FD.SG_FILIAL");
		sql.addOrderBy("MUD.NM_MUNICIPIO");
		sql.addOrderBy("AD.SG_AEROPORTO");
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("VTLMD.DS_TIPO_LOCAL_MUNICIPIO_I"));
		
		return sql;
	}
	
	public JRDataSource executeGeneralidades(Object[] parameters) throws Exception {

		SqlTemplate sql = super.createSqlTemplate();

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"), "NM_PARCELA_PRECO");
		sql.addProjection("GC.TP_INDICADOR", "TP_INDICADOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VVDI.DS_VALOR_DOMINIO_I"), "DS_TP_INDICADOR");
		sql.addProjection("GC.VL_GENERALIDADE", "VL_GENERALIDADE");
		sql.addProjection("M.SG_MOEDA", "SG_MOEDA");
		sql.addProjection("M.DS_SIMBOLO", "DS_SIMBOLO");

		sql.addFrom("PARCELA_PRECO", "PP");
		sql.addFrom("GENERALIDADE_CLIENTE", "GC");
		sql.addFrom("DOMINIO", "VDI");
		sql.addFrom("VALOR_DOMINIO", "VVDI");
		sql.addFrom("MOEDA", "M");
		sql.addFrom("PARAMETRO_CLIENTE", "PC");
		sql.addFrom("TABELA_DIVISAO_CLIENTE", "TDC");
		sql.addFrom("TABELA_PRECO", "TP");

		sql.addJoin("GC.ID_PARCELA_PRECO", "PP.ID_PARCELA_PRECO");
		sql.addJoin("VDI.ID_DOMINIO", "VVDI.ID_DOMINIO");
		sql.addJoin("VVDI.VL_VALOR_DOMINIO", "GC.TP_INDICADOR");
		sql.addJoin("PC.ID_TABELA_DIVISAO_CLIENTE", "TDC.ID_TABELA_DIVISAO_CLIENTE");
		sql.addJoin("TDC.ID_TABELA_PRECO", "TP.ID_TABELA_PRECO");
		sql.addJoin("TP.ID_MOEDA", "M.ID_MOEDA");
		sql.addJoin("PC.ID_PARAMETRO_CLIENTE", "GC.ID_PARAMETRO_CLIENTE");

		sql.addCriteria("VDI.NM_DOMINIO", "=", "DM_INDICADOR_PARAMETRO_CLIENTE");

		Long idParametroCliente = (Long) parameters[0];
		if(idParametroCliente != null) {
			sql.addCriteria("GC.ID_PARAMETRO_CLIENTE", "=", idParametroCliente);
		} else {
			sql.addCriteria("GC.ID_PARAMETRO_CLIENTE", "=", LongUtils.ZERO);
		}

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"));
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	public JRDataSource executeTaxas(Object[] parameters) throws Exception {
		SqlTemplate sql = super.createSqlTemplate();
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"), "NM_PARCELA_PRECO");
		sql.addProjection("TC.TP_TAXA_INDICADOR", "TP_TAXA_INDICADOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VVDTI.DS_VALOR_DOMINIO_I"), "DS_TP_TAXA_INDICADOR");
		sql.addProjection("TC.VL_TAXA", "VL_TAXA");
		sql.addProjection("TC.PS_MINIMO", "PS_MINIMO");
		sql.addProjection("TC.VL_EXCEDENTE", "VL_EXCEDENTE");

		sql.addFrom("PARCELA_PRECO", "PP");
		sql.addFrom("TAXA_CLIENTE", "TC");
		sql.addFrom("DOMINIO", "VDTI");
		sql.addFrom("VALOR_DOMINIO", "VVDTI");
		sql.addJoin("PP.ID_PARCELA_PRECO", "TC.ID_PARCELA_PRECO");

		sql.addJoin("VDTI.ID_DOMINIO", "VVDTI.ID_DOMINIO");
		sql.addJoin("VVDTI.VL_VALOR_DOMINIO", "TC.TP_TAXA_INDICADOR");
		sql.addCriteria("VDTI.NM_DOMINIO",  "=",  "DM_INDICADOR_PARAMETRO_CLIENTE");

		Long idParametroCliente = (Long) parameters[0];
		if(idParametroCliente != null) {
			sql.addCriteria("TC.ID_PARAMETRO_CLIENTE", "=", idParametroCliente);
		} else {
			sql.addCriteria("TC.ID_PARAMETRO_CLIENTE", "=", LongUtils.ZERO);
		}

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"));
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public JRDataSource executeServicosAdicionais(Object[] parameters) throws Exception {

		SqlTemplate sql = super.createSqlTemplate();

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"), "NM_PARCELA_PRECO");
		sql.addProjection("SAC.TP_INDICADOR", "TP_INDICADOR");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VVDI.DS_VALOR_DOMINIO_I"), "DS_TP_INDICADOR");
		sql.addProjection("SAC.VL_VALOR", "VL_VALOR");
		sql.addProjection("NVL(SAC.NR_QUANTIDADE_DIAS, 0)", "NR_QUANTIDADE_DIAS");
		sql.addProjection("SAC.VL_MINIMO", "VL_MINIMO");
		sql.addProjection("M.SG_MOEDA || ' ' || M.DS_SIMBOLO", "MOEDA");

		sql.addFrom("PARCELA_PRECO", "PP");
		sql.addFrom("SERVICO_ADICIONAL_CLIENTE", "SAC");
		sql.addFrom("DOMINIO", "VDI");
		sql.addFrom("VALOR_DOMINIO", "VVDI");
		sql.addFrom("MOEDA", "M");
		sql.addFrom("TABELA_DIVISAO_CLIENTE", "TDC");
		sql.addFrom("TABELA_PRECO", "TP");

		sql.addJoin("SAC.ID_PARCELA_PRECO", "PP.ID_PARCELA_PRECO");
		sql.addJoin("VDI.ID_DOMINIO", "VVDI.ID_DOMINIO");
		sql.addJoin("VVDI.VL_VALOR_DOMINIO", "SAC.TP_INDICADOR");
		sql.addJoin("SAC.ID_TABELA_DIVISAO_CLIENTE", "TDC.ID_TABELA_DIVISAO_CLIENTE");
		sql.addJoin("TDC.ID_TABELA_PRECO", "TP.ID_TABELA_PRECO");
		sql.addJoin("TP.ID_MOEDA", "M.ID_MOEDA");
		sql.addJoin("TDC.ID_TABELA_DIVISAO_CLIENTE", "SAC.ID_TABELA_DIVISAO_CLIENTE");

		sql.addCriteria("VDI.NM_DOMINIO", "=", "DM_INDICADOR_PARAMETRO_CLIENTE");

		Long idTabelaDivisaoCliente = (Long) parameters[0];
		if(idTabelaDivisaoCliente != null) {
			sql.addCriteria("SAC.ID_TABELA_DIVISAO_CLIENTE", "=", idTabelaDivisaoCliente);
		}

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("PP.NM_PARCELA_PRECO_I"));

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
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
					sql.append("F.ID_FILIAL IN(").append(filial.getIdFilial());
				} else if(it.hasNext()) {
					sql.append(", ").append(filial.getIdFilial());
				}
			}

			if(!filiais.isEmpty())
				sql.append(")");

			return sql.toString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getTelefoneByIdEnderecoTpPessoa(Long idEnderecoPessoa, String tpPessoa) {
		String telefoneFormatado = null;
		if (idEnderecoPessoa != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM \n");
			sql.append("(SELECT te.nr_telefone, te.nr_ddd, te.nr_ddI \n");
			sql.append("FROM TELEFONE_ENDERECO te \n");
			sql.append("WHERE 1=1 \n");
			sql.append("AND te.TP_USO = 'FO' \n");
			sql.append("AND te.ID_ENDERECO_PESSOA = ? \n");
			sql.append("AND te.TP_TELEFONE = ? \n");
			sql.append("ORDER BY te.id_telefone_endereco) \n");
			sql.append("WHERE rownum = 1");

			List<Map<String, String>> listaTelefoneMap = getJdbcTemplate().queryForList(sql.toString(), new Object[] {idEnderecoPessoa, "F".equals(tpPessoa) ? "R" : "C"});

			if (listaTelefoneMap != null && !listaTelefoneMap.isEmpty()) {
				Map<String, String> telefoneMap = listaTelefoneMap.get(0);
				telefoneFormatado = FormatUtils.formatTelefone(telefoneMap.get("nr_telefone"), telefoneMap.get("nr_ddd"), telefoneMap.get("nr_ddI"));
			}
		}
		return telefoneFormatado;
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#configReportDomains(com.mercurio.adsm.framework.report.ReportServiceSupport.ReportDomainConfig)
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("BL_ATUALIZACAO_AUTOMATICA", "DM_SIM_NAO");
		config.configDomainField("BL_PAGA_CUBAGEM", "DM_SIM_NAO");
		config.configDomainField("BL_PAGA_PESO_EXCEDENTE", "DM_SIM_NAO");
		config.configDomainField("TP_INDICADOR_PERC_MINIMO_PROGR", "DM_ACRESCIMO_DESCONTO");
		config.configDomainField("TP_INDICADOR_MIN_FRETE_PESO", "DM_INDICADOR_FRETE_MINIMO");
		config.configDomainField("TP_INDICADOR_FRETE_PESO", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_INDIC_VLR_TBL_ESPECIFICA", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_INDICADOR_ADVALOREM", "DM_INDICADOR_ADVALOREM");
		config.configDomainField("TP_INDICADOR_ADVALOREM_2", "DM_INDICADOR_ADVALOREM");
		config.configDomainField("TP_INDICADOR_VALOR_REFERENCIA", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_INDICADOR_PERCENTUAL_GRIS", "DM_INDICADOR_ADVALOREM");
		config.configDomainField("TP_INDICADOR_MINIMO_GRIS", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_INDICADOR_PERCENTUAL_TDE", "DM_INDICADOR_ADVALOREM");
		config.configDomainField("TP_INDICADOR_MINIMO_TDE", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_TARIFA_MINIMA", "DM_INDICADOR_PARAMETRO_CLIENTE");
		config.configDomainField("TP_INDICADOR_PEDAGIO", "DM_INDICADOR_PEDAGIO");
	}
}