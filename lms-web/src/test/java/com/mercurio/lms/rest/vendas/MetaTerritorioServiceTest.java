package com.mercurio.lms.rest.vendas;

import static com.mercurio.lms.vendas.model.service.MetaService.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.mockito.Matchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import br.com.tntbrasil.integracao.domains.comissionamento.MetaDTO;
import br.com.tntbrasil.integracao.domains.comissionamento.MetaTerritorioDTO;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.services.vendas.MetaRest;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Meta;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.MetaService;
import com.mercurio.lms.vendas.model.service.TerritorioService;


@PrepareForTest({SessionContext.class, SessionUtils.class, JTDateTimeUtils.class, MetaRest.class, MetaService.class}) 
public class MetaTerritorioServiceTest extends PowerMockTestCase {

	private static final String NM_TERRITORIO = "STO01";
	private static final String VL_MODAL_RODOVIARIO = "R";
	private static final String VL_MODAL_AEREO = "A";
	private static final String DM_MODAL = "DM_MODAL";
	
	private static final Territorio TERRITORIO = getTerritorio(NM_TERRITORIO);
	private static final DomainValue MODAL_RODOVIARIO = getModal(VL_MODAL_RODOVIARIO);
	private static final DomainValue MODAL_AEREO = getModal(VL_MODAL_AEREO);
	
	@Mock
	private MetaRest metaRest;
	private MetaService metaService;
	@Mock
	private TerritorioService territorioService;
	@Mock
	private DomainValueService domainValueService;
	@Mock
	private UsuarioLMSService usuarioLMSService;
	@Mock
	private ParametroGeralService parametroGeralService;
	
	@ObjectFactory
	public IObjectFactory setObjectFactory() {
		return new PowerMockObjectFactory();
	}

