<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.manterTiposTabelaPrecoAction">
	<adsm:form action="/tabelaPrecos/manterTiposTabelaPreco" idProperty="idTipoTabelaPreco" onDataLoadCallBack="form">
		<adsm:combobox
			label= "tipo"
			property="tpTipoTabelaPreco"
			domain="DM_TIPO_TABELA_PRECO"
			onchange="onSelectTipo();"
			required="true"
		/>
		<adsm:range label="versao" >
			<adsm:textbox dataType="integer" maxLength="6" minValue="0"  property="nrVersao" size="6" onchange="validaNrVersaoInicialFinal();"/>
			<adsm:textbox dataType="integer" maxLength="6" minValue="0"  property="nrVersaoFinal" size="6" onchange="validaNrVersaoInicialFinal();"/>
		</adsm:range>

		<adsm:textbox
			label="versao"
			property="nrVersao"
			dataType="integer"
			maxLength="6"
			minValue="0"
			size="6"
		/>
		<adsm:hidden
			property="empresaByIdEmpresaCadastrada.tpSituacao"
			serializable="false"
			value="A"
		/>
		<adsm:lookup
			label="empresa"
			property="empresaByIdEmpresaCadastrada"
			idProperty="idEmpresa"
			criteriaProperty="pessoa.nrIdentificacao"
			service="lms.tabelaprecos.manterTiposTabelaPrecoAction.findLookupEmpresa"
			action="/municipios/manterEmpresas"
			dataType="text"
			exactMatch="true"
			labelWidth="15%"
			maxLength="20"
			required="true"
			size="20"
			width="85%"
			afterPopupSetValue="afterPopupEmpresa"
		>
			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
				size="30"
			/>
		</adsm:lookup>

		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.tabelaprecos.manterTiposTabelaPrecoAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="15%"
			maxLength="20"
			size="20"
			width="85%"
			afterPopupSetValue="afterPopupCliente"
		>
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="cliente.pessoa.nmPessoa"
				size="30"
				serializable="false"
			/>
		</adsm:lookup>
		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>
		<adsm:combobox
			autoLoad="false"
			boxWidth="250"
			label="servico"
			onlyActiveValues="true"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			property="servico.idServico"
			service="lms.configuracoes.servicoService.find"
			onDataLoadCallBack="servico"
		/>
		<adsm:textbox
			dataType="text"
			label="identificacao"
			maxLength="60"
			property="dsIdentificacao"
			required="true"
			size="45"
		/>
		<adsm:combobox
			label="situacao"
			property="tpSituacao"
			domain="DM_STATUS"
			required="true"
			width="85%"
		/>

		<adsm:hidden property="servicoValueAux"/>
		<adsm:hidden property="servicoValue"/>
		<adsm:hidden property="cameFromOutside" value="Y"/>
		<adsm:hidden property="servicoLabelAux"/>
		<adsm:hidden property="tipoValueAux"/>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" disabled="false"/>
			<adsm:button caption="novo" id="novo" buttonType="newButton" onclick="return myNewButtonScript(this.document);" disabled="false"/>
			<adsm:button caption="excluir" id="excluir" buttonType="removeButton" onclick="removeButtonScript('lms.tabelaprecos.tipoTabelaPrecoService.removeById','myNewButtonScript','idTipoTabelaPreco',this.document);" disabled="false"/>
			<!-- adsm:removeButton/ -->
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript">

function afterStore_cb(data,erro) {
	if(erro != undefined) {
		alert(erro);
		return false;
	}
	store_cb(data, erro);
	setElementValue("cameFromOutside", "Y");
}

function cleanCadTrash() {
	setElementValue("servicoValueAux","");
	setElementValue("servicoValue","");
	setElementValue("servicoLabelAux","");
	setElementValue("tipoValueAux","");
	var e = document.getElementById("servico.idServico");
	for (var i = e.options.length; i >= 1 ; i--) {
		e.options.remove(i);
	}
	setDisabled("servico.idServico", false);
	setDisabled("nrVersao", false);
	setDisabled("nrVersaoFinal", false);
}

function myNewButtonScript(documento) {
	setDisabled("nrVersao", false);
	setDisabled("nrVersaoFinal", false);
	setDisabled("servico.idServico", false);
	newButtonScript(documento);
	cleanCadTrash();
}

function myNewButtonScript_cb(data,errorMsg) {
	if(errorMsg!=null) {
		alert(errorMsg);
		return;
	}
	myNewButtonScript(this.document);
}

function servico_cb(data,errorMsg) {
	var servicoValueAux = getElementValue("servicoValueAux");
	var servicoValue = getElementValue("servicoValue");
	comboboxLoadOptions({data:data, e:document.getElementById("servico.idServico")});

	var tipoValueAux = getElementValue("tipoValueAux");
	var tpTipoTabelaPreco = getElementValue("tpTipoTabelaPreco");
	if(tpTipoTabelaPreco == tipoValueAux) {
		var contains = false;
		var e = document.getElementById("servico.idServico");
		for (var i = 0; i < e.options.length; i++) {
			var o = e.options[i];
			if (o.value == servicoValueAux) {
				contains = true;
				break;
			}
		}
		if(contains == false) {
			var o = new Option(getElementValue("servicoLabelAux"), getElementValue("servicoValueAux"));
			o._inactive = true;
			e.options.add(o);
		}
	}

	if(getElementValue("cameFromOutside") == "Y") {
		setElementValue("servico.idServico",servicoValue);
		setElementValue("cameFromOutside","N");
	}

}

