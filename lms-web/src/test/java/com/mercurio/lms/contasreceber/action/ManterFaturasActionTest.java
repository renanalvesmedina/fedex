package com.mercurio.lms.contasreceber.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.testng.PowerMockTestCase;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.dto.ProcessaArquivoLinhaDTO;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ItemRelacaoPagtoParcial;
import com.mercurio.lms.contasreceber.model.service.ItemRelacaoPagtoParcialService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;


public class ManterFaturasActionTest extends PowerMockTestCase {

		@InjectMocks private ManterFaturasAction manterFaturasAction;
		@Mock private ItemRelacaoPagtoParcialService itemRelacaoPagtoParcialService;

	    @BeforeTest
	    protected void setup(){
	    	manterFaturasAction = new ManterFaturasAction();
	        MockitoAnnotations.initMocks(this);
	    }
	
		@Test
		public void hasRequiredFieldsValue_sgFaturaNull() throws Exception {
			String sgFilialFatura = null;
			Long nrFatura = null;
			String tpDocumento = null;
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			
			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}

		@Test
		public void hasRequiredFieldsValue_sgFaturaBlank() throws Exception {
			String sgFilialFatura = "";
			Long nrFatura = null;
			String tpDocumento = null;
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;

			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_nrFaturaNull() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = null;
			String tpDocumento = null;
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			
			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_tpDocumentoNull() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = null;
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			

			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_tpDocumentoBlank() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "";
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;

			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_sgFilialDoctoNull() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = null;
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			
			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_sgFilialDoctoBlank() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "";
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			
			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_nrDoctoNull() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Integer nrDocto = null;
			BigDecimal vlDevido = null;
			
			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void hasRequiredFieldsValue_vlDevidoNull() throws Exception {
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = null;

			Boolean hasRequired = Whitebox.invokeMethod(manterFaturasAction,"hasRequiredFieldsValue",new Object[]{sgFilialFatura, nrFatura, tpDocumento, sgFilialDocto, nrDocto,vlDevido});
			
			Assert.assertNotNull(hasRequired);
			Assert.assertFalse(hasRequired);
		}
		
		@Test
		public void validateLinha_FilialOrigemDiferenteFaturaMaster() throws Exception {
			Integer nrLinha = 1;
			
			String sgFilialFatura = "SAO";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = new BigDecimal(3333L);
			BigDecimal vlDesconto = BigDecimal.ZERO;

			Fatura master = new Fatura();
			master.setIdFatura(1L);
			master.setNrFatura(nrFatura);
			Filial filial = new Filial();
			filial.setSgFilial("POA");
			master.setFilialByIdFilial(filial);
			
			Map<String, ItemFatura> keyItems = new HashMap<String, ItemFatura>();
			
			ItemFatura itemFatura = null;
			DevedorDocServFat devedorDocServFat = null;
			DoctoServico docto = null;
			Filial origem = null;
			{
				itemFatura = new ItemFatura();
				devedorDocServFat = new DevedorDocServFat();
				devedorDocServFat.setVlDevido(vlDevido);
				
				docto = new DoctoServico();
				docto.setNrDoctoServico(nrDocto);
				docto.setTpDocumentoServico(new DomainValue("CTE"));
				
				origem = new Filial();
				origem.setSgFilial(sgFilialDocto);
				
				docto.setFilialByIdFilialOrigem(origem);
				
				devedorDocServFat.setDoctoServico(docto);
				itemFatura.setDevedorDocServFat(devedorDocServFat);
				
				keyItems.put(createKey(tpDocumento,sgFilialFatura,nrDocto), itemFatura);
			}
			
			List<ItemRelacaoPagtoParcial> pagtosParcial = new ArrayList<ItemRelacaoPagtoParcial>();
			Mockito.when(itemRelacaoPagtoParcialService.findByIdDevedorDocServFat(itemFatura.getDevedorDocServFat().getIdDevedorDocServFat())).thenReturn(pagtosParcial);
			
			ProcessaArquivoLinhaDTO parameters = new ProcessaArquivoLinhaDTO();
			parameters.setFaturaMaster(master);
			parameters.setKeyItems(keyItems);
			parameters.setSgFilialFatura(sgFilialFatura);
			parameters.setNrFatura(nrFatura);
			parameters.setTpDocumento(tpDocumento);
			parameters.setSgFilialDocto(sgFilialDocto);
			parameters.setNrDocto(nrDocto);
			parameters.setVlDevido(vlDevido);
			parameters.setVlDesconto(vlDesconto);
			parameters.setNrLinha(nrLinha);
			List<BusinessException> excepetions = Whitebox.invokeMethod(manterFaturasAction, "validateLinha", new Object[] { parameters });

			Assert.assertNotNull(excepetions);
			BusinessException businessException = excepetions.get(0);
			Assert.assertNotNull(businessException);
			Assert.assertEquals(businessException.getMessageKey(),"LMS-36285");
		}
		
		@Test
		public void validateLinha_DoctoServicoInexistente_LinhaArquivo() throws Exception {
			Integer nrLinha = 1;
			
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = new BigDecimal(3333L);
			BigDecimal vlDesconto = BigDecimal.ZERO;
			
			Fatura master = new Fatura();
			master.setIdFatura(1L);
			master.setNrFatura(nrFatura);
			Filial filial = new Filial();
			filial.setSgFilial(sgFilialFatura);
			master.setFilialByIdFilial(filial);

			Map<String, ItemFatura> keyItems = new HashMap<String, ItemFatura>();

			ProcessaArquivoLinhaDTO parameters = new ProcessaArquivoLinhaDTO();
			parameters.setFaturaMaster(master);
			parameters.setKeyItems(keyItems);
			parameters.setSgFilialFatura(sgFilialFatura);
			parameters.setNrFatura(nrFatura);
			parameters.setTpDocumento(tpDocumento);
			parameters.setSgFilialDocto(sgFilialDocto);
			parameters.setNrDocto(nrDocto);
			parameters.setVlDevido(vlDevido);
			parameters.setVlDesconto(vlDesconto);
			parameters.setNrLinha(nrLinha);
			List<BusinessException> excepetions = Whitebox.invokeMethod(manterFaturasAction, "validateLinha", new Object[] { parameters });

			Assert.assertNotNull(excepetions);
			BusinessException businessException = excepetions.get(0);
			Assert.assertNotNull(businessException);
			Assert.assertEquals(businessException.getMessageKey(),"LMS-36286");
		}
		
		@Test
		public void validateLinha_VlDevidoDiferenteFaturaMaster() throws Exception {
			Integer nrLinha = 1;
			
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = new BigDecimal(3333L);
			BigDecimal vlDesconto = null;

			Fatura master = new Fatura();
			master.setIdFatura(1L);
			master.setNrFatura(nrFatura);
			Filial filial = new Filial();
			filial.setSgFilial(sgFilialFatura);
			master.setFilialByIdFilial(filial);

			Map<String, ItemFatura> keyItems = new HashMap<String, ItemFatura>();
			
			ItemFatura itemFatura = null;
			DevedorDocServFat devedorDocServFat = null;
			DoctoServico docto = null;
			Filial origem = null;
			{
				itemFatura = new ItemFatura();
				devedorDocServFat = new DevedorDocServFat();
				devedorDocServFat.setVlDevido(BigDecimal.ONE);
				
				docto = new DoctoServico();
				docto.setNrDoctoServico(nrDocto);
				docto.setTpDocumentoServico(new DomainValue(tpDocumento));
				
				origem = new Filial();
				origem.setSgFilial(sgFilialDocto);
				
				docto.setFilialByIdFilialOrigem(origem);
				
				devedorDocServFat.setDoctoServico(docto);
				itemFatura.setDevedorDocServFat(devedorDocServFat);
				keyItems.put(createKey(tpDocumento,sgFilialFatura,nrDocto), itemFatura);
			}
			
			ProcessaArquivoLinhaDTO parameters = new ProcessaArquivoLinhaDTO();
			parameters.setFaturaMaster(master);
			parameters.setKeyItems(keyItems);
			parameters.setSgFilialFatura(sgFilialFatura);
			parameters.setNrFatura(nrFatura);
			parameters.setTpDocumento(tpDocumento);
			parameters.setSgFilialDocto(sgFilialDocto);
			parameters.setNrDocto(nrDocto);
			parameters.setVlDevido(vlDevido);
			parameters.setVlDesconto(vlDesconto);
			parameters.setNrLinha(nrLinha);
			List<BusinessException> excepetions = Whitebox.invokeMethod(manterFaturasAction, "validateLinha", new Object[] { parameters });

			Assert.assertNotNull(excepetions);
			BusinessException businessException = excepetions.get(0);
			Assert.assertNotNull(businessException);
			Assert.assertEquals(businessException.getMessageKey(),"LMS-36288");
		}
		
		
		@Test
		public void validateLinha_VlDescontoSuperiorDiferencaDoValorDocMenosRecebidoParcial() throws Exception {
			Integer nrLinha = 1;
			
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = new BigDecimal(200L);
			BigDecimal vlDesconto = new BigDecimal(1000L);

			Fatura master = new Fatura();
			master.setIdFatura(1L);
			master.setNrFatura(nrFatura);
			Filial filial = new Filial();
			filial.setSgFilial(sgFilialFatura);
			master.setFilialByIdFilial(filial);

			Map<String, ItemFatura> keyItems = new HashMap<String, ItemFatura>();
			
			ItemFatura itemFatura = null;
			DevedorDocServFat devedorDocServFat = null;
			DoctoServico docto = null;
			Filial origem = null;
			{
				itemFatura = new ItemFatura();
				devedorDocServFat = new DevedorDocServFat();
				devedorDocServFat.setVlDevido(vlDevido);
				
				docto = new DoctoServico();
				docto.setNrDoctoServico(nrDocto);
				docto.setTpDocumentoServico(new DomainValue(tpDocumento));
				
				origem = new Filial();
				origem.setSgFilial(sgFilialDocto);
				
				docto.setFilialByIdFilialOrigem(origem);
				
				devedorDocServFat.setDoctoServico(docto);
				itemFatura.setDevedorDocServFat(devedorDocServFat);
				
				keyItems.put(createKey(tpDocumento,sgFilialFatura,nrDocto), itemFatura);
			}

			ProcessaArquivoLinhaDTO parameters = new ProcessaArquivoLinhaDTO();
			parameters.setFaturaMaster(master);
			parameters.setKeyItems(keyItems);
			parameters.setSgFilialFatura(sgFilialFatura);
			parameters.setNrFatura(nrFatura);
			parameters.setTpDocumento(tpDocumento);
			parameters.setSgFilialDocto(sgFilialDocto);
			parameters.setNrDocto(nrDocto);
			parameters.setVlDevido(vlDevido);
			parameters.setVlDesconto(vlDesconto);
			parameters.setNrLinha(nrLinha);
			List<BusinessException> excepetions = Whitebox.invokeMethod(manterFaturasAction, "validateLinha", new Object[] { parameters });

			Assert.assertNotNull(excepetions);
			BusinessException businessException = excepetions.get(0);
			Assert.assertNotNull(businessException);
			Assert.assertEquals(businessException.getMessageKey(),"LMS-36289");
		}
		
		@Test
		public void validateLinha_VlDescontoSuperiorDiferencaDoValorDocMenosRecebidoParcial_ComRecebimentosParciais() throws Exception {
			Integer nrLinha = 1;
			
			String sgFilialFatura = "POA";
			Long nrFatura = 3333L;
			String tpDocumento = "CTE";
			String sgFilialDocto = "POA";
			Long nrDocto = 3333L;
			BigDecimal vlDevido = new BigDecimal(200L);
			BigDecimal vlDesconto = new BigDecimal(200L);
			
			Fatura master = new Fatura();
			master.setIdFatura(1L);
			master.setNrFatura(nrFatura);
			Filial filial = new Filial();
			filial.setSgFilial(sgFilialFatura);
			master.setFilialByIdFilial(filial);

			Map<String, ItemFatura> keyItems = new HashMap<String, ItemFatura>();
			
			ItemFatura itemFatura = null;
			DevedorDocServFat devedorDocServFat = null;
			DoctoServico docto = null;
			Filial origem = null;
			{
				itemFatura = new ItemFatura();
				devedorDocServFat = new DevedorDocServFat();
				devedorDocServFat.setVlDevido(vlDevido);
				
				docto = new DoctoServico();
				docto.setNrDoctoServico(nrDocto);
				docto.setTpDocumentoServico(new DomainValue(tpDocumento));
				
				origem = new Filial();
				origem.setSgFilial(sgFilialDocto);
				
				docto.setFilialByIdFilialOrigem(origem);
				
				devedorDocServFat.setDoctoServico(docto);
				itemFatura.setDevedorDocServFat(devedorDocServFat);
				
				keyItems.put(createKey(tpDocumento,sgFilialFatura,nrDocto), itemFatura);
			}
			

			List<ItemRelacaoPagtoParcial> pagtosParcial = new ArrayList<ItemRelacaoPagtoParcial>();
			{
				ItemRelacaoPagtoParcial relacaoPagtoParcial = new ItemRelacaoPagtoParcial();
				relacaoPagtoParcial.setDevedorDocServFat(devedorDocServFat);
				relacaoPagtoParcial.setVlPagamento(BigDecimal.TEN);
				pagtosParcial.add(relacaoPagtoParcial);
			}

			Mockito.when(itemRelacaoPagtoParcialService.findByIdDevedorDocServFat(itemFatura.getDevedorDocServFat().getIdDevedorDocServFat())).thenReturn(pagtosParcial);
			
			ProcessaArquivoLinhaDTO parameters = new ProcessaArquivoLinhaDTO();
			parameters.setFaturaMaster(master);
			parameters.setKeyItems(keyItems);
			parameters.setSgFilialFatura(sgFilialFatura);
			parameters.setNrFatura(nrFatura);
			parameters.setTpDocumento(tpDocumento);
			parameters.setSgFilialDocto(sgFilialDocto);
			parameters.setNrDocto(nrDocto);
			parameters.setVlDevido(vlDevido);
			parameters.setVlDesconto(vlDesconto);
			parameters.setNrLinha(nrLinha);
			List<BusinessException> excepetions = Whitebox.invokeMethod(manterFaturasAction, "validateLinha", new Object[] { parameters });

			Assert.assertNotNull(excepetions);
			BusinessException businessException = excepetions.get(0);
			Assert.assertNotNull(businessException);
			Assert.assertEquals(businessException.getMessageKey(),"LMS-36289");
		}
		

		private String createKey(String tpDocumento, String sgFilialDocto, Long nrDocto) {
			StringBuilder keyLinhaArquivo = new StringBuilder();
			keyLinhaArquivo.append(tpDocumento).append(sgFilialDocto).append(nrDocto);
			return keyLinhaArquivo.toString();
		}



		
}
