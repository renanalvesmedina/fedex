package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.ComposicaoPagamentoDto;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.RedecoBaixadoDto;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.service.CotacaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.ComposicaoPagamentoRedeco;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.dao.RedecoDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarBaixaRedecoService"
 */
public class GerarBaixaRedecoService extends CrudService {
	
	private static final Set<String> FINALIDADES_QUE_NAO_LIQUIDAM = 
			new HashSet<String>(Arrays.asList(new String[]{ "PR", "DR", "JU", "OR" }));
	
	private RedecoService redecoService;
	
	private GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService;
	
	private GerarLiquidacaoReciboService gerarLiquidacaoReciboService;
	
	private GerarEncerramentoCobrancaService gerarEncerramentoCobrancaService;
	
	private FaturaService faturaService;
	
	private DomainValueService domainValueService;

	private CotacaoMoedaService cotacaoMoedaService;
	
	private BaixarDocumentoDivergenciaCorporativoLMSService baixDocumentoDivergenciaCorporativoLMSService;
	
	private FilialService filialService;
	
	private ParametroGeralService parametroGeralService;
	
	private ComposicaoPagamentoRedecoService composicaoPagamentoRedecoService;
	
	private BoletoService boletoService;
	
	private IntegracaoJmsService integracaoJmsService;
	
	/**
	 * Liquida os itens do redeco e gera as rela�oes de cobran�a do redeco informado.
	 * Devolve o caminho do relat�rio de rela��o de cobran�a caso for redeco nacional
	 * 
	 * Altera��es em 05/03/2010 por Christian S. Perone:
	 * 		Este m�todo foi sobrecarregado para acomodar altera��es
	 * 		referentes aos casos de uso [RE-00] e [RE-01]. Esta sobrecarga
	 * 		foi necess�ria para n�o impactar nos sistemas j� utilizando
	 * 		esta assinatura de m�todo.
	 * 
	 * @author Micka�l Jalbert / Christian S. Perone
	 * @since 14/07/2006
	 * @param Long idRedeco
	 * @return Boolean
	 */
	public Boolean executeBaixaRedeco(Redeco redeco, ItemList items){
		return executeBaixaRedeco(redeco, items, null);
	}
		
	
	/**
	 * Sobrecarga de m�todo para acomodar as altera��es necess�rias
	 * referentes aos casos de uso [RE-00] e [RE-01]. Um novo par�metro
	 * foi inclu�do para receber o mapeamento entre o n�mero de rela��o
	 * cobran�a e a filial da fatura.
	 * 
	 * @param redeco Redeco
	 * @param items ItemList
	 * @param mapRelacaoFilial mapeamento entre nrRelacaoCobrancaFilial e
	 * filial fatura
	 * @since 05/03/2010
	 * @return Boolean
	 */
	public Boolean executeBaixaRedeco(Redeco redeco, ItemList items, Map<Long, Long> mapRelacaoFilial) {
		Boolean retorno = null;
	    if (!SessionUtils.isIntegrationRunning()){
	    	validateRedeco(redeco);
	    }
		updateRedeco(redeco);
		

		Map<String, Object> findSomatorio = redecoService.findSomatoriosRedeco(redeco.getIdRedeco());

		BigDecimal vlLiquido = ((BigDecimal) findSomatorio.get("vl_total_comp_pagto")).setScale(2);
		BigDecimal vlTotalReceb = ((BigDecimal) findSomatorio.get("vl_total_recebido")).setScale(2);

		BigDecimal vlTotalFat = ((BigDecimal) findSomatorio.get("vl_total_fat")).setScale(2);
		BigDecimal vlTotalDesc = ((BigDecimal) findSomatorio.get("vl_total_desc")).setScale(2);
		BigDecimal vlTotalRecebParcial = ((BigDecimal) findSomatorio.get("vl_total_receb_parcial")).setScale(2);
		
		BigDecimal vlTotalTarifa = ((BigDecimal) findSomatorio.get("vl_total_tarifa")).setScale(2);

		BigDecimal vlSaldoTotal =  vlTotalFat.subtract(vlTotalDesc).subtract(vlTotalRecebParcial) ;
    	
		
		redecoService.updateLancamentoFranquiaBaixaRedeco(redeco.getIdRedeco(), redeco.getFilial().getIdFilial(), redeco.getNrRedeco(), redeco.getDtLiquidacao());

    	if (!FINALIDADES_QUE_NAO_LIQUIDAM.contains(redeco.getTpFinalidade().getValue())) {
    		redecoService.updateLancamentoBaixaParcialRedeco(redeco.getIdRedeco(), redeco.getFilial().getIdFilial(), 
    				redeco.getNrRedeco(), redeco.getDtLiquidacao());

	    	if(CompareUtils.lt(vlLiquido, vlTotalReceb)){
	    		executeBaixasParciais(redeco);
	    	} else {
	    		executeBaixasTotais(redeco);
	    	}
    	} else {
    		executeBaixaOutrasFinalidades(redeco);
    	}
    	
		JmsMessageSender jmsMessageSender = 
				integracaoJmsService.createMessage(Queues.FINANCEIRO_REDECOS_BAIXADOS);

		jmsMessageSender.addMsg(createRedecoBaixadoDto(redeco, vlLiquido, vlTotalFat, vlTotalReceb));

		integracaoJmsService.storeMessage(jmsMessageSender);

		return retorno;		
	}

