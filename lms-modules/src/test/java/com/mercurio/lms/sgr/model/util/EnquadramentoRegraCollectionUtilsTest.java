package com.mercurio.lms.sgr.model.util;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_DESTINO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_ORIGEM;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_DESTINATARIO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_COLETA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_ENTREGA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_VIAGEM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * LMS-6850 - TDD para {@link EnquadramentoRegraCollectionUtils}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EnquadramentoRegraCollectionUtilsTest {

	private EnquadramentoRegra regraClienteRemetente1;
	private EnquadramentoRegra regraClienteRemetente2;
	private EnquadramentoRegra regraClienteDestinatario1;
	private EnquadramentoRegra regraClienteDestinatario2;
	private EnquadramentoRegra regraClienteRemetenteDestinatario1;
	private EnquadramentoRegra regraClienteRemetenteDestinatario2;
	private EnquadramentoRegra regraValorDominioTpOperacaoColeta;
	private EnquadramentoRegra regraValorDominioTpOperacaoEntrega;
	private EnquadramentoRegra regraValorDominioTpOperacaoViagem;
	private EnquadramentoRegra regraAtributoNaturezaProduto1;
	private EnquadramentoRegra regraAtributoNaturezaProduto2;
	private EnquadramentoRegra regraCriterioEnquadramentoOrigem1;
	private EnquadramentoRegra regraCriterioEnquadramentoOrigem2;
	private EnquadramentoRegra regraCriterioEnquadramentoDestino1;
	private EnquadramentoRegra regraCriterioEnquadramentoDestino2;

	@BeforeTest
	public void beforeTest() {
		// Cliente
		Cliente cliente1 = new Cliente();
		cliente1.setIdCliente(1L);
		Cliente cliente2 = new Cliente();
		cliente2.setIdCliente(2L);
		// tpIntegranteFrete
		DomainValue tpIntegranteFreteRemetente = new DomainValue(TP_INTEGRANTE_FRETE_REMETENTE);
		DomainValue tpIntegranteFreteDestinatario = new DomainValue(TP_INTEGRANTE_FRETE_DESTINATARIO);
		DomainValue tpIntegranteFreteRemetenteDestinatario = new DomainValue(TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO);
		// Pais
		Pais pais1 = new Pais();
		pais1.setIdPais(1L);
		Pais pais2 = new Pais();
		pais2.setIdPais(2L);

		// regraClienteRemetente1 -- idCliente = 1, tpIntegranteFreteRemetente = R
		ClienteEnquadramento clienteEnquadramentoRemetente1 = new ClienteEnquadramento();
		clienteEnquadramentoRemetente1.setCliente(cliente1);
		clienteEnquadramentoRemetente1.setTpIntegranteFrete(tpIntegranteFreteRemetente);
		List<ClienteEnquadramento> clienteEnquadramentosRemetente1 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosRemetente1.add(clienteEnquadramentoRemetente1);
		regraClienteRemetente1 = new EnquadramentoRegra();
		regraClienteRemetente1.setClienteEnquadramentos(clienteEnquadramentosRemetente1);

		// regraClienteRemetente2 -- idCliente = 2, tpIntegranteFreteRemetente = R
		ClienteEnquadramento clienteEnquadramentoRemetente2 = new ClienteEnquadramento();
		clienteEnquadramentoRemetente2.setCliente(cliente2);
		clienteEnquadramentoRemetente2.setTpIntegranteFrete(tpIntegranteFreteRemetente);
		List<ClienteEnquadramento> clienteEnquadramentosRemetente2 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosRemetente2.add(clienteEnquadramentoRemetente2);
		regraClienteRemetente2 = new EnquadramentoRegra();
		regraClienteRemetente2.setClienteEnquadramentos(clienteEnquadramentosRemetente2);

		// regraClienteDestinatario1 -- idCliente = 1, tpIntegranteFreteRemetente = D
		ClienteEnquadramento clienteEnquadramentoDestinatario1 = new ClienteEnquadramento();
		clienteEnquadramentoDestinatario1.setCliente(cliente1);
		clienteEnquadramentoDestinatario1.setTpIntegranteFrete(tpIntegranteFreteDestinatario);
		List<ClienteEnquadramento> clienteEnquadramentosDestinatario1 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosDestinatario1.add(clienteEnquadramentoDestinatario1);
		regraClienteDestinatario1 = new EnquadramentoRegra();
		regraClienteDestinatario1.setClienteEnquadramentos(clienteEnquadramentosDestinatario1);

		// regraClienteDestinatario2 -- idCliente = 2, tpIntegranteFreteRemetente = D
		ClienteEnquadramento clienteEnquadramentoDestinatario2 = new ClienteEnquadramento();
		clienteEnquadramentoDestinatario2.setCliente(cliente2);
		clienteEnquadramentoDestinatario2.setTpIntegranteFrete(tpIntegranteFreteDestinatario);
		List<ClienteEnquadramento> clienteEnquadramentosDestinatario2 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosDestinatario2.add(clienteEnquadramentoDestinatario2);
		regraClienteDestinatario2 = new EnquadramentoRegra();
		regraClienteDestinatario2.setClienteEnquadramentos(clienteEnquadramentosDestinatario2);

		// regraClienteRemetenteDestinatario1 -- idCliente = 1, tpIntegranteFreteRemetente = A
		ClienteEnquadramento clienteEnquadramentoRemetenteDestinatario1 = new ClienteEnquadramento();
		clienteEnquadramentoRemetenteDestinatario1.setCliente(cliente1);
		clienteEnquadramentoRemetenteDestinatario1.setTpIntegranteFrete(tpIntegranteFreteRemetenteDestinatario);
		List<ClienteEnquadramento> clienteEnquadramentosRemetenteDestinatario1 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosRemetenteDestinatario1.add(clienteEnquadramentoRemetenteDestinatario1);
		regraClienteRemetenteDestinatario1 = new EnquadramentoRegra();
		regraClienteRemetenteDestinatario1.setClienteEnquadramentos(clienteEnquadramentosRemetenteDestinatario1);

		// regraClienteRemetenteDestinatario2 -- idCliente = 2, tpIntegranteFreteRemetente = A
		ClienteEnquadramento clienteEnquadramentoRemetenteDestinatario2 = new ClienteEnquadramento();
		clienteEnquadramentoRemetenteDestinatario2.setCliente(cliente2);
		clienteEnquadramentoRemetenteDestinatario2.setTpIntegranteFrete(tpIntegranteFreteRemetenteDestinatario);
		List<ClienteEnquadramento> clienteEnquadramentosRemetenteDestinatario2 = new ArrayList<ClienteEnquadramento>();
		clienteEnquadramentosRemetenteDestinatario2.add(clienteEnquadramentoRemetenteDestinatario2);
		regraClienteRemetenteDestinatario2 = new EnquadramentoRegra();
		regraClienteRemetenteDestinatario2.setClienteEnquadramentos(clienteEnquadramentosRemetenteDestinatario2);

		// regraValorDominio1 -- tpOperacao = C
		regraValorDominioTpOperacaoColeta = new EnquadramentoRegra();
		regraValorDominioTpOperacaoColeta.setTpOperacao(new DomainValue(TP_OPERACAO_COLETA));

		// regraValorDominio2 -- tpOperacao = E
		regraValorDominioTpOperacaoEntrega = new EnquadramentoRegra();
		regraValorDominioTpOperacaoEntrega.setTpOperacao(new DomainValue(TP_OPERACAO_ENTREGA));

		// regraValorDominio3 -- tpOperacao = V
		regraValorDominioTpOperacaoViagem = new EnquadramentoRegra();
		regraValorDominioTpOperacaoViagem.setTpOperacao(new DomainValue(TP_OPERACAO_VIAGEM));

		// regraAtributo1 -- idNaturezaProduto = 1
		NaturezaProduto naturezaProduto1 = new NaturezaProduto();
		naturezaProduto1.setIdNaturezaProduto(1L);
		regraAtributoNaturezaProduto1 = new EnquadramentoRegra();
		regraAtributoNaturezaProduto1.setNaturezaProduto(naturezaProduto1);

		// regraAtributo2 -- idNaturezaProduto = 2
		NaturezaProduto naturezaProduto2 = new NaturezaProduto();
		naturezaProduto2.setIdNaturezaProduto(2L);
		regraAtributoNaturezaProduto2 = new EnquadramentoRegra();
		regraAtributoNaturezaProduto2.setNaturezaProduto(naturezaProduto2);

		// regraCriterioEnquadramentoOrigem1 -- idPais = 1, tpInfluenciaMunicipio = O
		List<Pais> paisOrigem1 = new ArrayList<Pais>();
		paisOrigem1.add(pais1);
		regraCriterioEnquadramentoOrigem1 = new EnquadramentoRegra();
		regraCriterioEnquadramentoOrigem1.setPaisEnquadramentosOrigem(paisOrigem1);

		// regraCriterioEnquadramentoOrigem2 -- idPais = 2, tpInfluenciaMunicipio = O
		List<Pais> paisOrigem2 = new ArrayList<Pais>();
		paisOrigem2.add(pais2);
		regraCriterioEnquadramentoOrigem2 = new EnquadramentoRegra();
		regraCriterioEnquadramentoOrigem2.setPaisEnquadramentosOrigem(paisOrigem2);

		// regraCriterioEnquadramentoDestino1 -- idPais = 1, tpInfluenciaMunicipio = D
		List<Pais> paisDestino1 = new ArrayList<Pais>();
		paisDestino1.add(pais1);
		regraCriterioEnquadramentoDestino1 = new EnquadramentoRegra();
		regraCriterioEnquadramentoDestino1.setPaisEnquadramentosDestino(paisDestino1);

		// regraCriterioEnquadramentoDestino2 -- idPais = 2, tpInfluenciaMunicipio = D
		List<Pais> paisDestino2 = new ArrayList<Pais>();
		paisDestino2.add(pais2);
		regraCriterioEnquadramentoDestino2 = new EnquadramentoRegra();
		regraCriterioEnquadramentoDestino2.setPaisEnquadramentosDestino(paisDestino2);
	}

	@Test(description = "Seleciona regras de cliente remetente específico", groups = "filterClienteEnquadramento")
	public void testClienteRemetenteExists() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1); // true 1 -- idCliente = 1, tpIntegranteFreteRemetente = R
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1); // true 2 -- idCliente = 1, tpIntegranteFreteRemetente = A
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		Assert.assertTrue(EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, 1L, TP_INTEGRANTE_FRETE_REMETENTE));
		Assert.assertEquals(regras.size(), 2);
	}

	@Test(description = "Nenhuma regra para cliente remetente específico", groups = "filterClienteEnquadramento")
	public void testClienteRemetenteIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		//regras.add(regraClienteRemetente2); -- idCliente = 2, tpIntegranteFreteRemetente = R
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1);
		//regras.add(regraClienteRemetenteDestinatario2); -- idCliente = 2, tpIntegranteFreteRemetente = A
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		int size = regras.size();
		Assert.assertFalse(EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, 2L, TP_INTEGRANTE_FRETE_REMETENTE));
		Assert.assertEquals(regras.size(), size);
	}

	@Test(description = "Seleciona regras para cliente destinatário específico", groups = "filterClienteEnquadramento")
	public void testClienteDestinatarioExists() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1); // true 1 -- idCliente = 1, tpIntegranteFreteRemetente = D
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1); // true 2 -- idCliente = 1, tpIntegranteFreteRemetente = A
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		Assert.assertTrue(EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, 1L, TP_INTEGRANTE_FRETE_DESTINATARIO));
		Assert.assertEquals(regras.size(), 2);
	}

	@Test(description = "Nenhuma regra para cliente destinatário específico", groups = "filterClienteEnquadramento")
	public void testClienteDestinatarioIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		//regras.add(regraClienteDestinatario2); -- idCliente = 2, tpIntegranteFreteRemetente = D
		regras.add(regraClienteRemetenteDestinatario1);
		//regras.add(regraClienteRemetenteDestinatario2); -- idCliente = 2, tpIntegranteFreteRemetente = D
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		int size = regras.size();
		Assert.assertFalse(EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, 2L, TP_INTEGRANTE_FRETE_DESTINATARIO));
		Assert.assertEquals(regras.size(), size);
	}

	@Test(description = "Seleciona regras sem cliente remetente/destinatário específico", groups = "filterClienteEnquadramento")
	public void testClienteIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1);
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta); // true 1 -- clienteEnquadramentos.isEmpty
		regras.add(regraValorDominioTpOperacaoEntrega); // true 2 -- clienteEnquadramentos.isEmpty
		regras.add(regraValorDominioTpOperacaoViagem); // true 3 -- clienteEnquadramentos.isEmpty
		regras.add(regraAtributoNaturezaProduto1); // true 4 -- clienteEnquadramentos.isEmpty
		regras.add(regraAtributoNaturezaProduto2); // true 5 -- clienteEnquadramentos.isEmpty
		regras.add(regraCriterioEnquadramentoOrigem1); // true 6 -- clienteEnquadramentos.isEmpty
		regras.add(regraCriterioEnquadramentoOrigem2); // true 7 -- clienteEnquadramentos.isEmpty
		regras.add(regraCriterioEnquadramentoDestino1); // true 8 -- clienteEnquadramentos.isEmpty
		regras.add(regraCriterioEnquadramentoDestino2); // true 9 -- clienteEnquadramentos.isEmpty
		Collections.shuffle(regras);

		EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras);
		Assert.assertEquals(regras.size(), 9);
	}

	@Test(description = "Seleciona regras com valor de domínio específico", groups = "filterValorDominio")
	public void testValorDominioExists() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1);
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta); // true 1 -- tpOperacao = C
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		Assert.assertTrue(EnquadramentoRegraCollectionUtils.filterValorDominio(regras, "TpOperacao", TP_OPERACAO_COLETA));
		Assert.assertEquals(regras.size(), 1);
	}

	@Test(description = "Seleciona regras sem valor de domínio específico", groups = "filterValorDominio")
	public void testValorDominioIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1); // false 1
		regras.add(regraClienteRemetente2); // false 2
		regras.add(regraClienteDestinatario1); // false 3
		regras.add(regraClienteDestinatario2); // false 4
		regras.add(regraClienteRemetenteDestinatario1); // false 5
		regras.add(regraClienteRemetenteDestinatario2); // false 6
		//regras.add(regraValorDominioTpOperacaoColeta); -- tpOperacao = C
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1); // false 7
		regras.add(regraAtributoNaturezaProduto2); // false 8
		regras.add(regraCriterioEnquadramentoOrigem1); // false 9
		regras.add(regraCriterioEnquadramentoOrigem2); // false 10
		regras.add(regraCriterioEnquadramentoDestino1); // false 11
		regras.add(regraCriterioEnquadramentoDestino2); // false 12
		Collections.shuffle(regras);

		Assert.assertFalse(EnquadramentoRegraCollectionUtils.filterValorDominio(regras, "TpOperacao", TP_OPERACAO_COLETA));
		Assert.assertEquals(regras.size(), 12);
	}
	
	@Test(description = "Seleciona regras com atributo específico", groups = "filterIdAtributo")
	public void testIdAtributoExists() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1);
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1); // true 1 -- idNaturezaProduto = 1
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1);
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		Assert.assertTrue(EnquadramentoRegraCollectionUtils.filterIdAtributo(regras, "NaturezaProduto", 1L));
		Assert.assertEquals(regras.size(), 1);
	}

	@Test(description = "Seleciona regras sem atributo específico", groups = "filterIdAtributo")
	public void testIdAtributoIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1); // false 1
		regras.add(regraClienteRemetente2); // false 2
		regras.add(regraClienteDestinatario1); // false 3
		regras.add(regraClienteDestinatario2); // false 4
		regras.add(regraClienteRemetenteDestinatario1); // false 5
		regras.add(regraClienteRemetenteDestinatario2); // false 6
		regras.add(regraValorDominioTpOperacaoColeta); // false 7
		regras.add(regraValorDominioTpOperacaoEntrega); // false 8
		regras.add(regraValorDominioTpOperacaoViagem); // false 9
		regras.add(regraAtributoNaturezaProduto1); 
		//regras.add(regraAtributoNaturezaProduto2); -- idNaturezaProduto = 2
		regras.add(regraCriterioEnquadramentoOrigem1); // false 10
		regras.add(regraCriterioEnquadramentoOrigem2); // false 11
		regras.add(regraCriterioEnquadramentoDestino1); // false 12
		regras.add(regraCriterioEnquadramentoDestino2); // false 13
		Collections.shuffle(regras);

		Assert.assertFalse(EnquadramentoRegraCollectionUtils.filterIdAtributo(regras, "NaturezaProduto", 2L));
		Assert.assertEquals(regras.size(), 13);
	}

	@Test(description = "Seleciona regras com critério de enquadramento específico", groups = "filterCriterioEnquadramento")
	public void testCriterioEnquadramentoExists() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1);
		regras.add(regraClienteRemetente2);
		regras.add(regraClienteDestinatario1);
		regras.add(regraClienteDestinatario2);
		regras.add(regraClienteRemetenteDestinatario1);
		regras.add(regraClienteRemetenteDestinatario2);
		regras.add(regraValorDominioTpOperacaoColeta);
		regras.add(regraValorDominioTpOperacaoEntrega);
		regras.add(regraValorDominioTpOperacaoViagem);
		regras.add(regraAtributoNaturezaProduto1);
		regras.add(regraAtributoNaturezaProduto2);
		regras.add(regraCriterioEnquadramentoOrigem1); // true 1 -- idPais = 1, tpInfluenciaMunicipio = O
		regras.add(regraCriterioEnquadramentoOrigem2);
		regras.add(regraCriterioEnquadramentoDestino1);
		regras.add(regraCriterioEnquadramentoDestino2);
		Collections.shuffle(regras);

		Assert.assertTrue(EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_ORIGEM, "Pais", 1L));
		Assert.assertEquals(regras.size(), 1);
	}

	@Test(description = "Seleciona regras sem critério de enquadramento específico", groups = "filterCriterioEnquadramento")
	public void testCriterioEnquadramentoIsEmpty() throws Exception {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>();
		regras.add(regraClienteRemetente1); // false 1
		regras.add(regraClienteRemetente2); // false 2
		regras.add(regraClienteDestinatario1); // false 3
		regras.add(regraClienteDestinatario2); // false 4
		regras.add(regraClienteRemetenteDestinatario1); // false 5
		regras.add(regraClienteRemetenteDestinatario2); // false 6
		regras.add(regraValorDominioTpOperacaoColeta); // false 7
		regras.add(regraValorDominioTpOperacaoEntrega); // false 8
		regras.add(regraValorDominioTpOperacaoViagem); // false 9
		regras.add(regraAtributoNaturezaProduto1); // false 10
		regras.add(regraAtributoNaturezaProduto2); // false 11
		regras.add(regraCriterioEnquadramentoOrigem1); // false 12
		regras.add(regraCriterioEnquadramentoOrigem2); // false 13
		regras.add(regraCriterioEnquadramentoDestino1); 
		//regras.add(regraCriterioEnquadramentoDestino2); -- idPais = 2, tpInfluenciaMunicipio = D
		Collections.shuffle(regras);

		Assert.assertFalse(EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_DESTINO, "Pais", 2L));
		Assert.assertEquals(regras.size(), 13);
	}

}
