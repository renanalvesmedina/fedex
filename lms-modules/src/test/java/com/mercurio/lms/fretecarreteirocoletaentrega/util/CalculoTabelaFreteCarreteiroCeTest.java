package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;







import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.vendas.model.Cliente;

public class CalculoTabelaFreteCarreteiroCeTest {

	@BeforeTest
	public void setUp() throws Exception {
		
	}
	
	private Long[] getDocumento() {
		Long[] documento = { 1L, 1L, 1L, 1L, 1L, 1L};
		return documento;
	}

	
	/**
	 * Simula retorno tabelas 
	 * @return
	 */
	public List<TabelaFcValores> getTabelas(){
		List<TabelaFcValores> tabelas = new ArrayList<TabelaFcValores>();
		
		TabelaFcValores tabela1 = new TabelaFcValores();
		tabela1.setIdTabelaFcValores(1L);
		tabela1.setBlTipo(new DomainValue("CL"));
		
		TabelaFcValores tabela2 = new TabelaFcValores();
		tabela2.setIdTabelaFcValores(2L);
		tabela2.setBlTipo(new DomainValue("CD"));
		
		TabelaFcValores tabela3 = new TabelaFcValores();
		tabela3.setIdTabelaFcValores(3L);
		
		TabelaFcValores tabela4 = new TabelaFcValores();
		tabela4.setIdTabelaFcValores(4L);
		
		TabelaFcValores tabela5 = new TabelaFcValores();
		tabela5.setIdTabelaFcValores(5L);
		
		TabelaFcValores tabela6 = new TabelaFcValores();
		tabela6.setIdTabelaFcValores(6L);
		
		TabelaFcValores tabela7 = new TabelaFcValores();
		tabela7.setIdTabelaFcValores(7L);
		
		TabelaFcValores tabela8 = new TabelaFcValores();
		tabela8.setIdTabelaFcValores(8L);
		
		TabelaFcValores tabela9 = new TabelaFcValores();
		tabela9.setIdTabelaFcValores(9L);
		
		TabelaFcValores tabela10 = new TabelaFcValores();
		tabela10.setIdTabelaFcValores(10L);
		
		TabelaFcValores tabela11 = new TabelaFcValores();
		tabela11.setIdTabelaFcValores(11L);
		
		TabelaFcValores tabela12 = new TabelaFcValores();
		tabela12.setIdTabelaFcValores(12L);
		
		TabelaFcValores tabela13 = new TabelaFcValores();
		tabela13.setIdTabelaFcValores(13L);
		
		TabelaFcValores tabela14 = new TabelaFcValores();
		tabela14.setIdTabelaFcValores(14L);
		
		TabelaFcValores tabela15 = new TabelaFcValores();
		tabela15.setIdTabelaFcValores(15L);
		
		TabelaFcValores tabela16 = new TabelaFcValores();
		tabela16.setIdTabelaFcValores(16L);
		
		TabelaFcValores tabela17 = new TabelaFcValores();
		tabela17.setIdTabelaFcValores(17L);
		
		TabelaFcValores tabela18 = new TabelaFcValores();
		tabela18.setIdTabelaFcValores(18L);
		
		TabelaFcValores tabela19 = new TabelaFcValores();
		tabela19.setIdTabelaFcValores(19L);
		
		TabelaFcValores tabela20 = new TabelaFcValores();
		tabela20.setIdTabelaFcValores(20L);
		
		TabelaFcValores tabela21 = new TabelaFcValores();
		tabela21.setIdTabelaFcValores(21L);
		
		TabelaFcValores tabela22 = new TabelaFcValores();
		tabela22.setIdTabelaFcValores(22L);
		
		TabelaFcValores tabela23 = new TabelaFcValores();
		tabela23.setIdTabelaFcValores(23L);
		
		TabelaFcValores tabela24 = new TabelaFcValores();
		tabela24.setIdTabelaFcValores(24L);
		
		TabelaFcValores tabela25 = new TabelaFcValores();
		tabela25.setIdTabelaFcValores(25L);
		
		TabelaFcValores tabela26 = new TabelaFcValores();
		tabela26.setIdTabelaFcValores(26L);
		
		TabelaFcValores tabela27 = new TabelaFcValores();
		tabela27.setIdTabelaFcValores(27L);
		
		TabelaFcValores tabela28 = new TabelaFcValores();
		tabela28.setIdTabelaFcValores(28L);
		
		TabelaFcValores tabela29 = new TabelaFcValores();
		tabela29.setIdTabelaFcValores(29L);
		
		TabelaFcValores tabela30 = new TabelaFcValores();
		tabela30.setIdTabelaFcValores(30L);
		
		TabelaFcValores tabela31 = new TabelaFcValores();
		tabela31.setIdTabelaFcValores(31L);
		
		TabelaFcValores tabela32 = new TabelaFcValores();
		tabela32.setIdTabelaFcValores(32L);
		
		TabelaFcValores tabela221 = new TabelaFcValores();
		tabela221.setIdTabelaFcValores(221L);
		
		TabelaFcValores tabela222 = new TabelaFcValores();
		tabela222.setIdTabelaFcValores(222L);
		
		TabelaFcValores tabela223 = new TabelaFcValores();
		tabela223.setIdTabelaFcValores(223L);
		
		TabelaFcValores tabela224 = new TabelaFcValores();
		tabela224.setIdTabelaFcValores(224L);
		
		TabelaFcValores tabela225 = new TabelaFcValores();
		tabela225.setIdTabelaFcValores(225L);
		
		TabelaFcValores tabela226 = new TabelaFcValores();
		tabela226.setIdTabelaFcValores(226L);
		
		TabelaFcValores tabela227 = new TabelaFcValores();
		tabela227.setIdTabelaFcValores(227L);
		
		TabelaFcValores tabela228 = new TabelaFcValores();
		tabela228.setIdTabelaFcValores(228L);
		
		TabelaFcValores tabela229 = new TabelaFcValores();
		tabela229.setIdTabelaFcValores(229L);
		
		TabelaFcValores tabela210 = new TabelaFcValores();
		tabela210.setIdTabelaFcValores(210L);
		
		TabelaFcValores tabela211 = new TabelaFcValores();
		tabela211.setIdTabelaFcValores(211L);
		
		TabelaFcValores tabela212 = new TabelaFcValores();
		tabela212.setIdTabelaFcValores(212L);
		
		TabelaFcValores tabela213 = new TabelaFcValores();
		tabela213.setIdTabelaFcValores(213L);
		
		TabelaFcValores tabela214 = new TabelaFcValores();
		tabela214.setIdTabelaFcValores(214L);
		
		TabelaFcValores tabela215 = new TabelaFcValores();
		tabela215.setIdTabelaFcValores(215L);
		
		TabelaFcValores tabela216 = new TabelaFcValores();
		tabela216.setIdTabelaFcValores(216L);
		
		TabelaFcValores tabela217 = new TabelaFcValores();
		tabela217.setIdTabelaFcValores(217L);
		
		TabelaFcValores tabela218 = new TabelaFcValores();
		tabela218.setIdTabelaFcValores(218L);
		
		TabelaFcValores tabela219 = new TabelaFcValores();
		tabela219.setIdTabelaFcValores(219L);
		
		TabelaFcValores tabela220 = new TabelaFcValores();
		tabela220.setIdTabelaFcValores(220L);
		
		TabelaFcValores tabela201 = new TabelaFcValores();
		tabela201.setIdTabelaFcValores(201L);
		
		TabelaFcValores tabela202 = new TabelaFcValores();
		tabela202.setIdTabelaFcValores(202L);
		
		TabelaFcValores tabela203 = new TabelaFcValores();
		tabela203.setIdTabelaFcValores(203L);
		
		TabelaFcValores tabela204 = new TabelaFcValores();
		tabela204.setIdTabelaFcValores(204L);
		
		TabelaFcValores tabela205 = new TabelaFcValores();
		tabela205.setIdTabelaFcValores(205L);
		
		TabelaFcValores tabela206 = new TabelaFcValores();
		tabela206.setIdTabelaFcValores(206L);
		
		TabelaFcValores tabela207 = new TabelaFcValores();
		tabela207.setIdTabelaFcValores(207L);
		
		TabelaFcValores tabela208 = new TabelaFcValores();
		tabela208.setIdTabelaFcValores(208L);
		
		TabelaFcValores tabela209 = new TabelaFcValores();
		tabela209.setIdTabelaFcValores(209L);
		
		TabelaFcValores tabela300 = new TabelaFcValores();
		tabela300.setIdTabelaFcValores(300L);
		
		TabelaFcValores tabela301 = new TabelaFcValores();
		tabela301.setIdTabelaFcValores(301L);
		
		TabelaFcValores tabela302 = new TabelaFcValores();
		tabela302.setIdTabelaFcValores(302L);
		
		
		addAtributos(tabela1,true,true,true,true,true);
		addAtributos(tabela2,true,true,true,true,false);
		addAtributos(tabela3,true,true,true,false,true);
		addAtributos(tabela4,true,true,true,false,false);
		addAtributos(tabela5,true,true,false,true,true);
		addAtributos(tabela6,true,true,false,true,false);
		addAtributos(tabela7,true,true,false,false,true);
		addAtributos(tabela8,true,true,false,false,false);
		addAtributos(tabela9,true,false,true,true,true);
		addAtributos(tabela10,true,false,true,true,false);
		addAtributos(tabela11,true,false,true,false,true);
		addAtributos(tabela12,true,false,true,false,false);
		addAtributos(tabela13,true,false,false,true,true);
		addAtributos(tabela14,true,false,false,true,false);
		addAtributos(tabela15,true,false,false,false,true);
		addAtributos(tabela16,true,false,false,false,false);
		addAtributos(tabela17,false,true,true,true,true);
		addAtributos(tabela18,false,true,true,true,false);
		addAtributos(tabela19,false,true,true,false,true);
		addAtributos(tabela20,false,true,true,false,false);
		addAtributos(tabela21,false,true,false,true,true);
		addAtributos(tabela22,false,true,false,true,false);
		addAtributos(tabela23,false,true,false,false,true);
		addAtributos(tabela24,false,true,false,false,false);
		addAtributos(tabela25,false,false,true,true,true);
		addAtributos(tabela26,false,false,true,true,false);
		addAtributos(tabela27,false,false,true,false,true);
		addAtributos(tabela28,false,false,true,false,false);
		addAtributos(tabela29,false,false,false,true,true);
		addAtributos(tabela30,false,false,false,true,false);
		addAtributos(tabela31,false,false,false,false,true);
		addAtributos(tabela32,false,false,false,false,false);
		
		addAtributos2(tabela201,true,true,true,true,true);
		addAtributos2(tabela202,true,true,true,true,false);
		addAtributos2(tabela203,true,true,true,false,true);
		addAtributos2(tabela204,true,true,true,false,false);
		addAtributos2(tabela205,true,true,false,true,true);
		addAtributos2(tabela206,true,true,false,true,false);
		addAtributos2(tabela207,true,true,false,false,true);
		addAtributos2(tabela208,true,true,false,false,false);
		addAtributos2(tabela209,true,false,true,true,true);
		addAtributos2(tabela210,true,false,true,true,false);
		addAtributos2(tabela211,true,false,true,false,true);
		addAtributos2(tabela212,true,false,true,false,false);
		addAtributos2(tabela213,true,false,false,true,true);
		addAtributos2(tabela214,true,false,false,true,false);
		addAtributos2(tabela215,true,false,false,false,true);
		addAtributos2(tabela216,true,false,false,false,false);
		addAtributos2(tabela217,false,true,true,true,true);
		addAtributos2(tabela218,false,true,true,true,false);
		addAtributos2(tabela219,false,true,true,false,true);
		addAtributos2(tabela220,false,true,true,false,false);
		addAtributos2(tabela221,false,true,false,true,true);
		addAtributos2(tabela222,false,true,false,true,false);
		addAtributos2(tabela223,false,true,false,false,true);
		addAtributos2(tabela224,false,true,false,false,false);
		addAtributos2(tabela225,false,false,true,true,true);
		addAtributos2(tabela226,false,false,true,true,false);
		addAtributos2(tabela227,false,false,true,false,true);
		addAtributos2(tabela228,false,false,true,false,false);
		addAtributos2(tabela229,false,false,false,true,true);
		addAtributos2(tabela300,false,false,false,true,false);
		addAtributos2(tabela301,false,false,false,false,true);
		addAtributos2(tabela302,false,false,false,false,false);
		
		tabelas.add(tabela1);
		tabelas.add(tabela2);
		tabelas.add(tabela3);
		tabelas.add(tabela4);
		tabelas.add(tabela5);
		tabelas.add(tabela6);
		tabelas.add(tabela7);
		tabelas.add(tabela8);
		tabelas.add(tabela9);
		tabelas.add(tabela10);
		tabelas.add(tabela11);
		tabelas.add(tabela12);
		tabelas.add(tabela13);
		tabelas.add(tabela14);
		tabelas.add(tabela15);
		tabelas.add(tabela16);
		tabelas.add(tabela17);
		tabelas.add(tabela18);
		tabelas.add(tabela19);
		tabelas.add(tabela20);
		tabelas.add(tabela21);
		tabelas.add(tabela22);
		tabelas.add(tabela23);
		tabelas.add(tabela24);
		tabelas.add(tabela25);
		tabelas.add(tabela26);
		tabelas.add(tabela27);
		tabelas.add(tabela28);
		tabelas.add(tabela29);
		tabelas.add(tabela30);
		tabelas.add(tabela31);
		tabelas.add(tabela32);
		
		tabelas.add(tabela201);
		tabelas.add(tabela202);
		tabelas.add(tabela203);
		tabelas.add(tabela204);
		tabelas.add(tabela205);
		tabelas.add(tabela206);
		tabelas.add(tabela207);
		tabelas.add(tabela208);
		tabelas.add(tabela209);
		tabelas.add(tabela210);
		tabelas.add(tabela211);
		tabelas.add(tabela212);
		tabelas.add(tabela213);
		tabelas.add(tabela214);
		tabelas.add(tabela215);
		tabelas.add(tabela216);
		tabelas.add(tabela217);
		tabelas.add(tabela218);
		tabelas.add(tabela219);
		tabelas.add(tabela220);
		tabelas.add(tabela221);
		tabelas.add(tabela222);
		tabelas.add(tabela223);
		tabelas.add(tabela224);
		tabelas.add(tabela225);
		tabelas.add(tabela226);
		tabelas.add(tabela227);
		tabelas.add(tabela228);
		tabelas.add(tabela229);
		tabelas.add(tabela300);
		tabelas.add(tabela301);
		tabelas.add(tabela302);
		

		TabelaFcValores tabelaAvulsa1 = new TabelaFcValores();
		tabelaAvulsa1.setIdTabelaFcValores(5001L);
		
		tabelaAvulsa1.setRotaColetaEntrega(getRota(5L));
		
		tabelas.add(tabelaAvulsa1);
		
		
		TabelaFcValores tabelaAvulsa2 = new TabelaFcValores();
		tabelaAvulsa2.setIdTabelaFcValores(5002L);
		
		tabelaAvulsa2.setRotaColetaEntrega(getRota(5L));
		
		tabelaAvulsa2.setMunicipio(getMunicipio(5L));
		
		tabelas.add(tabelaAvulsa2);
		
		
		
		return tabelas;
	}

