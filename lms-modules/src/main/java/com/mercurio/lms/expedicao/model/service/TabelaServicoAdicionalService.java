package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.TipoParcela;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;

/**
 * Classe responsável por gerar a "tabela de preços de serviços" adicionais, utilizada para 
 * calcular os valores dos serviços adicionais.  
 * @since 03/04/2014 - Projeto Serviços Adicionais
 */
public class TabelaServicoAdicionalService {
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ServicoAdicionalService servicoAdicionalService;
	private ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService;	
	private TabelaPrecoService tabelaPrecoService;
	
	
	public List<ServicoAdicionalPrecificado> findByTabelaSimulacao(Long idServico, Long idSimulacao) {		
		TabelaPreco tabelaSimulacao = null;
		TabelaPreco tabelaPadrao = tabelaPrecoService.findTabelaVigente(
				ConstantesTabelaPrecos.TP_TABELA_PRECO_REFERENCIAL_TNT, 
				ConstantesTabelaPrecos.SUB_TP_TABELA_PRECO_REFERENCIAL_TNT);	
						
		if(idServico == null) {
			idServico = Long.valueOf(
					parametroGeralService.findByNomeParametro("IDServicoPadraoConhecimento").getDsConteudo());
		}
		
		if(idSimulacao != null ) {			
			tabelaSimulacao = tabelaPrecoService.findTabelaSimulacao(idSimulacao);
		} 
		
		if(idServico != null) {			
			TabelaPreco tabelaX = tabelaPrecoService.findTabelaXVigenteByServico(idServico);
			if(tabelaX != null) {
				tabelaPadrao = tabelaX;
			}
		}
				
		List<ServicoAdicionalPrecificado> servicosRetorno = new ArrayList<ServicoAdicionalPrecificado>();
		List<ServicoAdicionalPrecificado> servicosTabelaPadrao = findParcelas(tabelaPadrao.getIdTabelaPreco());
		List<ServicoAdicionalPrecificado> servicosTabelaCliente = null;		
		
		if(tabelaSimulacao != null && !tabelaSimulacao.getIdTabelaPreco().equals(tabelaPadrao.getIdTabelaPreco())) {
			servicosTabelaCliente = findParcelas(tabelaSimulacao.getIdTabelaPreco());
		}
		
		/* Verifica se existe precificação para o serviço na tabela do cliente */
		for(ServicoAdicionalPrecificado servicoTabPadrao : servicosTabelaPadrao) {
			ServicoAdicionalPrecificado servicoRetorno = servicoTabPadrao;
						
			if(servicosTabelaCliente != null && servicosTabelaCliente.size() > 0) {
				int index = servicosTabelaCliente.indexOf(servicoTabPadrao);
				if(index >= 0) {
					servicoRetorno = servicosTabelaCliente.get(index);
				} 
			}
			
			servicosRetorno.add(servicoRetorno);
		}
		
		if(tabelaSimulacao != null) {
			executeParametrizacaoSimulacao(servicosRetorno, tabelaSimulacao.getIdTabelaPreco(), idSimulacao);
		}			
		
		executeVincularServicoAdicionalComplementar(servicosRetorno);
		executeOrderByDsParcela(servicosRetorno);
		
		return servicosRetorno;
	}
	
	
	/**
	 * Busca os serviços adicionais da tabela de preço referente ao serviço informado e a 
	 * divisão do cliente, substituindo pelos serviços adicionais da tabela do cliente (quando houver) 
	 * e aplicando as parametrizações do cliente (desconto, acréscimo).	
	 * @param idServico Caso null, usa a tabela vigente de tipo T e subtipo X.
	 * @param idDivisaoCliente Caso null, usa a tabela vigente de subtipo X para o serviço informado. 
	 * @return Lista de serviços adicionais precificados.
	 * @see ServicoAdicionalPrecificado 
	 */
	public List<ServicoAdicionalPrecificado> findByTabelaCliente(Long idServico, Long idDivisaoCliente) {
		
		TabelaPreco tabelaCliente = null;
		TabelaPreco tabelaPadrao = tabelaPrecoService.findTabelaVigente(ConstantesTabelaPrecos.TP_TABELA_PRECO_REFERENCIAL_TNT, ConstantesTabelaPrecos.SUB_TP_TABELA_PRECO_REFERENCIAL_TNT);

		if (idServico == null) {
			idServico = Long.valueOf(parametroGeralService.findByNomeParametro("IDServicoPadraoConhecimento").getDsConteudo());
		}
		
		if (idServico != null && idDivisaoCliente != null) {
			tabelaCliente = tabelaPrecoService.findTabelaCliente(idDivisaoCliente, idServico);
		}

		if (idServico != null) {
			TabelaPreco tabelaX = tabelaPrecoService.findTabelaXVigenteByServico(idServico);
			if (tabelaX != null) {
				tabelaPadrao = tabelaX;
			}
		}

		List<ServicoAdicionalPrecificado> servicosRetorno = new ArrayList<ServicoAdicionalPrecificado>();
		List<ServicoAdicionalPrecificado> servicosTabelaPadrao = findParcelas(tabelaPadrao.getIdTabelaPreco());
		List<ServicoAdicionalPrecificado> servicosTabelaCliente = null;

		if (tabelaCliente != null && !tabelaCliente.getIdTabelaPreco().equals(tabelaPadrao.getIdTabelaPreco())) {
			servicosTabelaCliente = findParcelas(tabelaCliente.getIdTabelaPreco());
		}

		/* Verifica se existe precificação para o serviço na tabela do cliente */
		for (ServicoAdicionalPrecificado servicoTabPadrao : servicosTabelaPadrao) {
			ServicoAdicionalPrecificado servicoRetorno = servicoTabPadrao;

			if (servicosTabelaCliente != null && servicosTabelaCliente.size() > 0) {
				int index = servicosTabelaCliente.indexOf(servicoTabPadrao);
				if (index >= 0) {
					servicoRetorno = servicosTabelaCliente.get(index);
				}
			}

			servicosRetorno.add(servicoRetorno);
		}

		if (tabelaCliente != null) {
			executeParametrizacaoCliente(servicosRetorno, tabelaCliente.getIdTabelaPreco(), idDivisaoCliente);
		}

		executeVincularServicoAdicionalComplementar(servicosRetorno);
		executeOrderByDsParcela(servicosRetorno);

		return servicosRetorno;
	}
	
	
	public List<ServicoAdicionalPrecificado> findByTabelaPadraoByIdServico(Long idServico) {
		
		TabelaPreco tabelaPadrao = tabelaPrecoService.findTabelaVigente(ConstantesTabelaPrecos.TP_TABELA_PRECO_REFERENCIAL_TNT, ConstantesTabelaPrecos.SUB_TP_TABELA_PRECO_REFERENCIAL_TNT);

		if (idServico == null) {
			idServico = Long.valueOf(parametroGeralService.findByNomeParametro("IDServicoPadraoConhecimento").getDsConteudo());
		}

		if (idServico != null) {
			TabelaPreco tabelaX = tabelaPrecoService.findTabelaXVigenteByServico(idServico);
			if (tabelaX != null) {
				tabelaPadrao = tabelaX;
			}
		}

		List<ServicoAdicionalPrecificado> servicosTabelaPadrao = findParcelas(tabelaPadrao.getIdTabelaPreco());


		executeVincularServicoAdicionalComplementar(servicosTabelaPadrao);
		executeOrderByDsParcela(servicosTabelaPadrao);

		return servicosTabelaPadrao;
	}
	
