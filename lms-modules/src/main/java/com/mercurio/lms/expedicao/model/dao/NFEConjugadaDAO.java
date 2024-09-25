package com.mercurio.lms.expedicao.model.dao;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;

public class NFEConjugadaDAO extends AdsmDao {

	private static final String UTF_8 = "UTF-8";

	/**
	 * Recupera todos os dados de negócio para formar a nota fiscal eletrônica conjugada.
	 * 
	 * @param idDoctoServico
	 * @param idFilial
	 * 
	 * @return List<NFEConjugada> 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findEnvioNotaFiscalConjugada(Long idDoctoServico, Long idFilial) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT * ");
		sql.append("FROM ");
		sql.append("  ( SELECT DISTINCT ");
		sql.append("    uf.nr_ibge               AS idemitenteufnfe, ");
		sql.append("    uf.sg_unidade_federativa AS sigla, ");		
		sql.append("	(SELECT LISTAGG(DS_OBSERVACAO_DOCTO_SERVICO, '; ') WITHIN GROUP ( ");
		sql.append("		ORDER BY bl_prioridade) ");
		sql.append("		FROM OBSERVACAO_DOCTO_SERVICO ");
		sql.append("		WHERE ID_DOCTO_SERVICO = :idDoctoServico) AS observacao,");		
		sql.append("    (SELECT ds_conteudo ");
		sql.append("    FROM parametro_geral ");
		sql.append("    WHERE nm_parametro_geral = 'NAT_OPER_NFE_CONJUGADA' ");
		sql.append("    )    AS naturezaoperacao, ");
		sql.append("    '0'  AS tpformapagamento, ");
		sql.append("    '55' AS tpmodelodocumentofiscal, ");
		sql.append("    (SELECT vl_conteudo_parametro_filial ");
		sql.append("    FROM conteudo_parametro_filial ");
		sql.append("    WHERE id_filial          = :idFilial ");
		sql.append("    AND id_parametro_filial IN ");
		sql.append("      (SELECT id_parametro_filial ");
		sql.append("      FROM parametro_filial ");
		sql.append("      WHERE nm_parametro_filial = 'SERIE_NFE_CONJUGADA' ");
		sql.append("      ) ");
		sql.append("    )                   AS seriedocumentofiscal, ");
		sql.append("    ds.nr_docto_servico AS nrdocumentofiscal, ");
		sql.append("    ds.dh_emissao       AS dhemissao, ");
		sql.append("    '1'                 AS tipooperacao, ");
		sql.append("    '1'                 AS tpformatoimpressaodanfe, ");
		sql.append("    '1'                 AS tpformaemissaonfe, ");
		sql.append("    (SELECT ds_conteudo ");
		sql.append("    FROM parametro_geral ");
		sql.append("    WHERE nm_parametro_geral = 'AMBIENTE_NFE_CONJUGADA' ");
		sql.append("    )                         AS tpambiente, ");
		sql.append("    '1'                       AS finalidadenfe, ");
		sql.append("    '0'                       AS tpindicadoroperacao, ");
		sql.append("    '0'                       AS tpprocessoemissaonfe, ");
		sql.append("    pe.tp_pessoa              AS tppessoaemitente, ");
		sql.append("    pe.nr_identificacao       AS cpfcnpjemitente, ");
		sql.append("    pe.nm_pessoa              AS nmpessoaemitente, ");
		sql.append("    pe.nm_fantasia            AS nmfantasia, ");
		sql.append("    ie.nr_inscricao_estadual  AS ieemitente, ");
		sql.append("    pe.nr_inscricao_municipal AS imemitente, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'COD_CNAE_TRANSPORTE' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'COD_CNAE_SERVICO' ");
		sql.append("        ) ");
		sql.append("    END                     AS cnaefiscal, ");
		sql.append("    ds.tp_documento_servico AS tp_documento_servico, ");
		sql.append("    (SELECT vl_conteudo_parametro_filial ");
		sql.append("    FROM conteudo_parametro_filial ");
		sql.append("    WHERE id_filial          = :idFilial ");
		sql.append("    AND id_parametro_filial IN ");
		sql.append("      (SELECT id_parametro_filial ");
		sql.append("      FROM parametro_filial ");
		sql.append("      WHERE nm_parametro_filial = 'REGIME_TRIBUTARIO_NF' ");
		sql.append("      ) ");
		sql.append("    ) AS idregimetributarioemitente, ");
		sql.append("    /*endereco emitente*/ ");
		sql.append("    vi18n(tl.ds_tipo_logradouro_i) ");
		sql.append("    ||' ' ");
		sql.append("    ||ep.ds_endereco  AS logradouroemitente, ");
		sql.append("    ep.nr_endereco    AS numeroemitente, ");
		sql.append("    ep.ds_complemento AS complementoemitente, ");
		sql.append("    ep.ds_bairro      AS bairroemitente, ");
		sql.append("    uf.nr_ibge ");
		sql.append("    || LPAD(mu.cd_ibge,5,0)  AS idmunicipioemitente, ");
		sql.append("    mu.nm_municipio          AS nmmunicipioemitente, ");
		sql.append("    uf.sg_unidade_federativa AS siglaufemitente, ");
		sql.append("    ep.nr_cep                AS idcepemitente, ");
		sql.append("    pa.nr_bacen              AS idpaisemitente, ");
		sql.append("    vi18n(pa.nm_pais_i)      AS nmpaisemitente, ");
		sql.append("    te.nr_ddd ");
		sql.append("    || te.nr_telefone AS telefoneemitente ");
		sql.append("  FROM docto_servico ds , ");
		sql.append("    pessoa pe, ");
		sql.append("    endereco_pessoa ep, ");
		sql.append("    municipio mu , ");
		sql.append("    unidade_federativa uf , ");
		sql.append("    inscricao_estadual ie, ");
		sql.append("    tipo_logradouro tl, ");
		sql.append("    pais pa, ");
		sql.append("    telefone_endereco te ");
		sql.append("  WHERE ds.id_docto_servico    = :idDoctoServico ");
		sql.append("  AND pe.id_pessoa             = ds.id_filial_origem ");
		sql.append("  AND ie.id_pessoa             = pe.id_pessoa ");
		sql.append("  AND ie.tp_situacao           = 'A' ");
		sql.append("  AND ie.bl_indicador_padrao   = 'S' ");
		sql.append("  AND ep.id_endereco_pessoa    = pe.id_endereco_pessoa ");
		sql.append("  AND mu.id_municipio          = ep.id_municipio ");
		sql.append("  AND te.id_endereco_pessoa    = ep.id_endereco_pessoa ");
		sql.append("  AND te.tp_telefone           = 'C' ");
		sql.append("  AND te.tp_uso               IN ('FO','FF') ");
		sql.append("  AND tl.id_tipo_logradouro    = ep.id_tipo_logradouro ");
		sql.append("  AND uf.id_unidade_federativa = mu.id_unidade_federativa ");
		sql.append("  AND uf.id_pais               = pa.id_pais ");
		sql.append("  ) , ");
		sql.append("  /*fim emitente*/ ");
		sql.append("  /*inicio do devedor*/ ");
		sql.append("  ( ");
		sql.append("  SELECT pe.nr_identificacao  AS cpfcnpjdestinatario, ");
		sql.append("    pe.nm_pessoa              AS nmpessoadestinatario, ");
		sql.append("    (CASE WHEN ie.nr_inscricao_estadual = 'ISENTO' THEN '2' ELSE '1' END) AS indicadoriedestinatario, ");
		sql.append("    (CASE WHEN ie.nr_inscricao_estadual = 'ISENTO' THEN '' ELSE ie.nr_inscricao_estadual END) AS iedestinatario, ");
		sql.append("    pe.tp_pessoa              AS tppessoadestinatario, ");
		sql.append("    cl.nr_inscricao_suframa   AS inscricaosuframa, ");
		sql.append("    pe.nr_inscricao_municipal AS imtomador, ");
		sql.append("    vi18n(tl.ds_tipo_logradouro_i) ");
		sql.append("    ||' ' ");
		sql.append("    ||ep.ds_endereco  AS logradourodestinatario, ");
		sql.append("    ep.nr_endereco    AS numerodestinatario, ");
		sql.append("    ep.ds_complemento AS complementodestinatario, ");
		sql.append("    ep.ds_bairro      AS bairrodestinatario, ");
		sql.append("    uf.nr_ibge ");
		sql.append("    || LPAD(mu.cd_ibge,5,0)  AS idmunicipiodestinatario, ");
		sql.append("    mu.nm_municipio          AS nmmunicipiodestinatario, ");
		sql.append("    uf.sg_unidade_federativa AS siglaufdestinatario, ");
		sql.append("    ep.nr_cep                AS idcepdestinatario, ");
		sql.append("    pa.nr_bacen              AS idpaisdestinatario, ");
		sql.append("    vi18n(pa.nm_pais_i)      AS nmpaisdestinatario, ");
		sql.append("    te.nr_ddd ");
		sql.append("    || ' ' ");
		sql.append("    || te.nr_telefone AS telefonedestinatario, ");
		sql.append("    pe.ds_email       AS emaildestinatario ");
		sql.append("  FROM docto_servico ds, ");
		sql.append("    pessoa pe, ");
		sql.append("    devedor_doc_serv dev, ");
		sql.append("    endereco_pessoa ep, ");
		sql.append("    municipio mu , ");
		sql.append("    unidade_federativa uf, ");
		sql.append("    inscricao_estadual ie, ");
		sql.append("    tipo_logradouro tl, ");
		sql.append("    pais pa, ");
		sql.append("    telefone_endereco te, ");
		sql.append("    cliente cl ");
		sql.append("  WHERE ds.id_docto_servico       = :idDoctoServico ");
		sql.append("  AND dev.id_docto_servico        = ds.id_docto_servico ");
		sql.append("  AND pe.id_pessoa                = dev.id_cliente ");
		sql.append("  AND cl.id_cliente               = dev.id_cliente ");
		sql.append("  AND ie.id_pessoa                = dev.id_cliente");
		sql.append("  AND ep.id_endereco_pessoa       = pe.id_endereco_pessoa ");
		sql.append("  AND mu.id_municipio             = ep.id_municipio ");
		sql.append("  AND te.id_endereco_pessoa(+)    = ep.id_endereco_pessoa ");
		sql.append("  AND te.tp_telefone(+)           = 'C' ");
		sql.append("  AND te.tp_uso(+)               IN ('FO','FF') ");
		sql.append("  AND tl.id_tipo_logradouro       = ep.id_tipo_logradouro ");
		sql.append("  AND uf.id_unidade_federativa    = mu.id_unidade_federativa ");
		sql.append("  AND uf.id_pais                  = pa.id_pais ");
		sql.append("  AND ie.tp_situacao(+)           = 'A' ");
		sql.append("  AND ie.bl_indicador_padrao(+)   = 'S' ");
		sql.append("  ), ");
		sql.append("  (SELECT DISTINCT ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'COD_CNAE_TRANSPORTE' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'COD_CNAE_SERVICO' ");
		sql.append("        ) ");
		sql.append("    END AS idprodutoservico, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'SERVICO_NTE' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN vi18n(sa.ds_servico_adicional_i) ");
		sql.append("    END            AS descprodutoservico, ");
		sql.append("  CASE ");
		sql.append("    WHEN ds.tp_documento_servico = 'NSE' or ds.tp_documento_servico = 'NTE' ");
		sql.append("    THEN ");
		sql.append("      (SELECT ");
		sql.append("        CASE ");
		sql.append("          WHEN uf2 = uf1 ");
		sql.append("          THEN '1' ");
		sql.append("          WHEN uf2 <> uf1 ");
		sql.append("          THEN '2' ");
		sql.append("        END ");
		sql.append("      FROM ");
		sql.append("        (SELECT ufi.sg_unidade_federativa AS uf1 ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          endereco_pessoa ep, ");
		sql.append("          pessoa pe, ");
		sql.append("          municipio mui, ");
		sql.append("          unidade_federativa ufi ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND ufi.id_unidade_federativa = mui.id_unidade_federativa ");
		sql.append("        AND mui.id_municipio          = ep.id_municipio ");
		sql.append("        AND ep.id_endereco_pessoa     = pe.id_endereco_pessoa ");
		sql.append("        AND pe.id_pessoa              = ds.id_filial_origem ");
		sql.append("        ) , ");
		sql.append("        (SELECT uf.sg_unidade_federativa AS uf2");
		sql.append("			FROM docto_servico ds, ");
		sql.append(" 		 		pessoa pe, ");
		sql.append("  			devedor_doc_serv dev, ");
		sql.append("  			endereco_pessoa ep, ");
		sql.append("  			municipio mu, ");
		sql.append("  			unidade_federativa uf ");
		sql.append("		WHERE ds.id_docto_servico  = :idDoctoServico ");
		sql.append("			AND dev.id_docto_servico     = ds.id_docto_servico ");
		sql.append("			AND pe.id_pessoa             = dev.id_cliente ");
		sql.append("			AND ep.id_endereco_pessoa    = pe.id_endereco_pessoa ");
		sql.append("			AND mu.id_municipio          = ep.id_municipio ");
		sql.append("			AND uf.id_unidade_federativa = mu.id_unidade_federativa");		
		sql.append("        ) ");
		sql.append("      ) ");
		sql.append(" 	 END 					AS idlocaldestinooperacao, ");
		sql.append("    'UN'                    AS uncomercial, ");
		sql.append("    '1'                     AS qtcomercial, ");
		sql.append("    ds.vl_total_doc_servico AS vlunitariocomercializacao, ");
		sql.append("    ds.vl_total_doc_servico AS vltotalbrutoprodutoservico, ");
		sql.append("    'UN'                    AS untributavel, ");
		sql.append("    '1'                     AS qttributavel, ");
		sql.append("    ds.vl_total_doc_servico AS vlunitariotributacao, ");
		sql.append("    '1'                     AS tptotalnfe, ");
		sql.append("    ds.vl_total_doc_servico AS vlbasecalculoissqn, ");
		sql.append("    ise.pc_aliquota         AS vlaliquotaissqn, ");
		sql.append("    ise.vl_imposto          AS vlissqn, ");
		sql.append("    CASE ");
		sql.append("      WHEN ise.bl_retencao_tomador_servico = 'S' ");
		sql.append("      THEN 'R' ");
		sql.append("      WHEN ise.bl_retencao_tomador_servico <> 'S' ");
		sql.append("      THEN 'N' ");
		sql.append("    END AS idtributacaoissqn, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'LISTA_SERVICO_RPST' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ds_conteudo ");
		sql.append("        FROM parametro_geral ");
		sql.append("        WHERE nm_parametro_geral = 'LISTA_SERVICO_RPSS' ");
		sql.append("        ) ");
		sql.append("    END AS idlistaservicos ");
		sql.append("  FROM docto_servico ds , ");
		sql.append("    serv_adicional_doc_serv se, ");
		sql.append("    servico_adicional sa, ");
		sql.append("    imposto_servico ise ");
		sql.append("  WHERE ds.id_docto_servico      = :idDoctoServico ");
		sql.append("  AND se.id_docto_servico(+)     = ds.id_docto_servico ");
		sql.append("  AND sa.id_servico_adicional(+) = se.id_servico_adicional ");
		sql.append("  AND (ise.id_conhecimento       = ds.id_docto_servico ");
		sql.append("  OR ise.id_nota_fiscal_servico  = ds.id_docto_servico) ");
		sql.append("  AND ise.tp_imposto             = 'IS' ");
		sql.append("  ), ");
		sql.append("  (SELECT ds.vl_total_doc_servico AS vltotalservicos, ");
		sql.append("    ds.vl_total_doc_servico       AS vlbasecalculoiss, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          imposto_servico ise ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND (ise.id_conhecimento      = ds.id_docto_servico ");
		sql.append("        OR ise.id_nota_fiscal_servico = ds.id_docto_servico) ");
		sql.append("        AND ise.tp_imposto            = 'IS' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM imposto_servico ise ");
		sql.append("        WHERE ise.id_nota_fiscal_servico = :idDoctoServico ");
		sql.append("        AND ise.tp_imposto               = 'IS' ");
		sql.append("        ) ");
		sql.append("    END AS vltotaliss, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          imposto_servico ise ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND (ise.id_conhecimento      = ds.id_docto_servico ");
		sql.append("        OR ise.id_nota_fiscal_servico = ds.id_docto_servico) ");
		sql.append("        AND ise.tp_imposto            = 'PI' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM imposto_servico ise ");
		sql.append("        WHERE ise.id_nota_fiscal_servico = :idDoctoServico ");
		sql.append("        AND ise.tp_imposto               = 'PI' ");
		sql.append("        ) ");
		sql.append("    END AS vlpisservicos, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          imposto_servico ise ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND (ise.id_conhecimento      = ds.id_docto_servico ");
		sql.append("        OR ise.id_nota_fiscal_servico = ds.id_docto_servico) ");
		sql.append("        AND ise.tp_imposto            = 'CO' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM imposto_servico ise ");
		sql.append("        WHERE ise.id_nota_fiscal_servico = :idDoctoServico ");
		sql.append("        AND ise.tp_imposto               = 'CO' ");
		sql.append("        ) ");
		sql.append("    END           AS vlcofinsservicos, ");
		sql.append("    ds.dh_emissao AS dhprestacaoservico, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          imposto_servico ise ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND (ise.id_conhecimento      = ds.id_docto_servico ");
		sql.append("        OR ise.id_nota_fiscal_servico = ds.id_docto_servico) ");
		sql.append("        AND ise.tp_imposto            = 'IS' ");
		sql.append("        ) ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN ");
		sql.append("        (SELECT ise.vl_imposto ");
		sql.append("        FROM imposto_servico ise ");
		sql.append("        WHERE ise.id_nota_fiscal_servico = :idDoctoServico ");
		sql.append("        AND ise.tp_imposto               = 'IS' ");
		sql.append("        ) ");
		sql.append("    END AS vltotalretencaooiss, ");
		sql.append("    '2' AS idregimeespecialtributacao, ");
		sql.append("    CASE ");
		sql.append("      WHEN ds.tp_documento_servico = 'NTE' ");
		sql.append("      THEN ");
		sql.append("        CASE ");
		sql.append("          WHEN co.tp_frete = 'C' ");
		sql.append("          THEN '0' ");
		sql.append("          WHEN co.tp_frete = 'F' ");
		sql.append("          THEN '1' ");
		sql.append("        END ");
		sql.append("      WHEN ds.tp_documento_servico = 'NSE' ");
		sql.append("      THEN '9' ");
		sql.append("    END AS tpmodalidadefrete ");
		sql.append("  FROM docto_servico ds, ");
		sql.append("    conhecimento co ");
		sql.append("  WHERE ds.id_docto_servico = :idDoctoServico ");
		sql.append("  AND co.id_conhecimento(+) = ds.id_docto_servico ");
		sql.append("  ), ");
		sql.append("  (SELECT rce.nr_rota  AS rpsnrrotaentrega, ");
		sql.append("    ds.dt_prev_entrega AS rpsotd, ");
		sql.append("    pe.nm_pessoa       AS rpsnmpessoaremetente, ");
		sql.append("    vi18n(tpl.ds_tipo_logradouro_i) ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_endereco ");
		sql.append("    || ', ' ");
		sql.append("    || ep.nr_endereco ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_complemento ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_bairro           AS rpsenderecoremetente, ");
		sql.append("    ep.nr_cep                 AS rpscepremetente, ");
		sql.append("    mu.nm_municipio           AS nmMunicipioremetente, ");
		sql.append("    uf.sg_unidade_federativa  AS rpsSiglaUfRemetente, ");
		sql.append("    pe.nr_identificacao       AS rpsCpfCnpjRemetente, ");
		sql.append("    pe.nr_inscricao_municipal AS rpsimremetente ");
		sql.append("  FROM docto_servico ds, ");
		sql.append("    rota_coleta_entrega rce, ");
		sql.append("    pessoa pe, ");
		sql.append("    endereco_pessoa ep, ");
		sql.append("    tipo_logradouro tpl, ");
		sql.append("    municipio mu, ");
		sql.append("    unidade_federativa uf ");
		sql.append("  WHERE ds.id_docto_servico         = :idDoctoServico ");
		sql.append("  AND rce.id_rota_coleta_entrega(+) = ds.id_rota_coleta_entrega_sugerid ");
		sql.append("  AND pe.id_pessoa                  = ds.id_cliente_remetente ");
		sql.append("  AND ep.id_endereco_pessoa         = pe.id_endereco_pessoa ");
		sql.append("  AND tpl.id_tipo_logradouro        = ep.id_tipo_logradouro ");
		sql.append("  AND mu.id_municipio               = ep.id_municipio ");
		sql.append("  AND uf.id_unidade_federativa      = mu.id_unidade_federativa ");
		sql.append("  ) , ");
		sql.append("  (SELECT pe.nm_pessoa AS rpsnmpessoadestinatario, ");
		sql.append("    vi18n(tpl.ds_tipo_logradouro_i) ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_endereco ");
		sql.append("    || ', ' ");
		sql.append("    || ep.nr_endereco ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_complemento ");
		sql.append("    || ' ' ");
		sql.append("    || ep.ds_bairro           AS rpsenderecodestinatario, ");
		sql.append("    ep.nr_cep                 AS rpscepdestinatario, ");
		sql.append("    mu.nm_municipio           AS rpsmunicipiodestinatario, ");
		sql.append("    uf.sg_unidade_federativa  AS rpsSiglaUfDestinatario, ");
		sql.append("    pe.nr_identificacao       AS rpscpfcnpjdestinatario, ");
		sql.append("    pe.nr_inscricao_municipal AS rpsimdestinatario ");
		sql.append("  FROM docto_servico ds, ");
		sql.append("    pessoa pe, ");
		sql.append("    endereco_pessoa ep, ");
		sql.append("    tipo_logradouro tpl, ");
		sql.append("    municipio mu, ");
		sql.append("    unidade_federativa uf ");
		sql.append("  WHERE ds.id_docto_servico    = :idDoctoServico ");
		sql.append("  AND pe.id_pessoa             = ds.id_cliente_destinatario ");
		sql.append("  AND ep.id_endereco_pessoa    = pe.id_endereco_pessoa ");
		sql.append("  AND tpl.id_tipo_logradouro   = ep.id_tipo_logradouro ");
		sql.append("  AND mu.id_municipio          = ep.id_municipio ");
		sql.append("  AND uf.id_unidade_federativa = mu.id_unidade_federativa ");
		sql.append("  ) , ");
		sql.append("  (SELECT nfc.nr_nota_fiscal AS rpsNotasFiscaisOriginarias, ");
		sql.append("    co.ds_endereco_entrega ");
		sql.append("    || ' ' ");
		sql.append("    || co.ds_bairro_entrega ");
		sql.append("    || ' ' ");
		sql.append("    || mu.nm_municipio ");
		sql.append("    || ' ' ");
		sql.append("    || uf.sg_unidade_federativa AS rpslocalentrega, ");
		sql.append("    doc.ps_cubado_declarado     AS rpspesodeclarado, ");
		sql.append("    doc.ps_aferido              AS rpspesoaferido, ");
		sql.append("    doc.ps_aforado              AS rpspesocubado, ");
		sql.append("    doc.ps_referencia_calculo   AS rpspesofaturado, ");
		sql.append("    (SELECT ");
		sql.append("      CASE ");
		sql.append("        WHEN ds.id_divisao_cliente IS NOT NULL ");
		sql.append("        THEN ");
		sql.append("          CASE ");
		sql.append("            WHEN (SELECT nvl(tdc.nr_fator_cubagem,0) ");
		sql.append("              FROM tabela_divisao_cliente tdc ");
		sql.append("              WHERE tdc.id_divisao_cliente = ds.id_divisao_cliente) > 0 ");
		sql.append("            THEN ds.ps_aforado / ");
		sql.append("              (SELECT tdc.nr_fator_cubagem ");
		sql.append("              FROM tabela_divisao_cliente tdc ");
		sql.append("              WHERE tdc.id_divisao_cliente = ds.id_divisao_cliente ");
		sql.append("              ) ");
		sql.append("            WHEN (SELECT nvl(tdc.nr_fator_cubagem,0) ");
		sql.append("              FROM tabela_divisao_cliente tdc ");
		sql.append("              WHERE tdc.id_divisao_cliente = ds.id_divisao_cliente) = 0 ");
		sql.append("            THEN ");
		sql.append("              CASE ");
		sql.append("                WHEN (SELECT se.tp_modal FROM servico se WHERE se.id_servico = ds.id_servico) = 'R' ");
		sql.append("                THEN ds.ps_aforado / ");
		sql.append("                  (SELECT ds_conteudo ");
		sql.append("                  FROM parametro_geral ");
		sql.append("                  WHERE nm_parametro_geral = 'FATOR_CUBAGEM_PADRAO_RODO' ");
		sql.append("                  ) ");
		sql.append("                WHEN (SELECT se.tp_modal FROM servico se WHERE se.id_servico = ds.id_servico) = 'A' ");
		sql.append("                THEN ds.ps_aforado / ");
		sql.append("                  (SELECT ds_conteudo ");
		sql.append("                  FROM parametro_geral ");
		sql.append("                  WHERE nm_parametro_geral = 'FATOR_CUBAGEM_PADRAO_AEREO' ");
		sql.append("                  ) ");
		sql.append("              END ");
		sql.append("          END ");
		sql.append("        WHEN ds.id_divisao_cliente IS NULL ");
		sql.append("        THEN ");
		sql.append("          CASE ");
		sql.append("            WHEN (SELECT se.tp_modal FROM servico se WHERE se.id_servico = ds.id_servico) = 'R' ");
		sql.append("            THEN ds.ps_aforado / ");
		sql.append("              (SELECT ds_conteudo ");
		sql.append("              FROM parametro_geral ");
		sql.append("              WHERE nm_parametro_geral = 'FATOR_CUBAGEM_PADRAO_RODO' ");
		sql.append("              ) ");
		sql.append("            WHEN (SELECT se.tp_modal FROM servico se WHERE se.id_servico = ds.id_servico) = 'A' ");
		sql.append("            THEN ds.ps_aforado / ");
		sql.append("              (SELECT ds_conteudo ");
		sql.append("              FROM parametro_geral ");
		sql.append("              WHERE nm_parametro_geral = 'FATOR_CUBAGEM_PADRAO_AEREO' ");
		sql.append("              ) ");
		sql.append("          END ");
		sql.append("      END AS n_cubagem ");
		sql.append("    FROM docto_servico ds ");
		sql.append("    WHERE ds.id_docto_servico = :idDoctoServico ");
		sql.append("    )                               AS rpscubagem, ");
		sql.append("    doc.qt_volumes                  AS rpsvolumes, ");
		sql.append("    vi18n(np.ds_natureza_produto_i) AS rpsnatureza, ");
		sql.append("    doc.vl_mercadoria               AS vlmercadoria, ");
		sql.append("    ise.bl_retencao_tomador_servico AS issqnretido, ");
		sql.append("    doc.vl_frete_liquido            AS vlliquido, ");
		sql.append("    doc.tp_documento_servico        AS tpDocumento ");
		sql.append("  FROM conhecimento co , ");
		sql.append("    docto_servico doc, ");
		sql.append("    nota_fiscal_conhecimento nfc, ");
		sql.append("    unidade_federativa uf, ");
		sql.append("    municipio mu, ");
		sql.append("    imposto_servico ise, ");
		sql.append("    natureza_produto np ");
		sql.append("  WHERE doc.id_docto_servico      = :idDoctoServico ");
		sql.append("  AND co.id_conhecimento(+)       = doc.id_docto_servico ");
		sql.append("  AND nfc.id_conhecimento(+)      = co.id_conhecimento ");
		sql.append("  AND mu.id_municipio(+)          = co.id_municipio_entrega ");
		sql.append("  AND uf.id_unidade_federativa(+) = mu.id_unidade_federativa ");
		sql.append("  AND np.id_natureza_produto(+)   = co.id_natureza_produto ");
		sql.append("  AND (ise.id_conhecimento        = doc.id_docto_servico ");
		sql.append("  OR ise.id_nota_fiscal_servico   = doc.id_docto_servico) ");
		sql.append("  AND ise.tp_imposto              = 'IS' ");
		sql.append("  )");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) { 
    			
    			sqlQuery.addScalar("idEmitenteUfNfe", Hibernate.STRING);
    			sqlQuery.addScalar("naturezaOperacao", Hibernate.STRING);
    			sqlQuery.addScalar("tpFormaPagamento", Hibernate.STRING);
    			sqlQuery.addScalar("tpModeloDocumentoFiscal", Hibernate.INTEGER);
    			sqlQuery.addScalar("serieDocumentoFiscal", Hibernate.STRING);
    			sqlQuery.addScalar("nrDocumentoFiscal", Hibernate.STRING);
    			sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
    			sqlQuery.addScalar("tipoOperacao", Hibernate.INTEGER);
    			sqlQuery.addScalar("idLocalDestinoOperacao", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpFormatoImpressaoDanfe", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpFormaEmissaoNfe", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpAmbiente", Hibernate.STRING);
    			sqlQuery.addScalar("finalidadeNfe", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpIndicadorOperacao", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpProcessoEmissaoNfe", Hibernate.INTEGER);
    			sqlQuery.addScalar("cpfCnpjEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("nmPessoaEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("tpPessoaEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("nmFantasia", Hibernate.STRING);
    			sqlQuery.addScalar("ieEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("imEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("cnaeFiscal", Hibernate.STRING);
    			sqlQuery.addScalar("idRegimeTributarioEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("logradouroEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("numeroEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("complementoEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("bairroEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("idMunicipioEmitente", Hibernate.INTEGER);
    			sqlQuery.addScalar("nmMunicipioEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("siglaUfEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("idCepEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("idPaisEmitente", Hibernate.INTEGER);
    			sqlQuery.addScalar("nmPaisEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("telefoneEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("cpfCnpjDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("nmPessoaDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("indicadorIeDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("ieDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("inscricaoSuframa", Hibernate.STRING);
    			sqlQuery.addScalar("imTomador", Hibernate.STRING);
    			sqlQuery.addScalar("emailDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("tpPessoaDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("logradouroDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("numeroDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("complementoDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("bairroDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("idMunicipioDestinatario", Hibernate.INTEGER);
    			sqlQuery.addScalar("nmMunicipioDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("siglaUfDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("idCepDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("idPaisDestinatario", Hibernate.INTEGER);
    			sqlQuery.addScalar("nmPaisDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("telefoneDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("idProdutoServico", Hibernate.STRING);
    			sqlQuery.addScalar("descProdutoServico", Hibernate.STRING);
    			sqlQuery.addScalar("unComercial", Hibernate.STRING);
    			sqlQuery.addScalar("qtComercial", Hibernate.INTEGER);
    			sqlQuery.addScalar("vlUnitarioComercializacao", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlTotalBrutoProdutoServico", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("unTributavel", Hibernate.STRING);
    			sqlQuery.addScalar("qtTributavel", Hibernate.INTEGER);
    			sqlQuery.addScalar("vlUnitarioTributacao", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("tpTotalNfe", Hibernate.INTEGER);
    			sqlQuery.addScalar("vlBaseCalculoISSQN", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlAliquotaISSQN", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlISSQN", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("idListaServicos", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("idTributacaoISSQN", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalServicos", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlBaseCalculoISS", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlTotalISS", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlPisServicos", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlCofinsServicos", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("dhPrestacaoServico", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("vlTotalRetencaooISS", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("idRegimeEspecialTributacao", Hibernate.INTEGER);
    			sqlQuery.addScalar("tpModalidadeFrete", Hibernate.INTEGER);    			
    			sqlQuery.addScalar("rpsNrRotaEntrega", Hibernate.STRING);
    			sqlQuery.addScalar("rpsOtd", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("rpsNmPessoaRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsEnderecoRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsCepRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsSiglaUfRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("nmMunicipioRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsCpfCnpjRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsImRemetente", Hibernate.STRING);
    			sqlQuery.addScalar("rpsNmPessoaDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsEnderecoDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsCepDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsMunicipioDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsSiglaUfDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsCpfCnpjDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsImDestinatario", Hibernate.STRING);
    			sqlQuery.addScalar("rpsNotasFiscaisOriginarias", Hibernate.STRING);
    			sqlQuery.addScalar("rpsLocalEntrega", Hibernate.STRING);
    			sqlQuery.addScalar("rpsPesoDeclarado", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsPesoAferido", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsPesoCubado", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsPesoFaturado", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsCubagem", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsVolumes", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("rpsNatureza", Hibernate.STRING);    			
    			sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("issqnRetido", Hibernate.STRING);
    			sqlQuery.addScalar("vlLiquido", Hibernate.BIG_DECIMAL);    			
    			sqlQuery.addScalar("tpDocumento", Hibernate.STRING);
    			sqlQuery.addScalar("observacao", Hibernate.STRING);    			
      		}    		
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idDoctoServico", idDoctoServico);
    	parameters.put("idFilial", idFilial);
    	
		return (List<Map<String, Object>>) getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq, Transformers.ALIAS_TO_ENTITY_MAP);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findCancNotaFiscalConjugada(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT '2.00'    AS versao,");
		sql.append("  'CANCELAMENTO' AS tpoperacao,");
		sql.append("  'ID' || mo.nr_chave  AS idChave,");
		sql.append("  pg.ds_conteudo  AS tpAmbiente,");
		sql.append("  'CANCELAR'      AS nmServico,");
		sql.append("  mo.nr_chave     AS nrChaveNFE,");
		sql.append("  mo.nr_protocolo AS nrProtocoloAutorizacao,");
		sql.append("  pg1.ds_conteudo AS nmJustificativa");
		sql.append(" FROM parametro_geral pg,");
		sql.append("  parametro_geral pg1,");
		sql.append("  monitoramento_doc_eletronico mo");
		sql.append(" WHERE pg.nm_parametro_geral = 'AMBIENTE_NFE_CONJUGADA'");
		sql.append("  AND pg1.nm_parametro_geral = 'JUST_CANC_NFE_CONJUGADA'");
		sql.append("  AND id_docto_servico       = :idDoctoServico");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
    			sqlQuery.addScalar("versao", Hibernate.STRING);
    			sqlQuery.addScalar("tpOperacao", Hibernate.STRING);
    			sqlQuery.addScalar("idChave", Hibernate.STRING);
    			sqlQuery.addScalar("tpAmbiente", Hibernate.STRING);
    			sqlQuery.addScalar("nmServico", Hibernate.STRING);
    			sqlQuery.addScalar("nrChaveNFE", Hibernate.STRING);
    			sqlQuery.addScalar("nrProtocoloAutorizacao", Hibernate.STRING);
    			sqlQuery.addScalar("nmJustificativa", Hibernate.STRING);
      		}    		
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idDoctoServico", idDoctoServico);
    	
		return (List<Map<String, Object>>) getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq, Transformers.ALIAS_TO_ENTITY_MAP);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findInutNotaFiscalConjugada(Long idDoctoServico, Long idFilial) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'ID' || q.idUfSolicitante  || TO_CHAR(SYSDATE,'YY') || q.cnpjEmitente || q.nrModeloNfe || LPAD(q.nrserienfe,3,0) || LPAD(q.nrInicialNfe,9,0) || LPAD(q.nrFinalNfe,9,0) AS idChave, q.*");
		sql.append(" FROM");
		sql.append("  (SELECT '3.10'            AS versao,");
		sql.append("    'INUTILIZAÇÃO'          AS tpoperacao,");
		sql.append("    pg.ds_conteudo          AS tpAmbiente,");
		sql.append("    'INUTILIZAR'            AS nmServico,");
		sql.append("    uf.nr_ibge              AS idUfSolicitante,");
		sql.append("    TO_CHAR(SYSDATE,'YY') AS anoNumeracao,");
		sql.append("    pe.nr_identificacao     AS cnpjEmitente,");
		sql.append("    '55'                    AS nrModeloNfe,");
		sql.append("    (SELECT vl_conteudo_parametro_filial");
		sql.append("    FROM conteudo_parametro_filial");
		sql.append("    WHERE id_parametro_filial IN");
		sql.append("      (SELECT id_parametro_filial");
		sql.append("      FROM parametro_filial");
		sql.append("      WHERE nm_parametro_filial = 'SERIE_NFE_CONJUGADA'");
		sql.append("      )");
		sql.append("    AND id_filial = :idFilial");
		sql.append("    ) AS nrSerieNfe,");
		sql.append("    CASE");
		sql.append("      WHEN doc.tp_documento_servico = 'NTE'");
		sql.append("      THEN");
		sql.append("        (SELECT co.nr_conhecimento");
		sql.append("        FROM conhecimento co");
		sql.append("        WHERE co.id_conhecimento = doc.id_docto_servico");
		sql.append("        )");
		sql.append("      WHEN doc.tp_documento_servico = 'NSE'");
		sql.append("      THEN");
		sql.append("        (SELECT nr_nota_fiscal_servico");
		sql.append("        FROM nota_fiscal_servico");
		sql.append("        WHERE id_nota_fiscal_servico = :idDoctoServico");
		sql.append("        )");
		sql.append("    END AS nrInicialNfe,");
		sql.append("    CASE");
		sql.append("      WHEN doc.tp_documento_servico = 'NTE'");
		sql.append("      THEN");
		sql.append("        (SELECT co.nr_conhecimento");
		sql.append("        FROM conhecimento co");
		sql.append("        WHERE co.id_conhecimento = :idDoctoServico");
		sql.append("        )");
		sql.append("      WHEN doc.tp_documento_servico = 'NSE'");
		sql.append("      THEN");
		sql.append("        (SELECT nr_nota_fiscal_servico");
		sql.append("        FROM nota_fiscal_servico");
		sql.append("        WHERE id_nota_fiscal_servico =  :idDoctoServico");		
		sql.append("        )");
		sql.append("    END             AS nrFinalNfe,");
		sql.append("    pg1.ds_conteudo AS nmJustificativa");
		sql.append("  FROM parametro_geral pg,");
		sql.append("    parametro_geral pg1 ,");
		sql.append("    unidade_federativa uf,");
		sql.append("    docto_servico doc,");
		sql.append("    municipio mu,");
		sql.append("    endereco_pessoa ep,");
		sql.append("    pessoa pe");
		sql.append("  WHERE pg.nm_parametro_geral  = 'AMBIENTE_NFE_CONJUGADA'");
		sql.append("  AND pg1.nm_parametro_geral   = 'JUST_CANC_NFE_CONJUGADA'");
		sql.append("  AND doc.id_docto_servico     = :idDoctoServico");
		sql.append("  AND uf.id_unidade_federativa = mu.id_unidade_federativa");
		sql.append("  AND mu.id_municipio          = ep.id_municipio");
		sql.append("  AND ep.id_endereco_pessoa    = pe.id_endereco_pessoa");
		sql.append("  AND pe.id_pessoa             = doc.id_filial_origem");
		sql.append("  ) q");
				
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
    			sqlQuery.addScalar("versao", Hibernate.STRING);
    			sqlQuery.addScalar("tpOperacao", Hibernate.STRING);
    			sqlQuery.addScalar("idChave", Hibernate.STRING);
    			sqlQuery.addScalar("tpAmbiente", Hibernate.STRING);
    			sqlQuery.addScalar("nmServico", Hibernate.STRING);
    			sqlQuery.addScalar("idUfSolicitante", Hibernate.STRING);
    			sqlQuery.addScalar("anoNumeracao", Hibernate.STRING);
    			sqlQuery.addScalar("cnpjEmitente", Hibernate.STRING);
    			sqlQuery.addScalar("nrModeloNfe", Hibernate.STRING);
    			sqlQuery.addScalar("nrSerieNfe", Hibernate.STRING);
    			sqlQuery.addScalar("nrInicialNfe", Hibernate.STRING);
    			sqlQuery.addScalar("nrFinalNfe", Hibernate.STRING);
    			sqlQuery.addScalar("nmJustificativa", Hibernate.STRING);
      		}    		
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idDoctoServico", idDoctoServico);
    	parameters.put("idFilial", idFilial);
    	
		return (List<Map<String, Object>>) getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq, Transformers.ALIAS_TO_ENTITY_MAP);
	}	
	
	public void storeEnvioMonitoramento(Long idMonitoramento, String documentData, String nrChave) {
		StringBuilder sql = new StringBuilder()
		.append("UPDATE monitoramento_doc_eletronico")
		.append(" SET DS_DADOS_DOCUMENTO = :documentData,")
		.append(" NR_CHAVE = :nrChave")
		.append(" WHERE ID_MONITORAMENTO_DOC_ELETRONIC = :idMonitoramento");
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idMonitoramento", idMonitoramento);
		parametersValues.put("documentData", documentData.getBytes(Charset.forName(UTF_8)));
		parametersValues.put("nrChave", nrChave);	
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	@SuppressWarnings("rawtypes")
	public List findDocumentData(String nrChave) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("mde.dsDadosDocumento");
		hql.addFrom(MonitoramentoDocEletronico.class.getName(), "mde");
		hql.addCriteria("mde.nrChave", "=", nrChave);
        
        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findDocsNFEConjugada(final List<Long> idMonitoramentoEletronico) {		
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("mde.dsDadosDocumento");
		hql.addProjection("mde.tpSituacaoDocumento");
		hql.addFrom(MonitoramentoDocEletronico.class.getName(), "mde");
		hql.addCriteriaIn("mde.tpSituacaoDocumento", new String[] { "A", "R" });
		hql.addCriteriaIn("mde.idMonitoramentoDocEletronic", idMonitoramentoEletronico);
        
        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Short findNrCFOP(Long idDoctoServico){
		StringBuilder sql = new StringBuilder();		
		sql.append("SELECT DISTINCT ( ");
		sql.append("  CASE ");
		sql.append("    WHEN ds.tp_documento_servico = 'NSE' or ds.tp_documento_servico = 'NTE' ");
		sql.append("    THEN ");
		sql.append("      (SELECT ");
		sql.append("        CASE ");
		sql.append("          WHEN uf2 = uf1 ");
		sql.append("          THEN '5933' ");
		sql.append("          WHEN uf2 <> uf1 ");
		sql.append("          THEN '6933' ");
		sql.append("        END ");
		sql.append("      FROM ");
		sql.append("        (SELECT ufi.sg_unidade_federativa AS uf1 ");
		sql.append("        FROM docto_servico ds , ");
		sql.append("          endereco_pessoa ep, ");
		sql.append("          pessoa pe, ");
		sql.append("          municipio mui, ");
		sql.append("          unidade_federativa ufi ");
		sql.append("        WHERE ds.id_docto_servico     = :idDoctoServico ");
		sql.append("        AND ufi.id_unidade_federativa = mui.id_unidade_federativa ");
		sql.append("        AND mui.id_municipio          = ep.id_municipio ");
		sql.append("        AND ep.id_endereco_pessoa     = pe.id_endereco_pessoa ");
		sql.append("        AND pe.id_pessoa              = ds.id_filial_origem ");
		sql.append("        ) , ");
		sql.append("        (SELECT uf.sg_unidade_federativa AS uf2");
		sql.append("			FROM docto_servico ds, ");
		sql.append(" 		 		pessoa pe, ");
		sql.append("  			devedor_doc_serv dev, ");
		sql.append("  			endereco_pessoa ep, ");
		sql.append("  			municipio mu, ");
		sql.append("  			unidade_federativa uf ");
		sql.append("		WHERE ds.id_docto_servico  = :idDoctoServico ");
		sql.append("			AND dev.id_docto_servico     = ds.id_docto_servico ");
		sql.append("			AND pe.id_pessoa             = dev.id_cliente ");
		sql.append("			AND ep.id_endereco_pessoa    = pe.id_endereco_pessoa ");
		sql.append("			AND mu.id_municipio          = ep.id_municipio ");
		sql.append("			AND uf.id_unidade_federativa = mu.id_unidade_federativa");		
		sql.append("        ) ");
		sql.append("      ) ");
		sql.append("  END) AS idcfop ");
		sql.append("FROM docto_servico ds ");
		sql.append("WHERE ds.id_docto_Servico = :idDoctoServico");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
    			sqlQuery.addScalar("idcfop", Hibernate.SHORT);
      		}    		
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idDoctoServico", idDoctoServico);
    	
		List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
		
		if(!object.isEmpty()){
			return (Short) object.get(0);
		}
		
		return null;
	}
	
	
	public Long findNumeroDocumentoFiscal(long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
			.append(" SELECT DISTINCT ")
			.append("   CASE ")
			.append("       WHEN ds.tp_documento_servico = 'NTE' ")
			.append("       THEN( ")
			.append("            SELECT co.nr_conhecimento ")
			.append("              FROM conhecimento co ")
			.append("             WHERE co.id_conhecimento = ds.id_docto_servico ")
			.append("           ) ")
			.append("       WHEN ds.tp_documento_servico = 'NSE' ")
			.append("       THEN( ")
			.append("            SELECT nf.nr_nota_fiscal_servico ")
			.append("              FROM nota_fiscal_servico nf ")
			.append("             WHERE nf.id_nota_fiscal_servico = ds.id_docto_servico ")
			.append("           ) ")
			.append("   END as nr_documento_fiscal ")
			.append("  FROM docto_servico ds ")
			.append(" WHERE ds.id_docto_servico  = :idDoctoServico ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
    			sqlQuery.addScalar("nr_documento_fiscal", Hibernate.LONG);
      		}    		
    	};
    	
    	Map<String, Object> parameters = new HashMap<>();
    	parameters.put("idDoctoServico", idDoctoServico);
    	
		List<?> retorno = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
		
		if(!retorno.isEmpty()) {
			return (Long) retorno.get(0);
		}
		return null;
	}

	public boolean existsNfeByNrCnf(String cnf) {
        StringBuilder sql = new StringBuilder()
        	.append(" SELECT 1 ")
        	.append("   FROM monitoramento_doc_eletronico ")
        	.append("  WHERE nr_cnf = :cnf ")
        	.append("    AND rownum <= 1");
        
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("cnf", cnf);
		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters);
        return CompareUtils.gt(qtdRows, IntegerUtils.ZERO);        
	}
}