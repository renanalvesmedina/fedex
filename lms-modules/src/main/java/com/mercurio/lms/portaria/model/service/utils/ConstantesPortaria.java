package com.mercurio.lms.portaria.model.service.utils;

import com.mercurio.adsm.core.util.VMProperties;

import br.com.tntbrasil.integracao.domains.jms.Queues;

/**
 * LMSA-778 - Constantes para processo "Informar Chegada na Portaria".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public final class ConstantesPortaria {

	public static final String TP_VIAGEM = "V";
	public static final String TP_COLETA_ENTREGA = "C";
	public static final String TP_ORDEM_SAIDA = "O";
	public static final String TP_TERCEIRO = "T";

	public static final String PARAMETRO_CD_PEND_BLOQ_LOCALIZACAO = "CD_PEND_BLOQ_LOCALIZACAO";
	public static final String PARAMETRO_BL_PORT_CHEGADA_NOVO = "BL_PORT_CHEGADA_NOVO";
	public static final String PARAMETRO_BL_PORT_SAIDA_NOVO = "BL_PORT_SAIDA_NOVO";
	public static final String PARAMETRO_REMETENTE_EMAIL_LMS = "REMETENTE_EMAIL_LMS";

	public static final String TP_STATUS_AGUARDANDO_SAIDA_PARA_VIAGEM = "AV";
	public static final String TP_STATUS_AGUARDANDO_DESCARGA = "AD";
	public static final String TP_STATUS_EM_VIAGEM = "EV";
	public static final String TP_STATUS_PARADA_OPERACIONAL = "PO";
	public static final String TP_STATUS_EM_TRANSITO_COLETA = "TC";
	public static final String TP_STATUS_EMITIDO = "ME";
	public static final String TP_STATUS_EM_ESCALA_NA_FILIAL = "EF";
	public static final String TP_STATUS_EM_TRANSITO_PARA_O_PORTO = "TP";
	public static final String TP_STATUS_COLETA_EXECUTADA = "EX";
	public static final String TP_STATUS_CANCELADO = "CA";

	public static final String TP_MANIFESTO_ENTREGA_DIRETA = "ED";
	public static final String TP_MANIFESTO_ENTREGA = "EN";
	public static final String TP_MANIFESTO_ENTREGA_PARCEIRA = "EP";

	public static final String TP_MODAL_RODOVIARIO = "R";
	public static final String TP_MODAL_AEREO = "A";

	public static final String TP_PEDIDO_COLETA_AEROPORTO = "AE";

	public static final Short CD_LOCALIZACAO_ENTREGA_REALIZADA = 1;
	public static final Short CD_LOCALIZACAO_COLETA_REALIZADA_NO_AEROPORTO = 101;
	public static final Short CD_LOCALIZACAO_ROTA_DE_ENTREGA_DIRETA_AO_CLIENTE = 103;

	public static final String TP_OCORRENCIA_ENTREGUE = "E";
	public static final String TP_OCORRENCIA_ENTREGUE_AEROPORTO = "A";

	public static final String TP_SITUACAO_ATIVO = "A";

	public static final Short CD_OCORRENCIA_ENTREGA_REALIZADA_NORMALMENTE = 1;

	public static final Short CD_FASE_PROCESSO_NO_TERMINAL = 1;

	public static final Short CD_EVENTO_EM_VIAGEM = 55;
	public static final Short CD_EVENTO_MERCADORIA_RETORNADA = 57;
	public static final Short CD_EVENTO_EM_ESCALA_NA_FILIAL = 58;
	public static final Short CD_EVENTO_PREVISAO_DE_CHEGADA = 66;
	public static final Short CD_EVENTO_AGUARDANDO_DESCARGA = 130;
	public static final Short CD_EVENTO_SEM_LOCALIZACAO_MERCADORIA = 152;
	public static final Short CD_EVENTO_ENTREGA_PARCIAL = 102;

	public static final String TP_SITUACAO_A_DISPOSICAO_DA_FROTA = "ADFR";
	public static final String TP_SITUACAO_AGUARDANDO_DESCARGA = "AGDE";
	public static final String TP_SITUACAO_EM_VIAGEM = "EVPA";
	public static final String TP_SITUACAO_PARADA_OPERACIONAL = "PAOP";

	public static final String TP_EVENTO_CHEGADA_NA_PORTARIA = "CP";
	public static final String TP_EVENTO_SAIDA_NA_PORTARIA = "SP";

	public static final String TP_DOCUMENTO_CONTROLE_CARGA = "CCA";
	public static final String TP_DOCUMENTO_CONHECIMENTO_NACIONAL = "CTR";
	public static final String TP_DOCUMENTO_CONHECIMENTO_ELETRONICO = "CTE";

	public static final String TP_SCAN_LMS = "LM";

	public static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();

	public static final Queues QUEUE_MAIL_SENDER_SERVICE = Queues.MAIL_SENDER_SERVICE_FEDEX;

	public static final String TP_CONTATO_AEREO = "AE";

	public static final String MENSAGEM_LOGISTICS_MANAGEMENT_SYSTEM = "logisticsManagementSystem";
	public static final String MENSAGEM_PRE_ALERTA_EMBARQUE = "preAlertaEmbarqueCargaAerea";
	public static final String MENSAGEM_REFERENTE_MANIFESTO = "referenteManifesto";
	public static final String MENSAGEM_TEXTO_EMAIL_MANIFESTO = "textoEmailManifestoAereoTabela";
	public static final String MENSAGEM_DATA = "data";
	public static final String MENSAGEM_DOCUMENTO = "documento";
	public static final String MENSAGEM_DPE = "dpe";
	public static final String MENSAGEM_VOLUMES = "volumes";
	public static final String MENSAGEM_PESO_KG = "pesoKG";
	public static final String MENSAGEM_SERVICO = "servico";
	public static final String MENSAGEM_AVISO_EMAIL_MANIFESTO = "avisoEmailManifestoAereo";
	
	public static final String TIPO_CHEGADA = "CHEGADA";
	public static final String TIPO_SAIDA = "SAIDA";
	
	public static final String SUBTIPO_VIAGEM = "VIAGEM";
	public static final String SUBTIPO_COLETA_ENTREGA = "COLETAENTREGA";
	public static final String SUBTIPO_ORDEM_SAIDA = "ORDEMSAIDA";
	
	public static final String SIM = "S";

	private ConstantesPortaria() {
		throw new AssertionError();
	}

}
