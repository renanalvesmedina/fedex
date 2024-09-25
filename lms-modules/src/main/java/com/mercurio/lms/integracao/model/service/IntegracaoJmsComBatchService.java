package com.mercurio.lms.integracao.model.service;

import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.IProducer;
import com.mercurio.adsm.core.util.ADSMInitArgs;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.integracao.model.IntegracaoFilaJms;
import com.mercurio.lms.integracao.model.dao.IntegracaoJmsDAO;
import com.mercurio.lms.integracao.util.JodaISOModule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IntegracaoJmsComBatchService extends CrudService<IntegracaoFilaJms, Long> {
	private static final String NOT_IMPLEMENTED_METHOD = "Medoto não deve ser acessado";
	private static final String TABLE_STORE_EXCEPTION = "Erro ao gravar na tabela INTEGRACAO_FILA_JMS";
	private static final String MESSAGE_NOT_FOUND_EXCEPTION = "Não existe mensagem a ser enviada!";
	private static final String QUEUE_NOT_FOUND_EXCEPTION = "Não existe endereço de fila!";
	private static final String FILAS_DESABILITADAS = "FILAS_DESABILITADAS";
	private static final String FILAS_EM_ESPERA = "FILAS_EM_ESPERA";

	private ObjectMapper jsonMapper ;

	private ConfiguracoesFacade configuracoesFacade;

	public IntegracaoJmsComBatchService() {
		super();
		jsonMapper = new ObjectMapper();
		jsonMapper.registerModule(JodaISOModule.newInstance());
	}

	public void storeMessage(JmsMessageSender message, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
		message.validate();
		sendDBQueue(message.destination, message.correlationId,
					message.header, message.getMsg(),adsmNativeBatchSqlOperations);
	}

	public JmsMessageSender createMessage(IProducer destination) {
		return new JmsMessageSender(destination);
	}

	public JmsMessageSender createMessage(IProducer destination, Serializable msg) {
		return new JmsMessageSender(destination, msg);
	}

	@SuppressWarnings("rawtypes") 
	private void sendDBQueue(IProducer destination, String correlationId,
			HeaderParams header, List<Serializable> messages, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
		try {
			if( filaHabilitada(destination.getName()) ){
				Boolean blAtivo = filaAtiva(destination.getName());
				for (Serializable message : messages) {
					IntegracaoFilaJms integracaoFilaJms = newMessage(destination.to(), correlationId,header, message,blAtivo);
					Map<String,Object> entity = new HashMap<String,Object>();
					entity.put("BL_ATIVO",integracaoFilaJms.getBlAtivo());
					entity.put("BL_ERRO",integracaoFilaJms.getBlErro());
					entity.put("HEADER_JSON",integracaoFilaJms.getHeaderJson());
					entity.put("IDENTIFICADOR",integracaoFilaJms.getIdentificador());
					entity.put("MESSAGE_JSON",integracaoFilaJms.getMessageJson());
					entity.put("NM_INTEGRACAO_FILA_JMS",integracaoFilaJms.getNmIntegracaoFilaJms());
					adsmNativeBatchSqlOperations.addNativeBatchInsert("INTEGRACAO_FILA_JMS",entity);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(TABLE_STORE_EXCEPTION,e);
		}
	}
	
	private Boolean filaHabilitada(String fila){
		Boolean result = true;
		String filas = (String)configuracoesFacade.getValorParametroWithoutException(FILAS_DESABILITADAS);
		if( filas != null && ArrayUtils.contains(filas.split(";"),fila) ){
			result = false;
		}
		return result;
	}

	private Boolean filaAtiva(String fila){
		Boolean result = true;
		String filas = (String)configuracoesFacade.getValorParametroWithoutException(FILAS_EM_ESPERA);
		if( filas != null && ArrayUtils.contains(filas.split(";"),fila) ){
			result = false;
		}
		return result;
	}

	private IntegracaoFilaJms newMessage(String name, String correlationId,
			HeaderParams header, Serializable message,Boolean blAtivo)
			throws IOException {
		IntegracaoFilaJms i = new IntegracaoFilaJms();
		i.setNmIntegracaoFilaJms(name);
		i.setIdentificador(correlationId);
		i.setBlErro(false);
		i.setMessageJson(marshall(message));
		i.setHeaderJson(marshall(header));
		i.setBlAtivo(blAtivo);
		return i;
	}

	private String marshall(Serializable message)
			throws IOException {
		return jsonMapper.writeValueAsString(message);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List find(Map criteria) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findLookup(Map criteria) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map findReport(Map criteria) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@Override
	public List<?> findSuggest(Map<String, Object> filter) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@Override
	public void flush() {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@Override
	protected IntegracaoFilaJms get(Long id) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getRowCount(Map criteria) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	@Override
	public void storeAll(List<IntegracaoFilaJms> list) {
		throw new NotImplementedException(NOT_IMPLEMENTED_METHOD);
	}

	private class HeaderParams extends HashMap<String, String> {
		private static final long serialVersionUID = 1L;

	}

	public class JmsMessageSender {
		IProducer destination;
		String correlationId;
		HeaderParams header;
		List<Serializable> msg = new LinkedList<Serializable>();

		private JmsMessageSender(IProducer destination) {
			super();
			this.destination = destination;
			addHeader(HeaderParam.SENDER_SERVER_URL.getName(), ADSMInitArgs.LMS_ADDRESS.getValue());
		}

		private JmsMessageSender(IProducer destination, Serializable msg) {
			this(destination);
			addMsg(msg);
		}

		public void setCorrelationId(String correlationId) {
			this.correlationId = correlationId;
		}

		public void addHeader(String chave, String valor) {
			if (this.header == null) {
				this.header = new HeaderParams();
			}
			header.put(chave, valor);
		}

		public void addMsg(Serializable msg) {
			this.msg.add(msg);
		}

		public void addAllMsg(List<Serializable> msgs) {
			this.msg.addAll(msgs);
		}
		
		private List<Serializable> getMsg() {
			return msg;
		}

		private void validate() {
			if (CollectionUtils.isEmpty(msg)) {
				throw new IllegalArgumentException(MESSAGE_NOT_FOUND_EXCEPTION);
			}
			if ( destination == null) {
				throw new IllegalArgumentException(QUEUE_NOT_FOUND_EXCEPTION);
			}
		}
	}

	public void setIntegracaoJmsDAO(IntegracaoJmsDAO integracaoJmsDAO) {
		super.setDao(integracaoJmsDAO);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacadeImpl(ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
		this.configuracoesFacade = configuracoesFacadeImpl;
	}

}
