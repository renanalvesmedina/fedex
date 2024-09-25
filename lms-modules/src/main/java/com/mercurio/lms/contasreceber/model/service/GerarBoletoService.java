package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 */
public class GerarBoletoService {
	 
	protected DevedorDocServFatService devedorDocServFatService;
	
	protected ClienteService clienteService;
	
	protected GerarFaturaBoletoService gerarFaturaBoletoService;
	
	protected BoletoService boletoService;
	
	protected FaturaService faturaService;
	
	protected NrBoletoService nrBoletoService;
	
	protected DiaUtils diaUtils;

	protected CalcularJurosDiarioService calcularJurosDiarioService;
	
	private MunicipioService municipioService;
	
	private ValidateItemFaturaService validateItemFaturaService;
	
	private ConhecimentoService conhecimentoService;
	
	private ItemFaturaService itemFaturaService;
	
	private ParametroGeralService parametroGeralService;
	
	private BoletoAnexoService boletoAnexoService;
	
	/**
	 * Gera um boleto, fatura e item fatura a partir do devedor informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 19/04/2006
	 * 
	 * @param Boleto boleto
	 * @param DevedorDocServFat devedorDocServFat
	 * @param DivisaoCliente divisaoCliente
	 * 
	 * @return entidade que foi armazenada.
	 */
    public Boleto store(Boleto boleto, DevedorDocServFat devedorDocServFat, ItemList listAnexos) {
    	devedorDocServFatService.validateDisponibilidadeDevedorDocServFat(devedorDocServFat);
    	
    	// Caso o focumento de serviço não seja uma nota de débito nacional, 
    	// valida se o documento foi entregue.
    	if(!"NDN".equalsIgnoreCase(devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue())){
    		validateItemFaturaService.validateDocumentoEntregue(devedorDocServFat, devedorDocServFat.getCliente(), conhecimentoService.findTpFreteTpSituacaoByIdConhecimento(devedorDocServFat.getDoctoServico().getIdDoctoServico()));
    	}
    	//Gerar a fatura e o itemFatura
    	Fatura fatura = initializeFatura(boleto.getFatura(), devedorDocServFat, boleto.getDtEmissao(), boleto.getDtVencimento(), boleto.getCedente());
    	gerarFaturaBoletoService.storeFaturaWithDevedorDocServFat(fatura, devedorDocServFat); 	
    	
    	boleto.setFatura(fatura);
    	
    	boleto = beforeInsert(boleto);
    	
    	boletoAnexoService.storeAnexos(listAnexos);
    	
        return boletoService.store(boleto);
    }
    
    
    public Boleto store(Boleto boleto) {
    	return this.store(boleto, null);
    }
	/**
	 * Gera ou atualiza um boleto
	 * 
	 * @author Mickaël Jalbert
	 * @since 19/04/2006
	 * 
	 * @param Boleto boleto
	 * 
	 * @return entidade que foi armazenada.
	 */
    public Boleto store(Boleto boleto,ItemList listAnexos) {
    	
    	boleto = beforeStore(boleto);
    	
    	boletoService.store(boleto);
    	
    	boletoAnexoService.storeAnexos(listAnexos);
    	
        return boleto;
    }
    
    
    /**
     * Overload para manter integridade da chamada mais antiga
     * 
     * @author Ícaro Damiani
     * 
     * @param boleto
     * @return
     */
    public Boleto insertBoleto(Boleto boleto) {
    	return insertBoleto(boleto, true);
    }
    
    
	/**
	 * Gera ou atualiza um boleto
	 * 
	 * @author Mickaël Jalbert
	 * @param validateSessao 
	 * @since 19/04/2006
	 * 
	 * @param Boleto boleto
	 * @param boolean validateSessao
	 * 
	 * @return entidade que foi armazenada.
	 */
    public Boleto insertBoleto(Boleto boleto, boolean isBatch) {
    	
    	boleto = beforeInsert(boleto,isBatch);
    	
    	boletoService.store(boleto);
    	
        return boleto;
    }    
    
