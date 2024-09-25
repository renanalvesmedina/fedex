<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form 	action="/vendas/manterCarteiraVendas" service="lms.vendas.manterCarteiraVendasAction.findById"
				idProperty="idCarteiraVendas" onDataLoadCallBack="myOnDataLoad">
	 	<adsm:textbox property="numeroLote" label="numeroLote" dataType="text" 
	 		width="43%" labelWidth="17%"
	 		size="16" maxLength="16"
	 		disabled="true"/>
	 	<adsm:combobox property="tpSituacaoAprovacao" label="situacaoAprovacao" domain="DM_STATUS_WORKFLOW"
	 		width="25%" labelWidth="15%"
	 		disabled="true"/>

		<adsm:lookup property="usuario" label="vendedor" required="true"
			idProperty="idUsuario" criteriaProperty="nrMatricula"			
			service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor"
			action="/configuracoes/consultarFuncionarios" cmd="promotor"
			dataType="text"
			size="16" maxLength="16"
			width="43%"
			labelWidth="17%"
			onDataLoadCallBack="vendedorOnChange()"
			onchange="vendedorOnChange">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" inlineQuery="true"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false" onchange="vendedorOnChange"/>
		</adsm:lookup>
		
		<adsm:hidden property="pendencia.idPendencia"/>		

		<adsm:checkbox property="blEfetivado" label="efetivado" labelWidth="15%" width="25%" disabled="true"/>
		
		<adsm:textbox property="dtInicioLote" label="dataInicio" dataType="JTDate" labelWidth="17%" width="43%" disabled="true"/>
		<adsm:textbox property="dtFimLote" label="dataFim" dataType="JTDate" labelWidth="15%" width="25%" disabled="true"/>
		
		<adsm:checkbox property="blEfetivadoNivel1" label="efetivadoNivel1" labelWidth="17%" width="43%" disabled="true"/>
		<adsm:checkbox property="blEfetivadoNivel2" label="efetivadoNivel2" labelWidth="15%" width="25%" disabled="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="fluxoAprovacao" id="btnFluxo" disabled="true"
				onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendencia.idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');"/>
			<adsm:storeButton service="lms.vendas.manterCarteiraVendasAction.store" id="storeButton"/>
			<adsm:button caption="aprovacao" id="btnAprovacao" onclick="onClickAprovacao()" disabled="true"/>
			<adsm:button caption="botaoEfetivar" id="btnEfetivar" onclick="onClickEfetivar()" disabled="true"/>
			<adsm:resetButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>



function initWindow(event){
	//Se está em modo inclusão, busca os dados padrão
	if (getElementValue("idCarteiraVendas").length == 0){
		var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.findBasicIncludeData","findBasicIncludeData");
		xmit({serviceDataObjects:[sdo]});
	}else{
		prepareWindow(true);
	}	
}

function myOnDataLoad_cb(data,error){
	onDataLoad_cb(data,error);
	setElementValue("numeroLote",getNestedBeanPropertyValue(data,"idCarteiraVendas"));
	if (getElementValue("numeroLote").length > 0){
		getTabGroup(document).setDisabledTab("clientes",false);
		prepareWindow(true);
	}
}

function onClickAprovacao(){
	var data = new Object();
	data.idCarteiraVendas = getElementValue("idCarteiraVendas");
	var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.executeAprovacao","executeAprovacao",data);
	xmit({serviceDataObjects:[sdo]});
}

function executeAprovacao_cb(data,error){
	if (error != undefined){
		alert(error);
		return;
	}
	myOnDataLoad_cb(data,error);
}

function onClickEfetivar(){
	var data = new Object();
	data.idCarteiraVendas = getElementValue("idCarteiraVendas");
	var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.efetivarCarteira","efetivarCarteira",data);
	xmit({serviceDataObjects:[sdo]});
}

function efetivarCarteira_cb(data,error){
	if(error!=undefined){
		alert(error);
		return;
	}
	myOnDataLoad_cb(data,error);
}

function prepareWindow(isSaved){
	setDisabled("storeButton", !isSaved);
	situacaoAprovacao = getElementValue("tpSituacaoAprovacao");
	var aprovacao = (situacaoAprovacao.length > 0)
	setDisabled("btnAprovacao",aprovacao);
	setDisabled("btnFluxo",!aprovacao);

	var efetivado1 = getElementValue("blEfetivadoNivel1");
	var efetivado2 = getElementValue("blEfetivadoNivel2");
	
	if (situacaoAprovacao == "A" && (!efetivado1 || !efetivado2)){
		setDisabled("btnEfetivar",false);
	}	
}

function findBasicIncludeData_cb(data,error){
	onPageLoad_cb(data,error);
	setElementValue("usuario.idUsuario",getNestedBeanPropertyValue(data,"idUsuario"));
	setElementValue("usuario.nrMatricula",getNestedBeanPropertyValue(data,"nrMatricula"));
	setElementValue("usuario.nmUsuario",getNestedBeanPropertyValue(data,"nmUsuario"));
	setElementValue("dtInicioLote",getNestedBeanPropertyValue(data,"dtInicio"));
	getTabGroup(document).setDisabledTab("clientes",true);
}

function vendedorOnChange(){
	return true;
}

//Função sobrescrita de callback do botão de salvar
function store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (eventObj == undefined) {
		eventObj = {name:"storeButton"};
	}
	myOnDataLoad_cb(data,errorMsg);
	if (errorMsg == null) {
		var tab = getTab(document, false);
		if (tab) {
			eventObj.src = tab.tabGroup.selectedTab;
		}
        showSuccessMessage();
    	initWindowScript(document.parentWindow, eventObj);
	} else {
		if ((showErrorAlert == undefined) || (showErrorAlert == true)) {
			alert(errorMsg);
		}
		setFocusOnFirstFocusableField(document);
	}
}
</script>
