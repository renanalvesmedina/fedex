package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DificuldadeColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaRateioFreteCarreteiroCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.RateioFreteCarreteiroCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.RateioFreteCarreteiroCeDAO;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Service responsável por calculos de rateio de frete carreteiro de
 * coleta/entrega.
 * @author AlessandroSF
 */
public class RateioFreteCarreteiroCeService extends CrudService<RateioFreteCarreteiroCE, Long> {

	
	private static final String PS_REFERENCIA_CALCULO = "PS_REFERENCIA_CALCULO";
	private static final String ID_DOCUMENTO = "ID_DOCUMENTO";
	private static final String TP_COLETA = "C";
	private static final String TP_COLETA_PARCEIRA = "CP";

	private DoctoServicoService doctoServicoService;
	private PedidoColetaService pedidoColetaService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private NotaCreditoPadraoService notaCreditoPadraoService;
	private NotaCreditoService notaCreditoService;
	private ControleCargaService controleCargaService;
	private RateioFreteCarreteiroCeDAO rateioFreteCarreteiroCeDAO;
	private DescontoRfcService descontoRfcService;
	private EnderecoPessoaService enderecoPessoaService;
	private DificuldadeColetaEntregaService dificuldadeColetaEntregaService;
	private Map<Long, NotaCredito> notas;
	private Map<Long, Long> coletasId;
	private BigDecimal descontoReciboProporcional; 
	private BigDecimal descontoFixoProporcional;   
	private BigDecimal acrescimoReciboComplementarProporcional;
	private BigDecimal descontoReciboInss;
	private BigDecimal descontoReciboIr;
	private BigDecimal capacidadeVeiculo;
	private List<String> parcelasDificuldade;
	
	private RateioFreteCarreteiroHelper helper;
	
	public void execute(Long idReciboFreteCarreteiro) {
		
		List<NotaCredito> notasPadrao = new ArrayList<NotaCredito>();
		List<NotaCredito> notasAntigas = new ArrayList<NotaCredito>();		
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = new HashMap<Long, List<ParcelaRateioFreteCarreteiroCE>>();		
		coletasId = new HashMap<Long, Long>();
		notas = new HashMap<Long, NotaCredito>();
		parcelasDificuldade =  buscaParcelasDificuldade();
		
		ReciboFreteCarreteiro recibo = reciboFreteCarreteiroService.findByIdCustom(idReciboFreteCarreteiro);
		
		
		
		if(recibo == null){
			throw new BusinessException("Recibo não encontrado");
		}

		List<Long> ids = separaNotas(notasAntigas,notasPadrao,recibo);
		
		descontoReciboProporcional 				= buscarDescontoProporcional(recibo);
		descontoFixoProporcional   				= buscarDescontoFixoProporcional(recibo);
		acrescimoReciboComplementarProporcional = buscarAcrescimoReciboComplementarProporcional(recibo);
		descontoReciboInss 						= buscarDescontoInss(recibo);
		descontoReciboIr 						= buscarDescontoIr(recibo);
		
		helper = new RateioFreteCarreteiroHelper();
		helper.setCapacidadeVeiculo(capacidadeVeiculo);
		
		calculaRateioCalculo1e2(notasAntigas, rateio);

		calculaRateioCalculoPadrao(notasPadrao, rateio);

		rateioFreteCarreteiroCeDAO.removeByNotaCredito(ids);

		List<RateioFreteCarreteiroCE> rateioList = montaListRateio(rateio,recibo);

		rateioFreteCarreteiroCeDAO.storeAll(rateioList);

	}


	private List<String> buscaParcelasDificuldade() {
		List<String> parcelasDificuldade = new ArrayList<String>();		
		parcelasDificuldade.add(RateioFreteCarreteiroHelper.PARCELA_REGRA_1_DIFICULDADE_ENTREGA);
		parcelasDificuldade.add(RateioFreteCarreteiroHelper.PARCELA_REGRA_2_DIFICULDADE_ENTREGA);
		parcelasDificuldade.add(RateioFreteCarreteiroHelper.PARCELA_REGRA_3_DIFICULDADE_ENTREGA);
		return parcelasDificuldade;
	}
	
