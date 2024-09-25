package com.mercurio.lms.expedicao;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.service.CalculoFreteService;
import com.mercurio.lms.expedicao.model.service.CalculoNFServicoService;
import com.mercurio.lms.pendencia.model.CalculoMda;
import com.mercurio.lms.pendencia.model.service.CalculoMdaService;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.documentoServicoFacade"
 */
public class DocumentoServicoFacadeImpl implements DocumentoServicoFacade {
	private CalculoFreteService calculoFreteService;
	private CalculoNFServicoService calculoNFServicoService;
	private CalculoMdaService calculoMdaService;

	public void executeCalculoConhecimentoNacionalNormal(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRNormal(calculoFrete);
	}

	public void executeCalculoConhecimentoNacionalReentrega(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRReentrega(calculoFrete);
	}

	public void executeCalculoConhecimentoNacionalRefaturamento(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRRefaturamento(calculoFrete);
	}

	/**
	 * Executa o calculo do Conhecimento de Complemento de Frete
	 * @param calculoFrete
	 */
	public void executeCalculoConhecimentoNacionalComplementoFrete(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRComplementoFrete(calculoFrete);
	}

	/**
	 * Executa o calculo do Conhecimento de Complemento de ICMS
	 * @param calculoFrete
	 */
	public void executeCalculoConhecimentoNacionalComplementoICMS(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRComplementoICMS(calculoFrete);
	}

	/**
	 * Executa o calculo do Conhecimento de Devolucao
	 * @param calculoFrete
	 */
	public void executeCalculoConhecimentoNacionalDevolucao(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRDevolucao(calculoFrete);
	}

	/**
	 * Executa o calculo do Conhecimento de Segundo Percurso
	 * @param calculoFrete
	 */
	public void executeCalculoConhecimentoNacionalSegundoPercurso(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRSegundoPercurso(calculoFrete);
	}

	/**
	 * Executa o calculo da Nota Fiscal de Transporte
	 * @param calculoNFTransporte
	 */
	public void executeCalculoNotaFiscalTransporte(CalculoNFT calculoNFTransporte) {
		calculoFreteService.executeCalculoCTRNormal(calculoNFTransporte);
	}

	/**
	 * Executa o calculo da Nota Fiscal de Servico
	 * @param calculoNFServico
	 */
	public void executeCalculoNotaFiscalServico(CalculoNFServico calculoNFServico) {
		calculoNFServicoService.executeCalculoNFServico(calculoNFServico);
	}

	/**
	 * Executa o calculo do Conhecimento Internacional
	 * @param calculoFrete
	 */
	public void executeCalculoConhecimentoInternacionalNormal(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCRTNormal(calculoFrete);
	}

	/**
	 * Executa o calculo da Cotação
	 * @param calculoFrete
	 */
	public void executeCalculoCotacao(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoFreteCotacao(calculoFrete);
	}

	public void executeCalculoCotacaoViaWeb(CalculoFrete calculoFrete) {
		calculoFreteService.executeCalculoCTRNormal(calculoFrete);
	}

	/**
	 * Executa o calculo do MDA
	 * @param calculoMda
	 */
	public void executeCalculoMda(CalculoMda calculoMda) {
		calculoMdaService.executeCalculoMda(calculoMda);
	}

	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}
	public void setCalculoNFServicoService(CalculoNFServicoService calculoNFServicoService) {
		this.calculoNFServicoService = calculoNFServicoService;
	}
	public void setCalculoMdaService(CalculoMdaService calculoMdaService) {
		this.calculoMdaService = calculoMdaService;
	}
}
