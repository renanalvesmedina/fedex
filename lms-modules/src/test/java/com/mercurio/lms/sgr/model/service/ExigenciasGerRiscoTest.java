package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.municipios.model.FilialBuilder;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.sgr.model.EnquadramentoRegraBuilder;
import com.mercurio.lms.sgr.model.ExigenciaGerRiscoBuilder;
import com.mercurio.lms.sgr.model.ExigenciaGerRiscoDTOBuilder;

import edu.emory.mathcs.backport.java.util.Collections;

public class ExigenciasGerRiscoTest {

	private PlanoGerenciamentoRiscoService service;

	@BeforeTest
	public void beforeTest() {
		service = new PlanoGerenciamentoRiscoService();
	}

	@Test(description = "Sem redu��o para mesma regra de enquadramento")
	public void testRegraEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertTrue(result.isEmpty());
	}

	@Test(description = "Redu��o para exig�ncias iguais")
	public void testTipoEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1) || result.contains(dto2));
	}

	@Test(description = "Sem redu��o para exig�ncias de tipos diferentes")
	public void testTipoNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertTrue(result.isEmpty());
	}

	@Test(description = "Redu��o para exig�ncias de escolta ponta-a-ponta com maior quantidade")
	public void testEscoltaEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(1)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(10)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto2));
	}

	@Test(description = "Redu��o para exig�ncias de 2 escoltas ponta-a-ponta e 1 escolta de entrada")
	public void testEscoltaNotEqual1() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(2)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1));
	}

	@Test(description = "Redu��o para exig�ncias de 1 escolta ponta-a-ponta e 2 escoltas de entrada")
	public void testEscoltaNotEqual2() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(1)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(2)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 2);
		Assert.assertTrue(result.remove(dto1));
		Assert.assertEquals(dto1.getQtExigida(), Integer.valueOf(1));
		Assert.assertEquals(dto2.getQtExigida(), Integer.valueOf(2));
		Assert.assertEquals(result.iterator().next().getQtExigida(), Integer.valueOf(1));
	}

	@Test(description = "Redu��o para exig�ncias escolta de entrada com quilometragens diferentes")
	public void testEscoltaKmNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(400)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto2));
	}

	@Test(description = "Sem redu��o para exig�ncias de escolta com diferentes filiais")
	public void testEscoltaFilialNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.SAO())
				.km(200)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertTrue(result.isEmpty());
	}

	@Test(description = "Redu��o para exig�ncias de monitoramento de mesmo n�vel")
	public void testMonitoramentoEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_1())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1) || result.contains(dto2));
	}

	@Test(description = "Redu��o para exig�ncias de monitoramento de n�veis diferentes")
	public void testMonitoramentoNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_2())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1));
	}

	@Test(description = "Redu��o para exig�ncias de motorista de mesmo n�vel")
	public void testMotoristaEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_1())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1) || result.contains(dto2));
	}

	@Test(description = "Redu�ao para exig�ncias de motorista de n�veis diferentes")
	public void testMotoristaNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_2())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1));
	}

	@Test(description = "Redu��o para exig�ncias de ve�culo de mesmo n�vel")
	public void testVeiculoEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_1())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1) || result.contains(dto2));
	}

	@Test(description = "Redu��o para exig�ncias de ve�culo de n�veis diferentes")
	public void testVeiculoNotEqual() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_2())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1));
	}

	@Test(description = "Redu��o para exig�ncias de averba��o iguais")
	public void testAverbacao() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertTrue(result.contains(dto1) || result.contains(dto2));
	}

	@Test(description = "Redu��o para exig�ncias de v�rus com soma de quantidades")
	public void testVirus() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(1)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(2)
				.build();

		Collection<ExigenciaGerRiscoDTO> result = service.reduceExigenciasGerRisco(dto1, dto2);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertEquals(result.iterator().next().getExigenciaGerRisco(), ExigenciaGerRiscoBuilder.VIRUS());
		Assert.assertEquals(result.iterator().next().getQtExigida(), Integer.valueOf(3));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de escolta ponta-a-ponta e de entrada")
	public void testEscoltas() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(2)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();
		ExigenciaGerRiscoDTO dto3 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.SAO())
				.km(400)
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		dtos.add(dto3);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 2);
		Assert.assertTrue(dtos.contains(dto1));
		Assert.assertTrue(dtos.contains(dto2));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de monitoramento")
	public void testMonitoramentos() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_2())
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 1);
		Assert.assertTrue(dtos.contains(dto1));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de motorista")
	public void testMotoristas() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_2())
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 1);
		Assert.assertTrue(dtos.contains(dto1));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de ve�culo")
	public void testVeiculos() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_1())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_2())
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 1);
		Assert.assertTrue(dtos.contains(dto1));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de averba��o")
	public void testAverbacoes() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 1);
		Assert.assertTrue(dtos.contains(dto1) || dtos.contains(dto2));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de v�rus")
	public void testViruses() {
		ExigenciaGerRiscoDTO dto1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(1)
				.build();
		ExigenciaGerRiscoDTO dto2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(2)
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dto1);
		dtos.add(dto2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 1);
		Assert.assertFalse(dtos.contains(dto1));
		Assert.assertFalse(dtos.contains(dto2));
		Assert.assertNull(dtos.iterator().next().getEnquadramentoRegra());
		Assert.assertEquals(dtos.iterator().next().getExigenciaGerRisco(), ExigenciaGerRiscoBuilder.VIRUS());
		Assert.assertEquals(dtos.iterator().next().getQtExigida(), Integer.valueOf(3));
	}

	@Test(description = "Redu��o para m�ltiplas exig�ncias de diferentes tipos")
	public void testExigencias() {
		ExigenciaGerRiscoDTO dtoEscolta1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_PP())
				.quantidade(2)
				.build();
		ExigenciaGerRiscoDTO dtoEscolta2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.POA())
				.km(200)
				.build();
		ExigenciaGerRiscoDTO dtoEscolta3 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.ESCOLTA_ENTRADA())
				.quantidade(1)
				.filial(FilialBuilder.SAO())
				.km(400)
				.build();
		ExigenciaGerRiscoDTO dtoMonitoramento1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_1())
				.build();
		ExigenciaGerRiscoDTO dtoMonitoramento2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MONITORAMENTO_2())
				.build();
		ExigenciaGerRiscoDTO dtoMotorista1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_1())
				.build();
		ExigenciaGerRiscoDTO dtoMotorista2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.MOTORISTA_2())
				.build();
		ExigenciaGerRiscoDTO dtoVeiculo1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_1())
				.build();
		ExigenciaGerRiscoDTO dtoVeiculo2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VEICULO_2())
				.build();
		ExigenciaGerRiscoDTO dtoAverbacao1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		ExigenciaGerRiscoDTO dtoAverbacao2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
		ExigenciaGerRiscoDTO dtoVirus1 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_1())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(1)
				.build();
		ExigenciaGerRiscoDTO dtoVirus2 = ExigenciaGerRiscoDTOBuilder.newDoctoServicoDTO()
				.regra(EnquadramentoRegraBuilder.REGRA_2())
				.exigencia(ExigenciaGerRiscoBuilder.VIRUS())
				.quantidade(2)
				.build();
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		dtos.add(dtoEscolta1);
		dtos.add(dtoEscolta2);
		dtos.add(dtoEscolta3);
		dtos.add(dtoMonitoramento1);
		dtos.add(dtoMonitoramento2);
		dtos.add(dtoMotorista1);
		dtos.add(dtoMotorista2);
		dtos.add(dtoVeiculo1);
		dtos.add(dtoVeiculo2);
		dtos.add(dtoAverbacao1);
		dtos.add(dtoAverbacao2);
		dtos.add(dtoVirus1);
		dtos.add(dtoVirus2);
		Collections.shuffle(dtos);

		service.reduceExigenciasGerRisco(dtos);
		Assert.assertEquals(dtos.size(), 7);
		Assert.assertTrue(dtos.contains(dtoEscolta1));
		Assert.assertTrue(dtos.contains(dtoEscolta2));
		Assert.assertTrue(dtos.contains(dtoMonitoramento1));
		Assert.assertTrue(dtos.contains(dtoMotorista1));
		Assert.assertTrue(dtos.contains(dtoVeiculo1));
		Assert.assertTrue(dtos.contains(dtoAverbacao1) || dtos.contains(dtoAverbacao2));
		Assert.assertTrue(!dtos.contains(dtoAverbacao1) || !dtos.contains(dtoAverbacao2));
		Assert.assertFalse(dtos.contains(dtoVirus1));
		Assert.assertFalse(dtos.contains(dtoVirus2));
	}

}
