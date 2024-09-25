package com.mercurio.lms.carregamento.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.lms.carregamento.model.service.GerarDadosEstoqueDispUnitizacaoService;

/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.gerarDadosEstoqueDispUnitizacaoAction"
 */
@Assynchronous
public class GerarDadosEstoqueDispUnitizacaoAction {
	
	private GerarDadosEstoqueDispUnitizacaoService gerarDadosEstoqueDispUnitizacaoService;

	public GerarDadosEstoqueDispUnitizacaoService getGerarDadosEstoqueDispUnitizacaoService() {
		return gerarDadosEstoqueDispUnitizacaoService;
	}

	public void setGerarDadosEstoqueDispUnitizacaoService(
			GerarDadosEstoqueDispUnitizacaoService gerarDadosEstoqueDispUnitizacaoService) {
		this.gerarDadosEstoqueDispUnitizacaoService = gerarDadosEstoqueDispUnitizacaoService;
	}
	
	@AssynchronousMethod(	name="carregamento.GerarDadosEstoqueDispositivosUnitizacao",
							type = BatchType.BATCH_SERVICE,
							htmlTemplateMessage="com/mercurio/lms/carregamento/report/GerarDadosEstoqueDispUnitizacaoActionHtmlResponse.vm")
	public void generateDadosEstoqueDispositivoUnitizacao() {
		this.getGerarDadosEstoqueDispUnitizacaoService().generateDadosEstoqueDispositivoUnitizacao();
	}
	
}