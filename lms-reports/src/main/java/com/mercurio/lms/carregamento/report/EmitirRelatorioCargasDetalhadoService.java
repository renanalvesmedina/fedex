package com.mercurio.lms.carregamento.report;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.joda.time.YearMonthDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsvel pela gerao de Relatrio de Cargas no Terminal
 * (Detalhado) Especificao tcnica 05.02.01.01
 *
 * @spring.bean id="lms.carregamento.emitirRelatorioCargasDetalhadoService"
 * @spring.property name="reportName" value=
 * "com/mercurio/lms/carregamento/report/emitirRelatorioCargasDetalhado.jasper"
 */

public class EmitirRelatorioCargasDetalhadoService extends
        EmitirRelatorioCargasService {

	/**
	 * mtodo responsvel por gerar o relatrio.
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		StringBuilder sql = new StringBuilder()
				.append("SELECT * FROM ( ")
				.append("SELECT ");
		getDtAgendamentoField(tfm, sql);
		getProdutoPerigosoField(tfm, sql);
		getProdutoControladoField(tfm, sql);
		sql.append("filialDestino.SG_FILIAL as SG_FILIAL_DESTINO, ")
				.append("filialOrigem.ID_FILIAL as ID_FILIAL_ORIGEM, ")
				.append("filialOrigem.SG_FILIAL as SG_FILIAL_ORIGEM, ")
				.append("filialLocalizacao.SG_FILIAL as SG_FILIAL_LOCALIZACAO, ")
				.append("(TRUNC(sysdate)  -  TRUNC(ds.DT_PREV_ENTREGA)) as NR_DIASARMAZENAMENTO, ")
				.append(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I", "DS_LOCALIZACAO_MERCADORIA") + ", ")
				.append("ds.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO, ")
				.append("ds.NR_DOCTO_SERVICO as NR_DOCTO_SERVICO, ")
				.append("TRUNC(CAST(ds.DH_EMISSAO AS DATE)) as DT_EMISSAO, ")
				.append("servico.SG_SERVICO as SG_SERVICO, ")
				.append("conhecimento.TP_FRETE, ")
				.append("ds.QT_VOLUMES as QT_VOLUMES, ")
				.append("ds.PS_REAL as PS_REAL, ")
				.append("ds.PS_AFORADO as PS_AFORADO, ")
				.append("ds.VL_MERCADORIA as VL_MERCADORIA, ")
				.append("ds.VL_MERCADORIA as VL_MERCADORIA_CONVERT, ")
				.append("ds.VL_TOTAL_DOC_SERVICO as VL_TOTAL_DOC_SERVICO, ")
				.append("ds.VL_TOTAL_DOC_SERVICO as VL_TOTAL_DOC_SERVICO_CONVERT, ")
				.append("'R$' as DS_SIMBOLO, 1 as  ID_MOEDA, 'BRL' 	as SG_MOEDA, ")
				.append("pessoaRemetente.NM_PESSOA AS DS_REMETENTE, ")
				.append("pessoaDestinatario.NM_PESSOA AS DS_DESTINATARIO, ")
				.append("ds.DT_PREV_ENTREGA as DT_PREV_ENTREGA, ")
				.append("DECODE (ds.ID_ROTA_COLETA_ENTREGA_REAL, NULL, rce_sug.NR_ROTA || ' ' || rce_sug.DS_ROTA, ")
				.append("rce_real.NR_rota || ' ' || rce_real.DS_ROTA ) as DS_ROTA, ")
				.append("tipoServico.BL_PRIORIZAR as BL_PRIORIZAR, ")
				.append("( ")
				.append("TRUNC(?) - TRUNC(( ")
				.append("SELECT MAX(eds.DH_EVENTO) as dtMaior ")
				.append("FROM EVENTO_DOCUMENTO_SERVICO eds, evento ev ")
				.append("WHERE ")
				.append("eds.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO ")
				.append("AND ev.ID_EVENTO = eds.ID_EVENTO ")
				.append("AND eds.BL_EVENTO_CANCELADO = 'N' ")
				.append("AND NVL(ev.ID_LOCALIZACAO_MERCADORIA, 0) > 0 ) ")
				.append(")) ")
				.append("NR_DIAS, ")
				.append("(SELECT count(*) as total ")
				.append("FROM EVENTO_DOCUMENTO_SERVICO eds, evento ev ")
				.append("WHERE ")
				.append("eds.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO ")
				.append("AND ev.ID_EVENTO = eds.ID_EVENTO ")
				.append("AND ev.CD_EVENTO = 59 ) ")
				.append("as RETORNOS_ENTREGAS, ")
				.append("(case ")
				.append("when TRUNC(?) > ds.DT_PREV_ENTREGA then 'N' ")
				.append("else 'S' ")
				.append("end ")
				.append(") as BL_GRIFAR_DPE, ")
				.append("municipio.NM_MUNICIPIO as NM_MUNICIPIO, ")
				.append("DECODE (ds.ID_ROTA_COLETA_ENTREGA_REAL, NULL, ")
				.append(" rce_sug.NR_rota , rce_real.NR_rota ) as nrRota, ")
				.append("pessoaRemetente.nr_identificacao as nrIdentificacaoRemetente, ")
				.append("pessoaDestinatario.nr_identificacao as nrIdentificacaoDestinatario, ")
				.append("ds.ds_endereco_entrega_real as dsEnderecoEntregaReal, ")
				.append("LPAD(conhecimento.nr_cep_entrega, 8, '0') as nrCepEntrega, ")
				.append(getColunasSelectAgendamentoRoteirizacao(tfm))
				.append("ufFilialDestino.sg_unidade_federativa as sgUfDestino ")
				.append("FROM ")
				.append(getAguardandoAgendamentoTables(tfm))
				.append(getDocumentosComAgendamentoTables(tfm))
				.append(getAgendamentoRoteirizacaoTables(tfm))
				.append("DOCTO_SERVICO ds, FILIAL filialOrigem, unidade_federativa uffilialdestino, FILIAL filialDestino, LOCALIZACAO_MERCADORIA lm, ")
				.append("FILIAL filialLocalizacao, ")
				.append("FLUXO_FILIAL ff, ")
				.append("SERVICO servico, CONHECIMENTO conhecimento, TIPO_SERVICO tipoServico, ")
				.append("PESSOA pessoaRemetente, PESSOA pessoaDestinatario, ")
				.append("MUNICIPIO municipio, ")
				.append("rota_coleta_entrega rce_real, rota_coleta_entrega rce_sug ")
				.append("WHERE	 ")
				.append("filialOrigem.ID_FILIAL = ds.ID_FILIAL_ORIGEM ")
				.append(getAguardandoAgendamentoFilter(tfm))
				.append(getDocumentosComAgendamentoFilter(tfm))
				.append(getAgendamentoRoteirizacaoFilter(tfm))
				.append("AND ds.id_rota_coleta_entrega_real = rce_real.id_rota_coleta_entrega(+) ")
				.append("AND ds.id_rota_coleta_entrega_sugerid = rce_sug.id_rota_coleta_entrega(+) ")
				.append("AND filialDestino.ID_FILIAL = ds.ID_FILIAL_DESTINO ")
				.append("AND lm.ID_LOCALIZACAO_MERCADORIA = ds.ID_LOCALIZACAO_MERCADORIA ")
				.append("AND filialLocalizacao.ID_FILIAL = ds.ID_FILIAL_LOCALIZACAO ")
				.append("AND ds.ID_SERVICO = servico.ID_SERVICO ")
				.append("AND ds.ID_DOCTO_SERVICO = conhecimento.ID_CONHECIMENTO(+) ")
				.append("AND conhecimento.ID_MUNICIPIO_ENTREGA = municipio.ID_MUNICIPIO(+) ")
				.append("AND ufFilialDestino.ID_UNIDADE_FEDERATIVA(+) = municipio.ID_UNIDADE_FEDERATIVA ")
				.append("AND servico.ID_TIPO_SERVICO = tipoServico.ID_TIPO_SERVICO ")
				.append("AND ds.id_cliente_remetente = pessoaRemetente.ID_PESSOA(+) ")
				.append("AND ds.id_cliente_destinatario = pessoaDestinatario.ID_PESSOA(+) ")
				.append("AND ds.id_Fluxo_filial = ff.id_fluxo_filial(+) ")
				.append("AND ds.TP_DOCUMENTO_SERVICO in ('MDA', 'NTE', 'CTE') ")
				.append("AND ds.NR_DOCTO_SERVICO > 0 ");

		SqlTemplate sqlTemplate = createSqlTemplate();
		List criterias = new ArrayList();

		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		criterias.add(dtAtual);
		criterias.add(dtAtual);

		sql.append(populateCriterios(tfm, criterias, sqlTemplate));

		sql.append(" order by filialDestino.SG_FILIAL, ds.TP_DOCUMENTO_SERVICO, filialOrigem.SG_FILIAL, ds.NR_DOCTO_SERVICO) ");

		populateCriterioDias(sql, criterias, tfm);

		JRReportDataObject jr = executeQuery(sql.toString(), criterias.toArray());
		jr.setParameters(getParametrosRelatorio(tfm, sqlTemplate));

		if (tfm.getString("formatoRelatorio").equals("xls")) {
			setReportName("com/mercurio/lms/carregamento/report/emitirRelatorioCargasDetalhadoExcel.jasper");
		} else {
			setReportName("com/mercurio/lms/carregamento/report/emitirRelatorioCargasDetalhado.jasper");
		}

		return jr;
	}

	private String getAgendamentoRoteirizacaoFilter(TypedFlatMap tfm) {
		if (isRoteirizacao(tfm)) {
			StringBuilder sql = new StringBuilder();

			if (isAgendamento(tfm)) {
				sql.append(" AND ae.id_turno = turno.id_turno(+) ");
				sql.append(" AND ae.tp_agendamento = valorDominioAgendamento.VL_VALOR_DOMINIO ");
				sql.append(" AND dominioAgendamento.id_dominio = valorDominioAgendamento.id_dominio ");
				sql.append(" AND dominioAgendamento.nm_dominio = 'DM_TIPO_AGENDAMENTO' ");
			}

			return sql.toString();
		}

		return "";
	}

	private String getAgendamentoRoteirizacaoTables(TypedFlatMap tfm) {
		if (isRoteirizacao(tfm)) {
			StringBuilder sql = new StringBuilder();

			if (isAgendamento(tfm)) {
				sql.append(" TURNO turno, DOMINIO dominioAgendamento, VALOR_DOMINIO valorDominioAgendamento, ");
			}

			return sql.toString();
		}

		return "";
	}

	private StringBuilder getColunasSelectAgendamentoRoteirizacao(TypedFlatMap tfm) {
		StringBuilder sql = new StringBuilder();
		if (isRoteirizacao(tfm)) {
			if (isAgendamento(tfm)) {
				sql.append("SUBSTR(REGEXP_SUBSTR(valorDominioAgendamento.ds_valor_dominio_i, 'pt_BR?[^?]+'), INSTR(REGEXP_SUBSTR(valorDominioAgendamento.ds_valor_dominio_i, 'pt_BR?[^?]+'), 'pt_BR?')+LENGTH('pt_BR?')) AS tpAgendamento, ");
				sql.append("ae.hr_preferencia_inicial as hrPreferenciaInicial, ");
				sql.append("ae.hr_preferencia_final as hrPreferenciaFinal, ");
				sql.append("turno.ds_turno as dsTurno, ");
				sql.append("turno.hr_turno_inicial as hrTurnoInicial, ");
				sql.append("turno.hr_turno_final as hrTurnoFinal, ");
			} else {
				sql = getColunasSelectRoteirizacaoAgendamentoNull(tfm);
			}
			sql.append(" ds.TP_CARGA_DOCUMENTO, ");
		} else {
			sql = getColunasSelectRoteirizacaoAgendamentoNull(tfm);
			sql.append("null as TP_CARGA_DOCUMENTO, ");
		}
		return sql;
	}

	private StringBuilder getColunasSelectRoteirizacaoAgendamentoNull(TypedFlatMap tfm) {
		StringBuilder sql = new StringBuilder();
		sql.append("NULL as tpAgendamento, ");
		sql.append("NULL as hrPreferenciaInicial, ");
		sql.append("NULL as hrPreferenciaFinal, ");
		sql.append("NULL as dsTurno, ");
		sql.append("NULL as hrTurnoInicial, ");
		sql.append("NULL as hrTurnoFinal, ");
		return sql;
	}
}
