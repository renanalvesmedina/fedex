package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FeriadoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.HorarioCorteCliente;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;
import com.mercurio.lms.vendas.model.RemetenteOTD;
import com.mercurio.lms.vendas.model.service.HorarioCorteClienteService;
import com.mercurio.lms.vendas.model.service.PrazoEntregaClienteService;
import com.mercurio.lms.vendas.model.service.RemetenteOTDService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

/**
 * Data de Previsão de Entrega
 *
 * @author Éderson Frozi
 * @spring.bean id="lms.expedicao.dpeService"
 */
public class DpeService {

    public static final String DT_PRAZO_ENTREGA = "dtPrazoEntrega";
    public static final String NR_PRAZO = "nrPrazo";
    public static final String NR_DIAS_COLETA = "nrDiasColeta";
    public static final String NR_DIAS_ENTREGA = "nrDiasEntrega";
    public static final String NR_DIAS_TRANSFERENCIA = "nrDiasTransferencia";
    
    public static final DateTimeFormatter PATTERN_DATA = DateTimeFormat.forPattern("dd-MM");

    private PpeService ppeService;
    private MunicipioService municipioService;
    private PrazoEntregaClienteService prazoEntregaClienteService;
    private FeriadoService feriadoService;
    private EnderecoPessoaService enderecoPessoaService;
    private HorarioCorteClienteService horarioCorteClienteService;
    private FilialService filialService;
    private RemetenteOTDService remetenteOTDService;

    public Map<String, Object> executeCalculoDPE(
            final Cliente clienteRemetente,
            final Cliente clienteDestinatario,
            final Cliente clienteDevedor,
            final Cliente clienteConsignatario,
            final Cliente clienteRedespacho,
            final Long idPedidoColeta,
            final Long idServico,
            final Long idMunicipioOrigem,
            final Long idFilialOrigem,
            final Long idFilialDestino,
            final Long idMunicipioDestino,
            final String nrCepOrigem,
            final String nrCepDestino,
            final DateTime dhEmissaoConhecimento
    ) {
        final Boolean ignoraFeriados = clienteRemetente.getBlDpeFeriado(); // LMSA-3959
        final Map<String, Object> dadosEnderecoDestino = municipioService.findZonaPaisUFByIdMunicipio(idMunicipioDestino);

        final Filial filialOrigem = filialService.findById(idFilialOrigem);

        final RestricaoRota restricaoRotaOrigem = getRestricaoRotaOrigem(idMunicipioOrigem, idServico, nrCepOrigem, idFilialOrigem);
        final RestricaoRota restricaoRotaDestino = getRestricaoRotaDestino(dadosEnderecoDestino, idMunicipioDestino, idFilialDestino);

        final PrazoEntregaCliente prazoEntregaCliente = findPrazoEspecial(clienteRemetente, clienteDevedor, idServico, restricaoRotaOrigem, restricaoRotaDestino, clienteDestinatario);
        final Map<String, Object> prazos = findPrazosInicialDeEntrega(prazoEntregaCliente, idMunicipioOrigem, idMunicipioDestino, idServico, clienteDevedor, nrCepOrigem, nrCepDestino);

        this.calcularPrazosDeEntrega(prazos, prazoEntregaCliente, clienteRemetente, clienteDestinatario, clienteConsignatario, clienteDevedor, dhEmissaoConhecimento, filialOrigem, idMunicipioOrigem, ignoraFeriados, idServico, restricaoRotaOrigem, dadosEnderecoDestino, idFilialOrigem, idFilialDestino, idMunicipioDestino);

        return prazos;
    }

