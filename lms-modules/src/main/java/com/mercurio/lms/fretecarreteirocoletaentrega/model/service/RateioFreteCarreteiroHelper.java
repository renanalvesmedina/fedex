package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaRateioFreteCarreteiroCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.RateioFreteCarreteiroCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.FaixaPeso;
import com.mercurio.lms.fretecarreteirocoletaentrega.util.ParametrosCalculoRateio;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * RateioFreteCarreteiroHelper, classe que executa os calculos do rateio
 * @author AlessandroSF
 *
 */
public class RateioFreteCarreteiroHelper {

	// ACRESCIMO/DESCONTO
	public static final String PARCELA_DESCONTO_NOTA_FIXO = "DNF";
	public static final String PARCELA_DESCONTO_USO_EQUIPAMENTO = "DUE";
	public static final String PARCELA_ACRESCIMO_RECIBO_COMPLEMENTAR = "ARC";
	public static final String PARCELA_DESCONTO_RECIBO_FIXO = "DRF";
	public static final String PARCELA_DESCONTO_RECIBO_IR = "DIR";
	public static final String PARCELA_DESCONTO_RECIBO_INSS = "DIN";
	public static final String PARCELA_DESCONTO_RECIBO_PARCELADO = "DRP";
	// C1E2
	public static final String PARCELA_PERCENTUAL_SOBRE_MERCADORIA_C1E2 = "PC";
	public static final String PARCELA_PERCENTUAL_SOBRE_FRETE_C1E2 = "PF";
	public static final String PARCELA_PERCENTUAL_SOBRE_VALOR_C1E2 = "PV";
	public static final String PARCELA_FRETE_PESO_C1E2 = "FP";
	public static final String PARCELA_QUILOMETROS_C1E2 = "QU";
	public static final String PARCELA_DIARIA_C1E2 = "DH";
	public static final String PARCELA_EVENTO_C1E2 = "EV";
	public static final String PARCELA_VOLUME_C1E2 = "VO";
	// PADRAO
	public static final String PARCELA_FAIXA_PESO_QUILO = "FPK";
	public static final String PARCELA_FAIXA_PESO = "FPD";
	public static final String PARCELA_AJUDANTE = "VAJ";
	public static final String PARCELA_CAPATAZIA_CLIENTE = "CCL";
	public static final String PARCELA_FRETE_BRUTO_VALOR = "FBV";
	public static final String PARCELA_FRETE_BRUTO_PERCENTUAL = "FBP";
	public static final String PARCELA_VOLUME = "VOL";
	public static final String PARCELA_VALOR_MECADORIA_VALOR = "MEV";
	public static final String PARCELA_VALOR_MECADORIA_PERCENTUAL = "MEP";
	public static final String PARCELA_CONHECIMENTO = "CON";
	public static final String PARCELA_EVENTO = "EVE";
	public static final String PARCELA_DEDICADA = "DED";
	public static final String PARCELA_HORA = "HOR";
	public static final String PARCELA_TRANSFERENCIA = "TRA";
	public static final String PARCELA_LOCACAO_CARRETA = "LOC";
	public static final String PARCELA_PERNOITE = "PNO";
	public static final String PARCELA_SAIDA = "PRE";
	public static final String PARCELA_DIARIA = "DIA";
	public static final String PARCELA_QUILOMETRO = "KME";
	// PREMIO
	public static final String PARCELA_PREMIO_EVENTO = "PEV";
	public static final String PARCELA_PREMIO_VALOR_MECADORIA = "PME";
	public static final String PARCELA_PREMIO_FRETE_BRUTO = "PFB";
	public static final String PARCELA_PREMIO_VOLUME = "PVO";
	public static final String PARCELA_PREMIO_SAIDA = "PSA";
	public static final String PARCELA_PREMIO_DIARIA = "PDI";
	public static final String PARCELA_PREMIO_CTE = "PCT";
	
	//REGRA PARCELA DIFICULDADE
	public static final String PARCELA_REGRA_1_DIFICULDADE_ENTREGA = "R1D";
	public static final String PARCELA_REGRA_2_DIFICULDADE_ENTREGA = "R2D";
	public static final String PARCELA_REGRA_3_DIFICULDADE_ENTREGA = "R3D";
	public static final String DIFICULDADE = "DIFICULDADE";
	

	public static final String PS_REFERENCIA_CALCULO = "PS_REFERENCIA_CALCULO";
	public static final String VL_MERCADORIA = "VL_MERCADORIA";
	public static final String VL_FRETE_LIQUIDO = "VL_FRETE_LIQUIDO";
	public static final String QT_VOLUMES = "QT_VOLUMES";
	public static final String ID_DOCUMENTO = "ID_DOCUMENTO";
	public static final String QT_TOTAL = "QT_TOTAL";
	public static final String TP_VALOR = "TP_VALOR";
	public static final String VL_TOTAL = "VL_TOTAL";
	public static final String ENDERECO = "ENDERECO";
	public static final BigDecimal PARCELA_NEGATIVA = new BigDecimal(-1);
	private static final String VL_TOTAL_PARCELAS = "VL_TOTAL_PARCELAS";
	private static final String DOCUMENTO = "Documento";
	private static final String PESO = "KG";
	private static final String PARCELA_ACRESCIMO_NOTA_FIXA_S = "ANO";
	private static final Object VL_FRETE_BRUTO = "VL_TOTAL_DOC_SERVICO";

	private BigDecimal capacidadeVeiculo;
	
	public void calculaParcelasC1E2(List<Map<String, Object>> docts, List<NotaCreditoParcela> parcelas,	Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, NotaCredito notaCredito) {
		
		List<Long> parcelasEspeciais = new ArrayList<Long>();
		
		for (NotaCreditoParcela notaCreditoParcela : parcelas) {
			if(BigDecimal.ZERO.equals(notaCreditoParcela.getVlNotaCreditoParcela()) || BigDecimal.ZERO.equals(notaCreditoParcela.getQtNotaCreditoParcela())){
				continue;
			}
			
			Long idCliente  = findIdCliente(notaCreditoParcela);
			if(idCliente != null){
				parcelasEspeciais.add(idCliente);
			}
		}
		
		BigDecimal totalNota =  BigDecimal.ZERO;
		
		for (NotaCreditoParcela notaCreditoParcela : parcelas) {
			
			if(BigDecimal.ZERO.equals(notaCreditoParcela.getVlNotaCreditoParcela()) || BigDecimal.ZERO.equals(notaCreditoParcela.getQtNotaCreditoParcela())){
				continue;
			}
			
			String tpParcela = notaCreditoParcela.getParcelaTabelaCe().getTpParcela().getValue();
			
			Long idCliente  = findIdCliente(notaCreditoParcela);
			
			BigDecimal quilo = getSomatorio(docts, parcelasEspeciais, tpParcela, idCliente);

			
			ParametrosCalculoRateio parametrosCalculoRateio = new ParametrosCalculoRateio(docts, rateio, notaCreditoParcela, tpParcela, quilo , idCliente, parcelasEspeciais);
			
			calculaParcela1e2Diaria(parametrosCalculoRateio);
			calculaParcela1e2Evento(parametrosCalculoRateio);
			calculaParcela1e2FretePeso(parametrosCalculoRateio);
			calculaParcela1e2ValorFrete(parametrosCalculoRateio);
			calculaParcela1e2Valor(parametrosCalculoRateio);
			calculaParcela1e2Quilometros(parametrosCalculoRateio);
			calculaParcela1e2Volume(parametrosCalculoRateio);
			
			totalNota = totalNota.add(notaCreditoParcela.getQtNotaCreditoParcela().multiply(notaCreditoParcela.getVlNotaCreditoParcela()));
		}
		
		calculaParcelaDificuldadeEntrega(docts, rateio, notaCredito, totalNota);
		
	}

