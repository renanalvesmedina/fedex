package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.JobInterfaceService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaClienteDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ReajusteCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.service.HistoricoReajusteClienteService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.ReajusteClienteService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.tabelaprecos.reajusteTabelaClienteService"
 */
@Assynchronous(name = "ReajusteTabelaClienteService" )
public class ReajusteTabelaClienteService {

	private static final String TP_VALOR = "V";
	private static final String TP_PERCENTUAL = "P";
	private static final String TP_FRACAO = "F";
	static final String TP_QUILO = "Q";
	static final String TP_FAIXA = "X";
	
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ParametroClienteService parametroClienteService;
	private ReajusteTabelaClienteDAO reajusteTabelaClienteDAO;
	private JobInterfaceService jobService;
	private ReajusteClienteService reajusteClienteService;
	private HistoricoReajusteClienteService historicoReajusteClienteService;
	private GrupoRegiaoService grupoRegiaoService;

	/**
	 * Rotina de Reajuste dos Parametros do Cliente via Agendamento.
	 * 
	 * @author Andre Valadas
	 * @param idTabelaPreco
	 */
	public void executeAgendamentoReajusteCliente(Long idTabelaPreco, YearMonthDay dtVigenciaInicial) {
		//parametros para a service
		Map parameters = new HashMap();
		parameters.put("idTabelaPreco", idTabelaPreco);

		//calculo da data
		DateTime proximaExecucao = JTDateTimeUtils.yearMonthDayToDateTime(dtVigenciaInicial);

		String cronExpression = String.format("0 %d %d %d %d ? %d",
								proximaExecucao.getMinuteOfHour(),
								proximaExecucao.getHourOfDay(),
								proximaExecucao.getDayOfMonth(),
								proximaExecucao.getMonthOfYear(),
								proximaExecucao.getYear());

		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		jobService.schedule("tabelaprecos.SimularNovosPrecos",
								"Reajuste de tabela",
								cronExpression,
								new Serializable[]{ idTabelaPreco },
								usuarioLogado.getLogin(),
								Collections.emptySet());
	}

	/**
	 * Rotina chamada pelo Quartz para execução de tarefas agendadas
	 * @param criteria
	 */
	@AssynchronousMethod(name="tabelaprecos.SimularNovosPrecos",
							type=BatchType.BIZZ_BATCH_SERVICE)// AGENDADO VIA REGRA DE NEGOCIO
	public void executeJobReajusteCliente(Long idTabelaPreco) {
		reajusteTabelaClienteDAO.executeJobReajusteCliente(idTabelaPreco);
	}

	/**
	 * 01.01.04.01 Gerar reajuste do cliente
	 * Agendamento da execução do job vendas.efetuarReajusteCliente
	 * 
	 * @param idReajusteCliente
	 */
	public void executeAgendamentoJobEfetuarReajusteCliente(Long idReajusteCliente, YearMonthDay dtVigenciaInicial) {
		
		executeJobEfetuarReajusteCliente(idReajusteCliente);
	}


	/**
	 * 01.01.04.01 Gerar reajuste do cliente
	 * Job vendas.efetuarReajusteCliente 
	 * 
	 * @param idReajusteCliente
	 */
	@AssynchronousMethod(name="vendas.efetuarReajusteCliente",
			type=BatchType.BIZZ_BATCH_SERVICE)// AGENDADO VIA REGRA DE NEGOCIO
	public void executeJobEfetuarReajusteCliente(Long idReajusteCliente) {
		this.executeEfetuarReajusteCliente(idReajusteCliente);
	}


