package com.mercurio.lms.expedicao.model.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.MunicipioTrtCliente;
import com.mercurio.lms.vendas.model.service.MunicipioTrtClienteService;

public class CalculoParcelaFreteServiceFindParcelaTRTTest {

	private static final long ID_MUNICIPIO_TRT = 0L;



	@Test
	public void findParcelaTRT() {

		CalculoFrete calculoFrete = configuraCalculoFrete(null, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", null, new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}


	@Test
	public void findParcelaTRTComMunicipioTrtClienteQueCobra() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtClienteList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtCliente = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtCliente.setMunicipio(municipio);
		trtCliente.setBlCobraTrt(true);
		trtClienteList.add(trtCliente);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, true, true, false, "", null, trtClienteList,new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult != null);
	}

	@Test
	public void findParcelaTRTComMunicipioTrtClienteQueNaoCobra() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtClienteList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtCliente = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtCliente.setMunicipio(municipio);
		trtCliente.setBlCobraTrt(false);
		trtClienteList.add(trtCliente);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, true, false, false, "", null, trtClienteList, new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}

	@Test
	public void findParcelaTRTComMunicipioTrtClienteQueNaoCobraEMunicipioTemRestricaoShouldNaoCobrar() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtClienteList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtCliente = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtCliente.setMunicipio(municipio);
		trtCliente.setBlCobraTrt(false);
		trtClienteList.add(trtCliente);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, true, false, false, "", null, trtClienteList, new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}

	@Test
	public void findParcelaTRTComMunicipioTrtClienteQueNaoFoiEncontrado() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", null, new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}

	@Test
	public void findParcelaTRTComMunicipioTrtClientePorTabelaQueNaoFoiEncontrado() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", null, new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}
	
	@Test
	public void findParcelaTRTComMunicipioTrtClientePorTabelaQueCobra() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtTabelaList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtTabela = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtTabela.setMunicipio(municipio);
		trtTabela.setBlCobraTrt(true);
		trtTabelaList.add(trtTabela);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, true, "S", null, new ArrayList<MunicipioTrtCliente>(), trtTabelaList, false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult != null);
	}
	
	@Test
	public void findParcelaTRTComMunicipioTrtClientePorTabelaQueNaoCobra() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtTabelaList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtTabela = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtTabela.setMunicipio(municipio);
		trtTabela.setBlCobraTrt(false);
		trtTabelaList.add(trtTabela);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, true, "N", null, new ArrayList<MunicipioTrtCliente>(), trtTabelaList, false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}
	
	@Test
	public void findParcelaTRTComMunicipioTrtClientePorTabelaQueNaoCobraEMunicipioTemRestricaoShouldNaoCobrar() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ArrayList<MunicipioTrtCliente> trtTabelaList = new ArrayList<MunicipioTrtCliente>();
		MunicipioTrtCliente trtTabela = new MunicipioTrtCliente();
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
		trtTabela.setMunicipio(municipio);
		trtTabela.setBlCobraTrt(false);
		trtTabelaList.add(trtTabela);
		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, true, "N", null, new ArrayList<MunicipioTrtCliente>(), trtTabelaList, false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}
	

	@Test
	public void findParcelaTRTComMunicipioTrtClienteCobraPorTabelaPadrao() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", "S", new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult != null);
	}
	@Test
	public void findParcelaTRTComMunicipioTrtClienteNaoCobraPorTabelaPadrao() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", "N", new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}
	@Test
	public void findParcelaTRTComMunicipioTrtClienteNaoEncontraPorTabelaPadrao() {

		CalculoFrete calculoFrete = configuraCalculoFrete(1L, 1L);

		ParcelaServico findParcelaTRTResult = getService(calculoFrete, false, false, false, "", null, new ArrayList<MunicipioTrtCliente>(), new ArrayList<MunicipioTrtCliente>(), false).findParcelaTRT(calculoFrete);

		Assert.assertTrue(findParcelaTRTResult == null);
	}
	
	private CalculoFrete configuraCalculoFrete(Long idClienteDevedor, Long idServico) {
		CalculoFrete calculoFrete = new CalculoFrete();
		Cliente cliente = new Cliente();
		cliente.setIdCliente(idClienteDevedor);
		calculoFrete.setClienteBase(cliente);
		MunicipioFilial municipioFilial = new MunicipioFilial();
		municipioFilial.setIdMunicipioFilial(ID_MUNICIPIO_TRT);

		calculoFrete.setMunicipioFilialDestino(municipioFilial);

		TabelaPreco tabelaPreco = new TabelaPreco();
		calculoFrete.setTabelaPreco(tabelaPreco);
		
		calculoFrete.setIdServico(idServico);
		
		return calculoFrete;
	}

	private CalculoParcelaFreteService getService(CalculoFrete calculoFrete, Boolean encontraMunicipio, Boolean cobraTRT, Boolean encontraMunicipioTabela, String cobraMunicipioTabela, String cobraMunicipioTabelaPadrao, List<MunicipioTrtCliente> trtClienteList, List<MunicipioTrtCliente> trtTabelaList, Boolean cobraPorTabelaSemMunicipio ) {
		CalculoParcelaFreteService service = new CalculoParcelaFreteServiceMocker(calculoFrete).mock();
		service.setMunicipioFilialService(getMunicipioFilialServiceMocker());
		service.setMunicipioTrtClienteService(getMunicipioTrtClienteServiceMocker(encontraMunicipio, cobraTRT, encontraMunicipioTabela, cobraMunicipioTabela, cobraMunicipioTabelaPadrao, trtClienteList, trtTabelaList, cobraPorTabelaSemMunicipio));
		
		return service;
	}

	private MunicipioTrtClienteService getMunicipioTrtClienteServiceMocker(Boolean encontraMunicipio, Boolean cobraTRT, Boolean encontraMunicipioTabela, String cobraMunicipioTabela, String cobraMunicipioTabelaPadrao, List<MunicipioTrtCliente> trtClienteList, List<MunicipioTrtCliente> trtTabelaList, Boolean cobraPorTabelaSemMunicipio) {
		return new MunicipioTrtClienteServiceMocker().findParcelasPrecoByIdTabelaPreco(1L, ID_MUNICIPIO_TRT, encontraMunicipio, cobraTRT, encontraMunicipioTabela, cobraMunicipioTabela, cobraMunicipioTabelaPadrao, trtClienteList, trtTabelaList, cobraPorTabelaSemMunicipio).mock();
	}

	private MunicipioFilialService getMunicipioFilialServiceMocker() {
		return new MunicipioFilialServiceMocker().findById().mock();
	}


	static class MunicipioFilialServiceMocker {

		@Mock private MunicipioFilialService service;

		public MunicipioFilialServiceMocker() {
			initMocks(this);
		}

		public MunicipioFilialService mock(){
			return service;
		}

		public MunicipioFilialServiceMocker findById(){

			MunicipioFilial municipioFilial = new MunicipioFilial();
			municipioFilial.setIdMunicipioFilial(ID_MUNICIPIO_TRT);
			Municipio municipio = new Municipio();
			municipio.setIdMunicipio(ID_MUNICIPIO_TRT);
			municipioFilial.setMunicipio(municipio);

			when(service.findById(ID_MUNICIPIO_TRT)).thenReturn(municipioFilial);
			return this;
		}
	}

	
	static class MunicipioTrtClienteServiceMocker {

		@Mock private MunicipioTrtClienteService service;

		public MunicipioTrtClienteServiceMocker() {
			initMocks(this);
		}

		public MunicipioTrtClienteService mock(){
			return service;
		}

		public MunicipioTrtClienteServiceMocker findParcelasPrecoByIdTabelaPreco(Long idClienteDevedor, Long idMunicipioEntrega, Boolean encontraMunicipio, Boolean cobraTRT, Boolean encontraMunicipioTabela, String cobraMunicipioTabela, String cobraMunicipioTabelaPadrao, List<MunicipioTrtCliente> trtClienteList, List<MunicipioTrtCliente> trtTabelaList, Boolean cobraPorTabelaSemMunicipio){

			MunicipioTrtCliente municipioTRT=null;
			if(encontraMunicipio){
				municipioTRT = new MunicipioTrtCliente();
				municipioTRT.setBlCobraTrt(cobraTRT);
			}
			
			when(service.findTRTVigenteParaCliente(1L)).thenReturn(trtClienteList);

			when(service.cobraPorTabela(null)).thenReturn(trtTabelaList );

			if(!encontraMunicipioTabela){
				cobraMunicipioTabela = null;
			}
			when(service.cobraPorTabelaMunicipio(idMunicipioEntrega, 2L)).thenReturn(cobraMunicipioTabelaPadrao );
			
			when(service.findTabelaPadraoModal(1L)).thenReturn(2L);

			return this;
		}
	}
	

	static class CalculoParcelaFreteServiceMocker {

		@Spy private CalculoParcelaFreteService service;

		public CalculoParcelaFreteServiceMocker(CalculoFrete calculoFrete) {
			initMocks(this);
			
			ParcelaServico parcela = new ParcelaServico(new ParcelaPreco());
			doReturn(parcela).when(service).executeCalculoParcelaTRT(calculoFrete);

			
		}

		public CalculoParcelaFreteService mock(){
			return service;
		}
	}
}
