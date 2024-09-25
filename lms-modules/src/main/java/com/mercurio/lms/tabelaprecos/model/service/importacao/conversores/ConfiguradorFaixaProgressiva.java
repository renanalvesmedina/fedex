package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hibernate.ObjectNotFoundException;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.UnidadeMedida;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.FaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ItemValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorFaixaProgressivaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ConfiguradorFaixaProgressiva extends ConfiguradorTabelaPrecoParcela {

	private static final String TP_SITUACAO_ATIVA = "A";
	private FornecedoresRotaTarifa fornecedoresChave;

	public ConfiguradorFaixaProgressiva(Map<String, Object> dependencias) {
		super(dependencias);
		this.fornecedoresChave = (FornecedoresRotaTarifa) dependencias.get(ConfiguradoresTabelaPrecoParcela.FORNECEDORES_ROTAVSTARIFA);
	}

	@Override
	public List<TabelaPrecoParcela> configura(List<ComponenteImportacao> componentes) {

		List<TabelaPrecoParcela> retorno = new ArrayList<TabelaPrecoParcela>();
		TabelaPrecoParcela tabelaPrecoParcela = new TabelaPrecoParcela();

		tabelaPrecoParcela.setTabelaPreco(this.tabelaPreco);
		tabelaPrecoParcela.setParcelaPreco(this.obtemParcelaPreco(componentes));

		for(ComponenteImportacao componente : componentes){
			try {
				FaixaProgressivaImportacao faixaProgressivaImportacao = (FaixaProgressivaImportacao) componente;
				tabelaPrecoParcela.incluiFaixasProgressiva(criarFaixasProgressiva(tabelaPrecoParcela, faixaProgressivaImportacao));
			} catch (ImportacaoException ie) {
				this.capturaErro(ie);
			}
		}

		retorno.add(tabelaPrecoParcela);
		return retorno;
	}

	private List<FaixaProgressiva> criarFaixasProgressiva(TabelaPrecoParcela tabelaPrecoParcela, FaixaProgressivaImportacao faixaProgressivaImportacao) {
		List<FaixaProgressiva> retorno = new ArrayList<FaixaProgressiva>();

		DomainValue cdMinimoProgressivo = (DomainValue) ReflectionUtils.toObject(faixaProgressivaImportacao.minimoProgressivo(), DomainValue.class);
		UnidadeMedida unidadeMedida = null;
		if(faixaProgressivaImportacao.unidadeMedida() != null){
			unidadeMedida = this.buscarUnidadeMedida(Long.valueOf(faixaProgressivaImportacao.unidadeMedida()));
		}

		for(ValorFaixaProgressivaImportacao vlFaixaProgImportacao : faixaProgressivaImportacao.valores()){
			try {
				FaixaProgressiva faixaProgressiva = criaFaixaProgressiva(
						tabelaPrecoParcela, cdMinimoProgressivo,
						unidadeMedida, vlFaixaProgImportacao);
				retorno.add(faixaProgressiva);

			} catch (ImportacaoException ie) {
				this.capturaErro(ie);
			}
		}

		return retorno;
	}

	private FaixaProgressiva criaFaixaProgressiva(TabelaPrecoParcela tabelaPrecoParcela, DomainValue cdMinimoProgressivo,
			UnidadeMedida unidadeMedida, ValorFaixaProgressivaImportacao vlFaixaProgImportacao) {
		FaixaProgressiva faixaProgressiva = new FaixaProgressiva();
		faixaProgressiva.setTabelaPrecoParcela(tabelaPrecoParcela);
		faixaProgressiva.setCdMinimoProgressivo(cdMinimoProgressivo);
		faixaProgressiva.setUnidadeMedida(unidadeMedida);
		DomainValue tpIndicadorCalculo = null;
		if(vlFaixaProgImportacao.valores() != null){
			ValorImportacao valorIndicador =  vlFaixaProgImportacao.valores().values().iterator().next().indicadorTipoCalculo();
			tpIndicadorCalculo = valorIndicador == null ? null : (DomainValue) ReflectionUtils.toObject(valorIndicador.valorString(), DomainValue.class);
		}
		faixaProgressiva.setTpIndicadorCalculo(tpIndicadorCalculo);

		faixaProgressiva.setVlFaixaProgressiva(vlFaixaProgImportacao.valorfaixaProgressiva());
		faixaProgressiva.setTpSituacao((DomainValue) ReflectionUtils.toObject(TP_SITUACAO_ATIVA, DomainValue.class));
		if(vlFaixaProgImportacao.idProdutoEspecifico() != null){
			configuraProdutoEspecifico(vlFaixaProgImportacao, faixaProgressiva);
		}
		faixaProgressiva.setValoresFaixasProgressivas(this.criarValoresFaixaProgressiva(faixaProgressiva, vlFaixaProgImportacao));
		return faixaProgressiva;
	}

	private void configuraProdutoEspecifico(ValorFaixaProgressivaImportacao vlFaixaProgImportacao,	FaixaProgressiva faixaProgressiva) {
		try{
			ProdutoEspecifico produtoEspecifico = this.buscarProdutoEspecifico(vlFaixaProgImportacao.idProdutoEspecifico());
			if (produtoEspecifico == null) {
				this.capturaErro(lancaExcecaoProdutoEspecificoNaoEncontrado(vlFaixaProgImportacao));
			}
			faixaProgressiva.setProdutoEspecifico(produtoEspecifico);
		}catch (ObjectNotFoundException onf) {
			this.capturaErro(lancaExcecaoProdutoEspecificoNaoEncontrado(vlFaixaProgImportacao));
		}
	}

	private ImportacaoException lancaExcecaoProdutoEspecificoNaoEncontrado(
			ValorFaixaProgressivaImportacao vlFaixaProgImportacao) {
		return new ImportacaoException(vlFaixaProgImportacao.faixa().coluna(), vlFaixaProgImportacao.faixa().linha(), String.format("Produto Específico %d não cadastrado na base de dados.", vlFaixaProgImportacao.faixa().valorLong()));
	}

	private List<ValorFaixaProgressiva> criarValoresFaixaProgressiva(FaixaProgressiva faixaProgressiva, ValorFaixaProgressivaImportacao vlFaixaProgImportacao) {
		if (vlFaixaProgImportacao == null || MapUtils.isEmpty(vlFaixaProgImportacao.valores())) {
			return Collections.emptyList();
		}
		List<ValorFaixaProgressiva> retorno = new ArrayList<ValorFaixaProgressiva>();

		Set<ChaveProgressao> chaves = vlFaixaProgImportacao.valores().keySet();
		for(ChaveProgressao chaveProgressao : chaves){
			try {
				ValorFaixaProgressiva valorFaixaProgressiva = criaValorFaixaProgressiva(faixaProgressiva, vlFaixaProgImportacao, chaveProgressao);
				if (valorFaixaProgressiva == null) {
					continue;
				}
				retorno.add(valorFaixaProgressiva);

			} catch (ImportacaoException ie) {
				this.capturaErro(ie);
			}
		}

		return retorno;
	}

	private ValorFaixaProgressiva criaValorFaixaProgressiva(FaixaProgressiva faixaProgressiva, ValorFaixaProgressivaImportacao vlFaixaProgImportacao, ChaveProgressao chaveProgressao) {
		FornecedorChave fornecedor = this.fornecedoresChave.fornecedorPara(chaveProgressao.tipo());
		if (fornecedor.chaveComProblema(chaveProgressao)) {
			return null;
		}

		ItemValorFaixaProgressivaImportacao itemValorFaixaProgressivaImportacao = vlFaixaProgImportacao.valores().get(chaveProgressao);

		ValorImportacao valorImportacao = itemValorFaixaProgressivaImportacao.valor();
		ValorImportacao valorImportacaoPsMinimo = itemValorFaixaProgressivaImportacao.pesoMinimo();
		ValorImportacao valorImportacaoFatorMultiplicacao = itemValorFaixaProgressivaImportacao.fatorMultiplicacao();
		ValorImportacao valorTaxaFixaProgressiva = itemValorFaixaProgressivaImportacao.valorTaxaFixaProgressiva();
		ValorImportacao valorKmExtra = itemValorFaixaProgressivaImportacao.valorKmExtra();

		if (valorImportacao == null){
			throw new ImportacaoException(vlFaixaProgImportacao.faixa().coluna(), chaveProgressao.linha(), String.format("Valor fixo é obrigatório para a faixa/produto específico '%s'.", vlFaixaProgImportacao.faixa().valorShort()));
		}

		ValorFaixaProgressiva valorFaixaProgressiva = new ValorFaixaProgressiva();
		valorFaixaProgressiva.setFaixaProgressiva(faixaProgressiva);
		valorFaixaProgressiva.setBlPromocional(Boolean.FALSE);

		valorFaixaProgressiva.setVlFixo(valorImportacao.valorBigDecimal());
		valorFaixaProgressiva.setVlTaxaFixa(valorTaxaFixaProgressiva != null ? valorTaxaFixaProgressiva.valorBigDecimal() : null);
		valorFaixaProgressiva.setVlKmExtra(valorKmExtra != null ? valorKmExtra.valorBigDecimal() : null);

		valorFaixaProgressiva.setPsMinimo(valorImportacaoPsMinimo != null ? valorImportacaoPsMinimo.valorBigDecimal() : null);
		valorFaixaProgressiva.setNrFatorMultiplicacao(valorImportacaoFatorMultiplicacao != null ? valorImportacaoFatorMultiplicacao.valorBigDecimal() : null);

		if (this.tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue().equalsIgnoreCase("C")) {
			valorFaixaProgressiva.setDtVigenciaPromocaoInicial(JTDateTimeUtils.getDataAtual());
			valorFaixaProgressiva.setDtVigenciaPromocaoFinal(JTDateTimeUtils.maxYmd(""));
		}
		
		TradutorChaveProgressao tradutor = fornecedor.forneceChave(chaveProgressao);
		valorFaixaProgressiva.setTarifaPreco(tradutor.tarifaPreco());
		valorFaixaProgressiva.setRotaPreco(tradutor.rotaPreco());

		return valorFaixaProgressiva;
	}

	private ParcelaPreco obtemParcelaPreco(List<ComponenteImportacao> componentes) {
		if(CollectionUtils.isEmpty(componentes)) {
			return null;
		}
		String tagArquivo = componentes.get(0).valorTagPrincipal();
		TagTabelaPreco tag = buscador.buscarTagTabelaPreco(tagArquivo);
		return tag.getParcelaPreco();
	}

	private UnidadeMedida buscarUnidadeMedida(Long idUnidadeMedida) {
		return buscador.buscarUnidadeMedida(idUnidadeMedida);
	}

	private ProdutoEspecifico buscarProdutoEspecifico(Short nrTarifa) {
		if(nrTarifa == null){
			return null;
		}
		return buscador.buscarProdutoEspecifico(nrTarifa);
	}

}
