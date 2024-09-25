package com.mercurio.lms.franqueados.model.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;

public class ConhecimentoFranqueadoDAO extends JdbcDaoSupport {

	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return Conhecimento.class;
	}

	@SuppressWarnings("unchecked")
	public List<TypedFlatMap> findDocumentoReembarcado(
			Long idFranquia, YearMonthDay dtInicioCompetencia, 
			YearMonthDay dtFimCompetencia) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  ");
		sql.append(" 	ds.id_docto_servico, ");
		sql.append(" 	ds.ps_real, ");
		sql.append(" 	ma.id_manifesto ");
		sql.append(" FROM ");
		sql.append(" 	manifesto_nacional_cto mc, ");
		sql.append(" 	manifesto_viagem_nacional mvn, ");
		sql.append(" 	manifesto ma, ");
		sql.append(" 	docto_servico ds, ");
		sql.append(" 	filial fo, ");
		sql.append(" 	filial fd ");
		sql.append(" WHERE ");
		sql.append(" 	ma.id_manifesto = mvn.id_manifesto_viagem_nacional ");
		sql.append(" 	and mvn.id_manifesto_viagem_nacional = mc.id_manifesto_viagem_nacional ");
		sql.append(" 	and mc.id_conhecimento = ds.id_docto_servico ");
		sql.append(" 	and ds.id_filial_origem = fo.id_filial ");
		sql.append(" 	and ds.id_filial_destino = fd.id_filial ");
		sql.append(" 	and fo.id_filial_responsavel <> ? ");
		sql.append(" 	and fd.id_filial_responsavel <> ? ");
		sql.append(" 	and ma.id_filial_origem = ? ");
		sql.append(" 	and ma.tp_status_manifesto <> ? ");
		sql.append(" 	and ma.dh_emissao_manifesto between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') ");

	
		List<Object> params = new ArrayList<Object>();
			params.add(idFranquia);
			params.add(idFranquia);
			params.add(idFranquia);
		params.add(ConstantesEntrega.STATUS_MANIFESTO_CANCELADO);
		params.add(dtInicioCompetencia.toString());
		params.add(dtFimCompetencia.toString());

		return (List<TypedFlatMap>)getJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet documentoReembarcado) throws SQLException {

				ResultSetMetaData metaData = documentoReembarcado.getMetaData();
				int colCount = metaData.getColumnCount();
				List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
				while (documentoReembarcado.next()) {
					TypedFlatMap flatMap = new TypedFlatMap();
					for (int i = 1; i <= colCount; i++) {
						flatMap.put(metaData.getColumnLabel(i).toLowerCase(), documentoReembarcado.getObject(i));
					}
					result.add(flatMap);
				}

				return result;
			}
		});

	}

	@SuppressWarnings("unchecked")
	public List<ListOrderedMap> findDadosServicosAdicionaisFranqueado(
			Long idFranquia, YearMonthDay dataInicioCompetencia, 
			YearMonthDay dataFimCompetencia) {
		
		StringBuilder query = new StringBuilder()
		.append(" SELECT /*+ INDEX(ds DSER_PK)*/ ") 
		.append(" 	ds.id_docto_servico, ") 
		.append(" 	ddsf.vl_devido as vl_total_doc_servico, ") 
		.append(" 	ddsf.id_devedor_doc_serv_fat, ") 
		.append(" 	des.id_desconto, ") 
		.append(" 	des.vl_desconto, ") 
		.append(" 	des.id_motivo_desconto, ") 
		.append(" 	des.tp_situacao_aprovacao, ") 
		.append(" 	nvl(ds.vl_mercadoria,0) as vl_mercadoria, ") 
		.append(" 	iss.vl_imposto, ") 
		.append(" 	sads.id_servico_adicional ") 

		.append(" FROM ")
		.append(" 	nota_fiscal_servico nfs, ") 
		.append(" 	docto_servico ds, ") 
		.append(" 	devedor_doc_serv_fat ddsf, ") 
		.append(" 	desconto des, ") 
		.append(" 	imposto_servico iss, ") 
		.append(" 	filial f, ") 
		.append(" 	serv_adicional_doc_serv sads ")
		
		.append(" WHERE ")
		.append(" 	nfs.id_nota_fiscal_servico = ds.id_docto_servico ")
		.append(" 	and nfs.id_nota_fiscal_servico = ddsf.id_docto_servico ")
		.append(" 	and ddsf.id_devedor_doc_serv_fat = des.id_devedor_doc_serv_fat(+) ")
		.append(" 	and nfs.id_nota_fiscal_servico = iss.id_nota_fiscal_servico ")
		.append(" 	and nfs.id_filial_origem = f.id_filial  ")
		.append(" 	and sads.id_docto_servico = ds.id_docto_servico ")
		.append(" 	and f.id_filial_responsavel = ? ")
		.append(" 	and nfs.tp_situacao_nf = ? ")
		.append(" 	and iss.tp_imposto = ? ")
		.append(" 	and trunc(cast(ds.dh_emissao as date)) between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') ");

		List<Object> params = new ArrayList<Object>();
		params.add(idFranquia);
		params.add(ConstantesExpedicao.CD_EMISSAO);
		params.add(ConstantesExpedicao.CD_ISS);
		params.add(dataInicioCompetencia.toString());
		params.add(dataFimCompetencia.toString());

		return getJdbcTemplate().query(query.toString(), params.toArray(), new ColumnMapRowMapper());
	}

	@SuppressWarnings("unchecked")
	public List<ListOrderedMap> findDadosServicosAdicionaisRecalculo(
			Long idFranquia, YearMonthDay dataInicioCompetencia,
			YearMonthDay dataFimCompetencia) {

		StringBuilder query = new StringBuilder()
		.append(" SELECT /*+ INDEX(des DECO_DDSF_FK_I)*/ ") 
		.append(" 	ds.id_docto_servico, ") 
		.append(" 	ddsf.vl_devido as vl_total_doc_servico, ") 
		.append(" 	ddsf.id_devedor_doc_serv_fat, ") 
		.append(" 	des.id_desconto, ") 
		.append(" 	des.vl_desconto, ") 
		.append(" 	des.id_motivo_desconto, ") 
		.append(" 	des.tp_situacao_aprovacao, ") 
		.append(" 	nvl(ds.vl_mercadoria,0) as vl_mercadoria, ") 
		.append(" 	iss.vl_imposto, ") 
		.append(" 	sads.id_servico_adicional, ") 
		.append(" 	dsf.id_docto_servico_frq as id_conhecimento_original, ")
		.append("	dsf.vl_participacao as vl_participacao_original, ")
		.append("	to_char(dsf.dt_competencia,'ddmmyyyy') as dt_competencia ")

		.append(" FROM ")
		.append(" 	nota_fiscal_servico nfs, ") 
		.append(" 	docto_servico ds, ") 
		.append(" 	devedor_doc_serv_fat ddsf, ") 
		.append(" 	desconto des, ") 
		.append(" 	imposto_servico iss, ") 
		.append(" 	filial f, ") 
		.append(" 	serv_adicional_doc_serv sads ")
		.append(" , docto_servico_frq dsf ")
	
		.append(" WHERE ")
		.append(" 	nfs.id_nota_fiscal_servico = ds.id_docto_servico ")
		.append(" 	and nfs.id_nota_fiscal_servico = ddsf.id_docto_servico ")
		.append(" 	and ddsf.id_devedor_doc_serv_fat = des.id_devedor_doc_serv_fat(+) ")
		.append(" 	and nfs.id_nota_fiscal_servico = iss.id_nota_fiscal_servico ")
		.append(" 	and nfs.id_filial_origem = f.id_filial  ")
		.append(" 	and sads.id_docto_servico = ds.id_docto_servico ")
		.append(" 	and f.id_filial_responsavel = ? ")
		.append(" 	and nfs.tp_situacao_nf = ? ")
		.append(" 	and iss.tp_imposto = ? ")
		.append(" 	and dsf.id_franquia = ? ")
		.append(" 	and dsf.id_docto_servico = ds.id_docto_servico ")
		.append(" 	and dsf.dt_competencia < to_date(?,'yyyy-mm-dd') ")
		.append(" 	and ddsf.DT_LIQUIDACAO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') ")
		.append(" 	and des.tp_situacao_aprovacao = ? ");

		List<Object> params = new ArrayList<Object>();
		params.add(idFranquia);
		params.add(ConstantesExpedicao.CD_EMISSAO);
		params.add(ConstantesExpedicao.CD_ISS);
		params.add(idFranquia);
		params.add(dataInicioCompetencia.toString());
		params.add(dataInicioCompetencia.toString());
		params.add(dataFimCompetencia.toString());
		params.add(ConstantesFranqueado.TP_APROVADO);

		return getJdbcTemplate().query(query.toString(), params.toArray(), new ColumnMapRowMapper());
	}

	@SuppressWarnings("unchecked")
	public List<ListOrderedMap> findConhecimentoFranqueado(
			Long idFranquia, YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia,
			Long psMinFreteCarreteiro, List<Long> idsMunicipios) {

		StringBuilder query = new StringBuilder()
		.append(" SELECT ")
		.append("   /*+ INDEX(ds DSER_PK)*/ ")
		.append("   t.id_docto_servico, ")
		.append("   id_filial_origem, ")
		.append("   id_filial_destino, ")
		.append("   tp_frete, ")
		.append("   ddsf.vl_devido AS vl_total_doc_servico, ")
		.append("   ddsf.id_devedor_doc_serv_fat, ")
		.append("   id_desconto, ")
		.append("   vl_desconto, ")
		.append("   id_motivo_desconto, ")
		.append("   tp_situacao_aprovacao, ")
		.append("   vl_mercadoria, ")
		.append("   vl_imposto, ")
		.append("   id_municipio_entrega, ")
		.append("   id_municipio_coleta, ")
		.append("   id_cliente_destinatario, ")
		.append("   id_cliente_remetente, ")
		.append("   nr_distancia, ")
		.append("   custo_carreteiro, ")
		.append("   custo_aereo, ")
		.append("   qt_franquias ")
		.append(" FROM ")
		.append("   (SELECT ds.id_docto_servico, ")
		.append("     con.id_municipio_entrega, ")
		.append("     con.id_municipio_coleta, ")
		.append("     ds.id_cliente_remetente, ")
		.append("     ds.id_cliente_destinatario, ")
		.append("     con.tp_frete, ")
		.append("     fo.id_filial_responsavel AS id_filial_origem, ")
		.append("     fd.id_filial_responsavel AS id_filial_destino, ")
		.append("     ds.vl_mercadoria, ")
		.append("     ds.vl_imposto, ")
		.append("     ds.ps_real, ")
		.append("     ds.dh_emissao, ")
		.append("     (SELECT MAX(ff.nr_distancia) ")
		.append("     FROM fluxo_filial ff ")
		.append("     WHERE ff.id_filial_origem = fo.id_filial_responsavel ")
		.append("     AND ff.id_filial_destino  = fd.id_filial_responsavel ")
		.append("     AND TRUNC(CAST(ds.dh_emissao AS DATE)) BETWEEN ff.dt_vigencia_inicial AND ff.dt_vigencia_final ")
		.append("     ) AS nr_distancia, ")
		.append("   ( CASE ")
		.append("     WHEN ps_real > ? ")
		.append("     THEN f_custo_frete_carreteiro( ds.id_docto_servico ) ")
		.append("     ELSE 0 ")
		.append("   END)                                       AS custo_carreteiro, ")
		.append("   f_custo_frete_aereo( ds.id_docto_servico ) AS custo_aereo, ")
		.append("   (SELECT COUNT(*) FROM franqueado_franquia frf WHERE frf.id_franquia IN (fo.id_filial_responsavel, fd.id_filial_responsavel) ") 
		.append("   AND TRUNC(CAST(ds.dh_emissao AS DATE)) BETWEEN frf.dt_vigencia_inicial AND frf.dt_vigencia_final) as qt_franquias ")
		.append("   FROM conhecimento con, ")
		.append("     docto_servico ds, ")
		.append("     filial fo, ")
		.append("     filial fd ")
		.append("   WHERE con.id_conhecimento        = ds.id_docto_servico ")
		.append("   AND ds.id_filial_origem          = fo.id_filial ")
		.append("   AND ds.id_filial_destino         = fd.id_filial ")
		.append("   AND ( fo.id_filial_responsavel    = ? OR fd.id_filial_responsavel = ? ) ")
		.append("   AND con.tp_situacao_conhecimento = 'E' ")
		.append("   AND TRUNC(CAST(ds.dh_emissao AS DATE)) BETWEEN to_date(?,'yyyy-mm-dd') AND to_date(?,'yyyy-mm-dd') ")
		.append("   ) t, ")
		.append("   devedor_doc_serv_fat ddsf, ")
		.append("   desconto des, ")
		.append("   municipio munc, ")
		.append("   municipio mune, ")
		.append("   cliente clir, ")
		.append("   cliente clid ")
		.append(" WHERE ")
		.append(" t.id_docto_servico           = ddsf.id_docto_servico ")
		.append(" AND ddsf.id_devedor_doc_serv_fat = des.id_devedor_doc_serv_fat(+) ")
		.append(" AND t.id_municipio_entrega       = mune.id_municipio ")
		.append(" AND t.id_municipio_coleta        = munc.id_municipio ")
		.append(" AND t.id_cliente_remetente       = clir.id_cliente ")
		.append(" AND t.id_cliente_destinatario    = clid.id_cliente ")
		.append(" AND ddsf.vl_devido               > 0 ");

		List<Object> params = new ArrayList<Object>();
		params.add(psMinFreteCarreteiro);
		params.add(idFranquia);
		params.add(idFranquia);
		params.add(dtIniCompetencia.toString());
		params.add(dtFimCompetencia.toString());

		if (idsMunicipios != null && !idsMunicipios.isEmpty()) {
			query.append(" AND (mune.id_municipio in (?) OR munc.id_municipio in (?))");
			params.add(idsMunicipios);
			params.add(idsMunicipios);
		}

		return getJdbcTemplate().query(query.toString(), params.toArray(), new ColumnMapRowMapper());
	}

	@SuppressWarnings("unchecked")
	public List<ListOrderedMap> findConhecimentoRecalculo(
			Long idFranquia, YearMonthDay dataInicioCompetencia, 
			YearMonthDay dataFimCompetencia, Long psMinFreteCarreteiro) {

		StringBuilder query = new StringBuilder()
		.append(" SELECT /*+ LEADING(ddsf,con) INDEX(ddsf IDX_DH_LIQUID_01) INDEX(con CTOS_PK) opt_param('_OPTIMIZER_USE_FEEDBACK','FALSE') */ ")
		.append("	ds.id_docto_servico, ")
		.append("	fo.id_filial_responsavel as id_filial_origem, ")
		.append("	fd.id_filial_responsavel as id_filial_destino, ")
		.append("	con.tp_frete, ")
		.append("	ddsf.vl_devido as vl_total_doc_servico, ") 
		.append("	ddsf.id_devedor_doc_serv_fat, ")
		.append("	des.id_desconto, ") 
		.append("	des.vl_desconto, ") 
		.append("	des.id_motivo_desconto, ")
		.append("	des.tp_situacao_aprovacao, ")
		.append("	ds.vl_mercadoria, ")
		.append("	ds.vl_imposto, ")
		.append("	con.id_municipio_entrega, ")
		.append("	con.id_municipio_coleta, ")
		.append("	ds.id_cliente_destinatario, ")
		.append("	ds.id_cliente_remetente, ")
		.append("   (select max(ff.nr_distancia) from fluxo_filial ff where ff.id_filial_origem = fo.id_filial_responsavel ")
		.append("		and ff.id_filial_destino = fd.id_filial_responsavel ")
		.append(" 		and trunc(cast(ds.dh_emissao as date)) between ff.dt_vigencia_inicial and ff.dt_vigencia_final ) as nr_distancia, ")
		.append(" 	(case when ds.ps_real > ? then f_custo_frete_carreteiro( con.id_conhecimento ) else 0 end) as custo_carreteiro, ")
		.append(" 		f_custo_frete_aereo( con.id_conhecimento ) as custo_aereo, ")
		.append(" 	(SELECT COUNT(*) FROM franqueado_franquia frf WHERE frf.id_franquia IN (fo.id_filial_responsavel, fd.id_filial_responsavel) ")
		.append(" 		AND TRUNC(CAST(ds.dh_emissao AS DATE)) BETWEEN frf.dt_vigencia_inicial AND frf.dt_vigencia_final) as qt_franquias, ")
		.append("	dsf.id_docto_servico_frq as id_conhecimento_original, ")
		.append("	dsf.vl_participacao as vl_participacao_original, ")
		.append("	to_char(dsf.dt_competencia,'ddmmyyyy') as dt_competencia ")

		.append( " FROM ")
		.append( "  conhecimento con, ")
		.append( " 	docto_servico ds,  ")
		.append( " 	devedor_doc_serv_fat ddsf,  ")
		.append( " 	desconto des,  ")
		.append( " 	municipio munc,  ")
		.append( " 	municipio mune,  ")
		.append( " 	cliente clir,  ")
		.append( " 	cliente clid, ")
		.append( " 	filial fo, ")
		.append( " 	filial fd, ")
		.append( "  docto_servico_frq dsf ")

		.append(" WHERE " )
		.append(" 	con.id_conhecimento = ds.id_docto_servico " )
		.append(" 	and con.id_conhecimento =ddsf.id_docto_servico " )
		.append(" 	and ddsf.id_devedor_doc_serv_fat = des.id_devedor_doc_serv_fat " )
		.append(" 	and con.id_municipio_entrega = mune.id_municipio " )
		.append(" 	and con.id_municipio_coleta = munc.id_municipio " )
		.append(" 	and ds.id_cliente_remetente = clir.id_cliente " )
		.append(" 	and ds.id_cliente_destinatario = clid.id_cliente " )
		.append(" 	and ds.id_filial_origem = fo.id_filial " )
		.append(" 	and ds.id_filial_destino = fd.id_filial ")
		.append(" 	and ( fo.id_filial_responsavel = ? or fd.id_filial_responsavel = ? ) ")  
		.append(" 	and dsf.id_docto_servico = ds.id_docto_servico ")
		.append(" 	and dsf.id_franquia = ? ")
		.append(" 	and dsf.dt_competencia < to_date(?,'yyyy-mm-dd') ")
		.append(" 	and TRUNC(CAST(DDSF.DT_LIQUIDACAO AS DATE)) between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') ")
		.append(" 	and des.tp_situacao_aprovacao = ? ");

    	List<Object> params = new ArrayList<Object>();
		params.add(psMinFreteCarreteiro);
		params.add(idFranquia);
		params.add(idFranquia);
		params.add(idFranquia);
		params.add(dataInicioCompetencia.toString());
		params.add(dataInicioCompetencia.toString());
		params.add(dataFimCompetencia.toString());
		params.add(ConstantesFranqueado.TP_APROVADO);

		return getJdbcTemplate().query(query.toString(), params.toArray(), new ColumnMapRowMapper());
	}

}
