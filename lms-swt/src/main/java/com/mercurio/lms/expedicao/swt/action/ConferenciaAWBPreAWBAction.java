package com.mercurio.lms.expedicao.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.util.ValidateUtils;

public class ConferenciaAWBPreAWBAction  extends CrudAction{
	
	private AwbService awbService;
	private CtoAwbService ctoAwbService;
	
	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public Boolean validateChaveAwb(Map<String, Object> parameters){
		return ValidateUtils.validateDigitoVerificadorNfe((String) parameters.get("nrChaveAwb"));
	}
	
	public Map<String, Object> findDadosByChaveAwb(Map<String, Object> parameters){
		String chaveAwb = (String) parameters.get("nrChaveAwb");
		List<Map<String, Object>> map = getAwbService().findDadosByChaveAwb(chaveAwb);
		if(!map.isEmpty()){
			Map<String, Object> mapa = map.get(0);
			setAwbFormatado (mapa);
			return mapa;
		}else{
			return findLogCargaAwbByChaveAwb(chaveAwb);
		}
	}

	private void setAwbFormatado(Map<String, Object> mapa) {
		if(mapa.get("numeroAwb") != null && mapa.get("serieAwb") != null && mapa.get("digitoAwb") != null && mapa.get("ciaAerea") != null){
			mapa.put("awbFormatado", mapa.get("ciaAerea") +  " " + 
					AwbUtils.getNrAwbFormated(mapa.get("serieAwb").toString(), 
											  Long.valueOf(mapa.get("numeroAwb").toString()), 
											  Integer.valueOf(mapa.get("digitoAwb").toString())));
		}else{
			mapa.put("awbFormatado", "");
		}
		
	}

	private Map<String, Object> findLogCargaAwbByChaveAwb(String chaveAwb) {
		String mensagem = getAwbService().findAwbLogCargaAwb(chaveAwb);
		Map<String, Object> retorno = new HashMap<String, Object>(); 
		if (!"".equals(mensagem)){
			retorno.put("hasLogCargaAwb", true);
			retorno.put("dsMensagem", mensagem);
		}else{
			retorno.put("hasLogCargaAwb", false);
		}
		return retorno;
	}
	
	public ResultSetPage findPaginatedPreAwbConferencia(Map criteria){
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getCtoAwbService().findPaginatedPreAwbConferencia((Long)criteria.get("idAwb"), findDef);
	}
	
	public Integer getRowCount(Map criteria) {
		return getCtoAwbService().getRowCount(criteria);
	}
	
	public void updateConferenciaAwb(Map criteria){
		FindDefinition findDef = FindDefinition.createFindDefinition(null);
		Long idAwb = (Long)criteria.get("idAwb");
		getCtoAwbService().updateConferenciaPreAwb(idAwb, findDef);
		getAwbService().updateConferenciaAwb(idAwb);
	}

}
