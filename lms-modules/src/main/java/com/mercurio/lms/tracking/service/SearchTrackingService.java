package com.mercurio.lms.tracking.service;

import br.com.tntbrasil.integracao.domains.sim.StatusLinhaTempoRastreabilidade;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.tracking.*;
import com.mercurio.lms.tracking.util.AliasTracking;
import com.mercurio.lms.tracking.util.CalculoDirecao;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;
import com.mercurio.lms.util.JTFormatUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Classe de serviï¿½o para tracking:   
 *
 * 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviï¿½o.
 * @spring.bean id="lms.tracking.searchTrackingService"
 */
@ServiceSecurity
public class SearchTrackingService {

	private static  final String CNPJ_INI = "01310212";
	private static  final int DOIS = 2;
	private static  final int TRES = 3;
	
	private ConhecimentoService conhecimentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private EventoColetaService eventoColetaService;
	private DevedorDocServService devedorDocServService;
	private DoctoServicoService doctoServicoService;
	private List<EventoDocumentoServico> eventoDocumentoServicoList = null;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private LinhaTempoService linhaTempoService;
	
	@MethodSecurity(processGroup = "tracking.searchTrackingService", processName = "searchConsignment", authenticationRequired = false)
	public Map<String, Object> searchConsignment(TypedFlatMap tfm) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		Conhecimento conhecimento = null;
		
		
		/**
		 * LMS-3854 - essa validação está chumbada no código por solitação da BS, pois o LMS ainda não está¡
		 * preparado para a partir do cadastro filtrar se os dados do cliente devem ou não estar disponiveis na Internet.
		 */
		Boolean permiteConsulta = (!tfm.containsKey("cnpj") || StringUtils.isBlank(tfm.getString("cnpj")) || !tfm.getString("cnpj").startsWith(CNPJ_INI));

		Long idConhecimento = null;
		Long nrDoctoServico = null;
		
		if(permiteConsulta){
			String nrIdentificacao = "";
	        if ( !StringUtils.isBlank(tfm.getString("cpf")) ) {
	        	nrIdentificacao = tfm.getString("cpf");
	        } else if( !StringUtils.isBlank(tfm.getString("cnpj")) ){
	        	nrIdentificacao = tfm.getString("cnpj");
	        }
			
			Integer destRmt = tfm.getInteger("destRmt");
			
			if (tfm.containsKey("ctrc")) {
				
				nrDoctoServico = tfm.getLong("ctrc");
				idConhecimento = doctoServicoService.findIdDoctoServicoByNrDoctoServico(nrDoctoServico, nrIdentificacao, destRmt);
				
			} else if (tfm.containsKey("notaFiscal")) {
				Integer nf = tfm.getInteger("notaFiscal");
			
				idConhecimento = notaFiscalConhecimentoService.findNFConhecimentoByNrNotaFiscal(nf, destRmt, nrIdentificacao);
			} else if (tfm.containsKey("idDoctoServico")) {
				idConhecimento = tfm.getLong("idDoctoServico");
			}
					
			if (idConhecimento != null) {
				conhecimento = conhecimentoService.findByIdJoinLocalizacaoMercadoria(idConhecimento);
					
				if( !permiteConsulta(conhecimento) ){
					conhecimento = null;
				}else{
					eventoDocumentoServicoList = eventoDocumentoServicoService.findByDocumentoServico(idConhecimento, false, false);
				}
			}
			
		}
		
		if (conhecimento != null) {	
			Consignment consignment = createConsignment(conhecimento);
			Events events = findEventos();
			Invoices invoices = findNotasFiscais(conhecimento.getIdDoctoServico());
			Collections.sort(invoices.getInvoices());
			
			Schedulings schedulings = findAgendamentoDoctoServico(conhecimento.getIdDoctoServico());

			Element element = new Element();
			element.setConsignment(consignment);
			element.setListEvent(events);
			element.setListInvoice(invoices);
			
			if(schedulings != null ){
				element.setListSchedulings(schedulings);
			}
			
			Map<String, Object> dadosLocalizacao = conhecimentoService.findDadosLocalizacaoDoctoServico(idConhecimento);
			DeliveryLocation deliveryLocation = getDeliveryLocation(idConhecimento, nrDoctoServico, dadosLocalizacao);
			deliveryLocation.setDirection( getDirecaoRota(deliveryLocation) );
			
			element.setDeliveryLocation(deliveryLocation);
			
			XStream xStream = AliasTracking.createAlias();
			String xml = xStream.toXML(element);
			
			data.put("xml", xml);
			data.put("isXml", true);	
		}
		
