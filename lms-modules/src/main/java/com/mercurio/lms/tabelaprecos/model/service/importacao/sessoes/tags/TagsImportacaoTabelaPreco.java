package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags;

import java.util.regex.Pattern;


public abstract class TagsImportacaoTabelaPreco {


	public static final String FORMATO_TAG = "(\\[#[A-Z0-9_]+\\])";
	//tags de sessão de tabela
	public static final String TAG_ZONA_ORIGEM = "[#ZONORIGEM]";
	public static final String TAG_ZONA_DESTINO = "[#ZONDESTINO]";

	public static final String TAG_TP_LOCAL_ORIGEM = "[#TPLOCORIGEM]";
	public static final String TAG_TP_LOCAL_DESTINO = "[#TPLOCDESTINO]";

	public static final String TAG_TP_LOCAL_COML_ORIGEM = "[#TPLCOMORIGEM]";
	public static final String TAG_TP_LOCAL_COML_DESTINO = "[#TPLCOMDESTINO]";

	public static final String TAG_AERO_ORIGEM = "[#AERORIGEM]";
	public static final String TAG_AERO_DESTINO = "[#AERODESTINO]";
	public static final String TAG_UF_ORIGEM = "[#UFORIGEM]";
	public static final String TAG_UF_DESTINO = "[#UFDESTINO]";
	public static final String TAG_PAIS_ORIGEM = "[#PAISORIGEM]";
	public static final String TAG_PAIS_DESTINO = "[#PAISDESTINO]";
	public static final String TAG_MUNICIPIO_ORIGEM = "[#MUNORIGEM]";
	public static final String TAG_MUNICIPIO_DESTINO = "[#MUNDESTINO]";
	public static final String TAG_FILIAL_ORIGEM = "[#FILORIGEM]";
	public static final String TAG_FILIAL_DESTINO = "[#FILDESTINO]";
	public static final String TAG_GRUPO_REG_ORIGEM = "[#GRPRORIGEM]";
	public static final String TAG_GRUPO_REG_DESTINO = "[#GRPRDESTINO]";

	public static final String TAG_COD_TARIFA = "[#CODTARIFAPRECO]";


	public static final String FORMATO_TAG_FAIXA_PROGRESSIVA = "(\\[#)([A-Z0-9_]+)(_FXP_)((TPINDCL)|(MINPROG)|(UNMED)|(PRDESP)|(VLFXPROG)|(PSMIN)|(FATMUL)|(TXFXPROG)|(KMEXPROG))(\\])";
	public static final String FORMATO_TAG_TIPO_IND_CALC = "(\\[#)([A-Z0-9_]+)(_FXP_TPINDCL\\])";
	public static final String FORMATO_TAG_MINIMO_PROGRESSIVO = "(\\[#)([A-Z0-9_]+)(_FXP_MINPROG\\])";
	public static final String FORMATO_TAG_UNIDADE_MEDIDA = "(\\[#)([A-Z0-9_]+)(_FXP_UNMED\\])";
	public static final String FORMATO_TAG_PRODUTO_ESPECIFICO = "(\\[#)([A-Z0-9_]+)(_FXP_PRDESP\\])";
	public static final String FORMATO_TAG_VALOR_FAIXA_PROGRESSIVA = "(\\[#)([A-Z0-9_]+)(_FXP_VLFXPROG\\])";
	public static final String FORMATO_TAG_PESO_MINIMO_FAIXA_PROGRESSIVA = "(\\[#)([A-Z0-9_]+)(_FXP_PSMIN\\])";
	public static final String FORMATO_TAG_FATOR_MULTIPLICACAO_FAIXA_PROGRESSIVA = "(\\[#)([A-Z0-9_]+)(_FXP_FATMULT\\])";
	public static final String FORMATO_TAG_VALOR_TAXA_FIXA_PROGRESSIVA = "(\\[#)([A-Z0-9_]+)(_FXP_TXFXPROG\\])";
	public static final String FORMATO_TAG_VALOR_KM_EXTRA = "(\\[#)([A-Z0-9_]+)(_FXP_KMEXPROG\\])";

	public static final String FORMATO_TAG_PRECO_FRETE = "(\\[#)([A-Z0-9_]+)(_PRF_)([A-Z0-9]*)(\\])";
	public static final String FORMATO_TAG_VALOR_PRECO_FRETE = "(\\[#)([A-Z0-9_]+)(_PRF_VLPRECOFRETE\\])";
	public static final String FORMATO_TAG_PESO_MINIMO_PRECO_FRETE = "(\\[#)([A-Z0-9_]+)(_PRF_PSMIN\\])";
	//tag de sessão de fixos
	public static final String FORMATO_TAG_GENERICO = "(\\[#)([A-Z]+)((_VLMIN)|(_PSMIN)|(_PSTAXADO)|(_VLEXCED))?(\\])";
	
	//tag de sessão de tabela
	public static final String FORMATO_TAG_TABELA = "(\\[#)((([A-Z0-9_]+)((_FXP)|(_PRF))(_)?([A-Z0-9]*))|(([A-Z]+)((ORIGEM)|(DESTINO)))|(CODTARIFAPRECO)|(TARIFAVSROTA))(\\])";
	
	//tags de arquivo
	public static final String FORMATO_TAG_FECHA_SESSAO = "(\\[#FIM)((TAB)|(FIXO))(\\])";
	public static final String FORMATO_TAG_ABRE_SESSAO = "(\\[#INI)((TAB)|(FIXO))(\\])";
	public static final String TAG_ABRE_TABELA = "[#INITAB]";
	public static final String TAG_FECHA_TABELA = "[#FIMTAB]";
	
	public static final String TAG_ABRE_FIXO = "[#INIFIXO]";
	public static final String TAG_FECHA_FIXO = "[#FIMFIXO]";
	
	public static final String TAG_FIM_ARQUIVO = "[#FIMARQ]";
	public static final String TAG_TARIFA_X_ROTA = "[#TARIFAVSROTA]";

	private TagsImportacaoTabelaPreco() {

	}

	public static boolean ehTag(String valor) {
		return Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG, valor);
	}

	public static boolean ehTagFechaSessao(String valorCelula) {
		return Pattern.matches(FORMATO_TAG_FECHA_SESSAO, valorCelula);
	}

	public static boolean ehTagAbreSessao(String valorCelula) {
		return Pattern.matches(FORMATO_TAG_ABRE_SESSAO, valorCelula);
	}
	
	public static boolean ehTagTabela(String valorCelula) {
		return Pattern.matches(FORMATO_TAG_TABELA, valorCelula);
	}

}
