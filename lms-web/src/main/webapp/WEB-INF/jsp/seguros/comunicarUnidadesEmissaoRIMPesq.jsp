<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>
	function loadPage() {
		setMasterLink(document, true);
		onPageLoad();
		
		//TODO: Validar se o carregamento pela tela "pai" e feito de forma coesa...
		var idNrProcessoSinistroValue = getElementValue("nrProcessoSinistro");
		if (idNrProcessoSinistroValue!="") {
			var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findLookupControleCarga", "loadDataNumeroProcesso", idNrProcessoSinistroValue);
	    	xmit({serviceDataObjects:[sdo]});
		}
	}
</script>

<adsm:window onPageLoad="loadPage" service="lms.seguros.comunicarUnidadesEmissaoRIMAction">
	<adsm:form action="/seguros/comunicarUnidadesEmissaoRIM" height="200" idProperty="idprocessoSinistro">

		<adsm:lookup label="processoSinistro" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					disabled="false"
					picker="true" 	
					popupLabel="pesquisarProcessoSinistro"				
					dataType="text"	
					onchange="return onProcessoSinistroChange(this);"
					onDataLoadCallBack="loadDataNumeroProcesso"
					onPopupSetValue="onProcessoSinistroPopupSetValue"
					action="/seguros/manterProcessosSinistro" cmd="list"
					service="lms.seguros.emitirCartaOcorrenciaAction.findLookupProcessoSinistro"
					width="85%">

					<adsm:propertyMapping modelProperty="dhSinistro" relatedProperty="dhSinistro" />
					<adsm:propertyMapping modelProperty="tipoSinistro.dsTipo" relatedProperty="tipoSinistro.dsTipo" />
					<adsm:propertyMapping modelProperty="municipio.nmMunicipio" relatedProperty="municipio.nmMunicipio" />
					<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
					<adsm:propertyMapping modelProperty="dsSinistro" relatedProperty="dsSinistro" />
					<adsm:propertyMapping relatedProperty="nrProcessoSinistro" modelProperty="nrProcessoSinistro"/>
					<adsm:hidden property="nrProcessoSinistro" serializable="false"/>
		</adsm:lookup>
		

		<adsm:textbox property="dhSinistro" dataType="JTDateTimeZone" label="dataHoraSinistro" size="20" maxLength="16" disabled="true" />

		<adsm:textbox property="tipoSinistro.dsTipo" dataType="text" label="tipoSinistro" disabled="true" />

		<adsm:textbox property="municipio.nmMunicipio" dataType="text" label="municipio" maxLength="60" size="30" disabled="true" />

		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" size="2" disabled="true" />

		<adsm:textarea property="dsSinistro" label="descricaoSinistro" maxLength="200" columns="109" width="85%" disabled="true" />

		<adsm:hidden property="nrProcessoSinistroOld"/>

		<adsm:buttonBar freeLayout="bottom">
			<adsm:resetButton />
			<adsm:button id="btnGerarCarta" caption="gerarCarta" onclick="openLetter()"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-22050"/>
		</adsm:i18nLabels>
		
	</adsm:form>

</adsm:window>

<script>
	
	function initWindow(eventObj) {
	}
	
	function openLetter() {
		var data = new Object();
		
		var idProcessoSinistro = getElementValue("processoSinistro.idProcessoSinistro");
		
		data.idProcessoSinistro = idProcessoSinistro;
		
		var sdo = createServiceDataObject("lms.seguros.comunicarUnidadesEmissaoRIMAction.findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo", 
					"openLetter", data);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	var getSelectedIds;
	function openLetter_cb(data, error){
		if(error!="" && error != null && error!="undefined"){
			alert("LMS-22050 - " + i18NLabel.getLabel("LMS-22050"));
			return false;
		}
		
		getSelectedIds = data;
	
		showModalDialog('seguros/comunicarUnidadesEmissaoRIM.do?cmd=dadosCarta',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Reseta os dados da tela
	 */
	function resetView(){
		resetValue(this.document);
	}
	
	function onProcessoSinistroPopupSetValue(data, error) {
		if (data!=undefined) {			
			if (data.idProcessoSinistro!=undefined) {
				var vet = new Array();				
				vet.idProcessoSinistro = data.idProcessoSinistro;
			    var sdo = createServiceDataObject("lms.seguros.emitirCartaOcorrenciaAction.findLookupProcessoSinistro", "loadDataNumeroProcesso", vet);
			    xmit({serviceDataObjects:[sdo]});
			}
		}
	}	
	
	function loadDataNumeroProcesso_cb(data, error) {
		processoSinistro_nrProcessoSinistro_exactMatch_cb(data);		
		
		if (data._exception!=undefined){
			alert(data._exception._message);
			resetView();
		} 
		
		habilitaBotao();				
	}

	function habilitaBotao(){
		if(getElementValue("nrProcessoSinistro") == ""){
			setDisabled("btnGerarCarta", true);
		}else{
			setDisabled("btnGerarCarta", false);
		}
	}	
	
	// onchange do campo processoSinistro
	function onProcessoSinistroChange(e) {
		if (e.value == "") {
			setDisabled("btnGerarCarta", true);
		}
		return processoSinistro_nrProcessoSinistroOnChangeHandler();
	}
	
</script>