/**
* Fun��o de callback do formul�rio. Ajusta as vari�veis escondidas (hidden)
* para os valores que vem junto com o pojo de tipo de tabela preco. Esse ajuste
* � necess�rio para o funcionamento correto da tela, que n�o usa as combo din�micas
* com as tags propertyMapping.
*/
function form_cb(data,errorMsg,errorKey, showErrorAlert) {
	onDataLoad_cb(data,errorMsg,errorKey,showErrorAlert);
	//store_cb(data,errorMsg,errorKey,showErrorAlert);

	var servico = getElementValue("servico.idServico");
	setElementValue("servicoValue",servico);
	if(getElementValue("servicoValueAux") == "") {
		setElementValue("servicoValueAux",servico);
	}

	if(servico != "") {
		var e = document.getElementById("servico.idServico");
		var servicoLabel = e.options[e.selectedIndex].text;
		if(getElementValue("servicoLabelAux") == "") {
			setElementValue("servicoLabelAux",servicoLabel);
		}
	}

	var tipo = getElementValue("tpTipoTabelaPreco");
	if(getElementValue("tipoValueAux") == "") {
		setElementValue("tipoValueAux",tipo);
	}

	setElementValue("empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "empresaByIdEmpresaCadastrada.pessoa.nrIdentificacaoFormatado"));
	setElementValue("cliente.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado"));
}

function onSelectTipo() {

	var tipo = getElementValue("tpTipoTabelaPreco");
	if (tipo == 'C') {
		document.getElementById("servico.idServico").required = "false";
		setElementValue("nrVersao", "");
		setElementValue("nrVersaoFinal", "");
		setDisabled("nrVersao", true);
		setDisabled("nrVersaoFinal", true);
		setElementValue("servico.idServico", "");
		setDisabled("servico.idServico", true);
	} else {
		document.getElementById("servico.idServico").required = "true";
		setDisabled("nrVersao", false);
		setDisabled("nrVersaoFinal", false);
		setDisabled("servico.idServico", false);

		var params = [];
		if (tipo == "A") {
			params.tpModal = "A";
		} else if (tipo == "I") {
			params.tpModal = "R";
			params.tpAbrangencia = "I";
		} else if ( (tipo == "R") || (tipo == "M") || (tipo == "@") || (tipo == "E") || (tipo == "B") || (tipo == "F")) {
			params.tpModal = "R";
		}
		if (tipo != "I") {
			params.tpAbrangencia = "N";
		}

		params.tpSituacao = "A";
		var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico", params);
		xmit({serviceDataObjects:[sdo]});

		onSelectTipoCarregaNrVersaoFinal(tipo);
	}
}

function onSelectTipoCarregaNrVersaoFinal(tipo) {
	var paramsTipo = [];
	paramsTipo.tpTipoTabelaPreco = tipo;
	if (tipo != 'C') {
		var sdo = createServiceDataObject("lms.tabelaprecos.manterTiposTabelaPrecoAction.findByTpTipoTabelaPrecoUltimoNrVersao", "carregaNrVersaoFinalUtilizada", paramsTipo);
		xmit({serviceDataObjects:[sdo]});
	}
}

function carregaNrVersaoFinalUtilizada_cb(data, error){
	setElementValue("nrVersao", data.nrVersao);
}

function afterPopupEmpresa(data) {
	setElementValue("empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado"));
}

function afterPopupCliente(data) {
	setElementValue("cliente.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado"));
}

function validaNrVersaoInicialFinal() {
	var nrVersaoInicial = getElementValue("nrVersao");
	var nrVersaoFinal = getElementValue("nrVersaoFinal");

	if (nrVersaoInicial == "" && nrVersaoFinal != "") {
		alert('Informe a Vers\u00E3o Inicial');
		setElementValue("nrVersaoFinal", "");
		setFocus(document.getElementById("nrVersao"));
	} else if ((nrVersaoFinal - nrVersaoInicial) > 500) {
		alert('Ultrapassou o limite de 500 Registros');
		setElementValue("nrVersaoFinal", "");
		setFocus(document.getElementById("nrVersao"));
	} else if (nrVersaoFinal == nrVersaoInicial) {
		alert('Intervalo inv\u00E1lido');
		setElementValue("nrVersaoFinal", "");
		setFocus(document.getElementById("nrVersao"));
	} else if (nrVersaoFinal > nrVersaoInicial){
		if(confirm("Ser\u00E1 gerado " + ((nrVersaoFinal - nrVersaoInicial) + 1) + " Tipos de Tabela pre\u00E7o! Deseja prosseguir?")){
			return true;
		} else {
			setElementValue("nrVersaoFinal", "");
			setFocus(document.getElementById("nrVersao"));
			setFocus(document.getElementById("nrVersaoFinal"));
		}
	}
}
</script>
