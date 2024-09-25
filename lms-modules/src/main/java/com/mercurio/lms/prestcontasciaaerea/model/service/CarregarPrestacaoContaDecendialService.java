package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.prestcontasciaaerea.model.AwbCancelado;
import com.mercurio.lms.prestcontasciaaerea.model.IcmsPrestacao;
import com.mercurio.lms.prestcontasciaaerea.model.IntervaloAwb;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;
import com.mercurio.lms.prestcontasciaaerea.model.ValorPrestacaoConta;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.prestcontasciaaerea.carregarPrestacaoContaDecendialService"
 */
public class CarregarPrestacaoContaDecendialService {

	private AwbService awbService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private PrestacaoContaService prestacaoContaService;
	private IntervaloAwbService intervaloAwbService;
	private AwbCanceladoService awbCanceladoService;
	private ValorPrestacaoContaService valorPrestacaoContaService;
	private IcmsPrestacaoService icmsPrestacaoService;
	private ParametroFilialService parametroFilialService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private FaturamentoCiaAereaService faturamentoCiaAereaService;
	private ConfiguracoesFacade configuracoesFacade;

	private static final BigDecimal ZERO = new BigDecimal(0);
	
	public void storePestacaoContaDecencialTeste(Map map){
		
		TypedFlatMap tmap = new TypedFlatMap();
		
		tmap.putAll(map);
		
		Long idFilialPrestadora = (map.containsKey("filial")) ? Long.valueOf((String)((Map)map.get("filial")).get("idFilial")) : null;
		Long idCiaAerea = (map.containsKey("cliente")) ? Long.valueOf((String)((Map)map.get("cliente")).get("idCliente")) : null;
		YearMonthDay dtDecendioInicial = JTDateTimeUtils.getDataAtual();
		
		if (StringUtils.isNotBlank(tmap.getString("dtDecendioInicial"))) {
			dtDecendioInicial = tmap.getYearMonthDay("dtDecendioInicial");
		}

		YearMonthDay dtDecendioFinal = JTDateTimeUtils.getDataAtual();
		
		if (StringUtils.isNotBlank(tmap.getString("dtDecendioFinal"))) {
			dtDecendioInicial = tmap.getYearMonthDay("dtDecendioFinal");
		}
		storePrestacaoContaDecencial(idFilialPrestadora, idCiaAerea, dtDecendioInicial, dtDecendioFinal);
	}
	
	
	
