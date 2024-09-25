package com.mercurio.lms.portaria.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.action.MdfeActionUtils;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.service.ManifestoEletronicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.service.OperacoesPortuariasService;
import com.mercurio.lms.util.session.SessionUtils;


public class OperacoesPortuariasAction extends CrudAction {

	private OperacoesPortuariasService OperacoesPortuariasService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private ManifestoEletronicoService manifestoEletronicoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public TypedFlatMap findFilialSessao(){
		
		TypedFlatMap retorno  = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();
		
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		
		return retorno;
	}
	 public List findLookupFilial(Map map) {
	    	FilterList filter = new FilterList(filialService.findLookup(map)) {
				public Map filterItem(Object item) {
					Filial filial = (Filial)item;
	    			TypedFlatMap typedFlatMap = new TypedFlatMap();
		    		typedFlatMap.put("idFilial", filial.getIdFilial());
			    	typedFlatMap.put("sgFilial",  filial.getSgFilial());
			    	typedFlatMap.put("pessoa.nmFantasia",  filial.getPessoa().getNmFantasia());
					return typedFlatMap;
				}
	    	};
	    	return (List)filter.doFilter();
	    }
	
	public List findGridEntrega(TypedFlatMap parametros){
		Long idFilial = parametros.getLong("idFilial");
		return getOperacoesPortuariasService().findGridEntrega(idFilial);
	}
	
	
	public List findGridColeta(TypedFlatMap parametros){
		Long idFilial = parametros.getLong("idFilial");
		return getOperacoesPortuariasService().findGridColeta(idFilial);
	}
	
	public void generateEntrega(TypedFlatMap parametros){
		Long idControleCarga = parametros.getLong("idControleCarga");
		controleCargaService.executeEntregaControleCargaPorto(idControleCarga);
	}
	
	public TypedFlatMap generateColeta(TypedFlatMap parametros){
		Long idMeioTransporte = parametros.getLong("idMeioTransporte");
		Long idControleCarga = parametros.getLong("idControleCarga");
		boolean contingenciaConfirmada = Boolean.TRUE.equals(parametros.getBoolean("contingenciaConfirmada"));
		return controleCargaService.executeColetaControleCargaPortoGeracaoMDFe(idControleCarga, idMeioTransporte, contingenciaConfirmada);
		
	}

	public void generateColetaAtualizaStatus(TypedFlatMap parametros){
		Long idControleCarga = parametros.getLong("idControleCarga");
		controleCargaService.executeColetaControleCargaPortoAtualizacaoStatus(idControleCarga);
		
	}
	
	public TypedFlatMap encerrarMdfesAutorizados(TypedFlatMap parameters) {
        return controleCargaService.executeEncerrarMdfesAutorizados(parameters.getLong("idControleCarga"));
    }
	
	public TypedFlatMap verificaAutorizacaoMdfe(TypedFlatMap parameters) {
	    return controleCargaService.executeVerificaAutorizacaoMdfe(parameters);
	}
	
	public TypedFlatMap verificaEncerramentoMdfe(TypedFlatMap parameters) {
	    return controleCargaService.executeVerificaEncerramentoMdfe(parameters.getLong("idManifestoEletronicoEncerrado"), parameters.getDateTime("dhEncerramento"));
	}
	
	public OperacoesPortuariasService getOperacoesPortuariasService() {
		return OperacoesPortuariasService;
	}

	public List findLookupMeioTransporte(Map criteria) {
    	List list = meioTransporteService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		MeioTransporte meioTransporte = (MeioTransporte)iter.next();
    		
    		verificaBloqueioMeioTransporte(meioTransporte);

    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
    		typedFlatMap.put("nrIdentificador", meioTransporte.getNrIdentificador());
    		typedFlatMap.put("nrFrota", meioTransporte.getNrFrota());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	private void verificaBloqueioMeioTransporte(MeioTransporte meioTransporte) {
		MeioTranspProprietario meioTranspProprietario = new MeioTranspProprietario();
		meioTranspProprietario.setMeioTransporte(meioTransporte);
		meioTranspProprietarioService.validateBloqueioMeioTransporte(meioTranspProprietario);
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public File imprimirMDFe(TypedFlatMap parameters) {
		
		List list = parameters.getList("idsManifestoEletronico");
		
		List<Long> idsManifestoEletronico = new ArrayList<Long>();
		for (Object o: list) {
			if (o instanceof String) {
				idsManifestoEletronico.add(Long.valueOf(o.toString()));
			} else if (o instanceof Long) {
				idsManifestoEletronico.add((Long)o);
			} else {
				throw new IllegalArgumentException("idsManifestoEletronico");
			}
		}
		
		
		List<ManifestoEletronico> manifestos = new ArrayList<ManifestoEletronico>();
		
		for(Long idManifestoEletronico : idsManifestoEletronico){
			manifestos.add((ManifestoEletronico) manifestoEletronicoService.findById(Long.valueOf(idManifestoEletronico)));
		}
		
		Object oObsContingMdfe1 = configuracoesFacade.getValorParametro("OBS_CONTING_MDFE1");
		String obsContingMdfe1 = oObsContingMdfe1 == null ? "" : oObsContingMdfe1.toString();
		Object oObsContingMdfe2 = configuracoesFacade.getValorParametro("OBS_CONTING_MDFE2");
		String obsContingMdfe2 = oObsContingMdfe2 == null ? "" : oObsContingMdfe2.toString();
		
		return MdfeActionUtils.imprimirMDFe(manifestos, obsContingMdfe1, obsContingMdfe2);

	}
	
	public void setOperacoesPortuariasService(
			OperacoesPortuariasService operacoesPortuariasService) {
		OperacoesPortuariasService = operacoesPortuariasService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setControleCargaService(
			ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setManifestoEletronicoService(ManifestoEletronicoService manifestoEletronicoService) {
		this.manifestoEletronicoService = manifestoEletronicoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
}