	/**
	 * @param docts
	 * @param rateio
	 * @param notaCredito
	 * @param totalNota
	 */
	private void calculaParcelaDificuldadeEntrega(List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio,	NotaCredito notaCredito, BigDecimal totalNota) {
		BigDecimal vlAcrescimo = notaCredito.getVlAcrescimo();
		BigDecimal vlDesconto = notaCredito.getVlDesconto();
		BigDecimal vlDescontoEquipamento = notaCredito.getVlDescUsoEquipamento();
		
		totalNota = addTotal(totalNota, vlAcrescimo);
		totalNota = addTotal(totalNota, vlDesconto != null ? vlDesconto.multiply(PARCELA_NEGATIVA) : null);
		totalNota = addTotal(totalNota, vlDescontoEquipamento!= null ? vlDescontoEquipamento.multiply(PARCELA_NEGATIVA) : null);
		
		
		
		if(!BigDecimal.ZERO.equals(totalNota)){
			calculaParcelaRegraDificuldadeEntrega(docts, rateio,totalNota);
		}
	}

	/**
	 * @param totalNota
	 * @param vlAcrescimo
	 * @return
	 */
	private BigDecimal addTotal(BigDecimal totalNota, BigDecimal vlAcrescimo) {
		if(vlAcrescimo != null && vlAcrescimo.compareTo(BigDecimal.ZERO) > 0){
			totalNota = totalNota.add(vlAcrescimo); 
		}
		return totalNota;
	}

	/**
	 * @param docts
	 * @param parcelasEspeciais
	 * @param tpParcela
	 * @param idCliente
	 * @return
	 */
	private BigDecimal getSomatorio(List<Map<String, Object>> docts, List<Long> parcelasEspeciais, String tpParcela, Long idCliente) {
		BigDecimal quilo = BigDecimal.ZERO;
		
		for (Map<String, Object> map : docts) {
			if(aplicaParcela(idCliente, map, parcelasEspeciais, tpParcela)){
				
				if(map.get(PS_REFERENCIA_CALCULO) == null){
					map.put(PS_REFERENCIA_CALCULO,BigDecimal.ZERO);
				}
				quilo = quilo.add((BigDecimal) map.get(PS_REFERENCIA_CALCULO));
			}
			
		}
		
		if(BigDecimal.ZERO.equals(quilo)){
			quilo = BigDecimal.ONE;
		}
		return quilo;
	}

	public void calculaParcelasPadrao(List<Map<String, Object>> docts, List<Map<String, Object>> parcelas,	Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilo, NotaCredito notaCredito) {
		BigDecimal totalNota =  BigDecimal.ZERO;
		for (Map<String, Object> p : parcelas) {
			
			BigDecimal vlParcela = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtParcela = (BigDecimal) p.get(QT_TOTAL);
			
			if(BigDecimal.ZERO.equals(vlParcela) || BigDecimal.ZERO.equals(qtParcela)){
				continue;
			}
			
			// por controleCarga
			calculaPacelaControleCarga(docts, p, PARCELA_QUILOMETRO, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_DIARIA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_SAIDA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_PERNOITE, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_LOCACAO_CARRETA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_TRANSFERENCIA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_HORA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_DEDICADA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_PREMIO_CTE, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_PREMIO_DIARIA, rateio, quilo);
			calculaPacelaControleCarga(docts, p, PARCELA_PREMIO_SAIDA, rateio, quilo);

			// por documento
			calculaPacelaConhecimento(docts, p, rateio);
			calculaParcelaEvento(docts, p, rateio, quilo);
			calculaParcelaVolume(docts, p, rateio);
			calculaParcelaCapataziaCliente(docts, p, rateio, quilo);
			calculaParcelaAjudante(docts, p, rateio, quilo);
			calculaParcelaFaixaPesoDocumento(docts, p, rateio, quilo);
			calculaParcelaFaixaPesoQuilo(docts, p, rateio, quilo);
			calculaParcelaValorMercadoria(docts, p, rateio);
			calculaParcelaValorFreteLiquido(docts, p, rateio);
			calculaParcelaValorFreteBruto(docts, p, rateio);
			
			totalNota = totalNota.add(vlParcela.multiply(qtParcela));
		}
		
		calculaParcelaDificuldadeEntrega(docts, rateio, notaCredito, totalNota);
	}

	public void calculaPacelaConhecimento(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {

		if (PARCELA_CONHECIMENTO.equals(p.get(TP_VALOR))) {
			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			Integer qtd = docts.size();

			if (qtd == 0) {
				qtd = 1;
			}

			BigDecimal parte = valor.multiply(qtotal).divide(new BigDecimal(qtd), 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, PARCELA_CONHECIMENTO, new BigDecimal(qtd));

				addParcelaRateio(rateio, doc, parcela);

			}

		}
	}

