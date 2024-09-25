/**
 * 
 */
package com.mercurio.lms.workflow.util;

/**
 * @author Luis Carlos Poletto
 *
 */
public interface ConstantesWorkflow {
	public String APROVADO = "A";
	public String REPROVADO = "R";
	public String CANCELADO = "C";
	public String EM_APROVACAO = "E";
	
	public Short NR_APROVACAO_PROPOSTA = Short.valueOf("3001");
	
	// LMS-5261 - Aprovaï¿½ï¿½o de carregamento com documentos jï¿½ manifestados para o mesmo destino. (0502)
	public Short NR_APROVACAO_DOCTO_MANIFESTADO = Short.valueOf("0502");
	
//CONTAS A RECEBER
		/* Cadastro das chamadas workFlow do modulo contasReceber
		 * @author Diego Umpierre
		 * @since 06/07/2006 */
		

	/** Desconto rodoviï¿½rio internacional. (3601)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3601_DESCONTO_RODO_INTERN = Short.valueOf("3601");
	
	/** Desconto atï¿½ 30%. (3602)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3602_DESCONTO_ATE_30 = Short.valueOf("3602");

	/** Desconto acima de 30% atï¿½ 40%. (3603)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3603_DESCONTO_DE_30_40 = Short.valueOf("3603");
	
	/** Desconto acima de 40% atï¿½ 45%. (3603)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3604_DESCONTO_DE_40_45 = Short.valueOf("3604");
	
	/** Desconto geral. (3605)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3605_DESCONTO_GERAL_100 = Short.valueOf("3605");

	/** Alteraï¿½ï¿½o de cotaï¿½ï¿½o do dï¿½lar na fatura. (3606)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3606_ALT_COT_DOL_FAT = Short.valueOf("3606");	
	
	/** Devoluï¿½ï¿½o de cheque nï¿½o cobrado para a Matriz. (3607)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3607_DEVOLUCAO_CHEQUE_MTZ = Short.valueOf("3607");
	
	/** Protestar boleto. (3608)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3608_PROT_BOL = Short.valueOf("3608");	
	
	/** Prorrogar o vencimento de boleto. (3609)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3609_PRORRO_VENC_BOL = Short.valueOf("3609");	
	
	/** Cancelar boleto em banco. (3610)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3610_CANCEL_BOLE_BCO = Short.valueOf("3610");
	
	/** Inclusï¿½o de Prï¿½-Fatura. (3611)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3611_INCL_PRE_FAT = Short.valueOf("3611");	

	/** Cancelamento de Nota de Dï¿½bito Nacional. (3613)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3613_CANCEL_NT_DEB_NAC = Short.valueOf("3613");
	
	/** Cancelar recibo. (3614)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3614_CANC_RECB = Short.valueOf("3614");	
	
	/** Cancelamento de recibo de desconto. (3615)
	 * @author Diego Umpierre 
	 * @since 06/07/2006 */
	public Short NR3615_CANC_RECB_DESC = Short.valueOf("3615");
	
	/** Redeco de desconto atï¿½ 500,00. (3616)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3616_RED_DESC_PEQ = Short.valueOf("3616");	
	
	/** Redeco de desconto atï¿½ 1.500,00. (3617)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3617_RED_DESC_MED = Short.valueOf("3617");	

	/** Redeco de desconto maior que 1.500,00. (3618)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3618_RED_DESC_GRD = Short.valueOf("3618");	
	
	/** Redeco de Lucros e Perdas (3619)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3619_RED_LCR_PRD = Short.valueOf("3619");
	
	/** Redeco de Lucros e Perdas para cliente (3620)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3620_RED_LCR_PRD_CLT = Short.valueOf("3620");
	
	/** Redeco de mercadoria (3621)
	 * @author Mickaï¿½l Jalbert
	 * @since 11/07/2006 */
	public Short NR3621_RED_MERC = Short.valueOf("3621");		
	
	/** Desconto atï¿½ 30% na fatura (3625)
	 * @author Mickaï¿½l Jalbert
	 * @since 20/11/2006 */
	public Short NR3625_FAT_ATE_30 = Short.valueOf("3625");	
	
