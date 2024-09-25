package com.mercurio.lms.fretecarreteiro.swt.action;

import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.GerarNotaCreditoService;
import com.mercurio.lms.util.session.SessionUtils;

public class GerarNotaCreditoParceiraAction extends CrudAction {

	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";
	private GerarNotaCreditoService gerarNotaCreditoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;

	@Override
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(Map criteria) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		if(isCalculoPadrao(idFilial)){
			throw new BusinessException("LMS-25131");
		}
		
		return getControleCargaService().findPaginatedByParceiro(new TypedFlatMap(criteria) , FindDefinition.createFindDefinition(criteria));
	}
	
	private boolean isCalculoPadrao(Long idFilial){	
		boolean calculoPadrao = false;
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(idFilial, PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
		return calculoPadrao;		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Integer getRowCount(Map criteria) {
		return getControleCargaService().getRowCountParceiros(new TypedFlatMap(criteria));
	}

	@SuppressWarnings("rawtypes")
	public Map geraNotaCreditoParceira(Map criteria){
		return gerarNotaCreditoService.generateNotaCreditoParceira(criteria);
	}

	public void setService(ControleCargaService controleCargaService) {
		this.defaultService = controleCargaService;
	}

	public ControleCargaService getService() {
		return (ControleCargaService) this.defaultService;
	}

	private ControleCargaService getControleCargaService() {
		return ((ControleCargaService) getDefaultService());
	}

	public GerarNotaCreditoService getGerarNotaCreditoService() {
		return gerarNotaCreditoService;
	}

	public void setGerarNotaCreditoService(GerarNotaCreditoService gerarNotaCreditoService) {
		this.gerarNotaCreditoService = gerarNotaCreditoService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
}
