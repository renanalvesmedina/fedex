<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.executarRetornarColetasEntregasAction" onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/coleta/executarRetornarColetasEntregas">

		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false" />
		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />
		
		<adsm:lookup dataType="text" property="meioTransporte2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.executarRetornarColetasEntregasAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="return meioTransporte2_OnChange()"
					 onDataLoadCallBack="retornoMeioTransporte2"
					 picker="false" label="meioTransporte" width="85%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />

			<adsm:propertyMapping modelProperty="controleCarga.filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" />
			<adsm:propertyMapping modelProperty="controleCarga.filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" />
			<adsm:propertyMapping modelProperty="controleCarga.idControleCarga" relatedProperty="controleCarga.idControleCarga" />
			<adsm:propertyMapping modelProperty="controleCarga.nrControleCarga" relatedProperty="controleCarga.nrControleCarga" />

			<adsm:lookup dataType="text" property="meioTransporte" 
						 idProperty="idMeioTransporte"
						 criteriaProperty="nrIdentificador"
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 service="lms.coleta.executarRetornarColetasEntregasAction.findLookupMeioTransporte" 
						 onchange="return meioTransporte_OnChange()"
						 onDataLoadCallBack="retornoMeioTransporte"
						 onPopupSetValue="retornoPopupMeioTransporte"
						 picker="true" maxLength="25" size="16" >
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte2.nrFrota" />
				<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte"	/>	
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrota" />
	
				<adsm:propertyMapping modelProperty="controleCarga.filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping modelProperty="controleCarga.filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" />
				<adsm:propertyMapping modelProperty="controleCarga.idControleCarga" relatedProperty="controleCarga.idControleCarga" />
				<adsm:propertyMapping modelProperty="controleCarga.nrControleCarga" relatedProperty="controleCarga.nrControleCarga" />
			</adsm:lookup>

		</adsm:lookup>

		<adsm:hidden property="controleCarga.tpControleCarga" value="C" serializable="false" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />
		<adsm:lookup dataType="text" label="controleCargas"
					 property="controleCarga.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.executarRetornarColetasEntregasAction.findLookupFilialByControleCarga" 
					 action="/municipios/manterFiliais" 
					 onDataLoadCallBack="retornoControleCargaFilial"
					 popupLabel="pesquisarFilial"
					 size="3" maxLength="3" labelWidth="15%" width="35%" picker="false" serializable="false" disabled="true">
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="flagBuscaEmpresaUsuarioLogado"/> 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />

	 		<adsm:lookup dataType="integer" property="controleCarga" 
	 					 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.coleta.executarRetornarColetasEntregasAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 onPopupSetValue="popupControleCarga"
						 popupLabel="pesquisarControleCarga"
						 size="9" maxLength="8" mask="00000000" serializable="true" disabled="false">
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="true"  />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
 				 <adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="controleCarga.tpControleCarga" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte" />
				 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrFrota" relatedProperty="meioTransporte2.nrFrota" />
				 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />
				 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />
			</adsm:lookup>
		</adsm:lookup>


		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="35%" serializable="true" renderOptions="true"
					   service="lms.coleta.executarRetornarColetasEntregasAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="tpManifesto_OnChange(); 
					   		return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.coleta.executarRetornarColetasEntregasAction'
						   });
						   " >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" serializable="false"
						 popupLabel="pesquisarFilial" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 disabled="true"
						 />

			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 onDataLoadCallBack="retornoNrManifesto"
						 afterPopupSetValue="manifestoNrManifestoOrigem_retornoPopup"
						 onchange="return manifestoNrManifestoOrigem_OnChange();"
						 size="10" maxLength="8" mask="00000000" disabled="false" serializable="true" />
		</adsm:combobox>
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />


		<adsm:buttonBar freeLayout="true"> 
			<adsm:button caption="consultar" id="botaoConsultar" disabled="false" onclick="consultar_OnClick();"/>
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpar_OnClick()" disabled="false" />
		</adsm:buttonBar>

		<script>
				var msgCriteriosNaoInformados = '<adsm:label key="LMS-00055"/>';
		</script>
	</adsm:form>
	
	<adsm:tabGroup  selectedTab="0" >
		<adsm:tab title="coletasPendentes" id="coletasPendentes" src="/coleta/executarRetornarColetasEntregas" cmd="coletasPendentes" boxWidth="180" height="320" />
		<adsm:tab title="entregasRealizar" id="entregasRealizar" src="/coleta/executarRetornarColetasEntregas" cmd="entregasPendentes" boxWidth="180" height="320" />
		<adsm:tab title="entregasParciaisVolumes" id="entregasParciaisPendentes" src="/coleta/executarRetornarColetasEntregas" cmd="entregasParciaisPendentes" boxWidth="180" height="320" />
	</adsm:tabGroup>
