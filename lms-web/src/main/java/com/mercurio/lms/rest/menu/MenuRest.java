package com.mercurio.lms.rest.menu;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import br.com.tntbrasil.integracao.domains.autenticacao.MenuItemDMN;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.util.session.SessionKey;

@Path("/menu")
public class MenuRest {

	@InjectInJersey
	private AutenticacaoService autenticacaoService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	
	@GET
	@Produces("application/json")
	@Path("/getMenuUsuario")
	public Response getMenuUsuario(@Context HttpHeaders headers) {
		List<MenuItemDMN> listMenuItems = SessionContext.get(SessionKey.MENU_KEY);
		if(listMenuItems == null){
			listMenuItems = autenticacaoService.findMenu(integracaoJwtService.findAutenticacaoDMN(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
		}
		MenuItemDMN menu = new MenuItemDMN("Menu", null, 0);
		menu.setChilds(listMenuItems);
		MenuItem menuItem = buildMenu(menu, isIEBrowser(headers));
		if (menuItem != null) {
			return Response.ok(menuItem.getChilds()).build();
		}
		return Response.ok().build();
	}

	public MenuItem buildMenu(MenuItemDMN current,  boolean edge){
		MenuItem menuItem = new MenuItem(current.getTexto(),current.getAcao(),current.getLevel(),isNewFrontEnd(current.getAcao()));
		
		for (MenuItemDMN menuItemDMN : current.getChilds()) {
			MenuItem menuItemChild = buildMenu(menuItemDMN, edge);
			if (menuItemChild != null) {
				menuItem.addChild(menuItemChild);
			}
		}

		if (isItemValido(menuItem, edge)) {
			return menuItem;
		}
		return null;
	}
	
	private boolean isIEBrowser(HttpHeaders headers){		
		String userAgent = headers.getRequestHeader("user-agent").get(0);
		return userAgent.contains("MSIE") || userAgent.contains("Trident");
	}
	
	private boolean isItemValido(MenuItem menuItem, boolean ie){
		if(isMenuAgrupador(menuItem))
			return true;
		
		if(isMenuComAcao(menuItem))
			return true;

		if(ie)
			return true;
		
		return false;
	}

	private boolean isMenuComAcao(MenuItem menuItem) {
		return (menuItem.getChilds() == null || menuItem.getChilds().isEmpty()) && isNewFrontEnd(menuItem.getAcao());
	}

	private boolean isMenuAgrupador(MenuItem menuItem) {
		return menuItem.getAcao() == null  && menuItem.getChilds() != null && !menuItem.getChilds().isEmpty();
	}	
	
	private boolean isNewFrontEnd(String acao){
		if(acao == null)
			return false;
		
		return acao.indexOf(".do") == -1;
	}

	public void setAutenticacaoService(AutenticacaoService autenticacaoService) {
		this.autenticacaoService = autenticacaoService;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