    private void calcularPrazosDeEntrega(Map<String, Object> prazos, PrazoEntregaCliente prazoEntregaCliente, Cliente clienteRemetente, Cliente clienteDestinatario, Cliente clienteConsignatario, Cliente clienteDevedor, DateTime dhEmissaoConhecimento, Filial filialOrigem, Long idMunicipioOrigem, Boolean ignoraFeriados, Long idServico, RestricaoRota restricaoRotaOrigem, Map<String, Object> dadosEnderecoDestino, Long idFilialOrigem, Long idFilialDestino, Long idMunicipioDestino) {
        final Long idMunicipioFilialDestino = enderecoPessoaService.findIdMunicipioByIdPessoa(idFilialDestino);
        final Set<String> feriadosOrigem = getFeriadosByFilialAndMunicipio(filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio(), idMunicipioOrigem);
        final Set<String> feriadosDestino = getFeriadosByFilialAndMunicipio(idMunicipioFilialDestino, idMunicipioDestino);
        final Map<String, Object> dtPrazoEntregaInicial = getDtPrazoEntregaInicial(dhEmissaoConhecimento);
        final YearMonthDay dtPrazoEntregaOriginal = (YearMonthDay) dtPrazoEntregaInicial.get("dtPrazoEntrega");
        final Long nrPrazoOriginal = (Long) prazos.get(NR_PRAZO);

        YearMonthDay dtPrazoEntrega = dtPrazoEntregaOriginal;
        TimeOfDay hrEmissao = (TimeOfDay) dtPrazoEntregaInicial.get("hrEmissao");

        // Calcula quantos dias úteis deve adicionar ao DPE.
        Long nrPrazo = this.calcularDiasUteis(nrPrazoOriginal, clienteRemetente, clienteDestinatario, clienteConsignatario, clienteDevedor, dtPrazoEntrega, idServico, restricaoRotaOrigem, idMunicipioOrigem, dadosEnderecoDestino, idFilialOrigem, filialOrigem, hrEmissao);

        // Calcula o DPE com base no nrPrazo.
        Map<String, Object> retorno = this.getProximosDiasUteis(dtPrazoEntrega, nrPrazo, ignoraFeriados, feriadosOrigem, feriadosDestino, clienteDevedor.getBlEmissaoDiaNaoUtil(), clienteDevedor.getBlEmissaoSabado(), idMunicipioFilialDestino);

        prazos.put(DT_PRAZO_ENTREGA, retorno.get(DT_PRAZO_ENTREGA));
        prazos.put(NR_PRAZO, retorno.get(NR_PRAZO));

        if (prazoEntregaCliente != null) {
            ajustaPrazosColetaEntregaTransferencia(prazos);
        }
    }
    
    private Long calcularDiasUteis(final Long nrPrazoOriginal, Cliente clienteRemetente, Cliente clienteDestinatario, Cliente clienteConsignatario, Cliente clienteDevedor, YearMonthDay dtPrazoEntrega, Long idServico, RestricaoRota restricaoRotaOrigem, Long idMunicipioOrigem, Map<String, Object> dadosEnderecoDestino, Long idFilialOrigem, Filial filialOrigem, TimeOfDay hrEmissao) {
        // Adiciona dias úteis no prazo a partir da dificuldade
        final Long dificuldadeColetaEntregaCliente = getDificuldadeColetaEntregaCliente(clienteRemetente, clienteDestinatario, clienteConsignatario, clienteDevedor);

        // Adiciona dias no prazo a serem calculados pelo horário de corte do cliente
        final Long prazoEmDiasHorarioCorteCliente = findNrDiasAplicadosHorarioCorteCliente(dtPrazoEntrega, clienteRemetente, idServico, restricaoRotaOrigem, idMunicipioOrigem, dadosEnderecoDestino, idFilialOrigem, filialOrigem, hrEmissao);

        return (nrPrazoOriginal + prazoEmDiasHorarioCorteCliente + dificuldadeColetaEntregaCliente);
    }
  
    protected Map<String, Object> getProximosDiasUteis(final YearMonthDay dtEmissao, Long nrPrazo, Boolean remetenteIgnoraFeriados,
                                                       Set<String> feriadosOrigem, Set<String> feriadosDestino, Boolean devedorEmiteDiaNaoUtil,
                                                       Boolean devedorEmiteSabado, Long idMunicipioFilialDestino) {
        YearMonthDay dtPrazoEntrega = dtEmissao;
        boolean dtPrazoEntregaIsSabado = false;

        Set<String> feridadosNacionalMundialVigentes = getFeriadosNacionalMundialVigentes(idMunicipioFilialDestino);

        // CQPRO00029593 Se DATA_BASE (dtPrazoEntrega) for no sábado, domingo ou feriado, então DATA_BASE = DATA_BASE + 1.
        if (Boolean.FALSE.equals(devedorEmiteDiaNaoUtil)) {
            dtPrazoEntrega = getProximaDataUtilEmDocumentosEmitidosAoSabado(dtEmissao, feriadosOrigem, remetenteIgnoraFeriados, devedorEmiteSabado);

            if (isSabado(JTDateTimeUtils.getNroDiaSemana(dtPrazoEntrega))) {
                dtPrazoEntregaIsSabado = true;
            }
        }

        // Verifica se o prazo inicial foi alterado
        final boolean prazoInicialAlterado = !dtEmissao.equals(dtPrazoEntrega);

        //DESCONSIDERA TODOS OS FERIADOS DURANTE A VIAJEM
        for (int i = 0; i < nrPrazo; i++) {
            dtPrazoEntrega = getProximoDiaUtil(dtPrazoEntrega, feridadosNacionalMundialVigentes, remetenteIgnoraFeriados);
        }

        if(isFeriado(dtPrazoEntrega, feriadosDestino)) {
            dtPrazoEntrega = getProximoDiaUtil(dtPrazoEntrega, feriadosDestino, remetenteIgnoraFeriados);
        }

        /**
         * Após gerar a data Final de Entrega Verifica se deve alterar o numero
         * de dias da previsão de entrega, adicionando +1
         *
         * @author Eri Eliseu
         */
        if (prazoInicialAlterado && !dtPrazoEntregaIsSabado) {
            nrPrazo++;
        }

        /* Caso a data atual seja maior que data calculada do DPE, utiliza a data atual, se for dia útil (senão pega o próximo dia útil) */
        if (dtPrazoEntrega.compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
            dtPrazoEntrega = this.getProximoDiaUtil(dtPrazoEntrega, feriadosDestino, remetenteIgnoraFeriados);
        }

        Map<String, Object> retorno = new HashMap<String, Object>();

        retorno.put(DT_PRAZO_ENTREGA, dtPrazoEntrega);
        retorno.put(NR_PRAZO, nrPrazo);

        return retorno;
    }

