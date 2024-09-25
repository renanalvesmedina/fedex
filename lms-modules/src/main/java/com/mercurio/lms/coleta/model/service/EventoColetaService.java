package com.mercurio.lms.coleta.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.dao.EventoColetaDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsComBatchService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import br.com.tntbrasil.integracao.domains.sim.EventoColetaDMN;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.eventoColetaService"
 */
public class EventoColetaService extends CrudService<EventoColeta, Long> {

	private IntegracaoJmsService integracaoJmsService;
	private IntegracaoJmsComBatchService integracaoJmsComBatchService;

	public void storeMessageTopic(EventoColeta eventoColeta){
		storeMessageTopic(eventoColeta, null, null);
	}

	public void storeMessageTopic(EventoColeta eventoColeta,String nrIdentificacaoDevedor){
		storeMessageTopic(eventoColeta, nrIdentificacaoDevedor, null);
	}

	public void storeMessageTopic(EventoColeta eventoColeta, String nrIdentificacaoDevedor, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations){
		if(eventoColeta!=null){
			EventoColetaDMN eventoColetaDMN = buildEventoColetaDMN(eventoColeta);

			if(!StringUtils.isEmpty(nrIdentificacaoDevedor)) {
				eventoColetaDMN.setNrIdentificacaoDevedor(nrIdentificacaoDevedor);
			}

			if(adsmNativeBatchSqlOperations == null) {
				JmsMessageSender messsageSender = integracaoJmsService.createMessage(VirtualTopics.EVENTO_COLETA, eventoColetaDMN);
				integracaoJmsService.storeMessage(messsageSender);
			} else{
				IntegracaoJmsComBatchService.JmsMessageSender messageSender = integracaoJmsComBatchService.createMessage(VirtualTopics.EVENTO_COLETA, eventoColetaDMN);
				integracaoJmsComBatchService.storeMessage(messageSender,adsmNativeBatchSqlOperations);
			}
		}
	}

	private EventoColetaDMN buildEventoColetaDMN(EventoColeta eventoColeta) {
		EventoColetaDMN eventoColetaDMN = new EventoColetaDMN();
		eventoColetaDMN.setIdEventoColeta(eventoColeta.getIdEventoColeta());
		eventoColetaDMN.setDhEvento(eventoColeta.getDhEvento());
		eventoColetaDMN.setTpEventoColeta(eventoColeta.getTpEventoColeta() != null ? eventoColeta.getTpEventoColeta().getValue() : null);
		eventoColetaDMN.setDsDescricao(eventoColeta.getDsDescricao());

		if(eventoColeta.getOcorrenciaColeta() != null){
			eventoColetaDMN.setDsDescricaoOcorrenciaColeta(eventoColeta.getOcorrenciaColeta().getDsDescricaoCompleta() != null ? eventoColeta.getOcorrenciaColeta().getDsDescricaoCompleta().getValue() : null);
			eventoColetaDMN.setCdOcorrenciaColeta(eventoColeta.getOcorrenciaColeta().getCodigo());
		}

		if(eventoColeta.getPedidoColeta() != null){
			eventoColetaDMN.setCdColetaCliente(eventoColeta.getPedidoColeta().getCdColetaCliente());
			eventoColetaDMN.setIdPedidoColeta(eventoColeta.getPedidoColeta().getIdPedidoColeta());
			eventoColetaDMN.setNrColeta(eventoColeta.getPedidoColeta().getNrColeta());
			eventoColetaDMN.setDhPedidoColeta(eventoColeta.getPedidoColeta().getDhPedidoColeta());

			if(eventoColeta.getPedidoColeta().getFilialByIdFilialSolicitante() != null){
				eventoColetaDMN.setIdFilialSolicitante(eventoColeta.getPedidoColeta().getFilialByIdFilialSolicitante().getIdFilial());
			}

			if(eventoColeta.getPedidoColeta().getFilialByIdFilialResponsavel() != null){
				eventoColetaDMN.setIdFilialResponsavel(eventoColeta.getPedidoColeta().getFilialByIdFilialResponsavel().getIdFilial());
			}

			if(eventoColeta.getPedidoColeta().getCliente() != null && eventoColeta.getPedidoColeta().getCliente().getPessoa() != null){
				eventoColetaDMN.setNrIdentificacaoCliente(eventoColeta.getPedidoColeta().getCliente().getPessoa().getNrIdentificacao());
				eventoColetaDMN.setNmCliente(eventoColeta.getPedidoColeta().getCliente().getPessoa().getNmPessoa());
			}
		}
		return eventoColetaDMN;
	}