	/** Desconto acima de 30% atï¿½ 40% na fatura (3626)
	 * @author Mickaï¿½l Jalbert
	 * @since 20/11/2006 */
	public Short NR3626_FAT_DE_30_40 = Short.valueOf("3626");	
	
	/** Desconto acima de 30% atï¿½ 45% na fatura (3627)
	 * @author Mickaï¿½l Jalbert
	 * @since 20/11/2006 */
	public Short NR3627_FAT_DE_40_45 = Short.valueOf("3627");		
	
	/** Desconto acima de 25% na fatura (3628)
	 * @author Mickaï¿½l Jalbert
	 * @since 20/11/2006 */
	public Short NR3628_FAT_GERAL = Short.valueOf("3628");		
	
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de municipios*/
	
	/** Aprova abertura de filial
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2901_ABR_FILIAL = Short.valueOf("2901");
	
	/** Confirma substituiï¿½ï¿½o do atendimento do municï¿½pio
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2902_SUBS_ATEND = Short.valueOf("2902");
	
	/** Confirma fechamento da filial
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2903_FEC_FILIAL = Short.valueOf("2903");
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de contrataï¿½ï¿½o de meio de transporte*/
	
	/** Aprovaï¿½ï¿½o solicitaï¿½ï¿½o coleta
	 *  @author Daniel Tavares
	 *  @since 17.04.2009
	 */
	public Short NR2600_APRV = Short.valueOf("2600");
	
	/** Definiï¿½ï¿½o do valor mï¿½ximo da contrataï¿½ï¿½o - Viagem
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2601_VLR_CONTRV = Short.valueOf("2601");
	
	/** Definiï¿½ï¿½o do valor mï¿½ximo da contrataï¿½ï¿½o - coleta/entrega
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	
	public Short NR2608_VLR_CONTRP = Short.valueOf("2608");
	
	public Short NR2602_VLR_CONTRCE = Short.valueOf("2602");
	
	
	/** Aviso de retorno da solicitaï¿½ï¿½o de contrataï¿½ï¿½o
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2603_AVI_RET_CONTR = Short.valueOf("2603");
	
	/** Nï¿½mero de liberaï¿½ï¿½o da reguladora prestes a vencer
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2604_LIBRE_VENCER = Short.valueOf("2604");
	
	/** Aviso de criaï¿½ï¿½o da solicitaï¿½ï¿½o de contrataï¿½ï¿½o
	 *  @author Andresa Vargas
	 *  @since 24/10/2006
	 */
	public Short NR2605_AVI_CRIA_CONTR = Short.valueOf("2605");
	
	/** Aviso de criaï¿½ï¿½o da chave de liberaï¿½ï¿½o
	 *  @author Tiago Sauter Lauxen
	 *  @since 17/01/2007
	 */
	public Short NR2606_AVI_CHAVE_LIBER = Short.valueOf("2606");
	
	/** Aviso de descarga para o trecho
	 *  @author Daniel Finger Tavares
	 *  @since 19/04/2010
	 */
	public Short NR2607_AVI_DESCARGA_TRECHO = Short.valueOf("2607");
	
	
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de frete carreteiro coleta/entrega*/
	
	/** Confirmaï¿½ï¿½o da tabela de preï¿½os do carreteiro de coleta/entrega
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2501_VLR_CARTCE = Short.valueOf("2501");
	
	/** Aprovaï¿½ï¿½o de desconto na nota de crï¿½dito
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2502_DESC_NOTCRE = Short.valueOf("2502");
	
	/** Aprovaï¿½ï¿½o de acrï¿½scimo na nota de crï¿½dito
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2503_ACRE_NOTCRE = Short.valueOf("2503");
	
	/**
	 * Aprovaï¿½ï¿½o de desconto na nota de crï¿½dito padrï¿½o (Nova UI)
	 */
	public Short NR2507_DESC_NOTCRE = Short.valueOf("2507");
	
