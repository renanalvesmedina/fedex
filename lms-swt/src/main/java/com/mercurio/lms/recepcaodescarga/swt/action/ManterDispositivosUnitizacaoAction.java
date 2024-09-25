package com.mercurio.lms.recepcaodescarga.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.util.FormatUtils;

public class ManterDispositivosUnitizacaoAction extends CrudAction {
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
	private EmpresaService empresaService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;

    public void removeById(java.lang.Long id) {
        dispositivoUnitizacaoService.removeById(id);
    }       

    @Override
    public ResultSetPage findPaginated(Map criteria) {
    	if(criteria.get("tpNrIdentificacao")!=null && criteria.get("nrIdentificacao") != null){
    		criteria.put("nrIdentificacao", criteria.get("tpNrIdentificacao").toString().concat(criteria.get("nrIdentificacao").toString()));  
    	}
		ResultSetPage rsp = dispositivoUnitizacaoService.findPaginated(new PaginatedQuery(criteria));
		return rsp;
	}
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	dispositivoUnitizacaoService.removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(id);
    	Map<String,Object> dispositivoMapped = new HashMap<String, Object>();    	
    	dispositivoMapped.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
    	dispositivoMapped.put("tpNrIdentificacao", dispositivo.getNrIdentificacao().substring(0, 2));
    	dispositivoMapped.put("nrIdentificacao", dispositivo.getNrIdentificacao().substring(2));
    	dispositivoMapped.put("tpSituacao", dispositivo.getTpSituacao().getValue());
    	    	
    	dispositivoMapped.put("idTipoDispositivoUnitizacao", dispositivo.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
    	dispositivoMapped.put("dsTipoDispositivoUnitizacao", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());    	
    	    	    	
    	dispositivoMapped.put("idEmpresa", dispositivo.getEmpresa().getIdEmpresa());    	    	
    	dispositivoMapped.put("nrIdentificacaoFormatado",  FormatUtils.formatIdentificacao(dispositivo.getEmpresa().getPessoa()));
    	dispositivoMapped.put("nmPessoa", dispositivo.getEmpresa().getPessoa().getNmPessoa());    	  
    	
    	if(dispositivo.getLocalizacaoMercadoria() != null)
    		dispositivoMapped.put("dsLocalizacaoMercadoria", dispositivo.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
    	if(dispositivo.getLocalizacaoFilial() != null)
    		dispositivoMapped.put("sgFilialLocalizacaoMercadoria", dispositivo.getLocalizacaoFilial().getSgFilial());    	
    	if(dispositivo.getMacroZona() != null) {
    		dispositivoMapped.put("idMacroZona", dispositivo.getMacroZona().getIdMacroZona());
    		dispositivoMapped.put("dsMacroZona", dispositivo.getMacroZona().getDsMacroZona());
    		dispositivoMapped.put("nmPessoaTerminal", dispositivo.getMacroZona().getTerminal().getPessoa().getNmPessoa());
    		dispositivoMapped.put("sgFilialTerminal", dispositivo.getMacroZona().getTerminal().getFilial().getSgFilial());
    	}
    	if(dispositivo.getDispositivoUnitizacaoPai() != null) {    		
    		dispositivoMapped.put("idDispositivoUnitizacaoPai",dispositivo.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao());
    		dispositivoMapped.put("dsTipoDispositivoUnitizacaoPai",dispositivo.getDispositivoUnitizacaoPai().getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
    		dispositivoMapped.put("nrIdentificacaoPai",dispositivo.getDispositivoUnitizacaoPai().getNrIdentificacao());
    	}
    	
    	return dispositivoMapped;
    }

    public Serializable store(TypedFlatMap bean) {    	    	    	
    	DispositivoUnitizacao dispositivo = new DispositivoUnitizacao();    	
    	
    	if(bean.getLong("idDispositivoUnitizacao") != null) {
    		dispositivo = dispositivoUnitizacaoService.findById(bean.getLong("idDispositivoUnitizacao"));
    	}
    	
    	dispositivo.setNrIdentificacao(bean.getString("tpNrIdentificacao").concat(bean.getString("nrIdentificacao")));    	
    	dispositivo.setTpSituacao(new DomainValue(bean.getString("tpSituacao")));
    	
    	/* Seta o id do tipo de dispositivo */
    	TipoDispositivoUnitizacao tipoDispositivo = new TipoDispositivoUnitizacao();
    	tipoDispositivo.setIdTipoDispositivoUnitizacao(bean.getLong("idTipoDispositivoUnitizacao"));
    	dispositivo.setTipoDispositivoUnitizacao(tipoDispositivo);
    	
    	/* Seta o id da empresa */
    	Empresa empresa = new Empresa();
    	empresa.setIdEmpresa(bean.getLong("idEmpresa"));
    	dispositivo.setEmpresa(empresa);    	    
    	
    	return dispositivoUnitizacaoService.store(dispositivo);
    }
    
    public List findTiposDispositivoCombo(Map criteria) {
    	return tipoDispositivoUnitizacaoService.findCombo();
    }
    
    public List findLookupEmpresa(Map criteria) {
		Map mapPessoa = new HashMap();
		mapPessoa.put("nmPessoa", criteria.get("nmPessoa"));
		criteria.put("pessoa", mapPessoa);
		criteria.remove("nmPessoa");
			
		List result = empresaService.findLookup(criteria);
		List listReturn = new ArrayList();		
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Empresa empresa = (Empresa) iter.next();

			Map mapReturn = new HashMap();			
			mapReturn.put("idEmpresa", empresa.getIdEmpresa());
			mapReturn.put("nmPessoa", empresa.getPessoa().getNmPessoa());
						
			listReturn.add(mapReturn);
		}
		return listReturn;		
	}
    
    public ResultSetPage<Map<String, Object>> findPaginatedVolumesByDispositivoUnitizacao(Map<String, Object> criteria) {
    	return volumeNotaFiscalService.findPaginatedMap(new PaginatedQuery(criteria));
    }
    
    public Integer getRowCountVolumesByDispositivoUnitizacao(Map<String,Object> criteria) {
    	return volumeNotaFiscalService.getRowCount(criteria);
    }
    
    public ResultSetPage<Map<String, Object>> findPaginatedByDispositivoUnitizacaoPai(Map<String, Object> criteria) {    	
		return dispositivoUnitizacaoService.findPaginatedMap(new PaginatedQuery(criteria));	
    }

    public Integer getRowCountGrid(Map<String,Object> criteria) {
    	return dispositivoUnitizacaoService.getRowCount(criteria);    	
    }

    public Integer getRowCountByDispositivoUnitizacaoPai(Map<String,Object> criteria) {
    	return dispositivoUnitizacaoService.getRowCount(criteria);    	
    }
    
    public ResultSetPage<Map<String, Object>> findPaginatedEventos(Map<String, Object> criteria) {
    	return eventoDispositivoUnitizacaoService.findPaginatedMap(new PaginatedQuery(criteria));
    }
    
    public Integer getRowCountEventos(Map<String,Object> criteria) {
    	return eventoDispositivoUnitizacaoService.getRowCount(criteria);    	
    }
    
	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setTipoDispositivoUnitizacaoService(TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setEventoDispositivoUnitizacaoService(EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}		
}
