package com.mercurio.lms.sgr.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.sgr.model.ExigenciaSmp;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.service.ExigenciaGerRiscoService;
import com.mercurio.lms.sgr.model.service.ExigenciaSmpService;
import com.mercurio.lms.sgr.model.service.SolicMonitPreventivoService;
import com.mercurio.lms.sgr.model.service.TipoExigenciaGerRiscoService;
import com.mercurio.lms.sgr.report.EmitirSMPService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.consultarSMPAction"
 */

public class ConsultarSMPAction extends ReportActionSupport {
	private FilialService filialService;
	private SolicMonitPreventivoService solicMonitPreventivoService;
	private MeioTransporteService meioTransporteService;
	private ControleCargaService controleCargaService;
	private ExigenciaGerRiscoService exigenciaGerRiscoService;
	private TipoExigenciaGerRiscoService tipoExigenciaGerRiscoService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private ExigenciaSmpService exigenciaSmpService;
	private MoedaService moedaService;

	private MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setReportService(EmitirSMPService emitirSMPService) {
		this.reportServiceSupport = emitirSMPService;
	}

	private ExigenciaSmpService getExigenciaSmpService() {
		return exigenciaSmpService;
	}

	public void setExigenciaSmpService(ExigenciaSmpService exigenciaSmpService) {
		this.exigenciaSmpService = exigenciaSmpService;
	}

	private RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}

	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}

	private TipoExigenciaGerRiscoService getTipoExigenciaGerRiscoService() {
		return tipoExigenciaGerRiscoService;
	}

	public void setTipoExigenciaGerRiscoService(
			TipoExigenciaGerRiscoService tipoExigenciaGerRiscoService) {
		this.tipoExigenciaGerRiscoService = tipoExigenciaGerRiscoService;
	}

	private ExigenciaGerRiscoService getExigenciaGerRiscoService() {
		return exigenciaGerRiscoService;
	}

	public void setExigenciaGerRiscoService(
			ExigenciaGerRiscoService exigenciaGerRiscoService) {
		this.exigenciaGerRiscoService = exigenciaGerRiscoService;
	}

	private ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	private MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	private SolicMonitPreventivoService getSolicMonitPreventivoService() {
		return solicMonitPreventivoService;
	}

	public void setSolicMonitPreventivoService(SolicMonitPreventivoService solicMonitPreventivoService) {
		this.solicMonitPreventivoService = solicMonitPreventivoService;
	}

	private FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public SolicMonitPreventivo findSMPById(Long idSMP) {
		return this.getSolicMonitPreventivoService().findSMPById(idSMP);
	}
	
	/**
     * Consulta a filial pela sigla informada 
     * @param map
     * @return
     */
    public List findLookupFilial(Map map) {
    	return getFilialService().findLookup(map);
    }    
	
    /**
     * M�todo de pesquisa para Manter SMP 
     * @param tfm
     * @param fd
     * @return
     * @author Rodrigo Antunes
     */
    public ResultSetPage findPaginatedSMP(TypedFlatMap tfm) {
    	ResultSetPage rsp = this.getSolicMonitPreventivoService().findPaginatedSMP(tfm, FindDefinition.createFindDefinition(tfm));
    	List lista = rsp.getList();
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		SolicMonitPreventivo smp = (SolicMonitPreventivo)iter.next();
    		ControleCarga cc = smp.getControleTrecho().getControleCarga();
    		if (cc.getRotaIdaVolta() != null && cc.getRotaIdaVolta().getRota() != null) {
    			cc.setRota(cc.getRotaIdaVolta().getRota());
    		}
    	}
    	rsp.setList(lista);
    	return rsp;
    }
    
    /**
     * row count para Manter SMP 
     * @param tfm
     * @return
     */
    public Integer getRowCountSMP(TypedFlatMap tfm) {
        return this.getSolicMonitPreventivoService().getRowCountSMP(tfm);
    }
    
    
    /**
     * lookup de veiculo 
     * @param map
     * @return
     */
    public List findLookupMeioTransporte(Map map) {
    	return getMeioTransporteService().findLookup(map);
    }
    
    /**
     * lookup de controle de carga 
     * @param criteria
     * @return
     */
    public List findControleCarga(Map criteria) {
    	FilterList filter = new FilterList(getControleCargaService().findLookup(criteria)) {
			public Map filterItem(Object item) {
				ControleCarga controleCarga = (ControleCarga)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idControleCarga", controleCarga.getIdControleCarga());
		    	typedFlatMap.put("nrControleCarga", controleCarga.getNrControleCarga());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }
    
    /**
     * Combo de exigencias 
     * @param map
     * @return
     */
    public List findExigenciaGerRisco(Map map) {
    	return getExigenciaGerRiscoService().findOrdenadoPorNivel(map);
    }
    
    /**
     * Combo de tipo de exigencias 
     * @param map
     * @return
     */
    public List findTipoExigenciaGerRisco(Map map) {
    	return getTipoExigenciaGerRiscoService().findOrdenadoPorDescricao(map);
    }
    
    public List findLookupRotaIdaVolta(Map map) {
    	return getRotaIdaVoltaService().findLookup(map);
    }
    
    /**
     * M�todo de pesquisa para aba de providencias
     * @param map
     * @return
     */
    public ResultSetPage findPaginatedExigenciaSmpByIdSmp(TypedFlatMap  tfm) {
    	Long idSmp = tfm.getLong("solicMonitPreventivo.idSolicMonitPreventivo"); 
    	return this.getExigenciaSmpService().findPaginatedExigenciaSmpByIdSmp(idSmp, FindDefinition.createFindDefinition(tfm));
    }
    
    /**
     * row count para findPaginatedExigenciaSmpByIdSmp
     * @param map
     * @return
     */
    public Integer getRowCountExigenciaSmpByIdSmp(TypedFlatMap tfm) {
    	Long idSmp = tfm.getLong("solicMonitPreventivo.idSolicMonitPreventivo"); 
    	return this.getExigenciaSmpService().getRowCountExigenciaSmpByIdSmp(idSmp);
    }
    
    /**
     * Remove as associa��es existentes de ExigenciaSmp com SolicmonitPreventivo e ExigenciaGerRisco  
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)    
    public void removeByIdsExigenciasSmp(List ids) {
    	this.getExigenciaSmpService().removeByIds(ids);
    }
    
    /**
     * Remove a ExigenciaSMP pelo seu id
     */
    public void removeByIdExigenciaSmp(Long id) {
    	this.getExigenciaSmpService().removeById(id);
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public ExigenciaSmp findExigenciaSmpById(Long id) {
    	return this.getExigenciaSmpService().findById(id);
    }
    
    /**
     * Salva a exigencia smp 
     * @param bean
     * @return
     */
    public  Serializable storeExigenciaSmp(ExigenciaSmp bean) {
    	return this.getExigenciaSmpService().store(bean);
    }
    
    /***
     * 
     * @param criteria
     * @return
     */
    public Map findExigenciaById(Map criteria) {
    	String idStr = (String) criteria.get("idExigenciaGerRisco");
    	Long id  = Long.valueOf(idStr);
    	Map map = new HashMap(1);
    	map.put("dsCompleta", getExigenciaGerRiscoService().findById(id).getDsCompleta() ) ;
    	return map;
    }
    
    public List findMoeda(Map map) {
    	return getMoedaService().find(map);
    }
    
    public List findSMPLookup(Map map) { 
    	return getSolicMonitPreventivoService().findLookup(map);
    }

}
