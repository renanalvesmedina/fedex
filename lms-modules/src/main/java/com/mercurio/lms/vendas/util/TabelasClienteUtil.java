package com.mercurio.lms.vendas.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe com metodos uteis para os relatorios de vendas
 * 
 * @author Alexandre Poletto, Diego Pacheco, Rodrigo Dias e Maximiliano Guzenski
 * @version 2.5
 * 
 * @deprecated
 */
public class TabelasClienteUtil {

	private static ThreadLocal threadLocal = new ThreadLocal();

	/* Constantes de SQL */
	private static final String SQL_PARAMETROS = "SELECT VL_ADVALOREM, TP_INDICADOR_ADVALOREM FROM PARAMETRO_CLIENTE WHERE ID_PARAMETRO_CLIENTE = ?";

	/* Constantes de Retorno */
	public static final int RETORNO_LIST = 0;
	public static final int RETORNO_DATASOURCE = 1;

	/* Constantes de SubRelatorios */
	public static final int SUBREPORT_FORMALIDADES = 0;
	public static final int SUBREPORT_GENERALIDADES = 1;
	public static final int SUBREPORT_SERVICOSAD = 2;
	public static final int SUBREPORT_SERVICOSCONTRATADOS = 3;
	public static final int SUBREPORT_SERVICOSNAOCONTRATADOS = 4;
	public static final int SUBREPORT_COLETA = 5;
	public static final int SUBREPORT_ENTREGA = 6;
	public static final int SUBREPORT_COLETA_CLIENTE = 7;
	public static final int SUBREPORT_ENTREGA_CLIENTE = 8;
	public static final int SUBREPORT_LEGENDAS = 9;
	public static final int SUBREPORT_AEREO = 10;
	public static final int SUBREPORT_FORMALIDADES_AEREO = 11;	
	public static final int SUBREPORT_COLETA_ROTA = 12;
	public static final int SUBREPORT_ENTREGA_ROTA = 13;
	public static final int SUBREPORT_GENERALIDADES_TABELA_PRECO = 14;
	public static final int SUBREPORT_GENFOR_TABELA_PRECO = 15;
	public static final int SUBREPORT_GENFOR_VENDAS = 16;
	public static final int SUBREPORT_FORMALIDADES_AEREO_VOLUME = 17;
	public static final int SUBREPORT_FORMALIDADES_AEREO_PERCENTUAL = 18;
	public static final int SUBREPORT_TAXA_TERRESTRE = 19;
	public static final int SUBREPORT_TAXA_COMBUSTIVEL = 20;	
	public static final int SUBREPORT_GENERALIDADE_DIFICULDADE_ENTREGA = 21;

	/* Constantes de chaves dos SubRelatorios */
	public static final String KEY_PARAMETER_FORMALIDADES = "SUBREPORTFORMALIDADES";
	public static final String KEY_PARAMETER_FORMALIDADES_AEREO = "SUBREPORTFORMALIDADESAEREO";
	public static final String KEY_PARAMETER_FORMALIDADES_AEREO_VOLUME = "SUBREPORTFORMALIDADESAEREOVOLUME";
	public static final String KEY_PARAMETER_FORMALIDADES_AEREO_PERCENTUAL = "SUBREPORTFORMALIDADESAEREOPERCENTUAL";
	public static final String KEY_PARAMETER_GENERALIDADES = "SUBREPORTGENERALIDADES";
	public static final String KEY_PARAMETER_SERVICOSAD = "SUBREPORTSERVICOSAD";
	public static final String KEY_PARAMETER_SERVICOSCONTRATADOS = "SUBREPORTSERVICOADCONTR";
	public static final String KEY_PARAMETER_SERVICOSADICIONAIS = "SUBREPORTSERVICOADNAOCONTR";
	public static final String KEY_PARAMETER_COLETA = "srcColeta";
	public static final String KEY_PARAMETER_ENTREGA = "srcEntrega";
	public static final String KEY_PARAMETER_LEGENDAS = "srcLegendas";
	public static final String KEY_PARAMETER_AEREO = "srcAereo";
	public static final String KEY_PARAMETER_PAGEFOOTER = "PAGEFOOTER";
	public static final String KEY_PARAMETER_TAXA_TERRESTRE = "SUBREPORTTAXATERRESTRE";
	public static final String KEY_PARAMETER_TAXA_COMBUSTIVEL = "SUBREPORTTAXACOMBUSTIVEL";	
	public static final String KEY_PARAMETER_DIFICULDADE_ENTREGA = "SUBREPORTDIFICULDADEENTREGA";
	public static final Object KEY_PARAMETER_LEGENDA_GENERALIDADE = "LEGENDAGENERALIDADE";

	/* Constantes de Calculo */
	public static final String ACRESCIMO = "A";
	public static final String DESCONTO = "D";
	public static final String TABELA = "T";
	public static final String VALOR = "V";
	public static final String METROS_CUBICOS = "M";
	public static final String PONTOS_PESO = "P";

	/* Constantes de Generalidades */
	public static final String REENTREGA = "REENTREGA";
	public static final String DEVOLUCAO = "DEVOLUCAO";

	/* Constantes de Formatação */
	public static final String FORMATO_2_CASAS = "##,###,###,###,##0.00";
	public static final String FORMATO_3_CASAS = "##,###,###,###,##0.000";
	public static final String FORMATO_5_CASAS = "##,###,###,###,##0.00000";

	/*Constantes de Path*/
	public static final String PATH_TABELAPRECOS = "com/mercurio/lms/tabelaprecos/";
	public static final String PATH_VENDAS = "com/mercurio/lms/vendas/";

	/**
	 * Metodo que busca todos os subRelatorios passados no array de inteiros e
	 * adiciona eles em um map de parametros do relatorio, também adiciona o
	 * simbolo da moeda (DS_SIMBOLO), e o dataSourceAuxiliar (DataSourceAux) e
	 * os dados do Header (HEADER)
	 */
	public static Map getAllChoiceReportParameters(Map parameters,
			Boolean isTabelaNova, int[] subReport,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getReportParameters(parameters, null, isTabelaNova, 0, null,
				subReport, configuracoesFacade, jdbcTemplate);
	}
	