	  private BigDecimal buscarCapacidadeMediaTransporte(ControleCarga controleCarga, List<NotaCreditoParcela> parcelas) {
		  boolean buscarValor = false;
		  for (NotaCreditoParcela notaCreditoParcela : parcelas) {
			  String tpCalculo = notaCreditoParcela.getParcelaTabelaCe().getTabelaColetaEntrega().getTpCalculo().getValue();
			  String tpParcela = notaCreditoParcela.getParcelaTabelaCe().getTpParcela().getValue();
			  
				if("C1".equals(tpCalculo) && tpParcela.equals("FP") ){
					buscarValor = true;
					break;
				}
		  }
		  if(buscarValor){
			  return controleCargaService.findCapacidadeMediaMeioTransporteControleCarga(controleCarga);
		  }
		  return BigDecimal.ZERO;
	    }
	
	private BigDecimal buscarDescontoIr(ReciboFreteCarreteiro recibo) {
		if(recibo.getVlIrrf() == null || recibo.getVlIrrf().equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		return recibo.getVlIrrf(); 	
	}

	private BigDecimal buscarDescontoInss(ReciboFreteCarreteiro recibo) {
		if(recibo.getVlInss() == null || recibo.getVlInss().equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO; 
		}
		return recibo.getVlInss(); 	
	}

	@SuppressWarnings("unchecked")
	private List<Long> separaNotas(List<NotaCredito> notasAntigas, List<NotaCredito> notasPadrao, ReciboFreteCarreteiro recibo) {
		List<Long> ids = new ArrayList<Long>();
		for (NotaCredito nota : (List<NotaCredito>) recibo.getNotaCreditos()) {
			ids.add(nota.getIdNotaCredito());
			if (nota.getTpNotaCredito() == null) {
				notasAntigas.add(nota);
				buscaColetas(nota);
			} else {
				notasPadrao.add(nota);
				buscaColetasPadrao(nota);
			}
		}
		return ids;
	}
	
	private void buscaColetas(NotaCredito nota) {
		List<Map<String, Object>> coletas = doctoServicoService.findColeatsByIdNotaCreditoRateio(nota.getIdNotaCredito());
		
		for (Map<String, Object> map : coletas) {
			coletasId.put(((BigDecimal) map.get("ID_DOCUMENTO")).longValue(),((BigDecimal) map.get("ID_PEDIDO_COLETA")).longValue());
		} 
	}
	
	private void buscaColetasPadrao(NotaCredito nota) {
		List<Map<String, Object>> docts;

		Long idNotaCredito = nota.getIdNotaCredito();
		DomainValue tpNota = new DomainValue(TP_COLETA);
		DomainValue tpNotaP = new DomainValue(TP_COLETA_PARCEIRA);

		if (tpNota.getValue().equals(nota.getTpNotaCredito().getValue()) || tpNotaP.getValue().equals(nota.getTpNotaCredito().getValue())) {
			docts = doctoServicoService.findColeatsByIdNotaCredito(idNotaCredito);
			
			for (Map<String, Object> map : docts) {
				coletasId.put(((BigDecimal) map.get("ID_DOCUMENTO")).longValue(),((BigDecimal) map.get("ID_PEDIDO_COLETA")).longValue());
			}
			
		}
	}
	
	
	private BigDecimal buscarDescontoProporcional(ReciboFreteCarreteiro recibo) {
		ParcelaDescontoRfc parcela = descontoRfcService.findParcelaByRecibo(recibo);
		
		if(parcela == null || parcela.getVlParcela().equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		return parcela.getVlParcela(); 		
	}
	
	private BigDecimal buscarDescontoFixoProporcional(ReciboFreteCarreteiro recibo) {
		
		BigDecimal vlDesconto = recibo.getVlDesconto();
		
		if(vlDesconto == null ||vlDesconto.equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		
		ParcelaDescontoRfc parcela = descontoRfcService.findParcelaByRecibo(recibo);
		
		if(parcela != null && parcela.getVlParcela().equals(vlDesconto)){
			return BigDecimal.ZERO;
		}else if(parcela != null ){
			vlDesconto = vlDesconto.subtract(parcela.getVlParcela());
		}
		
		if(vlDesconto.compareTo(BigDecimal.ZERO) <=0){
			return BigDecimal.ZERO;
		}
		
		return vlDesconto;		
	}
	

	private BigDecimal buscarAcrescimoReciboComplementarProporcional(ReciboFreteCarreteiro recibo) {
		@SuppressWarnings("unchecked")
		List<ReciboFreteCarreteiro> list = reciboFreteCarreteiroService.findRecibosComplementares(recibo.getIdReciboFreteCarreteiro());
		
		BigDecimal somatorioRecibo = BigDecimal.ZERO;
		List<String> status = new ArrayList<String>();
		status.add("AJ");
		status.add("EJ");
		status.add("PA");
		
		if(list == null || list.isEmpty()){
			return BigDecimal.ZERO;
		}else{
			for (ReciboFreteCarreteiro reciboFreteCarreteiro : list) {
				if(status.contains(reciboFreteCarreteiro.getTpSituacaoRecibo().getValue())){
					somatorioRecibo = somatorioRecibo.add(reciboFreteCarreteiro.getVlLiquido());
				}
			}
		}
		return somatorioRecibo;
	}

	
	private void calculaRateioCalculo1e2(List<NotaCredito> notasAntigas, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		for (NotaCredito notaCredito : notasAntigas) {
			rateio.putAll(executarRateioCalculo1e2(notaCredito));			
		}
	}
	
	private Map<Long, List<ParcelaRateioFreteCarreteiroCE>> executarRateioCalculo1e2(NotaCredito notaCredito) {
		
		List<Map<String, Object>> docts   = doctoServicoService.findDoctsManifestoEntregaByNotaCredito(notaCredito.getIdNotaCredito());
		List<Map<String, Object>> coletas = doctoServicoService.findColeatsByIdNotaCreditoRateio(notaCredito.getIdNotaCredito());
		
		Map<Long, String> cache = new HashMap<Long, String>();
		
		for (Map<String, Object> map : coletas) {
			Long idColeta = ((BigDecimal) map.get("ID_PEDIDO_COLETA")).longValue();
			
			String endereco = "";
			
			if(cache.containsKey(idColeta)){
				endereco = cache.get(idColeta);
			}else{
				endereco = getEnderecoByPedidoColeta(map, idColeta);
				cache.put(idColeta,endereco);
			}
			
			map.put("ENDERECO", endereco);
			
			coletasId.put(((BigDecimal) map.get("ID_DOCUMENTO")).longValue(),idColeta);			
			
		}
		
		docts.addAll(coletas);
		
		Map<String,DificuldadeColetaEntrega> dificuldadesCache = new HashMap<String, DificuldadeColetaEntrega>();
		
		for (Map<String, Object> map : docts) {
			if( map.containsKey("COLETA")){						
				addDificuldadeColeta(dificuldadesCache, map);
			}else{
				addDificuldadeEntrega(dificuldadesCache, map);
			}
			
			if(map.get(PS_REFERENCIA_CALCULO) == null){
				map.put(PS_REFERENCIA_CALCULO ,RateioFreteCarreteiroHelper.getMaiorPeso((BigDecimal) map.get("PS_AFORADO"), (BigDecimal) map.get("PS_REAL"),(BigDecimal) map.get("PS_AFERIDO"),BigDecimal.ZERO));
			}
		}
		
		
		List<NotaCreditoParcela> parcelas = notaCreditoService.findParcelasNotaCredito(notaCredito.getIdNotaCredito());
		
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> parcelaRateio = new HashMap<Long, List<ParcelaRateioFreteCarreteiroCE>>();

		startListRateio(docts, parcelaRateio, notaCredito);
		
		capacidadeVeiculo = buscarCapacidadeMediaTransporte(notaCredito.getControleCarga() , parcelas);
		helper.setCapacidadeVeiculo(capacidadeVeiculo);
		
		helper.calculaParcelasC1E2(docts,  parcelas, parcelaRateio,notaCredito);		
		
		helper.calculaParcelasAdicionais(buscaRateioParcial(parcelaRateio),notaCredito,parcelaRateio,buscaTotalRateio(parcelaRateio),docts);
		
		return parcelaRateio;

	}


	/**
	 * @param map
	 * @param idColeta
	 * @return
	 */
	private String getEnderecoByPedidoColeta(Map<String, Object> map, Long idColeta) {
		@SuppressWarnings("unused")
		String endereco;
		PedidoColeta p = pedidoColetaService.findById(idColeta);
		
		Long idPessoa = p.getCliente().getPessoa().getIdPessoa();
		
		YearMonthDay dataVigencia  = JTDateTimeUtils.convertDataStringToYearMonthDay((String)map.get("DH_EMISSAO"));
		EnderecoPessoa c = enderecoPessoaService.findEnderecoPeriodo(idPessoa,dataVigencia );
		
		if(c == null){
			c = p.getEnderecoPessoa();
		}
		
		endereco = ConhecimentoUtils.getEnderecoEntregaReal(c);
		return endereco;
	}

	
	

	private List<RateioFreteCarreteiroCE> buscaRateioParcial(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> parcelaRateio) {
		List<RateioFreteCarreteiroCE> rateioList = new ArrayList<RateioFreteCarreteiroCE>();
		for (Long key : parcelaRateio.keySet()) {
			RateioFreteCarreteiroCE rateioFrete = new RateioFreteCarreteiroCE();
			
			if (coletasId.containsKey(key)) {
				DoctoServico docto = new DoctoServico();
				docto.setIdDoctoServico(key);
				rateioFrete.setDoctoServico(docto);
				
				PedidoColeta pedido = pedidoColetaService.findById(coletasId.get(key));
				rateioFrete.setPedidoColeta(pedido);
			}else{
				DoctoServico docto = new DoctoServico();
				docto.setIdDoctoServico(key);
				rateioFrete.setDoctoServico(docto);
			}
			
			
			List<ParcelaRateioFreteCarreteiroCE> parcelas = parcelaRateio.get(key);
			BigDecimal total = somaParcelas(parcelas);
			
			rateioFrete.setVlTotal(total);
			rateioList.add(rateioFrete);
		}
		return rateioList;
	}


	/**
	 * @param parcelas
	 * @return
	 */
	private BigDecimal somaParcelas(List<ParcelaRateioFreteCarreteiroCE> parcelas) {
		BigDecimal total = BigDecimal.ZERO;
		for (ParcelaRateioFreteCarreteiroCE parcelaRateioFreteCarreteiroCE : parcelas) {		
			if(isParcelaDificuldade(parcelaRateioFreteCarreteiroCE.getTpValor())){
				continue;
			}
			
			total = total.add(parcelaRateioFreteCarreteiroCE.getVlTotal());
		}
		return total;
	}


	private boolean isParcelaDificuldade(String tpValor) {		
		return parcelasDificuldade.contains(tpValor);
	}


	private void calculaRateioCalculoPadrao(List<NotaCredito> notasPadrao, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		for (NotaCredito notaCredito : notasPadrao) {
			rateio.putAll(executarRateioCalculoPadrao(notaCredito));
		}
	}
	
	private Map<Long, List<ParcelaRateioFreteCarreteiroCE>> executarRateioCalculoPadrao(NotaCredito notaCredito) {
		List<Map<String, Object>> docts;

		Long idNotaCredito = notaCredito.getIdNotaCredito();
		DomainValue tpNota = new DomainValue(TP_COLETA);
		DomainValue tpNotaP = new DomainValue(TP_COLETA_PARCEIRA);

		if (tpNota.getValue().equals(notaCredito.getTpNotaCredito().getValue()) || tpNotaP.getValue().equals(notaCredito.getTpNotaCredito().getValue())) {
			docts = doctoServicoService.findColeatsByIdNotaCredito(idNotaCredito);
			
			for (Map<String, Object> map : docts) {
				coletasId.put(((BigDecimal) map.get("ID_DOCUMENTO")).longValue(),((BigDecimal) map.get("ID_PEDIDO_COLETA")).longValue());
			}
			
		} else {
			docts = doctoServicoService.findDoctosByIdNotaCredito(idNotaCredito);
		}

		List<Map<String, Object>> parcelas = notaCreditoPadraoService.findParcelasByIdNotaCredito(idNotaCredito);

		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> parcelaRateio = new HashMap<Long, List<ParcelaRateioFreteCarreteiroCE>>();

		startListRateio(docts, parcelaRateio, notaCredito);
		
		if(docts.isEmpty()){
			return parcelaRateio;
		}
		

		
		BigDecimal quilo = BigDecimal.ZERO;
		Map<String,DificuldadeColetaEntrega> dificuldadesCache = new HashMap<String, DificuldadeColetaEntrega>();

		for (Map<String, Object> map : docts) {
			if( map.containsKey("COLETA")){						
				addDificuldadeColeta(dificuldadesCache, map);
			}else{
				addDificuldadeEntrega(dificuldadesCache, map);
			}
			
			if(map.get(PS_REFERENCIA_CALCULO) == null){
				map.put(PS_REFERENCIA_CALCULO ,RateioFreteCarreteiroHelper.getMaiorPeso((BigDecimal) map.get("PS_AFORADO"), (BigDecimal) map.get("PS_REAL"),(BigDecimal) map.get("PS_AFERIDO"),BigDecimal.ZERO));
			}
			
			quilo = quilo.add((BigDecimal) map.get(PS_REFERENCIA_CALCULO));
		}

		if(BigDecimal.ZERO.equals(quilo)){
			quilo = BigDecimal.ONE;
		}
		
		helper.calculaParcelasPadrao(docts, parcelas, parcelaRateio, quilo,notaCredito);
		
		helper.calculaParcelasAdicionais(buscaRateioParcial(parcelaRateio),notaCredito,parcelaRateio,buscaTotalRateio(parcelaRateio),docts);
		
		return parcelaRateio;

	}


	/**
	 * @param dificuldadesCache
	 * @param map
	 */
	private void addDificuldadeEntrega(Map<String, DificuldadeColetaEntrega> dificuldadesCache, Map<String, Object> map) {
		String chave = "E"+ map.get("ID_CLIENTE_REMETENTE");
		DificuldadeColetaEntrega dificuldade = null;
		if(dificuldadesCache.containsKey(chave)){
			dificuldade = dificuldadesCache.get(chave);
			
		}else{
			dificuldade = dificuldadeColetaEntregaService.findByCliente( ((BigDecimal) map.get("ID_CLIENTE_REMETENTE")).longValue());
			dificuldadesCache.put(chave, dificuldade);
		}
		map.put(RateioFreteCarreteiroHelper.DIFICULDADE, new BigDecimal(dificuldade.getNrFatorEntrega()));
	}


	/**
	 * @param dificuldadesCache
	 * @param map
	 */
	private void addDificuldadeColeta(Map<String, DificuldadeColetaEntrega> dificuldadesCache, Map<String, Object> map) {
		String chave = "C"+ map.get("ID_CLIENTE");
		DificuldadeColetaEntrega dificuldade = null;
		if(dificuldadesCache.containsKey(chave)){
			dificuldade = dificuldadesCache.get(chave);
		}else{
			dificuldade = dificuldadeColetaEntregaService.findByCliente( ((BigDecimal) map.get("ID_CLIENTE")).longValue());
			dificuldadesCache.put(chave, dificuldade);
		}
		
		map.put(RateioFreteCarreteiroHelper.DIFICULDADE, new BigDecimal(dificuldade.getNrFatorColeta()));
	}
	
	private BigDecimal buscaTotalRateio(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		
		BigDecimal totalRateio = BigDecimal.ZERO;
		for (Long key : rateio.keySet()) {
			
			List<ParcelaRateioFreteCarreteiroCE> parcelas = rateio.get(key);
			for (ParcelaRateioFreteCarreteiroCE parcelaRateioFreteCarreteiroCE : parcelas) {
				if(isParcelaDificuldade(parcelaRateioFreteCarreteiroCE.getTpValor())){
					continue;
				}
				totalRateio = totalRateio.add(parcelaRateioFreteCarreteiroCE.getVlTotal());
			}
		}
		return totalRateio;
	}

	
	private List<RateioFreteCarreteiroCE> montaListRateio(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, ReciboFreteCarreteiro reciboFreteCarreteiro) {
		List<RateioFreteCarreteiroCE> rateioListParcial = new ArrayList<RateioFreteCarreteiroCE>();		
		BigDecimal totalRateio = BigDecimal.ZERO;
		
		totalRateio = buscaTotalRateio(rateio);
		rateioListParcial = buscaRateioParcial(rateio);
		
		for (RateioFreteCarreteiroCE rateioFreteCarreteiroCE : rateioListParcial) {
			BigDecimal vl =  rateioFreteCarreteiroCE.getVlTotal();
			
			if (descontoReciboIr != null && !descontoReciboIr.equals(BigDecimal.ZERO)) {
				RateioFreteCarreteiroHelper.addDescontoRecibo(descontoReciboIr, RateioFreteCarreteiroHelper.PARCELA_DESCONTO_RECIBO_IR , totalRateio, rateioFreteCarreteiroCE, vl, rateio);
			}
			
			if (descontoReciboInss != null && !descontoReciboInss.equals(BigDecimal.ZERO)) {
				RateioFreteCarreteiroHelper.addDescontoRecibo(descontoReciboInss, RateioFreteCarreteiroHelper.PARCELA_DESCONTO_RECIBO_INSS , totalRateio, rateioFreteCarreteiroCE, vl , rateio);
			}
			
			if (descontoFixoProporcional != null && !descontoFixoProporcional.equals(BigDecimal.ZERO)) {
				RateioFreteCarreteiroHelper.addDescontoRecibo(descontoFixoProporcional, RateioFreteCarreteiroHelper.PARCELA_DESCONTO_RECIBO_FIXO , totalRateio, rateioFreteCarreteiroCE, vl , rateio);
			}
			
			if (descontoReciboProporcional != null && !descontoReciboProporcional.equals(BigDecimal.ZERO)) {
				RateioFreteCarreteiroHelper.addDescontoRecibo(descontoReciboProporcional, RateioFreteCarreteiroHelper.PARCELA_DESCONTO_RECIBO_PARCELADO , totalRateio, rateioFreteCarreteiroCE, vl, rateio);
			}
			
			if (acrescimoReciboComplementarProporcional != null && !acrescimoReciboComplementarProporcional.equals(BigDecimal.ZERO)) {
				RateioFreteCarreteiroHelper.addAcrescimoRecibo(acrescimoReciboComplementarProporcional, RateioFreteCarreteiroHelper.PARCELA_ACRESCIMO_RECIBO_COMPLEMENTAR , totalRateio, rateioFreteCarreteiroCE, vl, rateio);
			}
		}
		
		
		
		List<RateioFreteCarreteiroCE> rateioList = new ArrayList<RateioFreteCarreteiroCE>();		
		
		for (Long key : rateio.keySet()) {
			RateioFreteCarreteiroCE rateioFrete = new RateioFreteCarreteiroCE();
			NotaCredito nota = getNota(key);
			
			if (coletasId.containsKey(key)) {
				DoctoServico docto = new DoctoServico();
				docto.setIdDoctoServico(key);
				rateioFrete.setDoctoServico(docto);
				
				PedidoColeta pedido = pedidoColetaService.findById(coletasId.get(key));
				rateioFrete.setPedidoColeta(pedido);
			}else{
				DoctoServico docto = new DoctoServico();
				docto.setIdDoctoServico(key);
				rateioFrete.setDoctoServico(docto);
			}
			
			
			List<ParcelaRateioFreteCarreteiroCE> parcelas = rateio.get(key);
			BigDecimal total = somaParcelas(parcelas);
			BigDecimal totalPadraoDificuldade = BigDecimal.ZERO;
			
			for (ParcelaRateioFreteCarreteiroCE parcelaRateioFreteCarreteiroCE : parcelas) {
				parcelaRateioFreteCarreteiroCE.setRateioFreteCarreteiroCE(rateioFrete);
				if(isParcelaDificuldade(parcelaRateioFreteCarreteiroCE.getTpValor()) && parcelaRateioFreteCarreteiroCE.getTpValor().equals(RateioFreteCarreteiroHelper.PARCELA_REGRA_1_DIFICULDADE_ENTREGA)){
					totalPadraoDificuldade = totalPadraoDificuldade.add(parcelaRateioFreteCarreteiroCE.getVlTotal());
				}
			}
			
			rateioFrete.setVlTotal(total);
			rateioFrete.setDhRateio(new DateTime());
			rateioFrete.setVlRateioDE(totalPadraoDificuldade);
			rateioFrete.setReciboFreteCarreteiro(reciboFreteCarreteiro);
			rateioFrete.setNotaCredito(nota);
			rateioFrete.setParcelas(parcelas);
			rateioList.add(rateioFrete);
		}
		return rateioList;
	}


	private NotaCredito getNota(Long key) {
		return notas.get(key);
	}

	private void startListRateio(List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, NotaCredito nc) {
		for (Map<String, Object> doc : docts) {
			rateio.put(((BigDecimal) doc.get(ID_DOCUMENTO)).longValue(), new ArrayList<ParcelaRateioFreteCarreteiroCE>());
			notas.put(((BigDecimal) doc.get(ID_DOCUMENTO)).longValue(), nc);
		}
	}

	public TabelaFreteCarreteiroCe findById(java.lang.Long id) {
		return (TabelaFreteCarreteiroCe) super.findById(id);
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public ReciboFreteCarreteiroService getReciboFreteCarreteiroService() {
		return reciboFreteCarreteiroService;
	}

	public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public NotaCreditoPadraoService getNotaCreditoPadraoService() {
		return notaCreditoPadraoService;
	}

	public void setNotaCreditoPadraoService(NotaCreditoPadraoService notaCreditoPadraoService) {
		this.notaCreditoPadraoService = notaCreditoPadraoService;
	}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

	public RateioFreteCarreteiroCeDAO getRateioFreteCarreteiroCeDAO() {
		return rateioFreteCarreteiroCeDAO;
	}

	public void setRateioFreteCarreteiroCeDAO(RateioFreteCarreteiroCeDAO rateioFreteCarreteiroCeDAO) {
		this.rateioFreteCarreteiroCeDAO = rateioFreteCarreteiroCeDAO;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public DescontoRfcService getDescontoRfcService() {
		return descontoRfcService;
	}

	public void setDescontoRfcService(DescontoRfcService descontoRfcService) {
		this.descontoRfcService = descontoRfcService;
	}


	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}


	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}


	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}


	public void setDificuldadeColetaEntregaService(DificuldadeColetaEntregaService dificuldadeColetaEntregaService) {
		this.dificuldadeColetaEntregaService = dificuldadeColetaEntregaService;
	}
}