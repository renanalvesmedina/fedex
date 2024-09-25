<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarDivisaoClienteLogAction">
	<adsm:form action="/vendas/consultarDivisaoClienteLog" >
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="cliente"
			labelWidth="10%"
			maxLength="20"
			property="cliente"
			service="lms.vendas.consultarDivisaoClienteLogAction.findCliente"
			onDataLoadCallBack="clienteRemetenteCallBack" 
			onPopupSetValue="populaCombos"
			onchange="return clienteRemetenteChange();"
			size="20"
			width="50%"
			required="true">
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
			property="divisaoCliente.idDivisaoCliente"
			label="divisao"
			autoLoad="false"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			service=""
			labelWidth="10%"
			width="30%"
			required="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idDivisaoClienteLog"
			selectionMode="none"
			scrollBars="horizontal"
			onRowClick="myOnRowClick"
			width="1000"
			rows="11"
			>

		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone" width="150"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		
		<adsm:gridColumn property="dsDivisaoCliente" title="descricao" width="200"/>
		<adsm:gridColumn property="cdDivisaoCliente" title="codigo"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true"/>
		<adsm:gridColumn property="dsNaturezaProduto" title="naturezaProdutoPadrao" width="150"/>
		<adsm:gridColumn property="nrQtdeDocsRomaneio" title="quantidadeDocumentosRomaneio"/>
		
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script language="javascript" type="text/javascript">

function myOnRowClick() {
	return false;
}

//POPULA COMBOS
function populaCombos(data) {
	var sdo = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.findDivisao", "divisao_ok", {idCliente: data.idCliente});
	xmit({serviceDataObjects:[sdo]});
}
function divisao_ok_cb(data, error) {
	if (error != undefined) {
		alert(error);
	} else {
		divisaoCliente_idDivisaoCliente_cb(data) 
	}
}

function clienteRemetenteChange() {
	var r = cliente_pessoa_nrIdentificacaoOnChangeHandler();
	if (getElementValue("cliente.pessoa.nrIdentificacao") == "") { 
		document.getElementById("divisaoCliente.idDivisaoCliente").options.length = 1;
	}
	return r;
}

function clienteRemetenteCallBack_cb(data, error) {
	if (error != undefined){
		alert(error);
	} else {
		if (cliente_pessoa_nrIdentificacao_exactMatch_cb(data)) {
			populaCombos(data[0]);
		}
	}
}

</script>