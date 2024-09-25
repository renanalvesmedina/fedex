package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.service.EventoManifestoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.contasreceber.model.service.GerarBoletoReciboManifestoService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Emitir Manifesto de Viagem Nacional.
 *
 * @author Andre Valadas.
 * @spring.bean id="lms.expedicao.emitirMVNService"
 */
public class EmitirMVNService {
    private ConfiguracoesFacade configuracoesFacade;
    private ManifestoService manifestoService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private EmitirDocumentoService emitirDocumentoService;
    private ConhecimentoService conhecimentoService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private GerarMVNService gerarMVNService;
    private GerarBoletoReciboManifestoService gerarBoletoReciboManifestoService;
    private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
    private EventoManifestoService eventoManifestoService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private ContatoService contatoService;

    /**
     * Rotida de Emissao do Manifesto de Viagem Nacional.
     *
     * @param idManifesto
     * @param observacao
     * @return
     * @author Andre Valadas.
     */
    public String executeEmitirMVN(Long idManifesto, String observacao) {
        validarEmissaoAtualizarMVN(idManifesto, observacao);

        /** Gera Formulario de Impressao */
        return gerarMVNService.generateMVN(idManifesto);
    }

    /**
     * Emite MVN para filial com MDF-e.
     *
     * @param idManifesto
     * @param observacao
     * @return
     * @throws Exception
     */
    public void executeEmitirMVNParaMDFe(Long idManifesto, String observacao) throws Exception {
        validarEmissaoAtualizarMVN(idManifesto, observacao);
    }

