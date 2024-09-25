package com.mercurio.lms.entrega.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.service.EventoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
 

/**
 * Generated by: ADSM ActionGenerator
 *   
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.manterOcorrenciasEntregaAction"
 */
 
public class ManterOcorrenciasEntregaAction extends CrudAction {
	
	
	private EventoService eventoService;
	private DomainValueService domainValueService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	
	public void setService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.defaultService = ocorrenciaEntregaService;
	}
    public void removeById(java.lang.Long id) {
        ((OcorrenciaEntregaService)defaultService).removeById(id);
        
    }
    public List find(Map criteria) {
    	List myList = super.find(criteria);
    	
    	return myList;
    }
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return findPaginatedCustom(criteria);
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria){
    	
    	ResultSetPage rs = ((OcorrenciaEntregaService)defaultService).findPaginatedCustom(criteria);
    	List listaRetorno = new ArrayList();
    	for (int i = 0 ; i <= rs.getList().size() -1; i++){
    		OcorrenciaEntrega oe = (OcorrenciaEntrega)rs.getList().get(i);
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("dsOcorrenciaEntrega",oe.getDsOcorrenciaEntrega());
    		tfm.put("idOcorrenciaEntrega",oe.getIdOcorrenciaEntrega());
    		tfm.put("cdOcorrenciaEntrega",oe.getCdOcorrenciaEntrega());
    		tfm.put("tpOcorrencia",oe.getTpOcorrencia());
    		
    		if (oe.getEvento() != null)
    			tfm.put("evento.dsEvento",oe.getEvento().getDsEvento());

    		tfm.put("blDescontoDpe",oe.getBlDescontoDpe());
    		tfm.put("blOcasionadoMercurio",oe.getBlOcasionadoMercurio());
    		tfm.put("tpSituacao.description",oe.getTpSituacao().getDescription());
    		
    		listaRetorno.add(tfm);
    	}
    	rs.setList(listaRetorno);
    	return rs; 
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((OcorrenciaEntregaService)defaultService).removeByIds(ids);
    }

    public Integer getRowCount(TypedFlatMap criteria) {
		
		return ((OcorrenciaEntregaService)defaultService).getRowCount(criteria);
	}
 
    public OcorrenciaEntrega findById(java.lang.Long id) {
    	return ((OcorrenciaEntregaService)defaultService).findOcorrenciaEntregaById(id);
    }
     
    // Buscar evento associado
    public List findLookupEventoAssociado(Map criteria){
    	return getEventoService().findLookupEvento(criteria);
    }

    public List findLookupOcorrenciaPendencia(TypedFlatMap tfm) {
		return this.ocorrenciaPendenciaService.findLookupOcorrenciaPendencia(tfm);
	}
    
    
    public Serializable store(TypedFlatMap parameters) {
    	OcorrenciaEntrega bean = new OcorrenciaEntrega();
    							bean.setIdOcorrenciaEntrega(parameters.getLong("idOcorrenciaEntrega"));
    							bean.setCdOcorrenciaEntrega(parameters.getShort("cdOcorrenciaEntrega"));
    							bean.setDsOcorrenciaEntrega(parameters.getVarcharI18n("dsOcorrenciaEntrega"));
    							bean.setTpOcorrencia(parameters.getDomainValue("tpOcorrencia"));
    							bean.setBlOcasionadoMercurio(parameters.getBoolean("blOcasionadoMercurio"));
    							bean.setBlDescontoDpe(parameters.getBoolean("blDescontoDpe"));
    							bean.setBlContabilizarEntrega(parameters.getBoolean("blContabilizarEntrega"));
    							bean.setBlContabilizarTentativaEntrega(parameters.getBoolean("blContabilizarTentativaEntrega"));
    							bean.setTpSituacao(parameters.getDomainValue("tpSituacao"));
      							if (parameters.getLong("evento.idEvento") != null) {
    								Evento evento = new Evento();
    								evento.setIdEvento(parameters.getLong("evento.idEvento"));
    								bean.setEvento(evento);
    							}
		if (parameters.getLong("ocorrenciaPendencia.idOcorrenciaPendencia") != null) {
			OcorrenciaPendencia ocorrenciaPendencia = new OcorrenciaPendencia();
			ocorrenciaPendencia.setIdOcorrenciaPendencia(parameters.getLong("ocorrenciaPendencia.idOcorrenciaPendencia"));
			bean.setOcorrenciaPendencia(ocorrenciaPendencia);
		}
    							
    	return ((OcorrenciaEntregaService)defaultService).store(bean);
    }
    
    
    /**
     * Retorna um dominio customizado, utilizado quando � realizada as tratativas do VOL 
     * @return
     */
	public List findDmTipoOcorrenciaEntrega(){
		List listRetorno = new ArrayList();
		List tpOcorrenciaList = domainValueService.findDomainValues("DM_TIPO_OCORRENCIA_ENTREGA");
		
		for(Iterator it = tpOcorrenciaList.iterator(); it.hasNext();){		
			DomainValue domainValue = (DomainValue)it.next();
			if((domainValue.getValue().equalsIgnoreCase("R")) ||(domainValue.getValue().equalsIgnoreCase("N"))){
				TypedFlatMap mapTpOcorrencia = new TypedFlatMap();
				mapTpOcorrencia.put("value", domainValue.getValue());				
				mapTpOcorrencia.put("description", domainValue.getDescription());
				listRetorno.add(mapTpOcorrencia);
			}
		}
		return listRetorno;
	}
    
    /**
	 * @return Returns the eventoService.
	 */
	public EventoService getEventoService() {
		return eventoService;
	}

	/**
	 * @param eventoService The eventoService to set.
	 */
	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

}
