<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.copiarParametrizacoesAction"
	title="branco">

	<adsm:i18nLabels>
		<adsm:include key="requiredField"/>
		<adsm:include key="LMS-01120"/>
		<adsm:include key="cliente"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/copiarParametrizacoes">

		<adsm:hidden property="_idTabelaDivisaoCliente" serializable="false" />
		<adsm:hidden property="_idDivisaoCliente" serializable="false" />

		<adsm:section caption="origem" width="100%"/>
		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:lookup
			label="cliente"
			property="clienteOrigem"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.copiarParametrizacoesAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true" 
			size="20"
			maxLength="20"
			width="45%"
			required="true"
			labelWidth="10%"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa" 
				relatedProperty="clienteOrigem.pessoa.nmPessoa" />

			<adsm:textbox
				dataType="text"
				disabled="true"
				serializable="false"
				property="clienteOrigem.pessoa.nmPessoa"
				size="30"/>

		</adsm:lookup>

		<%-- DIVISAO COMBO --%>
		<adsm:combobox optionLabelProperty="dsDivisaoCliente" width="50%" 
			optionProperty="idDivisaoCliente" labelWidth="10%" boxWidth="140"
			property="divisaoCliente.idDivisaoCliente" 
			service="lms.vendas.copiarParametrizacoesAction.findDivisaoCombo" 
			label="divisao" onDataLoadCallBack="divisaoClienteDataLoad">
			
			<adsm:propertyMapping modelProperty="cliente.idCliente" 
				criteriaProperty="clienteOrigem.idCliente" />
				
		</adsm:combobox>

		<%-- TABELA COMBO --%>
		<adsm:combobox label="tabela" labelWidth="10%" boxWidth="325"
			optionLabelProperty="tabelaPreco.tabelaPrecoStringDescricao" 
			optionProperty="idTabelaDivisaoCliente" width="70%" 
			property="tabelaDivisaoCliente.idTabelaDivisaoCliente" 
			service="lms.vendas.copiarParametrizacoesAction.findTabelaDivisaoClienteCombo" 
			onDataLoadCallBack="tabelaDivisaoClienteDataLoad">

			<adsm:propertyMapping modelProperty="divisaoCliente.idDivisaoCliente" 
				criteriaProperty="divisaoCliente.idDivisaoCliente" />

			<adsm:propertyMapping modelProperty="idTabelaDivisaoCliente"
				relatedProperty="_idTabelaDivisaoCliente"/>

		</adsm:combobox>

		<adsm:section caption="destino" width="100%"/>
		<%-- CLIENTE LOOKUP --%>
		<adsm:lookup
			label="cliente"
			property="clienteDestino"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.copiarParametrizacoesAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			size="20"
			maxLength="20"
			width="45%"
			required="true"
			labelWidth="10%"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa" 
				relatedProperty="clienteDestino.pessoa.nmPessoa" />

			<adsm:textbox dataType="text" disabled="true" serializable="false"
				property="clienteDestino.pessoa.nmPessoa" size="30" />

		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button caption="copiarParametrizacoes" id="copy" onclick="copiarParametrizacoesClick();" disabled="false" />
			<adsm:resetButton disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">

function initWindow(eventObj) {
	setDisabled("copy", false);
}

function copiarParametrizacoesClick(){
	if(!validateTabScript(document.forms)) {
		return false;
	}
	copiarParametrizacoes();
}

function copiarParametrizacoes() {
	var data = new Array();
	var idClienteOrigem = getElementValue("clienteOrigem.idCliente");
	var idClienteDestino = getElementValue("clienteDestino.idCliente");
	var idDivisaoCliente = getElementValue("divisaoCliente.idDivisaoCliente");
	var idTabelaDivisaoCliente = getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente");

	setNestedBeanPropertyValue(data, "clienteOrigem.idCliente",idClienteOrigem);
	setNestedBeanPropertyValue(data, "clienteDestino.idCliente",idClienteDestino);
	setNestedBeanPropertyValue(data, "divisaoCliente.idDivisaoCliente",idDivisaoCliente);
	setNestedBeanPropertyValue(data, "tabelaDivisaoCliente.idTabelaDivisaoCliente",idTabelaDivisaoCliente);

	var sdo = createServiceDataObject("lms.vendas.copiarParametrizacoesAction.storeCopiaParametrizacao", "copiarParametrizacoes", data);
	xmit({serviceDataObjects:[sdo]});
}

function copiarParametrizacoes_cb(data,erro) {
	if(erro != undefined) {
		alert(erro);
		return;
	}
	alertI18nMessage("LMS-01120");
	initTela();
}

function initTela(){
	cleanButtonScript(document);	
}

/**************************/
/* CALLBACK DIVISAO COMBO */
/**************************/
function divisaoClienteDataLoad_cb(dados, erro) {
	divisaoCliente_idDivisaoCliente_cb(dados);
	var idDivisaoCliente = getElementValue("_idDivisaoCliente");
	if (idDivisaoCliente != null && idDivisaoCliente != "") {
		setElementValue("divisaoCliente.idDivisaoCliente", idDivisaoCliente);
		setElementValue("_idDivisaoCliente", "");
		var idTabelaDivisaoCliente = getElementValue("_idTabelaDivisaoCliente");
		if(idTabelaDivisaoCliente != null && idTabelaDivisaoCliente != "") {
			notifyElementListeners({e:document.getElementById("divisaoCliente.idDivisaoCliente")});
		}
	}
}

/*********************************/
/* CALLBACK TABELA DIVISAO COMBO */
/*********************************/
function tabelaDivisaoClienteDataLoad_cb(dados, erros) {
	tabelaDivisaoCliente_idTabelaDivisaoCliente_cb(dados);
	var idTabela = getElementValue("_idTabelaDivisaoCliente");
	if(idTabela != null && idTabela != "") {
		setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", idTabela);
		setElementValue("_idTabelaDivisaoCliente", "");
	}
}

</script>