	/**
	 * Aprovaï¿½ï¿½o de acrï¿½scimo na nota de crï¿½dito padrï¿½o (Nova UI)
	 */
	public Short NR2508_ACRE_NOTCRE = Short.valueOf("2508");
	
	/**
	 * Nota de crédito gerada com valor maior do que informado no parâmetro.
	 * (Nova UI)
	 */
	public Short NR2509_NC_VL_MAIOR_PARAMETRO = Short.valueOf("2509");
	
	/** Conhecimento de nota de crï¿½dito gerada maior que o valor mï¿½ximo
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR2504_CTO_MAIOR_VLR = Short.valueOf("2504");
	
	
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de portaria*/
	
	/** Veï¿½culo de coleta/entrega com km maior que definido na rota
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR601_KM_NOTA = Short.valueOf("601");
		
	/** Ciï¿½ncia da entrada de veï¿½culo de coleta sem coleta efetuada
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR603_ENT_SEM_COLETA = Short.valueOf("603");
	
	/** Liberar auditoria reprovada
	 *  @author Andresa Vargas
	 *  @since 12/09/2006 
	 */
	public Short NR604_LIB_AUD_REPRV = Short.valueOf("604");
	
	/** Meio de transporte selecionado para auditoria
	 *  @author Andresa Vargas
	 *  @since 04/12/2006
	 */
	public Short NR605_MT_SELEC_AUD = Short.valueOf("605");
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de entrega*/
	
	/** Aviso de manifesto de entrega fechado com documentos nï¿½o recolhidos
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR901_DOCTO_NREC = Short.valueOf("901");
		
	/** Aviso de agendamento cancelado ou reagendado por outra filial
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR902_AGED_CANC_REAG = Short.valueOf("902");
	
	/*Padronizaï¿½ï¿½o das chamadas do workflow para o mï¿½dulo de sim*/
	
	/** Aviso de documento retirado pelo cliente em outra filial
	 *  @author Andresa Vargas
	 *  @since 12/09/2006
	 */
	public Short NR1001_DOCTO_RETFIL = Short.valueOf("1001");
	
	/** 
	 * Aviso de valor elevado para um RNC
	 */
	public Short NR0201_COLETA_AEREA = Short.valueOf("0201");
	
	/** 
	 * Aviso de produto diferenciado informado nï¿½o aprovado nos produtos do cliente
	 */
	public Short NR0202_APROVACAO_PROD_DIFERENCIADO_COLETA = Short.valueOf("0202");
	
	/** 
	 * Aviso de quilometragem maior do que prevista
	 */
	public Short NR0301_CONTROLE_QUILOMETRAGEM = Short.valueOf("0301");
	
	public Short NR1201_VALOR_ELEVADO_RNC = Short.valueOf("1201");

	public Short NR1202_APROVAR_RNC_PARA_DOCUMENTO_DE_SERVICO = Short.valueOf("1202");

	/**
	 * Avisos Indenizaï¿½ï¿½es.
	 */
	public Short NR2100_VALOR_INDENIZACAO = Short.valueOf("2100");
	
	public Short NR2101_VALOR_INDENIZACAO = Short.valueOf("2101");

	public Short NR2102_VALOR_INDENIZACAO = Short.valueOf("2102");

	public Short NR2103_VALOR_INDENIZACAO = Short.valueOf("2103");

	public Short NR2104_VALOR_INDENIZACAO = Short.valueOf("2104");
	
	public Short NR2105_VALOR_INDENIZACAO = Short.valueOf("2105");

	public Short NR2106_VALOR_INDENIZACAO = Short.valueOf("2106");
	
	public Short NR2107_VALOR_INDENIZACAO = Short.valueOf("2107");

	public Short NR2108_VALOR_INDENIZACAO = Short.valueOf("2108");
	
	public Short NR2109_VALOR_INDENIZACAO = Short.valueOf("2109");

	public Short NR2110_VALOR_INDENIZACAO = Short.valueOf("2110");

	public Short NR2111_VALOR_INDENIZACAO = Short.valueOf("2111");
	
	public Short NR2112_VALOR_INDENIZACAO = Short.valueOf("2112");

