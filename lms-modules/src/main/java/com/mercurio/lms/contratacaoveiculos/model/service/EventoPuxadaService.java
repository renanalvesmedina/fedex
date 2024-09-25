package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.EventoPuxada;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.dao.EventoPuxadaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.contratacaoveiculos.eventoPuxadaService"
 */
public class EventoPuxadaService extends CrudService<EventoPuxada, Long>{
	
	/**
	 * Recupera uma instância de <code>EventoPuxada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EventoPuxada findById(java.lang.Long id) {
        return (EventoPuxada)super.findById(id);
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
    public java.io.Serializable store(EventoPuxada bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoPuxadaDAO(EventoPuxadaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoPuxadaDAO getEventoPuxadaDAO() {
        return (EventoPuxadaDAO) getDao();
    }
    
	public List<EventoPuxada> findBySolicitacaoContratacao(Long idSolicitacaoContratacao) {
		return getEventoPuxadaDAO().findBySolicitacaoContratacao(idSolicitacaoContratacao);
	}
	public void generateEventoPuxada(SolicitacaoContratacao solicitacao, String tpEvento) {
		EventoPuxada eventoPuxada = new EventoPuxada();
		eventoPuxada.setDhEvento(new DateTime());
		eventoPuxada.setTpStatusEvento(new DomainValue(tpEvento));
		eventoPuxada.setUsuario(SessionUtils.getUsuarioLogado()); 
		eventoPuxada.setSolicitacaoContratacao(solicitacao);
		
		store(eventoPuxada);
	}
	
	public EventoPuxada findByLastSolicitacaoContratacao(Long idSolicitacaoContratacao) {
		return getEventoPuxadaDAO().findByLastSolicitacaoContratacao(idSolicitacaoContratacao);
	}
}
