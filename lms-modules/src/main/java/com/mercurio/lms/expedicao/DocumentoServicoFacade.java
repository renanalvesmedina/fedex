package com.mercurio.lms.expedicao;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.pendencia.model.CalculoMda;

public interface DocumentoServicoFacade {

	public void executeCalculoConhecimentoNacionalNormal(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalReentrega(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalRefaturamento(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalComplementoICMS(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalComplementoFrete(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalDevolucao(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoNacionalSegundoPercurso(CalculoFrete calculoFrete);

	public void executeCalculoConhecimentoInternacionalNormal(CalculoFrete calculoFrete);

	public void executeCalculoNotaFiscalTransporte(CalculoNFT calculoNFTransporte);

	public void executeCalculoNotaFiscalServico(CalculoNFServico calculoNFServico);

	public void executeCalculoCotacao(CalculoFrete calculoFrete);

	public void executeCalculoCotacaoViaWeb(CalculoFrete calculoFrete);

	public void executeCalculoMda(CalculoMda calculoMda);
}