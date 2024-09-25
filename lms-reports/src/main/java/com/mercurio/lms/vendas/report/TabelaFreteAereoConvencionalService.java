package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;

public class TabelaFreteAereoConvencionalService extends ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		// Não é utilizado.
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		TypedFlatMap parametros = new TypedFlatMap();
		parametros.putAll(parameters);
		parameters.put(TabelasClienteService.KEY_PARAMETER_LEGENDAS, getTabelasClienteService().getSubRelLegendas(getJdbcTemplate()));
		parameters.put(TabelasClienteService.KEY_PARAMETER_AEREO, getTabelasClienteService().getSubRelAereo(getJdbcTemplate()));
		parameters.put("cabecalhoProdutoEspecifico", getJdbcTemplate().queryForList("select nr_tarifa_especifica as ds_cabecalho from produto_especifico order by nr_tarifa_especifica"));
		parameters.put("sgAeroportoOrigem", findSgAeroportoOrigem(parameters));
		
		Map parametersReport = new HashMap();
		parametersReport = agruparDadosPorParametroCliente(parametros);
		parameters.put("grupos", parametros.get("grupos"));
		
		List<Map<String, String>> listaDadosCompleta = executeBuscaDados((Map) parametros.get("idsPametrosClientePorGrupo"), parameters.getString("ordenacao"));

		result.addAll(listaDadosCompleta);
		result.add(parametersReport);
		result.add(parameters);

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String findSgAeroportoOrigem(TypedFlatMap parameters){
		String sgAeroportoOrigem = "";
		List parametros = MapUtilsPlus.getList(parameters, "listaParametros", null);
		
		if(parametros != null && !parametros.isEmpty()){
			List<Map> lista = getJdbcTemplate().queryForList("select a.sg_aeroporto from parametro_cliente p, aeroporto a where p.id_aeroporto_origem = a.id_aeroporto and id_parametro_cliente = " +  parametros.get(0));
			if(lista != null && !lista.isEmpty()){
				sgAeroportoOrigem = MapUtilsPlus.getString(lista.get(0), "sg_aeroporto");
			}
		}
		
		return sgAeroportoOrigem;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, String>> executeBuscaDados(Map idsPametrosClientePorGrupoMap, String orderBy) {
		List listaDadosCompleta = new ArrayList();
		
		for (int indexGrupo = 0; indexGrupo < idsPametrosClientePorGrupoMap.size(); indexGrupo++) {
			String idsParametroClienteIn = getIdsParametroClienteIn((List<Long>) idsPametrosClientePorGrupoMap.get("grupo" + indexGrupo));
			listaDadosCompleta.addAll(getJdbcTemplate().queryForList(getQueryListaPreco(indexGrupo, idsParametroClienteIn, orderBy)));
			listaDadosCompleta.addAll(getJdbcTemplate().queryForList(getQueryProdutoEspecifico(indexGrupo, idsParametroClienteIn, orderBy)));
			listaDadosCompleta.addAll(getJdbcTemplate().queryForList(getQueryFaixasPeso(indexGrupo, idsParametroClienteIn, orderBy)));
			listaDadosCompleta.addAll(getJdbcTemplate().queryForList(getQueryCabecalhoFaixasPeso(indexGrupo, idsParametroClienteIn)));
		}
		return listaDadosCompleta;
	}
	
	private String getQueryCabecalhoFaixasPeso(int indexGrupo, String idsParametroClienteIn){
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT FPP.VALOR_FAIXA, ");
		sql.append(" 'grupofaixapesocabecalho");
		sql.append(indexGrupo);
		sql.append("' as GRUPOFAIXAPESOCABECALHO ");
		sql.append(" FROM ");
		sql.append(" FAIXA_PROGRESSIVA_PROPOSTA FPP, ");
		sql.append(" VL_FAIXA_PROGR_PROPOSTA VFPP, ");
		sql.append(" PARAMETRO_CLIENTE PC, ");
		sql.append(" AEROPORTO DEST, ");
		sql.append(" PESSOA P, ");
		sql.append(" ENDERECO_PESSOA ENDP, ");
		sql.append(" MUNICIPIO MUN, ");
		sql.append(" UNIDADE_FEDERATIVA UNF, ");
		sql.append(" REGIAO_GEOGRAFICA REG ");
		sql.append(" WHERE VFPP.ID_FAIXA_PROGRESSIVA_PROPOSTA = FPP.ID_FAIXA_PROGRESSIVA_PROPOSTA ");
		sql.append(" AND PC.ID_PARAMETRO_CLIENTE = VFPP.ID_PARAMETRO_CLIENTE ");
		sql.append(" AND DEST.ID_AEROPORTO = PC.ID_AEROPORTO_DESTINO ");
		sql.append(" AND P.ID_PESSOA = DEST.ID_AEROPORTO ");
		sql.append(" AND P.ID_ENDERECO_PESSOA = ENDP.ID_ENDERECO_PESSOA ");
		sql.append(" AND MUN.ID_MUNICIPIO = ENDP.ID_MUNICIPIO ");
		sql.append(" AND UNF.ID_UNIDADE_FEDERATIVA = MUN.ID_UNIDADE_FEDERATIVA ");
		sql.append(" AND REG.ID_REGIAO_GEOGRAFICA = UNF.ID_REGIAO_GEOGRAFICA ");
		sql.append(" AND FPP.ID_PRODUTO_ESPECIFICO IS NULL ");
		sql.append(" AND PC.TP_SITUACAO_PARAMETRO = 'P' ");
		sql.append(" AND pc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append(" ORDER BY FPP.VALOR_FAIXA ");
		
		return sql.toString();
	}
	
	private String getQueryFaixasPeso(int indexGrupo, String idsParametroClienteIn, String orderBy){
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT 'grupofaixapeso");
		sql.append(indexGrupo);
		sql.append("' as GRUPOFAIXAPESO, ");
		sql.append(" VI18N(reg.ds_regiao_geografica_i) dsRegiao, ");
		sql.append(" DEST.SG_AEROPORTO || ' - ' || MUN.NM_MUNICIPIO || ' - ' || UNF.SG_UNIDADE_FEDERATIVA  dsDestino, ");
		sql.append(" to_char(pc.vl_tarifa_minima, 'FM999G999G990D90')  vlTarifa, ");
		sql.append(" FPP.VALOR_FAIXA vlFaixa, ");
		sql.append(" to_char(vfpp.valor_fixo, 'FM999G999G990D90')  valorFixo ");
		sql.append("FROM ");
		sql.append(" FAIXA_PROGRESSIVA_PROPOSTA FPP, ");
		sql.append(" VL_FAIXA_PROGR_PROPOSTA VFPP, ");
		sql.append(" PARAMETRO_CLIENTE PC, ");
		sql.append(" AEROPORTO DEST, ");
		sql.append(" PESSOA P, ");
		sql.append(" ENDERECO_PESSOA ENDP, ");
		sql.append(" MUNICIPIO MUN, ");
		sql.append(" UNIDADE_FEDERATIVA UNF, ");
		sql.append(" REGIAO_GEOGRAFICA REG ");
		sql.append(" WHERE VFPP.ID_FAIXA_PROGRESSIVA_PROPOSTA = FPP.ID_FAIXA_PROGRESSIVA_PROPOSTA ");
		sql.append(" AND PC.ID_PARAMETRO_CLIENTE = VFPP.ID_PARAMETRO_CLIENTE ");
		sql.append(" AND DEST.ID_AEROPORTO = PC.ID_AEROPORTO_DESTINO ");
		sql.append(" AND P.ID_PESSOA = DEST.ID_AEROPORTO ");
		sql.append(" AND P.ID_ENDERECO_PESSOA = ENDP.ID_ENDERECO_PESSOA ");
		sql.append(" AND MUN.ID_MUNICIPIO = ENDP.ID_MUNICIPIO ");
		sql.append(" AND UNF.ID_UNIDADE_FEDERATIVA = MUN.ID_UNIDADE_FEDERATIVA ");
		sql.append(" AND REG.ID_REGIAO_GEOGRAFICA = UNF.ID_REGIAO_GEOGRAFICA ");
		sql.append(" AND FPP.ID_PRODUTO_ESPECIFICO IS NULL ");
		sql.append(" AND PC.TP_SITUACAO_PARAMETRO = 'P' ");
		sql.append(" AND pc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		
		addOrderBy(sql, orderBy);
		
		if(orderBy != null){
			sql.append(", FPP.VALOR_FAIXA");
		}
		
		return sql.toString();
	}
	
	private String getQueryProdutoEspecifico(int indexGrupo, String idsParametroClienteIn, String orderBy){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'subgrupo");
		sql.append(indexGrupo);
		sql.append("' as SUBGRUPO ");
		sql.append(", VI18N(reg.ds_regiao_geografica_i) dsRegiao ");
		sql.append(", dest.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || unf.sg_unidade_federativa   dsDestino ");
		sql.append(", pe.nr_tarifa_especifica nrTarifaEspecifica ");
		sql.append(", to_char(vfpp.valor_fixo, 'FM999G999G990D90')  valorFixo ");
		sql.append("FROM parametro_cliente pc ");
		sql.append(", aeroporto dest ");
		sql.append(", endereco_pessoa endp ");
		sql.append(", municipio mun ");
		sql.append(", unidade_federativa unf ");
		sql.append(", regiao_geografica reg ");
		sql.append(", faixa_progressiva_proposta fpp ");
		sql.append(", vl_faixa_progr_proposta vfpp ");
		sql.append(", produto_especifico pe ");
		sql.append(", pessoa p ");
		sql.append("WHERE pc.id_aeroporto_destino = dest.id_aeroporto ");
		sql.append("AND fpp.id_simulacao = pc.id_simulacao ");
		sql.append("AND fpp.id_produto_especifico IS NOT NULL ");
		sql.append("AND vfpp.id_parametro_cliente = pc.id_parametro_cliente ");
		sql.append("AND vfpp.id_faixa_progressiva_proposta = fpp.id_faixa_progressiva_proposta ");
		sql.append("AND pe.id_produto_especifico = fpp.id_produto_especifico ");
		sql.append("AND p.id_pessoa = dest.id_aeroporto ");
		sql.append("AND pc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("AND p.id_endereco_pessoa = endp.id_endereco_pessoa ");
		sql.append("AND endp.id_municipio = mun.id_municipio ");
		sql.append("AND mun.id_unidade_federativa = unf.id_unidade_federativa ");
		sql.append("AND unf.id_regiao_geografica = reg.id_regiao_geografica ");
		
		addOrderBy(sql, orderBy);
		
		return sql.toString();
	}

	private void addOrderBy(StringBuilder sql, String orderBy){
		if (orderBy != null) {
			sql.append("ORDER BY ");

			if ("R".equals(orderBy)) {
				sql.append("VI18N(reg.ds_regiao_geografica_i), dest.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || unf.sg_unidade_federativa");
			} else if ("A".equals(orderBy)) {
				sql.append("dest.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || unf.sg_unidade_federativa, VI18N(reg.ds_regiao_geografica_i)");
			}
		}
	}
	
	private String getQueryListaPreco(int indexGrupo, String idsParametroClienteIn, String orderBy){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT VI18N(reg.ds_regiao_geografica_i)  dsRegiao  ");
		sql.append(",'grupo");
		sql.append(indexGrupo);
		sql.append("' GRUPO ");
		sql.append("     , dest.sg_aeroporto || ' - ' || mun.nm_municipio || ' - ' || unf.sg_unidade_federativa   dsDestino ");
		sql.append("     , to_char(pc.vl_advalorem, 'FM999G999G990D90') advalorem1 ");
		sql.append("     , to_char(pc.vl_advalorem_2, 'FM999G999G990D90') advalorem2 ");
		//Coleta Urbana Convencional
		sql.append("     , to_char(tcuc.vl_taxa, 'FM999G999G990D90') taxaColUrbConvencional ");
		sql.append("     , to_char(tcuc.ps_minimo, 'FM999G999G990D900') psMinimoColUrbConvencional ");
		sql.append("     , to_char(tcuc.vl_excedente, 'FM999G999G990D90') vlExcedenteColUrbConvencional ");
		//Coleta Urbana Emergencial
		sql.append("     , to_char(tcue.vl_taxa, 'FM999G999G990D90') taxaColUrbEmergencial ");
		sql.append("     , to_char(tcue.ps_minimo, 'FM999G999G990D900') psMinimoColUrbEmergencial ");
		sql.append("     , to_char(tcue.vl_excedente, 'FM999G999G990D90') vlExcedenteColUrbEmergencial ");
		//Coleta Interior Convencional
		sql.append("     , to_char(tcic.vl_taxa, 'FM999G999G990D90') taxaColIntConvencional ");
		sql.append("     , to_char(tcic.ps_minimo, 'FM999G999G990D900') psMinimoColIntConvencional ");
		sql.append("     , to_char(tcic.vl_excedente, 'FM999G999G990D90') vlExcedenteColIntConvencional ");
		//Coleta Interior Emergencial
		sql.append("     , to_char(tcie.vl_taxa, 'FM999G999G990D90') taxaColIntEmergencial ");
		//Entrega Urbana Convencional
		sql.append("     , to_char(teuc.vl_taxa, 'FM999G999G990D90') taxaEntUrbConvencional ");
		sql.append("     , to_char(teuc.ps_minimo, 'FM999G999G990D900') psMinimoEntUrbConvencional ");
		sql.append("     , to_char(teuc.vl_excedente, 'FM999G999G990D90') vlExcedenteEntUrbConvencional ");
		//Entrega Urbana Emergencial
		sql.append("     , to_char(teue.vl_taxa, 'FM999G999G990D90') taxaEntUrbEmergencial ");
		sql.append("     , to_char(teue.ps_minimo, 'FM999G999G990D900') psMinimoEntUrbEmergencial ");
		sql.append("     , to_char(teue.vl_excedente, 'FM999G999G990D90') vlExcedenteEntUrbEmergencial ");
		//Entrega Interior Convencional
		sql.append("     , to_char(teic.vl_taxa, 'FM999G999G990D90') taxaEntIntConvencional ");
		sql.append("     , to_char(teic.ps_minimo, 'FM999G999G990D900') psMinimoEntIntConvencional ");
		sql.append("     , to_char(teic.vl_excedente, 'FM999G999G990D90') vlExcedenteEntIntConvencional ");
		//Entrega Interior Emergencial
		sql.append("     , to_char(teie.vl_taxa, 'FM999G999G990D90') taxaEntIntEmergencial    ");
		
		sql.append("  FROM parametro_cliente pc ");
		sql.append("     , aeroporto dest ");
		sql.append("     , endereco_pessoa endp ");
		sql.append("     , municipio mun ");
		sql.append("     , unidade_federativa unf ");
		sql.append("     , regiao_geografica reg ");
		
		// Taxa coleta urbana convencional
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco = pp.id_parcela_preco  ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) tcuc  ");
		
		// Taxa coleta urbana emergencial
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco =  pp.id_parcela_preco  ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) tcue  ");
				
		// Taxa entrega urbana convencional
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco = pp.id_parcela_preco  ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) teuc  ");
		sql.append("      ");
		
		// Taxa entrega urbana emergencial
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco =  pp.id_parcela_preco  ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) teue  ");
		sql.append("      ");
		
		// Taxa coleta interior convencional       ");
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco =  pp.id_parcela_preco  ");
		sql.append("          AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) tcic  ");
		sql.append("      ");
		
		// Taxa coleta interior emergencial
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco = pp.id_parcela_preco  ");
		sql.append("         AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) tcie ");
		sql.append("            ");
		
		// Taxa entrega interior convencional
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("             , txc.ps_minimo ");
		sql.append("             , txc.vl_excedente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco = pp.id_parcela_preco ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) teic  ");
		sql.append("            ");
		
		// Taxa entrega interior emergencial
		sql.append("     , (SELECT txc.vl_taxa ");
		sql.append("             , txc.id_parametro_cliente ");
		sql.append("          FROM taxa_cliente txc, parcela_preco pp ");
		sql.append("         WHERE txc.id_parcela_preco = pp.id_parcela_preco ");
		sql.append("           AND pp.cd_parcela_preco = '").append(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA).append("' ");
		sql.append("           AND txc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		sql.append("           ) teie  ");

		sql.append(" WHERE 1=1 ");
		sql.append("   AND pc.id_aeroporto_destino = dest.id_aeroporto ");
		sql.append("   AND dest.id_aeroporto = endp.id_pessoa ");
		sql.append("   AND endp.id_municipio = mun.id_municipio ");
		sql.append("   AND mun.id_unidade_federativa = unf.id_unidade_federativa ");
		sql.append("   AND unf.id_regiao_geografica = reg.id_regiao_geografica ");
		sql.append("   AND tcuc.id_parametro_cliente(+) = pc.id_parametro_cliente ");
		sql.append("   AND tcue.id_parametro_cliente(+) = pc.id_parametro_cliente   ");
		sql.append("   AND teuc.id_parametro_cliente(+) = pc.id_parametro_cliente ");
		sql.append("   AND teue.id_parametro_cliente(+) = pc.id_parametro_cliente   ");
		sql.append("   AND tcic.id_parametro_cliente(+) = pc.id_parametro_cliente ");
		sql.append("   AND tcie.id_parametro_cliente(+) = pc.id_parametro_cliente ");
		sql.append("   AND teic.id_parametro_cliente(+) = pc.id_parametro_cliente      ");
		sql.append("   AND teie.id_parametro_cliente(+) = pc.id_parametro_cliente ");
		sql.append("   AND pc.id_parametro_cliente IN (");
		sql.append(idsParametroClienteIn);
		sql.append(") ");
		
		addOrderBy(sql, orderBy);
		
		return sql.toString();
	}
	
	private String getIdsParametroClienteIn(List<Long> idsParametroClienteDoGrupo){
		String ids = "";
		for (Iterator<Long> iterator = idsParametroClienteDoGrupo.iterator(); iterator.hasNext();) {
			ids += iterator.next().toString();
			
			if(iterator.hasNext()){
				ids += ",";
			}
		}
		
		return ids;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map agruparDadosPorParametroCliente(Map parameters) {
		Long idCliente = MapUtilsPlus.getLong(parameters, "idCliente", null);
		Long idDivisao = MapUtilsPlus.getLong(parameters, "idDivisao", null);
		List parametros = MapUtilsPlus.getList(parameters, "listaParametros", null);
		Long idTabelaPreco = MapUtilsPlus.getLong(parameters, "idTabelaPreco", null);
		Boolean isTabelaNova = MapUtils.getBoolean(parameters, "isTabelaNova", false);
		Long idTabelaDivisao = MapUtilsPlus.getLong(parameters, "idTabelaDivisao", null);
		Long idContato = MapUtilsPlus.getLong(parameters, "idContato", null);
		Long idSimulacao = MapUtilsPlus.getLong(parameters, "idSimulacao", null);
		Long idServico = MapUtilsPlus.getLong(parameters, "idServico", null);
		Simulacao simulacao = (Simulacao) MapUtils.getObject(parameters, "simulacao");

		Map parametersReport = new HashMap();
		parametersReport.put("idCliente", idCliente);
		parametersReport.put("idServico", idServico);
		parametersReport.put("idContato", idContato);

		// Agrupa os dados
		Map agrup = new HashMap();
		agrup = montaAgrupamento(idCliente, idDivisao, idTabelaDivisao, parametros, agrup, idSimulacao);

		if (parameters != null) {
			/* MODAL/ABRANGENCIA/SERVICO */
			Servico servico = simulacao.getServico();
			parametersReport.put("MODAL", servico.getTpModal().getDescription().getValue());
			parametersReport.put("ABRANGENCIA", servico.getTpAbrangencia().getDescription().getValue());
			Map mapTipoServico = getTabelasClienteService().getTipoServicoByServico(servico.getIdServico(), getJdbcTemplate());
			parametersReport.put("SERVICO", MapUtils.getString(mapTipoServico, "DS_TIPO_SERVICO"));
		}

		Long idParametro = null;
		if (parameters.containsKey("listaParametros")
				&& (parameters.get("listaParametros") instanceof List && CollectionUtils.isNotEmpty((List) parameters.get("listaParametros")))) {
			List<Long> params = MapUtilsPlus.getList(parameters, "listaParametros", ListUtils.EMPTY_LIST);
			idParametro = params.get(0);
		}

		// monta os parameters.
		emitirTabelasClientesService.executeMontarParametrosAereoNovo(idTabelaPreco, isTabelaNova, idCliente, idDivisao, idParametro, idSimulacao,
				parametersReport, agrup);

		parameters.put("grupos", agrup.size());
		parameters.put("idsPametrosClientePorGrupo", agrup);

		return parametersReport;
	}

	/**
	 * 
	 * @param idCliente
	 * @param agrup
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private Map montaAgrupamento(Long idCliente, Long idDivisao, Long idTabelaDivisao, List parametros, Map agrup, Long idSimulacao) {
		// monta um map com as generalidades de cada parametro
		String sqlGrupo = null;
		List grupos = new ArrayList();

		if (idSimulacao == null) {

			/*
			 * LMS-6600 Antes de executar, a query é montada com todos os
			 * idParametro. O problema é que o IN não admite mais de 1000
			 * valores. Por isso quebramos as chamadas ao banco em tantas quanto
			 * necessário.
			 */
			if (parametros.size() > 900) {
				int c = 0;
				int paramProcessed = 0;
				List<Long> arrayParams = new ArrayList<Long>();
				for (Object idParametro : parametros) {
					arrayParams.add(Long.valueOf((Long) idParametro));
					c++;
					paramProcessed++;
					if (c == 900 || ((paramProcessed) == parametros.size())) {
						sqlGrupo = montaSqlGrupos(arrayParams, false);
						grupos.addAll(getJdbcTemplate().queryForList(sqlGrupo, new Long[] { idCliente, idDivisao, idTabelaDivisao }));
						c = 0;
						arrayParams = new ArrayList<Long>();
					}
				}

			} else {
				sqlGrupo = montaSqlGrupos(parametros, false);
				grupos = getJdbcTemplate().queryForList(sqlGrupo, new Long[] { idCliente, idDivisao, idTabelaDivisao });
			}

		} else {
			sqlGrupo = montaSqlGrupos(parametros, true);
			grupos = getJdbcTemplate().queryForList(sqlGrupo);
		}

		for (Iterator iter = grupos.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			Long idParametro = Long.valueOf(map.get("ID_PARAMETRO_CLIENTE").toString());
			String valor = map.get("BRAKEGROUP").toString();
			if (!agrup.containsKey(idParametro)) {
				agrup.put(idParametro, valor);

			} else {
				String valores = valor + agrup.get(idParametro).toString();
				agrup.put(idParametro, valores);
			}
		}
		int i = 0;
		Map mapa = new HashMap();
		List todasChaves = new ArrayList();
		// le o map de generalidades de parametros e agrupa os que tiverem os
		// mesmos valores
		for (Iterator iter = agrup.keySet().iterator(); iter.hasNext();) {
			Long chave = (Long) iter.next();
			String value = agrup.get(chave).toString();
			// apenas faz a verificação em registros que ainda nao tenham sido
			// verificados
			if (todasChaves.indexOf(chave) == -1) {
				if (agrup.containsValue(value)) {
					List listaChaves = new ArrayList();
					for (Iterator it = agrup.keySet().iterator(); it.hasNext();) {
						Long key = (Long) it.next();
						String v = agrup.get(key).toString();
						if (value.equals(v)) {
							listaChaves.add(key);
							todasChaves.add(key);

						}
					}
					iter.remove();
					mapa.put("grupo" + i, listaChaves);
					i++;
				}
			}
		}
		return mapa;
	}

	private String montaSqlGrupos(List parametros, boolean isSimulacao) {
		StringBuffer sql = new StringBuffer();

		if (isSimulacao) {
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,").append("(G.VL_GENERALIDADE ||")
					.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
					.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
					.append(" || PC.PC_COBRANCA_REENTREGA || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) as BRAKEGROUP ")
					.append(" FROM 	PARAMETRO_CLIENTE PC, ").append("		GENERALIDADE_CLIENTE G,").append("		UNIDADE_FEDERATIVA UF_ORIGEM, ")
					.append("		UNIDADE_FEDERATIVA UF_DESTINO ").append("WHERE").append("	PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
					.append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA").append("	AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)")
					.append("   AND PC.ID_PARAMETRO_CLIENTE IN ( ");

		} else {
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,").append("(G.VL_GENERALIDADE ||")
					.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
					.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
					.append(" || PC.PC_COBRANCA_REENTREGA || PC.TP_INDICADOR_PEDAGIO || PC.VL_PEDAGIO) as BRAKEGROUP ")
					.append(" FROM 	PARAMETRO_CLIENTE PC, ").append("		DIVISAO_CLIENTE DC, ").append("		GENERALIDADE_CLIENTE G,")
					.append("		TABELA_DIVISAO_CLIENTE TDC, ").append("		UNIDADE_FEDERATIVA UF_ORIGEM, ").append("		UNIDADE_FEDERATIVA UF_DESTINO ")
					.append("WHERE").append("	DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
					.append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
					.append("	AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA").append("	AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
					.append("	AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)").append("	AND DC.ID_CLIENTE = ?")
					.append("	AND DC.ID_DIVISAO_CLIENTE = ?").append("	AND TDC.ID_TABELA_DIVISAO_CLIENTE = ?").append("   AND PC.ID_PARAMETRO_CLIENTE IN ( ");
		}

		boolean primeiro = true;
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if (primeiro) {
				sql.append(pc);
				primeiro = false;
			} else {
				sql.append(", " + pc);
			}
		}
		sql.append(") ");
		sql.append(" ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP ");
		return sql.toString();
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}

	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}

	public void setEmitirTabelasClientesService(EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
