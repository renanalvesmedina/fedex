package com.mercurio.lms.expedicao.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * ...
 * @autor juliosce
 * @since 13/02/2007 18:39:05
 */
public class TokenGroup {
	private List<String> possibities;
	private Integer key;

	public TokenGroup(Integer key){
		this.key = key;
		possibities = new ArrayList<String>(3);
	}

	public TokenGroup(Integer key, String...strings){
		this(key);
		for(String word : strings) add(word);
	}

	public boolean add(String possibitie) {
		if(!possibities.contains(possibitie)) return possibities.add(possibitie);
		return false;
	}

	public String get(int index) {
		return possibities.get(index);
	}

	public Iterator<String> iterator() {
		return possibities.iterator();
	}

	public int size() {
		return possibities.size();
	}

	public int getKey() {
		return key;
	}
}