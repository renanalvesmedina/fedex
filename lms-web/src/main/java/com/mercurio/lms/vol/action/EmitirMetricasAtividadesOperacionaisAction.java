package com.mercurio.lms.vol.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.VolGruposFrotas;
import com.mercurio.lms.vol.model.service.VolGruposFrotasService;
import com.mercurio.lms.vol.report.EmitirMetricasAtividadesOperacionaisService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.emitirMetricasAtividadesOperacionaisAction"
 */

public class EmitirMetricasAtividadesOperacionaisAction extends ReportActionSupport {
	private FilialService filialService;
	private VolGruposFrotasService volGruposFrotasService;
	public void setEmitirMetricasAtividadesOperacionaisService(EmitirMetricasAtividadesOperacionaisService emitirMetricasAtividadesOperacionaisService) {
		this.reportServiceSupport = emitirMetricasAtividadesOperacionaisService;
	}	
	
	public List findLookupFilialByUsuarioLogado(TypedFlatMap map) {
		List listFilial = this.filialService.findLookupByUsuarioLogado(map);
		
		if ( listFilial.isEmpty() ) {
			return listFilial;
		}
		
		List resultList = new ArrayList();
		
		Iterator iterator = listFilial.iterator();
		Map filial = (Map)iterator.next();
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		typedFlatMap.put("idFilial", filial.get("idFilial") );
		typedFlatMap.put("pessoa.nmFantasia", filial.get("pessoa.nmFantasia") );
		typedFlatMap.put("sgFilial", filial.get("sgFilial") );
		resultList.add(typedFlatMap);
		
		return resultList;
	}
	
	public List findLookupGrupo(Map map) {
		List result = getVolGruposFrotasService().findLookup(map);
		List resultList = new ArrayList();
		
		for (java.util.Iterator iter = result.iterator();iter.hasNext();) {
			TypedFlatMap resultMap = new TypedFlatMap();

			VolGruposFrotas gf = (VolGruposFrotas)iter.next();
			resultMap.put("idGrupoFrota", gf.getIdGrupoFrota());
			resultMap.put("dsNome", gf.getDsNome());
			resultMap.put("filial.idFilial", gf.getFilial().getIdFilial());
			resultMap.put("filial.sgFilial", gf.getFilial().getSgFilial());
			resultMap.put("filial.pessoa.nmFantasia", gf.getFilial().getPessoa().getNmFantasia());
			resultList.add(resultMap);
			
		}
		
		return resultList;
	}
	public TypedFlatMap findDataSession() {
		Filial bean = SessionUtils.getFilialSessao();
		TypedFlatMap result = new TypedFlatMap();
		
		result.put("filial.idFilial",bean.getIdFilial());
		result.put("filial.sgFilial",bean.getSgFilial());
		result.put("filial.pessoa.nmFantasia",bean.getPessoa().getNmFantasia());
		result.put("mesAno", JTDateTimeUtils.getDataAtual());
		return result;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setVolGruposFrotasService(VolGruposFrotasService volGruposFrotasService) {
		this.volGruposFrotasService = volGruposFrotasService;
	}
	public VolGruposFrotasService getVolGruposFrotasService() {
		return volGruposFrotasService;
	}	
}
