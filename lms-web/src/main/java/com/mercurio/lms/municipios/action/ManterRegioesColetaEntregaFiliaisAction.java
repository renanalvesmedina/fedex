package com.mercurio.lms.municipios.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterRegioesColetaEntregaFiliaisAction"
 */

public class ManterRegioesColetaEntregaFiliaisAction extends CrudAction {
	
	public void setRegiaoColetaEntregaFil(RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.defaultService = regiaoColetaEntregaFilService;
	}
		
	public TypedFlatMap findInfoUsuarioLogado() {
		TypedFlatMap info = new TypedFlatMap();
		
		Filial filial = SessionUtils.getFilialSessao();
		info.put("filial.idFilial",filial.getIdFilial());
		info.put("filial.sgFilial",filial.getSgFilial());
		info.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		
		return info;
	}
}
