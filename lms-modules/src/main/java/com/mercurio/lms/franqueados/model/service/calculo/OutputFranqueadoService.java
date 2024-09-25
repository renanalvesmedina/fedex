package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.workflow.model.Pendencia;

public class OutputFranqueadoService {
	private Queue dados;

	public void setData(Queue lista) {
		dados = lista;
	}

	private String buildSQLDoctoServicoFranqueado(DoctoServicoFranqueado docto) {
		StringBuilder sbInsert = new StringBuilder("INSERT INTO DOCTO_SERVICO_FRQ_TMP "
				+ "(ID_DOCTO_SERVICO_FRQ,ID_FRANQUIA,ID_DOCTO_SERVICO,ID_MUNICIPIO_COLETA_ENTREGA,TP_FRETE,"
				+ "ID_DOCTO_SERVICO_FRQ_ORIGINAL,TP_OPERACAO,DT_COMPETENCIA,VL_DOCTO_SERVICO,VL_MERCADORIA,"
				+ "VL_ICMS,VL_PIS,VL_COFINS,VL_DESCONTO,VL_GRIS,VL_CUSTO_CARRETEIRO,VL_CUSTO_AEREO,VL_GENERALIDADE,"
				+ "VL_BASE_CALCULO,VL_AJUSTE_BASE_NEGATIVA,NR_KM_TRANSFERENCIA,VL_KM_TRANSFERENCIA,NR_KM_COLETA_ENTREGA,"
				+ "VL_KM_COLETA_ENTREGA,VL_FIXO_COLETA_ENTREGA,VL_REPASSE_ICMS,VL_REPASSE_PIS,VL_REPASSE_COFINS,"
				+ "VL_DESCONTO_LIMITADOR,VL_REPASSE_GENERALIDADE,VL_PARTICIPACAO,VL_DIFERENCA_PARTICIPACAO) "
				+ "values ( (SELECT  nvl(MAX(ID_DOCTO_SERVICO_FRQ),0) + 1 FROM DOCTO_SERVICO_FRQ_TMP) ");
		buildLineSQL(sbInsert, docto.getFranquia());// ID_FRANQUIA
		buildLineSQL(sbInsert, docto.getConhecimento());// ID_DOCTO_SERVICO
		buildLineSQL(sbInsert, docto.getMunicipio());// ID_MUNICIPIO_COLETA_ENTREGA
		buildLineSQL(sbInsert, docto.getTpFrete());// TP_FRETE
		buildLineSQL(sbInsert, docto.getDoctoServicoFranqueadoOriginal());// ID_DOCTO_SERVICO_FRQ_ORIGINAL
		buildLineSQL(sbInsert, docto.getTpOperacao());// TP_OPERACAO
		buildLineSQL(sbInsert, docto.getDtCompetencia());// DT_COMPETENCIA
		buildLineSQL(sbInsert, docto.getVlDoctoServico());// VL_DOCTO_SERVICO
		buildLineSQL(sbInsert, docto.getVlMercadoria());// VL_MERCADORIA
		buildLineSQL(sbInsert, docto.getVlIcms());// VL_ICMS
		buildLineSQL(sbInsert, docto.getVlPis());// VL_PIS
		buildLineSQL(sbInsert, docto.getVlCofins());// VL_COFINS
		buildLineSQL(sbInsert, docto.getVlDesconto());// VL_DESCONTO
		buildLineSQL(sbInsert, docto.getVlGris());// VL_GRIS
		buildLineSQL(sbInsert, docto.getVlCustoCarreteiro());// VL_CUSTO_CARRETEIRO
		buildLineSQL(sbInsert, docto.getVlCustoAereo());// VL_CUSTO_AEREO
		buildLineSQL(sbInsert, docto.getVlGeneralidade());// VL_GENERALIDADE
		buildLineSQL(sbInsert, docto.getVlBaseCalculo());// VL_BASE_CALCULO
		buildLineSQL(sbInsert, docto.getVlAjusteBaseNegativa());// VL_AJUSTE_BASE_NEGATIVA
		buildLineSQL(sbInsert, docto.getNrKmTransferencia());// NR_KM_TRANSFERENCIA
		buildLineSQL(sbInsert, docto.getVlKmTransferencia());// VL_KM_TRANSFERENCIA
		buildLineSQL(sbInsert, docto.getNrKmColetaEntrega());// NR_KM_COLETA_ENTREGA
		buildLineSQL(sbInsert, docto.getVlKmColetaEntrega());// VL_KM_COLETA_ENTREGA
		buildLineSQL(sbInsert, docto.getVlFixoColetaEntrega());// VL_FIXO_COLETA_ENTREGA
		buildLineSQL(sbInsert, docto.getVlRepasseIcms());// VL_REPASSE_ICMS
		buildLineSQL(sbInsert, docto.getVlRepassePis());// VL_REPASSE_PIS
		buildLineSQL(sbInsert, docto.getVlRepasseCofins());// VL_REPASSE_COFINS
		buildLineSQL(sbInsert, docto.getVlDescontoLimitador());// VL_DESCONTO_LIMITADOR
		buildLineSQL(sbInsert, docto.getVlRepasseGeneralidade());// VL_REPASSE_GENERALIDADE
		buildLineSQL(sbInsert, docto.getVlParticipacao());// VL_PARTICIPACAO
		buildLineSQL(sbInsert, docto.getVlDiferencaParticipacao());// VL_DIFERENCA_PARTICIPACAO
		sbInsert.append(");");
		return sbInsert.toString();
	}

