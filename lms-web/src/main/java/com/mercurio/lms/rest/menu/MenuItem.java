package com.mercurio.lms.rest.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Representa a estrutura de um nodo do menu.
 *
 */
public class MenuItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Jersey hot fix to ignore relationships between descendants. */
	@JsonIgnore
	private MenuItem parent;

	private List<MenuItem> childs;

	private String texto;
	private String acao;
	
	private long level;
	
	private Boolean novo;

	/**
	 * Default constructor.
	 * 
	 */
	public MenuItem() {
		this.childs = new ArrayList<MenuItem>();
		this.novo = Boolean.FALSE;
		
		this.parent = null;		
	}

	/**
	 * Define construtor com um item de menu pai.
	 * 
	 * @param parent
	 */
	public MenuItem(MenuItem parent) {
		this();
		this.parent = parent;
	}

	/**
	 * Define construtor com um item de menu pai e atributos.
	 * 
	 * @param parent
	 * @param texto
	 * @param acao
	 * @param level
	 * @param novo
	 */
	public MenuItem(MenuItem parent, String texto, String acao, long level, Boolean novo) {
		this(parent);

		this.texto = texto;
		this.acao = acao;
		this.level = level;
		this.novo = novo;
	}

	/**
	 * Define construtor com item de menu pai nulo e atributos.
	 * 
	 * @param texto
	 * @param acao
	 * @param level
	 * @param novo
	 */
	public MenuItem(String texto, String acao, long level, Boolean novo) {
		this(null, texto, acao, level, novo);
	}

	/**
	 * Retorna nível do menu.
	 * 
	 * @return long
	 */
	public long getLevel() {
		return level;
	}

	/**
	 * Define nível do menu.
	 * 
	 * @param level
	 */
	public void setLevel(long level) {
		this.level = level;
	}

	/**
	 * Retorna ação do item de menu.
	 * 
	 * @return acao
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * Define ação do item de menu.
	 * @param acao
	 */
	public void setAcao(String acao) {
		this.acao = acao;
	}

	/**
	 * Retorna nome do item de menu.
	 * 
	 * @return String
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Retorna se item de menu é do novo front-end
	 * .
	 * @return Boolean
	 */
	public Boolean getNovo() {
		return novo;
	}

	/**
	 * Define se item de menu é do novo front-end
	 * .
	 * @return Boolean
	 */
	public void setNovo(Boolean novo) {
		this.novo = novo;
	}
	
	/**
	 * Retorna item de menu pai.
	 * 
	 * @return MenuItem
	 */
	public MenuItem getParent() {
		return this.parent;
	}

	/**
	 * Define item de menu pai.
	 * 
	 * @param parent
	 */
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	/**
	 * Adiciona um filho ao item de menu.
	 * 
	 * @param child
	 */
	public void addChild(MenuItem child) {
		child.setParent(this);
		this.childs.add(child);
	}

	/**
	 * Retorna todos itens filhos do item.
	 * 
	 * @return List<MenuItem>
	 */
	public List<MenuItem> getChilds() {
		return this.childs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result + (int) (level ^ (level >>> 32));
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuItem other = (MenuItem) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (level != other.level)
			return false;
		if (texto == null) {
			if (other.texto != null)
				return false;
		} else if (!texto.equals(other.texto))
			return false;
		return true;
	}
	
}