    protected int findQtdeDiasUteisEntreDatas(final YearMonthDay dtEmissao, final YearMonthDay dtEntrega,
            final Boolean remetenteIgnoraFeriados, final Set<String> feriadosMunicipioEntrega, final Set<String> MunicipioFilialDestino,
            final Boolean devedorEmiteDiaNaoUtil, final Boolean devedorEmiteSabado) {
        YearMonthDay dtAtual = dtEmissao;
        int dias = 0;

        // CQPRO00029593 Se DATA_BASE (dtPrazoEntrega) for no sábado, domingo ou feriado, então DATA_BASE = DATA_BASE + 1.
        if (Boolean.TRUE.equals(devedorEmiteDiaNaoUtil)) {
            dtAtual = getProximaDataUtilEmDocumentosEmitidosAoSabado(dtAtual, feriadosMunicipioEntrega, remetenteIgnoraFeriados, devedorEmiteSabado);

            boolean dtPrazoEntregaIsSabado = isSabado(JTDateTimeUtils.getNroDiaSemana(dtAtual));
            boolean prazoInicialAlterado = !dtEmissao.equals(dtAtual);

            if (prazoInicialAlterado && !dtPrazoEntregaIsSabado) {
                dias++;
            }
        }

        while (dtAtual.compareTo(dtEntrega) < 0) {
            dtAtual = getProximoDiaUtil(dtAtual, MunicipioFilialDestino, true);
            dias++;
        }

        return dias;
    }

    private Map<String, Object> getDtPrazoEntregaInicial(DateTime dhEmissaoConhecimento) {
        Map<String, Object> retorno = new HashMap<String, Object>();

        YearMonthDay dtPrazoEntrega;
        TimeOfDay hrEmissao;

        if (dhEmissaoConhecimento == null) {
            dtPrazoEntrega = JTDateTimeUtils.getDataAtual();
            hrEmissao = dtPrazoEntrega.toDateTimeAtCurrentTime().toTimeOfDay();
        } else {
            dtPrazoEntrega = dhEmissaoConhecimento.toYearMonthDay();
            hrEmissao = dhEmissaoConhecimento.toTimeOfDay();
        }

        retorno.put("dtPrazoEntrega", dtPrazoEntrega);
        retorno.put("hrEmissao", hrEmissao);

        return retorno;
    }

    private Long findNrDiasAplicadosHorarioCorteCliente(YearMonthDay dtPrazoEntrega, Cliente clienteRemetente, Long idServico, RestricaoRota restricaoRotaOrigem, Long idMunicipioOrigem, Map<String, Object> dadosEnderecoDestino, Long idFilialOrigem, Filial filialOrigem, TimeOfDay hrEmissao) {
        if (dtPrazoEntrega == null) {
            return LongUtils.ZERO;
        }

        List<HorarioCorteCliente> horariosCorteCliente = horarioCorteClienteService.findHorarioCorteColeta(clienteRemetente.getIdCliente(), idServico, restricaoRotaOrigem);
        HorarioCorteCliente horarioCorteCliente = filtrarHorarioDeCorteDoCliente(horariosCorteCliente, idMunicipioOrigem, dadosEnderecoDestino, idFilialOrigem, filialOrigem);

        Long prazo = LongUtils.ZERO;

        if (horarioCorteCliente == null) {
            int hrCorte = filialService.validateHorarioCorteFilial(SessionUtils.getFilialSessao(), null);

            if (hrCorte > 0) {
                prazo = 1L;
            }
            // Se Horário da Coleta estiver dentro do Horario de Corte, considera também as horas aplicadas
        } else if ((horarioCorteCliente.getNrHorasAplicadas() != null) && ((CompareUtils.gt(horarioCorteCliente.getHrInicial(), horarioCorteCliente.getHrFinal()) && (CompareUtils.gt(hrEmissao, horarioCorteCliente.getHrInicial()) || CompareUtils.lt(hrEmissao, horarioCorteCliente.getHrFinal()))) || (CompareUtils.gt(horarioCorteCliente.getHrFinal(), horarioCorteCliente.getHrInicial()) && (CompareUtils.gt(hrEmissao, horarioCorteCliente.getHrInicial()) && CompareUtils.lt(hrEmissao, horarioCorteCliente.getHrFinal()))))) {
            prazo = JTDateTimeUtils.getDaysFromHours(horarioCorteCliente.getNrHorasAplicadas());
        }

        return prazo;
    }

