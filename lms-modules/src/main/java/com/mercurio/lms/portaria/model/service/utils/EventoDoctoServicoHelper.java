package com.mercurio.lms.portaria.model.service.utils;

import com.mercurio.lms.sim.model.EventoDocumentoServico;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.sim.StatusLinhaTempoRastreabilidade;

public class EventoDoctoServicoHelper {

    public static final Short CD_OCORRENCIA_ENTREGA_REALIZADA_NORMALMENTE = 1;

    private EventoDoctoServicoHelper() {
        throw new AssertionError();
    }

    public static EventoDocumentoServicoDMN convertEventoDoctoServico(EventoDocumentoServico eventoDocumentoServico) {
        EventoDocumentoServicoDMN dto = new EventoDocumentoServicoDMN();
        dto.setIdEventoDocumentoServico(eventoDocumentoServico.getIdEventoDocumentoServico());
        dto.setDhEvento(eventoDocumentoServico.getDhEvento());

        if (eventoDocumentoServico.getDoctoServico() != null) {
            dto.setIdDoctoServico(eventoDocumentoServico.getDoctoServico().getIdDoctoServico());
            dto.setNrDoctoServico(eventoDocumentoServico.getDoctoServico().getNrDoctoServico());
            dto.setIdFilialOrigem(eventoDocumentoServico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
            dto.setSgFilialOrigem(eventoDocumentoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
            if (eventoDocumentoServico.getDoctoServico().getFilialByIdFilialDestino() != null) {
                dto.setIdFilialDestino(eventoDocumentoServico.getDoctoServico().getFilialByIdFilialDestino().getIdFilial());
                dto.setSgFilialDestino(eventoDocumentoServico.getDoctoServico().getFilialByIdFilialDestino().getSgFilial());
            }
        }

        if (eventoDocumentoServico.getFilial() != null) {
            dto.setIdFilialEvento(eventoDocumentoServico.getFilial().getIdFilial());
            dto.setSgFilialEvento(eventoDocumentoServico.getFilial().getSgFilial());
        }

        setInformacaoEvento(eventoDocumentoServico, dto);
        setInformacaoPendencia(eventoDocumentoServico, dto);

        if (eventoDocumentoServico.getBlEventoCancelado() != null) {
            dto.setBlEventoCancelado(eventoDocumentoServico.getBlEventoCancelado());
        }

        try {
            if (eventoDocumentoServico.getDoctoServico().getDevedorDocServs() != null &&
                !eventoDocumentoServico.getDoctoServico().getDevedorDocServs().isEmpty() &&
                eventoDocumentoServico.getDoctoServico().getDevedorDocServs().get(0) != null) {
                    dto.setNrIdentClienteDevedor(eventoDocumentoServico.getDoctoServico().getDevedorDocServs().get(0)
                        .getCliente().getPessoa().getNrIdentificacao());
            }
        } catch (Exception ignored) {}

        return dto;
    }

    private static void setInformacaoPendencia(EventoDocumentoServico eventoDocumentoServico, EventoDocumentoServicoDMN dto) {
        if (eventoDocumentoServico.getOcorrenciaEntrega() != null) {
            dto.setCdOcorrenciaEntrega(eventoDocumentoServico.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
            if (eventoDocumentoServico.getOcorrenciaEntrega().getDsOcorrenciaEntrega() != null) {
                dto.setDsOcorrenciaEntrega(eventoDocumentoServico.getOcorrenciaEntrega().getDsOcorrenciaEntrega().getValue());
            }
        }

        if (eventoDocumentoServico.getOcorrenciaPendencia() != null) {
            dto.setCdOcorrenciaPendencia(eventoDocumentoServico.getOcorrenciaPendencia().getCdOcorrencia());
            if (eventoDocumentoServico.getOcorrenciaPendencia().getDsOcorrencia() != null) {
                dto.setDsOcorrenciaPendencia(eventoDocumentoServico.getOcorrenciaPendencia().getDsOcorrencia().getValue());
            }
        }
    }

    private static void setInformacaoEvento(EventoDocumentoServico eventoDocumentoServico, EventoDocumentoServicoDMN dto) {
        if (eventoDocumentoServico.getEvento() != null) {
            dto.setCdEvento(eventoDocumentoServico.getEvento().getCdEvento());
            dto.setDsEvento(eventoDocumentoServico.getEvento().getDsEvento());
            dto.setTpEvento(eventoDocumentoServico.getEvento().getTpEvento().getValue());
            dto.setTpSituacaoEvento(eventoDocumentoServico.getEvento().getTpSituacao().getValue());
            dto.setBlExibeCliente(eventoDocumentoServico.getEvento().getBlExibeCliente());

            if (eventoDocumentoServico.getEvento().getLocalizacaoMercadoria() != null) {
                dto.setCdLocalizacaoMercadoria(eventoDocumentoServico.getEvento().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
                dto.setDsLocalizacaoMercadoria(eventoDocumentoServico.getEvento().getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue());
                dto.setBlArmazenagem(eventoDocumentoServico.getEvento().getLocalizacaoMercadoria().getBlArmazenagem());
            }
        }
    }

    public static StatusLinhaTempoRastreabilidade getLinhaTempo(EventoDocumentoServicoDMN domain) {

        if (isFinalizado(domain)) {
            return StatusLinhaTempoRastreabilidade.FINALIZADO;
        }
        if (isEmRotaEntrega(domain)) {
            return StatusLinhaTempoRastreabilidade.EM_ROTA_ENTREGA;
        }
        if (isEmFilialDestino(domain)) {
            return StatusLinhaTempoRastreabilidade.EM_FILIAL_DESTINO;
        }
        if (isEmTransito(domain)) {
            return StatusLinhaTempoRastreabilidade.EM_TRANSITO;
        }
        if (isEmColeta(domain)) {
            return StatusLinhaTempoRastreabilidade.EM_COLETA;
        }
        return StatusLinhaTempoRastreabilidade.NENHUMA;
    }

    private static boolean isFinalizado(EventoDocumentoServicoDMN domain) {
        return CD_OCORRENCIA_ENTREGA_REALIZADA_NORMALMENTE.equals(domain.getCdOcorrenciaEntrega())
                && StatusLinhaTempoRastreabilidade.FINALIZADO.getCdEvento().equals(domain.getCdEvento());
    }

    private static Boolean isEmRotaEntrega(EventoDocumentoServicoDMN domain) {
        return StatusLinhaTempoRastreabilidade.EM_ROTA_ENTREGA.getCdEvento().equals(domain.getCdEvento());
    }

    private static Boolean isEmFilialDestino(EventoDocumentoServicoDMN domain) {
        return domain.getIdFilialEvento().equals(domain.getIdFilialDestino())
                && StatusLinhaTempoRastreabilidade.EM_FILIAL_DESTINO.getCdEvento().equals(domain.getCdEvento());
    }

    private static Boolean isEmTransito(EventoDocumentoServicoDMN domain) {
        return domain.getIdFilialEvento().equals(domain.getIdFilialOrigem())
                && StatusLinhaTempoRastreabilidade.EM_TRANSITO.getCdEvento().equals(domain.getCdEvento());
    }

    private static Boolean isEmColeta(EventoDocumentoServicoDMN domain) {
        return StatusLinhaTempoRastreabilidade.EM_COLETA.getCdEvento().equals(domain.getCdEvento());
    }
}
