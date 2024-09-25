package com.mercurio.lms.contasreceber.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.FaturaLog;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaturaLogDAO extends BaseCrudDao<FaturaLog, Long> {
	
	public Integer getRowCount(Map criteria) {
		StringBuffer hql = new StringBuffer("select");
		hql.append(" count(*) as quantidade");
		hql.append(" from FaturaLog as fl");

		String nrFatura = (String) criteria.get("nrFatura");
		String idFilial = (String) criteria.remove("filialByIdFilial.idFilial");
		criteria.put("idFilial", idFilial);
		Map<String, Long> m = new HashMap<String, Long>();
		if (StringUtils.isNotBlank(nrFatura) || StringUtils.isNotBlank(idFilial)) {
			hql.append(" where");
			boolean and = false;
			if (StringUtils.isNotBlank(idFilial)) {
				m.put("idFilial", Long.valueOf(criteria.get("idFilial").toString()));
				hql.append(" fl.filial.idFilial = :idFilial");
				and = true;
			}
			if (StringUtils.isNotBlank(nrFatura)) {
				m.put("nrFatura", Long.valueOf(criteria.get("nrFatura").toString()));
				hql.append((and ? " and" : "") + " fl.nrFatura = :nrFatura");
			}
		}
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), m);
		return result.intValue();
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		StringBuffer hql = new StringBuffer("select");
		hql.append(" new Map(");
		hql.append(" fl.idFaturaLog as idFaturaLog");
		hql.append(", fatura.idFatura as idFatura");
		hql.append(", filial.idFilial as idFilial");
		hql.append(", cliente.idCliente as idCliente");
		hql.append(", filialCobradora.idFilial as idFilialCobradora");
		hql.append(", divisaoCliente.idDivisaoCliente as idDivisaoCliente");
		hql.append(", fl.nrFatura as nrFatura");
		hql.append(", fl.qtDocumentos as qtDocumentos");
		hql.append(", fl.vlBaseCalcPisCofinsCsll as vlBaseCalcPisCofinsCsll");
		hql.append(", fl.vlBaseCalcIr as vlBaseCalcIr");
		hql.append(", fl.vlPis as vlPis");
		hql.append(", fl.vlCofins as vlCofins");
		hql.append(", fl.vlCsll as vlCsll");
		hql.append(", fl.vlIr as vlIr");
		hql.append(", fl.vlIva as vlIva");
		hql.append(", fl.vlTotal as vlTotal");
		hql.append(", fl.vlDesconto as vlDesconto");
		hql.append(", fl.vlTotalRecebido as vlTotalRecebido");
		hql.append(", fl.vlJuroCalculado as vlJuroCalculado");
		hql.append(", fl.vlJuroRecebido as vlJuroRecebido");
		hql.append(", fl.vlCotacaoMoeda as vlCotacaoMoeda");
		hql.append(", fl.dtEmissao as dtEmissao");
		hql.append(", fl.dtVencimento as dtVencimento");
		hql.append(", fl.blGerarEdi as blGerarEdi");
		hql.append(", fl.blGerarBoleto as blGerarBoleto");
		hql.append(", fl.blFaturaReemitida as blFaturaReemitida");
		hql.append(", fl.blIndicadorImpressao as blIndicadorImpressao");
		hql.append(", fl.tpFatura as tpFatura");
		hql.append(", fl.tpSituacaoAprovacao as tpSituacaoAprovacao");
		hql.append(", fl.tpSituacaoFatura as tpSituacaoFatura");
		hql.append(", fl.tpOrigem as tpOrigem");
		hql.append(", fl.tpAbrangencia as tpAbrangencia");
		hql.append(", fl.tpModal as tpModal");
		hql.append(", relacaoCobranca.idRelacaoCobranca as idRelacaoCobranca");
		hql.append(", usuario.idUsuario as idUsuario");
		hql.append(", faturaOriginal.idFatura as idFaturaOriginal");
		hql.append(", cedente.idCedente as idCedente");
		hql.append(", manifestoEntrega.idManifestoEntrega as idManifestoEntrega");
		hql.append(", cotacaoMoeda.idCotacaoMoeda as idCotacaoMoeda");
		hql.append(", manifesto.idManifesto as idManifesto");
		hql.append(", tipoAgrupamento.idTipoAgrupamento as idTipoAgrupamento");
		hql.append(", agrupamentoCliente.idAgrupamentoCliente as idAgrupamentoCliente");
		hql.append(", pendencia.idPendencia as idPendencia");
		hql.append(", fl.dtLiquidacao as dtLiquidacao");
		hql.append(", fl.dtTransmissaoEdi as dtTransmissaoEdi");
		hql.append(", fl.dhReemissao as dhReemissao");
		hql.append(", fl.dhTransmissao as dhTransmissao");
		hql.append(", fl.nrPreFatura as nrPreFatura");
		hql.append(", fl.obFatura as obFatura");
		hql.append(", moeda.idMoeda as idMoeda");
		hql.append(", fl.tpFrete as tpFrete");
		hql.append(", servico.idServico as idServico");
		hql.append(", manifestoOrigem.idManifesto as idManifestoOrigem");
		hql.append(", manifestoEntregaOrigem.idManifestoEntrega as idManifestoEntregaOrigem");
		hql.append(", pendenciaDesconto.idPendencia as idPendenciaDesconto");
		hql.append(", boleto.idBoleto as idBoleto");
		hql.append(", recibo.idRecibo as idRecibo");
		hql.append(", redeco.idRedeco as idRedeco");
		hql.append(", notaDebitoNacional.idDoctoServico as idNotaDebitoNacional");
		hql.append(", fl.loginLog as loginLog");
		hql.append(", fl.dhLog as dhLog");
		hql.append(", fl.opLog as opLog");
		hql.append(")");
		hql.append(" from FaturaLog as fl");
		hql.append(" left join fl.fatura as fatura");
		hql.append(" left join fl.filial as filial");
		hql.append(" left join fl.cliente as cliente");
		hql.append(" left join fl.filialCobradora as filialCobradora");
		hql.append(" left join fl.divisaoCliente as divisaoCliente");
		hql.append(" left join fl.relacaoCobranca as relacaoCobranca");
		hql.append(" left join fl.usuario as usuario");
		hql.append(" left join fl.faturaOriginal as faturaOriginal");
		hql.append(" left join fl.cedente as cedente");
		hql.append(" left join fl.manifestoEntrega as manifestoEntrega");
		hql.append(" left join fl.cotacaoMoeda as cotacaoMoeda");
		hql.append(" left join fl.manifesto as manifesto");
		hql.append(" left join fl.tipoAgrupamento as tipoAgrupamento");
		hql.append(" left join fl.agrupamentoCliente as agrupamentoCliente");
		hql.append(" left join fl.pendencia as pendencia");
		hql.append(" left join fl.moeda as moeda");
		hql.append(" left join fl.servico as servico");
		hql.append(" left join fl.manifestoOrigem as manifestoOrigem");
		hql.append(" left join fl.manifestoEntregaOrigem as manifestoEntregaOrigem");
		hql.append(" left join fl.pendenciaDesconto as pendenciaDesconto");
		hql.append(" left join fl.boleto as boleto");
		hql.append(" left join fl.recibo as recibo");
		hql.append(" left join fl.redeco as redeco");
		hql.append(" left join fl.notaDebitoNacional as notaDebitoNacional");
		String nrFatura = (String) criteria.get("nrFatura");
		String idFilial = (String) criteria.remove("filialByIdFilial.idFilial");
		criteria.put("idFilial", idFilial);
		Map<String, Long> m = new HashMap<String, Long>();
		if (StringUtils.isNotBlank(nrFatura) || StringUtils.isNotBlank(idFilial)) {
			hql.append(" where");
			boolean and = false;
			if (StringUtils.isNotBlank(idFilial)) {
				m.put("idFilial", Long.valueOf(criteria.get("idFilial").toString()));
				hql.append(" filial.idFilial = :idFilial");
				and = true;
			}
			if (StringUtils.isNotBlank(nrFatura)) {
				m.put("nrFatura", Long.valueOf(criteria.get("nrFatura").toString()));
				hql.append((and ? " and" : "") + " fl.nrFatura = :nrFatura");
			}
		}
		hql.append(" order by filial.idFilial, fl.nrFatura, fl.dhLog.value");
		
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(),
				m);
		
		/*

select * from fatura where tp_situacao_fatura = 'EM' and id_filial = 370

alter session set time_zone = 'AMERICA/SAO_PAULO'
select sessiontimezone from dual;

select SYSTIMESTAMP AT time zone (select sessiontimezone from dual) FROM DUAL;

UPDATE LOG$_FATURA SET DH_LOG = DH_LOG AT time zone 'AMERICA/SAO_PAULO'

lms.contasreceber.calcularJurosDiarioAutomaticoAction.executeCalcularJurosDiario
0 0/5 * * * ?
		
		
		select faturalog0_.ID_FATURA_LOG as col_0_0_, fatura1_.ID_FATURA as col_1_0_, cliente3_.ID_CLIENTE as col_2_0_, filial4_.ID_FILIAL as col_3_0_, divisaocli5_.ID_DIVISAO_CLIENTE as col_4_0_, faturalog0_.NR_FATURA as col_5_0_, faturalog0_.QT_DOCUMENTOS as col_6_0_, faturalog0_.VL_BASE_CALC_PIS_COFINS_CSLL as col_7_0_, faturalog0_.VL_BASE_CALC_IR as col_8_0_, faturalog0_.VL_PIS as col_9_0_, faturalog0_.VL_COFINS as col_10_0_, faturalog0_.VL_CSLL as col_11_0_, faturalog0_.VL_IR as col_12_0_, faturalog0_.VL_IVA as col_13_0_, faturalog0_.VL_TOTAL as col_14_0_, faturalog0_.VL_DESCONTO as col_15_0_, faturalog0_.VL_TOTAL_RECEBIDO as col_16_0_, faturalog0_.VL_JURO_CALCULADO as col_17_0_, faturalog0_.VL_JURO_RECEBIDO as col_18_0_, faturalog0_.VL_COTACAO_MOEDA as col_19_0_, faturalog0_.DT_EMISSAO as col_20_0_, faturalog0_.DT_VENCIMENTO as col_21_0_, faturalog0_.BL_GERAR_EDI as col_22_0_, faturalog0_.BL_GERAR_BOLETO as col_23_0_, faturalog0_.BL_FATURA_REEMITIDA as col_24_0_, faturalog0_.BL_INDICADOR_IMPRESSAO as col_25_0_, faturalog0_.TP_FATURA as col_26_0_, faturalog0_.TP_SITUACAO_APROVACAO as col_27_0_, faturalog0_.TP_SITUACAO_FATURA as col_28_0_, faturalog0_.TP_ORIGEM as col_29_0_, faturalog0_.TP_ABRANGENCIA as col_30_0_, faturalog0_.TP_MODAL as col_31_0_, relacaocob6_.ID_RELACAO_COBRANCA as col_32_0_, usuario7_.ID_USUARIO as col_33_0_, fatura8_.ID_FATURA as col_34_0_, cedente9_.ID_CEDENTE as col_35_0_, manifestoe10_.ID_MANIFESTO_ENTREGA as col_36_0_, cotacaomoe11_.ID_COTACAO_MOEDA as col_37_0_, manifesto12_.ID_MANIFESTO as col_38_0_, tipoagrupa13_.ID_TIPO_AGRUPAMENTO as col_39_0_, agrupament14_.ID_AGRUPAMENTO_CLIENTE as col_40_0_, pendencia15_.ID_PENDENCIA as col_41_0_, faturalog0_.DT_LIQUIDACAO as col_42_0_, faturalog0_.DT_TRANSMISSAO_EDI as col_43_0_, faturalog0_.DH_REEMISSAO as col_44_0_, faturalog0_.DH_TRANSMISSAO as col_45_0_, faturalog0_.NR_PRE_FATURA as col_46_0_, faturalog0_.OB_FATURA as col_47_0_, moeda16_.ID_MOEDA as col_48_0_, faturalog0_.TP_FRETE as col_49_0_, servico17_.ID_SERVICO as col_50_0_, manifesto18_.ID_MANIFESTO as col_51_0_, manifestoe19_.ID_MANIFESTO_ENTREGA as col_52_0_, pendencia20_.ID_PENDENCIA as col_53_0_, boleto21_.ID_BOLETO as col_54_0_, recibo22_.ID_RECIBO as col_55_0_, redeco23_.ID_REDECO as col_56_0_, notadebito24_.ID_NOTA_DEBITO_NACIONAL as col_57_0_, faturalog0_.LOGIN_LOG as col_58_0_, faturalog0_.DH_LOG as col_59_0_, faturalog0_.OP_LOG as col_60_0_ from LOG$_FATURA faturalog0_, FATURA fatura1_, FILIAL filial2_, CLIENTE cliente3_, FILIAL filial4_, DIVISAO_CLIENTE divisaocli5_, RELACAO_COBRANCA relacaocob6_, USUARIO usuario7_, FATURA fatura8_, CEDENTE cedente9_, MANIFESTO_ENTREGA manifestoe10_, COTACAO_MOEDA cotacaomoe11_, MANIFESTO manifesto12_, TIPO_AGRUPAMENTO tipoagrupa13_, AGRUPAMENTO_CLIENTE agrupament14_, PENDENCIA pendencia15_, MOEDA moeda16_, SERVICO servico17_, MANIFESTO manifesto18_, MANIFESTO_ENTREGA manifestoe19_, PENDENCIA pendencia20_, BOLETO boleto21_, RECIBO recibo22_, REDECO redeco23_, NOTA_DEBITO_NACIONAL notadebito24_, DOCTO_SERVICO notadebito24_1_ where faturalog0_.ID_NOTA_DEBITO_NACIONAL=notadebito24_.ID_NOTA_DEBITO_NACIONAL(+) and notadebito24_.ID_NOTA_DEBITO_NACIONAL=notadebito24_1_.ID_DOCTO_SERVICO(+) and faturalog0_.ID_REDECO=redeco23_.ID_REDECO(+) and faturalog0_.ID_RECIBO=recibo22_.ID_RECIBO(+) and faturalog0_.ID_BOLETO=boleto21_.ID_BOLETO(+) and faturalog0_.ID_PENDENCIA_DESCONTO=pendencia20_.ID_PENDENCIA(+) and faturalog0_.ID_MANIFESTO_ENTREGA_ORIGEM=manifestoe19_.ID_MANIFESTO_ENTREGA(+) and faturalog0_.ID_MANIFESTO_ORIGEM=manifesto18_.ID_MANIFESTO(+) and faturalog0_.ID_SERVICO=servico17_.ID_SERVICO(+) and faturalog0_.ID_MOEDA=moeda16_.ID_MOEDA(+) and faturalog0_.ID_PENDENCIA=pendencia15_.ID_PENDENCIA(+) and faturalog0_.ID_AGRUPAMENTO_CLIENTE=agrupament14_.ID_AGRUPAMENTO_CLIENTE(+) and faturalog0_.ID_TIPO_AGRUPAMENTO=tipoagrupa13_.ID_TIPO_AGRUPAMENTO(+) and faturalog0_.ID_MANIFESTO=manifesto12_.ID_MANIFESTO(+) and faturalog0_.ID_COTACAO_MOEDA=cotacaomoe11_.ID_COTACAO_MOEDA(+) and faturalog0_.ID_MANIFESTO_ENTREGA=manifestoe10_.ID_MANIFESTO_ENTREGA(+) and faturalog0_.ID_CEDENTE=cedente9_.ID_CEDENTE(+) and faturalog0_.ID_FATURA_ORIGINAL=fatura8_.ID_FATURA(+) and faturalog0_.ID_USUARIO=usuario7_.ID_USUARIO(+) and faturalog0_.ID_RELACAO_COBRANCA=relacaocob6_.ID_RELACAO_COBRANCA(+) and faturalog0_.ID_DIVISAO_CLIENTE=divisaocli5_.ID_DIVISAO_CLIENTE(+) and faturalog0_.ID_FILIAL_COBRADORA=filial4_.ID_FILIAL(+) and faturalog0_.ID_CLIENTE=cliente3_.ID_CLIENTE(+) and faturalog0_.ID_FILIAL=filial2_.ID_FILIAL(+) and faturalog0_.ID_FATURA=fatura1_.ID_FATURA(+) order by fatura1_.ID_FATURA, faturalog0_.DH_LOG


		desc LOG$_ITEM_FATURA

		select * from usuario



		Select owner, TRIGGER_NAME from ALL_triggers   where TABLE_NAME = 'DESCONTO';

		select line, text from all_source where NAME = 'T_LOG$_FATURA' and owner = 'LMS_INTEGRADO01' order by line

-- as ID1_0_, filial1_.ID_FILIAL as ID1_1_, cliente2_.ID_CLIENTE as ID1_2_, fatura0_.NR_VERSAO as NR2_161_0_, fatura0_.NR_FATURA as NR3_161_0_, fatura0_.QT_DOCUMENTOS as QT4_161_0_, fatura0_.VL_BASE_CALC_PIS_COFINS_CSLL as VL5_161_0_, fatura0_.VL_BASE_CALC_IR as VL6_161_0_, fatura0_.VL_PIS as VL7_161_0_, fatura0_.VL_COFINS as VL8_161_0_, fatura0_.VL_CSLL as VL9_161_0_, fatura0_.VL_IR as VL10_161_0_, fatura0_.VL_IVA as VL11_161_0_, fatura0_.VL_TOTAL as VL12_161_0_, fatura0_.VL_DESCONTO as VL13_161_0_, fatura0_.VL_TOTAL_RECEBIDO as VL14_161_0_, fatura0_.VL_JURO_CALCULADO as VL15_161_0_, fatura0_.VL_JURO_RECEBIDO as VL16_161_0_, fatura0_.VL_COTACAO_MOEDA as VL17_161_0_, fatura0_.DT_EMISSAO as DT18_161_0_, fatura0_.DT_VENCIMENTO as DT19_161_0_, fatura0_.DT_TRANSMISSAO_EDI as DT20_161_0_, fatura0_.BL_GERAR_EDI as BL21_161_0_, fatura0_.BL_GERAR_BOLETO as BL22_161_0_, fatura0_.BL_FATURA_REEMITIDA as BL23_161_0_, fatura0_.BL_INDICADOR_IMPRESSAO as BL24_161_0_, fatura0_.TP_FATURA as TP25_161_0_, fatura0_.TP_SITUACAO_APROVACAO as TP26_161_0_, fatura0_.TP_SITUACAO_FATURA as TP27_161_0_, fatura0_.TP_ORIGEM as TP28_161_0_, fatura0_.TP_ABRANGENCIA as TP29_161_0_, fatura0_.TP_MODAL as TP30_161_0_, fatura0_.TP_FRETE as TP31_161_0_, fatura0_.DT_LIQUIDACAO as DT32_161_0_, fatura0_.DH_TRANSMISSAO as DH33_161_0_, fatura0_.DH_REEMISSAO as DH34_161_0_, fatura0_.NR_PRE_FATURA as NR35_161_0_, fatura0_.OB_FATURA as OB36_161_0_, fatura0_.ID_COTACAO_MOEDA as ID37_161_0_, fatura0_.ID_CLIENTE as ID38_161_0_, fatura0_.ID_MOEDA as ID39_161_0_, fatura0_.ID_SERVICO as ID40_161_0_, fatura0_.ID_FATURA_ORIGINAL as ID41_161_0_, fatura0_.ID_USUARIO as ID42_161_0_, fatura0_.ID_RELACAO_COBRANCA as ID43_161_0_, fatura0_.ID_TIPO_AGRUPAMENTO as ID44_161_0_, fatura0_.ID_AGRUPAMENTO_CLIENTE as ID45_161_0_, fatura0_.ID_MANIFESTO as ID46_161_0_, fatura0_.ID_MANIFESTO_ENTREGA as ID47_161_0_, fatura0_.ID_DIVISAO_CLIENTE as ID48_161_0_, fatura0_.ID_FILIAL as ID49_161_0_, fatura0_.ID_FILIAL_COBRADORA as ID50_161_0_, fatura0_.ID_CEDENTE as ID51_161_0_, fatura0_.ID_PENDENCIA as ID52_161_0_, fatura0_.ID_PENDENCIA_DESCONTO as ID53_161_0_, fatura0_.ID_MANIFESTO_ORIGEM as ID54_161_0_, fatura0_.ID_MANIFESTO_ENTREGA_ORIGEM as ID55_161_0_, fatura0_.ID_BOLETO as ID56_161_0_, fatura0_.ID_RECIBO as ID57_161_0_, fatura0_.ID_REDECO as ID58_161_0_, fatura0_.ID_NOTA_DEBITO_NACIONAL as ID59_161_0_, filial1_.SG_FILIAL as SG2_342_1_, filial1_.DS_TIMEZONE as DS3_342_1_, filial1_.BL_EMITE_BOLETO_ENTREGA as BL4_342_1_, filial1_.BL_EMITE_BOLETO_FATURAMENTO as BL5_342_1_, filial1_.BL_EMITE_RECIBO_FRETE as BL6_342_1_, filial1_.BL_RECEBE_VEICULOS_SEM_COLETA as BL7_342_1_, filial1_.BL_INFORMA_KM_PORTARIA as BL8_342_1_, filial1_.BL_ORDENA_ENTREGA_VALOR as BL9_342_1_, filial1_.BL_OBRIGA_BAIXA_ENTREGA_ORDEM as BL10_342_1_, filial1_.NR_PRAZO_COBRANCA as NR11_342_1_, filial1_.VL_CUSTO_REEMBARQUE as VL12_342_1_, filial1_.NR_CENTRO_CUSTO as NR13_342_1_, filial1_.PC_JURO_DIARIO as PC14_342_1_, filial1_.PC_FRETE_CARRETEIRO as PC15_342_1_, filial1_.NR_FRANQUIA_PESO as NR16_342_1_, filial1_.NR_FRANQUIA_KM as NR17_342_1_, filial1_.NR_AREA_TOTAL as NR18_342_1_, filial1_.NR_AREA_ARMAZENAGEM as NR19_342_1_, filial1_.NR_INSCRICAO_MUNICIPAL as NR20_342_1_, filial1_.DS_HOMEPAGE as DS21_342_1_, filial1_.OB_FILIAL as OB22_342_1_, filial1_.OB_APROVACAO as OB23_342_1_, filial1_.DT_IMPLANTACAO_LMS as DT24_342_1_, filial1_.BL_LIBERA_FOB_AEREO as BL25_342_1_, filial1_.ID_MOEDA as ID26_342_1_, filial1_.ID_EMPRESA as ID27_342_1_, filial1_.ID_FRANQUEADO as ID28_342_1_, filial1_.ID_PENDENCIA as ID29_342_1_, filial1_.ID_AEROPORTO as ID30_342_1_, filial1_.ID_FILIAL_RESPONSAVEL_AWB as ID31_342_1_, filial1_.ID_FILIAL_RESPONSAVEL as ID32_342_1_, filial1_.ID_CEDENTE as ID33_342_1_, filial1_.ID_CEDENTE_BLOQUETO as ID34_342_1_, cliente2_.TP_SITUACAO as TP2_574_2_, cliente2_.TP_CLIENTE as TP3_574_2_, cliente2_.DT_GERACAO as DT4_574_2_, cliente2_.BL_MATRIZ as BL5_574_2_, cliente2_.NR_CONTA as NR6_574_2_, cliente2_.DT_ULTIMO_MOVIMENTO as DT7_574_2_, cliente2_.DS_SITE as DS8_574_2_, cliente2_.OB_CLIENTE as OB9_574_2_, cliente2_.TP_ATIVIDADE_ECONOMICA as TP10_574_2_, cliente2_.DT_FUNDACAO_EMPRESA as DT11_574_2_, cliente2_.BL_PERMANENTE as BL12_574_2_, cliente2_.BL_COBRA_REENTREGA as BL13_574_2_, cliente2_.BL_COBRA_DEVOLUCAO as BL14_574_2_, cliente2_.BL_FOB_DIRIGIDO as BL15_574_2_, cliente2_.BL_PESO_AFORADO_PEDAGIO as BL16_574_2_, cliente2_.BL_ICMS_PEDAGIO as BL17_574_2_, cliente2_.VL_FATURAMENTO_PREVISTO as VL18_574_2_, cliente2_.TP_FORMA_ARREDONDAMENTO as TP19_574_2_, cliente2_.TP_FREQUENCIA_VISITA as TP20_574_2_, cliente2_.BL_OPERADOR_LOGISTICO as BL21_574_2_, cliente2_.NR_CASAS_DECIMAIS_PESO as NR22_574_2_, cliente2_.BL_DIFICULDADE_ENTREGA as BL23_574_2_, cliente2_.BL_RETENCAO_COMPROVANTE_ENT as BL24_574_2_, cliente2_.BL_DIVULGA_LOCALIZACAO as BL25_574_2_, cliente2_.ID_FILIAL_ATENDE_COMERCIAL as ID26_574_2_, cliente2_.ID_REGIONAL_COMERCIAL as ID27_574_2_, cliente2_.ID_MOEDA_FAT_PREV as ID28_574_2_, cliente2_.BL_COLETA_AUTOMATICA as BL29_574_2_, cliente2_.TP_DIFICULDADE_COLETA as TP30_574_2_, cliente2_.TP_DIFICULDADE_ENTREGA as TP31_574_2_, cliente2_.TP_DIFICULDADE_CLASSIFICACAO as TP32_574_2_, cliente2_.TP_LOCAL_EMISSAO_CON_REENT as TP33_574_2_, cliente2_.BL_AGRUPA_NOTAS as BL34_574_2_, cliente2_.BL_CADASTRADO_COLETA as BL35_574_2_, cliente2_.BL_AGENDAMENTO_PESSOA_FISICA as BL36_574_2_, cliente2_.BL_AGENDAMENTO_PESSOA_JURIDICA as BL37_574_2_, cliente2_.BL_OBRIGA_RECEBEDOR as BL38_574_2_, cliente2_.ID_FILIAL_ATENDE_OPERACIONAL as ID39_574_2_, cliente2_.ID_REGIONAL_OPERACIONAL as ID40_574_2_, cliente2_.BL_GERA_RECIBO_FRETE_ENTREGA as BL41_574_2_, cliente2_.BL_RESPONSAVEL_FRETE as BL42_574_2_, cliente2_.BL_BASE_CALCULO as BL43_574_2_, cliente2_.BL_INDICADOR_PROTESTO as BL44_574_2_, cliente2_.PC_DESCONTO_FRETE_CIF as PC45_574_2_, cliente2_.PC_DESCONTO_FRETE_FOB as PC46_574_2_, cliente2_.BL_EMITE_BOLETO_CLI_DESTINO as BL47_574_2_, cliente2_.BL_AGRUPA_FATURAMENTO_MES as BL48_574_2_, cliente2_.TP_PERIODICIDADE_TRANSF as TP49_574_2_, cliente2_.BL_RESSARCE_FRETE_FOB as BL50_574_2_, cliente2_.TP_MEIO_ENVIO_BOLETO as TP51_574_2_, cliente2_.TP_COBRANCA as TP52_574_2_, cliente2_.NR_DIAS_LIMITE_DEBITO as NR53_574_2_, cliente2_.VL_SALDO_ATUAL as VL54_574_2_, cliente2_.VL_LIMITE_CREDITO as VL55_574_2_, cliente2_.BL_PRE_FATURA as BL56_574_2_, cliente2_.PC_JURO_DIARIO as PC57_574_2_, cliente2_.BL_FATURA_DOCS_ENTREGUES as BL58_574_2_, cliente2_.BL_COBRANCA_CENTRALIZADA as BL59_574_2_, cliente2_.BL_FATURA_DOCS_CONFERIDOS as BL60_574_2_, cliente2_.BL_FRONTEIRA_RAPIDA as BL61_574_2_, cliente2_.VL_LIMITE_DOCUMENTOS as VL62_574_2_, cliente2_.BL_FATURA_DOC_REFERENCIA as BL63_574_2_, cliente2_.ID_BANCO as ID64_574_2_, cliente2_.ID_MOEDA_LIM_CRED as ID65_574_2_, cliente2_.ID_MOEDA_LIM_DOCTOS as ID66_574_2_, cliente2_.ID_MOEDA_SALDO_ATUAL as ID67_574_2_, cliente2_.ID_FILIAL_COBRANCA as ID68_574_2_, cliente2_.ID_CLIENTE_MATRIZ as ID69_574_2_, cliente2_.ID_CLIENTE_RESPONSAVEL_FRETE as ID70_574_2_, cliente2_.ID_REGIONAL_FINANCEIRO as ID71_574_2_, cliente2_.ID_OBSERVACAO_CONHECIMENTO as ID72_574_2_, cliente2_.ID_GRUPO_ECONOMICO as ID73_574_2_, cliente2_.ID_USUARIO_INCLUSAO as ID74_574_2_, cliente2_.ID_USUARIO_ALTERACAO as ID75_574_2_, cliente2_.ID_RAMO_ATIVIDADE as ID76_574_2_, cliente2_.ID_SEGMENTO_MERCADO as ID77_574_2_, cliente2_.ID_CEDENTE as ID78_574_2_ 

update fatura 
set vl_juro_calculado = 0 
where id_fatura in (
select fatura0_.ID_FATURA 
from FATURA fatura0_, FILIAL filial1_, CLIENTE cliente2_, ITEM_REDECO itemredeco3_, REDECO redeco4_, BOLETO boleto5_ 
where fatura0_.ID_BOLETO=boleto5_.ID_BOLETO(+) and itemredeco3_.ID_REDECO=redeco4_.ID_REDECO(+) and fatura0_.ID_FATURA=itemredeco3_.ID_FATURA(+) and fatura0_.ID_CLIENTE=cliente2_.ID_CLIENTE and fatura0_.ID_FILIAL=filial1_.ID_FILIAL and (filial1_.DT_IMPLANTACAO_LMS is not null) and filial1_.DT_IMPLANTACAO_LMS<=sysdate and fatura0_.DT_VENCIMENTO<(sysdate-1) and (fatura0_.TP_SITUACAO_FATURA in ('BL' , 'RE')) and (redeco4_.TP_FINALIDADE='CF' and redeco4_.TP_SITUACAO_REDECO<>'CA' or redeco4_.TP_FINALIDADE is null) and  not (exists (select 1 from OCORRENCIA_RETORNO_BANCO ocorrencia6_ where ocorrencia6_.ID_BOLETO=boleto5_.ID_BOLETO)) 
)

commit

delete from LOG$_FATURA

CREATE OR REPLACE TRIGGER T_LOG$_FATURA
AFTER INSERT OR UPDATE OR DELETE ON FATURA
FOR EACH ROW
DECLARE
  DUMMY_NR NUMBER(10);
  TZ_REGION VARCHAR(60);
  BATCH_RUNNING VARCHAR2(1);
  INTEGRACAO_RUNNING VARCHAR2(1);
  DH_LOG TIMESTAMP;
  LOGIN_LOG VARCHAR2(60);
  OP_LOG VARCHAR2(1);
  CURSOR c1 IS SELECT ID_PARAMETRO_LOG FROM PARAMETRO_LOG WHERE NM_TRIGGER_LOG = 'T_LOG$_FATURA' AND NM_PARAMETRO_LOG = 'FATURA_JAH_LOGADA' AND DS_PARAMETRO_LOG = 'S';
  NEXT_ID_FATURA_LOG NUMBER(10);
--  SESSION_PARAM_NOT_FOUND EXCEPTION;
--  PRAGMA EXCEPTION_INIT(SESSION_PARAM_NOT_FOUND, -20050);
BEGIN
  select F_INT_APPLICATION_INFO('Processo_Batch') INTO BATCH_RUNNING from dual;
  select F_INT_APPLICATION_INFO('Processo_Integracao') INTO INTEGRACAO_RUNNING from dual;

  IF (INTEGRACAO_RUNNING = '1' OR BATCH_RUNNING = '1') THEN
     RETURN;
  END IF;

  select sessiontimezone INTO TZ_REGION from dual;
  IF (SUBSTR(TZ_REGION, 1, 1) = '-' OR SUBSTR(TZ_REGION, 1, 1) = '+') THEN
     TZ_REGION := 'AMERICA/SAO_PAULO';
  END IF;
  select SYSTIMESTAMP AT time zone (select TZ_REGION from dual) into DH_LOG FROM DUAL;
  
--  select SYSTIMESTAMP AT time zone (select (select F_INT_APPLICATION_INFO('Timezone_Offset') from dual) || ':00' from dual) into DH_LOG FROM DUAL;
  select login into LOGIN_LOG from usuario where id_usuario = (select F_INT_APPLICATION_INFO('Id_Usuario_Logado') from dual);
  IF UPDATING THEN
    OP_LOG := 'U';
    
    OPEN c1;
    FETCH c1 INTO DUMMY_NR;
    IF (c1%ROWCOUNT > 0) THEN
      UPDATE LOG$_FATURA
      SET
      ID_FILIAL = :new.ID_FILIAL, 
      ID_CLIENTE = :new.ID_CLIENTE,
      ID_FILIAL_COBRADORA = :new.ID_FILIAL_COBRADORA,
      ID_DIVISAO_CLIENTE = :new.ID_DIVISAO_CLIENTE,
      NR_FATURA = :new.NR_FATURA,
      NR_VERSAO = :new.NR_VERSAO,
      QT_DOCUMENTOS = :new.QT_DOCUMENTOS,
      VL_BASE_CALC_PIS_COFINS_CSLL = :new.VL_BASE_CALC_PIS_COFINS_CSLL,
      VL_BASE_CALC_IR = :new.VL_BASE_CALC_IR,
      VL_PIS = :new.VL_PIS,
      VL_COFINS = :new.VL_COFINS,
      VL_CSLL = :new.VL_CSLL,
      VL_IR = :new.VL_IR,
      VL_IVA = :new.VL_IVA,
      VL_TOTAL = :new.VL_TOTAL,
      VL_DESCONTO = :new.VL_DESCONTO,
      VL_TOTAL_RECEBIDO = :new.VL_TOTAL_RECEBIDO,
      VL_JURO_CALCULADO = :new.VL_JURO_CALCULADO,
      VL_JURO_RECEBIDO = :new.VL_JURO_RECEBIDO,
      VL_COTACAO_MOEDA = :new.VL_COTACAO_MOEDA,
      DT_EMISSAO = :new.DT_EMISSAO,
      DT_VENCIMENTO = :new.DT_VENCIMENTO,
      BL_GERAR_EDI = :new.BL_GERAR_EDI,
      BL_GERAR_BOLETO = :new.BL_GERAR_BOLETO,
      BL_FATURA_REEMITIDA = :new.BL_FATURA_REEMITIDA,
      BL_INDICADOR_IMPRESSAO = :new.BL_INDICADOR_IMPRESSAO,
      TP_FATURA = :new.TP_FATURA,
      TP_SITUACAO_APROVACAO = :new.TP_SITUACAO_APROVACAO,
      TP_SITUACAO_FATURA = :new.TP_SITUACAO_FATURA,
      TP_ORIGEM = :new.TP_ORIGEM,
      TP_ABRANGENCIA = :new.TP_ABRANGENCIA,
      TP_MODAL = :new.TP_MODAL,
      ID_RELACAO_COBRANCA = :new.ID_RELACAO_COBRANCA,
      ID_USUARIO = :new.ID_USUARIO,
      ID_FATURA_ORIGINAL = :new.ID_FATURA_ORIGINAL,
      ID_CEDENTE = :new.ID_CEDENTE,
      ID_MANIFESTO_ENTREGA = :new.ID_MANIFESTO_ENTREGA,
      ID_COTACAO_MOEDA = :new.ID_COTACAO_MOEDA,
      ID_MANIFESTO = :new.ID_MANIFESTO,
      ID_TIPO_AGRUPAMENTO = :new.ID_TIPO_AGRUPAMENTO,
      ID_AGRUPAMENTO_CLIENTE = :new.ID_AGRUPAMENTO_CLIENTE,
      ID_PENDENCIA = :new.ID_PENDENCIA,
      DT_LIQUIDACAO = :new.DT_LIQUIDACAO,
      DT_TRANSMISSAO_EDI = :new.DT_TRANSMISSAO_EDI,
      DH_REEMISSAO = :new.DH_REEMISSAO,
      DH_TRANSMISSAO = :new.DH_TRANSMISSAO,
      NR_PRE_FATURA = :new.NR_PRE_FATURA,
      OB_FATURA = :new.OB_FATURA,
      ID_MOEDA = :new.ID_MOEDA,
      TP_FRETE = :new.TP_FRETE,
      ID_SERVICO = :new.ID_SERVICO,
      ID_MANIFESTO_ORIGEM = :new.ID_MANIFESTO_ORIGEM,
      ID_MANIFESTO_ENTREGA_ORIGEM = :new.ID_MANIFESTO_ENTREGA_ORIGEM,
      ID_PENDENCIA_DESCONTO = :new.ID_PENDENCIA_DESCONTO,
      ID_BOLETO = :new.ID_BOLETO,
      ID_RECIBO = :new.ID_RECIBO,
      ID_REDECO = :new.ID_REDECO,
      ID_NOTA_DEBITO_NACIONAL = :new.ID_NOTA_DEBITO_NACIONAL,
      LOGIN_LOG = LOGIN_LOG,
      DH_LOG = DH_LOG,
      OP_LOG = OP_LOG 
      WHERE 
      ID_FATURA_LOG = ( SELECT MAX(ID_FATURA_LOG) FROM LOG$_FATURA WHERE ID_FATURA = :new.ID_FATURA);
      IF (SQL%ROWCOUNT = 1) THEN
         RETURN;
      END IF;
    END IF;
    CLOSE c1;
  ELSE 
     IF INSERTING THEN
        OP_LOG := 'I'; -- I
     ELSE
        OP_LOG := 'D';
     END IF;
  END IF;
  
  OPEN c1;
  FETCH c1 INTO DUMMY_NR;
  IF (c1%NOTFOUND) THEN
  	INSERT INTO PARAMETRO_LOG VALUES ( PARAMETRO_LOG_SQ.NEXTVAL, 'T_LOG$_FATURA', 'FATURA_JAH_LOGADA', 'S' );
  END IF;
  CLOSE c1;
  
  SELECT LOG$_FATURA_SQ.nextval INTO NEXT_ID_FATURA_LOG FROM DUAL;
  IF (INSERTING OR UPDATING) THEN
    INSERT INTO LOG$_FATURA (ID_FATURA_LOG,ID_FATURA,ID_FILIAL, ID_CLIENTE,ID_FILIAL_COBRADORA,ID_DIVISAO_CLIENTE,NR_FATURA,NR_VERSAO,QT_DOCUMENTOS,VL_BASE_CALC_PIS_COFINS_CSLL,VL_BASE_CALC_IR,VL_PIS,VL_COFINS,VL_CSLL,VL_IR,VL_IVA,VL_TOTAL,VL_DESCONTO,VL_TOTAL_RECEBIDO,VL_JURO_CALCULADO,VL_JURO_RECEBIDO,VL_COTACAO_MOEDA,DT_EMISSAO,DT_VENCIMENTO,BL_GERAR_EDI,BL_GERAR_BOLETO,BL_FATURA_REEMITIDA,BL_INDICADOR_IMPRESSAO,TP_FATURA,TP_SITUACAO_APROVACAO,TP_SITUACAO_FATURA,TP_ORIGEM,TP_ABRANGENCIA,TP_MODAL,ID_RELACAO_COBRANCA,ID_USUARIO,ID_FATURA_ORIGINAL,ID_CEDENTE,ID_MANIFESTO_ENTREGA,ID_COTACAO_MOEDA,ID_MANIFESTO,ID_TIPO_AGRUPAMENTO,ID_AGRUPAMENTO_CLIENTE,ID_PENDENCIA,DT_LIQUIDACAO,DT_TRANSMISSAO_EDI,DH_REEMISSAO,DH_TRANSMISSAO,NR_PRE_FATURA,OB_FATURA,ID_MOEDA,TP_FRETE,ID_SERVICO,ID_MANIFESTO_ORIGEM,ID_MANIFESTO_ENTREGA_ORIGEM,ID_PENDENCIA_DESCONTO,ID_BOLETO,ID_RECIBO,ID_REDECO,ID_NOTA_DEBITO_NACIONAL,LOGIN_LOG,DH_LOG,OP_LOG ) 
    VALUES (NEXT_ID_FATURA_LOG, :new.ID_FATURA,:new.ID_FILIAL, :new.ID_CLIENTE,:new.ID_FILIAL_COBRADORA,:new.ID_DIVISAO_CLIENTE,:new.NR_FATURA,:new.NR_VERSAO,:new.QT_DOCUMENTOS,:new.VL_BASE_CALC_PIS_COFINS_CSLL,:new.VL_BASE_CALC_IR,:new.VL_PIS,:new.VL_COFINS,:new.VL_CSLL,:new.VL_IR,:new.VL_IVA,:new.VL_TOTAL,:new.VL_DESCONTO,:new.VL_TOTAL_RECEBIDO,:new.VL_JURO_CALCULADO,:new.VL_JURO_RECEBIDO,:new.VL_COTACAO_MOEDA,:new.DT_EMISSAO,:new.DT_VENCIMENTO,:new.BL_GERAR_EDI,:new.BL_GERAR_BOLETO,:new.BL_FATURA_REEMITIDA,:new.BL_INDICADOR_IMPRESSAO,:new.TP_FATURA,:new.TP_SITUACAO_APROVACAO,:new.TP_SITUACAO_FATURA,:new.TP_ORIGEM,:new.TP_ABRANGENCIA,:new.TP_MODAL,:new.ID_RELACAO_COBRANCA,:new.ID_USUARIO,:new.ID_FATURA_ORIGINAL,:new.ID_CEDENTE,:new.ID_MANIFESTO_ENTREGA,:new.ID_COTACAO_MOEDA,:new.ID_MANIFESTO,:new.ID_TIPO_AGRUPAMENTO,:new.ID_AGRUPAMENTO_CLIENTE,:new.ID_PENDENCIA,:new.DT_LIQUIDACAO,:new.DT_TRANSMISSAO_EDI,:new.DH_REEMISSAO,:new.DH_TRANSMISSAO,:new.NR_PRE_FATURA,:new.OB_FATURA,:new.ID_MOEDA,:new.TP_FRETE,:new.ID_SERVICO,:new.ID_MANIFESTO_ORIGEM,:new.ID_MANIFESTO_ENTREGA_ORIGEM,:new.ID_PENDENCIA_DESCONTO,:new.ID_BOLETO,:new.ID_RECIBO,:new.ID_REDECO,:new.ID_NOTA_DEBITO_NACIONAL,LOGIN_LOG,DH_LOG,OP_LOG);
  ELSE
    INSERT INTO LOG$_FATURA (ID_FATURA_LOG,ID_FATURA,ID_FILIAL, ID_CLIENTE,ID_FILIAL_COBRADORA,ID_DIVISAO_CLIENTE,NR_FATURA,NR_VERSAO,QT_DOCUMENTOS,VL_BASE_CALC_PIS_COFINS_CSLL,VL_BASE_CALC_IR,VL_PIS,VL_COFINS,VL_CSLL,VL_IR,VL_IVA,VL_TOTAL,VL_DESCONTO,VL_TOTAL_RECEBIDO,VL_JURO_CALCULADO,VL_JURO_RECEBIDO,VL_COTACAO_MOEDA,DT_EMISSAO,DT_VENCIMENTO,BL_GERAR_EDI,BL_GERAR_BOLETO,BL_FATURA_REEMITIDA,BL_INDICADOR_IMPRESSAO,TP_FATURA,TP_SITUACAO_APROVACAO,TP_SITUACAO_FATURA,TP_ORIGEM,TP_ABRANGENCIA,TP_MODAL,ID_RELACAO_COBRANCA,ID_USUARIO,ID_FATURA_ORIGINAL,ID_CEDENTE,ID_MANIFESTO_ENTREGA,ID_COTACAO_MOEDA,ID_MANIFESTO,ID_TIPO_AGRUPAMENTO,ID_AGRUPAMENTO_CLIENTE,ID_PENDENCIA,DT_LIQUIDACAO,DT_TRANSMISSAO_EDI,DH_REEMISSAO,DH_TRANSMISSAO,NR_PRE_FATURA,OB_FATURA,ID_MOEDA,TP_FRETE,ID_SERVICO,ID_MANIFESTO_ORIGEM,ID_MANIFESTO_ENTREGA_ORIGEM,ID_PENDENCIA_DESCONTO,ID_BOLETO,ID_RECIBO,ID_REDECO,ID_NOTA_DEBITO_NACIONAL,LOGIN_LOG,DH_LOG,OP_LOG ) 
    VALUES (NEXT_ID_FATURA_LOG, :old.ID_FATURA,:old.ID_FILIAL, :old.ID_CLIENTE,:old.ID_FILIAL_COBRADORA,:old.ID_DIVISAO_CLIENTE,:old.NR_FATURA,:old.NR_VERSAO,:old.QT_DOCUMENTOS,:old.VL_BASE_CALC_PIS_COFINS_CSLL,:old.VL_BASE_CALC_IR,:old.VL_PIS,:old.VL_COFINS,:old.VL_CSLL,:old.VL_IR,:old.VL_IVA,:old.VL_TOTAL,:old.VL_DESCONTO,:old.VL_TOTAL_RECEBIDO,:old.VL_JURO_CALCULADO,:old.VL_JURO_RECEBIDO,:old.VL_COTACAO_MOEDA,:old.DT_EMISSAO,:old.DT_VENCIMENTO,:old.BL_GERAR_EDI,:old.BL_GERAR_BOLETO,:old.BL_FATURA_REEMITIDA,:old.BL_INDICADOR_IMPRESSAO,:old.TP_FATURA,:old.TP_SITUACAO_APROVACAO,:old.TP_SITUACAO_FATURA,:old.TP_ORIGEM,:old.TP_ABRANGENCIA,:old.TP_MODAL,:old.ID_RELACAO_COBRANCA,:old.ID_USUARIO,:old.ID_FATURA_ORIGINAL,:old.ID_CEDENTE,:old.ID_MANIFESTO_ENTREGA,:old.ID_COTACAO_MOEDA,:old.ID_MANIFESTO,:old.ID_TIPO_AGRUPAMENTO,:old.ID_AGRUPAMENTO_CLIENTE,:old.ID_PENDENCIA,:old.DT_LIQUIDACAO,:old.DT_TRANSMISSAO_EDI,:old.DH_REEMISSAO,:old.DH_TRANSMISSAO,:old.NR_PRE_FATURA,:old.OB_FATURA,:old.ID_MOEDA,:old.TP_FRETE,:old.ID_SERVICO,:old.ID_MANIFESTO_ORIGEM,:old.ID_MANIFESTO_ENTREGA_ORIGEM,:old.ID_PENDENCIA_DESCONTO,:old.ID_BOLETO,:old.ID_RECIBO,:old.ID_REDECO,:old.ID_NOTA_DEBITO_NACIONAL,LOGIN_LOG,DH_LOG,OP_LOG);
  END IF;
  
  DELETE FROM PARAMETRO_LOG WHERE NM_TRIGGER_LOG = 'T_LOG$_FATURA' AND NM_PARAMETRO_LOG = 'LAST_ID_FATURA_LOG';
  INSERT INTO PARAMETRO_LOG VALUES ( PARAMETRO_LOG_SQ.NEXTVAL, 'T_LOG$_FATURA', 'LAST_ID_FATURA_LOG', TO_CHAR(NEXT_ID_FATURA_LOG) );
   
--  exception
--    when SESSION_PARAM_NOT_FOUND then
--      null;
END;

CREATE OR REPLACE TRIGGER T_LOG$_ITEM_FATURA
AFTER INSERT OR UPDATE OR DELETE ON ITEM_FATURA
FOR EACH ROW
DECLARE
   TZ_REGION VARCHAR(60);
   BATCH_RUNNING VARCHAR2(1);
   INTEGRACAO_RUNNING VARCHAR2(1);
   DH_LOG TIMESTAMP;
   LOGIN_LOG VARCHAR2(60);
   OP_LOG VARCHAR2(1);
   ID_ITEM_FATURA_LOG_NEXT NUMBER(11);
BEGIN
   select F_INT_APPLICATION_INFO('Processo_Batch') INTO BATCH_RUNNING from dual;
   select F_INT_APPLICATION_INFO('Processo_Integracao') INTO INTEGRACAO_RUNNING from dual;

   IF (INTEGRACAO_RUNNING = '1' OR BATCH_RUNNING = '1') THEN
      RETURN;
   END IF;
   
   select sessiontimezone INTO TZ_REGION from dual;
   IF (SUBSTR(TZ_REGION, 1, 1) = '-' OR SUBSTR(TZ_REGION, 1, 1) = '+') THEN
      TZ_REGION := 'AMERICA/SAO_PAULO';
   END IF;
   select SYSTIMESTAMP AT time zone (select TZ_REGION from dual) into DH_LOG FROM DUAL;

--   select SYSTIMESTAMP AT time zone (select (select F_INT_APPLICATION_INFO('Timezone_Offset') from dual) || ':00' from dual) into DH_LOG FROM DUAL;
   select login into LOGIN_LOG from usuario where id_usuario = (select F_INT_APPLICATION_INFO('Id_Usuario_Logado') from dual);
   IF INSERTING THEN
     OP_LOG := 'I';
   ELSE 
     IF UPDATING THEN
       OP_LOG := 'U';
     ELSE
       OP_LOG := 'D';
     END IF;
   END IF;
   SELECT LOG$_ITEM_FATURA_SQ.nextval INTO ID_ITEM_FATURA_LOG_NEXT FROM DUAL;

   IF (INSERTING OR UPDATING) THEN
      INSERT INTO LOG$_ITEM_FATURA (ID_ITEM_FATURA_LOG,ID_FATURA_LOG,ID_ITEM_FATURA,ID_DEVEDOR_DOC_SERV_FAT,NR_VERSAO,LOGIN_LOG,DH_LOG,OP_LOG )
      VALUES (ID_ITEM_FATURA_LOG_NEXT, (SELECT MAX(ID_FATURA_LOG) FROM LOG$_FATURA WHERE ID_FATURA = :new.ID_FATURA), :new.ID_ITEM_FATURA, :new.ID_DEVEDOR_DOC_SERV_FAT, :new.NR_VERSAO, LOGIN_LOG, DH_LOG, OP_LOG );
   ELSE
      INSERT INTO LOG$_ITEM_FATURA (ID_ITEM_FATURA_LOG,ID_FATURA_LOG,ID_ITEM_FATURA,ID_DEVEDOR_DOC_SERV_FAT,NR_VERSAO,LOGIN_LOG,DH_LOG,OP_LOG )
      VALUES (ID_ITEM_FATURA_LOG_NEXT, (SELECT MAX(ID_FATURA_LOG) FROM LOG$_FATURA WHERE ID_FATURA = :old.ID_FATURA), :old.ID_ITEM_FATURA, :old.ID_DEVEDOR_DOC_SERV_FAT, :old.NR_VERSAO, LOGIN_LOG, DH_LOG, OP_LOG );
   END IF;
END;

CREATE OR REPLACE TRIGGER T_LOG$_DESCONTO
AFTER INSERT OR UPDATE OR DELETE ON DESCONTO
FOR EACH ROW
DECLARE
   TZ_REGION VARCHAR(60);
   BATCH_RUNNING VARCHAR2(1);
   INTEGRACAO_RUNNING VARCHAR2(1);
   DH_LOG TIMESTAMP;
   LOGIN_LOG VARCHAR2(60);
   OP_LOG VARCHAR2(1);
   CURSOR c1 IS SELECT TO_NUMBER(DS_PARAMETRO_LOG) FROM PARAMETRO_LOG WHERE NM_TRIGGER_LOG = 'T_LOG$_FATURA' AND NM_PARAMETRO_LOG = 'LAST_ID_FATURA_LOG';
   LAST_ID_FATURA_LOG NUMBER(10);
   ID_DEVEDOR_DOC_SERV_FAT_VAR NUMBER(10);
   ID_ITEM_FATURA_LOG_VAR NUMBER(10);
BEGIN
   select F_INT_APPLICATION_INFO('Processo_Batch') INTO BATCH_RUNNING from dual;
   select F_INT_APPLICATION_INFO('Processo_Integracao') INTO INTEGRACAO_RUNNING from dual;

   IF (INTEGRACAO_RUNNING = '1' OR BATCH_RUNNING = '1') THEN
      RETURN;
   END IF;

   select sessiontimezone INTO TZ_REGION from dual;
   IF (SUBSTR(TZ_REGION, 1, 1) = '-' OR SUBSTR(TZ_REGION, 1, 1) = '+') THEN
      TZ_REGION := 'AMERICA/SAO_PAULO';
   END IF;
   select SYSTIMESTAMP AT time zone (select TZ_REGION from dual) into DH_LOG FROM DUAL;
--   select SYSTIMESTAMP AT time zone (select (select F_INT_APPLICATION_INFO('Timezone_Offset') from dual) || ':00' from dual) into DH_LOG FROM DUAL;
   select login into LOGIN_LOG from usuario where id_usuario = (select F_INT_APPLICATION_INFO('Id_Usuario_Logado') from dual);
   IF INSERTING THEN
     OP_LOG := 'I'; -- I
   ELSE 
     IF UPDATING THEN
       OP_LOG := 'U';
     ELSE
       OP_LOG := 'D';
     END IF;
   END IF;
   
   OPEN c1;
   FETCH c1 INTO LAST_ID_FATURA_LOG;
   CLOSE c1;

   IF (LAST_ID_FATURA_LOG IS NOT NULL) THEN
      IF (INSERTING OR UPDATING) THEN
         ID_DEVEDOR_DOC_SERV_FAT_VAR := :new.ID_DEVEDOR_DOC_SERV_FAT;
      ELSE
         ID_DEVEDOR_DOC_SERV_FAT_VAR := :old.ID_DEVEDOR_DOC_SERV_FAT;
      END IF;
   
      SELECT MAX(ID_ITEM_FATURA_LOG) INTO ID_ITEM_FATURA_LOG_VAR FROM LOG$_ITEM_FATURA WHERE ID_DEVEDOR_DOC_SERV_FAT = ID_DEVEDOR_DOC_SERV_FAT_VAR AND ID_FATURA_LOG = LAST_ID_FATURA_LOG;
   END IF;

   IF (INSERTING OR UPDATING) THEN
      INSERT INTO LOG$_DESCONTO ( ID_DESCONTO_LOG, ID_DESCONTO, ID_ITEM_FATURA_LOG, ID_DEVEDOR_DOC_SERV_FAT, ID_RECIBO_DESCONTO, ID_DEMONSTRATIVO_DESCONTO, ID_MOTIVO_DESCONTO, TP_SITUACAO_APROVACAO, VL_DESCONTO, OB_DESCONTO, ID_PENDENCIA, LOGIN_LOG, DH_LOG, OP_LOG )
      VALUES (LOG$_DESCONTO_SQ.nextval, :new.ID_DESCONTO, ID_ITEM_FATURA_LOG_VAR, :new.ID_DEVEDOR_DOC_SERV_FAT, :new.ID_RECIBO_DESCONTO, :new.ID_DEMONSTRATIVO_DESCONTO, :new.ID_MOTIVO_DESCONTO, :new.TP_SITUACAO_APROVACAO, :new.VL_DESCONTO, :new.OB_DESCONTO, :new.ID_PENDENCIA, LOGIN_LOG, DH_LOG, OP_LOG ); 
   ELSE
      INSERT INTO LOG$_DESCONTO ( ID_DESCONTO_LOG, ID_DESCONTO, ID_ITEM_FATURA_LOG, ID_DEVEDOR_DOC_SERV_FAT, ID_RECIBO_DESCONTO, ID_DEMONSTRATIVO_DESCONTO, ID_MOTIVO_DESCONTO, TP_SITUACAO_APROVACAO, VL_DESCONTO, OB_DESCONTO, ID_PENDENCIA, LOGIN_LOG, DH_LOG, OP_LOG )
      VALUES (LOG$_DESCONTO_SQ.nextval, :old.ID_DESCONTO, ID_ITEM_FATURA_LOG_VAR, :old.ID_DEVEDOR_DOC_SERV_FAT, :old.ID_RECIBO_DESCONTO, :old.ID_DEMONSTRATIVO_DESCONTO, :old.ID_MOTIVO_DESCONTO, :old.TP_SITUACAO_APROVACAO, :old.VL_DESCONTO, :old.OB_DESCONTO, :old.ID_PENDENCIA, LOGIN_LOG, DH_LOG, OP_LOG ); 
   END IF;
END;

--------------------


		desc log$_item_fatura
		DESC ITEM_FATURA
		desc desconto
		desc log$_desconto

		select systimestamp from dual;


		select * from log$_fatura order by id_fatura
		select max(id_fatura_log) from log$_fatura where id_fatura = 805
		
		
		--------------
		
		select SYSTIMESTAMP AT time zone (select '00:00' from dual) FROM DUAL

select * from all_constraints where table_name = 'LOG$_DESCONTO'

desc log$_fatura

declare
  x varchar2(64);
begin
   DBMS_APPLICATION_INFO.SET_CLIENT_INFO ('teste');
   DBMS_APPLICATION_INFO.SET_CLIENT_INFO (null);
   DBMS_APPLICATION_INFO.READ_CLIENT_INFO(x);
   dbms_output.PUT_LINE(x);
end;

select max(id_usuario) from usuario
desc adsm_integra.usuario_adsm

exec 
		*/
	}
	
	public ResultSetPage findPaginatedItems(Map m, FindDefinition findDef) {
		
		StringBuffer hql = new StringBuffer("select");
		hql.append(" new Map(");
		hql.append(" itemFatura.idItemFatura as itemIdItemFatura");
		hql.append(", devedorDocServFat.idDevedorDocServFat as itemIdDevedorDocServFat");
		hql.append(", i.loginLog as itemLoginLog");
		hql.append(", i.dhLog as itemDhLog");
		hql.append(", i.opLog as itemOpLog");
		hql.append(", desconto.idDesconto as descontoIdDesconto");
		hql.append(", reciboDesconto.idReciboDesconto as descontoIdReciboDesconto");
		hql.append(", demonstrativoDesconto.idDemonstrativoDesconto as descontoIdDemonstrativoDesconto");
		hql.append(", motivoDesconto.idMotivoDesconto as descontoIdMotivoDesconto");
		hql.append(", d.tpSituacaoAprovacao as descontoTpSituacaoAprovacao");
		hql.append(", d.vlDesconto as descontoVlDesconto");
		hql.append(", d.obDesconto as descontoObDesconto");
		hql.append(", pendencia.idPendencia as descontoIdPendencia");
		hql.append(", d.loginLog as descontoLoginLog");
		hql.append(", d.dhLog as descontoDhLog");
		hql.append(", d.opLog as descontoOpLog");
		hql.append(" )");
		hql.append(" from");
		hql.append(" ItemFaturaLog as i");

		hql.append(" left join i.itemFatura as itemFatura");
		hql.append(" left join i.devedorDocServFat as devedorDocServFat");
		
		hql.append(" left join i.descontoLogs as d");
		
		hql.append(" left join d.desconto as desconto");
		hql.append(" left join d.reciboDesconto as reciboDesconto");
		hql.append(" left join d.demonstrativoDesconto as demonstrativoDesconto");
		hql.append(" left join d.motivoDesconto as motivoDesconto");
		
		hql.append(" where");
		hql.append(" i.faturaLog.idFaturaLog = :idFaturaLog");
		hql.append(" order by itemFatura.idItemFatura, i.dhLog.value");
		Map<String, Long> m2 = new HashMap<String, Long>();
		m2.put("idFaturaLog", Long.valueOf(m.get("idFaturaLog").toString()));
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), m2);
	}

	public Integer getRowCountItems(Map m) {
		
		StringBuffer hql = new StringBuffer("select");
		hql.append(" count(*) as quantidade");
		hql.append(" from ItemFaturaLog as ifl");
		hql.append(" where");
		hql.append(" ifl.faturaLog.idFaturaLog = ?");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Long[] { Long.valueOf(m.get("idFaturaLog").toString()) });
		return result.intValue();
	}

	protected Class getPersistentClass() {
		return FaturaLog.class;
	}
}