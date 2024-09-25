package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import org.apache.commons.lang.BooleanUtils;

class CopiadoraDeDadosDoClienteMatriz {

    private final ClienteService clienteService;

    CopiadoraDeDadosDoClienteMatriz(ClienteService clienteService) {
        if (clienteService == null)
            throw new IllegalArgumentException("O parâmetro 'clienteService' não pode ser nulo");
        this.clienteService = clienteService;
    }

    TypedFlatMap copiarSomenteSeForUmClienteFilial(Cliente cliente) {
        final TypedFlatMap result = new TypedFlatMap();
        /** Copia dados do Cliente Matriz */
        if(ehUmClienteFilial(cliente)) {

            Cliente clienteMatriz = clienteService.findById(cliente.getClienteMatriz().getIdCliente());
            Cliente responsavel = clienteService.findById(cliente.getCliente().getIdCliente());

            result.put("cliente.idCliente", responsavel.getIdCliente());
            result.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(responsavel.getPessoa()));
            result.put("cliente.pessoa.nmPessoa", responsavel.getPessoa().getNmPessoa());

            definirMoedaByIdMoedaLimCred(result, clienteMatriz);
            definirMoedaByIdMoedaLimDoctos(result, clienteMatriz);
            definirMoedaByIdMoedaSaldoAtual(result, clienteMatriz);

            result.put("vlLimiteCredito", BigDecimalUtils.defaultBigDecimal(clienteMatriz.getVlLimiteCredito()));
            result.put("vlLimiteDocumentos", BigDecimalUtils.defaultBigDecimal(clienteMatriz.getVlLimiteDocumentos()));
            result.put("vlSaldoAtual", BigDecimalUtils.ZERO);
            result.put("pcDescontoFreteCif", clienteMatriz.getPcDescontoFreteCif());
            result.put("pcDescontoFreteFob", clienteMatriz.getPcDescontoFreteFob());
            result.put("pcJuroDiario", clienteMatriz.getPcJuroDiario());
            result.put("nrDiasLimiteDebito", clienteMatriz.getNrDiasLimiteDebito());

            result.put("tpCobranca.value", clienteMatriz.getTpCobranca().getValue());

            definirMeioEnvioBoleto(result, clienteMatriz);
            definirPeriodicidadeTransf(result, clienteMatriz);

            result.put("blGeraReciboFreteEntrega", clienteMatriz.getBlGeraReciboFreteEntrega());
            result.put("blBaseCalculo", cliente.getBlBaseCalculo());
            result.put("blIndicadorProtesto", clienteMatriz.getBlIndicadorProtesto());
            result.put("blEmiteBoletoCliDestino", clienteMatriz.getBlEmiteBoletoCliDestino());
            result.put("blAgrupaFaturamentoMes", clienteMatriz.getBlAgrupaFaturamentoMes());
            result.put("blRessarceFreteFob", clienteMatriz.getBlRessarceFreteFob());
            result.put("blPreFatura", clienteMatriz.getBlPreFatura());
            result.put("blFaturaDocsEntregues", clienteMatriz.getBlFaturaDocsEntregues());
            result.put("blCobrancaCentralizada", clienteMatriz.getBlCobrancaCentralizada());
            result.put("blFaturaDocsConferidos", clienteMatriz.getBlFaturaDocsConferidos());
            result.put("blFronteiraRapida", clienteMatriz.getBlFronteiraRapida());
            result.put("blFaturaDocReferencia", clienteMatriz.getBlFaturaDocReferencia());

            //LMS-441 Nova coluna no cadastro de clientes para financeiro
            result.put("blCalculoArqPreFatura", clienteMatriz.getBlCalculoArqPreFatura());
            result.put("blMtzLiberaRIM", BooleanUtils.isTrue(clienteMatriz.getBlMtzLiberaRIM()));
            result.put("obFatura", clienteMatriz.getObFatura());
            result.put("blEnviaDacteXmlFat", clienteMatriz.getBlEnviaDacteXmlFat());
            result.put("blDpeFeriado", BooleanUtils.isTrue(clienteMatriz.getBlDpeFeriado()));

            result.put("blEnviaDocsFaturamentoNas", clienteMatriz.getBlEnviaDocsFaturamentoNas());
        }
        return result;
    }

    private void definirPeriodicidadeTransf(TypedFlatMap result, Cliente clienteMatriz) {
        DomainValue tpPeriodicidadeTransf = clienteMatriz.getTpPeriodicidadeTransf();
        if(tpPeriodicidadeTransf != null) {
            result.put("tpPeriodicidadeTransf", tpPeriodicidadeTransf);
        }
    }

    private void definirMeioEnvioBoleto(TypedFlatMap result, Cliente clienteMatriz) {
        final DomainValue tpMeioEnvioBoleto = clienteMatriz.getTpMeioEnvioBoleto();
        if(tpMeioEnvioBoleto != null){
            result.put("tpMeioEnvioBoleto.value", tpMeioEnvioBoleto.getValue());
        }
    }

    private boolean ehUmClienteFilial(Cliente cliente) {
        return ConstantesVendas.CLIENTE_FILIAL.equals(cliente.getTpCliente().getValue());
    }

    private void definirMoedaByIdMoedaSaldoAtual(TypedFlatMap result, Cliente clienteMatriz) {
        definirMoeda(result, clienteMatriz.getMoedaByIdMoedaSaldoAtual(), "moedaByIdMoedaSaldoAtual.idMoeda");
    }

    private void definirMoedaByIdMoedaLimDoctos(TypedFlatMap result, Cliente clienteMatriz) {
        final Moeda moedaByIdMoedaLimDoctos = clienteMatriz.getMoedaByIdMoedaLimDoctos();
        definirMoeda(result, moedaByIdMoedaLimDoctos, "moedaByIdMoedaLimDoctos.idMoeda");
    }

    private void definirMoedaByIdMoedaLimCred(TypedFlatMap result, Cliente clienteMatriz) {
        final Moeda moedaByIdMoedaLimCred = clienteMatriz.getMoedaByIdMoedaLimCred();
        definirMoeda(result, moedaByIdMoedaLimCred, "moedaByIdMoedaLimCred.idMoeda");
    }

    private void definirMoeda(TypedFlatMap result, Moeda moeda, String parametro) {
        if (moeda != null) {
            result.put(parametro, moeda.getIdMoeda());
        }
    }
}