	/**
     * 37.01.01.01 Carga das tabelas prestação de contas decendial
     * 
	 * Rotina para carregar os AWBs nas tabelas da prestação de contas
	 * 
     * @param idFilialPrestadora   Filial prestadora de contas
     * @param idCiaAerea   Cia aérea a ser prestada as contas
     * @param dtDecendioInicial   Data do inicial do decêndio
     * @param dtDecendioFinal   Data do final do decêndio
	 * 
	 * @return nrPrestacaoConta que acabou de ser inserido
	 */
    public Long  storePrestacaoContaDecencial(Long idFilialPrestadora, Long idCiaAerea, YearMonthDay dtDecendioInicial, YearMonthDay dtDecendioFinal){

    	//Regra 0
    	YearMonthDay dtIniPrestacaoConta = (YearMonthDay)this.configuracoesFacade.getValorParametroWithException(idFilialPrestadora, "DT_INI_PREST_CONTAS");
    	
    	// Regra 1 - AwbService.findPrestacaoContas
    	List awbs = getAwbService().findPrestacaoContas(idFilialPrestadora, idCiaAerea, dtIniPrestacaoConta, dtDecendioFinal);
    	
    	if (awbs.isEmpty()) return null;
    	
    	Long idCiaFilialMercurio = (Long) ((Map)awbs.get(0)).get("idCiaFilialMercurio");
    	
    	// Regra 2 - CiaFilialMercurioService.storePrestacaoContas
    	Long nrPrestacaoConta = getCiaFilialMercurioService().storePrestacaoContas(idCiaFilialMercurio);

    	// Regra 3 - PrestacaoContaService.store
    	PrestacaoConta prestacaoConta = new PrestacaoConta();

    	CiaFilialMercurio cia = getCiaFilialMercurioService().findById( idCiaFilialMercurio);
    	prestacaoConta.setCiaFilialMercurio(cia);
    	prestacaoConta.setNrPrestacaoConta(nrPrestacaoConta);
    	prestacaoConta.setQtAwb(Long.valueOf(awbs.size()));
    	   	
    	prestacaoConta.setDtEmissao(JTDateTimeUtils.getDataAtual());
    	prestacaoConta.setDtInicial(dtDecendioInicial);
    	
    	//regra 2.1 - NR_PRAZO_PAGAMENTO
    	prestacaoConta.setDtFinal(dtDecendioFinal.plusDays(getFaturamentoCiaAereaService().getPrazoPagamento(idCiaAerea, idFilialPrestadora, dtDecendioInicial)) );
    	
    	/**
    	 * Edenilson - 30/12/2005
    	 * Setar a data de vencimento - regra 3
    	 */
    	YearMonthDay dtVencimento = JTDateTimeUtils.getDataAtual();
    	
    	dtVencimento = JTDateTimeUtils.setYear(dtVencimento, dtDecendioInicial.getYear());
    	dtVencimento = JTDateTimeUtils.setMonth(dtVencimento, dtDecendioInicial.getMonthOfYear());
    	
    	if (dtDecendioInicial.getDayOfMonth()==1) {
    		dtVencimento = JTDateTimeUtils.setDay(dtVencimento, 23);
    	} else if (dtDecendioInicial.getMonthOfYear()==11) {
    		dtVencimento = JTDateTimeUtils.setDay(dtVencimento, 3);
    	} else {
    		dtVencimento = JTDateTimeUtils.setDay(dtVencimento, 13);
    	}

    	prestacaoConta.setDtVencimento(dtVencimento);

    	getPrestacaoContaService().store(prestacaoConta);

    	// Regra 4 - AwbService.storePrestacaoConta
    	//intervalos
    	List intervalos = new ArrayList(awbs.size());
    	long awbInicial = -1l;
    	long awbFinal = -1l;
    	long dvAwbInicial = -1l;
    	long dvAwbFinal = -1l;
    	long awbCurrent = -1l;
    	long dvAwbCurrent = -1l;

    	//Valor prestacao conta
    	BigDecimal vlFretePesoCif = ZERO;
    	BigDecimal vlFretePesoFob = ZERO;
    	long qtAwbCif = 0;
    	long qtAwbFob = 0;
    	BigDecimal psTotalCif = ZERO;
    	BigDecimal psTotalFob = ZERO;
    	BigDecimal vlTaxaTerrestreDestinoCif = ZERO;
    	BigDecimal vlTaxaTerrestreDestinoFob = ZERO;
    	BigDecimal vlTaxaCombustivelCif = ZERO;
    	BigDecimal vlTaxaCombustivelFob = ZERO;

    	for (Iterator iter = awbs.iterator(); iter.hasNext(); ){
    		Map map = (Map)iter.next();
    		Awb awb = (Awb) map.get("awb");
    		awb.setPrestacaoConta(prestacaoConta);
    		getAwbService().store(awb);

    		awbCurrent = awb.getNrAwb().longValue();
    		dvAwbCurrent = awb.getDvAwb().longValue();

    		//Inicializacao, primeira iterecao
    		if (awbInicial == -1l) {
    			awbInicial = awbCurrent;
    			dvAwbInicial = dvAwbCurrent;
    		}
    		if (awbFinal == -1l) {
    			awbFinal = awbCurrent;
    			dvAwbFinal = dvAwbCurrent;
    		}

    		//Verifica quebra de intervalo
    		//Intervalo unico (sem quebras) é incluido após loop
    		if (awbCurrent > (awbFinal+1) ){
    			//Intervalo
    			intervalos.add( new IntervaloAwb(Long.valueOf(awbInicial), Long.valueOf(dvAwbInicial), Long.valueOf(awbFinal), Long.valueOf(dvAwbFinal), prestacaoConta) );
    			//Inicia novo intervalo
    			awbInicial = awbCurrent;
    			dvAwbInicial = dvAwbCurrent;
    		}

    		//Final do intervalo e' sempre o nrAwb corrente
    		awbFinal = awbCurrent;
    		dvAwbFinal = dvAwbCurrent;

    		//AWBs Cancelados
    		if (awb.getTpStatusAwb() != null && awb.getTpStatusAwb().getValue().equals("C")){
    			AwbCancelado awbCancelado = new AwbCancelado();
    			awbCancelado.setNrAwbCancelado(Long.valueOf(awbCurrent));
    			awbCancelado.setDvAwbCancelado(Long.valueOf(dvAwbCurrent));
    			awbCancelado.setPrestacaoConta(prestacaoConta);
    			getAwbCanceladoService().store(awbCancelado);
    		//AWBs emitidos
    		} else {
	    		if (awb.getTpFrete() != null && awb.getTpFrete().getValue().equals("C")){
	    			vlFretePesoCif = BigDecimalUtils.add(vlFretePesoCif, awb.getVlFretePeso());
	    			qtAwbCif++;
	    			psTotalCif = BigDecimalUtils.add(psTotalCif, awb.getPsTotal());
	    			vlTaxaTerrestreDestinoCif = BigDecimalUtils.add(vlTaxaTerrestreDestinoCif, awb.getVlTaxaTerrestre());
	    			vlTaxaCombustivelCif = BigDecimalUtils.add(vlTaxaCombustivelCif, awb.getVlTaxaCombustivel());
	    		} else {
	    			vlFretePesoFob = BigDecimalUtils.add(vlFretePesoFob, awb.getVlFretePeso());
	    			qtAwbFob++;
	    			psTotalFob = BigDecimalUtils.add(psTotalFob, awb.getPsTotal());
	    			vlTaxaTerrestreDestinoFob = BigDecimalUtils.add(vlTaxaTerrestreDestinoFob, awb.getVlTaxaTerrestre());
	    			vlTaxaCombustivelFob = BigDecimalUtils.add(vlTaxaCombustivelFob, awb.getVlTaxaCombustivel());
	    		}
    		}
    	}

    	//Primeiro/ultimo intervalo (sem quebra -- intervalo unico)
    	if (awbFinal != -1l){
    		intervalos.add( new IntervaloAwb(Long.valueOf(awbInicial), Long.valueOf(dvAwbInicial), Long.valueOf(awbFinal), Long.valueOf(dvAwbFinal), prestacaoConta) );	
    	}

    	if (!intervalos.isEmpty()){
    		for (Iterator iter = intervalos.iterator(); iter.hasNext() ; ){
        		getIntervaloAwbService().store((IntervaloAwb) iter.next());
        	}
    	}

    	// Regra 5 - ValorPrestacaoContaService.store
    	//para Cif
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlFretePesoCif,"FR","PG",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(new BigDecimal(qtAwbCif),"QA","PG",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(psTotalCif,"PE","PG",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlTaxaCombustivelCif,"TC","PG",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlTaxaTerrestreDestinoCif,"TD","PG",prestacaoConta));
    	//para Fob
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlFretePesoFob,"FR","AP",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(new BigDecimal(qtAwbFob),"QA","AP",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(psTotalFob,"PE","AP",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlTaxaCombustivelFob,"TC","AP",prestacaoConta));
    	getValorPrestacaoContaService().store(new ValorPrestacaoConta(vlTaxaTerrestreDestinoFob,"TD","AP",prestacaoConta));
    	
    	// Regra 6 - AwbService.findPrestacaoContasICMS
    	List awbIcms = getAwbService().findPrestacaoContasICMS(idFilialPrestadora, idCiaAerea, dtIniPrestacaoConta, dtDecendioFinal);
    	
    	// Regra 7 - IcmsPretacaoService.store
    	for (Iterator iter = awbIcms.iterator(); iter.hasNext(); ){
    		Map map = (Map)iter.next();

    		BigDecimal pcAliquotaICMS = new BigDecimal(0);
    		BigDecimal vlFrete = new BigDecimal(0); 
			BigDecimal vlTaxa = new BigDecimal(0);
			BigDecimal vlICMS = new BigDecimal(0);
    		
			if (map.get("pcAliquotaICMS")!=null) {
				pcAliquotaICMS = (BigDecimal)map.get("pcAliquotaICMS");
			}

			if (map.get("vlFrete")!=null) {
				vlFrete = (BigDecimal)map.get("vlFrete");
			}
			
			if (map.get("vlTaxa")!=null) {
				vlTaxa = (BigDecimal)map.get("vlTaxa");
			}

			if (map.get("vlICMS")!=null) {
				vlICMS = (BigDecimal)map.get("vlICMS");
			}
			
			getIcmsPrestacaoService().storeWithFlush(
    				new IcmsPrestacao(
    						pcAliquotaICMS,
    						vlFrete, 
    						ZERO,
    						vlTaxa,
    						vlICMS,
    						prestacaoConta)
    				);
    	}

    	return prestacaoConta.getNrPrestacaoConta();
    }


	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public CiaFilialMercurioService getCiaFilialMercurioService() {
		return ciaFilialMercurioService;
	}