    protected Boleto beforeStore(Boleto boleto) {
    	
    	Fatura fatura = boleto.getFatura();
    	
    	if (!SessionUtils.isIntegrationRunning()){
    		if (fatura.getBlConhecimentoResumo() != null && fatura.getBlConhecimentoResumo()){
        		throw new BusinessException("LMS-36256");
        	}
    	}
    	

    	/* ET 36.02.03.06 - solicitado para retirar
    	// Caso o parametro BL_LMS_INTEGRADO_CORPORATIVO seja S e o 
		// documento da fatura não seja CTR, não deve gerar o boleto.
		if ("S".equals(configuracoesFacade.getValorParametro("BL_LMS_INTEGRADO_CORPORATIVO"))) {
			String tpDocumento = getFirstTpDoctoServicoByIdFatura(fatura.getIdFatura());
    		if(tpDocumento != null && !"CTR".equals(tpDocumento)){
    			throw new BusinessException("LMS-36246");
    		}
    	}
		*/
		
		// O intervalo de dias entre a da data de emissão e vencimento não pode ultrapassar 
		// o parametro geral NR_DIAS_LIMITE_VENCIMENTO (a menos que ele venha da integracao)
		if(!"I".equalsIgnoreCase(fatura.getTpOrigem().getValue())){
		faturaService.validateLimitDate(boleto.getDtEmissao(),boleto.getDtVencimento());
		}
						
    	// Caso a situacao de aprovação da fatura não seja nula
    	// e seja diferente de A, lança a exception.
    	DomainValue tpSituacaoAprovacao = boleto.getFatura()
    			.getTpSituacaoAprovacao();
		if (tpSituacaoAprovacao != null
				&& !"A".equals(tpSituacaoAprovacao.getValue())) {
    		throw new BusinessException("LMS-36095");
    	}
    	
    	if (boleto.getIdBoleto() == null){
    		
    		boleto = beforeInsert(boleto);
        	
        	// Se a situação de aprovação da fatura ou dos descontos da fatura for 
    		// igual a 'Em aprovação', lançar uma exception
        	faturaService.validateFaturaPendenteWorkflow(boleto.getFatura());    		
    	}
    	
    	return boleto;
    }
    
    /**
	 * Obtem o tipo de documento do primeiro objeto da lista de ItemFatura 
	 * relacionado com Fatura. Foi necessário criar este método devido a uma regra
	 * especifíca solicitada pelo análista.
	 * 
	 * @param idFatura
	 * @return
	 */
	private String getFirstTpDoctoServicoByIdFatura (Long idFatura) {		
		List<TypedFlatMap> lsTpDocumentoFatura = itemFaturaService.findTpDocumentoItemFatura(idFatura);		
		if (lsTpDocumentoFatura != null && !lsTpDocumentoFatura.isEmpty()) {	  
			return lsTpDocumentoFatura.get(0).getString("tpDocumentoServico.value");		
		}
		return null;
	}    
    
    /**
	 * Carrega o primeiro documento de serviço da fatura.
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
	private DoctoServico getFirstDoctoServicoFromFatura (Fatura fatura) {
		fatura = faturaService.findById(fatura.getIdFatura());
		DoctoServico ds = null;
		List itemFaturas = fatura.getItemFaturas();
		if (itemFaturas != null) {	  
			ds = ((ItemFatura)itemFaturas.get(0))    
					.getDevedorDocServFat().getDoctoServico();		
		}
		return ds;
	}
    

	/**
	 * Overload para manter integridade de chamadas antigas
	 * 
	 * @author Icaro Damiani
	 * 
	 * @param boleto
	 * @return
	 */
    protected Boleto beforeInsert(Boleto boleto) {
    	return beforeInsert(boleto,true);
    }
    
