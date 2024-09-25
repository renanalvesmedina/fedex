package com.mercurio.lms.vendas.util;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.PipelineCliente;

public class PipelineClienteHelper {

	public static YearMonthDay createYearMonthDayByFechamento(PipelineCliente bean) {
		if (!bean.hasNrMesAnoFechamentoFilled()) {
			return null;
		}
		YearMonthDay dataFechamento = JTDateTimeUtils.getDataAtual()
			.withYear(Integer.valueOf(bean.getNrAnoFechamento()))
			.withMonthOfYear(Integer.valueOf(bean.getNrMesFechamento())).withDayOfMonth(1);
		
		return dataFechamento;
	}

	public static YearMonthDay createYearMonthDayByFechamentoAtualizado(PipelineCliente bean) {
		if (!bean.hasNrMesAnoFechamentoAtualizadoFilled()) {
			return null;
		}
		YearMonthDay dataFechamentoAtualizado = JTDateTimeUtils.getDataAtual()
			.withYear(Integer.valueOf(bean.getNrAnoFechamentoAtualizado()))
			.withMonthOfYear(Integer.valueOf(bean.getNrMesFechamentoAtualizado())).withDayOfMonth(1);
		
		return dataFechamentoAtualizado;
	}

}
