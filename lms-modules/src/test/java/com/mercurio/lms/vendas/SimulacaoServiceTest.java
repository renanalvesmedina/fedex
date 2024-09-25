package com.mercurio.lms.vendas;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.mocks.ConfiguracoesFacadeMocker;
import com.mercurio.lms.mocks.ParametroClienteServiceMocker;
import com.mercurio.lms.mocks.PropostaServiceMocker;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;

public class SimulacaoServiceTest {
	
	private SimulacaoService simulacaoService;
	
	@BeforeTest
	public void init(){
		simulacaoService = new SimulacaoService();
		simulacaoService.setPropostaService(new PropostaServiceMocker().findByIdSimulacaoReturningNull().mock());
		simulacaoService.setParametroClienteService(createParametroClienteService(true, false));
		simulacaoService.setConfiguracoesFacade(new ConfiguracoesFacadeMocker().getMensagem("").mock());
	}

	private ParametroClienteService createParametroClienteService(boolean answerProposta, boolean answerSimulacao) {
		return new ParametroClienteServiceMocker()
					.existParametroByIdProposta(answerProposta)
					.existParametroByIdSimulacao(answerSimulacao)
					.mock();
	}
	
	@Test
	public void validateSimulacaoPropostaWithNullValidadeProposta(){
		try {
			simulacaoService.validateSimulacaoProposta(new Simulacao());
		} catch (BusinessException e) {
			Assert.assertTrue(e.getMessage().contains("LMS-04109"));
		}
	}
	
	@Test
	public void validateSimulacaoPropostaWithoutParametro(){
		try {
			simulacaoService.validateSimulacaoProposta(createSimulacao());
		} catch (BusinessException e) {
			Assert.assertTrue(e.getMessage().contains("LMS-30048"));
		}
		
	}
	
	@Test
	public void validateSimulacaoPropostaWithoutProposta(){
		try {
			Simulacao simulacao = createSimulacao();
			simulacao.setTpGeracaoProposta(new DomainValue("C"));
			
			simulacaoService.validateSimulacaoProposta(simulacao);
		} catch (BusinessException e) {
			Assert.assertTrue(e.getMessage().contains("LMS-01031"));
		}
	}

	private Simulacao createSimulacao() {
		Simulacao simulacao = new Simulacao();
		TabelaPreco tabelaPreco = new TabelaPreco();
		SubtipoTabelaPreco subtipoTabelaPreco = new SubtipoTabelaPreco();
		subtipoTabelaPreco.setTpSubtipoTabelaPreco("E");
		tabelaPreco.setSubtipoTabelaPreco(subtipoTabelaPreco);
		simulacao.setTabelaPreco(tabelaPreco);
		return simulacao;
	}


}
