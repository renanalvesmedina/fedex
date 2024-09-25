package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.service.ContingenciaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

public class ContingenciaAction extends CrudAction {
	private ContingenciaService contingenciaService;
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;
	private ControleCargaService controleCargaService;


	public Map<String, Object> findById(java.lang.Long id) {
		Contingencia contingencia = contingenciaService.findById(id);


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idContingencia", contingencia.getIdContingencia());
		map.put("dsContingencia", contingencia.getDsContingencia());
		map.put("tpContingencia", contingencia.getTpContingencia().getValue());
		map.put("tpSituacao", contingencia.getTpSituacao().getValue());
		map.put("nmUsuarioSolicitante", ( contingencia.getUsuarioSolicitante() == null) ? "" : contingencia.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
		map.put("nmUsuarioAprovador", ( contingencia.getUsuarioAprovador() == null ) ? "" : contingencia.getUsuarioAprovador().getUsuarioADSM().getNmUsuario());
		map.put("nmUsuarioFinalizador", (contingencia.getUsuarioFinalizador() == null) ? "" : contingencia.getUsuarioFinalizador().getUsuarioADSM().getNmUsuario());
		map.put("dhSolicitacao", contingencia.getDhSolicitacao());
		map.put("dhAprovacao", contingencia.getDhAprovacao());
		map.put("dhFinalizacao", contingencia.getDhFinalizacao());
		map.put("idFilial", contingencia.getFilial().getIdFilial());
		map.put("sgFilial", contingencia.getFilial().getSgFilial());
		map.put("qtEmissoes", contingencia.getQtEmissoes());
		return map;
	}

	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria){
		ResultSetPage rsp = contingenciaService.findPaginatedContingencia(criteria);
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}



	public  Map<String, Object> storeSolicitacao(TypedFlatMap criteria){
		Contingencia contingencia = contingenciaService.storeSolicitacao(criteria);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idContingencia", contingencia.getIdContingencia());
		map.put("tpSituacao", contingencia.getTpSituacao().getValue());
		map.put("nmUsuarioSolicitante", SessionUtils.getUsuarioLogado().getNmUsuario());
		map.put("dsContingencia", contingencia.getDsContingencia());
		map.put("dhSolicitacao", contingencia.getDhSolicitacao());
		map.put("qtEmissoes", contingencia.getQtEmissoes());

		return map;
	}

	public Map<String, Object> storeAprovacao(TypedFlatMap criteria){
		Contingencia contingencia =  contingenciaService.storeAprovacao(criteria);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tpSituacao", contingencia.getTpSituacao().getValue());
		map.put("nmUsuarioAprovador", SessionUtils.getUsuarioLogado().getNmUsuario());
		map.put("dhAprovacao", contingencia.getDhAprovacao());
		map.put("qtEmissoes", contingencia.getQtEmissoes());

		return map;
	}
	
	public TypedFlatMap executeReenviaMDFeConting(TypedFlatMap criteria) {

		return contingenciaService.executeReenviaMDFeConting(criteria);
	}

	public Map<String, Object> storeFinalizacao(TypedFlatMap criteria){
		
		if ("M".equals(criteria.getString("tpContingencia")) && !Boolean.TRUE.equals(criteria.getBoolean("blReenviaMDFeContingOk"))) {
			TypedFlatMap map = executeReenviaMDFeConting(criteria);
			if (Boolean.TRUE.equals(map.getBoolean("aguardarAutorizacaoMdfe"))) {
				return map;
			}
		}
		
		Contingencia contingencia  =  contingenciaService.storeFinalizacao(criteria);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tpSituacao", contingencia.getTpSituacao().getValue());
		map.put("nmUsuarioFinalizador", SessionUtils.getUsuarioLogado().getNmUsuario());
		map.put("dhFinalizacao", contingencia.getDhFinalizacao());
		map.put("qtEmissoes", contingencia.getQtEmissoes());

		return map;
	}

	public TypedFlatMap verificaAutorizacaoMdfe(TypedFlatMap parameters) {
	    return controleCargaService.executeVerificaAutorizacaoMdfes(parameters);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return contingenciaService.getRowCountContingencia(criteria);
	}
	
	public TypedFlatMap findDescContingenciaMdfe(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("descContingenciaMdfe", configuracoesFacade.getValorParametro("DESC_CONTING_MDFE"));
		return tfm;
	}

	public void setContingenciaService(
			ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void validateProcessarClienteEdi(Map<String, Object> criteria) {
		contingenciaService.validateProcessarClienteEdi((Long)criteria.get("idCliente"));
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	
}
