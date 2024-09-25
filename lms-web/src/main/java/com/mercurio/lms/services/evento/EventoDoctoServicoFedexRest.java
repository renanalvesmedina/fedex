package com.mercurio.lms.services.evento;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.endToEnd.EEnvioEventoDocumentoServico;
import br.com.tntbrasil.integracao.domains.endToEnd.EEventoDocumentoServico;
import br.com.tntbrasil.integracao.domains.fedex.BloqueioLiberacaoAgendamentoFedexDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexDTO;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

@Path("evento/doctoservico/integracaoFedexEntrega") 
public class EventoDoctoServicoFedexRest extends BaseRest{

		@InjectInJersey 
		MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
		
		@InjectInJersey 
		ConfiguracoesFacade configuracoesFacade;
		
		@InjectInJersey 
		DoctoServicoService doctoServicoService;
		
		@InjectInJersey 
		OcorrenciaEntregaService ocorrenciaEntregaService;
		
		@InjectInJersey
		private EventoDocumentoServicoService eventoDocumentoServicoService;
		
		private static final String RETORNO_OK = "OK";
		private static final Logger LOGGER = LogManager.getLogger(EventoDoctoServicoFedexRest.class);
		private static final String PARAMETRO_GERAL_TEMPO_ESPERA_NOTFIS_FEDEX = "TEMPO_ESPERA_NOTFIS_FEDEX";
		
		@POST
		@Path("buscaDadosFedexEntrega")
		public Response buscaDadosFedexEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
			monitoramentoDocEletronicoService.executeEnvioNotFisFedex(eventoDocumentoServicoDMN);
			
			return Response.ok().build();
		}
		
		@POST
        @Path("enriqueceOcorrenciaEntrega")
        public Response enriqueceOcorrenciaEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
		    OcorrenciaFedexDTO dto = monitoramentoDocEletronicoService.executeEnriqueceOcorrenciaEntrega(eventoDocumentoServicoDMN);
            return Response.ok(dto).build();
        }
		
		@POST
		@Path("buscarDadosEventoRPS")
		public Response buscarDadosEventoRPS() {
		    monitoramentoDocEletronicoService.executeMonitoramentoDocEletronicoEmitidosAutorizados();
		    return Response.ok().build();
		}
		
		@POST
        @Path("enriqueceBloqueioLiberacaoAgendamento")
        public Response enriqueceBloqueioLiberacaoAgendamento(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
		    BloqueioLiberacaoAgendamentoFedexDTO dto = monitoramentoDocEletronicoService.executeEnriqueceBloqueioLiberacaoAgendamento(eventoDocumentoServicoDMN);
            return Response.ok(dto).build();
        }
		
		@POST
		@Path("envioNotFis")
		public Response envioNotFis(EEnvioEventoDocumentoServico eEnvioEventoDocumentoServico) {
			List<String> erros = new ArrayList<String>();
			eEnvioEventoDocumentoServico.setDataHoraRecebimento(new DateTime());
	
			try {
				monitoramentoDocEletronicoService.executeEnvioNotFisFedex(buildEventoDocumentoServicoDMN(eEnvioEventoDocumentoServico.getDados()));
				eEnvioEventoDocumentoServico.setStatus(RETORNO_OK);
			} catch (BusinessException be) {
				erros.add(configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
				LOGGER.error(be);
			} catch (Exception ex) {
				if (ex.getMessage() != null) {
					erros.add(ex.getMessage());
				} else {
					erros.add(ex.getClass().toString());
				}
				LOGGER.error(ex);
			}
	
			eEnvioEventoDocumentoServico.setListErros(erros);
	
			return Response.ok(eEnvioEventoDocumentoServico).build();
		}
		
		@POST
		@Path("tempoFiltroNotfis")
		public Response tempoFiltroNotfis(EEnvioEventoDocumentoServico eEnvioEventoDocumentoServico) {
			List<String> erros = new ArrayList<String>();
			eEnvioEventoDocumentoServico.setDataHoraRecebimento(new DateTime());
			EEventoDocumentoServico eEventoDocumentoServico = eEnvioEventoDocumentoServico.getDados();
			
			try {
				eEventoDocumentoServico.setBlEnviaFilaTimer(monitoramentoDocEletronicoService.findBlEnviaFilaTimerNotfis(eEventoDocumentoServico.getIdDoctoServico()));
				eEventoDocumentoServico.setTempoDelay(configuracoesFacade.getValorParametro(PARAMETRO_GERAL_TEMPO_ESPERA_NOTFIS_FEDEX).toString());
				eEnvioEventoDocumentoServico.setDados(eEventoDocumentoServico);
				eEnvioEventoDocumentoServico.setStatus(RETORNO_OK);
			} catch (BusinessException be) {
				erros.add(configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
				LOGGER.error(be);
			} catch (Exception ex) {
				if (ex.getMessage() != null) {
					erros.add(ex.getMessage());
				} else {
					erros.add(ex.getClass().toString());
				}
				LOGGER.error(ex);
			}
	
			eEnvioEventoDocumentoServico.setListErros(erros);
			return Response.ok(eEnvioEventoDocumentoServico).build();
		}

		public EventoDocumentoServicoDMN buildEventoDocumentoServicoDMN(EEventoDocumentoServico eEventoDocumentoServico){
			
			DoctoServico doctoServico = doctoServicoService.findById(eEventoDocumentoServico.getIdDoctoServico());
			
			EventoDocumentoServicoDMN eventoDocumentoServicoDMN = new EventoDocumentoServicoDMN();
			eventoDocumentoServicoDMN.setIdDoctoServico(eEventoDocumentoServico.getIdDoctoServico());
			eventoDocumentoServicoDMN.setCdEvento(eEventoDocumentoServico.getCdEvento());
			eventoDocumentoServicoDMN.setIdFilialEvento(eEventoDocumentoServico.getIdFilial());
			eventoDocumentoServicoDMN.setIdFilialOrigem(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			eventoDocumentoServicoDMN.setIdFilialDestino(doctoServico.getFilialByIdFilialDestino().getIdFilial());
			
			if (null != eEventoDocumentoServico.getIdOcorrenciaEntrega()) {
				OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntregaById(eEventoDocumentoServico.getIdOcorrenciaEntrega());
				eventoDocumentoServicoDMN.setCdOcorrenciaEntrega(ocorrenciaEntrega.getCdOcorrenciaEntrega());
			}
			
		    return eventoDocumentoServicoDMN;
		}
		
	} 