	/**
	 * Recupera uma instância de <code>EventoColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public EventoColeta findById(java.lang.Long id) {
		return (EventoColeta)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EventoColeta bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setEventoColetaDAO(EventoColetaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoColetaDAO getEventoColetaDAO() {
		return (EventoColetaDAO) getDao();
	}

	public Integer getRowCount(Long idPedidoColeta) {
		return getEventoColetaDAO().getRowCountByIdPedidoColeta(idPedidoColeta);
	}

	/**
	 * Apaga uma entidade através do Id do Pedido de Coleta.
	 *
	 * @param idPedidoColeta id indica a entidade que deverá ser removida.
	 */
	public void removeByIdPedidoColeta(Long idPedidoColeta) {
		this.getEventoColetaDAO().removeByIdPedidoColeta(idPedidoColeta);
	}

	public DateTime findDhEventoByMeioTransporte(Long idMeioTransporte) {
		return getEventoColetaDAO().findDhEventoByMeioTransporte(idMeioTransporte);
	}


	/**
	 * Retorna lista de dh_evento dos eventos de coleta transmitidos (TR) e que não tenham evento mais recente que TR para um determinado
	 * pedido_coleta
	 * @param idMeioTransporte
	 * @return lista de dhEvento TR
	 */
	public List<EventoColeta> findEventoTransmitidoByMeioTransporte(Long idMeioTransporte){
		return getEventoColetaDAO().findEventoTransmitidoByMeioTransporte(idMeioTransporte);

	}

	public DateTime findDhEventoByPedidoColeta(Long idPedidoColeta, String tpEventoColeta) {
		return getEventoColetaDAO().findDhEventoByPedidoColeta(idPedidoColeta, tpEventoColeta);
	}

	/**
	 * Verifica se existe o tipo de EventoColeta para o PedidoColeta em questão
	 * @param idPedidoColeta
	 * @param tpEventoColeta
	 * @return
	 */
	public boolean validateEventoColetaByIdPedidoColeta(Long idPedidoColeta, String tpEventoColeta) {
		return getEventoColetaDAO().validateEventoColetaByIdPedidoColeta(idPedidoColeta, tpEventoColeta);
	}

	public List findEventoColetaByIdPedidoColeta(Long idPedidoColeta){
		return getEventoColetaDAO().findEventoColetaByIdPedidoColeta(idPedidoColeta);
	}

	public List findEventoColetaTransmitidaByIdPedidoColeta(Long idPedidoColeta){
		return getEventoColetaDAO().findEventoColetaTransmitidaByIdPedidoColeta(idPedidoColeta);
	}

	public List findEventoColetaManifestadaByIdPedidoColeta(Long idPedidoColeta){
		return getEventoColetaDAO().findEventoColetaManifestadaByIdPedidoColeta(idPedidoColeta);
	}

	/**
	 * Busca o último evento de coleta do tipo "manifestada" a partir do id do pedido coleta.
	 * @param idPedidoColeta
	 * @return
	 */
	public EventoColeta findLastEventoColetaManifestada(Long idPedidoColeta){
		return getEventoColetaDAO().findLastEventoColetaManifestada(idPedidoColeta);
	}


	public List findEventoColetaByIdPedidoColeta(Long idPedidoColeta, String tpEventoColeta){
		return getEventoColetaDAO().findEventoColetaByIdPedidoColeta(idPedidoColeta, tpEventoColeta);
	}

	/**
	 * Retorna um count de eventoColeta à partir do Id e tpEvento
	 * @param idPedidoColeta
	 * @param tpEventoColeta
	 * @return qtdeEventoColeta
	 */
	public Integer findQtdEventoColeta(Long idPedidoColeta, String tpEventoColeta) {
		return getEventoColetaDAO().findQtdEventoColeta(idPedidoColeta, tpEventoColeta);
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setIntegracaoJmsComBatchService(IntegracaoJmsComBatchService integracaoJmsComBatchService) {
		this.integracaoJmsComBatchService = integracaoJmsComBatchService;
	}
}
