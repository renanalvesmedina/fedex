package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.contasreceber.model.RetornoBanco;

public class RetornoBancoDAO extends BaseCrudDao<RetornoBanco, Long> {

	@Override
	protected Class getPersistentClass() {
		return RetornoBanco.class;
	}

	public List<RetornoBanco> findComDsOcorrenciaEMotivo(Map criteria) {

		StringBuilder sql = new StringBuilder()
		.append(" SELECT rb, ")
		.append(getSelectDescricaoOcorrencia())
		.append(" as dsOcorrencia, ")
		.append(getSelectDescricaoMotivo())
		.append(" as dsMotivo ")
		.append(" FROM RetornoBanco rb ")
		.append(" WHERE 1=1 ");

		HashMap<String, Object> params = new HashMap<String, Object>(); 
		
		QueryStruct queryStruct = getFilters(criteria);
		sql.append(queryStruct.getHql().toString());
		params.putAll(queryStruct.getParams());
		
		List<Object[]> resultList =  getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		
    	List<RetornoBanco> retornoList = transformToEntity(resultList);
    	return retornoList;
	}

	
	public List<RetornoBanco> fingByNrBoletoDsLinha(String nrBoleto, YearMonthDay dtMovimento, String dsLinha){
		String hql = "select rb from RetornoBanco rb "
				+ "where "
				+ "	rb.nrBoleto = :nrBoleto "
				+ " and rb.dsLinha = :dsLinha "
				+ " and rb.dtMovimento = :dtMovimento";
				
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nrBoleto", nrBoleto);
		parameters.put("dsLinha", dsLinha);
		parameters.put("dtMovimento", dtMovimento);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql, parameters);
	}
	
	public Integer findCount(Map criteria) {

		StringBuilder sql = new StringBuilder()
		.append(" SELECT count(*) ")
		.append(" FROM RetornoBanco rb ")
		.append(" WHERE 1=1 ");

		HashMap<String, Object> params = new HashMap<String, Object>(); 
		
		QueryStruct queryStruct = getFilters(criteria);
		sql.append(queryStruct.getHql().toString());
		params.putAll(queryStruct.getParams());
		
		List<Object[]> resultList =  getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		
    	return resultList.size();
	}

	private QueryStruct getFilters(Map criteria) {

		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>(); 
		
		if(criteria.get("TpSituacaoFatura") != null){
			sql.append(" AND rb.boleto.fatura.tpSituacaoFatura = :param_tp_sit_fat ");
			params.put("param_tp_sit_fat", criteria.get("TpSituacaoFatura"));
		}
		if(criteria.get("TpSituacaoBoleto") != null){
			sql.append(" AND rb.boleto.tpSituacaoBoleto = :param_tp_sit_bol ");
			params.put("param_tp_sit_bol", criteria.get("TpSituacaoBoleto"));
		}
		if(criteria.get("NrBanco") != null){
			sql.append(" AND rb.nrBanco = :param_nr_banco ");
			params.put("param_nr_banco", criteria.get("NrBanco"));
		}
		if(criteria.get("SomenteLiquidacao") != null && "S".equals(criteria.get("SomenteLiquidacao"))){
			sql.append(" AND rb.nrOcorrencia IN ('6', '7', '15', '16', '31', '32', '33', '36', '38', '39') ");
		}
		if(criteria.get("SomenteNaoCadastrado") != null && "S".equals(criteria.get("SomenteNaoCadastrado"))){
			sql.append(" AND rb.boleto.idBoleto IS NULL ");
		}
		if(criteria.get("IdFilial") != null){
			sql.append(" AND rb.boleto.fatura.filialByIdFilial.idFilial = :param_id_filial_cob ");
			params.put("param_id_filial_cob", criteria.get("IdFilial"));
		}
		if(criteria.get("DtMovimentoInicial") != null){
			sql.append(" AND rb.dtMovimento >= :param_dt_movto_inicial ");
			params.put("param_dt_movto_inicial", criteria.get("DtMovimentoInicial"));
		}
		if(criteria.get("DtMovimentoFinal") != null){
			sql.append(" AND rb.dtMovimento <= :param_dt_movto_final ");
			params.put("param_dt_movto_final", criteria.get("DtMovimentoFinal"));
		}
		if(criteria.get("DtEmissaoFaturaInicial") != null){
			sql.append(" AND rb.boleto.fatura.dtEmissao >= :param_dt_em_fat_inicial ");
			params.put("param_dt_em_fat_inicial", criteria.get("DtEmissaoFaturaInicial"));
		}
		if(criteria.get("DtEmissaoFaturaFinal") != null){
			sql.append(" AND rb.boleto.fatura.dtEmissao <= :param_dt_em_fat_final");
			params.put("param_dt_em_fat_final", criteria.get("DtEmissaoFaturaFinal"));
		}
		if(criteria.get("DtEmissaoBoletoInicial") != null){
			sql.append(" AND rb.boleto.dtEmissao >= :param_dt_em_bol_inicial ");
			params.put("param_dt_em_bol_inicial", criteria.get("DtEmissaoBoletoInicial"));
		}
		if(criteria.get("DtEmissaoBoletoFinal") != null){
			sql.append(" AND rb.boleto.dtEmissao <= :param_dt_em_bol_final ");
			params.put("param_dt_em_bol_final", criteria.get("DtEmissaoBoletoFinal"));
		}
		if(criteria.get("DtVencimentoInicial") != null){
			sql.append(" AND rb.boleto.dtVencimento >= :param_dt_vct_bol_inicial ");
			params.put("param_dt_vct_bol_inicial", criteria.get("DtVencimentoInicial"));
		}
		if(criteria.get("DtVencimentoFinal") != null){
			sql.append(" AND rb.boleto.dtVencimento <= :param_dt_vct_bol_final ");
			params.put("param_dt_vct_bol_final", criteria.get("DtVencimentoFinal"));
		}

		return new QueryStruct(sql,params);
	}

	private String getSelectDescricaoMotivo() {
		StringBuilder sql = new StringBuilder()
		.append(" (SELECT mob.dsMotivoOcorrenciaBanco ")
		.append(" FROM ")
		.append(" OcorrenciaBanco ob, ")
		.append(" MotivoOcorrenciaBanco mob ")
		.append(" WHERE ")
		.append(" ob.banco.nrBanco = rb.nrBanco ")
		.append(" AND ob.tpOcorrenciaBanco = 'RET' ")
		.append(" AND ob.nrOcorrenciaBanco = rb.nrOcorrencia ")
		.append(" AND ob = mob.ocorrenciaBanco ")
		.append(" AND mob.nrMotivoOcorrenciaBanco = rb.nrMotivoRejeicao  ) ")
		;
		return sql.toString();
	}

	private List<RetornoBanco> transformToEntity(List<Object[]> resultList) {
		List<RetornoBanco> retornoList = new ArrayList<RetornoBanco>();
		for(Object[] item : resultList){
			retornoList.add(transformToEntityUnique(item));
		}
		return retornoList;
	}

	private RetornoBanco transformToEntityUnique(Object[] item) {
		RetornoBanco naoEncontrou= null;
		if(item != null && item.length == 3){
			RetornoBanco retornoBanco = (RetornoBanco) item[0];
			retornoBanco.setDescricaoOcorrencia(((String)  item[1]));
			retornoBanco.setDescricaoMotivo(((String)  item[2]));
			return retornoBanco;
		}
		return naoEncontrou;
	}


	private String getSelectDescricaoOcorrencia() {
		StringBuilder sql = new StringBuilder()
		.append(" (SELECT ob.dsOcorrenciaBanco ")
		.append(" FROM ")
		.append(" OcorrenciaBanco ob ")
		.append(" WHERE ")
		.append(" ob.banco.nrBanco = rb.nrBanco ")
		.append(" AND ob.tpOcorrenciaBanco = 'RET' ")
		.append(" AND ob.nrOcorrenciaBanco = rb.nrOcorrencia) ")
		;
		return sql.toString();
	}

	public List<Map<String, Object>> findReport(Map criteria) {

		StringBuilder sql = new StringBuilder()
		.append(" SELECT rb.id_retorno_banco idRetornoBanco, ")
	       .append(" rb.nr_boleto nrBoletoMovimento, ")
	       .append(" rb.nr_banco nrBancoMovimento, ")
	       .append(" to_char(rb.dt_movimento, 'DD/MM/YYYY') dtMovimento, ")
	       .append(" rb.ds_retorno_banco dsRetorno, ")
	       .append(" rb.nr_ocorrencia nrOcorrencia, ")
	       .append(" (SELECT ob.ds_ocorrencia_banco ")
	          .append(" FROM banco            ba, ")
	               .append(" ocorrencia_banco ob ")
	         .append(" WHERE ba.id_banco = ob.id_banco ")
	           .append(" AND ba.nr_banco = rb.nr_banco ")
	           .append(" AND ob.tp_ocorrencia_banco = 'RET' ")
	           .append(" AND ob.nr_ocorrencia_banco = rb.nr_ocorrencia) dsOcorrencia, ")
	       .append(" rb.nr_motivo_rejeicao motivoRejeicao, ")
	       .append(" (SELECT "+ adicionarReplaceNoCampo("mob.ds_motivo_ocorrencia_banco")+ "as ds_motivo_ocorrencia_banco ")
	          .append(" FROM banco                   ba, ")
	               .append(" ocorrencia_banco        ob, ")
	               .append(" motivo_ocorrencia_banco mob ")
	         .append(" WHERE ba.id_banco = ob.id_banco ")
	           .append(" AND ob.id_ocorrencia_banco = mob.id_ocorrencia_banco ")
	           .append(" AND ba.nr_banco = rb.nr_banco ")
	           .append(" AND ob.tp_ocorrencia_banco = 'RET' ")
	           .append(" AND ob.nr_ocorrencia_banco = rb.nr_ocorrencia ")
	           .append(" AND mob.nr_motivo_ocorrencia_banco = rb.nr_motivo_rejeicao) dsMotivo, ")
	       .append(" TRIM(to_char(rb.vl_total, '9G999G999G999G999G990D00')) vlTotalBoletoRB, ")
	       .append(" TRIM(to_char(rb.vl_desconto, '9G999G999G999G999G990D00')) vlDesconto, ")
	       .append(" TRIM(to_char(rb.vl_abatimento, '9G999G999G999G999G990D00')) vlAbatimento, ")
	       .append(" TRIM(to_char(rb.vl_juros, '9G999G999G999G999G990D00')) vlJuros, ")
	       .append(" to_char(rb.dh_inclusao, 'DD/MM/YYYY HH24:MI:SS') dhInclusao, ")
	.append("         ")
	       .append(" b.id_boleto idBoleto, ")
	       .append(" b.nr_boleto nrBoleto, ")
	       .append(" cedbol.ds_cedente dsCedenteBoleto, ")
	       .append(" (SELECT vi18n(ds_valor_dominio_i) ")
	          .append(" FROM valor_dominio ")
	         .append(" WHERE id_dominio IN ")
	               .append(" (SELECT id_dominio ")
	                  .append(" FROM dominio ")
	                 .append(" WHERE nm_dominio = 'DM_STATUS_BOLETO') ")
	           .append(" AND vl_valor_dominio = b.tp_situacao_boleto) situacaoBoleto, ")
	       .append(" to_char(b.dt_emissao, 'DD/MM/YYYY') dtEmissaoBoleto, ")
	       .append(" to_char(b.dt_vencimento, 'DD/MM/YYYY') dtVencimentoBoleto, ")
	       .append(" to_char(b.dt_vencimento_novo, 'DD/MM/YYYY') dtVencimentoAprovacao, ")
	       .append(" TRIM(to_char(b.vl_total, '9G999G999G999G999G990D00')) vlTotalBoleto, ")
	       .append(" TRIM(to_char(b.vl_desconto, '9G999G999G999G999G990D00')) vlDescontoBoleto, ")
	       .append(" TRIM(to_char(b.vl_juros_dia, '9G999G999G999G999G990D00')) vlJuroDiario, ")
	.append("         ")
	       .append(" f.id_fatura idFatura, ")
	       .append(" fi.sg_filial sgFilial, ")
	       .append(" f.nr_fatura nrFatura, ")
	       .append(" pcli.nr_identificacao cnpj, ")
	       .append(adicionarReplaceNoCampo("pcli.nm_pessoa") + " razaoSocial, ")
	       .append(" ficobcli.sg_filial filialCobCliente, ")
	       .append(" (SELECT vi18n(ds_valor_dominio_i) ")
	          .append(" FROM valor_dominio ")
	         .append(" WHERE id_dominio IN ")
	               .append(" (SELECT id_dominio ")
	                  .append(" FROM dominio ")
	                 .append(" WHERE nm_dominio = 'DM_TIPO_PESSOA') ")
	           .append(" AND vl_valor_dominio = pcli.tp_pessoa) tpPessoa, ")
	       .append(" (SELECT vi18n(ds_valor_dominio_i) ")
	          .append(" FROM valor_dominio ")
	         .append(" WHERE id_dominio IN ")
	               .append(" (SELECT id_dominio ")
	                  .append(" FROM dominio ")
	                 .append(" WHERE nm_dominio = 'DM_STATUS_ROMANEIO') ")
	           .append(" AND vl_valor_dominio = f.tp_situacao_fatura) situacaoFatura, ")
	       .append(" (SELECT vi18n(ds_valor_dominio_i) ")
	          .append(" FROM valor_dominio ")
	         .append(" WHERE id_dominio IN ")
	               .append(" (SELECT id_dominio ")
	                  .append(" FROM dominio ")
	                 .append(" WHERE nm_dominio = 'DM_STATUS_WORKFLOW') ")
	           .append(" AND vl_valor_dominio = f.tp_situacao_aprovacao) situacaoAprovacao, ")
	       .append(" to_char(f.dt_emissao, 'DD/MM/YYYY') dtEmissao, ")
	       .append(" to_char(f.dt_vencimento, 'DD/MM/YYYY') dtVencimento, ")
	       .append(" f.qt_documentos qtDocumentos, ")
	       .append(" TRIM(to_char(f.vl_total, '9G999G999G999G999G990D00')) vlTotalFatura, ")
	       .append(" TRIM(to_char(f.vl_desconto, '9G999G999G999G999G990D00')) vlTotalDescontos, ")
	       .append(" TRIM(to_char(f.vl_juro_calculado, '9G999G999G999G999G990D00')) vlTotalJuros, ")
	       .append(" TRIM(to_char(nvl((SELECT SUM(rpp.vl_pagamento) ")
	                          .append(" FROM relacao_pagto_parcial rpp ")
	                         .append(" WHERE rpp.id_fatura = f.id_fatura), ")
	                        .append(" 0), ")
	                    .append(" '9G999G999G999G999G990D00')) vlRecebidoParcial, ")
	       .append(" TRIM(to_char(decode(f.tp_situacao_fatura, ")
	                           .append(" 'LI', ")
	                           .append(" 0, ")
	                           .append(" f.vl_total - ")
	                           .append(" nvl((SELECT SUM(rpp.vl_pagamento) ")
	                                 .append(" FROM relacao_pagto_parcial rpp ")
	                                .append(" WHERE rpp.id_fatura = f.id_fatura), ")
	                               .append(" 0) - f.vl_desconto), ")
	                    .append(" '9G999G999G999G999G990D00')) valorSaldo, ")
	       .append(" to_char((SELECT MAX(rpp.dt_pagamento) ")
	          .append(" FROM relacao_pagto_parcial rpp ")
	         .append(" WHERE rpp.id_fatura = f.id_fatura), 'DD/MM/YYYY') dtUltimoRecebimentoParcial, ")
	         .append(adicionarReplaceNoCampo("f.ob_fatura") + " observacoes ")
	       
		.append(" FROM retorno_banco rb, ")
		.append(" boleto        b, ")
		.append(" fatura        f, ")
		.append(" filial        fi, ")
		.append(" cedente       cedbol, ")
		.append(" pessoa        pcli, ")
		.append(" cliente       cli, ")
		.append(" filial        ficobcli ")
		.append(" WHERE rb.id_boleto = b.id_boleto(+) ")
		.append(" AND b.id_fatura = f.id_fatura(+) ")
		.append(" AND f.id_filial = fi.id_filial(+) ")
		.append(" AND b.id_cedente = cedbol.id_cedente(+) ")
		.append(" AND f.id_cliente = pcli.id_pessoa(+) ")
		.append(" AND f.id_cliente = cli.id_cliente(+) ")
		.append(" AND cli.id_filial_cobranca = ficobcli.id_filial(+) ");

		HashMap<String, Object> params = new HashMap<String, Object>(); 
		if(criteria.get("TpSituacaoFatura") != null){
			sql.append(" AND f.tp_situacao_fatura = :param_tp_sit_fat ");
			params.put("param_tp_sit_fat", criteria.get("TpSituacaoFatura"));
		}
		if(criteria.get("TpSituacaoBoleto") != null){
			sql.append(" AND b.tp_situacao_boleto = :param_tp_sit_bol ");
			params.put("param_tp_sit_bol", criteria.get("TpSituacaoBoleto"));
		}
		if(criteria.get("NrBanco") != null){
			sql.append(" AND rb.nr_banco = :param_nr_banco ");
			params.put("param_nr_banco", criteria.get("NrBanco"));
		}
		if(criteria.get("SomenteLiquidacao") != null && "S".equals(criteria.get("SomenteLiquidacao"))){
			sql.append(" AND rb.nr_ocorrencia IN ('06', '07', '15', '16', '31', '32', '33', '36', '38', '39') ");
		}
		if(criteria.get("SomenteNaoCadastrado") != null && "S".equals(criteria.get("SomenteNaoCadastrado"))){
			sql.append(" AND rb.id_boleto IS NULL ");
		}
		if(criteria.get("IdFilial") != null){
			sql.append(" AND fi.id_filial = :param_id_filial_cob ");
			params.put("param_id_filial_cob", criteria.get("IdFilial"));
		}
		if(criteria.get("DtMovimentoInicial") != null){
			sql.append(" AND rb.dt_movimento >= :param_dt_movto_inicial ");
			params.put("param_dt_movto_inicial", criteria.get("DtMovimentoInicial"));
		}
		if(criteria.get("DtMovimentoFinal") != null){
			sql.append(" AND rb.dt_movimento <= :param_dt_movto_final ");
			params.put("param_dt_movto_final", criteria.get("DtMovimentoFinal"));
		}
		if(criteria.get("DtEmissaoFaturaInicial") != null){
			sql.append(" AND f.dt_emissao >= :param_dt_em_fat_inicial ");
			params.put("param_dt_em_fat_inicial", criteria.get("DtEmissaoFaturaInicial"));
		}
		if(criteria.get("DtEmissaoFaturaFinal") != null){
			sql.append(" AND f.dt_emissao <= :param_dt_em_fat_final");
			params.put("param_dt_em_fat_final", criteria.get("DtEmissaoFaturaFinal"));
		}
		if(criteria.get("DtEmissaoBoletoInicial") != null){
			sql.append(" AND b.dt_emissao >= :param_dt_em_bol_inicial ");
			params.put("param_dt_em_bol_inicial", criteria.get("DtEmissaoBoletoInicial"));
		}
		if(criteria.get("DtEmissaoBoletoFinal") != null){
			sql.append(" AND b.dt_emissao <= :param_dt_em_bol_final ");
			params.put("param_dt_em_bol_final", criteria.get("DtEmissaoBoletoFinal"));
		}
		if(criteria.get("DtVencimentoInicial") != null){
			sql.append(" AND b.dt_vencimento >= :param_dt_vct_bol_inicial ");
			params.put("param_dt_vct_bol_inicial", criteria.get("DtVencimentoInicial"));
		}
		if(criteria.get("DtVencimentoFinal") != null){
			sql.append(" AND b.dt_vencimento <= :param_dt_vct_bol_final ");
			params.put("param_dt_vct_bol_final", criteria.get("DtVencimentoFinal"));
		}
		
    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
    		@Override
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idRetornoBanco", Hibernate.STRING);
    			sqlQuery.addScalar("nrBoletoMovimento", Hibernate.STRING);
    			sqlQuery.addScalar("nrBancoMovimento", Hibernate.STRING);
    			sqlQuery.addScalar("dtMovimento", Hibernate.STRING);
    			sqlQuery.addScalar("dsRetorno", Hibernate.STRING);
    			sqlQuery.addScalar("nrOcorrencia", Hibernate.STRING);
    			sqlQuery.addScalar("dsOcorrencia", Hibernate.STRING);
    			sqlQuery.addScalar("motivoRejeicao", Hibernate.STRING);
    			sqlQuery.addScalar("dsMotivo", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalBoletoRB", Hibernate.STRING);
    			sqlQuery.addScalar("vlDesconto", Hibernate.STRING);
    			sqlQuery.addScalar("vlAbatimento", Hibernate.STRING);
    			sqlQuery.addScalar("vlJuros", Hibernate.STRING);
    			sqlQuery.addScalar("dhInclusao", Hibernate.STRING);
    			sqlQuery.addScalar("idBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("nrBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("dsCedenteBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("situacaoBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("dtEmissaoBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("dtVencimentoBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("qtDocumentos", Hibernate.STRING);
    			sqlQuery.addScalar("dtVencimentoAprovacao", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("vlDescontoBoleto", Hibernate.STRING);
    			sqlQuery.addScalar("vlJuroDiario", Hibernate.STRING);
    			sqlQuery.addScalar("idFatura", Hibernate.STRING);
    			sqlQuery.addScalar("sgFilial", Hibernate.STRING);
    			sqlQuery.addScalar("nrFatura", Hibernate.STRING);
    			sqlQuery.addScalar("cnpj", Hibernate.STRING);
    			sqlQuery.addScalar("razaoSocial", Hibernate.STRING);
    			sqlQuery.addScalar("filialCobCliente", Hibernate.STRING);
    			sqlQuery.addScalar("tpPessoa", Hibernate.STRING);
    			sqlQuery.addScalar("situacaoFatura", Hibernate.STRING);
    			sqlQuery.addScalar("situacaoAprovacao", Hibernate.STRING);
    			sqlQuery.addScalar("dtEmissao", Hibernate.STRING);
    			sqlQuery.addScalar("dtVencimento", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalFatura", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalDescontos", Hibernate.STRING);
    			sqlQuery.addScalar("vlTotalJuros", Hibernate.STRING);
    			sqlQuery.addScalar("vlRecebidoParcial", Hibernate.STRING);
    			sqlQuery.addScalar("valorSaldo", Hibernate.STRING);
    			sqlQuery.addScalar("dtUltimoRecebimentoParcial", Hibernate.STRING);
    			sqlQuery.addScalar("observacoes", Hibernate.STRING);
    		}
    	};

    	return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, configSql);
	}
	
	private String adicionarReplaceNoCampo(String descricao){
		return " REPLACE(REPLACE("+ descricao +", '\"', ''), ';', ',') ";
	}
	
	class QueryStruct{
		private StringBuilder hql;
		private HashMap<String, Object> params;
		public QueryStruct(StringBuilder hql, HashMap<String, Object> params) {
			super();
			this.hql = hql;
			this.params = params;
		}
		public StringBuilder getHql() {
			return hql;
		}
		public HashMap<String, Object> getParams() {
			return params;
		}
	}

}
