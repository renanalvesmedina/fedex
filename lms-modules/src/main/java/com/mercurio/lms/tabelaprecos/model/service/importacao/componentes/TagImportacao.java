package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TagImportacao extends ItemImportacao {

	private String tagImportacao;

	public TagImportacao(Integer linha, Integer coluna, String tag) {
		super(linha, coluna);
		this.tagImportacao = tag;
	}

	public String tag() {
		return this.tagImportacao;
	}

	@Override
	public String toString() {
		return String.format("Linha: %3d; Coluna: %3d; Tag: %s", linhaItem, colunaItem, tagImportacao);
	}

	public boolean igual(String tagComparavel) {
		return this.tagImportacao.equals(tagComparavel);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!o.getClass().isAssignableFrom(TagImportacao.class)) {
			return false;
		}
		TagImportacao outroTag = (TagImportacao) o;
		return new EqualsBuilder().append(this.tagImportacao, outroTag.tagImportacao).append(this.linhaItem, outroTag.linhaItem).append(this.colunaItem, outroTag.colunaItem).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.tagImportacao).append(this.linhaItem).append(this.colunaItem).toHashCode();
	}

	public static TagImportacao novoNulo() {
		return new TagImportacao(null, null, null);
	}

}
