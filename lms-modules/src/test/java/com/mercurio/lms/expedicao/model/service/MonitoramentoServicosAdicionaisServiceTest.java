/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.expedicao.servicoadicional.DocumentoCalculoMensalServicoAdicionalDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteDestinatarioService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import org.joda.time.YearMonthDay;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author linux
 */
public class MonitoramentoServicosAdicionaisServiceTest {

    public MonitoramentoServicosAdicionaisServiceTest() {
    }
    
    private MonitoramentoServicosAdicionaisService instance;

    @Mock
    private ParametroGeralService parametroGeralService;

    @BeforeMethod
    public final void beforeTest() throws Exception {
        instance = new MonitoramentoServicosAdicionaisService();
        MockitoAnnotations.initMocks(this);
        instance.setParametroGeralService(parametroGeralService);
    }

    /**
     * Test of getVlParametroYearMonthDay method, of class
     * MonitoramentoServicosAdicionaisService.
     */
    @Test
    public void testGetVlParametroYearMonthDay() {
        System.out.println("getVlParametroYearMonthDay");
        int year = 2017;
        int month = 10;
        int day = 15;
        String parametro = "DT_EXEC_PERM_M";
        String retorno = day+"/"+month+"/"+year;
        YearMonthDay expResult = new YearMonthDay(year, month, day);
        Mockito.when(parametroGeralService.findSimpleConteudoByNomeParametro(parametro)).thenReturn(retorno);
        YearMonthDay result = instance.getVlParametroYearMonthDay(parametro);
        Assert.assertEquals(expResult, result);
    }
    
    @Test(expectedExceptions = RuntimeException.class)
    public void testGetVlParametroYearMonthDayError() {
        System.out.println("getVlParametroYearMonthDay");
        int year = 2017;
        int month = 13;
        int day = 15;
        String parametro = "DT_EXEC_PERM_M";
        String retorno = day+"/"+month+"/"+year;
        YearMonthDay expResult = new YearMonthDay(year, month, day);
        Mockito.when(parametroGeralService.findSimpleConteudoByNomeParametro(parametro)).thenReturn(retorno);
        YearMonthDay result = instance.getVlParametroYearMonthDay(parametro);
        Assert.assertEquals(expResult, result);
    }

}
