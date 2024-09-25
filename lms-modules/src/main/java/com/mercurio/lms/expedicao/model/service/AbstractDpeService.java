package com.mercurio.lms.expedicao.model.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FeriadoService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;

/**
 * Data de Previsão de Entrega
 *
 * @author Tiago Rosa
 *
 */
public abstract class AbstractDpeService {

    /**
     * Retorna a data prevista de entrega
     *
     * @param idCliente
     * @param idServico
     * @param idMunicipioOrigem
     * @param idMunicipioDestino
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param nrCepOrigem
     * @param nrCepDestino
     * @return Data prevista de entrega
     */
    public final Map<String, Object> executeCalculoDPE(
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

        //LMSA-3959
        Boolean ignoraFeriados = clienteRemetente.getBlDpeFeriado();

        // Busca na tabela PRAZO_ENTREGA_CLIENTE o prazo a mais ou menos a ser aplicado, conforme a rota.
        // Monta as restricoes para Origem
        RestricaoRota restricaoRotaOrigem = restricoesRotaOrigem(idMunicipioOrigem, idServico, nrCepOrigem, idFilialOrigem);

        // Monta as restricoes para Destino
        Map<String, Object> dadosEnderecoDestino = getMunicipioService().findZonaPaisUFByIdMunicipio(idMunicipioDestino);
        dadosEnderecoDestino.put("idMunicipioDestino", idMunicipioDestino);
        dadosEnderecoDestino.put("idFilialDestino", idFilialDestino);
        RestricaoRota restricaoRotaDestino = restricoesDestino(dadosEnderecoDestino, idMunicipioDestino, idServico, idFilialDestino);

        PrazoEntregaCliente prazoEntregaCliente = calculaPrazoEspecial(
                clienteRemetente, clienteDevedor, idServico,
                restricaoRotaOrigem, restricaoRotaDestino, clienteDestinatario);

        Long nrDiasColeta = 0L;
        Long nrDiasColetaDificuldade = 0L;
        Long nrDiasEntrega = 0L;
        Long nrDiasEntregaDificuldade = 0L;
        Long nrDiasTransferencia = 0L;

        Long nrPrazo;
        // Se encontrou prazoEntregaCliente utilizar este prazo. Caso contrario, chamar a rotina que calcula o PPE.
        if (prazoEntregaCliente != null) {

            nrPrazo = prazoEntregaCliente.getNrPrazo();

        } else {

            Map<String, Object> prazos = new HashMap<String, Object>();
            prazos = getPpeService().executeCalculoPPE(
                    idMunicipioOrigem,
                    idMunicipioDestino,
                    idServico,
                    clienteDevedor.getIdCliente(),
                    nrCepOrigem,
                    nrCepDestino,
                    null,
                    null,
                    "N");

            nrPrazo = (Long) prazos.get("nrPrazo");

            nrDiasColeta = Long.valueOf(JTDateTimeUtils.getDaysFromHours(((Long) prazos.get("nrTempoColeta")).longValue()));
            nrDiasEntrega = Long.valueOf(JTDateTimeUtils.getDaysFromHours(((Long) prazos.get("nrTempoEntrega")).longValue()));
            nrDiasTransferencia = Long.valueOf(JTDateTimeUtils.getDaysFromHours(((Long) prazos.get("nrTempoTransferencia")).longValue()));

        }

        // Converte o prazo em dias
        nrPrazo = Long.valueOf(JTDateTimeUtils.getDaysFromHours(nrPrazo.longValue()));

        // Verifica dificuldades cadastradas para os clientes
        Long vlDificuldade = getDificuldadeCliente(clienteRemetente.getTpDificuldadeColeta());
        nrPrazo = LongUtils.add(vlDificuldade, nrPrazo);
        nrDiasColetaDificuldade = LongUtils.add(vlDificuldade, nrDiasColeta);

        vlDificuldade = getDificuldadeCliente(clienteRemetente.getTpDificuldadeClassificacao());
        nrPrazo = LongUtils.add(vlDificuldade, nrPrazo);
        nrDiasColetaDificuldade = LongUtils.add(vlDificuldade, nrDiasColetaDificuldade);

        /* Busca o adicional referente a dificuldade de entrega do cliente */
        vlDificuldade = calcAdicionalDificuldadeEntrega(clienteDestinatario, clienteConsignatario, clienteDevedor);
        nrPrazo = LongUtils.add(vlDificuldade, nrPrazo);
        nrDiasEntregaDificuldade = LongUtils.add(vlDificuldade, nrDiasEntrega);

        YearMonthDay dtPrazoEntrega;
        YearMonthDay dtPrazoReentrega = JTDateTimeUtils.getDataAtual();
        TimeOfDay hrEmissao;

        //a data deve ser a data de emissão do conhecimento, para estar nivelado com a data do servidor do robo
        if (dhEmissaoConhecimento == null) {
            dtPrazoEntrega = JTDateTimeUtils.getDataAtual();
            hrEmissao = dtPrazoEntrega.toDateTimeAtCurrentTime().toTimeOfDay();
        } else {
            dtPrazoEntrega = dhEmissaoConhecimento.toYearMonthDay();
            hrEmissao = dhEmissaoConhecimento.toTimeOfDay();
        }

        /*
        * CQPRO00029593 Se DATA_BASE (dtPrazoEntrega) for em sábado, domingo ou feriado fazer DATA_BASE = DATA_BASE + 1, testando novamente até que
        * DATA_BASE não caia em nenhum dos dias citados.
        */
        /* Busca conjunto com todos os feriados do munícipio do remetente e do municipio da filial de origem */
        Filial filialOrigem = findFilialOrigem(idFilialOrigem);
        Set<String> feriadosOrigem = findFeriadosFilialOrigem(filialOrigem, idMunicipioOrigem);

        /* Adiciona dias úteis de prazo a data inicial */
        final YearMonthDay dtPrazoEntregaOriginal = dtPrazoEntrega;

        boolean dtPrazoEntregaIsSabado = false;
        if (clienteDevedor != null && !clienteDevedor.getBlEmissaoDiaNaoUtil()) {
            dtPrazoEntrega = getNextDataUtil(dtPrazoEntrega, feriadosOrigem, ignoraFeriados, clienteDevedor);

            int diaSemana = JTDateTimeUtils.getNroDiaSemana(dtPrazoEntrega);
            if (diaSemana == DateTimeConstants.SATURDAY) {
                dtPrazoEntregaIsSabado = true;
            }
        }

        /**
         * Valida se houve alteração na data de saida da mercadoria na filial de
         * origem Caso tenha sido alterado por motivos de feriado ou final de
         * semana, adicionar
         */
        boolean prazoInicialAlterado = !dtPrazoEntregaOriginal.equals(dtPrazoEntrega);

        if (dtPrazoEntrega != null) {
            nrPrazo = addHorarioCorteCliente(clienteRemetente, idServico, restricaoRotaOrigem, idMunicipioOrigem, dadosEnderecoDestino, idFilialOrigem, filialOrigem, hrEmissao, nrPrazo);
        }
        // Pega o municipio da Filial de destino
        final Map<String, Object> map = getEnderecoPessoaService().findMunicipioUfByIdPessoa(idFilialDestino);
        final Long idMunicipioFilialDestino = (Long) map.get("idMunicipio");

        /* Busca conjunto com todos os feriados do munícipio do destinatário e do municipio da filial de destino */
        final Set<String> feriados = this.getFeriadosByFilialAndMunicipio(idMunicipioFilialDestino, idMunicipioDestino);

        /* Adiciona dias úteis de prazo ao prazo de entrega */
        dtPrazoEntrega = this.addDiasUteisToData(dtPrazoEntrega, nrPrazo.intValue(), feriados, ignoraFeriados);
        dtPrazoReentrega = this.addDiasUteisToData(dtPrazoReentrega, (nrDiasEntregaDificuldade.intValue() < 1 ? 1 : nrDiasEntregaDificuldade.intValue()), feriados, ignoraFeriados);
        
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
            dtPrazoEntrega = this.getDataUtil(JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual(), feriados, ignoraFeriados);
        }

