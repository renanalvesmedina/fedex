<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirColetasEntregasFrotaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/coleta/emitirColetasEntregasFrota">
		<adsm:hidden property="meioTransporte.tpSituacao" value="A" />

		<adsm:lookup property="meioTransporteRodoviario2" idProperty="idMeioTransporte" criteriaProperty="nrFrota" 
					 picker="false"
					 dataType="text" 
					 label="meioTransporte" labelWidth="15%" width="85%" 
					 size="8" serializable="false" maxLength="6" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" 
					 service="lms.coleta.emitirColetasEntregasFrotaAction.findLookupMeioTransporte">					 
			<adsm:lookup property="meioTransporteRodoviario" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"					 
						 picker="true" maxLength="25"					 
						 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" 					 
						 service="lms.coleta.emitirColetasEntregasFrotaAction.findLookupMeioTransporte" 
						 dataType="text" size="20" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.nrFrota" modelProperty="nrFrota" disable="false"/>
				<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="tpSituacao"/>			
				<adsm:propertyMapping relatedProperty= "meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty= "meioTransporteRodoviario2.nrFrota" modelProperty="nrFrota" />
			</adsm:lookup>
					 
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.nrIdentificador" modelProperty="nrIdentificador" disable="false"/>
			<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty= "meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty= "meioTransporteRodoviario.nrIdentificador" modelProperty="nrIdentificador" />
		</adsm:lookup>

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
        <adsm:lookup dataType="text" 
			        property="controleCarga.filialByIdFilialOrigem"  
			        idProperty="idFilial" 
			        criteriaProperty="sgFilial" 
					service="lms.coleta.emitirColetasEntregasFrotaAction.findLookupFilial" 
					action="/municipios/manterFiliais" 
    				onchange="return sgFilialOnChangeHandler();"
    				onDataLoadCallBack="disableNrControleCarga"
 					label="controleCargas" 
 					popupLabel="pesquisarFilial"
					width="85%" 
					labelWidth="15%" 
					size="3" 
					maxLength="3" 
					picker="false" 
					serializable="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />					
			<adsm:lookup dataType="integer" 
						 property="controleCarga" 
						 idProperty="idControleCarga" 
						 criteriaProperty="nrControleCarga" 
						 service="lms.coleta.emitirColetasEntregasFrotaAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas"
						 popupLabel="pesquisarControleCarga"
						 cmd="list" mask="00000000"
						 onPopupSetValue="loadDataByNrControleCarga" 
						 onDataLoadCallBack="loadDataByNrControleCarga" 
						 onchange="return checkValueControleCarga(this.value)"
						 disabled="false" 
						 maxLength="8" size="8">
				<adsm:hidden property="tpControleCarga" value="C"/>
				<adsm:propertyMapping criteriaProperty="tpControleCarga" modelProperty="tpControleCarga"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"modelProperty="meioTransporteByIdTransportado.idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.nrFrota"        modelProperty="meioTransporteByIdTransportado.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.nrIdentificador" modelProperty="meioTransporteByIdTransportado.nrIdentificador" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:textbox label="dataConsulta" dataType="JTDate" property="dtConsulta" labelWidth="15%" width="85%" required="true"/>

		<adsm:multicheckbox 
			texts="coleta|entrega|" 
			cellStyle="vertical-align: bottom;"
			property="blColeta|blEntrega|" 
			label="processos"  
			labelWidth="15%" width="85%"
			onclick="onChangeCheckColeta()"
		/>
		
		
		<adsm:checkbox property="blSomenteRealizados" label="somenteRealizados" labelWidth="15%" width="85%"/>

		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="servico" property="servico.idServico" renderOptions="true"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.emitirColetasEntregasFrotaAction.findServico" 
					   labelWidth="15%" width="85%" boxWidth="230"
					   onlyActiveValues="true" onchange="setDsServico();"/>
		<adsm:hidden property="dsTpPedidoColeta" />
		<adsm:combobox label="tipoColeta" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" labelWidth="15%" width="85%" renderOptions="true" onchange="setDsTipoColeta();"/>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="processo"/>
			<adsm:button caption="limpar" id="limpar" onclick="newItem(this.document);" buttonType="resetButton" disabled="false"/>			
		</adsm:buttonBar>
		<script>
			var LMS_02024 = '<adsm:label key="LMS-02024"/>'
		</script>
	</adsm:form>
	
	<adsm:grid idProperty="id" property="processo"  selectionMode="none" gridHeight="130" unique="true" scrollBars="horizontal" rows="5" onRowClick="onRowClick" >
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilialControleCarga" title="controleCarga" width="50" />	
			<adsm:gridColumn property="nrControleCarga"       title=""  width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="processo" title="processo" width="68"/>
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
		<adsm:gridColumn property="sgFilialColeta" title="coleta"  width="35" />	   
		<adsm:gridColumn property="nrColeta" title="" align="right" width="50" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="tpDocumentoServico" title="documentoServico" width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
		<adsm:gridColumn property="sgFilialDocumentoServico" title="" width="50" />
		<adsm:gridColumn property="nrDocumentoServico"       title="" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="dhEmissao" title="dataColetaEmissaoDocto" dataType="JTDateTimeZone" width="140" align="center"/>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
		<adsm:gridColumn property="sgFilialManifesto" title="manifesto" width="50" />
		<adsm:gridColumn property="nrManifesto"       title=""  width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="nmClienteDestinatario" title="cliente" width="200"/>
		<adsm:gridColumn property="qtVolumes" title="volumes" width="65" align="right"/>
		<adsm:gridColumn property="peso" title="peso" width="70" align="right" unit="kg" dataType="decimal" mask="#,##0.000" />
		<adsm:gridColumn property="sgSimboloMoeda" dataType="text" title="valor" width="50"/>
		<adsm:gridColumn property="valor" dataType="currency" title="" width="80" align="right"/>
		<adsm:gridColumn property="tpStatus" title="status" width="130"/>
		<adsm:gridColumn property="qtRetornos" title="retornos" width="60" align="right"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="reportButton" onclick="onReportButtonClick()" disabled="false"/>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton"/>
		</adsm:buttonBar>					
	</adsm:grid>
