package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.EventoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.EventoNotaCreditoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EventoNotaCreditoService extends CrudService<EventoNotaCredito, Long> {
	
	private UsuarioLMSService usuarioLMSService;

    public EventoNotaCredito findById(java.lang.Long id) {
        return (EventoNotaCredito)super.findById(id);
    }

    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    public java.io.Serializable store(EventoNotaCredito bean) {
        return super.store(bean);
    }

    public void setEventoNotaCreditoDAO(EventoNotaCreditoDAO dao) {
        setDao( dao );
    }
    
    @SuppressWarnings("unused")
	private EventoNotaCreditoDAO getEventoNotaCreditoDAO() {
        return (EventoNotaCreditoDAO) getDao();
    }

    public java.io.Serializable storeEventoNotaCredito(NotaCredito notaCredito, String tpEventoNotaCredito, String tpEventoFluxoNotaCredito) {
    
    	DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	Usuario usuarioLogado = SessionUtils.getUsuarioLogado();

    	EventoNotaCredito eventoNotaCredito = new EventoNotaCredito();
    	eventoNotaCredito.setDhEvento(dataHoraAtual);
    	eventoNotaCredito.setNotaCredito(notaCredito);
    	eventoNotaCredito.setTpOrigemEvento( new DomainValue(tpEventoNotaCredito) );
    	eventoNotaCredito.setTpComplementoEvento( new DomainValue(tpEventoFluxoNotaCredito) );
    	eventoNotaCredito.setUsuario(usuarioLMSService.findById(usuarioLogado.getIdUsuario()));
    	
    	return this.store(eventoNotaCredito);
    }

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedEventos(TypedFlatMap criteria) {
		return getEventoNotaCreditoDAO().findPaginatedEventos(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCountEventos(TypedFlatMap criteria) {
		return getEventoNotaCreditoDAO().getRowCountEventos(criteria);
	}
    
}