	/**
	 * Busca os serviços adicionais da tabela de preço referente ao serviço definido 
	 * pelo parâmetro geral IDServicoPadraoConhecimento, substituindo pelos serviços adicionais 
	 * da tabela do cliente (quando houver) e aplicando as parametrizações do cliente (desconto, acréscimo).	
	 * @param idDivisaoCliente Caso null, usa a tabela vigente tipo X e 
	 * serviço definido pelo parâmetro geral IDServicoPadraoConhecimento.
	 * @return Lista de serviços adicionais precificados.
	 * @see ServicoAdicionalPrecificado
	 */
	public List<ServicoAdicionalPrecificado> findByTabelaCliente(Long idDivisaoCliente) {		
		return findByTabelaCliente(null, idDivisaoCliente);
	}
	
	/**
	 * Busca os serviços adicionais da tabela de preço referente ao serviço informado 
	 * e de tipo T, M ou A e subtipo X.
	 * @param idServico Caso null, usa a tabela vigente tipo T subtipo X.
	 * @return Lista de serviços adicionais precificados.
	 * @see ServicoAdicionalPrecificado 
	 */
	public List<ServicoAdicionalPrecificado> findByTabelaPadrao(Long idServico) {		
		return findByTabelaCliente(idServico, null);
	}
	
