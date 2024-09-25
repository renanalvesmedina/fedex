package com.mercurio.lms.sgr.model.service;

import java.math.BigDecimal;

import com.mercurio.lms.expedicao.model.NaturezaProdutoBuilder;
import com.mercurio.lms.integration.IntegrationTestBase;
import com.mercurio.lms.seguros.model.ApoliceSeguroBuilder;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.DoctoServicoDTOBuilder;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.EnquadramentoRegraBuilder;
import com.mercurio.lms.sgr.model.FaixaDeValorBuilder;
import com.mercurio.lms.sgr.model.PlanoGerenciamentoRiscoDTOBuilder;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.vendas.model.SeguroClienteBuilder;

public class PlanoGerenciamentoRiscoServiceTest extends IntegrationTestBase {

	PlanoGerenciamentoRiscoService service;

	public void testNaoEncontrados() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.CALCADOS())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getNaoEncontrados().size());
		assertEquals(documento, plano.getNaoEncontrados().iterator().next());
	}

	public void testDuplicados() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getDuplicados().size());
		assertEquals(documento, plano.getDuplicados().iterator().next());
	}

	public void testDocumentosRegra() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(30000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.coleta()
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.entrega()
				.build();
		EnquadramentoRegra regra3 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(3)
				.descricao("REGRA 3")
				.viagem()
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento1)
				.documento(documento2)
				.documento(documento3)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.regraCliente(regra3)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(2, plano.getRegrasEnquadramento().size());
		assertEquals(2, plano.getDocumentosRegra(regra1).size());
		assertTrue(plano.getDocumentosRegra(regra1).contains(documento1));
		assertTrue(plano.getDocumentosRegra(regra1).contains(documento2));
		assertEquals(1, plano.getDocumentosRegra(regra2).size());
		assertTrue(plano.getDocumentosRegra(regra2).contains(documento3));
	}

	public void testTotalRegra() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(30000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.coleta()
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.entrega()
				.build();
		EnquadramentoRegra regra3 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(3)
				.descricao("REGRA 3")
				.viagem()
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento1)
				.documento(documento2)
				.documento(documento3)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.regraCliente(regra3)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(2, plano.getTotaisRegra().size());
		assertEquals(BigDecimal.valueOf(2100.0), plano.getTotalRegra(regra1));
		assertEquals(BigDecimal.valueOf(30000.0), plano.getTotalRegra(regra2));
	}

	public void testTotalNaturezaProduto() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.CALCADOS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(30000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.CALCADOS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento1)
				.documento(documento2)
				.documento(documento3)
				.regraCliente(regra1)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(2, plano.getTotaisNaturezaProduto().size());
		assertEquals(BigDecimal.valueOf(100.0), plano.getTotalNaturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS().getIdNaturezaProduto()));
		assertEquals(BigDecimal.valueOf(32000.0), plano.getTotalNaturezaProduto(NaturezaProdutoBuilder.CALCADOS().getIdNaturezaProduto()));
	}

	public void testTotalTpOperacao() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(30000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento1)
				.documento(documento2)
				.documento(documento3)
				.regraCliente(regra1)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(2, plano.getTotaisTpOperacao().size());
		assertEquals(BigDecimal.valueOf(30100.0), plano.getTotalTpOperacao(ConstantesGerRisco.TP_OPERACAO_COLETA));
		assertEquals(BigDecimal.valueOf(2000.0), plano.getTotalTpOperacao(ConstantesGerRisco.TP_OPERACAO_ENTREGA));
	}

	public void testDuplicadosGeral() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA GERAL 1")
				.regraGeral()
				.build();
		EnquadramentoRegra regra3 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(3)
				.descricao("REGRA GERAL 2")
				.regraGeral()
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.verificacaoGeral()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraGeral(regra2)
				.regraGeral(regra3)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getDuplicados().size());
		assertEquals(BigDecimal.valueOf(2100.0), plano.getDuplicados().iterator().next().getVlMercadoria());
	}

	public void testDocumentosRegraGeral() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA GERAL 1")
				.regraGeral()
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.verificacaoGeral()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraGeral(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getDocumentosRegra(regra2).size());
		assertEquals(BigDecimal.valueOf(2100.0), plano.getDocumentosRegra(regra2).iterator().next().getVlMercadoria());
	}

	public void testTotalRegraGeral() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA GERAL 1")
				.regraGeral()
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.verificacaoGeral()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraGeral(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(BigDecimal.valueOf(2100.0), plano.getTotalRegra(regra2));
	}

	public void testFaixaInexistente() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0, 500)
						.build())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getFaixaInexistente().size());
		assertTrue(plano.getFaixaInexistente().contains(regra));
	}

	public void testMaximoCarregamento() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.apoliceSeguro(ApoliceSeguroBuilder.newApoliceSeguro()
						.limiteControleCarga(500)
						.build())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.seguroCliente(SeguroClienteBuilder.newSeguroCliente()
						.numero("20000")
						.tipo(2)
						.seguradora(2)
						.limiteControleCarga(500)
						.build())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.validaMaximoCarregamento()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(2, plano.getLimiteApolice().size());
		assertTrue(plano.getLimiteApolice().contains(regra1));
		assertTrue(plano.getLimiteApolice().contains(regra2));
	}

	public void testCargaExclusiva() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0)
						.exclusivaCliente()
						.build())
				.apoliceSeguro(ApoliceSeguroBuilder.newApoliceSeguro()
						.numero("10000")
						.tipo(1)
						.seguradora(1)
						.build())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.validaCargaExclusivaCliente()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getCargaExclusiva().size());
		assertEquals(regra1, plano.getCargaExclusiva().iterator().next());
	}

	public void testFaixaLiberacaoCemop() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0)
						.requerLiberacaoCemop()
						.build())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.validaLiberacaoCemop()
				.documento(documento)
				.regraCliente(regra)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getLiberacaoCemop().size());
		assertEquals(regra, plano.getLiberacaoCemop().iterator().next());
	}

	public void testNaturezaImpedida() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.CALCADOS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0)
						.naturezaImpedida(NaturezaProdutoBuilder.CALCADOS(), 500)
						.build())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 2")
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.validaNaturezaImpedida()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getNaturezasImpedidas().size());
		assertEquals(1, plano.getNaturezasImpedidas(regra1).size());
		assertEquals(NaturezaProdutoBuilder.CALCADOS(), plano.getNaturezasImpedidas(regra1).iterator().next());
	}

	public void testMensagensEnquadramento() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(30000)
				.viagem()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 2")
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		EnquadramentoRegra regra3 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 3")
				.viagem()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0, 500)
						.build())
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.realizaBloqueios()
				.documento(documento1)
				.documento(documento2)
				.documento(documento3)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.regraCliente(regra3)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getNaoEncontrados().size());
		assertEquals(documento1, plano.getNaoEncontrados().iterator().next());
		assertEquals(1, plano.getDuplicados().size());
		assertEquals(documento2, plano.getDuplicados().iterator().next());
		assertEquals(1, plano.getDuplicados().size());
		assertEquals(regra3, plano.getFaixaInexistente().iterator().next());
		assertEquals(6, plano.getMensagensEnquadramento().size());
	}

	public void testMensagensWorkflowRegras() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(100)
				.coleta()
				.naturezaProduto(NaturezaProdutoBuilder.AUTO_PECAS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(2000)
				.entrega()
				.naturezaProduto(NaturezaProdutoBuilder.CALCADOS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.descricao("REGRA 1")
				.faixa(FaixaDeValorBuilder.newFaixaDeValor()
						.limite(0)
						.requerLiberacaoCemop()
						.naturezaImpedida(NaturezaProdutoBuilder.CALCADOS())
						.exclusivaCliente()
						.build())
				.apoliceSeguro(ApoliceSeguroBuilder.newApoliceSeguro()
						.numero("10000")
						.tipo(1)
						.seguradora(1)
						.limiteControleCarga(500)
						.build())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.descricao("REGRA 2")
				.build();

		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.realizaBloqueios()
				.validaMaximoCarregamento()
				.validaCargaExclusivaCliente()
				.validaLiberacaoCemop()
				.validaNaturezaImpedida()
				.documento(documento1)
				.documento(documento2)
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();
		service.executeEnquadramentoRegra(plano);

		assertNotNull(service);
		assertEquals(1, plano.getLimiteApolice().size());
		assertEquals(regra1, plano.getLimiteApolice().iterator().next());
		assertEquals(1, plano.getCargaExclusiva().size());
		assertEquals(regra1, plano.getCargaExclusiva().iterator().next());
		assertEquals(1, plano.getLiberacaoCemop().size());
		assertEquals(regra1, plano.getLiberacaoCemop().iterator().next());
		assertEquals(1, plano.getNaturezasImpedidas().size());
		assertEquals(1, plano.getNaturezasImpedidas(regra1).size());
		assertEquals(NaturezaProdutoBuilder.CALCADOS(), plano.getNaturezasImpedidas(regra1).iterator().next());
		assertEquals(4, plano.getMensagensWorkflowRegras().size());
	}

	public void setPlanoGerenciamentoRiscoService(PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
		this.service = planoGerenciamentoRiscoService;
	}

}
