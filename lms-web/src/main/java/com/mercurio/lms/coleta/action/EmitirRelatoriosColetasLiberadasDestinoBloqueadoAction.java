package com.mercurio.lms.coleta.action;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.report.EmitirRelatoriosColetasLiberadasDestinoBloqueadoService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.JTDateTimeUtils;



/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.emitirRelatoriosColetasLiberadasDestinoBloqueadoAction"
 */

public class EmitirRelatoriosColetasLiberadasDestinoBloqueadoAction extends ReportActionSupport {
	
	private MunicipioService municipioService;
	
	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setEmitirCartaOcorrenciaReportService(EmitirRelatoriosColetasLiberadasDestinoBloqueadoService emitirRelatoriosColetasLiberadasDestinoBloqueadoService) {
		this.reportServiceSupport = emitirRelatoriosColetasLiberadasDestinoBloqueadoService;
	}
	
	public List findLookupMunicipio(Map criteria) {
		return this.getMunicipioService().findLookup(criteria);
	}
	
	/**
	 * Busca dados basicos da tela...
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getBasicData(TypedFlatMap criteria) {
		
		TypedFlatMap result = new TypedFlatMap();
		
		YearMonthDay dhInicial = new YearMonthDay(
				JTDateTimeUtils.getDataHoraAtual().getYear() , 
				JTDateTimeUtils.getDataHoraAtual().getMonthOfYear(),
				1); 
		
		result.put("dhInicial", dhInicial);
		result.put("dhFinal", JTDateTimeUtils.getDataAtual());
		
		return result;
	}

}
