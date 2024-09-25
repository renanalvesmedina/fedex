<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="comunicarUnidadesEmissaoRIM" service="lms.seguros.comunicarUnidadesEmissaoRimReportAction" onPageLoad="loadObjects">
	<adsm:form action="/seguros/comunicarUnidadesEmissaoRIM" id="emitirCartaForm">
	
		<adsm:section caption="dadosCarta" />
		
		<adsm:combobox property="filial" optionLabelProperty="filialComunicadaLabel" optionProperty="filialComunicadaValue" 
					   label="filialComunicada" service="" autoLoad="false" required="true"/>
					   
		<adsm:buttonBar>
			<adsm:button id="enviar" caption="enviarEmail" onclick="enviarEmail()"/>
			<adsm:button id="visualizar" caption="visualizar" onclick="generateLetter()"/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function loadObjects(){
		var dataObject = new Object();
    	var sdo = createServiceDataObject("lms.seguros.comunicarUnidadesEmissaoRimReportAction.getBasicData", "loadObjects", dataObject);
    	xmit({serviceDataObjects:[sdo]});	
	}
	
	function loadObjects_cb(data, error) {
		filial_cb(data.filialComunicada);
		disableButtons(false);
		onPageLoad();
	}
	
	function disableButtons(disable){
		setDisabled("enviar", disable);
		setDisabled("visualizar", disable);
		setDisabled("fechar", disable);
	}

	function enviarEmail(){
		
		if ((validateTabScript(document.getElementById("emitirCartaForm")))==false) return false;
		
		showModalDialog('seguros/comunicarUnidadesEmissaoRIM.do?cmd=enviarEmail&destinatarioCarta='+getElementValue("filial"),
			dialogArguments.window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	}
	
	/**
	 * Carrega a grid da tela pai e faz as devidas validacao para a impressao do relatorio.
	 */
	function generateLetter(){
	
		if ((validateTabScript(document.getElementById("emitirCartaForm")))==false) return false;
		
		var dataObject = mountDataObject();
		var sdo = createServiceDataObject('lms.seguros.comunicarUnidadesEmissaoRimReportAction.execute',  'openReportWithLocator', dataObject); 
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	/**
	 * Monta o objeto de data necessario para gerar a carta...
	 */
	function mountDataObject(){
		var parentWindow = dialogArguments.window;
		var idsSinistroDoctoServico = parentWindow.getSelectedIds;
		
		var dataObject = new Object();
			
    	setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", idsSinistroDoctoServico.ids);
    	setNestedBeanPropertyValue(dataObject, "filial", getElementValue("filial"));
	
		return dataObject;
	}

	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
</script>
