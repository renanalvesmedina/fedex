package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.dao.EventoControleCargaDAO;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.eventoControleCargaService"
 */
public class EventoControleCargaService extends CrudService<EventoControleCarga, Long> {

	/**
	 * Recupera uma instância de <code>EventoControleCarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EventoControleCarga findById(java.lang.Long id) {
        return (EventoControleCarga)super.findById(id);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
    public java.io.Serializable store(EventoControleCarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoControleCargaDAO(EventoControleCargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoControleCargaDAO getEventoControleCargaDAO() {
        return (EventoControleCargaDAO) getDao();
    }

    /**
     * Gera um novo eventoControleCarga setando os seguintes dados como 'default':
     * dhEvento - data e hora atual
     * usuario - usuario logado
     * meioTransporte - meioTransporte vindo do controleCarga    
     * 
     * @param tpEventoControleCarga
     * @param dsEvento
     * @param filial
     * @param controleCarga
     * @param equipeOperacao
     */
    public java.io.Serializable storeEventoControleCarga(String tpEventoControleCarga, String dsEvento, 
    		Filial filial, ControleCarga controleCarga, EquipeOperacao equipeOperacao) {
    
    	DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	Usuario usuarioLogado = SessionUtils.getUsuarioLogado();

    	return this.storeEventoControleCarga(tpEventoControleCarga, dsEvento, filial, controleCarga, equipeOperacao, dataHoraAtual, usuarioLogado);
    }
    
    /**
     * Alteração na assinatura proveniente de solicitação da integração CQPRO00005473. Para os outros casos, utilizar o método
     * cuja assinatura não possua usuário nem data.
     * Gera um novo eventoControleCarga setando os seguintes dados como 'default':
     * meioTransporte - meioTransporte vindo do controleCarga    
     * 
     * @param tpEventoControleCarga
     * @param dsEvento
     * @param filial
     * @param controleCarga
     * @param equipeOperacao
     * @param dataHoraEvento Necessário definir uma dataHora evento diferente da dataHora atual (Integração)
     * @param usuarioEvento Necessário definir um usuário diferente (Integração)
     */
    public java.io.Serializable storeEventoControleCarga(String tpEventoControleCarga, String dsEvento, 
    		Filial filial, ControleCarga controleCarga, EquipeOperacao equipeOperacao, DateTime dataHoraEvento, Usuario usuarioEvento ) {
    	
    	//Gerando um novo eventoControleCarga...
    	EventoControleCarga eventoControleCarga = new EventoControleCarga();
    	eventoControleCarga.setDhEvento(dataHoraEvento);
    	eventoControleCarga.setTpEventoControleCarga(new DomainValue(tpEventoControleCarga));
    	eventoControleCarga.setDsEvento(dsEvento);
    	//Gerando os relacionamentos
    	eventoControleCarga.setFilial(filial);
    	eventoControleCarga.setControleCarga(controleCarga);
    	eventoControleCarga.setUsuario(usuarioEvento);
    	eventoControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
    	eventoControleCarga.setEquipeOperacao(equipeOperacao);
    	
    	return super.store(eventoControleCarga);
    }
    

    public void storeEventoControleCarga(ControleCarga controleCarga,String tpStatusControleCarga) {
        
        storeEventoControleCarga(controleCarga, tpStatusControleCarga, JTDateTimeUtils.getDataHoraAtual());
        
    }
    
    
public void storeEventoControleCarga(ControleCarga controleCarga,String tpStatusControleCarga, DateTime dhEvento) {
        
        EventoControleCarga eventoControleCarga = new EventoControleCarga();
        eventoControleCarga.setDhEvento(new DateTime());
        eventoControleCarga.setUsuario(SessionUtils.getUsuarioLogado());
        eventoControleCarga.setFilial(SessionUtils.getFilialSessao());      
        eventoControleCarga.setTpEventoControleCarga(new DomainValue(tpStatusControleCarga));
        eventoControleCarga.setControleCarga(controleCarga);
        eventoControleCarga.setDhEvento(dhEvento);
        eventoControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
        
        store(eventoControleCarga);
    }
    
    
    /**
     * Método findPagineted para consulta liberação de risco 
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ResultSetPage findPaginatedConsultaLiberacaoRisco(Long idMeioTransporte, YearMonthDay dataInicial, YearMonthDay dataFinal, FindDefinition fd) {
        return getEventoControleCargaDAO().findPaginatedConsultaLiberacaoRisco(idMeioTransporte, dataInicial, dataFinal, fd);
    }
    
    /**
     * Row count especializado para consulta liberação de risco 
     * @param map
     * @return
     */
    public Integer getRowCountConsultaLiberacaoRisco(Long idMeioTransporte, YearMonthDay dataInicial, YearMonthDay dataFinal, FindDefinition fd) {
        return getEventoControleCargaDAO().getRowCountConsultaLiberacaoRisco(idMeioTransporte, dataInicial, dataFinal, fd);        
    }

    /**
     * Retorna Lista de Eventos e Controle de Carga pelo ID da filial, ID do controle de carga 
     * e tipo evento de controle de carga.
     * @param idFilial
     * @param idControleCarga
     * @param tpEvento
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(Long idFilial, Long idControleCarga, String tpEvento) {    	
    	return this.getEventoControleCargaDAO()
    	        .findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilial, idControleCarga, tpEvento);
    }
    
    /**
     * Verifica se já existe um evento de controle de carga antes de finalizar uma descarga 
     * 
     * @param idFilial
     * @param idControleCarga
     * @return boolean
     */    
    @SuppressWarnings("rawtypes")
    @Transactional
    public boolean existEventoControleCargaByIdCCAndIdFilial(TypedFlatMap criteria) {
    	List l = this.getEventoControleCargaDAO().findEventoControleCargaByIdFilialByIdControleCarga(criteria);
    	return !l.isEmpty();
    }
    
    // LMSA-6159
    public boolean existReSendEventoControleCarga(Long idControleCarga, List<Short> cdsEventosReenvNotfis) {
        Integer count = this.getEventoControleCargaDAO().countReSendEventoDocumentoByControleCarga(idControleCarga, cdsEventosReenvNotfis);
        return count != null ? count > 0 : false;
    }
    
    // LMSA-6159 LMSA-6249
    public List<EventoDocumentoServicoDMN> getEventoDocumentoContidoControleCarga(Long idControleCarga, List<Short> cdsEventosEdiFedex) {
        return this.getEventoControleCargaDAO().getReSendEventoDocumentoByControleCarga(idControleCarga, cdsEventosEdiFedex);
    }
    
}