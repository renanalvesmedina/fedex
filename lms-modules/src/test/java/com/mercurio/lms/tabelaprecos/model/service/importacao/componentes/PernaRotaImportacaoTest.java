package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;


import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.PernaRotaImportacao.TipoPernaRotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;

public class PernaRotaImportacaoTest {

	private static final int COLUNA_UF = 1;
	private static final int COLUNA_AERO = 2;
	private static final int COLUNA_ZONA = 3;
	private static final int COLUNA_PAIS = 4;
	private static final int COLUNA_MUNICIPIO = 5;
	private static final int COLUNA_FILIAL = 6;
	private static final int COLUNA_TIPO_LOC = 7;
	private static final int COLUNA_TP_LOC_COML = 8;
	private static final int COLUNA_GRUPO_REGIAO = 9;
	private static final int OFFSET_DESTINO = COLUNA_GRUPO_REGIAO;
	
	TagImportacao tagUfOrigem = new TagImportacao(1, COLUNA_UF, TagsImportacaoTabelaPreco.TAG_UF_ORIGEM);
	TagImportacao tagAeroportoOrigem = new TagImportacao(1, COLUNA_AERO, TagsImportacaoTabelaPreco.TAG_AERO_ORIGEM);
	TagImportacao tagAeroportoDestino = new TagImportacao(1, COLUNA_AERO + OFFSET_DESTINO, TagsImportacaoTabelaPreco.TAG_AERO_DESTINO);
	TagImportacao tagZonaOrigem = new TagImportacao(1, COLUNA_ZONA, TagsImportacaoTabelaPreco.TAG_ZONA_ORIGEM);
	TagImportacao tagPaisOrigem = new TagImportacao(1, COLUNA_PAIS, TagsImportacaoTabelaPreco.TAG_PAIS_ORIGEM);
	TagImportacao tagMunicipioOrigem = new TagImportacao(1, COLUNA_MUNICIPIO, TagsImportacaoTabelaPreco.TAG_MUNICIPIO_ORIGEM);
	TagImportacao tagFilialOrigem = new TagImportacao(1, COLUNA_FILIAL, TagsImportacaoTabelaPreco.TAG_FILIAL_ORIGEM);
	TagImportacao tagTipoLocalOrigem = new TagImportacao(1, COLUNA_TIPO_LOC, TagsImportacaoTabelaPreco.TAG_TP_LOCAL_ORIGEM);
	TagImportacao tagTipoLocalComlOrigem = new TagImportacao(1, COLUNA_TP_LOC_COML, TagsImportacaoTabelaPreco.TAG_TP_LOCAL_COML_ORIGEM);
	TagImportacao tagGrupoRegiaoOrigem = new TagImportacao(1, COLUNA_GRUPO_REGIAO, TagsImportacaoTabelaPreco.TAG_GRUPO_REG_ORIGEM);

	@Test
	public void deveValidarPernaOrigemComUfEAeroporto() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_AERO, "POA", tagAeroportoOrigem));
		perna.valida();
	}
	
	@Test
	public void naoDeveValidarPernaOrigemComUfEAeroportoEFilial() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_AERO, "POA", tagAeroportoOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_FILIAL, "POA", tagFilialOrigem));
		try {
			perna.valida();
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), String.format("G4: Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", TagsImportacaoTabelaPreco.TAG_AERO_ORIGEM));
		}
	}
	
	@Test
	public void deveValidarPernaOrigemComPaisEZona() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_ZONA, "1", tagZonaOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_PAIS, "BRA", tagPaisOrigem));
		perna.valida();
	}
	
	@Test
	public void deveValidarPernaOrigemComMunicipioEFilial() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_MUNICIPIO, "Porto Alegre", tagMunicipioOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_FILIAL, "POA", tagFilialOrigem));
		perna.valida();
	}
	
	@Test
	public void naoDeveValidarPernaOrigemComMunicipioESemFilial() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_MUNICIPIO, "Porto Alegre", tagMunicipioOrigem));
		try {
			perna.valida();
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), "F4: Para informar o município, você deve informar também a filial.");
		}
	}
	
	@Test
	public void naoDeveValidarPernaOrigemComMunicipioFilialEGrupoRegiao() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_MUNICIPIO, "Porto Alegre", tagMunicipioOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_FILIAL, "POA", tagFilialOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_GRUPO_REGIAO, "3", tagGrupoRegiaoOrigem));
		try {
			perna.valida();
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), String.format(
					"J4: Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", TagsImportacaoTabelaPreco.TAG_FILIAL_ORIGEM));
		}
	}
	
	@Test
	public void deveValidarPernaOrigemComTipoLocal() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_TIPO_LOC, "3", tagTipoLocalOrigem));
		perna.valida();
	}
	
	@Test
	public void naoDeveValidarPernaOrigemComTipoLocalETipoLocalComl() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_TIPO_LOC, "3", tagTipoLocalOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_TP_LOC_COML, "6", tagTipoLocalComlOrigem));
		try {
			perna.valida();
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), String.format(
					"I4: Valores(s) incompatíveis(s): o valor para '%s' já foi informado. Você deve informar uma configuração de rota válida.", TagsImportacaoTabelaPreco.TAG_TP_LOCAL_ORIGEM));
		}
	}
	
	@Test
	public void deveValidarPernaOrigemComTipoLocalComercial() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_TP_LOC_COML, "2", tagTipoLocalComlOrigem));
		perna.valida();
	}
	
	@Test
	public void deveValidarPernaOrigemComGrupoRegiao() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_GRUPO_REGIAO, "2", tagGrupoRegiaoOrigem));
		perna.valida();
	}
	
	@Test
	public void naoDeveValidarTagDuplicada() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		perna.incluiValor(new ValorImportacao(3, COLUNA_UF, "RS", tagUfOrigem));
		perna.incluiValor(new ValorImportacao(3, COLUNA_TIPO_LOC, "3", tagTipoLocalOrigem));
		try {
			perna.incluiValor(new ValorImportacao(3, COLUNA_TIPO_LOC + OFFSET_DESTINO, "6", tagTipoLocalOrigem));
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), String.format(
					"Q4: O valor para a tag '%s' já foi informado na célula %s.", TagsImportacaoTabelaPreco.TAG_TP_LOCAL_ORIGEM, "G4"));
		}
	}
	
	@Test
	public void naoDeveValidarTagDestino() {
		PernaRotaImportacao perna = new PernaRotaImportacao(TipoPernaRotaImportacao.ORIGEM);
		try {
			perna.incluiValor(new ValorImportacao(3, COLUNA_AERO + OFFSET_DESTINO, "6", tagAeroportoDestino));
		} catch (ImportacaoException e) {
			assertEquals(e.getMessage(), String.format(
					"L4: A tag '%s' não é uma tag de origem válida.", TagsImportacaoTabelaPreco.TAG_AERO_DESTINO));
		}
	}
	
}