	private String buildSQLLancamentoFranqueado(LancamentoFranqueado docto) {
		StringBuilder sbInsert = new StringBuilder("INSERT INTO LANCAMENTO_FRQ_TEMP ( ID_LANCAMENTO_FRQ,"
				+ "ID_PENDENCIA,ID_FRANQUIA,ID_CONTA_CONTABIL_FRQ,DT_COMPETENCIA,VL_LANCAMENTO,"
				+ "TP_SITUACAO_PENDENCIA,SG_DOCTO_INTERNACIONAL,CD_DOCTO_INTERNACIONAL,NR_DOCTO_INTERNACIONAL,"
				+ "OB_LANCAMENTO,DS_LANCAMENTO,NR_VERSAO,NR_PARCELAS,VL_CONTABIL) values ("
				+ "(SELECT  nvl(MAX(ID_LANCAMENTO_FRQ),0) + 1 FROM LANCAMENTO_FRQ_TEMP) ");
		buildLineSQL(sbInsert, docto.getPendencia());// ID_PENDENCIA
		buildLineSQL(sbInsert, docto.getFranquia());// ID_FRANQUIA
		buildLineSQL(sbInsert, docto.getContaContabilFranqueado());// ID_CONTA_CONTABIL_FRQ
		buildLineSQL(sbInsert, docto.getDtCompetencia());// DT_COMPETENCIA
		buildLineSQL(sbInsert, docto.getVlLancamento());// VL_LANCAMENTO
		buildLineSQL(sbInsert, docto.getTpSituacaoPendencia());// TP_SITUACAO_PENDENCIA
		buildLineSQL(sbInsert, docto.getSgDoctoInternacional());// SG_DOCTO_INTERNACIONAL
		buildLineSQL(sbInsert, docto.getCdDoctoInternacional());// CD_DOCTO_INTERNACIONAL
		buildLineSQL(sbInsert, docto.getNrDoctoInternacional());// NR_DOCTO_INTERNACIONAL
		buildLineSQL(sbInsert, docto.getObLancamento());// OB_LANCAMENTO
		buildLineSQL(sbInsert, docto.getDsLancamento());// DS_LANCAMENTO
		buildLineSQL(sbInsert, "null");// NR_VERSAO
		buildLineSQL(sbInsert, docto.getNrParcelas());// NR_PARCELAS
		buildLineSQL(sbInsert, docto.getVlContabil());// VL_CONTABIL
		sbInsert.append(");");
		return sbInsert.toString();
	}

