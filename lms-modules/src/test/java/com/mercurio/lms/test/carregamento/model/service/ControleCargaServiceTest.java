package com.mercurio.lms.test.carregamento.model.service;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.mocks.ParametroGeralServiceMocker;

public class ControleCargaServiceTest {
	
	private ControleCargaService controleCargaService;
	
	@BeforeTest
	public void init(){
		controleCargaService = new ControleCargaService();
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().mock());
	}
	
	@Test
	public void getHorasSomarByDiaSemanaMonday(){
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(1), 0);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaFriday(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().findByNomeParametro("TEMPO_LIMITE_ROTA_SEXTA", "54").mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(5), 54);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaSaturday(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().findByNomeParametro("TEMPO_LIMITE_ROTA_SABADO", "30").mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(6), 30);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaSunday(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().findByNomeParametro("TEMPO_LIMITE_ROTA_DOMINGO", "30").mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(7), 30);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaWithoutParametroGeral(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(7), 0);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaSundayWithoutDsConteudo(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().findByNomeParametro("TEMPO_LIMITE_ROTA_DOMINGO", "").mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(7), 0);
	}
	
	@Test
	public void getHorasSomarByDiaSemanaFridayWithDoubleValue(){
		controleCargaService.setParametroGeralService(new ParametroGeralServiceMocker().findByNomeParametro("TEMPO_LIMITE_ROTA_SEXTA", "30.5").mock());
		Assert.assertEquals(controleCargaService.getHorasSomarByDiaSemana(5), 30);
	}

}
