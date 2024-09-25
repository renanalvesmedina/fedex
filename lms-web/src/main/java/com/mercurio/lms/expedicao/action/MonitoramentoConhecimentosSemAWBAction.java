package com.mercurio.lms.expedicao.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


public class MonitoramentoConhecimentosSemAWBAction extends CrudAction{
	
	private ConhecimentoService conhecimentoService;
	
	/**
	 * Método responsável por consultar os conhecimentos sem vinculo com AWB 
	 */
	public List findConhecimentosSemVinculoAWB(TypedFlatMap criteria){
		List result = conhecimentoService.findConhecimentosSemAWB(criteria);
		
 		return result;
	}
	
    public TypedFlatMap loadFilialUsuarioLogado() {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	if(!SessionUtils.isFilialSessaoMatriz()) {
    		Filial f = SessionUtils.getFilialSessao();
    		retorno.put("idFilial",f.getIdFilial());
    		retorno.put("sgFilial",f.getSgFilial());
    		retorno.put("pessoa.nmFantasia",f.getPessoa().getNmFantasia());
		}

    	retorno.put("dataInicial", JTDateTimeUtils.getDataAtual().minusDays(30));
		retorno.put("dataFinal", JTDateTimeUtils.getDataAtual());    	
		retorno.put("filialSessaoMatriz", SessionUtils.isFilialSessaoMatriz());    	
    	
    	return retorno;
    }	
	
	@SuppressWarnings("unchecked")
	public List<TypedFlatMap> findDimensoes(TypedFlatMap criteria) {
		return conhecimentoService.findDimensoes(criteria);
	}
	
	public TypedFlatMap findTotalizadores(TypedFlatMap criteria){
		TypedFlatMap dadosTotalizadores = conhecimentoService.findTotalizadores(criteria);
		
		return dadosTotalizadores;
	}
	
	public void consolidarCarga(TypedFlatMap data) {
		List received = data.getList("conhecimentos");
		long cont = 0;
		
		if (received == null || received.isEmpty()) {
			throw new BusinessException("LMS-04159");
		}
		
		Awb awb = new Awb();
		Map ctoAwbs = getCtrcInSession();
		
		if (ctoAwbs != null) {
			ctoAwbs.clear();
		}

		for (int i = 0; i < received.size(); i++) {
			Long idConhecimento = Long.valueOf((String) received.get(i));
			Conhecimento conhecimento = conhecimentoService.findByIdPreAwb(idConhecimento);
			CtoAwb ctoAwb = new CtoAwb();
			ctoAwb.setConhecimento(conhecimento);
			ctoAwb.setIdCtoAwb(Long.valueOf(--cont));
			ctoAwbs.put(ctoAwb.getIdCtoAwb(), ctoAwb);
		}

		setCtrcInSession(ctoAwbs);		
	}  
	
	private Map getCtrcInSession(){
    	Map ctosAwbMap = (Map) SessionContext.get(ConstantesExpedicao.CTOs_AWB_IN_SESSION);
		if(ctosAwbMap == null) {
			ctosAwbMap = new HashMap(1);
		}
		setCtrcInSession(ctosAwbMap);
		return ctosAwbMap; 
    }
	
	 private void setCtrcInSession(Map ctosAwbMap) {
		 SessionContext.set(ConstantesExpedicao.CTOs_AWB_IN_SESSION, ctosAwbMap);
	 }

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
}