	private String buildSQLReembarqueFranqueado(ReembarqueDoctoServicoFranqueado docto) {
		StringBuilder sbInsert = new StringBuilder("INSERT INTO REEMBARQUE_DOC_SERV_FRQ_TMP "
				+ "(ID_REEMBARQUE_DOC_SERV_FRQ,ID_FRANQUIA,ID_MANIFESTO,ID_DOCTO_SERVICO,"
				+ "DT_COMPETENCIA,PS_MERCADORIA,VL_CTE,VL_TONELADA ) values ( "
				+ "(SELECT  nvl(MAX(ID_REEMBARQUE_DOC_SERV_FRQ),0) + 1 FROM REEMBARQUE_DOC_SERV_FRQ_TMP) ");
		buildLineSQL(sbInsert, docto.getFranquia());// ID_FRANQUIA
		buildLineSQL(sbInsert, docto.getManifesto());// ID_MANIFESTO
		buildLineSQL(sbInsert, docto.getConhecimento());// ID_DOCTO_SERVICO
		buildLineSQL(sbInsert, docto.getDtCompetencia());// DT_COMPETENCIA
		buildLineSQL(sbInsert, docto.getPsMercadoria());// PS_MERCADORIA
		buildLineSQL(sbInsert, docto.getVlCte());// VL_CTE
		buildLineSQL(sbInsert, docto.getVlTonelada());// VL_TONELADA
		sbInsert.append(");");
		return sbInsert.toString();
	}

	private void buildLineSQL(StringBuilder sql, Object valor) {
		if (valor == null) {
			sql.append(",null");
		} else if (valor instanceof String) {
			sql.append(",'" + valor + "'");
		} else if (valor instanceof BigDecimal) {
			sql.append("," + valor);
		} else if (valor instanceof Long) {
			sql.append("," + valor);
		} else if (valor instanceof Municipio) {
			sql.append("," + ((Municipio) valor).getIdMunicipio());
		} else if (valor instanceof DoctoServicoFranqueado) {
			sql.append("," + ((DoctoServicoFranqueado) valor).getIdDoctoServicoFrq());
		} else if (valor instanceof Franquia) {
			sql.append("," + ((Franquia) valor).getIdFranquia());
		} else if (valor instanceof DoctoServico) {
			sql.append("," + ((DoctoServico) valor).getIdDoctoServico());
		} else if (valor instanceof DomainValue) {
			sql.append(",'" + ((DomainValue) valor).getValue() + "'");
		} else if (valor instanceof YearMonthDay) {
			sql.append(",to_date('" + ((YearMonthDay) valor).toString("yyyy-MM-dd") + "','yyyy-mm-dd')");
		} else if (valor instanceof Integer) {
			sql.append("," + valor);
		} else if (valor instanceof Pendencia) {
			sql.append("," + ((Pendencia) valor).getIdPendencia());
		} else if (valor instanceof ContaContabilFranqueado) {
			sql.append("," + ((ContaContabilFranqueado) valor).getIdContaContabilFrq());
		} else if (valor instanceof Manifesto) {
			sql.append("," + ((Manifesto) valor).getIdManifesto());
		}
	}

	public List<String> toSQL() {
		List<String> list = new ArrayList<String>();
		while (!dados.isEmpty()) {
			Object o = dados.poll();
			if (o instanceof DoctoServicoFranqueado) {
				String insert = buildSQLDoctoServicoFranqueado((DoctoServicoFranqueado) o);
				list.add(insert);
			} else if (o instanceof LancamentoFranqueado) {
				String insert = buildSQLLancamentoFranqueado((LancamentoFranqueado) o);
				list.add(insert);
			} else if (o instanceof ReembarqueDoctoServicoFranqueado) {
				String insert = buildSQLReembarqueFranqueado((ReembarqueDoctoServicoFranqueado) o);
				list.add(insert);
			}
		}
		return list;
	}

}
