package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.util.emitirMaioresDevedoresSqlService"
 */

public class EmitirMaioresDevedoresSqlService{

	private DiaUtils diaUtils;
	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}
	
	private EnderecoPessoaService enderecoPessoaService;
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	} 
	
	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
		
	public void getSqlAVencerVencidoNaoFaturado(TypedFlatMap map, SqlTemplate sql){
		getSqlCliente(sql);
		SqlTemplate sqlValor = getSqlValorAVencerVencidoNaoFaturado(map);

		sql.addFrom("("+sqlValor.getSql()+")", "VALOR");
		sql.addCriteriaValue(sqlValor.getCriteria());
	}
	
	public void getSqlEmProtesto(TypedFlatMap map, SqlTemplate sql){
		getSqlCliente(sql);
		SqlTemplate sqlValor = getSqlValorEmProtesto(map);

		sql.addFrom("("+sqlValor.getSql()+")", "VALOR");
		sql.addCriteriaValue(sqlValor.getCriteria());
		
	}	
	
	public void getSqlCliente(SqlTemplate sql){
		
		sql.addProjection("VALOR.A_VENCER", "A_VENCER");
		sql.addProjection("VALOR.VENCIDO", "VENCIDO");
		sql.addProjection("VALOR.NAO_FATURADO", "NAO_FATURADO");
		sql.addProjection("VALOR.NR_NAO_FATURADO", "NR_NAO_FATURADO");
		
		/* LMS-2683 */
		sql.addProjection("VALOR.VL_DESCONTO_VENC", "VL_DESCONTO_VENC");
		sql.addProjection("VALOR.VL_REC_PARCIAL_VENC", "VL_REC_PARCIAL_VENC");
		sql.addProjection("VALOR.VL_SALDO_VENC", "VL_SALDO_VENC");		
		sql.addProjection("VALOR.VL_DESCONTO_A_VENC", "VL_DESCONTO_A_VENC");
		sql.addProjection("VALOR.VL_REC_PARCIAL_A_VENC", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("VALOR.VL_SALDO_A_VENC", "VL_SALDO_A_VENC");	
		
		sql.addProjection("FIL_COB.SG_FILIAL", "SG_FILIAL_COB");
		sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO", "IDENTIFICACAO");
		sql.addProjection("P.NM_PESSOA", "CLIENTE");
		sql.addProjection("P.ID_PESSOA", "ID_CLIENTE");
		
		// TELEFONE ENDEREÇO
		sql.addProjection("(SELECT DECODE(TE.NR_DDI, null, '', '+' || TE.NR_DDI || ' ') || DECODE(TE.NR_DDD, null, '', '(' || TE.NR_DDD || ') ') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.ID_ENDERECO_PESSOA = F_BUSCA_ENDERECO_PESSOA(P.ID_PESSOA, 'COB', SYSDATE)))", "TELEFONE");
		
		sql.addFrom("PESSOA", "P");
		sql.addJoin("VALOR.CLIENTE", "P.ID_PESSOA");
		sql.addFrom("CLIENTE", "C");
		sql.addJoin("C.ID_CLIENTE", "P.ID_PESSOA");
		sql.addFrom("FILIAL", "FIL_COB");
		sql.addJoin("FIL_COB.ID_FILIAL", "C.ID_FILIAL_COBRANCA");
		
		sql.addOrderBy("(A_VENCER + VENCIDO + NAO_FATURADO)", "DESC");
		
	}
	
	/**
	 * Monta o sql do subselect dos valores do dos 'a vencer', 'vencido' e 'não faturado'
	 */
	public SqlTemplate getSqlValorAVencerVencidoNaoFaturado(TypedFlatMap map){
		SqlTemplate sql = new SqlTemplate();
		SqlTemplate sqlAVencer = getSqlFaturaAVencer(map, false);
		SqlTemplate sqlVencido = getSqlFaturaVencido(map, false);
		SqlTemplate sqlNaoFaturado = getSqlDoctoServicoSintetico(map);

		sql.addProjection("VALOR.CLIENTE", "CLIENTE");
		sql.addProjection("SUM(VALOR.A_VENCER)", "A_VENCER");
		sql.addProjection("SUM(VALOR.VENCIDO)", "VENCIDO");
		sql.addProjection("SUM(VALOR.NAO_FATURADO)", "NAO_FATURADO");
		sql.addProjection("SUM(VALOR.NR_NAO_FATURADO)", "NR_NAO_FATURADO");
		
		/* LMS-2683 */
		sql.addProjection("SUM(VALOR.VL_DESCONTO_VENC)", "VL_DESCONTO_VENC");
		sql.addProjection("SUM(VALOR.VL_REC_PARCIAL_VENC)", "VL_REC_PARCIAL_VENC");
		sql.addProjection("SUM(VALOR.VL_SALDO_VENC)", "VL_SALDO_VENC");
		sql.addProjection("SUM(VALOR.VL_DESCONTO_A_VENC)", "VL_DESCONTO_A_VENC");
		sql.addProjection("SUM(VALOR.VL_REC_PARCIAL_A_VENC)", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("SUM(VALOR.VL_SALDO_A_VENC)", "VL_SALDO_A_VENC");	
		
		StringBuffer from = new StringBuffer();
		
		/* Monta o miolo do select */
		from.append(sqlAVencer.getSql());
		from.append("\n UNION \n");
		from.append(sqlVencido.getSql()); 
		from.append("\n UNION \n");
		from.append(sqlNaoFaturado.getSql());
		
		sql.addCriteriaValue(sqlAVencer.getCriteria());
		sql.addCriteriaValue(sqlVencido.getCriteria());
		sql.addCriteriaValue(sqlNaoFaturado.getCriteria());
		
		/* Insere o miolo na clausula FROM do select */
		sql.addFrom("(" + from.toString() + ")", "VALOR");
		sql.addGroupBy("VALOR.CLIENTE");
		
		return sql;
	}
	
	/**
	 * Monta o sql do subselect dos valores do dos 'a vencer', 'vencido' e 'não faturado'
	 */
	public SqlTemplate getSqlValorEmProtesto(TypedFlatMap map){
		SqlTemplate sql = new SqlTemplate();
		SqlTemplate sqlAVencer = getSqlFaturaAVencer(map, true);
		SqlTemplate sqlVencido = getSqlFaturaVencido(map, true);

		sql.addProjection("VALOR.CLIENTE", "CLIENTE");
		sql.addProjection("SUM(VALOR.A_VENCER)", "A_VENCER");
		sql.addProjection("SUM(VALOR.VENCIDO)", "VENCIDO");
		sql.addProjection("SUM(VALOR.NAO_FATURADO)", "NAO_FATURADO");
		sql.addProjection("SUM(VALOR.NR_NAO_FATURADO)", "NR_NAO_FATURADO");

		/* LMS-2683 */
		sql.addProjection("SUM(VALOR.VL_DESCONTO_VENC)", "VL_DESCONTO_VENC");
		sql.addProjection("SUM(VALOR.VL_REC_PARCIAL_VENC)", "VL_REC_PARCIAL_VENC");
		sql.addProjection("SUM(VALOR.VL_SALDO_VENC)", "VL_SALDO_VENC");
		sql.addProjection("SUM(VALOR.VL_DESCONTO_A_VENC)", "VL_DESCONTO_A_VENC");
		sql.addProjection("SUM(VALOR.VL_REC_PARCIAL_A_VENC)", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("SUM(VALOR.VL_SALDO_A_VENC)", "VL_SALDO_A_VENC");	
		
		StringBuffer from = new StringBuffer();
		
		from.append(sqlAVencer.getSql());
		from.append("\n UNION \n");
		from.append(sqlVencido.getSql());
		
		sql.addCriteriaValue(sqlAVencer.getCriteria());
		sql.addCriteriaValue(sqlVencido.getCriteria());
		
		sql.addFrom("(" + from.toString() + ")", "VALOR");
		sql.addGroupBy("VALOR.CLIENTE");
		
		return sql;
	}	
	
	public SqlTemplate getSqlFaturaAVencer(TypedFlatMap map, boolean blEmProtesto){
		SqlTemplate sql = getSqlFatura(map, blEmProtesto);
		sql.addProjection("SUM(F_CONV_MOEDA(U.ID_PAIS, FAT.ID_MOEDA, ?, ?, SYSDATE, FAT.VL_TOTAL))","A_VENCER");
		sql.addProjection("0", "VENCIDO");
		sql.addProjection("0", "NAO_FATURADO");
		sql.addProjection("0", "NR_NAO_FATURADO");
		
		/* LMS-2683 */
		sql.addProjection("0", "VL_DESCONTO_VENC");
		sql.addProjection("0", "VL_REC_PARCIAL_VENC");
		sql.addProjection("0", "VL_SALDO_VENC");
		sql.addProjection("SUM(FAT.VL_DESCONTO)", "VL_DESCONTO_A_VENC");
		sql.addProjection("SUM((SELECT NVL(SUM(VL_PAGAMENTO), 0) FROM RELACAO_PAGTO_PARCIAL WHERE ID_FATURA = FAT.ID_FATURA))", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("SUM(FAT.VL_TOTAL - FAT.VL_DESCONTO)", "VL_SALDO_A_VENC"); // Este valor esta sendo parte sendo calculado aqui e parte no jasper	
		
		sql.addCriteria("FAT.DT_VENCIMENTO", ">", diaUtils.subtraiNDiasUteis(JTDateTimeUtils.getDataAtual(), 2, enderecoPessoaService.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getIdFilial()).getMunicipio().getIdMunicipio()));
		
		return sql;
	}
	
	public SqlTemplate getSqlFaturaVencido(TypedFlatMap map, boolean blEmProtesto){
		SqlTemplate sql = getSqlFatura(map, blEmProtesto);
		sql.addProjection("0", "A_VENCER");
		sql.addProjection("SUM(F_CONV_MOEDA(U.ID_PAIS, FAT.ID_MOEDA, ?, ?, SYSDATE, FAT.VL_TOTAL))","VENCIDO");
		sql.addProjection("0", "NAO_FATURADO");
		sql.addProjection("0", "NR_NAO_FATURADO");

		/* LMS-2683 */
		sql.addProjection("SUM(FAT.VL_DESCONTO)", "VL_DESCONTO_VENC");
		sql.addProjection("SUM((SELECT NVL(SUM(VL_PAGAMENTO), 0) FROM RELACAO_PAGTO_PARCIAL WHERE ID_FATURA = FAT.ID_FATURA))", "VL_REC_PARCIAL_VENC");
		sql.addProjection("SUM(FAT.VL_TOTAL - FAT.VL_DESCONTO)", "VL_SALDO_VENC"); // Este valor esta sendo parte sendo calculado aqui e parte no jasper	
		sql.addProjection("0", "VL_DESCONTO_A_VENC");
		sql.addProjection("0", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("0", "VL_SALDO_A_VENC");	
		
		sql.addCriteria("FAT.DT_VENCIMENTO", "<=", diaUtils.subtraiNDiasUteis(JTDateTimeUtils.getDataAtual(), 2, enderecoPessoaService.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getIdFilial()).getMunicipio().getIdMunicipio()));

		return sql;
	}	
	
	public SqlTemplate getSqlFatura(TypedFlatMap map, boolean blEmProtesto){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("FAT.ID_CLIENTE", "CLIENTE");
		
		sql.addFrom("FATURA", "FAT");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("REDECO", "RED");
		sql.addFrom("PESSOA", "PF");
		sql.addFrom("ENDERECO_PESSOA", "EF");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "U");
		
		sql.addJoin("FAT.ID_FILIAL", "PF.ID_PESSOA");
		sql.addJoin("FAT.ID_CLIENTE", "CLI.ID_CLIENTE");
		sql.addJoin("EF.ID_ENDERECO_PESSOA", "PF.ID_ENDERECO_PESSOA");
		sql.addJoin("M.ID_MUNICIPIO", "EF.ID_MUNICIPIO");
		sql.addJoin("U.ID_UNIDADE_FEDERATIVA", "M.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("FAT.ID_REDECO", "RED.ID_REDECO(+)");

		/* Os filtros da conversão de moeda devem ser os primeiros pq são filtros para a projection */
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(map.getLong("moeda.idMoeda"));
		
		// Caso o tipo de cliente seja especial, filtrar tmb pelo tipo filial cliente especial
		if( map.getDomainValue("tpCliente").getValue().equals("S") ){
			sql.addCriteriaIn("CLI.TP_CLIENTE", new Object[]{"S", "F"});
		}else{
			sql.addCriteria("CLI.TP_CLIENTE", "=", map.getDomainValue("tpCliente").getValue());
		}
		
		if (blEmProtesto){
			sql.addFrom("BOLETO", "BOL");
			sql.addJoin("FAT.ID_BOLETO", "BOL.ID_BOLETO");
			sql.addCriteria("FAT.TP_SITUACAO_FATURA", "=", "BL");
			sql.addCriteria("BOL.TP_SITUACAO_BOLETO", "=", "BP");
		} else {
			sql.addCustomCriteria("((FAT.TP_SITUACAO_FATURA IN ('RC', 'DI', 'EM', 'BL')) OR (FAT.TP_SITUACAO_FATURA = 'RE' AND RED.TP_FINALIDADE = 'CF'))");
		}
		
		mountCriteria(map, sql);		
		
		sql.addGroupBy("FAT.ID_CLIENTE");

		return sql;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/02/2007
	 *
	 * @param map
	 * @param blEmProtesto
	 * @param sql
	 *
	 */
	private void mountCriteria(TypedFlatMap map, SqlTemplate sql) {
		
		sql.addCriteria("FAT.TP_MODAL", "=", map.getString("modal"));			
		sql.addCriteria("FAT.TP_ABRANGENCIA", "=", map.getString("abrangencia"));			
		sql.addCriteria("FAT.ID_FILIAL", "=", map.getLong("filial.idFilial"));
		
		if (map.getDomainValue("tipoFilial") != null && !map.getDomainValue("tipoFilial").getValue().equals("") ){
			sql.addFrom("HISTORICO_FILIAL", "HF");
			sql.addJoin("FAT.ID_FILIAL", "HF.ID_FILIAL");

			sql.addCriteria("HF.TP_FILIAL", "=", map.getString("tipoFilial"));
			sql.addCustomCriteria("? BETWEEN HF.DT_REAL_OPERACAO_INICIAL AND HF.DT_REAL_OPERACAO_FINAL");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}
		
		if (map.getLong("regional.idRegional") != null) {
			sql.addFrom("REGIONAL_FILIAL", "RF");
			sql.addJoin("RF.ID_FILIAL", "FAT.ID_FILIAL");
			
			sql.addCriteria("RF.ID_REGIONAL", "=", map.getLong("regional.idRegional"));
			sql.addCustomCriteria("? BETWEEN RF.DT_VIGENCIA_INICIAL and RF.DT_VIGENCIA_FINAL", JTDateTimeUtils.getDataAtual());
		}
		
		sql.addCriteria("FAT.DT_VENCIMENTO", "<=", map.getYearMonthDay("limiteVencimento"));
		
	}
	
	public SqlTemplate getSqlDoctoServicoSintetico(TypedFlatMap map){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("DDSF.ID_CLIENTE", "CLIENTE");
		sql.addProjection("0", "A_VENCER");
		sql.addProjection("0", "A_VENCER");
		sql.addProjection("SUM(F_CONV_MOEDA(U.ID_PAIS, DS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO))", "NAO_FATURADO");
		sql.addProjection("COUNT(DDSF.ID_DEVEDOR_DOC_SERV_FAT)", "NR_NAO_FATURADO");
		
		/* LMS-2683 */
		sql.addProjection("0", "VL_DESCONTO_VENC");
		sql.addProjection("0", "VL_REC_PARCIAL_VENC");
		sql.addProjection("0", "VL_SALDO_VENC");	
		sql.addProjection("0", "VL_DESCONTO_A_VENC");
		sql.addProjection("0", "VL_REC_PARCIAL_A_VENC");
		sql.addProjection("0", "VL_SALDO_A_VENC");	
		
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(map.getLong("moeda.idMoeda"));
		
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("SERVICO", "SER");
		sql.addFrom("PESSOA", "PF");
		sql.addFrom("ENDERECO_PESSOA", "EF");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "U");

		sql.addJoin("DS.ID_DOCTO_SERVICO", "DDSF.ID_DOCTO_SERVICO");
		sql.addJoin("DDSF.ID_CLIENTE", "CLI.ID_CLIENTE");
		sql.addJoin("DDSF.ID_FILIAL", "PF.ID_PESSOA");
		sql.addJoin("DS.ID_SERVICO", "SER.ID_SERVICO(+)");
		sql.addJoin("EF.ID_ENDERECO_PESSOA", "PF.ID_ENDERECO_PESSOA");
		sql.addJoin("M.ID_MUNICIPIO", "EF.ID_MUNICIPIO");
		sql.addJoin("U.ID_UNIDADE_FEDERATIVA", "M.ID_UNIDADE_FEDERATIVA");
		
		sql.addCustomCriteria("DDSF.TP_SITUACAO_COBRANCA IN ('P', 'C')");
		sql.addCustomCriteria("DS.NR_DOCTO_SERVICO > 0");
		
		sql.addCriteria("SER.TP_MODAL", "=", map.getString("modal"));			
		sql.addCriteria("SER.TP_ABRANGENCIA", "=", map.getString("abrangencia"));			
		sql.addCriteria("DDSF.ID_FILIAL", "=", map.getLong("filial.idFilial"));

		// Caso o tipo de cliente seja especial, filtrar tmb pelo tipo filial cliente especial
		if( map.getDomainValue("tpCliente").getValue().equals("S") ){
			sql.addCriteriaIn("CLI.TP_CLIENTE", new Object[]{"S", "F"});
		}else{
			sql.addCriteria("CLI.TP_CLIENTE", "=", map.getDomainValue("tpCliente").getValue());
		}
		
		if (map.getDomainValue("tipoFilial") != null && !map.getDomainValue("tipoFilial").getValue().equals("") ){
			sql.addFrom("HISTORICO_FILIAL", "HF");
			sql.addJoin("DDSF.ID_FILIAL", "HF.ID_FILIAL");

			sql.addCriteria("HF.TP_FILIAL", "=", map.getString("tipoFilial"));
			sql.addCustomCriteria("? BETWEEN HF.DT_REAL_OPERACAO_INICIAL AND HF.DT_REAL_OPERACAO_FINAL");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}
		
		if (map.getLong("regional.idRegional") != null) {
			sql.addFrom("REGIONAL_FILIAL", "RF");
			sql.addJoin("RF.ID_FILIAL", "DDSF.ID_FILIAL");
			
			sql.addCriteria("RF.ID_REGIONAL", "=", map.getLong("regional.idRegional"));
			sql.addCustomCriteria("? BETWEEN RF.DT_VIGENCIA_INICIAL and RF.DT_VIGENCIA_FINAL");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}		
		
		sql.addGroupBy("DDSF.ID_CLIENTE");
		
		return sql;
	}
	
	/**
	 * Monta o filterSummary
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 07/02/2007
	 *
	 * @param sql
	 * @param tfm
	 *
	 */
	public void mountFilterSummary(SqlTemplate sql, TypedFlatMap tfm){
		
		if( tfm.getYearMonthDay("limiteVencimento")!= null ){
			sql.addFilterSummary("limiteVencimento", tfm.getYearMonthDay("limiteVencimento"));
		}
		if( StringUtils.isNotBlank(tfm.getDomainValue("situacao").getValue()) ){
			sql.addFilterSummary("situacao", domainValueService.findDomainValueByValue("DM_SITUACAO_MAIORES_DEVEDORES", tfm.getDomainValue("situacao").getValue()).getDescription());
		}
		if( StringUtils.isNotBlank(tfm.getString("sgDsRegional")) ){
			sql.addFilterSummary("regional", tfm.getString("sgDsRegional"));
		}
		if( StringUtils.isNotBlank(tfm.getString("sgFilial")) ){
			sql.addFilterSummary("filialCobranca", tfm.getString("sgFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"));
		}
		if( StringUtils.isNotBlank(tfm.getDomainValue("tipoFilial").getValue()) ){
			sql.addFilterSummary("tipoFilial", domainValueService.findDomainValueByValue("DM_TIPO_FILIAL", tfm.getDomainValue("tipoFilial").getValue()).getDescription());
		}
		if(StringUtils.isNotBlank(tfm.getString("dsTipoCliente"))){
			sql.addFilterSummary("tipoCliente", tfm.getString("dsTipoCliente"));
		}
		if( StringUtils.isNotBlank(tfm.getDomainValue("modal").getValue()) ){
			sql.addFilterSummary("modal", domainValueService.findDomainValueByValue("DM_MODAL", tfm.getDomainValue("modal").getValue()).getDescription());
		}
		if( StringUtils.isNotBlank(tfm.getDomainValue("abrangencia").getValue()) ){
			sql.addFilterSummary("abrangencia", domainValueService.findDomainValueByValue("DM_ABRANGENCIA", tfm.getDomainValue("abrangencia").getValue()).getDescription());
		}
		if( tfm.getLong("qtdCliente") != null ){
			sql.addFilterSummary("quantidadeClientes", tfm.getLong("qtdCliente"));
		}
		if( StringUtils.isNotBlank(tfm.getString("moeda.dsSimbolo")) ){
			sql.addFilterSummary("moedaExibicao", tfm.getString("moeda.dsSimbolo"));
		}
		
		sql.addFilterSummary("soTotais", domainValueService.findDomainValueByValue("DM_SIM_NAO", tfm.getBoolean("soTotais") ? "S" : "N").getDescription());		
		
	}
	
	/**
	 * Monta o sql do subreport analítico
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/02/2007
	 *
	 * @param map
	 * @return
	 *
	 */
	public SqlTemplate getSqlDoctoServicoAnalitico(Long idCliente, TypedFlatMap map){
		
		SqlTemplate sql = new SqlTemplate();		
		
		sql.addProjection("FCOB.SG_FILIAL", "COB");
		sql.addProjection("FORI.SG_FILIAL", "SIGLA_FILIAL");
		sql.addProjection("TO_CHAR(FAT.NR_FATURA, '0000000000')", "DOCTO");
		
		sql.addProjection(new StringBuilder()
								.append(" (CASE ")
								.append("  WHEN FAT.TP_SITUACAO_FATURA = 'DI' OR ")
								.append("    FAT.TP_SITUACAO_FATURA = 'RC' OR ")
								.append("    FAT.TP_SITUACAO_FATURA = 'EM' ")
								.append("    THEN 'F' ")
								.append("  WHEN FAT.TP_SITUACAO_FATURA = 'BL' ")
								.append("    THEN ")
								.append("      (CASE")
								.append("       WHEN BOL.TP_SITUACAO_BOLETO = 'DB' OR")
								.append("            BOL.TP_SITUACAO_BOLETO = 'DI' OR")
								.append("            BOL.TP_SITUACAO_BOLETO = 'GM' OR")
								.append("            BOL.TP_SITUACAO_BOLETO = 'EM' OR")
								.append("            BOL.TP_SITUACAO_BOLETO = 'GE'")
								.append("            THEN 'B'")
								.append("       WHEN BOL.TP_SITUACAO_BOLETO = 'BN'")
								.append("            THEN 'BB'")
								.append("       WHEN BOL.TP_SITUACAO_BOLETO = 'BP'")
								.append("            THEN 'BC'")
								.append("       END)")
								.append("  WHEN FAT.TP_SITUACAO_FATURA = 'RE' ")
								.append("    THEN")
								.append("      (CASE")
								.append("       WHEN RED.TP_SITUACAO_REDECO <> 'CA' ")
								.append("       THEN 'FR'")
								.append("  END)")
								.append("END) ").toString(), "TP");
		
		sql.addProjection("FAT.DT_VENCIMENTO", "VENCTO");
		sql.addProjection("F_CONV_MOEDA(U.ID_PAIS, FAT.ID_MOEDA, ?, ?, SYSDATE, FAT.VL_TOTAL)", "VALOR");
		
		/* LMS-2683 */
		sql.addProjection("FAT.VL_DESCONTO", "DESCONTO");
		sql.addProjection("(SELECT NVL(SUM(VL_PAGAMENTO), 0) FROM RELACAO_PAGTO_PARCIAL WHERE ID_FATURA = FAT.ID_FATURA)", "RECEBIMENTO_PARCIAL");
		sql.addProjection("FAT.VL_TOTAL - FAT.VL_DESCONTO", "SALDO"); // Este valor esta sendo parte sendo calculado aqui e parte no jasper
		
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(map.getLong("moeda.idMoeda"));
		
		sql.addFrom("FATURA", "FAT");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("BOLETO", "BOL");
		sql.addFrom("REDECO", "RED");
		sql.addFrom("FILIAL", "FORI");
		sql.addFrom("PESSOA", "PFILORI");
		sql.addFrom("FILIAL", "FCOB");
		sql.addFrom("ENDERECO_PESSOA", "EF");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "U");
		
		sql.addJoin("FAT.ID_FILIAL", "FORI.ID_FILIAL");
		sql.addJoin("FAT.ID_CLIENTE", "CLI.ID_CLIENTE");
		sql.addJoin("CLI.ID_FILIAL_COBRANCA", "FCOB.ID_FILIAL");
		sql.addJoin("FAT.ID_FILIAL", "PFILORI.ID_PESSOA");
		sql.addJoin("EF.ID_ENDERECO_PESSOA", "PFILORI.ID_ENDERECO_PESSOA");
		sql.addJoin("M.ID_MUNICIPIO", "EF.ID_MUNICIPIO");
		sql.addJoin("U.ID_UNIDADE_FEDERATIVA", "M.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("FAT.ID_REDECO", "RED.ID_REDECO(+)");
		sql.addJoin("FAT.ID_BOLETO", "BOL.ID_BOLETO(+)"); 
		
		
		if ( !map.getString("situacao").equalsIgnoreCase("V") ){
			sql.addCriteria("FAT.TP_SITUACAO_FATURA", "=", "BL");
			sql.addCriteria("BOL.TP_SITUACAO_BOLETO", "=", "BP");
		} else {
			sql.addCustomCriteria("((FAT.TP_SITUACAO_FATURA IN ('RC', 'DI', 'EM', 'BL')) OR (FAT.TP_SITUACAO_FATURA = 'RE' AND RED.TP_FINALIDADE = 'CF'))");
		}
		
		/* Caso a situacao seja diferente de V, passa true por parametro */
		mountCriteria(map, sql);	
		
		sql.addCriteria("CLI.ID_CLIENTE", "=", idCliente);
		
		sql.addOrderBy("FAT.DT_VENCIMENTO");
		
		return sql;
	}
	
	/**
	 * Itera o resultSet para calcular o total geral
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 14/03/2007
	 *
	 * @param sql
	 * @param qtClientes
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetExtractor getResultSetExtractor(final Long qtClientes){
		ResultSetExtractor rse = new ResultSetExtractor(){

			Map row;
			List retorno = new ArrayList();
			BigDecimal valorTotal = BigDecimal.ZERO;
			BigDecimal tgDesconto = BigDecimal.ZERO; 
			BigDecimal tgRecebParcial = BigDecimal.ZERO; 
			BigDecimal tgSaldo = BigDecimal.ZERO; 
			BigDecimal saldoAVencer = BigDecimal.ZERO;
			BigDecimal saldoVencido = BigDecimal.ZERO;
			
			@SuppressWarnings({ "unchecked" })
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				// Controla o número de objetos que serão inseridos na List
				int count = 0;
				
				while( rs.next() ){
					// Insere os clientes na Lista de retorno, de acordo com a quantidade informada no filtro 
					if( qtClientes == null || count < qtClientes ){
						row = new HashMap(); 
						
						row.put("A_VENCER", rs.getBigDecimal("A_VENCER"));
						row.put("VENCIDO", rs.getBigDecimal("VENCIDO"));
						row.put("NAO_FATURADO", rs.getBigDecimal("NAO_FATURADO"));
						row.put("NR_NAO_FATURADO", rs.getLong("NR_NAO_FATURADO"));
						row.put("TP_IDENTIFICACAO", rs.getString("TP_IDENTIFICACAO"));
						row.put("IDENTIFICACAO", rs.getString("IDENTIFICACAO"));
						row.put("CLIENTE", rs.getString("CLIENTE"));
						row.put("SG_FILIAL_COB", rs.getString("SG_FILIAL_COB"));
						row.put("ID_CLIENTE", rs.getLong("ID_CLIENTE"));
						row.put("TELEFONE", rs.getString("TELEFONE"));
						row.put("TOTAL_GERAL", BigDecimal.ZERO);
						row.put("VL_DESCONTO_VENC", rs.getBigDecimal("VL_DESCONTO_VENC"));
						row.put("VL_REC_PARCIAL_VENC", rs.getBigDecimal("VL_REC_PARCIAL_VENC"));
						row.put("VL_SALDO_VENC", rs.getBigDecimal("VL_SALDO_VENC"));
						row.put("VL_DESCONTO_A_VENC", rs.getBigDecimal("VL_DESCONTO_A_VENC"));
						row.put("VL_REC_PARCIAL_A_VENC", rs.getBigDecimal("VL_REC_PARCIAL_A_VENC"));
						row.put("VL_SALDO_A_VENC", rs.getBigDecimal("VL_SALDO_A_VENC"));	
						
						retorno.add(row);
					}
					
					// Soma os valores A_VENCER, VENCIDO e NAO_FATURADO de cada linha do resultSet
					valorTotal = valorTotal.add(rs.getBigDecimal("A_VENCER")).add(rs.getBigDecimal("VENCIDO")).add(rs.getBigDecimal("NAO_FATURADO"));
					
					saldoVencido = rs.getBigDecimal("VL_SALDO_VENC").subtract(rs.getBigDecimal("VL_REC_PARCIAL_VENC"));
					saldoAVencer = rs.getBigDecimal("VL_SALDO_A_VENC").subtract(rs.getBigDecimal("VL_REC_PARCIAL_A_VENC"));
					
					tgDesconto = tgDesconto.add(rs.getBigDecimal("VL_DESCONTO_VENC")).add(rs.getBigDecimal("VL_DESCONTO_A_VENC"));
					tgRecebParcial = tgRecebParcial.add(rs.getBigDecimal("VL_REC_PARCIAL_VENC")).add(rs.getBigDecimal("VL_REC_PARCIAL_A_VENC"));
					tgSaldo = tgSaldo.add(saldoVencido.add(saldoAVencer)).add(rs.getBigDecimal("NAO_FATURADO"));
					
					++count;
				}
				
				// Adiciona o valor total ao último objeto do Map
				if (retorno.size() > 1) {
					((Map)retorno.get(retorno.size() - 1)).put("TOTAL_GERAL", valorTotal);
					((Map)retorno.get(retorno.size() - 1)).put("TOTAL_GERAL_DESCONTO", tgDesconto);
					((Map)retorno.get(retorno.size() - 1)).put("TOTAL_GERAL_RECEB_PARCIAL", tgRecebParcial);
					((Map)retorno.get(retorno.size() - 1)).put("TOTAL_GERAL_SALDO", tgSaldo);
				}
				
				return retorno;
			}
		};
		
		return rse;
	}

}
