
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.manterProcessosSinistroAction" onPageLoad="pageLoad" >
	<adsm:form action="/seguros/manterProcessosSinistroManifestosDocumentos" idProperty="idSinistroDoctoServico"  >

		<adsm:hidden property="idProcessoSinistroManifesto" serializable="true" />

		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />

		<adsm:textbox property="sgFilialManifesto" serializable="false" label="manifesto" dataType="text" size="3" disabled="true" labelWidth="20%" width="80%">
			<adsm:textbox property="nrManifesto"   serializable="false" dataType="text" size="10" disabled="true" />
		</adsm:textbox>
		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
		<script>
			var LMS_22008 = '<adsm:label key="LMS-22008"/>';
		</script>
	</adsm:form>
	
	<adsm:grid property="sinistroDoctoServico"
				idProperty="idSinistroDoctoServico" 
				selectionMode="none" 
				scrollBars="horizontal" 
				gridHeight="287"
				onRowClick="onRowClick" onDataLoadCallBack="processaGrid"
				onPopulateRow="onPopulateRow"
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedDocumentosPopup"
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountDocumentosPopup" 
				defaultOrder="doctoServico_.tpDocumentoServico, doctoServico_filialByIdFilialOrigem_.sgFilial, doctoServico_.nrDoctoServico"
				>

		<adsm:gridColumn property="tpDocumentoServico" isDomain="true" title="documentoServico" dataType="text"  width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="sgFilialDocumento" dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDocumentoServico" dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino"       property="sgFilialDestino" width="60" />
		<adsm:editColumn title="tipoPrejuizo"  property="tpPrejuizo" field="combobox" width="130" domain="DM_TIPO_PREJUIZO" required="true"/>
		<adsm:gridColumn property="sgSimboloMoedaPrejuizo" dataType="text" title="valorPrejuizo" width="60"/>
		<adsm:editColumn title="" property="vlPrejuizo" field="textbox"  width="100" dataType="currency"/>
		<adsm:gridColumn property="sgSimboloMoedaMercadoria" dataType="text" title="valorMercadoria" width="60"/>
		<adsm:gridColumn title="" property="vlMercadoria" width="100" align="right" dataType="currency" />
		<adsm:gridColumn title="remetente"      property="remetente" width="180" />
		<adsm:gridColumn title="destinatario"   property="destinatario" width="180" />
		<adsm:gridColumn title="volumes"        property="volumes"        width="90" align="right" dataType="integer" />
		<adsm:gridColumn title="peso"           property="peso"          width="130" align="right" unit="kg" mask="#,##0.000" dataType="decimal" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="rim"  property="sgFilialRIM" width="40" dataType="text" />
			<adsm:gridColumn  title=""          property="numeroRIM" width="90" dataType="integer" mask="000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="sgSimboloMoedaRIM" dataType="text" title="valorRIM" width="60" />
		<adsm:gridColumn title="" property="valorRIM" width="100" align="right" dataType="currency"  />
		<adsm:gridColumn title="dataPagamento"  property="dtPagamento" width="150" align="center" dataType="JTDate"/>
		<adsm:gridColumn title="dataGeracaoRim"  property="dtGeracaoRim" width="150" align="center" dataType="JTDate"/>
		<adsm:gridColumn property="dhCarta" title="dataHoraCarta" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="dhEnvio" title="dataHoraEnvio" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="tipoCarta" title="tipoCarta" width="100" />
		<adsm:editColumn title="hidden" property="vlMercadoriaHidden" dataType="text" field="hidden" width="" />
		<adsm:editColumn title="hidden" property="flagRIM"            dataType="text" field="hidden" width="" />
		<adsm:buttonBar>
		    <adsm:button caption="salvar" onclick="storeEditGridScript('lms.seguros.manterProcessosSinistroAction.storeDocumentosPopup', 'gridStore', document.forms[0], this.form);" disabled="false" buttonType="closeButton"/>
			<adsm:button id="closeButton" caption="fechar" disabled="false" onclick="onCloseButtonClick();" buttonType="closeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	// setando dados recebidos como masterlink
	document.getElementById('nrProcessoSinistro').masterLink = 'true';	
	document.getElementById('nrManifesto').masterLink = 'true';

	function onPopulateRow(tr, data) {
		var objTipoPrejuizo = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".tpPrejuizo");
		var	valorPrejuizo = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".vlPrejuizo");
		var	valorMercadoria = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".vlMercadoriaHidden");		
		var flagRIM = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".flagRIM"); 
						
		if (flagRIM.value == 'S') {
			setDisabled(valorPrejuizo, true);
			setDisabled(objTipoPrejuizo, true);
			
		} else {
		
			objTipoPrejuizo.onchange = function(){
	
				if (objTipoPrejuizo.value=="S"){
					setDisabled(valorPrejuizo, true);
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
				} else if (objTipoPrejuizo.value=="P"){
					setDisabled(valorPrejuizo, false);
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
					setFocus(valorPrejuizo);
				} else if (objTipoPrejuizo.value=="T"){
					
					// valorPrejuizo.value = valorMercadoria.value;
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, valorMercadoria.value));
					setDisabled(valorPrejuizo, true);
				}
			};
			valorPrejuizo.onblur = function(){
				
				if(getElementValue(valorPrejuizo)==""){
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
				} else {
					var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.validaValoresByTpPrejuizo", "validaValores", 
						{tpPrejuizo:getElementValue(objTipoPrejuizo),
						 vlPrejuizo:getElementValue(valorPrejuizo),
						 vlMercadoria:getElementValue(valorMercadoria),
						 objValorPrejuizoName:"sinistroDoctoServico:"+tr.rowIndex+".vlPrejuizo",
						 objTipoPrejuizoName:"sinistroDoctoServico:"+tr.rowIndex+".tpPrejuizo"});
			    	xmit({serviceDataObjects:[sdo]});
			    }
			};
		}
	}
	
	function validaValores_cb(data, error){
		if (data.mensagemErro){
			alert(data.mensagemErro);
			setFocus(document.getElementById(data.objValorPrejuizoName));
		} else if (data.flagValoresIguais && data.flagValoresIguais=="true"){
			setElementValue(data.objTipoPrejuizoName, "T");
			setDisabled(document.getElementById(data.objValorPrejuizoName), true);
		}
	}

	function processaGrid_cb(data, error) {
			
		var gridDef = document.getElementById("sinistroDoctoServico.dataTable").gridDefinition;
		var objTipoPrejuizo;
		var objValorPrejuizo;
		var valorMercadoria;
		
		for(var i = 0; i < gridDef.currentRowCount; i++) {
			objTipoPrejuizo = document.getElementById("sinistroDoctoServico:"+i+".tpPrejuizo");
			valorPrejuizo = document.getElementById("sinistroDoctoServico:"+i+".vlPrejuizo");
			vlMercadoria = document.getElementById("sinistroDoctoServico:"+i+".vlMercadoriaHidden");
			var flagRIM = document.getElementById("sinistroDoctoServico:"+i+".flagRIM"); 

			if (flagRIM.value == 'S') {
				setDisabled(valorPrejuizo, true);
				setDisabled(objTipoPrejuizo, true);

			} else {
			
				if (objTipoPrejuizo.value=="S" || objTipoPrejuizo.value=="T") {
					setDisabled(valorPrejuizo, true);
				} else {
					setDisabled(valorPrejuizo, false);
				}
			}
		}
	}
	
	function onRowClick() {
		return false;
	}
	
	// callback do store
	function gridStore_cb(data, error) {
		store_cb(data, error);
	
	}
	
	function onCloseButtonClick() {
		window.close();
	}

	// seta os valores iniciais da tela
	function pageLoad() {
		onPageLoad();
	    setMasterLink(this.document, true);	
	    if(verifyMotherIdentity()==true) {
		    setElementValue('nrProcessoSinistro', dialogArguments.getElementValue('nrProcessoSinistro'));
	   	    findManifestoValues();
   	    }
	}

	// busca os dados do manifesto	
	function findManifestoValues() {
		var remoteCall = {serviceDataObjects:new Array()}; 
		var data = {idProcessoSinistroManifesto: getElementValue('idProcessoSinistroManifesto')};
		var sdo = createServiceDataObject('lms.seguros.manterProcessosSinistroAction.findNrManifestoByIdProcessoSinistroManifesto', 'findManifestoValues', data);
		remoteCall.serviceDataObjects.push(sdo);
		xmit(remoteCall);
	}
	
	// funcao de callback
	function findManifestoValues_cb(data) {
		if (data!=undefined) {
			setElementValue('nrManifesto',       data.nrManifesto);
			setElementValue('sgFilialManifesto', data.sgFilialManifesto);
			var fb = buildFormBeanFromForm(document.forms[0]);
			sinistroDoctoServicoGridDef.executeSearch(fb);
		}
	}
	
	// verifica se o item chamador não possui id (está em memória) e fecha a tela.
	function verifyMotherIdentity() {
		if (getElementValue('idProcessoSinistroManifesto')=='undefined') { // neste caso <undefined> é entre string mesmo
			alert(LMS_22008);
			this.close();
			return false;
		}
		return true;
	}
	
</script>

