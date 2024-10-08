package com.mercurio.lms.contratacaoveiculos.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.report.EmitirLocalizacaoMeioTransporteService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction"
 */

public class EmitirLocalizacaoMeioTransporteAction extends ReportActionSupport {
	private EmitirLocalizacaoMeioTransporteService emitirLocalizacaoMeioTransporteService;
	private RegionalService regionalService; 
	private MeioTransporteService meioTransporteService;
	private FilialService filialService;
	
	public EmitirLocalizacaoMeioTransporteService getEmitirLocalizacaoMeioTransporteService() {
		return emitirLocalizacaoMeioTransporteService;
	}

	public void setEmitirLocalizacaoMeioTransporteService(EmitirLocalizacaoMeioTransporteService emitirLocalizacaoMeioTransporteService) {
		this.reportServiceSupport = emitirLocalizacaoMeioTransporteService;
	}
	//METODOS DA TELA
	public List findComboRegional() {
		return regionalService.findRegional();
	}
	public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}
	public List findLookupMeioTransp(Map criteria) {
		return meioTransporteService.findLookup(criteria);
	}
	//siglaDescricao
	//METODOS GETTERS E SETTERS
	public RegionalService getRegionalService() {
		return regionalService;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

}
