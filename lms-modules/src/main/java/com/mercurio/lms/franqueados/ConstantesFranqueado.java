package com.mercurio.lms.franqueados;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class ConstantesFranqueado {

	public static final int LIMITE_COMMIT = 100;

	// TP_FRETE IN ('FE', 'CR', 'FR', 'CE', 'FL')
	public static final String CIF_EXPEDIDO = "CE";
	public static final String FOB_EXPEDIDO = "FE";
	public static final String CIF_RECEBIDO = "CR";
	public static final String FOB_RECEBIDO = "FR";
	public static final String FRETE_LOCAL = "FL";
	public static final String SERVICO_ADICIONAL = "SE";
	public static final String AEREO = "AE";

	public static final String TP_OPERACAO_FRETE_NACIONAL = "NA";
	public static final String TP_OPERACAO_LOCAL_ENTRE_FRANQUIAS = "EF";

	public static final String TP_PAVIMENTO_ASFALTO = "A";
	public static final String TP_PAVIMENTO_TERRA = "T";

	public static final String TP_SITUACAO_PENDENCIA_EM_APROVACAO = "E";
	public static final String TP_SITUACAO_PENDENCIA_APROVADO = "A";
	public static final String TP_SITUACAO_PENDENCIA_REPROVADO = "R";
	public static final String TP_SITUACAO_PENDENCIA_CANCELADO = "C";

	public static final String TP_APROVADO = "A";

	public static final String TP_MOTIVO_AV = "AV";
	public static final String TP_MOTIVO_OU = "OU";
	public static final String TP_MOTIVO_FA = "FA";

	public static final String CONTA_CONTABIL_SERVICO_REEMBARQUE = "SR";
	public static final String CONTA_CONTABIL_IRE = "IR";
	public static final String CONTA_CONTABIL_OVER_60 = "OV";
	public static final String CONTA_CONTABIL_INDENIZACOES = "IN";
	public static final String CONTA_CONTABIL_BDM = "BD";
	public static final String CONTA_CONTABIL_CREDITO_DIVERSOS = "CD";
	public static final String CONTA_CONTABIL_DEBITO_DIVERSOS = "DD";
	public static final String CONTA_CONTABIL_CREDITO_ANTECIPACAO = "CA";
	public static final String CONTA_CONTABIL_DEBITO_ANTECIPACAO = "DA";
	public static final String CONTA_CONTABIL_IA = "IA";
	public static final String CONTA_CONTABIL_IF = "IF";
	public static final String CONTA_CONTABIL_IO = "IO";

	public static final String TIPO_LANCAMENTO_DEBITO = "D";
	public static final String TIPO_LANCAMENTO_CREDITO = "C";

	public static final String PARAMETRO_FECHTO_MENSAL = "IND_FECHTO_MENSAL_FINANCEIRO";
	public static final String PARAMETRO_MIN_FRETE_CARRETEIRO = "PS_MIN_FRETE_CARRETEIRO";
	public static final String PARAMETRO_COMPETENCIA_FINANCEIRO = "COMPETENCIA_FINANCEIRO";
	public static final String PARAMETRO_COMPETENCIA_FRANQUEADO = "COMPETENCIA_FRANQUEADO";
	public static final String PARAMETRO_COMPETENCIA_FRANQUEADO_BPEL = "COMPETENCIA_FRANQUEADO_BPEL";
	public static final String PARAMETRO_BL_LIBERADO_FRANQUEADO_BPEL = "BL_LIBERADO_FRANQUEADO_BPEL";

	public static final String PARAMETRO_TOTAL_FRANQUEADOS_A_PROCESSAR = "TOTAL_FRANQUEADOS_A_PROCESSAR";

	public static final String PARAMETRO_REPROCESSA_FRANQUEADO = "IND_REPROCESSA_FRANQUEADO";
	public static final String PARAMETRO_REPROCESSA_INICIO_COMPETENCIA = "REPROCESSA_INICIO_COMPETENCIA";
	public static final String PARAMETRO_REPROCESSA_FIM_COMPETENCIA = "REPROCESSA_FIM_COMPETENCIA";

	public static final String PARAMETRO_OCORRENCIA_DOCUMENTO_CALCULO_PERMANENCIA = "DOCTO_EXCL_CALC_PERMA";

	public static final String TP_SIM = "S";
	public static final String TP_NAO = "N";

	public static final String TP_STATUS_INDENIZACAO_LIQUIDADO = "P";

	public static final String TP_MOTIVO_NC_FALTA_VOLUME = "FV";
	public static final String TP_MOTIVO_NC_AVARIA = "AV";

	public static final String TP_MOTIVO_INDENIZACAO_FRQ_AVARIA = "AV";
	public static final String TP_MOTIVO_INDENIZACAO_FRQ_OUTROS = "OU";
	public static final String TP_MOTIVO_INDENIZACAO_FRQ_FALTA = "FA";

	public static final String DM_MOTIVO_INDENIZACAO_FRQ = "DM_MOTIVO_INDENIZACAO_FRQ";
	public static final String DM_TP_CONTA_CONTABIL_FRQ = "DM_TP_CONTA_CONTABIL_FRQ";
	public static final String DM_TIPO_DOCUMENTO_SERVICO = "DM_TIPO_DOCUMENTO_SERVICO";
	public static final String DM_STATUS_WORKFLOW = "DM_STATUS_WORKFLOW";

	public static final String VALOR_MINIMO_PENDENCIA_FRQ = "VL_MINIMO_PENDENCIA_FRQ";
	public static final String PARTICIPACAO_FRETE_INTERNACIONAL = "IRE";

	public static final String FAIXA_1_APROV_CRED_FRQ = "1_FAIXA_APROV_CRED_FRQ";
	public static final String FAIXA_2_APROV_CRED_FRQ = "2_FAIXA_APROV_CRED_FRQ";

	public static final String TP_DOCUMENTO_SERVICO_CTR = "CTR";
	public static final String TP_DOCUMENTO_SERVICO_CTE = "CTE";
	public static final String TP_DOCUMENTO_SERVICO_NFT = "NFT";
	public static final String TP_DOCUMENTO_SERVICO_NTE = "NTE";
	public static final String TP_DOCUMENTO_SERVICO_NFS = "NFS";
	public static final String TP_DOCUMENTO_SERVICO_NSE = "NSE";
	public static final String TP_DOCUMENTO_SERVICO_NFE = "NFE";

	public static final String TP_COLETA_ENTREGA_REL_FRETE_LOCAL = "FL";
	public static final String TP_COLETA_ENTREGA_REL_COLETA = "CO";
	public static final String TP_COLETA_ENTREGA_REL_ENTREGA = "EN";

	public static final String IMPORTACAO_INPUT_EXT = "csv";
	public static final String IMPORTACAO_INPUT_TXT_EXT = "txt";
	public static final String IMPORTACAO_PROCESSANDO_EXT = "proc";
	public static final String IMPORTACAO_OK_EXT = "ok";
	public static final String IMPORTACAO_NOK_EXT = "nok";
	public static final String IMPORTACAO_ERR_EXT = "err";
	public static final int IMPORTACAO_MAX_FILES = 30;
	public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("ddMMyyyy");

	public static final String TP_PROCESSO_MENSAL = "MENSAL";
	public static final String TP_PROCESSO_DIARIO = "DIARIO";
	public static final String TP_PROCESSO_SIMULACAO = "SIMULACAO";
	public static final String LOG_INICIO_PROCESSO = "INICIO_PROCESSO";
	public static final String LOG_FIM_PROCESSO = "FIM_PROCESSO";
	public static final String LOG_INICIO_CALCULO_FRANQUEADO = "Início do cálculo do franqueado";
	public static final String LOG_INICIO_CALCULO_SERVICOS_ADICIONAIS = "Início do cálculo de Serviços Adicionais";
	public static final String LOG_INICIO_CALCULO_BASE_CALCULO = "INÍCIO DO CÁLCULO DA BASE DE CÁLCULO";
	public static final String LOG_BUSCA_DOCUMENTOS_CIFFOB = "Busca de documentos CIF-FOB por franquia";
	public static final String LOG_BUSCA_DOCUMENTOS_LOCAL = "Busca de documentos Local por franquia";

	public static final String LOG_INICIO_BDM = "Inicio do cálculo BDM";
	public static final String LOG_INICIO_RECALCULO = "Início do Recálculo";
	public static final String LOG_INICIO_REEMBARQUE = "Início do cálculo de Reembarque";

	public static final String LOG_INICIO_GRAVACAO_DOCUMENTOS = "Início de gravação de documentos";
	public static final String LOG_INICIO_CALCULO_PARTICIPACAO_BASICA = "Início do cálculo de Participação Básica";
	public static final String LOG_INICIO_CALCULO_PARTICIPACAO = "Início do cálculo de Participação";
	public static final String LOG_INICIO_CALCULO_PARTICIPACAO_TOTAL = "Início do cálculo de Participação Total";
	public static final String LOG_INICIO_CALCULO_PARTICIPACAO_FINAL = "Início do cálculo de Participação Final";
	
	public static final String CONTRATO_MOTORISTA_MODELO_1 = "1";
	public static final String CONTRATO_MOTORISTA_MODELO_2 = "2";
	

	private ConstantesFranqueado() {
		super();
	}

}
