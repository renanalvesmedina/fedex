package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.PernaRotaImportacao.TipoPernaRotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public class RotaImportacao implements ChaveProgressao {

	private PernaRotaImportacao pernaOrigem;
	private PernaRotaImportacao pernaDestino;
	
	private static final String PADRAO_ORIGEM = "(\\[#)([A-Z]+)(ORIGEM)(\\])";
	private static final String PADRAO_DESTINO = "(\\[#)([A-Z]+)(DESTINO)(\\])";
	
	public static final Long ZONA_PADRAO = 1L; //América do Sul
	public static final String PAIS_PADRAO = "BRA"; //Brasil
	
	@Override
	public int linha(){
		if (this.pernaOrigem != null) {
			return this.pernaOrigem.linha();
		}
		if (this.pernaDestino != null) {
			return this.pernaDestino.linha();
		}
		return Integer.MIN_VALUE;
	}

	@Override
	public int coluna(){
		int colunaOrigem = (this.pernaOrigem == null ? Integer.MAX_VALUE : this.pernaOrigem.coluna());
		int colunaDestino = (this.pernaDestino == null ? Integer.MAX_VALUE : this.pernaDestino.coluna());
		return Math.min(colunaOrigem, colunaDestino);
	}

	public void incluiValor(ValorImportacao valor) {
		if (!ValorImportacao.estahInformado(valor)) {
			return;
		}
		
		if (Pattern.matches(PADRAO_ORIGEM, valor.tag())) {
			this.incluiValorEmPernaOrigem(valor);
			return;
		}
		
		if (Pattern.matches(PADRAO_DESTINO, valor.tag())) {
			this.incluiValorEmPernaDestino(valor);
			return;
		}
		
		throw new ImportacaoException(valor.coluna(), valor.linha(), String.format("O valor '%s' para a tag %s não é um valor válido para rotas.", valor.valorString(), valor.tag()));
		
	}

	private void incluiValorEmPernaOrigem(ValorImportacao valor) {
		if (this.pernaOrigem == null) {
			this.pernaOrigem = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		}
		this.pernaOrigem.incluiValor(valor);
	}
	
	private void incluiValorEmPernaDestino(ValorImportacao valor) {
		if (this.pernaDestino == null) {
			this.pernaDestino = new PernaRotaImportacao(TipoPernaRotaImportacao.DESTINO);
		}
		this.pernaDestino.incluiValor(valor);
	}

	@Override
	public boolean estahCompleta() {
		boolean origemCompleta = this.pernaOrigem != null && this.pernaOrigem.estahCompleta();
		boolean destinoCompleto = this.pernaDestino != null && this.pernaDestino.estahCompleta();
		return origemCompleta && destinoCompleto;
	}

	@Override
	public boolean valida() {
		this.validaPerna(this.pernaOrigem, "origem");
		this.validaPerna(this.pernaDestino, "destino");
		return true;
	}

	private void validaPerna(PernaRotaImportacao perna, String tipo) {
		if (perna == null) {
			throw new ImportacaoException(this.coluna(), this.linha(), String.format("Configuração de rota inválida. Nenhum valor de %s foi informado.", tipo));
		}
		perna.valida();
	}


	public Long zonaOrigem() {
		if (this.pernaOrigem == null) {
			return ZONA_PADRAO;
		}
		Long idZonaInformada = ValorImportacao.valorLong(this.pernaOrigem.zonaRota());
		if (idZonaInformada == null) {
			return ZONA_PADRAO;
		}
		return idZonaInformada;
	}
	
	public ValorImportacao valorZonaOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.zonaRota();
	}

	public Long zonaDestino() {
		if (this.pernaDestino == null) {
			return ZONA_PADRAO;
		}
		Long idZonaInformada = ValorImportacao.valorLong(this.pernaDestino.zonaRota());
		if (idZonaInformada == null) {
			return ZONA_PADRAO;
		}
		return idZonaInformada;
	}
	
	public ValorImportacao valorZonaDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.zonaRota();
	}

	public Long tipoLocalizacaoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorLong(this.pernaOrigem.tipoLocalizacaoRota());
	}
	
	public ValorImportacao valorTipoLocalizacaoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.tipoLocalizacaoRota();
	}

	public Long tipoLocalizacaoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorLong(this.pernaDestino.tipoLocalizacaoRota());
	}
	
	public ValorImportacao valorTipoLocalizacaoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.tipoLocalizacaoRota();
	}

	public Long tipoLocalComercialOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorLong(this.pernaOrigem.tipoLocalComercialRota());
	}
	
	public ValorImportacao valorTipoLocalComercialOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.tipoLocalComercialRota();
	}

	public Long tipoLocalComercialDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorLong(this.pernaDestino.tipoLocalComercialRota());
	}
	
	public ValorImportacao valorTipoLocalComercialDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.tipoLocalComercialRota();
	}

	public String aeroportoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaOrigem.aeroportoRota());
	}
	
	public ValorImportacao valorAeroportoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.aeroportoRota();
	}

	public String aeroportoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaDestino.aeroportoRota());
	}
	
	public ValorImportacao valorAeroportoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.aeroportoRota();
	}

	public String ufOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaOrigem.ufRota());
	}
	
	public ValorImportacao valorUfOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.ufRota();
	}

	public String ufDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaDestino.ufRota());
	}
	
	public ValorImportacao valorUfDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.ufRota();
	}

	public String grupoRegiaoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaOrigem.grupoRegiaoRota());
	}
	
	public ValorImportacao valorGrupoRegiaoOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.grupoRegiaoRota();
	}

	public String grupoRegiaoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaDestino.grupoRegiaoRota());
	}
	
	public ValorImportacao valorGrupoRegiaoDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.grupoRegiaoRota();
	}

	public String filialOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaOrigem.filialRota());
	}
	
	public ValorImportacao valorFilialOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.filialRota();
	}

	public String filialDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaDestino.filialRota());
	}
	
	public ValorImportacao valorFilialDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.filialRota();
	}

	public String municipioOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaOrigem.municipioRota());
	}
	
	public ValorImportacao valorMunicipioOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.municipioRota();
	}

	public String municipioDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return ValorImportacao.valorString(this.pernaDestino.municipioRota());
	}
	
	public ValorImportacao valorMunicipioDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.municipioRota();
	}

	public String paisOrigem() {
		if (this.pernaOrigem == null) {
			return PAIS_PADRAO;
		}
		String paisInformado = ValorImportacao.valorString(this.pernaOrigem.paisRota());
		if (StringUtils.isBlank(paisInformado)) {
			return PAIS_PADRAO;
		}
		return paisInformado;
	}
	
	public ValorImportacao valorPaisOrigem() {
		if (this.pernaOrigem == null) {
			return null;
		}
		return this.pernaOrigem.paisRota();
	}

	public String paisDestino() {
		if (this.pernaDestino == null) {
			return PAIS_PADRAO;
		}
		String paisInformado = ValorImportacao.valorString(this.pernaDestino.paisRota());
		if (StringUtils.isBlank(paisInformado)) {
			return PAIS_PADRAO;
		}
		return paisInformado;
	}
	
	public ValorImportacao valorPaisDestino() {
		if (this.pernaDestino == null) {
			return null;
		}
		return this.pernaDestino.paisRota();
	}


	@Override
	public TipoChaveProgressao tipo() {
		return TipoChaveProgressao.ROTA;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!RotaImportacao.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		RotaImportacao outro = (RotaImportacao) o;
		return new EqualsBuilder().append(this.pernaOrigem, outro.pernaOrigem)
								  .append(this.pernaDestino, outro.pernaDestino).isEquals();
	}

		@Override
	public int hashCode() {
		return new HashCodeBuilder()
					.append(this.pernaOrigem)
					.append(this.pernaDestino)
					.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder toStringBuilder = new StringBuilder(this.getClass().getSimpleName()).append(": ");
		if (this.pernaOrigem != null) {
			toStringBuilder.append(this.pernaOrigem.toString()).append(" // ");
		}
		if (this.pernaDestino != null) {
			toStringBuilder.append(this.pernaDestino.toString());
		}
		return toStringBuilder.toString();
	}

	@Override
	public int compareTo(ChaveProgressao o) {
		if (!RotaImportacao.class.isAssignableFrom(o.getClass())) {
			return -1;
		}
		RotaImportacao rota = (RotaImportacao) o;
		return this.hashCode() - rota.hashCode();
	}

}