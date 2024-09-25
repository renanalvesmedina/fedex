package com.mercurio.lms.fretecarreteiroviagem.model.service;

import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.constantes.entidades.ConsManifesto;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.constantes.entidades.ConsRatDocServTrecho;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoInternacCto;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.fretecarreteiroviagem.model.RateioDoctoServicoTrecho;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrêsa Vargas
 * @spring.bean id="lms.fretecarreteiroviagem.gerarRateioFreteCarreteiroService"
 */
public class GerarRateioFreteCarreteiroService {

    private static final String CONTROLE_TRECHO_NR_DISTANCIA = "controleTrecho_nrDistancia";
    private static final String CONTROLE_TRECHO_ID_CONTROLE_TRECHO = "controleTrecho_idControleTrecho";
    private static final String FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL = "filialByIdFilialOrigem_idFilial";
    private static final String FILIAL_BY_ID_FILIAL_DESTINO_ID_FILIAL = "filialByIdFilialDestino_idFilial";
    private static final String NR_ORDEM_ORIGEM = "nrOrdemOrigem";
    private static final String NR_ORDEM_DESTINO = "nrOrdemDestino";
    private static final int SCALE_FRETE_CARRETEIRO = 6;

    private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
    private ControleCargaService controleCargaService;
    private ManifestoService manifestoService;
    private RateioDoctoServicoTrechoService rateioDoctoServicoTrechoService;
    private ControleTrechoService controleTrechoService;
    private ManifestoNacionalCtoService manifestoNacionalCtoService;
    private ProprietarioService proprietarioService;

    /**
     * Chamada da rotina para teste via interface, onde busco o idControleCarga passado no parametros
     *
     * @param parametros
     */
    public TypedFlatMap execute(TypedFlatMap parametros) {
        if (parametros.getLong("idControleCarga") != null) {
            TypedFlatMap tfm = new TypedFlatMap();
            tfm.put("valorTotalPago", generateRateioFreteCarreteiro(parametros.getLong("idControleCarga")).floatValue());
            return tfm;
        } else {
            return null;
        }
    }

    public BigDecimal execute(Long idControleCarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        return generateRateioFreteCarreteiro(idControleCarga, adsmNativeBatchSqlOperations);
    }

    public BigDecimal execute(Long idControleCarga) {
        return generateRateioFreteCarreteiro(idControleCarga);
    }

    private BigDecimal generateRateioFreteCarreteiro(Long idControleCarga) {
        return generateRateioFreteCarreteiro(idControleCarga, null);
    }

    /**
     * Rotina: Gerar rateio do frete carreteiro
     *
     * @param idControleCarga
     */
    private BigDecimal generateRateioFreteCarreteiro(Long idControleCarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        if (idControleCarga == null) {
            return null;
        }

        BigDecimal vlFreteCarreteiro = getValorTotalPago(idControleCarga);
        List listTrechosDireto = findTrechosDireto(idControleCarga);
        BigDecimal vlFreteDistancia = BigDecimal.ZERO;
        Integer distanciaTotal = 0;

        for (Object aListTrechosDireto : listTrechosDireto) {
            Map map = (Map) aListTrechosDireto;
            distanciaTotal = distanciaTotal + (map.get(CONTROLE_TRECHO_NR_DISTANCIA) != null ? (Integer) map.get(CONTROLE_TRECHO_NR_DISTANCIA) : 0);
        }

        if (distanciaTotal != 0) {
            vlFreteDistancia = vlFreteCarreteiro.divide(BigDecimal.valueOf(distanciaTotal), SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP);
        }

        Map hashMapVlRateioFreteCarreteiro = new HashMap();

        for (Object aListTrechosDireto : listTrechosDireto) {
            Map map = (Map) aListTrechosDireto;

            ControleTrecho controleTrecho = controleTrechoService.findById((Long) map.get(CONTROLE_TRECHO_ID_CONTROLE_TRECHO));

            //Apagar todos rateio_docto_servico_trecho do Trecho em questão e inserir novos.
            rateioDoctoServicoTrechoService.removeByIdControleTrecho(controleTrecho.getIdControleTrecho());

            BigDecimal vlFreteTrechoDistancia = BigDecimal.ZERO;
            vlFreteTrechoDistancia = vlFreteTrechoDistancia.add(vlFreteDistancia.multiply(BigDecimal.valueOf(Integer.valueOf((Integer) map.get(CONTROLE_TRECHO_NR_DISTANCIA)).intValue())));

            // Encontro todos os manifestos de controle carga somando peso e Atualizo campo MANIFESTO.VL_RATEIO_FRETE_CARRETEIRO com vlFreteManifesto
            List<Manifesto> manifestos = findManifestosTrechoByIdControleCarga(idControleCarga, (Long) map.get(FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL), (Long) map.get(FILIAL_BY_ID_FILIAL_DESTINO_ID_FILIAL), (Byte) map.get(NR_ORDEM_ORIGEM), (Byte) map.get(NR_ORDEM_DESTINO));

            BigDecimal psTrecho = addPsTrechoAndSaveManifesto(adsmNativeBatchSqlOperations, BigDecimal.ZERO, manifestos);

            // Calcular o valor do peso do frete carreteiro do trecho...
            BigDecimal vlFreteTrechoPeso = BigDecimal.ZERO;
            if (psTrecho != null && psTrecho.longValue() != 0) {
                vlFreteTrechoPeso = vlFreteTrechoDistancia.divide(psTrecho, SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP);
            }

            for (Manifesto manifesto : manifestos) {
                gravarRateios(adsmNativeBatchSqlOperations, hashMapVlRateioFreteCarreteiro, controleTrecho, vlFreteTrechoPeso, manifesto);
            }
        }

        return vlFreteCarreteiro;
    }

