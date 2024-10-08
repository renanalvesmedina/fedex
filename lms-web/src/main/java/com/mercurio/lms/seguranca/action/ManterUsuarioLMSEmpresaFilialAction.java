package com.mercurio.lms.seguranca.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;
import com.mercurio.lms.configuracoes.model.service.EmpresaUsuarioService;
import com.mercurio.lms.configuracoes.model.service.FilialUsuarioService;
import com.mercurio.lms.configuracoes.model.service.RegionalUsuarioService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguranca.manterUsuarioLMSEmpresaFilialAction"
 */
public class ManterUsuarioLMSEmpresaFilialAction extends CrudAction {
	
	private FilialUsuarioService filialUsuarioService;
	private RegionalUsuarioService regionalUsuarioService; 	
	private DomainService ds;
	
	public DomainService getDs() {
		return ds;
	}

	public void setDs(DomainService ds) {
		this.ds = ds;
	}

	public void setService(EmpresaUsuarioService empresaUsuarioService) {
		this.defaultService = empresaUsuarioService;
	}
	
	public EmpresaUsuarioService getService() {
		return (EmpresaUsuarioService)this.defaultService;
	}	

	public void setFilialUsuarioService(FilialUsuarioService filialUsuarioService) {
		this.filialUsuarioService = filialUsuarioService;
	}

	public void setRegionalUsuarioService(RegionalUsuarioService regionalUsuarioService) {
		this.regionalUsuarioService = regionalUsuarioService;
	}

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = getService().findPaginatedFilialRegionalByEmpresa(criteria);
		return translatorDominios(rsp);
	}

	public Integer getRowCount(Map criteria) {
		Integer i = getService().getRowCountFilialRegionalByEmpresa(criteria);
		return i;
	}

	public Serializable store(TypedFlatMap map) {
		List listFiliais = new ArrayList();
		List listRegionais = new ArrayList();
		List all = map.getList("gridUsuario");
		
		for (Iterator it = all.iterator(); it.hasNext(); ) {
			TypedFlatMap mapI = (TypedFlatMap)it.next();
			
			Long id = mapI.getLong("id");
			boolean aprovaWorkFlow = (mapI.getString("blAprovaWorkflow").equals("S")) ? true : false;
			
			String tipo = (String)mapI.get("tipoReg");
			if ( tipo.startsWith("F")) {
				FilialUsuario fu = new FilialUsuario();
				fu.setIdFilialUsuario(id);
				fu.setBlAprovaWorkflow(Boolean.valueOf(aprovaWorkFlow));
				listFiliais.add(fu);
			} else {
				RegionalUsuario ru = new RegionalUsuario();
				ru.setIdRegionalUsuario(id);
				ru.setBlAprovaWorkflow(Boolean.valueOf(aprovaWorkFlow));
				listRegionais.add(ru);				
			}
		}
		
		
		filialUsuarioService.storeWorkFlowForAll(listFiliais);
		regionalUsuarioService.storeWorkFlowForAll(listRegionais);
		return null;
	}
	
	private ResultSetPage translatorDominios(ResultSetPage rsp) {
		Domain tipoWorkFlow = ds.findByName("TP_FILIAL_REGIONAL_USUARIO"); 
		for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			Map map = (Map)it.next();
			String key = (String)map.get("tipoReg");
			map.put("nmTipoReg", tipoWorkFlow.findDomainValueByValue(key).getDescription());
		}
		
		return rsp;
	}
	
}
