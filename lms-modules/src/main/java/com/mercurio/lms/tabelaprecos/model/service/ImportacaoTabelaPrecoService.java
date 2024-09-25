package com.mercurio.lms.tabelaprecos.model.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ComponentesTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.InterpretadorImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ResultadoImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.BuscadorDependencia;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.ConversorTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.FornecedoresRotaTarifa;

public class ImportacaoTabelaPrecoService {

	private TabelaPrecoService tabelaPrecoService;
	private TagTabelaPrecoService tagTabelaPrecoService;
	private UnidadeMedidaService unidadeMedidaService;
	private ProdutoEspecificoService produtoEspecificoService;
	private FornecedoresRotaTarifa fornecedoresChave;
	private List<String> errosProcesso = null;

	public ResultadoImportacao importar(InputStream arquivo, String idTabelaPreco) {
		try {
			return processaImportacao(arquivo, idTabelaPreco);
		} catch (ImportacaoExceptionBloqueante e) {
			ResultadoImportacao resultado = new ResultadoImportacao(false);
			resultado.incluiMensagem(e.getMessage());
			return resultado;
		}
	}

	private ResultadoImportacao processaImportacao(InputStream arquivo,	String idTabelaPreco) {
		
		TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(Long.valueOf(idTabelaPreco));

		List<TabelaPrecoParcela> entidades = processaLeituraEConversao(arquivo, tabelaPreco);

		ResultadoImportacao resultado = null;

		if(CollectionUtils.isNotEmpty(this.errosProcesso)){
			resultado = new ResultadoImportacao(false);
			resultado.incluiMensagens(this.errosProcesso);
			this.errosProcesso = null;
			return resultado;
		}
		
		tabelaPreco.incluiParcelas(entidades);
		tabelaPrecoService.store(tabelaPreco, false, null);
		
		String msgAtualizacaoAutomatica = chamaAtualizacaoAutomatica(tabelaPreco);

		resultado = new ResultadoImportacao(true);
		if(StringUtils.isNotBlank(msgAtualizacaoAutomatica)){
			resultado.incluiMensagemAtualizacao(msgAtualizacaoAutomatica);
		}
		this.errosProcesso = null;
		return resultado;
	}

	private List<TabelaPrecoParcela> processaLeituraEConversao(InputStream arquivo, TabelaPreco tabelaPreco) {
		ComponentesTabelaPreco componentes = varreArquivo(arquivo);
		List<TabelaPrecoParcela> entidades = null;

		if (componentes.naoEstahVazio()) {
			entidades = converteComponentes(tabelaPreco, componentes);
		} else {
			incluiErros("Arquivo não importado. Verifique as tags informadas e tente novamente.");
		}

		return entidades;
	}

	private ComponentesTabelaPreco varreArquivo(InputStream arquivo) {
		HSSFWorkbook excel = abrirArquivo(arquivo);
		InterpretadorImportacaoTabelaPreco interpretador = new InterpretadorImportacaoTabelaPreco();
		ComponentesTabelaPreco componentes = interpretador.varrer(excel);
		if (CollectionUtils.isNotEmpty(interpretador.erros())) {
			incluiErros(interpretador.erros().toArray(new String[interpretador.erros().size()]));
		}
		return componentes;
	}

	private List<TabelaPrecoParcela> converteComponentes(
			TabelaPreco tabelaPreco, ComponentesTabelaPreco componentes) {
		List<TabelaPrecoParcela> entidades;
		this.fornecedoresChave.informaTabelaPreco(tabelaPreco);
		ConversorTabelaPreco conversor = new ConversorTabelaPreco(
				new BuscadorDependencia(tagTabelaPrecoService, unidadeMedidaService, produtoEspecificoService),
				this.fornecedoresChave,
				tabelaPreco);
		
		conversor.converter(componentes);
		entidades = conversor.resultado();
		
		if(conversor.ocorreramErros()){
			incluiErros(conversor.erros().toArray(new String[conversor.erros().size()]));
		}
		return entidades;
	}

	private String chamaAtualizacaoAutomatica(TabelaPreco tabelaPreco) {
		String msgAtualizacaoAutomatica = null;
		if (tabelaPrecoService.validaAtualizacaoAutomatica(tabelaPreco)){
			msgAtualizacaoAutomatica = tabelaPrecoService.atualizaTabelaCustos(tabelaPreco.getTpServico().getValue());
		}
		return msgAtualizacaoAutomatica;
	}

	private HSSFWorkbook abrirArquivo(InputStream arquivo) {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(arquivo);
			return new HSSFWorkbook(fs);
		} catch (IOException e) {
			throw new BusinessException("O arquivo não pôde ser aberto.", e);
		}
	}
	
	private void incluiErros(String... erros) {
		if (this.errosProcesso == null) {
			this.errosProcesso = new ArrayList<String>();
		}
		this.errosProcesso.addAll(Arrays.asList(erros));
	}


	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setTagTabelaPrecoService(TagTabelaPrecoService tagService) {
		this.tagTabelaPrecoService = tagService;
	}

	public void setUnidadeMedidaService(UnidadeMedidaService unidadeMedidaService) {
		this.unidadeMedidaService = unidadeMedidaService;
	}

	public void setProdutoEspecificoService(ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}
	
	public void setFornecedoresChave(FornecedoresRotaTarifa fornecedoresChave) {
		this.fornecedoresChave = fornecedoresChave;
	}



}
