<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.manterAcoesAction">
	<adsm:form action="/workflow/manterAcoes" idProperty="idAcao" onDataLoadCallBack="myOnDataLoad">
		<adsm:hidden property="idPendencia" serializable="false"/>
		<adsm:hidden property="nmClasseVisualizacao" serializable="false"/>		
		<adsm:hidden property="nmChaveTitulo" serializable="false"/>				
		<adsm:hidden property="blRequerAprovacao" serializable="false"/>		
		<adsm:textbox dataType="text" property="dsTipoEvento" label="evento" disabled="true" size="40"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhInclusao" labelWidth="19%" width="31%" label="dataInclusao" disabled="true" picker="false"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhLiberacao" label="dataLiberacao" disabled="true" picker="false"/>
		<adsm:textbox dataType="integer" property="idProcesso" labelWidth="19%" width="31%" label="processo" size="16" disabled="true"/>
		<adsm:textbox dataType="text" property="nmUsuario" labelWidth="15%" width="85%" label="solicitante" size="40" disabled="true"/>

		<adsm:textarea columns="120" width="85%" rows="18" maxLength="4000" property="dsPendencia" label="descricaoProcesso" disabled="true"/>
		<adsm:textarea required="true" columns="120" width="85%" rows="5" maxLength="800" property="obAcao" label="observacao" />
		<adsm:buttonBar>
			<adsm:button caption="aprovar" id="aprovar" service="lms.workflow.manterAcoesAction.saveAprovarAcao" callbackProperty="saveAcoesAprovadas"/>
			<adsm:button caption="reprovar" id="reprovar" service="lms.workflow.manterAcoesAction.saveReprovarAcao" callbackProperty="saveAcoesAprovadas"/>
			<adsm:button caption="visualizado" id="visualizado" service="lms.workflow.manterAcoesAction.saveVisualizarAcao" callbackProperty="saveAcoes"/>
			<adsm:button caption="historicoAprovacao" id="btnFluxoAprovacao"
				onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');"/>
			<adsm:button caption="visualizarProcesso" id="visualizarProcesso" onclick="navigate();"/>	
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

function isNewFrontEnd(url) {
	return url.indexOf('.do') === -1;
}

function navigate() {
   var url = getElementValue("nmClasseVisualizacao") + "&idProcessoWorkflow=" + getElementValue("idProcesso")+"&tela=acao";
   
   if(isNewFrontEnd(url)) {
	   var contextPath = '<%=request.getContextPath()%>/';
	   var completeURL = contextPath + url;
	   window.open(completeURL, "_blank", "resizable=yes, scrollbars=yes");
   } else {
	   showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:500px;');
   }
}

function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj){
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	if (getElementValue("nmClasseVisualizacao") == ""){
		setDisabled("visualizarProcesso",true);
	} else {
		setDisabled("visualizarProcesso",false);	
	}

	if (getElementValue("blRequerAprovacao") == "false"){
		setDisabled("aprovar",true);
		setDisabled("reprovar",true);
		setDisabled("visualizado",false);		
	} else {
		setDisabled("aprovar",false);
		setDisabled("reprovar",false);			
		setDisabled("visualizado",true);				
	}		
	
}

function saveAcoesAprovadas_cb(data, errorMessage, errorCode, eventObj){
	if (data != undefined && data["_value"] != undefined) {
		alert(data["_value"]+"");	
	}
	saveAcoes_cb(data, errorMessage, errorCode, eventObj);
}

function saveAcoes_cb(data, errorMessage, errorCode, eventObj){
	if (errorMessage != undefined) {
		alert(errorMessage+'');
	} else {
		setDisabled("aprovar",true);
		setDisabled("reprovar",true);
		setDisabled("visualizado",true);
		showSuccessMessage();
	}
	getTabGroup(this.document).getTab("cad").setChanged(false);
	
	setFocus('btnFluxoAprovacao',true,true);
	
}
</script>