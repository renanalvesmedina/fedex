<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterReciboBrasilAction" onPageLoadCallBack="myOnPageLoad" onPageLoad="myOnPageLoad">
	<adsm:form action="/contasReceber/manterReciboBrasil">
		<adsm:i18nLabels>
			<adsm:include key="LMS-36221"/>
		</adsm:i18nLabels>
        
        <adsm:hidden property="idProcessoWorkflow"/>
        
        <adsm:lookup label="filialCobranca" property="filialByIdFilialEmissora" 
        	service="lms.municipios.filialService.findLookup" 
        	action="/municipios/manterFiliais" idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="3" required="true"
        	maxLength="3" labelWidth="20%" width="80%">
            <adsm:propertyMapping relatedProperty="filialByIdFilialEmissora.pessoa.nmFantasia"
	            modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialByIdFilialEmissora.pessoa.nmFantasia" 
				size="30" disabled="true"/>
        </adsm:lookup>				

		<adsm:textbox dataType="integer" property="nrRecibo" label="numeroRecibo" size="10" maxLength="10" labelWidth="20%" width="30%" mask="0000000000"/>

		<adsm:range label="dataEmissao" labelWidth="20%" width="30%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" picker="true"/>
	    	<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" picker="true"/>
		</adsm:range>

		<adsm:combobox label="situacao" property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_FRETE" labelWidth="20%" width="30%"/>

        <adsm:combobox label="situacaoAprovacao" property="tpSituacaoAprovacao" domain="DM_STATUS_WORKFLOW" labelWidth="20%" width="30%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('recibo', this.form);"/> 
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRecibo" property="recibo" rows="12" selectionMode="none"
		service="lms.contasreceber.manterReciboBrasilAction.findPaginatedTela"
		rowCountService="lms.contasreceber.manterReciboBrasilAction.getRowCountTela">
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="recibo" property="filialByIdFilialEmissora.sgFilial" width="50"/>
			<adsm:gridColumn title="" property="nrRecibo" dataType="integer" width="50" mask="0000000000"/>
		</adsm:gridColumnGroup>	

		<adsm:gridColumn title="dataEmissao" property="dtEmissao" dataType="JTDate" width="120"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoRecibo" isDomain="true" width="110"/>
		<adsm:gridColumn title="situacaoAprovacao" property="tpSituacaoAprovacao" isDomain="true" width="180"/>		
		<adsm:gridColumn title="valorTotalRecebido" property="vlTotalRecibo" dataType="currency"/>
		
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
<script>

function myOnPageLoad_cb(d,e){
	onPageLoad_cb(d,e);
	validaBotaoFechar();
	initPage();
}

/**
  *	Valida se deve ou não tornar o botão de fechar visível
  */
function validaBotaoFechar(){
	
	var url = new URL(location.href);
	if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
		document.getElementById('btnFechar').property = ".closeButton";
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}	
}

function initPage(){
	_serviceDataObjects = new Array();	
	addServiceDataObject(createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findFilialCliente", "findFilialCliente"));
	xmit(false);
}

function findFilialCliente_cb(d,e){
	if (e == undefined && document.getElementById("filialByIdFilialEmissora.sgFilial").masterLink != 'true') {
		setElementValue("filialByIdFilialEmissora.idFilial", d.filialByIdFilialEmissora.idFilial);
		setElementValue("filialByIdFilialEmissora.sgFilial", d.filialByIdFilialEmissora.sgFilial);
		setElementValue("filialByIdFilialEmissora.pessoa.nmFantasia", d.filialByIdFilialEmissora.pessoa.nmFantasia);
	}
}

function initWindow(eventObj){
	
	if (eventObj.name == "cleanButton_click") {
		initPage();
	}
}

function myFindButtonScript(callback, form){
	if (!validateForm(document.forms[0])){
		return false;
	}
	
	if ((getElementValue("nrRecibo") == "") &&
		(getElementValue("dtEmissaoInicial") == "" || getElementValue("dtEmissaoFinal") == "")
		){
		alertI18nMessage("LMS-36221");
		return false;
	}
	
	return findButtonScript(callback, form); 
}

function myOnPageLoad(){
	
	var url = new URL(parent.location.href);
		
	/** Caso o idProcessoWorkFlow venha na URL, seleciona a Tab de CAD */
	if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
		setDisabled(document, true);
		getTabGroup(this.document).getTab("pesq").setDisabled(true);
		getTabGroup(this.document).selectTab("cad", "tudoMenosNulo", true);
	}else{
		onPageLoad();
	}
	
}
</script>