	private RedecoBaixadoDto createRedecoBaixadoDto(Redeco redeco, BigDecimal vLiquido, BigDecimal vlTotalFat, BigDecimal vlTotalReceb) {
		Long idRedeco = redeco.getIdRedeco();
		String siglaFilial = redeco.getFilial().getSgFilial();
		Long idFilial = redeco.getFilial().getIdFilial();
		Long numeroRedeco = redeco.getNrRedeco();
		String tipoFinalidade = redeco.getTpFinalidade().getValue();
		String descricaoFinalidade = redeco.getTpFinalidade().getDescriptionAsString();
		Boolean isBaixaParcial = CompareUtils.lt(vLiquido, vlTotalReceb);
		List<FaturaDMN> faturas = createFaturasDto(redeco, vLiquido, vlTotalReceb, vlTotalFat);
		List<ComposicaoPagamentoDto> composicoesPagamento = createComposicoesPagamentoDto(redeco);
		
		return new RedecoBaixadoDto(idRedeco, siglaFilial, numeroRedeco, tipoFinalidade, 
				descricaoFinalidade, isBaixaParcial, faturas, composicoesPagamento, idFilial);
	}
	
	private List<ComposicaoPagamentoDto> createComposicoesPagamentoDto(Redeco redeco) {
    	List<ComposicaoPagamentoRedeco> composicoes = composicaoPagamentoRedecoService.findByIdRedeco(redeco.getIdRedeco());
    	List<ComposicaoPagamentoDto> composicoesPagamentoDto = new ArrayList<ComposicaoPagamentoDto>();
    	
    	for (ComposicaoPagamentoRedeco composicao : composicoes) {
    		composicoesPagamentoDto.add(createComposicaoPagamento(composicao));
		}

		return composicoesPagamentoDto;
	}

