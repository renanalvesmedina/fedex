package com.mercurio.lms.tributos.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIcmsService;
import com.mercurio.lms.tributos.report.EmitirAliquotasICMSService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.emitirAliquotasICMSAction"
 */

public class EmitirAliquotasICMSAction extends ReportActionSupport {

	private UnidadeFederativaService unidadeFederativaService;
	private TipoTributacaoIcmsService tipoTributacaoIcmsService;
	
	public void setEmitirAliquotasICMSService(EmitirAliquotasICMSService emitirAliquotasICMSService) {
		this.reportServiceSupport = emitirAliquotasICMSService;
	}
	
	public List findUnidadeFederativa(Map map){
		return this.getUnidadeFederativaService().findCombo(map);
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public List findComboTipoTributacao(TypedFlatMap tfm){
		return tipoTributacaoIcmsService.findComboTipoTributacaoIcms(Collections.EMPTY_LIST, null, null);
	}

	public void setTipoTributacaoIcmsService(
			TipoTributacaoIcmsService tipoTributacaoIcmsService) {
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}
	

}
