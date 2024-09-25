<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargasSemiReboques" service="lms.carregamento.manterControleCargasJanelasAction" 
			onPageLoadCallBack="retornoCarregaPagina"  >
	<adsm:form action="/carregamento/manterControleCargas">

		<adsm:hidden property="tpMeioTransporte" serializable="false" value="R"/>
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" serializable="false" value="46"/>
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false" />

		<adsm:hidden property="idControleCarga" />
		<adsm:hidden property="idRotaIdaVolta" serializable="false" />

		<adsm:section caption="novoSemiReboque" />

		<adsm:lookup dataType="text" property="meioTransporte2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransporteFrota"
					 onPopupSetValue="popupMeioTransporte"
					 picker="false" label="semiReboque" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportePlaca"
					 onPopupSetValue="popupMeioTransporte"
					 picker="true" maxLength="25" width="78%" size="24" serializable="true" required="true" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte2.nrFrota" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrota" />
		</adsm:lookup>


		<adsm:section caption="localTroca" />
		<adsm:textbox property="dhTroca" label="dataHoraTroca" dataType="JTDateTimeZone" size="20" width="85%" required="true" />

		<adsm:combobox property="pontoParada.idPontoParada" label="pontoParada" 
					   optionProperty="idPontoParada" optionLabelProperty="nmPontoParada" 
					   service="lms.carregamento.manterControleCargasJanelasAction.findPontoParada" 
					   onchange="return pontoParada_OnChange(this)" width="85%" >
			<adsm:propertyMapping modelProperty="idRotaIdaVolta" criteriaProperty="idRotaIdaVolta" />
			<adsm:propertyMapping modelProperty="municipio.idMunicipio" relatedProperty="municipio.idMunicipio" />
			<adsm:propertyMapping modelProperty="municipio.nmMunicipio" relatedProperty="municipio.nmMunicipio" />
			<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" relatedProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:combobox>


		<adsm:combobox property="controleTrecho.idControleTrecho" label="trecho" 
					   optionProperty="idControleTrecho" optionLabelProperty="trecho" 
					   service="lms.carregamento.manterControleCargasJanelasAction.findControleTrecho" 
					   width="85%" >
			<adsm:propertyMapping modelProperty="idControleCarga" criteriaProperty="idControleCarga" />
		</adsm:combobox>


		<adsm:lookup dataType="text" label="rodovia" 
					 property="rodovia" idProperty="idRodovia"
					 criteriaProperty="sgRodovia"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupRodovia"
					 action="/municipios/manterRodovias" 
					 size="5" maxLength="10" width="45%"
					 exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="30" disabled="true"/>
		</adsm:lookup>
		

		<adsm:textbox property="nrKmRodoviaTroca" label="km" dataType="text" size="5" maxLength="5" width="25%" />


		<adsm:lookup label="municipio" dataType="text" 
					 property="municipio" idProperty="idMunicipio" 
					 criteriaProperty="nmMunicipio"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupMunicipio"
					 action="/municipios/manterMunicipios" 
					 size="30" maxLength="60" width="45%" 
					 exactMatch="false" minLengthForAutoPopUpSearch="3" required="true" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:propertyMapping modelProperty="unidadeFederativa.sgUnidadeFederativa" relatedProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:lookup>

		<adsm:textbox property="unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" size="5"  width="25%" disabled="true" />

		<adsm:textarea property="dsTroca" label="descricaoLocal" maxLength="300" rows="4" columns="70" width="85%" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="confirmar" id="botaoConfirmar" onclick="javascript:confirmar_onClick(this.form);" disabled="false" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
		<script>
			var LMS_05162 = "<adsm:label key='LMS-05162'/>";
		</script>
	</adsm:form>
</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		setElementValue("idControleCarga", dialogArguments.window.document.getElementById('controleCarga.idControleCarga').value);
		setElementValue("idRotaIdaVolta", dialogArguments.window.document.getElementById('idRotaIdaVolta').value);
		notifyElementListeners({e:document.getElementById("idRotaIdaVolta")});
		notifyElementListeners({e:document.getElementById("idControleCarga")});
		document.getElementById("meioTransporte2.nrFrota").required = "true";

		var tpControleCarga = dialogArguments.window.document.getElementById('tpControleCarga').value;
		if (tpControleCarga == "C")
			document.getElementById("controleTrecho.idControleTrecho").required = "false";
		else
			document.getElementById("controleTrecho.idControleTrecho").required = "true";

		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.getDataLocalTroca", "resultado_loadDataLocalTroca");
	   	xmit({serviceDataObjects:[sdo]});
	}
}


function resultado_loadDataLocalTroca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("dhTroca", setFormat(document.getElementById("dhTroca"), getNestedBeanPropertyValue(data,"dhAtual")));
	setFocusOnFirstFocusableField();
}

function pontoParada_OnChange(combo) {
	var r = comboboxChange({e:combo});
	if (getElementValue('municipio.nmMunicipio') != "")
		setDisabled("municipio.idMunicipio", true);
	else
		setDisabled("municipio.idMunicipio", false);
	return r;
}



function confirmar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.storeTrocarSemiReboque", 
		"retornoStoreTrocarSemiReboque", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}

function retornoStoreTrocarSemiReboque_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	showSuccessMessage();

	var blNecessitaCartaoPedagio = getNestedBeanPropertyValue(data, "blNecessitaCartaoPedagio");
	if (blNecessitaCartaoPedagio != undefined && blNecessitaCartaoPedagio == "true") {
		alert(LMS_05162);
	}
	window.close();
}


/************************************ INICIO - SEMI REBOQUE ************************************/
function retornoMeioTransporteFrota_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransporte(data);
	}
	return r;
}

function retornoMeioTransportePlaca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransporte(data);
	}
	return r;
}

function callBackMeioTransporte(data) {
	validaSemiReboque(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
}

function popupMeioTransporte(data) {
	validaSemiReboque(getNestedBeanPropertyValue(data,"idMeioTransporte"));
}


function validaSemiReboque(idMeioTransporte) {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.validateSemiReboqueToLocalTroca", 
		"retornoValidaSemiReboque", 
		{idMeioTransporte:idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

function retornoValidaSemiReboque_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("meioTransporte2.idMeioTransporte");
		resetValue("meioTransporte.idMeioTransporte");
		setFocus(document.getElementById("meioTransporte2.nrFrota"));
		return false;
	}
}

/************************************ INICIO - SEMI REBOQUE ************************************/
</script>