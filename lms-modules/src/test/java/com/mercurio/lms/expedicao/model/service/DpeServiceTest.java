package com.mercurio.lms.expedicao.model.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.LmsMocks;
import com.mercurio.lms.vendas.model.Cliente;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.mockito.Mockito.when;

public class DpeServiceTest {

    private static final DateTimeFormatter DATA_PATTERN = DateTimeFormat.forPattern("dd-MM");

    private static final DomainValue TEN = new DomainValue("10");

    private DpeService dpeService;

    @Mock
    private Cliente clienteDestinatario;

    @Mock
    private Cliente clienteConsignatario;

    @Mock
    private Cliente clienteDevedor;

    @BeforeMethod
    public final void beforeTest() throws Exception {
        dpeService = new DpeService();
        MockitoAnnotations.initMocks(this);

        LmsMocks.mockDefaultContext();
    }

    // calcAdicionalDificuldadeEntrega
    @Test
    public void shouldReturnZeroWhenAllEntitiesAreNull() {
        clienteConsignatario = null;
        clienteDestinatario = null;
        clienteDevedor = null;

        Long result = dpeService.calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor);

        Assert.assertEquals(result, LongUtils.ZERO);
    }

    // calcAdicionalDificuldadeEntrega
    @Test
    public void shouldReturnZeroWhenDevedorBlDesconsideraEntregaIsTrue() {
        when(clienteDestinatario.getTpDificuldadeEntrega()).thenReturn(TEN);
        when(clienteDevedor.getBldesconsiderarDificuldade()).thenReturn(Boolean.TRUE);
        clienteConsignatario = null;

        Long result = dpeService.calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor);

        Assert.assertEquals(result, LongUtils.ZERO);
    }

    // calcAdicionalDificuldadeEntrega
    @Test
    public void shouldReturnDestinatarioWhenDevedorBlDesconsideraEntregaIsFalse() {
        when(clienteDestinatario.getTpDificuldadeEntrega()).thenReturn(TEN);
        when(clienteDevedor.getBldesconsiderarDificuldade()).thenReturn(Boolean.FALSE);
        clienteConsignatario = null;

        Long result = dpeService.calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor);

        Assert.assertEquals(result, LongUtils.TEN);
    }

    @Test
    public void assertDpeAndNrDiasRealEntrega() {
        // Datas para teste        
        YearMonthDay segunda = new YearMonthDay(2019, 2, 4);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);
        YearMonthDay proximaTerca = proximaSegunda.plusDays(1);
        
        // Vari�veis utilizadas nas valida��es do DPE e do c�lculo real de entrega
        YearMonthDay dtPrazoEntregaEsperado;
        Long nrPrazoEsperado;
        int diasEsperado;
        YearMonthDay dtInicio;
        YearMonthDay dtFinal;
        Long nrPrazo;
        Boolean remetenteIgnoraFeriados;
        Set<String> feriadosOrigem;
        Set<String> feriadosDestino;
        Set<String> feriadosNacionais;
        Boolean devedorEmiteDiaNaoUtil;
        Boolean devedorEmiteSabado;

        // **********
        // Valida��es
        // ********** 
        // Valida��o 001
        dtPrazoEntregaEsperado = proximaSegunda;
        diasEsperado = 5;
        nrPrazoEsperado = 5L;
        dtInicio = segunda;
        dtFinal = proximaSegunda;
        nrPrazo = 5L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        feriadosNacionais = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.FALSE;
        devedorEmiteSabado = Boolean.FALSE;

        dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        // Valida��o 002
        dtPrazoEntregaEsperado = quarta;
        diasEsperado = 2;
        nrPrazoEsperado = 2L;
        dtInicio = segunda;
        dtFinal = quarta;
        nrPrazo = 2L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.FALSE;
        devedorEmiteSabado = Boolean.FALSE;

        feriadosDestino.add(proximaSegunda.toString(DATA_PATTERN));

        dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        // Valida��o 003
        dtPrazoEntregaEsperado = proximaTerca;
        diasEsperado = 2;
        nrPrazoEsperado = 1L;
        dtInicio = sexta;
        dtFinal = proximaTerca;
        nrPrazo = 1L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.TRUE;
        devedorEmiteSabado = Boolean.FALSE;

        feriadosOrigem.add(sexta.toString(DATA_PATTERN));

        //dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);                
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        // Valida��o 004
        dtPrazoEntregaEsperado = proximaSegunda;
        diasEsperado = 1;
        nrPrazoEsperado = 1L;
        dtInicio = sexta;
        dtFinal = proximaSegunda;
        nrPrazo = 1L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.TRUE;
        devedorEmiteSabado = Boolean.TRUE;

        feriadosOrigem.add(sexta.toString(DATA_PATTERN));

        dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);                
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        // Valida��o 005
        dtPrazoEntregaEsperado = sexta;
        diasEsperado = 1;
        nrPrazoEsperado = 3L;
        dtInicio = segunda;
        dtFinal = quarta;
        nrPrazo = 3L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.TRUE;
        devedorEmiteSabado = Boolean.FALSE;

        feriadosOrigem.add(segunda.toString(DATA_PATTERN));
        feriadosOrigem.add(terca.toString(DATA_PATTERN));
        feriadosOrigem.add(sexta.toString(DATA_PATTERN));

        //dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        // Valida��o 006
        dtPrazoEntregaEsperado = sexta;
        diasEsperado = 4;
        nrPrazoEsperado = 3L;
        dtInicio = segunda;
        dtFinal = proximaSegunda;
        nrPrazo = 3L;
        remetenteIgnoraFeriados = Boolean.FALSE;
        feriadosOrigem = new HashSet<String>();
        feriadosDestino = new HashSet<String>();
        devedorEmiteDiaNaoUtil = Boolean.TRUE;
        devedorEmiteSabado = Boolean.FALSE;

        feriadosOrigem.add(segunda.toString(DATA_PATTERN));
        feriadosOrigem.add(terca.toString(DATA_PATTERN));
        feriadosOrigem.add(sexta.toString(DATA_PATTERN));

        //dtPrazoEntregaEsperado = adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);
        //assertDpeAndNrDiasRealEntrega(dtPrazoEntregaEsperado, nrPrazoEsperado, diasEsperado, dtInicio, dtFinal, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, feriadosNacionais, devedorEmiteDiaNaoUtil, devedorEmiteSabado);
    }
    
    private YearMonthDay adicionaMaisUmDiaUtilCasoADataFinalSejaMenorQueADataAtual(YearMonthDay dtPrazoEntregaEsperado, Set<String> feriadosDestino, Boolean remetenteIgnoraFeriados){
        if (dtPrazoEntregaEsperado.compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
            dtPrazoEntregaEsperado = dpeService.getProximoDiaUtil(dtPrazoEntregaEsperado, feriadosDestino, remetenteIgnoraFeriados);
        }
        return dtPrazoEntregaEsperado;
    }

    private void assertDpeAndNrDiasRealEntrega(YearMonthDay dtPrazoEntregaEsperado, Long nrPrazoEsperado, int diasEsperado,
            YearMonthDay dtInicio, YearMonthDay dtFinal, Long nrPrazo,
            Boolean remetenteIgnoraFeriados, Set<String> feriadosOrigem, Set<String> feriadosDestino, Set<String> feriadosNacionais,
            Boolean devedorEmiteDiaNaoUtil, Boolean devedorEmiteSabado, Long idMunicipioFilialDestino) {
        Map<String, Object> retorno = dpeService.getProximosDiasUteis(dtInicio, nrPrazo, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, devedorEmiteDiaNaoUtil, devedorEmiteSabado, idMunicipioFilialDestino);
        int nrDiasRealEntrega = dpeService.findQtdeDiasUteisEntreDatas(dtInicio, dtFinal, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, devedorEmiteDiaNaoUtil, devedorEmiteSabado);

        YearMonthDay _dtPrazoEntrega = (YearMonthDay) retorno.get("dtPrazoEntrega");
        Long nrDiasPrevEntrega = (Long) retorno.get("nrPrazo");

        Assert.assertEquals(_dtPrazoEntrega, dtPrazoEntregaEsperado);
        Assert.assertEquals(nrDiasPrevEntrega, nrPrazoEsperado);
        Assert.assertEquals(nrDiasRealEntrega, diasEsperado);
    }

    // getProximosDiasUteis(final YearMonthDay dataInicio, int diasUteis, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnSegundaWhenProximosDiasUteisIs00() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);

        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 0, feriados, null), segunda);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 0, feriados, Boolean.FALSE), segunda);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 0, feriados, Boolean.TRUE), segunda);
    }

    // getProximosDiasUteis(final YearMonthDay dataInicio, int diasUteis, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnTercaWhenProximosDiasUteisIs01AndTercaIsNotFeriado() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);

        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, null), terca);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, Boolean.FALSE), terca);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, Boolean.TRUE), terca);
    }

    // getProximosDiasUteis(final YearMonthDay dataInicio, int diasUteis, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnTercaWhenProximosDiasUteisIs01AndTercaIsFeriadoAndIgnoraFeriadosIsTrue() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);

        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, Boolean.TRUE), terca);
    }

    // getProximosDiasUteis(final YearMonthDay dataInicio, int diasUteis, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnProximaTercaWhenProximosDiasUteisIs01AndTercaQuartaQuintaSextaProximaSegundaIsFeriadoAndIgnoraFeriadosIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);
        YearMonthDay proximaTerca = proximaSegunda.plusDays(1);

        feriados.add(segunda.toString(DATA_PATTERN));
        feriados.add(terca.toString(DATA_PATTERN));
        feriados.add(quarta.toString(DATA_PATTERN));
        feriados.add(quinta.toString(DATA_PATTERN));
        feriados.add(sexta.toString(DATA_PATTERN));
        feriados.add(proximaSegunda.toString(DATA_PATTERN));

        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, null), proximaTerca);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 1, feriados, Boolean.FALSE), proximaTerca);
    }

    @Test
    public void shouldReturnProximaSextaWhenProximosDiasUteisIs05AndTercaQuartaQuintaSextaIsFeriadoAndIgnoraFeriadosIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);
        YearMonthDay proximaTerca = proximaSegunda.plusDays(1);
        YearMonthDay proximaQuarta = proximaTerca.plusDays(1);
        YearMonthDay proximaQuinta = proximaQuarta.plusDays(1);
        YearMonthDay proximaSexta = proximaQuinta.plusDays(1);

        feriados.add(segunda.toString(DATA_PATTERN));
        feriados.add(terca.toString(DATA_PATTERN));
        feriados.add(quarta.toString(DATA_PATTERN));
        feriados.add(quinta.toString(DATA_PATTERN));
        feriados.add(sexta.toString(DATA_PATTERN));

        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 5, feriados, null), proximaSexta);
        Assert.assertEquals(dpeService.getProximosDiasUteis(segunda, 5, feriados, Boolean.FALSE), proximaSexta);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnTercaWhenTercaIsFeriadoAndIgnoraFeriadosIsTrue() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);

        feriados.add(terca.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.TRUE), terca);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados, Boolean ignoraFeriados)
    @Test
    public void shouldReturnProximaTercaWhenTercaQuartaQuintaSextaProximaSegundaIsFeriadoAndIgnoraFeriadosIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);
        YearMonthDay proximaTerca = proximaSegunda.plusDays(1);

        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), terca);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), terca);

        feriados.add(segunda.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), terca);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), terca);

        feriados.add(terca.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), quarta);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), quarta);

        feriados.add(quarta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), quinta);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), quinta);

        feriados.add(quinta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), sexta);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), sexta);

        feriados.add(sexta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), proximaSegunda);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), proximaSegunda);

        feriados.add(proximaSegunda.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, null), proximaTerca);
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados, Boolean.FALSE), proximaTerca);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados)
    @Test
    public void shouldReturnProximoDiaUtilWhenDiaDaSemanaIsNotFeriado() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);

        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), terca);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados)
    @Test
    public void shouldReturnProximoDiaUtilWhenDiaDaSemanaIsFeriado() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);
        YearMonthDay proximaTerca = proximaSegunda.plusDays(1);

        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), terca);

        feriados.add(segunda.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), terca);

        feriados.add(terca.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), quarta);

        feriados.add(quarta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), quinta);

        feriados.add(quinta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), sexta);

        feriados.add(sexta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), proximaSegunda);

        feriados.add(proximaSegunda.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximoDiaUtil(segunda, feriados), proximaTerca);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio)
    @Test
    public void shouldReturnProximoDiaUtilWhenDiaDaSemanaIsSabado() {
        YearMonthDay sabado = new YearMonthDay(2019, 1, 5);
        YearMonthDay segunda = sabado.plusDays(2);

        Assert.assertEquals(dpeService.getProximoDiaUtil(sabado), segunda);
    }

    // getProximoDiaUtil(final YearMonthDay dataInicio)
    @Test
    public void shouldReturnProximoDiaUtilWhenDiaDaSemanaIsDomingo() {
        YearMonthDay domingo = new YearMonthDay(2019, 1, 6);
        YearMonthDay segunda = domingo.plusDays(1);

        Assert.assertEquals(dpeService.getProximoDiaUtil(domingo), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnSabadoWhenDiaDaSemanaIsSabadoAndBlEmissaoNoSabadoIsTrue() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay sabado = new YearMonthDay(2019, 1, 5);

        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(Boolean.TRUE);
        Assert.assertEquals(sabado, dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, null, clienteDevedor));
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnSegundaWhenDiaDaSemanaIsSabadoAndBlEmissaoNoSabadoIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay sabado = new YearMonthDay(2019, 1, 5);
        YearMonthDay segunda = sabado.plusDays(2);

        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(null);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, null, clienteDevedor), segunda);

        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(Boolean.FALSE);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, null, clienteDevedor), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnSegundaWhenDiaDaSemanaIsDomingo() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay domingo = new YearMonthDay(2019, 1, 6);
        YearMonthDay segunda = domingo.plusDays(1);

        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(domingo, feriados, null, clienteDevedor), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnSegundaWhenSegundaIsFeriadoAndIgnoraFeriadosIsTrue() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay sabado = new YearMonthDay(2019, 1, 5);
        YearMonthDay segunda = sabado.plusDays(2);

        feriados.add(segunda.toString(DATA_PATTERN));

        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, Boolean.TRUE, clienteDevedor), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnTercaWhenSegundaIsFeriadoAndIgnoraFeriadosIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay sabado = new YearMonthDay(2019, 1, 5);
        YearMonthDay segunda = sabado.plusDays(2);
        YearMonthDay terca = segunda.plusDays(1);

        feriados.add(segunda.toString(DATA_PATTERN));

        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, null, clienteDevedor), terca);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(sabado, feriados, Boolean.FALSE, clienteDevedor), terca);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnDiaAtualWhenDiaAtualIsNotFeriado() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);

        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), segunda);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), segunda);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.TRUE, clienteDevedor), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnDiaAtualWhenDiaAtualIsFeriadoAndIgnoraFeriadosIsTrue() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);

        feriados.add(segunda.toString(DATA_PATTERN));

        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.TRUE, clienteDevedor), segunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnDiaAtualWhenDiaAtualIsFeriadoAndIgnoraFeriadosIsFalse() {
        Set<String> feriados = new HashSet<String>();
        YearMonthDay segunda = new YearMonthDay(2019, 1, 7);
        YearMonthDay terca = segunda.plusDays(1);
        YearMonthDay quarta = terca.plusDays(1);
        YearMonthDay quinta = quarta.plusDays(1);
        YearMonthDay sexta = quinta.plusDays(1);
        YearMonthDay sabado = sexta.plusDays(1);
        YearMonthDay domingo = sabado.plusDays(1);
        YearMonthDay proximaSegunda = domingo.plusDays(1);

        feriados.add(segunda.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), terca);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), terca);

        feriados.add(terca.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), quarta);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), quarta);

        feriados.add(quarta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), quinta);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), quinta);

        feriados.add(quinta.toString(DATA_PATTERN));
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), sexta);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), sexta);

        feriados.add(sexta.toString(DATA_PATTERN));
        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(Boolean.TRUE);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), sabado);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), sabado);

        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(null);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), proximaSegunda);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), proximaSegunda);

        when(clienteDevedor.getBlEmissaoSabado()).thenReturn(Boolean.FALSE);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, null, clienteDevedor), proximaSegunda);
        Assert.assertEquals(dpeService.getProximaDataUtilEmDocumentosEmitidosAoSabado(segunda, feriados, Boolean.FALSE, clienteDevedor), proximaSegunda);
    }

    // getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor)
    @Test
    public void shouldReturnConsignatarioWhenDevedorBlDesconsideraEntregaIsFalse() {
        when(clienteConsignatario.getTpDificuldadeEntrega()).thenReturn(TEN);
        when(clienteDevedor.getBldesconsiderarDificuldade()).thenReturn(Boolean.FALSE);
        clienteDestinatario = null;

        Long result = dpeService.calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor);

        Assert.assertEquals(result, LongUtils.TEN);
    }

    // isSabado(int diaDaSemana)
    @Test
    public void shouldReturnTrueWhenDiaDaSemanaIsSabado() {
        Assert.assertTrue(dpeService.isSabado(DateTimeConstants.SATURDAY));

        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.MONDAY));
        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.TUESDAY));
        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.WEDNESDAY));
        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.THURSDAY));
        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.FRIDAY));
        Assert.assertFalse(dpeService.isSabado(DateTimeConstants.SUNDAY));
    }

    // isDomingo(int diaDaSemana)
    @Test
    public void shouldReturnTrueWhenDiaDaSemanaIsDomingo() {
        Assert.assertTrue(dpeService.isDomingo(DateTimeConstants.SUNDAY));

        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.MONDAY));
        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.TUESDAY));
        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.WEDNESDAY));
        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.THURSDAY));
        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.FRIDAY));
        Assert.assertFalse(dpeService.isDomingo(DateTimeConstants.SATURDAY));
    }

    // isFeriado(YearMonthDay data, Boolean ignoraFeriado, Set<String> feriados)
    @Test
    public void shouldReturnTrueWhenIgnoraFeriadoIsFalse() {
        YearMonthDay data = new YearMonthDay(2019, 1, 1);
        Set<String> feriados = new HashSet<String>();
        feriados.add("01-01");

        Assert.assertTrue(dpeService.isFeriado(data, Boolean.FALSE, feriados));
        Assert.assertTrue(dpeService.isFeriado(data, null, feriados));

        Assert.assertFalse(dpeService.isFeriado(data, Boolean.TRUE, feriados));

        data = new YearMonthDay(2019, 1, 2);
        Assert.assertFalse(dpeService.isFeriado(data, Boolean.TRUE, feriados));
        Assert.assertFalse(dpeService.isFeriado(data, Boolean.FALSE, feriados));
        Assert.assertFalse(dpeService.isFeriado(data, null, feriados));
    }

    // isFeriado(YearMonthDay data, Set<String> feriados)
    @Test
    public void shouldReturnTrueWhenIsFeriado() {
        YearMonthDay data = new YearMonthDay(2019, 1, 1);
        Set<String> feriados = new HashSet<String>();
        feriados.add("01-01");

        Assert.assertTrue(dpeService.isFeriado(data, feriados));

        data = new YearMonthDay(2019, 1, 2);
        Assert.assertFalse(dpeService.isFeriado(data, feriados));
    }

}
