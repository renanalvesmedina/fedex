package com.mercurio.lms.layoutNfse.model.rps;

import java.util.ArrayList;
import java.util.List;

public class ListaItens {
	private List<Item> Item = new ArrayList<Item>();

	public List<Item> getItem() {
		return Item;
	}
	
	public void addItem(Item item){
		this.Item.add(item);
	}

}
