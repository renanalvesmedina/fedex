package com.mercurio.lms.integracao.model.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.FlushMode;

import br.com.tntbrasil.integracao.domains.endToEnd.EMonitoramentoDMN;
import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.IProducer;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.core.util.ADSMInitArgs;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.integracao.model.IntegracaoFilaJms;
import com.mercurio.lms.integracao.model.dao.IntegracaoJmsDAO;
import com.mercurio.lms.integracao.util.JodaISOModule;
import com.mercurio.lms.util.JTDateTimeUtils;

public class IntegracaoJmsService extends CrudService<IntegracaoFilaJms, Long> {
	public static final String STATUS_RETORNO_OK = "OK";
	public static final String STATUS_RETORNO_ERRO = "ERRO";
	private static final String NOT_IMPLEMENTED_METHOD = "Medoto não deve ser acessado";
	private static final String TABLE_STORE_EXCEPTION = "Erro ao gravar na tabela INTEGRACAO_FILA_JMS";
	private static final String MESSAGE_NOT_FOUND_EXCEPTION = "Não existe mensagem a ser enviada!";
	private static final String QUEUE_NOT_FOUND_EXCEPTION = "Não existe endereço de fila!";
	private static final String FILAS_DESABILITADAS = "FILAS_DESABILITADAS";
	private static final String FILAS_EM_ESPERA = "FILAS_EM_ESPERA";

	private ObjectMapper jsonMapper ;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	public void storeMessage(JmsMessageSender message) {
	      storeMessage(message, false);
	}
	
	   public void storeMessage(JmsMessageSender message, boolean isCompressed) {
	        message.validate();
	        sendDBQueue(message.destination, message.correlationId,
	                    message.header, message.getMsg(), isCompressed);
	    }

	public IntegracaoJmsService() {
		super();
		jsonMapper = new ObjectMapper();
		jsonMapper.registerModule(JodaISOModule.newInstance());
	}

	public JmsMessageSender createMessage(IProducer destination) {
		return new JmsMessageSender(destination);
	}

	public JmsMessageSender createMessage(IProducer destination, Serializable data) {
		return new JmsMessageSender(destination, data);
	}
	
	public EMonitoramentoDMN<?> sendMonitoredMessage(IProducer destination, IMessageProducer<?> producer) {
		return sendMonitoredMessage(destination, true, producer);
	}
	
	public EMonitoramentoDMN<?> sendUncompressedMonitoredMessage(IProducer destination, IMessageProducer<?> producer) {
		return sendMonitoredMessage(destination, false, producer);
	}
	
	public EMonitoramentoDMN<?> executeSendMonitoredMessage(IProducer destination, boolean isCompressed, IMessageProducer<?> producer) {
		return sendMonitoredMessage(destination, isCompressed, producer);
	}
	
