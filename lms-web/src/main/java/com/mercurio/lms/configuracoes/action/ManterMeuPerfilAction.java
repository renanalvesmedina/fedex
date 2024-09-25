package com.mercurio.lms.configuracoes.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 * @spring.bean id="lms.seguranca.manterMeuPerfilAction"
 */
public class ManterMeuPerfilAction extends CrudAction {
	private UsuarioService usuarioService;
	private FilialService filialService;
	private EmpresaService empresaService;

	public List findLookupFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		return this.filialService.findLookupByEmpresaUsuarioLogado(m);
	}

	public ResultSetPage findPaginatedFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		return this.filialService.findPaginatedByEmpresaUsuarioLogado(m);
	}

	public Map<String,Object> findByEmpresaUsuarioLogadoMWW(Map m) {
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("_currentPage", "1");
		tfm.put("_pageSize", "9999");
		tfm.put("_order", "");
		tfm.put("empresa.idEmpresa", m.get("idEmpresa").toString());
		tfm.put("sgFilial", "");
				
		ResultSetPage rsp = this.filialService.findPaginatedByEmpresaUsuarioLogado(tfm);
		List<Map<String,Object>> lista = new ArrayList<Map<String,Object>>();
		for (Object item : rsp.getList()) {
			TypedFlatMap tfmItem = (TypedFlatMap) item;
			Map<String,Object> mapItem = new HashMap<String, Object>();
			mapItem.put("idFilial", tfmItem.get("idFilial"));
			mapItem.put("sgFilial", tfmItem.get("sgFilial"));
			mapItem.put("filialSgNm", tfmItem.get("filialSgNm"));
			lista.add(mapItem);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		
		map.put("filiais", lista);
		
		return map;
	}
	
	public Map<String,Object> findSgFilialByIdFilialMWW(Map m) {
		long idFilial = Long.parseLong(m.get("idFilial").toString());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sgFilial", this.filialService.findSgFilialByIdFilial(idFilial));
		return map;
	}

	public Integer getRowCountFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		return this.filialService.getRowCountByEmpresaUsuarioLogado(m);
	}

	public Map<String, Object> findEmpresasByUsuarioLogadoMWW(TypedFlatMap m) {
		Map<String,Object> mapRoot = new HashMap<String, Object>();
		List<Map> mapEmpresas = new ArrayList<Map>();
		List<TypedFlatMap> empresas = this.empresaService.findByUsuarioLogado(m);
		for (TypedFlatMap empresa : empresas) {
			Map<String, Object> mapPessoa = new HashMap<String, Object>();
			mapPessoa.put("nmPessoa", empresa.get("pessoa.nmPessoa"));
			
			Map<String, Object> mapEmpresa = new HashMap<String, Object>();
			mapEmpresa.put("idEmpresa", empresa.get("idEmpresa"));
			mapEmpresa.put("pessoa", mapPessoa);
			
			mapEmpresas.add(mapEmpresa);
		}
		mapRoot.put("empresas", mapEmpresas);
		return mapRoot;
	}

	public List findEmpresasByUsuarioLogado(TypedFlatMap m) {
		return this.empresaService.findByUsuarioLogado(m);
	}

	public Map findMeuPerfil() {
		return this.usuarioService.findMeuPerfil();
	}

	public void loadMeuPerfil(TypedFlatMap m) {
		this.usuarioService.loadMeuPerfil(m);
	}

	public void storeMeuPerfil(TypedFlatMap m) {
		this.usuarioService.storeMeuPerfil(m);
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}