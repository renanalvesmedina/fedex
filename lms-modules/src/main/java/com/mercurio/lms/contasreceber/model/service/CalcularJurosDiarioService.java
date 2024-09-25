package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.lms.configuracoes.model.service.CotacaoIndicadorFinanceiroService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroBoletoFilialService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Cálculo de Juro diário.
 * 
 * @author Robson Edemar Gehl
 * @author Felipe Cuozzo
 * @author Mickaël Jalbert
 * @spring.bean id="lms.contasreceber.calcularJurosDiarioService"
 */
public class CalcularJurosDiarioService {

	private FaturaService faturaService;
	private ClienteService clienteService;
	private FilialService filialService;
	
	private EnderecoPessoaService enderecoPessoaService;
	private CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService;
	
	private ParametroBoletoFilialService parametroBoletoFilialService;
	
	private PaisService paisService;
	
	private BatchLogger batchLogger;
	
	/**
	 * Atualiza valor do Juro Calculado da Fatura.<BR>
	 * O valor do juro calculado é a soma com o vlJuros informado.
	 * @param idFatura
	 * @param vlJuros
	 */
	private void storeFaturaJuroCalculado(Fatura fatura, BigDecimal vlJuros){
		fatura.setVlJuroCalculado(vlJuros);
		getFaturaService().store(fatura);
	}

	/**
	 * Cálculo dos Juros Diários das Faturas.
	 */
	public void executeCalcularJurosDiario() {
		// calcula sempre a partir da data atual.
		calcularJurosDiario(JTDateTimeUtils.getDataAtual());
	}
	
	/**
	 * Cálculo dos Juros Diários das Faturas.
	 * 
	 * @param dataReferencia indica a data base que será usada como referencia para calcular o intervalo de tempo que incide juros.
	 */
	protected void calcularJurosDiario(YearMonthDay dataReferencia) {
		
		batchLogger.info("Inicio de cálculo de juros diários de faturas");
		List faturas = getFaturaService().findFaturaCalculoJuroDiario(null);
		batchLogger.info("Total de faturas a serem corrigidas: "+faturas.size());

		Iterator iter = faturas.iterator();

		Map mapParametroFilialBoleto = parametroBoletoFilialService.findParametroBoletoFilial();
		
		long totalFaturasAtualizadas = 0;
		// Varre todas as faturas para calcular o juro de cada uma
		while (iter.hasNext()){

			Fatura fatura = (Fatura) iter.next();

			//Calcular o número de dias de atraso
			BigDecimal nrDiasAtraso = new BigDecimal(JTDateTimeUtils.getIntervalInDays(fatura.getDtVencimento(), dataReferencia));
			BigDecimal vlJuros = this.calcularVlJuros(fatura, nrDiasAtraso, mapParametroFilialBoleto);
			
			if (vlJuros != null){
				// Atualiza Fatura
				storeFaturaJuroCalculado(fatura, vlJuros);
				totalFaturasAtualizadas++;
			}

		}
		batchLogger.info("Processo concluído! Total de faturas que tiveram correção aplicada: "+totalFaturasAtualizadas);
	}
	
	/**
	 * @see calcularPercentualJuroDiario(Long, BigDecimal, BigDecimal, YearMonthDay)
	 */
	public BigDecimal calcularPercentualJuroDiario(Long idFilial, Long idCliente, YearMonthDay dtVigencia){
		BigDecimal clientePcJuroDiario = clienteService.findPcJuroDiario(idCliente);
		BigDecimal filialPcJuroDiario = filialService.findPcJuroDiario(idFilial);
		
		return calcularPercentualJuroDiario(idFilial, clientePcJuroDiario, filialPcJuroDiario, dtVigencia);	
	}
	
	/**
	 * @see calcularPercentualJuroDiario(Long, BigDecimal, BigDecimal, YearMonthDay)
	 */
	public BigDecimal calcularPercentualJuroDiario(Fatura fatura){
		return calcularPercentualJuroDiario(fatura.getFilialByIdFilial().getIdFilial(), fatura.getCliente().getPcJuroDiario(), fatura.getFilialByIdFilial().getPcJuroDiario(), fatura.getDtEmissao());	
	}
	
	/**
	 * autor Edenilson Fornazari
	 * Busca juros diário da fatura de acordo com parametrizações de cliente, filial e empresa 
	 * @param fatura
	 * @return percentual de juro diário
	 */
	public BigDecimal calcularPercentualJuroDiario(Long idFilial, BigDecimal clientePcJuroDiario, BigDecimal filialPcJuroDiario, YearMonthDay dtVigencia){
		BigDecimal pcJuros = null;
		
		
		if (clientePcJuroDiario != null && !BigDecimalUtils.isZero(clientePcJuroDiario) ){
			pcJuros = clientePcJuroDiario;
		} else if (filialPcJuroDiario != null && !BigDecimalUtils.isZero(filialPcJuroDiario) ){
			pcJuros = filialPcJuroDiario;
		}
		
		if ( pcJuros == null ) {
			/*
			 * Consulta para Pais do Endereço Padrão da Filial
			 */
			Long idPais = paisService.findIdPaisByIdPessoa(idFilial);
			
			BigDecimal vlIndicadorFinanceiro = getCotacaoIndicadorFinanceiroService().findVlCotacaoIndFinanceiro("JURODIA", idPais, dtVigencia);

			pcJuros = vlIndicadorFinanceiro;
		}
		
		return pcJuros;
	}
	
	public BigDecimal calcularVlJuros(Fatura fatura, BigDecimal nrDiasAtraso){
		return calcularVlJuros(fatura, nrDiasAtraso, null);
	}
	
	/**
	 * Retorna o valor total de juro em cima da fatura informada,
	 * a fatura tem que ter a filialByIdFilial e cliente inicialized.
	 * 
	 * author Mickaël Jalbert
	 * 20/03/2006
	 * 
	 * Atualizado por José Rodrigo Moraes em 13/09/2006
	 * 
	 * @param Fatura fatura
	 * @return BigDecimal
	 * */
	public BigDecimal calcularVlJuros(Fatura fatura, BigDecimal nrDiasAtraso, Map parametroBoletoFilial){
		BigDecimal vlJuros = BigDecimalUtils.ZERO;
		BigDecimal pcJuros = BigDecimalUtils.ZERO;
		
		pcJuros = calcularPercentualJuroDiario(fatura);

		if (pcJuros != null){
			
			BigDecimal vlTotal    = BigDecimalUtils.round(fatura.getVlTotal(), 2);
			vlJuros = BigDecimalUtils.round(vlTotal.multiply(pcJuros.divide(BigDecimalUtils.HUNDRED)), 2);
			vlJuros = vlJuros.multiply(nrDiasAtraso);
		}
		
		return vlJuros;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public CotacaoIndicadorFinanceiroService getCotacaoIndicadorFinanceiroService() {
		return cotacaoIndicadorFinanceiroService;
	}

	public void setCotacaoIndicadorFinanceiroService(
			CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService) {
		this.cotacaoIndicadorFinanceiroService = cotacaoIndicadorFinanceiroService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setParametroBoletoFilialService(
			ParametroBoletoFilialService parametroBoletoFilialService) {
		this.parametroBoletoFilialService = parametroBoletoFilialService;
	}
	
	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(CalcularJurosDiarioService.class);
	}
}
