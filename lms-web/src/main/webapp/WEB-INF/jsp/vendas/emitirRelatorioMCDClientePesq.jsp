<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.emitirRelatorioMCDClienteAction" >
	<adsm:form action="/vendas/emitirRelatorioMCDCliente">
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="cliente"
			labelWidth="15%"
			maxLength="20"
			property="cliente"
			service="lms.vendas.emitirRelatorioMCDClienteAction.findCliente"
			size="20"
			width="44%"
			onchange="return buscaUFs()">
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30" 
			/>
		</adsm:lookup>

		<adsm:combobox
			label="tipoEmissao" 
			property="tpEmissao" 
			domain="DM_TIPO_EMISSAO_MCD"
			defaultValue="D"
			labelWidth="11%"
			width="30%"
			required="true"/>

		<adsm:hidden property="contato.nmContato" />
		<adsm:combobox label="contato" 
			property="contato.idContato" 
			optionLabelProperty="nmContato" 
			optionProperty="idContato" 
			service="lms.vendas.emitirRelatorioMCDClienteAction.findContatos"
			labelWidth="15%"
			width="85%"
			boxWidth="313"
		>
			<adsm:propertyMapping criteriaProperty="cliente.idCliente" modelProperty="idPessoa"/>
			<adsm:propertyMapping relatedProperty="contato.nmContato" modelProperty="nmContato"/>
		</adsm:combobox>

		<adsm:hidden property="uf.siglaDescricao"/>
		<adsm:combobox
			service="lms.vendas.emitirRelatorioMCDClienteAction.findUFs"
			property="uf.idUnidadeFederativa"
			optionLabelProperty="siglaDescricao"
			optionProperty="idUnidadeFederativa"
			label="ufDestino"
			labelWidth="15%" width="44%" boxWidth="313">
			<adsm:propertyMapping relatedProperty="uf.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="servico.dsServico"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tpModal"/>
		<adsm:combobox property="servico.idServico" label="servico"
			onlyActiveValues="true" optionLabelProperty="dsServico"
			optionProperty="idServico"
			service="lms.vendas.emitirRelatorioMCDClienteAction.findServicos"
			required="true" width="30%" labelWidth="11%" boxWidth="210">
			<adsm:propertyMapping relatedProperty="servico.tpModal" modelProperty="tpModal"/>
			<adsm:propertyMapping relatedProperty="servico.tpAbrangencia" modelProperty="tpAbrangencia"/>
			<adsm:propertyMapping relatedProperty="servico.dsServico" modelProperty="dsServico"/>
		</adsm:combobox>

		<adsm:combobox width="35%" label="formatoRelatorio"
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.emitirRelatorioMCDClienteAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click"){
		resetValue("uf.idUnidadeFederativa")
		buscaUFs();
		setFormatoDefault_cb(null, null);
	}
}

function setFormatoDefault_cb(data, error) {
	if(data) {
		tpFormatoRelatorio_cb(data);
	}
	setElementValue("tpFormatoRelatorio", "pdf");
	ajustaFormatoRelatorio();
}

function ajustaFormatoRelatorio() {
	var combo = document.getElementById("tpFormatoRelatorio");
	setElementValue("tpFormatoRelatorio.descricao", combo.options[combo.selectedIndex].text);
	setElementValue("tpFormatoRelatorio.valor", combo.value);
}

function buscaUFs_cb(dados,erro) {
	if(erro) {
		alert(erro);
		return;
	}
	uf_idUnidadeFederativa_cb(dados);
}

function buscaUFs() {
	var sdo = createServiceDataObject("lms.vendas.emitirRelatorioMCDClienteAction.findUFs", "buscaUFs", {});
	xmit({serviceDataObjects:[sdo]});
	cliente_pessoa_nrIdentificacaoOnChangeHandler();
	return true;
}

function oplcb_cb(dados,erro) {
	if(erro) {
		alert(erro);
		return;
	}
	buscaUFs();
}

</script>