package com.mercurio.lms.pendencia.report;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.mocks.ClienteServiceMocker;
import com.mercurio.lms.mocks.DoctoServicoServiceMocker;
import com.mercurio.lms.mocks.ParametroGeralServiceMocker;
import com.mercurio.lms.mocks.ParcelaPrecoServiceMocker;
import com.mercurio.lms.mocks.ServicoAdicionalClienteServiceMocker;
import com.mercurio.lms.mocks.TabelaDivisaoClienteServiceMocker;
import com.mercurio.lms.mocks.TypedFlatMapMocker;
import com.mercurio.lms.vendas.model.Cliente;


public class EmitirCartaMercadoriasDisposicaoServiceTest {
	
	private EmitirCartaMercadoriasDisposicaoService service;
	
	@BeforeClass
	public void init(){
		service = new EmitirCartaMercadoriasDisposicaoService();
		service.setParametroGeralService(createParametroGeralServiceMocker());
		service.setParcelaPrecoService(new ParcelaPrecoServiceMocker().findByCdParcelaPreco("IDArmazenagem", 293847L).mock());
		service.setDoctoServicoService(new DoctoServicoServiceMocker().findDivisaoClienteById().mock());
		service.setTabelaDivisaoClienteService(new TabelaDivisaoClienteServiceMocker().findByDivisaoCliente().mock());
	}

	private TypedFlatMap createTypeFlatMap() {
		return new TypedFlatMapMocker()
							.findLongById("formBean.clienteRemetente.idCliente", 387464L)
							.findLongById("formBean.doctoServico.idDoctoServico", 8351L)
							.mock();
	}

	private ParametroGeralService createParametroGeralServiceMocker() {
		return new ParametroGeralServiceMocker()
			.findConteudoByNomeParametro("DEC_PRAZO", "15")
			.findConteudoByNomeParametro("CARENCIA_ARM", "1")
			.mock();
	}

	private void assertPutDays(boolean shouldShow, String decursoPrazo, String taxaPermanenciaCarga) {
		Map<String, Object> map = new HashMap<String, Object>();
		service.putDays(map,  createTypeFlatMap());
		Assert.assertEquals(map.get("haveCobrancaTaxaPermanenciaCarga"), shouldShow);
		Assert.assertEquals(map.get("diasRetornoDecursoPrazo"), decursoPrazo);
		Assert.assertEquals(map.get("diasCobrancaTaxaPermanenciaCarga"), taxaPermanenciaCarga);
	}
	
	@Test
	public void testClientePotencial(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("P")).mock());
		assertPutDays(true, "15", "1");
	}
	
	@Test
	public void testClienteEventual(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("E")).mock());
		assertPutDays(true, "15", "1");
	}
	
	@Test
	public void testClienteEspecial(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("F")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, "E", 0).mock());
		assertPutDays(true, "1", "15");
	}
	
	@Test
	public void testClienteEspecialWithoutServiceAdicional(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("S")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalClienteReturingNull().mock());
		assertPutDays(false, "15", "");
	}
	
	@Test
	public void testClienteEspecialWithValorZero(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("F")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, "V", 0).mock());
		assertPutDays(false, "0", "");
	}
	
	@Test
	public void testClienteEspecialWithValorNotZero(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("F")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, "V", 10).mock());
		assertPutDays(true, "1", "15");
	}
	
	@Test
	public void testClienteEspecialWithDescontoCem(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("F")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, "D", 100).mock());
		assertPutDays(false, "0", "");
	}
	
	@Test
	public void testClienteEspecialWithDesconto60(){
		service.setClienteService(new ClienteServiceMocker().findById(createCliente("F")).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, "D", 60).mock());
		assertPutDays(true, "1", "15");
	}
	
	private Cliente createCliente(String tpCliente) {
		Cliente cliente = new Cliente();
		cliente.setTpCliente(new DomainValue(tpCliente));

		return cliente;
	} 
	
}
