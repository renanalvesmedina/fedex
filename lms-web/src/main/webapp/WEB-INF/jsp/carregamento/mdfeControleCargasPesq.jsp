<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="mdfeControleCarga" onPageLoadCallBack="mdfeControleCargasPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04085"/>
		<adsm:include key="LMS-04397"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/carregamento/mdfeControleCargasPesq" id="form1">
		<adsm:hidden property="idControleCargaTemp"/>
	</adsm:form>
	
	<adsm:grid idProperty="idManifestoEletronico" property="mdfe" selectionMode="radio" autoSearch="false"
				   unique="true" gridHeight="320" showPagging="false"  
				   service="lms.carregamento.emitirControleCargasAction.findPaginatedManifestoEletronicoByControleCarga"
				   rows="14" onRowClick="retornoNulo" onSelectRow="validaMdfe">
			   
		<adsm:gridColumn title="origem" property="sgFilialOrigem" width="9%" dataType="text" align="center"/>
		<adsm:gridColumn title="numero" property="nrManifestoEletronico" width="11%" dataType="text" align="center"/>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="9%" dataType="text" align="center"/>
		<adsm:gridColumn title="situacao" property="tpSituacao.description" width="14%" dataType="text" align="center"/>
		<adsm:gridColumn title="observacao" property="dsObservacao" width="57%" dataType="text" />	
			   
	</adsm:grid>
		
	<adsm:buttonBar>
		<adsm:button buttonType="button" id="btnCancelarMdfe" caption="cancelar" disabled="true" onclick="cancelarMdfe();" />
		<adsm:button buttonType="button" id="btnReemitirMdfe" caption="reemitir" disabled="true" onclick="reemitirMdfe();" />
	</adsm:buttonBar>
	
</adsm:window>

<script>
	
	document.getElementById('form1').style.display='none';

	function mdfeControleCargasPageLoad_cb(){
		onPageLoad_cb();
		
		if (dialogArguments) {
			setElementValue("idControleCargaTemp", dialogArguments.document.getElementById("controleCarga.idControleCarga").value);
		}
		
		findButtonScript('mdfe', document.forms[0]);
		window.returnValue = window;
	}
	
	
	function mdfeOnRowClick() {
		return false;
	}
	
	function validaMdfe(rowRef) {
		var data = mdfeGridDef.gridState.data[rowRef.rowIndex];
		
		var criteria = new Object();
		
		criteria.tpSituacao = getNestedBeanPropertyValue(data,"tpSituacao.value");
		criteria.idFilial = getNestedBeanPropertyValue(data,"idFilial");
		
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.verificaSituacao", "liberaAcao", 
				criteria);
	   	xmit({serviceDataObjects:[sdo]});
	}
	
	function liberaAcao_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		if(data._value == 0 ){
			setDisabled("btnCancelarMdfe", false);
			setDisabled("btnReemitirMdfe", true);
		}else if(data._value == 1 ){
			setDisabled("btnReemitirMdfe", false);
			setDisabled("btnCancelarMdfe", true);
		}else if(data._value == 2 ){
			setDisabled("btnReemitirMdfe", false);
			setDisabled("btnCancelarMdfe", false);
		}else {
			setDisabled("btnReemitirMdfe", true);
			setDisabled("btnCancelarMdfe", true);
		}
	}
	
	function retornoNulo(){
		return false;
	}
	
	function cancelarMdfe(){
		var cancelaMdfe = confirm("LMS-04397 - " + getI18nMessage("LMS-04397"));
		if(cancelaMdfe){
			if (mdfeGridDef.getSelectedIds().ids.length>0) {
				var data = new Object();
				data.idManifestoEletronico = mdfeGridDef.getSelectedIds().ids[0];
				var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.cancelarMdfe", "cancelarMdfe", data);
			    xmit({serviceDataObjects:[sdo]});
			}
		}
	}

	function cancelarMdfe_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			findButtonScript('mdfe', document.forms[0]);
			return false;
		}

		verificaCancelamentoMdfe(data);		
	}
	
	function verificaCancelamentoMdfe(data) {
		var dataEnviar = buildFormBeanFromForm(document.forms[0]);
		dataEnviar.idManifestoEletronicoCancelado = data.idManifestoEletronicoCancelado;
		dataEnviar.dhEncerramento = data.dhEncerramento;
		showMessageMDFe();
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.verificaCancelamentoMdfe", "verificaCancelamentoMdfe", dataEnviar);
	 	xmit({serviceDataObjects:[sdo]});
	}
	
	function verificaCancelamentoMdfe_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			refreshGrid();
			hideMessageMDFe();
			return false;
		}
		
		if(data.limiteEspera) {
			setTimeout(function() {
					unblockUI();
					verificaCancelamentoMdfe(data);
					return false;
				},
				data.limiteEspera*1000);
		} else {
			/* Libera a tela e some com o loader que tinha sido setado por tempo ilimitado. */
			refreshGrid();
			hideMessageMDFe();
			alertI18nMessage("LMS-04085");
			findButtonScript('mdfe', document.forms[0]);
		}
	}
	
	/* 	Exibe loader e bloqueia tela por tempo ilimitado. O mesmo só será removido na função 'hideMessageMDFe'. 
	Implementado dessa forma para garantir que tela não fique liberada nos intervalos das chamadas, e para que loader seja exibido durante toda a espera do processo de cancelamento da MDFe. */
	function showMessageMDFe(){
		blockGridAndButtons(true);
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe == null || messageMDFe == undefined){
			showSystemMessage('processando', null, true);
			var messageSign = doc.getElementById("message.div");
			var messageMDFe = messageSign;
			messageMDFe.id = 'messageMDFe.div';
			doc.body.appendChild(messageMDFe);
		}
	}
	
	function hideMessageMDFe(){
		blockGridAndButtons(false);
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe != null && messageMDFe != undefined){
			doc.body.removeChild(messageMDFe)
		}		
	}
	
	function blockGridAndButtons(block){
		var allElements = document.getElementsByTagName("*");
		for (var i=0; i < allElements.length; i++) {
			var obj = allElements[i]; 
			if(obj.type == 'radio'){
		    	obj.disabled=block;
			}
		}
		
		/* Os botões ficam sempre desabilitados, forçando o usuário a clicar no item da grid, para executar a regra que habilita os mesmos. */
		setDisabled("btnReemitirMdfe", true);
		setDisabled("btnCancelarMdfe", true);
	}
	
	function refreshGrid(){
		var data = new Object();
		data.idControleCargaTemp = document.getElementById('idControleCargaTemp').value;
		var grid = getElement("mdfe.dataTable").gridDefinition;
		grid.executeSearch(data);
	}
	
	function reemitirMdfe(){
		if (mdfeGridDef.getSelectedIds().ids.length > 0) {
			var parameters = new Object();
			parameters.idsManifestoEletronico = [mdfeGridDef.getSelectedIds().ids[0]];
			var sdo = createServiceDataObject('lms.carregamento.emitirControleCargasAction.imprimirMDFe', 'openPdf', parameters);
			executeReportWindowed(sdo, 'pdf');
		}
	}
	
</script>