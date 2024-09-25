package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

public interface ProcessadorTabelaPreco {

	void processaLinha(HSSFRow linha);

	void abreSessao(int linha, int coluna);

	void fechaSessao(int linha, int coluna);

	List<String> erros();

}