	private static String montaSQLTaxaCombustivel() {
		StringBuilder sql = new StringBuilder()
				.append("SELECT distinct ufo.sg_unidade_federativa as ORIGEM, ufd.sg_unidade_federativa as DESTINO,")
				.append(" tp.id_tabela_preco, cd_parcela_preco, ")
				.append(" vfp.VL_FIXO, fp.id_faixa_progressiva ")
				.append(" FROM tabela_preco tp, ")
				.append(" tabela_preco_parcela tpp, ")
				.append(" parcela_preco pp, ")
				.append(" faixa_progressiva fp, ")
				.append(" valor_faixa_progressiva vfp, ")
				.append(" rota_preco rp, ")
				.append(" unidade_federativa ufo, ")
				.append(" unidade_federativa ufd ")
				.append(" WHERE ")
				.append(" tp.id_tabela_preco = tpp.id_tabela_preco ")
				.append(" and tpp.id_parcela_preco = pp.id_parcela_preco ")
				.append(" and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela ")
				.append(" and fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
				.append(" and vfp.id_rota_preco = rp.id_rota_preco ")
				.append(" and pp.cd_parcela_preco = 'IDTaxaCombustivel' ")
				.append(" and rp.id_uf_origem  = ufo.id_unidade_federativa ")
				.append(" and rp.id_uf_destino = ufd.id_unidade_federativa ")
				.append(" and tp.id_tabela_preco = ?")
				.append(" order by ufo.sg_unidade_federativa, ufd.sg_unidade_federativa ");
		return sql.toString();
	}

	private static String montaSQLTaxaCombustivelCliente() {
		StringBuilder sql = new StringBuilder()
				.append("SELECT distinct ufo.sg_unidade_federativa as ORIGEM, ")
				.append(" ufd.sg_unidade_federativa as DESTINO, ")
				.append(" tp.id_tabela_preco, cd_parcela_preco, ")
				.append(" tc.TP_TAXA_INDICADOR, ")
				.append(" tc.VL_TAXA, ")
				.append(" vfp.VL_FIXO, fp.id_faixa_progressiva ")

				.append(" FROM tabela_preco tp, ")
				.append(" tabela_preco_parcela tpp, ")
				.append(" parcela_preco pp, ")
				.append(" faixa_progressiva fp, ")
				.append(" valor_faixa_progressiva vfp, ")
				.append(" rota_preco rp, ")
				.append(" taxa_cliente tc, ")
				.append(" unidade_federativa ufo, ")
				.append(" unidade_federativa ufd ")

				.append(" WHERE ")
				.append(" tp.id_tabela_preco = tpp.id_tabela_preco ")
				.append(" and tpp.id_parcela_preco = pp.id_parcela_preco ")
				.append(" and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela ")
				.append(" and fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
				.append(" and vfp.id_rota_preco = rp.id_rota_preco ")
				.append(" and pp.cd_parcela_preco = 'IDTaxaCombustivel' ")
				.append(" and rp.id_uf_origem  = ufo.id_unidade_federativa ")
				.append(" and rp.id_uf_destino = ufd.id_unidade_federativa ")
				.append(" and pp.id_parcela_preco = tc.id_parcela_preco(+)")

				.append(" and tp.id_tabela_preco = ?")
				.append(" and ? = tc.id_parametro_cliente(+)")

				.append(" order by ufo.sg_unidade_federativa, ufd.sg_unidade_federativa ");
		return sql.toString();
	}

	private static String montaSQLTaxaTerrestre() {
		StringBuilder sql = new StringBuilder()
				.append("SELECT distinct tp.id_tabela_preco, cd_parcela_preco, VL_FAIXA_PROGRESSIVA, VL_FIXO ")
				.append("FROM tabela_preco tp, ")
				.append("tabela_preco_parcela tpp, ")
				.append("parcela_preco pp, ")
				.append("faixa_progressiva fp, ")
				.append("valor_faixa_progressiva vfp ")
				.append(" where tp.id_tabela_preco = tpp.id_tabela_preco ")
				.append("   and tpp.id_parcela_preco = pp.id_parcela_preco ")
				.append("   and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela ")
				.append("   and fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
				.append("   and pp.cd_parcela_preco = '")
				.append(ConstantesExpedicao.CD_TAXA_TERRESTRE).append("'")
				.append("   and tp.id_tabela_preco = ?");
		return sql.toString();

	}

	private static String montaSQLTaxaTerrestreCliente() {
		StringBuilder sql = new StringBuilder()
				.append("SELECT distinct tp.id_tabela_preco, cd_parcela_preco, VL_FAIXA_PROGRESSIVA, VL_FIXO, ")
				.append(" tc.TP_TAXA_INDICADOR, ")
				.append(" tc.VL_TAXA ")
				.append("FROM tabela_preco tp, ")
				.append("tabela_preco_parcela tpp, ")
				.append("parcela_preco pp, ")
				.append("taxa_cliente tc, ")
				.append("faixa_progressiva fp, ")
				.append("valor_faixa_progressiva vfp ")
				.append(" where tp.id_tabela_preco = tpp.id_tabela_preco ")
				.append("   and tpp.id_parcela_preco = pp.id_parcela_preco ")
				.append("   and tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela ")
				.append("   and fp.id_faixa_progressiva = vfp.id_faixa_progressiva ")
				.append("   and pp.id_parcela_preco = tc.id_parcela_preco(+)")
				.append("   and pp.cd_parcela_preco = '")
				.append(ConstantesExpedicao.CD_TAXA_TERRESTRE).append("'")
				.append("   and tp.id_tabela_preco = ?")
				.append("   and ? = tc.id_parametro_cliente(+)");
		return sql.toString();
	}

	private static String montaSQLColeta() {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select MO.DS_SIMBOLO , trim(to_char(VT.VL_TAXA,'999g999g990d90')) AS VL_TAXA , trim(to_char(VT.VL_EXCEDENTE,'999g999g990d90')) AS VL_EXCEDENTE, ")
			.append("VT.PS_TAXADO, PP.CD_PARCELA_PRECO ")
			.append("from ")
			.append("TABELA_PRECO TP, TABELA_PRECO_PARCELA TPP, TIPO_TABELA_PRECO TTP, ") 
			.append("SUBTIPO_TABELA_PRECO STP, MOEDA MO, PARCELA_PRECO PP, ") 
			.append("VALOR_TAXA VT ") 
			.append("WHERE ")
			.append("PP.TP_PARCELA_PRECO = 'T' AND PP.TP_PRECIFICACAO = 'T' AND ") 
			.append("TP.ID_TABELA_PRECO  =  TPP.ID_TABELA_PRECO AND ")
			.append("TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO AND ")
			.append("TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO AND ") 
			.append("TP.ID_MOEDA = MO.ID_MOEDA AND ")
			.append("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND ")
			.append("TPP.ID_TABELA_PRECO_PARCELA = VT.ID_VALOR_TAXA AND ")
			.append("PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') AND ") 
			.append("TPP.ID_TABELA_PRECO = ? ");			
		
		return sql.toString().replaceAll("\\[vlx\\]", "Coleta");
	}
		
	private static String montaSQLEntrega() {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select MO.DS_SIMBOLO , trim(to_char(VT.VL_TAXA,'999g999g990d90')) AS VL_TAXA , trim(to_char(VT.VL_EXCEDENTE,'999g999g990d90')) AS VL_EXCEDENTE, ")
				.append("VT.PS_TAXADO, PP.CD_PARCELA_PRECO ")
				.append("from ")
				.append("TABELA_PRECO TP, TABELA_PRECO_PARCELA TPP, TIPO_TABELA_PRECO TTP, ")
				.append("SUBTIPO_TABELA_PRECO STP, MOEDA MO, PARCELA_PRECO PP, ")
				.append("VALOR_TAXA VT ")
				.append("WHERE ")
				.append("PP.TP_PARCELA_PRECO = 'T' AND PP.TP_PRECIFICACAO = 'T' AND ")
				.append("TP.ID_TABELA_PRECO  =  TPP.ID_TABELA_PRECO AND ")
				.append("TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO AND ")
				.append("TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO AND ")
				.append("TP.ID_MOEDA = MO.ID_MOEDA AND ")
				.append("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND ")
				.append("TPP.ID_TABELA_PRECO_PARCELA = VT.ID_VALOR_TAXA AND ")
				.append("PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') AND ")
				.append("TPP.ID_TABELA_PRECO = ? ");
		return sql.toString().replaceAll("\\[vlx\\]", "Entrega");
	}

	private static String montaSQLColetaComRegras() {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ")
			.append("( ")
			.append("select MO.DS_SIMBOLO, VT.VL_TAXA, VT.VL_EXCEDENTE, ") 
			.append("VT.PS_TAXADO, PP.CD_PARCELA_PRECO, PP.ID_PARCELA_PRECO ")
			.append("from ")
			.append("TABELA_PRECO TP, TABELA_PRECO_PARCELA TPP, TIPO_TABELA_PRECO TTP, ") 
			.append("SUBTIPO_TABELA_PRECO STP, MOEDA MO, PARCELA_PRECO PP, ") 
			.append("VALOR_TAXA VT ") 
			.append("WHERE ")
			.append("PP.TP_PARCELA_PRECO = 'T' AND PP.TP_PRECIFICACAO = 'T' AND ") 
			.append("TP.ID_TABELA_PRECO  =  TPP.ID_TABELA_PRECO AND ")
			.append("TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO AND ")
			.append("TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO AND ") 
			.append("TP.ID_MOEDA = MO.ID_MOEDA AND ")
			.append("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND ")
			.append("TPP.ID_TABELA_PRECO_PARCELA = VT.ID_VALOR_TAXA AND ")
			.append("PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') AND ") 
			.append("TPP.ID_TABELA_PRECO = ? ")
			.append(") AA, ")
			.append("( ")
			.append("SELECT ")
			.append("TC.ID_PARCELA_PRECO AS TAXA_CLIENTE, ")
			.append("TC.TP_TAXA_INDICADOR AS TP_TAXA_INDICADOR, ") 
			.append("TC.PS_MINIMO PS_MINIMO , ")
			.append("TC.VL_TAXA as TX_VL_TAXA , ")
			.append("TC.VL_EXCEDENTE as TX_VL_EXCEDENTE ,")
			.append("PC.ID_PARAMETRO_CLIENTE, ")
			.append("PP.ID_PARCELA_PRECO ")
			.append("FROM ")
			.append("TAXA_CLIENTE TC, ")
			.append("PARAMETRO_CLIENTE PC, ") 
			.append("PARCELA_PRECO PP ")
			.append("WHERE ")
			.append("PP.ID_PARCELA_PRECO = TC.ID_PARCELA_PRECO ")
			.append("AND TC.ID_PARAMETRO_CLIENTE = PC.ID_PARAMETRO_CLIENTE ") 
				.append("AND PC.ID_PARAMETRO_CLIENTE = ? ").append(") BB ")
			.append("where AA.ID_PARCELA_PRECO = BB.ID_PARCELA_PRECO (+) ");
		
		return sql.toString().replaceAll("\\[vlx\\]", "Coleta");
	}
		
	private static String montaSQLEntregaComRegras() {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ")
				.append("( ")
				.append("select MO.DS_SIMBOLO, VT.VL_TAXA, VT.VL_EXCEDENTE, ")
				.append("VT.PS_TAXADO, PP.CD_PARCELA_PRECO, PP.ID_PARCELA_PRECO ")
				.append("from ")
				.append("TABELA_PRECO TP, TABELA_PRECO_PARCELA TPP, TIPO_TABELA_PRECO TTP, ")
				.append("SUBTIPO_TABELA_PRECO STP, MOEDA MO, PARCELA_PRECO PP, ")
				.append("VALOR_TAXA VT ")
				.append("WHERE ")
				.append("PP.TP_PARCELA_PRECO = 'T' AND PP.TP_PRECIFICACAO = 'T' AND ")
				.append("TP.ID_TABELA_PRECO  =  TPP.ID_TABELA_PRECO AND ")
				.append("TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO AND ")
				.append("TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO AND ")
				.append("TP.ID_MOEDA = MO.ID_MOEDA AND ")
				.append("TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND ")
				.append("TPP.ID_TABELA_PRECO_PARCELA = VT.ID_VALOR_TAXA AND ")
				.append("PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') AND ")
				.append("TPP.ID_TABELA_PRECO = ? ")
				.append(") AA, ")
				.append("( ")
				.append("SELECT ")
				.append("TC.ID_PARCELA_PRECO AS TAXA_CLIENTE, ")
				.append("TC.TP_TAXA_INDICADOR AS TP_TAXA_INDICADOR, ")
				.append("TC.PS_MINIMO PS_MINIMO , ")
				.append("TC.VL_TAXA as TX_VL_TAXA , ")
				.append("TC.VL_EXCEDENTE as TX_VL_EXCEDENTE ,")
				.append("PC.ID_PARAMETRO_CLIENTE, ")
				.append("PP.ID_PARCELA_PRECO ")
				.append("FROM ")
				.append("TAXA_CLIENTE TC, ")
				.append("PARAMETRO_CLIENTE PC, ")
				.append("PARCELA_PRECO PP ")
				.append("WHERE ")
				.append("PP.ID_PARCELA_PRECO = TC.ID_PARCELA_PRECO ")
				.append("AND TC.ID_PARAMETRO_CLIENTE = PC.ID_PARAMETRO_CLIENTE ")
				.append("AND PC.ID_PARAMETRO_CLIENTE = ? ").append(") BB ")
				.append("where AA.ID_PARCELA_PRECO = BB.ID_PARCELA_PRECO (+) ");

		return sql.toString().replaceAll("\\[vlx\\]", "Entrega");
	}

	private static String montaSQLColetaRota() {
		StringBuilder sql = new StringBuilder();
		sql.append("select \n")
		.append("TV.ID_PARCELA_PRECO, TV.ID_TABELA_PRECO, TV.DS_SIMBOLO, TV.CD_PARCELA_PRECO, TVC.TIPO_TAXA, TVC.ORDEM_TAXA, \n")
		.append("decode(TP_TAXA_INDICADOR, null, TV.VL_TAXA, 'T', TV.VL_TAXA, 'V', decode(TVC.VL_TAXA, 0, TV.VL_TAXA, TVC.VL_TAXA), \n")
		.append(" 'A', (TV.VL_TAXA+(TV.VL_TAXA*(TVC.VL_TAXA/100))), 'D', (TV.VL_TAXA-(TV.VL_TAXA*(TVC.VL_TAXA/100)))) as VL_TAXA, \n")

		.append("decode(TP_TAXA_INDICADOR, null, TV.PS_TAXADO, 'T', TV.PS_TAXADO, 'V', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO), \n")
		.append(" 'A', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO), 'D', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO)) as PS_TAXADO, \n")

		.append("decode(TP_TAXA_INDICADOR, null, TV.VL_EXCEDENTE, 'T', TV.VL_EXCEDENTE, 'V', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE), \n")
		.append(" 'A', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE), 'D', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE)) as VL_EXCEDENTE \n")

		.append(" from  \n")

		.append("(select PP.ID_PARCELA_PRECO, MO.DS_SIMBOLO, PP.CD_PARCELA_PRECO, TP.ID_TABELA_PRECO, VT.*  \n")
		.append(" from VALOR_TAXA VT, \n")
		.append("TABELA_PRECO_PARCELA TPP, \n")
		.append("TABELA_PRECO TP, \n")
		.append("MOEDA MO, \n")
		.append("PARCELA_PRECO PP  \n")
		.append(" where  \n") 
		.append(" VT.ID_VALOR_TAXA = TPP.ID_TABELA_PRECO_PARCELA \n")
		.append(" and TPP.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
		.append(" and TP.ID_MOEDA = MO.ID_MOEDA \n")
		.append(" and TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
		.append(" and PP.TP_PARCELA_PRECO = 'T' and PP.TP_PRECIFICACAO = 'T' \n")
		.append(" and TP.ID_TABELA_PRECO = ?) TV, \n")

		.append("(select 'ESP' as TIPO_TAXA, 2 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
		.append("   from PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARCELA_PRECO PP \n")
		.append("  where P.ID_PARAMETRO_CLIENTE = ? \n")
		.append("    and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE \n")
		.append("    and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
		.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
		.append(" and  ( P.ID_MUNICIPIO_ORIGEM is not null \n")
		.append(" or  P.ID_MUNICIPIO_DESTINO is not null \n")
		.append(" or  P.ID_FILIAL_ORIGEM is not null \n")
		.append(" or  P.ID_FILIAL_DESTINO is not null \n")
		.append(" or  P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is not null \n")
		.append(" or  P.ID_TIPO_LOC_MUNICIPIO_DESTINO is not null \n")
		.append(" ) \n")
		.append(" union  \n")
		.append(" select 'UF' as TIPO_TAXA, 1 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
		.append(" from  PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARAMETRO_CLIENTE P1, PARCELA_PRECO PP \n")
		.append(" where P1.ID_PARAMETRO_CLIENTE = ? \n")
		.append(" and P.ID_MUNICIPIO_ORIGEM is null \n") 
		.append(" and P.ID_MUNICIPIO_DESTINO is null \n")
		.append(" and P.ID_FILIAL_ORIGEM is null \n")
		.append(" and P.ID_FILIAL_DESTINO is null \n")
		.append(" and P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is null \n")
		.append(" and P.ID_TIPO_LOC_MUNICIPIO_DESTINO is null \n")
		.append(" and P.ID_UF_DESTINO = P1.ID_UF_DESTINO \n")
		.append(" and P.ID_UF_ORIGEM = P1.ID_UF_ORIGEM \n")
		.append(" and P.ID_TABELA_DIVISAO_CLIENTE = P1.ID_TABELA_DIVISAO_CLIENTE \n")
		.append(" and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE  \n")
		.append(" and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
		.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
		.append(" union  \n")
		.append(" select 'PAIS' as TIPO_TAXA, 0 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
		.append(" from  PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARAMETRO_CLIENTE P1, PARCELA_PRECO PP \n")
		.append(" where P1.ID_PARAMETRO_CLIENTE = ? \n")
		.append(" and P.ID_MUNICIPIO_ORIGEM is null  \n")
		.append(" and P.ID_MUNICIPIO_DESTINO is null  \n")
		.append(" and P.ID_FILIAL_ORIGEM is null \n")
		.append(" and P.ID_FILIAL_DESTINO is null \n")
		.append(" and P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is null \n")
		.append(" and P.ID_TIPO_LOC_MUNICIPIO_DESTINO is null \n")
		.append(" and P.ID_UF_DESTINO is null \n")
		.append(" and P.ID_UF_ORIGEM  is null \n")
		.append(" and P.ID_TABELA_DIVISAO_CLIENTE = P1.ID_TABELA_DIVISAO_CLIENTE \n")
		.append(" and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE \n")
		.append(" and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
		.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
		.append(" ) TVC  \n")
		.append(" where TV.ID_PARCELA_PRECO = TVC.ID_PARCELA_PRECO (+)  \n")
		.append(" order by ORDEM_TAXA \n");

		return sql.toString().replaceAll("\\[vlx\\]", "Coleta");
	}

	private static String montaSQLEntregaRota() {
		StringBuilder sql = new StringBuilder();
		sql.append("select \n")
				.append("TV.ID_PARCELA_PRECO, TV.ID_TABELA_PRECO, TV.DS_SIMBOLO, TV.CD_PARCELA_PRECO, TVC.TIPO_TAXA, TVC.ORDEM_TAXA, \n")
				.append("decode(TP_TAXA_INDICADOR, null, TV.VL_TAXA, 'T', TV.VL_TAXA, 'V', decode(TVC.VL_TAXA, 0, TV.VL_TAXA, TVC.VL_TAXA), \n")
				.append(" 'A', (TV.VL_TAXA+(TV.VL_TAXA*(TVC.VL_TAXA/100))), 'D', (TV.VL_TAXA-(TV.VL_TAXA*(TVC.VL_TAXA/100)))) as VL_TAXA, \n")
		
				.append("decode(TP_TAXA_INDICADOR, null, TV.PS_TAXADO, 'T', TV.PS_TAXADO, 'V', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO), \n")
				.append(" 'A', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO), 'D', decode(TVC.PS_MINIMO, 0, TV.PS_TAXADO, TVC.PS_MINIMO)) as PS_TAXADO, \n")
		
				.append("decode(TP_TAXA_INDICADOR, null, TV.VL_EXCEDENTE, 'T', TV.VL_EXCEDENTE, 'V', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE), \n")
				.append(" 'A', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE), 'D', decode(TVC.VL_EXCEDENTE, 0, TV.VL_EXCEDENTE, TVC.VL_EXCEDENTE)) as VL_EXCEDENTE \n")
		
				.append(" from  \n")
		
				.append("(select PP.ID_PARCELA_PRECO, MO.DS_SIMBOLO, PP.CD_PARCELA_PRECO, TP.ID_TABELA_PRECO, VT.*  \n")
				.append(" from VALOR_TAXA VT, \n")
				.append("TABELA_PRECO_PARCELA TPP, \n")
				.append("TABELA_PRECO TP, \n")
				.append("MOEDA MO, \n")
				.append("PARCELA_PRECO PP  \n")
				.append(" where  \n")
				.append(" VT.ID_VALOR_TAXA = TPP.ID_TABELA_PRECO_PARCELA \n")
				.append(" and TPP.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
				.append(" and TP.ID_MOEDA = MO.ID_MOEDA \n")
				.append(" and TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
				.append(" and PP.TP_PARCELA_PRECO = 'T' and PP.TP_PRECIFICACAO = 'T' \n")
				.append(" and TP.ID_TABELA_PRECO = ?) TV, \n")

				.append("(select 'ESP' as TIPO_TAXA, 2 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
				.append("   from PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARCELA_PRECO PP \n")
				.append("  where P.ID_PARAMETRO_CLIENTE = ? \n")
				.append("    and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE \n")
				.append("    and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
				.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
				.append(" and  ( P.ID_MUNICIPIO_ORIGEM is not null \n")
				.append(" or  P.ID_MUNICIPIO_DESTINO is not null \n")
				.append(" or  P.ID_FILIAL_ORIGEM is not null \n")
				.append(" or  P.ID_FILIAL_DESTINO is not null \n")
				.append(" or  P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is not null \n")
				.append(" or  P.ID_TIPO_LOC_MUNICIPIO_DESTINO is not null \n")
				.append(" ) \n")
				.append(" union  \n")
				.append(" select 'UF' as TIPO_TAXA, 1 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
				.append(" from  PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARAMETRO_CLIENTE P1, PARCELA_PRECO PP \n")
				.append(" where P1.ID_PARAMETRO_CLIENTE = ? \n")
				.append(" and P.ID_MUNICIPIO_ORIGEM is null \n")
				.append(" and P.ID_MUNICIPIO_DESTINO is null \n")
				.append(" and P.ID_FILIAL_ORIGEM is null \n")
				.append(" and P.ID_FILIAL_DESTINO is null \n")
				.append(" and P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is null \n")
				.append(" and P.ID_TIPO_LOC_MUNICIPIO_DESTINO is null \n")
				.append(" and P.ID_UF_DESTINO = P1.ID_UF_DESTINO \n")
				.append(" and P.ID_UF_ORIGEM = P1.ID_UF_ORIGEM \n")
				.append(" and P.ID_TABELA_DIVISAO_CLIENTE = P1.ID_TABELA_DIVISAO_CLIENTE \n")
				.append(" and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE  \n")
				.append(" and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
				.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
				.append(" union  \n")
				.append(" select 'PAIS' as TIPO_TAXA, 0 as ORDEM_TAXA, p.id_PARAMETRO_CLIENTE, PP.CD_PARCELA_PRECO, TC.* \n")
				.append(" from  PARAMETRO_CLIENTE P, TAXA_CLIENTE TC, PARAMETRO_CLIENTE P1, PARCELA_PRECO PP \n")
				.append(" where P1.ID_PARAMETRO_CLIENTE = ? \n")
				.append(" and P.ID_MUNICIPIO_ORIGEM is null  \n")
				.append(" and P.ID_MUNICIPIO_DESTINO is null  \n")
				.append(" and P.ID_FILIAL_ORIGEM is null \n")
				.append(" and P.ID_FILIAL_DESTINO is null \n")
				.append(" and P.ID_TIPO_LOC_MUNICIPIO_ORIGEM is null \n")
				.append(" and P.ID_TIPO_LOC_MUNICIPIO_DESTINO is null \n")
				.append(" and P.ID_UF_DESTINO is null \n")
				.append(" and P.ID_UF_ORIGEM  is null \n")
				.append(" and P.ID_TABELA_DIVISAO_CLIENTE = P1.ID_TABELA_DIVISAO_CLIENTE \n")
				.append(" and P.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE \n")
				.append(" and TC.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
				.append(" and PP.CD_PARCELA_PRECO in ('ID[vlx]UrbanaConvencional','ID[vlx]InteriorConvencional','ID[vlx]InteriorEmergencia','ID[vlx]UrbanaEmergencia') \n")
				.append(" ) TVC  \n")
				.append(" where TV.ID_PARCELA_PRECO = TVC.ID_PARCELA_PRECO (+)  \n")
				.append(" order by ORDEM_TAXA \n");

		return sql.toString().replaceAll("\\[vlx\\]", "Entrega");
	}
		
	private static String montaSQLProdEspc() {
		/* PARA PEGAR OS PROFUTOS ESPECIFICOS. */
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT 'TE ' || LPAD(TO_CHAR(NR_TARIFA_ESPECIFICA),3,0) || ' = ' ||")
				.append(PropertyVarcharI18nProjection.createProjection(
						"DS_PRODUTO_ESPECIFICO_I", "PROD_ESPEC"))
		.append(" FROM PRODUTO_ESPECIFICO ORDER BY ('TE ' || LPAD(TO_CHAR(NR_TARIFA_ESPECIFICA),3,0)) ASC ");
		return sql.toString();
	}
		
	private static String montaSQLAeroCids() {
		/* Para pegar o aeroporto por cidades. */
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT CD_CIDADE ,SG_AEROPORTO AS CID_AERO FROM AEROPORTO AE1 ")
		.append("where exists ")
		.append(" (select CD_CIDADE, COUNT(CD_CIDADE) from AEROPORTO AE2 where AE1.CD_CIDADE = AE2.CD_CIDADE")
		.append(" group by CD_CIDADE having COUNT(CD_CIDADE)>1) ")
		.append(" ORDER BY CD_CIDADE ASC");
		return sql.toString();
	}
		
	private static String montaSQLServicosAd() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
		.append("      PP.CD_PARCELA_PRECO, ")
		.append("      PP.TP_INDICADOR_CALCULO, ")
		.append("      decode(PP.TP_PRECIFICACAO,'S',VSA.VL_SERVICO, FPERC.VL_FIXO) AS VL_SERVICO, ")
		.append("      VSA.VL_MINIMO")
		.append(" FROM TABELA_PRECO TP, ")
		.append("      TIPO_TABELA_PRECO TTP, ")
		.append("      SUBTIPO_TABELA_PRECO STP, ")
		.append("      TABELA_PRECO_PARCELA TPP, ")
		.append("      PARCELA_PRECO PP, ")
		.append("      VALOR_SERVICO_ADICIONAL VSA,")
		.append("      (select fp.ID_TABELA_PRECO_PARCELA, vfp.* from Faixa_Progressiva FP, valor_faixa_progressiva vfp where FP.tp_indicador_calculo = 'PC' and FP.id_faixa_progressiva = vfp.id_faixa_progressiva) FPERC ")
		.append(" WHERE tp.ID_TABELA_PRECO = ?")
		.append("      AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO ")
		.append("      AND TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO ")
		.append("      AND TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO")
		.append("      AND TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
		.append("      AND TPP.ID_TABELA_PRECO_PARCELA = VSA.ID_VALOR_SERVICO_ADICIONAL (+)")
		.append("      AND PP.TP_PARCELA_PRECO = 'S'")
		.append("      AND PP.TP_PRECIFICACAO in('S','M')")
		.append("      AND TPP.ID_TABELA_PRECO_PARCELA = FPERC.ID_TABELA_PRECO_PARCELA (+)")
				.append(" ORDER BY ")
				.append(PropertyVarcharI18nProjection
						.createProjection("PP.NM_PARCELA_PRECO_I"));
		return sql.toString();
	}

	private static String montaSqlGeneralidadesRotaDificuldadeEntrega() {
		StringBuffer sql = new StringBuffer()
		.append(prepareSqlGeneralidadesRotaDificuldadeEntrega())
		.append(" WHERE o.id_parcela_preco = p.id_p_cliente(+) ")
		.append(" UNION ")
		.append(prepareSqlGeneralidadesRotaDificuldadeEntrega())
		.append(" WHERE o.id_parcela_preco(+) = p.id_p_cliente ");
	return sql.toString();
	}
	
	private static String prepareSqlGeneralidadesRotaDificuldadeEntrega() {
		StringBuilder sql = new StringBuilder()
		.append("SELECT * FROM ")
		.append("	(SELECT P.ID_PARCELA_PRECO,")
		.append("        P.CD_PARCELA_PRECO,")
		.append("		 P.TP_PARCELA_PRECO, ")
		.append("        P.TP_INDICADOR_CALCULO, ")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO"))
				.append(" ,")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
		.append("		 P.BL_EMBUTE_PARCELA AS BL_EMBUTE_PARCELA, ")
		.append("        T.ID_TABELA_PRECO,")
		.append("		 T.TP_CALCULO_PEDAGIO,")
		.append("		 T.PC_REAJUSTE,")
		.append("        STP.TP_SUBTIPO_TABELA_PRECO,")
		.append("        T.OB_TABELA_PRECO,")
		.append("        G.*")
		.append(" FROM TABELA_PRECO T, ")
		.append("	   TABELA_PRECO_PARCELA TT,")
		.append("      PARCELA_PRECO P,")
		.append("      GENERALIDADE g,")
		.append("      SUBTIPO_TABELA_PRECO STP")
		.append(" WHERE T.ID_TABELA_PRECO = TT.ID_TABELA_PRECO ")
		.append(" 	AND T.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO")
		.append("   AND TT.ID_PARCElA_PRECO = P.ID_PARCELA_PRECO")
		.append("   AND TT.ID_TABELA_PRECO_PARCELA = G.ID_generalidade")
		.append("   AND T.ID_TABELA_PRECO = ?")
		.append("   AND P.CD_PARCELA_PRECO IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
		.append(" ORDER BY NM_PARCELA_PRECO)O,")
		.append("   (SELECT PP.ID_PARCELA_PRECO as ID_P_CLIENTE, PP.CD_PARCELA_PRECO as CD_PARCELA, ")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.DS_PARCELA_PRECO_I", "DS_PARCELA"))
				.append(" ,")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA"))
				.append(" ,")
		.append(" 		G.VL_GENERALIDADE as VL_GENERALIDADE_CLIENTE,")
		.append("      	G.TP_INDICADOR, PP.TP_INDICADOR_CALCULO AS TP_CALCULO, ")		
		.append("      	PP.BL_EMBUTE_PARCELA AS BL_EMBUTE, P.* ")
		.append("    FROM PARAMETRO_CLIENTE P, ")
		.append("    	  GENERALIDADE_CLIENTE G, ")
		.append("    	  PARCELA_PRECO PP")
		.append("    WHERE P.ID_PARAMETRO_CLIENTE = ?")
		.append("      AND PP.CD_PARCELA_PRECO IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
		.append("      AND P.ID_PARAMETRO_CLIENTE = g.ID_PARAMETRO_CLIENTE")
		.append("      AND G.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
		.append(" 	 ORDER BY NM_PARCELA)P ");
	return sql.toString();
	}

	private static String montaSqlGeneralidadesDificuldadeEntrega() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ")
		.append("     (SELECT P.ID_PARCELA_PRECO,")
		.append("            P.CD_PARCELA_PRECO,")
		.append("	         P.TP_PARCELA_PRECO, ")
		.append("	         P.TP_INDICADOR_CALCULO AS TP_INDICADOR_CALCULO, ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO"))
				.append(" ,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
		.append("	         P.BL_EMBUTE_PARCELA,")
		.append("            T.ID_TABELA_PRECO,")
		.append("			 T.TP_CALCULO_PEDAGIO,")
		.append("		 	 T.PC_REAJUSTE,")
		.append("            STP.TP_SUBTIPO_TABELA_PRECO,")
		.append("    		 T.OB_TABELA_PRECO,")
		.append("            G.*")
		.append("       FROM TABELA_PRECO T,")
		.append("            TABELA_PRECO_PARCELA TT,")
		.append("            PARCELA_PRECO P,")
		.append("            GENERALIDADE g,")
		.append("            SUBTIPO_TABELA_PRECO STP")
		.append("       WHERE T.ID_TABELA_PRECO = TT.ID_TABELA_PRECO ")
		.append("         AND T.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO")
		.append("         AND TT.ID_PARCElA_PRECO = P.ID_PARCELA_PRECO")
		.append("         AND TT.ID_TABELA_PRECO_PARCELA = G.ID_generalidade")
		.append("         AND T.ID_TABELA_PRECO = ? ")
		.append("         AND P.CD_PARCELA_PRECO IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde')) O full outer join")
		.append("     (SELECT P.ID_PARCELA_PRECO as ID_P_CLIENTE, P.CD_PARCELA_PRECO as CD_PARCELA,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA"))
				.append(" ,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA"))
				.append(" ,")
		.append(" 			GC.VL_GENERALIDADE as VL_GENERALIDADE_CLIENTE,")
		.append(" 			GC.VL_MINIMO as VL_MINIMO_GEN,")
		.append(" 			GC.TP_INDICADOR_MINIMO as TP_INDICADOR_MINIMO_GEN,")
		.append("           GC.TP_INDICADOR, PC.*,")
		.append("		    P.TP_INDICADOR_CALCULO AS TP_CALCULO, ")
		.append("		    P.BL_EMBUTE_PARCELA AS BL_EMBUTE ")
		.append("        FROM GENERALIDADE_CLIENTE GC, ")
		.append("             PARCELA_PRECO P,")
		.append("             PARAMETRO_CLIENTE PC ")
		.append("       WHERE GC.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append("         AND PC.ID_PARAMETRO_CLIENTE = GC.ID_PARAMETRO_CLIENTE")
		.append("         AND PC.ID_PARAMETRO_CLIENTE = ? ")
		.append(" 		  AND P.CD_PARCELA_PRECO IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
				.append(")P ").append(" ON ")
		.append("     O.ID_PARCELA_PRECO = P.ID_P_CLIENTE ")
		.append("ORDER BY O.NM_PARCELA_PRECO, P.NM_PARCELA ");
		return sql.toString();
	}

	public static Map getAllDefaultChoiceCrosstabReportParameters(
			Map parameters, String nomeTabela, Boolean isTabelaNova,
			int nroCrosstabColumns, Set crosstab, int[] subReports,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getReportParameters(parameters, nomeTabela, isTabelaNova,
				nroCrosstabColumns, crosstab, subReports, configuracoesFacade,
				jdbcTemplate);
	}

	private static Map getReportParameters(Map parameters, String nomeTabela,
			Boolean isTabelaNova, int nroCrosstabColumns, Set crosstab,
			int[] subReports, ConfiguracoesFacade configuracoesFacade,
			JdbcTemplate jdbcTemplate) {
		Long idCliente = MapUtils.getLong(parameters, "idCliente");
		Long idParametro = null;

		if(idCliente == null){
			 idCliente = MapUtils.getLong(parameters, "remetente.idCliente");
		}
		parameters.put("idCliente", idCliente);

		// trecho que pega o parametro do cliente dependendo da chave que pode
		// vir
		if(parameters.containsKey("idParametro")){
			idParametro = MapUtils.getLong(parameters, "idParametro");
		}else if(parameters.containsKey("idParametroCliente")){
			idParametro = MapUtils.getLong(parameters, "idParametroCliente");
		}else if(parameters.containsKey("parametroCliente")){
			idParametro = MapUtils.getLong(parameters, "parametroCliente");
		}

		Long idDivisao = MapUtils.getLong(parameters, "idDivisao");
		Long idTabelaDivisao = null;
		if(parameters.containsKey("idTabelaDivisaoCliente")){
			idTabelaDivisao = MapUtils.getLong(parameters,
					"idTabelaDivisaoCliente");
		}else if(parameters.containsKey("idTabelaDivisao")){
			idTabelaDivisao = MapUtils.getLong(parameters, "idTabelaDivisao");
		}else if(parameters.containsKey("divisao")){
			idTabelaDivisao = MapUtils.getLong(parameters, "divisao");
		}
		
		Long idTabelaPreco = MapUtils.getLong(parameters, "idTabelaPreco");
		Long idSimulacao = MapUtils.getLong(parameters, "idSimulacao");
		
		Map parametros = new HashMap();
		// Busca as informações do header em formato JRMapCollectionDataSource
		Map map = (Map) montaHeader(parameters, jdbcTemplate,
				RETORNO_DATASOURCE);
		
		if(idSimulacao != null){
			parametros = montaSubRelatoriosProposta(idSimulacao, idCliente,
					idDivisao, idParametro, idTabelaPreco, isTabelaNova,
					parametros, subReports, configuracoesFacade, jdbcTemplate);
		}else if(idParametro != null){
			parametros = montaSubRelatoriosOfChoice(idCliente, idDivisao,
					idParametro, idTabelaPreco, isTabelaNova, parametros,
					subReports, configuracoesFacade, jdbcTemplate);
		}else{
			parametros = montaSubRelatoriosOfChoice(idCliente, idDivisao,
					nomeTabela, nroCrosstabColumns, idTabelaPreco,
					isTabelaNova, parametros, subReports, configuracoesFacade,
					jdbcTemplate);
		}

		parametros.put("HEADER", map);
		parametros.put("DADOS", map);
		montaLogoMercurio(parametros, jdbcTemplate);

		String servico = null;
		if(idTabelaDivisao != null)
			servico = getTipoServico(idTabelaDivisao, jdbcTemplate);
		else{
			servico = getTipoServicoParametro(idParametro, jdbcTemplate);
		}
		parametros.put("SERVICO",servico);
		if(crosstab != null){
			parametros = montaCrosstabParameters("PCOLUMN", crosstab,
					parametros);
			parametros.put(ReportServiceSupport.CT_NUMBER_OF_COLS,
					new Integer[] { Integer.valueOf(crosstab.size()) });
		}
		return parametros;
	}
	
	/**
	 * 
	 * Metodo que recebe um map com os filtros da tela retorna os dados do
	 * Header do cliente, identificado pelo valor do atributo idCliente, de
	 * acordo com o tipo de retorno especificado. O retorno pode ser um List ou
	 * um JrMapCollectionDataSource, caso nenhum retorno seja especificado, o
	 * metodo retorna um MAP
	 * 
	 * @author Rodrigo F. Dias - LMS - GT5
	 * @version 1.0
	 * 
	 * @param parameters
	 * @param jdbcTemplate
	 * @param retorno
	 * @return
	 */
	public static Object montaHeader(Map parameters, JdbcTemplate jdbcTemplate,
			int retorno) {
		
		Long idCliente = MapUtils.getLong(parameters, "idCliente");
		Long idContato = MapUtils.getLong(parameters, "idContato");
		
		
		return montaHeader(idCliente, idContato, jdbcTemplate);
	}	
	
	/**
	 * 
	 * @param map
	 * @param jdbcTemplate
	 */
	public static void montaLogoMercurio(Map map,JdbcTemplate jdbcTemplate){
		map.put("cidade", StringUtils.left(getCidadeFilial(jdbcTemplate),50));
		map.put("data", getDataPorExtenso());
	}

	/**
	 * Metodo que recebe o id de um cliente e retorna um map contendo os dados
	 * do header dos relatorios de vendas.
	 * 
	 * @param idCliente
	 * @param jdbcTemplate
	 * @return
	 */
	public static Map montaHeader(Long idCliente, Long idContato,
			JdbcTemplate jdbcTemplate) {
		List dados = jdbcTemplate.queryForList(montaSqlHeader(idContato),
				new Long[] { idCliente });

		Map parametersHeader = dados.isEmpty() ? new HashMap() : (Map) dados
				.get(0);

		//Formata a indentificação
		String tpIndentificacao = MapUtils.getString(parametersHeader,
				"TP_IDENTIFICACAO");
		String nrIdentificacao = MapUtils.getString(parametersHeader,
				"NR_IDENTIFICACAO");
		String nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(
				tpIndentificacao, nrIdentificacao);
		boolean controleIdentificacao = true;
		if (StringUtils.isBlank(tpIndentificacao)) {
			tpIndentificacao = "";
			controleIdentificacao = false;
		}
		if (StringUtils.isBlank(nrIdentificacaoFormatado)) {
			nrIdentificacaoFormatado = "";
			controleIdentificacao = false;
		}
		String identificacao = tpIndentificacao
				+ (controleIdentificacao ? ": " : "")
				+ nrIdentificacaoFormatado;
		parametersHeader.put("IDENTIFICACAO", identificacao);

		//Formata o cep
		String cep = MapUtils.getString(parametersHeader,"CEP");
		String cepFormatado = FormatUtils.formatCep("BRA",cep);
		String municipio = MapUtils.getString(parametersHeader,"MUNICIPIO");
		String uf = MapUtils.getString(parametersHeader,"UNIDADEFEDERATIVA");
		parametersHeader.put("CEP", cepFormatado
				+ (municipio != null ? " - " + StringUtils.left(municipio, 50)
						: "") + (uf != null ? " - " + uf : ""));

		//Formata a increição Estadual
		String blIndicador = MapUtils.getString(parametersHeader,"INDICADOR");
		if("S".equals(blIndicador)){
			//TODO ???
		}

		String numero = MapUtils.getString(parametersHeader,"NUMERO");
		if(StringUtils.isNotBlank(numero)) {
			if(StringUtils.isNumeric(numero)) {
				//IE é a mesma máscara que CPF. Ex. do prot: 234.574.532-12
				numero = "IE: "+FormatUtils.formatCPF(numero); 
				parametersHeader.put("NUMERO", numero);
			}
		}

		// Busca e formata o telefone e o fax
		List fone = jdbcTemplate.queryForList(montaSqlTelefone(),
				new Long[] { idCliente });
		List faxLista = jdbcTemplate.queryForList(montaSqlFax(),
				new Long[] { idCliente });

		Map telefone = fone.isEmpty() ? new HashMap() : (Map)fone.get(0);
		String numeroFone = MapUtils.getString(telefone,"TELEFONE", "");
		String foneFormatado = FormatUtils.formatTelefone(numeroFone);
		if(StringUtils.isNotBlank(foneFormatado)){
			foneFormatado =  "Telefone: +55 "+foneFormatado;
		}
		parametersHeader.put("TELEFONE", foneFormatado);

		Map fax = faxLista.isEmpty() ? new HashMap() : (Map)faxLista.get(0);
		String numeroFax = MapUtils.getString(fax,"FAX", "");
		String faxFormatado = FormatUtils.formatTelefone(numeroFax);
		if(StringUtils.isNotBlank(faxFormatado)){
			faxFormatado = "Fax: +55 "+faxFormatado;
		}
		parametersHeader.put("Fax", faxFormatado);

		parametersHeader.put("data", getCidadeFilial(jdbcTemplate) + ", "
				+ getDataPorExtenso());
		return parametersHeader;	
	}

	/**
	 * 
	 * @param jdbcTemplate
	 * @return
	 */
	public static String getCidadeFilial(JdbcTemplate jdbcTemplate){
		List cidades = jdbcTemplate.queryForList(montaSqlCidade(),
				new Long[] { SessionUtils.getFilialSessao().getIdFilial() });
		String cidade = "";
		if(!cidades.isEmpty()){
			cidade = ((Map)cidades.get(0)).get("nm_municipio").toString();
			cidade = WordUtils.capitalizeFully(cidade);
			
		}
		return cidade;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDataPorExtenso(){
		String dataExtenso = JTDateTimeUtils.getDataFormatadaPorExtenso();
		String[] data = dataExtenso.split(" ");
		dataExtenso = data[1] + " " + data[2] + " " + data[3] + " " + data[4]
				+ " " + data[5];
		return dataExtenso;
	}

	/**
	 * Recebe o id da tabela de preco e retorna o simbolo da moeda relacionada a
	 * ela
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return
	 */
	public static String getMoeda(Long idTabelaPreco, JdbcTemplate jdbcTemplate){
		StringBuffer sql = new StringBuffer();
		sql.append("select m.ds_simbolo from moeda m, tabela_preco tp ");
		sql.append("where tp.id_tabela_preco = ? and tp.id_moeda = m.id_moeda ");
		if(idTabelaPreco != null){
			List lista = jdbcTemplate.queryForList(sql.toString(),
					new Long[] { idTabelaPreco });
			if (!lista.isEmpty()) {
				Map map = (Map) lista.get(0);
				return MapUtils.getString(map,"ds_simbolo");
			}
		}
		return null;
	}

	/**
	 * Metodo que recebe o id do servico e retorna o tipo de
	 * serviço(DS_TIPO_SERVICO).
	 * 
	 * @param idServico
	 * @param jdbcTemplate
	 * @return
	 */
	public static Map getTipoServicoByServico(Long idServico,
			JdbcTemplate jdbcTemplate) {
		
		if (idServico == null)
			return null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"DS_TIPO_SERVICO_I", "DS_TIPO_SERVICO"))
		.append(" from tipo_servico ts, servico s ")
		.append(" where ts.id_tipo_servico = s.id_tipo_servico ")
		.append(" and s.id_servico = ?");
		
		List servicos = jdbcTemplate.queryForList(sql.toString(),
				new Long[] { idServico });
		if(!servicos.isEmpty())
			return (Map)servicos.get(0);
		else
			return null;
	}
	
	/**
	 * Metodo que recebe o id da tabela de divisao do cliente e retorna o tipo
	 * de serviço(DS_TIPO_SERVICO) pertencente ao serviço dela.
	 * 
	 * @author Alexandre Poletto - LMS - GT5
	 * @version 1.0
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param jdbcTemplate
	 * @return 
	 */
	public static String getTipoServico(Long idTabelaDivisaoCliente,
			JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"tpser.DS_TIPO_SERVICO_I", "DS_TIPO_SERVICO"))
				.append(" FROM ").append("	TABELA_DIVISAO_CLIENTE tdc,")
				.append("	SERVICO ser,").append("	TIPO_SERVICO tpser")
				.append(" WHERE").append("	tdc.id_tabela_divisao_cliente = ?")
		.append("	and tdc.id_Servico = ser.id_servico")
		.append("	and ser.id_tipo_servico = tpser.id_tipo_servico");
		if(idTabelaDivisaoCliente != null){
			List lista = jdbcTemplate.queryForList(sql.toString(),
					new Long[] { idTabelaDivisaoCliente });
			if (!lista.isEmpty()) {
				Map map = (Map) lista.get(0);
				return MapUtils.getString(map,"DS_TIPO_SERVICO");
			}
		}
		return null;
	}

	/**
	 * Metodo que recebe o id do parametro do cliente e retorna o tipo de
	 * serviço(DS_TIPO_SERVICO) pertencente ao serviço dela.
	 * 
	 * @author Alexandre Poletto - LMS - GT5
	 * @version 1.0
	 * 
	 * @param idParametro
	 * @param jdbcTemplate
	 * @return 
	 */
	public static String getTipoServicoParametro(Long idParametro,
			JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"tpser.DS_TIPO_SERVICO_I", "DS_TIPO_SERVICO"))
		.append(" FROM ")
		.append("	TABELA_DIVISAO_CLIENTE tdc,")
		.append("	PARAMETRO_CLIENTE pc,")
		.append("	SERVICO ser,")
		.append("	TIPO_SERVICO tpser")
		.append(" WHERE ")
		.append("	pc.id_parametro_cliente = ?")
		.append("	and pc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente")
		.append("	and tdc.id_Servico = ser.id_servico")
		.append("	and ser.id_tipo_servico = tpser.id_tipo_servico");
		if(idParametro != null){
			List lista = jdbcTemplate.queryForList(sql.toString(),
					new Long[] { idParametro });
			if(!lista.isEmpty()){
				Map map = (Map) lista.get(0);
				return MapUtils.getString(map,"DS_TIPO_SERVICO");
				
			}
		}
		return null;
	}
	
	/**
	 * Metodo que recebe o id da TABELA_PRECO e retorna o tipo de
	 * serviço(DS_TIPO_SERVICO) da tabela.
	 * 
	 * @author Baltazar Schirmer - LMS - GT5
	 * @version 1.0
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return 
	 */
	public static String getTipoServicoTabela(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"tpser.DS_TIPO_SERVICO_I", "DS_TIPO_SERVICO"))
		.append(" FROM ")
		.append("	TIPO_TABELA_PRECO ttp,")
		.append("	TABELA_PRECO tp,")
		.append("	SERVICO ser,")
		.append("	TIPO_SERVICO tpser")
		.append(" WHERE")
		.append("	tp.id_tabela_preco = ?")
		.append("	and tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
		.append("	and ttp.id_Servico = ser.id_servico")
		.append("	and ser.id_tipo_servico = tpser.id_tipo_servico");
		if(idTabelaPreco != null){
			List lista = jdbcTemplate.queryForList(sql.toString(),
					new Long[] { idTabelaPreco });
			Map map = (Map) lista.get(0);
			return MapUtils.getString(map,"DS_TIPO_SERVICO");
		}
		return null;
	}

	public static Map getServicoTabela(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SER.TP_MODAL ")
			.append(" 	   ,SER.TP_ABRANGENCIA ")
			.append(" FROM ")
			.append("	TIPO_TABELA_PRECO ttp,")
			.append("	TABELA_PRECO tp,")
			.append("	SERVICO ser")
			.append(" WHERE")
			.append("	tp.id_tabela_preco = ?")
			.append("	and tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco")
			.append("	and ttp.id_Servico = ser.id_servico");
		if(idTabelaPreco != null){
			List lista = jdbcTemplate.queryForList(sql.toString(),
					new Long[] { idTabelaPreco });
			return (Map) lista.get(0);
		}
		return null;
	}
	
	/**
	 * Gera subRelatorio de generalidades do cliente, baseado na sua rota
	 * 
	 * @param idParametro
	 * @param idTabelaPreco
	 * @param dsSimbolo
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @return
	 */	
	public static List getSubRelGeneralidadesRota(Long idParametro,
			Long idTabelaPreco, Boolean isTabelaNova, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getSubRelGeneralidades(idParametro, idTabelaPreco, isTabelaNova,
				dsSimbolo, true, configuracoesFacade, jdbcTemplate);
	}

	/**
	 * Gera subRelatorio de generalidades do cliente
	 * 
	 * @param idParametro
	 * @param idTabelaPreco
	 * @param dsSimbolo
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelGeneralidades(Long idParametro,
			Long idTabelaPreco, Boolean isTabelaNova, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getSubRelGeneralidades(idParametro, idTabelaPreco, isTabelaNova,
				dsSimbolo, false, configuracoesFacade, jdbcTemplate);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List getSubRelGeneralidades(Long idParametro,Long idTabelaPreco, Boolean isTabelaNova, String dsSimbolo,
			boolean subRota, ConfiguracoesFacade configuracoesFacade,JdbcTemplate jdbcTemplate) {
		

		List<Map> generalidades = new ArrayList<Map>();
		
		if(subRota){
			generalidades = jdbcTemplate.queryForList(
					montaSqlGeneralidadesRota(), new Long[] { idTabelaPreco,
							idParametro, idTabelaPreco, idParametro });
		}else{
			generalidades = jdbcTemplate.queryForList(montaSqlGeneralidades(),
					new Long[] { idTabelaPreco, idParametro });
		}

		aplicaGeneralidade(generalidades, isTabelaNova, jdbcTemplate,
				idParametro, configuracoesFacade, dsSimbolo);
			
		String vlMinimoReentrega = null;
		for (Map data : generalidades) {		
			StringBuilder strValorGeneralidade = new StringBuilder(
					MapUtils.getString(data, "VL_GENERALIDADE", "0"));
			String cdParcelaPreco = MapUtils
					.getString(data, "CD_PARCELA_PRECO");
		
		if("IDMinimoReentrega".equals(cdParcelaPreco)){
				String strNomeGeneralidade = MapUtils.getString(data,
						"DS_PARCELA_PRECO", "DS_PARCELA");
				String idParametroCliente = MapUtils.getString(data,
						"ID_PARAMETRO_CLIENTE");
			//Indicadores
			String tpIndicador = MapUtils.getString(data,"TP_INDICADOR");
			//se nao achar na priemira tabela, pega a da segunda
				String tpIndicadorCalculo = MapUtils.getString(data,
						"TP_INDICADOR_CALCULO", "TP_CALCULO");
				String blEmbuteParcela = MapUtils.getString(data,
						"BL_EMBUTE_PARCELA", "BL_EMBUTE");
				String tpSubtipoTabelaPreco = MapUtils.getString(data,
						"TP_SUBTIPO_TABELA_PRECO");
			//valores em String
			String strVlMinimo = MapUtils.getString(data,"VL_MINIMO","0") ;
				BigDecimal vlGeneralidadeCliente = MapUtilsPlus.getBigDecimal(
						data, "VL_GENERALIDADE_CLIENTE", BigDecimal.ZERO);
				BigDecimal pcReajuste = MapUtilsPlus.getBigDecimal(data,
						"PC_REAJUSTE", BigDecimal.ZERO);
			//valores em BigDecimal
				BigDecimal vlGeneralidade = new BigDecimal(strValorGeneralidade.toString());
			BigDecimal vlOriginal = vlGeneralidade; 
			BigDecimal vlMinimo = new BigDecimal(strVlMinimo);
			BigDecimal vlMinGeneralidade = new BigDecimal(strVlMinimo);
		
				BigDecimal vlMinGeneralidadeCliente = new BigDecimal(
						MapUtils.getString(data, "VL_MINIMO_GEN", "0"));
				String tpIndicadorMinimoGenCliente = MapUtils.getString(data,
						"TP_INDICADOR_MINIMO_GEN");
			
			/** Regra GERAL para qualquer Generalidade */
				if (idParametroCliente != null && !TABELA.equals(tpIndicador)
						&& !ConstantesExpedicao.CD_GRIS.equals(cdParcelaPreco)
						&& !ConstantesExpedicao.TIPOS_PEDAGIO.contains(cdParcelaPreco)) {
					
				/** Verifica se deve Reajusta Generalidade do Cliente */
					if (Boolean.TRUE.equals(isTabelaNova) && BigDecimalUtils.hasValue(pcReajuste)
							&& (TP_VALOR.equals(tpIndicador) || TP_QUILO.equals(tpIndicador))) {
						vlGeneralidadeCliente = BigDecimalUtils.acrescimo(
								vlGeneralidadeCliente, pcReajuste);
				}

					vlGeneralidade = calculaPercentagem(tpIndicador,vlGeneralidadeCliente, vlGeneralidade);
					strValorGeneralidade = new StringBuilder(formatDuasCasasDecimais(vlGeneralidade));
		}
			
			vlMinimoReentrega = strValorGeneralidade.toString();
			break;
		}
		
		}
		
		String reentrega = null;
		String devolucao = null;
		//Busca a entrega e a devolução genericas ou do parametro do cliente
		List<Map> generalidadesSub = jdbcTemplate.queryForList(montaSqlGeneralidadesSub(idParametro));
		
		for (Map data : generalidadesSub) {
			reentrega = MapUtils.getString(data, "PC_COBRANCA_REENTREGA",MapUtils.getString(data, REENTREGA));
			devolucao = MapUtils.getString(data, "PC_COBRANCA_DEVOLUCOES",MapUtils.getString(data, DEVOLUCAO));
		}
		String strReentrega = FormatUtils.formatDecimal(FORMATO_2_CASAS,new BigDecimal(reentrega))+ " "	+ configuracoesFacade.getMensagem("percentualfreteOriginal");
		String strDevolucao = FormatUtils.formatDecimal(FORMATO_2_CASAS,new BigDecimal(devolucao))+ " "	+ configuracoesFacade.getMensagem("percentualfreteOriginal");
		
		if (vlMinimoReentrega != null) {
			strReentrega += " "	+ configuracoesFacade.getMensagem("relOuMinimo") + " " + dsSimbolo + " " + vlMinimoReentrega;
		}
		
		Map data = new HashMap();
		data.put(REENTREGA, strReentrega);
		data.put(DEVOLUCAO, strDevolucao);

		// Verifica se tem taxa combustivel para colocar observacao na ultima
		// linha de generalidades
		List taxaCombustivel = jdbcTemplate.queryForList(
				montaSQLTaxaCombustivel(), new Long[] { idTabelaPreco });
		if(taxaCombustivel!=null && !taxaCombustivel.isEmpty()){
			data.put("TAXACOMBUSTIVEL",
					configuracoesFacade.getMensagem("videTabTaxaCombustivel"));
		}
		generalidades.add(data);
			
		return generalidades;
	}

	
	@SuppressWarnings("rawtypes")
	public static void aplicaGeneralidade(List<Map> generalidades,boolean isTabelaNova, JdbcTemplate jdbcTemplate, Long idParametro,ConfiguracoesFacade configuracoesFacade, String dsSimbolo) {
	
		boolean foundPedagio = false;
		
		for (Map data : generalidades) {
			StringBuilder descricao = new StringBuilder(MapUtils.getString(data,"DS_PARCELA_PRECO"));
			StringBuilder valor = new StringBuilder();
			StringBuilder minimoDescricao = new StringBuilder();
			StringBuilder minimoValor = new StringBuilder();

			BigDecimal vlGeneralidade = new BigDecimal(MapUtils.getString(data,"VL_GENERALIDADE"));
			BigDecimal vlOriginal = vlGeneralidade; 
			String vlGeneralidadeFormatado = formatDuasCasasDecimais(vlGeneralidade);

			String idParametroCliente = MapUtils.getString(data,"ID_PARAMETRO_CLIENTE");		

			BigDecimal vlMinimo = MapUtilsPlus.getBigDecimal(data, "VL_MINIMO");
			BigDecimal vlMinGeneralidade = new BigDecimal(vlMinimo != null ? vlMinimo.toString() : BigDecimal.ZERO.toString());
			BigDecimal vlMinGeneralidadeCliente = new BigDecimal(MapUtils.getString(data, "VL_MINIMO_GEN", "0"));
			BigDecimal vlGeneralidadeCliente = MapUtilsPlus.getBigDecimal(data, "VL_GENERALIDADE_CLIENTE", BigDecimal.ZERO);

			String blEmbuteParcela = MapUtils.getString(data,"BL_EMBUTE_PARCELA", "BL_EMBUTE");

			BigDecimal pcReajuste = MapUtilsPlus.getBigDecimal(data,"PC_REAJUSTE", BigDecimal.ZERO);

			if("IDMinimoReentrega".equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))){
				continue;
					}
					
			if (BigDecimalUtils.hasValue(vlMinGeneralidadeCliente)){
				vlMinGeneralidade = calculaPercentagem(MapUtils.getString(data,"TP_INDICADOR_MINIMO_GEN"), vlMinGeneralidadeCliente,vlMinGeneralidade);
					}


			// Regra GERAL para qualquer Generalidade
			if (idParametroCliente != null
				&& !TABELA.equals(MapUtils.getString(data,"TP_INDICADOR"))
				&& !ConstantesExpedicao.CD_GRIS.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))
				&& !ConstantesExpedicao.TIPOS_PEDAGIO.contains(MapUtils.getString(data, "CD_PARCELA_PRECO"))
				) {

				if (Boolean.TRUE.equals(isTabelaNova) && BigDecimalUtils.hasValue(pcReajuste) 
					&& (TP_VALOR.equals(MapUtils.getString(data,"TP_INDICADOR")) || TP_QUILO.equals(MapUtils.getString(data,"TP_INDICADOR")))
					) {
					vlGeneralidadeCliente = BigDecimalUtils.acrescimo(vlGeneralidadeCliente, pcReajuste);
					}

				vlGeneralidade = calculaPercentagem(MapUtils.getString(data,"TP_INDICADOR"),vlGeneralidadeCliente, vlGeneralidade);
				valor = new StringBuilder(formatDuasCasasDecimais(vlGeneralidade));
					}


			// Mensagem quando psMinimo for o informado
			BigDecimal psMinimo = MapUtilsPlus.getBigDecimal(data, "PS_MINIMO");
			if (StringUtils.isNotBlank(valor.toString()) && BigDecimalUtils.hasValue(psMinimo)) {
				minimoDescricao.append(" acima de ").append(FormatUtils.formatDecimal(FORMATO_3_CASAS, psMinimo)).append(" kg.");
					}

			/** Lista generalidades que SEJAM do tipo PEDÁGIO */
			if(ConstantesExpedicao.TIPOS_PEDAGIO.contains(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				if(!foundPedagio) {
					List<Map> paramPedagio = jdbcTemplate.queryForList(montaSqlParametrosCustom(),new Long[] { idParametro });
					Map dataPedagio = null;
					String tpIndicadorPedagio = null;
					BigDecimal vlPedagio = BigDecimal.ZERO;
					
					if(paramPedagio != null && paramPedagio.get(0) != null) {
						dataPedagio = paramPedagio.get(0);
				
						if (Boolean.TRUE.equals(isTabelaNova)) {
							TabelasClienteUtil.reajustarParametros(dataPedagio,	pcReajuste);
						}

						tpIndicadorPedagio = MapUtils.getString(dataPedagio,"TP_INDICADOR_PEDAGIO");
						vlPedagio = MapUtilsPlus.getBigDecimal(dataPedagio,"VL_PEDAGIO", BigDecimal.ZERO);
					}

					vlGeneralidadeFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS, vlGeneralidade);

					if (dataPedagio == null	|| TABELA.equals(tpIndicadorPedagio) || ACRESCIMO.equals(tpIndicadorPedagio) 
						|| DESCONTO.equals(tpIndicadorPedagio) || VALOR.equals(tpIndicadorPedagio)) {
						
						if(!TABELA.equals(tpIndicadorPedagio)){
							vlGeneralidade = calculaPercentagem(tpIndicadorPedagio, vlPedagio, vlGeneralidade);
							vlGeneralidadeFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade);
						}
						
						if ("F".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
							descricao.append(configuracoesFacade.getMensagem("reaisPorFracao"));
							valor.append(vlGeneralidadeFormatado);
						} else if ("P".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
							descricao.append(configuracoesFacade.getMensagem("reaisPorPostoFracao"));
							valor.append(vlGeneralidadeFormatado);
						} else if ("D".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
							descricao.append(configuracoesFacade.getMensagem("porConhecimento"));
							valor.append(vlGeneralidadeFormatado);
						} else if ("X".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
							valor.append("Se peso entre 0 kg e 100 kg: ")
									.append(dsSimbolo).append(" ")
									.append(vlGeneralidadeFormatado);
							valor.append("\nSe peso entre 101 kg e 500 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("2"))));
							valor.append("\nSe peso entre 501 kg e 1000 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("3"))));
							valor.append("\nAcima de 1000 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.add(vlGeneralidade.multiply(new BigDecimal("3"))))+ " por fração \nde 1000 kg");
						}

						// LMS-11142
						verificaSuspensao(valor,tpIndicadorPedagio, vlPedagio,configuracoesFacade);
					} else {
						if ("F".equals(tpIndicadorPedagio) || BigDecimalUtils.hasValue(vlPedagio)) {
							vlGeneralidade = vlPedagio;
							vlGeneralidadeFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade);
						}
						if(PONTOS_PESO.equals(tpIndicadorPedagio)) {
							if(BigDecimalUtils.hasValue(vlPedagio)
									|| ("F".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))
									|| ("P".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))
									|| ("D".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))
									|| ("X".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))) {
								descricao.append(" ").append(dsSimbolo).append(" ").append(configuracoesFacade.getMensagem("porQuilo"));
								valor.append(vlGeneralidadeFormatado);
							}
						} else if("F".equals(tpIndicadorPedagio)){
							descricao.append(" ").append(dsSimbolo).append(" ").append(configuracoesFacade.getMensagem("porConhecimento"));
							valor.append(vlGeneralidadeFormatado);
						} else if ("Q".equals(tpIndicadorPedagio)
								&& (BigDecimalUtils.hasValue(vlPedagio) || ConstantesExpedicao.CD_PEDAGIO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))) {
							descricao.append(" ").append(dsSimbolo).append(" ").append(configuracoesFacade.getMensagem("porQuiloOuFracao"));
							valor.append(vlGeneralidadeFormatado);
						} else if ("X".equals(tpIndicadorPedagio) && (BigDecimalUtils.hasValue(vlPedagio) || ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO")))) {
							valor.append("Se peso entre 0 kg e 100 kg: ")
									.append(dsSimbolo).append(" ")
									.append(vlGeneralidadeFormatado);
							valor.append("\nSe peso entre 101 kg e 500 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("2"))));
							valor.append("\nSe peso entre 501 kg e 1000 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("3"))));
							valor.append("\nAcima de 1000 kg: ")
									.append(dsSimbolo)
									.append(" ")
								.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.add(vlGeneralidade.multiply(new BigDecimal("3"))))+ " por fração \nde 1000 kg");
						} 
					}

					if(StringUtils.isNotBlank(valor.toString())) {
						foundPedagio = true;
						descricao = new StringBuilder(configuracoesFacade.getMensagem("pedagio"));
					}
				}
				
				
				descricao = new StringBuilder(configuracoesFacade.getMensagem("pedagio")).append(": ");
				
				vlGeneralidadeFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS, vlGeneralidade);
				
				if ("F".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("reaisPorFracao"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("P".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("reaisPorPostoFracao"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("D".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("porConhecimento"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("X".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					valor.append("Se peso entre 0 kg e 100 kg: ")
						.append(dsSimbolo).append(" ")
						.append(vlGeneralidadeFormatado);
					valor.append("\nSe peso entre 101 kg e 500 kg: ")
						.append(dsSimbolo)
						.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("2"))));
					valor.append("\nSe peso entre 501 kg e 1000 kg: ")
						.append(dsSimbolo)
						.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("3"))));
					valor.append("\nAcima de 1000 kg: ")
						.append(dsSimbolo)
						.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.add(vlGeneralidade.multiply(new BigDecimal("3"))))+ " por fração \nde 1000 kg");
				} else {
					valor = null; // Para não inserir
				}
				
				if(valor == null || StringUtils.isBlank(valor.toString())){
					descricao = new StringBuilder(configuracoesFacade.getMensagem("pedagio"));
				}

			} else if(ConstantesExpedicao.CD_TAS.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTas"));
				valor.append(vlGeneralidadeFormatado);
			} else if(ConstantesExpedicao.CD_TAD.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("reaisPorFracao"));
				valor.append(vlGeneralidadeFormatado);
			} else if(ConstantesExpedicao.CD_TRANSFERENCIA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))){
			if(BigDecimalUtils.hasValue(vlGeneralidade)){
					descricao.append(dsSimbolo)
					.append(" ")
					.append(configuracoesFacade.getMensagem("porMetroCubico"));
					
					valor.append(vlGeneralidadeFormatado);
			}else{
				continue;
			}
			} else if(ConstantesExpedicao.CD_SEGURO_FLUVIAL.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeSegFluvial"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_SUFRAMA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeSuframa"));
				valor.append(vlGeneralidadeFormatado);

				
			} else if(ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTSB"));
				valor.append(vlGeneralidadeFormatado);
			} else if(ConstantesExpedicao.CD_GRIS.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				descricao.append(" ").append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt1"));

				Map<String,Object> values = new HashMap<String, Object>();
				values.put("valor", vlGeneralidade);
				values.put("parametro", idParametro);
				values.put("novaTabela", isTabelaNova);
				values.put("reajuste", pcReajuste);
				values.put("minimo", vlMinimo);
				
				valor.append(getValorGris(values, jdbcTemplate,configuracoesFacade));				
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt2"));
					minimoValor.append(formatDuasCasasDecimais(vlMinimo));
				}
			} else if(ConstantesExpedicao.CD_TRT.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				descricao.append(" ")
				.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTRT"));
				
				Map<String, Object> values = new HashMap<String, Object>(); 
				values.put("valor", vlGeneralidade);
				values.put("minimo", vlMinGeneralidade);
				
				List parametros = jdbcTemplate.queryForList(montaSqlParametrosCustom(),	new Long[] { idParametro });
				if(!parametros.isEmpty()){
					Map paramMapped = (Map) parametros.get(0);
						
					values.put("percentual",MapUtilsPlus.getBigDecimal(paramMapped,"VL_PERCENTUAL_TRT", BigDecimal.ZERO));
					values.put("minimoEspecifico",MapUtilsPlus.getBigDecimal(paramMapped, "VL_MINIMO_TRT",BigDecimal.ZERO));
					values.put("tpPercentual",MapUtils.getString(paramMapped,"TP_INDICADOR_PERCENTUAL_TRT"));
					values.put("tpMinimo",MapUtils.getString(paramMapped, "TP_INDICADOR_MINIMO_TRT"));
				}
						
				valor.append(getValorTRTTDE(values, configuracoesFacade));
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt2"));
					minimoValor.append(FormatUtils.formatDecimal(FORMATO_2_CASAS,vlMinimo));
					}
						
						
			} else if(ConstantesExpedicao.CD_TDE.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))){
				descricao.append(" ")
				.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTRT"));
				
				Map<String, Object> values = new HashMap<String, Object>(); 
				values.put("valor", vlGeneralidade);
				values.put("minimo", vlMinGeneralidade);
				
				List parametros = jdbcTemplate.queryForList(montaSqlParametrosCustom(),	new Long[] { idParametro });
				if(!parametros.isEmpty()){
					Map paramMapped = (Map) parametros.get(0);

					values.put("percentual",MapUtilsPlus.getBigDecimal(paramMapped,"VL_PERCENTUAL_TDE", BigDecimal.ZERO));
					values.put("minimoEspecifico",MapUtilsPlus.getBigDecimal(paramMapped, "VL_MINIMO_TDE",BigDecimal.ZERO));
					values.put("tpPercentual",MapUtils.getString(paramMapped,"TP_INDICADOR_PERCENTUAL_TDE"));
					values.put("tpMinimo",MapUtils.getString(paramMapped, "TP_INDICADOR_MINIMO_TDE"));
					}
				
				valor.append(getValorTRTTDE(values, configuracoesFacade));
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt2"));
					minimoValor.append(FormatUtils.formatDecimal(FORMATO_2_CASAS,vlMinimo));
					}
						
						
		
			} else if("IDAgendamentoColetaEntrega".equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))){
				valor.append(vlGeneralidadeFormatado)
				.append(configuracoesFacade.getMensagem("porCTRC"));
			} else if("IDCustoDescarga".equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))){
				valor.append(vlGeneralidadeFormatado)
				.append(configuracoesFacade.getMensagem("porVolume"));
			} else if("IDTaxaPaletizacao".equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))){
				valor.append(vlGeneralidadeFormatado)
				.append(configuracoesFacade.getMensagem("porKg"));
			} else if("IDVeiculoDedicado".equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))){
				valor.append(vlGeneralidadeFormatado)
				.append(configuracoesFacade.getMensagem("porVeiculoDedicado"));
			} else if(ConstantesExpedicao.CD_REFATURAMENTO.equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))){
				descricao.append(configuracoesFacade.getMensagem("percentagemValorFreteOriginal"));
				valor.append(vlGeneralidadeFormatado);
				
				
			} else if("VL".equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))) {
				descricao.append(": ").append(configuracoesFacade.getMensagem("reaisPorConhecimento"));
				valor.append(vlGeneralidadeFormatado);
				
				
			} else if("PC".equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))) {
				descricao.append(configuracoesFacade.getMensagem("relSobreValorMercadoria"));
				valor.append(vlGeneralidadeFormatado);
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("relOuMinimo"));
					minimoValor.append(FormatUtils.formatDecimal(FORMATO_2_CASAS,vlMinimo));
					}
						
			} else if (ConstantesExpedicao.CD_ADVALOREM_1.equalsIgnoreCase(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				
				/**
				 * Aéreo
				 */
				List listaAdvalorem = jdbcTemplate.queryForList(montaSqlParametrosCustom(), new Long[] { idParametro });
				if(!listaAdvalorem.isEmpty()){
					Map mapGris = (Map) listaAdvalorem.get(0);
	
					String tpIndicadorAdvalorem = MapUtils.getString(mapGris,"TP_INDICADOR_ADVALOREM");
					BigDecimal vlPercentualAdvalorem = MapUtilsPlus.getBigDecimal(mapGris, "VL_ADVALOREM",BigDecimal.ZERO);
	
					if(!TABELA.equals(tpIndicadorAdvalorem)) {
						vlGeneralidade = calculaPercentagem(tpIndicadorAdvalorem, vlPercentualAdvalorem,vlGeneralidade);
				}
						
					valor.append(formatDuasCasasDecimais(vlGeneralidade));
					valor.append(configuracoesFacade.getMensagem("relSobreValorMercadoria"));
					
					if (vlMinimo != null) {
						minimoDescricao.append(configuracoesFacade.getMensagem("relOuMinimo"));
						minimoValor.append(FormatUtils.formatDecimal(FORMATO_2_CASAS,vlMinGeneralidade));
				}
				}
			} else if (ConstantesExpedicao.CD_ADVALOREM_2.equalsIgnoreCase(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
						
				/**
				 * Aéreo
				 */
				List listaAdvalorem = jdbcTemplate.queryForList(montaSqlParametrosCustom(), new Long[] { idParametro });
				if(!listaAdvalorem.isEmpty()){
					Map mapGris = (Map) listaAdvalorem.get(0);
	
					String tpIndicadorAdvalorem = MapUtils.getString(mapGris,"TP_INDICADOR_ADVALOREM_2");
					BigDecimal vlPercentualAdvalorem = MapUtilsPlus.getBigDecimal(mapGris, "VL_ADVALOREM_2",BigDecimal.ZERO);
	
					if(!TABELA.equals(tpIndicadorAdvalorem)) {
						vlGeneralidade = calculaPercentagem(tpIndicadorAdvalorem, vlPercentualAdvalorem,vlGeneralidade);
					}
					
					valor.append(formatDuasCasasDecimais(vlGeneralidade));
					valor.append(configuracoesFacade.getMensagem("relSobreValorMercadoria"));
					
					if (vlMinimo != null) {
						minimoDescricao.append(configuracoesFacade.getMensagem("relOuMinimo"));
						minimoValor.append(FormatUtils.formatDecimal(FORMATO_2_CASAS,vlMinGeneralidade));
				}
			}
			}
				
			// LMS-11142
			if(idParametroCliente != null
					&& !TABELA.equals(MapUtils.getString(data,"TP_INDICADOR"))
					&& !"PF".equals(MapUtils.getString(data,"TP_INDICADOR"))
					&& !ConstantesExpedicao.CD_GRIS.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))
					&& !ConstantesExpedicao.TIPOS_PEDAGIO.contains(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				// Regra aplicada de acordo com o requisito de número 11142.
					verificaSuspensao(valor, MapUtils.getString(data,"TP_INDICADOR"),vlGeneralidadeCliente, configuracoesFacade);
			}
				
				// Regra do caso de uso de minimo progressivo por taxa embutida
				// 30.03.02.16
			if("S".equals(blEmbuteParcela)) {
				Map generalidadesEmbutidas = new HashMap();		
				generalidadesEmbutidas.put(descricao.toString(),vlOriginal);					
				if( "Y".equals(MapUtils.getString(data,"TP_SUBTIPO_TABELA_PRECO")) ){
						generalidadesEmbutidas.put("despacho_cliente",vlGeneralidade);
				}
				data.put("EMBUTIDO",generalidadesEmbutidas);
			}
			
			/** Mensagem quando psMinimo for informado */
			if ((valor != null && StringUtils.isNotBlank(valor.toString()))	&& BigDecimalUtils.hasValue(psMinimo)) {
				valor.append(" acima de ").append(FormatUtils.formatDecimal(FORMATO_3_CASAS,psMinimo)).append(" kg.");
			}
			
			putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			}
		}

	private static String formatDuasCasasDecimais(BigDecimal vlGeneralidade) {
		vlGeneralidade = vlGeneralidade != null ? vlGeneralidade : BigDecimal.ZERO;  
		return FormatUtils.formatDecimal(FORMATO_2_CASAS, vlGeneralidade);
		}

	/**
	 * Método que recebe a lista de generalidades e gera o subRelatorio de
	 * legendas delas
	 * 
	 * @param listaGeneralidades
	 * @return
	 */
	public static List montaPageFooter(List listaGeneralidades){
		if(listaGeneralidades.isEmpty()){
			return new ArrayList();
		}
		List resultLegenda = new ArrayList();
		Map mapLegenda = new HashMap();
		int cont = 1;

		for (Iterator iter = listaGeneralidades.iterator(); iter.hasNext();) {
			Map data = (Map) iter.next();
			String nmGene = data.get("NM_PARCELA_PRECO") != null ? (String) data
					.get("NM_PARCELA_PRECO") : (String) data.get("NM_PARCELA");
			String parcelaPreco = data.get("CD_PARCELA_PRECO") != null ? (String) data
					.get("CD_PARCELA_PRECO") : (String) data.get("CD_PARCELA");
			String dsGene = data.get("DS_PARCELA_PRECO") != null ? (String) data
					.get("DS_PARCELA_PRECO") : (String) data.get("DS_PARCELA");

			if (ConstantesExpedicao.CD_GRIS.equals(parcelaPreco)
					|| ConstantesExpedicao.CD_TAD.equals(parcelaPreco)
					|| ConstantesExpedicao.CD_TDE.equals(parcelaPreco)
					|| ConstantesExpedicao.CD_TAS.equals(parcelaPreco)
					|| ConstantesExpedicao.CD_ITR.equals(parcelaPreco)
					|| ConstantesExpedicao.CD_CAT.equals(parcelaPreco)) {
				
				String legenda = dsGene + ": " + nmGene;
				if(!mapLegenda.containsValue(legenda)) {
					mapLegenda.put("desc"+ cont, legenda);
					resultLegenda.add(mapLegenda);
					cont++;
				}
			}
		}
		return resultLegenda;
	}

	/**
	 * Metodo que gera as generalidades da tabela de preço
	 * 
	 * @param idTabelaPreco
	 * @param dsSimbolo
	 * @param tabela
	 * @param crosstabSize
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getGeneralidadesTabelaPreco(String query,
			Long idTabelaPreco, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getSubRelGeneralidadesTabelaPreco(query, idTabelaPreco,
				dsSimbolo, null, 0, 0, configuracoesFacade, jdbcTemplate);
	}

	/**
	 * Metodo que gera as generalidades da tabela de preço para os relatorios de
	 * minimo progressivo
	 * 
	 * @param idTabelaPreco
	 * @param dsSimbolo
	 * @param tabela
	 * @param crosstabSize
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getGeneralidadesTabelaPreco(String query,
			Long idTabelaPreco, String dsSimbolo, String tabela,
			int crosstabSize, int posMinimo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return getSubRelGeneralidadesTabelaPreco(query, idTabelaPreco,
				dsSimbolo, tabela, crosstabSize, posMinimo,
				configuracoesFacade, jdbcTemplate);
	}

	/**
	 * Lista generalidades que NÃO SEJAM do tipo PEDÁGIO
	 * @param idTabelaPreco
	 * @param dsSimbolo
	 * @param tabela
	 * @param crosstabSize
	 * @param posPsminimo
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelGeneralidadesTabelaPreco(String query,Long idTabelaPreco, String dsSimbolo, String tabela,int crosstabSize, int posPsminimo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		
		List<Map> generalidades = jdbcTemplate.queryForList(query,
				new Long[] { idTabelaPreco });
		String vlMinimoReentrega = null;
		
		for (Map data : generalidades) {
			StringBuilder descricao = new StringBuilder(MapUtils.getString(data,"DS_PARCELA_PRECO"));
			StringBuilder valor = new StringBuilder();
			StringBuilder minimoDescricao = new StringBuilder();
			StringBuilder minimoValor = new StringBuilder();

			BigDecimal vlGeneralidade = new BigDecimal(MapUtils.getString(data,"VL_GENERALIDADE"));
			String vlGeneralidadeFormatado = formatDuasCasasDecimais(vlGeneralidade);
			

			BigDecimal vlMinimo = MapUtilsPlus.getBigDecimal(data, "VL_MINIMO");

			if("IDMinimoReentrega".equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))){
				vlMinimoReentrega = vlGeneralidadeFormatado;
				continue;
			}
			

			/** Mensagem quando psMinimo for o informado */
			BigDecimal psMinimo = MapUtilsPlus.getBigDecimal(data, "PS_MINIMO");
			if (StringUtils.isNotBlank(valor.toString()) && BigDecimalUtils.hasValue(psMinimo)) {
				minimoDescricao.append(" acima de ").append(FormatUtils.formatDecimal(FORMATO_3_CASAS, psMinimo)).append(" kg.");
			}
			
			/** Lista generalidades que SEJAM do tipo PEDÁGIO */
			if(ConstantesExpedicao.TIPOS_PEDAGIO.contains(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao = new StringBuilder(configuracoesFacade.getMensagem("pedagio")).append(": ");
				
				vlGeneralidadeFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS, vlGeneralidade);
				
				if ("F".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO")) && ConstantesExpedicao.CD_PEDAGIO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("reaisPorFracao"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("P".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("reaisPorPostoFracao"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("D".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					descricao.append(configuracoesFacade.getMensagem("porConhecimento"));
					valor.append(vlGeneralidadeFormatado);
				} else if ("X".equals(MapUtils.getString(data,"TP_CALCULO_PEDAGIO"))	&& ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
					StringBuilder msg = new StringBuilder();
					msg.append("Se peso entre 0 kg e 100 kg: ")
							.append(dsSimbolo).append(" ")
							.append(vlGeneralidadeFormatado);
					msg.append("\nSe peso entre 101 kg e 500 kg: ")
							.append(dsSimbolo)
							.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("2"))));
					msg.append("\nSe peso entre 501 kg e 1000 kg: ")
							.append(dsSimbolo)
							.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.multiply(new BigDecimal("3"))));
					msg.append("\nAcima de 1000 kg: ")
							.append(dsSimbolo)
							.append(" ")
						.append(FormatUtils.formatDecimal(FORMATO_5_CASAS,vlGeneralidade.add(vlGeneralidade.multiply(new BigDecimal("3"))))+ " por fração \nde 1000 kg");
					
					valor.append(msg.toString());
				} else {
					valor = null; // Para não inserir
				}
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_TAS.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTas"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_TAD.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("reaisPorFracao"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			}else if(ConstantesExpedicao.CD_TRANSFERENCIA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))){
				if(BigDecimalUtils.hasValue(vlGeneralidade)){
					descricao.append(dsSimbolo)
					.append(" ")
					.append(configuracoesFacade.getMensagem("porMetroCubico"));
					
					valor.append(vlGeneralidadeFormatado);
				
					putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
				}else{
					continue;
				}
			} else if(ConstantesExpedicao.CD_SEGURO_FLUVIAL.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeSegFluvial"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_SUFRAMA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeSuframa"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA.equals(MapUtils.getString(data, "CD_PARCELA_PRECO"))) {
				descricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTSB"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_GRIS.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				descricao.append(" ")
				.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt1"));
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("valor", vlGeneralidade);
				
				valor.append(getValorGris(map));
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt2"));
					minimoValor.append(formatDuasCasasDecimais(vlMinimo));
				}
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_TRT.equals(MapUtils.getString(data,"CD_PARCELA_PRECO"))) {
				descricao.append(" ").append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeTRT"));
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("valor", vlGeneralidade);
				
				valor.append(getValorTRTTDE(map));
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("emissaoTabelaGeneralidadeGrisPt2"));
					minimoValor.append(formatDuasCasasDecimais(vlMinimo));
				}
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if(ConstantesExpedicao.CD_REFATURAMENTO.equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))){
				descricao.append(configuracoesFacade.getMensagem("percentagemValorFreteOriginal"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if("VL".equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))) {
				descricao.append(": ").append(configuracoesFacade.getMensagem("reaisPorConhecimento"));
				valor.append(vlGeneralidadeFormatado);
				
				putDataLines(data, descricao, valor, minimoDescricao, minimoValor);
			} else if("PC".equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))) {
				descricao.append(configuracoesFacade.getMensagem("relSobreValorMercadoria"));
				valor.append(vlGeneralidadeFormatado);
				if (vlMinimo != null) {
					minimoDescricao.append(configuracoesFacade.getMensagem("relOuMinimo"));
					minimoValor.append(formatDuasCasasDecimais(vlMinimo));
			}
			}

			embutirParcelas(data,tabela,crosstabSize,vlGeneralidade,posPsminimo,jdbcTemplate);
			}


		Map additionalData = mountAddtionalData(idTabelaPreco, dsSimbolo,configuracoesFacade, jdbcTemplate, vlMinimoReentrega);
		
		generalidades.add(additionalData);

		return generalidades;
	}
	
	private static String getValorTRTTDE(Map values) {
		return getValorTRTTDE(values, null);
	}
	

	private static String getValorTRTTDE(Map values,ConfiguracoesFacade configuracoesFacade) {
		BigDecimal vlGeneralidade = (BigDecimal) values.get("valor");
		
		if(configuracoesFacade == null && !values.containsKey("parametros")){
			return formatDuasCasasDecimais(vlGeneralidade);
		}
		
		Map paramMapped = (Map) values.get("parametros");

		BigDecimal vlMinimo = (BigDecimal) values.get("minimo");
		
		
		BigDecimal vlParam = MapUtilsPlus.getBigDecimal(values,"percentual", BigDecimal.ZERO);
		BigDecimal vlMinimoParam = MapUtilsPlus.getBigDecimal(values,"minimoEspecifico", BigDecimal.ZERO);
		String tpIndicadorParam = MapUtilsPlus.getString(values,"tpPercentual");
		String tpIndicMinimoParam =MapUtilsPlus.getString(values,"tpMinimo");

		if(TABELA.equals(tpIndicMinimoParam)){
			vlMinimoParam = vlMinimo;
			
		}
		if(!TABELA.equals(tpIndicadorParam)) {
				vlGeneralidade = calculaPercentagem(tpIndicadorParam, vlParam, vlGeneralidade);
		}
			
		return formatDuasCasasDecimais(vlGeneralidade);		
	}

	
	private static String getValorGris(Map<String,Object> values) {
		return getValorGris(values,null,null);
	}
	
	/**
	 * 30.03.02.15 - LINHAS DE DETALHE 3.3 - Se a generalidade da tabela de preço for igual a gris (PARCELA_PRECO.CD_PARCELA_PRECO = ‘IDGris’)
	 * @param values
	 * @param jdbcTemplate
	 * @return
	 */
	private static String getValorGris(Map<String,Object> values ,JdbcTemplate jdbcTemplate, ConfiguracoesFacade configuracoesFacade) {
		if(jdbcTemplate == null && !values.containsKey("parametro")){
			return formatDuasCasasDecimais((BigDecimal) values.get("valor"));
		}
		
		BigDecimal vlGeneralidade = null;
		
		List listaGris = jdbcTemplate.queryForList(montaSqlParametrosCustom(), new Object[] { values.get("parametro") });
		if(!listaGris.isEmpty()) {
			Map mapGris = (Map) listaGris.get(0);
			
			String tpIndicadorMinimo = MapUtils.getString(mapGris,"TP_INDICADOR_MINIMO_GRIS");
			BigDecimal vlMinimoGris = MapUtilsPlus.getBigDecimal(mapGris, "VL_MINIMO_GRIS", null);
			String tpIndicadorPercentual = MapUtils.getString(mapGris,"TP_INDICADOR_PERCENTUAL_GRIS");
			BigDecimal vlPercentualGris = MapUtilsPlus.getBigDecimal(mapGris, "VL_PERCENTUAL_GRIS", BigDecimal.ZERO);

			if (Boolean.TRUE.equals(values.get("novaTabela"))) {
				TabelasClienteUtil.reajustarParametros(mapGris,(BigDecimal) values.get("reajuste"));
			}

			if(TABELA.equals(tpIndicadorMinimo)){
				vlMinimoGris = (BigDecimal) values.get("minimo");
				
			}
			if(!TABELA.equals(tpIndicadorPercentual)) {
				vlGeneralidade = calculaPercentagem(tpIndicadorPercentual, vlPercentualGris,vlGeneralidade);
			}
			
			// LMS-11142.
			if ((VALOR.equals(tpIndicadorPercentual) && BigDecimalUtils.isZero(vlPercentualGris)) || 
				(DESCONTO.equals(tpIndicadorPercentual) && BigDecimalUtils.HUNDRED.equals(vlPercentualGris) && VALOR.equals(tpIndicadorMinimo) && BigDecimalUtils.isZero(vlMinimoGris)) || 
				(DESCONTO.equals(tpIndicadorMinimo) && BigDecimalUtils.HUNDRED.equals(vlMinimoGris))
				) {
				return configuracoesFacade.getMensagem("suspenso");
			}

		}

		
		return formatDuasCasasDecimais(vlGeneralidade);
	}

	private static void embutirParcelas(Map data, String tabela, int crosstabSize, BigDecimal vlGeneralidade, int posPsminimo, JdbcTemplate jdbcTemplate){
			if ("S".equals(data.get("BL_EMBUTE_PARCELA").toString())
				&& "VL".equals(MapUtils.getString(data,"TP_INDICADOR_CALCULO"))
					&& data.get("TP_SUBTIPO_TABELA_PRECO").toString()
							.equals("Y")) {
				StringBuffer sql = new StringBuffer();
				if(StringUtils.isNotBlank(tabela)){
					crosstabSize = 0 == crosstabSize ? 1 : crosstabSize; 
					sql.append("UPDATE " + tabela + " SET COLUMN1 = COLUMN1 + "
							+ vlGeneralidade);
					for (int j = 2; j <= crosstabSize; j++) {
						if(posPsminimo >= j)
							sql.append(" , COLUMN" + j + " = COLUMN" + j
									+ " + " + vlGeneralidade);
					}
					jdbcTemplate.update(sql.toString());
				}
			}
			}


	private static Map mountAddtionalData(Long idTabelaPreco, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate,
			String vlMinimoReentrega) {
		String reentrega = null;
		String devolucao = null;
		
		List<Map> generalidadesSub = jdbcTemplate.queryForList(montaSqlGeneralidadesSub());
		
		for (Map data : generalidadesSub) {
			reentrega = data.get(REENTREGA).toString();
			devolucao = data.get(DEVOLUCAO).toString();
		}
		
		
		Map additionalData = new HashMap();
		
		String strReentrega = FormatUtils.formatDecimal(FORMATO_2_CASAS,
				new BigDecimal(reentrega))
				+ " "
				+ configuracoesFacade.getMensagem("percentualfreteOriginal");
		String strDevolucao = FormatUtils.formatDecimal(FORMATO_2_CASAS,
				new BigDecimal(devolucao))
				+ " "
				+ configuracoesFacade.getMensagem("percentualfreteOriginal");
		if (vlMinimoReentrega != null) {
			strReentrega += " "
					+ configuracoesFacade.getMensagem("relOuMinimo")
					+ " "
					+ dsSimbolo
					+ " "
					+ FormatUtils.formatDecimal(FORMATO_2_CASAS,
							new BigDecimal(vlMinimoReentrega));
		}
		
		additionalData.put(REENTREGA,strReentrega);
		additionalData.put(DEVOLUCAO,strDevolucao);

		// Verifica se tem taxa combustivel para colocar observacao na ultima
		// linha de generalidades
		List taxaCombustivel = jdbcTemplate.queryForList(
				montaSQLTaxaCombustivel(), new Long[] { idTabelaPreco });
		if(taxaCombustivel!=null && !taxaCombustivel.isEmpty()){
			additionalData.put("TAXACOMBUSTIVEL",
					configuracoesFacade.getMensagem("videTabTaxaCombustivel"));
		}
		return additionalData;
	}

	private static void putDataLines(Map data, StringBuilder descricao,
			StringBuilder valor,StringBuilder minimoDescricao, StringBuilder minimoValor) {
		if(valor != null && StringUtils.isNotBlank(valor.toString())) {
			data.put("NOMEGENERALIDADES",descricao.toString());
			data.put("VALORGENERALIDADES",valor.toString());
			if(minimoValor != null && StringUtils.isNotBlank(minimoValor.toString())){
				data.put("NOMEMINIMO",minimoDescricao.toString());
				data.put("VALORMINIMO",minimoValor.toString());
	}
		}
	}

	/**
	 * Metodo que recebe um indicador, um valor percentual e um valor e
	 * desconta/acresenta/adiciona o valor percentual ao valor, de acordo com o
	 * indicador
	 * 
	 * @param tp
	 * @param vlPercentual
	 * @param vl
	 * @return o valor calculado
	 */
	public static BigDecimal calculaPercentagem(String tp,
			BigDecimal vlPercentual, BigDecimal vl) {
		if(VALOR.equals(tp)){
			vl = vlPercentual;
		}else if(ACRESCIMO.equals(tp)){
			if(BigDecimalUtils.hasValue(vlPercentual)){
				BigDecimal acresimo = vlPercentual;
				vl = BigDecimalUtils.acrescimo(vl,acresimo);
			}
		}else if(DESCONTO.equals(tp)){
			if(BigDecimalUtils.hasValue(vlPercentual)){
				BigDecimal vlAux = vlPercentual;
				if(vlAux.longValue() >= 100){
					return BigDecimal.ZERO;
				}
				vl = BigDecimalUtils.desconto(vl,vlAux);
			}
		}else if(PONTOS_PESO.equals(tp)){
			vl = vl.add(vlPercentual);
		}else if(TABELA.equals(tp)){
			return vl;
		}else if(METROS_CUBICOS.equals(tp)){
			vl = vlPercentual;
		}else{
			vl = vlPercentual;
		}
		return vl; 
	}

	/**
	 * Adiciona os subrelatorios contidos no array de subRelatorios no map
	 * passado por parametro
	 * 
	 * @return - O mesmo map passado por parametro (vai e volta)
	 */
	public static Map montaSubRelatoriosOfChoice(Long idCliente,
			Long idDivisao, Long idParametro, Long idTabelaPreco,
			Boolean isTabelaNova, Map parametersReport, int[] subReports,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		return montaSubRelatorios(idCliente, idDivisao, idParametro,
				idTabelaPreco, isTabelaNova, null, null, 0, parametersReport,
				subReports, configuracoesFacade, jdbcTemplate);
	}

	public static Map montaSubRelatoriosProposta(Long idSimulacao,
			Long idCliente, Long idDivisao, Long idParametro,
			Long idTabelaPreco, Boolean isTabelaNova, Map parametersReport,
			int[] subReports, ConfiguracoesFacade configuracoesFacade,
			JdbcTemplate jdbcTemplate) {
		return montaSubRelatorios(idCliente, idDivisao, idParametro,
				idTabelaPreco, isTabelaNova, idSimulacao, null, 0,
				parametersReport, subReports, configuracoesFacade, jdbcTemplate);
	}

	/**
	 * Adiciona os subrelatorios quando nao se tem um parametro, por isso chama
	 * a generalidade da tabela de preco.
	 * 
	 * @return
	 */
	public static Map montaSubRelatoriosOfChoice(Long idCliente,
			Long idDivisao, String nomeTabela, int nroCrosstabColumns,
			Long idTabelaPreco, Boolean isTabelaNova, Map parametersReport,
			int[] subReports, ConfiguracoesFacade configuracoesFacade,
			JdbcTemplate jdbcTemplate) {
		return montaSubRelatorios(idCliente, idDivisao, null, idTabelaPreco,
				isTabelaNova, null, nomeTabela, nroCrosstabColumns,
				parametersReport, subReports, configuracoesFacade, jdbcTemplate);
	}

	public static List getFormalidadesClienteRodoviarioNacional(
			Long idTabelaPreco, Long idCliente, Long idDivisao,
			JdbcTemplate jdbcTemplate) {
		// formalidades de clientes do tipo modal RODOVIÁRIO e abrangência
		// NACIONAL
		return getSubRelFormalidades(idTabelaPreco, idCliente, idDivisao, null,
				'R', 'N', jdbcTemplate);
	}

	public static List getFormalidadesClienteAereoNacional(Long idTabelaPreco,
			Long idCliente, Long idDivisao,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		// formalidades de clientes do tipo modal RODOVIÁRIO e abrangência
		// NACIONAL
		List lista = getSubRelFormalidades(idTabelaPreco, idCliente, idDivisao,
				null, 'A', 'N', jdbcTemplate);
		if (lista.size() == 0)
			return null;
		Map map = (Map)lista.get(0);
		map.put("CARGAS", "CARGAS");
		map.put("DAC","DAC");
		map.put("SUFRAMA", "SUFRAMA");
		return lista;
	}

	public static List getFormalidadesPropostaRodoviarioNacional(
			Long idTabelaPreco, Long idCliente, Long idDivisao,
			Long idSimulacao, JdbcTemplate jdbcTemplate) {
		// formalidades de clientes do tipo modal RODOVIÁRIO e abrangência
		// NACIONAL
		return getSubRelFormalidades(idTabelaPreco, idCliente, idDivisao,
				idSimulacao, 'R', 'N', jdbcTemplate);
	}

	public static List getFormalidadesPropostaAereoNacional(Long idTabelaPreco,
			Long idCliente, Long idDivisao, Long idSimulacao,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		// formalidades de clientes do tipo modal RODOVIÁRIO e abrangência
		// NACIONAL
		List lista = getSubRelFormalidades(idTabelaPreco, idCliente, idDivisao,
				idSimulacao, 'A', 'N', jdbcTemplate);
		if (lista.size() == 0)
			return null;
		Map map = (Map)lista.get(0);
		map.put("CARGAS", "CARGAS");
		map.put("DAC","DAC");
		map.put("SUFRAMA", "SUFRAMA");
		return lista;
	}

	public static List getFormalidadesAereoNacional(Long idTabelaPreco,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		// formalidades de clientes do tipo modal RODOVIÁRIO e abrangência
		// NACIONAL
		List lista = getSubRelFormalidades(idTabelaPreco, null, null, null,
				'A', 'N', jdbcTemplate);
		if (lista.size() == 0)
			return null;
		Map map = (Map)lista.get(0);
		map.put("CARGAS", "CARGAS");
		map.put("DAC","DAC");
		map.put("SUFRAMA", "SUFRAMA");
		map.remove("NM_DIVISAO");
		return lista;
	}

	private static Map montaSubRelatorios(Long idCliente, Long idDivisao,
			Long idParametro, Long idTabelaPreco, Boolean isTabelaNova,
			Long idSimulacao, String tabela, int nroColunas,
			Map parametersReport, int[] subRelatorios,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		if(subRelatorios != null){
			//Busca o simbolo da moeda
			String dsSimbolo = getMoeda(idTabelaPreco, jdbcTemplate);
			parametersReport.put("DS_SIMBOLO", dsSimbolo);
			
			List coleta = new ArrayList();
			List entrega = new ArrayList();
			List taxaTerrestre = new ArrayList();
			List taxaCombustivel = new ArrayList();
			List aereo = new ArrayList();
			List legendas = new ArrayList();
			List formalidades = new ArrayList();
			List generalidades = new ArrayList();
			List servicosContratados = new ArrayList();
			List servicosNaoContratados = new ArrayList();
			List<Map> generalidadesDificuldadeEntrega = new ArrayList<Map>();
			if(containsSubReport(SUBREPORT_COLETA,subRelatorios)){
				coleta = getSubRelColeta(idTabelaPreco,jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_COLETA,coleta);
			} else if (containsSubReport(SUBREPORT_COLETA_CLIENTE,
					subRelatorios)) {
				coleta = getSubRelColetaCliente(idTabelaPreco, idParametro,
						jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_COLETA,coleta);
			}
			
			if(containsSubReport(SUBREPORT_ENTREGA,subRelatorios)){
				entrega = getSubRelEntrega(idTabelaPreco,jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_ENTREGA,entrega);
			} else if (containsSubReport(SUBREPORT_ENTREGA_CLIENTE,
					subRelatorios)) {
				entrega = getSubRelEntregaCliente(idTabelaPreco, idParametro,
						jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_ENTREGA,entrega);
			}
			
			if(containsSubReport(SUBREPORT_AEREO,subRelatorios)){
				aereo = getSubRelAereo(jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_AEREO, aereo);
			}

			if (containsSubReport(SUBREPORT_TAXA_TERRESTRE, subRelatorios)) {
				if(idParametro == null){
					taxaTerrestre = getSubRelTaxaTerrestre(idTabelaPreco,
							jdbcTemplate);
				}else{
					taxaTerrestre = getSubRelTaxaTerrestre(idTabelaPreco,
							idParametro, jdbcTemplate);
				}
				parametersReport.put(KEY_PARAMETER_TAXA_TERRESTRE,
						taxaTerrestre);
			}
			
			if(containsSubReport(SUBREPORT_LEGENDAS,subRelatorios)){
				legendas = getSubRelLegendas(jdbcTemplate);
				parametersReport.put(KEY_PARAMETER_LEGENDAS,legendas);
			}
			
			if(containsSubReport(SUBREPORT_FORMALIDADES,subRelatorios)){
				if(idSimulacao != null){
					formalidades = getFormalidadesPropostaRodoviarioNacional(
							idTabelaPreco, idCliente, idDivisao, idSimulacao,
							jdbcTemplate);
				}else{
					formalidades = getFormalidadesClienteRodoviarioNacional(
							idTabelaPreco, idCliente, idDivisao, jdbcTemplate);
				}
				parametersReport.put(KEY_PARAMETER_FORMALIDADES,formalidades);
			} else if (containsSubReport(SUBREPORT_FORMALIDADES_AEREO,
					subRelatorios)) {
				if(idSimulacao != null){
					formalidades = getFormalidadesPropostaAereoNacional(
							idTabelaPreco, idCliente, idDivisao, idSimulacao,
							configuracoesFacade, jdbcTemplate);
				}else{
					formalidades = getFormalidadesClienteAereoNacional(
							idTabelaPreco, idCliente, idDivisao,
							configuracoesFacade, jdbcTemplate);
				}
				parametersReport.put(KEY_PARAMETER_FORMALIDADES,formalidades);
			} else if (containsSubReport(SUBREPORT_FORMALIDADES_AEREO_VOLUME,
					subRelatorios)) {
				if(idSimulacao != null){
					formalidades = getFormalidadesPropostaAereoNacional(
							idTabelaPreco, idCliente, idDivisao, idSimulacao,
							configuracoesFacade, jdbcTemplate);
				}else{
					formalidades = getFormalidadesClienteAereoNacional(
							idTabelaPreco, idCliente, idDivisao,
							configuracoesFacade, jdbcTemplate);
				}
				parametersReport.put(KEY_PARAMETER_FORMALIDADES,formalidades);
			} else if (containsSubReport(
					SUBREPORT_FORMALIDADES_AEREO_PERCENTUAL, subRelatorios)) {
				if(idSimulacao != null){
					formalidades = getFormalidadesPropostaAereoNacional(
							idTabelaPreco, idCliente, idDivisao, idSimulacao,
							configuracoesFacade, jdbcTemplate);
				}else{
					formalidades = getFormalidadesClienteAereoNacional(
							idTabelaPreco, idCliente, idDivisao,
							configuracoesFacade, jdbcTemplate);
				}
				parametersReport.put(KEY_PARAMETER_FORMALIDADES,formalidades);
			}
			
			if(containsSubReport(SUBREPORT_GENERALIDADES,subRelatorios)){
				if(idParametro != null){
					generalidades = getSubRelGeneralidades(idParametro,
							idTabelaPreco, isTabelaNova, dsSimbolo,
							configuracoesFacade, jdbcTemplate);
				}else{
					generalidades = getGeneralidadesTabelaPreco(
							montaSqlSubGeneralidadesTab(), idTabelaPreco,
							dsSimbolo, configuracoesFacade, jdbcTemplate);
				}
				parametersReport
						.put(KEY_PARAMETER_GENERALIDADES, generalidades);
				
			}
			
			generalidadesDificuldadeEntrega = TabelasClienteUtil
					.getDificuldadeEntrega(idParametro, idTabelaPreco,
							isTabelaNova, jdbcTemplate);
		
			aplicaGeneralidade(generalidadesDificuldadeEntrega, isTabelaNova,
					jdbcTemplate, idParametro, configuracoesFacade, dsSimbolo);
		
			parametersReport.put(KEY_PARAMETER_DIFICULDADE_ENTREGA,
					generalidadesDificuldadeEntrega);
		
			/*if(containsSubReport(SUBREPORT_SERVICOSCONTRATADOS,subRelatorios)){
				servicosContratados = getSubServicoAdicionalContratado(
						idTabelaPreco, idDivisao, idSimulacao, dsSimbolo,
						configuracoesFacade, jdbcTemplate);
				
				parametersReport.put(KEY_PARAMETER_SERVICOSCONTRATADOS,
						servicosContratados);
			}*/
			
			if (containsSubReport(SUBREPORT_SERVICOSNAOCONTRATADOS,
					subRelatorios)) {
				List listaServicosNaoContr = TabelasClienteUtil
						.getSubServicoAdicionalNaoContratado(idDivisao,
								idTabelaPreco, dsSimbolo, configuracoesFacade,
								jdbcTemplate, idParametro);
				parametersReport.put(KEY_PARAMETER_SERVICOSADICIONAIS,
						listaServicosNaoContr);
			}
			
			parametersReport.put(KEY_PARAMETER_PAGEFOOTER,
					montaPageFooter(generalidades));
			
			Map mapFormalidade = new HashMap();
			if (!formalidades.isEmpty())
				mapFormalidade = (Map) formalidades.get(0);
		}
		return parametersReport;
	}

	public static List<Map> getGeneralidadesTabelaPrecoDificuldadeEntrega(
			Long idTabelaPreco, JdbcTemplate jdbcTemplate) {
		List<Map> generalidadesDificuldadeEntrega = jdbcTemplate.queryForList(
				montaSqlSubGeneralidadesDificuldadeEntrega(),
				new Long[] { idTabelaPreco });
		return generalidadesDificuldadeEntrega;
	}

	public static List<Map> getDificuldadeEntrega(Long idParametro,
			Long idTabelaPreco, Boolean isTabelaNova, JdbcTemplate jdbcTemplate) {
		List<Map> generalidadesDificuldadeEntrega;
		if(isTabelaNova){
			generalidadesDificuldadeEntrega = jdbcTemplate.queryForList(
					montaSqlGeneralidadesRotaDificuldadeEntrega(), new Long[] {
							idTabelaPreco, idParametro, idTabelaPreco,
							idParametro });
		}else{
			generalidadesDificuldadeEntrega = jdbcTemplate.queryForList(
					montaSqlGeneralidadesDificuldadeEntrega(), new Long[] {
							idTabelaPreco, idParametro });
		}
		return generalidadesDificuldadeEntrega;
	}

	/**
	 * 
	 * @param report
	 * @param array
	 * @return
	 */
	private static boolean containsSubReport(int report, int[] array){
		for (int i = 0; i < array.length; i++) {
			if (array[i] == report)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param idTabelaPreco
	 * @param idCliente
	 * @param idDivisao
	 * @param idSimulacao
	 * @param tpModal
	 * @param abrangencia
	 * @param jdbcTemplate
	 * @return
	 */
	private static List getSubRelFormalidades(Long idTabelaPreco,
			Long idCliente, Long idDivisao, Long idSimulacao, char tpModal,
			char abrangencia, JdbcTemplate jdbcTemplate) {
		
		//Executa o subRelatorio de formalidades da tabela de preco
		List listaFormalidadesDefault = getSubRelFormalidades(idTabelaPreco,
				jdbcTemplate);

		//executa a query de formalidades do cliente
		String tipoModal = "" + tpModal;
		String abrang = "" + abrangencia;
		
		List listaFormalidades = new ArrayList();
		if(idCliente!=null){
			listaFormalidades = jdbcTemplate.queryForList(
					montaSqlFormalidadesCliente(), new Object[] { idDivisao,
							tipoModal, abrang, idDivisao, tipoModal, abrang,
							idDivisao, idDivisao, idCliente, idDivisao });
		}

		String tpPeriodicidade = null;
		String nrPrazoPagamento = null;
		BigDecimal pcJuroCliente = null;
		BigDecimal pcDescontoFreteTotal = null;
		String atualizacao = null;
		for (Iterator i = listaFormalidades.iterator();i.hasNext();) {
			Map map = (Map)i.next();
			tpPeriodicidade = MapUtils.getString(map,"DS_VALOR_DOMINIO");
			nrPrazoPagamento = MapUtils.getString(map,"NR_PRAZO_PAGAMENTO");
			atualizacao = MapUtils.getString(map,"ATUALIZACAO");
			pcJuroCliente = MapUtilsPlus
					.getBigDecimal(map, "JURO_DIARIO", null);
			pcDescontoFreteTotal = MapUtilsPlus.getBigDecimal(map,
					"PC_DESCONTO_FRETE_TOTAL");
		}
		Map divisao = new HashMap();
		if(idDivisao!=null) {
			//query que busca o nome da divisao da formalidade do cliente
			String sqlDivisao = "SELECT DS_DIVISAO_CLIENTE AS DIVISAO FROM DIVISAO_CLIENTE WHERE ID_DIVISAO_CLIENTE = ?";
			List divisaoList = jdbcTemplate.queryForList(sqlDivisao,
					new Long[] { idDivisao });
			if (divisaoList!=null && divisaoList.size()>=1) {
				divisao = (Map)divisaoList.get(0);
			}
		}

		if (!listaFormalidadesDefault.isEmpty()) {
		Map map = (Map) listaFormalidadesDefault.get(0);
		//se nao tiver periodicidade é Diario por default
		if(tpPeriodicidade != null){
			map.put("vlFrqFaturamento", tpPeriodicidade);
		}else{
				String tpPeriodicidadeDefault = MapUtils.getString(map,
						"DS_VALOR_DOMINIO", "Diário");
			map.put("vlFrqFaturamento", tpPeriodicidadeDefault);
		}
			// seta se tiver prazo de pagamento do cliente, senão já está setado
			// prazo da filial
		if(nrPrazoPagamento != null){
			map.put("NR_PRAZO_COBRANCA",nrPrazoPagamento);
		}

		if(atualizacao != null){
			map.put("TP_REAJUSTE", atualizacao);
		}else{
			map.put("TP_REAJUSTE", "Automático");
		}

		if(BigDecimalUtils.hasValue(pcDescontoFreteTotal)){
				map.put("PC_DESCONTO_FRETE_TOTAL", formatDuasCasasDecimais(pcDescontoFreteTotal));
		}

			map.put("NM_DIVISAO", divisao.get("DIVISAO"));
			
			Map tabelaDivisaoCliente = new HashMap();
			StringBuilder query = new StringBuilder();
			query.append(" SELECT TDC.NR_FATOR_CUBAGEM, CL.TP_PESO_CALCULO "); 
			query.append(" FROM TABELA_DIVISAO_CLIENTE TDC ");
			query.append(" INNER JOIN DIVISAO_CLIENTE DC on DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE ");
			query.append(" INNER JOIN CLIENTE CL on CL.ID_CLIENTE = DC.ID_CLIENTE ");

			query.append(" WHERE TDC.ID_TABELA_PRECO = ? ");
			query.append(" AND TDC.ID_DIVISAO_CLIENTE = ? ");
			
			List tabelaDivisaoClienteList = jdbcTemplate.queryForList(
					query.toString(), new Long[] { idTabelaPreco, idDivisao });
			if (tabelaDivisaoClienteList != null
					&& tabelaDivisaoClienteList.size() >= 1) {
				tabelaDivisaoCliente = (Map)tabelaDivisaoClienteList.get(0);
			}
			
			if (tabelaDivisaoCliente.get("NR_FATOR_CUBAGEM") != null
					&& ((BigDecimal) tabelaDivisaoCliente
							.get("NR_FATOR_CUBAGEM")).doubleValue() != 0d) {
				map.put("CARGAS_VOLUMOSAS",
						tabelaDivisaoCliente.get("NR_FATOR_CUBAGEM").toString()
								+ " kg/m3");
			}else if (tabelaDivisaoCliente.get("TP_PESO_CALCULO") != null 
					&& (tabelaDivisaoCliente.get("TP_PESO_CALCULO").toString()
							.equals("F") || tabelaDivisaoCliente
							.get("TP_PESO_CALCULO").toString().equals("D"))) {
				map.put("CARGAS_VOLUMOSAS", "SUSPENSO");
			} else {
				String nmParametro = null;
				if ("R".equals(tipoModal)){
					// se for rodoviario, busca o valor padrao no parametro
					// geral
					// rodoviario (FATOR_CUBAGEM_PADRAO_RODO)
					nmParametro = "FATOR_CUBAGEM_PADRAO_RODO";
				}else{
					// se for aereo, busca o valor padrao no parametro geral
					// aereo
					// (FATOR_CUBAGEM_PADRAO_RODO)
					nmParametro = "FATOR_CUBAGEM_PADRAO_AEREO";
			}
				String valorParametro = getValorParametro(nmParametro,
						jdbcTemplate);
				if (valorParametro != null && valorParametro.length() >0){
					map.put("CARGAS_VOLUMOSAS", valorParametro + " kg/m3");
				}
			}

		/* formalidade especial da simulação */
		if(idSimulacao != null){
			StringBuffer sqlSimulacao = new StringBuffer();
				sqlSimulacao
						.append("select ")
						.append(PropertyVarcharI18nProjection.createProjection(
								"VD.DS_VALOR_DOMINIO_I", "periodicidade"))
				.append(", s.DT_VALIDADE_PROPOSTA as data, s.NR_DIAS_PRAZO_PAGAMENTO as NR_PRAZO_COBRANCA, s.NR_FATOR_CUBAGEM as NR_FATOR_CUBAGEM, cl.tp_peso_calculo as TP_PESO_CALCULO ");
				sqlSimulacao
						.append(" from simulacao s, VALOR_DOMINIO VD, DOMINIO D, CLIENTE CL");
				sqlSimulacao
						.append(" where s.id_simulacao = ? and VD.VL_VALOR_DOMINIO = s.TP_PERIODICIDADE_FATURAMENTO and D.NM_DOMINIO = 'DM_PERIODICIDADE_FATURAMENTO' and s.id_cliente = cl.id_cliente");
				sqlSimulacao
						.append("   and VD.ID_DOMINIO = D.ID_DOMINIO and rownum <= 1");

			Map simulacao = new HashMap();
				List simulacaoList = jdbcTemplate.queryForList(
						sqlSimulacao.toString(), new Long[] { idSimulacao });
			if (simulacaoList.size()>=1) {
				simulacao = (Map)simulacaoList.get(0);
				map.put("vlFrqFaturamento",   simulacao.get("periodicidade"));
				map.put("dtValidadeProposta", simulacao.get("data"));

				if(simulacao.get("NR_PRAZO_COBRANCA")!= null)
						map.put("NR_PRAZO_COBRANCA",
								simulacao.get("NR_PRAZO_COBRANCA").toString());
				
					if (simulacao.get("NR_FATOR_CUBAGEM") != null
							&& ((BigDecimal) simulacao.get("NR_FATOR_CUBAGEM"))
									.doubleValue() != 0d) {
						map.put("CARGAS_VOLUMOSAS",
								simulacao.get("NR_FATOR_CUBAGEM").toString()
										+ " kg/m3");
				} else if (simulacao.get("TP_PESO_CALCULO") != null 
							&& (simulacao.get("TP_PESO_CALCULO").toString()
									.equals("F") || simulacao
									.get("TP_PESO_CALCULO").toString()
									.equals("D"))) {
						map.put("CARGAS_VOLUMOSAS", "SUSPENSO");
				} else {
					String nmParametro = null;
					if ("R".equals(tipoModal)){
							// se for rodoviario, busca o valor padrao no
							// parametro
							// geral rodoviario (FATOR_CUBAGEM_PADRAO_RODO)
						nmParametro = "FATOR_CUBAGEM_PADRAO_RODO";
					}else{
							// se for aereo, busca o valor padrao no parametro
							// geral
							// aereo (FATOR_CUBAGEM_PADRAO_RODO)
						nmParametro = "FATOR_CUBAGEM_PADRAO_AEREO";
			}
						String valorParametro = getValorParametro(nmParametro,
								jdbcTemplate);
						if (valorParametro != null
								&& valorParametro.length() > 0) {
							map.put("CARGAS_VOLUMOSAS", valorParametro
									+ " kg/m3");
		}
				}
			}
		}
		}
		return listaFormalidadesDefault;
	}

	private static String getValorParametro(String nmParametro,
			JdbcTemplate jdbcTemplate) {
		String query = "select DS_CONTEUDO from parametro_geral where NM_PARAMETRO_GERAL = '"
				+ nmParametro + "'";
		List list = jdbcTemplate.queryForList(query);
		if (list != null && list.size() > 0){
			Map map = (Map)list.get(0);
			return (String) map.get("DS_CONTEUDO");
		}
		return null;
		
	}
	
	/**
	 * Recebe o id da Tabela de Preco e retorna uma lista contendo os dados do
	 * subrelatorio de Formalidades da Tabela de Preco
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelFormalidades(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		//Pega a filial da sessao
		Filial filial = SessionUtils.getFilialSessao();
		if(idTabelaPreco == null){
			return new ArrayList();
		}
		List listaFormalidades = jdbcTemplate.queryForList(
				montaSqlFormalidades(), new Long[] { idTabelaPreco });
		Map map = (Map) listaFormalidades.get(0);
		String nr_prazo_cobranca = "0"; 
		if(filial.getNrPrazoCobranca() != null){
			nr_prazo_cobranca = filial.getNrPrazoCobranca().toString();
		}
		map.put("NR_PRAZO_COBRANCA", nr_prazo_cobranca);
		
		//Caso contrário: FILIAL.PC_JURO_DIARIO (do usuário logado) + “ ao dia”
		if(map.get("PC_JURO_DIARIO") == null){
			map.put("PC_JURO_DIARIO", filial.getPcJuroDiario().toString());
		}
		
		//Caso a versao seja um numero de 0 a 9, concatena 0 a ele
		String versao = MapUtils.getString(map,"NR_VERSAO","");
		if(versao.length() == 1){
			versao = "0".concat(versao);
		}

		String tipoTabela = MapUtils.getString(map, "TP_TIPO_TABELA_PRECO")
				+ versao + "-"
				+ MapUtils.getString(map, "TP_SUBTIPO_TABELA_PRECO") + " - "
				+ MapUtils.getString(map, "DS_DESCRICAO");
		map.put("TIPO_TABELA", tipoTabela);

		return listaFormalidades;
	}
	
	/**
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelFormalidadesAereo(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		
		if (idTabelaPreco == null)
			return null;
		
		return jdbcTemplate.queryForList(montaSqlFormalidadesAereo(),
				new Long[] { idTabelaPreco });
	}
	
	/**
	 * 
	 * @param idTabelaPreco
	 * @param idParametro
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelTaxaTerrestre(Long idTabelaPreco,
			Long idParametro, JdbcTemplate jdbcTemplate) {
		
		List result = null;
		if(idParametro ==  null){
			result = jdbcTemplate.queryForList(montaSQLTaxaTerrestre(),
					new Long[] { idTabelaPreco });
		}else{
			result = jdbcTemplate.queryForList(montaSQLTaxaTerrestreCliente(),
					new Long[] { idTabelaPreco, idParametro });
		}

		if (result.isEmpty())
			return null;
		
		String moeda = getMoeda(idTabelaPreco, jdbcTemplate);
		List listTaxTerr = new ArrayList();
		Map mapLayout = new HashMap();
		
		BigDecimal valorFaixaOld = null, valorFaixaLast = null;
		for(int i=0; i<result.size(); i++){
			Map map = (Map)result.get(i);
			BigDecimal valorFaixa = (BigDecimal) map
					.get("VL_FAIXA_PROGRESSIVA");
			BigDecimal valorTaxa  = (BigDecimal)map.get("VL_FIXO");	
			
			String tpIndicador = MapUtilsPlus.getString(map,
					"TP_TAXA_INDICADOR");
			if (tpIndicador != null) {// parametro do cliente pode não ter taxa
										// terrestre
				BigDecimal vlTaxaCliente = MapUtilsPlus.getBigDecimal(map,
						"VL_TAXA", BigDecimal.ZERO);
				valorTaxa = TabelasClienteUtil.calculaPercentagem(tpIndicador,
						vlTaxaCliente, valorTaxa);
			}
			
			String strValorTaxa = formatDuasCasasDecimais(valorTaxa);
			
			if(i==0){
				mapLayout.put("VL_FAIXA_LAYOUT1", valorFaixa.toString());
				mapLayout.put("VL_TAXA_LAYOUT1", strValorTaxa);
				mapLayout.put("DS_SIMBOLO", moeda);
			} else if ((i + 1) == result.size()) {
				if(result.size()>2){
					mapLayout = new HashMap();
				}
				mapLayout.put("VL_FAIXA_LAYOUT3", valorFaixaLast.toString());
				mapLayout.put("VL_TAXA_LAYOUT3", strValorTaxa);
				mapLayout.put("DS_SIMBOLO", moeda);
				listTaxTerr.add(mapLayout);
			} else {
				if(result.size()>2 && i>1){
					mapLayout = new HashMap();
				}
				mapLayout.put("VL_EXCEDENTE_LAYOUT2", strValorTaxa);
				mapLayout.put("VL_FAIXA_INI_LAYOUT2", valorFaixaOld.toString());
				mapLayout.put("VL_FAIXA_FIM_LAYOUT2", valorFaixa.toString());
				mapLayout.put("DS_SIMBOLO", moeda);
				listTaxTerr.add(mapLayout);
			}
			valorFaixaOld = valorFaixa.add(new BigDecimal(1));
			valorFaixaLast = valorFaixa;
		}
		
		return listTaxTerr;
	}
	
	/**
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return
	 */
	public static List getSubRelTaxaTerrestre(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {

		return getSubRelTaxaTerrestre(idTabelaPreco, null, jdbcTemplate);
	}

	/**
	 * 
	 * @param idTabelaPreco
	 * @param idParametro
	 * @param jdbcTemplate
	 * @return
	 */
	public static Map getSubRelTaxaCombustivel(Long idTabelaPreco,
			Long idParametro, JdbcTemplate jdbcTemplate) {

		List result = null;
		List newResult = new ArrayList();
		
		if(idParametro==null){
			result = jdbcTemplate.queryForList(montaSQLTaxaCombustivel(),
					new Long[] { idTabelaPreco });
		}else{
			result = jdbcTemplate.queryForList(
					montaSQLTaxaCombustivelCliente(), new Long[] {
							idTabelaPreco, idParametro });
		}
		
		SortedMap mapDestinos = (SortedMap) getOrderedDestinos(result, 1); // colunas
																			// da
																			// tabela
		
		SortedMap mapOrigens = new TreeMap();

		// montagem da tabela
		String origem = null, destino = null, tpIndicador = null;
		BigDecimal vlTaxa = null, vlTaxaCliente = null;
		
		for (Iterator it = result.iterator(); it.hasNext();) {
			Map mapRec = (Map) it.next();
			origem = MapUtils.getString(mapRec, "ORIGEM");
			destino = MapUtils.getString(mapRec, "DESTINO");
			vlTaxa = MapUtilsPlus.getBigDecimal(mapRec, "VL_FIXO",
					BigDecimal.ZERO);
			
			tpIndicador = MapUtilsPlus.getString(mapRec, "TP_TAXA_INDICADOR");
			if (tpIndicador != null) {// parametro do cliente pode não ter taxa
										// combustível
				vlTaxaCliente = MapUtilsPlus.getBigDecimal(mapRec, "VL_TAXA",
						BigDecimal.ZERO);
				vlTaxa = TabelasClienteUtil.calculaPercentagem(tpIndicador,
						vlTaxaCliente, vlTaxa);
			}

			Integer intDestino = (Integer) mapDestinos.get(destino);
			String colDestino = "COLUMN" + (intDestino.intValue() + 1);

			SortedMap mapRecOrig = (SortedMap) MapUtilsPlus.getMap(mapOrigens,
					origem, null);
			if (mapRecOrig == null) {
				mapRecOrig = new TreeMap();
				mapRecOrig.put("ORIGEM", origem);
			}
			
			mapRecOrig.put(colDestino,
					formatDuasCasasDecimais(vlTaxa));
			mapOrigens.put(origem, mapRecOrig);
		}
		
		SortedMap resultMap = new TreeMap();
		Set subCrossTab = mapOrigens.keySet();
		for (Iterator it = subCrossTab.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Map mapRec = (Map) mapOrigens.get(key);
			newResult.add(mapRec);
		}
		
		resultMap.put("RESULT", newResult);//linhas
		resultMap.put("SUBCROSSTAB", mapDestinos.keySet());//colunas

		return resultMap;
	}
	
	/**
	 * 
	 * @param idTabelaPreco
	 * @param jdbcTemplate
	 * @return
	 */
	public static Map getSubRelTaxaCombustivel(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {

		return getSubRelTaxaCombustivel(idTabelaPreco, null, jdbcTemplate);
	}
	
	/**
	 * 
	 * @param dados
	 * @param by
	 *            0-chave ordem cardinal e conteudo destino 1-chave destino e
	 *            conteudo ordem cardinal
	 * @return
	 */
	private static Map getOrderedDestinos(List dados, int byKey) {
		if (byKey < 0 || byKey > 1)
			return null;
		
		SortedMap mapDestino = new TreeMap();
		
		Set destinos = new TreeSet(new Comparator() {
			public int compare(Object o1, Object o2) {
			
				if (o1 != null && o2 != null) {
					return o1.toString().compareTo(o2.toString());
				}				
				return -1;
			}			
		});
		//insere no set ordenado
		for (Iterator it = dados.iterator(); it.hasNext();) {
			Map map = (Map)it.next();
			destinos.add(((String)map.get("DESTINO")));
		}
		
		List listDestinos = new ArrayList();
		listDestinos.addAll(destinos);
		
		int pos = 0;
		for(Iterator it = listDestinos.iterator(); it.hasNext();){
			
			String sgDestino = (String) it.next();
			if(byKey==0){
				mapDestino.put(Integer.valueOf(pos), sgDestino);
			}else{
				mapDestino.put(sgDestino, Integer.valueOf(pos));
			}
			pos++;
		}
		return mapDestino;
	}
	
	/**
	 * @param idTabelaPreco
	 * @param idDivisao
	 * @param dsSimbolo
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @param idParametro 
	 * @return
	 */
	public static List getSubServicoAdicionalContratado(Long idTabelaPreco,
			Long idDivisao, Long idSimulacao, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate) {
		// busca os servicos contratados de acordo com a divisao e a tabela de
		// preco
		Long[] params = new Long[2];
		params[0] = LongUtils.hasValue(idSimulacao) ? idSimulacao : idDivisao;
		params[1] = idTabelaPreco;
		List servicoAdC = jdbcTemplate.queryForList(
				montaSqlServicoContratado(idSimulacao), params);
		List listToReturn = new ArrayList();
		Map mapLocal = (Map) threadLocal.get();
		
		for (Iterator i = servicoAdC.iterator(); i.hasNext();) {
			Map map = (Map) i.next();

			String cdParcelaPreco = MapUtils.getString(map,"CD_PARCELA_PRECO");
			String nomeParcela = MapUtils.getString(map,"NM_PARCELA_PRECO");
			String tpIndicador = MapUtils.getString(map,"TP_INDICADOR","");
			BigDecimal sacValor = MapUtilsPlus.getBigDecimal(map, "SAC_VALOR",
					BigDecimal.ZERO);
			BigDecimal vsaValor = MapUtilsPlus.getBigDecimal(map, "VSA_VALOR",
					BigDecimal.ZERO);
			BigDecimal vlMinimo = MapUtilsPlus.getBigDecimal(map, "SAC_MINIMO",
					BigDecimal.ZERO);
			String nrQuantidadeDias = MapUtils.getString(map,
					"NR_QUANTIDADE_DIAS");
			String sacValorFormatado = formatDuasCasasDecimais(sacValor);
			String vsaValorFormatado = formatDuasCasasDecimais(vsaValor);

			String valor = dsSimbolo+" ";
			if(TABELA.equals(tpIndicador)){
				valor = vsaValorFormatado;
			}else if(VALOR.equals(tpIndicador)){
				valor = sacValorFormatado;
			}else if(DESCONTO.equals(tpIndicador)){
				BigDecimal desconto = calculaPercentagem(tpIndicador, sacValor,
						vsaValor);
				valor = formatDuasCasasDecimais(desconto);
			}else if(ACRESCIMO.equals(tpIndicador)){
				BigDecimal acrecimo = calculaPercentagem(tpIndicador, sacValor,
						vsaValor);
				valor = formatDuasCasasDecimais(acrecimo);
			}

			/** Servicos adicionais NÃO-CONTRATADOS */
			if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)){
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relPorToneladaFracao");
			} else if (ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO
					.equals(cdParcelaPreco)) {
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorEvento");
			/** Lista serviços que SEJAM do tipo ESTADIA */
			} else if (ConstantesExpedicao.TIPOS_ESTADIA
					.contains(cdParcelaPreco)) {
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relPorQuantidadeDias");
			} else if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)){
				if( mapLocal != null){
					String value = dsSimbolo + " " + mapLocal.get("Valor")
							+ " "
							+ configuracoesFacade.getMensagem("relEscolta");
					Map mapTmp = new HashMap();
					mapTmp.put("NOME", mapLocal.get("Nome")+":");
					threadLocal.remove();
					mapLocal = null;
					mapTmp.put("VALOR", value);
					listToReturn.add(mapTmp);
				}
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relEscolta");
			} else if (ConstantesExpedicao.CD_SEGURANCA_ADICIONAL
					.equals(cdParcelaPreco)) {
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relSegurancaAdicional");
			} else if (ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA
					.equals(cdParcelaPreco)) {
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relSobreValorMercadoria");
			} else if (ConstantesExpedicao.CD_PALETIZACAO
					.equals(cdParcelaPreco)) {
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorPallet");
			} else if(ConstantesExpedicao.CD_REEMBOLSO.equals(cdParcelaPreco)) {
				valor = valor
						+ configuracoesFacade
								.getMensagem("relSobreValorMercadoria");
				if(vlMinimo != null){
					valor += " "
							+ configuracoesFacade.getMensagem("relOuMinimo")
							+ " "
							+ dsSimbolo
							+ " "
							+ formatDuasCasasDecimais(vlMinimo);
				}
			} else if (ConstantesExpedicao.CD_AGENDAMENTO_COLETA
					.equals(cdParcelaPreco)
					|| ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA
							.equals(cdParcelaPreco)
					|| ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA_SAB_DOM_FER
							.equals(cdParcelaPreco)) {
				valor = valor
						+ configuracoesFacade.getMensagem("relValorFrete");
				if(vlMinimo != null){
					valor += " "
							+ configuracoesFacade.getMensagem("relOuMinimo")
							+ " "
							+ dsSimbolo
							+ " "
							+ formatDuasCasasDecimais(vlMinimo);
				}
			} else {
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorConhecimento");
			}

			Map mapTmp = new HashMap();
			mapTmp.put("NOME", nomeParcela+":");
			mapTmp.put("VALOR", valor);
			listToReturn.add(mapTmp);
		}

		return unionOrdering(listToReturn, "NOME", "VALOR");
	}

	public static List getSubServicoAdicionalNaoContratado(Long idDivisao,
			Long idTabelaPreco, String dsSimbolo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate,
			Long idParametro) {
		List<Map> listaServicosAd = jdbcTemplate.queryForList(
				montaSqlServicoNaoContratado(), new Long[] { idTabelaPreco,
						idDivisao, idTabelaPreco });
		return parametrizeServicosAdicionais(listaServicosAd, idTabelaPreco,
				jdbcTemplate, configuracoesFacade);
	}

	/**
	 * Método que insere as colunas de uma crosstab num map a ser setado no jr
	 * 
	 * @param nomeParametro
	 * @param crosstab
	 * @param parametersReport
	 * @return
	 */
	public static Map montaCrosstabParameters(String nomeParametro,
			Set crosstab, Map parametersReport) {
		int cont = 1;
		for(Iterator i = crosstab.iterator();i.hasNext();){
			String coluna = nomeParametro+cont;
			Object v = i.next();
			String valor = v != null ? v.toString() : null;
			parametersReport.put(coluna,valor);
			cont++;
		}
		return parametersReport;
	}

	/**
	 * Metodo chamado dentro do Jasper, para formatar os numeros com 2 ou 5
	 * casas decimais, dependendo do seu valor e do seu valor minimo
	 * 
	 * @param parametro
	 * @param psMinimo
	 * @param field
	 * @return
	 */
	public static String formataCasasDecimais(String parametro,
			BigDecimal psMinimo, BigDecimal field) {
		String valorFormatado = "";

		if(field == null)
			return valorFormatado; 

		if (!StringUtils.isNumeric(parametro)) { // parametro =
													// "Acima 'psMinimo'"
			valorFormatado = FormatUtils.formatDecimal(FORMATO_5_CASAS, field);
		} else {
			BigDecimal valor = new BigDecimal(parametro);
			if(field != null) {
				if(psMinimo != null){
					if(valor.doubleValue() <= psMinimo.doubleValue()) {
						valorFormatado = formatDuasCasasDecimais(field);
					} else {
						valorFormatado = FormatUtils.formatDecimal(
								FORMATO_5_CASAS, field);
					}
				} else {
					valorFormatado = formatDuasCasasDecimais(field);
				}
			}
		}
		return valorFormatado;
	}
	
	/**
	 * Formata texto das colunas dinamicas dos relatorios
	 * 
	 * @param parametro
	 * @param dsSimbolo
	 * @param psMinimo
	 * @return
	 */
	public static String formataColumnParameter(String parametro,
			String dsSimbolo, BigDecimal psMinimo) {
		String formatedValue = "";
		
		if(StringUtils.isNumeric(parametro)){
			if (Long.parseLong(parametro) <= psMinimo.longValue()) {
				formatedValue = parametro + " Kg\n(" + dsSimbolo + "/CTRC)";
			}else{
				formatedValue = parametro + " Kg\n(" + dsSimbolo + "/Kg)";
			}
		}else{
			formatedValue = parametro + " Kg\n(" + dsSimbolo + "/Kg)";
		}

		return formatedValue;
	}

	/**
	 * 
	 * @param listaUFs
	 * @param campo
	 * @param nomeTabela
	 * @param msgTodoEstado
	 * @param msgDemaisLocalidades
	 * @param jdbcTemplate
	 */
	public static void verificaUfs(List listaUFs, String campo,
			String nomeTabela, String msgTodoEstado,
			String msgDemaisLocalidades, JdbcTemplate jdbcTemplate) {
		String campoContrario = "ORIGEM".equals(campo) ? "DESTINO" : "ORIGEM";
		String ufContrario = null;
		for (Iterator iter = listaUFs.iterator(); iter.hasNext();) {
			String uf = (String) iter.next();
			String[] estados = uf.split("-");
			if(estados.length != 2){
				return;
			}
			uf = "ORIGEM".equals(campo) ? estados[0] : estados[1];
			ufContrario = !"ORIGEM".equals(campo) ? estados[0] : estados[1];
			String sql = "UPDATE " + nomeTabela + " SET " + campo + " = '" + uf
					+ " - " + msgDemaisLocalidades + "' WHERE " + campo
					+ " = '" + uf + " - " + msgTodoEstado + "' and "
					+ campoContrario + " like '" + ufContrario + "%' ";
			jdbcTemplate.update(sql);
		}
	}

	/**
	 * 
	 * @param idContato
	 * @return
	 */
	private static String montaSqlHeader(Long idContato) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT")
		.append("     P.NM_PESSOA as NM_FANTASIA, (")
				.append(PropertyVarcharI18nProjection
						.createProjection("TL.DS_TIPO_LOGRADOURO_I"))
		.append(" || ' ' || EP.DS_ENDERECO || ', ' || EP.NR_ENDERECO || ' - ' || EP.DS_BAIRRO) as ENDERECO,")
		.append("     EP.NR_CEP as CEP,")
		.append("     M.NM_MUNICIPIO as MUNICIPIO,")
		.append("     UF.SG_UNIDADE_FEDERATIVA as UNIDADEFEDERATIVA, ")
		.append("     CO.NM_CONTATO AS CONTATO,")
		.append("     P.TP_IDENTIFICACAO as TP_IDENTIFICACAO,")
		.append("     P.NR_IDENTIFICACAO as NR_IDENTIFICACAO,")
		.append("     I.NR_INSCRICAO_ESTADUAL as NUMERO,")
		.append("     I.BL_INDICADOR_PADRAO as INDICADOR ")
				.append("FROM CLIENTE C, ").append("     PESSOA P, ")
		.append("     ENDERECO_PESSOA EP, ")
		.append("     CONTATO CO, ")
		.append("     INSCRICAO_ESTADUAL I, ")
		.append("     MUNICIPIO M, ")
		.append("     UNIDADE_FEDERATIVA UF, ")
				.append("     TIPO_LOGRADOURO TL ").append("WHERE")
		.append("     C.ID_CLIENTE = ?");
		if(idContato != null) {
			sql.append("	AND CO.ID_CONTATO = ").append(idContato);
		}
		sql.append("  AND C.ID_CLIENTE = P.ID_PESSOA")
		.append("     AND P.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA (+)")
		.append("     AND EP.ID_MUNICIPIO = M.ID_MUNICIPIO (+)")
		.append("     AND M.ID_UNIDADE_FEDERATIVA = UF.ID_UNIDADE_FEDERATIVA (+)")
		.append("     AND EP.ID_TIPO_LOGRADOURO = TL.ID_TIPO_LOGRADOURO (+)")
		.append("     AND P.ID_PESSOA = CO.ID_PESSOA (+)")
		.append("     AND P.ID_PESSOA = I.ID_PESSOA (+)")
		.append("	  AND I.BL_INDICADOR_PADRAO = 'S' ");

		return sql.toString();
	}

	private static String montaSqlCidade(){
		StringBuffer sql = new StringBuffer();
		sql.append("select nm_municipio ");
		sql.append("from filial f, pessoa p, endereco_pessoa ep, municipio m ");
		sql.append("where f.id_filial = ?");
		sql.append("	and f.id_filial = p.id_pessoa ");
		sql.append("	and p.id_pessoa = ep.Id_pessoa ");
		sql.append("	and ep.id_municipio = m.id_municipio ");
		sql.append("    AND rownum <= 1 ");
		return sql.toString();
	}

	private static String montaSqlTelefone(){
		StringBuffer sql = new StringBuffer();
 
		sql.append("SELECT")
		.append("     TE.NR_DDD || TE.NR_TELEFONE as TELEFONE ")
				.append("FROM PESSOA P, ").append("     TELEFONE_ENDERECO TE ")
		.append("WHERE P.ID_PESSOA = ?")
		.append("  AND P.ID_PESSOA  = TE.ID_PESSOA")
		.append("  AND (TE.TP_USO in ('FF','FO') )")
		.append("  AND rownum <= 1 ");

		return sql.toString();
	}

	private static String montaSqlFax(){
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT").append("     TE.NR_DDD || TE.NR_TELEFONE as FAX ")
				.append("FROM PESSOA P, ").append("     TELEFONE_ENDERECO TE ")
		.append("WHERE P.ID_PESSOA = ?")
		.append("  AND P.ID_PESSOA = TE.ID_PESSOA")
		.append("  AND (TE.TP_USO in ('FF','FA') )")
		.append("  AND rownum <= 1 ");

		return sql.toString();
	}

	private static String montaSqlFormalidades(){
		StringBuffer sqlFormalidades = new StringBuffer();
		sqlFormalidades
				.append("SELECT TTP.ID_TIPO_TABELA_PRECO, TTP.TP_TIPO_TABELA_PRECO, ");
		sqlFormalidades
				.append("TTP.NR_VERSAO, STP.TP_SUBTIPO_TABELA_PRECO, TP.DT_VIGENCIA_INICIAL, TP.DS_DESCRICAO ");
		sqlFormalidades.append("FROM TABELA_PRECO TP ");
		sqlFormalidades
				.append("JOIN TIPO_TABELA_PRECO TTP ON TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO ");
		sqlFormalidades
				.append("JOIN SUBTIPO_TABELA_PRECO STP ON TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO ");
		sqlFormalidades.append("WHERE ID_TABELA_PRECO = ?");

		return sqlFormalidades.toString();		
	}
	
	private static String montaSqlFormalidadesAereo(){
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select TTP.TP_TIPO_TABELA_PRECO || TTP.NR_VERSAO || '-' || STP.TP_SUBTIPO_TABELA_PRECO || ' - ' || TP.DS_DESCRICAO AS TABELA ")
	       .append(" FROM TABELA_PRECO TP, TIPO_TABELA_PRECO TTP, SUBTIPO_TABELA_PRECO STP ")
	       .append(" WHERE ")
	       .append(" TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO AND ")
	       .append(" STP.ID_SUBTIPO_TABELA_PRECO = TP. ID_SUBTIPO_TABELA_PRECO AND TP.ID_TABELA_PRECO = ? ");
		
		return sql.toString();
	}

	private static String montaSqlFormalidadesCliente(){
		StringBuffer sql = new StringBuffer()
			.append(" SELECT ")
			.append("	DV.NR_PRAZO_PAGAMENTO,")
			.append("   C.PC_JURO_DIARIO as JURO_DIARIO, ")
			.append("   TDC.BL_ATUALIZACAO_AUTOMATICA as ATUALIZACAO, ")
			.append("   PC.PC_DESCONTO_FRETE_TOTAL, ")
				.append("   ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"VALOR_DOMINIO.DS_VALOR_DOMINIO_I", "DS_VALOR_DOMINIO"))
			.append(" FROM ")
			.append("   CLIENTE C, ")
			.append("   DIVISAO_CLIENTE DC, ")
			.append("   PARAMETRO_CLIENTE PC, ")
			.append(" 	(SELECT * FROM PRAZO_VENCIMENTO DV ")
			.append(" 	 WHERE DV.ID_DIVISAO_CLIENTE = ?  ")
			.append(" 	   AND DV.TP_MODAL = ? and DV.TP_ABRANGENCIA = ? ")
			.append(" 	   UNION ALL ")
			.append(" 	SELECT * FROM PRAZO_VENCIMENTO DV  ")
			.append(" 	WHERE DV.ID_DIVISAO_CLIENTE = ? ) DV, ")
			.append("(SELECT DF.ID_DIVISAO_CLIENTE, VD.DS_VALOR_DOMINIO_I ")
			.append(" FROM DIA_FATURAMENTO DF, VALOR_DOMINIO VD, DOMINIO D ")
			.append(" WHERE DF.TP_MODAL = ? ")
			.append("  AND DF.TP_ABRANGENCIA = ? ")
			.append("  AND DF.ID_DIVISAO_CLIENTE = ? ")
			.append("  AND VD.VL_VALOR_DOMINIO = DF.TP_PERIODICIDADE ")
			.append("  AND D.NM_DOMINIO = 'DM_PERIODICIDADE_FATURAMENTO' ")
			.append("  AND VD.ID_DOMINIO = D.ID_DOMINIO ")
			.append(" 	UNION ALL ")
			.append(" SELECT DF.ID_DIVISAO_CLIENTE, VD.DS_VALOR_DOMINIO_I")
			.append(" FROM DIA_FATURAMENTO DF, VALOR_DOMINIO VD, DOMINIO D ")
			.append(" WHERE DF.ID_DIVISAO_CLIENTE = ? ")
			.append("  AND VD.VL_VALOR_DOMINIO = DF.TP_PERIODICIDADE ")
			.append("  AND D.NM_DOMINIO = 'DM_PERIODICIDADE_FATURAMENTO' ")
			.append("  AND VD.ID_DOMINIO = D.ID_DOMINIO) VALOR_DOMINIO,")		
			.append("    TABELA_DIVISAO_CLIENTE TDC ")
			.append(" WHERE C.ID_CLIENTE = ? ")
			.append("	AND C.ID_CLIENTE = DC.ID_CLIENTE ")
			.append("	AND DC.ID_DIVISAO_CLIENTE = ? ")
			.append("	AND PC.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE ")
			.append("   AND DC.ID_DIVISAO_CLIENTE = VALOR_DOMINIO.ID_DIVISAO_CLIENTE (+) ")
			.append("	AND TDC.ID_DIVISAO_CLIENTE = DC.ID_DIVISAO_CLIENTE ")
			.append("	AND DC.ID_DIVISAO_CLIENTE = DV.ID_DIVISAO_CLIENTE (+)");

		return sql.toString();
	}

	private static String montaSqlGeneralidades(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ")
		.append("     (SELECT P.ID_PARCELA_PRECO,")
		.append("            P.CD_PARCELA_PRECO,")
		.append("	         P.TP_PARCELA_PRECO, ")
		.append("	         P.TP_INDICADOR_CALCULO AS TP_INDICADOR_CALCULO, ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO"))
				.append(" ,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
		.append("	         P.BL_EMBUTE_PARCELA,")
		.append("            T.ID_TABELA_PRECO,")
		.append("			 T.TP_CALCULO_PEDAGIO,")
		.append("		 	 T.PC_REAJUSTE,")
		.append("            STP.TP_SUBTIPO_TABELA_PRECO,")
		.append("            G.*")
		.append("       FROM TABELA_PRECO T,")
		.append("            TABELA_PRECO_PARCELA TT,")
		.append("            PARCELA_PRECO P,")
		.append("            GENERALIDADE g,")
		.append("            SUBTIPO_TABELA_PRECO STP")
		.append("       WHERE T.ID_TABELA_PRECO = TT.ID_TABELA_PRECO ")
		.append("         AND T.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO")
		.append("         AND TT.ID_PARCElA_PRECO = P.ID_PARCELA_PRECO")
		.append("         AND TT.ID_TABELA_PRECO_PARCELA = G.ID_generalidade")
		.append("         AND T.ID_TABELA_PRECO = ? ")
		.append("         AND P.CD_PARCELA_PRECO NOT IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde')) O full outer join")
		.append("     (SELECT P.ID_PARCELA_PRECO as ID_P_CLIENTE, P.CD_PARCELA_PRECO as CD_PARCELA,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA"))
				.append(" ,")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA"))
				.append(" ,")
		.append(" 			GC.VL_GENERALIDADE as VL_GENERALIDADE_CLIENTE,")
		.append(" 			GC.VL_MINIMO as VL_MINIMO_GEN,")
		.append(" 			GC.TP_INDICADOR_MINIMO as TP_INDICADOR_MINIMO_GEN,")
		.append("           GC.TP_INDICADOR, PC.*,")
		.append("		    P.TP_INDICADOR_CALCULO AS TP_CALCULO, ")
		.append("		    P.BL_EMBUTE_PARCELA AS BL_EMBUTE ")
		.append("        FROM GENERALIDADE_CLIENTE GC, ")
		.append("             PARCELA_PRECO P,")
		.append("             PARAMETRO_CLIENTE PC ")
		.append("       WHERE GC.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append("         AND PC.ID_PARAMETRO_CLIENTE = GC.ID_PARAMETRO_CLIENTE")
		.append("         AND PC.ID_PARAMETRO_CLIENTE = ? ")
		.append(" 		  AND P.CD_PARCELA_PRECO NOT IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
				.append(")P ").append(" ON ")
		.append("     O.ID_PARCELA_PRECO = P.ID_P_CLIENTE ")
		.append("ORDER BY  P.NR_ORDEM_EMISSAO ");
		return sql.toString();
	}

	/**
	 * SQL Alterado conforme DBA Ricardo Peres Pinheiro
	 * 
	 * @return
	 */
	private static String montaSqlGeneralidadesRota(){
		StringBuffer sql = new StringBuffer()
			.append(prepareSqlGeneralidadesRota())
			.append(" WHERE o.id_parcela_preco = p.id_p_cliente(+) ")
				.append(" UNION ").append(prepareSqlGeneralidadesRota())
			.append(" WHERE o.id_parcela_preco(+) = p.id_p_cliente ");
		return sql.toString();
	}

	private static String prepareSqlGeneralidadesRota(){
		StringBuilder sql = new StringBuilder()
			.append("SELECT * FROM ")
			.append("	(SELECT P.ID_PARCELA_PRECO,")
			.append("        P.CD_PARCELA_PRECO,")
			.append("		 P.TP_PARCELA_PRECO, ")
			.append("        P.TP_INDICADOR_CALCULO, ")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO"))
				.append(" ,")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"P.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
			.append("		 P.BL_EMBUTE_PARCELA AS BL_EMBUTE_PARCELA, ")
			.append("        T.ID_TABELA_PRECO,")
			.append("		 T.TP_CALCULO_PEDAGIO,")
			.append("		 T.PC_REAJUSTE,")
			.append("        STP.TP_SUBTIPO_TABELA_PRECO,")
			.append("        G.*")
			.append(" FROM TABELA_PRECO T, ")
			.append("	   TABELA_PRECO_PARCELA TT,")
			.append("      PARCELA_PRECO P,")
			.append("      GENERALIDADE g,")
			.append("      SUBTIPO_TABELA_PRECO STP")
			.append(" WHERE T.ID_TABELA_PRECO = TT.ID_TABELA_PRECO ")
			.append(" 	AND T.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO")
			.append("   AND TT.ID_PARCElA_PRECO = P.ID_PARCELA_PRECO")
			.append("   AND TT.ID_TABELA_PRECO_PARCELA = G.ID_generalidade")
			.append("   AND T.ID_TABELA_PRECO = ?")
			.append("   AND P.CD_PARCELA_PRECO NOT IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
			.append(" ORDER BY NM_PARCELA_PRECO)O,")
			.append("   (SELECT PP.ID_PARCELA_PRECO as ID_P_CLIENTE, PP.CD_PARCELA_PRECO as CD_PARCELA, ")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.DS_PARCELA_PRECO_I", "DS_PARCELA"))
				.append(" ,")
				.append("		 ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA"))
				.append(" ,")
			.append(" 		G.VL_GENERALIDADE as VL_GENERALIDADE_CLIENTE,")
			.append("      	G.TP_INDICADOR, PP.TP_INDICADOR_CALCULO AS TP_CALCULO, ")
			.append("      	PP.BL_EMBUTE_PARCELA AS BL_EMBUTE, P.* ")
			.append("    FROM PARAMETRO_CLIENTE P, ")
			.append("    	  GENERALIDADE_CLIENTE G, ")
			.append("    	  PARCELA_PRECO PP")
			.append("    WHERE P.ID_PARAMETRO_CLIENTE = ?")
			.append("      AND PP.CD_PARCELA_PRECO NOT IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ")
			.append("      AND P.ID_PARAMETRO_CLIENTE = g.ID_PARAMETRO_CLIENTE")
			.append("      AND G.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
			.append(" 	 ORDER BY NM_PARCELA)P ");
		return sql.toString();
	}

	private static String montaSqlGeneralidadesSub(Long idParametro){
		if(idParametro == null){
			return montaSqlGeneralidadesSub();
		}

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PG.DS_CONTEUDO AS REENTREGA, ")
		.append("  PG1.DS_CONTEUDO AS DEVOLUCAO, ")
		.append(" PARAMCLI.*")

		.append("FROM PARAMETRO_GERAL PG, ")
		.append("  PARAMETRO_GERAL PG1, ")

		.append("(select pcl.PC_COBRANCA_REENTREGA, pcl.PC_COBRANCA_DEVOLUCOES ")
				.append("  from parametro_cliente pcl where pcl.id_parametro_cliente = ")
				.append(idParametro).append(") PARAMCLI ")

				.append("WHERE PG.NM_PARAMETRO_GERAL = '")
				.append(ConstantesExpedicao.PC_COBRANCA_REENTREGA).append("'")
				.append("  AND PG1.NM_PARAMETRO_GERAL = '")
				.append(ConstantesExpedicao.PC_COBRANCA_DEVOLUCAO).append("'");
		return sql.toString();		
	}

	private static String montaSqlGeneralidadesSub(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PG.DS_CONTEUDO AS REENTREGA, ")
		.append("  PG1.DS_CONTEUDO AS DEVOLUCAO ")

		.append("FROM PARAMETRO_GERAL PG, ")
		.append("  PARAMETRO_GERAL PG1 ")

				.append("WHERE PG.NM_PARAMETRO_GERAL = '")
				.append(ConstantesExpedicao.PC_COBRANCA_REENTREGA).append("'")
				.append("  AND PG1.NM_PARAMETRO_GERAL = '")
				.append(ConstantesExpedicao.PC_COBRANCA_DEVOLUCAO).append("'");
		return sql.toString();
	}

	public static String montaSqlSubGeneralidadesTab() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT(TP.ID_TABELA_PRECO), ");
		sql.append("	PP.TP_PARCELA_PRECO,");
		sql.append("	PP.CD_PARCELA_PRECO,");
		sql.append("	PP.TP_INDICADOR_CALCULO,");
		sql.append("	TP.TP_CALCULO_PEDAGIO,");
		sql.append(
				PropertyVarcharI18nProjection.createProjection(
						"PP.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO")).append(
				",");
		sql.append(
				PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO")).append(
				",");
		sql.append("	PP.BL_EMBUTE_PARCELA,");
		sql.append("	STP.TP_SUBTIPO_TABELA_PRECO,");
		sql.append("	G.VL_MINIMO,");
		sql.append("	G.PS_MINIMO,");
		sql.append("	G.ID_GENERALIDADE,");
		sql.append("	G.VL_GENERALIDADE,");
		sql.append("	M.DS_SIMBOLO");

		sql.append(" FROM TABELA_PRECO TP ");
		sql.append("	JOIN TIPO_TABELA_PRECO TTP ON TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO ");
		sql.append("	JOIN SUBTIPO_TABELA_PRECO STP ON TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO ");
		sql.append("	JOIN TABELA_PRECO_PARCELA TPP ON TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO ");
		sql.append("	JOIN MOEDA M ON TP.ID_MOEDA = M.ID_MOEDA ");
		sql.append("	JOIN PARCELA_PRECO PP ON TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ");
		sql.append("	JOIN GENERALIDADE G ON TPP.ID_TABELA_PRECO_PARCELA = G.ID_GENERALIDADE ");

		sql.append(" WHERE TP.ID_TABELA_PRECO = ? ");
		sql.append(" AND PP.CD_PARCELA_PRECO NOT IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ");
		sql.append(" ORDER BY ").append(
				PropertyVarcharI18nProjection
						.createProjection("PP.DS_PARCELA_PRECO_I"));

		return sql.toString();
	}

	private static String montaSqlSubGeneralidadesDificuldadeEntrega(){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT(TP.ID_TABELA_PRECO), ");
		sql.append("	PP.TP_PARCELA_PRECO,");
		sql.append("	PP.CD_PARCELA_PRECO,");
		sql.append("	PP.TP_INDICADOR_CALCULO,");
		sql.append("	TP.TP_CALCULO_PEDAGIO,");
		sql.append(
				PropertyVarcharI18nProjection.createProjection(
						"PP.DS_PARCELA_PRECO_I", "DS_PARCELA_PRECO")).append(
				",");
		sql.append(
				PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO")).append(
				",");
		sql.append("	PP.BL_EMBUTE_PARCELA,");
		sql.append("	STP.TP_SUBTIPO_TABELA_PRECO,");
		sql.append("	G.VL_MINIMO,");
		sql.append("	G.PS_MINIMO,");
		sql.append("	G.ID_GENERALIDADE,");
		sql.append("	G.VL_GENERALIDADE,");
		sql.append("	TP.OB_TABELA_PRECO,");
		sql.append("	M.DS_SIMBOLO");

		sql.append(" FROM TABELA_PRECO TP ");
		sql.append("	JOIN TIPO_TABELA_PRECO TTP ON TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO ");
		sql.append("	JOIN SUBTIPO_TABELA_PRECO STP ON TP.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO ");
		sql.append("	JOIN TABELA_PRECO_PARCELA TPP ON TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO ");
		sql.append("	JOIN MOEDA M ON TP.ID_MOEDA = M.ID_MOEDA ");
		sql.append("	JOIN PARCELA_PRECO PP ON TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ");
		sql.append("	JOIN GENERALIDADE G ON TPP.ID_TABELA_PRECO_PARCELA = G.ID_GENERALIDADE ");

		sql.append(" WHERE TP.ID_TABELA_PRECO = ? ");
		sql.append(" AND PP.CD_PARCELA_PRECO IN('IDVeiculoDedicado','IDCustoDescarga','IDTaxaPaletizacao','IDAgendamentoColetaEntrega', 'IDTde') ");
		sql.append(" ORDER BY ").append(
				PropertyVarcharI18nProjection
						.createProjection("PP.DS_PARCELA_PRECO_I"));

		return sql.toString();
	}

	private static String montaSqlServicoContratado(Long idSimulacao){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
			.append("		PP.CD_PARCELA_PRECO,")
			.append("		PP.TP_INDICADOR_CALCULO,")
			.append("		SAC.TP_INDICADOR,")
			.append("		SAC.NR_QUANTIDADE_DIAS,")
			.append("		SAC.VL_VALOR as SAC_VALOR,")
			.append("		SAC.VL_MINIMO as SAC_MINIMO,")
			.append("		decode(PP.TP_PRECIFICACAO,'S',VSA.VL_SERVICO, FPERC.VL_FIXO) as VSA_VALOR,")
			.append("		decode(PP.TP_PRECIFICACAO,'S',VSA.VL_SERVICO, FVALOR.VL_FIXO) as VSA_MINIMO ")
			.append(" FROM");
		if(!LongUtils.hasValue(idSimulacao)) {
			sql.append("		CLIENTE C,").append("		DIVISAO_CLIENTE DC,")
			   .append("		TABELA_DIVISAO_CLIENTE TDC,");
		}
		sql.append("		TABELA_PRECO TP,")
			.append("		VALOR_SERVICO_ADICIONAL VSA,")
			.append("		SERVICO_ADICIONAL_CLIENTE SAC,")
			.append("		TABELA_PRECO_PARCELA TPP,")
			.append("		PARCELA_PRECO PP,")
			.append("		(select fp.ID_TABELA_PRECO_PARCELA, vfp.* from Faixa_Progressiva FP, valor_faixa_progressiva vfp where FP.tp_indicador_calculo = 'VL' and FP.id_faixa_progressiva = vfp.id_faixa_progressiva) FVALOR,")
			.append("		(select fp.ID_TABELA_PRECO_PARCELA, vfp.* from Faixa_Progressiva FP, valor_faixa_progressiva vfp where FP.tp_indicador_calculo = 'PC' and FP.id_faixa_progressiva = vfp.id_faixa_progressiva) FPERC")
			.append(" WHERE");
		if(!LongUtils.hasValue(idSimulacao)) {
			sql.append("		C.ID_CLIENTE = DC.ID_CLIENTE")
			   .append("        AND DC.ID_DIVISAO_CLIENTE = ?")
			   .append("		AND DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
			   .append("		AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO")
			   .append("		AND TDC.ID_TABELA_DIVISAO_CLIENTE = SAC.ID_TABELA_DIVISAO_CLIENTE");
		} else {
			sql.append("		SAC.ID_SIMULACAO = ?");
		}
		sql.append("		AND TP.ID_TABELA_PRECO = ?")
			.append("		AND TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO ")
			.append("		AND PP.ID_PARCELA_PRECO = TPP.ID_PARCELA_PRECO")
			.append("		AND PP.ID_PARCELA_PRECO = SAC.ID_PARCELA_PRECO ")
			.append("		AND TPP.ID_TABELA_PRECO_PARCELA = VSA.ID_VALOR_SERVICO_ADICIONAL (+)")
			.append("		AND PP.TP_PARCELA_PRECO = 'S'")
			.append("		AND PP.TP_PRECIFICACAO in ('S','M')")
			.append("		AND TPP.ID_TABELA_PRECO_PARCELA = FVALOR.ID_TABELA_PRECO_PARCELA (+)")
			.append("		AND TPP.ID_TABELA_PRECO_PARCELA = FPERC.ID_TABELA_PRECO_PARCELA (+)");
		return sql.toString();
	}

	private static String montaSqlServicoNaoContratado(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select ")
				.append(PropertyVarcharI18nProjection.createProjection(
						"PP.NM_PARCELA_PRECO_I", "NM_PARCELA_PRECO"))
				.append(" ,")
		.append("	PP.TP_INDICADOR_CALCULO,")
		.append("	PP.CD_PARCELA_PRECO,")
		.append("	decode(PP.TP_PRECIFICACAO,'S',VSA.VL_SERVICO, FPERC.VL_FIXO) AS VL_SERVICO, ")
		.append("	VSA.VL_MINIMO ")
		.append(" from")
		.append("	TABELA_PRECO TP,")
		.append("	VALOR_SERVICO_ADICIONAL VSA,")
		.append("	TABELA_PRECO_PARCELA TPP,")
		.append("	PARCELA_PRECO PP,")
		.append("	(select fp.ID_TABELA_PRECO_PARCELA, vfp.* from Faixa_Progressiva FP, valor_faixa_progressiva vfp where FP.tp_indicador_calculo = 'PC' and FP.id_faixa_progressiva = vfp.id_faixa_progressiva) FPERC ")
		.append("	where")
		.append("	TP.ID_TABELA_PRECO = ?")
		.append("	AND TP.ID_TABELA_PRECO = TPP.ID_TABELA_PRECO ")
		.append("	AND TPP.ID_TABELA_PRECO_PARCELA = VSA.ID_VALOR_SERVICO_ADICIONAL (+)")
		.append("	AND PP.ID_PARCELA_PRECO = TPP.ID_PARCELA_PRECO")
		.append("	AND TPP.ID_TABELA_PRECO_PARCELA = FPERC.ID_TABELA_PRECO_PARCELA (+)")
		.append("	AND PP.TP_PARCELA_PRECO = 'S'")
		.append("	AND PP.TP_PRECIFICACAO in('S','M')")
		.append("	AND NOT EXISTS(")
		.append("	select 1 from ")
		.append("		CLIENTE C1,")
		.append("		DIVISAO_CLIENTE DC1,")
		.append("		TABELA_DIVISAO_CLIENTE TDC1,")
		.append("		TABELA_PRECO TP1,")
		.append("		SERVICO_ADICIONAL_CLIENTE SAC1,")
		.append("		TABELA_PRECO_PARCELA TPP1")
		.append("	where")
		.append("		C1.ID_CLIENTE = DC1.ID_CLIENTE")
		.append("		AND DC1.ID_DIVISAO_CLIENTE = ?")
		.append("		AND DC1.ID_DIVISAO_CLIENTE = TDC1.ID_DIVISAO_CLIENTE")
		.append("		AND TDC1.ID_TABELA_PRECO = TP1.ID_TABELA_PRECO")
		.append("		AND TP1.ID_TABELA_PRECO = ?")
		.append("		AND TDC1.ID_TABELA_DIVISAO_CLIENTE = SAC1.ID_TABELA_DIVISAO_CLIENTE")
		.append("		AND SAC1.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO")
		.append("		)");
		return sql.toString();
	}

	private static String montaSqlParametrosCustom(){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT").append("       pc.TP_INDICADOR_PERCENTUAL_GRIS,")
			.append("       pc.VL_PERCENTUAL_GRIS,")
			.append("       pc.TP_INDICADOR_MINIMO_GRIS,")
			.append("       pc.VL_MINIMO_GRIS,")
			.append("       pc.TP_INDICADOR_PEDAGIO, ")
			.append("       pc.VL_PEDAGIO, ")
			.append("       pc.TP_INDICADOR_PERCENTUAL_TDE,")
			.append("       pc.VL_PERCENTUAL_TDE,")
			.append("       pc.TP_INDICADOR_MINIMO_TDE,")
			.append("       pc.VL_MINIMO_TDE,")
			.append("       pc.TP_INDICADOR_PERCENTUAL_TRT,")
			.append("       pc.VL_PERCENTUAL_TRT,")
			.append("       pc.TP_INDICADOR_MINIMO_TRT,")
			.append("       pc.VL_MINIMO_TRT,")
			.append("       pc.TP_INDICADOR_ADVALOREM,")
			.append("       pc.VL_ADVALOREM,")
			.append("       pc.TP_INDICADOR_ADVALOREM_2,")
			.append("       pc.VL_ADVALOREM_2")
			.append("  FROM PARAMETRO_CLIENTE pc")
			.append(" WHERE PC.ID_PARAMETRO_CLIENTE = ?");
		return sql.toString();
	}

	private static String montaSqlGrupos(List parametros, Long idSimulacao) {
		if (idSimulacao == null)
			return montaSqlGrupos(parametros);
	
		//Parametro de simulacao
		if (parametros != null && parametros.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
			.append(" (TC.VL_TAXA || TC.PS_MINIMO || TC.VL_EXCEDENTE || G.VL_GENERALIDADE ||")
			.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
			.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
			.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO) as BRAKEGROUP ")
			.append("  FROM PARAMETRO_CLIENTE PC, ")
			.append("       GENERALIDADE_CLIENTE G,")
			.append("       TAXA_CLIENTE TC, ")
			.append("       UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("       UNIDADE_FEDERATIVA UF_DESTINO ")
			.append(" WHERE PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
			.append("   AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			.append("   AND PC.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE (+)")
			.append("   AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)");

			StringBuffer idsParamCliente = new StringBuffer("");
			boolean primeiro = true;
			for (Iterator iter = parametros.iterator(); iter.hasNext();) {
				Long pc = (Long) iter.next();
				if (!primeiro)
					idsParamCliente.append(",");
				else
					primeiro = false;

				idsParamCliente.append(pc);
			}
			if (!idsParamCliente.toString().equalsIgnoreCase(""))
				sql.append(" AND PC.ID_PARAMETRO_CLIENTE in (")
						.append(idsParamCliente).append(")");

			sql.append(" ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP");

			return sql.toString();
		}
		return null;
	}
	
	/**
	 * Retorna string do sql para formar os agrupamentos
	 * 
	 * @param parametros
	 * @return
	 */
	private static String montaSqlGrupos(List parametros) {
		if (parametros != null && parametros.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct PC.ID_PARAMETRO_CLIENTE,")
			.append(" (TC.VL_TAXA || TC.PS_MINIMO || TC.VL_EXCEDENTE || G.VL_GENERALIDADE ||")
			.append(" G.TP_INDICADOR || PC.TP_INDICADOR_PERCENTUAL_GRIS || PC.VL_PERCENTUAL_GRIS ")
			.append(" || PC.TP_INDICADOR_MINIMO_GRIS || PC.VL_MINIMO_GRIS || PC.PC_COBRANCA_DEVOLUCOES ")
			.append(" || PC.PC_COBRANCA_REENTREGA || PC.VL_MIN_FRETE_PESO) as BRAKEGROUP ")
			.append("  FROM PARAMETRO_CLIENTE PC, ")
			.append("       DIVISAO_CLIENTE DC, ")
			.append("       GENERALIDADE_CLIENTE G,")
			.append("       TABELA_DIVISAO_CLIENTE TDC, ")
			.append("       TAXA_CLIENTE TC, ")
			.append("       UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("       UNIDADE_FEDERATIVA UF_DESTINO ")
			.append(" WHERE DC.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE")
			.append("   AND TDC.ID_TABELA_DIVISAO_CLIENTE = PC.ID_TABELA_DIVISAO_CLIENTE")
			.append("   AND PC.ID_PARAMETRO_CLIENTE = TC.ID_PARAMETRO_CLIENTE (+)")
			.append("   AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA")
			.append("   AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA")
			.append("   AND PC.ID_PARAMETRO_CLIENTE = G.ID_PARAMETRO_CLIENTE (+)")
			.append("   AND DC.ID_CLIENTE = ? ")
			.append("   AND DC.ID_DIVISAO_CLIENTE = ?");

			StringBuffer idsParamCliente = new StringBuffer("");
			boolean primeiro = true;
			for (Iterator iter = parametros.iterator(); iter.hasNext();) {
				Long pc = (Long) iter.next();
				if (!primeiro)
					idsParamCliente.append(",");
				else
					primeiro = false;

				idsParamCliente.append(pc);
			}
			if (!idsParamCliente.toString().equalsIgnoreCase(""))
				sql.append(" AND PC.ID_PARAMETRO_CLIENTE in (")
						.append(idsParamCliente).append(")");

			sql.append(" ORDER BY PC.ID_PARAMETRO_CLIENTE, BRAKEGROUP");

			return sql.toString();
		}
		return null;
	}

	/**	
	 * Metodo que devolve as Coletas. Onde: DS_SIMBOLO_LAYOUT1 eh a moeda
	 * simbolo. EX: R$ VL_TAXA_LAYOUT1 eh o valor da faixa. EX: 4,56
	 * VL_EXCEDENTE_LAYOUT1 eh o valor exedente. EX: 9,31 PS_TAXADO eh o peso
	 * taxado eh 25.
	 * 
	 * OBS: tudo tem _LAYOUT{1,2,3,4} por que sao 4 areas que podem aparecer nos
	 * subreports.
	 * 
	 * @author diegop, rodrigodf
	 * @param id
	 *            identificador da tabela preco
	 * @param idParametroCliente
	 *            id do parametro do cliente
	 * @param jdbcTemplate
	 *            conexao
	 * @param sqlToExecute
	 *            variavel statica do sql a ser executado
	 * @return List com os dados de coleta ou entrega.
	 */
	private static List getColetaEntrega(Long id, Long idParametroCliente,
			JdbcTemplate jdbcTemplate, int sqlToExecute) {
		switch(sqlToExecute) {
			case SUBREPORT_COLETA:
			return jdbcTemplate.queryForList(montaSQLColeta(),
					new Long[] { id });
			case SUBREPORT_ENTREGA:
			return jdbcTemplate.queryForList(montaSQLEntrega(),
					new Long[] { id });
			case SUBREPORT_COLETA_CLIENTE:
			return jdbcTemplate.queryForList(montaSQLColetaComRegras(),
					new Long[] { id, idParametroCliente });
			case SUBREPORT_ENTREGA_CLIENTE:
			return jdbcTemplate.queryForList(montaSQLEntregaComRegras(),
					new Long[] { id, idParametroCliente });
			case SUBREPORT_COLETA_ROTA:
			return jdbcTemplate.queryForList(montaSQLColetaRota(), new Long[] {
					id, idParametroCliente, idParametroCliente,
					idParametroCliente });
			case SUBREPORT_ENTREGA_ROTA:
			return jdbcTemplate.queryForList(montaSQLEntregaRota(), new Long[] {
					id, idParametroCliente, idParametroCliente,
					idParametroCliente });
			default:
		}
		return null;
	}

	/**
	 * M&eacute;todo que rotorna sub-relat&oacute;rio de coleta rota 
	 * 
	 * @param idTabelaPreco
	 *            identificador da tabela de pre&ccedil;o
	 * @param idParamCliente
	 *            identificador do parametro do cliente
	 * @param jdbcTemplate
	 *            conexao
	 * @return
	 */
	public static List getSubRelColetaRota(Long idTabelaPreco,
			Long idParamCliente, JdbcTemplate jdbcTemplate) {
		// obs: o parametro comRegras está como falso porque o valor já vem
		// tratado do banco
		List result = normalizaListColetaEntrega(idTabelaPreco, idParamCliente,
				jdbcTemplate, 0, TabelasClienteUtil.SUBREPORT_COLETA_ROTA,
				false);
		
		if (result == null)
			return null;
		
		Set set = new TreeSet(new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Map map1 = (Map)arg0;
						Map map2 = (Map)arg1;
						
				boolean bool = (map1.get("ID_PARCELA_PRECO").equals(map2
						.get("ID_PARCELA_PRECO")));
						return (bool) ? 0 : 1;
			}
		});
				
		set.addAll(result);
		return new ArrayList(set);
	}

	/**
	 * M&eacute;todo que rotorna sub-relat&oacute;rio de coleta rota 
	 * 
	 * @param idTabelaPreco
	 *            identificador da tabela de pre&ccedil;o
	 * @param idParamCliente
	 *            identificador do parametro do cliente
	 * @param jdbcTemplate
	 *            conexao
	 * @return
	 */
	public static List getSubRelEntregaRota(Long idTabelaPreco,
			Long idParamCliente, JdbcTemplate jdbcTemplate) {
		// obs: o parametro comRegras está como falso porque o valor já vem
		// tratado do banco
		List result = normalizaListColetaEntrega(idTabelaPreco, idParamCliente,
				jdbcTemplate, 1, TabelasClienteUtil.SUBREPORT_ENTREGA_ROTA,
				false);
		
		if (result == null)
			return null;
		
		Set set = new TreeSet(new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Map map1 = (Map)arg0;
						Map map2 = (Map)arg1;
						
				boolean bool = (map1.get("ID_PARCELA_PRECO").equals(map2
						.get("ID_PARCELA_PRECO")));
						return (bool) ? 0 : 1;
			}
		});
				
		set.addAll(result);
		return new ArrayList(set);
	}
	
	public static List getSubRelColeta(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		return normalizaListColetaEntrega(idTabelaPreco, Long.valueOf(-1),
				jdbcTemplate, 0, "Coleta", false);
	}
	
	public static List getSubRelEntrega(Long idTabelaPreco,
			JdbcTemplate jdbcTemplate) {
		return normalizaListColetaEntrega(idTabelaPreco, Long.valueOf(-1),
				jdbcTemplate, 1, "Entrega", false);
	}
	
	/**
	 * 
	 * @param idTabelaPreco
	 * @param idParametroCliente
	 * @param jdbcTemplate
	 * @param modo
	 * @param sqlToExecute
	 * @return
	 */
	private static List normalizaListColetaEntrega(Long idTabelaPreco,
			Long idParametroCliente, JdbcTemplate jdbcTemplate, int modo,
			int sqlToExecute, boolean comRegra) {
		String complemento = null;
		List result = null;
		//busca dados
		result = getColetaEntrega(idTabelaPreco, idParametroCliente,
				jdbcTemplate, sqlToExecute);

		//verifica se eh coleta ou entrega
		if(modo == 0)
			complemento = "Coleta";
		else
			complemento = "Entrega";

		if (result == null || result.size() == 0)
			return null;
		
		DecimalFormat formatador = new DecimalFormat("###,##0.00;(###,##0.00)",
				new DecimalFormatSymbols(LocaleContextHolder.getLocale()));

		//formata conforme jasper
		List newResult = new ArrayList();
		Map newDados = new HashMap();		
		ListIterator lit = result.listIterator();
		while (lit.hasNext()) {
			Map dados = (Map)lit.next();
			if (dados != null) {
				if (MapUtilsPlus.getString(dados,"CD_PARCELA_PRECO",null)!=null) {
					if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO", null)
							.equals("ID" + complemento + "UrbanaConvencional"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT1",
								comRegra, formatador);
					else if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO",
							null).equals(
							"ID" + complemento + "InteriorConvencional"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT2",
								comRegra, formatador);
					else if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO",
							null).equals(
							"ID" + complemento + "UrbanaEmergencia"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT3",
								comRegra, formatador);
					else
						// sera: ID..InteriorEmergencia
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT4",
								comRegra, formatador);
					
				}				
			}
		}
		newResult.add(newDados);
		return newResult;
	}
	
	private static List normalizaListColetaEntrega(Long idTabelaPreco,
			Long idParametroCliente, JdbcTemplate jdbcTemplate, int modo,
			String complemento, boolean comRegra) {
		List result = null;
		
		//modo 0 - coleta, 1 - entrega
		if (modo == 0) {
			if(comRegra)
				result = getColetaEntrega(idTabelaPreco, idParametroCliente,
						jdbcTemplate,
						TabelasClienteUtil.SUBREPORT_COLETA_CLIENTE);
			else
				result = getColetaEntrega(idTabelaPreco, null, jdbcTemplate,
						TabelasClienteUtil.SUBREPORT_COLETA);
		} else if (modo == 1) {
			if(comRegra)
				result = getColetaEntrega(idTabelaPreco, idParametroCliente,
						jdbcTemplate,
						TabelasClienteUtil.SUBREPORT_ENTREGA_CLIENTE);
			else
				result = getColetaEntrega(idTabelaPreco, null, jdbcTemplate,
						TabelasClienteUtil.SUBREPORT_ENTREGA);
		}
		
		if (result == null || result.size() == 0)
			return null;
		
		List newResult = new ArrayList();
		Map newDados = new HashMap();		
		ListIterator lit = result.listIterator();
		while(lit.hasNext()) {
			Map dados = (Map)lit.next();
			if (dados!=null) {
				if (MapUtilsPlus.getString(dados,"CD_PARCELA_PRECO",null)!=null) {
					if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO", null)
							.equals("ID" + complemento + "UrbanaConvencional"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT1",
								comRegra);
					else if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO",
							null).equals(
							"ID" + complemento + "InteriorConvencional"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT2",
								comRegra);
					else if (MapUtilsPlus.getString(dados, "CD_PARCELA_PRECO",
							null).equals(
							"ID" + complemento + "UrbanaEmergencia"))
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT3",
								comRegra);
					else
						// sera: ID..InteriorEmergencia
						putOnMapColetaEntrega(dados, newDados, "_LAYOUT4",
								comRegra);
				}				
			}
		}
		newResult.add(newDados);
		return newResult;
	}
	
	private static void putOnMapColetaEntrega(Map dados, Map newDados,
			String chave, boolean comRegra) {
		newDados.put("DS_SIMBOLO" + chave,dados.get("DS_SIMBOLO"));						
		newDados.put("VL_TAXA" + chave,dados.get("VL_TAXA"));
		newDados.put("VL_EXCEDENTE" + chave,dados.get("VL_EXCEDENTE"));
		newDados.put("PS_TAXADO" + chave,dados.get("PS_TAXADO"));	
		if (comRegra) {
			newDados.put("TAXA_CLIENTE" + chave,dados.get("TAXA_CLIENTE"));
			newDados.put("TP_TAXA_INDICADOR" + chave,
					dados.get("TP_TAXA_INDICADOR"));
			newDados.put("PS_MINIMO" + chave,dados.get("PS_MINIMO"));
			newDados.put("TX_VL_TAXA" + chave,dados.get("TX_VL_TAXA"));
			newDados.put("TX_VL_EXCEDENTE" + chave,
					dados.get("TX_VL_EXCEDENTE"));
		}
	}
	
	/**
	 * Metodo para colocar os valores dos campos no map com os nomes
	 * especificados no jasper
	 * 
	 * @param dados
	 * @param newDados
	 * @param chave
	 * @param comRegra
	 * @param formatador
	 */
	private static void putOnMapColetaEntrega(Map dados, Map newDados,
			String chave, boolean comRegra, DecimalFormat formatador) {
		BigDecimal vlTaxa = null, vlExcedente = null, psTaxado = null;
		vlTaxa = (BigDecimal)dados.get("VL_TAXA");
		vlExcedente = (BigDecimal)dados.get("VL_EXCEDENTE");
		psTaxado = (BigDecimal)dados.get("PS_TAXADO");
			
		newDados.put("DS_SIMBOLO" + chave, dados.get("DS_SIMBOLO"));
		
		if (vlTaxa != null)
			newDados.put("VL_TAXA" + chave,
					formatador.format(vlTaxa.doubleValue()));
		else
			newDados.put("VL_TAXA" + chave, "");
		
		if (vlExcedente != null)
			newDados.put("VL_EXCEDENTE" + chave,
					formatador.format(vlExcedente.doubleValue()));
		else
			newDados.put("VL_EXCEDENTE" + chave, "");
		
		if (psTaxado != null)
			newDados.put("PS_TAXADO" + chave, psTaxado);
		else
			newDados.put("PS_TAXADO" + chave, Long.valueOf(0));
		
		if (comRegra) {
			BigDecimal taxaCliente = null, psMinimo = null, txVlTaxa = null, txVlExcedente = null;
			taxaCliente = (BigDecimal)dados.get("TAXA_CLIENTE");
			psMinimo = (BigDecimal)dados.get("PS_MINIMO");
			txVlTaxa = (BigDecimal)dados.get("TX_VL_TAXA");
			txVlExcedente = (BigDecimal)dados.get("TX_VL_EXCEDENTTE");
			
			newDados.put("TAXA_CLIENTE" + chave,
					formatador.format(taxaCliente.doubleValue()));
			newDados.put("TP_TAXA_INDICADOR" + chave,
					dados.get("TP_TAXA_INDICADOR"));
			newDados.put("PS_MINIMO" + chave,
					formatador.format(psMinimo.doubleValue()));
			newDados.put("TX_VL_TAXA" + chave,
					formatador.format(txVlTaxa.doubleValue()));
			newDados.put("TX_VL_EXCEDENTTE" + chave,
					formatador.format(txVlExcedente.doubleValue()));
		}
	}	
	
	public static List getSubRelEntregaCliente(Long idTabelaPreco,
			Long idParametroCliente, JdbcTemplate jdbcTemplate) {
		return getSubRelColetaEntregaComRegras(idTabelaPreco,
				idParametroCliente, jdbcTemplate, 1);
	}
	
	public static List getSubRelColetaCliente(Long idTabelaPreco,
			Long idParametroCliente, JdbcTemplate jdbcTemplate) {
		return getSubRelColetaEntregaComRegras(idTabelaPreco,
				idParametroCliente, jdbcTemplate, 0);
	}
	
	/**
	 * Metodo que retorna uma lista de Map com as Legendas.
	 * 
	 * @author Diego Pacheco - LMS - GT5
	 * @param void
	 * @return List com os Map e dentro dos maps as legendas. 
	 * 
	 */
	public static List getSubRelLegendas(JdbcTemplate jdbcTemplate) {
		/* Obtem os produtos especificos. */
		List result = jdbcTemplate.queryForList(montaSQLProdEspc());
		if (result == null)
			return null;
		
		Map mapDados = new HashMap();		
		List newList = new ArrayList(3);
		int i=0;
		
		ListIterator lit = result.listIterator();
		while (lit.hasNext()) {
			Map aux = (Map)lit.next();			
			mapDados.put("SUB1_TXT_LEG" + i++,aux.get("PROD_ESPEC"));
			
			if (i == 3) {
				newList.add(mapDados);
				mapDados = new HashMap();
				i=0;
			}
		}
		if (i != 0)
			newList.add(mapDados); /* No caso do ultimo registro nao chegar a 3 */
		
		return newList;
	}

	public static List getSubRelAereo(JdbcTemplate jdbcTemplate) {
		/* obtem os aeroportos por cidade. */
		List result = jdbcTemplate.queryForList(montaSQLAeroCids());
		if (result == null || result.isEmpty())
			return null;
		
		List newList = new ArrayList();

		String cidadeAnterior = null;
		String aeroportos = null; 
		Map mapDados = new HashMap();
		
		for (Iterator it = result.iterator(); it.hasNext();) {
			Map mapCidAero =(Map)it.next();
			
			if (cidadeAnterior == null
					|| !mapCidAero.get("CD_CIDADE").toString().toUpperCase()
							.trim().equals(cidadeAnterior)) {
				if(cidadeAnterior!=null){//já contem cidade com aeroportos
					mapDados.put("SUB_AEREO",
							(cidadeAnterior + " = " + aeroportos));
					newList.add(mapDados);
					
					//reinicia variáveis para próxima cidade com aeroportos
					mapDados = new HashMap();
					aeroportos = null;
				}
			}
			
			//atribui valores correntes do registro
			cidadeAnterior = mapCidAero.get("CD_CIDADE").toString()
					.toUpperCase().trim();
			if(aeroportos==null){
				aeroportos = mapCidAero.get("CID_AERO").toString();
			}else{
				aeroportos += "/" + mapCidAero.get("CID_AERO").toString();	
			}
		}	
		
		//associa ultimo registro
		mapDados.put("SUB_AEREO",(cidadeAnterior + " = " + aeroportos));
		newList.add(mapDados);
		
		return newList;
	}

	/**
	 * Metodo que retorna uma lista com o subRelatorio de Coleta ou entrega.
	 * 
	 * @author Diego Pacheco - LMS - GT5
	 * @version 1.0 
	 * @param idTabelaPreco
	 *            id da tabela de preco.
	 * @param jdbcTemplate
	 *            instancia do mesmo.
	 * @param modo
	 *            0 para Coleta 1 para entega.
	 * @return List com os dados.
	 */
	private static List getSubRelColetaEntregaComRegras(Long idTabelaPreco,
			Long idParametroCliente, JdbcTemplate jdbcTemplate, int modo) {
		List result = null;		
		if (modo==0)		// modo de Coleta	
			result = normalizaListColetaEntrega(idTabelaPreco,
					idParametroCliente, jdbcTemplate, 0, "Coleta", true);
		else if (modo==1)	// modo de Entrega	
			result = normalizaListColetaEntrega(idTabelaPreco,
					idParametroCliente, jdbcTemplate, 1, "Entrega", true);
		
		if (result == null || result.size() == 0)
			return null;
		
		DecimalFormat formatador = new DecimalFormat("###,##0.00;(###,##0.00)",
				new DecimalFormatSymbols(LocaleContextHolder.getLocale()));

		List newResult = new ArrayList();		
		ListIterator lit = result.listIterator();		
		while (lit.hasNext()) {
			Map dados = (Map)lit.next();
			if (dados != null) {
				gerenciaAplicaRegraSubRelColetaEntrega(dados, "_LAYOUT1",
						formatador);
				gerenciaAplicaRegraSubRelColetaEntrega(dados, "_LAYOUT2",
						formatador);
				gerenciaAplicaRegraSubRelColetaEntrega(dados, "_LAYOUT3",
						formatador);
				gerenciaAplicaRegraSubRelColetaEntrega(dados, "_LAYOUT4",
						formatador);
				newResult.add(dados);
			}						
		}		
		return newResult;		
	}

	/***
	 * Metodo que gerencia a chamada ao metiodo aplicaRegrasSubRelColeta
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados
	 *            eh o map com os dados.
	 * @param chave
	 *            eh o layut a ser procurado.
	 * @param formatador
	 *            eh o DecimalFormat q sera usada para aplicar a formatacao.
	 */
	private static void gerenciaAplicaRegraSubRelColetaEntrega(Map dados,
			String chave, DecimalFormat formatador) {
		Object aux = getFormMapUtils(dados,"TP_TAXA_INDICADOR",chave);
		if (aux!=null)				
			aplicaRegrasSubRelColetaEntrega(dados, aux.toString().toUpperCase()
					.toCharArray()[0], chave, formatador);
		else
			aplicaRegrasSubRelColetaEntrega(dados,'Z',chave,formatador);
	}
	
	/**
	 * Metodo que descobre o tipo de indicador e conforme esse tipo aplica a
	 * regra especifica do novo valor.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados
	 *            eh um Map com os dados
	 * @return não retorna valor.O retorbno eh feito por Referencia. 
	 **/
	private static void aplicaRegrasSubRelColetaEntrega(Map dados,
			char tpIndicador, String lista, DecimalFormat formatador) {
		switch (tpIndicador) {
			case 'V':
			aplicaValorRegraSubRelColetaEntrega(dados, new Object[] {
					getFormMapUtils(dados, "PS_MINIMO", lista),
					getFormMapUtils(dados, "TX_VL_TAXA", lista),
					getFormMapUtils(dados, "TX_VL_EXCEDENTE", lista) }, lista,
					formatador);
			break;
			case 'A':
			aplicaValorRegraSubRelColetaEntrega(dados,
					montaValorColetaEntrega(dados, '+', lista), lista,
					formatador);
			break;
			case 'D':
			aplicaValorRegraSubRelColetaEntrega(dados,
					montaValorColetaEntrega(dados, '-', lista), lista,
					formatador);
			break;			
			case 'T':			
			default:
			aplicaValorRegraSubRelColetaEntrega(dados,
					new Object[] { getFormMapUtils(dados, "PS_TAXADO", lista),
							getFormMapUtils(dados, "VL_TAXA", lista),
							getFormMapUtils(dados, "VL_EXCEDENTE", lista) },
					lista, formatador);
			break;
		}				
	}
	
	/**
	 * Metodo que retorna um array de Objetos no caso de acresimo ou decrecimo
	 * de valores em Coletas e Entregas.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados
	 *            eh um Map com os dados.
	 * @param sinal
	 *            ['+','-'] para indicar a operacao a ser realizada.
	 * @return void pois eh um procedimento e retorna por referencia.
	 */
	private static Object[] montaValorColetaEntrega(Map dados, char sinal,
			String lista) {
		double psMinimo = getFormMapUtilsBigDecimal(dados,"PS_MINIMO",lista);
		double txVlTaxa = getFormMapUtilsBigDecimal(dados,"TX_VL_TAXA",lista);
		double txVlExcendente = getFormMapUtilsBigDecimal(dados,
				"TX_VL_EXCEDENTE", lista);
		
		BigDecimal psTaxado = null, vlExcedente = null;
		
		if(psMinimo != 0){
			psTaxado = new BigDecimal(psMinimo);
		}else{
			psTaxado = new BigDecimal(getFormMapUtilsBigDecimal(dados,
					"PS_TAXADO", lista));
		}

		if(txVlExcendente != 0){
			vlExcedente = new BigDecimal(txVlExcendente);
		}else{
			vlExcedente = new BigDecimal(getFormMapUtilsBigDecimal(dados,
					"VL_EXCEDENTE", lista));
		}
		
		if (sinal=='+') {		
			return new Object[]{ 
					psTaxado,
					new BigDecimal(getFormMapUtilsBigDecimal(dados, "VL_TAXA",
							lista)
							+ ((getFormMapUtilsBigDecimal(dados, "VL_TAXA",
									lista) * txVlTaxa) / 100)), vlExcedente };
		} else if (sinal=='-') {
			return new Object[]{ 
					psTaxado,
					new BigDecimal(getFormMapUtilsBigDecimal(dados, "VL_TAXA",
							lista)
							- ((getFormMapUtilsBigDecimal(dados, "VL_TAXA",
									lista) * txVlTaxa) / 100)), vlExcedente };
		}
		return null;
	}
	
	/**
	 * Metodo que aplica os valores em cima dos dados de Coletas e Entregas.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados
	 *            eh um Map com os dados.
	 * @param valores
	 *            eh um array de Object contendo os valores a serem colocados.
	 * @return void pois eh um procedimento e retorna por referencia.
	 */
	private static void aplicaValorRegraSubRelColetaEntrega(Map dados,
			Object[] valores, String lista, DecimalFormat formatador) {
		if (MapUtilsPlus.intByMapExploder(dados,"PS_MINIMO" + lista )!=0) {
			dados.put("PS_TAXADO" + lista ,((BigDecimal)valores[0])) ;			
		}
		if (MapUtilsPlus.intByMapExploder(dados,"VL_TAXA" + lista )!=0) {
			
			dados.put("VL_TAXA" + lista, FormatUtils.formatDecimal(
					FORMATO_2_CASAS, (BigDecimal) valores[1]));
		}
		if (MapUtilsPlus.intByMapExploder(dados,"VL_EXCEDENTE" + lista )!=0) {
			dados.put("VL_EXCEDENTE" + lista, FormatUtils.formatDecimal(
					FORMATO_2_CASAS, (BigDecimal) valores[2]));
		}
	}

	private static Object getFormMapUtils(Map dados,String chave,String lista) {
		return MapUtilsPlus.getObjectOnList(dados, chave,
				new String[] { lista });
	}
	
	private static double getFormMapUtilsBigDecimal(Map dados, String chave,
			String lista) {
		return MapUtilsPlus.getDoubleOnList(dados, chave,
				new String[] { lista });
	}

	public static List getSubRelServicosAdicionais(Long idTabelaPreco,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate,
			Long idParametro) {
		List<Map> listaServicosAd = new ArrayList<Map>();
		listaServicosAd.addAll(jdbcTemplate.queryForList(montaSQLServicosAd(),
				new Long[] { idTabelaPreco }));
		return parametrizeServicosAdicionais(listaServicosAd, idTabelaPreco,
				jdbcTemplate, configuracoesFacade);
	}

	private static List parametrizeServicosAdicionais(
			List<Map> listaServicosAd, Long idTabelaPreco,
			JdbcTemplate jdbcTemplate, ConfiguracoesFacade configuracoesFacade) {
		Map mapLocal = (Map) threadLocal.get();
		String moeda = getMoeda(idTabelaPreco,jdbcTemplate);

		List listToReturn = new ArrayList();
		for (Map data : listaServicosAd) {
			String vlServico = MapUtils.getString(data,"VL_SERVICO");
			if (vlServico == null)
				continue;
			BigDecimal vlMinimo = MapUtilsPlus.getBigDecimal(data, "VL_MINIMO");
			String nmParcelaPreco = MapUtils
					.getString(data, "NM_PARCELA_PRECO");
			String vlServicoFormatado = FormatUtils.formatDecimal(
					FORMATO_2_CASAS, new BigDecimal(vlServico));
			String valor = moeda + " " + vlServicoFormatado;
			String cdParcelaPreco = MapUtils
					.getString(data, "CD_PARCELA_PRECO");
			String tpIndicadorCalculo = MapUtils.getString(data,
					"TP_INDICADOR_CALCULO");

			if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)){
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relPorToneladaFracao");
			} else if (ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO
					.equals(cdParcelaPreco)) {
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorEvento");
			/** Lista serviços que SEJAM do tipo ESTADIA */
			} else if (ConstantesExpedicao.TIPOS_ESTADIA
					.contains(cdParcelaPreco)) {
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relPorQuantidadeDias");
			} else if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)){
				if( mapLocal != null){
					String value = moeda + " " + mapLocal.get("Valor") + " "
							+ configuracoesFacade.getMensagem("relEscolta");
					Map mapTmp = new HashMap();
					mapTmp.put("NOME", mapLocal.get("Nome")+":");
					threadLocal.remove();
					mapLocal = null;
					mapTmp.put("VALOR", value);
					listToReturn.add(mapTmp);
				}
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relEscolta");
			} else if (ConstantesExpedicao.CD_SEGURANCA_ADICIONAL
					.equals(cdParcelaPreco)) {
				valor = valor
						+ " "
						+ configuracoesFacade
								.getMensagem("relSegurancaAdicional");
			} else if (ConstantesExpedicao.CD_PALETIZACAO
					.equals(cdParcelaPreco)) {
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorPallet");
			} else if("PF".equals(tpIndicadorCalculo)){
				valor = vlServicoFormatado + " "
						+ configuracoesFacade.getMensagem("relValorFrete");
				if (vlMinimo != null) {
					valor += " "
							+ configuracoesFacade.getMensagem("relOuMinimo")
							+ " "
							+ moeda
							+ " "
							+ formatDuasCasasDecimais(vlMinimo);
				}
			} else if("PC".equals(tpIndicadorCalculo)) {
				valor = vlServicoFormatado
						+ configuracoesFacade
								.getMensagem("relSobreValorMercadoria");
				if (vlMinimo != null) {
					valor += " "
							+ configuracoesFacade.getMensagem("relOuMinimo")
							+ " "
							+ moeda
							+ " "
							+ formatDuasCasasDecimais(vlMinimo);
				}
			}else{
				valor = valor + " "
						+ configuracoesFacade.getMensagem("relPorConhecimento");
			}

			Map mapTmp = new HashMap();
			mapTmp.put("label", nmParcelaPreco+":");
			mapTmp.put("valor", valor);
			listToReturn.add(mapTmp);
		}
		unionSort(listToReturn, "label");

		return listToReturn;
	}
	
	private static List unionOrdering(List list, final String keyName,
			final String keyValue) {
		unionSort(list, keyName);
		for (Iterator it = list.iterator(); it.hasNext(); ) {
			Map map = (Map)it.next();
			Map map2 = new HashMap();
			if (it.hasNext()) {
				map2 = (Map)it.next();
				it.remove();
			}		

			Object oN = map.remove(keyName);
			Object oV = map.remove(keyValue);
			map.put("label", oN);
			map.put("valor", oV);

			oN = map2.remove(keyName);
			oV = map2.remove(keyValue);
			map2.put("label", oN);
			map2.put("valor", oV);

			map.putAll(map2);
		}		
		return list;
	}

	public static void unionSort(List list, final String keyName) {
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map map1 = (Map)o1;
				Map map2 = (Map)o2;

				String nome1 = (String)map1.get(keyName);
				String nome2 = (String)map2.get(keyName);

				return nome1.compareTo(nome2);
			}
		});
	}

	/**
	 * Monta os subrelatorios do modulo tabela de preco que NÂO sao do tipo
	 * Minimo progressivo
	 * 
	 * @param idTabelaPreco
	 * @param subReports
	 * @param configuracoesFacade
	 * @param jdbcTemplate
	 * @param parametros
	 */
	public static void montaSubReportsTabelaPreco(Long idTabelaPreco,
			int[] subReports, ConfiguracoesFacade configuracoesFacade,
			JdbcTemplate jdbcTemplate, Map parametros) {
		montaSubReportsTabelaPreco(idTabelaPreco, subReports, null, 0, 0,
				configuracoesFacade, jdbcTemplate, parametros);
	}

	/**
	 * Monta os subrelatorios do modulo tabela de preco do tipo Minimo
	 * Progressivo
	 */
	public static void montaSubReportsTabelaPreco(Long idTabelaPreco,
			int[] subReports, String tabela, int crosstabSize, int posPsminimo,
			ConfiguracoesFacade configuracoesFacade, JdbcTemplate jdbcTemplate,
			Map parametros) {
		List generalidades = new ArrayList();
		if(containsSubReport(SUBREPORT_GENERALIDADES_TABELA_PRECO,subReports)){
			generalidades = getGeneralidadesTabelaPreco(
					montaSqlSubGeneralidadesTab(), idTabelaPreco,
					getMoeda(idTabelaPreco, jdbcTemplate), tabela,
					crosstabSize, posPsminimo, configuracoesFacade,
					jdbcTemplate);
			parametros.put(KEY_PARAMETER_GENERALIDADES,generalidades);
		}

		List formalidades = new ArrayList();
		if(containsSubReport(SUBREPORT_FORMALIDADES,subReports)){
			formalidades = TabelasClienteUtil
					.getFormalidadesClienteRodoviarioNacional(idTabelaPreco,
							null, null, jdbcTemplate);
			parametros.put(KEY_PARAMETER_FORMALIDADES,formalidades);
		} else if (containsSubReport(SUBREPORT_FORMALIDADES_AEREO,subReports)) {
			formalidades = getFormalidadesAereoNacional(idTabelaPreco,
					configuracoesFacade, jdbcTemplate);
			parametros.put(TabelasClienteUtil.KEY_PARAMETER_FORMALIDADES_AEREO,
					formalidades);
		}

		List servicosAd = new ArrayList();
		if(containsSubReport(SUBREPORT_SERVICOSAD,subReports)){
			servicosAd = getSubRelServicosAdicionais(idTabelaPreco,
					configuracoesFacade, jdbcTemplate, null);
			parametros.put(KEY_PARAMETER_SERVICOSAD,servicosAd);		
		}
		if (containsSubReport(SUBREPORT_GENERALIDADE_DIFICULDADE_ENTREGA,
				subReports)) {
			List<Map> generalidadesDificuldadeEntrega = getGeneralidadesTabelaPrecoDificuldadeEntrega(
					idTabelaPreco, jdbcTemplate);
			aplicaGeneralidade(generalidadesDificuldadeEntrega, false,
					jdbcTemplate, null, configuracoesFacade,
					getMoeda(idTabelaPreco, jdbcTemplate));
			parametros.put(KEY_PARAMETER_DIFICULDADE_ENTREGA,
					generalidadesDificuldadeEntrega);
		}
		List coleta = new ArrayList();
		if(containsSubReport(SUBREPORT_COLETA, subReports)){
			coleta = TabelasClienteUtil.getSubRelColeta(idTabelaPreco,
					jdbcTemplate);
			parametros.put(TabelasClienteUtil.KEY_PARAMETER_COLETA, coleta);			
		}

		List entrega = new ArrayList();
		if(containsSubReport(SUBREPORT_ENTREGA, subReports)){
			entrega = TabelasClienteUtil.getSubRelEntrega(idTabelaPreco,
					jdbcTemplate);
			parametros.put(TabelasClienteUtil.KEY_PARAMETER_ENTREGA, entrega);			
		}
		
		List taxaTerrestre = new ArrayList();
		if (containsSubReport(SUBREPORT_TAXA_TERRESTRE, subReports)) {
			taxaTerrestre = getSubRelTaxaTerrestre(idTabelaPreco,jdbcTemplate);
			parametros.put(KEY_PARAMETER_TAXA_TERRESTRE,taxaTerrestre);
		}
		
		List aereo = new ArrayList();
		if(containsSubReport(SUBREPORT_AEREO,subReports)){
			aereo = getSubRelAereo(jdbcTemplate);
			parametros.put(KEY_PARAMETER_AEREO, aereo);
		}
		
		List legendas = new ArrayList();
		if(containsSubReport(SUBREPORT_LEGENDAS,subReports)){
			legendas = getSubRelLegendas(jdbcTemplate);
			parametros.put(KEY_PARAMETER_LEGENDAS, legendas);
		}
		
		}

	/**
	 * Retorna o numero de registro da tabela
	 * 
	 * @param tableName
	 *            nome da tabela
	 * @param jdbcTemplate
	 *            conexao
	 * @return numero de registro da tabela
	 */
	public static int getCountTable(String tableName, JdbcTemplate jdbcTemplate) {
		if(tableName == null)
			return 0;
		String sqlCount = "select COUNT(*) as qtde from " + tableName;
		return jdbcTemplate.queryForInt(sqlCount);
	}

	/**
	 * 
	 * @param totRegistrosPorPagina
	 * @param totDeducoesPrimeiraPagina
	 * @param totalRegistros
	 * @param totDeducoesPorPagina
	 * @param parametersReport
	 */
	public static void setEspacoQuebra(int totRegistrosPorPagina,
			int totDeducoesPrimeiraPagina, int totalRegistros,
			int totDeducoesPorPagina, int reserverdSpaceSubreport,
			Map parametersReport) {

		}

	/**
	 * 
	 * @param listaUF
	 * @param msgTodoEstado
	 * @param msgDemaisLocalidades
	 * @param ufOrigemDestino
	 *            - O uf da origem e do destino concatenados com "-"
	 * @param complemento
	 * @return
	 */
	public static String verificaComplemento(List listaUF,
			String msgTodoEstado, String msgDemaisLocalidades,
			String ufOrigemDestino, String complemento) {
		if(complemento == null){
			complemento = !listaUF.contains(ufOrigemDestino) ? msgTodoEstado
					: msgDemaisLocalidades;
		} else {
			listaUF.add(ufOrigemDestino);
		}
		return complemento;
	}

	/**
	 * Monta um map agrupando as generalidades, coleta e entrega de cada
	 * parametro
	 * 
	 * @param idCliente
	 * @param idDivisao
	 * @param parametros
	 * @param agrup
	 * @return
	 */
	public static Map montaAgrupamento(Long idCliente, Long idDivisao,
			Long idSimulacao, List parametrosCliente, Map agrup,
			JdbcTemplate jdbcTemplate) {

		String sqlGrupo = montaSqlGrupos(parametrosCliente, idSimulacao);

		if (sqlGrupo != null) {
			List grupos = null;
			if(idSimulacao==null){
				grupos = jdbcTemplate.queryForList(sqlGrupo, new Long[] {
						idCliente, idDivisao });
			}else{
				grupos = jdbcTemplate.queryForList(sqlGrupo);
			}

			for (Iterator iter = grupos.iterator(); iter.hasNext();) {
				Map map = (Map) iter.next();
				Long idParametro = Long.valueOf(map.get("ID_PARAMETRO_CLIENTE")
						.toString());

				String valor = map.get("BRAKEGROUP").toString();
				if (!agrup.containsKey(idParametro)) {
					agrup.put(idParametro, valor);
				} else {
					String valores = valor + agrup.get(idParametro).toString();
					agrup.put(idParametro, valores);
				}
			}
		}

		int i = 0;
		Map mapa = new HashMap();
		List todasChaves = new ArrayList();
		// le o map de taxasClientes e generalidades de parametros e agrupa os
		// que tiverem os mesmos valores;
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

	/**
	 * Retorna o grupo ao qual o parametro pertence
	 * 
	 * @param idParametro
	 * @param agrup
	 * @return
	 */
	public static String getGrupo(Long idParametro, Map agrup, int tamanho) {
		String novoGrupo = "grupo" + tamanho;
		for (Iterator iter = agrup.keySet().iterator(); iter.hasNext();) {
			String chave = (String) iter.next();
			List value = (List) agrup.get(chave);
			for (Iterator it = value.iterator(); it.hasNext();) {
				Long v = (Long) it.next();
				if (idParametro.equals(v)) {
					return chave;
				}
			}
		}
		if (agrup.containsKey(novoGrupo)) {
			List lista = (List) agrup.get(novoGrupo);
			lista.add(idParametro);
			agrup.put(novoGrupo, lista);
		} else {
			List lista = new ArrayList();
			lista.add(idParametro);
			agrup.put(novoGrupo, lista);
		}
		return novoGrupo;
	}

	/**
	 * Aplica desconto na tabela temporária nos campos COLUMN.. especificados no
	 * set da faixa
	 * 
	 * @param faixaProgColunmName
	 *            set com maps da coluna com o valor do desconto
	 * @param tablename
	 *            nome da tabela temporária
	 * @param jdbctemplate
	 *            conexão
	 */
	public static void applyDiscount(Set faixaProgColunmName, String tablename,
			JdbcTemplate jdbctemplate) {
		StringBuffer updateRangeValueDiscount = new StringBuffer("");
		updateRangeValueDiscount.append("update ").append(tablename)
				.append(" set ");
		int keycount = 0;

		if(faixaProgColunmName != null && tablename != null) {
			for (Iterator fp = faixaProgColunmName.iterator(); fp.hasNext();) {
				Map map = new HashMap();
				map = (Map)fp.next(); //one record
				Set keyset = map.keySet(); //set of keys
				keycount++;

				for(Iterator ks = keyset.iterator(); ks.hasNext();) {
					String key = (String)ks.next();
					BigDecimal value = (BigDecimal)map.get(key);

					//0 isn't a discount and avoid division by 0
					if (value == null || value.longValue() == 0)
						continue;
					if (key.indexOf(updateRangeValueDiscount.toString()) == -1) { // column
																					// ainda
																					// não
																					// incluida
																					// no
																					// update
						//column = column - (column * (discont/100))
						updateRangeValueDiscount.append(key).append(" = ")
								.append(key).append(" - (").append(key)
								.append(" * (").append(value.longValue())
								.append("/100))");
						if(fp.hasNext()) {
							updateRangeValueDiscount.append(", ");
						}
					}
				}
			}
		}
		//apply discount
		if(keycount > 0)
			jdbctemplate.execute(updateRangeValueDiscount.toString());
	}

	/**
	 * Seta o atributo local identificado pela chave com o valor
	 * 
	 * @param key
	 *            chave do atributo local
	 * @param value
	 *            valor do atributo local
	 */
	public static void setLocalVariableValue(ThreadLocal thrLoc, Object key,
			Object value) {
		Map map = (Map)thrLoc.get();
		if(map == null)
			map = new HashMap();
		map.put(key, value);
		thrLoc.set(map);
	}

	/**
	 * Retorna valor do atributo local identificado pela chave
	 * 
	 * @param key
	 *            chave do atributo local
	 * @return valor do atributo local
	 */
	public static Object getLocalVariableValue(ThreadLocal thrLoc,Object key) {
		Map map = (Map)thrLoc.get();
		if(map == null)
			return null;
		return map.get(key);
	}

	/**
	 * Retorna um Map contendo dados do parametro do cliente obs: por enquanto
	 * apenas retorna os dados que dizem respeito ao Advalorem
	 * 
	 * @param idParametro
	 * @param jdbcTemplate
	 * @return
	 */
	public static Map buscaDadosParametroCliente(Long idParametro,
			JdbcTemplate jdbcTemplate) {
		List lista = jdbcTemplate.queryForList(SQL_PARAMETROS,
				new Long[] { idParametro });
		Map map = (Map) lista.get(0);
		return map;
	}

	/**
	 * Aplica o desconto/Acrescimo definidos no parametro do cliente que dizem
	 * respeito ao Advalorem
	 * 
	 * @param idParametro
	 * @param adv
	 */
	public static BigDecimal aplicaRegraAdvalorem(Map map, BigDecimal adv){
		String tpAdvalorem = MapUtils.getString(map,"tp_indicador_advalorem");
		BigDecimal vlAdvalorem = MapUtilsPlus.getBigDecimal(map,
				"vl_advalorem", BigDecimal.ZERO);
		adv = TabelasClienteUtil.calculaPercentagem(tpAdvalorem, vlAdvalorem,
				adv);
		return adv;
	}

	private static final String TP_VALOR = "V";
	private static final String TP_PERCENTUAL = "P";
	private static final String TP_FRACAO = "F";
	private static final String TP_QUILO = "Q";
	private static final String TP_FAIXA = "X";

	/**
	 * Reajusta Valor dos Parametros conforme Percentual de reajuste.
	 * 
	 * @param parametroMapped
	 * @param pcReajuste
	 */
	public static void reajustarParametros(Map parametroMapped,
			BigDecimal pcReajuste) {
		if(!BigDecimalUtils.hasValue(pcReajuste)) {
			return;
		}

		/** MINIMO_GRIS */
		String tpIndicadorMinimoGris = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_MINIMO_GRIS");
		if (TP_VALOR.equals(tpIndicadorMinimoGris)) {
			BigDecimal vlMinimoGris = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_MINIMO_GRIS", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlMinimoGris)) {
				parametroMapped.put("VL_MINIMO_GRIS",
						BigDecimalUtils.acrescimo(vlMinimoGris, pcReajuste));
			}
		}

		/** MINIMO_TRT */
		String tpIndicadorMinimoTrt = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_MINIMO_TRT");
		if (TP_VALOR.equals(tpIndicadorMinimoTrt)) {
			BigDecimal vlMinimoTrt = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_MINIMO_TRT", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlMinimoTrt)) {
				parametroMapped.put("VL_MINIMO_TRT",
						BigDecimalUtils.acrescimo(vlMinimoTrt, pcReajuste));
			}
		}

		/** MINIMO_TDE */
		String tpIndicadorMinimoTde = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_MINIMO_TDE");
		if (TP_VALOR.equals(tpIndicadorMinimoTde)) {
			BigDecimal vlMinimoTde = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_MINIMO_TDE", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlMinimoTde)) {
				parametroMapped.put("VL_MINIMO_TDE",
						BigDecimalUtils.acrescimo(vlMinimoTde, pcReajuste));
			}
		}

		/** PEDAGIO */
		String tpIndicadorPedagio = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_PEDAGIO");
		if (TP_VALOR.equals(tpIndicadorPedagio)
			|| TP_PERCENTUAL.equals(tpIndicadorPedagio)
			|| TP_FRACAO.equals(tpIndicadorPedagio)
			|| TP_QUILO.equals(tpIndicadorPedagio)
			|| TP_FAIXA.equals(tpIndicadorPedagio)) {
			BigDecimal vlPedagio = MapUtilsPlus.getBigDecimal(parametroMapped,
					"VL_PEDAGIO", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlPedagio)) {
				parametroMapped.put("VL_PEDAGIO",
						BigDecimalUtils.acrescimo(vlPedagio, pcReajuste));
			}
		}

		/** MINIMO_FRETE_PESO */
		String tpIndicadorMinFretePeso = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_MIN_FRETE_PESO");
		if (TP_VALOR.equals(tpIndicadorMinFretePeso)) {
			BigDecimal vlMinFretePeso = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_MIN_FRETE_PESO", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlMinFretePeso)) {
				parametroMapped.put("VL_MIN_FRETE_PESO",
						BigDecimalUtils.acrescimo(vlMinFretePeso, pcReajuste));
			}
		}

		/** FRETE_PESO */
		String tpIndicadorFretePeso = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_FRETE_PESO");
		if (TP_VALOR.equals(tpIndicadorFretePeso)) {
			BigDecimal vlFretePeso = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_FRETE_PESO", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlFretePeso)) {
				parametroMapped.put("VL_FRETE_PESO",
						BigDecimalUtils.acrescimo(vlFretePeso, pcReajuste));
			}
		}

		/** VALOR_REFERENCIA */
		String tpIndicadorValorReferencia = MapUtils.getString(parametroMapped,
				"TP_INDICADOR_VALOR_REFERENCIA");
		if (TP_VALOR.equals(tpIndicadorValorReferencia)) {
			BigDecimal vlValorReferencia = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_VALOR_REFERENCIA", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlValorReferencia)) {
				parametroMapped.put("VL_VALOR_REFERENCIA", BigDecimalUtils
						.acrescimo(vlValorReferencia, pcReajuste));
			}
		}

		/** MINIMO_FRETE_QUILO */
		BigDecimal vlMinimoFreteQuilo = MapUtilsPlus.getBigDecimal(
				parametroMapped, "VL_MINIMO_FRETE_QUILO", BigDecimal.ZERO);
		if (BigDecimalUtils.hasValue(vlMinimoFreteQuilo)) {
			parametroMapped.put("VL_MINIMO_FRETE_QUILO",
					BigDecimalUtils.acrescimo(vlMinimoFreteQuilo, pcReajuste));
		}

		/** MINIMO_FRETE_PERCENTUAL */
		BigDecimal vlMinimoFretePercentual = MapUtilsPlus.getBigDecimal(
				parametroMapped, "VL_MINIMO_FRETE_PERCENTUAL", BigDecimal.ZERO);
		if (BigDecimalUtils.hasValue(vlMinimoFretePercentual)) {
			parametroMapped.put("VL_MINIMO_FRETE_PERCENTUAL", BigDecimalUtils
					.acrescimo(vlMinimoFretePercentual, pcReajuste));
		}

		/** TONELADA_FRETE_PERCENTUAL */
		BigDecimal vlToneladaFretePercentual = MapUtilsPlus.getBigDecimal(
				parametroMapped, "VL_TONELADA_FRETE_PERCENTUAL",
				BigDecimal.ZERO);
		if (BigDecimalUtils.hasValue(vlToneladaFretePercentual)) {
			parametroMapped.put("VL_TONELADA_FRETE_PERCENTUAL", BigDecimalUtils
					.acrescimo(vlToneladaFretePercentual, pcReajuste));
		}

		/** TABELA_ESPECIFICA */
		String tpIndicVlrTblEspecifica = MapUtils.getString(parametroMapped,
				"TP_INDIC_VLR_TBL_ESPECIFICA");
		if (TP_VALOR.equals(tpIndicVlrTblEspecifica)) {
			BigDecimal vlVlrTblEspecifica = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_TBL_ESPECIFICA", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlVlrTblEspecifica)) {
				parametroMapped.put("VL_TBL_ESPECIFICA", BigDecimalUtils
						.acrescimo(vlVlrTblEspecifica, pcReajuste));
			}
		}

		/** FRETE_VOLUME */
		BigDecimal vlFreteVolume = MapUtilsPlus.getBigDecimal(parametroMapped,
				"VL_FRETE_VOLUME", BigDecimal.ZERO);
		if (BigDecimalUtils.hasValue(vlFreteVolume)) {
			parametroMapped.put("VL_FRETE_VOLUME",
					BigDecimalUtils.acrescimo(vlFreteVolume, pcReajuste));
		}

		/** TARIFA_MINIMA */
		String tpTarifaMinima = MapUtils.getString(parametroMapped,
				"TP_TARIFA_MINIMA");
		if (TP_VALOR.equals(tpTarifaMinima)) {
			BigDecimal vlTarifaMinima = MapUtilsPlus.getBigDecimal(
					parametroMapped, "VL_TARIFA_MINIMA", BigDecimal.ZERO);
			if (BigDecimalUtils.hasValue(vlTarifaMinima)) {
				parametroMapped.put("VL_TARIFA_MINIMA",
						BigDecimalUtils.acrescimo(vlTarifaMinima, pcReajuste));
			}
		}
	}

	/**
	 * Regra aplicada de acordo com o requisito de número 11142.
	 * 
	 * @param tpIndivacadorPercentual
	 * @param vlPercentualGris
	 */
	private static void verificaSuspensao(StringBuilder strValorGeneralidade,
			String tpIndicadorPercentual, BigDecimal vlPercentualGris,
			ConfiguracoesFacade configuracoesFacade) {
		if (("V".equals(tpIndicadorPercentual) && BigDecimalUtils
				.isZero(vlPercentualGris))
				|| ("D".equals(tpIndicadorPercentual) && vlPercentualGris
						.doubleValue() == 100)) {
			strValorGeneralidade.delete(0, strValorGeneralidade.length());
			strValorGeneralidade.append(configuracoesFacade
					.getMensagem("suspenso"));
		}
	}

}