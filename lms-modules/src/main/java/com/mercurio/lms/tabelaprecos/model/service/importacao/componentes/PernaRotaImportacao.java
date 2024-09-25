package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_AERO_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_AERO_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_FILIAL_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_FILIAL_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_GRUPO_REG_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_GRUPO_REG_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_MUNICIPIO_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_MUNICIPIO_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_PAIS_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_PAIS_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_COML_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_COML_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_TP_LOCAL_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_UF_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_UF_ORIGEM;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_ZONA_DESTINO;
import static com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco.TAG_ZONA_ORIGEM;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.excessao.ErroImportacaoUtils;

public class PernaRotaImportacao {
	
	public enum TipoPernaRotaImportacao { 
		ORIGEM("ORIGEM", TAG_AERO_ORIGEM, TAG_FILIAL_ORIGEM, TAG_GRUPO_REG_ORIGEM, TAG_MUNICIPIO_ORIGEM, TAG_PAIS_ORIGEM, TAG_TP_LOCAL_ORIGEM, TAG_TP_LOCAL_COML_ORIGEM, TAG_UF_ORIGEM, TAG_ZONA_ORIGEM), 
		DESTINO("DESTINO", TAG_AERO_DESTINO, TAG_FILIAL_DESTINO, TAG_GRUPO_REG_DESTINO, TAG_MUNICIPIO_DESTINO, TAG_PAIS_DESTINO, TAG_TP_LOCAL_DESTINO, TAG_TP_LOCAL_COML_DESTINO, TAG_UF_DESTINO, TAG_ZONA_DESTINO);
		private final String nomeTipo;
		
		private final String tagAeroporto;
		private final String tagFilial;
		private final String tagGrupoRegiao;
		private final String tagMunicipio;
		private final String tagPais;
		private final String tagTipoLocal;
		private final String tagTipoLocalComercial;
		private final String tagUf;
		private final String tagZona;

		TipoPernaRotaImportacao(String nome, String tagAeroporto, String tagFilial, String tagGrupoRegiao, String tagMunicipio, 
				String tagPais, String tagTipoLocal, String tagTipoLocalComercial, String tagUf, String tagZona) {
			this.nomeTipo = nome;
			this.tagAeroporto = tagAeroporto;
			this.tagFilial = tagFilial;
			this.tagGrupoRegiao = tagGrupoRegiao;
			this.tagMunicipio = tagMunicipio;
			this.tagPais = tagPais;
			this.tagTipoLocal = tagTipoLocal;
			this.tagTipoLocalComercial = tagTipoLocalComercial;
			this.tagUf = tagUf;
			this.tagZona = tagZona;
		}
		public String nome() {
			return this.nomeTipo;
		}
		public String tagAeroporto() {
			return this.tagAeroporto;
		}
		public String tagFilial() {
			return this.tagFilial;
		}
		public String tagGrupoRegiao() {
			return this.tagGrupoRegiao;
		}
		public String tagMunicipio() {
			return this.tagMunicipio;
		}
		public String tagPais() {
			return this.tagPais;
		}
		public String tagTipoLocal() {
			return this.tagTipoLocal;
		}
		public String tagTipoLocalComercial() {
			return this.tagTipoLocalComercial;
		}
		public String tagUf() {
			return this.tagUf;
		}
		public String tagZona() {
			return this.tagZona;
		}
		public boolean combina(String tag) {
			return this.tagAeroporto.equals(tag) || 
					this.tagFilial.equals(tag) || 
					this.tagGrupoRegiao.equals(tag) || 
					this.tagMunicipio.equals(tag) || 
					this.tagPais.equals(tag) || 
					this.tagTipoLocal.equals(tag) || 
					this.tagTipoLocalComercial.equals(tag) || 
					this.tagUf.equals(tag) || 
					this.tagZona.equals(tag);
		}
	}
	
	private Map<String, ValorImportacao> itens;
	private final TipoPernaRotaImportacao tipo;
	private int primeiraLinha = 0;
	private int primeiraColuna = Integer.MAX_VALUE;
	
	public PernaRotaImportacao(TipoPernaRotaImportacao tipo) {
		this.tipo = tipo;
	}
	
