<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tarifaPrecoRotaService">
	<adsm:form action="/tabelaPrecos/manterTarifasPrecoRotas">
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
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			size="5"
			width="85%"
			afterPopupSetValue="afterPopupTarifa"
			onDataLoadCallBack="dataLoadTarifa"/>

		<adsm:lookup
			action="/tabelaPrecos/manterRotas"
			criteriaProperty="origemString"
			dataType="text"
			idProperty="idRotaPreco"
			labelWidth="15%"
			label="origem"
			property="rotaPreco"
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
			serializable="false"
			size="80"
			width="85%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tarifaPrecoRota" id="consultar"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTarifaPrecoRota" property="tarifaPrecoRota" gridHeight="200" unique="true" rows="11" onSelectRow="checkProcessoWK();">
		<adsm:gridColumn title="tabela" property="tabelaPreco.tabelaPrecoString" width="16%"/>
		<adsm:gridColumn title="tarifa" property="tarifaPreco.cdTarifaPreco" width="8%"/>
		<adsm:gridColumn title="origem" property="rotaPreco.origemString" width="38%"/>
		<adsm:gridColumn title="destino" property="rotaPreco.destinoString" width="38%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript">
inicializar();

function inicializar() {
	var u = new URL(parent.location.href);
	var idTabelaPreco = u.parameters["idTabelaPreco"];
	var dsTabelaPreco = u.parameters["dsTabelaPreco"];
	var sgTabelaPreco = u.parameters["sgTabelaPreco"];
	if (idTabelaPreco != '' && idTabelaPreco != undefined) {
		setElementValue("tabelaPreco.tabelaPrecoString", sgTabelaPreco);
		setElementValue("tabelaPreco.idTabelaPreco", idTabelaPreco);
		setElementValue("tabelaPreco.dsDescricao", dsTabelaPreco);
		setDisabled("tabelaPreco.tabelaPrecoString", true);
		setDisabled("tabelaPreco.dsDescricao", true);
		
		//popula grid
		tarifaPrecoRotaGridDef.executeSearch({tabelaPreco:{idTabelaPreco:getElementValue("tabelaPreco.idTabelaPreco")}}, true);
	}
}

function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString", "");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});
	if( (getElementValue("tabelaPreco.tabelaPrecoString") == "") && (tabelaPrecoString != "") ) {
		setElementValue("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
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

function afterPopupTarifa(data) {
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

function initWindow(eventObj) {
	var event = eventObj.name;
	if (event = 'cleanButton_click') {
		inicializar();
	}
	checkProcessoWK();
}

function checkProcessoWK() {
	var u = new URL(parent.location.href);
	var idProcessoWorkflow = u.parameters["idProcessoWorkflow"];
	if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
		setDisabled("btnExcluir", true);
	}
}
</script>