	/**
	 * Busca os serviços adicionais da tabela de preço vigente do tipo T e subtipo X. 
	 * @return Lista de serviços adicionais precificados.
	 * @see ServicoAdicionalPrecificado 
	 */
	public List<ServicoAdicionalPrecificado> findByTabelaPadrao() {
		return findByTabelaPadrao(null);
	}
	
	
	public ServicoAdicionalPrecificado getByCdParcela(String cdParcela, List<ServicoAdicionalPrecificado> servicos) {
		ServicoAdicionalPrecificado filter = new ServicoAdicionalPrecificado();
		filter.setCdParcela(cdParcela);
		
		int index = servicos.indexOf(filter);
		if(index < 0) {
			return null;
		}
		
		return servicos.get(index);
	}

	private void executeParametrizacaoCliente(List<ServicoAdicionalPrecificado> servicos, Long idTabelaPreco, Long idDivisaoCliente) {
		List<ServicoAdicionalCliente> parametrosCliente = servicoAdicionalClienteService
				.findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente);
		
		/* Aplica os descontos / acréscimos conforme negociação (ServicoAdicionalCliente) */
		if(parametrosCliente != null && parametrosCliente.size() > 0) {
			for(ServicoAdicionalCliente sac : parametrosCliente) {								
				ServicoAdicionalPrecificado servicoRetorno = 
						getByCdParcela(sac.getParcelaPreco().getCdParcelaPreco(), servicos);
								
				if(servicoRetorno != null) {					
					servicoRetorno.setTpIndicadorParametrizacao(sac.getTpIndicador().getValue());
					servicoRetorno.setVlParametrizacao(sac.getVlValor());
					servicoRetorno.setVlMinimoParametrizacao(sac.getVlMinimo());
					if(sac.getNrDecursoPrazo() != null) {
						servicoRetorno.setQtDiasDecurso(sac.getNrDecursoPrazo());
					}
					if(sac.getNrQuantidadeDias() != null) {
						servicoRetorno.setQtDiasCarencia(sac.getNrQuantidadeDias());
					}
				}
			}	
		}
	}	
	
	private void executeParametrizacaoSimulacao(List<ServicoAdicionalPrecificado> servicos, Long idTabelaPreco, Long idSimulacao) {
		List<ServicoAdicionalCliente> parametrosCliente = servicoAdicionalClienteService.findByTabelaSimulacaoCliente(idSimulacao);
		
		/* Aplica os descontos / acréscimos conforme negociação (ServicoAdicionalCliente) */
		if(parametrosCliente != null && parametrosCliente.size() > 0) {
			for(ServicoAdicionalCliente sac : parametrosCliente) {								
				ServicoAdicionalPrecificado servicoRetorno = 
						getByCdParcela(sac.getParcelaPreco().getCdParcelaPreco(), servicos);
								
				if(servicoRetorno != null) {					
					servicoRetorno.setTpIndicadorParametrizacao(sac.getTpIndicador().getValue());
					servicoRetorno.setVlParametrizacao(sac.getVlValor());
					servicoRetorno.setVlMinimoParametrizacao(sac.getVlMinimo());
					if(sac.getNrDecursoPrazo() != null) {
						servicoRetorno.setQtDiasDecurso(sac.getNrDecursoPrazo());
					}
					if(sac.getNrQuantidadeDias() != null) {
						servicoRetorno.setQtDiasCarencia(sac.getNrQuantidadeDias());
					}
				}
			}	
		}
	}	

	private List<ServicoAdicionalPrecificado> findParcelas(Long idTabelaPreco) {
		List<TabelaPrecoParcela> parcelas = 
				servicoAdicionalService.findServicosAdicionaisByTabelaPreco(idTabelaPreco);
		
		List<ServicoAdicionalPrecificado> servicos = new ArrayList<ServicoAdicionalPrecificado>();
		
		Integer qtDiasDecurso = Integer.valueOf(parametroGeralService.findByNomeParametro("DEC_PRAZO", false).getDsConteudo());
		Integer qtDiasCarenciaArm = Integer.valueOf(parametroGeralService.findByNomeParametro("CARENCIA_ARM", false).getDsConteudo());
		Integer qtDiasCarenciaDep = Integer.valueOf(parametroGeralService.findByNomeParametro("CARENCIA_DEP", false).getDsConteudo());
		
		for(TabelaPrecoParcela parcela : parcelas) {
			ParcelaPreco parcelaPreco = parcela.getParcelaPreco();
		 	ValorServicoAdicional valorServicoAdicional = parcela.getValorServicoAdicional();
			TabelaPreco tabelaPreco = parcela.getTabelaPreco(); 
		 			
			ServicoAdicionalPrecificado servico = new ServicoAdicionalPrecificado();			
			servico.setIdParcelaPreco(parcelaPreco.getIdParcelaPreco());
			servico.setDsParcela(parcelaPreco.getDsParcelaPreco().getValue(LocaleContextHolder.getLocale()));
			servico.setCdParcela(parcelaPreco.getCdParcelaPreco());
			servico.setTpIndicadorCalculo(parcelaPreco.getTpIndicadorCalculo().getValue());
			if(valorServicoAdicional != null) {
				servico.setVlTabela(valorServicoAdicional.getVlServico());
				servico.setVlMinimoTabela(valorServicoAdicional.getVlMinimo());
			}else {
				System.out.println(parcelaPreco.getCdParcelaPreco());
			}
			servico.setMoeda(tabelaPreco.getMoeda());
						
			if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(parcelaPreco.getCdParcelaPreco())) {
				servico.setQtDiasCarencia(qtDiasCarenciaArm);
				servico.setQtDiasDecurso(qtDiasDecurso);
			}else if(ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(parcelaPreco.getCdParcelaPreco())){
				servico.setQtDiasCarencia(qtDiasCarenciaDep);
			}
			
			servicos.add(servico);
		}
		
		return servicos;
	}
	
	private void executeVincularServicoAdicionalComplementar(List<ServicoAdicionalPrecificado> servicos) {
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_KM);
		
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_TOCO, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_TOCO_KM);
		
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_VAN, ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_VAN_KM);
		
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_ESCOLTA, ConstantesExpedicao.CD_SEGURANCA_ADICIONAL);
		
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_ARMAZENAGEM, ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA);
		
		executeVincularServicoAdicionalComplementar(
				servicos, ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO, ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA);
	}
	
	private void executeOrderByDsParcela(List<ServicoAdicionalPrecificado> servicos) {
		Collections.sort(servicos, new Comparator<ServicoAdicionalPrecificado>() {
			@Override
			public int compare(ServicoAdicionalPrecificado o1, ServicoAdicionalPrecificado o2) {					
				String s1 = o1.getDsParcela().toLowerCase();
				String s2 = o2.getDsParcela().toLowerCase();
				return s1.compareTo(s2);
			}					
		});
	}
	
	private void executeVincularServicoAdicionalComplementar(List<ServicoAdicionalPrecificado> servicos, String cdParcelaPreco, String cdParcelaPrecoComplementar) {				
		ServicoAdicionalPrecificado servico = getByCdParcela(cdParcelaPreco, servicos);
		ServicoAdicionalPrecificado servicoComplementar = getByCdParcela(cdParcelaPrecoComplementar, servicos);
		
		if(servico != null && servicoComplementar != null) {			
			servico.setServicoAdicionalComplementar(servicoComplementar);
			servicoComplementar.setIsServicoComplementar(Boolean.TRUE);			
		}			
	}
	
	/**
	 * Aplica as regras de formatação para exibição dos valores de serviços adicionais utilizando 
	 * o locale do usuário logado.
	 * @param servico
	 * @param blConcatenaSgMoeda Indica se deve ser concatenado a propriedade sgMoeda da Classe Moeda 
	 * nas informações referentes a valor do serviço e valor mínimo.
	 * Ex.: BRL R$ 1,00 caso TRUE e R$ 1,00 caso FALSE.
	 * @return Valor do serviço adicional formatado. Caso não tenha regra para formatação, retorna null.
	 */
	public String findDsFormatada(ServicoAdicionalPrecificado servico, Boolean blConcatenaSgMoeda) {
		Locale locale = LocaleContextHolder.getLocale();
		return findDsFormatada(servico, blConcatenaSgMoeda, locale);
	}
	
	/**
	 * Aplica as regras de formatação para exibição dos valores de serviços adicionais.
	 * @param servico
	 * @param blConcatenaSgMoeda Indica se deve ser concatenado a propriedade sgMoeda da Classe Moeda
	 * @param locale Locale utilizado para formatação dos valores e dos strings internacionalizáveis
	 * nas informações referentes a valor do serviço e valor mínimo.
	 * Ex.: BRL R$ 1,00 caso TRUE e R$ 1,00 caso FALSE.
	 * @return Valor do serviço adicional formatado. Caso não tenha regra para formatação, retorna null.
	 */
	public String findDsFormatada(ServicoAdicionalPrecificado servico, Boolean blConcatenaSgMoeda, Locale locale) {
		String cdParcelaPreco = servico.getCdParcela();
		String dsMoeda = servico.getMoeda().getDsSimbolo();
		if(blConcatenaSgMoeda) {
			dsMoeda = servico.getMoeda().getSgMoeda() + " " + dsMoeda;
		}		
		
		if (ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)) {
			return findDsFormatadaArmazenagem(servico, dsMoeda, locale);
		}else if(ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcelaPreco)){
			return findDsFormatadaDepositario(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_PALETIZACAO.equals(cdParcelaPreco)) {
			return findDsFormatadaPalete(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_TOCO.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO_VAN.equals(cdParcelaPreco)) {
			return findDsFormatadaVeiculoDedicado(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)) {
			return findDsFormatadaEscolta(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_SEGURANCA_ADICIONAL.equals(cdParcelaPreco)) {
			return findDsFormatadaSergurancaAdicional(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_ESTADIA_VEICULO.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ESTADIA_TRUCK.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ESTADIA_CONJUNTO.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ESTADIA_34.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_ESTADIA_CARRETA.equals(cdParcelaPreco)) {
			return findDsFormatadaEstadia(servico, dsMoeda, locale);
		} else if (ConstantesExpedicao.CD_CAPATAZIA.equals(cdParcelaPreco)) {
			return findDsFormatadaCapatazia(servico, locale);
		} else if(ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA.equals(cdParcelaPreco)) {
			return findDsFormatadaPermanencia(servico, locale);			
		} else if (TipoParcela.PERCENTUAL_SOBRE_MERCADORIA.value().getValue().equals(servico.getTpIndicadorCalculo())) {			
			return findDsFormatadaPercentualSobreMercadoria(servico, dsMoeda, locale);					
		} else if (TipoParcela.PERCENTUAL_SOBRE_FRETE.value().getValue().equals(servico.getTpIndicadorCalculo())) {			
			return findDsFormatadaPercentualSobreFrete(servico, dsMoeda, locale);
		}
		
		return null;
	}
	
	private String findDsFormatadaDepositario(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("porKgDia");
		if (servico.getVlMinimoServico() != null) {
			complemento += " " + configuracoesFacade.getMensagem("comMinimoDe");
			complemento += " " + dsMoeda + " " + servico.getVlMinimoServicoFormatado("#,##0.00", locale);
		}
		
		String[] param = new String[2];
		param[0] = String.valueOf(servico.getQtDiasCarencia());
		
		complemento += " " + configuracoesFacade.getMensagem("carenciaDiasDepositario", param);		
		return complemento;		
	}


	private String findDsFormatadaArmazenagem(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("porKgDia");
		if (servico.getVlMinimoServico() != null) {
			complemento += " " + configuracoesFacade.getMensagem("comMinimoDe");
			complemento += " " + dsMoeda + " " + servico.getVlMinimoServicoFormatado("#,##0.00", locale);
		}
		
		String[] param = new String[2];
		param[0] = String.valueOf(servico.getQtDiasCarencia());
		param[1] = String.valueOf(servico.getQtDiasDecurso());		
		
		complemento += " " + configuracoesFacade.getMensagem("carenciaDiasDecurso", param);		
		return complemento;		
	}	
	
	private String findDsFormatadaPalete(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("relPorPallet") + "."; 
		return complemento;
	}
	
	private String findDsFormatadaVeiculoDedicado(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("porEntrega");
		if(servico.getServicoAdicionalComplementar() != null) {
			complemento += " - ";
			complemento += dsMoeda + " " + servico.getServicoAdicionalComplementar().getVlServicoFormatado("#,##0.00", locale) + " ";
			complemento += configuracoesFacade.getMensagem("porKmExcedente100");
		}		
		return complemento + ".";			
	}
		
	private String findDsFormatadaEscolta(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("relEscolta") + "."; 
		return complemento;
	}
	
	private String findDsFormatadaSergurancaAdicional(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("relSegurancaAdicional") + "."; 
		return complemento;
	}
	
	private String findDsFormatadaEstadia(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = dsMoeda + " " + servico.getVlServicoFormatado("#,##0.00", locale) + " ";
		complemento += configuracoesFacade.getMensagem("porPeriodo") + "."; 
		return complemento;
	}
	
	private String findDsFormatadaCapatazia(ServicoAdicionalPrecificado servico, Locale locale) {
		String custo = configuracoesFacade.getMensagem("custo");
		String percent = configuracoesFacade.getMensagem("percent");					
		return custo + " + " + servico.getVlServicoFormatado("#,##0.##", locale) + percent + ".";
	}
	
	private String findDsFormatadaPermanencia(ServicoAdicionalPrecificado servico, Locale locale) {
		String complemento = servico.getVlServicoFormatado("#,##0.##", locale);
		complemento += configuracoesFacade.getMensagem("relSobreValorMercadoriaPorPeriodo") + ".";		
		return complemento;
	}
	
	private String findDsFormatadaPercentualSobreMercadoria(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = servico.getVlServicoFormatado("#,##0.##", locale);
		complemento += configuracoesFacade.getMensagem("relSobreValorMercadoria");
		 
		if (servico.getVlMinimoServico() != null) {
			complemento += " " + configuracoesFacade.getMensagem("relComMinimo");
			complemento += " " + dsMoeda + " " + servico.getVlMinimoServicoFormatado("#,##0.00", locale);
		}
		complemento += ".";		
		return complemento;				
	}
	
	private String findDsFormatadaPercentualSobreFrete(ServicoAdicionalPrecificado servico, String dsMoeda, Locale locale) {
		String complemento = servico.getVlServicoFormatado("#,##0.##", locale);
		complemento += configuracoesFacade.getMensagem("relValorFrete");
		 
		if (servico.getVlMinimoServico() != null) {
			complemento += " " + configuracoesFacade.getMensagem("relComMinimo");
			complemento += " " + dsMoeda + " " + servico.getVlMinimoServicoFormatado("#,##0.00", locale);
		}
		complemento += ".";		
		return complemento;				
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setServicoAdicionalClienteService(
			ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public void setServicoAdicionalService(ServicoAdicionalService servicoAdicionalService) {
		this.servicoAdicionalService = servicoAdicionalService;
	}	
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}


	public ServicoGeracaoAutomaticaService getServicoGeracaoAutomaticaService() {
		return servicoGeracaoAutomaticaService;
	}


	public void setServicoGeracaoAutomaticaService(ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService) {
		this.servicoGeracaoAutomaticaService = servicoGeracaoAutomaticaService;
	}
}
