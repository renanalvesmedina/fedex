package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.RecalculoFrete;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.LongUtils;

public class RecalculoFreteFlexDAO extends JdbcDaoSupport {

	public Long findIdPessoa(String nrIdentificacao) {

		String sql = "SELECT ID_PESSOA FROM PESSOA WHERE NR_IDENTIFICACAO = ? ";

		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		List list =  jdbcTemplate.queryForList(sql, new Object[]{nrIdentificacao});
		if(list != null && !list.isEmpty()){
			ListOrderedMap ls = (ListOrderedMap) list.get(0);
			return LongUtils.getLong(ls.get("id_pessoa"));
		}

		return null;
	}

	public Long findIdMunicipio(Long cdIBGE, Long idUF) {

		String sql = "SELECT ID_MUNICIPIO FROM MUNICIPIO WHERE CD_IBGE = ? AND ID_UNIDADE_FEDERATIVA = ? ";

		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		List list = jdbcTemplate.queryForList(sql, new Object[]{cdIBGE,idUF});
		if(list != null && !list.isEmpty()){
			ListOrderedMap ls = (ListOrderedMap) list.get(0);
			return LongUtils.getLong(ls.get("id_municipio"));
		}

		return null;
	}

	public Long findIdFilial(Long idMunicipio) {

		String sql = "SELECT ID_FILIAL FROM MUNICIPIO_FILIAL WHERE ID_MUNICIPIO = ? ";

		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		List list = jdbcTemplate.queryForList(sql, new Object[]{idMunicipio});
		if(list != null && !list.isEmpty()){
			ListOrderedMap ls = (ListOrderedMap) list.get(0);
			return LongUtils.getLong(ls.get("id_filial"));
		}

		return null;
	}

	public List findDocRecalculo(Long nrDocumento){

		String sql = "SELECT NR_DOCUMENTO FROM DOCTO_RECALCULO WHERE NR_DOCUMENTO = ? ";

		return getJdbcTemplate().queryForList(sql, new Object[]{nrDocumento});
	}

	private String getSQLRecalculo(RecalculoFrete rec){

		StringBuilder query = new StringBuilder()
		.append(" select distinct DS.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ")
		.append(getSQL(rec));

		return query.toString();
	}

	private String getSQL(RecalculoFrete rec){

		StringBuilder query = new StringBuilder()

		.append(" from DOCTO_SERVICO DS,  ")
		.append(" CONHECIMENTO C, ")
		.append(" DEVEDOR_DOC_SERV DDS ")
		.append(" where 1=1 and C.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
		.append(" and C.TP_SITUACAO_CONHECIMENTO = 'E' ")
		.append(" and C.BL_EMITIDO_LMS = 'S' ")
		.append(" and DDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");

		if(rec.getTabelaPrecoFiltro() != null && rec.getTabelaPrecoFiltro().getIdTabelaPreco() != null){

			query.append(" and DS.ID_TABELA_PRECO = ").append(rec.getTabelaPrecoFiltro().getIdTabelaPreco());
		}

		if(rec.getTipoTabelaPreco() != null && rec.getTipoTabelaPreco().getIdTipoTabelaPreco() != null){

			query.append(" and DS.ID_TABELA_PRECO in (select TP.ID_TABELA_PRECO ")
				 .append(" from TABELA_PRECO TP ")
				 .append(" where TP.ID_TIPO_TABELA_PRECO = ").append(rec.getTipoTabelaPreco().getIdTipoTabelaPreco())
				 .append(" and TP.BL_EFETIVADA = 'S') ");

		}

		if(rec.getSubtipoTabelaPreco() != null && rec.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco() != null){

			query.append(" and DS.ID_TABELA_PRECO in (select TP.ID_TABELA_PRECO ")
			 	.append(" from TABELA_PRECO TP ")
			 	.append(" where TP.ID_SUBTIPO_TABELA_PRECO = ").append(rec.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco())
			 	.append(" and TP.BL_EFETIVADA = 'S') ");

		}

		if(CollectionUtils.isNotEmpty(rec.getClientesProcessar())){
			///-- se informados clientes a processar
			query.append(" and DDS.ID_CLIENTE in (select CP.ID_CLIENTE ");
			query.append(" from CLIENTE_PROCESSAR CP ");
			query.append(" where CP.ID_RECALCULO_FRETE = ").append(rec.getIdRecalculoFrete()).append(" ) ");
		}

		if(CollectionUtils.isNotEmpty(rec.getClientesNaoProcessar())){
			//-- se informados clientes a NÃO processar
			query.append(" and DDS.ID_CLIENTE not in (select CNP.ID_CLIENTE ");
			query.append(" from CLIENTE_NAO_PROCESSAR CNP ");
		    query.append(" where CNP.ID_RECALCULO_FRETE = ").append(rec.getIdRecalculoFrete()).append(" ) ");
		}

		query.append(" and trunc(cast (ds.DH_EMISSAO as date)) > to_date('").append(rec.getDtInicial()).append("' , 'yyyy-mm-dd') ")
			 .append(" and trunc(cast (ds.DH_EMISSAO as date)) < to_date('").append(rec.getDtFinal()).append("' , 'yyyy-mm-dd') ");

		return query.toString();
	}

	public List<ListOrderedMap> findDocsRecalculo(RecalculoFrete rec) {

		String sql = getSQLRecalculo(rec);

		return getJdbcTemplate().queryForList(sql);
	}

}
