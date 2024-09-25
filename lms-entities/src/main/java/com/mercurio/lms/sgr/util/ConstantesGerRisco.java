package com.mercurio.lms.sgr.util;

/**
 * LMS-6850 - Constantes para Plano de Gerenciamento de Riscos.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public final class ConstantesGerRisco {

	// CLIENTE_ENQUADRAMENTO.TP_INTEGRANTE_FRETE
	public static final String TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO = "A";
	public static final String TP_INTEGRANTE_FRETE_REMETENTE = "R";
	public static final String TP_INTEGRANTE_FRETE_DESTINATARIO = "D";

	// ENQUADRAMENTO_REGRA.TP_ABRANGENCIA
	public static final String TP_ABRANGENCIA_NACIONAL = "N";
	public static final String TP_ABRANGENCIA_INTERNACIONAL = "I";

	// ENQUADRAMENTO_REGRA.TP_OPERACAO
	public static final String TP_OPERACAO_COLETA = "C";
	public static final String TP_OPERACAO_ENTREGA = "E";
	public static final String TP_OPERACAO_VIAGEM = "V";

	// ENQUADRAMENTO_REGRA.TP_CRITERIO_ORIGEM
	// ENQUADRAMENTO_REGRA.TP_CRITERIO_DESTINO
	public static final String TP_CRITERIO_FILIAL = "F";
	public static final String TP_CRITERIO_MUNICIPIO = "M";
	public static final String TP_CRITERIO_UNIDADE_FEDERATIVA = "U";
	public static final String TP_CRITERIO_PAIS = "P";

	// MUNICIPIO_ENQUADRAMENTO.TP_ORIGEM_DESTINO
	// PAIS_ENQUADRAMENTO.TP_ORIGEM_DESTINO
	// UF_ENQUADRAMENTO.TP_ORIGEM_DESTINO
	// FILIAL_ENQUADRAMENTO.TP_ORIGEM_DESTINO
	public static final String TP_CRITERIO_ORIGEM = "O";
	public static final String TP_CRITERIO_DESTINO = "D";

	// MANIFESTO.TP_MANIFESTO
	public static final String TP_MANIFESTO_COLETA_ENTREGA = "E";

	// PEDIDO_COLETA.TP_PEDIDO_COLETA
	public static final String TP_PEDIDO_COLETA_AEROPORTO = "AE";

	// EXIGENCIA_GER_RISCO.TP_CRITERIO_AGRUPAMENTO
	public static final String TP_AGRUPAMENTO_ACUMULADO = "A";
	public static final String TP_AGRUPAMENTO_MAIOR_QUANTIDADE = "M";

	// EXIGENCIA_GER_RISCO.TP_SITUACAO
	// TIPO_EXIGENCIA_GER_RISCO.TP_SITUACAO
	public static final String TP_SITUACAO_ATIVO = "A";
	public static final String TP_SITUACAO_INATIVO = "I";

	// TIPO_EXIGENCIA_GER_RISCO.TP_EXIGENCIA
	public static final String TP_EXIGENCIA_ESCOLTA = "ES";
	public static final String TP_EXIGENCIA_MONITORAMENTO = "MN";
	public static final String TP_EXIGENCIA_MOTORISTA = "MO";
	public static final String TP_EXIGENCIA_VEICULO = "VE";
	public static final String TP_EXIGENCIA_AVERBACAO = "AV";
	public static final String TP_EXIGENCIA_VIRUS = "VI";
	
	//SMP GR
	public static final String TP_STATUS_SMP_GR_NAO_ENVIADA = "NE";
	public static final String TP_STATUS_SMP_GR_ENVIADA = "EN";
	public static final String TP_STATUS_SMP_GR_INCLUIDA = "IN";
	public static final String TP_STATUS_SMP_GR_ERRO_INSERIR = "EI";
	public static final String TP_STATUS_SMP_GR_FINALIZACAO_ENVIADA = "FE";
	public static final String TP_STATUS_SMP_GR_FINALIZADA = "FI";
	public static final String TP_STATUS_SMP_GR_ERRO_FINALIZAR = "EF";
	public static final String TP_STATUS_SMP_GR_CANCELAMENTO_ENVIADO = "CE";
	public static final String TP_STATUS_SMP_GR_SMP_CANCELADA = "CA";
	public static final String TP_STATUS_SMP_GR_SMP_ERRO_CANCELAR_SMP = "EC";
	public static final String TP_STATUS_SMP_GR_SMP_REJEITADA = "RE";
	public static final String TP_STATUS_SMP_GR_SMP_EM_MONITORAMENTO = "EM";
	
	//SMP 
	public static final String TP_STATUS_SMP_CANCELADA = "CA";
	public static final String TP_STATUS_SMP_FINALIZADA = "FI";
	
	//VERSAO APISUL
	public static final String SGR_GER_RIS_VERS_1 = "APISUL_1.0";
	public static final String SGR_GER_RIS_VERS_2 = "APISUL_2.0";			
	public static final String SGR_GER_RIS_AMBOS = "APISUL_AMBOS";			
	public static final String SGR_VERS_INT_GER_RIS = "SGR_VERS_INT_GER_RIS";
	
	//EVENTO SMP
	public static final String EVENTO_SMP_APROVADA = "1000";
	public static final String EVENTO_SMP_REJEITADA = "1031";
	
	//codigo de erro do lado do LMS como erro inserir smp e erro ao inserir na fila de integração
	public static final String CODIGO_ERRO_LMS= "0000";
	
	//mostra mensagem quando erro no popular insere smp
	public static final String ERRO_INSERIR_SMP = "LMS-11354";
	
	//mostra mensagem quando erro no inserir na fila smp
	public static final String ERRO_INSERIR_SMP_FILA = "LMS-11355";
	
	
	
	//PARAMETRO DATA VIAGEM PRA ENVIAR PARA APISUL 
	public static final String SGR_DIFERENCA_PHR_SMP = "SGR_DIFERENCA_PHR_SMP";
	public static final String SGR_MIN_ACRESC_DATA_S_SMP = "SGR_MIN_ACRESC_DATA_S_SMP";
	public static final String SGR_MIN_ACRESC_DATA_S_SMP_2 = "SGR_MIN_ACRESC_DATA_S_SMP_2";
	public static final String SGR_MIN_ACRESC_DATA_S_SMP_3 = "SGR_MIN_ACRESC_DATA_S_SMP_3";
	//PARAMETRO DATA COLETA ENTREGA PRA ENVIAR PARA APISUL 
	public static final String SGR_MIN_ACRESC_DATA_S_SMP_4 = "SGR_MIN_ACRESC_DATA_S_SMP_4";
	
	
	//PARAMETRO TEMPO PERMANENCIA VIAGEM PRA ENVIAR PARA APISUL 
	public static final String SGR_SMP_TEMPO_PERMANENCIA_VIAGEM  = "SGR_SMP_TEMPO_PERMANENCIA_VIAGEM";
	//PARAMETRO TEMPO PERMANENCIA COLETA ENTREGA PRA ENVIAR PARA APISUL	
	public static final String SGR_SMP_TEMPO_PERMANENCIA_COL_ENT = "SGR_SMP_TEMPO_PERMANENCIA_COL_ENT";
	
	//PARAMETRO VELOCIDADE PARA CALCULAR DATA VIAGEM PRA ENVIAR PARA APISUL 
	public static final String VEL_MEDIA = "ME";
	public static final String VEL_MT_MEDIO = "VEL_MT_MEDIO";
	public static final String VEL_PESADO = "PE";
	public static final String VEL_MT_PESADO = "VEL_MT_PESADO";
	public static final String VEL_LEVE = "LE";
	public static final String VEL_MT_LEVE = "VEL_MT_LEVE";
	public static final String VEL_LEVE_LEVE = "LL";
	public static final String VEL_MT_LEVE_LEVE = "VEL_MT_LEVE_LEVE";
	public static final String VEL_LEVE_MEDIO = "LM";
	public static final String VEL_MT_LEVE_MEDIO = "VEL_MT_LEVE_MEDIO";
	
	//TIPO CONTROLE CARGA PARA ENVIAR PARA APISUL
	public static final String VIAGEM_EXPRESSA = "VE";
	public static final String EXPRESSA = "EX";
	public static final String COLETA = "C";
	public static final String ENTREGA = "E";
	public static final String VIAGEM = "V";
	
	//PARAMETRO PARA PEGAR CNPJ PRA ENVIAR PARA APISUL 
	public static final String SGR_CNPJ_MATRIZ = "SGR_CNPJ_MATRIZ_TNT";
	public static final String SGR_CD_FABRICANTE_ISCAS = "CODIGO_FABRICANTE_ISCAS";
	
	public static final short SGR_TP_COMUNICACAO_SATELITAL = 2;
	public static final short SGR_TP_COMUNICACAO_CELULAR = 1;
	
	
	/**
	 * Retorna representação {@link String} descritiva para tipo de abrangência:
	 * <ul>
	 * <li>Para {@value #TP_ABRANGENCIA_NACIONAL} retorna {@code "NACIONAL"};
	 * <li>Para {@value #TP_ABRANGENCIA_INTERNACIONAL} retorna
	 * {@code "INTERNACIONAL"};
	 * <li>Caso contrário retorna {@code null}.
	 * </ul>
	 * 
	 * @param tpAbrangencia
	 *            Código para tipo de abrangência.
	 * @return {@link String} referente ao tipo de abrangência, ou {@code null}
	 *         para tipo inválido.
	 */
	public static String stringTpAbrangencia(String tpAbrangencia) {
		if (TP_ABRANGENCIA_NACIONAL.equals(tpAbrangencia)) {
			return "NACIONAL";
		}
		if (TP_ABRANGENCIA_INTERNACIONAL.equals(tpAbrangencia)) {
			return "INTERNACIONAL";
		}
		return null;
	}

	/**
	 * Retorna representação {@link String} descritiva para tipo de operação:
	 * <ul>
	 * <li>Para {@value #TP_OPERACAO_COLETA} retorna {@code "COLETA"};
	 * <li>Para {@value #TP_OPERACAO_ENTREGA} retorna {@code "ENTREGA"};
	 * <li>Para {@value #TP_OPERACAO_VIAGEM} retorna {@code "VIAGEM"};
	 * <li>Caso contrário retorna {@code null}.
	 * </ul>
	 * 
	 * @param tpOperacao
	 *            Código para tipo de operação.
	 * @return {@link String} referente ao tipo de operação, ou {@code null}
	 *         para tipo inválido.
	 */
	public static String stringTpOperacao(String tpOperacao) {
		if (TP_OPERACAO_COLETA.equals(tpOperacao)) {
			return "COLETA";
		}
		if (TP_OPERACAO_ENTREGA.equals(tpOperacao)) {
			return "ENTREGA";
		}
		if (TP_OPERACAO_VIAGEM.equals(tpOperacao)) {
			return "VIAGEM";
		}
		return null;
	}

	private ConstantesGerRisco() {
		throw new AssertionError();
	}

}
