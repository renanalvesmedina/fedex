package com.mercurio.lms.configuracoes;

public interface ConstantesConfiguracoes {

	String CD_COORDENADOR_VENDAS = "003.25";
	String CD_GERENTE_COMERCIAL = "002.06";
	String CD_GERENTE_FILIAL = "002.10";
	String CD_GERENTE_REGIONAL = "002.34";
	String CD_DIRETOR_COMERCIAL = "001.04";
	String CD_DIRETOR_REGIONAL = "001.09";

	//Especializacoes de pessoa
	Short IS_EMPRESA = Short.valueOf("1");
	Short IS_FILIAL = Short.valueOf("2");
	Short IS_AEROPORTO = Short.valueOf("4");
	Short IS_FILIAL_CIA_AEREA = Short.valueOf("8");
	Short IS_CON_POSTO_PASSAGEM = Short.valueOf("16");
	Short IS_PROPRIETARIO = Short.valueOf("32");
	Short IS_MOTORISTA = Short.valueOf("64");
	Short IS_BENEFICIARIO = Short.valueOf("128");
	Short IS_OPERADORA_MCT = Short.valueOf("256");
	Short IS_EMPRESA_COBRANCA = Short.valueOf("512");
	Short IS_DESPACHANTE = Short.valueOf("1024");
	Short IS_CLIENTE = Short.valueOf("2048");
	Short IS_CLIENTE_ESPECIAL = Short.valueOf("4096");
	Short IS_MOTORISTA_EVENTUAL = Short.valueOf("8192");

	//Bancos
	Short COD_BANRISUL = Short.valueOf("41");
	Short COD_HSBC = Short.valueOf("399");
	Short COD_BRADESCO = Short.valueOf("237");
	Short COD_ITAU = Short.valueOf("341");

	String PARAM_GERAL_BANRISUL = "ID_OCORR_REMESSA_BANRISUL";
	String PARAM_GERAL_HSBC = "ID_OCORR_REMESSA_HSBC";
	String PARAM_GERAL_BRADESCO = "ID_OCORR_REMESSA_BRADESCO";
	String PARAM_GERAL_ITAU = "ID_OCORR_REMESSA_ITAU";

	//Ocorrencia Banco
	
	Short CD_OCORRENCIA_PEDIDO_ENVIO = Short.valueOf("01");
	Short CD_OCORRENCIA_PEDIDO_BAIXA = Short.valueOf("02");
	Short CD_OCORRENCIA_CANCELAMENTO_ABATIMENTO = Short.valueOf("05");
	Short CD_OCORRENCIA_ALTERACAO_VENCIMENTO = Short.valueOf("06");
	Short CD_OCORRENCIA_PEDIDO_PROTESTO = Short.valueOf("09");
	Short CD_OCOR_BAIXAR_PROTESTO_BRADESCO =  Short.valueOf("08");
	Short CD_OCORRENCIA_ALTERAR_CEP = Short.valueOf("31");
	Short CD_OCORRENCIA_RETRANSMITIR = Short.valueOf("98");

	Short NBR_ISO_BR_REAL = Short.valueOf("986");
	Short NBR_ISO_US_DOLLAR = Short.valueOf("840");
	
	String TP_TELEFONE_COMERCIAL = "C";
	
	//Contatos
	String TP_CONTATO_OPERACIONAL = "CO";
	String TP_CONTATO_COBRANCA = "CB";
	String TP_CONTATO_GERENTE = "GE";
	
//	TipoSituacaoTributacao
	String TP_SITUACAO_TRIBUTARIA_NAO_CONTRIBUINTE = "NC";
	String TP_SITUACAO_TRIBUTARIA_ORGAO_PUBLICO_NAO_CONTRIBUINTE = "ON";
	String TP_SITUACAO_TRIBUTARIA_PRODUTOR_RURAL_NAO_CONTRIBUINTE = "PN";
	String TP_SITUACAO_TRIBUTARIA_ME_EPP_SIMPLES_NAO_CONTRIBUINTE = "MN";
	String TP_SITUACAO_TRIBUTARIA_CIA_MISTA_NAO_CONTRIBUINTE = "CN";
	
}