    private BigDecimal addPsTrechoAndSaveManifesto(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, BigDecimal psTrecho, List<Manifesto> manifestos) {
        BigDecimal psTrechoAux = psTrecho;
        for (Manifesto manifesto : manifestos) {
            psTrechoAux = psTrechoAux.add(manifesto.getPsTotalManifesto());
            manifesto.setVlRateioFreteCarreteiro(BigDecimal.ZERO);
            if (adsmNativeBatchSqlOperations == null) {
                manifestoService.storeBasic(manifesto);
            } else {
                adsmNativeBatchSqlOperations.evictObject(manifesto);
            }
        }
        return psTrechoAux;
    }

    private void gravarRateios(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Map hashMapVlRateioFreteCarreteiro, ControleTrecho controleTrecho, BigDecimal vlFreteTrechoPeso, Manifesto manifesto) {
        // Calcular o valor do frete carreteiro por manifesto...
        BigDecimal vlFreteManifesto;
        vlFreteManifesto = manifesto.getPsTotalManifesto().multiply(vlFreteTrechoPeso);

        // Atualizar o campo MANIFESTO.VL_RATEIO_FRETE_CARRETEIRO com vlFreteManifesto
        BigDecimal vlRateioFreteCarreteiro = BigDecimal.ZERO;
        vlRateioFreteCarreteiro = vlRateioFreteCarreteiro.add(manifesto.getVlRateioFreteCarreteiro());
        vlRateioFreteCarreteiro = vlRateioFreteCarreteiro.add(vlFreteManifesto);

        // Verifico se existe algum rateio já lançado no manifesto, se não existir eu lanço
        if (hashMapVlRateioFreteCarreteiro.get(manifesto.getIdManifesto()) == null) {
            hashMapVlRateioFreteCarreteiro.put(manifesto.getIdManifesto(), vlRateioFreteCarreteiro);
        } else {
            vlRateioFreteCarreteiro = vlRateioFreteCarreteiro.add((BigDecimal) hashMapVlRateioFreteCarreteiro.get(manifesto.getIdManifesto()));
            hashMapVlRateioFreteCarreteiro.put(manifesto.getIdManifesto(), vlRateioFreteCarreteiro);
        }

        manifesto.setVlRateioFreteCarreteiro(vlRateioFreteCarreteiro);

        if (adsmNativeBatchSqlOperations == null) {
            manifestoService.storeBasic(manifesto);
        } else {

            String alias = "man" + vlRateioFreteCarreteiro;

            if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsManifesto.TN_MANIFESTO, alias)) {
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsManifesto.TN_MANIFESTO,
                        ConsManifesto.VL_RATEIO_FRETE_CARRETEIRO, vlRateioFreteCarreteiro, alias);
            }

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsManifesto.TN_MANIFESTO,
                    manifesto.getIdManifesto(), alias);

        }

        // Para cada manifesto, buscar todos os documentos de servico,
        // NACIONAL
        BigDecimal vlFreteManifestoPeso = BigDecimal.ZERO;

        // Calcular o valor do peso do frete carreteiro do trecho
        if (manifesto.getPsTotalManifesto() != null && manifesto.getPsTotalManifesto().longValue() != 0) {
            vlFreteManifestoPeso = vlFreteManifesto.divide(manifesto.getPsTotalManifesto(), SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP);
        }

        // NACIONAL - Busca Conhecimentos Nacionais
        gravarManifestoViagemNacional(adsmNativeBatchSqlOperations, controleTrecho, manifesto, vlFreteManifestoPeso);

        // INTERNACIONAL
        gravarManifestoViagemInternacional(adsmNativeBatchSqlOperations, controleTrecho, manifesto, vlFreteManifestoPeso);
    }

    private void gravarManifestoViagemInternacional(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, ControleTrecho controleTrecho, Manifesto manifesto, BigDecimal vlFreteManifestoPeso) {
        BigDecimal vlFreteDocumento;
        if (manifesto.getManifestoInternacional() != null && manifesto.getManifestoInternacional().getManifestoInternacCtos() != null) {
            for (Iterator iter = manifesto.getManifestoInternacional().getManifestoInternacCtos().iterator(); iter.hasNext(); ) {
                ManifestoInternacCto manifestoInternacCto = (ManifestoInternacCto) iter.next();

                BigDecimal vlPesoAforado = manifestoInternacCto.getCtoInternacional().getPsAforado() == null ? BigDecimal.ZERO : manifestoInternacCto.getCtoInternacional().getPsAforado();
                BigDecimal vlPesoReal = manifestoInternacCto.getCtoInternacional().getPsReal() == null ? BigDecimal.ZERO : manifestoInternacCto.getCtoInternacional().getPsReal();

                if (CompareUtils.ge(vlPesoAforado, vlPesoReal)) {
                    // Calcular o valor do frete carreteiro por documento
                    vlFreteDocumento = vlPesoAforado.multiply(vlFreteManifestoPeso);
                } else {
                    // Calcular o valor do frete carreteiro por documento
                    vlFreteDocumento = vlPesoReal.multiply(vlFreteManifestoPeso);
                }

                // Gravar o frete por trecho do documento de serviço na tabela TN_RATEIO_DOCTO_SERVICO_TRECHO.VL_RATEIO
                gravarRateioDoctoServicoInternacional(manifestoInternacCto.getCtoInternacional(), controleTrecho, vlFreteDocumento, adsmNativeBatchSqlOperations);
            }
        }
    }

    private void gravarManifestoViagemNacional(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, ControleTrecho controleTrecho, Manifesto manifesto, BigDecimal vlFreteManifestoPeso) {
        BigDecimal vlFreteDocumento;
        if (manifesto.getManifestoViagemNacional() != null) {
            List<ManifestoNacionalCto> manifestosNacionaisCto = manifestoNacionalCtoService.findManifestoNacionalCtosByIdManifestoViagemNacional(manifesto.getManifestoViagemNacional().getIdManifestoViagemNacional());
            for (ManifestoNacionalCto manifestoNacionalCto : manifestosNacionaisCto) {
                BigDecimal vlPesoAforado = manifestoNacionalCto.getConhecimento().getPsAforado() == null ? BigDecimal.ZERO : manifestoNacionalCto.getConhecimento().getPsAforado();
                BigDecimal vlPesoReal = manifestoNacionalCto.getConhecimento().getPsReal() == null ? BigDecimal.ZERO : manifestoNacionalCto.getConhecimento().getPsReal();

                if (CompareUtils.ge(vlPesoAforado, vlPesoReal)) {
                    // Calcular o valor do frete carreteiro por documento
                    vlFreteDocumento = vlPesoAforado.multiply(vlFreteManifestoPeso);
                } else {
                    // Calcular o valor do frete carreteiro por documento
                    vlFreteDocumento = vlPesoReal.multiply(vlFreteManifestoPeso);
                }
                // Gravar o frete por trecho do documento de serviço na tabela TN_RATEIO_DOCTO_SERVICO_TRECHO.VL_RATEIO
                gravarRateioDoctoServicoNacional(manifestoNacionalCto.getConhecimento(), controleTrecho, vlFreteDocumento, adsmNativeBatchSqlOperations);
            }
        }
    }

    private void gravarRateioDoctoServicoNacional(Conhecimento conhecimento, ControleTrecho controleTrecho,
                                                  BigDecimal vlFreteDocumento, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        if (adsmNativeBatchSqlOperations == null) {
            RateioDoctoServicoTrecho rateioDoctoServicoTrecho = new RateioDoctoServicoTrecho();
            rateioDoctoServicoTrecho.setDoctoServico(conhecimento);
            rateioDoctoServicoTrecho.setControleTrecho(controleTrecho);
            rateioDoctoServicoTrecho.setVlRateio(vlFreteDocumento);

            rateioDoctoServicoTrechoService.store(rateioDoctoServicoTrecho);
        } else {
            Map<String, Object> rateioDoctoServicoTrechoKeyValueMap = new HashMap<String, Object>();
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.ID_DOCTO_SERVICO, conhecimento.getIdDoctoServico());
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.ID_CONTROLE_TRECHO, controleTrecho.getIdControleTrecho());
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.VL_RATEIO, vlFreteDocumento);

            adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsRatDocServTrecho.TN_RATEIO_DOCTO_SERVICO_TRECHO, rateioDoctoServicoTrechoKeyValueMap);
        }
    }

    private void gravarRateioDoctoServicoInternacional(CtoInternacional ctoInternacional, ControleTrecho controleTrecho,
                                                       BigDecimal vlFreteDocumento, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        if (adsmNativeBatchSqlOperations == null) {
            RateioDoctoServicoTrecho rateioDoctoServicoTrecho = new RateioDoctoServicoTrecho();
            rateioDoctoServicoTrecho.setDoctoServico(ctoInternacional);
            rateioDoctoServicoTrecho.setControleTrecho(controleTrecho);
            rateioDoctoServicoTrecho.setVlRateio(vlFreteDocumento);

            rateioDoctoServicoTrechoService.store(rateioDoctoServicoTrecho);
        } else {
            Map<String, Object> rateioDoctoServicoTrechoKeyValueMap = new HashMap<String, Object>();
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.ID_DOCTO_SERVICO, ctoInternacional.getIdDoctoServico());
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.ID_CONTROLE_TRECHO, controleTrecho.getIdControleTrecho());
            rateioDoctoServicoTrechoKeyValueMap.put(ConsRatDocServTrecho.VL_RATEIO, vlFreteDocumento);
            adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsRatDocServTrecho.TN_RATEIO_DOCTO_SERVICO_TRECHO, rateioDoctoServicoTrechoKeyValueMap);
        }
    }

    private List findManifestosTrechoByIdControleCarga(Long idControleCarga, Long idFilialOrigemTrecho, Long idFilialDestinoTrecho, Byte nrOrdemOrigem, Byte nrOrdemDestino) {
        return manifestoService.findManifestosTrechoByIdControleCarga(idControleCarga, idFilialOrigemTrecho, idFilialDestinoTrecho, nrOrdemOrigem, nrOrdemDestino);
    }

    private List findTrechosDireto(Long idControleCarga) {
        return controleCargaService.findTrechosDireto(idControleCarga);
    }

    private BigDecimal getValorTotalPago(Long idControleCarga) {
        BigDecimal valorTotalPago = BigDecimalUtils.ZERO;
        BigDecimal valorTotalAgregado = BigDecimalUtils.ZERO;
        BigDecimal valorTotalEventual = BigDecimalUtils.ZERO;
        BigDecimal valorTotalProprio = BigDecimalUtils.ZERO;

        // 1 - Consulta recibos gerados que não sejam de adiantamento
        List<ReciboFreteCarreteiro> recibosFreteCarreteiro = reciboFreteCarreteiroService.findReciboFreteCarreteiroByIdControleCarga(idControleCarga, Boolean.FALSE, new String[]{"A", "E"}); // [A]gregado, [E]ventual
        if (recibosFreteCarreteiro != null) {
            for (ReciboFreteCarreteiro reciboFreteCarreteiro : recibosFreteCarreteiro) {
                // 2 - Somar ocorrências de desconto ativas para recibos encontrados, onde TP_OCORRENCIA = 'D' && BL_DESCONTO_CANCELADO = FALSE
                BigDecimal valorDesconto = reciboFreteCarreteiroService.getSomaValoresDescontos(reciboFreteCarreteiro.getIdReciboFreteCarreteiro());
                // 3 - Somar em valorTotalPago: vlBruto + vlPremio + vlDiaria - vlDesconto
                BigDecimal valorPago = reciboFreteCarreteiro.getVlBruto() == null ? BigDecimal.ZERO : reciboFreteCarreteiro.getVlBruto();
                valorPago = valorPago.add(reciboFreteCarreteiro.getVlPremio() == null ? BigDecimal.ZERO : reciboFreteCarreteiro.getVlPremio());
                valorPago = valorPago.add(reciboFreteCarreteiro.getVlDiaria() == null ? BigDecimal.ZERO : reciboFreteCarreteiro.getVlDiaria());
                valorPago = valorPago.subtract(valorDesconto);

                valorTotalPago = valorTotalPago.add(valorPago);

                // Acumulo totalAgregado e totalEventual
                Proprietario proprietario = reciboFreteCarreteiro.getProprietario();

                if (proprietario != null && proprietario.getTpProprietario() == null) {
                    proprietario = proprietarioService.findById(proprietario.getIdProprietario());
                }

                if ("A".equals(proprietario.getTpProprietario().getValue())) { // [A]gregado
                    valorTotalAgregado = valorTotalAgregado.add(valorPago);
                } else if ("E".equals(proprietario.getTpProprietario().getValue())) { // [E]ventual
                    valorTotalEventual = valorTotalEventual.add(valorPago);
                }
            }
        }
        // 4 - Somar em vlTotal valores pagtoProprietario_cc.vlPagamento dos controles de carga onde proprietario seja do tipo Mercúrio
        valorTotalProprio = controleCargaService.getSomaValoresMercurioPagtoProprietarioCC(idControleCarga);
        valorTotalPago = valorTotalPago.add(valorTotalProprio);

        storeControleCargaIfHasValue(idControleCarga, valorTotalPago, valorTotalAgregado, valorTotalEventual, valorTotalProprio);

        return valorTotalPago;
    }

    private void storeControleCargaIfHasValue(Long idControleCarga, BigDecimal valorTotalPago, BigDecimal valorTotalAgregado, BigDecimal valorTotalEventual, BigDecimal valorTotalProprio) {
        if (BigDecimalUtils.hasValue(valorTotalPago)) {
            ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
            controleCarga.setPcFreteAgregado(valorTotalAgregado.divide(valorTotalPago, SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP).multiply(BigDecimalUtils.HUNDRED));
            controleCarga.setPcFreteEventual(valorTotalEventual.divide(valorTotalPago, SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP).multiply(BigDecimalUtils.HUNDRED));
            controleCarga.setPcFreteMercurio(valorTotalProprio.divide(valorTotalPago, SCALE_FRETE_CARRETEIRO, BigDecimal.ROUND_HALF_UP).multiply(BigDecimalUtils.HUNDRED));
            controleCargaService.store(controleCarga);
        }
    }

    public void setProprietarioService(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    public void setManifestoNacionalCtoService(ManifestoNacionalCtoService manifestoNacionalCtoService) {
        this.manifestoNacionalCtoService = manifestoNacionalCtoService;
    }

    public void setControleTrechoService(ControleTrechoService controleTrechoService) {
        this.controleTrechoService = controleTrechoService;
    }

    public void setRateioDoctoServicoTrechoService(RateioDoctoServicoTrechoService rateioDoctoServicoTrechoService) {
        this.rateioDoctoServicoTrechoService = rateioDoctoServicoTrechoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
        this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
    }
}