	/**
	 * 01.01.04.01 Gerar reajuste do cliente#efetuarReajusteCliente
	 * 
	 * Rotina de Reajuste da Tabela e Parametros do Cliente.
	 * @author Andre Valadas
	 * @param idReajusteCliente
	 */
	public ReajusteCliente executeEfetuarReajusteCliente(Long idReajusteCliente) {

		setDadosSessaoBanco(SessionUtils.getUsuarioLogado(), SessionUtils.getFilialSessao(), SessionUtils.getPaisSessao());
		
		ReajusteCliente reajusteCliente = reajusteClienteService.findById(idReajusteCliente);

		/** Nova Tabela de Preco */
		TabelaPreco tabelaPreco = reajusteCliente.getTabelaPreco();
		/** Parametros pela Tabela Divisa Cliente Original */
		TabelaDivisaoCliente tabelaDivisaoCliente = reajusteCliente.getTabelaDivisaoCliente();
		Long idTabelaPrecoAnterior = tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco();


		Boolean blGerarSomenteMarcados = reajusteCliente.getBlGerarSomenteMarcados();
		/** 1. Atualiza TABELA_DIVISAO_CLIENTE */
		BigDecimal pcReajuste = reajusteCliente.getPcReajusteAcordado();
		
		/** LMS - 5219
		 * Se o checkbox “Considerar somente parcelas selecionadas” estiver
		 * marcado, não deverão ser executados os itens “a”, “b” e “c” listados
		 * abaixo e será efetuado somente o reajuste das parcelas percentuais,
		 * contemplado no item 4.
		 */		
		if(!Boolean.TRUE.equals(blGerarSomenteMarcados)) {
			tabelaDivisaoCliente.setPcAumento(pcReajuste);
			tabelaDivisaoCliente.setTabelaPreco(tabelaPreco);
			//*** STORE TABELA_DIVISAO_CLIENTE
			tabelaDivisaoClienteService.store(tabelaDivisaoCliente);

			/** 2. Reajusta Servicos Adicionais de SERVICO_ADICIONAL_CLIENTE */
			List<ServicoAdicionalCliente> servicoAdicionalClientes = tabelaDivisaoCliente.getServicoAdicionalClientes();
			if (servicoAdicionalClientes != null) {
				for (ServicoAdicionalCliente servicoAdicionalCliente : servicoAdicionalClientes) {
					//*** Caso tpIndicador eh valor, reajustar o Valor
					if (TP_VALOR.equals(servicoAdicionalCliente.getTpIndicador().getValue())) {
						BigDecimal vlValor = BigDecimalUtils.acrescimo(servicoAdicionalCliente.getVlValor(), pcReajuste);
						servicoAdicionalCliente.setVlValor(vlValor);
					}
					BigDecimal vlMinimo = servicoAdicionalCliente.getVlMinimo();
					if (vlMinimo != null) {
						servicoAdicionalCliente.setVlMinimo(BigDecimalUtils.acrescimo(vlMinimo, pcReajuste));
					}
					//*** STORE SERV_ADIC_CLIENTE
					servicoAdicionalClienteService.store(servicoAdicionalCliente);
				}
			}
		}

		/**
		 * Gera Historido referente ao Reajuste
		 * #Quest 11147
		 */
		historicoReajusteClienteService.generateHistoricoReajusteCliente(
				tabelaDivisaoCliente.getIdTabelaDivisaoCliente()
				,idTabelaPrecoAnterior
				,tabelaPreco.getIdTabelaPreco()
				,pcReajuste
				,"M");

		/**
		 * c) Atualizar os dados da tabela REAJUSTE_CLIENTE -
		 * DT_INICIO_VIGENCIA = data do sistema - BL_EFETIVADO = ‘S’
		 */
		reajusteCliente.setDtInicioVigencia(JTDateTimeUtils.getDataAtual());
		reajusteCliente.setBlEfetivado(Boolean.TRUE);

		parametroClienteService.removeParametroClienteDataSuperiorReajuste(tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
		
		/** 
		 * 3. Reajusta Parametros de PARAMETRO_CLIENTE
		 *  -  Data eh passada para nao reajustar Parametros VENCIDOS. 
		 */
		List<ParametroCliente> parametroClientes = parametroClienteService.findByIdTabelaDivisaoCliente(tabelaDivisaoCliente.getIdTabelaDivisaoCliente(), new DomainValue("A"), reajusteCliente.getDtInicioVigencia());
		if (parametroClientes == null) {
			return reajusteCliente;
		}
		for (ParametroCliente parametroCliente : parametroClientes) {
			/** 4. Criar Novo Parametro; */
			ParametroCliente parametroClienteNovo = ParametroClienteUtils.getParametroClientePadrao();
			//*** Copia valores para nova referencia.
			ParametroClienteUtils.copyParametroClienteCompleto(parametroCliente, parametroClienteNovo);

			//LMS-3626
			Long idTabelaPreco = tabelaPreco.getIdTabelaPreco();
			GrupoRegiao grupoRegiao = new GrupoRegiao();
			if(parametroCliente.getGrupoRegiaoOrigem()!= null) {
				grupoRegiao = grupoRegiaoService.findByGrupoTabela(parametroCliente.getGrupoRegiaoOrigem().getIdGrupoRegiao(), idTabelaPreco);
				parametroClienteNovo.setGrupoRegiaoOrigem(grupoRegiao);
			}

			if(parametroCliente.getGrupoRegiaoDestino()!= null) {
				grupoRegiao = grupoRegiaoService.findByGrupoTabela(parametroCliente.getGrupoRegiaoDestino().getIdGrupoRegiao(), idTabelaPreco);
				parametroClienteNovo.setGrupoRegiaoDestino(grupoRegiao);
			}
			//
			
			//*** Reajusta Novo Parametro
			this.reajustarParametros(parametroClienteNovo, pcReajuste, reajusteCliente);

			//*** Altera propriedades da nova referencia.
			parametroClienteNovo.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			//*** STORE PARAMETRO_CLIENTE NOVO
			parametroClienteService.storeBasic(parametroClienteNovo);

			/** Altera propriedades da referencia original. */
			parametroCliente.setDtVigenciaFinal(JTDateTimeUtils.getDataAtual().minusDays(1));

			//*** STORE PARAMETRO_CLIENTE ORIGINAL
			parametroClienteService.storeBasic(parametroCliente);
		}
		
		/** Chama STORE efetivando o ReajusteCliente */
		reajusteCliente.setBlEfetivado(Boolean.TRUE);
		reajusteClienteService.store(reajusteCliente);
		
		return reajusteCliente;
	}

	private BigDecimal calculaReajusteParametro(BigDecimal valueParam, BigDecimal pcReajuste) {
		return BigDecimalUtils.acrescimo(valueParam, pcReajuste);
	}
	
	/**
	 * 4. Reajusta Valor dos Parametros/Taxas/Generalidades conforme Percentual de reajuste.
	 * @param parametroCliente
	 * @param pcReajuste
	 */
	private void reajustarParametros(ParametroCliente parametroCliente, BigDecimal pcReajuste, ReajusteCliente reajusteCliente) {
		/*
		 * VL_PERCENTUAL_GRIS = Valor original SE o campo “% GRIS”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_PERC_GRIS) estiver marcado, então
		 * VL_PERCENTUAL_GRIS = VL_PERCENTUAL_GRIS + (VL_PERCENTUAL_GRIS *
		 * PC_AUMENTO/100) Caso contrário VL_PERCENTUAL_GRIS = Valor
		 * original
		 */
		if (reajusteCliente.getBlReajustaPercGris() != null
				&& reajusteCliente.getBlReajustaPercGris()) {
			parametroCliente.setVlPercentualGris(calculaReajusteParametro(
					parametroCliente.getVlPercentualGris(), pcReajuste));
		}

		/*
		 * VL_ADVALOREM = Valor original SE o campo “Ad valorem 1”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_ADVALOREM) estiver marcado, então
		 * VL_ADVALOREM = VL_ADVALOREM + (VL_ADVALOREM * PC_AUMENTO/100) Caso
		 * contrário VL_ADVALOREM = Valor original
		 */
		if (reajusteCliente.getBlReajustaAdValorEm() != null
				&& reajusteCliente.getBlReajustaAdValorEm()) {
			parametroCliente.setVlAdvalorem(calculaReajusteParametro(
					parametroCliente.getVlAdvalorem(), pcReajuste));
		}

		/*
		 * VL_ADVALOREM_2 = Valor original SE o campo “Ad valorem 2”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_ADVALOREM_2) estiver marcado, então
		 * VL_ADVALOREM_2 = VL_ADVALOREM_2 + (VL_ADVALOREM_2 * PC_AUMENTO/100)
		 * Caso contrário VL_ADVALOREM_2 = Valor original
		 */
		if (reajusteCliente.getBlReajustaAdValorEm2() != null
				&& reajusteCliente.getBlReajustaAdValorEm2()) {
			parametroCliente.setVlAdvalorem2(calculaReajusteParametro(
					parametroCliente.getVlAdvalorem2(), pcReajuste));
		}

		/*
		 * PC_FRETE_PERCENTUAL = Valor original SE o campo “% frete percentual”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_FRETE_PERCENTUAL) estiver marcado,
		 * então PC_FRETE_PERCENTUAL = PC_FRETE_PERCENTUAL +
		 * (PC_FRETE_PERCENTUAL * PC_AUMENTO/100) Caso contrário
		 * PC_FRETE_PERCENTUAL = Valor original
		 */
		if (reajusteCliente.getBlReajustaFretePercentual() != null
				&& reajusteCliente.getBlReajustaFretePercentual()) {
			parametroCliente.setPcFretePercentual(calculaReajusteParametro(
					parametroCliente.getPcFretePercentual(), pcReajuste));
		}

		/*
		 * VL_PERCENTUAL_TDE = Valor original SE o campo “% TDE”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_PERC_TDE) estiver marcado, então
		 * VL_PERCENTUAL_TDE = VL_PERCENTUAL_TDE + (VL_PERCENTUAL_TDE *
		 * PC_AUMENTO/100) Caso contrário VL_PERCENTUAL_TDE = Valor original
		 */
		if (reajusteCliente.getBlReajustaPercTde() != null
				&& reajusteCliente.getBlReajustaPercTde()) {
			parametroCliente.setVlPercentualTde(calculaReajusteParametro(
					parametroCliente.getVlPercentualTde(), pcReajuste));
		}
		
		/*
		 * VL_PERCENTUAL_TRT = Valor original SE o campo “% TRT”
		 * (REAJUSTE_CLIENTE.BL_REAJUSTA_PERC_TRT) estiver marcado, então
		 * VL_PERCENTUAL_TRT = VL_PERCENTUAL_TRT + (VL_PERCENTUAL_TRT *
		 * PC_AUMENTO/100) Caso contrário VL_PERCENTUAL_TRT = Valor original
		 */
		if (reajusteCliente.getBlReajustaPercTrt() != null
				&& reajusteCliente.getBlReajustaPercTrt()) {
			parametroCliente.setVlPercentualTrt(calculaReajusteParametro(
					parametroCliente.getVlPercentualTrt(), pcReajuste));
		}
		
		
		/*
		 * Se o checkbox “Considerar somente parcelas selecionadas” estiver
		 * marcado, o procedimento abaixo deverá ser executado somente para as
		 * parcelas percentuais que estiverem marcadas. As parcelas percentuais
		 * são referenciadas pelos campos: “% TDE”, “% TRT”, “% GRIS”, “Ad
		 * valorem”, “Ad valorem 1”, “Ad valorem 2”, “% frete percentual”
		 */		
		if (!Boolean.TRUE.equals(reajusteCliente.getBlGerarSomenteMarcados())) {
		/** MINIMO_GRIS */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorMinimoGris().getValue())) {
			parametroCliente.setVlMinimoGris(BigDecimalUtils.acrescimo(parametroCliente.getVlMinimoGris(), pcReajuste));
			parametroCliente.setPcReajMinimoGris(pcReajuste);
		}
		/** MINIMO_TRT */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorMinimoTrt().getValue())) {
			parametroCliente.setVlMinimoTrt(BigDecimalUtils.acrescimo(parametroCliente.getVlMinimoTrt(), pcReajuste));
			parametroCliente.setPcReajMinimoTrt(pcReajuste);
		} else parametroCliente.setPcReajMinimoTrt(BigDecimal.ZERO);
		/** MINIMO_TDE */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorMinimoTde().getValue())) {
			parametroCliente.setVlMinimoTde(BigDecimalUtils.acrescimo(parametroCliente.getVlMinimoTde(), pcReajuste));
			parametroCliente.setPcReajMinimoTde(pcReajuste);
		}
		/** PEDAGIO */
		String tpIndicadorPedagio = parametroCliente.getTpIndicadorPedagio().getValue();
		if (TP_VALOR.equals(tpIndicadorPedagio)
			|| TP_PERCENTUAL.equals(tpIndicadorPedagio)
			|| TP_FRACAO.equals(tpIndicadorPedagio)
			|| TP_QUILO.equals(tpIndicadorPedagio)
			|| TP_FAIXA.equals(tpIndicadorPedagio)) {
			parametroCliente.setVlPedagio(BigDecimalUtils.acrescimo(parametroCliente.getVlPedagio(), pcReajuste));
			parametroCliente.setPcReajPedagio(pcReajuste);
		}
		/** MINIMO_FRETE_PESO */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorMinFretePeso().getValue())) {
			parametroCliente.setVlMinFretePeso(BigDecimalUtils.acrescimo(parametroCliente.getVlMinFretePeso(), pcReajuste));
		}
		/** FRETE_PESO */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorFretePeso().getValue())) {
			parametroCliente.setVlFretePeso(BigDecimalUtils.acrescimo(parametroCliente.getVlFretePeso(), pcReajuste));
			parametroCliente.setPcReajFretePeso(pcReajuste);
		}
		/** ADVALOREM */
		parametroCliente.setPcReajAdvalorem(BigDecimalUtils.ZERO);
		/** ADVALOREM2 */
		parametroCliente.setPcReajAdvalorem2(BigDecimalUtils.ZERO);

		/** VALOR_REFERENCIA */
		if (TP_VALOR.equals(parametroCliente.getTpIndicadorValorReferencia().getValue())) {
			parametroCliente.setVlValorReferencia(BigDecimalUtils.acrescimo(parametroCliente.getVlValorReferencia(), pcReajuste));
		}
		/** MINIMO_FRETE_QUILO */
		BigDecimal vlMinimoFreteQuilo = parametroCliente.getVlMinimoFreteQuilo();
		if (vlMinimoFreteQuilo != null) {
			parametroCliente.setVlMinimoFreteQuilo(BigDecimalUtils.acrescimo(vlMinimoFreteQuilo, pcReajuste));
		}
		parametroCliente.setPcReajVlMinimoFreteQuilo(pcReajuste);

		/** MINIMO_FRETE_PERCENTUAL */
		BigDecimal vlMinimoFretePercentual = parametroCliente.getVlMinimoFretePercentual();
		if (vlMinimoFretePercentual != null) {
			parametroCliente.setVlMinimoFretePercentual(BigDecimalUtils.acrescimo(vlMinimoFretePercentual, pcReajuste));
		}
		parametroCliente.setPcReajVlMinimoFretePercen(pcReajuste);

		/** TONELADA_FRETE_PERCENTUAL */
		BigDecimal vlToneladaFretePercentual = parametroCliente.getVlToneladaFretePercentual();
		if (vlToneladaFretePercentual != null) {
			parametroCliente.setVlToneladaFretePercentual(BigDecimalUtils.acrescimo(vlToneladaFretePercentual, pcReajuste));
		}
		parametroCliente.setPcReajVlToneladaFretePerc(pcReajuste);

		/** TABELA_ESPECIFICA */
		if (TP_VALOR.equals(parametroCliente.getTpIndicVlrTblEspecifica().getValue())) {
			parametroCliente.setVlTblEspecifica(BigDecimalUtils.acrescimo(parametroCliente.getVlTblEspecifica(), pcReajuste));
			parametroCliente.setPcReajVlTarifaEspecifica(pcReajuste);
		}
		/** FRETE_VOLUME */
		BigDecimal vlFreteVolume = parametroCliente.getVlFreteVolume();
		if (vlFreteVolume != null) {
			parametroCliente.setVlFreteVolume(BigDecimalUtils.acrescimo(vlFreteVolume, pcReajuste));			
		}
		parametroCliente.setPcReajVlFreteVolume(pcReajuste);

		/** TARIFA_MINIMA */
		if (TP_VALOR.equals(parametroCliente.getTpTarifaMinima().getValue())) {
			parametroCliente.setVlTarifaMinima(BigDecimalUtils.acrescimo(parametroCliente.getVlTarifaMinima(), pcReajuste));
			parametroCliente.setPcReajTarifaMinima(pcReajuste);
		}
		}
		
		/** SIMULACAO/COTACAO */
		parametroCliente.setSimulacao(null);
		parametroCliente.setCotacao(null);

		if (!Boolean.TRUE.equals(reajusteCliente.getBlGerarSomenteMarcados())) {
		/** 6. Reajustar Taxas */
		this.reajustarTaxas(parametroCliente, pcReajuste);
		/** 7. Reajustar Generalidades */
		this.reajustarGeneralidades(parametroCliente, pcReajuste);
	}

	}

	/**
	 * 5. Reajusta Valor dos Parametros de TAXA_CLIENTE conforme Percentual de reajuste.
	 * @param parametroCliente
	 * @param pcReajuste
	 */
	private void reajustarTaxas(ParametroCliente parametroCliente, BigDecimal pcReajuste) {
		List<TaxaCliente> taxaClientes = parametroCliente.getTaxaClientes();
		if (taxaClientes == null) {
			return;
		}

		for (TaxaCliente taxaCliente : taxaClientes) {
			/** TAXA */
			if (TP_VALOR.equals(taxaCliente.getTpTaxaIndicador().getValue())) {
				taxaCliente.setVlTaxa(BigDecimalUtils.acrescimo(taxaCliente.getVlTaxa(), pcReajuste));
				taxaCliente.setPcReajTaxa(pcReajuste);
			}
			/** EXCEDENTE */
			BigDecimal vlExcedente = taxaCliente.getVlExcedente();
			if (vlExcedente != null) {
				taxaCliente.setVlExcedente(BigDecimalUtils.acrescimo(vlExcedente, pcReajuste));
			}
			taxaCliente.setPcReajVlExcedente(pcReajuste);

			//*** Se nao for Taxa do Tipo VALOR, seta NULL
			if (!TP_VALOR.equals(taxaCliente.getTpTaxaIndicador().getValue())) {
				taxaCliente.setPcReajTaxa(null);
			}
		}
	}

	/**
	 * 6. Reajusta Valor dos Parametros de GENERALIDADE_CLIENTE conforme Percentual de reajuste.
	 * @param parametroCliente
	 * @param pcReajuste
	 */
	private void reajustarGeneralidades(ParametroCliente parametroCliente, BigDecimal pcReajuste) {
		List<GeneralidadeCliente> generalidadeClientes = parametroCliente.getGeneralidadeClientes();
		if (generalidadeClientes == null) {
			return;
		}

		for (GeneralidadeCliente generalidadeCliente : generalidadeClientes) {
			/** GENERALIDADE */
			if (TP_VALOR.equals(generalidadeCliente.getTpIndicador().getValue())) {
				generalidadeCliente.setVlGeneralidade(BigDecimalUtils.acrescimo(generalidadeCliente.getVlGeneralidade(), pcReajuste));
				generalidadeCliente.setPcReajGeneralidade(pcReajuste);
			}

			//*** Se nao for Taxa do Tipo VALOR, seta NULL
			if (!TP_VALOR.equals(generalidadeCliente.getTpIndicador().getValue())) {
				generalidadeCliente.setPcReajGeneralidade(null);
			}
			
			if (generalidadeCliente.getTpIndicadorMinimo()!=null){
				if (generalidadeCliente.getTpIndicadorMinimo().getValue().equals(TP_VALOR)){
					generalidadeCliente.setVlMinimo(BigDecimalUtils.acrescimo(generalidadeCliente.getVlMinimo(), pcReajuste));
					generalidadeCliente.setPcReajMinimo(pcReajuste);				
				}else{
					generalidadeCliente.setPcReajMinimo(new BigDecimal(0));
				}
			}else{
				generalidadeCliente.setTpIndicadorMinimo(new DomainValue("T"));
				generalidadeCliente.setPcReajMinimo(new BigDecimal(0));
				generalidadeCliente.setVlMinimo(new BigDecimal(0));
			}
			
			if (generalidadeCliente.getVlMinimo()==null){
				generalidadeCliente.setVlMinimo(new BigDecimal(0));
			}
		}
	}
	
	public void setDadosSessaoBanco(Usuario usuario, Filial filial, Pais pais) {

		String dadosSessao = "f>".concat(filial.getIdFilial().toString()).concat("|u>".concat(usuario.getIdUsuario().toString())).concat("|login>".concat(usuario.getLogin()))
		.concat("|to>-03|tl>1|i>0|");
		
		reajusteTabelaClienteDAO.setDadosSessaoBanco( dadosSessao );
		
	}
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
	}
	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setReajusteTabelaClienteDAO(ReajusteTabelaClienteDAO reajusteTabelaClienteDAO) {
		this.reajusteTabelaClienteDAO = reajusteTabelaClienteDAO;
	}
	public void setJobService(JobInterfaceService jobService) {
		this.jobService = jobService;
	}
	public void setReajusteClienteService(ReajusteClienteService reajusteClienteService) {
		this.reajusteClienteService = reajusteClienteService;
	}
	public void setHistoricoReajusteClienteService(HistoricoReajusteClienteService historicoReajusteClienteService) {
		this.historicoReajusteClienteService = historicoReajusteClienteService;
	}
	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}
	
}