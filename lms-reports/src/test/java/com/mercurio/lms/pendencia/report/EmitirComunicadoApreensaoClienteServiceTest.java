package com.mercurio.lms.pendencia.report;

import java.math.BigDecimal;
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
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

public class EmitirComunicadoApreensaoClienteServiceTest {

	private static final String DESCONTO = "D";
	private static final String VALOR = "V";
	private static final String CLIENTE_EVENTUAL = "E";
	private static final String CLIENTE_POTENCIAL = "P";
	private static final String CLIENTE_ESPECIAL = "S";
	private static final String CARENCIA_DEP = "CARENCIA_DEP";
	private static final String PARAMETRO_GERAL_CARENCIA_DEP_VALOR = "25";
	private static final String NRO_DIAS_FIEL_DEPOSITARIO = "NRO_DIAS_FIEL_DEPOSITARIO";
	private EmitirComunicadoApreensaoClienteService service;
	
	@BeforeClass
	public void init(){
		service = new EmitirComunicadoApreensaoClienteService();
		service.setParametroGeralService(createParametroGeralServiceMocker());
		service.setParcelaPrecoService(new ParcelaPrecoServiceMocker().findByCdParcelaPreco("IdTaxaFielDep", 293847L).mock());
		service.setDoctoServicoService(new DoctoServicoServiceMocker().findDivisaoClienteById().mock());
		service.setTabelaDivisaoClienteService(new TabelaDivisaoClienteServiceMocker().findByDivisaoCliente().mock());
	}

	@Test
	public void testBuildFielDepositarioClientePotencial() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_POTENCIAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(5, 1, DESCONTO, 100).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertEquals(map.get(NRO_DIAS_FIEL_DEPOSITARIO), PARAMETRO_GERAL_CARENCIA_DEP_VALOR);
	}
	
	@Test
	public void testBuildFielDepositarioClienteEventual() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_EVENTUAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(15, 1, DESCONTO, 100).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertEquals(map.get(NRO_DIAS_FIEL_DEPOSITARIO), PARAMETRO_GERAL_CARENCIA_DEP_VALOR);
	}

	@Test
	public void testBuildFielDepositarioExisteCobranca() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_ESPECIAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(3, 1, DESCONTO, 50).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertEquals(map.get(NRO_DIAS_FIEL_DEPOSITARIO), "3");
	}
	
	@Test
	public void testBuildFielDepositarioExisteCobrancaSemQtdDias() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_ESPECIAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(null, 1, DESCONTO, 50).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertEquals(map.get(NRO_DIAS_FIEL_DEPOSITARIO), PARAMETRO_GERAL_CARENCIA_DEP_VALOR);
	}
	
	@Test
	public void testBuildFielDepositarioComValorZerado() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_ESPECIAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(null, 1, VALOR, 0).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertNull(map.get(NRO_DIAS_FIEL_DEPOSITARIO));
	}
	
	@Test
	public void testBuildFielDepositarioComDescontoIntegral() {
		service.setClienteService(new ClienteServiceMocker().findById(createCliente(CLIENTE_ESPECIAL)).mock());
		service.setServicoAdicionalClienteService(new ServicoAdicionalClienteServiceMocker().findServicoAdicionalCliente(null, 1, DESCONTO, 100).mock());
		Map map = service.buildFielDepositario(createTypeFlatMap());
		Assert.assertNull(map.get(NRO_DIAS_FIEL_DEPOSITARIO));
	}
	
	@Test
	public void testExisteCombranca() {
		Assert.assertTrue(service.existeCombranca(createServicoAdicionalCliente(DESCONTO, "50")));
		Assert.assertTrue(service.existeCombranca(createServicoAdicionalCliente(VALOR, "50")));
		Assert.assertFalse(service.existeCombranca(createServicoAdicionalCliente(DESCONTO, "100")));
		Assert.assertFalse(service.existeCombranca(createServicoAdicionalCliente(VALOR, "0")));
	}
	
	@Test
	public void testIsDescontoIntegral() {
		Assert.assertTrue(service.isDescontoIntegral(createServicoAdicionalCliente(DESCONTO, "100")));
		Assert.assertFalse(service.isDescontoIntegral(createServicoAdicionalCliente(DESCONTO, "20")));
	}

	@Test
	public void testIsValorSemCobranca() {
		Assert.assertTrue(service.isValorSemCobranca(createServicoAdicionalCliente(VALOR, "0")));
		Assert.assertFalse(service.isValorSemCobranca(createServicoAdicionalCliente(VALOR, "20")));
	}
	
	
	private ServicoAdicionalCliente createServicoAdicionalCliente(String tpIndicador, String vlValor){
		ServicoAdicionalCliente servicoAdicionalCliente = new ServicoAdicionalCliente();
		servicoAdicionalCliente.setTpIndicador(new DomainValue(tpIndicador));
		servicoAdicionalCliente.setVlValor(new BigDecimal(vlValor));
		return servicoAdicionalCliente;
	}
	
	
	private ParametroGeralService createParametroGeralServiceMocker() {
		return new ParametroGeralServiceMocker()
			.findByNomeParametro(CARENCIA_DEP, PARAMETRO_GERAL_CARENCIA_DEP_VALOR)
			.mock();
	}
	
	private TypedFlatMap createTypeFlatMap() {
		return new TypedFlatMapMocker()
							.findLongById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa", 387464L)
							.findLongById("ocorrenciaDoctoServico.doctoServico.idDoctoServico", 578995L)
							.mock();
	}
	
	private Cliente createCliente(String tpCliente) {
		Cliente cliente = new Cliente();
		cliente.setTpCliente(new DomainValue(tpCliente));

		return cliente;
	}
	
}
