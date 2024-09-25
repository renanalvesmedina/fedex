package com.mercurio.lms.entrega.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.entrega.report.EmitirEntregasEfetuadasOrigemService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

public class EmitirEntregasEfetuadasOrigemAction extends ReportActionSupport {

	private FilialService filialService;
	private ServicoService servicoService; 
	private EmitirEntregasEfetuadasOrigemService emitirEntregasEfetuadasOrigemService; 

	public List findLookupFilial(Map criteria) {
		return this.filialService.findLookupFilial(criteria);
	}

	public Map findFilialUsuario(){
		Filial fi = SessionUtils.getFilialSessao();
		
		Map retorno = new HashMap();
		
		Map filial = new HashMap();
		filial.put("idFilial", fi.getIdFilial());
		filial.put("sgFilial", fi.getSgFilial());
				
		Map filial_pessoa = new HashMap();
		filial_pessoa.put("nmFantasia", fi.getPessoa().getNmFantasia());
		filial.put("pessoa", filial_pessoa);

		retorno.put("filial", filial);
		return retorno;
	}
	
	public List findServico(Map criteria){
		return this.servicoService.find(criteria);
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	public EmitirEntregasEfetuadasOrigemService getEmitirEntregasEfetuadasOrigemService() {
		return this.emitirEntregasEfetuadasOrigemService;
	}

	public void setEmitirEntregasEfetuadasOrigemService(EmitirEntregasEfetuadasOrigemService emitirEntregasEfetuadasOrigemService) {
		this.reportServiceSupport = emitirEntregasEfetuadasOrigemService;
	}
}