</adsm:window>

<script>

	var isPopup = window.dialogArguments && window.dialogArguments.window;
	if (isPopup) {
		setDisabled('btnFechar',false);
	} else {
		setDisplay('btnFechar');
	}	
 

	function onTestarProcessoClick() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.emitirColetasEntregasFrotaAction.testarProcesso", null, data);
    	xmit({serviceDataObjects:[sdo]});	
	}


	function validateTab() {
		if (document.getElementById("blColeta").checked == false && document.getElementById("blEntrega").checked == false) {
			alert(LMS_02024);
			return false;
		} 
		return validateTabScript(document.forms);
	}
	
	function onReportButtonClick() {
		reportButtonScript('lms.coleta.emitirColetasEntregasFrotaAction', 'openPdf', this.document.forms[0]);
	}
	
	function newItem(form) {
		cleanButtonScript(this.document);
		limpaTela();	
	}
	
	function limpaTela() {
		processoGridDef.resetGrid();			
   }
	
	
	function pageLoad_cb() {
		newPage();
		setDisabled('controleCarga.nrControleCarga', true);
	}
	
	function initWindow(event) {
		if(event.name=="cleanButton_click") {
			newPage();
			setDisabled('controleCarga.nrControleCarga', true);
		}
	}
	
	function newPage() {

		getDataAtual();

		// url trata os parâmetros passados na url (caso seja uma popup)
		var url = new URL(document.location.href);
		
		var idControleCarga = url.parameters.idControleCarga;
		var blColeta = true;
		var blEntrega = true;
		var blSomenteRealizados = true;
		
		if (idControleCarga!=undefined) {
		
			// 1) chama o find do controle de carga e veiculo
			getControleCarga(idControleCarga);
			
			// 2) seta valoes de checks e desabilita, caso tenham sido passados como parâmetro
			if (url.parameters.blColeta!=undefined) {
				blColeta = url.parameters.blColeta;
				setDisabled("blColeta", true);
			} 
			
			if (url.parameters.blEntrega!=undefined) {
				blEntrega = url.parameters.blEntrega;
				setDisabled("blEntrega", true);				
			}
			
			if (url.parameters.blSomenteRealizados!=undefined) {
				blSomenteRealizados = url.parameters.blSomenteRealizados;
				setDisabled("blSomenteRealizados", true);
			}
		}

		// se não é popup, seta todos os checkboxes e obtém a data atual como sugestão
		setElementValue("blColeta", blColeta);
		setElementValue("blEntrega", blEntrega);
		setElementValue("blSomenteRealizados", blSomenteRealizados);
		setDisabled("limpar", false);
		setDisabled("reportButton", false);
	}
	
	function onRowClick() {
		return false;
	}
	
	function getDataAtual() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.emitirColetasEntregasFrotaAction.getDate", "getDataAtual", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function getDataAtual_cb(data, error) {
		setElementValue("dtConsulta", setFormat("dtConsulta", getNestedBeanPropertyValue(data, "dtConsulta")));
	}

	// obtem o controle de cargas através do id (utilizado quando esta é uma popup)	
	function getControleCarga(idControleCarga) {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'idControleCarga', idControleCarga);
		var sdo = createServiceDataObject("lms.coleta.emitirColetasEntregasFrotaAction.findControleCargaById", "getControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// seta controle de cargas e veiculo como masterLink
	function getControleCarga_cb(data, error) {

		setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data, "controleCarga.idControleCarga"));
		setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data, "controleCarga.nrControleCarga"));
		setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', getNestedBeanPropertyValue(data, "controleCarga.filialByIdFilialOrigem.idFilial"));
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, "controleCarga.filialByIdFilialOrigem.sgFilial"));
		setElementValue('controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia', getNestedBeanPropertyValue(data, "controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"));
		setElementValue('meioTransporteRodoviario.idMeioTransporte', getNestedBeanPropertyValue(data, "controleCarga.meioTransporteByIdTransportado.idMeioTransporte"));
		setElementValue('meioTransporteRodoviario2.nrFrota', getNestedBeanPropertyValue(data, "controleCarga.meioTransporteByIdTransportado.nrFrota"));
		setElementValue('meioTransporteRodoviario.nrIdentificador', getNestedBeanPropertyValue(data, "controleCarga.meioTransporteByIdTransportado.nrIdentificador"));

		putMasterLink('controleCarga.idControleCarga');		
		putMasterLink('controleCarga.nrControleCarga');		
		putMasterLink('controleCarga.filialByIdFilialOrigem.idFilial');
		putMasterLink('controleCarga.filialByIdFilialOrigem.sgFilial');
		putMasterLink('controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia');
		putMasterLink('meioTransporteRodoviario.idMeioTransporte');		
		putMasterLink('meioTransporteRodoviario2.idMeioTransporte');		
		putMasterLink('meioTransporteRodoviario2.nrFrota');		
		putMasterLink('meioTransporteRodoviario.nrIdentificador');		
		
		findButtonScript('processo', document.forms[0]);
	}

	/**
	 * Seta como masterLink e desabilita campo
	 */	
	function putMasterLink(objName) {
		document.getElementById(objName).masterLink="true";
		setDisabled(objName, true);
	}
	
	
	function sgFilialOnChangeHandler() {

		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia");
		} else {
			disableNrControleCarga(false);

		}
		
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.nrControleCarga", disable);
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	function loadDataByNrControleCarga(data) {
    	setDisabled("controleCarga.nrControleCarga", false);      	
	}
	
	function loadDataByNrControleCarga_cb(data, error){
		controleCarga_nrControleCarga_exactMatch_cb(data);
	}
	
	function checkValueControleCarga(valor) {
		return controleCarga_nrControleCargaOnChangeHandler();
	}

	function setDsServico(){
		var comboBoxServico = document.getElementById("servico.idServico");
		setElementValue("servico.dsServico", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
	
	function setDsTipoColeta(){
		var comboBoxServico = document.getElementById("tpPedidoColeta");
		setElementValue("dsTpPedidoColeta", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
	
	function onChangeCheckColeta(){
		if(getElement("blColeta").checked){
			setDisabled("servico.idServico", false);  
			setDisabled("tpPedidoColeta", false);  
		}else{
			setDisabled("servico.idServico", true);  
			setDisabled("tpPedidoColeta", true); 
			setElementValue("servico.idServico", "");  
			setElementValue("tpPedidoColeta", "");
		}
	}

</script>