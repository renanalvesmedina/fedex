package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.model.NaturezaProdutoBuilder;
import com.mercurio.lms.municipios.model.FilialBuilder;
import com.mercurio.lms.municipios.model.MunicipioBuilder;
import com.mercurio.lms.municipios.model.PaisBuilder;
import com.mercurio.lms.municipios.model.UnidadeFederativaBuilder;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.model.DoctoServicoDTOBuilder;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.EnquadramentoRegraBuilder;
import com.mercurio.lms.vendas.model.ClienteBuilder;

import edu.emory.mathcs.backport.java.util.Collections;

public class EnquadramentoRegraTest {

	private PlanoGerenciamentoRiscoService service;

	private EnquadramentoRegra regraCliente1Remetente;
	private EnquadramentoRegra regraCliente2Remetente;
	private EnquadramentoRegra regraCliente1Destinatario;
	private EnquadramentoRegra regraCliente2Destinatario;
	private EnquadramentoRegra regraCliente1RemetenteDestinatario;
	private EnquadramentoRegra regraCliente2RemetenteDestinatario;
	private EnquadramentoRegra regraTpAbrangenciaNacional;
	private EnquadramentoRegra regraTpAbrangenciaInternacional;
	private EnquadramentoRegra regraNaturezaProduto1;
	private EnquadramentoRegra regraNaturezaProduto2;
	private EnquadramentoRegra regraTpOperacaoColeta;
	private EnquadramentoRegra regraTpOperacaoEntrega;
	private EnquadramentoRegra regraTpOperacaoViagem;
	private EnquadramentoRegra regraPaisOrigem1;
	private EnquadramentoRegra regraPaisOrigem2;
	private EnquadramentoRegra regraPaisDestino1;
	private EnquadramentoRegra regraPaisDestino2;
	private EnquadramentoRegra regraUnidadeFederativaOrigem1;
	private EnquadramentoRegra regraUnidadeFederativaOrigem2;
	private EnquadramentoRegra regraUnidadeFederativaDestino1;
	private EnquadramentoRegra regraUnidadeFederativaDestino2;
	private EnquadramentoRegra regraMunicipioOrigem1;
	private EnquadramentoRegra regraMunicipioOrigem2;
	private EnquadramentoRegra regraMunicipioDestino1;
	private EnquadramentoRegra regraMunicipioDestino2;
	private EnquadramentoRegra regraFilialOrigem1;
	private EnquadramentoRegra regraFilialOrigem2;
	private EnquadramentoRegra regraFilialDestino1;
	private EnquadramentoRegra regraFilialDestino2;
	private EnquadramentoRegra regraFull;
	private EnquadramentoRegra regraEmpty;

	private List<EnquadramentoRegra> regras;

	@BeforeTest
	public void beforeTest() {
		service = new PlanoGerenciamentoRiscoService();
	}

