package com.mercurio.lms.contasreceber.model.service;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;

public class CreditoBancarioPopulateService {

	private FilialService filialService;
	private UsuarioADSMService usuarioADSMService;
	private UsuarioService usuarioService;
	private BancoService bancoService;
	private ParametroGeralService parametroGeralService;

	public CreditoBancario populateCreditoBancario(CreditoBancario creditoBancario) {

		creditoBancario = populateUsuarioCreditoBancario(creditoBancario);

		creditoBancario = populateBancoCreditoBancario(creditoBancario);

		creditoBancario = populateFilialCreditoBancario(creditoBancario);
		
		return creditoBancario;
		
	}
	private CreditoBancario populateFilialCreditoBancario(CreditoBancario creditoBancario){

		if(creditoBancario.getIdFilial() == null){
			ParametroGeral idEmpresa = parametroGeralService.findByNomeParametro("ID_EMPRESA_MERCURIO");
			
			Filial filial = filialService.findBySgFilialAndIdEmpresa(creditoBancario.getSgFilial(), Long.valueOf(idEmpresa.getDsConteudo()));
			if(filial == null){
				return creditoBancario;
			}
			creditoBancario.setIdFilial(filial.getIdFilial());
		}
		
		return creditoBancario;
	}
	
	private CreditoBancario populateBancoCreditoBancario(CreditoBancario creditoBancario){
		if(creditoBancario.getIdBanco() == null){
			Banco banco = bancoService.findByNrBanco(creditoBancario.getNrBanco());
			if(banco == null){
				return creditoBancario;
			}
			creditoBancario.setIdBanco(banco.getIdBanco());
		}
		return creditoBancario;
	}

	private CreditoBancario populateUsuarioCreditoBancario(
			CreditoBancario creditoBancario) {
		UsuarioADSM usuarioADSM = usuarioADSMService.findUsuarioADSMByLogin(creditoBancario.getLogin());
		if(usuarioADSM == null){
			return creditoBancario;
		}
		Usuario usuario = usuarioService.findById(usuarioADSM.getIdUsuario());
		if(usuario == null){
			return creditoBancario;
		}
		creditoBancario.setIdUsuario(usuario.getIdUsuario());
		
		return creditoBancario;
	}
	
	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
