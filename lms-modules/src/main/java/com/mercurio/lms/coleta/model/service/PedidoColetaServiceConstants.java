package com.mercurio.lms.coleta.model.service;

public interface PedidoColetaServiceConstants {

	public static final String CONTROLE_CARGA = "CCA";
	public static final String STATUS_MAN_EM_TRANS_COL_ENT = "TC";
	public static final String COLETA = "PED";
	public static final String EVENTO_COLETA_EXECUCAO = "EX";
	public static final String EVENTO_COLETA_SOLICITACAO = "SO";
	public static final String EVENTO_COLETA_LIBERACAO = "LI";
	public static final String MODO_PEDIDO_COLETA_BALCAO = "BA";
	public static final String TP_MODAL_AEREO = "A";
	public static final String WORKFLOW_APROVADO = "A";
	public static final String WORKFLOW_EM_APROVACAO = "E";
	public static final int PROJECTION_BL_PRODUTO_PERIGOSO = 11;
	public static final int PROJECTION_TP_MODO_PEDIDO_COLETA = 9;
	public static final int PROJECTION_BL_PERIGOSO = 8;
	public static final int PROJECTION_BL_PRIORIZAR = 7;
	public static final int PROJECTION_NR_IDENTIFICACAO = 6;
	public static final int PROJECTION_TP_IDENTIFICACAO = 5;
	public static final int PROJECTION_NM_PESSOA = 4;
	public static final int PROJECTION_DS_ROTA = 3;
	public static final int PROJECTION_NR_COLETA = 2;
	public static final int PROJECTION_SG_FILIAL = 1;
	public static final int PROJECTION_ID_PEDIDO_COLETA = 0;
	public static final String LOGIN_USUARIO_BATCH = "rbt.lmsbatch";
	public static final String TP_MODO_PEDIDO_COLETA = "tpModoPedidoColeta";
	public static final String BL_PERIGOSO = "blPerigoso";
	public static final String BL_PRIORIZAR = "blPriorizar";
	public static final String NR_IDENTIFICACAO = "nrIdentificacao";
	public static final String TP_IDENTIFICACAO = "tpIdentificacao";
	public static final String BL_PRODUTO_PERIGOSO = "blProdutoPerigoso";
	public static final String NM_PESSOA = "nmPessoa";
	public static final String DS_ROTA = "dsRota";
	public static final String NR_COLETA = "nrColeta";
	public static final String SG_FILIAL = "sgFilial";
	public static final String ID_PEDIDO_COLETA = "idPedidoColeta";
	
	public static final String TP_LOCALIZACAO_AWB_RETIRADO_AEROPORTO = "RE";
	public static final String TP_LOCALIZACAO_AWB_RETIRADA_PARCIAL_AEROPORTO = "RP";

	public static final String TP_EVENTO_COLETA_CANCELADA = "CA";
	public static final String TP_EVENTO_COLETA_EXECUCAO = EVENTO_COLETA_EXECUCAO;
	public static final String TP_EVENTO_COLETA_RETORNO = "RC";
	public static final String TP_EVENTO_COLETA_TRANSMITIDA = "TR";
	public static final String TP_STATUS_COLETA_ABERTA = "AB";
	public static final String TP_STATUS_COLETA_EXECUTADA = EVENTO_COLETA_EXECUCAO;
	public static final String TP_STATUS_COLETA_TRANSMITIDA = "TR";
	public static final String TP_STATUS_COLETA_RETORNO = "RC";
	public static final String TP_PEDIDO_COLETA_AEREO = "AE";
	
	public static final String NR_PRE_MANIFESTO = "NR_PRE_MANIFESTO";
	public static final String TP_STATUS_AWB_PRE = "P";
	
	/** Localização Mercadoria */
	public static final Short CD_AGUARDANDO_EMBARQUE_CIA_AEREA = 55;
	public static final Short CD_EM_VIAGEM_AEREA = 2;
	
	/** Eventos*/
	public static final Short CD_COLETA_REALIZADA_AEROPORTO = 17;
	public static final Short CD_PEDIDO_COLETA_EMITIDO = 400;
	public static final Short CD_EM_ROTA_ENTREGA_DIRETA = 403;
	public static final Short CD_EVENTO_PRE_MANIFESTO = 62;
	public static final int INCLUSAO_NORMAL = 0;
	public static final int INCLUSAO_PARCIAL = 1;
	public static final int BLOQUEAR_INCLUSAO = 2;
	
	public static final int ADICIONA_VALORES = 1;
}
