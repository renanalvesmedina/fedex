package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.ServicoAdicionalCalculo;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;


public class CalculoServicoAdicionalService extends CalculoParcelaServicoService {
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;
	private ParametroGeralService parametroGeralService;
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
	/**
	 * 
	 * @param idServico
	 * @param idDivisaoCliente
	 * @param servicoCalculo
	 */
	public void executeCalculo(Long idServico, Long idDivisaoCliente, ServicoAdicionalCalculo servicoCalculo, Long idDoctoServico) {
		List<ServicoAdicionalCalculo> listaServicoAdicionalCalculo = new ArrayList<ServicoAdicionalCalculo>();
		listaServicoAdicionalCalculo.add(servicoCalculo);
		executeCalculo(idServico, idDivisaoCliente, listaServicoAdicionalCalculo, idDoctoServico);
	}
	
	/**
	 * 
	 * @param idServico
	 * @param idDivisaoCliente
	 * @param listaServicosCalculo
	 */
	public void executeCalculo(Long idServico, Long idDivisaoCliente, List<ServicoAdicionalCalculo> listaServicosCalculo, Long idDoctoServico) {
		List<ServicoAdicionalPrecificado> servicosPrecificados = tabelaServicoAdicionalService.findByTabelaCliente(idServico, idDivisaoCliente);
		List<ServicoAdicionalPrecificado> servicosPrecificadosPadrao = tabelaServicoAdicionalService.findByTabelaPadraoByIdServico(idServico);
		
		for (ServicoAdicionalCalculo servicoCalculo : listaServicosCalculo) {
			ServicoAdicionalPrecificado precificacao = tabelaServicoAdicionalService.getByCdParcela(servicoCalculo.getCdParcela(), servicosPrecificados);
			servicoCalculo.setServicoAdicionalPrecificado(precificacao);

			if (precificacao != null) {
				BigDecimal vlServico = precificacao.getVlServico();
				BigDecimal vlMinimoServico = precificacao.getVlMinimoServico();
				BigDecimal vlServicoComplementar = null;
				if (precificacao.getServicoAdicionalComplementar() != null) {
					vlServicoComplementar = precificacao.getServicoAdicionalComplementar().getVlServico();
				}

				// LMSA-372
				Boolean isTpUnidadeMedidoCobrTonelada = false;
				if (ConstantesExpedicao.CD_ARMAZENAGEM.equals(servicoCalculo.getCdParcela()) || 
						ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(servicoCalculo.getCdParcela())) {
					isTpUnidadeMedidoCobrTonelada = findIsTpUnidadeMedidoCobrTonelada(idDivisaoCliente, servicoCalculo.getCdParcela());
				}
				
				servicoCalculo.setIsTpUnidadeMedidoCobrTonelada(isTpUnidadeMedidoCobrTonelada);

				BigDecimal vlCalculado = executeCalculo(vlServico, vlMinimoServico, vlServicoComplementar, servicoCalculo);
				
				if (vlCalculado == null || vlCalculado.signum() == 0) {
					ServicoAdicionalPrecificado precificacaoTabelaPadrao = tabelaServicoAdicionalService.getByCdParcela(servicoCalculo.getCdParcela(), servicosPrecificadosPadrao);
					servicoCalculo.setServicoAdicionalPrecificado(precificacaoTabelaPadrao);
					
					vlServico = precificacaoTabelaPadrao.getVlServico();
					vlMinimoServico = precificacaoTabelaPadrao.getVlMinimoServico();
					
					if (precificacaoTabelaPadrao.getServicoAdicionalComplementar() != null) {
						vlServicoComplementar = precificacaoTabelaPadrao.getServicoAdicionalComplementar().getVlServico();
					}
					
					vlCalculado = executeCalculo(vlServico, vlMinimoServico, vlServicoComplementar, servicoCalculo);
					servicoCalculo.setVlCalculado(vlCalculado);
				} else {
					servicoCalculo.setVlCalculado(vlCalculado);
				}
				servicoCalculo.setVlTabela(vlCalculado);
			} else {
				if (ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS.equals(servicoCalculo.getCdParcela()) || ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS_NO_CLIENTE.equals(servicoCalculo.getCdParcela())) {
					servicoCalculo.setVlCalculado(getValorArredondado(servicoCalculo.getVlNegociado()));
				} else {
					throw new BusinessException("LMS-30030", new Object[] { servicoCalculo.getCdParcela() });
				}
			}
		}
	}
	
	private Boolean findIsTpUnidadeMedidoCobrTonelada(Long idDivisaoCliente,String cdParcelaPreco) {

		ServicoAdicionalCliente servicoSeguroPermanencia = this.findServicoAdicionalCliente(idDivisaoCliente, cdParcelaPreco);
		
		if(servicoSeguroPermanencia != null && servicoSeguroPermanencia.getTpUnidMedidaCalcCobr() != null && "T".equalsIgnoreCase(servicoSeguroPermanencia.getTpUnidMedidaCalcCobr().getValue())){
			return true;
		}
		return false;
	}
	