	/**
	 * Seta atributos da tabela conforme entrada
	 * @param tabela
	 * @param rota
	 * @param proprietario
	 * @param cliente
	 * @param municipio
	 * @param tipoMeioTransporte
	 */
	private void addAtributos2(TabelaFcValores tabela, boolean rota, boolean proprietario,boolean cliente, boolean municipio, boolean tipoMeioTransporte) {
		if(rota){
			tabela.setRotaColetaEntrega(getRota(2L));			
		}
		if(proprietario){
			tabela.setProprietario(getProprietario(2L));
		}
		if(cliente){
			tabela.setCliente(getCliente(2L));
		}
		
		if(municipio){
			tabela.setMunicipio(getMunicipio(2L));	
		}
		
		if(tipoMeioTransporte){
			tabela.setTipoMeioTransporte(getTipoMeioTransporte(2L));	
		}
	}


	private TipoMeioTransporte getTipoMeioTransporte(Long id) {
		TipoMeioTransporte tipoMeioTransporte = new TipoMeioTransporte();
		tipoMeioTransporte.setIdTipoMeioTransporte(id);
		return tipoMeioTransporte;
	}

	private Municipio getMunicipio(Long id) {
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(id);
		return municipio;
	}

	private Cliente getCliente(Long id) {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(id);
		return cliente;
	}

