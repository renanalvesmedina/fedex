<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>

	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		setDisabled("controleCarga.nrControleCarga", true);
		disableNrSmp(true);
		mostraEscondeBotaoFechar();
		carregaDadosSmp();
		
		var idProcessoWorkflow = getIdProcessoWorkflow(); 
		if (idProcessoWorkflow!=undefined) {
		
			var data = new Object();
			data.idProcessoWorkflow = idProcessoWorkflow;
			var sdo = createServiceDataObject("lms.sgr.consultarConteudosVeiculoManifestosAction.findByIdProcessoWorkflow", "carregaPagina", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function carregaPagina_cb(data, error) {
	 	if (error!=undefined) {
	 		alert(error);
	 		return false;
	 	}
	 	
		setMasterLink(document, true);
		
    	setDisabled('smp.filial.idFilial', true);
    	setDisabled('smp.idSolicMonitPreventivo', true);
		
		if (data.controleCarga!=undefined) {
			setElementValue("controleCarga.idControleCarga", data.controleCarga.idControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data.controleCarga.filialByIdFilialOrigem.idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data.controleCarga.filialByIdFilialOrigem.sgFilial);
			setElementValue("controleCarga.nrControleCarga", setFormat(document.getElementById("controleCarga.nrControleCarga"), data.controleCarga.nrControleCarga));
			document.getElementById("controleCarga.idControleCarga").masterLink=true;			
			document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").masterLink=true;
			document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").masterLink=true;
			document.getElementById("controleCarga.nrControleCarga").masterLink=true;
			setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true); //Desabilita a sigla da filial do CC
			setDisabled("controleCarga.idControleCarga", true); //Desabilita o nro e lupa do CC
		}

		if (data.meioTransporteRodoviario!=undefined) {
			setElementValue("meioTransporteRodoviario.idMeioTransporte", data.meioTransporteRodoviario.idMeioTransporte);
			setElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador", data.meioTransporteRodoviario.meioTransporte.nrIdentificador);
			document.getElementById("meioTransporteRodoviario.idMeioTransporte").masterLink=true;
			document.getElementById("meioTransporteRodoviario.meioTransporte.nrIdentificador").masterLink=true;
			setDisabled("meioTransporteRodoviario.idMeioTransporte", true);
			setDisabled("meioTransporteRodoviario.meioTransporte.nrIdentificador", true);
		}
		
		if (data.meioTransporteRodoviario2!=undefined) {
			setElementValue("meioTransporteRodoviario2.idMeioTransporte", data.meioTransporteRodoviario2.idMeioTransporte);
			setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota", data.meioTransporteRodoviario2.meioTransporte.nrFrota);
			document.getElementById("meioTransporteRodoviario2.idMeioTransporte").masterLink=true;
			document.getElementById("meioTransporteRodoviario2.meioTransporte.nrFrota").masterLink=true;
			setDisabled("meioTransporteRodoviario2.idMeioTransporte", true);
			setDisabled("meioTransporteRodoviario2.meioTransporte.nrFrota", true);
		}
		
		onPageLoad_cb(data, error);
		onConsultarButtonClick(document.getElementById("formConsultarConteudosVeiculoManifestos"));
	} 
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

</script>

<adsm:window service="lms.sgr.consultarConteudosVeiculoManifestosAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/sgr/consultarConteudosVeiculoManifestos" idProperty="idSolicMonitPreventivo" id="formConsultarConteudosVeiculoManifestos">

		<adsm:hidden property="idSolicMonitPreventivo" />
		<adsm:lookup dataType="text" property="smp.filial" 
					idProperty="idFilial" 
					criteriaProperty="sgFilial" 
					service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupFilial" 
					action="/municipios/manterFiliais" 
					onchange="return onFilialSmpChange(this);" 
					onDataLoadCallBack="onFilialSmpCallback" 
					required="false" 
					label="smp" 
					labelWidth="20%" 
					width="30%" size="3" 
					maxLength="3" 
					picker="false" 
					serializable="false">
			<adsm:lookup dataType="integer" 
						property="smp" 
						idProperty="idSolicMonitPreventivo" 
						criteriaProperty="solicMonitPreventivo.nrSmp" 
						service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupSmp" 
						action="/sgr/manterSMP" 
						onchange="return onSmpChange(this);"
						cmd="list" 
						afterPopupSetValue="onSmpPopupSetValue"  
						maxLength="8" size="8" mask="00000000" 
						onDataLoadCallBack="smpCallback">
				<adsm:propertyMapping criteriaProperty="smp.filial.idFilial" modelProperty="solicMonitPreventivo.filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="smp.filial.sgFilial" modelProperty="solicMonitPreventivo.filial.sgFilial"/>
				<adsm:propertyMapping relatedProperty="dhGeracao" modelProperty="dhGeracao" />
				<adsm:propertyMapping modelProperty="vlSmp" relatedProperty="vlSmp" />
				<adsm:propertyMapping modelProperty="moedaPais.moeda.siglaSimbolo" relatedProperty="moedaPais.moeda.siglaSimbolo" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox dataType="JTDateTimeZone" property="dhGeracao" label="dataGeracao" disabled="true" labelWidth="20%" width="30%" serializable="false" picker="false" />
	
		<adsm:lookup dataType="text" 
					property="controleCarga.filialByIdFilialOrigem" 
					idProperty="idFilial" 
					criteriaProperty="sgFilial" 
					service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupFilial" 
					action="/municipios/manterFiliais" 
					onchange="return onFilialControleCargaChange();" 
					onDataLoadCallBack="disableNrControleCarga" 
					required="false" label="controleCargas" 
					labelWidth="20%" width="30%" size="3" 
					maxLength="3" 
					picker="false" 
					serializable="false" >
			<adsm:lookup dataType="integer" 
						property="controleCarga" 
						idProperty="idControleCarga" 
						criteriaProperty="nrControleCarga" 
						service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupControleCarga" 
						action="/carregamento/manterControleCargas" 
						cmd="list" 
						onchange="return onControleCargaChange()" 
						onPopupSetValue="onControleCargaPopupSetValue" 
						onDataLoadCallBack="onControleCargaCallback" 
						maxLength="8" 
						size="8" 
						mask="00000000" 
						popupLabel="pesquisarControleCarga">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" inlineQuery="false" disable="false"/>
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado2.nrFrota"          criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"        blankFill="false" inlineQuery="false" disable="false"/>
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador"  criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" blankFill="false" inlineQuery="false" disable="false"/>
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado2.idMeioTransporte" criteriaProperty="meioTransporteRodoviario.idMeioTransporte"               blankFill="false" inlineQuery="false" disable="false"/>
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.idMeioTransporte" criteriaProperty="meioTransporteRodoviario.idMeioTransporte"               blankFill="false" inlineQuery="false" disable="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrFrota"          relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"        blankFill="false" />
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador"  relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" blankFill="false" />
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.idMeioTransporte" relatedProperty="meioTransporteRodoviario.idMeioTransporte"               blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="moedaPais.moeda.siglaSimbolo" label="valorControleCargas" size="7" labelWidth="20%" width="30%" disabled="true" serializable="false" >
			<adsm:textbox dataType="currency" property="vlSmp" size="20" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:hidden property="meioTransporte.tpSituacao" value="A" />
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupMeioTransporteRodoviario" picker="false" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota" label="meioTransporte" labelWidth="20%" width="80%" size="8" serializable="false" maxLength="6">

			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" disable="false" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao" />
			
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" service="lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupMeioTransporteRodoviario" picker="true" maxLength="25" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador" size="20" required="false">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" buttonType="findButton" onclick="onConsultarButtonClick(this.form);" disabled="false" />
			<adsm:button  buttonType="storeButton" caption="limpar" onclick="limpaTela()" disabled="false"/>
		</adsm:buttonBar>

		<script>
			var LMS_11008 = '<adsm:label key="LMS-11008"/>';
			var LMS_11012 = '<adsm:label key="LMS-11012"/>';
		</script>
	</adsm:form>

	<adsm:grid property="anyProperty" idProperty="rowId" selectionMode="none" service="lms.sgr.consultarConteudosVeiculoManifestosAction.findPages" rowCountService="lms.sgr.consultarConteudosVeiculoManifestosAction.getRows" onRowClick="onRowClick" onPopulateRow="onPopulateRow" scrollBars="horizontal" gridHeight="220">
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="sgFilialManifesto" dataType="text" title="manifesto" width="20" />
			<adsm:gridColumn property="nrManifesto" dataType="integer" title="" width="60" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgFilialCc" dataType="text" title="controleCarga" width="20" />
			<adsm:gridColumn title="" property="nrControleCarga" width="100" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="tipoOperacao" property="tpOperacao" width="130" />
		<adsm:gridColumn title="tipoManifesto" property="tpManifesto" width="130" />
		<adsm:gridColumn title="status" property="tpStatus" width="130" isDomain="true" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valor" width="30" />
			<adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="20" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlTotalManifesto" width="120" align="right" dataType="currency" />
		<adsm:gridColumn title="documentosServico" property="documentosServico" image="/images/popup.gif" openPopup="true" width="160" popupDimension="790,520" align="center" link="javascript:openDocumentos" linkIdProperty="idManifesto" />
		<adsm:editColumn title="hidden" property="tpColetaViagem" dataType="text" field="hidden" width="" />
		<adsm:editColumn title="hidden" property="idControleCarga" dataType="text" field="hidden" width="" />
		<adsm:editColumn title="hidden" property="idManifesto" dataType="text" field="hidden" width="" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	

	function carregaDadosSmp() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			var url = new URL(parent.location.href);
			var idSolicMonitPreventivo = url.parameters['idSolicMonitPreventivo'];
			
			if (idSolicMonitPreventivo!=undefined) {
				setMasterLink(this.document, true);
			 	document.getElementById("smp.filial.idFilial").masterLink = "true";
				document.getElementById("smp.filial.sgFilial").masterLink = "true";
				document.getElementById("smp.idSolicMonitPreventivo").masterLink = "true";
				document.getElementById("smp.solicMonitPreventivo.nrSmp").masterLink = "true";
				document.getElementById("dhGeracao").masterLink = "true";
				document.getElementById("vlSmp").masterLink = "true";
				document.getElementById("controleCarga.idControleCarga").masterLink = "true";
				document.getElementById("controleCarga.nrControleCarga").masterLink = "true";
				document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").masterLink = "true";
				document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").masterLink = "true";
				document.getElementById("meioTransporteRodoviario.idMeioTransporte").masterLink = "true";
				document.getElementById("meioTransporteRodoviario2.meioTransporte.nrFrota").masterLink = "true";				
				document.getElementById("meioTransporteRodoviario.meioTransporte.nrIdentificador").masterLink = "true";				
							
		    	setDisabled('controleCarga.idControleCarga', true);
		    	setDisabled('controleCarga.filialByIdFilialOrigem.idFilial', true);
		    	setDisabled('meioTransporteRodoviario.idMeioTransporte', true);
		    	setDisabled('meioTransporteRodoviario2.idMeioTransporte', true);
			
				var map = new Array();
				setNestedBeanPropertyValue(map, "idSolicMonitPreventivo", idSolicMonitPreventivo);
				
			    var sdo = createServiceDataObject("lms.sgr.consultarConteudosVeiculoManifestosAction.findSolicMonitPreventivo", "resultadoCarregaDadosSmp", map);
			    xmit({serviceDataObjects:[sdo]});		
			}
		}
	}
	
	/**
	 * Carrega o cliente que foi selecionado na tela anterior
	 */
	 function resultadoCarregaDadosSmp_cb(data, error) {
		if (error!=undefined) {
			alert(error);
			return false;
		}
    	setElementValue('smp.filial.idFilial', data.filial.idFilial);
    	setElementValue('smp.filial.sgFilial', data.filial.sgFilial);
    	setElementValue('smp.idSolicMonitPreventivo', data.idSolicMonitPreventivo);
    	setElementValue('smp.solicMonitPreventivo.nrSmp',  setFormat('smp.solicMonitPreventivo.nrSmp', data.nrSmp));
   	    setElementValue('dhGeracao', setFormat('dhGeracao', data.dhGeracao));

    	setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', data.controleCarga.filialByIdFilialOrigem.idFilial);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.controleCarga.filialByIdFilialOrigem.sgFilial);
		setElementValue('controleCarga.idControleCarga', data.controleCarga.idControleCarga);
		setElementValue('controleCarga.nrControleCarga', setFormat('controleCarga.nrControleCarga', data.controleCarga.nrControleCarga));
		setElementValue('vlSmp', setFormat('vlSmp', data.vlSmp));
		if (getNestedBeanPropertyValue(data, "moedaPais.moeda.siglaSimbolo") != undefined) {
			setElementValue("moedaPais.moeda.siglaSimbolo", getNestedBeanPropertyValue(data, "moedaPais.moeda.siglaSimbolo"));
		}

		if (data.meioTransporte!=undefined) {
			setElementValue('meioTransporteRodoviario.idMeioTransporte', data.meioTransporte.idMeioTransporte);
			setElementValue('meioTransporteRodoviario.meioTransporte.nrIdentificador', data.meioTransporte.nrIdentificador);
			setElementValue('meioTransporteRodoviario2.meioTransporte.nrFrota', data.meioTransporte.nrFrota);
		}

    	setDisabled('smp.filial.idFilial', true);
    	setDisabled('smp.idSolicMonitPreventivo', true);
    	setDisabled('controleCarga.idControleCarga', true);
    	setDisabled('controleCarga.filialByIdFilialOrigem.idFilial', true);
    	setDisabled('meioTransporteRodoviario.idMeioTransporte', true);
    	setDisabled('meioTransporteRodoviario2.idMeioTransporte', true);
	}

	// vetor auxiliar para decidir a chamada ao click em 'documemtos'
	var gridVector = new Array();

	function onRowClick() {
		return false;
	}

	function onConsultarButtonClick(frm) {
		if (getElementValue('smp.idSolicMonitPreventivo')=='' && getElementValue('meioTransporteRodoviario.idMeioTransporte')=='' && getElementValue('controleCarga.idControleCarga')=='' ) {
			alert(LMS_11008);
			setFocusOnFirstFocusableField(document);
			return;
		}
		gridVector = new Array();
		findButtonScript('anyProperty', frm);		
	}

	function limpaTela(){
		cleanButtonScript(this.document);
		setDisabled("controleCarga.nrControleCarga", true);
		disableNrSmp(true);
	}

	/*************************************************************************************
	 * Funções referentes ao botão Documentos
	 *************************************************************************************/
	function openDocumentos(id) {		
		var idManifesto = gridVector[id].idManifesto;
		var nrManifesto = gridVector[id].nrManifesto;
		var sgFilialOrigem = gridVector[id].sgFilialManifesto;

		if (nrManifesto==undefined) {
			nrManifesto = '';
		}

		// se linha de coleta
		if (gridVector[id].tpColetaViagem == "C") {
			
			var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;					
			showModalDialog("recepcaoDescarga/descarregarVeiculo.do?cmd=documentosColetas" + dadosURL, window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");

		} else {
			// chamar tela que lista entregas
			var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;		
			showModalDialog("/recepcaoDescarga/descarregarVeiculo.do?cmd=documentos" + dadosURL , window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");			
		}		
	}

	function onPopulateRow(tr, data) {
		var idManifesto   = document.getElementById("anyProperty:"+tr.rowIndex+".idManifesto").value;		
		var	tpColetaViagem = document.getElementById("anyProperty:"+tr.rowIndex+".tpColetaViagem").value;
		var idControleCarga = document.getElementById("anyProperty:"+tr.rowIndex+".idControleCarga").value;

		var nrManifesto       = data.nrManifesto;
		var sgFilialManifesto = data.sgFilialManifesto;
	
		gridVector[tr.rowIndex] = {tpColetaViagem:tpColetaViagem, idControleCarga:idControleCarga, idManifesto:idManifesto, nrManifesto:nrManifesto, sgFilialManifesto:sgFilialManifesto };
	}


	/*************************************************************************************
	 * Funções de Controle de Cargas
	 *************************************************************************************/
	function onFilialControleCargaChange() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			setDisabled("controleCarga.nrControleCarga", true);
			resetValue(document.getElementById("controleCarga.idControleCarga"));
			resetValue(document.getElementById("meioTransporteRodoviario.idMeioTransporte"));
			return true;
			
		} else {
			setDisabled("controleCarga.nrControleCarga", false);
			return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		}
	}

	function disableNrControleCarga_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data.length==0) {
			setDisabled("controleCarga.nrControleCarga", false);
		}
		return controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}

	function findLookupControleCarga(data) {
		var sdo = createServiceDataObject("lms.sgr.consultarConteudosVeiculoManifestosAction.findLookupControleCarga", "onControleCargaCallback", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function onControleCargaPopupSetValue(data) {
		setDisabled("controleCarga.nrControleCarga", false);
		var tpStatusControleCarga = data.tpStatusControleCarga.value;
		if (tpStatusControleCarga == 'CA' || tpStatusControleCarga == 'FE') {
			alert(LMS_11012);
			resetValue("controleCarga.idControleCarga");
			if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
				setDisabled("controleCarga.nrControleCarga", true);
			}
			setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
			return false;
		}
	}
	
	function onControleCargaCallback_cb(data, error){
		if (error != undefined) {
			alert(error);
			return false;
		}
		var retorno = controleCarga_nrControleCarga_exactMatch_cb(data);
		if (retorno==true) {
			setDisabled("controleCarga.nrControleCarga", false);
		}
	}
	
	function onControleCargaChange() {
		if (getElementValue('controleCarga.nrControleCarga')=='')
			resetValue(document.getElementById("meioTransporteRodoviario.idMeioTransporte"));
	
		return controleCarga_nrControleCargaOnChangeHandler();
	}

	/*************************************************************************************
	 * Funções de SMP
	 *************************************************************************************/
	function onFilialSmpChange(e) {
		if (e.value=='') { 
			disableNrSmp(true);
			resetValue(document);
			setDisabled('controleCarga.nrControleCarga', true);
			return true;
			
		} else {
			disableNrSmp(false);
			return smp_filial_sgFilialOnChangeHandler();
		}
	}
	
	function onSmpChange(e) {
		if (e.value=='') {
			var sgFilial=getElementValue('smp.filial.sgFilial');
			var idFilial=getElementValue('smp.filial.idFilial');
			resetValue(document);
			setElementValue('smp.filial.sgFilial', sgFilial);
			setElementValue('smp.filial.idFilial', idFilial);
			setDisabled('controleCarga.nrControleCarga', true);
			return true;
		} else {
			return smp_solicMonitPreventivo_nrSmpOnChangeHandler();
		}	
	}
		
	function disableNrSmp(disable) {
		document.getElementById('smp.solicMonitPreventivo.nrSmp').disabled = disable;
		if (disable==true) {
			resetValue(document.getElementById("smp.solicMonitPreventivo.nrSmp"));
		}
	}
	
	function onFilialSmpCallback_cb(data, error) {
		if (data.length==0) 
			disableNrSmp(false);
		return smp_filial_sgFilial_exactMatch_cb(data);
	}

	function smpCallback_cb(data, error) {		
		var r = smp_solicMonitPreventivo_nrSmp_exactMatch_cb(data);
		if (error==undefined) {
			if (data.length==1) {
				setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', data[0].controleCarga.idFilial);
				setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data[0].controleCarga.sgFilial);
				setElementValue('controleCarga.nrControleCarga', data[0].controleCarga.nrControleCarga);
				setDisabled('controleCarga.nrControleCarga', false);
				lookupChange({e:document.getElementById("controleCarga.idControleCarga"), forceChange:true});
			}
		} else {
			resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
			resetValue('controleCarga.idControleCarga');
			setDisabled('controleCarga.nrControleCarga', true);
			setFocus('smp.solicMonitPreventivo.nrSmp');
		}
		return r;		
	}

	function onSmpPopupSetValue(data) {		
		setElementValue('smp.filial.sgFilial', data.filial.sgFilial);
		setElementValue('smp.solicMonitPreventivo.nrSmp', setFormat('smp.solicMonitPreventivo.nrSmp', data.nrSmp));
		setDisabled('smp.solicMonitPreventivo.nrSmp', false);
		
		setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', data.controleTrecho.controleCarga.filialByIdFilialOrigem.idFilial);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.controleTrecho.controleCarga.filialByIdFilialOrigem.sgFilial);
		setElementValue('controleCarga.nrControleCarga', data.controleTrecho.controleCarga.nrControleCarga);
		setDisabled('controleCarga.nrControleCarga', false);
		lookupChange({e:document.getElementById("smp.idSolicMonitPreventivo"), forceChange:true});
	}
	
	/**
	 * Mostra ou esconde o botão Fechar caso seja uma lookup ou nao.
	 */
	function mostraEscondeBotaoFechar(){
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}
</script>