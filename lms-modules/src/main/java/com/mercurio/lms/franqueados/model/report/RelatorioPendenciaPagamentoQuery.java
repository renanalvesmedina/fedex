package com.mercurio.lms.franqueados.model.report;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioPendenciaPagamentoQuery {

	private static final String QUERY = "SELECT FFRQ.SG_FILIAL 					AS \"Unidade\",                     "+
			"  FO.SG_FILIAL			                                                                                "+
			"  || ' '			                                                                                    "+
			"  || DS.NR_DOCTO_SERVICO                              				AS \"Conhecimento\",                "+
			"  DS.TP_DOCUMENTO_SERVICO                             				AS \"Tipo\",                        "+
			"  TO_CHAR(DS.DH_EMISSAO, 'YYYY')                      				AS \"Ano\",                         "+
			"  TO_CHAR(DS.DH_EMISSAO, 'MON')                       				AS \"Mês\",                         "+
			"  TO_CHAR(DS.DH_EMISSAO, 'DD/MM/YYYY')                				AS \"Data\",                        "+
			"  TRUNC(SYSDATE) - TRUNC(CAST(DS.DH_EMISSAO AS DATE)) 				AS \"Dias em aberto\",              "+
			"  TO_CHAR(DS.DH_EMISSAO, 'YYYY.MM')                   				AS \"Chave\",                       "+
			"  FO.SG_FILIAL                                        				AS \"Origem\",                      "+
			"  FD.SG_FILIAL                                        				AS \"Destino\",                     "+
			"  VI18N(SM.DS_SEGMENTO_MERCADO_I)                     				AS \"Segmento\",                    "+
			"  PDEV.NM_PESSOA                                      				AS \"Razão Social\",                "+
			"  DEV.VL_DEVIDO                                       				AS \"Valor Bruto\",                 "+
			"  DES.VL_DESCONTO                                     				AS \"Desconto\",                    "+
			"  DEV.VL_DEVIDO - NVL(DES.VL_DESCONTO,0)              				AS \"Frete líquido\",               "+
			"  TO_CHAR(DECODE(DEV.TP_SITUACAO_COBRANCA, 'F',                                                        "+
			"  (SELECT MIN(FAT.DT_VENCIMENTO)                                                                       "+
			"  FROM FATURA FAT,                                                                                     "+
			"    ITEM_FATURA IFAT                                                                                   "+
			"  WHERE FAT.ID_FATURA              = IFAT.ID_FATURA                                                    "+
			"  AND IFAT.ID_DEVEDOR_DOC_SERV_FAT = dev.id_devedor_doc_serv_fat                                       "+
			"  AND fat.tp_situacao_fatura      <> 'CA'                                                              "+
			"  ), NULL), 'DD/MM/YYYY') 											AS \"Vencto. Fatura\",              "+
			"  DECODE(DEV.TP_SITUACAO_COBRANCA, 'F',                                                                "+
			"  (SELECT MIN(FFAT.SG_FILIAL                                                                           "+
			"    || ' '                                                                                             "+
			"    || FAT.NR_FATURA)                                                                                  "+
			"  FROM FATURA FAT,                                                                                     "+
			"    ITEM_FATURA IFAT,                                                                                  "+
			"    FILIAL FFAT                                                                                        "+
			"  WHERE FAT.ID_FATURA              = IFAT.ID_FATURA                                                    "+
			"  AND FAT.ID_FILIAL                = FFAT.ID_FILIAL                                                    "+
			"  AND IFAT.ID_DEVEDOR_DOC_SERV_FAT = dev.id_devedor_doc_serv_fat                                       "+
			"  AND fat.tp_situacao_fatura      <> 'CA'                                                              "+
			"  ), NULL) 														AS \"Fatura\"                       "+
			"FROM DOCTO_SERVICO DS,                                                                                 "+
			"  DEVEDOR_DOC_SERV_FAT DEV,                                                                            "+
			"  FILIAL FO,                                                                                           "+
			"  FILIAL FD,                                                                                           "+
			"  FILIAL FFRQ,                                                                                         "+
			"  PESSOA PDEV,                                                                                         "+
			"  SEGMENTO_MERCADO SM,                                                                                 "+
			"  CLIENTE CLI,                                                                                         "+
			"  DESCONTO DES,                                                                                        "+
			"  (SELECT F1.ID_FRANQUIA,                                                                              "+
			"    ds1.id_docto_servico                                                                               "+
			"  FROM DOCTO_SERVICO DS1,                                                                              "+
			"    DEVEDOR_DOC_SERV_FAT DEV1,                                                                         "+
			"    FILIAL FO1,                                                                                        "+
			"    FILIAL FD1,                                                                                        "+
			"    FRANQUIA F1,                                                                                       "+
			"    FRANQUEADO_FRANQUIA FF1                                                                            "+
			"  WHERE DS1.ID_DOCTO_SERVICO = DEV1.ID_DOCTO_SERVICO                                                   "+
			"  AND DS1.ID_FILIAL_ORIGEM   = FO1.ID_FILIAL                                                           "+
			"  AND DS1.ID_FILIAL_DESTINO  = FD1.ID_FILIAL                                                           "+
			"  AND F1.ID_FRANQUIA         = FF1.ID_FRANQUIA                                                         "+
			"  AND SYSDATE BETWEEN FF1.DT_VIGENCIA_INICIAL AND FF1.DT_VIGENCIA_FINAL                                "+
			"  AND DEV1.TP_SITUACAO_COBRANCA                             IN ('P','C','F')                           "+
			"  AND TRUNC(CAST(DS1.DH_EMISSAO AS DATE)) <= (TRUNC(SYSDATE) - :diasEmAberto)							"+
			"  AND TRUNC(CAST(DS1.DH_EMISSAO AS DATE)) >= TO_DATE('01/08/2012','DD/MM/YYYY')                        "+
			"  AND F1.ID_FRANQUIA                      IN (FO1.ID_FILIAL_RESPONSAVEL, FD1.ID_FILIAL_RESPONSAVEL)    "+
			"  AND NOT EXISTS                                                                                       "+
			"    (SELECT *                                                                                          "+
			"    FROM CONHECIMENTO C                                                                                "+
			"    WHERE C.ID_CONHECIMENTO        = DS1.ID_DOCTO_SERVICO                                              "+
			"    AND C.TP_SITUACAO_CONHECIMENTO = 'C'                                                               "+
			"    )                                                                                                  "+
			"  AND NOT EXISTS                                                                                       "+
			"    (SELECT *                                                                                          "+
			"    FROM NOTA_FISCAL_SERVICO N                                                                         "+
			"    WHERE N.ID_NOTA_FISCAL_SERVICO = DS1.ID_DOCTO_SERVICO                                              "+
			"    AND N.TP_SITUACAO_NF           = 'C'                                                               "+
			"    )                                                                                                  "+
			"  AND DS1.NR_DOCTO_SERVICO      > 0                                                                    "+
			"  AND DS1.TP_DOCUMENTO_SERVICO <> 'MDA'                                                                ";

	private static final String WHERE = 			"WHERE DS.ID_DOCTO_SERVICO         = DEV.ID_DOCTO_SERVICO		"+
			"AND DS.ID_FILIAL_ORIGEM           = FO.ID_FILIAL                                                       "+
			"AND DS.ID_FILIAL_DESTINO          = FD.ID_FILIAL                                                       "+
			"AND DEV.ID_CLIENTE                = PDEV.ID_PESSOA                                                     "+
			"AND DEV.ID_CLIENTE                = CLI.ID_CLIENTE                                                     "+
			"AND CLI.ID_SEGMENTO_MERCADO       = SM.ID_SEGMENTO_MERCADO (+)                                         "+
			"AND DEV.ID_DEVEDOR_DOC_SERV_FAT   = DES.ID_DEVEDOR_DOC_SERV_FAT (+)                                    "+
			"AND DES.TP_SITUACAO_APROVACAO (+) = 'A'                                                                "+
			"AND FFRQ.ID_FILIAL                = X.ID_FRANQUIA                                                      "+
			"AND DS.ID_DOCTO_SERVICO           = X.ID_DOCTO_SERVICO                                                 ";

			
	private static final String FILTRO_FRANQUIA = " AND   F1.ID_FRANQUIA = :idFilial ";
	private static final String ORDER_BY = "ORDER BY \"Dias em aberto\" DESC, \"Fatura\", \"Razão Social\", \"Valor Bruto\" DESC";
	public static String getQuery(boolean filtraFranquia){
		String result = "";
		result += QUERY;
		if(filtraFranquia == true){
			result += FILTRO_FRANQUIA;
		}
		
		result += " ) X ";
		result += WHERE;
		result += ORDER_BY;

		return result;
	}

	public static ConfigureSqlQuery createConfigureSql() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Unidade");
				sqlQuery.addScalar("Conhecimento");
				sqlQuery.addScalar("Tipo");
				sqlQuery.addScalar("Ano");
				sqlQuery.addScalar("Mês");
				sqlQuery.addScalar("Data");
				sqlQuery.addScalar("Dias em aberto");
				sqlQuery.addScalar("Chave");
				sqlQuery.addScalar("Origem");
				sqlQuery.addScalar("Destino");
				sqlQuery.addScalar("Segmento");
				sqlQuery.addScalar("Razão Social");
				sqlQuery.addScalar("Valor Bruto");
				sqlQuery.addScalar("Desconto");
				sqlQuery.addScalar("Frete líquido", Hibernate.BIG_DECIMAL);				
				sqlQuery.addScalar("Vencto. Fatura");
				sqlQuery.addScalar("Fatura");
			}
		};
		return csq;
	}
}
