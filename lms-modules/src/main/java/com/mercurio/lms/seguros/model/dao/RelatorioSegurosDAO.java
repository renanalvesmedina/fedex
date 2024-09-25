package com.mercurio.lms.seguros.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * 
 * @author lalmeida
 *
 *@spring.bean com.mercurio.lms.seguros.model.dao.RelatorioSegurosDAO
 */
public class RelatorioSegurosDAO extends AdsmDao{

	public List<Map<String, Object>> findDadosReport(TypedFlatMap parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(
				mountSQL(parameters), null, configureSQL(parameters));
	}
	
	private String mountSQL(TypedFlatMap parameters) {
		
		boolean checkProcessosSinistro = parameters.getBoolean("checkProcessosSinistro");
		boolean checkRims = parameters.getBoolean("checkRims");
		boolean checkCtes = parameters.getBoolean("checkCtes");
		boolean checkNfs = parameters.getBoolean("checkNfs");
		
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
	
		if(checkProcessosSinistro) {
			sql = appendColunasProcessoSinistro(sql);
		}
		
		if(checkRims) {
			if(checkProcessosSinistro) {
				sql.append(", ");
			}
			sql = appendColunasRIM(sql);
		}
		
		if(checkCtes) {
			if(checkProcessosSinistro || checkRims) {
				sql.append(", ");
			}
			sql = appendColunasDoctoServico(sql);
		}
		
		if(checkNfs) {
			if(checkProcessosSinistro || checkRims
					|| checkCtes) {
				sql.append(", ");
			}
			sql = appendColunasNotaFiscal(sql);
		}
		
		sql = appendFrom(sql);
		sql = appendRelationships(sql);
		sql = appendFiltrosProcessoSinistro(sql, parameters);
		sql = appendFiltrosRIM(sql, parameters);
		sql = appendFiltrosDoctoServico(sql, parameters);
		sql = appendOrderBy(sql, parameters);
		
		return sql.toString();
	}
	
	private StringBuilder appendColunasProcessoSinistro(StringBuilder sql) {
		return sql
				.append(" PS.DS_REGIONAL AS \"Regional\", ")
				.append(" PS.NR_PROCESSO_SINISTRO AS \"Número processo\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_SIT_PROCESSO_SINISTRO') ")
				.append(" 	AND VL_VALOR_DOMINIO = ")
				.append(" 		DECODE(PS.DH_FECHAMENTO, NULL, 'A', 'F')) AS \"Situação\", ")
				.append(" PS.NR_BOLETIM_OCORRENCIA AS \"Nº. boletim de ocorrência\", ")
				.append(" TO_CHAR(PS.DH_SINISTRO, 'DD/MM/YYYY HH24:MI') AS \"Data/hora sinistro\", ")
				.append(" TO_CHAR(PS.DH_ABERTURA, 'DD/MM/YYYY HH24:MI') AS \"Data/hora abertura\", ")
				.append(" TO_CHAR(PS.DH_FECHAMENTO, 'DD/MM/YYYY HH24:MI') AS \"Data/hora fechamento\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE( ")
				.append("                             PS.DS_JUSTIFICATIVA_ENCERRAMENTO, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Obs. fechamento\", ")
				.append(" USA.NM_USUARIO AS \"Usuário abertura\", ")
				.append(" USF.NM_USUARIO AS \"Usuário fechamento\", ")
				.append(" TS.SG_TIPO AS \"Tipo seguro\", ")
				.append(" VI18N(TSIN.DS_TIPO_I) AS \"Tipo sinistro\", ")
				.append(" PS.NR_APOLICE AS \"Apólice\", ")
				.append(" PS.NM_CORRETORA AS \"Corretora\", ")
				.append(" PS.NM_SEGURADORA AS \"Seguradora\", ")
				.append(" MT.NR_FROTA AS \"Frota\", ")
				.append(" MT.NR_IDENTIFICADOR AS \"Placa\", ")
				.append(" PS.TP_VEICULO AS \"Tipo de veículo\", ")
				.append(" MTR.NR_CERTIFICADO AS \"Certificado\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PMOTO.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PMOTO.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PMOTO.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CPF motorista\", ")
				.append(" PMOTO.NM_PESSOA AS \"Nome\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_VINCULO_MOTORISTA') ")
				.append(" 	AND VL_VALOR_DOMINIO = MOTO.TP_VINCULO) AS \"Tipo de vínculo\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE(PS.DS_SINISTRO, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Descrição do sinistro\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_LOCAL_SINISTRO') ")
				.append(" 	AND VL_VALOR_DOMINIO = PS.TP_LOCAL_SINISTRO) AS \"Local sinistro\", ")
				.append(" RODO.SG_RODOVIA AS \"Rodovia\", ")
				.append(" PS.NR_KM_SINISTRO AS \"Km\", ")
				.append(" FILOCAL.SG_FILIAL AS \"Filial sinistro\", ")
				.append(" AEROLOCAL.SG_AEROPORTO AS \"Aeroporto\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE(PS.DS_LOCAL_SINISTRO, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Local sinistro (outro)\", ")
				.append(" MUNLOCAL.NM_MUNICIPIO AS \"Município\", ")
				.append(" UFLOCAL.SG_UNIDADE_FEDERATIVA AS \"UF\", ")
				.append(" MO.SG_MOEDA || ' ' || MO.DS_SIMBOLO AS \"Moeda\", ")
				.append(" PS.VL_MERCADORIA_CALC AS \"Valor da mercadoria\", ")
				.append(" PS.VL_PREJUIZO_CARGA_CALC AS \"Valor do prejuízo carga\", ")
				.append(" PS.VL_PREJUIZO_ADIC_CALC AS \"Valor do prejuízo adicional\", ")
				.append(" PS.VL_PREJUIZO_CALC AS \"Valor do prejuízo total\", ")
				.append(" PS.VL_PREJUIZO_PROPRIO AS \"Valor do prejuízo próprio\", ")
				.append(" PS.VL_REEMBOLSO_CALC AS \"Valor do reembolso\", ")
				.append(" PS.VL_FRANQUIA AS \"Valor da franquia\", ")
				.append(" GREATEST(PS.VL_PREJUIZO_CALC - PS.VL_PREJUIZO_PROPRIO - PS.VL_REEMBOLSO_CALC - ")
				.append(" 		 PS.VL_FRANQUIA, ")
				.append(" 		 0) AS \"Valor a receber\", ")
				.append(" PS.VL_MERCADORIA_CALC - PS.VL_PREJUIZO_CALC AS \"Valor recuperado\", ")
				.append(" PS.VL_INDENIZADO_CALC AS \"Valor indenizado\", ")
				.append(" PS.VL_PREJUIZO_CALC - PS.VL_INDENIZADO_CALC - ")
				.append(" PS.VL_REEMBOLSO_CALC AS \"Dif. indenizado reembolsado\", ")
				.append(" PS.VL_PREJUIZO_CALC - PS.VL_INDENIZADO_CALC AS \"Dif. pgto. cliente\", ")
				.append(" DECODE(PS.DH_FECHAMENTO, ")
				.append(" 	   NULL, ")
				.append(" 	   NULL, ")
				.append(" 	   TO_NUMBER(TRUNC(CAST(PS.DH_FECHAMENTO AS DATE) - ")
				.append(" 					   CAST(PS.DH_ABERTURA AS DATE)))) AS \"Tempo de pgto\", ")
				.append(" SR.DS_SITUACAO_REEMBOLSO AS \"Situação do reembolso\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE(PS.OB_SINISTRO, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Observações\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE( ")
				.append("                                    PS.DS_COMUNICADO_CORRETORA, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Comunicado corretora\", ")
				.append(" PS.QT_DOCUMENTOS AS \"Qtd documentos\", ")
				.append(" TRUNC(CAST(PS.DH_ABERTURA AS DATE)) AS \"Data abertura\", ")
				.append(" PS.DT_OCORRENCIA AS \"Data comunicado\", ")
				.append(" PS.DT_RETIFICACAO AS \"Data atualização\", ")
				.append(" PS.DT_FILIAL_RIM AS \"Data comunicado RIM\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_SIM_NAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = PS.CTO_SEM_RIM) AS \"Doctos sem RIM\", ")
				.append(" PS.DT_RIM_SEM_PAGTO AS \"Data emissão RIM sem pagto\", ")
				.append(" CASE ")
				.append("   WHEN PS.VL_PREJUIZO_CALC > 0 ")
				.append(" 		AND PS.DT_RIM_SEM_PAGTO IS NOT NULL ")
				.append(" 		AND ")
				.append(" 		((PS.DT_RETIFICACAO IS NOT NULL AND ")
				.append(" 		(PS.DT_RETIFICACAO + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 									FROM PARAMETRO_GERAL PG ")
				.append(" 								   WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 										 'NR_PRAZO_PAGTO_RIM'), ")
				.append(" 								  55)) <= TRUNC(SYSDATE)) OR ")
				.append(" 		((PS.DT_RIM_SEM_PAGTO + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 									  FROM PARAMETRO_GERAL PG ")
				.append(" 									 WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 										   'NR_PRAZO_PAGTO_RIM'), ")
				.append(" 									55)) <= TRUNC(SYSDATE))) THEN ")
				.append(" 	  CASE ")
				.append(" 		WHEN (PS.CTO_SEM_RIM = 'S' AND ")
				.append(" 			 (NVL(PS.DT_FILIAL_RIM, TRUNC(CAST(PS.DH_ABERTURA AS DATE))) + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 		 FROM PARAMETRO_GERAL PG ")
				.append(" 		WHERE PG.NM_PARAMETRO_GERAL = 'NR_PRAZO_EMISSAO_RIM'),10)) <= TRUNC(SYSDATE)) THEN ")
				.append(" 		 NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22043' AND RM.IDIOMA = 'PT_BR'),'01 - PAGAMENTO EXPIRADO E PENDENTE DE ABERTURA DE RIM') ")
				.append(" 		ELSE ")
				.append(" 		 NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22044' AND RM.IDIOMA = 'PT_BR'),'02 - PAGAMENTO EXPIRADO') ")
				.append(" 	  END ")
				.append("    WHEN PS.VL_PREJUIZO_CALC > 0 ")
				.append(" 		AND PS.CTO_SEM_RIM = 'S' ")
				.append(" 		AND (NVL(PS.DT_FILIAL_RIM, TRUNC(CAST(PS.DH_ABERTURA AS DATE))) + ")
				.append(" 		NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 				   FROM PARAMETRO_GERAL PG ")
				.append(" 				  WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 						'NR_PRAZO_EMISSAO_RIM'), ")
				.append(" 				 10)) <= TRUNC(SYSDATE) THEN ")
				.append(" 	CASE ")
				.append(" 	  WHEN PS.DT_FILIAL_RIM IS NULL THEN ")
				.append(" 	   NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22045' AND RM.IDIOMA = 'PT_BR'),'03 - PENDENTE DE ABERTURA DE RIM E DE COMUNICADO PARA ABERTURA DE RIM') ")
				.append(" 	  ELSE ")
				.append(" 	   NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22046' AND RM.IDIOMA = 'PT_BR'),'04 - PENDENTE DE ABERTURA DE RIM') ")
				.append(" 	END ")
				.append("    WHEN PS.DT_RETIFICACAO IS NOT NULL ")
				.append(" 		AND PS.VL_PREJUIZO_CALC > 0 ")
				.append(" 		AND PS.DT_FILIAL_RIM IS NULL ")
				.append(" 		AND (PS.DT_RETIFICACAO + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 									  FROM PARAMETRO_GERAL PG ")
				.append(" 									 WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 										   'NR_PRAZO_COMUNIC_RIM'), ")
				.append(" 									1)) <= TRUNC(SYSDATE) THEN ")
				.append(" 	NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22047' AND RM.IDIOMA = 'PT_BR'),'05 - PENDENTE DE COMUNICADO PARA ABERTURA DE RIM') ")
				.append("    WHEN TRUNC(CAST(PS.DH_ABERTURA AS DATE)) IS NOT NULL ")
				.append(" 		AND PS.DT_RETIFICACAO IS NULL ")
				.append(" 		AND (TRUNC(CAST(PS.DH_ABERTURA AS DATE)) + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 								   FROM PARAMETRO_GERAL PG ")
				.append(" 								  WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 										'NR_PRAZO_CONFIRM_PREJUIZO'), ")
				.append(" 								 10)) <= TRUNC(SYSDATE) THEN ")
				.append(" 	NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22048' AND RM.IDIOMA = 'PT_BR'),'06 - PENDENTE DE CONFIRMAÇÃO DOS PREJUÍZOS') ")
				.append("    WHEN TRUNC(CAST(PS.DH_ABERTURA AS DATE)) IS NOT NULL ")
				.append(" 		AND PS.DT_OCORRENCIA IS NULL ")
				.append(" 		AND (TRUNC(CAST(PS.DH_ABERTURA AS DATE)) + NVL((SELECT PG.DS_CONTEUDO ")
				.append(" 								   FROM PARAMETRO_GERAL PG ")
				.append(" 								  WHERE PG.NM_PARAMETRO_GERAL = ")
				.append(" 										'NR_PRAZO_COMUNIC_CLIENTE'), ")
				.append(" 								 1)) <= TRUNC(SYSDATE) THEN ")
				.append(" 	NVL((SELECT RM.TEXTO FROM RECURSOS_MENSAGENS RM WHERE RM.CHAVE = 'LMS-22049' AND RM.IDIOMA = 'PT_BR'),'07 - PENDENTE DE COMUNICADO PARA O CLIENTE') ")
				.append(" END AS \"Situação do processo\"");
	}
	
