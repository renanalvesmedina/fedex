package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ConhecimentoBuilder;
import com.mercurio.lms.integration.IntegrationTestBase;
import com.mercurio.lms.municipios.model.FilialBuilder;
import com.mercurio.lms.municipios.model.UnidadeFederativaBuilder;
import com.mercurio.lms.vendas.model.ClienteBuilder;

public class CalculoFreteServiceTest extends IntegrationTestBase {

	private CalculoFreteService calculoFreteService;
	
	public void testExecuteCalculoCTRNormal() {
		Conhecimento conhecimento = ConhecimentoBuilder
						.novoConhecimento()
						.filialOrigem(FilialBuilder.newFilial().portoAlegre())
						.build();
		
		CalculoFrete calculoFrete = CalculoFreteBuilder
						.novoCalculoFreteCom()
						.calculoNormal()
						.conhecimentoNormal()
						.abrangenciaNacional()
						.modalRodoviario()
						.freteFOB()
						.servicoRodoviarioNacionalConvencional()
						.comMercadoriasNoValorDe(new BigDecimal("1000.00"))
						.comPesoCubadoDe(new BigDecimal("10"))
						.comPesoRealDe(new BigDecimal("10"))
						.volumes(1)
						.calculandoImpostos()
						.calculandoValorDasParcelas()
						.comRestricaoRotaDestino(RestricaoRotaBuilder.novaRestricaoRotaCom()
										.municipio(RestricaoRotaBuilder.RIBEIRAO_PRETO)
										.build())
						.comRestricaoRotaOrigem(RestricaoRotaBuilder.novaRestricaoRotaCom()
										.municipio(RestricaoRotaBuilder.PORTO_ALEGRE)
										.build())
						.paraCliente(ClienteBuilder.newCliente()
										.especial()
										.build())
						.dadosCliente(DoctoServicoDadosClienteBuilder.novosDados()
										.ufRementente(UnidadeFederativaBuilder.RS().getIdUnidadeFederativa())
										.situacaoTributariaRemetente("CO")
										.situacaoTributariaDestinatario("CO")
										.filialTransacao(FilialBuilder.newFilial().portoAlegre())
										.build())
						.divisaoCliente(29042L)
						.conhecimento(conhecimento)
						.build();
		
		calculoFreteService.executeCalculoCTRNormal(calculoFrete);
		
		assertNotNull(calculoFrete);
		assertEquals(5, calculoFrete.getParcelas().size());
		assertEquals(5, calculoFrete.getParcelasGerais().size());
		assertEquals(new BigDecimal("128.35"), calculoFrete.getVlTotal());
		assertEquals(new BigDecimal("128.35"), calculoFrete.getVlDevido());
		assertEquals(new BigDecimal("0.00"), calculoFrete.getVlTotalServicosAdicionais());
		assertEquals(new BigDecimal("0.00"), calculoFrete.getVlDesconto());
		assertEquals(new BigDecimal("0.0333"), calculoFrete.getNrCubagemCalculo());
	}
	
	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}
	
}