	public void calculaParcelaFaixaPesoQuilo(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilos) {

		String tpParcela = PARCELA_FAIXA_PESO_QUILO;
		if (tpParcela.equals(p.get(TP_VALOR))) {
			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorPorQuilo = valor.multiply(qtotal).divide(quilos, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				BigDecimal peso = (BigDecimal) doc.get(PS_REFERENCIA_CALCULO);
				
				BigDecimal parte = peso.multiply(valorPorQuilo).setScale(9, RoundingMode.HALF_UP);

				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, peso);

				addParcelaRateio(rateio, doc, parcela);

			}
		}
	}

	public void calculaParcelaFaixaPesoDocumento(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilo) {

		String tpParcela = PARCELA_FAIXA_PESO;
		if (tpParcela.equals(p.get(TP_VALOR))) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorPorQuilo = valor.multiply(qtotal).divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				
				BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, BigDecimal.ONE);

				addParcelaRateio(rateio, doc, parcela);
			}

		}
	}

	public void calculaParcelaAjudante(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilo) {

		String tpParcela = PARCELA_AJUDANTE;
		if (tpParcela.equals(p.get(TP_VALOR))) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorPorQuilo = valor.multiply(qtotal).divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				
				BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, BigDecimal.ONE);

				addParcelaRateio(rateio, doc, parcela);
			}

		}
	}

	public void calculaParcelaCapataziaCliente(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilo) {

		String tpParcela = PARCELA_CAPATAZIA_CLIENTE;
		if (tpParcela.equals(p.get(TP_VALOR))) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);

			BigDecimal valorPorQuilo = valor.divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				
				BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, BigDecimal.ONE);

				addParcelaRateio(rateio, doc, parcela);

			}

		}
	}

	public void calculaParcelaValorFreteBruto(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {

		String tpParcela = (String) p.get(TP_VALOR);

		if (PARCELA_FRETE_BRUTO_PERCENTUAL.equals(tpParcela) || PARCELA_FRETE_BRUTO_VALOR.equals(tpParcela)
				|| PARCELA_PREMIO_FRETE_BRUTO.equals(tpParcela)) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorMercadoria = BigDecimal.ZERO;

			for (Map<String, Object> map : docts) {
				valorMercadoria = valorMercadoria.add((BigDecimal) map.get(VL_FRETE_BRUTO));				
			}
			if(BigDecimal.ZERO.equals(valorMercadoria)){
				valorMercadoria = BigDecimal.ONE;
			}

			BigDecimal valorPormercadoria = valor.multiply(qtotal).divide(valorMercadoria, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				BigDecimal volume = (BigDecimal) doc.get(VL_FRETE_BRUTO);
				BigDecimal parte = volume.multiply(valorPormercadoria).setScale(9, RoundingMode.HALF_UP);

				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, volume);

				addParcelaRateio(rateio, doc, parcela);

			}
		}
	}

	public void calculaParcelaValorFreteLiquido(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		String tpParcela = (String) p.get(TP_VALOR);

		if ("FLB".equals(tpParcela) || "FLV".equals(tpParcela) || "PFL".equals(tpParcela)) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorMercadoria = BigDecimal.ZERO;

			for (Map<String, Object> map : docts) {
				valorMercadoria = valorMercadoria.add((BigDecimal) map.get(VL_FRETE_LIQUIDO));
			}
			
			if(BigDecimal.ZERO.equals(valorMercadoria)){
				valorMercadoria = BigDecimal.ONE;
			}

			BigDecimal valorPormercadoria = valor.multiply(qtotal).divide(valorMercadoria, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				BigDecimal volume = (BigDecimal) doc.get(VL_FRETE_LIQUIDO);
				BigDecimal parte = volume.multiply(valorPormercadoria).setScale(9, RoundingMode.HALF_UP);

				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, volume);

				addParcelaRateio(rateio, doc, parcela);

			}
		}
	}

	public void calculaParcelaValorMercadoria(List<Map<String, Object>> docts, Map<String, Object> p,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {

		String tpParcela = (String) p.get(TP_VALOR);

		if (PARCELA_VALOR_MECADORIA_PERCENTUAL.equals(tpParcela) || PARCELA_VALOR_MECADORIA_VALOR.equals(tpParcela)
				|| PARCELA_PREMIO_VALOR_MECADORIA.equals(tpParcela)) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorMercadoria = BigDecimal.ZERO;

			for (Map<String, Object> map : docts) {
				valorMercadoria = valorMercadoria.add((BigDecimal) map.get(VL_MERCADORIA));
			}
			if(BigDecimal.ZERO.equals(valorMercadoria)){
				valorMercadoria = BigDecimal.ONE;
			}

			BigDecimal valorPormercadoria = valor.multiply(qtotal).divide(valorMercadoria, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				BigDecimal vlMercadoria = (BigDecimal) doc.get(VL_MERCADORIA);
				BigDecimal parte = vlMercadoria.multiply(valorPormercadoria).setScale(9, RoundingMode.HALF_UP);

				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, vlMercadoria);

				addParcelaRateio(rateio, doc, parcela);
			}
		}
	}

	public void calculaParcelaVolume(List<Map<String, Object>> docts, Map<String, Object> p, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {

		String tpParcela = (String) p.get(TP_VALOR);

		if (PARCELA_VOLUME.equals(tpParcela) || PARCELA_PREMIO_VOLUME.equals(tpParcela)) {

			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal volumes = BigDecimal.ZERO;

			for (Map<String, Object> map : docts) {
				volumes = volumes.add((BigDecimal) map.get(QT_VOLUMES));
			}
			if(BigDecimal.ZERO.equals(volumes)){
				volumes = BigDecimal.ONE;
			}

			BigDecimal valorPorVolume = valor.multiply(qtotal).divide(volumes, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				BigDecimal volume = (BigDecimal) doc.get(QT_VOLUMES);
				BigDecimal parte = volume.multiply(valorPorVolume).setScale(9, RoundingMode.HALF_UP);

				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, volume);

				addParcelaRateio(rateio, doc, parcela);
			}
		}
	}

	public void calculaParcelaEvento(List<Map<String, Object>> docts, Map<String, Object> p, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio,
			BigDecimal quilo) {
		
		String tpParcela = (String) p.get(TP_VALOR);

		if (PARCELA_EVENTO.equals(tpParcela) || PARCELA_PREMIO_EVENTO.equals(tpParcela)) {
			BigDecimal vl	  =  (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qVl	  =  (BigDecimal) p.get(QT_TOTAL);
			
			vl = vl.multiply(qVl).divide(new BigDecimal(docts.size()), 9, RoundingMode.HALF_UP);
			
			Map<String, Integer> enderecos = new HashMap<String, Integer>();
			Map<String, BigDecimal> pesos = new HashMap<String, BigDecimal>();
			for (Map<String, Object> map : docts) {
				String endereco = (String) map.get(ENDERECO);
				
				if (enderecos.containsKey(endereco)) {
					enderecos.put(endereco, enderecos.get(endereco) + 1);
					pesos.put(endereco,pesos.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));
				} else {
					enderecos.put(endereco, 1);
					pesos.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
				}
			}
			
			for (Map<String, Object> doc : docts) {
				
				for (String endereco : enderecos.keySet()) {
					
					if (endereco.equals((String) doc.get(ENDERECO))) {
						Integer qtd = enderecos.get(endereco);
						
						if(qtd == 1){						
							ParcelaRateioFreteCarreteiroCE parcela = generateParcela(vl, tpParcela, new BigDecimal(qtd));
							
							addParcelaRateio(rateio, doc, parcela);
							
						}else{
							BigDecimal vlFracionado = vl.multiply(new BigDecimal(qtd)).setScale(9, RoundingMode.HALF_UP);
							
							BigDecimal valorPorEvento = vlFracionado.divide(pesos.get((String) doc.get(ENDERECO)), 9, RoundingMode.HALF_UP);
							
							BigDecimal parte = valorPorEvento.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
							
							ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, new BigDecimal(qtd));
							
							addParcelaRateio(rateio, doc, parcela);
						}
					}
				}
			}
		}
	}

	public void calculaPacelaControleCarga(List<Map<String, Object>> docts, Map<String, Object> p, String tpParcela,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal quilo) {

		if (tpParcela.equals(p.get(TP_VALOR))) {
			BigDecimal valor = (BigDecimal) p.get(VL_TOTAL);
			BigDecimal qtotal = (BigDecimal) p.get(QT_TOTAL);

			BigDecimal valorPorQuilo = valor.multiply(qtotal).divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {

				BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, BigDecimal.ONE);

				addParcelaRateio(rateio, doc, parcela);

			}

		}
	}

	public void calculaParcela1e2Volume(ParametrosCalculoRateio parametrosCalculoRateio) {
		
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();

		if (PARCELA_VOLUME_C1E2.equals(tpParcela)) {
			BigDecimal qtd = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();
			
			boolean isZero = BigDecimal.ZERO.equals(qtd.multiply(vl));

			BigDecimal volumes = BigDecimal.ZERO;

			for (Map<String, Object> map : docts) {
				if(aplicaParcela(idCliente, map, parcelasEspeciais, tpParcela)){
					volumes = volumes.add((BigDecimal) map.get(QT_VOLUMES));
				}
			}
			if(BigDecimal.ZERO.equals(volumes)){
				volumes= BigDecimal.ONE;
			}
			
			
			BigDecimal valorPorVolume = isZero ? BigDecimal.ZERO : vl.multiply(qtd).divide(volumes, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				if(aplicaParcela(idCliente, doc, parcelasEspeciais, tpParcela)){
					BigDecimal volume = (BigDecimal) doc.get(QT_VOLUMES);
					BigDecimal parte = volume.multiply(valorPorVolume).setScale(9, RoundingMode.HALF_UP);
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : parte, PARCELA_VOLUME_C1E2, volume);
					
					addParcelaRateio(rateio, doc, parcela);
				}

			}
		}
	}

	public void calculaParcela1e2Quilometros(ParametrosCalculoRateio parametrosCalculoRateio) {
		
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		BigDecimal quilo = parametrosCalculoRateio.getQuilo();		
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();

		if (PARCELA_QUILOMETROS_C1E2.equals(tpParcela)) {
			BigDecimal qtd = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();

			boolean isZero = BigDecimal.ZERO.equals(qtd.multiply(vl));
			
			BigDecimal valorPorQuilo = isZero ? BigDecimal.ZERO : vl.multiply(qtd).divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				if(aplicaParcela(idCliente, doc, parcelasEspeciais, tpParcela)){
					BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : parte, PARCELA_QUILOMETROS_C1E2, qtd);
					
					addParcelaRateio(rateio, doc, parcela);
				}
			}
		}
	}
	
	public void calculaParcela1e2ValorFrete(ParametrosCalculoRateio parametrosCalculoRateio) {
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();
		
		if (PARCELA_PERCENTUAL_SOBRE_FRETE_C1E2.equals(tpParcela)) {
			BigDecimal qt = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();
			
			boolean isZero = BigDecimal.ZERO.equals(qt.multiply(vl));
						
			for (Map<String, Object> doc : docts) {
				//se pedido de coleta não executa este calculo
				if(doc.get("ID_PEDIDO_COLETA")!= null){
					continue;
				}
				if(aplicaParcela(idCliente, doc, parcelasEspeciais, tpParcela)){
					BigDecimal vlFrete = (BigDecimal) doc.get(VL_TOTAL_PARCELAS);
					
					ParcelaTabelaCe parcelaTabelaCe = notaCreditoParcela.getParcelaTabelaCe();
					
					BigDecimal valor = getMaiorValor(parcelaTabelaCe.getVlDefinido(),
													 vlFrete.multiply(parcelaTabelaCe.getPcSobreValor()).divide(BigDecimal.valueOf(100)));
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : valor, tpParcela, vlFrete);
					addParcelaRateio(rateio, doc, parcela); 
				}
			}
		}
	}
	
	
	 protected BigDecimal getMaiorValor(BigDecimal... valores) {
	        if (valores != null) {
	            List<BigDecimal> compare = new ArrayList<BigDecimal>(valores.length);

	            for (BigDecimal valor : valores) {
	                if (valor != null && BigDecimalUtils.gtZero(valor)) {
	                    compare.add(valor);
	                }
	            }

	            if (!compare.isEmpty()) {
	                return Collections.max(compare);
	            }
	        }

	        return BigDecimal.ZERO;
	    }

	public void calculaParcela1e2Valor(ParametrosCalculoRateio parametrosCalculoRateio) {
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();
		
		
		if (PARCELA_PERCENTUAL_SOBRE_VALOR_C1E2.equals(tpParcela) || PARCELA_PERCENTUAL_SOBRE_MERCADORIA_C1E2.equals(tpParcela)) {
			
			BigDecimal qtd = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();
			
			boolean isZero = BigDecimal.ZERO.equals(qtd.multiply(vl));
			
			for (Map<String, Object> doc : docts) {
				//se pedido de coleta não executa este calculo
				if(doc.get("ID_PEDIDO_COLETA")!= null){
					continue;
				}
				if(aplicaParcela(idCliente, doc, parcelasEspeciais, tpParcela)){
					BigDecimal vlFrete = (BigDecimal) doc.get(VL_MERCADORIA);
					BigDecimal valor = getMaiorValor(notaCreditoParcela.getParcelaTabelaCe().getVlDefinido(),
													 vlFrete.multiply(notaCreditoParcela.getParcelaTabelaCe().getPcSobreValor()).divide(BigDecimal.valueOf(100)));
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : valor, tpParcela, vlFrete);
					addParcelaRateio(rateio, doc, parcela); 
				}
			}
		}
	}

	public void calculaParcela1e2FretePeso(ParametrosCalculoRateio parametrosCalculoRateio) {
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();
		

		if (PARCELA_FRETE_PESO_C1E2.equals(tpParcela)) {
			
			String tpCalculo = notaCreditoParcela.getParcelaTabelaCe().getTabelaColetaEntrega().getTpCalculo().getValue();
			
			BigDecimal qtd = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();
			
			Map<Long, BigDecimal> coletasPesos = new HashMap<Long, BigDecimal>();
			Map<Long, BigDecimal> coletasValor = new HashMap<Long, BigDecimal>();
			
			if("C2".equals(tpCalculo)){
				calculaFretePesoC2(idCliente, tpParcela, notaCreditoParcela, parcelasEspeciais, docts, rateio, qtd, vl, coletasPesos, coletasValor);
			}else{
				calculaFretePesoC1(idCliente, tpParcela, parcelasEspeciais, docts, rateio, vl);
			}
		}
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param rateio
	 * @param vl
	 */
	private void calculaFretePesoC1(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal vl) {
		//agrupo por endereço
		Map<String, BigDecimal> pesos = new HashMap<String, BigDecimal>();
		
		agrupaPesoPorEndereco(idCliente, tpParcela, parcelasEspeciais, docts, pesos);
		
		for (Map<String, Object> doc : docts) {
			
			if(aplicaParcela(idCliente, doc,parcelasEspeciais, tpParcela)){
				
				for (String endereco : pesos.keySet()) {
					
					if (endereco.equals((String) doc.get(ENDERECO))) {
						
						BigDecimal peso = pesos.get(endereco);
						if(BigDecimal.ZERO.equals(peso)){
							peso = BigDecimal.ONE;
						}
						
						BigDecimal valor = calculaPesoItem(peso, capacidadeVeiculo);
						
						if (valor.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal maiorPeso = getMaiorPeso((BigDecimal) doc.get("PS_AFORADO"), (BigDecimal) doc.get("PS_REAL"),(BigDecimal) doc.get("PS_AFERIDO"),BigDecimal.ZERO);
							
							BigDecimal parte = vl.divide(peso, 9, RoundingMode.HALF_UP).multiply(maiorPeso);
							
							ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, maiorPeso);					                
			                addParcelaRateio(rateio, doc, parcela);
			            }else{
			                ParcelaRateioFreteCarreteiroCE parcela = generateParcela(BigDecimal.ZERO, tpParcela, BigDecimal.ZERO);					                
			                addParcelaRateio(rateio, doc, parcela);
			            }
					}
				}
			}
			
		}
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param notaCreditoParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param rateio
	 * @param qtd
	 * @param vl
	 * @param coletasPesos
	 * @param coletasValor
	 */
	private void calculaFretePesoC2(Long idCliente, String tpParcela, NotaCreditoParcela notaCreditoParcela, List<Long> parcelasEspeciais,
			List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal qtd, BigDecimal vl,
			Map<Long, BigDecimal> coletasPesos, Map<Long, BigDecimal> coletasValor) {
		boolean isZero = BigDecimal.ZERO.equals(vl.multiply(qtd));

		for (Map<String, Object> doc : docts) {
			
			if(aplicaParcela(idCliente, doc,parcelasEspeciais, tpParcela)){
				
				BigDecimal peso = BigDecimal.ZERO;
				
				if( doc.containsKey("COLETA")){
					peso = getMaiorPeso((BigDecimal) doc.get("PS_TOTAL_VERIFICADO"), (BigDecimal) doc.get("PS_TOTAL_AFORADO_VERIFICADO"),BigDecimal.ZERO,BigDecimal.ZERO);
				}else{
					peso = getMaiorPeso((BigDecimal) doc.get("PS_AFORADO"), (BigDecimal) doc.get("PS_REAL"),(BigDecimal) doc.get("PS_AFERIDO"),BigDecimal.ZERO);
				}
				
				List<FaixaPeso> faixasPeso = calculaFaixaPeso(notaCreditoParcela.getFaixaPesoParcelaTabelaCE(), peso) ;
				
				for (FaixaPeso faixaPeso : faixasPeso) {
					BigDecimal qtNotaCredito = BigDecimal.valueOf(faixaPeso.getQuantidade());
					BigDecimal vlNotaCredito = BigDecimal.ZERO;
					
					if(PESO.equals(notaCreditoParcela.getFaixaPesoParcelaTabelaCE().getTpFator())) {
						vlNotaCredito = faixaPeso.getFaixa().getVlValor().multiply(qtNotaCredito);
					}else{
						vlNotaCredito = faixaPeso.getFaixa().getVlValor();
					}
					
					if(! doc.containsKey("COLETA")){
						ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : vlNotaCredito, tpParcela, qtNotaCredito);								
						addParcelaRateio(rateio, doc, parcela);
					}else{
						long idColeta = ((BigDecimal) doc.get("ID_PEDIDO_COLETA")).longValue();
						
						coletasValor.put(idColeta, vlNotaCredito);
						
						peso = (BigDecimal) doc.get(PS_REFERENCIA_CALCULO);
						
						if(!coletasPesos.containsKey(idColeta)){
							coletasPesos.put(idColeta, BigDecimal.ZERO);
						}
						coletasPesos.put(idColeta, coletasPesos.get(idColeta).add(peso));
					}
					
				}
				
			}
		}
			
		geraParcelaPorColeta(idCliente, tpParcela, parcelasEspeciais, docts, rateio, coletasPesos, coletasValor, isZero);
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param pesos
	 */
	private void agrupaPesoPorEndereco(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<String, BigDecimal> pesos) {
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			
			if(aplicaParcela(idCliente, map,parcelasEspeciais, tpParcela)){
			
				BigDecimal maiorPeso = getMaiorPeso((BigDecimal) map.get("PS_AFORADO"), (BigDecimal) map.get("PS_REAL"),(BigDecimal) map.get("PS_AFERIDO"),BigDecimal.ZERO);
				
				if (pesos.containsKey(endereco)) {
					pesos.put(endereco,pesos.get(endereco).add(maiorPeso));
				} else {
					pesos.put(endereco,maiorPeso);
				}
			}
		}
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param rateio
	 * @param coletasPesos
	 * @param coletasValor
	 * @param isZero
	 */
	private void geraParcelaPorColeta(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, Map<Long, BigDecimal> coletasPesos, Map<Long, BigDecimal> coletasValor,
			boolean isZero) {
		for (Map<String, Object> doc : docts) {
			
			if(aplicaParcela(idCliente, doc,parcelasEspeciais, tpParcela)){
				
				
				if( doc.containsKey("COLETA")){
					long idColeta = ((BigDecimal) doc.get("ID_PEDIDO_COLETA")).longValue();
					
					BigDecimal psTotalDoc =  coletasPesos.get(idColeta);
					
					if(BigDecimal.ZERO.equals(psTotalDoc)){
						psTotalDoc = BigDecimal.ONE;
					}
					
					BigDecimal vlColeta =  coletasValor.get(idColeta);
					
					
					if(vlColeta == null || psTotalDoc == null ){
						continue;
					}
					
					
					BigDecimal vlPorQuilo =  vlColeta.divide(psTotalDoc, 9, RoundingMode.HALF_UP);
					
					BigDecimal psdoc =  (BigDecimal) doc.get(PS_REFERENCIA_CALCULO);
					BigDecimal vlProporcional = vlPorQuilo.multiply(psdoc);
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : vlProporcional, tpParcela, psdoc);								
					addParcelaRateio(rateio, doc, parcela);
				}
			}
		}
	}

	private BigDecimal calculaPesoItem(BigDecimal peso, BigDecimal capacidade) {
		if(BigDecimal.ZERO.equals(capacidade)){
			capacidade = BigDecimal.ONE;
		}
	    return peso.divide(capacidade, BigDecimal.ROUND_UP).subtract(BigDecimal.ONE);
	}
	
	public static BigDecimal getMaiorPeso(BigDecimal psAforado, BigDecimal psReal, BigDecimal psAferido, BigDecimal psCubado) {
		List<BigDecimal> somaPs = new ArrayList<BigDecimal>();
		somaPs.add(BigDecimal.ZERO);
		
		if(psAforado != null){
			somaPs.add(psAforado);
		}
		
		if(psReal != null){
			somaPs.add(psReal);
		}
		
		if(psAferido != null){
			somaPs.add(psAferido);
		}
		if(psCubado != null){
			somaPs.add(psCubado);
		}
		
		Collections.sort(somaPs, Collections.reverseOrder());
		
		return somaPs.get(0);
	}
	
	
	private  List<FaixaPeso> calculaFaixaPeso(FaixaPesoParcelaTabelaCE faixa, BigDecimal pesoCarga) {
        FaixaPeso faixaPeso = new FaixaPeso(faixa);
        BigDecimal pesoCreditado = BigDecimal.ZERO;
        
        List<FaixaPeso> faixasPeso = new ArrayList<FaixaPeso>();
        int quantidadeFaixas = 0;

        if (!faixasPeso.contains(faixaPeso)) {
            if (DOCUMENTO.equals(faixa.getTpFator())) {
                pesoCreditado = faixa.getVlValor();
                quantidadeFaixas = 1;
            } else if (PESO.equals(faixa.getTpFator())) {
                pesoCreditado = calculaPesoCredito(pesoCarga, faixa.getPsInicial(), faixa.getPsFinal());
                quantidadeFaixas = pesoCreditado.intValue();
            }

            if (pesoCreditado.compareTo(BigDecimal.ZERO) > 0) {
                faixaPeso.setQuantidade(quantidadeFaixas);
                faixasPeso.add(faixaPeso);
            }
        }
		return faixasPeso;
    }
	
	 private BigDecimal calculaPesoCredito(BigDecimal pesoCarga, BigDecimal faixaInicial, BigDecimal faixaFinal) {
        BigDecimal pesoCreditado = BigDecimal.ZERO;

        if (pesoCarga.compareTo(faixaInicial) >= 0) {
            if (pesoCarga.compareTo(faixaFinal) >= 0) {
                pesoCreditado = faixaFinal.subtract(faixaInicial);
            } else if (pesoCarga.compareTo(faixaFinal) == -1 && pesoCarga.compareTo(faixaInicial) >= 0) {
                pesoCreditado = pesoCarga.subtract(faixaInicial);
            }
        }

        return pesoCreditado.setScale(0, RoundingMode.CEILING);
    }
	

	public void calculaParcela1e2Evento(ParametrosCalculoRateio parametrosCalculoRateio) {
		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();
		

		if (PARCELA_EVENTO_C1E2.equals(tpParcela)) {
			
			String tpCalculo = notaCreditoParcela.getParcelaTabelaCe().getTabelaColetaEntrega().getTpCalculo().getValue();
			
			BigDecimal qtotal = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal vl = notaCreditoParcela.getVlNotaCreditoParcela();
			
			boolean isZero = BigDecimal.ZERO.equals(qtotal.multiply(vl));
			
			if("C2".equals(tpCalculo)){
				calculaEventoC2(idCliente, tpParcela, parcelasEspeciais, docts, rateio, vl, isZero);
			}else{
				calculaEventoC1(idCliente, tpParcela, parcelasEspeciais, docts, rateio, vl, isZero);
			}
			
			
		}
	}
	
	public void calculaParcelaRegraDificuldadeEntrega(List<Map<String, Object>> docts,	Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal totalNota) {
		if(docts.isEmpty()){
			return;
		}
		Map<BigDecimal, BigDecimal> somatorioR2 = calculaParcelaRegra2DificuldadeEntrega(docts, rateio,totalNota.multiply(new BigDecimal(0.8)));
		Map<BigDecimal, BigDecimal> somatorioR2R3 = calculaParcelaRegra3DificuldadeEntrega(docts, rateio,totalNota.multiply(new BigDecimal(0.2)),somatorioR2);
		calculaDificuldadeRegra1(docts,rateio,somatorioR2R3);
		
	}
	
	
	
	private void calculaDificuldadeRegra1(List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, Map<BigDecimal, BigDecimal> somatorioR2R3) {
		String tpParcela = PARCELA_REGRA_1_DIFICULDADE_ENTREGA;
		
		for (Map<String, Object> doc : docts) {
			ParcelaRateioFreteCarreteiroCE parcela = generateParcela(somatorioR2R3.get((BigDecimal)doc.get("ID_DOCUMENTO")), tpParcela, new BigDecimal(100));
			addParcelaRateio(rateio, doc, parcela);
		}
		
	}

	/***
	 * 2) Regra (Apropriar 80% do custo identificado por parada em cada conhecimento referente a parada)
	 * @param docts
	 * @param rateio
	 * @param totalNota
	 * @return 
	 */
	public Map<BigDecimal, BigDecimal> calculaParcelaRegra2DificuldadeEntrega(List<Map<String, Object>> docts,	Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal totalNota) {
		String tpParcela = PARCELA_REGRA_2_DIFICULDADE_ENTREGA;
		 Map<BigDecimal, BigDecimal> somatorioR2R3 = new HashMap<BigDecimal, BigDecimal>();		
		return calculaParcelaDificuldadeProporcional(docts, rateio, totalNota, tpParcela,somatorioR2R3);
	}

	/**
	 * @param docts
	 * @param rateio
	 * @param totalNota
	 * @param tpParcela
	 */
	private Map<BigDecimal, BigDecimal> calculaParcelaDificuldadeProporcional(List<Map<String, Object>> docts, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio,	BigDecimal totalNota, String tpParcela, Map<BigDecimal, BigDecimal> somatorioR2R3) {
		Map<String, Integer> enderecos = new HashMap<String, Integer>();
		Map<String, BigDecimal> enderecosColetas = new HashMap<String, BigDecimal>();
			
		BigDecimal	somatorioDificuldade = agrupaEnderecoDificuldadeEntrega(docts, enderecos, enderecosColetas);
			
		if(BigDecimal.ZERO.equals(somatorioDificuldade)){
			somatorioDificuldade = BigDecimal.ONE;
		}
		
		BigDecimal vl = totalNota.divide(somatorioDificuldade, 9, RoundingMode.HALF_UP);
		
		
		for (Map<String, Object> doc : docts) {
			somatorioR2R3.put((BigDecimal)doc.get("ID_DOCUMENTO"),BigDecimal.ZERO);
			
			for (String endereco : enderecos.keySet()) {
				
				if (endereco.equals((String) doc.get(ENDERECO))) {
									
					BigDecimal parte = vl.multiply((BigDecimal) doc.get(DIFICULDADE));
					BigDecimal vlt = parte;

					if( doc.containsKey("COLETA")){		
						
						if(BigDecimal.ZERO.equals(enderecosColetas.get(endereco))){
							enderecosColetas.put(endereco,BigDecimal.ONE);
						}
						
						vlt = parte.divide(enderecosColetas.get(endereco), 9, RoundingMode.HALF_UP);
					}
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(vlt, tpParcela, new BigDecimal(80));
					
					addParcelaRateio(rateio, doc, parcela);
					
					somatorioR2R3.put((BigDecimal)doc.get("ID_DOCUMENTO"),somatorioR2R3.get((BigDecimal)doc.get("ID_DOCUMENTO")).add(vlt));
					
				}
			}
		}
		return somatorioR2R3;
	}

	/**
	 * @param docts
	 * @param enderecos
	 * @param pesosColetas
	 * @param pesosDocs
	 * @param enderecoDificuldade
	 * @param enderecoPesos 
	 * @param enderecosColetas 
	 * @param somatorioDificuldade
	 * @return
	 */
	private BigDecimal agrupaEnderecoDificuldadeEntrega(List<Map<String, Object>> docts, Map<String, Integer> enderecos, Map<String, BigDecimal> enderecosColetas) {
		
		Map<String, BigDecimal> pesosColetas = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> pesosDocs = new HashMap<String, BigDecimal>();
		
		BigDecimal	somatorioDificuldade = BigDecimal.ZERO;
		Map<BigDecimal,BigDecimal> dificuldadeColeta = new HashMap<BigDecimal, BigDecimal>();
		
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			
				if (enderecos.containsKey(endereco)) {
					
					if( map.containsKey("COLETA")){						
						if (pesosColetas.containsKey(endereco)) {
							pesosColetas.put(endereco,pesosColetas.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));
							enderecosColetas.put(endereco, enderecosColetas.get(endereco).add(BigDecimal.ONE));
						}else{
							pesosColetas.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
							enderecosColetas.put(endereco, BigDecimal.ONE);
						}

					}else{
						if (pesosDocs.containsKey(endereco)) {
							pesosDocs.put(endereco,pesosDocs.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));
						}else{
							pesosDocs.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
						}
					}
					
				} else {				
					enderecos.put(endereco, 1);
					if( map.containsKey("COLETA")){
						pesosColetas.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));		
						enderecosColetas.put(endereco, BigDecimal.ONE);
					}else{
						pesosDocs.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
					}
				}
				
				
		}
		
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			BigDecimal vlcoletas = pesosColetas.get(endereco);
			BigDecimal vlDocs    = pesosDocs.get(endereco);
		
			if(vlcoletas != null &&  vlDocs != null && vlcoletas.compareTo(BigDecimal.ZERO) > 0 && vlDocs.compareTo(BigDecimal.ZERO) > 0 ){
				enderecos.put(endereco, 2);
			}else{
				enderecos.put(endereco, 1);
			}
			
			BigDecimal ID_COLETA = (BigDecimal) map.get("ID_PEDIDO_COLETA");
			
			if(dificuldadeColeta.containsKey(ID_COLETA) &&  map.containsKey("COLETA")){
				continue;
			}
			
			dificuldadeColeta.put(ID_COLETA, (BigDecimal) map.get(DIFICULDADE));
			
			somatorioDificuldade = somatorioDificuldade.add((BigDecimal) map.get(DIFICULDADE));
		}
		return somatorioDificuldade;
	}
	
	/***
	 * 3) Regra (Apropriar 20% do custo identificado por parada em cada conhecimento referente a parada proporcional ao peso)
	 * @param docts
	 * @param rateio
	 * @param totalNota
	 * @param somatorioR2R3 
	 * @return 
	 */
	public Map<BigDecimal, BigDecimal> calculaParcelaRegra3DificuldadeEntrega(List<Map<String, Object>> docts,	Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal totalNota, Map<BigDecimal, BigDecimal> somatorioR2R3) {
		String tpParcela = PARCELA_REGRA_3_DIFICULDADE_ENTREGA;
		
			
		BigDecimal somatorioPesos = somatorioPesoDocs(docts);
		
		if(BigDecimal.ZERO.equals(somatorioPesos)){
			somatorioPesos = BigDecimal.ONE;
		}
		
		
		BigDecimal vlPorPeso = totalNota.divide(somatorioPesos, 9, RoundingMode.HALF_UP);
		
		for (Map<String, Object> doc : docts) {
					
			BigDecimal pesoDoc = (BigDecimal) doc.get(PS_REFERENCIA_CALCULO);
			
			BigDecimal vlp = vlPorPeso.multiply(pesoDoc);
			
			ParcelaRateioFreteCarreteiroCE parcela = generateParcela(vlp, tpParcela, new BigDecimal(20));
			
			addParcelaRateio(rateio, doc, parcela);
			
			somatorioR2R3.put((BigDecimal)doc.get("ID_DOCUMENTO"),somatorioR2R3.get((BigDecimal)doc.get("ID_DOCUMENTO")).add(vlp));
					
		}
		return somatorioR2R3;
	}

	/**
	 * @param docts
	 * @return
	 */
	private BigDecimal somatorioPesoDocs(List<Map<String, Object>> docts) {
		BigDecimal somatorioPesos = BigDecimal.ZERO; 
		
		for (Map<String, Object> doc : docts) {
			somatorioPesos = somatorioPesos.add((BigDecimal) doc.get(PS_REFERENCIA_CALCULO));
		}
		return somatorioPesos;
	}
	
	
	
	

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param rateio
	 * @param vl
	 * @param isZero
	 */
	private void calculaEventoC2(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal vl, boolean isZero) {
		
		
		Map<String, Integer> enderecos = new HashMap<String, Integer>();
		Map<String, BigDecimal> pesos  = new HashMap<String, BigDecimal>();
		agrupaEnderecoC2(idCliente, tpParcela, parcelasEspeciais, docts, enderecos, pesos);
		
		for (Map<String, Object> map : docts) {		
			if(aplicaParcela(idCliente, map,parcelasEspeciais, tpParcela)){
				if( map.containsKey("COLETA")){
					BigDecimal peso = pesos.get((String) map.get(ENDERECO));
					if(BigDecimal.ZERO.equals(peso)){
						peso = BigDecimal.ONE;
					}
					
					BigDecimal valorPorQuilo = isZero ? BigDecimal.ZERO : vl.divide(peso, 9, RoundingMode.HALF_UP);
					
					BigDecimal qtd = (BigDecimal) map.get(PS_REFERENCIA_CALCULO);
					
					BigDecimal parte = valorPorQuilo.multiply(qtd).setScale(9, RoundingMode.HALF_UP);
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : parte, tpParcela, qtd);
					
					addParcelaRateio(rateio, map, parcela);
				}else{
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : vl, tpParcela, BigDecimal.ONE);
					addParcelaRateio(rateio, map, parcela);				
				}
				
			}
		}
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param rateio
	 * @param vl
	 * @param isZero
	 */
	private void calculaEventoC1(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal vl, boolean isZero) {
		Map<String, Integer> enderecos = new HashMap<String, Integer>();
		Map<String, BigDecimal> pesosColeta = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> pesosDocs = new HashMap<String, BigDecimal>();
		
		agrupaEnderecoC1(idCliente, tpParcela, parcelasEspeciais, docts, enderecos, pesosColeta, pesosDocs);
		
		for (Map<String, Object> doc : docts) {
			
			if(aplicaParcela(idCliente, doc,parcelasEspeciais, tpParcela)){
				
				for (String endereco : enderecos.keySet()) {
					
					if (endereco.equals((String) doc.get(ENDERECO))) {
						Integer qtd = enderecos.get(endereco);
						
						if(qtd == 1){	
							BigDecimal valorPorQuilo = BigDecimal.ZERO;
							if( doc.containsKey("COLETA")){
								BigDecimal pesocol = pesosColeta.get((String) doc.get(ENDERECO));
								if(BigDecimal.ZERO.equals(pesocol)){
									pesocol = BigDecimal.ONE;
								}
								
								valorPorQuilo = isZero ? BigDecimal.ZERO : vl.divide(pesocol, 9, RoundingMode.HALF_UP);
							}else{
								BigDecimal pesodoc = pesosDocs.get((String) doc.get(ENDERECO));
								if(BigDecimal.ZERO.equals(pesodoc)){
									pesodoc = BigDecimal.ONE;
								}
								valorPorQuilo = isZero ? BigDecimal.ZERO : vl.divide(pesodoc, 9, RoundingMode.HALF_UP);
							}
							
							BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
							ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, new BigDecimal(qtd));
							
							addParcelaRateio(rateio, doc, parcela);
							
						}else{
							BigDecimal valorPorQuilo = BigDecimal.ZERO;
							
							BigDecimal vlp = vl.divide(new BigDecimal(qtd), 9, RoundingMode.HALF_UP);
							
							if( doc.containsKey("COLETA")){
								BigDecimal pesocol = pesosColeta.get((String) doc.get(ENDERECO));
								if(BigDecimal.ZERO.equals(pesocol)){
									pesocol = BigDecimal.ONE;
								}
								valorPorQuilo = isZero ? BigDecimal.ZERO : vlp.divide(pesocol, 9, RoundingMode.HALF_UP);
							}else{
								BigDecimal pesodoc = pesosDocs.get((String) doc.get(ENDERECO));
								if(BigDecimal.ZERO.equals(pesodoc)){
									pesodoc = BigDecimal.ONE;
								}
								valorPorQuilo = isZero ? BigDecimal.ZERO : vlp.divide(pesodoc, 9, RoundingMode.HALF_UP);
							}
							
							BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);
							
							ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : parte, tpParcela, new BigDecimal(qtd));
							
							addParcelaRateio(rateio, doc, parcela);
						}
					}
				}
			}
			
		}
	}

	/**
	 * @param idCliente
	 * @param tpParcela
	 * @param parcelasEspeciais
	 * @param docts
	 * @param enderecos
	 * @param pesosColetas
	 * @param pesosDocs 
	 */
	private void agrupaEnderecoC1(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<String, Integer> enderecos, Map<String, BigDecimal> pesosColetas, Map<String, BigDecimal> pesosDocs) {
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			
			if(aplicaParcela(idCliente, map,parcelasEspeciais, tpParcela)){
			
				if (enderecos.containsKey(endereco)) {
					
					if( map.containsKey("COLETA")){						
						if (pesosColetas.containsKey(endereco)) {
							pesosColetas.put(endereco,pesosColetas.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));						
						}else{
							pesosColetas.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
						}
					}else{
						if (pesosDocs.containsKey(endereco)) {
							pesosDocs.put(endereco,pesosDocs.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));
						}else{
							pesosDocs.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
						}
					}
				} else {				
					enderecos.put(endereco, 1);
					if( map.containsKey("COLETA")){
						pesosColetas.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
					}else{
						pesosDocs.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
					}
				}
			}
		}
		
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			BigDecimal vlcoletas = pesosColetas.get(endereco);
			BigDecimal vlDocs    = pesosDocs.get(endereco);
		
			if(vlcoletas != null &&  vlDocs != null && vlcoletas.compareTo(BigDecimal.ZERO) > 0 && vlDocs.compareTo(BigDecimal.ZERO) > 0 ){
				enderecos.put(endereco, 2);
			}else{
				enderecos.put(endereco, 1);
			}
		}
		
		
	}
	
	private void agrupaEnderecoC2(Long idCliente, String tpParcela, List<Long> parcelasEspeciais, List<Map<String, Object>> docts,
			Map<String, Integer> enderecos, Map<String, BigDecimal> pesos) {
		for (Map<String, Object> map : docts) {
			String endereco = (String) map.get(ENDERECO);
			
			if(aplicaParcela(idCliente, map,parcelasEspeciais, tpParcela)){
				if( map.containsKey("COLETA")){
					if (enderecos.containsKey(endereco)) {
						enderecos.put(endereco, enderecos.get(endereco) + 1);
						pesos.put(endereco,pesos.get(endereco).add((BigDecimal) map.get(PS_REFERENCIA_CALCULO)));
					} else {
						enderecos.put(endereco, 1);
						pesos.put(endereco,(BigDecimal) map.get(PS_REFERENCIA_CALCULO));
					}
				}
				
			
			}
		}
	}
	
	

	public void calculaParcela1e2Diaria(ParametrosCalculoRateio parametrosCalculoRateio) {

		Long idCliente   = parametrosCalculoRateio.getIdCliente();
		String tpParcela = parametrosCalculoRateio.getTpParcela();
		BigDecimal quilo = parametrosCalculoRateio.getQuilo();		
		NotaCreditoParcela notaCreditoParcela 				   =  parametrosCalculoRateio.getNotaCreditoParcela();
		List<Long> parcelasEspeciais         				   = parametrosCalculoRateio.getParcelasEspeciais();
		List<Map<String, Object>> docts      				   = parametrosCalculoRateio.getDocts();
		Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio = parametrosCalculoRateio.getRateio();
		
		if (PARCELA_DIARIA_C1E2.equals(tpParcela)) {
			
			
			BigDecimal qtotal = notaCreditoParcela.getQtNotaCreditoParcela();
			BigDecimal valor = notaCreditoParcela.getVlNotaCreditoParcela();
			
			boolean isZero = BigDecimal.ZERO.equals(qtotal.multiply(valor));

			BigDecimal valorPorQuilo = isZero ? BigDecimal.ZERO : valor.multiply(qtotal).divide(quilo, 9, RoundingMode.HALF_UP);

			for (Map<String, Object> doc : docts) {
				
				if(aplicaParcela(idCliente, doc,parcelasEspeciais, tpParcela)){
					
					
					BigDecimal parte = valorPorQuilo.multiply((BigDecimal) doc.get(PS_REFERENCIA_CALCULO)).setScale(9, RoundingMode.HALF_UP);				
					
					ParcelaRateioFreteCarreteiroCE parcela = generateParcela(isZero ? BigDecimal.ZERO : parte, tpParcela, BigDecimal.ONE);
					
					addParcelaRateio(rateio, doc, parcela);
				}


			}
		}
	}

	/**
	 * @param idCliente
	 * @param doc
	 * @param tpParcela 
	 * @return
	 */
	private boolean aplicaParcela(Long idCliente, Map<String, Object> doc,  List<Long> parcelasEspeciais, String tpParcela) {
		Long idRemetente = ((BigDecimal)doc.get("ID_CLIENTE_REMETENTE")).longValue();
		
		
		if(idCliente == null && !parcelasEspeciais.contains(idRemetente)){
			return true;
		}
		if(parcelasEspeciais.contains(idRemetente) && idRemetente.equals(idCliente)){
			return true;
		}
		return false;
	}

	/**
	 * @param notaCreditoParcela
	 */
	private Long findIdCliente(NotaCreditoParcela notaCreditoParcela) {
		Cliente cliente = notaCreditoParcela.getParcelaTabelaCe().getTabelaColetaEntrega().getCliente();
		if(cliente != null){
			return cliente.getIdCliente();
		}
		return null;
	}

	public void calculaParcelasAdicionais(List<RateioFreteCarreteiroCE> list, NotaCredito notaCredito, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, BigDecimal total, List<Map<String, Object>> docts) {
		calculaParcelaAcrescimoNota(list, notaCredito, total, rateio,docts);
		calculaParcelaDescontoNota(list, notaCredito, total, rateio);
		calculaParcelaDescUsoEquipamentoNota(list, notaCredito, total, rateio,docts);
	}

	public void calculaParcelaDescUsoEquipamentoNota(List<RateioFreteCarreteiroCE> list, NotaCredito notaCredito, BigDecimal total,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, List<Map<String, Object>> docts) {

		BigDecimal descUsoEquipamentoNota = notaCredito.getVlDescUsoEquipamento();

		if (descUsoEquipamentoNota != null && !descUsoEquipamentoNota.equals(BigDecimal.ZERO) && total.compareTo(BigDecimal.ZERO) > 0) {

			
			descUsoEquipamentoNota = descUsoEquipamentoNota.divide(total,9, RoundingMode.HALF_UP);

			
			for (RateioFreteCarreteiroCE rateioFreteCarreteiroCE : list) {
				BigDecimal vl = rateioFreteCarreteiroCE.getVlTotal();
				
				BigDecimal parte = vl.multiply(descUsoEquipamentoNota).multiply(RateioFreteCarreteiroHelper.PARCELA_NEGATIVA).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = RateioFreteCarreteiroHelper.generateParcela(parte, PARCELA_DESCONTO_USO_EQUIPAMENTO, vl);
				
				addParcelaRateio(rateio, rateioFreteCarreteiroCE.getDoctoServico(), parcela);
			}
		}
		
		else if(descUsoEquipamentoNota != null && !descUsoEquipamentoNota.equals(BigDecimal.ZERO) && total.compareTo(BigDecimal.ZERO) == 0){
			
			int qtdDocs = docts.size();
			
			for (Map<String, Object> doc : docts) {
				BigDecimal vl = descUsoEquipamentoNota.divide(new BigDecimal(qtdDocs),9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(vl.multiply(RateioFreteCarreteiroHelper.PARCELA_NEGATIVA).setScale(9, RoundingMode.HALF_UP), PARCELA_DESCONTO_USO_EQUIPAMENTO, new BigDecimal(qtdDocs));
				
				addParcelaRateio(rateio, doc, parcela);
			}
			
		}

	}

	public void calculaParcelaDescontoNota(List<RateioFreteCarreteiroCE> list, NotaCredito notaCredito, BigDecimal total,
			Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {

		BigDecimal valorDescontoNota = notaCredito.getVlDesconto();
		
		if (valorDescontoNota != null && !valorDescontoNota.equals(BigDecimal.ZERO) && total.compareTo(BigDecimal.ZERO) > 0) {

			valorDescontoNota = valorDescontoNota.divide(total,9, RoundingMode.HALF_UP);

			
			for (RateioFreteCarreteiroCE rateioFreteCarreteiroCE : list) {
				BigDecimal vl = rateioFreteCarreteiroCE.getVlTotal();
				
				BigDecimal parte = vl.multiply(valorDescontoNota).multiply(RateioFreteCarreteiroHelper.PARCELA_NEGATIVA).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = RateioFreteCarreteiroHelper.generateParcela(parte, PARCELA_DESCONTO_NOTA_FIXO, vl);
				
				addParcelaRateio(rateio, rateioFreteCarreteiroCE.getDoctoServico(), parcela);
			}
		}

	}

	public void calculaParcelaAcrescimoNota(List<RateioFreteCarreteiroCE> list, NotaCredito notaCredito, BigDecimal total, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, List<Map<String, Object>> docts) {

		BigDecimal valorAcrescimoNota = notaCredito.getVlAcrescimo();

		if (valorAcrescimoNota != null && !valorAcrescimoNota.equals(BigDecimal.ZERO) && total.compareTo(BigDecimal.ZERO) > 0 ) {

			valorAcrescimoNota = valorAcrescimoNota.divide(total,9, RoundingMode.HALF_UP);
			
			for (RateioFreteCarreteiroCE rateioFreteCarreteiroCE : list) {
				BigDecimal vl = rateioFreteCarreteiroCE.getVlTotal();
				
				BigDecimal parte = vl.multiply(valorAcrescimoNota).setScale(9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, PARCELA_ACRESCIMO_NOTA_FIXA_S, valorAcrescimoNota);
				
				addParcelaRateio(rateio, rateioFreteCarreteiroCE.getDoctoServico(), parcela);
			}
		}else if(valorAcrescimoNota != null && !valorAcrescimoNota.equals(BigDecimal.ZERO) && total.compareTo(BigDecimal.ZERO) == 0){
			
			int qtdDocs = docts.size();
			
			for (Map<String, Object> doc : docts) {
				BigDecimal vl = valorAcrescimoNota.divide(new BigDecimal(qtdDocs),9, RoundingMode.HALF_UP);
				
				ParcelaRateioFreteCarreteiroCE parcela = generateParcela(vl, PARCELA_ACRESCIMO_NOTA_FIXA_S, new BigDecimal(qtdDocs));
				
				addParcelaRateio(rateio, doc, parcela);
			}
		}
	}

	private void addParcelaRateio(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, Map<String, Object> doc, ParcelaRateioFreteCarreteiroCE parcela) {
		List<ParcelaRateioFreteCarreteiroCE> lista = rateio.get(((BigDecimal) doc.get(ID_DOCUMENTO)).longValue());
		lista.add(parcela);
		rateio.put(((BigDecimal) doc.get(ID_DOCUMENTO)).longValue(), lista);
	}
	
	private void addParcelaRateio(Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio, DoctoServico doc, ParcelaRateioFreteCarreteiroCE parcela) {
		List<ParcelaRateioFreteCarreteiroCE> lista = rateio.get(doc.getIdDoctoServico());
		lista.add(parcela);
		rateio.put(doc.getIdDoctoServico(), lista);
	}

	public static ParcelaRateioFreteCarreteiroCE generateParcela(BigDecimal parte, String tpValor, BigDecimal qtd) {
		ParcelaRateioFreteCarreteiroCE parcela = new ParcelaRateioFreteCarreteiroCE();
		parcela.setTpValor(tpValor);
		parcela.setQtdTotal(qtd);
		parcela.setVlTotal(parte);
		return parcela;
	}

	public void setCapacidadeVeiculo(BigDecimal capacidadeVeiculo) {
		this.capacidadeVeiculo = capacidadeVeiculo;
	}
	


	/**
	 * @param totalRateio
	 * @param rateioFreteCarreteiroCE
	 * @param vl
	 * @param tpParcela 
	 * @param rateio 
	 */
	public static void addDescontoRecibo(BigDecimal vlParcela, String tpParcela , BigDecimal totalRateio, RateioFreteCarreteiroCE rateioFreteCarreteiroCE, BigDecimal vl, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		
		if(totalRateio.equals(BigDecimal.ZERO)){
			return;
		}
		
		BigDecimal descontoProporcional = vlParcela.divide(totalRateio, 9, RoundingMode.HALF_UP);
		
		BigDecimal parte = vl.multiply(descontoProporcional).multiply(PARCELA_NEGATIVA).setScale(9, RoundingMode.HALF_UP);

		ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, vl);

		DoctoServico doc = rateioFreteCarreteiroCE.getDoctoServico();
		
		List<ParcelaRateioFreteCarreteiroCE> lista = rateio.get(doc.getIdDoctoServico());
		lista.add(parcela);
		rateio.put(doc.getIdDoctoServico(), lista);
	}
	
	/**
	 * @param totalRateio
	 * @param rateioFreteCarreteiroCE
	 * @param vl
	 * @param tpParcela 
	 * @param rateio 
	 */
	public static void addAcrescimoRecibo(BigDecimal vlParcela, String tpParcela , BigDecimal totalRateio, RateioFreteCarreteiroCE rateioFreteCarreteiroCE, BigDecimal vl, Map<Long, List<ParcelaRateioFreteCarreteiroCE>> rateio) {
		if(totalRateio.equals(BigDecimal.ZERO)){
			return;
		}
		
		BigDecimal descontoProporcional = vlParcela.divide(totalRateio, 9, RoundingMode.HALF_UP);
		
		BigDecimal parte = vl.multiply(descontoProporcional).setScale(9, RoundingMode.HALF_UP);

		ParcelaRateioFreteCarreteiroCE parcela = generateParcela(parte, tpParcela, vl);

		DoctoServico doc = rateioFreteCarreteiroCE.getDoctoServico();
		
		List<ParcelaRateioFreteCarreteiroCE> lista = rateio.get(doc.getIdDoctoServico());
		lista.add(parcela);
		rateio.put(doc.getIdDoctoServico(), lista);
	}
		
		

}
