package com.mercurio.lms.coleta.action;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.gerarManifestosAutomaticamenteAction"
 */

public class GerarManifestosAutomaticamenteAction extends CrudAction {

	private ConfiguracoesFacade configuracoesFacade;
	
    public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
		this.defaultService = manifestoColetaService;
	}

    public Map gerarManifestosAutomaticamente(){
		Map map = new HashMap();
		
        int coletasProgramadas = ((ManifestoColetaService)defaultService).generateManifestosAutomaticamente();
        if (coletasProgramadas>0){
        	String[] args = {Integer.toString(coletasProgramadas)};
            map.put("mensagem", configuracoesFacade.getMensagem("LMS-02004", args));
            return map;
        } else {
        	map.put("mensagem", configuracoesFacade.getMensagem("LMS-02005"));
        }
        
        return map;
    }
}
