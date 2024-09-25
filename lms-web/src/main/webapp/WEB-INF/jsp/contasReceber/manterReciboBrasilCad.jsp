<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterReciboBrasilAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterReciboBrasil" idProperty="idRecibo"
	newService="lms.contasreceber.manterReciboBrasilAction.newMaster" onDataLoadCallBack="myDataLoadCallBack">

		<adsm:hidden property="idProcessoWorkflow" value=""/>	
		
        <adsm:lookup label="filialCobranca" property="filialByIdFilialEmissora" 
        	service="lms.municipios.filialService.findLookup" 
        	action="/municipios/manterFiliais" idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="3" 
        	maxLength="3" width="80%" labelWidth="20%" required="true">
            <adsm:propertyMapping relatedProperty="filialByIdFilialEmissora.pessoa.nmFantasia"
	            modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialByIdFilialEmissora.pessoa.nmFantasia" 
				size="30" disabled="true"/>
        </adsm:lookup>	

		<adsm:textbox dataType="integer" property="nrRecibo" label="numeroRecibo" size="10" maxLength="10" width="30%" labelWidth="20%" disabled="true" mask="0000000000"/>

		<adsm:textbox dataType="JTDate" property="dtEmissao" label="dataEmissao" labelWidth="20%" width="30%" picker="true" required="true"/>

		<adsm:combobox label="situacao" property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_FRETE" width="30%" labelWidth="20%" defaultValue="D" disabled="true"/>

        <adsm:combobox label="situacaoAprovacao" property="tpSituacaoAprovacao" domain="DM_STATUS_WORKFLOW" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlTotalDocumentos" label="valorTotalDocumentos" maxLength="18" size="18" width="30%" labelWidth="20%" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlTotalDesconto"  label="valorTotalDesconto" maxLength="18" size="18" labelWidth="20%" width="30%" disabled="true"/>
		
		<adsm:textbox dataType="currency" property="vlTotalJuros"  label="jurosCalculado" maxLength="18" size="18" width="30%" labelWidth="20%" disabled="true"/>
		
		<adsm:textbox dataType="currency" property="vlTotalJuroRecebido"  label="jurosRecebidos" maxLength="18" size="18" labelWidth="20%" width="30%" disabled="true"/>		

		<adsm:textbox dataType="currency" property="vlTotalRecibo"  label="valorTotalRecebido" maxLength="18" size="18" width="80%" labelWidth="20%" disabled="true"/>		

		<adsm:textarea columns="100" rows="3" maxLength="255" property="obRecibo" label="observacao" width="80%" labelWidth="20%"/>		

		<adsm:hidden property="pendencia.idPendencia"/>	

		<adsm:buttonBar>
			<adsm:button caption="historicoAprovacao" id="btnHistoricoAprovacao" disabled="true"
						 onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendencia.idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');" />
			<adsm:button id="emitirRecibo" caption="emitirRecibo" onclick="emiteRecibo();" />
            <adsm:button id="cancelar" caption="cancelar" service="lms.contasreceber.manterReciboBrasilAction.cancelRecibo" callbackProperty="store"/>
			<adsm:storeButton id="storeButton" callbackProperty="myStore"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

document.getElementById("filialByIdFilialEmissora.idFilial").masterLink = "true";
document.getElementById("filialByIdFilialEmissora.sgFilial").masterLink = "true";
document.getElementById("filialByIdFilialEmissora.pessoa.nmFantasia").masterLink = "true";

setDisabled("filialByIdFilialEmissora.idFilial", true);

function emiteRecibo() {
	executeReportWithCallback("lms.contasreceber.emitirReciboFreteAction.execute", "showReport", document.forms[0]);
}

function myOnPageLoad_cb(d,e){
	onPageLoad_cb(d,e);
	initPage();
	
	var url = new URL(parent.location.href);
	
	if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
		onDataLoad(url.parameters.idProcessoWorkflow);		
	}

}

function initPage(){
	_serviceDataObjects = new Array();	
	addServiceDataObject(createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findDataAtual", "findDataAtual"));
	addServiceDataObject(createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findFilialCliente", "findFilialCliente"));
	xmit(false);
}

function findFilialCliente_cb(d,e){
	if (e == undefined) {
		setElementValue("filialByIdFilialEmissora.idFilial", d.filialByIdFilialEmissora.idFilial);
		setElementValue("filialByIdFilialEmissora.sgFilial", d.filialByIdFilialEmissora.sgFilial);
		setElementValue("filialByIdFilialEmissora.pessoa.nmFantasia", d.filialByIdFilialEmissora.pessoa.nmFantasia);
	}
}

function findDataAtual_cb(d,e){
	if (e == undefined) {
		setElementValue("dtEmissao", setFormat("dtEmissao", d.dtEmissao));
	}
}

function initWindow(eventObj){
	if (eventObj.name == "newButton_click" || eventObj.name == "tab_click" ){
		if (getElementValue("idRecibo") != ""){
			setDisabled("emitirRecibo",false);
			setDisabled("cancelar",false);
		} else {
			setDisabled("emitirRecibo",true);
			setDisabled("cancelar",true);
		}
	}
	    
    if (eventObj.name == "tab_click") {
    	if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
    		initPage();
    	}
    }	 
	
	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
		initPage();
	}
	
	var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
	/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
	if (idProcessoWorkflow != ""){
		setDisabled(document, true);
	}
}

function myStore_cb(d,e,c,x){
	store_cb(d,e,c,x);

	if (e == undefined){
		copyMasterLink(this.document, getTabGroup(this.document).getTab("faturas").tabOwnerFrame.document);
	}
}

function showReport_cb(strFile, error){

	if (error == null) {
		if( getElementValue('tpSituacaoRecibo') == 'D' ){	
			setElementValue("tpSituacaoRecibo", "E");
		}		
		openReportWithLocator(strFile._value); // definido em reports.js
	} else {
		alert(error+'');
	}
		
}

function myDataLoadCallBack_cb(data, error){
	
	onDataLoad_cb(data);
	
	var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
	/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
	if (idProcessoWorkflow != ""){
		showSuccessMessage();
		setDisabled(document, true);
	}
	
}

</script>