	@BeforeMethod
	public void setup() throws Exception {
		initMocks(this);
		
		PowerMockito.mockStatic(SessionContext.class);
		PowerMockito.mockStatic(SessionUtils.class);
		PowerMockito.mockStatic(JTDateTimeUtils.class);
		
		metaRest = PowerMockito.spy(new MetaRest());
		metaService = PowerMockito.spy(new MetaService());
		metaRest.setMetaService(metaService);
		
		metaRest.setDomainValueService(domainValueService);
		metaRest.setTerritorioService(territorioService);
		metaRest.setUsuarioLMSService(usuarioLMSService);
		metaRest.setParametroGeralService(parametroGeralService);
		
		doReturn(MODAL_RODOVIARIO).when(domainValueService).findDomainValueByValue(DM_MODAL, VL_MODAL_RODOVIARIO);
		doReturn(MODAL_AEREO).when(domainValueService).findDomainValueByValue(DM_MODAL, VL_MODAL_AEREO);
		doReturn(null).when(metaService).findByNaturalKey(anyLong(), (DomainValue) any(), anyInt(), anyString());//para que todo registro seja considerado um novo
		
		UsuarioLMS usuarioIntegracao = new UsuarioLMS();
		doReturn(usuarioIntegracao).when(metaRest, "findUsuarioIntegracao");
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComDadosValidosParaUmaMeta_thenSucesso() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		doReturn(1l).when(metaService, "callInheritedStore", convert(metaDTO1));
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		Assert.assertEquals(metaTerritorioDTO.getStatusRetorno(), MetaRest.RETORNO_OK);
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComNomeTerritorioNull_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(null, VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_TERRITORIO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComNomeTerritorioVazio_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO("", VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_TERRITORIO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComNomeTerritorioInvalido_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO("INVALIDO", VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(null).when(territorioService).findAtivoPorNome("INVALIDO");
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_TERRITORIO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComNomeTerritorioEmBranco_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(" ", VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_TERRITORIO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComModalNull_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, null, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MODAL_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComModalVazio_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, "", 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MODAL_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComModalEmBranco_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, " ", 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MODAL_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComModalInvalido_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, "INVALIDO", 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MODAL_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComAnoNull_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, null, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_ANO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComAnoNegativo_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, -1, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_ANO_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesNull_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO(null, new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesVazio_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesEmBranco_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO(" ", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesNumerico_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("01", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesDeNomeInvalido_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("INVALIDO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesDeMarcoSemCedilhaInvalido_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("MARCO", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_MES_INVALIDO;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComMesValidoEmLowerCaseParaUmaMeta_thenSucesso() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("janeiro", new BigDecimal("90000.00"));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_RODOVIARIO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		doReturn(1l).when(metaService, "callInheritedStore", convert(metaDTO1));
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		Assert.assertEquals(metaTerritorioDTO.getStatusRetorno(), MetaRest.RETORNO_OK);
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioSemValorMetaNull_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", null);
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_META_INVALIDA;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComValorMetaNegativo_thenExcecao() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", BigDecimal.valueOf(-1l));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		String erroEsperado = EXCEPTION_STORE_META_INVALIDA;
		Assert.assertTrue(metaTerritorioDTO.getStatusRetorno().contains(erroEsperado), getMensagemRetornoEsperado(erroEsperado, metaTerritorioDTO));
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComValorMetaZero_thenSucesso() throws Exception {
		
		MetaDTO metaDTO1 = new MetaDTO("JANEIRO", BigDecimal.valueOf(0l));
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_AEREO, 2015, Arrays.asList(metaDTO1));
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		doReturn(1l).when(metaService, "callInheritedStore", convert(metaDTO1));
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		Assert.assertEquals(metaTerritorioDTO.getStatusRetorno(), MetaRest.RETORNO_OK);
	}
	
	@Test
	public void givenNenhumaMeta_thenImportarMetaTerritorioComDadosValidosParaMetas12Meses_thenSucesso() throws Exception {
		List<MetaDTO> metasDTO = Arrays.asList(
				  new MetaDTO("JANEIRO", new BigDecimal("90000.00"))
				, new MetaDTO("FEVEREIRO", new BigDecimal("100000.00")) 
				, new MetaDTO("MARÇO", new BigDecimal("50000.00"))  
				, new MetaDTO("ABRIL", new BigDecimal("199000.00"))     
				, new MetaDTO("MAIO", new BigDecimal("147000.00"))      
				, new MetaDTO("JUNHO", new BigDecimal("90000.00"))      
				, new MetaDTO("JULHO", new BigDecimal("234000.00"))     
				, new MetaDTO("AGOSTO", new BigDecimal("300000.00"))    
				, new MetaDTO("SETEMBRO", new BigDecimal("292000.00"))  
				, new MetaDTO("OUTUBRO", new BigDecimal("348000.00"))   
				, new MetaDTO("NOVEMBRO", new BigDecimal("44000.00"))   
				, new MetaDTO("DEZEMBRO", new BigDecimal("321000.00"))  
		);
		MetaTerritorioDTO metaTerritorioDTO = getMetaTerritorioDTO(NM_TERRITORIO, VL_MODAL_RODOVIARIO, 2015, metasDTO);
		
		doReturn(TERRITORIO).when(territorioService).findAtivoPorNome(NM_TERRITORIO);
		for (int i = 0; i < metasDTO.size(); i++) {
			doReturn(i + 1).when(metaService, "callInheritedStore", convert(metasDTO.get(i)));
		}
		
		metaTerritorioDTO = (MetaTerritorioDTO) metaRest.storeMetaTerritorio(metaTerritorioDTO).getEntity();
		
		Assert.assertEquals(metaTerritorioDTO.getStatusRetorno(), MetaRest.RETORNO_OK);
	}
	
	private Meta convert(MetaDTO metaDTO) {
		Meta meta = new Meta();
		meta.setNmMes(metaDTO.getNmMes());
		meta.setVlMeta(metaDTO.getVlMeta());
		return meta;
	}
	
	private static Territorio getTerritorio(String nmTerritorio) {
		Territorio territorio = new Territorio();
		territorio.setNmTerritorio(nmTerritorio);
		return territorio;
	}
	
	private static DomainValue getModal(String vlModal) {
		DomainValue tpModal = new DomainValue();
		tpModal.setValue(vlModal);;
		return tpModal;
	}
	
	private MetaTerritorioDTO getMetaTerritorioDTO(String territorio, String modal, Integer ano, List<MetaDTO> metasDTO) {
		MetaTerritorioDTO metaTerritorioDTO = new MetaTerritorioDTO();
		metaTerritorioDTO.setTerritorio(territorio);
		metaTerritorioDTO.setAno(ano);
		metaTerritorioDTO.setMeta(metasDTO);
		metaTerritorioDTO.setModal(modal);
		
		return metaTerritorioDTO;
	}
	
	private String getMensagemRetornoEsperado(String erroEsperado, MetaTerritorioDTO metaTerritorioDTO) {
		return "Era esperado que a mensagem de erro contivesse [" + EXCEPTION_STORE_TERRITORIO_INVALIDO + "], mas foi encontrado [" + metaTerritorioDTO.getStatusRetorno() + "].";
	}
}
