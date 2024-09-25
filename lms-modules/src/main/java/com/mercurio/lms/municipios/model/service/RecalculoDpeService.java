package com.mercurio.lms.municipios.model.service;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoNaturaDMN;
import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.NovoDpeDoctoServico;
import com.mercurio.lms.expedicao.model.service.AbstractDpeService;
import com.mercurio.lms.expedicao.model.service.NovoDpeDoctoServicoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author Tiago Rosa
 *
 * @spring.bean id="lms.municipios.recalculoDpeService"
 */
@Assynchronous
public class RecalculoDpeService extends AbstractDpeService {

    private IntegracaoJmsService integracaoJmsService;

    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private NovoDpeDoctoServicoService novoDpeDoctoServicoService;
    private PessoaService pessoaService;
    private ClienteService clienteService;
    private EventoDocumentoServicoService eventoDocumentoServicoService; 
    
    private PpeService ppeService;
    private MunicipioService municipioService;
    private FeriadoService feriadoService;
    private EnderecoPessoaService enderecoPessoaService;

    private ParametroGeralService parametroGeralService;

    private static final Long MODAL = 1l;
    private static final String DT_PRAZO_REENTREGA = "dtPrazoReentrega";    
    private static final Long EVENT_TYPE = 42L;
    private static final String CARRIER_EVENT_DESCRIPTION = "NOVA PREVISAO DE ENTREGA";

    @AssynchronousMethod(name = "calculaNovoDpeService.executarHorario",
            type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
    public void executeHorario() {
        List<Map<String, Object>> resultList = manifestoEntregaDocumentoService.findRecalcularDpeHorario();
        
        for (Map<String, Object> resultMap : resultList) {
            recalculaDpeEnviaNatura(
                    (Long) resultMap.get("idDoctoServico"), 
                    (Long) resultMap.get("idPessoaOrigem"), 
                    (Long) resultMap.get("idPessoaDestino")
            );
        }
    }

    private void recalculaDpeEnviaNatura(Long idDoctoServico, Long idFilial, Long idCliente) {
        final Pessoa pessoaOrigem = pessoaService.findById(idFilial);
        final Cliente cliente = clienteService.findById(idCliente);
        final Pessoa pessoaDestino = cliente.getPessoa();
        final EnderecoPessoa origemEnderecoPessoa = pessoaOrigem.getEnderecoPessoa();
        final Municipio origemMunicipio = origemEnderecoPessoa.getMunicipio();
        final EnderecoPessoa destinoEnderecoPessoa = pessoaDestino.getEnderecoPessoa();
        final Municipio destinoMunicipio = destinoEnderecoPessoa.getMunicipio();

        YearMonthDay dtPrazoReentrega = (YearMonthDay) executeCalculoDPE(cliente,
                cliente,
                cliente,
                null,
                null,
                null,
                MODAL,
                origemMunicipio.getIdMunicipio(),
                null,
                idFilial,
                destinoMunicipio.getIdMunicipio(),
                origemEnderecoPessoa.getNrCep(),
                destinoEnderecoPessoa.getNrCep(),
                null).get(DT_PRAZO_REENTREGA);
        NovoDpeDoctoServico ndds = novoDpeDoctoServicoService.findByIdDoctoServico(idDoctoServico);
        if (ndds == null) {
            ndds = new NovoDpeDoctoServico();
            ndds.setIdDoctoServico(idDoctoServico);
        }
        ndds.setBlAtendido(Boolean.FALSE);
        ndds.setNovoDtPrevEntrega(dtPrazoReentrega);
        EventoDocumentoServicoDMN evento = new EventoDocumentoServicoDMN();
        evento.setIdDoctoServico(idDoctoServico);
        evento.setCdOcorrenciaEntrega(EVENT_TYPE.shortValue());
        evento.setDhEvento(null);
        List<EventoDocumentoServicoNaturaDMN> eventos = eventoDocumentoServicoService.findAndFillEventoDocumentoServicoNaturaDMN(evento);
        for(EventoDocumentoServicoNaturaDMN dmn : eventos){
            dmn.setDhEvento(JTDateTimeUtils.yearMonthDayToDateTime(dtPrazoReentrega));
            dmn.setCdTipoOcorrenciaCliente(EVENT_TYPE);
            dmn.setCdLocalizacaoMercadoria(EVENT_TYPE.shortValue());
            dmn.setCdOcorrenciaEntrega(EVENT_TYPE.shortValue());
//            dmn.setCdOcorrenciaPendencia(EVENT_TYPE.shortValue());
            dmn.setDsLocalizacaoMercadoria(CARRIER_EVENT_DESCRIPTION);
            dmn.setDsOcorrenciaEntrega(CARRIER_EVENT_DESCRIPTION);
//            dmn.setDsOcorrenciaPendencia(CARRIER_EVENT_DESCRIPTION);
            IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.RECALCULO_DPE, dmn);
            novoDpeDoctoServicoService.store(ndds);
            integracaoJmsService.storeMessage(jmsMessageSender);
        }
    }

