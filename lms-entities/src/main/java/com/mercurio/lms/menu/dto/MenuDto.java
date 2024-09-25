package com.mercurio.lms.menu.dto;

import java.util.ArrayList;
import java.util.List;


public class MenuDto extends ItemMenuDto {
	
	private static final long serialVersionUID = 1L;
	
	private List<ItemMenuDto> itens = new ArrayList<ItemMenuDto>();
	
	public MenuDto() {}
	
	public MenuDto(String texto, String acao, Long level) {
		super(texto, acao, level);
	}
	
	public void addFilho(String texto, String acao, Long level, boolean hasFilhos) {
		if (level == this.getLevel()+1) {
			if (hasFilhos) {
				itens.add(new MenuDto(texto, acao, level));
			} else {
				itens.add(new ItemMenuDto(texto, acao, level));
			}
		} else {
			((MenuDto)itens.get(itens.size()-1)).addFilho(texto, acao, level, hasFilhos);
		}
	}

	public List<ItemMenuDto> getItens() {
		return itens;
	}
	
	public void setItens(List<ItemMenuDto> itens) {
		this.itens = itens;
	}

}