	private StringBuilder appendColunasRIM(StringBuilder sql) {
		return sql
				.append(" REG.SG_REGIONAL AS \"Regional RIM\", ")
				.append(" REG.DS_REGIONAL AS \"Descrição regional\", ")
				.append(" FI.SG_FILIAL AS \"Filial RIM\", ")
				.append(" RI.NR_RECIBO_INDENIZACAO AS \"Número RIM\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_INDENIZACAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.TP_INDENIZACAO) AS \"Tipo de RIM\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_STATUS_INDENIZACAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.TP_STATUS_INDENIZACAO) AS \"Status RIM\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_STATUS_WORKFLOW') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.TP_SITUACAO_WORKFLOW) AS \"Situação da aprovação\", ")
				.append(" RI.DT_EMISSAO AS \"Data RIM\", ")
				.append(" RI.DT_LIBERACAO_PAGAMENTO AS \"Data liberação de pgto\", ")
				.append(" RI.DT_PROGRAMADA_PAGAMENTO AS \"Data programada pagamento\", ")
				.append(" RI.DT_PAGAMENTO_EFETUADO AS \"Data pagamento\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PBEN.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PBEN.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PBEN.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ beneficiário\", ")
				.append(" PBEN.NM_PESSOA AS \"Razão social beneficiário\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PFAV.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PFAV.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PFAV.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ favorecido\", ")
				.append(" PFAV.NM_PESSOA AS \"Razão social favorecido\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_FORMA_PAGAMENTO_INDENIZACAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.TP_FORMA_PAGAMENTO) AS \"Forma pagamento\", ")
				.append(" BA.NR_BANCO AS \"Núm. banco\", ")
				.append(" AB.NR_AGENCIA_BANCARIA AS \"Núm. agência\", ")
				.append(" AB.NR_DIGITO AS \"Núm. dígito agência\", ")
				.append(" RI.NR_CONTA_CORRENTE AS \"Núm. conta\", ")
				.append(" RI.NR_DIGITO_CONTA_CORRENTE AS \"Núm. dígito conta\", ")
				.append(" (SELECT MIN(PRI.DT_VENCIMENTO) ")
				.append("    FROM PARCELA_RECIBO_INDENIZACAO PRI ")
				.append("   WHERE PRI.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO) AS \"Data de vencimento\", ")
				.append(" RI.VL_INDENIZACAO AS \"Valor indenização\", ")
				.append(" CAST(ROUND((RI.VL_INDENIZACAO / DS.VL_MERCADORIA) * 100, 2) AS ")
				.append(" 	 NUMBER(19, 2)) AS \"% indenização\", ")
				.append(" RI.QT_VOLUMES_INDENIZADOS AS \"Volumes indenizados\", ")
				.append(" VI18N(NP.DS_NATUREZA_PRODUTO_I) AS \"Natureza produto\", ")
				.append(" VI18N(P.DS_PRODUTO_I) AS \"Produto\", ")
				.append(" (SELECT TO_CHAR(WM_CONCAT(FID.SG_FILIAL || ' - ' || ")
				.append(" 						  TO_CHAR(FD.PC_DEBITADO, '990.00') || '%')) ")
				.append("    FROM FILIAL_DEBITADA FD, ")
				.append(" 		FILIAL          FID ")
				.append("   WHERE FD.ID_FILIAL = FID.ID_FILIAL ")
				.append(" 	AND FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO) AS \"Filiais debitadas\", ")
				.append(" FNCONC.SG_FILIAL AS \"Filial RNC\", ")
				.append(" NCONC.NR_NAO_CONFORMIDADE AS \"Número RNC\", ")
				.append(" TRUNC(NCONC.DH_EMISSAO) AS \"Data emissão RNC\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_STATUS_NAO_CONFORMIDADE') ")
				.append(" 	AND VL_VALOR_DOMINIO = NCONC.TP_STATUS_NAO_CONFORMIDADE) AS \"Status RNC\", ")
				.append(" VI18N(MANC.DS_MOTIVO_ABERTURA_I) AS \"Motivo abertura\", ")
				.append(" PS2.NR_PROCESSO_SINISTRO AS \"Núm. processo sinistro\", ")
				.append(" TS2.SG_TIPO AS \"Tipo seguro RIM\", ")
				.append(" VI18N(TSIN2.DS_TIPO_I) AS \"Tipo sinistro RIM\", ")
				.append(" RI.NR_NOTA_FISCAL_DEBITO_CLIENTE AS \"Nota fiscal débito cliente\", ")
				.append(" FSIN.SG_FILIAL AS \"Filial ocorr. 1\", ")
				.append(" FROTA.SG_FILIAL AS \"Filial ocorr. 2\", ")
				.append(" REPLACE(REPLACE(REPLACE(REPLACE( ")
				.append("                              RI.OB_RECIBO_INDENIZACAO, ")
				.append(" 									   CHR(10) || CHR(13), ")
				.append(" 									   ' '), ")
				.append(" 							   CHR(10), ")
				.append(" 							   ' '), ")
				.append(" 					   CHR(13), ")
				.append(" 					   ' '), ")
				.append(" 			   CHR(9), ")
				.append(" 			   ' ') AS \"Observações RIM\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_SIM_NAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.BL_SEGURADO) AS \"Segurado\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_SIM_NAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = RI.BL_SALVADOS) AS \"Salvados\", ")
				.append(" (SELECT TO_CHAR(WM_CONCAT(F2.SG_FILIAL || ' ' || ")
				.append(" 						  DS2.NR_DOCTO_SERVICO)) ")
				.append("    FROM DOCTO_SERVICO           DS2, ")
				.append(" 		FILIAL                  F2, ")
				.append(" 		MDA_SALVADO_INDENIZACAO MD ")
				.append("   WHERE DS2.ID_FILIAL_ORIGEM = F2.ID_FILIAL ")
				.append(" 	AND MD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND MD.ID_MDA = DS2.ID_DOCTO_SERVICO ")
				.append(" 	AND ROWNUM <= 50) AS \"MDAs\", ")
				.append(" LJ.ID_LOTE_JDE_RIM AS \"Número do lote\", ")
				.append(" TRUNC(LJ.DH_LOTE_JDE_RIM) AS \"Data do lote\", ")
				.append(" (SELECT TRUNC(ER.DH_EVENTO_RIM) ")
				.append("    FROM EVENTO_RIM ER ")
				.append("   WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND ER.ID_EVENTO_RIM = ")
				.append(" 		(SELECT MAX(ER2.ID_EVENTO_RIM) ")
				.append(" 		   FROM EVENTO_RIM ER2 ")
				.append(" 		  WHERE ER2.ID_RECIBO_INDENIZACAO = ")
				.append(" 				ER.ID_RECIBO_INDENIZACAO ")
				.append(" 			AND ER2.TP_EVENTO_INDENIZACAO = 'CR')) AS \"Data cancelamento\", ")
				.append(" (SELECT TRUNC(ER.DH_EVENTO_RIM) ")
				.append("    FROM EVENTO_RIM ER ")
				.append("   WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND ER.ID_EVENTO_RIM = ")
				.append(" 		(SELECT MAX(ER2.ID_EVENTO_RIM) ")
				.append(" 		   FROM EVENTO_RIM ER2 ")
				.append(" 		  WHERE ER2.ID_RECIBO_INDENIZACAO = ")
				.append(" 				ER.ID_RECIBO_INDENIZACAO ")
				.append(" 			AND ER2.TP_EVENTO_INDENIZACAO = 'LI')) AS \"Data liberação pagamento\", ")
				.append(" (SELECT TRUNC(ER.DH_EVENTO_RIM) ")
				.append("    FROM EVENTO_RIM ER ")
				.append("   WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND ER.ID_EVENTO_RIM = ")
				.append(" 		(SELECT MAX(ER2.ID_EVENTO_RIM) ")
				.append(" 		   FROM EVENTO_RIM ER2 ")
				.append(" 		  WHERE ER2.ID_RECIBO_INDENIZACAO = ")
				.append(" 				ER.ID_RECIBO_INDENIZACAO ")
				.append(" 			AND ER2.TP_EVENTO_INDENIZACAO = 'EJ')) AS \"Data envio JDE\", ")
				.append(" (SELECT TRUNC(ER.DH_EVENTO_RIM) ")
				.append("    FROM EVENTO_RIM ER ")
				.append("   WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND ER.ID_EVENTO_RIM = ")
				.append(" 		(SELECT MAX(ER2.ID_EVENTO_RIM) ")
				.append(" 		   FROM EVENTO_RIM ER2 ")
				.append(" 		  WHERE ER2.ID_RECIBO_INDENIZACAO = ")
				.append(" 				ER.ID_RECIBO_INDENIZACAO ")
				.append(" 			AND ER2.TP_EVENTO_INDENIZACAO = 'RP')) AS \"Data retorno pagto banco\", ")
				.append(" (SELECT TRUNC(ER.DH_EVENTO_RIM) ")
				.append("    FROM EVENTO_RIM ER ")
				.append("   WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append(" 	AND ER.ID_EVENTO_RIM = ")
				.append(" 		(SELECT MAX(ER2.ID_EVENTO_RIM) ")
				.append(" 		   FROM EVENTO_RIM ER2 ")
				.append(" 		  WHERE ER2.ID_RECIBO_INDENIZACAO = ")
				.append(" 				ER.ID_RECIBO_INDENIZACAO ")
				.append(" 			AND ER2.TP_EVENTO_INDENIZACAO IN ('PA', 'PM'))) AS \"Data pagamento JDE\", ")
				.append(" NVL((SELECT TO_CHAR(WM_CONCAT(NFC.NR_NOTA_FISCAL)) ")
				.append(" 	  FROM RECIBO_INDENIZACAO_NF    RNF, ")
				.append(" 		   NOTA_FISCAL_CONHECIMENTO NFC ")
				.append(" 	 WHERE RNF.ID_DOCTO_SERVICO_INDENIZACAO = ")
				.append(" 		   DSI.ID_DOCTO_SERVICO_INDENIZACAO ")
				.append(" 	   AND RNF.ID_NOTA_FISCAL_CONHECIMENTO = ")
				.append(" 		   NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
				.append(" 	   AND ROWNUM <= 50), ")
				.append(" 	(SELECT TO_CHAR(WM_CONCAT(NFC.NR_NOTA_FISCAL)) ")
				.append(" 	   FROM NOTA_FISCAL_CONHECIMENTO NFC ")
				.append(" 	  WHERE NFC.ID_CONHECIMENTO = DSI.ID_DOCTO_SERVICO ")
				.append(" 		AND ROWNUM <= 50)) AS \"Notas fiscais\"");
	}
	
	private StringBuilder appendColunasDoctoServico(StringBuilder sql) {
		return sql
				.append(" FORIG.SG_FILIAL AS \"Filial de origem CTRC\", ")
				.append(" DS.NR_DOCTO_SERVICO AS \"Número\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO') ")
				.append(" 	AND VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO) AS \"Tipo documento\", ")
				.append(" FDEST.SG_FILIAL AS \"Filial de destino\", ")
				.append(" TRUNC(CAST(DS.DH_EMISSAO AS DATE)) AS \"Data de emissão\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_FRETE') ")
				.append(" 	AND VL_VALOR_DOMINIO = CO.TP_FRETE) AS \"Tipo de frete\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_CONHECIMENTO') ")
				.append(" 	AND VL_VALOR_DOMINIO = CO.TP_CONHECIMENTO) AS \"Tipo de conhecimento\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_CALCULO_FRETE') ")
				.append(" 	AND VL_VALOR_DOMINIO = DS.TP_CALCULO_PRECO) AS \"Tipo de cálculo\", ")
				.append(" DS.DT_PREV_ENTREGA AS \"Data prevista de entrega\", ")
				.append(" (SELECT TRUNC(CAST(MAX(MED.DH_OCORRENCIA) AS DATE)) DH_OCORRENCIA ")
				.append("    FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("   WHERE MED.ID_OCORRENCIA_ENTREGA = 5 ")
				.append(" 	AND MED.TP_SITUACAO_DOCUMENTO <> 'CANC' ")
				.append(" 	AND MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO) AS \"Data de entrega\", ")
				.append(" DS.NR_DIAS_REAL_ENTREGA AS \"Número de dias entrega\", ")
				.append(" DS.VL_MERCADORIA AS \"Valor mercadoria\", ")
				.append(" DS.VL_TOTAL_DOC_SERVICO AS \"Valor frete total\", ")
				.append(" DS.VL_FRETE_LIQUIDO AS \"Valor frete líquido\", ")
				.append(" DS.VL_IMPOSTO AS \"Valor ICMS\", ")
				.append(" DS.VL_ICMS_ST AS \"Valor ICMS ST\", ")
				.append(" DEV.VL_DEVIDO AS \"Valor devido frete\", ")
				.append(" DS.PS_AFERIDO AS \"Peso aferido\", ")
				.append(" DS.PS_REAL AS \"Peso real\", ")
				.append(" DS.QT_VOLUMES AS \"Qtd volumes\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_TIPO_PREJUIZO') ")
				.append(" 	AND VL_VALOR_DOMINIO = SDS.TP_PREJUIZO) AS \"Tipo de prejuízo\", ")
				.append(" SDS.VL_PREJUIZO AS \"Valor prejuízo\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_SIM_NAO') ")
				.append(" 	AND VL_VALOR_DOMINIO = SDS.BL_PREJUIZO_PROPRIO) AS \"Prejuízo próprio\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = ")
				.append(" 				'DM_STATUS_COBRANCA_DOCTO_SERVICO') ")
				.append(" 	AND VL_VALOR_DOMINIO = DEV.TP_SITUACAO_COBRANCA) AS \"Situação cobrança frete\", ")
				.append(" DEV.DT_LIQUIDACAO AS \"Data de liquidação frete\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PREM.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PREM.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PREM.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ remetente\", ")
				.append(" PREM.NM_PESSOA AS \"Razão social remetente\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PDES.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PDES.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PDES.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ destinatário\", ")
				.append(" PDES.NM_PESSOA AS \"Razão social destinatário\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PDEV.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PDEV.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PDEV.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ devedor\", ")
				.append(" PDEV.NM_PESSOA AS \"Razão social devedor\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PCONS.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PCONS.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PCONS.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ consignatário\", ")
				.append(" PCONS.NM_PESSOA AS \"Razão social consignatário\", ")
				.append(" CASE ")
				.append("   WHEN LENGTH(PREDE.NR_IDENTIFICACAO) < 12 THEN ")
				.append("     REGEXP_REPLACE(LPAD(PREDE.NR_IDENTIFICACAO, 11, '0'), '([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})','\\1.\\2.\\3-\\4') ")
				.append("   ELSE ")
				.append("     REGEXP_REPLACE(LPAD(PREDE.NR_IDENTIFICACAO, 14, '0'),'([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})','\\1.\\2.\\3/\\4-\\5') ")
				.append(" END AS \"CNPJ redespacho\", ")
				.append(" PREDE.NM_PESSOA AS \"Razão social redespacho\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_MODAL') ")
				.append(" 	AND VL_VALOR_DOMINIO = S.TP_MODAL) AS \"Modal\", ")
				.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("    FROM VALOR_DOMINIO ")
				.append("   WHERE ID_DOMINIO IN ")
				.append(" 		(SELECT ID_DOMINIO ")
				.append(" 		   FROM DOMINIO ")
				.append(" 		  WHERE NM_DOMINIO = 'DM_ABRANGENCIA') ")
				.append(" 	AND VL_VALOR_DOMINIO = S.TP_ABRANGENCIA) AS \"Abrangência\", ")
				.append(" VI18N(S.DS_SERVICO_I) AS \"Serviço\", ")
				.append(" CAST(NVL(SDS.DH_ENVIO_EMAIL_OCORRENCIA, ")
				.append(" 		 SDS.DH_GERACAO_CARTA_OCORRENCIA) AS DATE) AS \"Data comunicado PS\", ")
				.append(" CAST(NVL(SDS.DH_ENVIO_EMAIL_RETIFICACAO, ")
				.append(" 		 SDS.DH_GERACAO_CARTA_RETIFICACAO) AS DATE) AS \"Data atualização PS\", ")
				.append(" CAST(NVL(SDS.DH_ENVIO_EMAIL_FILIAL_RIM, ")
				.append(" 		 SDS.DH_GERACAO_FILIAL_RIM) AS DATE) AS \"Data comunicado RIM PS\"");
	}
	
	private StringBuilder appendColunasNotaFiscal(StringBuilder sql) {
		return sql
				.append(" NFC.NR_NOTA_FISCAL AS \"Nota fiscal número\", ")
				.append(" NFC.DS_SERIE AS \"Série\", ")
				.append(" NFC.DT_EMISSAO AS \"Data emissão\", ")
				.append(" NFC.NR_CHAVE AS \"Chave NF-e\"");
	}

	private StringBuilder appendFrom(StringBuilder sql) {
		return sql
				.append(" FROM (SELECT PS.*, ")
				.append("                NVL((SELECT SUM(DS2.VL_MERCADORIA) ")
				.append("                      FROM SINISTRO_DOCTO_SERVICO SDS2, ")
				.append("                           DOCTO_SERVICO          DS2 ")
				.append("                     WHERE SDS2.ID_PROCESSO_SINISTRO = ")
				.append("                           PS.ID_PROCESSO_SINISTRO ")
				.append("                       AND SDS2.ID_DOCTO_SERVICO = DS2.ID_DOCTO_SERVICO), ")
				.append("                    0) VL_MERCADORIA_CALC, ")
				.append("                NVL((SELECT SUM(SDS2.VL_PREJUIZO) ")
				.append("                      FROM SINISTRO_DOCTO_SERVICO SDS2 ")
				.append("                     WHERE SDS2.ID_PROCESSO_SINISTRO = ")
				.append("                           PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) VL_PREJUIZO_CARGA_CALC, ")
				.append("                NVL((SELECT SUM(CAS.VL_CUSTO_ADICIONAL) ")
				.append("                      FROM CUSTO_ADICIONAL_SINISTRO CAS ")
				.append("                     WHERE CAS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) VL_PREJUIZO_ADIC_CALC, ")
				.append("                NVL((SELECT SUM(SDS2.VL_PREJUIZO) ")
				.append("                      FROM SINISTRO_DOCTO_SERVICO SDS2 ")
				.append("                     WHERE SDS2.ID_PROCESSO_SINISTRO = ")
				.append("                           PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) + ")
				.append("                NVL((SELECT SUM(CAS.VL_CUSTO_ADICIONAL) ")
				.append("                      FROM CUSTO_ADICIONAL_SINISTRO CAS ")
				.append("                     WHERE CAS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) VL_PREJUIZO_CALC, ")
				.append("                NVL((SELECT SUM(SDS2.VL_PREJUIZO) ")
				.append("                      FROM SINISTRO_DOCTO_SERVICO SDS2 ")
				.append("                     WHERE SDS2.ID_PROCESSO_SINISTRO = ")
				.append("                           PS.ID_PROCESSO_SINISTRO ")
				.append("                           AND SDS2.BL_PREJUIZO_PROPRIO = 'S'), ")
				.append("                    0) VL_PREJUIZO_PROPRIO, ")
				.append("                NVL((SELECT SUM(RRP.VL_REEMBOLSO + RRP.VL_REEMBOLSO_AVULSO) ")
				.append("                      FROM RECIBO_REEMBOLSO_PROCESSO RRP ")
				.append("                     WHERE RRP.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) + ")
				.append("                NVL((SELECT SUM(CAS.VL_REEMBOLSADO) ")
				.append("                      FROM CUSTO_ADICIONAL_SINISTRO CAS ")
				.append("                     WHERE CAS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO), ")
				.append("                    0) VL_REEMBOLSO_CALC, ")
				.append("                NVL((SELECT SUM(DSI2.VL_INDENIZADO) ")
				.append("                      FROM SINISTRO_DOCTO_SERVICO    SDS2, ")
				.append("                           DOCTO_SERVICO_INDENIZACAO DSI2, ")
				.append("                           RECIBO_INDENIZACAO        RI2 ")
				.append("                     WHERE SDS2.ID_PROCESSO_SINISTRO = ")
				.append("                           PS.ID_PROCESSO_SINISTRO ")
				.append("                       AND SDS2.ID_DOCTO_SERVICO = DSI2.ID_DOCTO_SERVICO ")
				.append("                       AND DSI2.ID_RECIBO_INDENIZACAO = ")
				.append("                           RI2.ID_RECIBO_INDENIZACAO ")
				.append("                       AND RI2.TP_STATUS_INDENIZACAO <> 'C'), ")
				.append("                    0) VL_INDENIZADO_CALC, ")
				.append("                (SELECT TO_CHAR(WM_CONCAT(DISTINCT ")
				.append("                                          MUNFILOCALREG.SG_REGIONAL || ' - ' || ")
				.append("                                          MUNFILOCALREG.DS_REGIONAL)) ")
				.append("                   FROM (SELECT * ")
				.append("                           FROM MUNICIPIO_FILIAL MUNFILOCAL ")
				.append("                          WHERE TRUNC(SYSDATE) BETWEEN ")
				.append("                                MUNFILOCAL.DT_VIGENCIA_INICIAL AND ")
				.append("                                MUNFILOCAL.DT_VIGENCIA_FINAL) MUNFILOCAL, ")
				.append("                        (SELECT * ")
				.append("                           FROM REGIONAL_FILIAL MUNFILOCALREGFI ")
				.append("                          WHERE TRUNC(SYSDATE) BETWEEN ")
				.append("                                MUNFILOCALREGFI.DT_VIGENCIA_INICIAL AND ")
				.append("                                MUNFILOCALREGFI.DT_VIGENCIA_FINAL) MUNFILOCALREGFI, ")
				.append("                        REGIONAL MUNFILOCALREG ")
				.append("                  WHERE PS.ID_MUNICIPIO = MUNFILOCAL.ID_MUNICIPIO ")
				.append("                    AND MUNFILOCAL.ID_FILIAL = MUNFILOCALREGFI.ID_FILIAL ")
				.append("                    AND MUNFILOCALREGFI.ID_REGIONAL = ")
				.append("                        MUNFILOCALREG.ID_REGIONAL) DS_REGIONAL, ")
				.append("                (SELECT ASE.NR_APOLICE ")
				.append("                   FROM APOLICE_SEGURO ASE ")
				.append("                  WHERE ASE.ID_TIPO_SEGURO = PS.ID_TIPO_SEGURO ")
				.append("                    AND PS.DH_SINISTRO BETWEEN ASE.DT_VIGENCIA_INICIAL AND ")
				.append("                        ASE.DT_VIGENCIA_FINAL ")
				.append("                    AND ROWNUM = 1) NR_APOLICE, ")
				.append("                (SELECT PES.NM_PESSOA ")
				.append("                   FROM APOLICE_SEGURO ASE, ")
				.append("                        PESSOA         PES ")
				.append("                  WHERE ASE.ID_TIPO_SEGURO = PS.ID_TIPO_SEGURO ")
				.append("                    AND PS.DH_SINISTRO BETWEEN ASE.DT_VIGENCIA_INICIAL AND ")
				.append("                        ASE.DT_VIGENCIA_FINAL ")
				.append("                    AND ASE.ID_REGULADORA = PES.ID_PESSOA ")
				.append("                    AND ROWNUM = 1) NM_CORRETORA, ")
				.append("                (SELECT PES.NM_PESSOA ")
				.append("                   FROM APOLICE_SEGURO ASE, ")
				.append("                        PESSOA         PES ")
				.append("                  WHERE ASE.ID_TIPO_SEGURO = PS.ID_TIPO_SEGURO ")
				.append("                    AND PS.DH_SINISTRO BETWEEN ASE.DT_VIGENCIA_INICIAL AND ")
				.append("                        ASE.DT_VIGENCIA_FINAL ")
				.append("                    AND ASE.ID_SEGURADORA = PES.ID_PESSOA ")
				.append("                    AND ROWNUM = 1) NM_SEGURADORA, ")
				.append("                (SELECT VI18N(CAM.DS_CONTEUDO_ATRIBUTO_MODELO_I) ")
				.append("                   FROM MEIO_TRANSP_CONTEUDO_ATRIB MTCA, ")
				.append("                        CONTEUDO_ATRIBUTO_MODELO   CAM ")
				.append("                  WHERE MTCA.ID_MEIO_TRANSPORTE = PS.ID_MEIO_TRANSPORTE ")
				.append("                    AND MTCA.ID_CONTEUDO_ATRIBUTO_MODELO = ")
				.append("                        CAM.ID_CONTEUDO_ATRIBUTO_MODELO ")
				.append("                    AND CAM.ID_MODELO_MEIO_TRANSP_ATRIBUTO = ")
				.append("                        (SELECT TO_NUMBER(PG.DS_CONTEUDO) ")
				.append("                           FROM PARAMETRO_GERAL PG ")
				.append("                          WHERE PG.NM_PARAMETRO_GERAL = ")
				.append("                                'ID_ATRIB_FUNCAO_MEIO_TRANSP')) TP_VEICULO, ")
				.append("                (SELECT COUNT(*) ")
				.append("                   FROM SINISTRO_DOCTO_SERVICO SDS ")
				.append("                  WHERE SDS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO) QT_DOCUMENTOS, ")
				.append("                (SELECT TRUNC(CAST(NVL(SDS.DH_ENVIO_EMAIL_OCORRENCIA, ")
				.append("                                       SDS.DH_GERACAO_CARTA_OCORRENCIA) AS DATE)) ")
				.append("                   FROM SINISTRO_DOCTO_SERVICO SDS ")
				.append("                  WHERE SDS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO ")
				.append("                    AND (SDS.DH_ENVIO_EMAIL_OCORRENCIA IS NOT NULL OR ")
				.append("                        SDS.DH_GERACAO_CARTA_OCORRENCIA IS NOT NULL) ")
				.append("                    AND ROWNUM = 1) DT_OCORRENCIA, ")
				.append("                (SELECT TRUNC(CAST(NVL(SDS.DH_ENVIO_EMAIL_RETIFICACAO, ")
				.append("                                       SDS.DH_GERACAO_CARTA_RETIFICACAO) AS DATE)) ")
				.append("                   FROM SINISTRO_DOCTO_SERVICO SDS ")
				.append("                  WHERE SDS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO ")
				.append("                    AND (SDS.DH_ENVIO_EMAIL_RETIFICACAO IS NOT NULL OR ")
				.append("                        SDS.DH_GERACAO_CARTA_RETIFICACAO IS NOT NULL) ")
				.append("                    AND ROWNUM = 1) DT_RETIFICACAO, ")
				.append("                (SELECT TRUNC(CAST(NVL(SDS.DH_ENVIO_EMAIL_FILIAL_RIM, ")
				.append("                                       SDS.DH_GERACAO_FILIAL_RIM) AS DATE)) ")
				.append("                   FROM SINISTRO_DOCTO_SERVICO SDS ")
				.append("                  WHERE SDS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO ")
				.append("                    AND (SDS.DH_ENVIO_EMAIL_FILIAL_RIM IS NOT NULL OR ")
				.append("                        SDS.DH_GERACAO_FILIAL_RIM IS NOT NULL) ")
				.append("                    AND ROWNUM = 1) DT_FILIAL_RIM, ")
				.append("                (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
				.append("                   FROM VALOR_DOMINIO ")
				.append("                  WHERE ID_DOMINIO IN ")
				.append("                        (SELECT ID_DOMINIO ")
				.append("                           FROM DOMINIO ")
				.append("                          WHERE NM_DOMINIO = 'DM_SIM_NAO') ")
				.append("                    AND VL_VALOR_DOMINIO = ")
				.append("                        NVL((SELECT 'S' ")
				.append("                              FROM SINISTRO_DOCTO_SERVICO SDS ")
				.append("                             WHERE SDS.ID_PROCESSO_SINISTRO = ")
				.append("                                   PS.ID_PROCESSO_SINISTRO ")
				.append("                               AND SDS.TP_PREJUIZO IN ('P', 'T') ")
				.append("                               AND SDS.VL_PREJUIZO > 0 ")
				.append("                               AND NOT EXISTS (SELECT 1 ")
				.append("                                      FROM DOCTO_SERVICO_INDENIZACAO DSI, ")
				.append("                                           RECIBO_INDENIZACAO        RI ")
				.append("                                     WHERE DSI.ID_RECIBO_INDENIZACAO = ")
				.append("                                           RI.ID_RECIBO_INDENIZACAO ")
				.append("                                       AND DSI.ID_DOCTO_SERVICO = ")
				.append("                                           SDS.ID_DOCTO_SERVICO ")
				.append("                                       AND RI.TP_STATUS_INDENIZACAO <> 'C' ")
				.append("                                       AND ROWNUM = 1) ")
				.append("                               AND ROWNUM = 1), ")
				.append("                            'N')) CTO_SEM_RIM, ")
				.append("                (SELECT MIN(RI.DT_EMISSAO) ")
				.append("                   FROM SINISTRO_DOCTO_SERVICO    SDS, ")
				.append("                        DOCTO_SERVICO_INDENIZACAO DSI, ")
				.append("                        RECIBO_INDENIZACAO        RI ")
				.append("                  WHERE SDS.ID_PROCESSO_SINISTRO = PS.ID_PROCESSO_SINISTRO ")
				.append("                    AND DSI.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
				.append("                    AND DSI.ID_DOCTO_SERVICO = SDS.ID_DOCTO_SERVICO ")
				.append("                    AND RI.TP_STATUS_INDENIZACAO NOT IN ('C', 'P')) DT_RIM_SEM_PAGTO ")
				.append("           FROM PROCESSO_SINISTRO PS) PS, ")
				.append("        USUARIO USA, ")
				.append("        USUARIO USF, ")
				.append("        TIPO_SEGURO TS, ")
				.append("        TIPO_SINISTRO TSIN, ")
				.append("        MEIO_TRANSPORTE MT, ")
				.append("        MEIO_TRANSPORTE_RODOVIARIO MTR, ")
				.append("        MOTORISTA MOTO, ")
				.append("        PESSOA PMOTO, ")
				.append("        RODOVIA RODO, ")
				.append("        FILIAL FILOCAL, ")
				.append("        AEROPORTO AEROLOCAL, ")
				.append("        MUNICIPIO MUNLOCAL, ")
				.append("        UNIDADE_FEDERATIVA UFLOCAL, ")
				.append("        MOEDA MO, ")
				.append("        SITUACAO_REEMBOLSO SR, ")
				.append("        SINISTRO_DOCTO_SERVICO SDS, ")
				.append("        DOCTO_SERVICO_INDENIZACAO DSI, ")
				.append("        RECIBO_INDENIZACAO RI, ")
				.append("        FILIAL FI, ")
				.append("        (SELECT * ")
				.append("           FROM REGIONAL_FILIAL RF ")
				.append("          WHERE TRUNC(SYSDATE) BETWEEN RF.DT_VIGENCIA_INICIAL AND ")
				.append("                RF.DT_VIGENCIA_FINAL) RF, ")
				.append("        REGIONAL REG, ")
				.append("        DOCTO_SERVICO DS, ")
				.append("        CONHECIMENTO CO, ")
				.append("        NOTA_FISCAL_CONHECIMENTO NFC, ")
				.append("        FILIAL FORIG, ")
				.append("        FILIAL FDEST, ")
				.append("        DEVEDOR_DOC_SERV_FAT DEV, ")
				.append("        DEVEDOR_DOC_SERV DEVS, ")
				.append("        PESSOA PREM, ")
				.append("        PESSOA PDES, ")
				.append("        PESSOA PDEV, ")
				.append("        PESSOA PCONS, ")
				.append("        PESSOA PREDE, ")
				.append("        PESSOA PBEN, ")
				.append("        PESSOA PFAV, ").append("        SERVICO S, ")
				.append("        PRODUTO P, ")
				.append("        NATUREZA_PRODUTO NP, ")
				.append("        BANCO BA, ")
				.append("        AGENCIA_BANCARIA AB, ")
				.append("        PROCESSO_SINISTRO PS2, ")
				.append("        TIPO_SEGURO TS2, ")
				.append("        TIPO_SINISTRO TSIN2, ")
				.append("        FILIAL FSIN, ")
				.append("        FILIAL FROTA, ")
				.append("        OCORRENCIA_NAO_CONFORMIDADE ONC, ")
				.append("        NAO_CONFORMIDADE NCONC, ")
				.append("        FILIAL FNCONC, ")
				.append("        MOTIVO_ABERTURA_NC MANC, ")
				.append("        LOTE_JDE_RIM LJ ");
	}
	
	private StringBuilder appendRelationships(StringBuilder sql) {
		return sql
				.append("  WHERE PS.ID_USUARIO_ABERTURA = USA.ID_USUARIO(+) ")
				.append("    AND PS.ID_USUARIO_FECHAMENTO = USF.ID_USUARIO(+) ")
				.append("    AND PS.ID_TIPO_SEGURO = TS.ID_TIPO_SEGURO(+) ")
				.append("    AND PS.ID_TIPO_SINISTRO = TSIN.ID_TIPO_SINISTRO(+) ")
				.append("    AND PS.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE(+) ")
				.append("    AND MT.ID_MEIO_TRANSPORTE = MTR.ID_MEIO_TRANSPORTE(+) ")
				.append("    AND PS.ID_MOTORISTA = MOTO.ID_MOTORISTA(+) ")
				.append("    AND MOTO.ID_MOTORISTA = PMOTO.ID_PESSOA(+) ")
				.append("    AND PS.ID_RODOVIA = RODO.ID_RODOVIA(+) ")
				.append("    AND PS.ID_FILIAL = FILOCAL.ID_FILIAL(+) ")
				.append("    AND PS.ID_AEROPORTO = AEROLOCAL.ID_AEROPORTO(+) ")
				.append("    AND PS.ID_MUNICIPIO = MUNLOCAL.ID_MUNICIPIO(+) ")
				.append("    AND MUNLOCAL.ID_UNIDADE_FEDERATIVA = UFLOCAL.ID_UNIDADE_FEDERATIVA(+) ")
				.append("    AND PS.ID_MOEDA = MO.ID_MOEDA(+) ")
				.append("    AND PS.ID_SITUACAO_REEMBOLSO = SR.ID_SITUACAO_REEMBOLSO(+) ")
				.append("    AND PS.ID_PROCESSO_SINISTRO = SDS.ID_PROCESSO_SINISTRO(+) ")
				.append("    AND SDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO(+) ")
				.append("    AND DS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO(+) ")
				.append("    AND DS.ID_DOCTO_SERVICO = DSI.ID_DOCTO_SERVICO(+) ")
				.append("    AND DSI.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO(+) ")
				.append("    AND RI.ID_FILIAL = FI.ID_FILIAL(+) ")
				.append("    AND FI.ID_FILIAL = RF.ID_FILIAL(+) ")
				.append("    AND RF.ID_REGIONAL = REG.ID_REGIONAL(+) ")
				.append("    AND DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO(+) ")
				.append("    AND DS.ID_FILIAL_ORIGEM = FORIG.ID_FILIAL(+) ")
				.append("    AND DS.ID_FILIAL_DESTINO = FDEST.ID_FILIAL(+) ")
				.append("    AND DS.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO(+) ")
				.append("    AND DS.ID_DOCTO_SERVICO = DEVS.ID_DOCTO_SERVICO(+) ")
				.append("    AND DS.ID_CLIENTE_REMETENTE = PREM.ID_PESSOA(+) ")
				.append("    AND DS.ID_CLIENTE_DESTINATARIO = PDES.ID_PESSOA(+) ")
				.append("    AND DS.ID_CLIENTE_CONSIGNATARIO = PCONS.ID_PESSOA(+) ")
				.append("    AND DS.ID_CLIENTE_REDESPACHO = PREDE.ID_PESSOA(+) ")
				.append("    AND RI.ID_BENEFICIARIO = PBEN.ID_PESSOA(+) ")
				.append("    AND RI.ID_FAVORECIDO = PFAV.ID_PESSOA(+) ")
				.append("    AND DEVS.ID_CLIENTE = PDEV.ID_PESSOA(+) ")
				.append("    AND DS.ID_SERVICO = S.ID_SERVICO(+) ")
				.append("    AND DSI.ID_PRODUTO = P.ID_PRODUTO(+) ")
				.append("    AND CO.ID_NATUREZA_PRODUTO = NP.ID_NATUREZA_PRODUTO(+) ")
				.append("    AND RI.ID_BANCO = BA.ID_BANCO(+) ")
				.append("    AND RI.ID_AGENCIA_BANCARIA = AB.ID_AGENCIA_BANCARIA(+) ")
				.append("    AND RI.ID_PROCESSO_SINISTRO = PS2.ID_PROCESSO_SINISTRO(+) ")
				.append("    AND PS2.ID_TIPO_SEGURO = TS2.ID_TIPO_SEGURO(+) ")
				.append("    AND PS2.ID_TIPO_SINISTRO = TSIN2.ID_TIPO_SINISTRO(+) ")
				.append("    AND DSI.ID_FILIAL_SINISTRO = FSIN.ID_FILIAL(+) ")
				.append("    AND DSI.ID_ROTA_SINISTRO = FROTA.ID_FILIAL(+) ")
				.append("    AND DSI.ID_OCORRENCIA_NAO_CONFORMIDADE = ")
				.append("        ONC.ID_OCORRENCIA_NAO_CONFORMIDADE(+) ")
				.append("    AND ONC.ID_NAO_CONFORMIDADE = NCONC.ID_NAO_CONFORMIDADE(+) ")
				.append("    AND NCONC.ID_FILIAL = FNCONC.ID_FILIAL(+) ")
				.append("    AND ONC.ID_MOTIVO_ABERTURA_NC = MANC.ID_MOTIVO_ABERTURA_NC(+) ")
				.append("    AND RI.ID_LOTE_JDE_RIM = LJ.ID_LOTE_JDE_RIM(+) ");
	}
	
	private StringBuilder appendFiltrosProcessoSinistro(StringBuilder sql, TypedFlatMap parameters) {
		
		// Processo de sinistro
		if(!parameters.getString("nrProcessoSinistro").isEmpty()) {
			sql.append(" AND PS.NR_PROCESSO_SINISTRO = '").append(parameters.getString("nrProcessoSinistro")).append("'");
		}
		
		// Manifesto
		if(parameters.getLong("manifesto.idManifesto") != null) {
			sql.append(" AND (EXISTS (SELECT 1 ")
			.append("           FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
			.append("          WHERE MED.ID_DOCTO_SERVICO = SDS.ID_DOCTO_SERVICO ")
			.append("            AND MED.ID_MANIFESTO_ENTREGA = ").append(parameters.getLong("manifesto.idManifesto"))
			.append("            AND ROWNUM = 1) OR EXISTS ")
			.append(" (SELECT 1 ")
			.append("    FROM MANIFESTO_NACIONAL_CTO MNC ")
			.append("   WHERE MNC.ID_CONHECIMENTO = SDS.ID_DOCTO_SERVICO ")
			.append("     AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = ").append(parameters.getLong("manifesto.idManifesto"))
			.append("     AND ROWNUM = 1) OR EXISTS ")
			.append(" (SELECT 1 ")
			.append("    FROM MANIFESTO_INTERNAC_CTO MIC ")
			.append("   WHERE MIC.ID_CTO_INTERNACIONAL = SDS.ID_DOCTO_SERVICO ")
			.append("     AND MIC.ID_MANIFESTO_INTERNACIONAL = ").append(parameters.getLong("manifesto.idManifesto"))
			.append("     AND ROWNUM = 1))  ");
		}
		
		// Data sinistro
		if(parameters.getYearMonthDay("dhSinistroInicial") != null) {
			final String dhSinistroInicial = parameters.getYearMonthDay("dhSinistroInicial").toString("dd/MM/yyyy");
			sql.append(" AND PS.dh_sinistro >= TO_DATE('")
					.append(dhSinistroInicial)
					.append(" 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");
		}
		if(parameters.getYearMonthDay("dhSinistroFinal") != null) {
			final String dhSinistroFinal = parameters.getYearMonthDay("dhSinistroFinal").toString("dd/MM/yyyy");
			sql.append(" AND PS.dh_sinistro <= TO_DATE('")
					.append(dhSinistroFinal)
					.append(" 23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		// Tipo de sinistro
		if(parameters.getLong("tipoSinistro.idTipoSinistro") != null) {
			sql.append(" AND PS.ID_TIPO_SINISTRO = ").append(parameters.getLong("tipoSinistro.idTipoSinistro"));
		}
		
		// Rodovia
		if(parameters.getLong("rodovia.idRodovia") != null) {
			sql.append(" AND PS.ID_RODOVIA = ").append(parameters.getLong("rodovia.idRodovia"));
		}
		
		// Filial sinistro
		if(parameters.getLong("filialSinistro.idFilial") != null) {
			sql.append(" AND PS.ID_FILIAL = ").append(parameters.getLong("filialSinistro.idFilial"));
		}
		
		// Aeroporto
		if(parameters.getLong("aeroporto.idAeroporto") != null) {
			sql.append(" AND PS.ID_AEROPORTO = ").append(parameters.getLong("aeroporto.idAeroporto"));
		}
		
		// Situação
		if(!parameters.getString("situacaoProcessoSinistro").isEmpty()) {
			sql.append(" AND DECODE(PS.DH_FECHAMENTO, NULL, 'A', 'F') = '").append(parameters.getString("situacaoProcessoSinistro")).append("'");
		}
		
		// Controle de carga
		if(parameters.getLong("controleCarga.idControleCarga") != null) {
			sql.append(" AND (EXISTS (SELECT 1 ")
			.append("           FROM MANIFESTO_ENTREGA_DOCUMENTO MED, MANIFESTO MAN ")
			.append("          WHERE MED.ID_DOCTO_SERVICO = SDS.ID_DOCTO_SERVICO ")
			.append("            AND MED.ID_MANIFESTO_ENTREGA = MAN.ID_MANIFESTO ")
			.append("            AND MAN.ID_CONTROLE_CARGA = ").append(parameters.getLong("controleCarga.idControleCarga"))
			.append("            AND ROWNUM = 1) OR EXISTS ")
			.append(" (SELECT 1 ")
			.append("    FROM MANIFESTO_NACIONAL_CTO MNC, MANIFESTO MAN ")
			.append("   WHERE MNC.ID_CONHECIMENTO = SDS.ID_DOCTO_SERVICO ")
			.append("     AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MAN.ID_MANIFESTO ")
			.append("     AND MAN.ID_CONTROLE_CARGA = ").append(parameters.getLong("controleCarga.idControleCarga"))
			.append("     AND ROWNUM = 1) OR EXISTS ")
			.append(" (SELECT 1 ")
			.append("    FROM MANIFESTO_INTERNAC_CTO MIC, MANIFESTO MAN ")
			.append("   WHERE MIC.ID_CTO_INTERNACIONAL = SDS.ID_DOCTO_SERVICO ")
			.append("     AND MIC.ID_MANIFESTO_INTERNACIONAL = MAN.ID_MANIFESTO ")
			.append("     AND MAN.ID_CONTROLE_CARGA = ").append(parameters.getLong("controleCarga.idControleCarga"))
			.append("     AND ROWNUM = 1)) ");
		}
		
		// Frota/placa
		if(parameters.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
			sql.append(" AND PS.ID_MEIO_TRANSPORTE = ").append(parameters.getLong("meioTransporteRodoviario.idMeioTransporte"));
		}
		
		// Local sinistro
		if(!parameters.getString("localSinistro").isEmpty()) {
			sql.append(" AND PS.TP_LOCAL_SINISTRO = '").append(parameters.getString("localSinistro")).append("'");
		}
		
		// Tipo de seguro
		if(parameters.getLong("tipoSeguro.idTipoSeguro") != null) {
			sql.append(" AND PS.ID_TIPO_SEGURO = ").append(parameters.getLong("tipoSeguro.idTipoSeguro"));
		}
		
		// Município
		if(parameters.getLong("municipio.idMunicipio") != null) {
			sql.append(" AND PS.ID_MUNICIPIO = ").append(parameters.getLong("municipio.idMunicipio"));
		}
		
		// UF
		if(parameters.getLong("unidadeFederativa.idUnidadeFederativa") != null) {
			sql.append(" AND MUNLOCAL.ID_UNIDADE_FEDERATIVA = ").append(parameters.getLong("unidadeFederativa.idUnidadeFederativa"));
		}
		
		// Usuário responsável
		if(parameters.getLong("usuario.idUsuario") != null) {
			sql.append(" AND PS.ID_USUARIO_ABERTURA = ").append(parameters.getLong("usuario.idUsuario"));
		}
		
		return sql;
	}
	
	private StringBuilder appendFiltrosRIM(StringBuilder sql, TypedFlatMap parameters) {
		
		// Filial
		if(parameters.getLong("filial.idFilial") != null) {
			sql.append(" AND RI.ID_FILIAL = ").append(parameters.getLong("filial.idFilial"));
		}
		
		// Número
		if(parameters.getInteger("nrReciboIndenizacaoInicial") != null) {
			sql.append(" AND RI.NR_RECIBO_INDENIZACAO >= ").append(parameters.getLong("nrReciboIndenizacaoInicial"));
		}
		if(parameters.getInteger("nrReciboIndenizacaoFinal") != null) {
			sql.append(" AND RI.NR_RECIBO_INDENIZACAO <= ").append(parameters.getLong("nrReciboIndenizacaoFinal"));
		}
		
		// Tipo de indenização
		if(!parameters.getString("tpIndenizacao").isEmpty()) {
			sql.append(" AND RI.TP_INDENIZACAO = '").append(parameters.getString("tpIndenizacao")).append("'");
		}
		
		// Status
		if(!parameters.getString("tpStatusIndenizacao").isEmpty()) {
			sql.append(" AND RI.TP_STATUS_INDENIZACAO = '").append(parameters.getString("tpStatusIndenizacao")).append("'");
		}
		
		// Situação da aprovação
		if(!parameters.getString("tpSituacaoWorkFlow").isEmpty()) {
			sql.append(" AND RI.TP_SITUACAO_WORKFLOW = '").append(parameters.getString("tpSituacaoWorkFlow")).append("'");
		}
		
		// Natureza do produto
		if(parameters.getLong("naturezaProduto.idNaturezaProduto") != null) {
			sql.append(" AND NP.ID_NATUREZA_PRODUTO = ").append(parameters.getLong("naturezaProduto.idNaturezaProduto"));
		}
		
		// Forma de pagamento
		if(!parameters.getString("formaPagto").isEmpty()) {
			sql.append(" AND RI.TP_FORMA_PAGAMENTO = '").append(parameters.getString("formaPagto")).append("'");
		}
		
		// Valor indenização
		if(parameters.getBigDecimal("valorIndenizacaoInicial") != null) {
			sql.append(" AND RI.VL_INDENIZACAO >= ").append(parameters.getBigDecimal("valorIndenizacaoInicial"));
		}
		if(parameters.getBigDecimal("valorIndenizacaoFinal") != null) {
			sql.append(" AND RI.VL_INDENIZACAO <= ").append(parameters.getBigDecimal("valorIndenizacaoFinal"));
		}
		
		// Filial debitada
		if(parameters.getLong("filialDebitada.idFilial") != null) {
			sql.append(" AND EXISTS (SELECT 1 ")
			.append("           FROM FILIAL_DEBITADA FD ")
			.append("          WHERE FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO ")
			.append("            AND FD.ID_FILIAL = ").append(parameters.getLong("filialDebitada.idFilial"))
			.append("            AND ROWNUM = 1) ");
		}
		
		// Filial ocorrência 1
		if(parameters.getLong("filialOcorrencia1.idFilial") != null) {
			sql.append(" AND DSI.ID_FILIAL_SINISTRO = ").append(parameters.getLong("filialOcorrencia1.idFilial"));
		}
		
		// Filial ocorrência 2
		if(parameters.getLong("filialOcorrencia2.idFilial") != null) {
			sql.append(" AND DSI.ID_ROTA_SINISTRO = ").append(parameters.getLong("filialOcorrencia2.idFilial"));
		}
		
		// Beneficiário
		if(parameters.getLong("beneficiario.idPessoa") != null) {
			sql.append(" AND PBEN.ID_PESSOA = ").append(parameters.getLong("beneficiario.idPessoa"));
		}
		
		// Favorecido
		if(parameters.getLong("favorecido.idPessoa") != null) {
			sql.append(" AND PFAV.ID_PESSOA = ").append(parameters.getLong("favorecido.idPessoa"));
		}
		
		// Data de emissão
		if(parameters.getYearMonthDay("dataInicial") != null) {
			final String dataInicial = parameters.getYearMonthDay("dataInicial").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_EMISSAO >= TO_DATE('")
					.append(dataInicial)
					.append("', 'DD/MM/YYYY')");
		}
		if(parameters.getYearMonthDay("dataFinal") != null) {
			final String dataFinal = parameters.getYearMonthDay("dataFinal").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_EMISSAO <= TO_DATE('")
					.append(dataFinal)
					.append("', 'DD/MM/YYYY')");
		}
		
		// Data liberação de pgto
		if(parameters.getYearMonthDay("dataLiberacaoPgtoInicial") != null) {
			final String dataLiberacaoPgtoInicial = parameters.getYearMonthDay("dataLiberacaoPgtoInicial").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_LIBERACAO_PAGAMENTO >= TO_DATE('")
					.append(dataLiberacaoPgtoInicial)
					.append("', 'DD/MM/YYYY')");
		}
		if(parameters.getYearMonthDay("dataLiberacaoPgtoFinal") != null) {
			final String dataLiberacaoPgtoFinal = parameters.getYearMonthDay("dataLiberacaoPgtoFinal").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_LIBERACAO_PAGAMENTO <= TO_DATE('")
					.append(dataLiberacaoPgtoFinal)
					.append("', 'DD/MM/YYYY')");
		}
		
		// Data programada de pgto
		if(parameters.getYearMonthDay("dataProgramadaPagamentoInicial") != null) {
			final String dataProgramadaPagamentoInicial = parameters.getYearMonthDay("dataProgramadaPagamentoInicial").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_PROGRAMADA_PAGAMENTO >= TO_DATE('")
					.append(dataProgramadaPagamentoInicial)
					.append("', 'DD/MM/YYYY')");
		}
		if(parameters.getYearMonthDay("dataProgramadaPagamentoFinal") != null) {
			final String dataProgramadaPagamentoFinal = parameters.getYearMonthDay("dataProgramadaPagamentoFinal").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_PROGRAMADA_PAGAMENTO <= TO_DATE('")
					.append(dataProgramadaPagamentoFinal)
					.append("', 'DD/MM/YYYY')");
		}
		
		// Data de pagamento
		if(parameters.getYearMonthDay("dataPagtoInicial") != null) {
			final String dataPagtoInicial = parameters.getYearMonthDay("dataPagtoInicial").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_PAGAMENTO_EFETUADO >= TO_DATE('")
					.append(dataPagtoInicial)
					.append("', 'DD/MM/YYYY')");
		}

		if(parameters.getYearMonthDay("dataPagtoFinal") != null) {
			final String dataPagtoFinal = parameters.getYearMonthDay("dataPagtoFinal").toString("dd/MM/yyyy");
			sql.append(" AND RI.DT_PAGAMENTO_EFETUADO <= TO_DATE('")
					.append(dataPagtoFinal)
					.append("', 'DD/MM/YYYY')");
		}
		
		// Data lote
		if(parameters.getYearMonthDay("dataLoteInicial") != null) {
			final String dataLoteInicial = parameters.getYearMonthDay("dataLoteInicial").toString("dd/MM/yyyy");
			sql.append(" AND LJ.DH_LOTE_JDE_RIM >= TO_DATE('")
					.append(dataLoteInicial)
					.append(" 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");
		}
		if(parameters.getYearMonthDay("dataLoteFinal") != null) {
			final String dataLoteFinal = parameters.getYearMonthDay("dataLoteFinal").toString("dd/MM/yyyy");
			sql.append(" AND LJ.DH_LOTE_JDE_RIM  <= TO_DATE('")
					.append(dataLoteFinal)
					.append(" 23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		// Número do lote
		if(parameters.getLong("nrLote") != null) {
			sql.append(" AND LJ.ID_LOTE_JDE_RIM = ").append(parameters.getLong("nrLote"));
		}
		
		// Filial RNC
		if(parameters.getLong("filialRnc.idFilial") != null) {
			sql.append(" AND NCONC.ID_FILIAL = ").append(parameters.getLong("filialRnc.idFilial"));
		}
		
		// Número RNC
		if(parameters.getInteger("numeroRncInicial") != null) {
			sql.append(" AND NCONC.NR_NAO_CONFORMIDADE >= ").append(parameters.getInteger("numeroRncInicial"));
		}
		if(parameters.getInteger("numeroRncFinal") != null) {
			sql.append(" AND NCONC.NR_NAO_CONFORMIDADE <= ").append(parameters.getInteger("numeroRncFinal"));
		}
		
		// Data emissão RNC
		if(parameters.getYearMonthDay("dataEmissaoRncInicial") != null) {
			final String dataEmissaoRncInicial = parameters.getYearMonthDay("dataEmissaoRncInicial").toString("dd/MM/yyyy");
			sql.append(" AND NCONC.DH_EMISSAO >= TO_DATE('")
					.append(dataEmissaoRncInicial)
					.append( "23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		if(parameters.getYearMonthDay("dataEmissaoRncFinal") != null) {
			final String dataEmissaoRncFinal = parameters.getYearMonthDay("dataEmissaoRncFinal").toString("dd/MM/yyyy");
			sql.append(" AND NCONC.DH_EMISSAO <= TO_DATE('")
					.append(dataEmissaoRncFinal)
					.append(" 23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		// Motivo de abertura
		if(parameters.getLong("motivoAbertura.idMotivoAberturaNc") != null) {
			sql.append(" AND MANC.ID_MOTIVO_ABERTURA_NC = ").append(parameters.getLong("motivoAbertura.idMotivoAberturaNc"));
		}
		
		// Salvados
		if(!parameters.getString("blSalvados").isEmpty()) {
			sql.append(" AND RI.BL_SALVADOS = '").append(parameters.getString("blSalvados")).append("'");
		}
		
		return sql;
	}
	
	private StringBuilder appendFiltrosDoctoServico(StringBuilder sql, TypedFlatMap parameters) {
		
		// Documento de serviço
		if(parameters.getLong("doctoServico.idDoctoServico") != null) {
			sql.append(" AND DS.ID_DOCTO_SERVICO = ").append(parameters.getLong("doctoServico.idDoctoServico"));
		}
		
		// Remetente
		if(parameters.getLong("doctoServico.clienteByIdClienteRemetente.idCliente") != null) {
			sql.append(" AND PREM.ID_PESSOA = ").append(parameters.getLong("doctoServico.clienteByIdClienteRemetente.idCliente"));
		}
		
		// Destinatário
		if(parameters.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente") != null) {
			sql.append(" AND PDES.ID_PESSOA = ").append(parameters.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"));
		}
		
		// Devedor
		if(parameters.getLong("devedor.idPessoa") != null) {
			sql.append(" AND PDEV.ID_PESSOA = ").append(parameters.getLong("devedor.idPessoa"));
		}
		
		// Modal
		if(!parameters.getString("modal").isEmpty()) {
			sql.append(" AND S.TP_MODAL = '").append(parameters.getString("modal")).append("'");
		}
		
		// Abrangência
		if(!parameters.getString("abrangencia").isEmpty()) {
			sql.append(" AND S.TP_ABRANGENCIA = '").append(parameters.getString("abrangencia")).append("'");
		}
		
		// Data de emissão
		if(parameters.getYearMonthDay("dataEmissaoDocInicial") != null) {
			final String dataEmissaoDocInicial = parameters.getYearMonthDay("dataEmissaoDocInicial").toString("dd/MM/yyyy");
			sql.append(" AND DS.DH_EMISSAO >= TO_DATE('")
					.append(dataEmissaoDocInicial)
					.append(" 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");

		}
		if(parameters.getYearMonthDay("dataEmissaoDocFinal") != null) {
			final String dataEmissaoDocFinal = parameters.getYearMonthDay("dataEmissaoDocFinal").toString("dd/MM/yyyy");
			sql.append(" AND DS.DH_EMISSAO <= TO_DATE('")
					.append(dataEmissaoDocFinal)
					.append(" 23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		// Serviço
		if(parameters.getLong("servico.idServico") != null) {
			sql.append(" AND S.ID_SERVICO = ").append(parameters.getLong("servico.idServico"));
		}
		
		return sql;
	}
	
	private StringBuilder appendOrderBy(StringBuilder sql, TypedFlatMap parameters) {
		
		boolean checkProcessosSinistro = parameters.getBoolean("checkProcessosSinistro");
		boolean checkRims = parameters.getBoolean("checkRims");
		boolean checkCtes = parameters.getBoolean("checkCtes");
		boolean checkNfs = parameters.getBoolean("checkNfs");
		
		sql.append(" ORDER BY ");
		
		if(checkProcessosSinistro) {
			sql.append("PS.DS_REGIONAL, PS.NR_PROCESSO_SINISTRO");
		}
		
		if(checkRims) {
			if(checkProcessosSinistro) {
				sql.append(",");
			}
			sql.append(" REG.SG_REGIONAL, REG.DS_REGIONAL, FI.SG_FILIAL, RI.NR_RECIBO_INDENIZACAO");
		}
		
		if(checkCtes) {
			if(checkProcessosSinistro || checkRims) {
				sql.append(",");
			}
			sql.append(" FORIG.SG_FILIAL, DS.NR_DOCTO_SERVICO, ")
			.append(" \"Tipo documento\", ")
			.append(" TRUNC(CAST(DS.DH_EMISSAO AS DATE))");
		}
		
		if(checkNfs) {
			if(checkProcessosSinistro || checkRims
					|| checkCtes) {
				sql.append(",");
			}
			sql.append(" NFC.NR_NOTA_FISCAL, NFC.DS_SERIE");
		}
		
		return sql;
	}
	
	private ConfigureSqlQuery configureSQL(final TypedFlatMap parameters) {
		return new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				if(parameters.getBoolean("checkProcessosSinistro")) {
					sqlQuery.addScalar("Regional", Hibernate.STRING);
					sqlQuery.addScalar("Número processo", Hibernate.STRING);
					sqlQuery.addScalar("Situação", Hibernate.STRING);
					sqlQuery.addScalar("Nº. boletim de ocorrência", Hibernate.STRING);
					sqlQuery.addScalar("Data/hora sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Data/hora abertura", Hibernate.STRING);
					sqlQuery.addScalar("Data/hora fechamento", Hibernate.STRING);
					sqlQuery.addScalar("Obs. fechamento", Hibernate.STRING);
					sqlQuery.addScalar("Usuário abertura", Hibernate.STRING);
					sqlQuery.addScalar("Usuário fechamento", Hibernate.STRING);
					sqlQuery.addScalar("Tipo seguro", Hibernate.STRING);
					sqlQuery.addScalar("Tipo sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Apólice", Hibernate.STRING);
					sqlQuery.addScalar("Corretora", Hibernate.STRING);
					sqlQuery.addScalar("Seguradora", Hibernate.STRING);
					sqlQuery.addScalar("Frota", Hibernate.STRING);
					sqlQuery.addScalar("Placa", Hibernate.STRING);
					sqlQuery.addScalar("Tipo de veículo", Hibernate.STRING);
					sqlQuery.addScalar("Certificado", Hibernate.LONG);
					sqlQuery.addScalar("CPF motorista", Hibernate.STRING);
					sqlQuery.addScalar("Nome", Hibernate.STRING);
					sqlQuery.addScalar("Tipo de vínculo", Hibernate.STRING);
					sqlQuery.addScalar("Descrição do sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Local sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Rodovia", Hibernate.STRING);
					sqlQuery.addScalar("Km", Hibernate.INTEGER);
					sqlQuery.addScalar("Filial sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Aeroporto", Hibernate.STRING);
					sqlQuery.addScalar("Local sinistro (outro)", Hibernate.STRING);
					sqlQuery.addScalar("Município", Hibernate.STRING);
					sqlQuery.addScalar("UF", Hibernate.STRING);
					sqlQuery.addScalar("Moeda", Hibernate.STRING);
					sqlQuery.addScalar("Valor da mercadoria", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor do prejuízo carga", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor do prejuízo adicional", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor do prejuízo total", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor do prejuízo próprio", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor do reembolso", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor da franquia", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor a receber", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor recuperado", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor indenizado", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Dif. indenizado reembolsado", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Dif. pgto. cliente", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Tempo de pgto", Hibernate.INTEGER);
					sqlQuery.addScalar("Situação do reembolso", Hibernate.STRING);
					sqlQuery.addScalar("Observações", Hibernate.STRING);
					sqlQuery.addScalar("Comunicado corretora", Hibernate.STRING);
					sqlQuery.addScalar("Qtd documentos", Hibernate.INTEGER);
					sqlQuery.addScalar("Data abertura", Hibernate.DATE);
					sqlQuery.addScalar("Data comunicado", Hibernate.DATE);
					sqlQuery.addScalar("Data atualização", Hibernate.DATE);
					sqlQuery.addScalar("Data comunicado RIM", Hibernate.DATE);
					sqlQuery.addScalar("Doctos sem RIM", Hibernate.DATE);
					sqlQuery.addScalar("Data emissão RIM sem pagto", Hibernate.DATE);
					sqlQuery.addScalar("Situação do processo", Hibernate.STRING);
				}
				if(parameters.getBoolean("checkRims")) {
					sqlQuery.addScalar("Regional RIM", Hibernate.STRING);
					sqlQuery.addScalar("Descrição regional", Hibernate.STRING);
					sqlQuery.addScalar("Filial RIM", Hibernate.STRING);
					sqlQuery.addScalar("Número RIM", Hibernate.INTEGER);
					sqlQuery.addScalar("Tipo de RIM", Hibernate.STRING);
					sqlQuery.addScalar("Status RIM", Hibernate.STRING);
					sqlQuery.addScalar("Situação da aprovação", Hibernate.STRING);
					sqlQuery.addScalar("Data RIM", Hibernate.DATE);
					sqlQuery.addScalar("Data liberação de pgto", Hibernate.DATE);
					sqlQuery.addScalar("Data programada pagamento", Hibernate.DATE);
					sqlQuery.addScalar("Data pagamento", Hibernate.DATE);
					sqlQuery.addScalar("CNPJ beneficiário", Hibernate.STRING);
					sqlQuery.addScalar("Razão social beneficiário", Hibernate.STRING);
					sqlQuery.addScalar("CNPJ favorecido", Hibernate.STRING);
					sqlQuery.addScalar("Razão social favorecido", Hibernate.STRING);
					sqlQuery.addScalar("Forma pagamento", Hibernate.STRING);
					sqlQuery.addScalar("Núm. banco", Hibernate.INTEGER);
					sqlQuery.addScalar("Núm. agência", Hibernate.INTEGER);
					sqlQuery.addScalar("Núm. dígito agência", Hibernate.STRING);
					sqlQuery.addScalar("Núm. conta", Hibernate.LONG);
					sqlQuery.addScalar("Núm. dígito conta", Hibernate.STRING);
					sqlQuery.addScalar("Data de vencimento", Hibernate.DATE);
					sqlQuery.addScalar("Valor indenização", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("% indenização", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Volumes indenizados", Hibernate.INTEGER);
					sqlQuery.addScalar("Natureza produto", Hibernate.STRING);
					sqlQuery.addScalar("Produto", Hibernate.STRING);
					sqlQuery.addScalar("Filiais debitadas", Hibernate.STRING);
					sqlQuery.addScalar("Filial RNC", Hibernate.STRING);
					sqlQuery.addScalar("Número RNC", Hibernate.INTEGER);
					sqlQuery.addScalar("Data emissão RNC", Hibernate.DATE);
					sqlQuery.addScalar("Status RNC", Hibernate.STRING);
					sqlQuery.addScalar("Motivo abertura", Hibernate.STRING);
					sqlQuery.addScalar("Núm. processo sinistro", Hibernate.STRING);
					sqlQuery.addScalar("Tipo seguro RIM", Hibernate.STRING);
					sqlQuery.addScalar("Tipo sinistro RIM", Hibernate.STRING);
					sqlQuery.addScalar("Nota fiscal débito cliente", Hibernate.STRING);
					sqlQuery.addScalar("Filial ocorr. 1", Hibernate.STRING);
					sqlQuery.addScalar("Filial ocorr. 2", Hibernate.STRING);
					sqlQuery.addScalar("Observações RIM", Hibernate.STRING);
					sqlQuery.addScalar("Segurado", Hibernate.STRING);
					sqlQuery.addScalar("Salvados", Hibernate.STRING);
					sqlQuery.addScalar("MDAs", Hibernate.STRING);
					sqlQuery.addScalar("Número do lote", Hibernate.INTEGER);
					sqlQuery.addScalar("Data do lote", Hibernate.DATE);
					sqlQuery.addScalar("Data cancelamento", Hibernate.DATE);
					sqlQuery.addScalar("Data liberação pagamento", Hibernate.DATE);
					sqlQuery.addScalar("Data envio JDE", Hibernate.DATE);
					sqlQuery.addScalar("Data retorno pagto banco", Hibernate.DATE);
					sqlQuery.addScalar("Data pagamento JDE", Hibernate.DATE);
					sqlQuery.addScalar("Notas fiscais", Hibernate.STRING);
				}
				
				if(parameters.getBoolean("checkCtes")) {
					sqlQuery.addScalar("Filial de origem CTRC", Hibernate.STRING);
					sqlQuery.addScalar("Número", Hibernate.INTEGER);
					sqlQuery.addScalar("Tipo documento", Hibernate.STRING);
					sqlQuery.addScalar("Filial de destino", Hibernate.STRING);
					sqlQuery.addScalar("Data de emissão", Hibernate.DATE);
					sqlQuery.addScalar("Tipo de frete", Hibernate.STRING);
					sqlQuery.addScalar("Tipo de conhecimento", Hibernate.STRING);
					sqlQuery.addScalar("Tipo de cálculo", Hibernate.STRING);
					sqlQuery.addScalar("Data prevista de entrega", Hibernate.DATE);
					sqlQuery.addScalar("Data de entrega", Hibernate.DATE);
					sqlQuery.addScalar("Número de dias entrega", Hibernate.INTEGER);
					sqlQuery.addScalar("Valor mercadoria", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor frete total", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor frete líquido", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor ICMS", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor ICMS ST", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Valor devido frete", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Peso aferido", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Peso real", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Qtd volumes", Hibernate.INTEGER);
					sqlQuery.addScalar("Tipo de prejuízo", Hibernate.STRING);
					sqlQuery.addScalar("Valor prejuízo", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("Prejuízo próprio", Hibernate.STRING);
					sqlQuery.addScalar("Situação cobrança frete", Hibernate.STRING);
					sqlQuery.addScalar("Data de liquidação frete", Hibernate.DATE);
					sqlQuery.addScalar("CNPJ remetente", Hibernate.STRING);
					sqlQuery.addScalar("Razão social remetente", Hibernate.STRING);
					sqlQuery.addScalar("CNPJ destinatário", Hibernate.STRING);
					sqlQuery.addScalar("Razão social destinatário", Hibernate.STRING);
					sqlQuery.addScalar("CNPJ devedor", Hibernate.STRING);
					sqlQuery.addScalar("Razão social devedor", Hibernate.STRING);
					sqlQuery.addScalar("CNPJ consignatário", Hibernate.STRING);
					sqlQuery.addScalar("Razão social consignatário", Hibernate.STRING);
					sqlQuery.addScalar("CNPJ redespacho", Hibernate.STRING);
					sqlQuery.addScalar("Razão social redespacho", Hibernate.STRING);
					sqlQuery.addScalar("Modal", Hibernate.STRING);
					sqlQuery.addScalar("Abrangência", Hibernate.STRING);
					sqlQuery.addScalar("Serviço", Hibernate.STRING);
					sqlQuery.addScalar("Data comunicado PS", Hibernate.DATE);
					sqlQuery.addScalar("Data atualização PS", Hibernate.DATE);
					sqlQuery.addScalar("Data comunicado RIM PS", Hibernate.DATE);
				}
				
				if(parameters.getBoolean("checkNfs")) {
					sqlQuery.addScalar("Nota fiscal número", Hibernate.INTEGER);
					sqlQuery.addScalar("Série", Hibernate.STRING);
					sqlQuery.addScalar("Data emissão", Hibernate.DATE);
					sqlQuery.addScalar("Chave NF-e", Hibernate.STRING);
				}
			}
		};
	}
}
