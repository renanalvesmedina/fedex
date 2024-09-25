package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * 
 * @author Rafael Fernando Kohlrausch <rafaelfernando@cwi.com.br>
 * 
 * @spring.bean id="lms.expedicao.report.emitirMVNParaMDFeService"
 * @spring.property name="reportName"
 *                  value="com/mercurio/lms/expedicao/report/emitirMVNParaMDFe.jasper"
 */

public class EmitirMVNParaMDFeService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ApoliceSeguroService apoliceSeguroService;

	public JRReportDataObject execute(Map criteria) throws Exception {

		manifestoViagemNacionalService.flushModeParaManual();

		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);

		manifestoViagemNacionalService.flushModeParaAuto();

		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {

		SqlTemplate sql = this.createSqlTemplate();

		/** SELECT */

		/*
		 * Cabeçalho.
		 */
		sql.addProjection("MVN.ID_MANIFESTO_VIAGEM_NACIONAL", "ID_MANIFESTO_VIAGEM_NACIONAL");
		sql.addProjection("MAN.ID_FILIAL_ORIGEM", "MAN_ID_FILIAL_ORIGEM");
		
		
		sql.addProjection("PE_FL_ORIG.NM_PESSOA", "NM_PESSOA_FILIAL");
		sql.addProjection(
				PropertyVarcharI18nProjection.createProjection("TP_END_PE_FL_ORIG.DS_TIPO_LOGRADOURO_I")
						+ "|| ' ' || END_PE_FL_ORIG.DS_ENDERECO || ', ' || END_PE_FL_ORIG.NR_ENDERECO || CASE WHEN END_PE_FL_ORIG.DS_COMPLEMENTO IS NOT NULL THEN  ' - ' || END_PE_FL_ORIG.DS_COMPLEMENTO END",
				"END_PE_FILIAL_COMP");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TP_END_PE_FL_ORIG.DS_TIPO_LOGRADOURO_I"), "DS_TP_LOG_PE_FILIAL");
		sql.addProjection("END_PE_FL_ORIG.DS_ENDERECO", "DS_END_PE_FILIAL");
		sql.addProjection("END_PE_FL_ORIG.NR_ENDERECO", "NR_END_PE_FILIAL");
		sql.addProjection("END_PE_FL_ORIG.DS_COMPLEMENTO", "DS_COMP_PE_FILIAL");
		sql.addProjection("MUN_PE_FL_ORIG.NM_MUNICIPIO", "NM_MUN_PE_FILIAL");
		sql.addProjection("UF_PE_FL_ORIG.SG_UNIDADE_FEDERATIVA", "SG_UF_PE_FILIAL");
		sql.addProjection("FL_ORIG.SG_FILIAL", "SG_FL_ORIG");
		sql.addProjection("MVN.NR_MANIFESTO_ORIGEM", "NR_MAN_ORIGEM");
		sql.addProjection("FL_DEST.SG_FILIAL", "SG_FL_DEST");
		sql.addProjection("MAN.DH_EMISSAO_MANIFESTO", "DH_EMISSAO_MAN");

		/*
		 * Proprietário.
		 */
		sql.addProjection("PE_PROP.TP_IDENTIFICACAO", "PE_PROP_TP_IDENT");
		sql.addProjection("PE_PROP.NR_IDENTIFICACAO", "PE_PROP_NR_IDENT");
		sql.addProjection("PE_PROP.NM_PESSOA", "PE_PROP_NM_PESSOA");
		sql.addProjection(
				PropertyVarcharI18nProjection.createProjection("TP_END_PE_PROP.DS_TIPO_LOGRADOURO_I")
						+ "|| ' ' || END_PE_PROP.DS_ENDERECO || ', ' || END_PE_PROP.NR_ENDERECO || CASE WHEN END_PE_PROP.DS_COMPLEMENTO IS NOT NULL THEN  ' - ' || END_PE_PROP.DS_COMPLEMENTO END",
				"END_PE_PROP_COMP");
		sql.addProjection("MUN_PE_PROP.NM_MUNICIPIO", "NM_MUN_PE_PROP");
		sql.addProjection("UF_PE_PROP.SG_UNIDADE_FEDERATIVA", "SG_UF_PE_PROP");
		sql.addProjection("PAIS_PE_PROP.SG_PAIS", "SG_PAIS_PE_PROP");
		sql.addProjection("END_PE_PROP.NR_CEP", "NR_CEP_PE_PROP");

		/*
		 * Controle de carga.
		 */
		sql.addProjection("FILIAL_CC.SG_FILIAL", "SG_FILIAL_CC");
		sql.addProjection("CC.NR_CONTROLE_CARGA", "CC_NR_CONTROLE_CARGA");

		/*
		 * Motorista.
		 */
		sql.addProjection("MOT.NR_CARTEIRA_HABILITACAO", "MOT_NR_CART_HAB");
		sql.addProjection("MOT.NR_PRONTUARIO", "MOT_NR_PRONT");
		sql.addProjection("PE_MOT.TP_IDENTIFICACAO", "PE_MOT_TP_IDENT");
		sql.addProjection("PE_MOT.NR_IDENTIFICACAO", "PE_MOT_NR_IDENT");
		sql.addProjection("PE_MOT.NM_PESSOA", "PE_MOT_NM_PESSOA");
		sql.addProjection(
				PropertyVarcharI18nProjection.createProjection("TP_END_PE_MOT.DS_TIPO_LOGRADOURO_I")
						+ "|| ' ' || END_PE_MOT.DS_ENDERECO || ', ' || END_PE_MOT.NR_ENDERECO || CASE WHEN END_PE_MOT.DS_COMPLEMENTO IS NOT NULL THEN  ' - ' || END_PE_MOT.DS_COMPLEMENTO END",
				"END_PE_MOT_COMP");
		sql.addProjection("MUN_PE_MOT.NM_MUNICIPIO", "NM_MUN_PE_MOT");
		sql.addProjection("UF_PE_MOT.SG_UNIDADE_FEDERATIVA", "SG_UF_PE_MOT");
		sql.addProjection("PAIS_PE_MOT.SG_PAIS", "SG_PAIS_PE_MOT");
		sql.addProjection("END_PE_MOT.NR_CEP", "NR_CEP_PE_MOT");

		/*
		 * Veículo.
		 */
		sql.addProjection("ME_TP.NR_IDENTIFICADOR", "ME_TP_NR_IDENT");
		sql.addProjection("ME_TP.NR_FROTA", "ME_TP_NR_FROTA");
		sql.addProjection("MUN_ME_TP_RO.NM_MUNICIPIO", "MUN_ME_TP_RO_NM_MUN");
		sql.addProjection("UF_ME_TP_RO.SG_UNIDADE_FEDERATIVA", "UF_ME_TP_RO_SG_UNI_FE");
		sql.addProjection("MA_ME_TP.DS_MARCA_MEIO_TRANSPORTE", "MA_ME_TP_DS_MA_ME_TP");
		sql.addProjection("MO_ME_TP.DS_MODELO_MEIO_TRANSPORTE", "MO_ME_TP_DS_MO_ME_TP");
		sql.addProjection("ME_TP.NR_ANO_FABRICAO", "ME_TP_NR_ANO_FAB");
		sql.addProjection("ME_TP.NR_CAPACIDADE_KG", "ME_TP_NR_CAP_KG");
		sql.addProjection("ME_TP.NR_CAPACIDADE_M3", "ME_TP_NR_CAP_M3");

		/*
		 * Semi reboque.
		 */
		sql.addProjection("ME_TP_SR.NR_IDENTIFICADOR", "ME_TP_SR_NR_IDENT");
		sql.addProjection("ME_TP_SR.NR_FROTA", "ME_TP_SR_NR_FROTA");
		sql.addProjection("MUN_ME_TP_RO_SR.NM_MUNICIPIO", "MUN_ME_TP_RO_SR_NM_MUN");
		sql.addProjection("UF_ME_TP_RO_SR.SG_UNIDADE_FEDERATIVA", "UF_ME_TP_RO_SR_SG_UNI_FE");
		sql.addProjection("MA_ME_TP_SR.DS_MARCA_MEIO_TRANSPORTE", "MA_ME_TP_SR_DS_MA_ME_TP");
		sql.addProjection("MO_ME_TP_SR.DS_MODELO_MEIO_TRANSPORTE", "MO_ME_TP_SR_DS_MO_ME_TP");
		sql.addProjection("ME_TP_SR.NR_ANO_FABRICAO", "ME_TP_SR_NR_ANO_FAB");
		sql.addProjection("ME_TP_SR.NR_CAPACIDADE_KG", "ME_TP_SR_NR_CAP_KG");
		sql.addProjection("ME_TP_SR.NR_CAPACIDADE_M3", "ME_TP_SR_NR_CAP_M3");

		/*
		 * Totais gerais.
		 */
		sql.addProjection("MAN.VL_TOTAL_FRETE_CIF_EMISSAO", "VL_TOTAL_FRETE_CIF_EMISSAO");
		sql.addProjection("MAN.VL_TOTAL_FRETE_FOB_EMISSAO", "VL_TOTAL_FRETE_FOB_EMISSAO");
		sql.addProjection("MAN.VL_TOTAL_MANIFESTO_EMISSAO", "VL_TOTAL_MANIFESTO_EMISSAO");
		sql.addProjection("MAN.QT_TOTAL_VOLUMES_EMISSAO", "QT_TOTAL_VOLUMES_EMISSAO");
		sql.addProjection("MAN.PS_TOTAL_MANIFESTO_EMISSAO", "PS_TOTAL_MANIFESTO_EMISSAO");
		sql.addProjection("MAN.VL_TOTAL_FRETE_EMISSAO", "VL_TOTAL_FRETE_EMISSAO");

		/*
		 * Obs.
		 */
		sql.addProjection("MVN.OB_MANIFESTO_VIAGEM_NACIONAL", "MVN_OB_MAN_VIAG_NAC");

		
		/*
		 * Contador de cargas embarcadas.
		 */
		sql.addProjection("(SELECT COUNT(*) FROM MANIFESTO_NACIONAL_CTO M_CTO, DOCTO_SERVICO DO_SE WHERE " +
				"\n M_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL " +
				"\n AND M_CTO.ID_CONHECIMENTO = DO_SE.ID_DOCTO_SERVICO " +
				"\n AND DO_SE.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM)", "CONTADOR_EMBARCADAS");
        
		/*
		 * Contador de cargas reembarcadas.
		 */
		sql.addProjection("(SELECT COUNT(*) FROM MANIFESTO_NACIONAL_CTO M_CTO, DOCTO_SERVICO DO_SE WHERE " +
				"\n M_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL " +
				"\n AND M_CTO.ID_CONHECIMENTO = DO_SE.ID_DOCTO_SERVICO " +
				"\n AND DO_SE.ID_FILIAL_ORIGEM <> MAN.ID_FILIAL_ORIGEM)", "CONTADOR_REEMBARCADAS");
		
		/*
		 * Contador de seguros.
		 */
		sql.addProjection("(SELECT COUNT(*) FROM MANIFESTO_NACIONAL_CTO M_CTO, DOCTO_SERVICO DO_SE, DOCTO_SERVICO_SEGUROS DSS WHERE " +
				"\n M_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL " +
				"\n AND M_CTO.ID_CONHECIMENTO = DO_SE.ID_DOCTO_SERVICO " +
				"\n AND DSS.ID_CONHECIMENTO = DO_SE.ID_DOCTO_SERVICO )", "CONTADOR_SEGUROS");
		
		/*
		 * Mensagem concordância entrega.
		 */
		sql.addProjection("(SELECT PG.DS_CONTEUDO FROM PARAMETRO_GERAL PG WHERE PG.NM_PARAMETRO_GERAL = 'REGRA_RISCO_MVN')", "DS_REGRA_RISCO_MVN");
		
		/*
		 * Mensagem risco MVN.
		 */
		sql.addProjection("(SELECT PG.DS_CONTEUDO FROM PARAMETRO_GERAL PG WHERE PG.NM_PARAMETRO_GERAL = 'DS_ACORDO_ENTREGA_VIAGEM')", "DS_ACORDO_ENTREGA_VIAGEM");
		
		/** FROM */
		sql.addFrom("MANIFESTO_VIAGEM_NACIONAL", "MVN");
		sql.addFrom("MANIFESTO", "MAN");
		sql.addFrom("FILIAL", "FL_ORIG");
		sql.addFrom("PESSOA", "PE_FL_ORIG");
		sql.addFrom("ENDERECO_PESSOA", "END_PE_FL_ORIG");
		sql.addFrom("TIPO_LOGRADOURO", "TP_END_PE_FL_ORIG");
		sql.addFrom("MUNICIPIO", "MUN_PE_FL_ORIG");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PE_FL_ORIG");
		sql.addFrom("FILIAL", "FL_DEST");
		sql.addFrom("CONTROLE_CARGA", "CC");

		/*
		 * Proprietário.
		 */
		sql.addFrom("PROPRIETARIO", "PROP");
		sql.addFrom("PESSOA", "PE_PROP");
		sql.addFrom("ENDERECO_PESSOA", "END_PE_PROP");
		sql.addFrom("TIPO_LOGRADOURO", "TP_END_PE_PROP");
		sql.addFrom("MUNICIPIO", "MUN_PE_PROP");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PE_PROP");
		sql.addFrom("PAIS", "PAIS_PE_PROP");
		sql.addFrom("FILIAL", "FILIAL_CC");

		/*
		 * Motorista.
		 */
		sql.addFrom("MOTORISTA", "MOT");
		sql.addFrom("PESSOA", "PE_MOT");
		sql.addFrom("ENDERECO_PESSOA", "END_PE_MOT");
		sql.addFrom("TIPO_LOGRADOURO", "TP_END_PE_MOT");
		sql.addFrom("MUNICIPIO", "MUN_PE_MOT");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PE_MOT");
		sql.addFrom("PAIS", "PAIS_PE_MOT");

		/*
		 * Veículo.
		 */
		sql.addFrom("MEIO_TRANSPORTE", "ME_TP");
		sql.addFrom("MEIO_TRANSPORTE_RODOVIARIO", "ME_TP_RO");
		sql.addFrom("MUNICIPIO", "MUN_ME_TP_RO");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_ME_TP_RO");
		sql.addFrom("MODELO_MEIO_TRANSPORTE", "MO_ME_TP");
		sql.addFrom("MARCA_MEIO_TRANSPORTE", "MA_ME_TP");

		/*
		 * Semi reboque.
		 */
		sql.addFrom("MEIO_TRANSPORTE", "ME_TP_SR");
		sql.addFrom("MEIO_TRANSPORTE_RODOVIARIO", "ME_TP_RO_SR");
		sql.addFrom("MUNICIPIO", "MUN_ME_TP_RO_SR");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_ME_TP_RO_SR");
		sql.addFrom("MODELO_MEIO_TRANSPORTE", "MO_ME_TP_SR");
		sql.addFrom("MARCA_MEIO_TRANSPORTE", "MA_ME_TP_SR");

		/** JOIN */
		sql.addJoin("MVN.ID_MANIFESTO_VIAGEM_NACIONAL", "MAN.ID_MANIFESTO");
		sql.addJoin("MAN.ID_FILIAL_ORIGEM", "FL_ORIG.ID_FILIAL");
		sql.addJoin("FL_ORIG.ID_FILIAL", "PE_FL_ORIG.ID_PESSOA");
		sql.addJoin("PE_FL_ORIG.ID_ENDERECO_PESSOA", "END_PE_FL_ORIG.ID_ENDERECO_PESSOA(+)");
		sql.addJoin("END_PE_FL_ORIG.ID_TIPO_LOGRADOURO", "TP_END_PE_FL_ORIG.ID_TIPO_LOGRADOURO(+)");
		sql.addJoin("END_PE_FL_ORIG.ID_MUNICIPIO", "MUN_PE_FL_ORIG.ID_MUNICIPIO(+)");
		sql.addJoin("MUN_PE_FL_ORIG.ID_UNIDADE_FEDERATIVA", "UF_PE_FL_ORIG.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("MAN.ID_FILIAL_DESTINO", "FL_DEST.ID_FILIAL");

		sql.addJoin("MAN.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA(+)");
		sql.addJoin("CC.ID_PROPRIETARIO", "PROP.ID_PROPRIETARIO(+)");
		sql.addJoin("PROP.ID_PROPRIETARIO", "PE_PROP.ID_PESSOA(+)");
		sql.addJoin("PE_PROP.ID_ENDERECO_PESSOA", "END_PE_PROP.ID_ENDERECO_PESSOA(+)");
		sql.addJoin("END_PE_PROP.ID_TIPO_LOGRADOURO", "TP_END_PE_PROP.ID_TIPO_LOGRADOURO(+)");
		sql.addJoin("END_PE_PROP.ID_MUNICIPIO", "MUN_PE_PROP.ID_MUNICIPIO(+)");
		sql.addJoin("MUN_PE_PROP.ID_UNIDADE_FEDERATIVA", "UF_PE_PROP.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("UF_PE_PROP.ID_PAIS", "PAIS_PE_PROP.ID_PAIS(+)");

		sql.addJoin("CC.ID_FILIAL_ORIGEM", "FILIAL_CC.ID_FILIAL(+)");

		sql.addJoin("CC.ID_MOTORISTA", "MOT.ID_MOTORISTA(+)");
		sql.addJoin("MOT.ID_MOTORISTA", "PE_MOT.ID_PESSOA(+)");
		sql.addJoin("PE_MOT.ID_ENDERECO_PESSOA", "END_PE_MOT.ID_ENDERECO_PESSOA(+)");
		sql.addJoin("END_PE_MOT.ID_TIPO_LOGRADOURO", "TP_END_PE_MOT.ID_TIPO_LOGRADOURO(+)");
		sql.addJoin("END_PE_MOT.ID_MUNICIPIO", "MUN_PE_MOT.ID_MUNICIPIO(+)");
		sql.addJoin("MUN_PE_MOT.ID_UNIDADE_FEDERATIVA", "UF_PE_MOT.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("UF_PE_MOT.ID_PAIS", "PAIS_PE_MOT.ID_PAIS(+)");

		/*
		 * Veículo.
		 */
		sql.addJoin("CC.ID_TRANSPORTADO", "ME_TP.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("ME_TP.ID_MEIO_TRANSPORTE", "ME_TP_RO.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("ME_TP_RO.ID_MUNICIPIO", "MUN_ME_TP_RO.ID_MUNICIPIO(+)");
		sql.addJoin("MUN_ME_TP_RO.ID_UNIDADE_FEDERATIVA", "UF_ME_TP_RO.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("ME_TP.ID_MODELO_MEIO_TRANSPORTE", "MO_ME_TP.ID_MODELO_MEIO_TRANSPORTE(+)");
		sql.addJoin("MO_ME_TP.ID_MARCA_MEIO_TRANSPORTE", "MA_ME_TP.ID_MARCA_MEIO_TRANSPORTE(+)");

		/*
		 * Semi reboque.
		 */
		sql.addJoin("CC.ID_SEMI_REBOCADO", "ME_TP_SR.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("ME_TP_SR.ID_MEIO_TRANSPORTE", "ME_TP_RO_SR.ID_MEIO_TRANSPORTE(+)");
		sql.addJoin("ME_TP_RO_SR.ID_MUNICIPIO", "MUN_ME_TP_RO_SR.ID_MUNICIPIO(+)");
		sql.addJoin("MUN_ME_TP_RO_SR.ID_UNIDADE_FEDERATIVA", "UF_ME_TP_RO_SR.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("ME_TP_SR.ID_MODELO_MEIO_TRANSPORTE", "MO_ME_TP_SR.ID_MODELO_MEIO_TRANSPORTE(+)");
		sql.addJoin("MO_ME_TP_SR.ID_MARCA_MEIO_TRANSPORTE", "MA_ME_TP_SR.ID_MARCA_MEIO_TRANSPORTE(+)");

		
		/** WHERE */
		Long idManifestoViagemNacional = criteria.getLong("idManifestoViagemNacional");
		if (idManifestoViagemNacional != null) {
			sql.addCriteria("MVN.ID_MANIFESTO_VIAGEM_NACIONAL", "=", idManifestoViagemNacional);
		}

		/** ORDER BY */
		sql.addOrderBy("MVN.ID_MANIFESTO_VIAGEM_NACIONAL");

		return sql;
	}

	/**
	 * Retorna os dados para exibição do sub-relatório de cargas embarcadas.
	 * @param idManifestoViagemNacional
	 * @param idFilialOrigemManifesto
	 * @return
	 */
	public JRDataSource generateSubReportCargasEmbarcadas(Long idManifestoViagemNacional, Long idFilialOrigemManifesto) {
		SqlTemplate sql = createSqlCargas(idManifestoViagemNacional, idFilialOrigemManifesto, "E");
		JRDataSource dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		return dataSource;
	}

	/**
	 * Retorna os dados para exibição do sub-relatório de cargas reembarcadas.
	 * @param idManifestoViagemNacional
	 * @param idFilialOrigemManifesto
	 * @return
	 */
	public JRDataSource generateSubReportCargasReembarcadas(Long idManifestoViagemNacional, Long idFilialOrigemManifesto) {
		SqlTemplate sql = createSqlCargas(idManifestoViagemNacional, idFilialOrigemManifesto, "R");
		JRDataSource dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		return dataSource;
	}
	
	/**
	 * 
	 * @param idManifestoViagemNacional
	 * @param idFilialOrigemManifesto
	 * @param identificadorCarga
	 * @return
	 */
	private SqlTemplate createSqlCargas(Long idManifestoViagemNacional, Long idFilialOrigemManifesto, String identificadorCarga) {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.addProjection("DO_SE.ID_CLIENTE_REMETENTE", "DO_SE_ID_CLI_REM");
		sql.addProjection("DO_SE.ID_FILIAL_DESTINO", "DO_SE_ID_FL_DEST");
		sql.addProjection("CONH.ID_CONHECIMENTO", "CONH_ID_CONHECIMENTO");
		
		sql.addProjection("PES_REM.NM_PESSOA", "PES_REM_NM_PESSOA");
		sql.addProjection("PES_DEST.NM_PESSOA", "PES_DEST_NM_PESSOA");
		sql.addProjection("MUN_ENT.NM_MUNICIPIO", "MUN_ENT_NM_MUNICIPIO");
		
		/*
		 * Retorna número e série, da primeira nota fiscal ligada ao conhecimento, concatenados por /(barra), para evitar realizar nova consulta na base.
		 */
		sql.addProjection("(SELECT MIN(NR_NOTA_FISCAL || '/' || DS_SERIE) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO)", "NFC_NR_NOTA_FISCAL_DS_SERIE");
		
		sql.addProjection("DO_SE.VL_MERCADORIA", "DO_SE_VL_MERCADORIA");
		sql.addProjection("DO_SE.QT_VOLUMES", "DO_SE_QT_VOLUMES");
		sql.addProjection("DO_SE.PS_REAL", "DO_SE_PS_REAL");
		sql.addProjection("CONH.TP_FRETE", "CONH_TP_FRETE");
		sql.addProjection("DO_SE.VL_TOTAL_DOC_SERVICO", "DO_SE_VL_TOTAL_DOC_SERVICO");
		
		sql.addProjection("CONH.TP_DOCUMENTO_SERVICO", "CONH_TP_DOC_SERV");
		sql.addProjection("FL_ORG.SG_FILIAL", "FL_ORG_SG_FILIAL");
		sql.addProjection("CONH.NR_CONHECIMENTO", "CONH_NR_CONH");
		
		/*
		 * Verifica se o conhecimento possui seguro próprio. 
		 */
		sql.addProjection("DECODE((SELECT COUNT(*) \n"
		+ "FROM DOCTO_SERVICO_SEGUROS DSS \n" 
		+ "WHERE \n" 
		+ "DSS.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO \n"
		+ "), 0, 'N','S')", "DO_SE_POSSUI_SEGURO");
		
		sql.addProjection("FL_DEST.SG_FILIAL", "FL_DEST_SG_FILIAL");
		
		sql.addProjection("PE_CON.NM_PESSOA", "PE_CON_NM_PESSOA");
		sql.addProjection("PE_RED.NM_PESSOA", "PE_RED_NM_PESSOA");
		
		// LMS-6596
		sql.addProjection("ROTA.NR_ROTA", "NR_ROTA");
		
		/** FROM */
		sql.addFrom("MANIFESTO_NACIONAL_CTO", "MN_CTO");
		sql.addFrom("MANIFESTO", "MAN");
		sql.addFrom("CONHECIMENTO", "CONH");
		sql.addFrom("DOCTO_SERVICO", "DO_SE");
		sql.addFrom("CLIENTE", "CLI_REM");
		sql.addFrom("PESSOA", "PES_REM");
		sql.addFrom("CLIENTE", "CLI_DEST");
		sql.addFrom("PESSOA", "PES_DEST");
		sql.addFrom("MUNICIPIO", "MUN_ENT");
		
		sql.addFrom("FILIAL", "FL_ORG");
		sql.addFrom("FILIAL", "FL_DEST");
		
		sql.addFrom("PESSOA", "PE_CON");
		sql.addFrom("PESSOA", "PE_RED");
		
		// LMS-6596
		sql.addFrom("ROTA_COLETA_ENTREGA", "ROTA");
		
		/** JOIN´s */
		sql.addJoin("MN_CTO.ID_MANIFESTO_VIAGEM_NACIONAL", "MAN.ID_MANIFESTO");
		sql.addJoin("MN_CTO.ID_CONHECIMENTO", "CONH.ID_CONHECIMENTO");
		sql.addJoin("CONH.ID_CONHECIMENTO", "DO_SE.ID_DOCTO_SERVICO");
		sql.addJoin("DO_SE.ID_CLIENTE_REMETENTE", "CLI_REM.ID_CLIENTE");
		sql.addJoin("CLI_REM.ID_CLIENTE", "PES_REM.ID_PESSOA");
		sql.addJoin("DO_SE.ID_CLIENTE_DESTINATARIO", "CLI_DEST.ID_CLIENTE");
		sql.addJoin("CLI_DEST.ID_CLIENTE", "PES_DEST.ID_PESSOA");
		sql.addJoin("CONH.ID_MUNICIPIO_ENTREGA", "MUN_ENT.ID_MUNICIPIO");

		sql.addJoin("DO_SE.ID_FILIAL_ORIGEM", "FL_ORG.ID_FILIAL");
		sql.addJoin("DO_SE.ID_FILIAL_DESTINO", "FL_DEST.ID_FILIAL");
		
		sql.addJoin("DO_SE.ID_CLIENTE_CONSIGNATARIO", "PE_CON.ID_PESSOA(+)");
		sql.addJoin("DO_SE.ID_CLIENTE_REDESPACHO", "PE_RED.ID_PESSOA(+)");
		
		// LMS-6596
		sql.addJoin("DO_SE.ID_ROTA_COLETA_ENTREGA_REAL", "ROTA.ID_ROTA_COLETA_ENTREGA(+)");
		
		/** CRITERIA */
		sql.addCriteria("MN_CTO.ID_MANIFESTO_VIAGEM_NACIONAL", "=", idManifestoViagemNacional);

		if("E".equals(identificadorCarga)){
			sql.addCriteria("DO_SE.ID_FILIAL_ORIGEM", "=", idFilialOrigemManifesto);
		} else if("R".equals(identificadorCarga)){ 
			sql.addCriteria("DO_SE.ID_FILIAL_ORIGEM", "<>", idFilialOrigemManifesto);
		}

		/** ORDER */
		// LMS-6596
		sql.addOrderBy("FL_DEST.SG_FILIAL");
		sql.addOrderBy("ROTA.NR_ROTA ASC NULLS FIRST");		
		sql.addOrderBy("MAN.TP_MODAL");
		sql.addOrderBy("PES_REM.NM_PESSOA");
		
		return sql;
	}

	/**
	 * 
	 * @param idManifestoViagemNacional
	 * @return
	 */
	public String montarMensagemSeguros(Long idManifestoViagemNacional) {
		StringBuilder textoMensagemSeguros = new StringBuilder();
		ManifestoViagemNacional manifestoViagemNacional = manifestoViagemNacionalService.findById(idManifestoViagemNacional);
		List<ApoliceSeguro> retornaApolices = apoliceSeguroService.retornaApolices("R",
				new Date());

		String[] parametrosTNT = { "", "", "" };

		// 1) Buscar dados da(s) seguradora(s) TNT
		boolean variasApolices = !retornaApolices.isEmpty() && retornaApolices.size() > 1;
		boolean mesmaSeguradora = true;
		ApoliceSeguro apoliceDeMaiorValor = null;

		for (ApoliceSeguro as : retornaApolices) {
			apoliceDeMaiorValor = (apoliceDeMaiorValor != null && apoliceDeMaiorValor.getVlLimiteApolice().doubleValue() > as.getVlLimiteApolice().doubleValue()) ? apoliceDeMaiorValor : as;
			if (!apoliceDeMaiorValor.getSeguradora().getIdSeguradora().equals(as.getSeguradora().getIdSeguradora()))
				mesmaSeguradora = false;
		}

		if (variasApolices) {
			if (mesmaSeguradora) {
				for (ApoliceSeguro as : retornaApolices)
					parametrosTNT[0] += as.getTipoSeguro().getSgTipo() + " " + as.getNrApolice() + ", ";
				parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia() + " CNPJ "
						+ FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa()) + "";
				parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
			} else {
				for (ApoliceSeguro as : retornaApolices)
					parametrosTNT[0] += as.getTipoSeguro().getSgTipo() + " " + as.getNrApolice() + " "
							+ apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia() + " CNPJ "
							+ FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa());
				parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue()) + ".";
			}
		} else {
			for (ApoliceSeguro as : retornaApolices)
				parametrosTNT[0] += as.getTipoSeguro().getSgTipo() + " " + as.getNrApolice();
			parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia() + " CNPJ "
					+ FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa()) + "";
			parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
		}

		textoMensagemSeguros.append(configuracoesFacade.getMensagem("LMS-04410", parametrosTNT).replace("  ", " ").replace(", ,", ", "));
		textoMensagemSeguros.append("\n");

		// 2) Buscar Seguradora Cliente(s)
		Manifesto manifesto = manifestoViagemNacional.getManifesto();

		Map<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>> segurosClientes = new HashMap<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>>();

		for (PreManifestoDocumento pmd : manifesto.getPreManifestoDocumentos()) {

			DoctoServico doc = pmd.getDoctoServico();
			Cliente cli = doc.getClienteByIdClienteRemetente();

			for (Object obj : cli.getSeguroClientes()) {
				SeguroCliente sc = (SeguroCliente) obj;
				if ("N".equals(sc.getTpAbrangencia().getValue())// Nacional
						&& sc.getDtVigenciaInicial().toDateMidnight().toDate().before(new Date())// esta
																									// vigente?
						&& sc.getDtVigenciaFinal().toDateMidnight().toDate().after(new Date())) {

					Map<ReguladoraSeguro, List<SeguroCliente>> seguros = null;
					if (!segurosClientes.containsKey(cli)) {
						seguros = new HashMap<ReguladoraSeguro, List<SeguroCliente>>();
						segurosClientes.put(cli, seguros);
					} else {
						seguros = segurosClientes.get(cli);
					}

					if (!seguros.containsKey(sc.getReguladoraSeguro()))
						seguros.put(sc.getReguladoraSeguro(), new ArrayList<SeguroCliente>());

					if (!seguros.get(sc.getReguladoraSeguro()).contains(sc))
						seguros.get(sc.getReguladoraSeguro()).add(sc);

				}
			}
		}

		/*
		 * Concatena os dados dos seguros dos clientes.
		 */
		if (!segurosClientes.isEmpty()) {

			Iterator<Cliente> iteratorClientes = segurosClientes.keySet().iterator();

			while (iteratorClientes.hasNext()) {
				String[] param = { "", "", "" };
				BigDecimal valor = BigDecimal.ZERO;
				Cliente cliente = iteratorClientes.next();
				param[0] = (cliente.getPessoa().getNmFantasia() != null ? cliente.getPessoa().getNmFantasia() : cliente.getPessoa().getNmPessoa())
						+ " CNPJ " + FormatUtils.formatIdentificacao(cliente.getPessoa());

				Map<ReguladoraSeguro, List<SeguroCliente>> map = segurosClientes.get(cliente);
				Iterator<ReguladoraSeguro> iteratorSeguros = map.keySet().iterator();
				while (iteratorSeguros.hasNext()) {

					ReguladoraSeguro next = iteratorSeguros.next();
					List<SeguroCliente> list = map.get(next);

					for (SeguroCliente seguroCliente : list) {
						param[1] += seguroCliente.getTipoSeguro().getSgTipo() + " " + seguroCliente.getDsApolice()
								+ (!isLast(list, seguroCliente) ? ", " : "");
						valor = seguroCliente.getVlLimite().doubleValue() > valor.doubleValue() ? seguroCliente.getVlLimite() : valor;
					}

				}
				param[2] = FormatUtils.formatDecimal("#,###,###,##0.00", valor);

				// LMS-04411
				// "Apólice(s) própria(s): {0} apólice(s) de seguro(s) {1}, averbação de R$ {2}."
				textoMensagemSeguros.append(configuracoesFacade.getMensagem("LMS-04411", param));
				textoMensagemSeguros.append("\n");

			}
		}

		return textoMensagemSeguros.toString();
	}

	/**
	 * O Objeto é o último ítem da lista?
	 * 
	 * @param list
	 * @param item
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean isLast(List list, Object item) {
		return (list.indexOf(item)) == (list.size() - 1);
	}

	@Override
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("CONH_TP_DOC_SERV", "DM_TIPO_DOCUMENTO_SERVICO");
		config.configDomainField("CONH_TP_FRETE", "DM_TIPO_FRETE");
	}
	
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}

	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
}
