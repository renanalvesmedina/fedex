package com.mercurio.lms.rest.vendas;

import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.rest.vendas.dto.ParametrosClienteDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametrosClienteRestTest {

    @Test
    public void testCarregarParametroEnviaDocsFaturamentoNas() {
        final ClienteService clienteService = mock(ClienteService.class);
        final long id = RandomUtils.nextLong();
        final Cliente cliente = new Cliente(id);
        cliente.setPessoa(new Pessoa());

        cliente.setBlEnviaDocsFaturamentoNas(RandomUtils.nextBoolean());
        when(clienteService.findById(id)).thenReturn(cliente);

        final ParametrosClienteRest parametrosClienteRest = new ParametrosClienteRest();
        Whitebox.setInternalState(parametrosClienteRest, "clienteService", clienteService);

        ParametrosClienteDTO parametros = parametrosClienteRest.findById(id);
        assertEquals(cliente.getBlEnviaDocsFaturamentoNas(), parametros.getEnviaDocsFaturamentoNas());
    }

    @Test
    public void testSalvarParametroEnviaDocsFaturamentoNas() {
        final ClienteService clienteService = mock(ClienteService.class);
        final long id = RandomUtils.nextLong();
        final Cliente cliente = new Cliente(id);

        when(clienteService.findById(id)).thenReturn(cliente);

        final ParametrosClienteRest parametrosClienteRest = new ParametrosClienteRest();
        Whitebox.setInternalState(parametrosClienteRest, "clienteService", clienteService);

        ParametrosClienteDTO parametros = new ParametrosClienteDTO();
        parametros.setId(id);
        parametros.setEnviaDocsFaturamentoNas(RandomUtils.nextBoolean());

        parametrosClienteRest.store(parametros);

        assertEquals(parametros.getEnviaDocsFaturamentoNas(), cliente.getBlEnviaDocsFaturamentoNas());
    }
}