</adsm:window>


<script>
function initWindow(eventObj) {
	setDisabled('botaoConsultar', false);
	setDisabled('botaoLimpar', false);
	setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
	if(eventObj.name == "tab_load" || eventObj.name == 'cleanButton_click'){
		loadDadosSessao();
	} 
}
	
//Chama o servico que retorna os dados do usuario logado 
function loadDadosSessao(){
	var data = new Array();
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.findDadosSessao",
				"preencheDadosSessao",data);
	xmit({serviceDataObjects:[sdo]});
	}

//Funcao de callback do servico que retorna os dados do usuario logado. 
function preencheDadosSessao_cb(data, exception){
	if (exception == null){
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
		setElementValue("manifesto.filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		setElementValue("manifesto.filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
}
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	loadDataUsuario();
}

function limpar_OnClick(){
	resetValue('meioTransporte.idMeioTransporte');
	resetValue('meioTransporte2.idMeioTransporte');
	resetValue('controleCarga.idControleCarga');
	resetaTagManifesto();
	tabGroup.getTab("coletasPendentes").tabOwnerFrame.window.limpaGrid();
	tabGroup.getTab("entregasRealizar").tabOwnerFrame.window.limpaGrid();
	setFocusOnFirstFocusableField();
	loadDadosSessao();
}

function consultar_OnClick() {
	if (getElementValue('controleCarga.idControleCarga') == "") {
		alert(msgCriteriosNaoInformados);
		tabGroup.getTab("coletasPendentes").tabOwnerFrame.window.limpaGrid();
		tabGroup.getTab("entregasRealizar").tabOwnerFrame.window.limpaGrid();
		tabGroup.getTab("entregasParciaisPendentes").tabOwnerFrame.window.limpaGrid();		
		setFocus(document.getElementById("meioTransporte2.nrFrota"));
		return;
	}
	tabGroup.selectTab('coletasPendentes');
	tabGroup.getTab("coletasPendentes").tabOwnerFrame.window.povoaGrid(getElementValue('controleCarga.idControleCarga'));
}


/**
 * Carrega os dados do usuario (Usuario, Filial e Setor).
 */
function loadDataUsuario() {
   	var data = new Array();
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.getDataUsuario", "retorno_getDataUsuario", data);
   	xmit({serviceDataObjects:[sdo]});
}

function retorno_getDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("filialUsuario.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
	setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
	setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
}






/************************************ INICIO - MEIO DE TRANSPORTE ************************************/
function meioTransporte2_OnChange() {
	var r = meioTransporte2_nrFrotaOnChangeHandler();
	if (getElementValue('meioTransporte2.nrFrota') == "") {
		resetaTagManifesto();
		}
	return r;
}

function meioTransporte_OnChange() {
	var r = meioTransporte_nrIdentificadorOnChangeHandler();
	if (getElementValue('meioTransporte.nrIdentificador') == "") {
		resetaTagManifesto();
		}
	return r;
}

function retornoMeioTransporte2_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte2_nrFrota_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
	}
	return r;
}

function retornoMeioTransporte_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporte_nrIdentificador_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
	}
	return r;
}

function resetaMeioTransporte() {
}

