package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.ppd.model.PpdAtendimentoUsuario;
import com.mercurio.lms.ppd.model.PpdGrupoAtendimento;
import com.mercurio.lms.ppd.model.service.PpdAtendimentoUsuarioService;

public class ManterAtendimentoUsuariosAction {
	private PpdAtendimentoUsuarioService atendimentoUsuarioService;		

	public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = atendimentoUsuarioService.findById(id).getMapped();
    	return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = atendimentoUsuarioService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdAtendimentoUsuario> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdAtendimentoUsuario item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}
	
	public Map<String, Object> store(Map<String, Object> bean) {		
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario((Long) bean.get("idUsuario"));
		
		PpdGrupoAtendimento grupo = new PpdGrupoAtendimento();
		grupo.setIdGrupoAtendimento((Long) bean.get("idGrupoAtendimento"));
		
		PpdAtendimentoUsuario atendimentoUsuario = new PpdAtendimentoUsuario();
		
		atendimentoUsuario.setUsuario(usuario);
		atendimentoUsuario.setGrupoAtendimento(grupo);
		atendimentoUsuario.setBlAtivo((Boolean) bean.get("blAtivo"));
		if(bean.get("idAtendimentoUsuario") != null) {
			atendimentoUsuario.setIdAtendimentoUsuario((Long)bean.get("idAtendimentoUsuario"));
		}
		atendimentoUsuarioService.store(atendimentoUsuario);
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idAtendimentoUsuario", atendimentoUsuario.getIdAtendimentoUsuario());
		
		return retorno;
	}
	 
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		atendimentoUsuarioService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		atendimentoUsuarioService.removeById(id);
	}
	
	// Set das classes de serviço
	public void setStatusReciboService(PpdAtendimentoUsuarioService atendimentoUsuarioService) {
		this.atendimentoUsuarioService = atendimentoUsuarioService;
	}	
}
