package com.mercurio.lms.sim.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Rafael Fernando Kohlrausch <rafaelfernando@cwi.com.br>
 * 
 * @spring.bean id="lms.sim.emitirRelatorioDadosVolumesDocumentoService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/sim/report/emitirRelatorioDadosVolumesDocumento.jasper"
 */
public class EmitirRelatorioDadosVolumesDocumentoService extends ReportServiceSupport {

	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;

	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * 
	 */
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = (TypedFlatMap) criteria;
		SqlTemplate sql = createSqlTemplate(map);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		// Seta os parametros
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("formatoRelatorio"));

		jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "FILIAL_ORIGEM_SG_FILIAL");
		sql.addProjection("DOSE.NR_DOCTO_SERVICO", "DOSE_NR_DOCTO_SERVICO");
		sql.addProjection("DOSE.TP_DOCUMENTO_SERVICO", "DOSE_TP_DOCUMENTO_SERVICO");
		sql.addProjection("DOSE.NR_CFOP", "DOSE_NR_CFOP");
		sql.addProjection("DOSE.NR_FATOR_CUBAGEM", "DOSE_NR_FATOR_CUBAGEM");
		sql.addProjection("DOSE.TP_PESO_CALCULO", "DOSE_TP_PESO_CALCULO");
		sql.addProjection("DOSE.BL_UTILIZA_PESO_EDI", "DOSE_BL_UTILIZA_PESO_EDI");
		sql.addProjection("DOSE.NR_FATOR_DENSIDADE", "DOSE_NR_FATOR_DENSIDADE");
		sql.addProjection("CONH.TP_CONHECIMENTO", "CONH_TP_CONHECIMENTO");
		sql.addProjection("CONH.DV_CONHECIMENTO", "CONH_DV_CONHECIMENTO");
		sql.addProjection("CONH.BL_INDICADOR_EDI", "CONH_INFORMACOES_EDI");
		sql.addProjection("CASE  WHEN CONH.NR_CAE IS NULL THEN '' ELSE (FILIAL_ORIGEM.SG_FILIAL || ' ' || LPAD(CONH.NR_CAE,8,'0')) END", "NR_CAE");
		sql.addProjection("DOSE.PS_REAL", "DOSE_PS_DECLARADO");
		sql.addProjection("DOSE.NR_CUBAGEM_DECLARADA", "DOSE_NR_CUBAGEM_DECLARADA");
		sql.addProjection("DOSE.PS_CUBADO_DECLARADO", "DOSE_PS_CUBADO_DECLARADO");
		sql.addProjection("DOSE.PS_AFERIDO", "DOSE_PS_AFERIDO");
		sql.addProjection("DOSE.NR_CUBAGEM_AFERIDA", "DOSE_NR_CUBAGEM_AFERIDA");
		sql.addProjection("DOSE.PS_CUBADO_AFERIDO", "DOSE_PS_CUBADO_AFERIDO");
		sql.addProjection("DOSE.PS_REFERENCIA_CALCULO", "DOSE_PS_REFERENCIA_CALCULO");
		sql.addProjection("DOSE.BL_PESO_CUBADO_POR_DENSIDADE", "DOSE_BL_PESO_CUB_POR_DEN");
		sql.addProjection("DOSE.BL_PESO_FAT_POR_CUBADO_AFERIDO", "DOSE_BL_PESO_FAT_POR_CUB_AF");
		sql.addProjection("TITI.DS_TIPO_TRIBUTACAO_ICMS", "TITI_DS_TIPO_TRIBUTACAO_ICMS");
		sql.addProjection("DOSE.DH_EMISSAO", "DOSE_DH_EMISSAO");
		sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "FILIAL_DESTINO_SG_FILIAL");
		sql.addProjection("PESSOA_DESTINO.NM_PESSOA", "PESSOA_DESTINO_NM_PESSOA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SERV.DS_SERVICO_I"), "SERV_DS_SERVICO");
		sql.addProjection("CONH.TP_FRETE", "CONH_TP_FRETE");
		sql.addProjection("DOSE.VL_TOTAL_PARCELAS", "DOSE_VL_TOTAL_PARCELAS");
		sql.addProjection("DOSE.VL_TOTAL_SERVICOS", "DOSE_VL_TOTAL_SERVICOS");
		sql.addProjection("DOSE.VL_TOTAL_DOC_SERVICO", "DOSE_VL_TOTAL_DOC_SERVICO");
		sql.addProjection("MOED.DS_SIMBOLO", "MOED_DS_SIMBOLO");
		sql.addProjection("DOSE.DT_PREV_ENTREGA", "DOSE_DT_PREV_ENTREGA");
		sql.addProjection("CASE WHEN LOME.CD_LOCALIZACAO_MERCADORIA = 1 THEN DOSE.NR_DIAS_BLOQUEIO ELSE NULL END", "CONH_NR_DIAS_BLOQUEIO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOME.DS_LOCALIZACAO_MERCADORIA_I"), "LOME_DS_LOCALIZACAO_MERCADORIA");
		sql.addProjection("DOSE.QT_VOLUMES", "DOSE_QT_VOLUMES");
		sql.addProjection("DOSE.PS_REAL", "DOSE_PS_REAL");
		sql.addProjection("DOSE.PS_AFORADO", "DOSE_PS_AFORADO");
		sql.addProjection("DOSE.PS_REFERENCIA_CALCULO", "DOSE_PS_REFERENCIA_CALCULO");
		sql.addProjection("DOSE.VL_MERCADORIA", "DOSE_VL_MERCADORIA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("NAPR.DS_NATUREZA_PRODUTO_I"), "NAPR_DS_NATUREZA_PRODUTO");
		sql.addProjection("PECO.DS_COMPLEMENTO_ENDERECO", "PECO_DS_COMPLEMENTO_ENDERECO");
		sql.addProjection("NVL2(PECO.NR_ENDERECO,PECO.ED_COLETA||', '||PECO.NR_ENDERECO,PECO.ED_COLETA)", "PECO_ED_COLETA");
		sql.addProjection("PECO.NR_CEP", "PECO_NR_CEP");
		sql.addProjection("PECO.NR_COLETA", "PECO_NR_COLETA");
		sql.addProjection("PECO.DT_PREVISAO_COLETA", "PECO_DT_PREVISAO_COLETA");
		sql.addProjection("PECO.DH_COLETA_DISPONIVEL", "PECO_DH_COLETA_DISPONIVEL");
		sql.addProjection("PECO.TP_PEDIDO_COLETA", "PECO_TP_PEDIDO_COLETA");
		sql.addProjection("PECO.TP_MODO_PEDIDO_COLETA", "PECO_TP_MODO_PEDIDO_COLETA");
		sql.addProjection("MUNI.NM_MUNICIPIO", "MUNI_NM_MUNICIPIO");
		sql.addProjection("UNFE.SG_UNIDADE_FEDERATIVA", "UNFE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS.NM_PAIS_I"), "PAIS_NM_PAIS");
		sql.addProjection("DOSE.DS_ENDERECO_ENTREGA_REAL", "DOSE_DS_ENDERECO_ENTREGA_REAL");
		sql.addProjection("TITP.TP_TIPO_TABELA_PRECO", "TITP_TP_TIPO_TABELA_PRECO");
		sql.addProjection("TITP.NR_VERSAO", "TITP_NR_VERSAO");
		sql.addProjection("STTP.TP_SUBTIPO_TABELA_PRECO", "STTP_TP_SUBTIPO_TABELA_PRECO");
		sql.addProjection("DOSE.TP_CALCULO_PRECO", "DOSE_TP_CALCULO_PRECO");
		sql.addProjection("DOSE.ID_DOCTO_SERVICO", "DOSE_ID_DOCTO_SERVICO");
		sql.addProjection("MACO.NR_MANIFESTO", "MACO_NR_MANIFESTO");
		sql.addProjection("MACO.DH_EMISSAO", "MACO_DH_EMISSAO");
		sql.addProjection("( SELECT EVDS.DH_EVENTO " + "  FROM EVENTO_DOCUMENTO_SERVICO EVDS, " + "		  EVENTO EVEN"
				+ "  WHERE EVDS.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" + "  AND EVEN.ID_EVENTO = EVDS.ID_EVENTO" + "  AND EVEN.CD_EVENTO = 21"
				+ "  AND EVDS.BL_EVENTO_CANCELADO != 'S' " + "	 AND ROWNUM <= 1)", "EVDS_DH_EVENTO");

		sql.addProjection("(SELECT COUNT(*) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO)", "NFC_QTD_NOTAS");

		// Remetente
		sql.addProjection("PERE.ID_PESSOA", "PERE_ID_PESSOA");
		sql.addProjection("PERE.TP_IDENTIFICACAO", "PERE_TP_IDENTIFICACAO");
		sql.addProjection("PERE.NR_IDENTIFICACAO", "PERE_NR_IDENTIFICACAO");
		sql.addProjection("PERE.NM_PESSOA", "PERE_NM_PESSOA");
		sql.addProjection("ENDERECO_PERE.DS_ENDERECO", "ENDERECO_PERE_DS_ENDERECO");
		sql.addProjection("ENDERECO_PERE.NR_ENDERECO", "ENDERECO_PERE_NR_ENDERECO");
		sql.addProjection("ENDERECO_PERE.NR_CEP", "ENDERECO_PERE_NR_CEP");
		sql.addProjection("MUNICIPIO_PERE.NM_MUNICIPIO", "MUNICIPIO_PERE_NM_MUNICIPIO");
		sql.addProjection("UF_PERE.SG_UNIDADE_FEDERATIVA", "UF_PERE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PERE.NM_PAIS_I"), "PAIS_PERE_NM_PAIS");

		// Destinatário
		sql.addProjection("PEDE.ID_PESSOA", "PEDE_ID_PESSOA");
		sql.addProjection("PEDE.TP_IDENTIFICACAO", "PEDE_TP_IDENTIFICACAO");
		sql.addProjection("PEDE.NR_IDENTIFICACAO", "PEDE_NR_IDENTIFICACAO");
		sql.addProjection("PEDE.NM_PESSOA", "PEDE_NM_PESSOA");
		sql.addProjection("ENDERECO_PEDE.DS_ENDERECO", "ENDERECO_PEDE_DS_ENDERECO");
		sql.addProjection("ENDERECO_PEDE.NR_ENDERECO", "ENDERECO_PEDE_NR_ENDERECO");
		sql.addProjection("ENDERECO_PEDE.NR_CEP", "ENDERECO_PEDE_NR_CEP");
		sql.addProjection("MUNICIPIO_PEDE.NM_MUNICIPIO", "MUNICIPIO_PEDE_NM_MUNICIPIO");
		sql.addProjection("UF_PEDE.SG_UNIDADE_FEDERATIVA", "UF_PEDE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PEDE.NM_PAIS_I"), "PAIS_PEDE_NM_PAIS");

		// Redespacho
		sql.addProjection("PERD.ID_PESSOA", "PERD_ID_PESSOA");
		sql.addProjection("PERD.TP_IDENTIFICACAO", "PERD_TP_IDENTIFICACAO");
		sql.addProjection("PERD.NR_IDENTIFICACAO", "PERD_NR_IDENTIFICACAO");
		sql.addProjection("PERD.NM_PESSOA", "PERD_NM_PESSOA");
		sql.addProjection("ENDERECO_PERD.DS_ENDERECO", "ENDERECO_PERD_DS_ENDERECO");
		sql.addProjection("ENDERECO_PERD.NR_ENDERECO", "ENDERECO_PERD_NR_ENDERECO");
		sql.addProjection("ENDERECO_PERD.NR_CEP", "ENDERECO_PERD_NR_CEP");
		sql.addProjection("MUNICIPIO_PERD.NM_MUNICIPIO", "MUNICIPIO_PERD_NM_MUNICIPIO");
		sql.addProjection("UF_PERD.SG_UNIDADE_FEDERATIVA", "UF_PERD_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PERD.NM_PAIS_I"), "PAIS_PERD_NM_PAIS");

		// Consignatário
		sql.addProjection("PECON.ID_PESSOA", "PECON_ID_PESSOA");
		sql.addProjection("PECON.TP_IDENTIFICACAO", "PECON_TP_IDENTIFICACAO");
		sql.addProjection("PECON.NR_IDENTIFICACAO", "PECON_NR_IDENTIFICACAO");
		sql.addProjection("PECON.NM_PESSOA", "PECON_NM_PESSOA");
		sql.addProjection("ENDERECO_PECON.DS_ENDERECO", "ENDERECO_PECON_DS_ENDERECO");
		sql.addProjection("ENDERECO_PECON.NR_ENDERECO", "ENDERECO_PECON_NR_ENDERECO");
		sql.addProjection("ENDERECO_PECON.NR_CEP", "ENDERECO_PECON_NR_CEP");
		sql.addProjection("MUNICIPIO_PECON.NM_MUNICIPIO", "MUNICIPIO_PECON_NM_MUNICIPIO");
		sql.addProjection("UF_PECON.SG_UNIDADE_FEDERATIVA", "UF_PECON_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PECON.NM_PAIS_I"), "PAIS_PECON_NM_PAIS");

		// Tomador
		sql.addProjection("PETM.ID_PESSOA", "PETM_ID_PESSOA");
		sql.addProjection("PETM.TP_IDENTIFICACAO", "PETM_TP_IDENTIFICACAO");
		sql.addProjection("PETM.NR_IDENTIFICACAO", "PETM_NR_IDENTIFICACAO");
		sql.addProjection("PETM.NM_PESSOA", "PETM_NM_PESSOA");
		sql.addProjection("ENDERECO_PETM.DS_ENDERECO", "ENDERECO_PETM_DS_ENDERECO");
		sql.addProjection("ENDERECO_PETM.NR_ENDERECO", "ENDERECO_PETM_NR_ENDERECO");
		sql.addProjection("ENDERECO_PETM.NR_CEP", "ENDERECO_PETM_NR_CEP");
		sql.addProjection("MUNICIPIO_PETM.NM_MUNICIPIO", "MUNICIPIO_PETM_NM_MUNICIPIO");
		sql.addProjection("UF_PETM.SG_UNIDADE_FEDERATIVA", "UF_PETM_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PETM.NM_PAIS_I"), "PAIS_PETM_NM_PAIS");
		sql.addProjection("DOSE.VL_FRETE_LIQUIDO", "DOSE_VL_FRETE_LIQUIDO");
		sql.addProjection("DOSE.VL_ICMS_ST", "DOSE_VL_ICMS_ST");
		sql.addProjection("USDIG.NM_USUARIO", "NM_USUARIO_DIGITADOR");

		/** FROM */
		sql.addFrom("DOCTO_SERVICO", "DOSE");
		sql.addFrom("FILIAL", "FILIAL_ORIGEM");
		sql.addFrom("FILIAL", "FILIAL_DESTINO");
		sql.addFrom("PESSOA", "PESSOA_DESTINO");
		sql.addFrom("CONHECIMENTO", "CONH");
		sql.addFrom("SERVICO", "SERV");
		sql.addFrom("MOEDA", "MOED");
		sql.addFrom("LOCALIZACAO_MERCADORIA", "LOME");
		sql.addFrom("NATUREZA_PRODUTO", "NAPR");
		sql.addFrom("PEDIDO_COLETA", "PECO");
		sql.addFrom("MUNICIPIO", "MUNI");
		sql.addFrom("UNIDADE_FEDERATIVA", "UNFE");
		sql.addFrom("PAIS", "PAIS");
		sql.addFrom("TABELA_PRECO", "TAPE");
		sql.addFrom("TIPO_TABELA_PRECO", "TITP");
		sql.addFrom("TIPO_TRIBUTACAO_ICMS", "TITI");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "STTP");
		sql.addFrom("MANIFESTO_COLETA", "MACO");
		sql.addFrom("USUARIO", "USDIG");

		// Remetente
		sql.addFrom("PESSOA", "PERE");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PERE");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PERE");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PERE");
		sql.addFrom("PAIS", "PAIS_PERE");

		// Destinatario
		sql.addFrom("PESSOA", "PEDE");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PEDE");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PEDE");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PEDE");
		sql.addFrom("PAIS", "PAIS_PEDE");

		// Redespacho
		sql.addFrom("PESSOA", "PERD");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PERD");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PERD");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PERD");
		sql.addFrom("PAIS", "PAIS_PERD");

		// Consignatario
		sql.addFrom("PESSOA", "PECON");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PECON");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PECON");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PECON");
		sql.addFrom("PAIS", "PAIS_PECON");

		// Tomador
		sql.addFrom("DEVEDOR_DOC_SERV", "DDS");
		sql.addFrom("PESSOA", "PETM");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PETM");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PETM");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PETM");
		sql.addFrom("PAIS", "PAIS_PETM");

		/** JOIN */
		sql.addJoin("FILIAL_ORIGEM.ID_FILIAL(+)", "DOSE.ID_FILIAL_ORIGEM");
		sql.addJoin("FILIAL_DESTINO.ID_FILIAL(+)", "DOSE.ID_FILIAL_DESTINO");
		sql.addJoin("PESSOA_DESTINO.ID_PESSOA(+)", "FILIAL_DESTINO.ID_FILIAL");
		sql.addJoin("PECO.ID_PEDIDO_COLETA(+)", "DOSE.ID_PEDIDO_COLETA");
		sql.addJoin("MACO.ID_MANIFESTO_COLETA(+)", "PECO.ID_MANIFESTO_COLETA");
		sql.addJoin("TAPE.ID_TABELA_PRECO(+)", "DOSE.ID_TABELA_PRECO");
		sql.addJoin("TITP.ID_TIPO_TABELA_PRECO(+)", "TAPE.ID_TIPO_TABELA_PRECO");
		sql.addJoin("STTP.ID_SUBTIPO_TABELA_PRECO(+)", "TAPE.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("MUNI.ID_MUNICIPIO(+)", "PECO.ID_MUNICIPIO");
		sql.addJoin("UNFE.ID_UNIDADE_FEDERATIVA(+)", "MUNI.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS.ID_PAIS(+)", "UNFE.ID_PAIS");
		sql.addJoin("CONH.ID_CONHECIMENTO(+)", "DOSE.ID_DOCTO_SERVICO");
		sql.addJoin("TITI.ID_TIPO_TRIBUTACAO_ICMS(+)", "CONH.ID_TIPO_TRIBUTACAO_ICMS");
		sql.addJoin("NAPR.ID_NATUREZA_PRODUTO(+)", "CONH.ID_NATUREZA_PRODUTO");
		sql.addJoin("SERV.ID_SERVICO(+)", "DOSE.ID_SERVICO");
		sql.addJoin("MOED.ID_MOEDA(+)", "DOSE.ID_MOEDA");
		sql.addJoin("LOME.ID_LOCALIZACAO_MERCADORIA(+)", "DOSE.ID_LOCALIZACAO_MERCADORIA");
		sql.addJoin("DOSE.ID_USUARIO_INCLUSAO(+)", "USDIG.ID_USUARIO");

		// Remetente
		sql.addJoin("PERE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_REMETENTE");
		sql.addJoin("ENDERECO_PERE.ID_ENDERECO_PESSOA(+)", "PERE.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PERE.ID_MUNICIPIO(+)", "ENDERECO_PERE.ID_MUNICIPIO");
		sql.addJoin("UF_PERE.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PERE.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PERE.ID_PAIS(+)", "UF_PERE.ID_PAIS");

		// Destinatario
		sql.addJoin("PEDE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_DESTINATARIO");
		sql.addJoin("ENDERECO_PEDE.ID_ENDERECO_PESSOA(+)", "PEDE.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PEDE.ID_MUNICIPIO(+)", "ENDERECO_PEDE.ID_MUNICIPIO");
		sql.addJoin("UF_PEDE.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PEDE.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PEDE.ID_PAIS(+)", "UF_PEDE.ID_PAIS");

		// Redespacho
		sql.addJoin("PERD.ID_PESSOA(+)", "DOSE.ID_CLIENTE_REDESPACHO");
		sql.addJoin("ENDERECO_PERD.ID_ENDERECO_PESSOA(+)", "PERD.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PERD.ID_MUNICIPIO(+)", "ENDERECO_PERD.ID_MUNICIPIO");
		sql.addJoin("UF_PERD.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PERD.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PERD.ID_PAIS(+)", "UF_PERD.ID_PAIS");

		// Consignatario
		sql.addJoin("PECON.ID_PESSOA(+)", "DOSE.ID_CLIENTE_CONSIGNATARIO");
		sql.addJoin("ENDERECO_PECON.ID_ENDERECO_PESSOA(+)", "PECON.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PECON.ID_MUNICIPIO(+)", "ENDERECO_PECON.ID_MUNICIPIO");
		sql.addJoin("UF_PECON.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PECON.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PECON.ID_PAIS(+)", "UF_PECON.ID_PAIS");

		// Tomador
		sql.addJoin("DDS.ID_DOCTO_SERVICO(+)", "DOSE.ID_DOCTO_SERVICO");
		sql.addJoin("PETM.ID_PESSOA(+)", "DDS.ID_CLIENTE");
		sql.addJoin("ENDERECO_PETM.ID_ENDERECO_PESSOA(+)", "PETM.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PETM.ID_MUNICIPIO(+)", "ENDERECO_PETM.ID_MUNICIPIO");
		sql.addJoin("UF_PETM.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PETM.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PETM.ID_PAIS(+)", "UF_PETM.ID_PAIS");

		/** CRITERIA */
		if (criteria.getLong("idDoctoServico") != null) {
			sql.addCriteria("DOSE.ID_DOCTO_SERVICO", "=", criteria.getLong("idDoctoServico"));
		}

		return sql;
	}

	/**
	 * 
	 * @param idDoctoServico
	 * @return
	 * @throws Exception
	 */
	public JRDataSource generateSubReportDetalhe(Long idDoctoServico) throws Exception {
		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			dataSource = executeQuery(createSqlDetalhe(idDoctoServico), (new ArrayList()).toArray()).getDataSource();
		}

		return dataSource;
	}

	/**
	 * 
	 * @param idDoctoServico
	 * @return
	 * @throws Exception
	 */
	private String createSqlDetalhe(Long idDoctoServico) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("Select * from ( ");
		sql.append(createSqlDetalhe(idDoctoServico, Boolean.FALSE));
		sql.append(" GROUP BY NFC.ID_NOTA_FISCAL_CONHECIMENTO, NFC.NR_NOTA_FISCAL, DS.NR_FATOR_DENSIDADE, VNF.TP_ORIGEM_PESO, (CASE WHEN (vnf.TP_VOLUME = 'M' OR (vnf.TP_VOLUME = 'U' AND VNF.QT_VOLUMES > 1)) THEN 'P' ELSE 'V' END), VNF.PS_AFERIDO, VNF.NR_DIMENSAO_1_CM, VNF.NR_DIMENSAO_2_CM,	VNF.NR_DIMENSAO_3_CM, CASE WHEN (VNF.NR_CUBAGEM  IS NOT NULL) THEN VNF.NR_CUBAGEM WHEN (VNF.NR_DIMENSAO_1_CM  IS NOT NULL) THEN TRUNC((VNF.NR_DIMENSAO_1_CM * VNF.NR_DIMENSAO_2_CM * VNF.NR_DIMENSAO_3_CM)/1000000, 4) ELSE  NULL END , DS.NR_FATOR_CUBAGEM, USU.NM_USUARIO");
		sql.append(" UNION ALL ");
		sql.append(createSqlDetalhe(idDoctoServico, Boolean.TRUE));
		sql.append(" ) tb ");
		sql.append("order by tb.NFC_NR_NOTA_FISCAL");

		return sql.toString();
	}
	
	private String createSqlDetalhe(Long idDoctoServico, Boolean isPalete) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection("NFC.ID_NOTA_FISCAL_CONHECIMENTO", "NFC_ID_NOTA_FISCAL_CONH");
		sql.addProjection("NFC.NR_NOTA_FISCAL", "NFC_NR_NOTA_FISCAL");
		if (isPalete) {
			sql.addProjection("VNF.QT_VOLUMES", "VNF_SOMA_QT_VOLUMES");
		} else {
			sql.addProjection("SUM(VNF.QT_VOLUMES)", "VNF_SOMA_QT_VOLUMES");
		}

		sql.addProjection("DS.NR_FATOR_DENSIDADE", "DS_NR_FATOR_DENSIDADE");
		sql.addProjection("VNF.TP_ORIGEM_PESO", "VNF_TP_ORIGEM_PESO");

		sql.addProjection("(CASE WHEN (vnf.TP_VOLUME = 'M' OR (vnf.TP_VOLUME = 'U' AND VNF.QT_VOLUMES > 1)) THEN 'P' ELSE 'V' END)", "VNF_TP_VOLUME");
		sql.addProjection("VNF.PS_AFERIDO", "VNF_PS_AFERIDO");
		sql.addProjection("VNF.NR_DIMENSAO_1_CM", "VNF_NR_DIMENSAO_1_CM");
		sql.addProjection("VNF.NR_DIMENSAO_2_CM", "VNF_NR_DIMENSAO_2_CM");
		sql.addProjection("VNF.NR_DIMENSAO_3_CM", "VNF_NR_DIMENSAO_3_CM");
		sql.addProjection("CASE WHEN (VNF.NR_CUBAGEM IS NOT NULL) THEN VNF.NR_CUBAGEM WHEN (VNF.NR_DIMENSAO_1_CM IS NOT NULL) THEN TRUNC((VNF.NR_DIMENSAO_1_CM * VNF.NR_DIMENSAO_2_CM * VNF.NR_DIMENSAO_3_CM)/1000000, 4) ELSE  NULL END", "VNF_NR_CUBAGEM");
		sql.addProjection("DS.NR_FATOR_CUBAGEM ", "DS_NR_FATOR_CUBAGEM");
		sql.addProjection("USU.NM_USUARIO", "USU_NM_USUARIO");

		/** FROM */
		sql.addFrom("VOLUME_NOTA_FISCAL", "VNF");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NFC");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("CONHECIMENTO", "CONH");
		sql.addFrom("USUARIO", "USU");

		/** JOIN´s */
		sql.addJoin("VNF.ID_NOTA_FISCAL_CONHECIMENTO", "NFC.ID_NOTA_FISCAL_CONHECIMENTO");
		sql.addJoin("CONH.ID_CONHECIMENTO", "DS.ID_DOCTO_SERVICO");
		sql.addJoin("NFC.ID_CONHECIMENTO", "CONH.ID_CONHECIMENTO");
		sql.addJoin("VNF.ID_USUARIO_EMISSAO", "USU.ID_USUARIO (+)");

		/** CRITERIA */
		if (isPalete) {
			sql.addCustomCriteria("(vnf.TP_VOLUME = 'M' OR (vnf.TP_VOLUME = 'U' AND VNF.QT_VOLUMES > 1))");
		} else {
			sql.addCustomCriteria("VNF.TP_VOLUME = 'U'");
			sql.addCustomCriteria("VNF.QT_VOLUMES = 1");
		}
		
		sql.addCustomCriteria("DS.ID_DOCTO_SERVICO = " + idDoctoServico);

		return sql.getSql();
	}
	

	/**
	 * 
	 * @param idPessoa
	 * @return
	 */
	public String findEnderecoCompleto(Long idPessoa) {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);

		if (enderecoPessoa != null) {
			return enderecoPessoaService.formatEnderecoPessoaComplemento(enderecoPessoa);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param idPessoa
	 * @return
	 */
	public String findTelefoneEnderecoPadrao(Long idPessoa) {
		TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoPadrao(idPessoa);

		if (telefoneEndereco != null) {
			if (telefoneEndereco.getNrDdi() != null)
				return "+" + telefoneEndereco.getNrDdi() + " " + telefoneEndereco.getDddTelefone();
			else
				return telefoneEndereco.getDddTelefone();
		}

		return null;
	}

	/**
	 * Configura Dominios
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("COCA_TP_CONTROLE_CARGA", "DM_TIPO_CONTROLE_CARGAS");
		config.configDomainField("EVDS_TP_DOCUMENTO", "DM_TIPO_DOCUMENTO");
		config.configDomainField("DOSE_TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
		config.configDomainField("CONH_TP_CONHECIMENTO", "DM_TIPO_CONHECIMENTO");
		config.configDomainField("CONH_TP_FRETE", "DM_TIPO_FRETE");
		config.configDomainField("PECO_TP_PEDIDO_COLETA", "DM_TIPO_PEDIDO_COLETA");
		config.configDomainField("PECO_TP_MODO_PEDIDO_COLETA", "DM_MODO_PEDIDO_COLETA");
		config.configDomainField("TITP_TP_TIPO_TABELA_PRECO", "DM_TIPO_TABELA_PRECO");
		config.configDomainField("DOSE_TP_CALCULO_PRECO", "DM_TIPO_CALCULO_FRETE");
		config.configDomainField("DDSF_TP_SITUACAO_COBRANCA", "DM_STATUS_COBRANCA_DOCTO_SERVICO");
		config.configDomainField("DOSE_TP_PESO_CALCULO", "DM_TIPO_PESO_CALCULO");
		config.configDomainField("VNF_TP_ORIGEM_PESO", "DM_TIPO_ORIGEM_PESO");
	}
}