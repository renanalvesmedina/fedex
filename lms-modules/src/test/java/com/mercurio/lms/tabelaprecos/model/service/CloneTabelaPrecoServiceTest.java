package com.mercurio.lms.tabelaprecos.model.service;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;

public class CloneTabelaPrecoServiceTest {
	
	@Test
	public void executeClonarGeneralidade() throws Exception {

		Generalidade generalidade = getExemploGeneralidade();

		CloneTabelaPrecoService service = getService();
		service.setTabelaPrecoParcelaService(getReajusteParametroClienteServiceMockerComUmGeneralidadeEspecifico(generalidade));
		
		List<TabelaPrecoParcela> tabelaPrecoParcelas = service.clonarTabelaPreco(1L,2L);

		Assert.assertTrue(tabelaPrecoParcelas.size() == 1);
		TabelaPrecoParcela parcelaGeneralidade = tabelaPrecoParcelas.get(0);
		Generalidade generalidadeResult = parcelaGeneralidade.getGeneralidade();
		Assert.assertTrue(generalidadeResult.getVlGeneralidade().equals(new BigDecimal("10")));
		Assert.assertTrue(generalidadeResult.getVlMinimo().equals(new BigDecimal("8")));
		Assert.assertTrue(generalidadeResult.getIdGeneralidade() == null);
		Assert.assertTrue(generalidadeResult.getTabelaPrecoParcela() != null);
		Assert.assertTrue(generalidade != generalidadeResult);
	}


	private Generalidade getExemploGeneralidade() {
		Generalidade generalidade = new Generalidade();
		generalidade.setIdGeneralidade(1L);
		generalidade.setVlGeneralidade(new BigDecimal("10"));
		generalidade.setVlMinimo(new BigDecimal("8"));
		return generalidade;
	}


	private CloneTabelaPrecoService getService() {
		CloneTabelaPrecoService service = new CloneTabelaPrecoService();
		service.setTabelaPrecoService(new TabelaPrecoServiceMocker().mock());
		return service;
	}


	@Test
	public void executeClonarTabelaPreco() throws Exception {
		CloneTabelaPrecoService service = getService();
		service.setTabelaPrecoParcelaService(getReajusteParametroClienteServiceMocker());

		List<TabelaPrecoParcela> tabelaPrecoParcelas = service.clonarTabelaPreco(1L,2L);
		Assert.assertTrue(tabelaPrecoParcelas.size() == 0);
	}

	@Test
	public void executeClonarTabelaPrecoComUmElemento() throws Exception {
		CloneTabelaPrecoService service = getService();
		service.setTabelaPrecoParcelaService(getReajusteParametroClienteServiceMockerComUmGeneralidade());

		List<TabelaPrecoParcela> tabelaPrecoParcelas = service.clonarTabelaPreco(1L,2L);
		Assert.assertTrue(tabelaPrecoParcelas.size() == 1);
	}

	@Test
	public void executeClonarTabelaPrecoComUmElementoNull() throws Exception {
		CloneTabelaPrecoService service = getService();
		service.setTabelaPrecoParcelaService(getReajusteParametroClienteServiceMockerComUmElementoNull());

		List<TabelaPrecoParcela> tabelaPrecoParcelas = service.clonarTabelaPreco(1L,2L);
		Assert.assertTrue(tabelaPrecoParcelas.size() == 0);
	}

	@Test
	public void executeClonarTabelaPrecoComUmGeneralidade() throws Exception {
		CloneTabelaPrecoService service = getService();
		service.setTabelaPrecoParcelaService(getReajusteParametroClienteServiceMockerComUmGeneralidade());

		List<TabelaPrecoParcela> tabelaPrecoParcelas = service.clonarTabelaPreco(1L,2L);
		Assert.assertTrue(tabelaPrecoParcelas.size() == 1);
	}


	private TabelaPrecoParcelaService getReajusteParametroClienteServiceMocker() {
		return new TabelaPrecoParcelaServiceMocker().findParcelasPrecoByIdTabelaPreco(1L).mock();
	}

	private TabelaPrecoParcelaService getReajusteParametroClienteServiceMockerComUmElementoNull() {
		return new TabelaPrecoParcelaServiceMocker().findParcelasPrecoByIdTabelaPrecoComUmElementoNull(1L).mock();
	}

	private TabelaPrecoParcelaService getReajusteParametroClienteServiceMockerComUmGeneralidade() {
		return new TabelaPrecoParcelaServiceMocker().findParcelasPrecoByIdTabelaPrecoComUmGeneralidade(1L).mock();
	}

	private TabelaPrecoParcelaService getReajusteParametroClienteServiceMockerComUmGeneralidadeEspecifico(Generalidade generalidade) {
		return new TabelaPrecoParcelaServiceMocker().findParcelasPrecoByIdTabelaPrecoComUmGeneralidadeEspecifico(1L, generalidade).mock();
	}
	

	static class TabelaPrecoServiceMocker {

		@Mock private TabelaPrecoService service;

		public TabelaPrecoServiceMocker() {
			initMocks(this);
		}

		public TabelaPrecoService mock(){
			return service;
		}
	}