	private Proprietario getProprietario(Long id) {
		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(id);
		return proprietario;
	}

	private RotaColetaEntrega getRota(Long id) {
		RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
		rotaColetaEntrega.setIdRotaColetaEntrega(id);
		return rotaColetaEntrega;
	}

	
	

	private void addAtributos(TabelaFcValores tabela, boolean rota, boolean proprietario,boolean cliente, boolean municipio, boolean tipoMeioTransporte) {
		if(rota){
			tabela.setRotaColetaEntrega(getRota(1L));			
		}
		if(proprietario){
			tabela.setProprietario(getProprietario(1L));
		}
		if(cliente){
			tabela.setCliente(getCliente(1L));
		}
		
		if(municipio){
			tabela.setMunicipio(getMunicipio(1L));	
		}
		
		if(tipoMeioTransporte){
			tabela.setTipoMeioTransporte(getTipoMeioTransporte(1L));	
		}		
	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testaGetEstrategiaEntradaVazia() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento =  new Long[99];		
		calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testaGetEstrategiaEntradaNula() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = null;
		calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testaGetEstrategiaEntradaMenor() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 1L, 1L};
		calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
	}
	
	@Test
	public void testaGetValidaTamanhoSaida() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = getDocumento();
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		Assert.assertNotNull(chaves);
		Assert.assertEquals(chaves.size(),64);
	}
	
	@Test
	public void testaGetTabela1() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 1L, 1L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(1L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela2() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 1L, 1L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(3L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela3() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 1L, 6L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(1L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela4() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 1L, 6L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(3L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela5() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 6L, 1L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(2L),tabela.getIdTabelaFcValores());
	}

	@Test
	public void testaGetTabela6() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 6L, 1L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(7L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela7() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 6L, 6L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);		
		Assert.assertEquals(Long.valueOf(5L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela8() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 1L, 6L, 6L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(7L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela9() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 6L, 1L, 1L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(9L),tabela.getIdTabelaFcValores());		
	}
	
	@Test
	public void testaGetTabela10() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 6L, 1L, 1L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(11L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela11() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 6L, 1L, 6L, 1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(9L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela12() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = { 1L, 6L, 1L, 6L, 6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(11L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela13() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {1L,6L,6L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(13L),tabela.getIdTabelaFcValores());
	}
	
		
	@Test
	public void testaGetTabela14() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {1L,6L,6L,1L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(15L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela15() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {1L,6L,6L,6L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(13L),tabela.getIdTabelaFcValores());		
	}
	
	@Test
	public void testaGetTabela16() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {1L,6L,6L,6L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(15L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela17() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,1L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(tabela.getIdTabelaFcValores(),Long.valueOf(17L));
	}
	
	@Test
	public void testaGetTabela18() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,1L,1L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(19L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela19() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,1L,6L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(17L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela20() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,1L,6L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(19L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela21() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,6L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(21L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela22() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,6L,1L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(23L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela23() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,6L,6L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(21L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela24() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,1L,6L,6L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(23L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela25() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,1L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(25L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela26() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,1L,1L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(27L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela27() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,1L,6L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(25L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela28() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,1L,6L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(27L),tabela.getIdTabelaFcValores());
	}
	
	
	@Test
	public void testaGetTabela29() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,6L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(29L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela30() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,6L,1L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(31L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela31() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,6L,6L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(29L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela32() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {6L,6L,6L,6L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(31L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela201() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {2L,2L,2L,2L,2L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(202L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabela202() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {2L,2L,2L,2L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(204L),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabelaAvulsa() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,2L,2L,2L,6L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5001),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabelaAvulsa2() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,1L,2L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5001),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabelaAvulsa3() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,1L,2L,1L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5001),tabela.getIdTabelaFcValores());
	}

	
	@Test
	public void testaGetTabelaAvulsa5() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,1L,1L,5L,1L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5001),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabelaAvulsa6() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,1L,1L,5L,5L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5002),tabela.getIdTabelaFcValores());
	}
	
	@Test
	public void testaGetTabelaAvulsa7() {
		CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
		Long[] documento = {5L,5L,5L,5L,5L,1L};
		List<Chave> chaves = calculoTabelaFreteCarreteiroCe.getEstrategia(documento);
		TabelaFcValores tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(chaves, getTabelas());
		Assert.assertNotNull(tabela);
		Assert.assertEquals(Long.valueOf(5002),tabela.getIdTabelaFcValores());
	}

	
}
