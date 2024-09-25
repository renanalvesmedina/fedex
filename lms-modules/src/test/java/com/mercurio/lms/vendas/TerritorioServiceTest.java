package com.mercurio.lms.vendas;

import static com.mercurio.lms.vendas.model.service.TerritorioService.EXCEPTION_STORE_FILIAL_COM_REGIONAL_INVALIDA;
import static com.mercurio.lms.vendas.model.service.TerritorioService.EXCEPTION_STORE_NATURAL_KEY_CONFLICT;
import static com.mercurio.lms.vendas.model.service.TerritorioService.EXCEPTION_STORE_SEM_NM_TERRITORIO;
import static com.mercurio.lms.vendas.model.service.TerritorioService.EXCEPTION_STORE_SEM_REGIONAL;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.TerritorioService;


@PrepareForTest({SessionContext.class, SessionUtils.class, JTDateTimeUtils.class, TerritorioService.class, AdsmDao.class}) 
public class TerritorioServiceTest extends PowerMockTestCase {

	private TerritorioService territorioService;
	@Mock 
	private RegionalService regionalService;
	
	@ObjectFactory
	public IObjectFactory setObjectFactory() {
		return new PowerMockObjectFactory();
	}

	@BeforeMethod
	public void setup() throws Exception {

		PowerMockito.mockStatic(SessionContext.class);
		PowerMockito.mockStatic(SessionUtils.class);
		PowerMockito.mockStatic(JTDateTimeUtils.class);

		initMocks(this);

		PowerMockito.when(JTDateTimeUtils.getDataHoraAtual()).thenReturn(new DateTime());
		PowerMockito.when(JTDateTimeUtils.getDataAtual()).thenReturn(new YearMonthDay());
		
		territorioService = PowerMockito.spy(new TerritorioService());
		territorioService.setRegionalService(regionalService);
	}
	
	@Test
	public void givenNenhumTerritorio_whenCadastrarTerritorio1CompletoSemFilial_thenInclusaoComSucesso() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(1l).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test
	public void givenNenhumTerritorio_whenCadastrarTerritorio1CompletoComFilial_thenInclusaoComSucesso() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoComFilial();
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(1l).when(territorioService, "callInheritedStore", territorio1);
		doReturn(getRegional1()).when(regionalService).findRegionalAtivaByIdFilial(territorio1.getFilial().getIdFilial());
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_FILIAL_COM_REGIONAL_INVALIDA)
	public void givenNenhumTerritorio_whenCadastrarTerritorio1CompletoComFilialSemCorrespondenciaComRegional_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoComFilial();
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(1l).when(territorioService, "callInheritedStore", territorio1);
		doReturn(null).when(regionalService).findRegionalAtivaByIdFilial(territorio1.getFilial().getIdFilial());
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_REGIONAL)
	public void givenNenhumTerritorio_whenCadastrarTerritorio1SemRegional_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		territorio1.setRegional(null);
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(null).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_NM_TERRITORIO)
	public void givenNenhumTerritorio_whenCadastrarTerritorio1SemNomeAsNull_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		territorio1.setNmTerritorio(null);
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(null).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_NM_TERRITORIO)
	public void givenNenhumTerritorio_whenCadastrarTerritorio1SemNomeAsEmpty_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		territorio1.setNmTerritorio("");
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(null).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_NM_TERRITORIO)
	public void givenNenhumTerritorio_whenCadastrarTerritorio1SemNomeAsBlank_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		territorio1.setNmTerritorio("    ");
		
		//nenhum território inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(null).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_NATURAL_KEY_CONFLICT)
	public void givenTerritorio1JaCadastradoEAtivo_whenCadastrarTerritorioComMesmaChaveNatural_thenExcecao() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		
		//território 1 já cadastrado (e ativo) inicialmente
		doReturn(territorio1).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(null).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(1l).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	@Test()
	public void givenTerritorio1JaCadastradoEInativo_whenCadastrarTerritorioComMesmaChaveNatural_thenSucesso() throws Exception {
		Territorio territorio1 = getTerritorio1CompletoSemFilial();
		
		//território 1 já cadastrado (e inativo) inicialmente
		doReturn(null).when(territorioService).findAtivoPorNome(territorio1.getNmTerritorio());
		doReturn(territorio1).when(territorioService).findInativoPorNome(territorio1.getNmTerritorio());
		
		doReturn(null).when(territorioService, "callInheritedStore", territorio1);
		
		territorioService.store(territorio1);
	}
	
	
	private Territorio getTerritorio1CompletoSemFilial() {
		Territorio territorio1Completo = new Territorio();
		territorio1Completo.setRegional(getRegional1());
		territorio1Completo.setNmTerritorio("Território 1".toUpperCase());
		return territorio1Completo;
	}
	
	private Territorio getTerritorio1CompletoComFilial() {
		Territorio territorio1Completo = getTerritorio1CompletoSemFilial();
		territorio1Completo.setFilial(getFilial1());
		return territorio1Completo;
	}
	
	private Regional getRegional1() {
		Regional regional1 = new Regional();
		regional1.setIdRegional(1l);
		regional1.setDsRegional("Regional 1");
		return regional1;
	}
	
	private Filial getFilial1() {
		Filial filial = new Filial(1l);
		filial.setSgFilial("FL1");
		return filial;
	}
}