	public BigDecimal executeCalculo(BigDecimal vlTabela, BigDecimal vlMinimo,
			BigDecimal vlServicoComplementar, ServicoAdicionalCalculo servicoCalculo) {
		String cdParcelaPreco = servicoCalculo.getCdParcela();
		BigDecimal vlFrete = servicoCalculo.getVlFrete();
		BigDecimal vlMercadoria = servicoCalculo.getVlMercadoria();
		BigDecimal psReferencia = servicoCalculo.getPsReferencia();
		BigDecimal vlCusto = servicoCalculo.getVlCusto();
		BigDecimal vlNegociado = servicoCalculo.getVlNegociado();
		Integer qtDias = servicoCalculo.getQtDias();
		Integer qtPaletes = servicoCalculo.getQtPaletes();
		Integer qtKmRodados = servicoCalculo.getQtKmRodados();
		Integer qtSegurancasAdicionais = servicoCalculo.getQtSegurancasAdicionais();
		Boolean blPagaSeguro = servicoCalculo.getBlPagaSeguro();
		Boolean isTpUnidadeMedidoCobrTonelada = servicoCalculo.getIsTpUnidadeMedidoCobrTonelada();
		
		if(ConstantesExpedicao.CD_AGENDAMENTO_COLETA.equals(cdParcelaPreco)) {
			return executeCalculoAgendamento(vlTabela, vlMinimo, vlFrete);
		} else if(ConstantesExpedicao.CD_PALETIZACAO.equals(cdParcelaPreco)) {
			return executeCalculoPaletizacao(vlTabela, qtPaletes);
		} else if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcelaPreco)) {
			return executeCalculoArmazenagem(vlTabela, vlMinimo, vlServicoComplementar, qtDias, vlMercadoria, psReferencia, blPagaSeguro, cdParcelaPreco, isTpUnidadeMedidoCobrTonelada);
		} else if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)) {
			return executeCalculoEscolta(vlTabela, vlServicoComplementar, qtSegurancasAdicionais, qtKmRodados);
		} else if(ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO.equals(cdParcelaPreco) || 
				ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_TOCO.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_VAN.equals(cdParcelaPreco)) {
			return executeCalculoVeiculoDedicado(vlTabela, vlServicoComplementar, qtKmRodados);
		} else if(ConstantesExpedicao.CD_ESTADIA_VEICULO.equals(cdParcelaPreco) || 
				ConstantesExpedicao.CD_ESTADIA_CARRETA.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_ESTADIA_CONJUNTO.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_ESTADIA_34.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_ESTADIA_TRUCK.equals(cdParcelaPreco)) {
			return executeCalculoEstadia(vlTabela, qtDias);
		} else if(ConstantesExpedicao.CD_CAPATAZIA.equals(cdParcelaPreco)) {
			return executeCalculoCapatazia(vlTabela, vlCusto);
		} else if(ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS_NO_CLIENTE.equals(cdParcelaPreco)) {
			return executeCalculoOperacoesLogisticas(vlNegociado);
		}
				
		return null;			
	}	
	
	private BigDecimal executeCalculoAgendamento(BigDecimal vlTabela, BigDecimal vlMinimo, BigDecimal vlFrete) {
		if(vlTabela == null || vlFrete == null) {
			throw new IllegalArgumentException();
		}
		return getValorArredondado(getMax(getValorPercentual(vlFrete, vlTabela), vlMinimo));
	}
	
	private BigDecimal executeCalculoPaletizacao(BigDecimal vlTabela, Integer qtPalete) {
		if(vlTabela == null || qtPalete == null) {
			throw new IllegalArgumentException();
		}
		return getValorArredondado(vlTabela.multiply(new BigDecimal(qtPalete)));
	}
	
	private BigDecimal executeCalculoArmazenagem(BigDecimal vlTabela, BigDecimal vlMinimo, BigDecimal pcSeguro, 
			Integer qtDias, BigDecimal vlMercadoria, BigDecimal psReferencia, Boolean blPagaSeguro, String cdParcelaPreco, Boolean isTpUnidadeMedidoCobrEqualsT) {
		if(vlTabela == null || (Boolean.TRUE.equals(blPagaSeguro) && pcSeguro == null)
				|| qtDias == null || vlMercadoria == null || psReferencia == null) {
			throw new IllegalArgumentException();
		}
        
        // LMSA-7276
        if (BigDecimal.ZERO.compareTo(vlTabela) == 0 && BigDecimal.ZERO.compareTo(vlMinimo) == 0) {
            return BigDecimal.ZERO;
        }
        // LMSA-7276

		BigDecimal vlArmazenagem =  null;
		
		if(isTpUnidadeMedidoCobrEqualsT){
			vlArmazenagem = (psReferencia.add(BigDecimalUtils.getBigDecimal("999.999"))).divide(BigDecimalUtils.getBigDecimal("1000"));
			vlArmazenagem = vlArmazenagem.setScale(0, RoundingMode.DOWN);
			vlArmazenagem = (vlArmazenagem).multiply(BigDecimal.valueOf(qtDias)).multiply(vlTabela);
		}else{
			vlArmazenagem = psReferencia.multiply(BigDecimal.valueOf(qtDias)).multiply(vlTabela);
		}
			
		vlArmazenagem = getValorArredondado(getMax(vlArmazenagem, vlMinimo));
		
		if (Boolean.TRUE.equals(blPagaSeguro)) { 
			BigDecimal vlSeguro = getValorArredondado(getValorPercentual(vlMercadoria, pcSeguro));
			vlArmazenagem = vlArmazenagem.add(vlSeguro);
		}
		
		return getValorArredondado(vlArmazenagem);
	}
	
	private BigDecimal executeCalculoEscolta(BigDecimal vlTabela, BigDecimal vlSegurancaAdicional, 
			Integer qtSegurancasAdicionais, Integer qtKmRodados) {			
		if(vlTabela == null || qtKmRodados == null) {
			throw new IllegalArgumentException();
		}
		
		BigDecimal vlAdicional = BigDecimal.ZERO;		
		if(vlSegurancaAdicional != null && qtSegurancasAdicionais != null) {
			vlAdicional = vlSegurancaAdicional.multiply(BigDecimal.valueOf(qtSegurancasAdicionais));
		}
		
		return getValorArredondado(vlTabela.add(vlAdicional).multiply(BigDecimal.valueOf(qtKmRodados)));
	}
	
	private BigDecimal executeCalculoVeiculoDedicado(BigDecimal vlTabela, BigDecimal vlKmAdicional, 
			Integer qtKmRodados) {			
		if(vlTabela == null || qtKmRodados == null) {
			throw new IllegalArgumentException();
		}
		
		BigDecimal vlAdicional = BigDecimal.ZERO;
		if(vlKmAdicional != null && qtKmRodados > 100) {			
			vlAdicional = BigDecimal.valueOf(qtKmRodados).subtract(HUNDRED).multiply(vlKmAdicional);
		}
		
		return getValorArredondado(vlTabela.add(vlAdicional));
	}
	
	public Integer executeCalculoPeriodosEstadia(DateTime dhInicio, DateTime dhFim) {
		Integer qtMinutos = null;
		Integer qtPeriodos = null;
		
		if(dhInicio != null && dhFim != null) {
			Integer qtHorasPeriodo = 
					Integer.valueOf(parametroGeralService.findByNomeParametro("PERIODO_ESTADIA").getDsConteudo());						
			
			qtMinutos = Minutes.minutesBetween(dhInicio, dhFim).getMinutes();
			
			qtPeriodos = BigDecimal.valueOf(qtMinutos) 
					.divide(new BigDecimal(60), 0, BigDecimal.ROUND_UP)
					.divide(BigDecimal.valueOf(qtHorasPeriodo), 0, BigDecimal.ROUND_UP)
					.intValue();					
		}		
		
		return qtPeriodos;
	}
	
	private BigDecimal executeCalculoEstadia(BigDecimal vlTabela, Integer qtDias) {
		if(vlTabela == null || qtDias == null) {
			throw new IllegalArgumentException();
		}
		
		return getValorArredondado(vlTabela.multiply(new BigDecimal(qtDias)));
	}
	
	private BigDecimal executeCalculoCapatazia(BigDecimal vlTabela, BigDecimal vlCusto) {	
		if(vlTabela == null || vlCusto == null) {
			throw new IllegalArgumentException();
		}
		
		return getValorArredondado(vlCusto.add(getValorPercentual(vlCusto, vlTabela)));
	}
	
	private BigDecimal executeCalculoOperacoesLogisticas(BigDecimal vlNegociado) {
		if(vlNegociado == null) {
			throw new IllegalArgumentException();
		}
		
		return getValorArredondado(vlNegociado);
	}
	
	private BigDecimal getMax(BigDecimal valor, BigDecimal valorMinimo) {
		if(valorMinimo == null) {
			return valor;
		}
		return valor.max(valorMinimo);
	}
	
	private BigDecimal getValorPercentual(BigDecimal valor, BigDecimal percentual) {
		if(valor == null) {
			return null;
		}
		return getValorArredondado(valor.multiply(percentual).divide(HUNDRED));
	}

	private BigDecimal getValorArredondado(BigDecimal valor) {
		if(valor == null) {
			return null;
		}
		return valor.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public ServicoAdicionalCalculo getByCdParcela(String cdParcela, List<ServicoAdicionalCalculo> servicos) {
		ServicoAdicionalCalculo filter = new ServicoAdicionalCalculo();
		filter.setCdParcela(cdParcela);
		
		int index = servicos.indexOf(filter);
		if(index < 0) {
			return null;
		}
		
		return servicos.get(index);
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
		this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
}
