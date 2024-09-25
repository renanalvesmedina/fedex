package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.tntbrasil.integracao.domains.autenticacao.MenuItemDMN;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.util.session.SessionKey;

public class MenuLms {
	
	public MenuLms() {
		super();
	}
	
	public String getItens(String contextName, HttpServletRequest request) {
		List<MenuItemDMN> listMenuItems = SessionContext.get(SessionKey.MENU_KEY);
		StringBuffer out = new StringBuffer();
		
		buildMenu(listMenuItems, out, contextName);
		
		return out.toString();
	}
	
	private void buildMenu(List<MenuItemDMN> listMenuItems, StringBuffer menu, String contextName) {
		for (MenuItemDMN menuItemDMN : listMenuItems) {
			menu.append("{");
			menu.append(menuItemDMN.getTexto());
			if (menuItemDMN.getAcao() != null) {
				menu.append(" *javascript:RP('/" + contextName + "/" + menuItemDMN.getAcao() + "')");
			} else {
				menu.append("\n");
			}
			buildMenu(menuItemDMN.getChilds(), menu, contextName);
			menu.append("}");
			if (menuItemDMN.getAcao() != null) {
				menu.append("\n");
			}
		}
	}
}