function retornoPopupMeioTransporte(data) {
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.findControleCargaByMeioTransporte", 
									  "resultado_buscarControleCargaByMeioTransporte", 
									  {idMeioTransporte:data.idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa os campos com os dados retornados da busca em documento de serviço
 */
function resultado_buscarControleCargaByMeioTransporte_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetaControleCarga();
		resetaMeioTransporte();
		setFocus(document.getElementById("meioTransporte2.nrFrota"));
		return;
	}
	if (data != undefined) {
		setElementValue("controleCarga.idControleCarga", getNestedBeanPropertyValue(data,"controleCarga.idControleCarga"));
		setElementValue("controleCarga.nrControleCarga", getNestedBeanPropertyValue(data,"controleCarga.nrControleCarga"));
		setDisabled('controleCarga.idControleCarga', false);
		format(document.getElementById("controleCarga.nrControleCarga"));
	}
}
/************************************ FIM - MEIO DE TRANSPORTE ************************************/







/************************************ INICIO - CONTROLE DE CARGA ************************************/

function controleCargaFilial_OnChange() {
	var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	return r;
}

function retornoControleCargaFilial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}
	return r;
}

function resetaControleCarga() {
	resetValue('controleCarga.idControleCarga');
}

function popupControleCarga(data) {
	setDisabled('controleCarga.idControleCarga', false);
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.validateControleCarga", 
		"retorno_validateControleCarga", {tpStatusControleCarga:getNestedBeanPropertyValue(data,"tpStatusControleCarga.value")});
   	xmit({serviceDataObjects:[sdo]});
}

function retorno_validateControleCarga_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("controleCarga.idControleCarga");
		setFocus(document.getElementById("controleCarga.idControleCarga"));
		return false;
	}
}
/************************************ FIM - CONTROLE DE CARGA ************************************/







/************************************ INICIO - MANIFESTO ************************************/

/**
 * Limpa os dados informados na tag manifesto
 */
function resetaTagManifesto() {
	resetValue("manifesto.tpManifesto");
	resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
	resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
	setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
}

function retornoNrManifesto_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
   	if (r == true) {
   		buscarControleCarga();
	}
}

function manifestoNrManifestoOrigem_retornoPopup(data) {
	buscarControleCarga();
}

function enableManifestoManifestoViagemNacioal_cb(data) {
   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
   }
}

/**
 * Busca os dados relacionados ao Manifesto/Controle de cargas
 */
function buscarControleCarga() {
	var dataPesq = new Array();
	if (getElementValue('manifesto.tpManifesto') == "CO") {
		dataPesq = {idManifestoColeta:getElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional')};
	}
	else
	if (getElementValue('manifesto.tpManifesto') == "EN") {
		dataPesq = {idManifesto:getElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional')};
	}
	var sdo = createServiceDataObject("lms.coleta.executarRetornarColetasEntregasAction.findControleCargaByManifesto", 
			"resultado_buscarControleCarga", dataPesq);
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa os campos com os dados retornados da busca em Manifesto/Controle de cargas
 */
function resultado_buscarControleCarga_cb(data, error) {
	if (error != undefined) {
		alert(error);
			resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
			setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		return;
	}
	setElementValue("controleCarga.idControleCarga", getNestedBeanPropertyValue(data,"idControleCarga"));
	setElementValue("controleCarga.nrControleCarga", getNestedBeanPropertyValue(data,"nrControleCarga"));
	setElementValue("meioTransporte.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.idMeioTransporte"));
	setElementValue("meioTransporte.nrIdentificador", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrIdentificador"));
	setElementValue("meioTransporte2.idMeioTransporte", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.idMeioTransporte"));
	setElementValue("meioTransporte2.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrFrota"));
	setDisabled("controleCarga.nrControleCarga", false);
	format(document.getElementById("controleCarga.nrControleCarga"));
	setFocus(document.getElementById("botaoConsultar"));
}

function tpManifesto_OnChange() {
	resetaMeioTransporte();
	resetaControleCarga();
}

function manifestoSgFilial_OnChange() {
}

function manifestoNrManifestoOrigem_OnChange() {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
	if (r == true && getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
		resetaMeioTransporte();
		resetaControleCarga();
	}
	return r;
}

/************************************ FIM - MANIFESTO ***********************************/
</script>