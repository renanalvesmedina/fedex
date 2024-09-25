package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ManterClienteService;
import com.mercurio.lms.vendas.model.service.UsuarioClienteResponsavelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({WarningCollectorUtils.class, ManterClienteAction.class, CopiadoraDeDadosDoClienteMatriz.class, TypedFlatMap.class})
public class ManterClienteActionTest {

    @Test
    public void testCopiarDadosClienteMatriz() throws Exception {
        PowerMockito.mockStatic(WarningCollectorUtils.class);

        final ClienteService clienteService = mock(ClienteService.class);
        final UsuarioClienteResponsavelService usuarioClienteResponsavelService = mock(UsuarioClienteResponsavelService.class);
        final ManterClienteService manterClienteService = mock(ManterClienteService.class);

        final Cliente cliente = mock(Cliente.class);
        when(cliente.getPessoa()).thenReturn(mock(Pessoa.class));
        when(cliente.getTpSituacao()).thenReturn(mock(DomainValue.class));
        when(cliente.getTpCliente()).thenReturn(mock(DomainValue.class));
        when(cliente.getUsuarioByIdUsuarioAlteracao()).thenReturn(mock(Usuario.class));
        when(cliente.getUsuarioByIdUsuarioInclusao()).thenReturn(mock(Usuario.class));

        final CopiadoraDeDadosDoClienteMatriz copiadora = mock(CopiadoraDeDadosDoClienteMatriz.class);
        final TypedFlatMap resultadoCopiadora = new TypedFlatMap();
        String keyTeste = "chave.teste";
        resultadoCopiadora.put(keyTeste, "valor.teste");
        when(copiadora.copiarSomenteSeForUmClienteFilial(cliente)).thenReturn(resultadoCopiadora);
        PowerMockito.whenNew(CopiadoraDeDadosDoClienteMatriz.class).withArguments(clienteService).thenReturn(copiadora);

        TypedFlatMap parametrosCopidados = new TypedFlatMap();
        PowerMockito.whenNew(TypedFlatMap.class).withNoArguments().thenReturn(parametrosCopidados);

        final ManterClienteAction action = new ManterClienteAction();
        action.setClienteService(clienteService);
        action.setUsuarioClienteResponsavelService(usuarioClienteResponsavelService);
        action.setManterClienteService(manterClienteService);
        action.store(cliente);

        assertTrue(parametrosCopidados.containsKey(keyTeste));
    }

}