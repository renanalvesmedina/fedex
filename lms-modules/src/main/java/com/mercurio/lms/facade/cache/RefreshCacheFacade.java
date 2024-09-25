package com.mercurio.lms.facade.cache;

import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.model.service.MenuService;
import com.mercurio.lms.configuracoes.model.dao.ImagemDAO;
import com.mercurio.lms.configuracoes.model.dao.ParametroGeralDAO;

/**
 * 
 * @author lucianos
 * @spring.bean id="lms.cache.refreshCacheFacade"
 */
public class RefreshCacheFacade {
	private ParametroGeralDAO parametroGeralDAO;
	private DomainService domainService;
	private MenuService menuService;
	private ImagemDAO imagemDAO;
	

	public void refreshParametroGeral() {
		parametroGeralDAO.refreshCache();
	}

	public void refreshImagem() {
		imagemDAO.refreshCache();
	}

	public void refreshDomainValue(){
		domainService.executeInvalidateCache();
	}

	public void refreshRecursoMensagem(){
		RecursoMensagemCache.setInitialized(false);		
	}
	public void refreshMenu() {
		menuService.flushMenuCache();
	}

	public void setParametroGeralDAO(ParametroGeralDAO parametroGeralDAO) {
		this.parametroGeralDAO = parametroGeralDAO;
	}

	public void setImagemDAO(ImagemDAO imagemDAO) {
		this.imagemDAO = imagemDAO;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
