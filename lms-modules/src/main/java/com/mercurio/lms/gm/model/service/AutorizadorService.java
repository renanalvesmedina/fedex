package com.mercurio.lms.gm.model.service;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.gm.model.dao.AutorizadorDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.gm.autorizadorService"
 */
public class AutorizadorService  extends CrudService<UsuarioADSM, Long>{
	
	private UsuarioADSMService usuarioADSMService;
	private FuncionarioService funcionarioService;
	

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}


	public void setAutorizadorDao(AutorizadorDAO autorizadorDao) {
		setDao(autorizadorDao);
	}


	/**
	 * Valida o Usuario Autorizador. 
	 * 
	 * @author Samuel Alves
	 * @param String usuario
	 * @param String pim
	 * @return
	 */
	public Map<String, Object> autorizarPendencia(String usuario, String pin){
		Map map = new HashMap();
		Boolean autorizador = false;
		
		UsuarioADSM us = usuarioADSMService.findUsuarioADSMByLogin(usuario);
		
		if(us != null){
			pin = AuthenticationService.crypt(pin);       
			if(us.getDsPin().equals(pin)){
				Funcionario func = funcionarioService.findByIdUsuario(us.getIdUsuario());
				
				if(func != null){
					if((func.getCdCargo().equals("004") || func.getCdCargo().equals("009")) && func.getCdDepartamento().equals("84") && func.getTpSituacaoFuncionario().equals("A")){
						map.put("idUsuario", us.getIdUsuario());
						return map;
					}else{
						throw new BusinessException("LMS-04346");
					}
				}else{
					throw new BusinessException("LMS-04346");
				}
			}else{
				throw new BusinessException("LMS-40004");
			}	
		}else{
			throw new BusinessException("LMS-04346");
		}
	}
}
