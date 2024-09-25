package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte ao Hibernate em conjunto com o Spring. Não inserir documentação após ou remover a tag do XDoclet a
 * seguir.
 * 
 * @author André Valadas
 * @spring.bean
 */
public class CheckReceitaDAO extends JdbcDaoSupport {

	/**
	 * Busca dados para Receita
	 * 
	 * @see http://nt-swdep01:8080/w/index.php?title=Check-Receita_-_Rodado_na_madrugada_de_quarta-feira
	 */
	public List<Map> findCheckReceitaMapped() {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT TO_CHAR(MAX(a.data_emissao), 'DD/MM/YYYY') AS DTEMISSAO, \n");
		sql.append("	a.filial AS SGFILIAL, \n");
		sql.append(" 	a.nr_conhecimento AS NRCONHECIMENTO, \n");
		sql.append("	SUM(a.peso_aferido) AS PSAFERIDO, \n");
		sql.append("	MAX(a.valor_total_ctrc) AS VLTOTAL, \n");
		sql.append("	MAX(a.status) AS STATUS, \n");
		sql.append("	MAX(a.usuario_liberacao) AS USERLIBERACAO, \n");
		sql.append("	MAX(a.usuario_digitador) AS USERDIGITADOR \n");
		sql.append("FROM \n");
		sql.append("(SELECT ds.dh_emissao AS data_emissao, \n");
		sql.append("	flo.sg_filial       AS filial, \n");
		sql.append("  	conh.nr_conhecimento, \n");
		sql.append("  	vnf.nr_sequencia                                                                                                   AS sequencia, \n");
		sql.append("  	vnf.ps_aferido                                                                                                     AS peso_aferido, \n");
		sql.append("  	ds.vl_total_doc_servico                                                                                            AS valor_total_ctrc, \n");
		sql.append("  	ds.vl_icms_st                                                                                                      AS valor_icms, \n");
		sql.append("  	DECODE(conh.tp_situacao_conhecimento, 'E', 'Emitido', 'P', 'Pre-Conhecimento', 'C', 'Cancelado', 'D', 'Duplicado') AS status, \n");
		sql.append("  	usr.nm_usuario                                                                                                     AS usuario_liberacao, \n");
		sql.append("  	usr2.nm_usuario                                                                                                    AS usuario_digitador \n");
		sql.append("FROM conhecimento conh \n");
		sql.append("	INNER JOIN docto_servico ds \n");
		sql.append("		ON (ds.id_docto_servico = conh.id_conhecimento) \n");
		sql.append("	INNER JOIN nota_fiscal_conhecimento nfc \n");
		sql.append("		ON (nfc.id_conhecimento = conh.id_conhecimento) \n");
		sql.append("	INNER JOIN volume_nota_fiscal vnf \n");
		sql.append("		ON (vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento) \n");
		sql.append("	INNER JOIN filial flo \n");
		sql.append("		ON (flo.id_filial = conh.id_filial_origem) \n");
		sql.append("	LEFT JOIN liberacao_doc_serv lib \n");
		sql.append("		ON (ds.id_docto_servico = lib.id_docto_servico) \n");
		sql.append("	LEFT JOIN usuario usr \n");
		sql.append("		ON (lib.id_usuario = usr.id_usuario) \n");
		sql.append("	LEFT JOIN usuario usr2 \n");
		sql.append("		ON (usr2.id_usuario  = ds.id_usuario_inclusao) \n");
		sql.append("WHERE vnf.ps_aferido > 10000 \n");
		sql.append("ORDER BY ds.dh_emissao DESC, \n");
		sql.append("  flo.sg_filial DESC, \n");
		sql.append("  conh.nr_conhecimento DESC, \n");
		sql.append("  vnf.nr_sequencia DESC \n");
		sql.append(") a GROUP BY a.filial, a.nr_conhecimento ORDER BY DTEMISSAO");

		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(getJdbcTemplate().queryForList(sql.toString()));
	}
}