	private ComposicaoPagamentoDto createComposicaoPagamento(ComposicaoPagamentoRedeco composicao) {
		Long idComposicaoPagamentoRedeco = composicao.getIdComposicaoPagamentoRedeco();
		String tipoComposicaoPagamentoRedeco = composicao.getTpComposicaoPagamentoRedeco().getValue();
		YearMonthDay dataPagamento = composicao.getDtPagamento();
		BigDecimal valorPagamento = composicao.getVlPagamento();
		Long idBanco = composicao.getBanco() == null ? null : composicao.getBanco().getIdBanco();
		Short numeroBanco = composicao.getBanco() == null ? null : composicao.getBanco().getNrBanco();
		String observacaoComposicaoPagamentoRedeco = composicao.getObComposicaoPagamentoRedeco();
		Integer numeroParcelasFranqueado = composicao.getNumeroDeParcelas();
		Long idCreditoBancario = composicao.getCreditoBancario() == null ? null : composicao.getCreditoBancario().getIdCreditoBancario();
		String tipoOrigem = composicao.getCreditoBancario() == null ? null : composicao.getCreditoBancario().getTpOrigem().getValue();
		Long idFilial = composicao.getFilial().getIdFilial();
		String siglaFilial = composicao.getFilial().getSgFilial();
		
		return new ComposicaoPagamentoDto(idComposicaoPagamentoRedeco, tipoComposicaoPagamentoRedeco, dataPagamento, 
				valorPagamento, idBanco, numeroBanco, observacaoComposicaoPagamentoRedeco, numeroParcelasFranqueado, 
				idCreditoBancario, tipoOrigem, idFilial, siglaFilial);
	}


	private List<FaturaDMN> createFaturasDto(Redeco redeco, BigDecimal vlLiquido, BigDecimal vlTotalReceb, BigDecimal vlTotalFat) {
		List<FaturaDMN> faturas = new ArrayList<FaturaDMN>();
		
		List lstItemRedeco = redecoService.findItemRedeco(redeco.getIdRedeco());
		List<Map<String, Object>> tpDoctoServicos = redecoService.findTpDoctoServicoFaturaRedeco(redeco.getIdRedeco());
		for (Iterator iter = lstItemRedeco.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			if (itemRedeco.getFatura() != null){
				faturas.add(createFaturaDto(itemRedeco.getFatura(), vlLiquido, vlTotalReceb, itemRedeco.getVlJuros(), tpDoctoServicos, redeco, vlTotalFat, itemRedeco.getVlTarifa()));
			}
		}
		return faturas;
	}


	private FaturaDMN createFaturaDto(Fatura fatura, BigDecimal vlLiquido, BigDecimal vlTotalReceb, 
			BigDecimal vlJuros, List<Map<String, Object>> tpDoctoServicos, Redeco redeco, BigDecimal vlTotalFat, BigDecimal vlTarifa) {
		
		Long idFatura = fatura.getIdFatura();
		String siglaFilial = fatura.getFilialByIdFilial().getSgFilial();
		Long numeroFatura = fatura.getNrFatura();
		YearMonthDay dataEmissao = fatura.getDtEmissao();
		YearMonthDay dataVencimento = fatura.getDtVencimento();
		YearMonthDay dataLiquidacao = redeco.getDtLiquidacao();
		BigDecimal valorDesconto = vlLiquido.equals(vlTotalReceb) ? fatura.getVlDesconto() : new BigDecimal(0.00, MathContext.DECIMAL64);
		BigDecimal totalVlPagamento = redecoService.findVlPagtoFatura(fatura.getIdFatura(), redeco.getIdRedeco());
		BigDecimal saldoFatura = fatura.getVlTotal().subtract(fatura.getVlDesconto()).subtract(totalVlPagamento);
		BigDecimal valorTotal = extractValorTotal(saldoFatura, vlLiquido, vlTotalReceb, valorDesconto, redeco, vlTotalFat, fatura.getVlTotal(), vlJuros);
		BigDecimal valorJuroRecebido = vlJuros;
		Integer quantidadeDocumentos = fatura.getQtDocumentos();
		String tipoSituacaoFatura = fatura.getTpSituacaoFatura().getValue();
		String tipoDocumentoServico = getTipoDoctoServico(fatura.getIdFatura(), tpDoctoServicos);
		
		
		FaturaDMN f = new FaturaDMN(idFatura, 
				siglaFilial, numeroFatura, dataEmissao, dataVencimento, dataLiquidacao, 
				valorTotal, valorDesconto, quantidadeDocumentos, tipoSituacaoFatura, tipoDocumentoServico, null, valorJuroRecebido, vlTarifa);
		
		Long idFilial = fatura.getFilialByIdFilial().getIdFilial();
		String nrIdentificacaoFilial = fatura.getFilialByIdFilial().getPessoa().getNrIdentificacao();
		String nmPessoaFilial = fatura.getFilialByIdFilial().getPessoa().getNmPessoa();
		
		f.setIdFilial(idFilial);
		f.setNrIdentificacaoFilial(nrIdentificacaoFilial);
		f.setNmPessoaFilial(nmPessoaFilial);
		
		return f;
	}
	
