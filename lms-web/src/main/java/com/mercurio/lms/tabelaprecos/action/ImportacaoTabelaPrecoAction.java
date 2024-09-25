package com.mercurio.lms.tabelaprecos.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.service.ImportacaoTabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ResultadoImportacao;

public class ImportacaoTabelaPrecoAction {

	private ImportacaoTabelaPrecoService importacaoTabelaPrecoService;

	public void setImportacaoTabelaPrecoService(ImportacaoTabelaPrecoService importacaoTabelaPrecoService) {
		this.importacaoTabelaPrecoService = importacaoTabelaPrecoService;
	}

	public TypedFlatMap importarTabela(TypedFlatMap parametros) {
		TypedFlatMap tfm = new TypedFlatMap();
		
		byte[] arquivoEmBytes = decodificaArquivo(parametros);
		byte[] bytesConteudoArquivo = new byte[arquivoEmBytes.length - 1024];
		System.arraycopy(arquivoEmBytes, 1024, bytesConteudoArquivo, 0, bytesConteudoArquivo.length);

		InputStream inputStream = this.abreArquivo(bytesConteudoArquivo);

		ResultadoImportacao resultado = this.importacaoTabelaPrecoService.importar(inputStream, (String) parametros.get("idTabelaPreco"));

		if(resultado.sucesso()){
			tfm.put("sucesso", "Arquivo importado com sucesso.");
			
			String mensagensAtualizacao = "";
			for (String m : resultado.mensagensAtualizacao()) {
				mensagensAtualizacao = mensagensAtualizacao+m+"\n";
			}
			tfm.put("resultadoAtualizacaoAutomatica", mensagensAtualizacao);
		}else{
			String mensagens = "";
			for (String m : resultado.mensagens()) {
				mensagens = mensagens+m+"\n";
			}
			tfm.put("resultadoValidacao", mensagens);
		}

		return tfm;
	}

	private InputStream abreArquivo(byte[] arquivo) {
		try {
			return new ByteArrayInputStream(arquivo);
		} catch (Exception e) {
			throw new BusinessException("Erro ao abrir arquivo.", e);
		}
	}

	private byte[] decodificaArquivo(TypedFlatMap parametros) {
		try {
			return Base64Util.decode((String) parametros.get("dcArquivo"));
		} catch (IOException e) {
			throw new BusinessException("Erro ao decodificar arquivo.");
		}
	}

}