        final Map<String, Object> retorno = new HashMap<String, Object>();
        retorno.put("dtPrazoEntrega", dtPrazoEntrega);
        retorno.put("nrPrazo", nrPrazo);

        retorno.put("dtPrazoReentrega", dtPrazoReentrega);
        //LMS-8779
        retorno.put("nrDiasColeta", nrDiasColeta);
        retorno.put("nrDiasColetaDificuldade", nrDiasColetaDificuldade);
        retorno.put("nrDiasEntrega", nrDiasEntrega);
        retorno.put("nrDiasEntregaDificuldade", nrDiasEntregaDificuldade);
        retorno.put("nrDiasTransferencia", nrDiasTransferencia);

        if (prazoEntregaCliente != null) {
            ajustaPrazosColetaEntregaTransferencia(retorno);
        }

        return retorno;
    }
    
    protected abstract Set<String> findFeriadosFilialOrigem(Filial filialOrigem, final Long idMunicipioOrigem) ;

    protected abstract Filial findFilialOrigem(final Long idFilialOrigem) ;

    protected abstract Long addHorarioCorteCliente(final Cliente clienteRemetente, final Long idServico, final RestricaoRota restricaoRotaOrigem, final Long idMunicipioOrigem, Map<String, Object> dadosEnderecoDestino, final Long idFilialOrigem, final Filial filialOrigem, TimeOfDay hrEmissao, Long nrPrazo);

    protected abstract RestricaoRota restricoesRotaOrigem(Long idMunicipioOrigem, Long idServico, String nrCepOrigem, Long idFilialOrigem);

    protected abstract RestricaoRota restricoesDestino(Map<String, Object> dadosEnderecoDestino, Long idMunicipioDestino, Long idServico, Long idFilialDestino);

    protected abstract PrazoEntregaCliente calculaPrazoEspecial(Cliente clienteRemetente,
            Cliente clienteDevedor, Long idServico, RestricaoRota restricaoRotaOrigem,
            RestricaoRota restricaoRotaDestino, Cliente clienteDestinatario);

    private void ajustaPrazosColetaEntregaTransferencia(Map<String, Object> retorno) {
        Long nrPrazoCliente = (Long) retorno.get("nrPrazo");
        Long nrDiasColeta = (Long) retorno.get("nrDiasColeta");
        Long nrDiasEntrega = (Long) retorno.get("nrDiasEntrega");
        Long nrDiasTransferencia = (Long) retorno.get("nrDiasTransferencia");

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

        retorno.put("nrDiasColeta", nrDiasColeta);
        retorno.put("nrDiasEntrega", nrDiasEntrega);
        retorno.put("nrDiasTransferencia", nrDiasTransferencia);
    }

    protected final Long calcAdicionalDificuldadeEntrega(final Cliente clienteDestinatario, final Cliente clienteConsignatario, final Cliente clienteDevedor) {
        if (clienteDevedor != null && clienteDevedor.getBldesconsiderarDificuldade()) {
            return LongUtils.ZERO;
        }

        if (clienteConsignatario != null) {
            return getDificuldadeCliente(clienteConsignatario.getTpDificuldadeEntrega());
        } else if (clienteDestinatario != null) {
            return getDificuldadeCliente(clienteDestinatario.getTpDificuldadeEntrega());
        } else {
            return LongUtils.ZERO;
        }

    }

    private YearMonthDay addDiasUteisToData(YearMonthDay data, int dias, final Set<String> feriados, Boolean ignoraFeriados) {
        YearMonthDay dtInicio = data.plusDays(1);
        /* Pega data inicial (ou data de coleta ou data atual) e soma 1 dia */
        data = data.plusDays(dias);
        /* Adiciona dias de prazo à data de entrega */
        return this.getDataUtil(dtInicio, data, feriados, ignoraFeriados);
    }

    /**
     * Adiciona dias a data final de acordo com finais de semana e feriados
     * presentes no intervalo dataInicio / dataFinal
     *
     * @param dataInicio
     * @param dataFinal
     * @param feriados
     * @return
     */
    private YearMonthDay getDataUtil(YearMonthDay dataInicio, YearMonthDay dataFinal, final Set<String> feriados, Boolean ignoraFeriados) {
        // Verifica feriados e finais de semana
        int diaSemana = 0;
        YearMonthDay dataFinalWk = dataFinal;
        /* Enquanto data corrente for menor ou igual a data */
        while (dataInicio.compareTo(dataFinalWk) < 1) {
            diaSemana = JTDateTimeUtils.getNroDiaSemana(dataInicio);
            if (diaSemana == DateTimeConstants.SATURDAY) {
                // Adiciona 2 dias para nao testar domingo
                dataInicio = dataInicio.plusDays(2);
                dataFinalWk = dataFinalWk.plusDays(2);

            } else if (diaSemana == DateTimeConstants.SUNDAY) {
                dataInicio = dataInicio.plusDays(1);
                dataFinalWk = dataFinalWk.plusDays(1);

            } else if (feriados.contains(dataInicio.toString(DateTimeFormat.forPattern("dd-MM")))) {
                dataInicio = dataInicio.plusDays(1);

                //LMSA-3959
                if (null == ignoraFeriados || !ignoraFeriados) {
                    dataFinalWk = dataFinalWk.plusDays(1);

                }
            } else {
                dataInicio = dataInicio.plusDays(1);

            }
        }

        //LMSA-3959
        if (feriados.contains(dataFinalWk.toString(DateTimeFormat.forPattern("dd-MM"))) && ignoraFeriados) {
            dataFinalWk = dataFinalWk.plusDays(1);

        }

        return dataFinalWk;

    }

    private YearMonthDay getNextDataUtil(YearMonthDay data, final Set<String> feriados, Boolean ignoraFeriados, Cliente clienteDevedor) {
        // Verifica feriados e finais de semana
        int diaSemana = 0;
        boolean achou = false;

        /* Enquanto data corrente for menor ou igual a data */
        while (!achou) {
            diaSemana = JTDateTimeUtils.getNroDiaSemana(data);
            if (diaSemana == DateTimeConstants.SATURDAY) {
                if (null == clienteDevedor.getBlEmissaoSabado() || !clienteDevedor.getBlEmissaoSabado()) {
                    // Adiciona 2 dias para nao testar domingo
                    data = data.plusDays(2);
                } else {
                    achou = true;
                }
            } else if (diaSemana == DateTimeConstants.SUNDAY) {
                data = data.plusDays(1);
            } else if (feriados.contains(data.toString(DateTimeFormat.forPattern("dd-MM"))) && (null == ignoraFeriados || !ignoraFeriados)) {
                data = data.plusDays(1);
            } else {
                achou = true;
            }
        }
        return data;
    }

    protected Set<String> getFeriadosByFilialAndMunicipio(final Long idMunicipioFilialDestino, final Long idMunicipioDestino) {
        Set<String> retorno = new HashSet<String>();
        List feriadosVigentesMunicipioDestino = getFeriadoService().findAllDtFeriadosVigentesByIdMunicipio(idMunicipioDestino);
        retorno.addAll(feriadosVigentesMunicipioDestino);

        if (!idMunicipioFilialDestino.equals(idMunicipioDestino)) {
            List feriadosVigentesMunicipioFilialDestino = getFeriadoService().findAllDtFeriadosVigentesByIdMunicipio(idMunicipioFilialDestino);
            retorno.addAll(feriadosVigentesMunicipioFilialDestino);
        }

        return retorno;
    }

    private Long getDificuldadeCliente(final DomainValue tpDificuldade) {
        if (tpDificuldade != null) {
            String vlDificuldade = tpDificuldade.getValue();
            if (StringUtils.isNumeric(vlDificuldade) && StringUtils.isNotBlank(vlDificuldade)) {
                return LongUtils.getLong(vlDificuldade);
            }
        }
        return LongUtils.ZERO;
    }

    public abstract PpeService getPpeService();

    public abstract MunicipioService getMunicipioService();

    public abstract FeriadoService getFeriadoService();

    public abstract EnderecoPessoaService getEnderecoPessoaService();
    
}