	private String getTipoDoctoServico(Long idFatura, List<Map<String, Object>> tpDoctoServicos) {
		for(Map<String, Object> tpDoctoServico : tpDoctoServicos) {
			Long idFaturaCurrent = (Long)tpDoctoServico.get("id_fatura");
			
			if(idFatura.equals(idFaturaCurrent)) {
				return (String) tpDoctoServico.get("tp_documento_servico");
			}
		}
		
		throw new IllegalStateException("Couldn't find tp_docto_servico for fatura: " + idFatura);
	}


	private BigDecimal extractValorTotal(BigDecimal saldoFatura, BigDecimal vlLiquido, BigDecimal vlSaldoTotal, BigDecimal valorDesconto, Redeco redeco, BigDecimal vlTotalFat, BigDecimal vlFatura, BigDecimal vlJuros) {
		String tpFinalidade = redeco.getTpFinalidade().getValue();
		
		if (FINALIDADES_QUE_NAO_LIQUIDAM.contains(tpFinalidade)) {
			return vlFatura.multiply(vlLiquido.divide(vlTotalFat, 10, RoundingMode.HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		return vlLiquido.equals(vlSaldoTotal) ? saldoFatura.add(vlJuros).add(valorDesconto) : vlLiquido;  
		}
		
	private void executeBaixasParciais(Redeco redeco) {
		List lstItemRedeco = redecoService.findItemRedeco(redeco.getIdRedeco());
		
		List<Boleto> boletosToPersist = new ArrayList<Boleto>();
		List<Fatura> faturasToPersist = new ArrayList<Fatura>();
		
		for (Iterator iter = lstItemRedeco.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			Fatura fatura = itemRedeco.getFatura();
			
			Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
			
			if(boleto != null) {
				fatura.setTpSituacaoFatura(new DomainValue("BL"));
				boleto.setTpSituacaoBoleto(boleto.getTpSituacaoAntBoleto());
				boletosToPersist.add(boleto);
			} else {
				fatura.setTpSituacaoFatura(new DomainValue("EM"));
			}
			faturasToPersist.add(fatura);
		}
		
		faturaService.storeAll(faturasToPersist);
		boletoService.storeAll(boletosToPersist);
	}


	private void executeBaixaOutrasFinalidades(Redeco redeco) {
		List lstItemRedeco = redecoService.findItemRedeco(redeco.getIdRedeco());
		
		List<Boleto> boletosToPersist = new ArrayList<Boleto>();
		List<Fatura> faturasToPersist = new ArrayList<Fatura>();
		
		for (Iterator iter = lstItemRedeco.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			Fatura fatura = itemRedeco.getFatura();
			
			if(fatura.getDtLiquidacao() != null) {
				fatura.setTpSituacaoFatura(new DomainValue("LI"));
				
				Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
				
				if(boleto != null) {
					boleto.setTpSituacaoBoleto(new DomainValue("LI"));
					boletosToPersist.add(boleto);
				}
				
				faturasToPersist.add(fatura);
			} else {
				Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
				
				if(boleto != null) {
					fatura.setTpSituacaoFatura(new DomainValue("BL"));
					boleto.setTpSituacaoBoleto(boleto.getTpSituacaoAntBoleto());
					boletosToPersist.add(boleto);
				} else {
					fatura.setTpSituacaoFatura(new DomainValue("EM"));
				}
				faturasToPersist.add(fatura);

			}
		}
		
		boletoService.storeAll(boletosToPersist);
		faturaService.storeAll(faturasToPersist);
	}


	private void executeBaixasTotais(Redeco redeco) {
		List lstItemRedeco = redecoService.findItemRedeco(redeco.getIdRedeco());
		List lstItemRedecoRelacao = new ArrayList();
		
		List<Fatura> faturasParaSeremLiquidadas = new ArrayList<Fatura>();
		List<Recibo> recibosParaSeremLiquidados = new ArrayList<Recibo>();
		
		//Por cada item de redeco
		for (Iterator iter = lstItemRedeco.iterator(); iter.hasNext();) {
			ItemRedeco itemRedeco = (ItemRedeco) iter.next();
			
			//Se e uma fatura, liquidar a fatura
			if (itemRedeco.getFatura() != null){
				
				//S� os itemRedeco que tem fatura(documentos nacionais) podem ter relacaoCobranca
				lstItemRedecoRelacao.add(itemRedeco);
				
				Fatura fatura = itemRedeco.getFatura();
				BigDecimal vlCotacao = null;
				
				if (fatura.getVlCotacaoMoeda() != null) 
					vlCotacao = fatura.getVlCotacaoMoeda();
				else if (fatura.getCotacaoMoeda() != null)
					vlCotacao = fatura.getCotacaoMoeda().getVlCotacaoMoeda();
				
				
				if (vlCotacao != null && !vlCotacao.equals(BigDecimal.ZERO)){
					BigDecimal vlDevido = fatura.getVlTotal().divide(vlCotacao, 2, RoundingMode.HALF_UP);
					vlDevido = fatura.getVlTotal().subtract(cotacaoMoedaService.findVlCotacaoByMoedaPais(fatura.getCotacaoMoeda().getMoedaPais().getIdMoedaPais(), redeco.getDtLiquidacao()).multiply(vlDevido));
					
					itemRedeco.setVlDiferencaCambialCotacao(vlDevido);
				}
				
				itemRedeco.getFatura().setVlJuroRecebido(itemRedeco.getVlJuros());
				
				//////////gerarLiquidacaoFaturaService.executeLiquidarFatura(itemRedeco.getFatura(),redeco.getDtLiquidacao());
				faturasParaSeremLiquidadas.add(itemRedeco.getFatura());
				
			//Se � um recibo liquidar o recibo e todas as fatura ligadas a ela
			} else {
				recibosParaSeremLiquidados.add(itemRedeco.getRecibo());
				//gerarLiquidacaoReciboService.executeLiquidarFatura(itemRedeco.getRecibo(),redeco.getDtLiquidacao());
			}
		}

		if(!faturasParaSeremLiquidadas.isEmpty()) {
			gerarLiquidacaoFaturaService.executeLiquidarFaturas(faturasParaSeremLiquidadas, redeco.getDtLiquidacao(), redeco.getIdRedeco());	
		}
		
		if (!recibosParaSeremLiquidados.isEmpty()) {
			gerarLiquidacaoReciboService.executeLiquidarFaturas(recibosParaSeremLiquidados, redeco.getDtLiquidacao());	
		}
		
		
		gerarEncerramentoCobrancaService.executeEncerrarCobranca();
		//retorno = generateRelacaoCobranca(lstItemRedecoRelacao, redeco, mapRelacaoFilial);
	}
	
	/**
	 * Valida se o redeco pode ser baixado
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Redeco redeco
	 */
	private void validateRedeco(Redeco redeco) {
		//Se o redeco for diferente de 'Digitado' e 'Emitido', lancar uma exception
		if ((!redeco.getTpSituacaoRedeco().getValue().equals("DI") && !redeco.getTpSituacaoRedeco().getValue().equals("EM")) || (SessionUtils.isIntegrationRunning() && !redeco.getTpSituacaoRedeco().getValue().equals("LI"))){
			throw new BusinessException("LMS-36160");
		}
		
		//Se o redeco esta 'Em aprovacao' no workflow, lancar uma exception
		if (redeco.getTpSituacaoWorkflow() != null && redeco.getTpSituacaoWorkflow().getValue().equals("E")){
			throw new BusinessException("LMS-36155");
		}
		
		//Se a filial da sessao nao eh matriz, lancar uma exception
		if (!SessionUtils.isFilialSessaoMatriz()){
			throw new BusinessException("LMS-36152");
		}
		
		validateDataLiquidacao(redeco);
		
		
		if ("N".equals(redeco.getBlDigitacaoConcluida().getValue())) {
			throw new BusinessException("LMS-36344");
		}
		if ("CF".equals(redeco.getTpFinalidade().getValue())) {
			throw new BusinessException("LMS-36345");
		}
		
		
	}

	private void validateDataLiquidacao(Redeco redeco) {
		String paramCompetencia = parametroGeralService.findSimpleConteudoByNomeParametro("COMPETENCIA_FINANCEIRO");
		String fechamento = String.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("IND_FECHTO_MENSAL_FINANCEIRO"));
		YearMonthDay competencia = JTDateTimeUtils.convertDataStringToYearMonthDay(paramCompetencia, "yyyy-MM-dd");
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

		if (((dtAtual.getMonthOfYear() != competencia.getMonthOfYear()) || dtAtual.getYear() != competencia.getYear()) && "N".equals(fechamento)) {
			if (JTDateTimeUtils.comparaData(redeco.getDtLiquidacao(), competencia) < 0 || JTDateTimeUtils.comparaData(redeco.getDtLiquidacao(), dtAtual) > 0) {
				throw new BusinessException("LMS-36346");
			}
		} else {
			YearMonthDay primeiroDiaAtual = JTDateTimeUtils.getFirstDayOfYearMonthDay(dtAtual);

			if (JTDateTimeUtils.comparaData(redeco.getDtLiquidacao(), primeiroDiaAtual) < 0 || JTDateTimeUtils.comparaData(redeco.getDtLiquidacao(), dtAtual) > 0) {
				throw new BusinessException("LMS-36346");
			}
		}
		
		if(redeco.getDtLiquidacao().isBefore(redeco.getDtRecebimento())){
			throw new BusinessException("LMS-36169"); 
		}
	}


	/**
	 * Atualiza o redeco
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Redeco redeco
	 */
	private void updateRedeco(Redeco redeco) {
		redeco.setTpSituacaoRedeco(domainValueService.findDomainValueByValue("DM_STATUS_REDECO","LI"));
		redecoService.storeBasic(redeco);
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	public void setGerarLiquidacaoFaturaService(
			GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService) {
		this.gerarLiquidacaoFaturaService = gerarLiquidacaoFaturaService;
	}

	public void setGerarLiquidacaoReciboService(
			GerarLiquidacaoReciboService gerarLiquidacaoReciboService) {
		this.gerarLiquidacaoReciboService = gerarLiquidacaoReciboService;
	}

	public void setGerarEncerracaoCobrancaService(
			GerarEncerramentoCobrancaService gerarEncerracaoCobrancaService) {
		this.gerarEncerramentoCobrancaService = gerarEncerracaoCobrancaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public void setRedecoDao(RedecoDAO dao) {
		setDao( dao );
	}

	public void setCotacaoMoedaService(CotacaoMoedaService cotacaoMoedaService) {
		this.cotacaoMoedaService = cotacaoMoedaService;
	}


	public BaixarDocumentoDivergenciaCorporativoLMSService getBaixDocumentoDivergenciaCorporativoLMSService() {
		return baixDocumentoDivergenciaCorporativoLMSService;
}


	public void setBaixDocumentoDivergenciaCorporativoLMSService(
			BaixarDocumentoDivergenciaCorporativoLMSService baixDocumentoDivergenciaCorporativoLMSService) {
		this.baixDocumentoDivergenciaCorporativoLMSService = baixDocumentoDivergenciaCorporativoLMSService;
	}


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setComposicaoPagamentoRedecoService(
			ComposicaoPagamentoRedecoService composicaoPagamentoRedecoService) {
		this.composicaoPagamentoRedecoService = composicaoPagamentoRedecoService;
	}
	
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