		return data;
	}

	
	private String getDirecaoRota(DeliveryLocation deliveryLocation) {
		CalculoDirecao.Direcao direcao = CalculoDirecao.calculaDirecao( deliveryLocation.getCurrent().getNrLatitude().doubleValue(), deliveryLocation.getCurrent().getNrLongitude().doubleValue(),
				deliveryLocation.getDestination().getNrLatitude().doubleValue(), deliveryLocation.getDestination().getNrLongitude().doubleValue() );
		return direcao.toString();
	}
	
	private String findStatus(Long idDoctoServico) {
		StatusLinhaTempoRastreabilidade status = linhaTempoService.findLinhaTempoEvento(idDoctoServico);
		
		return status.toString();
	}
	
	private DeliveryLocation getDeliveryLocation(Long idDoctoServico, Long nrDoctoServico, Map<String, Object> dadosLocalizacao ) {
		DeliveryLocation delivery = new DeliveryLocation();

		
		delivery.setStatus(findStatus(idDoctoServico));
		delivery.setCurrent(createCurrent(dadosLocalizacao));
		delivery.setIdDoctoServico(idDoctoServico);

		Depot origin = null;
		Depot destination = null;

		if (dadosLocalizacao != null) {
			origin = new Depot();
			origin.setCode((Long)dadosLocalizacao.get("ID_FILIAL_ORIGEM"));
			origin.setAcronym((String)dadosLocalizacao.get("SG_FILIAL_ORIGEM"));
			origin.setDescription((String)dadosLocalizacao.get("NM_FILIAL_ORIGEM"));
			origin.setNrLatitude((BigDecimal)dadosLocalizacao.get("LATITUDE_ORIGEM"));
			origin.setNrLongitude((BigDecimal)dadosLocalizacao.get("LONGITUDE_ORIGEM"));

			destination = new Depot();
			destination.setCode((Long)dadosLocalizacao.get("ID_FILIAL_DESTINO"));
			destination.setAcronym((String)dadosLocalizacao.get("SG_FILIAL_DESTINO"));
			destination.setDescription((String)dadosLocalizacao.get("NM_FILIAL_DESTINO"));
			destination.setNrLatitude((BigDecimal)dadosLocalizacao.get("LATITUDE_DESTINO"));
			destination.setNrLongitude((BigDecimal)dadosLocalizacao.get("LONGITUDE_DESTINO"));
		}

		delivery.setOrigin(origin);
		delivery.setDestination(destination);

		delivery.setCtrc(nrDoctoServico);

		delivery.setEvent(getEvent(idDoctoServico));

		return delivery;
	}

	private Depot createCurrent(Map<String, Object> dadosLocalizacao) {
		if (dadosLocalizacao != null) {
			Depot current = new Depot();
			current.setCode((Long)dadosLocalizacao.get("ID_FILIAL_ATUAL"));
			current.setAcronym((String)dadosLocalizacao.get("SG_FILIAL_ATUAL"));
			current.setDescription((String)dadosLocalizacao.get("NM_FANTASIA_ATUAL"));
			current.setNrLatitude((BigDecimal)dadosLocalizacao.get("LATITUDE_ATUAL"));
			current.setNrLongitude((BigDecimal)dadosLocalizacao.get("LONGITUDE_ATUAL"));
			return current;
		}
		return null;
	}



	private Event getEvent(Long idDoctoServico) {
		Object[] currentEvent = eventoDocumentoServicoService.findInfoEventoAtualDoctoByIdDoctoServico(idDoctoServico);
		if(currentEvent != null && currentEvent.length > 0){
			Event event = new Event();
			event.setDescription((String) currentEvent[DOIS]);
		}
		return null;
	}
	
	/**
	 * LMS-3854 - essa validaÃ§Ã£o estÃ¡ chumbada no cÃ³digo por solitaÃ§Ã£o da BS, pois o LMS ainda nÃ£o estÃ¡
	 * preparado para a partir do cadastro filtrar se os dados do cliente devem ou nÃ£o estar disponiveis na Internet.
	 */
	private Boolean permiteConsulta(Conhecimento conhecimento){
		
		if( conhecimento.getClienteByIdClienteDestinatario() != null 
				&& conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao().startsWith(CNPJ_INI) ){
			return false;
		}
		
		if( conhecimento.getClienteByIdClienteRemetente() != null 
				&& conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao().startsWith(CNPJ_INI) ){
			return false;
		}
		
		DevedorDocServ devedor = devedorDocServService.findDevedorByDoctoServico(conhecimento.getIdDoctoServico());
		if( devedor != null && devedor.getCliente().getPessoa().getNrIdentificacao().startsWith(CNPJ_INI) ){
			return false;
		}
		return true;
	}
		
	private Consignment createConsignment(Conhecimento conhecimento){
		String modalRodoviario = "R";
		
		Consignment consignment = new Consignment();
		consignment.setNumber( conhecimento.getFilialByIdFilialOrigem().getSgFilial()
				.concat("-")
				.concat(String.valueOf(conhecimento.getNrConhecimento())) );
		consignment.setEstimatedDateDelivery( conhecimento.getDtPrevEntrega().toString(TrackingContantsUtil.FORMATO_DATA) );
		
		if(conhecimento.getServico().getTpModal() != null){
			consignment.setModal(conhecimento.getServico().getTpModal().getValue());
		}else{
			consignment.setModal(modalRodoviario);
		}
		
		
		/**
		 * Data coleta
		 */
		String pickupDate = "";
		if(conhecimento.getPedidoColeta() != null){
			DateTime dhEventoColeta = conhecimento.getPedidoColeta().getDhColetaDisponivel();
			
			if( dhEventoColeta != null ){
				pickupDate = JTFormatUtils.format(dhEventoColeta, TrackingContantsUtil.FORMATO_DATA);				
			}
		}
		consignment.setPickupDate(pickupDate);
		
		/**
		 * Data entrega LMS-4060/CLI-36
		 */
		String deliveryDate = "";
		for(EventoDocumentoServico eventoDocumentoServico : eventoDocumentoServicoList ){
			
			if( eventoDocumentoServico.getEvento() != null &&  eventoDocumentoServico.getOcorrenciaEntrega() != null 
					&& ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA == eventoDocumentoServico.getEvento().getCdEvento() 
					&& ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA == eventoDocumentoServico.getOcorrenciaEntrega().getCdOcorrenciaEntrega()){
				
				deliveryDate = JTFormatUtils.format(eventoDocumentoServico.getDhEvento(), TrackingContantsUtil.FORMATO_DATA);
		
			}
		}
		consignment.setDeliveryDate( deliveryDate );
				
		if (conhecimento.getLocalizacaoMercadoria() != null) {
			String status = conhecimento.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue();
			status += getComplementoObservacao(conhecimento.getObComplementoLocalizacao());				
			consignment.setStatus( status );
		}
		return consignment;
	}

	private Events findEventos() {

		Events events = new Events();	
	
		for(EventoDocumentoServico evento : eventoDocumentoServicoList) {
			Event event = new Event();
				
			/* Verifica se deve exibir o evento ao cliente e se ï¿½ um evento Realizado e nï¿½o Previsto */
			if(evento.getEvento().getBlExibeCliente() && ("R").equals(evento.getEvento().getTpEvento().getValue())) {
				event.setDate( JTFormatUtils.format(evento.getDhEvento(), TrackingContantsUtil.FORMATO_DATA ) );
				
				String dsEvento = "";
				if(evento.getEvento().getLocalizacaoMercadoria() != null) {
					dsEvento = evento.getEvento().getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue();
				} else if(evento.getEvento().getDsEvento() != null) {
					dsEvento = evento.getEvento().getDsEvento();
				} else if(evento.getOcorrenciaEntrega() != null) {
					dsEvento = evento.getOcorrenciaEntrega().getDsOcorrenciaEntrega().getValue();
				} else if(evento.getOcorrenciaPendencia() != null) {
					dsEvento = evento.getOcorrenciaPendencia().getDsOcorrencia().getValue();
				}
				
				if(!"".equalsIgnoreCase(dsEvento)){
					dsEvento += getComplementoObservacao(evento.getObComplemento());
					
					event.setDescription(dsEvento);
					event.setDepotCode(evento.getFilial().getSgFilial());
					event.setCdEvent(String.valueOf(evento.getEvento().getCdEvento()));
					
					events.addEvent(event);
				}
			}
		}
		
		return events;
	}
	
	/**
	 * Jira CLI-22
	 * verifica se existe agendamentos para o documento de serviï¿½o
	 * @param eventoDocumentoServicoList
	 */
	private Schedulings findAgendamentoDoctoServico(Long idDoctoServico){
		List<AgendamentoDoctoServico> agendamentoDoctoServicoList = null;
		
		agendamentoDoctoServicoList =  agendamentoDoctoServicoService.findAgendamentoByIdDoctoServicoJoinFilial(idDoctoServico);

		Schedulings schedulings = null;
		if(agendamentoDoctoServicoList != null && !agendamentoDoctoServicoList.isEmpty()){
			schedulings = new Schedulings();
			for(AgendamentoDoctoServico agendamentoDoctoServico : agendamentoDoctoServicoList){									
					Scheduling scheduling = new Scheduling();
					
					scheduling.setContactDate(agendamentoDoctoServico.getAgendamentoEntrega().getDhContato()
							.toString(TrackingContantsUtil.FORMATO_DATA));
					scheduling.setDepotCode(agendamentoDoctoServico.getAgendamentoEntrega().getFilial().getSgFilial());
					scheduling.setDepotName(agendamentoDoctoServico.getAgendamentoEntrega().getFilial().getPessoa().getNmFantasia());

					if( "TA".equalsIgnoreCase( agendamentoDoctoServico.getAgendamentoEntrega().getTpAgendamento().getValue()) ){
						scheduling.setSchedulingDate(agendamentoDoctoServico.getAgendamentoEntrega().getDhContato()
								.toString(TrackingContantsUtil.FORMATO_DATA));
						
						scheduling.setDescription(TrackingContantsUtil.TENTATIVA_AGENDAMENTO + " " + scheduling.getDepotCode() + "-" + scheduling.getDepotName());
						
					}else{
						scheduling.setSchedulingDate(agendamentoDoctoServico.getAgendamentoEntrega().getDtAgendamento()
								.toString(TrackingContantsUtil.FORMATO_DATA));
						
						scheduling.setDescription(TrackingContantsUtil.AGENDAMENTO + " " + scheduling.getDepotCode() + "-" + scheduling.getDepotName());
					}						
					schedulings.addScheduling(scheduling);
			}
		}
		
		return schedulings;
	}
	
	private Invoices findNotasFiscais(Long idConhecimento) {
		/* Busca as notas fiscais do documento de servico para retorno */
		List<NotaFiscalConhecimento> notasFiscais = notaFiscalConhecimentoService.findByConhecimento(idConhecimento);
		Invoices invoices = new Invoices();
		
		if(notasFiscais != null) {
			for(NotaFiscalConhecimento nota : notasFiscais) {
				Invoice invoice = new Invoice();
				invoice.setNumber( String.valueOf(nota.getNrNotaFiscal()) );
				invoice.setIssuedDate( nota.getDtEmissao().toString(TrackingContantsUtil.FORMATO_DATA) );
				invoice.setVolumes( String.valueOf(nota.getQtVolumes()) );
				
				String peso = ((BigDecimal)nota.getPsMercadoria()).setScale(TRES).toString().replace(".", ",");
				invoice.setWeight( peso.concat(" kg" ) ); 
		
				invoices.addInvoice(invoice);
			}
		}
			
		return invoices;
	}
	
	private String getComplementoObservacao(String complementoObservacao) {
		String retorno = "";
		
		if(complementoObservacao != null) {
			if(complementoObservacao.toUpperCase().contains("BAIXA POR CELULAR")) {
				retorno += " " + complementoObservacao.toLowerCase();
			} else {
				retorno += " " + complementoObservacao;
			}
		}
		
		return retorno;
	}
	
	
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public EventoColetaService getEventoColetaService() {
		return eventoColetaService;
	}


	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}


	public void setEventoColetaService(EventoColetaService eventoColetaService) {
		this.eventoColetaService = eventoColetaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}


	public void setAgendamentoDoctoServicoService(
			AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}

	public void setLinhaTempoService(LinhaTempoService linhaTempoService) {
		this.linhaTempoService = linhaTempoService;
	}
}