	static class TabelaPrecoParcelaServiceMocker {

		@Mock private TabelaPrecoParcelaService service;

		public TabelaPrecoParcelaServiceMocker() {
			initMocks(this);
		}

		public TabelaPrecoParcelaService mock(){
			return service;
		}

		public TabelaPrecoParcelaServiceMocker findParcelasPrecoByIdTabelaPreco(Long id){

			List<TabelaPrecoParcela> tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();

			when(service.findParcelasPrecoByIdTabelaPreco(id,"G")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"T")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"S")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"P")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"M")).thenReturn(tabelaPrecoParcelas);
			return this;
		}

		public TabelaPrecoParcelaServiceMocker findParcelasPrecoByIdTabelaPrecoComUmElemento(Long id){

			List<TabelaPrecoParcela> tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();

			List<TabelaPrecoParcela> tabelaPrecoParcelasUm = new ArrayList<TabelaPrecoParcela>();
			TabelaPrecoParcela valorTaxa = new TabelaPrecoParcela();
			valorTaxa.setValorTaxa(new ValorTaxa());
			tabelaPrecoParcelasUm.add(valorTaxa);

			when(service.findParcelasPrecoByIdTabelaPreco(id,"G")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"T")).thenReturn(tabelaPrecoParcelasUm);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"S")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"P")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"M")).thenReturn(tabelaPrecoParcelas);
			return this;
		}

		public TabelaPrecoParcelaServiceMocker findParcelasPrecoByIdTabelaPrecoComUmElementoNull(Long id){

			List<TabelaPrecoParcela> tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();

			List<TabelaPrecoParcela> tabelaPrecoParcelasUmNull = null;

			when(service.findParcelasPrecoByIdTabelaPreco(id,"G")).thenReturn(tabelaPrecoParcelasUmNull);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"T")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"S")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"P")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"M")).thenReturn(tabelaPrecoParcelas);
			return this;
		}

		public TabelaPrecoParcelaServiceMocker findParcelasPrecoByIdTabelaPrecoComUmGeneralidade(Long id){

			List<TabelaPrecoParcela> tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();

			List<TabelaPrecoParcela> tabelaPrecoParcelasUm = new ArrayList<TabelaPrecoParcela>();
			TabelaPrecoParcela parcelaGeneralidade = new TabelaPrecoParcela();
			
			Generalidade generalidade = new Generalidade();
			generalidade.setIdGeneralidade(1L);
			generalidade.setVlGeneralidade(new BigDecimal("10"));
			generalidade.setVlMinimo(new BigDecimal("8"));
			parcelaGeneralidade.setGeneralidade(generalidade);
			tabelaPrecoParcelasUm.add(parcelaGeneralidade);

			when(service.findParcelasPrecoByIdTabelaPreco(id,"G")).thenReturn(tabelaPrecoParcelasUm);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"T")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"S")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"P")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"M")).thenReturn(tabelaPrecoParcelas);
			return this;
		}
		
		public TabelaPrecoParcelaServiceMocker findParcelasPrecoByIdTabelaPrecoComUmGeneralidadeEspecifico(Long id, Generalidade generalidade){

			List<TabelaPrecoParcela> tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();

			List<TabelaPrecoParcela> tabelaPrecoParcelasUm = new ArrayList<TabelaPrecoParcela>();
			TabelaPrecoParcela parcelaGeneralidade = new TabelaPrecoParcela();
			
			parcelaGeneralidade.setGeneralidade(generalidade);
			tabelaPrecoParcelasUm.add(parcelaGeneralidade);

			when(service.findParcelasPrecoByIdTabelaPreco(id,"G")).thenReturn(tabelaPrecoParcelasUm);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"T")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"S")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"P")).thenReturn(tabelaPrecoParcelas);
			when(service.findParcelasPrecoByIdTabelaPreco(id,"M")).thenReturn(tabelaPrecoParcelas);
			return this;
		}
	}



	private List<TabelaPrecoParcela> simulateTabelaPrecoParcelaTaxa() {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		
		TabelaPrecoParcela parcela = new TabelaPrecoParcela();
		
		ValorTaxa taxa = new ValorTaxa();
		taxa.setIdValorTaxa(1L);
		taxa.setVlTaxa(new BigDecimal("10"));
		parcela.setValorTaxa(taxa);
		
		ParcelaPreco preco = new ParcelaPreco();
		preco.setIdParcelaPreco(1L);
		parcela.setParcelaPreco(preco);
		
		parcelas.add(parcela);
		
		return parcelas;
	}

	private List<TabelaPrecoParcela> simulateTabelaPrecoParcela() {
		List<TabelaPrecoParcela> parcelas = new ArrayList<TabelaPrecoParcela>();
		
		TabelaPrecoParcela parcela = new TabelaPrecoParcela();
		Generalidade generalidade = getExemploGeneralidade();
		generalidade.setTabelaPrecoParcela(parcela);
		parcela.setGeneralidade(generalidade);

		ParcelaPreco preco = new ParcelaPreco();
		preco.setIdParcelaPreco(1L);
		parcela.setParcelaPreco(preco);
		
		parcelas.add(parcela);
		
		return parcelas;
	}
}
