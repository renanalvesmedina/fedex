package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.regex.Pattern;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.criadores.CriadoresEntidadesGenericas;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class SubConfiguradorGenerico {

	private BuscadorDependencia buscador;
	private final CriadoresEntidadesGenericas criadores;
	private static final String TIPO_PRECIFICACAO_AUTORIZADAS = "^([TSG])$";

	public SubConfiguradorGenerico(BuscadorDependencia buscador) {
		this.buscador = buscador;
		this.criadores = new CriadoresEntidadesGenericas();
	}

	public TabelaPrecoParcela configura(ComponenteImportacao componente) {
		TabelaPrecoParcela resultado = new TabelaPrecoParcela();
		TagImportacao tagPrincipal = componente.tagPrincipal();
		ParcelaPreco parcelaPreco = this.buscaParcelaPreco(tagPrincipal);
		resultado.setParcelaPreco(parcelaPreco);
		String tipoPrecificacao = leTipoPrecificacao(tagPrincipal, parcelaPreco);
		CriadorEntidadeGenerica criador = criadores.criadorPara(tipoPrecificacao);
		return criador.configuraEntidade(resultado, componente);
	}

	private String leTipoPrecificacao(TagImportacao tagPrincipal, ParcelaPreco parcelaPreco) {
		String tipoPrecificacao = parcelaPreco.getTpPrecificacao().getValue();
		if (!Pattern.matches(TIPO_PRECIFICACAO_AUTORIZADAS, tipoPrecificacao)) {
			throw new ImportacaoException(tagPrincipal.coluna(), tagPrincipal.linha(), String.format("A tag '%s' não é uma tag válida para a seção de fixos.", tagPrincipal.tag()));
		}
		return tipoPrecificacao;
	}

	private ParcelaPreco buscaParcelaPreco(TagImportacao tag) {
		TagTabelaPreco tagTabelaPreco = this.buscador.buscarTagTabelaPreco(tag.tag());
		if (TagTabelaPreco.VAZIO.equals(tagTabelaPreco)) {
			throw new ImportacaoException(tag.coluna(), tag.linha(), String.format("O valor '%s' não é uma tag válida.", tag.tag()));
		}
		ParcelaPreco parcelaPreco = tagTabelaPreco.getParcelaPreco();
		if (parcelaPreco == null) {
			throw new ImportacaoException(tag.coluna(), tag.linha(), String.format("Não foi encontrada parcela de preço para a tag '%s'", tag.tag()));
		}
		return parcelaPreco;
	}

}
