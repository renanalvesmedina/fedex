/**
 * 
 */
package com.mercurio.lms.tabelaprecos.util;

/**
 * Define constantes para serem utilizadas no módulo tabela de precos.
 * 
 * @author Luis Carlos Poletto
 *
 */
public interface ConstantesTabelaPrecos {
	
	String TABELA_PRECO_IN_SESSION          = "TABELA_PRECO_IN_SESSION";
	String GENERALIDADES_IN_SESSION         = "GENERALIDADES_IN_SESSION";
	String TAXAS_IN_SESSION                 = "TAXAS_IN_SESSION";
	String AEROPORTOS_IN_SESSION            = "AEROPORTOS_IN_SESSION";
	String PRODUTOS_ESPECIFICOS_IN_SESSION  = "PRODUTOS_ESPECIFICOS_IN_SESSION";
	String REAJUSTES_ESPECIFICOS_IN_SESSION = "REAJUSTES_ESPECIFICOS_IN_SESSION";
	
	String TP_REAJUSTE_TARIFA_MINIMA    = "M";
	String TP_REAJUSTE_GERAL            = "G";
	String TP_REAJUSTE_ESPECIFICO       = "E";
	String TP_REAJUSTE_GERAL_ESPECIFICO = "X";
	String TP_REAJUSTE_GERAL_MINIMO     = "Y";
	String TP_REAJUSTE_TODOS            = "T";

	String TP_INDICADOR_DESCONTO_COTACAO = "C";
	String TP_INDICADOR_DESCONTO_PARAMETRIZACAO = "P";

	String TP_TABELA_PRECO_REFERENCIAL_TNT  = "T";
	String TP_TABELA_PRECO_DIFERENCIADA = "D";
	String SUB_TP_TABELA_PRECO_REFERENCIAL_TNT = "X";
	String SUB_TP_TABELA_FOB = "F";

	String TP_SERVICO_PROXIMO_DIA = "P";
	String TP_SERVICO_PROXIMO_VOO = "V";

}