	public EMonitoramentoDMN<?> sendMonitoredMessage(IProducer destination, boolean isCompressed, IMessageProducer<?> producer) {
		
		Boolean blMonitoramentoIb = "S".equals(configuracoesFacade.getValorParametro("BL_MONITORAMENTO_IB"));
			
		EMonitoramentoDMN<?> msg = new EMonitoramentoDMN<Serializable>();
		try {
			msg = producer.build();
			if(msg != null){
			    msg.setStatus(STATUS_RETORNO_OK);
				msg.setDataHoraGeracao(JTDateTimeUtils.getDataHoraAtual());

                this.storeMessage(this.createMessage(destination, (Serializable) msg), isCompressed);
                
                if (blMonitoramentoIb) {
                	this.storeMessage(this.createMessage(Queues.INTEGRACAO_FEDEX_MONITORAMENTO, (Serializable) msg), true);
                }
			}	
		} catch (BusinessException be) {
			if (blMonitoramentoIb) {
				String mensagem = configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments());
				this.buildErrorMessage(msg, be.getMessageKey().concat(" - ").concat(mensagem));
				this.storeMessage(this.createMessage(Queues.INTEGRACAO_FEDEX_MONITORAMENTO, (Serializable) msg), true);
			}
		} catch (Exception e) {
			if (blMonitoramentoIb) {
				this.buildErrorMessage(msg, e.getClass().getName().concat(": ").concat(e.getMessage() != null ? e.getMessage() : "null"));
				this.storeMessage(this.createMessage(Queues.INTEGRACAO_FEDEX_MONITORAMENTO, (Serializable) msg), true);
			}
		}
		return msg;
	}

	private void buildErrorMessage(EMonitoramentoDMN<?> msg, String errorMessage) {
		msg.setStatus(STATUS_RETORNO_ERRO);
        msg.setDataHoraGeracao(JTDateTimeUtils.getDataHoraAtual());
        msg.setListErros(new ArrayList<String>());
        msg.getListErros().add(errorMessage);
	}

	public static String compress(String str) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
		
		ZipOutputStream zipOutput = new ZipOutputStream(output);
		
	    ZipEntry entry = new ZipEntry("JSON");
	    zipOutput.putNextEntry(entry);
	    
	    for (int byteData = input.read(); byteData != -1; byteData = input.read()) {
	        zipOutput.write(byteData);
        }
	    zipOutput.closeEntry();
	    zipOutput.close();
	    
	    return output.toString();
	}
	
	public static String deCompress(byte[] str) throws IOException {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    ByteArrayInputStream input = new ByteArrayInputStream(str);

	    ZipInputStream zipInput = new ZipInputStream(input);
	    ZipEntry entry = null;

        while ((entry = zipInput.getNextEntry()) != null) {
            for (int byteData = zipInput.read(); byteData != -1; byteData = zipInput.read()) {
                output.write(byteData);
              }
            zipInput.closeEntry();
        }
        output.close();
        zipInput.close();
        
        return output.toString();
    }
	
	@SuppressWarnings("rawtypes") 
	private void sendDBQueue(IProducer destination, String correlationId,
			HeaderParams header, List<Serializable> messages, boolean isCompressed) {
		try {
			if( filaHabilitada(destination.getName()) ){
				List<IntegracaoFilaJms> persistList = new LinkedList<IntegracaoFilaJms>();
				Boolean blAtivo = filaAtiva(destination.getName());
				for (Serializable message : messages) {
					persistList.add(newMessage(destination.to(), correlationId,
							header, message,blAtivo, destination.getType(), isCompressed));
				}
                                getDao().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
				super.storeAll(persistList);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(TABLE_STORE_EXCEPTION,e);
		}
	}
	
	private Boolean filaHabilitada(String fila){
		Boolean _return = true;
		String filas = (String)configuracoesFacade.getValorParametroWithoutException(FILAS_DESABILITADAS);
		if( filas != null && ArrayUtils.contains(filas.split(";"),fila) ){
			_return = false;
		}
		return _return;
	}

	private Boolean filaAtiva(String fila){
		Boolean _return = true;
		String filas = (String)configuracoesFacade.getValorParametroWithoutException(FILAS_EM_ESPERA);
		if( filas != null && ArrayUtils.contains(filas.split(";"),fila) ){
			_return = false;
		}
		return _return;
	}

	private IntegracaoFilaJms newMessage(String name, String correlationId,
			HeaderParams header, Serializable message,Boolean blAtivo, String tpDestinoIbmMq, boolean isCompressed)
			throws JsonGenerationException, JsonMappingException, IOException {
		IntegracaoFilaJms i = new IntegracaoFilaJms();
		i.setNmIntegracaoFilaJms(name);
		i.setIdentificador(correlationId);
		i.setBlErro(false);
		
		if (message instanceof String){
			i.setMessageJson((String)message);
		}else{
			i.setMessageJson(marshall(message));
		}
		i.setHeaderJson(marshall(header));
		i.setBlAtivo(blAtivo);
		i.setTpDestinoIbmMq(tpDestinoIbmMq);
		i.setBlCompressMessage(isCompressed);
		return i;
	}

	private String marshall(Serializable message)
			throws JsonGenerationException, JsonMappingException, IOException {
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
