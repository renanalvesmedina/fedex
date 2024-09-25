package com.mercurio.lms.vendas.util;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.DadosTabelaPrecoDTO;
import com.mercurio.lms.vendas.dto.DadosEfetivacaoPipelineDTO;
import com.mercurio.lms.vendas.dto.DadosEmbarquePipelineDTO;
import com.mercurio.lms.vendas.model.PipelineEtapa;
import com.mercurio.lms.vendas.model.Simulacao;

public class PipelineEtapaHelper {

	public static DadosEmbarquePipelineDTO chooseDadosEmbrarquePipelineDto(DadosEmbarquePipelineDTO dadosByTabelasPrecos,
			DadosEmbarquePipelineDTO dadosBySimulacoes) {
		if (dadosByTabelasPrecos != null && dadosBySimulacoes != null) {
			if (dadosByTabelasPrecos.getDhEmissao().isBefore(dadosBySimulacoes.getDhEmissao())){
				return dadosByTabelasPrecos;
			} else {
				return dadosBySimulacoes;
			}
		}
		if(dadosByTabelasPrecos != null) {
			return dadosByTabelasPrecos;
		}
		if (dadosBySimulacoes != null) {
			return dadosBySimulacoes;
		}
		return null;
	}

	public static DadosEfetivacaoPipelineDTO createDadosEfetivacaoPipelineDTO(DadosTabelaPrecoDTO dadosTabelaPreco, Simulacao simulacao) {
		if (dadosTabelaPreco != null && simulacao != null) {
			if (dadosTabelaPreco.getDtVigenciaInicial().isBefore(simulacao.getDtEfetivacao())) {
				return buildByDadosTabelaPreco(dadosTabelaPreco);
			} else {
				return buildBySimulacao(simulacao);
			}
		}
		if (dadosTabelaPreco != null) {
			return buildByDadosTabelaPreco(dadosTabelaPreco);
		}
		if (simulacao != null) {
			return buildBySimulacao(simulacao);
		}
		return null;
	}

	private static DadosEfetivacaoPipelineDTO buildBySimulacao(Simulacao simulacao) {
		DadosEfetivacaoPipelineDTO result = new DadosEfetivacaoPipelineDTO();
		result.setDtEvento(simulacao.getDtEfetivacao());
		result.setDsEvento(simulacao.getFilial().getSgFilial() + " - " + StringUtils.leftPad(""+simulacao.getNrSimulacao(), 6, "0"));
		return result;
	}

	private static DadosEfetivacaoPipelineDTO buildByDadosTabelaPreco(DadosTabelaPrecoDTO dadosTabelaPreco) {
		DadosEfetivacaoPipelineDTO result = new DadosEfetivacaoPipelineDTO();
		result.setDtEvento(dadosTabelaPreco.getDtVigenciaInicial());
		result.setDsEvento(dadosTabelaPreco.toFormattedTabelaPreco());
		return result;
	}

	public static boolean isFechamentoNegocio(PipelineEtapa bean) {
		return bean != null && bean.getTpPipelineEtapa() != null && "35".equals(bean.getTpPipelineEtapa().getValue());
	}
	
	public static boolean isCancelamento(PipelineEtapa bean) {
		return bean != null && bean.getTpPipelineEtapa() != null && "60".equals(bean.getTpPipelineEtapa());
	}

	public static boolean hasDataFilled(TypedFlatMap mapa, int i) {
		return mapa.getLong("idPipelineEtapa" + i) != null || mapa.getYearMonthDay("dtEvento" + i) != null 
				|| StringUtils.isNotBlank(mapa.getString("dsObservacao" + i));
	}
	
}