	public Short NR2113_VALOR_INDENIZACAO = Short.valueOf("2113");
	
	public Short NR2114_VALOR_INDENIZACAO = Short.valueOf("2114");
	
	public Short NR2115_VALOR_INDENIZACAO = Short.valueOf("2115");
	
	public Short NR2116_VALOR_INDENIZACAO = Short.valueOf("2116");
	
	public Short NR2117_VALOR_INDENIZACAO = Short.valueOf("2117");
	
	public Short NR2118_VALOR_INDENIZACAO = Short.valueOf("2118");
	
	public Short NR2119_VALOR_INDENIZACAO = Short.valueOf("2119");

	public Short NR2120_VALOR_INDENIZACAO = Short.valueOf("2120");
	
	public Short NR2121_VALOR_INDENIZACAO = Short.valueOf("2121");
	
	public Short NR2122_VALOR_INDENIZACAO = Short.valueOf("2122");
	
	public Short NR2123_VALOR_INDENIZACAO = Short.valueOf("2123");
	
	public Short NR2124_VALOR_INDENIZACAO = Short.valueOf("2124");
	
	public Short NR2125_VALOR_INDENIZACAO = Short.valueOf("2125");
	
	public Short NR2126_VALOR_INDENIZACAO = Short.valueOf("2126");
	
	public Short NR2127_VALOR_INDENIZACAO = Short.valueOf("2127");
	
	public Short NR2128_VALOR_INDENIZACAO = Short.valueOf("2128");
	
	public Short NR2129_VALOR_INDENIZACAO = Short.valueOf("2129");
	
	public Short NR2130_VALOR_INDENIZACAO = Short.valueOf("2130");
	
	public Short NR2131_VALOR_INDENIZACAO = Short.valueOf("2131");
	
	public Short NR2132_VALOR_INDENIZACAO = Short.valueOf("2132");
	
	public Short NR2133_VALOR_INDENIZACAO = Short.valueOf("2133");
	
	public Short NR2134_VALOR_INDENIZACAO = Short.valueOf("2134");
	
	public Short NR2135_VALOR_INDENIZACAO = Short.valueOf("2135");
	
	public Short NR2136_VALOR_INDENIZACAO = Short.valueOf("2136");
	
	public Short NR2137_VALOR_INDENIZACAO = Short.valueOf("2137");
	
	public Short NR2138_VALOR_INDENIZACAO = Short.valueOf("2138");
	
	public Short NR2141_VALOR_INDENIZACAO = Short.valueOf("2141");
	
	public Short NR103_APROVACAO_CARTEIRA_VENDAS = Short.valueOf("103");
	
	public Short NR105_APROVACAO_PRODUTO_PERIGOSO = Short.valueOf("105");
	
	public Short NR106_APROVACAO_PRODUTO_RISCO = Short.valueOf("106");
	
	
	/* Expediï¿½ï¿½o */
	public Short NR402_VL_FRETE_SUPERIOR_LIMITE = Short.valueOf("402");
	public Short NR403_VL_PERCENTUAL_FRETE_SUPERIOR_LIMITE = Short.valueOf("403");
	public Short NR404_VL_MERCADORIA_SUPERIOR_LIMITE = Short.valueOf("404");
	
	public Short NR405_CONHECIMENTO_RODOVIARIO_VINCULADO_A_AWB = Short.valueOf("405");
	public Short NR406_XML_CANCELAMENTO_AWB = Short.valueOf("406");
	
	//Franqueados
	public Short NR4601_LIBERACAO_LANCAMENTO_MANUAL = Short.valueOf("4601");
	public Short NR4603_RECIBO_INDENIZACAO_FRANQUEADO = Short.valueOf("4603");
	public Short NR4604_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_1 = Short.valueOf("4604");
	public Short NR4605_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_2 = Short.valueOf("4605");
	public Short NR4606_APROVACAO_LANCAMENTO_FRANQUEADO_FAIXA_3 = Short.valueOf("4606");
	
	//top security
	public Short NR503_APROVACAO_FINALIZAR_CARREGAMENTO = Short.valueOf("503");
	public Short NR504_APROVACAO_EMISSAO_CONTROLE_CARGA= Short.valueOf("504");
	

