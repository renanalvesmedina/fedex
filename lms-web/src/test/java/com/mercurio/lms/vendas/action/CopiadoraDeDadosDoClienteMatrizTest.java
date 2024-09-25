package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CopiadoraDeDadosDoClienteMatrizTest {

    private final ClienteService service = mock(ClienteService.class);
    private final Cliente cliente = mock(Cliente.class);
    private final DomainValue tipoDeCliente = mock(DomainValue.class);

    private final Cliente clienteMatriz = mock(Cliente.class);
    private final Cliente responsavel = mock(Cliente.class);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        when(cliente.getTpCliente()).thenReturn(tipoDeCliente);
        when(tipoDeCliente.getValue()).thenReturn(ConstantesVendas.CLIENTE_FILIAL);
        when(cliente.getClienteMatriz()).thenReturn(clienteMatriz);
        when(cliente.getCliente()).thenReturn(responsavel);

        final Moeda moeda= mock(Moeda.class);
        when(moeda.getIdMoeda()).thenReturn(RandomUtils.nextLong());
        final DomainValue tipoDeCobrancaMatriz = mock(DomainValue.class);
        when(tipoDeCobrancaMatriz.getValue()).thenReturn(RandomStringUtils.randomAlphabetic(10));

        when(clienteMatriz.getIdCliente()).thenReturn(RandomUtils.nextLong());
        when(clienteMatriz.getMoedaByIdMoedaLimCred()).thenReturn(moeda);
        when(clienteMatriz.getMoedaByIdMoedaLimDoctos()).thenReturn(moeda);
        when(clienteMatriz.getMoedaByIdMoedaSaldoAtual()).thenReturn(moeda);
        when(clienteMatriz.getVlLimiteCredito()).thenReturn(BigDecimal.valueOf(RandomUtils.nextDouble()));
        when(clienteMatriz.getVlLimiteDocumentos()).thenReturn(BigDecimal.valueOf(RandomUtils.nextDouble()));
        when(clienteMatriz.getPcDescontoFreteCif()).thenReturn(BigDecimal.valueOf(RandomUtils.nextDouble()));
        when(clienteMatriz.getPcDescontoFreteFob()).thenReturn(BigDecimal.valueOf(RandomUtils.nextDouble()));
        when(clienteMatriz.getTpCobranca()).thenReturn(tipoDeCobrancaMatriz);

        final Pessoa pessoaResponsavel = mock(Pessoa.class);
        when(pessoaResponsavel.getNmPessoa()).thenReturn(RandomStringUtils.randomAlphabetic(10));

        when(responsavel.getIdCliente()).thenReturn(RandomUtils.nextLong());
        when(responsavel.getPessoa()).thenReturn(pessoaResponsavel);

        when(service.findById(cliente.getClienteMatriz().getIdCliente())).thenReturn(clienteMatriz);
        when(service.findById(cliente.getCliente().getIdCliente())).thenReturn(responsavel);
    }

    @Test
    public void testClienteServerNaoPodeSerNulo() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("O parâmetro 'clienteService' não pode ser nulo");
        //noinspection ConstantConditions
        new CopiadoraDeDadosDoClienteMatriz(null);
    }

    @Test
    public void testSoPodeCopiarSeForUmClienteFilial() {
        final ClienteService service = mock(ClienteService.class);
        final Cliente cliente = mock(Cliente.class);
        final DomainValue tipoDeCliente = mock(DomainValue.class);

        when(cliente.getTpCliente()).thenReturn(tipoDeCliente);
        when(tipoDeCliente.getValue()).thenReturn(ConstantesVendas.CLIENTE_EVENTUAL);

        CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testCopiarParametroIdCliente() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(responsavel.getIdCliente(), resultado.get("cliente.idCliente"));
    }

    @Test
    public void testCopiarParametroClientePessoaNumeroIdentificacao() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(FormatUtils.formatIdentificacao(responsavel.getPessoa()), resultado.get("cliente.pessoa.nrIdentificacao"));
    }

    @Test
    public void testCopiarParametroClientePessoaNome() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(responsavel.getPessoa().getNmPessoa(), resultado.get("cliente.pessoa.nmPessoa"));
    }

    @Test
    public void testCopiarParametroMoedaByIdMoedaLimCredIdMoeda() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(clienteMatriz.getMoedaByIdMoedaLimCred().getIdMoeda(), resultado.get("moedaByIdMoedaLimCred.idMoeda"));
    }

    @Test
    public void testCopiarParametroMoedaByIdMoedaLimDoctosIdMoeda() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(clienteMatriz.getMoedaByIdMoedaLimDoctos().getIdMoeda(), resultado.get("moedaByIdMoedaLimDoctos.idMoeda"));
    }

    @Test
    public void testCopiarParametroMoedaByIdMoedaSaldoAtualIdMoeda() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(clienteMatriz.getMoedaByIdMoedaSaldoAtual().getIdMoeda(), resultado.get("moedaByIdMoedaSaldoAtual.idMoeda"));
    }

    @Test
    public void testCopiarParametroLimiteCredito() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertNotNull(clienteMatriz.getVlLimiteCredito());
        assertEquals(clienteMatriz.getVlLimiteCredito(), resultado.get("vlLimiteCredito"));
    }

    @Test
    public void testCopiarParametroLimiteDocumentos() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertNotNull(clienteMatriz.getVlLimiteDocumentos());
        assertEquals(clienteMatriz.getVlLimiteDocumentos(), resultado.get("vlLimiteDocumentos"));

    }

    @Test
    public void testCopiarParametroSaldoAtual() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(BigDecimalUtils.ZERO, resultado.get("vlSaldoAtual"));
    }

    @Test
    public void testCopiarParametroDescontoFreteCifl() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertNotNull(clienteMatriz.getPcDescontoFreteCif());
        assertEquals(clienteMatriz.getPcDescontoFreteCif(), resultado.get("pcDescontoFreteCif"));
    }

    @Test
    public void testCopiarParametroDescontoFreteFob() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertNotNull(clienteMatriz.getPcDescontoFreteFob());
        assertEquals(clienteMatriz.getPcDescontoFreteFob(), resultado.get("pcDescontoFreteFob"));
    }

    @Test
    public void testCopiarParametroTipoCobranca() {
        final CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertNotNull(clienteMatriz.getTpCobranca().getValue());
        assertEquals(clienteMatriz.getTpCobranca().getValue(), resultado.get("tpCobranca.value"));
    }

    @Test
    public void testCopiarParametroBlEnviaDocsFaturamentoNas() {
        CopiadoraDeDadosDoClienteMatriz copiadora = new CopiadoraDeDadosDoClienteMatriz(service);
        when(clienteMatriz.getBlEnviaDocsFaturamentoNas()).thenReturn(RandomUtils.nextBoolean());
        TypedFlatMap resultado = copiadora.copiarSomenteSeForUmClienteFilial(cliente);
        assertNotNull(resultado);
        assertEquals(clienteMatriz.getBlEnviaDocsFaturamentoNas(), resultado.get("blEnviaDocsFaturamentoNas"));
    }
}
