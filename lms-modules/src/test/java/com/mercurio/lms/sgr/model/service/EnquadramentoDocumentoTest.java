package com.mercurio.lms.sgr.model.service;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_COLETA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_ENTREGA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_VIAGEM;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.model.NaturezaProdutoBuilder;
import com.mercurio.lms.municipios.model.FilialBuilder;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.DoctoServicoDTOBuilder;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.EnquadramentoRegraBuilder;
import com.mercurio.lms.sgr.model.PlanoGerenciamentoRiscoDTOBuilder;
import com.mercurio.lms.vendas.model.ClienteBuilder;

public class EnquadramentoDocumentoTest {

	private PlanoGerenciamentoRiscoService service;

	@BeforeTest
	public void beforeTest() {
		service = new PlanoGerenciamentoRiscoService();
	}

	@Test(description = "Documentos sem regra de enquadramento encontrada")
	public void testNaoEncontrados() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.internacional()
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento)
				.regraCliente(EnquadramentoRegraBuilder.newEnquadramentoRegra()
						.nacional()
						.build())
				.build();

		service.executeEnquadramentoDocumento(documento, plano);
		Assert.assertEquals(plano.getNaoEncontrados().size(), 1);
		Assert.assertEquals(plano.getNaoEncontrados().iterator().next(), documento);
	}

	@Test(description = "Múltiplas regras de enquadramento para o documento")
	public void testDuplicados() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.nacional()
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(documento)
				.regraCliente(EnquadramentoRegraBuilder.newEnquadramentoRegra()
						.id(1)
						.nacional()
						.build())
				.build();

		service.executeEnquadramentoDocumento(documento, plano);
		Assert.assertNull(plano.getDuplicados());

		plano.getRegrasCliente().add(EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.nacional()
				.build());

		service.executeEnquadramentoDocumento(documento, plano);
		Assert.assertEquals(plano.getDuplicados().size(), 1);
		Assert.assertEquals(plano.getDuplicados().iterator().next(), documento);
	}

	@Test(description = "Regras de enquadramento relacionadas aos documentos")
	public void testDocumentosRegra() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(0.1)
				.coleta()
				.remetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.destinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.nacional()
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.filialOrigem(FilialBuilder.POA())
				.filialDestino(FilialBuilder.SAO())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(1)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();

		service.executeEnquadramentoDocumento(documento1, plano);
		service.executeEnquadramentoDocumento(documento2, plano);
		service.executeEnquadramentoDocumento(documento3, plano);
		Assert.assertEquals(plano.getDocumentosRegra().size(), 2);
		Assert.assertEquals(plano.getDocumentosRegra().get(regra1).size(), 2);
		Assert.assertTrue(plano.getDocumentosRegra().get(regra1).contains(documento1));
		Assert.assertTrue(plano.getDocumentosRegra().get(regra1).contains(documento2));
		Assert.assertEquals(plano.getDocumentosRegra().get(regra2).size(), 1);
		Assert.assertTrue(plano.getDocumentosRegra().get(regra2).contains(documento3));
	}

	@Test(description = "Totalização dos valores de mercadoria por regra de enquadramento")
	public void testTotalRegra() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(0.1)
				.coleta()
				.remetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.destinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.nacional()
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.filialOrigem(FilialBuilder.POA())
				.filialDestino(FilialBuilder.SAO())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(1)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		EnquadramentoRegra regra1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		EnquadramentoRegra regra2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.regraCliente(regra1)
				.regraCliente(regra2)
				.build();

		service.executeEnquadramentoDocumento(documento1, plano);
		service.executeEnquadramentoDocumento(documento2, plano);
		service.executeEnquadramentoDocumento(documento3, plano);
		Assert.assertEquals(plano.getTotaisRegra().size(), 2);
		Assert.assertEquals(plano.getTotaisRegra().get(regra1), BigDecimal.valueOf(10.1));
		Assert.assertEquals(plano.getTotaisRegra().get(regra2), BigDecimal.valueOf(1.0));
	}

	@Test(description = "Totalização dos valores de mercadoria por natureza do produto")
	public void testTotalNaturezaProduto() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(1)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.regraCliente(EnquadramentoRegraBuilder.newEnquadramentoRegra()
						.build())
				.build();

		Long idNaturezaProduto1 = NaturezaProdutoBuilder.ELETRO_ELETRONICOS().getIdNaturezaProduto();
		Long idNaturezaProduto2 = NaturezaProdutoBuilder.COSMETICOS().getIdNaturezaProduto();
		plano.sumTotalNaturezaProduto(idNaturezaProduto1, BigDecimal.ONE);

		service.executeEnquadramentoDocumento(documento1, plano);
		Assert.assertEquals(plano.getTotaisNaturezaProduto().size(), 1);
		Assert.assertEquals(plano.getTotaisNaturezaProduto().get(idNaturezaProduto1), BigDecimal.valueOf(11.0));

		service.executeEnquadramentoDocumento(documento2, plano);
		Assert.assertEquals(plano.getTotaisNaturezaProduto().size(), 2);
		Assert.assertEquals(plano.getTotaisNaturezaProduto().get(idNaturezaProduto1), BigDecimal.valueOf(11.0));
		Assert.assertEquals(plano.getTotaisNaturezaProduto().get(idNaturezaProduto2), BigDecimal.valueOf(1.0));
	}

	@Test(description = "Totalização dos valores de mercadoria por tipo de operação")
	public void testTotalTpOperacao() {
		DoctoServicoDTO documento1 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.coleta()
				.build();
		DoctoServicoDTO documento2 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(1)
				.entrega()
				.build();
		DoctoServicoDTO documento3 = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(0.1)
				.viagem()
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.regraCliente(EnquadramentoRegraBuilder.newEnquadramentoRegra()
						.build())
				.build();

		plano.sumTotalTpOperacao(TP_OPERACAO_COLETA, BigDecimal.ONE);

		service.executeEnquadramentoDocumento(documento1, plano);
		Assert.assertEquals(plano.getTotaisTpOperacao().size(), 1);
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_COLETA), BigDecimal.valueOf(11.0));

		service.executeEnquadramentoDocumento(documento2, plano);
		Assert.assertEquals(plano.getTotaisTpOperacao().size(), 2);
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_COLETA), BigDecimal.valueOf(11.0));
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_ENTREGA), BigDecimal.valueOf(1.0));

		service.executeEnquadramentoDocumento(documento3, plano);
		Assert.assertEquals(plano.getTotaisTpOperacao().size(), 3);
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_COLETA), BigDecimal.valueOf(11.0));
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_ENTREGA), BigDecimal.valueOf(1.0));
		Assert.assertEquals(plano.getTotaisTpOperacao().get(TP_OPERACAO_VIAGEM), BigDecimal.valueOf(0.1));
	}

}