    private RestricaoRota getRestricaoRotaDestino(Map<String, Object> dadosEnderecoDestino, final Long idMunicipioDestino, final Long idFilialDestino) {
        Long idZonaDestino = (Long) dadosEnderecoDestino.get("idZona");
        Long idPaisDestino = (Long) dadosEnderecoDestino.get("idPais");
        Long idUnidadeFederativaDestino = (Long) dadosEnderecoDestino.get("idUnidadeFederativa");
        Long idTipoLocalizacaoDestino = (Long) dadosEnderecoDestino.get("idTipoLocalizacaoMunicipio");

        dadosEnderecoDestino.put("idMunicipioDestino", idMunicipioDestino);
        dadosEnderecoDestino.put("idFilialDestino", idFilialDestino);

        RestricaoRota restricaoRotaDestino = RotaPrecoUtils.getRestricaoRota(idZonaDestino, idPaisDestino, idUnidadeFederativaDestino, idFilialDestino, idMunicipioDestino, idTipoLocalizacaoDestino, null);

        return restricaoRotaDestino;
    }

    private RestricaoRota getRestricaoRotaOrigem(final Long idMunicipioOrigem, final Long idServico, final String nrCepOrigem, final Long idFilialOrigem) {
        Map<String, Object> dadosEndereco = municipioService.findZonaPaisUFByIdMunicipio(idMunicipioOrigem);
        Map<String, Object> dadosTipoLocalizacao = ppeService.findAtendimentoMunicipio(idMunicipioOrigem, idServico, Boolean.TRUE, null, nrCepOrigem, null, null, null, null, null, null, null, null);

        Long idZonaOrigem = (Long) dadosEndereco.get("idZona");
        Long idPaisOrigem = (Long) dadosEndereco.get("idPais");
        Long idUnidadeFederativaOrigem = (Long) dadosEndereco.get("idUnidadeFederativa");
        Long idTipoLocalizacaoOrigem = (Long) dadosTipoLocalizacao.get("idTipoLocalizacaoMunicipio");

        RestricaoRota restricaoRotaOrigem = RotaPrecoUtils.getRestricaoRota(idZonaOrigem, idPaisOrigem, idUnidadeFederativaOrigem, idFilialOrigem, idMunicipioOrigem, idTipoLocalizacaoOrigem, null);

        return restricaoRotaOrigem;
    }

    private Map<String, Object> findPrazosInicialDeEntrega(PrazoEntregaCliente prazoEntregaCliente, final Long idMunicipioOrigem, final Long idMunicipioDestino, final Long idServico, final Cliente clienteDevedor, final String nrCepOrigem, final String nrCepDestino) {
        Map<String, Object> prazos = new HashMap<String, Object>();

        if (prazoEntregaCliente == null) {
            TypedFlatMap prazosPPE = new TypedFlatMap(ppeService.executeCalculoPPE(idMunicipioOrigem, idMunicipioDestino, idServico, clienteDevedor.getIdCliente(), nrCepOrigem, nrCepDestino, null, null, "N"));

            prazos.put(NR_PRAZO, JTDateTimeUtils.getDaysFromHours(prazosPPE.getLong(PpeService.NR_PRAZO)));
            prazos.put(NR_DIAS_COLETA, JTDateTimeUtils.getDaysFromHours(prazosPPE.getLong(PpeService.NR_TEMPO_COLETA)));
            prazos.put(NR_DIAS_ENTREGA, JTDateTimeUtils.getDaysFromHours(prazosPPE.getLong(PpeService.NR_TEMPO_ENTREGA)));
            prazos.put(NR_DIAS_TRANSFERENCIA, JTDateTimeUtils.getDaysFromHours(prazosPPE.getLong(PpeService.NR_TEMPO_TRANSFERENCIA)));

            return prazos;
        }

        prazos.put(NR_PRAZO, JTDateTimeUtils.getDaysFromHours(prazoEntregaCliente.getNrPrazo()));
        prazos.put(NR_DIAS_COLETA, LongUtils.ZERO);
        prazos.put(NR_DIAS_ENTREGA, LongUtils.ZERO);
        prazos.put(NR_DIAS_TRANSFERENCIA, LongUtils.ZERO);

        return prazos;
    }