    @AssynchronousMethod(name = "calculaNovoDpeService.executarDiario",
            type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
    public void executeDiario() {
        Integer diasEmissaoDoctoServico = Integer.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("RECALCULO_DPE_DIARIO_DIAS_EMISSAO_DOCTO_SERVICO"));
        List<Map<String, Object>> resultList = manifestoEntregaDocumentoService.findRecalcularDpeDiario(diasEmissaoDoctoServico);
        
        for (Map<String, Object> resultMap : resultList) {
            recalculaDpeEnviaNatura(
                    (Long) resultMap.get("idDoctoServico"),
                    (Long) resultMap.get("idPessoaOrigem"), 
                    (Long) resultMap.get("idPessoaDestino")
            );
        }
    }

    @Override
    protected RestricaoRota restricoesRotaOrigem(Long idMunicipioOrigem, Long idServico, String nrCepOrigem, Long idFilialOrigem) {
        /**
         * Para este recalculo não deve ser considerado restricoesRotaOrigem
         */
        return null;
    }

    @Override
    protected RestricaoRota restricoesDestino(Map<String, Object> dadosEnderecoDestino, Long idMunicipioDestino, Long idServico, Long idFilialDestino) {
        /**
         * Para este recalculo não deve ser considerado restricoesDestino
         */
        return null;
    }

    @Override
    protected PrazoEntregaCliente calculaPrazoEspecial(Cliente clienteRemetente, Cliente clienteDevedor, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino, Cliente clienteDestinatario) {
        /**
         * Para este recalculo não deve ser considerado prazo especial
         */
        return null;
    }

    @Override
    protected Long addHorarioCorteCliente(Cliente clienteRemetente, Long idServico, RestricaoRota restricaoRotaOrigem, Long idMunicipioOrigem, Map<String, Object> dadosEnderecoDestino, Long idFilialOrigem, Filial filialOrigem, TimeOfDay hrEmissao, Long nrPrazo) {
        return nrPrazo;
    }

    @Override
    protected Set<String> findFeriadosFilialOrigem(Filial filialOrigem, Long idMunicipioOrigem) {
        return Collections.EMPTY_SET;
    }

    @Override
    protected Filial findFilialOrigem(Long idFilialOrigem) {
        return null;
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public void setNovoDpeDoctoServicoService(NovoDpeDoctoServicoService novoDpeDoctoServicoService) {
        this.novoDpeDoctoServicoService = novoDpeDoctoServicoService;
    }

    public void setPessoaService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public void setPpeService(PpeService ppeService) {
        this.ppeService = ppeService;
    }

    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    public void setFeriadoService(FeriadoService feriadoService) {
        this.feriadoService = feriadoService;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    @Override
    public PpeService getPpeService() {
        return ppeService;
    }

    @Override
    public MunicipioService getMunicipioService() {
        return municipioService;
    }

    @Override
    public FeriadoService getFeriadoService() {
        return feriadoService;
    }

    @Override
    public EnderecoPessoaService getEnderecoPessoaService() {
        return enderecoPessoaService;
    }

    public ParametroGeralService getParametroGeralService() {
        return parametroGeralService;
}

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

}
