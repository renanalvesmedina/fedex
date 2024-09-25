package com.mercurio.lms.tabelaprecos.model.service;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;

public class ReajusteParametroClienteDispatcherServiceTest {

	@Test
	public void reajustarDivisoesClientesAutomaticos() {
		ReajusteParametroClienteService reajusteParametroClienteServiceMocker = getReajusteParametroClienteServiceMocker();

		ReajusteParametroClienteDispatcherService service = new ReajusteParametroClienteDispatcherService();
		service.setReajusteParametroClienteService(reajusteParametroClienteServiceMocker);

		List<Long> ids = Arrays.asList(new Long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L});	  
		service.reajustarDivisoesClientesAutomaticos(ids);

	}
	@Test
	public void reajustarDivisoesClientesAutomaticosDuplicado() {
		ReajusteParametroClienteService reajusteParametroClienteServiceMocker = getReajusteParametroClienteServiceMocker();

		ReajusteParametroClienteDispatcherService service = new ReajusteParametroClienteDispatcherService();
		service.setReajusteParametroClienteService(reajusteParametroClienteServiceMocker);

		List<Long> ids = Arrays.asList(new Long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L});	  
		service.reajustarDivisoesClientesAutomaticos(ids);

	}
	@Test
	public void reajustarDivisoesClientesAutomaticosDuasVezes() {
		ReajusteParametroClienteService reajusteParametroClienteServiceMocker = getReajusteParametroClienteServiceMocker();

		ReajusteParametroClienteDispatcherService service = new ReajusteParametroClienteDispatcherService();
		service.setReajusteParametroClienteService(reajusteParametroClienteServiceMocker);

		List<Long> ids = Arrays.asList(new Long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L});	  
		service.reajustarDivisoesClientesAutomaticos(ids);
		service.reajustarDivisoesClientesAutomaticos(ids);

	}

	private ReajusteParametroClienteService getReajusteParametroClienteServiceMocker() {
		return new ReajusteParametroClienteServiceMocker().executeReajustarClienteAutomatico(1L).mock();
	}

	static class ReajusteParametroClienteServiceMocker {

		@Mock private ReajusteParametroClienteService service;

		public ReajusteParametroClienteServiceMocker() {
			initMocks(this);
		}

		public ReajusteParametroClienteService mock(){
			return service;
		}

		public ReajusteParametroClienteServiceMocker executeReajustarClienteAutomatico(Long id){

			ReajusteClienteAutomaticoDTO reajuste = new ReajusteClienteAutomaticoDTO();
			reajuste.setIdTabelaDivisaoCliente(id);
			reajuste.setTipo("A");
			when(service.executeReajustarClienteAutomatico(reajuste, false)).thenReturn(true);
			return this;
		}
	}
}