	//Frete Carreteiro
	public Short NR2505_APROVACAO_RECIBO_FRETE_CARRETEIRO = Short.valueOf("2505");
	public Short NR2506_APROVACAO_DESCONTO_FRETE_CARRETEIRO = Short.valueOf("2506");
	

	public Short NR2401_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_ANALISTAS = Short.valueOf("2401");
    public Short NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO = Short.valueOf("2402");
	
    //Histï¿½rico workflow
    public Short NR111_APROVACAO_LIBERACAO_EMBARQUE = Short.valueOf("111");
    public Short NR135_APROVACAO_SITUACAO_DIVISAO_CLIENTE = Short.valueOf("135");
    public Short NR136_APROVACAO_TP_PESO_CALCULO = Short.valueOf("136");
    public Short NR137_APROVACAO_TABELA_PRECO = Short.valueOf("137");
    public Short NR138_APROVACAO_FILIAL_COMERCIAL = Short.valueOf("138");
    public Short NR139_APROVACAO_FILIAL_OPERACIONAL = Short.valueOf("139");
    public Short NR140_APROVACAO_FILIAL_FINANCEIRO = Short.valueOf("140");
    public Short NR141_APROVACAO_TIPO_CLIENTE = Short.valueOf("141");
    public Short NR142_APROVACAO_DESEFETIVACAO_TABELA_PRECO= Short.valueOf("142");
    public Short NR143_APROVACAO_EFETIVACAO_TABELA_PRECO= Short.valueOf("143");
    public Short NR144_APROVACAO_OBRIGA_DIMENSOES = Short.valueOf("144");
    public Short NR145_APROVACAO_NR_FATOR_DENSIDADE = Short.valueOf("145");
    public Short NR146_APROVACAO_NR_FATOR_CUBAGEM = Short.valueOf("146");
    public Short NR147_APROVACAO_DESEFETIVACAO_LIBERACAO_EMBARQUE = Short.valueOf("147");
    public Short NR148_APROVACAO_TRT_CLIENTE = Short.valueOf("148");
    
    
	
	public Short NR2611_APROVACAO_CADASTRO_MOTORISTA_CE = Short.valueOf("2611");
	public Short NR2617_ALTERACAO_CADASTRO_MOTORISTA_CE = Short.valueOf("2617");
	
	public Short NR2620_ALTERACAO_CADASTRO_MOTORISTA_VI = Short.valueOf("2620");
	public Short NR2614_APROVACAO_CADASTRO_MOTORISTA_VI = Short.valueOf("2614");
	
	public Short NR2612_APROVACAO_CADASTRO_MEIO_TRANSPORTE_CE = Short.valueOf("2612");
	public Short NR2618_ALTERACAO_CADASTRO_MEIO_TRANSPORTE_CE = Short.valueOf("2618");
	
	public Short NR2615_APROVACAO_CADASTRO_MEIO_TRANSPORTE_VI = Short.valueOf("2615");
	public Short NR2621_ALTERACAO_CADASTRO_MEIO_TRANSPORTE_VI = Short.valueOf("2621");
	
	public Short NR2609_APROVACAO_CADASTRO_PROPRIETARIO_CE = Short.valueOf("2609");
	public Short NR2616_ALTERACAO_CADASTRO_PROPRIETARIO_CE = Short.valueOf("2616");
	
	public Short NR2613_APROVACAO_CADASTRO_PROPRIETARIO_VI = Short.valueOf("2613");
	public Short NR2619_ALTERACAO_CADASTRO_PROPRIETARIO_VI = Short.valueOf("2619");
	
	public Short NR2139_VALOR_INDENIZACAO = Short.valueOf("2139");

	public Short NR2140_VALOR_INDENIZACAO = Short.valueOf("2140");
	
	public Short NR1203_VALOR_ELEVADO_RNC = Short.valueOf("1203");

	public Short NR5001_SOLICITACAO_PAGAMENTO_GNRE = Short.valueOf("5001");

}