    protected Boleto beforeInsert(Boleto boleto, boolean isBatch) {
		/** Valida para não inserir nrSequenciaFilial com diferença de numeração */
		if (!LongUtils.hasValue(boleto.getNrSequenciaFilial())) {
			boleto.setNrSequenciaFilial(boletoService.findNextNrSequenciaFilial(boleto.getFatura().getFilialByIdFilial().getIdFilial()));
		}
    	
    	boleto.setNrBoleto(nrBoletoService.findNextNrBoleto(boleto.getFatura().getFilialByIdFilial().getIdFilial(), boleto.getCedente().getIdCedente()));
    	boleto.setBlBoletoConhecimento(Boolean.FALSE);
    	boleto.setBlBoletoReemitido(Boolean.FALSE);
    	
    	//Calcular o valor do jurio diario
    	boleto.setVlJurosDia(calcularJurosDiarioService.calcularVlJuros(boleto.getFatura(), new BigDecimal(1)));

    	/*Comentado devido LMS-3906
    	 * 
    	 * A data de emissao deve ser menor de 3 dias ou igual a data atual
    	YearMonthDay dtEmissao = boleto.getDtEmissao();
    	if (dtEmissao.isAfter(JTDateTimeUtils.getDataAtual()) || dtEmissao.isBefore(JTDateTimeUtils.getDataAtual().minusDays(4))){
    		throw new BusinessException("LMS-36099");
    	}
 		*/    	
    	
    	if(!faturaService.validaDtEmissaoComDataFatura(boleto.getDtEmissao(), boleto.getFatura().getDtEmissao())){
    		throw new BusinessException("LMS-36099");
    	}
    	
    	Long idMunicipio = municipioService.findIdMunicipioByPessoa(boleto.getFatura().getCliente().getIdCliente()); 
    	if (idMunicipio != null){
    		//A data de vencimento tem que ser um dia util
    		if (boleto.getDtVencimento() != null){
    			boleto.setDtVencimento(boleto.getDtVencimento());
    		} else {
    			boleto.setDtVencimento(boleto.getFatura().getDtVencimento());
    		}
    	}else{
    		throw new BusinessException("LMS-00069");
    	}
    	//A filial de origem da fatura tem que ser igual a filial da sessão
    	if (!isBatch && (!boleto.getFatura().getFilialByIdFilial().equals(SessionUtils.getFilialSessao()) && !SessionUtils.isFilialSessaoMatriz()) ){
    		throw new BusinessException("LMS-36098");
    	}
    	
    	String tpSituacao = boleto.getFatura().getTpSituacaoFatura().getValue();
    	
    	//Se a situação da fatura for diferente de 'Digitado' e 'Emitido', lançar uma exception
    	if (!tpSituacao.equals("DI") && !tpSituacao.equals("EM")) {
    		throw new BusinessException("LMS-36094");
    	}
    	
    	//Seta o valor convertido da fatura no boleto
    	boleto.setVlTotal(boleto.getFatura().getVlTotal());

    	//Não pode usar uma fatura migrada para gerar boleto
    	if (boleto.getFatura().getNrFatura().compareTo(Long.valueOf(9999999)) >= 0){
    		throw new BusinessException("LMS-36214");
    	}
    	
    	return boleto;
    }
 
	/**
	 * Inicializar os dados da nova fatura que vai ser gerada a partir do boleto
	 * */
	protected Fatura initializeFatura(Fatura fatura, DevedorDocServFat devedorDocServFat, YearMonthDay dtEmissao, YearMonthDay dtVencimento, Cedente cedente){
		if (fatura == null){
			fatura = new Fatura();
		}
		
		fatura.setCliente(devedorDocServFat.getCliente());	
		fatura.setCedente(cedente);
		
		fatura.setFilialByIdFilialCobradora(devedorDocServFat.getFilial());
		
		fatura.setTpModal(devedorDocServFat.getDoctoServico().getServico().getTpModal());
		fatura.setTpAbrangencia(devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia());						
		fatura.setVlTotal(devedorDocServFat.getDoctoServico().getVlTotalDocServico());
		fatura.setMoeda(devedorDocServFat.getDoctoServico().getMoeda());		
		fatura.setDivisaoCliente(devedorDocServFat.getDivisaoCliente());
		
		fatura.setDtEmissao(dtEmissao);
		fatura.setDtVencimento(dtVencimento);
		
		return fatura;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setCalcularJurosDiarioService(
			CalcularJurosDiarioService calcularJurosDiarioService) {
		this.calcularJurosDiarioService = calcularJurosDiarioService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setGerarFaturaBoletoService(
			GerarFaturaBoletoService gerarFaturaBoletoService) {
		this.gerarFaturaBoletoService = gerarFaturaBoletoService;
	}

	public void setNrBoletoService(NrBoletoService nrBoletoService) {
		this.nrBoletoService = nrBoletoService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setValidateItemFaturaService(
			ValidateItemFaturaService validateItemFaturaService) {
		this.validateItemFaturaService = validateItemFaturaService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}
    
	public void setItemFaturaService(ItemFaturaService itemFaturaService) {
		this.itemFaturaService = itemFaturaService;
}
    
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setBoletoAnexoService(BoletoAnexoService boletoAnexoService) {
		this.boletoAnexoService = boletoAnexoService;
	}

	public BoletoAnexoService getBoletoAnexoService() {
		return boletoAnexoService;
	}
    
}