	@BeforeMethod
	public void beforeMethod() {
		regraCliente1Remetente = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(1)
				.clienteRemetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.build();
		regraCliente2Remetente = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(2)
				.clienteRemetente(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.build();
		regraCliente1Destinatario = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(3)
				.clienteDestinatario(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.build();
		regraCliente2Destinatario = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(4)
				.clienteDestinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.build();
		regraCliente1RemetenteDestinatario = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(5)
				.clienteRemetenteDestinatario(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.build();
		regraCliente2RemetenteDestinatario = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(6)
				.clienteRemetenteDestinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.build();
		regraTpAbrangenciaNacional = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(7)
				.nacional()
				.build();
		regraTpAbrangenciaInternacional = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(8)
				.internacional()
				.build();
		regraNaturezaProduto1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(9)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();
		regraNaturezaProduto2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(10)
				.naturezaProduto(NaturezaProdutoBuilder.COSMETICOS())
				.build();
		regraTpOperacaoColeta = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(11)
				.coleta()
				.build();
		regraTpOperacaoEntrega = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(12)
				.entrega()
				.build();
		regraTpOperacaoViagem = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(13)
				.viagem()
				.build();
		regraPaisOrigem1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(14)
				.paisOrigem(PaisBuilder.BRASIL())
				.build();
		regraPaisOrigem2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(15)
				.paisOrigem(PaisBuilder.ESTADOS_UNIDOS())
				.build();
		regraPaisDestino1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(16)
				.paisDestino(PaisBuilder.BRASIL())
				.build();
		regraPaisDestino2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(17)
				.paisDestino(PaisBuilder.ESTADOS_UNIDOS())
				.build();
		regraUnidadeFederativaOrigem1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(18)
				.unidadeFederativaOrigem(UnidadeFederativaBuilder.RS())
				.build();
		regraUnidadeFederativaOrigem2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(19)
				.unidadeFederativaOrigem(UnidadeFederativaBuilder.SP())
				.build();
		regraUnidadeFederativaDestino1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(20)
				.unidadeFederativaDestino(UnidadeFederativaBuilder.RS())
				.build();
		regraUnidadeFederativaDestino2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(21)
				.unidadeFederativaDestino(UnidadeFederativaBuilder.SP())
				.build();
		regraMunicipioOrigem1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(22)
				.municipioOrigem(MunicipioBuilder.PORTO_ALEGRE())
				.build();
		regraMunicipioOrigem2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(23)
				.municipioOrigem(MunicipioBuilder.SAO_PAULO())
				.build();
		regraMunicipioDestino1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(24)
				.municipioDestino(MunicipioBuilder.PORTO_ALEGRE())
				.build();
		regraMunicipioDestino2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(25)
				.municipioDestino(MunicipioBuilder.SAO_PAULO())
				.build();
		regraFilialOrigem1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(26)
				.filialOrigem(FilialBuilder.POA())
				.build();
		regraFilialOrigem2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(27)
				.filialOrigem(FilialBuilder.SAO())
				.build();
		regraFilialDestino1 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(28)
				.filialDestino(FilialBuilder.POA())
				.build();
		regraFilialDestino2 = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(29)
				.filialDestino(FilialBuilder.SAO())
				.build();
		regraFull = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(30)
				.clienteRemetenteDestinatario(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.clienteRemetenteDestinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.nacional()
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.coleta()
				.filialOrigem(FilialBuilder.POA())
				.filialOrigem(FilialBuilder.SAO())
				.filialDestino(FilialBuilder.POA())
				.filialDestino(FilialBuilder.SAO())
				.build();
		regraEmpty = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.id(31)
				.build();

		regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraCliente1Remetente);
		regras.add(regraCliente2Remetente);
		regras.add(regraCliente1Destinatario);
		regras.add(regraCliente2Destinatario);
		regras.add(regraCliente1RemetenteDestinatario);
		regras.add(regraCliente2RemetenteDestinatario);
		regras.add(regraTpAbrangenciaNacional);
		regras.add(regraTpAbrangenciaInternacional);
		regras.add(regraNaturezaProduto1);
		regras.add(regraNaturezaProduto2);
		regras.add(regraTpOperacaoColeta);
		regras.add(regraTpOperacaoEntrega);
		regras.add(regraTpOperacaoViagem);
		regras.add(regraPaisOrigem1);
		regras.add(regraPaisOrigem2);
		regras.add(regraPaisDestino1);
		regras.add(regraPaisDestino2);
		regras.add(regraUnidadeFederativaOrigem1);
		regras.add(regraUnidadeFederativaOrigem2);
		regras.add(regraUnidadeFederativaDestino1);
		regras.add(regraUnidadeFederativaDestino2);
		regras.add(regraMunicipioOrigem1);
		regras.add(regraMunicipioOrigem2);
		regras.add(regraMunicipioDestino1);
		regras.add(regraMunicipioDestino2);
		regras.add(regraFilialOrigem1);
		regras.add(regraFilialOrigem2);
		regras.add(regraFilialDestino1);
		regras.add(regraFilialDestino2);
		regras.add(regraFull);
		regras.add(regraEmpty);
		Collections.shuffle(regras);
	}

	@Test(description = "Filtra regra para cliente remetente específico")
	public void testClienteRemetente() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.remetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 2);
		Assert.assertTrue(regras.contains(regraCliente1Remetente));
		Assert.assertTrue(regras.contains(regraCliente1RemetenteDestinatario));
	}

	@Test(description = "Filtra regra para cliente destinatário específico")
	public void testClienteDestinatario() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.destinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 2);
		Assert.assertTrue(regras.contains(regraCliente2Destinatario));
		Assert.assertTrue(regras.contains(regraCliente2RemetenteDestinatario));
	}

	@Test(description = "Filtra regra para tipo de abrangência nacional")
	public void testTpAbrangenciaNacional() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.nacional()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraTpAbrangenciaNacional));
	}

	@Test(description = "Filtra regra para tipo de abrangência internacional")
	public void testTpAbrangenciaInternacional() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.internacional()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraTpAbrangenciaInternacional));
	}

	@Test(description = "Filtra regra para natureza de produto 1")
	public void testNaturezaProduto() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraNaturezaProduto1));
	}

	@Test(description = "Filtra regra para tipo de operação coleta")
	public void testTpOperacaoColeta() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.coleta()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraTpOperacaoColeta));
	}

	@Test(description = "Filtra regra para tipo de operação entrega")
	public void testTpOperacaoEntrega() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(1)
				.entrega()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraTpOperacaoEntrega));
	}

	@Test(description = "Filtra regra para tipo de operação viagem")
	public void testTpOperacaoViagem() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(10)
				.viagem()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraTpOperacaoViagem));
	}

	@Test(description = "Filtra regra para país de origem específico")
	public void testPaisOrigem() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.paisOrigem(PaisBuilder.BRASIL())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraPaisOrigem1));
	}

	@Test(description = "Filtra regra para país de destino específico")
	public void testPaisDestino() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.paisDestino(PaisBuilder.ESTADOS_UNIDOS())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraPaisDestino2));
	}

	@Test(description = "Filtra regra para unidade federativa de origem específica")
	public void testUnidadeFederativaOrigem() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.unidadeFederativaOrigem(UnidadeFederativaBuilder.RS())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraUnidadeFederativaOrigem1));
	}

	@Test(description = "Filtra regra para unidade federativa de destino específica")
	public void testUnidadeFederativaDestino() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.unidadeFederativaDestino(UnidadeFederativaBuilder.SP())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraUnidadeFederativaDestino2));
	}

	@Test(description = "Filtra regra para município de origem específico")
	public void testMunicipioOrigem() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.municipioOrigem(MunicipioBuilder.PORTO_ALEGRE())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraMunicipioOrigem1));
	}

	@Test(description = "Filtra regra para município de destino específico")
	public void testMunicipioDestino() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.municipioDestino(MunicipioBuilder.SAO_PAULO())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraMunicipioDestino2));
	}

	@Test(description = "Filtra regra para filial de origem específica")
	public void testFilialOrigem() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.filialOrigem(FilialBuilder.POA())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraFilialOrigem1));
	}

	@Test(description = "Filtra regra para filial de destino específica")
	public void testFilialDestino() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.filialDestino(FilialBuilder.SAO())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraFilialDestino2));
	}

	@Test(description = "Filtra regra com especificação completa")
	public void testFilterFull() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.valor(0.1)
				.coleta()
				.remetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
				.destinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
				.nacional()
				.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
				.paisOrigem(PaisBuilder.BRASIL())
				.paisDestino(PaisBuilder.ESTADOS_UNIDOS())
				.unidadeFederativaOrigem(UnidadeFederativaBuilder.RS())
				.unidadeFederativaDestino(UnidadeFederativaBuilder.SP())
				.municipioOrigem(MunicipioBuilder.PORTO_ALEGRE())
				.municipioDestino(MunicipioBuilder.SAO_PAULO())
				.filialOrigem(FilialBuilder.POA())
				.filialDestino(FilialBuilder.SAO())
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraFull));
	}

	@Test(description = "Filtra regra com especificação vazia")
	public void testFilterEmpty() {
		DoctoServicoDTO documento = DoctoServicoDTOBuilder.newDoctoServicoDTO()
				.build();

		service.filterEnquadramentoRegra(documento, regras);
		Assert.assertEquals(regras.size(), 1);
		Assert.assertTrue(regras.contains(regraEmpty));
	}

}
