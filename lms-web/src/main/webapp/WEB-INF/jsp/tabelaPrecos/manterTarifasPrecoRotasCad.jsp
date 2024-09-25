<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tarifaPrecoRotaService">
	<adsm:form action="/tabelaPrecos/manterTarifasPrecoRotas" idProperty="idTarifaPrecoRota" onDataLoadCallBack="onMyDataLoad">
		<adsm:hidden property="idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="idTipoTabelaPreco" serializable="false"/>
		<adsm:lookup
			action="/tabelaPrecos/manterTabelasPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text"
			exactMatch="true"
			idProperty="idTabelaPreco"
			labelWidth="15%"
			label="tabela"
			property="tabelaPreco"
			service="lms.tabelaprecos.tabelaPrecoService.findLookup"
			size="8"
			maxLength="7"
			width="85%"
			required="true"
			picker="false"
		>
			<adsm:propertyMapping
				relatedProperty="tabelaPreco.dsDescricao"
				modelProperty="dsDescricao"
			/>
			<adsm:propertyMapping criteriaProperty="idTipoTabelaPreco" modelProperty="tipoTabelaPreco.idTipoTabelaPreco"/>
			<adsm:propertyMapping criteriaProperty="idSubtipoTabelaPreco" modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaPreco.dsDescricao"
				size="60"
			/>
		</adsm:lookup>
		<adsm:hidden
			property="tarifaPreco.tpSituacao"
			serializable="false"
			value="A"
		/>

		<adsm:lookup
			action="/tabelaPrecos/manterTarifasPreco"
			criteriaProperty="cdTarifaPreco"
			dataType="text"
			exactMatch="true"
			idProperty="idTarifaPreco"
			labelWidth="15%"
			label="tarifa"
			maxLength="5"
			property="tarifaPreco"
			required="true"
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			size="5"
			width="85%"
			afterPopupSetValue="afterPopupTarifa"
			onDataLoadCallBack="dataLoadTarifa"
		>
			<adsm:propertyMapping criteriaProperty="tarifaPreco.tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:lookup
			action="/tabelaPrecos/manterRotas"
			criteriaProperty="origemString"
			dataType="text"
			idProperty="idRotaPreco"
			labelWidth="15%"
			label="origem"
			property="rotaPreco"
			required="true"
			disabled="true"
			service="lms.tabelaprecos.rotaPrecoService.findLookup"
			size="80"
			width="85%"
			onPopupSetValue="onPopupSetValueRotaPrecoOrigem"
		>
			<adsm:propertyMapping
				relatedProperty="rotaPreco.destinoString"
				modelProperty="destinoString"
			/>
		</adsm:lookup>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="destino"
			property="rotaPreco.destinoString"
			required="true"
			serializable="false"
			size="80"
			width="85%"
		/>
		<adsm:buttonBar>
			<adsm:storeButton id="btnSalvar" disabled="true"/>
			<adsm:newButton id="btnLimpar"/>
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	var event = eventObj.name;
	var u = new URL(parent.location.href);
	checkPendenciaWorkflow();
	if (event = 'tab_click') {
		var idTabelaPreco = u.parameters["idTabelaPreco"];
		var dsTabelaPreco = u.parameters["dsTabelaPreco"];
		var sgTabelaPreco = u.parameters["sgTabelaPreco"];
		if (idTabelaPreco != '' && idTabelaPreco != undefined) {
			setElementValue("tabelaPreco.tabelaPrecoString", sgTabelaPreco);
			setElementValue("tabelaPreco.idTabelaPreco", idTabelaPreco);
			setElementValue("tabelaPreco.dsDescricao", dsTabelaPreco);
			setDisabled("tabelaPreco.tabelaPrecoString", true);
			setDisabled("tabelaPreco.dsDescricao", true);
		}
	}
}

function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString","");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});
	//if(tabelaPrecoString != "")
	if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
	}
}

setDisabled("rotaPreco.idRotaPreco", false);
setDisabled("rotaPreco.origemString", true);
setDisabled("rotaPreco.destinoString", true);


/*
* Callback para lookup tarifaPreco.
*/
function dataLoadTarifa_cb(data) {
	var retorno = lookupExactMatch({e:document.getElementById("tarifaPreco.idTarifaPreco"), data:data });
	if(retorno){
		setFocus(document.getElementById("rotaPreco_lupa"), false);
	}
	return retorno;
}

function afterPopupTarifa(data){
	setFocus(document.getElementById("rotaPreco_lupa"), false);
}

function onPopupSetValueRotaPrecoOrigem(data, error) {
	if (data != undefined) {
		if (data.tipoLocalizacaoMunicipioComercialOrigem != undefined) {
			data.origemString += "-" + data.tipoLocalizacaoMunicipioComercialOrigem.dsTipoLocalizacaoMunicipio;
		}
		if (data.tipoLocalizacaoMunicipioComercialDestino != undefined) {
			data.destinoString += "-" + data.tipoLocalizacaoMunicipioComercialDestino.dsTipoLocalizacaoMunicipio;
		}
	}
	return true;
}

function onMyDataLoad_cb(data, error) {
	onDataLoad_cb(data, error);
	checkPendenciaWorkflow();
}
function checkPendenciaWorkflow() {
	var u = new URL(parent.location.href);
	var idPendencia = u.parameters["idPendencia"];
	var idProcessoWorkflow = u.parameters["idProcessoWorkflow"];
	var isEfetivada = u.parameters["isEfetivada"];
	if (isEfetivada == "true") {
		setDisabled("btnSalvar", true);
	} else {
		if (idPendencia != "") {
			setDisabled("btnSalvar", true);
			setDisabled("btnLimpar", true);
			if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
				setDisabled("btnExcluir", true);	
			}
		} else {
			setDisabled("btnSalvar", false);
		}
	}
	if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
		setDisabled("btnSalvar", true);
		setDisabled("btnLimpar", true);
		setDisabled("btnExcluir", true);	
		setDisabled("btnSalvar", true);
	}
}

</script>