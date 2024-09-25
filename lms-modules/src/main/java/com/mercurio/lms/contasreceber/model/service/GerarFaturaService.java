package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Boleto.SITUACAOBOLETO;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.param.GenerateFaturaParam;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public abstract class GerarFaturaService {
	private FilialService filialService;
	protected FaturaService faturaService;
	protected DevedorDocServFatService devedorDocServFatService;
	private ConhecimentoService conhecimentoService;
	private DataVencimentoService dataVencimentoService;
	protected ConfiguracoesFacade configuracoesFacade;
	protected WorkflowPendenciaService workflowPendenciaService;
	protected BoletoService boletoService;
	private ClienteService clienteService;
	private CalcularValorAtualFaturaService calcularValorAtualFaturaService;
	private DomainValueService domainValueService;
	private QuestionamentoFaturasService questionamentoFaturasService; 
	private DescontoService descontoService;
	private ZerarDescontoService zerarDescontoService;
	private EfetivarDescontoFaturaService efetivarDescontoFaturaService;
	private OcorrenciaBancoService ocorrenciaBancoService;
	private HistoricoBoletoService historicoBoletoService;

	public Fatura executeInicializeDadosFatura(Fatura fatura) {
		fatura = setValorDefault(fatura);

		return fatura;
	}
	
	/**
	 * 
	 * C�DIGO COMUM DO INSERT E DO UPDATE
	 * 
	 */

	protected Fatura beforeInsert(Fatura fatura) {
		fatura = setValorDefault(fatura);

		fatura = setFilial(fatura);

		fatura = setFilialCobranca(fatura);

		faturaService.generateProximoNumero(fatura);

		return fatura;
	}

	protected Fatura beforeStore(Fatura fatura) {
		Fatura faturaAnterior = null;

		if (fatura.getIdFatura() == null) {
			fatura = beforeInsert(fatura);
		} else {
			faturaAnterior = faturaService.findByIdDisconnected(fatura.getIdFatura());
		}		
		
    	//Valida se a filial de cobran�a � centralizada, se � tem que ser igual a filial da sess�o
    	clienteService.validateCobrancaCentralizada(fatura.getCliente());		

    	if (fatura.getQtDocumentos() == null){
    		fatura.setQtDocumentos(Integer.valueOf(0));
    	}
    	
    	if (fatura.getVlTotal() == null){
    		fatura.setVlTotal(new BigDecimal(0));
    	}

		if (fatura.getVlDesconto() == null){
			fatura.setVlDesconto(new BigDecimal(0));
		}
		
		fatura = setDtEmissao(fatura, faturaAnterior);

		fatura = setDtVencimento(fatura);

		fatura = setServico(fatura);

		fatura = setCedente(fatura);

		if(!SessionUtils.isIntegrationRunning()){	

		validateDtVencimento(fatura, faturaAnterior);
		}

		validateCotacao(fatura);		

		faturaService.validateFatura(fatura.getIdFatura());

		return fatura;
	}

	protected Fatura store(Fatura fatura) {
		fatura = beforeStore(fatura);
		faturaService.storeBasic(fatura);
		return fatura;
	}

	protected Fatura afterStore(Fatura fatura, List lstItemFatura, boolean blNovaFatura) {
		fatura = generateQuestionamento(fatura, lstItemFatura);
		
		fatura.setPendencia(generatePendenciaDeCotacao(fatura));
		
    	fatura = calculatVlIva(fatura);
    	
    	fatura = calculateSomatorioFatura(fatura);
    	
		generateBoleto(fatura, blNovaFatura, lstItemFatura);

		validateTipoFrete(fatura, lstItemFatura);

		devedorDocServFatService.executeUpdateSituacaoByIdFatura(fatura.getIdFatura(), "F");

		
		return fatura;
	}

	
	/**
	 * 
	 * @param boleto
	 * @param dhOcorrencia
	 * @param nrOcorrenciaBanco
	 */
    private void storeHistoricoBoleto(Boleto boleto, DateTime dhOcorrencia, Short nrOcorrenciaBancoCancelar, Short nrOcorrenciaBancoIncluir){
    	Long idBanco = boleto.getCedente().getAgenciaBancaria().getBanco().getIdBanco();
    	OcorrenciaBanco ocorrenciaBancoIncluir = ocorrenciaBancoService.findByBancoNrOcorrenciaTpOcorrencia(idBanco, nrOcorrenciaBancoIncluir, "REM");
    	
    	// Cancela os historicosBoleto que sejam da mesma ocorrenciaBanco.
    	historicoBoletoService.cancelaHistoricosComOcorrenciaIguais(boleto, nrOcorrenciaBancoCancelar);
    	
    	HistoricoBoleto historicoBoleto = new HistoricoBoleto();

    	historicoBoleto.setBoleto(boleto);
    	historicoBoleto.setDhOcorrencia(dhOcorrencia);
    	historicoBoleto.setUsuario(SessionUtils.getUsuarioLogado());
    	historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("A"));
    	
    	historicoBoleto.setOcorrenciaBanco(ocorrenciaBancoIncluir);
    	
    	historicoBoletoService.store(historicoBoleto);
    }	
    
    
    /**
     * 
     * @param boleto
     */
    private void retransmiteFatura(Boleto boleto){
    	boolean hasTransmissao = CollectionUtils.exists(boleto.getHistoricoBoletos(), new Predicate() {	
			public boolean evaluate(Object arg0) {
				HistoricoBoleto hb = (HistoricoBoleto) arg0;
				return "REM".equals(hb.getOcorrenciaBanco().getTpOcorrenciaBanco().getValue()) && "A".equals(hb.getTpSituacaoHistoricoBoleto().getValue());
			}
		});
    	
    	//Se o BOLETO.TP_SITUACAO_BOLETO seja (�EM�)
    	if (boleto.getTpSituacaoBoleto().getValue().equals("EM") || !hasTransmissao){
    		historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR, "REM");
    	}		    		
    }
    
	
	
	private Fatura generateQuestionamento(Fatura fatura, List<ItemFatura> lstItemFatura) {
		final BigDecimal valorMinimoDesconto = (BigDecimal)configuracoesFacade.getValorParametro("VL_MINIMO_DOCUMENTO_DESCONTO");
		
		BigDecimal valorDescontos = BigDecimal.ZERO;
		BigDecimal valorDevido = fatura.getVlTotal();
		BigDecimal valorDescontosAprovados = BigDecimal.ZERO;
		
		for(ItemFatura itemFatura: lstItemFatura){
			for (Object oDesconto : itemFatura.getDevedorDocServFat().getDescontos()) {
				Desconto desconto = (Desconto) oDesconto;
				valorDescontos = valorDescontos.add( desconto.getVlDesconto());
				if (desconto.getIdDesconto() != null){
					Desconto descontoAntigo = descontoService.findById(desconto.getIdDesconto());
				if(descontoAntigo != null && descontoAntigo.getTpSituacaoAprovacao() != null && "A".equals(descontoAntigo.getTpSituacaoAprovacao().getValue())){
					valorDescontosAprovados = valorDescontosAprovados.add(descontoAntigo.getVlDesconto());
				}
			}
		}
		}
		
		BigDecimal vlDescontoAnterior = fatura.getVlDesconto();
		
		//se o desconto foi zerado ....
		if(BigDecimal.ZERO.compareTo(valorDescontos) == 0 && BigDecimal.ZERO.compareTo(vlDescontoAnterior) < 0 ){
			zerarDescontoService.executeProcess(fatura.getIdFatura());
			fatura = faturaService.findById(fatura.getIdFatura());
		
		// se o desconto � maior do que o desconto anterior
		}else if ( valorDescontos.compareTo(valorMinimoDesconto) > 0){
			// se o valor de desconto aumentou ou (o  workflows estiver reprovado e a fatura mudou de valor
			if(valorDescontos.compareTo(valorDescontosAprovados) >0 || ((fatura.getTpSituacaoAprovacao() != null && "R".equals(fatura.getTpSituacaoAprovacao().getValue())) && valorDescontos.compareTo(vlDescontoAnterior) != 0 )){
				Long idPendenciaDesconto = null;
					QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
					questionamentoFatura.setBlConcedeAbatimentoSol(true);
					questionamentoFatura.setMotivoDesconto(fatura.getMotivoDesconto());
					questionamentoFatura.setTpSetorCausadorAbatimento(fatura.getTpSetorCausadorAbatimento());
					questionamentoFatura.setVlAbatimentoSolicitado(valorDescontos);
	        		questionamentoFatura.setObAbatimento(fatura.getObFatura());
	        		questionamentoFatura.setObAcaoCorretiva(fatura.getObAcaoCorretiva());
	        		questionamentoFatura.setFatura(fatura);
	        		questionamentoFatura.setTpDocumento(new DomainValue("R"));
	        		questionamentoFatura.setDtVencimentoDocumento(fatura.getDtVencimento());
	        		questionamentoFatura.setDtEmissaoDocumento(fatura.getDtEmissao());
	        		
	        		if(fatura.getBoletos() != null){
	        			questionamentoFatura.setNrBoleto(fatura.getBoleto().getNrBoleto());
	        			questionamentoFatura.setTpSituacaoBoleto(SITUACAOBOLETO.getSituacaoBloquete(fatura.getBoleto().getTpSituacaoBoleto()));
	        		}
	        		
					idPendenciaDesconto = (Long)questionamentoFaturasService.storeGenericQuestionamentoFatura( questionamentoFatura,
							fatura.getFilialByIdFilialCobradora(), 
							fatura.getCliente(), 
							valorDevido,
							fatura.getIdPendenciaDesconto());
	    		fatura.setIdPendenciaDesconto(idPendenciaDesconto);
	    		fatura.setTpSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", "E"));
			}
		} else if (valorDescontos.compareTo(valorMinimoDesconto) <= 0) {
			if( "BL".equals(fatura.getTpSituacaoFatura().getValue())){
				fatura.getBoleto().setVlDesconto(valorDescontos);
		    	if (fatura.getBoleto().getTpSituacaoBoleto().getValue().equals("BN") || fatura.getBoleto().getTpSituacaoBoleto().getValue().equals("BP")){
		    		this.storeHistoricoBoleto(fatura.getBoleto(), JTDateTimeUtils.getDataHoraAtual().plusSeconds(10),Short.valueOf("5"), Short.valueOf("4") );
		    	} else if (fatura.getBoleto().getTpSituacaoBoleto().getValue().equals("EM")){
		    		retransmiteFatura(fatura.getBoleto());
		    	}
		    	efetivarDescontoFaturaService.storeBoleto((Boleto)fatura.getBoleto(), fatura, null, valorDescontos);
	    	}
			fatura.setTpSituacaoAprovacao(new DomainValue("A"));
			fatura.setDhTransmissao(null);
			faturaService.storeBasic(fatura);		
				}				
		
		// Se o valor do desconto apenas diminuiu mas � maior que Zero, atualizar o valor do boleto
		if ( "BL".equals(fatura.getTpSituacaoFatura().getValue()) && vlDescontoAnterior.compareTo(valorDescontos) > 0 && valorDescontos.compareTo(BigDecimal.ZERO) > 0 && valorDescontos.compareTo(valorMinimoDesconto) > 0){	
			fatura.setDhTransmissao(null);
			faturaService.storeBasic(fatura);
			
			// Se a fatura tem boleto e est� aprovada, atualizar o valor do boleto e gerar um historico
			// com ocorr�ncia 4, para reenviar o novo desconto para o banco, j� que neste n�o ser� gerada
			// uma nova pend�ncia.
			if (fatura.getBoleto() != null){
				if (fatura.getIdPendenciaDesconto() != null && "A".equals(fatura.getTpSituacaoAprovacao().getValue())){
					efetivarDescontoFaturaService.storeBoleto((Boleto)fatura.getBoleto(), fatura, fatura.getIdPendenciaDesconto(), valorDescontos);
				} 
			}
		}
		
		/*
		 * situa��o da fatura deve ser a mesma dos descontos.
		 */
		for(ItemFatura itemFatura: lstItemFatura){
			for (Object oDesconto : itemFatura.getDevedorDocServFat().getDescontos()) {
				Desconto desconto = (Desconto) oDesconto;
				if(desconto.getIdPendencia() == null && desconto.getMotivoDesconto() == null ){
					desconto.setTpSituacaoAprovacao(fatura.getTpSituacaoAprovacao());
					desconto.setTpSetorCausadorAbatimento(fatura.getTpSetorCausadorAbatimento());
					desconto.setMotivoDesconto(fatura.getMotivoDesconto());
					desconto.setObAcaoCorretiva(fatura.getObAcaoCorretiva());
				}
				if (desconto.getVlDesconto().compareTo(BigDecimal.ZERO) > 0){
					getDescontoService().storePadrao(desconto);
				} else {
					if (desconto.getIdDesconto() != null){
						getDescontoService().removeById(desconto.getIdDesconto());
					}
				}
			}
		}
		
		fatura.setVlDesconto(valorDescontos);
		
		faturaService.store(fatura);
    	
    	return fatura;
	}

	protected void storeItemFatura(Fatura fatura, List lstItemFatura) {
		storeItemFatura(fatura, lstItemFatura, null, null);
	}

	protected void storeItemFatura(Fatura fatura, List lstItemFatura, List lstItemFaturaNewOrModified, List lstItemFaturaRemoved) {

		if (lstItemFaturaRemoved != null && !lstItemFaturaRemoved.isEmpty()) {
			faturaService.removeItemFatura(lstItemFaturaRemoved);
		}

		if (lstItemFaturaNewOrModified != null && !lstItemFaturaNewOrModified.isEmpty()) {
			faturaService.storeItemFatura(lstItemFaturaNewOrModified);			
		} else {
			faturaService.storeItemFatura(lstItemFatura);			
		}
	}

	/**
	 * 
	 * C�DIGO DO INSERT DE FATURA
	 * 
	 */

	/**
	 * Salva uma fatura com os itens fatura para incluir ou alterar.
	 * 
	 * @author Micka�l Jalbert
	 * @since 09/05/2006
	 * 
	 * @param Fatura fatura
	 * @param List lstItemFatura
	 */
	public Fatura storeFaturaWithItemFatura(Fatura fatura, List lstItemFatura) {
		return storeFaturaWithItemFatura(fatura, lstItemFatura, null, null);
	}

	/**
	 * Salva uma fatura com os itens fatura para incluir ou alterar e os itens
	 * fatura para excluir.
	 * 
	 * @author Micka�l Jalbert
	 * @since 09/05/2006
	 * 
	 * @param Fatura fatura
	 * @param List lstItemFatura
	 * @param List lstItemFaturaRemoved
	 */
	public Fatura storeFaturaWithItemFatura(Fatura fatura, List lstItemFatura,List lstItemFaturaNewOrModified, List lstItemFaturaRemoved) {
		boolean blNovaFatura = (fatura.getIdFatura() == null);
		
		fatura = prepareFaturaWithItemFatura(fatura, lstItemFatura);		
		fatura = store(fatura);
		
		// Tem que existir o m�nimo um item de fatura
		validateExistenciaItemFatura(fatura, lstItemFatura, lstItemFaturaNewOrModified, lstItemFaturaRemoved, blNovaFatura);
		
		storeItemFatura(fatura, lstItemFatura, lstItemFaturaNewOrModified, lstItemFaturaRemoved);
		fatura = afterStore(fatura, lstItemFatura, blNovaFatura);
		return fatura;
	}
	
	/**
	 * Salva os itens fatura com os descontos, � executado s� quando a fatura est� 'Em boleto'
	 * 
	 * @author Micka�l Jalbert
	 * @since 20/11/2006
	 * 
	 * @param Fatura fatura
	 * @param List lstItemFatura
	 */
    public Fatura storeFaturaWithItemFaturaDesconto(Fatura fatura, List lstItemFatura){
    	fatura = generateQuestionamento(fatura, lstItemFatura);

    	// Carrega novamente a fatura, pode ter sido editada na
    	// rotina de zerarDescontos.
    	fatura = faturaService.findById(fatura.getIdFatura());
    	
    	fatura = calculateSomatorioFatura(fatura); 
    	faturaService.storeBasic(fatura);
    	return fatura;
    }	

	public Fatura storeFaturaWithIdsDevedorDocServFat(Fatura fatura, List lstDevedorDocServFat) {
		boolean blNovaFatura = (fatura.getIdFatura() == null);
		
		fatura = prepareFaturaWithDevedorDocServFat(fatura,lstDevedorDocServFat);
		fatura = store(fatura);
		
		// Tem que existir o m�nimo um item de fatura
		validateExistenciaItemFatura(fatura, lstDevedorDocServFat, null, Collections.EMPTY_LIST, blNovaFatura);
		
		List lstItemFatura = storeDevedorDocServFat(fatura,lstDevedorDocServFat);
		storeItemFatura(fatura, lstItemFatura);
		
		fatura.setItemFaturas(lstItemFatura);
		fatura = afterStore(fatura, lstItemFatura, blNovaFatura);

		return fatura;
	}

	protected List storeDevedorDocServFat(Fatura fatura,List lstDevedorDocServFat) {
		List lstItemFatura = new ArrayList();

		for (Iterator iter = lstDevedorDocServFat.iterator(); iter.hasNext();) {
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdInitLazyProperties((Long) iter.next(), false);
			ItemFatura itemFatura = generateItemFatura(fatura,devedorDocServFat);
			lstItemFatura.add(itemFatura);
		}

		return lstItemFatura;
	}

	/**
	 * 
	 * 
	 * M�TODOS ESPECIFICOS
	 * 
	 * 
	 */

	/**
	 * Atribui os valores na fatura que s�o determinadas pelo documento de
	 * servi�o da fatura a partir do primeiro item fatura
	 */
	private Fatura prepareFaturaWithItemFatura(Fatura fatura, List lstItemFatura) {
		if (!lstItemFatura.isEmpty()) {
			ItemFatura itemFatura = (ItemFatura) lstItemFatura.get(0);
			prepareFatura(fatura, itemFatura.getDevedorDocServFat());
		}

		return fatura;
	}

	/**
	 * Atribui os valores na fatura que s�o determinadas pelo documento de
	 * servi�o da fatura a partir do primeiro devedor doc serv fat da lista
	 */
	protected Fatura prepareFaturaWithDevedorDocServFat(Fatura fatura, List lstDevedorDocServFat) {
		if (!lstDevedorDocServFat.isEmpty()) {
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findById((Long) lstDevedorDocServFat.get(0));
			prepareFatura(fatura, devedorDocServFat);
		}

		return fatura;
	}

	/**
	 * Atribui os valores na fatura que s�o determinadas pelo documento de
	 * servi�o da fatura a partir do devedor doc serv fat
	 */
	private Fatura prepareFatura(Fatura fatura, DevedorDocServFat devedorDocServFat) {
		
		//Se a filial da fatura est� nula, setar a filial do devedorDocServFat
		if (fatura.getFilialByIdFilial() == null){
			fatura.setFilialByIdFilial(devedorDocServFat.getFilial());
		}
		
		
		if( fatura.getFilialByIdFilialCobradora() == null ){
			// Caso a fatura tenha itemFatura e a filial do usu�rio seja MTZ, seta a filial do devedor na fatura
			if(SessionUtils.isFilialSessaoMatriz()){
				fatura.setFilialByIdFilialCobradora(devedorDocServFat.getFilial());
			}
		}
		
		if (fatura.getMoeda() == null) {
			fatura.setMoeda(devedorDocServFat.getDoctoServico().getMoeda());
		}
		
		String tpDocumentoServico = devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue();

		if ((tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("NFT") || tpDocumentoServico.equals("NTE") || tpDocumentoServico.equals("CTE")) && 
			(fatura.getTpFrete() == null || (fatura.getTpFrete() != null && StringUtils.isBlank(fatura.getTpFrete().getValue())))) {
			fatura.setTpFrete(conhecimentoService.findTpFreteByIdConhecimento(devedorDocServFat.getDoctoServico().getIdDoctoServico()));
		}		
			
		//Se o tipo de origem � igual a 'Integra��o' ou 'Pre-Fatura', n�o pode validar e montar a fatura
		if (fatura.getTpOrigem() != null && (fatura.getTpOrigem().getValue().equals("I") || fatura.getTpOrigem().getValue().equals("P"))){
			return fatura;
		}
		
		
		boolean separaFaturaModal = devedorDocServFat.getCliente().getBlSeparaFaturaModal();
		
		if (fatura.getTpModal() == null) {
			fatura.setTpModal(devedorDocServFat.getDoctoServico().getServico().getTpModal());
		} else if (!fatura.getTpModal().getValue().equals(devedorDocServFat.getDoctoServico().getServico().getTpModal().getValue()) && separaFaturaModal) {
			// Se o tpModal da fatura � diferente do tpModal do primeiro
			// documento de servi�o
			throw new BusinessException("LMS-36145");
		}
		
		if (fatura.getTpAbrangencia() == null) {
			fatura.setTpAbrangencia(devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia());
		} else if (!fatura.getTpAbrangencia().getValue().equals(devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia().getValue()) && separaFaturaModal) {
			// Se o tpAbrangencia da fatura � diferente do tpAbrangencia do primeiro
			// documento de servi�o
			throw new BusinessException("LMS-36145");
		}			

		if (fatura.getServico() == null) {
			fatura.setServico(devedorDocServFat.getDoctoServico().getServico());
		} else if (!fatura.getServico().getIdServico().equals(devedorDocServFat.getDoctoServico().getServico().getIdServico()) && separaFaturaModal) {
			// Se o servico da fatura � diferente do servico do primeiro
			// documento de servi�o
			throw new BusinessException("LMS-36145");
		}

		if (fatura.getCliente() == null) {
			fatura.setCliente(devedorDocServFat.getCliente());
		} else if (devedorDocServFat.getCliente().getPessoa().getTpIdentificacao().getValue().equals("CNPJ")) {
			String cnpjFatura = fatura.getCliente().getPessoa().getNrIdentificacao().substring(0,8);
			String cnpjDoc = devedorDocServFat.getCliente().getPessoa().getNrIdentificacao().substring(0,8);
			if (!cnpjFatura.equals(cnpjDoc)){
				// Se os 8 primeiros digitos do cliente da fatura � diferente do cliente do primeiro
				// documento de servi�o
				throw new BusinessException("LMS-36123");				
			}
		} else if (!fatura.getCliente().getIdCliente().equals(devedorDocServFat.getCliente().getIdCliente())) {
			// Se o cliente da fatura � diferente do cliente do primeiro
			// documento de servi�o
			throw new BusinessException("LMS-36123");
		}
				
		
		
		if (fatura.getDivisaoCliente() == null){
			if (devedorDocServFat.getDivisaoCliente() != null){
				fatura.setDivisaoCliente(devedorDocServFat.getDivisaoCliente());
			}
		} else if (devedorDocServFat.getDivisaoCliente() != null && !fatura.getDivisaoCliente().getIdDivisaoCliente().equals(devedorDocServFat.getDivisaoCliente().getIdDivisaoCliente()) && separaFaturaModal){
			// Se a divis�o da fatura � diferente da divis�o do primeiro
			// documento de servi�o
			throw new BusinessException("LMS-36222");
		}

		return fatura;
	}

	/**
	 * Atribui a filial e a filial de cobran�a da fatura
	 * 
	 * Regra 1.1
	 */
	protected Fatura setFilial(Fatura fatura) {
		// Buscar a filial da sessao
		Filial filialSessao = SessionUtils.getFilialSessao();
		
		if (fatura.getFilialByIdFilial() == null) {
			fatura.setFilialByIdFilial(filialService.findById(filialSessao.getIdFilial()));
		}

		return fatura;
	}

	/**
	 * Atribui a filial e a filial de cobran�a da fatura
	 * 
	 * Regra 1.1
	 */
	protected Fatura setFilialCobranca(Fatura fatura) {
		// Buscar a filial da sessao
		Filial filialSessao = SessionUtils.getFilialSessao();

		Filial filialCobranca = null;

		// Buscar a filial centralizadora da filial do cliente
		if (fatura.getCliente() != null) {
			filialCobranca = filialService.findFilialCentralizadoraByFilial(fatura.getCliente().getFilialByIdFilialCobranca().getIdFilial(), filialSessao.getIdFilial(),fatura.getTpModal().getValue(), fatura.getTpAbrangencia().getValue());
		}

		if (fatura.getFilialByIdFilialCobradora() == null) {
			if (filialCobranca != null) {
				fatura.setFilialByIdFilialCobradora(fatura.getCliente().getFilialByIdFilialCobranca());
			} else {
				fatura.setFilialByIdFilialCobradora(filialSessao);
			}
		}
		return fatura;
	}

	/**
	 * Atribui os valores por default da fatura
	 * 
	 * Regra 1.2
	 */
	protected Fatura setValorDefault(Fatura fatura) {

		if (fatura.getDtEmissao() == null) {
			fatura.setDtEmissao(JTDateTimeUtils.getDataAtual());
		}

		if (fatura.getTpFatura() == null) {
			fatura.setTpFatura(new DomainValue("R"));
		}

		if (fatura.getTpModal() == null) {
			fatura.setTpModal(new DomainValue("R"));
		}

		if (fatura.getTpAbrangencia() == null) {
			fatura.setTpAbrangencia(new DomainValue("N"));
		}

		if (fatura.getTpSituacaoFatura() == null) {
			fatura.setTpSituacaoFatura(new DomainValue("DI"));
		}

		if (fatura.getBlGerarEdi() == null) {
			fatura.setBlGerarEdi(Boolean.TRUE);
		}

		if (fatura.getBlGerarBoleto() == null) {
			fatura.setBlGerarBoleto(Boolean.TRUE);
		}

		if (fatura.getBlFaturaReemitida() == null) {
			fatura.setBlFaturaReemitida(Boolean.FALSE);
		}

		if (fatura.getBlIndicadorImpressao() == null) {
			fatura.setBlIndicadorImpressao(Boolean.FALSE);
		}

		if (fatura.getVlBaseCalcPisCofinsCsll() == null) {
			fatura.setVlBaseCalcPisCofinsCsll(new BigDecimal(0));
		}

		if (fatura.getVlBaseCalcIr() == null) {
			fatura.setVlBaseCalcIr(new BigDecimal(0));
		}

		if (fatura.getVlPis() == null) {
			fatura.setVlPis(new BigDecimal(0));
		}

		if (fatura.getVlCofins() == null) {
			fatura.setVlCofins(new BigDecimal(0));
		}

		if (fatura.getVlCsll() == null) {
			fatura.setVlCsll(new BigDecimal(0));
		}

		if (fatura.getVlIr() == null) {
			fatura.setVlIr(new BigDecimal(0));
		}

		if (fatura.getVlJuroCalculado() == null) {
			fatura.setVlJuroCalculado(new BigDecimal(0));
		}

		if (fatura.getVlJuroRecebido() == null) {
			fatura.setVlJuroRecebido(new BigDecimal(0));
		}

		if (fatura.getVlTotalRecebido() == null) {
			fatura.setVlTotalRecebido(new BigDecimal(0));
		}

		if (fatura.getVlIva() == null) {
			fatura.setVlIva(new BigDecimal(0));
		}

		setValorDefaultSpecific(fatura);
		
		return fatura;
	}

	protected abstract void setValorDefaultSpecific(Fatura fatura);

	
	/**
	 * Atribui a data de emissao da fatura
	 * 
	 * Regra 4.1
	 */
	protected Fatura setDtEmissao(Fatura fatura, Fatura faturaAnterior) {
		YearMonthDay dtEmissao = fatura.getDtEmissao();
		// YearMonthDay dtEmissaoAnterior = null;
		// Se a data de emissao � nulla, setar a data atual
		if (dtEmissao == null) {
			fatura.setDtEmissao(JTDateTimeUtils.getDataAtual());
		}else if ( !faturaService.validaDtEmissao(fatura.getDtEmissao()) && (faturaAnterior != null && faturaAnterior.getDtEmissao() != null && dtEmissao.compareTo(faturaAnterior.getDtEmissao()) != 0)) {
			throw new BusinessException("LMS-36099");
		}

		return fatura;
	}

	/**
	 * Atribui a data de vencimento da fatura
	 * 
	 * Regra 6.1
	 */
	protected Fatura setDtVencimento(Fatura fatura) {
		// Se a fatura n�o tem data de vencimento
		if (fatura.getDtVencimento() == null) {
			Long idDivisao = null;
			if (fatura.getDivisaoCliente() != null) {
				idDivisao = fatura.getDivisaoCliente().getIdDivisaoCliente();
			}

			String tpFrete = null;

			if (fatura.getTpFrete() != null) {
				tpFrete = fatura.getTpFrete().getValue();
			}

			if (fatura.getServico() != null) {
				fatura.setDtVencimento(dataVencimentoService.generateDataVencimento(fatura.getFilialByIdFilialCobradora().getIdFilial(),idDivisao, tpFrete, fatura.getDtEmissao(), fatura.getTpModal().getValue(), fatura.getTpAbrangencia().getValue(), fatura.getServico().getIdServico()));
			} else {
				fatura.setDtVencimento(dataVencimentoService.generateDataVencimento(fatura.getFilialByIdFilialCobradora().getIdFilial(),idDivisao, tpFrete, fatura.getDtEmissao(), fatura.getTpModal().getValue(), fatura.getTpAbrangencia().getValue(), null));
			}
		}

		return fatura;
	}
	

	/**
	 * Valida a data de vencimento da fatura
	 * 
	 * Regra 4.2
	 */
	protected void validateDtVencimento(Fatura fatura, Fatura faturaAnterior) {
		YearMonthDay dtEmissao = fatura.getDtEmissao();
		YearMonthDay dtVencimento = fatura.getDtVencimento();
		YearMonthDay dtVencimentoAnterior = null;
		boolean blValidar = true;

		// Se existe uma fatura anterior, buscar as datas
		if (faturaAnterior != null) {
			dtVencimentoAnterior = faturaAnterior.getDtVencimento();

			// Se a data de vencimento n�o mudou, n�o precisa validar com a data
			// atual
			if (dtVencimentoAnterior.compareTo(dtVencimento) == 0) {
				blValidar = false;
			}
		}

		// Se a data de vencimento � antes da data atual ou se o registro �
		// novo, lan�ar uma exception
		if ((blValidar == true || fatura.getIdFatura() == null) && dtVencimento.isBefore(JTDateTimeUtils.getDataAtual())) {
			throw new BusinessException("LMS-36091");
		}

		// Se a data de emiss�o � depois da data de vencimento, lan�ar uma
		// exception
		if (dtEmissao.isAfter(dtVencimento)) {
			throw new BusinessException("LMS-36125");
		}
	}

	/**
	 * Atribui o servi�o da fatura
	 * 
	 * Regra 4.4
	 */
	protected Fatura setServico(Fatura fatura) {
		// Se o servi�o for nullo
		if (fatura.getServico() == null) {
			Servico servico = new Servico();
			// Se a abrangencia for 'Nacional', selecionar o servi�o padr�o
			// nacional, sen�o, internacional
			if (fatura.getTpAbrangencia().getValue().equals("N")) {
				servico.setIdServico(Long.valueOf(((BigDecimal) configuracoesFacade.getValorParametro("SERVICO_PADRAO")).longValue()));
			} else {
				servico.setIdServico(Long.valueOf(((BigDecimal) configuracoesFacade.getValorParametro("SERVICO_PADRAO_INTERNACIONAL")).longValue()));
			}
			fatura.setServico(servico);
		}

		return fatura;
	}

	/**
	 * Valida o tipo de frete da fatura
	 * 
	 * Regra 4.5
	 */
	protected void validateTipoFrete(Fatura fatura, List lstItemFatura) {
		// Se o tipo de abrang�ncia for nacional, o tipo de frete nulo e o tipo
		// de origem diferente
		// de 'integra��o', lan�ar uma exception
		if (lstItemFatura != null && !lstItemFatura.isEmpty()){
			ItemFatura itemFatura = (ItemFatura) lstItemFatura.get(0);
			String tpDocumentoServico = itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue();

			if (fatura.getTpAbrangencia().getValue().equals("N") && fatura.getTpFrete() == null && (tpDocumentoServico.equals("CRT") || tpDocumentoServico.equals("NFT")) && 
				!fatura.getTpOrigem().getValue().equals("I")) {
				throw new BusinessException("LMS-36126");
			}
		}
	}

	/**
	 * Atribui o cedente da fatura
	 * 
	 * Regra 4.6
	 */
	protected Fatura setCedente(Fatura fatura) {
		// Se n�o tem cedente
		if (fatura.getCedente() == null) {
			// Se o cliente tem cedente e a filial de emiss�o tamb�m tem, atribuir na fatura o cedente do cliente
			if (fatura.getCliente().getCedente() != null && fatura.getFilialByIdFilial().getCedenteByIdCedenteBloqueto() != null) {
				fatura.setCedente(fatura.getCliente().getCedente());
				// Sen�o, se a filial tem cedente bloqueto, atribuir na fatura o cedente da filial
			} else if (fatura.getFilialByIdFilial().getCedenteByIdCedenteBloqueto() != null) {
				fatura.setCedente(fatura.getFilialByIdFilial().getCedenteByIdCedenteBloqueto());
			} else {
				fatura.setBlGerarBoleto(Boolean.FALSE);
			}
		}

		return fatura;
	}

	/**
	 * Valida a cota��o moeda da fatura
	 * 
	 * Regra 4.7
	 */
	protected void validateCotacao(Fatura fatura) {
		// Se o tipo de abrang�ncia for 'Internacional' a cota��o moeda �
		// obrigat�ria,
		// caso n�o tem, lan�ar uma exception
		if (fatura.getTpAbrangencia().getValue().equals("I") && fatura.getCotacaoMoeda() == null) {
			throw new BusinessException("LMS-36128");
		}
	}

	/**
	 * Valida a obrigat�rariedade de item de fatura da fatura
	 * 
	 * Regra 4.8
	 */
	protected void validateExistenciaItemFatura(Fatura fatura, List lstItemFatura, List lstItemFaturaNewOrModified, List lstItemFaturaRemoved, boolean blNovaFatura) {
		// Se a situa��o da fatura for 'Inutilizado' e a observa��o for nulo,
		// lan�ar uma exception
		if (fatura.getTpSituacaoFatura().getValue().equals("IN")) {
			if (!StringUtils.isNotBlank(fatura.getObFatura())) {
				throw new BusinessException("LMS-36129");
			}
			// Sen�o � 'Inutilizado' e que n�o tem item fatura, lan�ar uma
			// exception
		} else if ((blNovaFatura && lstItemFatura.isEmpty()) ||
				(!blNovaFatura && !lstItemFaturaRemoved.isEmpty() && lstItemFatura.isEmpty())) {
			throw new BusinessException("LMS-36038");
		}
	}

	/**
	 * Calcula o valor total dos documentos de servi�os e atualiza a os campos
	 * da fatura
	 * 
	 * Regra 5.1 / 5.2 / 5.3
	 */
	protected Fatura calculateSomatorioFatura(Fatura fatura) {
		return calcularValorAtualFaturaService.executeCalcularValorAtualFatura(fatura);
	}
	
	/**
	 * Calcula o novo valor iva da fatura baseado no porcentagem de al�quota vigente
	 * e o valor de frete externo do cto internacional
	 * 
	 * @author Micka�l Jalbert
	 * @since 04/07/2006
	 * 
	 * @param Fatura fatura
	 * 
	 * @return Fatura
	 */
	protected Fatura calculatVlIva(Fatura fatura){
		//Se a fatura � internacional, calcular o valor iva.
		if (fatura.getTpAbrangencia().getValue().equals("I")){
			BigDecimal vlIva = faturaService.findVlIvaByFatura(fatura.getIdFatura(), fatura.getFilialByIdFilial().getIdFilial(), fatura.getDtEmissao());
			
			fatura.setVlIva(vlIva);
		}
		
		return fatura;
	}

	/**
	 * Carrega o primeiro documento de servi�o da fatura.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param fatura
	 * @return
	 *
	 * DoctoServico
	 *
	 */
	public DoctoServico getFirstDoctoServicoFromFatura (Fatura fatura, List lstItemFatura) {
		DoctoServico ds = null;
		if (lstItemFatura != null && !lstItemFatura.isEmpty() ) {	  
			ds = ((ItemFatura)lstItemFatura.get(0))    
					.getDevedorDocServFat().getDoctoServico();		
		}
		return ds;
	}
	
	/**
	 * Gera um boleto para a fatura informada.
	 * 
	 * Regra 5.4
	 */
	protected void generateBoleto(Fatura fatura, boolean blNovaFatura, List lstItemFatura) {
		if (fatura.getBlGerarBoleto().equals(Boolean.TRUE) && fatura.getCedente() != null && blNovaFatura) {
			//N�o pode gerar boleto com fatura 'Em aprova��o'
			if (fatura.getTpSituacaoAprovacao() == null || (fatura.getTpSituacaoAprovacao() != null && !fatura.getTpSituacaoAprovacao().getValue().equals("E"))) {
				// Gera boleto
				boletoService.generateBoletoDeFatura(fatura);
			}
		}
	}

	/**
	 * Gera uma pendencia (workflow) para liberar um valor especial de cota��o
	 * (quando a cota��o n�o � igual � cota��o que vem do banco)
	 * 
	 * Regra 5.5
	 */
	protected Pendencia generatePendenciaDeCotacao(Fatura fatura) {
		// Se o tipo de abrang�ncia for 'Internacional'
		if (fatura.getTpAbrangencia().getValue().equals("I")) {
			// Se o valor da cota��o na fatura � diferente do valor da cota��o
			// do banco
			if (fatura.getVlCotacaoMoeda() != null) {
				// Se � um update
				if (fatura.getIdFatura() != null) {
					Fatura faturaBD = faturaService.findByIdDisconnected(fatura.getIdFatura());
					// Se o valor � diferente
					if (fatura.getVlCotacaoMoeda().compareTo(faturaBD.getVlCotacaoMoeda()) != 0) {
						// Cancela a pendencia ligada
						if (fatura.getPendencia() != null) {
							workflowPendenciaService.cancelPendencia(fatura.getPendencia().getIdPendencia());
						}
					}
				}
				// Gera uma nova pendencia
				fatura.setTpSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", "E"));
				return workflowPendenciaService.generatePendencia(fatura.getFilialByIdFilial().getIdFilial(), ConstantesWorkflow.NR3606_ALT_COT_DOL_FAT, fatura.getIdFatura(), null, JTDateTimeUtils.getDataHoraAtual());
			}
		}

		return null;
	}	

	private ItemFatura generateItemFatura(Fatura fatura, DevedorDocServFat devedorDocServFat) {
		ItemFatura itemFatura = new ItemFatura();

		itemFatura.setFatura(fatura);
		itemFatura.setDevedorDocServFat(devedorDocServFat);

		return itemFatura;
	}

	public Fatura storeFaturaWithDevedorDocServFat(Fatura fatura, DevedorDocServFat devedorDocServFat) {
		List lstDevedorDocServFat = new ArrayList();
		lstDevedorDocServFat.add(devedorDocServFat.getIdDevedorDocServFat());

		return storeFaturaWithIdsDevedorDocServFat(fatura, lstDevedorDocServFat);
	}
	

	

	/**
	 * 
	 * 
	 * FACILITADORES
	 * 
	 * 
	 */

	/**
	 * Monta o objeto que serve para comparar com o �ltimo devedor doc serv fat
	 */
	public GenerateFaturaParam mountGenerateFaturaParam(DevedorDocServFat devedorDocServFat) {
		GenerateFaturaParam generateFaturaParam = new GenerateFaturaParam();

		String tpDocumentoServico = devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue();

		generateFaturaParam.setIdMoeda(devedorDocServFat.getDoctoServico().getMoeda().getIdMoeda());

		generateFaturaParam.setIdServico(devedorDocServFat.getDoctoServico().getServico().getIdServico());
		generateFaturaParam.setTpDoctoServico(tpDocumentoServico);

		generateFaturaParam.setTpAbrangencia(devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia().getValue());
		generateFaturaParam.setTpModal(devedorDocServFat.getDoctoServico().getServico().getTpModal().getValue());

		generateFaturaParam.setIdCliente(devedorDocServFat.getCliente().getIdCliente());
		generateFaturaParam.setTpIdentificacao(devedorDocServFat.getCliente().getPessoa().getTpIdentificacao().getValue());
		generateFaturaParam.setNrIdentificacaoParcial(devedorDocServFat.getCliente().getPessoa().getNrIdentificacao().substring(0,8));		
		
		if (tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("NFT") || tpDocumentoServico.equals("CTE")
				|| tpDocumentoServico.equals("NTE")) {
 			generateFaturaParam.setTpFrete(conhecimentoService.findTpFreteByIdConhecimento(devedorDocServFat.getDoctoServico().getIdDoctoServico()).getValue());
		}
		
		if (devedorDocServFat.getDivisaoCliente() != null){
			generateFaturaParam.setIdDivisaoCliente(devedorDocServFat.getDivisaoCliente().getIdDivisaoCliente());
		}
		
		return generateFaturaParam;
	}

	/**
	 * 
	 * 
	 * SETTER'S E GETTER'S
	 * 
	 * 
	 */

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setCalcularValorAtualFaturaService(
			CalcularValorAtualFaturaService calcularValorAtualFaturaService) {
		this.calcularValorAtualFaturaService = calcularValorAtualFaturaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setZerarDescontoService(ZerarDescontoService zerarDescontoService) {
		this.zerarDescontoService = zerarDescontoService;
	}

	public ZerarDescontoService getZerarDescontoService() {
		return zerarDescontoService;
	}

	public void setEfetivarDescontoFaturaService(
			EfetivarDescontoFaturaService efetivarDescontoFaturaService) {
		this.efetivarDescontoFaturaService = efetivarDescontoFaturaService;
	}

	public EfetivarDescontoFaturaService getEfetivarDescontoFaturaService() {
		return efetivarDescontoFaturaService;
	}

	public void setOcorrenciaBancoService(
			OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}
	
	public void setHistoricoBoletoService(
			HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}
}
