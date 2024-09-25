package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.dto.ProcessaNotasEdiItemDto;
import com.mercurio.lms.expedicao.edi.model.service.ProcessamentoEdiService;
import com.mercurio.lms.expedicao.edi.model.service.ProcessarNotaFiscalEDIService;
import com.mercurio.lms.expedicao.model.ProcessamentoEdi;
import com.mercurio.lms.expedicao.model.ProcessamentoIbEdi;
import com.mercurio.lms.expedicao.model.dao.ProcessamentoIbEdiDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessamentoIbEdiService extends CrudService<ProcessamentoIbEdi, Long>{

	private IntegracaoJmsService integracaoJmsService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ProcessarNotaFiscalEDIService processarNotaFiscalEDIService;
	private ProcessamentoEdiService processamentoEdiService;

	public Serializable store(ProcessamentoIbEdi bean) {
		return super.store(bean);
	}

	public void findRemainingNotes(Long nrProcessamento) {
		ProcessamentoIbEdi processamentoIbEdi = getProcessamentoIbEdiDAO().findRemainingNotesByNrProcesso(nrProcessamento, true);

		if(processamentoIbEdi != null) {
			List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos = null;
			try {
				processaNotasEdiItemDtos = new ObjectMapper().readValue(processamentoIbEdi.getDsDados(), new TypeReference<List<ProcessaNotasEdiItemDto>>(){});
			} catch (IOException e) {
				throw new RuntimeException();
			}

			if(processaNotasEdiItemDtos != null && !processaNotasEdiItemDtos.isEmpty()) {
				processaNotasEdiItemDtos.remove(0);
				IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.PROCESSAMENTO_NOTAS_EDI_ITEM);
				processaNotasEdiItemDtos.forEach(jmsMessageSender::addMsg);
				integracaoJmsService.storeMessage(jmsMessageSender);
			}

			processamentoIbEdi.setDhEnvio(new DateTime());
			store(processamentoIbEdi);
		}
	}

	public ProcessaNotasEdiItemDto findProcessaNotasEdiItemDto(String nrProcessamento) throws IOException {
		Long nrP = Long.parseLong(nrProcessamento);
		List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos = new ArrayList<>();
		ProcessamentoIbEdi processamentoIbEdi = getProcessamentoIbEdiDAO().findRemainingNotesByNrProcesso(nrP, false);

		if(processamentoIbEdi != null) {
			processaNotasEdiItemDtos = new ObjectMapper().readValue(processamentoIbEdi.getDsDados(), new TypeReference<List<ProcessaNotasEdiItemDto>>(){});
			int totalElementoLista = processaNotasEdiItemDtos.size() - 1;
			return processaNotasEdiItemDtos.get(totalElementoLista);
		}
		return null;
	}

	public void reenviarProcessamentoNotaEdiItem(ProcessaNotasEdiItemDto item) {

		List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos = null;
		List<ProcessaNotasEdiItemDto> novaListaProcessaNotasEdiItemDto = null;
		ProcessaNotasEdiItemDto processaNotasEdiItemDto = null;
		ProcessamentoIbEdi processamentoIbEdi = null;
		int totalItemLista = 0;
		try {
			if (item.getFinalizou()){
				if (!item.getPrimeiraExecucao()) {
					processamentoIbEdi = getProcessamentoIbEdiDAO()
						.findRemainingNotesByNrProcesso(item.getNrProcessamento(), false);
					processaNotasEdiItemDtos = converterEstruturaNotasEdiItem(item.getNrProcessamento(), processamentoIbEdi);
					novaListaProcessaNotasEdiItemDto = processaNotasEdiItemDtos.stream()
						.filter(pne -> !pne.getUuid().equals(item.getUuid())).collect(Collectors.toList());

					totalItemLista = novaListaProcessaNotasEdiItemDto.size() - 1;
					processaNotasEdiItemDto = novaListaProcessaNotasEdiItemDto.get(totalItemLista);
					processaNotasEdiItemDto.setFinalizou(Boolean.TRUE);
					storeProcessamentoIbEdi(novaListaProcessaNotasEdiItemDto, processamentoIbEdi);

					processamentoEdiService.updateTpStatus(String.valueOf(item.getNrProcessamento()), new DomainValue("PI"));
				} else {
					processamentoEdiService.updateTpStatus(String.valueOf(item.getNrProcessamento()), new DomainValue("PF"));
				}
			} else if (item.getPrimeiraExecucao()) {
				IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
					.createMessage(Queues.PROCESSAMENTO_NOTAS_EDI_ITEM);
				processamentoIbEdi = getProcessamentoIbEdiDAO()
					.findRemainingNotesByNrProcesso(item.getNrProcessamento(), true);
				processaNotasEdiItemDtos = converterEstruturaNotasEdiItem(item.getNrProcessamento(), processamentoIbEdi);

				novaListaProcessaNotasEdiItemDto = processaNotasEdiItemDtos.stream()
					.filter(pne -> !pne.getUuid().equals(item.getUuid())).collect(Collectors.toList());

				processaNotasEdiItemDto = novaListaProcessaNotasEdiItemDto.get(0);
				processaNotasEdiItemDto.setPrimeiraExecucao(Boolean.TRUE);
				jmsMessageSender.addMsg(processaNotasEdiItemDto);

				storeProcessamentoIbEdi(novaListaProcessaNotasEdiItemDto, processamentoIbEdi);
				integracaoJmsService.storeMessage(jmsMessageSender);
			} else {
				processamentoIbEdi = getProcessamentoIbEdiDAO()
					.findRemainingNotesByNrProcesso(item.getNrProcessamento(), false);
				processaNotasEdiItemDtos = converterEstruturaNotasEdiItem(item.getNrProcessamento(), processamentoIbEdi);

				novaListaProcessaNotasEdiItemDto = processaNotasEdiItemDtos.stream()
					.filter(pne -> !pne.getUuid().equals(item.getUuid())).collect(Collectors.toList());
				storeProcessamentoIbEdi(novaListaProcessaNotasEdiItemDto, processamentoIbEdi);
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	private List<ProcessaNotasEdiItemDto> converterEstruturaNotasEdiItem
	(Long nrProcessamento, ProcessamentoIbEdi processamentoIbEdi) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper
			.readValue(processamentoIbEdi.getDsDados(), new TypeReference<List<ProcessaNotasEdiItemDto>>(){});
	}

	private void
	storeProcessamentoIbEdi(List<ProcessaNotasEdiItemDto> novaListaProcessaNotasEdiItemDto, ProcessamentoIbEdi processamentoIbEdi) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		processamentoIbEdi.setDsDados(mapper.writeValueAsString(novaListaProcessaNotasEdiItemDto));
		store(processamentoIbEdi);
	}
	public ProcessamentoIbEdiDAO getProcessamentoIbEdiDAO() {
		return (ProcessamentoIbEdiDAO) getDao();
	}

	public void setProcessamentoIbEdiDAO(ProcessamentoIbEdiDAO processamentoIbEdiDAO) {
		setDao(processamentoIbEdiDAO);
	}

	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public ProcessarNotaFiscalEDIService getProcessarNotaFiscalEDIService() {
		return processarNotaFiscalEDIService;
	}

	public void setProcessarNotaFiscalEDIService(ProcessarNotaFiscalEDIService processarNotaFiscalEDIService) {
		this.processarNotaFiscalEDIService = processarNotaFiscalEDIService;
	}

	public void setProcessamentoEdiService(ProcessamentoEdiService processamentoEdiService) {
		this.processamentoEdiService = processamentoEdiService;
	}
}