    /**
     * @param idManifesto
     * @param observacao
     */
    private void validarEmissaoAtualizarMVN(Long idManifesto, String observacao) {

        /** Valida parametros */
        manifestoService.flushModeParaCommit();

        if (idManifesto == null || idManifesto.longValue() < 1) {
            throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("preManifesto")});
        }

        /** Busca Manifesto */
        Manifesto manifesto = manifestoService.findById(idManifesto);

        /** Valida negocio */
        validateEmissao(manifesto);

        /** Atualiza Dados */
        updateDados(manifesto, observacao);

        manifestoService.flushModeParaAutoComFlushEClear();
    }

    /**
     * Rotida de Reemissao do Manifesto de Viagem Nacional.
     *
     * @param idManifestoViagemNacional
     * @return
     * @author Andre Valadas.
     */
    public String executeReemitirMVN(Long idManifestoViagemNacional) {
        validarReemissaoMVN(idManifestoViagemNacional);

        /** Gera Formulario de Impressao */
        return gerarMVNService.generateMVN(idManifestoViagemNacional);
    }

    /**
     * Reemite MVN para filial com MDF-e.
     *
     * @param idManifestoViagemNacional
     * @return
     * @throws Exception
     */
    public void executeReemitirMVNParaMDFe(Long idManifestoViagemNacional) throws Exception {
        validarReemissaoMVN(idManifestoViagemNacional);
    }

    private void validarReemissaoMVN(Long idManifestoViagemNacional) {
        /** Valida parametros */
        if (idManifestoViagemNacional == null || idManifestoViagemNacional.longValue() < 1) {
            throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("manifestoViagem")});
        }
        /** Busca Manifesto */
        Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifestoViagemNacional, false);
        /** Valida negocio */
        validateReemissao(manifesto);
    }


    /**
     * Valida se pode Emitir Manifesto de Viagem.
     *
     * @param manifesto
     * @author Andre Valadas.
     */
    private void validateEmissao(Manifesto manifesto) {
        if (!ConstantesEntrega.STATUS_CARREGAMENTO_CONCLUIDO.equals(manifesto.getTpStatusManifesto().getValue())) {
            throw new BusinessException("LMS-04172");
        }
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(manifesto.getTpAbrangencia().getValue())) {
            throw new BusinessException("LMS-04173");
        }
        if (!ConstantesExpedicao.TP_MANIFESTO_VIAGEM.equals(manifesto.getTpManifesto().getValue())) {
            throw new BusinessException("LMS-04192");
        }
        if (!SessionUtils.getFilialSessao().getIdFilial().equals(manifesto.getFilialByIdFilialOrigem().getIdFilial())) {
            throw new BusinessException("LMS-04174");
        }
    }

    /**
     * Valida se pode Reemitir Manifesto de Viagem.
     *
     * @param manifesto
     * @author Andre Valadas.
     */
    private void validateReemissao(Manifesto manifesto) {
        if (!ConstantesEntrega.STATUS_MANIFESTO_EMITIDO.equals(manifesto.getTpStatusManifesto().getValue())) {
            throw new BusinessException("LMS-04184");
        }
        if (!SessionUtils.getFilialSessao().getIdFilial().equals(manifesto.getFilialByIdFilialOrigem().getIdFilial())) {
            throw new BusinessException("LMS-04185");
        }
    }

    /**
     * Operacoes de Atualizacao do Manifesto.
     *
     * @param manifesto
     * @author Andre Valadas.
     */
    private void updateDados(Manifesto manifesto, String observacao) {
        /** Totalizadores Manifesto referente a seus Conhecimentos */
        BigDecimal vlTotalManifestoEmissao = BigDecimalUtils.ZERO;
        BigDecimal psTotalManifestoEmissao = BigDecimalUtils.ZERO;
        Integer qtTotalVolumesEmissao = IntegerUtils.ZERO;
        BigDecimal vlTotalFreteCifEmissao = BigDecimalUtils.ZERO;
        BigDecimal vlTotalFreteFobEmissao = BigDecimalUtils.ZERO;
        BigDecimal vlTotalFreteEmissao = BigDecimalUtils.ZERO;

        List<PreManifestoDocumento> documentos =
                preManifestoDocumentoService.findPreManifestoDocumentos(manifesto.getIdManifesto(),
                        ConstantesExpedicao.CONHECIMENTO_NACIONAL,
                        ConstantesExpedicao.CONHECIMENTO_ELETRONICO,
                        ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA);

        ManifestoViagemNacional manifestoViagemNacional = carregarMVN(manifesto);

        manifestoViagemNacional.setFilial(manifesto.getFilialByIdFilialOrigem());
        /** Seta nrManifestoOrigem */
        emitirDocumentoService.generateProximoNumero(manifestoViagemNacional);

        /** Busca Doctos relacionados ao Pre-Manifesto */
        manifestoViagemNacional.setNrCto(documentos.size());
        manifestoViagemNacional.setObManifestoViagemNacional(observacao);

        /** Grava manifestoViagemNacional */
        manifestoService.storeManifesto(manifesto);

        for (PreManifestoDocumento preManifestoDocumento : documentos) {

            Long idConhecimento = preManifestoDocumento.getDoctoServico().getIdDoctoServico();
            ManifestoNacionalCto manifestoNacionalCto = verificarSeConhecimentoJaFoiAdicionado(manifesto, idConhecimento);
            Conhecimento conhecimento;

            /** Verifica se o conhecimento já foi previamente adicionado ao manifesto **/
            if (manifestoNacionalCto == null) {
                manifestoNacionalCto = new ManifestoNacionalCto();

                /** Carrega POJO para inclusao */
                manifestoNacionalCto.setManifestoViagemNacional(manifestoViagemNacional);

                DomainValue tpFrete = conhecimentoService.findTpFreteByIdConhecimento(idConhecimento);
                conhecimento = new Conhecimento();
                conhecimento.setIdDoctoServico(idConhecimento);
                manifestoNacionalCto.setConhecimento(conhecimento);
                manifestoNacionalCto.setBlGeraFronteiraRapida(Boolean.TRUE);

                /** Totalizadores */
                vlTotalManifestoEmissao = BigDecimalUtils.add(vlTotalManifestoEmissao, preManifestoDocumento.getDoctoServico().getVlMercadoria());
                psTotalManifestoEmissao = BigDecimalUtils.add(psTotalManifestoEmissao, preManifestoDocumento.getDoctoServico().getPsReal());
                qtTotalVolumesEmissao = IntegerUtils.addNull(qtTotalVolumesEmissao, preManifestoDocumento.getDoctoServico().getQtVolumes());
                /** Tipo de Frete */
                BigDecimal vlTotalDocServico = preManifestoDocumento.getDoctoServico().getVlTotalDocServico();

                if (vlTotalDocServico == null) {
                    vlTotalDocServico = new BigDecimal(0);
                }
                if (ConstantesExpedicao.TP_FRETE_CIF.equals(tpFrete.getValue())) {
                    vlTotalFreteCifEmissao = BigDecimalUtils.add(vlTotalFreteCifEmissao, vlTotalDocServico);
                } else if (ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete.getValue())) {
                    vlTotalFreteFobEmissao = BigDecimalUtils.add(vlTotalFreteFobEmissao, vlTotalDocServico);
                }
                vlTotalFreteEmissao = BigDecimalUtils.add(vlTotalFreteEmissao, vlTotalDocServico);
                /** Adiciona manifestoNacionalCto */
                manifestoViagemNacional.getManifestoNacionalCtos().add(manifestoNacionalCto);
            }

            gerarBoletoReciboManifestoService.generateFaturamentoManifestoViagem(manifesto, manifestoNacionalCto);

            adicionarListaVolumesDoctoAoMVN(manifesto, manifestoViagemNacional, preManifestoDocumento, manifestoNacionalCto);
        }

        adicionarListaVolumesSemDoctoAoMVN(manifesto, manifestoViagemNacional);

        // Geração de Recibos de Frete Carreteiro e Adiantamento
        // obs.: Essa rotina deve ser executada antes de alterar o tpStatusManifesto.
        reciboFreteCarreteiroService.generateReciboFreteCarreteiro(manifesto);

        /** Seta Totais */
        manifesto.setVlTotalManifestoEmissao(vlTotalManifestoEmissao);
        manifesto.setPsTotalManifestoEmissao(psTotalManifestoEmissao);
        manifesto.setQtTotalVolumesEmissao(qtTotalVolumesEmissao);
        manifesto.setVlTotalFreteCifEmissao(vlTotalFreteCifEmissao);
        manifesto.setVlTotalFreteFobEmissao(vlTotalFreteFobEmissao);
        manifesto.setVlTotalFreteEmissao(vlTotalFreteEmissao);
        manifesto.setDhEmissaoManifesto(JTDateTimeUtils.getDataHoraAtual().toDateTime(manifesto.getFilialByIdFilialOrigem().getDateTimeZone()));
        manifesto.setTpStatusManifesto(new DomainValue(ConstantesEntrega.STATUS_MANIFESTO_EMITIDO));

        // Atualiza manifesto
        manifestoService.storeManifesto(manifesto);

        // Inclui evento de manifesto emitido
        eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), ConstantesEntrega.EVENTO_MANIFESTO_EMITIDO);

        // Gera Evento Manifesto Emitido
        if (!documentos.isEmpty()) {
            incluirEventosRastreabilidadeInternacionalService.insereEventos(
                    ConstantesEntrega.MANIFESTO_VIAGEM,
                    ExpedicaoUtils.formatManifestoViagemNacional(manifestoViagemNacional.getFilial().getSgFilial(), manifestoViagemNacional.getNrManifestoOrigem(), 8),
                    ConstantesSim.EVENTO_MANIFESTO_EMITIDO,
                    manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                    JTDateTimeUtils.getDataHoraAtual().toDateTime(manifesto.getFilialByIdFilialOrigem().getDateTimeZone()),
                    null,
                    null
            );
        }
    }

    private ManifestoViagemNacional carregarMVN(Manifesto manifesto) {
        ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();
        if (manifestoViagemNacional == null) {
            manifestoViagemNacional = new ManifestoViagemNacional();
            manifestoViagemNacional.setFilial(manifesto.getFilialByIdFilialOrigem());
            manifesto.setManifestoViagemNacional(manifestoViagemNacional);
            manifestoViagemNacional.setManifesto(manifesto);
            manifestoViagemNacional.setManifestoNacionalVolumes(new ArrayList<ManifestoNacionalVolume>());
        }
        return manifestoViagemNacional;
    }

    private void adicionarListaVolumesSemDoctoAoMVN(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional) {
        List<PreManifestoVolume> listaVolumesSemDocto = preManifestoVolumeService.findVolumesSemPreManifDoctoByManifesto(manifesto.getIdManifesto());
        for (PreManifestoVolume preManifestoVolume : listaVolumesSemDocto) {
            ManifestoNacionalVolume mnv = new ManifestoNacionalVolume();
            mnv.setManifestoViagemNacional(manifestoViagemNacional);
            mnv.setVolumeNotaFiscal(preManifestoVolume.getVolumeNotaFiscal());
            Conhecimento ctrc = new Conhecimento();
            ctrc.setIdDoctoServico(preManifestoVolume.getDoctoServico().getIdDoctoServico()); //LMS-627
            mnv.setConhecimento(ctrc);
            manifestoViagemNacional.getManifestoNacionalVolumes().add(mnv);
        }
    }

    private void adicionarListaVolumesDoctoAoMVN(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, PreManifestoDocumento preManifestoDocumento, ManifestoNacionalCto manifestoNacionalCto) {
        List<PreManifestoVolume> listaVolumesDocto = preManifestoVolumeService.findByManifestoDoctoServico(manifesto.getIdManifesto(), preManifestoDocumento.getDoctoServico().getIdDoctoServico());
        for (PreManifestoVolume preManifestoVolume : listaVolumesDocto) {
            ManifestoNacionalVolume mnv = new ManifestoNacionalVolume();
            mnv.setManifestoViagemNacional(manifestoViagemNacional);
            mnv.setVolumeNotaFiscal(preManifestoVolume.getVolumeNotaFiscal());
            mnv.setConhecimento(manifestoNacionalCto.getConhecimento());
            mnv.setManifestoNacionalCto(manifestoNacionalCto);
            manifestoViagemNacional.getManifestoNacionalVolumes().add(mnv);
        }
    }

    private ManifestoNacionalCto verificarSeConhecimentoJaFoiAdicionado(Manifesto manifesto, Long idConhecimento) {
        if (manifesto.getManifestoViagemNacional().getManifestoNacionalCtos() == null) {
            manifesto.getManifestoViagemNacional().setManifestoNacionalCtos(new ArrayList<ManifestoNacionalCto>());
        }
        for (ManifestoNacionalCto manifestoNacionalCto : manifesto.getManifestoViagemNacional().getManifestoNacionalCtos()) {
            if (manifestoNacionalCto.getConhecimento().getIdDoctoServico().equals(idConhecimento)){
                return manifestoNacionalCto;
            }
        }

        return null;
    }

    public void setGerarMVNService(GerarMVNService gerarMVNService) {
        this.gerarMVNService = gerarMVNService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
        this.emitirDocumentoService = emitirDocumentoService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setGerarBoletoReciboManifestoService(GerarBoletoReciboManifestoService gerarBoletoReciboManifestoService) {
        this.gerarBoletoReciboManifestoService = gerarBoletoReciboManifestoService;
    }

    public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
        this.eventoManifestoService = eventoManifestoService;
    }

    public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
        this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
    }

    public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public ContatoService getContatoService() {
        return contatoService;
    }

    public void setContatoService(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

}