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
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.contratacaoveiculos.eventoPuxadaService"
 */
public class EventoPuxadaService extends CrudService<EventoPuxada, Long>{
	
	/**
	 * Recupera uma inst�ncia de <code>EventoPuxada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public EventoPuxada findById(java.lang.Long id) {
        return (EventoPuxada)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(EventoPuxada bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setEventoPuxadaDAO(EventoPuxadaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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
