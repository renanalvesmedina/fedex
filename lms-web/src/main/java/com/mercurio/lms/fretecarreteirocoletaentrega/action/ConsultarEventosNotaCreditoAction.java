package com.mercurio.lms.fretecarreteirocoletaentrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.EventoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.EventoNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;


public class ConsultarEventosNotaCreditoAction extends CrudAction {
	
	private EventoNotaCreditoService eventoNotaCreditoService;
	private UsuarioService usuarioService;
	private NotaCreditoService notaCreditoService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = eventoNotaCreditoService.findPaginated(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		EventoNotaCredito eventoNotaCredito = (EventoNotaCredito)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("tpOrigemEvento", eventoNotaCredito.getTpOrigemEvento());
    		tfm.put("tpComplementoEvento", eventoNotaCredito.getTpComplementoEvento());
    		tfm.put("dhEvento", eventoNotaCredito.getDhEvento());
    		if (eventoNotaCredito.getUsuario() != null) {
    			tfm.put("nmUsuario", usuarioService.findById(eventoNotaCredito.getUsuario().getIdUsuario()).getNmUsuario());
    		}
    		retorno.add(tfm);
    	}
    	rsp.setList(retorno);
    	return rsp;
    }
	
	@SuppressWarnings("rawtypes")
	public Integer getRowCount(Map criteria) {
    	return eventoNotaCreditoService.getRowCount(criteria);
    }
	
	@SuppressWarnings("rawtypes")
	public TypedFlatMap findNotaCreditoById(Long id) {
        List notaCredito = notaCreditoService.findByIdCustom(id);
        
        TypedFlatMap tfm = new TypedFlatMap();
        
        Object[] projections = (Object[])notaCredito.get(0);
        tfm.put("sgFilial",projections[0]);
        tfm.put("nrNotaCredito",projections[2]);
        return tfm;
    }

	public EventoNotaCreditoService getEventoNotaCreditoService() {
		return eventoNotaCreditoService;
	}

	public void setEventoNotaCreditoService(EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

}
