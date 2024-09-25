/**
 * 
 */
package com.mercurio.lms.contasreceber.action;

import java.util.List;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.service.CedenteService;
import com.mercurio.lms.contasreceber.model.service.OcorrenciaBancoService;
import com.mercurio.lms.contasreceber.report.EmitirOcorrenciasRetornoBancosService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Luis Carlos Poletto
 * 
 * @spring.bean id="lms.contasreceber.emitirOcorrenciasRetornoBancosAction"
 *
 */
public class EmitirOcorrenciasRetornoBancosAction extends ReportActionSupport {
	
	private FilialService filialService;
	private CedenteService cedenteService;
	private OcorrenciaBancoService ocorrenciaBancoService;
	
	public List findLookupFilial(TypedFlatMap criteria){
		String sgFilial = criteria.getString("sgFilial");
		String tpAcesso = criteria.getString("tpAcesso");
		return filialService.findLookupBySgFilial(sgFilial, tpAcesso);
	}
	
	public List findComboCedentes(TypedFlatMap criteria) {
		return cedenteService.findComboByActiveValues(criteria);
	}
	
	public List findComboOcorrenciasBanco(TypedFlatMap criteria) {
		Long idCedente = criteria.getLong("idCedente");
		String tpOcorrencia = "RET";
		return ocorrenciaBancoService.findComboByIdCedente(idCedente, tpOcorrencia);
	}
	
	public TypedFlatMap findDadosSessao() {
		TypedFlatMap result = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();
		result.put("filial.idFilial", filial.getIdFilial());
		result.put("filial.sgFilial", filial.getSgFilial());
		result.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());		
		return result;
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	
	public void setEmitirOcorrenciasRetornoBancosService(EmitirOcorrenciasRetornoBancosService emitirOcorrenciasRetornoBancosService) {
		this.reportServiceSupport = emitirOcorrenciasRetornoBancosService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setOcorrenciaBancoService(
			OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}

}
