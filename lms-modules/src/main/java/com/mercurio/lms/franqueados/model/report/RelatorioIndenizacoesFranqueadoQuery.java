package com.mercurio.lms.franqueados.model.report;

import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;

public class RelatorioIndenizacoesFranqueadoQuery {
	
	
	private static final String PROJECTION = 
			" SELECT      REG.DS_REGIONAL AS \"Regional debitada\", " +
			"             FFRQ.SG_FILIAL AS \"Filial debitada\", " +
			"             P.NR_IDENTIFICACAO AS \"CPF/CNPJ\", " +
			"             P.NM_PESSOA AS \"Cliente\", " +
			"             MIN(RI.VL_INDENIZACAO) AS \"Valor\", " +
			"             F.SG_FILIAL || ' ' || RI.NR_RECIBO_INDENIZACAO AS \"RIM\", " +
			"             TO_CHAR(RI.DT_PAGAMENTO_EFETUADO,'DD/MM/YYYY') as \"Data pagto\", " +
			"             DECODE(MANC.TP_MOTIVO, 'AV','Avaria','FV','Falta','Outra') AS \"Tipo\", " +
			"             MIN(FDS.SG_FILIAL || ' ' || DS.NR_DOCTO_SERVICO) AS \"CT-e\", " +
			"             MIN(TO_CHAR(DS.DH_EMISSAO, 'DD/MM/YYYY')) AS \"Data de emissão\" ";
	
	
	private static final String FROM =
			" FROM        RECIBO_INDENIZACAO RI,  " +
			"             DOCTO_SERVICO_INDENIZACAO DSI, " +
			"             DOCTO_SERVICO DS, " +
			"             DEVEDOR_DOC_SERV_FAT DEV, " +
			"             PESSOA P, " +
			"             FILIAL FDS, " +
			"             FILIAL F, " +
			"             FRANQUIA FRQ, " +
			"             FILIAL FFRQ, " +
			"             FILIAL_DEBITADA FD, " +
			"             OCORRENCIA_NAO_CONFORMIDADE ONC,  " +
			"             MOTIVO_ABERTURA_NC MANC, " +
			"             REGIONAL REG, " +
			"             REGIONAL_FILIAL REF ";
	
	
	private static final String CRITERIA =
			" WHERE       RI.ID_FILIAL = F.ID_FILIAL " +
			" AND         RI.ID_RECIBO_INDENIZACAO = DSI.ID_RECIBO_INDENIZACAO " +
			" AND         DSI.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
			" AND         DS.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO " +
			" AND         DEV.ID_CLIENTE = P.ID_PESSOA " +
			" AND         DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL " +
			" AND         FRQ.ID_FRANQUIA = FFRQ.ID_FILIAL " +
			" AND         RI.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO " +
			" AND         FD.ID_FILIAL = FRQ.ID_FRANQUIA " +
			" AND         ONC.ID_OCORRENCIA_NAO_CONFORMIDADE = DSI.ID_OCORRENCIA_NAO_CONFORMIDADE " +
			" AND         ONC.ID_MOTIVO_ABERTURA_NC = MANC.ID_MOTIVO_ABERTURA_NC " +
			" AND         REG.ID_REGIONAL = REF.ID_REGIONAL " +
			" AND         REF.ID_FILIAL = FFRQ.ID_FILIAL " +
			" AND         RI.DT_PAGAMENTO_EFETUADO BETWEEN REF.DT_VIGENCIA_INICIAL AND REF.DT_VIGENCIA_FINAL " +
			" AND         RI.DT_PAGAMENTO_EFETUADO BETWEEN REG.DT_VIGENCIA_INICIAL AND REG.DT_VIGENCIA_FINAL " +
			" AND         RI.TP_STATUS_INDENIZACAO = 'P' " +
			" AND         NOT EXISTS(SELECT * " +
			"                        FROM   RECIBO_INDENIZACAO_FRQ RIF  " +
			"                        WHERE  RIF.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO " +
			"                        AND    RIF.ID_FRANQUIA = FRQ.ID_FRANQUIA " +
			"                        AND    RIF.TP_SITUACAO_PENDENCIA IN ('A','E')) ";
	
	private static final String FILTRO_FRANQUIA =
			" AND         FRQ.ID_FRANQUIA = :idFilial ";
	
	private static final String FILTRO_DT_MENOR =
			" AND RI.DT_PAGAMENTO_EFETUADO >= :dtPagamentoInicial ";
	
	private static final String FILTRO_DT_MAIOR =
			" AND RI.DT_PAGAMENTO_EFETUADO <= :dtPagamentoFinal ";
	
	
	private static final String GROUP =
			" GROUP BY REG.DS_REGIONAL, " +
			"             FFRQ.SG_FILIAL, " +
			"             F.SG_FILIAL || ' ' || RI.NR_RECIBO_INDENIZACAO, " +
			"             TO_CHAR(RI.DT_PAGAMENTO_EFETUADO,'DD/MM/YYYY'), " +
			"             DECODE(MANC.TP_MOTIVO, 'AV','Avaria','FV','Falta','Outra'), " +
			"             P.NR_IDENTIFICACAO, " +
			"             P.NM_PESSOA ";
	
	
	private static final String ORDER =
			" ORDER BY \"Filial debitada\", \"RIM\" ";
			
	
	public static String getQuery(Map<String, Object> parameters){
		StringBuilder query = new StringBuilder();
		query.append(PROJECTION);
		query.append(FROM);
		query.append(CRITERIA);
		
		if(parameters.get("idFilial") != null){
			query.append(FILTRO_FRANQUIA);
		}
		if(parameters.get("dtPagamentoInicial") != null){
			query.append(FILTRO_DT_MENOR);
		}
		if(parameters.get("dtPagamentoFinal") != null){
			query.append(FILTRO_DT_MAIOR);
		}
		
		query.append(GROUP);
		query.append(ORDER);
		return query.toString();
	}

	public static final ConfigureSqlQuery configureCSV() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Regional debitada");
				sqlQuery.addScalar("Filial debitada");
				sqlQuery.addScalar("CPF/CNPJ");
				sqlQuery.addScalar("Cliente");
				sqlQuery.addScalar("Valor", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("RIM");
				sqlQuery.addScalar("Data pagto");
				sqlQuery.addScalar("Tipo");
				sqlQuery.addScalar("CT-e");
				sqlQuery.addScalar("Data de emissão");
			}
		};
		return csq;
	}
}
