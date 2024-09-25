package com.mercurio.lms.carregamento.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Transponder;
import com.mercurio.lms.carregamento.model.Transponder.SITUACAO_TRANSPONDER;
import com.mercurio.lms.carregamento.model.service.TransponderService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.vincularTransponderControleCargaAction"
 */
public class VincularTransponderControleCargaAction extends CrudAction  {


	/**
	 * Declara��o servi�o principal da Action.
	 * 
	 * @param transponderService
	 */
	public void setTransponderService(TransponderService transponderService) {
		defaultService =  transponderService;
	}
	public TransponderService getTransponderService() {
		return (TransponderService) defaultService;
	}
	
	public Serializable store(TypedFlatMap tfm) {
		getTransponderService().storeVincularTransponderControleCarga(tfm.getLong("transponder.idTransponder"), tfm.getLong("controleCarga.idControleCarga"));
		return tfm; 
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		criteria.put("idTransponder", ((Map)criteria.get("transponder")).get("idTransponder"));
		criteria.put("tpSituacaoTransponder", SITUACAO_TRANSPONDER.EM_USO.getValue());
		return super.findPaginated(criteria);
	}
	
	public Serializable findById(TypedFlatMap bean) {
		Transponder transponder = (Transponder) getTransponderService().findById(bean.getLong("idTransponder"));
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("controleCarga.idControleCarga", transponder.getControleCarga().getIdControleCarga());
		tfm.put("controleCarga.nrControleCarga", transponder.getControleCarga().getNrControleCarga());
		tfm.put("controleCarga.filialByIdFilialOrigem.sgFilial", transponder.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
		tfm.put("controleCarga.filialByIdFilialOrigem.idFilial", transponder.getControleCarga().getFilialByIdFilialOrigem().getIdFilial());
		
		tfm.put("transponder.idTransponder", transponder.getIdTransponder());
		tfm.put("transponder.nrTransponder", transponder.getNrTransponder());
		tfm.put("idTransponder", transponder.getIdTransponder());
		
		return tfm;
	}
	
	public void removeById(TypedFlatMap id) {
		getTransponderService().removeVinculoIdTransponder(id.getLong("idTransponder"));
	}
	
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getTransponderService().removeVinculoIdsTransponder(ids);
	}
}