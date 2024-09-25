<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="emitirCartaOcorrencia" service="lms.seguros.emitirCartaOcorrenciaReportAction" onPageLoad="carregaPagina">
	<adsm:form action="/seguros/emitirCartaOcorrencia" id="emitirCartaForm">
	
		<adsm:section caption="dadosCarta" />
		
		<adsm:combobox property="tipoCarta" domain="DM_TIPO_CARTA" label="tipoCarta" required="true" renderOptions="true"/>
					   
		<adsm:combobox property="destinatarioCarta" domain="DM_DESTINATARIO_CARTA" label="destinatarioCarta" required="true"/>
		
		<adsm:buttonBar>
			<adsm:button id="enviar" caption="enviarEmail" onclick="enviarEmail()"/>
			<adsm:button id="visualizar" caption="visualizar" onclick="generateLetter()"/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function carregaPagina() {
		disableButtons(false);
		onPageLoad();
	}
	
	function disableButtons(disable) {
		setDisabled("enviar", disable);
		setDisabled("visualizar", disable);
		setDisabled("fechar", disable);
	}

	function enviarEmail(){
		if ((validateTabScript(document.getElementById("emitirCartaForm"))) == false) {
			return false;
		}
		
		var linkParameters = '&destinatarioCarta='+getElementValue("destinatarioCarta")+'&tipoCarta='+getElementValue("tipoCarta");
		showModalDialog('seguros/emitirCartaOcorrencia.do?cmd=enviarEmail'+linkParameters,dialogArguments.window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	}
	
	/**
	 * Carrega a grid da tela pai e faz as devidas validacao para a impressao do relatorio.
	 */
	function generateLetter(){
		var dataObject = new Object();
		
		setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", dialogArguments.window.idsSinistroDoctoServico);
		
		var sdo = createServiceDataObject('lms.seguros.emitirCartaOcorrenciaAction.findClienteByIdsSinistroDoctoServico', 'generateLetter', dataObject); 
		xmit({serviceDataObjects:[sdo]});
	}
	
	function generateLetter_cb(data, error){
		if ((validateTabScript(document.getElementById("emitirCartaForm")))==false) return false;
		
		var dataObject = mountDataObject();
		
		if (getElementValue("destinatarioCarta")=="A") {
			getIdsDestinatario(dataObject, data);
		}
		
		var sdo = createServiceDataObject('lms.seguros.emitirCartaOcorrenciaReportAction.execute', 'openPdfLetter', dataObject); 
		xmit({serviceDataObjects:[sdo]});
	}
	
	function openPdfLetter_cb(data, error) {
		openReportWithLocator(data, error);
		
		var dataObject = mountDataObject();
		var sdo = createServiceDataObject("lms.seguros.emitirCartaOcorrenciaAction.updateDataGeracaoCarta", "closeLetter", dataObject);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	function closeLetter_cb(data, error) {
		return true;
	}
	
	/**
	 * Monta o objeto de data necessario para gerar a carta...
	 */
	function mountDataObject(){
		var sinistroDoctoServicoGridDef = dialogArguments.window.idsSinistroDoctoServico;
		
		var dataObject = new Object();
			
    	setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", sinistroDoctoServicoGridDef);
    	setNestedBeanPropertyValue(dataObject, "tipoCarta", getElementValue("tipoCarta"));
    	setNestedBeanPropertyValue(dataObject, "destinatarioCarta", getElementValue("destinatarioCarta"));
	
		return dataObject;
	}
	
	function getIdsDestinatario(dataObject, data){
		var sinistroDoctoServicoGridDef = dialogArguments.window.idsSinistroDoctoServico;
		
		var idObject = null;
		var list = new Array();
		var listSize = 0;
		
		for (i=0; i<sinistroDoctoServicoGridDef.length; i++) {
			idObject = sinistroDoctoServicoGridDef[i];

			for (j=0; j<sinistroDoctoServicoGridDef.length; j++) {
				var rowData = data.sinistros[j];
				
				if (rowData.idSinistroDoctoServico==idObject){    				
    				if (rowData.nmPessoaClienteRemetente!=rowData.nmPessoaClienteDestinatario) {
    					list[listSize] = rowData.idSinistroDoctoServico;
    					listSize++;
    				}
				}
			}
		}	
		//Seta o objeto com os ids de destinatario...
		setNestedBeanPropertyValue(dataObject, "idsDestinatario", list);
		//Determina que este xmit sera para um relatorio...
		dataObject._reportCall = true; 	
		
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