	public void incluiValor(ValorImportacao valor) {
		if (!ValorImportacao.estahInformado(valor)) {
			return;
		}
		if (!this.tipo.combina(valor.tag())) {
			throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("A tag '%s' não é uma tag de %s válida.", valor.tag(), this.tipo.nome().toLowerCase()));
		}
		iniciaItens();
		if (this.itens.containsKey(valor.tag())) {
			ValorImportacao valorExistente = this.itens.get(valor.tag());
			throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("O valor para a tag '%s' já foi informado na célula %s%d.",
							valor.tag(), this.converteColuna(valorExistente.coluna()), valorExistente.linha()+1));
		}
		this.itens.put(valor.tag(), valor);
		this.primeiraLinha = valor.linha();
		this.primeiraColuna = Math.min(this.primeiraColuna, valor.coluna());
	}
	
	private String converteColuna(int coluna) {
		return new ErroImportacaoUtils().converteColuna(coluna);
	}

	private void iniciaItens() {
		if (this.itens == null) {
			this.itens = new HashMap<String, ValorImportacao>();
		}
	}
	
	public void valida() {
		this.validaZonaPais();
		this.validaUf();
		this.validaAeroporto();
		this.validaFilial();
		this.validaMunicipio();
		this.validaTipoLocalizacao();
		this.validaTipoLocalComercial();
		this.validaGrupoRegiao();
		
	}
	
	public boolean estahCompleta() {
		return this.contem(this.tipo.tagUf());
	}
	
	private void validaZonaPais() {
		if (!this.contem(this.tipo.tagPais()) && !this.contem(this.tipo.tagZona())) {
			return;
		}
		if (!this.contem(this.tipo.tagZona())) {
			ValorImportacao valor = this.buscaPrimeiro(this.tipo.tagPais());
			throw new ImportacaoException(valor.coluna(), valor.linha(), "Para informar um país, você deve informar também a zona.");
		}
	}

	private void validaUf() {
		if (!this.contem(this.tipo.tagUf())) {
			throw new ImportacaoException(this.primeiraColuna, this.primeiraLinha, String.format("O valor de uf %s é obrigatório.", this.tipo.nome().toLowerCase()));
		}
	}

	private void validaAeroporto() {
		if (!this.contem(this.tipo.tagAeroporto())) {
			return;
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagFilial(), this.tipo.tagMunicipio(), this.tipo.tagTipoLocal(), this.tipo.tagTipoLocalComercial(), this.tipo.tagGrupoRegiao());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagAeroporto()));
		}
	}
	
	private void validaFilial() {
		if (!this.contem(this.tipo.tagFilial())) {
			return;
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagAeroporto(), this.tipo.tagTipoLocal(), this.tipo.tagTipoLocalComercial(), this.tipo.tagGrupoRegiao());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagFilial()));
		}
		
	}

	private void validaMunicipio() {
		if (!this.contem(this.tipo.tagMunicipio())) {
			return;
		}
		if (!this.contem(this.tipo.tagFilial())) {
			ValorImportacao municipio = buscaPrimeiro(this.tipo.tagMunicipio());
			throw new ImportacaoException(municipio.coluna(), municipio.linha(), "Para informar o município, você deve informar também a filial.");
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagAeroporto(), this.tipo.tagTipoLocal(), this.tipo.tagTipoLocalComercial(), this.tipo.tagGrupoRegiao());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagMunicipio()));
		}
		
	}

	private void validaTipoLocalizacao() {
		if (!this.contem(this.tipo.tagTipoLocal())) {
			return;
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagAeroporto(), this.tipo.tagFilial(), this.tipo.tagMunicipio(), this.tipo.tagTipoLocalComercial(), this.tipo.tagGrupoRegiao());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagTipoLocal(), this.tipo.nome()));
		}
	}

	private void validaTipoLocalComercial() {
		if (!this.contem(this.tipo.tagTipoLocalComercial())) {
			return;
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagAeroporto(), this.tipo.tagFilial(), this.tipo.tagMunicipio(), this.tipo.tagTipoLocal(), this.tipo.tagGrupoRegiao());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagTipoLocalComercial(), this.tipo.nome()));
		}
		
	}

	private void validaGrupoRegiao() {
		if (!this.contem(this.tipo.tagGrupoRegiao())) {
			return;
		}
		ValorImportacao primeiro = buscaPrimeiro(this.tipo.tagAeroporto(), this.tipo.tagFilial(), this.tipo.tagMunicipio(), this.tipo.tagTipoLocal(), this.tipo.tagTipoLocalComercial());
		if (primeiro != null) {
			throw new ImportacaoException(primeiro.coluna(), primeiro.linha(), 
					String.format("Valores(s) incompatíveis(s): o valor para '[#%s%s]' já foi informado. Você deve informar uma configuração de rota válida.", this.tipo.tagGrupoRegiao(), this.tipo.nome()));
		}
		
	}
	
	private boolean contem(String... tags) {
		return this.buscaPrimeiro(tags) != null;
	}
	
	private ValorImportacao buscaPrimeiro(final String... tags) {
		for (String tag : tags) {
			if (this.itens.containsKey(tag)) {
				return this.itens.get(tag);
			}
		}
		return null;
	}

	public ValorImportacao aeroportoRota() {
		if (this.itens.containsKey(this.tipo.tagAeroporto())) {
			return this.itens.get(this.tipo.tagAeroporto());
		}
		return null;
	}

	public ValorImportacao zonaRota() {
		if (this.itens.containsKey(this.tipo.tagZona())) {
			return this.itens.get(this.tipo.tagZona());
		}
		return null;
	}
	
	public ValorImportacao paisRota() {
		if (this.itens.containsKey(this.tipo.tagPais())) {
			return this.itens.get(this.tipo.tagPais()); 
		}
		return null;
	}
	
	public ValorImportacao ufRota() {
		if (this.itens.containsKey(this.tipo.tagUf())) {
			return this.itens.get(this.tipo.tagUf());
		}
		return null;
	}
	
	public ValorImportacao filialRota() {
		if (this.itens.containsKey(this.tipo.tagFilial())) {
			return this.itens.get(this.tipo.tagFilial());
		}
		return null;
	}
	
	public ValorImportacao municipioRota() {
		if (this.itens.containsKey(this.tipo.tagMunicipio())) {
			return this.itens.get(this.tipo.tagMunicipio());
		}
		return null;
	}
	
	public ValorImportacao tipoLocalizacaoRota() {
		if (this.itens.containsKey(this.tipo.tagTipoLocal())) {
			return this.itens.get(this.tipo.tagTipoLocal());
		}
		return null;
	}
	
	public ValorImportacao tipoLocalComercialRota() {
		if (this.itens.containsKey(this.tipo.tagTipoLocalComercial())) {
			return this.itens.get(this.tipo.tagTipoLocalComercial());
		}
		return null;
	}
	
	public ValorImportacao grupoRegiaoRota() {
		if (this.itens.containsKey(this.tipo.tagGrupoRegiao())) {
			return this.itens.get(this.tipo.tagGrupoRegiao());
		}
		return null;
	}

	public int linha() {
		return this.primeiraLinha;
	}

	public int coluna() {
		return this.primeiraColuna;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!PernaRotaImportacao.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		PernaRotaImportacao perna = (PernaRotaImportacao) o;
		return new EqualsBuilder().append(this.aeroportoRota(), perna.aeroportoRota())
								  .append(this.filialRota(), perna.filialRota())
								  .append(this.grupoRegiaoRota(), perna.grupoRegiaoRota())
								  .append(this.municipioRota(), perna.municipioRota())
								  .append(this.paisRota(), perna.paisRota())
								  .append(this.tipoLocalizacaoRota(), perna.tipoLocalizacaoRota())
								  .append(this.tipoLocalComercialRota(), perna.tipoLocalComercialRota())
								  .append(this.ufRota(), perna.ufRota())
								  .append(this.zonaRota(), perna.zonaRota())
								  .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.aeroportoRota())
								  .append(this.filialRota())
								  .append(this.grupoRegiaoRota())
								  .append(this.municipioRota())
								  .append(this.paisRota())
								  .append(this.tipoLocalizacaoRota())
								  .append(this.tipoLocalComercialRota())
								  .append(this.ufRota())
								  .append(this.zonaRota())
								  .toHashCode();
	}
	
	
	@Override
	public String toString() {
		StringBuilder toStringBuilder = new StringBuilder(this.getClass().getSimpleName()).append("[").append(this.tipo.nome()).append("]: ");
		Pattern pattern = Pattern.compile("(\\[#)([A-Z]+)((ORIGEM)|(DESTINO))(\\])");
		for (ValorImportacao valor : this.itens.values()) {
			Matcher matcher = pattern.matcher(valor.tag());
			if (matcher.matches()) {
				toStringBuilder.append(matcher.group(2)).append("=").append(valor.valorString()).append(" ");
			}
		}
		return toStringBuilder.toString();
	}

}