    private Long getDificuldadeColetaEntregaCliente(Cliente clienteRemetente, Cliente clienteDestinatario, Cliente clienteConsignatario, Cliente clienteDevedor) {
        Long dificuldadeColeta = LongUtils.getOrZero(getDificuldadeCliente(clienteRemetente.getTpDificuldadeColeta()));
        Long dificuldadeClassificacao = LongUtils.getOrZero(getDificuldadeCliente(clienteRemetente.getTpDificuldadeClassificacao()));
        Long dificuldadeEntrega = LongUtils.getOrZero(calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor));

        Long dificuldadeColetaEntrega = dificuldadeColeta + dificuldadeClassificacao + dificuldadeEntrega;

        return dificuldadeColetaEntrega;
    }

    private HorarioCorteCliente filtrarHorarioDeCorteDoCliente(List<HorarioCorteCliente> horariosCorteCliente, final Long idMunicipioOrigem, Map<String, Object> dadosEnderecoDestino, final Long idFilialOrigem, final Filial filialOrigem) {
        HorarioCorteCliente horarioCorteCliente = filtraMunicipioOrigem(idMunicipioOrigem, dadosEnderecoDestino, horariosCorteCliente, null);

        if (horarioCorteCliente == null) {
            horarioCorteCliente = filtraFilialOrigem(idFilialOrigem, dadosEnderecoDestino, horariosCorteCliente, horarioCorteCliente);
        }

        if (horarioCorteCliente == null) {
            Long idUnidadeFederativa = filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
            horarioCorteCliente = filtraUFOrigem(idUnidadeFederativa, dadosEnderecoDestino, horariosCorteCliente, horarioCorteCliente);
        }

        if (horarioCorteCliente == null) {
            Long idPais = filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais();
            horarioCorteCliente = filtraPaisOrigem(idPais, dadosEnderecoDestino, horariosCorteCliente, horarioCorteCliente);
        }

        if (horarioCorteCliente == null) {
            Long idZona = filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getZona().getIdZona();
            horarioCorteCliente = filtraZonaOrigem(idZona, dadosEnderecoDestino, horariosCorteCliente, horarioCorteCliente);
        }

        if (horarioCorteCliente == null) {
            horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, horariosCorteCliente);
        }

        return horarioCorteCliente;
    }

    private void ajustaPrazosColetaEntregaTransferencia(Map<String, Object> retorno) {
        Long nrPrazoCliente = (Long) retorno.get(NR_PRAZO);
        Long nrDiasColeta = (Long) retorno.get(NR_DIAS_COLETA);
        Long nrDiasEntrega = (Long) retorno.get(NR_DIAS_ENTREGA);
        Long nrDiasTransferencia = (Long) retorno.get(NR_DIAS_TRANSFERENCIA);

        if (nrDiasEntrega >= nrPrazoCliente) {
            nrDiasEntrega = nrPrazoCliente;
            nrDiasTransferencia = 0L;
            nrDiasColeta = 0L;
        } else {
            if (nrPrazoCliente < (nrDiasEntrega + nrDiasColeta)) {
                nrDiasColeta = (nrPrazoCliente - nrDiasEntrega);
                nrDiasTransferencia = 0L;
            } else {
                nrDiasTransferencia = (nrPrazoCliente - nrDiasColeta - nrDiasEntrega);
            }
        }

        retorno.put(NR_DIAS_COLETA, nrDiasColeta);
        retorno.put(NR_DIAS_ENTREGA, nrDiasEntrega);
        retorno.put(NR_DIAS_TRANSFERENCIA, nrDiasTransferencia);
    }

    private PrazoEntregaCliente findPrazoEspecial(Cliente clienteRemetente, Cliente clienteDevedor, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino, Cliente clienteDestinatario) {
        PrazoEntregaCliente prazoEntregaRemetente = prazoEntregaClienteService.findPrazoEntregaCliente(clienteRemetente.getIdCliente(), idServico, restricaoRotaOrigem, restricaoRotaDestino);

        if (prazoEntregaRemetente != null) {
            return prazoEntregaRemetente;
        }

        PrazoEntregaCliente prazoEntregaDevedor = prazoEntregaClienteService.findPrazoEntregaCliente(clienteDevedor.getIdCliente(), idServico, restricaoRotaOrigem, restricaoRotaDestino);

        if (clienteRemetente.getIdCliente().equals(clienteDevedor.getIdCliente()) || clienteDestinatario.getIdCliente().equals(clienteDevedor.getIdCliente())) {
            return prazoEntregaDevedor;
        }

        return calculaPrazoEspecialTomador(clienteDevedor, prazoEntregaDevedor, prazoEntregaRemetente, clienteRemetente);
    }

    private PrazoEntregaCliente calculaPrazoEspecialTomador(Cliente clienteDevedor, PrazoEntregaCliente prazoEntregaDevedor, PrazoEntregaCliente prazoEntregaRemetente, Cliente clienteRemetente) {
        Boolean prazoEspecialClientesSelecionados = clienteDevedor.getBlRemetenteOTD();

        if (Boolean.TRUE.equals(prazoEspecialClientesSelecionados)) {
            return prazoParaClientesSelecionados(clienteDevedor, clienteRemetente, prazoEntregaRemetente, prazoEntregaDevedor);
        }

        return prazoEntregaDevedor;
    }

    private PrazoEntregaCliente prazoParaClientesSelecionados(Cliente clienteDevedor, Cliente clienteRemetente, PrazoEntregaCliente prazoEntregaRemetente, PrazoEntregaCliente prazoEntregaDevedor) {
        List<RemetenteOTD> remetentesOTD = remetenteOTDService.findByClienteRemetente(clienteDevedor.getIdCliente(), clienteRemetente.getIdCliente());

        if (remetentesOTD.isEmpty()) {
            return prazoEntregaRemetente;
        }

        return prazoEntregaDevedor;
    }

    protected Long calcAdicionalDificuldadeEntrega(final Cliente clienteDestinatario, final Cliente clienteConsignatario, final Cliente clienteDevedor) {
        if (clienteDevedor != null && clienteDevedor.getBldesconsiderarDificuldade()) {
            return LongUtils.ZERO;
        }

        if (clienteConsignatario != null) {
            return getDificuldadeCliente(clienteConsignatario.getTpDificuldadeEntrega());
        }

        if (clienteDestinatario != null) {
            return getDificuldadeCliente(clienteDestinatario.getTpDificuldadeEntrega());
        }

        return LongUtils.ZERO;
    }

    private HorarioCorteCliente filtraZonaOrigem(Long idZona, Map<String, Object> dadosEnderecoDestino, List<HorarioCorteCliente> horariosCorteCliente, HorarioCorteCliente horarioCorteCliente) {
        List<HorarioCorteCliente> filtro = new ArrayList<HorarioCorteCliente>();

        for (HorarioCorteCliente hcc : horariosCorteCliente) {
            if (hcc.getZonaOrigem() != null && hcc.getZonaOrigem().equals(idZona)) {
                filtro.add(hcc);
            }
        }

        horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, filtro);

        return horarioCorteCliente;
    }

    private HorarioCorteCliente filtraPaisOrigem(Long idPais, Map<String, Object> dadosEnderecoDestino, List<HorarioCorteCliente> horariosCorteCliente, HorarioCorteCliente horarioCorteCliente) {
        List<HorarioCorteCliente> filtro = new ArrayList<HorarioCorteCliente>();

        for (HorarioCorteCliente hcc : horariosCorteCliente) {
            if (hcc.getPaisOrigem() != null && hcc.getPaisOrigem().getIdPais().equals(idPais)) {
                filtro.add(hcc);
            }
        }

        horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, filtro);

        return horarioCorteCliente;
    }

    private HorarioCorteCliente filtraUFOrigem(Long idUFOrigem, Map<String, Object> dadosEnderecoDestino, List<HorarioCorteCliente> horariosCorteCliente, HorarioCorteCliente horarioCorteCliente) {
        List<HorarioCorteCliente> filtro = new ArrayList<HorarioCorteCliente>();

        for (HorarioCorteCliente hcc : horariosCorteCliente) {
            if (hcc.getUnidadeFederativaOrigem() != null && hcc.getUnidadeFederativaOrigem().getIdUnidadeFederativa().equals(idUFOrigem)) {
                filtro.add(hcc);
            }
        }

        horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, filtro);

        return horarioCorteCliente;
    }

    private HorarioCorteCliente filtraFilialOrigem(Long idFilialOrigem, Map<String, Object> dadosEnderecoDestino, List<HorarioCorteCliente> horariosCorteCliente, HorarioCorteCliente horarioCorteCliente) {
        List<HorarioCorteCliente> filtro = new ArrayList<HorarioCorteCliente>();

        for (HorarioCorteCliente hcc : horariosCorteCliente) {
            if (hcc.getFilialOrigem() != null && hcc.getFilialOrigem().getIdFilial().equals(idFilialOrigem)) {
                filtro.add(hcc);
            }
        }

        horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, filtro);

        return horarioCorteCliente;
    }

    private HorarioCorteCliente filtraDestino(Map<String, Object> dadosEnderecoDestino, HorarioCorteCliente horarioCorteCliente, List<HorarioCorteCliente> filtro) {
        Long idZonaDestino = (Long) dadosEnderecoDestino.get("idZona");
        Long idPaisDestino = (Long) dadosEnderecoDestino.get("idPais");
        Long idUnidadeFederativaDestino = (Long) dadosEnderecoDestino.get("idUnidadeFederativa");
        Long idMunicipioDestino = (Long) dadosEnderecoDestino.get("idMunicipioDestino");
        Long idFilialDestino = (Long) dadosEnderecoDestino.get("idFilialDestino");

        if (filtro.isEmpty()) {
            return horarioCorteCliente;
        }

        List<HorarioCorteCliente> retirar = new ArrayList<HorarioCorteCliente>();
        for (HorarioCorteCliente hcc : filtro) {
            if (hcc.getMunicipioDestino() != null && !hcc.getMunicipioDestino().getIdMunicipio().equals(idMunicipioDestino)) {
                retirar.add(hcc);
            }
        }

        if (retirar.size() > 0) {
            filtro.removeAll(retirar);
        }

        for (HorarioCorteCliente hcc : filtro) {
            if (hcc.getFilialDestino() != null && !hcc.getFilialDestino().getIdFilial().equals(idFilialDestino)) {
                retirar.add(hcc);
            }
        }

        if (retirar.size() > 0) {
            filtro.removeAll(retirar);
        }
        for (HorarioCorteCliente hcc : filtro) {
            if (hcc.getUnidadeFederativaDestino() != null && !hcc.getUnidadeFederativaDestino().getIdUnidadeFederativa().equals(idUnidadeFederativaDestino)) {
                retirar.add(hcc);
            }
        }

        if (retirar.size() > 0) {
            filtro.removeAll(retirar);
        }

        for (HorarioCorteCliente hcc : filtro) {
            if (hcc.getPaisDestino() != null && !hcc.getPaisDestino().getIdPais().equals(idPaisDestino)) {
                retirar.add(hcc);
            }
        }

        if (retirar.size() > 0) {
            filtro.removeAll(retirar);
        }

        for (HorarioCorteCliente hcc : filtro) {
            if (hcc.getZonaDestino() != null && !hcc.getZonaDestino().getIdZona().equals(idZonaDestino)) {
                retirar.add(hcc);
            }
        }

        if (filtro.size() > 1) {
            TimeOfDay maior = filtro.get(0).getHrFinal();
            for (HorarioCorteCliente hcc : filtro) {
                if (hcc.getHrFinal().isAfter(maior)) {
                    horarioCorteCliente = hcc;
                    maior = hcc.getHrFinal();
                }
            }
        }

        if (filtro.size() == 1) {
            horarioCorteCliente = filtro.get(0);
        }

        return horarioCorteCliente;
    }

    private HorarioCorteCliente filtraMunicipioOrigem(final Long idMunicipioOrigem, final Map<String, Object> dadosEnderecoDestino, List<HorarioCorteCliente> horariosCorteCliente, HorarioCorteCliente horarioCorteCliente) {
        List<HorarioCorteCliente> filtro = new ArrayList<HorarioCorteCliente>();

        for (HorarioCorteCliente hcc : horariosCorteCliente) {
            if (hcc.getMunicipioOrigem() != null && hcc.getMunicipioOrigem().getIdMunicipio().equals(idMunicipioOrigem)) {
                filtro.add(hcc);
            }
        }

        horarioCorteCliente = filtraDestino(dadosEnderecoDestino, horarioCorteCliente, filtro);

        return horarioCorteCliente;
    }

    protected YearMonthDay getProximosDiasUteis(final YearMonthDay dataInicio, int diasUteis, Set<String> feriados, Boolean ignoraFeriados) {
        YearMonthDay dataAtual = dataInicio;

        for (int i = 0; i < diasUteis; i++) {
            dataAtual = getProximoDiaUtil(dataAtual, feriados, ignoraFeriados);
        }

        return dataAtual;
    }

    protected YearMonthDay getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados, Boolean ignoraFeriados) {
        if (Boolean.TRUE.equals(ignoraFeriados)) {
            return getProximoDiaUtil(dataInicio);
        }

        return getProximoDiaUtil(dataInicio, feriados);
    }

    protected YearMonthDay getProximoDiaUtil(final YearMonthDay dataInicio, Set<String> feriados) {
        YearMonthDay dataAtual = getProximoDiaUtil(dataInicio);

        while (isFeriado(dataAtual, feriados)) {
            dataAtual = getProximoDiaUtil(dataAtual);
        }

        return dataAtual;
    }

    protected YearMonthDay getProximoDiaUtil(final YearMonthDay dataInicio) {
        YearMonthDay dataAtual = dataInicio.plusDays(1);
        int diaDaSemana = JTDateTimeUtils.getNroDiaSemana(dataAtual);

        if (isSabado(diaDaSemana)) {
            return dataAtual.plusDays(2);
        }

        if (isDomingo(diaDaSemana)) {
            return dataAtual.plusDays(1);
        }

        return dataAtual;
    }

    protected YearMonthDay getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor) {
        return getProximaDataUtilEmDocumentosEmitidosAoSabado(data, feriados, ignoraFeriados, clienteDevedor.getBlEmissaoSabado());
    }

    protected YearMonthDay getProximaDataUtilEmDocumentosEmitidosAoSabado(YearMonthDay data, Set<String> feriados, Boolean ignoraFeriados, Boolean clienteDevedorEmiteSabado) {
        boolean emiteNoSabado = Boolean.TRUE.equals(clienteDevedorEmiteSabado);

        while (true) {
            int diaSemana = JTDateTimeUtils.getNroDiaSemana(data);

            if (isSabado(diaSemana)) {
                if (emiteNoSabado) {
                    return data;
                }

                data = data.plusDays(2);
                continue;
            }

            if (isDomingo(diaSemana)) {
                data = data.plusDays(1);
                continue;
            }

            if (isFeriado(data, ignoraFeriados, feriados)) {
                data = data.plusDays(1);
                continue;
            }

            return data;
        }
    }

    protected boolean isSabado(int diaDaSemana) {
        return diaDaSemana == DateTimeConstants.SATURDAY;
    }

    protected boolean isDomingo(int diaDaSemana) {
        return diaDaSemana == DateTimeConstants.SUNDAY;
    }
    
    protected boolean isFinalDeSemana(YearMonthDay data) {
        return isFinalDeSemana(JTDateTimeUtils.getNroDiaSemana(data));
    }
    
    protected boolean isFinalDeSemana(int diaDaSemana) {
        return isSabado(diaDaSemana) || isDomingo(diaDaSemana);
    }

    protected boolean isFeriado(YearMonthDay data, Boolean ignoraFeriado, Set<String> feriados) {
        if (Boolean.TRUE.equals(ignoraFeriado)) {
            return false;
        }

        return isFeriado(data, feriados);
    }

    protected boolean isFeriado(YearMonthDay data, Set<String> feriados) {
        if (CollectionUtils.isEmpty(feriados)) {
            return false;
        }
        
        return feriados.contains(data.toString(PATTERN_DATA));
    }
    
    protected List<YearMonthDay> findDatasSemFinaisDeSemana(YearMonthDay dtInicio, YearMonthDay dtFim) {
        YearMonthDay dtAtual = dtInicio;
        List<YearMonthDay> datas = new ArrayList<YearMonthDay>();

        while (dtAtual.compareTo(dtFim) <= 0) {
            if (!isFinalDeSemana(dtAtual)) {
                datas.add(dtAtual);
            }
            
            dtAtual = dtAtual.plusDays(1);
        }
        
        return datas;
    }

    private Set<String> getFeriadosByFilialAndMunicipio(Long idMunicipioFilialDestino, Long idMunicipioDestino) {
        Set<String> retorno = new HashSet<String>();
        List feriadosVigentesMunicipioDestino = feriadoService.findAllDtFeriadosVigentesByIdMunicipio(idMunicipioDestino);
        retorno.addAll(feriadosVigentesMunicipioDestino);

        if (!idMunicipioFilialDestino.equals(idMunicipioDestino)) {
            List feriadosVigentesMunicipioFilialDestino = feriadoService.findAllDtFeriadosVigentesByIdMunicipio(idMunicipioFilialDestino);
            retorno.addAll(feriadosVigentesMunicipioFilialDestino);
        }

        return retorno;
    }

    private Long getDificuldadeCliente(DomainValue tpDificuldade) {
        if (tpDificuldade == null) {
            return LongUtils.ZERO;
        }

        String vlDificuldade = tpDificuldade.getValue();

        if (StringUtils.isNumeric(vlDificuldade) && StringUtils.isNotBlank(vlDificuldade)) {
            return LongUtils.getLong(vlDificuldade);
        }

        return LongUtils.ZERO;
    }

    private Set<String> getFeriadosNacionalMundialVigentes(Long idMunicipio) {
        Set<String> retorno = new HashSet<String>();
        retorno.addAll(feriadoService.findDtFeriadosNacionaisEMundialByIdMunicipio(idMunicipio));

        return retorno;
    }

    public void setPpeService(final PpeService ppeService) {
        this.ppeService = ppeService;
    }

    public void setMunicipioService(final MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    public void setPrazoEntregaClienteService(final PrazoEntregaClienteService prazoEntregaClienteService) {
        this.prazoEntregaClienteService = prazoEntregaClienteService;
    }

    public void setFeriadoService(final FeriadoService feriadoService) {
        this.feriadoService = feriadoService;
    }

    public void setEnderecoPessoaService(final EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    public void setHorarioCorteClienteService(final HorarioCorteClienteService horarioCorteClienteService) {
        this.horarioCorteClienteService = horarioCorteClienteService;
    }

    public void setFilialService(final FilialService filialService) {
        this.filialService = filialService;
    }

    public RemetenteOTDService getRemetenteOTDService() {
        return remetenteOTDService;
    }

    public void setRemetenteOTDService(RemetenteOTDService remetenteOTDService) {
        this.remetenteOTDService = remetenteOTDService;
    }

}