	public void setCiaFilialMercurioService(
			CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}

	public PrestacaoContaService getPrestacaoContaService() {
		return prestacaoContaService;
	}

	public void setPrestacaoContaService(PrestacaoContaService prestacaoContaService) {
		this.prestacaoContaService = prestacaoContaService;
	}

	public IntervaloAwbService getIntervaloAwbService() {
		return intervaloAwbService;
	}

	public void setIntervaloAwbService(IntervaloAwbService intervaloAwbService) {
		this.intervaloAwbService = intervaloAwbService;
	}

	public AwbCanceladoService getAwbCanceladoService() {
		return awbCanceladoService;
	}

	public void setAwbCanceladoService(AwbCanceladoService awbCanceladoService) {
		this.awbCanceladoService = awbCanceladoService;
	}

	public ValorPrestacaoContaService getValorPrestacaoContaService() {
		return valorPrestacaoContaService;
	}

	public void setValorPrestacaoContaService(
			ValorPrestacaoContaService valorPrestacaoContaService) {
		this.valorPrestacaoContaService = valorPrestacaoContaService;
	}

	public IcmsPrestacaoService getIcmsPrestacaoService() {
		return icmsPrestacaoService;
	}

	public void setIcmsPrestacaoService(IcmsPrestacaoService icmsPrestacaoService) {
		this.icmsPrestacaoService = icmsPrestacaoService;
	}

	public ParametroFilialService getParametroFilialService() {
		return parametroFilialService;
	}

	public void setParametroFilialService(
			ParametroFilialService parametroFilialService) {
		this.parametroFilialService = parametroFilialService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}



	public void setFaturamentoCiaAereaService(FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.faturamentoCiaAereaService = faturamentoCiaAereaService;
	}



	public FaturamentoCiaAereaService getFaturamentoCiaAereaService() {
		return faturamentoCiaAereaService;
	}
}